import {
  Component,
  EventEmitter,
  Injector,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { Subscription } from 'rxjs';
import { AnnualitaSDVo } from '../../../../../../../core/commons/vo/annualita-sd-vo';
import { ComponenteDt } from '../../../../../../../core/commons/vo/componente-dt-vo';
import {
  PraticaDTVo,
  PraticaEDatiTecnici,
} from '../../../../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../../../../core/commons/vo/stato-debitorio-vo';
import { IUsoLeggeVo } from '../../../../../../../core/commons/vo/uso-legge-vo';
import { AccessoElementiAppKeyConsts } from '../../../../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormParentAndChildComponent } from '../../../../../../../shared/components/risca/risca-form-parent-and-child/risca-form-parent-and-child.component';
import { RiscaAlertService } from '../../../../../../../shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaMessagesService } from '../../../../../../../shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  DynamicObjAny,
  IRiscaFormTreeParent,
  IVerifyComponeteDt,
  RiscaFormStatus,
} from '../../../../../../../shared/utilities';
import { QuadriTecniciSDConsts } from '../../../../../consts/quadri-tecnici/quadri-tecnici-sd/quadri-tecnici-sd.consts';
import { DatiTecniciEventsService } from '../../../services/dati-tecnici/dati-tecnici-events.service';
import { DatiTecniciService } from '../../../services/dati-tecnici/dati-tecnici.service';
import { QuadriTecniciSDService } from '../../../services/quadri-tecnici/stato-debitorio/quadri-tecnici-sd.service';
import {
  DTAnnualitaConfig,
  DT_ANNUALITA_CONFIG,
} from '../../../utilities/configs/dt-annualita.injectiontoken';
import { DTAmbienteSDAnnualitaComponent } from '../../ambito-ambiente/version-20211001/dt-ambiente-sd-annualita/dt-ambiente-sd-annualita.component';
import { DTTributiSDAnnualitaComponent } from '../../ambito-tributi/version-20220101/dt-tributi-sd-annualita/dt-tributi-sd-annualita.component';

/**
 * Componente per la gestione del form dati tecnici.
 * Questo componente farà da gestore per i dati tecnici, visualizzando la pagina e gestendo gli eventi di conferma del form.
 */
@Component({
  selector: 'quadri-tecnici-sd',
  templateUrl: './quadri-tecnici-sd.component.html',
  styleUrls: ['./quadri-tecnici-sd.component.scss'],
})
export class QuadriTecniciSDComponent
  extends RiscaFormParentAndChildComponent<PraticaDTVo | string>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti utilizzate nel componente. */
  QT_SD_C = QuadriTecniciSDConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** Subscription collegato all'evento: json_regola_mancante per un uso annualità. */
  private onUsoAnnualitaJRM$: Subscription;
  /** Subscription collegato all'evento: json_regola_mancante per gli usi annualità. */
  private onUsiAnnualitaJRM$: Subscription;
  /** Subscription collegato all'evento: json_regola_mancante per il canone annualità. */
  private onCanoneAnnualitaJRM$: Subscription;
  /** EventEmitter che propaga al componente padre l'evento aggiornamento con "pulizia" dati dal componente. */
  private onClean$: Subscription;

  /** PraticaEDatiTecnici per la pre-valorizzazione del componente dei dati tecnici, partendo dal dato tecnico della pratica. */
  @Input('praticaEDatiTecnici') praticaEDatiTecnici: PraticaEDatiTecnici;
  /** Input dataInserimentoPratica che definisce la data in cui è stata inserita la pratica. */
  @Input('dataInserimentoPratica') dataInserimentoPratica: string;
  /** Input string che definisce la configurazione dati dello stato debitorio d'interesse. */
  @Input('statoDebitorio') statoDebitorio: StatoDebitorioVo;
  /** AnnualitaSDVo per la pre-valorizzazione del componente dei dati tecnici, partendo dal dato tecnico dell'annualità. */
  @Input('annualitaDT') annualitaDT: AnnualitaSDVo;
  /** Number che definisce l'id del componente dt da caricare. */
  @Input() idComponenteDt: number;
  /** Boolean che definisce se lo stato debitorio ha già un'annualità con il flag rateo prima annualità settato. */
  @Input('rateoPrimaAnnualita') rateoPrimaAnnualita: boolean = false;

  /** EventEmitter che propaga al componente padre l'evento di: json_regola_mancante per un uso annualità. */
  @Output() onUsoAnnualitaJRM = new EventEmitter<IUsoLeggeVo>();
  /** EventEmitter che propaga al componente padre l'evento di: json_regola_mancante per gli usi annualità. */
  @Output() onUsiAnnualitaJRM = new EventEmitter<IUsoLeggeVo[]>();
  /** EventEmitter che propaga al componente padre l'evento di: json_regola_mancante per il canone annualità. */
  @Output() onCanoneAnnualitaJRM = new EventEmitter<any>();
  /** EventEmitter che propaga al componente padre l'evento aggiornamento con "pulizia" dati dal componente. */
  @Output() onClean = new EventEmitter<any>();

  /** ComponenteDt che definisce l'oggetto di configurazione per caricare il componente dei dati tecnici specifico. */
  private _componenteDt: ComponenteDt;
  /** number che definisce l'id del componente dt attivo per la pratica. */
  private _idCDtLoaded: number;
  /** Oggetto contenente una coppia di chiavi valore per il caricamento dei dati tecnici dello stato debitorio, scheda annualita. */
  private _dtSDAnnualitaComponents: DynamicObjAny;
  /** Oggetto contenente la configurazione da caricare per i dati tecnici. */
  dtComponent: any;
  /** Injector utilizzato per configurare il caricamento dinamico dei dati tecnici. */
  injectorDatiTecniciSD: Injector;
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
    private _dtEvents: DatiTecniciEventsService,
    private injector: Injector,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _quadriTecniciSD: QuadriTecniciSDService,
    riscaAlert: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaMessages: RiscaMessagesService,
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

    // Richiamo le funzioni di destroy di questo componente
    try {
      if (this.onUsoAnnualitaJRM$) {
        this.onUsoAnnualitaJRM$.unsubscribe();
      }
      if (this.onUsiAnnualitaJRM$) {
        this.onUsiAnnualitaJRM$.unsubscribe();
      }
      if (this.onCanoneAnnualitaJRM$) {
        this.onCanoneAnnualitaJRM$.unsubscribe();
      }
      if (this.onClean$) {
        this.onClean$.unsubscribe();
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
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  setupDTDisabled() {
    // Recupero la chiave per la configurazione della form
    const dtKey = this.AEAK_C.PAGINA_DATI_TECNICI;
    const detSDKey = this.AEAK_C.DET_STATO_DEB;

    // Verifico se la configurazione di accesso agli elementi è attivo
    let AEA_DTDisabled: boolean;
    let AEA_detSDDisabled: boolean;
    AEA_DTDisabled = this._accessoElementiApp.isAEADisabled(dtKey);
    AEA_detSDDisabled = this._accessoElementiApp.isAEADisabled(detSDKey);

    // Definisco la configurazione di disabilitazione per i dati tecnici
    this.AEA_DTDisabled = AEA_DTDisabled || AEA_detSDDisabled;
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
    // COMPONENTI PER: STATO DEBITORIO - ANNUALITA'
    this._dtSDAnnualitaComponents = {
      DatiTecniciAmbiente: DTAmbienteSDAnnualitaComponent,
      DatiTecniciTributi: DTTributiSDAnnualitaComponent,
    };
  }

  /**
   * Funzione di setup per il servizio RiscaFormSubmitHandlerService.
   */
  private setupFormSubmitHandlerService() {
    // Definisco la chiave per il parent
    const parent = this.QT_SD_C.FORM_KEY_PARENT;
    // Definisco le chiavi per i figli
    const children = [this.QT_SD_C.FORM_KEY_CHILD];

    // Richiamo il super
    this.setupFormsSubmitHandler(parent, children);
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
    // Lancio l'init per gli ascoltatori del componente
    this.initListeners();
    // Lancio l'init per il recupero della configurazione del componente dt da visualizzare
    this.initComponenteDt();
    // Lancio l'init per il caricamento del componente dei dati tecnici
    this.initDatiTecnici();
  }

  /**
   * Funzione di init che aggancia gli ascoltati del componente agli eventi applicativi.
   */
  private initListeners() {
    // Registro gli gli ascoltatori agli eventi
    this.onUsoAnnualitaJRM$ = this._dtEvents.onUsoAnnualitaJRM$.subscribe({
      next: (usoJRM: IUsoLeggeVo) => {
        // Uso annalità senza json regola
        this.onUsoAnnualitaJRM.emit(usoJRM);
      },
    });
    this.onUsiAnnualitaJRM$ = this._dtEvents.onUsiAnnualitaJRM$.subscribe({
      next: (usiJRM: IUsoLeggeVo[]) => {
        // Usi annalità senza json regola
        this.onUsiAnnualitaJRM.emit(usiJRM);
      },
    });
    this.onCanoneAnnualitaJRM$ = this._dtEvents.onCanoneAnnualitaJRM$.subscribe(
      {
        next: (info: any) => {
          // Canone annualita senza json regola
          this.onCanoneAnnualitaJRM.emit(info);
        },
      }
    );
    this.onClean$ = this._dtEvents.onClean$.subscribe({
      next: (info: any) => {
        // Emetto l'evento di clean dati
        this.onClean.emit(info);
      },
    });
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
      this.idComponenteDt = this._componenteDt.id_componente_dt;
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
    let inputs = new DTAnnualitaConfig();
    inputs.parentFormKey = this.QT_SD_C.FORM_KEY_PARENT;
    inputs.childFormKey = this.QT_SD_C.FORM_KEY_CHILD;
    inputs.modalita = this.modalita;
    inputs.praticaEDatiTecnici = this.praticaEDatiTecnici;
    inputs.dataInserimentoPratica = this.dataInserimentoPratica;
    inputs.statoDebitorio = this.statoDebitorio;
    inputs.annualitaDT = this.annualitaDT;
    inputs.idComponenteDt = this.idComponenteDt;
    inputs.rateoPrimaAnnualita = this.rateoPrimaAnnualita;
    inputs.disableUserInputs = this.disableUserInputs;

    // Definisco il nome per le opzioni
    const nameCDt = `${this._componenteDt.nome_componente_dt} | ${this._componenteDt.des_componente_dt}`;
    // Definisco le optioni per il caricamento del componente
    const options = {
      providers: [{ provide: DT_ANNUALITA_CONFIG, useValue: inputs }],
      parent: this.injector,
      name: nameCDt,
    };
    // Creo l'oggetto injector per il caricamento del componente
    this.injectorDatiTecniciSD = Injector.create(options);

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
    this._quadriTecniciSD.onFormSubmit.next(true);
  }

  /**
   * Funzione che gestisce la funzionalità di reset del form.
   * In questo caso verrà utilizzata dal componente padre per triggerare il reset dei sotto form di questo componente.
   * @override
   */
  onFormReset() {
    // Emetto l'evento di reset tramite servizio
    this._quadriTecniciSD.onFormReset.next(true);
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
    this._quadriTecniciSD.aggiornaDatiCanone(
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
    const datiTecnici = mapData.get(this.QT_SD_C.FORM_KEY_CHILD);

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
   * ###############
   * GETTER E SETTER
   * ###############
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
    return this._dtSDAnnualitaComponents;
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.isPraticaLocked || this.AEA_DTDisabled;
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
