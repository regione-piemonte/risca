import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormChildComponent } from '../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaErrorsMap } from '../../../../shared/utilities/classes/errors-maps';
import { RegolaRangeFormConsts } from './utilities/regola-range-form.consts';
import { RegolaRangeFormFieldsConfig } from './utilities/regola-range-form.fields-configs';
import { RegolaRangeFormFormConfigs } from './utilities/regola-range-form.form-configs';
import { IRegolaRangeForm } from './utilities/regola-range-form.interfaces';

@Component({
  selector: 'regola-range-form',
  templateUrl: './regola-range-form.component.html',
  styleUrls: ['./regola-range-form.component.scss'],
})
export class RegolaRangeFormComponent
  extends RiscaFormChildComponent<IRegolaRangeForm>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** RegolaRangeFormConsts come classe che definisce le costanti del componente. */
  RRF_C = new RegolaRangeFormConsts();
  /** RiscaErrorsMap come classe che definisce la mappatura degli errori per il form. */
  EM = new RiscaErrorsMap();

  /** RegolaRangeFormFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: RegolaRangeFormFieldsConfig;
  /** RegolaRangeFormFormConfigs come classe che definisce la struttura di configurazione del form. */
  formConfigs: RegolaRangeFormFormConfigs;

  /** IRegolaRangeForm che definisce i dati di configurazione per la form. */
  @Input() regolaRangeForm: IRegolaRangeForm;
  /** boolean con un flag di disabilitazione generico per la form dati. */
  @Input() disableForm: boolean = false;

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
    this.formErrors = [
      ...this.EM.MAP_FORM_GROUP_REQUIRED,
      ...this.EM.MAP_FORM_GROUP_ONE_FIELD_REQUIRED,
      ...this.EM.MAP_RANGE_VALORE1_VALORE2,
    ];
  }

  /**
   * Funzione di setup per la configurazione dei dati per la costruzione delle input del form.
   */
  private setupFormInputs() {
    // Recupero il servizio di configurazione
    const s = this._riscaFormBuilder;
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new RegolaRangeFormFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new RegolaRangeFormFormConfigs(s);
    // Tramite la classe di configurazione, definisco la struttura per il main form
    this.mainForm = this.formConfigs.instantiateForm();

    // Verifico se devo disabilitare il form per configurazione
    if (this.disableForm) {
      // Disabilito
      this.mainForm.disable();
      // #
    }
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
    // Lancio la funzione di init per le configurazioni sul form
    this.initFormConfigs();
    // Lancio la funzione di init del form
    this.initFormData();
  }

  /**
   * Funzione di init delle configurazioni del form date in input.
   */
  private initFormConfigs() {
    // Verifico se devo disabilitare il form per configurazione
    if (this.disableForm) {
      // Disabilito
      this.mainForm.disable();
      // #
    }
  }

  /**
   * Funzione di init del form dati.
   */
  private initFormData() {
    // Recupero la configurazione in input
    let formData: IRegolaRangeForm;
    formData = this.regolaRangeForm;

    if (formData?.valore1 != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RRF_C.VALORE_1,
        formData.valore1
      );
    }

    if (formData?.valore2 != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RRF_C.VALORE_2,
        formData.valore2
      );
    }

    if (formData?.canoneMinimo != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RRF_C.CANONE_MINIMO,
        formData.canoneMinimo
      );
    }
  }

  /**
   * ####################
   * FUNZIONI PER LA FORM
   * ####################
   */

  /**
   * Funzione di reset del form e del componente.
   * @param maintainMinimoPrincipale boolean con il flag per mantenere valorizzato il campo "minimo principale".
   * @override
   */
  onFormReset(maintainMinimoPrincipale: boolean = false) {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;

    // Richiamo il reset del form
    super.onFormReset();

    // Verifico la modalità di gestione
    if (this.modifica) {
      // Effettuo il reset dei dati, come restore
      this.initFormData();
      // #
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */
}
