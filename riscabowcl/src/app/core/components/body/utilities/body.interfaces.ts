import { AppRoutes } from '../../../../shared/utilities';

/**
 * Interface di comodo che definisce la struttura dell'oggetto per la gestione della navigazione tramite ngbNav
 */
export interface IRiscaBodyNav {
  title: string;
  path: AppRoutes;
  basePath: AppRoutes;
  disabled: boolean;
  state?: any;
  badges?: boolean;
}
