import { RiscaRecapitoConsts } from '../../../../../../shared/consts/risca/risca-recapito.consts';
import { ValidatorCampiDAClass } from '../require-campi.dati-anagrafici.class';

/**
 * Classe comune per la gestione dell'accesso alla sezione: recapiti.
 */
export class RequireRecapitiClass extends ValidatorCampiDAClass {
  /** Costante RiscaRecapitoConsts contenente le costanti utilizzate dal componente. */
  RR_C = RiscaRecapitoConsts;

  constructor() {
    super();
  }
}
