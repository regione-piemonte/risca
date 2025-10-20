import { IRiscaNavRouteParams } from '../../../../../shared/utilities';
import { TipiRicerchePagamenti } from '../../ricerche-pagamenti/utilities/ricerche-pagamenti.enums';
import { RiscaAzioniGestionePagamenti } from './gestione-pagamenti.enums';

/**
 * Interface che definisce i parametri che è possibile passare come route params.
 */
export interface IGestionePagamentiRouteParams
  extends IRiscaNavRouteParams<RiscaAzioniGestionePagamenti> {
  tipoRicercaPagamenti?: TipiRicerchePagamenti;
}
