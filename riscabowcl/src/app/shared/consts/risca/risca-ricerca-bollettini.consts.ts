/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per la ricerca semplice di pratiche.
 */
interface IRRBollettiniPagamenti {
  TIPO: string;
  STATO: string;
  DATA_RICHIESTA_INIZIO: string;
  DATA_RICHIESTA_FINE: string;

  PROPERTY_TIPO: string;
  PROPERTY_STATO: string;

  LABEL_TIPO: string;
  LABEL_STATO: string;
  LABEL_DATA: string;
  LABEL_DATA_DA: string;
  LABEL_DATA_A: string;

  RIC_BOLL_CONFERMA: string;
  RIC_BOLL_ANNULLA: string;
  RIC_BOLL_SCARICA_FILE: string;
}

/**
 * Oggetto contenente una serie di costanti per il componente di ricerca semplice di pratiche.
 */
export const RRBollettiniPagamenti: IRRBollettiniPagamenti = {
  /** Costante che definisce il nome del campo form: tipo. */
  TIPO: 'tipo',
  /** Costante che definisce il nome del campo form: stato. */
  STATO: 'stato',
  /** Costante che definisce il nome del campo form: dataRichiestaInizio. */
  DATA_RICHIESTA_INIZIO: 'dataRichiestaInizio',
  /** Costante che definisce il nome del campo form: dataRichiestaFine. */
  DATA_RICHIESTA_FINE: 'dataRichiestaFine',

  /** Costante che definisce il nome del campo form: dataRichiestaFine. */
  PROPERTY_TIPO: 'des_tipo_elabora',
  /** Costante che definisce il nome del campo form: dataRichiestaFine. */
  PROPERTY_STATO: 'des_stato_elabora',
  
  /** Costante che definisce la label del tipo */
  LABEL_TIPO: 'Tipo',
  /** Costante che definisce la label dello stato */
  LABEL_STATO: 'Stato',
  /** Costante che definisce la label della data */
  LABEL_DATA: 'Data elaborazione',
  /** Costante che definisce la label a sinistra della data richiesta inizio */
  LABEL_DATA_DA: 'da',
  /** Costante che definisce la label a sinistra della data richiesta fine */
  LABEL_DATA_A: 'a',

  /** Costante che definisce l'azione di conferma nella tabella dei bollettini */
  RIC_BOLL_CONFERMA: 'conferma',
  /** Costante che definisce l'azione di annullamento nella tabella dei bollettini */
  RIC_BOLL_ANNULLA: 'annulla',
  /** Costante che definisce l'azione di scarico file nella tabella dei bollettini */
  RIC_BOLL_SCARICA_FILE: 'scarica_file',
};
