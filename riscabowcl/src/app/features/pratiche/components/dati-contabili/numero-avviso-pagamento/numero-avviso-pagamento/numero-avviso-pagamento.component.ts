import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgbNav, NgbNavChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { StatoDebitorioVo } from '../../../../../../core/commons/vo/stato-debitorio-vo';
import { RiscaNav2ndLvlClasses } from '../../../../../../shared/components/risca/risca-nav-helper/utilities/risca-nav-helper.enums';
import { RiscaUtilitiesComponent } from '../../../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import {
  IRiscaNavLinkConfig,
  IRiscaTabChanges,
  RiscaNumeroAvvisoPagamento,
} from '../../../../../../shared/utilities';
import { NumeroAvvisoPagamentoNavClass } from '../../../../class/navs/numero-avviso-pagamento.nav.class';
import { DatiContabiliUtilityService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { NumeroAvvisoPagamentoService } from '../../../../service/dati-contabili/numero-avviso-pagamento.service';
import { StatiDebitoriNAPService } from '../../../../service/dati-contabili/stati-debitori-nap.service';
import { StatiDebitoriNAPConsts } from '../stati-debitori-nap/utilities/stati-debitori-nap.consts';
import { RiscaLockPraticaService } from '../../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';

@Component({
  selector: 'numero-avviso-pagamento',
  templateUrl: './numero-avviso-pagamento.component.html',
  styleUrls: ['./numero-avviso-pagamento.component.scss'],
})
export class NumeroAvvisoPagamentoComponent
  extends RiscaUtilitiesComponent
  implements OnInit, OnDestroy
{
  /** Costante per le informazioni del componente. */
  NAP_C = new StatiDebitoriNAPConsts();
  /** Oggetto contenente la configurazione per le classi di stile nav, di secondo livello. */
  nav2ndLvlClass: IRiscaNavLinkConfig = RiscaNav2ndLvlClasses;

  /** ViewChild collegato alla navigation bar. */
  @ViewChild('napNav') napNav: NgbNav;

  /** StatoDebitorioVo che identifica i dati dello stato debitorio in modifica/dettaglio. */
  statoDebitorio: StatoDebitorioVo;

  /** String che definisce il target dello stato debitorio. */
  sezioneSDNAP: RiscaNumeroAvvisoPagamento;
  /** NumeroAvvisoPagamentoNavClass per gestire la navigazione tra le pagine (l'header sarà nascosto). */
  sdNapNavConfigs: NumeroAvvisoPagamentoNavClass;

  /** StatoDebitorioVo con le informazioni dello stato debitorio selezionato. */
  statoDebitorioSelezionato: StatoDebitorioVo;

  /**
   * Costruttore
   */
  constructor(
    private _datiContabiliUtility: DatiContabiliUtilityService,
    navigationHelper: NavigationHelperService,
    private _numeroAvvisoPagamento: NumeroAvvisoPagamentoService,
    riscaAlert: RiscaAlertService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaMessages: RiscaMessagesService,
    private _statiDebitoriNAP: StatiDebitoriNAPService
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
    // Lancio il setup per i dati dello stato debitorio
    this.setupStatoDebitorio();
    // Lancio il setup della nav nascosta
    this.setupNavBar();
  }

  /**
   * Funzione di setup che verifica e gestisce i dati per lo stato debitorio.
   */
  private setupStatoDebitorio() {
    // Recupero lo stato debitorio dal servizio
    this.statoDebitorio = this._datiContabiliUtility.statoDebitorio;
  }

  /**
   * Funzione di setup per la nav bar del componente.
   */
  private setupNavBar() {
    // Lancio il setup della nav nascosta
    this.sdNapNavConfigs = new NumeroAvvisoPagamentoNavClass();
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
    // Funzioni di init del componente
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
    // Resetto il dato dell'ultima paginazione salvata
    this._statiDebitoriNAP.resetLastPagination();

    // Tento di fare l'unsubscribe degli events
    try {
    } catch (e) {}
  }

  /**
   * #######################
   * GESTIONE DELLE NAV TABS
   * #######################
   */

  /**
   * Funzione che aggiorna la sezione attiva.
   * @param sezione RiscaNumeroAvvisoPagamento che definisce la nuova sezione.
   */
  cambiaSezioneNAP(sezione: RiscaNumeroAvvisoPagamento) {
    // Modifico il flag per la sezione
    this.sezioneSDNAP = sezione;
    // Aggiorno la visualizzazione della sezione per la nav
    this.napNav.select(this.sezioneSDNAP);
  }

  /**
   * Funzione agganciata all'evento di cambio della tab per la nav bar.
   * @param nav NgbNavChangeEvent che definisce la nuova sezione.
   */
  onSezioneNAPChange(nav: NgbNavChangeEvent) {
    // Recupero dall'evento di cambio nav le informazioni
    const { activeId, nextId } = nav;
    // Emetto l'evento per informare i tab della pratica che sta avvenendo un cambio
    this.sezioneNAPChange(activeId, nextId);
  }

  /**
   * Funzione di comodo che crea i dati ed emette l'evento di cambio tab del componente.
   * @param actual RiscaNumeroAvvisoPagamento definisce l'id del tab attualmente attivo.
   * @param next RiscaNumeroAvvisoPagamento che definisce l'id del target della tab.
   */
  private sezioneNAPChange(
    actual: RiscaNumeroAvvisoPagamento,
    next: RiscaNumeroAvvisoPagamento
  ) {
    // Definisco l'oggetto per il tab changes
    const tabs: IRiscaTabChanges = { actual, next };
    // Emetto l'evento per informare i tab della pratica che sta avvenendo un cambio
    this._numeroAvvisoPagamento.sezioneNAPChanged(tabs);
  }

  /**
   * Funzione di comodo che modifica la sezione attiva in: lista stati debitori per NAP.
   */
  vaiAListaSDNap() {
    // Mi sposto sulla sezione della lista degli stati debitori per nap
    const sezione = RiscaNumeroAvvisoPagamento.statiDebitoriNAP;
    // Modifica la sezione attiva
    this.cambiaSezioneNAP(sezione);
  }

  /**
   * Funzione di comodo che modifica la sezione attiva in: dettaglio stato debitorio nap.
   */
  vaiADettaglioSDNap() {
    // Definisco la sezione d'attivare
    const sezione = RiscaNumeroAvvisoPagamento.dettaglioStatoDebitorioNAP;
    // Modifico la sezione attiva
    this.cambiaSezioneNAP(sezione);
  }

  /**
   * ##########################
   * EVENTI DI OUTPUT DEI FIGLI
   * ##########################
   */

  /**
   * Funzione collegata all'evento onDettaglioStatoDebitorio del componente stati-debitori-nap.
   * @param statoDebitorio StatoDebitorioVo contenente i dati dello stato debitorio seleizionato.
   */
  onDettaglioStatoDebitorio(statoDebitorio: StatoDebitorioVo) {
    // Verifico l'input
    if (!statoDebitorio) {
      // Nessuna configurazione
      return;
    }

    // Assegno localmente lo stato debitorio in dettaglio
    this.statoDebitorioSelezionato = statoDebitorio;
    // Modifico la sezione attiva
    this.vaiADettaglioSDNap();
  }

  /**
   * Funzione collegata all'evento onIndietro del componente di dettaglio dello stato debitorio nap.
   * @param payload any contenente delle informazioni aggiuntivo per l'indietro.
   */
  onTornaAStatiDebitoriNap(payload?: any) {
    // Pulisco dalla memoria lo stato debitorio selezionato
    this.statoDebitorioSelezionato = undefined;

    // Modifica la sezione attiva
    this.vaiAListaSDNap();
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.isPraticaLocked;
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
