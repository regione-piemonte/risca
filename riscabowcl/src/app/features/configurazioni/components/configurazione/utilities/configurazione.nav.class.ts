import { AccessoElementiAppKeyConsts } from '../../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import {
  IRiscaNav,
  RiscaNavClass,
} from '../../../../../shared/classes/risca-nav/risca-nav.class';
import { IRiscaNavItem } from '../../../../../shared/utilities';
import { AzioniConfigurazione } from './configurazione.enums';

/**
 * Interfaccia di configurazione per la configurazione delle classe.
 */
export interface IConfigurazioniNavClassConfigs {
  AEA_canoni_NavDisabled?: boolean;
  AEA_tassi_interesse_NavDisabled?: boolean;
  AEA_parametri_dilazione_NavDisabled?: boolean;
  AEA_altri_parametri_NavDisabled?: boolean;
}

/**
 * Classe utilizzata per la gestione della barra di navigazione del componente configurazione.
 */
export class ConfigurazioneNavClass
  extends RiscaNavClass<AzioniConfigurazione>
  implements IRiscaNav
{
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** Costante che identifica le possibili azioni sulla pratica: canoni. */
  NAV_CANONI = AzioniConfigurazione.canoni;
  /** Costante che identifica le possibili azioni sulla pratica: tassi d'interesse. */
  NAV_TASSI_INTERESSE = AzioniConfigurazione.tassiDiInteresse;
  /** Costante che identifica le possibili azioni sulla pratica: parametri della dilazione. */
  NAV_PARAMETRI_DILAZIONE = AzioniConfigurazione.parametriDellaDilazione;
  /** Costante che identifica le possibili azioni sulla pratica: altri parametri. */
  NAV_ALTRI_PARAMETRI = AzioniConfigurazione.altriParametri;

  /** IRiscaNav contenente la configurazione per la tab: Canoni. */
  private canoni: IRiscaNavItem<AzioniConfigurazione> = {
    ngbNavItem: AzioniConfigurazione.canoni,
    label: 'Canoni',
    component: AzioniConfigurazione.canoni,
    default: true,
  };
  /** IRiscaNav contenente la configurazione per la tab: Canoni. */
  private tassiDiInteresse: IRiscaNavItem<AzioniConfigurazione> = {
    ngbNavItem: AzioniConfigurazione.tassiDiInteresse,
    label: `Tassi d'interesse`,
    component: AzioniConfigurazione.tassiDiInteresse,
    disabled: true,
  };
  /** IRiscaNav contenente la configurazione per la tab: Canoni. */
  private parametriDellaDilazione: IRiscaNavItem<AzioniConfigurazione> = {
    ngbNavItem: AzioniConfigurazione.parametriDellaDilazione,
    label: 'Parametri della dilazione',
    component: AzioniConfigurazione.parametriDellaDilazione,
    disabled: true,
  };
  /** IRiscaNav contenente la configurazione per la tab: Canoni. */
  private altriParametri: IRiscaNavItem<AzioniConfigurazione> = {
    ngbNavItem: AzioniConfigurazione.altriParametri,
    label: 'Altri parametri',
    component: AzioniConfigurazione.altriParametri,
    disabled: true,
  };

  /**
   * Costruttore
   */
  constructor(configs: IConfigurazioniNavClassConfigs) {
    // Richiamo il super
    super();
    // Lancio la funzione di generazione delle navs
    this.generaNavs(configs);
  }

  /**
   * Funzione che genera le navs per la navigazione.
   * @param configs IPraticaNavClassConfigs con le configurazioni per la classe.
   */
  generaNavs(configs: IConfigurazioniNavClassConfigs) {
    // Recupero le proprietà dall'oggetto
    const {
      AEA_canoni_NavDisabled,
      AEA_tassi_interesse_NavDisabled,
      AEA_parametri_dilazione_NavDisabled,
      AEA_altri_parametri_NavDisabled,
    } = configs;

    this.canoni.disabled = AEA_canoni_NavDisabled ?? false;
    this.tassiDiInteresse.disabled = AEA_tassi_interesse_NavDisabled ?? false;
    this.parametriDellaDilazione.disabled =
      AEA_parametri_dilazione_NavDisabled ?? false;
    this.altriParametri.disabled = AEA_altri_parametri_NavDisabled ?? false;

    // Verifico le sezioni d'aggiungere
    this.addNav(this.canoni);
    this.addNav(this.tassiDiInteresse);
    // this.addNav(this.parametriDellaDilazione);
    // this.addNav(this.altriParametri);
  }
}
