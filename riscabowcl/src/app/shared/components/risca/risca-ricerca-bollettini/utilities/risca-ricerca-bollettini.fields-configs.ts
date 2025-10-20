import { FormGroup } from '@angular/forms';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { RRBollettiniPagamenti } from 'src/app/shared/consts/risca/risca-ricerca-bollettini.consts';
import { RiscaComponentConfig } from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../services/risca/risca-form-builder/risca-form-builder.service';
import {
  RiscaFormInputDatepicker,
  RiscaFormInputSelect,
} from '../../../../utilities/classes/utilities.classes';

/**
 * Classe che definisce le configurazioni per i campi del form del componente home.component.ts
 */
export class RiscaRicercaBollettiniFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente i valori costanti per il componente. */
  RRB_C = RRBollettiniPagamenti;
  /** Oggetto costante, che definisce la grafica da utilizzare per le label a sinistra: "da" e "a". */
  LEFT_LABEL_CSS = { width: '20px' };
  /** Oggetto costante, che definisce la grafica da utilizzare per le componenti che includono label a sinistra e datepicker. */
  DATEPICKER_CSS = { width: '168px' };

  /** FormGroup per la ricerca di una pratica. */
  ricercaBollettiniForm: FormGroup;

  /** Oggetto di configurazione per il campo: codiceUtenza. */
  tipoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: numeroAvvisoPagamento. */
  statoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: numeroPratica. */
  dataElaborazioneInizioConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: codiceFiscale. */
  dataElaborazioneFineConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;

  /**
   * Costruttore.
   */
  constructor(_riscaFormBuilder: RiscaFormBuilderService) {
    // Lancio il setup delle configurazioni
    this.setupFormInputs(_riscaFormBuilder);
  }

  /**
   * Funzione di setup per le input del form.
   * @param _riscaFormBuilder RiscaFormBuilderService servizio per la costruzione delle form input.
   * @returns Oggetto contenente le configurazioni dei campi del form.
   */
  setupFormInputs(_riscaFormBuilder: RiscaFormBuilderService) {
    this.tipoConfig = _riscaFormBuilder.genInputSelect({
      label: this.RRB_C.LABEL_TIPO,
      showErrorFG: true,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
    });

    this.statoConfig = _riscaFormBuilder.genInputSelect({
      label: this.RRB_C.LABEL_STATO,
      showErrorFG: true,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
    });

    this.dataElaborazioneInizioConfig = _riscaFormBuilder.genInputDatepicker({
      label: this.RRB_C.LABEL_DATA,
      showErrorFG: true,
      showErrorFC: true,
      labelLeft: this.RRB_C.LABEL_DATA_DA,
    });

    // Definisco il css specifico per la label di sinistra
    this.dataElaborazioneInizioConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataElaborazioneInizioConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.dataElaborazioneFineConfig = _riscaFormBuilder.genInputDatepicker({
      label: this.RRB_C.LABEL_DATA,
      showErrorFG: true,
      showErrorFC: true,
      labelLeft: this.RRB_C.LABEL_DATA_A,
      hideLabel: true,
    });

    // Definisco il css specifico per la label di sinistra
    this.dataElaborazioneFineConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataElaborazioneFineConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;
  }
}
