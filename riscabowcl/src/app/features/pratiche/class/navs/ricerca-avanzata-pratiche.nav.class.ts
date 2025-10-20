import {
  IRiscaNav,
  RiscaNavClass,
} from '../../../../shared/classes/risca-nav/risca-nav.class';
import {
  IRiscaNavItem,
  RiscaIstanzeRicercaAvanzata,
} from '../../../../shared/utilities';

/**
 * Classe utilizzata per la gestione della barra di navigazione del componente ricerca avanzata.
 */
export class RicercaAvanzataNavClass
  extends RiscaNavClass<RiscaIstanzeRicercaAvanzata>
  implements IRiscaNav
{
  /** Costante che identifica l'attuale istanza della pratica aperta: pratiche. */
  ISTANZA_PRATICHE = RiscaIstanzeRicercaAvanzata.pratiche;
  /** Costante che identifica l'attuale istanza della pratica aperta: stati-debitori. */
  ISTANZA_STATI_DEB = RiscaIstanzeRicercaAvanzata.statiDebitori;

  /** Costante che identifica il componente da visualizzare: appRicercaAvanzataPratiche. */
  RIC_PRATICHE_COMPONENT: string = 'appRicercaAvanzataPratiche';
  /** Costante che identifica il componente da visualizzare: appRicercaAvanzataStatiDebitori. */
  RIC_SD_COMPONENT: string = 'appRicercaAvanzataStatiDebitori';

  /** IRiscaNav contenente la configurazione per la tab: appRicercaAvanzataPratiche. */
  private pratiche: IRiscaNavItem<RiscaIstanzeRicercaAvanzata> = {
    ngbNavItem: this.ISTANZA_PRATICHE,
    label: 'Per pratica',
    component: this.RIC_PRATICHE_COMPONENT,
    default: true,
    disabled: false,
  };
  /** IRiscaNav contenente la configurazione per la tab: appRicercaAvanzataStatiDebitori. */
  private statiDebitori: IRiscaNavItem<RiscaIstanzeRicercaAvanzata> = {
    ngbNavItem: this.ISTANZA_STATI_DEB,
    label: 'Per stato debitorio',
    component: this.RIC_SD_COMPONENT,
    disabled: false,
  };

  /**
   * Costruttore
   */
  constructor() {
    // Richiamo il super
    super();
    // Lancio la funzione di generazione delle navs
    this.generaNavs();
  }

  /**
   * Funzione che genera le navs per la navigazione.
   */
  generaNavs() {
    // Verifico le sezioni d'aggiungere
    this.addNav(this.pratiche);
    this.addNav(this.statiDebitori);
  }
}
