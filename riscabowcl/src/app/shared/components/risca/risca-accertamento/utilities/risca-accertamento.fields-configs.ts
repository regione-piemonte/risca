import { AccertamentiConsts } from '../../../../../features/pratiche/components/dati-contabili/accertamenti/utilities/accertamenti.consts';
import { CommonConsts } from '../../../../consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../services/risca/risca-form-builder/risca-form-builder.service';
import {
  IRiscaCheckboxData,
  RiscaComponentConfig,
  RiscaFormInputDatepicker,
  RiscaFormInputSelect,
  RiscaFormInputText,
} from '../../../../utilities';
import { RiscaFormBuilderSize } from '../../../../utilities/enums/utilities.enums';
import { RiscaFormInputCheckbox } from './../../../../utilities/classes/utilities.classes';

/**
 * Classe che definisce le configurazioni per i campi del form del componente generali-amministrativi.component.ts
 */
export class AccertamentoFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  ACC_C = AccertamentiConsts;

  /** Oggetto di configurazione per il campo: num protocollo. */
  numProtocolloConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: data protocollo. */
  dataProtocolloConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: data scadenza. */
  dataScadenzaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: data notifica. */
  dataNotificaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: id tipo accertamento. */
  idTipoAccertamentoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: flag restituito. */
  flgRestituitoConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo: flag annullato. */
  flgAnnullatoConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo: note. */
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
    // Genero le configurazioni
    this.numProtocolloConfig = this._riscaFormBuilder.genInputText({
      label: this.ACC_C.LABEL_NUM_PROTOCOLLO,
      size: RiscaFormBuilderSize.small,
      maxLength: 10,
      showErrorFC: true,
    });

    this.dataProtocolloConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.ACC_C.LABEL_DATA_PROTOCOLLO,
      size: RiscaFormBuilderSize.small,
      showErrorFC: true,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.dataProtocolloConfig.css['customForm'] = '';
    this.dataProtocolloConfig.css['customError'] = {
      'max-width': '133px',
    };

    this.dataScadenzaConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.ACC_C.LABEL_DATA_SCADENZA,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.dataScadenzaConfig.css['customForm'] = '';
    this.dataScadenzaConfig.css['customError'] = {
      'max-width': '133px',
    };

    this.dataNotificaConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.ACC_C.LABEL_DATA_NOTIFICA,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.dataNotificaConfig.css['customForm'] = '';
    this.dataNotificaConfig.css['customError'] = {
      'max-width': '133px',
    };

    this.idTipoAccertamentoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.ACC_C.LABEL_TIPO_ACCERTAMENTO,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.flgRestituitoConfig = this._riscaFormBuilder.genInputCheckbox({});
    const flgRestituitoConfig: IRiscaCheckboxData[] = [
      {
        id: this.ACC_C.FLG_RESTITUITO,
        label: this.ACC_C.LABEL_FLG_RESTITUITO,
        value: false,
        check: false,
      },
    ];
    this.flgRestituitoConfig.source = flgRestituitoConfig;

    this.flgAnnullatoConfig = this._riscaFormBuilder.genInputCheckbox({});
    const annullatoSource: IRiscaCheckboxData[] = [
      {
        id: this.ACC_C.FLG_ANNULLATO,
        label: this.ACC_C.LABEL_FLG_ANNULLATO,
        value: false,
        check: false,
      },
    ];
    this.flgAnnullatoConfig.source = annullatoSource;

    this.noteConfig = this._riscaFormBuilder.genInputTextarea({
      label: this.ACC_C.LABEL_NOTE,
      size: RiscaFormBuilderSize.full,
      maxLength: 200,
    });
  }
}
