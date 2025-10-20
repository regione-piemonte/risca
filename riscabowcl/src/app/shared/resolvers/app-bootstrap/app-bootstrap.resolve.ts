import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { backEnd } from 'src/environments/back-end';
import { environment } from 'src/environments/environment';
import { ConfigService } from '../../../core/services/config.service';

@Injectable({ providedIn: 'root' })
export class AppBootstrapInit {
  /** String che definisce il path per lo scarico dati */
  private beUrl = `${environment.beServerPrefix}${backEnd.path}`;

  /**
   * Costruttore.
   */
  constructor(_configuration: ConfigService, private _http: HttpClient) {
    // Imposto l'url del backend
    this.beUrl = _configuration.getBEUrl();
  }

  /**
   * Funzione d'inizializzazione dei dati necessari al funzionamento dell'app.
   * @returns Promise<boolean> con le informa
   */
  initAppData(): Promise<boolean> {
    // Richiamo lo scarico dei dati per l'utente
    // In previsione di sviluppi futuri, per ora manteniamo solo un mock
    return of(true).toPromise();
  }
}
