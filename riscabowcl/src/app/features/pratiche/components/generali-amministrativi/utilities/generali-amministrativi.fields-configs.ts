import * as moment from 'moment';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { CommonConsts } from '../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import {
  DynamicObjString,
  IRiscaCheckboxData,
  RiscaComponentConfig,
  RiscaFormInputCheckbox,
  RiscaFormInputDatepicker,
  RiscaFormInputSelect,
  RiscaFormInputText,
} from '../../../../../shared/utilities';
import { RiscaFormBuilderSize } from '../../../../../shared/utilities/enums/utilities.enums';
import { GeneraliAmministrativiConsts } from '../../../consts/generali-amministrativi/generali-amministrativi.consts';

/**
 * Classe che definisce le configurazioni per i campi del form del componente generali-amministrativi.component.ts
 */
export class GAFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti per il componente. */
  GA_C = GeneraliAmministrativiConsts;

  /** Oggetto di configurazione per il campo: tipologiaPratica. */
  tipologiaPraticaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: codiceUtenza. */
  codiceUtenzaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: tipoTributoUtenza. */
  tipoTributoUtenzaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: progressivoUtenza. */
  progressivoUtenzaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: numeroPratica. */
  numeroPraticaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: statoPratica. */
  statoPraticaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: dataRinunciaRevoca. */
  dataRinunciaRevocaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: procedimento. */
  procedimentoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: prenotata. */
  prenotataConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo: statoDebInvioSpeciale. */
  statoDebitorioConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo: motivazione. */
  motivazioneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: note. */
  noteConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: dataInizioConcessione. */
  dataInizioConcessioneConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: dataFineConcessione. */
  dataFineConcessioneConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: dataInizioSospensione. */
  dataInizioSospensioneConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: dataFineSospensione. */
  dataFineSospensioneConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: tipologiaIstanza. */
  tipologiaIstanzaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: numeroIstanza. */
  numeroIstanzaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: dataIstanza. */
  dataIstanzaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: noteIstanza. */
  noteIstanzaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: tipologiaProvvedimento. */
  tipologiaProvvedimentoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: tipoTitolo. */
  tipoTitoloConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: numeroProvvedimento. */
  numeroProvvedimentoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: dataProvvedimento. */
  dataProvvedimentoConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: noteProvvedimento. */
  noteProvvedimentoConfig: RiscaComponentConfig<RiscaFormInputText>;

  /**
   * Costruttore.
   */
  constructor(
    private _riscaFormBuilder: RiscaFormBuilderService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Lancio il setup delle configurazioni
    this.setupFormInputs();
  }

  /**
   * Funzione di setup per le input del form.
   * @returns Oggetto contenente le configurazioni dei campi del form.
   */
  setupFormInputs() {
    // Funzione comune per la gestione della grafica per le select
    const customOptionStyle = (o: any) => {
      // Richiamo e ritorno la funzione dello style
      return this.customOptionStyle(o);
    };

    // Funzione di gestione per la fine data del campo
    const customOptionTP = (o: any) => {
      // Richiamo e ritorno la funzione del dato
      return this.customOptionDesTipoRiscossione(o);
    };
    this.tipologiaPraticaConfig = this._riscaFormBuilder.genInputSelect({
      label: this.GA_C.LABEL_TIPOLOGIA_PRATICA,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      customOption: customOptionTP,
      customOptionStyle,
    });

    this.codiceUtenzaConfig = this._riscaFormBuilder.genInputSelect({
      label: this.GA_C.LABEL_CODICE_UTENZA,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.tipoTributoUtenzaConfig = this._riscaFormBuilder.genInputSelect({
      label: this.GA_C.LABEL_TIPO_TRIBUTO_UTENZA,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.progressivoUtenzaConfig = this._riscaFormBuilder.genInputText({
      label: this.GA_C.LABEL_PROGRESSIVO_UTENZA,
      hideLabel: true,
      showErrorFG: true,
      showErrorFC: true,
      maxLength: 5,
      onlyNumber: true,
    });

    this.numeroPraticaConfig = this._riscaFormBuilder.genInputText({
      label: this.GA_C.LABEL_NUMERO_PRATICA,
      showErrorFC: true,
      maxLength: 40,
    });

    // Funzione di gestione per la fine data del campo
    const customOptionP = (o: any) => {
      // Richiamo e ritorno la funzione del dato
      return this.customOptionDesProcedimento(o);
    };
    this.procedimentoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.GA_C.LABEL_PROCEDIMENTO,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      customOption: customOptionP,
      customOptionStyle,
    });

    this.statoPraticaConfig = this._riscaFormBuilder.genInputSelect({
      label: this.GA_C.LABEL_STATO_PRATICA,
      showErrorFG: true,
    });

    this.dataRinunciaRevocaConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.GA_C.LABEL_DATA_RINUNCIA_REVOCA,
      showErrorFC: true,
      showErrorFG: true,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.dataRinunciaRevocaConfig.css['customForm'] = '';
    this.dataRinunciaRevocaConfig.css['customError'] = {
      'max-width': '133px',
    };

    this.statoDebitorioConfig = this._riscaFormBuilder.genInputCheckbox({});
    const statoDebitorioConfig: IRiscaCheckboxData[] = [
      {
        id: this.GA_C.STATO_DEBITORIO_INVIO_SPECIALE,
        label: this.GA_C.LABEL_STATO_DEBITORIO_INVIO_SPECIALE,
        value: false,
        check: false,
      },
    ];
    this.statoDebitorioConfig.source = statoDebitorioConfig;

    this.prenotataConfig = this._riscaFormBuilder.genInputCheckbox({});
    const prenotataSource: IRiscaCheckboxData[] = [
      {
        id: this.GA_C.LABEL_PRENOTATA,
        label: this.GA_C.LABEL_PRENOTATA,
        value: false,
        check: false,
      },
    ];
    this.prenotataConfig.source = prenotataSource;

    this.motivazioneConfig = this._riscaFormBuilder.genInputTextarea(
      {
        size: RiscaFormBuilderSize.full,
        label: this.GA_C.LABEL_MOTIVAZIONE,
        extraLabelSub: this.GA_C.LABEL_SUB_MOTIVAZIONE,
        showErrorFC: true,
        maxLength: 200,
      },
      RiscaFormBuilderSize.small
    );

    this.noteConfig = this._riscaFormBuilder.genInputTextarea({
      size: RiscaFormBuilderSize.full,
      label: this.GA_C.LABEL_NOTE,
      extraLabelSub: this.GA_C.LABEL_SUB_NOTE,
      maxLength: 500,
    });

    this.dataInizioConcessioneConfig =
      this._riscaFormBuilder.genInputDatepicker({
        label: this.GA_C.LABEL_DATA_INIZIO_CONCESSIONE,
        showErrorFG: true,
      });

    this.dataFineConcessioneConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.GA_C.LABEL_DATA_FINE_CONCESSIONE,
      showErrorFG: true,
    });

    this.dataInizioSospensioneConfig =
      this._riscaFormBuilder.genInputDatepicker({
        label: this.GA_C.LABEL_DATA_INIZIO_SOSPENSIONE,
        showErrorFG: true,
      });

    this.dataFineSospensioneConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.GA_C.LABEL_DATA_FINE_SOSPENSIONE,
      showErrorFG: true,
    });

    // Funzione di gestione per la fine data del campo
    const customOptionTI = (o: any) => {
      // Richiamo e ritorno la funzione del dato
      return this.customOptionDesIstanza(o);
    };
    this.tipologiaIstanzaConfig = this._riscaFormBuilder.genInputSelect({
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      customOption: customOptionTI,
      customOptionStyle,
    });

    this.numeroIstanzaConfig = this._riscaFormBuilder.genInputText({
      size: RiscaFormBuilderSize.small,
      maxLength: 20,
    });

    this.dataIstanzaConfig = this._riscaFormBuilder.genInputDatepicker({
      showErrorFC: true,
    });

    this.noteIstanzaConfig = this._riscaFormBuilder.genInputTextarea({
      size: RiscaFormBuilderSize.x3,
      label: this.GA_C.LABEL_NOTE_ISTANZA,
      extraLabelSub: this.GA_C.LABEL_SUB_NOTE_ISTANZA,
      maxLength: 500,
    });

    // Funzione di gestione per la fine data del campo
    const customOptionTPr = (o: any) => {
      // Richiamo e ritorno la funzione del dato
      return this.customOptionDesProvvedimento(o);
    };
    this.tipologiaProvvedimentoConfig = this._riscaFormBuilder.genInputSelect({
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      customOption: customOptionTPr,
      customOptionStyle,
    });

    // Funzione di gestione per la fine data del campo
    const customOptionTT = (o: any) => {
      // Richiamo e ritorno la funzione del dato
      return this.customOptionDesTipoTitolo(o);
    };
    this.tipoTitoloConfig = this._riscaFormBuilder.genInputSelect({
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      customOption: customOptionTT,
      customOptionStyle,
    });

    this.numeroProvvedimentoConfig = this._riscaFormBuilder.genInputText({
      showErrorFC: true,
      maxLength: 20,
    });

    this.dataProvvedimentoConfig = this._riscaFormBuilder.genInputDatepicker({
      showErrorFC: true,
    });
    // Setting manuali
    const ieri = moment().subtract(1, 'day');
    // Riassegno l'oggetto data tipizzandolo
    this.dataProvvedimentoConfig = this.dataProvvedimentoConfig as any;
    // Imposto come data massima la data di ieri per la data provvedimento
    this.dataProvvedimentoConfig.data.maxDate = {
      year: ieri.year(),
      month: ieri.month() + 1,
      day: ieri.date(),
    };

    this.noteProvvedimentoConfig = this._riscaFormBuilder.genInputTextarea({
      size: RiscaFormBuilderSize.x3,
      label: this.GA_C.LABEL_NOTE_PROVVEDIMENTO,
      extraLabelSub: this.GA_C.LABEL_SUB_NOTE_PROVVEDIMENTO,
      maxLength: 500,
    });
  }

  /**
   * Funzione di supporto per gestire la descrizione delle opzioni con data fine validità per: tipo-titolo.
   * @param o any con i dati dell'oggetto da rimappare.
   * @returns string con la descrizione dell'oggetto.
   */
  private customOptionDesTipoTitolo(o: any): string {
    // Recupero la proprietà per la select
    const p = this.GA_C.PROPERTY_TIPO_TITOLO;
    // Concateno le informazioni
    return this._riscaUtilities.customOptionDesDataFineVal(o, p);
  }

  /**
   * Funzione di supporto per gestire la descrizione delle opzioni con data fine validità per: tipo-riscossione.
   * @param o any con i dati dell'oggetto da rimappare.
   * @returns string con la descrizione dell'oggetto.
   */
  private customOptionDesTipoRiscossione(o: any): string {
    // Recupero la proprietà per la select
    const p = this.GA_C.PROPERTY_TIPOLOGIA_PRATICA;
    // Concateno le informazioni
    return this._riscaUtilities.customOptionDesDataFineVal(o, p);
  }

  /**
   * Funzione di supporto per gestire la descrizione delle opzioni con data fine validità per: tipo-autorizza.
   * @param o any con i dati dell'oggetto da rimappare.
   * @returns string con la descrizione dell'oggetto.
   */
  private customOptionDesProcedimento(o: any): string {
    // Recupero la proprietà per la select
    const p = this.GA_C.PROPERTY_PROCEDIMENTO;
    // Concateno le informazioni
    return this._riscaUtilities.customOptionDesDataFineVal(o, p);
  }

  /**
   * Funzione di supporto per gestire la descrizione delle opzioni con data fine validità per: tipo-istanza.
   * @param o any con i dati dell'oggetto da rimappare.
   * @returns string con la descrizione dell'oggetto.
   */
  private customOptionDesIstanza(o: any): string {
    // Recupero la proprietà per la select
    const p = this.GA_C.PROPERTY_TIPOLOGIA_ISTANZA;
    // Concateno le informazioni
    return this._riscaUtilities.customOptionDesDataFineVal(o, p);
  }

  /**
   * Funzione di supporto per gestire la descrizione delle opzioni con data fine validità per: tipo-provvedimento.
   * @param o any con i dati dell'oggetto da rimappare.
   * @returns string con la descrizione dell'oggetto.
   */
  private customOptionDesProvvedimento(o: any): string {
    // Recupero la proprietà per la select
    const p = this.GA_C.PROPERTY_TIPOLOGIA_PROVVEDIMENTO;
    // Concateno le informazioni
    return this._riscaUtilities.customOptionDesDataFineVal(o, p);
  }

  /**
   * Funzione di wrapper per la gestione della grafica della componente select per un'option con data fine validità.
   * @param o any con i dati dell'oggetto per la gestione dello stile.
   * @returns DynamicObjString con la classe di stile per data fine validità.
   */
  private customOptionStyle(o: any): DynamicObjString {
    // Richiamo la funzione dell'utility per lo stile dell'option
    return this._riscaUtilities.customOptionStyleDataFineVal(o);
  }
}
