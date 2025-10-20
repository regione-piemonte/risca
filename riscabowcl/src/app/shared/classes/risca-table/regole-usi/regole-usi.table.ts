import { ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import {
  RegolaUsoVo,
  TipoCanoneRegolaUso,
} from '../../../../core/commons/vo/regola-uso-vo';
import { legendaMinimiRegoleUso } from '../../../../features/configurazioni/services/configurazioni/configurazioni-utilities.functions';
import { generaObjErrore, numberComposition } from '../../../miscellaneous/forms-validators/forms-validators';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import {
  IRiscaTableBodyInputField,
  IRiscaTableNumberConfig,
  IRTInputValidatorParams,
  MappaErroriFormECodici,
  RiscaAppendix,
  RiscaAppendixPosition,
  RiscaFormBuilderSize,
  RiscaFormInputCss,
  RiscaFormInputNumber,
  RiscaTableCss,
  RiscaTablePagination,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
  TipoInputTable,
  TRTInputValidator,
} from '../../../utilities';
import { RiscaErrorKeys } from '../../../utilities/classes/errors-keys';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';
import { RegoleUsiTableConsts } from './utilities/regole-usi.consts';
import { RiscaTableDataConfig } from '../../../components/risca/risca-table/utilities/risca-table.classes';
import { LegendaMinimiRegoleUso } from '../../../../features/configurazioni/services/configurazioni/utilities/configurazioni.enums';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati impiegato come configurazione della tabella risca-table.
 */
export interface IRegoleUsoTable {
  uso: string;
  unitaMisura: string;
  canoneUnitario: string;
  minimi: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface RegoleUsiTableConfigs {
  regoleUsi?: RegolaUsoVo[];
  paginazione?: RiscaTablePagination;
  AEA_pagina_canoni_disabled?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table per la visualizzazione delle informazioni.
 * @version SONARQUBE_22_04_24 getDataSource modificata per: sonarqube non apprezza la modifica dati per referenza, per cui ritornare due volte "regoleUsi" viene segnato come errore, nonostante siano condizioni gestite in maniera specifica.
 */
export class RegoleUsiTable
  extends RiscaTableClass<IRegoleUsoTable>
  implements IRiscaTableClass<IRegoleUsoTable>
{
  /** RegoleUsiTableConsts con le costanti per la struttura omonima. */
  private RUT_C = new RegoleUsiTableConsts();

  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_USO: any = {};
  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_UNITA_MISURA: any = {};
  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_CANONE_UNITARIO: any = {};
  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_MINIMI: any = {};
  /** any con la configurazione grafica, compaibile NgStyle, per la colonna. */
  private STYLE_AZIONI: any = {};

  /** Boolean che definisce l'abilitazione di tutte le componenti della pagina. */
  AEA_pagina_canoni_disabled: boolean;

  /**
   * Costruttore.
   */
  constructor(
    configs: RegoleUsiTableConfigs,
    private _riscaFormBuilder: RiscaFormBuilderService
  ) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { regoleUsi, paginazione, AEA_pagina_canoni_disabled } = configs;
    // Setto localmente le informazioni
    this.AEA_pagina_canoni_disabled = AEA_pagina_canoni_disabled ?? false;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(regoleUsi);
    // Lancio il setup delle configurazioni
    this.setupClasse(paginazione);
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (ru: RegolaUsoVo) => {
      // Verifico l'oggetto esista
      if (!ru) {
        return undefined;
      }

      // Recupero le informazioni per la tabella
      const uso: string = ru.tipo_uso?.des_tipo_uso ?? '';
      const unitaMisura: string =
        ru.tipo_uso?.unita_misura?.des_unita_misura ?? '';
      const canoneUnitario: string = '__json_regola_canone';
      const minimi: string = legendaMinimiRegoleUso(ru);

      // Creo l'oggetto convertito
      const obj: IRegoleUsoTable = {
        uso,
        unitaMisura,
        canoneUnitario,
        minimi,
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
    const table = { 'min-width': '1225px' };
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
    // ### USO DI LEGGE / USO DELL'ACQUA
    const uso = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: `Uso dell'acqua`,
        sortable: false,
        cssCell: this.STYLE_USO,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'uso',
          type: 'string',
        },
      })
    );

    // ### UNITA' DI MISURA
    const unitaMisura = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: 'Unità di misura',
        sortable: false,
        cssCell: this.STYLE_UNITA_MISURA,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'unitaMisura',
          type: 'string',
        },
      })
    );

    // ### CANONE UNITARIO
    const canoneUnitario = this.canoneUnitarioConfig();

    // ### MINIMI
    const minimi = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: 'Minimi',
        sortable: false,
        cssCell: this.STYLE_MINIMI,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'minimi',
          type: 'string',
        },
        title: 'Legenda minimi',
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
              action: 'detail',
              disable: (v?: RiscaTableDataConfig<RegolaUsoVo>) => {
                // Recupero la regola uso dalla riga di tabella
                const ru: RegolaUsoVo = v?.original;
                // Genero la legenda per i minimi
                const minimi: string = legendaMinimiRegoleUso(ru);
                // Se la descrizioni per minimi è vuota, allora è disabilitata
                return minimi == LegendaMinimiRegoleUso.vuoto;
                // #
              },
            },
          ],
        },
      }),
    };

    // Inserisco all'interno della sourceMap le configurazioni per le colonne
    sourceMap = [uso, unitaMisura, canoneUnitario, minimi, azioni];

    // Definisco la paginazione di default
    const defaultPagination = this.getDefaultPagination();
    // Definisco la paginazione
    let pagination: RiscaTablePagination;
    pagination = paginazione ?? defaultPagination;
    // Gestisco alcune condizioni specifiche per la paginazione
    pagination.label = defaultPagination.label;
    pagination.showTotal = true;

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, pagination };
  }

  /**
   * Funzione che genera la configurazione per la gestione del campo "Canone unitario".
   * Essendo il campo una input con configurazioni complesse, la funzione impacchetterà le logiche per la costruzione dell'oggetto.
   * @returns RiscaTableSourceMap con la configurazione completa del campo.
   */
  private canoneUnitarioConfig(): RiscaTableSourceMap {
    // Recupero le costanti per la gestione degli errori di una input
    const EM = new RiscaErrorsMap();
    // Definizione delle informazioni statiche della input
    const property = 'canoneUnitario';
    const originalProperty = '__json_regola_canone';

    // Genero la configurazione principale per la gestione delle input
    const numberConfigs = this._riscaFormBuilder.genInputNumber({
      // label: originalProperty,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
      useDecimal: true,
      min: 0,
      decimals: 2,
    });
    // Definizione delle configurazioni specifiche dell'input
    const cssConfig: RiscaFormInputCss = numberConfigs.css as RiscaFormInputCss;
    const dataConfig: RiscaFormInputNumber = numberConfigs.data;
    const disabled: boolean = this.AEA_pagina_canoni_disabled;
    const errMapFormControl: MappaErroriFormECodici[] = [
      ...EM.MAP_GENERIC_ERROR,
      ...EM.MAP_REGOLA_CANONE_MINIMO_UNITARIO_PERCENTUALE,
    ];
    const customValidators: TRTInputValidator<number>[] = [
      this.validatorMinZero,
      this.validator7Int2Float,
    ];
    const angularValidators: ValidatorFn[] = [Validators.required];

    // Definisco la configurazione per la gestione della input
    const numberItFormatConfig: IRiscaTableNumberConfig = {
      cssConfig,
      dataConfig,
      disabled,
      errMapFormControl,
      customValidators,
      angularValidators,
    };
    // Definisco una funzione per gestire la dinamicità dell'appendice dell'input
    let customAppendix: (
      original: any,
      cssConfig: RiscaFormInputCss
    ) => RiscaFormInputCss = this.appendixCondition();

    // Definisco l'oggetto finale di configurazione per la input
    // NOTA BENE: type, come TipoInputTable, deve avere lo stesso formato della configurazione dati definita come (in quest caso): numberItFormatConfig.
    const inputData: IRiscaTableBodyInputField = {
      property,
      originalProperty,
      type: TipoInputTable.numberItFormat,
      numberItFormatConfig,
      customAppendix,
    };

    // Definisco la configurazione del campo
    const canoneUnitario = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: 'Canone unitario',
        sortable: false,
        cssCell: this.STYLE_CANONE_UNITARIO,
      }),
      new RiscaTableSourceMapBody({
        useInput: true,
        inputData,
      })
    );

    // Ritorno la configurazione
    return canoneUnitario;
  }

  /**
   * Funzione che genera un validatore per la input del campo.
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
      // Input definita, verifico se è minore di 0
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

  /**
   * Funzione che genera un validatore per la input del campo.
   * Il validatore gestirà la formattazione numerica della input.
   * @returns TRTInputValidator<number> con la funzione di validazione.
   */
  private validator7Int2Float: TRTInputValidator<number> = (
    params: IRTInputValidatorParams<number>
  ): ValidationErrors | null => {
    // Estraggo dall'input i parametri
    const { value, row, inputConfig } = params ?? {};
    // Lancio la funzione di controllo
    return numberComposition(value, 7, 2);
  };

  /**
   * Funzione di comodo che definisce le logiche della funzione per la customizzazione dell'appendice per le input.
   * @returns (original: any, cssConfig: RiscaFormInputCss) => RiscaFormInputCss; con la funzione di gestione.
   */
  private appendixCondition(): (
    original: any,
    cssConfig: RiscaFormInputCss
  ) => RiscaFormInputCss {
    // Definisco le logiche per la gestione dell'appendice e le ritorno come funzione
    let customAppendix: (
      original: any,
      cssConfig: RiscaFormInputCss
    ) => RiscaFormInputCss;

    // Gestisco l'oggetto originale e verifico le condizioni di attivazione dell'appendice
    customAppendix = (original: RegolaUsoVo, cssConfig: RiscaFormInputCss) => {
      // Verifico l'input
      if (!original || !cssConfig) {
        // Ritorno l'oggetto di configurazione
        return cssConfig;
      }

      // Recupero la proprietà di controllo
      let __tipoCanone: TipoCanoneRegolaUso;
      __tipoCanone = original.__json_regola_tipo_canone;

      // Verifico la tipologia dell'oggetto
      if (__tipoCanone == TipoCanoneRegolaUso.percentuale) {
        // Il tipo canone è percentuale, definisco le configurazioni per l'appendice
        const text: string = '%';
        const position = RiscaAppendixPosition.right;
        // Aggiungo i dati dell'appendice alla configurazione
        cssConfig.appendix = new RiscaAppendix({ text, position });
        // #
      }

      // ritorno la configurazione aggiornata
      return cssConfig;
    };

    // Ritorno la funzione
    return customAppendix;
  }

  /**
   * Genera la paginazione di default
   * @returns RiscaTablePagination come paginazione di default
   */
  getDefaultPagination(): RiscaTablePagination {
    // Recupero la paginazione dalle costanti
    let p: RiscaTablePagination;
    p = this.RUT_C.DEFAULT_PAGINATION;

    // Modifico il totale
    p.total = this.source?.length ?? 0;
    // Ritorno la paginazione
    return p;
  }

  /**
   * ####################
   * FUNZIONI DI OVERRIDE
   * ####################
   */

  /**
   * Funzione che ritorna le informazioni inserite all'interno come valore original.
   * La funzione viene overridata per poter gestire la peculiare struttura degli oggetti RegolaUsoVo.
   * Quando vengono recuperati i dati originali tramite funzione, gli oggetti devono essere aggiornati internamente mediante specifica funzione.
   * @param skipCanoniSynch boolean che permette saltare il passaggio di sincronizzazione delle proprietà degli oggetti RegolaUsoVo. Per default è: true.
   * @returns RegolaUsoVo[] con le informazioni aggiornate secondo flusso di FE.
   * @override
   */
  getDataSource(skipCanoniSynch: boolean = true): RegolaUsoVo[] {
    // Definisco una variabile di supporto per il dato da ritornare
    let regoleUsi: RegolaUsoVo[];
    regoleUsi = this.source?.map((e) => e.original) ?? [];

    // Verifico se devo evitare la sincronizzazione dei dati
    if (skipCanoniSynch) {
      // Ritorno le regole senza allineare le informazioni interne all'oggetto (__json_regola_canone)
      return regoleUsi;
    }

    // Per ogni oggetto, eseguo la funzione di aggiornamento e gestione specifica
    const regoleUsiUpd = regoleUsi?.map((ru: RegolaUsoVo) => {
      // Lancio la funzione di aggiornamento dalle proprietà FE alle proprietà specifiche dei canoni delle regole
      ru.updateRegolaUsoVoFromTable();
      // Ritorno l'oggetto aggiornato
      return ru;
      // #
    });

    // Ritorno tutti gli oggetti originali da source
    return regoleUsiUpd ?? [];
  }
}
