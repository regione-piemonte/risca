import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FASegmento } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaNavHelperComponent } from '../../../../shared/components/risca/risca-nav-helper/risca-nav-helper.component';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IRicerchePagamentiNavClassConfigs,
  RicerchePagamanetiNavClass,
} from '../../classes/navs/ricerche-pagamenti.nav.class';
import { TipiRicerchePagamenti } from './utilities/ricerche-pagamenti.enums';

@Component({
  selector: 'ricerche-pagamenti',
  templateUrl: './ricerche-pagamenti.component.html',
  styleUrls: ['./ricerche-pagamenti.component.scss'],
})
export class RicerchePagamentiComponent
  extends RiscaNavHelperComponent<TipiRicerchePagamenti>
  implements OnInit
{
  /** Input TipiRicerchePagamenti che definisce una specifica nav per la parte delle ricerche pagamenti. */
  @Input('tipoRicercaPagamenti') tipoRicercaPagamenti: TipiRicerchePagamenti;

  /** RicerchePagamanetiNavClass per gestire le riscaNav. */
  riscaNav: RicerchePagamanetiNavClass;

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

  /**
   * ngOnInit.
   */
  ngOnInit() {
    // Lancio l'init del componente
    this.initComponent();
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
    // Setup della nav tabs
    this.setupNavTabs();
  }

  /**
   * Funzione di setup per i dati della nav tabs.
   */
  private setupNavTabs() {
    // Costruisco l'oggetto di configurazione per la classe delle nav
    const c: IRicerchePagamentiNavClassConfigs = {};
    // Genero un oggetto per le riscaNav
    this.riscaNav = new RicerchePagamanetiNavClass(c);

    // Imposto il tab di default
    this.setManualNav();
    // Il filo d'arianna è vuoto, quindi non sono altre pagine a richiamare la sezione pratiche, lancio la gestione del filo
    this.gestisciFiloArianna(this.lastTabActive);
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
  cambiaNav(azione: TipiRicerchePagamenti) {
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
  private gestisciFiloArianna(azione: TipiRicerchePagamenti) {
    // Mi sto spostando da un tab all'altro, devo rimuovere l'ultimo segmento
    this.rimuoviSegmentoRicerche();

    // Imposto il nuovo segmento in base al tab premuto
    let tabFA: FALivello;

    // Verifico quale tab si vuole caricare
    switch (azione) {
      case TipiRicerchePagamenti.pagamenti:
        // Assegno la chiave del componente
        tabFA = this._riscaFA.ricercaPagamenti;
        break;
    }

    // Definisco il nuovo segmento per la tab
    this._riscaFA.aggiungiSegmentoByLivelli(tabFA);
  }

  /**
   * Funzione di comodo che rimuove i segmenti partendo dalla tab ricerche.
   */
  private rimuoviSegmentoRicerche() {
    // Recupero i 3 livelli che possono essere presenti nel filo d'arianna
    const rpFA = this._riscaFA.ricercaPagamenti;
    // Effettuo 3 ricerche all'interno dei segmenti per il filo d'arianna
    const segmentoRP: FASegmento = this._riscaFA.segmentoInFAByLivello(rpFA);
    // Definisco un'unica variabile che contiene uno dei possibili valori ritornati
    const segmento = segmentoRP ?? undefined;

    // Verifico se è stato trovato un segmento
    if (segmento) {
      // Segmento di gestione trovato, elimino lui e i segmenti successivi
      this._riscaFA.rimuoviSegmento(segmento);
    }
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di init del componente.
   */
  private initComponent() {
    // Lancio il set della navigazione sulla base di un possibile input
    this.initRouteParams();
  }

  /**
   * Funzione che gestisce l'inizializzazione della rotta tramite input.
   */
  private initRouteParams() {
    // Recupero la variabile per la nav d'aprire
    const navTarget: TipiRicerchePagamenti = this.tipoRicercaPagamenti;
    // Verifico se esiste la configurazione
    if (navTarget) {
      // Esiste la configurazione, cambio pagina
      this.setManualNav(navTarget);
    }
  }
}
