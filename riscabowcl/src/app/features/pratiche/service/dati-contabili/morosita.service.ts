import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { cloneDeep } from 'lodash';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ConfigService } from 'src/app/core/services/config.service';
import { TipoRicercaMorositaVo } from '../../../../core/commons/vo/tipo-ricerca-morosita-vo';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';

@Injectable({ providedIn: 'root' })
export class MorositaService extends HttpUtilitiesService {
  /** Costante per il path: /tipi-ricerca-morosita */
  private PATH_TIPI_RICERCA_MOROSITA = '/tipi-ricerca-morosita';

  /** TipoRicercaMorosita[] con la lista di dati scaricati e da tenere in sessione. */
  _tipiRicercaMorosita: TipoRicercaMorositaVo[];

  /**
   * Costruttore.
   */
  constructor(
    config: ConfigService,
    private _http: HttpClient,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * Funzione che recupera i tipi ricerca morosità esistenti.
   * @returns Observable<TipoRicercaMorositaVo[]> con la lista degli elementi tornati dal servizio.
   */
  getTipiRicercaMorosita(): Observable<TipoRicercaMorositaVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._tipiRicercaMorosita?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._tipiRicercaMorosita);
    }

    // Definisco l'url
    const url = this.appUrl(this.PATH_TIPI_RICERCA_MOROSITA);
    // Richiamo il servizio
    return this._http.get<TipoRicercaMorositaVo[]>(url).pipe(
      tap((trMorosita: TipoRicercaMorositaVo[]) => {
        // Assegno localmente il risultato
        this._tipiRicercaMorosita = cloneDeep(trMorosita);
      })
    );
  }
}
