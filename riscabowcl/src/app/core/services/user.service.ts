import { HttpClient } from '@angular/common/http';
import { Injectable, OnDestroy } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { forkJoin, Observable, Subject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { RiscaStorageService } from '../../shared/services/risca/risca-storage.service';
import { RiscaServerError, UserRoles } from '../../shared/utilities';
import { RISCA_USER_SESSION_STORAGE } from '../../shared/utilities/consts/utilities.consts';
import { LoginTracciamento } from '../commons/class/tracciamento';
import { AmbitoVo } from '../commons/vo/ambito-vo';
import { RiscaRpTypes } from '../commons/vo/csi-log-audit-vo';
import { LoginCSILogAuditVo } from '../commons/vo/login-csi-log-audit-vo';
import { UserInfoVo } from '../commons/vo/user-info-vo';
import { ConfigService } from './config.service';

/**
 * Interfaccia di comodo per la gestione della richiesta tracciamento e logaudit login.
 */
interface ILoginTLAReq {
  logAudit: Observable<any>;
  tracciamento: Observable<any>;
}
/**
 * Interfaccia di comodo per la gestione della risposta tracciamento e logaudit login.
 */
interface ILoginTLARes {
  logAudit: any;
  tracciamento: any;
}

/**
 * Interfaccia di comodo per la gestione della richiesta logaudit login.
 * emmanuel
 */
interface ILoginTLAReqLogAudit {
  logAudit: Observable<any>;
}

/**
 * Interfaccia di comodo per la gestione della risposta logaudit login.
 */
interface ILoginTLAResLogAudit {
  logAudit: any;
}



/**
 * @version SONARQUBE_22_04_24 Rimosso OnDestroy vuoto. 
 */
@Injectable({
  providedIn: 'root',
})
export class UserService {
  /** Costante con il path per: /info-utente */
  private PATH_INFO_UTENTE = '/info-utente';
  /** Costante con il path per: /csi-log-audit */
  private PATH_CSI_LOG_AUDIT = '/csi-log-audit';
  /** Costante con il path per: /tracciamento */
  private PATH_TRACCIAMENTO = '/tracciamento';
  /** Costante con il path per: /localLogout. */
  private PATH_LOGOUT = '/localLogout';

  /** Subject che permette di registrarsi all'evento di avvenuta modifica sugli step di journey. */
  onLoginSuccess$ = new Subject<UserInfoVo>();

  /** UserInfoVo con i dati scaricati dal servizio. */
  private _user: UserInfoVo;

  /**
   * Costruttore.
   */
  constructor(
    private _cookie: CookieService,
    private _config: ConfigService,
    private _http: HttpClient,
    private _riscaStorage: RiscaStorageService
  ) {}

  /**
   * Getter per l'user loggato.
   * @returns Observable<any>.
   */
  getUser(): Observable<UserInfoVo> {
    // Definisco l'url
    const url: string = `${this.urlBE}${this.PATH_INFO_UTENTE}`;

    // Effettuo la chiamata per il recupero delle informazioni
    return this._http.get<UserInfoVo>(url).pipe(
      tap((user: UserInfoVo) => {
        // Emetto il cambio dati
        this._user = user;
        // Salvo i dati in sessione
        this._riscaStorage.sSetItem(RISCA_USER_SESSION_STORAGE, user);
        // #
      }),
      tap((user: UserInfoVo) => {
        // Provvisorio: effettuo la chiamata per il logaudit (emmanuel)
        this.loginTrackingNotTracciamento(user).subscribe();

         // Provvisorio: effettuo la chiamata per il logaudit e tracciamento
        //this.loginTracking(user).subscribe();
      })
    );
  }

  /**
   * Funzione che effettua il tracciamento e il log audit per la login.
   * @param u UserInfoVo con i dati dell'utente.
   * @returns Observable<ILoginTLARes> con i dati restituiti.
   */
  private loginTracking(u: UserInfoVo): Observable<ILoginTLARes> {
    // Definisco l'insieme di chiamate da fare per logaudit e tracciamento
    const tlaReq: ILoginTLAReq = {
      logAudit: this.loginCSILogAudit(u),
      tracciamento: this.loginTracciamento(),
    };

    // Ritorno la forkJoin delle chiamate
    return forkJoin(tlaReq);
  }

      /**
   * Funzione che effettua il tracciamento e il log audit per la login.
   * @param u UserInfoVo con i dati dell'utente.
   * @returns Observable<ILoginTLARes> con i dati restituiti.
   * emmanuel
   */
    private loginTrackingNotTracciamento(u: UserInfoVo): Observable<ILoginTLAResLogAudit> {
      // Definisco l'insieme di chiamate da fare per logaudit
      const tlaReq: ILoginTLAReqLogAudit = {
        logAudit: this.loginCSILogAudit(u),
      };
  
      // Ritorno la forkJoin delle chiamate
      return forkJoin(tlaReq);
    }
    

  /**
   * Funzione che effettua il log audit per la login.
   * @param user UserInfoVo con i dati dell'utente.
   * @returns Observable<any> con i dati restituiti.
   */
  private loginCSILogAudit(user: UserInfoVo): Observable<any> {
    // Definisco l'url
    const url: string = `${this.urlBE}${this.PATH_CSI_LOG_AUDIT}`;

    // Recupero le informazioni per il logaudit
    const cf = user?.codFisc;
    const env = this.isDev ? RiscaRpTypes.sviluppo : RiscaRpTypes.produzione;
    // Definisco il body della richiesta
    const logAudit = new LoginCSILogAuditVo(env, cf);

    // Richiamo l'API
    return this._http.post(url, logAudit);
  }

  /**
   * Funzione che effettua il tracciamento per la login.
   * @returns Observable<any> con i dati restituiti.
   */
  private loginTracciamento(): Observable<any> {
    // Definisco l'url
    const url: string = `${this.urlBE}${this.PATH_TRACCIAMENTO}`;

    // Definisco il body della richiestaPi
    const tracciamento = new LoginTracciamento();

    // Richiamo l'API
    return this._http.post(url, tracciamento);
  }

  /**
   * Funzione che invoca il logout applicativo.
   */
  logOut() {
    // Creo l'url
    const url = this._config.appUrl(this.PATH_LOGOUT);
    // Richiamo il servizio
    this._http.get(url).subscribe({
      next: () => {
        // Rimuovo i cookies di sessione
        this.clearAppData();
        // Logout completato, effettuo la navigazione definita per l'ambiente
        window.location.replace(this._config.localLogoutRedirect);
        // #
      },
      error: (error: RiscaServerError) => {
        // Per ora non si segnala niente
      },
    });
  }

  forceLogin() {
    // Creo l'url
    const url = this._config.appUrl(this.PATH_LOGOUT);
    // Richiamo il servizio
    this._http.get(url).subscribe({
      next: () => {
        // Rimuovo i cookies di sessione
        this.clearAppData();
        // Logout completato, effettuo la navigazione definita per l'ambiente
        window.location.replace(this._config.beServerPrefix);
        // #
      },
      error: (error: RiscaServerError) => {
        // Per ora non si segnala niente
      },
    });
  }

  /**
   * Funzione di comodo che rimuove tutte le informazioni dell'applicazione: cookie, local e session storage.
   */
  private clearAppData() {
    // Rimuovo i cookies di sessione
    this._cookie.deleteAll();
    // Rimuovo i dati dalla session storage
    this._riscaStorage.sClear();
    // Rimuovo i dati dalla local storage
    this._riscaStorage.lClear();
  }

  /**
   * Funzione di comodo che emette i dati dell'utente al termine della login.
   */
  onLoginSuccess() {
    // Emetto l'evento di login completato
    this.onLoginSuccess$.next(this.user);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di comodo per la gestione dell'url del BE.
   */
  private get urlBE() {
    return this._config.getBEUrl();
  }

  /**
   * Getter di comodo per la gestione dell'environment dell'app.
   */
  get isDev() {
    return this._config.isDebugMode();
  }

  /**
   * Getter per user.
   * @returns UserInfoVo con l'ultimo valore per user.
   */
  get user(): UserInfoVo {
    return this._user;
  }

  /**
   * Getter per il codice fiscale dell'user.
   * @returns string con l'ultimo valore per il codice fiscale user.
   */
  get cf(): string {
    return this.user?.codFisc ?? '';
  }

  /**
   * Getter per l'id dell'ambito.
   * @returns number con l'id.
   */
  get idAmbito(): number {
    // Ritorno l'id ambito
    return this._user?.ambito;
  }

  /**
   * Getter per l'ambito dell'utete.
   * NOTA BENE: Al momento non è gestito l'oggetto, ma viene creato un oggetto di supporto localmente.
   */
  get ambito(): AmbitoVo {
    // Recupero l'id ambito
    const id_ambito = this.idAmbito;
    // Ritorno un oggetto con l'idAmbito
    return { id_ambito, cod_ambito: '', des_ambito: '' };
  }

  /**
   * Getter che ritorna il risultato sulla verifica del ruolo dell'utente per: CONSULTATORE.
   * @returns boolean con il risultato del controllo.
   */
  get isCONSULTATORE(): boolean {
    // Verifico il ruolo dell'utente attivo
    return this.user.ruolo === UserRoles.consultatore;
  }

  /**
   * Getter che ritorna il risultato sulla verifica del ruolo dell'utente per: GESTORE_BASE.
   * @returns boolean con il risultato del controllo.
   */
  get isGESTOREBASE(): boolean {
    // Verifico il ruolo dell'utente attivo
    return this.user.ruolo === UserRoles.gestoreBase;
  }
}
