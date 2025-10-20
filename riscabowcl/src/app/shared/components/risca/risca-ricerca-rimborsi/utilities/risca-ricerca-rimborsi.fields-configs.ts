import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import {
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputCheckbox,
  RiscaFormInputRadio,
  RiscaFormInputSelect,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaRicercaRimborsiConsts } from './risca-ricerca-rimborsi.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RiscaRicercaRimborsiFieldsConfig {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** RiscaRicercaRimborsiConsts come classe che definisce le costanti del componente. */
  RRM_C = new RiscaRicercaRimborsiConsts();

  /** Oggetto di configurazione per il campo: statoRimborsi. */
  tipoRicercaRimborsiConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: anno. */
  annoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: restituitoMittente. */
  restituitoMittenteConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo: annullato. */
  annullatoConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo: limiteInvioAccertamento. */
  limiteInvioAccertamentoConfig: RiscaComponentConfig<RiscaFormInputRadio>;

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
    this.tipoRicercaRimborsiConfig = riscaFormBuilder.genInputSelect({
      label: this.RRM_C.LABEL_TIPO_RICERCA_RIMBORSI,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      showErrorFC: true,
    });

    this.annoConfig = riscaFormBuilder.genInputSelect({
      label: this.RRM_C.LABEL_ANNO,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      size: RiscaFormBuilderSize.small,
    });
  }
}
