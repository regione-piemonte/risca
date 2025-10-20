import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { JsonRegolaVo } from '../../../../core/commons/vo/json-regola-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormChildComponent } from '../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaErrorsMap } from '../../../../shared/utilities/classes/errors-maps';
import { JsonRegolaFormConsts } from './utilities/json-regola-form.consts';
import { JsonRegolaFormFieldsConfig } from './utilities/json-regola-form.fields-configs';
import { JsonRegolaFormFormConfigs } from './utilities/json-regola-form.form-configs';
import { IJsonRegolaForm } from './utilities/json-regola-form.interfaces';

@Component({
  selector: 'json-regola-form',
  templateUrl: './json-regola-form.component.html',
  styleUrls: ['./json-regola-form.component.scss'],
})
export class JsonRegolaFormComponent
  extends RiscaFormChildComponent<IJsonRegolaForm>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** JsonRegolaFormConsts come classe che definisce le costanti del componente. */
  JRF_C = new JsonRegolaFormConsts();
  /** RiscaErrorsMap come classe che definisce la mappatura degli errori per il form. */
  EM = new RiscaErrorsMap();

  /** JsonRegolaFormFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: JsonRegolaFormFieldsConfig;
  /** JsonRegolaFormFormConfigs come classe che definisce la struttura di configurazione del form. */
  formConfigs: JsonRegolaFormFormConfigs;

  /** JsonRegolaVo che definisce i dati di configurazione per la form. */
  @Input() jsonRegola: JsonRegolaVo;
  /** boolean con un flag di disabilitazione generico per la form dati. */
  @Input() disableForm: boolean = false;
  /** any compatibile con la direttiva NgStyle per la gestione della grafica della sezione: canone minimo, row. */
  @Input('rowMinimoPrincipale') _rowMinimoPrincipale: any;
  /** any compatibile con la direttiva NgStyle per la gestione della grafica della sezione: canone minimo, col. */
  @Input('colMinimoPrincipale') _colMinimoPrincipale: any;

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
    this.formErrors = [];
  }

  /**
   * Funzione di setup per la configurazione dei dati per la costruzione delle input del form.
   */
  private setupFormInputs() {
    // Recupero il servizio di configurazione
    const s = this._riscaFormBuilder;
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new JsonRegolaFormFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new JsonRegolaFormFormConfigs(s);
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
    let formData: JsonRegolaVo;
    formData = this.jsonRegola;

    // Verifico e inizializzo i campi del form se esiste una configurazione
    const minimoPrincipale: number = formData?.minimoPrincipale;
    if (minimoPrincipale != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.JRF_C.MINIMO_PRINCIPALE,
        minimoPrincipale
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
    // Mi salvo l'informazione del minimo principale
    const minimoPrincipale: number = this.minimoPrincipale;

    // Richiamo il reset del form
    super.onFormReset();

    // Verifico la modalità di gestione
    if (this.modifica) {
      // Effettuo il reset dei dati, come restore
      this.initFormData();
      // #
    }

    // Verifico se devo mantenere il minimo principale
    if (maintainMinimoPrincipale) {
      // Vado a impostare nuovamente il minimo principale
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.JRF_C.MINIMO_PRINCIPALE,
        minimoPrincipale
      );
      // #
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
  get minimoPrincipaleFC(): AbstractControl {
    // Definisco le informazioni per il ritorno del campo
    const k: string = this.JRF_C.MINIMO_PRINCIPALE;
    const f: FormGroup = this.mainForm;
    // Tento di ritornare la referenza del campo del form
    return f?.get(k);
  }

  /**
   * Getter per il recupero dell'abstract control omonimo.
   * @returns number con il valore del campo del form.
   */
  get minimoPrincipale(): number {
    // Tento di ritornare il valore del campo
    return this.minimoPrincipaleFC?.value;
  }

  /**
   * Getter che ritorna lo stile grafico per la gestione della sezione: minimo principale; per la sezione: row.
   * @returns any compatibile con la direttiva NgStyle per la gestione della grafica.
   */
  get rowMinimoPrincipale(): any {
    // Verifico e gestisco lo stile per la sezione
    return this._rowMinimoPrincipale ?? this.JRF_C.STYLE_ROW_MINIMO_PRINCIPALE;
  }

  /**
   * Getter che ritorna lo stile grafico per la gestione della sezione: minimo principale; per la sezione: col.
   * @returns any compatibile con la direttiva NgStyle per la gestione della grafica.
   */
  get colMinimoPrincipale(): any {
    // Verifico e gestisco lo stile per la sezione
    return this._colMinimoPrincipale ?? this.JRF_C.STYLE_COL_MINIMO_PRINCIPALE;
  }
}
