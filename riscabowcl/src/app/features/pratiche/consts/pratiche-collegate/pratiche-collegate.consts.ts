import { JourneySnapshot } from '../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes } from '../../../../shared/utilities';

/**
 * Interfaccia per il mapping dell'oggetto QuadriTecniciConsts
 */
interface IPraticheCollegateConsts {
  NAVIGATION_CONFIG: IJStepConfig;
  SNAPSHOT_CONFIG: JourneySnapshot;
}

/**
 * Oggetto costante contenente una serie di costanti per il componente pratiche-collegate.
 */
export const PraticheCollegateConsts: IPraticheCollegateConsts = {
  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: {
    componentId: 'pratiche_collegate_component',
    name: 'Pratiche collegate',
    routeStep: AppRoutes.praticheCollegate,
    stepCaller: AppCallers.praticheCollegate,
  },
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: {
    saveFunc: false,
    mapping: {
      soggetto: true,
      gruppoEComponenti: true,
    },
  },
};
