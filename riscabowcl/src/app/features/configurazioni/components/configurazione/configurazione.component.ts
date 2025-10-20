import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { FASegmento } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaNavHelperComponent } from '../../../../shared/components/risca/risca-nav-helper/risca-nav-helper.component';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { AltriParametriConsts } from '../altri-parametri/utilities/altri-parametri.consts';
import { CanoniConsts } from '../canoni/utilities/canoni.consts';
import { ParametriDilazioneConsts } from '../parametri-dilazione/utilities/parametri-dilazione.consts';
import { TassiDiInteresseConsts } from '../tassi-di-interesse/utilities/tassi-di-interesse.consts';
import {
  AzioniConfigurazione,
  ConfigurazioneRouteKeys,
} from './utilities/configurazione.enums';
import { IConfigurazioneRouteParams } from './utilities/configurazione.interfaces';
import {
  ConfigurazioneNavClass,
  IConfigurazioniNavClassConfigs,
} from './utilities/configurazione.nav.class';

@Component({
  selector: 'configurazione',
  templateUrl: './configurazione.component.html',
  styleUrls: ['./configurazione.component.scss'],
})
export class ConfigurazioneComponent
  extends RiscaNavHelperComponent<AzioniConfigurazione>
  implements OnInit
{
  /** Costanti associate al componente: Canoni. */
  CAN_C = new CanoniConsts();
  /** Costanti associate al componente: Tassi d'interessi. */
  TDI_C = new TassiDiInteresseConsts();
  /** Costanti associate al componente: Parametri dilazione. */
  PD_C = new ParametriDilazioneConsts();
  /** Costanti associate al componente: Altri parametri. */
  AP_C = new AltriParametriConsts();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;
  /** Costante contenente le informazioni dell'enum: RiscaAzioniConfigurazione. */
  configurazioniNavIdLinks = AzioniConfigurazione;

  /** ConfigurazioneNavClass per gestire le riscaNav. */
  riscaNav: ConfigurazioneNavClass;
  /** RiscaAzioniConfigurazione che definisce il caricamento del contenuto di una specifica componente DIVERSA rispetto alla nav selezionata. */
  pageTarget: AzioniConfigurazione;

  /** any contenente i parametri per il setup canoni. */
  canoniConfigs: any;
  /** any contenente i parametri per il setup tassi d'interesse. */
  tassiInteresseConfigs: any;
  /** any contenente i parametri per il setup parametri dilazione. */
  parametriDilazioneConfigs: any;
  /** any contenente i parametri per il setup altri parametri. */
  altriParametriConfigs: any;

  /** Boolean che definisce l'abilitazione della nav, secondo la configurazione per profilo: canoni. */
  private _AEA_canoni_NavDisabled: boolean;
  /** Boolean che definisce l'abilitazione della nav, secondo la configurazione per profilo: tassi d'interesse. */
  private _AEA_tassi_interesse_NavDisabled: boolean;
  /** Boolean che definisce l'abilitazione della nav, secondo la configurazione per profilo: paramentri della dilazione. */
  private _AEA_parametri_dilazione_NavDisabled: boolean;
  /** Boolean che definisce l'abilitazione della nav, secondo la configurazione per profilo: altri parametri. */
  private _AEA_altri_parametri_NavDisabled: boolean;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    riscaUtilities: RiscaUtilitiesService,
    router: Router
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

    // Resetto il filo d'arianna
    this.setupFiloArianna();
    // Setup delle navs
    this.setupNavs();
    // Effettuo il setup dei dati passati alla pagina
    this.setupRouteParams();
  }

  /**
   * Funzione di setup per la gestione del filo d'arianna. In questo caso specifico, quando si accede alla HOME, bisogna resettare sempre il filo.
   */
  private setupFiloArianna() {
    // Resetto il filo d'arianna
    this._riscaFA.resetFiloArianna();
  }

  /**
   * Funzione di setup per l'inizializzazione delle navs.
   */
  private setupNavs() {
    // Recupero le chiavi per la verifica sulla disabilitazione della nav
    // const CD_KEY = this.AEAK_C.PAGINA_CANONI; ==> Accesso consentito alla pagina sempre, ma solo in lettura. Non blocca la tab.
    const TDD_KEY = this.AEAK_C.TASSI_DI_INTERESSI_H;
    const PDD_KEY = this.AEAK_C.PARAMETRI_DILAZIONE_H;
    const APD_KEY = this.AEAK_C.ALTRI_PARAMETRI_H;
    // Definisco disabled per le nav
    // this._AEA_canoni_NavDisabled =
    //   this._accessoElementiApp.isAEADisabled(CD_KEY);
    //this._AEA_tassi_interesse_NavDisabled =
    //  this._accessoElementiApp.isAEADisabled(TDD_KEY);
    this._AEA_parametri_dilazione_NavDisabled =
      this._accessoElementiApp.isAEADisabled(PDD_KEY);
    this._AEA_altri_parametri_NavDisabled =
      this._accessoElementiApp.isAEADisabled(APD_KEY);

    // Costruisco l'oggetto di configurazione per la classe delle nav
    const c: IConfigurazioniNavClassConfigs = {
      AEA_canoni_NavDisabled: this._AEA_canoni_NavDisabled,
      AEA_tassi_interesse_NavDisabled: this._AEA_tassi_interesse_NavDisabled,
      AEA_parametri_dilazione_NavDisabled: this._AEA_parametri_dilazione_NavDisabled,
      AEA_altri_parametri_NavDisabled: this._AEA_altri_parametri_NavDisabled,
    };

    // Genero un oggetto per le riscaNav
    this.riscaNav = new ConfigurazioneNavClass(c);

    // Verifico se canoni, nav di default, è disabilitata
    if (this._AEA_canoni_NavDisabled) {
      // Recupero la lista delle nav
      const n = this.riscaNav;
      // Recupero la prima nav libera
      const fallback = this.firstNavEnabled<AzioniConfigurazione>(n);
      // Imposto il nav disponibile
      this.setManualNav(fallback.navTarget);
      // #
    } else {
      // Imposto il tab di default
      this.setManualNav();
    }

    // Lancio la gestione del filo d'arianna
    this.gestisciFiloArianna(this.lastTabActive);
  }

  /**
   * Funzione che setta le configurazioni per la nav definita dall'input.
   * @param navTarget RiscaAzioniConfigurazione con il taget per la navbar.
   * @param state IConfigurazioneRouteParams contenente i parametri passati tramite routing.
   * @override
   */
  protected setNavTargetConfigs(
    navTarget: AzioniConfigurazione,
    state: IConfigurazioneRouteParams
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
   * @param navTarget RiscaAzioniConfigurazione con il taget per la navbar.
   * @param state IConfigurazioneRouteParams contenente i parametri passati tramite routing.
   */
  private setPagesParameters(
    navTarget: AzioniConfigurazione,
    state: IConfigurazioneRouteParams
  ) {
    // Verifico se esiste un target
    if (!navTarget) {
      return;
    }

    // Verifico qual è il target della navbar
    switch (navTarget) {
      case AzioniConfigurazione.canoni:
        // Recupero il relativo oggetto
        this.canoniConfigs = state[ConfigurazioneRouteKeys.canoni];
        break;
      // #
      case AzioniConfigurazione.tassiDiInteresse:
        // Recupero il relativo oggetto
        this.tassiInteresseConfigs =
          state[ConfigurazioneRouteKeys.tassiDiInteresse];
        break;
      // #
      case AzioniConfigurazione.parametriDellaDilazione:
        // Recupero il relativo oggetto
        this.parametriDilazioneConfigs =
          state[ConfigurazioneRouteKeys.parametriDellaDilazione];
        break;
      // #
      case AzioniConfigurazione.altriParametri:
        // Recupero il relativo oggetto
        this.altriParametriConfigs =
          state[ConfigurazioneRouteKeys.altriParametri];
        break;
      // #
    }
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione che aggiorna l'azione selezionata dall'utente.
   * @param azione RiscaAzioniConfigurazione che definisce la nuova azione.
   * @override
   */
  cambiaNav(azione: AzioniConfigurazione) {
    // Verifico se si sta premendo sulla tab già attiva
    const sameTab = this.lastTabActive === azione;
    // Check sulla variabile
    if (sameTab) {
      // Blocco le logiche
      return;
    }

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
   * @param azione RiscaAzioniConfigurazione con la tab che si sta cercando di caricare.
   */
  private gestisciFiloArianna(azione: AzioniConfigurazione) {
    // Mi sto spostando da un tab all'altro, devo rimuovere l'ultimo segmento
    this.rimuoviSegmentoConfigurazioni();

    // Definisco il livello radice della sezione
    let radice: FALivello = this._riscaFA.configurazioni;
    // Imposto il nuovo segmento in base al tab premuto
    let tabFA: FALivello;

    // Verifico quale tab si vuole caricare
    switch (azione) {
      case AzioniConfigurazione.canoni:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.configurazioneCanoni;
        break;
      case AzioniConfigurazione.tassiDiInteresse:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.tassiDiInteresse;
        break;
      case AzioniConfigurazione.parametriDellaDilazione:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.parametriDellaDilazione;
        break;
      case AzioniConfigurazione.altriParametri:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.altriParametri;
        break;
    }

    // Definisco il nuovo segmento per la tab
    this._riscaFA.aggiungiSegmentoByLivelli(radice, tabFA);
  }

  /**
   * Funzione di comodo che rimuove i segmenti del filo d'arianna per la sezione.
   */
  private rimuoviSegmentoConfigurazioni() {
    // Recupero i livelli che possono essere presenti nel filo d'arianna
    const ccFA = this._riscaFA.configurazioneCanoni;
    const tdiFA = this._riscaFA.tassiDiInteresse;
    const pddFA = this._riscaFA.parametriDellaDilazione;
    const apFA = this._riscaFA.altriParametri;
    // Effettuo le ricerche all'interno dei segmenti per il filo d'arianna
    const segmentoCC: FASegmento = this._riscaFA.segmentoInFAByLivello(ccFA);
    const segmentoTDI: FASegmento = this._riscaFA.segmentoInFAByLivello(tdiFA);
    const segmentoPDD: FASegmento = this._riscaFA.segmentoInFAByLivello(pddFA);
    const segmentoAP: FASegmento = this._riscaFA.segmentoInFAByLivello(apFA);
    // Definisco un'unica variabile che contiene uno dei possibili valori ritornati
    const segmento = segmentoCC ?? segmentoTDI ?? segmentoPDD ?? segmentoAP;

    // Verifico se è stato trovato un segmento
    if (segmento) {
      // Segmento di gestione trovato, elimino lui e i segmenti successivi
      this._riscaFA.rimuoviSegmento(segmento);
    }
  }

  /**
   * Funzione che gestisce lo stato dello step all'interno dalla navigazione mediante journey.
   * @param azione RiscaAzioniConfigurazione con la tab che si sta cercando di caricare.
   */
  private gestisciJStep(azione: AzioniConfigurazione) {
    // Definisco la chiave per la rimozione dello step da journey
    let praticaKey: string;

    // Verifico quale tab si vuole caricare
    switch (azione) {
      case AzioniConfigurazione.canoni:
        // Assegno la chiave del componente
        praticaKey = this.CAN_C.NAVIGATION_CONFIG.componentId;
        break;
      case AzioniConfigurazione.tassiDiInteresse:
        // Assegno la chiave del componente
        praticaKey = this.TDI_C.NAVIGATION_CONFIG.componentId;
        break;
      case AzioniConfigurazione.parametriDellaDilazione:
        // Assegno la chiave del componente
        praticaKey = this.PD_C.NAVIGATION_CONFIG.componentId;
        break;
      case AzioniConfigurazione.altriParametri:
        // Assegno la chiave del componente
        praticaKey = this.AP_C.NAVIGATION_CONFIG.componentId;
        break;
    }

    // Verifico che sia definita una chiave e che l'ultimo step di journey sia coerente con la chiave
    if (praticaKey && this._navigationHelper.isLastStep(praticaKey)) {
      // Rimuovo lo step dalla lista journey
      this._navigationHelper.removeStep(praticaKey);
    }
  }
}
