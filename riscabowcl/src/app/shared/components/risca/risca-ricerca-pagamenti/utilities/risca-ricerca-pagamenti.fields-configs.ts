import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import {
  RiscaComponentConfig,
  RiscaFormInputDatepicker,
  RiscaFormInputNumber,
  RiscaFormInputSelect,
  RiscaFormInputText,
  RiscaFormBuilderSize,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaRicercaPagamentiConsts } from './risca-ricerca-pagamenti.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RiscaRicercaPagamentiFieldsConfig {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** RiscaRicercaPagamentiConsts come classe che definisce le costanti del componente. */
  RRP_C = new RiscaRicercaPagamentiConsts();

  /** Oggetto costante, che definisce la grafica da utilizzare per le label a sinistra: "da" e "a". */
  LEFT_LABEL_CSS = { width: '20px' };
  /** Oggetto costante, che definisce la grafica da utilizzare per le componenti che includono label a sinistra e datepicker. */
  DATEPICKER_CSS = { width: '168px' };

  /** Oggetto di configurazione per il campo: statoPagamento. */
  statoPagamentoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: modalitaPagamento. */
  modalitaPagamentoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: dataOperazioneValutaDa. */
  dataOperazioneValutaDaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: dataOperazioneValutaA. */
  dataOperazioneValutaAConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: quintoCampo. */
  quintoCampoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: codiceAvviso. */
  codiceAvvisoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: codiceRiferimentoOperazione. */
  codiceRiferimentoOperazioneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: importoDa. */
  importoDaConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: importoA. */
  importoAConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: soggetto. */
  soggettoConfig: RiscaComponentConfig<RiscaFormInputText>;

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
    this.statoPagamentoConfig = riscaFormBuilder.genInputSelect({
      label: this.RRP_C.LABEL_STATO_PAGAMENTO,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      showErrorFG: true,
    });

    this.modalitaPagamentoConfig = riscaFormBuilder.genInputSelect({
      label: this.RRP_C.LABEL_MODALITA_PAGAMENTO,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      showErrorFG: true,
    });

    this.dataOperazioneValutaDaConfig = riscaFormBuilder.genInputDatepicker({
      label: this.RRP_C.LABEL_DATA_OP_VALUTA,
      labelLeft: this.RRP_C.LABEL_DATA_OP_VALUTA_DA,
      showErrorFG: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.dataOperazioneValutaDaConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataOperazioneValutaDaConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.dataOperazioneValutaAConfig = riscaFormBuilder.genInputDatepicker({
      label: this.RRP_C.LABEL_DATA_OP_VALUTA,
      labelLeft: this.RRP_C.LABEL_DATA_OP_VALUTA_A,
      showErrorFG: true,
      hideLabel: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.dataOperazioneValutaAConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataOperazioneValutaAConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.quintoCampoConfig = riscaFormBuilder.genInputText({
      label: this.RRP_C.LABEL_QUINTO_CAMPO,
      showErrorFG: true,
    });

    this.codiceAvvisoConfig = riscaFormBuilder.genInputText({
      label: this.RRP_C.LABEL_CODICE_AVVISO,
      showErrorFG: true,
    });

    this.codiceRiferimentoOperazioneConfig = riscaFormBuilder.genInputText({
      label: this.RRP_C.LABEL_CODICE_RIFERIMENTO_OPERAZIONE,
      showErrorFG: true,
    });

    this.importoDaConfig = riscaFormBuilder.genInputNumber({
      size: RiscaFormBuilderSize.small,
      label: this.RRP_C.LABEL_IMPORTO,
      labelLeft: this.RRP_C.LABEL_IMPORTO_DA,
      showErrorFG: true,
      useDecimal: true,
      min: 0,
    });
    // Definisco il css specifico per la label di sinistra
    this.importoDaConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.importoDaConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.importoAConfig = riscaFormBuilder.genInputNumber({
      size: RiscaFormBuilderSize.small,
      label: this.RRP_C.LABEL_IMPORTO,
      labelLeft: this.RRP_C.LABEL_IMPORTO_A,
      showErrorFG: true,
      useDecimal: true,
      min: 0,
      hideLabel: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.importoAConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.importoAConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.soggettoConfig = riscaFormBuilder.genInputText({
      label: this.RRP_C.LABEL_SOGGETTO,
      showErrorFG: true,
      maxLength: 200,
    });
  }
}
