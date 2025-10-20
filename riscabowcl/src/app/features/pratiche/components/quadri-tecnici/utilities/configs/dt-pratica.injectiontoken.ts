import { InjectionToken } from '@angular/core';
import { PraticaDTVo } from '../../../../../../core/commons/vo/pratica-vo';
import { AppActions } from '../../../../../../shared/utilities';

/**
 * Classe che definisce la struttura dei parametri per la generazione dinamica dei componenti che gestiranno i dati tecnici, a seconda dell'ambito.
 */
export class DTPraticaConfig {
  parentFormKey: string;
  childFormKey: string;
  disableUserInputs: boolean = false;
  showSubmit?: boolean = false;
  modalita?: AppActions = AppActions.inserimento;
  configs?: PraticaDTVo;

  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}

/**
 * InjectionToken<DTPraticaConfig> che permette di definire i parametri di input dei componenti che gestiranno i dati tecnici.
 */
export const DT_PRATICA_CONFIG = new InjectionToken<DTPraticaConfig>(
  'dati-tecnici-pratica-config'
);
