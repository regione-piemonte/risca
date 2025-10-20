/**
 * Definisco un interface personalizzato per la gestione dei placeholders.
 */
export interface DynamicObjRiscaNotifyPlaceholders {
  [key: string]: string[];
}

/**
 * Enum personalizzato per la gestione dei placeholder per i code generati dal server e definiti all'interno degli oggetti d'errore intercettati dalle subscribe per le http requests.
 */
export const RiscaNotifyPlaceholders: DynamicObjRiscaNotifyPlaceholders = {
  /**
   * ###################################################
   * GESTIONE PLACEHOLDER PER CODICE APPLICAZIONE PER: E
   * ###################################################
   */
  E000: [],
  E028: ['{PH_NUM_TOTALE}', '{PH_OGGETTO}'],
  E040: ['{PH_NUM_TOTALE}', '{PH_OGGETTO}'],
  E092: ['{TIPO_REPORT}', '{TIMESTAMP}'],
  /**
   * ###################################################
   * GESTIONE PLACEHOLDER PER CODICE APPLICAZIONE PER: A
   * ###################################################
   */
  A000: [],
  A009: ['{PH_NUM_TOTALE}', '{PH_OGGETTO}'],
  A010: ['{PH_NUM_TOTALE}', '{PH_OGGETTO}'],
  A012: ['{PH_NUM_TOTALE}', '{PH_OGGETTO}'],
  A015: ['{PH_NUM_TOTALE}', '{PH_OGGETTO}'],
  A016: ['{PH_NUM_TOTALE}', '{PH_OGGETTO}'],
  /**
   * ###################################################
   * GESTIONE PLACEHOLDER PER CODICE APPLICAZIONE PER: I
   * ###################################################
   */
  I000: [],
  I024: ['{PH_UTENTE_LOCK}'],
  I042: ['{ANNO}'],
  /**
   * ###################################################
   * GESTIONE PLACEHOLDER PER CODICE APPLICAZIONE PER: F
   * ###################################################
   */
  F000: [],
  /**
   * ###################################################
   * GESTIONE PLACEHOLDER PER CODICE APPLICAZIONE PER: C
   * ###################################################
   */
  C000: [],
  /**
   * ###################################################
   * GESTIONE PLACEHOLDER PER CODICE APPLICAZIONE PER: P
   * ###################################################
   */
  P000: [],
};
