import { InjectionToken } from '@angular/core';
import { PraticaDTVo } from '../../../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../../../core/commons/vo/stato-debitorio-vo';
import { AppActions } from '../../../../../../shared/utilities';

/**
 * Classe che definisce la struttura dei parametri per la generazione dinamica dei componenti che gestiranno i dati tecnici dello stato debitorio, a seconda dell'ambito.
 */
export class DTRiepilogoSDConfig {
  parentFormKey: string;
  childFormKey: string;
  disableUserInputs: boolean = false;
  showSubmit?: boolean = false;
  modalita?: AppActions = AppActions.inserimento;
  praticaDT?: PraticaDTVo;
  statoDebitorio?: StatoDebitorioVo;

  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}

/**
 * InjectionToken<DTRiepilogoSDConfig> che permette di definire i parametri di input dei componenti che gestiranno i dati tecnici.
 */
export const DT_RIEPILOGO_SD_CONFIG = new InjectionToken<DTRiepilogoSDConfig>(
  'dati-tecnici-riepilogo-sd-config'
);
