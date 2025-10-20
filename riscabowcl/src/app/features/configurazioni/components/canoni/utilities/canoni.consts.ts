import { JourneySnapshot } from '../../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import {
  AppCallers,
  AppRoutes,
  RiscaButtonConfig,
  RiscaButtonCss,
  RiscaButtonTypes,
} from '../../../../../shared/utilities';

/**
 * Oggetto contenente una serie di costanti per il componente canoni.
 */
export class CanoniConsts {
  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: IJStepConfig = {
    componentId: 'canoni_component',
    name: 'Canoni',
    routeStep: AppRoutes.gestioneConfigurazioni,
    stepCaller: AppCallers.canoni,
  };
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: JourneySnapshot = {
    saveFunc: false,
    mapping: {},
  };

  JSON_REGOLE_TABLE_ID: string = 'JSON_REGOLE_TABLE_ID';

  BTN_CREA_ANNUALITA: RiscaButtonConfig = { label: 'Crea annualità' };
  CSS_CREA_ANNUALITA: RiscaButtonCss = {
    typeButton: RiscaButtonTypes.link,
    customButton: { padding: '0px', border: '0px' },
  };
  BTN_SALVA_MODIFICHE: RiscaButtonConfig = { label: 'SALVA MODIFICHE' };
}
