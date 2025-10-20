import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { RiscaServerStatus } from '../../shared/utilities';
import { ConfigService } from '../services/config.service';
import { LoggerService } from '../services/logger.service';

@Injectable()
export class AppAPIErrorsInterceptor implements HttpInterceptor {
  constructor(private _config: ConfigService, private _logger: LoggerService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((httpError: HttpErrorResponse) => {
        // Verifico se l'errore ha generato uno status per sessione scaduta (302)
        this.sessionExpired(request, httpError);

        // Ritorno l'errore
        return throwError(httpError);
        // #
      })
    );
  }

  /**
   * Funzione che implementa le logiche di verifica e gestione per la casistica: sessione scaduta.
   * La funzione se intercetterà una sessione scaduta, tenterà di ricaricare l'applicazione.
   * NOTA BENE: la sessione scaduta di shibbolet, impedisce alle chiamate di procedere verso il server effettivo.
   *            Il risultato è che la risposta ottenuta avrà uno status 0 (sconosciuto).
   * @param request HttpRequest<any> con la chiamata effettuata.
   * @param httpResponse HttpResponse<any> | HttpErrorResponse con la response generata dalla chiamta.
   */
  private sessionExpired(
    request: HttpRequest<any>,
    httpResponse: HttpResponse<any> | HttpErrorResponse
  ) {
    // Verifico se l'errore ha generato uno status per sessione scaduta
    let expired: boolean;
    expired = httpResponse?.status == RiscaServerStatus.unknown;

    // Verifico se la connessione è scaduta
    if (expired) {
      // Definisco le informazioni per il log applicativo
      const title = `Session expired`;
      this._logger.warning(title, request);

      // Effettuo il relog tramite la navigazione definita per l'ambiente
      window.location.replace(this._config.applicationUrl);
    }
  }
}
