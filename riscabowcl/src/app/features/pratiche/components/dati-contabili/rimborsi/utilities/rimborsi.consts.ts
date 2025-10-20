/**
 * Interfaccia strettamente collegata alla costante: IRimborsiConst.
 */
export interface IRimborsiConsts {
  CERCA_CREDITORE_MODAL_TITLE: string;

  TIPO_ATTIVITA: string;
  PROPERTY_TIPO_ATTIVITA_RIMBORSO: string;
  RIMBORSI: string;

  PROPERTY_DES_ATTIVITA_STATO_DEB: string;

  LABEL_NUOVO_RIMBORSO: string;

  LABEL_CODICE_FISCALE: string;
  LABEL_RAGIONE_SOCIALE: string;
  LABEL_ATTIVITA: string;
  LABEL_IMPORTO_ECCEDENTE: string;
  LABEL_NUMERO_PROVVEDIMENTO: string;
  LABEL_DATA_PROVVEDIMENTO: string;
  LABEL_CAUSALE_RIMBORSO: string;

  SOGGETTO: string;
  CODICE_FISCALE: string;
  RAGIONE_SOCIALE: string;
  TIPO_RIMBORSO: string;
  IMPORTO_ECCEDENTE: string;
  NUMERO_PROVVEDIMENTO: string;
  DATA_PROVVEDIMENTO: string;
  CAUSALE_RIMBORSO: string;
  ID_GRUPPO: string;

  FORM_KEY_PARENT_RIMBORSI: string;
  FORM_KEY_CHILD_RIMBORSI: string;

  TITLE_STATI_DEBITORI_COLLEGATI: string;
  STATI_DEBITORI_COLLEGATI: string;

  LABEL_ANNULLA: string;
  LABEL_INSERISCI_CREDITORE: string;

  DESCRIPTION_CERCA_CREDITORE: string;
}

/**
 * Costante che rappresenta le funzionalità presenti su db per la parte dei pagamenti.
 */
export const RimborsiConsts: IRimborsiConsts = {
  CERCA_CREDITORE_MODAL_TITLE: 'Cerca creditore',

  TIPO_ATTIVITA: 'attivita_stato_deb',
  PROPERTY_TIPO_ATTIVITA_RIMBORSO: 'des_tipo_rimborso',
  RIMBORSI: 'rimborsi',
  
  PROPERTY_DES_ATTIVITA_STATO_DEB: 'des_attivita_stato_deb',

  LABEL_NUOVO_RIMBORSO: 'Nuovo Rimborso',
  LABEL_CODICE_FISCALE: '*Codice fiscale',
  LABEL_RAGIONE_SOCIALE: 'Ragione sociale',
  LABEL_ATTIVITA: '*Attività svolta',
  LABEL_IMPORTO_ECCEDENTE: '*Importo eccedente',
  LABEL_NUMERO_PROVVEDIMENTO: '*N. Provvedimento',
  LABEL_DATA_PROVVEDIMENTO: '*Data Provvedimento',
  LABEL_CAUSALE_RIMBORSO: '*Causale rimborso',

  SOGGETTO: 'soggetto',
  CODICE_FISCALE: 'codiceFiscale',
  RAGIONE_SOCIALE: 'ragioneSociale',
  TIPO_RIMBORSO: 'tipoRimborso',
  IMPORTO_ECCEDENTE: 'importoEccedente',
  NUMERO_PROVVEDIMENTO: 'numeroProvvedimento',
  DATA_PROVVEDIMENTO: 'dataProvvedimento',
  CAUSALE_RIMBORSO: 'causaleRimborso',
  ID_GRUPPO: 'idGruppo',

  FORM_KEY_PARENT_RIMBORSI: 'datiContabili',
  FORM_KEY_CHILD_RIMBORSI: 'rimborsi',

  TITLE_STATI_DEBITORI_COLLEGATI: 'Stati debitori collegati',
  STATI_DEBITORI_COLLEGATI: 'statiDebitoriCollegati',

  LABEL_ANNULLA: 'ANNULLA',
  LABEL_INSERISCI_CREDITORE: 'INSERISCI CREDITORE',

  DESCRIPTION_CERCA_CREDITORE:
    'Indicare il codice fiscale del <strong>creditore</strong>',
};
