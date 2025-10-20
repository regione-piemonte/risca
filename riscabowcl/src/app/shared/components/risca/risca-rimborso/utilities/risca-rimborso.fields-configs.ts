import { RimborsiConsts } from 'src/app/features/pratiche/components/dati-contabili/rimborsi/utilities/rimborsi.consts';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { CommonConsts } from '../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../services/risca/risca-form-builder/risca-form-builder.service';
import {
  RiscaComponentConfig,
  RiscaFormInputDatepicker,
  RiscaFormInputNumber,
  RiscaFormInputSelect,
  RiscaFormInputText,
} from '../../../../../shared/utilities';
import { RiscaFormBuilderSize } from '../../../../../shared/utilities/enums/utilities.enums';

/**
 * Classe che definisce le configurazioni per i campi del form del componente generali-amministrativi.component.ts
 */
export class RimborsoFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  RMB_C = RimborsiConsts;
  /** Costante di comodo per le classi di stile specifiche per le input. */
  private INPUT_STANDARD =
    'risca-input-content-standard r-input-disabled';

  /** Oggetto di configurazione per il campo: codice fiscale. */
  codiceFiscaleConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: ragione sociale. */
  ragioneSocialeConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: attività. */
  attivitaTipoRimborsoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: importo eccedente. */
  importoEccedenteConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: numero provvedimento. */
  numeroProvvedimentoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: data provvedimento. */
  dataProvvedimentoConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: causale rimborso. */
  causaleRimborsoConfig: RiscaComponentConfig<RiscaFormInputText>;

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
    // Genero il setup per i campi
    this.codiceFiscaleConfig = this._riscaFormBuilder.genInputText({
      label: this.RMB_C.LABEL_CODICE_FISCALE,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
    });
    // Assegno una nuova classe di stile per l'input
    this.codiceFiscaleConfig.css['customInput'] = this.INPUT_STANDARD;

    this.ragioneSocialeConfig = this._riscaFormBuilder.genInputText({
      label: this.RMB_C.LABEL_RAGIONE_SOCIALE,
      size: RiscaFormBuilderSize.standard,
    });

    this.importoEccedenteConfig = this._riscaFormBuilder.genInputNumber({
      size: RiscaFormBuilderSize.small,
      label: this.RMB_C.LABEL_IMPORTO_ECCEDENTE,
      showErrorFC: true,
      useDecimal: true,
      maxLength: 10,
    });

    this.attivitaTipoRimborsoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RMB_C.LABEL_ATTIVITA,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.numeroProvvedimentoConfig = this._riscaFormBuilder.genInputText({
      label: this.RMB_C.LABEL_NUMERO_PROVVEDIMENTO,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
      maxLength: 10,
    });

    this.causaleRimborsoConfig = this._riscaFormBuilder.genInputText({
      label: this.RMB_C.LABEL_CAUSALE_RIMBORSO,
      size: RiscaFormBuilderSize.full,
      showErrorFC: true,
      maxLength: 200,
    });

    this.dataProvvedimentoConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.RMB_C.LABEL_DATA_PROVVEDIMENTO,
      showErrorFC: true,
    });
    this.dataProvvedimentoConfig.css['customForm'] = 'overflow: visible';
  }
}
