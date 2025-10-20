import { RiscaFruitori } from '../../../../../shared/utilities';

/**
 * Interfaccia che definisce i campi per la request di ricerca di Documenti&Allegati.
 */
export interface IRicercaDocumentiAllegati {
  ricerca?: string;
  fruitore?: RiscaFruitori;
}

/**
 * Interfaccia che definisce i campi per i query params per la ricerca dei Documenti&Allegati.
 */
export interface IParamsDocumentiAllegati {
  ricerca?: string;
  fruitore?: RiscaFruitori;
}
