import { DettaglioPagSearchResultVo } from '../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { PagamentiSDConsts } from '../../../../features/pratiche/components/dati-contabili/pagamenti-sd/utilities/pagamenti-sd.consts';
import {
  convertMomentToViewDate,
  formatoImportoITA,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaTableBodyTabMethodData,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
  RiscaFormBuilderSize,
  RiscaFormInputCss,
  RiscaFormInputNumber,
  MappaErroriFormECodici,
  TRTInputValidator,
  IRiscaTableNumberConfig,
  IRiscaTableBodyInputField,
  TipoInputTable,
  IRTInputValidatorParams,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { ValidatorFn, Validators, ValidationErrors } from '@angular/forms';
import { RiscaErrorKeys } from '../../../utilities/classes/errors-keys';
import { generaObjErrore } from '../../../miscellaneous/forms-validators/forms-validators';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati impiegato come configurazione della tabella risca-table.
 */
export interface IDettagliPagamentoSDTable {
  codiceUtenza: string;
  annualita: number;
  importoDovuto: string;
  importoVersato: any;
  scadenza: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface DettagliVersamentoTableConfigs {
  dettPagSearch?: DettaglioPagSearchResultVo[];
  disabled?: boolean;
  importoVersatoPagamento: number;
}

/**
 * Classe usata per la generazione delle risca-table per la visualizzazione delle informazioni.
 */
export class DettagliPagamentoSDTable
  extends RiscaTableClass<IDettagliPagamentoSDTable>
  implements IRiscaTableClass<IDettagliPagamentoSDTable>
{
  // Costante comune dei versamenti
  public P_C = PagamentiSDConsts;

  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_CODICE_UTENZA = {
    'min-width': '120px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_ANNUALITA = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_DOVUTO = {
    'max-width': '250px',
    'min-width': '125px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_IMPORTO_VERSATO = {
    'max-width': '250px',
    'min-width': '125px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_SCADENZA = {
    'max-width': '250px',
    'min-width': '125px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = { width: '120px' };

  /** Boolean che definisce lo stato di abilitazione delle righe. */
  private _disabled: boolean = false;
  /** number con l'importo versato del pagamento, utilizzato per i controlli sull'inserimento. */
  private _importoVersato: number;

  /**
   * Costruttore.
   */
  constructor(
    configs: DettagliVersamentoTableConfigs,
    private _riscaFormBuilder: RiscaFormBuilderService
  ) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { dettPagSearch, disabled, importoVersatoPagamento } = configs;
    // Assegno localmente le informazioni
    this._disabled = disabled ?? false;
    // Assegno localmente le informazioni
    this._importoVersato = importoVersatoPagamento;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(dettPagSearch);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (dettPagSearch: DettaglioPagSearchResultVo) => {
      // Verifico l'oggetto esista
      if (!dettPagSearch) {
        return undefined;
      }

      // Prendo codice utenza ed annualità
      const codiceUtenza = dettPagSearch.cod_riscossione ?? '';
      const annualita = dettPagSearch.anno;
      const importoDovuto = formatoImportoITA(dettPagSearch?.canone_dovuto);
      const importoVersato = 'importo_versato';
      // Recupero la data scadenza
      const dtScadenza = dettPagSearch.data_scadenza_pagamento;
      const scadenza = convertMomentToViewDate(dtScadenza);

      // Creo l'oggetto convertito
      const conv: IDettagliPagamentoSDTable = {
        codiceUtenza,
        annualita,
        importoDovuto,
        importoVersato,
        scadenza,
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
      'min-width': '1225px',
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

    // # CODICE UTENZA
    const codiceUtenza: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Codice utenza',
        sortable: false,
        cssCell: this.STYLE_CODICE_UTENZA,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'codiceUtenza',
          type: 'string',
        },
        cssCell: this.STYLE_CODICE_UTENZA,
      }),
    };

    // # ANNUALITA
    const annualita: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Annualità',
        sortable: false,
        cssCell: this.STYLE_ANNUALITA,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'annualita',
          type: 'string',
        },
        cssCell: this.STYLE_ANNUALITA,
      }),
    };

    // # IMPORTO DOVUTO
    const importoDovuto: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Importo dovuto',
        sortable: false,
        cssCell: this.STYLE_IMPORTO_DOVUTO,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'importoDovuto',
          type: 'string',
        },
        cssCell: this.STYLE_IMPORTO_DOVUTO,
      }),
    };

    // # IMPORTO VERSATO (VEDI SOTTO!!!)
    let importoVersato: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Importo versato',
        sortable: false,
        cssCell: this.STYLE_IMPORTO_VERSATO,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'importoVersato',
          type: 'string',
        },
        cssCell: this.STYLE_IMPORTO_VERSATO,
      }),
    };
    // RISCA-798 => Richiesta di definire importo versato come input. Sostituisco la configurazione
    importoVersato = this.importoVersatoConfig();

    // # SCADENZA
    const scadenza: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Scadenza',
        sortable: false,
        cssCell: this.STYLE_SCADENZA,
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'scadenza',
          type: 'string',
        },
        cssCell: this.STYLE_SCADENZA,
      }),
    };

    // # AZIONI
    const azioni: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Azioni',
        sortable: false,
        cssCell: this.STYLE_AZIONI,
      }),
      body: new RiscaTableSourceMapBody({
        useTabMethod: true,
        tabMethodData: {
          actions: [this.actionDettaglioPagamento(), this.actionDelete()],
        },
      }),
    };

    // Inserisco all'interno della sourceMap le configurazioni per le colonne
    sourceMap.push(
      codiceUtenza,
      annualita,
      importoDovuto,
      importoVersato,
      scadenza,
      azioni
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }

  /**
   * Crea l'action della tabella per l'indirizzo spedizione
   * @returns RiscaTableBodyTabMethodData
   */
  private actionDettaglioPagamento(): RiscaTableBodyTabMethodData {
    return {
      action: 'modify',
      custom: true,
      title: this.P_C.DETTAGLIO_PAGAMENTO,
      class: this.P_C.RISCA_TABLE_ACTION,
      style: { width: '25px' },
      disable: (v: any) => {
        // Ritorno la configurazione
        return this._disabled;
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
        // Ritorno la configurazione
        return this._disabled;
      },
    };
  }

  /**
   * Funzione che genera la configurazione per la gestione del campo "Importo versato".
   * Essendo il campo una input con configurazioni complesse, la funzione impacchetterà le logiche per la costruzione dell'oggetto.
   * @returns RiscaTableSourceMap con la configurazione completa del campo.
   */
  private importoVersatoConfig(): RiscaTableSourceMap {
    // Recupero le costanti per la gestione degli errori di una input
    const EM = new RiscaErrorsMap();
    // Definizione delle informazioni statiche della input
    const property = 'importoVersato';
    const originalProperty = 'importo_versato';

    // Genero la configurazione principale per la gestione delle input
    const numberConfigs = this._riscaFormBuilder.genInputNumber({
      // label: originalProperty,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
      useDecimal: true,
      decimals: 2,
      decimaliNonSignificativi: true,
      min: 0,
    });
    // Definizione delle configurazioni specifiche dell'input
    const cssConfig: RiscaFormInputCss = numberConfigs.css as RiscaFormInputCss;
    const dataConfig: RiscaFormInputNumber = numberConfigs.data;
    const disabled: boolean = false;
    const errMapFormGroup: MappaErroriFormECodici[] = [];
    const errMapFormControl: MappaErroriFormECodici[] = [
      ...EM.MAP_GENERIC_ERROR,
      ...EM.MAP_IMP_VERSATO_MAGG_CANONE_DOVUTO,
      ...EM.MAP_IMP_VERSATO_MAGG_IMP_PAGAMENTO,
      ...EM.MAP_FORM_CONTROL_REQUIRED,
    ];
    const customValidators: TRTInputValidator<number>[] = [
      this.validatorMinZero,
      // this.validatorMaxCanoneDovuto,
      this.validatorMaxImportoVersato,
    ];
    const angularValidators: ValidatorFn[] = [Validators.required];

    // Definisco la configurazione per la gestione della input
    const numberItFormatConfig: IRiscaTableNumberConfig = {
      cssConfig,
      dataConfig,
      disabled,
      errMapFormControl,
      errMapFormGroup,
      customValidators,
      angularValidators,
    };
    // Definisco l'oggetto finale di configurazione per la input
    const inputData: IRiscaTableBodyInputField = {
      property,
      originalProperty,
      type: TipoInputTable.numberItFormat,
      numberItFormatConfig,
    };

    // Definisco la configurazione del campo
    const importoVersato = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: '*Importo versato',
        sortable: false,
        cssCell: this.STYLE_IMPORTO_VERSATO,
      }),
      new RiscaTableSourceMapBody({
        useInput: true,
        inputData,
      })
    );

    // Ritorno la configurazione
    return importoVersato;
  }

  /**
   * Funzione che genera un validatore per la input dell'importo versato.
   * Il validatore gestirà il valore minimo della input.
   * @returns TRTInputValidator<number> con la funzione di validazione.
   */
  private validatorMinZero: TRTInputValidator<number> = (
    params: IRTInputValidatorParams<number>
  ): ValidationErrors | null => {
    // Estraggo dall'input i parametri
    const { value, row, inputConfig } = params ?? {};
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Verifico che il valore sia definito
    if (value != undefined) {
      // Input definita, verifico se è minore uguale di 0
      // [VF] Issue 88 - si richiede di poter inserire il valore 0 nell'importo versato sugli stati debitori selezionati
      if (value < 0) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.ERRORE_FORMATO_GENERICO;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
      }
    }

    // Nessun errore
    return null;
  };

  // /**
  //  * Funzione che genera un validatore per la input dell'importo versato.
  //  * Il validatore gestirà il valore massimo della input.
  //  * @returns TRTInputValidator<number> con la funzione di validazione.
  //  */
  // private validatorMaxCanoneDovuto: TRTInputValidator<number> = (
  //   params: IRTInputValidatorParams<number>
  // ): ValidationErrors | null => {
  //   // Estraggo dall'input i parametri
  //   const { value, row, inputConfig } = params ?? {};
  //   // Definisco la costante contenente le chiavi d'errore per i forms validators
  //   const ERR_KEY = RiscaErrorKeys;
  //   // Recupero dall'oggetto della riga l'oggetto originale
  //   const statoDebitorio: StatoDebitorioVo = row?.original;
  //   // Dallo stato debitorio recupero il valore per l'importo del canone dovuto
  //   const canoneDovuto: number = statoDebitorio?.canone_dovuto;

  //   // Verifico che il valore sia definito
  //   if (value != undefined) {
  //     // Input definita, verifico se il valore è maggiore del canone dovuto o dell'importo versato
  //     if (value > canoneDovuto) {
  //       // Recupero la chiave d'errore
  //       const key = ERR_KEY.IMPORTO_VERSATO_MAGG_CANONE_DOVUTO;
  //       // Genero e ritorno l'errore
  //       return generaObjErrore(key);
  //     }
  //   }

  //   // Nessun errore
  //   return null;
  // };

  /**
   * Funzione che genera un validatore per la input dell'importo versato.
   * Il validatore gestirà il valore massimo della input.
   * @returns TRTInputValidator<number> con la funzione di validazione.
   */
  private validatorMaxImportoVersato: TRTInputValidator<number> = (
    params: IRTInputValidatorParams<number>
  ): ValidationErrors | null => {
    // Estraggo dall'input i parametri
    const { value, row, inputConfig } = params ?? {};
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;
    // Dall'instanza della classe recupero il valore per l'importo versato
    const importoVersato: number = this._importoVersato;

    // Verifico che il valore sia definito
    if (value != undefined) {
      // Input definita, verifico se il valore è maggiore del canone dovuto o dell'importo versato
      if (value > importoVersato) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.IMPORTO_VERSATO_MAGG_IMPORTO_PAGAMENTO;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
      }
    }

    // Nessun errore
    return null;
  };
}
