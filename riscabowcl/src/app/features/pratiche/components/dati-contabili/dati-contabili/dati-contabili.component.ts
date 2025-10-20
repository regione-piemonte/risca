import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgbNav } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/index';
import { LoggerService } from 'src/app/core/services/logger.service';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from 'src/app/shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { PraticaEDatiTecnici } from '../../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { FASegmento } from '../../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { FALivello } from '../../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaFiloAriannaService } from '../../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  IRiscaTabChanges,
  RiscaDatiContabili,
  RiscaIstanzePratica,
  RiscaServerError,
} from '../../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../../shared/utilities/enums/risca-notify-codes.enums';
import { DatiContabiliNavClass } from '../../../class/navs/dati-contabili.nav.class';
import { DatiContabiliConsts } from '../../../consts/dati-contabili/dati-contabili.consts';
import { DatiContabiliUtilityService } from '../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { DatiContabiliService } from '../../../service/dati-contabili/dati-contabili/dati-contabili.service';
import { InserisciPraticaService } from '../../../service/inserisci-pratica/inserisci-pratica.service';
import { RiscaFormParentComponent } from './../../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';

@Component({
  selector: 'dati-contabili',
  templateUrl: './dati-contabili.component.html',
  styleUrls: ['./dati-contabili.component.scss'],
})
export class DatiContabiliComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy
{
  /** Costante per le informazioni del componente. */
  DC_C = DatiContabiliConsts;
  /** Costante che definisce le possibili azioni sui componenti dell'applicazione. */
  APP_ACTIONS = AppActions;

  /** PraticaEDatiTecnici che identifica i dati della riscossione aperta in modifica. */
  @Input() praticaEDT: PraticaEDatiTecnici;

  /** ViewChild collegato alla navigation bar. */
  @ViewChild('datiContabiliNav') datiContabiliNav: NgbNav;

  /** String che definisce il target dell'istanza della partica. */
  sezioneDatiContabili: RiscaDatiContabili;
  /** DatiContabiliNavClass per gestire la navigazione tra le pagine (l'header sarà nascosto). */
  datiContabiliNavConfigs: DatiContabiliNavClass;

  /** Boolean che identifica come flag se come sezione della riscossione è attualmente attiva quella dei dati contabili. */
  isDatiContabiliActive = false;

  /** Subscription registrato per il cambio dati della pratica. */
  onModalitaChanged: Subscription;
  onPraticaChanged: Subscription;
  onISTabChanged: Subscription;
  /** Subscription registrato per la gestione degli errori. */
  onSectionError: Subscription;

  /** Subscription registrato per il cambio di pagina dei dati contabili. */
  onStatiDebitori: Subscription;
  onVersamenti: Subscription;
  onRimborsi: Subscription;
  onAccertamenti: Subscription;
  onStatoDebitorio: Subscription;
  onInserimentoStatoDebitorio: Subscription;
  onNap: Subscription;

  /** Subscription che permette la condivisione dell'evento di inserimento di un oggetto StatoDebitorioVo. */
  onSDInsSuccess: Subscription;
  /** Subscription che permette la condivisione dell'evento d'errore durante un inserimento di un oggetto StatoDebitorioVo. */
  onSDInsError: Subscription;
  /** Subscription che permette la condivisione dell'evento di annuallamento della funzionalità di inserimento di un oggetto StatoDebitorioVo. */
  onSDInsCancel: Subscription;

  /** Subscription che permette la condivisione dell'evento di modifica di un oggetto StatoDebitorioVo. */
  onSDModSuccess: Subscription;
  /** Subscription che permette la condivisione dell'evento d'errore durante una modifica di un oggetto StatoDebitorioVo. */
  onSDModError: Subscription;
  /** Subscription che permette la condivisione dell'evento di annuallamento della funzionalità di modifica di un oggetto StatoDebitorioVo. */
  onSDModCancel: Subscription;

  /**
   * Costruttore
   */
  constructor(
    logger: LoggerService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _datiContabili: DatiContabiliService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _inserisciPratica: InserisciPraticaService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages
    );
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
    // Lancio il setup della nav nascosta
    this.datiContabiliNavConfigs = new DatiContabiliNavClass();
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente(this.praticaEDT);
    // Lancio l'init dei listener del componente
    this.initListeners();
  }

  /**
   * Funzione di init per il componente.
   */
  private initComponente(praticaEDTDatiContabili: PraticaEDatiTecnici) {
    // Salvo nel servizio dedidcato il dato della pratica
    this.praticaEDTDatiContabili = praticaEDTDatiContabili;
  }

  /**
   * Funzione di init per i listener del componente, per rimanere in ascolto degli eventi per il cambio pagina dei dati contabili.
   */
  private initListeners() {
    // #############################################################
    // ############### EVENTI COLLEGATI ALLA PRATICA ###############
    // #############################################################
    this.onModalitaChanged =
      this._inserisciPratica.onModalitaChanges$.subscribe({
        next: (modalita: AppActions) => {
          // Aggiorno la modalità della pratica
          this.modalita = modalita;
        },
      });
    this.onPraticaChanged = this._inserisciPratica.onPraticaChanges$.subscribe({
      next: (praticaEDatiTecnici: PraticaEDatiTecnici) => {
        // Aggiorno l'id pratica per il componente
        this.praticaEDTDatiContabili = praticaEDatiTecnici;
        // #
      },
    });
    this.onISTabChanged = this._inserisciPratica.onISTabChanges$.subscribe({
      next: (tabs: IRiscaTabChanges) => {
        // Lancio la funzione di verifica ed eventualmente pulizia dei servizi
        this.isTabMovingAway(tabs);
        // Lancio la funzione di verifica per vedere se sto atterrando sulla pagina
        this.isTabMovingHere(tabs);
      },
    });

    // ####################################################################
    // ############### EVENTI COLLEGATI AI COMPONENTI FIGLI ###############
    // ####################################################################
    this.onSectionError = this._datiContabiliUtility.onSectionError$.subscribe({
      next: (error: RiscaServerError) => {
        // Richiamo la funzione di gestione degli errori
        this.onServiziError(error);
      },
    });

    // ####################################################################################
    // ############### EVENTI COLLEGATI ALLA NAVIGAZIONE DEI DATI CONTABILI ###############
    // ####################################################################################
    this.onStatiDebitori =
      this._datiContabiliUtility.onStatiDebitori$.subscribe({
        next: () => {
          // Effettuo una pulizia del filo d'arianna per gestire la nuova voce
          this.pulisciFAStatiDebitori();

          // Recupero la nav dedicata per la specifica pagina
          const tab = this.datiContabiliNavConfigs.statiDebitori.ngbNavItem;
          // Richiamo la funzione per il cambio di pagina
          this.cambiaSezioneDatiContabili(tab);
        },
      });

    this.onVersamenti = this._datiContabiliUtility.onVersamenti$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Effettuo una pulizia del filo d'arianna per gestire la nuova voce
        this.pulisciFAStatiDebitori();
        // Definisco il filo d'arianna specifico
        this.aggiungiSegmentoDC(this.pagamentiSD);

        // Recupero la nav dedicata per la specifica pagina
        const tab = this.datiContabiliNavConfigs.versamenti.ngbNavItem;
        // Richiamo la funzione per il cambio di pagina
        this.cambiaSezioneDatiContabili(tab, statoDebitorio);
      },
    });

    this.onRimborsi = this._datiContabiliUtility.onRimborsi$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Effettuo una pulizia del filo d'arianna per gestire la nuova voce
        this.pulisciFAStatiDebitori();

        // Recupero la nav dedicata per la specifica pagina
        const tab = this.datiContabiliNavConfigs.rimborsi.ngbNavItem;
        // Richiamo la funzione per il cambio di pagina
        this.cambiaSezioneDatiContabili(tab, statoDebitorio);
      },
    });

    this.onAccertamenti = this._datiContabiliUtility.onAccertamenti$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Effettuo una pulizia del filo d'arianna per gestire la nuova voce
        this.pulisciFAStatiDebitori();
        // Definisco il filo d'arianna specifico
        this.aggiungiSegmentoDC(this.accertamentiSD);

        // Recupero la nav dedicata per la specifica pagina
        const tab = this.datiContabiliNavConfigs.accertamenti.ngbNavItem;
        // Richiamo la funzione per il cambio di pagina
        this.cambiaSezioneDatiContabili(tab, statoDebitorio);
      },
    });

    this.onStatoDebitorio =
      this._datiContabiliUtility.onStatoDebitorio$.subscribe({
        next: (statoDebitorio: StatoDebitorioVo) => {
          // Effettuo una pulizia del filo d'arianna per gestire la nuova voce
          this.pulisciFAStatiDebitori();
          // Definisco il filo d'arianna specifico
          this.aggiungiSegmentoDC(this.dettaglioSD);

          // Recupero la nav dedicata per la specifica pagina
          const tab = this.datiContabiliNavConfigs.statoDebitorio.ngbNavItem;
          // Richiamo la funzione per il cambio di pagina
          this.cambiaSezioneDatiContabili(tab, statoDebitorio);
        },
      });

    this.onInserimentoStatoDebitorio =
      this._datiContabiliUtility.onInserimentoStatoDebitorio$.subscribe({
        next: () => {
          // Effettuo una pulizia del filo d'arianna per gestire la nuova voce
          this.pulisciFAStatiDebitori();
          // Definisco il filo d'arianna specifico
          this.aggiungiSegmentoDC(this.inserisciSD);

          // Recupero la nav dedicata per la specifica pagina
          const tab =
            this.datiContabiliNavConfigs.inserimentoStatoDebitorio.ngbNavItem;
          // Richiamo la funzione per il cambio di pagina
          this.cambiaSezioneDatiContabili(tab);
        },
      });

    this.onNap = this._datiContabiliUtility.onNap$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Effettuo una pulizia del filo d'arianna per gestire la nuova voce
        this.pulisciFAStatiDebitori();

        // Recupero la nav dedicata per la specifica pagina
        const tab = this.datiContabiliNavConfigs.nap.ngbNavItem;
        // Richiamo la funzione per il cambio di pagina
        this.cambiaSezioneDatiContabili(tab, statoDebitorio);
      },
    });

    // ####################################################################################
    // ############## EVENTI COLLEGATI AL SALVATAGGIO DEI DEI DATI CONTABILI ##############
    // ####################################################################################
    this.onSDInsSuccess = this._datiContabili.onSDInsSuccess$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Gestisco l'evento di success
        this.onSDSuccessResult(statoDebitorio);
      },
    });
    this.onSDInsError = this._datiContabili.onSDInsError$.subscribe({
      next: (error: RiscaServerError) => {
        // Gestisco l'errore anche nel caso sia multiplo
        this.onSDErrorResult(error);
      },
    });
    this.onSDInsCancel = this._datiContabili.onSDInsCancel$.subscribe({
      next: () => {
        // Gestisco l'evento di cancellazione
        this.onSDCancelResult();
      },
    });
    this.onSDModSuccess = this._datiContabili.onSDModSuccess$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Gestisco l'evento di success
        this.onSDSuccessResult(statoDebitorio);
      },
    });
    this.onSDModError = this._datiContabili.onSDModError$.subscribe({
      next: (error: RiscaServerError) => {
        // Gestisco l'errore anche nel caso sia multiplo
        this.onSDErrorResult(error);
      },
    });
    this.onSDModCancel = this._datiContabili.onSDModCancel$.subscribe({
      next: () => {
        // Gestisco l'evento di cancellazione
        this.onSDCancelResult();
      },
    });
  }

  /**
   * ##################################
   * FUNZIONI DI DESTROY DEL COMPONENTE
   * ##################################
   */

  ngOnDestroy() {
    // Alla distruzione del componente (cambio interamente sezione), resetto i dati del servizio
    this.resetDatiContabili();

    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      this.onModalitaChanged?.unsubscribe();
      this.onPraticaChanged?.unsubscribe();
      this.onISTabChanged?.unsubscribe();
      this.onSectionError?.unsubscribe();
      this.onVersamenti?.unsubscribe();
      this.onRimborsi?.unsubscribe();
      this.onAccertamenti?.unsubscribe();
      this.onStatoDebitorio?.unsubscribe();
      this.onInserimentoStatoDebitorio?.unsubscribe();
      this.onNap?.unsubscribe();
      this.onSDInsSuccess?.unsubscribe();
      this.onSDInsError?.unsubscribe();
      this.onSDInsCancel?.unsubscribe();
      this.onSDModSuccess?.unsubscribe();
      this.onSDModError?.unsubscribe();
      this.onSDModCancel?.unsubscribe();
    } catch (e) {}
  }

  /**
   * #######################
   * GESTIONE DELLE NAV TABS
   * #######################
   */

  /**
   * Funzione gestisce la navigazione dei dati contabili.
   * La navigazione è permessa a seconda della configurazione per le varie maschere, se la configurazione non permette la navigazione non verrà caricata la maschera.
   * @param sezione RiscaDatiContabili che definisce la nuova sezione.
   * @param statoDebitorio StatoDebitorioVo che definisce l'oggetto dello stato debitorio da passare alla pagina.
   */
  cambiaSezioneDatiContabili(
    sezione: RiscaDatiContabili,
    statoDebitorio?: StatoDebitorioVo
  ) {
    // Variabili di comodo
    const s = sezione;
    const sd = statoDebitorio;

    // Resetto l'alert della sezione
    this.resetAlertConfigs();
    // Resetto il dato all'interno del servizio condiviso
    this._datiContabiliUtility.resetStatoDebitorio();
    // Verifico se la navigazione è consentita
    const checkN = this._datiContabiliUtility.checkNavigazione(s, sd);

    // Verifico la navigazione richiesta
    if (checkN) {
      // Salvo i dati per la sezione e la navigo
      this.goToSezione(s, sd);
      // #
    } else {
      // Gestisco l'errore per il cambio di sezione
      this.onSezioneError();
    }
  }

  /**
   * Funzione che definisce le logiche per il salvataggio dei dati per le sezioni e ne esegue la navigazione.
   * @param sezione RiscaDatiContabili che definisce la nuova sezione.
   * @param statoDebitorio StatoDebitorioVo che definisce l'oggetto dello stato debitorio da passare alla pagina.
   */
  private goToSezione(
    sezione: RiscaDatiContabili,
    statoDebitorio?: StatoDebitorioVo
  ) {
    // Modifico il flag per la sezione
    this.sezioneDatiContabili = sezione;
    // Definisco lo stato debitorio all'interno del servizio
    this._datiContabiliUtility.statoDebitorio = statoDebitorio;
    // Aggiorno la visualizzazione della sezione per la nav
    this.datiContabiliNav.select(this.sezioneDatiContabili);
  }

  /**
   * Funzione di comodo che gestisce la visualizzazione dell'errore dovuto al blocco della navigazione verso una sezione dell'app.
   */
  private onSezioneError() {
    // Genero e visualizzo un errore (utilizzo un servizio di comodo anche se non è un errore server)
    const error = new RiscaServerError({
      error: { code: RiscaNotifyCodes.A022 },
    });
    // Visualizzo l'errore generato
    this.onServiziError(error);
  }

  /**
   * Funzione che verifica il cambio di tab della componente padre: inserisci-pratica.
   * Se avviene un cambio verso una nuova tab e i dati contabili erano attivi, allora pulisco i dati dentro il servizio.
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   */
  private isTabMovingAway(tabs: IRiscaTabChanges) {
    // Recupero il tab dei dati contabili
    const datiContabili = RiscaIstanzePratica.datiContabili;
    // Verifico se il tab sta cambiando verso un altro tab
    const isGoingAway = this._riscaUtilities.movingFromTab(tabs, datiContabili);

    // Controllo se ci stiamo quindi spostando dai dati contabili ad un'altra tab
    if (isGoingAway) {
      // Resetto le informazioni per i dati contabili
      this._datiContabiliUtility.resetDatiContabiliSD();
      // Imposto il flag di attivazione di questa sezione a false
      this.isDatiContabiliActive = false;
      // Resetto la tab dei dati contabili
      this.datiContabiliNav.select(undefined);
    }
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
      // Imposto il flag di attivazione di questa sezione a true
      this.isDatiContabiliActive = true;
      // Imposto la tab dei dati contibili sugli stati debitori
      this.datiContabiliNav.select(RiscaDatiContabili.statiDebitori);
    }
  }

  /**
   * Funzione di comodo che tenta di recuperare il segmento appena successivo a quello contenente il livello dati contabili.
   * Se esiste, verrà rimosso insieme a tutti i possibili figli.
   */
  private pulisciFAStatiDebitori() {
    // Recupero la posizione del segmento per il livello "dati contabili"
    const dtFA: FALivello = this._riscaFA.datiContabili;
    // Recupero l'indice posizionale del segmento successivo ai dati contabili
    const iSDC = this._riscaFA.indexSegmentoInFAByIdLivello(dtFA.id);
    // Tento di recuperare un possibile segmento SUCCESSIVO a quello dei dati contabili
    const iNextSDC = iSDC + 1;

    // Tento di recuperare il segmento dal filo d'arianna
    const segmentoNextDC: FASegmento = this._riscaFA.segmentoByIndex(iNextSDC);
    // Verifico se esiste effettivamente un segmento
    if (segmentoNextDC) {
      // Esiste il segmento, cerco di rimuoverlo dal filo d'arianna
      this._riscaFA.rimuoviSegmento(segmentoNextDC);
    }
  }

  /**
   * Funzione di comodo che imposta un nuovo segmento dentro il filo d'arianna.
   * Il livello deve essere passato per gestire, in maniera dedicata, la sezione da mostrare per il filo d'arianna.
   * @param livello FALivello con il livello d'aggiungere per il segmento.
   */
  private aggiungiSegmentoDC(livello: FALivello) {
    // Definisco il livello per il filo d'arianna
    const livelloFA: FALivello = livello;
    // Imposto il filo d'arianna
    this._riscaFA.aggiungiSegmentoByLivelli(livelloFA);
  }

  /**
   * #####################################################################
   * FUNZIONI RICHIAMATE PER GLI EVENTI DI MODIFICA DI UNO STATO DEBITORIO
   * #####################################################################
   */

  /**
   * Gestisce le operazioni da eseguire alla fine del successo nel salvataggio dello stato debitorio.
   * @param statoDebitorio StatoDebitorioVo salvato.
   */
  onSDSuccessResult(statoDebitorio: StatoDebitorioVo) {
    // Gestione del risultato dall'ascoltatore
  }

  /**
   * Gestisce le operazioni da eseguire in caso di errore nel salvataggio dello stato debitorio.
   */
  onSDErrorResult(error: RiscaServerError) {
    // Gestione del risultato dall'ascoltatore
  }

  /**
   * Gestisce le operazioni da eseguire alla fine della cancellazione nel salvataggio dello stato debitorio.
   */
  onSDCancelResult() {
    // Gestione del risultato dall'ascoltatore
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione che effettua il reset delle informazioni salvate all'interno del servizio dei dati contabili.
   */
  private resetDatiContabili() {
    // Pulisco le informazioni all'interno del servizio
    this._datiContabiliUtility.resetPratica();
    this._datiContabiliUtility.resetStatoDebitorio();
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Setter per l'oggetto della pratica all'interno del servizio.
   */
  set praticaEDTDatiContabili(praticaEDT: PraticaEDatiTecnici) {
    this.praticaEDT = praticaEDT;
    this._datiContabiliUtility.praticaEDT = praticaEDT;
  }

  /**
   * Getter per l'id pratica.
   */
  get idPratica() {
    // Recupero l'id pratica dal servizio
    return this._datiContabiliUtility.idPratica;
  }

  /**
   * Getter di comodo per la configurazione del livello per: inserisciSD.
   * @returns FALivello con i dati di configurazione.
   */
  get inserisciSD(): FALivello {
    // Recupero dal servizio il livello
    return this._riscaFA.nuovoStatoDeb;
  }

  /**
   * Getter di comodo per la configurazione del livello per: dettaglioSD.
   * @returns FALivello con i dati di configurazione.
   */
  get dettaglioSD(): FALivello {
    // Recupero dal servizio il livello
    return this._riscaFA.dettaglioStatoDeb;
  }

  /**
   * Getter di comodo per la configurazione del livello per: accertamentiSD.
   * @returns FALivello con i dati di configurazione.
   */
  get accertamentiSD(): FALivello {
    // Recupero dal servizio il livello
    return this._riscaFA.dettaglioAccertamenti;
  }

  /**
   * Getter di comodo per la configurazione del livello per: pagamentiSD.
   * @returns FALivello con i dati di configurazione.
   */
  get pagamentiSD(): FALivello {
    // Recupero dal servizio il livello
    return this._riscaFA.dettaglioPagamenti;
  }
}
