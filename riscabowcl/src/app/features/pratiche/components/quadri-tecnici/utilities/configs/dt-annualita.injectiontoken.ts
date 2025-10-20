import { InjectionToken } from '@angular/core';
import { AnnualitaSDVo } from '../../../../../../core/commons/vo/annualita-sd-vo';
import { PraticaEDatiTecnici } from '../../../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../../../core/commons/vo/stato-debitorio-vo';
import { AppActions } from '../../../../../../shared/utilities';

/**
 * Classe che definisce la struttura dei parametri per la generazione dinamica dei componenti che gestiranno i dati tecnici, a seconda dell'ambito.
 */
export class DTAnnualitaConfig {
  parentFormKey: string;
  childFormKey: string;
  modalita?: AppActions = AppActions.inserimento;
  praticaEDatiTecnici?: PraticaEDatiTecnici;
  dataInserimentoPratica?: string;
  statoDebitorio: StatoDebitorioVo;
  annualitaDT?: AnnualitaSDVo;
  idComponenteDt?: number;
  disableUserInputs: boolean = false;
  rateoPrimaAnnualita: boolean = false;

  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}

/**
 * InjectionToken<DTAnnualitaConfig> che permette di definire i parametri di input dei componenti che gestiranno i dati tecnici.
 */
export const DT_ANNUALITA_CONFIG = new InjectionToken<DTAnnualitaConfig>(
  'dati-tecnici-annualita-config'
);
