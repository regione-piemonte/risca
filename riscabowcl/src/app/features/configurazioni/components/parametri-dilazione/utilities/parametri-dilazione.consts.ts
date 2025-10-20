import { JourneySnapshot } from '../../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes } from '../../../../../shared/utilities';

/**
 * Oggetto contenente una serie di costanti per il componente omonimo.
 */
export class ParametriDilazioneConsts {
  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: IJStepConfig = {
    componentId: 'parametri_dilazione_component',
    name: 'Parametri della dilazione',
    routeStep: AppRoutes.gestioneConfigurazioni,
    stepCaller: AppCallers.parametriDellaDilazione,
  };
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: JourneySnapshot = {
    saveFunc: false,
    mapping: {},
  };
}
