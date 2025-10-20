import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';

@Injectable({ providedIn: 'root' })
export class RicerchePagamentiService {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** Subject dedicato all'evento per annullare la ricerca dei pagamenti. */
  onAnnullaRicerca$ = new Subject<any>();
  /** Subject dedicato all'evento per avviare la ricerca dei pagamenti. */
  onAvviaRicerca$ = new Subject<any>();

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione di supporto per l'emissione dell'evento: onAnnullaRicerca.
   */
  annullaRicerca() {
    // Emetto l'evento
    this.onAnnullaRicerca$.next(true);
  }

  /**
   * Funzione di supporto per l'emissione dell'evento: onAvviaRicerca.
   */
  avviaRicerca() {
    // Emetto l'evento
    this.onAvviaRicerca$.next(true);
  }
}
