import { IActionPayloadSD, IActionRejectSD } from '../../../interfaces/dati-contabili/dati-contabili.interfaces';

/**
 * Type specifico che definisce la struttura della funzione per gestire le domande dei prompt, per: CONFIRM.
 */
export type TGSSDMPromptLogicConfirm = (
  payload: IActionPayloadSD
) => IActionPayloadSD;

/**
 * Type specifico che definisce la struttura della funzione per gestire le domande dei prompt, per: CLOSE.
 */
export type TGSSDMPromptLogicClose = (
  payload: IActionPayloadSD
) => IActionPayloadSD | undefined;

/**
 * Type specifico che definisce la struttura della funzione per gestire le domande dei prompt, per: CANCEL.
 */
export type TGSSDMPromptLogicCancel = (
  payload: IActionPayloadSD
) => IActionPayloadSD | undefined;

/**
 * Type specifico che definisce la struttura della funzione per gestire le domande dei prompt, per: REJECT.
 * NOTA BENE: a differenza di Close e Cancel, reject viene sempre ed eseguita solo sulla modalCancel. Questo perché, se le funzioni ritornano qualcosa, vengono viste come delle "confirm" con risposta negativa.
 * Questa reject invece viene gestita come una vera e propria cancellazione della richiesta.
 */
export type TGSSDMPromptLogicReject = (
  payload?: IActionPayloadSD
) => IActionRejectSD | undefined;

/**
 * Type specifico che definisce la struttura delle funzioni per gestire le domande dei prompt.
 */
export type TGSSDMPromptLogic =
  | TGSSDMPromptLogicConfirm
  | TGSSDMPromptLogicClose
  | TGSSDMPromptLogicCancel
  | TGSSDMPromptLogicReject;
