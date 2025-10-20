import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import {
  RiscaComponentConfig,
  RiscaFormInputSelect,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { CanoniFormConsts } from './canoni-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class CanoniFormFieldsConfig {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** CanoniFormConsts come classe che definisce le costanti del componente. */
  CF_C = new CanoniFormConsts();

  /** Oggetto di configurazione per il campo: canone ufficiale. */
  canoneUfficiale: RiscaComponentConfig<RiscaFormInputSelect>;

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
    this.canoneUfficiale = riscaFormBuilder.genInputSelect({
      label: this.CF_C.LABEL_CANONE_UFFICIALE,
      emptyLabelSelected: false,
      showErrorFC: true,
    });
  }
}
