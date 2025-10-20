import { IRiscaButtonAllConfig } from 'src/app/shared/utilities';

/**
 * Interfaccia strettamente collegata alla costante: ICalcoloInteressiConsts.
 */
export interface ICalcoloInteressiConsts {
  // TITOLO DELLA MODALE
  TITOLO_CALCOLO_INTERESSI: string;

  // LABEL
  LABEL_CANONE_MANCANTE: string;
  LABEL_DATA_SCADENZA_PAGAMENTO: string;
  LABEL_DATA_VERSAMENTO: string;
  LABEL_INTERESSI_DOVUTI: string;
  LABEL_TOTALE_DA_VERSARE: string;

  // CAMPI
  CANONE_MANCANTE: string;
  DATA_SCADENZA_PAGAMENTO: string;
  DATA_VERSAMENTO: string;
  INTERESSI_DOVUTI: string;
  TOTALE_DA_VERSARE: string;

  /**
   * ######################
   * BOTTONI DI NAVIGAZIONE
   * ######################
   */
  /** Configurazione versamenti */
  BTN_CONFIG_VERSAMENTI: IRiscaButtonAllConfig;
}

/**
 * Costante che rappresenta le funzionalità presenti su db per la parte dei pagamenti.
 */
export const CalcoloInteressiConsts: ICalcoloInteressiConsts = {
  // TITOLO DELLA MODALE
  TITOLO_CALCOLO_INTERESSI: 'Calcolo Interessi',
  // LABEL
  LABEL_CANONE_MANCANTE: '*Canone mancante',
  LABEL_DATA_SCADENZA_PAGAMENTO: 'Data scadenza',
  LABEL_DATA_VERSAMENTO: 'Data pagamento',
  LABEL_INTERESSI_DOVUTI: 'Interessi dovuti',
  LABEL_TOTALE_DA_VERSARE: 'Totale da versare',

  // CAMPI
  CANONE_MANCANTE: 'canoneMancante',
  DATA_SCADENZA_PAGAMENTO: 'dataScadenza',
  DATA_VERSAMENTO: 'dataVersamento',
  INTERESSI_DOVUTI: 'interessiDovuti',
  TOTALE_DA_VERSARE: 'totaleDaVersare',

  /** Configurazione versamenti */
  BTN_CONFIG_VERSAMENTI: {
    cssConfig: { customButton: 'mr-auto' },
    dataConfig: { label: 'Calcola' }, // , codeAEA: AccessoElementiAppKeyConsts.CALCOLA_INTERESSI },
  },
};
