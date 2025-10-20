import { InteressiLegaliVo } from '../../../../../core/commons/vo/interessi-legali-vo';

/**
 * Interfaccia che definisce le informazioni che richiede la modale omonima in input.
 */
export interface IInteressiLegaliFormDataModal {
  interessiLegali: InteressiLegaliVo;
  readOnly: boolean;
}
