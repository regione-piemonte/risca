import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, Observable, throwError, TimeoutError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { HttpHelperService } from '../../core/services/http-helper/http-helper.service';
import { CommonConsts } from '../consts/common-consts.consts';
import { RISCA_NO_SPINNER_HEADER } from '../consts/utilities.consts';
import { RiscaSpinnerService } from '../services/risca-spinner.service';

/**
 * Interceptor che permette di lanciare gli spinner di caricamento quando parte una chiamata http e quando si conclude.
 */
@Injectable()
export class RiscaSpinnerInterceptor implements HttpInterceptor {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /**
   * Costruttore.
   */
  constructor(
    private _httHelper: HttpHelperService,
    private _spinner: RiscaSpinnerService
  ) {}

  /**
   * Funzione che gestisce Angular per gestire il flusso delle chiamate ai servizi in uscita.
   * @param request HttpRequest<any> con la richiesta dati verso il servizio.
   * @param next HttpHandler con il gestore delle richieste dati.
   * @returns Observable<HttpEvent<any>> con la chiamata da effettuare.
   */
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Recupero la chiave per la gestione dello spinner
    const noSpinnerHeader = RISCA_NO_SPINNER_HEADER;
    // Verifico se negli header è stato richiesto di NON attivare lo spinner
    let noSpinner: boolean;
    noSpinner = this._httHelper.getHeader(noSpinnerHeader, request);

    // Verifico se è stato definito l'header
    if (noSpinner) {
      // Ritorno la request senza la gestione dello spinner
      return next.handle(request);
      // #
    } else {
      // Lancio e gestisco la chiamata con lo spinner
      return this.requestWithSpinner(request, next);
    }
  }

  /**
   * Funzione che gestisce uno spinner di caricamento automatico ad ogni chiamata effettuata verso i servizi.
   * @param request HttpRequest<any> con la richiesta dati verso il servizio.
   * @param next HttpHandler con il gestore delle richieste dati.
   * @returns Observable<HttpEvent<any>> con la chiamata da effettuare.
   */
  private requestWithSpinner(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Lancio lo spinner
    this._spinner.show();

    // Lancio la chiamata e intercetto gli eventi di errore e di success
    return next.handle(request).pipe(
      // timeout(300000),
      catchError((err) => {
        // ERRORE
        // Nascondo lo spinner
        this._spinner.hideAll();
        this._spinner.hideAllGeneric();

        // Gestisco l'errore di timeout
        if (err instanceof TimeoutError) {
          // Tratto l'errore come se non fosse successo niente
          return EMPTY;
        }

        // Ritorno l'errore
        return throwError(err);
      }),
      map<HttpEvent<any>, any>((evt: HttpEvent<any>) => {
        // SUCCESS - verifico che sia una response
        if (evt instanceof HttpResponse) {
          // Nascondo lo spinner
          this._spinner.hide();
        }
        // Lascio proseguire il flow
        return evt;
      })
    );
  }
}
