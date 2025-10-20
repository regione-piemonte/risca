import { IFRAIstanza } from '../../../../features/pratiche/components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.interfaces';
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
interface IRAIstanzeTable {
  tipoIstanza?: string;
  dataDaA?: string; // Combinazione di data da e data a
}

/**
 * Classe che gestisce la struttura di una tabella per la ricerca avanzata: istanze.
 */
export class RAIstanzeTable
  extends RiscaTableClass<IRAIstanzeTable>
  implements IRiscaTableClass<IRAIstanzeTable>
{
  /**
   * Costruttore.
   */
  constructor(ricercaIstanze?: IFRAIstanza[]) {
    // Richiamo il super
    super();

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(ricercaIstanze);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (e: IFRAIstanza) => {
      let eParsed: IRAIstanzeTable = {};

      // Definisco i dati per la tabella
      eParsed.tipoIstanza = e.tipoIstanza?.des_tipo_provvedimento;
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
    const table = { 'min-width': '694px', width: '735px' };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(undefined, { table });
  }

  /**
   * Funzione di setup per la configurazione dati.
   */
  setupDataConfig() {
    // Definisco gli oggetti per le colonne della tabella
    const tipologiaIstanza: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Tipologia Istanza',
        sortable: true,
        sortType: RiscaSortTypes.crescente,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'tipoIstanza', type: 'string' },
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
      tipologiaIstanza,
      dataDaA,
      azioni,
    ];
    // Definisco le regole di sorting
    const sorting: RiscaTableSorting = { resetSortOnAdd: true };

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, sorting };
  }
}
