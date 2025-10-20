import { CommonConsts } from '../../../../consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../services/risca/risca-form-builder/risca-form-builder.service';
import {
  IRiscaCheckboxData,
  IRiscaRadioData,
  RiscaComponentConfig,
  RiscaFormInputText,
} from '../../../../utilities';
import {
  RiscaFormInputCheckbox,
  RiscaFormInputNumber,
  RiscaFormInputRadio,
} from '../../../../utilities/classes/utilities.classes';
import {
  RCOrientamento,
  RiscaFormBuilderSize,
} from '../../../../utilities/enums/utilities.enums';
import { RiscaPagamentoBollettazioneConsts } from './risca-pagamento-bollettazione.consts';
import { RicercaSDTitolare } from './risca-pagamento-bollettazione.enums';
import { TipoRicercaSDPagamento } from '../../../../../core/commons/vo/ricerca-stati-debitori-pagamento-vo';

/**
 * Classe che definisce le configurazioni per i campi del form del componente generali-amministrativi.component.ts
 */
export class RiscaPagamentoBollettazioneFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente una serie di costanti per la gestione del pagamento bollettazione. */
  RPB_C = RiscaPagamentoBollettazioneConsts;

  /** Oggetto di configurazione per il campo: soggetto versamento. */
  soggettoVersamentoConfig: RiscaComponentConfig<RiscaFormInputText>;

  /** Oggetto di configurazione per il campo: target ricerca puntuale. */
  targetRicercaStatiDebitoriConfig: RiscaComponentConfig<RiscaFormInputRadio>;
  /** Oggetto di configurazione per il campo: ricerca puntuale per nap o codice utenza o numero pratica. */
  ricercaPuntualeConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: target ricerca titolare. */
  targetRicercaTitolareConfig: RiscaComponentConfig<RiscaFormInputRadio>;
  /** Oggetto di configurazione per il campo: ricerca puntuale per nap o codice utenza o numero pratica. */
  ricercaTitolareConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: importoDa. */
  importoDaConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: importoA. */
  importoAConfig: RiscaComponentConfig<RiscaFormInputNumber>;

  /** Oggetto di configurazione per il campo: non_identificato imp_versato. */
  nonIdentificatoConfigs: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: non_di_competenza imp_versato. */
  nonDiCompetenzaConfigs: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: da_rimborsare importo_versato. */
  daRimborsareConfigs: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: rimborsato. */
  rimborsatoConfigs: RiscaComponentConfig<RiscaFormInputCheckbox>;

  /** Oggetto di configurazione per il campo: imp_da_assegnare. */
  importoDaAssegnareConfigs: RiscaComponentConfig<RiscaFormInputText>;

  /** Oggetto di configurazione per il campo: importo eccedente. */
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
    this.soggettoVersamentoConfig = this._riscaFormBuilder.genInputText({
      label: this.RPB_C.LABEL_SOGGETTO_VERSAMENTO,
      size: RiscaFormBuilderSize.x3,
      showErrorFC: true,
      maxLength: 200,
    });

    /**
     * SETUP CAMPI RICERCA
     */
    // # RICERCA PUNTUALE
    this.targetRicercaStatiDebitoriConfig =
      this._riscaFormBuilder.genInputRadio({
        orientation: RCOrientamento.orizzontale,
      });
    this.targetRicercaStatiDebitoriConfig.source = [
      {
        label: this.RPB_C.LABEL_NAP,
        value: TipoRicercaSDPagamento.nap,
      },
      {
        label: this.RPB_C.LABEL_CODICE_UTENZA,
        value: TipoRicercaSDPagamento.codiceUtenza,
      },
      {
        label: this.RPB_C.LABEL_NUMERO_PRATICA,
        value: TipoRicercaSDPagamento.numeroPratica,
      },
    ] as IRiscaRadioData[];

    this.ricercaPuntualeConfig = this._riscaFormBuilder.genInputSearch({
      size: RiscaFormBuilderSize.standard,
      hideLabel: true,
    });
    // Modifico la classe di stile per dare uno stile specifico alla input
    this.ricercaPuntualeConfig.css['customForm'] = 'risca-input-target-ricerca';

    // # RICERCA TITOLARE
    this.targetRicercaTitolareConfig = this._riscaFormBuilder.genInputRadio({
      orientation: RCOrientamento.orizzontale,
    });
    this.targetRicercaTitolareConfig.source = [
      {
        label: this.RPB_C.LABEL_PRATICA,
        value: RicercaSDTitolare.pratica,
      },
      {
        label: this.RPB_C.LABEL_STATO_DEBITORIO,
        value: RicercaSDTitolare.statoDebitorio,
      },
    ] as IRiscaRadioData[];

    this.ricercaTitolareConfig = this._riscaFormBuilder.genInputSearch({
      size: RiscaFormBuilderSize.standard,
      hideLabel: true,
    });
    // Modifico la classe di stile per dare uno stile specifico alla input
    this.ricercaTitolareConfig.css['customForm'] = 'risca-input-target-ricerca';

    this.importoDaConfig = this._riscaFormBuilder.genInputNumber({
      size: RiscaFormBuilderSize.small,
      label: this.RPB_C.LABEL_IMPORTO,
      labelLeft: this.RPB_C.LABEL_IMPORTO_DA,
      showErrorFG: true,
      useDecimal: true,
      decimals: 2,
      decimaliNonSignificativi: true,
      min: 0,
    });
    // Definisco il css specifico per la label di sinistra
    this.importoDaConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.importoDaConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.importoAConfig = this._riscaFormBuilder.genInputNumber({
      size: RiscaFormBuilderSize.small,
      label: this.RPB_C.LABEL_IMPORTO,
      labelLeft: this.RPB_C.LABEL_IMPORTO_A,
      showErrorFG: true,
      useDecimal: true,
      decimals: 2,
      decimaliNonSignificativi: true,
      min: 0,
      hideLabel: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.importoAConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.importoAConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    /**
     * SETUP CAMPI NON IDENTIFICATO
     */
    // Input
    this.nonIdentificatoConfigs = this._riscaFormBuilder.genInputNumber({
      label: this.RPB_C.LABEL_IMPORTO_NON_IDENTIFICATO,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
      useDecimal: true,
      decimals: 2,
      decimaliNonSignificativi: true,
    });

    /**
     * SETUP CAMPI NON DI COMPETENZA
     */
    // Input
    this.nonDiCompetenzaConfigs = this._riscaFormBuilder.genInputNumber({
      label: this.RPB_C.LABEL_IMPORTO_NON_DI_COMPETENZA,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
      useDecimal: true,
      decimals: 2,
      decimaliNonSignificativi: true,
    });

    /**
     * SETUP CAMPI DA RIMBORSARE
     */
    // Input
    this.daRimborsareConfigs = this._riscaFormBuilder.genInputNumber({
      label: this.RPB_C.LABEL_IMPORTO_DA_RIMBORSARE,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
      useDecimal: true,
      decimals: 2,
      decimaliNonSignificativi: true,
    });

    /**
     * SETUP CAMPO DA ASSEGNARE
     */
    this.importoDaAssegnareConfigs = this._riscaFormBuilder.genInputNumber({
      label: this.RPB_C.LABEL_IMPORTO_DA_ASSEGNARE,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
      showErrorFG: true,
      decimals: 2,
      decimaliNonSignificativi: true,
    });
    this.importoDaAssegnareConfigs.css['customForm'] = 'overflow: visible';
    this.importoDaAssegnareConfigs.css['customError'] = {
      'max-width': '170px',
      position: 'absolute',
    };

    /**
     * SETUP CAMPI RIMBORSATO
     */
    // Checkbox
    this.rimborsatoConfigs = this._riscaFormBuilder.genInputCheckbox({});
    const rimborsatoSource: IRiscaCheckboxData[] = [
      {
        id: this.RPB_C.LABEL_RIMBORSATO,
        label: this.RPB_C.LABEL_RIMBORSATO,
        value: false,
        check: false,
      },
    ];
    this.rimborsatoConfigs.source = rimborsatoSource;

    /**
     * SETUP CAMPO NOTE
     */
    this.noteConfig = this._riscaFormBuilder.genInputTextarea({
      maxLength: 500,
      label: this.RPB_C.LABEL_NOTE,
      size: RiscaFormBuilderSize.x3,
      showErrorFC: true,
    });
  }
}
