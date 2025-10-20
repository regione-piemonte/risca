/**
 * Interfaccia che racchiude le informazioni per la ricerca morosita da utilizzare per i query param di ricerca.
 */
export interface IRicercaMorosita {
  tipoRicercaMorosita?: string;
  anno?: number;
  flgRest?: number;
  flgAnn?: number;
  lim?: string;
}
