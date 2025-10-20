import {
  IRiscaNav,
  RiscaNavClass,
} from '../../../../shared/classes/risca-nav/risca-nav.class';
import { IRiscaNavItem } from '../../../../shared/utilities';
import { TipiRicerchePagamenti } from '../../component/ricerche-pagamenti/utilities/ricerche-pagamenti.enums';

/**
 * Interfaccia di configurazione per la configurazione delle classe.
 */
export interface IRicerchePagamentiNavClassConfigs {}

/**
 * Classe utilizzata per la gestione della barra di navigazione del componente ricerca pagamenti.
 */
export class RicerchePagamanetiNavClass
  extends RiscaNavClass<TipiRicerchePagamenti>
  implements IRiscaNav
{
  /** Costante che identifica l'attuale ricerca tipo pagamento aperta: pagamenti. */
  TIPO_RICERCA_PAGAMENTI = TipiRicerchePagamenti.pagamenti;

  /** Costante che identifica il componente da visualizzare: appRicercaPagamenti. */
  RICERCA_PAG_COMPONENT: string = 'appRicercaPagamenti';

  /** IRiscaNav contenente la configurazione per la tab: appGeneraliAmministrativi. */
  private ricercaPagamenti: IRiscaNavItem<TipiRicerchePagamenti> = {
    ngbNavItem: this.TIPO_RICERCA_PAGAMENTI,
    label: 'Pagamenti',
    component: this.RICERCA_PAG_COMPONENT,
    default: true,
  };

  /**
   * Costruttore
   */
  constructor(configs: IRicerchePagamentiNavClassConfigs) {
    // Richiamo il super
    super();
    // Lancio la funzione di generazione delle navs
    this.generaNavs(configs);
  }

  /**
   * Funzione che genera le navs per la navigazione.
   */
  generaNavs(configs: IRicerchePagamentiNavClassConfigs) {
    // Verifico le sezioni d'aggiungere
    this.addNav(this.ricercaPagamenti);
  }
}
