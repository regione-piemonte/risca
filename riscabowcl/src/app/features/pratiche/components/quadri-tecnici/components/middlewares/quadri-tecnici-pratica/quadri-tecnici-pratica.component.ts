import { Component, Injector, Input, OnInit } from '@angular/core';
import { Subscription } from 'rxjs/index';
import { ComponenteDt } from '../../../../../../../core/commons/vo/componente-dt-vo';
import { PraticaDTVo } from '../../../../../../../core/commons/vo/pratica-vo';
import { AccessoElementiAppKeyConsts } from '../../../../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormParentAndChildComponent } from '../../../../../../../shared/components/risca/risca-form-parent-and-child/risca-form-parent-and-child.component';
import { RiscaAlertService } from '../../../../../../../shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaMessagesService } from '../../../../../../../shared/services/risca/risca-messages.service';
import { RiscaStorageService } from '../../../../../../../shared/services/risca/risca-storage.service';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  DynamicObjAny,
  IRiscaFormTreeParent,
  IVerifyComponeteDt,
  RiscaFormStatus,
  RiscaServerError,
} from '../../../../../../../shared/utilities';
import { QuadriTecniciPraticaConsts } from '../../../../../consts/quadri-tecnici/quadri-tecnici-pratica/quadri-tecnici-pratica.consts';
import { DatiTecniciService } from '../../../services/dati-tecnici/dati-tecnici.service';
import { QuadriTecniciService } from '../../../services/quadri-tecnici/pratica/quadri-tecnici.service';
import {
  DTPraticaConfig,
  DT_PRATICA_CONFIG,
} from '../../../utilities/configs/dt-pratica.injectiontoken';
import { DTPratica20211001Component } from '../../ambito-ambiente/version-20211001/dt-pratica/dt-pratica.component';
import { DatiTecniciTributiComponent } from '../../ambito-tributi/version-20220101/dati-tecnici-tributi/dati-tecnici-tributi.component';
import { DatiTecniciKeys } from '../../core/dati-tecnici-pratica/utilities/dati-tecnici-pratica.enums';

/**
 * Componente per la gestione del form dati tecnici.
 * Questo componente farà da gestore per i dati tecnici, visualizzando la pagina e gestendo gli eventi di conferma del form.
 */
@Component({
  selector: 'quadri-tecnici-pratica',
  templateUrl: './quadri-tecnici-pratica.component.html',
  styleUrls: ['./quadri-tecnici-pratica.component.scss'],
})
export class QuadriTecniciPraticaComponent
  extends RiscaFormParentAndChildComponent<PraticaDTVo>
  implements OnInit
{
  /** Oggetto contenente le costanti utilizzate nel componente. */
  QT_C = QuadriTecniciPraticaConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** PraticaDTVo per la pre-valorizzazione del componente dei dati tecnici (usato principalmente per il dettaglio/modifica pratica). */
  @Input('datiTecnici') configs: PraticaDTVo;
  /** number che definisce l'id del componente dt da caricare (usato principalmente per il dettaglio/modifica pratica). */
  @Input() idComponenteDt: number;

  /** Subscription che rimane in ascolto dell'evento: onServiziError. */
  private onServiziError$: Subscription;

  /** ComponenteDt che definisce l'oggetto di configurazione per caricare il componente dei dati tecnici specifico. */
  private _componenteDt: ComponenteDt;
  /** number che definisce l'id del componente dt attivo per la pratica. */
  private _idCDtLoaded: number;
  /** Oggetto contenente una coppia di chiavi valore per il caricamento dei dati tecnici della pratica. */
  private _dtPraticaComponents: DynamicObjAny;
  /** Oggetto contenente la configurazione da caricare per i dati tecnici. */
  dtComponent: any;
  /** Injector utilizzato per configurare il caricamento dinamico dei dati tecnici. */
  injectorDatiTecnici: Injector;
  /** Boolean per gestire la parte di visualizzazione dei dati del componente tecnico. */
  showDt = false;

  /** Boolean che definisce se la pagina dei dati tecnici deve risultare bloccata. */
  AEA_DTDisabled: boolean = false;

  /**
   * Costruttore
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _datiTecnici: DatiTecniciService,
    private injector: Injector,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _quadriTecnici: QuadriTecniciService,
    riscaAlert: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaMessages: RiscaMessagesService,
    private _riscaStorage: RiscaStorageService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages,
      riscaUtilities
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio la funzione di init del componente
    this.initComponente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();

    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onServiziError$) {
        this.onServiziError$.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Funzione che recupera la configurazione d'abilitazione della form
    this.setupDTDisabled();
    // Lancio il setup per la configurazione dei possibili componenti per i dati tecnici
    this.setupComponentiDatiTecnici();
    // Effettuo il setup del servizio
    this.setupFormSubmitHandlerService();
    // Effettuo la sottoscrizione agli eventi del component
    this.setupEventListeners();
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  setupDTDisabled() {
    // Recupero la chiave per la configurazione della form
    const dtKey = this.AEAK_C.PAGINA_DATI_TECNICI;
    // Recupero la configurazione della form dal servizio
    this.AEA_DTDisabled = this._accessoElementiApp.isAEADisabled(dtKey);
  }

  /**
   * Funzione di setup che imposta, all'interno del componente, i possibili dati tecnici da caricare.
   */
  private setupComponentiDatiTecnici() {
    /**
     * ############### NOTA ###############
     * Un vincolo imposto dal framework è quello di definire direttamente i componenti che potrebbero essere caricati
     * ####################################
     */
    // COMPONENTE PER: PRATICA
    this._dtPraticaComponents = {
      DatiTecniciAmbiente: DTPratica20211001Component,
      DatiTecniciTributi: DatiTecniciTributiComponent,
    };
  }

  /**
   * Funzione di setup per il servizio RiscaFormSubmitHandlerService.
   */
  private setupFormSubmitHandlerService() {
    // Definisco la chiave per il parent
    const parent = this.QT_C.FORM_KEY_PARENT;
    // Definisco le chiavi per i figli
    const children = [this.QT_C.FORM_KEY_CHILD];

    // Richiamo il super
    this.setupFormsSubmitHandler(parent, children);
  }

  /**
   * Funzione di supporto che si connette come ascoltatore agli eventi applicativi.
   */
  private setupEventListeners() {
    // Registo un ascoltatore per gli errori sui servizi
    this.onServiziError$ = this._quadriTecnici.onServiziError$.subscribe(
      (serverError: RiscaServerError) => {
        // Genero un alert sulla base dell'errore ritornato
        const a = this._riscaAlert.createAlertFromServerError(serverError);
        // Utilizzo la funzione di notifica al padre, passando l'alert di errore
        this.notifyParent(a);
      }
    );
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di init del componente.
   */
  initComponente() {
    // Lancio l'init per il recupero della configurazione del componente dt da visualizzare
    this.initComponenteDt();
    // Lancio l'init per il caricamento del componente dei dati tecnici
    this.initDatiTecnici();
  }

  /**
   * Funzione di init che definisce il componente dt da caricare per la pagina della pratica.
   */
  private initComponenteDt() {
    // Variabile di comodo
    const idCDt = this.idComponenteDt;
    const existIdCDT = idCDt != null;

    // Verifico la modalità in cui si sta gestendo il componente
    if (this.inserimento) {
      // Sono in inserimento, recupero il componente dal servizio
      this._componenteDt = this._datiTecnici.praticaInsDt;
      // #
    } else if (this.modifica && existIdCDT) {
      // Variabile di comodo
      const idCDtA = this._componenteDt?.id_componente_dt;

      // Lancio la verifica per l'attuale componente dt attivo
      const isCDTActive = this.checkComponenteDtAttivo(idCDtA, idCDt);
      // Verifico il risultato del controllo
      if (!isCDTActive) {
        // Il componente non è già attivo, sono in modifica e ho l'id del componente dt, lo recupero dal servizio
        this._componenteDt = this._datiTecnici.praticaModDtById(idCDt);
      }
      // #
    } else {
      // Resetto la variabile del componente dt
      this._componenteDt = undefined;
    }

    // Valorizzo l'id del componente dt scelto
    this._idCDtLoaded = this._componenteDt?.id_componente_dt;
  }

  /**
   * Funzione di supporto che verifica se l'attuale componente attivo è quello definito dall'input del componente.
   * @returns boolean che definisce se il componente dt già caricato è lo stesso a seguito del cambio informazioni del componente.
   */
  private checkComponenteDtAttivo(idAttivo: number, idInput: number): boolean {
    // Verifico lo stato del componente dt
    if (idAttivo == undefined) {
      // Il componente sicuramente non è attivo
      return false;
      // #
    } else {
      // Ritorno il check tra id
      return idAttivo === idInput;
    }
  }

  /**
   * Funzione di controllo e di init del componente per i dati tecnici.
   */
  private initDatiTecnici() {
    // Gestisco la variabile di visualizzazione
    this.showDt = false;

    // Verifico che sia definito un componente da caricare per i dati tecnici
    const dtComponent = this.verifyAndGetComponentDt(
      this._datiTecniciComponent,
      this._componenteDt
    );

    // La configurazione esiste, estraggo il componente
    this.dtComponent = dtComponent;
    // Definisco i parametri per il componente da caricare
    let inputs = new DTPraticaConfig();
    inputs.parentFormKey = this.QT_C.FORM_KEY_PARENT;
    inputs.childFormKey = this.QT_C.FORM_KEY_CHILD;
    inputs.modalita = this.modalita;
    inputs.configs = this.configs;
    inputs.disableUserInputs = this.disableUserInputs;

    // Definisco il nome per le opzioni
    const nameCDt = `${this._componenteDt.nome_componente_dt} | ${this._componenteDt.des_componente_dt}`;
    // Definisco le optioni per il caricamento del componente
    const options = {
      providers: [{ provide: DT_PRATICA_CONFIG, useValue: inputs }],
      parent: this.injector,
      name: nameCDt,
    };
    // Creo l'oggetto injector per il caricamento del componente
    this.injectorDatiTecnici = Injector.create(options);

    // Gestisco la variabile di visualizzazione
    this.showDt = true;
  }

  /**
   * Funzione di verifica formale sulle informazioni per i dati tecnici per il componente dt.
   * Se la verifica va a buon fine, viene ritornata la referenza del componente definita nella configurazione.
   * @param componentiLocali DynamicObjAny con i dati per verifica e recupero.
   * @param componenteDt ComponenteDt con i dati per verifica e recupero.
   * @returns any che contiene la referenza del componente definito nelle configurazioni.
   */
  private verifyAndGetComponentDt(
    componentiLocali: DynamicObjAny,
    componenteDt: ComponenteDt
  ) {
    // Creo la configurazione per la gestione dati tecnici
    const config: IVerifyComponeteDt = {
      componentiLocali,
      componenteDt,
    };

    // Richiamo il servizio di utility per il controllo e recupero del componente
    return this._riscaUtilities.verifyAndGetComponentDt(config);
  }

  /**
   * Funzione che gestisce la funzionalità di submit del form.
   * In questo caso verrà utilizzata dal componente padre per triggerare il submit dei sotto form di questo componente.
   * @override
   */
  onFormSubmit() {
    // Emetto l'evento di submit tramite servizio
    this._quadriTecnici.onFormSubmit$.next(true);
  }

  /**
   * Funzione che gestisce la funzionalità di reset del form.
   * In questo caso verrà utilizzata dal componente padre per triggerare il reset dei sotto form di questo componente.
   * @override
   */
  onFormReset() {
    // Emetto l'evento di reset tramite servizio
    this._quadriTecnici.onFormReset$.next(true);
  }

  /**
   * Funzione che imposta le informazioni per i dati tecnici.
   * @param praticaDTVo PraticaDTVo con all'interno i dati tecnici d'aggiornare.
   */
  setDatiTecnici(praticaDTVo: PraticaDTVo) {
    // Emetto l'evento di dati tecnici aggiornati
    this._quadriTecnici.aggiornaDatiTecnici(praticaDTVo);
  }

  /**
   * Funzione che imposta le informazioni per il calcolo del canone.
   * @param idRiscossione string o number che identifica una riscossione.
   * @deprecated dataRiscossione string che identifica la data di riscossione. La data deve avere formato: AAAA-MM-GG.
   */
  setDatiCalcoloCanone(
    idRiscossione: string | number
    /* dataRiscossione: string */
  ) {
    // Richiamo il servizio per la gestione dei nuovi dati calcolo canone
    this._quadriTecnici.aggiornaDatiCanone(
      idRiscossione /*, dataRiscossione */
    );
  }

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsValid.
   * In questo caso la funzionalità sarà di semplice passacarte.
   * Verranno recuperati i dati e saranno "inviati" al componente padre.
   * @param formsValid IRiscaFormTreeParent con i dati validi.
   * @override
   */
  formsValid(formsValid: IRiscaFormTreeParent) {
    // Recupero la mappa dati dei componenti
    const mapData = this.getRiscaFormTreedData(formsValid);
    // Recupero i dati tramite chiavi
    const datiTecnici = mapData.get(this.QT_C.FORM_KEY_CHILD);

    // Emetto l'evento con i dati del form
    this.onFormSubmit$.emit(datiTecnici);
    // Lancio la funzione per l'emissione dell'evento gestito tramite servizio
    this.formSubmitHandler(datiTecnici, RiscaFormStatus.valid);
  }

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsInvalid.
   * In questo caso la funzionalità sarà di semplice passacarte.
   * Verranno recuperati i messaggi d'errore e saranno "inviati" al componente padre.
   * @param formsInvalid IRiscaFormTreeParent con i dati invalidi.
   * @override
   */
  formsInvalid(formsInvalid: IRiscaFormTreeParent) {
    // Definisco un array per i messaggi di errore
    let messaggi = [];
    // Genero una mappa con gli errori da visualizzare
    const mapErr = this.getRiscaFormTreedData(formsInvalid);

    // Ciclo la mappa e concateno i messaggi d'errore
    for (let messForm of mapErr.values()) {
      // Verifico che esista un valore per la chiave
      const messFormCheck = messForm ?? [];
      // Concateno gli array
      messaggi = messaggi.concat(messFormCheck);
    }

    // Emetto l'evento comunicando i messaggi
    this.onFormErrors$.emit(messaggi);
    // Lancio la funzione per l'emissione dell'evento gestito tramite servizio
    this.formSubmitHandler(messaggi, RiscaFormStatus.invalid);
  }

  /**
   * Funzione che gestisce il recupero dei dati del mainForm.
   * Essendo che questo componente è un wrapper del vero componente che gestisce i dati tecnici, effettuo un'override della funzione.
   * Definisco le logiche per il recupero dati che farebbe la funzione di default.
   * @returns PraticaDTVo contenente l'oggetto dati generato dal formMain del componente caricato dinamicamente.
   * @override
   */
  getMainFormActualRawValue(): PraticaDTVo {
    // Recupero dalla sessione il checkpoint data del componente
    return this._riscaStorage.sGetItem(DatiTecniciKeys.checkpoint);
  }

  /**
   * ###############
   * GETTER E SETTER
   */

  get existComponenteDt() {
    // Verifico che _componenteDt esista
    return this._componenteDt != null;
  }

  /**
   * Getter dell'id del componente dt caricato per la pratica.
   * Viene utilizzato dal padre, durante l'inserimento, per sapere quale componente è stato utilizzato per i dati tecnici della pratica.
   */
  get idCDtLoaded() {
    return this._idCDtLoaded;
  }

  /**
   * Getter che recupera l'informazione contestuale rispetto alle configurazioni in input del componente.
   */
  private get _datiTecniciComponent(): DynamicObjAny {
    // Ritorno la configurazione specifica
    return this._dtPraticaComponents;
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.isPraticaLocked;
  }

  /**
   * Getter di comodo che verifica se la pratica è lockata.
   * @returns boolean con il risultato del check.
   */
  get isPraticaLocked(): boolean {
    // Verifico che esista nel servizio la configurazione del lock pratica
    const existLockStatus = this._riscaLockP.isPraticaInLockStatus();
    // Verifico se un altro utente sta lockando la pratica
    const anotherUserLocker = this._riscaLockP.isAnotherUserLockingPratica();
    // Ritorno le condizioni di disabilitazione
    return existLockStatus && anotherUserLocker;
  }
}
