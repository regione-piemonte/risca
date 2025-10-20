import { JsonRegolaRangeVo } from '../../../../../core/commons/vo/json-regola-range-vo';
import { JsonRegolaSogliaVo } from '../../../../../core/commons/vo/json-regola-soglia-vo';
import { RegolaUsoVo } from '../../../../../core/commons/vo/regola-uso-vo';

/**
 * Interfaccia che definisce le informazioni che richiede la modale omonima in input.
 */
export interface IDettaglioRegolaUsoModal {
  regola: RegolaUsoVo;
}

/**
 * Interfaccia che definisce le informazioni restituite dalla modale in caso di chiusura con salvataggio dati.
 */
export interface IDettaglioRegolaUsoModalConfirm {
  regolaUso: RegolaUsoVo;
}

/**
 * Interfaccia che definisce le informazioni per gestire la modalità dati.
 */
export interface IDRUDatiModalita {
  canoneMinimo?: number;
  soglia?: JsonRegolaSogliaVo;
  ranges?: JsonRegolaRangeVo[];
}
