import { IRiscaNavRouteParams } from '../../../../../shared/utilities';
import { TipiRicerchePagamenti } from './ricerche-pagamenti.enums';

/**
 * Interface che definisce i parametri che è possibile passare come route params.
 */
export interface IRicerchePagamentiRouteParams
  extends IRiscaNavRouteParams<TipiRicerchePagamenti> {}
