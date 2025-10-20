import { DatiTecniciTributiConsts } from 'src/app/features/pratiche/consts/quadri-tecnici/dati-tecnici-tributi/dati-tecnici-tributi.consts';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { CommonConsts } from '../../../../../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import {
  DynamicObjString,
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputSelect,
  RiscaFormInputText,
} from '../../../../../../../../../shared/utilities';

/**
 * Classe che definisce le configurazioni per i campi del form del componente dati tecnici tributi sd.
 */
export class DTTSDAFieldsConfigClass {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTT_C = new DatiTecniciTributiConsts();

  /** Costante di comodo per le classi di stile specifiche per le input. */
  private INPUT_STANDARD =
    'risca-input-content-standard gad-input-content-standard';

  /** Oggetto di configurazione per il campo: annualità. */
  annoConfig: RiscaComponentConfig<RiscaFormInputSelect>;

  /** Oggetto di configurazione per il campo: usoDiLegge. */
  usoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: comune. */
  popolazioneConfig: RiscaComponentConfig<RiscaFormInputText>;

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
    /** Oggetto di configurazione per il campo: annualità. */
    this.annoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.DTT_C.LABEL_ANNO,
      showErrorFC: true,
      size: RiscaFormBuilderSize.small,
    });

    // Definisco una funzione per customizzare la descrizione dell'option delle select
    const customOptionStyle = (o: any) => {
      return this.customOptionStyle(o);
    };
    // Definisco una funzione per customizzare la descrizione dell'option delle select
    const customOptionDesUso = (o: any) => {
      return this.customOptionDesUso(o);
    };

    /** Oggetto di configurazione per il campo: uso. */
    this.usoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.DTT_C.LABEL_USO,
      defaultLabel: this.C_C.SELEZIONA,
      showErrorFC: true,
      emptyLabelSelected: true,
      customOption: customOptionDesUso,
      customOptionStyle: customOptionStyle,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.usoConfig.css['customForm'] = '';
    // Assegno una nuova classe di stile per l'input
    this.usoConfig.css['customInput'] = this.INPUT_STANDARD;

    /** Oggetto di configurazione per il campo: popolazione. */
    this.popolazioneConfig = this._riscaFormBuilder.genInputText({
      label: this.DTT_C.LABEL_POPOLAZIONE_RIEPILOGO,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.popolazioneConfig.css['customForm'] = '';
    // Assegno una nuova classe di stile per l'input
    this.popolazioneConfig.css['customInput'] = this.INPUT_STANDARD;
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
    const p = this.DTT_C.PROPERTY_USO_DI_LEGGE;
    // Concateno le informazioni
    return this._riscaUtilities.customOptionDesDataFineVal(o, p);
  }
}
