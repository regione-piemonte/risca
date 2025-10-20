import { RiscaDatiSoggettoConsts } from "../../../../../../shared/consts/risca/risca-dati-soggetto.consts";
import { ValidatorCampiDAClass } from "../require-campi.dati-anagrafici.class";
import { RiscaRegExp } from "../../../../../../shared/utilities";

/**
 * Classe comune per la gestione dell'accesso alla sezione: dati soggetto.
 */
export class RequireDatiSoggettoClass extends ValidatorCampiDAClass {
  /** Costante RiscaDatiSoggettoConsts contenente le costanti utilizzate dal componente. */
  protected RDS_C = RiscaDatiSoggettoConsts;
  /** Classe RiscaRegExp contenente le regular expression dell'applicazione. */
  protected riscaRegExp = new RiscaRegExp();

  constructor() {
    super();
  }
}