import {
  Component,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { clone, cloneDeep, intersectionBy } from 'lodash';
import { IRiscaTableACEvent } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.interfaces';
import { JourneyStep } from '../../../../core/classes/navigation-helper/navigation-helper.classes';
import { ComponenteGruppo } from '../../../../core/commons/vo/componente-gruppo-vo';
import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { IJsonWarning } from '../../../../core/services/http-helper/utilities/http-helper.interfaces';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import {
  DAComponentiGruppoTable,
  IDAComponentiGruppoTableConfigs,
} from '../../../../shared/classes/risca-table/dati-anagrafici/dati-anagrafici-componenti-gruppo.table';
import {
  DARecapitiAlternativiTable,
  IDARecapitiAlternativiTableConfigs,
} from '../../../../shared/classes/risca-table/dati-anagrafici/dati-anagrafici-recapiti-alternativi.table';
import { FormCercaSoggettoComponent } from '../../../../shared/components/form-cerca-soggetto/form-cerca-soggetto.component';
import { RiscaContattiComponent } from '../../../../shared/components/risca/risca-contatti/risca-contatti.component';
import { RiscaDatiSoggettoComponent } from '../../../../shared/components/risca/risca-dati-soggetto/risca-dati-soggetto.component';
import { FASegmento } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { RiscaFormParentAndChildComponent } from '../../../../shared/components/risca/risca-form-parent-and-child/risca-form-parent-and-child.component';
import { SetupFormSubmitHandlerListener } from '../../../../shared/components/risca/risca-form-parent/utilities/risca-form-parent.interfaces';
import { RiscaRecapitoModalComponent } from '../../../../shared/components/risca/risca-modals/risca-recapito-modal/risca-recapito-modal.component';
import { RiscaRecapitoComponent } from '../../../../shared/components/risca/risca-recapito/risca-recapito.component';
import { RiscaTableComponent } from '../../../../shared/components/risca/risca-table/risca-table.component';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RSEPostSoggettiIS } from '../../../../shared/consts/risca/errors/http-errors-handler';
import { IIndirizziSpedizioneModalConfig } from '../../../../shared/modals/risca-indirizzi-spedizione-modal/utilities/risca-indirizzi-spedizione-modal.interfaces';
import { RiscaSpinnerService } from '../../../../shared/services/risca-spinner.service';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import { RiscaServerErrorManagerService } from '../../../../shared/services/risca/risca-server-error-manager.service';
import {
  isRecapitoAlternativo,
  isRecapitoPrincipale,
  isDataValiditaAttiva,
} from '../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  AppClaimants,
  AppRoutes,
  DatiAnagrafici,
  IAlertCAFConfigs,
  IAlertConfigs,
  ICallbackDataModal,
  IFormCercaSoggetto,
  IResultVerifyRStDDettaglio,
  IRFCReqJourneyNavigation,
  IRiscaFormTreeParent,
  RiscaAlertConfigs,
  RiscaAzioniPratica,
  RiscaButtonConfig,
  RiscaContatto,
  RiscaDatiSoggetto,
  RiscaFormStatus,
  RiscaGestioneISModal,
  RiscaInfoLevels,
  RiscaRecapito,
  RiscaServerError,
} from '../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../shared/utilities/classes/errors-maps';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IApriModalConfigs } from '../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { IGruppoRouteParams } from '../../../soggetti/components/gruppo/utilities/gruppo.interfaces';
import { ISoggettoRouteParams } from '../../../soggetti/components/soggetto/utilities/soggetto.interfaces';
import { GruppoConsts } from '../../../soggetti/consts/gruppo/gruppo.consts';
import { SoggettoConsts } from '../../../soggetti/consts/soggetto/soggetto.consts';
import { GruppoService } from '../../../soggetti/services/gruppo/gruppo.service';
import { SoggettoService } from '../../../soggetti/services/soggetto/soggetto.service';
import { DatiAnagraficiConsts } from '../../consts/dati-anagrafici/dati-anagrafici.consts';
import { CercaTitolareModalChiusure } from '../../enums/cerca-titolare-modal/cerca-titolare-modal.enums';
import { ICTMCloseParams } from '../../interfaces/cerca-titolare-modal/cerca-titolare-modal.interfaces';
import {
  IAggiornaIndirizzoSpedizione,
  IInvalidISDataFromSoggetto,
} from '../../interfaces/indirizzi-spedizione/indirizzi-spedizione.interfaces';
import { GruppoSoggettoService } from '../../service/dati-anagrafici/gruppo-soggetto.service';
import { IndirizziSpedizioneUtilityService } from '../../service/dati-anagrafici/indirizzi-spedizione-utility/indirizzi-spedizione-utility.service';
import {
  DatiRecapitoFormConfigs,
  RecapitiService,
} from '../../service/dati-anagrafici/recapiti.service';
import { SoggettoDatiAnagraficiService } from '../../service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { SoggettoHTTPResponse } from '../../service/dati-anagrafici/soggetto-dati-anagrafici/utilities/soggetto-dati-anagrafici.interfaces';
import { ComuneVo } from '../../../../core/commons/vo/comune-vo';

@Component({
  selector: 'dati-anagrafici',
  templateUrl: './dati-anagrafici.component.html',
  styleUrls: ['./dati-anagrafici.component.scss'],
})
export class DatiAnagraficiComponent
  extends RiscaFormParentAndChildComponent<DatiAnagrafici>
  implements OnInit, OnChanges, OnDestroy
{
  /**
   * #######################
   * COSTANTI DEL COMPONENTE
   * #######################
   */
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts();
  /** Oggetto contenente i valori costanti per il componente soggetto. */
  S_C = SoggettoConsts;
  /** Oggetto contenente i valori costanti per il componente gruppo. */
  G_C = GruppoConsts;
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto con le costanti per la sezione dei dati anagrafici. */
  DA_C = new DatiAnagraficiConsts();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /**
   * #######################
   * CONFIGURAZIONI DI INPUT
   * #######################
   */
  /** Id della pratica. Può essere undefined. */
  @Input('idPratica') idPratica?: number;
  /** SoggettoVo per il pre-caricamento delle informazioni. */
  @Input('datiAnagrafici') datiAnagraficiPratica: DatiAnagrafici;
  /** IAlertConfigs contenente possibili messaggi da visualizzare all'utente all'avvio del componente. */
  @Input() comunicazioni: IAlertConfigs;

  /**
   * ###############################
   * RIFERIMENTI AI SOTTO COMPONENTI
   * ###############################
   */
  /** ViewChild per la gestione del form di ricerca componente di un gruppo. */
  @ViewChild('cercaComponenteGruppo') cercaCG: FormCercaSoggettoComponent;

  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaDatiSoggetto') riscaDatiSoggetto: RiscaDatiSoggettoComponent;
  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaRecapito') riscaRecapito: RiscaRecapitoComponent;
  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaContatti') riscaContatti: RiscaContattiComponent;

  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaDARecapitoAlt') riscaDARecapitoAlt: RiscaRecapitoComponent;
  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaDAContattiAlt') riscaDAContattiAlt: RiscaContattiComponent;

  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaTRecapitiAlt') riscaTRecapitiAlt: RiscaTableComponent<any>;

  /** ViewChild connesso al componente di ricerca soggett. */
  @ViewChild('resFormCercaSoggetto') formCS: FormCercaSoggettoComponent;

  /**
   * #########################################
   * DATI A SEGUITO DELLA RICERCA DEL TITOLARE
   * #########################################
   */
  /** Boolean per la visualizzazione dei dati anagrafici. */
  showDatiAnagrafici = false;
  /** Number che definisce l'id del recapito principale . */
  idRecapitoP: number;

  /**
   * #################################
   * INFORMAZIONI RECAPITI ALTERNATIVI
   * #################################
   */
  /** String che definisce la chiave di gestione per la composizione dati dei form per i reapiti alternativi. */
  raParentKey: string;
  /** Array di string che definisce la chiave di gestione per la composizione dati dei form per i reapiti alternativi. */
  raChildrenKeys: string[];
  /** RecapitoVo contenente i dati del recapito alternativo scelto per la connessione alla pratica.  */
  recapitoAP: RecapitoVo;

  /**
   * #############################
   * GESTIONE ACCORDION COMPONENTE
   * #############################
   */
  /** Boolean per la visualizzazione dei nuovi recapiti alternativi per l'utente. */
  showNuoviRecapitiAlternativi = false;

  /**
   * ################################
   * TABELLE IMPIEGATE NEL COMPONENTE
   * ################################
   */
  /** Oggetto DARecapitiAlternativiTable che conterrà le configurazioni per la tabella dei recapiti alternativi. */
  recapitiAlternativiTable: DARecapitiAlternativiTable;
  /** Oggetto DAComponentiGruppoTable che conterrà le configurazioni per la tabella dei componenti gruppo. */
  componentiGruppoTable: DAComponentiGruppoTable;

  /**
   * ############################################
   * COMUNICAZIONI ALL'UTENTE POSIZIONATO IN ALTO
   * ############################################
   */
  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente, per le comunicazioni extra. */
  alertCEConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /**
   * ################################
   * VARIABILI D'UTILITA' MISCELLANEE
   * ################################
   */
  /** SoggettoVo contenente le informazioni del soggetto della pratica, o che verrà associato alla pratica. */
  soggettoP: SoggettoVo;
  /** SoggettoVo contenente le informazioni del soggetto della pratica al suo stato iniziale. Verrà usato per verificare se è necessario aggiornare prima il soggetto. */
  soggettoPInit: SoggettoVo;
  /** SoggettoVo con le informazioni del soggetto generato dall'errore sugli indirizzi di spedizione. */
  soggettoISError: SoggettoVo;

  /** Gruppo contenente le informazioni del gruppo associato alla pratica. O che verrà associato alla pratica. */
  gruppoP: Gruppo;
  /** Lista dei campi modificati alla fonte da evidenziare in risca-dati-soggetto */
  fonteConfigs: string[] = [];
  /** Boolean che definisce lo stato di disabilitazione degli elementi del componente, per: inserisci/modifica soggetto. */
  AEA_pratica_DADisabled = false;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _formBuilder: FormBuilder,
    private _isUtility: IndirizziSpedizioneUtilityService,
    logger: LoggerService,
    private _gruppo: GruppoService,
    private _gruppoSoggetto: GruppoSoggettoService,
    navigationHelper: NavigationHelperService,
    private _recapiti: RecapitiService,
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaMessages: RiscaMessagesService,
    private _riscaModal: RiscaModalService,
    private _riscaSEM: RiscaServerErrorManagerService,
    riscaUtilities: RiscaUtilitiesService,
    private _soggetto: SoggettoService,
    private _soggettoDA: SoggettoDatiAnagraficiService,
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
    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponent();
  }

  ngOnChanges(changes: SimpleChanges) {
    // Variabile di comodo
    const changesDap = changes.datiAnagraficiPratica;
    // Verifico se è modificato il dato anagrafico del soggetto
    if (changesDap && !changesDap.firstChange) {
      // Verifico ed eventualmente aggiorno i dati per
      this.onChangesDatiAnagrafici(changesDap.currentValue);
    }
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Lancio la funzione di recupero delle configurazioni per l'abilitazione della pagina
    this.setupAEASoggetto();
    // Lancio la funzione di setup per le tabelle
    this.setupDATable();
    // Effettuo il setup del servizio
    this.setupFormSubmitHandlerService();
    // Richiamo il setup degli errori del form
    this.setupMainFormErrors();
  }

  /**
   * Funzione di init per le configurazioni relative alla pagina del soggetto.
   */
  setupAEASoggetto() {
    // Recupero la chiave per la configurazione della form
    const daKey = this.AEAK_C.PAGINA_DATI_ANAG;
    // Recupero la configurazione della form dal servizio
    this.AEA_pratica_DADisabled = this._accessoElementiApp.isAEADisabled(daKey);
  }

  /**
   * Funzione di setup delle tabelle del componente.
   */
  setupDATable() {
    // Richiamo la configurazione delle tabelle
    this.setupDARecapitiAlternativiTable();
    this.setupComponentiGruppiTable();
  }

  /**
   * Funzione di comodo che permette l'inizializzazione delle tabelle.
   * @param recapiti Array di RecapitoVo con i dati di complicazione della tabella.
   */
  private setupDARecapitiAlternativiTable(recapiti?: RecapitoVo[]) {
    // Variabile di comodo
    const isGestioneAbilitata = this.isGestioneAbilitata;
    const disableUserInputs = this.disableUserInputs;
    const idGruppo = this.gruppoP?.id_gruppo_soggetto;
    // Definisco gli oggetti di configurazione delle tabelle
    const cra: IDARecapitiAlternativiTableConfigs = {
      recapiti,
      isGestioneAbilitata,
      disableUserInputs,
      idGruppo,
    };
    // Lancio la configurazione della tabella
    this.recapitiAlternativiTable = new DARecapitiAlternativiTable(cra);
  }

  /**
   * Funzione di comodo che permette l'inizializzazione delle tabelle.
   * @param componenti Array di SoggettoVo con i dati di complicazione della tabella.
   */
  private setupComponentiGruppiTable(componenti?: SoggettoVo[]) {
    // Variabile di comodo
    const AEA_pratica_DADisabled = this.AEA_pratica_DADisabled;
    // Definisco gli oggetti di configurazione delle tabelle
    const ccg: IDAComponentiGruppoTableConfigs = {
      AEA_pratica_DADisabled,
      componenti,
    };
    // Lancio la configurazione della tabella
    this.componentiGruppoTable = new DAComponentiGruppoTable(ccg);
  }

  /**
   * Funzione di setup per il servizio RiscaFormSubmitHandlerService.
   */
  private setupFormSubmitHandlerService() {
    // Effettuo il setup per la gestione dei dati principali
    this.setupMainFormsSubmitHandler();
    // Effettuo il setup per la gestione dei recapiti alternativi
    this.setupRAFormsSubmitHandler();
  }

  /**
   * Funzione che gestisce il setup del servizio: RiscaFormSubmitHandlerService; per il gruppo di forms principale.
   */
  private setupMainFormsSubmitHandler() {
    // Definisco la chiave per il parent
    const parent = this.DA_C.FORM_KEY_PARENT_DA;
    // Definisco le chiavi per i figli
    const children = [
      this.DA_C.FORM_KEY_CHILD_DA_DATI_SOGGETTO,
      this.DA_C.FORM_KEY_CHILD_DA_RECAPITO,
      this.DA_C.FORM_KEY_CHILD_DA_CONTATTI,
    ];

    // Definisco le funzioni di gestione dei listener sui form da overridare
    const customListener: SetupFormSubmitHandlerListener = {
      onFormsValid: (fv) => {
        // Richiamo la funzione del componente
        this.onServiceFormsValid(fv);
      },
      onFormsInvalid: (fi) => {
        // Richiamo la funzione del componente
        this.onServiceFormsInvalid(fi);
      },
    };

    // Richiamo il super
    this.setupFormsSubmitHandler(parent, children, customListener);
  }

  /**
   * Funzione che gestisce il setup del servizio: RiscaFormSubmitHandlerService; per il gruppo di forms recapiti alternativi.
   */
  private setupRAFormsSubmitHandler() {
    // Assegno localmente le chiavi
    this.raParentKey = this.DA_C.FORM_KEY_PARENT_DA_RECAPITI_ALTERNATIVI;
    this.raChildrenKeys = [
      this.DA_C.FORM_KEY_CHILD_DA_RECAPITO_ALTERNATIVO,
      this.DA_C.FORM_KEY_CHILD_DA_CONTATTI_ALTERNATIVO,
    ];

    // Aggiungo il nuovo albero all'interno, passando da un reset dei dati
    this._riscaFormSubmitHandler.resetFormTree(
      this.raParentKey,
      this.raChildrenKeys
    );
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [...this.EM.MAP_FORM_GROUP_REQUIRED];
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  private initComponent() {
    // Lancio il setup della form del componente (usato come struttura d'appoggio)
    this.initForms();
    // Lancio l'init dei messaggi informativi sulla pratica
    this.initComunicazioniPratica();
    // Verifico se è stato passato un oggetto per il pre-caricamento
    this.initDatiAnagraficiPratica();
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup della struttura della form principale
    this.initFormDatiAnagrafici();
  }

  /**
   * Setup del form: mainForm.
   */
  private initFormDatiAnagrafici() {
    this.mainForm = this._formBuilder.group({
      soggettoPratica: new FormControl(null, {
        validators: [Validators.required],
      }),
      gruppoPratica: new FormControl(),
      recapitiPratica: new FormControl(null, {
        validators: [Validators.required],
      }),
    });
    this.mainForm.markAsPristine();
  }

  /**
   * Funzione di init per le comunicazioni sulla pratica.
   */
  private initComunicazioniPratica() {
    // Verifico se ci sono comunicazioni per l'utente
    if (this.comunicazioni) {
      // Aggiorno l'alert per le comunicazioni
      this.aggiornaAlertConfigs(
        this.alertCEConfigs,
        this.comunicazioni.messaggi,
        this.comunicazioni.tipo
      );
    }
  }

  /**
   * Funzione di init per i dati passati al componente come Input.
   */
  private initDatiAnagraficiPratica() {
    // Definisco variabili di comodo
    const soggetto = this.datiAnagraficiPratica?.soggettoPratica;
    const gruppo = this.datiAnagraficiPratica?.gruppoPratica;

    // Lancio il set per le informazioni del componente
    this.impostaDatiComponente(soggetto, gruppo);
  }

  /**
   * ###################
   * FUNZIONI DI CHANGES
   * ###################
   */

  /**
   * Funzione adibita al check e alla modifica dei dati anagrafici della pratica.
   * @param datiAnagrafici DatiAnagrafici per l'aggiornamento della pagina
   */
  private onChangesDatiAnagrafici(datiAnagrafici: DatiAnagrafici) {
    // Resetto il flag visibilita
    this.showDatiAnagrafici = false;
    // Lancio uno spinner di caricamento
    this._spinner.show();
    // Imposto un timeout e aggiorno i dati
    setTimeout(() => {
      // Recupero il nuovo soggetto e gruppo
      const soggetto = datiAnagrafici.soggettoPratica;
      const gruppo = datiAnagrafici.gruppoPratica;
      // Aggiorno i dati del componente
      this.impostaDatiComponente(soggetto, gruppo);
      // Blocco lo spinner
      this._spinner.hide();
    });
  }

  /**
   * #####################
   * GESTIONE DEGLI EVENTI
   * #####################
   */

  /**
   * Funzione specifica di gestione del warning per il salvataggio su SCRIVA fallito a causa del codice fiscale.
   * @param jsonWarn IJsonWarning con l'oggetto generato per il warning
   */
  private handleJsonWarningCFRisca(jsonWarn: IJsonWarning) {
    // Verifico l'input
    if (!jsonWarn || jsonWarn.code != RiscaNotifyCodes.I025) {
      // nessuna segnalazione
      return;
    }

    // Recupero il codice dall'oggetto generato
    const code = jsonWarn.code;
    // Creo un alert specifico che informi l'utente
    const infoAlert = this._riscaAlert.createAlertFromMsgCode({ code });
    // Aggiorno l'alert impostando la chiusura automatica
    infoAlert.allowAlertClose = true;
    // Aggiorno l'alert
    this.alertConfigs = infoAlert;
  }

  /**
   * ##############################################################
   * FUNZIONI COLLEGATE AL SERVZIO PER GLI EVENTI DEI FORM CHILDREN
   * ##############################################################
   */

  /**
   * Funzione personalizzata passata al componente padre per la gestione dei listener del servizio: RiscaFormSubmitHandlerService.
   * La funzione verrà associata la listener: onFormsValid; del servizio sopra citato.
   * @param formsValid IRiscaFormTreeParent con le informazioni dei form in success.
   */
  private onServiceFormsValid(formsValid: IRiscaFormTreeParent) {
    // Verifico se i dati sono riferiti ad una chiave padre di questo componente
    const mainData = formsValid[this.parentKey];
    const raData = formsValid[this.raParentKey];

    // Verifico L'oggetto e lancio la funzione associata
    if (mainData) {
      // Richiamo la funzione di gestione del form principale
      this.formsValid(formsValid);
      // #
    } else if (raData) {
      // Richiamo la funzione di gestione per i recapiti alternativi
      this.formsRAValid(formsValid);
      // #
    }
  }

  /**
   * Funzione personalizzata passata al componente padre per la gestione dei listener del servizio: RiscaFormSubmitHandlerService.
   * La funzione verrà associata la listener: onFormsInvalid; del servizio sopra citato.
   * @param formsInvalid IRiscaFormTreeParent con le informazioni dei form in error.
   */
  private onServiceFormsInvalid(formsInvalid: IRiscaFormTreeParent) {
    // Verifico se i dati sono riferiti ad una chiave padre di questo componente
    const mainData = formsInvalid[this.parentKey];

    // Verifico L'oggetto e lancio la funzione associata
    if (mainData) {
      // Richiamo la funzione di gestione del form principale
      this.formsInvalid(formsInvalid);
      // #
    }
  }

  /**
   * ################################
   * GESTIONE MODALE RICERCA TITOLARE
   * ################################
   */

  /**
   * Funzione per l'apertura del modal "ricerca titolare".
   */
  apriCercaTitolare() {
    // Definisco la callback di success
    const onConfirm = (callback: ICTMCloseParams) => {
      // Richiamo la funzione di gestione dei dati
      this.onCTMClose(callback);
    };

    // Richiamo il servizio per l'apertura del modale
    this._soggettoDA.apriCercaTitolareModal({ onConfirm });
  }

  /**
   * Funzione che gestisce le logiche alla chiusura della modale per la ricerca di un titolare per la pratica.
   * @param params ICTMCloseParams con le informazioni di ritorno.
   */
  private onCTMClose(params: ICTMCloseParams) {
    // Recupero dall'input le informazioni
    const { action, data } = params || {};

    // Verifico il tipo di callback
    switch (action) {
      case CercaTitolareModalChiusure.aggiungiSoggetto:
        // Richiamo la funzione di gestione inserimento soggetto
        this.richiestaAggiungiSoggetto(data);
        break;
      // #
      case CercaTitolareModalChiusure.aggiungiGruppo:
        // Richiamo la funzione di gestione inserimento gruppo
        this.richiestaAggiungiGruppo();
        break;
      // #
      case CercaTitolareModalChiusure.titolareScelto:
        // Richiamo la funzione di gestione dati del titolare
        this.gestisciDatiAnagraficiTitolare(data);
        break;
      // #
    }
  }

  /**
   * Funzione che gestisce i dati nel momento in cui viene richiesto l'inserimento di un nuovo soggetto titolare.
   * @param data IFormCercaSoggetto che identifica i dati del titolare cercato, ma non trovato.
   */
  private richiestaAggiungiSoggetto(data: IFormCercaSoggetto) {
    // Definisco un oggetto per l'aggiornamento del filo d'arianna
    const cercaTFA = this._riscaFA.cercaTitolare;
    const insSFA = this._riscaFA.nuovoSoggetto;
    // Creo un segmento con i livelli
    let segmento: FASegmento;
    segmento = this._riscaFA.creaSegmentoByLivelli(cercaTFA, insSFA);

    // Definisco una possibile partita iva
    let partitaIva: string;
    // Verifico se il codice fiscale è una partita iva
    if (this._riscaUtilities.controllaPartitaIVA(data.codiceFiscale)) {
      // Il codice fiscale passato è in realtà una partita iva
      partitaIva = data.codiceFiscale;
    }

    // Creo un oggetto SoggettoVo parziale con le informazioni del codice fiscale e tipo soggetto
    const datiAnagrafici: SoggettoVo = {
      id_ambito: this._user.idAmbito,
      cf_soggetto: data.codiceFiscale,
      tipo_soggetto: data.tipoSoggetto,
      partita_iva_soggetto: partitaIva,
    };
    // Definisco lo state da passare alla pagina
    const state: ISoggettoRouteParams = {
      soggetto: datiAnagrafici,
      modalita: AppActions.inserimento,
      praticaNavTarget: this.praticaNavTarget,
    };

    // Definisco l'oggetto di configurazione per la richiesta di navigazione
    const config: IRFCReqJourneyNavigation = {
      route: AppRoutes.soggetto,
      extras: { state },
      claimant: AppClaimants.insertSoggetto,
      sameRoute: false,
      jStepTarget: this.S_C.NAVIGATION_CONFIG,
      segmentoFA: segmento,
    };

    // Richiamo la funzione per la richiesta della snapshot
    this.navigaJourney(config);
  }

  /**
   * Funzione che gestisce i dati nel momento in cui viene richiesto l'inserimento di un nuovo gruppo.
   * La funzione apre, senza passare informazioni specifiche, la scheda del gruppo, in modalità inserimento.
   */
  private richiestaAggiungiGruppo() {
    // Definisco un oggetto per l'aggiornamento del filo d'arianna
    const cercaTFA = this._riscaFA.cercaTitolare;
    const insGFA = this._riscaFA.nuovoGruppo;
    // Creo un segmento con i livelli
    let segmento: FASegmento;
    segmento = this._riscaFA.creaSegmentoByLivelli(cercaTFA, insGFA);

    // Definisco lo state da passare alla pagina
    const state: IGruppoRouteParams = {
      modalita: AppActions.inserimento,
      praticaNavTarget: this.praticaNavTarget,
    };

    // Definisco l'oggetto di configurazione per la richiesta di navigazione
    const config: IRFCReqJourneyNavigation = {
      route: AppRoutes.gruppo,
      claimant: AppClaimants.insertGruppo_cercaTitolare,
      extras: { state },
      sameRoute: false,
      jStepTarget: this.G_C.NAVIGATION_CONFIG,
      segmentoFA: segmento,
    };

    // Richiamo la funzione per la richiesta della snapshot
    this.navigaJourney(config);
  }

  /**
   * Funzione che gestisce i dati nel momento in cui viene selezionato un titolare.
   * @param data IFormCercaSoggetto che identifica i dati del titolare trovato.
   */
  private gestisciDatiAnagraficiTitolare(data: IFormCercaSoggetto) {
    // Resetto possibili alert comunicazioni
    this.aggiornaAlertConfigs(this.alertCEConfigs);

    // Definisco una funzione locale che gestisce i dati del referente
    const updateDataComponent = () => {
      // Assegno localmente i dati del titolare
      const { gruppoSelezionato } = data || {};
      const { soggetto } = data.ricercaSoggetto || {};

      // Aggiorno i dati del componente
      this.impostaDatiComponente(soggetto, gruppoSelezionato);
      // Se ho dei campi modificati, li evidenzio
      this.evidenziaCampiModificati(data);
    };

    // Verifico se esistono già dei dati del referente
    if (this.soggettoP) {
      // Resetto il flag visibilita
      this.showDatiAnagrafici = false;
      // Lancio uno spinner di caricamento
      this._spinner.show();
      // Imposto un timeout e aggiorno i dati
      setTimeout(() => {
        // Blocco lo spinner
        this._spinner.hide();
        // Aggiorno i dati
        updateDataComponent();
      });
      // #
    } else {
      // Aggiorno i dati del componente
      updateDataComponent();
    }
  }

  /**
   * #######################################################################
   * FUNZIONE DI GESTIONE DELLA SEGNALAZIONE DEI CAMPI AGGIORNATI ALLA FONTE
   * #######################################################################
   */

  /**
   * Apre l'alert info in cima alla pagina e mostra i campi aggiornati dalla fonte, recuperati mediante ricerca del soggetto.
   * @param ricerca IFormCercaSoggetto con le informazioni della ricerca del soggetto.
   */
  private evidenziaCampiModificati(ricerca: IFormCercaSoggetto) {
    // Ottengo i campi aggiornati dalla fonte dall'oggetto del BE
    const caf = ricerca?.ricercaSoggetto?.lista_campi_aggiornati || [];

    // Assegno i campi aggiornati dalla fonte in una variabile del componente.
    this.fonteConfigs = caf;

    // Verifico che ci siano elementi
    if (caf.length === 0) {
      // Blocco il flusso
      return;
    }

    // Genero l'input per la configurazione dell'alert
    const c: IAlertCAFConfigs = {
      alertConfigs: this.alertCEConfigs,
      listaCampiAggiornati: caf,
    };
    // Richiamo la funzione
    const newAlert = this._soggettoDA.alertCampiAggiornatiFonte(c);
    // Verifico sia stata generata una configurazione
    if (newAlert) {
      // Aggiorno la struttura dell'alert
      this.alertCEConfigs = newAlert;
    }
  }

  /**
   * ##################################################################################
   * FUNZIONI DI SET DELLE INFORMAZIONI LEGATE AI DATI ANAGRAFICI DEL SOGGETTO E GRUPPO
   * ##################################################################################
   */

  /**
   * Funzione di comodo che permette di gestione i dati riguardanti soggetto e gruppo.
   * La funzione ha una logica specifica: essendo che il set dei dati del gruppo, influenzano il set dei dati del soggetto, il set dei dati del gruppo devono avvenire PRIMA di quelli del soggetto.
   * Principalmente questo avviene perché il set dei dati del soggetto, prevede la gestione dei recapiti alternativi, che però sono affetti dalla possibilità di presenza di un gruppo.
   * @param soggetto SoggettoVo contenente i dati del soggetto trovato/configurato.
   * @param gruppo Gruppo per il caricamento dei componenti del gruppo.
   */
  private impostaDatiComponente(soggetto: SoggettoVo, gruppo: Gruppo) {
    // Lancio prima il set dei dati del gruppo
    this.impostaGruppoComponente(gruppo);
    // Poi lancio il set dei dati del soggetto
    this.impostaDAComponente(soggetto);
  }

  /**
   * #########################################################################
   * FUNZIONI DI SET DELLE INFORMAZIONI LEGATE AI DATI ANAGRAFICI DEL SOGGETTO
   * #########################################################################
   */

  /**
   * Funzione di setup per le informazioni recuperate dalla ricerca del titolare.
   * @param soggetto SoggettoVo contenente i dati del soggetto trovato/configurato.
   */
  private impostaDAComponente(soggetto: SoggettoVo) {
    // Verifico l'input
    if (!soggetto) {
      // Blocco il flusso
      return;
    }

    // Aggiorno informazioni e strutture componente
    this.aggiornaComponenteDA(soggetto);
    // Aggiorno i dati delle tabelle
    this.setupRecapitiAlternativiTable(soggetto);
  }

  /**
   * Funzione di supporto per l'aggiornamento dei dati anagrafici e delle strutture dei componenti.
   * @param da SoggettoVo d'aggiornare.
   */
  private aggiornaComponenteDA(da: SoggettoVo) {
    // Assegno localmente i dati del titolare
    this.soggettoP = da;
    // Creo una copia iniziale per il soggetto della pratica
    this.soggettoPInit = clone(da);

    // Recupero l'id_recapito principale dai dati anagrafici
    const { id_recapito } =
      this._recapiti.getRecapitoPrincipaleFromSoggettoVo(da) || {};
    // Imposto l'id recapito (può essere undefined)
    this.idRecapitoP = id_recapito;

    // Abilito la visualizzazione dei dati anagrafici
    this.showDatiAnagrafici = true;
  }

  /**
   * Funzione di supporto per la generazione della tabella riguardante: recapiti alternativi.
   * @param soggetto SoggettoVo per il setup delle informazioni.
   */
  private setupRecapitiAlternativiTable(soggetto?: SoggettoVo) {
    // Verifico l'input
    if (!soggetto) {
      // Inizializzo la tabella senza dati
      this.setupDARecapitiAlternativiTable();
      // #
    } else {
      // Lancio la funzione di setup dei dati dato il soggetto
      this.setupRecapitiAlternativiTableBySoggetto(soggetto);
      // #
    }
  }

  /**
   * Funzione di supporto per la generazione della tabella riguardante: recapiti alternativi.
   * La funzione gestirà le informazioni del soggetto in input e preparerà le informazioni rispetto a quello del soggetto in input.
   * @param soggetto SoggettoVo per il setup delle informazioni.
   */
  private setupRecapitiAlternativiTableBySoggetto(soggetto: SoggettoVo) {
    // Estraggo i recapiti alternativi
    let recapitiAlt: RecapitoVo[];
    recapitiAlt = this._recapiti.getRecapitiAlternativiFromSoggettoVo(soggetto);
    // Genero la tabella con i dati dei recapiti alternativi
    this.setupDARecapitiAlternativiTable(recapitiAlt);

    // Recupero il soggetto passato e impostato come di default se esiste
    const soggettoPratica: SoggettoVo =
      this.datiAnagraficiPratica?.soggettoPratica;
    // Verifico se il soggetto della pratica impostato esiste
    const existSoggPrat: boolean = soggettoPratica != undefined;
    // Verifico se il soggetto della pratica è lo stesso usato per il set dati
    const sameSoggPratAndInput: boolean =
      existSoggPrat && soggettoPratica.cf_soggetto === soggetto.cf_soggetto;

    // Verifico se sto cercando di settare i dati dello stesso soggetto della pratica
    if (sameSoggPratAndInput) {
      // Recupero da dati anagrafici RecapitiPratica, verifico che esista un recapito alternativo: se esite, selezionarlo di default nella tabella
      const recapitoAPratica =
        this.datiAnagraficiPratica?.recapitiPratica?.find((r: RecapitoVo) => {
          // Ritorno il check sul recapito alternativo
          return isRecapitoAlternativo(r);
          // #
        });

      // Definisco la funzione per la selezione dell'elemento principale
      const f = (r: RecapitoVo) => {
        // Uso la funzione di supporto per definire il selected
        return this.tableRecapitoAlternativoSelected(r, recapitoAPratica);
      };
      // Provo ad impostare il recapito alternativo selezionato rispetto agli elementi che sono stati settati nella tabella
      const selected =
        this.recapitiAlternativiTable.setElementSelectedExclusive(f);
      // Se è stato selezionato, definisco il recapito come selezionato
      if (selected) {
        // Imposto a livello di componente il recapito alternativo
        this.recapitoAP = recapitoAPratica;
      }
      // #
    } else {
      // I soggetti sono diversi, per sicurezza resette le informazioni per il recapito alternativo della pratica
      this.recapitoAP = undefined;
      // #
    }
  }

  /**
   * Funzione di supporto che serve a definire le logiche di selezione della riga attiva per la tabella.
   * @param recapito RecapitoVo per  la verifica/comparazione.
   * @param recapitoPratica RecapitoVo per  la verifica/comparazione.
   * @returns boolean che definisce la logica di selected.
   */
  private tableRecapitoAlternativoSelected(
    recapito: RecapitoVo,
    recapitoPratica: RecapitoVo
  ): boolean {
    // Verifico l'input
    if (!recapito || !recapitoPratica) {
      // Non è possibile matchare
      return false;
    }

    // Verifico se è il recapito è alternativo
    const isRA = isRecapitoAlternativo(recapito);
    // Verifico se è lo stesso recapito
    const sameR = recapito.id_recapito === recapitoPratica.id_recapito;

    // Verifico le condizioni
    return isRA && sameR;
  }

  /**
   * ####################################################################################
   * FUNZIONI DI SET DELLE INFORMAZIONI LEGATE AI GRUPPI DEI DATI ANAGRAFICI DEL SOGGETTO
   * ####################################################################################
   */

  /**
   * Funzione di setup per le informazioni del gruppo nel componente.
   * @param gruppo Gruppo per il caricamento dei componenti del gruppo.
   */
  private impostaGruppoComponente(gruppo: Gruppo) {
    // Assegno localmente i dati
    this.gruppoP = gruppo;

    // Recupero i componenti del gruppo
    const componentiGruppo = gruppo?.componenti_gruppo || [];
    // Aggiorno i dati delle tabelle
    this.setupComponentiGruppoTable(componentiGruppo);
  }

  /**
   * Funzione di supporto per la generazione della tabella riguardante: componenti gruppo.
   * @param componentiGruppo ComponenteGruppo con i componenti del gruppo per il setup.
   */
  private setupComponentiGruppoTable(componentiGruppo: ComponenteGruppo[]) {
    // Inizializzo la tabella senza dati
    this.setupComponentiGruppiTable();

    // Effettuo un check sull'input
    const existCG = componentiGruppo != undefined;
    const existSCG = existCG && componentiGruppo.length > 0;
    // Verifico l'input
    if (!existSCG) {
      // Niente da visualizzare
      return;
    }

    // Avvio lo spinner
    this._spinner.show();

    // Richiamo la funzione del servizio per lo scarico dei dettagli
    this._gruppo.getDettaglioComponentiGruppo(componentiGruppo).subscribe({
      next: (dettagli: SoggettoVo[]) => {
        // Richiamo la funzione per la gestione dei dettagli componenti
        this.onDettaglioComponentiGruppo(componentiGruppo, dettagli);
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
    // Effettuo un check sull'input
    const existCG = componentiGruppo != undefined;
    const existSCG = existCG && componentiGruppo.length > 0;

    // Verifico che i parametri esistano
    if (!existSCG || !dettagli) {
      // Nascondo lo spinner
      this._spinner.hide();
      // Blocco il flusso
      return;
    }

    // Imposto il capogruppo a livello di componente
    const capogruppo = componentiGruppo.find((c: ComponenteGruppo) => {
      // Verifico se è il capogruppo
      return c.flg_capo_gruppo;
    });

    // Definisco variabili di comodo
    const mergeProperty = 'id_soggetto';
    const mergeCG = componentiGruppo as any;
    const mergeD = dettagli as any;
    const componentiDettaglio = intersectionBy(mergeD, mergeCG, mergeProperty);

    // Rimuovo possibili valori sporchi
    this.componentiGruppoTable.clear();
    // Effettuo l'aggiunta di ogni elemento all'interno dell'array
    this.componentiGruppoTable.addElements(componentiDettaglio);
    // Una volta terminata l'agginta di tutti gli elementi imposto il selezionato per default
    this.componentiGruppoTable.setElementSelectedExclusive((s) => {
      // Uso la funzione di supporto per definire il selected
      return s?.id_soggetto === capogruppo?.id_soggetto;
    });
    // Richiamo il sort iniziale per i dati della tabella
    this._gruppo.sortTableByCapogruppo(this.componentiGruppoTable);

    // Nascondo lo spinner
    this._spinner.hide();
  }

  /**
   * Funzione che genera un oggetto SoggettoVo con tutti i dati recuperati dalla pagina.
   * @param datiSoggetto RiscaDatiSoggetto con le informazioni per: dati soggetto; da salvare.
   * @param recapito RiscaRecapito con le informazioni per: recapito; da salvare.
   * @param contatti RiscaContatto con le informazioni per: contatti; da salvare.
   */
  private generaSoggettoCompleto(
    datiSoggetto: RiscaDatiSoggetto,
    recapito: RiscaRecapito,
    contatti: RiscaContatto
  ): SoggettoVo {
    // Recupero gli oggetti dei recapiti alternativi
    const recapitiA = this.recapitiAlternativiTable.getDataSource();
    // Richiamo la funzione di gestione dei recapiti
    let recapiti = this._recapiti.gestisciRecapiti(
      recapito,
      contatti,
      recapitiA
    );

    // Per il recapito principale devo mergiare i dati con un possibile recapito principale esistente
    const recapitiC = this.soggettoP?.recapiti;
    // Lancio l'aggiornamento dati
    recapiti = this._soggettoDA.mergeRecapitiPrincipaliDaRecapiti(
      recapitiC,
      recapiti
    );

    // Genero i dati anagrafici
    return this._soggettoDA.convertDatiSoggettoFormToSoggettoVo(
      datiSoggetto,
      recapiti
    );
  }

  /**
   * ########################
   * GESTIONE DEGLI ACCORDION
   * ########################
   */

  /**
   * Funzione agganciata all'accordion per la gestione dei nuovi recapiti alternativi.
   * @param aperto boolean che definisce se l'accordion è aperto o chiuso.
   */
  toggleNuoviRecapitiAlternativi(aperto: boolean) {
    // Aggiorno lo stato dell'accordion
    this.showNuoviRecapitiAlternativi = aperto;
  }

  /**
   * #############################
   * GESTIONE RECAPITI ALTERNATIVI
   * #############################
   */

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsValid; per i recapiti alternativi.
   * @param formsValid IRiscaFormTreeParent con i dati validi.
   */
  private formsRAValid(formsValid: IRiscaFormTreeParent) {
    // Definisco i parametri per la generazione della mappa
    const fv = formsValid;
    const raPK = this.raParentKey;
    const raCKs = this.raChildrenKeys;
    // Recupero la mappa dati dei componenti
    const mapData = this.getRiscaFormTreedData(fv, raPK, raCKs);
    // Definisco le chiavi dei figli singolramente
    const keyRecapito = this.DA_C.FORM_KEY_CHILD_DA_RECAPITO_ALTERNATIVO;
    const keyContatti = this.DA_C.FORM_KEY_CHILD_DA_CONTATTI_ALTERNATIVO;
    const tipoRecapito = this._recapiti.tipoRecapitoAlternativo;
    // Genero l'oggetto di configurazione
    const c = new DatiRecapitoFormConfigs({
      mapData,
      keyRecapito,
      keyContatti,
      tipoRecapito,
    });

    // Richiamo la funzione di generazione del dato
    const recapitoToTable: RecapitoVo =
      this._recapiti.generateRecapitoVoFromDatiRecapitoForm(c);

    /**
     * Per la gestione di un nuovo recapito alternativo CON selezione da mettere dentro la pratica, aggiungo una proprietà CUSTOM di RISCA.
     * Questa proprietà verrà gestita dal BO e ci permetterà di sapere, quando si ritorna sull'applicazione, quale degli oggetti generati dal FE è stato collegato alla pratica.
     * Altrimenti si verifica un errore dove all'interno della pratica viene messo un recapito alternativo senza id.
     */
    recapitoToTable._risca_id_recapito = recapitoToTable.__id;

    // Aggiungo alla tabella dei recapiti alternativi le informazioni
    this.recapitiAlternativiTable.addElement(recapitoToTable);
    // Resetto e chiudo il form dei recapiti alternativi
    this.resetFormsRA();
  }

  /**
   * Funzione di reset della form per il recapito alternativo.
   */
  resetFormsRA() {
    // Resetto le form
    this.riscaDARecapitoAlt.onFormReset();
    this.riscaDAContattiAlt.onFormReset();
    // Aggiorno lo stato dell'accordion
    this.showNuoviRecapitiAlternativi = false;
  }

  /**
   * Funzione che rimuove dalla tabella dei recapiti alternativi l'oggetto passato come parametro.
   * @param row RiscaTableDataConfig<any> contenente i dati della riga.
   */
  rimuoviRecapitoAlternativo(row: RiscaTableDataConfig<any>) {
    // Richiamo la logica di rimozione recapito alternativo
    this.onRimuoviRecapitoAlternativo(row);
  }

  /**
   * Funzione invocata alla confirm della cancellazione di un recapito alternativo.
   * @param row RiscaTableDataConfig<RecapitoVo> contenente i dati della riga.
   */
  private onRimuoviRecapitoAlternativo(row: RiscaTableDataConfig<RecapitoVo>) {
    // Recupero il soggetto presente nel componente
    const s = this.soggettoP;
    // Recupero l'id_recapito alternativo dall'oggetto
    const r = row.original;

    // Verifico che esistano id soggetto e id recapito
    const existIdS = s?.id_soggetto != undefined;
    const existIdRA = r?.id_recapito != undefined;

    // Verifico se esiste l'id_soggetto e l'id_recapito
    if (existIdS && existIdRA) {
      // Definisco il possibile id della pratica da escludere per la verify
      const idPratica = this.idPraticaPerVerify;

      // Effettuo la verfica delle pratiche del soggetto
      this._soggetto
        .controllaRStDRecapitoAlternativoElimina(r, idPratica)
        .subscribe({
          next: (verifica: IResultVerifyRStDDettaglio) => {
            // Richiamo la funzione di verifica per la comunicazione con l'utente
            let m = this._soggetto.messaggioControlRStDRAElimina(verifica);
            // Richiamo la funzione di conferma cancellazione con uno specifico messaggio
            this.confermaRimuoviRecapitoAlternativo(m, row);
            // #
          },
          error: (error: any) => {
            // Gestisco l'errore
            this.onServiziError(error);
          },
        });
      // #
    } else {
      // Richiamo la remove della table perché sto lavorando con dati solo locali
      this.recapitiAlternativiTable.removeElement(row);
    }
  }

  /**
   * Funzione che gestisce il flusso di conferma cancellazione di un recapito alternativo.
   * In input riceverà il codice del messaggio da visualizzare utente per la richiesta di conferma.
   * @param msg string che definisce il messaggio da visualizzare.
   * @param row RiscaTableDataConfig<RecapitoVo> contenente i dati della riga.
   */
  private confermaRimuoviRecapitoAlternativo(
    msg: string,
    row: RiscaTableDataConfig<RecapitoVo>
  ) {
    // Definisco la funzione di callback nel caso in cui l'utente confermi la cancellazione della riga
    const onConfirm = () => {
      // Recupero l'id_soggetto del possibile soggetto presente nel componente
      const idSoggetto = this.soggettoP?.id_soggetto;
      // Recupero l'id_recapito alternativo dall'oggetto
      const idRecapitoA = row.original?.id_recapito;

      // Richiamo la funzione di cancellazione del recapito alternativo
      this._recapiti
        .removeRecapitoAlternativo(idSoggetto, idRecapitoA)
        .subscribe({
          next: (recapitiA: RecapitoVo[]) => {
            // Riga rimossa
            this.onRimossoRecapitoAlternativo(row);
            // #
          },
          error: (error: RiscaServerError) => {
            // Gestisco l'errore
            this.onServiziError(error);
          },
        });
    };
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConferma(msg, { onConfirm });
  }

  /**
   * Funzione di supporto richiamata quando un recapito alternativo viene rimosso.
   * @param row RiscaTableDataConfig<RecapitoVo> contenente i dati della riga.
   */
  private onRimossoRecapitoAlternativo(row: RiscaTableDataConfig<RecapitoVo>) {
    // Richiamo la remove della table
    this.recapitiAlternativiTable.removeElement(row);

    // Aggiorno i dati del soggetto della pratica
    this.soggettoP.recapiti = this.soggettoP.recapiti.filter((r) => {
      // Assegno e tipizzo l'oggetto recapito eliminato
      const rE: RecapitoVo = row.original;
      // Tengo i recapiti che non matchano con l'id del recapito eliminato
      return r.id_recapito !== rE.id_recapito;
    });

    // Recupero l'oggetto della riga
    const recapitoRimosso = row.original;
    // Verifico se il recapito rimosso è quello selezionato per la pratica
    if (this.recapitoAP?.id_recapito === recapitoRimosso?.id_recapito) {
      // Resetto il recapito selezionato
      this.recapitoAP = undefined;
    }

    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.P002;
    // Definisco la lista messaggi per l'utente
    const dE = [];
    // Recupero il codice per la comunicazione utente
    dE.push(this._riscaMessages.getMessage(code));
    // Definisco un oggetto di alert
    const a = new RiscaAlertConfigs({
      type: RiscaInfoLevels.success,
      messages: dE,
    });

    // Emetto l'evento per far visualizzare il messaggio al componente padre
    this.notifyParent(a);
  }

  /**
   * Funzione che apre un modale e visualizza i dati di un recapito alternativo.
   * @param row RiscaTableDataConfig<RecapitoVo> contenente i dati della riga.
   */
  modificaRecapitoAlternativo(row: RiscaTableDataConfig<RecapitoVo>) {
    // Salvo localmente il dato
    const recapitoAlternativoRow = row;
    // Definisco la funzione di aggiornamento dato
    const onConfirm = (raModificato: RecapitoVo) => {
      // Aggiorno l'oggetto della tabella
      this.recapitiAlternativiTable.convertAndUpdateElement(
        raModificato,
        recapitoAlternativoRow
      );

      // Verifico se il recapito alternativo è quello selezionato. Attenzione: potrebbe non essere stato selezionato alcun recapito alternativo connesso alla pratica.
      const existRAP = this.recapitoAP !== undefined;
      const idRAP = existRAP && this.recapitoAP.id_recapito;
      const sameIdR = existRAP && idRAP === raModificato?.id_recapito;

      // Verifico se è lo stesso indirizzo
      if (sameIdR) {
        // Aggiorno l'oggetto per recapitoAP
        this.recapitoAP = raModificato;
      }
    };

    // Recupero le informazioni del recapito alternativo
    const ra: RecapitoVo = row.original;
    const idPratica = this.idPraticaPerVerify;
    const datiAnagrafici = this.soggettoP;
    const component = RiscaRecapitoModalComponent;
    const onConfirmModal = onConfirm;

    // Richiamo l'apertura del modale passando le configurazioni
    this._recapiti.updateRecapitoAlternativoModal({
      idPratica,
      datiAnagrafici,
      ra,
      component,
      onConfirmModal,
    });
  }

  /**
   * Funzione che imposta i dati di un recapito alternativo, inviato dalla tabella, come recapito alternativo della pratica.
   * @param row RiscaTableDataConfig<RecapitoVo> contenente i dati della riga.
   */
  collegaRecapitoAlternativoAPratica(row: RiscaTableDataConfig<RecapitoVo>) {
    // Salvo l'oggetto localmente
    this.recapitoAP = row.original;
  }

  /**
   * Funzione agganciata all'evento di azione custom da parte della tabella dei recapiti alternativi.
   * @param customAction IRiscaTableACEvent<any> con la configurazione dati per il pulsante premuto.
   */
  customActionRATable(customAction: IRiscaTableACEvent<any>) {
    // Definisco le chiavi per le azioni
    const MOD_IND_SPED = this.DA_C.INDIRIZZO_SPEDIZIONE;
    // Verifico quale pulsante è stato premuto, recupero l'action
    const { action } = customAction?.action || {};

    // Verifico quale azione è stata premuta
    switch (action) {
      case MOD_IND_SPED:
        // Recupero dalla configurazione il recapito alternativo premuto come riga di tabella
        const recapitoRow: RiscaTableDataConfig<RecapitoVo> = customAction.row;
        // Richiamo la funzione per l'aggiornamento dell'indirizzo di spedizione
        this.modificaISRATable(recapitoRow);
        break;
      // #
      default:
        return;
    }
  }

  /**
   * Funzione di supporto che gestisce la gestione dei dati per l'aggiornamento di un recapito alternativo, come dato passato dalla tabella.
   * @param recapitoARow RiscaTableDataConfig<RecapitoVo> contenente i dati della riga del recapito alternativo per la quale modificare l'indirizzo di spedizione.
   */
  private modificaISRATable(recapitoARow: RiscaTableDataConfig<RecapitoVo>) {
    // Verifico l'input
    if (!recapitoARow) {
      // Non ci sono dati per fare la modifica
      return;
    }

    // Recupero l'oggetto recapito dalla riga della tabella
    const recapito = clone(recapitoARow.original);
    // Recupero l'oggetto soggetto di riferimento del componente
    const soggetto = this.soggettoP;
    // Prendo l'id del gruppo
    const idGruppo = this.gruppoP?.id_gruppo_soggetto ?? undefined;

    // Creo la configurazione per le callbacks della modale
    const callbacks: ICallbackDataModal = {
      onConfirm: (indirizzi: IndirizzoSpedizioneVo[]) => {
        // Richiamo la funzione di gestione dell'aggiornamento dati
        this.onModificaISRATable(recapitoARow, indirizzi[0], idGruppo);
      },
    };

    // Creo la configurazione per la modifica tramite modale
    const config: IAggiornaIndirizzoSpedizione = {
      soggetto,
      idRecapito: recapito.id_recapito,
      callbacks,
      idGruppo,
      isFormDisabilitato: this.disableUserInputs,
    };
    // Richiamo la funzione di modifica indirizzo spedizone tramite modale
    this._isUtility.modificaIndirizziSpedizioneSoggetto(config);
  }

  /**
   * Funzione invocata alla modifica di un indirizzo di spedizione.
   * Come per la gestione dei recapiti alternativi, vado a gestire i dati della tabella.
   * @param recapitoARow RiscaTableDataConfig<RecapitoVo> con l'oggetto che identifica la riga della tabella.
   * @param indirizzoUpd IndirizzoSpedizioneVo che definisce l'oggetto indirizzo di spedizione aggiornato su DB.
   * @param idGruppo number che definisce l'id del gruppo di riferimento selezionato per la pratica.
   */
  private onModificaISRATable(
    recapitoARow: RiscaTableDataConfig<RecapitoVo>,
    indirizzoUpd: IndirizzoSpedizioneVo,
    idGruppo?: number
  ) {
    // Verifico l'input
    if (!recapitoARow || !indirizzoUpd) {
      // Non c'è il dato d'aggiornare
      return;
    }

    // Recupero l'oggetto base del recapito
    const recapitoUpd: RecapitoVo = clone(recapitoARow.original);
    // Per il recapito, vado ad aggiornare l'indirizzo di spedizione
    this._isUtility.aggiornaIndirizzoSpedizioneInRecapito(
      recapitoUpd,
      indirizzoUpd,
      idGruppo
    );

    // Una volta aggiornato l'indirizzo di spedizione, aggiorno i dati della tabella
    this.recapitiAlternativiTable.convertAndUpdateElement(
      recapitoUpd,
      recapitoARow
    );

    // Recupero i dati dalla tabella per rigenerare correttamente le icone
    const recapitiA = this.recapitiAlternativiTable.getDataSource();
    // Richiamo la funzione per rigenerare la tabella
    this.rigeneraTabellaRA(recapitiA);
  }

  /**
   * ###################################
   * GESTIONE DEGLI EVENTI DELLE TABELLE
   * ###################################
   */

  /**
   * Funzione agganciata all'evento di dettaglio lanciato dalla tabella.
   * @param row RiscaTableDataConfig<SoggettoVo> contenente i dati della riga.
   */
  dettaglioComponenteGruppo(row: RiscaTableDataConfig<SoggettoVo>) {
    // Verifico l'input
    if (!row) {
      return;
    }

    // Recupero dalla riga l'oggetto original
    const soggetto: SoggettoVo = row.original;
    // Definisco lo state da passare alla pagina
    const state: ISoggettoRouteParams = {
      soggetto,
      modalita: AppActions.modifica,
      eliminaDisabled: true,
    };

    // Definisco l'oggetto di configurazione per la richiesta di navigazione
    const config: IRFCReqJourneyNavigation = {
      route: AppRoutes.soggetto,
      extras: { state },
      claimant: AppClaimants.dettaglioGruppo_pratica,
      sameRoute: false,
      jStepTarget: this.S_C.NAVIGATION_CONFIG,
    };

    // Richiamo la funzione per la richiesta della snapshot
    this.navigaJourney(config);
  }

  /**
   * ##################################
   * PULSANTI PER AZIONI DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione invocata al click del pulsante di aggiunta recapiti alternativi.
   */
  aggiungiRecapitoAlternativo() {
    // Richiamo il submit delle form
    this.riscaDARecapitoAlt.onFormSubmit();
    this.riscaDAContattiAlt.onFormSubmit();
  }

  /**
   * Funzione che resetta la selezione della riga per la tabella dei recapiti alternativi.
   */
  deselezionaRecapitoAlternativoScelto() {
    // Lancio la funzione di deselezione per la tabella dei recapiti alternativi
    this.riscaTRecapitiAlt.resetRadio();
    // Resetto la selezione della riga locale
    this.recapitoAP = undefined;
  }

  /**
   * Funzione che apre la scheda gruppo permettendo delle modifiche all'utente.
   * Le modifiche verranno riacquisite nel momento di "indietro".
   */
  dettaglioGruppo() {
    // Definisco lo state da passare alla pagina
    const state: IGruppoRouteParams = {
      gruppo: clone(this.gruppoP),
      modalita: AppActions.modifica,
      idPratica: this.idPraticaPerVerify,
      praticaNavTarget: this.praticaNavTarget,
    };

    // Definisco l'oggetto di configurazione per la richiesta di navigazione
    const config: IRFCReqJourneyNavigation = {
      route: AppRoutes.gruppo,
      extras: { state },
      claimant: AppClaimants.dettaglioGruppo_pratica,
      sameRoute: false,
      jStepTarget: this.G_C.NAVIGATION_CONFIG,
    };

    // Richiamo la funzione per la richiesta della snapshot
    this.navigaJourney(config);
  }

  /**
   * ##################################################################
   * FUNZIONALITA PER LA GESTIONE DEL SUBMBIT/RESET DEL FORM COME PADRE
   * ##################################################################
   */

  /**
   * Funzione che gestisce la funzionalità di submit del form.
   * In questo caso verrà utilizzata dal componente padre per triggerare il submit dei sotto form di questo componente.
   * @override
   */
  onFormSubmit() {
    // Verifico che esista almeno il soggetto della pratica
    if (!this.soggettoP) {
      // Blocco il flusso con un errore
      this.gestisciErroriDatiAnagrafici(this.mainForm);
      // #
    }

    // Verifico che esistano le istanze dei componenti
    const existRDS = this.riscaDatiSoggetto !== undefined;
    const existRR = this.riscaRecapito !== undefined;
    const existRC = this.riscaContatti !== undefined;
    // Verifico che esistano le istanzi dei componenti figli
    if (!existRDS || !existRR || !existRC) {
      // Blocco il flusso con un errore
      this.gestisciErroriDatiAnagrafici(this.mainForm);
      // Blocco il flusso
      return;
      // #
    }

    // Verifico che i recapiti alternativi siano validi
    const checkRA = this.checkComuniDimessiRA();
    // Verifico il risultato del controllo
    if (checkRA) {
      // Blocco il flusso con un errore
      this.gestisciErroriDatiAnagrafici(this.mainForm);
      // Blocco il flusso
      return;
      // #
    }

    // Lancio il submit dei sotto forms
    this.riscaDatiSoggetto.onFormSubmit();
    this.riscaRecapito.onFormSubmit();
    this.riscaContatti.onFormSubmit();
  }

  /**
   * Funzione che gestisce la funzionalità di reset del form.
   * In questo caso verrà utilizzata dal componente padre per triggerare il reset dei sotto form di questo componente.
   * @override
   */
  onFormReset() {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Submit manuale della form
    this.mainForm.reset();

    this.showDatiAnagrafici = false;
    this.idRecapitoP = undefined;
    this.showNuoviRecapitiAlternativi = false;
    this.alertCEConfigs = new RiscaAlertConfigs();
    this.soggettoP = undefined;
    this.soggettoPInit = undefined;
    this.gruppoP = undefined;
    this.fonteConfigs = [];
    this.setupDARecapitiAlternativiTable();
    this.setupComponentiGruppiTable();

    // Verifico se sono in modifica, se lo sono ripristino le informazioni
    if (this.modifica) {
      // Rilancio l'init dei dati in un timeout, così che l'ngif possa ricostruire le parti della pagina
      setTimeout(() => {
        // Init dei dati
        this.initDatiAnagraficiPratica();
      });
    }
  }

  /**
   * Funzione che permette di definire le logiche per la verifica del mainForm.
   * @override
   */
  prepareMainFormForValidation() {
    // Recupero i dati del soggetto della pratica
    const soggetto = this.soggettoP;
    // Recupero i dati del gruppo della pratica
    const gruppo = this.gruppoP;
    // Definisco il contenitore per i recapiti
    const recapiti = [];
    // Recupero i dati per i recapiti della pratica
    const recapitoP = this.soggettoP?.recapiti?.find((r: RecapitoVo) => {
      // Verifico se il recapito è il principale
      return isRecapitoPrincipale(r);
      // #
    });

    // Verifico se c'è il recapito principale e lo aggiungo
    if (recapitoP) {
      // Inserisco il recapito nell'array
      recapiti.push(recapitoP);
    }
    // Verifico se c'è un recapito alternativo selezionato per la pratica
    if (this.recapitoAP) {
      // Inserisco il recapito nell'array
      recapiti.push(this.recapitoAP);
    }

    // Variabili di comodo
    const f = this.mainForm;
    const F_SP = this.DA_C.SOGGETTO_PRATICA;
    const F_GP = this.DA_C.GRUPPO_PRATICA;
    const F_RP = this.DA_C.RECAPITI_PRATICA;

    // Aggiorno i valori per il main form
    this._riscaUtilities.setFormValue(f, F_SP, soggetto);
    this._riscaUtilities.setFormValue(f, F_GP, gruppo);
    this._riscaUtilities.setFormValue(f, F_RP, recapiti);
  }

  /**
   * ##################################################################################################
   * GESTIONE DELL'AGGREGAZIONE DATI DELLE SOTTO FORM, PER COMPORRE L'INFORMAZIONE DA MANDARE AL PARENT
   * ##################################################################################################
   */

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsValid.
   * @param formsValid IRiscaFormTreeParent con i dati validi.
   * @override
   */
  formsValid(formsValid: IRiscaFormTreeParent) {
    // Definisco variabili di comodo
    const KDS = this.DA_C.FORM_KEY_CHILD_DA_DATI_SOGGETTO;
    const KRP = this.DA_C.FORM_KEY_CHILD_DA_RECAPITO;
    const KCP = this.DA_C.FORM_KEY_CHILD_DA_CONTATTI;

    // Richiamo la funzione di gestione dell'alert per il reset
    this.aggiornaAlertConfigs(this.alertConfigs);

    // Recupero la mappa dati dei componenti
    const mapData = this.getRiscaFormTreedData(formsValid);
    // Recupero i dati tramite chiavi per: dati soggetto, recapito principale, contatti principali
    const ds: RiscaDatiSoggetto = mapData.get(KDS);
    const rp: RiscaRecapito = mapData.get(KRP);
    const cp: RiscaContatto = mapData.get(KCP);

    // Genero i dati anagrafici
    this.componiSoggettoPraticaDaForms(ds, rp, cp);
    // Lancio la funzione di gestione "finale" per i dati anagrafici
    this.verificaDatiAnagrafici();
  }

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsInvalid.
   * @param formsInvalid IRiscaFormTreeParent con i dati invalidi.
   * @override
   */
  formsInvalid(formsInvalid: IRiscaFormTreeParent) {
    // Richiamo la funzione di gestione dell'alert per il reset
    this.aggiornaAlertConfigs(this.alertConfigs);

    // Definisco un array per i messaggi di errore
    let messaggi = [];
    // Genero una mappa con gli errori da visualizzare
    const mapErr = this.getRiscaFormTreedData(formsInvalid);

    // Ciclo la mappa e concateno i messaggi d'errore
    for (let messForm of mapErr.values()) {
      // Verifico che esista un valore per la chiave
      const messFormCheck = messForm ?? [];
      // Concateno gli array
      messaggi = messaggi.concat(messFormCheck);
    }

    // Lancio la funzione di gestione "finale" per i dati anagrafici
    this.verificaDatiAnagrafici(messaggi);
  }

  /**
   * ###########################################################
   * FUNZIONALITA' DI CONTROLLO E VERIFICA PER I DATI ANAGRAFICI
   * ###########################################################
   */

  /**
   * Funzione che gestisce l'emissione delle informazioni per i dati anagrafici.
   * Questa funzione specifica, se possiede in input la variabile dei messaggi, andrà in errore.
   * @param erroriForms Array di string che contiene gli errori lanciati dalla gestione dei forms principali.
   */
  private verificaDatiAnagrafici(erroriForms?: string[]) {
    // Preparo i dati per la validazione del form
    this.prepareMainFormForValidation();

    // Verifico che la form sia valida
    if (this.mainForm.valid && !erroriForms) {
      // Lancio le logiche di verifica/aggiornamento del soggetto
      this.checkUpdateSoggetto();
      // #
    } else {
      // Gestisco la visualizzazione degli errori del form usiForm
      this.gestisciErroriDatiAnagrafici(this.mainForm, erroriForms);
    }
  }

  /**
   * Funzione di supporto che gestisce le possibili modifiche al soggetto.
   * Se rispetto allo stato iniziale il soggetto risulta modificato, allora chiedo all'utente se vuole aggiornare i dati del soggetto.
   */
  private checkUpdateSoggetto() {
    // Variabili di comodo
    let ST0 = this.soggettoPInit; // Dati soggetto a "T0" (iniziale)
    let ST1 = this.soggettoP; // Dati soggetto a "T1" (possibile modifiche)

    // Richiamo la funzione di check dei dati del soggetto
    const sameDA = this._soggettoDA.compareSoggetti(ST0, ST1);
    // Verifico se i dati sono gli stessi
    if (sameDA) {
      // Non ci sono state modifiche, comunico i dati anagrafici
      this.emitDatiAnagrafici();
      // #
    } else {
      // I dati del soggetto sono stati modificati sulla pagina
      this.onSoggettoModificato();
    }
  }

  /**
   * ######################################################################################
   * GESTIONE DELLA CASISTICA: IL SOGGETTO NELLA SCHEDA DATI ANAGRAFICI E' STATO MODIFICATO
   * ######################################################################################
   */

  /**
   * Funzione che gestisce le logiche per l'aggiornamento dei dati del soggetto.
   * Verranno effettuati dei passaggi di richiesta all'utente per confermare l'aggiornamento dati del soggetto.
   */
  private onSoggettoModificato() {
    // Recupero le informazioni del soggetto
    const soggetto = this.soggettoP;
    // Procedo alla prossima verifica
    this.controllaPraticheSoggetto(soggetto);
  }

  /**
   * Funzione che effettua tutti i controlli sul soggetto per la gestione dall'azione da fare sul soggetto.
   * Dalla documentazione i controlli saranno:
   * - Controllo presenza RISCOSSIONI associate al SOGGETTO;
   * - Controllo presenza STATI DEBITORI associati al SOGGETTO;
   * @param soggetto SoggettoVo da gestire.
   */
  private controllaPraticheSoggetto(soggetto: SoggettoVo) {
    // Verifico l'input
    if (!soggetto || !soggetto.id_soggetto) {
      return;
    }

    // Effettuo la verfica delle pratiche del soggetto
    this._soggetto
      .controllaRStDSoggettoModifica(soggetto, this.idPraticaPerVerify)
      .subscribe({
        next: (verifica: IResultVerifyRStDDettaglio) => {
          // Verifico se ho trovato le pratiche e definisco il messaggio
          if (verifica.isObjCollegato) {
            // Richiamo la funzione che gestirà la messaggistica utente
            let m = this._soggetto.messaggioControlRStDSModifica(verifica);
            // Richiamo la funzione di conferma modifica con uno specifico messaggio
            this.confermaModificaSoggetto(soggetto, m);
            // #
          } else {
            // Richiamo la funzione di modifica del soggetto direttamente
            this.modificaSoggetto(soggetto);
            // #
          }
        },
        error: (e: RiscaServerError) => {
          // Gestisco l'errore
          this.onFormErrors(this.mainForm, { serverError: e });
        },
      });
  }

  /**
   * Funzione che gestisce la conferma di modifica di un soggetto.
   * @param s SoggettoVo da modificare.
   * @param msgCode string che definisce il codice per il messaggio da visualizzare all'utente.
   */
  private confermaModificaSoggetto(s: SoggettoVo, msgCode: string) {
    // Definisco la funzione di callback nel caso in cui l'utente confermi la cancellazione della riga
    const onConfirm = () => {
      // Richiamo la funzione di modifica del soggetto
      this.modificaSoggetto(s);
    };
    const onCancel = () => {
      // Ripristino il soggetto della pratica al suo stato iniziale
      this.soggettoP = this.soggettoPInit;
    };
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConferma(msgCode, {
      onConfirm,
      onCancel,
    });
  }

  /**
   * Effettua la chiamata al servizio per modificare il soggetto dato.
   * @param s il soggetto da modificare.
   */
  private modificaSoggetto(s: SoggettoVo) {
    // Cancello possibili informazioni del soggetto generato dall'errore sugli indirizzi di spedizione
    this.soggettoISError = undefined;

    // Richiamo la funzione di modifica del soggetto
    this._soggettoDA.updateDASoggetto(s).subscribe({
      next: (soggettoRes: SoggettoHTTPResponse) => {
        // Recupero il soggetto dalla risposta del servizio
        const { soggetto, jsonWarning } = soggettoRes;

        // Lancio la gestione del jsonWarning
        this.handleJsonWarningCFRisca(jsonWarning);
        // Soggetto modificato, aggiorno il valore nel componente
        this.onModificaDASoggetto(soggetto);
        // Richiamo l'evento di soggetto aggiornato con correzione degli indirizzi di spedizione
        this.emitDatiAnagrafici();
        // #
      },
      error: (e: RiscaServerError) => {
        // Verifico e gestisco l'errore per l'update del soggetto
        this.onSoggettoError(e);
      },
    });
  }

  /**
   * Effettua la chiamata al servizio per modificare il soggetto dato.
   * @param s il soggetto da modificare
   * @param indModManuale boolean che specifica se la modifica del soggetto deve risultare con gli indirizzi spedizione editati manualmente.
   */
  private modificaSoggettoConIndirizziCorretti(
    s: SoggettoVo,
    indModManuale?: boolean
  ) {
    // Cancello possibili informazioni del soggetto generato dall'errore sugli indirizzi di spedizione
    this.soggettoISError = undefined;

    // Richiamo la funzione di modifica del soggetto
    this._soggettoDA.updateDASoggetto(s, indModManuale).subscribe({
      next: (soggettoRes: SoggettoHTTPResponse) => {
        // Recupero il soggetto dalla risposta del servizio
        const { soggetto, jsonWarning } = soggettoRes;

        // Lancio la gestione del jsonWarning
        this.handleJsonWarningCFRisca(jsonWarning);
        // Soggetto modificato, aggiorno il valore nel componente
        this.onModificaDASoggetto(soggetto);
        // Richiamo la funzione di comunicazione dei dati anagrafici
        this._riscaFormSubmitHandler.childSubmit(this.childFormKey);
        // #
      },
      error: (e: RiscaServerError) => {
        // Verifico e gestisco l'errore per l'update del soggetto
        this.onSoggettoError(e);
      },
    });
  }

  /**
   * Funzione che aggiorna i dati all'interno del componente a seguito di una modifica del soggetto.
   * @param sm SoggettoVo con i dati del soggetto modificato.
   */
  private onModificaDASoggetto(sm: SoggettoVo) {
    // Verifico l'input
    if (!sm) {
      // Non aggiorno i dati
      return;
    }

    // Aggiorno le variabili locali
    this.soggettoP = sm;
    this.soggettoPInit = sm;

    // Recupero i recapiti
    const { recapiti } = this.soggettoP;
    // Aggiorno la tabella dei recapiti
    this.onModificaDASoggettoUpdateTRA(recapiti);
  }

  /**
   * Funzione che aggiorna la tabella dei recapiti alternativi con le informazioni ottenute a seguito dell'aggiornamento dei dati del soggetto.
   * @param recapiti Array di RecapitoVo con tutti i recapiti del soggetto.
   */
  private onModificaDASoggettoUpdateTRA(recapiti: RecapitoVo[]) {
    // Verifico l'input
    if (!recapiti || recapiti.length === 0) {
      // Blocco il flusso
      return;
    }

    // Recupero i recapiti alternativi dai recapiti
    const recapitiA = this._recapiti.recapitiAlternativiFromRecapiti(recapiti);
    // Richiamo la funzione per rigenerare la tabella
    this.rigeneraTabellaRA(recapitiA);
  }

  /**
   * Funzione di comodo che rigenera la tabella dei recapiti alternativi.
   * @param recapitiA Array di RecapitoVo che definirà la nuova struttura della tabella.
   */
  private rigeneraTabellaRA(recapitiA: RecapitoVo[]) {
    // Verifico che esistano recapiti alternativi trovati
    if (!recapitiA || recapitiA.length === 0) {
      // Resetto la tabella
      this.recapitiAlternativiTable.source = [];
      // Blocco il flusso
      return;
    }

    // Richiamo il setup della tabella dei recapiti alternativi
    this.setupDARecapitiAlternativiTable(recapitiA);
    // Recupero il vecchio recapito alternativo selezionato
    const raSel = this.recapitoAP;
    // Se esiste, imposto nella tabella lo stesso recapito alternativo selezionato
    if (raSel) {
      // Definisco l'elemento selezionato
      this.recapitiAlternativiTable.setElementSelectedExclusive(
        (r: RecapitoVo) => {
          // Per gestire la casistica di nuovo recapito inserito, verifico anche l'id custom risca
          const checkIDR = r.id_recapito === raSel.id_recapito;
          const checkIDRR = r._risca_id_recapito === raSel._risca_id_recapito;
          // Verifico gli id dei recapiti
          return checkIDR || checkIDRR;
        }
      );
      // Cerco se esiste un recapito alternativo selezionato
      const rowRASel = this.recapitiAlternativiTable.getElementSelected();
      // Verifico se esiste un recapito alternativo selezionato
      if (rowRASel) {
        // Vado ad aggiornare le informazioni per il recapito alternativo
        this.collegaRecapitoAlternativoAPratica(rowRASel);
      }
    }
  }

  /**
   * Funzione di comodo che impacchetta e comunica al componente padre i dati anagrafici.
   */
  private emitDatiAnagrafici() {
    // Recupero i dati della form
    let formData: DatiAnagrafici = this.getMainFormActualRawValue();

    // Rimuovo tutte le informazioni dei metadati
    formData = this.cleanRiscaMetadata(formData);

    // Emetto l'evento con i dati del form
    this.onFormSubmit$.emit(formData);
    // Lancio la funzione per l'emissione dell'evento gestito tramite servizio
    this.formSubmitHandler(formData, RiscaFormStatus.valid);
  }

  /**
   * Funzione di supporto che gestisce la "pulizia" dei metadati di RISCA prima dell'emissione delle informazioni.
   * @param formData DatiAnagrafici contenente le informazioni generate dal componente dei dati anagrafici.
   * @returns DatiAnagrafici ripuliti dai metadati di RISCA.
   */
  private cleanRiscaMetadata(formData: DatiAnagrafici): DatiAnagrafici {
    // Verifico all'interno dei dati se esistono metadati
    // 1) Recapiti collegati alla pratica
    if (formData?.recapitiPratica) {
      // Rimappo le informazioni dei recapiti della pratica rimuovendo i metadati
      formData.recapitiPratica = formData.recapitiPratica.map(
        (ra: RecapitoVo) => {
          // Richiamo la funzione per il clear dati dei recapiti
          return this._soggettoDA.clearRiscaMDRecapito(ra);
        }
      );
    }
    // 2) Recapiti del soggetto
    if (formData?.soggettoPratica?.recapiti) {
      // Rimappo le informazioni dei recapiti della pratica rimuovendo i metadati
      formData.soggettoPratica.recapiti = formData.soggettoPratica.recapiti.map(
        (ra: RecapitoVo) => {
          // Richiamo la funzione per il clear dati dei recapiti
          return this._soggettoDA.clearRiscaMDRecapito(ra);
        }
      );
    }

    // Ritorno l'oggetto aggiornato
    return formData;
  }

  /**
   * ######################################################
   * FLUSSO INSERIMENTO/MODIFICA SOGGETTO - GESTIONE ERRORI
   * ######################################################
   */

  /**
   * Funzione di supporto che gestisce l'errore in caso di fallimento dell'insert soggetto.
   * @param error RiscaServerError con le informazioni relative all'errore generato dal server.
   */
  private onSoggettoError(error: RiscaServerError) {
    // Gestisco l'errore normalmente
    this.onFormErrors(this.mainForm, { serverError: error });

    // Definisco uno specifico errore per la gestione degli indirizzi di spedizione
    const eIS = RSEPostSoggettiIS;
    // Verifico se è un errore specifico
    const isRSEIS = this._riscaSEM.checkServerError(error, eIS);

    // Verifico se è necessario gestire l'errore sugli indirizzi di spedizione
    if (isRSEIS) {
      // L'errore generato è proprio per la gestione degli indirizzi di spedizione
      this.onIndirizziSpedizioneError(error);
      // #
    }
  }

  /**
   * Funzione che gestisce il caso in cui l'insert soggetto dia errore per colpa degli indirizzi di spedizione.
   * @param error RiscaServerError contente le informazioni dell'errore per la gestione dell'insert soggetto con indirizzi di spedizione errati.
   */
  private onIndirizziSpedizioneError(error: RiscaServerError) {
    // Estraggo dall'oggetto di errore i dati del soggetto
    const { soggetto } = error?.error?.detail || {};
    // Verifico per sicurezza la presenza dell'oggetto soggetto
    if (soggetto) {
      // E' necessario correggere gli indirizzi di spedizione, richiamo la funzione apposita
      this.correzioneIndirizziSpedizione(soggetto);
    }
  }

  /**
   * Funzione che gestisce la casistica d'errore di un oggetto non inserito perché gli indirizzi di spedizione sono sbagliati.
   * @param soggetto SoggettoVo generato dal server come errore. Conterrà gli indirizzi di spedizione da correggere.
   */
  private correzioneIndirizziSpedizione(soggetto: SoggettoVo) {
    // Verifico l'input
    if (!soggetto || !soggetto.recapiti) {
      // Non ci sono informazioni per la correzione
      return;
    }

    // Salvo localmente il soggetto
    this.soggettoISError = cloneDeep(soggetto);
    // Richiamo la funzione di comodo del servizio
    const errData: IInvalidISDataFromSoggetto =
      this._isUtility.invalidIndirizziSpedizioneFromSoggetto(soggetto);

    // Creo la configurazione di parametri per correggere gli indirizzi di spedizione
    const dataModal: IIndirizziSpedizioneModalConfig = {
      gestione: RiscaGestioneISModal.correzione,
      recapiti: errData.recapiti,
      indirizziSpedizione: errData.indirizziSpedizione,
      gruppi: errData.gruppi,
    };
    // Creo la configurazione per le callbacks della modale
    const callbacks: ICallbackDataModal = {
      onConfirm: (indirizziOK: IndirizzoSpedizioneVo[]) => {
        // Richiamo la funzione di gestione della correzione
        this.onIndirizziSpedizioneCorretti(indirizziOK);
      },
    };

    // Preparo le configurazioni per l'apertura della modale
    const modalConfig: IApriModalConfigs = { params: { dataModal }, callbacks };
    // Richiamo l'apertura della modale
    this._isUtility.apriModaleIS(modalConfig);
  }

  /**
   * Funzione richiamata quando la modale che gestisce le informazioni per gli indirizzi di spedizione errati risultano corretti.
   * @param indirizziOK Array di IndirizzoSpedizioneVo con le informazioni degli indirizzi di spedizione corretti.
   */
  private onIndirizziSpedizioneCorretti(indirizziOK: IndirizzoSpedizioneVo[]) {
    // Verifico l'input
    if (!indirizziOK || indirizziOK.length === 0) {
      // Qualcosa è andato storto, blocco le logiche
      return;
    }

    // Lancio l'aggiornamento dati dell'oggetto soggetto in errore, con gli indirizzi validi
    const sOK = this._isUtility.updateSoggettoWithValidIndirizziSpedizione(
      this.soggettoISError,
      indirizziOK
    );
    // Pulisco l'alert precedentemente aperto per l'errore degli indirizzi di spedizione
    this.resetAlertConfigs(this.alertConfigs);

    // Rilancio l'aggiornamento del soggetto con le nuove informazioni
    this.modificaSoggettoConIndirizziCorretti(sOK, true);
  }

  /**
   * ##############################################
   * FUNZIONALITA' VARIE DI GESTIONE DEL COMPONENTE
   * ##############################################
   */

  /**
   * Funzione che verifica i recapiti alternativi nella tabella dei recapiti alternativi.
   * Se uno o più comuni risultano dismessi, verrà generato errore.
   * @returns boolean con il risultato del controllo.
   */
  private checkComuniDimessiRA(): boolean {
    // Recupero gli oggetti dei recapiti alternativi
    const recapitiA = this.recapitiAlternativiTable.getDataSource();
    // Recupero dai recapiti i comuni di residenza
    const comuni: ComuneVo[] = recapitiA?.map((r: RecapitoVo) => {
      // Recupero il comune
      return r?.comune_recapito;
    });
    // Verifico per ogni comune se risulta dimesso
    const comuneDismesso: boolean = comuni?.some((c: ComuneVo) => {
      // Verifico la validità per data_fine_validità
      const comuneValido = isDataValiditaAttiva(c);
      // Viene generato errore se il comune non è valido
      return !comuneValido;
    });

    // Ritorno il flag
    return comuneDismesso ?? false;
  }

  /**
   * Funzione che gestisce l'emissione delle informazioni per i dati anagrafici in errore.
   * @param f FormGroup che contiene i dati dei dati anagrafici.
   * @param erroriForms Array di string che contiene gli errori lanciati dalla gestione dei forms principali.
   */
  private gestisciErroriDatiAnagrafici(f: FormGroup, erroriForms?: string[]) {
    // Definisco al mappatura degli errori da recuperare
    const me = this.formErrors;

    // Recupero tutti i messaggi
    let mef = this._riscaUtilities.getAllErrorMessagesFromForm(f, me);
    // Aggiungo eventuali errori extra
    if (erroriForms) {
      // Concateno i messaggi d'errore
      mef = mef.concat(erroriForms);
    }

    // Verifico che i recapiti alternativi siano validi
    const checkRA = this.checkComuniDimessiRA();
    // Verifico il risultato del controllo
    if (checkRA) {
      // Aggiungo alla lista il messaggio
      mef = mef.concat(this.comuniDimessiErrors);
    }

    // Emetto l'evento comunicando i messaggi
    this.onFormErrors$.emit(mef);
    // Lancio la funzione per l'emissione dell'evento gestito tramite servizio
    this.formSubmitHandler(mef, RiscaFormStatus.invalid);
  }

  /**
   * Funzione che componente un oggetto SoggettoVo dai dati dei form.
   * @param datiSoggetto RiscaDatiSoggetto con i dati del form soggetto.
   * @param recapito RiscaRecapito con i dati del form recapito (principale).
   * @param contatti RiscaContatto con i dati del form contatti (principale).
   * @returns SoggettoVo con l'oggetto composto dai dati dei form.
   */
  private componiSoggettoPraticaDaForms(
    datiSoggetto: RiscaDatiSoggetto,
    recapito: RiscaRecapito,
    contatti: RiscaContatto
  ): SoggettoVo {
    // Verifico l'input
    if (!datiSoggetto || !recapito || !contatti) {
      // Ritorno undefined
      return undefined;
    }

    // Per il recapito definisco che è il principale
    recapito.tipoRecapito = this._recapiti.tipoRecapitoPrincipale;

    // Genero i dati anagrafici
    let s = this.generaSoggettoCompleto(datiSoggetto, recapito, contatti);

    // Aggiorno il dato locale, mergiando le proprietà aggiornate (la funzione mergia i dati del soggetto e dei recapiti)
    this.soggettoP = this._soggettoDA.mergeSoggetti(this.soggettoP, s);

    /**
     * Aggiorno anche l'oggetto di init.
     * PERCHE'? La generazione dell'oggetto front-end produrrà un oggetto che di base fornisce tutti i dati per il soggetto.
     * Questi dati potrebbero NON essere definiti nell'oggetto iniziale perché le informazioni null o undefined, non vengono gestite dal server.
     * L'oggetto quindi iniziale potrebbe non avere delle proprietà che verranno COMUNQUE definite dalla generazione del dato front end.
     * Utilizziamo quindi un modo per gestire questa situazione (dato che le logiche di controllo PREVEDANO una compare tra oggetto iniziale e oggetto finale del soggetto).
     * L'oggetto generato front end verrà mergiato con l'oggetto iniziale.
     * Essendo che la destrutturazione/assegnazione dell'oggetto va a SOSTITUIRE le proprietà comuni quando un oggetto è definito dopo un altro,
     * manterremo tutte le proprietà dell'oggetto front-end e si andranno a mantenere tutte le proprietà dell'oggetto iniziale.
     */
    this.soggettoPInit = { ...this.soggettoP, ...this.soggettoPInit };

    // Ritorno l'oggetto composto
    return this.soggettoP;
  }

  /**
   * #############################################################################################
   * FUNZIONE DI GESTIONE PER LA NAVIGAZIONE DELLE PAGINE, GESTENDO DATI E CONTESTO DEL COMPONENTE
   * #############################################################################################
   */

  /**
   * Funzione che permette di mettersi in contatto con il componente padre per attivare la navigazione mediante uno step journey.
   * Lo step verrà generato dal componente padre, e gestirà le configurazioni di navigazione che verranno passate a lui.
   * @param config IRFCReqJourneyNavigation con i dati di configurazione per la navigazione.
   */
  private navigaJourney(config: IRFCReqJourneyNavigation) {
    // Recupero lo stato attuale dei dati del form
    const datiSoggetto = this.riscaDatiSoggetto?.getMainFormActualRawValue();
    const recapito = this.riscaRecapito?.getMainFormActualRawValue();
    const contatti = this.riscaContatti?.getMainFormActualRawValue();
    // Compongo i dati anagrafici
    this.componiSoggettoPraticaDaForms(datiSoggetto, recapito, contatti);

    // Richiedo una snapshot dei dati, così da poter ricaricare le informazioni una volta tornati sulla pagina dell'inserimento pratica
    this.childReqJourneyNavigation(config);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di comodo che verifica se è la sezione dei soggetti è abilitata in modifica.
   */
  get isGestioneAbilitata() {
    // Verifico la configurazione dei gruppi è abilitata
    return this._soggettoDA.isGestioneAbilitata;
  }

  /**
   * Getter di comodo che verifica se è la sezione dei gruppi è da visualizzare.
   */
  get gruppoIsAbilitato() {
    // Verifico la configurazione dei gruppi è abilitata
    return this._gruppoSoggetto.isAbilitato;
  }

  /**
   * Getter che verifica se esiste l'oggetto del gruppo per la pratica ed esiste la tabella dei componenti (con almeno un componente).
   */
  get checkGruppo() {
    // Verifico che la configurazione non sia undefined
    const existGSC = this.gruppoP !== undefined;
    // Verifico se la tabella esiste e ha valori
    const existTCG = this.componentiGruppoTable?.source?.length > 0;

    // Ritorno le condizioni
    return existGSC && existTCG;
  }

  /**
   * Getter per il testo del pulsante di selezione/ricerca titolare.
   */
  get labelPulsanteTitolare(): RiscaButtonConfig {
    // Sulla base della modalità del componente ritorno uno specifico testo
    if (this.inserimento) {
      // Ritorno l'etichetta
      return { label: this.DA_C.TESTO_BOTTONE_CERCA_TITOLARE };
      // #
    } else if (this.modifica) {
      // Ritorno l'etichetta
      return { label: this.DA_C.TESTO_BOTTONE_CAMBIA_TITOLARE };
      // #
    } else {
      // Ritorno il default
      return { label: this.DA_C.TESTO_BOTTONE_CAMBIA_TITOLARE };
    }
  }

  /**
   * Getter per la label per il title della sezione dati anagrafici.
   */
  get titleDatiAnagrafici() {
    // Verifico se un gruppo è selezionato
    const existG = this.gruppoP !== undefined;

    // Verifico se esiste o non esiste l'oggetto del gruppo selezionato
    if (existG) {
      // Recupero la testata con il testo per il gruppo
      let testata = this.DA_C.TESTO_TESTATA_CON_GRUPPO;
      // Recupero il placeholder della label
      let ph = this.DA_C.TESTO_TESTATA_CON_GRUPPO_PLACEHOLDER;
      // Recupero la descrizione del gruppo
      let descGruppo = this.gruppoP.des_gruppo_soggetto;
      // Sostiutisco il placeholder con la descrizione e ritorno la label
      return testata.replace(ph, descGruppo);
      // #
    } else {
      // Ritorno la testata senza gruppo selezionato
      return this.DA_C.TESTO_TESTATA_SENZA_GRUPPO;
    }
  }

  /**
   * Getter di comodo che verifica le condizioni per visualizzare il pulsante "Cerca titolare" in testata.
   * @returns boolean con il risultato del check.
   */
  get mostraCercaTitolareTestata(): boolean {
    // Il pulsante in testata viene visualizzato solo se siamo in inserimento e non è ancora stato selezionato un soggetto
    return this.inserimento && this.soggettoP == undefined;
  }

  /**
   * Getter di verifica per la tabella dei recapiti alternativi.
   */
  get checkRecapitiA() {
    // Verifico che la tabella esista e abbia dati
    return this.recapitiAlternativiTable?.source?.length > 0;
  }

  /**
   * Getter che verifica che le informazioni per la tabella dei componenti gruppo siano valide.
   */
  get checkTableCG() {
    // Verifico che esista la tabella
    if (!this.componentiGruppoTable) {
      return false;
    }
    // Esiste la tabella, verifico che ci siano dati
    return this.componentiGruppoTable.source?.length > 0;
  }

  /**
   * Getter che verifica che le informazioni per la configurazione di alertCEConfigs siano valide.
   */
  get alertCEConfigscheck() {
    // Verifico che la configurazione per l'alert delle comunicazioni esista e contenga dati
    return this.alertCEConfigs?.messages?.length > 0;
  }

  /**
   * Getter di comodo per l'idPratica da passare al verify.
   */
  get idPraticaPerVerify() {
    return this.modalita == AppActions.modifica ? this.idPratica : undefined;
  }

  /**
   * Getter di comodo che recupera le informazioni della nav attualmente attiva per la gestione della pratica.
   * Se non definita, il default sarà: RiscaAzioniPratica.inserimento.
   * @returns RiscaAzioniPratica con l'indicazione della nav per la gestione della pratica.
   */
  get praticaNavTarget(): RiscaAzioniPratica {
    // Recupero dal servizio di navigazione l'ultimo step
    const lastStep: JourneyStep = this._navigationHelper.getLastStep();
    // Dall'ultimo step, cerco di recuperare l'informazione della nav
    const nav = lastStep?.stateTarget?.navTarget;

    // Ritorno l'id della nav
    return nav ?? RiscaAzioniPratica.inserisciPratica;
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.isAEAPraticaDADisabled || this.isPraticaLocked;
  }

  /**
   * Getter di comodo che verifica se la pratica è disabilitata da configurazione.
   * @returns boolean con il risultato del check.
   */
  get isAEAPraticaDADisabled(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.AEA_pratica_DADisabled;
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
   * Getter di comodo che recupera l'array di messaggi d'errore quando il check sui comuni dismessi viene intercettato.
   * @returns string[] con la lista dei messaggi d'errore.
   */
  get comuniDimessiErrors(): string[] {
    // Recupero il messaggio d'errore
    const code1 = RiscaNotifyCodes.F010;
    const code2 = RiscaNotifyCodes.E085;
    const m = [
      this._riscaMessages.getMessage(code1),
      this._riscaMessages.getMessage(code2),
    ];
    // Ritorno l'array
    return m;
  }
}
