import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs/index';
import { map } from 'rxjs/operators';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaUtilitiesComponent } from 'src/app/shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import {
  IRiscaTabChanges,
  RiscaDatiContabili,
  RiscaIstanzePratica,
  RiscaServerError,
  RiscaTablePagination,
} from 'src/app/shared/utilities';
import { RicercaPaginataResponse } from '../../../../../core/classes/http-helper/http-helper.classes';
import {
  PraticaEDatiTecnici,
  PraticaVo,
} from '../../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { StatiDebitoriDCTable } from '../../../../../shared/classes/risca-table/dati-contabili/stati-debitori.table';
import { RiscaTableDataConfig } from '../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { RiscaSpinnerService } from '../../../../../shared/services/risca-spinner.service';
import { RiscaLockPraticaService } from '../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { StatiDebitoriConsts } from '../../../consts/dati-contabili/stati-debitori.consts';
import { DatiContabiliUtilityService } from '../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { DatiContabiliService } from '../../../service/dati-contabili/dati-contabili/dati-contabili.service';
import { InserisciPraticaService } from '../../../service/inserisci-pratica/inserisci-pratica.service';
import { StatiDebitoriDedicatedService } from './services/stati-debitori-dedicated.service';

@Component({
  selector: 'stati-debitori',
  templateUrl: './stati-debitori.component.html',
  styleUrls: ['./stati-debitori.component.scss'],
  providers: [DatiContabiliService, StatiDebitoriDedicatedService],
})
export class StatiDebitoriComponent
  extends RiscaUtilitiesComponent
  implements OnInit, OnDestroy
{
  /** Costante per le informazioni del componente. */
  SD_C = StatiDebitoriConsts;

  /** Input che permette di gestire l'inizializzazione dello scarico dati, per default è true. */
  @Input() isDatiContabiliActive = true;

  /** Subscription registrato per il cambio dei dati della pratica. */
  onPraticaChange: Subscription;
  /** Subscription registrato per il cambio della tab di navigazione per inserisci pratica. */
  onISTabChanged: Subscription;
  /** Subscription registrato per l'avvenuto lock della pratica. */
  onPraticaLocked: Subscription;
  /** Subscription registrato per l'avvenuto unlock della pratica. */
  onPraticaUnlocked: Subscription;

  /** Number con l'id della riscossione. */
  idPratica: number;
  /** PraticaVo con l'oggetto della riscosssione. */
  pratica: PraticaVo;
  /** StatoDebitorioVo che definisce la riga selezionata dalla tabella. */
  statoDebitorio: StatoDebitorioVo;

  /** Boolean di comodo per la gestione del refresh del DOM a seguito dell'inserimento/modifica pratica. */
  showActions: boolean;

  /** StatiDebitoriTable che conterrà le configurazioni per la tabella dei documenti e allegati. */
  statiDebitoriTable: StatiDebitoriDCTable;

  /**
   * Costruttore
   */
  constructor(
    private _datiContabili: DatiContabiliService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _inserisciPratica: InserisciPraticaService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaMessages: RiscaMessagesService,
    private _riscaSpinner: RiscaSpinnerService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _sdDedicated: StatiDebitoriDedicatedService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);
    // Richiamo la funzione di setup del componente
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
    // Imposto la visualizzazione attiva
    this.showActions = true;

    // Lancio il reset delle informazioni di ogni possibile stato debitorio salvato nel servizio
    this.resetStatoDebitorio();
    // Lancio il setup dei dati della pratica
    this.setupPratica();
    // Lancio il setup per la tabella degli stati debitori (N.B.: DA FARE DOPO SETUP PRATICA!)
    this.setupTables();
  }

  /**
   * Funzione di comodo per il reset delle informazioni dello stato debitorio salvato nel servizio condiviso.
   */
  private resetStatoDebitorio() {
    // Resetto lo stato debitorio condiviso
    this._datiContabiliUtility.resetStatoDebitorio();
  }

  /**
   * Funzione di comodo per il recupero dell'informazione della pratica dal servizio condiviso.
   */
  private setupPratica() {
    // Recupero dal servizio condiviso le informazioni per l'id pratica
    this.idPratica = this._datiContabiliUtility.idPratica;
    // Recupero dal servizio l'oggetto della pratica
    this.pratica = this._datiContabiliUtility.pratica;
  }

  /**
   * Funzione di setup per le tabelle del componente.
   */
  private setupTables() {
    // Lancio il setup per la tabella degli stati debitori
    this.statiDebitoriTable = new StatiDebitoriDCTable({
      pratica: this.pratica,
      statiDebitori: [],
    });
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
   * Funzione di comodo per l'init del componente.
   */
  private initComponente() {
    // Lancio la funzione di init del componente
    this.initStatiDebitori();

    // La prima ricerca avviene solo se la modalità della pratica è: modifica
    if (this.modifica && this.isDatiContabiliActive) {
      // Recupero dal servizio una possibile paginazione memorizzata da riapplicare, se è il primo accesso sarà undefined
      const p: RiscaTablePagination = this.paginazioneSDUtente;
      // Avvio la ricerca dei dati contabili
      this.cercaStatiDebitori(p);
    }
  }

  /**
   * Funzione di init per il componente.
   */
  private initStatiDebitori() {
    // Lancio l'inizzazione dei listener
    this.initListeners();
  }

  /**
   * Funzione di init per i listener del componente, per rimanere in ascolto degli eventi per il cambio pagina dei dati contabili.
   */
  private initListeners() {
    // Ascoltatore cambio dati della pratica
    this.onPraticaChange = this._inserisciPratica.onPraticaChanges$.subscribe({
      next: (praticaEDatiTecnici: PraticaEDatiTecnici) => {
        // Aggiorno l'id pratica per il componente
        this.idPratica = praticaEDatiTecnici?.pratica?.id_riscossione;
        this.pratica = praticaEDatiTecnici?.pratica;
        // La ricerca resetta la selezione dello stato debitorio selezionato nella tabella
        this.statoDebitorio = undefined;
        this.statoDebitorioUtente = undefined;
        // Rilancio l'aggiornamento dati
        this.cercaStatiDebitori();
      },
    });

    // Ascoltatore cambio tab pratica
    this.onISTabChanged = this._inserisciPratica.onISTabChanges$.subscribe({
      next: (tabs: IRiscaTabChanges) => {
        // Lancio la funzione di verifica per scaricare i dati del componente
        this.isTabMovingHere(tabs);
      },
    });

    // Ascoltatore per la pratica lockata
    this.onPraticaLocked = this._riscaLockP.onPraticaLocked$.subscribe({
      next: () => {
        // Gestisco il refresh delle action
        this.updateActions();
      },
    });

    // Ascoltatore per la pratica unlockata
    this.onPraticaUnlocked = this._riscaLockP.onPraticaUnlocked$.subscribe({
      next: () => {
        // Gestisco il refresh delle action
        this.updateActions();
      },
    });
  }

  /**
   * Funzione che verifica il cambio di tab della componente padre: inserisci-pratica.
   * Se avviene un cambio verso questa tab e i dati contabili non erano attivi, allora rilancio lo scarico dati.
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   */
  private isTabMovingHere(tabs: IRiscaTabChanges) {
    // Recupero il tab dei dati contabili
    const datiContabili = RiscaIstanzePratica.datiContabili;
    // Verifico se il tab sta cambiando in questo specifico tab
    const isComingHere = this._riscaUtilities.movingIntoTab(
      tabs,
      datiContabili
    );

    // Controllo se ci stiamo quindi spostando su questa specifica sezione
    if (isComingHere) {
      // La ricerca resetta la selezione dello stato debitorio selezionato nella tabella
      this.statoDebitorio = undefined;
      this.statoDebitorioUtente = undefined;
      // Avvio la ricerca dei dati contabili
      this.cercaStatiDebitori();
    }
  }

  /**
   * ###################
   * FUNZIONI DI DESTROY
   * ###################
   */

  ngOnDestroy() {
    try {
      // Verifico le subscription
      if (this.onPraticaChange) {
        this.onPraticaChange.unsubscribe();
      }
      if (this.onISTabChanged) {
        this.onISTabChanged.unsubscribe();
      }
      if (this.onPraticaLocked) {
        this.onPraticaLocked.unsubscribe();
      }
      if (this.onPraticaUnlocked) {
        this.onPraticaUnlocked.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * ########################
   * FUNZIONI DI RICERCA DATI
   * ########################
   */

  /**
   * Effettua l'avvio della ricerca dei dati-contabili
   */
  private cercaStatiDebitori(paginazione?: RiscaTablePagination) {
    // Se non c'è la paginazione, metto quella di default
    if (!paginazione) {
      // Recupero la paginazione di default dalla tabella
      paginazione = this.statiDebitoriTable.getDefaultPagination();
    }

    // Lancio lo spinner per gli stati debitori
    this._riscaSpinner.show();

    // Lancio la ricerca dei dati contabili
    this._datiContabili
      .ricercaStatiDebitoriPagination(this.idPratica, paginazione)
      .pipe(
        map((response: RicercaPaginataResponse<StatoDebitorioVo[]>) => {
          // Variabili di comodo
          const r = response;
          const p = paginazione;
          // Richiamo la funzione di supporto per la gestione specifica dell'ordinamento per data scadenza e annualità dello stato debitorio
          return this._sdDedicated.sortDataScandenzaEAnnualita(r, p);
          // #
        })
      )
      .subscribe({
        next: (response: RicercaPaginataResponse<StatoDebitorioVo[]>) => {
          // Lancio la funzione per la response
          this.onCercaStatiDebitori(response);
          // Chiudo lo spinner per gli stati debitori
          this._riscaSpinner.hide();
          // #
        },
        error: (error: RiscaServerError) => {
          // Gestisco l'errore
          this.onServiziError(error);
          // Chiudo lo spinner per gli stati debitori
          this._riscaSpinner.hide();
          // #
        },
      });
  }

  /**
   * Funzione di comodo, invocata nel momento in cui la ricerca degli stati debitori è conclusa.
   * @param response RicercaPaginataResponse<StatoDebitorioVo[]> con il risultato della ricerca.
   */
  private onCercaStatiDebitori(
    response: RicercaPaginataResponse<StatoDebitorioVo[]>
  ) {
    // Resetto l'alert, pulendo messaggi vecchi
    this.resetAlertConfigs();

    // Recupero le informazioni dalla risposta del servizio
    const sources: StatoDebitorioVo[] = response.sources;
    const paging: RiscaTablePagination = response.paging;

    // Salvo la paginazione impostata nel servizio per mantenerla in memoria
    this.paginazioneSDUtente = paging;
    // Aggiorno la tabella degli stati debitori
    this.statiDebitoriTable.setElements(sources);
    this.selezionaStatoDebitorioTabella();
    // Aggiorno la paginazione
    this.statiDebitoriTable.updatePaginazioneAfterSearch(paging);
  }

  /**
   * Funzione di supporto che gestisce la selezione automatica di uno stato debitorio in sessione se l'utente ricarica la lista degli stati debitori dopo l'accesso ad una delle sotto sezioni degli stati debitori.
   */
  private selezionaStatoDebitorioTabella() {
    // Recupero lo stato debitorio dalla sessione
    const sdSessione: StatoDebitorioVo = this.statoDebitorioUtente;
    // Verifico se esiste effettivamente un dato in sessione
    if (!sdSessione) {
      // Non esiste un stato debitorio in sessione
      return;
    }

    // Esiste un dato in sessione, lo assegno localmente
    this.statoDebitorio = this.statoDebitorioUtente;

    // Definisco la logica per selezionare lo stato debitorio in sessione
    const trovaSDTabella = (statoDebitorioTab: StatoDebitorioVo) => {
      // Verifico se gli stati debitori sono gli stessi
      const idSDTab = statoDebitorioTab.id_stato_debitorio;
      const idSDSessione = sdSessione.id_stato_debitorio;
      // Comparo gli id
      return idSDTab === idSDSessione;
      // #
    };

    // Richiamo la funzione di set del dato per lo stato debitorio e assegno le righe selezionate
    let elementiSelezionati: RiscaTableDataConfig<StatoDebitorioVo>[];
    elementiSelezionati =
      this.statiDebitoriTable.setElementsSelection(trovaSDTabella);

    // Dovrebbe esserci un solo elemento nella lista, verifico che esista
    if (elementiSelezionati[0]) {
      // La riga è stata selezionata, recupero lo stato debitorio
      const sdRiga: StatoDebitorioVo = elementiSelezionati[0].original;
      // Lo stato debitorio della tabella è lo stato debitorio scaricato dal servizio.
      // Questo oggetto potrebbe essere stato modificato rispetto alla selezione precedente utente, aggiorno quindi il dato localmente
      this.statoDebitorio = sdRiga;
      this.statoDebitorioUtente = sdRiga;
      // #
    }
  }

  /**
   * #####################
   * GESTIONE DELLE AZIONI
   * #####################
   */

  /**
   * Funzione di comodo per l'aggiornamento del template dei pulsanti delle azioni.
   */
  private updateActions() {
    // Imposto il flag di visualizzazione a false e nascondo il DOM
    this.showActions = false;
    // Imposto un time per far riapparire i pulsanti, triggherando gli eventi del dom
    setTimeout(() => {
      // Visualizzo di nuovo il DOM
      this.showActions = true;
    }, 500);
  }

  /**
   * ####################
   * EVENTI SULLA TABELLA
   * ####################
   */

  /**
   * Questa funzione ricarica la ricerca semplice pratiche ma passa anche i dati della paginazione al BE,
   * così da cambiare pagina
   * @param $event RiscaTablePagination con i dati della paginazione
   */
  cambiaPagina(paginazione: RiscaTablePagination) {
    // La ricerca resetta la selezione dello stato debitorio selezionato nella tabella
    this.statoDebitorio = undefined;
    this.statoDebitorioUtente = undefined;
    // Avvio la ricerca
    this.cercaStatiDebitori(paginazione);
  }

  /**
   * Funzione invocata al momento della selezione di uno stato debitorio.
   * @param row RiscaTableDataConfig<any> con le informazioni della riga selezionata.
   */
  statoDebitorioSelezionato(row: RiscaTableDataConfig<any>) {
    // Recupero l'oggetto dello stato debitorio
    this.statoDebitorio = row?.original;
    // Creo una copia dello stato debitorio e lo salvo in sessione
    this.statoDebitorioUtente = this.statoDebitorio;
  }

  /**
   * ################
   * FUNZIONI BOTTONI
   * ################
   */

  /**
   * Risponde al click del pulsante Versamenti
   */
  clickVersamenti() {
    // Recupero lo stato debitorio selezionato
    const sd = this.statoDebitorio;
    // Richiamo l'evento per il cambio pagina
    this._datiContabiliUtility.navigateVersamenti(sd);
  }

  /**
   * Risponde al click del pulsante Rimborsi
   */
  clickRimborsi() {
    // Recupero lo stato debitorio selezionato
    const sd = this.statoDebitorio;
    // Richiamo l'evento per il cambio pagina
    this._datiContabiliUtility.navigateRimborsi(sd);
  }

  /**
   * Risponde al click del pulsante Accertamenti
   */
  clickAccertamenti() {
    // Recupero lo stato debitorio selezionato
    const sd = this.statoDebitorio;
    // Richiamo l'evento per il cambio pagina
    this._datiContabiliUtility.navigateAccertamenti(sd);
  }

  /**
   * Risponde al click del pulsante Stato Debitorio
   */
  clickStatoDebitorio() {
    // Recupero lo stato debitorio selezionato
    const sd = this.statoDebitorio;
    // Richiamo l'evento per il cambio pagina
    this._datiContabiliUtility.navigateStatoDebitorio(sd);
  }

  /**
   * Risponde al click del pulsante Inserimento Stato Debitorio
   */
  clickInserimentoStatoDebitorio() {
    // Richiamo l'evento per il cambio pagina
    this._datiContabiliUtility.navigateInserimentoStatoDebitorio();
  }

  /**
   * Risponde al click del pulsante NAP
   */
  clickNap() {
    // Recupero lo stato debitorio selezionato
    const sd = this.statoDebitorio;
    // Richiamo l'evento per il cambio pagina
    this._datiContabiliUtility.navigateNap(sd);
  }

  /**
   * Risponde al click del pulsante Calcola Interessi
   */
  clickCalcolaInteressi() {
    // Recupero lo stato debitorio selezionato
    const sd = this.statoDebitorio;
    // Richiamo l'evento per il cambio pagina
    this._datiContabiliUtility.navigateCalcolaInteressi(sd);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di verifica per la tabella degli stati debitori.
   */
  get checkStatiDebitoriTable() {
    // Verifico che la tabella esista e abbia dati
    return this.statiDebitoriTable?.source?.length > 0;
  }

  /**
   * Getter di comodo per il recupero della configurazione per il pulsante: Versamenti.
   */
  get btnVersamenti() {
    // Recupero la configurazione del pulsante
    return this.SD_C.BTN_CONFIG_VERSAMENTI;
  }

  /**
   * Getter di comodo per il recupero della configurazione per il pulsante: Rimborsi.
   */
  get btnRimborsi() {
    // Recupero la configurazione del pulsante
    return this.SD_C.BTN_CONFIG_RIMBORSI;
  }

  /**
   * Getter di comodo per il recupero della configurazione per il pulsante: Accertamenti.
   */
  get btnAccertamenti() {
    // Recupero la configurazione del pulsante
    return this.SD_C.BTN_CONFIG_ACCERTAMENTI;
  }

  /**
   * Getter di comodo per il recupero della configurazione per il pulsante: StDebitorio.
   */
  get btnStDebitorio() {
    // Recupero la configurazione del pulsante
    return this.SD_C.BTN_CONFIG_ST_DEBITORIO;
  }

  /**
   * Getter di comodo per il recupero della configurazione per il pulsante: StDebitorioIns.
   */
  get btnStDebitorioIns() {
    // Recupero la configurazione del pulsante
    return this.SD_C.BTN_CONFIG_ST_DEBITORIO_INS;
  }

  /**
   * Getter di comodo per il recupero della configurazione per il pulsante: NAP.
   */
  get btnNAP() {
    // Recupero la configurazione del pulsante
    return this.SD_C.BTN_CONFIUG_NAP;
  }

  /**
   * Getter di comodo per il recupero della configurazione per il pulsante: CalcInteressi.
   */
  get btnCalcInteressi() {
    // Recupero la configurazione del pulsante
    return this.SD_C.BTN_CONFIG_CALC_INTERESSI;
  }

  /**
   * Getter di comodo per il recupero della configurazione per la sezione associata al pulsante: Versamenti.
   */
  get sezioneVersamenti() {
    // Recupero la configurazione del pulsante
    return RiscaDatiContabili.versamenti;
  }

  /**
   * Getter di comodo per il recupero della configurazione per la sezione associata al pulsante: Rimborsi.
   */
  get sezioneRimborsi() {
    // Recupero la configurazione del pulsante
    return RiscaDatiContabili.rimborsi;
  }

  /**
   * Getter di comodo per il recupero della configurazione per la sezione associata al pulsante: Accertamenti.
   */
  get sezioneAccertamenti() {
    // Recupero la configurazione del pulsante
    return RiscaDatiContabili.accertamenti;
  }

  /**
   * Getter di comodo per il recupero della configurazione per la sezione associata al pulsante: StDebitorio.
   */
  get sezioneStDebitorio() {
    // Recupero la configurazione del pulsante
    return RiscaDatiContabili.statoDebitorio;
  }

  /**
   * Getter di comodo per il recupero della configurazione per la sezione associata al pulsante: StDebitorioIns.
   */
  get sezioneStDebitorioIns() {
    // Recupero la configurazione del pulsante
    return RiscaDatiContabili.inserimentoStatoDebitorio;
  }

  /**
   * Getter di comodo per il recupero della configurazione per la sezione associata al pulsante: NAP.
   */
  get sezioneNAP() {
    // Recupero la configurazione del pulsante
    return RiscaDatiContabili.nap;
  }

  /**
   * Getter di comodo per il recupero della configurazione per la sezione associata al pulsante: CalcInteressi.
   */
  get sezioneCalcInteressi() {
    // Recupero la configurazione del pulsante
    return RiscaDatiContabili.calcolaInteressi;
  }

  /**
   * Getter per la paginazione utente salvata in sessione per gli stati debitori della tabella dei dati contaibili.
   * @returns RiscaTablePagination con l'oggetto di paginazione impostato alla ricerca utente.
   */
  get paginazioneSDUtente(): RiscaTablePagination {
    // Recupero il dato all'interno del servizio
    return this._datiContabiliUtility.paginazioneSDUtente;
    // #
  }

  /**
   * Setter per la paginazione utente sa salvare in sessione per gli stati debitori della tabella dei dati contaibili.
   * @param paginazione RiscaTablePagination con l'oggetto di paginazione impostato alla ricerca utente.
   */
  set paginazioneSDUtente(paginazione: RiscaTablePagination) {
    // Recupero il dato all'interno del servizio
    this._datiContabiliUtility.paginazioneSDUtente = paginazione;
    // #
  }

  /**
   * Getter per lo stato debitorio selezionato dall'utente salvato in sessione della tabella dei dati contaibili.
   * @returns StatoDebitorioVo con l'oggetto selezionato dall'utente.
   */
  get statoDebitorioUtente(): StatoDebitorioVo {
    // Recupero il dato all'interno del servizio
    return this._datiContabiliUtility.statoDebitorioUtente;
    // #
  }

  /**
   * Setter per lo stato debitorio selezionato dall'utente salvato in sessione della tabella dei dati contaibili.
   * @param statoDebitorio StatoDebitorioVo con l'oggetto selezionato dall'utente.
   */
  set statoDebitorioUtente(statoDebitorio: StatoDebitorioVo) {
    // Recupero il dato all'interno del servizio
    this._datiContabiliUtility.statoDebitorioUtente = statoDebitorio;
    // #
  }
}
