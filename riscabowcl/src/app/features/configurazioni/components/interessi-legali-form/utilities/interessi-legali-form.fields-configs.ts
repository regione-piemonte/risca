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
import { RiscaMessagesService } from '../../../../../shared/services/risca/risca-messages.service';
import { InteressiLegaliFormConsts } from './interessi-legali-form.consts';
import { RiscaNotifyCodes } from '../../../../../shared/utilities/enums/risca-notify-codes.enums';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class InteressiLegaliFormFieldsConfig {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** InteressiLegaliFormConsts come classe che definisce le costanti del componente. */
  ILF_C = new InteressiLegaliFormConsts();

  /** Oggetto di configurazione per il campo: percentuale. */
  percentuale: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: data inizio. */
  dataInizio: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** oggetto di configurazione per il campo data fine */
  dataFine: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: giorni. */
  giorni: RiscaComponentConfig<RiscaFormInputNumber>;

  /**
   * Costruttore.
   */
  constructor(
    riscaFormBuilder: RiscaFormBuilderService,
    private _riscaMessages: RiscaMessagesService
  ) {
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
      label: this.ILF_C.LABEL_PERCENTUALE,
      useDecimal: true,
      decimals: 4,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
    });
    this.percentuale.css['appendix'] = appendix;

    this.dataInizio = riscaFormBuilder.genInputDatepicker({
      label: this.ILF_C.LABEL_DATA_INIZIO,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
    });
    this.dataFine = riscaFormBuilder.genInputDatepicker({
      label: this.ILF_C.LABEL_DATA_FINE,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: false,
    });

    // Per la input dei giorni c'è un tooltip particolare da usare, lo recupero
    const code: string = RiscaNotifyCodes.I044;
    const giorniTooltip: string = this._riscaMessages.getMessage(code);

    this.giorni = riscaFormBuilder.genInputNumber({
      label: this.ILF_C.LABEL_GIORNI,
      title: giorniTooltip,
      useDecimal: false,
      maxLength: 3,
      allowThousandsSeparator: false,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
    });
  }
}
