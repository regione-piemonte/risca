import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { clone, cloneDeep } from 'lodash';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { ConfigService } from '../../core/services/config.service';
import { LoggerService } from '../../core/services/logger.service';
import { Procedimento } from '../models';
import { RiscaUtilitiesService } from './risca/risca-utilities/risca-utilities.service';

@Injectable({
  providedIn: 'root',
})
export class ProcedimentoService {
  private beUrl = '';

  /**
   * Variabile di gestione dei dati in sessione. Viene valorizzata dopo la prima chiamata
   * getProcedimenti() e poi viene fatto riferimento ad essa per non effettuare nuovamente la chiamata.
   */
  private _procedimenti: Procedimento[] = [];

  constructor(
    private _http: HttpClient,
    config: ConfigService,
    private _logger: LoggerService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    this.beUrl = config.getBEUrl();
  }

  /**
   * Getter per procediemnti
   * @param forceDownload boolean che forza lo scarico dei dati della funzione anche se ci sono dati nel servizio. Default: false;
   * @returns Array di tipi soggetto
   */
  getProcedimenti(forceDownload = false) {
    // Recupero il dato dal servizio
    const procedimenti = this.procedimenti;
    // Effettuo un check per lo scarico dati
    const useLocal = procedimenti?.length > 0 && !forceDownload;

    // Verifico se esiste il dato nel servizio
    if (useLocal) {
      // Li ho già, quindi li ritorno
      return this._riscaUtilities.responseWithDelay(procedimenti);
      // #
    } else {
      // Creo l'url per la chiamata
      const url = `${this.beUrl}/procedimenti`;
      // Tramite servizio scarico i dati
      return this._http.get<Procedimento[]>(url).pipe(
        tap((res: Procedimento[]) => {
          // Assegno localmente il risultato
          this._procedimenti = clone(res);
        })
      );
    }
  }

  // restituisce il procedimento sulla base del codice
  getProcedimentoByCode(code): Observable<Procedimento> {
    return this._http
      .get<Procedimento[]>(`${this.beUrl}/procedimenti/codice/${code}`)
      .pipe(map((procedimenti) => procedimenti[0]));
  }

  // restituisce tutti i procedimenti di un certo ambito by id
  getProcedimentiAmbito(idAmbito: number): Observable<Procedimento[]> {
    return this._http.get<Procedimento[]>(
      `${this.beUrl}/procedimenti/id-ambito/${idAmbito}`
    );
  }

  /**
   * Getter per _procedimenti
   * @returns Array di Procedimento.
   */
  get procedimenti(): Procedimento[] {
    // Creo una copia del dato
    return cloneDeep(this._procedimenti);
  }
}
