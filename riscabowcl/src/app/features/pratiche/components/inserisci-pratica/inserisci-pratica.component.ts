import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgbNav, NgbNavChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { clone } from 'lodash';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import {
  PraticaDTDataVo,
  PraticaDTVo,
  PraticaEDatiTecnici,
  PraticaVo,
} from '../../../../core/commons/vo/pratica-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import {
  IJourneySnapshot,
  IJourneyStep,
  IJToStep,
} from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaSelectService } from '../../../../shared/services/form-inputs/risca-select/risca-select.service';
import { RiscaSpinnerService } from '../../../../shared/services/risca-spinner.service';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { IRichiestaLockPraticaRes } from '../../../../shared/services/risca/risca-lock-pratica/utilities/risca-lock-pratica.interfaces';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import { RiscaStampaPraticaService } from '../../../../shared/services/risca/risca-stampa/risca-stampa-pratica.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  AppCallers,
  DatiAnagrafici,
  GeneraliAmministrativi,
  IAlertConfigs,
  IRFCReqJourneyNavigation,
  IRiscaFormTreeParent,
  IRiscaTabChanges,
  PraticaCorrelati,
  PraticaCorrelatiRes,
  PraticaForms,
  RiscaFruitori,
  RiscaInfoLevels,
  RiscaIstanzePratica,
  RiscaServerError,
  ServerStringAsBoolean,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IHomeRouteParams } from '../../../home/components/home/utilities/home.interfaces';
import { InserisciPraticaNavClass } from '../../class/navs/inserisci-pratica.nav.class';
import { InserisciPraticaConsts } from '../../consts/inserisci-pratica/inserisci-pratica.consts';
import { SoggettoDatiAnagraficiService } from '../../service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { DocumentiAllegatiService } from '../../service/documenti-allegati/documenti-allegati.service';
import { IParamsDocumentiAllegati } from '../../service/documenti-allegati/utilities/documenti-allegati.interfaces';
import { InserisciPraticaService } from '../../service/inserisci-pratica/inserisci-pratica.service';
import { DatiAnagraficiComponent } from '../dati-anagrafici/dati-anagrafici.component';
import { DatiContabiliComponent } from '../dati-contabili/dati-contabili/dati-contabili.component';
import { GeneraliAmministrativiComponent } from '../generali-amministrativi/generali-amministrativi.component';
import { QuadriTecniciPraticaComponent } from '../quadri-tecnici/components/middlewares/quadri-tecnici-pratica/quadri-tecnici-pratica.component';
import { QuadriTecniciService } from '../quadri-tecnici/services/quadri-tecnici/pratica/quadri-tecnici.service';
import { DatiTecniciAmbienteVo } from '../quadri-tecnici/utilities/vo/dati-tecnici-ambiente-vo';
import { IIPConfigs } from './utilities/inserisci-pratica.interfaces';

@Component({
  selector: 'inserisci-pratica',
  templateUrl: './inserisci-pratica.component.html',
  styleUrls: ['./inserisci-pratica.component.scss'],
})
export class InserisciPraticaComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy
{
  /** Oggetto con le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti del componente. */
  IP_C = InserisciPraticaConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** any che contiene le configurazioni del componente. */
  @Input('inserisciPraticaConfigs') iIPConfigs: IIPConfigs;

  /** ViewChild collegato al componente: app-generali-amministrativi. */
  @ViewChild('appGenAmm') appGenAmm: GeneraliAmministrativiComponent;
  /** ViewChild collegato al componente: quadri-tecnici. */
  @ViewChild('appQuadriTecnici')
  appQuadriTecnici: QuadriTecniciPraticaComponent;
  /** ViewChild collegato al componente: app-dati-anagrafici. */
  @ViewChild('appDatiAnagrafici') appDatiAnagrafici: DatiAnagraficiComponent;
  /** ViewChild collegato al componente: dati-contabili */
  @ViewChild('appDatiContabili') datiCont: DatiContabiliComponent;

  /** ViewChild collegato alla navigation bar. */
  @ViewChild('inserisciPraticaNav') inserisciPraticaNav: NgbNav;

  /** PraticaEDatiTecnici che definisce la configurazione base per i dati della pratica. */
  praticaEDatiTecnici: PraticaEDatiTecnici;
  /** number che definisce l'id del componente tecnico salvato per la pratica. */
  idComponenteDt: number;

  /** Oggetto GeneraliAmministrativi contenente i dati per la sezione: dati generali amministrativi. */
  datiGenAmm: GeneraliAmministrativi;
  /** Oggetto PraticaDTVo contenente i dati per la sezione: dati tecnici. */
  datiTecnici: PraticaDTVo;
  /** Oggetto DatiAnagrafici contenente i dati per la sezione: dati anagrafici. */
  datiAnag: DatiAnagrafici;

  /** Oggetto GeneraliAmministrativi contenente i dati per la sezione: dati generali amministrativi. */
  datiGenAmmConfigs: GeneraliAmministrativi;
  /** Oggetto PraticaDTVo contenente i dati per la sezione: dati tecnici. */
  datiTecniciConfigs: PraticaDTVo;
  /** Oggetto DatiAnagrafici contenente i dati per la configurazione della sezione: dati anagrafici. */
  datiAnagraficiConfigs: DatiAnagrafici;
  /** Oggetto SoggettoVo contenente i dati per la sezione: dati anagrafici. */
  datiAnagraficiMsgConfigs: IAlertConfigs;

  /** RiscaIstanzePratica che definisce il target dell'istanza della partica. */
  istanzaPratica: RiscaIstanzePratica;
  /** InserisciPraticaNavClass per gestire le praticaNav. */
  istanzaPraticaNav: InserisciPraticaNavClass;

  /** Oggetto che definisce il layout per l'alert di comunicazione principale. */
  private alertConfigsStyle = { 'margin-bottom': '40px' };

  /**
   * Costruttore.
   */
  constructor(
    private _docAllegati: DocumentiAllegatiService,
    private _inserisciPratica: InserisciPraticaService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _quadriTecnici: QuadriTecniciService,
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    private _riscaModal: RiscaModalService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaSelect: RiscaSelectService,
    private _riscaStampaP: RiscaStampaPraticaService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _soggettoDA: SoggettoDatiAnagraficiService,
    private _spinner: RiscaSpinnerService,
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
    // Imposto le configurazioni per lo step di journey
    this.stepConfig = InserisciPraticaConsts.NAVIGATION_CONFIG;

    // Effettuiamo l'override del back del browser
    this.setupBrowserRouting();
    // Richiamo il setup per il componente
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponent();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * Setta la funzione di override del back del browser
   */
  private setupBrowserRouting() {
    // Verifico se esiste uno step (che potenzialmente ha richiamato l'inserisci-pratica)
    if (this.tornaIndietroEnabled) {
      // Definisco una funzione custom per la gestione del back del browser
      const back = () => {
        // Richiamo la funzione locale
        this.tornaIndietro();
      };
      // Registro una funzione custom per il back del browser
      this._navigationHelper.setBrowserBack(back);
      // Sovrascrivo la funzione di default del back con la funzione sopra definita
      this._navigationHelper.overrideBrowserBack();
    }
  }

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
    // Aggiungo lo stile all'oggetto dell'alert
    this.alertConfigs.containerCss = this.alertConfigsStyle;

    // Reset del behavior subject che gestisce i dati del calcolo canone
    this.resetOnDatiCalcoloCanone();
    // Setup della nav tabs
    this.setupNavTabs();
    // Effettuo il setup del servizio di submit handler
    this.setupFormSubmitHandlerService();
    // Dopo la registrazione per il servizio di handler, richiamo la registrazione per i submit da parte dei child
    this.listenToChildSubmit();
  }

  /**
   * Funzione di reset iniziale, che reimposta la struttura dedicata alla comunicazione dei dati per il calcolo canone.
   */
  private resetOnDatiCalcoloCanone() {
    // Funzione che effettua un refresh della struttura dati per il calcolo canone
    this._quadriTecnici.resetOnDatiCalcoloCanone();
  }

  /**
   * Funzione di setup per i dati della nav tabs.
   */
  private setupNavTabs() {
    // Creo l'oggetto per le nav
    this.istanzaPraticaNav = new InserisciPraticaNavClass();
    // Definisco l'istanza iniziale
    this.setManualNav();
  }

  /**
   * Funzione di setup per il servizio RiscaFormSubmitHandlerService.
   */
  private setupFormSubmitHandlerService() {
    // Definisco la chiave per il parent
    const parent = this.IP_C.FORM_KEY_PARENT;
    // Definisco le chiavi per i figli
    const children = [
      this.IP_C.FORM_KEY_CHILD_DGA,
      this.IP_C.FORM_KEY_CHILD_DA,
      this.IP_C.FORM_KEY_CHILD_DT,
    ];

    // Richiamo il super
    this.setFormsSubmitHandler(parent, children);
  }

  /**
   * Funzione di init del componente.
   */
  private initComponent() {
    // Definisco la modalità di defualt del componente
    this.initModalita();
    // Verifico se esiste uno snapshot definito dalla navigazione tramite journey
    this.loadJSnapshot(this);
    // Effettuo l'init dei dati passati alla pagina
    this.initIPConfigs();
  }

  /**
   * Funzione di comodo che gestisce il default della modalità del componente.
   */
  private initModalita() {
    // Verifico se esiste una modalità passata in input
    if (this.modalita == undefined) {
      // Imposto come dafault l'inserimento
      this.modalita = AppActions.inserimento;
    }
  }

  /**
   * Funzione che verifica se tramite il servizio di Route sono stati passati parametri di configurazione.
   */
  private initIPConfigs() {
    // Verifico esistano configurazioni
    if (!this.iIPConfigs) {
      // Niente configurazioni
      return;
    }

    // Recupero l'oggetto configs
    const configs = this.iIPConfigs;
    // Verifico se esiste l'oggetto
    if (!this.datiAnagraficiConfigs) {
      // Definisco un oggetto senza dati
      this.setEmptyDatiAnagraficiConfigs();
    }
    // Attivo l'init delle configurazioni passate al componente
    this.initComponenteConfigs(configs);
  }

  /**
   * Funzione di supporto che verifica e carica i dati del componente in base alla configurazione e ai dati della snapshot.
   * @param routeParams IIPConfigs contenente i dati di configurazione del componente.
   */
  private initComponenteConfigs(routeParams: IIPConfigs) {
    // Recupero i valori
    const {
      navTarget,
      modalita,
      praticaEDatiTecnici,
      soggettoInsert,
      soggettoMessaggi,
      gruppoInsert,
      gruppoUpdate,
    } = routeParams;

    // Verifico se è stata definita la modalita
    if (modalita) {
      // Aggiorno la modalita d'azione
      this.modalita = modalita;
    }
    // Verifico se è stato definito un target per le navs
    if (navTarget) {
      // Imposto il nav come attivo
      this.setManualNav(navTarget);
    }
    // Verifico se é stata passata una pratica
    if (praticaEDatiTecnici) {
      // Recupero i dati per pre-valorizzare i navs della pratica
      this.initPratica(praticaEDatiTecnici);
      // [RISCA-ISSUES-37] Lancio la funzione di scarico dei documenti allegati
      this.initDocumentiAllegati(praticaEDatiTecnici);
    }
    // Verifico se ci sono dati per il soggetto da pre-valorizzare
    if (soggettoInsert) {
      // Assegno i dati alla configurazione
      this.datiAnagraficiConfigs.soggettoPratica = soggettoInsert;
      // Rimuovo l'oggetto del gruppo, perché se è stato inserito, vuol dire che non ci sarà un gruppo
      this.datiAnagraficiConfigs.gruppoPratica = undefined;
    }
    // Verifico se esistono messaggi
    if (soggettoMessaggi) {
      // Assegno i dati alla configurazione
      this.datiAnagraficiMsgConfigs = soggettoMessaggi;
    }

    // Verifico se c'è un gruppoInsert o un gruppoUpdate
    const gruppoState = gruppoInsert || gruppoUpdate || false;
    // Verifico se è stato inserito un gruppo
    if (gruppoState) {
      // Lancio la funzione per la gestione del gruppo
      this.gestisciDatiGruppo(gruppoState);
      // Inserisco nella configurazione il dato del gruppo
      this.datiAnagraficiConfigs.gruppoPratica = gruppoState;
    }
  }

  /**
   * Funzione specifica per la gestione dell'aggiornamento di un gruppo.
   * E' stata aperta la pagina d'inserimento pratica passando un gruppo in aggiornamento.
   * Potrebbero essereci stati delle modifiche per cui, in questo modo, si va ad aggiornare le informazioni per il componente dei dati anagrafici.
   * @param g Gruppo aggiornato.
   */
  gestisciDatiGruppo(g: Gruppo) {
    // Verifico l'input
    if (!g) {
      return;
    }

    // Recupero i dati anagrafici di configurazione
    const da = this.datiAnagraficiConfigs;
    // Recupero l'id del capogruppo
    const { idSC } = this.recuperaIdCPEIdSC(da, g);
    // Richiamo la funzione d'allineamento dei dati
    this.gestisciAllineamentoDatiAnagrafici(idSC);
  }

  /**
   * Funzione di comodo che gestisce l'allineamento delle informazioni tra capogruppo e dati del soggetto della pratica.
   * @param idSC number l'id del soggetto capogruppo.
   */
  private gestisciAllineamentoDatiAnagrafici(idSC: number) {
    // Verifico se esiste l'oggetto
    if (!this.datiAnagraficiConfigs) {
      // Definisco un oggetto senza dati
      this.setEmptyDatiAnagraficiConfigs();
    }
    // Devo scaricare il soggetto e aggiornarlo (il soggetto della pratica DEVE essere il capogruppo)
    this._soggettoDA.getSoggetto(idSC).subscribe({
      next: (soggetto: SoggettoVo) => {
        // Aggiorno il soggetto nella configurazione
        this.datiAnagraficiConfigs.soggettoPratica = soggetto;
        // Creo un clone per l'aggiornamento della referenza,
        const dac = clone(this.datiAnagraficiConfigs);
        // Riassegno l'oggetto per l'aggiornamento della referenza
        this.datiAnagraficiConfigs = dac;
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la gestione degli errori
        this.onServiziError(e);
        // #
      },
    });
  }

  /**
   * Se ho una pratica in arrivo in ingresso, qui la trasformo per avere un oggetto compatibile.
   * @param pedt PraticaEDatiTecnici dalla quale estrarre le informazioni per la generazione dei dati delle tab.
   */
  private initPratica(pedt: PraticaEDatiTecnici) {
    // Avvio lo spinner
    this._spinner.show();
    // Genero i dati generali amministrativi dalla pratica
    this.initPraticaAsync(pedt).subscribe({
      next: (res: boolean) => {
        // Nascondo lo spinner
        this._spinner.hide();
      },
      error: (e: RiscaServerError) => {
        // Nascondo lo spinner
        this._spinner.hide();
      },
    });
  }

  /**
   * Se ho una pratica in arrivo in ingresso, qui la trasformo per avere un oggetto compatibile.
   * @param pedt PraticaEDatiTecnici dalla quale estrarre le informazioni per la generazione dei dati delle tab.
   */
  private initPraticaAsync(pedt: PraticaEDatiTecnici): Observable<boolean> {
    // Assegno localmente le informazioni per i dati della pratica
    this.praticaEDatiTecnici = pedt;
    this.idComponenteDt = this.praticaIdCDt;

    // Recupero i dati passati
    const { pratica, datiTecnici } = pedt;
    // Genero i dati anagrafici dalla pratica
    const da = this._inserisciPratica.convertPraticaVoToDatiAnagrafici(pratica);
    // Assegno localmente le informazioni
    this.datiAnagraficiConfigs = da;
    this.datiTecniciConfigs = datiTecnici;
    // Imposto i valori per il calcolo canone
    this.setDatiCalcoloCanone(datiTecnici);

    // Genero i dati generali amministrativi dalla pratica
    return this.convertPraticaVoToGeneraliAmministrativi(pratica).pipe(
      map((gm: GeneraliAmministrativi) => {
        // Operazione andata a buon fine
        return true;
      })
    );
  }

  /**
   * Funzione che richiama la conversione per i dati da PraticaVo a GeneraliAmministrativi.
   * @param pratica PraticaVo da convertire.
   * @returns Observable<GeneraliAmministrativi> con i dati convertiti.
   */
  private convertPraticaVoToGeneraliAmministrativi(
    pratica: PraticaVo
  ): Observable<GeneraliAmministrativi> {
    // Genero i dati generali amministrativi dalla pratica
    return this._inserisciPratica
      .convertPraticaVoToGeneraliAmministrativi(pratica)
      .pipe(
        tap((gm: GeneraliAmministrativi) => {
          // Assegno localmente i dati
          this.datiGenAmmConfigs = gm;
        })
      );
  }

  /**
   * Se ho una pratica in arrivo in ingresso, lancio lo scarico dei documenti allegati senza spinner.
   * @param pedt PraticaEDatiTecnici dalla quale estrarre le informazioni per la generazione dei dati delle tab.
   */
  private initDocumentiAllegati(pedt: PraticaEDatiTecnici) {
    // Estraggo le informazioni per il caricamento dei dati per i documenti allegati
    const { pratica } = pedt ?? {};
    const idPratica = pratica?.id_riscossione;
    // Verifico che esista l'id riscossione e che sono in modifica
    if (idPratica == undefined || !this.isModifica) {
      // Non sono nelle condizioni per effettuare la ricerca, blocco il flusso
      return;
    }

    // Definisco i parametri per la chiamata
    const queryParams: IParamsDocumentiAllegati = {
      // [RISCA-ISSUE-36]: Per il fruitore verifico se l'utente in linea è un CONSULTATORE
      fruitore: this._user.isCONSULTATORE ? RiscaFruitori.RISCA : undefined,
    };

    // Lancio la chiamata per lo scarico delle informazioni dei documenti allegati
    this._docAllegati.avviaRicercaDocEAllLocale(idPratica, queryParams);
  }

  /**
   * #######################
   * GESTIONE DELLE NAV TABS
   * #######################
   */

  /**
   * Funzione agganciata all'evento di cambio della tab per la nav bar.
   * @param nav NgbNavChangeEvent che definisce la nuova istanza.
   */
  onNavPraticaChange(nav: NgbNavChangeEvent) {
    // Recupero dall'evento di cambio nav le informazioni
    const { activeId, nextId } = nav;
    // Emetto l'evento per informare i tab della pratica che sta avvenendo un cambio
    this.emitNavPraticaChange(activeId, nextId);
  }

  /**
   * Funzione che aggiorna l'istanza selezionata dall'utente, modificando manualmente la tab attiva.
   * @param istanza RiscaIstanzePratica che definisce la nuova istanza.
   */
  private navPraticaChange(istanza: RiscaIstanzePratica) {
    // Recupero l'attuale istanza della pratica
    const actual = this.inserisciPraticaNav.activeId;
    // L'input sarà il target della prossima tab
    const next = istanza;

    // Modifico il flag per l'istanza
    this.istanzaPratica = istanza;
    // Aggiorno la visualizzazione dell'istanza per la nav
    this.inserisciPraticaNav.select(this.istanzaPratica);
    // Emetto l'evento per informare i tab della pratica che sta avvenendo un cambio
    this.emitNavPraticaChange(actual, next);
  }

  /**
   * Funzione di comodo che crea i dati ed emette l'evento di cambio tab della pratica.
   * @param actual RiscaIstanzePratica definisce l'id del tab attualmente attivo.
   * @param next RiscaIstanzePratica che definisce l'id del target della tab.
   */
  private emitNavPraticaChange(
    actual: RiscaIstanzePratica,
    next: RiscaIstanzePratica
  ) {
    // Definisco l'oggetto per il tab changes
    const tabs: IRiscaTabChanges = { actual, next };

    // Lancio la funzione di gestione per il filo d'arianna
    this.gestisciFiloArianna(tabs);
    // Emetto l'evento per informare i tab della pratica che sta avvenendo un cambio
    this._inserisciPratica.inserisciPraticaTabChanged(tabs);
  }

  /**
   * Funzione che gestisce il filo d'arianna per la parte dedicata e specifica della pratica.
   * @param tabs IRiscaTabChanges con le informazioni di spostamento tra le tab della pratica.
   */
  private gestisciFiloArianna(tabs: IRiscaTabChanges) {
    // Verifico dove ci stiamo spostando
    const movingToGA = this.movingToGA(tabs);
    const movingToDA = this.movingToDA(tabs);
    const movingToDT = this.movingToDT(tabs);
    const movingToDC = this.movingToDC(tabs);
    const movingToDOC = this.movingToDOC(tabs);

    // Verifico le macro condizioni di spostamento tra tab
    if (movingToGA || movingToDA || movingToDT || movingToDOC) {
      // Rimuovo dal filo d'arianna il possibile segmento dati contabili/documenti allegati
      this.rimuoviSegmentoDC();
      // #
    } else if (movingToDC) {
      // Aggiungo al filo d'arianna il segmento per i dati contabili
      const dtFA: FALivello = this._riscaFA.datiContabili;
      this._riscaFA.aggiungiSegmentoByLivelli(dtFA);
      // #
    }
  }

  /**
   * Funzione di supporto per la gestione del filo d'arianna per rimuovere il segemento dati contabili.
   */
  private rimuoviSegmentoDC() {
    // Rimuovo dal filo d'arianna il possibile segmento dati contabili
    const dcFA: FALivello = this._riscaFA.datiContabili;
    // Ricerco il segmento
    const segmentoDC = this._riscaFA.segmentoInFAByLivello(dcFA);
    // Cerco di rimuovere il segmento
    this._riscaFA.rimuoviSegmento(segmentoDC);
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: generali-amministrativi.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  private movingToGA(tabs: IRiscaTabChanges): boolean {
    // Lancio la funzione di verifica
    return this._inserisciPratica.movingToGeneraliAmministrativi(tabs);
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: dati anagrafici.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  private movingToDA(tabs: IRiscaTabChanges): boolean {
    // Lancio la funzione di verifica
    return this._inserisciPratica.movingToDatiAnagrafici(tabs);
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: dati tecnici.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  private movingToDT(tabs: IRiscaTabChanges): boolean {
    // Lancio la funzione di verifica
    return this._inserisciPratica.movingToDatiTecnici(tabs);
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: dati contabili.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  private movingToDC(tabs: IRiscaTabChanges): boolean {
    // Lancio la funzione di verifica
    return this._inserisciPratica.movingToDatiContabili(tabs);
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: documenti allegati.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  private movingToDOC(tabs: IRiscaTabChanges): boolean {
    // Lancio la funzione di verifica
    return this._inserisciPratica.movingToDocumentiAllegati(tabs);
  }

  /**
   * ######################################
   * AZIONI TRAMITE PULSANTI DEL COMPONENTE
   * ######################################
   */

  /**
   * Funzione in ascolto del submit dei child.
   * Al momento viene solo usato per i dati anagrafici a seguito della correzione degli indirizzi di spedizione quando viene salvato il soggetto.
   * @override
   */
  submitFromChild() {
    // Richiamo di nuovo il salva della pratica
    this.salvaIstanzaPratica();
  }

  /**
   * Funzione invocata al click del pulsante SALVA.
   */
  salvaIstanzaPratica() {
    // Lancio la richiesta di salvataggio delle form
    this.appGenAmm.onFormSubmit();
    this.appDatiAnagrafici.onFormSubmit();
    this.appQuadriTecnici.onFormSubmit();
  }

  /**
   * Funzione che chiede all'utente se vuole resettare tutti i dati per tutte le form della pratica.
   */
  annullaIstanzaPratica() {
    // Dichiaro la funzione di callback
    const onConfirm = () => {
      // Definisco le logiche di reset
      const resetPratica = () => {
        // Richiamo il reset delle form
        this.resetSubForms();
        // Ripristino il focus sul primo tab
        this.navPraticaChange(this.istanzaPraticaNav.IST_PRT_GEN_AMM);
      };

      // Verifico la modalita
      if (this.modifica) {
        // Avvio lo spinner
        this._spinner.show();

        // Richiamo il reset dei dati partendo dalla pratica
        this.initPraticaAsync(this.praticaEDatiTecnici).subscribe({
          next: (res: boolean) => {
            // Dati ripristinati dalla pratica, ora il reset dei componenti
            resetPratica();
            // Nascondo lo spinner
            this._spinner.hide();
          },
          error: (e: RiscaServerError) => {
            // Nascondo lo spinner
            this._spinner.hide();
          },
        });
      } else {
        // Siamo in inserimento, niente da ripristinare
        resetPratica();
      }
    };

    // Richiamo la funzione del servizio per la modale di conferma annullamento
    this._riscaModal.apriModalConfermaAnnullamento({ onConfirm });
  }

  /**
   * Resetta le subform
   */
  private resetSubForms() {
    this.appGenAmm.onFormReset();
    this.appDatiAnagrafici.onFormReset();
    this.appQuadriTecnici.onFormReset();
  }

  /**
   * Funzione agganciata al pulsante stampa pratica.
   * Gestisce la logica quando viene avviata la stampa.
   */
  onStampaAvviata() {
    // Resetto possibili alert d'errore
    this.resetAlertConfigs();
  }

  /**
   * Funzione agganciata al pulsante stampa pratica.
   * Gestisce la logica quando la stampa va in errore.
   * @param error RiscaServerError con il dettaglio dell'errore generato.
   */
  onStampaError(error: RiscaServerError) {
    // Lancio la gestione dell'errore
    this.onServiziError(error);
  }

  /**
   * #############################################################
   * GESTIONE LOGICA PER IL SUBMIT DELLE FORM DEI COMPONENTI FIGLI
   * #############################################################
   */

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsValid.
   * @param formsValid IRiscaFormTreeParent con i dati validi.
   * @override
   */
  formsValid(formsValid: IRiscaFormTreeParent) {
    // Resetto l'alert dei messaggi
    this.resetAlertConfigs();

    // Recupero la mappa dati dei componenti
    const mapData = this.getRiscaFormTreedData(formsValid);
    // Recupero i dati tramite chiavi
    const datiGA: GeneraliAmministrativi = mapData.get(
      this.IP_C.FORM_KEY_CHILD_DGA
    );
    const datiDA: DatiAnagrafici = mapData.get(this.IP_C.FORM_KEY_CHILD_DA);
    const datiT: PraticaDTVo = mapData.get(this.IP_C.FORM_KEY_CHILD_DT);
    const datiC = mapData.get(this.IP_C.FORM_KEY_CHILD_DC);
    // const docs = mapData.get(this.IP_C.FORM_KEY_CHILD_DOC);

    // Imposto i dati all'interno del componente
    this.datiGenAmm = datiGA;
    this.datiAnag = datiDA;
    this.datiTecnici = datiT;
    this.datiCont = datiC;
    // this.documenti = docs;

    // Verifico la modalità
    if (this.inserimento) {
      // Lancio il salvataggio dei dati della pratica
      this.step1InserisciPratica();
      // #
    } else if (this.modifica) {
      // Lancio l'aggiornamento dei dati della pratica
      this.step1ModificaPratica();
      // #
    }
  }

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsInvalid.
   * @param formsInvalid IRiscaFormTreeParent con i dati invalidi.
   */
  formsInvalid(formsInvalid: IRiscaFormTreeParent) {
    // Richiamo la funzione di gestione dell'alert per il reset
    this.resetAlertConfigs();

    // Definisco un array per i messaggi di errore
    let messaggi = [];
    // Genero una mappa con gli errori da visualizzare
    const mapErr = this.getRiscaFormTreedData(formsInvalid);
    // Recupero gli errori tramite chiavi
    let errGA = mapErr.get(this.IP_C.FORM_KEY_CHILD_DGA);
    let errDA = mapErr.get(this.IP_C.FORM_KEY_CHILD_DA);
    let errDT = mapErr.get(this.IP_C.FORM_KEY_CHILD_DT);

    // Aggiungo alle liste di errori le testate delle sezioni
    errGA ? errGA.unshift(this.IP_C.HEAD_DATI_GEN_AMM) : (errGA = []);
    errDA ? errDA.unshift(this.IP_C.HEAD_DATI_ANAG) : (errDA = []);
    errDT ? errDT.unshift(this.IP_C.HEAD_DATI_TECNICI) : (errDT = []);

    // Compongo la lista completa di errori
    messaggi = [...errGA, ...errDA, ...errDT];
    // Variabili di comodo
    const a = this.alertConfigs;
    const m = messaggi;
    const d = RiscaInfoLevels.danger;

    // Problemi di digest
    setTimeout(() => {
      // Richiamo la funzione di gestione dell'alert
      this.aggiornaAlertConfigs(a, m, d);
    });

    // Emetto l'evento custom di refresh delle select
    this.refreshSelect();
  }

  /**
   * Questa funzione specifica gestisce il flusso dati quando, nel componente dei dati anagrafici, l'utente aggiorna un soggetto e corregge gli indirizzi di spedizione.
   * Sostanzialmente si ritenta di salvare i dati della pratica.
   */
  onIndirizziSpedizioneCorrettiManualmente() {
    // Lancio la richiesta di salvataggio delle form
    this.appGenAmm.onFormSubmit();
    this.appQuadriTecnici.onFormSubmit();
    this.appDatiAnagrafici.onFormSubmit();
  }

  /**
   * ############################################
   * FUNZIONALITA' DI GESTIONE PER I SINGOLI FORM
   * ############################################
   */

  /**
   * Questa funzione è agganciata alla richiesta di snapshot per i dati anagrafici.
   * Quando questo accade, è necessario creare una snapshot dei dati recuperando tutte le informazioni attualmente presenti nei vari forms.
   * @param params IRFCReqSnapshot contenente i parametri di gestione per la richiesta.
   */
  richiestaNavigazioneDA(params: IRFCReqJourneyNavigation) {
    // Recupero i dati delle form
    this.datiGenAmmConfigs = this.appGenAmm.getMainFormActualRawValue();
    this.datiTecniciConfigs = this.appQuadriTecnici.getMainFormActualRawValue();
    this.datiAnagraficiConfigs =
      this.appDatiAnagrafici.getMainFormActualRawValue();

    // Estraggo dall'oggetto le informazioni per la navigazione
    const { route, claimant, extras, sameRoute, jStepTarget, segmentoFA } =
      params || {};

    // Verifico se esiste un segmento per il filo d'arianna
    if (segmentoFA) {
      // E' stato generato un segmento, lo aggiungo al filo
      this._riscaFA.aggiungiSegmento(segmentoFA);
    }

    // Recupero i dati comuni per la generazione dello step
    const stepBase = this.stepConfig;
    const snapshot = this.snapshotConfigs;
    // Aggiungo alcune informazioni allo step
    stepBase.stepClaimant = claimant;
    // Genero l'oggetto per il target del routing
    const target = this.navigationConfigs(route, extras?.state);
    // Assegno, anche se undefined, il journey step target all'oggetto target
    target.jStepTarget = jStepTarget;

    // Definisco le informazioni per il salvataggio dello step
    const step: IJourneyStep = { ...stepBase, ...target };
    // Utilizzo il servizio per aggiungere uno step al journey
    this._navigationHelper.stepForward(step, snapshot, sameRoute);
  }

  /**
   * ####################################
   * STEP PER L'INSERIMENTO DELLA PRATICA
   * ####################################
   */

  //#region

  /**
   * Funzione che gestisce il primo step per il salvataggio dei dati della pratica.
   */
  private step1InserisciPratica() {
    // Resetto i messaggi d'errore in pagina
    this.aggiornaAlertConfigs(this.alertConfigs);
    // Lancio lo step 2
    this.step2InserisciPratica();
  }

  /**
   * Funzione che gestisce il secondo step per il salvataggio dei dati della pratica.
   * La funzione prevede il salvataggio della pratica.
   */
  private step2InserisciPratica() {
    // Compongo l'oggetto per l'inserimento della pratica
    const datiPratica: PraticaForms = {
      idComponenteDt: this.appQuadriTecnici.idCDtLoaded,
      generaliAmministrativi: this.datiGenAmm,
      datiAnagrafici: this.datiAnag,
    };

    // Procedo con il salvataggio dei dati che compongono la pratica
    this._inserisciPratica.inserisciPratica(datiPratica).subscribe({
      next: (praticaVo: PraticaVo) => {
        // Lancio la funzione di salvataggio degli altri form
        this.step3InserisciPratica(praticaVo);
        // #
      },
      error: (error: any) => {
        // Effettuo il refresh delle select
        this.refreshSelect();
        // Gestisco il messaggio d'errore inaspettato
        this.onServiziError(error);
      },
    });
  }

  /**
   * Funzione che gestisce il terzo step per il salvataggio dei dati della pratica.
   * La funzione prevede il salvataggio di tutti i dati della pratica restanti e derivanti dal salvataggio della pratica.
   * @param praticaVo PraticaVo con le informazioni di riferimento per salvare i dati correlati all'oggetto.
   */
  private step3InserisciPratica(praticaVo: PraticaVo) {
    // La pratica è stata inserita, definisco l'id della pratica per la gestione della stampa
    this._riscaStampaP.setIdPraticaStampa(praticaVo.id_riscossione);

    // Aggiorno i dati della sezione generali amministrativi
    this.appGenAmm.setProgressivoUtenza(praticaVo.cod_riscossione_prog);
    this.appGenAmm.updateIstanzeProvvedimenti(praticaVo.provvedimentoIstanza);
    // Aggiorno i dati tecnici della pratica
    this.aggiornaPraticaEDatiTecnici(praticaVo);

    // Definisco un oggetto con i dati correlati alla pratica da salvare
    const correlati: PraticaCorrelati = {
      datiTecnici: this.datiTecnici,
    };

    // Lancio il salvataggio dei dati correlati alla pratica
    this._inserisciPratica
      .salvaDatiCorrelatiAPratica(praticaVo, correlati)
      .subscribe({
        next: (salvaResponse: PraticaCorrelatiRes) => {
          // Procedo con la conclusione del processo di salva pratica
          this.step4InserisciPratica(salvaResponse);
          // #
        },
        error: (error: any) => {
          // Effettuo il refresh delle select
          this.refreshSelect();
          // Gestisco il messaggio d'errore inaspettato
          this.onServiziError(error);
        },
      });
  }

  /**
   * Funzione che gestisce il quarto step per il salvataggio dei dati della pratica.
   * La pratica è salvata completamente, gestisco le informazioni ritornate dal salvataggio dei dati correlati.
   * @param correlatiSaved PraticaCorrelatiRes con i dati restituiti dalla chiamata.
   */
  private step4InserisciPratica(correlatiSaved: PraticaCorrelatiRes) {
    // Recupero il ritorno
    const { datiTecnici } = correlatiSaved;

    // Modifico la modalità del componente
    this.aggiornaModalita(AppActions.modifica);
    // Aggiorno i dati tecnici della pratica
    this.aggiornaPraticaEDatiTecnici(undefined, datiTecnici);

    // Aggiornamento dati per il calcolo canone
    this.setDatiCalcoloCanone(datiTecnici);
    // Verifico e tento di aggiornare i dati tecnici per possibili riduzioni/aumenti automatici
    this.updateDatiTecnici(datiTecnici);

    // Salvataggio dati andato a buon fine, emetto il messaggio di success
    this.onDatiSalvati();

    // RISCA-ISSUES-37 al termine dell'inserimento di una riscossione, bisgona lanciare la ricerca dei documenti allegati
    this.initDocumentiAllegati(this.praticaEDatiTecnici);
  }

  /**
   * Funzione che aggiorna i dati di configurazione della pratica.
   * @param p PraticaVo contenente i dati della pratica d'aggiornare.
   * @param dt PraticaDTVo contenente i dati tecnici della pratica d'aggiornare.
   */
  private aggiornaPraticaEDatiTecnici(p?: PraticaVo, dt?: PraticaDTVo) {
    // Creo una variabile d'appoggio per le informazioni di pratica e dati tecnici
    let praticaEDatiTecnici: PraticaEDatiTecnici = {
      ...this.praticaEDatiTecnici,
    };

    // Verifico se non esiste una configurazione attiva
    if (!praticaEDatiTecnici) {
      // Creo l'oggetto da configurazione
      praticaEDatiTecnici = {
        pratica: undefined,
        datiTecnici: undefined,
      };
    }

    // Verifico se esiste la pratica
    if (p) {
      // Aggiorno la pratica
      praticaEDatiTecnici.pratica = p;
    }
    // Verifico se esistono i dati tecnici
    if (dt) {
      // Aggiorno i dati tecnici
      praticaEDatiTecnici.datiTecnici = dt;
    }

    // Aggiorno la variabile del componente
    this.praticaEDatiTecnici = praticaEDatiTecnici;
    this.idComponenteDt = this.praticaIdCDt;
  }

  //#endregion

  /**
   * ##################################
   * STEP PER LA MODIFICA DELLA PRATICA
   * ##################################
   */

  //#region

  /**
   * Funzione che gestisce il primo step per la modifica dei dati della pratica.
   */
  private step1ModificaPratica() {
    // Resetto i messaggi d'errore in pagina
    this.resetAlertConfigs(this.alertConfigs);
    // Lancio lo step 2
    this.step2ModificaPratica();
  }

  /**
   * Funzione che gestisce il secondo step per la modifica dei dati della pratica.
   * La funzione prevede la modifica della pratica.
   */
  private step2ModificaPratica() {
    // Definisco i dati per l'aggiornamento
    const generaliAmministrativi = {
      ...this.datiGenAmmConfigs,
      ...this.datiGenAmm,
    };
    const datiAnagrafici = {
      ...this.datiAnagraficiConfigs,
      ...this.datiAnag,
    };

    // Compongo l'oggetto per l'inserimento della pratica
    const datiPratica: PraticaForms = {
      idComponenteDt: this.praticaEDatiTecnici?.pratica?.id_componente_dt,
      generaliAmministrativi,
      datiAnagrafici,
    };

    // Recupero l'id_riscossione
    const praticaEDatiTecnici = this.praticaEDatiTecnici;
    const { pratica } = praticaEDatiTecnici || {};
    const { id_riscossione } = pratica || {};

    // Procedo con la modifica dei dati che compongono la pratica
    this._inserisciPratica
      .modificaPratica(id_riscossione, datiPratica)
      .subscribe({
        next: (praticaVo: PraticaVo) => {
          // Lancio la funzione di salvataggio degli altri form
          this.step3ModificaPratica(praticaVo);
          // #
        },
        error: (error: any) => {
          // Effettuo il refresh delle select
          this.refreshSelect();
          // Gestisco il messaggio d'errore inaspettato
          this.onServiziError(error);
        },
      });
  }

  /**
   * Funzione che gestisce il terzo step per la modifica dei dati della pratica.
   * La funzione prevede la modifica di tutti i dati della pratica restanti e derivanti dal salvataggio della pratica.
   * @param praticaVo PraticaVo con le informazioni di riferimento per salvare i dati correlati all'oggetto.
   */
  private step3ModificaPratica(praticaVo: PraticaVo) {
    // Aggiorno i dati della sezione generali amministrativi
    this.appGenAmm.setProgressivoUtenza(praticaVo.cod_riscossione_prog);
    this.appGenAmm.updateIstanzeProvvedimenti(praticaVo.provvedimentoIstanza);
    // Aggiorno i dati tecnici della pratica
    this.aggiornaPraticaEDatiTecnici(praticaVo);

    // Estraggo i dati per riscossione
    const r0 = this.datiTecniciConfigs?.riscossione || {};
    const r1 = this.datiTecnici?.riscossione || {};
    // Definisco l'unione delle proprietà per i dati tecnici
    const riscossione = { ...r0, ...r1 };

    // Definisco i dati per l'aggiornamento
    const datiTecnici = { ...this.datiTecniciConfigs, ...this.datiTecnici };
    // Definisco la proprietà modificata
    datiTecnici.riscossione = riscossione as PraticaDTDataVo;

    // Definisco un oggetto con i dati correlati alla pratica da salvare
    const correlati: PraticaCorrelati = {
      datiTecnici,
    };

    // Lancio la modifica dei dati correlati alla pratica
    this._inserisciPratica
      .modificaDatiCorrelatiAPratica(praticaVo, correlati)
      .subscribe({
        next: (salvaResponse: PraticaCorrelatiRes) => {
          // Procedo con la conclusione del processo di salva pratica
          this.step4ModificaPratica(salvaResponse);
          // #
        },
        error: (error: any) => {
          // Effettuo il refresh delle select
          this.refreshSelect();
          // Gestisco il messaggio d'errore inaspettato
          this.onServiziError(error);
        },
      });
  }

  /**
   * Funzione che gestisce il quarto step per la modifica dei dati della pratica.
   * La pratica è salvata completamente, gestisco le informazioni ritornate dal salvataggio dei dati correlati.
   * @param correlatiSaved PraticaCorrelatiRes con i dati restituiti dalla chiamata.
   */
  private step4ModificaPratica(correlatiSaved: PraticaCorrelatiRes) {
    // Recupero il ritorno
    const { datiTecnici } = correlatiSaved;
    // Aggiorno i dati tecnici della pratica
    this.aggiornaPraticaEDatiTecnici(undefined, datiTecnici);

    // Resetto il campo del canone
    this.resetCanoneCalcolato(datiTecnici);
    // Salvataggio dati tecnici
    this.setDatiCalcoloCanone(datiTecnici);
    // Verifico e tento di aggiornare i dati tecnici per possibili riduzioni/aumenti automatici
    this.updateDatiTecnici(datiTecnici);

    // Salvataggio dati andato a buon fine, emetto il messaggio di success
    this.onDatiSalvati();
  }

  //#endregion

  /**
   * #######################################
   * FUNZIONALITA' DI UTILITY DEL COMPONENTE
   * #######################################
   */

  /**
   * Funzione di comodo per il refresh delle select.
   */
  private dtPraticaSaved() {
    // Emetto l'evento di pratica salvata
    this._quadriTecnici.praticaSaved();
  }

  /**
   * Funzione di comodo per il refresh delle select.
   */
  private refreshSelect() {
    // Emetto l'evento custom di refresh delle select
    this._riscaSelect.refreshDOM();
  }

  /**
   * Funzione che scatena gli eventi per il cambio della modalità di gestione dati.
   * @param m AppActions con la modalità da impostare.
   */
  private aggiornaModalita(m: AppActions.inserimento | AppActions.modifica) {
    // Aggiorno la modalità localre
    this.modalita = m;
    // Aggiorno la modalità per i dati tecnici
    this._quadriTecnici.cambioModalita(this.modalita);
    // Aggiorno la modalità per la pagina della pratica
    this._inserisciPratica.modalitaChanged(this.modalita);
  }

  /**
   * Funzione di comodo dalla quale estrarre gli id per i controlli di gestione di insert/update di un Gruppo.
   * Verranno recuperati gli id del capogruppo dal Gruppo, e l'id del soggetto da DatiAnagrafici.
   * @param da DatiAnagrafici per il recupero dati.
   * @param g Gruppo per il recupero dati.
   * @returns Oggetto { idSC: number; idSP: number; } con i dati estratti.
   */
  recuperaIdCPEIdSC(
    da: DatiAnagrafici,
    g: Gruppo
  ): {
    idSC: number;
    idSP: number;
  } {
    // Recupero dal gruppo il capogruppo
    const capogruppo = g.componenti_gruppo?.find((cg) => cg.flg_capo_gruppo);
    // Recupero l'id del soggetto capogruppo
    const idSC = capogruppo?.id_soggetto;
    // Verifico se nella configurazione del soggetto è presente un soggetto diverso
    const idSP = da?.soggettoPratica?.id_soggetto;

    // Ritorno gli id
    return { idSC, idSP };
  }

  /**
   * Funzione che recupera un oggetto della nav.
   * Se viene passato un idLink verrà impostato quell'oggetto, se esiste.
   * Se non specificato, verrà impostato il default.
   * @param idLink RiscaIstanzePratica per la ricerca della nav.
   */
  private setManualNav(idLink?: RiscaIstanzePratica) {
    // Recupero il IRiscaNavItem per il link
    const navItem = this.istanzaPraticaNav.getNav(idLink);
    // Tento di recuperare l'item
    const { ngbNavItem } = navItem || {};
    // Assegno l'item
    this.istanzaPratica = ngbNavItem;
  }

  /**
   * Setter custom che va a creare una configurazione vuota per la proprietà: datiAnagraficiConfigs.
   */
  setEmptyDatiAnagraficiConfigs() {
    // Definisco la variabile datiAnagraficiConfigs
    this.datiAnagraficiConfigs = {
      soggettoPratica: undefined,
      gruppoPratica: undefined,
      recapitiPratica: [],
    };
  }

  /**
   * Funzione utilizzata al salvataggio dei dati tecnici per il set dei nuovi dati per il calcolo del canone.
   * @param praticaDTVo PraticaDTVo contente i dati salvati.
   */
  private setDatiCalcoloCanone(praticaDTVo: PraticaDTVo) {
    // Verifico l'input
    if (!praticaDTVo?.riscossione) {
      // Interrompo la configurazione per il calcolo canone
      return;
    }

    // Estraggo le informazioni dall'oggetto riscossione
    const { riscossione } = praticaDTVo;
    // Recupero le informazioni per il calcolo del canone
    const idRiscossione = riscossione.id_riscossione;
    // const dataRiscossione = riscossione.data_inserimento;

    // Imposto i dati per il calcolo del canone
    this._quadriTecnici.aggiornaDatiCanone(
      idRiscossione /*, dataRiscossione */
    );
  }

  /**
   * Funzione utilizzata alla modifica della pratica, per il reset del campo del campo canone.
   * @param praticaDTVo PraticaDTVo contente i dati salvati.
   */
  private resetCanoneCalcolato(praticaDTVo: PraticaDTVo) {
    // Verifico l'input
    if (!praticaDTVo?.riscossione) {
      // Interrompo la configurazione per il calcolo canone
      return;
    }

    // Imposto i dati per il calcolo del canone
    this._quadriTecnici.resetEDisabilitaCanone();
  }

  /**
   * Funzione che effettua l'update dei dati tecnici.
   * @param praticaDTVo PraticaDTVo contente i dati salvati.
   */
  private updateDatiTecnici(praticaDTVo: PraticaDTVo) {
    // Verifico l'input
    if (!praticaDTVo?.riscossione) {
      // Interrompo la configurazione per il calcolo canone
      return;
    }

    // Recupero il flag di gestione manuale
    const { riscossione } = praticaDTVo || {};
    const { dati_tecnici } = riscossione || {};
    // Effettuo il parse dei dati tecnici
    const dt: DatiTecniciAmbienteVo =
      this._riscaUtilities.riscaJSONParse(dati_tecnici);
    // Dai dati generali tento di recuperare il flag di gestione manuale
    const { dati_generali } = dt || {};
    const { gestione_manuale } = dati_generali || {};

    // Verifico se esiste la gestione manuale
    if (gestione_manuale && gestione_manuale === ServerStringAsBoolean.false) {
      // Aggiorno i dati tecnici con le possibili riduzioni/aumenti aggiornate
      this._quadriTecnici.aggiornaDatiTecnici(praticaDTVo);
    }
  }

  /**
   * Funzione di supporto che gestisce la comunicazione con l'utente una volta salvati correttamente i dati.
   */
  private onDatiSalvati() {
    // Aggiorno l'alert
    this.aggiornaAlertSalvataggio();
    // Emetto l'evento di aggiornamento pratica per i dati tecnici
    this.dtPraticaSaved();
    // Emetto l'evento custom di refresh delle select
    this.refreshSelect();

    // Recupero lo stato della pratica e dati tecnici ed emetto l'evento
    this.onPraticaChange(this.praticaEDatiTecnici);

    // Lancio la funzione di gestione del lock della pratica una volta che è stato completato l'inserimento
    this.lockPratica(this.praticaEDatiTecnici);
  }

  /**
   * Funzione che emette l'evento che definisce la modifica dei dati della pratica.
   * @param praticaEDatiTecnici PraticaEDatiTecnici con le informazioni attuali della pratica.
   */
  private onPraticaChange(praticaEDatiTecnici: PraticaEDatiTecnici) {
    // Richiamo l'emit dei dati della pratica
    this._inserisciPratica.praticaChanged(praticaEDatiTecnici);
  }

  /**
   * Funzione che emette l'evento che definisce la modifica dei dati della pratica.
   * @param praticaEDatiTecnici PraticaEDatiTecnici con le informazioni attuali della pratica.
   */
  private lockPratica(praticaEDatiTecnici: PraticaEDatiTecnici) {
    // Il lock della riscossione avviene solo se l'utente non è un CONSULTATORE
    if (this._user.isCONSULTATORE) {
      // E' un consultatore, blocco il flusso
      return;
    }

    // Recupero dai dati passati in input l'id della pratica
    const idPratica = praticaEDatiTecnici?.pratica?.id_riscossione;
    // Lancio la funzione di lock della pratica
    this._riscaLockP.bloccaPratica(idPratica).subscribe({
      next: (richiestaLockRes: IRichiestaLockPraticaRes) => {
        // Lancio la funzione di verifica e gestione del lock riscossione
        this._riscaLockP.utenteLockaPratica(richiestaLockRes);
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione che aggiorna l'alert a seguito del salvataggio concluso per i dati della pratica.
   */
  private aggiornaAlertSalvataggio() {
    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.P001;
    // Definisco la variabile con il messaggio
    const success = [this._riscaMessages.getMessage(code)];
    // Variabili di comodo
    const a = this.alertConfigs;
    const i = RiscaInfoLevels.success;

    // Aggiorno la lista dei messaggi utente
    this.aggiornaAlertConfigs(a, success, i);
  }

  /**
   * Funzione che attiva la route per ritornare alla pagina precedente.
   */
  tornaIndietro() {
    // Gestico la parte di "pulizia" del filo d'arianna per la scheda soggetto
    this.indietroFiloArianna();
    // Richiamo l'unlock della riscossione. Essendo che l'indietro non cambia pagina, e quindi non attiva l'unlock del body/del cambio nav ricerche/inserimento pratica, lo richiamo direttamente
    this.sbloccaPratica();

    if (this.callerHome) {
      // Richiamo lo step back della navigazione definendo il flag di sameRoute
      this.tornaHome();
      return;
    }

    // Definisco un oggetto di stepback vuoto, che può essere riempito sulla base di alcune condizioni
    let jToStep: IJToStep = null;
    // Definisco il flag di sameRoute sulla base dell'ultimo caller (ricerca pratiche: semplice o avanzata)
    const sameRoute = this.callerRicercaSemplice || this.callerRicercaAvanzata;

    // Verifico se il chiamante precedente è "pratiche collegate"
    if (this.callerPraticheCollegate) {
      // Il caller è pratiche collegate, per evitare il reset del filo d'arianna, passo un "inutile" oggetto con parametri, così da non attivare il reset di default del filo
      jToStep = { stateTarget: { avoidResetFiloArianna: true } };
    }

    // Richiamo lo step back della navigazione definendo il flag di sameRoute
    this._navigationHelper.stepBack(jToStep, sameRoute);
  }

  /**
   * Funzione di gestione del filo d'arianna.
   * Va a rimuovere l'ultimo segmento che definisce il dettaglio o l'inserimento soggetto.
   */
  private indietroFiloArianna() {
    // Recupero i livelli di gestione per inserimento e dettaglio
    const insPFA: FALivello = this._riscaFA.nuovaPratica;
    const dettPFA: FALivello = this._riscaFA.dettaglioPratica;
    // Verifico se uno dei due livelli è presente nel filo d'arianna
    const segmentoIP = this._riscaFA.segmentoInFAByLivello(insPFA);
    const segmentoDP = this._riscaFA.segmentoInFAByLivello(dettPFA);
    // Definisco un oggetto singolo che conterrà solo un oggetto valorizzato (dei due) o undefined se non è stato trovato niente
    const segmento = segmentoIP || segmentoDP || undefined;

    // Tento di rimuovere il segmento dal filo d'arianna
    this._riscaFA.rimuoviSegmento(segmento);
  }

  /**
   * Funzione di gestione dello sblocco di una possibile pratica bloccata.
   */
  private sbloccaPratica() {
    // Richiamo il servizio di sblocco della pratica
    this._riscaLockP.richiediSbloccaPratica();
  }

  /**
   * Funzione che effettua un reindirizzamento verso la pagina della home.
   */
  private tornaHome() {
    // Definisco i parametri per la navigazione
    let state: IHomeRouteParams;
    // Definisco i parametri per la pagina home
    state = {};
    // Richiamo la funzione di back del servizio di navigazione
    this._navigationHelper.stepBack({ stateTarget: state });
  }

  /**
   * #########################
   * GETTER E SETTER DI COMODO
   * #########################
   */

  /**
   * Getter che verifica se esistono messaggi per alertConfigs e che non sia di tipo: RiscaInfoLevels.success
   */
  get alertConfigsNoSuccess() {
    return (
      this.alertConfigs?.messages?.length > 0 &&
      this.alertConfigs?.type !== RiscaInfoLevels.success
    );
  }

  /**
   * Getter che verifica se il caller è definito per 'ricerca semplice pratiche'.
   */
  get callerHome(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastCaller(AppCallers.home);
  }

  /**
   * Getter che verifica se il caller è definito per 'ricerca semplice pratiche'.
   */
  get callerRicercaSemplice(): boolean {
    // Definisco il caller da controllare
    const caller = AppCallers.ricercaSemplicePratiche;
    // Verifico il caller
    return this._navigationHelper.isLastCaller(caller);
  }

  /**
   * Getter che verifica se il caller è definito per 'ricerca avanzata pratiche'.
   */
  get callerRicercaAvanzata(): boolean {
    // Definisco il caller da controllare
    const caller = AppCallers.ricercaAvanzata;
    // Verifico il caller
    return this._navigationHelper.isLastCaller(caller);
  }

  /**
   * Getter che verifica se il caller è definito per 'pratiche collegate'.
   */
  get callerPraticheCollegate(): boolean {
    // Definisco il caller da controllare
    const caller = AppCallers.praticheCollegate;
    // Verifico il caller
    return this._navigationHelper.isLastCaller(caller);
  }

  /**
   * Getter di comodo che visualizza il pulsante INDIETRO solo ed esclusivamente se esiste una rotta sulla quale navigare all'indietro.
   */
  get tornaIndietroEnabled() {
    // Verifico se esiste uno step all'interno del servizio di navigazione
    const lastStep = this._navigationHelper.getLastStep();
    // Verifico se esiste uno step (che potenzialmente ha richiamato l'inserisci-pratica)
    return lastStep !== undefined;
  }

  /**
   * Getter di comodo che restitusce un oggetto IJourneySnapshot per generare una snapshot del componente.
   */
  get snapshotConfigs(): IJourneySnapshot {
    return {
      source: this,
      mapping: InserisciPraticaConsts.SNAPSHOT_CONFIG.mapping,
      saveFunc: InserisciPraticaConsts.SNAPSHOT_CONFIG.saveFunc,
    };
  }

  /**
   * Getter di comodo per gestire i componenti dei dati tecnici.
   */
  get praticaIdCDt() {
    // Navigo l'oggetto della pratica e recupero l'id del componente
    return this.praticaEDatiTecnici?.pratica?.id_componente_dt;
  }

  /**
   * Getter di comodo per l'id pratica.
   */
  get idPratica() {
    // Navigo l'oggetto della pratica e recupero l'id della pratica
    return this.praticaEDatiTecnici?.pratica?.id_riscossione;
  }
}
