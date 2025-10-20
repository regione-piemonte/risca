import { RiscaContattiConsts } from '../../../../../../shared/consts/risca/risca-contatti.consts';
import { ValidatorCampiDAClass } from '../require-campi.dati-anagrafici.class';

/**
 * Classe comune per la gestione dell'accesso alla sezione: recapiti.
 */
export class RequireContattiClass extends ValidatorCampiDAClass {
  /** Costante RiscaContattiConsts contenente le costanti utilizzate dal componente. */
  RC_C = RiscaContattiConsts;

  constructor() {
    super();
  }
}
