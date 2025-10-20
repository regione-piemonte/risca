import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

/**
 * Servizio collegato strettamente alla componente delle risca-select.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaSelectService {
  /** Subject che permette l'emissione dell'evento: refreshData. */
  refreshDOM$ = new Subject<any>();

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione impiegata per la gestione del bug grafico che avviene durante il ciclo di vita del componente: risca-select.
   * In alcuni momenti del ciclo di vita dell'applicazione, le select si resettano graficamente al primo valore della lista.
   * Non vengono però emessi eventi, quindi la struttura dati rimane invariata, causando questo bug.
   */
  refreshDOM() {
    // Emetto l'evento di refresh
    this.refreshDOM$.next(true);
  }
}
