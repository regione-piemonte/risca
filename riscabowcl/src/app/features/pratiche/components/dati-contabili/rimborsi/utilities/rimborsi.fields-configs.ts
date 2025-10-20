import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from 'src/app/shared/services/risca/risca-form-builder/risca-form-builder.service';
import {
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputSelect,
} from 'src/app/shared/utilities';
import { RimborsiConsts } from './rimborsi.consts';

/**
 * Interfacci di comodo per la configurazione della classe: BMFieldsConfigClass.
 */
export interface IRFieldsConfigClass {
  riscaFormBuilder: RiscaFormBuilderService;
}

/**
 * Classe che definisce le configurazioni per i campi del form del componente rimborsi-modal.component.ts
 */
export class RFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  RMB_C = RimborsiConsts;
  /** Oggetto di configurazione per il campo: anno. */
  tipoRimborsoConfig: RiscaComponentConfig<RiscaFormInputSelect>;

  /** RiscaFormBuilderService che definisce il servizio per la generazione delle input delle form. */
  private _riscaFormBuilder: RiscaFormBuilderService;

  /**
   * Costruttore.
   */
  constructor(config: IRFieldsConfigClass) {
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
    this.tipoRimborsoConfig = this._riscaFormBuilder.genInputSelect({
      size: RiscaFormBuilderSize.standard,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });
  }
}
