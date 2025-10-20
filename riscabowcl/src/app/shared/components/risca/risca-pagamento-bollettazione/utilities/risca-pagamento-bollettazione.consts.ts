/**
 * Interfaccia strettamente collegata alla costante: RiscaPagamentoBollettazioneConsts.
 */
export interface IRiscaPagamentoBollettazioneConsts {
  SD_SELEZIONATI_TABLE_ID: string;

  LABEL_SOGGETTO_VERSAMENTO: string;
  LABEL_NOTE: string;
  LABEL_RIMBORSATO: string;
  LABEL_CODICE_UTENZA: string;
  LABEL_NUMERO_PRATICA: string;
  LABEL_NAP: string;
  LABEL_VERIFICA_IMPORTI: string;
  LABEL_IMPORTO: string;
  LABEL_IMPORTO_DA: string;
  LABEL_IMPORTO_A: string;
  LABEL_TITOLARE: string;
  LABEL_PRATICA: string;
  LABEL_STATO_DEBITORIO: string;

  LABEL_IMPORTO_NON_IDENTIFICATO: string;
  LABEL_IMPORTO_NON_DI_COMPETENZA: string;
  LABEL_IMPORTO_DA_RIMBORSARE: string;
  LABEL_IMPORTO_DA_ASSEGNARE: string;

  // CODICE_UTENZA: string;
  // NAP: string;
  NUMERO_PRATICA: string;
  TARGET_RICERCA_STATI_DEBITORI: string;
  RICERCA_PUNTUALE: string;
  TARGET_RICERCA_TITOLARE: string;
  RICERCA_TITOLARE: string;
  IMPORTO_DA: string;
  IMPORTO_A: string;

  SOGGETTO_VERSAMENTO: string;
  NOTE: string;
  IMPORTO_NON_IDENTIFICATO: string;
  IMPORTO_NON_DI_COMPETENZA: string;
  IMPORTO_DA_RIMBORSARE: string;
  IMPORTO_DA_ASSEGNARE: string;
  RIMBORSATO: string;
}

/**
 * Costante che rappresenta le funzionalità presenti su db per la parte dei pagamenti.
 */
export const RiscaPagamentoBollettazioneConsts: IRiscaPagamentoBollettazioneConsts = {
  SD_SELEZIONATI_TABLE_ID: 'SD_SELEZIONATI_TABLE_ID',

  LABEL_SOGGETTO_VERSAMENTO: 'Soggetto versamento',
  LABEL_NOTE: 'Note',
  LABEL_RIMBORSATO: 'Rimborsato',
  LABEL_CODICE_UTENZA: 'Codice utenza',
  LABEL_NUMERO_PRATICA: 'Numero pratica',
  LABEL_NAP: 'NAP',
  LABEL_TITOLARE: 'Titolare',
  LABEL_PRATICA: 'Pratica',
  LABEL_STATO_DEBITORIO: 'Stato debitorio',
  LABEL_IMPORTO: 'Importo',
  LABEL_IMPORTO_DA: 'Da',
  LABEL_IMPORTO_A: 'A',

  LABEL_IMPORTO_NON_IDENTIFICATO: 'Non Identificato',
  LABEL_IMPORTO_NON_DI_COMPETENZA: 'Non di competenza',
  LABEL_IMPORTO_DA_RIMBORSARE: 'Da rimborsare',
  LABEL_IMPORTO_DA_ASSEGNARE: 'Importo da assegnare',
  LABEL_VERIFICA_IMPORTI: 'Verifica importi',  

  // NAP: 'nap',
  // CODICE_UTENZA: 'codiceUtenza',
  NUMERO_PRATICA: 'numeroPratica',
  TARGET_RICERCA_STATI_DEBITORI: 'targetRicercaStatiDebitori',
  RICERCA_PUNTUALE: 'ricercaPuntuale',
  TARGET_RICERCA_TITOLARE: 'targetRicercaTitolare',
  RICERCA_TITOLARE: 'ricercaTitolare',
  IMPORTO_DA: 'importoDa',
  IMPORTO_A: 'importoA',

  SOGGETTO_VERSAMENTO: 'soggettoVersamento',
  IMPORTO_NON_IDENTIFICATO: 'importoNonIdentificato',
  IMPORTO_NON_DI_COMPETENZA: 'importoNonDiCompetenza',
  IMPORTO_DA_RIMBORSARE: 'importoDaRimborsare',
  RIMBORSATO: 'rimborsato',
  IMPORTO_DA_ASSEGNARE: 'importoDaAssegnare',
  NOTE: 'note',
};
