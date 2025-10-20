import { SoggettoVo } from '../../../../../core/commons/vo/soggetto-vo';
import {
  ICommonRouteParams,
  RiscaAzioniPratica,
} from '../../../../../shared/utilities';

/**
 * Interafaccia che definisce i dati gestiti per il route sul componente.
 */
export interface ISoggettoRouteParams extends ICommonRouteParams {
  soggetto: SoggettoVo;
  eliminaDisabled?: boolean;
  campiAggiornatiFonte?: string[];
  praticaNavTarget?: RiscaAzioniPratica;
}
