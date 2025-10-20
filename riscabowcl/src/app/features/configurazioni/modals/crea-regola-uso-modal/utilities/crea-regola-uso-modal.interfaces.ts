import { RiscaNotifyCodes } from '../../../../../shared/utilities/enums/risca-notify-codes.enums';
import { ICreaRegolaUsoForm } from '../../../components/crea-regola-uso-form/utilities/crea-regola-uso-form.interfaces';

/**
 * Interfaccia che definisce le informazioni che richiede la modale omonima in input.
 */
export interface ICreaRegolaUsoModal {
  creaRegolaUsoForm?: ICreaRegolaUsoForm;
}

/**
 * Interfaccia che definisce le informazioni restituite dalla modale in caso di chiusura con salvataggio dati.
 */
export interface ICreaRegolaUsoModalConfirm {
  code: RiscaNotifyCodes;
  anno: number;
}
