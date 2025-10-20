import { JourneySnapshot } from '../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes } from '../../../../shared/utilities';

/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente bollettini.
 */
interface IBollettiniConsts {
  NAVIGATION_CONFIG: IJStepConfig;
  SNAPSHOT_CONFIG: JourneySnapshot;
}

/**
 * Oggetto contenente una serie di costanti per il componente bollettini.
 */
export const BollettiniConsts: IBollettiniConsts = {
  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: {
    componentId: 'bollettini_pagamenti_component',
    name: 'Bollettini - Pagamenti',
    routeStep: AppRoutes.pagamenti,
    stepCaller: AppCallers.bollettini,
  },
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: {
    saveFunc: false,
    mapping: {
      ricercaB: true,
    },
  },
};
