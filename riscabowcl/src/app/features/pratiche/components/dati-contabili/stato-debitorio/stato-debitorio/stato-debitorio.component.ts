import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbNav, NgbNavChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/index';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaUtilitiesComponent } from 'src/app/shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { StatoDebitorioVo } from '../../../../../../core/commons/vo/stato-debitorio-vo';
import { RiscaNav2ndLvlClasses } from '../../../../../../shared/components/risca/risca-nav-helper/utilities/risca-nav-helper.enums';
import { RiscaLockPraticaService } from '../../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaUtilitiesService } from '../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  DatiAnagrafici,
  IRiscaNavLinkConfig,
  IRiscaTabChanges,
  RiscaInfoLevels,
  RiscaServerError,
  RiscaStatoDebitorio,
} from '../../../../../../shared/utilities';
import { StatoDebitorioNavClass } from '../../../../class/navs/stato-debitorio.nav.class';
import { DatiContabiliConsts } from '../../../../consts/dati-contabili/dati-contabili.consts';
import { StatoDebitorioConsts } from '../../../../consts/dati-contabili/stato-debitorio.consts';
import { IDatiAnagraficiSDUpd } from '../../../../interfaces/dati-contabili/dati-anagrafici-sd.interfaces';
import { IActionRejectSD } from '../../../../interfaces/dati-contabili/dati-contabili.interfaces';
import { DatiContabiliUtilityService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { DatiContabiliService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili.service';
import { StatoDebitorioService } from '../../../../service/dati-contabili/stato-debitorio.service';
import { InserisciPraticaService } from './../../../../service/inserisci-pratica/inserisci-pratica.service';

@Component({
  selector: 'stato-debitorio',
  templateUrl: './stato-debitorio.component.html',
  styleUrls: ['./stato-debitorio.component.scss'],
})
export class StatoDebitorioComponent
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Costante per le informazioni del componente. */
  DC_C = DatiContabiliConsts;
  /** Costante per le informazioni del componente. */
  SD_C = StatoDebitorioConsts;
  /** Oggetto contenente la configurazione per le classi di stile nav, di secondo livello. */
  nav2ndLvlClass: IRiscaNavLinkConfig = RiscaNav2ndLvlClasses;

  /** StatoDebitorioVo che identifica i dati dello stato debitorio in modifica/dettaglio. */
  @Input() statoDebitorio: StatoDebitorioVo;

  /** ViewChild collegato alla navigation bar. */
  @ViewChild('statoDebitorioNav') statoDebitorioNav: NgbNav;
  /** String che definisce il target dello stato debitorio. */
  sezioneStatoDebitorio: RiscaStatoDebitorio;
  /** StatoDebitorioNavClass per gestire la navigazione tra le pagine (l'header sarà nascosto). */
  statoDebitorioNavConfigs: StatoDebitorioNavClass;

  /** String che definisce il title del componente. */
  statoDebitorioTitle: string = '';

  /** Oggetto DatiAnagrafici contenente i dati per la configurazione della sezione: dati anagrafici. */
  datiAnagraficiConfigs: DatiAnagrafici;

  /**
   * ##############################################
   * SUBSCRIPTION AL SALVATAGGIO DELLE INFORMAZIONI
   * ##############################################
   */
  /** Subscription che permette la condivisione dell'evento di errore generato da un componente figlio. */
  onChildError: Subscription;

  /** Subscription che permette la condivisione dell'evento di inserimento di un oggetto StatoDebitorioVo. */
  onSDInsSuccess: Subscription;
  /** Subscription che permette la condivisione dell'evento d'errore durante un inserimento di un oggetto StatoDebitorioVo. */
  onSDInsError: Subscription;
  /** Subscription che permette la condivisione dell'evento di annuallamento della funzionalità di inserimento di un oggetto StatoDebitorioVo. */
  onSDInsCancel: Subscription;

  /** Subscription che permette la condivisione dell'evento di modifica di un oggetto StatoDebitorioVo. */
  onSDModSuccess: Subscription;
  /** Subscription che permette la condivisione dell'evento d'errore durante una modifica di un oggetto StatoDebitorioVo. */
  onSDModError: Subscription;
  /** Subscription che permette la condivisione dell'evento di annuallamento della funzionalità di modifica di un oggetto StatoDebitorioVo. */
  onSDModCancel: Subscription;

  /**
   * Costruttore
   */
  constructor(
    private _datiContabili: DatiContabiliService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _inserisciPratica: InserisciPraticaService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _statoDebitorioServ: StatoDebitorioService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);
    // Lancio il setup del componente
    this.setupComponente();
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
    // Lancio il setup della nav nascosta
    this.setupNavBar();
    // Setup dati anagrafici
    this.setupDatiAnagrafici();
  }

  /**
   * Funzione di setup per la nav bar del componente.
   */
  private setupNavBar() {
    // Lancio il setup della nav nascosta
    this.statoDebitorioNavConfigs = new StatoDebitorioNavClass();
  }

  /**
   * Prende la pratica dal servizio e compila i dati anagrafici
   */
  private setupDatiAnagrafici() {
    // Prendo la pratica
    const pratica = this._datiContabiliUtility?.pratica;
    // Prendo i dati anagrafici dalla pratica
    this.datiAnagraficiConfigs =
      this._inserisciPratica.convertPraticaVoToDatiAnagrafici(pratica);
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  /**
   * Funzione di init per il componente.
   */
  private initComponente() {
    // Lancio l'init del title
    this.initTitle();
    // Lancio il setup per i dati dello stato debitorio
    this.initStatoDebitorio();
    // Lancio l'init dei listener del componente
    this.initListeners();
  }

  /**
   * Funzione di init per il titolo della schermata.
   */
  private initTitle() {
    // Verifico la modalita del componente
    if (this.inserimento) {
      // Definisco il titolo
      this.statoDebitorioTitle = this.SD_C.SD_TITLE_INSERIMENTO;
      // #
    } else if (this.modifica) {
      // Definisco il titolo
      this.statoDebitorioTitle = this.SD_C.SD_TITLE_MODIFICA;
      // #
    } else {
      // Definisco il titolo
      this.statoDebitorioTitle = this.SD_C.SD_TITLE_DEFAULT;
    }
  }

  /**
   * Funzione di init che verifica e gestisce i dati per lo stato debitorio.
   */
  private initStatoDebitorio() {
    // Recupero lo stato debitorio dall'input
    const inputSD = this.statoDebitorio;
    // Recupero lo stato debitorio dal servizio
    const serviceSD = this._datiContabiliUtility.statoDebitorio;

    // Verifico lo stato debitorio in input
    if (inputSD) {
      // Esiste un input, imposto l'oggetto nel servizio
      this._datiContabiliUtility.statoDebitorio = inputSD;
      // #
    } else if (!inputSD && serviceSD) {
      // Non è definito uno sd in input, ma nel servizio è settato, lo imposto
      this.statoDebitorio = serviceSD;
      // #
    } else {
      // Non esiste uno stato debitorio, lo creo nuovo
      const sd = new StatoDebitorioVo();
      // Aggiorno sia componente che servizio
      this.statoDebitorio = sd;
      this._datiContabiliUtility.statoDebitorio = sd;
    }

    // Generato l'oggetto definisco i dati partendo dalla pratica
    this.initDatiPratica();
    // Una volta generato l'oggetto, definisco subito i dati anagrafici
    this.initDatiAnagraficiSD();
  }

  /**
   * Funzione di init dei dati della pratica minimi richiesti per lo stato debitorio.
   * L'init avverrà solo se ci troviamo in modalità: inserimento.
   */
  private initDatiPratica() {
    // Verifico se ci troviamo in inserimento
    if (this.inserimento) {
      // Recupero l'id della pratica
      const idP = this.pratica?.id_riscossione;
      // Imposto gli id nei servizi
      this._datiContabiliUtility.updateIdRiscossione(idP);
    }
  }

  /**
   * Funzione di init dei dati anagrafici di default per lo stato debitorio.
   * L'init avverrà solo se ci troviamo in modalità: inserimento.
   */
  private initDatiAnagraficiSD() {
    // Verifico se ci troviamo in inserimento
    if (this.inserimento) {
      // Partendo dai dati della pratica recupero gli id per: soggetto, gruppo e recapito principale
      const p = this.pratica;
      // Recupero le informazioni
      const soggetto = p?.soggetto;
      const idSoggetto = soggetto?.id_soggetto;
      const idGruppo = p?.gruppo_soggetto?.id_gruppo_soggetto;
      const recapP = this._riscaUtilities.recapitoPrincipalePratica(p);
      const recapA = this._riscaUtilities.recapitoAlternativoPratica(p);
      // La priorità sul recapito ce l'ha il recapito alternativo. Se non esiste, viene valorizzato con il recapito principale
      const idRecapito = recapA?.id_recapito ?? recapP?.id_recapito;

      // Definisco un oggetto per il set dei dati
      const daSD: IDatiAnagraficiSDUpd = { idRecapito, idSoggetto, idGruppo };
      // Imposto gli id nei servizi
      this._datiContabiliUtility.updateDatiAnagrafici(daSD);
    }
  }

  /**
   * Funzione di init per i listener del componente, per rimanere in ascolto degli eventi per il cambio pagina dei dati contabili.
   */
  private initListeners() {
    this.onChildError = this._statoDebitorioServ.onChildError$.subscribe({
      next: (error: RiscaServerError) => {
        // Resetto l'alert
        this.resetAlertConfigs(this.alertConfigs);
        // Gestisco l'errore generato
        this.onServiziError(error);
      },
    });

    this.onSDInsSuccess = this._datiContabili.onSDInsSuccess$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Gestisco l'evento di success
        this.onSDSuccessResult(statoDebitorio);
      },
    });

    this.onSDInsError = this._datiContabili.onSDInsError$.subscribe({
      next: (error: RiscaServerError) => {
        // Gestisco l'errore anche nel caso sia multiplo
        this.onSDErrorResult(error);
      },
    });

    this.onSDInsCancel = this._datiContabili.onSDInsCancel$.subscribe({
      next: () => {
        // Gestisco l'evento di cancellazione
        this.onSDCancelResult();
      },
    });

    this.onSDModSuccess = this._datiContabili.onSDModSuccess$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Gestisco l'evento di success
        this.onSDSuccessResult(statoDebitorio);
      },
    });

    this.onSDModError = this._datiContabili.onSDModError$.subscribe({
      next: (error: RiscaServerError) => {
        // Gestisco l'errore anche nel caso sia multiplo
        this.onSDErrorResult(error);
      },
    });

    this.onSDModCancel = this._datiContabili.onSDModCancel$.subscribe({
      next: (reject?: IActionRejectSD) => {
        // Gestisco l'evento di cancellazione
        this.onSDCancelResult(reject);
      },
    });
  }

  /**
   * Gestisce le operazioni da eseguire alla fine del successo nel salvataggio dello stato debitorio.
   * @param statoDebitorio StatoDebitorioVo salvato.
   */
  onSDSuccessResult(statoDebitorio: StatoDebitorioVo) {
    // Resetto l'alert
    this.resetAlertConfigs(this.alertConfigs);

    // Inserimento avvenuto correttamente, modifico la modalita
    this.modalita = AppActions.modifica;
    // Richiamo la funzione di gestione salvataggio dello stato debitorio
    const alertSuccess =
      this._datiContabiliUtility.onStatoDebitorioSalvato(statoDebitorio);
    // Aggiorno l'alert con la configurazione ritornata
    this.alertConfigs = alertSuccess;
  }

  /**
   * Gestisce le operazioni da eseguire in caso di errore nel salvataggio dello stato debitorio.
   */
  onSDErrorResult(error: RiscaServerError) {
    // Resetto l'alert
    this.resetAlertConfigs(this.alertConfigs);
    // Gestisco l'errore anche nel caso sia multiplo
    this.onServiziErrors(error);
  }

  /**
   * Gestisce le operazioni da eseguire alla fine della cancellazione nel salvataggio dello stato debitorio.
   * @param reject IActionRejectSD contenente possibili informazioni per gestire la reject utente.
   */
  onSDCancelResult(reject?: IActionRejectSD) {
    // Resetto l'alert
    this.resetAlertConfigs(this.alertConfigs);

    // Verifico se ci sono informazioni per la reject
    if (reject) {
      // Genero l'alert partendo dalla reject
      this.alertConfigs = this._datiContabiliUtility.alertConfigsReject(reject);
    }
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Destroy
   * @override
   */
  ngOnDestroy(): void {
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onChildError) {
        this.onChildError.unsubscribe();
      }
      if (this.onSDInsSuccess) {
        this.onSDInsSuccess.unsubscribe();
      }
      if (this.onSDInsError) {
        this.onSDInsError.unsubscribe();
      }
      if (this.onSDInsCancel) {
        this.onSDInsCancel.unsubscribe();
      }
      if (this.onSDModSuccess) {
        this.onSDModSuccess.unsubscribe();
      }
      if (this.onSDModError) {
        this.onSDModError.unsubscribe();
      }
      if (this.onSDModCancel) {
        this.onSDModCancel.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * #######################
   * GESTIONE DELLE NAV TABS
   * #######################
   */

  /**
   * Funzione che aggiorna la sezione dei dati contabili.
   * @param sezione RiscaStatoDebitorio che definisce la nuova sezione.
   */
  cambiaSezioneStatoDebitorio(sezione: RiscaStatoDebitorio) {
    // Modifico il flag per la sezione
    this.sezioneStatoDebitorio = sezione;
    // Aggiorno la visualizzazione della sezione per la nav
    this.statoDebitorioNav.select(this.sezioneStatoDebitorio);
  }

  /**
   * Funzione agganciata all'evento di cambio della tab per la nav bar.
   * @param nav NgbNavChangeEvent che definisce la nuova sezione.
   */
  onSezioneSDChange(nav: NgbNavChangeEvent) {
    // Recupero dall'evento di cambio nav le informazioni
    const { activeId, nextId } = nav;
    // Emetto l'evento per informare i tab della pratica che sta avvenendo un cambio
    this.sezioneSDChange(activeId, nextId);
  }

  /**
   * Funzione di comodo che crea i dati ed emette l'evento di cambio tab dello stato debitorio.
   * @param actual RiscaStatoDebitorio definisce l'id del tab attualmente attivo.
   * @param next RiscaStatoDebitorio che definisce l'id del target della tab.
   */
  private sezioneSDChange(
    actual: RiscaStatoDebitorio,
    next: RiscaStatoDebitorio
  ) {
    // Definisco l'oggetto per il tab changes
    const tabs: IRiscaTabChanges = { actual, next };
    // Emetto l'evento per informare i tab della pratica che sta avvenendo un cambio
    this._statoDebitorioServ.sezioneSDChanged(tabs);
  }

  /**
   * #######################
   * EVENTI EMESSI DAI FIGLI
   * #######################
   */

  /**
   * Funzione agganciata all'evento onFormErrors del componente: generali-amministrativi-dilazione.
   * @param errors string[] con la lista di messaggi d'errore generati dalla componente.
   */
  onFormErrorsGAD(errors: string[]) {
    // Richiamo la funzione per la gestione dei messaggi
    this.gestisciErroriPagina(errors);
  }

  /**
   * Funzione agganciata all'evento onFormErrors del componente: annualita.
   * @param errors string[] con la lista di messaggi d'errore generati dalla componente.
   */
  onFormErrorsAnn(errors: string[]) {
    // Richiamo la funzione per la gestione dei messaggi
    this.gestisciErroriPagina(errors);
  }

  /**
   * Funzione che gestisce gli errori lanciati dalle sotto pagine dello stato debitorio.
   * @param errors string[] con la lista di messaggi d'errore generati dalla componente.
   */
  private gestisciErroriPagina(errors: string[]) {
    // Verifico la lista
    if (errors && errors.length > 0) {
      // Variabili di comodo
      const a = this.alertConfigs;
      const m = errors;
      const d = RiscaInfoLevels.danger;

      // Resetto l'alert
      this.resetAlertConfigs(a);
      // Aggiorno l'alert dei messaggi
      setTimeout(() => {
        // Imposto i nuovi messaggi
        this.aggiornaAlertConfigs(a, m, d);
      });
    }
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Recupera l'id della pratica
   */
  get idPratica() {
    return this._datiContabiliUtility.idPratica;
  }

  /**
   * Getter di comodo per il recupero dell'oggetto pratica.
   */
  get pratica() {
    return this._datiContabiliUtility.pratica;
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
