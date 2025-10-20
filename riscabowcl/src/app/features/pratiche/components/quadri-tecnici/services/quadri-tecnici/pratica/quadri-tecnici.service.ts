import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { PraticaDTVo } from '../../../../../../../core/commons/vo/pratica-vo';
import {
  AppActions,
  RiscaServerError,
} from '../../../../../../../shared/utilities';
import { DatiTecniciAmbienteConsts } from '../../../../../consts/quadri-tecnici/dati-tecnici-ambiente/dati-tecnici-ambiente.consts';
import { IDatiCalcoloCanone } from '../../../../../interfaces/quadri-tecnici/quadri-tecnici.interfaces';

@Injectable({
  providedIn: 'root',
})
export class QuadriTecniciService {
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_C = new DatiTecniciAmbienteConsts();

  /** EventEmitter che emette l'evento per il submit della form. */
  onFormSubmit$ = new Subject<boolean>();
  /** EventEmitter che emette l'evento per il reset della form. */
  onFormReset$ = new Subject<boolean>();

  /** EventEmitter permette di fare aggiornare al componente dei dati tecnici per ambito, la struttura dei dati tecnici. */
  onDatiTecniciUpdate$ = new Subject<PraticaDTVo>();
  /** EventEmitter permette di fare aggiornare al componente del calcolo canone le informazioni di calcolo. */
  onDatiCalcoloCanone$ = new BehaviorSubject<IDatiCalcoloCanone>(undefined);
  /** EventEmitter permette di resettare al componente del calcolo canone  le informazioni di gestione. */
  onResetEAbilitaCanone$ = new Subject<boolean>();
  /** EventEmitter permette di resettare il campo del canone. */
  onResetEDisabilitaCanone$ = new Subject<boolean>();
  /** EventEmitter permette di resettare il campo del canone. */
  onCambioModalita$ = new Subject<AppActions>();
  /** EventEmitter che definisce il momento in cui la pratica è stata salvata (per inserimento o per modifica). */
  onPraticaSaved$ = new Subject<any>();

  /** EventEmitter che informa che il calcolo del canone è fallito. */
  onCalcoloCanoneError$ = new Subject<RiscaServerError>();
  /** EventEmitter che informa che il calcolo del canone è fallito per la mancanza della regola di calcolo. */
  onRegolaMancanteCC$ = new Subject<RiscaServerError>();

  /** EventEmitter che informa che si è verificato un errore nella gestione delle chiamate ai servizi. */
  onServiziError$ = new Subject<RiscaServerError>();

  /**
   * Costruttore
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che emette l'evento di change dei dati tecnici.
   * @param praticaDTVo PraticaDTVo con all'interno i dati tecnici d'aggiornare.
   */
  aggiornaDatiTecnici(praticaDTVo: PraticaDTVo) {
    // Emetto l'evento di dati tecnici aggiornati
    this.onDatiTecniciUpdate$.next(praticaDTVo);
  }

  /**
   * Funzione che emette l'evento di change dei dati del calcolo canone.
   * @param idRiscossione string o number che identifica una riscossione.
   * @deprecated dataRiscossione string che identifica la data di riscossione. La data deve avere formato: AAAA-MM-GG.
   */
  aggiornaDatiCanone(
    idRiscossione: string | number /*dataRiscossione: string*/
  ) {
    // Emetto l'evento
    this.onDatiCalcoloCanone$.next({ idRiscossione /*, dataRiscossione*/ });
  }

  /**
   * Funzione che emette l'evento di reset per i dati del calcolo canone.
   */
  resetEAbilitaCanone() {
    // Emetto l'evento
    this.onResetEAbilitaCanone$.next(true);
  }

  /**
   * Funzione che emette l'evento di reset per il campo del canone.
   */
  resetEDisabilitaCanone() {
    // Emetto l'evento
    this.onResetEDisabilitaCanone$.next(true);
  }

  /**
   * Funzione che permette di resettare la struttura di gestione dei dati del calcolo canone.
   */
  resetOnDatiCalcoloCanone() {
    // Reimposto l'observable
    this.onDatiCalcoloCanone$ = new BehaviorSubject<IDatiCalcoloCanone>(
      undefined
    );
  }

  /**
   * Funzione che emette un evento per impostare la modalità del componente in: modifica.
   * @param m AppActions.inserimento | AppActions.modifica che definisce la modalità da cambiare per i componenti dei dati tecnici.
   */
  cambioModalita(m: AppActions.inserimento | AppActions.modifica) {
    // Emetto l'evento
    this.onCambioModalita$.next(m);
  }

  /**
   * Funzione che emette un evento per impostare la modalità del componente in: modifica.
   */
  impostaModifica() {
    // Emetto l'evento
    this.cambioModalita(AppActions.modifica);
  }

  /**
   * Funzione che emette un evento per impostare la modalità del componente in: niserimento.
   */
  impostaInserimento() {
    // Emetto l'evento
    this.cambioModalita(AppActions.inserimento);
  }

  /**
   * Funzione che emette un evento per informare i componenti che la pratica è stata salvata (per inserimento o per modifica).
   * @param v any da passare come payload ai componenti.
   */
  praticaSaved(v?: any) {
    // Emetto l'evento
    this.onPraticaSaved$.next(v);
  }

  /**
   * Funzione che emette un evento per: errore nel calcolo canone.
   * @param e RiscaServerError con l'errore da emettere.
   */
  calcoloCanoneError(e: RiscaServerError) {
    // Emetto l'evento
    this.onCalcoloCanoneError$.next(e);
  }

  /**
   * Funzione che emette un evento per: errore nel calcolo canone, dovuto alla mancanza di regole per il calcolo.
   * @param e RiscaServerError con l'errore da emettere.
   */
  regolaMancanteCC(e: RiscaServerError) {
    // Emetto l'evento
    this.onRegolaMancanteCC$.next(e);
  }

  /**
   * Funzione di comodo che invoca l'attivazione degli errori e la comunicazione ai componenti padri.
   * @param e RiscaServerError con l'oggetto d'errore generato.
   */
  onServiziError(e: RiscaServerError) {
    // Richiamo la funzione del servizio e propago l'errore
    this.onServiziError$.next(e);
  }
}
