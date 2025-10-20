import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { LoggerService } from '../../../../../../../core/services/logger.service';
import { RiscaServerError } from '../../../../../../../shared/utilities';
import { DatiTecniciAmbienteConsts } from '../../../../../consts/quadri-tecnici/dati-tecnici-ambiente/dati-tecnici-ambiente.consts';
import { IDatiCalcoloCanone } from '../../../../../interfaces/quadri-tecnici/quadri-tecnici.interfaces';

@Injectable({
  providedIn: 'root',
})
export class QuadriTecniciSDService {
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_C = new DatiTecniciAmbienteConsts();

  /** EventEmitter che emette l'evento per il submit della form. */
  onFormSubmit = new Subject<boolean>();
  /** EventEmitter che emette l'evento per il reset della form. */
  onFormReset = new Subject<boolean>();

  /** EventEmitter permette di fare aggiornare al componente del calcolo canone le informazioni di calcolo. */
  onDatiCalcoloCanone = new BehaviorSubject<IDatiCalcoloCanone>(undefined);
  /** EventEmitter permette di resettare al componente del calcolo canone  le informazioni di gestione. */
  onResetCalcoloCanone = new Subject<boolean>();
  /** EventEmitter permette di resettare il campo del canone. */
  onResetCanoneCalcolato = new Subject<boolean>();

  /** EventEmitter che informa che il calcolo del canone è fallito. */
  onCalcoloCanoneError = new Subject<RiscaServerError>();
  /** EventEmitter che informa che il calcolo del canone è fallito per la mancanza della regola di calcolo. */
  onRegolaMancanteCC = new Subject<RiscaServerError>();

  /** EventEmitter che informa che si è verificato un errore nella gestione delle chiamate ai servizi. */
  onServiziError$ = new Subject<RiscaServerError>();

  /**
   * Costruttore
   */
  constructor(private _logger: LoggerService) {}

  /**
   * Funzione che emette l'evento di change dei dati del calcolo canone.
   * @param idRiscossione string o number che identifica una riscossione.
   * @deprecated dataRiscossione string che identifica la data di riscossione. La data deve avere formato: AAAA-MM-GG.
   */
  aggiornaDatiCanone(
    idRiscossione: string | number /*, dataRiscossione: string */
  ) {
    // Emetto l'evento
    this.onDatiCalcoloCanone.next({ idRiscossione /*, dataRiscossione */ });
  }

  /**
   * Funzione che emette l'evento di reset per i dati del calcolo canone.
   */
  resetDatiCanone() {
    // Emetto l'evento
    this.onResetCalcoloCanone.next(true);
  }

  /**
   * Funzione che emette l'evento di reset per il campo del canone.
   */
  resetCanoneCalcolato() {
    // Emetto l'evento
    this.onResetCanoneCalcolato.next(true);
  }

  /**
   * Funzione che permette di resettare la struttura di gestione dei dati del calcolo canone.
   */
  resetOnDatiCalcoloCanone() {
    // Reimposto l'observable
    this.onDatiCalcoloCanone = new BehaviorSubject<IDatiCalcoloCanone>(
      undefined
    );
  }

  /**
   * Funzione che emette un evento per: errore nel calcolo canone.
   * @param e RiscaServerError con l'errore da emettere.
   */
  calcoloCanoneError(e: RiscaServerError) {
    // Emetto l'evento
    this.onCalcoloCanoneError.next(e);
  }

  /**
   * Funzione che emette un evento per: errore nel calcolo canone, dovuto alla mancanza di regole per il calcolo.
   * @param e RiscaServerError con l'errore da emettere.
   */
  regolaMancanteCC(e: RiscaServerError) {
    // Emetto l'evento
    this.onRegolaMancanteCC.next(e);
  }

  /**
   * Funzione di comodo che invoca l'attivazione degli errori e la comunicazione ai componenti padri.
   * @param e RiscaServerError con l'oggetto d'errore generato.
   */
  onServiziError(e: RiscaServerError) {
    // Richiamo la funzione del servizio e propago l'errore
    this.onServiziError$.next(e);
  }

  /**
   * ##########################
   * SEGNALAZIONI ERRORI COMUNI
   * ##########################
   */

  /**
   * Funzione di supporto che gestisce la casistica d'errore in caso in cui manchino i dati tecnici per la gestione delle pagine dei dati tecnici.
   */
  dtNotDefined() {
    // Recupero titolo e body per il messaggio
    const t = this.DTA_C.NO_DT_TITLE;
    const b = this.DTA_C.NO_DT_DES;
    // Scrivo in console l'errore
    this._logger.error(t, b);
  }
}
