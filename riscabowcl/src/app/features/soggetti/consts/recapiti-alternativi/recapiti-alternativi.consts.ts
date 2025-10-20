/**
 * Interfaccia che definisce la costruzione dell'oggetto SoggettoConsts.
 */
interface IRecapitialternativiConsts {
  TITLE_INDIRIZZO_SPEDIZIONE: string;
  INDIRIZZO_SPEDIZIONE: string;
}

/**
 * Oggetto costante contenente una serie di costanti per il componente dati-anagrafici.
 */
export const RecapitialternativiConsts: IRecapitialternativiConsts = {
  /** Definisce il titolo per l'indirizzo di spedizione */
  TITLE_INDIRIZZO_SPEDIZIONE: '',
  /** Definisce il nome della action per l'indirizzo di spedizione da usare nel bottone della tabella dei recapiti */
  INDIRIZZO_SPEDIZIONE: 'indirizzospedizione',
};
