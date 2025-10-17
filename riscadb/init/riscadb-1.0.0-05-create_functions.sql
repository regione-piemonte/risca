/* *****************************************************
 * Copyright Regione Piemonte - 2025
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************/

-- DROP FUNCTION utility_fnc_aggiorna_sequence(int4);

CREATE OR REPLACE FUNCTION utility_fnc_aggiorna_sequence(p_operazione integer DEFAULT 0)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
declare

/*********************************************************************************************************************
   NOME:     UTILITY_fnc_aggiorna_sequence
   SCOPO:    La funzione permette di listare o ricalcolare le sequence
   UTILIZZO: di SERVIZIO - Ricalcolo sequence in base ai valori contenuti nelle relative tabelle

   REVISIONE:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   0.1        10/05/2023                   Creazione della funzione iniziale
   0.2        10/05/2023                   Aggiunta la gestione delle sequence di 'codifica applicativa'
   0.21       11/05/2023                   Corretto il campo letto in risca_r_uso_ridaum - id_uso_ridaum
   0.22       30/05/2023                   Aggiornamento sequenza lista
   0.23       31/05/2023                   Gestite le categorie delle sequence (migliore leggibilità)
   0.30       01/06/2023                   Corretto impostazione sequence errata. alter SEQUENCE con Select Setval()
   0.30b      01/06/2023                   Reimposto la sequence solo se necessario (valore attuale disallineato)
   0.40       05/06/2023                   Nel caso il valore calcolato sia 0; la sequence deve essere 1 - set_val() con flag 'false' in modo che non venga staccato un valore al primo utilizzo
   0.50       26/02/2024                   Gestite le sequence dei delegati
   1.0.0      26/02/2024                   Gestite le sequence dei pagamenti PagoPa e solleciti
   1.1.0      04/03/2024                   Gestite le sequence delle tabelle di configurazione gestite da applicativo
   1.1.1      07/06/2024                   Gestite le sequence delle tabelle : seq_risca_t_file_soris e seq_risca_r_dettaglio_pag_bil_acc
   1.1.2      26/06/2024                   Gestita la nuova sequence : seq_risca_r_ruolo

**********************************************************************************************************************/

esito int4 := 1;
t_msg_exc text;
t_dett_exc text;
t_hint_exc text;
--

   -- -------------------------------------------------------------------------------------------
   -- Attenzione :
   -- impostare n_operazione = 0 per fare solo la Lista
   -- impostare n_operazione = 1 per fare oltre alla lista, anche AGGIORNAMENTO delle sequence ! 
   n_operazione int4 :=0;
  -- --------------------------------------------------------------------------------------------
   n_sequence int4;  
   v_sequence text;
   v_tabella text;   
   v_campoid text;
   v_queryaggiorna  text;
   v_regola text;
   --
   n_attuale int4;
   n_calcolato int4;
   v_notice text;
   v_da_aggiornare text;
   c_regoleresoconto text := '';

begin

   n_operazione := coalesce(p_operazione,0);

   -- Descrizione Operazione effettuata
   v_notice = CASE n_operazione 
                WHEN 0 THEN 'solo lista dei valori' 
                ELSE 'AGGIORNAMENTO delle sequence !' 
              END;
   RAISE NOTICE 'UTILITY - Operazione effettuata : %', v_notice;
   RAISE NOTICE 'Data : %', current_timestamp;
   RAISE NOTICE '%', '';
  
   -- Descrizione lista
   RAISE notice '%', '|---------------------------------------------------|---------------------------------------------------------------------------------|';
   RAISE notice '%', '| valore attuale sequence                           | ultimo valore recuperato dal campo della tabella                                |';
   RAISE notice '%', '|---------------------------------------------------|---------------------------------------------------------------------------------|';
  
   -- Generazione lista info sequence
   FOR n_sequence, v_sequence, v_tabella, v_campoid IN
       with d_sequence (id_seq, des_seq, nome_tab, id_tab) 
         as ( values
                     (   0 ,'[Riscossioni]'                          ,''                                   ,''                                 ),
                     (   1, 'seq_risca_t_riscossione'                ,'risca_t_riscossione'                ,'id_riscossione'                   ),
                     (   2, 'seq_risca_r_provvedimento'              ,'risca_r_provvedimento'              ,'id_provvedimento'                 ),
                     (   3, 'seq_risca_r_riscossione_uso'            ,'risca_r_riscossione_uso'            ,'id_riscossione_uso'               ),
                     (   4, 'seq_risca_r_uso_ridaum'                 ,'risca_r_uso_ridaum'                 ,'id_uso_ridaum'                    ),
                     (   5, 'seq_risca_t_stato_debitorio'            ,'risca_t_stato_debitorio'            ,'id_stato_debitorio'               ),
                     (   6, 'seq_risca_r_annualita_sd'               ,'risca_r_annualita_sd'               ,'id_annualita_sd'                  ),
                     (   7, 'seq_risca_r_annualita_uso_sd'           ,'risca_r_annualita_uso_sd'           ,'id_annualita_uso_sd'              ),
                     (   8, 'seq_risca_r_annualita_uso_sd_ra'        ,'risca_r_annualita_uso_sd_ra'        ,'id_annualita_uso_sd_ra'           ),
                     (   9, 'seq_risca_r_rimborso'                   ,'risca_r_rimborso'                   ,'id_rimborso'                      ),
                     (  10, 'seq_risca_r_rata_sd'                    ,'risca_r_rata_sd'                    ,'id_rata_sd'                       ),
                     (  11, 'seq_risca_r_dettaglio_pag'              ,'risca_r_dettaglio_pag'              ,'id_dettaglio_pag'                 ),
                     (  12, 'seq_risca_t_file_450'                   ,'risca_t_file_450'                   ,'id_file_450'                      ),
                     (  13, 'seq_risca_r_ruolo'                      ,'risca_r_ruolo'                      ,'id_ruolo'                         ),    -- 2024_06_26 - inserito
                     (  14, 'seq_risca_t_accertamento'               ,'risca_t_accertamento'               ,'id_accertamento'                  ),
                     (  15, 'seq_risca_r_riscossione_storia_titolare','risca_r_riscossione_storia_titolare','id_riscossione_storia_titolare'   ),
                     (  16, 'seq_risca_t_soggetto'                   ,'risca_t_soggetto'                   ,'id_soggetto'                      ),
                     (  17, 'seq_risca_r_recapito'                   ,'risca_r_recapito'                   ,'id_recapito'                      ),
                     (  18, 'seq_risca_r_recapito_postel'            ,'risca_r_recapito_postel'            ,'id_recapito_postel'               ),
                     (  19, 'seq_risca_t_gruppo_soggetto'            ,'risca_t_gruppo_soggetto'            ,'id_gruppo_soggetto'               ),
                     (   0 ,'[Spedizioni]'                           ,''                                   ,''                                 ),
                     (  20, 'seq_risca_t_spedizione'                 ,'risca_t_spedizione'                 ,'id_spedizione'                    ),
                     (  21, 'seq_risca_r_spedizione_acta'            ,'risca_r_spedizione_acta'            ,'id_spedizione_acta'               ),
                     (  22, 'seq_risca_t_invio_acta'                 ,'risca_t_invio_acta'                 ,'id_invio_acta'                    ),
                     (  23, 'seq_risca_t_lotto'                      ,'risca_t_lotto'                      ,'id_lotto'                         ),
                     (  24, 'seq_risca_t_iuv'                        ,'risca_t_iuv'                        ,'id_iuv'                           ),
                     (  25, 'seq_risca_r_pagopa_lista_car_iuv'       ,'risca_r_pagopa_lista_car_iuv'       ,'id_pagopa_lista_car_iuv'          ),
                     (  26, 'seq_risca_r_pagopa_scomp_rich_iuv'      ,'risca_r_pagopa_scomp_rich_iuv'      ,'id_pagopa_scomp_rich_iuv'         ),
                     (   0 ,'[Pagamenti]'                            ,''                                   ,''                                 ),
                     (  27, 'seq_risca_t_pagamento'                  ,'risca_t_pagamento'                  ,'id_pagamento'                     ),
                     (  28, 'seq_risca_r_pag_non_propri'             ,'risca_r_pag_non_propri'             ,'id_pag_non_propri'                ),
                     (  29, 'seq_risca_t_file_poste'                 ,'risca_t_file_poste'                 ,'id_file_poste'                    ),
                     (  30, 'seq_risca_t_immagine'                   ,'risca_t_immagine'                   ,'id_immagine'                      ),
                     (  31, 'seq_risca_r_dettaglio_pag_bil_acc'      ,'risca_r_dettaglio_pag_bil_acc'      ,'id_dettaglio_pag_bil_acc'         ),
                     (   0 ,'[PagoPa]'                               ,''                                   ,''                                 ),
                     (  32, 'seq_risca_r_pagopa_lista_carico'        ,'risca_r_pagopa_lista_carico'        ,'id_pagopa_lista_carico'           ),
                     (  33, 'seq_risca_r_iuv_da_inviare'             ,'risca_r_iuv_da_inviare'             ,'id_iuv_da_inviare'                ),
                     (  34, 'seq_risca_r_pagopa_scomp_var_iuv'       ,'risca_r_pagopa_scomp_var_iuv'       ,'id_pagopa_scomp_var_iuv'          ),
                     (  35, 'seq_risca_r_soll_dati_pagopa'           ,'risca_r_soll_dati_pagopa'           ,'id_soll_dati_pagopa'              ),
                     (  36, 'seq_risca_r_soll_dati_amministr'        ,'risca_r_soll_dati_amministr'        ,'id_soll_dati_amministr'           ),
                     (  37, 'seq_risca_r_soll_dati_titolare'         ,'risca_r_soll_dati_titolare'         ,'id_soll_dati_titolare'            ),
                     (  38, 'seq_risca_r_soll_dett_vers'             ,'risca_r_soll_dett_vers'             ,'id_soll_dett_vers'                ),
                     (   0 ,'[Anagrafiche]'                          ,''                                   ,''                                 ),
                     (  39, 'seq_risca_t_delegato'                   ,'risca_t_delegato'                   ,'id_delegato'                      ),
                     (  40, 'seq_risca_r_soggetto_delega'            ,'risca_r_soggetto_delega'            ,'id_soggetto_delega'               ),
                     (  41, 'seq_risca_r_gruppo_delega'              ,'risca_r_gruppo_delega'              ,'id_gruppo_delega'                 ),
                     (   0 ,'[Elaborazioni]'                         ,''                                   ,''                                 ),
                     (  42, 'seq_risca_t_elabora'                    ,'risca_t_elabora'                    ,'id_elabora'                       ),
                     (  43, 'seq_risca_r_parametro_elabora'          ,'risca_r_parametro_elabora'          ,'id_parametro_elabora'             ),
                     (  44, 'seq_risca_r_registro_elabora'           ,'risca_r_registro_elabora'           ,'id_registro_elabora'              ),
                     (  45, 'seq_risca_s_tracciamento'               ,'risca_s_tracciamento'               ,'id_tracciamento'                  ),
                     (  46, 'seq_risca_t_file_soris'                 ,'risca_t_file_soris'                 ,'id_file_soris'                    )
            )
          --SELECT id_seq, des_seq, nome_tab, id_tab
          SELECT id_seq, des_seq, nome_tab, id_tab
            FROM d_sequence
   loop
   
     if n_sequence = 0 then
         -- riporto la categoria
         select FORMAT('| %-49s | %-79s |', v_sequence, ' ' ) into v_notice;
         RAISE notice '%',v_notice;

     else   
         -- letura del valore corrente e quello calcolato
         EXECUTE format('SELECT last_value FROM %I', v_sequence) into n_attuale;
         EXECUTE format('SELECT coalesce(max(%I),0) FROM %I', v_campoid, v_tabella) into n_calcolato;
         
         -- resoconto
         if (n_attuale =1 and n_calcolato=0) then
             v_da_aggiornare := '';
         elsif (n_attuale <> n_calcolato) then
             v_da_aggiornare := '*';
         else 
             v_da_aggiornare := '';
         end if;
         
         select FORMAT('| %-40s %8s | %-35s - %-32s %8s |%s', v_sequence, n_attuale, v_tabella, v_campoid, n_calcolato, v_da_aggiornare ) into v_notice;
         RAISE notice '%',v_notice;
                  
         -- aggiorna la sequence con il valore calcolato
         if (n_operazione > 0) then
             
             if (n_attuale =1 and n_calcolato=0) then
                 -- aggiornamento non necessario
             elseif (v_da_aggiornare = '*' and n_calcolato=0) then
                 -- reimposto la sequence con il valore iniziale = 1
                 SELECT setval(v_sequence, n_calcolato+1, false) into v_notice;
             elsif (n_attuale <> n_calcolato) then
                 -- aggiornamento della sequence con valore calcolato
                 SELECT setval(v_sequence, n_calcolato) into v_notice;
             else 
                 -- aggiornamento non necessario
             end if;
         end if;
     end if;
     
   END LOOP;

   -- Termine lista
   RAISE notice '%', '|---------------------------------------------------|---------------------------------------------------------------------------------|';
   RAISE NOTICE '%', '';


   -- Descrizione lista CONFIGURAZIONE - evolutiva : 04/03/2024
   RAISE NOTICE '%', 'Sequence di Configurazione';
   RAISE notice '%', '|---------------------------------------------------|---------------------------------------------------------------------------------|';
   RAISE notice '%', '| valore attuale sequence                           | ultimo valore recuperato dal campo della tabella                                |';
   RAISE notice '%', '|---------------------------------------------------|---------------------------------------------------------------------------------|';
  
   -- Generazione lista info sequence
   FOR n_sequence, v_sequence, v_tabella, v_campoid IN
       with d_sequence (id_seq, des_seq, nome_tab, id_tab) 
         as ( values
                     (   0 ,'[Configurazione]'                       ,''                                   ,''                                 ),
                     (   1, 'seq_risca_r_tipo_uso_regola'            ,'risca_r_tipo_uso_regola'            ,'id_tipo_uso_regola'               ),
                     (   2, 'seq_risca_r_ambito_interesse'           ,'risca_r_ambito_interesse'           ,'id_ambito_interesse'              )
            )
          --SELECT id_seq, des_seq, nome_tab, id_tab
          SELECT id_seq, des_seq, nome_tab, id_tab
            FROM d_sequence
   loop
   
     if n_sequence = 0 then
         -- riporto la categoria
         select FORMAT('| %-49s | %-79s |', v_sequence, ' ' ) into v_notice;
         RAISE notice '%',v_notice;

     else   
         -- letura del valore corrente e quello calcolato
         EXECUTE format('SELECT last_value FROM %I', v_sequence) into n_attuale;
         EXECUTE format('SELECT coalesce(max(%I),0) FROM %I', v_campoid, v_tabella) into n_calcolato;
         
         -- resoconto
         if (n_attuale =1 and n_calcolato=0) then
             v_da_aggiornare := '';
         elsif (n_attuale <> n_calcolato) then
             v_da_aggiornare := '*';
         else 
             v_da_aggiornare := '';
         end if;
         
         select FORMAT('| %-40s %8s | %-35s - %-32s %8s |%s', v_sequence, n_attuale, v_tabella, v_campoid, n_calcolato, v_da_aggiornare ) into v_notice;
         RAISE notice '%',v_notice;
                  
         -- aggiorna la sequence con il valore calcolato
         if (n_operazione > 0) then
             
             if (n_attuale =1 and n_calcolato=0) then
                 -- aggiornamento non necessario
             elseif (v_da_aggiornare = '*' and n_calcolato=0) then
                 -- reimposto la sequence con il valore iniziale = 1
                 SELECT setval(v_sequence, n_calcolato+1, false) into v_notice;
             elsif (n_attuale <> n_calcolato) then
                 -- aggiornamento della sequence con valore calcolato
                 SELECT setval(v_sequence, n_calcolato) into v_notice;
             else 
                 -- aggiornamento non necessario
             end if;
         end if;
     end if;
     
   END LOOP;

   -- Termine lista
   RAISE notice '%', '|---------------------------------------------------|---------------------------------------------------------------------------------|';
   RAISE NOTICE '%', '';



   -- ************************************** CODIFICHE APPLICATIVE **********************************************************
   -- Descrizione lista codifiche
   RAISE NOTICE '%', 'Sequence di codifica Applicativa';
   RAISE notice '%', '|---------------------------------------------------|---------------------------------------------------------------------------------|';
   RAISE notice '%', '| valore attuale sequence                           | ultimo valore recuperato dal campo CODIFICATO                                   |';
   RAISE notice '%', '|---------------------------------------------------|---------------------------------------------------------------------------------|';

   -- Generazione lista info sequence
   FOR v_sequence, v_tabella, v_campoid, v_queryaggiorna, v_regola IN
       with d_sequence (id_seq, des_seq, nome_tab, id_tab, query_aggiorna, regola) 
         as ( values 
                     (   1 ,'seq_risca_cod_gruppo_soggetto'          ,'risca_t_gruppo_soggetto'            ,'cod_gruppo_soggetto'              ,
                            'select coalesce(max(to_number(replace(cod_gruppo_soggetto, ''GRI'', ''''))),0) as valore_massimo
                               from risca_t_gruppo_soggetto;',
                            ' Regola : <SIGLA>-<progressivo assoluto> ' || chr(10) ||
                            '          <SIGLA>= ''GRI''' || chr(10) ||
                            '          <progressivo assoluto> = seq_risca_cod_gruppo_soggetto.nextval' || chr(10) ||
                            '          es: GRI-1234')                     
            )
          SELECT des_seq, nome_tab, id_tab, query_aggiorna, regola
            FROM d_sequence
   loop
       -- letura del valore corrente e quello calcolato
       EXECUTE format('SELECT last_value FROM %I', v_sequence) INTO n_attuale;
       EXECUTE format( v_queryaggiorna, v_tabella) INTO n_calcolato;

       -- resoconto
       if (n_attuale =1 and n_calcolato=0) then
           v_da_aggiornare := '';
       elsif (n_attuale <> n_calcolato) then
           v_da_aggiornare := '*';
       else 
           v_da_aggiornare := '';
       end if;
       
       select FORMAT('| %-40s %8s | %-35s - %-32s %8s |%s', v_sequence, n_attuale, v_tabella, v_campoid, n_calcolato, v_da_aggiornare ) into v_notice;
       RAISE notice '%',v_notice;
       --
       select FORMAT('%-s ', v_regola ) into v_notice;
       c_regoleresoconto := c_regoleresoconto || chr(10) || '-- ' || v_tabella || '.' || v_campoid || chr(10) || v_notice || chr(10);

       -- aggiorna la sequence con il valore calcolato
       if (n_operazione > 0) then
           
           if (n_attuale =1 and n_calcolato=0) then
               -- aggiornamento non necessario
           elseif (v_da_aggiornare = '*' and n_calcolato=0) then
               -- reimposto la sequence con il valore iniziale = 1
               SELECT setval(v_sequence, n_calcolato+1, false) into v_notice;
           elsif (n_attuale <> n_calcolato) then
               -- aggiornamento della sequence con valore calcolato
               SELECT setval(v_sequence, n_calcolato) into v_notice;
           else 
               -- aggiornamento non necessario
           end if;
       end if;

   END LOOP;

   -- Termine lista
   RAISE notice '%', '|---------------------------------------------------|---------------------------------------------------------------------------------|';

   -- Resoconto regole applicative
   RAISE NOTICE '%', '';
   RAISE NOTICE '%', '';
   RAISE NOTICE '%', '[DOCUMENTAZIONE] - Regole applicative utilizzate per calcolare il campo';
   RAISE notice '%', '-----------------------------------------------------------------------';
   RAISE notice '%', c_regoleresoconto;

   return esito;
   
EXCEPTION WHEN OTHERS then
   esito := 1;
   GET STACKED DIAGNOSTICS 
   t_msg_exc = MESSAGE_TEXT,
   t_dett_exc = PG_EXCEPTION_DETAIL,
   t_hint_exc = PG_EXCEPTION_HINT;
   RAISE NOTICE '% -Errore durante l''esecuzione della funzione', now(); 
   RAISE NOTICE '%', t_msg_exc; 
   RAISE NOTICE '%', t_dett_exc;  
   RAISE NOTICE '%', t_hint_exc;  
   return esito;
END;
   
$function$
;

-- DROP FUNCTION utility_fnc_check_elaborazione(int4);

CREATE OR REPLACE FUNCTION utility_fnc_check_elaborazione(p_idelaborazione integer DEFAULT 0)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
declare

/*********************************************************************************************************************
   NOME:     UTILITY_fnc_check_elaborazione
   SCOPO:    La funzione permette di verificare i dati di una elaborazione
   UTILIZZO: di SERVIZIO

   REVISIONE:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        23/02/2023                   Creazione della funzione iniziale
   1.1        26/02/2023                   Interrogato registro elaborazione

**********************************************************************************************************************/

esito int4 := 1;
t_msg_exc text;
t_dett_exc text;
t_hint_exc text;
--
   -- -------------------------------------------------------------------------------------------
   -- Attenzione :
   -- impostare n_elaborazione = <id_elaborazione> da esaminare
   n_elaborazione int4 :=0;
  -- --------------------------------------------------------------------------------------------
   v_idtabella   int4;
   v_tabella     text;   
   v_idpadre     int4;
   v_livello     int4;  
   v_notice      text;
   v_tabula      text;
   v_queryconta  text;
   --
   n_totrecords  int4;
   c_tabulazione text='   '; -- tabulazione
   
   
   rec_parametro RECORD;
   rec_registro_elabora RECORD;
   
begin

--  begin
   n_elaborazione := coalesce(p_idelaborazione,0);

   RAISE NOTICE '%', 'UTILITY - Verifica Elaborazione';
   RAISE NOTICE 'Data : %', current_timestamp;

   -- verifico modalità
   if n_elaborazione = 0 then
      RAISE notice 'Attenzione : %','Occorre specificare l''elaborazione da verificare (id_elaborazione)';
      RAISE EXCEPTION 'Specificare come parametro l''id_elaborazione da verificare - %', now();
      
   else
      RAISE NOTICE '%', '--';   
      RAISE notice 'Elaborazione = %',n_elaborazione;
      
      -- raccolgo info Elaborazione
      EXECUTE format( 'select to_jsonb(''id_elabora='' || ela.id_elabora::text || 
                                       '' | tipo: '' || dte.cod_tipo_elabora || ''-'' || dte.des_tipo_elabora || ''('' || ela.id_tipo_elabora || '')'' || 
                                       '' | stato:'' || dse.cod_stato_elabora || ''-'' || dse.des_stato_elabora || ''('' || ela.id_stato_elabora || '')''
                                      )
                        from risca_t_elabora ela
                   left join risca_d_tipo_elabora dte on ela.id_tipo_elabora = dte.id_tipo_elabora
                   left join risca_d_stato_elabora dse on ela.id_stato_elabora = dse.id_stato_elabora
                       where id_elabora = $1', v_tabella)
          USING n_elaborazione
           INTO v_notice;
      RAISE notice 'Dati Elaborazione : %',coalesce(v_notice,'<NON presente in archivio>');

   end if;  
   RAISE NOTICE '%', '';

-- Verifico gli eventuali parametri
   RAISE NOTICE '%', '--- Parametri :';
   FOR rec_parametro IN select ela.id_elabora,
                               rpe.id_parametro_elabora,
                               rpe.raggruppamento,
                               rpe.chiave,
                               rpe.valore
                          from risca_t_elabora ela
                     left join risca_r_parametro_elabora rpe on ela.id_elabora = rpe.id_elabora
                         where ela.id_elabora = n_elaborazione
                      order by rpe.id_parametro_elabora
   loop
       if rec_parametro.id_parametro_elabora is null then
           RAISE NOTICE '%', 'Nessun parametro di elaborazione, definito.';
       else
           RAISE NOTICE 'Parametro : %  = % - %', 
                        rec_parametro.id_parametro_elabora,
                        rec_parametro.chiave, 
                        rec_parametro.valore ;
       end if;

   END LOOP;


-- Verifico il registro elaborazione
   RAISE NOTICE '%', '';   
   RAISE NOTICE '%', '--- Registro Elaborazione:';
   RAISE NOTICE '%', 'Passo di Elaborazione                                                                            Fase di Elaborazione                                                             Records  Esito';
   RAISE NOTICE '%', '------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------';
   FOR rec_registro_elabora IN select -- ela.id_elabora,
                               -- rre.id_registro_elabora,
                               rre.id_passo_elabora,
                               pas.passo_elabora,
                               rre.id_fase_elabora, 
                               fas.fase_elabora,
                               rre.flg_esito_elabora,
                               --rre.nota_elabora,
                               count(*) as quanti 
                          from risca_t_elabora ela
                     left join risca_r_registro_elabora rre on ela.id_elabora = rre.id_elabora
                     left join ( -- passi
                                 SELECT pe.id_passo_elabora, 
--                                        pe.des_passo_elabora || 
--                                        ' (' || pe.id_passo_elabora || '-' || pe.cod_passo_elabora || 
--                                        ') -- tipo= ' || tpe.cod_tipo_passo_elabora || ' (' || tpe.id_tipo_passo_elabora || ')' as passo_elabora
                                        tpe.id_tipo_passo_elabora || '-' || tpe.cod_tipo_passo_elabora || '/' ||
                                        pe.id_passo_elabora || '-' || pe.cod_passo_elabora || ';' || pe.des_passo_elabora  as passo_elabora
                                   FROM RISCA_D_PASSO_ELABORA pe
                              left join RISCA_D_TIPO_PASSO_ELABORA tpe on pe.id_tipo_passo_elabora = tpe.id_tipo_passo_elabora
                               ) pas on rre.id_passo_elabora = pas.id_passo_elabora
                     left join (-- fase
                                 SELECT fel.id_fase_elabora, 
                                        fel.des_fase_elabora || ' (' || fel.cod_fase_elabora || ')' as fase_elabora
                                   FROM risca_d_fase_elabora fel
                               ) fas on rre.id_fase_elabora = fas.id_fase_elabora
                         where ela.id_elabora = n_elaborazione
                     group by  rre.id_passo_elabora, pas.passo_elabora, 
                               rre.id_fase_elabora, fas.fase_elabora,
                               rre.flg_esito_elabora -- , rre.nota_elabora
                      order by rre.id_passo_elabora
   loop                      
       if rec_registro_elabora.id_passo_elabora is null then
           RAISE NOTICE '%', 'Nessun registro di elaborazione presente';
       else
--           RAISE NOTICE '% - % = %  (flag : %)', 
--                        rec_registro_elabora.passo_elabora,
--                        rec_registro_elabora.fase_elabora, 
--                        rec_registro_elabora.quanti,
--                        rec_registro_elabora.flg_esito_elabora;

        select FORMAT('%-94s | %-75s | %10s | %-10s', 
                      rec_registro_elabora.passo_elabora, 
                      rec_registro_elabora.fase_elabora,
                      rec_registro_elabora.quanti,
                      rec_registro_elabora.flg_esito_elabora
                     ) into v_notice;       
        RAISE notice '%',v_notice;

       end if;

   END LOOP;



-- Verifico il le tabelle legate all'elaborazione
   RAISE NOTICE '%', '';   
   RAISE NOTICE '%', '--- Tabelle legate all''Elaborazione :';

   -- Descrizione lista
   RAISE notice '%', 'tabella                                         tot.records';
   RAISE notice '%', '-----------------------------------------------------------';

   -- Generazione lista info tabelle
   FOR v_idtabella, v_tabella, v_idpadre, v_livello, v_tabula, v_queryconta IN
       with d_tabelle (id_tab, nome_tab, id_padre, livello, query_conta)        
         as ( values 
                     (   1 ,'risca_w_output_dati'                 ,  0  , 0 , 'select count(*) from risca_w_output_dati             where id_elabora = $1'),
                     (   2 ,'risca_w_accertamento'                ,  0  , 0 , 'select count(*) from risca_w_accertamento            where id_elabora = $1'),
                     (   3 ,'risca_w_spedizione'                  ,  0  , 0 , 'select count(*) from risca_w_spedizione              where id_elabora = $1'),
                     (   4 ,'risca_w_stato_debitorio_upd'         ,  0  , 0 , 'select count(*) from risca_w_stato_debitorio_upd     where id_elabora = $1'),
                     (   5 ,'risca_w_rp_pagopa'                   ,  0  , 0 , 'select count(*) from risca_w_rp_pagopa               where id_elabora = $1'),
                     (   6 ,'risca_w_rp_estrco'                   ,  0  , 0 , 'select count(*) from risca_w_rp_estrco               where id_elabora = $1'),
                     (   7 ,'risca_w_rp_nonpremarcati'            ,  0  , 0 , 'select count(*) from risca_w_rp_nonpremarcati        where id_elabora = $1'),
                     (   8 ,'risca_t_lotto'                       ,  0  , 0 , 'select count(*) from risca_t_lotto                   where id_elabora = $1'),
                     (   9 ,'risca_t_spedizione'                  ,  0  , 0 , 'select count(*) from risca_t_spedizione              where id_elabora = $1'),
                     (  10 ,'risca_r_spedizione_acta'             , 10  , 1 , 'select count(*) from risca_r_spedizione_acta         where id_elabora = $1'),
                     (  11 ,'risca_r_iuv_da_inviare'              , 10  , 1 , 'select count(*) from risca_r_iuv_da_inviare          where id_elabora = $1')
             )
          SELECT id_tab, nome_tab, id_padre, livello, repeat(c_tabulazione,livello), query_conta as tabula
            FROM d_tabelle
   loop

        -- [Conteggio dei records della sola elaborazione selezionata]
        -- Es. select count(*) from scriva_t_istanza where id_istanza = %'
        EXECUTE format( v_queryconta, v_tabella)
          USING n_elaborazione
           INTO n_totrecords;

        -- visualizzo info tabella elaborata
--        select FORMAT('%-43s : %10s', v_tabula||v_tabella, n_totrecords ) into v_notice;
        select FORMAT('%-43s : %12s', v_tabula||v_tabella, replace(to_char(n_totrecords, '9999,999,999'),',','.' )) into v_notice;       
        RAISE notice '%',v_notice;
        -- debug
        --select FORMAT('%-43s : %s', v_tabula||v_tabella, v_queryconta ) into v_notice;
        --RAISE notice '%',v_notice;
        
   END LOOP;

   -- Termine lista
   RAISE notice '%', '-----------------------------------------------------------';

   esito := 0;   
   return esito;
   
EXCEPTION WHEN OTHERS then
   esito := 1;
   GET STACKED DIAGNOSTICS 
   t_msg_exc = MESSAGE_TEXT,
   t_dett_exc = PG_EXCEPTION_DETAIL,
   t_hint_exc = PG_EXCEPTION_HINT;
   RAISE NOTICE '% -Errore durante l''esecuzione della funzione', now(); 
   RAISE NOTICE '%', t_msg_exc; 
   RAISE NOTICE '%', t_dett_exc;  
   RAISE NOTICE '%', t_hint_exc;  
   return esito;
END;

$function$
;

-- DROP FUNCTION utility_fnc_check_riscossioni(int4);

CREATE OR REPLACE FUNCTION utility_fnc_check_riscossioni(p_idriscossione integer DEFAULT 0)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
declare

/*********************************************************************************************************************
   NOME:     UTILITY_fnc_check_riscossione
   SCOPO:    La funzione permette di verificare i dati inseriti in archivio
   UTILIZZO: di SERVIZIO

   REVISIONE:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        16/06/2023                   Creazione della funzione iniziale
   1.1        16/06/2023                   Aggiunta delle tabelle Anagrafiche
   1.2        16/06/2023                   Aggiunta delle tabelle Elaborazione
   1.3        20/06/2023                   Rivisto ordinamento tabelle
   1.4        20/06/2023                   Spostati i pagamenti nella lista delle elaborazioni
   1.5        26/06/2023                   Aggiunta la tabella : 'risca_s_tracciamento'
   1.6        19/09/2023                   Gestito esito=0 per operazione terminata correttamente
   1.7        22/09/2023                   Aggiunta la tabella : 'csi_log_audit'
   1.8        09/01/2024                   Aggiunte Anagrafiche : DDL_RISCA_1.0.0_rev_009c - (2023_10_19 -Aggiornamento)
                                                                  nuove tabelle : risca_t_delegato
                                                                                  risca_r_soggetto_delega
                                                                                  risca_r_gruppo_delega
                                                                                  
                                           Aggiunte Elaborazioni : DDL_RISCA_1.0.0_rev_010a - (2023_12_01 -Aggiornamento)
                                                                  nuove tabelle : risca_r_iuv_da_inviare
                                                                                  risca_r_pagopa_scomp_var_iuv
                                                                                  risca_r_pagopa_lista_carico
                                           
                                           Aggiunte Elaborazioni : DDL_RISCA_1.0.0_rev_010b - (2024_01_09 -Aggiornamento)                                                        
                                                                   nuove tabelle : risca_t_terna_prezzi_energ 
                                                                                   risca_t_terna_utenze 
                                                                                   risca_r_terna_prod_energ

   1.0.0      01/03/2024                   Aggiunte formattazione numeri ai totali
   1.1.0      26/06/2024                   Aggiunte Elaborazioni : DDL_RISCA_1.2.1 - (2024_06_26 -Aggiornamento)
                                                                   nuove tabelle : risca_r_ruolo

**********************************************************************************************************************/

esito int4 := 1;
t_msg_exc text;
t_dett_exc text;
t_hint_exc text;
--
   -- -------------------------------------------------------------------------------------------
   -- Attenzione :
   -- impostare n_riscossione = 0 per fare conteggio di tutto
   -- impostare n_riscossione = <id_riscossione> per fare conteggio dei soli records dell'istanza selazionata
   n_riscossione int4 :=0;
  -- --------------------------------------------------------------------------------------------
   v_idtabella   int4;
   v_tabella     text;   
   v_idpadre     int4;
   v_livello     int4;  
   v_notice      text;
   v_tabula      text;
   v_queryconta  text;
   --
   n_totrecords  int4;
   c_tabulazione text='   '; -- tabulazione
begin

--  begin
   n_riscossione := coalesce(p_idriscossione,0);

   RAISE NOTICE '%', 'UTILITY - Conteggio dei records in archivio';
   RAISE NOTICE 'Data : %', current_timestamp;

   -- verifico modalità
   if n_riscossione = 0 then
      RAISE notice 'Modalità : %','Conteggio di tutte le Riscossioni';
   else
      RAISE notice 'Modalità : Conteggio Riscossione = %',n_riscossione;
      
      -- raccolgo info Riscossione
      EXECUTE format( 'select to_jsonb(''id_riscossione='' || ris.id_riscossione::text || 
                                       '' | codice: '' || ris.cod_riscossione || 
                                       '' | stato:'' || dsr.des_stato_riscossione || ''('' || ris.id_stato_riscossione || '')''
                                      )
                        from risca_t_riscossione ris
                   left join risca_d_stato_riscossione dsr on ris.id_stato_riscossione = dsr.id_stato_riscossione  
                       where id_riscossione = $1', v_tabella)
          USING n_riscossione
           INTO v_notice;
      RAISE notice 'Dati Riscossione : %',coalesce(v_notice,'<NON presente in archivio>');

   end if;  
   RAISE NOTICE '%', '';


   -- Descrizione lista
   RAISE notice '%', 'tabella                                       tot.records';
   RAISE notice '%', '---------------------------------------------------------';

   -- Generazione lista info tabelle
   FOR v_idtabella, v_tabella, v_idpadre, v_livello, v_tabula, v_queryconta IN
       with d_tabelle (id_tab, nome_tab, id_padre, livello, query_conta)        
         as ( values 
                     (   1 ,'risca_t_riscossione'                 ,  0  , 0 , 'select count(*) from risca_t_riscossione                 where id_riscossione = $1'),
                     (   2 ,'risca_r_riscossione_storia_titolare' ,  1  , 1 , 'select count(*) from risca_r_riscossione_storia_titolare where id_riscossione = $1'),
                     (   3 ,'risca_r_riscossione_recapito'        ,  1  , 1 , 'select count(*) from risca_r_riscossione_recapito        where id_riscossione = $1'),
                     (   4 ,'risca_r_provvedimento'               ,  1  , 1 , 'select count(*) from risca_r_provvedimento               where id_riscossione = $1'),
                     (   5 ,'risca_r_riscossione_uso'             ,  1  , 1 , 'select count(*) from risca_r_riscossione_uso             where id_riscossione = $1'),
                     (   6 ,'risca_r_uso_ridaum'                  ,  5  , 2 , 'select count(*) from risca_r_uso_ridaum rru
                                                                                 left join risca_r_riscossione_uso rur on rru.id_riscossione_uso = rur.id_riscossione_uso 
                                                                                where id_riscossione = $1'),
                     (   7 ,'risca_t_stato_debitorio'             ,  1  , 1 , 'select count(*) from risca_t_stato_debitorio             where id_riscossione = $1'),
                     (   8 ,'risca_r_rata_sd'                     ,  7  , 2 , 'select count(*) from risca_r_rata_sd rsd
                                                                                 left join risca_t_stato_debitorio sde on rsd.id_stato_debitorio = sde.id_stato_debitorio 
                                                                                where id_riscossione = $1'),
                     (   9 ,'risca_r_rimborso'                    ,  7  , 2 , 'select count(*) from risca_r_rimborso rim
                                                                                 left join risca_t_stato_debitorio sde on rim.id_stato_debitorio = sde.id_stato_debitorio 
                                                                                where id_riscossione = $1'),
                     (  10 ,'risca_r_rimborso_sd_utilizzato'      ,  9  , 3 , 'select count(*) from risca_r_rimborso_sd_utilizzato rsu
                                                                                 left join risca_r_rimborso rim on rsu.id_rimborso = rim.id_rimborso 
                                                                                 left join risca_t_stato_debitorio sde on rim.id_stato_debitorio = sde.id_stato_debitorio 
                                                                                where id_riscossione = $1'),
                     (  11 ,'risca_r_annualita_sd'                ,  7  , 2 , 'select count(*) from risca_r_annualita_sd asd
                                                                                 left join risca_t_stato_debitorio sde on asd.id_stato_debitorio = sde.id_stato_debitorio 
                                                                                where id_riscossione = $1'),
                     (  12 ,'risca_r_annualita_uso_sd'            , 11  , 3 , 'select count(*) from risca_r_annualita_uso_sd aus
                                                                                 left join risca_r_annualita_sd asd on aus.id_annualita_sd = asd.id_annualita_sd 
                                                                                 left join risca_t_stato_debitorio sde on asd.id_stato_debitorio = sde.id_stato_debitorio   
                                                                                where id_riscossione = $1'),
                     (  13 ,'risca_r_annualita_uso_sd_ra'         , 12  , 4 , 'select count(*) from risca_r_annualita_uso_sd_ra usr
                                                                                 left join risca_r_annualita_uso_sd aus on usr.id_annualita_uso_sd = aus.id_annualita_uso_sd 
                                                                                 left join risca_r_annualita_sd asd on aus.id_annualita_sd = asd.id_annualita_sd 
                                                                                 left join risca_t_stato_debitorio sde on asd.id_stato_debitorio = sde.id_stato_debitorio 
                                                                                where id_riscossione = $1'),
--                     (  14 ,'risca_t_pagamento'                   ,  1  , 1 , 'select count(*) from risca_t_pagamento pag
--                                                                                 left join risca_r_dettaglio_pag rdp on pag.id_pagamento = rdp.id_pagamento
--                                                                                 left join risca_r_rata_sd rsd on rdp.id_rata_sd = rsd.id_rata_sd
--                                                                                 left join risca_t_stato_debitorio sde on rsd.id_stato_debitorio = sde.id_stato_debitorio
--                                                                                where id_riscossione = $1'),
                     (  15 ,'risca_r_dettaglio_pag'               ,  1  , 2 , 'select count(*) from risca_r_dettaglio_pag rdp
                                                                                 left join risca_r_rata_sd rsd on rdp.id_rata_sd = rsd.id_rata_sd
                                                                                 left join risca_t_stato_debitorio sde on rsd.id_stato_debitorio = sde.id_stato_debitorio
                                                                                where id_riscossione = $1'),
--                     (  16 ,'risca_r_pag_non_propri'              , 14  , 2 , 'select count(*) from risca_r_pag_non_propri pnp
--                                                                                 left join risca_t_pagamento pag on pnp.id_pagamento = pag.id_pagamento
--                                                                                 left join risca_r_dettaglio_pag rdp on pag.id_pagamento = rdp.id_pagamento
--                                                                                 left join risca_r_rata_sd rsd on rdp.id_rata_sd = rsd.id_rata_sd
--                                                                                 left join risca_t_stato_debitorio sde on rsd.id_stato_debitorio = sde.id_stato_debitorio
--                                                                                where id_riscossione = $1'),
--                     (  17 ,'risca_t_file_poste'                  , 14  , 2 , 'select count(*) from risca_t_file_poste fpo
--                                                                                 left join risca_t_pagamento pag on fpo.id_file_poste = pag.id_file_poste
--                                                                                 left join risca_r_dettaglio_pag rdp on pag.id_pagamento = rdp.id_pagamento
--                                                                                 left join risca_r_rata_sd rsd on rdp.id_rata_sd = rsd.id_rata_sd
--                                                                                 left join risca_t_stato_debitorio sde on rsd.id_stato_debitorio = sde.id_stato_debitorio
--                                                                                where id_riscossione = $1'),
--                     (  18 ,'risca_t_immagine'                    , 14  , 2 , 'select count(*) from risca_t_immagine imm
--                                                                                 left join risca_t_pagamento pag on imm.id_immagine = pag.id_immagine
--                                                                                 left join risca_r_dettaglio_pag rdp on pag.id_pagamento = rdp.id_pagamento
--                                                                                 left join risca_r_rata_sd rsd on rdp.id_rata_sd = rsd.id_rata_sd
--                                                                                 left join risca_t_stato_debitorio sde on rsd.id_stato_debitorio = sde.id_stato_debitorio
--                                                                                where id_riscossione = $1'),
                     (  19 ,'risca_t_accertamento'                ,  7  , 1 , 'select count(*) from risca_t_accertamento acc
                                                                                 left join risca_t_stato_debitorio sde on acc.id_stato_debitorio = sde.id_stato_debitorio 
                                                                                where id_riscossione = $1'),
--                     (  20 ,'risca_t_file_450'                    , 19  , 2 , 'select count(*) from risca_t_file_450 f45
--                                                                                 left join risca_t_accertamento acc on f45.id_file_450 = acc.id_file_450
--                                                                                 left join risca_t_stato_debitorio sde on acc.id_stato_debitorio = sde.id_stato_debitorio 
--                                                                                where id_riscossione = $1')
                     (  21 ,'risca_r_ruolo'                       , 19  , 2 , 'select count(*) from risca_r_ruolo ruo
                                                                                 left join risca_t_accertamento acc on ruo.id_accertamento = acc.id_accertamento
                                                                                 left join risca_t_stato_debitorio sde on acc.id_stato_debitorio = sde.id_stato_debitorio 
                                                                                where id_riscossione = $1')
             )
          SELECT id_tab, nome_tab, id_padre, livello, repeat(c_tabulazione,livello), query_conta as tabula
            FROM d_tabelle
   loop

      -- [Conteggio di tutte le Riscossioni]
      if n_riscossione = 0 then
      
--   RAISE NOTICE '%', 'debug';
        -- lettura dei records in tabella
        EXECUTE format('SELECT count(*) FROM %I', v_tabella) into n_totrecords;
        -- RAISE notice '% : %',v_tabella,n_totrecords;

        -- visualizzo info tabella elaborata
        --select FORMAT('%-43s : %10s', v_tabula||v_tabella, n_totrecords ) into v_notice;
        select FORMAT('%-43s : %10s', v_tabula||v_tabella, replace(to_char(n_totrecords, '99,999,999'),',','.' )) into v_notice;
        RAISE notice '%',v_notice;

      else
        -- [Conteggio dei records della sola riscossione selezionata]
        -- Es. select count(*) from scriva_t_istanza where id_istanza = %'
        EXECUTE format( v_queryconta, v_tabella)
          USING n_riscossione
           INTO n_totrecords;

        -- visualizzo info tabella elaborata
        -- select FORMAT('%-43s : %10s', v_tabula||v_tabella, n_totrecords ) into v_notice;
        select FORMAT('%-43s : %12s', v_tabula||v_tabella, replace(to_char(n_totrecords, '9999,999,999'),',','.' )) into v_notice;                   
        RAISE notice '%',v_notice;
        -- debug
        --select FORMAT('%-43s : %s', v_tabula||v_tabella, v_queryconta ) into v_notice;
        --RAISE notice '%',v_notice;
        
      end if;

   END LOOP;

   -- Termine lista
   RAISE notice '%', '---------------------------------------------------------';


  -- ======================================================================================================
  -- INFO ANAGRAFICHE
  -- ======================================================================================================

   -- solo se : [Conteggio di tutte le Riscossioni]
   if n_riscossione = 0 then

      RAISE NOTICE '%', '';
      RAISE NOTICE '%', '';

      -- Descrizione lista
      RAISE notice '%', 'Conteggio tabelle ANAGRAFICHE';
      RAISE notice '%', 'tabella                                       tot.records';
      RAISE notice '%', '---------------------------------------------------------';  
  
      -- Generazione lista info tabelle anagrafiche
      FOR v_idtabella, v_tabella, v_idpadre, v_livello, v_tabula, v_queryconta IN
          with d_anagrafiche (id_tab, nome_tab, id_padre, livello, query_conta) 
            as ( values 
                        (   1 ,'risca_t_soggetto'                   ,  0  , 0 , 'select count(*) from risca_t_soggetto;'),
                        (   2 ,'risca_r_recapito'                   ,  1  , 1 , 'select count(*) from risca_r_recapito;'),
                        (   3 ,'risca_r_recapito_postel'            ,  1  , 1 , 'select count(*) from risca_r_recapito_postel;'),
                        (   4 ,'risca_t_gruppo_soggetto'            ,  0  , 0 , 'select count(*) from risca_t_gruppo_soggetto;'),
                        (   5 ,'risca_r_soggetto_gruppo'            ,  4  , 1 , 'select count(*) from risca_r_soggetto_gruppo;'),
-- no dominio           (   6 ,'risca_t_bil_acc'                    ,  0  , 0 , 'select count(*) from risca_t_bil_acc;')
                        (   6 ,'risca_t_delegato'                   ,  1  , 0 , 'select count(*) from risca_t_delegato;'),
                        (   7 ,'risca_r_soggetto_delega'            ,  7  , 1 , 'select count(*) from risca_r_soggetto_delega;'),
                        (   8 ,'risca_r_gruppo_delega'              ,  7  , 1 , 'select count(*) from risca_r_gruppo_delega;')
                )
             SELECT id_tab, nome_tab, id_padre, livello, repeat(c_tabulazione,livello), query_conta as tabula
               FROM d_anagrafiche
      loop
      
           -- lettura dei records in tabella
           EXECUTE format('SELECT count(*) FROM %I', v_tabella) into n_totrecords;
      
           -- visualizzo info tabella elaborata
           --select FORMAT('%-43s : %10s', v_tabula||v_tabella, n_totrecords ) into v_notice;
           select FORMAT('%-43s : %10s', v_tabula||v_tabella, replace(to_char(n_totrecords, '99,999,999'),',','.' )) into v_notice;
           RAISE notice '%',v_notice;
      
      
      END LOOP;
      
      -- Termine lista
      RAISE notice '%', '---------------------------------------------------------';
   end if;


  -- ======================================================================================================
  -- INFO ELABORAZIONI
  -- ======================================================================================================

   -- solo se : [Conteggio di tutte le Riscossioni]
   if n_riscossione = 0 then
  
      RAISE NOTICE '%', '';
      RAISE NOTICE '%', '';

      -- Descrizione lista
      RAISE notice '%', 'Conteggio tabelle di ELABORAZIONE';
      RAISE notice '%', 'tabella                                       tot.records';
      RAISE notice '%', '---------------------------------------------------------';  
  
      -- Generazione lista info tabelle elaborazione
      FOR v_idtabella, v_tabella, v_idpadre, v_livello, v_tabula, v_queryconta IN
          with d_anagrafiche (id_tab, nome_tab, id_padre, livello, query_conta) 
            as ( values 
                        (   1 ,'csi_log_audit'                       ,  0  , 0 , 'select count(*) from csi_log_audit clo;'),
                        (   2 ,'risca_s_tracciamento'                ,  0  , 0 , 'select count(*) from risca_s_tracciamento;'),
                        (   3 ,'risca_t_elabora'                     ,  0  , 0 , 'select count(*) from risca_t_elabora;'),
                        (   4 ,'risca_r_parametro_elabora'           ,  1  , 1 , 'select count(*) from risca_r_parametro_elabora;'),
                        (   5 ,'risca_r_registro_elabora'            ,  1  , 1 , 'select count(*) from risca_r_registro_elabora;'),
                        (   6 ,'risca_t_spedizione'                  ,  1  , 1 , 'select count(*) from risca_t_spedizione;'),
                        (   7 ,'risca_t_avviso_pagamento'            ,  4  , 2 , 'select count(*) from risca_t_avviso_pagamento;'),
                        (   8 ,'risca_r_spedizione_acta'             ,  4  , 2 , 'select count(*) from risca_r_spedizione_acta;'),
                        (   9 ,'risca_t_invio_acta'                  ,  4  , 2 , 'select count(*) from risca_t_invio_acta;'),
                        (  10 ,'risca_t_lotto'                       ,  4  , 2 , 'select count(*) from risca_t_lotto;'),
                        (  11 ,'risca_r_pagopa_lista_car_iuv'        ,  7  , 3 , 'select count(*) from risca_r_pagopa_lista_car_iuv;'),
                        (  12 ,'risca_r_pagopa_scomp_rich_iuv'       ,  7  , 3 , 'select count(*) from risca_r_pagopa_scomp_rich_iuv;'),
                        (  13 ,'risca_t_pagamento'                   ,  0  , 0 , 'select count(*) from risca_t_pagamento pag;'),
                        (  14 ,'risca_r_pag_non_propri'              , 11  , 1 , 'select count(*) from risca_r_pag_non_propri pnp;'),
                        (  15 ,'risca_t_file_poste'                  , 11  , 1 , 'select count(*) from risca_t_file_poste fpo;'),
                        (  16 ,'risca_t_immagine'                    , 11  , 1 , 'select count(*) from risca_t_immagine imm;'),
                        (  17 ,'risca_t_iuv'                         ,  0  , 0 , 'select count(*) from risca_t_iuv;'),
                        (  18 ,'risca_r_iuv_da_inviare'              , 17  , 1 , 'select count(*) from risca_r_iuv_da_inviare;'),
                        (  19 ,'risca_r_pagopa_scomp_var_iuv'        , 17  , 1 , 'select count(*) from risca_r_pagopa_scomp_var_iuv;'),
                        (  20 ,'risca_r_pagopa_lista_carico'         , 17  , 1 , 'select count(*) from risca_r_pagopa_lista_carico;'),
                        (  21 ,'risca_t_file_450'                    ,  0  , 0 , 'select count(*) from risca_t_file_450;'),
                        (  22 ,'risca_t_terna_prezzi_energ'          ,  0  , 0 , 'select count(*) from risca_t_terna_prezzi_energ;'),
                        (  23 ,'risca_t_terna_utenze'                , 22  , 1 , 'select count(*) from risca_t_terna_utenze;'),
                        (  24 ,'risca_r_terna_prod_energ'            , 22  , 1 , 'select count(*) from risca_r_terna_prod_energ;')
                )
             SELECT id_tab, nome_tab, id_padre, livello, repeat(c_tabulazione,livello), query_conta as tabula
               FROM d_anagrafiche
      loop
      
           -- lettura dei records in tabella
           EXECUTE format('SELECT count(*) FROM %I', v_tabella) into n_totrecords;
      
           -- visualizzo info tabella elaborata
           --select FORMAT('%-43s : %10s', v_tabula||v_tabella, n_totrecords ) into v_notice;
           select FORMAT('%-43s : %10s', v_tabula||v_tabella, replace(to_char(n_totrecords, '99,999,999'),',','.' )) into v_notice;
           RAISE notice '%',v_notice;
      
      END LOOP;

      -- Termine lista
      RAISE notice '%', '---------------------------------------------------------';
      RAISE notice '%', 'NB: Nelle risca_t_spedizione/figli e nella risca_t_iuv';
      RAISE notice '%', '    sono conteggiati i records generici.';
      RAISE notice '%', '   (non legati ad una specifica Riscossione)';
   end if;
   esito := 0;   
   return esito;
   
EXCEPTION WHEN OTHERS then
   esito := 1;
   GET STACKED DIAGNOSTICS 
   t_msg_exc = MESSAGE_TEXT,
   t_dett_exc = PG_EXCEPTION_DETAIL,
   t_hint_exc = PG_EXCEPTION_HINT;
   RAISE NOTICE '% -Errore durante l''esecuzione della funzione', now(); 
   RAISE NOTICE '%', t_msg_exc; 
   RAISE NOTICE '%', t_dett_exc;  
   RAISE NOTICE '%', t_hint_exc;  
   return esito;
END;
   
$function$
;

-- DROP FUNCTION utility_fnc_delete_file_450(int4, int4);

CREATE OR REPLACE FUNCTION utility_fnc_delete_file_450(p_idfile450 integer, p_operazione integer DEFAULT 0)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
declare

/*********************************************************************************************************************
   NOME:     UTILITY_fnc_delete_file_450
   SCOPO:    La funzione permette di cancellare un file 450 e tutte le tabelle 'figlie' ad esso legate quando questo è errato (segnalato da utente) e NON è ancora stato inviato a SORIS
   UTILIZZO: Per ora di SERVIZIO
             La funzione dovrà essere implementata in un servizio per essere usata da una voce di menu dell'applicativo

   REGOLE  : Il File 450 può essere cancellato solo se presente in tabella (id_file_450) e :
             valorizzati i campi 'nome_file', 'data_creazione' e 'data_conferma'
             Non ci devono essere : 
                 -- accertamenti legati al file 450 devono essere tutti di tipo=3 (riscossione coattiva) - (risca_t_acertamento)
                    gli accertamenti identificati NON devono essere stati spediti (campo : id_spedizione NULL)
                 -- accertamenti legati al file 450 (tipo=3) che hanno dei dati sollecito figli (risca_t_acertamento)
                 -- ruoli degli accertamenti (tipo=3) che hanno dei dati figli (risca_r_ruolo)
                 
             Se il File 450 è valido occorre :
                 -- Aggiornare lo stato e i dati necessari dello Stato Debitorio su cui è stato fatto l'accertamento - risca_t_stato_debitorio
                 -- Cancellare i ruoli iscritti legati agli accertamenti in esame (Ruoli-Accertamento) -  risca_r_ruolo
                 -- Cancellare gli accertamenti legati al file_450 in esame (Accertamento) - risca_t_accertamento
                    Eventualmente dati di working ancora presenti - risca_w_accertamento
                 -- Cancellare il file_450 - risca_t_file_450


   REVISIONE:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        02/10/2023                   Creazione della funzione iniziale
   1.1        03/10/2023                   Aggiunta la gestione del parametro Operazione (se non specificato o a 0 fà solo lalista senza aggiornare !
   1.2        04/10/2023                   Aggiunta la gestione delle DELETE (prima solo ipotizzate) e la gestione errore ROLLBACK
   
**********************************************************************************************************************/

ctr    int4 := 0;
b_Cancellazione_Valida boolean DEFAULT FALSE;
file_450_info RECORD;
v_notice text;
---
esito  int4 := 1;
t_msg_exc text;
t_dett_exc text;
t_hint_exc text;
canc_count int4 := 0;

-- -------------------------------------------------------------------------------------------
-- Attenzione :
-- impostare n_operazione = 0 per fare solo la Lista
-- impostare n_operazione = 1 per fare oltre alla lista, anche CANCELLAZIONE
n_operazione int4 :=0;
-- --------------------------------------------------------------------------------------------

BEGIN

   n_operazione := coalesce(p_operazione,0);
   
   RAISE NOTICE '%','UTILITY - Cancellazione del File_450 dalla banca dati (se non ancora inviato a Soris)';
   RAISE NOTICE 'Data : %', current_timestamp;
   RAISE NOTICE '%', '';

   -- ----------------------------------
   -- Verifico della fattibilità
   -- ----------------------------------
   begin
      -- Verifico il soggetto è presente in anagrafica
      select count(1) into ctr from risca_t_file_450 where id_file_450 = p_idfile450;


      if ctr = 1 then 

         -- Verifico se possibile cancellare il soggetto
         RAISE NOTICE 'File_450 : % presente in archivio', p_idfile450; 

         -- DEBUG
         b_Cancellazione_Valida := TRUE;
         -- b_Cancellazione_Valida := FALSE;

               select rtf.id_file_450, 
                      rtf.nome_file,
                      rtf.data_creazione,
                      rtf.data_conferma, 
                      coalesce(aok.quanti,0) as tot_accertamenti,
                      coalesce(ako.quanti,0) as tot_accertamenti_ko,
                      coalesce(asp.quanti,0) as tot_accertamenti_spediti,
                      coalesce(rok.quanti,0) as tot_ruoli,
                      coalesce(rko.quanti,0) as tot_ruoli_ko
                 into file_450_info
                 from risca_t_file_450 rtf 
            -- --------------------------------------------------------------------- accertamenti
            left join (-- accertamenti di tipo 3 validi da cancellare   
                       select acc.id_file_450, acc.id_tipo_accertamento, count(*) as quanti
                         from risca_t_accertamento acc
                        where acc.id_tipo_accertamento = 3  -- 3 (riscossione coattiva)
                     group by acc.id_file_450, acc.id_tipo_accertamento
                     ) aok on rtf.id_file_450 = aok.id_file_450
            left join (-- accertamenti di tipo diverso da 3 NON validi da cancellare
                       select acc.id_file_450, acc.id_tipo_accertamento, count(*) as quanti
                         from risca_t_accertamento acc
                        where acc.id_tipo_accertamento <> 3  -- (NO riscossione coattiva)
                     group by acc.id_file_450, acc.id_tipo_accertamento
                     ) ako on rtf.id_file_450 = ako.id_file_450
            left join (-- accertamenti spediti NON validi da cancellare
                       select acc.id_file_450, acc.id_tipo_accertamento, count(*) as quanti
                         from risca_t_accertamento acc
                        where acc.id_spedizione is not null  -- (spediti)
                     group by acc.id_file_450, acc.id_tipo_accertamento
                     ) asp on rtf.id_file_450 = asp.id_file_450
            -- --------------------------------------------------------------------- ruoli-accertamenti
            left join (-- ruoli-accertamenti di tipo 3 validi da cancellare
                       select aco.id_file_450, count(*) as quanti
                         from risca_r_ruolo ruo
                           inner join (-- accertamenti di tipo riscossione coattiva (3)
                                       select acc.id_accertamento, acc.id_file_450, acc.id_tipo_accertamento
                                         from risca_t_accertamento acc
                                        where acc.id_tipo_accertamento = 3  -- 3 (riscossione coattiva)
                                      ) aco on ruo.id_accertamento = aco.id_accertamento
                     group by aco.id_file_450
                       ) rok on rtf.id_file_450 = rok.id_file_450
            left join (-- ruoli-accertamenti di tipo 3 NON validi perchè hanno legami attivi !
                        select aco.id_file_450, (coalesce(da1.quanti,0) + coalesce(da3.quanti,0) + coalesce(da7.quanti,0)) as quanti
                                 from risca_r_ruolo ruo
                           inner join (-- accertamenti di tipo riscossione coattiva (3)
                                       select acc.id_accertamento, acc.id_file_450, acc.id_tipo_accertamento
                                         from risca_t_accertamento acc
                                        where acc.id_tipo_accertamento = 3  -- 3 (riscossione coattiva)
                                      ) aco on ruo.id_accertamento = aco.id_accertamento
                                               and aco.id_file_450 = p_idfile450
                            left join (-- presenza di un record fr1 tornato da SORIS ! errore
                                       select fr1.id_ruolo, count(*) as quanti
                                         from risca_r_ruolo_soris_fr1 fr1
                                     group by fr1.id_ruolo  
                                      ) da1 on ruo.id_ruolo = da1.id_ruolo   
                            left join (-- presenza di un record fr3 tornato da SORIS ! errore
                                       select fr3.id_ruolo, count(*) as quanti
                                         from risca_r_ruolo_soris_fr3 fr3
                                     group by fr3.id_ruolo  
                                      ) da3 on ruo.id_ruolo = da3.id_ruolo   
                            left join (-- presenza di un record fr3 tornato da SORIS ! errore
                                       select fr7.id_ruolo, count(*) as quanti
                                         from risca_r_ruolo_soris_fr7 fr7
                                     group by fr7.id_ruolo  
                                      ) da7 on ruo.id_ruolo = da7.id_ruolo   
                       ) rko on rtf.id_file_450 = rko.id_file_450
            -- --------------------------------------------------------------------- SD
            -- nessuno
            where rtf.id_file_450 = p_idfile450;

         RAISE NOTICE 'nome_file : % - Data creazione : % - Data conferma : %', file_450_info.nome_file, file_450_info.data_creazione, file_450_info.data_conferma;
         RAISE NOTICE '%', '';
         RAISE NOTICE 'Verifica dati CORRELATI...';
         -- RAISE NOTICE '|tot_accertamenti_correlati|tot_ruoli_correlati| ?? SD?? |'; 
         RAISE NOTICE '|tot_accertamenti_correlati|tot_accertamenti_spediti|tot_ruoli_correlati|'; 
         RAISE NOTICE '+--------------------------+------------------------+-------------------+';
         -- RAISE NOTICE '|                         0|                       0|                       0|';

         select FORMAT('|%26s|%24s|%19s|', file_450_info.tot_accertamenti_ko,
                                           file_450_info.tot_accertamenti_spediti,
                                           file_450_info.tot_ruoli_ko ) into v_notice;
         RAISE notice '%',v_notice;
         RAISE NOTICE '+--------------------------+------------------------+-------------------+';

         if (file_450_info.tot_accertamenti_ko + 
             file_450_info.tot_accertamenti_spediti +
             file_450_info.tot_ruoli_ko ) = 0 then
             b_Cancellazione_Valida := TRUE;
         else
             b_Cancellazione_Valida := FALSE;
             RAISE NOTICE '%', '';
             RAISE NOTICE 'Il File_450 risulta CORRELATO ad altri dati. NON è possibile cancellarlo !';
         end if;

      else 
         RAISE NOTICE 'File_450 : % non esiste', p_idfile450; 
         esito := 2;
      end if;
   end;  -- BEGIN (verifica)


   -- ----------------------------------
   -- Cancellare il file_450
   -- ----------------------------------
   begin

      if ctr = 1 and b_Cancellazione_Valida = TRUE then 

         -- operazione richiesta
         v_notice = CASE n_operazione 
                      WHEN 1 THEN 'Richiesta CANCELAZIONE DATI'
                      ELSE 'Solo verifica, per cancellare impostare il parametro ''p_operazione = 1'''
                    END;

         RAISE NOTICE '%', '';
         RAISE NOTICE '%', 'Esito Verifica: Il file 450 è CANCELLABILE.';
         RAISE NOTICE '*** Operazione effettuata : % ***', v_notice;

         if n_operazione = 1 then
          
            raise notice '--- Inizio cancellazione file_450 : %', p_idfile450;
   
            -- -----------------------------------------------------
            -- Stati Debitori (UPDATE) - riportare lo SD a id_attivita_stato_deb = 4 (DA INVIARE A RISCOSSIONE COATTIVA)
            -- -----------------------------------------------------
            -- SD legati agli accertamenti (di tipo=3) facenti parte dell'invio del file_450 in esame
            UPDATE risca_t_stato_debitorio
               SET id_attivita_stato_deb = 4,
                   gest_data_upd = current_timestamp,
                   gest_attore_upd = 'SERVIZIO'
              FROM (select id_accertamento, id_stato_debitorio
                      from risca_t_accertamento rif
                     where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
                   ) as subquery
             WHERE risca_t_stato_debitorio.id_stato_debitorio = subquery.id_stato_debitorio;
            GET DIAGNOSTICS canc_count = ROW_COUNT;

            -- debug salvataggio ------------------
            --CREATE TABLE IF NOT EXISTS save_risca_t_stato_debitorio_2024_10_03
            --                    AS SELECT * from risca_t_stato_debitorio can
            --                               where exists (select 1 
            --                                               from risca_t_accertamento rif
            --                                              where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
            --                                                and rif.id_stato_debitorio= can.id_stato_debitorio);
            -- -------------------------------------
            raise notice '% - stati debitori legati ad accertamenti del file_450, aggiornati : %', now(), canc_count;
   
            -- -----------------------------------------------------
            -- Ruoli (Cancellazione)
            -- -----------------------------------------------------
            -- Ruoli legati agli accertamenti (di tipo=3) facenti parte dell'invio del file_450 in esame
            DELETE FROM risca_r_ruolo u 
                  where exists (select 1 
                                  from risca_t_accertamento oi
                                 where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
                                   and oi.id_accertamento= u.id_accertamento);
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            -- debug salvataggio ------------------
            --CREATE TABLE IF NOT EXISTS save_risca_r_ruolo_2024_10_03
            --                    AS SELECT * FROM risca_r_ruolo can
            --                               where exists (select 1 
            --                                               from risca_t_accertamento rif
            --                                              where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
            --                                                and rif.id_accertamento= can.id_accertamento);
            -- -------------------------------------
            raise notice '% - ruoli legati ad accertamenti del file_450, cancellati : %', now(), canc_count;
   
            -- -----------------------------------------------------
            -- Accertamenti (Cancellazione)
            -- -----------------------------------------------------
            
            -- Eventualmente dati di working ancora presenti - risca_w_accertamento
            DELETE FROM risca_w_accertamento  where id_file_450 = p_idfile450;
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            -- debug salvataggio ------------------
            -- CREATE TABLE IF NOT EXISTS save_risca_w_accertamento_2024_10_03
            --                    AS SELECT * FROM risca_w_accertamento where id_file_450 = p_idfile450;
            -- -------------------------------------
            raise notice '% - working accertamenti del file_450 (tipo_accertamento=3), cancellati : %', now(), canc_count;
   
            -- Accertamenti (di tipo=3) facenti parte dell'invio del file_450 in esame
            DELETE FROM risca_t_accertamento  where id_file_450 = p_idfile450 and id_tipo_accertamento = 3;
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            -- salvataggio ------------------------------
            -- CREATE TABLE IF NOT EXISTS save_risca_t_accertamento_2024_10_03
            --                    AS SELECT * FROM risca_t_accertamento where id_file_450 = p_idfile450 and id_tipo_accertamento = 3;
            -- -------------------------------------
            raise notice '% - accertamenti del file_450, cancellati : %', now(), canc_count;
   
            -- -----------------------------------------------------
            -- File_450 (Cancellazione)
            -- -----------------------------------------------------
            DELETE FROM risca_t_file_450 where id_file_450 = p_idfile450;
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            -- debug salvataggio ------------------
            -- CREATE TABLE IF NOT EXISTS save_risca_t_file_450_2024_10_03
            --                    AS SELECT * FROM risca_t_file_450 where id_file_450 = p_idfile450;
            -- -------------------------------------
            raise notice '% - file_450, cancellati : %', now(), canc_count;
            raise notice '--- Fine cancellazione file_450 : %', p_idfile450;
            
            -- COMMIT;
            esito := 0;

         else

            RAISE NOTICE '%', '';
            RAISE NOTICE '%', '-------------------------------------------------------------------------------';
            RAISE NOTICE '--- Conteggio dei dati presenti per il file_450 : %', p_idfile450;
            
            -- Stati Debitori
            SELECT count(*) into canc_count from risca_t_stato_debitorio can
                                           where exists (select 1 
                                                           from risca_t_accertamento rif
                                                          where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
                                                            and rif.id_stato_debitorio= can.id_stato_debitorio);
            raise notice '% - stati debitori legati ad accertamenti del file_450, in archivio : %', now(), canc_count;
   

            -- Ruoli
            SELECT count(*) into canc_count FROM risca_r_ruolo can
                                           where exists (select 1 
                                                           from risca_t_accertamento rif
                                                          where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
                                                            and rif.id_accertamento= can.id_accertamento);
            raise notice '% - ruoli legati ad accertamenti del file_450, in archivio : %', now(), canc_count;
   
            -- Accertamenti
            SELECT count(*) into canc_count FROM risca_w_accertamento where id_file_450 = p_idfile450;
            raise notice '% - working accertamenti del file_450 (tipo_accertamento=3), in archivio : %', now(), canc_count;
            -- Accertamenti (di tipo=3) facenti parte dell'invio del file_450 in esame
            SELECT count(*) into canc_count FROM risca_t_accertamento where id_file_450 = p_idfile450 and id_tipo_accertamento = 3;
            raise notice '% - accertamenti del file_450, in archivio : %', now(), canc_count;
   
            -- File_450
            SELECT count(*) into canc_count FROM risca_t_file_450 where id_file_450 = p_idfile450;
            raise notice '% - file_450, in archivio : %', now(), canc_count;
            
            esito := 0;
         end if;
         
      else
         if ctr = 1 then
            raise notice '--- Attenzione: Non è possibile cancellare i dati del File_450 : %', p_idfile450;
         end if;
      end if;
   
   end;  -- BEGIN (Cancellazione)
   return esito;

EXCEPTION WHEN OTHERS then
   ROLLBACK;
   ---
   esito := 1;
   GET STACKED DIAGNOSTICS 
   t_msg_exc = MESSAGE_TEXT,
   t_dett_exc = PG_EXCEPTION_DETAIL,
   t_hint_exc = PG_EXCEPTION_HINT;
   RAISE NOTICE '% -Errore durante l''esecuzione della funzione', now(); 
   RAISE NOTICE '%', t_msg_exc; 
   RAISE NOTICE '%', t_dett_exc;  
   RAISE NOTICE '%', t_hint_exc;  
   return esito;
END; -- BEGIN

$function$
;

-- DROP FUNCTION utility_fnc_delete_file_450_nosd(int4, int4);

CREATE OR REPLACE FUNCTION utility_fnc_delete_file_450_nosd(p_idfile450 integer, p_operazione integer DEFAULT 0)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
declare

/*********************************************************************************************************************
   NOME:     UTILITY_fnc_delete_file_450
   SCOPO:    La funzione permette di cancellare un file 450 e tutte le tabelle 'figlie' ad esso legate quando questo è errato (segnalato da utente) e NON è ancora stato inviato a SORIS
   UTILIZZO: Per ora di SERVIZIO
             La funzione dovrà essere implementata in un servizio per essere usata da una voce di menu dell'applicativo

   REGOLE  : Il File 450 può essere cancellato solo se presente in tabella (id_file_450) e :
             valorizzati i campi 'nome_file', 'data_creazione' e 'data_conferma'
             Non ci devono essere : 
                 -- accertamenti legati al file 450 devono essere tutti di tipo=3 (riscossione coattiva) - (risca_t_acertamento)
                    gli accertamenti identificati NON devono essere stati spediti (campo : id_spedizione NULL)
                 -- accertamenti legati al file 450 (tipo=3) che hanno dei dati sollecito figli (risca_t_acertamento)
                 -- ruoli degli accertamenti (tipo=3) che hanno dei dati figli (risca_r_ruolo)
                 
             Se il File 450 è valido occorre :
                 -- Aggiornare lo stato e i dati necessari dello Stato Debitorio su cui è stato fatto l'accertamento - risca_t_stato_debitorio
                 -- Cancellare i ruoli iscritti legati agli accertamenti in esame (Ruoli-Accertamento) -  risca_r_ruolo
                 -- Cancellare gli accertamenti legati al file_450 in esame (Accertamento) - risca_t_accertamento
                    Eventualmente dati di working ancora presenti - risca_w_accertamento
                 -- Cancellare il file_450 - risca_t_file_450


   REVISIONE:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        02/10/2023                   Creazione della funzione iniziale (clonata da funzione originale )per non aggiornare gli SD !
   
**********************************************************************************************************************/

ctr    int4 := 0;
b_Cancellazione_Valida boolean DEFAULT FALSE;
file_450_info RECORD;
v_notice text;
---
esito  int4 := 1;
t_msg_exc text;
t_dett_exc text;
t_hint_exc text;
canc_count int4 := 0;

-- -------------------------------------------------------------------------------------------
-- Attenzione :
-- impostare n_operazione = 0 per fare solo la Lista
-- impostare n_operazione = 1 per fare oltre alla lista, anche CANCELLAZIONE
n_operazione int4 :=0;
-- --------------------------------------------------------------------------------------------

BEGIN

   n_operazione := coalesce(p_operazione,0);
   
   RAISE NOTICE '%','UTILITY - Cancellazione del File_450 dalla banca dati (se non ancora inviato a Soris)';
   RAISE NOTICE 'Data : %', current_timestamp;
   RAISE NOTICE '%', '';

   -- ----------------------------------
   -- Verifico della fattibilità
   -- ----------------------------------
   begin
      -- Verifico il soggetto è presente in anagrafica
      select count(1) into ctr from risca_t_file_450 where id_file_450 = p_idfile450;


      if ctr = 1 then 

         -- Verifico se possibile cancellare il soggetto
         RAISE NOTICE 'File_450 : % presente in archivio', p_idfile450; 

         -- DEBUG
         b_Cancellazione_Valida := TRUE;
         -- b_Cancellazione_Valida := FALSE;

               select rtf.id_file_450, 
                      rtf.nome_file,
                      rtf.data_creazione,
                      rtf.data_conferma, 
                      coalesce(aok.quanti,0) as tot_accertamenti,
                      coalesce(ako.quanti,0) as tot_accertamenti_ko,
                      coalesce(asp.quanti,0) as tot_accertamenti_spediti,
                      coalesce(rok.quanti,0) as tot_ruoli,
                      coalesce(rko.quanti,0) as tot_ruoli_ko
                 into file_450_info
                 from risca_t_file_450 rtf 
            -- --------------------------------------------------------------------- accertamenti
            left join (-- accertamenti di tipo 3 validi da cancellare   
                       select acc.id_file_450, acc.id_tipo_accertamento, count(*) as quanti
                         from risca_t_accertamento acc
                        where acc.id_tipo_accertamento = 3  -- 3 (riscossione coattiva)
                     group by acc.id_file_450, acc.id_tipo_accertamento
                     ) aok on rtf.id_file_450 = aok.id_file_450
            left join (-- accertamenti di tipo diverso da 3 NON validi da cancellare
                       select acc.id_file_450, acc.id_tipo_accertamento, count(*) as quanti
                         from risca_t_accertamento acc
                        where acc.id_tipo_accertamento <> 3  -- (NO riscossione coattiva)
                     group by acc.id_file_450, acc.id_tipo_accertamento
                     ) ako on rtf.id_file_450 = ako.id_file_450
            left join (-- accertamenti spediti NON validi da cancellare
                       select acc.id_file_450, acc.id_tipo_accertamento, count(*) as quanti
                         from risca_t_accertamento acc
                        where acc.id_spedizione is not null  -- (spediti)
                     group by acc.id_file_450, acc.id_tipo_accertamento
                     ) asp on rtf.id_file_450 = asp.id_file_450
            -- --------------------------------------------------------------------- ruoli-accertamenti
            left join (-- ruoli-accertamenti di tipo 3 validi da cancellare
                       select aco.id_file_450, count(*) as quanti
                         from risca_r_ruolo ruo
                           inner join (-- accertamenti di tipo riscossione coattiva (3)
                                       select acc.id_accertamento, acc.id_file_450, acc.id_tipo_accertamento
                                         from risca_t_accertamento acc
                                        where acc.id_tipo_accertamento = 3  -- 3 (riscossione coattiva)
                                      ) aco on ruo.id_accertamento = aco.id_accertamento
                     group by aco.id_file_450
                       ) rok on rtf.id_file_450 = rok.id_file_450
            left join (-- ruoli-accertamenti di tipo 3 NON validi perchè hanno legami attivi !
                        select aco.id_file_450, (coalesce(da1.quanti,0) + coalesce(da3.quanti,0) + coalesce(da7.quanti,0)) as quanti
                                 from risca_r_ruolo ruo
                           inner join (-- accertamenti di tipo riscossione coattiva (3)
                                       select acc.id_accertamento, acc.id_file_450, acc.id_tipo_accertamento
                                         from risca_t_accertamento acc
                                        where acc.id_tipo_accertamento = 3  -- 3 (riscossione coattiva)
                                      ) aco on ruo.id_accertamento = aco.id_accertamento
                                               and aco.id_file_450 = p_idfile450
                            left join (-- presenza di un record fr1 tornato da SORIS ! errore
                                       select fr1.id_ruolo, count(*) as quanti
                                         from risca_r_ruolo_soris_fr1 fr1
                                     group by fr1.id_ruolo  
                                      ) da1 on ruo.id_ruolo = da1.id_ruolo   
                            left join (-- presenza di un record fr3 tornato da SORIS ! errore
                                       select fr3.id_ruolo, count(*) as quanti
                                         from risca_r_ruolo_soris_fr3 fr3
                                     group by fr3.id_ruolo  
                                      ) da3 on ruo.id_ruolo = da3.id_ruolo   
                            left join (-- presenza di un record fr3 tornato da SORIS ! errore
                                       select fr7.id_ruolo, count(*) as quanti
                                         from risca_r_ruolo_soris_fr7 fr7
                                     group by fr7.id_ruolo  
                                      ) da7 on ruo.id_ruolo = da7.id_ruolo   
                       ) rko on rtf.id_file_450 = rko.id_file_450
            -- --------------------------------------------------------------------- SD
            -- nessuno
            where rtf.id_file_450 = p_idfile450;

         RAISE NOTICE 'nome_file : % - Data creazione : % - Data conferma : %', file_450_info.nome_file, file_450_info.data_creazione, file_450_info.data_conferma;
         RAISE NOTICE '%', '';
         RAISE NOTICE 'Verifica dati CORRELATI...';
         -- RAISE NOTICE '|tot_accertamenti_correlati|tot_ruoli_correlati| ?? SD?? |'; 
         RAISE NOTICE '|tot_accertamenti_correlati|tot_accertamenti_spediti|tot_ruoli_correlati|'; 
         RAISE NOTICE '+--------------------------+------------------------+-------------------+';
         -- RAISE NOTICE '|                         0|                       0|                       0|';

         select FORMAT('|%26s|%24s|%19s|', file_450_info.tot_accertamenti_ko,
                                           file_450_info.tot_accertamenti_spediti,
                                           file_450_info.tot_ruoli_ko ) into v_notice;
         RAISE notice '%',v_notice;
         RAISE NOTICE '+--------------------------+------------------------+-------------------+';

         if (file_450_info.tot_accertamenti_ko + 
             file_450_info.tot_accertamenti_spediti +
             file_450_info.tot_ruoli_ko ) = 0 then
             b_Cancellazione_Valida := TRUE;
         else
             b_Cancellazione_Valida := FALSE;
             RAISE NOTICE '%', '';
             RAISE NOTICE 'Il File_450 risulta CORRELATO ad altri dati. NON è possibile cancellarlo !';
         end if;

      else 
         RAISE NOTICE 'File_450 : % non esiste', p_idfile450; 
         esito := 2;
      end if;
   end;  -- BEGIN (verifica)


   -- ----------------------------------
   -- Cancellare il file_450
   -- ----------------------------------
   begin

      if ctr = 1 and b_Cancellazione_Valida = TRUE then 

         -- operazione richiesta
         v_notice = CASE n_operazione 
                      WHEN 1 THEN 'Richiesta CANCELAZIONE DATI'
                      ELSE 'Solo verifica, per cancellare impostare il parametro ''p_operazione = 1'''
                    END;

         RAISE NOTICE '%', '';
         RAISE NOTICE '%', 'Esito Verifica: Il file 450 è CANCELLABILE.';
         RAISE NOTICE '*** Operazione effettuata : % ***', v_notice;

         if n_operazione = 1 then
          
            raise notice '--- Inizio cancellazione file_450 : %', p_idfile450;
   
            -- -----------------------------------------------------
            -- Stati Debitori (UPDATE) - riportare lo SD a id_attivita_stato_deb = 4 (DA INVIARE A RISCOSSIONE COATTIVA)
            -- -----------------------------------------------------
            -- SD legati agli accertamenti (di tipo=3) facenti parte dell'invio del file_450 in esame
--            UPDATE risca_t_stato_debitorio
--               SET id_attivita_stato_deb = 4,
--                   gest_data_upd = current_timestamp,
--                   gest_attore_upd = 'SERVIZIO'
--              FROM (select id_accertamento, id_stato_debitorio
--                      from risca_t_accertamento rif
--                     where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
--                   ) as subquery
--             WHERE risca_t_stato_debitorio.id_stato_debitorio = subquery.id_stato_debitorio;
--            GET DIAGNOSTICS canc_count = ROW_COUNT;

            -- debug salvataggio ------------------
            --CREATE TABLE IF NOT EXISTS save_risca_t_stato_debitorio_2024_10_03
            --                    AS SELECT * from risca_t_stato_debitorio can
            --                               where exists (select 1 
            --                                               from risca_t_accertamento rif
            --                                              where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
            --                                                and rif.id_stato_debitorio= can.id_stato_debitorio);
            -- -------------------------------------
--            raise notice '% - stati debitori legati ad accertamenti del file_450, aggiornati : %', now(), canc_count;
   
            -- -----------------------------------------------------
            -- Ruoli (Cancellazione)
            -- -----------------------------------------------------
            -- Ruoli legati agli accertamenti (di tipo=3) facenti parte dell'invio del file_450 in esame
            DELETE FROM risca_r_ruolo u 
                  where exists (select 1 
                                  from risca_t_accertamento oi
                                 where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
                                   and oi.id_accertamento= u.id_accertamento);
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            -- debug salvataggio ------------------
            --CREATE TABLE IF NOT EXISTS save_risca_r_ruolo_2024_10_03
            --                    AS SELECT * FROM risca_r_ruolo can
            --                               where exists (select 1 
            --                                               from risca_t_accertamento rif
            --                                              where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
            --                                                and rif.id_accertamento= can.id_accertamento);
            -- -------------------------------------
            raise notice '% - ruoli legati ad accertamenti del file_450, cancellati : %', now(), canc_count;
   
            -- -----------------------------------------------------
            -- Accertamenti (Cancellazione)
            -- -----------------------------------------------------
            
            -- Eventualmente dati di working ancora presenti - risca_w_accertamento
            DELETE FROM risca_w_accertamento  where id_file_450 = p_idfile450;
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            -- debug salvataggio ------------------
            -- CREATE TABLE IF NOT EXISTS save_risca_w_accertamento_2024_10_03
            --                    AS SELECT * FROM risca_w_accertamento where id_file_450 = p_idfile450;
            -- -------------------------------------
            raise notice '% - working accertamenti del file_450 (tipo_accertamento=3), cancellati : %', now(), canc_count;
   
            -- Accertamenti (di tipo=3) facenti parte dell'invio del file_450 in esame
            DELETE FROM risca_t_accertamento  where id_file_450 = p_idfile450 and id_tipo_accertamento = 3;
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            -- salvataggio ------------------------------
            -- CREATE TABLE IF NOT EXISTS save_risca_t_accertamento_2024_10_03
            --                    AS SELECT * FROM risca_t_accertamento where id_file_450 = p_idfile450 and id_tipo_accertamento = 3;
            -- -------------------------------------
            raise notice '% - accertamenti del file_450, cancellati : %', now(), canc_count;
   
            -- -----------------------------------------------------
            -- File_450 (Cancellazione)
            -- -----------------------------------------------------
            DELETE FROM risca_t_file_450 where id_file_450 = p_idfile450;
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            -- debug salvataggio ------------------
            -- CREATE TABLE IF NOT EXISTS save_risca_t_file_450_2024_10_03
            --                    AS SELECT * FROM risca_t_file_450 where id_file_450 = p_idfile450;
            -- -------------------------------------
            raise notice '% - file_450, cancellati : %', now(), canc_count;
            raise notice '--- Fine cancellazione file_450 : %', p_idfile450;
            
            -- COMMIT;
            esito := 0;

         else

            RAISE NOTICE '%', '';
            RAISE NOTICE '%', '-------------------------------------------------------------------------------';
            RAISE NOTICE '--- Conteggio dei dati presenti per il file_450 : %', p_idfile450;
            
            -- Stati Debitori
            SELECT count(*) into canc_count from risca_t_stato_debitorio can
                                           where exists (select 1 
                                                           from risca_t_accertamento rif
                                                          where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
                                                            and rif.id_stato_debitorio= can.id_stato_debitorio);
            raise notice '% - stati debitori legati ad accertamenti del file_450, in archivio : %', now(), canc_count;
   

            -- Ruoli
            SELECT count(*) into canc_count FROM risca_r_ruolo can
                                           where exists (select 1 
                                                           from risca_t_accertamento rif
                                                          where (id_file_450 = p_idfile450 and id_tipo_accertamento = 3)
                                                            and rif.id_accertamento= can.id_accertamento);
            raise notice '% - ruoli legati ad accertamenti del file_450, in archivio : %', now(), canc_count;
   
            -- Accertamenti
            SELECT count(*) into canc_count FROM risca_w_accertamento where id_file_450 = p_idfile450;
            raise notice '% - working accertamenti del file_450 (tipo_accertamento=3), in archivio : %', now(), canc_count;
            -- Accertamenti (di tipo=3) facenti parte dell'invio del file_450 in esame
            SELECT count(*) into canc_count FROM risca_t_accertamento where id_file_450 = p_idfile450 and id_tipo_accertamento = 3;
            raise notice '% - accertamenti del file_450, in archivio : %', now(), canc_count;
   
            -- File_450
            SELECT count(*) into canc_count FROM risca_t_file_450 where id_file_450 = p_idfile450;
            raise notice '% - file_450, in archivio : %', now(), canc_count;
            
            esito := 0;
         end if;
         
      else
         if ctr = 1 then
            raise notice '--- Attenzione: Non è possibile cancellare i dati del File_450 : %', p_idfile450;
         end if;
      end if;
   
   end;  -- BEGIN (Cancellazione)
   return esito;

EXCEPTION WHEN OTHERS then
   ROLLBACK;
   ---
   esito := 1;
   GET STACKED DIAGNOSTICS 
   t_msg_exc = MESSAGE_TEXT,
   t_dett_exc = PG_EXCEPTION_DETAIL,
   t_hint_exc = PG_EXCEPTION_HINT;
   RAISE NOTICE '% -Errore durante l''esecuzione della funzione', now(); 
   RAISE NOTICE '%', t_msg_exc; 
   RAISE NOTICE '%', t_dett_exc;  
   RAISE NOTICE '%', t_hint_exc;  
   return esito;
END; -- BEGIN

$function$
;

-- DROP FUNCTION utility_fnc_delete_riscossione(int4);

CREATE OR REPLACE FUNCTION utility_fnc_delete_riscossione(p_idriscossione integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
declare

/*********************************************************************************************************************
   NOME:     UTILITY_fnc_delete_riscossione
   SCOPO:    La funzione permette di cancellare una riscossione e tutte le tabelle 'figlie' ad essa legate
   UTILIZZO: Per ora di SERVIZIO
             La funzione sara' richiamata dal servizio : /riscossione/id/{idIstanza} (DELETE)
   
             Il servizio effettua quanto segue:
                         1. Estrazione uuindex allegati istanza
                         2. Esecuzione fnc_delete_istanza
                         3. In caso di presenza di allegati richiama il servizio di delete allegati su index 
                            per gli uuindex salvati in precedenza

                         Questo per permettere la pulizia totale sia su scriva che su index.            

   REVISIONE:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        31/03/2022                   Creazione della funzione iniziale
   1.1        31/01/2023                   Aggiunte le nuove tabelle : SD, pagamenti e accertamenti
   1.2        19/06/2023                   Cancellate nuove tabelle : risca_r_riscossione_storia_titolare
   1.0.0      26/07/2023                   Cancellate nuove tabelle : dati-avvisi e solleciti-accertamenti
   
**********************************************************************************************************************/

ctr int4 := 0;
ctr_allegati int4 := 0;
esito int4 := 1;
t_msg_exc text;
t_dett_exc text;
t_hint_exc text;
canc_count int4 := 0;

BEGIN
   begin
      select count(1) into ctr from risca_t_riscossione where id_riscossione = p_idriscossione;
      
      if ctr = 1 then 
      
         raise notice '% -Inizio-  cancellazione riscossione : %', now(), p_idriscossione;

         -- -----------------------------------------------------		
         -- Accertamenti
         -- -----------------------------------------------------

/*
--verificare se necessario cancellare il file 450 

         -- File_450 Accertamento (Stato Debitorio)
         DELETE FROM risca_t_file_450 u 
               where exists (select 1 
                               from risca_t_accertamento oi
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= oi.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and oi.id_file_450= u.id_file_450);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - file 450 accertamento stato debitorio cancellati : %', now(), canc_count;  
*/

         -- -------------------------------------------------------------------- inizio-solleciti   (26.07.2023)

         -- risca_r_soll_dati_amministr (Accertamento-Stato_Debitorio)
         DELETE FROM risca_r_soll_dati_amministr u 
               where exists (select 1 
                               from risca_t_accertamento oi
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= oi.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and oi.id_accertamento= u.id_accertamento);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - Dati Ammnistrativi Sollecito (accertamento-Stato Debitorio) cancellati : %', now(), canc_count;

         -- risca_r_soll_dati_titolare (Accertamento-Stato_Debitorio)
         DELETE FROM risca_r_soll_dati_titolare u 
               where exists (select 1 
                               from risca_t_accertamento oi
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= oi.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and oi.id_accertamento= u.id_accertamento);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - Dati Titolare Sollecito (accertamento-Stato Debitorio) cancellati : %', now(), canc_count;


         -- risca_r_soll_dati_pagopa (Accertamento-Stato_Debitorio)
         DELETE FROM risca_r_soll_dati_pagopa u 
               where exists (select 1 
                               from risca_t_accertamento oi
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= oi.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and oi.id_accertamento= u.id_accertamento);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - Dati PagoPa Sollecito (accertamento-Stato Debitorio) cancellati : %', now(), canc_count;
         
         -- risca_r_soll_destinatari (Accertamento-Stato_Debitorio)
         DELETE FROM risca_r_soll_destinatari u 
               where exists (select 1 
                               from risca_t_accertamento oi
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= oi.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and oi.id_accertamento= u.id_accertamento);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - Dati Destinatari Sollecito (accertamento-Stato Debitorio) cancellati : %', now(), canc_count;
         
         -- risca_r_soll_dett_vers (Accertamento-Stato_Debitorio)
         DELETE FROM risca_r_soll_dett_vers u 
               where exists (select 1 
                               from risca_t_accertamento oi
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= oi.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and oi.id_accertamento= u.id_accertamento);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - Dati Dettaglio Versamenti Sollecito (accertamento-Stato Debitorio) cancellati : %', now(), canc_count;
         -- -------------------------------------------------------------------- fine-solleciti


         -- Accertamento (Stato Debitorio)
         DELETE FROM risca_t_accertamento u 
               where exists (select 1 
                               from risca_t_stato_debitorio oi
                              where id_riscossione = p_idriscossione
                                and oi.id_stato_debitorio= u.id_stato_debitorio);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - accertamenti stato debitorio cancellati : %', now(), canc_count;


         -- -----------------------------------------------------		
         -- Pagamenti
         -- -----------------------------------------------------
         DELETE FROM risca_r_dettaglio_pag u 
               where exists (select 1 
                               from risca_r_rata_sd oi
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= oi.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and oi.id_rata_sd= u.id_rata_sd);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - dettaglio pagamento rate stato debitorio cancellate : %', now(), canc_count;         
                  
         
         -- -----------------------------------------------------		
         -- Stato Debitorio
         -- -----------------------------------------------------

         -- Rate (Stato Debitorio)
         DELETE FROM risca_r_rata_sd u 
               where exists (select 1 
                               from risca_t_stato_debitorio oi
                              where id_riscossione = p_idriscossione
                                and oi.id_stato_debitorio= u.id_stato_debitorio);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - rate stato debitorio cancellate : %', now(), canc_count;


         -- Rimborso Utilizzato (Stato Debitorio)
         DELETE FROM risca_r_rimborso_sd_utilizzato u 
               where exists (select 1 
                               from risca_r_rimborso oi
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= oi.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and oi.id_rimborso= u.id_rimborso);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - rimborsi utilizzati stato debitorio cancellati : %', now(), canc_count;

         -- Rimborso (Stato Debitorio)
         DELETE FROM risca_r_rimborso u 
               where exists (select 1 
                               from risca_t_stato_debitorio oi
                              where id_riscossione = p_idriscossione
                                and oi.id_stato_debitorio= u.id_stato_debitorio);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - rimborsi stato debitorio cancellati : %', now(), canc_count;

         -- -------------------------------------------------------------------- inizio-annualita
         -- Riduzione-Aumento Annualita Uso (Stato Debitorio)
         DELETE FROM risca_r_annualita_uso_sd_ra u 
               where exists (select 1 
                               from risca_r_annualita_uso_sd oi
                               inner join risca_r_annualita_sd asd
                                      on asd.id_annualita_sd= oi.id_annualita_sd
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= asd.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and oi.id_annualita_uso_sd= u.id_annualita_uso_sd);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - riduzioni-aumenti annualita uso stato debitorio cancellati : %', now(), canc_count;
         
         -- Annualita Uso (Stato Debitorio)
         DELETE FROM risca_r_annualita_uso_sd u 
               where exists (select 1 
                               from risca_r_annualita_sd oi
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= oi.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and oi.id_annualita_sd= u.id_annualita_sd);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - annualita uso stato debitorio cancellate : %', now(), canc_count;

         -- Annualita (Stato Debitorio)
         DELETE FROM risca_r_annualita_sd u 
               where exists (select 1 
                               from risca_t_stato_debitorio oi
                              where id_riscossione = p_idriscossione
                                and oi.id_stato_debitorio= u.id_stato_debitorio);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - annualita stato debitorio cancellate : %', now(), canc_count;
         -- -------------------------------------------------------------------- fine-annualita
         
         
         -- -------------------------------------------------------------------- inizio-dati-avvisi   (26.07.2023)

         -- risca_r_avviso_uso (Avviso_dati_titolare -- Stato Debitorio)
         DELETE FROM risca_r_avviso_uso u 
               where exists (select 1 
                               from risca_r_avviso_annualita oi
                               inner join risca_r_avviso_dati_ammin asd
                                      on (asd.nap = u.nap and asd.codice_utenza = u.codice_utenza)
                               inner join risca_t_stato_debitorio sd
                                      on sd.id_stato_debitorio= asd.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and (oi.nap= u.nap and oi.codice_utenza= u.codice_utenza and oi.anno_rich_pagamento= u.anno_rich_pagamento));
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - avvisi uso (Dati_titolare - Stato Debitorio) cancellati : %', now(), canc_count;
         
         -- risca_r_avviso_annualita (Avviso_dati_titolare -- Stato Debitorio)
         DELETE FROM risca_r_avviso_annualita u 
               where exists (select 1 
                               from risca_r_avviso_dati_ammin oi
                               inner join risca_t_stato_debitorio sd
                                       on sd.id_stato_debitorio= oi.id_stato_debitorio
                              where sd.id_riscossione = p_idriscossione
                                and (oi.nap = u.nap and oi.codice_utenza = u.codice_utenza));
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - avvisi annualita (Dati_titolare - Stato Debitorio) cancellati : %', now(), canc_count;

         -- risca_r_avviso_dati_ammin (Avviso_dati_titolare -- Stato Debitorio)
         DELETE FROM risca_r_avviso_dati_ammin u 
               where exists (select 1 
                               from risca_t_stato_debitorio oi
                              where id_riscossione = p_idriscossione
                                and oi.id_stato_debitorio= u.id_stato_debitorio);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - avvisi dati amminitrstivi (Dati_titolare - Stato Debitorio) cancellati : %', now(), canc_count;
         -- -------------------------------------------------------------------- fine-dati-avvisi
         
         -- Stato Debitorio
         DELETE FROM risca_t_stato_debitorio where id_riscossione = p_idriscossione;
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - stati debitori cancellati : %', now(), canc_count;
         
         
         -- -----------------------------------------------------		
         -- Soggetti Riscossione
         -- -----------------------------------------------------
         -- Non si cancellano dati dei Soggetti in Anagrafica !


         -- -----------------------------------------------------       
         -- Riscossione
         -- -----------------------------------------------------
         
         -- Riscossione Recapito (Riscossione)
         DELETE FROM risca_r_riscossione_recapito where id_riscossione = p_idriscossione;
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - recapiti riscossione cancellati : %', now(), canc_count;

         -- Usi e Riduzioni (Riscossione)
         DELETE FROM risca_r_uso_ridaum u 
               where exists (select 1 
                               from risca_r_riscossione_uso oi
                              where id_riscossione = p_idriscossione
                                and oi.id_riscossione_uso= u.id_riscossione_uso);
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - riduzioni-aumenti degli usi riscossione cancellati : %', now(), canc_count;

         DELETE FROM risca_r_riscossione_uso where id_riscossione = p_idriscossione;
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - usi riscossione cancellati : %', now(), canc_count;

         -- Provvedimento (Riscossione)
         DELETE FROM risca_r_provvedimento where id_riscossione = p_idriscossione;
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - provvedimenti cancellati : %', now(), canc_count;

         -- Storia Titolare (Riscossione)
         DELETE FROM risca_r_riscossione_storia_titolare where id_riscossione = p_idriscossione;
         GET DIAGNOSTICS canc_count = ROW_COUNT;
         raise notice '% - storia titolare cancellati : %', now(), canc_count;

         -- Riscossione
         DELETE FROM risca_t_riscossione where id_riscossione = p_idriscossione;
         raise notice '% -Fine-  cancellazione riscossione : %', now(), p_idriscossione;
         
         esito := 0;
      else 
         
         RAISE NOTICE 'Riscossione % non esiste', p_idriscossione; 
         esito := 2;
      end if;
   end;
   return esito;

EXCEPTION WHEN OTHERS then
   esito := 1;
   GET STACKED DIAGNOSTICS 
   t_msg_exc = MESSAGE_TEXT,
   t_dett_exc = PG_EXCEPTION_DETAIL,
   t_hint_exc = PG_EXCEPTION_HINT;
   RAISE NOTICE '% -Errore durante l''esecuzione della funzione', now(); 
   RAISE NOTICE '%', t_msg_exc; 
   RAISE NOTICE '%', t_dett_exc;  
   RAISE NOTICE '%', t_hint_exc;  
   return esito;
END;

$function$
;

-- DROP FUNCTION utility_fnc_delete_stato_debitorio(int4, int4);

CREATE OR REPLACE FUNCTION utility_fnc_delete_stato_debitorio(p_idstatodebitorio integer, p_operazione integer DEFAULT 0)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
declare

/*********************************************************************************************************************
   NOME:     UTILITY_fnc_delete_stato_debitorio
   SCOPO:    La funzione permette di cancellare uno stato debitorio e tutte le tabelle 'figlie' ad esso legate
   UTILIZZO: SERVIZIO

   REGOLE  : Lo Stato Debitorio può essere cancellato solo se risulta scorrelato.
             Non ci devono essere : 
                 -- Rimborso (Stato debitorio)                    - risca_r_rimborso
                 -- Rimborso Utilizzato (Stato debitorio)         - risca_r_rimborso_sd_utilizzato
                 -- Accertamento (Stato debitorio)                - risca_t_accertamento
                 -- Avviso dato amministrativo (Stato debitorio)  - risca_r_avviso_dati_ammin
                 -- Dettaglio Pagamento (rata-SD)                 - risca_r_dettaglio_pag

   REVISIONE:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        15/05/2024                   Creazione della funzione iniziale
   1.1.0      26/06/2024                   Recuperata informazione del codice Riscossione in esame
   
**********************************************************************************************************************/

ctr    int4 := 0;
b_Cancellazione_Valida boolean DEFAULT FALSE;
--soggetto_info RECORD;
stato_debitorio_info RECORD;
v_notice text;
---
esito  int4 := 1;
t_msg_exc text;
t_dett_exc text;
t_hint_exc text;
canc_count int4 := 0;

-- -------------------------------------------------------------------------------------------
-- Attenzione :
-- impostare n_operazione = 0 per fare solo la Lista
-- impostare n_operazione = 1 per fare oltre alla lista, anche CANCELLAZIONE
n_operazione int4 :=0;
-- --------------------------------------------------------------------------------------------

BEGIN

   n_operazione := coalesce(p_operazione,0);

   RAISE NOTICE '%','UTILITY - Cancellazione dello Stato Debitorio dalla banca dati (se non correlato)';
   RAISE NOTICE 'Data : %', current_timestamp;
   RAISE NOTICE '%', '';

      
   -- ----------------------------------
   -- Verifico della fattibilità
   -- ----------------------------------
   begin
      -- Verifico se lo stao debitorio è presente in anagrafica
      select count(1) into ctr from risca_t_stato_debitorio where id_stato_debitorio = p_idstatodebitorio;

      if ctr = 1 then 
         -- Verifico se possibile cancellare lo stato debitorio
         RAISE NOTICE 'Stato Debitorio : % presente in archivio', p_idstatodebitorio; 

               select sta.id_stato_debitorio, 
                      sta.id_riscossione,
                      ris.cod_riscossione,
                      sta.num_titolo,
                      sta.data_provvedimento,
                      sta.num_richiesta_protocollo,
                      -- dati titolare ? coalesce(sog.den_soggetto,'') || '' || coalesce(sog.nome,'') || ' ' || coalesce(sog.cognome,'') as soggetto_titolare,
                      coalesce(rim.quanti,0) as tot_rimborsi,
                      coalesce(riu.quanti,0) as tot_rimborsi_utilizzati,
                      coalesce(acc.quanti,0) as tot_accertamenti,
                      coalesce(ava.quanti,0) as tot_avvisi_dati_amm,
                      coalesce(dpa.quanti,0) as tot_dettaglio_pagamento
                 into stato_debitorio_info
                 from risca_t_stato_debitorio sta
            left join risca_t_riscossione ris
                      on sta.id_riscossione = ris.id_riscossione 
            left join (-- sd legati ad un rimborso
                      select rim.id_stato_debitorio, count(*) as quanti
                        from risca_r_rimborso rim
                    group by rim.id_stato_debitorio 
                       ) rim on sta.id_stato_debitorio = rim.id_stato_debitorio
            left join (-- sd legati ad un rimborso utilizzato
                      select riu.id_stato_debitorio, count(*) as quanti
                        from risca_r_rimborso_sd_utilizzato riu
                    group by riu.id_stato_debitorio 
                       ) riu on sta.id_stato_debitorio = riu.id_stato_debitorio
            left join (-- sd legati ad un accertamento
                      select acc.id_stato_debitorio, count(*) as quanti
                        from risca_t_accertamento acc
                    group by acc.id_stato_debitorio 
                       ) acc on sta.id_stato_debitorio = acc.id_stato_debitorio
            left join (-- sd legati ad un avviso con dato amministrativo
                      select ava.id_stato_debitorio, count(*) as quanti
                        from risca_r_avviso_dati_ammin ava
                    group by ava.id_stato_debitorio 
                       ) ava on sta.id_stato_debitorio = ava.id_stato_debitorio
            left join (-- sd legati ad un dettaglio di pagamento
                      select rrs.id_stato_debitorio, count(*) as quanti
                        from risca_r_dettaglio_pag dpa
                        join risca_r_rata_sd rrs on dpa.id_rata_sd = rrs.id_rata_sd
                    group by rrs.id_stato_debitorio 
                       ) dpa on sta.id_stato_debitorio = dpa.id_stato_debitorio
            where sta.id_stato_debitorio = p_idstatodebitorio;

         RAISE NOTICE 'Codice Riscossione : %', stato_debitorio_info.cod_riscossione;
         RAISE NOTICE 'Num.Titolo : % - Data Provvedimento : %', stato_debitorio_info.num_titolo, stato_debitorio_info.data_provvedimento;
         RAISE NOTICE '%', '';
         RAISE NOTICE 'Verifica dati CORRELATI...';
         RAISE NOTICE '|rimborsi    |rimb_utilizzati |accertamenti |avvisi_dati_amm |dettagli_pagamento |'; 
         RAISE NOTICE '+------------+----------------+-------------+----------------+-------------------+';
         
         -- RAISE NOTICE '|           0|           0|           0|           0|           0|';
         select FORMAT('|%12s|%16s|%13s|%16s|%19s|', stato_debitorio_info.tot_rimborsi,
                                                     stato_debitorio_info.tot_rimborsi_utilizzati,
                                                     stato_debitorio_info.tot_accertamenti,
                                                     stato_debitorio_info.tot_avvisi_dati_amm, 
                                                     stato_debitorio_info.tot_dettaglio_pagamento ) into v_notice;
         RAISE notice '%',v_notice;
         
         if (stato_debitorio_info.tot_rimborsi +
             stato_debitorio_info.tot_rimborsi_utilizzati +
             stato_debitorio_info.tot_accertamenti +
             stato_debitorio_info.tot_avvisi_dati_amm +
             stato_debitorio_info.tot_dettaglio_pagamento
             ) = 0 then
             b_Cancellazione_Valida := TRUE;
         else
             b_Cancellazione_Valida := FALSE;
             RAISE NOTICE '%', '';
             RAISE NOTICE '%', 'Esito Verifica: Lo Stato Debitorio risulta CORRELATO ad altri dati.' || chr(10) || 'NON è possibile cancellarlo !';
         end if;

      else 
         RAISE NOTICE 'Stato Debitorio : % non esiste', p_idstatodebitorio; 
         esito := 2;
      end if;
   end;


   -- ----------------------------------
   -- Cancellare lo Stato Debitorio
   -- ----------------------------------
   begin
      if ctr = 1 and b_Cancellazione_Valida = TRUE then 

         -- operazione richiesta
         v_notice = CASE n_operazione 
                      WHEN 0 THEN 'Solo verifica, per cancellare lo SD impostare il parametro ''p_operazione = 1'''
                      ELSE 'Richiesta CANCELAZIONE DATI' 
                    END;
      
         RAISE NOTICE '%', '';
         RAISE NOTICE '%', 'Esito Verifica: Lo stato debitorio è CANCELLABILE.';
         RAISE NOTICE '*** Operazione effettuata : % ***', v_notice;

         if n_operazione = 1 then 
   
            raise notice '--- Inizio cancellazione stato debitorio : %', p_idstatodebitorio;

            -- -----------------------------------------------------
            -- Legami
            -- -----------------------------------------------------

            -- -------------------------------------------------------------------- inizio-annualita
            -- Riduzione-Aumento Annualita Uso (Stato Debitorio)
            DELETE FROM risca_r_annualita_uso_sd_ra u 
                  where exists (select 1 
                                  from risca_r_annualita_uso_sd oi
                                  inner join risca_r_annualita_sd asd
                                         on asd.id_annualita_sd= oi.id_annualita_sd
                                  inner join risca_t_stato_debitorio sd
                                         on sd.id_stato_debitorio= asd.id_stato_debitorio
                                 where sd.id_stato_debitorio = p_idstatodebitorio
                                   and oi.id_annualita_uso_sd= u.id_annualita_uso_sd);
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            raise notice '% - riduzioni-aumenti annualita uso stato debitorio cancellati : %', now(), canc_count;
            
            -- Annualita Uso (Stato Debitorio)
            DELETE FROM risca_r_annualita_uso_sd u 
                  where exists (select 1 
                                  from risca_r_annualita_sd oi
                                  inner join risca_t_stato_debitorio sd
                                         on sd.id_stato_debitorio= oi.id_stato_debitorio
                                 where sd.id_stato_debitorio = p_idstatodebitorio
                                   and oi.id_annualita_sd= u.id_annualita_sd);
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            raise notice '% - annualita uso stato debitorio cancellate : %', now(), canc_count;
   
            -- Annualita (Stato Debitorio)
            DELETE FROM risca_r_annualita_sd u 
                  where exists (select 1 
                                  from risca_t_stato_debitorio oi
                                 where id_stato_debitorio = p_idstatodebitorio
                                   and oi.id_stato_debitorio= u.id_stato_debitorio);
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            raise notice '% - annualita stato debitorio cancellate : %', now(), canc_count;
            -- -------------------------------------------------------------------- fine-annualita

            -- risca_r_rata_sd
            DELETE FROM risca_r_rata_sd where id_stato_debitorio = p_idstatodebitorio;
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            raise notice '% - rate cancellate : %', now(), canc_count;


            -- NON NECESSARI
            
            -- risca_r_avviso_dati_ammin
            --DELETE FROM risca_r_avviso_dati_ammin where id_stato_debitorio = p_idstatodebitorio;
            --GET DIAGNOSTICS canc_count = ROW_COUNT;
            --raise notice '% - legame con avviso dato amministrativo cancellati : %', now(), canc_count;

            -- risca_r_rimborso
            --DELETE FROM risca_r_rimborso where id_stato_debitorio = p_idstatodebitorio;
            --GET DIAGNOSTICS canc_count = ROW_COUNT;
            --raise notice '% - legame con rimborso cancellati : %', now(), canc_count;

            -- risca_r_rimborso_sd_utilizzato
            --DELETE FROM risca_r_rimborso_sd_utilizzato where id_stato_debitorio = p_idstatodebitorio;
            --GET DIAGNOSTICS canc_count = ROW_COUNT;
            --raise notice '% - legame con rimborso utilizzato cancellati : %', now(), canc_count;
   
            -- risca_t_accertamento
            --DELETE FROM risca_t_accertamento where id_stato_debitorio = p_idstatodebitorio;
            --GET DIAGNOSTICS canc_count = ROW_COUNT;
            --raise notice '% - legame con accertamento cancellati : %', now(), canc_count;

            -- -----------------------------------------------------
            -- Stato Debitorio
            -- -----------------------------------------------------

            -- risca_t_stato_debitorio
            DELETE FROM risca_t_stato_debitorio where id_stato_debitorio = p_idstatodebitorio;
            GET DIAGNOSTICS canc_count = ROW_COUNT;
            raise notice '% - stati debitori cancellati : %', now(), canc_count;

            -- COMMIT;

            raise notice '--- Fine cancellazione stato debitorio : %', p_idstatodebitorio;
            esito := 0;
         end if;
      end if;
      esito := 0;
      
   end;
   return esito;

EXCEPTION WHEN OTHERS then
   esito := 1;
   GET STACKED DIAGNOSTICS 
   t_msg_exc = MESSAGE_TEXT,
   t_dett_exc = PG_EXCEPTION_DETAIL,
   t_hint_exc = PG_EXCEPTION_HINT;
   RAISE NOTICE '% -Errore durante l''esecuzione della funzione', now(); 
   RAISE NOTICE '%', t_msg_exc; 
   RAISE NOTICE '%', t_dett_exc;  
   RAISE NOTICE '%', t_hint_exc;  
   return esito;
END;

$function$
;

-- DROP FUNCTION utility_fnc_ricerca_riscossione_json_dt(text, text, text);

CREATE OR REPLACE FUNCTION utility_fnc_ricerca_riscossione_json_dt(p_tipo_elemento text DEFAULT ''::text, p_codice_uso text DEFAULT ''::text, p_elemento text DEFAULT ''::text)
 RETURNS TABLE(dt_id_riscossione integer, dt_cod_riscossione character varying, dt_valore_json_dt text)
 LANGUAGE plpgsql
AS $function$
declare

/*********************************************************************************************************************
   NOME:     utility_fnc_ricerca_riscossione_JSON_DT
   SCOPO:    La funzione permette di ricercare le riscossioni che hanno nel campo JSON_DT gli elementi corrispondenti alle tipologie cercarl'elemento cercato definito
   UTILIZZO: Servizio

   REVISIONE:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0.0        06/06/2024                   Creazione della funzione iniziale
   1.1.1        12/06/2024                   Parametrizzazione e prima query base di ricerca
   1.2.1        12/06/2024                   Versione iniziale query ricerca elemento json_dt
   1.2.2        13/06/2024                   Versione query parametrizzata in base alla ricerca
   1.2.3        14/06/2024                   Versione query EXECUTE parametrizzata in base alla ricerca e al tipo
   1.2.4        19/07/2024                   Versione finale comprensiva di tutti i casi di combinazione analizzati
   
**********************************************************************************************************************/

   -- -------------------------------------------------------------------------------------------
   -- Identificativi dell'elemento da ricercare nel JSON_DT
   c_tipo_elemento text := '';
   c_codice_uso text := '';
   c_elemento text := '';
   -- --------------------------------------------------------------------------------------------
   c_filtro_ricerca text := '';
   c_where_ricerca text := '';
   --
   rec_elementi_json RECORD;
   n_totrecords  int4;
   -- c_tabulazione text='   '; -- tabulazione
   v_notice      text;
   -- 
   c_log_raccolto text := '';
   c_log_elaborazione text := '';
   -- gestione exception
   t_msg_exc text;
   t_dett_exc text;
   t_hint_exc text;

begin

   -- Riferimenti necessari alla ricerca dell'elemento json
   c_tipo_elemento := coalesce(p_tipo_elemento,'');
   c_codice_uso := coalesce(p_codice_uso,'');
   c_elemento := coalesce(p_elemento,'');

   c_log_raccolto := 'Funzione di ricerca elemento JSON_DT nelle Riscossioni';
   c_log_elaborazione := c_log_elaborazione || c_log_raccolto || chr(10);
   RAISE NOTICE '%', c_log_raccolto;
   
   c_log_raccolto := 'Data : ' || current_timestamp;
   c_log_elaborazione := c_log_elaborazione || c_log_raccolto || chr(10);
   RAISE NOTICE '%', c_log_raccolto;


   -- Verifica parametri di ricerca ricevuti : 
   -- Solo alcune combinazioni permettono in base allastruttura del JSON di effettuare la ricerca (esito=ok)
   -- ---------------------------------------------------------------------------------------------------------------------------------- 
   -- | Livello | Parametro         |Es.Elemento                        |   1    |      2     |    3   |    4   |       5       |   6  |
   -- |---------|-------------------|-----------------------------------|--------|------------|--------|--------|---------------|------|
   -- | 1       | p_tipo_elemento   | usi/dati_generali                 |  null  |      x     |  null  |    x   |       x       |   x  |
   -- | 2       | p_codice_uso      | AGRICOLO, etc..                   |  null  |    null    |  null  |  null  |       x       |   x  |
   -- | 3       | p_elemento        | qta_acqua (usi); comune (dati_gen)|  null  |      x     |    x   |  null  |     null      |   x  |
   -- |---------|-------------------|-----------------------------------|--------|------------|--------|--------|---------------|------|
   -- | Esito                                                           | errore | usi=errore | errore | errore | usi (ok)      | (ok) |
   -- |                                                                 |        | dati_g (ok)|        |        | dati_g=errore |      |
   -- ----------------------------------------------------------------------------------------------------------------------------------
   
   if (coalesce(c_tipo_elemento,'') = '' and coalesce(p_codice_uso,'') = '' and coalesce(c_elemento,'') = '') then
      -- (1) Segnalo errore
      c_log_raccolto := now() || ' - Attenzione : Specificare obbligatoriamente il parametro p_tipo_elemento e almeno uno degli altri 2  !';
      RAISE EXCEPTION '%', c_log_raccolto; -- ERRORE - SEGNALO E TERMINO
   elseif (coalesce(c_tipo_elemento,'') = '' and coalesce(p_codice_uso,'') = '') then
      -- (3) Segnalo errore
      c_log_raccolto := now() || ' - Attenzione : Specificare almeno il parametro ''p_tipo_elemento'' !';
      RAISE EXCEPTION '%', c_log_raccolto; -- ERRORE - SEGNALO E TERMINO
   elseif (c_tipo_elemento = 'usi' and coalesce(p_codice_uso,'') = '') then
      -- (2) Segnalo errore
      c_log_raccolto := now() || ' - Attenzione : Nel caso di parametro c_tipo_elemento=''usi'' occorre specificare anche il parametro p_codice_uso';
      RAISE EXCEPTION '%', c_log_raccolto; -- ERRORE - SEGNALO E TERMINO
   elseif (coalesce(p_codice_uso,'') = '' and coalesce(c_elemento,'') = '')  then
      -- (4) Segnalo errore
      c_log_raccolto := now() || ' - Attenzione : Specificare oltre al parametro p_tipo_elemento almeno uno degli altri 2  !';
      RAISE EXCEPTION '%', c_log_raccolto; -- ERRORE - SEGNALO E TERMINO
   elseif (c_tipo_elemento = 'dati_generali' and coalesce(p_elemento,'') = '') then
      -- (5) Segnalo errore
      c_log_raccolto := now() || ' - Attenzione : Nel caso di parametro c_tipo_elemento=''dati_generali'' occorre specificare anche il parametro p_elemento';
      RAISE EXCEPTION '%', c_log_raccolto; -- ERRORE - SEGNALO E TERMINO

   else
      -- -----------------------------------------------------------------------------------------
      -- Per le combinazioni di parametri che ricadono nei casi di analisi VALIDI, inizio la ricerca
      -- -----------------------------------------------------------------------------------------
      c_log_raccolto := '--';
      c_log_elaborazione := c_log_elaborazione || c_log_raccolto || chr(10);
      RAISE NOTICE '%', c_log_raccolto;

      c_log_raccolto := 'Parametri di Ricerca :';
      c_log_elaborazione := c_log_elaborazione || c_log_raccolto || chr(10);
      RAISE NOTICE '%', c_log_raccolto;
      c_log_raccolto := 'Tipologia json_dt : ' || c_tipo_elemento;
      c_log_elaborazione := c_log_elaborazione || c_log_raccolto || chr(10);
      RAISE NOTICE '%', c_log_raccolto;
      c_log_raccolto := 'Tipo json_dt : ' || c_codice_uso;
      c_log_elaborazione := c_log_elaborazione || c_log_raccolto || chr(10);
      RAISE NOTICE '%', c_log_raccolto;
      c_log_raccolto := 'Elemento json_dt : ' || c_elemento;
      c_log_elaborazione := c_log_elaborazione || c_log_raccolto || chr(10);
      RAISE NOTICE '%', c_log_raccolto;
      RAISE NOTICE '%', '';

      -- -----------------------------------------------------------------------------------------
      -- Si compone il filtro di ricerca dinamicamente in base ai casi identificati in analisi
      -- -----------------------------------------------------------------------------------------
      if c_tipo_elemento = 'usi' then
         -- -------------------------------------- CASO : USI --------------
         -- Esempio :
         -- c_tipo_elemento := 'usi';
         -- c_codice_uso := 'GRANDE_IDROELETTRICO'
         -- c_elemento := 'qta_acqua'  -- 'extra_profitti'
         -- ----------------------------------------------------------------
         -- Tutti e 3 i parametri valorizzati (caso=6)
         if (coalesce(c_tipo_elemento,'') <> '' and coalesce(p_codice_uso,'') <> '' and coalesce(c_elemento,'') <> '') then
            -- L'operatore JSON ->restituisce un valore JSON. Ecco perché è citato. Se desideri un testo semplice, utilizza l'operatore che restituisce un valore di testo : ->>                
            --c_filtro_ricerca := ' ''riscossione''->''dati_tecnici''->''' || c_tipo_elemento || '''->''' || c_codice_uso || '''->''' || c_elemento || '''';
            c_filtro_ricerca := ' ''riscossione''->''dati_tecnici''->''' || c_tipo_elemento || '''->''' || c_codice_uso || '''->>''' || c_elemento || '''';
         else
             -- soli i parametri 'p_tipo_elemento' e 'p_codice_uso' valorizzati (caso=5)
            c_filtro_ricerca := ' ''riscossione''->''dati_tecnici''->''' || c_tipo_elemento || '''->''' || c_codice_uso || '''';
        end if;
      else 
         -- -------------------------------------- CASO : DATI_GENERALI ----         
         -- Esempio :
         -- c_tipo_elemento := 'dati_generali';
         -- c_codice_uso := ''       -- superfluo
         -- c_elemento := 'comune'  -- 'nome_impianto_idrico'
         -- ----------------------------------------------------------------      
         -- Tutti e 3 i parametri valorizzati (caso=6) oppure solo i primi 2 (caso=2)
         --c_filtro_ricerca := ' ''riscossione''->''dati_tecnici''->''' || c_tipo_elemento || '''->''' || c_elemento || '''';
         c_filtro_ricerca := ' ''riscossione''->''dati_tecnici''->''' || c_tipo_elemento || '''->>''' || c_elemento || '''';
      end IF;
      c_where_ricerca := c_filtro_ricerca;

      -- DEBUG
      RAISE NOTICE '***** DEBUG *****';
      RAISE NOTICE 'filtro_ricerca: %', c_filtro_ricerca;
      RAISE NOTICE '';
      -- ----------------------------------------------------------------------------------------------------------------------------------------
      
      -- Recupero i dati RICHIESTI
      n_totrecords := 0;
      FOR rec_elementi_json IN 
              EXECUTE format('select ris.id_riscossione, 
                                     ris.cod_riscossione, -- tut.cod_utenza, 
                                     tjs.valore_json_dt
                                from risca_t_riscossione ris
                          inner join ( -- solo quelle con elemento VALORIZZATO
                                   select id_riscossione, cod_riscossione,
                                          CAST(json_dt -> %s as varchar) as valore_json_dt
                                     from risca_t_riscossione
                                    where CAST(json_dt -> %s as varchar) is not null
                                     ) tjs on ris.id_riscossione = tjs.id_riscossione
                             -- where ris.id_riscossione = 21309
                             order by ris.cod_riscossione', c_filtro_ricerca, c_where_ricerca)
      loop
      
         if n_totrecords = 0 then
              RAISE NOTICE '%', ' riscossione     | codice riscossione   | valore_elemento json_dt';
              RAISE NOTICE '%', '-----------------|----------------------|-----------------------------------------------------------------';
         end if;

         -- Elemento
         select FORMAT(' %-15s | %-20s | %-50s',
                       rec_elementi_json.id_riscossione,
                       rec_elementi_json.cod_riscossione,
                       rec_elementi_json.valore_json_dt
                      ) into v_notice;
         c_log_raccolto := v_notice;
         c_log_elaborazione := c_log_elaborazione || c_log_raccolto || chr(10);
         RAISE NOTICE '%', c_log_raccolto;

         n_totrecords := n_totrecords+1;
             
         -- -------------------------------------------
         -- Valorizzo record TABLE restituita
         -- -------------------------------------------
         dt_id_riscossione := rec_elementi_json.id_riscossione;
         dt_cod_riscossione := rec_elementi_json.cod_riscossione;
         dt_valore_json_dt := rec_elementi_json.valore_json_dt;
         RETURN NEXT;
   
      END LOOP;
   
      -- Verifico le tabelle legate all'elaborazione
      if n_totrecords = 0 then
           c_log_raccolto := 'Non sono presenti elementi ricercati nel JSON_DT delle riscossioni (risca_t_riscossione)';
           c_log_elaborazione := c_log_elaborazione || c_log_raccolto || chr(10);
           RAISE NOTICE '%', c_log_raccolto;
      end if;
   end if;
  --END

EXCEPTION WHEN OTHERS then

   GET STACKED DIAGNOSTICS 
   t_msg_exc = MESSAGE_TEXT,
   t_dett_exc = PG_EXCEPTION_DETAIL,
   t_hint_exc = PG_EXCEPTION_HINT;
   RAISE NOTICE '% - Errore durante l''esecuzione della funzione', now(); 
   RAISE NOTICE '%', t_msg_exc; 
   RAISE NOTICE '%', t_dett_exc;  
   RAISE NOTICE '%', t_hint_exc;  

   -- -------------------------------------------
   -- valorizzo dati mancanti TABLE restituita
   -- -------------------------------------------
   c_log_elaborazione := c_log_elaborazione || now() || ' - Errore durante l''esecuzione della funzione' || chr(10);
   c_log_elaborazione := c_log_elaborazione || t_msg_exc || chr(10);
   c_log_elaborazione := c_log_elaborazione || t_dett_exc || chr(10);
   c_log_elaborazione := c_log_elaborazione || t_hint_exc || chr(10);
   dt_valore_json_dt := c_log_elaborazione;
   RETURN NEXT;

END;

$function$
;


ALTER FUNCTION utility_fnc_aggiorna_sequence(int4)                     OWNER TO risca;
ALTER FUNCTION utility_fnc_check_elaborazione(int4)                    OWNER TO risca;
ALTER FUNCTION utility_fnc_check_riscossioni(int4)                     OWNER TO risca;
ALTER FUNCTION utility_fnc_delete_file_450(int4,int4)                  OWNER TO risca;
ALTER FUNCTION utility_fnc_delete_file_450_nosd(int4,int4)             OWNER TO risca;
ALTER FUNCTION utility_fnc_delete_riscossione(int4)                    OWNER TO risca;
ALTER FUNCTION utility_fnc_delete_stato_debitorio(int4,int4)           OWNER TO risca;
ALTER FUNCTION utility_fnc_ricerca_riscossione_json_dt(text,text,text) OWNER TO risca;

GRANT ALL ON FUNCTION utility_fnc_aggiorna_sequence(int4)                     TO public;
GRANT ALL ON FUNCTION utility_fnc_check_elaborazione(int4)                    TO public;
GRANT ALL ON FUNCTION utility_fnc_check_riscossioni(int4)                     TO public;
GRANT ALL ON FUNCTION utility_fnc_delete_file_450(int4,int4)                  TO public;
GRANT ALL ON FUNCTION utility_fnc_delete_file_450_nosd(int4,int4)             TO public;
GRANT ALL ON FUNCTION utility_fnc_delete_riscossione(int4)                    TO public;
GRANT ALL ON FUNCTION utility_fnc_delete_stato_debitorio(int4,int4)           TO public;
GRANT ALL ON FUNCTION utility_fnc_ricerca_riscossione_json_dt(text,text,text) TO public;

GRANT ALL ON FUNCTION utility_fnc_aggiorna_sequence(int4)                     TO risca;
GRANT ALL ON FUNCTION utility_fnc_check_elaborazione(int4)                    TO risca;
GRANT ALL ON FUNCTION utility_fnc_check_riscossioni(int4)                     TO risca;
GRANT ALL ON FUNCTION utility_fnc_delete_file_450(int4,int4)                  TO risca;
GRANT ALL ON FUNCTION utility_fnc_delete_file_450_nosd(int4,int4)             TO risca;
GRANT ALL ON FUNCTION utility_fnc_delete_riscossione(int4)                    TO risca;
GRANT ALL ON FUNCTION utility_fnc_delete_stato_debitorio(int4,int4)           TO risca;
GRANT ALL ON FUNCTION utility_fnc_ricerca_riscossione_json_dt(text,text,text) TO risca;

GRANT ALL ON FUNCTION utility_fnc_aggiorna_sequence(int4)                     TO risca_rw;
GRANT ALL ON FUNCTION utility_fnc_check_elaborazione(int4)                    TO risca_rw;
GRANT ALL ON FUNCTION utility_fnc_check_riscossioni(int4)                     TO risca_rw;
GRANT ALL ON FUNCTION utility_fnc_delete_file_450(int4,int4)                  TO risca_rw;
GRANT ALL ON FUNCTION utility_fnc_delete_file_450_nosd(int4,int4)             TO risca_rw;
GRANT ALL ON FUNCTION utility_fnc_delete_riscossione(int4)                    TO risca_rw;
GRANT ALL ON FUNCTION utility_fnc_delete_stato_debitorio(int4,int4)           TO risca_rw;
GRANT ALL ON FUNCTION utility_fnc_ricerca_riscossione_json_dt(text,text,text) TO risca_rw; 