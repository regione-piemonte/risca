import { Moment } from 'moment';
import { DocumentiAllegatiConsts } from 'src/app/features/pratiche/consts/documenti-allegati/documenti-allegati.consts';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { RataVo } from '../../../../core/commons/vo/rata-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import {
  annualitaRiferimento,
  dataScadenzaRataPadre,
} from '../../../../features/pratiche/service/dati-contabili/dati-contabili/dati-contabili-utility.functions';
import { RiscaTableDataConfig } from '../../../components/risca/risca-table/utilities/risca-table.classes';
import {
  convertMomentToViewDate,
  formatoImportoITA,
  statoSDFlagAnnullato,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaSortTypes,
  RiscaTableBodyTabMethods,
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
export interface IPagamentoStatiDebitoriTable {
  codiceUtenza?: string;
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
  numeroPratica?: string;
  titolare?: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface PagamentoStatiDebitoriTableConfigs {
  statiDebitori?: StatoDebitorioVo[];
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati per gli stati debitori per pagamento.
 */
export class PagamentoStatiDebitoriTable
  extends RiscaTableClass<IPagamentoStatiDebitoriTable>
  implements IRiscaTableClass<IPagamentoStatiDebitoriTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella con il radiobutton di selezione. */
  private STYLE_SELEZIONA_H = {};
  private STYLE_SELEZIONA_B = { 'text-align': 'center' };
  private STYLE_NUMERO_PRATICA = { 'text-align': 'center' };
  private STYLE_TITOLARE = { 'text-align': 'center' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_CODICE_UTENZA = {
    'min-width': '150px',
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
  private STYLE_CANONE_DOVUTO_H = { 'text-align': 'center' };
  private STYLE_CANONE_DOVUTO_B = { 'text-align': 'right' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_INTERESSI_E_SPESE_NOTIFICA_H = {
    width: '250px',
    'text-align': 'center',
  };
  private STYLE_INTERESSI_E_SPESE_NOTIFICA_B = { 'text-align': 'right' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_SCADENZA = {
    width: '250px',
    'text-align': 'center',
    'min-width': '130px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_VERSATO_H = {
    width: '250px',
    'text-align': 'center',
  };
  private STYLE_IMPORTO_VERSATO_B = { 'text-align': 'right' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_DATA_PAGAMENTO = {
    width: '250px',
    'text-align': 'center',
    'min-width': '110px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_COMPENSATO_RIMBORSATO_H = {
    width: '250px',
    'text-align': 'center',
  };
  private STYLE_IMPORTO_COMPENSATO_RIMBORSATO_B = { 'text-align': 'right' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_ATTIVITA = {
    width: '250px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_MANCANTE_ECCEDENTE_H = {
    width: '250px',
    'text-align': 'center',
  };
  private STYLE_IMPORTO_MANCANTE_ECCEDENTE_B = { 'text-align': 'right' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_STATO_CONTRIBUZIONE = {
    width: '250px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_STATO = {
    width: '250px',
    'text-align': 'center',
  };

  /** Oggetto con le costanti comuni. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti per la sezione dei documenti allegati. */
  DA_C = DocumentiAllegatiConsts;

  /**
   * Costruttore.
   */
  constructor(configs?: PagamentoStatiDebitoriTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { statiDebitori } = configs || {};

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
    // Definisco le logiche per il comparatore degli oggetti originali
    this.comparatorOriginals = (
      originA: StatoDebitorioVo,
      originB: StatoDebitorioVo
    ) => {
      // Verifico che gli oggetti esistano
      if (!originA && !originB) {
        // Oggetti non definit
        return false;
      }
      // Definisco la condizione di comparazione tra i dati
      return originA?.id_stato_debitorio === originB?.id_stato_debitorio;
    };

    // Definisco le logiche per il comparatore delle righe
    this.comparatorRows = (
      a: RiscaTableDataConfig<StatoDebitorioVo>,
      b: RiscaTableDataConfig<StatoDebitorioVo>
    ): boolean => {
      // Recupero dagli oggetti di riga, le informazioni originali
      const originA: StatoDebitorioVo = a?.original;
      const originB: StatoDebitorioVo = b?.original;
      // Richiamo e ritorno la funzione di compare tra oggetti originali
      return this.comparatorOriginals(originA, originB);
    };

    // Definisco le logiche per il converter
    this.converter = (sd: StatoDebitorioVo) => {
      // Verifico l'oggetto esista
      if (!sd) {
        return undefined;
      }

      // Gestione del codice utenza
      const codiceUtenza = sd.cod_utenza;

      // Gestisco il dato del nap
      const multiNap = sd.multi_nap;
      const nap = `${sd.nap ?? ''}${multiNap ? '*' : ''}`;

      // Gestisco il dato dell'annualità
      const annualita = annualitaRiferimento(sd.annualita_sd);

      const canoneDovuto = sd.canone_dovuto;
      // Interessi e spese di notifica
      const interessiESpeseNotifica = sd.interessi_maturati_spese_di_notifica;

      // Gestisco la scadenza per le rate
      const dataScadenzaRNP = dataScadenzaRataPadre(sd.rate as RataVo[]);
      const dataSRView = convertMomentToViewDate(dataScadenzaRNP as Moment);
      const dataScadenza = `${dataSRView}`;

      const importoVersato = sd.importo_versato;
      const dataPagamento = convertMomentToViewDate(
        sd.data_pagamento as Moment
      );
      // const importoCompensatoRimborsato = sd.imp_compensazione_canone;
      const importoCompensatoRimborsato = sd.acc_importo_rimborsato;
      const attivita = sd.attivita;
      const importoMancanteEccedente = sd.imp_mancante_imp_eccedente;
      const statoContribuzione =
        sd.stato_contribuzione?.des_stato_contribuzione;
      const stato = statoSDFlagAnnullato(sd);

      // RISCA-ISSUES-35: parametri aggiunti agli stati debitori e gestiti per questa tabella specifica
      const numeroPratica: string = sd?.num_pratica;
      const titolare: string = sd?.titolare;

      // Crep l'oggetto da passare alla tabella
      const configs: IPagamentoStatiDebitoriTable = {
        codiceUtenza,
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
        numeroPratica,
        titolare,
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
    // Definisco le configurazioni per gli stili
    const table = { 'min-width': '1900px' };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(undefined, { table });
  }

  /**
   * Funzione di setup per la configurazione dati.
   */
  setupDataConfig() {
    // Creo la configurazione RiscaTableSourceMap
    let sourceMap: RiscaTableSourceMap[] = [];

    // Definisco le configurazioni delle colonne

    // SELEZIONA TUTTI
    const selezionaTutti: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Seleziona tutti',
        sortable: false,
        cssCell: this.STYLE_SELEZIONA_H,
        action: { checkbox: {} },
      }),
      body: new RiscaTableSourceMapBody({
        cssCell: this.STYLE_SELEZIONA_B,
        useTabMethod: true,
        tabMethodData: {
          actions: [
            {
              action: RiscaTableBodyTabMethods.check,
              checkboxConfigs: { isRowData: false },
              disable: (v?: RiscaTableDataConfig<StatoDebitorioVo>) => {
                // Verifico se il flag per tutti gli elementi selezionati è true
                return this.tutteLeRigheSelezionate;
              },
            },
          ],
        },
      }),
    };

    // NUMERO PRATICA
    const numeroPratica: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Numero pratica',
        sortable: false,
        cssCell: this.STYLE_NUMERO_PRATICA,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'numeroPratica',
          type: 'string',
        },
        cssCell: this.STYLE_NUMERO_PRATICA,
      }),
    };

    // TITOLARE
    const titolare: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Titolare',
        sortable: false,
        cssCell: this.STYLE_TITOLARE,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'titolare',
          type: 'string',
        },
        cssCell: this.STYLE_TITOLARE,
      }),
    };

    // CODICE UTENZA
    const codiceUtenza: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Codice utenza',
        sortable: false,
        cssCell: this.STYLE_CODICE_UTENZA,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'codiceUtenza',
          type: 'string',
        },
        cssCell: this.STYLE_CODICE_UTENZA,
      }),
    };

    // NAP
    const nap: RiscaTableSourceMap = {
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
    };

    // ANNUALITA'
    const annualita: RiscaTableSourceMap = {
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
    };

    // CANONE DOVUTO
    const canoneDovuto: RiscaTableSourceMap = {
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
    };

    // INTERESSI E SPESE DI NOTIFICA
    const interessiSpeseNotifica: RiscaTableSourceMap = {
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
    };

    // SCADENZA
    const scadenza: RiscaTableSourceMap = {
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
    };

    // IMPORTO VERSATO
    const importoVersato: RiscaTableSourceMap = {
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
    };

    // DATA PAGAMENTO
    const dataPagamento: RiscaTableSourceMap = {
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
    };

    // IMPORTO COMPENSATO RIMBORSATO
    const importoCompensatoRimborsato: RiscaTableSourceMap = {
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
    };

    // ATTIVITA
    const attivita: RiscaTableSourceMap = {
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
          ellipsisAt: 15,
        },
      }),
    };

    // IMPORTO MANCANTE ECCEDENTE
    const importoMancanteEccedente: RiscaTableSourceMap = {
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
    };

    // STATO CONTRIBUZIONE
    const statoContribuzione: RiscaTableSourceMap = {
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
      }),
    };

    // STATO
    const stato: RiscaTableSourceMap = {
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
      }),
    };

    // Inserisco all'interno della sourceMap le configurazioni per le colonne
    sourceMap = [
      selezionaTutti,
      numeroPratica,
      titolare,
      codiceUtenza,
      nap,
      annualita,
      canoneDovuto,
      interessiSpeseNotifica,
      scadenza,
      importoVersato,
      dataPagamento,
      importoCompensatoRimborsato,
      attivita,
      importoMancanteEccedente,
      statoContribuzione,
      stato,
    ];

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
    // Richiamo la funzione comune
    return getDefaultPaginationPagaSDTable();
  }
}

/**
 * Genera la paginazione di default
 * @returns RiscaTablePagination come paginazione di default
 */
export const getDefaultPaginationPagaSDTable = (): RiscaTablePagination => {
  return {
    total: 0,
    label: 'Risultati di ricerca',
    elementsForPage: 10,
    showTotal: true,
    sortBy: 'dataScadenza', // 'dataPagamento',
    sortDirection: RiscaSortTypes.decrescente,
    currentPage: 1,
    maxPages: 10,
  };
};

/**
 * ########################################################################################################################################################################################################
 * ########################################################################################################################################################################################################
 * ########################################################################################################################################################################################################
 */

/**
 * Interfaccia personalizzata che definisce la struttura dei dati visualizzati come colonne per la risca-table.
 */
interface ISDSelezionatixPagamentoTable {
  codiceUtenza: string;
  nap: string;
  anno: number;
}

/**
 * Interfaccia che rappresenta l'oggetto di configurazione della tabella degli sd selezionati.
 */
export interface ISDSelezionatixPagamentoTableConfigs {
  statiDebitori?: StatoDebitorioVo[];
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati selezionati dalla tabella "padre".
 */
export class SDSelezionatixPagamentoTable
  extends RiscaTableClass<ISDSelezionatixPagamentoTable>
  implements IRiscaTableClass<ISDSelezionatixPagamentoTable>
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** Configurazioni per le colonne della tabella. */
  private STYLE_CODICE_UTENZA = {
    'min-width': '260px',
    width: '260px',
    'max-width': '260px',
  };
  private STYLE_NAP = {};
  private STYLE_ANNO = {
    'min-width': '50px',
    width: '50px',
    'max-width': '50px',
  };
  private STYLE_AZIONI = {
    'min-width': '15px',
    width: '15px',
    'max-width': '15px',
  };

  /**
   * Costruttore.
   */
  constructor(sdxPagamentoConfigs?: ISDSelezionatixPagamentoTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo dalla configurazione i parametri
    const { statiDebitori } = sdxPagamentoConfigs || {};

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
      // Converto e definisco le informazioni per la tabella
      const codiceUtenza: string = sd?.cod_utenza;
      const nap: string = sd?.nap;
      const anno: number = sd?.anno;

      // Definisco l'oggetto che comporrà le colonne della tabella
      let mParsed: ISDSelezionatixPagamentoTable = {
        codiceUtenza,
        nap,
        anno,
      };
      // Ritorno l'oggetto convertito
      return mParsed;
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
    const table = { 'min-width': '555px' };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(undefined, { table });
  }

  /**
   * Funzione di setup per la configurazione dati.
   */
  setupDataConfig() {
    // Creo gli oggetti per la configurazione RiscaTableSourceMap e per il tracciatore
    // ############# codiceUtenza
    const codiceUtenza: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Codice Utenza',
        sortable: false,
        cssCell: this.STYLE_CODICE_UTENZA,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'codiceUtenza',
          type: 'string',
        },
      }),
    };

    // ############# nap
    const nap: RiscaTableSourceMap = {
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
      }),
    };

    // ############# codiceUtenza
    const anno: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Anno',
        sortable: false,
        cssCell: this.STYLE_ANNO,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'anno',
          type: 'number',
        },
      }),
    };

    // ############# azioni
    const azioni: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: '',
        cssCell: this.STYLE_AZIONI,
      }),
      body: new RiscaTableSourceMapBody({
        useTabMethod: true,
        tabMethodData: {
          actions: [{ action: RiscaTableBodyTabMethods.close }],
        },
      }),
    };

    // Creo la configurazione RiscaTableSourceMap per le colonne
    const sourceMap: RiscaTableSourceMap[] = [codiceUtenza, nap, anno, azioni];

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}
