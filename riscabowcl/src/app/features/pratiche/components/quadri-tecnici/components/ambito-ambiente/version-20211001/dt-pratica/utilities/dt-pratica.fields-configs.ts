import { RiduzioneAumentoVo } from '../../../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { CommonConsts } from '../../../../../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaUtilitiesService } from '../../../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  DynamicObjString,
  IRiscaCheckboxData,
  RiscaComponentConfig,
  RiscaFormInputCheckbox,
  RiscaFormInputDatepicker,
  RiscaFormInputNumber,
  RiscaFormInputSelect,
  RiscaFormInputText,
} from '../../../../../../../../../shared/utilities';
import { DatiTecniciAmbienteConsts } from '../../../../../../../consts/quadri-tecnici/dati-tecnici-ambiente/dati-tecnici-ambiente.consts';

/**
 * Classe che definisce le configurazioni per i campi del form del componente generali-amministrativi.component.ts
 */
export class DTPratica20211001FieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_C = new DatiTecniciAmbienteConsts();

  /** Oggetto di configurazione per il campo: corpoIdricoCaptazione. */
  corpoIdricoCaptazioneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: comune. */
  comuneConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: nomeImpiantoIdroElettrico. */
  nomeImpiantoIdroElettricoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: portataDaAssegnare. */
  portataDaAssegnareConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: gestioneManuale. */
  gestioneManualeConfig: RiscaComponentConfig<RiscaFormInputCheckbox>;
  /** Oggetto di configurazione per il campo: usoDiLegge. */
  usoDiLeggeConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: usiDiLeggeSpecifici. */
  usiDiLeggeSpecificiConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: unitaMisuraDesc. */
  unitaMisuraDescConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: quantita. */
  quantitaConfig: RiscaComponentConfig<RiscaFormInputNumber>;
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
  /** Oggetto di configurazione per il campo: dataScadenzaEmasIso. */
  dataScadenzaEmasIsoConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;

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
    // Definisco una funzione per customizzare la descrizione dell'option delle select
    const customOptionStyle = (o: any) => {
      return this.customOptionStyle(o);
    };
    // Definisco una funzione per customizzare la descrizione dell'option delle select
    const customOptionDesRidAum = (o: any) => {
      return this.customOptionDesRidAum(o);
    };
    // Definisco una funzione per customizzare la descrizione dell'option delle select
    const customOptionDesUso = (o: any) => {
      return this.customOptionDesUso(o);
    };
    // Definisco una funzione per customizzare la descrizione dell'option delle select
    const customOptionDesUsiSpec = (o: any) => {
      return this.customOptionDesUsiSpecifici(o);
    };

    this.corpoIdricoCaptazioneConfig = this._riscaFormBuilder.genInputText({
      label: this.DTA_C.LABEL_CORPO_IDRICO_CAPTAZIONE,
      showErrorFC: true,
      maxLength: 200,
    });

    this.comuneConfig = this._riscaFormBuilder.genInputText({
      label: this.DTA_C.LABEL_COMUNE,
      showErrorFC: true,
      maxLength: 200,
    });

    this.nomeImpiantoIdroElettricoConfig =
      this._riscaFormBuilder.genInputNumber({
        label: this.DTA_C.LABEL_NOME_IMPIANTO_IDROELETTRICO,
        showErrorFC: true,
        useDecimal: true,
        min: 0,
        maxLength: 200,
      });

    this.portataDaAssegnareConfig = this._riscaFormBuilder.genInputNumber({
      label: this.DTA_C.LABEL_PORTATA_DA_ASSEGNARE,
      showErrorFC: true,
      useDecimal: true,
      maxLength: 12,
    });

    this.gestioneManualeConfig = this._riscaFormBuilder.genInputCheckbox({});
    const gestioneManualeSource: IRiscaCheckboxData[] = [
      {
        id: this.DTA_C.LABEL_GESTIONE_MANUALE,
        label: this.DTA_C.LABEL_GESTIONE_MANUALE,
        value: false,
        check: false,
      },
    ];
    this.gestioneManualeConfig.source = gestioneManualeSource;

    this.usoDiLeggeConfig = this._riscaFormBuilder.genInputSelect({
      label: this.DTA_C.LABEL_USO_DI_LEGGE,
      defaultLabel: this.C_C.SELEZIONA,
      showErrorFC: true,
      emptyLabelSelected: true,
      customOption: customOptionDesUso,
      customOptionStyle: customOptionStyle,
    });

    this.usiDiLeggeSpecificiConfig = this._riscaFormBuilder.genInputSelect({
      label: this.DTA_C.LABEL_USI_SPECIFICI,
      multiple: true,
      customOption: customOptionDesUsiSpec,
      customOptionStyle: customOptionStyle,
    });

    this.unitaMisuraDescConfig = this._riscaFormBuilder.genInputText({
      label: this.DTA_C.LABEL_UNITA_DI_MISURA_DESC,
    });

    this.quantitaConfig = this._riscaFormBuilder.genInputNumber({
      label: this.DTA_C.LABEL_QUANTITA,
      showErrorFC: true,
      showErrorFG: true,
      useDecimal: true,
      min: 0,
    });

    this.quantitaFaldaProfondaConfig = this._riscaFormBuilder.genInputNumber({
      label: this.DTA_C.LABEL_QUANTITA_FALDA_PROFONDA,
      showErrorFC: true,
      showErrorFG: true,
      useDecimal: true,
      min: 0,
    });

    this.percFaldaProfondaConfig = this._riscaFormBuilder.genInputNumber({
      label: this.DTA_C.LABEL_PERC_FALDA_PROFONDA,
      useDecimal: true,
    });

    this.quantitaFaldaSuperficialeConfig =
      this._riscaFormBuilder.genInputNumber({
        label: this.DTA_C.LABEL_QUANTITA_FALDA_SUPERFICIALE,
        useDecimal: true,
      });

    this.percFaldaSuperficialeConfig = this._riscaFormBuilder.genInputNumber({
      label: this.DTA_C.LABEL_PERC_FALDA_SUPERFICIALE,
      useDecimal: true,
    });

    this.percRiduzioneConfig = this._riscaFormBuilder.genInputSelect({
      label: this.DTA_C.LABEL_PERC_RIDUZIONE,
      multiple: true,
      customOption: customOptionDesRidAum,
      customOptionStyle: customOptionStyle,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.percRiduzioneConfig.css['customForm'] = '';
    this.percRiduzioneConfig.css['customInput'] =
      'risca-input-standard risca-input-content-standard';

    this.percAumentoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.DTA_C.LABEL_PERC_AUMENTO,
      multiple: true,
      customOption: customOptionDesRidAum,
      customOptionStyle: customOptionStyle,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.percAumentoConfig.css['customForm'] = '';
    this.percAumentoConfig.css['customInput'] =
      'risca-input-standard risca-input-content-standard';

    this.dataScadenzaEmasIsoConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.DTA_C.LABEL_DATA_SCADENZA_EMAS_ISO,
      showErrorFG: true,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.dataScadenzaEmasIsoConfig.css['customForm'] = '';
    this.dataScadenzaEmasIsoConfig.css['customError'] = {
      'max-width': '133px',
    };
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

  /**
   * Funzione di supporto per gestire la descrizione delle opzioni con data fine validità per: uso di legge.
   * @param o any con i dati dell'oggetto da rimappare.
   * @returns string con la descrizione dell'oggetto.
   */
  private customOptionDesUso(o: any): string {
    // Recupero la proprietà per la select
    const p = this.DTA_C.PROPERTY_USO_DI_LEGGE;
    // Concateno le informazioni
    return this._riscaUtilities.customOptionDesDataFineVal(o, p);
  }

  /**
   * Funzione di supporto per gestire la descrizione delle opzioni con data fine validità per: usi di legge specifici.
   * @param o any con i dati dell'oggetto da rimappare.
   * @returns string con la descrizione dell'oggetto.
   */
  private customOptionDesUsiSpecifici(o: any): string {
    // Recupero la proprietà per la select
    const p = this.DTA_C.PROPERTY_USI_DI_LEGGE_SPECIFICI;
    // Concateno le informazioni
    return this._riscaUtilities.customOptionDesDataFineVal(o, p);
  }

  /* Funzione di supporto per gestire la descrizione delle opzioni per riduzioni e aumenti usi.
   * @param r RiduzioneAumentoVo con i dati della riduzione/aumento.
   * @returns string con la descrizione della riduzione/aumento.
   */
  private customOptionDesRidAum(r: RiduzioneAumentoVo): string {
    // Definisco la label da visuallizare
    let label = `${r.sigla_riduzione_aumento} - ${r.des_riduzione_aumento}`;

    // Recupero la data di fine validità
    const dfv = r.data_fine_validita;
    // Verifico se l'oggetto ha la proprietà data_fine_validita
    if (dfv) {
      // Converto la data server in data view
      const dataView = this._riscaUtilities.convertServerDateToViewDate(dfv);
      // Concateno la label con l'extra testo
      label = this._riscaUtilities.desSelectDataFineVal(dataView, label);
    }

    // Concateno le informazioni
    return label;
  }
}
