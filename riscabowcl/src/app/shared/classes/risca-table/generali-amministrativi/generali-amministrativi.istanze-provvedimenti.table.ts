import {
  convertNgbDateStructToString,
  isIstanza,
  isProvvedimento,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  NuovaIstanzaFormData,
  NuovoProvvedimentoFormData,
  RiscaTableCss,
  RiscaTableSorting,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
  TNIPFormData,
} from '../../../utilities';
import {
  RiscaSortTypes,
  TipiIstanzaProvvedimento,
} from '../../../utilities/enums/utilities.enums';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia che rappresenta la struttura dati utilizzata all'interno della tabella dati generali amministrativi, per istanze e provvedimenti.
 */
export interface IIstanzeProvvedimentiTable {
  data?: string;
  numero?: string;
  tipo?: TipiIstanzaProvvedimento;
  descrizione?: string;
  tipoTitolo?: string;
  note?: string;
}

/**
 * Interfaccia che definisce le configurazioni per il costruttore della tabella.
 */
export interface IIstanzeProvvedimentiTableConfigs {
  istanzeProvvedimenti?: TNIPFormData[];
  disableUserInputs?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati degli usi di legge inseriti dall'utente.
 */
export class IstanzeProvvedimentiTable
  extends RiscaTableClass<IIstanzeProvvedimentiTable>
  implements IRiscaTableClass<IIstanzeProvvedimentiTable>
{
  /** Boolean che definisce la configurazion di disabilitazione per il profilo. */
  private _disableUserInputs: boolean = false;

  /**
   * Costruttore.
   */
  constructor(configs: IIstanzeProvvedimentiTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le proprietà
    const { istanzeProvvedimenti, disableUserInputs } = configs;

    // Assegno localmente le variabili
    this._disableUserInputs = disableUserInputs;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(istanzeProvvedimenti);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (e: TNIPFormData) => {
      // Verifico l'input
      if (!e) {
        return undefined;
      }

      // Definisco di che tipo è l'oggetto da convertire
      const isI = isIstanza(e);
      const isP = isProvvedimento(e);
      // Definisco una variabile che verrà tipizzata
      let eParsed: IIstanzeProvvedimentiTable = {};

      // Verifico il tipo
      if (isI) {
        // Riassegno tipizzando l'input
        e = e as NuovaIstanzaFormData;
        // Definisco i dati per la tabella
        eParsed.data = convertNgbDateStructToString(e.dataIstanza, '/') || '';
        eParsed.numero = e.numeroIstanza || ('' as any);
        eParsed.tipo = TipiIstanzaProvvedimento.istanza;
        eParsed.descrizione = e.tipologiaIstanza?.des_tipo_provvedimento || '';
        eParsed.tipoTitolo = '';
        eParsed.note = e.noteIstanza || '';
        // #
      } else if (isP) {
        // Riassegno tipizzando l'input
        e = e as NuovoProvvedimentoFormData;
        // Definisco i dati per la tabella
        eParsed.data = convertNgbDateStructToString(e.dataProvvedimento, '/');
        eParsed.numero = e.numeroProvvedimento || ('' as any);
        eParsed.tipo = TipiIstanzaProvvedimento.provvedimento;
        eParsed.descrizione =
          e.tipologiaProvvedimento?.des_tipo_provvedimento || '';
        eParsed.tipoTitolo = e.tipoTitolo?.des_tipo_titolo || '';
        eParsed.note = e.noteProvvedimento || '';
        // #
      } else {
        // Tipo sconosciuto
        throw new Error('setSearcherParser | unkwonw object type');
      }

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
    const data: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Data',
        sortable: true,
        sortType: RiscaSortTypes.crescente,
        cssCell: { 'min-width': '105px', width: '105px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'data', type: 'date' },
      }),
    };
    const numero: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Numero',
        sortable: true,
        cssCell: { width: '190px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'numero', type: 'string' },
      }),
    };
    const istanzaProvvedimento: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Istanza Provvedimento',
        sortable: true,
        cssCell: { 'min-width': '180px', width: '180px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'tipo', type: 'string' },
      }),
    };
    const descrizione: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Descrizione',
        sortable: true,
        cssCell: { 'min-width': '300px', width: '300px', 'max-width': '300px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'descrizione', type: 'string' },
      }),
    };
    const tipoTitolo: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Tipo titolo',
        sortable: true,
        cssCell: { 'min-width': '135px', width: '185px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'tipoTitolo', type: 'string' },
      }),
    };
    const note: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Note',
        sortable: true,
        cssCell: { 'max-width': '550px'},
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'note', type: 'string', ellipsisAt: 75 },
      }),
    };
    const azioni: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Azioni',
        cssCell: { width: '80px' },
      }),
      body: new RiscaTableSourceMapBody({
        useTabMethod: true,
        tabMethodData: {
          actions: [
            {
              action: 'delete',
              disable: (v: any) => {
                // Recupero la configurazione
                const disabled = this._disableUserInputs ?? false;
                // Ritorno lo stato per il disabled
                return disabled;
              },
            },
          ],
        },
      }),
    };

    // Creo la configurazione RiscaTableSourceMap per le colonne
    const sourceMap: RiscaTableSourceMap[] = [
      data,
      numero,
      istanzaProvvedimento,
      descrizione,
      tipoTitolo,
      note,
      azioni,
    ];
    // Definisco le regole di sorting
    const sorting: RiscaTableSorting = { resetSortOnAdd: true };

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, sorting };
  }
}
