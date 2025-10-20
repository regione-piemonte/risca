import { JsonRegolaRangeVo } from '../../../../core/commons/vo/json-regola-range-vo';
import { RegolaRangeFormConsts } from '../../../../features/configurazioni/components/regola-range-form/utilities/regola-range-form.consts';
import { JsonRegolaRangeFormats } from '../../../../features/configurazioni/services/configurazioni/utilities/configurazioni.consts';
import { RiscaTableDataConfig } from '../../../components/risca/risca-table/utilities/risca-table.classes';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import { formatoImportoITA } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaTableCss,
  RiscaTablePagination,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';
import { RegoleUsoRangeTableConsts } from './utilities/regole-uso-range.consts';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati impiegato come configurazione della tabella risca-table.
 */
export interface IRegoleUsoRangeTable {
  condizione1: string;
  valore1: number;
  condizione2: string;
  valore2: number;
  canoneMinimo: number;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface RegoleUsoRangeTableConfigs {
  jsonRegolaRanges?: JsonRegolaRangeVo[];
  AEA_pagina_canoni_disabled?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table per la visualizzazione delle informazioni.
 */
export class RegoleUsoRangeTable
  extends RiscaTableClass<IRegoleUsoRangeTable>
  implements IRiscaTableClass<IRegoleUsoRangeTable>
{
  /** RegoleUsoRangeTableConsts con le costanti per la struttura omonima. */
  private RURT_C = new RegoleUsoRangeTableConsts();
  /** JsonRegolaRangeFormats con le costanti per la gestione del formato dati dei JsonRegolaRangeVo. */
  private JRRF_C = new JsonRegolaRangeFormats();
  /** RegolaRangeFormConsts con le costanti per la gestione dati simili al form RegolaRangeForm. */
  private RRF_C = new RegolaRangeFormConsts();

  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_CONDIZIONE_1_H: any = { 'text-align': 'center' };
  private STYLE_CONDIZIONE_1_B: any = {
    'text-align': 'center',
    ...this.RRF_C.STYLE_SEGNO_CONDIZIONE_1,
  };
  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_VALORE_1: any = {};
  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_CONDIZIONE_2_H: any = { 'text-align': 'center' };
  private STYLE_CONDIZIONE_2_B: any = {
    'text-align': 'center',
    ...this.RRF_C.STYLE_SEGNO_CONDIZIONE_2,
  };
  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_VALORE_2: any = {};
  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_CANONE_MINIMO_H: any = { width: '140px', 'min-width': '140px' };
  private STYLE_CANONE_MINIMO_B: any = { 'text-align': 'right' };
  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_AZIONI: any = {};

  /** Boolean che definisce l'abilitazione di tutte le componenti della pagina. */
  AEA_pagina_canoni_disabled: boolean;

  /**
   * Costruttore.
   */
  constructor(
    configs: RegoleUsoRangeTableConfigs,
    private _riscaFormBuilder: RiscaFormBuilderService
  ) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { jsonRegolaRanges, AEA_pagina_canoni_disabled } = configs;
    // Setto localmente le informazioni
    this.AEA_pagina_canoni_disabled = AEA_pagina_canoni_disabled ?? false;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(jsonRegolaRanges);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (jsonRR: JsonRegolaRangeVo) => {
      // Verifico l'oggetto esista
      if (!jsonRR) {
        return undefined;
      }

      // Recupero le informazioni per la tabella
      const condizione1: string = this.RURT_C.LABEL_SEGNO_CONDIZIONE_1;
      const valore1: number = jsonRR.soglia_min;
      const condizione2: string = this.RURT_C.LABEL_SEGNO_CONDIZIONE_2;
      const valore2: number = jsonRR.soglia_max;
      const canoneMinimo: number = jsonRR.canone_minimo;

      // Creo l'oggetto convertito
      const obj: IRegoleUsoRangeTable = {
        condizione1,
        valore1,
        condizione2,
        valore2,
        canoneMinimo,
      };

      // Ritorno la configurazione
      return obj;
    };
  }

  /**
   * Funzione di setup per la classe.
   * @param paginazione RiscaTablePagination con la paginazione d'inizializzazione.
   */
  private setupClasse(paginazione?: RiscaTablePagination) {
    // Lancio il setup del cssConfig
    this.setupCssConfig();
    // Lancio il setup del cssConfig
    this.setupDataConfig(paginazione);
  }

  /**
   * Funzione di setup per la configurazione css.
   */
  setupCssConfig() {
    // Definisco le configurazioni per gli stili
    const table = { 'min-width': '1170px' };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(undefined, { table });
  }

  /**
   * Funzione di setup per la configurazione dati.
   * @param paginazione RiscaTablePagination con la paginazione d'inizializzazione.
   */
  setupDataConfig(paginazione?: RiscaTablePagination) {
    // Creo la configurazione RiscaTableSourceMap
    let sourceMap: RiscaTableSourceMap[] = [];

    // Definizione degli oggetti per la configurazione della tabella
    // ### CONDIZIONE 1
    const condizione1 = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: this.RURT_C.LABEL_CONDIZIONE_1,
        sortable: false,
        cssCell: this.STYLE_CONDIZIONE_1_H,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        cssCell: this.STYLE_CONDIZIONE_1_B,
        sourceData: {
          property: 'condizione1',
          type: 'string',
        },
      })
    );

    // ### VALORE 1
    const valore1 = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: this.RURT_C.LABEL_VALORE_1,
        sortable: false,
        cssCell: this.STYLE_VALORE_1,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'valore1',
          type: 'number',
          outputFormat: (v: number) => {
            // Verifico se esiste il valore
            if (v == undefined) {
              return '';
            }
            // Definisco i decimali da visualizzare
            const d = this.JRRF_C.FLOAT_SOGLIA_MIN;
            // Ritorno una formattazione custom
            return `${formatoImportoITA(v, d, true)}`;
          },
        },
      })
    );

    // ### CONDIZIONE 2
    const condizione2 = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: this.RURT_C.LABEL_CONDIZIONE_2,
        sortable: false,
        cssCell: this.STYLE_CONDIZIONE_2_H,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        cssCell: this.STYLE_CONDIZIONE_2_B,
        sourceData: {
          property: 'condizione2',
          type: 'string',
        },
      })
    );

    // ### VALORE 2
    const valore2 = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: this.RURT_C.LABEL_VALORE_2,
        sortable: false,
        cssCell: this.STYLE_VALORE_2,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        cssCell: this.STYLE_VALORE_2,
        sourceData: {
          property: 'valore2',
          type: 'number',
          outputFormat: (v: number) => {
            // Verifico se esiste il valore
            if (v == undefined) {
              return '';
            }
            // Definisco i decimali da visualizzare
            const d = this.JRRF_C.FLOAT_SOGLIA_MAX;
            // Ritorno una formattazione custom
            return `${formatoImportoITA(v, d, true)}`;
          },
        },
      })
    );

    // ### CANONE MINIMO
    const canoneMinimo = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: this.RURT_C.LABEL_CANONE_MINIMO,
        sortable: false,
        cssCell: this.STYLE_CANONE_MINIMO_H,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        cssCell: this.STYLE_CANONE_MINIMO_B,
        sourceData: {
          property: 'canoneMinimo',
          type: 'number',
          outputFormat: (v: number) => {
            // Verifico se esiste il valore
            if (v == undefined) {
              return '';
            }
            // Definisco i decimali da visualizzare
            const d = this.JRRF_C.FLOAT_CANONE_MINIMO;
            // Ritorno una formattazione custom
            return `${formatoImportoITA(v, d, true)} €`;
          },
        },
      })
    );

    // ### AZIONI
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
              action: 'delete',
              disable: (v?: RiscaTableDataConfig<JsonRegolaRangeVo>) => {
                // Ritorno le condizioni
                return this.AEA_pagina_canoni_disabled;
                // #
              },
            },
          ],
        },
      }),
    };

    // Inserisco all'interno della sourceMap le configurazioni per le colonne
    sourceMap = [
      condizione1,
      valore1,
      condizione2,
      valore2,
      canoneMinimo,
      azioni,
    ];

    // Definisco la paginazione
    let pagination: RiscaTablePagination;
    pagination = undefined;

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, pagination };
  }

  // /**
  //  * Funzione che genera la configurazione per la gestione del campo "valore 1", mappato per la soglia minima.
  //  * Essendo il campo una input con configurazioni complesse, la funzione impacchetterà le logiche per la costruzione dell'oggetto.
  //  * @returns RiscaTableSourceMap con la configurazione completa del campo.
  //  */
  // private valore1Configs(): RiscaTableSourceMap {
  //   // Recupero le costanti per la gestione degli errori di una input
  //   const EM = new RiscaErrorsMap();
  //   // Definizione delle informazioni statiche della input
  //   const property = this.RURT_C.FIELD_VALORE_1;
  //   const originalProperty = 'soglia_min';

  //   // Genero la configurazione principale per la gestione delle input
  //   const numberConfigs = this._riscaFormBuilder.genInputNumber({
  //     // label: originalProperty,
  //     size: RiscaFormBuilderSize.small,
  //     showErrorFC: true,
  //     useDecimal: true,
  //     min: 0,
  //     decimals: 4,
  //   });
  //   // Definizione delle configurazioni specifiche dell'input
  //   const cssConfig: RiscaFormInputCss = numberConfigs.css as RiscaFormInputCss;
  //   const dataConfig: RiscaFormInputNumber = numberConfigs.data;
  //   const disabled: boolean = false;
  //   const errMapFormControl: MappaErroriFormECodici[] = [
  //     ...EM.MAP_REGOLA_RANGE_VALORI,
  //     ...EM.MAP_FORM_GROUP_ONE_FIELD_REQUIRED,
  //   ];
  //   const customValidators: TRTInputValidator<number>[] = [
  //     this.numberComposition5Int4Float,
  //     this.emptyValue1AndValue2,
  //   ];
  //   const angularValidators: ValidatorFn[] = [];

  //   // Definisco la configurazione per la gestione della input
  //   const numberItFormatConfig: IRiscaTableNumberConfig = {
  //     cssConfig,
  //     dataConfig,
  //     disabled,
  //     errMapFormControl,
  //     customValidators,
  //     angularValidators,
  //   };

  //   // Definisco l'oggetto finale di configurazione per la input
  //   // NOTA BENE: type, come TipoInputTable, deve avere lo stesso formato della configurazione dati definita come (in quest caso): numberItFormatConfig.
  //   const inputData: IRiscaTableBodyInputField = {
  //     property,
  //     originalProperty,
  //     type: TipoInputTable.numberItFormat,
  //     numberItFormatConfig,
  //   };

  //   // Definisco la configurazione del campo
  //   const valore1 = new RiscaTableSourceMap(
  //     new RiscaTableSourceMapHeader({
  //       label: this.RURT_C.LABEL_VALORE_1,
  //       sortable: false,
  //       cssCell: this.STYLE_VALORE_1,
  //     }),
  //     new RiscaTableSourceMapBody({
  //       useInput: true,
  //       inputData,
  //     })
  //   );

  //   // Ritorno la configurazione
  //   return valore1;
  // }

  // /**
  //  * Funzione che genera la configurazione per la gestione del campo "valore 2", mappato per la soglia massima.
  //  * Essendo il campo una input con configurazioni complesse, la funzione impacchetterà le logiche per la costruzione dell'oggetto.
  //  * @returns RiscaTableSourceMap con la configurazione completa del campo.
  //  */
  // private valore2Configs(): RiscaTableSourceMap {
  //   // Recupero le costanti per la gestione degli errori di una input
  //   const EM = new RiscaErrorsMap();
  //   // Definizione delle informazioni statiche della input
  //   const property = this.RURT_C.FIELD_VALORE_2;
  //   const originalProperty = 'soglia_max';

  //   // Genero la configurazione principale per la gestione delle input
  //   const numberConfigs = this._riscaFormBuilder.genInputNumber({
  //     // label: originalProperty,
  //     size: RiscaFormBuilderSize.small,
  //     showErrorFC: true,
  //     useDecimal: true,
  //     min: 0,
  //     decimals: 4,
  //   });
  //   // Definizione delle configurazioni specifiche dell'input
  //   const cssConfig: RiscaFormInputCss = numberConfigs.css as RiscaFormInputCss;
  //   const dataConfig: RiscaFormInputNumber = numberConfigs.data;
  //   const disabled: boolean = false;
  //   const errMapFormControl: MappaErroriFormECodici[] = [
  //     ...EM.MAP_REGOLA_RANGE_VALORI,
  //     ...EM.MAP_FORM_GROUP_ONE_FIELD_REQUIRED,
  //   ];
  //   const customValidators: TRTInputValidator<number>[] = [
  //     this.numberComposition5Int4Float,
  //     this.emptyValue1AndValue2,
  //   ];
  //   const angularValidators: ValidatorFn[] = [];

  //   // Definisco la configurazione per la gestione della input
  //   const numberItFormatConfig: IRiscaTableNumberConfig = {
  //     cssConfig,
  //     dataConfig,
  //     disabled,
  //     errMapFormControl,
  //     customValidators,
  //     angularValidators,
  //   };

  //   // Definisco l'oggetto finale di configurazione per la input
  //   // NOTA BENE: type, come TipoInputTable, deve avere lo stesso formato della configurazione dati definita come (in quest caso): numberItFormatConfig.
  //   const inputData: IRiscaTableBodyInputField = {
  //     property,
  //     originalProperty,
  //     type: TipoInputTable.numberItFormat,
  //     numberItFormatConfig,
  //   };

  //   // Definisco la configurazione del campo
  //   const valore2 = new RiscaTableSourceMap(
  //     new RiscaTableSourceMapHeader({
  //       label: this.RURT_C.LABEL_VALORE_2,
  //       sortable: false,
  //       cssCell: this.STYLE_VALORE_2,
  //     }),
  //     new RiscaTableSourceMapBody({
  //       useInput: true,
  //       inputData,
  //     })
  //   );

  //   // Ritorno la configurazione
  //   return valore2;
  // }

  // /**
  //  * Funzione che genera la configurazione per la gestione del campo "canone minimo".
  //  * Essendo il campo una input con configurazioni complesse, la funzione impacchetterà le logiche per la costruzione dell'oggetto.
  //  * @returns RiscaTableSourceMap con la configurazione completa del campo.
  //  */
  // private canoneMinimoConfigs(): RiscaTableSourceMap {
  //   // Recupero le costanti per la gestione degli errori di una input
  //   const EM = new RiscaErrorsMap();
  //   // Definizione delle informazioni statiche della input
  //   const property = 'canoneMinimo';
  //   const originalProperty = 'canone_minimo';

  //   // Genero la configurazione principale per la gestione delle input
  //   const numberConfigs = this._riscaFormBuilder.genInputNumber({
  //     // label: originalProperty,
  //     size: RiscaFormBuilderSize.small,
  //     showErrorFC: true,
  //     useDecimal: true,
  //     min: 0,
  //     decimals: 4,
  //   });
  //   // Definizione delle configurazioni specifiche dell'input
  //   const cssConfig: RiscaFormInputCss = numberConfigs.css as RiscaFormInputCss;
  //   // Definisco l'appendice per il campo
  //   const text: string = '€';
  //   const position = RiscaAppendixPosition.right;
  //   // Aggiungo i dati dell'appendice alla configurazione
  //   cssConfig.appendix = new RiscaAppendix({ text, position });

  //   const dataConfig: RiscaFormInputNumber = numberConfigs.data;
  //   const disabled: boolean = false;
  //   const errMapFormControl: MappaErroriFormECodici[] = [];
  //   const customValidators: TRTInputValidator<number>[] = [
  //     this.numberComposition7Int2Float,
  //   ];
  //   const angularValidators: ValidatorFn[] = [Validators.required];

  //   // Definisco la configurazione per la gestione della input
  //   const numberItFormatConfig: IRiscaTableNumberConfig = {
  //     cssConfig,
  //     dataConfig,
  //     disabled,
  //     errMapFormControl,
  //     customValidators,
  //     angularValidators,
  //   };

  //   // Definisco l'oggetto finale di configurazione per la input
  //   // NOTA BENE: type, come TipoInputTable, deve avere lo stesso formato della configurazione dati definita come (in quest caso): numberItFormatConfig.
  //   const inputData: IRiscaTableBodyInputField = {
  //     property,
  //     originalProperty,
  //     type: TipoInputTable.numberItFormat,
  //     numberItFormatConfig,
  //   };

  //   // Definisco la configurazione del campo
  //   const canoneMinimo = new RiscaTableSourceMap(
  //     new RiscaTableSourceMapHeader({
  //       label: this.RURT_C.LABEL_CANONE_MINIMO,
  //       sortable: false,
  //       cssCell: this.STYLE_VALORE_2,
  //     }),
  //     new RiscaTableSourceMapBody({
  //       useInput: true,
  //       inputData,
  //     })
  //   );

  //   // Ritorno la configurazione
  //   return canoneMinimo;
  // }

  // /**
  //  * Funzione che genera un validatore per la input dell'importo versato.
  //  * Il validatore gestirà il valore minimo della input.
  //  * @returns TRTInputValidator<number> con la funzione di validazione.
  //  */
  // private numberComposition5Int4Float: TRTInputValidator<number> = (
  //   params: IRTInputValidatorParams<number>
  // ): ValidationErrors | null => {
  //   // Estraggo dall'input i parametri
  //   const { value, row, inputConfig } = params ?? {};
  //   // Lancio la funzione di controllo
  //   return numberComposition(value, 5, 4);
  // };

  // /**
  //  * Funzione che genera un validatore per la input dell'importo versato.
  //  * Il validatore gestirà il valore minimo della input.
  //  * @returns TRTInputValidator<number> con la funzione di validazione.
  //  */
  // private numberComposition7Int2Float: TRTInputValidator<number> = (
  //   params: IRTInputValidatorParams<number>
  // ): ValidationErrors | null => {
  //   // Estraggo dall'input i parametri
  //   const { value, row, inputConfig } = params ?? {};
  //   // Lancio la funzione di controllo
  //   return numberComposition(value, 7, 2);
  // };

  // /**
  //  * Funzione che genera un validatore per la input dell'importo versato.
  //  * Il validatore gestirà il valore minimo della input.
  //  * @returns TRTInputValidator<number> con la funzione di validazione.
  //  */
  // private emptyValue1AndValue2: TRTInputValidator<number> = (
  //   params: IRTInputValidatorParams<number>
  // ): ValidationErrors | null => {
  //   // Estraggo dall'input i parametri
  //   const { value, row, inputConfig } = params ?? {};
  //   // Recupero il valore originale della riga
  //   let jsonRegola: JsonRegolaRangeVo;
  //   jsonRegola = row?.original;
  //   // Recupero il campo che sto verificando
  //   const field: string = inputConfig?.property;
  //   const isValue1: boolean = field == this.RURT_C.FIELD_VALORE_1;
  //   const isValue2: boolean = field == this.RURT_C.FIELD_VALORE_2;

  //   // Recupero i valori per soglia minima e massima
  //   const sogliaMin: number = isValue1 ? value : jsonRegola?.soglia_min;
  //   const sogliaMax: number = isValue2 ? value : jsonRegola?.soglia_max;

  //   // Verifico le input
  //   if (sogliaMin == undefined && sogliaMax == undefined) {
  //     // Definisco la costante contenente le chiavi d'errore per i forms validators
  //     const ERR_KEY = RiscaErrorKeys;
  //     // Definisco la chiave per l'errore
  //     const key = ERR_KEY.CAMPI_OBBLIGATORI;
  //     // Genero e ritorno l'errore
  //     return generaObjErrore(key);
  //   }

  //   // Almeno uno dei due campi è valorizzato
  //   return null;
  // };

  /**
   * Genera la paginazione di default
   * @returns RiscaTablePagination come paginazione di default
   */
  getDefaultPagination(): RiscaTablePagination {
    // Recupero la paginazione dalle costanti
    let p: RiscaTablePagination;
    p = this.RURT_C.DEFAULT_PAGINATION;

    // Modifico il totale
    p.total = this.source?.length ?? 0;
    // Ritorno la paginazione
    return p;
  }
}
