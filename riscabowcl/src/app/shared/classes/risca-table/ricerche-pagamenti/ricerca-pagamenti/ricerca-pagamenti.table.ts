import { PagamentoVo } from '../../../../../core/commons/vo/pagamento-vo';
import { TipoRicercaPagamentoVo } from '../../../../../core/commons/vo/tipo-ricerca-pagamento-vo';
import { CommonConsts } from '../../../../consts/common-consts.consts';
import { formatoImportoITA } from '../../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaSortTypes,
  RiscaTableCss,
  RiscaTablePagination,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../../risca-table';

/**
 * Interfaccia che rappresenta la struttura dati utilizzata all'interno della tabella.
 */
interface IRicercaPagamentiTable {
  soggettoVersamento: string;
  statoPagamento: string;
  modalitaPagamento: string;
  data: string;
  importo: number;
  quintoCampo: string;
  cro: string;
}

/**
 * Interfaccia che rappresenta i dati di configurazione per la tabella.
 */
export interface IRicercaPagamentiTableConfigs {
  pagamenti?: PagamentoVo[];
  statoPagamento?: TipoRicercaPagamentoVo;
  paginazione?: RiscaTablePagination;
}

/**
 * Funzione che ritorna un nuovo oggetto con la configurazione per la paginazione di default.
 */
export const RicercaPagamantiTablePagination: () => RiscaTablePagination =
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
export class RicercaPagamentiTable
  extends RiscaTableClass<IRicercaPagamentiTable>
  implements IRiscaTableClass<IRicercaPagamentiTable>
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** Classi di stile d'applicare alle colonne. */
  private STYLE_SOGGETTO_VERSAMENTO = {};
  private STYLE_STATO_PAGAMENTO = {};
  private STYLE_MODALITA_PAGAMENTO = {};
  private STYLE_DATA = {};
  private STYLE_IMPORTO = {};
  private STYLE_QUINTO_CAMPO = {};
  private STYLE_CRO = {};
  private STYLE_AZIONI = { width: '80px' };

  /** TipoRicercaPagamentoVo come valore d'assegnare alla colonna dello stato pagamento. Sarà uguale per tutte le righe della tabella. */
  statoPagamento: TipoRicercaPagamentoVo;

  /**
   * Costruttore.
   */
  constructor(configs?: IRicercaPagamentiTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo dall'oggetto le configurazioni
    const { pagamenti, paginazione, statoPagamento } = configs || {};
    // Verifico se esistono i pagamenti
    const pags = pagamenti ?? [];

    // Assegno localmente le informazioni
    this.statoPagamento = statoPagamento;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(pags);
    // Lancio il setup delle configurazioni
    this.setupClasse();

    // La paginazione si può aggiornare solo dopo che la classe ha generato le informazioni di data config
    this.updatePaginazione(paginazione);
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (p: PagamentoVo) => {
      // Verifico l'input
      if (!p) {
        // Nessuna configurazione
        return undefined;
      }

      // Recupero le informazioni dall'oggetto pagamento
      const soggettoVersamento = p.soggetto_versamento;
      const statoPagamento = this.statoPagamento?.des_tipo_ricerca_pagamento;
      const modalitaPagamento = p.tipo_modalita_pag?.des_modalita_pag;
      const data = p.data_op_val?.format(this.C_C.DATE_FORMAT_VIEW);
      const importo = p.importo_versato;
      const quintoCampo = p.quinto_campo;
      const cro = p.cro;

      // Creo l'oggetto per la tabella
      let e: IRicercaPagamentiTable = {
        soggettoVersamento,
        statoPagamento,
        modalitaPagamento,
        data,
        importo,
        quintoCampo,
        cro,
      };
      // Ritorno l'oggetto convertito
      return e;
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
    // Definisco gli oggetti per le colonne della tabella
    const soggettoVersamento: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Soggetto versamento',
        sortable: false,
        cssCell: this.STYLE_SOGGETTO_VERSAMENTO,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'soggettoVersamento',
          type: 'string',
        },
      }),
    };

    const statoPagamento: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Stato pagamento',
        sortable: false,
        cssCell: this.STYLE_STATO_PAGAMENTO,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'statoPagamento',
          type: 'string',
        },
      }),
    };

    const modalitaPagamento: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Modalita pagamento',
        sortable: false,
        cssCell: this.STYLE_MODALITA_PAGAMENTO,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'modalitaPagamento',
          type: 'string',
        },
      }),
    };

    const data: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Data',
        sortable: false,
        cssCell: this.STYLE_DATA,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'data',
          type: 'string',
        },
      }),
    };

    const importo: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Importo',
        sortable: false,
        cssCell: this.STYLE_IMPORTO,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'importo',
          type: 'number',
          outputFormat: (v: number): string => {
            // Ritorno la formattazione numerica per italia
            return formatoImportoITA(v);
          },
        },
      }),
    };

    const quintoCampo: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Quinto campo',
        sortable: false,
        cssCell: this.STYLE_QUINTO_CAMPO,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'quintoCampo',
          type: 'string',
        },
      }),
    };

    const cro: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'CRO Codice Riferimento Operazione',
        sortable: false,
        cssCell: this.STYLE_CRO,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'cro',
          type: 'string',
        },
      }),
    };

    const azioni: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Azioni',
        cssCell: this.STYLE_AZIONI,
      }),
      body: new RiscaTableSourceMapBody({
        useTabMethod: true,
        tabMethodData: { actions: [{ action: 'detail' }] },
      }),
    };

    // Creo la configurazione RiscaTableSourceMap per le colonne
    const sourceMap: RiscaTableSourceMap[] = [
      soggettoVersamento,
      statoPagamento,
      modalitaPagamento,
      data,
      importo,
      quintoCampo,
      cro,
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
    return {
      total: this.source?.length || 0,
      label: 'Risultati di ricerca',
      elementsForPage: 10,
      showTotal: true,
      currentPage: 1,
      sortBy: '',
      sortDirection: RiscaSortTypes.nessuno,
      maxPages: 10,
    };
  }
}
