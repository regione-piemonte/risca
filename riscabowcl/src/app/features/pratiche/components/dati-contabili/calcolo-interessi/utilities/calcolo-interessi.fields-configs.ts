import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from 'src/app/shared/services/risca/risca-form-builder/risca-form-builder.service';
import {
  RiscaComponentConfig,
  RiscaFormBuilderSize,
} from 'src/app/shared/utilities';
import {
  RiscaFormInputDatepicker,
  RiscaFormInputNumber,
  RiscaFormInputText,
} from './../../../../../../shared/utilities/classes/utilities.classes';
import { CalcoloInteressiConsts } from './calcolo-interessi.consts';

/**
 * Interfacci di comodo per la configurazione della classe: BMFieldsConfigClass.
 */
export interface ICIFieldsConfigClass {
  riscaFormBuilder: RiscaFormBuilderService;
}

/**
 * Classe che definisce le configurazioni per i campi del form del componente rimborsi-modal.component.ts
 */
export class CIFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  CI_C = CalcoloInteressiConsts;

  /** Oggetto di configurazione per il campo: canone mancante. */
  canoneMancanteConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: data scadenza pagamento. */
  dataScadenzaPagamentoConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: data versamento. */
  dataVersamentoConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: interessi dovuti. */
  interessiDovutiConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: totale da versare. */
  totaleDaVersareConfig: RiscaComponentConfig<RiscaFormInputText>;

  /** RiscaFormBuilderService che definisce il servizio per la generazione delle input delle form. */
  private _riscaFormBuilder: RiscaFormBuilderService;

  /**
   * Costruttore.
   */
  constructor(config: ICIFieldsConfigClass) {
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
    // Importo versamento
    this.canoneMancanteConfig = this._riscaFormBuilder.genInputNumber({
      size: RiscaFormBuilderSize.standard,
      label: this.CI_C.LABEL_CANONE_MANCANTE,
      showErrorFC: true,
      showErrorFG: true,
      useDecimal: true,
      decimals: 2,
      decimaliNonSignificativi: true,
      min: 0,
    });
    this.canoneMancanteConfig.css['customError'] = 'overflow: visible';

    // Data scadenza
    this.dataScadenzaPagamentoConfig =
      this._riscaFormBuilder.genInputDatepicker({
        size: RiscaFormBuilderSize.small,
        label: this.CI_C.LABEL_DATA_SCADENZA_PAGAMENTO,
        showErrorFC: true,
        showErrorFG: true,
      });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.dataScadenzaPagamentoConfig.css['customForm'] = '';
    this.dataScadenzaPagamentoConfig.css['customError'] = {
      'max-width': '133px',
    };

    // Data versamento
    this.dataVersamentoConfig = this._riscaFormBuilder.genInputDatepicker({
      size: RiscaFormBuilderSize.small,
      label: this.CI_C.LABEL_DATA_VERSAMENTO,
      showErrorFC: true,
      showErrorFG: true,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.dataVersamentoConfig.css['customForm'] = '';
    this.dataVersamentoConfig.css['customError'] = {
      'max-width': '133px',
    };

    // Importo versamento
    this.interessiDovutiConfig = this._riscaFormBuilder.genInputNumber({
      size: RiscaFormBuilderSize.small,
      label: this.CI_C.LABEL_INTERESSI_DOVUTI,
      useDecimal: true,
      maxLength: 10,
    });

    // Importo versamento
    this.totaleDaVersareConfig = this._riscaFormBuilder.genInputNumber({
      size: RiscaFormBuilderSize.small,
      label: this.CI_C.LABEL_TOTALE_DA_VERSARE,
      useDecimal: true,
      maxLength: 10,
    });
  }
}
