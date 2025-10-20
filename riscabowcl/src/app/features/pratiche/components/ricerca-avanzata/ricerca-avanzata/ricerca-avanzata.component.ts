import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgbNavChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { clone } from 'lodash';
import { tap } from 'rxjs/operators';
import { RicercaPaginataResponse } from '../../../../../core/classes/http-helper/http-helper.classes';
import { IReportLocationVo } from '../../../../../core/commons/vo/report-location-vo';
import { RiscossioneSearchResultVo } from '../../../../../core/commons/vo/riscossione-search-result-vo';
import {
  IRicercaIstanzaVo,
  IRicercaProvvedimentoVo,
  RiscossioneSearchV2Vo,
} from '../../../../../core/commons/vo/riscossione-search-vo';
import { IJourneySnapshot } from '../../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { LoggerService } from '../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../../core/services/user.service';
import { RiscaRicercaAvanzataPraticheTable } from '../../../../../shared/classes/risca-table/ricerca-avanzata/ricerca-avanzata-pratiche.table';
import { FASegmento } from '../../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { FALivello } from '../../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaFormParentComponent } from '../../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { RiscaTableDataConfig } from '../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../../shared/consts/common-consts.consts';
import { RiscaSelectService } from '../../../../../shared/services/form-inputs/risca-select/risca-select.service';
import { RiscaAlertService } from '../../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaFormSubmitHandlerService } from '../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { IRichiestaLockPraticaRes } from '../../../../../shared/services/risca/risca-lock-pratica/utilities/risca-lock-pratica.interfaces';
import { RiscaMessagesService } from '../../../../../shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppClaimants,
  IRicercaPraticaAvanzataReduced,
  IRiscaTabChanges,
  RiscaAzioniPratica,
  RiscaInfoLevels,
  RiscaIstanzeRicercaAvanzata,
  RiscaServerError,
  RiscaTablePagination,
} from '../../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IAlertConfigsFromCode } from '../../../../../shared/utilities/interfaces/risca/risca-alert/risca-alert.interface';
import { ReportService } from '../../../../report/service/report/report.service';
import { RicercaAvanzataNavClass } from '../../../class/navs/ricerca-avanzata-pratiche.nav.class';
import { FormRicercaAvanzataPraticheConsts } from '../../../consts/ricerca-avanzata-pratiche/form-ricerca-avanzata-pratiche.consts';
import { RicercaAvanzataPraticheConsts } from '../../../consts/ricerca-avanzata-pratiche/ricerca-avanzata-pratiche.consts';
import { IRModificaPratica } from '../../../interfaces/ricerca-pratiche/ricerca-pratiche.interfaces';
import { RicercaAvanzataPraticheService } from '../../../service/ricerca-avanzata-pratiche/ricerca-avanzata-pratiche/ricerca-avanzata-pratiche.service';
import { RicercaPraticheService } from '../../../service/ricerca-pratiche/ricerca-pratiche.service';
import { IRAConfigs } from '../../inserisci-pratica/utilities/inserisci-pratica.interfaces';
import { FormRicercaAvanzataPraticheSDComponent } from '../form-ricerca-avanzata-pratiche-sd/form-ricerca-avanzata-pratiche-sd.component';
import { IRicercaAvanzataPraticheSDFilters } from '../form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.classes';
import {
  IFRAPModalitaRicerca,
  IRicercaPraticaAvanzataForm,
} from '../form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.interfaces';

@Component({
  selector: 'ricerca-avanzata',
  templateUrl: './ricerca-avanzata.component.html',
  styleUrls: ['./ricerca-avanzata.component.scss'],
})
export class RicercaAvanzataComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente i valori costanti per il componente. */
  RRAP_C = new FormRicercaAvanzataPraticheConsts();
  /** Oggetto contenente i valori costanti del componente. */
  RAP_C = RicercaAvanzataPraticheConsts;

  /** Input IRAConfigs contenente i parametri per il setup della ricerca avanzata. */
  @Input() ricercaAvanzataConfigs: IRAConfigs;

  /** ViewChild che permette la connessione al componente: FormRicercaAvanzataPraticheComponent. */
  @ViewChild('refFRAPSD') refFRAPSD: FormRicercaAvanzataPraticheSDComponent;

  /** RiscaIstanzeRicercaAvanzata che definisce il target per la ricerca avanzata. */
  istanzaRicercaAvanzata: RiscaIstanzeRicercaAvanzata;
  /** RicercaAvanzataNavClass per gestire le praticaNav. */
  istanzaRicercaAvanzataNav: RicercaAvanzataNavClass;
  /** IFRAPModalitaRicerca con la modalita di ricerca che piloterà il form di ricerca. */
  modalitaRicerca: IFRAPModalitaRicerca;

  /** Tabelle per i risultati */
  raPraticheTable = new RiscaRicercaAvanzataPraticheTable();
  /** Oggetto utilizzato per la gestione della tabella delle pratiche. IRiscaRicercaForm */
  ricercaPA: IRicercaAvanzataPraticheSDFilters;

  constructor(
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _report: ReportService,
    private _ricercaAvanzata: RicercaAvanzataPraticheService,
    private _ricercaPratiche: RicercaPraticheService,
    riscaAlertService: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaSelect: RiscaSelectService,
    private _riscaUtilities: RiscaUtilitiesService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessageService: RiscaMessagesService,
    private _user: UserService
  ) {
    // Inizializzo il super
    super(
      logger,
      navigationHelper,
      riscaAlertService,
      riscaFormSubmitHandler,
      riscaMessageService
    );
    // Definisco la configurazione per lo step config
    this.stepConfig = this.RAP_C.NAVIGATION_CONFIG;
    // Richiamo il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Richiamo l'init del componente
    this.initComponente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
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
    // Setup della nav tabs
    this.setupNavTabs();
  }

  /**
   * Funzione di setup per i dati della nav tabs.
   */
  private setupNavTabs() {
    // Creo l'oggetto per le nav
    this.istanzaRicercaAvanzataNav = new RicercaAvanzataNavClass();
    // Definisco l'istanza iniziale
    this.setManualNav();
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di init del componente.
   */
  private initComponente() {
    // Init per le informazioni salvata come snapshot nel servizio
    this.initJSnapshot();
    // Init delle logiche per la gestione dei parametri di input
    this.initInputConfigs();
    // Init delle informazioni relative alla ricerca avanzata
    this.initRicercaPA();
    // Lancio il set dell'oggetto per la modalità di ricerca
    this.setModalitaRicerca();
  }

  /**
   * Funzione di init per i dati della snapshot salvata in journey.
   */
  private initJSnapshot() {
    // Carico i dati della snapshot
    this.loadJSnapshot(this);
  }

  /**
   * Funzione di init che verifica e gestisce le informazioni in input del componente.
   */
  private initInputConfigs() {
    // Recupero i dati di configurazione
    const configs = this.ricercaAvanzataConfigs;
    // Verifico se esistono informazioni
    if (!configs) {
      // Niente configurazione
      return;
    }

    // Esistono configurazioni, estraggo i dati
    const navTarget: RiscaIstanzeRicercaAvanzata = configs.navTarget;

    // Verifico se esiste un navTarget da impostare come attivo
    if (navTarget) {
      // E' definito un navtarget, gestisco manualmente il cambio
      this.setManualNav(navTarget);
    }
  }

  /**
   * Funzione di init che lancia la ricerca dati per le pratiche.
   */
  private initRicercaPA() {
    // Verifico se è presente l'oggetto per la form ricerca pratiche
    if (this.ricercaPA) {
      // Dall'oggetto di configurazione estraggo le informazioni per pre-popolare i filtri di ricerca
      const {
        datiTecnici,
        pratica,
        istanze,
        provvedimenti,
        paginazione,
        modalitaRicerca,
      } = this.ricercaPA;

      // I dati esistono, rilancio la ricerca aggiornando la paginazione
      this.raPraticheTable.updatePaginazione(paginazione);
      // Unisco le informazioni degli oggetti che ho ottenuto per avere una request con dati completi
      const praticaSearch: RiscossioneSearchV2Vo =
        this.creaOggettoRiscossioneSearch(
          datiTecnici,
          pratica,
          istanze,
          provvedimenti
        );

      // I dati esistono, rilancio la ricerca
      this.avviaRicercaAvanzata(praticaSearch, paginazione);
      // #
    }
  }

  /**
   * ####################################################
   * FUNZIONI COLLEGATE AL COMPONENTE DI RICERCA AVANZATA
   * ####################################################
   */

  /**
   * Funzione che gestisce la paginazione quando viene applicato un filtro per i dati delle bollettazioni.
   * @param paginazione RiscaTablePagination definisce la configurazione per la paginazione.
   * @returns RiscaTablePagination con la configurazione per la paginazione d'applicare.
   */
  private paginazioneRicercaAvanzata(
    paginazione?: RiscaTablePagination
  ): RiscaTablePagination {
    // Definisco l'oggetto della paginazione d'applicare
    let paginazioneConfig: RiscaTablePagination;
    // Recupero la paginazione di default per la tabella dei bollettini
    let defaultConfig: RiscaTablePagination =
      this.raPraticheTable.getDefaultPagination();

    // Verifico se esiste un oggetto di paginazione in input
    if (!paginazione) {
      // Non esiste paginazione, imposto quella di default
      paginazioneConfig = defaultConfig;
      // #
    } else {
      // Esiste la paginazione imposto dall'input
      paginazioneConfig = clone(paginazione);
      // Per specifiche funzionali, al filtra bisogna resettare la ricerca sulla base del campo e ordinamento di default
      paginazione.sortBy = defaultConfig.sortBy;
      paginazione.sortDirection = defaultConfig.sortDirection;
      // #
    }

    // Ritorno l'oggeto di configurazione
    return paginazioneConfig;
  }

  /**
   * Funzione collegata all'evento di submit del componente "FormRicercaAvanzataPraticheComponent".
   * La funzione raccoglierà le informazioni e farà partire la ricerca con i filtri passati alla funzione.
   * @param rapFilters IRicercaAvanzataPraticheFilters come oggetto contenente i filtri di ricerca.
   */
  onRicercaAvanzataSubmit(rapFilters: IRicercaAvanzataPraticheSDFilters) {
    // Resetto l'alert
    this.resetAlertConfigs();
    // Aggiorno i dati da salvare per la navigazione
    this.ricercaPA = rapFilters;

    // Definisco l'oggetto di paginazione da usare per la chiamata
    let paginazione: RiscaTablePagination = this.ricercaPA.paginazione;
    // Prendo la paginazione corrente
    paginazione = this.paginazioneRicercaAvanzata(paginazione);
    // Aggiorno l'oggetto della paginazione a seguito del ritorno dalla funzione precedente
    this.ricercaPA.paginazione = paginazione;

    // Ottengo i dati per la creazione dell'oggetto della ricerca
    const { datiTecnici, pratica, istanze, provvedimenti } = this.ricercaPA;

    // Unisco le informazioni degli oggetti che ho ottenuto per avere una request con dati completi
    let praticaSearch: RiscossioneSearchV2Vo;
    praticaSearch = this.creaOggettoRiscossioneSearch(
      datiTecnici,
      pratica,
      istanze,
      provvedimenti
    );
    // Avvio la ricerca
    this.avviaRicercaAvanzata(praticaSearch, paginazione);
  }

  /**
   * Funzione collegata all'evento di error del componente "FormRicercaAvanzataPraticheComponent".
   * La funzione raccoglierà le informazioni e visualizzerà un alert con le informazioni errate.
   * @param errors string[] con la lista di errori generati dal form.
   */
  onRicercaAvanzataError(errors: string[]) {
    // Variabili di comodo
    const a = this.alertConfigs;
    const m = errors;
    const t = RiscaInfoLevels.danger;
    // Aggiorno l'alert con i messaggi d'errore
    this.aggiornaAlertConfigs(a, m, t);
  }

  /**
   * ######################
   * FUNZIONI DELLE TABELLE
   * ######################
   */

  /**
   * Questa funzione ricarica la ricerca ma passa anche i dati della paginazione al BE,
   * così da cambiare pagina
   * @param $event RiscaTablePagination con i dati della paginazione
   */
  cambiaPagina(event: RiscaTablePagination) {
    // Aggiorno la paginazione
    this.ricercaPA.paginazione = event;
    // Ricarico i dati
    const { datiTecnici, pratica, istanze, provvedimenti, paginazione } =
      this.ricercaPA;

    // Unisco le informazioni degli oggetti che ho ottenuto per avere una request con dati completi
    const praticaSearch: RiscossioneSearchV2Vo =
      this.creaOggettoRiscossioneSearch(
        datiTecnici,
        pratica,
        istanze,
        provvedimenti
      );

    // Avvio la ricerca
    this.avviaRicercaAvanzata(praticaSearch, paginazione);
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

    // Il lock della riscossione è attivo solo se il ruolo non è consultatore
    if (this._user.isCONSULTATORE) {
      // Richiamo la funzione per l'apertura della pratica
      this.onRichiediBloccoPratica(id_riscossione);
      // #
    } else {
      // Verifico il lock sulla riscossione prima di aprirla
      this._riscaLockP
        .bloccaPratica(id_riscossione)
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
            this.onRichiediBloccoPratica(id_riscossione);
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
    // Creo una copia dell'oggetto dello stepConfig
    const stepConfig = clone(this.stepConfig);
    // Aggiungo all'oggetto dello step il chiamante in base alla tab selezionata
    stepConfig.stepClaimant = this.stepClaimant;
    // Definisco l'oggetto stateKeeper che conterrà la configurazione di ritorno per lo step
    const stateKeeper: IRAConfigs = { navTarget: this.istanzaRicercaAvanzata };
    stepConfig.stateKeeper = stateKeeper;

    // Definisco la configurazione per la modifica pratica
    const config: IRModificaPratica = {
      idRiscossione: idRiscossione,
      currentNav: RiscaAzioniPratica.ricercaAvanzata,
      stepConfig,
      snapshotConfigs: this.snapshotConfigs,
    };

    // Definisco i livelli per il filo d'arianna
    const dettPraticaFA: FALivello = this._riscaFA.dettaglioPratica;
    // Imposto il filo d'arianna
    this._riscaFA.aggiungiSegmentoByLivelli(dettPraticaFA);

    // Richiamo la funzionalità per la modifica pratica
    this._ricercaPratiche.richiediModificaPratica(config);
  }

  /**
   * #############################################
   * FUNZIONI COLLEGATI AI PULSANTI DI FINE PAGINA
   * #############################################
   */

  /**
   * Annulla la ricerca svuotando campi e risultati della ricercas
   */
  annullaRicercaAvanzata() {
    // Resetto il form
    this.refFRAPSD.annullaRicercaAvanzata();
    // Tolgo la tabella
    this.raPraticheTable.clear();
  }

  /**
   * Al click del pulsante di ricerca pratica, chiede alla form di inviare i parametri di ricerca.
   */
  ricercaAvanzata() {
    // Resetto la paginazione
    const newPaginazione = this.raPraticheTable.getDefaultPagination();
    this.raPraticheTable.updatePaginazione(newPaginazione);
    // Svuoto gli errori
    this._riscaAlert.aggiornaAlertConfigs(this.alertConfigs);
    // Richiesta invio form
    this.refFRAPSD.onFormSubmit();
  }

  /**
   * ############################################
   * FUNZIONI EFFETTIVE DI GESTIONE DELLA RICERCA
   * ############################################
   */

  /**
   * Funzione che lancia la ricerca avanzata delle pratiche.
   * @param riscossioneSearch RiscossioneSearchV2Vo con le informazioni per la ricerca.
   */
  avviaRicercaAvanzata(
    riscossioneSearch: RiscossioneSearchV2Vo,
    paginazione: RiscaTablePagination
  ) {
    // Chiamo il servizio
    this._ricercaAvanzata
      .getRicercaPraticaAvanzata(riscossioneSearch, paginazione)
      .subscribe({
        next: (
          response: RicercaPaginataResponse<IRicercaPraticaAvanzataReduced[]>
        ) => {
          // Aggiorno l'alert
          this.aggiornaAlertConfigs(this.alertConfigs);
          // Richiamo la funzione per gestire i risultati della ricerca
          this.onRicercaAvanzata(response);
          // #
        },
        error: (e: RiscaServerError) => {
          // Gestisco il messaggio d'errore inaspettato
          this.onServiziError(e);
          // Gestisco il bug grafico delle select
          this.refreshSelect();
        },
      });
  }

  /**
   * Funzione che gestisce i risultati della ricerca avanzata.
   * @param listaPratiche RicercaPaginataResponse<IRicercaPraticaAvanzataReduced[]> con i risultati della ricerca.
   */
  private onRicercaAvanzata(
    response: RicercaPaginataResponse<IRicercaPraticaAvanzataReduced[]>
  ) {
    // Estraggo dall'input le informazioni della risposta
    const praticheRes: IRicercaPraticaAvanzataReduced[] = response?.sources;

    // Gestisco la comunicazione per i risultati di ricerca
    this.onRicercaAvanzataInfo(praticheRes);
    // Aggiorno la paginazione
    this.raPraticheTable.updatePaginazioneAfterSearch(response.paging);
    // Inserisco i dati trovati nella tabella dei risultati
    this.raPraticheTable.setElements(praticheRes);
    // Gestisco il bug grafico delle select
    this.refreshSelect();
  }

  /**
   * Funzione di supporto che gestisce la comunicazione all'utente per i risultati di ricerca semplice.
   * @param listaPratiche IRicercaPraticaAvanzataReduced[] con i dati ottenuti dalla ricerca.
   */
  private onRicercaAvanzataInfo(
    listaPratiche: IRicercaPraticaAvanzataReduced[]
  ) {
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
    }
  }

  /**
   * #######################
   * GESTIONE DELLE NAV TABS
   * #######################
   */

  /**
   * Funzione che recupera un oggetto della nav.
   * Se viene passato un idLink verrà impostato quell'oggetto, se esiste.
   * Se non specificato, verrà impostato il default.
   * @param idLink RiscaIstanzeRicercaAvanzata per la ricerca della nav.
   */
  private setManualNav(idLink?: RiscaIstanzeRicercaAvanzata) {
    // Recupero il IRiscaNavItem per il link
    const navItem = this.istanzaRicercaAvanzataNav.getNav(idLink);
    // Tento di recuperare l'item
    const { ngbNavItem } = navItem || {};
    // Assegno l'item
    this.istanzaRicercaAvanzata = ngbNavItem;
  }

  /**
   * Funzione agganciata all'evento di cambio della tab per la nav bar.
   * @param nav NgbNavChangeEvent che definisce la nuova istanza.
   */
  onNavRicercaChange(nav: NgbNavChangeEvent) {
    // Recupero dall'evento di cambio nav le informazioni
    const { activeId, nextId } = nav;
    // Aggiorno la variabile locale che tiene traccia della tab attiva
    this.istanzaRicercaAvanzata = nextId;

    // Emetto l'evento per informare i tab della pratica che sta avvenendo un cambio
    this.navRicercaChanged(activeId, nextId);
  }

  /**
   * Funzione di comodo che crea i dati ed emette l'evento di cambio tab della pratica.
   * @param actual RiscaIstanzeRicercaAvanzata definisce l'id del tab attualmente attivo.
   * @param next RiscaIstanzeRicercaAvanzata che definisce l'id del target della tab.
   */
  private navRicercaChanged(
    actual: RiscaIstanzeRicercaAvanzata,
    next: RiscaIstanzeRicercaAvanzata
  ) {
    // Definisco l'oggetto per il tab changes
    const tabs: IRiscaTabChanges = { actual, next };

    // Modifico l'oggetto x modalità di ricerca configurazione
    this.setModalitaRicerca();
    // Lancio la funzione di gestione per il filo d'arianna
    this.gestisciFiloArianna(tabs);
    // Pulisco i dati della ricerca e resetto le informazioni
    this.annullaRicercaAvanzata();
  }

  /**
   * #####################
   * GESTIONE FILO ARIANNA
   * #####################
   */

  /**
   * Funzione che gestisce il filo d'arianna per la parte dedicata e specifica della ricerca avanzata.
   * @param tabs IRiscaTabChanges con le informazioni di spostamento tra le tab della ricerca avanzata.
   */
  private gestisciFiloArianna(tabs: IRiscaTabChanges) {
    // Rimuovo il segmento per la ricerca avanzata
    this.rimuoviSegmentoRicercaAvanzata();

    // Recupero il livello per la ricerca avanzata
    const raFA: FALivello = this._riscaFA.ricercaAvanzata;
    // Definisco un contenitore per il livello definito dalla tab specifica
    let livRAFA: FALivello;
    // Verifico dove ci stiamo spostando
    const movingToRP = this.movingToRAPratiche(tabs);
    const movingToRSD = this.movingToRAStatiDebitori(tabs);

    // Gestisco le casistiche specifiche per le tab
    if (movingToRP) {
      // Definisco il segmento per la ricerca pratica
      livRAFA = this._riscaFA.ricercaAvanzataPratiche;
      // #
    } else if (movingToRSD) {
      // Definisco il segmento per la ricerca stati debitori
      livRAFA = this._riscaFA.ricercaAvanzataStatiDebitori;
      // #
    }

    // Aggiorno il filo d'arianna con i nuovi livelli generati
    this._riscaFA.aggiungiSegmentoByLivelli(raFA, livRAFA);
  }

  /**
   * Funzione che effettua la rimozione del segmento per il filo d'arianna: ricerca avanzata.
   */
  private rimuoviSegmentoRicercaAvanzata() {
    // Recupero il livello per la ricerca avanzata
    const raFA = this._riscaFA.ricercaAvanzata;
    // Effettuo la ricerca all'interno dei segmenti per il filo d'arianna
    const segmentoRA: FASegmento = this._riscaFA.segmentoInFAByLivello(raFA);
    // Definisco un'unica variabile che contiene uno dei possibili valori ritornati
    const segmento = segmentoRA || undefined;

    // Verifico se è stato trovato un segmento
    if (segmento) {
      // Segmento di gestione trovato, elimino lui e i segmenti successivi
      this._riscaFA.rimuoviSegmento(segmento);
    }
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: pratiche.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  movingToRAPratiche(tabs: IRiscaTabChanges): boolean {
    // Definisco la costante con la tab specifica da controllare
    const p = RiscaIstanzeRicercaAvanzata.pratiche;
    // Lancio la funzione di verifica
    return this._riscaUtilities.movingIntoTab(tabs, p);
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: stati-debitori.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  movingToRAStatiDebitori(tabs: IRiscaTabChanges): boolean {
    // Definisco la costante con la tab specifica da controllare
    const sd = RiscaIstanzeRicercaAvanzata.statiDebitori;
    // Lancio la funzione di verifica
    return this._riscaUtilities.movingIntoTab(tabs, sd);
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

    // Ottengo i dati per la creazione dell'oggetto della ricerca
    const { datiTecnici, pratica, istanze, provvedimenti, paginazione } =
      this.ricercaPA;

    // Unisco le informazioni degli oggetti che ho ottenuto per avere una request con dati completi
    let search: RiscossioneSearchV2Vo;
    search = this.creaOggettoRiscossioneSearch(
      datiTecnici,
      pratica,
      istanze,
      provvedimenti
    );

    // Richiamo il servizio per la generazione del report
    this._report.createReportRicercaAvanzata(search).subscribe({
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
   * Funzione che unisce i dati della pratica, i dati tecnici, le istanze e i provvedimenti per creare un unico oggetto utilizzato per la ricerca.
   * @param datiTecnici string che definisce i dati tecnici per la ricerca. Essendo dinamici, sarà un oggetto stringhizzato.
   * @param pratica IRicercaPraticaAvanzataForm con i campi di ricerca generati dal form per la pratica.
   * @param istanze IRicercaIstanzaVo[] che definisce la lista di oggetti per la ricerca tramite istanze.
   * @param provvedimenti IRicercaProvvedimentoVo[] che definisce la lista di oggetti per la ricerca tramite provvedimenti.
   * @returns RiscossioneSearchV2Vo con tutt le informazioni per effettuare la ricerca delle pratiche.
   */
  private creaOggettoRiscossioneSearch(
    datiTecnici: string,
    pratica: IRicercaPraticaAvanzataForm,
    istanze: IRicercaIstanzaVo[] = [],
    provvedimenti: IRicercaProvvedimentoVo[]
  ): RiscossioneSearchV2Vo {
    // Creo l'oggetto con i dati.
    const filtri = new RiscossioneSearchV2Vo();
    // Utilizzo la funzione di set appositamente per aggiornare i dati
    filtri.datiTecnici = datiTecnici;
    filtri.istanze = istanze;
    filtri.provvedimenti = provvedimenti;
    filtri.setDataFromRAPForm(pratica);

    // Ritorno l'oggetto
    return filtri;
  }

  /**
   * Funzione di comodo per il refresh delle select.
   */
  private refreshSelect() {
    // Emetto l'evento custom di refresh delle select
    this._riscaSelect.refreshDOM();
  }

  /**
   * Funzione che, in base alla tab aperta, definisce l'oggetto per la modalità di ricerca avanzata.
   */
  private setModalitaRicerca() {
    // Verifico se la tab aperta è quella delle pratiche
    if (this.isNavPratiche) {
      // Pratiche, definisco l'oggetto
      this.modalitaRicerca = this.RRAP_C.ricercaPratiche;
      // #
    } else if (this.isNavStatiDebitori) {
      // Stati debitori, definisco l'oggetto
      this.modalitaRicerca = this.RRAP_C.ricercaStatiDebitori;
      // #
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che restitusce un oggetto IJourneySnapshot per generare una snapshot del componente.
   */
  get snapshotConfigs(): IJourneySnapshot {
    return {
      source: this,
      mapping: this.RAP_C.SNAPSHOT_CONFIG.mapping,
      saveFunc: this.RAP_C.SNAPSHOT_CONFIG.saveFunc,
    };
  }

  /**
   * Getter di comodo per il check della tabella della ricerca pratiche.
   * @returns boolean con il risultato del check.
   */
  get checkRAPraticheTable(): boolean {
    // Verifico che esista l'oggetto e abbia dati
    return this.raPraticheTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che ritorna un flag che indica se la nav attiva è: pratiche.
   * @returns boolean con il risultato del check.
   */
  get isNavPratiche(): boolean {
    // Recupero la nav attualmente aperta
    const nav: RiscaIstanzeRicercaAvanzata = this.istanzaRicercaAvanzata;
    // Verifico e ritorno la nav
    return nav === RiscaIstanzeRicercaAvanzata.pratiche;
  }

  /**
   * Getter di comodo che ritorna un flag che indica se la nav attiva è: stati debitori.
   * @returns boolean con il risultato del check.
   */
  get isNavStatiDebitori(): boolean {
    // Recupero la nav attualmente aperta
    const nav: RiscaIstanzeRicercaAvanzata = this.istanzaRicercaAvanzata;
    // Verifico e ritorno la nav
    return nav === RiscaIstanzeRicercaAvanzata.statiDebitori;
  }

  /**
   * Getter di comodo che definisce il claimant per la ricerca avanzata in base al tab aperto.
   * @returns AppClaimants con l'identificativo del chiamante.
   */
  get stepClaimant(): AppClaimants {
    // Recupero i check sulla nav, solo uno sarà true
    const isNavPratiche = this.isNavPratiche;
    const isNavStatiDebitori = this.isNavStatiDebitori;

    // Verifico e ritorno il claimant
    if (isNavPratiche) {
      // Nav pratiche attiva
      return AppClaimants.ricercaAvanzataPratiche;
    }
    if (isNavStatiDebitori) {
      // Nav stati debitori
      return AppClaimants.ricercaAvanzataStatiDebitori;
    }

    // Nessuna configurazione trovata
    return undefined;
  }
}
