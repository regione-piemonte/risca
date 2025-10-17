/* *****************************************************
 * Copyright Regione Piemonte - 2025
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************/

-- v_archiviazione_acta_bonari_solleciti source

CREATE OR REPLACE VIEW v_archiviazione_acta_bonari_solleciti
AS WITH rec_principale AS (
         SELECT '-->'::text AS dati_spedizione,
            spe.id_ambito,
            amb.cod_ambito,
            spe.id_spedizione,
            spe.id_tipo_spedizione,
            spe.id_elabora AS id_elabora_spedizione,
            ela.cod_tipo_elabora,
            spe.data_protocollo,
            spe.data_scadenza,
            spe.anno,
            '-->'::text AS dati_tipo_spedizione,
            tsp.cod_tipo_spedizione,
            '-->'::text AS dati_spedizione_acta,
            rpa.id_spedizione_acta,
            rpa.id_elabora AS id_elabora_spedizione_acta,
            rpa.nome_dirigente_protempore,
            '-->'::text AS dati_accertamento,
            acc.id_accertamento,
            acc.cod_tipo_accertamento,
            acc.nap,
            acc.num_protocollo,
            '-->'::text AS dati_titolare_avviso,
            dti.id_soggetto,
            '-->'::text AS dati_soggetto_titolare_avviso,
            sog.cf_soggetto,
            sog.nome,
            sog.cognome,
            sog.den_soggetto,
            sog.id_tipo_soggetto,
            '-->'::text AS dati_tipo_soggetto,
            tsg.cod_tipo_soggetto,
            '-->'::text AS dati_amministrativi_titolare_accertamento,
            dam.codice_utenza,
            '-->'::text AS dati_invio_acta,
            dia.id_invio_acta,
            dia.flg_multiclassificazione,
            dia.flg_archiviata_acta,
            dia.data_invio,
            dia.cod_esito_invio_acta,
            dia.cod_esito_acquisizione_acta
           FROM risca_t_spedizione spe
             LEFT JOIN risca_d_ambito amb ON spe.id_ambito = amb.id_ambito
             LEFT JOIN risca_d_tipo_spedizione tsp ON spe.id_tipo_spedizione = tsp.id_tipo_spedizione
             JOIN risca_r_spedizione_acta rpa ON spe.id_spedizione = rpa.id_spedizione
             JOIN ( SELECT acc_1.id_accertamento,
                    acc_1.id_stato_debitorio,
                    acc_1.id_tipo_accertamento,
                    acc_1.id_file_450,
                    acc_1.num_protocollo,
                    acc_1.data_protocollo,
                    acc_1.data_scadenza,
                    acc_1.flg_restituito,
                    acc_1.flg_annullato,
                    acc_1.data_notifica,
                    acc_1.nota,
                    acc_1.gest_data_ins,
                    acc_1.gest_attore_ins,
                    acc_1.gest_data_upd,
                    acc_1.gest_attore_upd,
                    acc_1.gest_uid,
                    acc_1.id_spedizione,
                    tsd.nap,
                    dta.cod_tipo_accertamento
                   FROM risca_t_accertamento acc_1
                     LEFT JOIN risca_d_tipo_accertamento dta ON dta.id_tipo_accertamento = acc_1.id_tipo_accertamento
                     LEFT JOIN risca_t_stato_debitorio tsd ON acc_1.id_stato_debitorio = tsd.id_stato_debitorio) acc ON acc.id_spedizione = spe.id_spedizione
             LEFT JOIN ( SELECT elb.id_elabora,
                    dte.cod_tipo_elabora
                   FROM risca_t_elabora elb
                     JOIN risca_d_tipo_elabora dte ON elb.id_tipo_elabora = dte.id_tipo_elabora) ela ON spe.id_elabora = ela.id_elabora
             JOIN risca_r_soll_dati_titolare dti ON acc.id_accertamento = dti.id_accertamento
             JOIN risca_t_soggetto sog ON dti.id_soggetto = sog.id_soggetto
             JOIN risca_d_tipo_soggetto tsg ON sog.id_tipo_soggetto = tsg.id_tipo_soggetto
             JOIN risca_r_soll_dati_amministr dam ON acc.id_accertamento = dam.id_accertamento
             LEFT JOIN risca_t_invio_acta dia ON acc.id_accertamento = dia.id_accertamento
          WHERE rpa.flg_archiviata_acta = 0::numeric AND spe.id_tipo_spedizione = 3
          ORDER BY rpa.id_spedizione_acta, spe.id_tipo_spedizione, acc.id_accertamento
        )
 SELECT vab.dati_spedizione,
    vab.id_ambito,
    vab.cod_ambito,
    vab.id_spedizione,
    vab.id_tipo_spedizione,
    vab.id_elabora_spedizione,
    vab.cod_tipo_elabora,
    vab.data_protocollo,
    vab.data_scadenza,
    vab.anno,
    vab.dati_tipo_spedizione,
    vab.cod_tipo_spedizione,
    vab.dati_spedizione_acta,
    vab.id_spedizione_acta,
    vab.id_elabora_spedizione_acta,
    vab.nome_dirigente_protempore,
    vab.dati_accertamento,
    vab.id_accertamento,
    vab.cod_tipo_accertamento,
    vab.nap,
    vab.num_protocollo,
    vab.dati_titolare_avviso,
    vab.id_soggetto,
    vab.dati_soggetto_titolare_avviso,
    vab.cf_soggetto,
    vab.nome,
    vab.cognome,
    vab.den_soggetto,
    vab.id_tipo_soggetto,
    vab.dati_tipo_soggetto,
    vab.cod_tipo_soggetto,
    vab.dati_amministrativi_titolare_accertamento,
    vab.codice_utenza,
    vab.dati_invio_acta,
    vab.id_invio_acta,
    vab.flg_multiclassificazione,
    vab.flg_archiviata_acta,
    vab.data_invio,
    vab.cod_esito_invio_acta,
    vab.cod_esito_acquisizione_acta
   FROM rec_principale vab
     JOIN ( SELECT foo.codice_utenza,
            foo.nap,
            foo.id_accertamento
           FROM ( SELECT rtia.codice_utenza,
                    rtia.nap,
                    rtia.id_accertamento,
                    row_number() OVER (PARTITION BY rtia.codice_utenza ORDER BY rtia.codice_utenza, (COALESCE(rtia.nap, ''::character varying)) DESC) AS numriga
                   FROM rec_principale rtia
                  ORDER BY (COALESCE(rtia.nap, ''::character varying)) DESC) foo
          WHERE foo.numriga = 1) rva ON vab.codice_utenza::text = rva.codice_utenza::text AND vab.id_accertamento = rva.id_accertamento;

COMMENT ON VIEW v_archiviazione_acta_bonari_solleciti IS 'Contiene il riferimento al titolare.
Se presente un indirizzo alternativo nella riscossione, riporta l''id_soggetto + ''R'' + id_recapito
Se presente un id_gruppo nella riscossione, riporta l''id_soggetto + ''*'' + id_gruppo
altrimenti solo l''id_soggetto';


-- v_archiviazione_acta_ordinaria_speciale source

CREATE OR REPLACE VIEW v_archiviazione_acta_ordinaria_speciale
AS SELECT '-->'::text AS dati_spedizione,
    spe.id_ambito,
    amb.cod_ambito,
    spe.id_spedizione,
    spe.id_tipo_spedizione,
    spe.id_elabora AS id_elabora_spedizione,
    ela.cod_tipo_elabora,
    spe.data_protocollo,
    spe.data_scadenza,
    spe.anno,
    '-->'::text AS dati_tipo_spedizione,
    tsp.cod_tipo_spedizione,
    '-->'::text AS dati_spedizione_acta,
    rpa.id_spedizione_acta,
    rpa.id_elabora AS id_elabora_spedizione_acta,
    rpa.nome_dirigente_protempore,
    '-->'::text AS dati_avviso_pagamento,
    avv.nap,
    '-->'::text AS dati_titolare_avviso,
    dti.id_soggetto,
    '-->'::text AS dati_soggetto_titolare_avviso,
    sog.cf_soggetto,
    sog.nome,
    sog.cognome,
    sog.den_soggetto,
    sog.id_tipo_soggetto,
    '-->'::text AS dati_tipo_soggetto,
    tsg.cod_tipo_soggetto,
    '-->'::text AS dati_amministrativi_titolare_avviso,
    dam.codice_utenza,
    '-->'::text AS dati_invio_acta,
    dia.id_invio_acta,
    dia.flg_multiclassificazione,
    dia.flg_archiviata_acta,
    dia.data_invio,
    dia.cod_esito_invio_acta,
    dia.cod_esito_acquisizione_acta
   FROM risca_t_spedizione spe
     LEFT JOIN risca_d_ambito amb ON spe.id_ambito = amb.id_ambito
     LEFT JOIN risca_d_tipo_spedizione tsp ON spe.id_tipo_spedizione = tsp.id_tipo_spedizione
     JOIN risca_r_spedizione_acta rpa ON spe.id_spedizione = rpa.id_spedizione
     JOIN risca_t_avviso_pagamento avv ON avv.id_spedizione = spe.id_spedizione
     LEFT JOIN ( SELECT elb.id_elabora,
            dte.cod_tipo_elabora
           FROM risca_t_elabora elb
             JOIN risca_d_tipo_elabora dte ON elb.id_tipo_elabora = dte.id_tipo_elabora) ela ON spe.id_elabora = ela.id_elabora
     JOIN risca_r_avviso_dati_titolare dti ON avv.nap::text = dti.nap::text
     JOIN risca_t_soggetto sog ON dti.id_soggetto = sog.id_soggetto
     JOIN risca_d_tipo_soggetto tsg ON sog.id_tipo_soggetto = tsg.id_tipo_soggetto
     JOIN risca_r_avviso_dati_ammin dam ON dam.nap::text = dti.nap::text
     LEFT JOIN risca_t_invio_acta dia ON dam.nap::text = dia.nap::text
  WHERE rpa.flg_archiviata_acta = 0::numeric AND (spe.id_tipo_spedizione = ANY (ARRAY[1, 2, 4]))
  ORDER BY rpa.id_spedizione_acta, spe.id_tipo_spedizione, avv.nap;

COMMENT ON VIEW v_archiviazione_acta_ordinaria_speciale IS 'La vista permette di identificare le spedizioni ancora da archiviare per le bollettazioni ordinarie, speciali e grande-idroelettrico (ACTA-STARDAS)';


-- v_report_bilancio_altri_pagamenti source

CREATE OR REPLACE VIEW v_report_bilancio_altri_pagamenti
AS SELECT pag_da_visionare.tipologia_da_visionare,
    pag_da_visionare.id_ambito,
    pag_da_visionare.id_pagamento,
    pag_da_visionare.cod_tipo_modalita_pag,
    pag_da_visionare.des_tipo_modalita_pag,
    pag_da_visionare.data_op_val,
    pag_da_visionare.anno_data_op_val,
    pag_da_visionare.soggetto_versamento,
    pag_da_visionare.causale,
    pag_da_visionare.note,
    pag_da_visionare.importo_versato,
    pag_da_visionare.imp_da_assegnare,
    pag_da_visionare.id_pag_non_propri,
    pag_da_visionare.cod_tipo_imp_non_propri,
    pag_da_visionare.des_tipo_imp_non_propri,
    pag_da_visionare.importo_versato_non_propri,
    pag_da_visionare.importo_accerta_bilancio,
    pag_da_visionare.desc_accertamento,
    tba.id_bil_acc,
    tba.cod_bil_acc AS cod_accertamento,
    tba.cod_accerta_bilancio,
    tba.des_accerta_bilancio
   FROM ( SELECT 'SENZA_DETTAGLI_E_SENZA_NON_PROPRI'::text AS tipologia_da_visionare,
            pag.id_ambito,
            pag_da_visionare_1.id_pagamento,
            dmp.cod_tipo_modalita_pag,
            dmp.des_tipo_modalita_pag,
            pag.data_op_val,
            date_part('year'::text, pag.data_op_val) AS anno_data_op_val,
            pag.soggetto_versamento,
            pag.causale,
            pag.note,
            COALESCE(pag.importo_versato, 0::numeric) AS importo_versato,
            NULL::numeric AS imp_da_assegnare,
            NULL::integer AS id_pag_non_propri,
            NULL::text AS cod_tipo_imp_non_propri,
            NULL::text AS des_tipo_imp_non_propri,
            NULL::numeric AS importo_versato_non_propri,
            COALESCE(pag.importo_versato, 0::numeric) AS importo_accerta_bilancio,
            'Da assegnare'::text AS desc_accertamento
           FROM risca_t_pagamento pag
             LEFT JOIN risca_d_tipo_modalita_pag dmp ON pag.id_tipo_modalita_pag = dmp.id_tipo_modalita_pag
             JOIN ( SELECT pag_1.id_pagamento,
                    COALESCE(pag_1.imp_da_assegnare, 0::numeric) AS imp_da_assegnare,
                    COALESCE(dett_pag.quanti_dett_pag, 0::bigint) AS quanti_dett_pag,
                    COALESCE(pag_non_propri.quanti_pag_non_propri, 0::bigint) AS quanti_pag_non_propri
                   FROM risca_t_pagamento pag_1
                     LEFT JOIN ( SELECT rdp.id_pagamento,
                            count(*) AS quanti_dett_pag
                           FROM risca_r_dettaglio_pag rdp
                          GROUP BY rdp.id_pagamento) dett_pag ON pag_1.id_pagamento = dett_pag.id_pagamento
                     LEFT JOIN ( SELECT rpn.id_pagamento,
                            count(*) AS quanti_pag_non_propri
                           FROM risca_r_pag_non_propri rpn
                          GROUP BY rpn.id_pagamento) pag_non_propri ON pag_1.id_pagamento = pag_non_propri.id_pagamento) pag_da_visionare_1 ON pag.id_pagamento = pag_da_visionare_1.id_pagamento
          WHERE pag_da_visionare_1.quanti_dett_pag = 0 AND pag_da_visionare_1.quanti_pag_non_propri = 0 AND pag_da_visionare_1.imp_da_assegnare = 0::numeric
        UNION ALL
         SELECT 'CON_IMPORTO_DA_ASSEGNARE'::text AS tipologia_da_visionare,
            pag.id_ambito,
            pag.id_pagamento,
            dmp.cod_tipo_modalita_pag,
            dmp.des_tipo_modalita_pag,
            pag.data_op_val,
            date_part('year'::text, pag.data_op_val) AS anno_data_op_val,
            pag.soggetto_versamento,
            pag.causale,
            pag.note,
            COALESCE(pag.importo_versato, 0::numeric) AS importo_versato,
            COALESCE(pag.imp_da_assegnare, 0::numeric) AS imp_da_assegnare,
            NULL::integer AS id_pag_non_propri,
            NULL::text AS cod_tipo_imp_non_propri,
            NULL::text AS des_tipo_imp_non_propri,
            NULL::numeric AS importo_versato_non_propri,
            COALESCE(pag.imp_da_assegnare, 0::numeric) AS importo_accerta_bilancio,
            'Da assegnare'::text AS desc_accertamento
           FROM risca_t_pagamento pag
             LEFT JOIN risca_d_tipo_modalita_pag dmp ON pag.id_tipo_modalita_pag = dmp.id_tipo_modalita_pag
          WHERE pag.imp_da_assegnare <> 0::numeric
        UNION ALL
         SELECT 'CON_PAGAMENTO_NON_PROPRIO'::text AS tipologia_da_visionare,
            pag.id_ambito,
            pag.id_pagamento,
            dmp.cod_tipo_modalita_pag,
            dmp.des_tipo_modalita_pag,
            pag.data_op_val,
            date_part('year'::text, pag.data_op_val) AS anno_data_op_val,
            pag.soggetto_versamento,
            pag.causale,
            pag.note,
            COALESCE(pag.importo_versato, 0::numeric) AS importo_versato,
            NULL::numeric AS imp_da_assegnare,
            pag_non_propri_associati.id_pag_non_propri,
            pag_non_propri_associati.cod_tipo_imp_non_propri,
            pag_non_propri_associati.des_tipo_imp_non_propri,
            pag_non_propri_associati.importo_versato_non_propri,
            COALESCE(pag_non_propri_associati.importo_versato_non_propri, 0::numeric) AS importo_accerta_bilancio,
            pag_non_propri_associati.des_tipo_imp_non_propri AS desc_accertamento
           FROM risca_t_pagamento pag
             LEFT JOIN risca_d_tipo_modalita_pag dmp ON pag.id_tipo_modalita_pag = dmp.id_tipo_modalita_pag
             JOIN ( SELECT pag_non_propri_ass.id_pagamento,
                    pnp.id_pag_non_propri,
                    dtn.cod_tipo_imp_non_propri,
                    dtn.des_tipo_imp_non_propri,
                    pnp.importo_versato AS importo_versato_non_propri
                   FROM risca_r_pag_non_propri pnp
                     JOIN ( SELECT pag_1.id_pagamento,
                            COALESCE(pag_non_propri.quanti_pag_non_propri, 0::bigint) AS quanti_pag_non_propri
                           FROM risca_t_pagamento pag_1
                             LEFT JOIN ( SELECT rpn.id_pagamento,
                                    count(*) AS quanti_pag_non_propri
                                   FROM risca_r_pag_non_propri rpn
                                  GROUP BY rpn.id_pagamento) pag_non_propri ON pag_1.id_pagamento = pag_non_propri.id_pagamento) pag_non_propri_ass ON pnp.id_pagamento = pag_non_propri_ass.id_pagamento
                     LEFT JOIN risca_d_tipo_imp_non_propri dtn ON pnp.id_tipo_imp_non_propri = dtn.id_tipo_imp_non_propri
                  WHERE pag_non_propri_ass.quanti_pag_non_propri <> 0) pag_non_propri_associati ON pag.id_pagamento = pag_non_propri_associati.id_pagamento) pag_da_visionare
     LEFT JOIN ( SELECT tba_1.id_ambito,
            tba_1.id_bil_acc,
            tba_1.cod_bil_acc,
            tba_1.anno,
            tba_1.anno_competenza,
            dab.id_accerta_bilancio,
            dab.cod_accerta_bilancio,
            dab.des_accerta_bilancio
           FROM risca_t_bil_acc tba_1
             LEFT JOIN risca_d_accerta_bilancio dab ON tba_1.id_accerta_bilancio = dab.id_accerta_bilancio) tba ON pag_da_visionare.id_ambito = tba.id_ambito AND pag_da_visionare.anno_data_op_val = tba.anno::double precision AND pag_da_visionare.anno_data_op_val = tba.anno_competenza::double precision AND tba.cod_accerta_bilancio::text = 'CANONE'::text
  ORDER BY pag_da_visionare.data_op_val;

COMMENT ON VIEW v_report_bilancio_altri_pagamenti IS 'La vista permette di estrarre gli altri pagamenti non considerati nella risca_r_dettaglio_pag_bil_acc (SENZA_DETTAGLI_E_SENZA_NON_PROPRI, CON_IMPORTO_DA_ASSEGNARE, CON_PAGAMENTO_NON_PROPRIO)';


-- v_report_bilancio_dettagli_bil_acc source

CREATE OR REPLACE VIEW v_report_bilancio_dettagli_bil_acc
AS SELECT '-->'::text AS dati_bilancio_accertamento,
    tba.id_ambito,
    tba.voce_sintesi,
    tba.id_bil_acc,
    tba.id_accerta_bilancio,
    tba.cod_bil_acc AS cod_accertamento,
    tba.anno,
    tba.des_bil_acc AS accertamento,
    tba.anno_competenza,
    '-->'::text AS dati_tipo_accerta_bilancio,
    dab.cod_accerta_bilancio,
    dab.des_accerta_bilancio,
    '-->'::text AS dati_dettaglio_pag_ba,
    dpb.id_dettaglio_pag_bil_acc,
    dpb.flg_ruolo,
    dpb.flg_pubblico,
    dpb.flg_eccedenza,
    dpb.importo_accerta_bilancio,
    '-->'::text AS dati_dettaglio_pagamento,
    dpa.id_dettaglio_pag,
    dpa.id_rata_sd,
    dpa.id_pagamento,
    dpa.importo_versato,
    dpa.interessi_maturati,
    dpa.data_op_val,
    date_part('year'::text, dpa.data_op_val) AS anno_data_op_val,
    dpa.note,
    dpa.id_tipo_modalita_pag,
    dpa.cod_tipo_modalita_pag,
    dpa.des_tipo_modalita_pag,
    dpa.id_stato_debitorio,
    dpa.canone_dovuto,
    dpa.data_scadenza_pagamento,
    dpa.id_riscossione,
    dpa.nap,
    dpa.cod_riscossione,
    dpa.imp_spese_notifica,
        CASE
            WHEN dpb.flg_eccedenza = 1::numeric THEN dpa.tot_importo_compensato
            ELSE 0::numeric
        END AS tot_importo_compensato,
        CASE
            WHEN dpb.flg_eccedenza = 1::numeric THEN dpa.tot_importo_rimborso
            ELSE 0::numeric
        END AS tot_importo_rimborso
   FROM ( SELECT x.id_bil_acc,
            x.id_ambito,
            x.cod_bil_acc,
            x.des_bil_acc,
            x.anno,
            x.note_backoffice,
            x.dati_spec_risc,
            x.gest_data_ins,
            x.gest_attore_ins,
            x.gest_data_upd,
            x.gest_attore_upd,
            x.gest_uid,
            x.anno_competenza,
            x.data_inizio_validita,
            x.data_fine_validita,
            x.id_accerta_bilancio,
                CASE
                    WHEN x.id_accerta_bilancio <> ALL (ARRAY[10, 20]) THEN 'Canoni e monetizzazione'::text
                    WHEN x.id_accerta_bilancio = 10 THEN 'Interessi'::text
                    WHEN x.id_accerta_bilancio = 20 THEN 'Spese di notifica'::text
                    ELSE '*** errore ***'::text
                END AS voce_sintesi
           FROM risca_t_bil_acc x
          ORDER BY x.id_bil_acc) tba
     LEFT JOIN risca_d_accerta_bilancio dab ON tba.id_accerta_bilancio = dab.id_accerta_bilancio
     JOIN risca_r_dettaglio_pag_bil_acc dpb ON tba.id_bil_acc = dpb.id_bil_acc
     LEFT JOIN ( SELECT rdp.id_dettaglio_pag,
            rdp.id_rata_sd,
            rdp.id_pagamento,
            rdp.importo_versato,
            rdp.interessi_maturati AS interessi_maturati_dpa,
            rat.interessi_maturati,
            pag.data_op_val,
            pag.note,
            pag.id_tipo_modalita_pag,
            dmp.cod_tipo_modalita_pag,
            dmp.des_tipo_modalita_pag,
            rat.id_stato_debitorio,
            rat.canone_dovuto,
            rat.data_scadenza_pagamento,
            sde.id_riscossione,
            sde.nap,
            sde.imp_spese_notifica,
            tco.tot_importo_compensato,
            tri.tot_importo_rimborso,
            ris.cod_riscossione
           FROM risca_r_dettaglio_pag rdp
             LEFT JOIN risca_t_pagamento pag ON rdp.id_pagamento = pag.id_pagamento
             LEFT JOIN risca_d_tipo_modalita_pag dmp ON pag.id_tipo_modalita_pag = dmp.id_tipo_modalita_pag
             LEFT JOIN risca_r_rata_sd rat ON rdp.id_rata_sd = rat.id_rata_sd
             LEFT JOIN risca_t_stato_debitorio sde ON rat.id_stato_debitorio = sde.id_stato_debitorio
             LEFT JOIN risca_t_riscossione ris ON sde.id_riscossione = ris.id_riscossione
             LEFT JOIN ( SELECT rim.id_stato_debitorio,
                    sum(rim.imp_rimborso) AS tot_importo_compensato
                   FROM risca_r_rimborso rim
                  WHERE rim.id_tipo_rimborso = 3
                  GROUP BY rim.id_stato_debitorio) tco ON sde.id_stato_debitorio = tco.id_stato_debitorio
             LEFT JOIN ( SELECT rim.id_stato_debitorio,
                    sum(rim.imp_rimborso) AS tot_importo_rimborso
                   FROM risca_r_rimborso rim
                  WHERE rim.id_tipo_rimborso = 1
                  GROUP BY rim.id_stato_debitorio) tri ON sde.id_stato_debitorio = tri.id_stato_debitorio) dpa ON dpb.id_dettaglio_pag = dpa.id_dettaglio_pag
  ORDER BY dpa.data_op_val, dpa.id_pagamento, dpa.id_dettaglio_pag, dpb.id_dettaglio_pag_bil_acc;

COMMENT ON VIEW v_report_bilancio_dettagli_bil_acc IS 'La vista permette di estrarre i dettagli di pagamento (Accertamento Bilancio) necessari a produrre il relativo Report';


-- v_riscossioni_bollettazione_ordinaria source

CREATE OR REPLACE VIEW v_riscossioni_bollettazione_ordinaria
AS WITH rec_principale AS (
         SELECT '-->'::text AS dati_recapito_principale,
            rrr.id_riscossione AS id_riscossione_p,
            rrr.id_recapito AS id_recapito_p,
            dtr.cod_tipo_recapito AS cod_tipo_recapito_p,
            dtr.des_tipo_recapito AS des_tipo_recapito_p,
            dti.cod_tipo_invio AS cod_tipo_invio_p,
            dti.des_tipo_invio AS des_tipo_invio_p
           FROM risca_r_riscossione_recapito rrr
             LEFT JOIN risca_r_recapito rep ON rrr.id_recapito = rep.id_recapito
             JOIN risca_d_tipo_recapito dtr ON rep.id_tipo_recapito = dtr.id_tipo_recapito
             JOIN risca_d_tipo_invio dti ON rep.id_tipo_invio = dti.id_tipo_invio
          WHERE rep.id_tipo_recapito = 1
        ), rec_alternativo AS (
         SELECT '-->'::text AS dati_recapito_alternativo,
            rrr.id_riscossione AS id_riscossione_a,
            rrr.id_recapito AS id_recapito_a,
            dtr.cod_tipo_recapito AS cod_tipo_recapito_a,
            dtr.des_tipo_recapito AS des_tipo_recapito_a,
            dti.cod_tipo_invio AS cod_tipo_invio_a,
            dti.des_tipo_invio AS des_tipo_invio_a
           FROM risca_r_riscossione_recapito rrr
             LEFT JOIN risca_r_recapito rep ON rrr.id_recapito = rep.id_recapito
             JOIN risca_d_tipo_recapito dtr ON rep.id_tipo_recapito = dtr.id_tipo_recapito
             JOIN risca_d_tipo_invio dti ON rep.id_tipo_invio = dti.id_tipo_invio
          WHERE rep.id_tipo_recapito = 2
        ), imp_compensare AS (
         SELECT sta_1.id_riscossione,
            sum(rim.imp_rimborso) AS sum_imp_rimborso_riscossione
           FROM risca_r_rimborso rim
             LEFT JOIN risca_t_stato_debitorio sta_1 ON rim.id_stato_debitorio = sta_1.id_stato_debitorio
          WHERE rim.id_tipo_rimborso = 2
          GROUP BY sta_1.id_riscossione
        ), stato_contribuzione AS (
         SELECT contribuzione.id_riscossione,
            contribuzione.id_stato_contribuzione,
            contribuzione.des_stato_contribuzione,
            contribuzione.periodo_pagamento,
            contribuzione.data_richiesta_protocollo
           FROM ( SELECT lista.id_riscossione,
                    lista.id_stato_contribuzione,
                    lista.des_stato_contribuzione,
                    lista.periodo_pagamento,
                    lista.data_richiesta_protocollo,
                    row_number() OVER (PARTITION BY lista.id_riscossione ORDER BY lista.periodo_pagamento DESC, lista.data_richiesta_protocollo DESC) AS numero_riga
                   FROM ( SELECT sta_1.id_riscossione,
                            dsc.id_stato_contribuzione,
                            dsc.des_stato_contribuzione,
                            max(sta_1.desc_periodo_pagamento::text) AS periodo_pagamento,
                            max(sta_1.data_richiesta_protocollo) AS data_richiesta_protocollo
                           FROM risca_t_stato_debitorio sta_1
                             LEFT JOIN risca_d_stato_contribuzione dsc ON sta_1.id_stato_contribuzione = dsc.id_stato_contribuzione
                          WHERE sta_1.flg_invio_speciale <> 1::numeric
                          GROUP BY sta_1.id_riscossione, dsc.id_stato_contribuzione, dsc.des_stato_contribuzione) lista) contribuzione
          WHERE contribuzione.numero_riga = 1
        )
 SELECT '-->'::text AS dati_riscossione,
    ris.id_riscossione,
    ris.id_tipo_riscossione,
    ris.id_stato_riscossione,
    ris.id_soggetto,
    ris.id_gruppo_soggetto,
        CASE
            WHEN rec_alternativo.id_recapito_a IS NULL THEN btrim(to_char(ris.id_soggetto, '9999999999'::text))
            ELSE (ris.id_soggetto || 'R'::text) || rec_alternativo.id_recapito_a
        END AS id_titolare,
    ris.id_tipo_autorizza,
    ris.cod_riscossione,
    ris.cod_riscossione_prov,
    ris.cod_riscossione_prog,
    ris.id_sigla_riscossione,
    ris.cod_riscossione_lettera_prov,
    ris.num_pratica,
    ris.flg_prenotata,
    ris.motivo_prenotazione,
    ris.note_riscossione,
    ris.data_ini_concessione,
    ris.data_scad_concessione,
    ris.data_ini_sosp_canone,
    ris.data_fine_sosp_canone,
    ris.json_dt,
    ris.id_componente_dt,
    '-->'::text AS dati_tipo_riscossione,
    tri.cod_tipo_riscossione,
    tri.des_tipo_riscossione,
    '-->'::text AS dati_stato_riscossione,
    sri.cod_stato_riscossione,
    sri.des_stato_riscossione,
    '-->'::text AS dati_tipo_autorizza,
    rdt.cod_tipo_autorizza,
    rdt.des_tipo_autorizza,
    '-->'::text AS dati_ambito,
    amb.cod_ambito,
    amb.des_ambito,
    '-->'::text AS dati_indirizzo_principale,
    rec_principale.id_riscossione_p,
    rec_principale.id_recapito_p,
    rec_principale.cod_tipo_recapito_p,
    rec_principale.des_tipo_recapito_p,
    rec_principale.cod_tipo_invio_p,
        CASE
            WHEN rec_principale.cod_tipo_invio_p::text = 'C'::text THEN 'CARTA'::text
            WHEN rec_principale.cod_tipo_invio_p::text = 'P'::text THEN 'PEC'::text
            WHEN rec_principale.cod_tipo_invio_p::text = 'E'::text THEN 'EMAIL'::text
            ELSE upper(rec_principale.des_tipo_invio_p::text)
        END AS des_tipo_invio_p,
    '-->'::text AS dati_indirizzo_alternativo,
    rec_alternativo.id_riscossione_a,
    rec_alternativo.id_recapito_a,
    rec_alternativo.cod_tipo_recapito_a,
    rec_alternativo.des_tipo_recapito_a,
    rec_alternativo.cod_tipo_invio_a,
        CASE
            WHEN rec_alternativo.cod_tipo_invio_a::text = 'C'::text THEN 'CARTA'::text
            WHEN rec_alternativo.cod_tipo_invio_a::text = 'P'::text THEN 'PEC'::text
            WHEN rec_alternativo.cod_tipo_invio_a::text = 'E'::text THEN 'EMAIL'::text
            ELSE upper(rec_alternativo.des_tipo_invio_a::text)
        END AS des_tipo_invio_a,
    '-->'::text AS dati_rimborso,
    COALESCE(imp_compensare.sum_imp_rimborso_riscossione, 0::numeric) AS sum_imp_rimborso_riscossione,
    stato_contribuzione.id_stato_contribuzione,
    stato_contribuzione.des_stato_contribuzione AS stato_contribuzione
   FROM risca_t_riscossione ris
     LEFT JOIN risca_d_tipo_riscossione tri ON ris.id_tipo_riscossione = tri.id_tipo_riscossione
     LEFT JOIN risca_d_stato_riscossione sri ON ris.id_stato_riscossione = sri.id_stato_riscossione
     LEFT JOIN risca_d_tipo_autorizza rdt ON ris.id_tipo_autorizza = rdt.id_tipo_autorizza
     LEFT JOIN risca_d_ambito amb ON tri.id_ambito = amb.id_ambito
     LEFT JOIN risca_d_componente_dt dcd ON ris.id_componente_dt = dcd.id_componente_dt
     LEFT JOIN rec_principale ON ris.id_riscossione = rec_principale.id_riscossione_p
     LEFT JOIN rec_alternativo ON ris.id_riscossione = rec_alternativo.id_riscossione_a
     LEFT JOIN imp_compensare ON ris.id_riscossione = imp_compensare.id_riscossione
     LEFT JOIN stato_contribuzione ON ris.id_riscossione = stato_contribuzione.id_riscossione
  WHERE (ris.id_tipo_riscossione = ANY (ARRAY[1, 2, 4])) AND NOT (EXISTS ( SELECT 'XXX'::text
           FROM risca_t_stato_debitorio sde
          WHERE ris.id_riscossione = sde.id_riscossione AND sde.flg_invio_speciale = 1::numeric))
  ORDER BY (
        CASE
            WHEN rec_alternativo.id_recapito_a IS NULL THEN btrim(to_char(ris.id_soggetto, '9999999999'::text))
            ELSE (ris.id_soggetto || 'R'::text) || rec_alternativo.id_recapito_a
        END), imp_compensare.sum_imp_rimborso_riscossione, ris.id_riscossione;

COMMENT ON VIEW v_riscossioni_bollettazione_ordinaria IS 'La vista permette di identificare le riscossioni da elaborare durante la bollettazione ordinaria';


-- v_riscossioni_bollettazione_speciale source

CREATE OR REPLACE VIEW v_riscossioni_bollettazione_speciale
AS WITH rec_principale AS (
         SELECT '-->'::text AS dati_recapito_principale,
            rrr.id_riscossione AS id_riscossione_p,
            rrr.id_recapito AS id_recapito_p,
            dtr.cod_tipo_recapito AS cod_tipo_recapito_p,
            dtr.des_tipo_recapito AS des_tipo_recapito_p,
            dti.cod_tipo_invio AS cod_tipo_invio_p,
            dti.des_tipo_invio AS des_tipo_invio_p
           FROM risca_r_riscossione_recapito rrr
             LEFT JOIN risca_r_recapito rep ON rrr.id_recapito = rep.id_recapito
             JOIN risca_d_tipo_recapito dtr ON rep.id_tipo_recapito = dtr.id_tipo_recapito
             JOIN risca_d_tipo_invio dti ON rep.id_tipo_invio = dti.id_tipo_invio
          WHERE rep.id_tipo_recapito = 1
        ), rec_alternativo AS (
         SELECT '-->'::text AS dati_recapito_alternativo,
            rrr.id_riscossione AS id_riscossione_a,
            rrr.id_recapito AS id_recapito_a,
            dtr.cod_tipo_recapito AS cod_tipo_recapito_a,
            dtr.des_tipo_recapito AS des_tipo_recapito_a,
            dti.cod_tipo_invio AS cod_tipo_invio_a,
            dti.des_tipo_invio AS des_tipo_invio_a
           FROM risca_r_riscossione_recapito rrr
             LEFT JOIN risca_r_recapito rep ON rrr.id_recapito = rep.id_recapito
             JOIN risca_d_tipo_recapito dtr ON rep.id_tipo_recapito = dtr.id_tipo_recapito
             JOIN risca_d_tipo_invio dti ON rep.id_tipo_invio = dti.id_tipo_invio
          WHERE rep.id_tipo_recapito = 2
        ), imp_compensare AS (
         SELECT sta_1.id_riscossione,
            sum(rim.imp_rimborso) AS sum_imp_rimborso_riscossione
           FROM risca_r_rimborso rim
             LEFT JOIN risca_t_stato_debitorio sta_1 ON rim.id_stato_debitorio = sta_1.id_stato_debitorio
          WHERE rim.id_tipo_rimborso = 2
          GROUP BY sta_1.id_riscossione
        ), stato_contribuzione AS (
         SELECT contribuzione.id_riscossione,
            contribuzione.id_stato_contribuzione,
            contribuzione.des_stato_contribuzione,
            contribuzione.periodo_pagamento,
            contribuzione.data_richiesta_protocollo
           FROM ( SELECT lista.id_riscossione,
                    lista.id_stato_contribuzione,
                    lista.des_stato_contribuzione,
                    lista.periodo_pagamento,
                    lista.data_richiesta_protocollo,
                    row_number() OVER (PARTITION BY lista.id_riscossione ORDER BY lista.des_stato_contribuzione DESC) AS numero_riga
                   FROM ( SELECT sta_1.id_riscossione,
                            dsc.id_stato_contribuzione,
                            dsc.des_stato_contribuzione,
                            max(sta_1.desc_periodo_pagamento::text) AS periodo_pagamento,
                            max(sta_1.data_richiesta_protocollo) AS data_richiesta_protocollo
                           FROM risca_t_stato_debitorio sta_1
                             LEFT JOIN risca_d_stato_contribuzione dsc ON sta_1.id_stato_contribuzione = dsc.id_stato_contribuzione
                          WHERE sta_1.flg_invio_speciale = ANY (ARRAY[1::numeric, 2::numeric])
                          GROUP BY sta_1.id_riscossione, dsc.id_stato_contribuzione, dsc.des_stato_contribuzione) lista) contribuzione
          WHERE contribuzione.numero_riga = 1
        )
 SELECT '-->'::text AS dati_riscossione,
    ris.id_riscossione,
    ris.id_tipo_riscossione,
    ris.id_stato_riscossione,
    ris.id_soggetto,
    ris.id_gruppo_soggetto,
        CASE
            WHEN rec_alternativo.id_recapito_a IS NULL THEN btrim(to_char(ris.id_soggetto, '9999999999'::text))
            ELSE (ris.id_soggetto || 'R'::text) || rec_alternativo.id_recapito_a
        END AS id_titolare,
    ris.id_tipo_autorizza,
    ris.cod_riscossione,
    ris.cod_riscossione_prov,
    ris.cod_riscossione_prog,
    ris.id_sigla_riscossione,
    ris.cod_riscossione_lettera_prov,
    ris.num_pratica,
    ris.flg_prenotata,
    ris.motivo_prenotazione,
    ris.note_riscossione,
    ris.data_ini_concessione,
    ris.data_scad_concessione,
    ris.data_ini_sosp_canone,
    ris.data_fine_sosp_canone,
    ris.json_dt,
    ris.id_componente_dt,
    '-->'::text AS dati_tipo_riscossione,
    tri.cod_tipo_riscossione,
    tri.des_tipo_riscossione,
    '-->'::text AS dati_stato_riscossione,
    sri.cod_stato_riscossione,
    sri.des_stato_riscossione,
    '-->'::text AS dati_tipo_autorizza,
    rdt.cod_tipo_autorizza,
    rdt.des_tipo_autorizza,
    '-->'::text AS dati_ambito,
    amb.cod_ambito,
    amb.des_ambito,
    '-->'::text AS dati_indirizzo_principale,
    rec_principale.id_riscossione_p,
    rec_principale.id_recapito_p,
    rec_principale.cod_tipo_recapito_p,
    rec_principale.des_tipo_recapito_p,
    rec_principale.cod_tipo_invio_p,
        CASE
            WHEN rec_principale.cod_tipo_invio_p::text = 'C'::text THEN 'CARTA'::text
            WHEN rec_principale.cod_tipo_invio_p::text = 'P'::text THEN 'PEC'::text
            WHEN rec_principale.cod_tipo_invio_p::text = 'E'::text THEN 'EMAIL'::text
            ELSE upper(rec_principale.des_tipo_invio_p::text)
        END AS des_tipo_invio_p,
    '-->'::text AS dati_indirizzo_alternativo,
    rec_alternativo.id_riscossione_a,
    rec_alternativo.id_recapito_a,
    rec_alternativo.cod_tipo_recapito_a,
    rec_alternativo.des_tipo_recapito_a,
    rec_alternativo.cod_tipo_invio_a,
        CASE
            WHEN rec_alternativo.cod_tipo_invio_a::text = 'C'::text THEN 'CARTA'::text
            WHEN rec_alternativo.cod_tipo_invio_a::text = 'P'::text THEN 'PEC'::text
            WHEN rec_alternativo.cod_tipo_invio_a::text = 'E'::text THEN 'EMAIL'::text
            ELSE upper(rec_alternativo.des_tipo_invio_a::text)
        END AS des_tipo_invio_a,
    '-->'::text AS dati_rimborso,
    COALESCE(imp_compensare.sum_imp_rimborso_riscossione, 0::numeric) AS sum_imp_rimborso_riscossione,
    '-->'::text AS dati_stato_debitorio,
    sta.id_stato_debitorio,
    sta.desc_periodo_pagamento,
    sta.flg_invio_speciale,
    stato_contribuzione.id_stato_contribuzione,
    stato_contribuzione.des_stato_contribuzione AS stato_contribuzione
   FROM risca_t_riscossione ris
     LEFT JOIN risca_d_tipo_riscossione tri ON ris.id_tipo_riscossione = tri.id_tipo_riscossione
     LEFT JOIN risca_d_stato_riscossione sri ON ris.id_stato_riscossione = sri.id_stato_riscossione
     LEFT JOIN risca_d_tipo_autorizza rdt ON ris.id_tipo_autorizza = rdt.id_tipo_autorizza
     LEFT JOIN risca_d_ambito amb ON tri.id_ambito = amb.id_ambito
     LEFT JOIN risca_d_componente_dt dcd ON ris.id_componente_dt = dcd.id_componente_dt
     LEFT JOIN risca_t_stato_debitorio sta ON ris.id_riscossione = sta.id_riscossione
     LEFT JOIN rec_principale ON ris.id_riscossione = rec_principale.id_riscossione_p
     LEFT JOIN rec_alternativo ON ris.id_riscossione = rec_alternativo.id_riscossione_a
     LEFT JOIN imp_compensare ON ris.id_riscossione = imp_compensare.id_riscossione
     LEFT JOIN stato_contribuzione ON ris.id_riscossione = stato_contribuzione.id_riscossione
  WHERE sta.flg_invio_speciale = ANY (ARRAY[1::numeric, 2::numeric])
  ORDER BY (
        CASE
            WHEN rec_alternativo.id_recapito_a IS NULL THEN btrim(to_char(ris.id_soggetto, '9999999999'::text))
            ELSE (ris.id_soggetto || 'R'::text) || rec_alternativo.id_recapito_a
        END), imp_compensare.sum_imp_rimborso_riscossione, ris.id_riscossione;

COMMENT ON VIEW v_riscossioni_bollettazione_speciale IS 'La vista permette di identificare le riscossioni da elaborare durante la bollettazione speciale';


-- v_riscossioni_report_ricerca source

CREATE OR REPLACE VIEW v_riscossioni_report_ricerca
AS SELECT dtr.id_ambito,
    ris.id_riscossione,
    ris.cod_riscossione,
    ris.num_pratica,
    dtr.des_tipo_riscossione,
    dsr.des_stato_riscossione,
    ris.data_rinuncia_revoca,
    dts.cod_tipo_soggetto,
    tgs.des_gruppo_soggetto,
    sog.cf_soggetto AS codice_fiscale,
    sog.partita_iva_soggetto AS partita_iva,
        CASE
            WHEN dts.cod_tipo_soggetto::text = 'PF'::text THEN ((sog.cognome::text || ' '::text) || sog.nome::text)::character varying
            ELSE sog.den_soggetto
        END AS titolare,
    rep.presso,
    (rep.indirizzo::text || ', '::text) || rep.num_civico::text AS via_principale,
    rep.cap_recapito,
        CASE
            WHEN rep.citta_estera_recapito IS NOT NULL THEN rep.citta_estera_recapito
            ELSE rep.denominazione_comune
        END AS citta,
    rep.denom_nazione,
    rep.destipoinvio AS tipo_invio,
    rep.des_pec,
    rep.des_email,
    rea.presso AS presso_alternativo,
    (rea.indirizzo::text || ', '::text) || rea.num_civico::text AS via_alternativa,
    rea.cap_recapito AS cap_recapito_alternativo,
        CASE
            WHEN rea.citta_estera_recapito IS NOT NULL THEN rea.citta_estera_recapito
            ELSE rea.denominazione_comune
        END AS citta_indirizzo_alternativo,
    rea.denom_nazione AS denom_nazione_alternativo,
    rea.destipoinvio AS destipoinvio_alternativo,
    rea.des_pec AS des_pec_alternativo,
    rea.des_email AS des_email_alternativo,
    ldi.istanza_e,
    lim.data_provvedimento AS data_ultima_istanza,
    prv.des_tipo_provvedimento,
    prv.des_tipo_titolo,
    prv.num_titolo,
    prv.data_provvedimento AS data_titolo,
    ris.data_ini_concessione,
    ris.data_scad_concessione,
    ris.data_ini_sosp_canone,
    ris.data_fine_sosp_canone,
    replace((((((ris.json_dt -> 'riscossione'::text) -> 'dati_tecnici'::text) -> 'dati_generali'::text) -> 'corpo_idrico_captazione'::text)::character varying)::text, '"'::text, ''::text) AS corpo_idrico,
    replace((((((ris.json_dt -> 'riscossione'::text) -> 'dati_tecnici'::text) -> 'dati_generali'::text) -> 'comune'::text)::character varying)::text, '"'::text, ''::text) AS corpo_captazione,
    replace((((((ris.json_dt -> 'riscossione'::text) -> 'dati_tecnici'::text) -> 'dati_generali'::text) -> 'nome_impianto'::text)::character varying)::text, '"'::text, ''::text) AS nome_impianto,
    ris.note_riscossione,
    prv.note
   FROM risca_t_riscossione ris
     LEFT JOIN risca_d_tipo_riscossione dtr ON ris.id_tipo_riscossione = dtr.id_tipo_riscossione
     LEFT JOIN risca_d_stato_riscossione dsr ON ris.id_stato_riscossione = dsr.id_stato_riscossione
     LEFT JOIN risca_t_soggetto sog ON ris.id_soggetto = sog.id_soggetto
     LEFT JOIN risca_d_tipo_soggetto dts ON sog.id_tipo_soggetto = dts.id_tipo_soggetto
     LEFT JOIN risca_t_gruppo_soggetto tgs ON ris.id_gruppo_soggetto = tgs.id_gruppo_soggetto
     LEFT JOIN ( SELECT rrr.id_riscossione,
            rrr.id_recapito,
            rre.id_tipo_recapito,
            rre.presso,
            rre.indirizzo,
            rre.num_civico,
            rre.cap_recapito,
            dco.denom_comune AS denominazione_comune,
            dna.denom_nazione,
            rre.citta_estera_recapito,
            dti.des_tipo_invio AS destipoinvio,
            rre.pec AS des_pec,
            rre.email AS des_email,
            dtr_1.cod_tipo_recapito
           FROM risca_r_riscossione_recapito rrr
             LEFT JOIN risca_r_recapito rre ON rrr.id_recapito = rre.id_recapito
             LEFT JOIN risca_d_comune dco ON rre.id_comune_recapito = dco.id_comune
             LEFT JOIN risca_d_nazione dna ON rre.id_nazione_recapito = dna.id_nazione
             LEFT JOIN risca_d_tipo_recapito dtr_1 ON rre.id_tipo_recapito = dtr_1.id_tipo_recapito
             LEFT JOIN risca_d_tipo_invio dti ON rre.id_tipo_invio = dti.id_tipo_invio
          WHERE dtr_1.cod_tipo_recapito::text = 'P'::text) rep ON ris.id_riscossione = rep.id_riscossione
     LEFT JOIN ( SELECT rrr.id_riscossione,
            rrr.id_recapito,
            rre.id_tipo_recapito,
            rre.presso,
            rre.indirizzo,
            rre.num_civico,
            rre.cap_recapito,
            dco.denom_comune AS denominazione_comune,
            dna.denom_nazione,
            rre.citta_estera_recapito,
            dti.des_tipo_invio AS destipoinvio,
            rre.pec AS des_pec,
            rre.email AS des_email,
            dtr_1.cod_tipo_recapito
           FROM risca_r_riscossione_recapito rrr
             LEFT JOIN risca_r_recapito rre ON rrr.id_recapito = rre.id_recapito
             LEFT JOIN risca_d_comune dco ON rre.id_comune_recapito = dco.id_comune
             LEFT JOIN risca_d_nazione dna ON rre.id_nazione_recapito = dna.id_nazione
             LEFT JOIN risca_d_tipo_recapito dtr_1 ON rre.id_tipo_recapito = dtr_1.id_tipo_recapito
             LEFT JOIN risca_d_tipo_invio dti ON rre.id_tipo_invio = dti.id_tipo_invio
          WHERE dtr_1.cod_tipo_recapito::text = 'A'::text) rea ON ris.id_riscossione = rea.id_riscossione
     LEFT JOIN ( SELECT lis.id_riscossione,
            string_agg(lis.des_tipo_provvedimento::text, ', '::text) AS istanza_e
           FROM ( SELECT rpr.id_riscossione,
                    dtp.des_tipo_provvedimento
                   FROM risca_r_provvedimento rpr
                     LEFT JOIN risca_d_tipo_titolo dtt ON rpr.id_tipo_titolo = dtt.id_tipo_titolo
                     LEFT JOIN risca_d_tipo_provvedimento dtp ON rpr.id_tipo_provvedimento = dtp.id_tipo_provvedimento
                  WHERE dtp.flg_istanza = 1::numeric
                  ORDER BY rpr.id_riscossione, (COALESCE(rpr.data_provvedimento, to_date('1000/01/01'::text, 'YYYY/MM/DD'::text))) DESC) lis
          GROUP BY lis.id_riscossione) ldi ON ris.id_riscossione = ldi.id_riscossione
     LEFT JOIN ( SELECT DISTINCT ON (rpr.id_riscossione) rpr.id_provvedimento,
            rpr.id_riscossione,
            dtt.id_tipo_titolo,
            dtp.flg_istanza,
            dtp.des_tipo_provvedimento,
            rpr.data_provvedimento,
            rpr.num_titolo
           FROM risca_r_provvedimento rpr
             LEFT JOIN risca_d_tipo_titolo dtt ON rpr.id_tipo_titolo = dtt.id_tipo_titolo
             LEFT JOIN risca_d_tipo_provvedimento dtp ON rpr.id_tipo_provvedimento = dtp.id_tipo_provvedimento
          WHERE dtp.flg_istanza = 1::numeric AND rpr.data_provvedimento IS NOT NULL
          ORDER BY rpr.id_riscossione, rpr.data_provvedimento DESC) lim ON ris.id_riscossione = lim.id_riscossione
     LEFT JOIN ( SELECT DISTINCT ON (rpr.id_riscossione) rpr.id_riscossione,
            dtp.flg_istanza,
            dtp.des_tipo_provvedimento,
            dtt.des_tipo_titolo,
            rpr.num_titolo,
            rpr.data_provvedimento,
            rpr.note
           FROM risca_r_provvedimento rpr
             LEFT JOIN risca_d_tipo_titolo dtt ON rpr.id_tipo_titolo = dtt.id_tipo_titolo
             LEFT JOIN risca_d_tipo_provvedimento dtp ON rpr.id_tipo_provvedimento = dtp.id_tipo_provvedimento
          WHERE dtp.flg_istanza = 0::numeric
          ORDER BY rpr.id_riscossione, rpr.data_provvedimento DESC, rpr.id_provvedimento DESC) prv ON ris.id_riscossione = prv.id_riscossione;

COMMENT ON VIEW v_riscossioni_report_ricerca IS 'La vista permette di estrarre i dati necessari a produrre il report richiesto dalla ricerca avanzata';


-- v_stati_debitori_avvisi_e_solleciti source

CREATE OR REPLACE VIEW v_stati_debitori_avvisi_e_solleciti
AS WITH rec_principale AS (
         SELECT '-->'::text AS dati_recapito_principale,
            rrr.id_riscossione AS id_riscossione_p,
            rrr.id_recapito AS id_recapito_p,
            dtr.cod_tipo_recapito AS cod_tipo_recapito_p,
            dtr.des_tipo_recapito AS des_tipo_recapito_p,
            dti.cod_tipo_invio AS cod_tipo_invio_p,
            dti.des_tipo_invio AS des_tipo_invio_p,
            dts.cod_tipo_sede AS cod_tipo_sede_p,
            dts.des_tipo_sede AS des_tipo_sede_p
           FROM risca_r_riscossione_recapito rrr
             LEFT JOIN risca_r_recapito rep ON rrr.id_recapito = rep.id_recapito
             JOIN risca_d_tipo_recapito dtr ON rep.id_tipo_recapito = dtr.id_tipo_recapito
             JOIN risca_d_tipo_invio dti ON rep.id_tipo_invio = dti.id_tipo_invio
             JOIN risca_d_tipo_sede dts ON rep.id_tipo_sede = dts.id_tipo_sede
          WHERE rep.id_tipo_recapito = 1
        ), rec_alternativo AS (
         SELECT '-->'::text AS dati_recapito_alternativo,
            rrr.id_riscossione AS id_riscossione_a,
            rrr.id_recapito AS id_recapito_a,
            dtr.cod_tipo_recapito AS cod_tipo_recapito_a,
            dtr.des_tipo_recapito AS des_tipo_recapito_a,
            dti.cod_tipo_invio AS cod_tipo_invio_a,
            dti.des_tipo_invio AS des_tipo_invio_a,
            dts.cod_tipo_sede AS cod_tipo_sede_a,
            dts.des_tipo_sede AS des_tipo_sede_a
           FROM risca_r_riscossione_recapito rrr
             LEFT JOIN risca_r_recapito rep ON rrr.id_recapito = rep.id_recapito
             JOIN risca_d_tipo_recapito dtr ON rep.id_tipo_recapito = dtr.id_tipo_recapito
             JOIN risca_d_tipo_invio dti ON rep.id_tipo_invio = dti.id_tipo_invio
             JOIN risca_d_tipo_sede dts ON rep.id_tipo_sede = dts.id_tipo_sede
          WHERE rep.id_tipo_recapito = 2
        ), rec_sd_importo_versato AS (
         SELECT '-->'::text AS dati_sd_importo_versato,
            rat.id_stato_debitorio,
            sum(rdp_1.importo_versato) AS somma_importo_versato
           FROM risca_r_dettaglio_pag rdp_1
             LEFT JOIN risca_r_rata_sd rat ON rdp_1.id_rata_sd = rat.id_rata_sd
          GROUP BY rat.id_stato_debitorio
        ), rec_detpag_importo_versato AS (
         SELECT '-->'::text AS dati_pag_importo_versato,
            rde.id_rata_sd,
            sum(rde.importo_versato) AS importo_versato
           FROM risca_r_dettaglio_pag rde
          GROUP BY rde.id_rata_sd
        ), rec_importo_rimborsato AS (
         SELECT '-->'::text AS dati_importo_rimborsato,
            rim.id_stato_debitorio,
            sum(
                CASE
                    WHEN rim.id_tipo_rimborso = 1 THEN rim.imp_rimborso
                    WHEN rim.id_tipo_rimborso <> 1 THEN rim.imp_restituito
                    ELSE NULL::numeric
                END) AS importo_rimborsato
           FROM risca_r_rimborso rim
          GROUP BY rim.id_stato_debitorio
        )
 SELECT '-->'::text AS dati_stato_debitorio,
    sta.id_stato_debitorio,
    sta.desc_periodo_pagamento,
    sta.gest_attore_ins,
    sta.id_attivita_stato_deb,
    sta.id_soggetto AS id_soggetto_sd,
    sta.id_gruppo_soggetto AS id_gruppo_soggetto_sd,
        CASE
            WHEN sta.id_attivita_stato_deb = 2 THEN
            CASE
                WHEN rec_alternativo.id_recapito_a IS NULL THEN btrim(to_char(ris.id_soggetto, '9999999999'::text))
                ELSE (ris.id_soggetto || 'R'::text) || rec_alternativo.id_recapito_a
            END
            WHEN sta.id_attivita_stato_deb = 3 THEN btrim(to_char(ris.id_soggetto, '9999999999'::text))
            ELSE NULL::text
        END AS id_titolare,
    ssd.cf_soggetto AS cf_soggetto_sd,
    sta.num_titolo,
    sta.data_provvedimento,
    sta.des_tipo_titolo,
    sta.flg_dilazione,
    sta.nap,
    '-->'::text AS dati_ambito,
    amb.cod_ambito,
    amb.des_ambito,
    '-->'::text AS dati_riscossione,
    ris.id_riscossione,
    ris.id_tipo_riscossione,
    ris.id_stato_riscossione,
    ris.id_soggetto,
    ris.id_gruppo_soggetto,
    ris.id_tipo_autorizza,
    ris.cod_riscossione,
    ris.cod_riscossione_prov,
    ris.cod_riscossione_prog,
    ris.id_sigla_riscossione,
    ris.cod_riscossione_lettera_prov,
    ris.num_pratica,
    ris.flg_prenotata,
    ris.motivo_prenotazione,
    ris.note_riscossione,
    ris.data_ini_concessione,
    ris.data_scad_concessione,
    ris.data_ini_sosp_canone,
    ris.data_fine_sosp_canone,
    ris.json_dt,
    ris.id_componente_dt,
    '-->'::text AS dati_soggetto,
    sog.cf_soggetto,
    sog.id_tipo_soggetto,
    '-->'::text AS dati_avviso_titolare,
    dav.id_soggetto AS id_soggetto_avviso,
    dav.codice_fiscale_calc AS cf_soggetto_avviso,
    '-->'::text AS dati_indirizzo_principale,
    rec_principale.id_riscossione_p,
    rec_principale.id_recapito_p,
    rec_principale.cod_tipo_recapito_p,
    rec_principale.des_tipo_recapito_p,
    rec_principale.cod_tipo_invio_p,
        CASE
            WHEN rec_principale.cod_tipo_invio_p::text = 'C'::text THEN 'CARTA'::text
            WHEN rec_principale.cod_tipo_invio_p::text = 'P'::text THEN 'PEC'::text
            WHEN rec_principale.cod_tipo_invio_p::text = 'E'::text THEN 'EMAIL'::text
            ELSE upper(rec_principale.des_tipo_invio_p::text)
        END AS des_tipo_invio_p,
    rec_principale.cod_tipo_sede_p,
    rec_principale.des_tipo_sede_p,
    '-->'::text AS dati_indirizzo_alternativo,
    rec_alternativo.id_riscossione_a,
    rec_alternativo.id_recapito_a,
    rec_alternativo.cod_tipo_recapito_a,
    rec_alternativo.des_tipo_recapito_a,
    rec_alternativo.cod_tipo_invio_a,
        CASE
            WHEN rec_alternativo.cod_tipo_invio_a::text = 'C'::text THEN 'CARTA'::text
            WHEN rec_alternativo.cod_tipo_invio_a::text = 'P'::text THEN 'PEC'::text
            WHEN rec_alternativo.cod_tipo_invio_a::text = 'E'::text THEN 'EMAIL'::text
            ELSE upper(rec_alternativo.des_tipo_invio_a::text)
        END AS des_tipo_invio_a,
    rec_alternativo.cod_tipo_sede_a,
    rec_alternativo.des_tipo_sede_a,
    '-->'::text AS dati_sd_importo_versato,
    rec_sd_importo_versato.somma_importo_versato,
    '-->'::text AS dati_rata_sd,
    rsd.id_rata_sd,
    rsd.data_scadenza_pagamento,
    to_char(rsd.data_scadenza_pagamento::timestamp with time zone, 'yyyy'::text) AS annualita_di_pagamento,
    rsd.canone_dovuto,
    rsd.interessi_maturati AS rata_interessi_maturati,
    '-->'::text AS dati_detpag_importo_versato,
    rec_detpag_importo_versato.importo_versato,
    '-->'::text AS dati_rimborso,
    rec_importo_rimborsato.importo_rimborsato,
    '-->'::text AS dati_dettaglio_pagamento,
    rdp.importo_versato AS dett_pag_imp_vers,
    rdp.interessi_maturati,
    rdp.id_rata_sd AS rata_pag,
    '-->'::text AS dati_pagamenti,
    pag.id_pagamento,
    pag.data_op_val AS data_versamento_dett,
        CASE
            WHEN pag.data_op_val IS NULL THEN NULL::integer
            ELSE
            CASE
                WHEN (date_trunc('day'::text, pag.data_op_val::timestamp with time zone)::date - date_trunc('day'::text, rsd.data_scadenza_pagamento::timestamp with time zone)::date + abs(date_trunc('day'::text, pag.data_op_val::timestamp with time zone)::date - date_trunc('day'::text, rsd.data_scadenza_pagamento::timestamp with time zone)::date)) = 0 THEN 0
                ELSE date_trunc('day'::text, pag.data_op_val::timestamp with time zone)::date - date_trunc('day'::text, rsd.data_scadenza_pagamento::timestamp with time zone)::date
            END
        END AS giorni_ritardo
   FROM risca_t_stato_debitorio sta
     LEFT JOIN risca_t_riscossione ris ON sta.id_riscossione = ris.id_riscossione
     LEFT JOIN risca_t_soggetto sog ON ris.id_soggetto = sog.id_soggetto
     LEFT JOIN risca_t_soggetto ssd ON sta.id_soggetto = ssd.id_soggetto
     LEFT JOIN rec_principale ON ris.id_riscossione = rec_principale.id_riscossione_p
     LEFT JOIN rec_alternativo ON ris.id_riscossione = rec_alternativo.id_riscossione_a
     LEFT JOIN rec_sd_importo_versato ON sta.id_stato_debitorio = rec_sd_importo_versato.id_stato_debitorio
     LEFT JOIN risca_r_rata_sd rsd ON sta.id_stato_debitorio = rsd.id_stato_debitorio AND rsd.id_rata_sd_padre IS NULL
     LEFT JOIN rec_detpag_importo_versato ON rsd.id_rata_sd = rec_detpag_importo_versato.id_rata_sd
     LEFT JOIN rec_importo_rimborsato ON sta.id_stato_debitorio = rec_importo_rimborsato.id_stato_debitorio
     LEFT JOIN risca_r_dettaglio_pag rdp ON rsd.id_rata_sd = rdp.id_rata_sd
     LEFT JOIN risca_t_pagamento pag ON rdp.id_pagamento = pag.id_pagamento
     LEFT JOIN risca_d_tipo_riscossione tri ON ris.id_tipo_riscossione = tri.id_tipo_riscossione
     LEFT JOIN risca_d_ambito amb ON tri.id_ambito = amb.id_ambito
     LEFT JOIN risca_r_avviso_dati_titolare dav ON sta.nap::text = dav.nap::text
  WHERE sta.id_attivita_stato_deb = ANY (ARRAY[2, 3])
  ORDER BY sta.id_stato_debitorio DESC;

COMMENT ON VIEW v_stati_debitori_avvisi_e_solleciti IS 'La vista permette di identificare gli avvisi e i solleciti da elaborare durante l''elaborazione batch degli stati debitori';


-- v_utility_lista_pagamenti_sd source

CREATE OR REPLACE VIEW v_utility_lista_pagamenti_sd
AS SELECT '-->'::text AS dati_pagamento,
    pag.id_ambito,
    pag.id_pagamento,
    pag.id_tipologia_pag,
    pag.id_tipo_modalita_pag,
    pag.causale,
    pag.data_op_val,
    pag.flg_rimborsato,
    pag.imp_da_assegnare,
    pag.note,
    '-->'::text AS dati_dettaglio_pagamento,
    rdp.id_dettaglio_pag,
    rdp.importo_versato,
    rdp.interessi_maturati AS interessi_maturati_dettaglio,
    '-->'::text AS dati_rata_sd,
    rat.id_rata_sd,
    rat.id_stato_debitorio,
    rat.canone_dovuto,
    rat.interessi_maturati AS interessi_maturati_rata,
    rat.data_scadenza_pagamento,
    '-->'::text AS dati_stato_debitorio,
    sta.id_riscossione,
    sta.id_soggetto,
    sta.id_gruppo_soggetto,
    sta.id_attivita_stato_deb,
    sta.id_stato_contribuzione,
    sta.id_tipo_dilazione,
    sta.nap,
    sta.imp_spese_notifica,
    sta.imp_compensazione_canone
   FROM risca_t_pagamento pag
     LEFT JOIN risca_r_dettaglio_pag rdp ON pag.id_pagamento = rdp.id_pagamento
     LEFT JOIN ( SELECT ras.id_rata_sd,
            ras.id_stato_debitorio,
            ras.id_rata_sd_padre,
            ras.canone_dovuto,
            ras.interessi_maturati,
            ras.data_scadenza_pagamento,
            ras.gest_data_ins,
            ras.gest_attore_ins,
            ras.gest_data_upd,
            ras.gest_attore_upd,
            ras.gest_uid
           FROM risca_r_rata_sd ras
          WHERE ras.id_rata_sd_padre IS NULL) rat ON rdp.id_rata_sd = rat.id_rata_sd
     LEFT JOIN risca_t_stato_debitorio sta ON rat.id_stato_debitorio = sta.id_stato_debitorio
  ORDER BY pag.id_pagamento;


-- v_utility_lista_provvedimenti source

CREATE OR REPLACE VIEW v_utility_lista_provvedimenti
AS WITH dato_riscossione AS (
         SELECT risco.id_riscossione,
            risco.id_stato_riscossione,
            risco.id_soggetto,
            risco.id_gruppo_soggetto,
            risco.id_tipo_autorizza,
            risco.cod_riscossione,
            risco.cod_riscossione_prov,
            risco.cod_riscossione_prog,
            risco.id_sigla_riscossione,
            risco.cod_riscossione_lettera_prov,
            risco.num_pratica,
            risco.flg_prenotata,
            risco.motivo_prenotazione,
            risco.note_riscossione,
            risco.data_ini_concessione,
            risco.data_scad_concessione,
            risco.data_ini_sosp_canone,
            risco.data_fine_sosp_canone,
            risco.json_dt,
            risco.gest_data_ins,
            risco.gest_attore_ins,
            risco.gest_data_upd,
            risco.gest_attore_upd,
            risco.gest_uid,
            risco.id_componente_dt,
            risco.id_tipo_riscossione,
            tri.cod_tipo_riscossione,
            tri.des_tipo_riscossione,
            amb.id_ambito,
            amb.cod_ambito,
            amb.des_ambito
           FROM risca_t_riscossione risco
             LEFT JOIN risca_d_tipo_riscossione tri ON risco.id_tipo_riscossione = tri.id_tipo_riscossione
             LEFT JOIN risca_d_ambito amb ON tri.id_ambito = amb.id_ambito
        )
 SELECT '-->'::text AS dati_provvedimento,
    pro.id_provvedimento,
    pro.num_titolo,
    pro.data_provvedimento,
    pro.note,
    '-->'::text AS dati_tipo_titolo,
    pro.id_tipo_titolo,
    tti.cod_tipo_titolo,
    tti.des_tipo_titolo,
    tti.flg_default AS flg_default_tipo_titolo,
    '-->'::text AS dati_tipo_provvedimento,
    pro.id_tipo_provvedimento,
    tpr.cod_tipo_provvedimento,
    tpr.des_tipo_provvedimento,
    tpr.flg_istanza,
    tpr.flg_default AS flg_default_tipo_provvedimento,
    '-->'::text AS dati_riscossione,
    ris.id_riscossione,
    ris.num_pratica,
    ris.cod_riscossione,
    ris.json_dt,
    '-->'::text AS dati_ambito,
    ris.id_ambito,
    ris.cod_ambito,
    ris.des_ambito,
    '-->'::text AS dati_gest_provvedimento,
    pro.gest_data_ins,
    pro.gest_attore_ins,
    pro.gest_data_upd,
    pro.gest_attore_upd,
    pro.gest_uid
   FROM risca_r_provvedimento pro
     LEFT JOIN dato_riscossione ris ON pro.id_riscossione = ris.id_riscossione
     LEFT JOIN risca_d_tipo_titolo tti ON pro.id_tipo_titolo = tti.id_tipo_titolo
     LEFT JOIN risca_d_tipo_provvedimento tpr ON pro.id_tipo_provvedimento = tpr.id_tipo_provvedimento;


-- v_utility_lista_riscossioni source

CREATE OR REPLACE VIEW v_utility_lista_riscossioni
AS WITH riscossione_recapito_principale AS (
         SELECT '-->'::text AS dati_riscossione_recapito,
            rrr.id_riscossione,
            rrr.id_recapito,
            '-->'::text AS dati_recapito_principale,
            rec_pri.id_soggetto,
            rec_pri.cod_recapito,
            rec_pri.indirizzo,
            rec_pri.num_civico,
            rec_pri.denom_comune,
            rec_pri.cap_comune,
            rec_pri.sigla_provincia,
            rec_pri.cod_tipo_invio,
            rec_pri.des_tipo_invio,
            rec_pri.email,
            rec_pri.pec,
            rec_pri.presso,
            rec_pri.des_localita,
            rec_pri.cod_tipo_recapito,
            rec_pri.des_tipo_recapito,
            rec_pri.id_recapito_postel,
            rec_pri.id_gruppo_soggetto,
            rec_pri.destinatario_postel,
            rec_pri.presso_postel,
            rec_pri.indirizzo_postel,
            rec_pri.citta_postel,
            rec_pri.provincia_postel,
            rec_pri.cap_postel
           FROM risca_r_riscossione_recapito rrr
             JOIN ( SELECT rre.id_recapito,
                    rre.id_soggetto,
                    rre.id_tipo_recapito,
                    rre.cod_recapito,
                    rre.indirizzo,
                    rre.num_civico,
                    dco.denom_comune,
                    rre.cap_recapito AS cap_comune,
                    dpr.sigla_provincia,
                    dti.cod_tipo_invio,
                    dti.des_tipo_invio,
                    rre.email,
                    rre.pec,
                    rre.presso,
                    rre.des_localita,
                    dtr.cod_tipo_recapito,
                    dtr.des_tipo_recapito,
                    rrp.id_recapito_postel,
                    rrp.id_gruppo_soggetto,
                    rrp.destinatario_postel,
                    rrp.presso_postel,
                    rrp.indirizzo_postel,
                    rrp.citta_postel,
                    rrp.provincia_postel,
                    rrp.cap_postel
                   FROM risca_r_recapito rre
                     LEFT JOIN risca_d_tipo_recapito dtr ON rre.id_tipo_recapito = dtr.id_tipo_recapito
                     LEFT JOIN risca_d_tipo_invio dti ON rre.id_tipo_invio = dti.id_tipo_invio
                     LEFT JOIN risca_r_recapito_postel rrp ON rre.id_recapito = rrp.id_recapito
                     LEFT JOIN risca_d_comune dco ON rre.id_comune_recapito = dco.id_comune
                     LEFT JOIN risca_d_provincia dpr ON dco.id_provincia = dpr.id_provincia
                  WHERE rre.id_tipo_recapito = 1) rec_pri ON rrr.id_recapito = rec_pri.id_recapito
        ), riscossione_recapito_alternativo AS (
         SELECT '-->'::text AS dati_riscossione_recapito,
            rrr.id_riscossione,
            rrr.id_recapito,
            '-->'::text AS dati_recapito_principale,
            rec_pri.id_soggetto,
            rec_pri.cod_recapito,
            rec_pri.indirizzo,
            rec_pri.num_civico,
            rec_pri.denom_comune,
            rec_pri.cap_comune,
            rec_pri.sigla_provincia,
            rec_pri.cod_tipo_invio,
            rec_pri.des_tipo_invio,
            rec_pri.email,
            rec_pri.pec,
            rec_pri.presso,
            rec_pri.des_localita,
            rec_pri.cod_tipo_recapito,
            rec_pri.des_tipo_recapito,
            rec_pri.id_recapito_postel,
            rec_pri.id_gruppo_soggetto,
            rec_pri.destinatario_postel,
            rec_pri.presso_postel,
            rec_pri.indirizzo_postel,
            rec_pri.citta_postel,
            rec_pri.provincia_postel,
            rec_pri.cap_postel
           FROM risca_r_riscossione_recapito rrr
             JOIN ( SELECT rre.id_recapito,
                    rre.id_soggetto,
                    rre.id_tipo_recapito,
                    rre.cod_recapito,
                    rre.indirizzo,
                    rre.num_civico,
                    dco.denom_comune,
                    rre.cap_recapito AS cap_comune,
                    dpr.sigla_provincia,
                    dti.cod_tipo_invio,
                    dti.des_tipo_invio,
                    rre.email,
                    rre.pec,
                    rre.presso,
                    rre.des_localita,
                    dtr.cod_tipo_recapito,
                    dtr.des_tipo_recapito,
                    rrp.id_recapito_postel,
                    rrp.id_gruppo_soggetto,
                    rrp.destinatario_postel,
                    rrp.presso_postel,
                    rrp.indirizzo_postel,
                    rrp.citta_postel,
                    rrp.provincia_postel,
                    rrp.cap_postel
                   FROM risca_r_recapito rre
                     LEFT JOIN risca_d_tipo_recapito dtr ON rre.id_tipo_recapito = dtr.id_tipo_recapito
                     LEFT JOIN risca_d_tipo_invio dti ON rre.id_tipo_invio = dti.id_tipo_invio
                     LEFT JOIN risca_r_recapito_postel rrp ON rre.id_recapito = rrp.id_recapito
                     LEFT JOIN risca_d_comune dco ON rre.id_comune_recapito = dco.id_comune
                     LEFT JOIN risca_d_provincia dpr ON dco.id_provincia = dpr.id_provincia
                  WHERE rre.id_tipo_recapito = 2) rec_pri ON rrr.id_recapito = rec_pri.id_recapito
        )
 SELECT '-->'::text AS dati_riscossione,
    ris.id_riscossione,
    ris.id_tipo_riscossione,
    ris.id_stato_riscossione,
    ris.id_soggetto,
    ris.id_gruppo_soggetto,
    ris.id_tipo_autorizza,
    ris.cod_riscossione,
    ris.cod_riscossione_prov,
    ris.cod_riscossione_prog,
    ris.id_sigla_riscossione,
    ris.cod_riscossione_lettera_prov,
    ris.num_pratica,
    ris.flg_prenotata,
    ris.motivo_prenotazione,
    ris.note_riscossione,
    ris.data_ini_concessione,
    ris.data_scad_concessione,
    ris.data_ini_sosp_canone,
    ris.data_fine_sosp_canone,
    ris.json_dt,
    ris.gest_data_ins,
    ris.gest_attore_ins,
    ris.gest_data_upd,
    ris.gest_attore_upd,
    ris.gest_uid,
    ris.id_componente_dt,
    '-->'::text AS dati_tipo_riscossione,
    tri.cod_tipo_riscossione,
    tri.des_tipo_riscossione,
    tri.flg_default AS flg_default_riscossione,
    '-->'::text AS dati_stato_riscossione,
    sri.cod_stato_riscossione,
    sri.des_stato_riscossione,
    sri.flg_default AS flg_default_stato_risco,
    '-->'::text AS dati_tipo_autorizza,
    rdt.cod_tipo_autorizza,
    rdt.des_tipo_autorizza,
    rdt.flg_default AS flg_default_tipo_autorizza,
    '-->'::text AS dati_ambito,
    amb.id_ambito,
    amb.cod_ambito,
    amb.des_ambito,
    '-->'::text AS dati_componente_dt,
    dcd.id_tipo_componente_dt,
    dcd.nome_componente_dt,
    dcd.des_componente_dt,
    '-->'::text AS dati_soggetto,
    sog.cod_tipo_soggetto,
    sog.des_tipo_soggetto,
    sog.cod_tipo_natura_giuridica,
    sog.des_tipo_natura_giuridica,
    sog.cf_soggetto,
    sog.nome,
    sog.cognome,
    sog.den_soggetto,
    sog.partita_iva_soggetto,
    '-->'::text AS dati_gruppo_soggetto,
    gso.cod_gruppo_soggetto,
    gso.des_gruppo_soggetto,
    gso.cod_gruppo_fonte,
    '-->'::text AS dati_ris_recapito_principale,
    ris_pri.id_recapito AS id_recapito_principale,
    ris_pri.cod_recapito AS cod_recapito_principale,
    ris_pri.indirizzo AS indirizzo_principale,
    ris_pri.num_civico AS num_civico_principale,
    ris_pri.denom_comune AS denom_comune_principale,
    ris_pri.cap_comune AS cap_comune_principale,
    ris_pri.sigla_provincia AS sigla_provincia_principale,
    ris_pri.cod_tipo_invio AS cod_tipo_invio_principale,
    ris_pri.des_tipo_invio AS des_tipo_invio_principale,
    ris_pri.email AS email_principale,
    ris_pri.pec AS pec_principale,
    ris_pri.presso AS presso_principale,
    ris_pri.des_localita AS des_localita_principale,
    ris_pri.cod_tipo_recapito AS cod_tipo_recapito_principale,
    ris_pri.des_tipo_recapito AS des_tipo_recapito_principale,
    ris_pri.destinatario_postel AS destinatario_postel_principale,
    ris_pri.presso_postel AS presso_postel_principale,
    ris_pri.indirizzo_postel AS indirizzo_postel_principale,
    ris_pri.citta_postel AS citta_postel_principale,
    ris_pri.provincia_postel AS provincia_postel_principale,
    ris_pri.cap_postel AS cap_postel_principale,
    '-->'::text AS dati_ris_recapito_alternativo,
    ris_alt.id_recapito AS id_recapito_alternativo,
    ris_alt.cod_recapito AS cod_recapito_alternativo,
    ris_alt.indirizzo AS indirizzo_alternativo,
    ris_alt.num_civico AS num_civico_alternativo,
    ris_alt.denom_comune AS denom_comune_alternativo,
    ris_alt.cap_comune AS cap_comune_alternativo,
    ris_alt.sigla_provincia AS sigla_provincia_alternativo,
    ris_alt.cod_tipo_invio AS cod_tipo_invio_alternativo,
    ris_alt.des_tipo_invio AS des_tipo_invio_alternativo,
    ris_alt.email AS email_alternativo,
    ris_alt.pec AS pec_alternativo,
    ris_alt.presso AS presso_alternativo,
    ris_alt.des_localita AS des_localita_alternativo,
    ris_alt.cod_tipo_recapito AS cod_tipo_recapito_alternativo,
    ris_alt.des_tipo_recapito AS des_tipo_recapito_alternativo,
    ris_alt.destinatario_postel AS destinatario_postel_alternativo,
    ris_alt.presso_postel AS presso_postel_alternativo,
    ris_alt.indirizzo_postel AS indirizzo_postel_alternativo,
    ris_alt.citta_postel AS citta_postel_alternativo,
    ris_alt.provincia_postel AS provincia_postel_alternativo,
    ris_pri.cap_postel AS cap_postel_alternativo
   FROM risca_t_riscossione ris
     LEFT JOIN risca_d_tipo_riscossione tri ON ris.id_tipo_riscossione = tri.id_tipo_riscossione
     LEFT JOIN risca_d_stato_riscossione sri ON ris.id_stato_riscossione = sri.id_stato_riscossione
     LEFT JOIN risca_d_tipo_autorizza rdt ON ris.id_tipo_autorizza = rdt.id_tipo_autorizza
     LEFT JOIN risca_d_ambito amb ON tri.id_ambito = amb.id_ambito
     LEFT JOIN risca_d_componente_dt dcd ON ris.id_componente_dt = dcd.id_componente_dt
     LEFT JOIN ( SELECT sgg.id_soggetto,
            sgg.id_ambito,
            sgg.id_tipo_soggetto,
            sgg.cf_soggetto,
            sgg.id_tipo_natura_giuridica,
            sgg.id_fonte_origine,
            sgg.id_fonte,
            sgg.nome,
            sgg.cognome,
            sgg.den_soggetto,
            sgg.partita_iva_soggetto,
            sgg.data_nascita_soggetto,
            sgg.id_comune_nascita,
            sgg.id_stato_nascita,
            sgg.citta_estera_nascita,
            sgg.gest_attore_ins,
            sgg.gest_data_ins,
            sgg.gest_attore_upd,
            sgg.gest_data_upd,
            sgg.gest_uid,
            sgg.data_aggiornamento,
            sgg.data_cancellazione,
            dts.cod_tipo_soggetto,
            dts.des_tipo_soggetto,
            dtn.cod_tipo_natura_giuridica,
            dtn.des_tipo_natura_giuridica
           FROM risca_t_soggetto sgg
             LEFT JOIN risca_d_tipo_soggetto dts ON sgg.id_tipo_soggetto = dts.id_tipo_soggetto
             LEFT JOIN risca_d_tipo_natura_giuridica dtn ON sgg.id_tipo_natura_giuridica = dtn.id_tipo_natura_giuridica) sog ON ris.id_soggetto = sog.id_soggetto
     LEFT JOIN risca_t_gruppo_soggetto gso ON ris.id_gruppo_soggetto = gso.id_gruppo_soggetto
     LEFT JOIN riscossione_recapito_principale ris_pri ON ris.id_riscossione = ris_pri.id_riscossione AND COALESCE(ris.id_gruppo_soggetto, 0) = COALESCE(ris_pri.id_gruppo_soggetto, 0)
     LEFT JOIN riscossione_recapito_alternativo ris_alt ON ris.id_riscossione = ris_alt.id_riscossione AND COALESCE(ris.id_gruppo_soggetto, 0) = COALESCE(ris_alt.id_gruppo_soggetto, 0)
  ORDER BY ris.id_riscossione;


-- v_utility_lista_stati_debitori source

CREATE OR REPLACE VIEW v_utility_lista_stati_debitori
AS WITH lista_riscossioni AS (
         SELECT '-->'::text AS dati_riscossione,
            ris_1.id_riscossione,
            ris_1.id_tipo_riscossione,
            ris_1.id_stato_riscossione,
            ris_1.cod_riscossione,
            ris_1.num_pratica,
            '-->'::text AS dati_tipo_riscossione,
            tri.cod_tipo_riscossione,
            tri.des_tipo_riscossione,
            '-->'::text AS dati_stato_riscossione,
            sri.cod_stato_riscossione,
            sri.des_stato_riscossione,
            '-->'::text AS dati_ambito,
            amb.id_ambito,
            amb.cod_ambito,
            amb.des_ambito
           FROM risca_t_riscossione ris_1
             LEFT JOIN risca_d_tipo_riscossione tri ON ris_1.id_tipo_riscossione = tri.id_tipo_riscossione
             LEFT JOIN risca_d_stato_riscossione sri ON ris_1.id_stato_riscossione = sri.id_stato_riscossione
             LEFT JOIN risca_d_ambito amb ON tri.id_ambito = amb.id_ambito
        ), stato_debitorio_recapito AS (
         SELECT '-->'::text AS dati_stato_debitorio_recapito,
            sta_1.id_stato_debitorio,
            sta_1.id_riscossione,
            sta_1.id_recapito,
            '-->'::text AS dati_recapito_stato_debitorio,
            rre.id_soggetto,
            rre.cod_recapito,
            rre.indirizzo,
            rre.num_civico,
            dco.denom_comune,
            rre.cap_recapito AS cap_comune,
            dpr.sigla_provincia,
            dti.cod_tipo_invio,
            dti.des_tipo_invio,
            rre.email,
            rre.pec,
            rre.presso,
            rre.des_localita,
            dtr.cod_tipo_recapito,
            dtr.des_tipo_recapito,
            rrp.id_recapito_postel,
            rrp.id_gruppo_soggetto,
            rrp.destinatario_postel,
            rrp.presso_postel,
            rrp.indirizzo_postel,
            rrp.citta_postel,
            rrp.provincia_postel,
            rrp.cap_postel
           FROM risca_t_stato_debitorio sta_1
             LEFT JOIN risca_r_recapito rre ON sta_1.id_recapito = rre.id_recapito
             LEFT JOIN risca_d_tipo_recapito dtr ON rre.id_tipo_recapito = dtr.id_tipo_recapito
             LEFT JOIN risca_d_tipo_invio dti ON rre.id_tipo_invio = dti.id_tipo_invio
             LEFT JOIN risca_r_recapito_postel rrp ON rre.id_recapito = rrp.id_recapito
             LEFT JOIN risca_d_comune dco ON rre.id_comune_recapito = dco.id_comune
             LEFT JOIN risca_d_provincia dpr ON dco.id_provincia = dpr.id_provincia
        ), stato_debitorio_annualita AS (
         SELECT ras.id_stato_debitorio,
            max(ras.anno) AS annualita
           FROM risca_r_annualita_sd ras
          GROUP BY ras.id_stato_debitorio
        ), stato_debitorio_rata_padre AS (
         SELECT rsd.id_stato_debitorio,
            rsd.canone_dovuto,
            rsd.interessi_maturati,
            rsd.data_scadenza_pagamento,
            rdd.tot_importo_versato
           FROM risca_r_rata_sd rsd
             LEFT JOIN ( SELECT rdp.id_rata_sd,
                    sum(rdp.importo_versato) AS tot_importo_versato
                   FROM risca_r_dettaglio_pag rdp
                  GROUP BY rdp.id_rata_sd) rdd ON rsd.id_rata_sd = rdd.id_rata_sd
          WHERE rsd.id_rata_sd_padre IS NULL
        ), stato_debitorio_pagamenti AS (
         SELECT rsd.id_stato_debitorio,
            max(pag.data_op_val) AS max_data_pag
           FROM risca_r_rata_sd rsd
             LEFT JOIN risca_r_dettaglio_pag rdd ON rsd.id_rata_sd = rdd.id_rata_sd
             LEFT JOIN risca_t_pagamento pag ON rdd.id_pagamento = pag.id_pagamento
          WHERE rsd.id_rata_sd_padre IS NULL
          GROUP BY rsd.id_stato_debitorio
        ), stato_debitorio_rimborsi_compensa AS (
         SELECT rrr.id_stato_debitorio,
            sum(
                CASE rrr.id_tipo_rimborso
                    WHEN 1 THEN rrr.imp_rimborso
                    ELSE rrr.imp_restituito
                END) AS importo_rimb_comp
           FROM risca_r_rimborso rrr
          GROUP BY rrr.id_stato_debitorio
        ), stato_debitorio_attivita AS (
         SELECT sta_1.id_stato_debitorio,
            btrim(split_part(((((COALESCE(decode(dsd.des_attivita_stato_deb, NULL::character varying, acc.des_tipo_accertamento::text, dsd.des_attivita_stato_deb::text), ''::text) || decode(COALESCE(sta_1.imp_recupero_canone, 0::numeric), '0'::numeric, ''::text, '-Annual. prec.'::text)) || decode(COALESCE(sta_1.flg_addebito_anno_successivo, 0::numeric), '1'::numeric, '-Annual. succes.'::text, ''::text)) || decode(COALESCE(sta_1.imp_compensazione_canone, 0::numeric), '0'::numeric, ''::text, '-Con compens.'::text)) || decode(COALESCE(sta_1.flg_dilazione, 0::numeric), '1'::numeric, '-Dilaz.'::text, ''::text)) || COALESCE(tri.accoda_tipo_rimborso, ''::text), ','::text, 1), '-'::text) AS attivita
           FROM risca_t_stato_debitorio sta_1
             LEFT JOIN risca_d_attivita_stato_deb dsd ON sta_1.id_attivita_stato_deb = dsd.id_attivita_stato_deb
             LEFT JOIN ( SELECT DISTINCT tac.id_stato_debitorio,
                    dta.des_tipo_accertamento
                   FROM risca_t_accertamento tac
                     LEFT JOIN risca_d_tipo_accertamento dta ON tac.id_tipo_accertamento = dta.id_tipo_accertamento
                     JOIN ( SELECT x.id_stato_debitorio,
                            max(x.data_protocollo) AS max_data_protocollo
                           FROM risca_t_accertamento x
                          WHERE x.flg_annullato = 0::numeric AND x.flg_restituito = 0::numeric
                          GROUP BY x.id_stato_debitorio) uac ON tac.id_stato_debitorio = uac.id_stato_debitorio AND tac.data_protocollo = uac.max_data_protocollo) acc ON sta_1.id_stato_debitorio = acc.id_stato_debitorio
             LEFT JOIN ( SELECT sta_2.id_stato_debitorio,
                    (decode(COALESCE(rim.quanti_rimborsi, 0::bigint), '0'::bigint, ''::text, '-Rimborsato'::text) || decode(COALESCE(cop.quanti_compensati, 0::bigint), '0'::bigint, ''::text, '-Compensato'::text)) || decode(COALESCE(dco.quanti_da_compensare, 0::bigint), '0'::bigint, ''::text, '-Da compensare'::text) AS accoda_tipo_rimborso
                   FROM risca_t_stato_debitorio sta_2
                     LEFT JOIN ( SELECT rim_1.id_stato_debitorio,
                            count(*) AS quanti_rimborsi
                           FROM risca_r_rimborso rim_1
                          WHERE rim_1.id_tipo_rimborso = 1
                          GROUP BY rim_1.id_stato_debitorio) rim ON sta_2.id_stato_debitorio = rim.id_stato_debitorio
                     LEFT JOIN ( SELECT rim_1.id_stato_debitorio,
                            count(*) AS quanti_da_compensare
                           FROM risca_r_rimborso rim_1
                          WHERE rim_1.id_tipo_rimborso = 2
                          GROUP BY rim_1.id_stato_debitorio) dco ON sta_2.id_stato_debitorio = dco.id_stato_debitorio
                     LEFT JOIN ( SELECT rim_1.id_stato_debitorio,
                            count(*) AS quanti_compensati
                           FROM risca_r_rimborso rim_1
                          WHERE rim_1.id_tipo_rimborso = 3
                          GROUP BY rim_1.id_stato_debitorio) cop ON sta_2.id_stato_debitorio = cop.id_stato_debitorio) tri ON sta_1.id_stato_debitorio = tri.id_stato_debitorio
        )
 SELECT sta.id_stato_debitorio,
    sta.id_riscossione,
    sta.id_soggetto,
    sta.id_gruppo_soggetto,
    sta.id_recapito,
    sta.flg_annullato,
    sta.nap,
    '-->'::text AS dati_attivita_sd,
    das.cod_attivita_stato_deb,
    das.des_attivita_stato_deb,
    '-->'::text AS dati_stato_contribuzione,
    dsc.cod_stato_contribuzione,
    dsc.des_stato_contribuzione,
    '-->'::text AS dati_tipo_dilazione,
    dtd.data_inizio_val,
    dtd.data_fine_val,
    '-->'::text AS dati_riscossione,
    ris.id_tipo_riscossione,
    ris.id_stato_riscossione,
    ris.cod_riscossione,
    ris.num_pratica,
    '-->'::text AS dati_sd_recapito,
    sdr.id_recapito AS id_recapito_sd,
    sdr.cod_recapito AS cod_recapito_sd,
    sdr.indirizzo AS indirizzo_sd,
    sdr.num_civico AS num_civico_sd,
    sdr.denom_comune AS denom_comune_sd,
    sdr.cap_comune AS cap_comune_sd,
    sdr.sigla_provincia AS sigla_provincia_sd,
    sdr.cod_tipo_invio AS cod_tipo_invio_sd,
    sdr.des_tipo_invio AS des_tipo_invio_sd,
    sdr.email AS email_sd,
    sdr.pec AS pec_sd,
    sdr.presso AS presso_sd,
    sdr.des_localita AS des_localita_sd,
    sdr.cod_tipo_recapito AS cod_tipo_recapito_sd,
    sdr.des_tipo_recapito AS des_tipo_recapito_sd,
    sdr.destinatario_postel AS destinatario_postel_sd,
    sdr.presso_postel AS presso_postel_sd,
    sdr.indirizzo_postel AS indirizzo_postel_sd,
    sdr.citta_postel AS citta_postel_sd,
    sdr.provincia_postel AS provincia_postel_sd,
    sdr.cap_postel AS cap_postel_sd,
    '-->'::text AS dati_sd_contabili,
    sda.annualita,
    srp.canone_dovuto,
    srp.interessi_maturati,
    sta.imp_spese_notifica,
    srp.data_scadenza_pagamento,
    srp.tot_importo_versato,
    sdp.max_data_pag,
    src.importo_rimb_comp,
    upper(sat.attivita) AS attivita_sd,
    COALESCE(srp.tot_importo_versato, 0::numeric) - (decode(sta.flg_annullato, '1'::numeric, 0::numeric, COALESCE(srp.canone_dovuto, 0::numeric)) + decode(sta.flg_annullato, '1'::numeric, 0::numeric, COALESCE(srp.interessi_maturati, 0::numeric)) + decode(sta.flg_annullato, '1'::numeric, 0::numeric, COALESCE(sta.imp_spese_notifica, 0::numeric)) + COALESCE(src.importo_rimb_comp, 0::numeric)) AS imp_mancante_eccedente
   FROM risca_t_stato_debitorio sta
     LEFT JOIN risca_d_attivita_stato_deb das ON sta.id_attivita_stato_deb = das.id_attivita_stato_deb
     LEFT JOIN risca_d_stato_contribuzione dsc ON sta.id_stato_contribuzione = dsc.id_stato_contribuzione
     LEFT JOIN risca_d_tipo_dilazione dtd ON sta.id_tipo_dilazione = dtd.id_tipo_dilazione
     LEFT JOIN lista_riscossioni ris ON sta.id_riscossione = ris.id_riscossione
     LEFT JOIN stato_debitorio_recapito sdr ON sta.id_stato_debitorio = sdr.id_stato_debitorio AND sta.id_recapito = sdr.id_recapito AND COALESCE(sta.id_gruppo_soggetto, 0) = COALESCE(sdr.id_gruppo_soggetto, 0)
     LEFT JOIN stato_debitorio_annualita sda ON sta.id_stato_debitorio = sda.id_stato_debitorio
     LEFT JOIN stato_debitorio_rata_padre srp ON sta.id_stato_debitorio = srp.id_stato_debitorio
     LEFT JOIN stato_debitorio_pagamenti sdp ON sta.id_stato_debitorio = sdp.id_stato_debitorio
     LEFT JOIN stato_debitorio_rimborsi_compensa src ON sta.id_stato_debitorio = src.id_stato_debitorio
     LEFT JOIN stato_debitorio_attivita sat ON sta.id_stato_debitorio = sat.id_stato_debitorio
  ORDER BY sta.id_stato_debitorio;


-- v_utility_lista_usi_riscossione source

CREATE OR REPLACE VIEW v_utility_lista_usi_riscossione
AS WITH dato_riscossione AS (
         SELECT risco.id_riscossione,
            risco.id_stato_riscossione,
            risco.id_soggetto,
            risco.id_gruppo_soggetto,
            risco.id_tipo_autorizza,
            risco.cod_riscossione,
            risco.cod_riscossione_prov,
            risco.cod_riscossione_prog,
            risco.id_sigla_riscossione,
            risco.cod_riscossione_lettera_prov,
            risco.num_pratica,
            risco.flg_prenotata,
            risco.motivo_prenotazione,
            risco.note_riscossione,
            risco.data_ini_concessione,
            risco.data_scad_concessione,
            risco.data_ini_sosp_canone,
            risco.data_fine_sosp_canone,
            risco.json_dt,
            risco.gest_data_ins,
            risco.gest_attore_ins,
            risco.gest_data_upd,
            risco.gest_attore_upd,
            risco.gest_uid,
            risco.id_componente_dt,
            risco.id_tipo_riscossione,
            tri.cod_tipo_riscossione,
            tri.des_tipo_riscossione,
            amb.id_ambito,
            amb.cod_ambito,
            amb.des_ambito
           FROM risca_t_riscossione risco
             LEFT JOIN risca_d_tipo_riscossione tri ON risco.id_tipo_riscossione = tri.id_tipo_riscossione
             LEFT JOIN risca_d_ambito amb ON tri.id_ambito = amb.id_ambito
        )
 SELECT '-->'::text AS dati_riscossione_uso,
    rus.id_riscossione_uso,
    rus.id_tipo_uso,
    '-->'::text AS dati_riscossione,
    ris.id_riscossione,
    ris.num_pratica,
    ris.cod_riscossione,
    ris.json_dt,
    '-->'::text AS dati_ambito,
    ris.id_ambito,
    ris.cod_ambito,
    ris.des_ambito,
    '-->'::text AS dati_tipo_uso,
    dtu.cod_tipo_uso,
    dtu.des_tipo_uso,
    dtu.id_tipo_uso_padre,
    dtu.flg_uso_principale,
    dtu.ordina_tipo_uso,
    dtu.flg_default,
    dtu.data_inizio_validita,
    dtu.data_fine_validita,
    '-->'::text AS dati_accerta_bilancio,
    ccb.id_accerta_bilancio,
    ccb.cod_accerta_bilancio,
    ccb.des_accerta_bilancio,
    '-->'::text AS dati_unita_misura,
    dum.id_unita_misura,
    dum.sigla_unita_misura,
    dum.des_unita_misura,
    '-->'::text AS dati_gest_riscossione_uso,
    rus.gest_data_ins,
    rus.gest_attore_ins,
    rus.gest_data_upd,
    rus.gest_attore_upd,
    rus.gest_uid
   FROM risca_r_riscossione_uso rus
     LEFT JOIN dato_riscossione ris ON rus.id_riscossione = ris.id_riscossione
     LEFT JOIN risca_d_tipo_uso dtu ON rus.id_tipo_uso = dtu.id_tipo_uso
     LEFT JOIN risca_d_accerta_bilancio ccb ON dtu.id_accerta_bilancio = ccb.id_accerta_bilancio
     LEFT JOIN risca_d_unita_misura dum ON dtu.id_unita_misura = dum.id_unita_misura;
