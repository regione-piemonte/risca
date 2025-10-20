import { ComuneVo } from '../../../../../../core/commons/vo/comune-vo';
import { CommonConsts } from '../../../../../../shared/consts/common-consts.consts';
import { LocationService } from '../../../../../../shared/services/location.service';
import { RiscaFormBuilderService } from '../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { typeaheadComuneFormatter } from '../../../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import {
  IRiscaCheckboxData,
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputCheckbox,
  RiscaFormInputDatepicker,
  RiscaFormInputNumber,
  RiscaFormInputSelect,
  RiscaFormInputText,
  RiscaFormInputTypeahead,
} from '../../../../../../shared/utilities';
import { FormRicercaAvanzataPraticheConsts } from '../../../../consts/ricerca-avanzata-pratiche/form-ricerca-avanzata-pratiche.consts';

/**
 * Classe che definisce le configurazioni per i campi del form del componente correlato.
 */
export class FormRicercaAvanzataPSDFieldsConfigClass {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti per il componente. */
  FRAP_C = new FormRicercaAvanzataPraticheConsts();

  /** Oggetto di configurazione per il campo del form. */
  tipoSoggettoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo del form. */
  ragioneSocialeOCognomeConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  codiceFiscaleConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  partitaIVAConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  statoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo del form. */
  provinciaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo del form. */
  indirizzoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  cittaEsteraNascitaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  comuneResidenzaConfig: RiscaComponentConfig<RiscaFormInputTypeahead>;
  /** Oggetto di configurazione per il campo del form. */
  provinciaCompetenzaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  corpoIdricoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  comuneConfig: RiscaComponentConfig<RiscaFormInputTypeahead>;
  /** Oggetto di configurazione per il campo del form. */
  nomeImpiantoIdroelettricoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  tipoTitoloConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo del form. */
  tipoProvvedimentoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo del form. */
  numeroTitoloConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo del form. */
  dataTitoloDaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  dataTitoloAConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: tipologiaPratica. */
  tipologiaPraticaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo del form. */
  statoPraticaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo del form. */
  scadenzaConcessioneDaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  scadenzaConcessioneAConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  dataRinunciaRevocaDaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  dataRinunciaRevocaAConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  annoCanoneConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo del form. */
  canoneConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo del form. */
  restituitoAlMittenteConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo del form. */
  tipoIstanzaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo del form. */
  dataIstanzaDaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo del form. */
  dataIstanzaAConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;

  /**
   * Costruttore.
   */
  constructor(
    _riscaFormBuilder: RiscaFormBuilderService,
    private _location: LocationService
  ) {
    // Lancio il setup delle configurazioni
    this.setupFormInputs(_riscaFormBuilder);
  }

  /**
   * Funzione di setup per le input del form.
   * @param riscaFormBuilder RiscaFormBuilderService servizio per la costruzione delle form input.
   * @returns Oggetto contenente le configurazioni dei campi del form.
   */
  setupFormInputs(riscaFormBuilder: RiscaFormBuilderService) {
    this.tipoSoggettoConfig = riscaFormBuilder.genInputSelect({
      label: this.FRAP_C.LABEL_TIPO_SOGGETTO,
      showErrorFG: true,
      defaultLabel: this.FRAP_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.ragioneSocialeOCognomeConfig = riscaFormBuilder.genInputText({
      label: this.FRAP_C.LABEL_RAGIONE_SOCIALE_COGNOME,
      size: RiscaFormBuilderSize.standard,
      maxLength: 250,
    });

    this.codiceFiscaleConfig = riscaFormBuilder.genInputText({
      label: this.FRAP_C.LABEL_CODICE_FISCALE,
      size: RiscaFormBuilderSize.standard,
      maxLength: 16,
      showErrorFC: true,
    });

    this.partitaIVAConfig = riscaFormBuilder.genInputText({
      label: this.FRAP_C.LABEL_PARTITA_IVA,
      size: RiscaFormBuilderSize.standard,
      maxLength: 11,
      showErrorFC: true,
    });

    this.statoConfig = riscaFormBuilder.genInputSelect({
      label: this.FRAP_C.LABEL_STATO,
      showErrorFG: true,
    });

    this.provinciaConfig = riscaFormBuilder.genInputSelect({
      label: this.FRAP_C.LABEL_PROVINCIA,
      showErrorFG: true,
      defaultLabel: this.FRAP_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.indirizzoConfig = riscaFormBuilder.genInputText({
      label: this.FRAP_C.LABEL_INDIRIZZO,
      size: RiscaFormBuilderSize.standard,
      maxLength: 100,
    });

    // Definisco la funzione di recupero dati alla digitazione
    const typeaheadSearch = (v: string) => {
      // Richiamo la funzione di scarico dei comuni
      return this._location.getComuniQry(v, false);
    };
    // Definisco la funzione di mapping dati a seguito della scelta dai consigli
    const typeaheadMap = (c: ComuneVo) => {
      // Richiamo la funzione di comodo per la formattazione
      return typeaheadComuneFormatter(c);
    };

    this.cittaEsteraNascitaConfig = riscaFormBuilder.genInputText({
      label: this.FRAP_C.LABEL_CITTA_ESTERA,
      size: RiscaFormBuilderSize.standard,
      maxLength: 100,
    });

    this.comuneResidenzaConfig = riscaFormBuilder.genInputTypeahead({
      label: this.FRAP_C.LABEL_COMUNE_RESIDENZA,
      showErrorFG: true,
      showErrorFC: true,
      typeaheadSearch,
      typeaheadMap,
      maxLength: 100,
    });

    this.provinciaCompetenzaConfig = riscaFormBuilder.genInputSelect({
      label: this.FRAP_C.LABEL_PROVINCIA_COMPETENZA,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.provinciaCompetenzaConfig.css['customForm'] = '';

    this.corpoIdricoConfig = riscaFormBuilder.genInputText({
      label: this.FRAP_C.LABEL_CORPO_IDRICO,
      size: RiscaFormBuilderSize.standard,
      maxLength: 200,
    });

    this.comuneConfig = riscaFormBuilder.genInputTypeahead({
      label: this.FRAP_C.LABEL_COMUNE,
      showErrorFG: true,
      showErrorFC: true,
      searchOnLength: this.C_C.TYPEAHEAD_COMUNE_SEARCH_ON_LENGHT,
      typeaheadSearch,
      typeaheadMap,
      maxLength: 200,
    });

    this.nomeImpiantoIdroelettricoConfig = riscaFormBuilder.genInputText({
      label: this.FRAP_C.LABEL_NOME_IMPIANTO_IDROELETTRICO,
      size: RiscaFormBuilderSize.standard,
      maxLength: 200,
    });

    this.tipoTitoloConfig = riscaFormBuilder.genInputSelect({
      label: this.FRAP_C.LABEL_TIPO_TITOLO,
      showErrorFG: true,
      defaultLabel: this.FRAP_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.tipoProvvedimentoConfig = riscaFormBuilder.genInputSelect({
      label: this.FRAP_C.LABEL_TIPO_PROVVEDIMENTO,
      showErrorFG: true,
      defaultLabel: this.FRAP_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.numeroTitoloConfig = riscaFormBuilder.genInputText({
      label: this.FRAP_C.LABEL_NUMERO_TITOLO,
      size: RiscaFormBuilderSize.small,
      maxLength: 20,
    });

    this.dataTitoloDaConfig = riscaFormBuilder.genInputDatepicker({
      label: this.FRAP_C.LABEL_DATA_TITOLO,
      labelLeft: this.FRAP_C.LABEL_DA,
      showErrorFG: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.dataTitoloDaConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataTitoloDaConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.dataTitoloAConfig = riscaFormBuilder.genInputDatepicker({
      label: this.FRAP_C.LABEL_DATA_TITOLO,
      labelLeft: this.FRAP_C.LABEL_A,
      showErrorFG: true,
      hideLabel: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.dataTitoloAConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataTitoloAConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.tipologiaPraticaConfig = riscaFormBuilder.genInputSelect({
      label: this.FRAP_C.LABEL_TIPOLOGIA_PRATICA,
      defaultLabel: this.FRAP_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.statoPraticaConfig = riscaFormBuilder.genInputSelect({
      label: this.FRAP_C.LABEL_STATO_PRATICA,
      showErrorFG: true,
      defaultLabel: this.FRAP_C.SELEZIONA,
      emptyLabelSelected: true,
      size: RiscaFormBuilderSize.small,
    });

    this.scadenzaConcessioneDaConfig = riscaFormBuilder.genInputDatepicker({
      label: this.FRAP_C.LABEL_SCADENZA_CONCESSIONE,
      labelLeft: this.FRAP_C.LABEL_DA,
      showErrorFG: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.scadenzaConcessioneDaConfig.css['customForm'] =
      this.C_C.DATEPICKER_CSS;
    this.scadenzaConcessioneDaConfig.css['labelColLeft'] =
      this.C_C.LEFT_LABEL_CSS;

    this.scadenzaConcessioneAConfig = riscaFormBuilder.genInputDatepicker({
      label: this.FRAP_C.LABEL_SCADENZA_CONCESSIONE,
      labelLeft: this.FRAP_C.LABEL_A,
      showErrorFG: true,
      hideLabel: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.scadenzaConcessioneAConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.scadenzaConcessioneAConfig.css['labelColLeft'] =
      this.C_C.LEFT_LABEL_CSS;

    this.dataRinunciaRevocaDaConfig = riscaFormBuilder.genInputDatepicker({
      label: this.FRAP_C.LABEL_DATA_RINUNCIA_REVOCA,
      labelLeft: this.FRAP_C.LABEL_DA,
      showErrorFG: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.dataRinunciaRevocaDaConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataRinunciaRevocaDaConfig.css['labelColLeft'] =
      this.C_C.LEFT_LABEL_CSS;

    this.dataRinunciaRevocaAConfig = riscaFormBuilder.genInputDatepicker({
      label: this.FRAP_C.LABEL_DATA_RINUNCIA_REVOCA,
      labelLeft: this.FRAP_C.LABEL_A,
      showErrorFG: true,
      hideLabel: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.dataRinunciaRevocaAConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataRinunciaRevocaAConfig.css['labelColLeft'] =
      this.C_C.LEFT_LABEL_CSS;

    this.annoCanoneConfig = riscaFormBuilder.genInputNumber({
      label: this.FRAP_C.LABEL_ANNO_CANONE,
      showErrorFC: true,
      showErrorFG: true,
      useDecimal: true,
      maxLength: 4,
    });

    this.canoneConfig = riscaFormBuilder.genInputNumber({
      label: this.FRAP_C.LABEL_CANONE,
      size: RiscaFormBuilderSize.small,
      maxLength: 10,
      min: 0,
      useDecimal: true,
    });

    this.restituitoAlMittenteConfig = riscaFormBuilder.genInputCheckbox({});
    const restituitoAlMittenteSource: IRiscaCheckboxData[] = [
      {
        id: this.FRAP_C.RESTITUITO_AL_MITTENTE,
        label: this.FRAP_C.LABEL_RESTITUITO_AL_MITTENTE,
        value: false,
        check: false,
      },
    ];
    this.restituitoAlMittenteConfig.source = restituitoAlMittenteSource;

    this.tipoIstanzaConfig = riscaFormBuilder.genInputSelect({
      label: this.FRAP_C.LABEL_TIPO_ISTANZA,
      showErrorFG: true,
      defaultLabel: this.FRAP_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.dataIstanzaDaConfig = riscaFormBuilder.genInputDatepicker({
      label: this.FRAP_C.LABEL_DATA_ISTANZA,
      labelLeft: this.FRAP_C.LABEL_DA,
      showErrorFG: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.dataIstanzaDaConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataIstanzaDaConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.dataIstanzaAConfig = riscaFormBuilder.genInputDatepicker({
      label: this.FRAP_C.LABEL_DATA_ISTANZA,
      labelLeft: this.FRAP_C.LABEL_A,
      showErrorFG: true,
      hideLabel: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.dataIstanzaAConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataIstanzaAConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;
  }
}
