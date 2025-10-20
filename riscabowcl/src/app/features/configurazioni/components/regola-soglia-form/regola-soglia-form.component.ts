import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { JsonRegolaSogliaVo } from '../../../../core/commons/vo/json-regola-soglia-vo';
import { RegolaUsoVo } from '../../../../core/commons/vo/regola-uso-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormChildComponent } from '../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaErrorsMap } from '../../../../shared/utilities/classes/errors-maps';
import { RegolaSogliaFormConsts } from './utilities/regola-soglia-form.consts';
import { RegolaSogliaFormFieldsConfig } from './utilities/regola-soglia-form.fields-configs';
import { RegolaSogliaFormFormConfigs } from './utilities/regola-soglia-form.form-configs';
import { IRegolaSogliaForm } from './utilities/regola-soglia-form.interfaces';

@Component({
  selector: 'regola-soglia-form',
  templateUrl: './regola-soglia-form.component.html',
  styleUrls: ['./regola-soglia-form.component.scss'],
})
export class RegolaSogliaFormComponent
  extends RiscaFormChildComponent<IRegolaSogliaForm>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** RegolaSogliaFormConsts come classe che definisce le costanti del componente. */
  RCMF_C = new RegolaSogliaFormConsts();
  /** RiscaErrorsMap come classe che definisce la mappatura degli errori per il form. */
  EM = new RiscaErrorsMap();

  /** RegolaSogliaFormFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: RegolaSogliaFormFieldsConfig;
  /** RegolaSogliaFormFormConfigs come classe che definisce la struttura di configurazione del form. */
  formConfigs: RegolaSogliaFormFormConfigs;

  /** RegolaUsoVo che definisce i dati di configurazione per la form. */
  @Input() regolaUso: RegolaUsoVo;
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
    this.formErrors = [...this.EM.MAP_FORM_GROUP_REQUIRED];
  }

  /**
   * Funzione di setup per la configurazione dei dati per la costruzione delle input del form.
   */
  private setupFormInputs() {
    // Recupero il servizio di configurazione
    const s = this._riscaFormBuilder;
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new RegolaSogliaFormFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new RegolaSogliaFormFormConfigs(s);
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
    // Lancio la funzione di init per le configurazioni sul form
    this.initFormConfigs();
    // Lancio la funzione di init dati del form
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
    let formData: RegolaUsoVo;
    formData = this.regolaUso;
    // Recupero l'oggetto per la soglia
    let soglia: JsonRegolaSogliaVo;
    soglia = formData?.json_regola_obj?.soglia;

    // Verifico i campi
    if (soglia?.soglia != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RCMF_C.SOGLIA,
        soglia?.soglia
      );
    }

    if (soglia?.canone_minimo_soglia_inf != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RCMF_C.MINIMO_INFERIORE,
        soglia?.canone_minimo_soglia_inf
      );
    }

    if (soglia?.canone_minimo_soglia_sup != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RCMF_C.MINIMO_SUPERIORE,
        soglia?.canone_minimo_soglia_sup
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
   * @override
   */
  onFormReset() {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;

    // Verifico la modalità di gestione
    if (this.modifica) {
      // Effettuo il reset dei dati, come restore
      this.initFormData();
      // #
    } else {
      // Richiamo il reset
      super.onFormReset();
    }
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
  get sogliaFC(): AbstractControl {
    // Definisco le informazioni per il ritorno del campo
    const k: string = this.RCMF_C.SOGLIA;
    const f: FormGroup = this.mainForm;
    // Tento di ritornare la referenza del campo del form
    return f?.get(k);
  }

  /**
   * Getter per il recupero dell'abstract control omonimo.
   * @returns number con il valore del campo del form.
   */
  get soglia(): number {
    // Tento di ritornare il valore del campo
    return this.sogliaFC?.value;
  }
}
