import { DisabledRicercaAvanzataPraticheClass } from "./disabled-ricerca-avanzata-pratiche.class";

/**
 * Classe per la gestione dell'accesso alla sezione: dati soggetto.
 */
export class DisabledRicercaAvanzataPraticheMRStatoDebitorioClass extends DisabledRicercaAvanzataPraticheClass {
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
    // Definisco chiave e valore di accesso
    this.config[this.FRAP_C.MODALITA_RICERCA] = { disabled: false };
    this.config[this.FRAP_C.TIPO_SOGGETTO] = { disabled: false };
    this.config[this.FRAP_C.RAGIONE_SOCIALE_COGNOME] = { disabled: false };
    this.config[this.FRAP_C.CODICE_FISCALE] = { disabled: false };
    this.config[this.FRAP_C.PARTITA_IVA] = { disabled: false };
    this.config[this.FRAP_C.STATO_RESIDENZA] = { disabled: false };
    this.config[this.FRAP_C.PROVINCIA_RESIDENZA] = { disabled: false };
    this.config[this.FRAP_C.INDIRIZZO] = { disabled: false };
    this.config[this.FRAP_C.COMUNE_RESIDENZA] = { disabled: false };
    this.config[this.FRAP_C.PROVINCIA_COMPETENZA] = { disabled: false };
    this.config[this.FRAP_C.CORPO_IDRICO] = { disabled: false };
    this.config[this.FRAP_C.COMUNE_COMPETENZA] = { disabled: false };
    this.config[this.FRAP_C.NOME_IMPIANTO_IDROELETTRICO] = { disabled: false };
    this.config[this.FRAP_C.TIPO_TITOLO] = { disabled: false };
    this.config[this.FRAP_C.TIPO_PROVVEDIMENTO] = { disabled: false };
    this.config[this.FRAP_C.NUMERO_TITOLO] = { disabled: false };
    this.config[this.FRAP_C.DATA_TITOLO_DA] = { disabled: false };
    this.config[this.FRAP_C.DATA_TITOLO_A] = { disabled: false };
    this.config[this.FRAP_C.TIPOLOGIA_PRATICA] = { disabled: true };
    this.config[this.FRAP_C.STATO_PRATICA] = { disabled: true };
    this.config[this.FRAP_C.SCADENZA_CONCESSIONE_DA] = { disabled: false };
    this.config[this.FRAP_C.SCADENZA_CONCESSIONE_A] = { disabled: false };
    this.config[this.FRAP_C.DATA_RINUNCIA_REVOCA_DA] = { disabled: true };
    this.config[this.FRAP_C.DATA_RINUNCIA_REVOCA_A] = { disabled: true };
    this.config[this.FRAP_C.ANNO_CANONE] = { disabled: false };
    this.config[this.FRAP_C.CANONE] = { disabled: false };
    this.config[this.FRAP_C.RESTITUITO_AL_MITTENTE] = { disabled: false };
    this.config[this.FRAP_C.TIPO_ISTANZA] = { disabled: false };
    this.config[this.FRAP_C.DATA_ISTANZA_DA] = { disabled: false };
    this.config[this.FRAP_C.DATA_ISTANZA_A] = { disabled: false };
    // this.config[this.FRAP_C.USO_DI_LEGGE] = { disabled: false };
    // this.config[this.FRAP_C.USO_SPECIFICO] = { disabled: false };
    // this.config[this.FRAP_C.UNITA_DI_MISURA] = { disabled: false };
    // this.config[this.FRAP_C.QUANTITA_DA] = { disabled: false };
    // this.config[this.FRAP_C.QUANTITA_A] = { disabled: false };
    // this.config[this.FRAP_C.DATA_SCADENZA_EMAS_O_ISO_DA] = { disabled: true };
    // this.config[this.FRAP_C.DATA_SCADENZA_EMAS_O_ISO_A] = { disabled: true };
  }
}
