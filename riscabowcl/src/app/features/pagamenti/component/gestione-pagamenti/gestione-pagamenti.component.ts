import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FASegmento } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaNavHelperComponent } from '../../../../shared/components/risca/risca-nav-helper/risca-nav-helper.component';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  GestionePagamentiNavClass,
  IGestionePagamentiNavClassConfigs,
} from '../../classes/navs/gestione-pagamenti.nav.class';
import { RiscaAzioniGestionePagamenti } from './utilities/gestione-pagamenti.enums';
import { IGestionePagamentiRouteParams } from './utilities/gestione-pagamenti.interfaces';
import { TipiRicerchePagamenti } from '../ricerche-pagamenti/utilities/ricerche-pagamenti.enums';

@Component({
  selector: 'gestione-pagamenti',
  templateUrl: './gestione-pagamenti.component.html',
  styleUrls: ['./gestione-pagamenti.component.scss'],
})
export class GestionePagamentiComponent
  extends RiscaNavHelperComponent<RiscaAzioniGestionePagamenti>
  implements OnInit
{
  /** PagamentoNavClass per gestire le riscaNav. */
  riscaNav: GestionePagamentiNavClass;

  /** TipiRicerchePagamenti che definisce una specifica nav per la parte delle ricerche pagamenti. */
  tipoRicercaPagamenti: TipiRicerchePagamenti;

  /**
   * Costruttore.
   */
  constructor(
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
    // Setup delle navs
    this.setupNavs();
    // Effettuo il setup dei dati passati alla pagina
    this.setupRouteParams();
  }

  /**
   * Funzione di setup per l'inizializzazione delle navs.
   */
  private setupNavs() {
    // Costruisco l'oggetto di configurazione per la classe delle nav
    const c: IGestionePagamentiNavClassConfigs = {};
    // Genero un oggetto per le riscaNav
    this.riscaNav = new GestionePagamentiNavClass(c);

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
    let state: IGestionePagamentiRouteParams;
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
   * @param navTarget any con il taget per la navbar.
   * @param state IGestionePagamentiRouteParams contenente i parametri passati tramite routing.
   * @override
   */
  protected setNavTargetConfigs(
    navTarget: any,
    state: IGestionePagamentiRouteParams
  ) {
    // Verifico se esiste un target
    if (!navTarget) {
      return;
    }

    // Recupero i parametri dallo state della route
    const { tipoRicercaPagamenti } = state || {};
    // Verifico se è stato definita una sotto nav
    if (tipoRicercaPagamenti) {
      // Salvo l'informazione localmente e la passo al componente
      this.tipoRicercaPagamenti = tipoRicercaPagamenti;
    }
  }

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
  cambiaNav(azione: RiscaAzioniGestionePagamenti) {
    // Verifico se si sta premendo sulla tab già attiva
    const sameTab = this.lastTabActive === azione;
    // Check sulla variabile
    if (sameTab) {
      // Blocco le logiche
      return;
    }

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
   * @param azione RiscaAzioniGestionePagamenti con la tab che si sta cercando di caricare.
   */
  private gestisciFiloArianna(azione: RiscaAzioniGestionePagamenti) {
    // Mi sto spostando da un tab all'altro, devo rimuovere l'ultimo segmento
    this.rimuoviSegmentoPagamenti();

    // Imposto il nuovo segmento in base al tab premuto
    let tabFA: FALivello;

    // Verifico quale tab si vuole caricare
    switch (azione) {
      case RiscaAzioniGestionePagamenti.ricerche:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.ricerchePagamenti;
        break;
      case RiscaAzioniGestionePagamenti.pagamentiDaVisionare:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.pagamentiDaVisionare;
        break;
    }

    // Definisco il nuovo segmento per la tab
    this._riscaFA.aggiungiSegmentoByLivelli(tabFA);
  }

  /**
   * Funzione di comodo che rimuove i segmenti partendo dalla tab dei pagamenti.
   */
  private rimuoviSegmentoPagamenti() {
    // Recupero i 3 livelli che possono essere presenti nel filo d'arianna
    const rpFA = this._riscaFA.ricerchePagamenti;
    const pdvFA = this._riscaFA.pagamentiDaVisionare;
    // Effettuo 3 ricerche all'interno dei segmenti per il filo d'arianna
    const segmentoRP: FASegmento = this._riscaFA.segmentoInFAByLivello(rpFA);
    const segmentoPDV: FASegmento = this._riscaFA.segmentoInFAByLivello(pdvFA);
    // Definisco un'unica variabile che contiene uno dei possibili valori ritornati
    const segmento = segmentoRP ?? segmentoPDV ?? undefined;

    // Verifico se è stato trovato un segmento
    if (segmento) {
      // Segmento di gestione trovato, elimino lui e i segmenti successivi
      this._riscaFA.rimuoviSegmento(segmento);
    }
  }
}
