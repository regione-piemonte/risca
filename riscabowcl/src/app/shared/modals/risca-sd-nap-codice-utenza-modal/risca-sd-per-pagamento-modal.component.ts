import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { RicercaPaginataResponse } from '../../../core/classes/http-helper/http-helper.classes';
import { RicercaStatiDebitoriPagamentoVo } from '../../../core/commons/vo/ricerca-stati-debitori-pagamento-vo';
import { StatoDebitorioVo } from '../../../core/commons/vo/stato-debitorio-vo';
import { NavigationHelperService } from '../../../core/services/navigation-helper/navigation-helper.service';
import { DatiContabiliService } from '../../../features/pratiche/service/dati-contabili/dati-contabili/dati-contabili.service';
import {
  PagamentoStatiDebitoriTable,
  SDSelezionatixPagamentoTable,
} from '../../classes/risca-table/dati-contabili/pagamento-stati-debitori.table';
import { RiscaTableComponent } from '../../components/risca/risca-table/risca-table.component';
import { RiscaTableDataConfig } from '../../components/risca/risca-table/utilities/risca-table.classes';
import { RiscaUtilitiesComponent } from '../../components/risca/risca-utilities/risca-utilities.component';
import { CommonConsts } from '../../consts/common-consts.consts';
import { RiscaSpinnerService } from '../../services/risca-spinner.service';
import { RiscaAlertService } from '../../services/risca/risca-alert.service';
import { RiscaMessagesService } from '../../services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../services/risca/risca-utilities/risca-utilities.service';
import {
  RiscaButtonConfig,
  RiscaButtonCss,
  RiscaServerError,
  RiscaTablePagination,
} from '../../utilities';
import { RiscaSDPerPagamentoConsts } from './utilities/risca-sd-per-pagamento-modal.consts';
import { IRiscaSDPerPagamentoModalConfig } from './utilities/risca-sd-per-pagamento-modal.interfaces';

@Component({
  selector: 'risca-sd-per-pagamento-modal',
  templateUrl: './risca-sd-per-pagamento-modal.component.html',
  styleUrls: ['./risca-sd-per-pagamento-modal.component.scss'],
})
export class RiscaSDPerPagamentoModalComponent
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** RiscaSDPerPagamentoConsts con le costanti del componente. */
  RSNCU_C = new RiscaSDPerPagamentoConsts();

  /** RiscaButtonConfig che definisce la struttura di default del pulsante: ANNULLA. */
  BTN_ANNULLA: RiscaButtonConfig = { label: this.RSNCU_C.LABEL_ANNULLA };
  /** RiscaButtonConfig che definisce la struttura di default del pulsante: CONFERMA. */
  BTN_CONFERMA: RiscaButtonConfig = { label: this.RSNCU_C.LABEL_CONFERMA };

  /** IRiscaSDNapCodiceUtenzaModalConfig che definisce i dati di configurazione per la modale. */
  @Input() dataModal: IRiscaSDPerPagamentoModalConfig;

  /** ViewChild che definisce la connessione al componente: RiscaTableComponent<StatoDebitorioVo>. */
  @ViewChild('sdTable') sdTable: RiscaTableComponent<StatoDebitorioVo>;

  /** PagamentoStatiDebitoriTable che conterrà le configurazioni per la tabella dei documenti e allegati. */
  statiDebitoriTable: PagamentoStatiDebitoriTable;
  /** SDSelezionatixPagamentoTable che conterrà gli stati debitorio selezionati dall'utente. */
  sdSelezionatixPagamentoTable: SDSelezionatixPagamentoTable;

  /** StatoDebitorio[] con la lista degli stati debitori selezionati dalla tabella. */
  statiDebitoriSelezionati: StatoDebitorioVo[] = [];
  /** StatoDebitorio[] con la lista degli stati debitori selezionati dalla tabella. */
  statiDebitoriSelezionatiRows: RiscaTableDataConfig<StatoDebitorioVo>[] = [];
  /** Boolean che gestisce l'apertura dell'accordion per gli stati debitori selezionati. */
  sdSelezionatiApriChiudi: boolean = false;
  /** Boolean che identifica come selezionati tutti gli stati debitori ricercati, anche quelli non visibili per la paginazione. */
  allSDSelezionati: boolean = false;

  /** RiscaButtonCss per la configurazione del css per il pulsante di cancellazione di uno stato debitorio selezionato. */
  cssX: RiscaButtonCss;
  /** RiscaButtonConfig per la configurazione dati per il pulsante di cancellazione di uno stato debitorio selezionato. */
  dataX: RiscaButtonConfig;

  /**
   * Costruttore.
   */
  constructor(
    public activeModal: NgbActiveModal,
    private _datiContabili: DatiContabiliService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService,
    private _riscaSpinner: RiscaSpinnerService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo del super della classe estesa
    super(navigationHelper, riscaAlert, riscaMessages);
    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup per le informazioni del componente.
   */
  private setupComponente() {
    // Setup per il pulsante di cancellazione delle righe degli stati debitori selezionati
    this.setupBtnDeleteSD();
    // Setup della tabella
    this.setupTabSD();
  }

  /**
   * Funzione per il setup delle configurazioni per la cancellazione di uno stato debitorio selezionato.
   */
  private setupBtnDeleteSD() {
    // Recupero dalle costanti le configurazioni del pulsante per il CSS
    const cssX: RiscaButtonCss = this.C_C.BTN_CLOSE_X_CSS;
    // Aggiungo alle classi di stile la specifica per il clore blu di risca
    cssX.customButton = `risca-blue-i ${cssX.customButton}`;
    // Assegno localmente le informazioni
    this.cssX = cssX;

    // Recupero dalle costanti le configurazioni del pulsante per i dati
    const dataX: RiscaButtonConfig = this.C_C.BTN_CLOSE_X_DATA;
    // Assegno localmente le informazioni
    this.dataX = dataX;
  }

  /**
   * Funzione di setup per l'inizializzazione della tabella degli stati debitori.
   */
  private setupTabSD() {
    // Inizializzo la tabella
    this.statiDebitoriTable = new PagamentoStatiDebitoriTable();
    this.sdSelezionatixPagamentoTable = new SDSelezionatixPagamentoTable();
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di init per le informazioni del componente.
   */
  private initComponente() {
    // Lancio la funzione di init per gestire eventuali dati pre caricati
    this.initRicercaDati();
  }

  /**
   * Funzione di init delegata alla gestione del flusso di caricamento/ricerca dati per gli stati debitori.
   * La funzione verificherà se, all'interno dei parametri in input, esistono informazioni pre-caricate e gestirà un flusso specifico.
   */
  private initRicercaDati() {
    // Recupero i possibili parametri di pre-caricamento
    let preLoadedSearch: RicercaPaginataResponse<StatoDebitorioVo[]>;
    preLoadedSearch = this.preLoadedSearch;
    let preLoadedError: RiscaServerError;
    preLoadedError = this.preLoadedError;

    // Verifico se esistono dati pre-caricati di ricerca
    if (preLoadedSearch) {
      // Esistono già i dati, carico la tabella
      this.onRicercaStatiDebitori(preLoadedSearch);
      // #
    } else if (preLoadedError) {
      // Il pre caricamento è fallito, gestisco la segnalazione
      this.onServiziError(preLoadedError);
      // #
    } else {
      // Non ci sono dati pre-caricati, lancio la ricerca degli stati debitori
      this.ricercaStatiDebitoriPaginati();
      // #
    }
  }

  /**
   * #######################
   * FUNZIONI PER LA TABELLA
   * #######################
   */

  /**
   * Questa funzione ricarica la ricerca semplice pratiche ma passa anche i dati della paginazione al BE,
   * così da cambiare pagina
   * @param $event RiscaTablePagination con i dati della paginazione
   */
  cambiaPagina(paginazione: RiscaTablePagination) {
    // Avvio la ricerca
    this.ricercaStatiDebitoriPaginati(paginazione);
  }

  /**
   * Funzione che effettua la ricerca delle informazioni per gli stati debitori.
   * A seconda della configurazione della modale verranno impostati dei filtri di ricerca.
   * @param paginazione RiscaTablePagination con i dati per la paginazione per la ricerca.
   */
  private ricercaStatiDebitoriPaginati(paginazione?: RiscaTablePagination) {
    // Se non c'è la paginazione, metto quella di default
    if (!paginazione) {
      // Recupero la paginazione di default dalla tabella
      paginazione = this.statiDebitoriTable.getDefaultPagination();
    }

    // Recupero l'oggetto contenente i criteri di ricerca
    const ricerca: RicercaStatiDebitoriPagamentoVo = this.ricerca;
    // Richiamo la ricerca per NAP
    this.ricercaSDxPagamentoPaginata(ricerca, paginazione);
    // #
  }

  /**
   * Funzione richiamata per la ricerca degli stati debitori per un pagamento.
   * @param paginazione RiscaTablePagination con la configurazione per la paginazione dei dati.
   */
  private ricercaSDxPagamentoPaginata(
    ricerca: RicercaStatiDebitoriPagamentoVo,
    paginazione: RiscaTablePagination
  ) {
    // Attivo manualmente uno spinner
    this.showSpinner();

    // Richiamo il servizio per lo scarico dati
    this._datiContabili
      .getStatiDebitorixPagamentoPagination(ricerca, paginazione)
      .subscribe({
        next: (response: RicercaPaginataResponse<StatoDebitorioVo[]>) => {
          // Richiamo la funzione per gestire i risultati della ricerca
          this.onRicercaStatiDebitori(response);
          // Chiuso lo spinner
          this.hideSpinner();
          // #
        },
        error: (e: RiscaServerError) => {
          // Gestisco il messaggio d'errore inaspettato
          this.onServiziError(e);
          // Chiuso lo spinner
          this.hideSpinner();
          // #
        },
      });
  }

  /**
   * Funzione richiamata nel momento in cui è stata completata la ricerca per gli stati debitori, dato il: NAP.
   * @param response RicercaPaginataResponse<StatoDebitorioVo[]> con la risposta paginata degli stati debitori.
   */
  private onRicercaStatiDebitori(
    response: RicercaPaginataResponse<StatoDebitorioVo[]>
  ) {
    // Estraggo dalla risposta le informazioni utili
    const statiDebitori: StatoDebitorioVo[] = response.sources;
    const paginazione: RiscaTablePagination = response.paging;

    // Inserisco i dati trovati nella tabella dei risultati
    this.statiDebitoriTable.setElements(statiDebitori);
    // Aggiorno la paginazione
    this.statiDebitoriTable.updatePaginazioneAfterSearch(paginazione);
  }

  /**
   * Funzione che effettua la ricerca delle informazioni per gli stati debitori.
   * A seconda della configurazione della modale verranno impostati dei filtri di ricerca.
   * La funzione ritornerà risultati non paginati.
   * @returns Observable<StatoDebitorioVo[]> con la ricerca senza paginazione degli stati debitori.
   */
  private ricercaStatiDebitori(): Observable<StatoDebitorioVo[]> {
    // Verifico la modalità di gestione della ricerca stati debitori
    if (this.ricerca) {
      // Richiamo la ricerca senza paginazione
      return this.getStatiDebitorixPagamento(this.ricerca);
      // #
    } else {
      // Caso di default, non dovrebbe mai finire qui, ma non si sa mai
      return this._riscaUtilities.responseWithDelay([]);
    }
  }

  /**
   * Funzione richiamata per la ricerca degli stati debitori.
   * La funzione ritornerà i risultati di ricerca non paginati.
   * [RISCA-ISSUES-35]: la funzione è stata modificata in base alla richiesta utente. Non c'è la ricerca stati debitori x nap o codice utenza, ma è diventata una ricerca più simile a quella avanzata delle riscossioni, ma per gli stati debitori.
   * @param ricerca RicercaStatiDebitoriPagamentoVo con le informazioni di ricerca dati.
   * @returns Observable<StatoDebitorioVo[]> con le informazioni scaricate.
   */
  private getStatiDebitorixPagamento(
    ricerca: RicercaStatiDebitoriPagamentoVo
  ): Observable<StatoDebitorioVo[]> {
    // Attivo manualmente uno spinner
    this.showSpinner();

    // Richiamo il servizio per lo scarico dati
    return this._datiContabili.getStatiDebitorixPagamento(ricerca).pipe(
      tap((res: StatoDebitorioVo[]) => {
        // Chiuso lo spinner
        this.hideSpinner();
      })
    );
  }

  /**
   * #############################
   * FUNZIONI PER GESTIONE TABELLA
   * #############################
   */

  /**
   * Funzione invocata al momento della selezione di tutte le righe della tabella, tramite checkbox nella riga di testata.
   * @param rows RiscaTableDataConfig<StatoDebitorioVo[]> con tutte le righe selezionate/deselezionate.
   */
  onTuttiSDSelezionati(rows: RiscaTableDataConfig<StatoDebitorioVo[]>) {
    // Aggiorno il valore nella classe della tabella di ricerca
    this.statiDebitoriTable.tutteLeRigheSelezionate = false;
    this.statiDebitoriTable.sourceSelected = [];
    // Aggiorno il valore nella classe della tabella dei selezionati
    this.sdSelezionatixPagamentoTable.clear();

    // Resetto gli stati debitori selezionati
    this.statiDebitoriSelezionati = [];
    this.statiDebitoriSelezionatiRows = [];
    // Aggiorno il flag per la selezione di tutte le righe
    this.allSDSelezionati = !this.allSDSelezionati;
  }

  /**
   * Funzione invocata al momento della selezione o deselezione di una riga nella tabella.
   * @param row RiscaTableDataConfig<StatoDebitorioVo> con le informazioni della riga selezionata.
   */
  onStatiDebitoriSelezionati(rows: RiscaTableDataConfig<StatoDebitorioVo>[]) {
    // Recupero dall'input solo le informazioni degli stati debitori
    let sd: StatoDebitorioVo[];
    sd = rows?.map((r: RiscaTableDataConfig<StatoDebitorioVo>) => {
      // Estraggo solo l'oggetto dello stato debitorio
      return r.original;
    });

    // Assegno localmente le informazioni
    this.statiDebitoriSelezionati = sd;
    // Aggiorno la tabella degli stati debitori selezionati
    this.sdSelezionatixPagamentoTable.setElements(sd);
  }

  /**
   * ###############################
   * FUNZIONI PER GESTIONE ACCORDION
   * ###############################
   */

  /**
   * Funzione che gestisce il toggle per l'apertura del container per gli stati debitori selezionati.
   * @param status boolean con il valore del check dell'accordion.
   */
  toggleSDSelezionati(status: boolean) {
    // Inverto il valore del flag dell'accordion
    this.sdSelezionatiApriChiudi = !this.sdSelezionatiApriChiudi;
  }

  /**
   * Funzione che rimuove uno stato debitorio selezionato dalla tabella dei selezionati e aggiorna il check nella tabella principale.
   * @param row RiscaTableDataConfig<StatoDebitorioVo> con il dato originale.
   */
  rimuoviSDSelezionato(row: RiscaTableDataConfig<StatoDebitorioVo>) {
    // Verifico l'input
    if (!row) {
      // Nessuna configurazione
      return;
    }

    // Estraggo lo stato debitorio da gestire
    const sdToDelete: StatoDebitorioVo = row.original;

    // Vado a recuperare la posizione dello stato debitorio da cancellare
    const iSD = this.statiDebitoriSelezionati?.findIndex(
      (sd: StatoDebitorioVo) => {
        // Verifico per stesso id
        return sd.id_stato_debitorio === sdToDelete.id_stato_debitorio;
      }
    );
    // Verifico se è stato trovato l'oggetto
    if (iSD !== -1) {
      // Esiste l'id, rimuovo l'elemento nella lista
      this.statiDebitoriSelezionati.splice(iSD, 1);
      // Vado ad aggiornare la selezione all'interno della tabella
      this.sdTable.updateSelectedRowsByOrigin(this.statiDebitoriSelezionati);
      // Rimuovo l'elemento dalla tabella di tracciamento
      this.sdSelezionatixPagamentoTable.removeRow(row);
    }
  }

  /**
   * ######################
   * GESTIONE DELLO SPINNER
   * ######################
   */

  /**
   * Funzione di comodo per l'avvio di uno spinner specifico.
   */
  private showSpinner() {
    // Recupero la chiave dello spinner specifico
    const k = this.RSNCU_C.SPINNER;
    // Lancio lo spinner
    this._riscaSpinner.show(k);
  }

  /**
   * Funzione di comodo per la chisura dello spinner specifico.
   */
  private hideSpinner() {
    // Recupero la chiave dello spinner specifico
    const k = this.RSNCU_C.SPINNER;
    // Lancio lo spinner
    this._riscaSpinner.hideAll();
  }

  /**
   * ##################################
   * FUNZIONI PER GESTIONE DELLA MODALE
   * ##################################
   */

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalDismiss() {
    // Dismiss della modale
    this.activeModal.dismiss();
  }

  /**
   * Funzione che effettua cancel dei dati selezionati.
   */
  modalCancel() {
    // Chiudo l'accordion
    this.sdSelezionatiApriChiudi = false;
    // Cancello gli elementi all'interno della lista degli stati debitori
    this.statiDebitoriSelezionati = [];
    // Resetto i dati in tabella
    this.sdSelezionatixPagamentoTable?.clear();
    // Lancio la cancellazione degli elementi selezioniati + paginazione della tabella
    this.sdTable?.resetSelectedRows();
    // Recupero la paginazione di default
    let paginazione: RiscaTablePagination;
    paginazione = this.statiDebitoriTable.getDefaultPagination();
    // Rilancio la paginazione partendo dalla prima
    this.sdTable?.cambioPagina(paginazione);
  }

  /**
   * Funzione che effettua la close (e quindi la resolved) della modale.
   */
  modalConfirm() {
    // Verifico se l'utente ha fatto una selezione manuale o totale degli stati debitori
    if (this.allSDSelezionati) {
      // Ha selezionato tutti gli stati debitori
      this.modalConfirmAllSDSelezionati();
      // #
    } else {
      // Recupero gli stati debitori selezionati dall'utente
      let sdSel: StatoDebitorioVo[] = this.statiDebitoriSelezionati;
      // Ha selezionato singolarmente gli stati debitori
      this.modalConfirmStatiDebitoriSelezionati(sdSel);
    }
  }

  /**
   * Funzione che gestisce il ritorno dei dati al componente chiamante, a seguito della confirm della modale, quando l'utente ha flaggato dalla tabella tutti gli stati debitori.
   */
  private modalConfirmAllSDSelezionati() {
    // Verifico se la tabella ha una sola pagina
    let p: RiscaTablePagination;
    p = this.statiDebitoriTable?.getPaginazione();
    // Dalla paginazione estraggo il totale di elementi e gli elementi per pagina
    const total: number = p?.total;
    const elementsForPage: number = p?.elementsForPage;
    // Verifico che esistano dati
    const existTotal: boolean = total != undefined;
    const existEFP: boolean = elementsForPage != undefined;

    // Verifico se esistono elementi nella tabella e sono tutti visualizzati in pagina
    if (existTotal && existEFP && total <= elementsForPage) {
      // Ho già tutte le informazioni in pagina, recupero i dati
      let statiDebitoriTab: StatoDebitorioVo[];
      statiDebitoriTab = this.statiDebitoriTable.getDataSource();
      // Richiamo la funzione passando gli stati debitori dalla tabella
      this.modalConfirmStatiDebitoriSelezionati(statiDebitoriTab);
      // #
    } else {
      // Dato che l'utente ha selezionato tutti gli stati debitori e sono paginati, richiamo la ricerca senza paginazione
      this.ricercaStatiDebitori().subscribe({
        next: (statiDebitori: StatoDebitorioVo[]) => {
          // Richiamo la funzione passando gli stati debitori scaricati
          this.modalConfirmStatiDebitoriSelezionati(statiDebitori);
          // #
        },
        error: (e: RiscaServerError) => {
          // Lancio la gestione dell'errore
          this.onServiziError(e);
        },
      });
    }
  }

  /**
   * Funzione che gestisce il ritorno dei dati al componente chiamante, a seguito della confirm della modale, quando l'utente ha selezionato e gestito manualmente gli stati debitori.
   * @param statiDebitoriSelezionati StatoDebitorioVo[] con la lista degli stati debitori selezionati dall'utente e da ritornare al chiamate.
   */
  private modalConfirmStatiDebitoriSelezionati(
    statiDebitoriSelezionati?: StatoDebitorioVo[]
  ) {
    // Recupero gli stati debitori selezionati
    let sd: StatoDebitorioVo[] = statiDebitoriSelezionati ?? [];

    // Per la gestione degli stati debitori, devo "svuotare" la proprietà importo_versato per permettere all'utente di popolarlo
    let sdClear: StatoDebitorioVo[];
    sdClear = this.statiDebitoriPerChiamante(sd);

    // Close della modale
    this.activeModal.close(sdClear);
  }

  /**
   * Funzione che prepara i dati per il ritorno al componente chiamate.
   * Gli stati debitori passati in input verranno manipolati per poterli poi gestire dalla pagina chiamante.
   * @param sd StatoDebitorioVo[] con la lista di stati debitori da perparare per il ritorno al chiamate.
   * @returns StatoDebitorioVo[] elaborati.
   */
  private statiDebitoriPerChiamante(
    sd: StatoDebitorioVo[]
  ): StatoDebitorioVo[] {
    // Verifico l'input
    sd = sd ?? [];
    // Per il momento c'è da fare una sola manipolazione, ritorno direttamente il dato
    return sd.map((s: StatoDebitorioVo, i: number) => {
      // Imposto ad undefined l'importo versato
      s.importo_versato = undefined;
      // Ritorno l'oggetto
      return s;
    });
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per il titolo della modale.
   * @readonly string con il titolo della modale.
   */
  get title(): string {
    // Recupero la base fissa del titolo
    let title = this.RSNCU_C.TITLE_MODALE;
    // Ritorno il titolo
    return title;
  }

  /**
   * Getter di comodo per i dati di ricerca.
   * @returns RicercaStatiDebitoriPagamentoVo con le informazioni per la ricerca.
   */
  get ricerca(): RicercaStatiDebitoriPagamentoVo {
    // Recupero dall'oggetto di configurazione la modalità
    return this.dataModal?.ricerca;
  }

  /**
   * Getter di comodo per i dati di ricerca pre-caricati.
   * @returns RicercaPaginataResponse<StatoDebitorioVo[]> con le informazioni di ricerca per i dati pre-caricati.
   */
  get preLoadedSearch(): RicercaPaginataResponse<StatoDebitorioVo[]> {
    // Recupero dall'oggetto di configurazione la modalità
    return this.dataModal?.preLoadedSearch;
  }

  /**
   * Getter di comodo per i dati di ricerca pre-caricati.
   * @returns RiscaServerError con le informazioni di errore per i dati pre-caricati.
   */
  get preLoadedError(): RiscaServerError {
    // Recupero dall'oggetto di configurazione la modalità
    return this.dataModal?.preLoadedError;
  }

  /**
   * Getter di verifica per la tabella degli stati debitori.
   * @returns boolean con il risultato del check.
   */
  get checkStatiDebitoriTable(): boolean {
    // Verifico che la tabella esista e abbia dati
    return this.statiDebitoriTable?.source?.length > 0;
  }

  /**
   * Getter di verifica degli elementi per gli stati debitori selezionati.
   * @returns boolean con il risultato del check.
   */
  get checkStatiDebitoriSelezionati(): boolean {
    // Verifico che la tabella esista e abbia dati
    return this.statiDebitoriSelezionati?.length > 0;
  }

  /**
   * Getter di comodo che verifica se esistono dati all'interno della tabella per la visualizzazione dei dati selezionati.
   * @returns boolean con il risultato del check della tabella.
   */
  get checkTableTracciatore(): boolean {
    // Verifico se esiste la tabella e ha dei dati al suo interno
    return this.sdSelezionatixPagamentoTable?.source?.length > 0;
  }
}
