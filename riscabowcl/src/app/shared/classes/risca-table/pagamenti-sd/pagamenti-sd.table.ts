import { RiscaTableDataConfig } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.classes';
import {
  RiscaIconsCss,
  RiscaIconsCssDisabled,
} from 'src/app/shared/enums/icons.enums';
import {
  convertMomentToViewDate,
  formatoImportoITA,
} from 'src/app/shared/services/risca/risca-utilities/risca-utilities.functions';
import { DettaglioPagamentoExtraVo } from '../../../../core/commons/vo/dettaglio-pagamento-extra';
import { PagamentoVo } from '../../../../core/commons/vo/pagamento-vo';
import { PagamentiSDConsts } from '../../../../features/pratiche/components/dati-contabili/pagamenti-sd/utilities/pagamenti-sd.consts';
import { isPagamentoManuale } from '../../../../features/pratiche/service/dati-contabili/versamenti/versamenti.functions';
import {
  RiscaTableBodyTabMethodData,
  RiscaTableBodyTabMethods,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IDettPagExtraTable {
  dataVersamento?: string;
  importoVersato?: number;
  modalitaPagamento?: string;
  note?: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface DettPagExtraTableConfigs {
  dettPagExtra?: DettaglioPagamentoExtraVo[];
  disableUserInputs?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table.
 */
export class DettPagExtraTable
  extends RiscaTableClass<IDettPagExtraTable>
  implements IRiscaTableClass<IDettPagExtraTable>
{
  // Costante comune dei versamenti
  public V_C = PagamentiSDConsts;

  /** Stile per le colonne. */
  private STYLE_DATA_VERSAMENTO = {};
  private STYLE_IMPORTO_VERSATO = {};
  private STYLE_MODALITA_PAGAMENTO = {};
  private STYLE_NOTE = {};
  private STYLE_AZIONI = {};

  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _disableUserInputs: boolean;

  /**
   * Costruttore.
   */
  constructor(configs: DettPagExtraTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { dettPagExtra, disableUserInputs } = configs;
    // Assegno le variabili
    this._disableUserInputs = disableUserInputs || false;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(dettPagExtra);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (dettPagExtra: DettaglioPagamentoExtraVo) => {
      // Verifico l'oggetto esista
      if (!dettPagExtra) {
        return undefined;
      }

      // Recupero le informazioni dal dettaglio pagamento
      const pagamento = dettPagExtra.pagamento;

      // Valorizzo le colonne
      const dataVersamento = convertMomentToViewDate(pagamento?.data_op_val);
      const importoVersato = dettPagExtra.importo_versato;
      const modalitaPagamento =
        pagamento?.tipo_modalita_pag?.des_modalita_pag ?? '';
      const note = pagamento?.note ?? '';

      // Definisco l'oggetto da ritornare dalla conversione
      const row: IDettPagExtraTable = {
        dataVersamento,
        importoVersato,
        modalitaPagamento,
        note,
      };

      // Ritorno l'oggetto dati
      return row;
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
          label: 'Data pagamento',
          sortable: false,
          cssCell: this.STYLE_DATA_VERSAMENTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataVersamento',
            type: 'date',
          },
          cssCell: this.STYLE_DATA_VERSAMENTO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Importo versato',
          sortable: false,
          cssCell: this.STYLE_IMPORTO_VERSATO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'importoVersato',
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v);
            },
          },
          cssCell: this.STYLE_IMPORTO_VERSATO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Modalità di pagamento',
          sortable: false,
          cssCell: this.STYLE_MODALITA_PAGAMENTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'modalitaPagamento',
            type: 'string',
          },
          cssCell: this.STYLE_MODALITA_PAGAMENTO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Note',
          sortable: false,
          cssCell: this.STYLE_NOTE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'note',
            type: 'string',
          },
          cssCell: this.STYLE_NOTE,
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
              this.actionDettaglioPagamento(),
              {
                action: 'delete',
                disable: (
                  v: RiscaTableDataConfig<DettaglioPagamentoExtraVo>
                ) => {
                  // Invoco la funzione di disabilitazione delete
                  return this.disableDelete(v);
                },
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

  /**
   * Funzione che pilota l'abilitazione del pulsante di cancellazione del pagamento.
   * @param v RiscaTableDataConfig<DettaglioPagamentoExtraVo> contenente il pagamento da autorizzare alla cancellazione.
   * @returns true se il pagamento non è manuale.
   */
  private disableDelete(v: RiscaTableDataConfig<DettaglioPagamentoExtraVo>) {
    // La prima verifica deriva dalle configurazioni
    if (this._disableUserInputs) {
      // Ritorno true, va disabilitato in ogni caso
      return true;
    }

    // Recupero dalla riga l'oggetto originale
    const dettPagExt: DettaglioPagamentoExtraVo = v?.original;
    // Dall'oggetto recupero il pagamento
    const pagamento: PagamentoVo = dettPagExt?.pagamento;
    // Richiamo il controllo sul pagamento manuale
    const isPagMan = isPagamentoManuale(pagamento);
    // Se il pagamento non è manuale, la cancellazione è disabilitata
    const disable = !isPagMan;

    // Se non è manuale, devo disabilitarlo
    return disable;
  }

  /**
   * Crea l'action della tabella per l'indirizzo spedizione
   * @returns RiscaTableBodyTabMethodData
   */
  private actionDettaglioPagamento(): RiscaTableBodyTabMethodData {
    return {
      action: RiscaTableBodyTabMethods.modify,
      custom: false,
      title: this.V_C.DETTAGLIO_PAGAMENTO,
      class: this.V_C.RISCA_TABLE_ACTION,
      style: { width: '25px' },
      disable: (v: RiscaTableDataConfig<DettaglioPagamentoExtraVo>) => {
        // La prima verifica deriva dalle configurazioni
        if (this._disableUserInputs) {
          // Ritorno true, va disabilitato in ogni caso
          return true;
          // #
        } else {
          // Abilitato
          return false;
        }
      },
      chooseIconEnabled: (
        v: RiscaTableDataConfig<DettaglioPagamentoExtraVo>,
        ...p
      ) => {
        return RiscaIconsCss.pagamento;
      },
      chooseIconDisabled: (
        v: RiscaTableDataConfig<DettaglioPagamentoExtraVo>,
        ...p
      ) => {
        return RiscaIconsCssDisabled.pagamento;
      },
    };
  }
}
