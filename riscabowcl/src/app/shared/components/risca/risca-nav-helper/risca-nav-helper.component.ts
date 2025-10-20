import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgbNav } from '@ng-bootstrap/ng-bootstrap';
import { RiscaNavClass } from '../../../classes/risca-nav/risca-nav.class';
import { RiscaAlertService } from '../../../services/risca/risca-alert.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  IRiscaNavItem,
  IRiscaNavLinkConfig,
  IRiscaNavRouteParams,
  IRiscaTabFallback,
  RiscaAlertConfigs,
  RiscaServerError,
  TRiscaAlertType,
} from '../../../utilities';
import { RiscaNotifyCodes } from '../../../utilities/enums/risca-notify-codes.enums';
import {
  AppActions,
  RiscaInfoLevels,
} from '../../../utilities/enums/utilities.enums';
import { RiscaNav2ndLvlClasses } from './utilities/risca-nav-helper.enums';

@Component({
  selector: 'risca-nav-helper',
  templateUrl: './risca-nav-helper.component.html',
  styleUrls: ['./risca-nav-helper.component.scss'],
})
export class RiscaNavHelperComponent<T> implements OnInit {
  /** Oggetto contenente la configurazione per le classi di stile nav, di secondo livello. */
  nav2ndLvlClass: IRiscaNavLinkConfig = RiscaNav2ndLvlClasses;

  /** ViewChild collegato alla navigation bar. */
  @ViewChild('ngbNavList') ngbNavList: NgbNav;

  /** Definisce la modalità della componente: inerimento o modifica. */
  modalita = AppActions.inserimento;
  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();
  /** Boolean che definisce la configurazione per distruggere il DOM quando viene selezionato un altro tab. */
  destroyOnHide = true;

  /** T che mantiene lo stato delle nav per l'azione pratica. */
  tabActive: T;
  /** T che mantiene lo stato delle nav per l'azione pratica. */
  lastTabActive: T;
  /** RiscaNavClass<T> per gestire le navs del componente. */
  riscaNav: RiscaNavClass<T>;

  /**
   * Costruttore.
   */
  constructor(
    protected _riscaAlert: RiscaAlertService,
    protected _riscaUtilities: RiscaUtilitiesService,
    protected _router: Router
  ) {}

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup del componente.
   */
  protected setupComponenteRNH() {
    // Effettuo il setup dei dati passati alla pagina
    this.setupRouteParams();
  }

  /**
   * Funzione che verifica se tramite il servizio di Route sono stati passati parametri di configurazione.
   */
  protected setupRouteParams() {
    // Definisco l'oggetto state
    let state: IRiscaNavRouteParams<T>;
    // Recupero l'oggetto state del routing
    state = this._riscaUtilities.getRouterState(this._router);

    // Verifico che esista lo state
    if (!state) {
      return;
    }

    // Recupero i parametri dallo state della route
    const { navTarget, modalita } = state;

    // Se esiste la modalità la aggiorno nel componente
    if (modalita) {
      this.modalita = modalita;
    }

    // Lancio la funzione di setup per target e parametri
    this.setupNavTargetAndParams(navTarget, state);
  }

  /**
   * Funzione di setup del target richiesto per la visualizzazione dati e per i relativi parametri.
   * @param navTarget T con il taget per la navbar.
   * @param state IRiscaNavRouteParams<T> contenente i parametri passati tramite routing.
   */
  protected setupNavTargetAndParams(
    navTarget: T,
    state: IRiscaNavRouteParams<T>
  ) {
    // Verifico se esiste un target
    if (!navTarget) {
      return;
    }

    // Esiste, modifico il target di default
    this.setManualNav(navTarget);
    // Definisco le configurazioni per la nav
    this.setNavTargetConfigs(navTarget, state);
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione che ritorna la prima nav abilitata.
   * Questa funzione viene chiamata per il fallback di una navigazione verso una nav disabilitata.
   * @param nav RiscaNavClass<any> con la configurazione delle nav.
   * @returns IRiscaTabFallback<T> con i dati di fallback per la navigazione.
   */
  protected firstNavEnabled<T>(nav: RiscaNavClass<any>): IRiscaTabFallback<T> {
    // Definisco un contenitore per lo state in base al link di navigazione
    let navTarget: T;
    const state: any = {};

    // Dalle navs cerco la prima che abbia la configurazione disabled a false
    navTarget = nav.navs.find((n: IRiscaNavItem<any>) => {
      // Verifico se la nav non è disabilitata
      return !n.disabled;
      // #
    })?.ngbNavItem as any;

    // Ritorno l'oggetto di fallback
    return { navTarget, state };
  }

  /**
   * Funzione che recupera un oggetto della nav.
   * Se viene passato un idLink verrà impostato quell'oggetto, se esiste.
   * Se non specificato, verrà impostato il default.
   * @param idLink T per la ricerca della nav.
   */
  protected setManualNav(idLink?: T | string) {
    // idLinks sarà identificato dalla proprietà di un enum, quindi lo gestisco come stringa
    const id = idLink as any;
    // Imposto il tab richiesto
    this.tabActive = this.riscaNav.getNav(id)?.ngbNavItem;
    // Inizializzo l'ultima azione pratica
    this.lastTabActive = this.tabActive;
  }

  /**
   * Funzione pensata per l'override, che setta le configurazioni per la nav definita dall'input.
   * @param navTarget any con il taget per la navbar.
   * @param state IRiscaNavRouteParams<any> contenente i parametri passati tramite routing.
   */
  protected setNavTargetConfigs(
    navTarget: any,
    state: IRiscaNavRouteParams<any>
  ) {
    // Verifico se esiste un target
    if (!navTarget) {
      return;
    }
  }

  /**
   * Funzione che aggiorna l'azione selezionata dall'utente.
   * @param azione T che definisce la nuova azione.
   * @param params any come tipo di dato che può essere impiegato per logiche specifiche.
   */
  public cambiaNav(azione: T, params?: any) {
    // Verifico se si sta premendo sulla tab già attiva
    const sameTab = this.lastTabActive === azione;
    // Check sulla variabile
    if (sameTab) {
      // Blocco le logiche
      return;
    }

    // Modifico il flag per l'azione
    this.lastTabActive = azione;
    // Aggiorno la visualizzazione dell'azione per la nav
    this.ngbNavList.select(this.tabActive);
  }

  /**
   * Funzione di comodo che imposta il messaggio d'errore
   * @param e RiscaServerError che definisce il corpo dell'errore.
   * @param messageCode string che definisce il messaggio d'errore da visualizzare in testata. Default è 'E005'.
   * @param othersMessages Array di string contenente un array con altri messaggi di dettaglio per l'errore. Default è [].
   */
  protected onServiziError(
    e: RiscaServerError,
    messageCode = RiscaNotifyCodes.E005,
    othersMessages: string[] = []
  ) {
    // Variabili di comodo
    const mC = messageCode;
    const oM = othersMessages;

    // Definisco un array di messaggi d'errore
    let erroriValidazione = this._riscaAlert.messagesFromServerError(e, mC, oM);

    // Variabili di comodo
    const a = this.alertConfigs;
    const m = erroriValidazione;
    const d = RiscaInfoLevels.danger;

    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(a, m, d);
  }

  /**
   * Funzione di comodo che imposta il messaggio d'errore per molteplici errori
   * @param errors RiscaServerError che definisce il corpo dell'errore.
   */
  protected onServiziErrors(error: RiscaServerError) {
    // Variabile di comodo per controllare gli errori
    const errors = error.errors;

    // Se ho un errore solo, verifico se ha dei sottoerrori
    if (errors == undefined || errors.length == 0) {
      // Se non ne ha, invoco la funzione di gestione del singolo errore
      this.onServiziError(error);
      // Non devo fare altro
      return;
    }

    // Variabile di appoggio per la lista di errori di validazione
    let erroriValidazione: string[] = [];
    // Itero sugli errori per ottenere
    errors.forEach((error: RiscaServerError) => {
      // Prendo la lista di messaggi di errore
      const listaMessaggi = this._riscaAlert.messagesFromServerError(error);
      // Aggiungo agli errori di validazione la lista di errori
      erroriValidazione.push(...listaMessaggi);
    });

    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(
      this.alertConfigs,
      erroriValidazione,
      RiscaInfoLevels.danger
    );
  }

  /**
   * Funzione di comodo che imposta il messaggio d'errore generando un alert config specifico sulla base della configurazione dell'errore del server.
   * @param error RiscaServerError che definisce il corpo dell'errore.
   */
  protected alertFromServiziError(error: RiscaServerError) {
    // Gestisco il messaggio d'errore inaspettato
    const a = this._riscaAlert.createAlertFromServerError(error);
    // Modifico il dato dell'alert config
    this.alertConfigs = a;
  }

  /**
   * Funzione che gestisce il prompt dei messaggi.
   * Se i parametri non sono definiti, verrà effettuato un reset.
   * @param c RiscaAlertConfigs d'aggiornare con le nuove informazioni.
   * @param m Array di string contenente i messaggi da visualizzare.
   * @param t TRiscaAlertType che definisce la tipologia di alert da visualizzare.
   */
  protected aggiornaAlertConfigs(
    c: RiscaAlertConfigs,
    m?: string[],
    t?: TRiscaAlertType
  ) {
    // Aggiorno la configurazione
    this._riscaAlert.aggiornaAlertConfigs(c, m, t);
  }

  /**
   * Funzione che gestisce il reset del prompt dei messaggi.
   * Se i parametri non sono definiti, verrà effettuato un reset.
   * @param c RiscaAlertConfigs da resettare.
   */
  resetAlertConfigs(c?: RiscaAlertConfigs) {
    // Verifico se esiste l'oggetto per l'alert, altrimenti imposto quello locale
    c = c ?? this.alertConfigs;
    // Resetto la configurazione
    c = this._riscaAlert.aggiornaAlertConfigs(c);
    // Propago l'alert tramite servizio
    this.emettiAlert(c);
  }

  /**
   * Funzione che emette la struttura di un alert per la visualizzazione.
   * @param a RiscaAlertConfigs con la configurazione dell'alert.
   */
  protected emettiAlert(a: RiscaAlertConfigs) {
    // Richiamo la funzione del servizio con l'aggiornamento dell'alert
    this._riscaAlert.emettiAlert(a);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  get navExists() {
    return this.riscaNav?.navs != null;
  }
}
