import { Gruppo } from '../../../../../core/commons/vo/gruppo-vo';
import { SoggettoVo } from '../../../../../core/commons/vo/soggetto-vo';
import {
  ICommonRouteParams,
  RiscaAzioniPratica,
} from '../../../../../shared/utilities';

/**
 * Interfaccia che definisce la struttura dell'oggetto state passato nel router.
 */
export interface IGruppoRouteParams extends ICommonRouteParams {
  gruppo?: Gruppo;
  soggettoInsert?: SoggettoVo;
  idPratica?: number;
  praticaNavTarget?: RiscaAzioniPratica;
}
