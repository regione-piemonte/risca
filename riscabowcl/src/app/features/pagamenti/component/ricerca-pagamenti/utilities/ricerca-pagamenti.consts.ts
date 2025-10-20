import {
  IJourneySnapshot,
  IJStepConfig,
} from '../../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import {
  AppCallers,
  AppClaimants,
  AppRoutes,
} from '../../../../../shared/utilities';

/**
 * Classe contenente le costanti per il componente associato.
 */
export class RicercaPagamentiConsts {
  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: IJStepConfig = {
    componentId: 'ricerca_pagamenti_component',
    name: 'Ricerca pagamenti',
    routeStep: AppRoutes.gestionePagamenti,
    stepCaller: AppCallers.ricerchePagamenti,
    stepClaimant: AppClaimants.ricercaPagamenti,
  };
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: IJourneySnapshot = {
    source: undefined,
    saveFunc: false,
    mapping: {
      filtriPagamenti: true,
      filtriPagIniziali: true,
      paginazionePagamenti: true,
    },
  };
}
