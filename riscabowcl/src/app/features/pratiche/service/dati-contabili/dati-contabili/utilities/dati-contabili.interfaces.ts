/**
 * Interfaccia con le informazioni di filtro per ricerca stati debitori per morosità.
 */
export interface IDCRicercaSDByMorosita {
  tipoRicercaMorosita: any;
  anno: number;
  flgRest: number;
  flgAnn: number;
  lim: string;
}

/**
 * Interfaccia con le informazioni di ricerca stati debitori condivisi per la ricerca per nap e la ricerca per codice utenza.
 */
export interface ISearchSDByNapOrCodiceUtenza {}

/**
 * Interfaccia con le informazioni di ricerca stati debitori condivisi per la ricerca per nap.
 */
export interface ISearchSDByNap extends ISearchSDByNapOrCodiceUtenza {}

/**
 * Interfaccia con le informazioni di ricerca stati debitori condivisi per la ricerca per codice.
 */
export interface ISearchSDByCodiceUtenza extends ISearchSDByNapOrCodiceUtenza {}
