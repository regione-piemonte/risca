import { Moment } from 'moment';
import { DocumentiAllegatiConsts } from 'src/app/features/pratiche/consts/documenti-allegati/documenti-allegati.consts';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { PraticaVo } from '../../../../core/commons/vo/pratica-vo';
import { RataVo } from '../../../../core/commons/vo/rata-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import {
  annualitaPiuRecente,
  dataScadenzaRataPadre,
} from '../../../../features/pratiche/service/dati-contabili/dati-contabili/dati-contabili-utility.functions';
import {
  convertMomentToViewDate,
  formatoImportoITA,
  statoSDFlagAnnullato,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaSortTypes,
  RiscaTableCss,
  RiscaTablePagination,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IStatiDebitoriTable {
  nap?: string;
  annualita?: string;
  canoneDovuto: number;
  interessiESpeseNotifica?: number;
  dataScadenza?: string;
  importoVersato?: number;
  dataPagamento?: string;
  importoCompensatoRimborsato?: number;
  attivita?: string;
  importoMancanteEccedente?: number;
  statoContribuzione?: string;
  stato?: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface StatiDebitoriTableConfigs {
  statiDebitori?: StatoDebitorioVo[];
  pratica: PraticaVo;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati per i componenti gruppo dei dati anagrafici.
 */
export class StatiDebitoriDCTable
  extends RiscaTableClass<IStatiDebitoriTable>
  implements IRiscaTableClass<IStatiDebitoriTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella con il radiobutton di selezione. */
  private STYLE_SELEZIONA = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_NAP = {
    'min-width': '130px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_ANNUALITA = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_CANONE_DOVUTO_H = {
    'text-align': 'center',
  };
  private STYLE_CANONE_DOVUTO_B = {
    'text-align': 'right',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_INTERESSI_E_SPESE_NOTIFICA_H = {
    'max-width': '250px',
    'text-align': 'center',
  };
  private STYLE_INTERESSI_E_SPESE_NOTIFICA_B = {
    'text-align': 'right',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_SCADENZA = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '130px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_VERSATO_H = {
    'max-width': '250px',
    'text-align': 'center',
  };
  private STYLE_IMPORTO_VERSATO_B = {
    'text-align': 'right',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_DATA_PAGAMENTO = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '110px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_COMPENSATO_RIMBORSATO_H = {
    'max-width': '250px',
    'text-align': 'center',
  };
  private STYLE_IMPORTO_COMPENSATO_RIMBORSATO_B = {
    'text-align': 'right',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_ATTIVITA = {
    'max-width': '250px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_MANCANTE_ECCEDENTE_H = {
    'max-width': '275px',
    'text-align': 'center',
  };
  private STYLE_IMPORTO_MANCANTE_ECCEDENTE_B = {
    'text-align': 'right',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_STATO_CONTRIBUZIONE = {
    'max-width': '250px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_STATO = {
    'max-width': '250px',
    'text-align': 'center',
  };

  /** Oggetto con le costanti comuni. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti per la sezione dei documenti allegati. */
  DA_C = DocumentiAllegatiConsts;

  /**
   * Costruttore.
   */
  constructor(configs: StatiDebitoriTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { statiDebitori } = configs;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(statiDebitori);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (sd: StatoDebitorioVo) => {
      // Verifico l'oggetto esista
      if (!sd) {
        return undefined;
      }

      // Gestisco il dato del nap
      const multiNap = sd.multi_nap;
      const nap = `${sd.nap ?? ''}${multiNap ? '*' : ''}`;

      // Gestisco il dato dell'annualità
      const multiA = sd.annualita_sd?.length > 1;
      const recentA = annualitaPiuRecente(sd.annualita_sd);
      const annualita = `${recentA?.anno ?? ''}${multiA ? '+' : ''}`;

      const canoneDovuto = sd.canone_dovuto;
      // Interessi e spese di notifica
      const interessiESpeseNotifica = sd.interessi_maturati_spese_di_notifica;

      // Gestisco la scadenza per le rate
      const dataScadenzaRNP = dataScadenzaRataPadre(sd.rate as RataVo[]);
      const dataSRView = convertMomentToViewDate(dataScadenzaRNP as Moment);
      const dataScadenza = `${dataSRView}`;

      const importoVersato = sd.importo_versato;
      const dataPagamento = convertMomentToViewDate(sd.data_pagamento);
      // Gestione importo compensato/rimborsato
      const impCompCanone = sd.imp_compensazione_canone ?? 0;
      const accImportoRimborsato = sd.acc_importo_rimborsato ?? 0;
      // const importoCompensatoRimborsato = impCompCanone + accImportoRimborsato;
      const importoCompensatoRimborsato = accImportoRimborsato;
      const attivita = sd.attivita;
      const importoMancanteEccedente = sd.imp_mancante_imp_eccedente;
      const statoContribuzione =
        sd.stato_contribuzione?.des_stato_contribuzione;
      const stato = statoSDFlagAnnullato(sd);

      // Crep l'oggetto da passare alla tabella
      const configs: IStatiDebitoriTable = {
        nap,
        annualita,
        canoneDovuto,
        interessiESpeseNotifica,
        dataScadenza,
        importoVersato,
        dataPagamento,
        importoCompensatoRimborsato,
        attivita,
        importoMancanteEccedente,
        statoContribuzione,
        stato,
      };

      // Ritorno l'oggetto di configurazione
      return configs;
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
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Seleziona',
          sortable: false,
        }),
        body: new RiscaTableSourceMapBody({
          cssCell: this.STYLE_SELEZIONA,
          useTabMethod: true,
          tabMethodData: { actions: [{ action: 'radio' }] },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'NAP',
          sortable: false,
          cssCell: this.STYLE_NAP,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'nap',
            type: 'string',
          },
          cssCell: this.STYLE_NAP,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Annualità',
          sortable: false,
          cssCell: this.STYLE_ANNUALITA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'annualita',
            type: 'string',
          },
          cssCell: this.STYLE_ANNUALITA,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Canone dovuto',
          sortable: false,
          cssCell: this.STYLE_CANONE_DOVUTO_H,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'canoneDovuto',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_CANONE_DOVUTO_B,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Interessi e Spese di notifica',
          sortable: false,
          cssCell: this.STYLE_INTERESSI_E_SPESE_NOTIFICA_H,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'interessiESpeseNotifica',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_INTERESSI_E_SPESE_NOTIFICA_B,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Scadenza',
          sortable: true,
          cssCell: this.STYLE_SCADENZA,
          sortType: RiscaSortTypes.decrescente,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataScadenza',
            type: 'date',
          },
          cssCell: this.STYLE_SCADENZA,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo Versato',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_VERSATO_H,
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
          cssCell: this.STYLE_IMPORTO_VERSATO_B,
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
            type: 'date',
          },
          cssCell: this.STYLE_DATA_PAGAMENTO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo compensato o rimborsato',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_COMPENSATO_RIMBORSATO_H,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoCompensatoRimborsato',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_COMPENSATO_RIMBORSATO_B,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Attività',
          sortable: false,
          cssCell: this.STYLE_ATTIVITA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'attivita',
            type: 'string',
          },
          cssCell: this.STYLE_ATTIVITA,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Mancante -\nEccedente +',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_MANCANTE_ECCEDENTE_H,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoMancanteEccedente',
            type: 'number',
            outputFormat: (v: number): string => {
              // Dichiaro la label di visualizzazione
              let value = '';
              // Verifico se il valore è negativo
              value = `${v >= 0 ? '+' : '-'}${value}`;
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_MANCANTE_ECCEDENTE_B,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Stato contribuzione',
          sortable: false,
          cssCell: this.STYLE_STATO_CONTRIBUZIONE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'statoContribuzione',
            type: 'string',
          },
          cssCell: this.STYLE_STATO_CONTRIBUZIONE,
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

    // Definisco la paginazione
    const pagination: RiscaTablePagination = this.getDefaultPagination();
    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, pagination };
  }

  /**
   * Genera la paginazione di default
   * @returns RiscaTablePagination come paginazione di default
   */
  getDefaultPagination(): RiscaTablePagination {
    return {
      total: this.source?.length || 0,
      label: 'Risultati di ricerca',
      elementsForPage: 5,
      showTotal: true,
      sortBy: 'dataScadenza', // 'dataPagamento',
      sortDirection: RiscaSortTypes.decrescente,
      currentPage: 1,
      maxPages: 10,
    };
  }
}
