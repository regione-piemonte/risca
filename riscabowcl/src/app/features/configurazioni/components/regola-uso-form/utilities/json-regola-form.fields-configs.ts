import {
  RiscaAppendix,
  RiscaAppendixPosition,
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputNumber,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { JsonRegolaFormConsts } from './json-regola-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class JsonRegolaFormFieldsConfig {
  /** JsonRegolaFormConsts come classe che definisce le costanti del componente. */
  JRF_C = new JsonRegolaFormConsts();

  /** Oggetto di configurazione per il campo: minimo principale. */
  minimoPrincipaleConfigs: RiscaComponentConfig<RiscaFormInputNumber>;

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
    // Il tipo canone è percentuale, definisco le configurazioni per l'appendice
    const text: string = '€';
    const position = RiscaAppendixPosition.right;
    const appendix = new RiscaAppendix({ text, position });

    this.minimoPrincipaleConfigs = riscaFormBuilder.genInputNumber({
      label: this.JRF_C.LABEL_MINIMO_PRINCIPALE,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
      useDecimal: true,
      min: 0,
      decimals: 2,
    });
    this.minimoPrincipaleConfigs.css['appendix'] = appendix;
  }
}
