/**
 * Class contenente una serie di costanti per il componente omonimo.
 */
export class RiscaSDPerPagamentoConsts {
  // Chiave per lo spinner specifico
  SPINNER = 'RISCA_SD_NAP_CODICE_UTENZA_SPINNER';

  // Titolo modale
  TITLE_MODALE = 'Ricerca stati debitori per pagamento';

  /** Costante come label per l'accordion degli stati debitori selezionati. */
  ACCORDION_SD_SELEZIONATI: string = 'Stati debitori selezionati';
  /** Costante come label per la sezione degli stati debitori selezionati, quando l'utente non ha ancora selezionato elementi dalla tabella. */
  LABEL_NESSUN_SD_SELEZIONATO: string = 'Nessun stato debitorio selezionato.';
  /** Costante come label per la sezione degli stati debitori selezionati, quando l'utente ha premuto su "seleziona tutti". */
  LABEL_TUTTI_SD_SELEZIONATI: string = 'Selezionati tutti gli stati debitori.';

  // Pulsanti
  LABEL_ANNULLA = 'ANNULLA';
  LABEL_CONFERMA = 'CONFERMA';

  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}
