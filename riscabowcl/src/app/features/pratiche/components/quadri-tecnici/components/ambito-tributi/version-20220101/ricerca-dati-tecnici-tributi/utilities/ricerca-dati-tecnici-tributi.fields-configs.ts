import { DatiTecniciTributiConsts } from 'src/app/features/pratiche/consts/quadri-tecnici/dati-tecnici-tributi/dati-tecnici-tributi.consts';
import { CommonConsts } from '../../../../../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import {
  RiscaComponentConfig,
  RiscaFormInputNumber,
  RiscaFormInputSelect,
} from '../../../../../../../../../shared/utilities';

/**
 * Classe che definisce le configurazioni per i campi del form del componente generali-amministrativi.component.ts
 */
export class RDTTFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTT_C = new DatiTecniciTributiConsts();

  /** Oggetto di configurazione per il campo: percFaldaProfonda. */
  popolazioneConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: usoDiLegge. */
  usoConfig: RiscaComponentConfig<RiscaFormInputSelect>;

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
    this.popolazioneConfig = this._riscaFormBuilder.genInputNumber({
      label: this.DTT_C.LABEL_POPOLAZIONE,
      maxLength: 12,
    });

    this.usoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.DTT_C.LABEL_USO,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });
  }
}
