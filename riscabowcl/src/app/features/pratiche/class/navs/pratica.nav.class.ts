import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import {
  IRiscaNav,
  RiscaNavClass,
} from '../../../../shared/classes/risca-nav/risca-nav.class';
import {
  IRiscaNavItem,
  RiscaAzioniPratica,
} from '../../../../shared/utilities';

/**
 * Interfaccia di configurazione per la configurazione delle classe.
 */
export interface IPraticaNavClassConfigs {
  AEA_ins_pratica_NavDisabled?: boolean;
}

/**
 * Classe utilizzata per la gestione della barra di navigazione del componente pratica.
 */
export class PraticaNavClass
  extends RiscaNavClass<RiscaAzioniPratica>
  implements IRiscaNav
{
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** Costante che identifica le possibili azioni sulla pratica: ricerca-semplice. */
  ACT_RICERCA_SEMPLICE: RiscaAzioniPratica = RiscaAzioniPratica.ricercaSemplice;
  /** Costante che identifica le possibili azioni sulla pratica: ricerca-avanzata. */
  ACT_RICERCA_AVANZATA: RiscaAzioniPratica = RiscaAzioniPratica.ricercaAvanzata;
  /** Costante che identifica le possibili azioni sulla pratica: inserisci-pratica. */
  ACT_INSERISCI_PRATICA: RiscaAzioniPratica =
    RiscaAzioniPratica.inserisciPratica;

  /** Costante che identifica il componente da visualizzare: appRicercaSemplicePratica. */
  RIC_SEM_COMPONENT: string = 'appRicercaSemplicePratica';
  /** Costante che identifica il componente da visualizzare: appRicercaAvanzataPratica. */
  RIC_AVA_COMPONENT: string = 'appRicercaAvanzataPratica';
  /** Costante che identifica il componente da visualizzare: appInserisciPratica. */
  INS_PRA_COMPONENT: string = 'appInserisciPratica';

  /** IRiscaNav contenente la configurazione per la tab: Ricerca semplice. */
  private ricercaSemplice: IRiscaNavItem<RiscaAzioniPratica> = {
    ngbNavItem: this.ACT_RICERCA_SEMPLICE,
    label: 'Ricerca semplice',
    component: this.RIC_SEM_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab: Ricerca avanzata. */
  private ricercaAvanzata: IRiscaNavItem<RiscaAzioniPratica> = {
    ngbNavItem: this.ACT_RICERCA_AVANZATA,
    label: 'Ricerca avanzata',
    component: this.RIC_AVA_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab: Inserisci pratica. */
  private inserisciPratica: IRiscaNavItem<RiscaAzioniPratica> = {
    ngbNavItem: this.ACT_INSERISCI_PRATICA,
    label: 'Inserisci pratica',
    component: this.INS_PRA_COMPONENT,
    default: true,
  };

  /**
   * Costruttore
   */
  constructor(configs: IPraticaNavClassConfigs) {
    // Richiamo il super
    super();
    // Lancio la funzione di generazione delle navs
    this.generaNavs(configs);
  }

  /**
   * Funzione che genera le navs per la navigazione.
   * @param configs IPraticaNavClassConfigs con le configurazioni per la classe.
   */
  generaNavs(configs: IPraticaNavClassConfigs) {
    // Recupero le proprietà dall'oggetto
    const { AEA_ins_pratica_NavDisabled } = configs;
    // Aggiorno le configurazioni
    this.inserisciPratica.disabled = AEA_ins_pratica_NavDisabled;

    // Verifico le sezioni d'aggiungere
    this.addNav(this.ricercaSemplice);
    this.addNav(this.ricercaAvanzata);
    this.addNav(this.inserisciPratica);
  }
}
