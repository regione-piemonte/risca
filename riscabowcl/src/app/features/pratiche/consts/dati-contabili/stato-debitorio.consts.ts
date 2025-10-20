/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente: stato debitorio.
 */
interface IStatoDebitorioConsts {
  SD_TITLE_DEFAULT: string;
  SD_TITLE_MODIFICA: string;
  SD_TITLE_INSERIMENTO: string;
}

/**
 * Oggetto contenente una serie di costanti per il componente: stato debitorio.
 */
export const StatoDebitorioConsts: IStatoDebitorioConsts = {
  /** Stringhe per la gestione dei titoli della pagina. */
  SD_TITLE_DEFAULT: 'Stato debitorio',
  SD_TITLE_MODIFICA: 'Dettaglio stato debitorio',
  SD_TITLE_INSERIMENTO: 'Inserisci stato debitorio',
};
