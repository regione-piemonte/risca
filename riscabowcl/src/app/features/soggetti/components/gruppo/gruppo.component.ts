import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { clone, cloneDeep, intersectionBy } from 'lodash';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IndirizziSpedizioneUtilityService } from 'src/app/features/pratiche/service/dati-anagrafici/indirizzi-spedizione-utility/indirizzi-spedizione-utility.service';
import { RiscaTableDataConfig } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.classes';
import { IRiscaTableACEvent } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.interfaces';
import { almenoUnComponenteEUnCapogruppoValidator } from 'src/app/shared/miscellaneous/forms-validators/gruppi/forms-validators.g';
import { RiscaSpinnerService } from 'src/app/shared/services/risca-spinner.service';
import { ComponenteGruppo } from '../../../../core/commons/vo/componente-gruppo-vo';
import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import {
  IJourneySnapshot,
  IJourneyStep,
  IJToStep,
} from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import {
  FCSDatiSoggettoTable,
  IFCSDatiSoggettoTableConfigs,
} from '../../../../shared/classes/risca-table/cerca-titolare-modal/cerca-soggetto.soggetti.table';
import {
  GComponentiGruppoTable,
  IGComponentiGruppoTableConfigs,
} from '../../../../shared/classes/risca-table/gruppo/componenti-gruppo.gruppo.table';
import { FormCercaSoggettoComponent } from '../../../../shared/components/form-cerca-soggetto/form-cerca-soggetto.component';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaFormParentAndChildComponent } from '../../../../shared/components/risca/risca-form-parent-and-child/risca-form-parent-and-child.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  AppCallers,
  AppClaimants,
  AppRoutes,
  ICallbackDataModal,
  IFormCercaSoggetto,
  IPraticaRouteParams,
  IResultVerifyRStDDettaglio,
  IRFCFormErrorsConfigs,
  RiscaAzioniPratica,
  RiscaComponentConfig,
  RiscaFormInputText,
  RiscaGruppo,
  RiscaInfoLevels,
  RiscaIstanzePratica,
  RiscaServerError,
  VerifyIndTipiOperazioni,
} from '../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../shared/utilities/classes/errors-maps';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { AmbitoService } from '../../../ambito/services';
import { IHomeRouteParams } from '../../../home/components/home/utilities/home.interfaces';
import { IIPConfigs } from '../../../pratiche/components/inserisci-pratica/utilities/inserisci-pratica.interfaces';
import { PraticaRouteKeys } from '../../../pratiche/enums/pratica/pratica.enums';
import { IAggiornaIndirizzoSpedizione } from '../../../pratiche/interfaces/indirizzi-spedizione/indirizzi-spedizione.interfaces';
import { GruppoSoggettoService } from '../../../pratiche/service/dati-anagrafici/gruppo-soggetto.service';
import { GruppoConsts } from '../../consts/gruppo/gruppo.consts';
import { GruppoService } from '../../services/gruppo/gruppo.service';
import { ISoggettoRouteParams } from '../soggetto/utilities/soggetto.interfaces';
import {
  RiscaAlertConfigs,
  RiscaButtonConfig,
} from './../../../../shared/utilities/classes/utilities.classes';
import { IGruppoRouteParams } from './utilities/gruppo.interfaces';

@Component({
  selector: 'gruppo',
  templateUrl: './gruppo.component.html',
  styleUrls: ['./gruppo.component.scss'],
})
export class GruppoComponent
  extends RiscaFormParentAndChildComponent<RiscaGruppo>
  implements OnInit, OnDestroy
{
  /** Oggetto di costanti contenente le informazioni comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto GruppoConsts contenente le costanti utilizzate dal componente. */
  G_C = GruppoConsts;
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** Input Gruppo per la pre-configurazione del form. */
  @Input('gruppo') _gruppoConfigs: Gruppo;
  /** AppActions che definisce di che tipo di azioni verranno fatti sui dati. */
  @Input() modalita: AppActions = AppActions.inserimento;

  /** ViewChild connesso al componente di ricerca soggett. */
  @ViewChild('resFormCercaSoggetto') formCS: FormCercaSoggettoComponent;

  /** Oggetto di configurazione per il campo: nome gruppo. */
  nomeGruppoConfig: RiscaComponentConfig<RiscaFormInputText>;

  /** Boolean che definisce la configurazione di accesso all'elemento dell'app. */
  AEA_GRDisabled: boolean = false;

  /** Oggetto IFormCercaSoggetto contenente le informazioni del soggetto cercato. */
  ricerca: IFormCercaSoggetto;
  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert per la ricerca soggetto. */
  alertRicercaConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();
  /** Oggetto FormCercaSoggettoDatiSoggettoTable che conterrà configurazioni per la tabella dei soggetti. */
  tableSoggettiRicerca: FCSDatiSoggettoTable;
  /** Oggetto GComponentiGruppoTable che conterrà le configurazioni per la tabella dei componenti gruppo. */
  tableComponentiGruppo: GComponentiGruppoTable;

  /** String che definisce il nome del gruppo. */
  private nomeGruppo: string;
  /** SoggettoVo che definisce il capogruppo del gruppo. */
  private capogruppo: SoggettoVo;
  /** idPratica a cui è associato il gruppo. */
  private idPratica?: number;

  /** RiscaAzioniPratica che definisce indicazioni per la gestione del ritorno dei dati del soggetto per la pratica. */
  private _praticaNavTarget: RiscaAzioniPratica;

  /** Variabile di appoggio per il soggetto inserito nella pagina di inserimento di un nuovo soggetto. */
  private nuovoSoggettoInserito: SoggettoVo | undefined;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _ambito: AmbitoService,
    private _formBuilder: FormBuilder,
    private _gruppo: GruppoService,
    private _gruppoSoggetto: GruppoSoggettoService,
    private _isUtility: IndirizziSpedizioneUtilityService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    private _riscaModal: RiscaModalService,
    riscaUtilities: RiscaUtilitiesService,
    private _router: Router,
    private _spinner: RiscaSpinnerService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages,
      riscaUtilities
    );
    // Imposto le configurazioni per lo step di journey
    this.stepConfig = GruppoConsts.NAVIGATION_CONFIG;

    // Funzione di setup per il routing del browser
    this.setupBrowserRouting();
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit() {
    // Funzione di init generico
    this.initComponente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();

    // Rimuovo la funzionalità del back del browser
    this._navigationHelper.deleteBrowerBack();
  }

  /**
   * ####################
   * SETUP DEL COMPONENTE
   * ####################
   */

  /**
   * Setta la funzione di override del back del browser
   */
  private setupBrowserRouting() {
    // Definisco una funzione custom per la gestione del back del browser
    const back = () => {
      // Richiamo la funzione locale
      this.tornaIndietro();
    };
    // Registro una funzione custom per il back del browser
    this._navigationHelper.setBrowserBack(back);
    // Sovrascrivo la funzione di default del back con la funzione sopra definita
    this._navigationHelper.overrideBrowserBack();
  }

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Funzione che recupera la configurazione d'abilitazione della form
    this.setupGRDisabled();
    // Funzione di setup per le tabelle del componente
    this.setupGRTables();
    // Setep degli errori da verificare al submit della form
    this.setupMainFormErrors();
    // Setup delle input form del componente
    this.setupFormInputs();

    // Carico i dati della snapshot
    this.loadJSnapshot(this);
    // Effettuo il setup dei dati passati alla pagina
    this.setupRouteParams();
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  setupGRDisabled() {
    // Recupero la chiave per la configurazione della form
    const gaKey = this.AEAK_C.DETTAGLIO_GRUPPO;
    // Recupero la configurazione della form dal servizio
    this.AEA_GRDisabled = this._accessoElementiApp.isAEADisabled(gaKey);
  }

  /**
   * Funzione che inizializza le tabelle del componente.
   */
  setupGRTables() {
    // Richiamo la configurazione delle tabelle
    this.setupTavleSoggettiRicerca();
  }

  /**
   * Funzione di comodo che permette l'inizializzazione delle tabelle.
   * @param soggetti Array di SoggettoVo con i dati di complicazione della tabella.
   */
  private setupTavleSoggettiRicerca(soggetti?: SoggettoVo[]) {
    // Definisco gli oggetti di configurazione delle tabelle
    const cds: IFCSDatiSoggettoTableConfigs = {
      soggetti,
      mostraFonteRicerca: false,
    };
    // Lancio la configurazione della tabella
    this.tableSoggettiRicerca = new FCSDatiSoggettoTable(cds);
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [
      ...this.EM.MAP_FORM_CONTROL_REQUIRED,
      ...this.EM.MAP_FORM_GROUP_REQUIRED,
      ...this.EM.MAP_CONTROLLI_COMPONENTI_GRUPPO,
    ];
  }

  /**
   * Funzione di setup delle inputs form del componente.
   */
  private setupFormInputs() {
    // Genero il setup per i campi
    const { nomeGruppoConfig } = this._gruppo.setupFormInputs();
    // Assegno locamente il setup
    this.nomeGruppoConfig = nomeGruppoConfig;
  }

  /**
   * Funzione che verifica se tramite il servizio di Route sono stati passati parametri di configurazione.
   */
  private setupRouteParams() {
    // Recupero i parametri dallo state della route
    const state = this._navigationHelper.getRouterState(this._router);
    // Attivo il setup dei dati di route
    this.setupConfigsData(state);
  }

  /**
   * Funzione di supporto che verifica e carica i dati del componente in base ai route params e i dati della snapshot.
   * @param routeParams IGruppoRouteParams contenente i dati recuperati dal servizio di router.
   */
  private setupConfigsData(routeParams: IGruppoRouteParams) {
    // Recupero le informazioni
    const { gruppo, modalita, soggettoInsert, idPratica, praticaNavTarget } =
      routeParams;

    // Verifico i parametri
    if (gruppo) {
      this.gruppoConfigs = gruppo;
    }
    // Verifico se esiste la configurazione dal route per la modalità
    if (modalita) {
      this.modalita = modalita;
    }
    // Verifico se è stato inserito un soggetto
    if (soggettoInsert) {
      // Se è stato inserito un nuovo oggetto, lo metto nella variabile di appoggio.
      this.nuovoSoggettoInserito = soggettoInsert;
    }
    // Imposto l'idPratica
    if (idPratica) {
      // Assegnazione idPratica
      this.idPratica = idPratica;
    }
    // Verifico se tra i parametri c'è un'indicazione di quale parte della gestione della pratica ha aperto la pratica effettivamente (la nav)
    if (praticaNavTarget) {
      // Esiste, mi salvo localmente l'informazione per gestire il ritorno
      this._praticaNavTarget = praticaNavTarget;
    }
  }

  /**
   * ###################
   * INIT DEL COMPONENTE
   * ###################
   */

  private initComponente() {
    // Lancio il setup delle form
    this.initForms();
    // Lancio la funzione di setup dei dati del gruppo
    this.initGruppoConfigs(this.gruppoConfigs);
  }

  /**
   * Funzione di comodo che permette l'inizializzazione delle tabelle.
   * @param componenti Array di SoggettoVo con i dati di complicazione della tabella.
   */
  private initTableComponentiGruppo(componenti?: SoggettoVo[]) {
    // Variabile di comodo
    const AEA_GRDisabled = this.AEA_GRDisabled;
    const isGestioneAbilitata = this.isGestioneAbilitata;
    // Id Capogruppo
    const idCapogruppo = this.capogruppo?.id_soggetto;
    // Id gruppo
    const idGruppo = this.gruppoConfigs?.id_gruppo_soggetto;

    // Definisco gli oggetti di configurazione delle tabelle
    const cgcg: IGComponentiGruppoTableConfigs = {
      componenti,
      AEA_GRDisabled,
      isGestioneAbilitata,
      idCapogruppo,
      idGruppo,
    };
    // Lancio la configurazione della tabella
    this.tableComponentiGruppo = new GComponentiGruppoTable(cgcg);

    // Aggiorno l'elemento del capogruppo
    this.tableComponentiGruppo.setElementSelectedExclusive((c: SoggettoVo) => {
      // Ritorno il check sugli id
      return c.id_soggetto === this.capogruppo.id_soggetto;
    });
  }

  /**
   * Funzione di init speciale che recupera in automatico le informazioni della tabella ed effettua una nuova istanza.
   */
  private initTCGFromExistingData() {
    // Recupero le informazioni dall'attuale struttura della tabella
    const componenti = this.tableComponentiGruppo?.getDataSource() ?? [];
    // Ricostruisco la tabella partendo dai componente ritornati
    this.initTableComponentiGruppo(componenti);
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup della struttura della form principale
    this.initFormGruppo();
  }

  /**
   * Init del form: mainForm.
   */
  private initFormGruppo() {
    this.mainForm = this._formBuilder.group({
      nomeGruppo: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      componentiGruppo: new FormControl(null, {
        validators: [almenoUnComponenteEUnCapogruppoValidator()],
      }),
    });

    // Verifico se è definito un nome gruppo
    if (this.nomeGruppo) {
      // Aggiorno il nome gruppo
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.G_C.NOME_GRUPPO,
        this.nomeGruppo
      );
    }

    // Verifico se la form è da disabilitare per la configurazione profilo
    if (this.AEA_GRDisabled || !this.isGestioneAbilitata) {
      // Disabilito il form
      this.mainForm.disable();
    }
  }

  /**
   * ################################
   * FUNZIONI PER GESTIONE DELLA FORM
   * ################################
   */

  /**
   * Funzione agganciata all'evento di Submit per il mainForm.
   */
  onFormSubmit() {
    // Verifico che la form esisti
    if (!this.mainForm) {
      return;
    }

    // Resetta banner dei messaggi
    this.resetAlertConfigs(this.alertConfigs);

    // Lancio la funzione di pre-validazione del form
    this.prepareMainFormForValidation();
    // Il form è stato submittato
    this.mainFormSubmitted = true;

    // Verifico che la form sia valida
    if (this.mainForm.valid) {
      // Recupero i dati finali del gruppo
      const gruppoForm = this.getMainFormRawValue();
      // Salvo i dati del gruppo
      this.gestioneSalvataggioGruppo(gruppoForm);
      // #
    } else {
      // Gestisco la visualizzazione degli errori del form usiForm
      this.onFormErrors(this.mainForm);
    }
  }

  /**
   * L'override della funzione prepareMainFormForValidation() in RiscaFormParentAndChildComponent<RiscaGruppo>.
   * La funzione recupererà tutti i dati e aggiungerà le informazioni all'interno del form per la validazione.
   * @override
   */
  prepareMainFormForValidation() {
    // Recupero le informazioni per configs
    const actualConfigs: Gruppo = this.gruppoConfigs || ({} as any);
    // Recupero il capogruppo
    const capogruppo = this.capogruppo;
    // Recupero i dati della tabella
    const soggetti: SoggettoVo[] =
      this.tableComponentiGruppo?.getDataSource() || [];

    // Converto i soggetti in componenti
    const componenti = this._gruppo.convertSoggettiVoToComponentiGruppo(
      soggetti,
      actualConfigs,
      capogruppo
    );
    // Imposto nel form i dati dei componenti
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.G_C.COMPONENTI_GRUPPO,
      componenti
    );
  }

  /**
   * Funzione di supporto che gestisce la visualizzazione dei messaggi d'errore per il form group passato in input.
   * @param formGroup FormGroup da verificare.
   * @param errConfigs IRFCFormErrorsConfigs contenente le configurazioni extra per la gestione della funzione.
   * @override
   */
  onFormErrors(formGroup: FormGroup, errConfigs?: IRFCFormErrorsConfigs) {
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
    const mes =
      this._riscaUtilities.getMessageFromRiscaServerError(serverError);
    // Verifico se esiste il messaggio
    if (mes) {
      // Aggiungo il messaggio alla lista dei messaggi errore
      mef.push(mes);
    }

    // Variabili di comodo
    const a = this.alertConfigs;
    const d = RiscaInfoLevels.danger;
    // Definisco un timeout per permettere ad angular di accorgersi della modifica dell'oggetto
    setTimeout(() => {
      // Aggiorno l'alert
      this.aggiornaAlertConfigs(a, mef, d);
    });
  }

  /**
   * Funzione di reset del form e del componente.
   */
  onFormReset() {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Reset manuale della form
    this.mainForm.reset();
    // Resetto la tabella dei componenti gruppo
    this.tableComponentiGruppo.clear();
  }

  /**
   * ################################
   * GESTIONE DELLA MODALE DI RICERCA
   * ################################
   */

  /**
   * Funzione agganciata al componente di datiSoggetto soggetto.
   * @param ricerca IFormCercaSoggetto contente i dati ottenuti dalla ricerca del soggetto.
   */
  onSoggettoCercato(ricerca: IFormCercaSoggetto) {
    // Resetto l'alert
    this.aggiornaAlertConfigs(this.alertRicercaConfigs);

    // Aggiorno i dati della ricerca
    this.ricerca = ricerca;
    // Resetto la tabella
    this.tableSoggettiRicerca.clear();

    // Verifico il soggetto
    if (this.verificaDatiSoggetto(ricerca)) {
      // Recupero i dati anagrafici
      const { soggetto } = ricerca?.ricercaSoggetto || {};
      // Genero la tabella dei soggetti
      this.setSoggettoTable(soggetto);
    }
  }

  /**
   * Funzione che verifica i dati del soggetto ritornata dalla ricerca.
   * @param ricerca IFormCercaSoggetto con i dati del soggetto.
   * @returns boolean se i dati del soggetto sono validi.
   */
  private verificaDatiSoggetto(ricerca: IFormCercaSoggetto): boolean {
    // Recupero il soggetto dalla ricerca
    const { soggetto } = ricerca?.ricercaSoggetto || {};

    // Verifico che esista il valore di ritorno
    if (!soggetto) {
      // Definisco il codice per il messaggio
      const code = RiscaNotifyCodes.A008;
      // Recupero il messaggio di warning per l'utente
      const nessunSoggetto = this._riscaMessages.getMessage(code);
      // Definisco i valori per l'alert
      const aC = this.alertRicercaConfigs;
      const m = [nessunSoggetto];
      const l = RiscaInfoLevels.warning;

      // Setto le configurazioni dell'alert
      this.aggiornaAlertConfigs(aC, m, l);
      // Dato non valido
      return false;
    }

    // Dati validi
    return true;
  }

  /**
   * Funzione che effettua l'istanza della tabella soggetti.
   * @param soggetto SoggettoVo contenente i dati anagrafici del titolare trovato.
   */
  private setSoggettoTable(soggetto: SoggettoVo) {
    // Aggiungo il soggetto trovato
    this.tableSoggettiRicerca.addElement(soggetto);
  }

  /**
   * Funzione invocata alla pressione del pulsante ANNULLA all'interno dell'interfaccia.
   * Verrà resettata la tabella dei risultati per la ricerca soggetto.
   */
  resetRicercaSoggetto() {
    // Lancio la funzione di reset del componente
    this.formCS.resetForm();
    // Resetto la tabella
    this.tableSoggettiRicerca.clear();
  }

  /**
   * Funzione invocata alla pressione del pulsante INSERISCI SOGGETTO all'interno dell'interfaccia.
   * Verrà aperta la pagina soggetto per l'inserimento di un nuovo soggetto.
   */
  richiestaInserimentoSoggetto() {
    // Verifico se ci sono dati della ricerca
    if (!this.ricerca) {
      return;
    }

    // Salvo nel componente le informazioni
    this.nomeGruppo = this._riscaUtilities.getFormValue(
      this.mainForm,
      this.G_C.NOME_GRUPPO
    );

    // Aggiorno i dati di configurazione che verranno salvati nella snapshot per poi essere ricaricati
    this.updateGruppoConfig();
    // Richiamo la funzione per inserire un nuovo soggetto
    this.inserisciNuovoSoggetto(this.ricerca);
  }

  /**
   * Funzione che permette d'inserire un nuovo soggetto.
   * @param data IFormCercaSoggetto che identifica i dati del titolare cercato, ma non trovato.
   */
  private inserisciNuovoSoggetto(data: IFormCercaSoggetto) {
    // Creo un oggetto SoggettoVo parziale con le informazioni del codice fiscale e tipo soggetto
    const datiAnagrafici: SoggettoVo = {
      id_ambito: this._user.idAmbito,
      cf_soggetto: data.codiceFiscale,
      tipo_soggetto: data.tipoSoggetto,
    };
    // Definisco lo state per la rotta
    const state: ISoggettoRouteParams = {
      soggetto: datiAnagrafici,
      modalita: AppActions.inserimento,
    };

    // Aggiungo al filo d'arianna il segmento per nuovo soggetto
    const nuovoSFA: FALivello = this._riscaFA.nuovoSoggetto;
    // Creo e aggiungo il segmento
    this._riscaFA.aggiungiSegmentoByLivelli(nuovoSFA);

    // Variabile di comodo
    const route = AppRoutes.soggetto;
    // Recupero i dati comuni per la generazione dello step
    const stepBase = this.stepConfig;
    const snapshot = this.snapshotConfigs;
    // Aggiungo alcune informazioni allo step
    stepBase.stepClaimant = AppClaimants.insertSoggetto;
    // Genero l'oggetto per il target del routing
    const target = this.navigationConfigs(route, state);

    // Definisco le informazioni per il salvataggio dello step
    const step: IJourneyStep = { ...stepBase, ...target };
    // Utilizzo il servizio per aggiungere uno step al journey
    this._navigationHelper.stepForward(step, snapshot);
  }

  /**
   * ##################################
   * FUNZIONI COLLEGATE ALL'INTERFACCIA
   * ##################################
   */

  /**
   * Funzione invocata alla pressione del pulsante ANNULLA all'interno dell'interfaccia.
   */
  annullaForm() {
    // Dichiaro la funzione di callback
    const onConfirm = () => {
      // Verifico se sono in inserimento o in modifica
      if (this.inserimento) {
        // Richiamo il reset delle form
        this.onFormReset();
        // #
      } else if (this.modifica) {
        // Resetto la tabella dei componenti gruppo
        this.tableComponentiGruppo.clear();
        // Resetto il form allo stato originale del gruppo
        this.initComponente();
      }
    };
    // Richiamo la funzione del servizio per la modale di conferma annullamento
    this._riscaModal.apriModalConfermaAnnullamento({ onConfirm });
  }

  /**
   * Funzione invocata alla pressione del pulsante AGGIUNGI COMPONENTE all'interno dell'interfaccia.
   * Il soggetto trovato dalla ricerca verrà aggiunto alla lista dei componenti del gruppo.
   */
  aggiungiComponente() {
    // Verifico se ci sono dati della ricerca
    if (!this.ricerca) {
      return;
    }

    // Recupero il soggetto
    const soggetto = this.ricerca?.ricercaSoggetto?.soggetto;
    // Definisco la funzione di ricerca per un soggetto nella tabella
    const findSoggetto = (
      s: SoggettoVo,
      eS: RiscaTableDataConfig<SoggettoVo>
    ) => {
      // Verifico per stesso id soggetto
      return s.id_soggetto === eS.original.id_soggetto;
    };

    // Se non esiste la tabella dei componenti del gruppo, la creo
    if (!this.tableComponentiGruppo) {
      // Variabile di comodo
      const AEA_GRDisabled = this.AEA_GRDisabled;
      // Id Capogruppo
      const idCapogruppo = this.capogruppo?.id_soggetto;
      // Id gruppo
      const idGruppo = this.gruppoConfigs?.id_gruppo_soggetto;
      // Modifico i componenti per settare un nuovo capogruppo
      const componenti = this.tableComponentiGruppo?.getDataSource() ?? [];
      // Aggiorno i componenti del gruppo per aggiornare il flag capogruppo
      componenti.forEach((c) => {
        c.flg_capo_gruppo = c.id_soggetto == idCapogruppo;
      });

      // Definisco gli oggetti di configurazione delle tabelle
      const cgcg: IGComponentiGruppoTableConfigs = {
        componenti,
        AEA_GRDisabled,
        idCapogruppo,
        idGruppo,
      };
      // Ricreo la tabella
      this.tableComponentiGruppo = new GComponentiGruppoTable(cgcg);
    }

    // Verifico se nei componenti non esiste già il soggetto
    const existSoggettoInTable = this.tableComponentiGruppo.isElementInTable(
      soggetto,
      findSoggetto
    );

    // Verifico se il soggetto è già stato inserito
    if (!existSoggettoInTable) {
      // Aggiungo alla tabella dei componenti il nuovo soggetto
      this.tableComponentiGruppo.addElement(soggetto);
    }

    // Resetto la tabella del soggetto cercato
    this.resetRicercaSoggetto();
  }

  /**
   * Funzione invocata al click del pulsante ELIMINA GRUPPO.
   */
  eliminaGruppoBtn() {
    this.eliminaGruppo(this.gruppoConfigs);
  }

  /**
   * Funzione che gestisce le logiche di eliminazione dei dati.
   * @param gruppo Gruppo contenente le informazioni del gruppo da eliminare.
   */
  eliminaGruppo(gruppo: Gruppo) {
    // Resetto la lista errori
    this.aggiornaAlertConfigs(this.alertConfigs);

    // Verifico che esistano i dati anagrafici del soggetto
    if (gruppo) {
      // Richiamo la funzione di verifica e cancellazione dati
      this.controllaPraticheGruppo(gruppo, AppActions.cancellazione);
    }
  }

  /**
   * ###################################
   * GESTIONE DEGLI EVENTI DELLE TABELLE
   * ###################################
   */

  /**
   * Funzione agganciata all'evento di click di una radio dalla tabella.
   * @param row RiscaTableDataConfig<SoggettoVo> contenente i dati della riga.
   */
  modificaCapogruppo(row: RiscaTableDataConfig<SoggettoVo>) {
    // Aggiorno il capogruppo
    this.capogruppo = row.original;
    // Modifico i componenti per settare un nuovo capogruppo
    const componenti = this.tableComponentiGruppo?.getDataSource() ?? [];
    // Richiamo la costruzione del gruppo
    this.initTableComponentiGruppo(componenti);
  }

  /**
   * Funzione agganciata all'evento di dettaglio lanciato dalla tabella.
   * @param row RiscaTableDataConfig<SoggettoVo> contenente i dati della riga.
   */
  dettaglioComponenteGruppo(row: RiscaTableDataConfig<SoggettoVo>) {
    // Verifico l'input
    if (!row) {
      return;
    }

    // Salvo nel componente le informazioni
    this.nomeGruppo = this._riscaUtilities.getFormValue(
      this.mainForm,
      this.G_C.NOME_GRUPPO
    );

    // Aggiungo al filo d'arianna il segmento per dettaglio soggetto
    const dettSFA: FALivello = this._riscaFA.dettaglioSoggetto;
    // Creo e aggiungo il segmento
    this._riscaFA.aggiungiSegmentoByLivelli(dettSFA);

    // Recupero il dato originale
    const datiAnagrafici: SoggettoVo = row.original;
    // Definisco lo state per la route per soggetto
    const state: ISoggettoRouteParams = {
      soggetto: datiAnagrafici,
      modalita: AppActions.modifica,
      eliminaDisabled: true,
    };

    // Variabile di comodo
    const route = AppRoutes.soggetto;
    // Genero l'oggetto per il target del routing
    const target = this.navigationConfigs(route, state);
    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * Funzione agganciata all'evento di eliminazione di una riga della tabella.
   * @param row RiscaTableDataConfig<SoggettoVo> contenente i dati della riga.
   */
  eliminaComponenteGruppo(row: RiscaTableDataConfig<SoggettoVo>) {
    // Definisco la funzione di callback nel caso in cui l'utente confermi la cancellazione della riga
    const onConfirm = () => {
      // Richiamo la logica di rimozione recapito alternativo
      this.tableComponentiGruppo.removeElement(row);
    };

    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.A020;
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConfermaWithCode(code, { onConfirm });
  }

  /**
   * Funzione agganciata all'evento di azione custom da parte della tabella dei componenti del gruppo.
   * @param customAction IRiscaTableACEvent<SoggettoVo> con la configurazione dati per la riga per la quale l'azione è stata premuta.
   */
  customCGTable(customAction: IRiscaTableACEvent<SoggettoVo>) {
    // Definisco le chiavi per le azioni
    const MOD_IND_SPED_PRI = this.G_C.INDIRIZZO_SPEDIZIONE_PRI;
    const MOD_IND_SPED_ALT = this.G_C.INDIRIZZO_SPEDIZIONE_ALT;
    // Verifico quale pulsante è stato premuto, recupero l'action
    const { action } = customAction?.action || {};

    // Verifico quale azione è stata premuta
    switch (action) {
      case MOD_IND_SPED_PRI:
      case MOD_IND_SPED_ALT:
        // Richiamo la funzione per l'aggiornamento dell'indirizzo di spedizione
        this.modificaISCTable(customAction);
        break;
      // #
      default:
        return;
    }
  }

  /**
   * ##############################################################
   * FUNZIONI PER LA MODIFICA DI UN SINGOLO INDIRIZZO DI SPEDIZIONE
   * ##############################################################
   */

  /**
   * Funzione di supporto che gestisce la gestione dei dati per l'aggiornamento di un recapito di un soggetto, come dato passato dalla tabella.
   * @param action RiscaTableDataConfig<RecapitoVo> contenente i dati della riga del recapito alternativo per la quale modificare l'indirizzo di spedizione.
   */
  private modificaISCTable(action: IRiscaTableACEvent<SoggettoVo>) {
    // Verifico l'input
    if (!action) {
      // Non ci sono dati per fare la modifica
      return;
    }

    // Recupero l'id del gruppo corrente se esiste
    const idGruppo = this.gruppoConfigs?.id_gruppo_soggetto;
    // Richiamo la funzione per generare le configurazioni dati per la modale
    const config = this.infoISFromCustomAction(action, idGruppo);
    // Verifico che la configurazione esista, altrimenti blocco le logiche
    if (!config) {
      // Nessuna configurazione generata
      return;
    }

    // Creo una copiag della configurazione della modale perché verrà riutilizzata sul confirm, ma così facendo slego le referenze dei dati
    const configConfirm = cloneDeep(config);
    // Definisco le callbacks per la modale
    const callbacks: ICallbackDataModal = {
      onConfirm: (indirizzi: IndirizzoSpedizioneVo[]) => {
        // Richiamo la funzione di gestione dell'aggiornamento dati
        this.onModificaISCTable(action, indirizzi[0], configConfirm);
      },
    };
    // Aggiorno la proprietà delle callbacks
    config.callbacks = callbacks;

    // Richiamo la funzione di modifica indirizzo spedizone tramite modale
    this._isUtility.modificaIndirizziSpedizioneSoggetto(config);
  }

  /**
   * Funzione di supporto che estrae le informazioni per la configurazione della modale, da un oggetto custom action generato dalla tabella dei componenti soggetto.
   * La funzione non definirà la proprietà "callbacks" dell'oggetto di ritorno.
   * @param action IRiscaTableACEvent<SoggettoVo> con le informazioni riguardanti il componente del gruppo per la modifica dell'indirizzo di spedizione.
   * @param idGruppo number che definisce l'id gruppo di riferimento per il recupero dei dati relativi agli indirizzi di spedizione.
   * @returns IAggiornaIndirizzoSpedizione come configurazione per la modale di modifica dell'indirizzo di spedizione.
   */
  private infoISFromCustomAction(
    action: IRiscaTableACEvent<SoggettoVo>,
    idGruppo: number
  ): IAggiornaIndirizzoSpedizione {
    // Verifico l'input
    if (!action || !idGruppo) {
      // Niente configurazioni
      return;
    }

    // Definisco le variabili per la configurazione della modale
    let soggetto: SoggettoVo;
    let idRecapito: number;
    const callbacks = undefined;

    // Recupero le informazioni dall'action con i dati
    const { row, subRow } = action;
    // Estraggo le informazioni comuni
    soggetto = cloneDeep(row?.original);

    // Verifico quali informazioni ho dalla riga
    if (subRow) {
      // C'è la subrow, definisco le logiche per il recapito alternativo
      const recapitoSubRow: RecapitoVo = subRow?.original;
      // Definisco il valore per le variabili
      idRecapito = recapitoSubRow?.id_recapito;
      // #
    } else {
      // Recupero il recapito principale del soggetto
      const rp = this._riscaUtilities.recapitoPrincipaleSoggetto(soggetto);
      // Estraggo l'id dal recapito principale
      idRecapito = rp?.id_recapito;
    }

    // Definisco la gestione del flag di abilitazione del form
    const isFormDisabilitato = this.isISFormDisabilitato;

    // Ritorno la configurazione dati
    return { soggetto, idRecapito, idGruppo, callbacks, isFormDisabilitato };
  }

  /**
   * Funzione invocata alla modifica di un indirizzo di spedizione.
   * Come per la gestione dei recapiti alternativi, vado a gestire i dati della tabella.
   * @param action RiscaTableDataConfig<RecapitoVo> contenente i dati della riga del recapito alternativo per la quale modificare l'indirizzo di spedizione.
   * @param indirizzoUpd IndirizzoSpedizioneVo che definisce l'oggetto indirizzo di spedizione aggiornato su DB.
   * @param modalConfig IAggiornaIndirizzoSpedizione con all'interno delle informazioni di comodo per velocizzare l'aggiornamento dati a seguito della confirm della modale.
   */
  private onModificaISCTable(
    action: IRiscaTableACEvent<SoggettoVo>,
    indirizzoUpd: IndirizzoSpedizioneVo,
    modalConfig: IAggiornaIndirizzoSpedizione
  ) {
    // Verifico l'input
    if (!action || !indirizzoUpd || !modalConfig) {
      // Non c'è il dato d'aggiornare
      return;
    }

    // Recupero dall'input le informazioni per l'aggiornamento dei dati
    const { soggetto, idRecapito, idGruppo } = modalConfig;
    const soggettoRow = action?.row;

    // Per il recapito, vado ad aggiornare l'indirizzo di spedizione
    this._isUtility.aggiornaIndirizzoSpedizioneInSoggetto(
      soggetto,
      idRecapito,
      indirizzoUpd,
      idGruppo
    );

    // Una volta aggiornato l'indirizzo di spedizione, aggiorno i dati della tabella
    this.tableComponentiGruppo.convertAndUpdateElement(soggetto, soggettoRow);
    // Riassegno l'oggetto della variabile per aggiornare la parte di grafica
    this.initTCGFromExistingData();
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione che effettua il setup dei dati del form e delle tabelle della pagina.
   * @param gruppo Gruppo contenente le informazioni per il setup.
   */
  private initGruppoConfigs(gruppo: Gruppo) {
    // Verifico l'input
    if (!gruppo) {
      return;
    }

    // Recupero le proprietà d'interesse
    const { des_gruppo_soggetto, componenti_gruppo } = gruppo;

    // Verifico esista la proprietà
    if (des_gruppo_soggetto) {
      // Imposto la proprietà nel FormGroup
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.G_C.NOME_GRUPPO,
        des_gruppo_soggetto
      );
    }

    // Avvio lo spinner per caricare i dati
    this._spinner.show();

    // Verifico esista la proprietà
    if (componenti_gruppo) {
      // Lancio la funzione per aggiungere i soggetti alla tabella dei recapiti
      this.initComponentiSoggetto(componenti_gruppo);
      // N.B.: ^^^^^ La chiusura dello spinner sarà qui dentro ^^^^^
      // #
    } else {
      // Interrompo eventuali spinner attivi
      this._spinner.hide();
    }
  }

  /**
   * Funzione che cicla i componenti del gruppo e scarica le informazioni dei soggetti.
   * @param componentiGruppo Array di ComponenteGruppo per l'ottenimento dei dati.
   */
  private initComponentiSoggetto(componentiGruppo: ComponenteGruppo[]) {
    // Verifico l'input
    if (!componentiGruppo) {
      // Nessun dato, blocco il flusso
      return;
    }

    // Richiamo la funzione del servizio per lo scarico dei dettagli
    this._gruppo.getDettaglioComponentiGruppo(componentiGruppo).subscribe({
      next: (dettagli: SoggettoVo[]) => {
        // Richiamo la funzione per la gestione dei dettagli componenti
        this.onDettaglioComponentiGruppo(componentiGruppo, dettagli);
        // N.B.: ^^^^^ La chiusura dello spinner sarà qui dentro ^^^^^
        // #
      },
      error: (e: RiscaServerError) => {
        // Interrompo eventuali spinner attivi
        this._spinner.hide();
        // Richiamo la gestione dell'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione di supporto invocata nel momento in cui vengono scaricati i dettagli dei componenti del gruppo.
   * @param componentiGruppo Array di ComponenteGruppo con le informazioni dei componenti gruppo.
   * @param dettagli Array di SoggettoVo contenente i dati dei dettagli dei componenti del gruppo.
   */
  private onDettaglioComponentiGruppo(
    componentiGruppo: ComponenteGruppo[],
    dettagli: SoggettoVo[]
  ) {
    // Verifico che i parametri esistano
    if (!componentiGruppo || !dettagli) {
      // Nascondo lo spinner
      this._spinner.hide();
      // Blocco il flusso
      return;
    }

    // Richiamo la funzione di creazione della tabella
    this.aggiornaComponentiGruppo(componentiGruppo, dettagli);
  }

  /**
   * Funzione che gestisce l'aggiornamento dei componenti gruppo.
   * @param cg Array di ComponenteGruppo con le informazioni dei componenti gruppo.
   * @param d Array di SoggettoVo contenente i dati dei dettagli dei componenti del gruppo.
   */
  private aggiornaComponentiGruppo(cg: ComponenteGruppo[], d: SoggettoVo[]) {
    // Imposto il capogruppo a livello di componente
    this.capogruppo = this.getCapogruppoDaComponenti(cg, d);

    // Creo la tabella
    this.initTableComponentiGruppo();

    // Recupero i soggetti che sono i componenti del gruppo
    const cDet = this.generaComponentiDaDettaglio(cg, d);
    // Aggiorno le informazioni dei componenti già presenti
    const cDetNotUpd = this.aggiornaComponentiTabDaSoggetti(cDet);

    // Aggiungo alla tabella tutti i soggetti non presenti
    this.aggiungiComponentiTabDaSoggetti(cDetNotUpd, this.capogruppo).subscribe(
      {
        next: (r: boolean) => {
          // Riassegno l'oggetto della tabella per aggiornare lo stato delle icone
          this.tableComponentiGruppo = clone(this.tableComponentiGruppo);
          // se è stato inserito un nuovo soggetto, lo aggiungo alla tabella.
          if (this.nuovoSoggettoInserito) {
            this.tableComponentiGruppo.addElement(this.nuovoSoggettoInserito);
            this.nuovoSoggettoInserito = undefined;
          }
          // Nascondo lo spinner
          this._spinner.hide();
        },
        error: (e: RiscaServerError) => {
          // Interrompo eventuali spinner attivi
          this._spinner.hide();
          // Richiamo la gestione dell'errore
          this.onServiziError(e);
        },
      }
    );
  }

  /**
   * Funzione che gestisce l'aggiornamento dei componenti gruppo.
   * @param cg Array di ComponenteGruppo con le informazioni dei componenti gruppo.
   * @param d Array di SoggettoVo contenente i dati dei dettagli dei componenti del gruppo.
   * @returns Array di SoggettoVo contenente i soggetti che fanno parte del gruppo, estratti dai componenti.
   */
  private generaComponentiDaDettaglio(
    cg: ComponenteGruppo[] = [],
    d: SoggettoVo[] = []
  ): SoggettoVo[] {
    // Definisco variabili di comodo
    const mergeProperty = 'id_soggetto';
    const mergeCG = cg as any;
    const mergeD = d as any;
    // Recupero i soggetti che sono definiti come componenti gruppo
    const comp = intersectionBy(mergeD, mergeCG, mergeProperty) as SoggettoVo[];

    // Ritorno la lista dei componenti
    return comp;
  }

  /**
   * Funzione che aggiorna i componenti tabella da un array di soggetti.
   * @param s Array di SoggettoVo che possono essere aggiornati nella tabella.
   * @returns Array di SoggettoVo che non sono stati aggiornati.
   */
  private aggiornaComponentiTabDaSoggetti(s: SoggettoVo[]): SoggettoVo[] {
    // Creo una copia dell'array
    const cDet = clone(s);
    // Recupero la lista dei componenti nella tabella (magari sono stati inseriti, ma non ancora salvati)
    const cTab = this.tableComponentiGruppo
      .source as RiscaTableDataConfig<SoggettoVo>[];

    // Ciclo la lista degli elementi della tabella già presenti
    cTab.forEach((eTab: RiscaTableDataConfig<SoggettoVo>) => {
      // Verifico se l'elemento della tabella è presente nella lista componenti dettaglio
      const iCD = cDet.findIndex((eDet: SoggettoVo) => {
        // Recupero l'oggetto originale dall'elemento tabella ciclato
        const o = eTab.original;
        // Verifico se combaciano gli id_soggetto
        return eDet.id_soggetto === o.id_soggetto;
      });

      // Verifico se c'è corrispondenza
      if (iCD !== -1) {
        // Recupero l'elemento dalla lista dei componenti dettaglio
        const sDet = cDet[iCD];
        // Gli oggetti sono gli stessi, effettuo una modifica del dato in tabella
        this.tableComponentiGruppo.convertAndUpdateElement(sDet, eTab);
        // Una volta aggiornato, rimuovo l'elemento dalla lista dei componenti dettaglio
        cDet.splice(iCD, 1);
        // #
      }
    });

    // Ritorno i restanti elementi
    return cDet;
  }

  /**
   * Funzione che aggiunge i soggetti in input all'interno della tabella.
   * La funzione imposterà un capogruppo e ordinerà gli elementi per il capogruppo.
   * @param soggetti Array di SoggettoVo contenente i soggetti d'aggiungere alla tabella.
   * @param capogruppo SoggettoVo da impostare come capogruppo.
   * @returns Observable<boolean> con il risultato dell'operazione.
   */
  private aggiungiComponentiTabDaSoggetti(
    soggetti: SoggettoVo[],
    capogruppo?: SoggettoVo
  ): Observable<boolean> {
    // Verifico l'input
    if (!soggetti || soggetti.length === 0) {
      // Non è successo niente
      return of(true);
    }

    // Effettuo l'aggiunta di ogni elemento all'interno dell'array
    return this.tableComponentiGruppo.addElements(soggetti).pipe(
      map((r: boolean) => {
        // Una volta terminata l'agginta di tutti gli elementi imposto il selezionato per default
        this.tableComponentiGruppo.setElementSelectedExclusive((s) => {
          // Uso la funzione di supporto per definire il selected
          return s?.id_soggetto === capogruppo?.id_soggetto;
        });
        // Richiamo il sort iniziale per i dati della tabella
        this._gruppo.sortTableByCapogruppo(this.tableComponentiGruppo);

        // Ritorno il valore dell'add elements
        return r;
      })
    );
  }

  /**
   * Funzione di comodo per il setup del capogruppo a livello componente.
   * @param componentiGruppo Array di ComponenteGruppo con le informazioni dei componenti gruppo.
   * @param dettagli Array di SoggettoVo contenente i dati dei dettagli dei componenti del gruppo.
   * @returns ComponenteGruppo che definisce l'oggetto capogruppo, se esiste.
   */
  private getCapogruppoDaComponenti(
    componentiGruppo: ComponenteGruppo[],
    dettagli: SoggettoVo[]
  ): ComponenteGruppo {
    // Cerco il capogruppo dai componenti
    const capogruppo = componentiGruppo.find((c: ComponenteGruppo) => {
      // Verifico se è il capogruppo
      return c.flg_capo_gruppo;
    });
    // Verifico se esiste (dovrebbe sempre esistere, ma non si sa mai nella vita)
    if (capogruppo) {
      // Recupero l'id del capogruppo
      const idCG = capogruppo.id_soggetto;
      // Cerco il dettaglio del capogruppo
      const sCG = dettagli.find((s: SoggettoVo) => {
        // Recupero il soggetto capogruppo per id
        return s.id_soggetto === idCG;
      });
      // Verifico se è stato trovato il dettaglio
      if (sCG) {
        // Assegno localmente il capogruppo
        this.capogruppo = sCG;
      }
    }

    // Ritorno il capogruppo
    return capogruppo;
  }

  /**
   * Funzione di supporto per la conversione di un oggetto RiscaGruppo ad un oggetto Gruppo.
   * @param rg RiscaGruppo da convertire.
   * @param g Gruppo da mergiare ai dati della form.
   * @returns Gruppo convertito.
   */
  private convertRiscaGruppoToGruppo(rg: RiscaGruppo, g?: Gruppo): Gruppo {
    // Converto l'oggetto gruppoForm
    return this._gruppo.convertRiscaGruppoToGruppo(rg, g);
  }

  /**
   * Funzione di supporto per l'aggiornamento dell'oggetto _gruppoConfigs, basandosi sulla situazione attuale del componente.
   */
  private updateGruppoConfig() {
    // Lancio la funzione di pre-validazione del form
    this.prepareMainFormForValidation();
    // Recupero i dati finali del gruppo
    const rg = this.getMainFormRawValue();

    // Recupero l'oggetto della configurazione gruppo
    const gc = this.gruppoConfigs;
    // Effettuo un merge tra l'attuale configurazione e il form del gruppo
    this.gruppoConfigs = this.convertRiscaGruppoToGruppo(rg, gc);
  }

  /**
   * Funzione di comodo che gestisce le informazioni per il check per stesso capogruppo.
   * @param gruppo Gruppo che definisce le informazioni per il check del capogruppo. Questa informazione deve essere generata dalla pagina.
   * @returns boolean che definisce se il capogruppo iniziale è lo stesso del capogruppo generato dalla pagina del gruppo.
   */
  private sameCapigruppoByGruppi(gruppo: Gruppo): boolean {
    // Recupero la configurazione del gruppo a "tempo 0"
    const GT0 = this.gruppoConfigs;
    // Verifico se i capigruppo sono gli stessi
    return this._gruppoSoggetto.sameCapigruppoByGruppi(GT0, gruppo);
  }

  /**
   * Funzione di comodo che aggiorna, per la tabella dei componenti del gruppo, l'id gruppo.
   * @param gruppo Gruppo come oggetto per l'aggiornamento dell'id.
   */
  private aggiornaIdGruppoTable(gruppo: Gruppo) {
    // Recupero l'id del gruppo
    const idG = gruppo?.id_gruppo_soggetto;

    // Verifico se esiste l'id e l'oggetto tabella
    if (idG != undefined && this.tableComponentiGruppo != undefined) {
      // Aggiorno l'informazione per l'id gruppo
      this.tableComponentiGruppo.idGruppo = idG;
    }
  }

  /**
   * ##################################################
   * FUNZIONI DI COMODO PER LA GESTIONE CRUD DEL GRUPPO
   * ##################################################
   */

  /**
   * Funzione che gestisce la tipologia di salvataggio per il gruppo.
   * Controlla se siamo in inserimento o modifica e salva il gruppo in input.
   * @param gruppoForm RiscaGruppo da salvare.
   */
  private gestioneSalvataggioGruppo(gruppoForm: RiscaGruppo) {
    // Controllo il tipo di modalità
    if (this.inserimento) {
      // Inserisco il gruppo
      this.inserisciGruppo(gruppoForm);
      // #
    } else if (this.modifica) {
      // Aggiorno il gruppo
      this.modificaGruppo(gruppoForm);
    }
  }

  /**
   * Funzione che effettua tutti i controlli sul soggetto per la gestione dall'azione da fare sul soggetto.
   * A seconda della modalità verranno gestiti i controlli in maniera diversa.
   * @param g SoggettoVo da gestire.
   * @param action AppActions che definisce il tipo d'azione per la gestione del soggetto.
   */
  private controllaPraticheGruppo(g: Gruppo, action: AppActions) {
    // Resetto l'alert
    this.resetAlertConfigs();

    // Verifico l'input
    if (!g || !g.id_gruppo_soggetto) {
      // Blocco il flusso
      return;
    }

    // Definisco variabili di comodo
    const isCanc = action === AppActions.cancellazione;
    const isMod = action == AppActions.modifica;
    // Verifico l'azione
    if (isCanc) {
      // Richiamo la funzione di gestione della cancellazione
      this.gestisciCPStDGCancellazione(g);
      // #
    } else if (isMod) {
      // Richiamo la funzione di gestione della modifica
      this.gestisciCPGModifica(g);
      // #
    }
  }

  /**
   * Funzione invocata a seguito del corretto salvataggio delle informazioni da parte del server.
   * @param gruppo Gruppo con i dati inseriti/modificati su DB.
   */
  private onGruppoSaved(gruppo: Gruppo) {
    // Aggiorno l'oggetto base del gruppo
    this.gruppoConfigs = gruppo;
    // Aggiorno l'id del gruppo all'interno della tabella
    this.aggiornaIdGruppoTable(gruppo);

    // Variabile di comodo
    const callerPratica = this.callerPratica;
    // A seconda del caller definisco la logica di gestione
    if (callerPratica && this.claimantDettaglioGruppo) {
      // Richiamo il back alla pratica
      this.tornaPratica();
      // Blocco il flusso
      return;
    }
    // A seconda del caller definisco la logica di gestione
    if (callerPratica && this.claimantCercaTitolare) {
      // Modifico la modalità del componente in modifica
      this.modalita = AppActions.modifica;
      // Gestisco il flusso della pagina
      this.informaGruppoSalvato();
      // Rilancio l'init dei dati per poter aggiornare le informazioni dei soggetti
      this.initGruppoConfigs(gruppo);
      // Blocco il flusso
      return;
    }

    // Variabile di comodo
    const callerAltri = this.callerHome || this.callerPraticheCollegate;
    // A seconda del caller definisco la logica di gestione
    if (callerAltri) {
      // Modifico la modalità del componente in modifica
      this.modalita = AppActions.modifica;
      // Gestisco il flusso della pagina
      this.informaGruppoSalvato();
      // Rilancio l'init dei dati per poter aggiornare le informazioni dei soggetti
      this.initGruppoConfigs(gruppo);
      // Blocco il flusso
      return;
    }
  }

  /**
   * Funzione che informa l'utente che il gruppo è stato salvato correttamente.
   */
  private informaGruppoSalvato() {
    // Definisco la lista messaggi per l'utente
    const datiInseriti = [];
    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.P001;
    // Recupero il codice per la comunicazione utente
    datiInseriti.push(this._riscaMessages.getMessage(code));

    // Variabili di comodo
    const a = this.alertConfigs;
    const m = datiInseriti;
    const s = RiscaInfoLevels.success;

    // Aggiorno la lista dei messaggi nel componente locale
    this.aggiornaAlertConfigs(a, m, s);
  }

  /**
   * ##########################################################################
   * FUNZIONI CHE DEFINISCO IL FLUSSO DATI PER LA FUNZIONALITA' DI: INSERIMENTO
   * ##########################################################################
   */

  /**
   * Effettua la chiamata al servizio per inserire il gruppo dato.
   * @param gruppoForm RiscaGruppo che definisce il gruppo da inserire.
   */
  private inserisciGruppo(gruppoForm: RiscaGruppo) {
    // Converto l'oggetto gruppoForm
    const g = this.convertRiscaGruppoToGruppo(gruppoForm);

    // Richiamo la funzione di modifica del soggetto
    this._gruppoSoggetto.insertGruppo(g).subscribe({
      next: (gruppo: Gruppo) => {
        // Lancio la gestione dei dati eliminati
        this.onGruppoSaved(gruppo);
        // #
      },
      error: (error: any) => {
        // Gestisco l'errore
        this.onServiziError(error);
        // #
      },
    });
  }

  /**
   * #######################################################################
   * FUNZIONI CHE DEFINISCO IL FLUSSO DATI PER LA FUNZIONALITA' DI: MODIFICA
   * #######################################################################
   */

  /**
   * Funzione che verifica se il gruppo definito come configurazione iniziale del componente, è stato effettivamente modificato.
   * @param gruppoForm RiscaGruppo modificato da verificare.
   */
  private modificaGruppo(gruppoForm: RiscaGruppo) {
    // Recupero i possibili dati di input di configurazione
    const gruppoConfigs = this.gruppoConfigs;
    // Converto l'oggetto gruppoForm
    const g = this._gruppo.convertRiscaGruppoToGruppo(
      gruppoForm,
      gruppoConfigs
    );

    // Variabili di comodo
    let GT0 = gruppoConfigs; // Dati gruppo a "T0" (iniziale)
    let GT1 = g; // Dati gruppo a "T1" (possibile modifiche)

    // Richiamo la funzione di check dei dati del gruppo
    const sameG = this._gruppoSoggetto.compareGruppi(GT0, GT1);
    // Verifico se i dati dei gruppi sono differenti
    if (!sameG) {
      // I dati del gruppo sono stati modificati sulla pagina, controllo le pratiche del gruppo
      this.controllaPraticheGruppo(g, AppActions.modifica);
      // #
    } else {
      // Resetto possibili messaggi in pagina per l'utente
      this.aggiornaAlertConfigs(this.alertConfigs);
      // Variabili di comodo
      const a = this.alertConfigs;
      const c = RiscaNotifyCodes.I015;
      const m = [this._riscaMessages.getMessage(c)];
      const i = RiscaInfoLevels.info;

      // Imposto un timeout per la gestione della digest di Angular
      setTimeout(() => {
        // Richiamo la funzione di gestione dell'alert
        this.aggiornaAlertConfigs(a, m, i);
      });
      // #
    }
  }

  /**
   * Funzione di supporto per la gestione del controllo pratiche gruppo, azione: Modifica.
   * Dalla documentazione i controlli saranno:
   * - Controllo presenza GRUPPI associati al soggetto e associati alla RISCOSSIONE;
   * - Controllo presenza GRUPPI associati al soggetto e associati ad uno o più STATI DEBITORI;
   * @param gruppo Gruppo da gestire.
   */
  private gestisciCPGModifica(gruppo: Gruppo) {
    // Verifico se il capogruppo è diverso rispetto al dato iniziale che ha compilato la pagina del gruppo
    const sameCG = this.sameCapigruppoByGruppi(gruppo);
    // Definisco il tipo d'azione per il controllo
    const idTOper = VerifyIndTipiOperazioni.update;

    // Effettuo la verfica delle pratiche del soggetto
    this._gruppo
      .controllaPStDGruppoModifica(
        gruppo,
        idTOper,
        sameCG,
        this.idPraticaPerVerify
      )
      .subscribe({
        next: (verifica: IResultVerifyRStDDettaglio) => {
          // Verifico se ho trovato le pratiche e definisco il messaggio
          if (verifica.isObjCollegato) {
            // Recupero il messaggio con placeholder
            const m = this.messaggioControlRStDGModifica(gruppo, verifica);
            // Richiamo la funzione di conferma modifica con uno specifico messaggio
            this.confermaModificaGruppo(gruppo, m);
            // #
          } else {
            // Richiamo la funzione di modifica del soggetto direttamente
            this.chiamaModificaGruppo(gruppo);
            // #
          }
        },
        error: (error: any) => {
          // Gestisco l'errore
          this.onServiziError(error);
        },
      });
  }

  /**
   * Funzione di comodo che gestisce tutte le logiche di comunicazione all'utente per quanto riguarda le pratiche/stati debitori collegate al gruppo.
   * La gestione avverrà per l'operazione di: modifica.
   * @param gruppo Gruppo con l'oggetto delle informazioni del gruppo da verificare.
   * @param verifica IResultVerifyRStDDettaglio con l'oggetto di controllo generato dalla funzione. Contiene il risultato dei controlli.
   * @returns string contenente il messaggio da visualizzare all'utente.
   */
  messaggioControlRStDGModifica(
    gruppo: Gruppo,
    verifica: IResultVerifyRStDDettaglio
  ): string {
    // Variabili di comodo
    let code = '';

    // Verifico se i capigruppo sono gli stessi
    if (this.sameCapigruppoByGruppi(gruppo)) {
      // Utilizzo il codice per modifica gruppo senza modifica referente
      code = RiscaNotifyCodes.A012;
      // #
    } else {
      // Utilizzo il codice per modifica gruppo senza modifica referente
      code = RiscaNotifyCodes.A016;
      // #
    }

    // Recupero le informazioni per la pratica/st debitori
    const { dettaglio } = verifica || {};

    // Definisco le informazioni da andare a sostituire nel messaggio
    const info = this._riscaUtilities.verifyPStDInfo(dettaglio);

    // Definisco le configurazioni per la generazione dell'errore
    const phs = [info.quantita, info.label];
    // Recupero il messaggio con placeholder
    return this._riscaMessages.getMessageWithPlacholderByCode(code, phs);
  }

  /**
   * Funzione che gestisce la conferma di modifica di un soggetto.
   * @param g SoggettoVo da modificare.
   * @param msg string che definisce il messaggio da visualizzare all'utente.
   */
  private confermaModificaGruppo(g: Gruppo, msg: string) {
    // Definisco la funzione di callback nel caso in cui l'utente confermi la cancellazione della riga
    const onConfirm = () => {
      // Richiamo la funzione di modifica del soggetto
      this.chiamaModificaGruppo(g);
    };
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConferma(msg, { onConfirm });
  }

  /**
   * Effettua la chiamata al servizio per modificare il gruppo dato.
   * @param g Gruppo che definisce il gruppo da modificare.
   */
  private chiamaModificaGruppo(g: Gruppo) {
    // Richiamo la funzione di modifica del soggetto
    this._gruppoSoggetto.updateGruppo(g).subscribe({
      next: (gruppo: Gruppo) => {
        // Lancio la gestione dei dati eliminati
        this.onGruppoSaved(gruppo);
        // #
      },
      error: (error: any) => {
        // Gestisco l'errore
        this.onServiziError(error);
        // #
      },
    });
  }

  /**
   * ############################################################################
   * FUNZIONI CHE DEFINISCO IL FLUSSO DATI PER LA FUNZIONALITA' DI: CANCELLAZIONE
   * ############################################################################
   */

  /**
   * Funzione di supporto per la gestione del controllo pratiche gruppo, azione: Cancellazione.
   * Dalla documentazione i controlli saranno:
   * - Controllo presenza GRUPPI associati al soggetto e associati alla RISCOSSIONE;
   * - Controllo presenza GRUPPI associati al soggetto e associati ad uno o più STATI DEBITORI;
   * @param gruppo Gruppo da verificare.
   */
  private gestisciCPStDGCancellazione(gruppo: Gruppo) {
    // Definisco il tipo d'azione per il controllo
    const idTOper = VerifyIndTipiOperazioni.delete;

    // Effettuo la verfica delle pratiche del soggetto
    this._gruppo
      .controllaPStDGruppoElimina(gruppo, idTOper, this.idPraticaPerVerify)
      .subscribe({
        next: (verifica: IResultVerifyRStDDettaglio) => {
          // Verifico se sono state trovate pratiche
          if (verifica.isObjCollegato) {
            // Richiamo la funzione di gestione
            this.onGruppoAssociatoRStD(verifica);
            // #
          } else {
            // Richiamo la funzione di conferma cancellazione con uno specifico messaggio
            this.confermaRimuoviGruppo(gruppo, RiscaNotifyCodes.A011);
          }
        },
        error: (error: any) => {
          // Gestisco l'errore
          this.onServiziError(error);
        },
      });
  }

  /**
   * Funzione di comodo che gestisce la segnalazione per un gruppo collegato a pratiche/stati debitori.
   * @param verifica IResultVerifyRStDDettaglio con le informazioni di dettaglio.
   */
  private onGruppoAssociatoRStD(verifica: IResultVerifyRStDDettaglio) {
    // Recupero il messaggio con placeholder
    const mi = [this._gruppo.messaggioControlRStDGElimina(verifica)];
    // Variabili di comodo
    const a = this.alertConfigs;
    const d = RiscaInfoLevels.danger;
    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(a, mi, d);
  }

  /**
   * Funzione che gestisce la conferma di cancellazione di un gruppo.
   * @param g Gruppo da cancellare.
   * @param msgCode string che definisce il codice per il messaggio da visualizzare all'utente.
   */
  private confermaRimuoviGruppo(g: Gruppo, msgCode: string) {
    // Definisco la funzione di callback nel caso in cui l'utente confermi la cancellazione della riga
    const onConfirm = () => {
      this.chiamaRimuoviGruppo(g);
    };
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConfermaWithCode(msgCode, { onConfirm });
  }

  /**
   * Chiama direttamente il servizio per eliminare il soggetto fornito
   * @param g soggetto da cancellare
   */
  private chiamaRimuoviGruppo(g: Gruppo) {
    // Recupero l'id_soggetto del possibile soggetto presente nel componente
    const idSoggetto = g?.id_gruppo_soggetto;

    // Richiamo la funzione di cancellazione del recapito alternativo
    this._gruppoSoggetto.deleteGruppo(idSoggetto).subscribe({
      next: (deleted: boolean) => {
        // Lancio la gestione dei dati eliminati
        this.onGruppoDeleted(g);
      },
      error: (error: any) => {
        // Gestisco l'errore
        this.onServiziError(error);
      },
    });
  }

  /**
   * Funzione invocata a seguito della corretta cancellazione delle informazioni da parte del server.
   * @param gruppoEliminato Gruppo con i dati cancellati su DB.
   */
  private onGruppoDeleted(gruppoEliminato: Gruppo) {
    // Definisco la lista messaggi per l'utente
    const datiEliminati = [];
    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.P002;
    // Recupero il codice per la comunicazione utente
    datiEliminati.push(this._riscaMessages.getMessage(code));

    // Rimuovo l'oggetto di configurazione
    this.gruppoConfigs = undefined;
    // La modalità viene impostata a inserimento
    this.modalita = AppActions.inserimento;

    // Variabili di comodo
    const a = this.alertConfigs;
    const m = datiEliminati;
    const s = RiscaInfoLevels.success;

    // Aggiorno la lista dei messaggi nel componente locale
    this.aggiornaAlertConfigs(a, m, s);

    // Reset della form
    this.onFormReset();
  }

  /**
   * ##########################
   * GESTIONE DELLA NAVIGAZIONE
   * ##########################
   */

  /**
   * Funzione che attiva la route per ritornare alla pagina precedente.
   */
  tornaIndietro() {
    // Gestico la parte di "pulizia" del filo d'arianna per la scheda gruppo
    this.gestisciFiloArianna();

    // Verifico il caller
    if (this.callerHome) {
      // Ritorno alla home
      this.tornaHome();
      // #
    } else if (this.callerPratica) {
      // Ritorno alla pratica
      this.tornaPratica();
      // #
    } else if (this.callerPraticheCollegate) {
      // Ritorno alle pratiche collegate
      this.tornaPraticheCollegate();
    }
  }

  /**
   * Funzione di gestione del filo d'arianna.
   * Va a rimuovere l'ultimo segmento che definisce il dettaglio o l'inserimento gruppo.
   */
  private gestisciFiloArianna() {
    // Recupero i livelli di gestione per inserimento e dettaglio
    const insGFA: FALivello = this._riscaFA.nuovoGruppo;
    const dettGFA: FALivello = this._riscaFA.dettaglioGruppo;
    // Verifico se uno dei due livelli è presente nel filo d'arianna
    const segmentoIG = this._riscaFA.segmentoInFAByLivello(insGFA);
    const segmentoDG = this._riscaFA.segmentoInFAByLivello(dettGFA);
    // Definisco un oggetto singolo che conterrà solo un oggetto valorizzato (dei due) o undefined se non è stato trovato niente
    const segmento = segmentoIG || segmentoDG || undefined;

    // Tento di rimuovere il segmento dal filo d'arianna
    this._riscaFA.rimuoviSegmento(segmento);
  }

  /**
   * Funzione che effettua un reindirizzamento verso la pagina della home.
   */
  private tornaHome() {
    // Definisco i parametri per la navigazione
    let state: IHomeRouteParams;
    // Definisco i parametri per la pagina home
    state = {};
    // Richiamo la funzione di back del servizio di navigazione
    this._navigationHelper.stepBack({ stateTarget: state });
  }

  /**
   * Funzione che effettua un reindirizzamento verso la pagina della pratica.
   */
  private tornaPratica() {
    // Definisco i parametri per la pagina chiamante
    const state: IPraticaRouteParams = {
      navTarget: this.praticaNavTarget,
      pageTarget: this.praticaPageTarget,
    };

    // Creo l'oggetto con tutti i parametri per il setup dell'aggiornamento dati pratica
    const datiGruppo: IIPConfigs = {
      navTarget: RiscaIstanzePratica.datiAnagrafici,
    };

    // Verifico se il claimant è il dettaglio del gruppo
    if (this.claimantDettaglioGruppo) {
      // Aggiungo le informazioni da ritornare alla pagina
      datiGruppo.gruppoUpdate = this.gruppoConfigs;
    }

    // Definisco una proprietà specifica come configurazione dell'inserisci pratica
    state[PraticaRouteKeys.inserimento] = datiGruppo;

    // Richiamo la funzione di back del servizio di navigazione
    this._navigationHelper.stepBack({ stateTarget: state });
  }

  /**
   * Funzione che effettua un reindirizzamento verso la pagina delle pratiche collegate.
   */
  private tornaPraticheCollegate() {
    // Verifico se è stato cancellato il soggetto
    if (this.gruppoConfigs === undefined) {
      // Definisco la route verso la home
      const route = AppRoutes.home;
      // Aggiungo il soggetto cancellato
      let state: IHomeRouteParams = {};
      // Devo navigare verso la home
      this._navigationHelper.goToStep(route, { stateTarget: state });
      // #
    } else {
      // Definisco un oggetto di stepback vuoto, che può essere riempito sulla base di alcune condizioni
      let jToStep: IJToStep = null;
      // Il caller è pratiche collegate, per evitare il reset del filo d'arianna, passo un "inutile" oggetto con parametri, così da non attivare il reset di default del filo
      jToStep = { stateTarget: { avoidResetFiloArianna: true } };
      // Richiamo la funzione di back del servizio di navigazione
      this._navigationHelper.stepBack(jToStep);
    }
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per _gruppoConfigs.
   */
  get gruppoConfigs() {
    // Ritorno una copia dell'oggetto
    return clone(this._gruppoConfigs);
  }

  /**
   * Setter per _gruppoConfigs.
   */
  set gruppoConfigs(gruppoConfigs: Gruppo) {
    // Ritorno una copia dell'oggetto
    this._gruppoConfigs = gruppoConfigs;
  }

  /**
   * Getter che verifica se il caller è definito per 'home'.
   */
  get callerHome(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastCaller(AppCallers.home);
  }

  /**
   * Getter che verifica se il claimant è: inserimento gruppo/cerca titolare.
   */
  get claimantCercaTitolare(): boolean {
    // Definisco il claimant cerca titolare
    const claimant = AppClaimants.insertGruppo_cercaTitolare;
    // Verifico il claimant
    return this._navigationHelper.isLastClaimant(claimant);
  }

  /**
   * Getter che verifica se il claimant è: dettaglio gruppo/pratica.
   */
  get claimantDettaglioGruppo(): boolean {
    // Definisco il claimant cerca titolare
    const claimant = AppClaimants.dettaglioGruppo_pratica;
    // Verifico il claimant
    return this._navigationHelper.isLastClaimant(claimant);
  }

  /**
   * Getter per le configurazioni del pulante di salvataggio
   */
  get configBtnSalva(): RiscaButtonConfig {
    // Definisco la label del pulsante
    let label = 'SALVA';
    let codeAEA: string = this.AEAK_C.SALVA_GRUPPO;

    // Verifico se il caller è la pratica
    if (this.callerPratica) {
      // Verifico chi è il richiedente
      if (this.claimantDettaglioGruppo) {
        // Il richiedente è il dettaglio gruppo da pratica, aggiungo un extra
        label = `${label} E TORNA ALLA PRATICA`;
      }
    }

    // Ritorno la configurazione per il pulsante
    return { label, codeAEA };
  }

  /**
   * Getter che verifica se il caller è definito per 'pratica'.
   */
  get callerPratica(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastCaller(AppCallers.pratica);
  }

  /**
   * Getter che verifica se il caller è definito per 'pratiche/pratiche-collegate'.
   */
  get callerPraticheCollegate(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastCaller(AppCallers.praticheCollegate);
  }

  /**
   * Getter per il titolo della pagina.
   * A seconda della modalità del componente verrà visualizzato uno specifico titolo.
   */
  get titolo() {
    // Verifico esista la modalità
    if (!this.modalita) {
      return '';
    }

    // Verifico la modalità
    switch (this.modalita) {
      case AppActions.inserimento:
        return this.G_C.TITLE_INSERISCI_GRUPPO;
      case AppActions.modifica:
        return this.G_C.TITLE_GRUPPO;
      default:
        return '';
    }
  }

  /**
   * Getter che verifica che la ricerca sia stata effettuata e abbia dati anagrafici.
   */
  get checkRicerca() {
    // recupero i dati anagrafici
    const { soggetto } = this.ricerca?.ricercaSoggetto || {};
    // Verifico se la ricerca è stata effettuata
    const existR = soggetto !== undefined;

    // Ritorno la condizione di verifica
    return existR;
  }

  /**
   * Getter che verifica che le informazioni per la tabella del soggetto cercato siano valide.
   */
  get checkTableSR() {
    // Verifico che esista la tabella
    if (!this.tableSoggettiRicerca) {
      return false;
    }
    // Esiste la tabella, verifico che ci siano dati
    return this.tableSoggettiRicerca.source?.length > 0;
  }

  /**
   * Getter che verifica che le informazioni per la tabella dei componenti gruppo siano valide.
   */
  get checkTableCG() {
    // Verifico che esista la tabella
    if (!this.tableComponentiGruppo) {
      return false;
    }
    // Esiste la tabella, verifico che ci siano dati
    return this.tableComponentiGruppo.source?.length > 0;
  }

  /**
   * Getter che verifica se l'eliminazione dei soggetti è possibile.
   */
  get eliminazionePossibile() {
    // Verifico se siamo in modifica
    const isModifica = this.modifica;
    // Verifico che esista l'informazione di un gruppo da cancellare
    const existG = this.gruppoConfigs !== undefined;

    // Ritorno la combinazione dei flag
    return isModifica && existG;
  }

  /**
   * Getter che verifica se l'eliminazione dei soggetti è abilitata come funzionalità.
   */
  get eliminazioneAttiva() {
    // Recupero se la gestione è abilitata
    const isGestioneAbilitata = this.isGestioneAbilitata;
    // Verifico
    if (!isGestioneAbilitata) {
      return false;
    }

    // Verifico se il caller è pratica
    if (this.callerPratica) {
      // Per la pratica non bisogna poter cancellare
      return false;
    }

    // Controlli passati
    return true;
  }

  /**
   * Getter di comodo che restitusce un oggetto IJourneySnapshot per generare una snapshot del componente.
   */
  get snapshotConfigs(): IJourneySnapshot {
    return {
      source: this,
      mapping: GruppoConsts.SNAPSHOT_CONFIG.mapping,
      saveFunc: GruppoConsts.SNAPSHOT_CONFIG.saveFunc,
    };
  }

  /**
   * Getter di comodo per l'idPratica da passare al verify.
   */
  get idPraticaPerVerify() {
    return this.modalita == AppActions.modifica ? this.idPratica : undefined;
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

  /**
   * Getter che genera la condizione per disabilitare il form dell'indirizzo spedizione dei soggetti componente.
   * @returns boolean che definisce il flag di disabilitazione del form degli indirizzi di spedizione.
   */
  get isISFormDisabilitato() {
    // Verifico il flag di abilitazione per "isGestioneAbilitata"
    const disabledIGA = !this.isGestioneAbilitata;
    // Verifico il flag di abilitazione per "isGestioneAbilitata"
    const disabledGIA = !this.isGruppoAbilitato;
    // Verifico i flag di abilitazione per "accesso elementi app"
    const disabledAEA = this.AEA_GRDisabled;

    // Verifico i flag
    return disabledIGA || disabledGIA || disabledAEA;
  }

  /**
   * Getter di comodo che recupera le informazioni della nav attualmente attiva per la gestione della pratica.
   * Se non definita, il default sarà: RiscaAzioniPratica.inserimento.
   * @returns RiscaAzioniPratica con l'indicazione della nav per la gestione della pratica.
   */
  get praticaNavTarget(): RiscaAzioniPratica {
    // Dalle variabili locali, recupero un possibile configurazione per il ritorno sulla pratica
    const nav = this._praticaNavTarget;
    // Ritorno l'id della nav oppure il default nav "inserimento pratica"
    return nav ?? RiscaAzioniPratica.inserisciPratica;
  }

  /**
   * Getter di comodo che recupera le informazioni del target della pagina da caricare indipendentemente dalla nav.
   * Se la pagina nav target non è di ricerca allora viene ritornato un default: undefined.
   * @returns RiscaAzioniPratica con l'indicazione della pagina per la gestione della pratica.
   */
  get praticaPageTarget(): RiscaAzioniPratica {
    // Recupero il nav target
    const navTarget = this.praticaNavTarget;
    // Variabili di supporto per il check della nav
    const isRicSem = navTarget === RiscaAzioniPratica.ricercaSemplice;
    const isRicAva = navTarget === RiscaAzioniPratica.ricercaAvanzata;

    // Verifico il nav target
    if (isRicSem || isRicAva) {
      // Definisco la pagina da caricare
      return RiscaAzioniPratica.inserisciPratica;
    }

    // Non è una ricerca, ritorno undefined
    return undefined;
  }
}
