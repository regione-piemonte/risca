import { Component, OnInit, ViewChild } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { RicercaPaginataResponse } from 'src/app/core/classes/http-helper/http-helper.classes';
import { PraticaEDatiTecnici } from 'src/app/core/commons/vo/pratica-vo';
import { RiscossioneSearchResultVo } from 'src/app/core/commons/vo/riscossione-search-result-vo';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import {
  IJourneySnapshot,
  IStepNavigation,
} from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../../core/services/logger.service';
import { UserService } from '../../../../core/services/user.service';
import { NavigationHelperClass } from '../../../../shared/classes/navigation/navigation-helper.class';
import { HomeGruppiTable } from '../../../../shared/classes/risca-table/home/home-gruppi.table';
import { HomeSoggettiTable } from '../../../../shared/classes/risca-table/home/home-soggetti.table';
import { RiscaRicercaSemplicePraticheTable } from '../../../../shared/classes/risca-table/ricerca-semplice-pratiche/ricerca-semplice-pratiche.table';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaRicercaSemplicePraticheComponent } from '../../../../shared/components/risca/risca-ricerca-semplice-pratiche/risca-ricerca-semplice-pratiche.component';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { IRiscaTableACEvent } from '../../../../shared/components/risca/risca-table/utilities/risca-table.interfaces';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaLockPraticaService } from '../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { IRichiestaLockPraticaRes } from '../../../../shared/services/risca/risca-lock-pratica/utilities/risca-lock-pratica.interfaces';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AbilitaDAGruppi,
  AbilitaDASezioni,
  AppActions,
  AppRoutes,
  FlagRicercaCL,
  GruppoEComponenti,
  IPraticaRouteParams,
  IRicercaPraticaSempliceRes,
  IRiscaRadioData,
  IRiscaServerError,
  RicercaPraticaSemplice,
  RiscaAlertConfigs,
  RiscaAzioniPratica,
  RiscaInfoLevels,
  RiscaIstanzePratica,
  RiscaServerError,
  RiscaTablePagination,
  TRiscaAlertType,
} from '../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../shared/utilities/classes/errors-maps';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { AmbitoService } from '../../../ambito/services';
import { IIPConfigs } from '../../../pratiche/components/inserisci-pratica/utilities/inserisci-pratica.interfaces';
import { IPraticheCollegateRouteParams } from '../../../pratiche/components/pratiche-collegate/utilities/pratiche-collegate.utilities';
import { InserisciPraticaConsts } from '../../../pratiche/consts/inserisci-pratica/inserisci-pratica.consts';
import { PraticheCollegateConsts } from '../../../pratiche/consts/pratiche-collegate/pratiche-collegate.consts';
import { RicercaAvanzataPraticheConsts } from '../../../pratiche/consts/ricerca-avanzata-pratiche/ricerca-avanzata-pratiche.consts';
import { PraticaRouteKeys } from '../../../pratiche/enums/pratica/pratica.enums';
import { SoggettoDatiAnagraficiService } from '../../../pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { PraticheService } from '../../../pratiche/service/pratiche.service';
import { RicercaSemplicePraticheService } from '../../../pratiche/service/ricerca-semplice-pratiche/ricerca-semplice-pratiche.service';
import { IGruppoRouteParams } from '../../../soggetti/components/gruppo/utilities/gruppo.interfaces';
import { ISoggettoRouteParams } from '../../../soggetti/components/soggetto/utilities/soggetto.interfaces';
import { GruppoConsts } from '../../../soggetti/consts/gruppo/gruppo.consts';
import { SoggettoConsts } from '../../../soggetti/consts/soggetto/soggetto.consts';
import { HomeConsts } from '../../consts/home/home.consts';
import { CercaSGRes, HomeService } from '../../services/home/home.service';
import { HomeFieldsConfigClass } from './utilities/home.fields-configs';
import { IHomeRouteParams } from './utilities/home.interfaces';

/**
 * Interfaccia personalizzata che deifnisce la struttura dei dati di ricerca soggetti/gruppi per la form della home page.
 */
export interface HomeRicercaSoggettiGruppi {
  target: IRiscaRadioData;
  campoLibero: string;
}

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent extends NavigationHelperClass implements OnInit {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente i valori costanti per il componente home. */
  H_C = HomeConsts;
  /** Oggetto contenente i valori costanti per il componente soggetto. */
  S_C = SoggettoConsts;
  /** Oggetto contenente i valori costanti per il componente gruppo. */
  G_C = GruppoConsts;
  /** Oggetto contenente i valori costanti per il componente pratiche collegate. */
  PC_C = PraticheCollegateConsts;
  /** Oggetto contenente i valori costanti per il componente inserisci pratica. */
  RAP_C = RicercaAvanzataPraticheConsts;
  /** Oggetto contenente i valori costanti per il componente inserisci pratica. */
  IP_C = InserisciPraticaConsts;

  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;
  /** Classe costante contenente il mapping degli errori per i form. */
  EM = new RiscaErrorsMap();

  /** ViewChild per utilizzare le funzioni del componente associato. */
  @ViewChild('riscaRicercaSemplicePratiche')
  riscaRicercaSemplicePratiche: RiscaRicercaSemplicePraticheComponent;

  /** Copia locale dell'enum di gestione dei gruppi, per la sezione. */
  sezioniDA = AbilitaDASezioni;
  /** Copia locale dell'enum di gestione dei gruppi, per il tipo di abilitazione. */
  abilitaGruppi = AbilitaDAGruppi;

  /** FormGroup per la ricerca di soggetti o gruppi. */
  ricercaSGForm: FormGroup;
  /** Boolean che definisce lo stato di submit della form: ricercaSGForm. */
  ricercaSGSubmitted = false;

  /** HomeRicercaSoggettiGruppi contenente le informazioni del form ricercaSGForm. */
  ricercaSGData: HomeRicercaSoggettiGruppi;
  /** IRicercaPraticaSempliceReq utilizzata per la gestione della tabella delle pratiche. */
  ricercaPS: {
    ricerca: RicercaPraticaSemplice;
    paginazione: RiscaTablePagination;
  };
  /** Dati per la paginazione di soggetti e gruppi */
  ricercaSG: {
    // Soggetti
    paginazioneS: RiscaTablePagination;
    // Gruppi
    paginazioneG: RiscaTablePagination;
  };

  // TODO: @Ismaele => Gestione delle notifiche da sviluppare
  listaNotifiche = [];
  showNotifiche = false;

  /** Classe HomeFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: HomeFieldsConfigClass;

  /** HomeSoggettiGruppiTable utilizzata per la gestione della tabella istanze/provvedimenti. */
  ricercaSTable: HomeSoggettiTable;
  /** HomeSoggettiGruppiTable utilizzata per la gestione della tabella istanze/provvedimenti. */
  ricercaGTable: HomeGruppiTable;
  /** Oggetto con infomazioni per il rendering della tabella dei risultati delle ricerche. */
  tablePratiche = new RiscaRicercaSemplicePraticheTable();

  /** String contenente la testata per la ricerca soggetti gruppi. */
  testataSoggettiGruppi = '';
  /** String contenente la descrizione per la ricerca soggetti gruppi. */
  descrizioneSoggettiGruppi = '';

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertSGFConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();
  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente per la ricerca semplice. */
  alertRSConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _ambito: AmbitoService,
    private _formBuilder: FormBuilder,
    private _home: HomeService,
    private _logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _pratiche: PraticheService,
    private _riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _router: Router,
    private _ricercaSemplice: RicercaSemplicePraticheService,
    private _soggettoDA: SoggettoDatiAnagraficiService,
    private _user: UserService
  ) {
    // Chiamata al super
    super(navigationHelper, HomeConsts.NAVIGATION_CONFIG);
    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio il setup delle form
    this.initForms();
  }

  /**
   * Funzione di setup per le parti del componente.
   */
  private setupComponente() {
    // TODO: @Ismaele => Gestione delle notifiche da sviluppare
    this.mockNotifiche();
    // Setup delle input form del componente
    this.setupFormInputs();
    // Setup di alcune descrizioni
    this.setupTesti();
    // Setup delle logiche per il filo d'arianna
    this.setupFiloArianna();
    // Setup delle tabelle di soggetti e gruppi
    this.setupTabelle();
    // Effettuo il setup dei dati passati alla pagina
    this.setupRouteParams();
  }

  mockNotifiche() {
    this.listaNotifiche = [
      {
        titolo: 'Edilizia',
        testo:
          "L'istanza  Nr. di riferimento: 0100127200004169492020 è stata accettata",
        data: '09 ott 2019 14:23',
      },
      {
        titolo: 'Riscossione canoni',
        testo:
          "E' stata autorizzata la concessione N. XXXXX del 04/05/2021 Tipologia: Concessione Referente: Aldo Sabatini",
        data: '09 ott 2019 14:23',
      },
      {
        titolo: 'Notifica numero 3',
        testo:
          'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor',
        data: '09 ott 2019 14:23',
      },
    ];
  }

  /**
   * Funzione di setup delle inputs form del componente.
   */
  private setupFormInputs() {
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new HomeFieldsConfigClass(this._riscaFormBuilder);
  }

  /**
   * Funzione di setup per i testi di gestione ricerca soggetti/gruppi.
   * Questo setup va lanciato dopo la funzione: setupFormInputs(); poiché verrà modificato un oggetto al suo interno.
   */
  private setupTesti() {
    // Recupero il messaggio da visualizzare per ricerca soggetti/gruppi
    const { testata, descrizione, placeholder } =
      this._home.testiRicercaSoggettiGruppi();
    // Imposto i testi
    this.testataSoggettiGruppi = testata;
    this.descrizioneSoggettiGruppi = descrizione;

    // Verifico se esiste la configurazione per le input
    if (this.formInputs?.campoLiberoConfig?.data)
      this.formInputs.campoLiberoConfig.data.placeholder = placeholder;
  }

  /**
   * Funzione di setup per la gestione del filo d'arianna. In questo caso specifico, quando si accede alla HOME, bisogna resettare sempre il filo.
   */
  private setupFiloArianna() {
    // Resetto il filo d'arianna
    this._riscaFA.resetFiloArianna();
  }

  /**
   * Creo le istanze per le tabelle di soggetti e gruppi
   */
  private setupTabelle() {
    // Tabella soggetti
    this.ricercaSTable = new HomeSoggettiTable([], this._accessoElementiApp);
    // Tabella Gruppi
    this.ricercaGTable = new HomeGruppiTable([], this._accessoElementiApp);
    // Creo la paginazione della snapshot
    this.ricercaSG = this.ricercaSG ?? {
      paginazioneS: this.ricercaSTable.getPaginazione(),
      paginazioneG: this.ricercaGTable.getPaginazione(),
    };
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   */
  private initForms() {
    // Init della struttura della form ricerca soggetto/gruppo
    this.initRicercaSoggettoOGruppo();
    // Init dei campi per il form ricercaSGForm
    this.initRicercaSGFormField();
  }

  /**
   * Setup del form: nuovaIstanzaForm.
   */
  private initRicercaSoggettoOGruppo() {
    this.ricercaSGForm = this._formBuilder.group({
      target: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      campoLibero: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
    });
  }

  /**
   * Funzione che esegue i setup per i campi del form: ricercaSGForm.
   */
  initRicercaSGFormField() {
    // Lancio la funzione di gestione del campo target
    this.initTarget();
    // Lancio la funzione di init per la descrizione
    this.initCampoLibero();
  }

  /**
   * Funzione che inizializza il campo target.
   * Verranno effettuate le logiche di controllo sul campo, poiché, in caso i gruppi non fossero abilitati, di default deve essere sempre attivo il valore per i soggetti.
   */
  private initTarget() {
    // Recupero l'abilitazione ai gruppi
    const nonAbilitato = !this.isGruppoAbilitato;

    // Contenitore di supporto
    let radioSelected: IRiscaRadioData;

    // Definisco il discriminante per la ricerca verificando se esiste già un valore salvato
    let findValue: FlagRicercaCL;
    // Verifico se esiste un valore salvato
    if (this.ricercaSGData?.target) {
      // Recupero dal target presente in memoria il valore da cercare
      findValue = this.ricercaSGData.target.value;
      // #
    } else {
      // Definisco il default sulla base delle abilitazioni sul gruppo
      findValue = nonAbilitato
        ? FlagRicercaCL.soggetti
        : FlagRicercaCL.soggettiEGruppi;
    }

    // Recupero dalla lista dei target il solo soggetto
    radioSelected = this.formInputs.targetConfig.source.find(
      (t: IRiscaRadioData) => {
        // Ritorno il check sul value
        return t.value === findValue;
      }
    );

    // Imposto il valore di default per il campo 'target'
    this._riscaUtilities.setFormValue(
      this.ricercaSGForm,
      this.H_C.TARGET,
      radioSelected
    );
  }

  /**
   * Funzione che inizializza il campo 'campo libero'.
   */
  private initCampoLibero() {
    // Verifico se esiste un valore salvato
    if (this.ricercaSGData?.campoLibero) {
      // Recupero dal target presente in memoria il valore da cercare
      const { campoLibero } = this.ricercaSGData || {};

      // Imposto il valore di default per il campo target
      this._riscaUtilities.setFormValue(
        this.ricercaSGForm,
        this.H_C.CAMPO_LIBERO,
        campoLibero
      );
    }
  }

  /**
   * Funzione che verifica se tramite il servizio di Route sono stati passati parametri di configurazione.
   */
  private setupRouteParams() {
    // Recupero i parametri dallo state della route
    const state = this._navigationHelper.getRouterState(this._router);
    // Carico i dati della snapshot
    this.loadJSnapshot(this);
    // Attivo il setup dei dati di route
    this.setupSnapshotData(state);
  }

  /**
   * Funzione di supporto che verifica e carica i dati del componente in base ai route params e i dati della snapshot.
   * @param routeParams IHomeRouteParams contenente i dati recuperati dal servizio di router.
   */
  private setupSnapshotData(routeParams: IHomeRouteParams) {
    // Gestisco il reload del form dei soggetti/gruppi
    this.reloadSGSearch();
    // Gestisco il reload della ricerca delle pratiche
    this.reloadRicercaPS();
  }

  /**
   * Funzione impiegata per il caricamento dei dati dei soggetti/gruppi.
   */
  private reloadSGSearch() {
    // Aggiorno la paginazione nella snapshot
    this.ricercaSTable?.updatePaginazione(this.ricercaSG?.paginazioneS);
    this.ricercaGTable?.updatePaginazione(this.ricercaSG?.paginazioneG);
    // Verifico se è presente l'oggetto per la form soggetti/gruppi
    if (this.ricercaSGData) {
      // I dati esistono, rilancio la ricerca
      this.cercaSGCampoLibero(this.ricercaSGData);
    }
  }

  /**
   * Funzione impiegata per il caricamento dei dati delle pratiche.
   */
  private reloadRicercaPS() {
    // Verifico se è presente l'oggetto per la form ricerca pratiche
    if (this.ricercaPS) {
      // Ottengo i dati dalla snapshot
      const { ricerca, paginazione } = this.ricercaPS;
      // I dati esistono, recupero la paginazione
      this.tablePratiche.updatePaginazione(paginazione);
      // Rilancio la ricerca, settando la paginazione
      this.avviaRicercaSemplice(ricerca, paginazione);
    }
  }

  /**
   * #######################################
   * PULSANTI PER LA NAVIGAZIONE ALLE PAGINE
   * #######################################
   */

  /**
   * Funzione per la navigazione alla sezione: ricerca avanzata per le pratiche.
   */
  goToRicercaAvanzata() {
    // Variabile di comodo
    const route = AppRoutes.gestionePratiche;
    // Genero lo state da passare alla rotta
    const state: IPraticaRouteParams = {
      navTarget: RiscaAzioniPratica.ricercaAvanzata,
    };

    // Genero l'oggetto per il target del routing
    const target: IStepNavigation = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.RAP_C.NAVIGATION_CONFIG;
    target.stateTarget.pageTarget = RiscaAzioniPratica.ricercaAvanzata;

    // Definisco i livelli per il filo d'arianna
    const homeFA: FALivello = this._riscaFA.home;
    const ricercaAFA: FALivello = this._riscaFA.ricercaAvanzata;
    // Imposto il filo d'arianna
    this._riscaFA.resetFiloArianna();
    this._riscaFA.aggiungiSegmentoByLivelli(homeFA, ricercaAFA);

    // Navigo sulla rotta
    this.journeyNavigation(target);
  }

  /**
   * Funzione per la navigazione alla sezione: inserimento nuova pratica.
   */
  goToInserimentoPratica() {
    // Variabile di comodo
    const route = AppRoutes.gestionePratiche;
    // Genero lo state da passare alla rotta
    const state: IPraticaRouteParams = {
      navTarget: RiscaAzioniPratica.inserisciPratica,
    };

    // Genero l'oggetto per il target del routing
    const target: IStepNavigation = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.IP_C.NAVIGATION_CONFIG;

    // Definisco i livelli per il filo d'arianna
    const homeFA: FALivello = this._riscaFA.home;
    const nuovaPFA: FALivello = this._riscaFA.nuovaPratica;
    // Imposto il filo d'arianna
    this._riscaFA.resetFiloArianna();
    this._riscaFA.aggiungiSegmentoByLivelli(homeFA, nuovaPFA);

    // Navigo sulla rotta
    this.journeyNavigation(target);
  }

  /**
   * Funzione per la navigazione alla sezione: inserimento gruppo.
   */
  goToInserimentoGruppo() {
    // Variabile di comodo
    const route = AppRoutes.gruppo;
    // Genero l'oggetto per il target del routing
    const target: IStepNavigation = this.navigationConfigs(route);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.G_C.NAVIGATION_CONFIG;

    // Definisco i livelli per il filo d'arianna
    const homeFA: FALivello = this._riscaFA.home;
    const nuovoGFA: FALivello = this._riscaFA.nuovoGruppo;
    // Imposto il filo d'arianna
    this._riscaFA.resetFiloArianna();
    this._riscaFA.aggiungiSegmentoByLivelli(homeFA, nuovoGFA);

    // Navigo sulla rotta
    this.journeyNavigation(target);
  }

  /**
   * ##########################
   * GESTIONE DEL FORM PRATICHE
   * ##########################
   */

  /** Al click del pulsante Cerca della ricerca semplice, avvia la procedura di ricerca */
  ricercaSemplice() {
    // Richiamo il submit della form tramite referenza del componente
    this.riscaRicercaSemplicePratiche.onFormSubmit();
  }

  /** Al click del pulsante Annulla, cancella i risultati la form di ricerca semplice */
  annullaRicercaSemplice() {
    // Se Restto la paginazione
    const paginazioneDefault = this.tablePratiche.getDefaultPagination();
    this.tablePratiche.updatePaginazione(paginazioneDefault);
    this.ricercaPS.paginazione = paginazioneDefault;
    // Cancello i dati della snapshot
    this.ricercaPS = undefined;
    // Resetto la form della ricerca
    this.riscaRicercaSemplicePratiche.onFormReset();
    // Pulisco la tabella dei risultati
    this.tablePratiche.clear();
  }

  /**
   * Avvia la chiamata al servizio per la ricerca semplice
   * @param ricerca parametri di ricerca
   * @param paginazione RiscaTablePagination, opzionale. Se ci sono, i dati della paginazione vengono aggiornati con questi, altrimenti con quelli della tabella.
   */
  avviaRicercaSemplice(
    ricerca: RicercaPraticaSemplice,
    paginazione?: RiscaTablePagination
  ) {
    // Rimetto la paginazione di default se non c'è
    if (paginazione == null) {
      // Recupero la paginazione di default
      paginazione = this.tablePratiche.getDefaultPagination();
    }

    // Salvo localmente l'informazione dell'ultima ricerca richiesta
    this.ricercaPS = { ricerca, paginazione };
    // Lancio la ricerca
    this._ricercaSemplice
      .getRicercaPraticaSemplice(ricerca, paginazione)
      .subscribe({
        next: (
          response: RicercaPaginataResponse<IRicercaPraticaSempliceRes[]>
        ) => {
          // Aggiorno l'alert
          this.aggiornaAlertConfigs(this.alertRSConfigs);
          // Richiamo la funzione di gestione dei risultati della ricerca
          this.onRicercaSemplice(response);
          // #
        },
        error: (error: IRiscaServerError) => {
          // Gestisco il messaggio d'errore inaspettato
          this.onServiziError(error);
        },
      });
  }

  /**
   * Funzione che definisce la gestione dei dati a seguito della ricerca.
   * @param response RicercaPaginataResponse<IRicercaPraticaSempliceRes[]> con i dati ottenuti dalla ricerca.
   * @author Ismaele Bottelli
   * @date 25/11/2024
   * @notes C'è un'interazione particolare che avviene quando la ricerca semplice viene gestita. Per richiesta utente, se il risultato della ricerca è 1, bisogna aprire subito la pratica.
   *        Nei test però è emerso la seguente casistica: se l'utente cerca delle pratiche e la risposta è paginata, se l'utente accede all'ultima pagina e quest'ultima pagina ha solo 1 elemento, questa pratica verrà aperta in automatico.
   *        Al momento della segnalazione alla PM [Siliva Cordero], si è deciso di non intevenire immediatamente (nel contesto temporale, c'era un po' di fretta).
   *        Come soluzione, bisogna verificare le informazioni di paginazione e aprire la pratica solo se c'è un solo elemento, la paginazione riporta 1 elemento e il numero totale di elementi è 1.
   *        P.S.: riporto questo commento in questa data "futura", rispetto agli sviluppi, perché nei test per le attività mi sono accorto di questa cosa lasciata indietro nel tempo. Per chiunque mai dovrà affrontare questo bug, saprà come fare.
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
      const a = this.alertRSConfigs;
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
   * Funzione agganciata all'evento di dettaglio lanciato dalla tabella.
   * @param row RiscaTableDataConfig<RiscossioneSearchResultVo> contenente i dati della riga.
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
    // Definisco una variabile per gestire la richiesta
    let req: Observable<PraticaEDatiTecnici>;

    // Il lock della riscossione è attivo solo se il ruolo non è consultatore
    if (this._user.isCONSULTATORE) {
      // Non deve avvenire il lock della riscossione
      req = this._pratiche.getPraticaEDatiTecnici(idPratica);
      // #
    } else {
      // Devo effettuare il lock della riscossione
      req = this._riscaLockP.bloccaPratica(idPratica).pipe(
        tap((resLock: IRichiestaLockPraticaRes) => {
          // Richiamo il servizio di lock per la gestione del risultato
          this._riscaLockP.utenteLockaPratica(resLock);
          // #
        }),
        switchMap((resLock: IRichiestaLockPraticaRes) => {
          // Scarico i dati della pratica
          return this._pratiche.getPraticaEDatiTecnici(idPratica);
        })
      );
    }

    // Effettuo la chiamata per la gestione del flusso
    req.subscribe({
      next: (pedt: PraticaEDatiTecnici) => {
        // Richiamo la funzione per la gestione della response
        this.onDettaglioPratica(pedt);
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione errori dal server
        this.onServiziError(e);
        // #
      },
    });
  }

  /**
   * Funzione richiamata al momento dello scarico di una pratica e dei suoi dettagli.
   * @param pedt PraticaEDatiTecnici con le informazioni della pratica.
   */
  private onDettaglioPratica(pedt: PraticaEDatiTecnici) {
    // Definisco i livelli per il filo d'arianna
    const homeFA: FALivello = this._riscaFA.home;
    const ricercaSFA: FALivello = this._riscaFA.ricercaSemplice;
    const dettPraticaFA: FALivello = this._riscaFA.dettaglioPratica;
    // Imposto il filo d'arianna
    this._riscaFA.resetFiloArianna();
    this._riscaFA.aggiungiSegmentoByLivelli(homeFA, ricercaSFA, dettPraticaFA);

    // Definisco lo state da passare alla pratica
    const state: IPraticaRouteParams = {
      navTarget: RiscaAzioniPratica.inserisciPratica,
      modalita: AppActions.modifica,
    };

    // Creo l'oggetto con tutti i parametri per il setup dell'inserimento pratica
    const praticaInserimento: IIPConfigs = {
      navTarget: RiscaIstanzePratica.generaliAmministrativi,
      praticaEDatiTecnici: pedt,
    };
    // Definisco i dati specifici per l'inserimento
    state[PraticaRouteKeys.inserimento] = praticaInserimento;

    // Variabile di comodo
    const route = AppRoutes.gestionePratiche;
    // Genero l'oggetto per il target del routing
    const target: IStepNavigation = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.IP_C.NAVIGATION_CONFIG;
    target.stateTarget.navTarget = RiscaAzioniPratica.ricercaSemplice;
    target.stateTarget.pageTarget = RiscaAzioniPratica.inserisciPratica;

    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * #################################
   * GESTIONE DEL FORM SOGGETTI GRUPPI
   * #################################
   */

  /**
   * Avvia la ricerca resettando la paginazione. Viene usata quando l'utente preme il pulsante di ricerca.
   */
  ricercaSGFormSubmitReset() {
    // Cancello i dati dalle tabelle per ripristinare la paginazione
    this.ricercaGTable.clear();
    this.ricercaSTable.clear();
    // Rimetto la paginazione di default
    this.ricercaSG.paginazioneS = this.ricercaSTable.getDefaultPagination();
    this.ricercaSG.paginazioneG = this.ricercaGTable.getDefaultPagination();
    this.ricercaSTable?.updatePaginazione(this.ricercaSG.paginazioneS);
    this.ricercaGTable?.updatePaginazione(this.ricercaSG.paginazioneG);
    this.ricercaSGFormSubmit();
  }

  /**
   * Funzione agganciata al submit della form ricercaSGForm.
   */
  ricercaSGFormSubmit() {
    // Attivo il flag di submit
    this.ricercaSGSubmitted = true;
    // Resetto i messaggi per l'alert
    this.aggiornaAlertConfigs(this.alertSGFConfigs);

    // Verifico il form
    if (this.ricercaSGForm.valid) {
      // Recupero i dati del form
      const ricercaSGData = this.ricercaSGForm.getRawValue();
      // Lancio la funzione di ricerca soggetto e dati affini
      this.cercaSGCampoLibero(ricercaSGData);
    }
  }

  /**
   * Funzione predisposta alla ricerca di soggetti e gruppi, usando un campo libero e un target per il tipo di ricerca.
   * @param ricercaSGData HomeRicercaSoggettiGruppi contenente i dati per la ricerca.
   */
  private cercaSGCampoLibero(ricercaSGData: HomeRicercaSoggettiGruppi) {
    // Recupero i dati dalla form
    this.ricercaSGData = ricercaSGData;
    // Recupero la paginazione
    this.ricercaSG.paginazioneS = this.ricercaSTable?.getPaginazione();
    this.ricercaSG.paginazioneG = this.ricercaGTable?.getPaginazione();

    // Richiamo la ricerca dei dati
    this._home
      .cercaSGCampoLibero(
        this.idAmbito,
        ricercaSGData,
        this.ricercaSG.paginazioneS,
        this.ricercaSG.paginazioneG
      )
      .subscribe({
        next: (r: CercaSGRes) => {
          // Recupero i dati dalla response
          const { soggetti, gruppiEComponenti } = r;
          // Creo le tabelle con le informazioni di soggetto e gruppi
          this.gestisciTabelleSG(soggetti.sources, gruppiEComponenti.sources);
          // Gestisco la paginazione
          this.gestisciPaginazioneTabelleSG(
            soggetti.paging,
            gruppiEComponenti.paging
          );
          // Resetto il flag di submit
          this.ricercaSGSubmitted = false;
        },
        error: (e: RiscaServerError) => {
          // Gestisco gli errori
          let errors = this.onServiziError(e);
          // Variabili di comodo
          const a = this.alertSGFConfigs;
          const d = RiscaInfoLevels.danger;
          // Aggiorno il prompt
          this.aggiornaAlertConfigs(a, errors, d);
        },
      });
  }

  /**
   * Gestisce la generazione delle tabelle dati e delle strutture connesse alla ricerca soggetti/gruppi.
   * @param soggetti Array di SoggettoVo contenente le informazioni per la generazione della tabella soggetti.
   * @param gruppi Array di GruppoEComponenti contenente le informazioni per la generazione della tabella gruppi.
   */
  private gestisciTabelleSG(
    soggetti: SoggettoVo[],
    gruppi: GruppoEComponenti[]
  ) {
    // Creo o aggiorno le tabelle con le informazioni dei soggetti
    this.ricercaSTable.setElements(soggetti);
    // Creo o aggiorno le tabelle con le informazioni dei gruppi
    this.ricercaGTable.setElements(gruppi);

    // Variabili di comodo
    const existTS = this.dataRicercaSTable;
    const existTG = this.dataRicercaGTable;

    // Verifico la condizione sui soggetti
    if (!existTS && !existTG) {
      // Definisco un array di messaggi per l'alert
      let alertMsg = [];

      // Definisco il codice del messaggio
      const code = RiscaNotifyCodes.I001;
      // Recupero il messaggio per gruppi non trovati
      const gruppiMsg = this._riscaMessages.getMessage(code);

      // Verifico se verrà visualizzato il pulsante di aggiungi nuovo gruppo
      if (this.nuovoGruppoBtn && this.isGestioneAbilitata) {
        // Definisco un messaggio extra
        alertMsg.push(`E' possibile creare un Nuovo Gruppo.`);
      }
      // Unisco i messaggi d'errore
      alertMsg.unshift(gruppiMsg);

      // Variabili di comodo
      const a = this.alertSGFConfigs;
      const w = RiscaInfoLevels.warning;
      // Aggiorno l'alert
      this.aggiornaAlertConfigs(a, alertMsg, w);
    }
  }

  /**
   * Gestisce la paginazione delle tabelle dati e delle strutture connesse alla ricerca soggetti/gruppi.
   * @param pagingSoggetti RiscaTablePagination con le informazioni sulla paginazione dei soggetti.
   * @param pagingGruppi RiscaTablePagination con le informazioni sulla paginazione dei gruppi.
   */
  gestisciPaginazioneTabelleSG(
    pagingSoggetti: RiscaTablePagination,
    pagingGruppi: RiscaTablePagination
  ) {
    // Aggiorno le configurazioni locali della paginazione sul totale degli elementi
    if (this.ricercaSG) {
      this.ricercaSG.paginazioneS.total = pagingSoggetti.total;
      this.ricercaSG.paginazioneS.currentPage = pagingSoggetti.currentPage;
      this.ricercaSG.paginazioneG.total = pagingGruppi.total;
      this.ricercaSG.paginazioneG.currentPage = pagingGruppi.currentPage;
    }
    // Paginazione dei soggetti
    this.ricercaSTable.updatePaginazioneAfterSearch(pagingSoggetti);
    // paginazione dei gruppi
    this.ricercaGTable.updatePaginazioneAfterSearch(pagingGruppi);
  }

  /**
   * Reset delle tabelle soggetti gruppi.
   */
  private resetTabelleSG() {
    this.ricercaSTable.clear();
    this.ricercaGTable.clear();
  }

  /**
   * Funzione collegata al template per il reset della form di ricercaSG.
   * @param resetAlert boolean che permette di resettare anche l'alert del form.
   */
  resetSGForm(resetAlert?: boolean) {
    // Se non ho la paginazione, carico quella di default
    const paginazioneDefaultG = this.ricercaGTable?.getDefaultPagination();
    const paginazioneDefaultS = this.ricercaSTable?.getDefaultPagination();
    this.ricercaGTable.updatePaginazione(paginazioneDefaultG);
    this.ricercaSTable.updatePaginazione(paginazioneDefaultS);
    if (this.ricercaSG) {
      this.ricercaSG.paginazioneG = paginazioneDefaultG;
      this.ricercaSG.paginazioneS = paginazioneDefaultS;
    }
    // Cancello i dati della snapshot
    this.ricercaSGData = undefined;
    // Resetto il flag di submitted
    this.ricercaSGSubmitted = false;
    // Resetto il form
    this.ricercaSGForm.reset();
    // Inizializzo i campi
    this.initRicercaSGFormField();
    // Resetto le tabelle
    this.resetTabelleSG();

    // Resetto l'alert
    if (resetAlert) {
      this.aggiornaAlertConfigs(this.alertSGFConfigs);
    }
  }

  /**
   * ###################################
   * GESTIONE DEGLI EVENTI DELLE TABELLE
   * ###################################
   */

  /**
   * Funzione agganciata all'evento di dettaglio lanciato dalla tabella.
   * @param dettaglio RiscaTableDataConfig<SoggettoVo> contenente i dati della riga.
   */
  dettaglioSTable(dettaglio: RiscaTableDataConfig<SoggettoVo>) {
    // Verifico l'input
    if (!dettaglio) {
      // Blocco le logiche
      return;
    }

    // Recupero il dato originale
    const soggetto: SoggettoVo = dettaglio.original;
    // Definisco lo state per la route per soggetto
    const state: ISoggettoRouteParams = {
      soggetto: soggetto,
      modalita: AppActions.modifica,
    };

    // Variabile di comodo
    const route = AppRoutes.soggetto;
    // Genero l'oggetto per il target del routing
    const target: IStepNavigation = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.S_C.NAVIGATION_CONFIG;

    // Definisco i livelli per il filo d'arianna
    const homeFA: FALivello = this._riscaFA.home;
    const ricercaSFA: FALivello = this._riscaFA.ricercaSoggetto;
    const dettGenericoFA: FALivello = this._riscaFA.dettaglioGenerico;
    // Imposto il filo d'arianna
    this._riscaFA.resetFiloArianna();
    this._riscaFA.aggiungiSegmentoByLivelli(homeFA, ricercaSFA, dettGenericoFA);

    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * Funzione agganciata all'evento di dettaglio lanciato dalla tabella: gruppi.
   * @param dettaglio RiscaTableDataConfig<GruppoEComponenti> contenente i dati della riga.
   */
  dettaglioGTable(dettaglio: RiscaTableDataConfig<GruppoEComponenti>) {
    // Verifico l'input
    if (!dettaglio) {
      return;
    }

    // Recupero il dato originale
    const gec: GruppoEComponenti = dettaglio.original;
    // Recupero il dato del gruppo
    const { gruppo } = gec;
    // Definisco lo state per la route per gruppo
    const state: IGruppoRouteParams = {
      gruppo: gruppo,
      modalita: AppActions.modifica,
    };

    // Genero l'oggetto per il target del routing
    const target = this.navigationConfigs(AppRoutes.gruppo, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.G_C.NAVIGATION_CONFIG;

    // Definisco i livelli per il filo d'arianna
    const homeFA: FALivello = this._riscaFA.home;
    const ricercaGFA: FALivello = this._riscaFA.ricercaGruppo;
    const dettGenericoFA: FALivello = this._riscaFA.dettaglioGenerico;
    // Imposto il filo d'arianna
    this._riscaFA.resetFiloArianna();
    this._riscaFA.aggiungiSegmentoByLivelli(homeFA, ricercaGFA, dettGenericoFA);

    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * Gestione delle custom action relative alla tabella: soggetti;
   * Verrà gestita la tipologia di custom action e attivata una logica specifica.
   * @param customAction IRiscaTableACEvent<any> contenente le informazioni sulla custom action triggherata.
   */
  customSTable(customAction: IRiscaTableACEvent<any>) {
    switch (customAction.action.action) {
      case this.H_C.TAB_SOG_PRATICHE_COLLEGATE: {
        // Richiamo la funzione per la gestione delle pratiche collegate tramite soggetto
        this.soggettiTablePraticheCollegate(customAction);
        break;
      }
      case this.H_C.TAB_SOG_NUOVA_PRATICA: {
        // Richiamo la funzione di gestione per l'inserimento della pratica
        this.soggettiTableNuovaPratica(customAction);
        break;
      }
      default: {
        // Variabili di comodo
        const m = 'customSTable';
        const data = {
          warning: 'Nessuna gestione per la custom action.',
          customAction,
        };
        // Loggo un warning, nient'altro richiesto
        this._logger.warning(m, data);
      }
    }
  }

  /**
   * Gestisce l'azione in customSTable per pratiche collegate.
   * @param customAction IRiscaTableACEvent<any> contenente le informazioni della riga che ha emesso l'evento custom.
   */
  private soggettiTablePraticheCollegate(
    customAction: IRiscaTableACEvent<any>
  ) {
    // Recupero i dati anagrafici del soggetto per la riga
    const datiAnagrafici = customAction.row.original;
    // Definisco i parametri da passare alla pagina
    const state: IPraticheCollegateRouteParams = {
      modalita: AppActions.modifica,
      soggetto: datiAnagrafici,
    };

    // Variabile di comodo
    const route = AppRoutes.praticheCollegate;
    // Genero l'oggetto per il target del routing
    const target = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.PC_C.NAVIGATION_CONFIG;

    // Definisco i livelli per il filo d'arianna
    const homeFA: FALivello = this._riscaFA.home;
    const ricercaSoggFA: FALivello = this._riscaFA.ricercaSoggetto;
    const pratCollFA: FALivello = this._riscaFA.praticheCollegate;
    // Imposto il filo d'arianna
    this._riscaFA.resetFiloArianna();
    this._riscaFA.aggiungiSegmentoByLivelli(homeFA, ricercaSoggFA, pratCollFA);

    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * Funzione che gestisce l'inserimento di una nuova pratica partendo dalla tabella dei soggetti.
   * @param customAction IRiscaTableACEvent<any> contenente i dati emessi dall'evento della tabella soggetti.
   */
  private soggettiTableNuovaPratica(customAction: IRiscaTableACEvent<any>) {
    // Definisco i parametri per la pagina delle pratiche
    const state: IPraticaRouteParams = {
      navTarget: RiscaAzioniPratica.inserisciPratica,
      modalita: AppActions.inserimento,
    };
    // Creo l'oggetto con tutti i parametri per il setup dell'inserimento pratica
    const soggettoInserimento: IIPConfigs = {
      navTarget: RiscaIstanzePratica.datiAnagrafici,
      soggettoInsert: customAction.row.original,
    };
    // Definisco una proprietà specifica come configurazione dell'inserisci pratica
    state[PraticaRouteKeys.inserimento] = soggettoInserimento;

    // Variabile di comodo
    const route = AppRoutes.gestionePratiche;
    // Genero l'oggetto per il target del routing
    const target = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.IP_C.NAVIGATION_CONFIG;

    // Definisco i livelli per il filo d'arianna
    const homeFA: FALivello = this._riscaFA.home;
    const ricercaSoggFA: FALivello = this._riscaFA.ricercaSoggetto;
    const nuovaPFA: FALivello = this._riscaFA.nuovaPratica;
    // Imposto il filo d'arianna
    this._riscaFA.resetFiloArianna();
    this._riscaFA.aggiungiSegmentoByLivelli(homeFA, ricercaSoggFA, nuovaPFA);

    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * Gestione delle custom action relative alla tabella: gruppi;
   * Verrà gestita la tipologia di custom action e attivata una logica specifica.
   * @param customAction IRiscaTableACEvent<GruppoEComponenti> contenente le informazioni sulla custom action triggherata.
   */
  customGTable(customAction: IRiscaTableACEvent<GruppoEComponenti>) {
    switch (customAction.action.action) {
      case this.H_C.TAB_GRP_PRATICHE_COLLEGATE: {
        // Richiamo la funzione per la gestione delle pratiche collegate tramite gruppo
        this.gruppiTablePraticheCollegate(customAction);
        break;
      }
      case this.H_C.TAB_GRP_NUOVA_PRATICA: {
        // Richiamo la funzione di gestione per l'inserimento della pratica partendo da un gruppo
        this.gruppiTableNuovaPratica(customAction);
        break;
      }
      default: {
        // Variabili di comodo
        const m = 'customGTable';
        const data = {
          warning: 'Nessuna gestione per la custom action.',
          customAction,
        };
        // Loggo un warning, nient'altro richiesto
        this._logger.warning(m, data);
      }
    }
  }

  /**
   * Gestisce l'azione in customGTable per pratiche collegate.
   * @param customAction IRiscaTableACEvent<GruppoEComponenti> contenente le informazioni della riga che ha emesso l'evento custom.
   */
  private gruppiTablePraticheCollegate(
    customAction: IRiscaTableACEvent<GruppoEComponenti>
  ) {
    // Recupero i dati dalla riga
    const gec: GruppoEComponenti = customAction?.row?.original;
    // Recupero il gruppo dall'oggetto
    const { gruppo } = gec || {};

    // Definisco i parametri da passare alla pagina
    const state: IPraticheCollegateRouteParams = {
      modalita: AppActions.modifica,
      gruppo: gruppo,
    };

    // Variabile di comodo
    const route = AppRoutes.praticheCollegate;
    // Genero l'oggetto per il target del routing
    const target = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.PC_C.NAVIGATION_CONFIG;

    // Definisco i livelli per il filo d'arianna
    const homeFA: FALivello = this._riscaFA.home;
    const ricercaGrupFA: FALivello = this._riscaFA.ricercaGruppo;
    const pratCollFA: FALivello = this._riscaFA.praticheCollegate;
    // Imposto il filo d'arianna
    this._riscaFA.resetFiloArianna();
    this._riscaFA.aggiungiSegmentoByLivelli(homeFA, ricercaGrupFA, pratCollFA);

    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * Funzione che gestisce l'inserimento di una nuova pratica partendo dalla tabella dei gruppi.
   * @param customAction IRiscaTableACEvent<GruppoEComponenti> contenente i dati emessi dall'evento della tabella gruppi.
   */
  private gruppiTableNuovaPratica(
    customAction: IRiscaTableACEvent<GruppoEComponenti>
  ) {
    // Definisco i parametri per la pagina delle pratiche
    const state: IPraticaRouteParams = {
      navTarget: RiscaAzioniPratica.inserisciPratica,
      modalita: AppActions.inserimento,
    };

    // Recupero i dati dalla custom action
    const gec: GruppoEComponenti = customAction?.row?.original;
    // Recupero il gruppo
    const { gruppo } = gec || {};
    // Estraggo il capogruppo
    const capogruppo = this._soggettoDA.getCapogruppoFromGruppoEComponenti(gec);

    // Creo l'oggetto con tutti i parametri per il setup dell'inserimento pratica
    const gruppoInserimento: IIPConfigs = {
      navTarget: RiscaIstanzePratica.datiAnagrafici,
      soggettoInsert: capogruppo,
      gruppoInsert: gruppo,
    };
    // Definisco una proprietà specifica come configurazione dell'inserisci pratica
    state[PraticaRouteKeys.inserimento] = gruppoInserimento;

    // Variabile di comodo
    const route = AppRoutes.gestionePratiche;
    // Genero l'oggetto per il target del routing
    const target = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.IP_C.NAVIGATION_CONFIG;

    // Definisco i livelli per il filo d'arianna
    const homeFA: FALivello = this._riscaFA.home;
    const ricercaGrupFA: FALivello = this._riscaFA.ricercaGruppo;
    const nuovaPFA: FALivello = this._riscaFA.nuovaPratica;
    // Imposto il filo d'arianna
    this._riscaFA.resetFiloArianna();
    this._riscaFA.aggiungiSegmentoByLivelli(homeFA, ricercaGrupFA, nuovaPFA);

    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * ################################
   * GESTIONE DEGLI ALERT PER LE FORM
   * ################################
   */

  /**
   * Funzione di comodo che imposta il messaggio d'errore
   * @param error any che definisce il corpo dell'errore.
   * @param messageCode string che definisce il messaggio d'errore da visualizzare in testata. Default è 'E005'.
   * @param otherMessages Array di string contenente un array con altri messaggi di dettaglio per l'errore. Default è [].
   */
  private onServiziError(
    error: any,
    messageCode = RiscaNotifyCodes.E005,
    otherMessages: string[] = []
  ): string[] {
    // Definisco un array di messaggi d'errore
    return this._riscaAlert.messagesFromServerError(
      error,
      messageCode,
      otherMessages
    );
  }

  /**
   * Funzione che gestisce il prompt dei messaggi.
   * Se i parametri non sono definiti, verrà effettuato un reset.
   * @param c RiscaAlertConfigs d'aggiornare con le nuove informazioni.
   * @param messaggi Array di string contenente i messaggi da visualizzare.
   * @param tipo TRiscaAlertType che definisce la tipologia di alert da visualizzare.
   */
  private aggiornaAlertConfigs(
    c: RiscaAlertConfigs,
    messaggi?: string[],
    tipo?: TRiscaAlertType
  ) {
    // Aggiorno la configurazione
    c = this._riscaAlert.aggiornaAlertConfigs(c, messaggi, tipo);
  }

  /**
   * Questa funzione ricarica la ricerca semplice pratiche ma passa anche i dati della paginazione al BE,
   * così da cambiare pagina
   * @param $event RiscaTablePagination con i dati della paginazione
   */
  cambiaPaginaPratiche(event: RiscaTablePagination) {
    // Aggiorno la paginazione
    this.ricercaPS.paginazione = event;
    // Ricarico i dati
    const { ricerca, paginazione } = this.ricercaPS;
    // Avvio la ricerca
    this.avviaRicercaSemplice(ricerca, paginazione);
  }

  /**
   * Questa funzione ricarica la ricerca dei soggetti ma passa anche i dati della paginazione al BE,
   * così da cambiare pagina
   * @param $event RiscaTablePagination con i dati della paginazione
   */
  cambiaPaginaSoggetti(event: RiscaTablePagination) {
    // Aggiorno la snapshot
    this.ricercaSG.paginazioneS = event;
    // Aggiorno la paginazione corrente
    this.ricercaSTable.updatePaginazione(event);
    // Ricarico i dati
    this.ricercaSGFormSubmit();
  }

  /**
   * Questa funzione ricarica la ricerca dei gruppi ma passa anche i dati della paginazione al BE,
   * così da cambiare pagina
   * @param $event RiscaTablePagination con i dati della paginazione
   */
  cambiaPaginaGruppi(event: RiscaTablePagination) {
    // Aggiorno la snapshot
    this.ricercaSG.paginazioneG = event;
    // Aggiorno la paginazione corrente
    this.ricercaGTable.updatePaginazione(event);
    // Ricarico i dati
    this.ricercaSGFormSubmit();
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per verificare se è necessario visualizzare il pulsante di "vai a nuovo gruppo".
   * Le condizioni sono: il radio button contenga la selezione per il gruppo e che non ci siano stati risultati.
   */
  get nuovoGruppoBtn() {
    // Verifico che il dato del form ricercaSGData esista
    if (!this.ricercaSGData) {
      return false;
    }
    // Variabile di comodo
    const target = this.ricercaSGData?.target?.value;
    // Verifico se il radio selezionato prevede i gruppi
    const sEg = target === FlagRicercaCL.soggettiEGruppi;
    const g = target === FlagRicercaCL.gruppi;
    // Ritorno il flag sulla base del tipo di selezione
    return (sEg || g) && !this.dataRicercaSTable && !this.dataRicercaGTable;
  }

  /**
   * Getter per idAmbito.
   */
  get idAmbito() {
    // Ritorno l'id ambito dal servizio
    return this._user.idAmbito;
  }

  /**
   * Getter che verifica se la tabella ricercaSTable esiste e contiene dati.
   */
  get dataRicercaSTable() {
    return this.ricercaSTable?.source?.length > 0;
  }

  /**
   * Getter che verifica se la tabella ricercaGTable esiste e contiene dati.
   */
  get dataRicercaGTable() {
    return this.ricercaGTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che effettua le verifiche sulla configurazione dell'alert: alertSGFConfigs.
   */
  get alertSGFConfigsCheck() {
    // Ritorno il check tramite servizio
    return this._riscaAlert.alertConfigsCheck(this.alertSGFConfigs);
  }

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertRSConfigs);
  }

  /**
   * Getter di comodo che restitusce un oggetto IJourneySnapshot per generare una snapshot del componente.
   */
  get snapshotConfigs(): IJourneySnapshot {
    return {
      source: this,
      mapping: HomeConsts.SNAPSHOT_CONFIG.mapping,
      saveFunc: HomeConsts.SNAPSHOT_CONFIG.saveFunc,
    };
  }

  /**
   * Getter di comodo per il recupero della configurazione dei soggetti.
   */
  get isGestioneAbilitata() {
    // Recupero il flag per la gestione abilitata
    return this._ambito.isGestioneAbilitata;
  }

  /**
   * Getter di comodo per il recupero della configurazione dei gruppi.
   */
  get isGruppoAbilitato() {
    // Recupero il flag per la gestione gruppi
    return this._ambito.isGruppoAbilitato;
  }
}
