import { DynamicObjAny, RiscaCss } from '../../../../utilities';
import { FALivello } from './rfa-level.class';

/**
 * Classe che definisce le proprietà per la gestione degli elementi per il filo d'arianna.
 */
export class IFASegmento {
  /** String identificativo dell'elemento del livello. */
  id?: string;
  /** number che definisce l'ordine del segmento all'interno del filo d'arianna. */
  segmento?: number;
  /** FALivello[] con la lista di tutti i livelli che compongono il segmento. */
  livelli: FALivello[];
}

/**
 * Interfaccia che definisce le proprietà per la gestione degli elementi per il filo d'arianna.
 */
export class IFALivello {
  /** String identificativo dell'elemento del livello. */
  id: string;
  /** String che definisce la label associata a questo livello. */
  label: string;
  /** string | DynamicObjAny che definisce la struttura grafica per NgClass o NgStyle della label. */
  css?: RiscaCss;
  /** DynamicObjAny che definisce uno spazio in cui è possibile inserire informazioni extra. */
  extra?: DynamicObjAny;
}
