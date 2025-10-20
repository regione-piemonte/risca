/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente generali-amministrativi.
 */
interface IGeneraliAmministrativiConsts {
  COD_OPERE_PUBBLICHE: string;
  TIPOLOGIA_PRATICA: string;
  CODICE_UTENZA: string;
  TIPO_TRIBUTO_UTENZA: string;
  PROGRESSIVO_UTENZA: string;
  NUMERO_PRATICA: string;
  STATO_PRATICA: string;
  DATA_RINUNCIA_REVOCA: string;
  PROCEDIMENTO: string;
  STATO_DEBITORIO_INVIO_SPECIALE: string;
  PRENOTATA: string;
  MOTIVAZIONE: string;
  NOTE: string;
  DATA_INIZIO_CONCESSIONE: string;
  DATA_FINE_CONCESSIONE: string;
  DATA_INIZIO_SOSPENSIONE: string;
  DATA_FINE_SOSPENSIONE: string;
  TIPOLOGIA_ISTANZA: string;
  NUMERO_ISTANZA: string;
  DATA_ISTANZA: string;
  NOTE_ISTANZA: string;
  TIPOLOGIA_PROVVEDIMENTO: string;
  TIPO_TITOLO: string;
  NUMERO_PROVVEDIMENTO: string;
  DATA_PROVVEDIMENTO: string;
  NOTE_PROVVEDIMENTO: string;
  ISTANZE_PROVVEDIMENTI: string;

  PROPERTY_TIPOLOGIA_PRATICA: string;
  PROPERTY_CODICE_UTENZA: string;
  PROPERTY_STATO_PRATICA: string;
  PROPERTY_PROCEDIMENTO: string;
  PROPERTY_TIPOLOGIA_ISTANZA: string;
  PROPERTY_TIPOLOGIA_PROVVEDIMENTO: string;
  PROPERTY_TIPO_TITOLO: string;

  LABEL_TIPOLOGIA_PRATICA: string;
  LABEL_CODICE_UTENZA: string;
  LABEL_TIPO_TRIBUTO_UTENZA: string;
  LABEL_PROGRESSIVO_UTENZA: string;
  LABEL_NUMERO_PRATICA: string;
  LABEL_STATO_PRATICA: string;
  LABEL_DATA_RINUNCIA_REVOCA: string;
  LABEL_PROCEDIMENTO: string;
  LABEL_STATO_DEBITORIO_INVIO_SPECIALE: string;
  LABEL_PRENOTATA: string;
  LABEL_MOTIVAZIONE: string;
  LABEL_SUB_MOTIVAZIONE: string;
  LABEL_NOTE: string;
  LABEL_SUB_NOTE: string;
  LABEL_DATA_INIZIO_CONCESSIONE: string;
  LABEL_DATA_FINE_CONCESSIONE: string;
  LABEL_DATA_INIZIO_SOSPENSIONE: string;
  LABEL_DATA_FINE_SOSPENSIONE: string;
  LABEL_TIPOLOGIA_ISTANZA: string;
  LABEL_NUMERO_ISTANZA: string;
  LABEL_DATA_ISTANZA: string;
  LABEL_NOTE_ISTANZA: string;
  LABEL_SUB_NOTE_ISTANZA: string;
  LABEL_TIPOLOGIA_PROVVEDIMENTO: string;
  LABEL_TIPO_TITOLO: string;
  LABEL_NUMERO_PROVVEDIMENTO: string;
  LABEL_NOTE_PROVVEDIMENTO: string;
  LABEL_SUB_NOTE_PROVVEDIMENTO: string;
  LABEL_DATA_PROVVEDIMENTO: string;

  IPT_DATA: string;
  IPT_NUMERO: string;
  IPT_TIPO: string;
  IPT_DESCRIZIONE: string;
  IPT_TIPO_TITOLO: string;
  IPT_NOTE: string;

  ACCORDION_NUOVA_ISTANZA: string;
  ACCORDION_NUOVO_PROVVEDIMENTO: string;
}

/**
 * Oggetto contenente una serie di costanti per il componente generali-amministrativi.
 */
export const GeneraliAmministrativiConsts: IGeneraliAmministrativiConsts = {
  /** Costante che indica il codice delle opere pubbliche. */
  COD_OPERE_PUBBLICHE: 'OOPP',

  /** Costante che indica il campo form: tipologiaPratica. */
  TIPOLOGIA_PRATICA: 'tipologiaPratica',
  /** Costante che indica il campo form: codiceUtenza. */
  CODICE_UTENZA: 'codiceUtenza',
  /** Costante che indica il campo form: tipoTributoUtenza. */
  TIPO_TRIBUTO_UTENZA: 'tipoTributoUtenza',
  /** Costante che indica il campo form: progressivoUtenza. */
  PROGRESSIVO_UTENZA: 'progressivoUtenza',
  /** Costante che indica il campo form: numeroPratica. */
  NUMERO_PRATICA: 'numeroPratica',
  /** Costante che indica il campo form: statoPratica. */
  STATO_PRATICA: 'statoPratica',
  /** Costante che indica il campo form: dataRinunciaRevoca. */
  DATA_RINUNCIA_REVOCA: 'dataRinunciaRevoca',
  /** Costante che indica il campo form: procedimento. */
  PROCEDIMENTO: 'procedimento',
  /** Costante che indica il campo form: statoDebInvioSpeciale. */
  STATO_DEBITORIO_INVIO_SPECIALE: 'statoDebInvioSpeciale',
  /** Costante che indica il campo form: prenotata. */
  PRENOTATA: 'prenotata',
  /** Costante che indica il campo form: motivazione. */
  MOTIVAZIONE: 'motivazione',
  /** Costante che indica il campo form: note. */
  NOTE: 'note',
  /** Costante che indica il campo form: dataInizioConcessione. */
  DATA_INIZIO_CONCESSIONE: 'dataInizioConcessione',
  /** Costante che indica il campo form: dataFineConcessione. */
  DATA_FINE_CONCESSIONE: 'dataFineConcessione',
  /** Costante che indica il campo form: dataInizioSospensione. */
  DATA_INIZIO_SOSPENSIONE: 'dataInizioSospensione',
  /** Costante che indica il campo form: dataFineSospensione. */
  DATA_FINE_SOSPENSIONE: 'dataFineSospensione',
  /** Costante che indica il campo form: tipologiaIstanza. */
  TIPOLOGIA_ISTANZA: 'tipologiaIstanza',
  /** Costante che indica il campo form: numeroIstanza. */
  NUMERO_ISTANZA: 'numeroIstanza',
  /** Costante che indica il campo form: dataIstanza. */
  DATA_ISTANZA: 'dataIstanza',
  /** Costante che indica il campo form: noteIstanza. */
  NOTE_ISTANZA: 'noteIstanza',
  /** Costante che indica il campo form: tipologiaProvvedimento. */
  TIPOLOGIA_PROVVEDIMENTO: 'tipologiaProvvedimento',
  /** Costante che indica il campo form: tipoTitolo. */
  TIPO_TITOLO: 'tipoTitolo',
  /** Costante che indica il campo form: numeroProvvedimento. */
  NUMERO_PROVVEDIMENTO: 'numeroProvvedimento',
  /** Costante che indica il campo form: dataProvvedimento. */
  DATA_PROVVEDIMENTO: 'dataProvvedimento',
  /** Costante che indica il campo form: noteProvvedimento. */
  NOTE_PROVVEDIMENTO: 'noteProvvedimento',
  /** Costante che indica il campo form: istanzeProvvedimenti. */
  ISTANZE_PROVVEDIMENTI: 'istanzeProvvedimenti',

  /** Costante che inidica il nome della proprietà per la select: tipologiaPratica. */
  PROPERTY_TIPOLOGIA_PRATICA: 'des_tipo_riscossione',
  /** Costante che inidica il nome della proprietà per la select: codiceUtenza. */
  PROPERTY_CODICE_UTENZA: 'sigla_provincia',
  /** Costante che inidica il nome della proprietà per la select: statoPratica. */
  PROPERTY_STATO_PRATICA: 'des_stato_riscossione',
  /** Costante che inidica il nome della proprietà per la select: procedimento. */
  PROPERTY_PROCEDIMENTO: 'des_tipo_autorizza',
  /** Costante che inidica il nome della proprietà per la select: tipologiaIstanza. */
  PROPERTY_TIPOLOGIA_ISTANZA: 'des_tipo_provvedimento',
  /** Costante che inidica il nome della proprietà per la select: tipologiaProvvedimento. */
  PROPERTY_TIPOLOGIA_PROVVEDIMENTO: 'des_tipo_provvedimento',
  /** Costante che inidica il nome della proprietà per la select: tipoTitolo. */
  PROPERTY_TIPO_TITOLO: 'des_tipo_titolo',

  /** Costante che indica la label del campo form: tipologiaPratica. */
  LABEL_TIPOLOGIA_PRATICA: '*Tipologia pratica',
  /** Costante che indica la label del campo form: codiceUtenza. */
  LABEL_CODICE_UTENZA: '*Codice utenza',
  /** Costante che indica la label del campo form: tipoTributoUtenza. */
  LABEL_TIPO_TRIBUTO_UTENZA: 'Tipo tributo utenza',
  /** Costante che indica la label del campo form: progressivoUtenza. */
  LABEL_PROGRESSIVO_UTENZA: 'Progressivo utenza',
  /** Costante che indica la label del campo form: numeroPratica. */
  LABEL_NUMERO_PRATICA: '*Numero pratica',
  /** Costante che indica la label del campo form: statoPratica. */
  LABEL_STATO_PRATICA: 'Stato',
  /** Costante che indica la label del campo form: dataRinunciaRevoca. */
  LABEL_DATA_RINUNCIA_REVOCA: 'Data rinuncia/revoca',
  /** Costante che indica la label del campo form: procedimento. */
  LABEL_PROCEDIMENTO: '*Procedimento',
  /** Costante che indica la label del campo form: statoDebInvioSpeciale. */
  LABEL_STATO_DEBITORIO_INVIO_SPECIALE:
    'Almeno uno stato debitorio con un invio speciale',
  /** Costante che indica la label del campo form: prenotata. */
  LABEL_PRENOTATA: 'Prenotata',
  /** Costante che indica la label del campo form: motivazione. */
  LABEL_MOTIVAZIONE: 'Motivazione',
  /** Costante che indica la sub label del campo form: motivazione. */
  LABEL_SUB_MOTIVAZIONE:
    '(Obbligatoria se <strong>"Prenotata"</strong> è selezionata, max 200 caratteri)',
  /** Costante che indica la label del campo form: note. */
  LABEL_NOTE: 'Note',
  /** Costante che indica la sub label del campo form: note. */
  LABEL_SUB_NOTE: '(Max 500 caratteri)',
  /** Costante che indica la label del campo form: dataInizioConcessione. */
  LABEL_DATA_INIZIO_CONCESSIONE: 'Data inizio',
  /** Costante che indica la label del campo form: dataFineConcessione. */
  LABEL_DATA_FINE_CONCESSIONE: 'Scadenza',
  /** Costante che indica la label del campo form: dataInizioSospensione. */
  LABEL_DATA_INIZIO_SOSPENSIONE: 'Data inizio',
  /** Costante che indica la label del campo form: dataFineSospensione. */
  LABEL_DATA_FINE_SOSPENSIONE: 'Scadenza',
  /** Costante che indica la label del campo form: tipologiaIstanza. */
  LABEL_TIPOLOGIA_ISTANZA: '*Descrizione istanza',
  /** Costante che indica la label del campo form: numeroIstanza. */
  LABEL_NUMERO_ISTANZA: 'Numero',
  /** Costante che indica la label del campo form: dataIstanza. */
  LABEL_DATA_ISTANZA: '*Data',
  /** Costante che indica la label del campo form: noteIstanza. */
  LABEL_NOTE_ISTANZA: 'Note',
  /** Costante che indica la side label del campo form: noteIstanza. */
  LABEL_SUB_NOTE_ISTANZA: '(max 500 caratteri)',
  /** Costante che indica la label del campo form: tipologiaProvvedimento. */
  LABEL_TIPOLOGIA_PROVVEDIMENTO: '*Descrizione provvedimento',
  /** Costante che indica la label del campo form: tipoTitolo. */
  LABEL_TIPO_TITOLO: '*Tipo titolo',
  /** Costante che indica la label del campo form: numeroProvvedimento. */
  LABEL_NUMERO_PROVVEDIMENTO: '*Numero',
  /** Costante che indica la label del campo form: dataProvvedimento. */
  LABEL_DATA_PROVVEDIMENTO: '*Data',
  /** Costante che indica la label del campo form: noteIstanza. */
  LABEL_NOTE_PROVVEDIMENTO: 'Note',
  /** Costante che indica la side label del campo form: noteIstanza. */
  LABEL_SUB_NOTE_PROVVEDIMENTO: '(max 500 caratteri)',

  /** Costante per accedere alla proprietà [data] dell'oggetto gestito dalla tabella istanze provvedimenti. */
  IPT_DATA: 'data',
  /** Costante per accedere alla proprietà [numero] dell'oggetto gestito dalla tabella istanze provvedimenti. */
  IPT_NUMERO: 'numero',
  /** Costante per accedere alla proprietà [tipo] dell'oggetto gestito dalla tabella istanze provvedimenti. */
  IPT_TIPO: 'tipo',
  /** Costante per accedere alla proprietà [descrizione] dell'oggetto gestito dalla tabella istanze provvedimenti. */
  IPT_DESCRIZIONE: 'descrizione',
  /** Costante per accedere alla proprietà [tipoTitolo] dell'oggetto gestito dalla tabella istanze provvedimenti. */
  IPT_TIPO_TITOLO: 'tipoTitolo',
  /** Costante per accedere alla proprietà [note] dell'oggetto gestito dalla tabella istanze provvedimenti. */
  IPT_NOTE: 'note',

  /** Costante che definisce la label per l'accordion: nuova istanza. */
  ACCORDION_NUOVA_ISTANZA: 'Nuova istanza',
  /** Costante che definisce la label per l'accordion: nuovo provvedimento. */
  ACCORDION_NUOVO_PROVVEDIMENTO: 'Nuovo provvedimento',
};