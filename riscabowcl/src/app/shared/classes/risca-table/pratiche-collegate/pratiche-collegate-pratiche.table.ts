import {
  IRiscaTableGraphic,
  RiscaSortTypes,
  RiscaTableBodyTabMethods,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from 'src/app/shared/utilities';
import { RiscossioneSearchResultVo } from '../../../../core/commons/vo/riscossione-search-result-vo';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IPraticheCollegatePraticheTable {
  codiceUtenza: string;
  numeroPratica: string;
  procedimento: string;
  stato: string;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i risultati di ricerca per i soggetti nella home page.
 */
export class PraticheCollegatePraticheTable
  extends RiscaTableClass<IPraticheCollegatePraticheTable>
  implements IRiscaTableClass<IPraticheCollegatePraticheTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = {
    width: '110px',
    'min-width': '110px',
    'max-width': '110px',
  };

  /**
   * Costruttore.
   */
  constructor(praticheResult?: RiscossioneSearchResultVo[]) {
    // Richiamo il super
    super();

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(praticheResult);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (r: RiscossioneSearchResultVo) => {
      // Verifico l'oggetto esista
      if (!r) return undefined;

      // Eseguo il mapping dei campi
      const codiceUtenza = r.codice_utenza || '';
      const numeroPratica = r.numero_pratica || '';
      const procedimento = r.procedimento || '';
      const stato = r.stato || '';

      // Definisco l'oggetto da ritornare
      const v: IPraticheCollegatePraticheTable = {
        codiceUtenza,
        numeroPratica,
        procedimento,
        stato,
      };

      // Effettuo il parsing  delle informazioni in input
      return v;
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
    // Definisco le configurazioni
    const graphicCss: IRiscaTableGraphic = {
      showContainer: true,
      titleContainer: 'Pratiche',
    };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(graphicCss);
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
          label: 'Codice Utenza',
          sortable: true,
          sortType: RiscaSortTypes.nessuno,
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
          label: 'Numero Pratica',
          sortable: true,
          sortType: RiscaSortTypes.crescente,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'numeroPratica',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Procedimento',
          sortable: true,
          sortType: RiscaSortTypes.nessuno,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'procedimento',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Stato',
          sortable: true,
          sortType: RiscaSortTypes.nessuno,
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
                action: RiscaTableBodyTabMethods.detail,
                title: 'Dettaglio',
              },
            ],
          },
          cssCell: this.STYLE_AZIONI,
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}
