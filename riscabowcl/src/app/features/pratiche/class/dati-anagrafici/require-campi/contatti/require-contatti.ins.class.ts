import { Validators } from '@angular/forms';
import { RequireContattiClass } from './require-contatti.class';

/**
 * Classe per la gestione dell'accesso alla sezione: recapiti.
 */
export class ValidatorContattiInsClass extends RequireContattiClass {
  /**
   * Costruttore.
   */
  constructor() {
    // Richiamo del super
    super();
    // Setup dei campi per i campi require
    this.setupAccessi();
  }

  /**
   * Funzione di setup per la variabile require.
   */
  private setupAccessi() {
    // Definisco chiave e valore di accesso
    this.config[this.RC_C.TIPO_INVIO] = { isRequired: true };
    this.config[this.RC_C.PEC] = { isRequired: false, otherValidators: [Validators.email] };
    this.config[this.RC_C.EMAIL] = { isRequired: false, otherValidators: [Validators.email] };
    this.config[this.RC_C.TELEFONO] = { isRequired: false };
    this.config[this.RC_C.CELLULARE] = { isRequired: false };
  }
}
