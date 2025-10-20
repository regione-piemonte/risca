import { InteressiDiMoraVo } from 'src/app/core/commons/vo/interessi-di-mora-vo';

/**
 * Interfaccia che definisce le informazioni che richiede la modale omonima in input.
 */
export interface IInteressiDiMoraFormDataModal {
  interessiDiMora: InteressiDiMoraVo;
  readOnly: boolean;
}
