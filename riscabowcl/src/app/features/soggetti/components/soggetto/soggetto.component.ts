import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { clone, cloneDeep, uniq } from 'lodash';
import { IRiscaTableACEvent } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.interfaces';
import { ComuneVo } from '../../../../core/commons/vo/comune-vo';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { IJsonWarning } from '../../../../core/services/http-helper/utilities/http-helper.interfaces';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import {
  ISoggettoRecapitiAlternativiTableConfigs,
  SoggettoRecapitiAlternativiTable,
} from '../../../../shared/classes/risca-table/soggetto/soggetto-recapiti-alternativi.table';
import { RiscaContattiComponent } from '../../../../shared/components/risca/risca-contatti/risca-contatti.component';
import { RiscaDatiSoggettoComponent } from '../../../../shared/components/risca/risca-dati-soggetto/risca-dati-soggetto.component';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { SetupFormSubmitHandlerListener } from '../../../../shared/components/risca/risca-form-parent/utilities/risca-form-parent.interfaces';
import { RiscaRecapitoModalComponent } from '../../../../shared/components/risca/risca-modals/risca-recapito-modal/risca-recapito-modal.component';
import { RiscaRecapitoComponent } from '../../../../shared/components/risca/risca-recapito/risca-recapito.component';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RSEPostSoggettiIS } from '../../../../shared/consts/risca/errors/http-errors-handler';
import { IIndirizziSpedizioneModalConfig } from '../../../../shared/modals/risca-indirizzi-spedizione-modal/utilities/risca-indirizzi-spedizione-modal.interfaces';
import { RiscaSelectService } from '../../../../shared/services/form-inputs/risca-select/risca-select.service';
import { RiscaSpinnerService } from '../../../../shared/services/risca-spinner.service';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import { RiscaServerErrorManagerService } from '../../../../shared/services/risca/risca-server-error-manager.service';
import { isDataValiditaAttiva } from '../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  AppCallers,
  AppClaimants,
  AppRoutes,
  IAlertCAFConfigs,
  ICallbackDataModal,
  IPraticaRouteParams,
  IResultVerifyRStDDettaglio,
  IRiscaFormTreeParent,
  RiscaAlertConfigs,
  RiscaAzioniPratica,
  RiscaContatto,
  RiscaDatiSoggetto,
  RiscaGestioneISModal,
  RiscaInfoLevels,
  RiscaIstanzePratica,
  RiscaRecapito,
  RiscaServerError,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IApriModalConfigs } from '../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { IHomeRouteParams } from '../../../home/components/home/utilities/home.interfaces';
import { IIPConfigs } from '../../../pratiche/components/inserisci-pratica/utilities/inserisci-pratica.interfaces';
import { IPraticheCollegateRouteParams } from '../../../pratiche/components/pratiche-collegate/utilities/pratiche-collegate.utilities';
import { PraticaRouteKeys } from '../../../pratiche/enums/pratica/pratica.enums';
import {
  IAggiornaIndirizzoSpedizione,
  IInvalidISDataFromSoggetto,
} from '../../../pratiche/interfaces/indirizzi-spedizione/indirizzi-spedizione.interfaces';
import { IndirizziSpedizioneUtilityService } from '../../../pratiche/service/dati-anagrafici/indirizzi-spedizione-utility/indirizzi-spedizione-utility.service';
import {
  DatiRecapitoFormConfigs,
  RecapitiService,
} from '../../../pratiche/service/dati-anagrafici/recapiti.service';
import { SoggettoDatiAnagraficiService } from '../../../pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { SoggettoHTTPResponse } from '../../../pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/utilities/soggetto-dati-anagrafici.interfaces';
import { SoggettoConsts } from '../../consts/soggetto/soggetto.consts';
import { SoggettoService } from '../../services/soggetto/soggetto.service';
import { IGruppoRouteParams } from '../gruppo/utilities/gruppo.interfaces';
import { ISoggettoRouteParams } from './utilities/soggetto.interfaces';

@Component({
  selector: 'soggetto',
  templateUrl: './soggetto.component.html',
  styleUrls: ['./soggetto.component.scss'],
})
export class SoggettoComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy
{
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti per la sezione dei dati del soggetto. */
  S_C = SoggettoConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaDatiSoggetto') riscaDatiSoggetto: RiscaDatiSoggettoComponent;
  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaRecapito') riscaRecapito: RiscaRecapitoComponent;
  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaContatti') riscaContatti: RiscaContattiComponent;

  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaRecapitoAlt') riscaRecapitoAlt: RiscaRecapitoComponent;
  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaContattiAlt') riscaContattiAlt: RiscaContattiComponent;

  /** String che definisce la chiave di gestione per la composizione dati dei form per i reapiti alternativi. */
  raParentKey: string;
  /** Array di string che definisce la chiave di gestione per la composizione dati dei form per i reapiti alternativi. */
  raChildrenKeys: string[];

  /** Array di stringhe che contiene gli errori per la form: dati generali amministrativi. */
  listaSoggetto: string[] = [];
  /** Lista dei campi modificati alla fonte da evidenziare in risca-dati-soggetto */
  fonteConfigs: string[] = [];

  /** SoggettoVo per la pre-valorizzazione dei componenti */
  private _soggettoDAConfigs: SoggettoVo;

  /** Number che definisce l'id del recapito principale per la modifica dei dati. */
  idRecapitoP: number;

  /** Boolean per la visualizzazione dei nuovi recapiti alternativi per l'utente. */
  showNuoviRecapitiAlternativi = false;

  /** Oggetto SoggettoRecapitiAlternativiTable che conterrà le configurazioni per la tabella dei recapiti alternativi. */
  tableRecapitiAlternativi: SoggettoRecapitiAlternativiTable;

  /** Boolean di configurazione che definisce se per default il cancella soggetto deve essere disabilitato. */
  private eliminaDisabled: boolean = false;

  /** SoggettoVo dell'oggetto salvato o modificato. */
  private soggettoSaved: SoggettoVo;
  /** SoggettoVo con le informazioni dell'oggetto eliminato. */
  private soggettoDeleted: SoggettoVo;
  /** SoggettoVo con le informazioni del soggetto generato dall'errore sugli indirizzi di spedizione. */
  private soggettoISError: SoggettoVo;

  /** RiscaAzioniPratica che definisce indicazioni per la gestione del ritorno dei dati del soggetto per la pratica. */
  private _praticaNavTarget: RiscaAzioniPratica;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente, per le comunicazioni extra. */
  alertCEConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();
  /** Oggetto che definisce il layout per l'alert di comunicazione principale. */
  private alertConfigsStyle = { 'margin-bottom': '40px' };

  /** Boolean che definisce lo stato di disabilitazione degli elementi del componente, per: inserisci/modifica soggetto. */
  AEA_ins_mod_DADisabled = false;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _activatedRoute: ActivatedRoute,
    private _isUtility: IndirizziSpedizioneUtilityService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _recapiti: RecapitiService,
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    private _riscaModal: RiscaModalService,
    private _riscaSEM: RiscaServerErrorManagerService,
    private _riscaSelect: RiscaSelectService,
    private _riscaSpinner: RiscaSpinnerService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _soggetto: SoggettoService,
    private _soggettoDA: SoggettoDatiAnagraficiService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages
    );
    // Imposto le configurazioni per lo step di journey
    this.stepConfig = SoggettoConsts.NAVIGATION_CONFIG;

    // Funzione di setup per il routing del browser
    this.setupBrowserRouting();
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnDestroy() {
    // Richiamo il destroy per il super
    this.onDestroySuper();
    // Richiamo il destroy per i dati locali
    this.onDestroyLocal();
  }

  /**
   * ##############################
   * FUNZIONI DI SETUP: CONSTRUCTOR
   * ##############################
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
    // Aggiungo lo stile all'oggetto dell'alert
    this.alertConfigs.containerCss = this.alertConfigsStyle;

    // Lancio la funzione di recupero delle configurazioni per l'abilitazione della pagina
    this.setupAEASoggetto();
    // Effettuo il setup per le tabelle
    this.setupTabelle();
    // Effettuo il setup dei dati passati alla pagina
    this.setupActivatedRouteParams();
    // Effettuo il setup del servizio
    this.setupFormSubmitHandlerService();
  }

  /**
   * Funzione di init per le configurazioni relative alla pagina del soggetto.
   */
  setupAEASoggetto() {
    // Recupero la chiave per la configurazione della form
    const daKey = this.AEAK_C.DETTAGLIO_SOGGETTO;
    // Recupero la configurazione della form dal servizio
    this.AEA_ins_mod_DADisabled = this._accessoElementiApp.isAEADisabled(daKey);
  }

  /**
   * Funzione di comodo per la gestione del setup delle tabelle.
   */
  private setupTabelle() {
    // Variabili di supporto
    const isGestioneAbilitata = this._soggettoDA.isGestioneAbilitata;
    const AEA_ins_mod_DADisabled = this.AEA_ins_mod_DADisabled;
    // Definisco la configurazione per la tabella
    const c: ISoggettoRecapitiAlternativiTableConfigs = {
      isGestioneAbilitata,
      AEA_ins_mod_DADisabled,
    };

    // Inizializzo la tabella dei recapiti alternativi
    this.tableRecapitiAlternativi = new SoggettoRecapitiAlternativiTable(c);
  }

  /**
   * Funzione di setup delle informazioni relative al routing della pagina.
   */
  private setupActivatedRouteParams() {
    // Repero il data dal servizio
    const data = this._riscaUtilities.getActivatedRouteData(
      this._activatedRoute
    );
    // La variabile data è composta dai parametri definiti nel <nome_modulo>-routing.module.ts che implementa il componente
    const resolveData: ISoggettoRouteParams = data?.soggetto;
    // Estraggo le proprietà
    const {
      soggetto,
      modalita,
      eliminaDisabled,
      campiAggiornatiFonte,
      praticaNavTarget,
    } = resolveData || {};

    // Verifico la configurazione per eliminaDisabled
    if (eliminaDisabled !== undefined) {
      this.eliminaDisabled = eliminaDisabled;
    }
    // Verifico i parametri
    if (soggetto) {
      // Assegno localmente i dati anagrafici
      this.soggettoDAConfigs = soggetto;

      // Definisco l'id del recapito principale per la valorizzazione della form
      this.setupIdRecapitoPrincipale(this.soggettoDAConfigs.recapiti);
      // Inizializzo la tabella dei recapiti alternativi
      this.setupRecapitiAlternativi(this.soggettoDAConfigs.recapiti);
    }
    // La modalità è obbligatoria
    if (modalita) {
      // Imposto la modalità
      this.modalita = modalita;
    }
    // Verifico se ci sono stati campi aggiornati dalla fonte
    if (campiAggiornatiFonte?.length > 0) {
      // Imposto le informazioni e l'alert
      this.evidenziaCampiModificati(campiAggiornatiFonte);
    }
    // Verifico se tra i parametri c'è un'indicazione di quale parte della gestione della pratica ha aperto la pratica effettivamente (la nav)
    if (praticaNavTarget) {
      // Esiste, mi salvo localmente l'informazione per gestire il ritorno
      this._praticaNavTarget = praticaNavTarget;
    }
  }

  /**
   * Funzione di setup per l'id del recapito principale.
   * @param recapiti Array di RecapitoVo contenente le informazioni dei recapiti.
   */
  private setupIdRecapitoPrincipale(recapiti: RecapitoVo[]) {
    // Verifico se esistono i recapiti
    if (!recapiti) {
      return;
    }

    // Filtro gli oggetti dei recapiti e estraggo solo gli alternativi
    const recapitoP =
      this._recapiti.getRecapitoPrincipaleFromRecapiti(recapiti);
    // Verifico se esiste un recapito principale
    if (recapitoP) {
      this.idRecapitoP = recapitoP.id_recapito;
    }
  }

  /**
   * Funzione di setup per i recapiti alternativi.
   * @param recapiti Array di RecapitoVo contenente le informazioni dei recapiti.
   */
  private setupRecapitiAlternativi(recapiti: RecapitoVo[]) {
    // Verifico se esistono i recapiti
    if (!recapiti) {
      return;
    }

    // Filtro gli oggetti dei recapiti e estraggo solo gli alternativi
    const recapitiAlternativi =
      this._recapiti.getRecapitiAlternativiFromRecapiti(recapiti);

    // Aggiungo l'elemento
    this.tableRecapitiAlternativi.setElements(recapitiAlternativi);
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
    const parent = this.S_C.FORM_KEY_PARENT_SOGGETTO;
    // Definisco le chiavi per i figli
    const children = [
      this.S_C.FORM_KEY_CHILD_DATI_SOGGETTO,
      this.S_C.FORM_KEY_CHILD_RECAPITO,
      this.S_C.FORM_KEY_CHILD_CONTATTI,
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
    this.setFormsSubmitHandler(parent, children, customListener);
  }

  /**
   * Funzione che gestisce il setup del servizio: RiscaFormSubmitHandlerService; per il gruppo di forms recapiti alternativi.
   */
  private setupRAFormsSubmitHandler() {
    // Assegno localmente le chiavi
    this.raParentKey = this.S_C.FORM_KEY_PARENT_SOGGETTO_RECAPITI_ALTERNATIVI;
    this.raChildrenKeys = [
      this.S_C.FORM_KEY_CHILD_RECAPITO_ALTERNATIVO,
      this.S_C.FORM_KEY_CHILD_CONTATTI_ALTERNATIVO,
    ];

    // Aggiungo il nuovo albero all'interno, passando da un reset dei dati
    this._riscaFormSubmitHandler.resetFormTree(
      this.raParentKey,
      this.raChildrenKeys
    );
  }

  /**
   * ##########################
   * FUNZIONI DI INIT: NGONINIT
   * ##########################
   */

  /**
   * ################################
   * FUNZIONI DI DESTROY: NGONDESTROY
   * ################################
   */

  /**
   * Funzione di gestione dati per la destroy del componente.
   * La funzione gestisce la destroy delle informazioni condivise con il componente ereditato.
   */
  private onDestroySuper() {
    // Rimuovo dal servizio dei form la chiave
    this._riscaFormSubmitHandler.deleteFormTree(
      this.S_C.FORM_KEY_PARENT_SOGGETTO
    );
    // Richiamo il super
    super.ngOnDestroy();
  }

  /**
   * Funzione di gestione dati per la destroy del componente.
   * La funzione gestisce la destroy delle informazioni locali al componente.
   */
  private onDestroyLocal() {
    // Rimuovo la funzionalità del back del browser
    this._navigationHelper.deleteBrowerBack();

    // Rimuovo gli ascoltatori del componente
    try {
    } catch (e) {}
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
   * Funzione di comodo che gestisce l'errore nel caso in cui ci siano recapiti alternativi (dalla tabella) che contengono comuni dismessi.
   */
  private onComuniDimessiRA() {
    // Recupero il messaggio d'errore
    const m = this.comuniDimessiErrors;
    // Genero la configurazione per l'alert
    const a = this.alertConfigs;
    const t = RiscaInfoLevels.danger;
    // Aggiorno l'alert
    this.aggiornaAlertConfigs(a, m, t);
  }

  /**
   * #########################
   * GESTIONE FORMS PRINCIPALI
   * #########################
   */

  /**
   * Funzione che verifica i recapiti alternativi nella tabella dei recapiti alternativi.
   * Se uno o più comuni risultano dismessi, verrà generato errore.
   * @returns boolean con il risultato del controllo.
   */
  private checkComuniDimessiRA(): boolean {
    // Recupero gli oggetti dei recapiti alternativi
    const recapitiA = this.tableRecapitiAlternativi.getDataSource();
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
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsValid.
   * @param formsValid IRiscaFormTreeParent con i dati validi.
   * @override
   */
  formsValid(formsValid: IRiscaFormTreeParent) {
    // Richiamo la funzione di gestione dell'alert per il reset
    this.aggiornaAlertConfigs(this.alertConfigs);
    // Verifico se i recapiti alternativi sono effettivamente corretti
    const checkRA = this.checkComuniDimessiRA();
    // Verifico il risultato del controllo
    if (checkRA) {
      // Almeno un recapito alternativo con comune non più valido
      this.onComuniDimessiRA();
      // Blocco il flusso
      return;
    }

    // Recupero la mappa dati dei componenti
    const mapData = this.getRiscaFormTreedData(formsValid);
    // Recupero i dati tramite chiavi
    const datiSoggetto: RiscaDatiSoggetto = mapData.get(
      this.S_C.FORM_KEY_CHILD_DATI_SOGGETTO
    );
    const recapito: RiscaRecapito = mapData.get(
      this.S_C.FORM_KEY_CHILD_RECAPITO
    );
    const contatto: RiscaContatto = mapData.get(
      this.S_C.FORM_KEY_CHILD_CONTATTI
    );

    // Genero i dati del soggetto
    const s = this.generaSoggettoCompleto(datiSoggetto, recapito, contatto);

    // Verifico la modalità
    if (this.inserimento) {
      // Devo salvare un nuovo soggetto
      this.inserisciSoggetto(s);
      // #
    } else if (this.modifica) {
      // Devo aggiornare il soggetto
      this.modificaSoggetto(s);
    }
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

    // Verifico se i recapiti alternativi sono effettivamente corretti
    const checkRA = this.checkComuniDimessiRA();
    // Verifico il risultato del controllo
    if (checkRA) {
      // Aggiungo alla lista il messaggio
      messaggi = messaggi.concat(this.comuniDimessiErrors);
    }

    // Rimuovo possibili messaggi duplicati
    messaggi = uniq(messaggi);

    // Richiamo la funzione di gestione dell'alert
    this.aggiornaAlertConfigs(
      this.alertConfigs,
      messaggi,
      RiscaInfoLevels.danger
    );
  }

  /**
   * ###########################
   * FLUSSO INSERIMENTO SOGGETTO
   * ###########################
   */

  /**
   * Funzione che gestisce le logiche di salvataggio dei dati.
   * @param soggetto SoggettoVo da inserire.
   */
  private inserisciSoggetto(soggetto: SoggettoVo) {
    // Cancello possibili informazioni del soggetto generato dall'errore sugli indirizzi di spedizione
    this.soggettoISError = undefined;

    // Salvo i dati anagrafici
    this._soggettoDA.insertDASoggetto(soggetto).subscribe({
      next: (soggettoRes: SoggettoHTTPResponse) => {
        // Recupero il soggetto dalla risposta del servizio
        const { soggetto, jsonWarning } = soggettoRes;
        // Lancio la gestione dei dati salvati
        this.onSoggettoSaved(soggetto, jsonWarning);
        // #
      },
      error: (error: RiscaServerError) => {
        // Definisco la modalità in inserimento
        const modalita = AppActions.inserimento;
        // Richiamo la funzione di gestione degli errori specifica per questa situazione
        this.onSoggettoError(error, modalita);
        // #
      },
    });
  }

  /**
   * ########################
   * FLUSSO MODIFICA SOGGETTO
   * ########################
   */

  /**
   * Funzione che gestisce le logiche di modifica dei dati.
   * @param soggetto SoggettoVo da modificare.
   */
  private modificaSoggetto(soggetto: SoggettoVo) {
    // Recupero i possibili dati di input di configurazione
    const soggettoDA = this.soggettoDAConfigs;
    // Dai dati anagrafici in input tento l'aggiunta dei campi con l'input del componente
    if (soggettoDA) {
      // Mergio le informazioni mancanti dal semplice oggetto generato dal componente
      soggetto = this._soggettoDA.mergeSoggetti(soggettoDA, soggetto);
    }

    // Variabili di comodo
    let ST0 = soggettoDA; // Dati soggetto a "T0" (iniziale)
    let ST1 = soggetto; // Dati soggetto a "T1" (possibile modifiche)

    // Richiamo la funzione di check dei dati del soggetto
    const sameD = this._soggettoDA.compareSoggetti(ST0, ST1);
    // Verifico se i dati sono differenti
    if (!sameD) {
      // I dati del soggetto sono stati modificati sulla pagina, controllo le pratiche del soggetto
      this.controllaPraticheSoggetto(soggetto, AppActions.modifica);
      // #
    } else {
      // Resetto possibili messaggi in pagina per l'utente
      this.aggiornaAlertConfigs(this.alertConfigs);
      // Variabili di comodo
      const a = this.alertConfigs;
      const c = RiscaNotifyCodes.I015;
      const m = [this._riscaMessages.getMessage(c)];
      const i = RiscaInfoLevels.info;

      setTimeout(() => {
        // Richiamo la funzione di gestione dell'alert
        this.aggiornaAlertConfigs(a, m, i);
      });
      // Eventemitter per la gestione del soggetto submittato
      this.refreshSelect();
      // #
    }
  }

  /**
   * Funzione di supporto che definisce le logiche per il controllo delle riscossioni e degli stati debitori di un soggetto, quando è in corso una: modifica.
   * @param soggetto SoggettoVo con le informazioni del soggetto da verificare.
   */
  private gestisciCRStDSModifica(soggetto: SoggettoVo) {
    // Effettuo la verfica delle pratiche del soggetto
    this._soggetto.controllaRStDSoggettoModifica(soggetto).subscribe({
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
          this.chiamaModificaSoggetto(soggetto);
          // #
        }
      },
      error: (error: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(error);
        // Eventemitter per la gestione del soggetto submittato
        this.refreshSelect();
      },
    });
  }

  /**
   * Funzione che gestisce la conferma di modifica di un soggetto.
   * @param s SoggettoVo da modificare.
   * @param msg string che definisce testo da far apparire nel modale.
   */
  private confermaModificaSoggetto(s: SoggettoVo, msg: string) {
    // Definisco la funzione di callback nel caso in cui l'utente confermi la cancellazione della riga
    const onConfirm = () => {
      // Richiamo la funzione di modifica del soggetto
      this.chiamaModificaSoggetto(s);
    };
    const onCancel = () => {
      // Eventemitter per la gestione del soggetto submittato
      this.refreshSelect();
    };
    const onClose = () => {
      // Eventemitter per la gestione del soggetto submittato
      this.refreshSelect();
    };
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConferma(msg, { onConfirm, onCancel, onClose });
  }

  /**
   * Effettua la chiamata al servizio per modificare il soggetto dato.
   * @param s SoggettoVo da modificare.
   * @param indModManuale boolean che specifica se la modifica del soggetto deve risultare con gli indirizzi spedizione editati manualmente.
   */
  private chiamaModificaSoggetto(s: SoggettoVo, indModManuale?: boolean) {
    // Cancello possibili informazioni del soggetto generato dall'errore sugli indirizzi di spedizione
    this.soggettoISError = undefined;

    // Richiamo la funzione di modifica del soggetto
    this._soggettoDA.updateDASoggetto(s, indModManuale).subscribe({
      next: (soggettoRes: SoggettoHTTPResponse) => {
        // Recupero il soggetto dalla risposta del servizio
        const { soggetto, jsonWarning } = soggettoRes;
        // Lancio la funzione per il soggetto salvato
        this.onSoggettoSaved(soggetto, jsonWarning);
        // #
      },
      error: (error: RiscaServerError) => {
        // Definisco la modalità in modifica
        const modalita = AppActions.modifica;
        // Richiamo la funzione di gestione degli errori specifica per questa situazione
        this.onSoggettoError(error, modalita);

        // Eventemitter per la gestione del soggetto submittato
        this.refreshSelect();
      },
    });
  }

  /**
   * ######################################################
   * FLUSSO INSERIMENTO/MODIFICA SOGGETTO - GESTIONE ERRORI
   * ######################################################
   */

  /**
   * Funzione di supporto che gestisce l'errore in caso di fallimento dell'insert soggetto.
   * @param error RiscaServerError con le informazioni relative all'errore generato dal server.
   * @param modalita AppActions che definisce la modalità di gestione per la gestione degli errori dei soggetti.
   */
  private onSoggettoError(error: RiscaServerError, modalita: AppActions) {
    // Gestisco l'errore normalmente
    this.onServiziError(error);

    // Definisco uno specifico errore per la gestione degli indirizzi di spedizione
    const eIS = RSEPostSoggettiIS;
    // Verifico se è un errore specifico
    const isRSEIS = this._riscaSEM.checkServerError(error, eIS);

    // Verifico se è necessario gestire l'errore sugli indirizzi di spedizione
    if (isRSEIS) {
      // L'errore generato è proprio per la gestione degli indirizzi di spedizione
      this.onIndirizziSpedizioneError(error, modalita);
      // #
    }
  }

  /**
   * Funzione che gestisce il caso in cui l'insert soggetto dia errore per colpa degli indirizzi di spedizione.
   * @param error RiscaServerError contente le informazioni dell'errore per la gestione dell'insert soggetto con indirizzi di spedizione errati.
   * @param modalita AppActions che definisce la modalità di gestione per la gestione degli errori dei soggetti.
   */
  private onIndirizziSpedizioneError(
    error: RiscaServerError,
    modalita: AppActions
  ) {
    // Estraggo dall'oggetto di errore i dati del soggetto
    const { soggetto } = error?.error?.detail || {};
    // Verifico per sicurezza la presenza dell'oggetto soggetto
    if (soggetto) {
      // E' necessario correggere gli indirizzi di spedizione, richiamo la funzione apposita
      this.correzioneIndirizziSpedizione(soggetto, modalita);
    }
  }

  /**
   * Funzione che gestisce la casistica d'errore di un oggetto non inserito perché gli indirizzi di spedizione sono sbagliati.
   * @param soggetto SoggettoVo generato dal server come errore. Conterrà gli indirizzi di spedizione da correggere.
   * @param modalita AppActions che definisce la modalità di gestione per la gestione degli errori dei soggetti.
   */
  private correzioneIndirizziSpedizione(
    soggetto: SoggettoVo,
    modalita: AppActions
  ) {
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
        this.onIndirizziSpedizioneCorretti(indirizziOK, modalita);
      },
    };

    // Preparo le configurazioni per l'apertura della modale
    const modalConfig: IApriModalConfigs = { params: { dataModal }, callbacks };
    // Richiamo l'apertura della modale
    this._isUtility.apriModaleIS(modalConfig);

    // Verifico se siamo in modalità modifica
    if (this.isModifica(modalita)) {
      // Eventemitter per la gestione del soggetto submittato
      this.refreshSelect();
    }
  }

  /**
   * Funzione richiamata quando la modale che gestisce le informazioni per gli indirizzi di spedizione errati risultano corretti.
   * @param indirizziOK Array di IndirizzoSpedizioneVo con le informazioni degli indirizzi di spedizione corretti.
   * @param modalita AppActions che definisce la modalità di gestione per la gestione degli errori dei soggetti.
   */
  private onIndirizziSpedizioneCorretti(
    indirizziOK: IndirizzoSpedizioneVo[],
    modalita: AppActions
  ) {
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

    // Verifico la modalità di gestione
    if (this.isInserimento(modalita)) {
      // Rilancio l'inserimento del soggetto con le nuove informazioni
      this.inserisciSoggetto(sOK);
      // #
    } else {
      // Rilancio l'aggiornamento del soggetto con le informazioni corrette
      this.chiamaModificaSoggetto(sOK, true);
    }
  }

  /**
   * ############################
   * FLUSSO ELIMINAZIONE SOGGETTO
   * ############################
   */

  /**
   * Funzione che gestisce le logiche di eliminazione dei dati.
   * @param datiAnagrafici SoggettoVo contenente le informazioni del soggetto da eliminare.
   */
  private eliminaSoggetto(datiAnagrafici: SoggettoVo) {
    // Resetto la lista errori
    this.aggiornaAlertConfigs(this.alertConfigs);
    // Definisco variabili di comodo
    const a = this.alertConfigs;
    const m = ['Attenzione: nessun dato per il soggetto da cancellare.'];
    const d = RiscaInfoLevels.danger;

    // Verifico che esistano i dati anagrafici del soggetto
    if (!datiAnagrafici) {
      // Resetto la lista errori
      this.aggiornaAlertConfigs(a, m, d);
      // #
    } else {
      // Richiamo la funzione di verifica e cancellazione dati
      this.controllaPraticheSoggetto(datiAnagrafici, AppActions.cancellazione);
    }
  }

  /**
   * Funzione di supporto che definisce le logiche per il controllo delle riscossioni e degli stati debitori di un soggetto, quando è in corso una: cancellazione.
   * @param soggetto SoggettoVo con le informazioni del soggetto da verificare.
   */
  private gestisciCRStDSCancellazione(soggetto: SoggettoVo) {
    // Effettuo la verfica delle pratiche del soggetto
    this._soggetto.controllaRStDSoggettoElimina(soggetto).subscribe({
      next: (verifica: IResultVerifyRStDDettaglio) => {
        // Verifico se sono state trovate pratiche/st debitori
        if (verifica.isObjCollegato) {
          // Richiamo la funzione di gestione del soggetto collegato
          this.onControllaRStDSElimina(verifica);
          // #
        } else {
          // Richiamo la funzione di conferma cancellazione con uno specifico messaggio
          this.confermaRimuoviSoggetto(soggetto, RiscaNotifyCodes.A013);
        }
      },
      error: (error: any) => {
        // Gestisco l'errore
        this.onServiziError(error);
        // Eventemitter per la gestione del soggetto submittato
        this.refreshSelect();
      },
    });
  }

  /**
   * Funzione di comodo per gestire il risultato del controllo su pratiche e stati debitori sul soggetto.
   * Modalità di gestion: cancellazione.
   * @param verifica IResultVerifyRStDDettaglio con i dettagli della verifica.
   */
  private onControllaRStDSElimina(verifica: IResultVerifyRStDDettaglio) {
    // Recupero il messaggio con placeholder
    const mi = [this._soggetto.messaggioControlRStDSElimina(verifica)];

    // Variabili di comodo
    const aType = RiscaInfoLevels.danger;
    const a = this.alertConfigs;
    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(a, mi, aType);
    // Eventemitter per la gestione delle select
    this.refreshSelect();
  }

  /**
   * Funzione che gestisce la conferma di cancellazione di un soggetto.
   * @param s SoggettoVo da cancellare.
   * @param msgCode string che definisce il codice per il messaggio da visualizzare all'utente.
   */
  private confermaRimuoviSoggetto(s: SoggettoVo, msgCode: string) {
    // Definisco la funzione di callback nel caso in cui l'utente confermi la cancellazione della riga
    const onConfirm = () => {
      this.chiamaRimuoviSoggetto(s);
    };
    const onCancel = () => {
      // Eventemitter per la gestione del soggetto submittato
      this.refreshSelect();
    };
    const onClose = () => {
      // Eventemitter per la gestione del soggetto submittato
      this.refreshSelect();
    };
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConfermaWithCode(msgCode, {
      onConfirm,
      onCancel,
      onClose,
    });
  }

  /**
   * Chiama direttamente il servizio per eliminare il soggetto fornito
   * @param s soggetto da cancellare
   */
  private chiamaRimuoviSoggetto(s: SoggettoVo) {
    // Recupero l'id_soggetto del possibile soggetto presente nel componente
    const idSoggetto = s?.id_soggetto;

    // Richiamo la funzione di cancellazione del recapito alternativo
    this._soggettoDA.eliminaDASoggetto(idSoggetto).subscribe({
      next: (soggettoEliminato: SoggettoVo) => {
        // Lancio la gestione dei dati eliminati
        this.onDatiAnagraficiDeleted(soggettoEliminato);
      },
      error: (error: any) => {
        // Gestisco l'errore
        this.onServiziError(error);
        // Eventemitter per la gestione del soggetto submittato
        this.refreshSelect();
      },
    });
  }

  /**
   * Funzione invocata a seguito della corretta cancellazione delle informazioni da parte del server.
   * @param datiAnagraficiEliminati SoggettoVo con i dati cancellati su DB.
   */
  private onDatiAnagraficiDeleted(datiAnagraficiEliminati: SoggettoVo) {
    // Definisco la lista messaggi per l'utente
    const datiEliminati = [];
    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.P002;
    // Recupero il codice per la comunicazione utente
    datiEliminati.push(this._riscaMessages.getMessage(code));

    // Salvo l'oggetto del soggetto eliminato
    this.soggettoDeleted = datiAnagraficiEliminati;

    // Variabili di comodo
    const a = this.alertConfigs;
    const m = datiEliminati;
    const s = RiscaInfoLevels.success;
    // Aggiorno la lista dei messaggi nel componente locale
    this.aggiornaAlertConfigs(a, m, s);

    // Richiamo il reset delle form
    this.resetFormSoggetto();
    // Effettuo il back
    this.tornaIndietro();
  }

  /**
   * ##########################################
   * FUNZIONI DI CONTROLLO PER MODIFICA/ELIMINA
   * ##########################################
   */

  /**
   * Funzione che effettua tutti i controlli sul soggetto per la gestione dall'azione da fare sul soggetto.
   * Dalla documentazione i controlli saranno:
   * - Controllo presenza RISCOSSIONI associate al SOGGETTO;
   * - Controllo presenza STATI DEBITORI associati al SOGGETTO;
   * - Controllo presenza GRUPPI associati al soggetto e associati alla RISCOSSIONE;
   * - Controllo presenza GRUPPI associati al soggetto e associati ad uno o più STATI DEBITORI;
   * @param soggetto SoggettoVo da gestire.
   * @param action AppActions che definisce il tipo d'azione per la gestione del soggetto.
   */
  private controllaPraticheSoggetto(soggetto: SoggettoVo, action: AppActions) {
    // Verifico l'input
    if (!soggetto || !soggetto.id_soggetto) {
      return;
    }

    // Definisco variabili di comodo
    const isCanc = action === AppActions.cancellazione;
    const isMod = action == AppActions.modifica;
    // Verifico l'azione
    if (isCanc) {
      // Richiamo la funzione di gestione della cancellazione
      this.gestisciCRStDSCancellazione(soggetto);
      // #
    } else if (isMod) {
      // Richiamo la funzione di gestione della modifica
      this.gestisciCRStDSModifica(soggetto);
      // #
    }
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
    const keyRecapito = this.S_C.FORM_KEY_CHILD_RECAPITO_ALTERNATIVO;
    const keyContatti = this.S_C.FORM_KEY_CHILD_CONTATTI_ALTERNATIVO;
    const tipoRecapito = this._recapiti.tipoRecapitoAlternativo;
    // Genero l'oggetto di configurazione
    const c = new DatiRecapitoFormConfigs({
      mapData,
      keyRecapito,
      keyContatti,
      tipoRecapito,
    });

    // Richiamo la funzione di generazione del dato
    const recapitoToTable =
      this._recapiti.generateRecapitoVoFromDatiRecapitoForm(c);

    // Aggiungo alla tabella dei recapiti alternativi le informazioni
    this.tableRecapitiAlternativi.addElement(recapitoToTable);
    // Resetto e chiudo il form dei recapiti alternativi
    this.resetFormsRA();
  }

  /**
   * Funzione di reset della form per il recapito alternativo.
   */
  resetFormsRA() {
    // Resetto le form
    this.riscaRecapitoAlt.onFormReset();
    this.riscaContattiAlt.onFormReset();
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
    const s = this.soggettoDAConfigs;
    // Recupero l'id_recapito alternativo dall'oggetto
    const r = row.original;

    // Verifico che esistano id soggetto e id recapito
    const existIdS = s?.id_soggetto != undefined;
    const existIdRA = r?.id_recapito != undefined;

    // Verifico se esiste l'id_soggetto e l'id_recapito
    if (existIdS && existIdRA) {
      // Effettuo la verfica delle pratiche del soggetto
      this._soggetto.controllaRStDRecapitoAlternativoElimina(r).subscribe({
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
          // Eventemitter per la gestione del soggetto submittato
          this.refreshSelect();
        },
      });
      // #
    } else {
      // Richiamo la remove della table perché sto lavorando con dati solo locali
      this.tableRecapitiAlternativi.removeElement(row);
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
      const idSoggetto = this.soggettoDAConfigs?.id_soggetto;
      // Recupero l'id_recapito alternativo dall'oggetto
      const idRecapitoA = row.original?.id_recapito;

      // Richiamo la funzione di cancellazione del recapito alternativo
      this._recapiti
        .removeRecapitoAlternativo(idSoggetto, idRecapitoA)
        .subscribe({
          next: (recapitiA: RecapitoVo[]) => {
            // Richiamo la remove della table
            this.tableRecapitiAlternativi.removeElement(row);

            // Definisco la lista messaggi per l'utente
            const datiEliminati = [];
            // Definisco il codice del messaggio
            const code = RiscaNotifyCodes.P002;
            // Recupero il codice per la comunicazione utente
            datiEliminati.push(this._riscaMessages.getMessage(code));

            // Aggiorno la lista dei messaggi nel componente locale
            this.aggiornaAlertConfigs(
              this.alertConfigs,
              datiEliminati,
              RiscaInfoLevels.success
            );
          },
          error: (error: any) => {
            // Gestisco l'errore
            this.onServiziError(error);
          },
        });
    };
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConferma(msg, { onConfirm });
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
      this.tableRecapitiAlternativi.convertAndUpdateElement(
        raModificato,
        recapitoAlternativoRow
      );
    };

    // Recupero le informazioni del recapito alternativo
    const ra: RecapitoVo = row.original;
    const idPratica = undefined;
    const datiAnagrafici = this.soggettoDAConfigs;
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
   * Funzione agganciata all'evento di azione custom da parte della tabella dei recapiti alternativi.
   * @param customAction IRiscaTableACEvent<any> con la configurazione dati per il pulsante premuto.
   */
  customActionRATable(customAction: IRiscaTableACEvent<any>) {
    // Definisco le chiavi per le azioni
    const MOD_IND_SPED = this.S_C.INDIRIZZO_SPEDIZIONE;
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
    const soggetto = this.soggettoDAConfigs;
    // Creo la configurazione per le callbacks della modale
    const callbacks: ICallbackDataModal = {
      onConfirm: (indirizzi: IndirizzoSpedizioneVo[]) => {
        // Richiamo la funzione di gestione dell'aggiornamento dati
        this.onModificaISRATable(recapitoARow, indirizzi[0]);
      },
    };

    // Creo la configurazione per la modifica tramite modale
    const config: IAggiornaIndirizzoSpedizione = {
      soggetto,
      idRecapito: recapito.id_recapito,
      callbacks,
    };
    // Richiamo la funzione di modifica indirizzo spedizone tramite modale
    this._isUtility.modificaIndirizziSpedizioneSoggetto(config);
  }

  /**
   * Funzione invocata alla modifica di un indirizzo di spedizione.
   * Come per la gestione dei recapiti alternativi, vado a gestire i dati della tabella.
   * @param recapitoARow RiscaTableDataConfig<RecapitoVo> con l'oggetto che identifica la riga della tabella.
   * @param indirizzoUpd IndirizzoSpedizioneVo che definisce l'oggetto indirizzo di spedizione aggiornato su DB.
   */
  private onModificaISRATable(
    recapitoARow: RiscaTableDataConfig<RecapitoVo>,
    indirizzoUpd: IndirizzoSpedizioneVo
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
      indirizzoUpd
    );

    // Una volta aggiornato l'indirizzo di spedizione, aggiorno i dati della tabella
    this.tableRecapitiAlternativi.convertAndUpdateElement(
      recapitoUpd,
      recapitoARow
    );
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Apre l'alert info in cima alla pagina e mostra i campi aggiornati dalla fonte, recuperati mediante ricerca del soggetto.
   * @param campiAggiornatiFonte Array di string con le informazioni dei campi aggiornati dalla fonte.
   */
  private evidenziaCampiModificati(campiAggiornatiFonte: string[]) {
    // Ottengo i campi aggiornati dalla fonte dall'oggetto del BE
    const caf = campiAggiornatiFonte || [];

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
   * Funzione che emette l'evento custom per gestire il bug del browser che resetta le select
   */
  private refreshSelect() {
    // Emetto l'evento custom di pratica submittata
    this._riscaSelect.refreshDOM();
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
    const recapitiA = this.tableRecapitiAlternativi.getDataSource();
    // Richiamo la funzione di gestione dei recapiti
    let recapiti = this._recapiti.gestisciRecapiti(
      recapito,
      contatti,
      recapitiA
    );

    // Per il recapito principale devo mergiare i dati con un possibile recapito principale esistente
    const recapitiC = this.soggettoDAConfigs?.recapiti;
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
   * Funzione invocata a seguito del corretto salvataggio delle informazioni da parte del server.
   * @param datiAnagrafici SoggettoVo con i dati inseriti a DB.
   * @param jsonWarn IJsonWarning come oggetto generato di warning non bloccante all'inserimento del soggetto.
   */
  private onSoggettoSaved(datiAnagrafici: SoggettoVo, jsonWarn?: IJsonWarning) {
    // Aggiorno i dati del componente
    this.aggiornaDatiAnagraficiComponente(datiAnagrafici);

    // Definisco la lista messaggi per l'utente
    const datiInseriti = [];
    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.P001;
    // Recupero il codice per la comunicazione utente
    datiInseriti.push(this._riscaMessages.getMessage(code));

    // Verifico se esiste qualche warning per il soggetto
    if (jsonWarn && jsonWarn.code) {
      // Recupero il codice ed il messaggio dal servizio
      const msgWarn = this._riscaMessages.getMessage(jsonWarn.code);
      // Aggiungo ai messaggi anche il messaggi di warning
      datiInseriti.push(msgWarn);
    }

    // Variabile di comodo
    const callerPratica = this.callerPratica;
    // A seconda del caller definisco la logica di gestione
    if (callerPratica) {
      // Richiamo il back alla pratica
      this.tornaPratica(datiInseriti);
      // Blocco il flusso
      return;
    }

    // Variabile di comodo
    const callerAltri =
      this.callerPratica ||
      this.callerHome ||
      this.callerPraticheCollegate ||
      this.callerGruppo;
    // A seconda del caller definisco la logica di gestione
    if (callerAltri) {
      // Modifico la modalità di gestione dati
      this.modalita = AppActions.modifica;
      // Gestisco il flusso della pagina
      this.completaDatiAnagraficiSalvati(datiInseriti);
      // Blocco il flusso
      return;
    }
  }

  /**
   * Funzione che completa il flusso di gestione del salvataggio dei dati anagrafici di un soggetto.
   * @param messaggi Array di string con dei messaggi da visualizzare nella view della pratica.
   */
  private completaDatiAnagraficiSalvati(messaggi?: string[]) {
    // Variabili di comodo
    const a = this.alertConfigs;
    const m = messaggi;
    const s = RiscaInfoLevels.success;

    // Aggiorno la lista dei messaggi nel componente locale
    this.aggiornaAlertConfigs(a, m, s);

    // Eventemitter per la gestione del soggetto submittato
    this.refreshSelect();
  }

  /**
   * Funzione richiamata quando i dati anagrafici del soggetto vengono salvati e si rimane all'interno della pagina.
   * @param datiAnagrafici SoggettoVo aggiornati a seguito di un salva tramite server.
   */
  private aggiornaDatiAnagraficiComponente(datiAnagrafici: SoggettoVo) {
    // Salvo localmente l'oggetto salvato
    this.soggettoSaved = datiAnagrafici;
    // Aggiorno l'oggetto di configurazione
    this.soggettoDAConfigs = datiAnagrafici;
    // Aggiorno l'id recapito principale
    this.setupIdRecapitoPrincipale(datiAnagrafici.recapiti);

    // Inizializzo la tabella dei recapiti alternativi
    this.setupRecapitiAlternativi(this.soggettoDAConfigs.recapiti);
  }

  /**
   * #############################
   * FUNZIONALITA LEGATE ALLE FORM
   * #############################
   */

  /**
   * Funzione che gestisce il reset delle informazioni all'interno
   */
  private resetFormSoggetto() {
    // Richiamo il reset delle form
    this.riscaDatiSoggetto.onFormReset();
    this.riscaRecapito.onFormReset();
    this.riscaContatti.onFormReset();
    this.tableRecapitiAlternativi.clear();
  }

  /**
   * Reset del form per la gestione in inserimento.
   */
  private resetFormSoggettoInsert() {
    // Richiamo il reset delle form
    this.riscaDatiSoggetto.onFormRestore(true);
    this.riscaRecapito.onFormReset();
    this.riscaContatti.onFormReset();
    this.tableRecapitiAlternativi.clear();
  }

  /**
   * Reset del form per la gestione in aggiornamento.
   */
  private resetFormSoggettoUpdate() {
    // Richiamo il reset delle form
    this.riscaDatiSoggetto.onFormRestore();
    this.riscaRecapito.onFormRestore();
    this.riscaContatti.onFormRestore();

    // Recupero i recapiti alternativi del soggetto
    const { recapiti } = this.soggettoDAConfigs;
    // Imposto i recapiti alternativi dalla config iniziale
    this.setupRecapitiAlternativi(recapiti);
  }

  /**
   * Aggiorna il soggetto dopo la chiusura della RiscaRecapitoComponent.
   * @param s SoggettoVo aggiornato da RiscaRecapitoComponent.
   */
  aggiornaSoggettoDaRR(s: SoggettoVo) {
    this.soggettoDAConfigs = s;
  }

  /**
   * #####################
   * PULSANTI DELLA PAGINA
   * #####################
   */

  /**
   * Funzione che attiva la route per ritornare alla pagina precedente.
   */
  tornaIndietro() {
    // Gestico la parte di "pulizia" del filo d'arianna per la scheda soggetto
    this.gestisciFiloArianna();

    // Effettuo il back
    if (this.callerPratica) {
      // Ritorno alla pratica
      this.tornaPratica();
      // #
    } else if (this.callerHome) {
      // Ritorno alla home
      this.tornaHome();
      // #
    } else if (this.callerPraticheCollegate) {
      // Ritorno alle pratiche collegate
      this.tornaPraticheCollegate();
      // #
    } else if (this.callerGruppo) {
      // Ritorno alla pagina del gruppo
      this.tornaGruppo();
    }
  }

  /**
   * Funzione di gestione del filo d'arianna.
   * Va a rimuovere l'ultimo segmento che definisce il dettaglio o l'inserimento soggetto.
   */
  private gestisciFiloArianna() {
    // Recupero i livelli di gestione per inserimento e dettaglio
    const insSFA: FALivello = this._riscaFA.nuovoSoggetto;
    const dettSFA: FALivello = this._riscaFA.dettaglioSoggetto;
    // Verifico se uno dei due livelli è presente nel filo d'arianna
    const segmentoIS = this._riscaFA.segmentoInFAByLivello(insSFA);
    const segmentoDS = this._riscaFA.segmentoInFAByLivello(dettSFA);
    // Definisco un oggetto singolo che conterrà solo un oggetto valorizzato (dei due) o undefined se non è stato trovato niente
    const segmento = segmentoIS || segmentoDS || undefined;

    // Tento di rimuovere il segmento dal filo d'arianna
    this._riscaFA.rimuoviSegmento(segmento);
  }

  /**
   * Funzione che effettua un reindirizzamento verso la pagina della pratica.
   * @param messaggi Array di string con dei messaggi da visualizzare nella view della pratica.
   */
  private tornaPratica(messaggi?: string[]) {
    // Definisco i parametri per la pagina della pratica
    const state: IPraticaRouteParams = {
      navTarget: this.praticaNavTarget,
      pageTarget: this.praticaPageTarget,
    };
    // Creo l'oggetto con tutti i parametri per il setup dell'inserimento pratica
    const soggettoInserito: IIPConfigs = {
      navTarget: RiscaIstanzePratica.datiAnagrafici,
      soggettoMessaggi: {
        messaggi: messaggi,
        tipo: RiscaInfoLevels.success,
      },
    };

    // Variabili di comodo
    const cIS = this._navigationHelper.isLastClaimant(
      AppClaimants.insertSoggetto
    );
    // Verifico chi è il richiedente e la modalità
    if (this.inserimento && cIS) {
      // Aggiungo il soggetto inserito
      soggettoInserito.soggettoInsert = this.soggettoSaved;
    }
    // Definisco una proprietà specifica come configurazione dell'inserisci pratica
    state[PraticaRouteKeys.inserimento] = soggettoInserito;

    // Richiamo la funzione di back del servizio di navigazione
    this._navigationHelper.stepBack({ stateTarget: state });
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
   * Funzione che effettua un reindirizzamento verso la pagina delle pratiche collegate.
   */
  private tornaPraticheCollegate() {
    // Verifico se è stato cancellato il soggetto
    if (this.soggettoDeleted !== undefined) {
      // Definisco la route verso la home
      const route = AppRoutes.home;
      // Aggiungo il soggetto cancellato
      let state: IHomeRouteParams = {};
      // Devo navigare verso la home
      this._navigationHelper.goToStep(route, { stateTarget: state });
      // #
    } else {
      // Definisco i parametri per la navigazione
      let state: IPraticheCollegateRouteParams = {};
      // Per evitare il reset del filo d'arianna, passo sempre una variabile "inutile" come oggetto di parametri da passare alla pagina, così da non attivare il reset di default del filo
      state.extra = { avoidResetFiloArianna: true };

      // Devo fare il back alle pratiche collegate
      if (this.soggettoSaved !== undefined) {
        // Aggiungo il soggetto modificato
        state.soggetto = this.soggettoSaved;
        // #
      } else {
        // Ripasso le informazioni che sono state mandate in input
        state.soggetto = this.soggettoDAConfigs;
      }
      // Richiamo la funzione di back del servizio di navigazione
      this._navigationHelper.stepBack({ stateTarget: state });
    }
  }

  /**
   * Funzione che effettua un reindirizzamento verso la pagina per la gestione dei gruppi.
   */
  private tornaGruppo() {
    // Definisco i parametri per il routing
    const state: IGruppoRouteParams = {};

    // Variabili di comodo
    const cIS = this._navigationHelper.isLastClaimant(
      AppClaimants.insertSoggetto
    );
    // Verifico se è stato fatto un inserimento o un aggiornamento
    if (this.soggettoSaved && cIS) {
      // Aggiungo il soggetto come inserito
      state.soggettoInsert = this.soggettoSaved;
      // #
    }

    // Richiamo la funzione di back del servizio di navigazione
    this._navigationHelper.stepBack({ stateTarget: state });
  }

  /**
   * ########################################
   * FUNZIONI COLLEGATI AI PULSANTI DI PAGINA
   * ########################################
   */

  /**
   * Funzione che chiede all'utente se vuole resettare tutti i dati per tutte le form della pratica.
   */
  resetFormSoggettoBtn() {
    // Dichiaro la funzione di callback
    const onConfirm = () => {
      // Resetto le form
      this.resetSubForms();
    };
    // Richiamo la funzione del servizio per la modale di conferma annullamento
    this._riscaModal.apriModalConfermaAnnullamento({ onConfirm });
  }

  /**
   * Funzione che gestisce il reset delle sub form del componente.
   */
  private resetSubForms() {
    // Verifico la modalità del componente
    if (this.inserimento) {
      // Reset per inserimento
      this.resetFormSoggettoInsert();
      // #
    } else if (this.modifica) {
      // Reset per inserimento
      this.resetFormSoggettoUpdate();
      // #
    } else {
      // Reset totale
      this.resetFormSoggetto();
    }

    setTimeout(() => {
      // Rimuovo eventuali spinner extra
      this._riscaSpinner.hide();
    });
  }

  /**
   * Funzione invocata al click del pulsante ELIMINA SOGGETTO.
   */
  eliminaSoggettoBtn() {
    // Richiamo la funzione di cancellazione
    this.eliminaSoggetto(this.soggettoDAConfigs);
  }

  /**
   * Funzione invocata al click del pulsante SALVA.
   */
  salvaSoggettoBtn() {
    // Richiamo il submit delle form
    this.riscaDatiSoggetto.onFormSubmit();
    this.riscaRecapito.onFormSubmit();
    this.riscaContatti.onFormSubmit();
  }

  /**
   * Funzione invocata al click del pulsante di aggiunta recapiti alternativi.
   */
  aggiungiRecapitiAlternativi() {
    // Richiamo il submit delle form
    this.riscaRecapitoAlt.onFormSubmit();
    this.riscaContattiAlt.onFormSubmit();
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
   * #######################
   * GETTER SETTER DI COMODO
   * #######################
   */

  /**
   * Getter per _soggettoDAConfigs.
   */
  get soggettoDAConfigs() {
    // Ritorno una copia dell'oggetto
    return clone(this._soggettoDAConfigs);
  }

  /**
   * Setter per _soggettoDAConfigs.
   */
  set soggettoDAConfigs(soggettoDAConfigs: SoggettoVo) {
    // Ritorno una copia dell'oggetto
    this._soggettoDAConfigs = soggettoDAConfigs;
  }

  /**
   * Getter che ottiene l'oggetto di onfigurazione per il tasto Salva
   */
  get dataConfigSalva() {
    const text = this.callerPratica ? ' E TORNA ALLA PRATICA' : '';
    return { label: 'SALVA' + text, codeEAE: this.AEAK_C.SALVA_SOGGETTO };
  }

  /**
   * Getter che verifica se il caller è definito per 'pratica'.
   */
  get callerPratica(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastCaller(AppCallers.pratica);
  }

  /**
   * Getter che verifica se il caller è definito per 'home'.
   */
  get callerHome(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastCaller(AppCallers.home);
  }

  /**
   * Getter che verifica se il caller è definito per 'pratiche/pratiche-collegate'.
   */
  get callerPraticheCollegate(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastCaller(AppCallers.praticheCollegate);
  }

  /**
   * Getter che verifica se il caller è definito per 'soggetti/gruppo'.
   */
  get callerGruppo(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastCaller(AppCallers.gruppo);
  }

  /**
   * Getter che recupera l'abilitazione per i soggetti.
   */
  get abilitaSoggetto(): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    const abilitato = this._soggettoDA.isGestioneAbilitata;
    // Ritorno l'abilitazione
    return abilitato;
  }

  /**
   * Getter che verifica se l'eliminazione dei soggetti è possibile.
   */
  get eliminazionePossibile() {
    // Verifico se siamo in modifica
    const isModifica = this.modifica;
    // Ritorno la combinazione dei flag
    return isModifica;
  }

  /**
   * Getter che verifica se l'eliminazione dei soggetti è abilitata come funzionalità.
   */
  get eliminazioneAttiva() {
    // Recupero se la gestione è abilitata
    const isGestioneAbilitata = this.abilitaSoggetto;

    // 1) La gestione del soggetto, come ambito config, non è abilitata
    if (!isGestioneAbilitata) {
      // Ritorno false (disabilitata quindi)
      return false;
    }
    // 2) Verifico se l'elimina è disabilitato per configurazione della pagina
    if (this.eliminaDisabled) {
      // Ritorno false (disabilitata quindi)
      return false;
    }
    // 3) Verifico se il chiamate della pagina è: pratica o gruppo
    if (this.callerPratica || this.callerGruppo) {
      // Ritorno false (disabilitata quindi)
      return false;
    }

    // Controlli passati
    return true;
  }

  /**
   * Getter di comodo per la disabilitazione del pulsante salva.
   */
  get salvaDisabled() {
    // Ritorno le condizioni
    return this.AEA_ins_mod_DADisabled || !this.abilitaSoggetto;
  }

  /**
   * Getter di comodo per la disabilitazione del pulsante annulla.
   */
  get annullaDisabled() {
    // Ritorno le condizioni
    return this.AEA_ins_mod_DADisabled || !this.abilitaSoggetto;
  }

  /**
   * Getter che verifica che le informazioni per la configurazione di alertCEConfigs siano valide.
   */
  get alertCEConfigscheck() {
    // Verifico che la configurazione per l'alert delle comunicazioni esista e contenga dati
    return this.alertCEConfigs?.messages?.length > 0;
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
