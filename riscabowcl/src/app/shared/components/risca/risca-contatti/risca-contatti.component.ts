import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { GestioneFormsDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/gestione-forms-dati-anagrafici.service';
import { SoggettoDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaContattiConsts } from '../../../consts/risca/risca-contatti.consts';
import { TipoInvio } from '../../../models/contatti/tipo-invio.model';
import { ContattiService } from '../../../services/contatti.service';
import { RiscaContattiService } from '../../../services/risca/risca-contatti/risca-contatti.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { riscaPhoneSanitizer } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  FormUpdatePropagation,
  RiscaComponentConfig,
  RiscaContatto,
  RiscaFormInputEmail,
  RiscaFormInputSelect,
  RiscaFormInputText,
  RiscaServerError,
} from '../../../utilities';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { GestioneRecapitoComponent } from '../risca-dati-anagrafici/gestione-recapito.component';

@Component({
  selector: 'risca-contatti',
  templateUrl: './risca-contatti.component.html',
  styleUrls: ['./risca-contatti.component.scss'],
})
export class RiscaContattiComponent
  extends GestioneRecapitoComponent<RiscaContatto>
  implements OnInit, OnChanges
{
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts();
  /** Costante RiscaContattiConsts contenente le costanti utilizzate dal componente. */
  RC_C = RiscaContattiConsts;
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();

  /** Oggetto di configurazione per il campo: tipoInvio. */
  tipoInvioConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: pec. */
  pecConfig: RiscaComponentConfig<RiscaFormInputEmail>;
  /** Oggetto di configurazione per il campo: email. */
  emailConfig: RiscaComponentConfig<RiscaFormInputEmail>;
  /** Oggetto di configurazione per il campo: telefono. */
  telefonoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: cellulare. */
  cellulareConfig: RiscaComponentConfig<RiscaFormInputText>;

  /** Lista di oggetti any contenente le informazioni per i tipi invio. */
  listaTipiInvio: TipoInvio[] = [];

  /** (value: any) => any; che permette di definire la logica di sanitizzazione per i numeri di telefono. */
  sanitizeNumeroTelefono: (value: any) => any = (value: any) => {
    // Richiamo al funzione di sanitizzazione del numero di telefono
    const numSanitize = riscaPhoneSanitizer(value);
    // Ritorno il numero sanitizzato
    return numSanitize;
  };

  /**
   * Costruttore.
   */
  constructor(
    private _contatti: ContattiService,
    private _formBuilder: FormBuilder,
    private _gestioneFormsDA: GestioneFormsDatiAnagraficiService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _riscaContatti: RiscaContattiService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaUtilities: RiscaUtilitiesService,
    soggettoDA: SoggettoDatiAnagraficiService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaFormSubmitHandler,
      riscaUtilities,
      soggettoDA
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit(): void {
    // Lancio il setup delle form
    this.initForms();
    // Setup delle liste per le select del form
    this.initListeSelect();
  }

  ngOnChanges(changes: SimpleChanges) {
    // Verifico se la modalità è stata cambiata
    if (changes.modalita && !changes.modalita.firstChange) {
      // Richiamo i validatori
      this.initValidators();
      this.updateTipoInvioValidator();
    }
  }

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Setup degli errori da verificare al submit della form
    this.setupMainFormErrors();
    // Setup delle input del calendario
    this.setupFormInputs();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [
      ...this.EM.MAP_FORM_GROUP_REQUIRED,
      ...this.EM.MAP_PATTERN_EMAIL,
      ...this.EM.MAP_PATTERN_PEC,
    ];
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Genero il setup per i campi
    const {
      tipoInvioConfig,
      pecConfig,
      emailConfig,
      telefonoConfig,
      cellulareConfig,
    } = this._riscaContatti.setupFormInputs();

    // Assegno locamente il setup
    this.tipoInvioConfig = tipoInvioConfig;
    this.pecConfig = pecConfig;
    this.emailConfig = emailConfig;
    this.telefonoConfig = telefonoConfig;
    this.cellulareConfig = cellulareConfig;
  }

  /**
   * Funzione di setup per le liste per le select del form.
   */
  private initListeSelect() {
    // Recupero i tipi invio
    this._contatti.getTipiInvio().subscribe({
      next: (tipiInvio: TipoInvio[]) => {
        // Lancio il setup
        this.onScaricoTipiInvio(tipiInvio);

        // Verfico che esista l'oggeto del form
        if (this.mainForm) {
          // Richiamo il pristine
          this.mainForm.markAsPristine();
        }
        // #
      },
      error: (e: RiscaServerError) => {
        // Loggo l'errore
        this._logger.error('initListeSelect', e);
        // #
      },
    });
  }

  /**
   * Funzione invocata allo scarico dei tipi invio.
   * @param tipiInvio Array di TipoInvio scaricati.
   */
  private onScaricoTipiInvio(tipiInvio: TipoInvio[]) {
    // Assegno localmente la lista
    this.listaTipiInvio = tipiInvio;
    // Setup iniziale
    this.initTipoInvio();
  }

  /**
   * Funzione di setup per il tipo invio.
   * @param isReset boolean che definisce che è richiesto un reset totale.
   */
  private initTipoInvio(isReset?: boolean) {
    // Verifico se è stato richiesto un reset
    if (isReset) {
      // Resetto il valore per il tipo sede
      this.initCampoForm(this.RC_C.TIPO_INVIO, null);
      // Blocco il flusso
      return;
    }

    // Setup per il valore di default del form
    setTimeout(() => {
      this._contatti.setTipoContattoDefault(
        this.mainForm,
        this.RC_C.TIPO_INVIO,
        this.listaTipiInvio,
        this.recapito?.tipo_invio?.id_tipo_invio
      );
    });
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup del form
    this.initContattiForm();
    // Setup dei listiner dei form per la manipolazione dei dati
    this.initFormListener();
    // Setup dei campi form in base alla parametrizzazione degli input
    this.initCampiForm();
    // Lancio il setup per i validatori dei campi
    this.initValidators();
  }

  /**
   * Setup del form.
   */
  private initContattiForm() {
    this.mainForm = this._formBuilder.group(
      {
        tipoInvio: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        pec: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        email: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        telefono: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        cellulare: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
      },
      {
        validators: [],
      }
    );

    // Verifico i flag
    if (this.isISFormDisabilitato) {
      // Disabilito il form
      this.mainForm.disable();
    }
  }

  /**
   * Funzione specifica adibita al setup dei listner delle form.
   * La funzione registrerà diversi eventi sulle form, atte a gestire la logica e la manipolazione dei dati.
   */
  private initFormListener() {
    // Registro un ascoltatore per il cambio del comune
    this.mainForm.get(this.RC_C.TIPO_INVIO).valueChanges.subscribe({
      next: (tipoInvio: TipoInvio) => {
        // Aggiorno i validatori per il tipo invio
        this.updateTipoInvioValidator();
      },
    });
  }

  /**
   * Funzione di setup dei campi del form in base alla parametrizzazione degli input.
   * @param isReset boolean che definisce che è richiesto un reset totale.
   */
  private initCampiForm(isReset?: boolean) {
    // Definisco una variabile per il recapito principale
    let recapito = this.recapito;
    // Verifico esista
    if (!recapito) {
      return;
    }

    // Verifico la configurazione per pec
    if (recapito.pec && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RC_C.PEC, recapito.pec);
    }
    // Verifico la configurazione per email
    if (recapito.email && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RC_C.EMAIL, recapito.email);
    }
    // Verifico la configurazione per telefono
    if (recapito.telefono && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RC_C.TELEFONO, recapito.telefono);
    }
    // Verifico la configurazione per cellulare
    if (recapito.cellulare && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RC_C.CELLULARE, recapito.cellulare);
    }
  }

  /**
   * Funzione di comodo usato per l'init dei campi del form.
   * @param campo string form control name per il set del valore.
   * @param valore any valore da settare.
   */
  private initCampoForm(campo: string, valore: any) {
    // Imposto il campo secondo i parametri
    this._riscaUtilities.setFormValue(this.mainForm, campo, valore);
  }

  /**
   * Funzione che assegna ai campi del form, le validazioni per i dati anagrafici.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param update boolean che definisce se è necessario ricaricare e ri-validare la form a seguito del cambio dei validatori.
   */
  private initValidators(update = false) {
    // Richiamo la funzione per la gestione dei validatori
    this._gestioneFormsDA.setValidatorsContatti(
      this.mainForm,
      this.modalita,
      update
    );
  }

  /**
   * Funzione che assegna ai campi del form, le validazioni per i validatori dello stato.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param update boolean che definisce se è necessario ricaricare e ri-validare la form a seguito del cambio dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private updateTipoInvioValidator(
    update = true,
    uC: FormUpdatePropagation = { onlySelf: true, emitEvent: false }
  ) {
    // Lancio i validatori a seguito della selezione del tipo soggetto
    this._riscaContatti.validatoriTipoInvio(
      this.mainForm,
      this.modalita,
      update,
      uC
    );
  }

  /**
   * ######################
   * FUNZIONALITA' DEL FORM
   * ######################
   */

  /**
   * Funzione di reset manuale richiamabile dal componente padre.
   * @override
   */
  onFormReset() {
    // Resetto la form
    this.mainForm.reset();
    // Resetto il flag del form submitted
    this.mainFormSubmitted = false;

    // Rilancio i setup/init del componente
    this.initTipoInvio(true);
  }

  /**
   * Funzione di restore manuale richiamabile dal componente padre.
   */
  onFormRestore() {
    // Resetto il flag del form submitted
    this.mainFormSubmitted = false;

    // Rilancio i setup/init del componente
    this.initCampiForm();
    this.initTipoInvio();
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di comodo che verifica le condizioni per abilitare il form e le sue componenti.
   * @returns boolean che specifica se il form e le sue componenti sono disabilitate.
   */
  get isISFormDisabilitato() {
    // Verifico il flag di abilitazione per "isGestioneAbilitata"
    const disabledIGA = !this.isGestioneAbilitata;
    // Verifico i flag di abilitazione per "accesso elementi app"
    const disabledAEA =
      this.insModSoggettoDisabled || this.praticaSoggettoDisabled;

    // Verifico i flag
    return disabledIGA || disabledAEA;
  }
}
