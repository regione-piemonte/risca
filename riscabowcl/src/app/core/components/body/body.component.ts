import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  ActivatedRoute,
  NavigationEnd,
  NavigationStart,
  Router,
} from '@angular/router';
import { Subscription } from 'rxjs/index';
import { ReportService } from '../../../features/report/service/report/report.service';
import { IPollReportsStatus } from '../../../features/report/service/report/utilities/report.interfaces';
import { RiscaFiloAriannaService } from '../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaLockPraticaService } from '../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaStampaPraticaService } from '../../../shared/services/risca/risca-stampa/risca-stampa-pratica.service';
import { RiscaUtilitiesService } from '../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppRoutes,
  AppRoutesSpecsState,
  RiscaServerError,
} from '../../../shared/utilities';
import { ElaborazioneVo } from '../../commons/vo/elaborazione-vo';
import { AccessoElementiAppKeyConsts } from '../../consts/accesso-elementi-app/accesso-elementi-app.consts';
import { BodyConsts } from '../../consts/body/body.consts';
import { AccessoElementiAppService } from '../../services/accesso-elementi-app.service';
import { NavigationHelperService } from '../../services/navigation-helper/navigation-helper.service';
import { UserService } from '../../services/user.service';
import { IRiscaBodyNav } from './utilities/body.interfaces';
import { DocumentiAllegatiService } from '../../../features/pratiche/service/documenti-allegati/documenti-allegati.service';
import { DatiContabiliUtilityService } from '../../../features/pratiche/service/dati-contabili/dati-contabili/dati-contabili-utility.service';

/**
 * @version SONARQUBE_22_04_24 Rimosso OnInit vuoto. 
 */
@Component({
  selector: 'risca-body',
  templateUrl: './body.component.html',
  styleUrls: ['./body.component.scss'],
})
export class RiscaBodyComponent implements OnDestroy {
  /** Costante che definisce le proprietà del componente. */
  B_C = BodyConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** Oggetto di configurazione per il routing dell'applicazione. */
  links: IRiscaBodyNav[] = [];
  /** String come contenitore per lo stato dell'url corrente dell'applicazione. */
  private currentUrl: string;
  /** string che definisce il numero di report scaricabili per la sessione corrente. */
  private _notificheReportSession: string;
  /** string che definisce il numero di report scaricabili per le vecchie sessioni applicative. */
  private _notificheReportOldSessions: string;

  /** Subscription come ascoltatore del cambio di pagina dell'applicazione. */
  onRouteChange$: Subscription;
  /** Subscription per rimanere in ascolto dell'evento di success per la generazione report. */
  private _onPollingReportsSuccess$: Subscription;
  /** Subscription per rimanere in ascolto dell'evento di modifica ai dati dei report "vecchi" collegati alle elaborazioni. */
  private _onElaborazioniReportsChange$: Subscription;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _activateRoute: ActivatedRoute,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _docAllegati: DocumentiAllegatiService,
    private _navigationHelper: NavigationHelperService,
    private _report: ReportService,
    private _riscaFA: RiscaFiloAriannaService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaStampaP: RiscaStampaPraticaService,
    private _riscaUtilities: RiscaUtilitiesService,
    public router: Router,
    private _user: UserService
  ) {
    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnDestroy() {
    try {
      if (this.onRouteChange$) {
        this.onRouteChange$.unsubscribe();
      }
      if (this._onPollingReportsSuccess$) {
        this._onPollingReportsSuccess$.unsubscribe();
      }
      if (this._onElaborazioniReportsChange$) {
        this._onElaborazioniReportsChange$.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di comodo che gestisce il setup del componente.
   */
  private setupComponente() {
    // Lancio la funzione di generazione delle route principali
    this.setupMainRoutes();
    // Setup del routing dell'applicazione
    this.setupRouting();
    // Setup per i listener del componente per gestire le rotte applicative
    this.setupListeners();
    // Lancio il setup per le informazioni della lista dei report generati
    this.setupReportsData();
  }

  /**
   * Funzione che gestisce il setup delle route principali utilizzate dalla navigazione.
   */
  private setupMainRoutes() {
    // Definisco un oggetto comune per gli state delle route
    const baseStateParam: any = {};
    // Utilizzo l'assegnazione tramite proprietà per definire specifiche proprietà di routing
    baseStateParam[AppRoutesSpecsState.resetState] = true;

    // Definisco gli oggetti delle route
    const home = this.B_C.NAV_HOME;
    const pratiche = this.B_C.NAV_PRATICHE;
    const pagamenti = this.B_C.NAV_PAGAMENTI;
    const verifiche = this.B_C.NAV_VERIFICHE;
    const spedizioni = this.B_C.NAV_SPEDIZIONI;
    const configurazioni = this.B_C.NAV_CONFIGURAZIONI;
    const report = this.B_C.NAV_REPORT;
    // Definisco lo state per le route
    home.state = baseStateParam;
    pratiche.state = baseStateParam;
    pagamenti.state = baseStateParam;
    verifiche.state = baseStateParam;
    spedizioni.state = baseStateParam;
    configurazioni.state = baseStateParam;
    report.state = baseStateParam;
    // Definisco l'abilitazione
    pagamenti.disabled = this._accessoElementiApp.isAEADisabled(
      this.AEAK_C.PAGAMENTI
    );
    verifiche.disabled = this._accessoElementiApp.isAEADisabled(
      this.AEAK_C.VERIFICHE
    );
    spedizioni.disabled = this._accessoElementiApp.isAEADisabled(
      this.AEAK_C.SPEDIZIONI
    );
    configurazioni.disabled = this._accessoElementiApp.isAEADisabled(
      this.AEAK_C.CONFIGURAZIONI
    );

    // Inerisco le configurazioni per il routing
    this.links.push(home);
    this.links.push(pratiche);
    this.links.push(pagamenti);
    this.links.push(verifiche);
    this.links.push(spedizioni);
    this.links.push(report);
    this.links.push(configurazioni);
  }

  /**
   * Funzione che gestisce il setup del routing.
   * Se si sta navigando sulla root dell'applicazione, per default verrà attivata la navigazione alla route di default.
   */
  private setupRouting() {
    // Recupero l'url di route
    const url = this.router.url;

    // Verifico se l'url non ha path
    if (url === AppRoutes.root) {
      // Navigo alla route di default
      this.router.navigateByUrl(AppRoutes.home);
    }
  }

  /**
   * Funzione di setup che gestisce gli ascoltatori del componente.
   */
  private setupListeners() {
    // Registro un ascoltatore per il cambio di rotta
    this.onRouteChange$ = this.router.events.subscribe((event) => {
      // Verifico se si sta cercando di cambiare rotta
      if (event instanceof NavigationStart) {
        // Sta avvenendo un cambio di rotta, verifico le condizioni per il filo d'arianna
        this.gestisciFiloArianna(event);
        // Gestisco le configurazioni di stampa
        this.gestisciStampaPratica(event);
        // Gestisco le configurazioni di lock della pratica
        this.gestisciLockPratica(event);
        // Gestisco le configurazioni per il download dei documenti allegati
        this.gestisceStopDocsDownload(event);
        // Gestisco le logiche per uscita dalla pagina dell'istanza
        this.gestisciUscitaORefreshPratica(event);
        // #
      } else if (event instanceof NavigationEnd) {
        // Navigazione terminata, mi salvo l'url navigato
        this.currentUrl = event.url;
      }
    });

    // Registro un ascoltatore per le notifiche dei documenti per la sezione report
    this._onPollingReportsSuccess$ =
      this._report.onPollingReportsSuccess$.subscribe({
        next: (reportsStatus: IPollReportsStatus) => {
          // Richiamo la funzione di gestione notifiche
          this.onReportStatus(reportsStatus);
        },
      });

    // Registro un ascoltatore per gli aggiornamenti dai report/elaborazioni vecchi
    this._onElaborazioniReportsChange$ =
      this._report.onElaborazioniReportsChange$.subscribe({
        next: (elaborazioniReports: ElaborazioneVo[]) => {
          // Richiamo la funzione di gestione notifiche
          this.onElaborazioniReportsChange(elaborazioniReports);
        },
      });
  }

  /**
   * Funzione di setup per i report che hanno già ottenuto un risposta per la loro generazione.
   */
  private setupReportsData() {
    // Recupero dal servizio la lista di elaborazioni con report precedenti
    this.onElaborazioniReportsChange(this._report.elaborazioniReports);
  }

  /**
   * ##############################
   * FUNZIONI DI GESTIONI NOTIFICHE
   * ##############################
   */

  /**
   * Funzione che gestisce il dato ritornato dal polling per le nuove notifiche per la scheda report.
   * @param reportsStatus IPollReportsStatus con le informazioni per le notifiche sui report.
   */
  private onReportStatus(reportsStatus: IPollReportsStatus) {
    // Estraggo gli array per success e errors
    const numSuccess = reportsStatus.completed?.length;
    const numErrors = reportsStatus.errors?.length;

    // Verifico se ci sono reporta disponibili
    if (numSuccess > 0) {
      // Abbiamo dei report disponibili
      this._notificheReportSession = numSuccess.toString();
      // #
    } else if (numErrors > 0) {
      // Abbiamo report in erorre
      this._notificheReportSession = '!';
      // #
    } else {
      // Non ci sono dati disponibili
      this._notificheReportSession = undefined;
    }
  }

  /**
   * Funzione che gestisce il dato ritornato dalla modifica delle elaborazioni reports modificati.
   * @param elaborazioniReports ElaborazioneVo[] con le informazioni per le notifiche sui report.
   */
  private onElaborazioniReportsChange(elaborazioniReports: ElaborazioneVo[]) {
    // Calcolo la quantità di informazioni dalla lista
    this._notificheReportOldSessions = elaborazioniReports?.length?.toString();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione che gestisce il filo d'arianna a seconda della condizioni della rotta e dei parametri.
   * @param event NavigationStart con le informazioni di inizio navigazione.
   */
  private gestisciFiloArianna(event: NavigationStart) {
    // Verifico l'input
    if (!event) {
      // Niente che si possa gestire
      return;
    }

    // Recupero le rotte dall'evento
    const currentUrl = this.currentUrl;
    const targetUrl = event.url;
    const state = this.routeState;
    const existState = !this._riscaUtilities.isEmptyObject(state);

    if (currentUrl == targetUrl) {
      // #1) Sono sulla stessa rotta, non devo fare nulla
      return;
      // #
    } else if (currentUrl != targetUrl && existState) {
      // #2) Sto andando su un'altra rotta e ho dei dati, non devo fare nulla
      return;
      // #
    } else if (currentUrl != targetUrl && !existState) {
      // #3) Sto andando su un'altra rotta, ma non ho dati, verifico le rotte specifiche
      this.nuovaRottaNoParametri(currentUrl, targetUrl);
      // #
    }
  }

  /**
   * Funzione impiegata per la gestione specifica delle rotte dell'applicazione e per la gestione del filo d'arianna.
   * @param currentUrl string come url attuale dell'applicazione.
   * @param targetUrl string come url di cambio pagina dell'applicazione.
   */
  private nuovaRottaNoParametri(currentUrl: string, targetUrl: string) {
    // Verifico se mi sto dirigendo sulla pagina delle pratiche
    if (targetUrl.includes(AppRoutes.pratiche)) {
      // Definisco il livello per pratiche
      const praticheFA = this._riscaFA.pratiche;
      // Imposto il filo d'arianna
      this._riscaFA.resetFiloArianna();
      this._riscaFA.aggiungiSegmentoByLivelli(praticheFA);
      // #
    } else if (targetUrl.includes(AppRoutes.pagamenti)) {
      // Definisco il livello per pratiche
      const pagamentiFA = this._riscaFA.pagamenti;
      // Imposto il filo d'arianna
      this._riscaFA.resetFiloArianna();
      this._riscaFA.aggiungiSegmentoByLivelli(pagamentiFA);
      // #
    } else if (targetUrl.includes(AppRoutes.verifiche)) {
      // Definisco il livello per pratiche
      const verificheFA = this._riscaFA.verifiche;
      // Imposto il filo d'arianna
      this._riscaFA.resetFiloArianna();
      this._riscaFA.aggiungiSegmentoByLivelli(verificheFA);
      // #
    } else if (targetUrl.includes(AppRoutes.spedizioni)) {
      // Definisco il livello per spedizioni
      const spedizioniFA = this._riscaFA.spedizioni;
      // Imposto il filo d'arianna
      this._riscaFA.resetFiloArianna();
      this._riscaFA.aggiungiSegmentoByLivelli(spedizioniFA);
      // #
    } else if (targetUrl.includes(AppRoutes.configurazioni)) {
      // Definisco il livello per pratiche
      const canoneFA = this._riscaFA.canone;
      // Imposto il filo d'arianna
      this._riscaFA.resetFiloArianna();
      this._riscaFA.aggiungiSegmentoByLivelli(canoneFA);
      // #
    } else if (targetUrl.includes(AppRoutes.report)) {
      // Definisco il livello per pratiche
      const reportFA = this._riscaFA.report;
      // Imposto il filo d'arianna
      this._riscaFA.resetFiloArianna();
      this._riscaFA.aggiungiSegmentoByLivelli(reportFA);
      // #
    }
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando dalla pagina delle pratiche.
   * @param event NavigationStart con le informazioni di inizio navigazione.
   * @returns boolean che definisce se ci si sta spostando dalla pagina delle riscossioni.
   */
  private awayFromRiscossioni(event: NavigationStart) {
    // Verifico l'input
    if (!event) {
      // Niente che si possa gestire
      return false;
    }

    // Recupero le rotte dall'evento
    const currentUrl = this.currentUrl;
    const targetUrl = event.url;
    // Definisco i check di controllo
    const currentRoutePratiche = this.isRoutePratiche(currentUrl);
    const targetRouteAway = !this.isRoutePratiche(targetUrl);

    // Definisco i flag di check finali
    const isAwayPratiche = currentRoutePratiche && targetRouteAway;

    // Verifico se mi sto spostando con la navigazione dalla pagina delle pratiche
    return isAwayPratiche;
  }

  /**
   * Funzione di supporto che verifica se sta avenendo un reload della pagina delle riscossioni.
   * @param event NavigationStart con le informazioni di inizio navigazione.
   * @returns boolean che definisce se si sta ricaricando la pagina delle riscossioni.
   */
  private reloadingRiscossioni(event: NavigationStart): boolean {
    // Verifico l'input
    if (!event) {
      // Niente che si possa gestire
      return false;
    }

    // Recupero le rotte dall'evento
    const currentUrl = this.currentUrl;
    const targetUrl = event.url;
    // Definisco i check di controllo
    const currentRoutePratiche = this.isRoutePratiche(currentUrl);
    const isReloading = targetUrl === AppRoutes.root;

    // Definisco i flag di check finali
    const isReloadingPratiche = currentRoutePratiche && isReloading;

    // Verifico se mi sto spostando con la navigazione dalla pagina delle pratiche
    return isReloadingPratiche;
  }
  
  /**
   * Funzione che gestisce il flusso dati nel momento in cui la pagina delle pratiche viene aggiornata o ci si sposta dalla pagina pratiche ad un altra pagina.
   * @param event NavigationStart con le informazioni di inizio navigazione.
   */
  private gestisciUscitaORefreshPratica(event: NavigationStart) {
    // Verifico l'input
    if (!event) {
      // Niente che si possa gestire
      return;
    }

    // Verifico se mi sto spostando dalla pagina delle riscossioni
    const isLeavingRiscossioni = this.awayFromRiscossioni(event);
    // Verifico se si sta verificando un reload della pagina
    const isReloadingRiscossioni = this.reloadingRiscossioni(event);

    // Verifico se mi sto spostando con la navigazione dalla pagina delle pratiche
    if (isLeavingRiscossioni && isReloadingRiscossioni) {
      // Vado a resettare le informaizioni dei servizi per la pagina delle pratiche
      this._datiContabiliUtility.resetDatiContabiliSD();
    }
  }

  /**
   * Funzione che gestisce le impostazioni di stampa pratica rispetto al tipo di navigazione in corso.
   * @param event NavigationStart con le informazioni di inizio navigazione.
   */
  private gestisciStampaPratica(event: NavigationStart) {
    // Verifico l'input
    if (!event) {
      // Niente che si possa gestire
      return;
    }

    // Verifico se mi sto spostando con la navigazione dalla pagina delle pratiche
    if (this.awayFromRiscossioni(event)) {
      // Mi sto spostando, vado a resettare il servizio di stampa
      this._riscaStampaP.resetIdPratica();
    }
  }

  /**
   * Funzione che gestisce le impostazioni di stampa pratica rispetto al tipo di navigazione in corso.
   * @param event NavigationStart con le informazioni di inizio navigazione.
   */
  private gestisciLockPratica(event: NavigationStart) {
    // Verifico l'input
    if (!event) {
      // Niente che si possa gestire
      return;
    }

    // Verifico se mi sto spostando dalla pagina delle riscossioni
    const isLeavingRiscossioni = this.awayFromRiscossioni(event);
    // Verifico se si sta verificando un reload della pagina
    const isReloadingRiscossioni = this.reloadingRiscossioni(event);
    // Verifico che non sia un consultatore
    const notCONSULTATORE = !this._user.isCONSULTATORE;

    // Verifico se mi sto spostando con la navigazione dalla pagina delle pratiche
    if (isLeavingRiscossioni && !isReloadingRiscossioni && notCONSULTATORE) {
      // Mi sto spostando, rimuovo il lock della pratica
      this._riscaLockP.sbloccaPratica().subscribe({
        error: (e: RiscaServerError) => {
          // Niente da gestire per ora
        },
      });
    }
  }

  /**
   * Funzione che gestisce il reset dei dati collegati allo scarico dei documenti allegati di una riscossione.
   * Se c'è una chiamata in corso, si tenterà di annullarla.
   * @param event NavigationStart con le informazioni di inizio navigazione.
   */
  private gestisceStopDocsDownload(event: NavigationStart) {
    // Verifico l'input
    if (!event) {
      // Niente che si possa gestire
      return;
    }

    // Verifico se mi sto spostando dalla pagina delle riscossioni
    const isLeavingRiscossioni = this.awayFromRiscossioni(event);
    // Verifico se si sta verificando un reload della pagina
    const isReloadingRiscossioni = this.reloadingRiscossioni(event);

    // Verifico se mi sto spostando con la navigazione dalla pagina delle pratiche
    if (isLeavingRiscossioni && !isReloadingRiscossioni) {
      // Mi sto spostando, cerco d'interrompere la chiamata allo scarico documenti della riscossione
      this._docAllegati.resetDocsData();
      // #
    }
  }

  /**
   * Funzione collegata al click delle tab di navigazione.
   * @param link IRiscaBodyNav contenente i dati della tab premuta.
   */
  onTabClick(link: IRiscaBodyNav) {
    // Richiamo la gestione della navigazione
    this.resetJourney();
  }

  /**
   * Funzione collegata al click delle tab di navigazione.
   */
  private resetJourney() {
    // Pulisco il journey di navigazione
    this._navigationHelper.resetJourney();
  }

  /**
   * #############################
   * FUNZIONI DI COMODO E SUPPORTO
   * #############################
   */

  /**
   * Funzione di supporto che verifica se la route passata in input è quella per: Pratiche.
   * @param route string contenente l'url da verificare.
   * @returns boolean con il risultato della verifica.
   */
  private isRoutePratiche(route: string): boolean {
    // Verifico se la route in input è quella delle pratiche
    return route === AppRoutes.gestionePratiche || route === AppRoutes.pratiche;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo per il recupero dei parametri possibili passati nel route delle pagine.
   * @returns any con i dati nel passaggio delle route.
   */
  get routeState(): any {
    // Uso il servizio per possibili parametri nell'active route
    const arp = this._navigationHelper.getActivatedRouteData(
      this._activateRoute
    );
    // Uso il servizio per possibili parametri nello state route
    const rp = this._navigationHelper.getRouterState(this.router);

    // Verifico e cerco di unire le informazioni degli oggetti per gestire il flusso
    let arpObj = arp ?? {};
    let rpObj = rp ?? {};
    // Unisco le informazioni degli oggetti
    const stateData = { ...arpObj, ...rpObj };
    // Vado a cancellare la possibile variabile di state condivisa, il parametro serve per "riempire" lo state di Angular e permetterne comunque l'individuazione
    delete stateData[AppRoutesSpecsState.resetState];

    // Ritorno uno o l'altro oggetto
    return stateData;
  }

  /**
   * Getter di comodo che recupera la quantità di report generati e non ancora scaricati dall'utente.
   * @returns number con il numero di report generati e non ancora scaricati dall'utente.
   */
  get reportGenerated(): number {
    // Recupero dal servizio l'array di report
    const reports = this._report.reportsCompleted;
    // Ritorno il numero di elementi dell'array
    return reports?.length ?? 0;
  }

  /**
   * Getter di comodo che recupera la quantità di report generati in sessioni precedenti e non ancora scaricati dall'utente.
   * @returns number con il numero di report generati in sessioni precedenti e non ancora scaricati dall'utente.
   */
  get reportOldSessions(): number {
    // Recupero dal servizio l'array di report
    const reports = this._report.elaborazioniReports;
    // Ritorno il numero di elementi dell'array
    return reports?.length ?? 0;
  }

  /**
   * Getter che definisce come visualizzare le informazioni dei report generati.
   * @returns string con le informazioni per i report generati.
   */
  get notificaReport(): string {
    // Recupero le informazioni per le notifiche
    const notificheReportSession: string = this._notificheReportSession;
    const notificheReportOldSessions: string = this._notificheReportOldSessions;
    // Tento di convertire le informazioni in numeri
    const numberNRS: number = Number(notificheReportSession);
    const numberNROS: number = Number(notificheReportOldSessions);
    // Verifico se la conversione ha effettivamente generato dei numeri
    const isNumberNRS: boolean = !isNaN(numberNRS);
    const isNumberNROS: boolean = !isNaN(numberNROS);

    // Verifico se entrambe le informazioni sulle notifiche sono numeri
    if (isNumberNRS && isNumberNROS) {
      // Entrambi sono numeri, li sommo e ritorno una stringa
      return (numberNRS + numberNROS).toString();
      // #
    } else if (isNumberNRS) {
      // C'è almeno il numero di report in sessione come numero, lo ritorno
      return notificheReportSession;
      // #
    } else if (isNumberNROS && numberNROS > 0) {
      // C'è almeno il numero di report in sessione come numero, lo ritorno
      return notificheReportOldSessions;
      // #
    } else if (notificheReportSession) {
      // Potrebbe esserci un errore sui report in sessione, lo ritorno come condizione di stringa
      return notificheReportSession;
      // #
    } else {
      // Nessuno dei casi precedenti
      return '';
    }
  }
}
