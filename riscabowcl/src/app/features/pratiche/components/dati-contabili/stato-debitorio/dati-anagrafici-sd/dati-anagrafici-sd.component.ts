import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { clone, intersectionBy } from 'lodash';
import { forkJoin, Observable } from 'rxjs';
import { Subscription } from 'rxjs/index';
import { ComponenteGruppo } from 'src/app/core/commons/vo/componente-gruppo-vo';
import { Gruppo } from '../../../../../../core/commons/vo/gruppo-vo';
import { RecapitoVo } from '../../../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../../../core/commons/vo/soggetto-vo';
import { StatoDebitorioVo } from '../../../../../../core/commons/vo/stato-debitorio-vo';
import { AccessoElementiAppKeyConsts } from '../../../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { LoggerService } from '../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../core/services/navigation-helper/navigation-helper.service';
import {
  DAComponentiGruppoTable,
  IDAComponentiGruppoTableConfigs,
} from '../../../../../../shared/classes/risca-table/dati-anagrafici/dati-anagrafici-componenti-gruppo.table';
import {
  DARecapitiAlternativiTable,
  IDARecapitiAlternativiTableConfigs,
} from '../../../../../../shared/classes/risca-table/dati-anagrafici/dati-anagrafici-recapiti-alternativi.table';
import { RiscaContattiComponent } from '../../../../../../shared/components/risca/risca-contatti/risca-contatti.component';
import { RiscaDatiSoggettoComponent } from '../../../../../../shared/components/risca/risca-dati-soggetto/risca-dati-soggetto.component';
import { RiscaFormParentAndChildComponent } from '../../../../../../shared/components/risca/risca-form-parent-and-child/risca-form-parent-and-child.component';
import { RiscaRecapitoComponent } from '../../../../../../shared/components/risca/risca-recapito/risca-recapito.component';
import { RiscaTableComponent } from '../../../../../../shared/components/risca/risca-table/risca-table.component';
import { RiscaTableDataConfig } from '../../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../../../shared/consts/common-consts.consts';
import { RiscaSpinnerService } from '../../../../../../shared/services/risca-spinner.service';
import { RiscaAlertService } from '../../../../../../shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaMessagesService } from '../../../../../../shared/services/risca/risca-messages.service';
import { isRecapitoAlternativo } from '../../../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  DatiAnagrafici,
  IFormCercaSoggetto,
  IRiscaTabChanges,
  RiscaAlertConfigs,
  RiscaButtonConfig,
  RiscaServerError,
  RiscaStatoDebitorio,
} from '../../../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../../../shared/utilities/classes/errors-maps';
import { GruppoService } from '../../../../../soggetti/services/gruppo/gruppo.service';
import { DatiAnagraficiConsts } from '../../../../consts/dati-anagrafici/dati-anagrafici.consts';
import { DatiContabiliConsts } from '../../../../consts/dati-contabili/dati-contabili.consts';
import { ICCRMCloseParams } from '../../../../interfaces/cambia-creditore-rimborso-modal/cambia-creditore-rimborso-modal.interfaces';
import { IDatiAnagraficiSDUpd } from '../../../../interfaces/dati-contabili/dati-anagrafici-sd.interfaces';
import { GruppoSoggettoService } from '../../../../service/dati-anagrafici/gruppo-soggetto.service';
import { RecapitiService } from '../../../../service/dati-anagrafici/recapiti.service';
import { SoggettoDatiAnagraficiService } from '../../../../service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { DatiContabiliModalService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili-modal.service';
import { DatiContabiliUtilityService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { DatiContabiliService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili.service';
import { StatoDebitorioService } from '../../../../service/dati-contabili/stato-debitorio.service';
import { AccessoElementiAppService } from '../../../../../../core/services/accesso-elementi-app.service';

interface ISGStatoDebitorioReq {
  soggetto: Observable<SoggettoVo>;
  gruppo?: Observable<Gruppo>;
}

interface ISGStatoDebitorioRes {
  soggetto: SoggettoVo;
  gruppo?: Gruppo;
}

@Component({
  selector: 'dati-anagrafici-sd',
  templateUrl: './dati-anagrafici-sd.component.html',
  styleUrls: ['./dati-anagrafici-sd.component.scss'],
})
export class DatiAnagraficiSdComponent
  extends RiscaFormParentAndChildComponent<DatiAnagrafici>
  implements OnInit, OnDestroy
{
  /**
   * #######################
   * COSTANTI DEL COMPONENTE
   * #######################
   */
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts();
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto con le costanti per la sezione dei dati anagrafici. */
  DA_C = new DatiAnagraficiConsts();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;
  /** Costante per le informazioni del componente. */
  DC_C = DatiContabiliConsts;

  /**
   * #######################
   * CONFIGURAZIONI DI INPUT
   * #######################
   */
  /** Id della pratica. Può essere undefined. */
  @Input('idPratica') idPratica?: number;
  /** SoggettoVo per il pre-caricamento delle informazioni. */
  @Input('datiAnagrafici') datiAnagraficiPratica: DatiAnagrafici;

  /**
   * ###############################
   * RIFERIMENTI AI SOTTO COMPONENTI
   * ###############################
   */
  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaDatiSoggetto') riscaDatiSoggetto: RiscaDatiSoggettoComponent;
  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaRecapito') riscaRecapito: RiscaRecapitoComponent;
  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaContatti') riscaContatti: RiscaContattiComponent;

  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaTRecapitiAlt') riscaTRecapitiAlt: RiscaTableComponent<any>;

  /**
   * #########################################
   * DATI A SEGUITO DELLA RICERCA DEL TITOLARE
   * #########################################
   *
  /** Boolean per la visualizzazione dei dati anagrafici. */
  showDatiAnagrafici = false;
  /** Number che definisce l'id del recapito principale . */
  idRecapitoP: number;

  /**
   * #################################
   * INFORMAZIONI RECAPITI ALTERNATIVI
   * #################################
   */
  /** String che definisce la chiave di gestione per la composizione dati dei form per i recapiti alternativi. */
  raParentKey: string;
  /** Array di string che definisce la chiave di gestione per la composizione dati dei form per i recapiti alternativi. */
  raChildrenKeys: string[];
  /** RecapitoVo contenente i dati del recapito alternativo scelto per la connessione allo stato debitorio.  */
  recapitoASD: RecapitoVo;

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
  soggettoSD: SoggettoVo;
  /** SoggettoVo contenente le informazioni del soggetto della pratica al suo stato iniziale. Verrà usato per verificare se è necessario aggiornare prima il soggetto. */
  soggettoPInit: SoggettoVo;
  /** SoggettoVo con le informazioni del soggetto generato dall'errore sugli indirizzi di spedizione. */
  soggettoISError: SoggettoVo;

  /** Gruppo contenente le informazioni del gruppo associato alla pratica. O che verrà associato alla pratica. */
  gruppoSD: Gruppo;
  /** Lista dei campi modificati alla fonte da evidenziare in risca-dati-soggetto */
  fonteConfigs: string[] = [];
  /** Boolean che definisce lo stato di disabilitazione degli elementi del componente, per: inserisci/modifica soggetto. */
  AEA_pratica_DADisabled = false;

  /**
   * ##############################################
   * SUBSCRIPTION AL SALVATAGGIO DELLE INFORMAZIONI
   * ##############################################
   */
  /** Subscription che permette di sapere quando la tab dello stato debitorio viene cambiato dall'utente. */
  onSezioneSDChange: Subscription;

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
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _datiContabili: DatiContabiliService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    private _gruppo: GruppoService,
    private _gruppoSoggetto: GruppoSoggettoService,
    navigationHelper: NavigationHelperService,
    private _recapiti: RecapitiService,
    private _rimborsoModal: DatiContabiliModalService,
    riscaAlert: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaMessages: RiscaMessagesService,
    riscaUtilities: RiscaUtilitiesService,
    private _soggettoDA: SoggettoDatiAnagraficiService,
    private _spinner: RiscaSpinnerService,
    private _statoDebitorioServ: StatoDebitorioService
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
    // Lancio l'init dei listener del componente
    this.initListeners();
  }

  /**
   * Destroy
   * @override
   */
  ngOnDestroy(): void {
    // Invoco il destroy del padre
    super.ngOnDestroy();
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onSezioneSDChange) {
        this.onSezioneSDChange.unsubscribe();
      }
      if (this.onSDInsSuccess) {
        this.onSDInsSuccess.unsubscribe();
      }
      if (this.onSDInsError) {
        this.onSDInsError.unsubscribe();
      }
      if (this.onSDInsCancel) {
        this.onSDInsCancel.unsubscribe();
      }
      if (this.onSDModSuccess) {
        this.onSDModSuccess.unsubscribe();
      }
      if (this.onSDModError) {
        this.onSDModError.unsubscribe();
      }
      if (this.onSDModCancel) {
        this.onSDModCancel.unsubscribe();
      }
    } catch (e) {}
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
    // Richiamo la configurazione della tabella dei recapiti alternativi
    this.setupDARecapitiAlternativiTable();
    // Richiamo la configurazione della tabella dei componenti del gruppo
    this.setupComponentiGruppiTable();
  }

  /**
   * Funzione di init per le configurazioni relative alla pagina del soggetto.
   */
  setupAEASoggetto() {
    // Recupero la chiave per la configurazione della form
    const daKey = this.AEAK_C.PAGINA_DATI_ANAG;
    // Recupero la configurazione della form dal servizio
    // this.AEA_pratica_DADisabled = this._accessoElementiApp.isAEADisabled(daKey);
  }

  /**
   * Funzione di comodo che permette l'inizializzazione delle tabelle.
   * @param recapiti Array di RecapitoVo con i dati di complicazione della tabella.
   */
  private setupDARecapitiAlternativiTable(recapiti?: RecapitoVo[]) {
    // Variabile di comodo
    const isGestioneAbilitata = this.isGestioneAbilitata;
    const idGruppo = this.gruppoSD?.id_gruppo_soggetto;
    const hideActions = true;
    const activeSelection = true;
    const disableUserInputs = true; // this.disableUserInputs;
    const isStatoDebitorio = true;
    // Definisco gli oggetti di configurazione delle tabelle
    const cra: IDARecapitiAlternativiTableConfigs = {
      recapiti,
      isGestioneAbilitata,
      disableUserInputs,
      idGruppo,
      hideActions,
      activeSelection,
      isStatoDebitorio,
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
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  private initComponent() {
    // Lancio il setup della form del componente (usato come struttura d'appoggio)
    this.initFormDatiAnagrafici();
    // Verifico se è stato passato un oggetto per il pre-caricamento
    this.initDatiAnagraficiSD();
  }

  /**
   * Setup del form: mainForm.
   */
  private initFormDatiAnagrafici() {
    this.mainForm = this._formBuilder.group({
      soggettoPratica: new FormControl(),
      gruppoPratica: new FormControl(),
      recapitiPratica: new FormControl(),
    });
    this.mainForm.markAsPristine();
  }

  /**
   * Funzione che recupera le informazioni per lo scarico dati del soggetto, gruppo ed eventuale recapito alternativo selezionato.
   * La gestione d'inserimento/modifica viene fatta nel componente stato-debitorio.component.ts.
   * In inserimento il soggetto dello stato debitorio viene subito settato partendo dai dati della pratica.
   * In modifica i dati saranno definiti dallo scarico dello stato debitorio.
   */
  private initDatiAnagraficiSD() {
    // Recupero dallo stato debitorio id gruppo e soggetto
    const idSoggetto = this._datiContabiliUtility?.idSoggettoSD;
    const idGruppo = this._datiContabiliUtility?.idGruppoSD;

    // Creo l'oggetto di request per lo scarico delle informazioni
    const req: ISGStatoDebitorioReq = {
      soggetto: this._soggettoDA.getSoggetto(idSoggetto),
    };

    // Verifico se l'id gruppo esiste come configurazione
    if (idGruppo != undefined) {
      // L'id gruppo esiste: aggiungo la chiamata
      req.gruppo = this._gruppoSoggetto.getGruppo(idGruppo);
    }

    // In forkjoin richiamo i servizi di scarico dati
    forkJoin(req).subscribe({
      next: (res: ISGStatoDebitorioRes) => {
        // Estraggo dalla response soggetto e gruppo
        const { soggetto, gruppo } = res;
        // Lancio il set per le informazioni del componente
        this.impostaDatiComponente(soggetto, gruppo);
        // #
      },
      error: (error: RiscaServerError) => {
        // Gestione errori
        this.onServiziError(error);
        // #
      },
    });
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
    this.soggettoSD = da;
    // Creo una copia iniziale per il soggetto della pratica
    this.soggettoPInit = clone(da);

    // Recupero l'id_recapito principale dai dati anagrafici
    const { id_recapito } =
      this._recapiti.getRecapitoPrincipaleFromSoggettoVo(da) || {};
    // Imposto l'id recapito (può essere undefined)
    this.idRecapitoP = id_recapito;

    // Resetto il flag visibilita
    this.showDatiAnagrafici = true;
  }

  /**
   * Funzione di supporto per la generazione della tabella riguardante: recapiti alternativi.
   * @param s SoggettoVo per il setup.
   */
  private setupRecapitiAlternativiTable(s: SoggettoVo) {
    // Verifico l'input
    if (!s) {
      // Inizializzo la tabella senza dati
      this.setupDARecapitiAlternativiTable();
      // #
    } else {
      // Estraggo i recapiti alternativi
      const recapitiAlt: RecapitoVo[] =
        this._recapiti.getRecapitiAlternativiFromSoggettoVo(s);
      // Genero la tabella con i dati dei recapiti alternativi
      this.setupDARecapitiAlternativiTable(recapitiAlt);

      // Recupero dallo stato debitorio l'id recapito selezionato
      const idRSD = this._datiContabiliUtility.idRecapitoSD;
      // Verifico se l'id dello stato debitorio è quello di un recapito alternativo
      const recapitoAPratica = recapitiAlt?.find((rA: RecapitoVo) => {
        // Ritorno il confronto tra id
        return rA.id_recapito == idRSD;
      });

      // Definisco la funzione per la selezione dell'elemento principale
      const f = (r: RecapitoVo) => {
        // Uso la funzione di supporto per definire il selected
        return this.tableRecapitoAlternativoSelected(r, recapitoAPratica);
      };
      // Provo ad impostare il recapito alternativo selezionato
      const sel = this.recapitiAlternativiTable.setElementSelectedExclusive(f);
      // Se è stato selezionato, definisco il recapito come selezionato
      if (sel) {
        // Assegno localmente il recapito alternativo stato debitorio
        this.recapitoASD = recapitoAPratica;
      }
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
    this.gruppoSD = gruppo;

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
   * ###############################################################
   * INIZIALIZZAZIONE LISTENER AD EVENTI DI MODIFICA STATO DEBITORIO
   * ###############################################################
   */

  /**
   * Funzione di init per i listener del componente, per rimanere in ascolto degli eventi per il cambio pagina dei dati contabili.
   */
  private initListeners() {
    this.onSezioneSDChange =
      this._statoDebitorioServ.onSezioneSDChanges$.subscribe({
        next: (tabs: IRiscaTabChanges) => {
          // Lancio la funzione di cambio tab
          this.tabSDChanged(tabs);
        },
      });

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
   * Gestisce le operazioni da eseguire alla fine del successo nel salvataggio dello stato debitorio.
   * @param statoDebitorio StatoDebitorioVo salvato.
   */
  onSDSuccessResult(statoDebitorio: StatoDebitorioVo) {
    // Il componente attiva la modalità modifica
    this.modalita = AppActions.modifica;
  }

  /**
   * Gestisce le operazioni da eseguire in caso di errore nel salvataggio dello stato debitorio.
   */
  onSDErrorResult(error: RiscaServerError) {
    // Gestione della risposta dall'ascoltatore
  }

  /**
   * Gestisce le operazioni da eseguire alla fine della cancellazione nel salvataggio dello stato debitorio.
   */
  onSDCancelResult() {
    // Gestione della risposta dall'ascoltatore
  }

  /**
   * ################################################
   * FUNZIONI PER LA COMUNICAZIONE DI ERRORI AL PADRE
   * ################################################
   */

  /**
   * Funzione di comodo che imposta il messaggio d'errore
   * @param error RiscaServerError che definisce il corpo dell'errore.
   */
  protected onServiziError(error: RiscaServerError) {
    // Emetto l'evento per la gestione degli errori per i figli dello stato debitorio
    this._statoDebitorioServ.onGADError(error);
  }

  /**
   * #############################
   * GESTIONE RECAPITI ALTERNATIVI
   * #############################
   */

  /**
   * Funzione che imposta i dati di un recapito alternativo, inviato dalla tabella, come recapito alternativo della pratica.
   * @param row RiscaTableDataConfig<RecapitoVo> contenente i dati della riga.
   */
  onRecapitoAlternativoSelezionato(row: RiscaTableDataConfig<RecapitoVo>) {
    // Salvo l'oggetto localmente
    this.recapitoASD = row.original;
  }

  /**
   * ##################################
   * PULSANTI PER AZIONI DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione che resetta la selezione della riga per la tabella dei recapiti alternativi.
   */
  onRecapitiAlternativiScollegati() {
    // Lancio la funzione di deselezione per la tabella dei recapiti alternativi
    this.riscaTRecapitiAlt.resetRadio();
    // Resetto la selezione della riga locale
    this.recapitoASD = undefined;
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
    const onConfirm = (callback: ICCRMCloseParams) => {
      // Richiamo la funzione di gestione dei dati
      this.onCCRMClose(callback);
    };

    // Richiamo il servizio per l'apertura del modale
    this._rimborsoModal.apriCambiaTitolareModal({ onConfirm });
  }

  /**
   * Funzione che gestisce le logiche alla chiusura della modale per la ricerca di un titolare per la pratica.
   * @param params ICCRMCloseParams con le informazioni di ritorno.
   */
  private onCCRMClose(params: ICCRMCloseParams) {
    // Recupero dall'input le informazioni
    const { data } = params || {};
    // Richiamo la funzione di gestione dati del titolare
    this.gestisciDatiAnagraficiTitolare(data);
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
    };

    // Verifico se esistono già dei dati del referente
    if (this.soggettoSD) {
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
   * ##################################################################
   * FUNZIONALITA PER LA GESTIONE DEL SUBMBIT/RESET DEL FORM COME PADRE
   * ##################################################################
   */

  /**
   * Avviata alla pressione del pulsante Salva modifiche.
   */
  salvaModifiche() {
    // Recuperare lo stato debitorio aggiornato
    const sd = this.aggiornaDatiAnagraficiSD();
    // Effettuo il salvataggio dello stato debitorio
    this.salvaStatoDebitorio(sd);
  }

  /**
   * Funzione che recupera e aggiorna i dati anagrafici dello stato debitorio all'interno del servizio condiviso.
   * Una volta aggiornati i dati, ritorno l'oggetto dello stato debitorio.
   * @returns StatoDebitorioVo con le informazioni aggiornate.
   */
  private aggiornaDatiAnagraficiSD(): StatoDebitorioVo {
    // Variabili di comodo
    const s = this.soggettoSD;
    const g = this.gruppoSD;
    // Recupero gli id di soggetto ed eventualmente gruppo
    const idSoggetto = s.id_soggetto;
    const idGruppo = g?.id_gruppo_soggetto;

    // Per il recapito alternativo esiste della logica tra alternativi e principale
    let idRecapito: number;
    // Verifico se è stato selezionato un recapito alternativo o devo recuperare il recapito principale
    if (this.recapitoASD) {
      // Esiste un recapito alternativo, ha la priorità
      idRecapito = this.recapitoASD.id_recapito;
      // #
    } else {
      // Recupero il recapito principale del soggetto
      const rp = this._riscaUtilities.recapitoPrincipaleSoggetto(s);
      // Recupero l'id del recapito principale
      idRecapito = rp?.id_recapito;
      // #
    }

    // Preparo l'oggetto con i dati da aggiornare sullo stato debitorio
    const data: IDatiAnagraficiSDUpd = {
      idSoggetto,
      idGruppo,
      idRecapito,
    };

    // Chiamo la funzione di aggiornamento dello stato debitorio
    this._datiContabiliUtility.updateDatiAnagrafici(data);
    // Ritorno i dati aggiornati
    return this._datiContabiliUtility.statoDebitorio;
  }

  /**
   * Funzione che verifica la modalità di salvataggio e lancia il salva dello stato debitorio.
   * @param statoDebitorio StatoDebitorioVo con le informazioni da salvare.
   */
  private salvaStatoDebitorio(statoDebitorio: StatoDebitorioVo) {
    // In base alla modalità eseguo verifica e salvataggio
    if (this.inserimento) {
      // Lancio il servizio di verifica ed inserimento dello stato debitorio
      this._datiContabili.verifyAndInsertStatoDebitorio(statoDebitorio);
    } else {
      // Lancio il servizio di verifica ed aggiornamento dello stato debitorio
      this._datiContabili.verifyAndUpdateStatoDebitorio(statoDebitorio);
    }
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione di comodo che crea i dati ed emette l'evento di cambio tab dello stato debitorio.
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   */
  private tabSDChanged(tabs: IRiscaTabChanges) {
    // Recupero l'identificativo di questo componente
    const tabComp = RiscaStatoDebitorio.datiAnagrafici;

    // Verifico tramite servizio se ci si sta spostando su un'altra tab
    if (this._statoDebitorioServ.checkTabSD(tabs, tabComp)) {
      // Mi sto spostando, aggiorno i dati dello stato debitorio nel servizio
      this.aggiornaDatiAnagraficiSD();
    }
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
    return false; // this._soggettoDA.isGestioneAbilitata;
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
    const existGSC = this.gruppoSD !== undefined;
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
    const existG = this.gruppoSD !== undefined;

    // Verifico se esiste o non esiste l'oggetto del gruppo selezionato
    if (existG) {
      // Recupero la testata con il testo per il gruppo
      let testata = this.DA_C.TESTO_TESTATA_CON_GRUPPO;
      // Recupero il placeholder della label
      let ph = this.DA_C.TESTO_TESTATA_CON_GRUPPO_PLACEHOLDER;
      // Recupero la descrizione del gruppo
      let descGruppo = this.gruppoSD.des_gruppo_soggetto;
      // Sostiutisco il placeholder con la descrizione e ritorno la label
      return testata.replace(ph, descGruppo);
      // #
    } else {
      // Ritorno la testata senza gruppo selezionato
      return this.DA_C.TESTO_TESTATA_SENZA_GRUPPO;
    }
  }

  /**
   * Getter di verifica per la tabella dei recapiti alternativi.
   */
  get checkRecapitiA() {
    // Verifico che la tabella esista e abbia dati
    return this.recapitiAlternativiTable?.source?.length > 0;
  }

  /**
   * Getter che verifica che le informazioni per la configurazione di alertCEConfigs siano valide.
   */
  get alertCEConfigscheck() {
    // Verifico che la configurazione per l'alert delle comunicazioni esista e contenga dati
    return this.alertCEConfigs?.messages?.length > 0;
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
   * Getter di comodo per l'idPratica da passare al verify.
   */
  get idPraticaPerVerify() {
    return this.modalita == AppActions.modifica ? this.idPratica : undefined;
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return (
      this.AEA_pratica_DADisabled ||
      this.isPraticaLocked ||
      this.AEA_detSDDisabled
    );
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
   * Getter per il recupero del flag per AEA: dettaglio stato debitorio.
   * @returns boolean con il valore della configurazione.
   */
  get AEA_detSDDisabled(): boolean {
    // Verifico se la configurazione di accesso agli elementi è attivo
    let AEA_detSDDisabled: boolean;
    AEA_detSDDisabled = this._accessoElementiApp.isAEADisabled(
      this.AEAK_C.DET_STATO_DEB
    );

    // Ritorno la configurazione
    return AEA_detSDDisabled;
  }
}
