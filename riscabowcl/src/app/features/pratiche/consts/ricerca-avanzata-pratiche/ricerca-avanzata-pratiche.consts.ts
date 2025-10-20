import { JourneySnapshot } from '../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes, AppClaimants } from '../../../../shared/utilities';

/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente di ricerca avanzata.
 */
interface IRicercaAvanzataConsts {
  NAVIGATION_CONFIG: IJStepConfig;
  SNAPSHOT_CONFIG: JourneySnapshot;
}

/**
 * Oggetto contenente una serie di costanti per il componente home.
 */
export const RicercaAvanzataPraticheConsts: IRicercaAvanzataConsts = {
  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: {
    componentId: 'ricerca_avanzata_pratiche_component',
    name: 'Ricerca avanzata',
    routeStep: AppRoutes.gestionePratiche,
    stepCaller: AppCallers.ricercaAvanzata,
  },
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: {
    saveFunc: false,
    mapping: {
      ricercaPA: true,
    },
  },
};
