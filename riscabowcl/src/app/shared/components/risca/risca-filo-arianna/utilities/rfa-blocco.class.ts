import { FALivello } from './rfa-level.class';
import { IFASegmento } from './rfa-level.interfaces';

/**
 * Classe che definisce le proprietà per la gestione degli elementi per il filo d'arianna.
 */
export class FASegmento {
  /** String identificativo dell'elemento del livello. */
  id?: string;
  /** number che definisce l'ordine del segmento all'interno del filo d'arianna. */
  segmento?: number;
  /** FALivello[] con la lista di tutti i livelli che compongono il segmento. */
  livelli: FALivello[];

  /**
   * Costruttore.
   */
  constructor(fas?: IFASegmento) {
    this.id = fas.id;
    this.segmento = fas.segmento;
    this.livelli = fas.livelli;
  }
}
