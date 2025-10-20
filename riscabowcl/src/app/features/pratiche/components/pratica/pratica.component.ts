import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import { FASegmento } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaNavHelperComponent } from '../../../../shared/components/risca/risca-nav-helper/risca-nav-helper.component';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaLockPraticaService } from '../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaStampaPraticaService } from '../../../../shared/services/risca/risca-stampa/risca-stampa-pratica.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  IPraticaRouteParams,
  RiscaAzioniPratica,
  RiscaServerError,
} from '../../../../shared/utilities';
import {
  IPraticaNavClassConfigs,
  PraticaNavClass,
} from '../../class/navs/pratica.nav.class';
import { InserisciPraticaConsts } from '../../consts/inserisci-pratica/inserisci-pratica.consts';
import { RicercaAvanzataPraticheConsts } from '../../consts/ricerca-avanzata-pratiche/ricerca-avanzata-pratiche.consts';
import { RicercaSemplicePraticheConsts } from '../../consts/ricerca-semplice-pratiche/ricerca-semplice-pratiche.consts';
import { PraticaRouteKeys } from '../../enums/pratica/pratica.enums';
import {
  IIPConfigs,
  IRAConfigs,
} from '../inserisci-pratica/utilities/inserisci-pratica.interfaces';

@Component({
  selector: 'pratica',
  templateUrl: './pratica.component.html',
  styleUrls: ['./pratica.component.scss'],
})
export class PraticaComponent
  extends RiscaNavHelperComponent<RiscaAzioniPratica>
  implements OnInit
{
  /** Costanti associate al componente: Ricerca semplice pratiche. */
  RSP_C = RicercaSemplicePraticheConsts;
  /** Costanti associate al componente: Ricerca avanzata pratiche.  */
  RAP_C = RicercaAvanzataPraticheConsts;
  /** Costanti associate al componente: Inserisci pratica.  */
  IP_C = InserisciPraticaConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;
  /** Costante contenente le informazioni dell'enum: RiscaAzioniPratica. */
  praticheNavIdLinks = RiscaAzioniPratica;

  /** PraticaNavClass per gestire le riscaNav. */
  riscaNav: PraticaNavClass;
  /** RiscaAzioniPratica che definisce il caricamento del contenuto di una specifica componente DIVERSA rispetto alla nav selezionata. */
  pageTarget: RiscaAzioniPratica;

  /** any contenente i parametri per il setup della ricerca semplice. */
  ricercaSempliceConfigs: any;
  /** IRAConfigs contenente i parametri per il setup della ricerca avanzata. */
  ricercaAvanzataConfigs: IRAConfigs;
  /** IIPConfigs contenente i parametri per il setup dell'inserimento pratica. */
  inserisciPraticaConfigs: IIPConfigs;

  /** Boolean che definisce l'abilitazione della nav, secondo la configurazione per profilo: inserisci-pratica. */
  private _AEA_ins_pratica_NavDisabled: boolean;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaStampaP: RiscaStampaPraticaService,
    riscaUtilities: RiscaUtilitiesService,
    router: Router,
    private _user: UserService
  ) {
    // Inizializzazione della class super
    super(riscaAlert, riscaUtilities, router);
    // Richiamo il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio l'init del super
    super.ngOnInit();
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup del componente.
   */
  protected setupComponente() {
    // Resetto il valore per il target della pagina
    this.pageTarget = undefined;

    // Setup delle navs
    this.setupNavs();
    // Effettuo il setup dei dati passati alla pagina
    this.setupRouteParams();
  }

  /**
   * Funzione di setup per l'inizializzazione delle navs.
   */
  private setupNavs() {
    // Recupero le chiavi per la verifica sulla disabilitazione della nav
    const IPD_KEY = this.AEAK_C.INS_PRATICA_H_CERCA;
    // Definisco disabled per le nav
    this._AEA_ins_pratica_NavDisabled =
      this._accessoElementiApp.isAEADisabled(IPD_KEY);

    // Costruisco l'oggetto di configurazione per la classe delle nav
    const c: IPraticaNavClassConfigs = {
      AEA_ins_pratica_NavDisabled: this._AEA_ins_pratica_NavDisabled,
    };

    // Genero un oggetto per le riscaNav
    this.riscaNav = new PraticaNavClass(c);

    // Verifico se l'inserisci-pratica, nav di default, è disabilitata
    if (this._AEA_ins_pratica_NavDisabled) {
      // Recupero la lista delle nav
      const n = this.riscaNav;
      // Recupero la prima nav libera
      const fallback = this.firstNavEnabled<RiscaAzioniPratica>(n);
      // Imposto il nav disponibile
      this.setManualNav(fallback.navTarget);
      // #
    } else {
      // Imposto il tab di default
      this.setManualNav();
    }

    // Verifico le condizioni per la gestione del filo d'arianna per la gestione di altre pagine che pre-poloano il filo
    const checkFA = this.setupVerificaFA();
    // Verifico se il filo d'arianna è già popolato
    if (checkFA) {
      // Il filo d'arianna è vuoto, quindi non sono altre pagine a richiamare la sezione pratiche, lancio la gestione del filo
      this.gestisciFiloArianna(this.lastTabActive);
    }
  }

  /**
   * Funzione di setup che effettua il controllo di gestione per il filo d'arianna.
   * @returns boolean con il risultato del controllo.
   */
  private setupVerificaFA(): boolean {
    // Il primo check è se ci sono già elementi all'interno del filo d'arianna
    const faElements = this._riscaFA.filoArianna?.length;
    // Verifico le condizioni sulla quantità
    const noElems = faElements === 0;
    const oneElem = faElements === 1;

    // Verifico se non ci sono elementi
    if (noElems) {
      // Ritorno true
      return true;
    }

    // Verifico se esiste esiste il segmento "Pratiche"
    const praticheFA = this._riscaFA.pratiche;
    const segmentoP: FASegmento =
      this._riscaFA.segmentoInFAByLivello(praticheFA);
    // Verifico se esiste un solo elemento ed è "Pratiche"
    if (oneElem && segmentoP !== undefined) {
      // E' questa la casistica, ritorno true
      return true;
    }

    // Altre condizioni, per esempio: filo arianna da home a pratiche
    return false;
  }

  /**
   * Funzione che setta le configurazioni per la nav definita dall'input.
   * @param navTarget RiscaAzioniPratica con il taget per la navbar.
   * @param state IPraticaRouteParams contenente i parametri passati tramite routing.
   * @override
   */
  protected setNavTargetConfigs(
    navTarget: RiscaAzioniPratica,
    state: IPraticaRouteParams
  ) {
    // Verifico se esiste un target
    if (!navTarget) {
      return;
    }

    // Recupero le informazioni di configurazioni dallo stato
    const { pageTarget } = state;

    // Verifico se esiste una specifica configurazione per il caricamento della pagina (standard si apre la nav => pagina associata)
    if (!pageTarget) {
      // Non esiste, lancio il set standard
      this.setPagesParameters(navTarget, state);
      // #
    } else {
      // Esiste la pagina, devo gestire in maniera diversa il settaggio, impostando diversamente la nav (stessa nav ricerca, ma pagina della pratica)
      this.setPagesParameters(pageTarget, state);
      // Caricate le informazioni definisco il target per la pagina
      this.pageTarget = pageTarget;
    }
  }

  /**
   * Funzione che setta le configurazioni dati per le pagine.
   * @param navTarget RiscaAzioniPratica con il taget per la navbar.
   * @param state IPraticaRouteParams contenente i parametri passati tramite routing.
   */
  private setPagesParameters(
    navTarget: RiscaAzioniPratica,
    state: IPraticaRouteParams
  ) {
    // Verifico se esiste un target
    if (!navTarget) {
      return;
    }

    // Verifico qual è il target della navbar
    switch (navTarget) {
      case RiscaAzioniPratica.ricercaSemplice:
        // Recupero il relativo oggetto
        this.ricercaSempliceConfigs = state[PraticaRouteKeys.ricercaSemplice];
        break;
      // #
      case RiscaAzioniPratica.ricercaAvanzata:
        // Recupero il relativo oggetto
        this.ricercaAvanzataConfigs = state[PraticaRouteKeys.ricercaAvanzata];
        break;
      // #
      case RiscaAzioniPratica.inserisciPratica:
        // Richiamo la funzione di comodo per il set dei dati
        this.setPageParameterInserimento(state);
        break;
      // #
    }
  }

  /**
   * Funzione di comodo per gestire le logiche per il set parametri per la pagina in inserimento.
   * @param state IPraticaRouteParams contenente i parametri passati tramite routing.
   */
  private setPageParameterInserimento(state: IPraticaRouteParams) {
    // Recupero il relativo oggetto e lo setto come configurazione
    this.inserisciPraticaConfigs = state[PraticaRouteKeys.inserimento];
    // Recupero l'id della riscossione
    const { pratica } = this.inserisciPraticaConfigs?.praticaEDatiTecnici ?? {};
    // Definisco a livello di servizio l'id della pratica per permettere la stampa
    this._riscaStampaP.setIdPraticaStampa(pratica?.id_riscossione);
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione che aggiorna l'azione selezionata dall'utente.
   * @param azione RiscaAzioniPratica che definisce la nuova azione.
   * @override
   */
  cambiaNav(azione: RiscaAzioniPratica) {
    // Verifico se si sta premendo sulla tab già attiva
    const sameTab = this.lastTabActive === azione;
    // Check sulla variabile
    if (sameTab) {
      // Blocco le logiche
      return;
    }

    // Resetto le informazioni per l'inserimento dei dati anagrafici
    this.resetInserisciPratica();
    // Modifico il flag per l'azione
    this.lastTabActive = azione;

    // Lancio la funzione per la gestione del filo d'arianna
    this.gestisciFiloArianna(azione);
    // Lancio la funzione per la gestione per la ricarica degli step, se è stata premuta una tab
    this.gestisciJStep(azione);
    // Aggiorno la visualizzazione dell'azione per la nav
    this.ngbNavList.select(this.tabActive);
  }

  /**
   * Funzione che gestisce le casistiche per il filo d'arianna.
   * Il livello digestione è basato sul tab selezionato per la navigazione.
   * @param azione RiscaAzioniPratica con la tab che si sta cercando di caricare.
   * @param isSetup boolean che permette di distinguere se la funzione è chiamata come setup. Per default è: false.
   */
  private gestisciFiloArianna(azione: RiscaAzioniPratica) {
    // Verifico se all'interno del filo d'arianna è presente la voce HOME
    const homeFA: FALivello = this._riscaFA.home;
    // Cerco tramite servizio se esiste il livello
    const existHFA = this._riscaFA.isLivelloInFiloArianna(homeFA);

    // Verifico il risultato del check
    if (existHFA) {
      // Esiste, vuol dire che l'utente ha fatto qualcosa nella home e poi è arrivato nella scheda pratica. Resetto il filo d'arianna
      this._riscaFA.resetFiloArianna();
      // Setto all'interno del filo il primo segmento che vale per "pratiche"
      const praticheFA: FALivello = this._riscaFA.pratiche;
      this._riscaFA.aggiungiSegmentoByLivelli(praticheFA);
      // #
    } else {
      // Mi sto spostando da un tab all'altro, devo rimuovere l'ultimo segmento
      this.rimuoviSegmentoPratica();
    }

    // Imposto il nuovo segmento in base al tab premuto
    let tabFA: FALivello;

    // Verifico quale tab si vuole caricare
    switch (azione) {
      case RiscaAzioniPratica.ricercaSemplice:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.ricercaSemplice;
        break;
      case RiscaAzioniPratica.ricercaAvanzata:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.ricercaAvanzata;
        break;
      case RiscaAzioniPratica.inserisciPratica:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.nuovaPratica;
        break;
    }

    // Definisco il nuovo segmento per la tab
    this._riscaFA.aggiungiSegmentoByLivelli(tabFA);
  }

  /**
   * Funzione di comodo che rimuove i segmenti partendo dalla tab della pratica
   */
  private rimuoviSegmentoPratica() {
    // Recupero i 3 livelli che possono essere presenti nel filo d'arianna
    const rsFA = this._riscaFA.ricercaSemplice;
    const raFA = this._riscaFA.ricercaAvanzata;
    const npFA = this._riscaFA.nuovaPratica;
    // Effettuo 3 ricerche all'interno dei segmenti per il filo d'arianna
    const segmentoRS: FASegmento = this._riscaFA.segmentoInFAByLivello(rsFA);
    const segmentoRA: FASegmento = this._riscaFA.segmentoInFAByLivello(raFA);
    const segmentoNP: FASegmento = this._riscaFA.segmentoInFAByLivello(npFA);
    // Definisco un'unica variabile che contiene uno dei possibili valori ritornati
    const segmento = segmentoRS || segmentoRA || segmentoNP || undefined;

    // Verifico se è stato trovato un segmento
    if (segmento) {
      // Segmento di gestione trovato, elimino lui e i segmenti successivi
      this._riscaFA.rimuoviSegmento(segmento);
    }
  }

  /**
   * Funzione che gestisce lo stato dello step all'interno dalla navigazione mediante journey.
   * @param azione RiscaAzioniPratica con la tab che si sta cercando di caricare.
   */
  private gestisciJStep(azione: RiscaAzioniPratica) {
    // Definisco la chiave per la rimozione dello step da journey
    let praticaKey: string;

    // Verifico quale tab si vuole caricare
    switch (azione) {
      case RiscaAzioniPratica.ricercaSemplice:
        // Assegno la chiave del componente
        praticaKey = this.RSP_C.NAVIGATION_CONFIG.componentId;
        break;
      case RiscaAzioniPratica.ricercaAvanzata:
        // Assegno la chiave del componente
        praticaKey = this.RAP_C.NAVIGATION_CONFIG.componentId;
        break;
      case RiscaAzioniPratica.inserisciPratica:
        // Assegno la chiave del componente
        praticaKey = this.IP_C.NAVIGATION_CONFIG.componentId;
        break;
    }

    // Verifico che sia definita una chiave e che l'ultimo step di journey sia coerente con la chiave
    if (praticaKey && this._navigationHelper.isLastStep(praticaKey)) {
      // Rimuovo lo step dalla lista journey
      this._navigationHelper.removeStep(praticaKey);
    }
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione che resetta le informazioni dell'inserisci pratica.
   */
  private resetInserisciPratica() {
    // Resetto la configurazione
    this.inserisciPraticaConfigs = undefined;
    // Resetto la modalità
    this.modalita = AppActions.inserimento;
    // Resetto il target della pagina
    this.pageTarget = undefined;
    // Rimuovo dal servizio di stampa i possibili id della pratica
    this._riscaStampaP.resetIdPratica();

    // Verifico che il ruolo non sia consultatore
    if (!this._user.isCONSULTATORE) {
      // Rimuovo la pratica dal lock su DB
      this._riscaLockP.sbloccaPratica().subscribe({
        error: (e: RiscaServerError) => {
          // Gestisco l'errore del servizio
          this.onServiziError(e);
        },
      });
    }
  }
}
