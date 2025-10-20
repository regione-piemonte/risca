import {
  convertMomentToViewDate,
  formatoImportoITA,
} from 'src/app/shared/services/risca/risca-utilities/risca-utilities.functions';
import { PraticaVo } from '../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { StatiDebitoriNAPConsts } from '../../../../features/pratiche/components/dati-contabili/numero-avviso-pagamento/stati-debitori-nap/utilities/stati-debitori-nap.consts';
import {
  annualitaPiuRecente,
  getRataPadre,
} from '../../../../features/pratiche/service/dati-contabili/dati-contabili/dati-contabili-utility.functions';
import { RiscaTableDataConfig } from '../../../components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../consts/common-consts.consts';
import {
  RiscaIconsCss,
  RiscaIconsCssDisabled,
} from '../../../enums/icons.enums';
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
export interface ISDByNAPTable {
  codiceUtenza?: string;
  annualita?: number;
  canoneDovuto?: number;
  interessiMaturatiSpeseNotifica?: number;
  scadenza?: string;
  importoVersato?: number;
  dataPagamento?: string;
  importoCompensatoRimborsato?: number;
  attivita?: string;
  importoMancanteEccedente?: number;
  dilazione?: boolean;
  statoContribuzione?: string;
  stato?: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface SDByNAPTableConfigs {
  pratica: PraticaVo;
  statiDebitori?: StatoDebitorioVo[];
  disableUserInputs?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table.
 */
export class StatiDebitoriNAPTable
  extends RiscaTableClass<ISDByNAPTable>
  implements IRiscaTableClass<ISDByNAPTable>
{
  /** Oggetto di costanti comuni all'applicazione. */
  private C_C = new CommonConsts();
  /** Oggetto contenente una serie di costanti per la gestione del componente. */
  private SD_NAP_C = new StatiDebitoriNAPConsts();

  /** Stile per le colonne. */
  private STYLE_CODICE_UTENZA = {
    width: '115px',
    'min-width': '115px',
  };
  private STYLE_ANNUALITA_H = { 'text-align': 'center' };
  private STYLE_ANNUALITA_B = { 'text-align': 'center' };
  private STYLE_CANONE_DOVUTO_H = {};
  private STYLE_CANONE_DOVUTO_B = { 'text-align': 'right' };
  private STYLE_INTERESSI_MATURATI_SPESE_NOTIFICA_H = {};
  private STYLE_INTERESSI_MATURATI_SPESE_NOTIFICA_B = { 'text-align': 'right' };
  private STYLE_SCADENZA_H = { 'text-align': 'center' };
  private STYLE_SCADENZA_B = { 'text-align': 'center' };
  private STYLE_IMPORTO_VERSATO_H = {};
  private STYLE_IMPORTO_VERSATO_B = { 'text-align': 'right' };
  private STYLE_DATA_PAGAMENTO_H = { 'text-align': 'center' };
  private STYLE_DATA_PAGAMENTO_B = { 'text-align': 'center' };
  private STYLE_IMPORTO_COMPENSATO_RIMBORSATO_H = {};
  private STYLE_IMPORTO_COMPENSATO_RIMBORSATO_B = { 'text-align': 'right' };
  private STYLE_ATTIVITA = {};
  private STYLE_IMPORTO_MANCANTE_ECCEDENTE_H = {};
  private STYLE_IMPORTO_MANCANTE_ECCEDENTE_B = { 'text-align': 'right' };
  private STYLE_DILAZIONE = { 'text-align': 'center' };
  private STYLE_STATO_CONTRIBUZIONE = {};
  private STYLE_STATO = {};
  private STYLE_AZIONI = {
    width: '85px',
    'min-width': '85px',
  };

  /** PraticaVo contenente i dati della riscossione in uso. */
  private _pratica: PraticaVo;
  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _disableUserInputs: boolean;

  /**
   * Costruttore.
   */
  constructor(configs: SDByNAPTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { pratica, statiDebitori, disableUserInputs } = configs || {};

    // Assegno le variabili
    this._disableUserInputs = disableUserInputs || false;
    // Assegno localmente le informazioni
    this._pratica = pratica;

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

      // Recupero le informazioni dallo stato debitorio
      const codiceUtenza = sd.cod_utenza ?? '';

      const annSD = sd.annualita_sd;
      const annRec = annualitaPiuRecente(annSD);
      const annMulti = annSD?.length > 1 ? ' +' : '';
      const annualita = annRec ? `${annRec.anno}${annMulti}` : '';

      const canoneDovuto = sd.canone_dovuto;
      // RISCA-772 => modificata la proprietà di assegnazione da [imp_spese_notifica] a [interessi_maturati_spese_di_notifica]
      const interessiMaturatiSpeseNotifica =
        sd.interessi_maturati_spese_di_notifica;

      let scadenza = '';
      const rateSD = sd.rate;
      const rataRif = getRataPadre(rateSD);
      // Verifico se esiste la rata
      if (rataRif) {
        // Recupero la data scadenza pagamento
        const dataScadPag = rataRif.data_scadenza_pagamento;
        // Converto il dato per la view
        scadenza = convertMomentToViewDate(dataScadPag);
      }

      const importoVersato = sd.importo_versato;
      const dataPagamento = convertMomentToViewDate(sd.data_pagamento);
      const importoCompensatoRimborsato = sd.imp_compensazione_canone;
      const attivita = sd.attivita_stato_deb?.des_attivita_stato_deb ?? '';
      const importoMancanteEccedente = sd.imp_mancante_imp_eccedente;
      const dilazione = sd.flg_dilazione;
      const statoContribuzione =
        sd.stato_contribuzione?.des_stato_contribuzione;
      const stato = sd.stato_riscossione ?? '';

      // Definisco l'oggetto da ritornare dalla conversione
      const row: ISDByNAPTable = {
        codiceUtenza,
        annualita: annualita as any,
        canoneDovuto,
        interessiMaturatiSpeseNotifica,
        scadenza,
        importoVersato,
        dataPagamento,
        importoCompensatoRimborsato,
        attivita,
        importoMancanteEccedente,
        dilazione,
        statoContribuzione,
        stato,
      };

      // Ritorno l'oggetto dati
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
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Codice utenza',
          sortable: true,
          sortType: RiscaSortTypes.crescente,
          cssCell: this.STYLE_CODICE_UTENZA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'codiceUtenza',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Annualità',
          sortable: false,
          cssCell: this.STYLE_ANNUALITA_H,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'annualita',
            type: 'number',
          },
          cssCell: this.STYLE_ANNUALITA_B,
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
          label: 'Interessi maturati e spese di notifica',
          sortable: false,
          cssCell: this.STYLE_INTERESSI_MATURATI_SPESE_NOTIFICA_H,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'interessiMaturatiSpeseNotifica',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_INTERESSI_MATURATI_SPESE_NOTIFICA_B,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Scadenza',
          sortable: false,
          cssCell: this.STYLE_SCADENZA_H,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'scadenza',
            type: 'date',
          },
          cssCell: this.STYLE_SCADENZA_B,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo versato',
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
          cssCell: this.STYLE_DATA_PAGAMENTO_H,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataPagamento',
            type: 'date',
          },
          cssCell: this.STYLE_DATA_PAGAMENTO_B,
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
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: `Importo mancante -
          Importo eccedente +`,
          sortable: false,
          cssCell: this.STYLE_IMPORTO_MANCANTE_ECCEDENTE_H,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoMancanteEccedente',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_MANCANTE_ECCEDENTE_B,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Dilazione',
          sortable: false,
          cssCell: this.STYLE_DILAZIONE,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [
              {
                action: RiscaTableBodyTabMethods.check,
                checkboxConfigs: {
                  isRowData: true,
                  dataTableProperty: 'dilazione',
                },
              },
            ],
          },
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
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Azioni',
          sortable: false,
          cssCell: this.STYLE_AZIONI,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [
              {
                action: this.SD_NAP_C.TAB_NAP_SD_STAMPA_PRATICA,
                custom: true,
                title: 'Stampa pratica',
                class: this.C_C.RISCA_TABLE_ACTION,
                iconEnabled: RiscaIconsCss.pdf,
                iconDisabled: RiscaIconsCssDisabled.pdf,
                disable: (v?: RiscaTableDataConfig<StatoDebitorioVo>) => {
                  // // La prima verifica deriva dalle configurazioni
                  // if (this._disableUserInputs) {
                  //   // Ritorno true, va disabilitato in ogni caso
                  //   return true;
                  //   // #
                  // }

                  // Stampa disabilitata se non esiste l'id_riscossione
                  return v.original?.id_riscossione == undefined;
                },
              },
            ],
          },
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
      elementsForPage: 10,
      showTotal: false,
      currentPage: 1,
      sortBy: 'codiceUtenza',
      sortDirection: RiscaSortTypes.crescente,
      maxPages: 10,
    };
  }
}
