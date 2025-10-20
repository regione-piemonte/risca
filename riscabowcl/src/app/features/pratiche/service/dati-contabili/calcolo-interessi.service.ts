import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ConfigService } from 'src/app/core/services/config.service';
import { convertNgbDateStructToServerDate } from '../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';

export interface ICalcoloInteressiReq {
  canone_dovuto: number;
  data_scadenza: NgbDateStruct;
  data_versamento: NgbDateStruct;
}

export class CalcoloInteressiReq {
  importo: number;
  dataScadenza: string;
  dataVersamento: string;

  constructor(request: ICalcoloInteressiReq) {
    this.importo = request.canone_dovuto;
    this.dataScadenza = convertNgbDateStructToServerDate(request.data_scadenza);
    this.dataVersamento = convertNgbDateStructToServerDate(
      request.data_versamento
    );
  }
}

@Injectable({
  providedIn: 'root',
})
export class CalcoloInteressiService {
  /** Costante per il path: /calcolo-interessi */
  private PATH_CALCOLO_INTERESSI = '/calcolo-interessi';

  constructor(
    private _config: ConfigService,
    private _http: HttpClient,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * Esegue la chiamata per il calcolo degli interessi
   * @param request CalcoloInteressiReq con i dati per chiamare il servizio
   * @returns TODO non ne ho idea
   */
  calcoloInteressi(request: CalcoloInteressiReq): Observable<number> {
    // Estraggo i dati della request
    const { importo, dataScadenza, dataVersamento } = request;

    // Creo l'url da richiamare
    const url = this._config.appUrl(this.PATH_CALCOLO_INTERESSI);

    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      importo,
      dataScadenza,
      dataVersamento,
    });

    // Richiamo il servizio
    return this._http.get<number>(url, { params }).pipe(
      map((p: number) => {
        // Response
        return p;
      })
    );
  }
}
