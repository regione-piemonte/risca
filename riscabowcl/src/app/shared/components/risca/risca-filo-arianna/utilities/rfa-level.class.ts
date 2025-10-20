import { RiscaCss, DynamicObjAny } from "../../../../utilities";
import { IFALivello } from "./rfa-level.interfaces";

/**
 * Classe che definisce le proprietà per la gestione degli elementi per il filo d'arianna.
 */
export class FALivello {
  /** String identificativo dell'elemento del livello. */
  id: string;
  /** String che definisce la label associata a questo livello. */
  label: string;
  /** string | DynamicObjAny che definisce la struttura grafica per NgClass o NgStyle della label. */
  css?: RiscaCss;
  /** DynamicObjAny che definisce uno spazio in cui è possibile inserire informazioni extra. */
  extra?: DynamicObjAny;

  /**
   * Costruttore.
   */
  constructor(rfa?: IFALivello) {
    this.id = rfa.id;
    this.label = rfa.label;
    this.css = rfa.css;
    this.extra = rfa.extra;
  }
}
