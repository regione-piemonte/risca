import { FormGroup } from "@angular/forms";
import { HomeConsts } from "src/app/features/home/consts/home/home.consts";
import { CommonConsts } from "src/app/shared/consts/common-consts.consts";
import { RiscaComponentConfig, RiscaFormInputText, RiscaFormInputCss, RiscaFormBuilderSize, RiscaFormInputNumber } from "src/app/shared/utilities";
import { RiscaFormBuilderService } from "../../../../services/risca/risca-form-builder/risca-form-builder.service";

/**
 * Classe che definisce le configurazioni per i campi del form del componente home.component.ts
 */
export class RiscaRicercaSempliceFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti per il componente. */
  H_C = HomeConsts;

  /** FormGroup per la ricerca di una pratica. */
  ricercaPraticaForm: FormGroup;

  /** Oggetto di configurazione per il campo: codiceUtenza. */
  codiceUtenzaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: numeroAvvisoPagamento. */
  numeroAvvisoPagamentoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: numeroPratica. */
  numeroPraticaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: codiceFiscale. */
  codiceFiscaleConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: ragioneSocialeOCognome. */
  ragioneSocialeOCognomeConfig: RiscaComponentConfig<RiscaFormInputText> = {
    css: undefined,
    data: null,
  };
  /** Oggetto di configurazione per il campo: codiceAvviso. */
  codiceAvvisoConfig: RiscaComponentConfig<RiscaFormInputText>;

  /** Oggetto di configurazione per il campo: test. */
  testConfig: RiscaComponentConfig<RiscaFormInputNumber>;

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
    this.codiceUtenzaConfig = _riscaFormBuilder.genInputText({
      label: this.H_C.LABEL_CODICE_UTENZA,
      size: RiscaFormBuilderSize.small,
      maxLength: 40,
    });

    this.numeroAvvisoPagamentoConfig = _riscaFormBuilder.genInputText({
      label: this.H_C.LABEL_NUMERO_AVVISO_PAGAMENTO,
      size: RiscaFormBuilderSize.small,
      maxLength: 20,
    });

    this.numeroPraticaConfig = _riscaFormBuilder.genInputText({
      label: this.H_C.LABEL_NUMERO_PRATICA,
      size: RiscaFormBuilderSize.small,
      maxLength: 40,
    });

    this.codiceFiscaleConfig = _riscaFormBuilder.genInputText({
      label: this.H_C.LABEL_CODICE_FISCALE,
      maxLength: 16,
      showErrorFC: true,
    });

    // Data la necessità di modificare una proprietà di un oggetto con multitipo, effettuo l'assegnazione manuale
    const rSOCConfig = _riscaFormBuilder.genInputText({
      label: this.H_C.LABEL_RAGIONE_SOCIALE_COGNOME,
      maxLength: 150,
    });
    this.ragioneSocialeOCognomeConfig.css = rSOCConfig.css as RiscaFormInputCss;
    this.ragioneSocialeOCognomeConfig.data = rSOCConfig.data;
    // Sovrascrivo la dimensione con una classe css custom
    this.ragioneSocialeOCognomeConfig.css.customForm = 'risca-input-small-x3';

    this.codiceAvvisoConfig = _riscaFormBuilder.genInputText({
      label: this.H_C.LABEL_CODICE_AVVISO,
      size: RiscaFormBuilderSize.small,
      maxLength: 35,
    });

    /** Oggetto di configurazione per il campo: canone_annuo. */
    this.testConfig = _riscaFormBuilder.genInputNumber({
      label: 'LABEL_TEST',
      showErrorFC: true,
      size: RiscaFormBuilderSize.small,
      useDecimal: true,
    });
  }
}
