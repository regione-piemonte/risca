import { getRataPadre } from 'src/app/features/pratiche/service/dati-contabili/dati-contabili/dati-contabili-utility.functions';
import {
  convertMomentToViewDate,
  formatoImportoITA,
} from 'src/app/shared/services/risca/risca-utilities/risca-utilities.functions';
import { PraticaVo } from '../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import {
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';
import { round } from 'lodash';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IRiepilogoSDATable {
  // codiceUtenza?: string;
  importoDovuto?: number;
  dataScadenza?: string;
  interessiDovuti: number;
  importoVersato?: number;
  importoDiCanoneMancante?: number;
  interessiMancanti?: number;
  interessiVersati?: number;
  importoRimborsato?: number;
  dataPagamento?: string;
  stato?: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface RiepilogoSDAccertamentiConfigs {
  statoDebitorio?: StatoDebitorioVo;
  disableUserInputs?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati per i componenti gruppo dei dati anagrafici.
 */
export class RiepilogoSDAccertamentiTable
  extends RiscaTableClass<IRiepilogoSDATable>
  implements IRiscaTableClass<IRiepilogoSDATable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Codice utenza. */
  private STYLE_CODICE_UTENZA = {
    'min-width': '120px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Importo dovuto. */
  private STYLE_IMPORTO_DOVUTO = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Data scadenza. */
  private STYLE_DATA_SCADENZA = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Interessi dovuti. */
  private STYLE_INTERESSI_DOVUTI = {
    'max-width': '250px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Importo eccedente. */
  private STYLE_IMPORTO_VERSATO = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '110px',
  };

  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Importo canone mancante. */
  private STYLE_IMPORTO_CANONE_MANCANTE = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '110px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Interessi mancanti. */
  private STYLE_INTERESSI_MANCANTI = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '110px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Interessi versati. */
  private STYLE_INTERESSI_VERSATI = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '110px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Importo rimborsato. */
  private STYLE_IMPORTO_RIMBORSATO = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '110px',
  };

  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Data pagamento. */
  private STYLE_DATA_PAGAMENTO = {
    'max-width': '250px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Stato. */
  private STYLE_STATO = {
    'max-width': '250px',
    'text-align': 'center',
  };

  // Oggetto della pratica a cui appartiene lo stato debitorio
  private _pratica: PraticaVo;
  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _disableUserInputs: boolean;

  /**
   * Costruttore.
   */
  constructor(configs: RiepilogoSDAccertamentiConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { statoDebitorio, disableUserInputs } = configs;
    // Assegno le variabili
    this._disableUserInputs = disableUserInputs || false;
    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements([statoDebitorio]);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    this.converter = (sd: StatoDebitorioVo) => {
      // Verifico l'input
      if (!sd) {
        // Nessuna configurazione
        return undefined;
      }

      // Gestione dati con le rate
      const rataPadre = getRataPadre(sd.rate);
      const dtScadRP = rataPadre?.data_scadenza_pagamento;
      // Gestione data pagamento dello stato debitorio
      const dtPagSD = sd.data_pagamento;
      // Recupero dello stato riscossione
      const praticaSR = this._pratica?.stato_riscossione?.des_stato_riscossione;

      // Definisco le proprietà che verranno tornate come parsing
      const importoDovuto = sd.canone_dovuto ?? 0;
      const dataScadenza = convertMomentToViewDate(dtScadRP) ?? '';
      // [VF] Issue 90 nella colonna 'interessi dovuti' deve essere riportato il valore degli 'interessi dovuti' + 'spese di notifica' e non solo gli interessi dovuti
      // const interessiDovuti = rataPadre?.interessi_maturati;
      const interessiMaturati = rataPadre?.interessi_maturati ?? 0;
      const speseNotifica = sd.imp_spese_notifica ?? 0;
      let interessiDovuti = interessiMaturati + speseNotifica;
      interessiDovuti = round(interessiDovuti, 2);

      const importoVersato = sd.importo_versato;

      const importoDiCanoneMancante = sd.acc_importo_di_canone_mancante;
      const interessiMancanti = sd.acc_interessi_mancanti
      const interessiVersati = sd.acc_interessi_versati;
      const importoRimborsato = sd.acc_importo_rimborsato;

      const dataPagamento = convertMomentToViewDate(dtPagSD) ?? '';
      const stato = praticaSR ?? '';

      // Effettuo il parsing  delle informazioni in input
      const row: IRiepilogoSDATable = {
        // codiceUtenza,
        importoDovuto,
        dataScadenza,
        interessiDovuti,
        importoVersato,
        importoDiCanoneMancante,
        interessiMancanti,
        interessiVersati,
        importoRimborsato,
        dataPagamento,
        stato,
      };

      // Ritorno la configurazione della riga
      return row;
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
    // Creo il setup css
    this.cssConfig = new RiscaTableCss();
  }

  /**
   * Funzione di setup per la configurazione dati.
   */
  setupDataConfig() {
    // Creo la configurazione RiscaTableSourceMap
    const sourceMap: RiscaTableSourceMap[] = [];
    // Inserisco all'interno della sourceMap le configurazioni per le colonne
    sourceMap.push(
      // {
      //   header: new RiscaTableSourceMapHeader({
      //     label: 'Codice utenza',
      //     sortable: false,
      //     cssCell: this.STYLE_CODICE_UTENZA,
      //   }),
      //   body: new RiscaTableSourceMapBody({
      //     useSource: true,
      //     sourceData: {
      //       property: 'codiceUtenza',
      //       type: 'string',
      //     },
      //     cssCell: this.STYLE_CODICE_UTENZA,
      //   }),
      // },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo dovuto',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_DOVUTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoDovuto',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
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
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Interessi dovuti',
          sortable: false,
          cssCell: this.STYLE_INTERESSI_DOVUTI,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'interessiDovuti',
            type: 'string',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_INTERESSI_DOVUTI,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo versato',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_VERSATO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoVersato',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_VERSATO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Imp. di canone mancante',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_CANONE_MANCANTE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoDiCanoneMancante',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_CANONE_MANCANTE,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Interessi mancanti',
          sortable: false,
          cssCell: this.STYLE_INTERESSI_MANCANTI,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'interessiMancanti',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_INTERESSI_MANCANTI,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Interessi versati',
          sortable: false,
          cssCell: this.STYLE_INTERESSI_MANCANTI,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'interessiVersati',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_INTERESSI_VERSATI,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Imp. rimborsato',
          sortable: false,
          cssCell: this.STYLE_INTERESSI_MANCANTI,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoRimborsato',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_INTERESSI_MANCANTI,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data pagamento',
          sortable: false,
          cssCell: this.STYLE_DATA_PAGAMENTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataPagamento',
            type: 'string',
          },
          cssCell: this.STYLE_DATA_PAGAMENTO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Stato',
          sortable: false,
          cssCell: this.STYLE_STATO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'stato',
            type: 'string',
          },
          cssCell: this.STYLE_STATO,
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}
