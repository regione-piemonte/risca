import {
  RiscaAppendix,
  RiscaAppendixPosition,
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputNumber,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RegolaRangeFormConsts } from './regola-range-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RegolaRangeFormFieldsConfig {
  /** RegolaRangeFormConsts come classe che definisce le costanti del componente. */
  RRF_C = new RegolaRangeFormConsts();

  /** Oggetto di configurazione per il campo: valore 1. */
  valore1Configs: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: valore 2. */
  valore2Configs: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: canone minimo. */
  canoneMinimoConfigs: RiscaComponentConfig<RiscaFormInputNumber>;

  /**
   * Costruttore.
   */
  constructor(riscaFormBuilder: RiscaFormBuilderService) {
    // Lancio il setup delle configurazioni
    this.setupFormInputs(riscaFormBuilder);
  }

  /**
   * Funzione di setup per le input del form.
   * @param riscaFormBuilder RiscaFormBuilderService servizio per la costruzione delle form input.
   * @returns Oggetto contenente le configurazioni dei campi del form.
   */
  setupFormInputs(riscaFormBuilder: RiscaFormBuilderService) {
    // Definisco le configurazioni per l'appendice
    const text: string = '€';
    const position = RiscaAppendixPosition.right;
    const appendix = new RiscaAppendix({ text, position });

    this.valore1Configs = riscaFormBuilder.genInputNumber({
      label: this.RRF_C.LABEL_VALORE_1,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
      showErrorFG: true,
      useDecimal: true,
      min: 0,
      decimals: 4,
      decimaliNonSignificativi: true
    });

    this.valore2Configs = riscaFormBuilder.genInputNumber({
      label: this.RRF_C.LABEL_VALORE_2,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
      showErrorFG: true,
      useDecimal: true,
      min: 0,
      decimals: 4,
      decimaliNonSignificativi: true,
    });

    this.canoneMinimoConfigs = riscaFormBuilder.genInputNumber({
      label: this.RRF_C.LABEL_CANONE_MINIMO,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
      useDecimal: true,
      min: 0,
      decimals: 2,
    });
    this.canoneMinimoConfigs.css['appendix'] = appendix;
  }
}
