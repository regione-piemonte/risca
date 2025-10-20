import { Injectable } from '@angular/core';
import { RiscaServerError } from '../../utilities';
import { RiscaUtilitiesService } from './risca-utilities/risca-utilities.service';

/**
 * Servizio di utility con funzionalità di gestione per gli errori generati dal server.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaServerErrorManagerService {
  /**
   * Costruttore.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione che verifica codice errore e stato di un server error, rispetto ad un errore di confronto.
   * @param serverError RiscaServerError generato dal server da verificare.
   * @param checkError RiscaServerError da verificare rispetto a quello del server.
   * @returns boolean che definisce se l'errore del server è quello specifico richiesto per il confronto.
   */
  checkServerError(
    serverError: RiscaServerError,
    checkError: RiscaServerError
  ): boolean {
    // Verifico l'input
    if (!serverError && !checkError) {
      // Due oggetti undefined, li considero uguali
      return true;
      // #
    } else if (!serverError || !checkError) {
      // Di sicuro non sono gli stessi errori
      return false;
    }

    // Estraggo gli oggetti di errore
    const sError = serverError.error;
    const cError = checkError.error;
    // Estraggo codice e status per entrambi gli errori
    const seCode = sError?.code;
    const seStatus = sError?.status;
    const ceCode = cError?.code;
    const ceStatus = cError?.status;
    // Verifico le informazioni
    const checkCode = seCode === ceCode;
    const checkStatus = seStatus === ceStatus;

    // Ritorno il check
    return checkCode && checkStatus;
  }
}
