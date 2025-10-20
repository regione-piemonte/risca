import { convertMomentToViewDate } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import { RataVo } from '../../../../core/commons/vo/rata-vo';
import { formatoImportoITA } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from './../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati impiegato come configurazione della tabella risca-table.
 */
export interface IRateSDTable {
  importoRata: number;
  dataScadenza: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface RateSDTableConfigs {
  rate?: RataVo[];
}

/**
 * Classe usata per la generazione delle risca-table per la visualizzazione delle informazioni.
 */
export class RateSDTable
  extends RiscaTableClass<IRateSDTable>
  implements IRiscaTableClass<IRateSDTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_RATA = {
    'min-width': '120px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_DATA_SCADENZA = {
    'text-align': 'center',
  };

  /**
   * Costruttore.
   */
  constructor(configs: RateSDTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { rate } = configs;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(rate);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (rata: RataVo) => {
      // Verifico l'oggetto esista
      if (!rata) {
        return undefined;
      }

      // Definisco le proprietà che verranno tornate come parsing
      const importoRata = rata.canone_dovuto;
      const dataScadenza = convertMomentToViewDate(rata.data_scadenza_pagamento);

      // Effettuo il parsing  delle informazioni in input
      const config: IRateSDTable = { importoRata, dataScadenza };

      // Ritorno la configurazione
      return config;
    };
  }

  /**
   * Funzione di setup per la classe.
   */
  private setupClasse() {
    // Lancio il setup del cssConfig
    this.setupCssConfig();
    // Lancio il setup del cssConfig
    this.setupDataConfig();
  }

  /**
   * Funzione di setup per la configurazione css.
   */
  setupCssConfig() {
    // Definisco le configurazioni per gli stili
    const table = {
      'min-width': '320px',
    };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(undefined, { table });
  }

  /**
   * Funzione di setup per la configurazione dati.
   */
  setupDataConfig() {
    // Creo la configurazione RiscaTableSourceMap
    const sourceMap: RiscaTableSourceMap[] = [];
    // Inserisco all'interno della sourceMap le configurazioni per le colonne
    sourceMap.push(
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo della rata',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_RATA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoRata',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_RATA,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data scadenza',
          sortable: false,
          cssCell: this.STYLE_DATA_SCADENZA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataScadenza',
            type: 'string',
          },
          cssCell: this.STYLE_DATA_SCADENZA,
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}
