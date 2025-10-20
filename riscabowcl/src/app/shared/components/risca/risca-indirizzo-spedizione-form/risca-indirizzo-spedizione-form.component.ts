import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/index';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { AmbitoService } from '../../../../features/ambito/services';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaIndirizzoSpedizioneFormService } from '../../../services/risca/risca-indirizzo-spedizione-form/risca-indirizzo-spedizione-form.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { AppActions } from '../../../utilities';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';
import { IndirizzoSpedizioneFormConsts } from './utilities/risca-indirizzo-spedizione-form.consts';
import {
  IISFFieldsConfigClass,
  ISFFieldsConfigClass,
} from './utilities/risca-indirizzo-spedizione-form.fields-config';
import {
  IIndirizzoSpedizioneForm,
  IISErrorMaps,
} from './utilities/risca-indirizzo-spedizione-form.interfaces';

@Component({
  selector: 'risca-indirizzo-spedizione-form',
  templateUrl: './risca-indirizzo-spedizione-form.component.html',
  styleUrls: ['./risca-indirizzo-spedizione-form.component.scss'],
  providers: [RiscaIndirizzoSpedizioneFormService],
})
export class RiscaIndirizzoSpedizioneFormComponent
  extends RiscaFormChildComponent<IndirizzoSpedizioneVo>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti per il componente attuale. */
  ISF_C = new IndirizzoSpedizioneFormConsts();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** RecapitoVo che definisce le informazioni per la pre-compilazione del form. */
  @Input('recapito') recapitoConfig: RecapitoVo;
  /** IndirizzoSpedizioneVo che definisce le informazioni per la pre-compilazione del form. */
  @Input('indirizzoSpedizione')
  indirizzoSpedizioneConfig: IndirizzoSpedizioneVo;
  /** Boolean che permette di disattivare il form tramite condizioni esterne. */
  @Input('isFormDisabilitato') isFormDisabledByInput: boolean = false;

  /** Boolean che definisce la configurazione di accesso all'elemento dell'app per il salvataggio dati. */
  private AEA_DISDisabled: boolean = false;

  /** Classe ISFFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: ISFFieldsConfigClass;
  /** any contenente la mappatura degli errori per il form, sulla base della nazione del recapito. */
  errorsMap: IISErrorMaps;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _ambito: AmbitoService,
    logger: LoggerService,
    private _formBuilder: FormBuilder,
    navigationHelper: NavigationHelperService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaISF: RiscaIndirizzoSpedizioneFormService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Lancio il super
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);

    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // N.B.: PER ORA SARA' SEMPRE MODIFICA, FORZO IL VALORE
    this.modalita = AppActions.modifica;
    // Lancio l'init dei dati del componente
    this.initComponente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup per le informazioni del componente.
   */
  private setupComponente() {
    // Setup dei flag di accesso alle sezioni
    this.setupDISDisabled();
    // Setep degli errori da verificare al submit della form
    this.setupMainFormErrors();
    // Lancio il setup delle costanti del componente
    this.setupFormInputs();

    // Lancio il setup per il listener del submit padre
    this.listenToParentSubmit();
    // Lancio il setup per il listener del reset padre
    this.listenToParentReset();
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  private setupDISDisabled() {
    // Recupero la chiave per la configurazione della form
    const disKey = this.AEAK_C.DETTAGLIO_IND_SPED;
    // Recupero la configurazione della form dal servizio
    this.AEA_DISDisabled = this._accessoElementiApp.isAEADisabled(disKey);
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [];
  }

  /**
   * Funzione di setup delle inputs form del componente.
   */
  protected setupFormInputs() {
    // Definisco la configurazioni per le input
    const iConfigs: IISFFieldsConfigClass = {
      riscaFormBuilder: this._riscaFormBuilder,
    };
    // Istanzio la classe per le configurazioni
    this.formInputs = new ISFFieldsConfigClass(iConfigs);
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di init del componente, che permette le logiche di gestione dati a seguito dell'hook ngOnInit di Angular.
   */
  private initComponente() {
    // Lancio l'init dell'oggetto della form principale
    this.initMainForm();
    // Lancio l'init per gli ascoltatori della main form
    this.initMainFormListener();
    // Lancio la gestione dei validatori per i campi del form
    this.initValidatoriForm(this.indirizzoSpedizioneConfig);
    // Lancio la gestione dei disabilitatori per i campi del form
    this.initDisabilitatoriForm(this.indirizzoSpedizioneConfig);
    // Lancio la funzione per la generazione della mappa dati
    this.initErrorsMap(this.indirizzoSpedizioneConfig);

    // Lancio la valorizzazione dei dati passati come configurazione iniziale
    this.initValoriForm(this.indirizzoSpedizioneConfig);
  }

  /**
   * Funzione di init per quanto riguarda la struttura del mainForm per il form di gestione dati.
   */
  private initMainForm() {
    // Inizializzo la form
    this.mainForm = this._formBuilder.group(
      {
        destinatario: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        presso: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        indirizzo: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        comune_citta: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        provincia: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        cap: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        frazione: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        nazione: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
      },
      // Validatori campi incrociati
      {
        validators: [],
      }
    );

    // Verifico se il form è da disabilitare
    if (this.isFormDisabilitato) {
      // Vado a disabilitare il form
      this.mainForm.disable();
    }
  }

  /**
   * Funzione d'inizializzazione dei listener per la form.
   */
  private initMainFormListener() {
    // Registro gli ascoltatori per i campi della form, in modo a averli maiuscoli e non accentati
    this.setEventoFormattaValore(this.ISF_C.DESTINATARIO);
    this.setEventoFormattaValore(this.ISF_C.PRESSO);
    this.setEventoFormattaValore(this.ISF_C.INDIRIZZO);
    this.setEventoFormattaValore(this.ISF_C.COMUNE_CITTA);
    this.setEventoFormattaValore(this.ISF_C.PROVINCIA);
    this.setEventoFormattaValore(this.ISF_C.FRAZIONE);
  }

  /**
   * Iscrive alla form gli eventi per trasformare il campo dato,
   * settando le lettere accentate come normali con apostrofo e con uppercase.
   * @param campo il nome del campo della form da modificare.
   * @returns Subscription all'evento di cambio del campo.
   */
  private setEventoFormattaValore(campo: string): Subscription {
    return this.mainForm
      .get(campo)
      .valueChanges.subscribe((destinatario: string) => {
        // Lancio le logiche per formattare il destinatario
        this.formatStringToUpperNoAccent(campo, destinatario);
      });
  }

  /**
   * Inizializza i validatori della componente in base al recapito della configurazione passata in input.
   * @param indirizzo IndirizzoSpedizioneVo contenente le informazioni per il setup iniziale dei dati.
   */
  private initValidatoriForm(indirizzo?: IndirizzoSpedizioneVo) {
    // Recupero l'oggetto del form
    const form = this.mainForm;
    // Richiamo la funzione di set del servizio
    this._riscaISF.setValidatoriForm(form, indirizzo);
  }

  /**
   * Inizializza i disabilitatori della componente in base al recapito della configurazione passata in input.
   * @param indirizzo IndirizzoSpedizioneVo contenente le informazioni per il setup iniziale dei dati.
   */
  private initDisabilitatoriForm(indirizzo?: IndirizzoSpedizioneVo) {
    // Verifico se il form è disattivato da configurazione
    if (this.isFormDisabilitato) {
      // Il form è già disabilitato per default
      return;
    }
    
    // Recupero l'oggetto del form
    const form = this.mainForm;
    // Richiamo la funzione di set del servizio
    this._riscaISF.setDisabilitatoriForm(form, indirizzo);
  }

  /**
   * Inizializza i valori della componente in base ai valori della configurazione passata in input.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni per il setup iniziale dei dati.
   */
  private initValoriForm(indirizzoSpedizione?: IndirizzoSpedizioneVo) {
    // Recupero l'oggetto del form
    const form = this.mainForm;
    // Richiamo la funzione di set del servizio
    this._riscaISF.setValoriForm(form, indirizzoSpedizione);

    // Imposto la form come se l'avesse già "toccata" l'utente
    this.mainForm.markAllAsTouched();
    this.mainForm.markAsDirty();
  }

  /**
   * Funzione di init del componente, che valorizza la mappa degli errori da usare nel form.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni per il setup iniziale dei dati.
   */
  initErrorsMap(indirizzoSpedizione?: IndirizzoSpedizioneVo) {
    // Richiamo la funzione del servizio
    this.errorsMap = this._riscaISF.generaMappeErroriForm(indirizzoSpedizione);
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione di comodo che converte e imposta il valore all'interno del campo della form a lui dedicato.
   * @param campo il campo della form da aggiornare
   * @param destinatario string da verificare ed eventualmente convertire.
   */
  private formatStringToUpperNoAccent(campo: string, destinatario: string) {
    // Verifico l'input
    if (!destinatario) {
      // Niente da formattare
      return;
    }

    // Dichiaro le variabili per il set del form
    const f = this.mainForm;
    const dFCN = campo;
    const d = this._riscaUtilities.formatStringToUpperNoAccent(destinatario);
    const o = { emitEvent: false };
    // Aggiorno il campo della form manualmente e non emetto l'evento (sennò si entra in loop di changes del destinatario)
    this._riscaUtilities.setFormValue(f, dFCN, d, o);
    // Rilancio la validazione dei campi e non emetto l'evento (come sopra, evito di creare un loop d'aggiornamento del destinatario)
    this.mainForm.get(dFCN).updateValueAndValidity(o);
  }

  /**
   * ################################
   * FUNZIONI DI GESTIONE PER IL FORM
   * ################################
   */

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @returns T contente le informazioni del form in modalità raw value.
   * @override
   */
  getMainFormRawValue(): IndirizzoSpedizioneVo {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }

    // Il main form esiste recupero i dati dal form
    const formData: IIndirizzoSpedizioneForm = this.mainForm.getRawValue();
    // Recupero l'oggetto originale che contiene i riferimenti di tutto l'oggetto
    const isInit = this.indirizzoSpedizioneConfig;

    // Converto l'oggetto in IndirizzoSpedizioneVo
    return this._riscaISF.convertIISFormToISVoFromBase(formData, isInit);
  }

  /**
   * Funzione di reset del form e del componente.
   * @override
   */
  onFormReset() {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Submit manuale della form
    this.mainForm.reset();

    // Verifico se la modalità è: modifica
    if (this.modifica) {
      // Vado a reimpostare i valori dei campi
      this.initValoriForm(this.indirizzoSpedizioneConfig);
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo per verificare se la nazione è italiana.
   */
  get isIndirizzoItaliano() {
    // Recupero l'indirizzo di spedizione
    const indirizzo = this.indirizzoSpedizioneConfig;
    // Verifico se la configurazione della nazione dell'indirizzo è ITALIA
    return this._riscaISF.isIndirizzoItaliano(indirizzo);
  }

  /**
   * Getter di comodo per il recupero della configurazione dei soggetti.
   */
  get isGestioneAbilitata() {
    // Recupero il flag per la gestione abilitata
    return this._ambito.isGestioneAbilitata;
  }

  /**
   * Getter di comodo che verifica le condizioni per abilitare il form e le sue componenti.
   * @returns boolean che specifica se il form e le sue componenti sono disabilitate.
   */
  get isFormDisabilitato() {
    // Verifico il flag di abilitazione per "isGestioneAbilitata"
    const disabledIGA = !this.isGestioneAbilitata;
    // Verifico i flag di abilitazione per "accesso elementi app"
    const disabledAEA = this.AEA_DISDisabled;
    // Form disabilitato da flag in input
    const isFDisabled = this.isFormDisabledByInput;

    // Verifico i flag
    return disabledIGA || disabledAEA || isFDisabled;
  }
}
