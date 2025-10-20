import { RiscaDatiSoggettoConsts } from "../../../../../../shared/consts/risca/risca-dati-soggetto.consts";
import { DisabledCampiDAClass } from "../disabled-campi.dati-anagrafici.class";

/**
 * Classe comune per la gestione dell'accesso alla sezione: dati soggetto.
 */
export class DisabledDatiSoggettoClass extends DisabledCampiDAClass {
  /** Costante RiscaDatiSoggettoConsts contenente le costanti utilizzate dal componente. */
  protected RDS_C = RiscaDatiSoggettoConsts;

  constructor() {
    super();
  }
}