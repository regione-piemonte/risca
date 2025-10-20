import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { clone } from 'lodash';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ConfigService } from '../../core/services/config.service';
import { TipoInvio } from '../models/contatti/tipo-invio.model';
import { HttpUtilitiesService } from './http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from './risca/risca-utilities/risca-utilities.service';

@Injectable({ providedIn: 'root' })
export class ContattiService extends HttpUtilitiesService {
  /** String costante che definisce il path per: /tipi-invio. */
  private PATH_TIPI_INVIO = '/tipi-invio';

  /** TipoInvio[] con le informazioni salvate in sessione. */
  private _tipiInvio: TipoInvio[] = [];

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
   * Getter per i tipi invio.
   * @returns Observable<TipoInvio[]>
   */
  getTipiInvio(): Observable<TipoInvio[]> {
    // Verifico se esistono dati già scaricati
    if (this._tipiInvio?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._tipiInvio);
    }

    // Definisco l'url
    const url = this.appUrl(this.PATH_TIPI_INVIO);
    // Lancio la chiamata _http
    return this._http.get<TipoInvio[]>(url).pipe(
      tap((tipiInvio: TipoInvio[]) => {
        // Assegno localmente il risultato
        this._tipiInvio = clone(tipiInvio);
      })
    );
  }

  /**
   * ##################
   * FUNZIONI DI COMODO
   * ##################
   */

  /**
   * Funzione che estrae e imposta come valore di default un tipo invio all'interno di un form.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param listaTipiInvio TipoInvio[] lista dei tipi invio scaricati
   * @param idTipoInvio number id tipo invio da settare
   * @returns
   */
  setTipoContattoDefault(
    f: FormGroup,
    fcn: string,
    listaTipiInvio: TipoInvio[],
    idTipoInvio: number
  ) {
    // Verifico che esista l'input
    if (!f || !fcn || !listaTipiInvio || !idTipoInvio) {
      return;
    }

    // Lancio una sanitizzazione degli oggetti qualora ci siano già dei dafeult
    this._riscaUtilities.sanitizeRiscaSelectedFlag(listaTipiInvio);

    // Vado a recuperare l'indice dell'oggetto
    const i = listaTipiInvio.findIndex(
      (s: TipoInvio) => s.id_tipo_invio === idTipoInvio
    );
    // Verifico sia stato trovato l'oggetto
    if (i !== -1) {
      // Recupero l'oggetto
      const nazione = listaTipiInvio[i] as any;
      // Imposto il flag di select
      nazione.__selected = true;
      // Imposto l'oggetto come valore
      this._riscaUtilities.setFormValue(f, fcn, nazione);
    }
  }
}
