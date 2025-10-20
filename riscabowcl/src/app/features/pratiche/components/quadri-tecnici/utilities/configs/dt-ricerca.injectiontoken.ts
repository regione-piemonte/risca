import { InjectionToken } from '@angular/core';

/**
 * Classe che definisce la struttura dei parametri per la generazione dinamica dei componenti che gestiranno la ricerca dei dati tecnici, a seconda dell'ambito.
 */
export class DTRicercaConfig {
  parentFormKey: string;
  childFormKey: string;
  showSubmit?: boolean = false;
  configs?: string;

  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}

/**
 * InjectionToken<DTRicercaConfig> che permette di definire i parametri di input dei componenti che gestiranno i dati tecnici.
 */
export const DT_RICERCA_CONFIG = new InjectionToken<DTRicercaConfig>(
  'dati-tecnici-ricerca-config'
);
