import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import {
  RiscaComponentConfig,
  RiscaFormInputSelect,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RicercaMorositaConsts } from './ricerca-morosita.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RicercaMorositaFieldsConfig {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** RicercaMorositaConsts come classe che definisce le costanti del componente. */
  RM_C = new RicercaMorositaConsts();

  /** Oggetto di configurazione per il campo: attivitaMorositaSD. */
  attivitaMorositaConfig: RiscaComponentConfig<RiscaFormInputSelect>;

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
    this.attivitaMorositaConfig = riscaFormBuilder.genInputSelect({
      label: this.RM_C.LABEL_ATTIVITA_MOROSITA_SD,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      showErrorFC: true,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.attivitaMorositaConfig.css['customForm'] = '';
  }
}
