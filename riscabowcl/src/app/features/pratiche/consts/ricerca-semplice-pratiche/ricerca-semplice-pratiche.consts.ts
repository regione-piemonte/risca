import { JourneySnapshot } from '../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes } from '../../../../shared/utilities';

/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente di ricerca semplice.
 */
interface IRicercaSempliceConsts {
  NAVIGATION_CONFIG: IJStepConfig;
  SNAPSHOT_CONFIG: JourneySnapshot;
}

/**
 * Oggetto contenente una serie di costanti per il componente home.
 */
export const RicercaSemplicePraticheConsts: IRicercaSempliceConsts = {
  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: {
    componentId: 'ricerca_semplice_pratiche_component',
    name: 'Ricerca semplice',
    routeStep: AppRoutes.gestionePratiche,
    stepCaller: AppCallers.ricercaSemplicePratiche,
  },
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: {
    saveFunc: false,
    mapping: {
      ricercaPS: true,
    },
  },
};
