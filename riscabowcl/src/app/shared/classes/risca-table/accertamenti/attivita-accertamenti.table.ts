import { AccertamentiConsts } from 'src/app/features/pratiche/components/dati-contabili/accertamenti/utilities/accertamenti.consts';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { convertMomentToViewDate } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaTableDataConfig } from '../../../components/risca/risca-table/utilities/risca-table.classes';
import {
  RiscaTableBodyTabMethods,
  RiscaTableCss,
  RiscaTableInput,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { AccertamentoVo } from './../../../../core/commons/vo/accertamento-vo';
import { IRiscaTableClass, RiscaTableClass } from './../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IAttivitaAccertamentiTable {
  num_protocollo?: string;
  data_protocollo?: string;
  tipo_accertamento?: string;
  data_scadenza?: string;
  data_notifica?: string;
  flg_restituito?: boolean;
  flg_annullato?: boolean;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface AttivitaAccertamentiTableConfigs {
  attivitaAccertamenti?: AccertamentoVo[];
  disableUserInputs?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati per i componenti gruppo dei dati anagrafici.
 */
export class AttivitaAccertamentiTable
  extends RiscaTableClass<IAttivitaAccertamentiTable>
  implements IRiscaTableClass<IAttivitaAccertamentiTable>
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente una serie di costanti per la gestione dei Accertamenti. */
  ACC_C = AccertamentiConsts;

  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Attività svolta. */
  private STYLE_NUMERO_PROTOCOLLO = {
    'min-width': '120px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Importo eccedente. */
  private STYLE_DATA_PROTOCOLLO = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Numero provvedimento. */
  private STYLE_TIPO_ACCERTAMENTO = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: data provvedimento. */
  private STYLE_DATA_SCADENZA = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: cauale rimborso. */
  private STYLE_DATA_NOTIFICA = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: codice fiscale creditore. */
  private STYLE_RESTITUITO_AL_MITTENTE = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: annullato. */
  private STYLE_ANNULLATO = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = { width: '120px' };

  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _disableUserInputs: boolean;

  /**
   * Costruttore.
   */
  constructor(configs: AttivitaAccertamentiTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { attivitaAccertamenti, disableUserInputs } = configs;
    // Assegno le variabili
    this._disableUserInputs = disableUserInputs || false;
    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(attivitaAccertamenti);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   * Va ridefinito dall'esterno
   */
  setTableFunctions() {
    this.converter = (accertamento: AccertamentoVo) => {
      if (!accertamento) {
        return undefined;
      }

      // Converto le date provvedimento
      const data_protocollo = convertMomentToViewDate(
        accertamento.data_protocollo
      );
      const data_scadenza = convertMomentToViewDate(accertamento.data_scadenza);
      const data_notifica = convertMomentToViewDate(accertamento.data_notifica);
      // Converto il tipo accertamento
      const tipo_accertamento =
        accertamento.tipo_accertamento?.des_tipo_accertamento ?? '';
      // Converto i flag
      const flg_restituito = accertamento.flg_restituito;
      const flg_annullato = accertamento.flg_annullato;

      // Creo l'oggetto
      const attivitaSvoltaTable: IAttivitaAccertamentiTable = {
        num_protocollo: accertamento.num_protocollo,
        data_protocollo,
        tipo_accertamento,
        data_scadenza,
        data_notifica,
        flg_restituito,
        flg_annullato,
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
          label: 'Numero protocollo',
          sortable: false,
          cssCell: this.STYLE_NUMERO_PROTOCOLLO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: this.ACC_C.NUM_PROTOCOLLO,
            type: 'string',
          },
          cssCell: this.STYLE_NUMERO_PROTOCOLLO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data protocollo',
          sortable: false,
          cssCell: this.STYLE_DATA_PROTOCOLLO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: this.ACC_C.DATA_PROTOCOLLO,
            type: 'string',
          },
          cssCell: this.STYLE_DATA_PROTOCOLLO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo accertamento',
          sortable: false,
          cssCell: this.STYLE_TIPO_ACCERTAMENTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: this.ACC_C.TIPO_ACCERTAMENTO,
            type: 'string',
          },
          cssCell: this.STYLE_TIPO_ACCERTAMENTO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data scadenza',
          sortable: false,
          cssCell: this.STYLE_DATA_SCADENZA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: this.ACC_C.DATA_SCADENZA,
            type: 'string',
          },
          cssCell: this.STYLE_DATA_SCADENZA,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data notifica',
          sortable: false,
          cssCell: this.STYLE_DATA_NOTIFICA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: this.ACC_C.DATA_NOTIFICA,
            type: 'string',
          },
          cssCell: this.STYLE_DATA_NOTIFICA,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Restituito al mittente',
          sortable: false,
          cssCell: this.STYLE_RESTITUITO_AL_MITTENTE,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [
              {
                action: RiscaTableBodyTabMethods.check,
                checkboxConfigs: {
                  isRowData: true,
                  dataTableProperty: this.ACC_C.FLG_RESTITUITO,
                },
              },
            ],
          },
          cssCell: this.STYLE_RESTITUITO_AL_MITTENTE,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Annullato',
          sortable: false,
          cssCell: this.STYLE_ANNULLATO,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [
              {
                action: RiscaTableBodyTabMethods.check,
                checkboxConfigs: {
                  isRowData: true,
                  dataTableProperty: this.ACC_C.FLG_ANNULLATO,
                },
              },
            ],
          },
          cssCell: this.STYLE_ANNULLATO,
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
                disable: (v: RiscaTableDataConfig<AccertamentoVo>) => {
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
                disable: (v: RiscaTableDataConfig<AccertamentoVo>) => {
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
            ],
          },
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = new RiscaTableInput({ sourceMap });
  }
}
