import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { RiscaServerError, RiscaServerErrorInfo } from '../utilities';
import { RiscaNotifyCodes } from '../utilities/enums/risca-notify-codes.enums';

/**
 * Interceptor adibito alla gestione degli errori ritornati dal sever.
 * Viene creata una mappa più pratica delle informazioni degli errori da poter gestire a livello applicativo.
 */
@Injectable()
export class RiscaServerErrorInterceptor implements HttpInterceptor {
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  public intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Lancio la chiamata e intercetto gli eventi di errore e di success
    return next.handle(request).pipe(
      catchError((httpError: HttpErrorResponse) => {
        // Creo una variabile d'appoggio per l'errore
        let riscaError = new RiscaServerError();
        // Mergio le proprietà restituite nell'errore risca
        riscaError = { ...riscaError, ...httpError };

        // Recupero l'errore dalla response
        const e = httpError.error;

        // Verifico se l'errore è una stringa
        const isEString = e && typeof e == 'string';
        // Verifico se l'errore è un array
        const isEArray = e && !isEString && e.length >= 0;

        // Verifico la struttura dell'errore ritornato
        if (isEArray) {
          // L'errore generato ha come tipo di error ==> array, modifico la struttura dell'errore
          riscaError.errors = httpError.error;
          // Rimuovo la proprietà error
          delete riscaError.error;
          // #
        } else if (isEString) {
          // L'errore generato è stringa, creo un oggetto di wrappre
          riscaError.error = new RiscaServerErrorInfo({
            code: RiscaNotifyCodes.E005,
            status: httpError?.status?.toString(),
            title: httpError.error,
          });
          // #
        }

        // Ritorno l'errore
        return throwError(riscaError);
      })
    );
  }
}
