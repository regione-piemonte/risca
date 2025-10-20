import { IFRAProvvedimento } from '../../../../features/pratiche/components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.interfaces';
import { da_a_labelNgbDateStruct } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaSortTypes,
  RiscaTableCss,
  RiscaTableSorting,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia che rappresenta la struttura dati utilizzata all'interno della tabella.
 */
interface IRAProvvedimentiTable {
  tipoProvvedimento?: string;
  tipoTitolo?: string;
  numeroTitolo?: string;
  dataDaA?: string; // Combinazione di data da e data a
}

/**
 * Classe che gestisce la struttura di una tabella per la ricerca avanzata: provvedimenti.
 */
export class RAProvvedimentiTable
  extends RiscaTableClass<IRAProvvedimentiTable>
  implements IRiscaTableClass<IRAProvvedimentiTable>
{
  /**
   * Costruttore.
   */
  constructor(ricercaProvvedimenti?: IFRAProvvedimento[]) {
    // Richiamo il super
    super();

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(ricercaProvvedimenti);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (e: IFRAProvvedimento) => {
      // Verifico l'input
      if (!e) {
        // Niente configurazione
        return undefined;
      }

      // Istanzio il contenitore
      let eParsed: IRAProvvedimentiTable = {};

      // Definisco i dati per la tabella
      eParsed.tipoProvvedimento = e.tipoProvvedimento?.des_tipo_provvedimento;
      eParsed.tipoTitolo = e.tipoTitolo?.des_tipo_titolo;
      eParsed.numeroTitolo = e.numeroTitolo ?? '';
      // Effettuo il parse degli oggetti a stringa
      eParsed.dataDaA = da_a_labelNgbDateStruct(e.dataDa, e.dataA);

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
    // Definisco le configurazioni per gli stili
    const table = { 'min-width': '1054px', width: '1054px' };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(undefined, { table });
  }

  /**
   * Funzione di setup per la configurazione dati.
   */
  setupDataConfig() {
    // Definisco gli oggetti per le colonne della tabella
    const tipologiaProvvedimento: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Tipologia Provvedimento',
        sortable: true,
        sortType: RiscaSortTypes.crescente,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'tipoProvvedimento', type: 'string' },
      }),
    };
    const tipoTitolo: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Tipo Titolo',
        sortable: true,
        sortType: RiscaSortTypes.crescente,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'tipoTitolo', type: 'string' },
      }),
    };
    const numeroTitolo: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Numero Titolo',
        sortable: true,
        sortType: RiscaSortTypes.crescente,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'numeroTitolo', type: 'string' },
      }),
    };
    const dataDaA: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Data da/a',
        sortable: false,
        cssCell: { 'min-width': '240px', width: '240px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'dataDaA', type: 'string' },
      }),
    };
    const azioni: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Azioni',
        cssCell: { width: '80px' },
      }),
      body: new RiscaTableSourceMapBody({
        useTabMethod: true,
        tabMethodData: { actions: [{ action: 'delete' }] },
      }),
    };

    // Creo la configurazione RiscaTableSourceMap per le colonne
    const sourceMap: RiscaTableSourceMap[] = [
      tipologiaProvvedimento,
      tipoTitolo,
      numeroTitolo,
      dataDaA,
      azioni,
    ];
    // Definisco le regole di sorting
    const sorting: RiscaTableSorting = { resetSortOnAdd: true };

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, sorting };
  }
}
