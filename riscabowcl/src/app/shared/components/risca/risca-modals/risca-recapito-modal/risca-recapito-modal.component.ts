import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { IndirizzoSpedizioneVo } from 'src/app/core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../../core/commons/vo/soggetto-vo';
import { LoggerService } from '../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../../core/services/user.service';
import { TipoSoggettoVo } from '../../../../../features/ambito/models';
import { RecapitiService } from '../../../../../features/pratiche/service/dati-anagrafici/recapiti.service';
import { SoggettoDatiAnagraficiService } from '../../../../../features/pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { SoggettoService } from '../../../../../features/soggetti/services/soggetto/soggetto.service';
import { RiscaRecapitoModalConsts } from '../../../../consts/risca/risca-recapito-modal';
import { RiscaAlertService } from '../../../../services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from '../../../../services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../services/risca/risca-messages.service';
import {
  AppActions,
  IResultVerifyRStDDettaglio,
  IRiscaButtonAllConfig,
  IRiscaFormTreeParent,
  RiscaAlertConfigs,
  RiscaButtonTypes,
  RiscaContatto,
  RiscaInfoLevels,
  RiscaRecapito,
  RiscaServerError,
} from '../../../../utilities';
import { RiscaContattiComponent } from '../../risca-contatti/risca-contatti.component';
import { RiscaFormParentComponent } from '../../risca-form-parent/risca-form-parent.component';
import { RiscaRecapitoComponent } from '../../risca-recapito/risca-recapito.component';

/**
 * Modale impiegata per la gestione dei dati di un recapito tramite modal.
 */
@Component({
  selector: 'risca-recapito-modal',
  templateUrl: './risca-recapito-modal.component.html',
  styleUrls: ['./risca-recapito-modal.component.scss'],
})
export class RiscaRecapitoModalComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy
{
  /** Oggetto con le costanti per la sezione dei dati anagrafici. */
  RRM_C = RiscaRecapitoModalConsts;

  /** TipoSoggetto contenente i dati del tipo soggetto per cui fa parte questo dato. */
  @Input() tipoSoggetto: TipoSoggettoVo;
  /** RecapitoVo contenente i dati del recapito da modificare. */
  @Input() recapito: RecapitoVo;
  /** number che definisce l'id della pratica a cui è collegato questo recapito. */
  @Input() idRiscossione: number;
  /** AppActions che definisce di che tipo di azioni verranno fatti sui dati. */
  @Input() modalita: AppActions;

  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaRecapitoModal') riscaRecapitoModal: RiscaRecapitoComponent;
  /** ViewChild connesso al DOM per utilizzare le funzioni del componente. */
  @ViewChild('riscaContattiModal') riscaContattiModal: RiscaContattiComponent;

  /** SoggettoVo utilizzato come supporto per l'utilizzo dei componenti per la modifica del recapito. */
  datiAnagrafici: SoggettoVo;
  /** Number che definisce l'id del recapito per la modifica. */
  idRecapito: number | string;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertConfirmConfigs: RiscaAlertConfigs;

  /**
   * Costruttore.
   */
  constructor(
    public _activeModal: NgbActiveModal,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _recapiti: RecapitiService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    private _soggetto: SoggettoService,
    private _soggettoDA: SoggettoDatiAnagraficiService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages
    );

    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit() {
    // Verifico gli input
    if (!this.modalita) {
      // Lancio un errore
      throw new Error('RiscaRecapitoModal modalita undefined');
    }

    // Lancio l'init del componente
    this.initComponent();
  }

  ngOnDestroy() {
    // Richiamo il destroy per il super
    super.ngOnDestroy();
  }

  /**
   * ##############################
   * INIZIALIZZATORI DEL COMPONENTE
   * ##############################
   */

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Effettuo il setup del servizio
    this.setupFormsSubmitHandlerService();
  }

  /**
   * Funzione che gestisce il setup del servizio: RiscaFormSubmitHandlerService; per il gruppo di forms principale.
   */
  private setupFormsSubmitHandlerService() {
    // Definisco la chiave per il parent
    const parent = this.RRM_C.FORM_KEY_PARENT_RECAPITO_MODAL;
    // Definisco le chiavi per i figli
    const children = [
      this.RRM_C.FORM_KEY_CHILD_RECAPITO_MODAL,
      this.RRM_C.FORM_KEY_CHILD_CONTATTI_MODAL,
    ];

    // Richiamo il super
    this.setFormsSubmitHandler(parent, children);
  }

  /**
   * Funzione di init del componente.
   */
  private initComponent() {
    // Creo un oggetto SoggettoVo dall'input
    this.gestisciDatiRecapito();
    // Init per la modale
    this.initModal();
  }

  /**
   * Funzione di supporto che effettua tutte le operazioni per poter impiegare i dati del recapito.
   */
  private gestisciDatiRecapito() {
    // Creo un oggetto dati anagrafici vo da passare al componente
    if (this.recapito) {
      // Aggiorno la variabile locale
      this.datiAnagrafici = {
        id_ambito: this._user.idAmbito,
        tipo_soggetto: this.tipoSoggetto,
        recapiti: [this.recapito],
      };
      // Recupero dal recapito l'id in caso di modifica
      this.idRecapito = this.recapito?.id_recapito || this.recapito?.__id;
    }
  }

  /**
   * Funzione di init per il modale.
   */
  private initModal() {
    // Gestione delle logiche
  }

  /**
   * #########################
   * GESTIONE FORMS PRINCIPALI
   * #########################
   */

  /**
   * Funzione di submit per le form di recapito e contatti.
   */
  salvaModificheRecapito() {
    // Lancio il submit
    this.riscaRecapitoModal.onFormSubmit();
    this.riscaContattiModal.onFormSubmit();
  }

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsValid.
   * @param formsValid IRiscaFormTreeParent con i dati validi.
   * @override
   */
  formsValid(formsValid: IRiscaFormTreeParent) {
    // Variabili di comodo
    const FKC_RM = this.RRM_C.FORM_KEY_CHILD_RECAPITO_MODAL;
    const FKC_CM = this.RRM_C.FORM_KEY_CHILD_CONTATTI_MODAL;
    let recapitoModal: RecapitoVo;

    // Recupero la mappa dati dei componenti
    const mapData = this.getRiscaFormTreedData(formsValid);
    // Recupero i dati tramite chiavi
    const recapito: RiscaRecapito = mapData.get(FKC_RM);
    const contatti: RiscaContatto = mapData.get(FKC_CM);

    // Richiamo la funzione di parsing per la gestione degli oggetti
    recapitoModal = this._recapiti.convertRiscaRecapitoToRecapitoVo(
      recapito,
      contatti
    );
    // Setto, se esiste, il tipo recapito rispetto alla configurazione in input
    recapitoModal.tipo_recapito = this.tipoRecapitoDB;

    // Lancio le logiche di gestione per il salvataggio/modifica del recapito
    this.salvaRecapito(recapitoModal);
  }

  /**
   * Gestisco il reset del recapito.
   */
  resetRecapitoBtn() {
    // Resetto gli alert
    this.resetAlertConfigs();

    // Gestione della modifica
    if (this.modifica) {
      // Reset per inserimento
      this.resetFormRecapitoUpdate();
      // #
    }
  }

  /**
   * ###################################
   * FUNZIONI DI GESTIONE CONFERMA ALERT
   * ###################################
   */

  /**
   * Funzione agganciata al componente dell'alert, che intercetterà l'evento di: onConfirm.
   * @param recapito RecapitoVo contenente le informazioni del recapito per la quale è stata gestita l'azione di: 'confirm'.
   */
  onConfirmModifica(recapito: RecapitoVo) {
    // La richiesta è stata confermata, chiudo la modale
    this.modalConfirm(recapito);
  }

  /**
   * Funzione agganciata al componente dell'alert, che intercetterà l'evento di: onCancel.
   */
  onCancelModifica() {
    // Recupero l'oggetto dell'alert di conferma
    const ac = this.alertConfirmConfigs;
    // Nascondo l'alert
    this.resetAlertConfigs(ac);
    // Vado a resettare le informazioni al loro stato originale
    this.resetRecapitoBtn();
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Reset del form per la gestione in aggiornamento.
   */
  private resetFormRecapitoUpdate() {
    // Richiamo il reset delle form
    this.riscaRecapitoModal.onFormRestore();
    this.riscaContattiModal.onFormRestore();
  }

  /**
   * Funzione di supporto che esegue le verifiche sul recapito in caso di modifica.
   * @param recapito RecapitoVo da salvare.
   */
  private salvaRecapito(recapito: RecapitoVo) {
    // Verifico se l'oggetto è un oggetto salvato su DB o se è solo locale
    if (this.isRecapitoOnDB && this.modifica) {
      // Verifico se il recapito è stato o non è stato modificato
      if (this.isRecapitoModified(recapito)) {
        // Lancio la funzione di gestione per la verifica del recapito
        this.verificaPStDRecapito(recapito);
        // #
      } else {
        // Chiudo semplicemente la modale
        this.modalDismiss();
        // #
      }
    } else {
      // In riporto le informazioni al componente chiamante
      this.modalConfirm(recapito);
    }
  }

  /**
   * Funzione di supporto che verifica se il recapito generato dalla form è uguale o differente rispetto a quello passato come configurazione iniziale.
   * @param recapito RecapitoVo da verificare.
   * @returns boolean che definisce se il recapito del form, in input, ha gli stessi valori rispetto a quello che risulta salvato su DB.
   */
  private isRecapitoModified(recapito: RecapitoVo): boolean {
    // Recupero i dati del recapito, sia per il FE sia per il DB
    const rDB = this.recapito;
    const rFE = recapito;

    // Verifico se il recapito da FE risulta diverso dal recapito da DB
    const sameRecapiti = this._recapiti.compareRecapiti(rDB, rFE);

    // Ritorno il check sui recapiti
    return !sameRecapiti;
  }

  /**
   * Funzione che gestisce le logiche per la verifica di un recapito, salvato su DB.
   * La verifica consiste nel controllo delle pratiche e degli stati debitori ad esso collegate.
   * @param recapito RecapitoVo da salvare.
   */
  private verificaPStDRecapito(recapito: RecapitoVo) {
    // Per la verifica del recapito, recupero l'oggetto salvato su DB (passato alla modale come parametro), per la conferma invece passo l'oggetto del form della modale
    const recapitoDB = this.recapito;
    const recapitoMD = recapito;

    // Lancio la verifica del recapito
    this.verificaModificaRecapitoPerTipo(recapitoDB).subscribe({
      next: (verifica: IResultVerifyRStDDettaglio) => {
        // Verifico se sono state trovate pratiche/st debitori
        if (verifica.isObjCollegato) {
          // Richiamo la funzione di gestione del recapito della modale
          this.onRecapitoCollegato(recapitoMD, verifica);
          // #
        } else {
          // In riporto le informazioni al componente chiamante
          this.modalConfirm(recapito);
        }
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione che, sulla base del tipo recapito, definisce le logiche di verifica per il recapito.
   * @param recapito RecapitoVo con le informazioni del recapito per la verifica.
   */
  verificaModificaRecapitoPerTipo(
    recapito: RecapitoVo
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabili di comodo
    const r = recapito;
    const idR = this.idRiscossione;

    // Richiamo il servizio di comodo
    return this._soggetto.controllaRStDTipoRecapitoModifica(r, idR);
  }

  /**
   * Funzione di supporto che gestisce le logiche a seguito della verifica, con riscontro dati, per il recapito.
   * La gestione verrà imbastita sulla base che la verifica ha trovato il recapito collegato a pratiche o stati debitori.
   * @param recapito RecapitoVo contenente le informazioni dell'oggetto da ritornare al componente chiamante.
   * @param verifica IResultVerifyRStDDettaglio contenente il dettaglio delle informazioni della verifica.
   */
  private onRecapitoCollegato(
    recapito: RecapitoVo,
    verifica: IResultVerifyRStDDettaglio
  ) {
    // Richiamo la funzione che gestirà la messaggistica utente
    let msg = this._soggetto.messaggioControlRStDRAModifica(verifica);
    // Gestisco l'alert, passando le configurazioni
    this.alertConfirmConfigs = this.setAlertConfigConfigs(recapito, msg);
    // #### NOTA: a questo punto la gestione passa al componente con i pulsanti di scelta ####
  }

  /**
   * Funzione di supporto che genera le configurazioni per l'alert di conferma modifica recapito.
   * @param recapito RecapitoVo contenente le informazioni dell'oggetto per la quale si chiede la conferma.
   * @param msg string con il messaggio sa visualizzare.
   * @returns RiscaAlertConfigs con le configurazioni del messaggio.
   */
  private setAlertConfigConfigs(
    recapito: RecapitoVo,
    msg: string = ''
  ): RiscaAlertConfigs {
    // Ritorno la configurazione per l'alert
    return new RiscaAlertConfigs({
      messages: [msg],
      type: RiscaInfoLevels.warning,
      buttonConfirm: this.buttonConfirm,
      buttonCancel: this.buttonCancel,
      payloadConfirm: recapito,
      allowAlertClose: true,
    });
  }

  /**
   * ###################################
   * GESTIONE DELLA CHIUSURA DELLA MODAL
   * ###################################
   */

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   * @param resource any che definisce le informazioni da ritornare per la chiusura del modal.
   */
  modalDismiss(resource?: any) {
    // Dismiss della modale
    this._activeModal.dismiss(resource);
  }

  /**
   * Funzione che effettua la close (e quindi la resolved) della modale.
   * @param resource RecapitoVo che definisce i dati gestiti dalla modal.
   */
  modalConfirm(resource: RecapitoVo) {
    // Close della modale
    this._activeModal.close(resource);
  }

  /**
   * Funzione lanciata alla chiusura della modale degli indirizzi spedizione della risca-contatti.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo ricevuto dalla risca-contatti.
   */
  onCloseIndirizziSpedizioneModal(indirizzoSpedizione: IndirizzoSpedizioneVo) {
    // Gestione delle logiche
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di comodo che recupera il tipo recapito dall'oggetti in input come configurazione iniziale.
   */
  get tipoRecapitoDB() {
    // Se esiste, recupero il tipo recapito
    return this.recapito?.tipo_recapito;
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
   * Getter di comodo che verifica se l'oggetto recapito è salvato su DB (esiste l'id_recapito).
   */
  get isRecapitoOnDB() {
    // Verifico e ritorno l'id_recapito
    return this.recapito?.id_recapito != undefined;
  }

  /**
   * Getter di comodo per il titolo della modale.
   */
  get modalTitle() {
    // A seconda della modalità, definisco il title
    if (this.modalita === AppActions.inserimento) {
      // Definisco l'etichetta
      return 'Inserisci recapito';
      // #
    } else if (this.modalita === AppActions.modifica) {
      // Definisco l'etichetta
      return 'Modifica recapito';
      // #
    } else {
      // Qualche errore di gestione
      return 'Recapito';
    }
  }

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfirmConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertConfirmConfigs);
  }

  /**
   * Getter di comodo per la configurazione del pulsante di conferma dell'alert a seguito delle verifiche sul recapito.
   */
  get buttonConfirm(): IRiscaButtonAllConfig {
    return {
      cssConfig: {
        typeButton: RiscaButtonTypes.primary,
      },
      dataConfig: {
        label: this.C_C.BTN_ALERT_LABEL_SI,
      },
    };
  }

  /**
   * Getter di comodo per la configurazione del pulsante di annulla dell'alert a seguito delle verifiche sul recapito.
   */
  get buttonCancel(): IRiscaButtonAllConfig {
    return {
      cssConfig: {
        typeButton: RiscaButtonTypes.default,
      },
      dataConfig: {
        label: this.C_C.BTN_ALERT_LABEL_NO,
      },
    };
  }
}
