import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NavigationExtras } from '@angular/router';
import { clone, cloneDeep } from 'lodash';
import { Observable } from 'rxjs';
import { Subscription } from 'rxjs/index';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { AttivitaSDVo } from '../../../../core/commons/vo/attivita-sd-vo';
import { IReportLocationVo } from '../../../../core/commons/vo/report-location-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import {
  IJourneySnapshot,
  IJourneyStep,
  IJStepConfig,
} from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import {
  IRicercaSDxRimborsiTableConfigs,
  RicercaSDxRimborsiTable,
  RicercaSDxRimborsiTablePagination,
  SDSelezionatixRimborsiTable,
} from '../../../../shared/classes/risca-table/ricerche-pagamenti/ricerca-rimborsi/ricerca-sd-rimborsi.table';
import { RiscaFormChildComponent } from '../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { RiscaRicercaRimborsiComponent } from '../../../../shared/components/risca/risca-ricerca-rimborsi/risca-ricerca-rimborsi.component';
import { IFiltriRicercaRimborsi } from '../../../../shared/components/risca/risca-ricerca-rimborsi/utilities/risca-ricerca-rimborsi.interfaces';
import { RiscaTableComponent } from '../../../../shared/components/risca/risca-table/risca-table.component';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { IRiscaTableACEvent } from '../../../../shared/components/risca/risca-table/utilities/risca-table.interfaces';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaStampaPraticaService } from '../../../../shared/services/risca/risca-stampa/risca-stampa-pratica.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppRoutes,
  IRFCFormErrorsConfigs,
  RiscaAlertConfigs,
  RiscaFormStatus,
  RiscaInfoLevels,
  RiscaServerError,
  RiscaTablePagination,
  TRiscaAlertType,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IAlertConfigsFromCode } from '../../../../shared/utilities/interfaces/risca/risca-alert/risca-alert.interface';
import { DatiContabiliService } from '../../../pratiche/service/dati-contabili/dati-contabili/dati-contabili.service';
import { RimborsiService } from '../../../pratiche/service/rimborsi/rimborsi.service';
import { ReportService } from '../../../report/service/report/report.service';
import { DettaglioRimborsiService } from '../../service/dettaglio-rimborsi/dettaglio-rimborsi.service';
import { RicercaRimborsiService } from '../../service/ricerca-rimborsi/ricerca-rimborsi.service';
import { IRicercaRimborsi } from '../../service/ricerca-rimborsi/utilities/ricerca-rimborsi.interfaces';
import { RicerchePagamentiService } from '../../service/ricerche-pagamenti/ricerche-pagamenti.service';
import { IDettRimborsiRouteParams } from '../dettaglio-rimborsi/utilities/dettaglio-rimborsi.interfaces';
import { RicercaRimborsiConsts } from './utilities/ricerca-rimborsi.consts';
import { RicercaRimborsiFieldsConfig } from './utilities/ricerca-rimborsi.fields-configs';
import { RicercaRimborsiFormConfigs } from './utilities/ricerca-rimborsi.form-configs';
import { IApplicaAttivitaRimborso } from './utilities/ricerca-rimborsi.interfaces';

@Component({
  selector: 'ricerca-rimborsi',
  templateUrl: './ricerca-rimborsi.component.html',
  styleUrls: ['./ricerca-rimborsi.component.scss'],
})
export class RicercaRimborsiComponent
  extends RiscaFormChildComponent<IApplicaAttivitaRimborso>
  implements OnInit
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** RicercaRimborsiConsts come classe di costanti del componente. */
  RR_C = new RicercaRimborsiConsts();

  /** ViewChild collegato al form di ricerca. */
  @ViewChild('riscaRicercaRimborsi')
  riscaRicercaRimborsi: RiscaRicercaRimborsiComponent;
  /** ViewChild collegato alla tabella per i risultati di ricerca degli stati debitori. */
  @ViewChild('tableSDRimborsi')
  tableSDRimborsi: RiscaTableComponent<StatoDebitorioVo>;

  /** Subject dedicato all'evento per annullare la ricerca dei pagamenti. */
  onAnnullaRicerca$: Subscription;
  /** Subject dedicato all'evento per avviare la ricerca dei pagamenti. */
  onAvviaRicerca$: Subscription;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();
  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert in riferimento alla ricerca dati. */
  alertRicercaConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /** RicercaSDxRimborsiTable con la struttura per la visualizzazione dei risultati della ricerca. */
  ricercaSDxRimborsiTable: RicercaSDxRimborsiTable;
  /** sdSelezionatixRimborsiTable con la struttura per il tracciatore di selezione per gli stati debitori selezionati. */
  sdSelezionatixRimborsiTable: SDSelezionatixRimborsiTable;
  /** RiscaTablePagination con i dati dell'ultima paginazione impostata. */
  paginazioneSDRimborsi: RiscaTablePagination;
  /** IFiltriRicercaRimborsi con l'ultimo oggetto di ricerca che l'utente ha usato come filtro. */
  filtriSDRimborsi: IFiltriRicercaRimborsi;
  /** IFiltriRicercaRimborsi con i filtri iniziali per la form di ricerca. */
  filtriSDRIniziali: IFiltriRicercaRimborsi;

  /** RicercaRimborsiFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: RicercaRimborsiFieldsConfig;
  /** RicercaRimborsiFormConfigs come classe che definisce la struttura di configurazione del form. */
  formConfigs: RicercaRimborsiFormConfigs;

  /** AttivitaSDVo[] con la lista di elementi per la gestione dello stato rimborsi degli stati debitori. */
  listaAttivitaStatoDeb: AttivitaSDVo[] = [];
  /** AttivitaSdVo con l'ultima selezione effettuata dall'utente. */
  attivitaStatoDebSelezionato: AttivitaSDVo;

  /** StatoDebitorio[] con la lista degli stati debitori selezionati dalla tabella. */
  statiDebitoriSelezionati: StatoDebitorioVo[] = [];
  /** StatoDebitorio[] con la lista degli stati debitori selezionati dalla tabella. */
  statiDebitoriSelezionatiRows: RiscaTableDataConfig<StatoDebitorioVo>[] = [];
  /** Boolean che identifica come selezionati tutti gli stati debitori ricercati, anche quelli non visibili per la paginazione. */
  allSDSelezionati: boolean = false;
  /** Boolean che gestisce l'apertura dell'accordion per gli stati debitori selezionati. */
  sdSelezionatiApriChiudi: boolean = false;

  /** Subscription che permette di collegarsi all'evento di errore generato dalla stampa pratica pdf. */
  private onStampaError$: Subscription;

  /**
   * Costruttore.
   */
  constructor(
    private _datiContabili: DatiContabiliService,
    private _dettaglioRimborsi: DettaglioRimborsiService,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _report: ReportService,
    private _ricercaRimborsi: RicercaRimborsiService,
    private _ricerchePagamenti: RicerchePagamentiService,
    private _rimborsi: RimborsiService,
    private _riscaAlert: RiscaAlertService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    private _riscaMessages: RiscaMessagesService,
    private _riscaStampaP: RiscaStampaPraticaService,
    submitHandlerService: RiscaFormSubmitHandlerService,
    utilityService: RiscaUtilitiesService
  ) {
    // Lancio il super del componente
    super(logger, navigationHelper, submitHandlerService, utilityService);
    // Imposto le configurazioni per lo step di journey
    this.stepConfig = this.RR_C.NAVIGATION_CONFIG;

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
      if (this.onStampaError$) {
        this.onStampaError$.unsubscribe();
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
    // Lancio la funzione di gestione per gli errori del form
    this.setupMainFormErrors();
    // Lancio la funzione di setup delle input del form
    this.setupFormInputs();
    // Lancio la funzione di setup per la struttura del form
    this.setupFormConfigs();
    // Lancio la funzione di setup per gli ascoltatori del form
    this.setupFormListeners();
    // Lancio la funzione di setup di route params e snapshot
    this.setupRouteParams();
    // Lancio la funzione di set per le liste del componente
    this.setupLists();
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

    // Mi aggancio all'evento di errore stampa pdf pratica
    this.onStampaError$ = this._riscaStampaP.onStampaError$.subscribe({
      next: (e: RiscaServerError) => {
        // Gestisco l'errore generato
        this.handleServiziError(e);
      },
    });
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [...this.EM.MAP_FORM_GROUP_REQUIRED];
  }

  /**
   * Funzione di setup per la configurazione dei dati per la costruzione delle input del form.
   */
  private setupFormInputs() {
    // Recupero il servizio di configurazione
    const s = this._riscaFormBuilder;
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new RicercaRimborsiFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new RicercaRimborsiFormConfigs(s);
    // Tramite la classe di configurazione, definisco la struttura per il main form
    this.mainForm = this.formConfigs.instantiateForm();
  }

  /**
   * Funzione di setup per gli ascoltatori sul form principale.
   */
  private setupFormListeners() {
    // Imposto un ascoltatore per il cambio dell'attività stato debitorio
    this.mainForm?.get(this.RR_C.ATTIVITA_RIMBORSO_SD)?.valueChanges.subscribe({
      next: (attivitaStatoDeb: AttivitaSDVo) => {
        // Creo una copia e l'assegno come ultimo valore
        this.onAttivitaStatoDebSelected(attivitaStatoDeb);
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
   * Funzione di setup per lo scarico dei dati delle liste del componente.
   */
  private setupLists() {
    // Scarico la lista per le attività dello stato debitorio
    this._rimborsi.getAttivitaStatoDebitorio().subscribe({
      next: (attivita: AttivitaSDVo[]) => {
        // Lancio la funzione di gestione dati
        this.onGetListAttivitaStatoDeb(attivita);
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore generato
        this.handleServiziError(e);
      },
    });
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
    const f: IFiltriRicercaRimborsi = this.filtriSDRimborsi;
    const p: RiscaTablePagination = this.paginazioneSDRimborsi;

    // Verifico se entrambi gli oggetti esistono all'interno del componente
    if (f != undefined && p != undefined) {
      // Converto i filtri di ricerca in una struttura gestibile per fare la query
      const q: IRicercaRimborsi = this.convertiFiltriRicerca(f);
      // Lancio la ricerca dati, richiedendo l'aggiornamento delle righe selezionate
      this.searchSDByFiltriRimborsi(q, p).subscribe({
        next: (ricerca: RicercaPaginataResponse<StatoDebitorioVo[]>) => {
          // Gestisco i risultati di ricerca rispetto alle strutture esistenti
          this.onInitSDxRimborsi(ricerca);
          // Lancio la funzione di init dati per gli stati debitori selezionati
          this.initStatiDebitoriSelezionati();
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
   * Funzione che gestisce il risultato della ricerca degli stati debitori impostando le strutture dati rispetto alle configurazioni già esistenti caricate tramite snapshot del componente.
   * @param ricerca RicercaPaginataResponse<StatoDebitorioVo[]> con i risultati di ricerca.
   */
  private onInitSDxRimborsi(
    ricerca: RicercaPaginataResponse<StatoDebitorioVo[]>
  ) {
    // Verifico l'input
    if (!ricerca) {
      // Niente da fare
      return;
    }

    // Estraggo le informazioni dall'oggetto di ricerca
    const { paging, sources } = ricerca;
    // Estraggo dalla risposta le informazioni utili
    const statiDebitori: StatoDebitorioVo[] = sources;
    const paginazione: RiscaTablePagination = paging;

    // Verifico se esistono risultati
    if (sources?.length > 0) {
      // Esistono risultati, gestisco le tabella
      this.onInitSDxRimborsiWithData(statiDebitori, paginazione);
      // #
    } else {
      // Non esistono risultati, visualizzo un altro messaggio
      this.onCercaSDxRimborsiNoData();
    }
  }

  /**
   * Funzione di comodo che gestisce la logica per quando la ricerca ritorna correttamente dei dati.
   * @param statiDebitori StatoDebitorioVo[] con la lista di dati scaricati.
   * @param paginazione RiscaTablePagination con l'oggetto di configurazione per la nuova paginazione.
   */
  private onInitSDxRimborsiWithData(
    statiDebitori: StatoDebitorioVo[],
    paginazione: RiscaTablePagination
  ) {
    // Aggiorno la variabile locale per la paginazione
    this.paginazioneSDRimborsi = paginazione;

    // Recupero il flag per la gestione del "tutti selezionati"
    const tuttiSelezionati = this.allSDSelezionati;
    // Definisco le configurazioni iniziali per la tabella di ricerca
    let c: IRicercaSDxRimborsiTableConfigs;
    c = { paginazione, statiDebitori, tuttiSelezionati };
    // Creo la struttura per la tabella con i risultati di ricerca
    this.ricercaSDxRimborsiTable = new RicercaSDxRimborsiTable(c);

    // Variabile di comodo
    let rows: RiscaTableDataConfig<StatoDebitorioVo>[];
    rows = this.statiDebitoriSelezionatiRows ?? [];
    // Aggiungo la lista di sorgente selezionato alla tabella
    this.ricercaSDxRimborsiTable.setSourceSelected(rows);
  }

  /**
   * Funzione di comodo che gestisce l'inizializzazione degli stati debitori selezionati.
   */
  private initStatiDebitoriSelezionati() {
    // Creo la struttura per la tabella con gli stati debitori selezionati
    this.sdSelezionatixRimborsiTable = new SDSelezionatixRimborsiTable();

    // Variabile di comod
    let sd: StatoDebitorioVo[];
    sd = this.statiDebitoriSelezionati ?? [];
    // Aggiorno il source per la tabella di tracciamento
    this.sdSelezionatixRimborsiTable.setNewElements(sd);
  }

  /**
   * ##############################################
   * FUNZIONI PER GLI ASCOLTATORI E RISULTATI SETUP
   * ##############################################
   */

  /**
   * Funzione invocata alla selezione dell'attività stato debitorio per la gestione degli stati debitori.
   * @param attivitaStatoDeb AttivitaSDVo con il dato selezionato.
   */
  private onAttivitaStatoDebSelected(attivitaStatoDeb: AttivitaSDVo) {
    // Verifico l'input
    if (!attivitaStatoDeb) {
      // Resetto la variabile locale
      this.attivitaStatoDebSelezionato = undefined;
      // #
    } else {
      // E' stato selezionato un oggetto, creo una copia
      const attStDeb = clone(attivitaStatoDeb);
      // Effettuo una pulizia del dato dai parametri di FE
      this._riscaUtilities.sanitizeFEProperties(attStDeb);
      // Assegno l'oggetto localmente
      this.attivitaStatoDebSelezionato = attStDeb;
    }
  }

  /**
   * Funzione invocata nel momento in cui la lista di stati debitori per il popolamento della select risulta pronto.
   * @param listaAttStatoDeb AttivitaSDVo[] con la lista stati debitori scaricati.
   */
  private onGetListAttivitaStatoDeb(listaAttStatoDeb: AttivitaSDVo[]) {
    // Setto localmente i dati
    this.listaAttivitaStatoDeb = listaAttStatoDeb;

    // Verifico se esiste uno stato debitorio selezionato in un momento precedente
    if (this.attivitaStatoDebSelezionato) {
      // Definisco le informazioni per il set dati
      const f = this.mainForm;
      const field = this.RR_C.ATTIVITA_RIMBORSO_SD;
      const list = this.listaAttivitaStatoDeb;
      const data = this.attivitaStatoDebSelezionato;
      // Funzione di find
      const find = (a: AttivitaSDVo, b: AttivitaSDVo) => {
        // Se uno dei due è undefined, non li confronto nemmeno
        if (a == undefined || b == undefined) {
          // Sono certamente diversi
          return false;
        }
        // Altrimenti controllo se sono uguali
        return a.cod_attivita_stato_deb == b.cod_attivita_stato_deb;
      };

      // Seleziono il valore
      this._riscaUtilities.setFormValueAndSelect(f, field, list, data, find);
    }
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
    // Resetto gli alert
    this.resetAlertConfigs(this.alertConfigs);
    this.resetAlertConfigs(this.alertRicercaConfigs);

    // Tramite referenza, chiedo il reset dei filtri
    this.riscaRicercaRimborsi.onFormReset();
    // Resetto anche il form di questo componente
    this.mainForm?.reset();
    this.mainFormSubmitted = false;

    // Lancio la clear dei dati delle tabelle
    this.ricercaSDxRimborsiTable = undefined;
    this.sdSelezionatixRimborsiTable = undefined;
    this.paginazioneSDRimborsi = undefined;

    // Cancello i filtri di ricerca impostati
    this.filtriSDRimborsi = undefined;
    this.filtriSDRIniziali = undefined;

    // Resetto le informazioni per gli stati debitori selezionati
    this.statiDebitoriSelezionati = [];
    this.statiDebitoriSelezionatiRows = [];
    this.allSDSelezionati = false;
    this.sdSelezionatiApriChiudi = false;
  }

  /**
   * Funzione collegata al pulsante CERCA della pagina.
   * La funzione prevede la richiesta di submit dei filtri da parte del sotto componente di ricerca.
   */
  avviaRicerca() {
    // Tramite referenza, chiedo il submit dei filtri
    this.riscaRicercaRimborsi.onFormSubmit();
  }

  /**
   * ##################################################
   * FUNZIONI COLLEGATE AGLI EVENTI DEL FORM DI RICERCA
   * ##################################################
   */

  /**
   * Funzione collegata all'evento: onFormSubmit; del componente di ricerca.
   * La funzione riceverà i filtri di ricerca generati dal componente.
   * @param filtri IFiltriRicercaRimborsi con le informazioni dei filtri generati dal componente.
   */
  onFiltriReady(filtri: IFiltriRicercaRimborsi) {
    // Verifico l'input
    if (!filtri) {
      // Nessuna configurazione
      return;
    }
    // Resetto gli alert
    this.resetAlertConfigs(this.alertConfigs);
    this.resetAlertConfigs(this.alertRicercaConfigs);

    // Resetto i dati della tabella
    this.ricercaSDxRimborsiTable = undefined;
    this.sdSelezionatixRimborsiTable = undefined;
    this.paginazioneSDRimborsi = undefined;
    // Resetto le informazioni per gli sd selezionati
    this.statiDebitoriSelezionati = [];
    this.statiDebitoriSelezionatiRows = [];
    this.allSDSelezionati = false;
    this.sdSelezionatiApriChiudi = false;

    // Aggiorno i filtri di locali
    this.filtriSDRimborsi = filtri;
    this.filtriSDRIniziali = filtri;
    // Lancio la funzione di ricerca dati
    this.cercaSDxRimborsi();
  }

  /**
   * ########################################
   * FUNZIONI DI GESTIONE PER LA RICERCA DATI
   * ########################################
   */

  /**
   * Funzione funzione che lancia la ricerca dei dati e gestisce il flusso per i risultati ottenuti.
   */
  cercaSDxRimborsi() {
    // Verifico se esiste la paginazione
    if (!this.paginazioneSDRimborsi) {
      // Non esite, recupero il default dalla tabella
      this.paginazioneSDRimborsi = RicercaSDxRimborsiTablePagination();
    }

    // Recupero la paginazione per la ricerca
    const p = this.paginazioneSDRimborsi;
    // Recupero i filtri di ricerca
    const f = this.filtriSDRimborsi;
    // Converto i filtri di ricerca in una struttura gestibile per fare la query
    const q: IRicercaRimborsi = this.convertiFiltriRicerca(f);

    // Richiamo il servizio di ricerca stati debitori con filtri rimborsi
    this.searchSDByFiltriRimborsi(q, p).subscribe({
      next: (ricerca: RicercaPaginataResponse<StatoDebitorioVo[]>) => {
        // Aggiorno i risultati di ricerca
        this.onCercaSDxRimborsi(ricerca);
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
   * @param q IRicercaRimborsi con l'oggetto di filtro per la ricerca.
   * @param p RiscaTablePagination con l'oggetto di paginazione per la ricerca.
   * @returns Osbervable<RicercaPaginataResponse<StatoDebitorioVo[]>> con la richiesta di ricerca dati.
   */
  private searchSDByFiltriRimborsi(
    q: IRicercaRimborsi,
    p: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> {
    // Ricerco mediante servizio
    return this._ricercaRimborsi.searchSDByFiltriRimborsi(q, p);
  }

  /**
   * Funzione che gestisce il risultato della ricerca degli stati debitori applicando i filtri rimborsi.
   * @param ricerca RicercaPaginataResponse<StatoDebitorioVo[]> con i risultati di ricerca.
   */
  private onCercaSDxRimborsi(
    ricerca: RicercaPaginataResponse<StatoDebitorioVo[]>
  ) {
    // Verifico l'input
    if (!ricerca) {
      // Niente da fare
      return;
    }

    // Estraggo le informazioni dall'oggetto di ricerca
    const { paging, sources } = ricerca;
    // Estraggo dalla risposta le informazioni utili
    const statiDebitori: StatoDebitorioVo[] = sources;
    const paginazione: RiscaTablePagination = paging;

    // Verifico se esistono risultati
    if (sources?.length > 0) {
      // Esistono risultati, aggiorno la tabella
      this.onCercaSDxRimborsiWithData(statiDebitori, paginazione);
      // #
    } else {
      // Non esistono risultati, visualizzo un altro messaggio
      this.onCercaSDxRimborsiNoData();
    }
  }

  /**
   * Funzione di comodo che gestisce la logica per quando la ricerca ritorna correttamente dei dati.
   * @param statiDebitori StatoDebitorioVo[] con la lista di dati scaricati.
   * @param paginazione RiscaTablePagination con l'oggetto di configurazione per la nuova paginazione.
   */
  private onCercaSDxRimborsiWithData(
    statiDebitori: StatoDebitorioVo[],
    paginazione: RiscaTablePagination
  ) {
    // Verifico se le classi per le tabelle sono state inizializzate
    if (!this.ricercaSDxRimborsiTable) {
      // Creo la struttura per la tabella con i risultati di ricerca
      this.ricercaSDxRimborsiTable = new RicercaSDxRimborsiTable();
    }
    if (!this.sdSelezionatixRimborsiTable) {
      // Creo la struttura per la tabella con gli stati debitori selezionati
      this.sdSelezionatixRimborsiTable = new SDSelezionatixRimborsiTable();
    }

    // Esistono risultati, aggiorno la tabella
    this.ricercaSDxRimborsiTable.setElements(statiDebitori);
    // Aggiorno la paginazione per la tabella e localmente
    this.paginazioneSDRimborsi = paginazione;
    this.ricercaSDxRimborsiTable.updatePaginazioneAfterSearch(paginazione);
  }

  /**
   * Funzione di comodo che gestisce la logica per quando la ricerca non ritorna dati.
   */
  private onCercaSDxRimborsiNoData() {
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
    this.paginazioneSDRimborsi = pagination;
    // Lancio la ricerca dati
    this.cercaSDxRimborsi();
  }

  /**
   * Funzione invocata al momento della selezione di tutte le righe della tabella, tramite checkbox nella riga di testata.
   * @param rows RiscaTableDataConfig<StatoDebitorioVo[]> con tutte le righe selezionate/deselezionate.
   */
  onTuttiSDSelezionati(rows: RiscaTableDataConfig<StatoDebitorioVo[]>) {
    // Aggiorno il valore nella classe della tabella di ricerca
    this.ricercaSDxRimborsiTable.tutteLeRigheSelezionate = false;
    this.ricercaSDxRimborsiTable.sourceSelected = [];
    // Aggiorno il valore nella classe della tabella dei selezionati
    this.sdSelezionatixRimborsiTable.clear();

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
    this.statiDebitoriSelezionatiRows = clone(rows);

    // Aggiorno il source per la tabella di tracciamento
    this.sdSelezionatixRimborsiTable.setNewElements(sd);
  }

  /**
   * Funzione collegata al pulsante "DETTAGLIO" della tabella.
   * Viene recuperato il dato dalla tabella originale e viene visualizzato il dettaglio.
   * @param row RiscaTableDataConfig<StatoDebitorioVo> che definisce la riga della tabella.
   */
  onDettaglioRimborso(row: RiscaTableDataConfig<StatoDebitorioVo>) {
    // Verifico l'input
    if (!row) {
      // Blocco il flusso
      return;
    }

    // Recupero il dato originale
    const statoDebitorio: StatoDebitorioVo = row.original;
    // Chiamo la funzione per aprire il dettaglio
    this.scaricaSDEApriDettaglioRimborsi(statoDebitorio);
  }

  /**
   * Funzione dedicata all'apertura della scheda dettaglio per i rimborsi dello stato debitorio.
   * NOTA BENE: lo stato debitorio presente in tabella è un oggetto con un set di dati minore rispetto allo stato debitorio effettivo.
   *            La funzione scaricherà il singolo stato debitorio completo per l'apertura della pagina di dettaglio.
   * @param statoDebitorio StatoDebitorioVo con l'oggetto recuperato dalla lista.
   */
  private scaricaSDEApriDettaglioRimborsi(statoDebitorio: StatoDebitorioVo) {
    // Recupero dallo stato debitorio il suo id
    const idStatoDeb = statoDebitorio?.id_stato_debitorio;
    // Verifico l'esistenza del dato
    if (idStatoDeb == undefined) {
      // Manca il dato
      return;
    }

    // Scarico il dato dello stato debitorio
    this._datiContabili.getStatoDebitorio(idStatoDeb).subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Apro il dettaglio
        this.apriDettaglioRimborsi(statoDebitorio);
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la gestione degli errori
        this.handleServiziError(e);
      },
    });
  }

  /**
   * Funzione che compila le informazioni per l'apertura del dettaglio delle rimborsi.
   * @param statoDebitorio StatoDebitorioVo con le informazioni complete dello stato debitorio.
   */
  private apriDettaglioRimborsi(statoDebitorio: StatoDebitorioVo) {
    // Salvo lo stato debitorio all'interno del servizio dedicato
    this._dettaglioRimborsi.setStatoDebitorio(statoDebitorio);
    // Definisco lo state da passare alla pagina
    const state: IDettRimborsiRouteParams = {};
    // Definisco le configurazioni per il cambio di pagina
    const targetRoute: AppRoutes = AppRoutes.dettaglioRimborsi;
    const extras: NavigationExtras = { state };
    const jStepTarget: IJStepConfig = this.RR_C.NAVIGATION_CONFIG;
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
   * Funzione agganciata all'evento di azione custom da parte della tabella degli stati debitori.
   * @param customAction IRiscaTableACEvent<any> con la configurazione dati per il pulsante premuto.
   */
  onSDCustomAction(customAction: IRiscaTableACEvent<any>) {
    // Recupero l'oggetto stato debitorio della riga
    const statoDebitorio: StatoDebitorioVo = customAction?.row?.original;

    // Verifico quale pulsante è stato premuto
    switch (customAction.action.action) {
      case this.RR_C.SCARICA_FILE_ACTION: {
        // Richiamo il download del file della pratica
        this.onScaricaFile(statoDebitorio);
        break;
      }
    }
  }

  /**
   * Funzione richiamata nel momento in cui un utente vuole scaricare un file.
   * @param statoDebitorio StatoDebitorioVo che contiene le informazioni del file da scaricare.
   */
  onScaricaFile(statoDebitorio: StatoDebitorioVo) {
    // Resetto l'alert dei messaggi
    this.aggiornaAlertConfigs(this.alertConfigs);
    // Prendo l'id della riscossione
    const { id_riscossione } = statoDebitorio;
    // Scarico il file
    this._riscaStampaP.stampaEApriPDFPratica(id_riscossione);
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
      this.statiDebitoriSelezionatiRows.splice(iSD, 1);
      // Vado ad aggiornare la selezione all'interno della tabella
      this.tableSDRimborsi.updateSelectedRowsByOrigin(
        this.statiDebitoriSelezionati
      );
      // Rimuovo l'elemento dalla tabella di tracciamento
      this.sdSelezionatixRimborsiTable.removeRow(row);
    }
  }

  /**
   * ####################################
   * FUNZIONI DI GESTIONI PULSANTI FOOTER
   * ####################################
   */

  /**
   * Funzione collegata al pulsante "CREA REPORT".
   * Verranno eseguite le funzioni per la gestione della creazione report.
   */
  creaReport() {
    // Resetto possibili alert aperti
    this.resetAlertConfigs();

    // Recupero dal componente le informazioni per la ricerca
    const f: IFiltriRicercaRimborsi = this.filtriSDRimborsi;
    // Converto i filtri di ricerca in una struttura gestibile per fare la query
    const q: IRicercaRimborsi = this.convertiFiltriRicerca(f);

    // Se non esiste l'oggetto di ricerca devo bloccare la creazione report
    if (!q) {
      // Nessun oggetto di ricerca
      return;
    }

    // Richiamo la creazione di un report
    this._report.createReportRicercaRimborsi(q).subscribe({
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
    // Propago l'alert tramite servizio
    this.emettiAlert(this.alertConfigs);
  }

  /**
   * ######################################################################
   * FUNZIONI DI GESTIONE DELL'APPLICAZIONE STATO Rimborsi A SD SELEZIONATI
   * ######################################################################
   */

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns IApplicaAttivitaRimborso contente le informazioni del form in modalità raw value.
   * @override
   */
  getMainFormRawValue(c?: any): IApplicaAttivitaRimborso {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      // Ritorno undefined
      return undefined;
    }

    // Il main form esiste, ritorno i dati del form
    let data: IApplicaAttivitaRimborso = cloneDeep(this.mainForm.getRawValue());
    // Verifico esista l'oggetto
    if (!data) {
      // Ritorno undefined
      return undefined;
    }

    // Effettuo una pulizia del dato dai parametri di FE
    this._riscaUtilities.sanitizeFEProperties(data.attivitaRimborsoSD);
    // Ritorno al form l'oggetto dati
    return data;
  }

  /**
   * Funzione agganciata all'evento di Submit per il mainForm.
   * @override
   */
  onFormSubmit() {
    // Verifico che la form esisti
    if (!this.mainForm) {
      return;
    }

    // Lancio la funzione di pre-validazione del form
    this.prepareMainFormForValidation();
    // Il form è stato submittato
    this.mainFormSubmitted = true;

    // Verifico che la form sia valida
    if (this.mainForm.valid) {
      // Recupero i dati della form
      const formData: IApplicaAttivitaRimborso = this.getMainFormRawValue();
      // Gestisco il success per il submit della form
      this.aggiornaStatiDebitori(formData);
      // #
    } else {
      // Gestisco la visualizzazione degli errori del form usiForm
      this.onFormErrors(this.mainForm);
    }
  }

  /**
   * ###############
   * FORM IN SUCCESS
   * ###############
   */

  /**
   * Funzione che gestisce le logiche d'aggiornamento per gli stati debitori selezionati.
   * @param formData IApplicaAttivitaRimborsi con le informazioni per l'aggiornamento.
   */
  private aggiornaStatiDebitori(formData: IApplicaAttivitaRimborso) {
    // Recupero lo stato d'assegnare agli stati debitori
    const attivitaStatoDeb: AttivitaSDVo = formData?.attivitaRimborsoSD;

    // Verifico se l'utente ha selezionato tutti gli stati debitori
    if (this.allSDSelezionati) {
      // Lancio la funzione per la gestione di tutti gli stati debitori
      this.aggiornaTuttiSDRimborsi(attivitaStatoDeb);
      // #
    } else {
      // Aggiornamento della selezione manuale degli stati debitori
      this.aggiornaSDSelezionati(attivitaStatoDeb);
    }
  }

  /**
   * Funzione che aggiorna tutti gli stati debitori selezionati.
   * La funzione richiamerà una specifica API passando i filtri di ricerca e l'attività stato debitorio per l'aggiornamento.
   * L'API ricercherà tutti gli stati debitori ed aggiornerà i dati, restituendo le informazioni aggiornate.
   * @param attivitaStatoDeb AttivitaSDVo con l'oggetto per l'aggiornamento degli stati debitori.
   */
  private aggiornaTuttiSDRimborsi(attivitaStatoDeb: AttivitaSDVo) {
    // Recupero gli ultimi filtri di ricerca usati
    const filtri: IFiltriRicercaRimborsi = this.filtriSDRimborsi;
    // Converto i filtri in un oggetto di ricerca
    const ricercaSD: IRicercaRimborsi = this.convertiFiltriRicerca(filtri);

    // Lancio la funzione per l'aggiornamento degli stati debitori
    this._ricercaRimborsi
      .updateAllSDRimborsi(ricercaSD, attivitaStatoDeb)
      .subscribe({
        next: (sdUpdate: StatoDebitorioVo[]) => {
          // Richiamo la funzione al success della modifica
          this.onUpdateSDRimborsi(sdUpdate);
        },
        error: (e: RiscaServerError) => {
          // Richiamo la gestione degli errori
          this.handleServiziError(e);
        },
      });
  }

  /**
   * Funzione che aggiorna gli stati debitori selezionati manualmente dalla tabella.
   * @param attivitaStatoDeb AttivitaSDVo con l'oggetto per l'aggiornamento degli stati debitori.
   */
  private aggiornaSDSelezionati(attivitaStatoDeb: AttivitaSDVo) {
    // Recupero gli stati debitori selezionati
    let sdSelezionati: StatoDebitorioVo[] = this.statiDebitoriSelezionati;
    // Aggiorno tutti gli stati debitori, modificando l'attività stato debitorio
    sdSelezionati = sdSelezionati.map((sd: StatoDebitorioVo) => {
      // Modifico l'attività stato debitorio dello stato debitorio
      sd.attivita_stato_deb = attivitaStatoDeb;
      // Ritorno l'oggetto stato debitorio aggiornato
      return sd;
    });

    // Lancio la funzione per l'aggiornamento degli stati debitori
    this._ricercaRimborsi.updateSDRimborsi(sdSelezionati).subscribe({
      next: (sdUpdate: StatoDebitorioVo[]) => {
        // Richiamo la funzione al success della modifica
        this.onUpdateSDRimborsi(sdUpdate);
      },
      error: (e: RiscaServerError) => {
        // Richiamo la gestione degli errori
        this.handleServiziError(e);
      },
    });
  }

  /**
   * Funzione richiamata nel momento in cui viene eseguita correttamente la modifica agli stati debitori.
   * @param sdUpdate StatoDebitorioVo[] con gli stati debitori aggiornati.
   */
  private onUpdateSDRimborsi(sdUpdate: StatoDebitorioVo[]) {
    // Lancio il reset della maschera
    this.annullaRicerca();

    // Gestisco il messaggio di risultati trovati
    const a = this.alertRicercaConfigs;
    const m = [this._riscaMessages.getMessage(RiscaNotifyCodes.P001)];
    const t = RiscaInfoLevels.success;
    // Aggiorno l'alert
    this.aggiornaAlertConfigs(a, m, t);
  }

  /**
   * #############
   * FORM IN ERROR
   * #############
   */

  /**
   * Funzione di supporto che gestisce la visualizzazione dei messaggi d'errore per il form group passato in input.
   * @param formGroup FormGroup da verificare.
   * @param errConfigs IRFCFormErrorsConfigs contenente le configurazioni extra per la gestione della funzione.
   * @override
   */
  protected onFormErrors(
    formGroup: FormGroup,
    errConfigs?: IRFCFormErrorsConfigs
  ) {
    // Variabili di comodo
    const fg = formGroup;
    const ec = errConfigs;
    // Recupero le configurazioni
    const { formErrors, serverError } = ec || {};

    // Definisco al mappatura degli errori da recuperare
    const me = formErrors || this.formErrors || [];
    // Recupero tutti i messaggi
    const mef = this._riscaUtilities.getAllErrorMessagesFromForm(fg, me);

    // Verifico e recupero un eventuale messaggio d'errore dall'oggetto errore del server
    const mse =
      this._riscaUtilities.getMessageFromRiscaServerError(serverError);
    // Verifico se esiste il messaggio
    if (mse) {
      // Aggiungo il messaggio alla lista dei messaggi errore
      mef.push(mse);
    }

    // Emetto l'evento comunicando i messaggi
    this.onComponenteErrors(mef);
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione collegata all'evento: onFormErrors; del componente di ricerca.
   * La funzione riceverà i messaggi d'errore generati dal componente.
   * @param errori string[] con la lista degli errori generati dal form.
   */
  onComponenteErrors(errori: string[]) {
    // Segnalo gli errori lanciati dal form
    const a = this.alertConfigs;
    const m = errori;
    const t = RiscaInfoLevels.danger;
    // Aggiorno l'alert con gli errori
    this.aggiornaAlertConfigs(a, m, t);
  }

  /**
   * Funzione che definisce un comportamento standard quando viene emesso l'evento: onAlertHidden; da parte del componente: risca-alert.
   * @param hidden boolean che definisce lo stato di nascosto dell'alert.
   * @param alertConfigs RiscaAlertConfigs da resettare.
   */
  onAlertHidden(hidden: boolean, alertConfigs: RiscaAlertConfigs) {
    // Verifico il risultato
    if (hidden) {
      // Resetto la configurazione dell'alert
      this.resetAlertConfigs(alertConfigs);
    }
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
   * Funzione che gestisce il prompt dei messaggi.
   * Se i parametri non sono definiti, verrà effettuato un reset.
   * @param c RiscaAlertConfigs d'aggiornare con le nuove informazioni.
   * @param messaggi Array di string contenente i messaggi da visualizzare.
   * @param tipo TRiscaAlertType che definisce la tipologia di alert da visualizzare.
   */
  aggiornaAlertConfigs(
    c?: RiscaAlertConfigs,
    messaggi?: string[],
    tipo?: TRiscaAlertType
  ) {
    // Verifico se esiste l'oggetto per l'alert, altrimenti imposto quello locale
    c = c ?? this.alertConfigs;
    // Aggiorno la configurazione
    c = this._riscaAlert.aggiornaAlertConfigs(c, messaggi, tipo);
    // Propago l'alert tramite servizio
    this.emettiAlert(c);
  }

  /**
   * Funzione di gestione degli errori generati dalle chiamate ai servizi.
   * @param e RiscaServerError con il dettaglio dell'errore generato.
   */
  handleServiziError(e: RiscaServerError) {
    // Lancio la gestione dell'errore
    this.onServiziErrorLocal(e);
  }

  /**
   * Funzione di comodo che imposta il messaggio d'errore
   * @param error RiscaServerError che definisce il corpo dell'errore.
   * @param messageCode string che definisce il messaggio d'errore da visualizzare in testata. Default è 'E005'.
   * @param otherMessages Array di string contenente un array con altri messaggi di dettaglio per l'errore. Default è [].
   */
  private onServiziErrorLocal(
    error: RiscaServerError,
    messageCode = RiscaNotifyCodes.E005,
    otherMessages: string[] = []
  ) {
    // Definisco un array di messaggi d'errore
    let erroriValidazione = this._riscaAlert.messagesFromServerError(
      error,
      messageCode,
      otherMessages
    );

    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(
      this.alertConfigs,
      erroriValidazione,
      RiscaInfoLevels.danger
    );
    // Propago l'alert tramite servizio
    this.emettiAlert(this.alertConfigs);
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
   * #####################################
   * FUNZIONI PER GESTIRE MEGLIO IL CODICE
   * #####################################
   */

  /**
   * Funzione che converte l'oggetto generato dal form di ricerca, in un oggetto compatibile con i filtri di ricerca della query.
   * @param f IFiltriRicercaRimborsi con l'oggetto da convertire.
   * @returns IRicercaRimborsi con l'oggetto convertito.
   */
  private convertiFiltriRicerca(f: IFiltriRicercaRimborsi): IRicercaRimborsi {
    // Converto l'oggetto
    const q =
      this._ricercaRimborsi.convertIFiltriRicercaRimborsiToIRicercaRimborsi(f);
    // Ritorno l'oggetto convertito
    return q;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertConfigs);
  }

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertRicercaConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertRicercaConfigs);
  }

  /**
   * Getter di comodo che verifica se esistono dati all'interno della tabella per la visualizzazione dei dati di ricerca.
   * @returns boolean con il risultato del check della tabella.
   */
  get checkTableRicerca(): boolean {
    // Verifico se esiste la tabella e ha dei dati al suo interno
    return this.ricercaSDxRimborsiTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che verifica se esistono dati all'interno della tabella per la visualizzazione dei dati selezionati.
   * @returns boolean con il risultato del check della tabella.
   */
  get checkTableTracciatore(): boolean {
    // Verifico se esiste la tabella e ha dei dati al suo interno
    return this.sdSelezionatixRimborsiTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che verifica se esistono dati all'interno della lista degli stati debitori selezionati.
   * @returns boolean con il risultato del check.
   */
  get checkSDSelezionati(): boolean {
    // Definisco le condizioni di attivazione pulsante
    const sdSel = this.statiDebitoriSelezionati?.length > 0;
    const allSDSel = this.allSDSelezionati;
    // Ritorno il check sulle condizioni
    return sdSel || allSDSel;
  }

  /**
   * Getter di comodo che restitusce un oggetto IJourneySnapshot per generare una snapshot del componente.
   */
  get snapshotConfigs(): IJourneySnapshot {
    return {
      source: this,
      mapping: this.RR_C.SNAPSHOT_CONFIG.mapping,
      saveFunc: this.RR_C.SNAPSHOT_CONFIG.saveFunc,
    };
  }

  /**
   * Getter che verifica se è stata effettuata una ricerca, quindi un oggetto di ricerca è presente nel componente.
   * @returns boolean con il risultato del check.
   */
  get checkFiltriSDRimborsi(): boolean {
    // Verifico che esista l'oggetto di ricerca
    return this.filtriSDRimborsi != undefined;
  }
}
