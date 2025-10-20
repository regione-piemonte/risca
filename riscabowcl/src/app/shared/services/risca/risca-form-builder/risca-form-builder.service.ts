import { Injectable } from '@angular/core';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import {
  DynamicObjString,
  IRiscaCheckboxData,
  IRiscaRadioData,
  RiscaComponentConfig,
  RiscaDatepickerConfig,
  RiscaFormBuilderSize,
  RiscaFormInputCheckbox,
  RiscaFormInputCss,
  RiscaFormInputDatepicker,
  RiscaFormInputEmail,
  RiscaFormInputNumber,
  RiscaFormInputRadio,
  RiscaFormInputSelect,
  RiscaFormInputText,
  RiscaFormInputTypeahead,
  TRiscaFormInputChoiceOrientation,
  RiscaFormInputSearch,
} from '../../../utilities';
import { RCOrientamento } from '../../../utilities/enums/utilities.enums';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';

/**
 * Interfaccia contenente le proprietà comuni per la configurazione delle form structures.
 */
export interface IRiscaFormBuilderCommon {
  size?: RiscaFormBuilderSize;
  label?: string;
  labelLeft?: string;
  labelRight?: string;
  labelBottom?: string;
  hideLabel?: boolean;
  hideLabelLeft?: boolean;
  hideLabelRight?: boolean;
  hideLabelBottom?: boolean;
  showErrorFG?: boolean;
  showErrorFC?: boolean;
  extraLabelRight?: string;
  extraLabelSub?: string;
  placeholder?: string;
  title?: string;
}

/**
 * Interfaccia contenente le proprietà comuni per la configurazione delle form structures che gestiscono numeri.
 */
export interface IRiscaFormBuilderCommonNumber {
  onlyNumber?: boolean;
  useDecimal?: boolean;
  useSign?: boolean;
}

/**
 * Interfaccia contenente le proprietà comuni per la configurazione delle form structures che gestiscono le scelte tramite radio/checkbox.
 */
export interface IRiscaFormBuilderCommonChoice {
  orientation?: TRiscaFormInputChoiceOrientation;
}

/**
 * Interfaccia che definisce le configurazioni per il componente risca-text.
 */
export interface IRiscaInputText
  extends IRiscaFormBuilderCommon,
    IRiscaFormBuilderCommonNumber {
  maxLength?: number;
}

/**
 * Interfaccia che definisce le configurazioni per il componente risca-text-fake.
 */
export interface IRiscaInputTextFake extends IRiscaFormBuilderCommon {}

/**
 * Interfaccia che definisce le configurazioni per il componente risca-search.
 */
export interface IRiscaInputSearch
  extends IRiscaFormBuilderCommon,
    IRiscaFormBuilderCommonNumber {
  maxLength?: number;
}

/**
 * Interfaccia che definisce le configurazioni per il componente risca-datepicker.
 */
export interface IRiscaInputDatepicker extends IRiscaFormBuilderCommon {
  minDate?: NgbDateStruct;
  maxDate?: NgbDateStruct;
}

/**
 * Interfaccia che definisce le configurazioni per il componente risca-typeahead.
 */
export interface IRiscaInputTypeahead
  extends IRiscaFormBuilderCommon,
    IRiscaFormBuilderCommonNumber {
  typeaheadSearch: (v: string) => Observable<any[]>;
  typeaheadMap: (v: any) => string;
  searchOnLength?: number;
  maxLength?: number;
  debounceTime?: number;
}

/**
 * Interfaccia che definisce le configurazioni per il componente risca-select.
 */
export interface IRiscaInputSelect extends IRiscaFormBuilderCommon {
  defaultLabel?: string;
  emptyLabelSelected?: boolean;
  ignoreDefault?: boolean;
  multiple?: boolean;
  customOption?: (v: any) => string;
  customOptionClass?: (v: any) => string[];
  customOptionStyle?: (v: any) => DynamicObjString;
}

/**
 * Interfaccia che definisce le configurazioni per il componente risca-email.
 */
export interface IRiscaInputEmail extends IRiscaFormBuilderCommon {
  maxLength?: number;
}

/**
 * Interfaccia che definisce le configurazioni per il componente risca-checkbox.
 */
export interface IRiscaInputCheckbox
  extends IRiscaFormBuilderCommon,
    IRiscaFormBuilderCommonChoice {}

/**
 * Interfaccia che definisce le configurazioni per il componente risca-radio.
 */
export interface IRiscaInputRadio
  extends IRiscaFormBuilderCommon,
    IRiscaFormBuilderCommonChoice {}

/**
 * Interfaccia che definisce le configurazioni per il componente risca-textarea.
 */
export interface IRiscaInputTextarea extends IRiscaFormBuilderCommon {
  maxLength?: number;
}

export interface IRiscaInputNumber extends IRiscaFormBuilderCommon {
  maxLength?: number;
  useDecimal?: boolean;
  useSign?: boolean;
  min?: number;
  max?: number;
  step?: number;
  decimals?: number;
  decimaliNonSignificativi?: boolean;
  allowThousandsSeparator?: boolean;
}

/**
 * Servizio di utility con funzionalità di comodo per la generazione delle configurazioni per i campi dei form dell'applicazione.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaFormBuilderService {
  /** Costante per la classe di stile: form */
  private RISCA_INPUT_SMALL = 'risca-input-small';
  private RISCA_INPUT_STANDARD = 'risca-input-standard';
  private RISCA_INPUT_STANDARD_X2 = 'risca-input-standard-x2';
  private RISCA_INPUT_STANDARD_X3 = 'risca-input-standard-x3';
  private RISCA_INPUT_STANDARD_FULL = 'risca-input-full';
  /** Costante per la classe di stile: input */
  private RISCA_INPUT_CONTENT_SMALL = 'risca-input-content-small';
  private RISCA_INPUT_CONTENT_STANDARD = 'risca-input-content-standard';
  private RISCA_INPUT_DATEPICKER_SMALL = 'risca-input-datepicker-sm';
  private RISCA_INPUT_TEXTAREA_STANDARD = 'risca-input-textarea-standard';
  /** Costante per la classe di stile: label */
  private RISCA_INPUT_LABEL_SMALL = 'risca-input-label-small';
  private RISCA_INPUT_LABEL_STANDARD = 'risca-input-label-standard';
  private RISCA_INPUT_LABEL_RADIO_H = 'risca-input-label-radio r-p--0';
  private RISCA_INPUT_LABEL_RADIO_V =
    'risca-input-label-radio risca-input-label-radio-v';
  private RISCA_INPUT_RADIO_V = 'risca-radio-v';
  private RISCA_INPUT_LABEL_CHECK_H = 'risca-input-label-checkbox r-p--0';
  private RISCA_INPUT_LABEL_CHECK_V =
    'risca-input-label-checkbox risca-input-label-checkbox-v';
  private RISCA_INPUT_CHECK_V = 'risca-checkbox-v';

  /**
   * Costruttore
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * ########################
   * GENERATORI DI CSS CONFIG
   * ########################
   */

  /**
   * Funzione che genera una classe RiscaFormInputCss con dimensioni standard.
   * @param customForm string con la classe di stile per form.
   * @param customInput string con la classe di stile per input.
   * @param customLabel string con la classe di stile per label.
   * @param showErrorFG boolean come flag di attivazione per la visualizzazione degli errori sul form group.
   * @param showErrorFC boolean come flag di attivazione per la visualizzazione degli errori sul form control.
   * @returns RiscaFormInputCss generata.
   */
  genRiscaFormInputCss(
    customForm: string,
    customInput: string,
    customLabel: string,
    showErrorFG: boolean,
    showErrorFC: boolean
  ): RiscaFormInputCss {
    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = new RiscaFormInputCss({
      customForm,
      customInput,
      customLabel,
      showErrorFG,
      showErrorFC,
    });
    // Ritorno l'oggetto generato
    return css;
  }

  /**
   * Funzione che genera una classe RiscaFormInputCss con dimensioni piccole.
   * @param showErrorFG boolean come flag di attivazione per la visualizzazione degli errori sul form group.
   * @param showErrorFC boolean come flag di attivazione per la visualizzazione degli errori sul form control.
   * @returns RiscaFormInputCss generata.
   */
  genRiscaFormInputCssSmall(
    showErrorFG: boolean,
    showErrorFC: boolean
  ): RiscaFormInputCss {
    // Definisco i parametri della input
    const customForm = this.RISCA_INPUT_SMALL;
    const customInput = this.RISCA_INPUT_CONTENT_SMALL;
    const customLabel = this.RISCA_INPUT_LABEL_SMALL;
    // Ritorno l'oggetto generato per il css
    return this.genRiscaFormInputCss(
      customForm,
      customInput,
      customLabel,
      showErrorFG,
      showErrorFC
    );
  }

  /**
   * Funzione che genera una classe RiscaFormInputCss con dimensioni standard.
   * @param showErrorFG boolean come flag di attivazione per la visualizzazione degli errori sul form group.
   * @param showErrorFC boolean come flag di attivazione per la visualizzazione degli errori sul form control.
   * @returns RiscaFormInputCss generata.
   */
  genRiscaFormInputCssStandard(
    showErrorFG: boolean,
    showErrorFC: boolean
  ): RiscaFormInputCss {
    // Definisco i parametri della input
    const customForm = this.RISCA_INPUT_STANDARD;
    const customInput = this.RISCA_INPUT_CONTENT_STANDARD;
    const customLabel = this.RISCA_INPUT_LABEL_STANDARD;
    // Ritorno l'oggetto generato per il css
    return this.genRiscaFormInputCss(
      customForm,
      customInput,
      customLabel,
      showErrorFG,
      showErrorFC
    );
  }

  /**
   * Funzione che genera una classe RiscaFormInputCss con dimensioni standard x2.
   * @param showErrorFG boolean come flag di attivazione per la visualizzazione degli errori sul form group.
   * @param showErrorFC boolean come flag di attivazione per la visualizzazione degli errori sul form control.
   * @returns RiscaFormInputCss generata.
   */
  genRiscaFormInputCssX2(
    showErrorFG: boolean,
    showErrorFC: boolean
  ): RiscaFormInputCss {
    // Definisco i parametri della input
    const customForm = this.RISCA_INPUT_STANDARD_X2;
    const customInput = this.RISCA_INPUT_CONTENT_STANDARD;
    const customLabel = this.RISCA_INPUT_LABEL_STANDARD;
    // Ritorno l'oggetto generato per il css
    return this.genRiscaFormInputCss(
      customForm,
      customInput,
      customLabel,
      showErrorFG,
      showErrorFC
    );
  }

  /**
   * Funzione che genera una classe RiscaFormInputCss con dimensioni standard x2.
   * @param showErrorFG boolean come flag di attivazione per la visualizzazione degli errori sul form group.
   * @param showErrorFC boolean come flag di attivazione per la visualizzazione degli errori sul form control.
   * @returns RiscaFormInputCss generata.
   */
  genRiscaFormInputCssX3(
    showErrorFG: boolean,
    showErrorFC: boolean
  ): RiscaFormInputCss {
    // Definisco i parametri della input
    const customForm = this.RISCA_INPUT_STANDARD_X3;
    const customInput = this.RISCA_INPUT_CONTENT_STANDARD;
    const customLabel = this.RISCA_INPUT_LABEL_STANDARD;
    // Ritorno l'oggetto generato per il css
    return this.genRiscaFormInputCss(
      customForm,
      customInput,
      customLabel,
      showErrorFG,
      showErrorFC
    );
  }

  /**
   * Funzione che genera una classe RiscaFormInputCss con dimensioni piccole.
   * @param showErrorFG boolean come flag di attivazione per la visualizzazione degli errori sul form group.
   * @param showErrorFC boolean come flag di attivazione per la visualizzazione degli errori sul form control.
   * @returns RiscaFormInputCss generata.
   */
  genRiscaFormDatepickerCssSmall(
    showErrorFG: boolean,
    showErrorFC: boolean
  ): RiscaFormInputCss {
    // Definisco i parametri del calendario
    const customForm = this.RISCA_INPUT_SMALL;
    const customInput = this.RISCA_INPUT_DATEPICKER_SMALL;
    const customLabel = this.RISCA_INPUT_LABEL_STANDARD;
    // Ritorno l'oggetto generato per il css
    return this.genRiscaFormInputCss(
      customForm,
      customInput,
      customLabel,
      showErrorFG,
      showErrorFC
    );
  }

  /**
   * Funzione che genera una classe RiscaFormInputCss con dimensioni full.
   * @param showErrorFG boolean come flag di attivazione per la visualizzazione degli errori sul form group.
   * @param showErrorFC boolean come flag di attivazione per la visualizzazione degli errori sul form control.
   * @returns RiscaFormInputCss generata.
   */
  genRiscaFormInputCssFull(
    showErrorFG: boolean,
    showErrorFC: boolean
  ): RiscaFormInputCss {
    // Definisco i parametri della input
    const customForm = this.RISCA_INPUT_STANDARD_FULL;
    const customInput = this.RISCA_INPUT_CONTENT_STANDARD;
    const customLabel = this.RISCA_INPUT_LABEL_STANDARD;
    // Ritorno l'oggetto generato per il css
    return this.genRiscaFormInputCss(
      customForm,
      customInput,
      customLabel,
      showErrorFG,
      showErrorFC
    );
  }

  /**
   * ###############################
   * SELETTORE DIMENSIONI CSS CONFIG
   * ###############################
   */

  /**
   * Funzione di selettore dimensioni per la configurazione css.
   * @param size RiscaFormBuilderSize con la dimensione della input.
   * @param showErrorFG boolean per visualizzare gli errori del form group. Default: false.
   * @param showErrorFC boolean per visualizzare gli errori del form control. Default: false.
   * @returns RiscaFormInputCss con la connfigurazione.
   */
  private selettoreDimensioneCssConfig(
    size: RiscaFormBuilderSize,
    showErrorFG: boolean,
    showErrorFC: boolean
  ): RiscaFormInputCss {
    // Definisco un contenitore per la dimensione css
    let css: RiscaFormInputCss;
    // Verifico la dimensione per il css (PER ORA ESISTE SOLO UNA CONFIGURAZIONE SMALL)
    switch (size) {
      case 'small':
        css = this.genRiscaFormInputCssSmall(showErrorFG, showErrorFC);
        break;
      case 'standard':
        css = this.genRiscaFormInputCssStandard(showErrorFG, showErrorFC);
        break;
      case 'x2':
        css = this.genRiscaFormInputCssX2(showErrorFG, showErrorFC);
        break;
      case 'x3':
        css = this.genRiscaFormInputCssX3(showErrorFG, showErrorFC);
        break;
      case 'full':
        css = this.genRiscaFormInputCssFull(showErrorFG, showErrorFC);
        break;
      default:
        css = this.genRiscaFormInputCssStandard(showErrorFG, showErrorFC);
    }

    // Ritorno la configurazione
    return css;
  }

  /**
   * ####################
   * GENERATORI DI CONFIG
   * ####################
   */

  /**
   * Funzione per la generazione di una input: text.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputText contenente le configurazioni per il componente.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputText(c: IRiscaInputText): RiscaComponentConfig<RiscaFormInputText> {
    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = this.selettoreDimensioneCssConfig(
      c.size || RiscaFormBuilderSize.standard,
      c.showErrorFG !== undefined ? c.showErrorFG : false,
      c.showErrorFC !== undefined ? c.showErrorFC : false
    );
    // Definisco l'oggetto per i dati
    const data: RiscaFormInputText = new RiscaFormInputText({
      label: c.label,
      labelLeft: c.labelLeft,
      labelRight: c.labelRight,
      labelBottom: c.labelBottom,
      hideLabel: c.hideLabel,
      hideLabelLeft: c.hideLabelLeft,
      hideLabelRight: c.hideLabelRight,
      hideLabelBottom: c.hideLabelBottom,
      maxLength: c.maxLength || 20,
      onlyNumber: c.onlyNumber !== undefined ? c.onlyNumber : false,
      useDecimal: c.useDecimal !== undefined ? c.useDecimal : false,
      useSign: c.useSign !== undefined ? c.useDecimal : false,
      extraLabelRight: c.extraLabelRight,
      extraLabelSub: c.extraLabelSub,
      placeholder: c.placeholder,
    });

    // Ritorno classe di stile e dati
    return { css, data };
  }

  /**
   * Funzione per la generazione di una input: text.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputTextFake contenente le configurazioni per il componente.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputTextFake(
    c: IRiscaInputTextFake
  ): RiscaComponentConfig<RiscaFormInputText> {
    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = this.selettoreDimensioneCssConfig(
      c.size || RiscaFormBuilderSize.standard,
      c.showErrorFG !== undefined ? c.showErrorFG : false,
      c.showErrorFC !== undefined ? c.showErrorFC : false
    );

    // Definisco l'oggetto per i dati
    const data: RiscaFormInputText = new RiscaFormInputText({
      label: c.label,
      labelLeft: c.labelLeft,
      labelRight: c.labelRight,
      labelBottom: c.labelBottom,
      hideLabel: c.hideLabel,
      hideLabelLeft: c.hideLabelLeft,
      hideLabelRight: c.hideLabelRight,
      hideLabelBottom: c.hideLabelBottom,
      // maxLength: c.maxLength || 20,
      // onlyNumber: c.onlyNumber !== undefined ? c.onlyNumber : false,
      // useDecimal: c.useDecimal !== undefined ? c.useDecimal : false,
      // useSign: c.useSign !== undefined ? c.useDecimal : false,
      extraLabelRight: c.extraLabelRight,
      extraLabelSub: c.extraLabelSub,
      placeholder: c.placeholder,
    });

    // Ritorno classe di stile e dati
    return { css, data };
  }

  /**
   * Funzione per la generazione di una input: search.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputSearch contenente le configurazioni per il componente.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputSearch(
    c: IRiscaInputSearch
  ): RiscaComponentConfig<RiscaFormInputSearch> {
    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = this.selettoreDimensioneCssConfig(
      c.size || RiscaFormBuilderSize.standard,
      c.showErrorFG !== undefined ? c.showErrorFG : false,
      c.showErrorFC !== undefined ? c.showErrorFC : false
    );
    // Definisco l'oggetto per i dati
    const data: RiscaFormInputSearch = new RiscaFormInputSearch({
      label: c.label,
      labelLeft: c.labelLeft,
      labelRight: c.labelRight,
      labelBottom: c.labelBottom,
      hideLabel: c.hideLabel,
      hideLabelLeft: c.hideLabelLeft,
      hideLabelRight: c.hideLabelRight,
      hideLabelBottom: c.hideLabelBottom,
      maxLength: c.maxLength || 20,
      onlyNumber: c.onlyNumber !== undefined ? c.onlyNumber : false,
      useDecimal: c.useDecimal !== undefined ? c.useDecimal : false,
      useSign: c.useSign !== undefined ? c.useDecimal : false,
      extraLabelRight: c.extraLabelRight,
      extraLabelSub: c.extraLabelSub,
      placeholder: c.placeholder,
    });

    // Ritorno classe di stile e dati
    return { css, data };
  }

  /**
   * Funzione per la generazione di una input: datepicker.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputDatepicker contenente le configurazioni per il componente.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputDatepicker(
    c: IRiscaInputDatepicker
  ): RiscaComponentConfig<RiscaFormInputDatepicker> {
    // Definisco un contenitore per la dimensione css
    let css: RiscaFormInputCss;
    // Verifico la dimensione per il css (PER ORA ESISTE SOLO UNA CONFIGURAZIONE SMALL)
    switch (c.size) {
      case 'small':
      default:
        css = this.genRiscaFormDatepickerCssSmall(
          c.showErrorFG !== undefined ? c.showErrorFG : false,
          c.showErrorFC !== undefined ? c.showErrorFC : false
        );
        break;
    }

    // Effettuo il setup per data minima e data massima dei calendari
    const calendarMinMax: RiscaDatepickerConfig = {};
    this._riscaUtilities.setupCalendarioMinMaxConfig(calendarMinMax);

    // Definisco l'oggetto per i dati del calendario
    const data: RiscaFormInputDatepicker = new RiscaFormInputDatepicker({
      label: c.label,
      labelLeft: c.labelLeft,
      labelRight: c.labelRight,
      labelBottom: c.labelBottom,
      hideLabel: c.hideLabel,
      hideLabelLeft: c.hideLabelLeft,
      hideLabelRight: c.hideLabelRight,
      hideLabelBottom: c.hideLabelBottom,
      minDate: c.minDate || calendarMinMax.minDate,
      maxDate: c.maxDate || calendarMinMax.maxDate,
      extraLabelRight: c.extraLabelRight,
      extraLabelSub: c.extraLabelSub,
      placeholder: c.placeholder,
    });

    // Ritorno classe di stile e dati
    return { css, data };
  }

  /**
   * Funzione per la generazione di una input: select.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputDatepicker contenente le configurazioni per il componente.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputSelect(
    c: IRiscaInputSelect
  ): RiscaComponentConfig<RiscaFormInputSelect> {
    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = this.selettoreDimensioneCssConfig(
      c.size || RiscaFormBuilderSize.standard,
      c.showErrorFG !== undefined ? c.showErrorFG : false,
      c.showErrorFC !== undefined ? c.showErrorFC : false
    );

    // Definisco le variabili di configurazione dati
    const labelSelect = c.defaultLabel !== undefined ? c.defaultLabel : '';
    const useLabelSelect = c.defaultLabel !== undefined;
    // Definisco l'oggetto per i dati
    const data: RiscaFormInputSelect = new RiscaFormInputSelect({
      label: c.label,
      labelLeft: c.labelLeft,
      labelRight: c.labelRight,
      labelBottom: c.labelBottom,
      hideLabel: c.hideLabel,
      hideLabelLeft: c.hideLabelLeft,
      hideLabelRight: c.hideLabelRight,
      hideLabelBottom: c.hideLabelBottom,
      multiple: c.multiple,
      allowEmpty: useLabelSelect,
      emptyLabel: labelSelect,
      emptyLabelSelected: c.emptyLabelSelected,
      ignoreDefault: c.ignoreDefault !== undefined ? c.ignoreDefault : false,
      extraLabelRight: c.extraLabelRight,
      extraLabelSub: c.extraLabelSub,
      customOption: c.customOption,
      customOptionStyle: c.customOptionStyle,
      customOptionClass: c.customOptionClass,
    });

    // Ritorno classe di stile e dati
    return { css, data };
  }

  /**
   * Funzione per la generazione di una input: typeahead.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputTypeahead contenente le configurazioni per il componente.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputTypeahead(
    c: IRiscaInputTypeahead
  ): RiscaComponentConfig<RiscaFormInputTypeahead> {
    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = this.selettoreDimensioneCssConfig(
      c.size,
      c.showErrorFG !== undefined ? c.showErrorFG : false,
      c.showErrorFC !== undefined ? c.showErrorFC : false
    );

    // Definisco l'oggetto per i dati
    const data: RiscaFormInputTypeahead = new RiscaFormInputTypeahead({
      label: c.label || 'standard',
      labelLeft: c.labelLeft,
      labelRight: c.labelRight,
      labelBottom: c.labelBottom,
      hideLabel: c.hideLabel,
      hideLabelLeft: c.hideLabelLeft,
      hideLabelRight: c.hideLabelRight,
      hideLabelBottom: c.hideLabelBottom,
      maxLength: c.maxLength || 20,
      searchOnLength: c.searchOnLength || 3,
      typeaheadSearch: c.typeaheadSearch,
      typeaheadMap: c.typeaheadMap,
      debounceTime: c.debounceTime || 300,
      onlyNumber: c.onlyNumber !== undefined ? c.onlyNumber : false,
      useDecimal: c.useDecimal !== undefined ? c.useDecimal : false,
      useSign: c.useSign !== undefined ? c.useSign : false,
      extraLabelRight: c.extraLabelRight,
      extraLabelSub: c.extraLabelSub,
      placeholder: c.placeholder,
    });

    // Ritorno classe di stile e dati
    return { css, data };
  }

  /**
   * Funzione per la generazione di una input: email.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputEmail contenente le configurazioni per il componente.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputEmail(
    c: IRiscaInputEmail
  ): RiscaComponentConfig<RiscaFormInputEmail> {
    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = this.selettoreDimensioneCssConfig(
      c.size || RiscaFormBuilderSize.standard,
      c.showErrorFG !== undefined ? c.showErrorFG : false,
      c.showErrorFC !== undefined ? c.showErrorFC : false
    );
    // Definisco l'oggetto per i dati
    const data: RiscaFormInputEmail = new RiscaFormInputEmail({
      label: c.label,
      labelLeft: c.labelLeft,
      labelRight: c.labelRight,
      labelBottom: c.labelBottom,
      hideLabel: c.hideLabel,
      hideLabelLeft: c.hideLabelLeft,
      hideLabelRight: c.hideLabelRight,
      hideLabelBottom: c.hideLabelBottom,
      maxLength: c.maxLength || 40,
      extraLabelRight: c.extraLabelRight,
      extraLabelSub: c.extraLabelSub,
      placeholder: c.placeholder,
    });

    // Ritorno classe di stile e dati
    return { css, data };
  }

  /**
   * Funzione per la generazione di una input: checkbox.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputCheckbox contenente le configurazioni per il componente.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputCheckbox(
    c: IRiscaInputCheckbox
  ): RiscaComponentConfig<RiscaFormInputCheckbox> {
    // Verifico l'orientamento
    if (!c.orientation) {
      c.orientation = RCOrientamento.orizzontale;
    }
    // Definisco le classi di stile in base all'ordientamento
    let customLabel = '';
    let customInput = '';
    let customFormCheck = '';
    if (c.orientation === RCOrientamento.orizzontale) {
      customLabel = this.RISCA_INPUT_LABEL_CHECK_H;
    } else if (c.orientation === RCOrientamento.verticale) {
      customLabel = this.RISCA_INPUT_LABEL_CHECK_V;
      customInput = this.RISCA_INPUT_CHECK_V;
      customFormCheck = 'r-p--0';
    }

    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = new RiscaFormInputCss({
      customLabel,
      customInput,
      customFormCheck,
      showErrorFG: c.showErrorFG !== undefined ? c.showErrorFG : false,
      showErrorFC: c.showErrorFC !== undefined ? c.showErrorFC : false,
    });
    // Definisco l'oggetto per i dati
    const data: RiscaFormInputCheckbox = new RiscaFormInputCheckbox({
      orientation: c.orientation,
    });
    // Inizializzo la proprietà di source
    const source: IRiscaCheckboxData[] = [];

    // Ritorno classe di stile e dati
    return { css, data, source };
  }

  /**
   * Funzione per la generazione di una input: radio.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputRadio contenente le configurazioni per il componente.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputRadio(
    c: IRiscaInputRadio
  ): RiscaComponentConfig<RiscaFormInputRadio> {
    // Verifico l'orientamento
    if (!c.orientation) {
      c.orientation = RCOrientamento.orizzontale;
    }
    // Definisco le classi di stile in base all'ordientamento
    let customLabel = '';
    let customInput = '';
    let customFormCheck = '';
    if (c.orientation === RCOrientamento.orizzontale) {
      customLabel = this.RISCA_INPUT_LABEL_RADIO_H;
    } else if (c.orientation === RCOrientamento.verticale) {
      customLabel = this.RISCA_INPUT_LABEL_RADIO_V;
      customInput = this.RISCA_INPUT_RADIO_V;
      customFormCheck = 'r-p--0';
    }

    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = new RiscaFormInputCss({
      customLabel,
      customInput,
      customFormCheck,
      showErrorFG: c.showErrorFG !== undefined ? c.showErrorFG : false,
      showErrorFC: c.showErrorFC !== undefined ? c.showErrorFC : false,
    });
    // Definisco l'oggetto per i dati
    const data: RiscaFormInputRadio = new RiscaFormInputRadio({
      orientation: c.orientation,
    });
    // Inizializzo la proprietà di source
    const source: IRiscaRadioData[] = [];

    // Ritorno classe di stile e dati
    return { css, data, source };
  }

  /**
   * Funzione per la generazione di una input: text.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputText contenente le configurazioni per il componente.
   * @param heigth literla 'small' | 'standard' che definisce la classe di stile per definire l'altezza della textarea.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputTextarea(
    c: IRiscaInputTextarea,
    heigth: 'small' | 'standard' = 'standard'
  ): RiscaComponentConfig<RiscaFormInputText> {
    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = this.selettoreDimensioneCssConfig(
      c.size || RiscaFormBuilderSize.full,
      c.showErrorFG !== undefined ? c.showErrorFG : false,
      c.showErrorFC !== undefined ? c.showErrorFC : false
    );
    // Verifico il parametro heigth, se esiste aggiungo alla stringa della classe di stile dell'input, quella per l'altezza
    if (heigth === 'standard') {
      css.customInput = `${css.customInput} ${this.RISCA_INPUT_TEXTAREA_STANDARD}`;
    }

    // Definisco l'oggetto per i dati
    const data: RiscaFormInputText = new RiscaFormInputText({
      label: c.label,
      labelLeft: c.labelLeft,
      labelRight: c.labelRight,
      labelBottom: c.labelBottom,
      hideLabel: c.hideLabel,
      hideLabelLeft: c.hideLabelLeft,
      hideLabelRight: c.hideLabelRight,
      hideLabelBottom: c.hideLabelBottom,
      maxLength: c.maxLength || 500,
      extraLabelRight: c.extraLabelRight,
      extraLabelSub: c.extraLabelSub,
      placeholder: c.placeholder,
    });

    // Ritorno classe di stile e dati
    return { css, data };
  }

  /**
   * Funzione per la generazione di una input: number.
   * Se le seguenti proprietà non vengono configurate, verrà applicato un default.
   * @param c IRiscaInputNumber contenente le configurazioni per il componente.
   * @returns RiscaComponentConfig con la configurazione input.
   */
  genInputNumber(
    c: IRiscaInputNumber
  ): RiscaComponentConfig<RiscaFormInputNumber> {
    // Definisco l'oggetto per il css
    const css: RiscaFormInputCss = this.selettoreDimensioneCssConfig(
      c.size || RiscaFormBuilderSize.standard,
      c.showErrorFG !== undefined ? c.showErrorFG : false,
      c.showErrorFC !== undefined ? c.showErrorFC : false
    );
    // Definisco l'oggetto per i dati
    const data: RiscaFormInputNumber = new RiscaFormInputNumber({
      label: c.label,
      labelLeft: c.labelLeft,
      labelRight: c.labelRight,
      labelBottom: c.labelBottom,
      hideLabel: c.hideLabel,
      hideLabelLeft: c.hideLabelLeft,
      hideLabelRight: c.hideLabelRight,
      hideLabelBottom: c.hideLabelBottom,
      maxLength: c.maxLength || 20,
      useDecimal: c.useDecimal ?? false,
      decimals: c.decimals ?? 0,
      decimaliNonSignificativi: c.decimaliNonSignificativi ?? false,
      allowThousandsSeparator: c.allowThousandsSeparator,
      useSign: c.useSign ?? false,
      min: c.min ?? null,
      max: c.max ?? null,
      step: c.step ?? null,
      extraLabelRight: c.extraLabelRight,
      extraLabelSub: c.extraLabelSub,
      placeholder: c.placeholder,
      title: c.title,
    });

    // Ritorno classe di stile e dati
    return { css, data };
  }
}
