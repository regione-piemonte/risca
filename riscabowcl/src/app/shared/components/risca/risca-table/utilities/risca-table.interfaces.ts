import { RiscaTableBodyTabMethodData } from '../../../../utilities';
import { RiscaTableDataConfig } from './risca-table.classes';

/**
 * Interfaccia che definisce la struttura dell'oggetto gestito dal componente risca-table, con l'aggiunta delle proprietà custom definite dalla classe table che l'implementa.
 */
export interface IRiscaTableDataConfig {
  /** Oggetto any che contiene l'oggetto originale che compone la riga. */
  original: any;
  /** any che definisce la configurazione delle informazioni da visualizzare in tabella. Le proprietà devono essere definite sulla base della configurazione definita per il body della tabella, quindi: RiscaTableSourceMapBody.sourceData.property. */
  dataTable: any;
  /** string generato dalle logiche di gestione della tabella. Viene utilizzato per identificare univocamente una riga. */
  id: string;
  /** boolean che definisce che la riga è selezionata in caso di utilizzo di checkbox, o radio. Nel caso di radio button, solo il primo valore con "default" a true, verrà considerato come attivo per default. */
  selected?: boolean;
  /** Array di RiscaTableDataConfig<any> che una struttura "ricorsiva" per la gestione dei dati delle sotto righe. */
  tableDataConfigSubs?: RiscaTableDataConfig<any>[];
}

/**
 * Interfaccia che definisce la struttura dati per l'evento di azione custom.
 */
export interface IRiscaTableAzioneCustom<T> {
  action: RiscaTableBodyTabMethodData;
  data: RiscaTableDataConfig<T>;
}

/**
 * Interfaccia che definisce le informazioni emesse a seguito della pressione del pulsante custom della tabella.
 */
export interface IRiscaTableACEvent<T> {
  action: RiscaTableBodyTabMethodData;
  row: RiscaTableDataConfig<T>;
  subRow?: RiscaTableDataConfig<any>;
}

/**
 * Interfaccia che definisce la struttura dati per l'evento di Checkboxes changed.
 * Conterrà:
 * - La riga che è stata per ultima modificata;
 * - Un array di righe della tabella attualmente attive;
 */
export interface IRiscaTableCheboxesChange<T> {
  rowChanged: RiscaTableDataConfig<T>;
  rowsChecked: RiscaTableDataConfig<T>[];
}

/**
 * Interfaccia che definisce la struttura dei dati che verranno passati al componente padre nel momento in cui si farà un'azione su una sotto riga.
 */
export interface IRiscaTableSubRowData {
  row: RiscaTableDataConfig<any>;
  subRow: RiscaTableDataConfig<any>;
}
