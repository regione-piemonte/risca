import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import {
  RiscaAppendix,
  RiscaAppendixPosition,
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputDatepicker,
  RiscaFormInputNumber,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { InteressiDiMoraFormConsts } from './interessi-di-mora-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class InteressiDiMoraFormFieldsConfig {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** InteressiDiMoraFormConsts come classe che definisce le costanti del componente. */
  IDMF_C = new InteressiDiMoraFormConsts();

  /** Oggetto di configurazione per il campo: percentuale. */
  percentuale: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: data inizio. */
  dataInizio: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** oggetto di configurazione per il campo data fine */
  dataFine: RiscaComponentConfig<RiscaFormInputDatepicker>;

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
    const text: string = '%';
    const position = RiscaAppendixPosition.right;
    const appendix = new RiscaAppendix({ text, position });
    this.percentuale = riscaFormBuilder.genInputNumber({
      label: this.IDMF_C.LABEL_PERCENTUALE,
      useDecimal: true,
      decimals: 4,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
    });
    this.percentuale.css['appendix'] = appendix;

    this.dataInizio = riscaFormBuilder.genInputDatepicker({
      label: this.IDMF_C.LABEL_DATA_INIZIO,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
    });
    this.dataFine = riscaFormBuilder.genInputDatepicker({
      label: this.IDMF_C.LABEL_DATA_FINE,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: false,
    });
  }
}
