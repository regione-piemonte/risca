import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import {
  IRiscaTableGraphic,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati gruppi impiegato come configurazione della tabella risca-table.
 */
export interface IGruppiTable {}

/**
 * Classe usata per la generazione delle risca-table che contiene i risultati di ricerca per i gruppi.
 */
export class FormCercaSoggettoDatiGruppiTable
  extends RiscaTableClass<IGruppiTable>
  implements IRiscaTableClass<IGruppiTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Associa alla riscossione. */
  private STYLE_ASSOCIA_RISCOSSIONE = {
    width: '210px',
    'min-width': '210px',
    'max-width': '210px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Nome raggruppamento. */
  private STYLE_NOME_RAGGRUPPAMENTO = {
    'max-width': '300px',
  };

  /**
   * Costruttore.
   */
  constructor(gruppi?: Gruppo[]) {
    // Richiamo il super
    super();

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(gruppi);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (g: Gruppo) => {
      // Definisco le proprietà che verranno tornate come parsing
      const raggruppamento = g?.des_gruppo_soggetto;

      // Effettuo il parsing  delle informazioni in input
      return {
        raggruppamento,
      } as IGruppiTable;
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
      titleContainer: 'Gruppi di soggetti',
    };
    // Definisco le configurazioni per gli stili
    const table = {
      'min-width': '875px',
    };

    // Creo il setup css
    this.cssConfig = new RiscaTableCss(graphicCss, { table });
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
          label: 'Associa alla riscossione',
          sortable: false,
        }),
        body: new RiscaTableSourceMapBody({
          cssCell: this.STYLE_ASSOCIA_RISCOSSIONE,
          useTabMethod: true,
          tabMethodData: { actions: [{ action: 'radio' }] },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Nome raggruppamento',
          sortable: false,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'raggruppamento', type: 'string' },
          cssCell: this.STYLE_NOME_RAGGRUPPAMENTO,
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}
