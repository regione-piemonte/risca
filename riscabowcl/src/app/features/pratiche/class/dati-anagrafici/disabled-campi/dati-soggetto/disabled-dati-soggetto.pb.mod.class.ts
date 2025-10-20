import { DisabledDatiSoggettoClass } from './disabled-dati-soggetto.class';

/**
 * Classe per la gestione dell'accesso alla sezione: dati soggetto.
 */
export class DisabledDatiSoggettoPBModClass extends DisabledDatiSoggettoClass {
  /**
   * Costruttore.
   */
  constructor() {
    // Richiamo del super
    super();
    // Setup dei campi per i campi disabled
    this.setupDisabilitazioni();
  }

  /**
   * Funzione di setup per la variabile disabled.
   */
  private setupDisabilitazioni() {
    // Definisco chiave e valore di disabilitazione
    this.config[this.RDS_C.TIPO_SOGGETTO] =         { disabled: true };
    this.config[this.RDS_C.CODICE_FISCALE] =        { disabled: true };
    this.config[this.RDS_C.NATURA_GIURIDICA] =      { disabled: false };
    this.config[this.RDS_C.NOME] =                  { disabled: true };
    this.config[this.RDS_C.COGNOME] =               { disabled: true };
    this.config[this.RDS_C.RAGIONE_SOCIALE] =       { disabled: false };
    this.config[this.RDS_C.PARTITA_IVA] =           { disabled: false };
    this.config[this.RDS_C.COMUNE_NASCITA] =        { disabled: true };
    this.config[this.RDS_C.DATA_NASCITA] =          { disabled: true };
    this.config[this.RDS_C.STATO_NASCITA] =         { disabled: true };
    this.config[this.RDS_C.PROVINCIA_NASCITA] =     { disabled: true };
    this.config[this.RDS_C.CITTA_ESTERA_NASCITA] =  { disabled: true };
  }
}
