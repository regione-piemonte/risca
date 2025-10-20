import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import {
  RiscaComponentConfig,
  RiscaFormInputSelect,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RicercaRimborsiConsts } from './ricerca-rimborsi.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RicercaRimborsiFieldsConfig {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** RicercaRimborsiConsts come classe che definisce le costanti del componente. */
  RM_C = new RicercaRimborsiConsts();

  /** Oggetto di configurazione per il campo: attivitaRimborsiSD. */
  attivitaRimborsoConfig: RiscaComponentConfig<RiscaFormInputSelect>;

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
    this.attivitaRimborsoConfig = riscaFormBuilder.genInputSelect({
      label: this.RM_C.LABEL_ATTIVITA_RIMBORSO_SD,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      showErrorFC: true,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.attivitaRimborsoConfig.css['customForm'] = '';
  }
}
