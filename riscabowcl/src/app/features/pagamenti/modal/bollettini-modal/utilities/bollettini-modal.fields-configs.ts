import { CommonConsts } from '../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import {
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputDatepicker,
  RiscaFormInputSelect,
  RiscaFormInputText,
} from '../../../../../shared/utilities';
import { BollettiniModalConsts } from '../../../consts/bollettini-modal/bollettini-modal.consts';

/**
 * Interfacci di comodo per la configurazione della classe: BMFieldsConfigClass.
 */
export interface IBMFieldsConfigClass {
  riscaFormBuilder: RiscaFormBuilderService;
}

/**
 * Classe che definisce le configurazioni per i campi del form del componente bollettini-modal.component.ts
 */
export class BMFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente le costanti per il componente attuale. */
  BM_C = new BollettiniModalConsts();

  /** Oggetto di configurazione per il campo: tipoElaborazione. */
  tipoElaborazioneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: dataScadenzaPagamento. */
  dataScedenzaPagamentoConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: anno. */
  annoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: dataProtocollo. */
  dataProtocolloConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: numeroProtocollo. */
  numeroProtocolloConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: dirigenteProTempore. */
  dirigenteProTemporeConfig: RiscaComponentConfig<RiscaFormInputText>;

  /** RiscaFormBuilderService che definisce il servizio per la generazione delle input delle form. */
  private _riscaFormBuilder: RiscaFormBuilderService;

  /**
   * Costruttore.
   */
  constructor(config: IBMFieldsConfigClass) {
    // Estraggo le configurazioni
    const { riscaFormBuilder } = config;
    // Assegno localmente le informazioni
    this._riscaFormBuilder = riscaFormBuilder;

    // Lancio il setup delle configurazioni
    this.setupFormInputs();
  }

  /**
   * Funzione di setup per le input del form.
   * @returns Oggetto contenente le configurazioni dei campi del form.
   */
  setupFormInputs() {
    this.tipoElaborazioneConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_TIPO_ELABORABORAZIONE,
    });

    this.dataScedenzaPagamentoConfig =
      this._riscaFormBuilder.genInputDatepicker({
        label: this.BM_C.LABEL_DATA_SCADENZA_PAGAMENTO,
        showErrorFC: true,
        showErrorFG: true,
      });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.dataScedenzaPagamentoConfig.css['customForm'] = '';
    this.dataScedenzaPagamentoConfig.css['customError'] = {
      'max-width': '133px',
    };

    this.dataProtocolloConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.BM_C.LABEL_DATA_PROTOCOLLO,
      showErrorFC: true,
      showErrorFG: true,
    });

    this.annoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.BM_C.LABEL_ANNO,
      showErrorFC: true,
      showErrorFG: true,
      size: RiscaFormBuilderSize.small,
    });

    this.numeroProtocolloConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_NUMERO_PROTOCOLLO,
      showErrorFC: true,
      size: RiscaFormBuilderSize.small,
      maxLength: this.BM_C.MAX_LENGTH_NUMERO_PROTOCOLLO,
    });

    this.dirigenteProTemporeConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_DIRIGENTE_PRO_TEMPORE,
      showErrorFC: true,
      maxLength: this.BM_C.NP_LENGHT_VALORE_PARAMETRO_ELABORA,
    });
  }
}
