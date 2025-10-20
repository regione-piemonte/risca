import { InteressiLegaliVo } from 'src/app/core/commons/vo/interessi-legali-vo';
import { RiscaTableDataConfig } from '../../../../components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../consts/common-consts.consts';
import {
  convertMomentToViewDate,
  formatoImportoITA,
} from '../../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaFormatoDate,
  RiscaSortTypes,
  RiscaTableBodyTabMethods,
  RiscaTableCss,
  RiscaTablePagination,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../../risca-table';
import { gestisciDataFine } from '../tassi-di-interesse.table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati visualizzati come colonne per la risca-table.
 */
interface IInteressiLegaliTable {
  percentuale?: number;
  dataInizio?: string;
  dataFine?: string;
  giorni?: number;
}

/**
 * Interfaccia che rappresenta l'oggetto di configurazione della tabella.
 */
export interface IInteressiLegaliTableConfigs {
  interessiLegali?: InteressiLegaliVo[];
  paginazione?: RiscaTablePagination;
  AEADisabled: boolean;
}

/**
 * Funzione che ritorna un nuovo oggetto con la configurazione per la paginazione di default.
 */
export const interessiLegaliTablePagination: () => RiscaTablePagination =
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
export class InteressiLegaliTable
  extends RiscaTableClass<IInteressiLegaliTable>
  implements IRiscaTableClass<IInteressiLegaliTable> {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** Configurazioni per le colonne della tabella. */
  private STYLE_AZIONI = { 'min-width': '90px' };
  private STYLE_PERCENTUALE = {};
  private STYLE_DATA_INIZIO = {};
  private STYLE_DATA_FINE = {};
  private STYLE_GIORNI = {};

  /** boolean che contiene la configurazione di gestione per l'abilitazione degli elementi applicativi della pagina. */
  private AEA_Disabled: boolean;

  /**
   * Costruttore.
   */
  constructor(interessiLegaliConfigs?: IInteressiLegaliTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo dalla configurazione i parametri
    const { interessiLegali, paginazione, AEADisabled } = interessiLegaliConfigs ?? {};

    // Assegno localmente il flag di gestione di abilitazione degli elementi
    this.AEA_Disabled = AEADisabled ?? false;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(interessiLegali);
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
    this.converter = (il: InteressiLegaliVo): IInteressiLegaliTable => {
      // Converto e definisco le informazioni per la tabella
      const percentuale: number = il?.percentuale;
      const dataInizio: string = convertMomentToViewDate(il?.data_inizio);
      const dataFine: string = convertMomentToViewDate(il?.data_fine);
      const giorni: number = il?.giorni_legali;

      // Definisco l'oggetto che comporrà le colonne della tabella
      let columnsData: IInteressiLegaliTable = {
        percentuale,
        dataInizio,
        dataFine,
        giorni,
      };
      // Ritorno l'oggetto convertito
      return columnsData;
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

    // ############# percentuale
    const percentuale: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Percentuale %',
        sortable: false,
        cssCell: this.STYLE_PERCENTUALE,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'percentuale',
          type: 'number',
          outputFormat: (v: number) => {
            return formatoImportoITA(v, 2, true);
          },
        },
      }),
    };

    // ############# dataInizio
    const dataInizio: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Data inizio',
        sortable: false,
        cssCell: this.STYLE_DATA_INIZIO,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'dataInizio',
          type: 'string',
        },
      }),
    };

    // ############# dataFine
    const dataFine: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Data fine',
        sortable: false,
        cssCell: this.STYLE_DATA_FINE,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'dataFine',
          type: 'string',
          outputFormat: (dataFine: RiscaFormatoDate.view): string => {
            // Richiamo la funzione interna per la gestione del dato
            return this.gestisciDataFine(dataFine);
            // #
          },
        },
      }),
    };

    // ############# giorni
    const giorni: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Giorni',
        sortable: false,
        cssCell: this.STYLE_GIORNI,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'giorni',
          type: 'number',
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
            {
              action: RiscaTableBodyTabMethods.modify,
              disable: (v?: RiscaTableDataConfig<InteressiLegaliVo>) => {
                // Verifico se il flag per tutti gli elementi selezionati è true
                return this.AEA_Disabled;
                // #
              },
            },
            {
              action: RiscaTableBodyTabMethods.delete,
              disable: (v?: RiscaTableDataConfig<InteressiLegaliVo>) => {
                // Verifico se il flag per tutti gli elementi selezionati è true
                return this.AEA_Disabled;
                // #
              },
              visible: (v?: RiscaTableDataConfig<InteressiLegaliVo>) => {
                const abilitato: boolean = v?.original?.flg_cancellazione ?? false;
                return abilitato;
              }
            },
          ],
        },
      }),
    };

    // Creo la configurazione RiscaTableSourceMap per le colonne
    const sourceMap: RiscaTableSourceMap[] = [
      percentuale,
      dataInizio,
      dataFine,
      giorni,
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
    return interessiLegaliTablePagination();
  }

  /**
   * Funzione che gestisce la logica per la visualizzazione della data fine.
   * Se la data fine risulta nel futuro rispetto ad oggi, allora bisogna visualizzare "IN CORSO".
   * Altrimenti si visualizza la data.
   * @param dataFine RiscaFormatoDate.view con la data fine da gestire in formato "view date".
   * @returns string con il risultato della gestione da visualizzare.
   */
  private gestisciDataFine(dataFine: RiscaFormatoDate.view): string {
    // Richiamo la funzione per gestire la data fine
    return gestisciDataFine(dataFine);
  }
}
