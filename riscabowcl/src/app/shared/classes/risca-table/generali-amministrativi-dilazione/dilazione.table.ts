import { DilazioneVo } from '../../../../core/commons/vo/dilazione-vo';
import {
  convertMomentToViewDate,
  formatoImportoITA,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
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
export interface IDilazioneSDTable {
  numAnnualitaMagDi: number;
  importoMagDi: number;
  importoMinDi: number;
  numMesi: number;
  numRate: number;
  dataInizioValidita: string;
  dataFineValidita: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface DilazioneSDTableConfigs {
  dilazioni?: DilazioneVo[];
}

/**
 * Classe usata per la generazione delle risca-table per la visualizzazione delle informazioni.
 */
export class DilazioneSDTable
  extends RiscaTableClass<IDilazioneSDTable>
  implements IRiscaTableClass<IDilazioneSDTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_NUM_ANNUALITA_MAG_DI = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_MAG_DI = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_MIN_DI = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_NUM_MESI = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_NUM_RATE = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_DATA_INIZIO_VALIDITA = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_DATA_FINE_VALIDITA = {};

  /**
   * Costruttore.
   */
  constructor(configs: DilazioneSDTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { dilazioni } = configs;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(dilazioni);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (dilazione: DilazioneVo) => {
      // Verifico l'oggetto esista
      if (!dilazione) {
        return undefined;
      }

      // Definisco le proprietà che verranno tornate come parsing
      const numAnnualitaMagDi = dilazione.num_annualita_magg;
      const importoMagDi = dilazione.importo_min;
      const importoMinDi = dilazione.importo_magg;
      const numMesi = dilazione.num_mesi;
      const numRate = dilazione.num_rate;
      const dataInizioValidita = convertMomentToViewDate(
        dilazione.data_inizio_val
      );
      const dataFineValidita = convertMomentToViewDate(dilazione.data_fine_val);

      // Effettuo il parsing  delle informazioni in input
      const config: IDilazioneSDTable = {
        numAnnualitaMagDi,
        importoMagDi,
        importoMinDi,
        numMesi,
        numRate,
        dataInizioValidita,
        dataFineValidita,
      };

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
      'min-width': '550px',
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
          label: 'Numero annualità > di',
          sortable: false,
          cssCell: this.STYLE_NUM_ANNUALITA_MAG_DI,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'numAnnualitaMagDi',
            type: 'number',
          },
          cssCell: this.STYLE_NUM_ANNUALITA_MAG_DI,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo > di',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_MAG_DI,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoMagDi',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_MAG_DI,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo < di',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_MIN_DI,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoMinDi',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_MIN_DI,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Numero mesi',
          sortable: false,
          cssCell: this.STYLE_NUM_MESI,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'numMesi',
            type: 'number',
          },
          cssCell: this.STYLE_NUM_MESI,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Numero rate',
          sortable: false,
          cssCell: this.STYLE_NUM_RATE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'numRate',
            type: 'number',
          },
          cssCell: this.STYLE_NUM_RATE,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data inizio validità',
          sortable: false,
          cssCell: this.STYLE_DATA_INIZIO_VALIDITA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataInizioValidita',
            type: 'string',
          },
          cssCell: this.STYLE_DATA_INIZIO_VALIDITA,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data fine validità',
          sortable: false,
          cssCell: this.STYLE_DATA_FINE_VALIDITA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataFineValidita',
            type: 'string',
          },
          cssCell: this.STYLE_DATA_FINE_VALIDITA,
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}
