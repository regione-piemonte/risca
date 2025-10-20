import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormChildComponent } from '../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaErrorsMap } from '../../../../shared/utilities/classes/errors-maps';
import { CreaRegolaUsoFormConsts } from './utilities/crea-regola-uso-form.consts';
import { CreaRegolaUsoFormFieldsConfig } from './utilities/crea-regola-uso-form.fields-configs';
import { CreaRegolaUsoFormFormConfigs } from './utilities/crea-regola-uso-form.form-configs';
import { ICreaRegolaUsoForm } from './utilities/crea-regola-uso-form.interfaces';

@Component({
  selector: 'crea-regola-uso-form',
  templateUrl: './crea-regola-uso-form.component.html',
  styleUrls: ['./crea-regola-uso-form.component.scss'],
})
export class CreaRegolaUsoFormComponent
  extends RiscaFormChildComponent<ICreaRegolaUsoForm>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** CreaRegolaUsoFormConsts come classe che definisce le costanti del componente. */
  CRUF_C = new CreaRegolaUsoFormConsts();
  /** RiscaErrorsMap come classe che definisce la mappatura degli errori per il form. */
  EM = new RiscaErrorsMap();

  /** CreaRegolaUsoFormFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: CreaRegolaUsoFormFieldsConfig;
  /** CreaRegolaUsoFormFormConfigs come classe che definisce la struttura di configurazione del form. */
  formConfigs: CreaRegolaUsoFormFormConfigs;

  /** ICreaRegolaUsoForm che definisce i dati di configurazione per la form. */
  @Input() creaRegolaUsoForm: ICreaRegolaUsoForm;

  /**
   * Costruttore.
   */
  constructor(
    logger: LoggerService,
    private _formBuilder: FormBuilder,
    navigationHelper: NavigationHelperService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);

    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
    // Lancio la funzione di gestione per gli errori del form
    this.setupMainFormErrors();
    // Lancio la funzione di setup delle input del form
    this.setupFormInputs();
    // Lancio la funzione di setup per la struttura del form
    this.setupFormConfigs();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [...this.EM.MAP_FORM_GROUP_REQUIRED];
  }

  /**
   * Funzione di setup per la configurazione dei dati per la costruzione delle input del form.
   */
  private setupFormInputs() {
    // Recupero il servizio di configurazione
    const s = this._riscaFormBuilder;
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new CreaRegolaUsoFormFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new CreaRegolaUsoFormFormConfigs(s);
    // Tramite la classe di configurazione, definisco la struttura per il main form
    this.mainForm = this.formConfigs.instantiateForm();
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di init per le informazioni del componente.
   */
  private initComponente() {
    // Lancio la funzione di init del form
    this.initForm();
  }

  /**
   * Funzione di init del form dati.
   */
  private initForm() {
    // Recupero la configurazione in input
    let formData: ICreaRegolaUsoForm;
    formData = this.creaRegolaUsoForm;

    // Verifico i campi
    if (formData?.annualita) {
      // Imposto il valore
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.CRUF_C.ANNUALITA,
        formData.annualita
      );
    }

    if (formData?.percentualeAggiornamento) {
      // Imposto il valore
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.CRUF_C.PERCENTUALE_AGGIORNAMENTO,
        formData.percentualeAggiornamento
      );
    }
  }

  /**
   * #############################
   * FUNZIONI DI OVERRIDE DEL FORM
   * #############################
   */

  /**
   * Funzione di reset del form e del componente.
   * @override
   */
  onFormReset() {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Per il reset del form, basta resettare la percentuale, l'annualità è fissa
    this.percentualeAggiornamentoFC?.reset();
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter per il recupero dell'abstract control omonimo.
   * @returns AbstractControl con la referenza al campo del form.
   */
  get annualitaFC(): AbstractControl {
    // Definisco le informazioni per il ritorno del campo
    const k: string = this.CRUF_C.ANNUALITA;
    const f: FormGroup = this.mainForm;
    // Tento di ritornare la referenza del campo del form
    return f?.get(k);
  }

  /**
   * Getter per il recupero dell'abstract control omonimo.
   * @returns AbstractControl con la referenza al campo del form.
   */
  get percentualeAggiornamentoFC(): AbstractControl {
    // Definisco le informazioni per il ritorno del campo
    const k: string = this.CRUF_C.PERCENTUALE_AGGIORNAMENTO;
    const f: FormGroup = this.mainForm;
    // Tento di ritornare la referenza del campo del form
    return f?.get(k);
  }

  /**
   * Getter per il recupero dell'abstract control omonimo.
   * @returns number con il valore del campo del form.
   */
  get annualita(): number {
    // Tento di ritornare il valore del campo
    return this.annualitaFC?.value;
  }

  /**
   * Getter per il recupero dell'abstract control omonimo.
   * @returns number con il valore del campo del form.
   */
  get percentualeAggiornamento(): number {
    // Tento di ritornare il valore del campo
    return this.percentualeAggiornamentoFC?.value;
  }
}
