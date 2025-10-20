import { RequireDatiSoggettoClass } from './require-dati-soggetto.class';
import { Validators } from '@angular/forms';
import { partitaIvaValidator } from '../../../../../../shared/miscellaneous/forms-validators/soggetto/form-validators.s';

/**
 * Classe per la gestione dell'accesso alla sezione: dati soggetto.
 */
export class ValidatorDatiSoggettoPBInsClass extends RequireDatiSoggettoClass {
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
    this.config[this.RDS_C.TIPO_SOGGETTO] = { isRequired: true };
    this.config[this.RDS_C.CODICE_FISCALE] = { isRequired: true };
    this.config[this.RDS_C.NATURA_GIURIDICA] = { isRequired: true };
    this.config[this.RDS_C.NOME] = { isRequired: false };
    this.config[this.RDS_C.COGNOME] = { isRequired: false };
    this.config[this.RDS_C.RAGIONE_SOCIALE] = { isRequired: true };
    this.config[this.RDS_C.PARTITA_IVA] = { isRequired: true, otherValidators: [partitaIvaValidator()] };
    this.config[this.RDS_C.COMUNE_NASCITA] = { isRequired: false };
    this.config[this.RDS_C.DATA_NASCITA] = { isRequired: false };
    this.config[this.RDS_C.STATO_NASCITA] = { isRequired: false };
    this.config[this.RDS_C.PROVINCIA_NASCITA] = { isRequired: false };
    this.config[this.RDS_C.CITTA_ESTERA_NASCITA] = { isRequired: false };
  }
}
