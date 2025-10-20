import { Moment } from 'moment';
import { RataVo } from 'src/app/core/commons/vo/rata-vo';
import {
  convertMomentToViewDate,
  formatoImportoITA,
} from 'src/app/shared/services/risca/risca-utilities/risca-utilities.functions';
import { PraticaVo } from '../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { dataScadenzaRataPadre } from '../../../../features/pratiche/service/dati-contabili/dati-contabili/dati-contabili-utility.functions';
import {
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IRiepilogoSDRTable {
  // codiceUtenza?: string;
  importoDovuto?: number;
  dataScadenza?: string;
  interessiDovuti: number;
  importoVersato?: number;
  importoEccedente?: number;
  importoRimborsoCompensazione?: number;
  dataPagamento?: string;
  stato?: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface RiepilogoSDRTableConfigs {
  pratica?: PraticaVo;
  statoDebitorio?: StatoDebitorioVo;
  disableUserInputs?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati per i componenti gruppo dei dati anagrafici.
 */
export class RiepilogoSDRimborsiTable
  extends RiscaTableClass<IRiepilogoSDRTable>
  implements IRiscaTableClass<IRiepilogoSDRTable>
{
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
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Importo versato. */
  private STYLE_IMPORTO_VERSATO = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '110px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Importo eccedente. */
  private STYLE_IMPORTO_ECCEDENTE = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '110px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Rimborso compensazione. */
  private STYLE_RIMBORSO_COMPENSAZIONE = {
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
  constructor(configs: RiepilogoSDRTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le proprietà dalla configurazione
    const { statoDebitorio, pratica, disableUserInputs } = configs;
    // Assegno le variabili
    this._disableUserInputs = disableUserInputs || false;
    // Setto la pratica
    this._pratica = pratica;
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
    this.converter = (statoDebitorio: StatoDebitorioVo) => {
      if (!statoDebitorio) {
        return undefined;
      }

      /**
       * ############################################################
       * FUNZIONI DI UTILITY PER LA CONVERSIONE DELLO STATO DEBITORIO
       * ############################################################
       */
      /**
       * Ottiene la data scadenza dallo stato debitorio
       * @param statoDebitorio StatoDebitorioVo
       * @returns data scadenza
       */
      const getDataScadenza = (statoDebitorio: StatoDebitorioVo) => {
        // Se lo trovo restituisco data_scadenza_pagamento, altrimenti undefined
        const data = dataScadenzaRataPadre(statoDebitorio?.rate as RataVo[]);
        // Data convertita
        return convertMomentToViewDate(data);
      };

      /**
       * Ottiene il totale degli interessi dovuti dallo stato debitorio
       * @param s StatoDebitorioVo
       * @returns string gli interessi dovuti
       */
      const getInteressiDovuti = (statoDebitorio: StatoDebitorioVo) => {
        // Controllo di esistenza
        if (!statoDebitorio) {
          return 0;
        }
        // Sommo gli interessi maturati
        let interessiMaturati = 0;
        // Ciclo sulle rate
        statoDebitorio.rate?.forEach((r) => {
          // Aggiungo gli interessi
          interessiMaturati += r.interessi_maturati ?? 0;
        });
        // Sommo l'importo spese notifica
        const tot =
          interessiMaturati + (statoDebitorio?.imp_spese_notifica ?? 0);
        // Ritorno il risultato
        return tot;
      };

      /**
       * Ottiene lo stato della riscossione a partire dalla pratica
       * @param pratica PraticaVo pratica associata allo stato debitorio
       * @returns string descrizione dello stato della pratica
       */
      const getStatoRiscossione = (pratica: PraticaVo) => {
        // Controllo di esistenza
        if (!pratica || !pratica.stato_riscossione) {
          return '';
        }
        // Formatto il valore
        return pratica.stato_riscossione.des_stato_riscossione;
      };

      /**
       * Ottiene la data pagamento della riscossione
       * @param statoDebitorio StatoDebitorioVo dello stato debitorio
       * @returns string con la data formattata per la View
       */
      const getDataPagamento = (statoDebitorio: StatoDebitorioVo) => {
        if (!statoDebitorio) {
          return undefined;
        }
        // Data pagamento dallo stato debitorio, come richiesto su JIRA 523.
        return convertMomentToViewDate(statoDebitorio.data_pagamento as Moment);
      };

      // Definisco le proprietà che verranno tornate come parsing
      // const codiceUtenza = getCodiceUtenza(this.pratica, statoDebitorio);
      const importoDovuto = statoDebitorio.canone_dovuto;
      const dataScadenza = getDataScadenza(statoDebitorio);
      const interessiDovuti = getInteressiDovuti(statoDebitorio);
      const importoVersato = statoDebitorio.importo_versato ?? 0;
      const importoEccedente = statoDebitorio.importo_eccedente;

      const impCompCanone = statoDebitorio.imp_compensazione_canone ?? 0;
      const accImportoRimborsato = statoDebitorio.acc_importo_rimborsato ?? 0;
      // const importoRimborsoCompensazione = impCompCanone + accImportoRimborsato;
      const importoRimborsoCompensazione = accImportoRimborsato;
      // const importoRimborsoCompensazione = statoDebitorio.imp_compensazione_canone;
      
      const dataPagamento = getDataPagamento(statoDebitorio);
      const stato = getStatoRiscossione(this._pratica);

      // Effettuo il parsing  delle informazioni in input
      return {
        // codiceUtenza,
        importoDovuto,
        dataScadenza,
        interessiDovuti,
        importoVersato,
        importoEccedente,
        importoRimborsoCompensazione,
        dataPagamento,
        stato,
      } as IRiepilogoSDRTable;
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
      // RISCA-520: rimuovere codice utenza da questa tabella
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
          cssCell: this.STYLE_IMPORTO_DOVUTO,
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
          label: 'Importo eccedente',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_ECCEDENTE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoEccedente',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_ECCEDENTE,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo rimborsato/compensato',
          sortable: false,
          cssCell: this.STYLE_RIMBORSO_COMPENSAZIONE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoRimborsoCompensazione',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_RIMBORSO_COMPENSAZIONE,
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
