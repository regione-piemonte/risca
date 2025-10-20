import { CommonConsts } from '../../../../../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import {
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputText,
} from '../../../../../../../../../shared/utilities';
import { DatiTecniciAmbienteConsts } from '../../../../../../../consts/quadri-tecnici/dati-tecnici-ambiente/dati-tecnici-ambiente.consts';

/**
 * Classe che definisce le configurazioni per i campi del form del componente generali-amministrativi.component.ts
 */
export class DTASDRiepilogoFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_C = new DatiTecniciAmbienteConsts();

  /** Costante di comodo per le classi di stile specifiche per le input. */
  private INPUT_STANDARD =
    'risca-input-content-standard gad-input-content-standard';

  /** Oggetto di configurazione per il campo: corpoIdricoCaptazione. */
  corpoIdricoCaptazioneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: comune. */
  comuneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: nomeImpiantoIdroElettrico. */
  nomeImpiantoIdroElettricoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: usiRiepilogo. */
  usiRiepilogoConfig: RiscaComponentConfig<RiscaFormInputText>;

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
    this.corpoIdricoCaptazioneConfig = this._riscaFormBuilder.genInputText({
      label: this.DTA_C.LABEL_CORPO_IDRICO_CAPTAZIONE,
      maxLength: 200,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.corpoIdricoCaptazioneConfig.css['customForm'] = '';
    // Assegno una nuova classe di stile per l'input
    this.corpoIdricoCaptazioneConfig.css['customInput'] = this.INPUT_STANDARD;

    this.comuneConfig = this._riscaFormBuilder.genInputText({
      label: this.DTA_C.LABEL_COMUNE,
      maxLength: 200,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.comuneConfig.css['customForm'] = '';
    // Assegno una nuova classe di stile per l'input
    this.comuneConfig.css['customInput'] = this.INPUT_STANDARD;

    this.nomeImpiantoIdroElettricoConfig = this._riscaFormBuilder.genInputText({
      label: this.DTA_C.LABEL_NOME_IMPIANTO_IDROELETTRICO,
      maxLength: 200,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.nomeImpiantoIdroElettricoConfig.css['customForm'] = '';
    // Assegno una nuova classe di stile per l'input
    this.nomeImpiantoIdroElettricoConfig.css['customInput'] =
      this.INPUT_STANDARD;

    this.usiRiepilogoConfig = this._riscaFormBuilder.genInputText({
      label: this.DTA_C.LABEL_USI_RIEPILOGO,
      maxLength: 999999,
      size: RiscaFormBuilderSize.full,
    });
  }
}
