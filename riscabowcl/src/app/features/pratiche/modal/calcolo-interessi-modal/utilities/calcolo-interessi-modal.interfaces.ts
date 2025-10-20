import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';

/**
 * Interfaccia che definisce i parametri da passare alla modale.
 */
export interface ICalcoloInteressiModalConfigs {
  /** StatoDebitorioVo con le informazioni dello stato debitorio di riferimento. */
  statoDebitorio: StatoDebitorioVo;
}
