import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from 'src/app/shared/services/risca/risca-form-builder/risca-form-builder.service';
import {
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputText,
} from 'src/app/shared/utilities';
import { IndirizzoSpedizioneFormConsts } from './risca-indirizzo-spedizione-form.consts';

/**
 * Interfacci di comodo per la configurazione della classe: ISFFieldsConfigClass.
 */
export interface IISFFieldsConfigClass {
  riscaFormBuilder: RiscaFormBuilderService;
}

/**
 * Classe che definisce le configurazioni per i campi del form del componente risca-indirizzo-spedizione-form.
 */
export class ISFFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente le costanti per il componente attuale. */
  BM_C = new IndirizzoSpedizioneFormConsts();

  /** Oggetto di configurazione per il campo: destinatario */
  destinatarioConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: presso */
  pressoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: indirizzo */
  indirizzoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: citta */
  cittaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: provincia */
  provinciaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: cap */
  capConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: frazione */
  frazioneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: nazione */
  nazioneConfig: RiscaComponentConfig<RiscaFormInputText>;

  /** RiscaFormBuilderService che definisce il servizio per la generazione delle input delle form. */
  private _riscaFormBuilder: RiscaFormBuilderService;

  /**
   * Costruttore.
   */
  constructor(config: IISFFieldsConfigClass) {
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
    this.destinatarioConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_DESTINATARIO,
      showErrorFC: true,
      size: RiscaFormBuilderSize.x2,
      maxLength: 100,
    });

    this.pressoConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_PRESSO,
      showErrorFC: true,
      size: RiscaFormBuilderSize.x2,
      maxLength: 100,
    });

    this.indirizzoConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_INDIRIZZO,
      showErrorFC: true,
      size: RiscaFormBuilderSize.standard,
      maxLength: 100,
    });

    this.cittaConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_COMUNE_CITTA,
      showErrorFC: true,
      size: RiscaFormBuilderSize.standard,
      maxLength: 90,
    });

    this.provinciaConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_PROVINCIA,
      showErrorFC: true,
      size: RiscaFormBuilderSize.small,
    });

    this.capConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_CAP,
      showErrorFC: true,
      size: RiscaFormBuilderSize.small,
      maxLength: 10,
    });

    this.frazioneConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_FRAZIONE,
      showErrorFC: true,
      size: RiscaFormBuilderSize.small,
      maxLength: 100,
    });

    this.nazioneConfig = this._riscaFormBuilder.genInputText({
      label: this.BM_C.LABEL_NAZIONE,
      showErrorFC: true,
      size: RiscaFormBuilderSize.small,
      maxLength: 60,
    });
  }
}
