import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { Subscription } from 'rxjs/index';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { RicercaPaginataResponse } from '../../../../../../core/classes/http-helper/http-helper.classes';
import { PraticaVo } from '../../../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../../../core/commons/vo/stato-debitorio-vo';
import { StatiDebitoriNAPTable } from '../../../../../../shared/classes/risca-table/numero-avviso-pagamento/stati-debitori-nap.table';
import { RiscaTableDataConfig } from '../../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { IRiscaTableACEvent } from '../../../../../../shared/components/risca/risca-table/utilities/risca-table.interfaces';
import { RiscaUtilitiesComponent } from '../../../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaLockPraticaService } from '../../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaStampaPraticaService } from '../../../../../../shared/services/risca/risca-stampa/risca-stampa-pratica.service';
import { RiscaUtilitiesService } from '../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  RiscaServerError,
  RiscaTablePagination,
} from '../../../../../../shared/utilities';
import { DatiContabiliUtilityService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { DatiContabiliService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili.service';
import { StatiDebitoriNAPService } from '../../../../service/dati-contabili/stati-debitori-nap.service';
import { StatiDebitoriNAPConsts } from './utilities/stati-debitori-nap.consts';

@Component({
  selector: 'stati-debitori-nap',
  templateUrl: './stati-debitori-nap.component.html',
  styleUrls: ['./stati-debitori-nap.component.scss'],
})
export class StatiDebitoriNAPComponent
  extends RiscaUtilitiesComponent
  implements OnInit, OnDestroy
{
  /** Oggetto contenente una serie di costanti per la gestione del componente. */
  SD_NAP_C = new StatiDebitoriNAPConsts();

  /** StatoDebitorioVo che identifica i dati dello stato debitorio in modifica/dettaglio. */
  @Input() statoDebitorio: StatoDebitorioVo;

  /** EventEmitter che comunica al componente padre lo stato debitorio di cui è stato richiesto il dettaglio. */
  @Output() onDettaglioStatoDebitorio = new EventEmitter<StatoDebitorioVo>();

  /** number con il totale degli importi versati degli stati debitori collegati al NAP. */
  canoneDovuto: number | string;

  /** Subscription che permette di collegarsi all'evento di errore generato dalla stampa pratica pdf. */
  private onStampaError$: Subscription;

  /**
   * ################################
   * TABELLE IMPIEGATE NEL COMPONENTE
   * ################################
   */
  /** NAPTable che definisce la tabella di gestione dati per i dati del NAP. */
  sdByNAPTable: StatiDebitoriNAPTable;

  /**
   * Costruttore
   */
  constructor(
    private _datiContabili: DatiContabiliService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaMessages: RiscaMessagesService,
    private _riscaStampaP: RiscaStampaPraticaService,
    private _riscaUtilities: RiscaUtilitiesService,
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
   * Funzione di setup del componente.
   */
  private setupComponente() {
    // Lancio la funzione di setup dei listener
    this.setupListeners();
  }

  /**
   * Funzione di setup che permette al componente di agganciarsi agli eventi asincroni.
   */
  private setupListeners() {
    // Mi aggancio all'evento di errore stampa pdf pratica
    this.onStampaError$ = this._riscaStampaP.onStampaError$.subscribe({
      next: (e: RiscaServerError) => {
        // Gestisco l'errore generato
        this.onServiziError(e);
      },
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
   * Funzione di init del componente.
   */
  private initComponente() {
    // Lancio l'init per la tabella degli stati debitori per nap
    this.initTableNAP();
  }

  /**
   * Funzione di setup per la tabella dello stato debitorio.
   */
  private initTableNAP() {
    // Recupero dal servizio l'ultima paginazione salvata
    const lastPagination = this._statiDebitoriNAP.lastPagination;
    // Lancio la generazione della tabella stati debitori x nap
    this.searchStatiDebitoriByNAP(this.nap, lastPagination);
  }

  /**
   * ##############################
   * FUNZIONI DI DESTROY COMPONENTE
   * ##############################
   */

  /**
   * OnDestroy.
   */
  ngOnDestroy() {
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onStampaError$) {
        this.onStampaError$.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione che genera la tabella degli stati debitori per stesso NAP.
   * @param nap string che definisce il nap di riferimento per lo scarico dati.
   * @param pagination RiscaTablePagination che definisce l'oggetto per la paginazione della tabella.
   */
  private searchStatiDebitoriByNAP(
    nap: string,
    pagination?: RiscaTablePagination
  ) {
    // Verifico l'input
    if (nap == undefined) {
      // Niente configurazione
      return;
    }
    // Verifico se esiste la tabella è da instanziare
    if (!this.sdByNAPTable) {
      // Recupero l'oggetto della pratica tramite getter
      const pratica = this.pratica;
      // Creo l'istanza della tabella
      this.sdByNAPTable = new StatiDebitoriNAPTable({ pratica });
    }

    // Verifico se è definita una configurazione specifica per la paginazione
    const pag = pagination
      ? pagination
      : this.sdByNAPTable.getDefaultPagination();

    // Richiamo il servizio per lo scarico dati
    this._datiContabili
      .getStatiDebitoriByNAPPagination(nap, [], pag)
      .subscribe({
        next: (response: RicercaPaginataResponse<StatoDebitorioVo[]>) => {
          // Richiamo la funzione per gestire i risultati della ricerca
          this.onSearchStatiDebitoriByNAP(response);
          // Lancio il calcolo per il totale degli importi degli SD x nap
          this.canoneDovutoStatiDebitoriByNAP(this.nap);
          // #
        },
        error: (error: any) => {
          // Gestisco il messaggio d'errore inaspettato
          this.onServiziError(error);
          // #
        },
      });
  }

  /**
   * Funzione che gestisce i risultati della ricerca per gli stati debitori per stesso NAP.
   * @param response RicercaPaginataResponse<StatoDebitorioVo[]> con i risultati della ricerca.
   */
  private onSearchStatiDebitoriByNAP(
    response: RicercaPaginataResponse<StatoDebitorioVo[]>
  ) {
    // Estraggo dalla risposta le informazioni utili
    const statiDebitori = response.sources;
    const paginazione = response.paging;

    // Salvo nel servizio la paginazione come ultima paginazione effettuata
    this._statiDebitoriNAP.lastPagination = paginazione;

    // Inserisco i dati trovati nella tabella dei risultati
    this.sdByNAPTable.setElements(statiDebitori);
    // Aggiorno la paginazione
    this.sdByNAPTable.updatePaginazioneAfterSearch(paginazione);
    // Dopo l'aggiornamento, vado a definire i vari flag per la dilazione
    this.sdByNAPTable.setElementsSelection((sd: StatoDebitorioVo) => {
      // Recupero il flag dilazione
      return sd?.flg_dilazione ?? false;
    });
  }

  /**
   * Funzione che imposta la chiamata di recupero per il totale degli importi versati di ogni stato debitorio del nap.
   * @param nap string che definisce il nap di riferimento per lo scarico dati.
   */
  private canoneDovutoStatiDebitoriByNAP(nap: string) {
    // Verifico l'input
    if (nap == undefined) {
      // Interrompo il flusso
      return;
    }

    // Richiamo il servizio per il calcolo del totale
    this._datiContabili.canoneDovutoStatiDebitoriByNAP(nap).subscribe({
      next: (canoneDovuto: number) => {
        // Converto il canone dovuto
        let canoneIT: number | string;
        canoneIT = this._riscaUtilities.formatoImportoITA(canoneDovuto, 2, true);
        // Assegno il valore nella variabile di componente
        this.canoneDovuto = canoneIT;
        // #
      },
      error: (e: RiscaServerError) => {
        // Lancio la gestione dell'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * ######################
   * FUNZIONI DELLE TABELLE
   * ######################
   */

  /**
   * Funzione che gestisce il cambio pagina per la tabella degli stati debitori x nap.
   * @param paginazione RiscaTablePagination con i dati della paginazione.
   */
  cambiaPagina(paginazione: RiscaTablePagination) {
    // Avvio la ricerca
    this.searchStatiDebitoriByNAP(this.nap, paginazione);
  }

  /**
   * Gestione delle custom action relative alla tabella: stati debitori nap;
   * Verrà gestita la tipologia di custom action e attivata una logica specifica.
   * @param customAction IRiscaTableACEvent<StatoDebitorioVo> contenente le informazioni sulla custom action triggherata.
   */
  onTabNAPCustomAction(customAction: IRiscaTableACEvent<StatoDebitorioVo>) {
    switch (customAction.action.action) {
      case this.SD_NAP_C.TAB_NAP_SD_STAMPA_PRATICA: {
        // Richiamo la funzione per la stampa della pratica collegata allo stato debitorio/NAP
        this.stampaPraticaNAPSD(customAction?.row);
        break;
      }
      default: {
        // Nessuna azone
      }
    }
  }

  /**
   * Funzione che gestisce la stampa della pratica definita dallo stato debitorio collegato al NAP.
   * @param row RiscaTableDataConfig<StatoDebitorioVo> con le informazioni della riga premuta.
   */
  private stampaPraticaNAPSD(row: RiscaTableDataConfig<StatoDebitorioVo>) {
    // Verifico l'input
    if (!row || !row.original || !row.original.id_riscossione) {
      // Non esistono dati per la riscossione
      return;
    }

    // Recupero l'id riscossione dall'oggetto della riga
    const idR = row.original.id_riscossione;
    // Lancio il servizio di stampa
    this._riscaStampaP.stampaEApriPDFPratica(idR);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per il dato della pratica dal servizio di utility.
   */
  get pratica(): PraticaVo {
    // Recupero l'oggetto pratica dal servizio
    return this._datiContabiliUtility.pratica;
  }

  /**
   * Getter di verifica per la tabella del nap.
   */
  get checkNAPTable(): boolean {
    // Verifico che la tabella esista e abbia dati
    return this.sdByNAPTable?.source?.length > 0;
  }

  /**
   * Getter per il nap dello stato debitorio in input.
   */
  get nap(): string {
    // Ritorno il nap dello stato debitorio
    return this.statoDebitorio?.nap;
  }

  /**
   * Getter per il totale degli stati debitori per stesso NAP.
   */
  get numeroUtenze(): number {
    // Ritorno il totale degli elementi della tabella, altrimenti 0
    return this.sdByNAPTable?.getPaginazione()?.total || 0;
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
