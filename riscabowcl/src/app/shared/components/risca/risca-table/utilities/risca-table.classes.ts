import { IRiscaTableDataConfig } from './risca-table.interfaces';

/**
 * Classe che definisce la struttura dell'oggetto gestito dal componente risca-table, con l'aggiunta delle proprietà custom definite dalla classe table che l'implementa.
 */
export class RiscaTableDataConfig<T> {
  /** Oggetto T che contiene l'oggetto originale che compone la riga. */
  original: T;
  /** any che definisce la configurazione delle informazioni da visualizzare in tabella. Le proprietà devono essere definite sulla base della configurazione definita per il body della tabella, quindi: RiscaTableSourceMapBody.sourceData.property. */
  dataTable: any;
  /** string generato dalle logiche di gestione della tabella. Viene utilizzato per identificare univocamente una riga. */
  id: string;
  /** boolean che definisce che la riga è selezionata in caso di utilizzo di checkbox, o radio. Nel caso di radio button, solo il primo valore con "default" a true, verrà considerato come attivo per default. */
  selected: boolean = false;
  /** Array di RiscaTableDataConfig<any> che una struttura "ricorsiva" per la gestione dei dati delle sotto righe. */
  tableDataConfigSubs?: RiscaTableDataConfig<any>[];

  /**
   * Costruttore.
   */
  constructor(c: IRiscaTableDataConfig) {
    // Verifico l'input
    if (!c) {
      throw new Error('RiscaTableDataConfig | No configuration provided');
    }

    this.original = c.original;
    this.dataTable = c.dataTable;
    this.tableDataConfigSubs = c.tableDataConfigSubs;
    this.id = c.id;
    this.selected = c.selected !== undefined ? c.selected : false;
  }
}
