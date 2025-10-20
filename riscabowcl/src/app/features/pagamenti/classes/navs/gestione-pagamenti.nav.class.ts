import {
  IRiscaNav,
  RiscaNavClass,
} from '../../../../shared/classes/risca-nav/risca-nav.class';
import { IRiscaNavItem } from '../../../../shared/utilities';
import { RiscaAzioniGestionePagamenti } from '../../component/gestione-pagamenti/utilities/gestione-pagamenti.enums';

/**
 * Interfaccia di configurazione per la configurazione delle classe.
 */
export interface IGestionePagamentiNavClassConfigs {}

/**
 * Classe utilizzata per la gestione della barra di navigazione del componente pratica.
 */
export class GestionePagamentiNavClass
  extends RiscaNavClass<RiscaAzioniGestionePagamenti>
  implements IRiscaNav
{
  /** Costante che identifica le possibili azioni sul pagamento: ricerche. */
  ACT_RICERCHE: RiscaAzioniGestionePagamenti =
    RiscaAzioniGestionePagamenti.ricerche;
  /** Costante che identifica le possibili azioni sul pagamento: pagamenti-da-visionare. */
  ACT_PAGAMENTI_DA_VISIONARE: RiscaAzioniGestionePagamenti =
    RiscaAzioniGestionePagamenti.pagamentiDaVisionare;

  /** Costante che identifica il componente da visualizzare: appRicerche. */
  RICERCHE_COMPONENT: string = 'appRicerche';
  /** Costante che identifica il componente da visualizzare: appPagamentiDaVisionare. */
  PAG_DA_VIS_COMPONENT: string = 'appPagamentiDaVisionare';

  /** IRiscaNav contenente la configurazione per la tab: ricerca. */
  private ricerche: IRiscaNavItem<RiscaAzioniGestionePagamenti> = {
    ngbNavItem: this.ACT_RICERCHE,
    label: 'Ricerche',
    component: this.RICERCHE_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab: pagamenti da visionare. */
  private pagamentiDaVisionare: IRiscaNavItem<RiscaAzioniGestionePagamenti> = {
    ngbNavItem: this.ACT_PAGAMENTI_DA_VISIONARE,
    label: 'Pagamenti da visionare',
    component: this.PAG_DA_VIS_COMPONENT,
    default: true,
  };

  /**
   * Costruttore
   */
  constructor(configs: IGestionePagamentiNavClassConfigs) {
    // Richiamo il super
    super();
    // Lancio la funzione di generazione delle navs
    this.generaNavs(configs);
  }

  /**
   * Funzione che genera le navs per la navigazione.
   * @param configs IPraticaNavClassConfigs con le configurazioni per la classe.
   */
  generaNavs(configs: IGestionePagamentiNavClassConfigs) {
    // Verifico le sezioni d'aggiungere
    this.addNav(this.ricerche);
    this.addNav(this.pagamentiDaVisionare);
  }
}
