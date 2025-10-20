import { Pipe, PipeTransform } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { RiscaUtilitiesService } from '../../services/risca/risca-utilities/risca-utilities.service';
import { MappaErroriFormECodici } from '../../utilities';

/**
 * Pipe dedicata alla gestione degli errori sulle form.
 * Fornito un FormGroup o un FormControl e una mappatura degli errori, la pipe restituirà un messaggio d'errore.
 * Il messaggio d'errore avrà la priorità definita come posizionale all'interno dell'array della mappatura.
 */
@Pipe({ name: 'riscaFormError' })
export class RiscaFormErrorPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   * @param _riscaUtilities RiscaUtilityService
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione di gestione, verifica e recupero degli errori per un formControl.
   * @param form FormGroup | FormControl per la verifica e recupero degli errori.
   * @param mappatura Array di MappaErroriFormECodici contenente la mappatura degli errori.
   * @param forceRefresh Parametro any usato per forzare il refresh della pipe. E' un workaround per forzare l'aggiornamento della pipe.
   * @returns string che contiene il primo messaggio d'errore da far visualizzare all'utente.
   */
  transform(
    form: FormGroup | FormControl,
    mappatura: MappaErroriFormECodici[],
    forceRefresh?: any
  ): string | undefined {
    // Verifico che gli input siano definiti
    if (!form || !mappatura || mappatura.length === 0) {
      return;
    }

    // Recupero tutti i messaggi d'errore per il form control
    const messaggiErrore = this._riscaUtilities.getErrorMessagesFromForm(
      form,
      mappatura
    );

    // Verifico se non ci sono errori
    if (!messaggiErrore && messaggiErrore.length === 0) {
      return;
    }

    // Ci sono messaggi d'errore, recupero il primo e lo ritorno
    return messaggiErrore[0];
  }
}
