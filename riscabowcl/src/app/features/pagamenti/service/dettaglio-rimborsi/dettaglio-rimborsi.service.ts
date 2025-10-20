import { Injectable } from '@angular/core';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { clone } from 'lodash';

@Injectable({ providedIn: 'root' })
export class DettaglioRimborsiService {
  /** StatoDebitorioVo contenente le informazioni da far utilizzare al componente dettaglio-rimborsi.component.ts per caricare le informazioni della pagina. */
  private _statoDebitorio: StatoDebitorioVo;

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * ###############################
   * FUNZIONI PER LO STATO DEBITORIO
   * ###############################
   */

  /**
   * Funzione di get che ritorna le informazioni per lo stato debitorio.
   * @returns StatoDebitorioVo presente nel servizio.
   */
  getStatoDebitorio(): StatoDebitorioVo {
    // Resetto le informazioni
    return clone(this._statoDebitorio);
  }

  /**
   * Funzione di set che assegna le informazioni per lo stato debitorio.
   * @param statoDebitorio StatoDebitorioVo d'assegnare al componente.
   */
  setStatoDebitorio(statoDebitorio: StatoDebitorioVo) {
    // Resetto le informazioni
    this._statoDebitorio = clone(statoDebitorio);
  }

  /**
   * Funzione di comodo che rimuove le informazioni per lo stato debitorio.
   */
  resetStatoDebitorio() {
    // Resetto le informazioni
    this._statoDebitorio = undefined;
  }
}
