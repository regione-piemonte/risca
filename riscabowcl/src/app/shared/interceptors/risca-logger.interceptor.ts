import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoggerService } from '../../core/services/logger.service';

/**
 * Interceptor di utility che va ad effettuare un log delle richieste http, loggando l'url richiamato.
 */
@Injectable()
export class RiscaLoggerInterceptor implements HttpInterceptor {
  /**
   * Costruttore.
   */
  constructor(private _logger: LoggerService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Loggo le informazioni per la chiamata
    this._logger.httpRequest(request);
    // Continuo il flusso, effettuando la chiamata
    return next.handle(request);
  }
}
