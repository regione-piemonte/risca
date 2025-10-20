import {
  IRiscaNav,
  RiscaNavClass,
} from '../../../../../shared/classes/risca-nav/risca-nav.class';
import { IRiscaNavItem } from '../../../../../shared/utilities';
import { TipiGestioneVerifiche } from './gestione-verifiche.enums';

/**
 * Interfaccia di configurazione per la configurazione delle classe.
 */
export interface IGestioneVerificheNavClassConfigs {}

/**
 * Classe utilizzata per la gestione della barra di navigazione del componente pratica.
 */
export class GestioneVerificheNavClass
  extends RiscaNavClass<TipiGestioneVerifiche>
  implements IRiscaNav
{
  /** Costante che identifica le possibili azioni sul pagamento: morosita. */
  ACT_MOROSITA: TipiGestioneVerifiche =
    TipiGestioneVerifiche.morosita;
  /** Costante che identifica le possibili azioni sul pagamento: rimborsi. */
  ACT_RIMBORSI: TipiGestioneVerifiche =
    TipiGestioneVerifiche.rimborsi;

  /** Costante che identifica il componente da visualizzare: appMorosita. */
  MOROSITA_COMPONENT: string = 'appMorosita';
  /** Costante che identifica il componente da visualizzare: appRimborsi. */
  RIMBORSI_COMPONENT: string = 'appRimborsi';

  /** IRiscaNav contenente la configurazione per la tab: ricerca. */
  private morosita: IRiscaNavItem<TipiGestioneVerifiche> = {
    ngbNavItem: this.ACT_MOROSITA,
    label: 'Morosità',
    component: this.MOROSITA_COMPONENT,
    default: true,
  };
  /** IRiscaNav contenente la configurazione per la tab: pagamenti da visionare. */
  private rimborsi: IRiscaNavItem<TipiGestioneVerifiche> = {
    ngbNavItem: this.ACT_RIMBORSI,
    label: 'Rimborsi',
    component: this.RIMBORSI_COMPONENT,
  };

  /**
   * Costruttore
   */
  constructor(configs: IGestioneVerificheNavClassConfigs) {
    // Richiamo il super
    super();
    // Lancio la funzione di generazione delle navs
    this.generaNavs(configs);
  }

  /**
   * Funzione che genera le navs per la navigazione.
   * @param configs IPraticaNavClassConfigs con le configurazioni per la classe.
   */
  generaNavs(configs: IGestioneVerificheNavClassConfigs) {
    // Verifico le sezioni d'aggiungere
    this.addNav(this.morosita);
    this.addNav(this.rimborsi);
  }
}
