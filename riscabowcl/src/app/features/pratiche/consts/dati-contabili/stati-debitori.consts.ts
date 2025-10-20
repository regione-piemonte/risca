import { AccessoElementiAppKeyConsts } from 'src/app/core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { IRiscaButtonAllConfig } from '../../../../shared/utilities';

/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente stati debitori.
 */
interface IStatiDebitoriConsts {
  /**
   * ######################
   * BOTTONI DI NAVIGAZIONE
   * ######################
   */
  /** Configurazione versamenti */
  BTN_CONFIG_VERSAMENTI: IRiscaButtonAllConfig;
  /** Configurazione Rimborsi */
  BTN_CONFIG_RIMBORSI: IRiscaButtonAllConfig;
  /** Configurazione Accertamenti */
  BTN_CONFIG_ACCERTAMENTI: IRiscaButtonAllConfig;
  /** Configurazione Stato debitorio */
  BTN_CONFIG_ST_DEBITORIO: IRiscaButtonAllConfig;
  /** Configurazione Inserisci stato debitorio */
  BTN_CONFIG_ST_DEBITORIO_INS: IRiscaButtonAllConfig;
  /** Configurazione NAP - Numero Avviso Pagamento */
  BTN_CONFIUG_NAP: IRiscaButtonAllConfig;
  /** Configurazione Calcola interessi */
  BTN_CONFIG_CALC_INTERESSI: IRiscaButtonAllConfig;
  /** Configurazione Stampa */
  BTN_CONFIG_STAMPA: IRiscaButtonAllConfig;
}

/**
 * Oggetto contenente una serie di costanti per il componente dati contabili.
 */
export const StatiDebitoriConsts: IStatiDebitoriConsts = {
  /** Configurazione versamenti */
  BTN_CONFIG_VERSAMENTI: {
    cssConfig: { customButton: 'mr-auto' },
    dataConfig: { label: 'Pagamenti', codeAEA: AccessoElementiAppKeyConsts.VERSAMENTI },
  },
  /** Configurazione Rimborsi */
  BTN_CONFIG_RIMBORSI: {
    cssConfig: { customButton: 'mr-auto' },
    dataConfig: { label: 'Rimborsi', codeAEA: AccessoElementiAppKeyConsts.RIMBORSI },
  },
  /** Configurazione Accertamenti */
  BTN_CONFIG_ACCERTAMENTI: {
    cssConfig: { customButton: 'mr-auto' },
    dataConfig: { label: 'Accertamenti', codeAEA: AccessoElementiAppKeyConsts.ACCERTAMENTI },
  },
  /** Configurazione Stato debitorio */
  BTN_CONFIG_ST_DEBITORIO: {
    cssConfig: { customButton: 'mr-auto' },
    dataConfig: { label: 'Stato debitorio', codeAEA: AccessoElementiAppKeyConsts.STATO_DEBITORIO },
  },
  /** Configurazione Inserisci stato debitorio */
  BTN_CONFIG_ST_DEBITORIO_INS: {
    cssConfig: { customButton: 'mr-auto' },
    dataConfig: { label: 'Inserisci stato debitorio', codeAEA: AccessoElementiAppKeyConsts.INSERIMENTO_STATO_DEBITORIO },
  },
  /** Configurazione NAP - Numero Avviso Pagamento */
  BTN_CONFIUG_NAP: {
    cssConfig: { customButton: 'mr-auto' },
    dataConfig: { label: 'NAP', codeAEA: AccessoElementiAppKeyConsts.NAP },
  },
  /** Configurazione Calcola interessi */
  BTN_CONFIG_CALC_INTERESSI: {
    cssConfig: { customButton: 'mr-auto' },
    dataConfig: { label: 'Calcola interessi', codeAEA: AccessoElementiAppKeyConsts.CALCOLA_INTERESSI },
  },
  /** Configurazione Stampa */
  BTN_CONFIG_STAMPA: {
    cssConfig: { customButton: 'mr-auto' },
    dataConfig: { label: 'Stampa' },
  },
};
