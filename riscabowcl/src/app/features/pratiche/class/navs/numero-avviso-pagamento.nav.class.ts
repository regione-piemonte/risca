import {
  IRiscaNav,
  RiscaNavClass,
} from '../../../../shared/classes/risca-nav/risca-nav.class';
import {
  IRiscaNavItem,
  RiscaNumeroAvvisoPagamento,
} from '../../../../shared/utilities';

/**
 * Classe utilizzata per la gestione della barra di navigazione.
 */
export class NumeroAvvisoPagamentoNavClass
  extends RiscaNavClass<RiscaNumeroAvvisoPagamento>
  implements IRiscaNav
{
  /** Costante che identifica una sezione del NAP. */
  NAV_ITEM_SD_NAP = RiscaNumeroAvvisoPagamento.statiDebitoriNAP;
  /** Costante che identifica una sezione del NAP. */
  NAV_ITEM_DETTAGLIO_SD = RiscaNumeroAvvisoPagamento.dettaglioStatoDebitorioNAP;

  /** Costante che identifica il componente da visualizzare per il nap. */
  SD_NAP_COMPONENT: string = 'appStatiDebitoriNAP';
  /** Costante che identifica il componente da visualizzare per il nap. */
  DETTAGLIO_SD_COMPONENT: string = 'appDettaglioStatoDebitorio';

  /** IRiscaNav contenente la configurazione per la tab del NAP. */
  statiDebitoriNAP: IRiscaNavItem<RiscaNumeroAvvisoPagamento> = {
    ngbNavItem: this.NAV_ITEM_SD_NAP,
    label: 'Dati generali/amministrativi/dilazione',
    component: this.SD_NAP_COMPONENT,
    default: true,
  };
  /** IRiscaNav contenente la configurazione per la tab del NAP. */
  dettaglioSDNAP: IRiscaNavItem<RiscaNumeroAvvisoPagamento> = {
    ngbNavItem: this.NAV_ITEM_DETTAGLIO_SD,
    label: 'Dati anagrafici',
    component: this.DETTAGLIO_SD_COMPONENT,
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
    this.addNav(this.statiDebitoriNAP);
    this.addNav(this.dettaglioSDNAP);
  }
}
