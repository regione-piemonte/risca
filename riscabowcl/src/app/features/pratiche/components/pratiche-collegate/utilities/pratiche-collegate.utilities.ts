import { Gruppo } from '../../../../../core/commons/vo/gruppo-vo';
import { SoggettoVo } from '../../../../../core/commons/vo/soggetto-vo';
import { ICommonRouteParams } from '../../../../../shared/utilities';

/**
 * Interfaccia che definisce la struttura dell'oggetto per la gestione delle pratiche collegate.
 */
export interface IPraticheCollegateRouteParams extends ICommonRouteParams {
  /** Definisce i dati di un possibile soggetto per la quale scaricare le pratiche collegate. */
  soggetto?: SoggettoVo;
  /** Definisce i dati di un possibile gruppo per la quale scaricare le pratiche collegate. */
  gruppo?: Gruppo;

  /** Any di supporto per la gestione di altri dati per le pratiche collegate. */
  extra?: any;
}
