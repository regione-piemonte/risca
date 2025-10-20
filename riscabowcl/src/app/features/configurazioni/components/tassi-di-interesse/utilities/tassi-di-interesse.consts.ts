import { JourneySnapshot } from '../../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes } from '../../../../../shared/utilities';
import { TipiTassiInteresse } from './tassi-di-interesse.enums';

/**
 * Oggetto contenente una serie di costanti per il componente omonimo.
 */
export class TassiDiInteresseConsts {
  /** Interesse legale (L) o interesse di mora (M) */
  readonly INTERESSE_LEGALE = TipiTassiInteresse.interesseLegale;
  readonly INTERESSE_DI_MORA = TipiTassiInteresse.interesseMora;
  /** boolean per mostrare il button aggiungi  */
  readonly SHOW_BUTTON_AGGIUNGI: boolean = true;
  /** boolean per mostrare il campo data fine */
  readonly SHOW_FIELD_DATA_FINE: boolean = false;
  /** boolean per mostrare il campo data inizio */
  readonly DISABLED_FIELD_DATA_INIZIO: boolean = false;
  // Boolean per mostrare il campo giorni
  readonly SHOW_FIELD_GIORNI: boolean = false;


  /** IJStepConfig con la configurazione per la navigazione. */
  readonly NAVIGATION_CONFIG: IJStepConfig = {
    componentId: 'tassi_di_interesse_component',
    name: `Tassi d'interesse`,
    routeStep: AppRoutes.gestioneConfigurazioni,
    stepCaller: AppCallers.tassiDiInteresse,
  };
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  readonly SNAPSHOT_CONFIG: JourneySnapshot = {
    saveFunc: false,
    mapping: {},
  };
}
