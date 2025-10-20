import {
  IRicercaPraticaAvanzataReduced,
  RiscaSortTypes,
  RiscaTableCss,
  RiscaTableInput,
  RiscaTablePagination,
  RiscaTableSorting,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia che rappresenta la struttura dati utilizzata all'interno della tabella dati generali amministrativi, per istanze e provvedimenti.
 */
interface IRiscaRicercaAvanzataPraticheTable {
  codiceUtenza?: string;
  titolare?: string;
  numero?: string;
  procedimento?: string;
  stato?: string;
  comuneOperaDiPresa?: string;
  corpoIdrico?: string;
  nomeImpianto?: string;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati degli usi di legge inseriti dall'utente.
 */
export class RiscaRicercaAvanzataPraticheTable
  extends RiscaTableClass<IRiscaRicercaAvanzataPraticheTable>
  implements IRiscaTableClass<IRiscaRicercaAvanzataPraticheTable>
{
  /**
   * Costruttore.
   */
  constructor(pratiche?: IRicercaPraticaAvanzataReduced[]) {
    // Richiamo il super
    super();

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(pratiche);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (e: IRicercaPraticaAvanzataReduced) => {
      let eParsed: IRiscaRicercaAvanzataPraticheTable = {};

      // Definisco i dati per la tabella
      eParsed.codiceUtenza = e.codiceUtenza;
      eParsed.titolare = e.titolare;
      eParsed.numero = e.numero;
      eParsed.procedimento = e.procedimento;
      eParsed.stato = e.stato?.des_stato_riscossione;
      eParsed.comuneOperaDiPresa = e.comuneOperaDiPresa;
      eParsed.corpoIdrico = e.corpoIdrico;
      eParsed.nomeImpianto = e.nomeImpianto;

      // Ritorno l'oggetto convertito
      return eParsed;
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
    const codiceUtenza: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Codice Utenza',
        sortable: true,
        sortType: RiscaSortTypes.crescente,
        cssCell: { width: '105px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'codiceUtenza', type: 'string' },
      }),
    };
    const titolare: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Titolare',
        sortable: true,
        cssCell: { width: '250px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'titolare', type: 'string' },
      }),
    };
    const numero: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Numero',
        sortable: true,
        cssCell: { width: '200px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'numero', type: 'string' },
      }),
    };
    const procedimento: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Procedimento',
        sortable: true,
        cssCell: { width: '150px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'procedimento', type: 'string' },
      }),
    };
    const stato: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Stato',
        sortable: true,
        cssCell: { width: '80px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'stato', type: 'string' },
      }),
    };

    const azioni: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Azioni',
        cssCell: { width: '80px' },
      }),
      body: new RiscaTableSourceMapBody({
        useTabMethod: true,
        tabMethodData: { actions: [{ action: 'detail' }] },
      }),
    };

    // Creo la configurazione RiscaTableSourceMap per le colonne
    const sourceMap: RiscaTableSourceMap[] = [
      codiceUtenza,
      titolare,
      numero,
      procedimento,
      stato,
      azioni,
    ];
    // Definisco le regole di sorting
    const sorting: RiscaTableSorting = { resetSortOnAdd: true };
    // Definisco la paginazione, ma se presente uso quella
    const pagination: RiscaTablePagination = this.getDefaultPagination();

    // Assegno alla variabile locale la configurazione
    this.dataConfig = new RiscaTableInput({ sourceMap, sorting, pagination });
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
      sortBy: 'codiceUtenza',
      sortDirection: RiscaSortTypes.crescente,
      maxPages: 10,
    };
  }
}
