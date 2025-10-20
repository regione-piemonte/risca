import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable()
export class RiscaNoCacheInterceptor implements HttpInterceptor {
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  public intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Clono la request con l'aggiunta dei nuovi header
    req = req.clone({
      headers: req.headers
        .set('Cache-Control', 'no-cache')
        .set('Pragma', 'no-cache'),
    });

    // "Rilancio" la request aggiornata
    return next.handle(req);
  }
}
