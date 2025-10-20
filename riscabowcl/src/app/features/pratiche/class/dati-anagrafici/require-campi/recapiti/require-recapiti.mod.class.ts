import { RequireRecapitiClass } from './require-recapiti.class';
import { comuneDataFineValiditaFC } from '../../../../../../shared/miscellaneous/forms-validators/soggetto/form-validators.r';

/**
 * Classe per la gestione dell'accesso alla sezione: recapiti.
 */
export class ValidatorRecapitiModClass extends RequireRecapitiClass {
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
    this.config[this.RR_C.TIPO_SEDE] =                { isRequired: true };
    this.config[this.RR_C.PRESSO] =                   { isRequired: false };
    this.config[this.RR_C.STATO] =                    { isRequired: true };
    this.config[this.RR_C.COMUNE] =                   { isRequired: false, otherValidators: [ comuneDataFineValiditaFC() ]  };
    this.config[this.RR_C.PROVINCIA] =                { isRequired: false };
    this.config[this.RR_C.CITTA_ESTERA_RECAPITO] =    { isRequired: false };
    this.config[this.RR_C.LOCALITA] =                 { isRequired: false };
    this.config[this.RR_C.INDIRIZZO] =                { isRequired: true };
    this.config[this.RR_C.NUMERO_CIVICO] =            { isRequired: true };
    this.config[this.RR_C.CAP] =                      { isRequired: true };
  }
}
