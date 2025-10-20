import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { cloneDeep } from 'lodash';
import { IndirizzoSpedizioneVo } from '../../../core/commons/vo/indirizzo-spedizione-vo';
import { AccessoElementiAppKeyConsts } from '../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../core/services/logger.service';
import { NavigationHelperService } from '../../../core/services/navigation-helper/navigation-helper.service';
import { AmbitoService } from '../../../features/ambito/services';
import { IndirizziSpedizioneService } from '../../../features/pratiche/service/dati-anagrafici/indirizzi-spedizione.service';
import { VerificaIndirizzoSpedizione } from '../../../features/pratiche/types/indirizzi-spedizione/indirizzi-spedizione.types';
import { RiscaFormParentComponent } from '../../components/risca/risca-form-parent/risca-form-parent.component';
import { RiscaFormSubmitHandlerService } from '../../services/risca/risca-form-submit-handler.service';
import { RiscaIndirizziSpedizioneModalService } from '../../services/risca/risca-indirizzi-spedizione-modal/risca-indirizzi-spedizione-modal.service';
import { RiscaMessagesService } from '../../services/risca/risca-messages.service';
import { isSBNTrue, riscaServerMultiErrors } from '../../services/risca/risca-utilities/risca-utilities.functions';
import { IRiscaFormTreeParent, RiscaAlertConfigs, RiscaButtonConfig, RiscaGestioneISModal, RiscaInfoLevels, RiscaServerError } from '../../utilities';
import { RiscaNotifyCodes } from '../../utilities/enums/risca-notify-codes.enums';
import { RiscaAlertService } from './../../services/risca/risca-alert.service';
import { IndirizzoSpedizioneConfig, ISComponentConfig } from './utilities/risca-indirizzi-spedizione-modal.classes';
import { IndirizziSpedizioneModalConsts } from './utilities/risca-indirizzi-spedizione-modal.consts';
import { IIndirizziSpedizioneModalConfig } from './utilities/risca-indirizzi-spedizione-modal.interfaces';

@Component({
  selector: 'risca-indirizzi-spedizione-modal',
  templateUrl: './risca-indirizzi-spedizione-modal.component.html',
  styleUrls: ['./risca-indirizzi-spedizione-modal.component.scss'],
})
export class RiscaIndirizziSpedizioneModalComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti per il componente attuale. */
  RISM_C = new IndirizziSpedizioneModalConsts();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** RiscaButtonConfig che definisce la struttura di default del pulsante: ANNULLA. */
  BTN_ANNULLA: RiscaButtonConfig = { label: this.RISM_C.LABEL_ANNULLA };
  /** RiscaButtonConfig che definisce la struttura di default del pulsante: CONFERMA. */
  BTN_CONFERMA: RiscaButtonConfig = { label: this.RISM_C.LABEL_CONFERMA };

  /** IIndirizziSpedizioneModalConfig che definisce i dati di configurazione per la modale. */
  @Input() dataModal: IIndirizziSpedizioneModalConfig;

  /** Boolean che definisce la configurazione di accesso all'elemento dell'app per il salvataggio dati. */
  private AEA_SISDisabled: boolean = false;
  /** Boolean che definisce la configurazione di accesso all'elemento dell'app per il salvataggio dati. */
  private AEA_DISDisabled: boolean = false;

  /** Array di IndirizzoSpedizioneConfig, con le configurazioni dati per la gestione dei vari indirizzi di spedizione. */
  indirizziConfigs: IndirizzoSpedizioneConfig[] = [];
  /** Array di IndirizzoSpedizioneVo che definsce le informazioni di appoggio degli indirizzi corretti, che verranno poi ritornati al chiamante. */
  private _indirizziOK: IndirizzoSpedizioneVo[] = [];

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertTopConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();
  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert riferita alla gestione gruppi. */
  alertGruppiConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    public activeModal: NgbActiveModal,
    private _ambito: AmbitoService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _indirizziSpedizione: IndirizziSpedizioneService,
    private _riscaISM: RiscaIndirizziSpedizioneModalService,
    riscaMessages: RiscaMessagesService
  ) {
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages,
    );

    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
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
   * Funzione di setup per le informazioni del componente.
   */
  private setupComponente() {
    // Recupero il flag di abilitazione per il profilo
    this.setupSISDisabled();
    // Lancio il setup per l'alert dei gruppi
    this.setupAlertGruppi();
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  private setupSISDisabled() {
    // Recupero la chiave per la configurazione della form
    const sisKey = this.AEAK_C.SALVA_INDIRIZZO_SPED;
    // Recupero la configurazione della form dal servizio
    this.AEA_SISDisabled = this._accessoElementiApp.isAEADisabled(sisKey);
    // Recupero la chiave per la configurazione della form
    const disKey = this.AEAK_C.DETTAGLIO_IND_SPED;
    // Recupero la configurazione della form dal servizio
    this.AEA_DISDisabled = this._accessoElementiApp.isAEADisabled(disKey);

    // Aggiorno la configurazione dei pulsanti
    this.BTN_CONFERMA.codeAEA = sisKey;
    this.BTN_ANNULLA.codeAEA = sisKey;
  }

  /**
   * Funzione di setup per la configurazione dell'alert adibito alla gestione dei gruppi.
   */
  private setupAlertGruppi() {
    // Recupero il messaggio di notifica per la gestione dei gruppi
    const a = this.alertGruppiConfigs;
    const m = [this._riscaMessages.getMessage(RiscaNotifyCodes.A021)];
    const i = RiscaInfoLevels.warning;

    // Configuro l'alert
    this.aggiornaAlertConfigs(a, m, i);
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di init per le informazioni del componente.
   */
  private initComponente() {
    // Lancio la configurazione degli indirizzi
    this.initIndirizziSpedizione(this.dataModal);
    // Lancio le funzioni per la gestione del flusso per modale (gestione singolo indirizzo di spedizione o correzione degli errori)
    this.initFlussoModale(this.indirizziConfigs);
  }

  /**
   * Funzione che gestisce la generazione delle informazioni per quanto riguarda gli indirizzi di spedizione.
   * @param dataModal IIndirizziSpedizioneModalConfig con le configurazioni per la modale.
   */
  private initIndirizziSpedizione(dataModal: IIndirizziSpedizioneModalConfig) {
    // Richiamo al funzione del servizio per le configurazioni
    const configs = this._riscaISM.initModalConfigs(dataModal);

    // Estraggo le informazioni per le configurazioni
    const { indirizziConfigs, children } = configs;
    // Imposto le informazioni per gli indirizzi corretti
    this._indirizziOK = cloneDeep(dataModal?.indirizziSpedizione);

    // Definisco gli elementi di configurazione
    this.indirizziConfigs = indirizziConfigs;
    // Lancio l'init per le chiavi dei componenti figli
    this.initiFormSubmitHandlerService(children);
  }

  /**
   * Funzione di init per il servizio RiscaFormSubmitHandlerService.
   * @param children Array di string che definisce la lista d'id dei figli generati per la modale.
   */
  private initiFormSubmitHandlerService(children: string[]) {
    // Definisco la chiave per il parent
    const parent = this.RISM_C.FORM_KEY_PARENT;
    // Richiamo il super
    this.setFormsSubmitHandler(parent, children);
  }

  /**
   * Funzione che gestisce le logiche di flusso per la modale (gestione singolo indirizzo di spedizione o correzione degli errori).
   * @param indirizzi Array di IndirizzoSpedizioneConfig con i dati degli indirizzi.
   */
  initFlussoModale(indirizzi: IndirizzoSpedizioneConfig[]) {
    // Verifico l'input
    if (!indirizzi || indirizzi.length === 0) {
      // Non si possono fare logiche
      return;
    }

    // Verifico qual è la modalità di gestione richiesta
    if (this.aggiornamento) {
      // Se siamo in aggiornamento ci sarà solo un oggetto (sennò qualcosa non va)
      const indirizzo = indirizzi[0];
      // Recupero l'oggetto puntuale dell'indirizzo di spedizione
      const { indirizzoSpedizione } = indirizzo;
      // Lancio la funzione di verifica per l'indirizzo di spedizione
      this.verifySoloIndirizzoSpedizione(indirizzoSpedizione);
    }
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione che gestisce la generazione delle informazioni per quanto riguarda gli indirizzi di spedizione, come aggiornamento.
   * @param indirizziSpedizione Array di IndirizzoSpedizione con le configurazioni per la modale.
   */
  private updateIndirizziSpedizione(
    indirizziSpedizione: IndirizzoSpedizioneVo[]
  ) {
    // Recupero dalla configurazione dataModal la modalità di gestione
    const modalita = this.dataModal?.modalita;
    // Creo l'oggetto da passare alla funzione di generazione dati
    const data = new ISComponentConfig({ modalita, indirizziSpedizione });

    // Richiamo la fuzione del servizio per generare le configurazioni
    const configs = this._riscaISM.initIndirizziSpedizione(data);

    // Estraggo le informazioni per le configurazioni
    const { indirizziConfigs, children } = configs;

    // Definisco gli elementi di configurazione
    this.indirizziConfigs = indirizziConfigs;
    // Lancio l'init per le chiavi dei componenti figli
    this.initiFormSubmitHandlerService(children);
  }

  /**
   * Funzione dedicata al check iniziale per l'indirizzo di spedizione aperto.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo da verificare.
   */
  private verifySoloIndirizzoSpedizione(
    indirizzoSpedizione: IndirizzoSpedizioneVo
  ) {
    // Lancio la funzione di verifica per l'indirizzo di spedizione
    this._indirizziSpedizione
      .verifyIndirizzoSpedizione(indirizzoSpedizione)
      .subscribe({
        next: (indirizzoChecked: IndirizzoSpedizioneVo) => {
          // Validato correttamente, nient'altro da fare
        },
        error: (e: RiscaServerError) => {
          // Richiamo la funzione di gestione degli errori
          this.alertIndirizzoSpedizione(e);
        },
      });
  }

  /**
   * Funzione che gestisce la comunicazione del risultato delle verifiche (in errore) per l'aggiornamento di un indirizzo di spedizione.
   * @param e RiscaServerError contenente le informazioni sugli errori generati.
   */
  private alertIndirizzoSpedizione(e: RiscaServerError) {
    // Verifico se esiste la proprietà array d'errore, che gestisce le varie parti invalide dell'indirizzo di spedizione
    if (riscaServerMultiErrors(e)) {
      // Esiste la lista d'errori, li segnalo all'utente
      const m = this._riscaISM.messaggiErroreVerifyIS(e.errors);
      // Definisco le configurazioni per l'alert
      const a = this.alertTopConfigs;
      const d = RiscaInfoLevels.danger;
      // Aggiorno l'alert con i messaggi d'errore
      this.aggiornaAlertConfigs(a, m, d);
      // #
    } else {
      // Gestisco normalmente l'errore
      this.onServiziError(e);
    }
  }

  /**
   * Funzione di comodo che aggiorna un indirizzo di spedizione all'interno dell'array di supporto "_indirizziOK".
   * @param indirizzoUpd IndirizzoSpedizioneVo che identifica l'oggetto con i dati aggiornati.
   * @returns boolean che informa il chiamante se l'oggetto è stato aggiornato correttamente.
   */
  private aggiornaIndirizzoOK(indirizzoUpd: IndirizzoSpedizioneVo): boolean {
    // Verifico l'input
    if (!indirizzoUpd) {
      // Non ho i dati d'aggiornare
      return false;
    }

    // Recupero i dati di supporto
    const indirizziOK = this._indirizziOK;
    // Vado a recuperare l'indice dell'oggetto nell'array di supporto
    const iIS = indirizziOK.findIndex((iOK: IndirizzoSpedizioneVo) => {
      // Ritorno il confronto tra gli id degli oggetti
      return iOK.id_recapito_postel === indirizzoUpd.id_recapito_postel;
    });

    // Verifico di aver trovato l'id
    if (iIS !== -1) {
      // Oggetto trovato, lo aggiorno
      indirizziOK[iIS] = indirizzoUpd;
      // Operazione completata
      return true;
      // #
    } else {
      // Elemento non trovato
      return false;
    }
  }

  /**
   * ######################################################
   * FUNZIONI PER LA GESTIONE DEI SUBMIT/RESET DEI CHILDREN
   * ######################################################
   */

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsValid.
   * @param formsValid IRiscaFormTreeParent con i dati validi.
   * @override
   */
  formsValid(formsValid: IRiscaFormTreeParent) {
    // Dichiaro una mappa come contenitore dei dati dei figli
    let mapData: Map<string, IndirizzoSpedizioneVo>;
    mapData = this.getRiscaFormTreedData(formsValid);

    // Recupero la lista dei children
    const children = this.childrenKeys;
    // Genero un array di indirizzi di spedizione, recuperando gli oggetti dalla mappa tramite chiavi
    const indirizziForms = children.map((child: string) => {
      // Ritorno l'oggetto per la chiave
      return mapData.get(child);
    });

    // Verifico la modalità di gestione della modale
    if (this.aggiornamento) {
      // Richiamo la funzione che gestisca l'aggiornamento di un indirizzo di spedizione
      this.gestisciAggiornamentoIS(indirizziForms);
      // #
    } else {
      // Richiamo la funzione che gestisca l'aggiornamento di tutti gli indirizzi di spedizione corretti
      this.gestisciCorrezioneIS(indirizziForms);
    }
  }

  /**
   * ##################################################
   * GESTIONE FLUSSO AGGIORNAMENTO INDIRIZZO SPEDIZIONE
   * ##################################################
   */

  /**
   * Funzione che gestisce l'aggiornamento dell'indirizzo di spedizione, partendo dai dati ottenuti dal submit della form.
   * @param indirizziForms Array di IndirizzoSpedizioneVo con le informazioni generate dai vari form degli indirizzi di spedizione.
   */
  private gestisciAggiornamentoIS(indirizziForms: IndirizzoSpedizioneVo[]) {
    // Verifico l'input
    if (!indirizziForms || indirizziForms.length === 0) {
      // Non ci sono dati da elaborare
      return;
    }

    // Essendo in aggiornamento, recupero solo il primo (e unico) elemento dall'array
    const indirizzo: IndirizzoSpedizioneVo = indirizziForms[0];
    // Richiamo la funzione d'aggiornamento del singolo indirizzo
    this.aggiornaIndirizzoSpedizione(indirizzo);
  }

  /**
   * Funzione che gestisce il flusso d'aggiornamento di un singolo indirizzo di spedizione.
   * @param indirizzo IndirizzoSpedizioneVo d'aggiornare.
   */
  private aggiornaIndirizzoSpedizione(indirizzo: IndirizzoSpedizioneVo) {
    // Verifico l'input
    if (!indirizzo) {
      // Non ci sono dati da elaborare
      return;
    }

    // Richiamo l'aggiornamento dell'indirizzo di spedizione
    this._indirizziSpedizione.updateIndirizzoSpedizione(indirizzo).subscribe({
      next: (indirizzoUpd: IndirizzoSpedizioneVo) => {
        // Richiamo la chiusura con la funzione di comodo
        this.onIndirizzoAggiornato(indirizzoUpd);
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione degli errori
        this.alertIndirizzoSpedizione(e);
        // #
      },
    });
  }

  /**
   * Funzione di comodo che gestisce i dati del singolo oggetto IndirizzoSpedizioneVo aggiornato e lo ritorna al componente chiamante.
   * @param indirizzoUpd IndirizzoSpedizioneVo come oggetto aggiornato sul DB.
   */
  private onIndirizzoAggiornato(indirizzoUpd: IndirizzoSpedizioneVo) {
    // Variabili di comodo
    const iiOK = this._indirizziOK;
    const iUpd = indirizzoUpd;

    // Vado ad aggiornare l'array di elementi
    const success = this._riscaISM.aggiornaIndirizzoOK(iiOK, iUpd);

    // Verifico il risultato dell'operazione
    if (success) {
      // Operazione andata a buon fine, chiudo la modale passando le informazioni
      this.modalConfirm(iiOK);
      // #
    } else {
      // Qualcosa non è andato bene, genero un errore
      const e = new RiscaServerError({
        error: { code: RiscaNotifyCodes.E005 },
      });
      // Lancio la gestione tramite alert
      this.onServiziError(e);
    }
  }

  /**
   * ###############################################
   * GESTIONE FLUSSO CORREZIONE INDIRIZZI SPEDIZIONE
   * ###############################################
   */

  /**
   * Funzione che gestisce la correzione degli indirizzi di spedizione, partendo dai dati ottenuti dal submit delle form.
   * @param indirizziForms Array di IndirizzoSpedizioneVo con le informazioni generate dai vari form degli indirizzi di spedizione.
   */
  private gestisciCorrezioneIS(indirizziForms: IndirizzoSpedizioneVo[]) {
    // Verifico l'input
    if (!indirizziForms || indirizziForms.length === 0) {
      // Non ci sono dati da elaborare
      return;
    }

    // Lancio la verifica di tutti gli indirizzi che sono stati forniti
    this._indirizziSpedizione
      .verifyIndirizziSpedizione(indirizziForms)
      .subscribe({
        next: (indirizziVerify: VerificaIndirizzoSpedizione[]) => {
          // Richiamo la funzione che gestirà il risultato della verifica
          this.onVerificaIndirizziSpedizione(indirizziVerify);
        },
        error: (e: RiscaServerError) => {
          // Richiamo la gestione dell'errore
          this.onServiziError(e);
        },
      });
  }

  /**
   * Funzione invocata nel momento in cui viene ottenuta risposta dal servizio di verifica degli indirizzi di spedizione per la correzione.
   * @param indirizziVerify Array di VerificaIndirizzoSpedizione con le informazioni relative alle verifiche fatte.
   */
  private onVerificaIndirizziSpedizione(
    indirizziVerify: VerificaIndirizzoSpedizione[]
  ) {
    // Verifico l'input
    if (!indirizziVerify || indirizziVerify.length === 0) {
      // Nessun dato da elaborare
      return;
    }

    // Lancio la funzione di supporto per divere le informazioni tra indirizzi validi/invalidi
    const { indirizziValidi, indirizziInvalidi } =
      this._indirizziSpedizione.risultatiVerifyIndirizziSpedizione(
        indirizziVerify
      );

    // Aggiorno tutti gli indirizzi che risultano corretti
    indirizziValidi.forEach((iv) => this.aggiornaIndirizzoOK(iv));

    // Verifico se ci sono indirizzi in errore
    if (indirizziInvalidi.length > 0) {
      // Lancio la funzione di gestione degli errori
      this.onErrorVerificaIS(this._indirizziOK);
      // #
    } else {
      // Lancio la funzione di gestione del success
      this.onSuccessVerificaIS(this._indirizziOK);
    }
  }

  /**
   * Funzione che gestisce le logiche sul "success" della verifica per correzione indirizzi.
   * @param indirizzi Array di IndirizzoSpedizioneVo con tutte le informazioni validate da ritornare al chiamante.
   */
  onSuccessVerificaIS(indirizzi: IndirizzoSpedizioneVo[]) {
    // Richiamo la chiusura della modale
    this.modalConfirm(indirizzi);
  }

  /**
   * Funzione che gestisce le logiche sull' "error" della verifica per correzione indirizzi.
   * @param indirizzi Array di IndirizzoSpedizioneVo con tutte le informazioni validate da ritornare al chiamante.
   */
  onErrorVerificaIS(indirizzi: IndirizzoSpedizioneVo[]) {
    // Vado a ricreare la struttura dei form degli indirizzi
    this.updateIndirizziSpedizione(indirizzi);
  }

  /**
   * ###############################################
   * FUNZIONE PER LA GESTIONE DEI PULSANTI IN PAGINA
   * ###############################################
   */

  /**
   * Funzione agganciata al pulsante di annulla.
   * Richiede il reset delle informazioni delle form e rilancia la validazione.
   */
  annullaIndirizzi() {
    // Resetto l'alert
    this.resetAlertConfigs();
    // Richiamo l'evento per informare i children della richiesta di reset
    this._riscaFormSubmitHandler.parentReset(this.parentKey);
  }

  /**
   * Funzione agganciata al pulsante di conferma.
   * Richiede la validazione di tutte le form per gli indirizzi di spedizione.
   */
  confermaIndirizzi() {
    // Verifico che si possa salvare gli indirizzi
    if (this.AEA_SISDisabled) {
      // Blocco il salvataggio
      return;
    }

    // Resetto l'alert
    this.resetAlertConfigs();
    // Richiamo l'evento per informare i children della richiesta di reset
    this._riscaFormSubmitHandler.parentSubmit(this.parentKey);
  }

  /**
   * ##################################
   * FUNZIONI PER GESTIONE DELLA MODALE
   * ##################################
   */

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalDismiss() {
    // Dismiss della modale
    this.activeModal.dismiss();
  }

  /**
   * Funzione che effettua la close (e quindi la resolved) della modale.
   * @param indirizziOK Array di IndirizzoSpedizioneVo che definisce il dato di ritorno della modale.
   */
  modalConfirm(indirizziOK: IndirizzoSpedizioneVo[]) {
    // Close della modale
    this.activeModal.close(indirizziOK);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di comodo per la gestione della struttura del DOM, basata sui dati di configurazione degli indirizzi di spedizione.
   */
  get checkIndirizziConfigs() {
    // Verifico che esista la struttura
    return this.indirizziConfigs?.length > 0;
  }

  /**
   * Getter di comodo per gli indirizzi passati alla modale come configurazione.
   */
  get indirizziSpedizione() {
    // Ritorno gli indirizzi passati alla modale
    return this.dataModal?.indirizziSpedizione;
  }

  /**
   * Getter di comodo per i recapiti passati alla modale come configurazione.
   */
  get recapiti() {
    // Ritorno i recapiti passati alla modale
    return this.dataModal?.recapiti;
  }

  /**
   * Getter di comodo per i gruppi passati alla modale come configurazione.
   */
  get gruppi() {
    // Ritorno i gruppi passati alla modale
    return this.dataModal?.gruppi;
  }

  /**
   * Getter di comodo che estrae e verifica la modalità di gestione della modale.
   */
  get correzione() {
    // Ritorno il check sulla modalità di gestione
    return this.dataModal?.gestione === RiscaGestioneISModal.correzione;
  }

  /**
   * Getter di comodo che estrae e verifica la modalità di gestione della modale.
   */
  get aggiornamento() {
    // Ritorno il check sulla modalità di gestione
    return (
      this.dataModal?.gestione === RiscaGestioneISModal.aggiornamentoSingolo
    );
  }

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertTopConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertTopConfigs);
  }

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertGruppiConfigsCheck() {
    // Verifico il tipo di gestione
    const isAggiornamento = this.aggiornamento;
    // Verifico se sono stati passati gruppi alla modale
    const gruppiCheck = this.gruppi?.length > 0;
    // Verifico se la configurazione a livello di ambito, per i gruppi, è abilitata
    const isGruppoAbilitato = this.isGruppoAbilitato;

    // Controllo le condizioni principali di visualizzazione
    if (isAggiornamento && gruppiCheck && isGruppoAbilitato) {
      // Recupero il primo indirizzo di spedizione della lista
      const isGruppoInvalid = this.indirizziSpedizione?.some(
        (is: IndirizzoSpedizioneVo) => {
          // Verifico se c'è almeno un indirizzo di spedizione con id_gruppo_soggetto
          const isGS = is.id_gruppo_soggetto != undefined;
          // Verifico se l'indirizzo non è valido
          const isInvalid = !isSBNTrue(is.ind_valido_postel);
          // Ritorno il match tra le due informazioni
          return isGS && isInvalid;
        }
      );

      // Verifico se l'indirizzo non è valido
      if (isGruppoInvalid) {
        // Ritorno il check sull'ultimo controllo
        return this._riscaAlert.alertConfigsCheck(this.alertGruppiConfigs);
      }
    }

    // Le condizioni non sono valide
    return false;
  }

  /**
   * Getter di comodo per il recupero della configurazione dei gruppi.
   */
  get isGruppoAbilitato() {
    // Recupero il flag per la gestione gruppi
    return this._ambito.isGruppoAbilitato;
  }

  /**
   * Getter di comodo per il recupero della configurazione dei soggetti.
   */
  get isGestioneAbilitata() {
    // Recupero il flag per la gestione abilitata
    return this._ambito.isGestioneAbilitata;
  }

  /**
   * Getter di comodo che verifica le configurazioni per l'abilitazione dei pulsanti per l'indirizzo di spedizione.
   * @returns boolean che definisce le condizioni di gestione per gli indirizzi di spedizione.
   */
  get disableGestioneIS() {
    // Verifico il flag di abilitazione per "isGestioneAbilitata"
    const disabledIGA = !this.isGestioneAbilitata;
    // Verifico i flag di abilitazione per "accesso elementi app"
    const disabledAEA = this.AEA_DISDisabled;

    // Verifico i flag
    return disabledIGA || disabledAEA;
  }

  /**
   * Getter di comodo che recupera la configurazione passata alla modale.
   * @returns bollean con la configurazione per disabilitare come input la form.
   */
  get isFormDisabilitato() {
    // Recupero la configurazione
    return this.dataModal?.isFormDisabilitato;
  }
}
