import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { RiscaUtilityModalComponent } from '../../../../shared/components/risca/risca-modals/risca-utility-modal/risca-utility-modal.component';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { DynamicObjAny } from '../../../../shared/utilities';
import {
  IActionHandleVerifySD,
  IActionPayloadSD,
  IActionRejectSD,
} from '../../interfaces/dati-contabili/dati-contabili.interfaces';
import { GSSDModalTipiReject } from './utilities/gestisci-salvataggio-sd-modal.enums';
import { TGSSDMPromptLogic } from './utilities/gestisci-salvataggio-sd-modal.types';

/**
 * Modale che gestisce una serie di domande da porre all'utente. Ogni domanda gestirà un input e un output definito e a seconda del flusso deciso dall'utente verranno gestite altrettante funzionalità.
 */
@Component({
  selector: 'gestisci-salvataggio-sd-modal',
  templateUrl: './gestisci-salvataggio-sd-modal.component.html',
  styleUrls: ['./gestisci-salvataggio-sd-modal.component.scss'],
})
export class GestisciSalvataggioSDModalComponent
  extends RiscaUtilityModalComponent
  implements OnInit
{
  /** Input StatoDebitorioVo contenente il dato iniziale per lo stato debitorio. */
  @Input() statoDebitorio: StatoDebitorioVo;
  /** Input IActionHandleVerifySD[] contenente le configurazioni per i prompt da visualizzare all'utente. */
  @Input() prompts: IActionHandleVerifySD[];

  /** IActionHandleVerifySD che definisce le configurazioni attive per la modale. Questo dato è alimentato dalla lista di prompt in input. */
  activePrompt: IActionHandleVerifySD;

  /** DynamicObjAny che gestisce tutte le informazioni relative ai flag in query param che verranno usati per il salvataggio dello stato debitorio. */
  queryFlags: DynamicObjAny = {};

  /**
   * Costruttore.
   */
  constructor(activeModal: NgbActiveModal, riscaAlert: RiscaAlertService) {
    // Richiamo il super
    super(activeModal, riscaAlert);
  }

  /**
   * OnInit.
   */
  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di init del componente che racchiude tutte le funzioni d'inizializzazione delle logiche.
   */
  private initComponente() {
    // Verifico l'input del componente
    this.initCheckInputs();
    // Attivo il primo prompt della lista come attivo
    this.activeFirstPrompt();
  }

  /**
   * Funzione che verifica l'input del componente.
   * Devono essere configurate le informazioni minime altrimenti verrà generato errore.
   */
  private initCheckInputs() {
    // Verifico che esista lo stato debitorio
    if (!this.statoDebitorio) {
      // Lancio errore
      throw new Error(
        '[gestisci-salvataggio-sd-modal.component.ts - stato debitorio undefined'
      );
    }
    // Verifico che esistano i prompt
    if (!this.prompts || this.prompts.length == 0) {
      // Lancio errore
      throw new Error(
        '[gestisci-salvataggio-sd-modal.component.ts - prompts undefined'
      );
    }
  }

  /**
   * #################################
   * FUNZIONI DI GESTIONE DELLA MODALE
   * #################################
   */

  /**
   * Funzione di comodo che raccoglie le informazioni ed esegue la funzione di callback specifica di un prompt.
   * @param logic TGSSDMPromptLogic con la funzione da eseguire per il prompt.
   */
  private eseguiLogichePrompt(
    logic: TGSSDMPromptLogic
  ): IActionPayloadSD | IActionRejectSD | undefined {
    // Verifico l'input
    if (!logic) {
      // Nessuna logica
      return undefined;
    }

    // Definisco l'oggetto di payload da passare alla funzione
    const payload: IActionPayloadSD = this.modalPayload;
    // Lancio la logica della funzione e ritorno il suo risultato al chiamante
    return logic(payload);
  }

  /**
   * Funzione collegata al pulsante X di chiusura della modale.
   * La funzione gestirà le logiche del prompt e a seconda dell'output generato, verrà gestita la chiusura della modale.
   */
  onPromptClose() {
    // Recupero il prompt attivo
    const p = this.activePrompt;
    // Recupero dal prompt la funzione di chiusura
    const logic: TGSSDMPromptLogic = p?.onClose;
    // Lancio la funzione e intercetto il payload di ritorno
    const payloadRes = this.eseguiLogichePrompt(logic) as IActionPayloadSD;

    // Verifico il risultato ottenuto e a seconda della sua composizione gestisco la logica
    if (payloadRes) {
      // Passo al prossimo prompt
      this.onNextPromptByPayload(payloadRes);
      // #
    } else {
      // E' stata richiesta la chiusura della modale senza salvataggio dati
      this.onPromptReject(GSSDModalTipiReject.close);
    }
  }

  /**
   * Funzione collegata al pulsante "cancella" della modale.
   * La funzione gestirà le logiche del prompt e a seconda dell'output generato, verrà gestita la chiusura della modale.
   */
  onPromptCancel() {
    // Recupero il prompt attivo
    const p = this.activePrompt;
    // Recupero dal prompt la funzione di chiusura
    const logic: TGSSDMPromptLogic = p?.onCancel;
    // Lancio la funzione e intercetto il payload di ritorno
    const payloadRes = this.eseguiLogichePrompt(logic) as IActionPayloadSD;

    // Verifico il risultato ottenuto e a seconda della sua composizione gestisco la logica
    if (payloadRes) {
      // Passo al prossimo prompt
      this.onNextPromptByPayload(payloadRes);
      // #
    } else {
      // E' stata richiesta la chiusura della modale senza salvataggio dati
      this.onPromptReject(GSSDModalTipiReject.cancel);
    }
  }

  /**
   * Funzione che definisce le logiche di chiusura della modale con modalità "reject".
   * La funzione di reject può definire dei parametri di ritorno per la notifica ai componenti chiamanti, quando l'utente decide d'interrompere le logiche collegate ai prompt.
   * @param tipoReject GSSDModalTipiReject con la modalità di chiusura della modale.
   */
  onPromptReject(tipoReject: GSSDModalTipiReject) {
    // Recupero il prompt attivo
    const p = this.activePrompt;
    // Recupero dal prompt la funzione di chiusura
    const logic: TGSSDMPromptLogic = p?.onReject;
    // Lancio la funzione e intercetto il payload di ritorno
    const rejectRes = this.eseguiLogichePrompt(logic) as IActionRejectSD;

    // Verifico il metodo di chiusura della modale
    switch (tipoReject) {
      // CANCEL
      case GSSDModalTipiReject.cancel:
        // Richiamo la funzione specifica
        this.modalCancel(rejectRes);
        break;
      // CLOSE
      case GSSDModalTipiReject.close:
        // Richiamo la funzione specifica
        this.modalClose(rejectRes);
        break;
      // DEFAULT
      default:
        // Richiamo la funzione specifica
        this.modalClose(rejectRes);
    }
  }

  /**
   * Funzione collegata al pulsante "conferma" della modale.
   * La funzione gestirà le logiche del prompt e a seconda dell'output generato, verrà gestita la chiusura della modale.
   */
  onPromptConfirm() {
    // Recupero il prompt attivo
    const p = this.activePrompt;
    // Recupero dal prompt la funzione di chiusura
    const logic: TGSSDMPromptLogic = p?.onConfirm;
    // Lancio la funzione e intercetto il payload di ritorno
    const payloadRes = this.eseguiLogichePrompt(logic) as IActionPayloadSD;

    // Passo al prossimo prompt
    this.onNextPromptByPayload(payloadRes);
  }

  /**
   * Funzione di comodo per gestire il passaggio al prossimo prompt una volta che è stato generato un payload dalle chiamate di: close, confirm, cancel.
   * @param payload IActionPayloadSD con le informazioni aggiornate dalle funzioni di callback del prompt.
   */
  private onNextPromptByPayload(payload: IActionPayloadSD) {
    // E' stato prodotto un "proseguo", aggiorno i dati e passo al prossimo prompt
    this.statoDebitorio = payload.statoDebitorio;
    this.queryFlags = payload.queryFlags;
    // Passo al prossimo prompt
    this.nextPrompt();
  }

  /**
   * ###############################
   * FUNZIONI DI GESTIONE DEI PROMPT
   * ###############################
   */

  /**
   * Funzione di supporto che recupera l'indice posizionale del prompt in input all'interno dell'array di prompts.
   * @param prompt IActionHandleVerifySD con il prompt da ricercare.
   * @returns number con l'indice posizionale del prompt nell'array. Se non esiste, ritorna -1.
   */
  promptIndex(prompt: IActionHandleVerifySD): number {
    // Esiste già un prompt attivo, ricerco la sua posizione nell'array
    const currentPrompt = prompt;
    // Ricerco all'interno dell'array dei prompt il suo indice
    return this.prompts.findIndex((p: IActionHandleVerifySD) => {
      // Effettuo un check per id
      return p.id === currentPrompt.id;
    });
  }

  /**
   * Funzione di comodo che imposta come prompt attivo il primo prompt della lista.
   */
  activeFirstPrompt() {
    // Attivo il primo prompt della modale
    this.activePrompt = this.prompts[0];
  }

  /**
   * Funzione di comodo che imposta come prompt attivo quello definito dall'indice in input.
   * Se l'indice è al di fuori del numero di elementi, verrà attivato il primo.
   * @param i number con l'indice di selezione del prompt.
   */
  activePromptByIndex(i: number) {
    // Verifico l'input
    if (i < 0 || i > this.lastPromptIndex) {
      // L'indice è al di fuori del range, attivo il primo
      this.activeFirstPrompt();
      // #
    } else {
      // Attivo il primo prompt della modale
      this.activePrompt = this.prompts[i];
    }
  }

  /**
   * Funzione che gestisce la selezione del prompt da visualizzare.
   * Se non esiste un prompt successivo a quello attivo, verrà gestita la conferma/chiusura con successo della modale e verrà salvato lo stato debitorio.
   */
  nextPrompt() {
    // Verifico se è attivo un prompt
    if (!this.activePrompt) {
      // Attivo il primo prompt della modale
      this.activeFirstPrompt();
      // Blocco il flusso
      return;
    }

    // Ricerco all'interno dell'array dei prompt il suo indice
    const iAP = this.currentPromptIndex;
    const iLP = this.lastPromptIndex;
    // Definisco dei flag di controllo
    const existAP = iAP > -1;
    const apHasNext = iAP + 1 < iLP;
    const apIsLast = iAP === iLP;

    // Verifico se esiste
    if (!existAP) {
      // Non esiste, imposto il primo prompt come attivo
      this.activeFirstPrompt();
      // #
    } else if (apHasNext) {
      // Attivo il prossimo prompt
      this.activePromptByIndex(iAP + 1);
      // #
    } else if (apIsLast) {
      // Questo era l'ultimo prompt, gestisco la chiusura della modale
      this.modalConfirm(this.modalPayload);
      // #
    } else {
      // Caso di default, chiudo la modale
      this.onPromptReject(GSSDModalTipiReject.cancel);
      // #
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che ritorna l'indice posizionale del prompt attualmente attivo all'interno della lista di prompts.
   * @returns number con l'indice posizionale trovato.
   */
  get currentPromptIndex(): number {
    // Richiamo la funzione di ricerca passando il prompt attivo
    return this.promptIndex(this.activePrompt);
  }

  /**
   * Getter di comodo che ritorna l'indice posizionale del prompt attualmente attivo all'interno della lista di prompts.
   * @returns number con l'indice posizionale trovato.
   */
  get lastPromptIndex(): number {
    // Recupero l'ultimo indice per la lista di prompts
    const lastIndex = this.prompts?.length - 1;
    // Ritorno l'ultimo indice posizionale
    return lastIndex;
  }

  /**
   * Getter di comodo che genera un oggetto IActionPayloadSD partendo dai dati presenti nel componente.
   * @returns IActionPayloadSD con le informazioni di ritorno.
   */
  get modalPayload(): IActionPayloadSD {
    // Compongo l'oggetto di ritorno
    const payload: IActionPayloadSD = {
      statoDebitorio: this.statoDebitorio,
      queryFlags: this.queryFlags,
    };
    // Ritorno il payload generato
    return payload;
  }
}
