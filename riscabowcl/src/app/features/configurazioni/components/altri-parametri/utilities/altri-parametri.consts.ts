import { JourneySnapshot } from '../../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes } from '../../../../../shared/utilities';

/**
 * Oggetto contenente una serie di costanti per il componente omonimo.
 */
export class AltriParametriConsts {
  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: IJStepConfig = {
    componentId: 'altri_parametri_component',
    name: 'Altri parametri',
    routeStep: AppRoutes.gestioneConfigurazioni,
    stepCaller: AppCallers.altriParametri,
  };
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: JourneySnapshot = {
    saveFunc: false,
    mapping: {},
  };
}
