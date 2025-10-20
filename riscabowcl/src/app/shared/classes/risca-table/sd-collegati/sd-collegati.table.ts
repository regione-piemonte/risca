import { ISDCollegatiVo } from 'src/app/core/commons/vo/sd-collegati-vo';
import {
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from './../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati impiegato come configurazione della tabella risca-table.
 */
export interface ISDCollegatiTable {
  cod_utenza: string;
  desc_periodo_pagamento: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface SDCollegatiTableConfigs {
  sdCollegati?: ISDCollegatiVo[];
}

/**
 * Classe usata per la generazione delle risca-table per la visualizzazione delle informazioni.
 */
export class SDCollegatiTable
  extends RiscaTableClass<ISDCollegatiTable>
  implements IRiscaTableClass<ISDCollegatiTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_CODICE_UTENZA = {
    'min-width': '120px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_ANNUALITA = {
  };

  /**
   * Costruttore.
   */
  constructor(configs: SDCollegatiTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { sdCollegati } = configs;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(sdCollegati);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (config: ISDCollegatiVo) => {
      // Verifico l'oggetto esista
      if (!config) {
        return undefined;
      }

      // Estraggo i campi
      const { cod_utenza, desc_periodo_pagamento } = config;

      // Creo l'oggetto convertito
      const conv: ISDCollegatiTable = {
        cod_utenza,
        desc_periodo_pagamento
      };
      // Ritorno la configurazione
      return conv;
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
      'min-width': '860px',
    };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(undefined, { table });
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
          sortable: false,
          cssCell: this.STYLE_CODICE_UTENZA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'cod_utenza',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Annualità',
          sortable: false,
          cssCell: this.STYLE_ANNUALITA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'desc_periodo_pagamento',
            type: 'string',
          },
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}
