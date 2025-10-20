import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import {
  DynamicObjAny,
  IRiscaButtonAllConfig,
  RiscaServerErrorInfo,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { ModalitaVerifySaveSD } from '../../enums/dati-contabili/dati-contabili.enums';
import {
  TGSSDMPromptLogicCancel,
  TGSSDMPromptLogicClose,
  TGSSDMPromptLogicConfirm,
  TGSSDMPromptLogicReject,
} from '../../modal/gestisci-salvataggio-sd-modal/utilities/gestisci-salvataggio-sd-modal.types';

/**
 * Interfaccia specifica utilizzata per gestire il risultato della funzione di verify dello stato debitorio.
 */
export interface IVerifySDResult {
  /** Boolean che definisce il risultato della verifica. Se false, verrà controllata la proprietà errors. */
  isValid: boolean;
  /** RiscaServerErrorInfo[] che definisce la lista di errori generati dal server. */
  errors?: RiscaServerErrorInfo[];
}

/**
 * Interfaccia che definisce le informazioni per la gestione "progressiva" delle risposte per i prompt di richiesta di conferma per la gestione dell'inserimento/modifica dello stato debitorio.
 */
export interface IActionPayloadSD {
  /** StatoDebitorioVo con le informazioni dello stato debitorio da salvare. */
  statoDebitorio: StatoDebitorioVo;
  /** DynamicObjAny che contiene tutte le informazioni gestite per i query param di salvataggio dello stato debitorio. */
  queryFlags: DynamicObjAny;
}

/**
 * Interfaccia che definisce le informazioni di gestione della "reject" al prompt di salvataggio dello stato debitorio.
 */
export interface IActionRejectSD {
  /** RiscaNotifyCodes con il codice da visualizzare in caso di annullamento della selezione del prompt. */
  code?: RiscaNotifyCodes;
}

/**
 * Interfaccia di supporto che gestisce una serie di funzionalità da eseguire tramite modale o altri metodi ed il codice di riferimento restituito dalla verify dello stato debitorio.
 */
export interface IActionHandleVerifySD {
  /** String che definisce un id univoco per il prompt, per essere gestito all'interno delle strutture dati. */
  id: string;
  /** RiscaNotifyCodes che mappa il codice gestito dal server per la verify. */
  code: RiscaNotifyCodes;
  /** Funzione che viene invocata quando la modale viene chiuso in modalità close (success). */
  onConfirm: TGSSDMPromptLogicConfirm;
  /** Funzione che viene invocata quando la modale viene chiuso in modalità dismiss (cancel). Se il valore di ritorno è undefined, viene annullato per intero il processo di domande e viene chiusa la modale senza effettuare modifiche. Se invece viene ritornato un valore, questa funzione attiva una "onConfirm", ma con una risposta negativa da parte dell'utente. */
  onCancel: TGSSDMPromptLogicCancel;
  /** Funzione che viene invocata quando la modale viene chiuso in modalità dismiss (cancel). Se il valore di ritorno è undefined, viene annullato per intero il processo di domande e viene chiusa la modale senza effettuare modifiche. */
  onClose: TGSSDMPromptLogicClose;
  /** Funzione che viene invocata quando la modale è stata chiusa (cancel) e gestite le logiche dati tramite "onCancel" o "onClose". La funzione prende in input le informazioni, se esistono, generate per la modale e può produrre un output informativo con dettagli sul motivo della chiusura della modale. */
  onReject?: TGSSDMPromptLogicReject;
  /** String che che definisce il title della modale. */
  title: string;
  /** IRiscaButtonAllConfig che che definisce le configurazioni per il pulsante di: conferma. */
  confirmBtn: IRiscaButtonAllConfig;
  /** IRiscaButtonAllConfig che che definisce le configurazioni per il pulsante di: annulla. */
  cancelBtn: IRiscaButtonAllConfig;
}

/**
 * Interfaccia di supporto che gestisce una serie di funzionalità da eseguire tramite modale o altri metodi ed il codice di riferimento restituito dalla verify dello stato debitorio.
 */
export interface IActionVerifySD {
  /** Funzione che viene invocata quando il modale viene chiuso in modalità close (success). */
  onConfirm: (...args: any) => any;
  /** Funzione che viene invocata quando il modale viene chiuso in modalità dismiss (cancel) e non sono stati forniti parametri. */
  onCancel: () => any;
  /** Funzione che viene invocata quando il modale viene chiuso in modalità dismiss (cancel) e sono stati forniti parametri. */
  onClose: (...args: any) => any;
}

/**
 * Interfaccia di supporto per la gestione della informazioni a seguito della verifica dello stato debitorio.
 */
export interface IGestioneVerifySD {
  /** StatoDebitorioVo che definisce l'oggetto stato debitorio sulla quale è stata fatta la funzione di verifica. */
  statoDebitorio: StatoDebitorioVo;
  /** IVerifySDResult con la configurazione restituita dalla verify stato debitorio. */
  verifica: IVerifySDResult;
  /** ModalitaVerifySD che definisce la modalita per verifica e inserimento dello stato debitorio. */
  modalita: ModalitaVerifySaveSD;
}
