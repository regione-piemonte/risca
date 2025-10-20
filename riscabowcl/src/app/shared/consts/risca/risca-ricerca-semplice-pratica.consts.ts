/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per la ricerca semplice di pratiche.
 */
 interface IRiscaRicercaSemplicePratiche {
    CODICE_UTENZA: string;
    NUMERO_AVVISO_PAGAMENTO: string;
    NUMERO_PRATICA: string;
    CODICE_FISCALE: string;
    RAGIONE_SOCIALE_COGNOME: string;
    CODICE_AVVISO: string;

    TEST: string;
  }
  
  /**
   * Oggetto contenente una serie di costanti per il componente di ricerca semplice di pratiche.
   */
  export const RiscaRicercaSemplicePratiche: IRiscaRicercaSemplicePratiche = {
    /** Costante che definisce il nome del campo form: codiceUtenza. */
    CODICE_UTENZA: 'codiceUtenza',
    /** Costante che definisce il nome del campo form: numeroAvvisoPagamento. */
    NUMERO_AVVISO_PAGAMENTO: 'numeroAvvisoPagamento',
    /** Costante che definisce il nome del campo form: numeroPratica. */
    NUMERO_PRATICA: 'numeroPratica',
    /** Costante che definisce il nome del campo form: codiceFiscale. */
    CODICE_FISCALE: 'codiceFiscale',
    /** Costante che definisce il nome del campo form: ragioneSocialeOCognome. */
    RAGIONE_SOCIALE_COGNOME: 'ragioneSocialeOCognome',
    /** Costante che definisce il nome del campo form: codiceAvviso. */
    CODICE_AVVISO: 'codiceAvviso',
    
    TEST: 'test',
  };