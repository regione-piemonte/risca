import { Moment } from 'moment';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { RicercaMorositaConsts } from '../../../../../features/pagamenti/component/ricerca-morosita/utilities/ricerca-morosita.consts';
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
interface IRicercaSDxMorositaTable {
  codiceUtenza: string;
  importoDovuto: number;
  scadenza: string;
  interessiDovuti: number;
  importoVersato: number;
  importoCanoneMancante: number;
  interessiMancanti: number;
  interessiVersati: number;
  importoRimborsato: number;
  dataPagamento: string;
  stato: string;
}

/**
 * Interfaccia che rappresenta l'oggetto di configurazione della tabella delle morosità.
 */
export interface IRicercaSDxMorositaTableConfigs {
  statiDebitori?: StatoDebitorioVo[];
  tuttiSelezionati?: boolean;
  paginazione?: RiscaTablePagination;
}

/**
 * Funzione che ritorna un nuovo oggetto con la configurazione per la paginazione di default.
 */
export const RicercaSDxMorositaTablePagination: () => RiscaTablePagination =
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
export class RicercaSDxMorositaTable
  extends RiscaTableClass<IRicercaSDxMorositaTable>
  implements IRiscaTableClass<IRicercaSDxMorositaTable>
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** RicercaMorositaConsts come classe di costanti del componente. */
  RM_C = new RicercaMorositaConsts();

  /** Configurazioni per le colonne della tabella. */
  private STYLE_SELEZIONA_HEAD = {};
  private STYLE_SELEZIONA_BODY = { 'text-align': 'center' };
  private STYLE_CODICE_UTENZA = {};
  private STYLE_IMPORTO_DOVUTO = {};
  private STYLE_SCADENZA = { 'min-width': '105px', width: '105px' };
  private STYLE_INTERESSI_DOVUTI = {};
  private STYLE_IMPORTO_VERSATO = {};
  private STYLE_IMPORTO_CANONE_MANCANTE: {};
  private STYLE_INTERESSI_MANCANTI: {};
  private STYLE_INTERESSI_VERSATI: {};
  private STYLE_IMPORTO_RIMBORSATO: {};
  private STYLE_DATA_PAGAMENTO = { 'min-width': '105px', width: '105px' };
  private STYLE_STATO = {};
  private STYLE_AZIONI = { 'min-width': '90px' };

  /**
   * Costruttore.
   */
  constructor(morositaConfigs?: IRicercaSDxMorositaTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo dalla configurazione i parametri
    const { statiDebitori, tuttiSelezionati, paginazione } =
      morositaConfigs || {};
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

      const inMatSpesNotifica = sd?.interessi_maturati_spese_di_notifica;
      const intDovuti = inMatSpesNotifica >= 0 ? inMatSpesNotifica : undefined;
      const interessiDovuti: number = intDovuti;

      const importoVersato: number = sd?.acc_importo_versato;
      const importoCanoneMancante: number = sd?.acc_importo_di_canone_mancante;
      const interessiMancanti: number = sd?.acc_interessi_mancanti;
      const interessiVersati: number = sd?.acc_interessi_versati;
      const importoRimborsato: number = sd?.acc_importo_rimborsato;
      const dataPagamento: string = convertMomentToViewDate(sd?.data_pagamento);
      const stato: string = sd?.stato_riscossione;

      // Definisco l'oggetto che comporrà le colonne della tabella
      let mParsed: IRicercaSDxMorositaTable = {
        codiceUtenza,
        importoDovuto,
        scadenza,
        interessiDovuti,
        importoVersato,
        importoCanoneMancante,
        interessiMancanti,
        interessiVersati,
        importoRimborsato,
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

    // ############# importoCanoneMancante
    const importoCanoneMancante: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Imp. di canone mancante',
        sortable: false,
        cssCell: this.STYLE_IMPORTO_CANONE_MANCANTE,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'importoCanoneMancante',
          type: 'number',
          outputFormat: (v: number): string => {
            // Ritorno la formattazione numerica per italia
            return formatoImportoITA(v);
          },
        },
      }),
    };

    // ############# interessiMancanti
    const interessiMancanti: RiscaTableSourceMap = {
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
      }),
    };

    // ############# interessiVersati
    const interessiVersati: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Interessi versati',
        sortable: false,
        cssCell: this.STYLE_INTERESSI_VERSATI,
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
      }),
    };

    // ############# importoRimborsato
    const importoRimborsato: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Imp. rimborsato',
        sortable: false,
        cssCell: this.STYLE_IMPORTO_RIMBORSATO,
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
              action: this.RM_C.SCARICA_FILE_ACTION,
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
      importoCanoneMancante,
      interessiMancanti,
      interessiVersati,
      importoRimborsato,
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
    return RicercaSDxMorositaTablePagination();
  }

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
interface ISDSelezionatixMorositaTable {
  codiceUtenza: string;
  importoDovuto: number;
  scadenza: string;
  dataPagamento: string;
}

/**
 * Interfaccia che rappresenta l'oggetto di configurazione della tabella delle morosità.
 */
export interface ISDSelezionatixMorositaTableConfigs {
  statiDebitori?: StatoDebitorioVo[];
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati selezionati dalla tabella "padre".
 */
export class SDSelezionatixMorositaTable
  extends RiscaTableClass<ISDSelezionatixMorositaTable>
  implements IRiscaTableClass<ISDSelezionatixMorositaTable>
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** RicercaMorositaConsts come classe di costanti del componente. */
  RM_C = new RicercaMorositaConsts();

  /** Configurazioni per le colonne della tabella. */
  private STYLE_CODICE_UTENZA = {};
  private STYLE_IMPORTO_DOVUTO = {};
  private STYLE_SCADENZA = { 'min-width': '105px' };
  private STYLE_DATA_PAGAMENTO = { 'min-width': '105px' };
  private STYLE_AZIONI = { width: '15px', 'max-width': '15px' };

  /**
   * Costruttore.
   */
  constructor(sdxMorositaConfigs?: ISDSelezionatixMorositaTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo dalla configurazione i parametri
    const { statiDebitori } = sdxMorositaConfigs || {};

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
      let mParsed: ISDSelezionatixMorositaTable = {
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
