import { Injectable } from '@angular/core';
import {
  lClear,
  lGetItem,
  lKeyAt,
  lRemoveItem,
  lSetItem,
  sClear,
  sGetItem,
  sKeyAt,
  sRemoveItem,
  sSetItem,
} from './risca-storage.functions';

/**
 * Servizio di utility che permette di gestire i dati tramite local e session storage.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaStorageService {
  /**
   * Costruttore
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * ###############
   * SESSION STORAGE
   * ###############
   */

  /**
   * sessionStorage.
   * Funzione che ritorna una chiave, dato un indice in input.
   * @param i number che definisce l'indice per il recupero della chiave.
   * @returns string con il nome della chiave per l'indice richiesto.
   */
  sKeyAt(i: number): string {
    // Richiamo al funzione di utility
    return sKeyAt(i);
  }

  /**
   * sessionStorage.
   * Funzione che ritorna un valore, data una chiave in input.
   * @param key string che definisce il nome della chiave.
   * @returns any contenente il valore associato alla chiave.
   */
  sGetItem(key: string): any {
    // Richiamo al funzione di utility
    return sGetItem(key);
  }

  /**
   * sessionStorage.
   * Funzione che ritorna un valore, data una chiave in input.
   * @param key string che definisce il nome della chiave.
   * @param value any che definisce l'informazione da salvare nel storage.
   * @returns any contenente il valore associato alla chiave.
   */
  sSetItem(key: string, value: any) {
    // Richiamo al funzione di utility
    sSetItem(key, value);
  }

  /**
   * sessionStorage.
   * Funzione che rimuove un valore, data una chiave in input.
   * @param key string che definisce il nome della chiave.
   * @returns any contenente il valore associato alla chiave.
   */
  sRemoveItem(key: string) {
    // Richiamo al funzione di utility
    sRemoveItem(key);
  }

  /**
   * sessionStorage.
   * Funzione che rimuove tutti i valori all'interno dello storage.
   */
  sClear(): any {
    // Richiamo al funzione di utility
    sClear();
  }

  /**
   * ###############
   * SESSION STORAGE
   * ###############
   */

  /**
   * localStorage.
   * Funzione che ritorna una chiave, dato un indice in input.
   * @param i number che definisce l'indice per il recupero della chiave.
   * @returns string con il nome della chiave per l'indice richiesto.
   */
  lKeyAt(i: number): string {
    // Richiamo al funzione di utility
    return lKeyAt(i);
  }

  /**
   * localStorage.
   * Funzione che ritorna un valore, data una chiave in input.
   * @param key string che definisce il nome della chiave.
   * @returns any contenente il valore associato alla chiave.
   */
  lGetItem(key: string): any {
    // Richiamo al funzione di utility
    return lGetItem(key);
  }

  /**
   * localStorage.
   * Funzione che ritorna un valore, data una chiave in input.
   * @param key string che definisce il nome della chiave.
   * @param value any che definisce l'informazione da salvare nel storage.
   * @returns any contenente il valore associato alla chiave.
   */
  lSetItem(key: string, value: any) {
    // Richiamo al funzione di utility
    lSetItem(key, value);
  }

  /**
   * localStorage.
   * Funzione che rimuove un valore, data una chiave in input.
   * @param key string che definisce il nome della chiave.
   * @returns any contenente il valore associato alla chiave.
   */
  lRemoveItem(key: string) {
    // Richiamo al funzione di utility
    lRemoveItem(key);
  }

  /**
   * localStorage.
   * Funzione che rimuove tutti i valori all'interno dello storage.
   */
  lClear(): any {
    // Richiamo al funzione di utility
    lClear();
  }
}
