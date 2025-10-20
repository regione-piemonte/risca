import { AppRoutes } from '../../../shared/utilities';
import { IRiscaBodyNav } from '../../components/body/utilities/body.interfaces';
import { IJStepConfig } from '../../interfaces/navigation-helper/navigation-helper.interfaces';

/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente body.
 */
interface IBodyConsts {
  // Definisco gli oggetti delle route
  NAV_HOME: IRiscaBodyNav;
  NAV_PRATICHE: IRiscaBodyNav;
  NAV_PAGAMENTI: IRiscaBodyNav;
  NAV_VERIFICHE: IRiscaBodyNav;
  NAV_SPEDIZIONI: IRiscaBodyNav;
  NAV_REPORT: IRiscaBodyNav;
  NAV_CONFIGURAZIONI: IRiscaBodyNav;
  NAV_GRUPPO: IRiscaBodyNav;
  // Path di default per la navigazione
  NAV_PATH_DEFAULT: AppRoutes;

  NAVIGATION_CONFIG: IJStepConfig;
}

/**
 * Oggetto contenente una serie di costanti per il componente body.
 */
export const BodyConsts: IBodyConsts = {
  // Definisco gli oggetti delle route
  NAV_HOME: {
    title: 'Home',
    path: AppRoutes.home,
    basePath: AppRoutes.home,
    disabled: false,
    state: undefined,
  },
  NAV_PRATICHE: {
    title: 'Pratiche',
    path: AppRoutes.gestionePratiche,
    basePath: AppRoutes.pratiche,
    disabled: false,
    state: undefined,
  },
  NAV_PAGAMENTI: {
    title: 'Pagamenti',
    path: AppRoutes.gestionePagamenti,
    basePath: AppRoutes.pagamenti,
    disabled: false,
    state: undefined,
  },
  NAV_VERIFICHE: {
    title: 'Verifiche',
    path: AppRoutes.gestioneVerifiche,
    basePath: AppRoutes.verifiche,
    disabled: false,
    state: undefined,
  },
  NAV_SPEDIZIONI: {
    title: 'Spedizioni',
    path: AppRoutes.bollettini,
    basePath: AppRoutes.spedizioni,
    disabled: false,
    state: undefined,
  },
  NAV_REPORT: {
    title: 'Report',
    path: AppRoutes.esportaDati,
    basePath: AppRoutes.report,
    disabled: false,
    state: undefined,
    badges: true,
  },
  NAV_CONFIGURAZIONI: {
    title: 'Configurazioni',
    path: AppRoutes.gestioneConfigurazioni,
    basePath: AppRoutes.configurazioni,
    disabled: false,
    state: undefined,
  },
  NAV_GRUPPO: {
    title: 'Gruppo',
    path: AppRoutes.gruppo,
    basePath: AppRoutes.soggetti,
    disabled: false,
    state: undefined,
  },

  // Path di default per la navigazione
  NAV_PATH_DEFAULT: AppRoutes.home,

  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: {
    componentId: 'body_component',
    name: 'Index',
    routeStep: undefined,
  },
};
