import { ValidatorFn } from '@angular/forms';

/**
 * Interfaccia di comodo per la gestione della coppia: campo/require.
 */
export interface IConfigCampiValidatorsDA {
  /** Boolean che definisce se il campo del form deve essere required. */
  isRequired: boolean;
  /** Array di ValidationErrors da concatenare al require. */
  otherValidators?: ValidatorFn[];
}

/**
 * Interfaccia di comodo per la gestione della coppia: campo/require.
 */
export interface IConfigCampiRDA {
  [key: string]: IConfigCampiValidatorsDA;
}

/**
 * Classe comune per la gestione del require campi per: dati anagrafici.
 */
export class ValidatorCampiDAClass {
  /** IConfigCampiRDA che definisce la mappa di campi e required. */
  config: IConfigCampiRDA = {};

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}
