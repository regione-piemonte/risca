import { Moment } from 'moment';
import { RimborsoVo } from 'src/app/core/commons/vo/rimborso-vo';
import { RimborsiConsts } from 'src/app/features/pratiche/components/dati-contabili/rimborsi/utilities/rimborsi.consts';
import { RiscaTableDataConfig } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { RiscaIconsCss } from 'src/app/shared/enums/icons.enums';
import {
  convertMomentToViewDate,
  formatoImportoITA,
} from 'src/app/shared/services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaTableBodyTabMethodData,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { RiscaIconsCssDisabled } from './../../../enums/icons.enums';
import { IRiscaTableClass, RiscaTableClass } from './../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IAttivitaRimborsiTable {
  attivitaSvolta?: string;
  importoEccedente?: number;
  importoRimborsoCompensazione?: number;
  numeroProvvedimento?: number;
  dataProvvedimento?: string;
  causaleRimborso?: string;
  codiceFiscaleCreditore?: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface AttivitaRimborsiTableConfigs {
  attivitaRimborsi?: RimborsoVo[];
  disableUserInputs?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati per i componenti gruppo dei dati anagrafici.
 */
export class AttivitaRimborsiTable
  extends RiscaTableClass<IAttivitaRimborsiTable>
  implements IRiscaTableClass<IAttivitaRimborsiTable>
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  RMB_C = RimborsiConsts;

  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Attività svolta. */
  private STYLE_ATTIVITA_SVOLTA = {
    'min-width': '120px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Importo eccedente. */
  private STYLE_IMPORTO_ECCEDENTE = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '110px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Importo rimborso compensazione. */
  private STYLE_IMPORTO_RIMBORSO_COMPENSAZIONE = {
    'max-width': '250px',
    'text-align': 'center',
    'min-width': '110px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Numero provvedimento. */
  private STYLE_NUMERO_PROVVEDIMENTO = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: data provvedimento. */
  private STYLE_DATA_PROVVEDIMENTO = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: cauale rimborso. */
  private STYLE_CAUSALE_RIMBORSO = {
    'max-width': '250px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: codice fiscale creditore. */
  private STYLE_CODICE_FISCALE_CREDITORE = {
    'min-width': '120px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = { width: '120px' };

  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _disableUserInputs: boolean;

  /**
   * Costruttore.
   */
  constructor(configs: AttivitaRimborsiTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { attivitaRimborsi, disableUserInputs } = configs;
    // Assegno le variabili
    this._disableUserInputs = disableUserInputs || false;
    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(attivitaRimborsi);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   * Va ridefinito dall'esterno
   */
  setTableFunctions() {
    this.converter = (rimborso: RimborsoVo) => {
      if (!rimborso) {
        return undefined;
      }
      // Converto la data provvedimento
      const dataProvvedimento = convertMomentToViewDate(
        rimborso.data_determina as Moment
      );

      // Creo l'oggetto
      const attivitaSvoltaTable: IAttivitaRimborsiTable = {
        attivitaSvolta: rimborso.tipo_rimborso?.des_tipo_rimborso,
        importoEccedente: rimborso.imp_rimborso,
        importoRimborsoCompensazione: rimborso.imp_restituito,
        numeroProvvedimento: rimborso.num_determina,
        dataProvvedimento,
        causaleRimborso: rimborso.causale,
        codiceFiscaleCreditore: rimborso.soggetto_rimborso?.cf_soggetto,
      };
      // Ritorno
      return attivitaSvoltaTable;
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
    // Creo la configurazione RiscaTableSourceMap
    const sourceMap: RiscaTableSourceMap[] = [];
    // Inserisco all'interno della sourceMap le configurazioni per le colonne
    sourceMap.push(
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Attività svolta',
          sortable: false,
          cssCell: this.STYLE_ATTIVITA_SVOLTA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'attivitaSvolta',
            type: 'string',
          },
          cssCell: this.STYLE_ATTIVITA_SVOLTA,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo eccedente',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_ECCEDENTE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoEccedente',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_ECCEDENTE,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo rimborsato/compensato',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_RIMBORSO_COMPENSAZIONE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoRimborsoCompensazione',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_RIMBORSO_COMPENSAZIONE,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'N. Provv.',
          sortable: false,
          cssCell: this.STYLE_NUMERO_PROVVEDIMENTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'numeroProvvedimento',
            type: 'string',
          },
          cssCell: this.STYLE_NUMERO_PROVVEDIMENTO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data Provvedimento',
          sortable: false,
          cssCell: this.STYLE_DATA_PROVVEDIMENTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataProvvedimento',
            type: 'date',
          },
          cssCell: this.STYLE_DATA_PROVVEDIMENTO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Causale rimborso',
          sortable: false,
          cssCell: this.STYLE_CAUSALE_RIMBORSO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'causaleRimborso',
            type: 'string',
          },
          cssCell: this.STYLE_CAUSALE_RIMBORSO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'CF Creditore',
          sortable: false,
          cssCell: this.STYLE_CODICE_FISCALE_CREDITORE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'codiceFiscaleCreditore',
            type: 'string',
          },
          cssCell: this.STYLE_CODICE_FISCALE_CREDITORE,
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
                action: 'modify',
                disable: (v: RiscaTableDataConfig<RimborsoVo>) => {
                  // La prima verifica deriva dalle configurazioni
                  if (this._disableUserInputs) {
                    // Ritorno true, va disabilitato in ogni caso
                    return true;
                    // #
                  }
                  // Abilitato
                  return false;
                },
              },
              {
                action: 'delete',
                disable: (v: RiscaTableDataConfig<RimborsoVo>) => {
                  // La prima verifica deriva dalle configurazioni
                  if (this._disableUserInputs) {
                    // Ritorno true, va disabilitato in ogni caso
                    return true;
                    // #
                  }
                  // Abilitato
                  return false;
                },
              },
              this.actionStatiDebitoriCollegati(),
            ],
          },
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }

  /**
   * Crea l'action della tabella per l'indirizzo spedizione
   * @returns RiscaTableBodyTabMethodData
   */
  private actionStatiDebitoriCollegati(): RiscaTableBodyTabMethodData {
    return {
      action: this.RMB_C.STATI_DEBITORI_COLLEGATI,
      custom: true,
      title: this.RMB_C.TITLE_STATI_DEBITORI_COLLEGATI,
      class: this.C_C.RISCA_TABLE_ACTION,
      style: { width: '25px' },
      disable: (v: RiscaTableDataConfig<RimborsoVo>) => {
        // Verifico e ritorno se esiste l'id_rimborso
        return v.original?.id_rimborso == undefined;
      },
      chooseIconEnabled: (v: RiscaTableDataConfig<RimborsoVo>, ...p) => {
        return RiscaIconsCss.stati_debitori_collegati;
      },
      chooseIconDisabled: (v: RiscaTableDataConfig<RimborsoVo>, ...p) => {
        return RiscaIconsCssDisabled.stati_debitori_collegati;
      },
    };
  }
}
