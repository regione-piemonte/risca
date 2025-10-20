import { Injectable } from '@angular/core';
import { RiscaTablePagination } from '../../../../shared/utilities';

@Injectable({ providedIn: 'root' })
export class StatiDebitoriNAPService {
  /** RiscaTablePagination che definisce l'ultima paginazione salvata per la sezione degli stati debitori nap. */
  private _lastPagination: RiscaTablePagination;

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * ###########################################
   * FUNZIONI DI GESTIONE VARIABILI DEL SERVIZIO
   * ###########################################
   */

  /**
   * Funzione di comodo per il reset dell'oggetto di ultima paginazione salvato.
   */
  resetLastPagination() {
    // Assegno undefined alla variabile
    this._lastPagination = undefined;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter per l'oggetto di ultima paginazione.
   */
  get lastPagination(): RiscaTablePagination {
    // Ritorno l'ultima paginazione
    return this._lastPagination;
  }

  /**
   * Setter per l'oggetto di ultima paginazione.
   */
  set lastPagination(lastPagination: RiscaTablePagination) {
    // Ritorno l'ultima paginazione
    this._lastPagination = lastPagination;
  }
}
