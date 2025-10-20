import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NavigationExtras } from '@angular/router';
import { Observable } from 'rxjs';
import { Subscription } from 'rxjs/index';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { DettaglioPagSearchResultVo } from '../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { PagamentoVo } from '../../../../core/commons/vo/pagamento-vo';
import { IRicercaPagamentiVo } from '../../../../core/commons/vo/ricerca-pagamenti-vo';
import { TipoRicercaPagamentoVo } from '../../../../core/commons/vo/tipo-ricerca-pagamento-vo';
import {
  IJourneySnapshot,
  IJourneyStep,
  IJStepConfig,
} from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RicercaSDxMorositaTablePagination } from '../../../../shared/classes/risca-table/ricerche-pagamenti/ricerca-morosita/ricerca-sd-morosita.table';
import {
  IRicercaPagamentiTableConfigs,
  RicercaPagamentiTable,
} from '../../../../shared/classes/risca-table/ricerche-pagamenti/ricerca-pagamenti/ricerca-pagamenti.table';
import { IRiscaGestionePagamentoRouteParams } from '../../../../shared/components/risca/risca-gestione-pagamento/utilities/risca-gestione-pagamento.interfaces';
import { RiscaRicercaPagamentiComponent } from '../../../../shared/components/risca/risca-ricerca-pagamenti/risca-ricerca-pagamenti.component';
import { IFiltriRicercaPagamenti } from '../../../../shared/components/risca/risca-ricerca-pagamenti/utilities/risca-ricerca-pagamenti.interfaces';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { RiscaUtilitiesComponent } from '../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import {
  AppRoutes,
  RiscaAlertConfigs,
  RiscaInfoLevels,
  RiscaServerError,
  RiscaTablePagination,
} from '../../../../shared/utilities';
import { VersamentiService } from '../../../pratiche/service/dati-contabili/versamenti/versamenti.service';
import { DettaglioPagamentiService } from '../../service/dettaglio-pagamenti/dettaglio-pagamenti.service';
import { RicercaPagamentiService } from '../../service/pagamenti/ricerca-pagamenti.service';
import { RicerchePagamentiService } from '../../service/ricerche-pagamenti/ricerche-pagamenti.service';
import { RicercaPagamentiConsts } from './utilities/ricerca-pagamenti.consts';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IAlertConfigsFromCode } from '../../../../shared/utilities/interfaces/risca/risca-alert/risca-alert.interface';
import { ReportService } from '../../../report/service/report/report.service';
import { IReportLocationVo } from '../../../../core/commons/vo/report-location-vo';

@Component({
  selector: 'ricerca-pagamenti',
  templateUrl: './ricerca-pagamenti.component.html',
  styleUrls: ['./ricerca-pagamenti.component.scss'],
})
export class RicercaPagamentiComponent
  extends RiscaUtilitiesComponent
  implements OnInit, OnDestroy
{
  /** RicercaPagamentiConsts come classe di costanti del componente. */
  RP_C = new RicercaPagamentiConsts();

  /** ViewChild collegato al form di ricerca. */
  @ViewChild('riscaRicercaPagamenti')
  riscaRicercaPagamenti: RiscaRicercaPagamentiComponent;

  /** Subject dedicato all'evento per annullare la ricerca dei pagamenti. */
  onAnnullaRicerca$: Subscription;
  /** Subject dedicato all'evento per avviare la ricerca dei pagamenti. */
  onAvviaRicerca$: Subscription;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert in riferimento alla ricerca dati. */
  alertRicercaConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /** IFiltriRicercaPagamenti con l'ultimo oggetto di ricerca che l'utente ha usato come filtro. */
  filtriPagamenti: IFiltriRicercaPagamenti;
  /** IFiltriRicercaPagamenti con i filtri iniziali per la form di ricerca. */
  filtriPagIniziali: IFiltriRicercaPagamenti;

  /** RicercaPagamentiTable con la struttura per la visualizzazione dei risultati della ricerca. */
  ricercaPagamentiTable: RicercaPagamentiTable;
  /** RiscaTablePagination con i dati dell'ultima paginazione impostata. */
  paginazionePagamenti: RiscaTablePagination;

  /**
   * Costruttore.
   */
  constructor(
    private _dettaglioPagamenti: DettaglioPagamentiService,
    navigationHelper: NavigationHelperService,
    private _report: ReportService,
    private _ricercaPagamenti: RicercaPagamentiService,
    private _ricerchePagamenti: RicerchePagamentiService,
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    riscaMessages: RiscaMessagesService,
    private _versamenti: VersamentiService
  ) {
    // Lancio il super
    super(navigationHelper, riscaAlert, riscaMessages);
    // Imposto le configurazioni per lo step di journey
    this.stepConfig = this.RP_C.NAVIGATION_CONFIG;

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
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onAnnullaRicerca$) {
        this.onAnnullaRicerca$.unsubscribe();
      }
      if (this.onAvviaRicerca$) {
        this.onAvviaRicerca$.unsubscribe();
      }
    } catch (e) {}
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
    // Lancio il set dei listener del componente
    this.setupListeners();
    // Lancio la funzione di setup di route params e snapshot
    this.setupRouteParams();
    // Lancio la funzione di setup per sapere se sto torndando sulla pagina a seguito di un pagamento aggiornato
    this.setupPagamentoSalvato();
  }

  /**
   * Funzione di setup per i listener del componente.
   */
  private setupListeners() {
    // Connetto l'ascoltatore all'evento ANNULLA
    this.onAnnullaRicerca$ =
      this._ricerchePagamenti.onAnnullaRicerca$.subscribe({
        next: () => {
          // Lancio la funzione collegata all'evento
          this.annullaRicerca();
        },
      });

    // Connetto l'ascoltatore all'evento CERCA
    this.onAvviaRicerca$ = this._ricerchePagamenti.onAvviaRicerca$.subscribe({
      next: () => {
        // Lancio la funzione collegata all'evento
        this.avviaRicerca();
      },
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
   * Funzione che imposta le logiche di gestione per sapere se sto ritornando su questa pagina avendo salvato un pagamento.
   */
  private setupPagamentoSalvato() {
    // Verifico se nel servizio c'è il flag di pagamento salvato
    if (this._dettaglioPagamenti.pagamentoSalvato) {
      // E' stato salvato un pagamento prima di ritornare su questa pagina, aggiorno l'alert
      const code = RiscaNotifyCodes.P001;
      // Lancio l'aggiornamento dell'alert mandando il codice
      this.alertConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
      // Emetto l'evento con l'alert
      this.emettiAlert(this.alertConfigs);

      // Resetto il flag per il pagamento salvato
      this._dettaglioPagamenti.resetPagamentoSalvato();
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
    // X ULTIMA !!!! Lancio la funzione di bootstrap dei dati partendo da quello che è già presente nel componente (a seguito del loadJStep)
    this.initBootstrapComponente();
  }

  /**
   * Funzione lanciata come init del componente per tutte le logiche di pre-caricamento.
   * La funzione verifica la presenza d'informazioni pre-inizializzate nel componente e lancia e gestisce la ricerca dati.
   */
  private initBootstrapComponente() {
    // Recupero dal componente le informazioni per la ricerca
    const f: IFiltriRicercaPagamenti = this.filtriPagamenti;
    const p: RiscaTablePagination = this.paginazionePagamenti;

    // Verifico se entrambi gli oggetti esistono all'interno del componente
    if (f != undefined && p != undefined) {
      // Converto i filtri di ricerca in una struttura gestibile per fare la query
      const q: IRicercaPagamentiVo = this.convertiFiltriRicerca(f);
      // Lancio la ricerca dati, richiedendo l'aggiornamento delle righe selezionate
      this.searchPagamenti(q, p).subscribe({
        next: (ricerca: RicercaPaginataResponse<PagamentoVo[]>) => {
          // Gestisco i risultati di ricerca rispetto alle strutture esistenti
          this.onInitPagamenti(ricerca);
          // #
        },
        error: (e: RiscaServerError) => {
          // Gestisco l'errore
          this.handleServiziError(e);
        },
      });
    }
  }

  /**
   * Funzione che gestisce il risultato della ricerca dei pagamenti impostando le strutture dati rispetto alle configurazioni già esistenti caricate tramite snapshot del componente.
   * @param ricerca RicercaPaginataResponse<PagamentoVo[]> con i risultati di ricerca.
   */
  private onInitPagamenti(ricerca: RicercaPaginataResponse<PagamentoVo[]>) {
    // Verifico l'input
    if (!ricerca) {
      // Niente da fare
      return;
    }

    // Estraggo le informazioni dall'oggetto di ricerca
    const { paging, sources } = ricerca;
    // Estraggo dalla risposta le informazioni utili
    const statiDebitori: PagamentoVo[] = sources;
    const paginazione: RiscaTablePagination = paging;

    // Verifico se esistono risultati
    if (sources?.length > 0) {
      // Esistono risultati, gestisco le tabella
      this.onInitPagamentiWithData(statiDebitori, paginazione);
      // #
    } else {
      // Non esistono risultati, visualizzo un altro messaggio
      this.onCercaPagamentiNoData();
    }
  }

  /**
   * Funzione di comodo che gestisce la logica per quando la ricerca ritorna correttamente dei dati.
   * @param pagamenti PagamentoVo[] con la lista di dati scaricati.
   * @param paginazione RiscaTablePagination con l'oggetto di configurazione per la nuova paginazione.
   */
  private onInitPagamentiWithData(
    pagamenti: PagamentoVo[],
    paginazione: RiscaTablePagination
  ) {
    // Aggiorno la variabile locale per la paginazione
    this.paginazionePagamenti = paginazione;

    // Definisco le configurazioni iniziali per la tabella di ricerca
    let c: IRicercaPagamentiTableConfigs;
    c = { paginazione, pagamenti };
    // Creo la struttura per la tabella con i risultati di ricerca
    this.ricercaPagamentiTable = new RicercaPagamentiTable(c);
  }

  /**
   * ################################
   * FUNZIONI DEI PULSANTI PRINCIPALI
   * ################################
   */

  /**
   * Funzione collegata al pulsante ANNULLA della pagina.
   * La funzione prevede la pulizia dei dati sia per la tabella, sia per il form di ricerca.
   */
  annullaRicerca() {
    // Resetto le informazioni definite da ricerche precendenti
    this.resetAlertConfigs();
    this.filtriPagamenti = undefined;
    this.ricercaPagamentiTable?.clear();
    this.paginazionePagamenti = undefined;

    // Tramite referenza, chiedo il reset dei filtri
    this.riscaRicercaPagamenti.onFormReset();
  }

  /**
   * Funzione collegata al pulsante CERCA della pagina.
   * La funzione prevede la richiesta di submit dei filtri da parte del sotto componente di ricerca.
   */
  avviaRicerca() {
    // Resetto le informazioni definite da ricerche precendenti
    this.resetAlertConfigs();
    this.filtriPagamenti = undefined;
    this.ricercaPagamentiTable?.clear();
    this.paginazionePagamenti = undefined;

    // Tramite referenza, chiedo il submit dei filtri
    this.riscaRicercaPagamenti.onFormSubmit();
  }

  /**
   * ##################################################
   * FUNZIONI COLLEGATE AGLI EVENTI DEL FORM DI RICERCA
   * ##################################################
   */

  /**
   * Funzione collegata all'evento: onFormSubmit; del componente di ricerca.
   * La funzione riceverà i filtri di ricerca generati dal componente.
   * @param filtri IFiltriRicercaPagamenti con le informazioni dei filtri generati dal componente.
   */
  onFiltriReady(filtri: IFiltriRicercaPagamenti) {
    // Verifico l'input
    if (!filtri) {
      // Nessuna configurazione
      return;
    }
    // Resetto le strutture
    this.filtriPagamenti = undefined;
    this.ricercaPagamentiTable?.clear();
    this.paginazionePagamenti = undefined;
    // Resetto gli alert
    this.resetAlertConfigs(this.alertConfigs);
    this.resetAlertConfigs(this.alertRicercaConfigs);

    // Aggiorno i filtri di locali
    this.filtriPagamenti = filtri;
    this.filtriPagIniziali = filtri;
    // Lancio la funzione di ricerca dati
    this.cercaPagamenti();
  }

  /**
   * Funzione collegata all'evento: onFormErrors; del componente di ricerca.
   * La funzione riceverà i messaggi d'errore generati dal componente.
   * @param errori string[] con la lista degli errori generati dal form.
   */
  onFiltriErrors(errori: string[]) {
    // Segnalo gli errori lanciati dal form
    const a = this.alertConfigs;
    const m = errori;
    const t = RiscaInfoLevels.danger;
    // Aggiorno l'alert con gli errori
    this.aggiornaAlertConfigs(a, m, t);
  }

  /**
   * ########################################
   * FUNZIONI DI GESTIONE PER LA RICERCA DATI
   * ########################################
   */

  /**
   * Funzione funzione che lancia la ricerca dei dati e gestisce il flusso per i risultati ottenuti.
   */
  cercaPagamenti() {
    // Verifico se esiste la paginazione
    if (!this.paginazionePagamenti) {
      // Non esite, recupero il default dalla tabella
      this.paginazionePagamenti = RicercaSDxMorositaTablePagination();
    }

    // Recupero la paginazione per la ricerca
    const p: RiscaTablePagination = this.paginazionePagamenti;
    // Recupero i filtri di ricerca
    const f: IFiltriRicercaPagamenti = this.filtriPagamenti;
    // Converto i filtri di ricerca in una struttura gestibile per fare la query
    const q: IRicercaPagamentiVo = this.convertiFiltriRicerca(f);

    // Richiamo il servizio di ricerca stati debitori con filtri morosita
    this.searchPagamenti(q, p).subscribe({
      next: (ricerca: RicercaPaginataResponse<PagamentoVo[]>) => {
        // Aggiorno i risultati di ricerca
        this.onCercaPagamenti(ricerca);
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.handleServiziError(e);
      },
    });
  }

  /**
   * Funzione di supporto che effettua la chiamata di ricerca diretta per i dati.
   * @param q IRicercaPagamentiVo con l'oggetto di filtro per la ricerca.
   * @param p RiscaTablePagination con l'oggetto di paginazione per la ricerca.
   * @returns Osbervable<RicercaPaginataResponse<PagamentoVo[]>> con la richiesta di ricerca dati.
   */
  private searchPagamenti(
    q: IRicercaPagamentiVo,
    p: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<PagamentoVo[]>> {
    // Ricerco mediante servizio
    return this._ricercaPagamenti.searchPagamenti(q, p);
  }

  /**
   * Funzione che gestisce il risultato della ricerca degli stati debitori applicando i filtri morosità.
   * @param ricerca RicercaPaginataResponse<PagamentoVo[]> con i risultati di ricerca.
   */
  private onCercaPagamenti(ricerca: RicercaPaginataResponse<PagamentoVo[]>) {
    // Verifico l'input
    if (!ricerca) {
      // Niente da fare
      return;
    }

    // Estraggo le informazioni dall'oggetto di ricerca
    const { paging, sources } = ricerca;
    // Estraggo dalla risposta le informazioni utili
    const statiDebitori: PagamentoVo[] = sources;
    const paginazione: RiscaTablePagination = paging;

    // Verifico se esistono risultati
    if (sources?.length > 0) {
      // Esistono risultati, aggiorno la tabella
      this.onCercaPagamentiWithData(statiDebitori, paginazione);
      // #
    } else {
      // Non esistono risultati, visualizzo un altro messaggio
      this.onCercaPagamentiNoData();
    }
  }

  /**
   * Funzione di comodo che gestisce la logica per quando la ricerca ritorna correttamente dei dati.
   * @param pagamenti PagamentoVo[] con la lista di dati scaricati.
   * @param paginazione RiscaTablePagination con l'oggetto di configurazione per la nuova paginazione.
   */
  private onCercaPagamentiWithData(
    pagamenti: PagamentoVo[],
    paginazione: RiscaTablePagination
  ) {
    // Aggiorno la paginazione per la tabella e localmente
    this.paginazionePagamenti = paginazione;

    // Recupero i filtri di ricerca
    const f: IFiltriRicercaPagamenti = this.filtriPagamenti;
    // Recupero dai filtri l'informazione relativa allo stato pagamento
    const statoPagamento: TipoRicercaPagamentoVo = f.statoPagamento;

    // Preparo l'oggetto di configurazione dati per generare una nuova tabella
    const c: IRicercaPagamentiTableConfigs = {
      pagamenti,
      paginazione,
      statoPagamento,
    };
    // Creo la struttura per la tabella con i risultati di ricerca
    this.ricercaPagamentiTable = new RicercaPagamentiTable(c);
  }

  /**
   * Funzione di comodo che gestisce la logica per quando la ricerca non ritorna dati.
   */
  private onCercaPagamentiNoData() {
    // Non esistono risultati, visualizzo un altro messaggio
    const a = this.alertRicercaConfigs;
    const m = [this.C_C.MSG_NESSUN_ELEMENTO_TROVATO_FILTRI];
    const t = RiscaInfoLevels.warning;
    // Aggiorno l'alert
    this.aggiornaAlertConfigs(a, m, t);
  }

  /**
   * ###############################
   * FUNZIONI COLLEGATE ALLA TABELLA
   * ###############################
   */

  /**
   * Funzione collegata alla funzionalità di paginazione della tabella.
   * Al cambio di paginazione, viene rilanciata la ricerca paginata per i dati della tabella.
   * @param pagination RiscaTablePagination ch definisce la configurazione di paginazione per la ricerca.
   */
  cambiaPagina(pagination: RiscaTablePagination) {
    // Resetto gli alert
    this.resetAlertConfigs(this.alertConfigs);
    this.resetAlertConfigs(this.alertRicercaConfigs);
    // Aggiorno l'oggetto di paginazione locale
    this.paginazionePagamenti = pagination;
    // Lancio la ricerca dati
    this.cercaPagamenti();
  }

  /**
   * Funzione collegata al pulsante "DETTAGLIO" della tabella.
   * Viene recuperato il dato dalla tabella originale e viene visualizzato il dettaglio.
   * @param row RiscaTableDataConfig<PagamentoVo> che definisce la riga della tabella.
   */
  onDettaglioPagamento(row: RiscaTableDataConfig<PagamentoVo>) {
    // Verifico l'input
    if (!row) {
      // Blocco il flusso
      return;
    }

    // Recupero il dato originale
    const pagamento: PagamentoVo = row.original;
    // Chiamo la funzione per aprire il dettaglio
    this.apriDettaglioPagamento(pagamento);
  }

  /**
   * Funzione che compila le informazioni per l'apertura del dettaglio pagamnto.
   * @param pagamento PagamentoVo con le informazioni del pagamento.
   */
  private apriDettaglioPagamento(pagamento: PagamentoVo) {
    // Recupero dal pagamento il suo id per poter recuperare i dettagli pagamento search (usati per la grafica)
    const idP = pagamento?.id_pagamento;

    // Richiamo il servizio e scarico i dati dei dettagli pag search
    this._versamenti.getDettagliPagSearchResult(idP).subscribe({
      next: (dettagliPagSearchResult: DettaglioPagSearchResultVo[]) => {
        // Dati recuperati, li passo alla funzione di cambio pagina
        this.apriPaginaDettaglioPagamento(pagamento, dettagliPagSearchResult);
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione che gestisce l'apertura della pagina di dettaglio per la modifica del pagamento.
   * @param pagamento PagamentoVo con le informazioni per la gestione del pagamento.
   * @param dettagliPagamento DettaglioPagSearchResultVo[] con le informazioni aggiuntive per la gestione del pagamento.
   */
  private apriPaginaDettaglioPagamento(
    pagamento: PagamentoVo,
    dettagliPagamento?: DettaglioPagSearchResultVo[]
  ) {
    // Salvo il pagamento e i suoi dettagli all'interno del servizio dedicato
    this._dettaglioPagamenti.pagamento = pagamento;
    this._dettaglioPagamenti.dettagliPagamento = dettagliPagamento;
    // Definisco i dati per il filo d'arianna
    const rFA = this._riscaFA.ricerchePagamenti;
    const rpFA = this._riscaFA.ricercaPagamenti;
    const dpFA = this._riscaFA.dettaglioPagamenti;
    this._dettaglioPagamenti.faLivelli = [rFA, rpFA, dpFA];

    // Definisco lo state da passare alla pagina
    const state: IRiscaGestionePagamentoRouteParams = {};
    // Definisco le configurazioni per il cambio di pagina
    const targetRoute: AppRoutes = AppRoutes.dettaglioPagamenti;
    const extras: NavigationExtras = { state };
    const jStepTarget: IJStepConfig = this.RP_C.NAVIGATION_CONFIG;
    const sameRoute: boolean = true;

    // Recupero i dati comuni per la generazione dello step
    const stepBase = this.stepConfig;
    const snapshot = this.snapshotConfigs;
    // Genero l'oggetto per il target del routing
    const target = this.navigationConfigs(targetRoute, extras?.state);
    // Assegno, anche se undefined, il journey step target all'oggetto target
    target.jStepTarget = jStepTarget;

    // Definisco le informazioni per il salvataggio dello step
    const step: IJourneyStep = { ...stepBase, ...target };
    // Utilizzo il servizio per aggiungere uno step al journey
    this._navigationHelper.stepForward(step, snapshot, sameRoute);
  }

  /**
   * ########################
   * FUNZIONE PER CREA REPORT
   * ########################
   */

  /**
   * Funzione per la creazione report di una pratica.
   */
  creaReport() {
    // Resetto possibili alert aperti
    this.resetAlertConfigs();
    
    // Recupero i filtri di ricerca
    const f: IFiltriRicercaPagamenti = this.filtriPagamenti;
    // Converto i filtri di ricerca in una struttura gestibile per fare la query
    const q: IRicercaPagamentiVo = this.convertiFiltriRicerca(f);

    // Se non esiste l'oggetto di ricerca devo bloccare la creazione report
    if (!q) {
      // Nessun oggetto di ricerca
      return;
    }

    // Richiamo la creazione di un report
    this._report.createReportRicercaPagamenti(q).subscribe({
      next: (reportLocation: IReportLocationVo) => {
        // Richiamo la funzione di gestione del crea report
        this.onCreaReport();
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
        // #
      },
    });
  }

  /**
   * Funzione invocata alla conclusione della funzione di creazione report.
   * La funzione gestirà la segnalazione verso l'utente di avvenuta presa in carico della creazione del report con i dati richiesti.
   */
  private onCreaReport() {
    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.P009;
    // Compongo l'oggetto per generare l'alert
    const c: IAlertConfigsFromCode = { code };
    // Genero l'alert sulla base del codice da visualizzare
    const newAlert = this._riscaAlert.createAlertFromMsgCode(c);

    // Modifico alcune informazioni della configurazione dell'alert
    newAlert.allowAlertClose = true;
    newAlert.timeoutMessage = 5000;
    newAlert.persistentMessage = false;

    // Assegno l'oggetto generato all'alert della pagina
    this.alertConfigs = newAlert;
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione di gestione degli errori generati dalle chiamate ai servizi.
   * @param e RiscaServerError con il dettaglio dell'errore generato.
   */
  handleServiziError(e: RiscaServerError) {
    // Lancio la gestione dell'errore
    this.onServiziError(e);
  }

  /**
   * #####################################
   * FUNZIONI PER GESTIRE MEGLIO IL CODICE
   * #####################################
   */

  /**
   * Funzione che converte l'oggetto generato dal form di ricerca, in un oggetto compatibile con i filtri di ricerca della query.
   * @param f IFiltriRicercaPagamenti con l'oggetto da convertire.
   * @returns IRicercaPagamentiVo con l'oggetto convertito.
   */
  private convertiFiltriRicerca(
    f: IFiltriRicercaPagamenti
  ): IRicercaPagamentiVo {
    // Converto l'oggetto
    const q =
      this._ricercaPagamenti.convertIFiltriRicercaPagamentiToIRicercaPagamenti(
        f
      );
    // Ritorno l'oggetto convertito
    return q;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che verifica se esistono dati all'interno della tabella per la visualizzazione dei dati di ricerca.
   * @returns boolean con il risultato del check della tabella.
   */
  get checkTableRicerca(): boolean {
    // Verifico se esiste la tabella e ha dei dati al suo interno
    return this.ricercaPagamentiTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che restitusce un oggetto IJourneySnapshot per generare una snapshot del componente.
   */
  get snapshotConfigs(): IJourneySnapshot {
    return {
      source: this,
      mapping: this.RP_C.SNAPSHOT_CONFIG.mapping,
      saveFunc: this.RP_C.SNAPSHOT_CONFIG.saveFunc,
    };
  }
}
