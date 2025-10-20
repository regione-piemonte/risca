import { Moment } from 'moment';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { RicercaRimborsiConsts } from '../../../../../features/pagamenti/component/ricerca-rimborsi/utilities/ricerca-rimborsi.consts';
import { RiscaTableDataConfig } from '../../../../components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../consts/common-consts.consts';
import {
  RiscaIconsCss,
  RiscaIconsCssDisabled,
} from '../../../../enums/icons.enums';
import {
  convertMomentToViewDate,
  formatoImportoITA,
} from '../../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaSortTypes,
  RiscaTableBodyTabMethods,
  RiscaTableCss,
  RiscaTablePagination,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati visualizzati come colonne per la risca-table.
 */
interface IRicercaSDxRimborsiTable {
  codiceUtenza: string;
  importoDovuto: number;
  scadenza: string;
  interessiDovuti: number;
  importoVersato: number;
  importoEccedente: number;
  importoRimborsoCompensazione: number;
  dataPagamento: string;
  stato: string;
}

/**
 * Interfaccia che rappresenta l'oggetto di configurazione della tabella delle morosità.
 */
export interface IRicercaSDxRimborsiTableConfigs {
  statiDebitori?: StatoDebitorioVo[];
  tuttiSelezionati?: boolean;
  paginazione?: RiscaTablePagination;
}


/**
 * Funzione che ritorna un nuovo oggetto con la configurazione per la paginazione di default.
 */
export const RicercaSDxRimborsiTablePagination: () => RiscaTablePagination =
  () => {
    return {
      total: 0,
      label: 'Risultati di ricerca',
      elementsForPage: 10,
      showTotal: true,
      currentPage: 1,
      sortBy: '',
      sortDirection: RiscaSortTypes.nessuno,
      maxPages: 10,
    };
  };

/**
 * Classe usata per la generazione delle risca-table che contiene i dati di ricerca.
 */
export class RicercaSDxRimborsiTable
  extends RiscaTableClass<IRicercaSDxRimborsiTable>
  implements IRiscaTableClass<IRicercaSDxRimborsiTable>
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** RicercaRimborsiConsts come classe di costanti del componente. */
  RR_C = new RicercaRimborsiConsts();

  /** Configurazioni per le colonne della tabella. */
  private STYLE_SELEZIONA_HEAD = {};
  private STYLE_SELEZIONA_BODY = { 'text-align': 'center' };
  private STYLE_CODICE_UTENZA = {};
  private STYLE_IMPORTO_DOVUTO = {};
  private STYLE_SCADENZA = { 'min-width': '105px', width: '105px' };
  private STYLE_INTERESSI_DOVUTI = {};
  private STYLE_IMPORTO_VERSATO = {};
  private STYLE_IMPORTO_ECCEDENTE = {};
  private STYLE_IMPORTO_RIMBORSO_COMPENSAZIONE = {};
  private STYLE_DATA_PAGAMENTO = { 'min-width': '105px', width: '105px' };
  private STYLE_STATO = {};
  private STYLE_AZIONI = { 'min-width': '90px' };

  /**
   * Costruttore.
   */
  constructor(rimborsiConfigs?: IRicercaSDxRimborsiTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo dalla configurazione i parametri
    const { statiDebitori, tuttiSelezionati, paginazione } =
      rimborsiConfigs || {};
    // Imposto localmente le informazioni
    this.tutteLeRigheSelezionate = tuttiSelezionati ?? false;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(statiDebitori);
    // Lancio il setup delle configurazioni
    this.setupClasse();

    // La paginazione si può aggiornare solo dopo che la classe ha generato le informazioni di data config
    this.updatePaginazione(paginazione);
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
      // Converto e definisco le informazioni per la tabella
      const codiceUtenza: string = sd?.cod_utenza;
      const importoDovuto: number = sd?.importo_dovuto;
      const accDtScadPag: Moment = sd?.acc_data_scadenza_pag;
      const scadenza: string = convertMomentToViewDate(accDtScadPag);
      const interessiDovuti: number = sd?.interessi_maturati_spese_di_notifica;
      const importoVersato: number = sd?.acc_importo_versato;
      const importoEccedente: number = sd?.importo_eccedente;
      const importoRimborsoCompensazione: number = sd?.imp_compensazione_canone;
      const dataPagamento: string = convertMomentToViewDate(sd?.data_pagamento);
      const stato: string = sd?.stato_riscossione;

      // Definisco l'oggetto che comporrà le colonne della tabella
      let mParsed: IRicercaSDxRimborsiTable = {
        codiceUtenza,
        importoDovuto,
        scadenza,
        interessiDovuti,
        importoVersato,
        importoEccedente,
        importoRimborsoCompensazione,
        dataPagamento,
        stato,
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
    const table = {
      'min-width': '1225px',
    };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(undefined, { table });
  }

  /**
   * Funzione di setup per la configurazione dati.
   */
  setupDataConfig() {
    // Creo gli oggetti per la configurazione RiscaTableSourceMap e per il tracciatore
    // ############# selezionaTutti
    const selezionaTutti: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Seleziona tutti i risultati della ricerca',
        sortable: false,
        cssCell: this.STYLE_SELEZIONA_HEAD,
        action: { checkbox: {} },
      }),
      body: new RiscaTableSourceMapBody({
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
        cssCell: this.STYLE_SELEZIONA_BODY,
      }),
    };

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

    // ############# importoDovuto
    const importoDovuto: RiscaTableSourceMap = {
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
    };

    // ############# scadenza
    const scadenza: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Scadenza',
        sortable: false,
        cssCell: this.STYLE_SCADENZA,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'scadenza',
          type: 'date',
        },
      }),
    };

    // ############# interessiDovuti
    const interessiDovuti: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Interessi dovuti',
        sortable: false,
        cssCell: this.STYLE_INTERESSI_DOVUTI,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'interessiDovuti',
          type: 'number',
          outputFormat: (v: number): string => {
            // Ritorno la formattazione numerica per italia
            return formatoImportoITA(v);
          },
        },
      }),
    };

    // ############# importoVersato
    const importoVersato: RiscaTableSourceMap = {
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
      }),
    };

    // ############# importoEccedente
    const importoEccedente: RiscaTableSourceMap = {
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
      }),
    };

    // ############# impRimborsatoCompensazione
    const impRimborsatoCompensazione: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Imp. rimb. e/o compensazione',
        sortable: false,
        cssCell: this.STYLE_IMPORTO_RIMBORSO_COMPENSAZIONE,
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
      }),
    };

    // ############# dataPagamento
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
      }),
    };

    // ############# stato
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

    // ############# azioni
    const azioni: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Azioni',
        cssCell: this.STYLE_AZIONI,
      }),
      body: new RiscaTableSourceMapBody({
        useTabMethod: true,
        tabMethodData: {
          actions: [
            { action: 'detail' },
            {
              action: this.RR_C.SCARICA_FILE_ACTION,
              custom: true,
              title: this.C_C.LABEL_SCARICA_FILE,
              class: this.C_C.RISCA_TABLE_ACTION,
              style: { width: '18px' },
              iconEnabled: RiscaIconsCss.download_file,
              iconDisabled: RiscaIconsCssDisabled.download_file,
              disable: (v?: RiscaTableDataConfig<StatoDebitorioVo>) => {
                // Recupero l'oggetto originale
                const statoDebitorio = v?.original;
                // Recupero l'id_riscossione
                const idRiscossione = statoDebitorio?.id_riscossione;
                // Disabilito la stampa se non esiste l'id_riscossione
                return idRiscossione == undefined;
              },
            },
          ],
        },
      }),
    };

    // Creo la configurazione RiscaTableSourceMap per le colonne
    const sourceMap: RiscaTableSourceMap[] = [
      selezionaTutti,
      codiceUtenza,
      importoDovuto,
      scadenza,
      interessiDovuti,
      importoVersato,
      importoEccedente,
      impRimborsatoCompensazione,
      dataPagamento,
      stato,
      azioni,
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
    // Paginazione di default
    return RicercaSDxRimborsiTablePagination();
  }

  /**
   * #################
   * OVERRIDE FUNZIONI
   * #################
   */

  /**
   * Funzione di comodo che effettua la rimozione di tutti gli elementi della tabella.
   * @override
   */
  clear() {
    // Rimuovo gli elementi
    this.source = [];
    this.tutteLeRigheSelezionate = false;
  }
}

/**
 * ########################################################################################################################################################################################################
 * ########################################################################################################################################################################################################
 * ########################################################################################################################################################################################################
 */

/**
 * Interfaccia personalizzata che definisce la struttura dei dati visualizzati come colonne per la risca-table.
 */
interface ISDSelezionatixRimborsiTable {
  codiceUtenza: string;
  importoDovuto: number;
  scadenza: string;
  dataPagamento: string;
}

/**
 * Interfaccia che rappresenta l'oggetto di configurazione della tabella delle morosità.
 */
export interface ISDSelezionatixRimborsiTableConfigs {
  statiDebitori?: StatoDebitorioVo[];
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati selezionati dalla tabella "padre".
 */
export class SDSelezionatixRimborsiTable
  extends RiscaTableClass<ISDSelezionatixRimborsiTable>
  implements IRiscaTableClass<ISDSelezionatixRimborsiTable>
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** RicercaRimborsiConsts come classe di costanti del componente. */
  RR_C = new RicercaRimborsiConsts();

  /** Configurazioni per le colonne della tabella. */
  private STYLE_CODICE_UTENZA = {};
  private STYLE_IMPORTO_DOVUTO = {};
  private STYLE_SCADENZA = { 'min-width': '105px' };
  private STYLE_DATA_PAGAMENTO = { 'min-width': '105px' };
  private STYLE_AZIONI = { width: '15px', 'max-width': '15px' };

  /**
   * Costruttore.
   */
  constructor(sdxRimborsiConfigs?: ISDSelezionatixRimborsiTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo dalla configurazione i parametri
    const { statiDebitori } = sdxRimborsiConfigs || {};

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
      const importoDovuto: number = sd?.importo_dovuto;
      const accDtScadPag: Moment = sd?.acc_data_scadenza_pag;
      const scadenza: string = convertMomentToViewDate(accDtScadPag);
      const dataPagamento: string = convertMomentToViewDate(sd?.data_pagamento);

      // Definisco l'oggetto che comporrà le colonne della tabella
      let mParsed: ISDSelezionatixRimborsiTable = {
        codiceUtenza,
        importoDovuto,
        scadenza,
        dataPagamento,
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
    const table = { 'min-width': '815px' };
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

    // ############# importoDovuto
    const importoDovuto: RiscaTableSourceMap = {
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
    };

    // ############# scadenza
    const scadenza: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Scadenza',
        sortable: false,
        cssCell: this.STYLE_SCADENZA,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'scadenza',
          type: 'date',
        },
      }),
    };

    // ############# dataPagamento
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
    const sourceMap: RiscaTableSourceMap[] = [
      codiceUtenza,
      importoDovuto,
      scadenza,
      dataPagamento,
      azioni,
    ];

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}
