import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import { RiscaFormChildComponent } from '../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { FormUpdatePropagation } from '../../../../shared/utilities';
import { InteressiDiMoraFormFormConfigs } from './utilities/interessi-di mora-form.form-configs';
import { InteressiDiMoraFormConsts } from './utilities/interessi-di-mora-form.consts';
import { InteressiDiMoraFormFieldsConfig } from './utilities/interessi-di-mora-form.fields-configs';
import { IInteressiDiMoraFormData } from './utilities/interessi-di-mora-form.interfaces';

@Component({
  selector: 'interessi-di-mora-form',
  templateUrl: './interessi-di-mora-form.component.html',
  styleUrls: ['./interessi-di-mora-form.component.scss'],
})
export class InteressiDiMoraFormComponent
  extends RiscaFormChildComponent<IInteressiDiMoraFormData>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti del componente di riferimento. */
  IDMF_C = new InteressiDiMoraFormConsts();

  /** IInteressiDiMoraFormData con le informazioni per l'inizializzazione del form. */
  @Input() interessiDiMoraForm: IInteressiDiMoraFormData;
  /** Input con il flag di gestione per la disabilitazione dell'elemento applicativo. */
  @Input() AEADisabled: boolean = false;

  /** InteressiDiMoraFormFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: InteressiDiMoraFormFieldsConfig;
  /** InteressiDiMoraFormFormConfig come classe che definisce la struttura di configurazione del form. */
  formConfigs: InteressiDiMoraFormFormConfigs;

  /**
   * Boolean per mostrare il button di aggiungi.
   * => se siamo in modale di modifica sarà false
   * => se siamo su tassi di interesse sarà sempre true
   */
  @Input() showButtonAggiungi: boolean;
  /** Il campo data fine viene nascosto nell'inserimento sul componente tassi-di-interesse */
  @Input() showFieldDataFine: boolean;
  //boolean per determinare se il campo data inizio dev'essere disabilitato
  @Input() disabledDataInizio: boolean;
  // Boolean per disabilitare i campi della form in caso di apertura da modale
  @Input() isReadOnly: boolean;

  /**
   * Costruttore.
   */
  constructor(
    logger: LoggerService,
    private _formBuilder: FormBuilder,
    navigationHelper: NavigationHelperService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
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
    this.formInputs = new InteressiDiMoraFormFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new InteressiDiMoraFormFormConfigs(s);
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
    // Lancio la funzione di configurazione per il form dati
    this.initFormConfigs();
    // Non ci sono logiche per il momento
    this.initForm();
  }

  /**
   * Funzione di init per la configurazione strutturale del form group.
   */
  private initFormConfigs() {
    // Verifico se la modalità di gestione del form è modifica
    if (this.modifica) {
      // Siamo in modifica devo disabilitare alcuni campi
      const DATA_INIZIO = this.IDMF_C.DATA_INIZIO;
      const DATA_FINE = this.IDMF_C.DATA_FINE;

      // Definisco la configurazione per la gestione della propagazione dell'evento
      const o: FormUpdatePropagation = { emitEvent: false };
      // Disabilito i campi specifici
      this.mainForm.get(DATA_INIZIO).disable(o);
      this.mainForm.get(DATA_FINE).disable(o);
    }

    // Recupero dal componente le configurazioni di input
    const disableForm: boolean = this.AEADisabled;
    // Verifico se devo disabilitare il form
    if (disableForm) {
      // Definisco la configurazione per la gestione della propagazione dell'evento
      const o: FormUpdatePropagation = { emitEvent: false };
      // Disattivo i campi del form
      this.mainForm.disable(o);
      // #
    }
  }

  /**
   *  Funzione di init per la form.
   * Inizializza i valori della form con i dati in input.
   */
  private initForm() {
    // Recupero la configurazione in input
    let formData: IInteressiDiMoraFormData = this.interessiDiMoraForm;

    // Verifico il campo percentuale
    if (formData?.percentuale) {
      // Imposto il valore
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.IDMF_C.PERCENTUALE,
        formData.percentuale
      );
    }

    // Verifico il campo dataInizio
    if (formData?.dataInizio) {
      // Imposto il valore
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.IDMF_C.DATA_INIZIO,
        formData.dataInizio
      );
    }

    // Verifico il campo dataFine
    if (formData?.dataFine) {
      // Imposto il valore
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.IDMF_C.DATA_FINE,
        formData.dataFine
      );
    }
  }

  /**
   * #################
   * FUNZIONI DEL FORM
   * #################
   */
  
  /**
   * Funzione di reset del form e del componente.
   * @param args any con possibili parametri da passare alla funzione.
   * @override
   */
  /* onFormReset(...args: any) {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Reset manuale della form
    this.mainForm.reset();
    
    // Verifico in che modalità sto gestendo i dati
    if (this.modifica) {
      // In modifica devo ripristinare le informazioni allo stato iniziale rispetto all'oggetto configurato in input
      this.initForm();
      // Imposto il form come se non fosse stato toccato
      this.mainForm?.markAsPristine();
      // #
    }
  } */

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter per l'id dell'ambito.
   * @returns number con l'id.
   */
  get idAmbito(): number {
    // Ritorno l'id ambito
    return this._user.idAmbito;
  }
}
