import { PagamentiSDConsts } from '../../../../../features/pratiche/components/dati-contabili/pagamenti-sd/utilities/pagamenti-sd.consts';
import { CommonConsts } from '../../../../consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../services/risca/risca-form-builder/risca-form-builder.service';
import {
  RiscaComponentConfig,
  RiscaFormInputDatepicker,
  RiscaFormInputNumber,
  RiscaFormInputText,
} from '../../../../utilities';
import { RiscaFormBuilderSize } from '../../../../utilities/enums/utilities.enums';

/**
 * Classe che definisce le configurazioni per i campi del form del componente generali-amministrativi.component.ts
 */
export class RiscaPagamentoManualeFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  V_C = PagamentiSDConsts;

  /** Oggetto di configurazione per il campo: data provvedimento. */
  dataVersamentoConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: importo eccedente. */
  importoVersamentoConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: codice fiscale. */
  modalitaPagamentoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: ragione sociale. */
  noteConfig: RiscaComponentConfig<RiscaFormInputText>;

  /**
   * Costruttore.
   */
  constructor(private _riscaFormBuilder: RiscaFormBuilderService) {
    // Lancio il setup delle configurazioni
    this.setupFormInputs();
  }

  /**
   * Funzione di setup per le input del form.
   * @returns Oggetto contenente le configurazioni dei campi del form.
   */
  setupFormInputs() {
    // Data versamento
    this.dataVersamentoConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.V_C.LABEL_DATA_VERSAMENTO,
      showErrorFC: true,
    });
    this.dataVersamentoConfig.css['customForm'] = 'overflow: visible';

    // Importo versamento
    this.importoVersamentoConfig = this._riscaFormBuilder.genInputNumber({
      size: RiscaFormBuilderSize.small,
      label: this.V_C.LABEL_IMPORTO_VERSAMENTO,
      showErrorFC: true,
      useDecimal: true,
      maxLength: 10,
    });
    this.importoVersamentoConfig.css['customForm'] = 'overflow: visible';

    // Genero il setup per i campi
    this.modalitaPagamentoConfig = this._riscaFormBuilder.genInputText({
      label: this.V_C.LABEL_MODALITA_PAGAMENTO,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
    });
    this.modalitaPagamentoConfig.css['customForm'] = 'overflow: visible';

    // Note
    this.noteConfig = this._riscaFormBuilder.genInputTextarea({
      maxLength: 200,
      label: this.V_C.LABEL_NOTE,
      size: RiscaFormBuilderSize.full,
      showErrorFC: true,
    });
  }
}
