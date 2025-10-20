import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FASegmento } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaNavHelperComponent } from '../../../../shared/components/risca/risca-nav-helper/risca-nav-helper.component';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { IRiscaNavRouteParams } from '../../../../shared/utilities';
import { TipiGestioneVerifiche } from './utilities/gestione-verifiche.enums';
import { IGestioneVerificheRouteParams } from './utilities/gestione-verifiche.interfaces';
import {
  GestioneVerificheNavClass,
  IGestioneVerificheNavClassConfigs,
} from './utilities/gestione-verifiche.nav.class';

@Component({
  selector: 'gestione-verifiche',
  templateUrl: './gestione-verifiche.component.html',
  styleUrls: ['./gestione-verifiche.component.scss'],
})
export class GestioneVerificheComponent extends RiscaNavHelperComponent<TipiGestioneVerifiche> {
  /** PagamentoNavClass per gestire le riscaNav. */
  riscaNav: GestioneVerificheNavClass;

  /**
   * Costruttore.
   */
  constructor(
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    riscaUtilities: RiscaUtilitiesService,
    router: Router
  ) {
    // Richiamo il super del componente
    super(riscaAlert, riscaUtilities, router);
    // Richiamo il setup per il componente
    this.setupComponente();
  }

  // /**
  //  * ngOnInit.
  //  */
  // ngOnInit() {
  //   // Lancio l'init del componente
  //   this.initComponent();
  // }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
    // Setup della nav tabs
    this.setupNavTabs();
    // Effettuo il setup dei dati passati alla pagina
    this.setupRouteParams();
  }

  /**
   * Funzione di setup per i dati della nav tabs.
   */
  private setupNavTabs() {
    // Costruisco l'oggetto di configurazione per la classe delle nav
    const c: IGestioneVerificheNavClassConfigs = {};
    // Genero un oggetto per le riscaNav
    this.riscaNav = new GestioneVerificheNavClass(c);

    // Imposto il tab di default
    this.setManualNav();
    // Il filo d'arianna è vuoto, quindi non sono altre pagine a richiamare la sezione pratiche, lancio la gestione del filo
    this.gestisciFiloArianna(this.lastTabActive);
  }

  /**
   * Funzione che verifica se tramite il servizio di Route sono stati passati parametri di configurazione.
   * @override
   */
  protected setupRouteParams() {
    // Definisco l'oggetto state
    let state: IGestioneVerificheRouteParams;
    // Recupero l'oggetto state del routing
    state = this._riscaUtilities.getRouterState(this._router);

    // Verifico che esista lo state
    if (!state) {
      // Nessun dato passato, non faccio niente
      return;
    }

    // Recupero i parametri dallo state della route
    const { navTarget } = state;
    // Lancio la funzione di setup per target e parametri
    this.setupNavTargetAndParams(navTarget, state);
  }

  /**
   * Funzione pensata per l'override, che setta le configurazioni per la nav definita dall'input.
   * @param navTarget TipiGestioneVerifiche con il taget per la navbar.
   * @param state IRiscaNavRouteParams<IGestioneVerificheRouteParams> contenente i parametri passati tramite routing.
   */
  protected setNavTargetConfigs(
    navTarget: TipiGestioneVerifiche,
    state: IRiscaNavRouteParams<IGestioneVerificheRouteParams>
  ) {
    // Verifico se esiste un target
    if (!navTarget) {
      return;
    }

    // Esiste, modifico il target di default
    this.setManualNav(navTarget);
    // Aggiorno il filo d'arianna
    this.gestisciFiloArianna(this.lastTabActive);
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  // /**
  //  * Funzione di init del componente.
  //  */
  // private initComponent() {}

  /**
   * ##################
   * GESTIONE DELLE NAV
   * ##################
   */

  /**
   * Funzione che aggiorna l'azione selezionata dall'utente.
   * @param azione T che definisce la nuova azione.
   * @override
   */
  cambiaNav(azione: TipiGestioneVerifiche) {
    // Verifico se si sta premendo sulla tab già attiva
    const sameTab = this.lastTabActive === azione;
    // Check sulla variabile
    if (sameTab) {
      // Blocco le logiche
      return;
    }

    // Resetto possibili alert apert
    this.resetAlertConfigs();
    // Lancio la funzione per la gestione del filo d'arianna
    this.gestisciFiloArianna(azione);

    // Modifico il flag per l'azione
    this.lastTabActive = azione;
    // Aggiorno la visualizzazione dell'azione per la nav
    this.ngbNavList.select(this.tabActive);
  }

  /**
   * Funzione che gestisce le casistiche per il filo d'arianna.
   * Il livello digestione è basato sul tab selezionato per la navigazione.
   * @param azione RiscaIstanzePagamento con la tab che si sta cercando di caricare.
   */
  private gestisciFiloArianna(azione: TipiGestioneVerifiche) {
    // Mi sto spostando da un tab all'altro, devo rimuovere l'ultimo segmento
    this.rimuoviSegmentoRicerche();

    // Definisco il livello radice della sezione
    let radice: FALivello = this._riscaFA.verifiche;
    // Imposto il nuovo segmento in base al tab premuto
    let tabFA: FALivello;

    // Verifico quale tab si vuole caricare
    switch (azione) {
      case TipiGestioneVerifiche.morosita:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.ricercaMorosita;
        break;
      case TipiGestioneVerifiche.rimborsi:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.ricercaRimborsi;
        break;
    }

    // Definisco il nuovo segmento per la tab
    this._riscaFA.aggiungiSegmentoByLivelli(radice, tabFA);
  }

  /**
   * Funzione di comodo che rimuove i segmenti partendo dalla tab ricerche.
   */
  private rimuoviSegmentoRicerche() {
    // Recupero i 3 livelli che possono essere presenti nel filo d'arianna
    const rmFA = this._riscaFA.ricercaMorosita;
    const rrFA = this._riscaFA.ricercaRimborsi;
    // Effettuo 3 ricerche all'interno dei segmenti per il filo d'arianna
    const segmentoRM: FASegmento = this._riscaFA.segmentoInFAByLivello(rmFA);
    const segmentoRR: FASegmento = this._riscaFA.segmentoInFAByLivello(rrFA);
    // Definisco un'unica variabile che contiene uno dei possibili valori ritornati
    const segmento = segmentoRM ?? segmentoRR ?? undefined;

    // Verifico se è stato trovato un segmento
    if (segmento) {
      // Segmento di gestione trovato, elimino lui e i segmenti successivi
      this._riscaFA.rimuoviSegmento(segmento);
    }
  }
}
