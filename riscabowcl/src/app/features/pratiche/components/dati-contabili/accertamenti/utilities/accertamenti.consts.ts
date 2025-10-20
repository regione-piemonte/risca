/**
 * Interfaccia strettamente collegata alla costante: IAccertamentiConsts.
 */
export interface IAccertamentiConsts {
  TIPO_ATTIVITA: string;
  PROPERTY_TIPO_ATTIVITA_ACCERTAMENTO: string;
  TITLE_ACCERTAMENTI: string;
  NUOVO_ACCERTAMENTO: string;
  ACCERTAMENTI: string;

  PROPERTY_DES_ATTIVITA_STATO_DEB: string;

  LABEL_NUM_PROTOCOLLO: string;

  LABEL_DATA_PROTOCOLLO: string;
  LABEL_DATA_SCADENZA: string;
  LABEL_DATA_NOTIFICA: string;
  LABEL_TIPO_ACCERTAMENTO: string;
  LABEL_FLG_RESTITUITO: string;
  LABEL_FLG_ANNULLATO: string;
  LABEL_NOTE: string;

  NUM_PROTOCOLLO: string;
  DATA_PROTOCOLLO: string;
  DATA_SCADENZA: string;
  DATA_NOTIFICA: string;
  TIPO_ACCERTAMENTO: string;
  FLG_RESTITUITO: string;
  FLG_ANNULLATO: string;
  NOTE: string;

  FORM_KEY_PARENT_ACCERTAMENTI: string;
  FORM_KEY_CHILD_ACCERTAMENTI: string;

  TITLE_STATI_DEBITORI_COLLEGATI: string;
  STATI_DEBITORI_COLLEGATI: string;

  LABEL_ANNULLA: string;
}

/**
 * Costante che rappresenta le funzionalità presenti su db per la parte dei pagamenti.
 */
export const AccertamentiConsts: IAccertamentiConsts = {
  TIPO_ATTIVITA: 'attivita_stato_deb',
  PROPERTY_TIPO_ATTIVITA_ACCERTAMENTO: 'des_tipo_accertamento',
  TITLE_ACCERTAMENTI: 'Accertamenti',
  NUOVO_ACCERTAMENTO: 'Nuovo accertamento',
  ACCERTAMENTI: 'accertamenti',

  PROPERTY_DES_ATTIVITA_STATO_DEB: 'des_attivita_stato_deb',

  LABEL_NUM_PROTOCOLLO: '*Numero protocollo',
  LABEL_DATA_PROTOCOLLO: '*Data protocollo',
  LABEL_DATA_SCADENZA: 'Data scadenza',
  LABEL_DATA_NOTIFICA: 'Data notifica',
  LABEL_TIPO_ACCERTAMENTO: '*Tipo accertamento',
  LABEL_FLG_RESTITUITO: 'Restituito al mittente',
  LABEL_FLG_ANNULLATO: 'Annullato',
  LABEL_NOTE: 'Note',

  NUM_PROTOCOLLO: 'num_protocollo',
  DATA_PROTOCOLLO: 'data_protocollo',
  DATA_SCADENZA: 'data_scadenza',
  DATA_NOTIFICA: 'data_notifica',
  TIPO_ACCERTAMENTO: 'tipo_accertamento',
  FLG_RESTITUITO: 'flg_restituito',
  FLG_ANNULLATO: 'flg_annullato',
  NOTE: 'nota',

  FORM_KEY_PARENT_ACCERTAMENTI: 'datiContabili',
  FORM_KEY_CHILD_ACCERTAMENTI: 'rimborsi',

  TITLE_STATI_DEBITORI_COLLEGATI: 'Stati debitori collegati',
  STATI_DEBITORI_COLLEGATI: 'statiDebitoriCollegati',

  LABEL_ANNULLA: 'ANNULLA',
};
