import { Gruppo } from '../../../../../core/commons/vo/gruppo-vo';
import { PraticaEDatiTecnici } from '../../../../../core/commons/vo/pratica-vo';
import { SoggettoVo } from '../../../../../core/commons/vo/soggetto-vo';
import {
  IAlertConfigs,
  ICommonRouteParams,
  RiscaIstanzePratica,
  RiscaIstanzeRicercaAvanzata,
} from '../../../../../shared/utilities';

/**
 * Interfaccia che definisce le proprietà per la gestione della configurazione del componente.
 */
export interface IIPConfigs extends ICommonRouteParams {
  navTarget?: RiscaIstanzePratica;
  praticaEDatiTecnici?: PraticaEDatiTecnici;
  soggettoInsert?: SoggettoVo;
  soggettoMessaggi?: IAlertConfigs;
  gruppoInsert?: Gruppo;
  gruppoUpdate?: Gruppo;
}

/**
 * Interfaccia che definisce le proprietà per la gestione della configurazione del componente.
 */
export interface IRAConfigs extends ICommonRouteParams {
  navTarget?: RiscaIstanzeRicercaAvanzata;
}
