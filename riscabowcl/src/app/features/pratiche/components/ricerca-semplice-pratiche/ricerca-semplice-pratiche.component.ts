import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { RiscossioneSearchResultVo } from '../../../../core/commons/vo/riscossione-search-result-vo';
import { IJourneySnapshot } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import { RiscaRicercaSemplicePraticheTable } from '../../../../shared/classes/risca-table/ricerca-semplice-pratiche/ricerca-semplice-pratiche.table';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { RiscaRicercaSemplicePraticheComponent } from '../../../../shared/components/risca/risca-ricerca-semplice-pratiche/risca-ricerca-semplice-pratiche.component';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { IRichiestaLockPraticaRes } from '../../../../shared/services/risca/risca-lock-pratica/utilities/risca-lock-pratica.interfaces';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import {
  IRicercaPraticaSempliceRes,
  RicercaPraticaSemplice,
  RiscaAzioniPratica,
  RiscaInfoLevels,
  RiscaServerError,
  RiscaTablePagination,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { RicercaSemplicePraticheConsts } from '../../consts/ricerca-semplice-pratiche/ricerca-semplice-pratiche.consts';
import { IRModificaPratica } from '../../interfaces/ricerca-pratiche/ricerca-pratiche.interfaces';
import { RicercaPraticheService } from '../../service/ricerca-pratiche/ricerca-pratiche.service';
import { RicercaSemplicePraticheService } from '../../service/ricerca-semplice-pratiche/ricerca-semplice-pratiche.service';

/**
 * Interfaccia che definisce i possibili route params passati alla pagina.
 */
export interface IRSRouteParams {}

@Component({
  selector: 'ricerca-semplice-pratiche',
  templateUrl: './ricerca-semplice-pratiche.component.html',
  styleUrls: ['./ricerca-semplice-pratiche.component.scss'],
})
export class RicercaSemplicePraticheComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy
{
  /** Costanti associate al componente */
  RSP_C = RicercaSemplicePraticheConsts;

  /** ViewChild che permette la connessione al componente: RiscaRicercaSemplicePraticheComponent. */
  @ViewChild('refRRSP') refRRSP: RiscaRicercaSemplicePraticheComponent;

  /** RiscaRicercaSemplicePraticheTable contentene la configurazione e i dati per la tabella delle pratiche ricercate. */
  tablePratiche = new RiscaRicercaSemplicePraticheTable();

  /** IRicercaPraticaSempliceReq utilizzata per la gestione della tabella delle pratiche. */
  ricercaPS: {
    ricerca: RicercaPraticaSemplice;
    paginazione: RiscaTablePagination;
  };

  /**
   * Costruttore.
   */
  constructor(
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _ricercaPratiche: RicercaPraticheService,
    private _ricercaSemplice: RicercaSemplicePraticheService,
    riscaAlertService: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessageService: RiscaMessagesService,
    private _router: Router,
    private _user: UserService
  ) {
    super(
      logger,
      navigationHelper,
      riscaAlertService,
      riscaFormSubmitHandler,
      riscaMessageService
    );
    this.stepConfig = this.RSP_C.NAVIGATION_CONFIG;
  }

  ngOnInit() {
    // Richiamo l'init per i dati possibilmante salvati come snapshot
    this.initJSnapshot();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * Funzione di init per i dati della snapshot salvata in journey.
   */
  private initJSnapshot() {
    // Recupero i parametri dallo state della route
    const state = this._navigationHelper.getRouterState(this._router);
    // Carico i dati della snapshot
    this.loadJSnapshot(this);
    // Attivo il setup dei dati di route
    this.setupSnapshotData(state);
  }

  /**
   * Funzione di supporto che verifica e carica i dati del componente in base ai route params e i dati della snapshot.
   * @param routeParams IRSRouteParams contenente i dati recuperati dal servizio di router.
   */
  private setupSnapshotData(routeParams: IRSRouteParams) {
    // Verifico se è presente l'oggetto per la form ricerca pratiche
    if (this.ricercaPS) {
      // Ottengo i dati dalla snapshot
      const { ricerca, paginazione } = this.ricercaPS;
      // I dati esistono, recupero la paginazione
      this.tablePratiche.updatePaginazione(paginazione);
      // Rilancio la ricerca
      this.avviaRicercaSemplice(ricerca, paginazione);
    }
  }

  /** Al click del pulsante di ricerca pratica, chiede alla form di inviare i parametri di ricerca */
  ricercaSemplice() {
    // Svuoto gli errori
    this._riscaAlert.aggiornaAlertConfigs(this.alertConfigs);
    // Richiamo il submit della form
    this.refRRSP.onFormSubmit();
  }

  /** Al click del pulsante Annulla, cancella i risultati la form di ricerca semplice */
  annullaRicercaSemplice() {
    // Resetta la paginazione
    const paginazioneDefault = this.tablePratiche.getDefaultPagination();
    this.tablePratiche.updatePaginazione(paginazioneDefault);
    this.ricercaPS.paginazione = paginazioneDefault;
    // Cancello i dati della snapshot
    this.ricercaPS = undefined;
    // Richiamo il reset della form
    this.refRRSP.onFormReset();
    // Pulisco la tabella dei risultati
    this.tablePratiche.clear();
  }

  /**
   * Funzione che lancia la ricerca semplice.
   * @param ricerca IRicercaPraticaSempliceReq con i dati per la ricerca.
   */
  avviaRicercaSemplice(
    ricerca: RicercaPraticaSemplice,
    paginazione?: RiscaTablePagination
  ) {
    // Se non sono inviati dati per la paginazione, li prendo dalla tabella
    if (paginazione == null) {
      paginazione = this.tablePratiche.getPaginazione();
    }

    // Aggiorno la snapshot
    this.ricercaPS = { ricerca, paginazione };
    // Lancio la ricerca aventi i campi per la versione: semplice
    this._ricercaSemplice
      .getRicercaPraticaSemplice(ricerca, paginazione)
      .subscribe({
        next: (
          response: RicercaPaginataResponse<IRicercaPraticaSempliceRes[]>
        ) => {
          // Aggiorno l'alert
          this.aggiornaAlertConfigs(this.alertConfigs);
          // Richiamo la funzione di gestione dei risultati della ricerca
          this.onRicercaSemplice(response);
          // #
        },
        error: (error: any) => {
          // Gestisco il messaggio d'errore inaspettato
          this.onServiziError(error);
        },
      });
  }

  /**
   * Funzione che definisce la gestione dei dati a seguito della ricerca.
   * @param response RicercaPaginataResponse<IRicercaPraticaSempliceRes[]> con i dati ottenuti dalla ricerca.
   */
  private onRicercaSemplice(
    response: RicercaPaginataResponse<IRicercaPraticaSempliceRes[]>
  ) {
    // Estraggo dall'input le informazioni della risposta
    const praticheRes: IRicercaPraticaSempliceRes[] = response?.sources;

    // Verifico le pratiche ricercate e se ne ho trovata solo una gestisco l'apertura automatica del dettaglio
    if (praticheRes?.length === 1) {
      // Estraggo l'unico risultato
      const pratica: IRicercaPraticaSempliceRes = praticheRes[0];
      // Passo la pratica all'apertura automatica
      this.ricercaSempliceAutoDettaglio(pratica);
      // #
    } else {
      // Gestisco la comunicazione per i risultati di ricerca
      this.onRicercaSempliceInfo(praticheRes);
      // Aggiorno la paginazione
      this.tablePratiche.updatePaginazioneAfterSearch(response.paging);
      // Inserisco i dati trovati nella tabella dei risultati
      this.tablePratiche.setElements(praticheRes);
    }
  }

  /**
   * Funzione di supporto che gestisce la comunicazione all'utente per i risultati di ricerca semplice.
   * @param listaPratiche IRicercaPraticaSempliceRes[] con i dati ottenuti dalla ricerca.
   */
  private onRicercaSempliceInfo(listaPratiche: IRicercaPraticaSempliceRes[]) {
    // Verifico la condizione sui soggetti
    if (!listaPratiche || listaPratiche.length == 0) {
      // Definisco il codice del messaggio
      const code = RiscaNotifyCodes.I001;
      // Recupero il messaggio per gruppi non trovati
      const gruppiMsg = this._riscaMessages.getMessage(code);

      // Variabili di comodo
      const a = this.alertConfigs;
      const w = RiscaInfoLevels.warning;
      // Aggiorno l'alert
      this.aggiornaAlertConfigs(a, [gruppiMsg], w);
      // #
    }
  }

  /**
   * Funzione che gestisce l'automatismo per l'apertura del dettaglio di una singola pratica dopo la ricerca semplice.
   * Vengono definite le logiche di flag per il pulsante indietro, per evitare di creare un loop infinito di chiamate.
   * @param praticaSearch IRicercaPraticaSempliceRes con i dati della pratica da aprire.
   */
  private ricercaSempliceAutoDettaglio(
    praticaSearch: IRicercaPraticaSempliceRes
  ) {
    // L'automatimo parte solo se è stata trovata una singola pratica
    if (praticaSearch) {
      // Lancio la funzione del pulsante "ANNULLA", per resettare le informazioni
      this.annullaRicercaSemplice();

      // Recupero l'id della pratica e vado ad aprire il dettaglio dell'unica pratica trovata
      const { id_riscossione } = praticaSearch || {};
      this.modificaDettaglioPratica(id_riscossione);
    }
  }

  /**
   * Emissione della pratica per fare la modifica
   * @param row RiscaTableDataConfig<RiscossioneSearchResultVo> con i dati della riga che identifica una pratica.
   */
  modificaPratica(row: RiscaTableDataConfig<RiscossioneSearchResultVo>) {
    // Verifico l'input
    if (!row) {
      // Blocco il flusso
      return;
    }

    // Recupero il dato originale
    const pratica: RiscossioneSearchResultVo = row.original;
    // Recupero l'id della pratica
    const { id_riscossione } = pratica || {};

    // Lancio la funzione di apertura pratica
    this.modificaDettaglioPratica(id_riscossione);
  }

  /**
   * Funzione che gestisce le informazioni e apre il dettaglio di una pratica.
   * @param idPratica numbre che definisce l'id delle pratica per l'apertura del dettaglio.
   */
  private modificaDettaglioPratica(idPratica: number) {
    // Il lock della riscossione è attivo solo se il ruolo non è consultatore
    if (this._user.isCONSULTATORE) {
      // Richiamo la funzione per l'apertura della pratica
      this.onRichiediBloccoPratica(idPratica);
      // #
    } else {
      // Verifico il lock sulla riscossione prima di aprirla
      this._riscaLockP
        .bloccaPratica(idPratica)
        .pipe(
          tap((resLock: IRichiestaLockPraticaRes) => {
            // Richiamo il servizio di lock per la gestione del risultato
            this._riscaLockP.utenteLockaPratica(resLock);
            // #
          })
        )
        .subscribe({
          next: (resLock: IRichiestaLockPraticaRes) => {
            // Richiamo la funzione per l'apertura della pratica
            this.onRichiediBloccoPratica(idPratica);
            // #
          },
          error: (e: RiscaServerError) => {
            // Richiamo la funzione di gestione errori dal server
            this.onServiziError(e);
            // #
          },
        });
    }
  }

  /**
   * Funzione di comodo invocata a seguito del controllo sul lock della pratica.
   * @param idRiscossione number con l'id risconssione da aprire.
   */
  private onRichiediBloccoPratica(idRiscossione: number) {
    // Definisco la configurazione per la modifica pratica
    const config: IRModificaPratica = {
      idRiscossione: idRiscossione,
      currentNav: RiscaAzioniPratica.ricercaSemplice,
      stepConfig: this.stepConfig,
      snapshotConfigs: this.snapshotConfigs,
    };

    // Definisco i livelli per il filo d'arianna
    const ricercaAFA: FALivello = this._riscaFA.ricercaSemplice;
    const dettPraticaFA: FALivello = this._riscaFA.dettaglioPratica;
    // Imposto il filo d'arianna
    this._riscaFA.aggiungiSegmentoByLivelli(ricercaAFA, dettPraticaFA);

    // Richiamo la funzionalità per la modifica pratica
    this._ricercaPratiche.richiediModificaPratica(config);
  }

  /**
   * Questa funzione ricarica la ricerca ma passa anche i dati della paginazione al BE,
   * così da cambiare pagina
   * @param $event RiscaTablePagination con i dati della paginazione
   */
  cambiaPagina(event: RiscaTablePagination) {
    // Aggiorno la paginazione per la navigazione, da salvare in memoria in caso di cambio scheda applicativo.
    this.ricercaPS.paginazione = event;
    // Ricarico i dati
    const { ricerca, paginazione } = this.ricercaPS;
    // Avvio la ricerca
    this.avviaRicercaSemplice(ricerca, paginazione);
  }

  /**
   * Getter di comodo che restitusce un oggetto IJourneySnapshot per generare una snapshot del componente.
   */
  get snapshotConfigs(): IJourneySnapshot {
    return {
      source: this,
      mapping: this.RSP_C.SNAPSHOT_CONFIG.mapping,
      saveFunc: this.RSP_C.SNAPSHOT_CONFIG.saveFunc,
    };
  }
}
