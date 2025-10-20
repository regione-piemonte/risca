import {
  RiscaAppendix,
  RiscaAppendixPosition,
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputNumber,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RegolaCanoneMinimoFormConsts } from './regola-canone-minimo-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RegolaCanoneMinimoFormFieldsConfig {
  /** RegolaCanoneMinimoFormConsts come classe che definisce le costanti del componente. */
  RCMF_C = new RegolaCanoneMinimoFormConsts();

  /** Oggetto di configurazione per il campo: canone minimo. */
  canoneMinimoConfig: RiscaComponentConfig<RiscaFormInputNumber>;

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
    
    this.canoneMinimoConfig = riscaFormBuilder.genInputNumber({
      label: this.RCMF_C.LABEL_CANONE_MINIMO,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
      useDecimal: true,
      min: 0,
      decimals: 2,
    });
    this.canoneMinimoConfig.css['appendix'] = appendix;
  }
}
