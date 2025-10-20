import { Injectable } from '@angular/core';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { RiscaConfirmModalComponent } from '../../components';
import {
  ConfirmDataModal,
  ICallbackDataModal,
  ICallbackPayloadModal,
  ICommonParamsModal,
} from '../../utilities';
import { ApriModalConfigs } from '../../utilities/classes/risca-modal/risca-modal.classes';
import { RiscaNotifyCodes } from '../../utilities/enums/risca-notify-codes.enums';
import { ICheckCallbacksDataModal } from '../../utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { RiscaMessagesService } from './risca-messages.service';
import { RiscaUtilitiesService } from './risca-utilities/risca-utilities.service';

/**
 * Servizio di utility per la gestione delle modali risca.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaModalService {
  /** Costante per gestione titolo modali risca. */
  private TITLE_RICHIESTA_CONFERMA = '<strong>Richiesta conferma</strong>';
  /** Costante per gestione titolo modali risca. */
  private TITLE_INFORMAZIONE = '<strong>Informazione</strong>';
  /** Costante per gestione modali risca. */
  private BTN_ANNULLA = 'ANNULLA';
  /** Costante per gestione modali risca. */
  private BTN_CONFERMA = 'CONFERMA';
  /** Costante per gestione modali risca. */
  private BTN_CHIUDI = 'CHIUDI';

  /**
   * Costruttore
   */
  constructor(
    private _modal: NgbModal,
    private _riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * ##############################
   * CONFIGURAZIONE MODALI STATICHE
   * ##############################
   */

  /**
   * Funzione che permette di aprire la modale di conferma passando un codice messaggio per il corpo del testo del modale.
   * @param msgCode string che definisce il codice del messaggio da recuperare per farlo visualizzare all'utente.
   * @param callbacks ICallbackDataModal contenete le callback da eseguire alla chiusura della modale.
   */
  apriModalConfermaWithCode(msgCode: string, callbacks?: ICallbackDataModal) {
    // Recupero dal servizio il messaggio della modale
    const bodyModal = this._riscaMessages.getMessage(msgCode);
    // Lancio della funzione per la gestione del modal
    this.apriModalConferma(bodyModal, callbacks);
  }

  /**
   * Funzione che permette di aprire la modale di conferma passando un codice messaggio per il corpo del testo del modale e dei payload da ritornare sulle callback.
   * @param msgCode string che definisce il codice del messaggio da recuperare per farlo visualizzare all'utente.
   * @param callbacks ICallbackDataModal contenete le callback da eseguire alla chiusura della modale.
   * @param payloads ICallbackPayloadModal contenente i payload da ritornare sulle callback.
   */
  apriModalConfermWithCodeAndPayloads(
    msgCode: string,
    callbacks?: ICallbackDataModal,
    payloads?: ICallbackPayloadModal
  ) {
    // Recupero dal servizio il messaggio della modale
    const bodyModal = this._riscaMessages.getMessage(msgCode);
    // Creo un oggetto ICommonParamsModal per la configurazione della modale
    const modalParams: ICommonParamsModal = {
      title: this.TITLE_RICHIESTA_CONFERMA,
      body: bodyModal,
      onConfirm: callbacks?.onConfirm,
      onClose: callbacks?.onClose,
      onCancel: callbacks?.onCancel,
      confirmPayload: payloads?.confirmPayload,
      closePayload: payloads?.closePayload,
      cancelPayload: payloads?.cancelPayload,
    };
    // Lancio della funzione per la gestione del modal
    this.apriModalConfermaWithParams(modalParams);
  }

  /**
   * Funzione che permette di aprire la modale di conferma annullamento.
   * @param callbacks CallbackDataModal contenete le callback da eseguire alla chiusura della modale.
   */
  apriModalConfermaAnnullamento(callbacks?: ICallbackDataModal) {
    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.A001;
    // Richiamo la funzione di apertura modale di conferma
    this.apriModalConfermaWithCode(code, callbacks);
  }

  /**
   * Funzione che permette di aprire la modale di conferma cancellazione.
   * @param callbacks CallbackDataModal contenete le callback da eseguire alla chiusura della modale.
   */
  apriModalConfermaCancellazione(callbacks?: ICallbackDataModal) {
    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.A002;
    // Richiamo la funzione di apertura modale di conferma
    this.apriModalConfermaWithCode(code, callbacks);
  }

  /**
   * Funzione che gestisce la costruzione, richiamo e callback del modal risca.
   * @param body string che contiene il testo del modale.
   * @param callbacks CallbackDataModal configurazione per le chiamate alle callback.
   */
  apriModalConferma(body: string, callbacks?: ICallbackDataModal) {
    // Costruisco l'oggetto di configurazione per la parametrizzazione del modale
    const confirmDataModal = new ConfirmDataModal({
      title: this.TITLE_RICHIESTA_CONFERMA,
      body: body,
      buttonCancel: this.BTN_ANNULLA,
      buttonConfirm: this.BTN_CONFERMA,
    });
    // Definisco l'oggetto di configurazione per la modal di bootstrap
    const options: NgbModalOptions = { windowClass: 'risca-modal-conferma' };

    // Richiamo la funzione di apertura del modale
    this.apriModal(
      new ApriModalConfigs({
        component: RiscaConfirmModalComponent,
        options,
        callbacks,
        params: { dataModal: confirmDataModal },
      })
    );
  }

  /**
   * Funzione che gestisce la costruzione, richiamo e callback del modal risca.
   * @param body string che contiene il testo del modale.
   * @param callbacks CallbackDataModal configurazione per le chiamate alle callback.
   */
  apriModalConfermaWithParams(params: ICommonParamsModal) {
    // Costruisco l'oggetto di configurazione per la parametrizzazione del modale
    const confirmDataModal = new ConfirmDataModal();
    // Lancio la funzione di setup dati per i parametri della modale
    confirmDataModal.setupByICommonParamsModal(params);
    // Definisco l'oggetto di configurazione per la modal di bootstrap
    const options: NgbModalOptions = { windowClass: 'risca-modal-conferma' };

    // Richiamo la funzione di apertura del modale
    this.apriModal(
      new ApriModalConfigs({
        component: RiscaConfirmModalComponent,
        options,
        callbacks: confirmDataModal.getCallbacks(),
        params: { dataModal: confirmDataModal },
      })
    );
  }

  /**
   * Funzione che gestisce la costruzione, richiamo e callback del modal risca informativa.
   * @param body string che contiene il testo del modale.
   * @param callbacks CallbackDataModal configurazione per le chiamate alle callback.
   */
  apriModalInformativa(body: string, callbacks?: ICallbackDataModal) {
    // Costruisco l'oggetto di configurazione per la parametrizzazione del modale
    const confirmDataModal = new ConfirmDataModal({
      title: this.TITLE_INFORMAZIONE,
      body: body,
      buttonConfirm: this.BTN_CHIUDI,
      showCancelBtn: false,
    });
    // Definisco l'oggetto di configurazione per la modal di bootstrap
    const options: NgbModalOptions = { windowClass: 'risca-modal-conferma' };

    // Richiamo la funzione di apertura del modale
    this.apriModal(
      new ApriModalConfigs({
        component: RiscaConfirmModalComponent,
        options,
        callbacks,
        params: { dataModal: confirmDataModal },
      })
    );
  }
  /**
   * #############################
   * GESTIONE MODALI CONFIGURABILI
   * #############################
   */

  /**
   * Funzione che apre un modale basandosi sulla configurazione in input.
   * @param config ApriModalConfigs contenente la configurazione del modale.
   * @property component Istanza del componente che si vuole aprire. Deve essere definito nelle entryComponents del modulo.
   * @property options NgbModalOptions contenente le informazioni di configurazione per le opzioni del modale, se non ci sono informazioni, definire un oggetto vuoto.
   * @property params Oggetto composto da proprietà che devono riflettere i nomi delle  @Input() del componente e i propri valori.
   * @property callbacks CallbackDataModal contenente la logica delle callback del modale.
   */
  apriModal(config: ApriModalConfigs) {
    // Definisco l'oggetto di configurazione per la modal di bootstrap
    const options: NgbModalOptions = config.options;
    // Definisco l'istanza del componente
    const component = config.component;

    // Definizione dell'istanza di apertura del modale
    const modal = this._modal.open(component, options);

    // Itero le proprietà dai parametri
    for (let [input, data] of Object.entries(config.params || {})) {
      // Definisco i parametri da passare alla modale per @Input()
      modal.componentInstance[input] = data;
    }

    // Registro le funzioni di callback alla confirm (fullyfied) e alla close (reject)
    modal.result.then(
      (confirmData: any) => {
        // Controllo se esiste una funzione di callback per la confirm
        const { confirm } = this.checkCallbacksDataModal(config.callbacks);
        // Richiamo la funzione di callback
        if (confirm) {
          config.callbacks.onConfirm(confirmData);
        }
      },
      (closeData: any) => {
        // Controllo se esiste una funzione di callback per la cancel
        const { cancel, close } = this.checkCallbacksDataModal(
          config.callbacks
        );
        // Richiamo la funzione di callback
        if (close && closeData) {
          config.callbacks.onClose(closeData);
        } else if (cancel) {
          config.callbacks.onCancel();
        }
      }
    );
  }

  /**
   * #####################################
   * FUNZIONI DI UTILITY DI CONTROLLO DATI
   * #####################################
   */

  /**
   * Funzione che verifica l'esistenza delle callback dentro l'oggetto CallbackDataModal.
   * @param callbacks CallbackDataModal contenente le configurazioni delle callback di success e/o di error.
   * @returns ICheckCallbacksDataModal che definisce lo stato di esistenza delle callbacks.
   */
  checkCallbacksDataModal(
    callbacks?: ICallbackDataModal
  ): ICheckCallbacksDataModal {
    // Definisco le condizioni
    const confirm =
      callbacks?.onConfirm &&
      this._riscaUtilities.isFunction(callbacks.onConfirm);
    const close =
      callbacks?.onClose && this._riscaUtilities.isFunction(callbacks.onClose);
    const cancel =
      callbacks?.onCancel &&
      this._riscaUtilities.isFunction(callbacks.onCancel);

    // Ritorno l'oggetto di check
    return { confirm, close, cancel };
  }
}
