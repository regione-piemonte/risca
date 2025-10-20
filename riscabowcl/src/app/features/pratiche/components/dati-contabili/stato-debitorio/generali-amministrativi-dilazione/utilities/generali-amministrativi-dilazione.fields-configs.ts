import { CommonConsts } from '../../../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import {
  IRiscaCheckboxData,
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputCheckbox,
  RiscaFormInputDatepicker,
  RiscaFormInputNumber,
  RiscaFormInputSelect,
  RiscaFormInputText,
} from '../../../../../../../shared/utilities';
import { GeneraliAmministrativiDilazioneConsts } from '../../../../../consts/dati-contabili/generali-amministrativi-dilazione.consts';

/**
 * Classe che definisce le configurazioni per i campi del form del componente generali-amministrativi-dilazione.component.ts
 */
export class GADFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Costante per le informazioni del componente specifico.  */
  GAD_C = GeneraliAmministrativiDilazioneConsts;

  /** Costante di comodo per la classe di stile. */
  private INPUT_SMALL = 'risca-input-content-small gad-input-content-small';
  /** Costante di comodo per le classi di stile specifiche per le input. */
  private INPUT_STANDARD =
    'risca-input-content-standard gad-input-content-standard';

  /** Oggetto di configurazione per il campo del form. */
  codiceUtenzaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  dataUltimaModificaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  numRichiestaProtocolloConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  dataRichiestaProtocolloConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  restituitoMittenteConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo del form. */
  periodoPagamentoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  scadenzaPagamentoConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  codiceAvvisoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  statoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  invioSpecialePostelConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo del form. */
  annullatoConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo del form. */
  motivazioneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  canoneAnnualitaCorrenteConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo del form. */
  annualitaPrecedenteConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo del form. */
  canoneDovutoConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo del form. */
  addebitoAnnoSuccessivoConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo del form. */
  interessiMaturatiConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo del form. */
  speseNotificaConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo del form. */
  importoCompensazioneConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo del form. */
  dilazioneConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo del form. */
  tipoTitoloConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo del form. */
  numeroTitoloConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  dataTitoloConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  inizioConcessioneConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  scadenzaConcessioneConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  istanzaRinnovoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  noteSDConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  utenzeCompensazioneConfig: RiscaComponentConfig<RiscaFormInputText>;

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
    // Definizione delle configurazioni per i campi della form
    this.codiceUtenzaConfig = this._riscaFormBuilder.genInputTextFake({
      label: this.GAD_C.LABEL_CODICE_UTENZA,
      size: RiscaFormBuilderSize.small,
    });

    this.dataUltimaModificaConfig = this._riscaFormBuilder.genInputTextFake({
      label: this.GAD_C.LABEL_DATA_ULTIMA_MODIFICA,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.dataUltimaModificaConfig.css['customForm'] = '';

    this.numRichiestaProtocolloConfig = this._riscaFormBuilder.genInputText({
      label: this.GAD_C.LABEL_NUM_RICHIESTA_PROTOCOLLO,
      maxLength: 15,
      size: RiscaFormBuilderSize.small,
    });

    this.dataRichiestaProtocolloConfig =
      this._riscaFormBuilder.genInputDatepicker({
        label: this.GAD_C.LABEL_DATA_RICHIESTA_PROTOCOLLO,
      });

    this.restituitoMittenteConfig = this._riscaFormBuilder.genInputCheckbox({});
    const restituitoMittenteSource: IRiscaCheckboxData[] = [
      {
        id: this.GAD_C.LABEL_RESTITUITO_MITTENTE,
        label: this.GAD_C.LABEL_RESTITUITO_MITTENTE,
        value: false,
        check: false,
      },
    ];
    this.restituitoMittenteConfig.source = restituitoMittenteSource;

    this.periodoPagamentoConfig = this._riscaFormBuilder.genInputText({
      label: this.GAD_C.LABEL_PERIODO_PAGAMENTO,
      maxLength: 30,
      showErrorFC: true,
      showErrorFG: true,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.periodoPagamentoConfig.css['customForm'] = '';
    this.periodoPagamentoConfig.css['customInput'] = this.INPUT_SMALL;
    this.periodoPagamentoConfig.css['customError'] = this.INPUT_SMALL;

    this.scadenzaPagamentoConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.GAD_C.LABEL_SCADENZA_PAGAMENTO,
      showErrorFC: true,
      showErrorFG: true,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.scadenzaPagamentoConfig.css['customForm'] = '';
    this.scadenzaPagamentoConfig.css['customError'] = this.INPUT_SMALL;

    this.codiceAvvisoConfig = this._riscaFormBuilder.genInputText({
      label: this.GAD_C.LABEL_CODICE_AVVISO,
      size: RiscaFormBuilderSize.small,
    });

    this.statoConfig = this._riscaFormBuilder.genInputText({
      label: this.GAD_C.LABEL_STATO,
      size: RiscaFormBuilderSize.small,
    });

    this.invioSpecialePostelConfig = this._riscaFormBuilder.genInputCheckbox(
      {}
    );
    const invioSpecialePostelSource: IRiscaCheckboxData[] = [
      {
        id: this.GAD_C.LABEL_INVIO_SPECIALE_POSTEL,
        label: this.GAD_C.LABEL_INVIO_SPECIALE_POSTEL,
        value: false,
        check: false,
      },
    ];
    this.invioSpecialePostelConfig.source = invioSpecialePostelSource;

    this.annullatoConfig = this._riscaFormBuilder.genInputCheckbox({});
    const annullatoSource: IRiscaCheckboxData[] = [
      {
        id: this.GAD_C.LABEL_ANNULLATO,
        label: this.GAD_C.LABEL_ANNULLATO,
        value: false,
        check: false,
      },
    ];
    this.annullatoConfig.source = annullatoSource;

    this.motivazioneConfig = this._riscaFormBuilder.genInputTextarea({
      maxLength: 200,
      showErrorFC: true,
      showErrorFG: true,
    });

    this.canoneAnnualitaCorrenteConfig = this._riscaFormBuilder.genInputNumber({
      label: this.GAD_C.LABEL_CANONE_ANNUALITA_CORRENTE,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.canoneAnnualitaCorrenteConfig.css['customForm'] = '';
    this.canoneAnnualitaCorrenteConfig.css['customInput'] = this.INPUT_SMALL;
    this.canoneAnnualitaCorrenteConfig.css['customError'] = this.INPUT_SMALL;

    this.annualitaPrecedenteConfig = this._riscaFormBuilder.genInputNumber({
      label: this.GAD_C.LABEL_ANNUALITA_PRECEDENTE,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.annualitaPrecedenteConfig.css['customForm'] = '';
    this.annualitaPrecedenteConfig.css['customInput'] = this.INPUT_SMALL;
    this.annualitaPrecedenteConfig.css['customError'] = this.INPUT_SMALL;

    this.canoneDovutoConfig = this._riscaFormBuilder.genInputNumber({
      label: this.GAD_C.LABEL_CANONE_DOVUTO,
      size: RiscaFormBuilderSize.small,
    });

    this.addebitoAnnoSuccessivoConfig = this._riscaFormBuilder.genInputCheckbox(
      {}
    );
    const addebitoAnnoSuccessivoSource: IRiscaCheckboxData[] = [
      {
        id: this.GAD_C.LABEL_ADDEBITO_ANNO_SUCCESSIVO,
        label: this.GAD_C.LABEL_ADDEBITO_ANNO_SUCCESSIVO,
        value: false,
        check: false,
      },
    ];
    this.addebitoAnnoSuccessivoConfig.source = addebitoAnnoSuccessivoSource;

    this.interessiMaturatiConfig = this._riscaFormBuilder.genInputNumber({
      label: this.GAD_C.LABEL_INTERESSI_MATURATI,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.interessiMaturatiConfig.css['customForm'] = '';
    this.interessiMaturatiConfig.css['customInput'] = this.INPUT_SMALL;
    this.interessiMaturatiConfig.css['customError'] = this.INPUT_SMALL;

    this.speseNotificaConfig = this._riscaFormBuilder.genInputNumber({
      label: this.GAD_C.LABEL_SPESE_NOTIFICA,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.speseNotificaConfig.css['customForm'] = '';
    this.speseNotificaConfig.css['customInput'] = this.INPUT_SMALL;
    this.speseNotificaConfig.css['customError'] = this.INPUT_SMALL;

    this.importoCompensazioneConfig = this._riscaFormBuilder.genInputNumber({
      label: this.GAD_C.LABEL_IMPORTO_COMPENSAZIONE,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.importoCompensazioneConfig.css['customForm'] = '';
    this.importoCompensazioneConfig.css['customInput'] = this.INPUT_SMALL;
    this.importoCompensazioneConfig.css['customError'] = this.INPUT_SMALL;

    this.dilazioneConfig = this._riscaFormBuilder.genInputCheckbox({});
    const dilazioneSource: IRiscaCheckboxData[] = [
      {
        id: this.GAD_C.LABEL_DILAZIONE,
        label: this.GAD_C.LABEL_DILAZIONE,
        value: false,
        check: false,
      },
    ];
    this.dilazioneConfig.source = dilazioneSource;

    this.tipoTitoloConfig = this._riscaFormBuilder.genInputSelect({
      label: this.GAD_C.LABEL_TIPO_TITOLO,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      showErrorFC: true,
      showErrorFG: true,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.tipoTitoloConfig.css['customForm'] = '';
    // Assegno una nuova classe di stile per l'input
    this.tipoTitoloConfig.css['customInput'] = this.INPUT_STANDARD;
    this.tipoTitoloConfig.css['customError'] = this.INPUT_STANDARD;

    this.numeroTitoloConfig = this._riscaFormBuilder.genInputText({
      label: this.GAD_C.LABEL_NUMERO_TITOLO,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.numeroTitoloConfig.css['customForm'] = '';
    // Assegno una nuova classe di stile per l'input
    this.numeroTitoloConfig.css['customInput'] = this.INPUT_STANDARD;
    this.numeroTitoloConfig.css['customError'] = this.INPUT_STANDARD;

    this.dataTitoloConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.GAD_C.LABEL_DATA_TITOLO,
    });

    this.inizioConcessioneConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.GAD_C.LABEL_INIZIO_CONCESSIONE,
    });

    this.scadenzaConcessioneConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.GAD_C.LABEL_SCADENZA_CONCESSIONE,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.scadenzaConcessioneConfig.css['customForm'] = '';
    this.scadenzaConcessioneConfig.css['customError'] = this.INPUT_SMALL;

    this.istanzaRinnovoConfig = this._riscaFormBuilder.genInputText({
      label: this.GAD_C.LABEL_ISTANZA_RINNOVO,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.istanzaRinnovoConfig.css['customForm'] = '';
    // Assegno una nuova classe di stile per l'input
    this.istanzaRinnovoConfig.css['customInput'] = this.INPUT_STANDARD;
    this.istanzaRinnovoConfig.css['customError'] = this.INPUT_STANDARD;

    this.noteSDConfig = this._riscaFormBuilder.genInputTextarea({
      label: this.GAD_C.LABEL_NOTE_SD,
      maxLength: 500,
      size: RiscaFormBuilderSize.full,
      showErrorFC: true,
      showErrorFG: true,
    });

    this.utenzeCompensazioneConfig = this._riscaFormBuilder.genInputText({
      label: this.GAD_C.LABEL_UTENZE_COMPENSAZIONE,
      maxLength: 200,
      size: RiscaFormBuilderSize.full,
      showErrorFC: true,
      showErrorFG: true,
    });
  }
}
