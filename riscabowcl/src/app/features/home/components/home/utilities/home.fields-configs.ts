import { CommonConsts } from '../../../../../shared/consts/common-consts.consts';
import {
  IRiscaRadioData,
  RiscaComponentConfig,
  RiscaFormInputRadio,
  RiscaFormInputText,
} from '../../../../../shared/utilities';
import {
  FlagRicercaCL,
  RCOrientamento,
  RiscaFormBuilderSize,
} from '../../../../../shared/utilities/enums/utilities.enums';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { HomeConsts } from '../../../consts/home/home.consts';

/**
 * Classe che definisce le configurazioni per i campi del form del componente home.component.ts
 */
export class HomeFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente le costanti per il componente. */
  H_C = HomeConsts;

  /** Oggetto di configurazione per il campo: target. */
  targetConfig: RiscaComponentConfig<RiscaFormInputRadio>;
  /** Oggetto di configurazione per il campo: campoLibero. */
  campoLiberoConfig: RiscaComponentConfig<RiscaFormInputText>;

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
    // Data la necessità di modificare una proprietà di un oggetto con multitipo, effettuo l'assegnazione manuale
    this.targetConfig = _riscaFormBuilder.genInputRadio({
      orientation: RCOrientamento.orizzontale,
    });
    this.targetConfig.source = [
      {
        label: this.H_C.VOCE_SOGGETTO_GRUPPO,
        value: FlagRicercaCL.soggettiEGruppi,
      },
      {
        label: this.H_C.VOCE_SOLO_SOGGETTO,
        value: FlagRicercaCL.soggetti,
      },
      {
        label: this.H_C.VOCE_SOLO_GRUPPO,
        value: FlagRicercaCL.gruppi,
      },
    ] as IRiscaRadioData[];

    this.campoLiberoConfig = _riscaFormBuilder.genInputText({
      size: RiscaFormBuilderSize.x3,
      maxLength: 250,
      showErrorFC: true,
    });
  }
}
