import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import {
  IRiscaCheckboxData,
  IRiscaRadioData,
  RCOrientamento,
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputCheckbox,
  RiscaFormInputRadio,
  RiscaFormInputSelect,
  IRiscaChoiceData,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaRicercaMorositaConsts } from './risca-ricerca-morosita.consts';
import { RiscaRicercaMorositaEnums } from './risca-ricerca-morosita.enums';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RiscaRicercaMorositaFieldsConfig {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** RiscaRicercaMorositaConsts come classe che definisce le costanti del componente. */
  RRM_C = new RiscaRicercaMorositaConsts();

  /** Oggetto di configurazione per il campo: statoMorosita. */
  tipoRicercaMorositaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
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
    this.tipoRicercaMorositaConfig = riscaFormBuilder.genInputSelect({
      label: this.RRM_C.LABEL_TIPO_RICERCA_MOROSITA,
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

    // di default devono essere disabilitati
    this.restituitoMittenteConfig = riscaFormBuilder.genInputCheckbox({});
    const restituitoMittenteSource: IRiscaCheckboxData[] = [
      {
        id: this.RRM_C.LABEL_RESTITUITO_MITTENTE,
        label: this.RRM_C.LABEL_RESTITUITO_MITTENTE,
        value: this.RRM_C.RESTITUITO_MITTENTE,
        check: false,
      },
    ];
    this.restituitoMittenteConfig.source = restituitoMittenteSource;

    // di default devono essere disabilitati
    this.annullatoConfig = riscaFormBuilder.genInputCheckbox({});
    const annullatoSource: IRiscaCheckboxData[] = [
      {
        id: this.RRM_C.LABEL_ANNULLATO,
        label: this.RRM_C.LABEL_ANNULLATO,
        value: this.RRM_C.ANNULLATO,
        check: false,
      },
    ];
    this.annullatoConfig.source = annullatoSource;

    this.limiteInvioAccertamentoConfig = riscaFormBuilder.genInputRadio({
      label: this.RRM_C.LABEL_LIMITE_INVIO_ACCERTAMENTO,
      orientation: RCOrientamento.verticale,
    });
    // Definisco degli elementi di default per quanto riguarda i valori per i radio button
    const source: IRiscaRadioData[] = [
      {
        id: RiscaRicercaMorositaEnums.limiteAccertamentoMinEq,
        value: 0,
        label: this.RRM_C.LABEL_LIMITE_INVIO_ACCERTAMENTO_MIN_EQ,
      },
      {
        id: RiscaRicercaMorositaEnums.limiteAccertamentoMag,
        value: 0,
        label: this.RRM_C.LABEL_LIMITE_INVIO_ACCERTAMENTO_MAG,
      },
    ];
    // Assegno i dati
    this.limiteInvioAccertamentoConfig.source = source;
  }
}
