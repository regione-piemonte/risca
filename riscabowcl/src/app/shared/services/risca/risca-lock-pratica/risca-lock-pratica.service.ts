import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import {
  ILockRiscossioneVo,
  LockRiscossioneVo,
} from '../../../../core/commons/vo/lock-riscossione-vo';
import { UserInfoVo } from '../../../../core/commons/vo/user-info-vo';
import { ConfigService } from '../../../../core/services/config.service';
import { UserService } from '../../../../core/services/user.service';
import { RiscaNotifyCodes } from '../../../utilities/enums/risca-notify-codes.enums';
import { HttpUtilitiesService } from '../../http-utilities/http-utilities.service';
import { RiscaMessagesService } from '../risca-messages.service';
import { RiscaModalService } from '../risca-modal.service';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';
import { IRichiestaLockPraticaRes } from './utilities/risca-lock-pratica.interfaces';

/**
 * Servizio che gestisce le logiche e le chiamate per il lock della pratica sull'applicazione.
 */
@Injectable({ providedIn: 'root' })
export class RiscaLockPraticaService extends HttpUtilitiesService {
  /** Costante con il path per: /lock-riscossione. */
  private PATH_LOCK_RISCOSSIONE = '/lock-riscossione';

  /** Subject che permette di gestire l'evento manuale di: avvenuto lock sulla pratica. */
  onPraticaLocked$ = new Subject<LockRiscossioneVo>();
  /** Subject che permette di gestire l'evento manuale di: avvenuto lock sulla pratica. */
  onPraticaUnlocked$ = new Subject<any>();

  /** LockRiscossioneVo che definisce l'oggetto che rappresenta il lock della riscossione in sessione. */
  private _lockRiscossione: LockRiscossioneVo;

  /**
   * Costruttore.
   */
  constructor(
    config: ConfigService,
    private _http: HttpClient,
    private _riscaMessages: RiscaMessagesService,
    private _riscaModal: RiscaModalService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * Funzione che estrae tutte le pratiche attualmente lockate su DB.
   * @returns Observable<ILockRiscossione[]> con la lista delle pratiche lockate.
   */
  riscossioniLocked(): Observable<ILockRiscossioneVo[]> {
    // Costruisco l'url per la chiamata
    const url = this.appUrl(this.PATH_LOCK_RISCOSSIONE);
    // Effettuo la chiamata al servizio
    return this._http.get<ILockRiscossioneVo[]>(url).pipe(
      map((lockRi: ILockRiscossioneVo[]) => {
        // Richiamo la funzione di conversione di comodo
        return this.lockRiscossioniBEToFE(lockRi);
      })
    );
  }

  /**
   * Funzione che ritorna le informazioni di una pratica lockata dato l'id pratica da verificare.
   * Se non esiste nessuna pratica lockata, ritornarà undefined.
   * @param idPratica number con l'id pratica da lockare.
   * @returns Observable<LockRiscossione> con il risultato della ricerca.
   */
  isRiscossioneLocked(idPratica: number): Observable<LockRiscossioneVo> {
    // Verifico che esista l'id pratica
    if (!idPratica) {
      // Non è definita nessuna pratica, tratto tutto come se non fosse salvata su DB
      return this._riscaUtilities.responseWithDelay(undefined);
    }

    // Costruisco l'url per la chiamata
    const url = this.appUrl(this.PATH_LOCK_RISCOSSIONE);
    // Definisco i parametri per la chiamata
    const params = this.createQueryParams({ idRiscossione: idPratica });
    // Effettuo la chiamata al servizio
    return this._http.get<ILockRiscossioneVo>(url, { params }).pipe(
      map((lockR: ILockRiscossioneVo) => {
        // Workaround fino a che il BO/BE sistema. Se non c'è salvato niente su DB, invece di tornare null, torna oggetto vuoto { }. Metto un controllo di sicurezza
        if (lockR && this.isEmptyObject(lockR)) {
          // Modifico l'output del servizio e ritorno null
          return null;
        }
        // Il dato esiste ed è valorizzato, oppure non esiste direttamente
        return lockR;
        // #
      }),
      map((lockR: ILockRiscossioneVo) => {
        // Richiamo la funzione di conversione di comodo
        return this.lockRiscossioneBEToFE(lockR);
      })
    );
  }

  /**
   * Funzione che permette di lockare una pratica su DB.
   * @param idPratica number con l'id pratica da lockare.
   * @returns Observable<ILockRiscossione> con il risultato del lock della pratica.
   */
  private lockRiscossione(idPratica: number): Observable<ILockRiscossioneVo> {
    // Costruisco l'url per la chiamata
    const url = this.appUrl(this.PATH_LOCK_RISCOSSIONE);
    // Definisco l'oggetto per il lock della pratica
    const lockPratica: ILockRiscossioneVo = {
      id_riscossione: idPratica,
      utente_lock: this.generalitaUtente,
      cf_utente_lock: this.cfUtente,
    };
    // Creo un oggetto LockRiscossione e lo rigenero le informazioni compatibili con il BE
    const lp = new LockRiscossioneVo(lockPratica);
    // Assegno ed invio il dato in modalità BE
    const lpBE: ILockRiscossioneVo = lp.toServerFormat();

    // Effettuo la chiamata al servizio
    return this._http.post<ILockRiscossioneVo>(url, lpBE).pipe(
      map((lockR: ILockRiscossioneVo) => {
        // Richiamo la funzione di conversione di comodo
        return this.lockRiscossioneBEToFE(lockR);
      })
    );
  }

  /**
   * Funzione che permette di rimuovere il lock di una pratica su DB.
   * @param idPratica number con l'id pratica da sbloccare.
   * @returns Observable<ILockRiscossione> con il risultato dello sblocco della pratica.
   */
  private unlockRiscossione(idPratica: number): Observable<ILockRiscossioneVo> {
    // Costruisco l'url per la chiamata
    const url = this.appUrl(this.PATH_LOCK_RISCOSSIONE);
    // Definisco i parametri per la chiamata
    const params = this.createQueryParams({ idRiscossione: idPratica });
    // Effettuo la chiamata al servizio
    return this._http.delete<ILockRiscossioneVo>(url, { params }).pipe(
      map((lockR: ILockRiscossioneVo) => {
        // Richiamo la funzione di conversione di comodo
        return this.lockRiscossioneBEToFE(lockR);
      })
    );
  }

  /**
   * Funzione di comodo per la conversione dell'oggetto del lock riscossione da BE a FE.
   * @param lockR ILockRiscossioneVo da convertire.
   * @returns LockRiscossioneVo convertito.
   */
  private lockRiscossioneBEToFE(lockR: ILockRiscossioneVo): LockRiscossioneVo {
    // Verifico se esiste l'oggetto
    if (!lockR) {
      // Mantengo l'oggetto undefined
      return undefined;
    }
    // Converto in formato FE l'oggetto
    const lockRVo = new LockRiscossioneVo(lockR);
    // Ritorno l'oggetto generato
    return lockRVo;
  }

  /**
   * Funzione di comodo per la conversione di un array di oggetti del lock riscossione da BE a FE.
   * @param lockRi ILockRiscossioneVo[] da convertire.
   * @returns LockRiscossioneVo[] convertito.
   */
  private lockRiscossioniBEToFE(
    lockRi: ILockRiscossioneVo[]
  ): LockRiscossioneVo[] {
    // Verifico se esiste l'oggetto
    if (!lockRi) {
      // Ritorno un array vuoto
      return [];
    }
    // Converto in formato FE gli oggetti
    return lockRi.map((lockR: ILockRiscossioneVo) => {
      // Creo un oggetto FE
      return this.lockRiscossioneBEToFE(lockR);
    });
  }

  /**
   * ##################################################
   * FUNZIONE DI VERIFICA E GESTIONE LOCK DELLA PRATICA
   * ##################################################
   */

  /**
   * Funzione custom che invoca il servizio di lock di una pratica.
   * La funzione gestirà il dato all'interno del servizio.
   * La funzione non ha ritorno, ma fa solo partire le logiche di blocco pratica.
   * Se l'utente ha ruolo: CONSULTATORE; non viene lanciata nessuna logica.
   * @param idPratica number con l'id pratica da lockare.
   */
  richiediBloccaPratica(idPratica?: number) {
    // Funzione attiva solo se l'utente non è CONSULTATORE
    if (this._user.isCONSULTATORE) {
      // Blocco le logiche
      return;
    }

    // Richiamo il servizio di sblocco pratica
    this.bloccaPratica(idPratica).subscribe();
  }

  /**
   * Funzione custom che invoca il servizio di lock di una pratica.
   * Se la pratica è già lockata oppure è stata lockata con la chiamata, la funzione gestirà il dato all'interno del servizio.
   * @param idPratica number con l'id pratica da lockare.
   * @returns Observable<IRichiestaBloccoPraticaRes> con il risultato del lock della pratica.
   */
  bloccaPratica(idPratica: number): Observable<IRichiestaLockPraticaRes> {
    // Verifico se sul DB la pratica è già stata lockata
    return this.isRiscossioneLocked(idPratica).pipe(
      switchMap((lockPVo: LockRiscossioneVo) => {
        // Richiamo la funzione per gestire il flusso di controllo
        return this.onBloccaPratica(lockPVo, idPratica);
      })
    );
  }

  /**
   * Funzione che gestisce il flusso logico al ritorno del controllo di "pratica lockata su DB?".
   * A seconda dei parametri in input, verrà impostato un flusso dati per ottenere un risultato che permetta di gestire l'apertura della pratica.
   * @param lockPVo LockRiscossioneVo con i dati della pratica già in lock su DB.
   * @param idPratica number con l'id della pratica di lockare per l'utente corrente.
   * @returns Observable<IRichiestaBloccoPraticaRes> con le informazioni per la gestione dell'apertura della pratica.
   */
  private onBloccaPratica(
    lockPVo: LockRiscossioneVo,
    idPratica: number
  ): Observable<IRichiestaLockPraticaRes> {
    // Verifico se è stato ritornato un oggetto (pratica già lockata) oppure undefined (pratica non lockata)
    const existLockPVo = lockPVo != undefined;
    // Verifico se l'utente che sta lockando la pratica è lo stesso attualmente loggato
    const isUserLocking =
      existLockPVo && this.cfUtente == lockPVo.cf_utente_lock;

    // Verifico le condizioni per la gestione del dato di lock
    if (!existLockPVo) {
      // Ritorno la funzione per: pratica non ancora lockata
      return this.onReqBlockPraticaNonAncoraLockata(idPratica);
      // #
    } else if (isUserLocking) {
      // Ritorno la funzione per: pratica già lockata dall'utente connesso
      return this.onReqBlockPraticaLockataUtenteAttuale(lockPVo);
      // #
    } else {
      // Ritorno la funzione per: pratica già lockata
      return this.onReqBlockPraticaGiaLockata(lockPVo);
      // #
    }
  }

  /**
   * Funzione di supporto che gestisce il flusso logico quando il controllo per il lock di una pratica risulta con la pratica non ancora lockata su DB.
   * @param idPratica number con l'id della pratica di lockare per l'utente corrente.
   * @returns Observable<IRichiestaBloccoPraticaRes> con le informazioni per la pratica lockata dall'utente corrente.
   */
  private onReqBlockPraticaNonAncoraLockata(
    idPratica: number
  ): Observable<IRichiestaLockPraticaRes> {
    // La pratica non è ancora stata lockata, vado a bloccarla per l'utente loggato
    return this.lockRiscossione(idPratica).pipe(
      map((lockPVo: LockRiscossioneVo) => {
        // Pratica lockata, imposto l'oggetto di lock nel servizio
        this._lockRiscossione = lockPVo;
        // Definisco le informazioni di risposta
        const result: IRichiestaLockPraticaRes = {
          lockRiscossione: lockPVo,
          isUserLockingRiscossione: true,
          code4PraticaLocked: undefined,
        };
        // Ritorno come risposta un oggetto che contiene le informazioni di comunicazione all'utente
        return result;
      }),
      tap(() => {
        // Emetto l'evento di avvenuto lock pratica
        this.onPraticaLocked(this._lockRiscossione);
      })
    );
  }

  /**
   * Funzione di supporto che gestisce il flusso logico quando il controllo per il lock di una pratica risulta con la pratica non ancora lockata su DB.
   * @param lockPVo LockRiscossioneVo con i dati della pratica già in lock su DB.
   * @returns Observable<IRichiestaBloccoPraticaRes> con le informazioni per la pratica lockata dall'utente corrente.
   */
  private onReqBlockPraticaLockataUtenteAttuale(
    lockPVo: LockRiscossioneVo
  ): Observable<IRichiestaLockPraticaRes> {
    // L'utente attualmente connesso ha lockato in precedenza la pratica, salvo i dati in sessione
    this._lockRiscossione = lockPVo;
    // Definisco le informazioni di risposta
    const result: IRichiestaLockPraticaRes = {
      lockRiscossione: lockPVo,
      isUserLockingRiscossione: true,
      code4PraticaLocked: undefined,
    };

    // Emetto l'evento di avvenuto lock pratica
    this.onPraticaLocked(this._lockRiscossione);

    // Ritorno come risposta un oggetto che contiene le informazioni di comunicazione all'utente
    return this._riscaUtilities.responseWithDelay(result);
  }

  /**
   * Funzione di supporto che gestisce il flusso logico quando il controllo per il lock di una pratica risulta con la pratica già lockata su DB.
   * @param lockPVo LockRiscossioneVo con i dati della pratica già in lock su DB.
   * @returns Observable<IRichiestaBloccoPraticaRes> con le informazioni per la pratica già lockata.
   */
  private onReqBlockPraticaGiaLockata(
    lockPVo: LockRiscossioneVo
  ): Observable<IRichiestaLockPraticaRes> {
    // Pratica già lockata, imposto l'oggetto di lock nel servizio
    this._lockRiscossione = lockPVo;
    // Definisco le informazioni di risposta
    const result: IRichiestaLockPraticaRes = {
      lockRiscossione: lockPVo,
      isUserLockingRiscossione: false,
      code4PraticaLocked: RiscaNotifyCodes.I024,
    };

    // Emetto l'evento di avvenuto lock pratica
    this.onPraticaLocked(this._lockRiscossione);

    // Ritorno come risposta un oggetto che contiene le informazioni di comunicazione all'utente
    return of(result);
  }

  /**
   * ##################################################
   * FUNZIONE DI VERIFICA E GESTIONE LOCK DELLA PRATICA
   * ##################################################
   */

  /**
   * Funzione custom che invoca il servizio di unlock di una pratica.
   * La funzione gestirà il dato all'interno del servizio.
   * La funzione non ha ritorno, ma fa solo partire le logiche di sblocco pratica.
   * Se l'utente ha ruolo: CONSULTATORE; non viene lanciata nessuna logica.
   * @param idPratica number con l'id pratica da lockare.
   */
  richiediSbloccaPratica(idPratica?: number) {
    // Funzione attiva solo se l'utente non è CONSULTATORE
    if (this._user.isCONSULTATORE) {
      // Blocco le logiche
      return;
    }

    // Richiamo il servizio di sblocco pratica
    this.sbloccaPratica(idPratica).subscribe();
  }

  /**
   * Funzione custom che invoca il servizio di unlock di una pratica.
   * La funzione gestirà il dato all'interno del servizio.
   * @param idPratica number con l'id pratica da lockare.
   * @returns Observable<IRichiestaLockPraticaRes> con il risultato del lock della pratica.
   */
  sbloccaPratica(idPratica?: number): Observable<IRichiestaLockPraticaRes> {
    // Verifico se devo gestire una specifica pratica o prendere quella in sessione
    const idP = idPratica ?? this._lockRiscossione?.id_riscossione;

    // Verifico se sul DB la pratica è già stata lockata
    return this.isRiscossioneLocked(idP).pipe(
      switchMap((lockPVo: LockRiscossioneVo) => {
        // Richiamo la funzione per gestire il flusso di controllo
        return this.onSbloccaPratica(lockPVo);
      })
    );
  }

  /**
   * Funzione che gestisce il flusso logico al ritorno del controllo di "pratica lockata su DB?".
   * A seconda dei parametri in input, verrà impostato un flusso dati per ottenere un risultato che permetta di gestire l'apertura della pratica.
   * @param lockPVo LockRiscossioneVo con i dati della pratica già in lock su DB.
   * @returns Observable<IRichiestaBloccoPraticaRes> con le informazioni per la gestione dell'apertura della pratica.
   */
  private onSbloccaPratica(
    lockPVo: LockRiscossioneVo
  ): Observable<IRichiestaLockPraticaRes> {
    // Verifico se è stato ritornato un oggetto (pratica già lockata) oppure undefined (pratica non lockata)
    const existLockPVo = lockPVo != undefined;
    // Verifico se l'utente che sta lockando la pratica è lo stesso attualmente loggato
    const isUserLocking =
      existLockPVo && this.cfUtente == lockPVo.cf_utente_lock;

    // Verifico le condizioni per la gestione del dato di lock
    if (!existLockPVo) {
      // Ritorno la funzione per: pratica non lockata su DB
      return this.onReqUnlockPraticaNonLockata();
      // #
    } else if (isUserLocking) {
      // Ritorno la funzione per: pratica lockata dall'utente connesso
      return this.onReqUnlockPraticaLockataUtenteAttuale(lockPVo);
      // #
    } else {
      // Ritorno la funzione per: pratica già lockata
      return this.onReqUnlockPraticaAltroUtente();
      // #
    }
  }

  /**
   * Funzione di supporto che gestisce il flusso logico quando il controllo per il lock di una pratica risulta con la pratica non ancora lockata su DB.
   * La funzione quindi non deve rimuovere informazioni su DB.
   * @returns Observable<IRichiestaBloccoPraticaRes> con le informazioni per la pratica lockata dall'utente corrente.
   */
  private onReqUnlockPraticaNonLockata(): Observable<IRichiestaLockPraticaRes> {
    // La pratica non risulta lockata su DB quindi bisogna rimuovere i dati solo nel servizio
    this._lockRiscossione = undefined;
    // Definisco le informazioni di risposta
    const result: IRichiestaLockPraticaRes = {
      lockRiscossione: undefined,
      isUserLockingRiscossione: false,
      code4PraticaLocked: undefined,
    };

    // Emetto l'evento di avvenuto unlock pratica
    this.onPraticaUnlocked();

    // Ritorno come risposta un oggetto che contiene le informazioni di comunicazione all'utente
    return of(result);
  }

  /**
   * Funzione di supporto che gestisce il flusso logico quando il controllo per il lock di una pratica risulta con la pratica non ancora lockata su DB.
   * @param lockPVo LockRiscossioneVo con i dati della pratica già in lock su DB.
   * @returns Observable<IRichiestaBloccoPraticaRes> con le informazioni per la pratica lockata dall'utente corrente.
   */
  private onReqUnlockPraticaLockataUtenteAttuale(
    lockPVo: LockRiscossioneVo
  ): Observable<IRichiestaLockPraticaRes> {
    // Ritorno come risposta un oggetto che contiene le informazioni di comunicazione all'utente
    return this.unlockRiscossione(lockPVo?.id_riscossione).pipe(
      map((lockPVoRes: LockRiscossioneVo) => {
        // Pratica sbloccata, aggiorno il servizio
        this._lockRiscossione = undefined;
        // Definisco le informazioni di risposta
        const result: IRichiestaLockPraticaRes = {
          lockRiscossione: undefined,
          isUserLockingRiscossione: false,
          code4PraticaLocked: undefined,
        };
        // Ritorno come risposta un oggetto che contiene le informazioni di comunicazione all'utente
        return result;
      }),
      tap(() => {
        // Emetto l'evento di avvenuto unlock pratica
        this.onPraticaUnlocked();
      })
    );
  }

  /**
   * Funzione di supporto che gestisce il flusso logico quando il controllo per il lock di una pratica risulta con la pratica già lockata su DB.
   * @returns Observable<IRichiestaBloccoPraticaRes> con le informazioni per la pratica già lockata.
   */
  private onReqUnlockPraticaAltroUtente(): Observable<IRichiestaLockPraticaRes> {
    // La pratica non risulta lockata su DB quindi bisogna rimuovere i dati solo nel servizio
    this._lockRiscossione = undefined;
    // Definisco le informazioni di risposta
    const result: IRichiestaLockPraticaRes = {
      lockRiscossione: undefined,
      isUserLockingRiscossione: false,
      code4PraticaLocked: undefined,
    };

    // Emetto l'evento di avvenuto unlock pratica
    this.onPraticaUnlocked();

    // Ritorno come risposta un oggetto che contiene le informazioni di comunicazione all'utente
    return of(result);
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione di supporto che gestisce la segnalazione di pratica lockata.
   * Se la pratica è già stata lockata da un altro utente, verrà visualizzato un prompt che avviserà l'utente.
   * @param richiestaLockRes IRichiestaLockPraticaRes con le informazioni risultanti dal lock della pratica.
   */
  utenteLockaPratica(richiestaLockRes: IRichiestaLockPraticaRes) {
    // Verifico l'input
    if (!richiestaLockRes) {
      // Niente configurazione
      return;
    }

    // Estraggo dal risultato di lock le informazioni
    const {
      lockRiscossione: lockR,
      isUserLockingRiscossione: isUserLocker,
      code4PraticaLocked: code,
    } = richiestaLockRes;

    // Verifico se è stato definito un codice da visualizzare all'utente e se l'utente NON è colui che ha lockato la pratica
    if (code && !isUserLocker) {
      // Esiste, vado definisco come placeholder le informazioni dell'utente
      const { utente_lock } = lockR;
      // Definisco il placeholder
      const ph = [utente_lock];
      // Recupero il messaggio sostituendo il placeholder
      const body = this._riscaMessages.getMessageWithPlacholderByCode(code, ph);

      // Apro una modale per segnalare il messaggio all'utente
      this._riscaModal.apriModalInformativa(body);
    }
  }

  /**
   * Funzione che verifica se, all'interno del servizio, è stata definita una configurazione di lock.
   * @returns boolean che definisce se esiste una definizione di lock della pratica.
   */
  isPraticaInLockStatus(): boolean {
    // Ritorno il check verificando la configurazione nel servizio
    return this._lockRiscossione !== undefined;
  }

  /**
   * Funzione che verifica se un utente sta lockando una pratica.
   * @param user UserInfoVo per verificare l'utente per il lock pratica.
   * @param lockPVo LockRiscossioneVo per verificare l'utente per il lock pratica.
   * @returns boolean che definisce se l'utente in input sta lockando la pratica.
   */
  isUserLockingPratica(user: UserInfoVo, lockPVo: LockRiscossioneVo): boolean {
    // Estraggo il codice fiscale dell'utente
    const userCF = user?.codFisc ?? 'NO_USER';
    // Estraggo il codice fiscale dall'oggetto di lock
    const lockCF = lockPVo?.cf_utente_lock ?? 'NO_LOCK_CF';
    // Verifico se è lo stesso codice fiscale del lockatore della pratica
    const sameCF = userCF === lockCF;

    // Ritorno il check sul lock utente
    return sameCF;
  }

  /**
   * Funzione che verifica se l'utente che sta lockando la pratica è l'utente attualmente connesso.
   * @param user UserInfoVo per verificare l'utente per il lock pratica. Se non definito, verrà recuperato il dato in sessione nei servizi.
   * @param lockPVo LockRiscossioneVo per verificare l'utente per il lock pratica. Se non definito, verrà recuperato il dato in sessione nei servizi.
   * @returns boolean che definisce se l'utente corrente sta lockando la pratica.
   */
  isCurrentUserLockingPratica(
    user?: UserInfoVo,
    lockPVo?: LockRiscossioneVo
  ): boolean {
    // Verifico l'input e definisco l'user da verificare
    const userCheck = user ?? this.user;
    // Verifico se è lo stesso codice fiscale del lockatore della pratica
    const lockPCheck = lockPVo ?? this._lockRiscossione;

    // Ritorno il check sul lock utente
    return this.isUserLockingPratica(userCheck, lockPCheck);
  }

  /**
   * Funzione che verifica se l'utente che sta lockando la pratica NON è l'utente attualmente connesso.
   * @param user UserInfoVo per verificare l'utente per il lock pratica. Se non definito, verrà recuperato il dato in sessione nei servizi.
   * @param lockPVo LockRiscossioneVo per verificare l'utente per il lock pratica. Se non definito, verrà recuperato il dato in sessione nei servizi.
   * @returns boolean che definisce se un altro utente sta lockando la pratica.
   */
  isAnotherUserLockingPratica(
    user?: UserInfoVo,
    lockPVo?: LockRiscossioneVo
  ): boolean {
    // Ritorno il check sul lock utente
    return !this.isCurrentUserLockingPratica(user, lockPVo);
  }

  /**
   * Funzione d'appoggio che richiama la utility del servizio che verifica se un oggetto è vuoto.
   * @param o any da verificare.
   * @returns boolean che definisce se l'oggetto è vuoto o undefined.
   */
  private isEmptyObject(o: any): boolean {
    // Richiamo la funzione di utility
    return this._riscaUtilities.isEmptyObject(o);
  }

  /**
   * #####################
   * GESTIONE DEGLI EVENTI
   * #####################
   */

  /**
   * Funzione di comodo che emette l'evento di avvenuto lock della pratica.
   * @param lockRiscossione LockRiscossioneVo con le informazioni del lock pratica. Se non definito, viene preso il dato salvato nel servizio.
   */
  onPraticaLocked(lockRiscossione: LockRiscossioneVo) {
    // Verifico l'input
    const lr: LockRiscossioneVo = lockRiscossione ?? this._lockRiscossione;
    // Lancio l'evento
    this.onPraticaLocked$.next(lr);
  }

  /**
   * Funzione di comodo che emette l'evento di avvenuto unlock della pratica.
   */
  onPraticaUnlocked() {
    // Lancio l'evento
    this.onPraticaUnlocked$.next(true);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Gettter di comodo per il recupero dell'user attualmente loggato in app.
   * @returns UserInfoVo con i dati dell'utente loggato.
   */
  private get user(): UserInfoVo {
    // Richiamo il recupero dal servizio
    return this._user.user;
  }

  /**
   * Getter di comodo che recupera le generalità dell'utente connesso.
   * @returns string con le generalità utente.
   */
  private get generalitaUtente(): string {
    // Recupero cognome e nome dal servizio user
    const nome = this.user?.nome ?? '';
    const cognome = this.user?.cognome ?? '';
    // Definisco le generalità complete
    const generalita = `${cognome} ${nome}`.trim();
    // Ritorno il codice fiscale
    return generalita;
  }

  /**
   * Getter di comodo che recupera il codice fiscale dell'utente connesso.
   * @returns string con il codice fiscale.
   */
  private get cfUtente(): string {
    // Recupero il codice fiscale dal servizio user
    const cf = this.user.codFisc ?? '';
    // Ritorno il codice fiscale
    return cf;
  }
}
