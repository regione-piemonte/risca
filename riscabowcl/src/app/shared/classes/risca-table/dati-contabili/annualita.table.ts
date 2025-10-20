import { AnnualitaSDVo } from 'src/app/core/commons/vo/annualita-sd-vo';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { formatoImportoITA } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaTableBodyTabMethodData,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from './../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface ISDAnnualitaTable {
  anno?: number;
  canone_annuo?: number;
  numero_mesi?: number;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface SDAnnualitaTableConfigs {
  annualita?: AnnualitaSDVo[];
  disableUserInputs?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati per i componenti gruppo dei dati anagrafici.
 */
export class SDAnnualitaTable
  extends RiscaTableClass<ISDAnnualitaTable>
  implements IRiscaTableClass<ISDAnnualitaTable>
{
  /** Oggetto con le costanti comuni. */
  C_C = new CommonConsts();

  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Anno. */
  private STYLE_ANNO = {
    'min-width': '120px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Canone. */
  private STYLE_CANONE = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Numero mesi. */
  private STYLE_NUMERO_MESI = {
    'max-width': '250px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = { width: '120px' };

  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _disableUserInputs: boolean;

  /**
   * Costruttore.
   */
  constructor(configs: SDAnnualitaTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { annualita, disableUserInputs } = configs;
    // Assegno le variabili
    this._disableUserInputs = disableUserInputs || false;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(annualita);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di conversione dei dati.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (a: AnnualitaSDVo) => {
      // Verifico l'oggetto esista
      if (!a) {
        return undefined;
      }

      // Definisco le proprietà che verranno tornate come parsing
      const anno = a.anno;
      // Prendo il valore del canone normalmente
      // Modifiche: 1) RISCA-459 [Togliere decimali] | 2) RISCA-622 [Prendere il numero per com'è definito nell'oggetto]
      const canone_annuo = a.canone_annuo;
      
      // Recupero i mesi
      const numero_mesi = a.numero_mesi;

      // Effettuo il parsing  delle informazioni in input
      const configs: ISDAnnualitaTable = {
        anno,
        canone_annuo,
        numero_mesi,
      };

      // Ritorno la configurazione
      return configs;
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
      'min-width': '840px',
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
          label: 'Anno',
          sortable: false,
          cssCell: this.STYLE_ANNO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'anno',
            type: 'number',
          },
          cssCell: this.STYLE_ANNO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Canone',
          sortable: false,
          cssCell: this.STYLE_CANONE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'canone_annuo',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_CANONE,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Numero mesi',
          sortable: false,
          cssCell: this.STYLE_NUMERO_MESI,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'numero_mesi',
            type: 'number',
          },
          cssCell: this.STYLE_NUMERO_MESI,
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
            actions: [this.actionModify(), this.actionDelete()],
          },
        }),
      }
    );

    this.dataConfig = { sourceMap };
  }

  /**
   * ####################
   * AZIONI DELLA TABELLA
   * ####################
   */

  /**
   * Crea l'action della tabella per la modifica dell'indirizzo spedizione
   * @returns RiscaTableBodyTabMethodData
   */
  private actionModify(): RiscaTableBodyTabMethodData {
    return {
      action: 'modify',
      disable: (v: any) => {
        // // Verifico la condizione di gestione utente
        // if (this._disableUserInputs) {
        //   // Configurazione blocca l'azione
        //   return true;
        // }

        // Ritorno la configurazione
        return false;
      },
    };
  }

  /**
   * Crea l'action della tabella per la cancellazione dell'indirizzo
   * @returns RiscaTableBodyTabMethodData
   */
  private actionDelete(): RiscaTableBodyTabMethodData {
    return {
      action: 'delete',
      disable: (v: any) => {
        // Verifico la condizione di gestione utente
        if (this._disableUserInputs) {
          // Configurazione blocca l'azione
          return true;
        }

        // Ritorno la configurazione
        return false;
      },
    };
  }
}
