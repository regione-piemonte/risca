/* *****************************************************
 * Copyright Regione Piemonte - 2025
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************/

-- csi_log_audit definition

-- Drop table

-- DROP TABLE csi_log_audit;

CREATE TABLE csi_log_audit (
	data_ora timestamp NOT NULL, -- Data e ora dell'evento
	id_app varchar(100) NOT NULL, -- Codice identificativo dell'applicazione utilizzata dall'utente; da comporre con i valori presenti in Anagrafica Prodotti: <codice prodotto>_<codice linea cliente>_<codice ambiente>_<codice Unit? di Installazione>,
	ip_address varchar(40) NULL, -- Ip del client utente (se possibile)
	utente varchar(100) NOT NULL, -- Identificativo univoco dell'utente che ha effettuato l'operazione (es. login / codice fiscale / matricola / ecc.)
	operazione varchar(50) NOT NULL, -- Questo campo dovrà contenere l'informazione circa l'operazione effettuata; utilizzare uno dei seguenti valori: login / logout / read / insert / update / delete / merge
	ogg_oper varchar(500) NOT NULL, -- Questo campo consentirà di identificare i dati e le informazioni trattati dall'operazione. Se la funzionalità lo permette inserire il nome delle tabelle (o in alternativa degli oggetti/entità) su cui viene eseguita l'operazione; l'indicazione della colonna è opzionale e andrà indicata nel formato tabella.colonna. Se l'applicativo prevede accessi a schemi dati esterni premettere il nome dello schema proprietario al nome della tabella.
	key_oper varchar(500) NULL -- Questo campo dovrà contenere l'identificativo univoco dell'oggetto dell'operazione oppure nel caso di aggiornamenti multipli del valore che caratterizza l'insieme di oggetti (es: modifica di un dato in tutta una categoria di riscossioni)
);
COMMENT ON TABLE csi_log_audit IS 'Il log di audit è una raccolta cronologica di informazioni finalizzata registrare le attività che sono state effettuate sui dati e che possono avere rilevanza dal punto di vista della sicurezza delle informazioni (ad esempio accessi, modifiche, cancellazione di dati personali).  I log di audit devono essere specificati nella fase di analisi  dei requisiti ai fini di decidere quali accessi tracciare, in relazione ai requisiti normativi e alle richieste del cliente, in quanto hanno un impatto non trascurabile sull''applicazione e sulle infrastrutture e, quindi, devono essere valutati e stimati.';

-- Column comments

COMMENT ON COLUMN csi_log_audit.data_ora IS 'Data e ora dell''evento';
COMMENT ON COLUMN csi_log_audit.id_app IS 'Codice identificativo dell''applicazione utilizzata dall''utente; da comporre con i valori presenti in Anagrafica Prodotti: <codice prodotto>_<codice linea cliente>_<codice ambiente>_<codice Unit? di Installazione>';
COMMENT ON COLUMN csi_log_audit.ip_address IS 'Ip del client utente (se possibile)';
COMMENT ON COLUMN csi_log_audit.utente IS 'Identificativo univoco dell''utente che ha effettuato l''operazione (es. login / codice fiscale / matricola / ecc.)';
COMMENT ON COLUMN csi_log_audit.operazione IS 'Questo campo dovrà contenere l''informazione circa l''operazione effettuata; utilizzare uno dei seguenti valori: login / logout / read / insert / update / delete / merge';
COMMENT ON COLUMN csi_log_audit.ogg_oper IS 'Questo campo consentirà di identificare i dati e le informazioni trattati dall''operazione. Se la funzionalità lo permette inserire il nome delle tabelle (o in alternativa degli oggetti/entità) su cui viene eseguita l''operazione; l''indicazione della colonna è opzionale e andrà indicata nel formato tabella.colonna. Se l''applicativo prevede accessi a schemi dati esterni premettere il nome dello schema proprietario al nome della tabella.';
COMMENT ON COLUMN csi_log_audit.key_oper IS 'Questo campo dovrà contenere l''identificativo univoco dell''oggetto dell''operazione oppure nel caso di aggiornamenti multipli del valore che caratterizza l''insieme di oggetti (es: modifica di un dato in tutta una categoria di riscossioni)';


-- risca_d_accerta_bilancio definition

-- Drop table

-- DROP TABLE risca_d_accerta_bilancio;

CREATE TABLE risca_d_accerta_bilancio (
	id_accerta_bilancio int4 NOT NULL, -- Identificativo univoco
	cod_accerta_bilancio varchar(20) NOT NULL,
	des_accerta_bilancio varchar(150) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	note_backoffice varchar(2000) NULL,
	CONSTRAINT pk_risca_d_accerta_bilancio PRIMARY KEY (id_accerta_bilancio)
);
CREATE UNIQUE INDEX ak_risca_d_accerta_bilancio_01 ON risca_d_accerta_bilancio USING btree (cod_accerta_bilancio);

-- Column comments

COMMENT ON COLUMN risca_d_accerta_bilancio.id_accerta_bilancio IS 'Identificativo univoco';


-- risca_d_algoritmo definition

-- Drop table

-- DROP TABLE risca_d_algoritmo;

CREATE TABLE risca_d_algoritmo (
	id_algoritmo int4 NOT NULL, -- Identificativo univoco
	script_algoritmo text NOT NULL,
	note_algoritmo varchar(200) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_algoritmo PRIMARY KEY (id_algoritmo)
);

-- Column comments

COMMENT ON COLUMN risca_d_algoritmo.id_algoritmo IS 'Identificativo univoco';


-- risca_d_ambito definition

-- Drop table

-- DROP TABLE risca_d_ambito;

CREATE TABLE risca_d_ambito (
	id_ambito int4 NOT NULL, -- Identificativo univoco
	cod_ambito varchar(20) NOT NULL,
	des_ambito varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_ambito PRIMARY KEY (id_ambito)
);
CREATE UNIQUE INDEX ak_risca_d_ambito_01 ON risca_d_ambito USING btree (cod_ambito);
COMMENT ON TABLE risca_d_ambito IS 'Definisce gli ambiti amministrativi PA gestiti dall''applicativo.';

-- Column comments

COMMENT ON COLUMN risca_d_ambito.id_ambito IS 'Identificativo univoco';


-- risca_d_configurazione definition

-- Drop table

-- DROP TABLE risca_d_configurazione;

CREATE TABLE risca_d_configurazione (
	chiave varchar(50) NOT NULL, -- chiave identificativa del parametro di configurazione
	valore varchar(1000) NOT NULL, -- valore configurato
	note varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_configurazione PRIMARY KEY (chiave)
);
COMMENT ON TABLE risca_d_configurazione IS 'Contiene i parametri di configurazione necessari al funzionamento e alla personalizzazione dei componenti applicativi.  Ogni parametro è definito nel formato : chiave-valore';

-- Column comments

COMMENT ON COLUMN risca_d_configurazione.chiave IS 'chiave identificativa del parametro di configurazione';
COMMENT ON COLUMN risca_d_configurazione.valore IS 'valore configurato';


-- risca_d_continente definition

-- Drop table

-- DROP TABLE risca_d_continente;

CREATE TABLE risca_d_continente (
	id_continente numeric(2) NOT NULL,
	denom_continente varchar(30) NOT NULL,
	CONSTRAINT ak_risca_d_continente_01 UNIQUE (denom_continente),
	CONSTRAINT pk_risca_d_continente PRIMARY KEY (id_continente)
);


-- risca_d_fase_elabora definition

-- Drop table

-- DROP TABLE risca_d_fase_elabora;

CREATE TABLE risca_d_fase_elabora (
	id_fase_elabora int4 NOT NULL, -- Identificativo univoco
	cod_fase_elabora varchar(30) NOT NULL,
	des_fase_elabora varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_fase_elabora PRIMARY KEY (id_fase_elabora)
);
CREATE UNIQUE INDEX ak_risca_d_fase_elabora_01 ON risca_d_fase_elabora USING btree (cod_fase_elabora);
COMMENT ON TABLE risca_d_fase_elabora IS 'Permette di definire le fasi previste dall''elaborazione specifica';

-- Column comments

COMMENT ON COLUMN risca_d_fase_elabora.id_fase_elabora IS 'Identificativo univoco';


-- risca_d_fonte definition

-- Drop table

-- DROP TABLE risca_d_fonte;

CREATE TABLE risca_d_fonte (
	id_fonte int4 NOT NULL, -- Identificativo univoco
	cod_fonte varchar(20) NOT NULL,
	des_fonte varchar(100) NULL,
	chiave_sottoscrizione varchar(100) NULL, -- E' la chiave di sottoscrizione definita in API-Manager (sottoscrizione nel contratto)¶Es. scriva : scriva_scrivabesrv_rp-01 
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_fonte PRIMARY KEY (id_fonte)
);
CREATE UNIQUE INDEX ak_risca_d_fonte_01 ON risca_d_fonte USING btree (cod_fonte);
COMMENT ON TABLE risca_d_fonte IS 'Definisce i Sistemi esterni (Fonti), con cui l''applicativo interagisce.';

-- Column comments

COMMENT ON COLUMN risca_d_fonte.id_fonte IS 'Identificativo univoco';
COMMENT ON COLUMN risca_d_fonte.chiave_sottoscrizione IS 'E'' la chiave di sottoscrizione definita in API-Manager (sottoscrizione nel contratto)
Es. scriva : scriva_scrivabesrv_rp-01 ';


-- risca_d_funzionalita definition

-- Drop table

-- DROP TABLE risca_d_funzionalita;

CREATE TABLE risca_d_funzionalita (
	id_funzionalita int4 NOT NULL, -- Identificativo univoco
	cod_funzionalita varchar(3) NOT NULL,
	des_funzionalita varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT ak_risca_d_funzionalita_01 UNIQUE (cod_funzionalita),
	CONSTRAINT pk_risca_d_funzionalita PRIMARY KEY (id_funzionalita)
);
COMMENT ON TABLE risca_d_funzionalita IS 'Permette di definire le funzionalità/contesto applicativo. Es. Bollettazione Stampe Morosità Utenze Simulazione Canone ...... etc .....';

-- Column comments

COMMENT ON COLUMN risca_d_funzionalita.id_funzionalita IS 'Identificativo univoco';


-- risca_d_mappa_fonte_esterna definition

-- Drop table

-- DROP TABLE risca_d_mappa_fonte_esterna;

CREATE TABLE risca_d_mappa_fonte_esterna (
	id_mappa_fonte_esterna int4 NOT NULL, -- Identificativo univoco
	cod_fonte_esterna varchar(20) NOT NULL,
	info_fonte varchar(50) NOT NULL,
	cod_fonte varchar(50) NOT NULL,
	cod_risca varchar(50) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_mappa_fonte_esterna PRIMARY KEY (id_mappa_fonte_esterna)
);
CREATE UNIQUE INDEX ak_risca_d_mappa_fonte_esterna_01 ON risca_d_mappa_fonte_esterna USING btree (cod_fonte_esterna, info_fonte, cod_fonte);

-- Column comments

COMMENT ON COLUMN risca_d_mappa_fonte_esterna.id_mappa_fonte_esterna IS 'Identificativo univoco';


-- risca_d_origine_limiti definition

-- Drop table

-- DROP TABLE risca_d_origine_limiti;

CREATE TABLE risca_d_origine_limiti (
	id_origine numeric(2) NOT NULL,
	cod_origine varchar(10) NOT NULL,
	desc_origine varchar(100) NOT NULL,
	CONSTRAINT pk_risca_d_origine_limiti PRIMARY KEY (id_origine)
);
CREATE UNIQUE INDEX ak_risca_d_origine_limiti_01 ON risca_d_origine_limiti USING btree (cod_origine);
CREATE UNIQUE INDEX ak_risca_d_origine_limiti_02 ON risca_d_origine_limiti USING btree (desc_origine);


-- risca_d_profilo_pa definition

-- Drop table

-- DROP TABLE risca_d_profilo_pa;

CREATE TABLE risca_d_profilo_pa (
	id_profilo_pa int4 NOT NULL, -- Identificativo univoco
	cod_profilo_pa varchar(20) NOT NULL,
	des_profilo_pa varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_profilo_pa PRIMARY KEY (id_profilo_pa)
);
CREATE UNIQUE INDEX ak_risca_d_profilo_pa_01 ON risca_d_profilo_pa USING btree (cod_profilo_pa);
COMMENT ON TABLE risca_d_profilo_pa IS 'Permette di definire i profili applicativi. es. ''Amministratore, Gestore con privilegi di base, etc..''  Nota L''applicativo ricava il profilo di appartenenza dell''utente connesso, dall''informazione passata dal sistema di autenticazione Iride (identità digitale)';

-- Column comments

COMMENT ON COLUMN risca_d_profilo_pa.id_profilo_pa IS 'Identificativo univoco';


-- risca_d_riduzione_aumento definition

-- Drop table

-- DROP TABLE risca_d_riduzione_aumento;

CREATE TABLE risca_d_riduzione_aumento (
	id_riduzione_aumento int4 NOT NULL, -- Identificativo univoco
	sigla_riduzione_aumento varchar(3) NULL,
	des_riduzione_aumento varchar(100) NULL,
	perc_riduzione_aumento numeric(7, 4) NULL,
	flg_riduzione_aumento numeric(1) NOT NULL, -- 1=Riduzione e 2=Aumento
	flg_manuale numeric(1) NOT NULL,
	flg_da_applicare numeric(1) NOT NULL,
	ordina_riduzione_aumento int4 NULL,
	cod_riduzione_aumento varchar(20) NOT NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_riduzione_aumento_01 CHECK ((flg_riduzione_aumento = ANY (ARRAY[(1)::numeric, (2)::numeric]))),
	CONSTRAINT chk_risca_d_riduzione_aumento_02 CHECK ((flg_manuale = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_d_riduzione_aumento_03 CHECK ((flg_da_applicare = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_riduzione_aumento PRIMARY KEY (id_riduzione_aumento)
);
CREATE UNIQUE INDEX ak_risca_d_riduzione_aumento_01 ON risca_d_riduzione_aumento USING btree (sigla_riduzione_aumento);
CREATE UNIQUE INDEX ak_risca_d_riduzione_aumento_02 ON risca_d_riduzione_aumento USING btree (cod_riduzione_aumento);
COMMENT ON TABLE risca_d_riduzione_aumento IS 'Definisce l epossibili percentuali di riduzione e aumento applicabili all''uso Es. (1=Riduzione e 2=Aumento)  il flg_manuale identifica se la riduzione/aumento deve essere applicata dal sistema in modo automatico o dall''utente in base a regole stabilite Es. (0-Automatico e 1-Manuale)';

-- Column comments

COMMENT ON COLUMN risca_d_riduzione_aumento.id_riduzione_aumento IS 'Identificativo univoco';
COMMENT ON COLUMN risca_d_riduzione_aumento.flg_riduzione_aumento IS '1=Riduzione e 2=Aumento';


-- risca_d_stato_contribuzione definition

-- Drop table

-- DROP TABLE risca_d_stato_contribuzione;

CREATE TABLE risca_d_stato_contribuzione (
	id_stato_contribuzione int4 NOT NULL, -- Identificativo univoco
	cod_stato_contribuzione varchar(2) NOT NULL,
	des_stato_contribuzione varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT ak_risca_d_stato_contribuzione_01 UNIQUE (cod_stato_contribuzione),
	CONSTRAINT pk_risca_d_stato_contribuzione PRIMARY KEY (id_stato_contribuzione)
);
COMMENT ON TABLE risca_d_stato_contribuzione IS 'Permette di definire gli stati di contribuzione. es. REGOLARE INSUFFICIENTE REGOLARIZZATO ECCEDENTE INESIGIBILE';

-- Column comments

COMMENT ON COLUMN risca_d_stato_contribuzione.id_stato_contribuzione IS 'Identificativo univoco';


-- risca_d_stato_elabora definition

-- Drop table

-- DROP TABLE risca_d_stato_elabora;

CREATE TABLE risca_d_stato_elabora (
	id_stato_elabora int4 NOT NULL, -- Identificativo univoco
	cod_stato_elabora varchar(10) NOT NULL,
	des_stato_elabora varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT ak_risca_d_stato_elabora_01 UNIQUE (cod_stato_elabora),
	CONSTRAINT pk_risca_d_stato_elabora PRIMARY KEY (id_stato_elabora)
);
COMMENT ON TABLE risca_d_stato_elabora IS 'Permette di definire gli stati di elaborazione : - EMISSIONE_RICHIESTA - EMESSA - EMESSA_KO - CONFERMA_RICHIESTA - -- ev. verifica PDF --- - TERMINATA - TERMINATA_KO - ANNULLATA';

-- Column comments

COMMENT ON COLUMN risca_d_stato_elabora.id_stato_elabora IS 'Identificativo univoco';


-- risca_d_stato_iuv definition

-- Drop table

-- DROP TABLE risca_d_stato_iuv;

CREATE TABLE risca_d_stato_iuv (
	id_stato_iuv int4 NOT NULL, -- Identificativo univoco
	cod_stato_iuv varchar(3) NOT NULL,
	des_stato_iuv varchar(50) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_stato_iuv PRIMARY KEY (id_stato_iuv)
);
CREATE INDEX ak_risca_d_stato_iuv_01 ON risca_d_stato_iuv USING btree (cod_stato_iuv);
COMMENT ON TABLE risca_d_stato_iuv IS 'Permette di definire lo stato dello iuv ES.  ATTIVO ANNULLATO UTILIZZATO EMISSIONE A RUOLO SCADUTO DA CONFERMARE';

-- Column comments

COMMENT ON COLUMN risca_d_stato_iuv.id_stato_iuv IS 'Identificativo univoco';


-- risca_d_tipo_accertamento definition

-- Drop table

-- DROP TABLE risca_d_tipo_accertamento;

CREATE TABLE risca_d_tipo_accertamento (
	id_tipo_accertamento int4 NOT NULL, -- Identificativo univoco
	cod_tipo_accertamento varchar(3) NOT NULL,
	des_tipo_accertamento varchar(50) NULL,
	flg_automatico numeric(1) DEFAULT 0 NOT NULL,
	flg_manuale numeric(1) DEFAULT 0 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_tipo_accertamento_01 CHECK ((flg_automatico = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_d_tipo_accertamento_02 CHECK ((flg_manuale = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_tipo_accertamento PRIMARY KEY (id_tipo_accertamento)
);
CREATE INDEX ak_risca_d_tipo_accertamento_01 ON risca_d_tipo_accertamento USING btree (cod_tipo_accertamento);
COMMENT ON TABLE risca_d_tipo_accertamento IS 'Permette di definire la tipologia di accertamento
ES. 
1 - Avviso bonario
2 - Sollecito di pagamento
3 - Riscossione coattiva
4 - Avviso bonario manuale
5 - Sollecito di pagamento manuale
6 - Rateizzazione pagamento
7 - Fallimento';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_accertamento.id_tipo_accertamento IS 'Identificativo univoco';


-- risca_d_tipo_area_competenza definition

-- Drop table

-- DROP TABLE risca_d_tipo_area_competenza;

CREATE TABLE risca_d_tipo_area_competenza (
	id_tipo_area_competenza int4 NOT NULL, -- Identificativo univoco
	cod_tipo_area_competenza varchar(10) NOT NULL,
	des_tipo_area_competenza varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_area_competenza PRIMARY KEY (id_tipo_area_competenza)
);
CREATE INDEX ak_risca_d_tipo_area_competenza_01 ON risca_d_tipo_area_competenza USING btree (cod_tipo_area_competenza);
COMMENT ON TABLE risca_d_tipo_area_competenza IS 'Definisce le aree di competenza Territoriale gestite nell''applicativo per filtrare i dati visibili all''utente in base al profilo Iride
Es. Provincia, Regione. Ente Parco';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_area_competenza.id_tipo_area_competenza IS 'Identificativo univoco';


-- risca_d_tipo_attivita_stato_deb definition

-- Drop table

-- DROP TABLE risca_d_tipo_attivita_stato_deb;

CREATE TABLE risca_d_tipo_attivita_stato_deb (
	id_tipo_attivita_stato_deb int4 NOT NULL,
	cod_tipo_attivita_stato_deb varchar(2) NOT NULL,
	des_tipo_attivita_stato_deb varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT ak_risca_d_tipo_attivita_stato_deb_01 UNIQUE (cod_tipo_attivita_stato_deb),
	CONSTRAINT pk_risca_d_tipo_attivita_stato_deb PRIMARY KEY (id_tipo_attivita_stato_deb)
);
COMMENT ON TABLE risca_d_tipo_attivita_stato_deb IS 'Tipologie previste di attività stato debitorio :  R-Rimborsi S-Sollecito M-Misto';


-- risca_d_tipo_componente_dt definition

-- Drop table

-- DROP TABLE risca_d_tipo_componente_dt;

CREATE TABLE risca_d_tipo_componente_dt (
	id_tipo_componente_dt int4 NOT NULL, -- Identificativo univoco
	cod_tipo_componente_dt bpchar(10) NOT NULL,
	des_tipo_componente_dt bpchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_componente_dt PRIMARY KEY (id_tipo_componente_dt)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_componente_dt_01 ON risca_d_tipo_componente_dt USING btree (cod_tipo_componente_dt);

-- Column comments

COMMENT ON COLUMN risca_d_tipo_componente_dt.id_tipo_componente_dt IS 'Identificativo univoco';


-- risca_d_tipo_dato_colonna definition

-- Drop table

-- DROP TABLE risca_d_tipo_dato_colonna;

CREATE TABLE risca_d_tipo_dato_colonna (
	id_tipo_dato_colonna int4 NOT NULL,
	desc_tipo_dato varchar(20) NULL,
	formato_dato varchar(20) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_dato_colonna PRIMARY KEY (id_tipo_dato_colonna)
);


-- risca_d_tipo_dato_tecnico definition

-- Drop table

-- DROP TABLE risca_d_tipo_dato_tecnico;

CREATE TABLE risca_d_tipo_dato_tecnico (
	id_tipo_dato_tecnico int4 NOT NULL, -- Identificativo univoco
	cod_tipo_dato_tecnico varchar(5) NOT NULL,
	des_tipo_dato_tecnico varchar(50) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_dato_tecnico PRIMARY KEY (id_tipo_dato_tecnico)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_dato_tecnico_01 ON risca_d_tipo_dato_tecnico USING btree (cod_tipo_dato_tecnico);

-- Column comments

COMMENT ON COLUMN risca_d_tipo_dato_tecnico.id_tipo_dato_tecnico IS 'Identificativo univoco';


-- risca_d_tipo_imp_non_propri definition

-- Drop table

-- DROP TABLE risca_d_tipo_imp_non_propri;

CREATE TABLE risca_d_tipo_imp_non_propri (
	id_tipo_imp_non_propri int4 NOT NULL, -- Identificativo univoco
	cod_tipo_imp_non_propri varchar(2) NOT NULL,
	des_tipo_imp_non_propri varchar(50) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_imp_non_propri PRIMARY KEY (id_tipo_imp_non_propri)
);
CREATE INDEX ak_risca_d_tipo_imp_non_propri_01 ON risca_d_tipo_imp_non_propri USING btree (cod_tipo_imp_non_propri);

-- Column comments

COMMENT ON COLUMN risca_d_tipo_imp_non_propri.id_tipo_imp_non_propri IS 'Identificativo univoco';


-- risca_d_tipo_invio definition

-- Drop table

-- DROP TABLE risca_d_tipo_invio;

CREATE TABLE risca_d_tipo_invio (
	id_tipo_invio int4 NOT NULL, -- Identificativo univoco
	cod_tipo_invio varchar(2) NOT NULL,
	des_tipo_invio varchar(50) NULL,
	ordina_tipo_invio int4 NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_invio PRIMARY KEY (id_tipo_invio)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_invio_01 ON risca_d_tipo_invio USING btree (cod_tipo_invio);
COMMENT ON TABLE risca_d_tipo_invio IS 'Permette di definire nei recapiti, il tipo di invio richiesto. es. ''pec, email, carta...''';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_invio.id_tipo_invio IS 'Identificativo univoco';


-- risca_d_tipo_messaggio definition

-- Drop table

-- DROP TABLE risca_d_tipo_messaggio;

CREATE TABLE risca_d_tipo_messaggio (
	id_tipo_messaggio int4 NOT NULL, -- Identificativo univoco
	cod_tipo_messaggio bpchar(1) NOT NULL,
	des_tipo_messaggio varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_messaggio PRIMARY KEY (id_tipo_messaggio)
);
COMMENT ON TABLE risca_d_tipo_messaggio IS 'Tipologie di messaggi gestite.  Es. P - Messaggi feedback positivo                E - Messaggi di errore                        A - Messaggio di avviso legato ad un’azione   I - Messaggi Informativi                      F - Messaggi di controllo formale             C - Messaggi di errore calcolo canone         U - Messaggio relativo ad abilitazione utente';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_messaggio.id_tipo_messaggio IS 'Identificativo univoco';


-- risca_d_tipo_modalita_pag definition

-- Drop table

-- DROP TABLE risca_d_tipo_modalita_pag;

CREATE TABLE risca_d_tipo_modalita_pag (
	id_tipo_modalita_pag int4 NOT NULL, -- Identificativo univoco
	cod_tipo_modalita_pag varchar(3) NOT NULL,
	des_tipo_modalita_pag varchar(50) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_modalita_pag PRIMARY KEY (id_tipo_modalita_pag)
);
CREATE INDEX ak_risca_d_tipo_modalita_pag_01 ON risca_d_tipo_modalita_pag USING btree (cod_tipo_modalita_pag);
COMMENT ON TABLE risca_d_tipo_modalita_pag IS 'Permette di definire la tipologia di pagamento non proprio
ES. 
1- Non identificato
2- Non di competenza';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_modalita_pag.id_tipo_modalita_pag IS 'Identificativo univoco';


-- risca_d_tipo_natura_giuridica definition

-- Drop table

-- DROP TABLE risca_d_tipo_natura_giuridica;

CREATE TABLE risca_d_tipo_natura_giuridica (
	id_tipo_natura_giuridica int4 NOT NULL, -- Identificativo univoco
	cod_tipo_natura_giuridica varchar(20) NOT NULL,
	des_tipo_natura_giuridica varchar(50) NULL,
	sigla_tipo_natura_giuridica varchar(20) NULL,
	ordina__natura_giuridica int4 NULL,
	flg_pubblico numeric(1) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_tipo_natura_giu_01 CHECK ((flg_pubblico = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_tipo_natura_giuridica PRIMARY KEY (id_tipo_natura_giuridica)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_natura_giuridica_01 ON risca_d_tipo_natura_giuridica USING btree (cod_tipo_natura_giuridica);
COMMENT ON TABLE risca_d_tipo_natura_giuridica IS 'Permette di definire nei soggetti, la tipologia di natura giuridica. La natura giuridica è identificata da un codice uivoco.';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_natura_giuridica.id_tipo_natura_giuridica IS 'Identificativo univoco';


-- risca_d_tipo_oggetto_app definition

-- Drop table

-- DROP TABLE risca_d_tipo_oggetto_app;

CREATE TABLE risca_d_tipo_oggetto_app (
	id_tipo_oggetto_app int4 NOT NULL, -- Identificativo univoco
	cod_tipo_oggetto_app varchar(20) NOT NULL,
	des_tipo_oggetto_app varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_oggetto_app PRIMARY KEY (id_tipo_oggetto_app)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_oggetto_app_01 ON risca_d_tipo_oggetto_app USING btree (cod_tipo_oggetto_app);
COMMENT ON TABLE risca_d_tipo_oggetto_app IS 'Permette di tipizzare gli oggetti applicativi da profilare. es. ''Funzione, Pulsante, etc...''';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_oggetto_app.id_tipo_oggetto_app IS 'Identificativo univoco';


-- risca_d_tipo_passo_elabora definition

-- Drop table

-- DROP TABLE risca_d_tipo_passo_elabora;

CREATE TABLE risca_d_tipo_passo_elabora (
	id_tipo_passo_elabora int4 NOT NULL, -- Identificativo univoco
	cod_tipo_passo_elabora varchar(20) NOT NULL,
	des_tipo_passo_elabora varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_passo_elabora PRIMARY KEY (id_tipo_passo_elabora)
);
CREATE INDEX ak_risca_d_tipo_passo_elabora_01 ON risca_d_tipo_passo_elabora USING btree (cod_tipo_passo_elabora);
COMMENT ON TABLE risca_d_tipo_passo_elabora IS 'Permette di definire le tipologie di elaborazione previste Es. Batch - RPGEBO10 (procedura di emissione)                    RPGERI010 (richiesta degli IUV) - SERVIZIO PRENOTAZIONE IUV --> integrazione con PagoPA                    RPGEBO030 (produce i file TXT per Stampe Massive)               ---- verifica pdf ? -------                    RPGEBO020 (invia bollettini)';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_passo_elabora.id_tipo_passo_elabora IS 'Identificativo univoco';


-- risca_d_tipo_personale definition

-- Drop table

-- DROP TABLE risca_d_tipo_personale;

CREATE TABLE risca_d_tipo_personale (
	id_tipo_personale int4 NOT NULL, -- Identificativo univoco
	cod_tipo_personale varchar(20) NOT NULL,
	des_tipo_personale varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_personale PRIMARY KEY (id_tipo_personale)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_personale_01 ON risca_d_tipo_personale USING btree (cod_tipo_personale);

-- Column comments

COMMENT ON COLUMN risca_d_tipo_personale.id_tipo_personale IS 'Identificativo univoco';


-- risca_d_tipo_recapito definition

-- Drop table

-- DROP TABLE risca_d_tipo_recapito;

CREATE TABLE risca_d_tipo_recapito (
	id_tipo_recapito int4 NOT NULL, -- Identificativo univoco
	cod_tipo_recapito varchar(2) NOT NULL,
	des_tipo_recapito varchar(50) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_recapito PRIMARY KEY (id_tipo_recapito)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_recapito_01 ON risca_d_tipo_recapito USING btree (cod_tipo_recapito);
COMMENT ON TABLE risca_d_tipo_recapito IS 'Permette di definire nei recapiti, la tipologia. es. ''principale, alternativo...''';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_recapito.id_tipo_recapito IS 'Identificativo univoco';


-- risca_d_tipo_ricerca_morosita definition

-- Drop table

-- DROP TABLE risca_d_tipo_ricerca_morosita;

CREATE TABLE risca_d_tipo_ricerca_morosita (
	id_tipo_ricerca_morosita int4 NOT NULL, -- Identificativo univoco
	cod_tipo_ricerca_morosita varchar(8) NOT NULL,
	des_tipo_ricerca_morosita varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_ricerca_morosita PRIMARY KEY (id_tipo_ricerca_morosita)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_ricerca_morosita_01 ON risca_d_tipo_ricerca_morosita USING btree (cod_tipo_ricerca_morosita);
COMMENT ON TABLE risca_d_tipo_ricerca_morosita IS 'Permette di definire le tipologie di ricerca previste per le morosità.
Per ogni voce l''applicativo applica delle logiche specifiche.';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_ricerca_morosita.id_tipo_ricerca_morosita IS 'Identificativo univoco';


-- risca_d_tipo_ricerca_pagamento definition

-- Drop table

-- DROP TABLE risca_d_tipo_ricerca_pagamento;

CREATE TABLE risca_d_tipo_ricerca_pagamento (
	id_tipo_ricerca_pagamento int4 NOT NULL, -- Identificativo univoco
	cod_tipo_ricerca_pagamento varchar(3) NOT NULL,
	des_tipo_ricerca_pagamento varchar(50) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_ricerca_pagamento PRIMARY KEY (id_tipo_ricerca_pagamento)
);
CREATE INDEX ak_risca_d_tipo_ricerca_pagamento_01 ON risca_d_tipo_ricerca_pagamento USING btree (cod_tipo_ricerca_pagamento);
COMMENT ON TABLE risca_d_tipo_ricerca_pagamento IS 'Utilizzata per la ricerca.
Ad ogni valore corrisponde un criterio di ricerca';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_ricerca_pagamento.id_tipo_ricerca_pagamento IS 'Identificativo univoco';


-- risca_d_tipo_ricerca_rimborso definition

-- Drop table

-- DROP TABLE risca_d_tipo_ricerca_rimborso;

CREATE TABLE risca_d_tipo_ricerca_rimborso (
	id_tipo_ricerca_rimborso int4 NOT NULL, -- Identificativo univoco
	cod_tipo_ricerca_rimborso varchar(8) NOT NULL,
	des_tipo_ricerca_rimborso varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_ricerca_rimborso PRIMARY KEY (id_tipo_ricerca_rimborso)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_ricerca_rimborso_01 ON risca_d_tipo_ricerca_rimborso USING btree (cod_tipo_ricerca_rimborso);
COMMENT ON TABLE risca_d_tipo_ricerca_rimborso IS 'Permette di definire le tipologie di ricerca previste i rimborsi.
Per ogni voce l''applicativo applica delle logiche specifiche.';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_ricerca_rimborso.id_tipo_ricerca_rimborso IS 'Identificativo univoco';


-- risca_d_tipo_rimborso definition

-- Drop table

-- DROP TABLE risca_d_tipo_rimborso;

CREATE TABLE risca_d_tipo_rimborso (
	id_tipo_rimborso int4 NOT NULL, -- Identificativo univoco
	cod_tipo_rimborso varchar(20) NOT NULL,
	des_tipo_rimborso varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT ak_risca_d_tipo_rimborso_01 UNIQUE (cod_tipo_rimborso),
	CONSTRAINT pk_risca_d_tipo_rimborso PRIMARY KEY (id_tipo_rimborso)
);
COMMENT ON TABLE risca_d_tipo_rimborso IS 'Permette di definire gli stati di contribuzione. es. REGOLARE INSUFFICIENTE REGOLARIZZATO ECCEDENTE INESIGIBILE';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_rimborso.id_tipo_rimborso IS 'Identificativo univoco';


-- risca_d_tipo_sede definition

-- Drop table

-- DROP TABLE risca_d_tipo_sede;

CREATE TABLE risca_d_tipo_sede (
	id_tipo_sede int4 NOT NULL, -- Identificativo univoco
	cod_tipo_sede bpchar(2) NOT NULL,
	des_tipo_sede bpchar(50) NOT NULL,
	ordina_tipo_sede int4 NULL,
	ind_default bpchar(20) NULL, -- potrà contenere PF PG oppure PB per idicare un default coerente al tipo soggetto (sede legale oppure residenza)
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_sede PRIMARY KEY (id_tipo_sede)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_sede_01 ON risca_d_tipo_sede USING btree (cod_tipo_sede);
COMMENT ON TABLE risca_d_tipo_sede IS 'Permette di specializzare il tipo recapito indicando per ciascun recapito se si tratta di sede legale, sede operativa, sede amministrativa';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_sede.id_tipo_sede IS 'Identificativo univoco';
COMMENT ON COLUMN risca_d_tipo_sede.ind_default IS 'potrà contenere PF PG oppure PB per idicare un default coerente al tipo soggetto (sede legale oppure residenza)';


-- risca_d_tipo_soggetto definition

-- Drop table

-- DROP TABLE risca_d_tipo_soggetto;

CREATE TABLE risca_d_tipo_soggetto (
	id_tipo_soggetto int4 NOT NULL, -- Identificativo univoco
	cod_tipo_soggetto varchar(2) NOT NULL,
	des_tipo_soggetto varchar(50) NULL,
	ordina_tipo_soggetto int4 NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_soggetto PRIMARY KEY (id_tipo_soggetto)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_soggetto_01 ON risca_d_tipo_soggetto USING btree (cod_tipo_soggetto);
COMMENT ON TABLE risca_d_tipo_soggetto IS 'Permette di definire nei soggetti, la tipologia. es. ''persona fisica, persona giuridica......''';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_soggetto.id_tipo_soggetto IS 'Identificativo univoco';


-- risca_d_tipo_spedizione definition

-- Drop table

-- DROP TABLE risca_d_tipo_spedizione;

CREATE TABLE risca_d_tipo_spedizione (
	id_tipo_spedizione int4 NOT NULL, -- Identificativo univoco
	cod_tipo_spedizione varchar(10) NOT NULL,
	des_tipo_spedizione varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_spedizione PRIMARY KEY (id_tipo_spedizione)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_spedizione_01 ON risca_d_tipo_spedizione USING btree (cod_tipo_spedizione);
COMMENT ON TABLE risca_d_tipo_spedizione IS 'Permette di definire le tipologie di spedizione previste.  ES.   1 - ORDINARIA 2 - SPECIALE';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_spedizione.id_tipo_spedizione IS 'Identificativo univoco';


-- risca_d_tipologia_pag definition

-- Drop table

-- DROP TABLE risca_d_tipologia_pag;

CREATE TABLE risca_d_tipologia_pag (
	id_tipologia_pag int4 NOT NULL, -- Identificativo univoco
	cod_tipologia_pag varchar(2) NOT NULL,
	des_tipologia_pag varchar(50) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipologia_pag PRIMARY KEY (id_tipologia_pag)
);
CREATE INDEX ak_risca_d_tipologia_pag_01 ON risca_d_tipologia_pag USING btree (cod_tipologia_pag);
COMMENT ON TABLE risca_d_tipologia_pag IS 'Permette di definire le tipologie di pagamento
ES.
M - Manuale
A  - Automatico (Poste/PagoPa)';

-- Column comments

COMMENT ON COLUMN risca_d_tipologia_pag.id_tipologia_pag IS 'Identificativo univoco';


-- risca_d_unita_misura definition

-- Drop table

-- DROP TABLE risca_d_unita_misura;

CREATE TABLE risca_d_unita_misura (
	id_unita_misura int4 NOT NULL, -- Identificativo univoco
	sigla_unita_misura varchar(20) NOT NULL,
	des_unita_misura varchar(150) NULL,
	ordina_unita_misura int4 NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_unita_misura PRIMARY KEY (id_unita_misura)
);

-- Column comments

COMMENT ON COLUMN risca_d_unita_misura.id_unita_misura IS 'Identificativo univoco';


-- risca_s_tracciamento definition

-- Drop table

-- DROP TABLE risca_s_tracciamento;

CREATE TABLE risca_s_tracciamento (
	id_tracciamento int4 NOT NULL, -- Identificativo univoco
	data_ora timestamp NOT NULL,
	flg_operazione bpchar(1) NOT NULL, -- Insert, Update, Delete
	id_riscossione int4 NULL, -- E' l'eventuale id del record 'padre' della tabella riscossione
	json_tracciamento jsonb NULL,
	tipo_json varchar(100) NULL,
	CONSTRAINT pk_risca_s_tracciamento PRIMARY KEY (id_tracciamento)
);
COMMENT ON TABLE risca_s_tracciamento IS '2 livello (interna al prodotto, solo di servizio) Permette di tracciare le attivià effettuate. Viene utilizzata nel caso ce ne fosse necessità, per poter ricostruire attività svolte nel tempo.  Il campo flg_operazione indica il tipo di operazione effettuata (Inserimento,Update, Delete) Per avere il riferimento alla eventuale riscossione ''padre'', occorre valorizzare il campo ''id_riscossione''';

-- Column comments

COMMENT ON COLUMN risca_s_tracciamento.id_tracciamento IS 'Identificativo univoco';
COMMENT ON COLUMN risca_s_tracciamento.flg_operazione IS 'Insert, Update, Delete';
COMMENT ON COLUMN risca_s_tracciamento.id_riscossione IS 'E'' l''eventuale id del record ''padre'' della tabella riscossione';


-- risca_t_delegato definition

-- Drop table

-- DROP TABLE risca_t_delegato;

CREATE TABLE risca_t_delegato (
	id_delegato int4 NOT NULL, -- Identificativo univoco
	cf_delegato varchar(16) NOT NULL,
	nominativo varchar(250) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_t_delegato PRIMARY KEY (id_delegato)
);
CREATE UNIQUE INDEX ak_risca_t_delegato_01 ON risca_t_delegato USING btree (cf_delegato);
COMMENT ON TABLE risca_t_delegato IS 'Anagrafica - Permette di censire gli utenti applicativi, delegati alla gestione della Riscossione (FO)';

-- Column comments

COMMENT ON COLUMN risca_t_delegato.id_delegato IS 'Identificativo univoco';


-- risca_t_file_450 definition

-- Drop table

-- DROP TABLE risca_t_file_450;

CREATE TABLE risca_t_file_450 (
	id_file_450 int4 NOT NULL, -- identificativo univoco
	nome_file varchar(100) NULL,
	data_creazione date NULL,
	data_conferma date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_t_file_450 PRIMARY KEY (id_file_450)
);

-- Column comments

COMMENT ON COLUMN risca_t_file_450.id_file_450 IS 'identificativo univoco';


-- risca_t_file_poste definition

-- Drop table

-- DROP TABLE risca_t_file_poste;

CREATE TABLE risca_t_file_poste (
	id_file_poste int4 NOT NULL, -- identificativo univoco
	tipo_file_poste varchar(20) NULL,
	nome varchar(100) NULL,
	data_file date NULL,
	data_scarico date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_t_file_poste PRIMARY KEY (id_file_poste)
);

-- Column comments

COMMENT ON COLUMN risca_t_file_poste.id_file_poste IS 'identificativo univoco';


-- risca_t_file_soris definition

-- Drop table

-- DROP TABLE risca_t_file_soris;

CREATE TABLE risca_t_file_soris (
	id_file_soris int4 NOT NULL, -- identificativo univoco
	nome_file_soris varchar(250) NULL,
	data_file date NULL,
	data_scarico date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_t_file_soris PRIMARY KEY (id_file_soris)
);

-- Column comments

COMMENT ON COLUMN risca_t_file_soris.id_file_soris IS 'identificativo univoco';


-- risca_t_gruppo_soggetto definition

-- Drop table

-- DROP TABLE risca_t_gruppo_soggetto;

CREATE TABLE risca_t_gruppo_soggetto (
	id_gruppo_soggetto int4 NOT NULL, -- Identificativo univoco
	cod_gruppo_soggetto varchar(20) NOT NULL,
	cod_gruppo_fonte varchar(20) NULL,
	des_gruppo_soggetto varchar(100) NULL,
	id_fonte int4 NULL,
	id_fonte_origine int4 NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_uid varchar(64) NULL,
	data_aggiornamento timestamp NULL,
	data_cancellazione timestamp NULL,
	CONSTRAINT pk_risca_t_gruppo_soggetto PRIMARY KEY (id_gruppo_soggetto)
);
CREATE UNIQUE INDEX ak_risca_t_gruppo_soggetto_01 ON risca_t_gruppo_soggetto USING btree (cod_gruppo_soggetto);
COMMENT ON TABLE risca_t_gruppo_soggetto IS 'Anagrafica dei Gruppi di Soggetti. Il gruppo è identificato da un codice uivoco.  La partecipazione al gruppo da parte di un soggetto e gestita nella relativa relazione soggetto-gruppo.';

-- Column comments

COMMENT ON COLUMN risca_t_gruppo_soggetto.id_gruppo_soggetto IS 'Identificativo univoco';


-- risca_t_immagine definition

-- Drop table

-- DROP TABLE risca_t_immagine;

CREATE TABLE risca_t_immagine (
	id_immagine int4 NOT NULL, -- identificativo univoco
	immagine varchar(100) NULL,
	flg_validita numeric(1) DEFAULT 0 NOT NULL, -- FLG_VALIDITA (Gerica) -  identifica la validità dell'immagine (1)
	path_immagine varchar(300) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_t_immagine_01 CHECK ((flg_validita = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_t_immagine PRIMARY KEY (id_immagine)
);

-- Column comments

COMMENT ON COLUMN risca_t_immagine.id_immagine IS 'identificativo univoco';
COMMENT ON COLUMN risca_t_immagine.flg_validita IS 'FLG_VALIDITA (Gerica) -  identifica la validità dell''immagine (1)';


-- risca_t_terna_prezzi_energ definition

-- Drop table

-- DROP TABLE risca_t_terna_prezzi_energ;

CREATE TABLE risca_t_terna_prezzi_energ (
	"data" date NOT NULL,
	ora varchar(2) NOT NULL,
	prezzo_nord_orig numeric(10, 2) NULL,
	prezzo_nord numeric(10, 2) NULL,
	note_backoffice varchar(2000) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_t_terna_prezzi_energ PRIMARY KEY (data, ora)
);
COMMENT ON TABLE risca_t_terna_prezzi_energ IS 'contiene i dati del prezzo orario dell''energia per una determinata data e ora fornite da terna';


-- risca_t_terna_utenze definition

-- Drop table

-- DROP TABLE risca_t_terna_utenze;

CREATE TABLE risca_t_terna_utenze (
	cod_censimp varchar(20) NOT NULL, -- codice censimento imprese
	cod_utenza varchar(20) NOT NULL, -- codice riscossione risca
	potenza_nomin_media_kwh numeric(10, 2) NULL,
	titolare varchar(200) NULL,
	note_backoffice varchar(2000) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_t_terna_utenze PRIMARY KEY (cod_censimp, cod_utenza)
);
COMMENT ON TABLE risca_t_terna_utenze IS 'Raccoglie la lista degli IUV che sono stati oggetto di modifica e che necessitano di elaborazione per comunicazione aggiornamenti a PagoPa';

-- Column comments

COMMENT ON COLUMN risca_t_terna_utenze.cod_censimp IS 'codice censimento imprese';
COMMENT ON COLUMN risca_t_terna_utenze.cod_utenza IS 'codice riscossione risca';


-- risca_w_lock_riscossione definition

-- Drop table

-- DROP TABLE risca_w_lock_riscossione;

CREATE TABLE risca_w_lock_riscossione (
	id_riscossione int4 NOT NULL, -- identificativo della riscossione bloccata
	utente_lock varchar(100) NULL, -- eventuale nominativo utente che ha preso in gestione la riscossione
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	cf_utente_lock varchar(16) NULL, -- eventuale cf utente che ha preso in gestione la riscossione
	CONSTRAINT pk_risca_w_lock_riscossione PRIMARY KEY (id_riscossione)
);
COMMENT ON TABLE risca_w_lock_riscossione IS 'Permette di gestire la concorrenzialità di gestione delle riscossioni. Il record viene scritto ogni volta che un utente prende in gestione la riscossione, e cancellato al termine dell''operazione.  --- applicativo ---  1) riscossione bloccata la vuole utilizzare lo stesso utente (cf_utente) che l''ha bloccata, può farlo ! altrimenti messaggio di errore : bloccata da <cf_utente>, (<utente_lock>))  2) riscossione non bloccata operazione consentita  --- servizi --- segue le regole applicative, tranne che per quelli BATCH e PORTING dove non effettua controlli  --- batch --- non considerano mai questi lock';

-- Column comments

COMMENT ON COLUMN risca_w_lock_riscossione.id_riscossione IS 'identificativo della riscossione bloccata';
COMMENT ON COLUMN risca_w_lock_riscossione.utente_lock IS 'eventuale nominativo utente che ha preso in gestione la riscossione';
COMMENT ON COLUMN risca_w_lock_riscossione.cf_utente_lock IS 'eventuale cf utente che ha preso in gestione la riscossione';


-- risca_w_pagopa_scomp_rich_iuv definition

-- Drop table

-- DROP TABLE risca_w_pagopa_scomp_rich_iuv;

CREATE TABLE risca_w_pagopa_scomp_rich_iuv (
	id_pagopa_scomp_rich_iuv int4 NOT NULL,
	nap varchar(20) NOT NULL,
	id_bil_acc int4 NULL,
	importo_per_nap numeric(13, 2) NULL,
	cod_bil_acc varchar(20) NULL,
	anno varchar(4) NULL,
	dati_spec_risc varchar(140) NULL,
	importo_per_acc_orig numeric(11, 2) NULL,
	importo_per_acc numeric(11, 2) NULL,
	id_tipo_bil_acc int4 NULL,
	sum_imp_per_acc_orig numeric(11, 2) NULL,
	perc_tot_imp numeric(11, 7) NULL,
	note_backoffice varchar(2000) NULL,
	CONSTRAINT pk_risca_w_pagopa_scomp_rich_iuv PRIMARY KEY (id_pagopa_scomp_rich_iuv)
);
CREATE INDEX ie_risca_w_pagopa_scomp_rich_iuv_01 ON risca_w_pagopa_scomp_rich_iuv USING btree (nap);


-- risca_w_pagopa_scomp_var_iuv definition

-- Drop table

-- DROP TABLE risca_w_pagopa_scomp_var_iuv;

CREATE TABLE risca_w_pagopa_scomp_var_iuv (
	id_pagopa_scomp_var_iuv int4 NOT NULL,
	nap varchar(20) NOT NULL,
	id_bil_acc int4 NULL,
	importo_per_nap numeric(13, 2) NULL,
	cod_bil_acc varchar(20) NULL,
	anno varchar(4) NULL,
	dati_spec_risc varchar(140) NULL,
	importo_per_acc_orig numeric(11, 2) NULL,
	importo_per_acc numeric(11, 2) NULL,
	id_tipo_bil_acc int4 NULL,
	sum_imp_per_acc_orig numeric(11, 2) NULL,
	perc_tot_imp numeric(11, 7) NULL,
	note_backoffice varchar(2000) NULL,
	CONSTRAINT pk_risca_w_pagopa_scomp_var_iuv PRIMARY KEY (id_pagopa_scomp_var_iuv)
);
CREATE INDEX ie_risca_w_pagopa_scomp_var_iuv_01 ON risca_w_pagopa_scomp_var_iuv USING btree (nap);


-- risca_w_soris_00c definition

-- Drop table

-- DROP TABLE risca_w_soris_00c;

CREATE TABLE risca_w_soris_00c (
	tipo_record varchar(3) NULL,
	filler1 varchar(2) NULL,
	cod_dest_mitt varchar(3) NULL,
	filler2 varchar(4) NULL,
	data_creazione_flusso date NULL,
	cod_tracciato varchar(8) NULL,
	filler3 varchar(371) NULL
);


-- risca_w_soris_99c definition

-- Drop table

-- DROP TABLE risca_w_soris_99c;

CREATE TABLE risca_w_soris_99c (
	tipo_record varchar(3) NULL,
	filler1 varchar(2) NULL,
	cod_dest_mitt varchar(3) NULL,
	filler2 varchar(4) NULL,
	totale_record_invio varchar(8) NULL,
	cod_tracciato varchar(8) NULL,
	filler3 varchar(379) NULL
);


-- risca_w_soris_fr0 definition

-- Drop table

-- DROP TABLE risca_w_soris_fr0;

CREATE TABLE risca_w_soris_fr0 (
	tipo_record varchar(3) NULL,
	progr_record numeric(7) NULL,
	cod_ambito varchar(3) NULL,
	mittente varchar(5) NULL,
	data_riferimento date NULL,
	identif_file varchar(20) NULL,
	tipo_file varchar(1) NULL,
	identif_file_orig varchar(20) NULL,
	data_creazione_file date NULL,
	"release" varchar(3) NULL,
	filler3 varchar(322) NULL
);


-- risca_w_soris_fr1 definition

-- Drop table

-- DROP TABLE risca_w_soris_fr1;

CREATE TABLE risca_w_soris_fr1 (
	tipo_record varchar(3) NULL,
	progr_record numeric(7) NULL,
	cod_ambito varchar(3) NULL,
	cod_ente_creditore varchar(5) NULL,
	anno_ruolo varchar(4) NULL,
	numero_ruolo varchar(6) NULL,
	p_tipo_ufficio varchar(1) NULL,
	p_codice_ufficio varchar(6) NULL,
	p_anno_riferimento varchar(4) NULL,
	p_tipo_modello varchar(3) NULL,
	p_ident_prenot_ruolo varchar(34) NULL,
	p_ident_atto varchar(48) NULL,
	progr_articolo_ruolo numeric(3) NULL,
	identif_cartella varchar(20) NULL,
	progr_articolo_cartella numeric(3) NULL,
	codice_entrata varchar(4) NULL,
	tipo_entrata varchar(1) NULL,
	codice_fiscale varchar(16) NULL,
	tipo_evento varchar(1) NULL,
	data_evento date NULL,
	importo_carico numeric(15, 2) NULL,
	codice_divisa varchar(3) NULL,
	data_scad_reg date NULL,
	iden_prec_avv_bon varchar(17) NULL,
	esito_notifica varchar(1) NULL,
	cod_ambito_delegato varchar(3) NULL,
	ident_proc_esecutiva varchar(20) NULL,
	tipo_spesa_proc_esec varchar(2) NULL,
	tab_deposito varchar(32) NULL,
	tab_spese varchar(16) NULL,
	importo_conf_proc_esec numeric(15) NULL,
	spese_proc_esec numeric(15) NULL,
	spese_proc_esec_p_lista numeric(15) NULL,
	t_spese_proc_esec numeric(15) NULL,
	t_spese_proc_esec_p_lista numeric(15) NULL,
	importo_tot_proc_esec numeric(15) NULL,
	attivazione_proc_esec varchar(1) NULL,
	integrazione_proc_esec varchar(1) NULL,
	info_evento_notifica varchar(1) NULL,
	filler varchar(10) NULL
);


-- risca_w_soris_fr2 definition

-- Drop table

-- DROP TABLE risca_w_soris_fr2;

CREATE TABLE risca_w_soris_fr2 (
	tipo_record varchar(3) NULL,
	progr_record numeric(7) NULL,
	cod_ambito varchar(3) NULL,
	cod_ente_creditore varchar(5) NULL,
	anno_ruolo varchar(4) NULL,
	numero_ruolo varchar(6) NULL,
	p_tipo_ufficio varchar(1) NULL,
	p_codice_ufficio varchar(6) NULL,
	p_anno_riferimento varchar(4) NULL,
	p_tipo_modello varchar(3) NULL,
	p_ident_prenot_ruolo varchar(34) NULL,
	p_ident_atto varchar(48) NULL,
	progr_articolo_ruolo numeric(3) NULL,
	identif_provv varchar(28) NULL,
	identif_provv_revocato varchar(28) NULL,
	codice_entrata varchar(4) NULL,
	tipo_entrata varchar(1) NULL,
	codice_fiscale varchar(16) NULL,
	tipo_evento varchar(1) NULL,
	data_evento date NULL,
	importo_carico numeric(15, 2) NULL,
	importo_provv numeric(15) NULL,
	importo_discaricato numeric(15) NULL,
	importo_rimb_darimb_norimb numeric(15) NULL,
	importo_inter numeric(15) NULL,
	importo_inter_mora numeric(15) NULL,
	spese_proc_esec numeric(15) NULL,
	spese_proc_esec_p_lista numeric(15) NULL,
	imp_rata1_aggio numeric(15) NULL,
	imp_rata_succ_aggio numeric(15) NULL,
	imp_inps numeric(15) NULL,
	codice_divisa varchar(3) NULL,
	num_rate varchar(2) NULL,
	data_registr date NULL,
	tipo_comunic_inesig varchar(1) NULL,
	tipo_sospensione varchar(1) NULL,
	flag_rimborso varchar(1) NULL,
	flag_ev_sismici varchar(1) NULL,
	num_rate_ev_sismici varchar(3) NULL,
	filler varchar(2) NULL
);


-- risca_w_soris_fr3 definition

-- Drop table

-- DROP TABLE risca_w_soris_fr3;

CREATE TABLE risca_w_soris_fr3 (
	tipo_record varchar(3) NULL,
	progr_record numeric(7) NULL,
	cod_ambito varchar(3) NULL,
	cod_ente_creditore varchar(5) NULL,
	anno_ruolo varchar(4) NULL,
	numero_ruolo varchar(6) NULL,
	p_tipo_ufficio varchar(1) NULL,
	p_codice_ufficio varchar(6) NULL,
	p_anno_riferimento varchar(4) NULL,
	p_tipo_modello varchar(3) NULL,
	p_ident_prenot_ruolo varchar(34) NULL,
	p_ident_atto varchar(48) NULL,
	progr_articolo_ruolo numeric(3) NULL,
	identif_cartella varchar(20) NULL,
	progr_articolo_cartella numeric(3) NULL,
	codice_entrata varchar(4) NULL,
	tipo_entrata varchar(1) NULL,
	codice_fiscale varchar(16) NULL,
	data_evento date NULL,
	importo_carico_risco numeric(15, 2) NULL,
	importo_interessi numeric(15) NULL,
	importo_aggio_ente numeric(15) NULL,
	importo_aggio_contrib numeric(15) NULL,
	t_spese_proc_esec numeric(15) NULL,
	t_spese_proc_esec_p_lista numeric(15) NULL,
	codice_divisa varchar(3) NULL,
	modalita_pagam varchar(1) NULL,
	filler1 varchar(2) NULL,
	data_registr date NULL,
	num_operaz_contabile varchar(25) NULL,
	progr_inter_op_contab varchar(3) NULL,
	filler2 varchar(3) NULL,
	data_decorrenza date NULL,
	numero_riversam varchar(3) NULL,
	restituzione_rimb_spese varchar(1) NULL,
	importo_carico_orig numeric(15) NULL,
	identif_provv_magg_rate varchar(28) NULL,
	importo_carico_resid_cond numeric(15) NULL,
	filler3 varchar(16) NULL
);


-- risca_w_soris_fr3_pag_da_inserire definition

-- Drop table

-- DROP TABLE risca_w_soris_fr3_pag_da_inserire;

CREATE TABLE risca_w_soris_fr3_pag_da_inserire (
	id_pagamento numeric NULL,
	identif_cartella varchar(20) NULL,
	data_evento date NULL,
	tot_riscosso numeric NULL
);


-- risca_w_soris_fr3_r_pag_sd definition

-- Drop table

-- DROP TABLE risca_w_soris_fr3_r_pag_sd;

CREATE TABLE risca_w_soris_fr3_r_pag_sd (
	id_pagamento numeric NULL,
	utenza varchar(7) NULL,
	p_anno_riferimento varchar(4) NULL,
	identif_cartella varchar(20) NULL,
	p_ident_prenot_ruolo varchar(34) NULL,
	data_evento date NULL,
	tot_riscosso numeric NULL,
	id_stato_deb numeric(6) NOT NULL,
	id_rata_stato_deb numeric(10) NOT NULL
);


-- risca_w_soris_fr7 definition

-- Drop table

-- DROP TABLE risca_w_soris_fr7;

CREATE TABLE risca_w_soris_fr7 (
	tipo_record varchar(3) NULL,
	progr_record numeric(7) NULL,
	cod_ambito varchar(3) NULL,
	cod_ente_creditore varchar(5) NULL,
	chiave_inf_annullare varchar(153) NULL,
	chiave_inf_correttiva varchar(153) NULL,
	tipo_evento varchar(1) NULL,
	motivo_rich_annul varchar(1) NULL,
	ente_richiedente varchar(5) NULL,
	data_richiesta date NULL,
	filler varchar(61) NULL
);


-- risca_w_stato_debitorio definition

-- Drop table

-- DROP TABLE risca_w_stato_debitorio;

CREATE TABLE risca_w_stato_debitorio (
	id_stato_debitorio int4 NOT NULL,
	id_riscossione int4 NOT NULL,
	id_soggetto int4 NOT NULL,
	id_gruppo_soggetto int4 NULL,
	id_recapito int4 NOT NULL,
	id_attivita_stato_deb int4 NULL,
	id_stato_contribuzione int4 NULL,
	id_tipo_dilazione int4 NULL,
	nap varchar(20) NULL,
	num_titolo varchar(20) NULL,
	data_provvedimento date NULL,
	num_richiesta_protocollo varchar(15) NULL,
	data_richiesta_protocollo date NULL,
	data_ultima_modifica date NULL,
	des_usi varchar(250) NULL,
	note varchar(500) NULL,
	imp_recupero_canone numeric(9, 2) NULL,
	imp_recupero_interessi numeric(9, 2) NULL,
	imp_spese_notifica numeric(9, 2) NULL,
	imp_compensazione_canone numeric(9, 2) NULL,
	desc_periodo_pagamento varchar(30) NULL,
	desc_motivo_annullo varchar(200) NULL,
	flg_annullato numeric(1) DEFAULT 0 NOT NULL,
	flg_restituito_mittente numeric(1) DEFAULT 0 NOT NULL,
	flg_invio_speciale numeric(1) DEFAULT 0 NOT NULL,
	flg_dilazione numeric(1) DEFAULT 0 NOT NULL,
	flg_addebito_anno_successivo numeric(1) DEFAULT 0 NOT NULL,
	nota_rinnovo varchar(100) NULL,
	des_tipo_titolo varchar(100) NULL,
	CONSTRAINT chk_risca_w_stato_debitorio_01 CHECK ((flg_annullato = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_w_stato_debitorio_02 CHECK ((flg_restituito_mittente = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_w_stato_debitorio_03 CHECK ((flg_invio_speciale = ANY (ARRAY[(0)::numeric, (1)::numeric, (2)::numeric]))),
	CONSTRAINT chk_risca_w_stato_debitorio_04 CHECK ((flg_dilazione = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_w_stato_debitorio_05 CHECK ((flg_addebito_anno_successivo = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_w_stato_debitorio PRIMARY KEY (id_stato_debitorio)
);
CREATE INDEX ie_risca_w_stato_debitorio_01 ON risca_w_stato_debitorio USING btree (nap);


-- risca_w_terna_prezzi_energ definition

-- Drop table

-- DROP TABLE risca_w_terna_prezzi_energ;

CREATE TABLE risca_w_terna_prezzi_energ (
	"data" date NOT NULL,
	ora varchar(2) NOT NULL,
	prezzo_nord_orig numeric(10, 2) NULL,
	prezzo_nord numeric(10, 2) NULL,
	note_backoffice varchar(2000) NULL
);
COMMENT ON TABLE risca_w_terna_prezzi_energ IS 'Permette di aquisire i dati del prezzo orario dell''energia Terna da file esterno';


-- risca_w_terna_prod_energ definition

-- Drop table

-- DROP TABLE risca_w_terna_prod_energ;

CREATE TABLE risca_w_terna_prod_energ (
	cod_censimp varchar(20) NOT NULL,
	cod_utenza varchar(20) NOT NULL,
	"data" date NOT NULL,
	ora varchar(2) NOT NULL,
	codice_up varchar(50) NULL,
	energia_pro_kwh_orig numeric(12, 2) NULL,
	energia_pro_kwh numeric(12, 2) NULL,
	note_backoffice varchar(2000) NULL
);
COMMENT ON TABLE risca_w_terna_prod_energ IS 'Permette di acquisire i dati di produzione energia Terna di ogni azienda,utenza da file esterno';


-- risca_w_terna_prod_energ_xls definition

-- Drop table

-- DROP TABLE risca_w_terna_prod_energ_xls;

CREATE TABLE risca_w_terna_prod_energ_xls (
	anno varchar(26) NULL,
	mese varchar(26) NULL,
	codice_up varchar(26) NULL,
	codice_censimp varchar(26) NULL,
	"data" varchar(26) NULL,
	energia_pro_kwh varchar(26) NULL,
	ora varchar(26) NULL,
	cod_utenza varchar(26) NULL
);
COMMENT ON TABLE risca_w_terna_prod_energ_xls IS 'Permette di acquisire i dati di produzione energia Terna di ogni azienda,utenza da file esterno in formato XLS';


-- risca_w_terna_utenze definition

-- Drop table

-- DROP TABLE risca_w_terna_utenze;

CREATE TABLE risca_w_terna_utenze (
	cod_censimp varchar(20) NOT NULL,
	cod_utenza varchar(20) NOT NULL,
	potenza_nomin_media_kwh numeric(10, 2) NULL,
	titolare varchar(200) NULL,
	note_backoffice varchar(2000) NULL
);
COMMENT ON TABLE risca_w_terna_utenze IS 'Permette di acquisire i dati delle utenze Terna da file esterno';


-- risca_d_attivita_stato_deb definition

-- Drop table

-- DROP TABLE risca_d_attivita_stato_deb;

CREATE TABLE risca_d_attivita_stato_deb (
	id_attivita_stato_deb int4 NOT NULL, -- Identificativo univoco
	cod_attivita_stato_deb varchar(2) NOT NULL,
	des_attivita_stato_deb varchar(50) NOT NULL,
	id_tipo_attivita_stato_deb int4 NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT ak_risca_d_attivita_stato_deb_01 UNIQUE (cod_attivita_stato_deb),
	CONSTRAINT pk_risca_d_attivita_stato_deb PRIMARY KEY (id_attivita_stato_deb),
	CONSTRAINT fk_risca_d_tipo_attivita_stato_deb_01 FOREIGN KEY (id_tipo_attivita_stato_deb) REFERENCES risca_d_tipo_attivita_stato_deb(id_tipo_attivita_stato_deb)
);
COMMENT ON TABLE risca_d_attivita_stato_deb IS 'Permette di definire le possibili attività dello stato debitorio es. NON ACCERTATO AVVISO BONARIO DA SPEDIRE SOLLECITO DI PAGAMENTO DA SPEDIRE DA INVIARE A RISCOSSIONE COATTIVA FALLIMENTO DA RIMBORSARE DA VERIFICARE DA COMPENSARE CANONE INESIGIBILE CONCORDATO';

-- Column comments

COMMENT ON COLUMN risca_d_attivita_stato_deb.id_attivita_stato_deb IS 'Identificativo univoco';


-- risca_d_componente_dt definition

-- Drop table

-- DROP TABLE risca_d_componente_dt;

CREATE TABLE risca_d_componente_dt (
	id_componente_dt int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	id_tipo_componente_dt int4 NOT NULL,
	nome_componente_dt varchar(50) NOT NULL,
	des_componente_dt varchar(150) NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_componente_dt PRIMARY KEY (id_componente_dt),
	CONSTRAINT fk_risca_d_ambito_10 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_tipo_componente_dt_01 FOREIGN KEY (id_tipo_componente_dt) REFERENCES risca_d_tipo_componente_dt(id_tipo_componente_dt)
);
CREATE UNIQUE INDEX ak_risca_d_componente_dt_01 ON risca_d_componente_dt USING btree (id_ambito, id_tipo_componente_dt, nome_componente_dt);

-- Column comments

COMMENT ON COLUMN risca_d_componente_dt.id_componente_dt IS 'Identificativo univoco';


-- risca_d_dato_tecnico definition

-- Drop table

-- DROP TABLE risca_d_dato_tecnico;

CREATE TABLE risca_d_dato_tecnico (
	id_dato_tecnico int4 NOT NULL, -- Identificativo univoco
	cod_dato_tecnico varchar(50) NOT NULL,
	des_dato_tecnico varchar(150) NULL,
	id_unita_misura int4 NOT NULL,
	id_tipo_dato_tecnico int4 NOT NULL,
	flg_calcolato numeric(1) DEFAULT 0 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_dato_tecnico_01 CHECK ((flg_calcolato = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_dato_tecnico PRIMARY KEY (id_dato_tecnico),
	CONSTRAINT fk_risca_d_tipo_dato_tecnico_01 FOREIGN KEY (id_tipo_dato_tecnico) REFERENCES risca_d_tipo_dato_tecnico(id_tipo_dato_tecnico),
	CONSTRAINT fk_risca_d_unita_misura_01 FOREIGN KEY (id_unita_misura) REFERENCES risca_d_unita_misura(id_unita_misura)
);
CREATE UNIQUE INDEX ak_risca_d_dato_tecnico_01 ON risca_d_dato_tecnico USING btree (cod_dato_tecnico);
COMMENT ON TABLE risca_d_dato_tecnico IS 'Definisce il dizionario dei dati tecnici, necessari alla raccolta degli stessi nella riscossione.  E'' possibile definire un unità di misura e gli eventuali  valori ammessi per guidare l''utente nell''inserimento del dato.   il flg_calcolato si riferisce a dati tecnici calcolati rispetto all''inserimento dell''utente di ulteriori dati  Es. (per risorse idriche, la % acqua di falda profonda'')';

-- Column comments

COMMENT ON COLUMN risca_d_dato_tecnico.id_dato_tecnico IS 'Identificativo univoco';


-- risca_d_email_servizio definition

-- Drop table

-- DROP TABLE risca_d_email_servizio;

CREATE TABLE risca_d_email_servizio (
	id_email_servizio int4 NOT NULL, -- Identificativo univoco
	cod_email_servizio varchar(20) NOT NULL,
	des_email_servizio varchar(150) NOT NULL,
	oggetto_email_servizio varchar(250) NOT NULL,
	id_fase_elabora int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_email_servizio PRIMARY KEY (id_email_servizio),
	CONSTRAINT fk_risca_d_fase_elabora_02 FOREIGN KEY (id_fase_elabora) REFERENCES risca_d_fase_elabora(id_fase_elabora)
);
CREATE UNIQUE INDEX ak_risca_d_email_servizio_01 ON risca_d_email_servizio USING btree (id_fase_elabora, cod_email_servizio);
COMMENT ON TABLE risca_d_email_servizio IS 'Permette di definire le tipologie di messaggio comunicate al servizio applicativo';

-- Column comments

COMMENT ON COLUMN risca_d_email_servizio.id_email_servizio IS 'Identificativo univoco';


-- risca_d_email_standard definition

-- Drop table

-- DROP TABLE risca_d_email_standard;

CREATE TABLE risca_d_email_standard (
	id_email_standard int4 NOT NULL, -- Identificativo univoco
	id_fase_elabora int4 NOT NULL,
	cod_email_standard varchar(20) NOT NULL,
	des_email_standard varchar(150) NOT NULL,
	oggetto_email varchar(250) NOT NULL,
	testo_email varchar(2000) NOT NULL,
	allegato_email varchar(250) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_email_standard PRIMARY KEY (id_email_standard),
	CONSTRAINT fk_risca_d_fase_elabora_03 FOREIGN KEY (id_fase_elabora) REFERENCES risca_d_fase_elabora(id_fase_elabora)
);
CREATE UNIQUE INDEX ak_risca_d_email_standard_01 ON risca_d_email_standard USING btree (id_fase_elabora, cod_email_standard);
COMMENT ON TABLE risca_d_email_standard IS 'Permette di definire i messaggi standard comunicati all''utente applicativo mediante email';

-- Column comments

COMMENT ON COLUMN risca_d_email_standard.id_email_standard IS 'Identificativo univoco';


-- risca_d_ente_pagopa definition

-- Drop table

-- DROP TABLE risca_d_ente_pagopa;

CREATE TABLE risca_d_ente_pagopa (
	id_ente_pagopa int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	cod_ente_pagopa varchar(2) NOT NULL,
	des_ente_pagopa varchar(50) NULL,
	cod_settore varchar(20) NULL,
	cf_ente_creditore varchar(35) NOT NULL,
	cod_versamento varchar(4) NOT NULL,
	flg_richiesta_iuv numeric(1) DEFAULT 0 NOT NULL,
	causale varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_ente_pagopa_01 CHECK ((flg_richiesta_iuv = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_ente_pagopa PRIMARY KEY (id_ente_pagopa),
	CONSTRAINT fk_risca_d_ambito_19 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
CREATE UNIQUE INDEX ak_risca_d_ente_pagopa_01 ON risca_d_ente_pagopa USING btree (id_ambito, cod_ente_pagopa);
COMMENT ON TABLE risca_d_ente_pagopa IS 'Permette di definire le voci di bilancio accertamento
ES. 
1 Canone
2 Canone grande idroelettrico
3 Canone aggiuntivo grande idroelettrico
4 Canone variabile grande idroelettrico
5 Canone monetizzazione grande idroelettrico
10 Interessi
20 Spese notifica';

-- Column comments

COMMENT ON COLUMN risca_d_ente_pagopa.id_ente_pagopa IS 'Identificativo univoco';


-- risca_d_messaggio definition

-- Drop table

-- DROP TABLE risca_d_messaggio;

CREATE TABLE risca_d_messaggio (
	id_messaggio int4 NOT NULL, -- Identificativo univoco
	id_tipo_messaggio int4 NOT NULL,
	cod_messaggio varchar(20) NOT NULL,
	des_testo_messaggio varchar(300) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT ak_risca_d_messaggio_01 UNIQUE (id_tipo_messaggio, cod_messaggio),
	CONSTRAINT pk_risca_d_messaggio PRIMARY KEY (id_messaggio),
	CONSTRAINT fk_risca_d_tipo_messaggio_01 FOREIGN KEY (id_tipo_messaggio) REFERENCES risca_d_tipo_messaggio(id_tipo_messaggio)
);
COMMENT ON TABLE risca_d_messaggio IS 'E'' il dizionario dei messaggi applicativi. Permette di codificare e centralizzare i messaggi utilizzati dai componenti applicativi.  E'' possibile tipizzare il messaggio in modo da distinguere il contesto. Es. ''Errore, Informazione, etc...''';

-- Column comments

COMMENT ON COLUMN risca_d_messaggio.id_messaggio IS 'Identificativo univoco';


-- risca_d_nazione definition

-- Drop table

-- DROP TABLE risca_d_nazione;

CREATE TABLE risca_d_nazione (
	id_nazione numeric(3) NOT NULL,
	cod_istat_nazione varchar(3) NULL,
	cod_belfiore_nazione varchar(4) NULL,
	denom_nazione varchar(100) NOT NULL,
	id_continente numeric(2) NOT NULL,
	unione_europea bool NULL,
	id_origine numeric(2) NOT NULL,
	cod_iso2 varchar(2) NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	dt_id_stato numeric(20) NOT NULL,
	dt_id_stato_prev numeric(20) NULL,
	dt_id_stato_next numeric(20) NULL,
	CONSTRAINT pk_risca_d_nazione PRIMARY KEY (id_nazione),
	CONSTRAINT fk_risca_d_continente_01 FOREIGN KEY (id_continente) REFERENCES risca_d_continente(id_continente),
	CONSTRAINT fk_risca_d_origine_limiti_01 FOREIGN KEY (id_origine) REFERENCES risca_d_origine_limiti(id_origine)
);
CREATE UNIQUE INDEX ak_risca_d_nazione_01 ON risca_d_nazione USING btree (cod_istat_nazione, cod_belfiore_nazione, denom_nazione, data_inizio_validita);


-- risca_d_oggetto_app definition

-- Drop table

-- DROP TABLE risca_d_oggetto_app;

CREATE TABLE risca_d_oggetto_app (
	id_oggetto_app int4 NOT NULL, -- Identificativo univoco
	id_tipo_oggetto_app int4 NOT NULL,
	cod_oggetto_app varchar(20) NOT NULL,
	des_oggetto_app varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_oggetto_app PRIMARY KEY (id_oggetto_app),
	CONSTRAINT fk_risca_d_tipo_oggetto_app_01 FOREIGN KEY (id_tipo_oggetto_app) REFERENCES risca_d_tipo_oggetto_app(id_tipo_oggetto_app)
);
CREATE UNIQUE INDEX ak_risca_d_oggetto_app_01 ON risca_d_oggetto_app USING btree (cod_oggetto_app);
COMMENT ON TABLE risca_d_oggetto_app IS 'Permette di definire gli oggetti applicativi per cui viene definita una regola di profilazione. es. ''Ricerca Riscossione, Visualizza Riscossione,etc..''   Ogni oggetto è tipizzato rispetto ad una tipologia es. ''Funzione, Servizio, etc...''';

-- Column comments

COMMENT ON COLUMN risca_d_oggetto_app.id_oggetto_app IS 'Identificativo univoco';


-- risca_d_passo_elabora definition

-- Drop table

-- DROP TABLE risca_d_passo_elabora;

CREATE TABLE risca_d_passo_elabora (
	id_passo_elabora int4 NOT NULL, -- Identificativo univoco
	id_tipo_passo_elabora int4 NOT NULL,
	cod_passo_elabora varchar(20) NOT NULL,
	des_passo_elabora varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_passo_elabora PRIMARY KEY (id_passo_elabora),
	CONSTRAINT fk_risca_d_tipo_passo_elabora_01 FOREIGN KEY (id_tipo_passo_elabora) REFERENCES risca_d_tipo_passo_elabora(id_tipo_passo_elabora)
);
CREATE INDEX ak_risca_d_passo_elabora_01 ON risca_d_passo_elabora USING btree (cod_passo_elabora);
COMMENT ON TABLE risca_d_passo_elabora IS 'Permette di definire i passi di una specifica fase prevista dal tipo di elaborazione in esecuzione Es. elaborazione :   (RPGEBO10) - CALCOLO_CANONE - (Esegue i calcoli dell’importo da pagare) - CREA_TAB_WORKING - (Aggiorna le tabelle di WORKING su db) - GENERA_NAP - (Genera il NAP) - GENERA_FILE_EMISSIONE - (produce 3 file (1 excel, 1 txt completo, 1 txt solo con le comunicazioni da stampare)  (RPGERI010)  - PRENOTA_IUV - PRENOTAZIONE IUV di PAGOPA - SALVA_IUV - Memorizza gli IUV  (RPGEBO030) - RIGENERA_FILE- Rigenera i due file TXT con nomi diversi aggiungendo anche lo IUV per produrre i PDF delle comunicazioni finali - DEPOSITA_FILE - Deposita i TXT in apposita area su FTPRUPAR  (RPGEBO020) - INVIA_PRIMPAMAIL - Invia i dati al servizio di PRIMPAMAIL per l’invio automatico delle PEC/Mail con allegata la comunicazione - POSTALIZZA -    Invia i dati al servizio di POSTALIZZAZIONE per stampa e invio cartaceo dei bollettini PDF mediante posta ordinaria - SALVA_PDF_DB - Memorizza i PDF su una tabella del db (in futuro su DOQUI ?) - CONSOLIDA_DATI - Consolida le tabelle di WORKING nelle tabelle APPLICATIVE - ELIMINA_WRK - Svuota le tabelle di WORKING ?';

-- Column comments

COMMENT ON COLUMN risca_d_passo_elabora.id_passo_elabora IS 'Identificativo univoco';


-- risca_d_regione definition

-- Drop table

-- DROP TABLE risca_d_regione;

CREATE TABLE risca_d_regione (
	id_regione numeric(6) NOT NULL,
	cod_regione varchar(3) NOT NULL,
	denom_regione varchar(100) NOT NULL,
	id_nazione numeric(3) NOT NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	CONSTRAINT pk_risca_d_regione PRIMARY KEY (id_regione),
	CONSTRAINT fk_risca_d_nazione_02 FOREIGN KEY (id_nazione) REFERENCES risca_d_nazione(id_nazione)
);
CREATE UNIQUE INDEX ak_risca_d_regione_01 ON risca_d_regione USING btree (cod_regione, id_nazione, data_inizio_validita);
CREATE UNIQUE INDEX ak_risca_d_regione_02 ON risca_d_regione USING btree (denom_regione, id_nazione, data_inizio_validita);


-- risca_d_stato_riscossione definition

-- Drop table

-- DROP TABLE risca_d_stato_riscossione;

CREATE TABLE risca_d_stato_riscossione (
	id_stato_riscossione int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	cod_stato_riscossione varchar(20) NOT NULL,
	des_stato_riscossione varchar(100) NULL,
	flg_default numeric(1) DEFAULT 0 NOT NULL, -- 0-No  1-Si  permette di selezionare l'elemento di default nelle tendine
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_stato_riscossione_01 CHECK ((flg_default = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_stato_riscossione PRIMARY KEY (id_stato_riscossione),
	CONSTRAINT fk_risca_d_ambito_06 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
CREATE UNIQUE INDEX ak_risca_d_stato_riscossione_01 ON risca_d_stato_riscossione USING btree (id_ambito, cod_stato_riscossione);
COMMENT ON TABLE risca_d_stato_riscossione IS 'Censisce gli stati previsti nelle riscossioni, per ambito.';

-- Column comments

COMMENT ON COLUMN risca_d_stato_riscossione.id_stato_riscossione IS 'Identificativo univoco';
COMMENT ON COLUMN risca_d_stato_riscossione.flg_default IS '0-No  1-Si  permette di selezionare l''elemento di default nelle tendine';


-- risca_d_tipo_autorizza definition

-- Drop table

-- DROP TABLE risca_d_tipo_autorizza;

CREATE TABLE risca_d_tipo_autorizza (
	id_tipo_autorizza int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	cod_tipo_autorizza varchar(20) NOT NULL,
	des_tipo_autorizza varchar(150) NULL,
	ordina_tipo_autorizza int4 NULL,
	flg_default numeric(1) DEFAULT 0 NOT NULL, -- 0-No  1-Si  permette di selezionare l'elemento di default nelle tendine
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_tipo_autorizza_01 CHECK ((flg_default = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_tipo_autorizza PRIMARY KEY (id_tipo_autorizza),
	CONSTRAINT fk_risca_d_ambito_04 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_autorizza_01 ON risca_d_tipo_autorizza USING btree (cod_tipo_autorizza, id_ambito);
COMMENT ON TABLE risca_d_tipo_autorizza IS 'Censisce i tipi di autorizzazione previsti per ambito (vecchia gestione ''tipo procedimento'' in GERICA .  E'' possibile decidere l''ordinamento delle voci nell''applicativo, utilizzando il campo ordina_tipo_autorizza.';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_autorizza.id_tipo_autorizza IS 'Identificativo univoco';
COMMENT ON COLUMN risca_d_tipo_autorizza.flg_default IS '0-No  1-Si  permette di selezionare l''elemento di default nelle tendine';


-- risca_d_tipo_dilazione definition

-- Drop table

-- DROP TABLE risca_d_tipo_dilazione;

CREATE TABLE risca_d_tipo_dilazione (
	id_tipo_dilazione int4 NOT NULL,
	data_inizio_val date NULL,
	data_fine_val date NULL,
	num_annualita_magg numeric(2) NULL,
	importo_magg numeric(10, 2) NULL,
	importo_min numeric(10, 2) NULL,
	num_mesi numeric(2) NULL,
	num_rate numeric(2) NULL,
	id_ambito int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_tipo_dilazione PRIMARY KEY (id_tipo_dilazione),
	CONSTRAINT fk_risca_d_ambito_18 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
COMMENT ON TABLE risca_d_tipo_dilazione IS 'Permette di definire le possibili dilazioni per ambito';


-- risca_d_tipo_elabora definition

-- Drop table

-- DROP TABLE risca_d_tipo_elabora;

CREATE TABLE risca_d_tipo_elabora (
	id_tipo_elabora int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	id_funzionalita int4 NOT NULL,
	cod_tipo_elabora varchar(2) NOT NULL,
	des_tipo_elabora varchar(50) NOT NULL,
	ordina_tipo_elabora numeric(4) NULL,
	flg_default numeric(1) DEFAULT 0 NOT NULL, -- la voce e' selezionata di default negli utilizzi previsti nella funzionalita' di riferimento
	flg_visibile numeric(1) DEFAULT 0 NOT NULL, -- la voce è visibile nella tendina 'nuova elaborazione' della sezione Pagamenti
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT ak_risca_d_tipo_elabora_01 UNIQUE (id_ambito, cod_tipo_elabora),
	CONSTRAINT chk_risca_d_tipo_elabora_01 CHECK ((flg_default = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_d_tipo_elabora_02 CHECK ((flg_visibile = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_tipo_elabora PRIMARY KEY (id_tipo_elabora),
	CONSTRAINT fk_risca_d_ambito_14 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_funzionalita_01 FOREIGN KEY (id_funzionalita) REFERENCES risca_d_funzionalita(id_funzionalita)
);
COMMENT ON TABLE risca_d_tipo_elabora IS 'Permette di definire le tipologie di elaborazione previste. Es. (Funzionalità Bollettazione) - Bollettazione Ordinaria (richiesta) - Bolletttazione Speciale (richiesta) - Solleciti di Pagamento - Avviso Bonario Es. (Stampe) - Stampa-1 - Stampa-2  --- ora definite come funzionalità ma sono dei PASSI della funzionalità Bollettazione Prenotazione richiesta IUV (cod_proc=RICH_IUV) Prenotazione Invio delle comunicazioni (cod_proc=????) Ricalcolo IUV (automatica)';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_elabora.id_tipo_elabora IS 'Identificativo univoco';
COMMENT ON COLUMN risca_d_tipo_elabora.flg_default IS 'la voce e'' selezionata di default negli utilizzi previsti nella funzionalita'' di riferimento';
COMMENT ON COLUMN risca_d_tipo_elabora.flg_visibile IS 'la voce è visibile nella tendina ''nuova elaborazione'' della sezione Pagamenti';


-- risca_d_tipo_provvedimento definition

-- Drop table

-- DROP TABLE risca_d_tipo_provvedimento;

CREATE TABLE risca_d_tipo_provvedimento (
	id_tipo_provvedimento int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	cod_tipo_provvedimento varchar(20) NOT NULL,
	des_tipo_provvedimento varchar(100) NULL,
	flg_istanza numeric(1) NOT NULL, -- 0-Provvedimento 1-Istanza
	ordina_tipo_provv int4 NULL,
	flg_default numeric(1) DEFAULT 0 NOT NULL, -- 0-No  1-Si  permette di selezionare l'elemento di default nelle tendine
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_tipo_provvedimento_01 CHECK ((flg_istanza = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_d_tipo_provvedimento_02 CHECK ((flg_default = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_tipo_provvedimento PRIMARY KEY (id_tipo_provvedimento),
	CONSTRAINT fk_risca_d_ambito_02 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_provvedimento_01 ON risca_d_tipo_provvedimento USING btree (cod_tipo_provvedimento, id_ambito);
COMMENT ON TABLE risca_d_tipo_provvedimento IS 'Censisce i tipi di provvedimento e le tipologie di istanza previste per ambito che hanno portato alla concessione.  il flg_istanza permette di specificare quelli che sono tipologie di istanza (1). E'' possibile decidere l''ordinamento delle voci nell''applicativo, utilizzando il campo ordina_tipo_provv.';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_provvedimento.id_tipo_provvedimento IS 'Identificativo univoco';
COMMENT ON COLUMN risca_d_tipo_provvedimento.flg_istanza IS '0-Provvedimento 1-Istanza';
COMMENT ON COLUMN risca_d_tipo_provvedimento.flg_default IS '0-No  1-Si  permette di selezionare l''elemento di default nelle tendine';


-- risca_d_tipo_riscossione definition

-- Drop table

-- DROP TABLE risca_d_tipo_riscossione;

CREATE TABLE risca_d_tipo_riscossione (
	id_tipo_riscossione int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	cod_tipo_riscossione varchar(20) NOT NULL,
	des_tipo_riscossione varchar(100) NULL,
	ordina_tipo_risco int4 NULL,
	flg_default numeric(1) DEFAULT 0 NOT NULL, -- 0-No  1-Si  permette di selezionare l'elemento di default nelle tendine
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_tipo_riscossione_01 CHECK ((flg_default = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_tipo_riscossione PRIMARY KEY (id_tipo_riscossione),
	CONSTRAINT fk_risca_d_ambito_03 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_riscossione_01 ON risca_d_tipo_riscossione USING btree (cod_tipo_riscossione, id_ambito);
COMMENT ON TABLE risca_d_tipo_riscossione IS 'Censisce i tipi di riscossione gestiti dal sistema, per ambito (vecchia gestione ''tipo pratica'' in GERICA .  E'' possibile decidere l''ordinamento delle voci nell''applicativo, utilizzando il campo ordina_tipo_risco.';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_riscossione.id_tipo_riscossione IS 'Identificativo univoco';
COMMENT ON COLUMN risca_d_tipo_riscossione.flg_default IS '0-No  1-Si  permette di selezionare l''elemento di default nelle tendine';


-- risca_d_tipo_titolo definition

-- Drop table

-- DROP TABLE risca_d_tipo_titolo;

CREATE TABLE risca_d_tipo_titolo (
	id_tipo_titolo int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	cod_tipo_titolo varchar(20) NOT NULL,
	des_tipo_titolo varchar(100) NULL,
	ordina_tipo_titolo int4 NULL,
	flg_default numeric(1) DEFAULT 0 NOT NULL, -- 0-No  1-Si  permette di selezionare l'elemento di default nelle tendine
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_tipo_titolo_01 CHECK ((flg_default = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_tipo_titolo PRIMARY KEY (id_tipo_titolo),
	CONSTRAINT fk_risca_d_ambito_01 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_titolo_01 ON risca_d_tipo_titolo USING btree (cod_tipo_titolo, id_ambito);
COMMENT ON TABLE risca_d_tipo_titolo IS 'Censisce i tipi di ''titolo'' amministrativo con cui è stato emesso il provvedimento.  E'' possibile tipizzare il titolo rispetto all''ambito di gestione. E'' possibile decidere l''ordinamento delle voci nell''applicativo, utilizzando il campo ordina_tipo_titolo.';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_titolo.id_tipo_titolo IS 'Identificativo univoco';
COMMENT ON COLUMN risca_d_tipo_titolo.flg_default IS '0-No  1-Si  permette di selezionare l''elemento di default nelle tendine';


-- risca_d_tipo_uso definition

-- Drop table

-- DROP TABLE risca_d_tipo_uso;

CREATE TABLE risca_d_tipo_uso (
	id_tipo_uso int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	id_unita_misura int4 NULL,
	id_accerta_bilancio int4 NULL,
	cod_tipo_uso varchar(20) NOT NULL,
	des_tipo_uso varchar(150) NULL,
	id_tipo_uso_padre int4 NULL,
	flg_uso_principale numeric(1) DEFAULT 0 NOT NULL,
	ordina_tipo_uso int4 NULL,
	flg_default numeric(1) DEFAULT 0 NOT NULL, -- 0-No  1-Si  permette di selezionare l'elemento di default nelle tendine
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_d_tipo_uso_01 CHECK ((flg_uso_principale = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_d_tipo_uso_02 CHECK ((flg_default = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_d_tipo_uso PRIMARY KEY (id_tipo_uso),
	CONSTRAINT fk_risca_d_accerta_bilancio_01 FOREIGN KEY (id_accerta_bilancio) REFERENCES risca_d_accerta_bilancio(id_accerta_bilancio),
	CONSTRAINT fk_risca_d_ambito_05 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_tipo_uso_03 FOREIGN KEY (id_tipo_uso_padre) REFERENCES risca_d_tipo_uso(id_tipo_uso),
	CONSTRAINT fk_risca_d_unita_misura_02 FOREIGN KEY (id_unita_misura) REFERENCES risca_d_unita_misura(id_unita_misura)
);
CREATE UNIQUE INDEX ak_risca_d_tipo_uso_01 ON risca_d_tipo_uso USING btree (cod_tipo_uso);
COMMENT ON TABLE risca_d_tipo_uso IS 'Censisce gli usi/utilizzi di legge nel contesto dei dati tecnici di una riscossione, per ambito. Permette di definire gli entuali usi/utilizzi specifici di uso padre (mediante id_tipo_uso_padre).   E'' possibile decidere l''ordinamento delle voci nell''applicativo, utilizzando il campo ordina_tipo_uso.';

-- Column comments

COMMENT ON COLUMN risca_d_tipo_uso.id_tipo_uso IS 'Identificativo univoco';
COMMENT ON COLUMN risca_d_tipo_uso.flg_default IS '0-No  1-Si  permette di selezionare l''elemento di default nelle tendine';


-- risca_r_ambito_area_competenza definition

-- Drop table

-- DROP TABLE risca_r_ambito_area_competenza;

CREATE TABLE risca_r_ambito_area_competenza (
	id_ambito_area_competenza int4 NOT NULL,
	id_ambito int4 NOT NULL,
	id_tipo_area_competenza int4 NOT NULL,
	chiave_area_competenza varchar(100) NOT NULL, -- campo che contiene il dato da utilizzare per applicare il filtro
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_ambito_area_competenza PRIMARY KEY (id_ambito_area_competenza),
	CONSTRAINT fk_risca_d_ambito_22 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_tipo_area_competenza_01 FOREIGN KEY (id_tipo_area_competenza) REFERENCES risca_d_tipo_area_competenza(id_tipo_area_competenza)
);
COMMENT ON TABLE risca_r_ambito_area_competenza IS 'Permette di definire l''area di competenza per uno specifico ambito.
Rispetto all''utente applicativo permette di dare visibilità solo ai dati di sua competenza.
Es .id_ambito = 1 ;  PROV ; chiave_area_competenza = cod_riscossione_prov';

-- Column comments

COMMENT ON COLUMN risca_r_ambito_area_competenza.chiave_area_competenza IS 'campo che contiene il dato da utilizzare per applicare il filtro';


-- risca_r_ambito_config definition

-- Drop table

-- DROP TABLE risca_r_ambito_config;

CREATE TABLE risca_r_ambito_config (
	id_ambito_config int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	chiave varchar(150) NULL,
	valore varchar(1000) NULL,
	flg_attivo numeric(1) NULL,
	note varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_r_ambito_config_01 CHECK ((flg_attivo = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_r_ambito_config PRIMARY KEY (id_ambito_config),
	CONSTRAINT fk_risca_d_ambito_08 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
CREATE UNIQUE INDEX ak_risca_r_ambito_config_01 ON risca_r_ambito_config USING btree (id_ambito, chiave);

-- Column comments

COMMENT ON COLUMN risca_r_ambito_config.id_ambito_config IS 'Identificativo univoco';


-- risca_r_ambito_fonte definition

-- Drop table

-- DROP TABLE risca_r_ambito_fonte;

CREATE TABLE risca_r_ambito_fonte (
	id_fonte int4 NOT NULL,
	id_ambito int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_ambito_fonte PRIMARY KEY (id_fonte, id_ambito),
	CONSTRAINT fk_risca_d_ambito_12 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_fonte_01 FOREIGN KEY (id_fonte) REFERENCES risca_d_fonte(id_fonte)
);
COMMENT ON TABLE risca_r_ambito_fonte IS 'Permette di definire gli ambiti abilitati per ciascuna Fonte (Fruitore)';


-- risca_r_ambito_interesse definition

-- Drop table

-- DROP TABLE risca_r_ambito_interesse;

CREATE TABLE risca_r_ambito_interesse (
	id_ambito_interesse int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	tipo_interesse varchar(1) NOT NULL, -- definisce il tipo di interesse configurato : L-Legale, M-Mora
	data_inizio date NOT NULL,
	data_fine date NOT NULL,
	percentuale numeric(7, 4) NOT NULL,
	giorni_legali numeric(3) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_r_ambito_interesse_01 CHECK (((tipo_interesse)::text = ANY (ARRAY[('L'::character varying)::text, ('M'::character varying)::text]))),
	CONSTRAINT pk_risca_r_ambito_interesse PRIMARY KEY (id_ambito_interesse),
	CONSTRAINT fk_risca_d_ambito_20 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
CREATE UNIQUE INDEX ak_risca_r_ambito_interesse_01 ON risca_r_ambito_interesse USING btree (id_ambito, tipo_interesse, data_inizio);
COMMENT ON TABLE risca_r_ambito_interesse IS 'permette di definire l''''interesse da calcolae in un determinato periodo rispetto all''''ambito di riferimento. Ex tabella : GERICA_T_INTERESSI';

-- Column comments

COMMENT ON COLUMN risca_r_ambito_interesse.id_ambito_interesse IS 'Identificativo univoco';
COMMENT ON COLUMN risca_r_ambito_interesse.tipo_interesse IS 'definisce il tipo di interesse configurato : L-Legale, M-Mora';


-- risca_r_dt_valore_ammesso definition

-- Drop table

-- DROP TABLE risca_r_dt_valore_ammesso;

CREATE TABLE risca_r_dt_valore_ammesso (
	id_dt_valore_ammesso int4 NOT NULL,
	id_dato_tecnico int4 NOT NULL,
	des_dt_valore_ammesso varchar(150) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_dt_valore_ammesso PRIMARY KEY (id_dt_valore_ammesso),
	CONSTRAINT fk_risca_d_dato_tecnico_01 FOREIGN KEY (id_dato_tecnico) REFERENCES risca_d_dato_tecnico(id_dato_tecnico)
);
COMMENT ON TABLE risca_r_dt_valore_ammesso IS 'Rappresenta i valori ammessi per il DT in esame';


-- risca_r_etichetta definition

-- Drop table

-- DROP TABLE risca_r_etichetta;

CREATE TABLE risca_r_etichetta (
	id_etichetta int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL, -- Ambito di appartenenza
	cod_etichetta varchar(20) NOT NULL,
	val_etichetta varchar(100) NOT NULL,
	nota_etichetta varchar(250) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_etichetta PRIMARY KEY (id_etichetta),
	CONSTRAINT fk_risca_d_ambito_11 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
CREATE INDEX ak_risca_r_etichetta_01 ON risca_r_etichetta USING btree (id_ambito, cod_etichetta);
CREATE UNIQUE INDEX ak_risca_r_etichetta_dt_01 ON risca_r_etichetta USING btree (id_ambito, cod_etichetta);
COMMENT ON TABLE risca_r_etichetta IS 'Permette di definire le etichette degli oggetti applicativi, utilizzate per uno specifico ambito amminitrativo. Il cod_etichetta in abbinamento all''id_ambito, permette di identificarla univocamente.';

-- Column comments

COMMENT ON COLUMN risca_r_etichetta.id_etichetta IS 'Identificativo univoco';
COMMENT ON COLUMN risca_r_etichetta.id_ambito IS 'Ambito di appartenenza';


-- risca_r_funzionalita_stato definition

-- Drop table

-- DROP TABLE risca_r_funzionalita_stato;

CREATE TABLE risca_r_funzionalita_stato (
	id_funzionalita int4 NOT NULL,
	id_stato_elabora int4 NOT NULL,
	ordina_tipo_stato_elabora numeric(4) NULL,
	flg_default numeric(1) DEFAULT 0 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_r_tipo_stato_elabora_01 CHECK ((flg_default = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_r_funzionalita_stato PRIMARY KEY (id_funzionalita, id_stato_elabora),
	CONSTRAINT fk_risca_d_funzionalita_02 FOREIGN KEY (id_funzionalita) REFERENCES risca_d_funzionalita(id_funzionalita),
	CONSTRAINT fk_risca_d_stato_elabora_02 FOREIGN KEY (id_stato_elabora) REFERENCES risca_d_stato_elabora(id_stato_elabora)
);
COMMENT ON TABLE risca_r_funzionalita_stato IS 'Definisce gli stati previsti per il tipo funzionalità';


-- risca_r_gruppo_delega definition

-- Drop table

-- DROP TABLE risca_r_gruppo_delega;

CREATE TABLE risca_r_gruppo_delega (
	id_gruppo_delega int4 NOT NULL, -- Identificativo univoco
	id_gruppo_soggetto int4 NOT NULL,
	id_delegato int4 NOT NULL,
	data_abilitazione date NOT NULL,
	data_disabilitazione date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_gruppo_delega PRIMARY KEY (id_gruppo_delega),
	CONSTRAINT fk_risca_t_delegato_02 FOREIGN KEY (id_delegato) REFERENCES risca_t_delegato(id_delegato),
	CONSTRAINT fk_risca_t_gruppo_soggetto_07 FOREIGN KEY (id_gruppo_soggetto) REFERENCES risca_t_gruppo_soggetto(id_gruppo_soggetto)
);
CREATE UNIQUE INDEX ak_risca_r_gruppo_delega_01 ON risca_r_gruppo_delega USING btree (id_gruppo_soggetto, id_delegato);
COMMENT ON TABLE risca_r_gruppo_delega IS 'Permette di definire le eventuali figure delegate alla consultazione delle proprie Riscossioni da parte del gruppo';

-- Column comments

COMMENT ON COLUMN risca_r_gruppo_delega.id_gruppo_delega IS 'Identificativo univoco';


-- risca_r_profilo_ogg_app definition

-- Drop table

-- DROP TABLE risca_r_profilo_ogg_app;

CREATE TABLE risca_r_profilo_ogg_app (
	id_profilo_pa int4 NOT NULL,
	id_oggetto_app int4 NOT NULL,
	flg_attivo numeric(1) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_r_profilo_ogg_app_01 CHECK ((flg_attivo = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_r_profilo_ogg_app PRIMARY KEY (id_profilo_pa, id_oggetto_app),
	CONSTRAINT fk_risca_d_oggetto_app_01 FOREIGN KEY (id_oggetto_app) REFERENCES risca_d_oggetto_app(id_oggetto_app),
	CONSTRAINT fk_risca_d_profilo_pa_01 FOREIGN KEY (id_profilo_pa) REFERENCES risca_d_profilo_pa(id_profilo_pa)
);
COMMENT ON TABLE risca_r_profilo_ogg_app IS 'Permette di definire per ogni oggetto applicativo il profilo abilitato all''utilizzo. Il flg_attivo, permette di disattivare la visibilita di quell''oggetto, anche solo temporaneamente.';


-- risca_r_terna_prod_energ definition

-- Drop table

-- DROP TABLE risca_r_terna_prod_energ;

CREATE TABLE risca_r_terna_prod_energ (
	cod_censimp varchar(20) NOT NULL, -- codice censimento imprese
	cod_utenza varchar(20) NOT NULL, -- codice riscossione risca
	"data" date NOT NULL,
	ora varchar(2) NOT NULL,
	codice_up varchar(50) NULL,
	energia_pro_kwh_orig numeric(12, 2) NULL,
	energia_pro_kwh numeric(12, 2) NULL,
	note_backoffice varchar(2000) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_terna_prod_energ PRIMARY KEY (cod_censimp, cod_utenza, data, ora),
	CONSTRAINT fk_risca_t_terna_utenze_01 FOREIGN KEY (cod_censimp,cod_utenza) REFERENCES risca_t_terna_utenze(cod_censimp,cod_utenza)
);
COMMENT ON TABLE risca_r_terna_prod_energ IS 'contiene i dati di produzione energia di ogni azienda,utenza fornite da terna, divisi per ora e giorno';

-- Column comments

COMMENT ON COLUMN risca_r_terna_prod_energ.cod_censimp IS 'codice censimento imprese';
COMMENT ON COLUMN risca_r_terna_prod_energ.cod_utenza IS 'codice riscossione risca';


-- risca_r_tipo_ricerca_mor_accerta definition

-- Drop table

-- DROP TABLE risca_r_tipo_ricerca_mor_accerta;

CREATE TABLE risca_r_tipo_ricerca_mor_accerta (
	id_tipo_ricerca_mor_accerta int4 NOT NULL, -- Identificativo univoco
	id_tipo_ricerca_morosita int4 NOT NULL,
	id_tipo_accertamento int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_tipo_ricerca_mor_accerta PRIMARY KEY (id_tipo_ricerca_mor_accerta),
	CONSTRAINT fk_risca_d_tipo_accertamento_04 FOREIGN KEY (id_tipo_accertamento) REFERENCES risca_d_tipo_accertamento(id_tipo_accertamento),
	CONSTRAINT fk_risca_d_tipo_ricerca_morosita_01 FOREIGN KEY (id_tipo_ricerca_morosita) REFERENCES risca_d_tipo_ricerca_morosita(id_tipo_ricerca_morosita)
);
CREATE UNIQUE INDEX ak_risca_r_tipo_ricerca_mor_accerta_01 ON risca_r_tipo_ricerca_mor_accerta USING btree (id_tipo_ricerca_morosita, id_tipo_accertamento);
COMMENT ON TABLE risca_r_tipo_ricerca_mor_accerta IS 'Permette di definire le tipologie di accertamento previste per una specifica tipo morisità ricercata
Non tutti i tipo morosità ricercata hanno un tipo accertamento associato

Es: 4 - AVVISO BONARIO SCADUTO (POSTEL O MANUALE) 
    tipi accertamento previsti : (1 AVB Avviso bonario) e (4 ABM Avviso bonario manuale)
';

-- Column comments

COMMENT ON COLUMN risca_r_tipo_ricerca_mor_accerta.id_tipo_ricerca_mor_accerta IS 'Identificativo univoco';


-- risca_r_tipo_sede_accertamento definition

-- Drop table

-- DROP TABLE risca_r_tipo_sede_accertamento;

CREATE TABLE risca_r_tipo_sede_accertamento (
	id_tipo_sede_accertamento int4 NOT NULL,
	id_tipo_sede int4 NOT NULL,
	id_tipo_accertamento int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_tipo_sede_accertamento PRIMARY KEY (id_tipo_sede_accertamento),
	CONSTRAINT fk_risca_d_tipo_accertamento_02 FOREIGN KEY (id_tipo_accertamento) REFERENCES risca_d_tipo_accertamento(id_tipo_accertamento),
	CONSTRAINT fk_risca_d_tipo_sede_02 FOREIGN KEY (id_tipo_sede) REFERENCES risca_d_tipo_sede(id_tipo_sede)
);
CREATE UNIQUE INDEX ak_risca_r_tipo_sede_accertamento_01 ON risca_r_tipo_sede_accertamento USING btree (id_tipo_sede, id_tipo_accertamento);


-- risca_r_tipo_uso_dt definition

-- Drop table

-- DROP TABLE risca_r_tipo_uso_dt;

CREATE TABLE risca_r_tipo_uso_dt (
	id_tipo_uso_dt int4 NOT NULL,
	id_tipo_uso int4 NOT NULL,
	id_dato_tecnico int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_tipo_uso_dt PRIMARY KEY (id_tipo_uso_dt),
	CONSTRAINT fk_risca_d_dato_tecnico_02 FOREIGN KEY (id_dato_tecnico) REFERENCES risca_d_dato_tecnico(id_dato_tecnico),
	CONSTRAINT fk_risca_d_tipo_uso_06 FOREIGN KEY (id_tipo_uso) REFERENCES risca_d_tipo_uso(id_tipo_uso)
);
CREATE UNIQUE INDEX ak_risca_r_tipo_uso_dt_01 ON risca_r_tipo_uso_dt USING btree (id_tipo_uso, id_dato_tecnico);
COMMENT ON TABLE risca_r_tipo_uso_dt IS 'Permette di associare ad un uso i suoi DT. Non è un vincolo applicativo, ma è necessaria a identificare gli elementi da presentare nelle eventuali liste.';


-- risca_r_tipo_uso_regola definition

-- Drop table

-- DROP TABLE risca_r_tipo_uso_regola;

CREATE TABLE risca_r_tipo_uso_regola (
	id_tipo_uso_regola int4 NOT NULL, -- Identificativo univoco
	id_tipo_uso int4 NOT NULL,
	data_inizio date NOT NULL,
	data_fine date NULL,
	json_regola jsonb NOT NULL,
	id_algoritmo int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_tipo_uso_regola PRIMARY KEY (id_tipo_uso_regola),
	CONSTRAINT fk_risca_d_algoritmo_01 FOREIGN KEY (id_algoritmo) REFERENCES risca_d_algoritmo(id_algoritmo),
	CONSTRAINT fk_risca_d_tipo_uso_01 FOREIGN KEY (id_tipo_uso) REFERENCES risca_d_tipo_uso(id_tipo_uso)
);
COMMENT ON TABLE risca_r_tipo_uso_regola IS 'Permette di definire le regole di calcolo del canone necessarie per i tipi d''uso di legge, previsti nella riscossione.   La regola è definita mediante la compilazione, con formalismo JSON, del campo json_regola. E'' possibile definire un periodo di validità valorizzando le date inizio e fine.';

-- Column comments

COMMENT ON COLUMN risca_r_tipo_uso_regola.id_tipo_uso_regola IS 'Identificativo univoco';


-- risca_r_tipo_uso_ridaum definition

-- Drop table

-- DROP TABLE risca_r_tipo_uso_ridaum;

CREATE TABLE risca_r_tipo_uso_ridaum (
	id_tipo_uso int4 NOT NULL,
	id_riduzione_aumento int4 NOT NULL,
	id_tipo_uso_ridaum int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_tipo_uso_ridaum PRIMARY KEY (id_tipo_uso_ridaum),
	CONSTRAINT fk_risca_d_riduzione_aumento_01 FOREIGN KEY (id_riduzione_aumento) REFERENCES risca_d_riduzione_aumento(id_riduzione_aumento),
	CONSTRAINT fk_risca_d_tipo_uso_04 FOREIGN KEY (id_tipo_uso) REFERENCES risca_d_tipo_uso(id_tipo_uso)
);
CREATE UNIQUE INDEX ak_risca_r_tipo_uso_ridaum_01 ON risca_r_tipo_uso_ridaum USING btree (id_tipo_uso, id_riduzione_aumento);


-- risca_s_nazione definition

-- Drop table

-- DROP TABLE risca_s_nazione;

CREATE TABLE risca_s_nazione (
	id_s_nazione numeric(3) NOT NULL,
	id_nazione numeric(3) NOT NULL,
	cod_istat_nazione varchar(3) NULL,
	cod_belfiore_nazione varchar(4) NULL,
	denom_nazione varchar(100) NOT NULL,
	id_continente numeric(2) NOT NULL,
	unione_europea bool NULL,
	cod_iso2 varchar(2) NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	dt_id_stato numeric(20) NOT NULL,
	dt_id_stato_prev numeric(20) NULL,
	dt_id_stato_next numeric(20) NULL,
	id_origine numeric(2) NOT NULL,
	CONSTRAINT pk_risca_s_nazione PRIMARY KEY (id_s_nazione),
	CONSTRAINT fk_risca_d_origine_limiti_02 FOREIGN KEY (id_origine) REFERENCES risca_d_origine_limiti(id_origine),
	CONSTRAINT fk_risca_s_nazione_01 FOREIGN KEY (id_nazione) REFERENCES risca_d_nazione(id_nazione)
);
CREATE UNIQUE INDEX ak_risca_s_nazione_01 ON risca_s_nazione USING btree (id_nazione, data_inizio_validita);


-- risca_s_regione definition

-- Drop table

-- DROP TABLE risca_s_regione;

CREATE TABLE risca_s_regione (
	id_s_regione numeric(6) NOT NULL,
	id_regione numeric(6) NOT NULL,
	cod_regione varchar(3) NOT NULL,
	denom_regione varchar(100) NOT NULL,
	id_nazione numeric(3) NOT NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	CONSTRAINT pk_risca_s_regione PRIMARY KEY (id_s_regione),
	CONSTRAINT fk_risca_d_nazione_03 FOREIGN KEY (id_nazione) REFERENCES risca_d_nazione(id_nazione),
	CONSTRAINT fk_risca_d_regione_01 FOREIGN KEY (id_regione) REFERENCES risca_d_regione(id_regione)
);
CREATE UNIQUE INDEX ak_risca_s_regione_01 ON risca_s_regione USING btree (id_regione, id_nazione, data_inizio_validita);
CREATE UNIQUE INDEX ak_risca_s_regione_02 ON risca_s_regione USING btree (cod_regione, id_nazione, data_inizio_validita);
CREATE UNIQUE INDEX ak_risca_s_regione_03 ON risca_s_regione USING btree (denom_regione, id_nazione, data_inizio_validita);


-- risca_t_bil_acc definition

-- Drop table

-- DROP TABLE risca_t_bil_acc;

CREATE TABLE risca_t_bil_acc (
	id_bil_acc int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	cod_bil_acc varchar(20) NOT NULL,
	des_bil_acc varchar(200) NOT NULL,
	anno numeric(4) NOT NULL,
	note_backoffice varchar(2000) NULL,
	dati_spec_risc varchar(140) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	anno_competenza numeric(4) NOT NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NOT NULL,
	id_accerta_bilancio int4 NOT NULL,
	CONSTRAINT pk_risca_t_bil_acc PRIMARY KEY (id_bil_acc),
	CONSTRAINT fk_risca_d_accerta_bilancio_02 FOREIGN KEY (id_accerta_bilancio) REFERENCES risca_d_accerta_bilancio(id_accerta_bilancio),
	CONSTRAINT fk_risca_d_ambito_17 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito)
);
COMMENT ON TABLE risca_t_bil_acc IS 'Gestita da servizio applicativo manualmente';

-- Column comments

COMMENT ON COLUMN risca_t_bil_acc.id_bil_acc IS 'Identificativo univoco';


-- risca_t_elabora definition

-- Drop table

-- DROP TABLE risca_t_elabora;

CREATE TABLE risca_t_elabora (
	id_elabora int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	id_tipo_elabora int4 NOT NULL,
	id_stato_elabora int4 NOT NULL,
	data_richiesta date NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_uid varchar(64) NULL,
	nome_file_generato varchar(500) NULL, -- nome del file .ods generato per il download da parte dell'utente (emissione)
	nome_file_stampe_massive varchar(500) NULL, -- nome del file .txt (completo), generato per la stampa massiva (conferma),
	CONSTRAINT pk_risca_t_elabora PRIMARY KEY (id_elabora),
	CONSTRAINT fk_risca_d_ambito_13 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_stato_elabora_01 FOREIGN KEY (id_stato_elabora) REFERENCES risca_d_stato_elabora(id_stato_elabora),
	CONSTRAINT fk_risca_d_tipo_elabora_01 FOREIGN KEY (id_tipo_elabora) REFERENCES risca_d_tipo_elabora(id_tipo_elabora)
);
CREATE INDEX ie_risca_t_elabora_01 ON risca_t_elabora USING btree (id_ambito, id_tipo_elabora, id_stato_elabora);
COMMENT ON TABLE risca_t_elabora IS 'Permette di memorizzare le elaborazioni  richieste o necessarie a funzionalità applicative.';

-- Column comments

COMMENT ON COLUMN risca_t_elabora.id_elabora IS 'Identificativo univoco';
COMMENT ON COLUMN risca_t_elabora.nome_file_generato IS 'nome del file .ods generato per il download da parte dell''utente (emissione)';
COMMENT ON COLUMN risca_t_elabora.nome_file_stampe_massive IS 'nome del file .txt (completo), generato per la stampa massiva (conferma)';


-- risca_t_iuv definition

-- Drop table

-- DROP TABLE risca_t_iuv;

CREATE TABLE risca_t_iuv (
	id_iuv int4 NOT NULL, -- identificativo univoco
	nap varchar(20) NOT NULL, -- identificativo univoco di pagamento
	id_stato_iuv int4 NOT NULL,
	iuv varchar(35) NOT NULL, -- identificativo univoco di versamento
	codice_avviso varchar(35) NOT NULL,
	importo numeric(13, 2) NOT NULL,
	codice_versamento varchar(4) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_t_iuv PRIMARY KEY (id_iuv),
	CONSTRAINT fk_risca_d_stato_iuv_01 FOREIGN KEY (id_stato_iuv) REFERENCES risca_d_stato_iuv(id_stato_iuv)
);
CREATE UNIQUE INDEX ak_risca_t_iuv_01 ON risca_t_iuv USING btree (iuv);
CREATE INDEX ie_risca_t_iuv_01 ON risca_t_iuv USING btree (codice_avviso);
CREATE INDEX ie_risca_t_iuv_02 ON risca_t_iuv USING btree (nap);
CREATE INDEX ie_risca_t_iuv_03 ON risca_t_iuv USING btree (id_stato_iuv);

-- Column comments

COMMENT ON COLUMN risca_t_iuv.id_iuv IS 'identificativo univoco';
COMMENT ON COLUMN risca_t_iuv.nap IS 'identificativo univoco di pagamento';
COMMENT ON COLUMN risca_t_iuv.iuv IS 'identificativo univoco di versamento';


-- risca_t_lotto definition

-- Drop table

-- DROP TABLE risca_t_lotto;

CREATE TABLE risca_t_lotto (
	id_lotto int4 NOT NULL, -- Identificativo univoco
	id_elabora int4 NOT NULL,
	nome_lotto varchar(50) NOT NULL,
	data_lotto date NOT NULL,
	flg_inviato numeric(1) DEFAULT 0 NOT NULL,
	flg_ricevuto numeric(1) DEFAULT 0 NOT NULL,
	flg_elaborato numeric(1) DEFAULT 0 NOT NULL,
	cod_esito_da_pagopa varchar(3) NULL, -- codice di ritorno del servizio inseriscilistadiCarico di pagopa
	desc_esito_da_pagopa varchar(200) NULL, -- descrizione esito di ritorno del servizio inseriscilistadiCarico di pagopa
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	cod_esito_acquisizione_pagopa varchar(3) NULL, -- codice esito ricevuto da pagopa sulla singola posizione debitoria in acquisizione IUV
	desc_esito_acquisizione_pagopa varchar(200) NULL, -- descrizione esito ricevuto da pagopa sulla singola posizione debitoria in acquisizione IUV
	CONSTRAINT ak_risca_t_lotto_01 UNIQUE (nome_lotto),
	CONSTRAINT chk_risca_t_lotto_01 CHECK ((flg_inviato = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_t_lotto_02 CHECK ((flg_ricevuto = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_t_lotto_03 CHECK ((flg_elaborato = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_t_lotto PRIMARY KEY (id_lotto),
	CONSTRAINT fk_risca_t_elabora_04 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora)
);
CREATE INDEX ie_risca_t_lotto_01 ON risca_t_lotto USING btree (id_elabora);
COMMENT ON TABLE risca_t_lotto IS 'Registro lotti prodotti per elaborazione';

-- Column comments

COMMENT ON COLUMN risca_t_lotto.id_lotto IS 'Identificativo univoco';
COMMENT ON COLUMN risca_t_lotto.cod_esito_da_pagopa IS 'codice di ritorno del servizio inseriscilistadiCarico di pagopa';
COMMENT ON COLUMN risca_t_lotto.desc_esito_da_pagopa IS 'descrizione esito di ritorno del servizio inseriscilistadiCarico di pagopa';
COMMENT ON COLUMN risca_t_lotto.cod_esito_acquisizione_pagopa IS 'codice esito ricevuto da pagopa sulla singola posizione debitoria in acquisizione IUV';
COMMENT ON COLUMN risca_t_lotto.desc_esito_acquisizione_pagopa IS 'descrizione esito ricevuto da pagopa sulla singola posizione debitoria in acquisizione IUV';


-- risca_t_pagamento definition

-- Drop table

-- DROP TABLE risca_t_pagamento;

CREATE TABLE risca_t_pagamento (
	id_pagamento int4 NOT NULL, -- identificativo univoco
	id_tipologia_pag int4 NOT NULL,
	id_tipo_modalita_pag int4 NOT NULL,
	id_file_poste int4 NULL,
	id_immagine int4 NULL,
	causale varchar(1000) NULL,
	data_op_val date NULL,
	importo_versato numeric(13, 2) NULL,
	data_download date NULL,
	quinto_campo varchar(16) NULL,
	cro varchar(16) NULL,
	note varchar(500) NULL,
	numero_pagamento numeric(6) NULL,
	soggetto_versamento varchar(200) NULL,
	indirizzo_versamento varchar(80) NULL,
	civico_versamento varchar(10) NULL,
	frazione_versamento varchar(50) NULL,
	comune_versamento varchar(40) NULL,
	cap_versamento varchar(5) NULL,
	prov_versamento varchar(20) NULL,
	flg_rimborsato numeric(1) DEFAULT 0 NOT NULL, -- FLG_RIMBORSATO (Gerica) -  identifica se il pagamento è stato rimborsato (1)
	imp_da_assegnare numeric(13, 2) NULL,
	codice_avviso varchar(35) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	id_ambito int4 NOT NULL,
	id_file_soris int4 NULL,
	CONSTRAINT chk_risca_t_pagamento_01 CHECK ((flg_rimborsato = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_t_pagamento PRIMARY KEY (id_pagamento),
	CONSTRAINT fk_risca_d_ambito_21 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_tipo_modalita_pag_01 FOREIGN KEY (id_tipo_modalita_pag) REFERENCES risca_d_tipo_modalita_pag(id_tipo_modalita_pag),
	CONSTRAINT fk_risca_d_tipologia_pag_01 FOREIGN KEY (id_tipologia_pag) REFERENCES risca_d_tipologia_pag(id_tipologia_pag),
	CONSTRAINT fk_risca_t_file_poste_01 FOREIGN KEY (id_file_poste) REFERENCES risca_t_file_poste(id_file_poste),
	CONSTRAINT fk_risca_t_file_soris_01 FOREIGN KEY (id_file_soris) REFERENCES risca_t_file_soris(id_file_soris),
	CONSTRAINT fk_risca_t_immagine_01 FOREIGN KEY (id_immagine) REFERENCES risca_t_immagine(id_immagine)
);
CREATE INDEX ie_risca_t_pagamento_01 ON risca_t_pagamento USING btree (id_tipologia_pag);
CREATE INDEX ie_risca_t_pagamento_02 ON risca_t_pagamento USING btree (id_tipo_modalita_pag);
CREATE INDEX ie_risca_t_pagamento_03 ON risca_t_pagamento USING btree (id_file_poste);
CREATE INDEX ie_risca_t_pagamento_04 ON risca_t_pagamento USING btree (id_immagine);

-- Column comments

COMMENT ON COLUMN risca_t_pagamento.id_pagamento IS 'identificativo univoco';
COMMENT ON COLUMN risca_t_pagamento.flg_rimborsato IS 'FLG_RIMBORSATO (Gerica) -  identifica se il pagamento è stato rimborsato (1)';


-- risca_t_spedizione definition

-- Drop table

-- DROP TABLE risca_t_spedizione;

CREATE TABLE risca_t_spedizione (
	id_spedizione int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	id_tipo_spedizione int4 NOT NULL,
	data_spedizione timestamp NOT NULL,
	data_protocollo date NOT NULL,
	num_protocollo varchar(30) NOT NULL,
	data_scadenza date NULL,
	num_determina varchar(25) NULL,
	data_determina date NULL,
	anno numeric(4) NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_uid varchar(64) NULL,
	id_elabora int4 NULL, -- Identificativo dell'elaborazione che ha generato la Spedizione
	CONSTRAINT pk_risca_t_spedizione PRIMARY KEY (id_spedizione),
	CONSTRAINT fk_risca_d_ambito_15 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_tipo_spedizione_01 FOREIGN KEY (id_tipo_spedizione) REFERENCES risca_d_tipo_spedizione(id_tipo_spedizione),
	CONSTRAINT fk_risca_t_elabora_07 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora)
);
CREATE INDEX ak_risca_t_spedizione_01 ON risca_t_spedizione USING btree (id_ambito, id_tipo_spedizione, data_spedizione);
COMMENT ON TABLE risca_t_spedizione IS 'Permette di memorizzare le spedizioni elaborate.';

-- Column comments

COMMENT ON COLUMN risca_t_spedizione.id_spedizione IS 'Identificativo univoco';
COMMENT ON COLUMN risca_t_spedizione.id_elabora IS 'Identificativo dell''elaborazione che ha generato la Spedizione';


-- risca_w_annualita_sd definition

-- Drop table

-- DROP TABLE risca_w_annualita_sd;

CREATE TABLE risca_w_annualita_sd (
	id_annualita_sd int4 NOT NULL,
	id_stato_debitorio int4 NOT NULL,
	anno numeric(4) NOT NULL,
	json_dt_riscossione jsonb NOT NULL,
	canone_annuo numeric(9, 2) NULL,
	flg_rateo_prima_annualita numeric(1) DEFAULT 0 NOT NULL,
	numero_mesi numeric(2) NULL,
	data_inizio date NULL,
	id_componente_dt int4 NOT NULL,
	CONSTRAINT chk_risca_w_annualita_uso_sd_01 CHECK ((flg_rateo_prima_annualita = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_w_annualita_sd PRIMARY KEY (id_annualita_sd),
	CONSTRAINT fk_risca_w_stato_debitorio_04 FOREIGN KEY (id_stato_debitorio) REFERENCES risca_w_stato_debitorio(id_stato_debitorio)
);
CREATE INDEX ie_risca_w_annualita_sd_01 ON risca_w_annualita_sd USING btree (id_stato_debitorio);


-- risca_w_annualita_uso_sd definition

-- Drop table

-- DROP TABLE risca_w_annualita_uso_sd;

CREATE TABLE risca_w_annualita_uso_sd (
	id_annualita_uso_sd int4 NOT NULL,
	id_annualita_sd int4 NOT NULL,
	id_tipo_uso int4 NOT NULL,
	canone_uso numeric(9, 2) NOT NULL,
	canone_unitario numeric(9, 2) NOT NULL,
	CONSTRAINT pk_risca_w_annualita_uso_sd PRIMARY KEY (id_annualita_uso_sd),
	CONSTRAINT fk_risca_w_annualita_sd_01 FOREIGN KEY (id_annualita_sd) REFERENCES risca_w_annualita_sd(id_annualita_sd)
);
CREATE INDEX ie_risca_w_annualita_uso_sd_01 ON risca_w_annualita_uso_sd USING btree (id_annualita_sd);


-- risca_w_annualita_uso_sd_ra definition

-- Drop table

-- DROP TABLE risca_w_annualita_uso_sd_ra;

CREATE TABLE risca_w_annualita_uso_sd_ra (
	id_annualita_uso_sd_ra int4 NOT NULL,
	id_annualita_uso_sd int4 NOT NULL,
	id_riduzione_aumento int4 NOT NULL,
	CONSTRAINT pk_risca_w_annualita_uso_sd_ra PRIMARY KEY (id_annualita_uso_sd_ra),
	CONSTRAINT fk_risca_d_riduzione_aumento_04 FOREIGN KEY (id_riduzione_aumento) REFERENCES risca_d_riduzione_aumento(id_riduzione_aumento),
	CONSTRAINT fk_risca_w_annualita_uso_sd_01 FOREIGN KEY (id_annualita_uso_sd) REFERENCES risca_w_annualita_uso_sd(id_annualita_uso_sd)
);
CREATE INDEX ie_risca_w_annualita_uso_sd_ra_01 ON risca_w_annualita_uso_sd_ra USING btree (id_annualita_uso_sd, id_riduzione_aumento);


-- risca_w_rp_estrco definition

-- Drop table

-- DROP TABLE risca_w_rp_estrco;

CREATE TABLE risca_w_rp_estrco (
	id_elabora int4 NOT NULL,
	numero_progressivo numeric(6) NULL,
	tipo_record bpchar(2) NULL,
	mittente varchar(5) NULL,
	ricevente varchar(5) NULL,
	data_creazione bpchar(6) NULL,
	nome_supporto varchar(20) NULL,
	centro_applicativo bpchar(5) NULL,
	numero_rendicontazioni numeric(6) NULL,
	numero_record numeric(6) NULL,
	giornata_applicativa numeric(6) NULL,
	codice_abi varchar(5) NULL,
	causale varchar(5) NULL,
	descrizione varchar(16) NULL,
	tipo_conto bpchar(2) NULL,
	coordinate_bancarie varchar(23) NULL,
	codice_divisa varchar(3) NULL,
	data_contabile varchar(6) NULL,
	segno bpchar(1) NULL,
	saldo_iniziale_quad numeric NULL,
	data_valuta varchar(6) NULL,
	data_registrazione varchar(6) NULL,
	segno_movimento bpchar(1) NULL,
	importo_movimento numeric(13, 2) NULL,
	causale_abi bpchar(2) NULL,
	causale_interna bpchar(2) NULL,
	numero_assegno varchar(16) NULL,
	riferimento_banca varchar(16) NULL,
	riferimento_cliente varchar(9) NULL,
	descrizione_movimento varchar(1000) NULL,
	cin bpchar(1) NULL,
	abi bpchar(5) NULL,
	cab bpchar(5) NULL,
	numero_cc varchar(12) NULL,
	id_file_poste numeric(7) NULL,
	progressivo_movimento numeric(7) NULL,
	prog_riga numeric(7) NULL,
	flg_validita bpchar(1) NULL,
	CONSTRAINT fk_risca_t_elabora_10 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora)
);


-- risca_w_rp_nonpremarcati definition

-- Drop table

-- DROP TABLE risca_w_rp_nonpremarcati;

CREATE TABLE risca_w_rp_nonpremarcati (
	id_elabora int4 NOT NULL,
	progr numeric(6) NULL,
	identif_inizio_flusso bpchar(2) NULL,
	data_rif_dati bpchar(8) NULL,
	numero_conto varchar(12) NULL,
	cuas bpchar(1) NULL,
	valuta bpchar(1) NULL,
	importo numeric(13, 2) NULL,
	data_acc bpchar(8) NULL,
	data_all bpchar(8) NULL,
	fraz_uff varchar(6) NULL,
	tipo_doc bpchar(3) NULL,
	quinto_campo varchar(16) NULL,
	numero_avviso bpchar(7) NULL,
	iden_imm varchar(50) NULL,
	vcy bpchar(4) NULL,
	identif bpchar(2) NULL,
	num_vers numeric(9) NULL,
	id_file_poste numeric(7) NULL,
	id_immagine varchar(100) NULL,
	flg_elaborato numeric(1) DEFAULT 1 NULL,
	prog_riga numeric(6) NULL,
	flg_validita bpchar(1) NULL,
	file_immagine varchar(150) NULL, -- Nome del file da cui caricare l'immagine del bollettino
	CONSTRAINT fk_risca_t_elabora_11 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora)
);

-- Column comments

COMMENT ON COLUMN risca_w_rp_nonpremarcati.file_immagine IS 'Nome del file da cui caricare l''immagine del bollettino';


-- risca_w_rp_pagopa definition

-- Drop table

-- DROP TABLE risca_w_rp_pagopa;

CREATE TABLE risca_w_rp_pagopa (
	id_elabora int4 NOT NULL,
	posiz_debitoria varchar(50) NULL,
	anno numeric(4) NULL,
	iuv varchar(35) NOT NULL,
	importo_pagato numeric(12, 2) NULL,
	data_scadenza date NULL,
	causale varchar(140) NULL,
	data_esito_pagamento date NULL,
	cogn_rsoc_debitore varchar(70) NULL,
	nome_debitore varchar(70) NULL,
	cf_pi_debitore varchar(35) NULL,
	cogn_rsoc_versante varchar(70) NULL,
	nome_versante varchar(70) NULL,
	cf_pi_versante varchar(35) NULL,
	importo_transitato numeric(12, 2) NULL,
	importo_commissioni numeric(12, 2) NULL,
	codice_avviso varchar(35) NULL,
	note varchar(2000) NULL,
	CONSTRAINT fk_risca_t_elabora_09 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora)
);
CREATE INDEX ie_risca_w_rp_pagopa_01 ON risca_w_rp_pagopa USING btree (id_elabora);
CREATE INDEX ie_risca_w_rp_pagopa_02 ON risca_w_rp_pagopa USING btree (iuv);
CREATE INDEX ie_risca_w_rp_pagopa_03 ON risca_w_rp_pagopa USING btree (posiz_debitoria);


-- risca_w_spedizione definition

-- Drop table

-- DROP TABLE risca_w_spedizione;

CREATE TABLE risca_w_spedizione (
	id_spedizione int4 NOT NULL,
	id_ambito int4 NOT NULL,
	id_tipo_spedizione int4 NOT NULL,
	data_spedizione timestamp NULL,
	data_protocollo date NULL,
	num_protocollo varchar(30) NULL,
	data_scadenza date NULL,
	num_determina varchar(25) NULL,
	data_determina date NULL,
	anno numeric(4) NULL,
	id_elabora int4 NOT NULL, -- Identificativo dell'elaborazione che ha generato la Spedizione
	CONSTRAINT pk_risca_w_spedizione PRIMARY KEY (id_spedizione),
	CONSTRAINT fk_risca_d_ambito_16 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_tipo_spedizione_02 FOREIGN KEY (id_tipo_spedizione) REFERENCES risca_d_tipo_spedizione(id_tipo_spedizione),
	CONSTRAINT fk_risca_t_elabora_05 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora)
);
CREATE INDEX ie_risca_w_spedizione_01 ON risca_w_spedizione USING btree (id_ambito, id_tipo_spedizione);
CREATE INDEX ie_risca_w_spedizione_02 ON risca_w_spedizione USING btree (id_elabora);

-- Column comments

COMMENT ON COLUMN risca_w_spedizione.id_elabora IS 'Identificativo dell''elaborazione che ha generato la Spedizione';


-- risca_w_stato_debitorio_upd definition

-- Drop table

-- DROP TABLE risca_w_stato_debitorio_upd;

CREATE TABLE risca_w_stato_debitorio_upd (
	id_elabora int4 NOT NULL,
	id_stato_debitorio int4 NOT NULL,
	id_riscossione int4 NOT NULL,
	id_soggetto int4 NOT NULL,
	id_gruppo_soggetto int4 NULL,
	id_recapito int4 NOT NULL,
	id_attivita_stato_deb int4 NULL,
	id_stato_contribuzione int4 NULL,
	id_tipo_dilazione int4 NULL,
	nap varchar(20) NULL,
	num_titolo varchar(20) NULL,
	data_provvedimento date NULL,
	num_richiesta_protocollo varchar(15) NULL,
	data_richiesta_protocollo date NULL,
	data_ultima_modifica date NULL,
	des_usi varchar(250) NULL,
	note varchar(500) NULL,
	imp_recupero_canone numeric(9, 2) NULL,
	imp_recupero_interessi numeric(9, 2) NULL,
	imp_spese_notifica numeric(9, 2) NULL,
	imp_compensazione_canone numeric(9, 2) NULL,
	desc_periodo_pagamento varchar(30) NULL,
	desc_motivo_annullo varchar(200) NULL,
	flg_annullato numeric(1) DEFAULT 0 NOT NULL,
	flg_restituito_mittente numeric(1) DEFAULT 0 NOT NULL,
	flg_invio_speciale numeric(1) DEFAULT 0 NOT NULL,
	flg_dilazione numeric(1) DEFAULT 0 NOT NULL,
	flg_addebito_anno_successivo numeric(1) DEFAULT 0 NOT NULL,
	nota_rinnovo varchar(100) NULL,
	des_tipo_titolo varchar(100) NULL,
	CONSTRAINT chk_risca_w_stato_debitorio_upd_01 CHECK ((flg_annullato = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_w_stato_debitorio_upd_02 CHECK ((flg_restituito_mittente = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_w_stato_debitorio_upd_03 CHECK ((flg_invio_speciale = ANY (ARRAY[(0)::numeric, (1)::numeric, (2)::numeric]))),
	CONSTRAINT chk_risca_w_stato_debitorio_upd_04 CHECK ((flg_dilazione = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_w_stato_debitorio_upd_05 CHECK ((flg_addebito_anno_successivo = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_w_stato_debitorio_upd PRIMARY KEY (id_elabora, id_stato_debitorio),
	CONSTRAINT fk_risca_t_elabora_06 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora)
);
CREATE INDEX ie_risca_w_stato_debitorio_upd_01 ON risca_w_stato_debitorio_upd USING btree (id_riscossione, id_stato_debitorio);
CREATE INDEX ie_risca_w_stato_debitorio_upd_02 ON risca_w_stato_debitorio_upd USING btree (id_soggetto, id_gruppo_soggetto, id_recapito);
CREATE INDEX ie_risca_w_stato_debitorio_upd_03 ON risca_w_stato_debitorio_upd USING btree (nap);


-- risca_d_output_file definition

-- Drop table

-- DROP TABLE risca_d_output_file;

CREATE TABLE risca_d_output_file (
	id_output_file int4 NOT NULL,
	id_tipo_elabora int4 NOT NULL,
	id_tipo_passo_elabora int4 NOT NULL,
	nome_file varchar(255) NULL,
	tipo_file varchar(20) NULL,
	cod_report varchar(15) NULL, -- Codice univoco che identifica il template del report prodotto da Papyrus¶¶Es.¶ambito ambiente  : RPAMBS0101¶ambito opere     : RPOPBS0101¶ambito tributi   : RPTRBS0101
	des_report varchar(250) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_output_file PRIMARY KEY (id_output_file),
	CONSTRAINT fk_risca_d_tipo_elabora_02 FOREIGN KEY (id_tipo_elabora) REFERENCES risca_d_tipo_elabora(id_tipo_elabora),
	CONSTRAINT fk_risca_d_tipo_passo_elabora_02 FOREIGN KEY (id_tipo_passo_elabora) REFERENCES risca_d_tipo_passo_elabora(id_tipo_passo_elabora)
);
CREATE UNIQUE INDEX ak_risca_d_output_file_01 ON risca_d_output_file USING btree (cod_report);

-- Column comments

COMMENT ON COLUMN risca_d_output_file.cod_report IS 'Codice univoco che identifica il template del report prodotto da Papyrus

Es.
ambito ambiente  : RPAMBS0101
ambito opere     : RPOPBS0101
ambito tributi   : RPTRBS0101';


-- risca_d_output_foglio definition

-- Drop table

-- DROP TABLE risca_d_output_foglio;

CREATE TABLE risca_d_output_foglio (
	id_output_foglio int4 NOT NULL,
	id_output_file int4 NOT NULL,
	nome_foglio varchar(255) NULL,
	numero_campi numeric(3) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_output_foglio PRIMARY KEY (id_output_foglio),
	CONSTRAINT fk_risca_d_output_file_01 FOREIGN KEY (id_output_file) REFERENCES risca_d_output_file(id_output_file)
);


-- risca_d_provincia definition

-- Drop table

-- DROP TABLE risca_d_provincia;

CREATE TABLE risca_d_provincia (
	id_provincia numeric(7) NOT NULL,
	cod_provincia varchar(3) NOT NULL,
	denom_provincia varchar(100) NOT NULL,
	sigla_provincia varchar(2) NULL,
	id_regione numeric(6) NOT NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	CONSTRAINT pk_risca_d_provincia PRIMARY KEY (id_provincia),
	CONSTRAINT fk_risca_d_regione_02 FOREIGN KEY (id_regione) REFERENCES risca_d_regione(id_regione)
);
CREATE UNIQUE INDEX ak_risca_d_provincia_01 ON risca_d_provincia USING btree (cod_provincia, id_regione);
CREATE UNIQUE INDEX ak_risca_d_provincia_02 ON risca_d_provincia USING btree (denom_provincia, id_regione, data_inizio_validita);
CREATE UNIQUE INDEX ak_risca_d_provincia_03 ON risca_d_provincia USING btree (id_regione, sigla_provincia, data_inizio_validita);


-- risca_d_sigla_personale definition

-- Drop table

-- DROP TABLE risca_d_sigla_personale;

CREATE TABLE risca_d_sigla_personale (
	id_sigla_personale int4 NOT NULL, -- Identificativo univoco
	id_tipo_riscossione int4 NOT NULL,
	id_tipo_personale int4 NOT NULL,
	cod_sigla_personale varchar(20) NOT NULL,
	des_sigla_personale varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_sigla_personale PRIMARY KEY (id_sigla_personale),
	CONSTRAINT fk_risca_d_tipo_personale_01 FOREIGN KEY (id_tipo_personale) REFERENCES risca_d_tipo_personale(id_tipo_personale),
	CONSTRAINT fk_risca_d_tipo_riscossione_01 FOREIGN KEY (id_tipo_riscossione) REFERENCES risca_d_tipo_riscossione(id_tipo_riscossione)
);
CREATE UNIQUE INDEX ak_risca_d_sigla_personale_01 ON risca_d_sigla_personale USING btree (cod_sigla_personale, id_tipo_riscossione);
COMMENT ON TABLE risca_d_sigla_personale IS 'Permette di definire la sigla nei casi in cui è richiesta nel calcolo del codice della riscossione E'' richiesto solo in alcuni ambiti (per ora in ''Opere Pubbliche'').';

-- Column comments

COMMENT ON COLUMN risca_d_sigla_personale.id_sigla_personale IS 'Identificativo univoco';


-- risca_r_ambito_provincia definition

-- Drop table

-- DROP TABLE risca_r_ambito_provincia;

CREATE TABLE risca_r_ambito_provincia (
	id_ambito int4 NOT NULL,
	id_provincia numeric(7) NOT NULL,
	lettera_provincia varchar(1) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_ambito_provincia PRIMARY KEY (id_ambito, id_provincia),
	CONSTRAINT fk_risca_d_ambito_07 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_provincia_04 FOREIGN KEY (id_provincia) REFERENCES risca_d_provincia(id_provincia)
);


-- risca_r_iuv_da_inviare definition

-- Drop table

-- DROP TABLE risca_r_iuv_da_inviare;

CREATE TABLE risca_r_iuv_da_inviare (
	id_iuv_da_inviare int4 NOT NULL, -- Identificativo univoco
	id_iuv int4 NOT NULL,
	id_elabora int4 NOT NULL,
	id_lotto int4 NULL,
	flg_da_inviare numeric(1) DEFAULT 0 NOT NULL, -- 0= inserito,non da inviare¶1= da inviare
	canone_dovuto numeric(13, 2) NOT NULL,
	imp_versato numeric(13, 2) NOT NULL,
	data_scad_pag date NOT NULL,
	motivazione varchar(200) NULL,
	importo_new numeric(13, 2) NULL,
	interessi numeric(13, 2) NOT NULL,
	interes_rit_pag numeric(13, 2) NOT NULL,
	tot_spese_notif_per_nap numeric(15, 2) NOT NULL,
	ind_tipo_aggiornamento varchar(1) NULL, -- identifica se lo IUV è oggetto di modifica o deve essere annullato.¶A - Annullato¶M - Modificato
	flg_sd_annullato numeric(1) DEFAULT 0 NOT NULL, -- Indica se lo IUV può considerato da annullare¶0 - almeno uno stato debitorio valido (non annullato)¶1 - tutti gli stati debitori sono annullati
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_r_iuv_da_inviare_01 CHECK ((flg_da_inviare = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_r_iuv_da_inviare_03 CHECK ((flg_sd_annullato = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_r_iuv_da_inviare PRIMARY KEY (id_iuv_da_inviare),
	CONSTRAINT fk_risca_t_elabora_14 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora),
	CONSTRAINT fk_risca_t_iuv_01 FOREIGN KEY (id_iuv) REFERENCES risca_t_iuv(id_iuv),
	CONSTRAINT fk_risca_t_lotto_03 FOREIGN KEY (id_lotto) REFERENCES risca_t_lotto(id_lotto)
);
CREATE INDEX ie_risca_r_iuv_da_inviare_01 ON risca_r_iuv_da_inviare USING btree (id_elabora, id_iuv);
COMMENT ON TABLE risca_r_iuv_da_inviare IS 'Raccoglie la lista degli IUV che sono stati oggetto di modifica e che necessitano di elaborazione per comunicazione aggiornamenti a PagoPa';

-- Column comments

COMMENT ON COLUMN risca_r_iuv_da_inviare.id_iuv_da_inviare IS 'Identificativo univoco';
COMMENT ON COLUMN risca_r_iuv_da_inviare.flg_da_inviare IS '0= inserito,non da inviare
1= da inviare';
COMMENT ON COLUMN risca_r_iuv_da_inviare.ind_tipo_aggiornamento IS 'identifica se lo IUV è oggetto di modifica o deve essere annullato.
A - Annullato
M - Modificato';
COMMENT ON COLUMN risca_r_iuv_da_inviare.flg_sd_annullato IS 'Indica se lo IUV può considerato da annullare
0 - almeno uno stato debitorio valido (non annullato)
1 - tutti gli stati debitori sono annullati';


-- risca_r_pag_non_propri definition

-- Drop table

-- DROP TABLE risca_r_pag_non_propri;

CREATE TABLE risca_r_pag_non_propri (
	id_pag_non_propri int4 NOT NULL,
	id_pagamento int4 NOT NULL,
	id_tipo_imp_non_propri int4 NOT NULL,
	importo_versato numeric(13, 2) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_pag_non_propri PRIMARY KEY (id_pag_non_propri),
	CONSTRAINT fk_risca_d_tipo_imp_non_propri_01 FOREIGN KEY (id_tipo_imp_non_propri) REFERENCES risca_d_tipo_imp_non_propri(id_tipo_imp_non_propri),
	CONSTRAINT fk_risca_t_pagamento_02 FOREIGN KEY (id_pagamento) REFERENCES risca_t_pagamento(id_pagamento)
);
CREATE INDEX ie_risca_r_pag_non_propri_01 ON risca_r_pag_non_propri USING btree (id_pagamento);
CREATE INDEX ie_risca_r_pag_non_propri_02 ON risca_r_pag_non_propri USING btree (id_tipo_imp_non_propri);


-- risca_r_pagopa_lista_car_iuv definition

-- Drop table

-- DROP TABLE risca_r_pagopa_lista_car_iuv;

CREATE TABLE risca_r_pagopa_lista_car_iuv (
	id_pagopa_lista_car_iuv int4 NOT NULL, -- Identificativo univoco
	id_lotto int4 NOT NULL,
	nap varchar(20) NOT NULL,
	anno varchar(4) NOT NULL,
	importo numeric(13, 2) NOT NULL,
	data_scadenza date NOT NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NOT NULL,
	causale varchar(100) NOT NULL,
	tipo_soggetto varchar(1) NOT NULL,
	cod_fiscale varchar(16) NOT NULL,
	ragione_sociale varchar(70) NULL,
	cognome varchar(70) NULL,
	nome varchar(70) NULL,
	note varchar(200) NOT NULL,
	cod_esito_da_pagopa varchar(3) NULL,
	desc_esito_da_pagopa varchar(200) NULL,
	codice_avviso varchar(35) NULL,
	iuv varchar(35) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_pagopa_lista_car_iuv PRIMARY KEY (id_pagopa_lista_car_iuv),
	CONSTRAINT fk_risca_t_lotto_01 FOREIGN KEY (id_lotto) REFERENCES risca_t_lotto(id_lotto)
);
CREATE INDEX ie_risca_r_pagopa_lista_car_iuv_01 ON risca_r_pagopa_lista_car_iuv USING btree (nap);
COMMENT ON TABLE risca_r_pagopa_lista_car_iuv IS 'Permette di memorizzare le richieste di elaborazione di procedure applicative.';

-- Column comments

COMMENT ON COLUMN risca_r_pagopa_lista_car_iuv.id_pagopa_lista_car_iuv IS 'Identificativo univoco';


-- risca_r_pagopa_lista_carico definition

-- Drop table

-- DROP TABLE risca_r_pagopa_lista_carico;

CREATE TABLE risca_r_pagopa_lista_carico (
	id_pagopa_lista_carico int4 NOT NULL, -- Identificativo univoco
	id_lotto int4 NOT NULL,
	nap varchar(20) NOT NULL,
	ind_tipo_aggiornamento varchar(1) NULL, -- identifica la tipologia di variazione dello IUV.¶Es.¶A - Annulllamento IUV¶M - Modifica IUV
	motivazione varchar(200) NULL,
	importo_new numeric(13, 2) NOT NULL,
	cod_esito_da_pagopa varchar(3) NULL,
	desc_esito_da_pagopa varchar(500) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_pagopa_lista_carico PRIMARY KEY (id_pagopa_lista_carico),
	CONSTRAINT fk_risca_t_lotto_05 FOREIGN KEY (id_lotto) REFERENCES risca_t_lotto(id_lotto)
);
CREATE INDEX ie_risca_r_pagopa_lista_carico_01 ON risca_r_pagopa_lista_carico USING btree (nap);
COMMENT ON TABLE risca_r_pagopa_lista_carico IS 'Permette di identificare i dati degli IUV oggetto di comunicazione verso PagoPa';

-- Column comments

COMMENT ON COLUMN risca_r_pagopa_lista_carico.id_pagopa_lista_carico IS 'Identificativo univoco';
COMMENT ON COLUMN risca_r_pagopa_lista_carico.ind_tipo_aggiornamento IS 'identifica la tipologia di variazione dello IUV.
Es.
A - Annulllamento IUV
M - Modifica IUV';


-- risca_r_pagopa_scomp_rich_iuv definition

-- Drop table

-- DROP TABLE risca_r_pagopa_scomp_rich_iuv;

CREATE TABLE risca_r_pagopa_scomp_rich_iuv (
	id_pagopa_scomp_rich_iuv int4 NOT NULL, -- Identificativo univoco
	id_lotto int4 NOT NULL,
	nap varchar(20) NOT NULL,
	id_bil_acc int4 NULL, -- ex fk_bil_acc in gerica
	importo_per_nap numeric(13, 2) NULL,
	cod_bil_acc varchar(20) NULL,
	anno varchar(4) NULL,
	dati_spec_risc varchar(140) NULL,
	importo_per_acc numeric(11, 2) NULL,
	id_tipo_bil_acc int4 NULL, -- ex fk_tipo_bil_acc in gerica
	note_backoffice varchar(2000) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_pagopa_scomp_rich_iuv PRIMARY KEY (id_pagopa_scomp_rich_iuv),
	CONSTRAINT fk_risca_t_lotto_02 FOREIGN KEY (id_lotto) REFERENCES risca_t_lotto(id_lotto)
);

-- Column comments

COMMENT ON COLUMN risca_r_pagopa_scomp_rich_iuv.id_pagopa_scomp_rich_iuv IS 'Identificativo univoco';
COMMENT ON COLUMN risca_r_pagopa_scomp_rich_iuv.id_bil_acc IS 'ex fk_bil_acc in gerica';
COMMENT ON COLUMN risca_r_pagopa_scomp_rich_iuv.id_tipo_bil_acc IS 'ex fk_tipo_bil_acc in gerica';


-- risca_r_pagopa_scomp_var_iuv definition

-- Drop table

-- DROP TABLE risca_r_pagopa_scomp_var_iuv;

CREATE TABLE risca_r_pagopa_scomp_var_iuv (
	id_pagopa_scomp_var_iuv int4 NOT NULL, -- Identificativo univoco
	id_lotto int4 NOT NULL,
	nap varchar(20) NOT NULL,
	id_bil_acc int4 NULL,
	importo_per_nap numeric(13, 2) NULL,
	cod_bil_acc varchar(20) NULL,
	anno varchar(4) NULL,
	dati_spec_risc varchar(140) NULL,
	importo_per_acc numeric(11, 2) NULL,
	id_tipo_bil_acc int4 NULL,
	note_backoffice varchar(2000) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_pagopa_scomp_var_iuv PRIMARY KEY (id_pagopa_scomp_var_iuv),
	CONSTRAINT fk_risca_t_lotto_04 FOREIGN KEY (id_lotto) REFERENCES risca_t_lotto(id_lotto)
);
COMMENT ON TABLE risca_r_pagopa_scomp_var_iuv IS 'Permette di raccogliere gli importi degli iuv variati dall''ultima comunicazione';

-- Column comments

COMMENT ON COLUMN risca_r_pagopa_scomp_var_iuv.id_pagopa_scomp_var_iuv IS 'Identificativo univoco';


-- risca_r_parametro_elabora definition

-- Drop table

-- DROP TABLE risca_r_parametro_elabora;

CREATE TABLE risca_r_parametro_elabora (
	id_parametro_elabora int4 NOT NULL,
	id_elabora int4 NOT NULL,
	raggruppamento varchar(10) NOT NULL, -- riferimento alla riga (es. r1,r2,r3) dove non necessaria la molteplicità sarà R1
	chiave varchar(150) NOT NULL, -- identificativo del campo a video su interfaccia
	valore varchar(1000) NOT NULL, -- valore raccolto nel campo
	CONSTRAINT pk_risca_r_parametro_elabora PRIMARY KEY (id_parametro_elabora),
	CONSTRAINT fk_risca_t_elabora_01 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora)
);
COMMENT ON TABLE risca_r_parametro_elabora IS 'Permette di memorizzare i parametri eventualmente richiesti necessari all''elaborazione richiesta';

-- Column comments

COMMENT ON COLUMN risca_r_parametro_elabora.raggruppamento IS 'riferimento alla riga (es. r1,r2,r3) dove non necessaria la molteplicità sarà R1';
COMMENT ON COLUMN risca_r_parametro_elabora.chiave IS 'identificativo del campo a video su interfaccia';
COMMENT ON COLUMN risca_r_parametro_elabora.valore IS 'valore raccolto nel campo';


-- risca_r_registro_elabora definition

-- Drop table

-- DROP TABLE risca_r_registro_elabora;

CREATE TABLE risca_r_registro_elabora (
	id_registro_elabora int4 NOT NULL,
	id_elabora int4 NOT NULL,
	id_passo_elabora int4 NOT NULL,
	flg_esito_elabora numeric(1) NULL, -- Esito del passo elaborativo. Es. 0 - OK 1 - errore  
	nota_elabora varchar(500) NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_uid varchar(64) NULL,
	id_fase_elabora int4 NOT NULL,
	CONSTRAINT chk_risca_r_registro_elabora_01 CHECK ((flg_esito_elabora = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_r_registro_elabora PRIMARY KEY (id_registro_elabora),
	CONSTRAINT fk_risca_d_fase_elabora_01 FOREIGN KEY (id_fase_elabora) REFERENCES risca_d_fase_elabora(id_fase_elabora),
	CONSTRAINT fk_risca_d_passo_elabora_01 FOREIGN KEY (id_passo_elabora) REFERENCES risca_d_passo_elabora(id_passo_elabora),
	CONSTRAINT fk_risca_t_elabora_02 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora)
);
CREATE INDEX ie_risca_r_registro_elabora_01 ON risca_r_registro_elabora USING btree (id_elabora);
CREATE INDEX ie_risca_r_registro_elabora_02 ON risca_r_registro_elabora USING btree (id_passo_elabora, id_fase_elabora);
COMMENT ON TABLE risca_r_registro_elabora IS 'E'' il registro delle elaborazioni eseguite. Permette di memorizzare le azioni eseguite dalle varie fasi  previste di ogni elaborazione rispetto alla richiesta prenotata';

-- Column comments

COMMENT ON COLUMN risca_r_registro_elabora.flg_esito_elabora IS 'Esito del passo elaborativo. Es. 0 - OK 1 - errore  ';


-- risca_r_spedizione_acta definition

-- Drop table

-- DROP TABLE risca_r_spedizione_acta;

CREATE TABLE risca_r_spedizione_acta (
	id_spedizione_acta int4 NOT NULL, -- identificativo univoco spedizione acta
	id_spedizione int4 NOT NULL,
	flg_archiviata_acta numeric(1) DEFAULT 0 NOT NULL, -- 0- da archiviare 1-archiaviato
	nome_dirigente_protempore varchar(100) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	id_elabora int4 NULL,
	CONSTRAINT chk_risca_r_spedizione_acta_01 CHECK ((flg_archiviata_acta = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_r_spedizione_acta PRIMARY KEY (id_spedizione_acta),
	CONSTRAINT fk_risca_t_elabora_08 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora),
	CONSTRAINT fk_risca_t_spedizione_02 FOREIGN KEY (id_spedizione) REFERENCES risca_t_spedizione(id_spedizione)
);
CREATE INDEX ie_risca_r_spedizione_acta_01 ON risca_r_spedizione_acta USING btree (id_spedizione);
CREATE INDEX ie_risca_r_spedizione_acta_02 ON risca_r_spedizione_acta USING btree (id_elabora);

-- Column comments

COMMENT ON COLUMN risca_r_spedizione_acta.id_spedizione_acta IS 'identificativo univoco spedizione acta';
COMMENT ON COLUMN risca_r_spedizione_acta.flg_archiviata_acta IS '0- da archiviare 1-archiaviato';


-- risca_s_provincia definition

-- Drop table

-- DROP TABLE risca_s_provincia;

CREATE TABLE risca_s_provincia (
	id_s_provincia numeric(7) NOT NULL,
	id_provincia numeric(7) NOT NULL,
	cod_provincia varchar(3) NOT NULL,
	denom_provincia varchar(100) NOT NULL,
	sigla_provincia varchar(2) NULL,
	id_regione numeric(6) NOT NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	CONSTRAINT pk_risca_s_provincia PRIMARY KEY (id_s_provincia),
	CONSTRAINT fk_risca_d_provincia_01 FOREIGN KEY (id_provincia) REFERENCES risca_d_provincia(id_provincia),
	CONSTRAINT fk_risca_d_regione_03 FOREIGN KEY (id_regione) REFERENCES risca_d_regione(id_regione)
);
CREATE UNIQUE INDEX ak_risca_s_provincia_01 ON risca_s_provincia USING btree (id_provincia, id_regione, data_inizio_validita);
CREATE UNIQUE INDEX ak_risca_s_provincia_02 ON risca_s_provincia USING btree (cod_provincia, id_regione, data_inizio_validita);
CREATE UNIQUE INDEX ak_risca_s_provincia_03 ON risca_s_provincia USING btree (denom_provincia, id_regione, data_inizio_validita);
CREATE UNIQUE INDEX ak_risca_s_provincia_04 ON risca_s_provincia USING btree (id_regione, sigla_provincia, data_inizio_validita);


-- risca_t_avviso_pagamento definition

-- Drop table

-- DROP TABLE risca_t_avviso_pagamento;

CREATE TABLE risca_t_avviso_pagamento (
	nap varchar(20) NOT NULL, -- NAP (gerica) identificativo NAP univoco di sistema
	id_spedizione int4 NOT NULL,
	prog_nap_avviso_pagamento numeric(5) NULL, -- NUMERO (gerica) utilizato per calcolare il max dell'ultimo progressivo utilizzato
	imp_totale_dovuto numeric(11, 2) NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_t_avviso_pagamento PRIMARY KEY (nap),
	CONSTRAINT fk_risca_t_spedizione_01 FOREIGN KEY (id_spedizione) REFERENCES risca_t_spedizione(id_spedizione)
);
CREATE INDEX ie_risca_t_avviso_pagamento_01 ON risca_t_avviso_pagamento USING btree (id_spedizione);

-- Column comments

COMMENT ON COLUMN risca_t_avviso_pagamento.nap IS 'NAP (gerica) identificativo NAP univoco di sistema';
COMMENT ON COLUMN risca_t_avviso_pagamento.prog_nap_avviso_pagamento IS 'NUMERO (gerica) utilizato per calcolare il max dell''ultimo progressivo utilizzato';


-- risca_w_accertamento definition

-- Drop table

-- DROP TABLE risca_w_accertamento;

CREATE TABLE risca_w_accertamento (
	id_accertamento int4 NOT NULL,
	id_stato_debitorio int4 NOT NULL,
	id_tipo_accertamento int4 NOT NULL,
	id_file_450 int4 NULL,
	id_elabora int4 NOT NULL,
	num_protocollo varchar(30) NULL,
	data_protocollo date NULL,
	data_scadenza date NULL,
	flg_restituito numeric(1) DEFAULT 0 NOT NULL,
	flg_annullato numeric(1) DEFAULT 0 NOT NULL,
	data_notifica date NULL,
	nota varchar(250) NULL,
	id_spedizione int4 NULL,
	CONSTRAINT chk_risca_t_accertamento_01 CHECK ((flg_restituito = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_t_accertamento_02 CHECK ((flg_annullato = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_w_accertamento PRIMARY KEY (id_accertamento),
	CONSTRAINT fk_risca_d_tipo_accertamento_03 FOREIGN KEY (id_tipo_accertamento) REFERENCES risca_d_tipo_accertamento(id_tipo_accertamento),
	CONSTRAINT fk_risca_t_elabora_13 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora),
	CONSTRAINT fk_risca_t_file_450_02 FOREIGN KEY (id_file_450) REFERENCES risca_t_file_450(id_file_450),
	CONSTRAINT fk_risca_w_spedizione_02 FOREIGN KEY (id_spedizione) REFERENCES risca_w_spedizione(id_spedizione)
);


-- risca_w_avviso_pagamento definition

-- Drop table

-- DROP TABLE risca_w_avviso_pagamento;

CREATE TABLE risca_w_avviso_pagamento (
	nap varchar(20) NOT NULL, -- Identificativo univoco
	id_spedizione int4 NOT NULL,
	prog_nap_avviso_pagamento numeric(5) NULL, -- utilizato per calcolare il max dell'ultimo progressivo utilizzato in risca : prog_nap_avviso_pagamento
	imp_totale_dovuto numeric(11, 2) NULL,
	CONSTRAINT pk_risca_w_avviso_pagamento PRIMARY KEY (nap),
	CONSTRAINT fk_risca_w_spedizione_01 FOREIGN KEY (id_spedizione) REFERENCES risca_w_spedizione(id_spedizione)
);
CREATE INDEX ie_risca_w_avviso_pagamento_01 ON risca_w_avviso_pagamento USING btree (id_spedizione);

-- Column comments

COMMENT ON COLUMN risca_w_avviso_pagamento.nap IS 'Identificativo univoco';
COMMENT ON COLUMN risca_w_avviso_pagamento.prog_nap_avviso_pagamento IS 'utilizato per calcolare il max dell''ultimo progressivo utilizzato in risca : prog_nap_avviso_pagamento';


-- risca_w_output_dati definition

-- Drop table

-- DROP TABLE risca_w_output_dati;

CREATE TABLE risca_w_output_dati (
	id_elabora int4 NOT NULL,
	progressivo numeric(6) NOT NULL,
	id_output_foglio int4 NOT NULL,
	id_tipo_passo_elabora int4 NULL,
	valore_colonna1 varchar(500) NULL,
	valore_colonna2 varchar(255) NULL,
	valore_colonna3 varchar(255) NULL,
	valore_colonna4 varchar(255) NULL,
	valore_colonna5 varchar(255) NULL,
	valore_colonna6 varchar(255) NULL,
	valore_colonna7 varchar(255) NULL,
	valore_colonna8 varchar(255) NULL,
	valore_colonna9 varchar(255) NULL,
	valore_colonna10 varchar(255) NULL,
	valore_colonna11 varchar(255) NULL,
	valore_colonna12 varchar(255) NULL,
	valore_colonna13 varchar(255) NULL,
	valore_colonna14 varchar(255) NULL,
	valore_colonna15 varchar(255) NULL,
	valore_colonna16 varchar(255) NULL,
	valore_colonna17 varchar(255) NULL,
	valore_colonna18 varchar(255) NULL,
	valore_colonna19 varchar(255) NULL,
	valore_colonna20 varchar(255) NULL,
	valore_colonna21 varchar(255) NULL,
	valore_colonna22 varchar(255) NULL,
	valore_colonna23 varchar(255) NULL,
	valore_colonna24 varchar(255) NULL,
	valore_colonna25 varchar(255) NULL,
	valore_colonna26 varchar(255) NULL,
	valore_colonna27 varchar(255) NULL,
	valore_colonna28 varchar(255) NULL,
	valore_colonna29 varchar(255) NULL,
	valore_colonna30 varchar(255) NULL,
	valore_colonna31 varchar(255) NULL,
	valore_colonna32 varchar(255) NULL,
	valore_colonna33 varchar(255) NULL,
	valore_colonna34 varchar(255) NULL,
	valore_colonna35 varchar(255) NULL,
	valore_colonna36 varchar(255) NULL,
	valore_colonna37 varchar(255) NULL,
	valore_colonna38 varchar(255) NULL,
	valore_colonna39 varchar(255) NULL,
	valore_colonna40 varchar(255) NULL,
	valore_colonna41 varchar(255) NULL,
	valore_colonna42 varchar(255) NULL,
	valore_colonna43 varchar(255) NULL,
	valore_colonna44 varchar(255) NULL,
	valore_colonna45 varchar(255) NULL,
	valore_colonna46 varchar(255) NULL,
	valore_colonna47 varchar(255) NULL,
	valore_colonna48 varchar(255) NULL,
	valore_colonna49 varchar(255) NULL,
	valore_colonna50 varchar(255) NULL,
	valore_colonna51 varchar(255) NULL,
	valore_colonna52 varchar(255) NULL,
	valore_colonna53 varchar(255) NULL,
	valore_colonna54 varchar(255) NULL,
	valore_colonna55 varchar(255) NULL,
	valore_colonna56 varchar(255) NULL,
	valore_colonna57 varchar(255) NULL,
	valore_colonna58 varchar(255) NULL,
	valore_colonna59 varchar(255) NULL,
	valore_colonna60 varchar(255) NULL,
	valore_colonna61 varchar(255) NULL,
	valore_colonna62 varchar(255) NULL,
	valore_colonna63 varchar(255) NULL,
	valore_colonna64 varchar(255) NULL,
	valore_colonna65 varchar(255) NULL,
	valore_colonna66 varchar(255) NULL,
	valore_colonna67 varchar(255) NULL,
	valore_colonna68 varchar(255) NULL,
	valore_colonna69 varchar(255) NULL,
	valore_colonna70 varchar(255) NULL,
	valore_colonna71 varchar(255) NULL,
	valore_colonna72 varchar(255) NULL,
	valore_colonna73 varchar(255) NULL,
	valore_colonna74 varchar(255) NULL,
	valore_colonna75 varchar(255) NULL,
	valore_colonna76 varchar(255) NULL,
	valore_colonna77 varchar(255) NULL,
	valore_colonna78 varchar(255) NULL,
	valore_colonna79 varchar(255) NULL,
	valore_colonna80 varchar(255) NULL,
	valore_colonna81 varchar(255) NULL,
	valore_colonna82 varchar(255) NULL,
	valore_colonna83 varchar(255) NULL,
	valore_colonna84 varchar(255) NULL,
	valore_colonna85 varchar(255) NULL,
	valore_colonna86 varchar(255) NULL,
	valore_colonna87 varchar(255) NULL,
	valore_colonna88 varchar(255) NULL,
	valore_colonna89 varchar(255) NULL,
	valore_colonna90 varchar(255) NULL,
	flg_invio_pec_email numeric(1) DEFAULT 0 NOT NULL, -- identifica i records ancora da inviare = 0, da quelli inviati = 1
	valore_colonna91 varchar(255) NULL,
	valore_colonna92 varchar(255) NULL,
	valore_colonna93 varchar(255) NULL,
	valore_colonna94 varchar(255) NULL,
	valore_colonna95 varchar(255) NULL,
	valore_colonna96 varchar(255) NULL,
	valore_colonna97 varchar(255) NULL,
	valore_colonna98 varchar(255) NULL,
	valore_colonna99 varchar(255) NULL,
	valore_colonna100 varchar(255) NULL,
	CONSTRAINT chk_risca_w_output_dati_01 CHECK ((flg_invio_pec_email = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_w_output_dati PRIMARY KEY (id_elabora, progressivo, id_output_foglio),
	CONSTRAINT fk_risca_d_output_foglio_02 FOREIGN KEY (id_output_foglio) REFERENCES risca_d_output_foglio(id_output_foglio),
	CONSTRAINT fk_risca_d_tipo_passo_elabora_03 FOREIGN KEY (id_tipo_passo_elabora) REFERENCES risca_d_tipo_passo_elabora(id_tipo_passo_elabora),
	CONSTRAINT fk_risca_t_elabora_03 FOREIGN KEY (id_elabora) REFERENCES risca_t_elabora(id_elabora)
);
CREATE INDEX ie_risca_w_output_dati_01 ON risca_w_output_dati USING btree (id_elabora);
CREATE INDEX ie_risca_w_output_dati_02 ON risca_w_output_dati USING btree (id_output_foglio);
CREATE INDEX ie_risca_w_output_dati_03 ON risca_w_output_dati USING btree (id_tipo_passo_elabora);
CREATE INDEX ie_risca_w_output_dati_04 ON risca_w_output_dati USING btree (valore_colonna89);

-- Column comments

COMMENT ON COLUMN risca_w_output_dati.flg_invio_pec_email IS 'identifica i records ancora da inviare = 0, da quelli inviati = 1';


-- risca_w_rimborso_upd definition

-- Drop table

-- DROP TABLE risca_w_rimborso_upd;

CREATE TABLE risca_w_rimborso_upd (
	id_rimborso int4 NOT NULL,
	id_elabora int4 NOT NULL,
	id_stato_debitorio int4 NOT NULL,
	id_tipo_rimborso int4 NULL,
	id_soggetto int4 NOT NULL,
	imp_rimborso numeric(9, 2) NOT NULL,
	causale varchar(300) NOT NULL,
	num_determina varchar(30) NOT NULL,
	data_determina date NOT NULL,
	imp_restituito numeric(9, 2) NULL,
	id_gruppo_soggetto int4 NULL,
	CONSTRAINT pk_risca_w_rimborso_upd PRIMARY KEY (id_rimborso),
	CONSTRAINT fk_risca_w_stato_debitorio_upd_01 FOREIGN KEY (id_elabora,id_stato_debitorio) REFERENCES risca_w_stato_debitorio_upd(id_elabora,id_stato_debitorio)
);
CREATE INDEX ie_risca_w_rimborso_upd_01 ON risca_w_rimborso_upd USING btree (id_stato_debitorio, id_tipo_rimborso);
CREATE INDEX ie_risca_w_rimborso_upd_02 ON risca_w_rimborso_upd USING btree (id_soggetto, id_gruppo_soggetto);
CREATE INDEX ie_risca_w_rimborso_upd_03 ON risca_w_rimborso_upd USING btree (id_elabora);


-- risca_w_soll_dati_amministr definition

-- Drop table

-- DROP TABLE risca_w_soll_dati_amministr;

CREATE TABLE risca_w_soll_dati_amministr (
	id_soll_dati_amministr int4 NOT NULL,
	id_accertamento int4 NOT NULL,
	codice_utente varchar(10) NULL,
	codice_utenza varchar(10) NULL,
	tipo_sollecito varchar(30) NULL,
	tipo_titolo varchar(20) NULL,
	num_titolo varchar(20) NULL,
	data_titolo date NULL,
	corpo_idrico varchar(200) NULL,
	comune_di_presa varchar(200) NULL,
	annualita_pagamento numeric(4) NULL,
	anno_rich_pagamento varchar(30) NULL,
	scadenza_pagamento varchar(20) NULL,
	motivo_soll varchar(100) NULL,
	canone_dovuto numeric(9, 2) NULL,
	importo_versato numeric(9, 2) NULL,
	importo_mancante numeric(9, 2) NULL,
	interessi_mancanti numeric(9, 2) NULL,
	spese_notifica numeric(9, 2) NULL,
	interessi_teorici numeric(9, 2) NULL,
	interessi_versati numeric(9, 2) NULL,
	dilazione varchar(1) NULL,
	codice_avviso varchar(35) NULL,
	CONSTRAINT chk_risca_t_soll_dati_amministr_01 CHECK (((dilazione)::text = ANY (ARRAY[('S'::character varying)::text, ('N'::character varying)::text]))),
	CONSTRAINT pk_risca_w_soll_dati_amministr PRIMARY KEY (id_soll_dati_amministr),
	CONSTRAINT fk_risca_w_accertamento_01 FOREIGN KEY (id_accertamento) REFERENCES risca_w_accertamento(id_accertamento)
);


-- risca_w_soll_dati_pagopa definition

-- Drop table

-- DROP TABLE risca_w_soll_dati_pagopa;

CREATE TABLE risca_w_soll_dati_pagopa (
	id_soll_dati_pagopa int4 NOT NULL,
	id_accertamento int4 NOT NULL,
	codice_utente varchar(10) NULL,
	codice_utenza varchar(10) NULL,
	iuv varchar(35) NULL,
	codice_avviso varchar(35) NULL,
	scadenza_pagamento varchar(20) NULL,
	importo_da_versare numeric(11, 2) NULL,
	nap varchar(20) NULL,
	nome_titolare_ind_post varchar(100) NULL,
	presso_ind_post varchar(150) NULL,
	indirizzo_ind_post varchar(80) NULL,
	cap_ind_post varchar(5) NULL,
	comune_ind_post varchar(50) NULL,
	prov_ind_post varchar(2) NULL,
	pec_email varchar(150) NULL,
	CONSTRAINT pk_risca_w_soll_dati_pagopa PRIMARY KEY (id_soll_dati_pagopa),
	CONSTRAINT fk_risca_w_accertamento_02 FOREIGN KEY (id_accertamento) REFERENCES risca_w_accertamento(id_accertamento)
);


-- risca_w_soll_dati_titolare definition

-- Drop table

-- DROP TABLE risca_w_soll_dati_titolare;

CREATE TABLE risca_w_soll_dati_titolare (
	id_soll_dati_titolare int4 NOT NULL,
	id_accertamento int4 NOT NULL,
	id_tipo_invio int4 NULL,
	nome_titolare_ind_post varchar(150) NULL,
	codice_fiscale_calc varchar(16) NULL,
	codice_fiscale_eti_calc varchar(16) NULL,
	prov_ind_post varchar(2) NULL,
	presso_ind_post varchar(150) NULL,
	indirizzo_ind_post varchar(80) NULL,
	comune_ind_post varchar(50) NULL,
	num_prot varchar(25) NULL,
	data_prot date NULL,
	scadenza_soll varchar(25) NULL,
	pec_email varchar(150) NULL,
	id_titolare varchar(50) NULL,
	id_soggetto int4 NULL,
	CONSTRAINT pk_risca_w_soll_dati_titolare PRIMARY KEY (id_soll_dati_titolare),
	CONSTRAINT fk_risca_d_tipo_invio_03 FOREIGN KEY (id_tipo_invio) REFERENCES risca_d_tipo_invio(id_tipo_invio),
	CONSTRAINT fk_risca_w_accertamento_03 FOREIGN KEY (id_accertamento) REFERENCES risca_w_accertamento(id_accertamento)
);


-- risca_w_soll_destinatari definition

-- Drop table

-- DROP TABLE risca_w_soll_destinatari;

CREATE TABLE risca_w_soll_destinatari (
	id_soll_destinatari int4 NOT NULL,
	id_accertamento int4 NOT NULL,
	codice_utenza varchar(10) NULL,
	scadenza_pagamento varchar(20) NULL,
	codice_utente_tit varchar(20) NOT NULL,
	codice_utente_dest varchar(20) NOT NULL,
	rag_soc_cogn varchar(100) NULL,
	codice_fiscale_calc varchar(16) NULL,
	codice_fiscale_eti_calc varchar(16) NULL,
	presso_ind_post varchar(150) NULL,
	indirizzo_ind_post varchar(80) NULL,
	comune_ind_post varchar(50) NULL,
	prov_ind_post varchar(2) NULL,
	num_prot varchar(25) NULL,
	data_prot date NULL,
	scadenza_soll varchar(25) NULL,
	rag_soc_cogn_ru varchar(50) NULL,
	nome_ru varchar(50) NULL,
	sesso_ru varchar(1) NULL,
	data_nascita_ru date NULL,
	luogo_nascita_ru varchar(50) NULL,
	prov_nascita_ru varchar(2) NULL,
	cap_ru varchar(5) NULL,
	comune_ru varchar(50) NULL,
	prov_ru varchar(2) NULL,
	indirizzo_civ_ru varchar(50) NULL,
	CONSTRAINT pk_risca_w_soll_destinatari PRIMARY KEY (id_soll_destinatari),
	CONSTRAINT fk_risca_w_accertamento_04 FOREIGN KEY (id_accertamento) REFERENCES risca_w_accertamento(id_accertamento)
);


-- risca_w_soll_dett_vers definition

-- Drop table

-- DROP TABLE risca_w_soll_dett_vers;

CREATE TABLE risca_w_soll_dett_vers (
	id_soll_dett_vers int4 NOT NULL,
	id_accertamento int4 NOT NULL,
	codice_utenza varchar(10) NULL,
	scadenza_pagamento varchar(20) NOT NULL,
	importo_versato numeric(9, 2) NULL,
	data_versamento date NULL,
	interessi_maturati numeric(9, 2) NULL,
	giorni_ritardo numeric(6) NULL,
	CONSTRAINT pk_risca_w_soll_dett_vers PRIMARY KEY (id_soll_dett_vers),
	CONSTRAINT fk_risca_w_accertamento_05 FOREIGN KEY (id_accertamento) REFERENCES risca_w_accertamento(id_accertamento)
);


-- risca_d_comune definition

-- Drop table

-- DROP TABLE risca_d_comune;

CREATE TABLE risca_d_comune (
	id_comune numeric(8) NOT NULL,
	cod_istat_comune varchar(6) NULL,
	cod_belfiore_comune varchar(4) NULL,
	denom_comune varchar(100) NOT NULL,
	cap_comune varchar(5) NULL,
	id_provincia numeric(7) NOT NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	dt_id_comune numeric(20) NOT NULL,
	dt_id_comune_prev numeric(20) NULL,
	dt_id_comune_next numeric(20) NULL,
	CONSTRAINT pk_risca_d_comune PRIMARY KEY (id_comune),
	CONSTRAINT fk_risca_d_provincia_02 FOREIGN KEY (id_provincia) REFERENCES risca_d_provincia(id_provincia)
);
CREATE UNIQUE INDEX ak_risca_d_comune_01 ON risca_d_comune USING btree (cod_istat_comune, id_provincia, data_inizio_validita);
CREATE UNIQUE INDEX ak_risca_d_comune_02 ON risca_d_comune USING btree (cod_belfiore_comune, id_provincia, data_inizio_validita);
CREATE UNIQUE INDEX ak_risca_d_comune_03 ON risca_d_comune USING btree (denom_comune, id_provincia, data_inizio_validita);


-- risca_d_output_colonna definition

-- Drop table

-- DROP TABLE risca_d_output_colonna;

CREATE TABLE risca_d_output_colonna (
	id_output_colonna int4 NOT NULL,
	id_output_foglio int4 NOT NULL,
	id_tipo_dato_colonna int4 NOT NULL,
	desc_etichetta varchar(255) NULL,
	progressivo numeric(7) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_d_output_colonna PRIMARY KEY (id_output_colonna, id_output_foglio),
	CONSTRAINT fk_risca_d_output_foglio_01 FOREIGN KEY (id_output_foglio) REFERENCES risca_d_output_foglio(id_output_foglio),
	CONSTRAINT fk_risca_d_tipo_dato_colonna_01 FOREIGN KEY (id_tipo_dato_colonna) REFERENCES risca_d_tipo_dato_colonna(id_tipo_dato_colonna)
);


-- risca_r_avviso_dati_titolare definition

-- Drop table

-- DROP TABLE risca_r_avviso_dati_titolare;

CREATE TABLE risca_r_avviso_dati_titolare (
	nap varchar(20) NOT NULL,
	id_soggetto int4 NULL,
	nome_titolare_ind_post varchar(200) NULL,
	indirizzo_ind_post varchar(80) NULL,
	presso_ind_post varchar(150) NULL,
	comune_ind_post varchar(50) NULL,
	cap_ind_post varchar(5) NULL,
	prov_ind_post varchar(2) NULL,
	importo_da_versare numeric(11, 2) NULL,
	scadenza_pagamento varchar(20) NULL,
	n_utenze numeric(6) NULL,
	annualita_pagamento numeric(6) NULL,
	stati_pagamenti varchar(50) NULL,
	dilazione varchar(20) NULL,
	quinto_campo varchar(20) NULL,
	codice_fiscale_eti_calc varchar(16) NULL,
	codice_fiscale_calc varchar(16) NULL,
	n_avviso_calc varchar(20) NULL,
	frase_stato_pag_calc varchar(20) NULL,
	codice_utenza_calc varchar(20) NULL,
	scadenza_pagamento2 varchar(20) NULL,
	vuoto3 varchar(20) NULL,
	modalita_invio varchar(20) NULL,
	pec_email varchar(100) NULL,
	numero_protocollo_sped varchar(30) NULL,
	data_protocollo_sped date NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_uid varchar(64) NULL,
	id_gruppo_soggetto int4 NULL,
	id_titolare varchar(50) NULL, -- Contiene il riferimento al titolare.¶Se presente un indirizzo alternativo nella riscossione, riporta l'id_soggetto + 'R' + id_recapito¶altrimenti solo l'id_soggetto
	CONSTRAINT pk_risca_r_avviso_dati_titolare PRIMARY KEY (nap),
	CONSTRAINT fk_risca_t_avviso_pagamento_02 FOREIGN KEY (nap) REFERENCES risca_t_avviso_pagamento(nap)
);

-- Column comments

COMMENT ON COLUMN risca_r_avviso_dati_titolare.id_titolare IS 'Contiene il riferimento al titolare.
Se presente un indirizzo alternativo nella riscossione, riporta l''id_soggetto + ''R'' + id_recapito
altrimenti solo l''id_soggetto';


-- risca_r_email_st_punti_valori definition

-- Drop table

-- DROP TABLE risca_r_email_st_punti_valori;

CREATE TABLE risca_r_email_st_punti_valori (
	id_email_st_punti_valori int4 NOT NULL, -- Identificativo univoco
	id_email_standard int4 NOT NULL,
	id_output_colonna int4 NOT NULL,
	id_output_foglio int4 NOT NULL,
	puntamento varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_email_st_punti_valori PRIMARY KEY (id_email_st_punti_valori),
	CONSTRAINT fk_risca_d_email_standard_01 FOREIGN KEY (id_email_standard) REFERENCES risca_d_email_standard(id_email_standard),
	CONSTRAINT fk_risca_d_output_colonna_01 FOREIGN KEY (id_output_colonna,id_output_foglio) REFERENCES risca_d_output_colonna(id_output_colonna,id_output_foglio)
);
COMMENT ON TABLE risca_r_email_st_punti_valori IS 'Permette di definire i puntamenti necessari a recuperare il dato dalla tabella degli output per ogni valore parametrizzato';

-- Column comments

COMMENT ON COLUMN risca_r_email_st_punti_valori.id_email_st_punti_valori IS 'Identificativo univoco';


-- risca_r_email_st_segnaposto definition

-- Drop table

-- DROP TABLE risca_r_email_st_segnaposto;

CREATE TABLE risca_r_email_st_segnaposto (
	id_email_st_segnaposto int4 NOT NULL, -- Identificativo univoco
	id_email_st_punti_valori int4 NOT NULL,
	segnaposto varchar(50) NOT NULL,
	sezione varchar(50) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_email_st_segnaposto PRIMARY KEY (id_email_st_segnaposto),
	CONSTRAINT fk_risca_r_email_st_punti_valori_01 FOREIGN KEY (id_email_st_punti_valori) REFERENCES risca_r_email_st_punti_valori(id_email_st_punti_valori)
);
COMMENT ON TABLE risca_r_email_st_segnaposto IS 'Permette di definire le sezioni relative ad ogni segnaposto';

-- Column comments

COMMENT ON COLUMN risca_r_email_st_segnaposto.id_email_st_segnaposto IS 'Identificativo univoco';


-- risca_s_comune definition

-- Drop table

-- DROP TABLE risca_s_comune;

CREATE TABLE risca_s_comune (
	id_s_comune numeric(8) NOT NULL,
	id_comune numeric(8) NOT NULL,
	cod_istat_comune varchar(6) NULL,
	cod_belfiore_comune varchar(4) NULL,
	denom_comune varchar(100) NOT NULL,
	cap_comune varchar(5) NULL,
	id_provincia numeric(7) NOT NULL,
	data_inizio_validita date NOT NULL,
	data_fine_validita date NULL,
	dt_id_comune numeric(20) NOT NULL,
	dt_id_comune_prev numeric(20) NULL,
	dt_id_comune_next numeric(20) NULL,
	CONSTRAINT pk_risca_s_comune PRIMARY KEY (id_s_comune),
	CONSTRAINT fk_risca_d_comune_01 FOREIGN KEY (id_comune) REFERENCES risca_d_comune(id_comune),
	CONSTRAINT fk_risca_d_provincia_03 FOREIGN KEY (id_provincia) REFERENCES risca_d_provincia(id_provincia)
);
CREATE UNIQUE INDEX ak_risca_s_comune_01 ON risca_s_comune USING btree (id_comune, cod_istat_comune, cod_belfiore_comune, denom_comune, id_provincia, data_inizio_validita);


-- risca_t_soggetto definition

-- Drop table

-- DROP TABLE risca_t_soggetto;

CREATE TABLE risca_t_soggetto (
	id_soggetto int4 NOT NULL, -- Identificativo univoco
	id_ambito int4 NOT NULL,
	id_tipo_soggetto int4 NOT NULL,
	cf_soggetto varchar(16) NOT NULL,
	id_tipo_natura_giuridica int4 NULL,
	id_fonte_origine int4 NULL,
	id_fonte int4 NULL,
	nome varchar(100) NULL,
	cognome varchar(100) NULL,
	den_soggetto varchar(250) NULL,
	partita_iva_soggetto varchar(16) NULL,
	data_nascita_soggetto date NULL,
	id_comune_nascita numeric(8) NULL,
	id_stato_nascita numeric(3) NULL,
	citta_estera_nascita varchar(100) NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_uid varchar(64) NULL,
	data_aggiornamento timestamp NULL,
	data_cancellazione timestamp NULL,
	CONSTRAINT pk_risca_t_soggetto PRIMARY KEY (id_soggetto),
	CONSTRAINT fk_risca_d_ambito_09 FOREIGN KEY (id_ambito) REFERENCES risca_d_ambito(id_ambito),
	CONSTRAINT fk_risca_d_comune_03 FOREIGN KEY (id_comune_nascita) REFERENCES risca_d_comune(id_comune),
	CONSTRAINT fk_risca_d_nazione_04 FOREIGN KEY (id_stato_nascita) REFERENCES risca_d_nazione(id_nazione),
	CONSTRAINT fk_risca_d_tipo_natura_giuridica_01 FOREIGN KEY (id_tipo_natura_giuridica) REFERENCES risca_d_tipo_natura_giuridica(id_tipo_natura_giuridica),
	CONSTRAINT fk_risca_d_tipo_soggetto_01 FOREIGN KEY (id_tipo_soggetto) REFERENCES risca_d_tipo_soggetto(id_tipo_soggetto)
);
CREATE UNIQUE INDEX ak_risca_t_soggetto_01 ON risca_t_soggetto USING btree (id_ambito, id_tipo_soggetto, cf_soggetto);
COMMENT ON TABLE risca_t_soggetto IS 'Anagrafica dei soggetti Titolari e/o appartenenti ad un gruppo di soggetti.  Lo stesso soggetto (identificato dal codice_fiscale) può essere presente in anagrafica per diversi Ambiti o Tipologie (PF,PG,PB)';

-- Column comments

COMMENT ON COLUMN risca_t_soggetto.id_soggetto IS 'Identificativo univoco';


-- risca_w_avviso_dati_titolare definition

-- Drop table

-- DROP TABLE risca_w_avviso_dati_titolare;

CREATE TABLE risca_w_avviso_dati_titolare (
	nap varchar(20) NOT NULL,
	id_soggetto int4 NULL,
	nome_titolare_ind_post varchar(200) NULL,
	indirizzo_ind_post varchar(80) NULL,
	presso_ind_post varchar(150) NULL,
	comune_ind_post varchar(50) NULL,
	cap_ind_post varchar(5) NULL,
	prov_ind_post varchar(2) NULL,
	importo_da_versare numeric(11, 2) NULL,
	scadenza_pagamento varchar(20) NULL,
	n_utenze numeric(6) NULL,
	annualita_pagamento numeric(6) NULL,
	stati_pagamenti varchar(50) NULL,
	dilazione varchar(20) NULL,
	quinto_campo varchar(20) NULL,
	codice_fiscale_eti_calc varchar(16) NULL,
	codice_fiscale_calc varchar(16) NULL,
	n_avviso_calc varchar(20) NULL,
	frase_stato_pag_calc varchar(20) NULL,
	codice_utenza_calc varchar(20) NULL,
	scadenza_pagamento2 varchar(20) NULL,
	vuoto3 varchar(20) NULL,
	modalita_invio varchar(20) NULL,
	pec_email varchar(100) NULL,
	numero_protocollo_sped varchar(30) NULL,
	data_protocollo_sped date NULL,
	id_gruppo_soggetto int4 NULL,
	id_titolare varchar(50) NULL,
	CONSTRAINT pk_risca_w_avviso_dati_titolare PRIMARY KEY (nap),
	CONSTRAINT fk_risca_w_avviso_pagamento_01 FOREIGN KEY (nap) REFERENCES risca_w_avviso_pagamento(nap)
);
CREATE INDEX ie_risca_w_avviso_dati_titolare_01 ON risca_w_avviso_dati_titolare USING btree (nap);


-- risca_w_rimborso_sd_utilizzato definition

-- Drop table

-- DROP TABLE risca_w_rimborso_sd_utilizzato;

CREATE TABLE risca_w_rimborso_sd_utilizzato (
	id_stato_debitorio int4 NOT NULL,
	id_elabora int4 NOT NULL,
	id_rimborso int4 NOT NULL,
	imp_utilizzato numeric(9, 2) NOT NULL,
	CONSTRAINT pk_risca_w_rimborso_sd_utilizzato PRIMARY KEY (id_stato_debitorio, id_rimborso),
	CONSTRAINT fk_risca_w_rimborso_upd_01 FOREIGN KEY (id_rimborso) REFERENCES risca_w_rimborso_upd(id_rimborso),
	CONSTRAINT fk_risca_w_stato_debitorio_03 FOREIGN KEY (id_stato_debitorio) REFERENCES risca_w_stato_debitorio(id_stato_debitorio)
);
CREATE INDEX ie_risca_w_rimborso_sd_utilizzato_01 ON risca_w_rimborso_sd_utilizzato USING btree (id_elabora);


-- risca_r_recapito definition

-- Drop table

-- DROP TABLE risca_r_recapito;

CREATE TABLE risca_r_recapito (
	id_recapito int4 NOT NULL, -- Identificativo univoco
	id_soggetto int4 NOT NULL,
	id_tipo_recapito int4 NOT NULL,
	id_tipo_invio int4 NULL,
	id_comune_recapito numeric(8) NULL,
	id_nazione_recapito numeric(3) NULL,
	id_fonte int4 NULL,
	id_fonte_origine int4 NULL,
	cod_recapito varchar(20) NOT NULL,
	cod_recapito_fonte varchar(20) NULL,
	indirizzo varchar(100) NULL,
	num_civico varchar(30) NULL,
	email varchar(300) NULL,
	pec varchar(150) NULL,
	telefono varchar(50) NULL,
	presso varchar(150) NULL,
	citta_estera_recapito varchar(100) NULL,
	cap_recapito varchar(10) NULL,
	des_localita varchar(250) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	cellulare varchar(50) NULL,
	id_tipo_sede int4 NULL,
	data_aggiornamento timestamp NULL,
	data_cancellazione timestamp NULL,
	CONSTRAINT pk_risca_r_recapito PRIMARY KEY (id_recapito),
	CONSTRAINT fk_risca_d_comune_02 FOREIGN KEY (id_comune_recapito) REFERENCES risca_d_comune(id_comune),
	CONSTRAINT fk_risca_d_nazione_05 FOREIGN KEY (id_nazione_recapito) REFERENCES risca_d_nazione(id_nazione),
	CONSTRAINT fk_risca_d_tipo_invio_01 FOREIGN KEY (id_tipo_invio) REFERENCES risca_d_tipo_invio(id_tipo_invio),
	CONSTRAINT fk_risca_d_tipo_recapito_01 FOREIGN KEY (id_tipo_recapito) REFERENCES risca_d_tipo_recapito(id_tipo_recapito),
	CONSTRAINT fk_risca_d_tipo_sede_01 FOREIGN KEY (id_tipo_sede) REFERENCES risca_d_tipo_sede(id_tipo_sede),
	CONSTRAINT fk_risca_t_soggetto_03 FOREIGN KEY (id_soggetto) REFERENCES risca_t_soggetto(id_soggetto)
);
CREATE UNIQUE INDEX ak_risca_r_recapito_01 ON risca_r_recapito USING btree (cod_recapito);
CREATE INDEX ie_risca_r_recapito_01 ON risca_r_recapito USING btree (id_soggetto);
COMMENT ON TABLE risca_r_recapito IS 'Permette di censire i recapiti di un soggetto.';

-- Column comments

COMMENT ON COLUMN risca_r_recapito.id_recapito IS 'Identificativo univoco';


-- risca_r_recapito_postel definition

-- Drop table

-- DROP TABLE risca_r_recapito_postel;

CREATE TABLE risca_r_recapito_postel (
	id_recapito_postel int4 NOT NULL,
	id_recapito int4 NOT NULL, -- Identificativo recapito di interesse
	id_gruppo_soggetto int4 NULL, -- Questa informazione deve essere valorizzata solo se il soggetto è il referente di uno o più gruppi 
	destinatario_postel varchar(200) NULL, -- Se è presente un gruppo, il destinatario è il NOME DEL GRUPPO di interesse.
	presso_postel varchar(150) NULL, -- Se non è presente il GRUPPO, il campo PRESSO_POSTEL è valorizzato con il campo PRESSO del recapito di interesse (se valorizzato).
	indirizzo_postel varchar(130) NULL,
	citta_postel varchar(100) NULL,
	provincia_postel varchar(2) NULL,
	cap_postel varchar(10) NULL,
	frazione_postel varchar(250) NULL,
	nazione_postel varchar(100) NULL,
	ind_valido_postel int4 NULL, -- Valorizzato automaticamente dal sistema in fase di inserimento o modifica previa applicazione dei controlli. Se controlli superati IND_VALIDO_POSTEL=1  Es. 0 controllo lunghezza non superato 1 controllo lunghezza superato 2 validato da servizio di postalizzazione 3 non ha superato i controlli del servizio di postalizzazione
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT ak_risca_r_recapito_postel_01 UNIQUE (id_recapito, id_gruppo_soggetto),
	CONSTRAINT pk_risca_r_recapito_postel PRIMARY KEY (id_recapito_postel),
	CONSTRAINT fk_risca_r_recapito_01 FOREIGN KEY (id_recapito) REFERENCES risca_r_recapito(id_recapito),
	CONSTRAINT fk_risca_t_gruppo_soggetto_04 FOREIGN KEY (id_gruppo_soggetto) REFERENCES risca_t_gruppo_soggetto(id_gruppo_soggetto)
);
CREATE UNIQUE INDEX ak_risca_r_recapito_postel_02 ON risca_r_recapito_postel USING btree (id_recapito) WHERE (id_gruppo_soggetto IS NULL);
COMMENT ON TABLE risca_r_recapito_postel IS 'Permette di associare alla riscossione i recapiti del soggetto titolare, necessari alle attività amministrative.';

-- Column comments

COMMENT ON COLUMN risca_r_recapito_postel.id_recapito IS 'Identificativo recapito di interesse';
COMMENT ON COLUMN risca_r_recapito_postel.id_gruppo_soggetto IS 'Questa informazione deve essere valorizzata solo se il soggetto è il referente di uno o più gruppi ';
COMMENT ON COLUMN risca_r_recapito_postel.destinatario_postel IS 'Se è presente un gruppo, il destinatario è il NOME DEL GRUPPO di interesse.';
COMMENT ON COLUMN risca_r_recapito_postel.presso_postel IS 'Se non è presente il GRUPPO, il campo PRESSO_POSTEL è valorizzato con il campo PRESSO del recapito di interesse (se valorizzato).';
COMMENT ON COLUMN risca_r_recapito_postel.ind_valido_postel IS 'Valorizzato automaticamente dal sistema in fase di inserimento o modifica previa applicazione dei controlli. Se controlli superati IND_VALIDO_POSTEL=1  Es. 0 controllo lunghezza non superato 1 controllo lunghezza superato 2 validato da servizio di postalizzazione 3 non ha superato i controlli del servizio di postalizzazione';


-- risca_r_soggetto_delega definition

-- Drop table

-- DROP TABLE risca_r_soggetto_delega;

CREATE TABLE risca_r_soggetto_delega (
	id_soggetto_delega int4 NOT NULL, -- Identificativo univoco
	id_soggetto int4 NOT NULL,
	id_delegato int4 NOT NULL,
	data_abilitazione date NOT NULL,
	data_disabilitazione date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_soggetto_delega PRIMARY KEY (id_soggetto_delega),
	CONSTRAINT fk_risca_t_delegato_01 FOREIGN KEY (id_delegato) REFERENCES risca_t_delegato(id_delegato),
	CONSTRAINT fk_risca_t_soggetto_07 FOREIGN KEY (id_soggetto) REFERENCES risca_t_soggetto(id_soggetto)
);
CREATE UNIQUE INDEX ak_risca_r_soggetto_delega_01 ON risca_r_soggetto_delega USING btree (id_soggetto, id_delegato);
COMMENT ON TABLE risca_r_soggetto_delega IS 'Permette di definire le eventuali figure delegate alla consultazione delle proprie Riscossioni da parte del soggetto';

-- Column comments

COMMENT ON COLUMN risca_r_soggetto_delega.id_soggetto_delega IS 'Identificativo univoco';


-- risca_r_soggetto_gruppo definition

-- Drop table

-- DROP TABLE risca_r_soggetto_gruppo;

CREATE TABLE risca_r_soggetto_gruppo (
	id_soggetto int4 NOT NULL,
	id_gruppo_soggetto int4 NOT NULL,
	flg_capo_gruppo numeric(1) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_r_soggetto_gruppo_01 CHECK ((flg_capo_gruppo = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_r_soggetto_gruppo PRIMARY KEY (id_soggetto, id_gruppo_soggetto),
	CONSTRAINT fk_risca_t_gruppo_soggetto_02 FOREIGN KEY (id_gruppo_soggetto) REFERENCES risca_t_gruppo_soggetto(id_gruppo_soggetto),
	CONSTRAINT fk_risca_t_soggetto_02 FOREIGN KEY (id_soggetto) REFERENCES risca_t_soggetto(id_soggetto)
);
COMMENT ON TABLE risca_r_soggetto_gruppo IS 'Permette di associare un soggetto al relativo gruppo di appartenenza.';


-- risca_t_riscossione definition

-- Drop table

-- DROP TABLE risca_t_riscossione;

CREATE TABLE risca_t_riscossione (
	id_riscossione int4 NOT NULL, -- Identificativo univoco
	id_tipo_riscossione int4 NOT NULL, -- Permette di identificare l'ambito di appartenenza
	id_stato_riscossione int4 NOT NULL,
	id_soggetto int4 NOT NULL,
	id_gruppo_soggetto int4 NULL,
	id_tipo_autorizza int4 NOT NULL,
	cod_riscossione varchar(40) NOT NULL, -- Codice univoco della riscossione, con formato specifico per ogni ambito
	cod_riscossione_prov varchar(2) NULL, -- Sigla della Provincia utilizzata nella generazione del cod_riscossione
	cod_riscossione_prog varchar(5) NULL, -- Progressivo Numerico utilizzato nella generazione del cod_riscossione
	id_sigla_riscossione int4 NULL,
	cod_riscossione_lettera_prov varchar(1) NULL,
	num_pratica varchar(40) NOT NULL, -- Numero della Pratica dell'ufficio Riscossione
	flg_prenotata numeric(1) DEFAULT 0 NOT NULL, -- Indica che la pratica è in carico ad un utente (prenotata con relativo motivo_prenotazione),
	motivo_prenotazione varchar(200) NULL, -- Motivo della prenotazione
	note_riscossione varchar(500) NULL,
	data_ini_concessione date NULL,
	data_scad_concessione date NULL,
	data_ini_sosp_canone date NULL,
	data_fine_sosp_canone date NULL,
	json_dt jsonb NULL, -- Contiene il dato tecnico, strutturato in elementi e raccolto dallo specifico quadro/template
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	id_componente_dt int4 NOT NULL, -- Permette di raccogliere i dati tecnici nel campo json_dt
	id_riscossione_padre int4 NULL,
	data_inizio_titolarita date NOT NULL,
	data_rinuncia_revoca date NULL, -- Valorizzata solo per stato riscossione RINUNCIATA o REVOCATA
	CONSTRAINT ak_risca_t_riscossione_01 UNIQUE (cod_riscossione),
	CONSTRAINT chk_risca_t_riscossione_01 CHECK ((flg_prenotata = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_t_riscossione PRIMARY KEY (id_riscossione),
	CONSTRAINT fk_risca_d_componente_dt_01 FOREIGN KEY (id_componente_dt) REFERENCES risca_d_componente_dt(id_componente_dt),
	CONSTRAINT fk_risca_d_sigla_personale_01 FOREIGN KEY (id_sigla_riscossione) REFERENCES risca_d_sigla_personale(id_sigla_personale),
	CONSTRAINT fk_risca_d_stato_risco_01 FOREIGN KEY (id_stato_riscossione) REFERENCES risca_d_stato_riscossione(id_stato_riscossione),
	CONSTRAINT fk_risca_d_tipo_autorizza_01 FOREIGN KEY (id_tipo_autorizza) REFERENCES risca_d_tipo_autorizza(id_tipo_autorizza),
	CONSTRAINT fk_risca_d_tipo_riscossione_02 FOREIGN KEY (id_tipo_riscossione) REFERENCES risca_d_tipo_riscossione(id_tipo_riscossione),
	CONSTRAINT fk_risca_t_gruppo_soggetto_01 FOREIGN KEY (id_gruppo_soggetto) REFERENCES risca_t_gruppo_soggetto(id_gruppo_soggetto),
	CONSTRAINT fk_risca_t_riscossione_05 FOREIGN KEY (id_riscossione_padre) REFERENCES risca_t_riscossione(id_riscossione),
	CONSTRAINT fk_risca_t_soggetto_01 FOREIGN KEY (id_soggetto) REFERENCES risca_t_soggetto(id_soggetto)
);
CREATE INDEX ie_risca_t_riscossione_01 ON risca_t_riscossione USING btree (id_tipo_riscossione, id_stato_riscossione);
CREATE INDEX ie_risca_t_riscossione_02 ON risca_t_riscossione USING btree (id_soggetto, id_gruppo_soggetto);
COMMENT ON TABLE risca_t_riscossione IS 'Permette di rappresentare la riscossione con i dati amministrativi e tecnici per poi generare il canone . La riscossione è identificata da un codice riscossione univoco, che viene generato con una codifica specifica per ogni ambito. Es. i dati utilizzati sono (cod_riscossione_prov, cod_riscossione_prog,cod_riscossione_lettera_prov, id_sigla_riscossione)  La tipologia di riscossione permette di identificare l''ambito di appartenenza. Il dato tecnico è raccolto in formato JSON nel campo json_dt, tramite un template di riferimento e uno o più quadri per la loro raccolta.  La riscossione può essere inserita direttamente in RISCA, ho essere comunicata da altri sistemi.';

-- Column comments

COMMENT ON COLUMN risca_t_riscossione.id_riscossione IS 'Identificativo univoco';
COMMENT ON COLUMN risca_t_riscossione.id_tipo_riscossione IS 'Permette di identificare l''ambito di appartenenza';
COMMENT ON COLUMN risca_t_riscossione.cod_riscossione IS 'Codice univoco della riscossione, con formato specifico per ogni ambito';
COMMENT ON COLUMN risca_t_riscossione.cod_riscossione_prov IS 'Sigla della Provincia utilizzata nella generazione del cod_riscossione';
COMMENT ON COLUMN risca_t_riscossione.cod_riscossione_prog IS 'Progressivo Numerico utilizzato nella generazione del cod_riscossione';
COMMENT ON COLUMN risca_t_riscossione.num_pratica IS 'Numero della Pratica dell''ufficio Riscossione';
COMMENT ON COLUMN risca_t_riscossione.flg_prenotata IS 'Indica che la pratica è in carico ad un utente (prenotata con relativo motivo_prenotazione)';
COMMENT ON COLUMN risca_t_riscossione.motivo_prenotazione IS 'Motivo della prenotazione';
COMMENT ON COLUMN risca_t_riscossione.json_dt IS 'Contiene il dato tecnico, strutturato in elementi e raccolto dallo specifico quadro/template';
COMMENT ON COLUMN risca_t_riscossione.id_componente_dt IS 'Permette di raccogliere i dati tecnici nel campo json_dt';
COMMENT ON COLUMN risca_t_riscossione.data_rinuncia_revoca IS 'Valorizzata solo per stato riscossione RINUNCIATA o REVOCATA';


-- risca_t_stato_debitorio definition

-- Drop table

-- DROP TABLE risca_t_stato_debitorio;

CREATE TABLE risca_t_stato_debitorio (
	id_stato_debitorio int4 NOT NULL, -- Identificativo univoco
	id_riscossione int4 NOT NULL,
	id_soggetto int4 NOT NULL,
	id_gruppo_soggetto int4 NULL,
	id_recapito int4 NOT NULL,
	id_attivita_stato_deb int4 NULL,
	id_stato_contribuzione int4 NULL,
	id_tipo_dilazione int4 NULL,
	num_titolo varchar(20) NULL,
	data_provvedimento date NULL,
	num_richiesta_protocollo varchar(15) NULL,
	data_richiesta_protocollo date NULL,
	data_ultima_modifica date NULL, -- vecchio campo DATA_MOD
	des_usi varchar(250) NULL,
	note varchar(500) NULL,
	imp_recupero_canone numeric(9, 2) NULL,
	imp_recupero_interessi numeric(9, 2) NULL,
	imp_spese_notifica numeric(9, 2) NULL,
	imp_compensazione_canone numeric(9, 2) NULL, -- verificare come viene calcolato
	desc_periodo_pagamento varchar(30) NULL,
	desc_motivo_annullo varchar(200) NULL,
	flg_annullato numeric(1) DEFAULT 0 NOT NULL, -- identifica se lo stato debitorio è annullato (1)
	flg_restituito_mittente numeric(1) DEFAULT 0 NOT NULL, -- identifica se lo stato debitorio è stato restituito al mittente (1)
	flg_invio_speciale numeric(1) DEFAULT 0 NOT NULL, -- identifica se lo stato debitorio ha necessità di invio speciale a POSTEL (1)
	flg_dilazione numeric(1) DEFAULT 0 NOT NULL, -- identifica se lo stato debitorio ha una a dilazione (1)
	flg_addebito_anno_successivo numeric(1) DEFAULT 0 NOT NULL, -- FLG_ADDE_CAN (Gerica) -  identifica se lo stato debitorio ha un addebito per l'anno sucessivo (1)
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	nap varchar(20) NULL,
	nota_rinnovo varchar(100) NULL,
	des_tipo_titolo varchar(100) NULL,
	CONSTRAINT chk_risca_t_stato_debitorio_01 CHECK ((flg_annullato = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_t_stato_debitorio_02 CHECK ((flg_restituito_mittente = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_t_stato_debitorio_03 CHECK ((flg_invio_speciale = ANY (ARRAY[(0)::numeric, (1)::numeric, (2)::numeric]))),
	CONSTRAINT chk_risca_t_stato_debitorio_04 CHECK ((flg_dilazione = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_t_stato_debitorio_05 CHECK ((flg_addebito_anno_successivo = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_t_stato_debitorio PRIMARY KEY (id_stato_debitorio),
	CONSTRAINT fk_risca_d_attivita_stato_deb_01 FOREIGN KEY (id_attivita_stato_deb) REFERENCES risca_d_attivita_stato_deb(id_attivita_stato_deb),
	CONSTRAINT fk_risca_d_stato_contribuzione_01 FOREIGN KEY (id_stato_contribuzione) REFERENCES risca_d_stato_contribuzione(id_stato_contribuzione),
	CONSTRAINT fk_risca_d_tipo_dilazione_01 FOREIGN KEY (id_tipo_dilazione) REFERENCES risca_d_tipo_dilazione(id_tipo_dilazione),
	CONSTRAINT fk_risca_r_recapito_03 FOREIGN KEY (id_recapito) REFERENCES risca_r_recapito(id_recapito),
	CONSTRAINT fk_risca_t_avviso_pagamento_01 FOREIGN KEY (nap) REFERENCES risca_t_avviso_pagamento(nap),
	CONSTRAINT fk_risca_t_gruppo_soggetto_03 FOREIGN KEY (id_gruppo_soggetto) REFERENCES risca_t_gruppo_soggetto(id_gruppo_soggetto),
	CONSTRAINT fk_risca_t_riscossione_01 FOREIGN KEY (id_riscossione) REFERENCES risca_t_riscossione(id_riscossione),
	CONSTRAINT fk_risca_t_soggetto_04 FOREIGN KEY (id_soggetto) REFERENCES risca_t_soggetto(id_soggetto)
);
CREATE INDEX ie_risca_t_stato_debitorio_01 ON risca_t_stato_debitorio USING btree (id_riscossione);
CREATE INDEX ie_risca_t_stato_debitorio_02 ON risca_t_stato_debitorio USING btree (nap);

-- Column comments

COMMENT ON COLUMN risca_t_stato_debitorio.id_stato_debitorio IS 'Identificativo univoco';
COMMENT ON COLUMN risca_t_stato_debitorio.data_ultima_modifica IS 'vecchio campo DATA_MOD';
COMMENT ON COLUMN risca_t_stato_debitorio.imp_compensazione_canone IS 'verificare come viene calcolato';
COMMENT ON COLUMN risca_t_stato_debitorio.flg_annullato IS 'identifica se lo stato debitorio è annullato (1)';
COMMENT ON COLUMN risca_t_stato_debitorio.flg_restituito_mittente IS 'identifica se lo stato debitorio è stato restituito al mittente (1)';
COMMENT ON COLUMN risca_t_stato_debitorio.flg_invio_speciale IS 'identifica se lo stato debitorio ha necessità di invio speciale a POSTEL (1)';
COMMENT ON COLUMN risca_t_stato_debitorio.flg_dilazione IS 'identifica se lo stato debitorio ha una a dilazione (1)';
COMMENT ON COLUMN risca_t_stato_debitorio.flg_addebito_anno_successivo IS 'FLG_ADDE_CAN (Gerica) -  identifica se lo stato debitorio ha un addebito per l''anno sucessivo (1)';


-- risca_w_avviso_dati_ammin definition

-- Drop table

-- DROP TABLE risca_w_avviso_dati_ammin;

CREATE TABLE risca_w_avviso_dati_ammin (
	nap varchar(20) NOT NULL,
	codice_utenza varchar(20) NOT NULL,
	id_stato_debitorio int4 NULL,
	corpo_idrico varchar(200) NULL,
	comune_di_presa varchar(200) NULL,
	periodo_di_contribuzione varchar(30) NULL,
	numero_protocollo_sped varchar(30) NULL,
	data_protocollo_sped date NULL,
	etichetta21_calc varchar(50) NULL,
	valore21_calc varchar(20) NULL,
	etichetta22_calc varchar(20) NULL,
	valore22_calc varchar(20) NULL,
	totale_utenza_calc varchar(20) NULL,
	scad_conc_eti_calc varchar(30) NULL,
	scad_conc_calc varchar(100) NULL,
	scadenza_concessione_calc varchar(100) NULL,
	provvedimento_calc varchar(150) NULL,
	testo varchar(20) NULL,
	data_scad_emas_iso date NULL,
	imp_compens_canone varchar(20) NULL,
	rec_canone varchar(20) NULL,
	num_pratica varchar(40) NULL,
	descr_utilizzo varchar(100) NULL,
	tot_energ_prod numeric(20, 6) NULL,
	CONSTRAINT pk_risca_w_avviso_dati_ammin PRIMARY KEY (nap, codice_utenza),
	CONSTRAINT fk_risca_w_avviso_dati_titolare_01 FOREIGN KEY (nap) REFERENCES risca_w_avviso_dati_titolare(nap),
	CONSTRAINT fk_risca_w_stato_debitorio_01 FOREIGN KEY (id_stato_debitorio) REFERENCES risca_w_stato_debitorio(id_stato_debitorio)
);
CREATE INDEX ie_risca_w_avviso_dati_ammin_01 ON risca_w_avviso_dati_ammin USING btree (id_stato_debitorio);
CREATE INDEX ie_risca_w_avviso_dati_ammin_02 ON risca_w_avviso_dati_ammin USING btree (nap);


-- risca_r_annualita_sd definition

-- Drop table

-- DROP TABLE risca_r_annualita_sd;

CREATE TABLE risca_r_annualita_sd (
	id_annualita_sd int4 NOT NULL, -- Identificativo univoco
	id_stato_debitorio int4 NOT NULL,
	anno numeric(4) NOT NULL,
	json_dt_riscossione jsonb NOT NULL, -- Permette di memorizzare i DT dello stato debitorio.¶Viene generato da una copia del json_dt presente nella riscossione è può in seguito essere modificato.¶¶Non ha un suo componente specifico SW, ma fà riferimento (risca_d_tipo_componente_dt) a quello definito per la riscossione (GESTIONE-Componente di Gestione)¶Contiene tutti i campi definiti nel componente_dt ma vengono visualizzati in modalità differenti a seconda della funzionalita'
	canone_annuo numeric(9, 2) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	flg_rateo_prima_annualita numeric(1) DEFAULT 0 NOT NULL,
	numero_mesi numeric(2) NULL,
	data_inizio date NULL,
	id_componente_dt int4 NOT NULL,
	CONSTRAINT ak_risca_r_annualita_sd_01 UNIQUE (id_stato_debitorio, anno),
	CONSTRAINT chk_risca_r_annualita_sd_01 CHECK ((flg_rateo_prima_annualita = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_r_annualita_sd PRIMARY KEY (id_annualita_sd),
	CONSTRAINT fk_risca_d_componente_dt_02 FOREIGN KEY (id_componente_dt) REFERENCES risca_d_componente_dt(id_componente_dt),
	CONSTRAINT fk_risca_t_stato_debitorio_01 FOREIGN KEY (id_stato_debitorio) REFERENCES risca_t_stato_debitorio(id_stato_debitorio)
);
COMMENT ON TABLE risca_r_annualita_sd IS 'Per ogni stato debitorio memorizza gli eventuali Importi dovuti per singola ANNUALITA La riscossione può avere più stati debitori';

-- Column comments

COMMENT ON COLUMN risca_r_annualita_sd.id_annualita_sd IS 'Identificativo univoco';
COMMENT ON COLUMN risca_r_annualita_sd.json_dt_riscossione IS 'Permette di memorizzare i DT dello stato debitorio.
Viene generato da una copia del json_dt presente nella riscossione è può in seguito essere modificato.

Non ha un suo componente specifico SW, ma fà riferimento (risca_d_tipo_componente_dt) a quello definito per la riscossione (GESTIONE-Componente di Gestione)
Contiene tutti i campi definiti nel componente_dt ma vengono visualizzati in modalità differenti a seconda della funzionalita''';


-- risca_r_annualita_uso_sd definition

-- Drop table

-- DROP TABLE risca_r_annualita_uso_sd;

CREATE TABLE risca_r_annualita_uso_sd (
	id_annualita_uso_sd int4 NOT NULL, -- Identificativo univoco
	id_annualita_sd int4 NOT NULL,
	id_tipo_uso int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	canone_uso numeric(9, 2) NOT NULL,
	canone_unitario numeric(9, 2) NOT NULL,
	CONSTRAINT pk_risca_r_annualita_uso_sd PRIMARY KEY (id_annualita_uso_sd),
	CONSTRAINT fk_risca_d_tipo_uso_07 FOREIGN KEY (id_tipo_uso) REFERENCES risca_d_tipo_uso(id_tipo_uso),
	CONSTRAINT fk_risca_r_annualita_sd_01 FOREIGN KEY (id_annualita_sd) REFERENCES risca_r_annualita_sd(id_annualita_sd)
);

-- Column comments

COMMENT ON COLUMN risca_r_annualita_uso_sd.id_annualita_uso_sd IS 'Identificativo univoco';


-- risca_r_annualita_uso_sd_ra definition

-- Drop table

-- DROP TABLE risca_r_annualita_uso_sd_ra;

CREATE TABLE risca_r_annualita_uso_sd_ra (
	id_annualita_uso_sd_ra int4 NOT NULL, -- Identificativo univoco
	id_annualita_uso_sd int4 NOT NULL,
	id_riduzione_aumento int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_annualita_uso_sd_ra PRIMARY KEY (id_annualita_uso_sd_ra),
	CONSTRAINT fk_risca_d_riduzione_aumento_03 FOREIGN KEY (id_riduzione_aumento) REFERENCES risca_d_riduzione_aumento(id_riduzione_aumento),
	CONSTRAINT fk_risca_r_annualita_uso_sd_01 FOREIGN KEY (id_annualita_uso_sd) REFERENCES risca_r_annualita_uso_sd(id_annualita_uso_sd)
);

-- Column comments

COMMENT ON COLUMN risca_r_annualita_uso_sd_ra.id_annualita_uso_sd_ra IS 'Identificativo univoco';


-- risca_r_avviso_dati_ammin definition

-- Drop table

-- DROP TABLE risca_r_avviso_dati_ammin;

CREATE TABLE risca_r_avviso_dati_ammin (
	nap varchar(20) NOT NULL,
	codice_utenza varchar(20) NOT NULL,
	id_stato_debitorio int4 NOT NULL,
	corpo_idrico varchar(200) NULL,
	comune_di_presa varchar(200) NULL,
	periodo_di_contribuzione varchar(30) NULL,
	numero_protocollo_sped varchar(30) NULL,
	data_protocollo_sped date NULL,
	etichetta21_calc varchar(50) NULL,
	valore21_calc varchar(20) NULL,
	etichetta22_calc varchar(20) NULL,
	valore22_calc varchar(20) NULL,
	totale_utenza_calc varchar(20) NULL,
	scad_conc_eti_calc varchar(30) NULL,
	scad_conc_calc varchar(100) NULL,
	scadenza_concessione_calc varchar(100) NULL,
	provvedimento_calc varchar(150) NULL,
	testo varchar(20) NULL,
	data_scad_emas_iso date NULL,
	imp_compens_canone varchar(20) NULL,
	rec_canone varchar(20) NULL,
	num_pratica varchar(40) NULL,
	descr_utilizzo varchar(100) NULL,
	tot_energ_prod numeric(20, 6) NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_avviso_dati_ammin PRIMARY KEY (nap, codice_utenza),
	CONSTRAINT fk_risca_r_avviso_dati_titolare_01 FOREIGN KEY (nap) REFERENCES risca_r_avviso_dati_titolare(nap),
	CONSTRAINT fk_risca_t_stato_debitorio_05 FOREIGN KEY (id_stato_debitorio) REFERENCES risca_t_stato_debitorio(id_stato_debitorio)
);


-- risca_r_provvedimento definition

-- Drop table

-- DROP TABLE risca_r_provvedimento;

CREATE TABLE risca_r_provvedimento (
	id_provvedimento int4 NOT NULL, -- Identificativo univoco
	id_riscossione int4 NOT NULL,
	id_tipo_titolo int4 NULL,
	id_tipo_provvedimento int4 NOT NULL,
	num_titolo varchar(20) NULL,
	data_provvedimento date NULL,
	note varchar(500) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_provvedimento PRIMARY KEY (id_provvedimento),
	CONSTRAINT fk_risca_d_tipo_provvedi_01 FOREIGN KEY (id_tipo_provvedimento) REFERENCES risca_d_tipo_provvedimento(id_tipo_provvedimento),
	CONSTRAINT fk_risca_d_tipo_titolo_01 FOREIGN KEY (id_tipo_titolo) REFERENCES risca_d_tipo_titolo(id_tipo_titolo),
	CONSTRAINT fk_risca_t_riscossione_02 FOREIGN KEY (id_riscossione) REFERENCES risca_t_riscossione(id_riscossione)
);
CREATE INDEX ie_risca_r_provvedimento_01 ON risca_r_provvedimento USING btree (id_riscossione);
CREATE INDEX ie_risca_r_provvedimento_02 ON risca_r_provvedimento USING btree (id_tipo_provvedimento, id_tipo_titolo);
COMMENT ON TABLE risca_r_provvedimento IS 'Permette di censire l''elenco delle tipologie di Istanza o Provvedimento che hanno portato all''ottenimento della Concessione. Per i provvedimenti si specifica anche il numero (num_titolo)';

-- Column comments

COMMENT ON COLUMN risca_r_provvedimento.id_provvedimento IS 'Identificativo univoco';


-- risca_r_rata_sd definition

-- Drop table

-- DROP TABLE risca_r_rata_sd;

CREATE TABLE risca_r_rata_sd (
	id_rata_sd int4 NOT NULL, -- Identificativo univoco
	id_stato_debitorio int4 NOT NULL,
	id_rata_sd_padre int4 NULL,
	canone_dovuto numeric(9, 2) NULL,
	interessi_maturati numeric(9, 2) NULL,
	data_scadenza_pagamento date NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_rata_sd PRIMARY KEY (id_rata_sd),
	CONSTRAINT fk_risca_r_rata_sd_01 FOREIGN KEY (id_rata_sd_padre) REFERENCES risca_r_rata_sd(id_rata_sd),
	CONSTRAINT fk_risca_t_stato_debitorio_02 FOREIGN KEY (id_stato_debitorio) REFERENCES risca_t_stato_debitorio(id_stato_debitorio)
);
CREATE INDEX ie_risca_r_rata_sd_01 ON risca_r_rata_sd USING btree (id_stato_debitorio);
CREATE INDEX ie_risca_r_rata_sd_02 ON risca_r_rata_sd USING btree (id_rata_sd_padre);

-- Column comments

COMMENT ON COLUMN risca_r_rata_sd.id_rata_sd IS 'Identificativo univoco';


-- risca_r_rimborso definition

-- Drop table

-- DROP TABLE risca_r_rimborso;

CREATE TABLE risca_r_rimborso (
	id_rimborso int4 NOT NULL,
	id_stato_debitorio int4 NOT NULL,
	id_tipo_rimborso int4 NOT NULL,
	id_soggetto int4 NOT NULL, -- Soggetto da rimborsare.  Non è detto che sia quello presente nella riscossione come titolare
	imp_rimborso numeric(9, 2) NOT NULL,
	causale varchar(300) NOT NULL,
	num_determina varchar(30) NOT NULL,
	data_determina date NOT NULL,
	imp_restituito numeric(9, 2) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	id_gruppo_soggetto int4 NULL,
	CONSTRAINT pk_risca_r_rimborso PRIMARY KEY (id_rimborso),
	CONSTRAINT fk_risca_d_tipo_rimborso_01 FOREIGN KEY (id_tipo_rimborso) REFERENCES risca_d_tipo_rimborso(id_tipo_rimborso),
	CONSTRAINT fk_risca_t_gruppo_soggetto_06 FOREIGN KEY (id_gruppo_soggetto) REFERENCES risca_t_gruppo_soggetto(id_gruppo_soggetto),
	CONSTRAINT fk_risca_t_soggetto_05 FOREIGN KEY (id_soggetto) REFERENCES risca_t_soggetto(id_soggetto),
	CONSTRAINT fk_risca_t_stato_debitorio_04 FOREIGN KEY (id_stato_debitorio) REFERENCES risca_t_stato_debitorio(id_stato_debitorio)
);
CREATE INDEX ie_risca_r_rimborso_01 ON risca_r_rimborso USING btree (id_tipo_rimborso, id_stato_debitorio);
CREATE INDEX ie_risca_r_rimborso_02 ON risca_r_rimborso USING btree (id_soggetto, id_gruppo_soggetto);

-- Column comments

COMMENT ON COLUMN risca_r_rimborso.id_soggetto IS 'Soggetto da rimborsare.  Non è detto che sia quello presente nella riscossione come titolare';


-- risca_r_rimborso_sd_utilizzato definition

-- Drop table

-- DROP TABLE risca_r_rimborso_sd_utilizzato;

CREATE TABLE risca_r_rimborso_sd_utilizzato (
	id_stato_debitorio int4 NOT NULL,
	id_rimborso int4 NOT NULL,
	imp_utilizzato numeric(9, 2) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_rimborso_sd_utilizzato PRIMARY KEY (id_stato_debitorio, id_rimborso),
	CONSTRAINT fk_risca_r_rimborso_01 FOREIGN KEY (id_rimborso) REFERENCES risca_r_rimborso(id_rimborso),
	CONSTRAINT fk_risca_t_stato_debitorio_03 FOREIGN KEY (id_stato_debitorio) REFERENCES risca_t_stato_debitorio(id_stato_debitorio)
);


-- risca_r_riscossione_recapito definition

-- Drop table

-- DROP TABLE risca_r_riscossione_recapito;

CREATE TABLE risca_r_riscossione_recapito (
	id_riscossione int4 NOT NULL,
	id_recapito int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_riscossione_recapito PRIMARY KEY (id_riscossione, id_recapito),
	CONSTRAINT fk_risca_r_recapito_02 FOREIGN KEY (id_recapito) REFERENCES risca_r_recapito(id_recapito),
	CONSTRAINT fk_risca_t_riscossione_03 FOREIGN KEY (id_riscossione) REFERENCES risca_t_riscossione(id_riscossione)
);


-- risca_r_riscossione_storia_titolare definition

-- Drop table

-- DROP TABLE risca_r_riscossione_storia_titolare;

CREATE TABLE risca_r_riscossione_storia_titolare (
	id_riscossione_storia_titolare int4 NOT NULL, -- Identificativo univoco
	id_riscossione int4 NOT NULL,
	id_soggetto int4 NOT NULL,
	id_gruppo_soggetto int4 NULL,
	data_inizio_titolarita date NOT NULL,
	data_fine_titolarita date NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_riscossione_storia_titolare PRIMARY KEY (id_riscossione_storia_titolare),
	CONSTRAINT fk_risca_t_gruppo_soggetto_05 FOREIGN KEY (id_gruppo_soggetto) REFERENCES risca_t_gruppo_soggetto(id_gruppo_soggetto),
	CONSTRAINT fk_risca_t_riscossione_06 FOREIGN KEY (id_riscossione) REFERENCES risca_t_riscossione(id_riscossione),
	CONSTRAINT fk_risca_t_soggetto_06 FOREIGN KEY (id_soggetto) REFERENCES risca_t_soggetto(id_soggetto)
);
CREATE UNIQUE INDEX ak_risca_r_riscossione_storia_titolare_01 ON risca_r_riscossione_storia_titolare USING btree (id_riscossione);
COMMENT ON TABLE risca_r_riscossione_storia_titolare IS 'Permette di storicizzare il soggetto della riscossione a fronte di un cambio di titolarita';

-- Column comments

COMMENT ON COLUMN risca_r_riscossione_storia_titolare.id_riscossione_storia_titolare IS 'Identificativo univoco';


-- risca_r_riscossione_uso definition

-- Drop table

-- DROP TABLE risca_r_riscossione_uso;

CREATE TABLE risca_r_riscossione_uso (
	id_riscossione_uso int4 NOT NULL, -- Identificativo univoco
	id_riscossione int4 NOT NULL,
	id_tipo_uso int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_riscossione_uso PRIMARY KEY (id_riscossione_uso),
	CONSTRAINT fk_risca_d_tipo_uso_05 FOREIGN KEY (id_tipo_uso) REFERENCES risca_d_tipo_uso(id_tipo_uso),
	CONSTRAINT fk_risca_t_riscossione_04 FOREIGN KEY (id_riscossione) REFERENCES risca_t_riscossione(id_riscossione)
);
CREATE UNIQUE INDEX ak_risca_r_riscossione_uso_01 ON risca_r_riscossione_uso USING btree (id_riscossione, id_tipo_uso);

-- Column comments

COMMENT ON COLUMN risca_r_riscossione_uso.id_riscossione_uso IS 'Identificativo univoco';


-- risca_r_uso_ridaum definition

-- Drop table

-- DROP TABLE risca_r_uso_ridaum;

CREATE TABLE risca_r_uso_ridaum (
	id_riscossione_uso int4 NOT NULL,
	id_riduzione_aumento int4 NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	id_uso_ridaum int4 NOT NULL,
	CONSTRAINT pk_risca_r_uso_ridaum PRIMARY KEY (id_uso_ridaum),
	CONSTRAINT fk_risca_d_riduzione_aumento_02 FOREIGN KEY (id_riduzione_aumento) REFERENCES risca_d_riduzione_aumento(id_riduzione_aumento),
	CONSTRAINT fk_risca_r_riscossione_uso_01 FOREIGN KEY (id_riscossione_uso) REFERENCES risca_r_riscossione_uso(id_riscossione_uso)
);
CREATE INDEX ie_risca_r_uso_ridaum_01 ON risca_r_uso_ridaum USING btree (id_riduzione_aumento);


-- risca_t_accertamento definition

-- Drop table

-- DROP TABLE risca_t_accertamento;

CREATE TABLE risca_t_accertamento (
	id_accertamento int4 NOT NULL, -- identificativo univoco
	id_stato_debitorio int4 NOT NULL,
	id_tipo_accertamento int4 NOT NULL,
	id_file_450 int4 NULL,
	num_protocollo varchar(30) NULL,
	data_protocollo date NULL,
	data_scadenza date NULL,
	flg_restituito numeric(1) DEFAULT 0 NOT NULL, -- FLG_RESTITUITO (Gerica) -  identifica se l'accertamento è stato restiuito
	flg_annullato numeric(1) DEFAULT 0 NOT NULL, -- FLG_ANNULLATO (Gerica) -  identifica se l'accertamento è stato annullato,
	data_notifica date NULL,
	nota varchar(250) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	id_spedizione int4 NULL,
	CONSTRAINT chk_risca_t_accertamento_01 CHECK ((flg_restituito = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_t_accertamento_02 CHECK ((flg_annullato = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_t_accertamento PRIMARY KEY (id_accertamento),
	CONSTRAINT fk_risca_d_tipo_accertamento_01 FOREIGN KEY (id_tipo_accertamento) REFERENCES risca_d_tipo_accertamento(id_tipo_accertamento),
	CONSTRAINT fk_risca_t_file_450_01 FOREIGN KEY (id_file_450) REFERENCES risca_t_file_450(id_file_450),
	CONSTRAINT fk_risca_t_spedizione_03 FOREIGN KEY (id_spedizione) REFERENCES risca_t_spedizione(id_spedizione),
	CONSTRAINT fk_risca_t_stato_debitorio_06 FOREIGN KEY (id_stato_debitorio) REFERENCES risca_t_stato_debitorio(id_stato_debitorio)
);
CREATE INDEX ie_risca_t_accertamento_01 ON risca_t_accertamento USING btree (id_stato_debitorio);
CREATE INDEX ie_risca_t_accertamento_02 ON risca_t_accertamento USING btree (id_tipo_accertamento);

-- Column comments

COMMENT ON COLUMN risca_t_accertamento.id_accertamento IS 'identificativo univoco';
COMMENT ON COLUMN risca_t_accertamento.flg_restituito IS 'FLG_RESTITUITO (Gerica) -  identifica se l''accertamento è stato restiuito';
COMMENT ON COLUMN risca_t_accertamento.flg_annullato IS 'FLG_ANNULLATO (Gerica) -  identifica se l''accertamento è stato annullato';


-- risca_t_invio_acta definition

-- Drop table

-- DROP TABLE risca_t_invio_acta;

CREATE TABLE risca_t_invio_acta (
	id_invio_acta int4 NOT NULL, -- identificativo univoco spedizione acta
	nap varchar(20) NULL,
	id_spedizione_acta int4 NOT NULL,
	nome_file varchar(150) NOT NULL,
	flg_multiclassificazione numeric(1) DEFAULT 0 NOT NULL, -- 1-multiclassificazione (indica che ci sono più codici utenza legati allo stesso nap)
	flg_archiviata_acta numeric(1) DEFAULT 0 NOT NULL, -- 1-invio_archiviato_in acta
	data_invio timestamp NOT NULL,
	uuid_message varchar(45) NULL, -- chiave univoca restituita dal sistema acta al ricevimento della comunicazione
	data_esito_acta timestamp NULL,
	cod_esito_acquisizione_acta varchar(3) NULL, -- codice esito ricevuto da acta sul singolo invio del documento
	desc_esito_acquisizione_acta varchar(500) NULL, -- descrizione esito ricevuto da acta sul singolo invio del documento
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	cod_esito_invio_acta varchar(3) NULL, -- codice esito ricevuto da acta a seguito del richiamo del servizio su singolo documento
	desc_esito_invio_acta varchar(500) NULL, -- descrizione esito ricevuto da acta  a seguito del richiamo del servizio su singolo documento
	id_accertamento int4 NULL,
	CONSTRAINT chk_risca_t_invio_acta_01 CHECK ((flg_multiclassificazione = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_t_invio_acta_02 CHECK ((flg_archiviata_acta = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_t_invio_acta PRIMARY KEY (id_invio_acta),
	CONSTRAINT fk_risca_r_spedizione_acta_01 FOREIGN KEY (id_spedizione_acta) REFERENCES risca_r_spedizione_acta(id_spedizione_acta),
	CONSTRAINT fk_risca_t_accertamento_06 FOREIGN KEY (id_accertamento) REFERENCES risca_t_accertamento(id_accertamento),
	CONSTRAINT fk_risca_t_avviso_pagamento_03 FOREIGN KEY (nap) REFERENCES risca_t_avviso_pagamento(nap)
);
CREATE INDEX ie_risca_t_invio_acta_01 ON risca_t_invio_acta USING btree (nap, id_accertamento);

-- Column comments

COMMENT ON COLUMN risca_t_invio_acta.id_invio_acta IS 'identificativo univoco spedizione acta';
COMMENT ON COLUMN risca_t_invio_acta.flg_multiclassificazione IS '1-multiclassificazione (indica che ci sono più codici utenza legati allo stesso nap)';
COMMENT ON COLUMN risca_t_invio_acta.flg_archiviata_acta IS '1-invio_archiviato_in acta';
COMMENT ON COLUMN risca_t_invio_acta.uuid_message IS 'chiave univoca restituita dal sistema acta al ricevimento della comunicazione';
COMMENT ON COLUMN risca_t_invio_acta.cod_esito_acquisizione_acta IS 'codice esito ricevuto da acta sul singolo invio del documento';
COMMENT ON COLUMN risca_t_invio_acta.desc_esito_acquisizione_acta IS 'descrizione esito ricevuto da acta sul singolo invio del documento';
COMMENT ON COLUMN risca_t_invio_acta.cod_esito_invio_acta IS 'codice esito ricevuto da acta a seguito del richiamo del servizio su singolo documento';
COMMENT ON COLUMN risca_t_invio_acta.desc_esito_invio_acta IS 'descrizione esito ricevuto da acta  a seguito del richiamo del servizio su singolo documento';


-- risca_w_avviso_annualita definition

-- Drop table

-- DROP TABLE risca_w_avviso_annualita;

CREATE TABLE risca_w_avviso_annualita (
	nap varchar(20) NOT NULL,
	codice_utenza varchar(20) NOT NULL,
	anno_rich_pagamento numeric(6) NOT NULL,
	fraz_totale_canone_anno numeric(9, 2) NULL,
	totale_canone_anno_calc numeric(9, 2) NULL,
	etichetta20_calc varchar(50) NULL,
	valore20_calc numeric(9, 2) NULL,
	vuoto varchar(20) NULL,
	CONSTRAINT pk_risca_w_avviso_annualita PRIMARY KEY (nap, codice_utenza, anno_rich_pagamento),
	CONSTRAINT fk_risca_w_avviso_dati_ammin_01 FOREIGN KEY (nap,codice_utenza) REFERENCES risca_w_avviso_dati_ammin(nap,codice_utenza)
);
CREATE INDEX ie_risca_w_avviso_annualita_01 ON risca_w_avviso_annualita USING btree (nap, codice_utenza);
CREATE INDEX ie_risca_w_avviso_annualita_02 ON risca_w_avviso_annualita USING btree (nap);


-- risca_w_avviso_uso definition

-- Drop table

-- DROP TABLE risca_w_avviso_uso;

CREATE TABLE risca_w_avviso_uso (
	nap varchar(20) NOT NULL,
	codice_utenza varchar(20) NOT NULL,
	anno_rich_pagamento numeric(6) NOT NULL,
	uso_denominazione varchar(200) NOT NULL,
	unita_di_misura varchar(50) NULL,
	quantita numeric(9, 2) NULL,
	canone_unitario numeric(9, 2) NULL,
	canone_uso numeric(9, 2) NULL,
	unita_mis1_calc varchar(20) NULL,
	quantita_calc varchar(11) NULL,
	condizioni_particolari_calc varchar(20) NULL,
	uso_denominazione_p_calc varchar(200) NULL,
	unita_mis_p_calc varchar(20) NULL,
	quantita_p_calc numeric(15, 4) NULL,
	unita_di_misura_p_calc varchar(20) NULL,
	canone_unitario_p_calc numeric(9, 2) NULL,
	canone_uso_p_calc numeric(9, 2) NULL,
	perc_falda_prof numeric(5, 2) NULL,
	CONSTRAINT pk_risca_w_avviso_uso PRIMARY KEY (nap, codice_utenza, anno_rich_pagamento, uso_denominazione),
	CONSTRAINT fk_risca_w_avviso_annualita_01 FOREIGN KEY (nap,codice_utenza,anno_rich_pagamento) REFERENCES risca_w_avviso_annualita(nap,codice_utenza,anno_rich_pagamento)
);
CREATE INDEX ie_risca_w_avviso_uso_01 ON risca_w_avviso_uso USING btree (nap, codice_utenza, anno_rich_pagamento);
CREATE INDEX ie_risca_w_avviso_uso_02 ON risca_w_avviso_uso USING btree (nap);


-- risca_w_rata_sd definition

-- Drop table

-- DROP TABLE risca_w_rata_sd;

CREATE TABLE risca_w_rata_sd (
	id_rata_sd int4 NOT NULL,
	id_stato_debitorio int4 NOT NULL,
	id_rata_sd_padre int4 NULL,
	canone_dovuto numeric(9, 2) NULL,
	interessi_maturati numeric(9, 2) NULL,
	data_scadenza_pagamento date NULL,
	CONSTRAINT pk_risca_w_rata_sd PRIMARY KEY (id_rata_sd),
	CONSTRAINT fk_risca_r_rata_sd_01 FOREIGN KEY (id_rata_sd_padre) REFERENCES risca_r_rata_sd(id_rata_sd),
	CONSTRAINT fk_risca_w_stato_debitorio_02 FOREIGN KEY (id_stato_debitorio) REFERENCES risca_w_stato_debitorio(id_stato_debitorio)
);
CREATE INDEX ie_risca_w_rata_sd_01 ON risca_w_rata_sd USING btree (id_stato_debitorio);


-- risca_r_avviso_annualita definition

-- Drop table

-- DROP TABLE risca_r_avviso_annualita;

CREATE TABLE risca_r_avviso_annualita (
	nap varchar(20) NOT NULL,
	codice_utenza varchar(20) NOT NULL,
	anno_rich_pagamento numeric(6) NOT NULL,
	fraz_totale_canone_anno numeric(9, 2) NULL,
	totale_canone_anno_calc numeric(9, 2) NULL,
	etichetta20_calc varchar(50) NULL,
	valore20_calc numeric(9, 2) NULL,
	vuoto varchar(20) NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_avviso_annualita PRIMARY KEY (nap, codice_utenza, anno_rich_pagamento),
	CONSTRAINT fk_risca_r_avviso_dati_ammin_01 FOREIGN KEY (nap,codice_utenza) REFERENCES risca_r_avviso_dati_ammin(nap,codice_utenza)
);
CREATE INDEX ie_risca_r_avviso_annualita_01 ON risca_r_avviso_annualita USING btree (nap, codice_utenza);


-- risca_r_avviso_uso definition

-- Drop table

-- DROP TABLE risca_r_avviso_uso;

CREATE TABLE risca_r_avviso_uso (
	nap varchar(20) NOT NULL,
	codice_utenza varchar(20) NOT NULL,
	anno_rich_pagamento numeric(6) NOT NULL,
	uso_denominazione varchar(200) NOT NULL,
	unita_di_misura varchar(50) NULL,
	quantita numeric(9, 2) NULL,
	canone_unitario numeric(9, 2) NULL,
	canone_uso numeric(9, 2) NULL,
	unita_mis1_calc varchar(20) NULL,
	quantita_calc varchar(11) NULL,
	condizioni_particolari_calc varchar(20) NULL,
	uso_denominazione_p_calc varchar(200) NULL,
	unita_mis_p_calc varchar(20) NULL,
	quantita_p_calc numeric(15, 4) NULL,
	unita_di_misura_p_calc varchar(20) NULL,
	canone_unitario_p_calc numeric(9, 2) NULL,
	canone_uso_p_calc numeric(9, 2) NULL,
	perc_falda_prof numeric(5, 2) NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_avviso_uso PRIMARY KEY (nap, codice_utenza, anno_rich_pagamento, uso_denominazione),
	CONSTRAINT fk_risca_r_avviso_annualita_01 FOREIGN KEY (nap,codice_utenza,anno_rich_pagamento) REFERENCES risca_r_avviso_annualita(nap,codice_utenza,anno_rich_pagamento)
);
CREATE INDEX ie_risca_r_avviso_uso_01 ON risca_r_avviso_uso USING btree (nap, codice_utenza, anno_rich_pagamento);


-- risca_r_dettaglio_pag definition

-- Drop table

-- DROP TABLE risca_r_dettaglio_pag;

CREATE TABLE risca_r_dettaglio_pag (
	id_dettaglio_pag int4 NOT NULL,
	id_rata_sd int4 NOT NULL,
	id_pagamento int4 NOT NULL,
	importo_versato numeric(13, 2) NULL,
	interessi_maturati numeric(13, 2) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_dettaglio_pag PRIMARY KEY (id_dettaglio_pag),
	CONSTRAINT fk_risca_r_rata_sd_02 FOREIGN KEY (id_rata_sd) REFERENCES risca_r_rata_sd(id_rata_sd),
	CONSTRAINT fk_risca_t_pagamento_01 FOREIGN KEY (id_pagamento) REFERENCES risca_t_pagamento(id_pagamento)
);
CREATE INDEX ie_risca_r_dettaglio_pag_01 ON risca_r_dettaglio_pag USING btree (id_rata_sd);
CREATE INDEX ie_risca_r_dettaglio_pag_02 ON risca_r_dettaglio_pag USING btree (id_pagamento);


-- risca_r_dettaglio_pag_bil_acc definition

-- Drop table

-- DROP TABLE risca_r_dettaglio_pag_bil_acc;

CREATE TABLE risca_r_dettaglio_pag_bil_acc (
	id_dettaglio_pag_bil_acc int4 NOT NULL,
	id_dettaglio_pag int4 NOT NULL,
	id_bil_acc int4 NOT NULL,
	importo_accerta_bilancio numeric(13, 2) NOT NULL, -- quota del dettaglio di pagamento relativa all'accertamento di bilancio indicato
	flg_eccedenza numeric(1) DEFAULT 0 NOT NULL, -- l'importo indicato è in eccedenza rispetto a quanto dovuto,
	flg_ruolo numeric(1) DEFAULT 0 NOT NULL, -- il soggetto tenuto al pagamento è stato iscritto a ruolo
	flg_pubblico numeric(1) DEFAULT 0 NOT NULL, -- il soggetto tenuto al pagamento è Persona Giuridica Pubblica
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_r_dettaglio_pag_bil_acc_01 CHECK ((flg_eccedenza = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_r_dettaglio_pag_bil_acc_03 CHECK ((flg_ruolo = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT chk_risca_r_dettaglio_pag_bil_acc_04 CHECK ((flg_pubblico = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_r_dettaglio_pag_bil_acc PRIMARY KEY (id_dettaglio_pag_bil_acc),
	CONSTRAINT fk_risca_r_dettaglio_pag_01 FOREIGN KEY (id_dettaglio_pag) REFERENCES risca_r_dettaglio_pag(id_dettaglio_pag),
	CONSTRAINT fk_risca_t_bil_acc_01 FOREIGN KEY (id_bil_acc) REFERENCES risca_t_bil_acc(id_bil_acc)
);
CREATE INDEX ie_risca_r_dettaglio_pag_bil_acc_01 ON risca_r_dettaglio_pag_bil_acc USING btree (id_dettaglio_pag);
CREATE INDEX ie_risca_r_dettaglio_pag_bil_acc_02 ON risca_r_dettaglio_pag_bil_acc USING btree (id_bil_acc);

-- Column comments

COMMENT ON COLUMN risca_r_dettaglio_pag_bil_acc.importo_accerta_bilancio IS 'quota del dettaglio di pagamento relativa all''accertamento di bilancio indicato';
COMMENT ON COLUMN risca_r_dettaglio_pag_bil_acc.flg_eccedenza IS 'l''importo indicato è in eccedenza rispetto a quanto dovuto';
COMMENT ON COLUMN risca_r_dettaglio_pag_bil_acc.flg_ruolo IS 'il soggetto tenuto al pagamento è stato iscritto a ruolo';
COMMENT ON COLUMN risca_r_dettaglio_pag_bil_acc.flg_pubblico IS 'il soggetto tenuto al pagamento è Persona Giuridica Pubblica';


-- risca_r_ruolo definition

-- Drop table

-- DROP TABLE risca_r_ruolo;

CREATE TABLE risca_r_ruolo (
	id_ruolo int4 NOT NULL, -- identificativo univoco
	id_accertamento int4 NOT NULL,
	data_creazione_ruolo date NOT NULL, -- sysdate di creazione ruolo (equivale a risca_t_file450.data_creazione)
	num_bil_acc_canone varchar(25) NULL, -- corrisponde al valore di 'NUMERO ACCERTAMENTO BILANCIO' (con codice entrata=1R96 – tipo record M50), per lo SD corrente
	num_bil_acc_interessi varchar(25) NULL, -- corrisponde al valore di 'NUMERO ACCERTAMENTO BILANCIO' (con codice entrata=1R97 – tipo record M50), per lo SD corrente
	num_bil_acc_spese varchar(25) NULL, -- corrisponde al valore di 'NUMERO ACCERTAMENTO BILANCIO' (con codice entrata=1R98 – tipo record M50), per lo SD corrente
	importo_canone_mancante numeric(11, 2) NULL, -- Codice Entrata IR96 che identifica l'importo (record M50)
	importo_interessi_mancanti numeric(11, 2) NULL, -- Codice Entrata IR97 che identifica l'importo (record M50),
	importo_spese_mancanti numeric(11, 2) NULL, -- Codice Entrata IR98 che identifica l'importo (record M50)
	codice_ente_creditore varchar(5) NOT NULL, -- Contiene l’analogo valore di 'CODICE ENTE CREDITORE' dei tipi record del file 450 creato, per lo stato debitorio iscritto alla riscossione coattiva,
	tipo_ufficio varchar(1) NULL, -- Informazione generata dal parametro 'MinutaRuolo.TipoUfficio' (record M20)
	codice_ufficio varchar(6) NULL, -- Informazione generata dal parametro 'MinutaRuolo.CodiceUfficio'  (record M20)
	anno_scadenza numeric(4) NULL, -- Anno scadenza del pagamento (record M20)
	identif_tipologia_atto numeric(3) NULL, -- Informazione generata dal parametro 'MinutaRuolo.IdentifTipologiaAtto' (record M20)
	numero_partita numeric(9) NULL, -- Valorizzato con il numero in fornitura (record M20)
	progressivo_partita numeric(3) NULL, -- Valorizzato con il progressivo in fornitura (record M20)
	codice_tipo_atto varchar(2) NULL, -- Informazione generata dal parametro 'MinutaRuolo.CodiceTipoAtto' (record M20)
	motivazione_iscrizione varchar(28) NULL, -- Eventualmente generata in fase di creazione file-450 (record M20)
	p_ident_prenot_ruolo varchar(34) NOT NULL, -- Identificativo della prenotazione del Ruolo generato in RISCA alla creazione del file 450
	note varchar(200) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_ruolo PRIMARY KEY (id_ruolo),
	CONSTRAINT fk_risca_t_accertamento_07 FOREIGN KEY (id_accertamento) REFERENCES risca_t_accertamento(id_accertamento)
);
COMMENT ON TABLE risca_r_ruolo IS 'Permette di raccogliere i dati dei ruoli generati per un determinato accertamento e inviati a Soris con il relativo file 450';

-- Column comments

COMMENT ON COLUMN risca_r_ruolo.id_ruolo IS 'identificativo univoco';
COMMENT ON COLUMN risca_r_ruolo.data_creazione_ruolo IS 'sysdate di creazione ruolo (equivale a risca_t_file450.data_creazione)';
COMMENT ON COLUMN risca_r_ruolo.num_bil_acc_canone IS 'corrisponde al valore di ''NUMERO ACCERTAMENTO BILANCIO'' (con codice entrata=1R96 – tipo record M50), per lo SD corrente';
COMMENT ON COLUMN risca_r_ruolo.num_bil_acc_interessi IS 'corrisponde al valore di ''NUMERO ACCERTAMENTO BILANCIO'' (con codice entrata=1R97 – tipo record M50), per lo SD corrente';
COMMENT ON COLUMN risca_r_ruolo.num_bil_acc_spese IS 'corrisponde al valore di ''NUMERO ACCERTAMENTO BILANCIO'' (con codice entrata=1R98 – tipo record M50), per lo SD corrente';
COMMENT ON COLUMN risca_r_ruolo.importo_canone_mancante IS 'Codice Entrata IR96 che identifica l''importo (record M50)';
COMMENT ON COLUMN risca_r_ruolo.importo_interessi_mancanti IS 'Codice Entrata IR97 che identifica l''importo (record M50)';
COMMENT ON COLUMN risca_r_ruolo.importo_spese_mancanti IS 'Codice Entrata IR98 che identifica l''importo (record M50)';
COMMENT ON COLUMN risca_r_ruolo.codice_ente_creditore IS 'Contiene l’analogo valore di ''CODICE ENTE CREDITORE'' dei tipi record del file 450 creato, per lo stato debitorio iscritto alla riscossione coattiva';
COMMENT ON COLUMN risca_r_ruolo.tipo_ufficio IS 'Informazione generata dal parametro ''MinutaRuolo.TipoUfficio'' (record M20)';
COMMENT ON COLUMN risca_r_ruolo.codice_ufficio IS 'Informazione generata dal parametro ''MinutaRuolo.CodiceUfficio''  (record M20)';
COMMENT ON COLUMN risca_r_ruolo.anno_scadenza IS 'Anno scadenza del pagamento (record M20)';
COMMENT ON COLUMN risca_r_ruolo.identif_tipologia_atto IS 'Informazione generata dal parametro ''MinutaRuolo.IdentifTipologiaAtto'' (record M20)';
COMMENT ON COLUMN risca_r_ruolo.numero_partita IS 'Valorizzato con il numero in fornitura (record M20)';
COMMENT ON COLUMN risca_r_ruolo.progressivo_partita IS 'Valorizzato con il progressivo in fornitura (record M20)';
COMMENT ON COLUMN risca_r_ruolo.codice_tipo_atto IS 'Informazione generata dal parametro ''MinutaRuolo.CodiceTipoAtto'' (record M20)';
COMMENT ON COLUMN risca_r_ruolo.motivazione_iscrizione IS 'Eventualmente generata in fase di creazione file-450 (record M20)';
COMMENT ON COLUMN risca_r_ruolo.p_ident_prenot_ruolo IS 'Identificativo della prenotazione del Ruolo generato in RISCA alla creazione del file 450';


-- risca_r_ruolo_soris_fr1 definition

-- Drop table

-- DROP TABLE risca_r_ruolo_soris_fr1;

CREATE TABLE risca_r_ruolo_soris_fr1 (
	id_ruolo_soris_fr1 int4 NOT NULL, -- identificativo univoco
	id_ruolo int4 NULL, -- identificativo del ruolo a cui e' associato il dato Soris. null nel caso in cui non è stata trovata la corrispondenza nei ruoli risca
	id_file_soris int4 NOT NULL, -- permette di identificare il file Soris che ha comunicato il dato,
	tipo_record varchar(3) NOT NULL, -- tipologia del record inviato da Soris
	utenza varchar(7) NOT NULL, -- estratto dal campo : p_ident_atto
	progr_record numeric(7) NULL,
	cod_ambito varchar(3) NULL,
	cod_ente_creditore varchar(5) NULL,
	anno_ruolo varchar(4) NULL,
	numero_ruolo varchar(6) NULL,
	p_tipo_ufficio varchar(1) NULL,
	p_codice_ufficio varchar(6) NULL,
	p_anno_riferimento varchar(4) NULL,
	p_tipo_modello varchar(3) NULL,
	p_ident_prenot_ruolo varchar(34) NULL,
	p_ident_atto varchar(48) NULL,
	progr_articolo_ruolo numeric(3) NULL,
	identif_cartella varchar(20) NULL,
	flg_rateizzazione numeric(1) DEFAULT 0 NOT NULL, -- Indica se la cartella è riferita ad una reteizzazione (1)
	progr_articolo_cartella numeric(3) NULL,
	codice_entrata varchar(4) NULL,
	tipo_entrata varchar(1) NULL,
	codice_fiscale varchar(16) NULL,
	tipo_evento varchar(1) NULL,
	data_evento date NULL,
	importo_carico numeric(15, 2) NULL,
	codice_divisa varchar(3) NULL,
	data_scad_reg date NULL,
	note varchar(200) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_r_ruolo_soris_fr1_01 CHECK ((flg_rateizzazione = ANY (ARRAY[(0)::numeric, (1)::numeric]))),
	CONSTRAINT pk_risca_r_ruolo_soris_fr1 PRIMARY KEY (id_ruolo_soris_fr1),
	CONSTRAINT fk_risca_r_ruolo_01 FOREIGN KEY (id_ruolo) REFERENCES risca_r_ruolo(id_ruolo),
	CONSTRAINT fk_risca_t_file_soris_02 FOREIGN KEY (id_file_soris) REFERENCES risca_t_file_soris(id_file_soris)
);
COMMENT ON TABLE risca_r_ruolo_soris_fr1 IS 'Permette di raccogliere i dati riferiti al tipo record FR1, comunicati da SORIS rispetto ai ruoli inviati precedentemente con il relativo file 450';

-- Column comments

COMMENT ON COLUMN risca_r_ruolo_soris_fr1.id_ruolo_soris_fr1 IS 'identificativo univoco';
COMMENT ON COLUMN risca_r_ruolo_soris_fr1.id_ruolo IS 'identificativo del ruolo a cui e'' associato il dato Soris. null nel caso in cui non è stata trovata la corrispondenza nei ruoli risca';
COMMENT ON COLUMN risca_r_ruolo_soris_fr1.id_file_soris IS 'permette di identificare il file Soris che ha comunicato il dato';
COMMENT ON COLUMN risca_r_ruolo_soris_fr1.tipo_record IS 'tipologia del record inviato da Soris';
COMMENT ON COLUMN risca_r_ruolo_soris_fr1.utenza IS 'estratto dal campo : p_ident_atto';
COMMENT ON COLUMN risca_r_ruolo_soris_fr1.flg_rateizzazione IS 'Indica se la cartella è riferita ad una reteizzazione (1)';


-- risca_r_ruolo_soris_fr3 definition

-- Drop table

-- DROP TABLE risca_r_ruolo_soris_fr3;

CREATE TABLE risca_r_ruolo_soris_fr3 (
	id_ruolo_soris_fr3 int4 NOT NULL, -- identificativo univoco
	id_ruolo int4 NULL, -- identificativo del ruolo a cui e' associato il dato Soris. null nel caso in cui non è stata trovata la corrispondenza nei ruoli risca
	id_file_soris int4 NOT NULL, -- permette di identificare il file Soris che ha comunicato il dato,
	id_pagamento int4 NULL,
	tipo_record varchar(3) NULL, -- tipologia del record inviato da Soris
	utenza varchar(7) NOT NULL, -- estratto dal campo : p_ident_atto
	progr_record numeric(7) NULL,
	cod_ambito varchar(3) NULL,
	cod_ente_creditore varchar(5) NULL,
	anno_ruolo varchar(4) NULL,
	numero_ruolo varchar(6) NULL,
	p_tipo_ufficio varchar(1) NULL,
	p_codice_ufficio varchar(6) NULL,
	p_anno_riferimento varchar(4) NULL,
	p_tipo_modello varchar(3) NULL,
	p_ident_prenot_ruolo varchar(34) NULL,
	p_ident_atto varchar(48) NULL,
	progr_articolo_ruolo numeric(3) NULL,
	identif_cartella varchar(20) NULL,
	progr_articolo_cartella numeric(3) NULL,
	codice_entrata varchar(4) NULL,
	tipo_entrata varchar(1) NULL,
	codice_fiscale varchar(16) NULL,
	data_evento date NULL,
	importo_carico_risco numeric(15, 2) NULL,
	importo_interessi numeric(15) NULL,
	importo_aggio_ente numeric(15) NULL,
	importo_aggio_contrib numeric(15) NULL,
	t_spese_proc_esec numeric(15) NULL,
	t_spese_proc_esec_p_lista numeric(15) NULL,
	codice_divisa varchar(3) NULL,
	modalita_pagam varchar(1) NULL,
	data_registr date NULL,
	num_operaz_contabile varchar(25) NULL,
	progr_inter_op_contab varchar(3) NULL,
	note varchar(200) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_ruolo_soris_fr3 PRIMARY KEY (id_ruolo_soris_fr3),
	CONSTRAINT fk_risca_r_ruolo_02 FOREIGN KEY (id_ruolo) REFERENCES risca_r_ruolo(id_ruolo),
	CONSTRAINT fk_risca_t_file_soris_03 FOREIGN KEY (id_file_soris) REFERENCES risca_t_file_soris(id_file_soris),
	CONSTRAINT fk_risca_t_pagamento_03 FOREIGN KEY (id_pagamento) REFERENCES risca_t_pagamento(id_pagamento)
);
COMMENT ON TABLE risca_r_ruolo_soris_fr3 IS 'Permette di raccogliere i dati riferiti al tipo record FR3, comunicati da SORIS rispetto ai ruoli inviati precedentemente con il relativo file 450';

-- Column comments

COMMENT ON COLUMN risca_r_ruolo_soris_fr3.id_ruolo_soris_fr3 IS 'identificativo univoco';
COMMENT ON COLUMN risca_r_ruolo_soris_fr3.id_ruolo IS 'identificativo del ruolo a cui e'' associato il dato Soris. null nel caso in cui non è stata trovata la corrispondenza nei ruoli risca';
COMMENT ON COLUMN risca_r_ruolo_soris_fr3.id_file_soris IS 'permette di identificare il file Soris che ha comunicato il dato';
COMMENT ON COLUMN risca_r_ruolo_soris_fr3.tipo_record IS 'tipologia del record inviato da Soris';
COMMENT ON COLUMN risca_r_ruolo_soris_fr3.utenza IS 'estratto dal campo : p_ident_atto';


-- risca_r_ruolo_soris_fr7 definition

-- Drop table

-- DROP TABLE risca_r_ruolo_soris_fr7;

CREATE TABLE risca_r_ruolo_soris_fr7 (
	id_ruolo_soris_fr7 int4 NOT NULL, -- identificativo univoco
	id_ruolo int4 NULL, -- identificativo del ruolo a cui e' associato il dato Soris. null nel caso in cui non è stata trovata la corrispondenza nei ruoli risca
	id_file_soris int4 NOT NULL, -- permette di identificare il file Soris che ha comunicato il dato,
	id_pagamento int4 NULL,
	tipo_record varchar(3) NULL, -- tipologia del record inviato da Soris
	utenza varchar(7) NOT NULL, -- estratto dal campo : chiave_inf_annullare
	anno_ruolo varchar(4) NULL, -- estratto dal campo : chiave_inf_annullare
	numero_ruolo varchar(6) NULL, -- estratto dal campo : chiave_inf_annullare
	p_tipo_ufficio varchar(1) NULL, -- estratto dal campo : chiave_inf_annullare
	p_codice_ufficio varchar(6) NULL, -- estratto dal campo : chiave_inf_annullare
	p_anno_riferimento varchar(4) NULL, -- estratto dal campo : chiave_inf_annullare
	p_ident_prenot_ruolo varchar(34) NULL, -- estratto dal campo : chiave_inf_annullare
	progr_record numeric(7) NULL,
	cod_ambito varchar(3) NULL,
	cod_ente_creditore varchar(5) NULL,
	chiave_inf_annullare varchar(153) NULL,
	chiave_inf_correttiva varchar(153) NULL,
	tipo_evento varchar(1) NULL,
	motivo_rich_annul varchar(1) NULL,
	ente_richiedente varchar(5) NULL,
	data_richiesta date NULL,
	note varchar(200) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_ruolo_soris_fr7 PRIMARY KEY (id_ruolo_soris_fr7),
	CONSTRAINT fk_risca_r_ruolo_03 FOREIGN KEY (id_ruolo) REFERENCES risca_r_ruolo(id_ruolo),
	CONSTRAINT fk_risca_t_file_soris_04 FOREIGN KEY (id_file_soris) REFERENCES risca_t_file_soris(id_file_soris),
	CONSTRAINT fk_risca_t_pagamento_04 FOREIGN KEY (id_pagamento) REFERENCES risca_t_pagamento(id_pagamento)
);
COMMENT ON TABLE risca_r_ruolo_soris_fr7 IS 'Permette di raccogliere i dati riferiti al tipo record FR7, comunicati da SORIS rispetto ai ruoli inviati precedentemente con il relativo file 450';

-- Column comments

COMMENT ON COLUMN risca_r_ruolo_soris_fr7.id_ruolo_soris_fr7 IS 'identificativo univoco';
COMMENT ON COLUMN risca_r_ruolo_soris_fr7.id_ruolo IS 'identificativo del ruolo a cui e'' associato il dato Soris. null nel caso in cui non è stata trovata la corrispondenza nei ruoli risca';
COMMENT ON COLUMN risca_r_ruolo_soris_fr7.id_file_soris IS 'permette di identificare il file Soris che ha comunicato il dato';
COMMENT ON COLUMN risca_r_ruolo_soris_fr7.tipo_record IS 'tipologia del record inviato da Soris';
COMMENT ON COLUMN risca_r_ruolo_soris_fr7.utenza IS 'estratto dal campo : chiave_inf_annullare';
COMMENT ON COLUMN risca_r_ruolo_soris_fr7.anno_ruolo IS 'estratto dal campo : chiave_inf_annullare';
COMMENT ON COLUMN risca_r_ruolo_soris_fr7.numero_ruolo IS 'estratto dal campo : chiave_inf_annullare';
COMMENT ON COLUMN risca_r_ruolo_soris_fr7.p_tipo_ufficio IS 'estratto dal campo : chiave_inf_annullare';
COMMENT ON COLUMN risca_r_ruolo_soris_fr7.p_codice_ufficio IS 'estratto dal campo : chiave_inf_annullare';
COMMENT ON COLUMN risca_r_ruolo_soris_fr7.p_anno_riferimento IS 'estratto dal campo : chiave_inf_annullare';
COMMENT ON COLUMN risca_r_ruolo_soris_fr7.p_ident_prenot_ruolo IS 'estratto dal campo : chiave_inf_annullare';


-- risca_r_soll_dati_amministr definition

-- Drop table

-- DROP TABLE risca_r_soll_dati_amministr;

CREATE TABLE risca_r_soll_dati_amministr (
	id_soll_dati_amministr int4 NOT NULL, -- identificativo univoco
	id_accertamento int4 NOT NULL,
	codice_utente varchar(10) NULL,
	codice_utenza varchar(10) NULL,
	tipo_sollecito varchar(30) NULL,
	tipo_titolo varchar(20) NULL,
	num_titolo varchar(20) NULL,
	data_titolo date NULL,
	corpo_idrico varchar(200) NULL,
	comune_di_presa varchar(200) NULL,
	annualita_pagamento numeric(4) NULL,
	anno_rich_pagamento varchar(30) NULL,
	scadenza_pagamento varchar(20) NULL,
	motivo_soll varchar(100) NULL,
	canone_dovuto numeric(9, 2) NULL,
	importo_versato numeric(9, 2) NULL,
	importo_mancante numeric(9, 2) NULL,
	interessi_mancanti numeric(9, 2) NULL,
	spese_notifica numeric(9, 2) NULL,
	interessi_teorici numeric(9, 2) NULL,
	interessi_versati numeric(9, 2) NULL,
	dilazione varchar(1) NULL,
	codice_avviso varchar(35) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT chk_risca_r_soll_dati_amministr_01 CHECK (((dilazione)::text = ANY (ARRAY[('S'::character varying)::text, ('N'::character varying)::text]))),
	CONSTRAINT pk_risca_r_soll_dati_amministr PRIMARY KEY (id_soll_dati_amministr),
	CONSTRAINT fk_risca_t_accertamento_01 FOREIGN KEY (id_accertamento) REFERENCES risca_t_accertamento(id_accertamento)
);
COMMENT ON TABLE risca_r_soll_dati_amministr IS 'Permette di raccogliere i dati amministrativi del relativo accertamento.
(avviso bonario o sollecito di pagamento).';

-- Column comments

COMMENT ON COLUMN risca_r_soll_dati_amministr.id_soll_dati_amministr IS 'identificativo univoco';


-- risca_r_soll_dati_pagopa definition

-- Drop table

-- DROP TABLE risca_r_soll_dati_pagopa;

CREATE TABLE risca_r_soll_dati_pagopa (
	id_soll_dati_pagopa int4 NOT NULL, -- identificativo univoco
	id_accertamento int4 NOT NULL,
	codice_utente varchar(10) NULL,
	codice_utenza varchar(10) NULL,
	iuv varchar(35) NULL,
	codice_avviso varchar(35) NULL,
	scadenza_pagamento varchar(20) NULL,
	importo_da_versare numeric(11, 2) NULL,
	nap varchar(20) NULL,
	nome_titolare_ind_post varchar(100) NULL,
	presso_ind_post varchar(150) NULL,
	indirizzo_ind_post varchar(80) NULL,
	cap_ind_post varchar(5) NULL,
	comune_ind_post varchar(50) NULL,
	prov_ind_post varchar(2) NULL,
	pec_email varchar(150) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_soll_dati_pagopa PRIMARY KEY (id_soll_dati_pagopa),
	CONSTRAINT fk_risca_t_accertamento_02 FOREIGN KEY (id_accertamento) REFERENCES risca_t_accertamento(id_accertamento)
);
COMMENT ON TABLE risca_r_soll_dati_pagopa IS 'Permette di raccogliere i dati di pagopa del relativo accertamento.
(avviso bonario o sollecito di pagamento).';

-- Column comments

COMMENT ON COLUMN risca_r_soll_dati_pagopa.id_soll_dati_pagopa IS 'identificativo univoco';


-- risca_r_soll_dati_titolare definition

-- Drop table

-- DROP TABLE risca_r_soll_dati_titolare;

CREATE TABLE risca_r_soll_dati_titolare (
	id_soll_dati_titolare int4 NOT NULL, -- identificativo univoco
	id_accertamento int4 NOT NULL,
	id_tipo_invio int4 NULL,
	nome_titolare_ind_post varchar(150) NULL,
	codice_fiscale_calc varchar(16) NULL,
	codice_fiscale_eti_calc varchar(16) NULL,
	prov_ind_post varchar(2) NULL,
	presso_ind_post varchar(150) NULL,
	indirizzo_ind_post varchar(80) NULL,
	comune_ind_post varchar(50) NULL,
	num_prot varchar(25) NULL,
	data_prot date NULL,
	scadenza_soll varchar(25) NULL,
	pec_email varchar(150) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	id_titolare varchar(50) NULL, -- Contiene il riferimento al titolare.¶Se presente un indirizzo alternativo nella riscossione, riporta l'id_soggetto + 'R' + id_recapito¶altrimenti solo l'id_soggetto
	id_soggetto int4 NOT NULL, -- la procedura lo valorizza, ma non lo usa.¶è indispensabile per recuperare i dati del soggetto, per l'archiviazione su Acta
	CONSTRAINT pk_risca_r_soll_dati_titolare PRIMARY KEY (id_soll_dati_titolare),
	CONSTRAINT fk_risca_d_tipo_invio_02 FOREIGN KEY (id_tipo_invio) REFERENCES risca_d_tipo_invio(id_tipo_invio),
	CONSTRAINT fk_risca_t_accertamento_03 FOREIGN KEY (id_accertamento) REFERENCES risca_t_accertamento(id_accertamento)
);
COMMENT ON TABLE risca_r_soll_dati_titolare IS 'Permette di raccogliere i dati del titolare del relativo accertamento.
(avviso bonario o sollecito di pagamento).';

-- Column comments

COMMENT ON COLUMN risca_r_soll_dati_titolare.id_soll_dati_titolare IS 'identificativo univoco';
COMMENT ON COLUMN risca_r_soll_dati_titolare.id_titolare IS 'Contiene il riferimento al titolare.
Se presente un indirizzo alternativo nella riscossione, riporta l''id_soggetto + ''R'' + id_recapito
altrimenti solo l''id_soggetto';
COMMENT ON COLUMN risca_r_soll_dati_titolare.id_soggetto IS 'la procedura lo valorizza, ma non lo usa.
è indispensabile per recuperare i dati del soggetto, per l''archiviazione su Acta';


-- risca_r_soll_destinatari definition

-- Drop table

-- DROP TABLE risca_r_soll_destinatari;

CREATE TABLE risca_r_soll_destinatari (
	id_soll_destinatari int4 NOT NULL, -- identificativo univoco
	id_accertamento int4 NOT NULL,
	codice_utenza varchar(10) NULL,
	scadenza_pagamento varchar(20) NULL,
	codice_utente_tit varchar(20) NOT NULL,
	codice_utente_dest varchar(20) NOT NULL,
	rag_soc_cogn varchar(100) NULL,
	codice_fiscale_calc varchar(16) NULL,
	codice_fiscale_eti_calc varchar(16) NULL,
	presso_ind_post varchar(150) NULL,
	indirizzo_ind_post varchar(80) NULL,
	comune_ind_post varchar(50) NULL,
	prov_ind_post varchar(2) NULL,
	num_prot varchar(25) NULL,
	data_prot date NULL,
	scadenza_soll varchar(25) NULL,
	rag_soc_cogn_ru varchar(50) NULL,
	nome_ru varchar(50) NULL,
	sesso_ru varchar(1) NULL,
	data_nascita_ru date NULL,
	luogo_nascita_ru varchar(50) NULL,
	prov_nascita_ru varchar(2) NULL,
	cap_ru varchar(5) NULL,
	comune_ru varchar(50) NULL,
	prov_ru varchar(2) NULL,
	indirizzo_civ_ru varchar(50) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_soll_destinatari PRIMARY KEY (id_soll_destinatari),
	CONSTRAINT fk_risca_t_accertamento_04 FOREIGN KEY (id_accertamento) REFERENCES risca_t_accertamento(id_accertamento)
);
CREATE UNIQUE INDEX ak_risca_r_soll_destinatari_01 ON risca_r_soll_destinatari USING btree (id_accertamento, codice_utente_tit, codice_utente_dest);
COMMENT ON TABLE risca_r_soll_destinatari IS 'Permette di raccogliere i dati destinatari del relativo accertamento.
(avviso bonario o sollecito di pagamento).';

-- Column comments

COMMENT ON COLUMN risca_r_soll_destinatari.id_soll_destinatari IS 'identificativo univoco';


-- risca_r_soll_dett_vers definition

-- Drop table

-- DROP TABLE risca_r_soll_dett_vers;

CREATE TABLE risca_r_soll_dett_vers (
	id_soll_dett_vers int4 NOT NULL, -- identificativo univoco
	id_accertamento int4 NOT NULL,
	codice_utenza varchar(10) NULL,
	scadenza_pagamento varchar(20) NOT NULL,
	importo_versato numeric(9, 2) NULL,
	data_versamento date NULL,
	interessi_maturati numeric(9, 2) NULL,
	giorni_ritardo numeric(6) NULL,
	gest_data_ins timestamp NOT NULL,
	gest_attore_ins varchar(30) NOT NULL,
	gest_data_upd timestamp NOT NULL,
	gest_attore_upd varchar(30) NOT NULL,
	gest_uid varchar(64) NULL,
	CONSTRAINT pk_risca_r_soll_dett_vers PRIMARY KEY (id_soll_dett_vers),
	CONSTRAINT fk_risca_t_accertamento_05 FOREIGN KEY (id_accertamento) REFERENCES risca_t_accertamento(id_accertamento)
);
COMMENT ON TABLE risca_r_soll_dett_vers IS 'Permette di raccogliere i dati del dettaglio di versamento del relativo accertamento.
(avviso bonario o sollecito di pagamento).';

-- Column comments

COMMENT ON COLUMN risca_r_soll_dett_vers.id_soll_dett_vers IS 'identificativo univoco';



ALTER TABLE csi_log_audit                           OWNER TO risca;
ALTER TABLE risca_d_accerta_bilancio                OWNER TO risca;
ALTER TABLE risca_d_algoritmo                       OWNER TO risca;
ALTER TABLE risca_d_ambito                          OWNER TO risca;
ALTER TABLE risca_d_attivita_stato_deb              OWNER TO risca;
ALTER TABLE risca_d_componente_dt                   OWNER TO risca;
ALTER TABLE risca_d_comune                          OWNER TO risca;
ALTER TABLE risca_d_configurazione                  OWNER TO risca;
ALTER TABLE risca_d_continente                      OWNER TO risca;
ALTER TABLE risca_d_dato_tecnico                    OWNER TO risca;
ALTER TABLE risca_d_email_servizio                  OWNER TO risca;
ALTER TABLE risca_d_email_standard                  OWNER TO risca;
ALTER TABLE risca_d_ente_pagopa                     OWNER TO risca;
ALTER TABLE risca_d_fase_elabora                    OWNER TO risca;
ALTER TABLE risca_d_fonte                           OWNER TO risca;
ALTER TABLE risca_d_funzionalita                    OWNER TO risca;
ALTER TABLE risca_d_mappa_fonte_esterna             OWNER TO risca;
ALTER TABLE risca_d_messaggio                       OWNER TO risca;
ALTER TABLE risca_d_nazione                         OWNER TO risca;
ALTER TABLE risca_d_oggetto_app                     OWNER TO risca;
ALTER TABLE risca_d_origine_limiti                  OWNER TO risca;
ALTER TABLE risca_d_output_colonna                  OWNER TO risca;
ALTER TABLE risca_d_output_file                     OWNER TO risca;
ALTER TABLE risca_d_output_foglio                   OWNER TO risca;
ALTER TABLE risca_d_passo_elabora                   OWNER TO risca;
ALTER TABLE risca_d_profilo_pa                      OWNER TO risca;
ALTER TABLE risca_d_provincia                       OWNER TO risca;
ALTER TABLE risca_d_regione                         OWNER TO risca;
ALTER TABLE risca_d_riduzione_aumento               OWNER TO risca;
ALTER TABLE risca_d_sigla_personale                 OWNER TO risca;
ALTER TABLE risca_d_stato_contribuzione             OWNER TO risca;
ALTER TABLE risca_d_stato_elabora                   OWNER TO risca;
ALTER TABLE risca_d_stato_iuv                       OWNER TO risca;
ALTER TABLE risca_d_stato_riscossione               OWNER TO risca;
ALTER TABLE risca_d_tipo_accertamento               OWNER TO risca;
ALTER TABLE risca_d_tipo_area_competenza            OWNER TO risca;
ALTER TABLE risca_d_tipo_attivita_stato_deb         OWNER TO risca;
ALTER TABLE risca_d_tipo_autorizza                  OWNER TO risca;
ALTER TABLE risca_d_tipo_componente_dt              OWNER TO risca;
ALTER TABLE risca_d_tipo_dato_colonna               OWNER TO risca;
ALTER TABLE risca_d_tipo_dato_tecnico               OWNER TO risca;
ALTER TABLE risca_d_tipo_dilazione                  OWNER TO risca;
ALTER TABLE risca_d_tipo_elabora                    OWNER TO risca;
ALTER TABLE risca_d_tipo_imp_non_propri             OWNER TO risca;
ALTER TABLE risca_d_tipo_invio                      OWNER TO risca;
ALTER TABLE risca_d_tipo_messaggio                  OWNER TO risca;
ALTER TABLE risca_d_tipo_modalita_pag               OWNER TO risca;
ALTER TABLE risca_d_tipo_natura_giuridica           OWNER TO risca;
ALTER TABLE risca_d_tipo_oggetto_app                OWNER TO risca;
ALTER TABLE risca_d_tipo_passo_elabora              OWNER TO risca;
ALTER TABLE risca_d_tipo_personale                  OWNER TO risca;
ALTER TABLE risca_d_tipo_provvedimento              OWNER TO risca;
ALTER TABLE risca_d_tipo_recapito                   OWNER TO risca;
ALTER TABLE risca_d_tipo_ricerca_morosita           OWNER TO risca;
ALTER TABLE risca_d_tipo_ricerca_pagamento          OWNER TO risca;
ALTER TABLE risca_d_tipo_ricerca_rimborso           OWNER TO risca;
ALTER TABLE risca_d_tipo_rimborso                   OWNER TO risca;
ALTER TABLE risca_d_tipo_riscossione                OWNER TO risca;
ALTER TABLE risca_d_tipo_sede                       OWNER TO risca;
ALTER TABLE risca_d_tipo_soggetto                   OWNER TO risca;
ALTER TABLE risca_d_tipo_spedizione                 OWNER TO risca;
ALTER TABLE risca_d_tipo_titolo                     OWNER TO risca;
ALTER TABLE risca_d_tipo_uso                        OWNER TO risca;
ALTER TABLE risca_d_tipologia_pag                   OWNER TO risca;
ALTER TABLE risca_d_unita_misura                    OWNER TO risca;
ALTER TABLE risca_r_ambito_area_competenza          OWNER TO risca;
ALTER TABLE risca_r_ambito_config                   OWNER TO risca;
ALTER TABLE risca_r_ambito_fonte                    OWNER TO risca;
ALTER TABLE risca_r_ambito_interesse                OWNER TO risca;
ALTER TABLE risca_r_ambito_provincia                OWNER TO risca;
ALTER TABLE risca_r_annualita_sd                    OWNER TO risca;
ALTER TABLE risca_r_annualita_uso_sd                OWNER TO risca;
ALTER TABLE risca_r_annualita_uso_sd_ra             OWNER TO risca;
ALTER TABLE risca_r_avviso_annualita                OWNER TO risca;
ALTER TABLE risca_r_avviso_dati_ammin               OWNER TO risca;
ALTER TABLE risca_r_avviso_dati_titolare            OWNER TO risca;
ALTER TABLE risca_r_avviso_uso                      OWNER TO risca;
ALTER TABLE risca_r_dettaglio_pag                   OWNER TO risca;
ALTER TABLE risca_r_dettaglio_pag_bil_acc           OWNER TO risca;
ALTER TABLE risca_r_dt_valore_ammesso               OWNER TO risca;
ALTER TABLE risca_r_email_st_punti_valori           OWNER TO risca;
ALTER TABLE risca_r_email_st_segnaposto             OWNER TO risca;
ALTER TABLE risca_r_etichetta                       OWNER TO risca;
ALTER TABLE risca_r_funzionalita_stato              OWNER TO risca;
ALTER TABLE risca_r_gruppo_delega                   OWNER TO risca;
ALTER TABLE risca_r_iuv_da_inviare                  OWNER TO risca;
ALTER TABLE risca_r_pag_non_propri                  OWNER TO risca;
ALTER TABLE risca_r_pagopa_lista_car_iuv            OWNER TO risca;
ALTER TABLE risca_r_pagopa_lista_carico             OWNER TO risca;
ALTER TABLE risca_r_pagopa_scomp_rich_iuv           OWNER TO risca;
ALTER TABLE risca_r_pagopa_scomp_var_iuv            OWNER TO risca;
ALTER TABLE risca_r_parametro_elabora               OWNER TO risca;
ALTER TABLE risca_r_profilo_ogg_app                 OWNER TO risca;
ALTER TABLE risca_r_provvedimento                   OWNER TO risca;
ALTER TABLE risca_r_rata_sd                         OWNER TO risca;
ALTER TABLE risca_r_recapito                        OWNER TO risca;
ALTER TABLE risca_r_recapito_postel                 OWNER TO risca;
ALTER TABLE risca_r_registro_elabora                OWNER TO risca;
ALTER TABLE risca_r_rimborso                        OWNER TO risca;
ALTER TABLE risca_r_rimborso_sd_utilizzato          OWNER TO risca;
ALTER TABLE risca_r_riscossione_recapito            OWNER TO risca;
ALTER TABLE risca_r_riscossione_storia_titolare     OWNER TO risca;
ALTER TABLE risca_r_riscossione_uso                 OWNER TO risca;
ALTER TABLE risca_r_ruolo                           OWNER TO risca;
ALTER TABLE risca_r_ruolo_soris_fr1                 OWNER TO risca;
ALTER TABLE risca_r_ruolo_soris_fr3                 OWNER TO risca;
ALTER TABLE risca_r_ruolo_soris_fr7                 OWNER TO risca;
ALTER TABLE risca_r_soggetto_delega                 OWNER TO risca;
ALTER TABLE risca_r_soggetto_gruppo                 OWNER TO risca;
ALTER TABLE risca_r_soll_dati_amministr             OWNER TO risca;
ALTER TABLE risca_r_soll_dati_pagopa                OWNER TO risca;
ALTER TABLE risca_r_soll_dati_titolare              OWNER TO risca;
ALTER TABLE risca_r_soll_destinatari                OWNER TO risca;
ALTER TABLE risca_r_soll_dett_vers                  OWNER TO risca;
ALTER TABLE risca_r_spedizione_acta                 OWNER TO risca;
ALTER TABLE risca_r_terna_prod_energ                OWNER TO risca;
ALTER TABLE risca_r_tipo_ricerca_mor_accerta        OWNER TO risca;
ALTER TABLE risca_r_tipo_sede_accertamento          OWNER TO risca;
ALTER TABLE risca_r_tipo_uso_dt                     OWNER TO risca;
ALTER TABLE risca_r_tipo_uso_regola                 OWNER TO risca;
ALTER TABLE risca_r_tipo_uso_ridaum                 OWNER TO risca;
ALTER TABLE risca_r_uso_ridaum                      OWNER TO risca;
ALTER TABLE risca_s_comune                          OWNER TO risca;
ALTER TABLE risca_s_nazione                         OWNER TO risca;
ALTER TABLE risca_s_provincia                       OWNER TO risca;
ALTER TABLE risca_s_regione                         OWNER TO risca;
ALTER TABLE risca_s_tracciamento                    OWNER TO risca;
ALTER TABLE risca_t_accertamento                    OWNER TO risca;
ALTER TABLE risca_t_avviso_pagamento                OWNER TO risca;
ALTER TABLE risca_t_bil_acc                         OWNER TO risca;
ALTER TABLE risca_t_delegato                        OWNER TO risca;
ALTER TABLE risca_t_elabora                         OWNER TO risca;
ALTER TABLE risca_t_file_450                        OWNER TO risca;
ALTER TABLE risca_t_file_poste                      OWNER TO risca;
ALTER TABLE risca_t_file_soris                      OWNER TO risca;
ALTER TABLE risca_t_gruppo_soggetto                 OWNER TO risca;
ALTER TABLE risca_t_immagine                        OWNER TO risca;
ALTER TABLE risca_t_invio_acta                      OWNER TO risca;
ALTER TABLE risca_t_iuv                             OWNER TO risca;
ALTER TABLE risca_t_lotto                           OWNER TO risca;
ALTER TABLE risca_t_pagamento                       OWNER TO risca;
ALTER TABLE risca_t_riscossione                     OWNER TO risca;
ALTER TABLE risca_t_soggetto                        OWNER TO risca;
ALTER TABLE risca_t_spedizione                      OWNER TO risca;
ALTER TABLE risca_t_stato_debitorio                 OWNER TO risca;
ALTER TABLE risca_t_terna_prezzi_energ              OWNER TO risca;
ALTER TABLE risca_t_terna_utenze                    OWNER TO risca;
ALTER TABLE risca_w_accertamento                    OWNER TO risca;
ALTER TABLE risca_w_annualita_sd                    OWNER TO risca;
ALTER TABLE risca_w_annualita_uso_sd                OWNER TO risca;
ALTER TABLE risca_w_annualita_uso_sd_ra             OWNER TO risca;
ALTER TABLE risca_w_avviso_annualita                OWNER TO risca;
ALTER TABLE risca_w_avviso_dati_ammin               OWNER TO risca;
ALTER TABLE risca_w_avviso_dati_titolare            OWNER TO risca;
ALTER TABLE risca_w_avviso_pagamento                OWNER TO risca;
ALTER TABLE risca_w_avviso_uso                      OWNER TO risca;
ALTER TABLE risca_w_lock_riscossione                OWNER TO risca;
ALTER TABLE risca_w_output_dati                     OWNER TO risca;
ALTER TABLE risca_w_pagopa_scomp_rich_iuv           OWNER TO risca;
ALTER TABLE risca_w_pagopa_scomp_var_iuv            OWNER TO risca;
ALTER TABLE risca_w_rata_sd                         OWNER TO risca;
ALTER TABLE risca_w_rimborso_sd_utilizzato          OWNER TO risca;
ALTER TABLE risca_w_rimborso_upd                    OWNER TO risca;
ALTER TABLE risca_w_rp_estrco                       OWNER TO risca;
ALTER TABLE risca_w_rp_nonpremarcati                OWNER TO risca;
ALTER TABLE risca_w_rp_pagopa                       OWNER TO risca;
ALTER TABLE risca_w_soll_dati_amministr             OWNER TO risca;
ALTER TABLE risca_w_soll_dati_pagopa                OWNER TO risca;
ALTER TABLE risca_w_soll_dati_titolare              OWNER TO risca;
ALTER TABLE risca_w_soll_destinatari                OWNER TO risca;
ALTER TABLE risca_w_soll_dett_vers                  OWNER TO risca;
ALTER TABLE risca_w_soris_00c                       OWNER TO risca;
ALTER TABLE risca_w_soris_99c                       OWNER TO risca;
ALTER TABLE risca_w_soris_fr0                       OWNER TO risca;
ALTER TABLE risca_w_soris_fr1                       OWNER TO risca;
ALTER TABLE risca_w_soris_fr2                       OWNER TO risca;
ALTER TABLE risca_w_soris_fr3                       OWNER TO risca;
ALTER TABLE risca_w_soris_fr3_pag_da_inserire       OWNER TO risca;
ALTER TABLE risca_w_soris_fr3_r_pag_sd              OWNER TO risca;
ALTER TABLE risca_w_soris_fr7                       OWNER TO risca;
ALTER TABLE risca_w_spedizione                      OWNER TO risca;
ALTER TABLE risca_w_stato_debitorio                 OWNER TO risca;
ALTER TABLE risca_w_stato_debitorio_upd             OWNER TO risca;
ALTER TABLE risca_w_terna_prezzi_energ              OWNER TO risca;
ALTER TABLE risca_w_terna_prod_energ                OWNER TO risca;
ALTER TABLE risca_w_terna_prod_energ_xls            OWNER TO risca;
ALTER TABLE risca_w_terna_utenze                    OWNER TO risca;


GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE csi_log_audit                           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_accerta_bilancio                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_algoritmo                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_ambito                          TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_attivita_stato_deb              TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_componente_dt                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_comune                          TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_configurazione                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_continente                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_dato_tecnico                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_email_servizio                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_email_standard                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_ente_pagopa                     TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_fase_elabora                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_fonte                           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_funzionalita                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_mappa_fonte_esterna             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_messaggio                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_nazione                         TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_oggetto_app                     TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_origine_limiti                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_output_colonna                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_output_file                     TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_output_foglio                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_passo_elabora                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_profilo_pa                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_provincia                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_regione                         TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_riduzione_aumento               TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_sigla_personale                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_stato_contribuzione             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_stato_elabora                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_stato_iuv                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_stato_riscossione               TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_accertamento               TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_area_competenza            TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_attivita_stato_deb         TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_autorizza                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_componente_dt              TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_dato_colonna               TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_dato_tecnico               TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_dilazione                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_elabora                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_imp_non_propri             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_invio                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_messaggio                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_modalita_pag               TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_natura_giuridica           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_oggetto_app                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_passo_elabora              TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_personale                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_provvedimento              TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_recapito                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_ricerca_morosita           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_ricerca_pagamento          TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_ricerca_rimborso           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_rimborso                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_riscossione                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_sede                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_soggetto                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_spedizione                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_titolo                     TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipo_uso                        TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_tipologia_pag                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_d_unita_misura                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_ambito_area_competenza          TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_ambito_config                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_ambito_fonte                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_ambito_interesse                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_ambito_provincia                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_annualita_sd                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_annualita_uso_sd                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_annualita_uso_sd_ra             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_avviso_annualita                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_avviso_dati_ammin               TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_avviso_dati_titolare            TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_avviso_uso                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_dettaglio_pag                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_dettaglio_pag_bil_acc           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_dt_valore_ammesso               TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_email_st_punti_valori           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_email_st_segnaposto             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_etichetta                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_funzionalita_stato              TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_gruppo_delega                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_iuv_da_inviare                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_pag_non_propri                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_pagopa_lista_car_iuv            TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_pagopa_lista_carico             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_pagopa_scomp_rich_iuv           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_pagopa_scomp_var_iuv            TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_parametro_elabora               TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_profilo_ogg_app                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_provvedimento                   TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_rata_sd                         TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_recapito                        TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_recapito_postel                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_registro_elabora                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_rimborso                        TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_rimborso_sd_utilizzato          TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_riscossione_recapito            TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_riscossione_storia_titolare     TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_riscossione_uso                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_ruolo                           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_ruolo_soris_fr1                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_ruolo_soris_fr3                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_ruolo_soris_fr7                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_soggetto_delega                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_soggetto_gruppo                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_soll_dati_amministr             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_soll_dati_pagopa                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_soll_dati_titolare              TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_soll_destinatari                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_soll_dett_vers                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_spedizione_acta                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_terna_prod_energ                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_tipo_ricerca_mor_accerta        TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_tipo_sede_accertamento          TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_tipo_uso_dt                     TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_tipo_uso_regola                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_tipo_uso_ridaum                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_r_uso_ridaum                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_s_comune                          TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_s_nazione                         TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_s_provincia                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_s_regione                         TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_s_tracciamento                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_accertamento                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_avviso_pagamento                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_bil_acc                         TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_delegato                        TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_elabora                         TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_file_450                        TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_file_poste                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_file_soris                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_gruppo_soggetto                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_immagine                        TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_invio_acta                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_iuv                             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_lotto                           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_pagamento                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_riscossione                     TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_soggetto                        TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_spedizione                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_stato_debitorio                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_terna_prezzi_energ              TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_t_terna_utenze                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_accertamento                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_annualita_sd                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_annualita_uso_sd                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_annualita_uso_sd_ra             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_avviso_annualita                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_avviso_dati_ammin               TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_avviso_dati_titolare            TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_avviso_pagamento                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_avviso_uso                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_lock_riscossione                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_output_dati                     TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_pagopa_scomp_rich_iuv           TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_pagopa_scomp_var_iuv            TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_rata_sd                         TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_rimborso_sd_utilizzato          TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_rimborso_upd                    TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_rp_estrco                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_rp_nonpremarcati                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_rp_pagopa                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soll_dati_amministr             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soll_dati_pagopa                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soll_dati_titolare              TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soll_destinatari                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soll_dett_vers                  TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soris_00c                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soris_99c                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soris_fr0                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soris_fr1                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soris_fr2                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soris_fr3                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soris_fr3_pag_da_inserire       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soris_fr3_r_pag_sd              TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_soris_fr7                       TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_spedizione                      TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_stato_debitorio                 TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_stato_debitorio_upd             TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_terna_prezzi_energ              TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_terna_prod_energ                TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_terna_prod_energ_xls            TO risca_rw;
GRANT UPDATE, INSERT, SELECT, DELETE ON TABLE risca_w_terna_utenze                    TO risca_rw;
