import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Subscription } from 'rxjs/index';
import { RicercaSoggetto } from '../../../core/commons/vo/ricerca-soggetto-vo';
import { LoggerService } from '../../../core/services/logger.service';
import { NavigationHelperService } from '../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../core/services/user.service';
import { TipoSoggettoVo } from '../../../features/ambito/models';
import { SoggettoDatiAnagraficiService } from '../../../features/pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { CommonConsts } from '../../consts/common-consts.consts';
import { FormCercaSoggettoConsts } from '../../consts/form-cerca-soggetto.consts';
import { codiceFiscalePerTipoSoggetto } from '../../miscellaneous/forms-validators/soggetto/form-validators.s';
import { RiscaAlertService } from '../../services/risca/risca-alert.service';
import {
  IRiscaEmitter,
  RiscaEventEmitterService,
} from '../../services/risca/risca-event-emitter.service';
import { RiscaFormBuilderService } from '../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../services/risca/risca-utilities/risca-utilities.service';
import {
  IFormCercaSoggetto,
  RiscaComponentConfig,
  RiscaFormInputSelect,
  RiscaFormInputText,
  RiscaServerError,
  RiscaServerStatus,
} from '../../utilities';
import { RiscaErrorsMap } from '../../utilities/classes/errors-maps';
import { RiscaNotifyCodes } from '../../utilities/enums/risca-notify-codes.enums';
import { RiscaSelectComponent } from '../form-inputs/risca-select/risca-select.component';
import { RiscaFormParentComponent } from '../risca/risca-form-parent/risca-form-parent.component';

/**
 * Interfaccia che definisce la struttura dati del form del componente.
 */
interface FormCercaSoggetto {
  tipoSoggetto: TipoSoggettoVo;
  codiceFiscale: string;
}

@Component({
  selector: 'form-cerca-soggetto',
  templateUrl: './form-cerca-soggetto.component.html',
  styleUrls: ['./form-cerca-soggetto.component.scss'],
})
export class FormCercaSoggettoComponent
  extends RiscaFormParentComponent
  implements OnInit
{
  /** Oggetto FormCercaSoggettoConsts contenente le costanti utilizzate dal componente. */
  FCS_C = FormCercaSoggettoConsts;
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts();
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();

  /** Boolean che definisce se il componente si deve registrare agli eventi "RiscaEmitter". */
  @Input() riscaEmitter: boolean = false;
  /** Boolean che definisce lo stato di disabilitazione degli elementi del componente, per: cerca soggetto per gruppo. */
  @Input('gruppoCSDisabled') AEA_gruppo_CSDisabled = false;

  /** Output che comunica al componente padre il l'avvio di una ricerca. */
  @Output() onSoggettoRicerca = new EventEmitter<IFormCercaSoggetto>();
  /** Output che comunica al componente padre il risultato della ricerca. */
  @Output() onSoggettoCercato = new EventEmitter<IFormCercaSoggetto>();
  /** Output che comunica al componente padre il risultato della ricerca in errore. */
  @Output() onSoggettoError = new EventEmitter<RiscaServerError>();

  /** ViewChild connesso alla select: tipo soggetto. */
  @ViewChild('selectTipoSoggetto') selectTipoSoggetto: RiscaSelectComponent;

  /** Form group che definisce la struttura della form: nuova istanza. */
  cercaSoggettoForm: FormGroup;

  /** Boolean che tiene traccia dello stato di submitted del form. */
  cercaSoggettoSubmitted = false;

  /** Oggetto di configurazione per il campo: tipo soggetto. */
  tipoSoggettoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: codice fiscale. */
  codiceFiscaleConfig: RiscaComponentConfig<RiscaFormInputText>;

  /** Lista di oggetti TipoSoggetto contenente le informazioni per i tipi soggetto. */
  listaTipiSoggetto: TipoSoggettoVo[] = [];

  /** Subscription che viene invocato all'emissione dell'evento dal servizio RiscaEventEmitter. */
  onRiscaEvent: Subscription;

  /**
   * Costruttore.
   */
  constructor(
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaEE: RiscaEventEmitterService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _soggettoDA: SoggettoDatiAnagraficiService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit(): void {
    // Lancio l'init per il risca emitter
    this.initRiscaEventEmitter();
    // Lancio il setup delle form
    this.initForms();
  }

  ngOnDestroy() {
    // Tento di fare l'unsubscribe dei listener
    try {
      // Verifico che esistano i listener
      if (this.onRiscaEvent) {
        this.onRiscaEvent.unsubscribe();
      }
    } catch (e) {
      // Variabili di comodo
      const method = 'FormCercaSoggettoComponent';
      // Loggo l'errore
      this._logger.warning(`${method} failed to unsubscribe`, e);
    }

    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Setup delle input del calendario
    this.setupFormInputs();
    // Setup delle liste per le select del form
    this.setupListeSelect();
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Genero il setup per i campi
    this.tipoSoggettoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.FCS_C.LABEL_TIPO_SOGGETTO,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    this.codiceFiscaleConfig = this._riscaFormBuilder.genInputText({
      label: this.FCS_C.LABEL_CODICE_FISCALE,
      showErrorFG: true,
      showErrorFC: true,
      maxLength: 16,
    });
  }

  /**
   * Funzione di setup per le liste per le select del form.
   */
  private setupListeSelect() {
    // Diachiaro una funzione di comodo per gli error delle response
    const resError = (err: any) => {
      this._logger.error('resError [resTipiSoggetto]', err);
    };

    // Recupero tipi soggetto
    this._soggettoDA.getTipiSoggetto().subscribe({
      next: (res: TipoSoggettoVo[]) => {
        // Assegno la lista
        this.listaTipiSoggetto = res;

        // Se il form esiste
        if (this.cercaSoggettoForm) {
          // Lancio il pristine
          this.cercaSoggettoForm.markAsPristine();
        }
      },
      error: resError,
    });
  }

  /**
   * Funzione che registra gli eventi del servizio RiscaEventEmitter.
   */
  private initRiscaEventEmitter() {
    // Verifico la configurazione
    if (this._riscaEE?.emitter) {
      // Registro gli eventi
      this.onRiscaEvent = this._riscaEE.emitter.subscribe({
        next: (event: IRiscaEmitter) => {
          // Richiamo la funzione di gestione degli eventi
          this.riscaEventHandler(event);
        },
      });
    }
  }

  /**
   * Funzione di gestione degli eventi scatenati tramite servizio RiscaEventEmitter.
   * @param event IRiscaEmitter con le informazioni dell'evento.
   */
  private riscaEventHandler(event: IRiscaEmitter) {
    // Verifico l'input
    if (!event) {
      // Blocco le logiche
      return;
    }

    // Recupero la chiave
    const { key } = event;

    // Verifico l'evento
    switch (key) {
      case this.C_C.REE_KEY_REST_FORM_CERCA_SOG: {
        // Richiamo il reset della form
        this.resetForm();
      }
    }
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup della struttura della sotto form nuova istanza
    this.initCercaSoggettoForm();
    // Setup dei listiner dei form per la manipolazione dei dati
    this.initFormListener();
  }

  /**
   * Setup del form: cerca soggetto.
   */
  private initCercaSoggettoForm() {
    // Init del form
    this.cercaSoggettoForm = this._formBuilder.group(
      {
        tipoSoggetto: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        codiceFiscale: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
      },
      {
        validators: [
          codiceFiscalePerTipoSoggetto(
            this.FCS_C.TIPO_SOGGETTO,
            this.FCS_C.CODICE_FISCALE
          ),
        ],
      }
    );

    // Verifico il flag d'abilitazione per la profilazione
    if (this.AEA_gruppo_CSDisabled) {
      // Disattivo il form
      this.cercaSoggettoForm.disable();
    }
  }

  /**
   * Funzione specifica adibita al setup dei listner delle form.
   * La funzione registrerà diversi eventi sulle form, atte a gestire la logica e la manipolazione dei dati.
   */
  private initFormListener() {
    // Gestione delle logiche
  }

  /**
   * #####################
   * METODI DEL COMPONENTE
   * #####################
   */

  /**
   * Funzione collegata al submit della form per la ricerca del soggetto.
   */
  cercaSoggettoSubmit() {
    // Resetto il submitted del form
    this.cercaSoggettoSubmitted = true;
    // Recupero i dati del form
    const cercaSoggetto: FormCercaSoggetto =
      this.cercaSoggettoForm.getRawValue();

    // Verifico la validità del form
    if (this.cercaSoggettoForm.valid) {
      // Lancio l'evento di inizio ricerca
      this.ricercaAvviata(cercaSoggetto);
      // Richiamo la funzione per la ricerca soggetto
      this.cercaSoggetto(cercaSoggetto);
    }
  }

  /**
   * Funzione di reset del form dati.
   */
  resetForm() {
    // Resetto il submitted del form
    this.cercaSoggettoSubmitted = false;
    // Resetto il campo codice fiscale del form
    this.cercaSoggettoForm.reset();
    // Per la select effettuo un reset con default
    this.selectTipoSoggetto.setDefault();
    // Imposto il form come untouched
    this.cercaSoggettoForm.markAsUntouched();
    this.cercaSoggettoForm.markAsPristine();
  }

  /**
   * Funzione di supporto per la gestione dell'avvio della ricerca del soggetto.
   * @param cercaSoggetto FormCercaSoggetto per la ricerca del soggetto.
   */
  private ricercaAvviata(cercaSoggetto: FormCercaSoggetto) {
    // Definisco le informazioni per la ricerca di un soggetto
    const ts = cercaSoggetto.tipoSoggetto;
    const cf = cercaSoggetto.codiceFiscale;

    // Emetto l'evento di ricerca avviata
    this.onSoggettoRicerca.emit({ codiceFiscale: cf, tipoSoggetto: ts });
  }

  /**
   * Funzione di supporto per la ricerca del soggetto.
   * @param cercaSoggetto FormCercaSoggetto per la ricerca del soggetto.
   */
  private cercaSoggetto(cercaSoggetto: FormCercaSoggetto) {
    // Definisco le informazioni per la ricerca di un soggetto
    const idA = this._user.idAmbito;
    const ts = cercaSoggetto.tipoSoggetto;
    const idTS = ts.id_tipo_soggetto;
    const cf = cercaSoggetto.codiceFiscale;

    // Lancio la ricerca di un soggetto
    this._soggettoDA.cercaSoggetto(idA, idTS, cf).subscribe({
      next: (ricercaSoggetto: RicercaSoggetto) => {
        // Gestisco le logiche come se fosse una success senza aver trovato dati
        this.emettiSoggetto(ricercaSoggetto, ts, cf);
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore con le verifiche
        this.onCSError(e, ts, cf);
      },
    });
  }

  /**
   * Funzione di comodo per gestire l'errore sulla ricerca del soggetto.
   * @param e RiscaServerError per la gestione dell'errore.
   * @param ts TipoSoggetto per la gestione dell'errore.
   * @param cf string con il codice fiscae per la gestione dell'errore.
   */
  private onCSError(e: RiscaServerError, ts: TipoSoggettoVo, cf: string) {
    // Verifico se non è stato trovato un soggetto
    const isSNF = this.isSoggettoNonTrovato(e);
    // Verifico l'errore
    if (isSNF) {
      // Variabili di comodo
      const s = undefined;
      // Gestisco le logiche come se fosse una success senza aver trovato dati
      this.emettiSoggetto(s, ts, cf);
      // #
    } else {
      // Normale gestione dell'errore
      this.onSoggettoError.emit(e);
    }
  }

  /**
   * Funzione di comodo per emettere l'evento di soggetto trovato.
   * @param rs RicercaSoggetto con le informazioni per il padre.
   * @param ts TipoSoggetto con le informazioni per il padre.
   * @param cf string con le informazioni per il padre.
   */
  emettiSoggetto(rs: RicercaSoggetto, ts: TipoSoggettoVo, cf: string) {
    // Gestisco le logiche come se fosse una success senza aver trovato dati
    this.onSoggettoCercato.emit({
      ricercaSoggetto: rs,
      tipoSoggetto: ts,
      codiceFiscale: cf,
    });
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione ad hoc creata per gestire la casistica in cui non viene trovato un soggetto.
   * Si verifica uno specifico codice e uno specifico status, entrambi generati dal server.
   * @param e RiscaServerError con l'errore generato.
   * @returns boolean che definisce se l'errore generato è perché non è stato trovato un soggetto [true] o è un altro tipo d'errore [false].
   */
  private isSoggettoNonTrovato(e: RiscaServerError): boolean {
    // Verifico l'input
    if (!e) {
      // Altro errore
      return false;
    }

    // Variabili di comodo
    const http404 = RiscaServerStatus.notFound;
    const notFoundSoggEnable: RiscaNotifyCodes = RiscaNotifyCodes.A008;
    const notFoundSoggDisable: RiscaNotifyCodes = RiscaNotifyCodes.I001;

    // Definisco i casi di controllo
    const c1 = this._riscaUtilities.isServerErrorManageable(
      e,
      http404,
      notFoundSoggEnable
    );
    const c2 = this._riscaUtilities.isServerErrorManageable(
      e,
      http404,
      notFoundSoggDisable
    );
    // Definisco un array delle condizioni
    const conditions = [c1, c2];

    // Ritorno il controllo
    return conditions.some((condition: boolean) => condition);
  }
}
