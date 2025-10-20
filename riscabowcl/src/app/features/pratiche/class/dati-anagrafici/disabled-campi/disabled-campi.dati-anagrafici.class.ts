/**
 * Interfaccia di comodo per la gestione della coppia: campo/disabled.
 */
export interface IConfigCampiDisabledDA {
  /** Boolean che definisce se il campo del form deve essere disabled. */
  disabled: boolean;
}

/**
 * Interfaccia di comodo per la gestione della coppia: campo/disabled.
 */
export interface IConfigCampiDDA {
  [key: string]: IConfigCampiDisabledDA;
}

/**
 * Classe comune per la gestione del disabled campi per: dati anagrafici.
 */
export class DisabledCampiDAClass {
  /** IConfigCampiRDA che definisce la mappa di campi e disabled. */
  config: IConfigCampiDDA = {};

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}
