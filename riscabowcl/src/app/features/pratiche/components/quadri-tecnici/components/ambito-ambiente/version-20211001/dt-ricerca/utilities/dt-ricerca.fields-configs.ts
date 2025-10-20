import { RiduzioneAumentoVo } from '../../../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { CommonConsts } from '../../../../../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import {
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputDatepicker,
  RiscaFormInputNumber,
  RiscaFormInputSelect,
  RiscaFormInputText,
} from '../../../../../../../../../shared/utilities';
import { DTRicerca20211001Consts } from './dt-ricerca.consts';

/**
 * Classe che definisce le configurazioni per i campi del form del componente generali-amministrativi.component.ts
 */
export class DTRicerca20211001FieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  RDTA_C = new DTRicerca20211001Consts();
  /** Oggetto costante, che definisce la grafica da utilizzare per le label per data scadenza emas iso. */
  DSEI_LABEL_CSS = 'risca-input-label-standard r-w--180';
  /** Oggetto costante, che definisce la grafica da utilizzare per le label a sinistra: "da" e "a". */
  LEFT_LABEL_CSS = { width: '20px' };
  /** Oggetto costante, che definisce la grafica da utilizzare per le componenti che includono label a sinistra e datepicker. */
  DATEPICKER_CSS = { width: '168px' };

  /** Oggetto di configurazione per il campo: corpoIdricoCaptazione. */
  corpoIdricoCaptazioneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: comune. */
  comuneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: nomeImpiantoIdroElettrico. */
  nomeImpiantoIdroElettricoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: portataDaAssegnare. */
  portataDaAssegnareConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: usoDiLegge. */
  usoDiLeggeConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: usiDiLeggeSpecifici. */
  usiDiLeggeSpecificiConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: unitaMisuraDesc. */
  unitaMisuraDescConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: quantita. */
  quantitaDaConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: quantita. */
  quantitaAConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: quantitaFaldaProfonda. */
  quantitaFaldaProfondaConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: percFaldaProfonda. */
  percFaldaProfondaConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: quantitaFaldaSuperficiale. */
  quantitaFaldaSuperficialeConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: percFaldaSuperficiale. */
  percFaldaSuperficialeConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: percRiduzioni. */
  percRiduzioneConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: percAumenti. */
  percAumentoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: dataScadenzaEmasIsoDa. */
  dataScadenzaEmasIsoDaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: dataScadenzaEmasIsoA. */
  dataScadenzaEmasIsoAConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;

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
      label: this.RDTA_C.LABEL_CORPO_IDRICO_CAPTAZIONE,
      showErrorFC: true,
      maxLength: 200,
    });

    this.comuneConfig = this._riscaFormBuilder.genInputText({
      label: this.RDTA_C.LABEL_COMUNE,
      showErrorFC: true,
      maxLength: 200,
    });

    this.nomeImpiantoIdroElettricoConfig =
      this._riscaFormBuilder.genInputNumber({
        label: this.RDTA_C.LABEL_NOME_IMPIANTO_IDROELETTRICO,
        showErrorFC: true,
        useDecimal: true,
        min: 0,
        maxLength: 200,
      });

    this.portataDaAssegnareConfig = this._riscaFormBuilder.genInputText({
      label: this.RDTA_C.LABEL_PORTATA_DA_ASSEGNARE,
      showErrorFC: true,
      onlyNumber: true,
      useDecimal: true,
      maxLength: 12,
    });

    this.usoDiLeggeConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RDTA_C.LABEL_USO_DI_LEGGE,
      defaultLabel: this.C_C.SELEZIONA,
      showErrorFC: true,
      emptyLabelSelected: true,
    });

    this.usiDiLeggeSpecificiConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RDTA_C.LABEL_USI_SPECIFICI,
      multiple: true,
    });

    this.unitaMisuraDescConfig = this._riscaFormBuilder.genInputText({
      label: this.RDTA_C.LABEL_UNITA_DI_MISURA_DESC,
    });

    this.quantitaDaConfig = this._riscaFormBuilder.genInputNumber({
      label: this.RDTA_C.LABEL_QUANTITA,
      labelLeft: this.RDTA_C.LABEL_DA,
      useDecimal: true,
      size: RiscaFormBuilderSize.small,
      showErrorFG: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.quantitaDaConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.quantitaDaConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.quantitaAConfig = this._riscaFormBuilder.genInputNumber({
      label: this.RDTA_C.LABEL_QUANTITA,
      labelLeft: this.RDTA_C.LABEL_A,
      useDecimal: true,
      size: RiscaFormBuilderSize.small,
      hideLabel: true,
      showErrorFG: true,
    });
    // Definisco il css specifico per la label di sinistra
    this.quantitaAConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.quantitaAConfig.css['labelColLeft'] = this.C_C.LEFT_LABEL_CSS;

    this.quantitaFaldaProfondaConfig = this._riscaFormBuilder.genInputNumber({
      label: this.RDTA_C.LABEL_QUANTITA_FALDA_PROFONDA,
      showErrorFC: true,
      showErrorFG: true,
      useDecimal: true,
    });

    this.percFaldaProfondaConfig = this._riscaFormBuilder.genInputNumber({
      label: this.RDTA_C.LABEL_PERC_FALDA_PROFONDA,
      useDecimal: true,
    });

    this.quantitaFaldaSuperficialeConfig =
      this._riscaFormBuilder.genInputNumber({
        label: this.RDTA_C.LABEL_QUANTITA_FALDA_SUPERFICIALE,
        useDecimal: true,
      });

    this.percFaldaSuperficialeConfig = this._riscaFormBuilder.genInputNumber({
      label: this.RDTA_C.LABEL_PERC_FALDA_SUPERFICIALE,
      useDecimal: true,
    });

    // Definisco una funzione per customizzare l'option delle select
    const customOption = (r: RiduzioneAumentoVo) => {
      // Concateno le informazioni
      return `${r.sigla_riduzione_aumento} - ${r.des_riduzione_aumento}`;
    };
    this.percRiduzioneConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RDTA_C.LABEL_PERC_RIDUZIONE,
      multiple: true,
      customOption,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.percRiduzioneConfig.css['customForm'] = '';
    this.percRiduzioneConfig.css['customInput'] =
      'risca-input-standard risca-input-content-standard';

    this.percAumentoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RDTA_C.LABEL_PERC_AUMENTO,
      multiple: true,
      customOption,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.percAumentoConfig.css['customForm'] = '';
    this.percAumentoConfig.css['customInput'] =
      'risca-input-standard risca-input-content-standard';

    this.dataScadenzaEmasIsoDaConfig =
      this._riscaFormBuilder.genInputDatepicker({
        label: this.RDTA_C.LABEL_DATA_SCADENZA_EMAS_ISO,
        labelLeft: this.RDTA_C.LABEL_DA,
        showErrorFG: true,
      });
    // Definisco il css specifico per la label di sinistra
    this.dataScadenzaEmasIsoDaConfig.css['customForm'] =
      this.C_C.DATEPICKER_CSS;
    this.dataScadenzaEmasIsoDaConfig.css['customLabel'] = this.DSEI_LABEL_CSS;
    this.dataScadenzaEmasIsoDaConfig.css['labelColLeft'] =
      this.C_C.LEFT_LABEL_CSS;

    this.dataScadenzaEmasIsoAConfig = this._riscaFormBuilder.genInputDatepicker(
      {
        label: this.RDTA_C.LABEL_DATA_SCADENZA_EMAS_ISO,
        labelLeft: this.RDTA_C.LABEL_A,
        hideLabel: true,
        showErrorFG: true,
      }
    );
    // Definisco il css specifico per la label di sinistra
    this.dataScadenzaEmasIsoAConfig.css['customForm'] = this.C_C.DATEPICKER_CSS;
    this.dataScadenzaEmasIsoAConfig.css['customLabel'] = this.DSEI_LABEL_CSS;
    this.dataScadenzaEmasIsoAConfig.css['labelColLeft'] =
      this.C_C.LEFT_LABEL_CSS;
  }
}
