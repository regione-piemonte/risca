/**
 * Interfaccia di comodo per la gestione della coppia: campo/disabled.
 */
export interface IConfigCampiDisabledRAP {
  /** Boolean che definisce se il campo del form deve essere disabled. */
  disabled: boolean;
}

/**
 * Interfaccia di comodo per la gestione della coppia: campo/disabled.
 */
export interface IConfigCampiDRAP {
  [key: string]: IConfigCampiDisabledRAP;
}

/**
 * Classe comune per la gestione del disabled campi per: dati anagrafici.
 */
export class DisabledCampiRAPClass {
  /** IConfigCampiRDA che definisce la mappa di campi e disabled. */
  config: IConfigCampiDRAP = {};

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}
