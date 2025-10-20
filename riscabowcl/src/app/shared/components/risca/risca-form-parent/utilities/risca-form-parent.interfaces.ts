import { IRiscaFormTreeParent, RiscaServerError } from '../../../../utilities';

/**
 * Interfaccia che definisce le configurazioni della richiesta di snapshot delle forms figlie al padre.
 */
export interface IRFPResSnapshot {
  /** string che definisce l'id del RiscaFormParent che ha sta gestendo la richiesta di snapshot. */
  parentFormKey: string;
  /** Boolean che definisce l'attivazione delle logiche per la complete dello snapshot. */
  complete?: boolean;
  /** Boolean che definisce l'attivazione delle logiche per la failure dello snapshot. */
  failure?: boolean;
  /** Any che definisce un oggetto da passare alla funzione di complete. */
  completeParams?: any;
  /** Any che definisce un oggetto da passare alla funzione di failure. */
  failureParams?: any;
}

/**
 * Interfaccia di comodo che definisce la struttura della configurazione per la gestione della funzione: setupFormSubmitHandler.
 * Si potranno configurare due funzioni che andranno a sostiture le logiche di default della funzione: setupFormSubmitHandler.
 */
export interface SetupFormSubmitHandlerListener {
  onFormsValid?: (formsValid: IRiscaFormTreeParent) => void;
  onFormsInvalid?: (formsInvalid: IRiscaFormTreeParent) => void;
}

/**
 * Interfaccia che definisce l'oggetto contenente le informazioni per la gestione degli errori all'interno di un componente figlio.
 */
export interface IFormChildServiziError {
  child: string;
  error: RiscaServerError;
}