import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IImmagineVo, ImmagineVo } from '../../../core/commons/vo/immagine-vo';
import { ConfigService } from '../../../core/services/config.service';
import { HttpUtilitiesService } from '../http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from './risca-utilities/risca-utilities.service';

/**
 * Servizio per la gestione delle risorse applicative RISCA.
 */
@Injectable({ providedIn: 'root' })
export class RiscaResourcesService extends HttpUtilitiesService {
  /** Costante per il path: /immagine */
  private PATH_IMMAGINE_PAGAMENTO = '/immagine';

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
   * #######################
   * GESTIONE DELLE IMMAGINI
   * #######################
   */

  /**
   * Get che ritorna i dati per visualizzare l'immagine di un pagamento.
   * @param idImmagine number con l'id dell'immagine da recuperare.
   * @returns Observable<ImmagineVo> con le informazioni per la costruzione dell'immagine del pagamento.
   */
  getImmagine(idImmagine: number): Observable<ImmagineVo> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_IMMAGINE_PAGAMENTO);
    // Creo i query param
    const params = this.createQueryParams({ idImmagine });

    // Richiamo il servizio
    return this._http.get<IImmagineVo>(url, { params }).pipe(
      map((immagine: IImmagineVo) => {
        // Converto l'oggetto
        return new ImmagineVo(immagine);
      })
    );
  }
}
