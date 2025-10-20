import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { cloneDeep } from 'lodash';
import { DettaglioPagamentoIdVo } from 'src/app/core/commons/vo/dettaglio-pagamento-id-vo';
import { AssegnaPagamento } from '../../../../../core/commons/vo/assegna-pagamento-vo';
import { DettaglioPagSearchResultVo } from '../../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { DettaglioPagamentoExtraVo } from '../../../../../core/commons/vo/dettaglio-pagamento-extra';
import { PagamentoVo } from '../../../../../core/commons/vo/pagamento-vo';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { AccessoElementiAppKeyConsts } from '../../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../../core/services/accesso-elementi-app.service';
import { NavigationHelperService } from '../../../../../core/services/navigation-helper/navigation-helper.service';
import { DettPagExtraTable } from '../../../../../shared/classes/risca-table/pagamenti-sd/pagamenti-sd.table';
import { RiscaPagamentoManualeComponent } from '../../../../../shared/components/risca/risca-pagamento-manuale/risca-pagamento-manuale.component';
import { RiscaTableDataConfig } from '../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { RiscaUtilitiesComponent } from '../../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaAlertService } from '../../../../../shared/services/risca/risca-alert.service';
import { RiscaLockPraticaService } from '../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaMessagesService } from '../../../../../shared/services/risca/risca-messages.service';
import { RiscaModalService } from '../../../../../shared/services/risca/risca-modal.service';
import {
  AppClaimants,
  ICallbackDataModal,
  RiscaServerError,
} from '../../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../../shared/utilities/enums/risca-notify-codes.enums';
import { DettaglioPagamentiService } from '../../../../pagamenti/service/dettaglio-pagamenti/dettaglio-pagamenti.service';
import { IConfirmModificaPag } from '../../../modal/pagamento-sd-modal/utilities/pagamento-sd-modal.interfaces';
import { DatiContabiliModalService } from '../../../service/dati-contabili/dati-contabili/dati-contabili-modal.service';
import { DatiContabiliUtilityService } from '../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { PagamentiSDService } from '../../../service/dati-contabili/pagamenti-sd.service';
import { IModificaVersamentoModalConfigs } from '../../../service/dati-contabili/utilities/dati-contabili-modal.interfaces';
import { IDettagliPagamentoPerModifica } from '../../../service/dati-contabili/utilities/pagamenti-sd.interfaces';
import { VersamentiService } from '../../../service/dati-contabili/versamenti/versamenti.service';
import { PagamentiSDConsts } from './utilities/pagamenti-sd.consts';

@Component({
  selector: 'pagamenti-sd',
  templateUrl: './pagamenti-sd.component.html',
  styleUrls: ['./pagamenti-sd.component.scss'],
})
export class PagamentiSDComponent
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Oggetto contenente una serie di costanti per la gestione del componente. */
  PSD_C = PagamentiSDConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** StatoDebitorioVo con le informazioni dello stato debitorio. */
  statoDebitorio: StatoDebitorioVo;

  /** VersamentiTable che definisce la tabella di gestione dati per i dati dei versamenti. */
  dettPagExtraTable: DettPagExtraTable;

  /** boolean per la gestione dell'accordion per inserimento pagamento manuale. */
  pagamentoApriChiudi: boolean = false;
  /** boolean per la gestione di visualizzazione della struttura specifica di dettaglio di un pagamento bollettazione. */
  visualizzaDettaglio: boolean = false;

  /** ViewChild collegato al componente per il versamento manuale. */
  @ViewChild('riscaInsVersamento')
  riscaInsVersamento: RiscaPagamentoManualeComponent;

  /**
   * Costruttore
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _datiContabiliModal: DatiContabiliModalService,
    private _dettaglioPagamenti: DettaglioPagamentiService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService,
    private _pagamentiSD: PagamentiSDService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaModal: RiscaModalService,
    private _versamenti: VersamentiService
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
   * Funzione di setup del componente.
   */
  private setupComponente() {
    // Lancio il setup dei dati dello stato debitorio
    this.setupStatoDebitorio();
    // Lancio il setup per la tabella degli stati debitori (N.B.: DA FARE DOPO SETUP PRATICA!)
    this.setupTableVersamenti();
    // Lancio la funzione di setup di route params e snapshot
    this.setupRouteParams();
  }

  /**
   * Funzione di comodo per il recupero dell'informazione dello stato debitorio dal servizio condiviso.
   */
  private setupStatoDebitorio() {
    // Recupero dal servizio condiviso le informazioni per lo stato debitorio
    this.statoDebitorio = this._datiContabiliUtility.statoDebitorio;
  }

  /**
   * Funzione di setup per la tabella dello stato debitorio.
   */
  private setupTableVersamenti(dettPagExtra: DettaglioPagamentoExtraVo[] = []) {
    // Genero la tabella dei dati per la pagina
    this.dettPagExtraTable = new DettPagExtraTable({
      dettPagExtra,
      disableUserInputs: this.disableUserInputs,
    });
  }

  /**
   * Funzione dedicata al caricamento dei route params e dal pre-caricamento mediante snapshot.
   */
  private setupRouteParams() {
    // Carico i dati della snapshot
    this.loadJSnapshot(this);
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Init
   */
  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  /**
   * Funzione di init per il componente.
   */
  private initComponente() {
    // Recupero l'idRiscossione
    const idSD = this.statoDebitorio.id_stato_debitorio;
    const idR = this.idRiscossione;
    // Recupero la lista di annualità
    this._versamenti.getDettagliPagamenti(idR, idSD).subscribe({
      next: (dettPagExtra: DettaglioPagamentoExtraVo[]) => {
        // Setto gli elementi nella tabella
        this.dettPagExtraTable.setElements(dettPagExtra);
        // #
      },
      error: (error: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(error);
      },
    });
  }

  /**
   * ##################
   * MODIFICA PAGAMENTO
   * ##################
   */

  /**
   * Modifica il record in input dalla tabella delle attività rimborsi.
   * @param row RiscaTableDataConfig<DettaglioPagamentoExtraVo> da modificare.
   */
  modificaPagamento(row: RiscaTableDataConfig<DettaglioPagamentoExtraVo>) {
    // Verifico l'esistenza
    if (!row) {
      return;
    }
    // Prendo l'elemento
    let dettPagExt: DettaglioPagamentoExtraVo;
    dettPagExt = cloneDeep(row.original);

    // Recupero l'id pagamento
    const idPag = dettPagExt?.pagamento?.id_pagamento;

    // Richiamo il servizio per lo scarico delle informazioni aggiuntive per il pagamento
    this._pagamentiSD.dettagliPagamentoPerModifica(idPag).subscribe({
      next: (res: IDettagliPagamentoPerModifica) => {
        // Estraggo dalla response le informazioni
        const pag = res.pagamento;
        const dettPagSearch = res.dettagliPagamentoSearch ?? [];

        // Passo le informazioni per la gestione del pagamento
        this.onDettagliPagamentoPerModifica(pag, dettPagSearch);
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione dell'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione che gestisce la metodologia di gestione del dato del pagamento per la modifica.
   * @param pagamento PagamentoVo con le informazioni per la gestione del pagamento.
   * @param dettPagSearch DettaglioPagSearchResultVo[] con le informazioni aggiuntive per la gestione del pagamento.
   */
  private onDettagliPagamentoPerModifica(
    pagamento: PagamentoVo,
    dettPagSearch: DettaglioPagSearchResultVo[]
  ) {
    // Verifico l'input
    if (!pagamento) {
      // Non c'è l'oggetto di modifica
      return;
    }

    // Verifico se il pagamento è manuale
    if (this.isPagamentoManuale(pagamento)) {
      // Pagamento manuale, apro la modale
      this.apriModalePagamento(pagamento, dettPagSearch);
      // #
    } else {
      // Pagamento bollettazione, apro una nuova pagina
      this.apriDettaglioPagamento(pagamento, dettPagSearch);
    }
  }

  /**
   * ##########################
   * MODIFICA PAGAMENTO MANUALE
   * ##########################
   */

  /**
   * Funzione che gestisce l'apertura della modale per la modifica del pagamento.
   * @param pagamento PagamentoVo con le informazioni per la gestione del pagamento.
   * @param dettPagSearch DettaglioPagSearchResultVo[] con le informazioni aggiuntive per la gestione del pagamento.
   */
  private apriModalePagamento(
    pagamento: PagamentoVo,
    dettPagSearch: DettaglioPagSearchResultVo[]
  ) {
    // Dichiaro le operazioni di callback
    const onConfirm = (payload: IConfirmModificaPag) => {
      // Richiamo la conferma modifica pagamento
      this.onModificaPagamentoConfirm(payload);
    };

    // Definisco l'oggetto per le callback
    const cbs: ICallbackDataModal = { onConfirm };
    // Definisco l'oggetto per la configurazione della modale
    const configs: IModificaVersamentoModalConfigs = {
      pagamento: pagamento,
      dettagliPagamentoSearch: dettPagSearch,
    };

    // Apro la modale di modifica
    this._datiContabiliModal.openModificaPagamendoSDModal(configs, cbs);
  }

  /**
   * Funzione invocata alla conferma della modifica del pagamento tramite modale.
   * @param payload IConfirmModificaPag con le informazioni ritornate dalla modale.
   */
  private onModificaPagamentoConfirm(payload: IConfirmModificaPag) {
    // Recupero dall'oggetto le informazioni ritornate
    const pag = payload.pagamento;
    const detPagCanc = payload.dettagliPagCancellati;
    // Recupero le informazioni per la gestione dati
    const idSD = this.statoDebitorio.id_stato_debitorio;
    const idR = this.idRiscossione;

    // Aggiorno il pagamento e aggiorno la lista in tabella
    this._pagamentiSD
      .aggiornaPagamentoECancellaDettagli(pag, detPagCanc, idR, idSD)
      .subscribe({
        next: (dettPagExtra: DettaglioPagamentoExtraVo[]) => {
          // Aggiorno la pagina
          this.onPagamentoSalvato(dettPagExtra);
          // #
        },
        error: (error: RiscaServerError) => {
          // Gestisci errore
          this.onServiziError(error);
        },
      });
  }

  /**
   * ################################
   * MODIFICA PAGAMENTO BOLLETTAZIONE
   * ################################
   */

  /**
   * Funzione che gestisce l'apertura della pagina di dettaglio per la modifica del pagamento.
   * @param pagamento PagamentoVo con le informazioni per la gestione del pagamento.
   * @param dettagliPagamento DettaglioPagSearchResultVo[] con le informazioni aggiuntive per la gestione del pagamento.
   */
  private apriDettaglioPagamento(
    pagamento: PagamentoVo,
    dettagliPagamento?: DettaglioPagSearchResultVo[]
  ) {
    // Salvo il pagamento e i suoi dettagli all'interno del servizio dedicato
    this._dettaglioPagamenti.pagamento = pagamento;
    this._dettaglioPagamenti.dettagliPagamento = dettagliPagamento;
    this._dettaglioPagamenti.standAlone = false;
    this._dettaglioPagamenti.appClaimant = AppClaimants.pagamentoSD;

    // Modifico il flag di visualizzazione del componente di dettaglio
    this.visualizzaDettaglio = true;
  }

  /**
   * Funzione invocata quando l'evento "onIndietro" viene triggherato dal componente "risca-gestione-pagamento".
   */
  chiudiDettaglioPagamento() {
    // Modifico il flag di visualizzazione del componente di dettaglio
    this.visualizzaDettaglio = false;
  }

  /**
   * Funzione collegata all'evento di "pagamento aggiornato" per un pagamento da bollettazione.
   * @param assegnaPagamento AssegnaPagamento con il risultato dell'operazione.
   */
  onPagamentoAggiornato(assegnaPagamento?: AssegnaPagamento) {
    // Modifico il flag di visualizzazione del componente di dettaglio
    this.visualizzaDettaglio = false;

    // Visualizzo un alert con il messaggio d'aggiornamento
    const code = RiscaNotifyCodes.P001;
    // Lancio l'aggiornamento dell'alert mandando il codice
    this.alertConfigs = this._riscaAlert.createAlertFromMsgCode({ code });

    // Recupero le informazioni per la gestione dati
    const idSD = this.statoDebitorio.id_stato_debitorio;
    const idR = this.idRiscossione;
    // Vado a recuperare le informazioni extra dei pagamenti e aggiorno la pagina
    this._versamenti.getDettagliPagamenti(idR, idSD).subscribe({
      next: (dettPagExtra: DettaglioPagamentoExtraVo[]) => {
        // Modifico il flag di visualizzazione del componente di dettaglio
        this.visualizzaDettaglio = false;
        // Aggiorno la pagina
        this.onPagamentoSalvato(dettPagExtra);
        // #
      },
      error: (error: RiscaServerError) => {
        // Gestisci errore
        this.onServiziError(error);
      },
    });
  }

  /**
   * #######################################################################
   * FUNZIONE COMUNI DI AGGIORNAMENTO PAGINA A SEGUITO SALVATAGGIO PAGAMENTO
   * #######################################################################
   */

  /**
   * Funzione di comodo che aggiorna le informazioni in pagina dopo che un pagamento è stato aggiornato.
   * @param dettPagExtra DettaglioPagamentoExtraVo[] con le informazioni per la gestione della pagina.
   */
  private onPagamentoSalvato(dettPagExtra: DettaglioPagamentoExtraVo[]) {
    // Aggiorno la tabella dati
    this.dettPagExtraTable = new DettPagExtraTable({
      dettPagExtra,
      disableUserInputs: this.disableUserInputs,
    });
  }

  /**
   * #######################
   * CANCELLAZIONE PAGAMENTO
   * #######################
   */

  /**
   * Cancella il record in input dalla tabella delle attività rimborsi.
   * @param row RiscaTableDataConfig<DettaglioPagamentoExtraVo> da rimuovere.
   */
  eliminaPagamento(row: RiscaTableDataConfig<DettaglioPagamentoExtraVo>) {
    // Recupero il dato originale dalla riga della tabella
    let dettPagExt: DettaglioPagamentoExtraVo;
    dettPagExt = cloneDeep(row.original);
    // Recupero il pagamento dal dettaglio
    const pagamento = dettPagExt?.pagamento;

    // Creo la funzione di rimozione dell'elemento
    const onConfirm = () => {
      // Richiamo la funzione di cancellazione del pagamento
      this.onEliminaPagamentoConfirm(pagamento);
    };
    // Definisco l'oggetto per le callback
    const cbs: ICallbackDataModal = { onConfirm };

    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConfermaCancellazione(cbs);
  }

  /**
   * Funzione invocata alla conferma della cancellazione del pagamento tramite modale.
   * @param pagamento PagamentoVo con le informazioni del pagamento da eliminare.
   */
  private onEliminaPagamentoConfirm(pagamento: PagamentoVo) {
    // Prendo l'id del pagamento
    const idP = pagamento?.id_pagamento;
    // Recupero le informazioni per la gestione dati
    const idSD = this.statoDebitorio.id_stato_debitorio;
    const idR = this.idRiscossione;

    // Chiamo la funzione
    this._pagamentiSD
      .eliminaPagamentoEAggiornaDettagli(idP, idR, idSD)
      .subscribe({
        next: (dettPagExtra: DettaglioPagamentoExtraVo[]) => {
          // Aggiorno la pagina
          this.onPagamentoSalvato(dettPagExtra);
          // #
        },
        error: (error: RiscaServerError) => {
          // Gestisci errore
          this.onServiziError(error);
        },
      });
  }

  /**
   * ##################
   * AGGIUNGI PAGAMENTO
   * ##################
   */

  /**
   * Funzione che apre e chiude la form di inserimento versamenti.
   * @param stato stato attuale
   */
  toggleNuovoPagamento(stato: boolean) {
    // Effettuo il toggle per la visualizzazione
    this.pagamentoApriChiudi = !this.pagamentoApriChiudi;
  }

  /**
   * Alla pressione del pulsante "Aggiungi" nella form di inserimento versamenti, aggiunge alla tabella un nuovo record.
   * @param pagamento PagamentoVo come oggetto generato dal form per l'inserimento.
   */
  aggiungiPagamento(pagamento: PagamentoVo) {
    // Chiudo la modale
    this.toggleNuovoPagamento(true);

    // Prendo l'importo versato
    const { importo_versato } = pagamento;
    // Recupero le rate dallo stato debitorio
    const rateSD = this.statoDebitorio.rate ?? [];
    // Prendo la rata padre
    const rataPadre = this._datiContabiliUtility.getRataPadre(rateSD);

    // Associo la rata padre
    const id_rata_sd = rataPadre?.id_rata_sd;
    // Creo un dettaglio pagamento
    const dp = new DettaglioPagamentoIdVo({ id_rata_sd, importo_versato });
    // Collego il pagamento al suo dettaglio
    pagamento.dettaglio_pag = [dp];

    // Recupero id stato debitorio e id pratica
    const idP = this.idRiscossione;
    const idSD = this.statoDebitorio.id_stato_debitorio;

    // Invio il versamento al BE per aggiungerlo
    this._pagamentiSD
      .inserisciPagamentoEAggiornaDettagli(pagamento, idP, idSD)
      .subscribe({
        next: (dettPagExtra: DettaglioPagamentoExtraVo[]) => {
          // Aggiorno la pagina
          this.onPagamentoSalvato(dettPagExtra);
          // #
        },
        error: (error: RiscaServerError) => {
          // Gestisco l'errore
          this.onServiziError(error);
        },
      });
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione di supporto che verifica se il pagamento in input è manuale.
   * @param pagamento PagamentoVo da verificare.
   * @returns boolean con il risultato della verifica.
   */
  isPagamentoManuale(pagamento: PagamentoVo): boolean {
    // Richiamo la funzione di verifica
    return this._versamenti.isPagamentoManuale(pagamento);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di verifica per la tabella dei versamenti.
   */
  get checkVersamentiTable() {
    // Verifico che la tabella esista e abbia dati
    return this.dettPagExtraTable?.source?.length > 0;
  }

  /**
   * Determina se la form di inserimento è valida
   */
  get formValida() {
    return this.riscaInsVersamento?.valid;
  }

  /**
   * Getter di comodo per il recupero dell'id riscossione della pratica in uso.
   * @returns number con l'id della pratica.
   */
  get idRiscossione(): number {
    // Recupero l'id riscossione dal servizio
    return this._datiContabiliUtility.idPratica;
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.isPraticaLocked || this.AEA_detPagamentiDisabled;
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

  /**
   * Getter per il recupero del flag per AEA: dettaglio pagamenti.
   * @returns boolean con il valore della configurazione.
   */
  get AEA_detPagamentiDisabled(): boolean {
    // Verifico se la configurazione di accesso agli elementi è attivo
    let AEA_detPagamentiDisabled: boolean;
    AEA_detPagamentiDisabled = this._accessoElementiApp.isAEADisabled(
      this.AEAK_C.DET_PAGAMENTI
    );

    // Ritorno la configurazione
    return AEA_detPagamentiDisabled;
  }
}
