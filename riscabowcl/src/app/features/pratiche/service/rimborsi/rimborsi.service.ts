import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { cloneDeep } from 'lodash';
import { Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import {
  AttivitaSDVo,
  IAttivitaSDVo,
} from 'src/app/core/commons/vo/attivita-sd-vo';
import {
  ISDCollegatiVo,
  SDCollegatiVo,
} from 'src/app/core/commons/vo/sd-collegati-vo';
import { ConfigService } from 'src/app/core/services/config.service';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { TipoRicercaRimborsoVo } from '../../../../core/commons/vo/tipo-ricerca-rimborso-vo';
import {
  ITipoRimborsoVo,
  TipoRimborsoVo,
} from '../../../../core/commons/vo/tipo-rimborso-vo';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RimborsiConsts } from '../../components/dati-contabili/rimborsi/utilities/rimborsi.consts';

export interface IRicercaDatiContabili {
  offset: number;
  limit: number;
}

@Injectable({ providedIn: 'root' })
export class RimborsiService extends HttpUtilitiesService {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  RMB_C = RimborsiConsts;

  /** Costante per il path: /tipi-rimborso. */
  private PATH_TIPI_RIMBORSI = '/tipi-rimborso';
  /** Costante per il path: /attivita_stato_deb. */
  private PATH_ATTIVITA_STATO_DEB = '/attivita-stato-deb?tipoAttivita=rimborsi';
  /** Costante per il path: /stati_debitori. */
  private PATH_STATI_DEB_COLL = '/stati-debitori/idRimborso';
  /** Costante per il path: /tipi-ricerca-rimborsi */
  private PATH_TIPI_RICERCA_RIMBORSI = '/tipi-ricerca-rimborsi';

  /**
   * ###############################
   * Liste di appoggio per le select
   * ###############################
   */

  /** Lista di appoggio per tipi attività dei rimborsi. */
  private _tipiAttivitaRimborso: TipoRimborsoVo[] = [];
  /** Lista di appoggio per tipi attività svolte dei rimborsi. */
  private _tipiAttivitaSvolteRimborso: AttivitaSDVo[] = [];
  /** TipoRicercaRimborsi[] con la lista di dati scaricati e da tenere in sessione. */
  private _tipiRicercaRimborsi: TipoRicercaRimborsoVo[];

  constructor(
    config: ConfigService,
    private _http: HttpClient,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * Ottiene la lista dei tipi di rimborso per la select nella pagina dei rimborsi.
   */
  getTipiRimborso(): Observable<TipoRimborsoVo[]> {
    // Se ho già gli elementi, li ritorno
    if (this._tipiAttivitaRimborso?.length > 0) {
      // Ritorno il dato salvato in sessione con un delay
      return this._riscaUtilities.responseWithDelay(this._tipiAttivitaRimborso);
    }

    // Definisco l'url
    const url = this._config.appUrl(this.PATH_TIPI_RIMBORSI);
    // Effettuo la chiamata
    return this._http.get<ITipoRimborsoVo[]>(url).pipe(
      map((result: ITipoRimborsoVo[]) => {
        // Converto la risposta dal server
        return result?.map((tAR: ITipoRimborsoVo) => new TipoRimborsoVo(tAR));
        // #
      }),
      tap((result: TipoRimborsoVo[]) => {
        // Salvo localmente l'informazione
        this._tipiAttivitaRimborso = result;
      })
    );
  }

  /**
   * Ottiene la lista dei tipi di attività rimborso per la select nella pagina di inserimento dei rimborsi.
   */
  getAttivitaStatoDebitorio(): Observable<AttivitaSDVo[]> {
    // Se ho già gli elementi, li ritorno
    if (this._tipiAttivitaSvolteRimborso?.length > 0) {
      // Ritorno il dato salvato in sessione con un delay
      return this._riscaUtilities.responseWithDelay(
        this._tipiAttivitaSvolteRimborso
      );
    }

    // Definisco l'url
    const url = this._config.appUrl(this.PATH_ATTIVITA_STATO_DEB);

    // Effettuo la chiamata
    return this._http.get<AttivitaSDVo[]>(url).pipe(
      map((result: IAttivitaSDVo[]) => {
        // Converto la risposta dal server
        return result?.map((tAR: IAttivitaSDVo) => new AttivitaSDVo(tAR));
        // #
      }),
      tap((result: AttivitaSDVo[]) => {
        // Salvo localmente l'informazione
        this._tipiAttivitaSvolteRimborso = result;
      })
    );
  }

  /**
   * Prende gli stati debitori collegati.
   * @param idRimborso id del rimborso da prendere per gli stati debitori collegati
   * @returns SDCollegatiVo[] con la lista degli stati debitori collegati
   */
  getStatiDebitoriCollegati(idRimborso: number): Observable<SDCollegatiVo[]> {
    if (idRimborso == null) {
      return of(null);
    }

    // Definisco l'url
    const url = this._config.appUrl(this.PATH_STATI_DEB_COLL, idRimborso);
    // Effettuo la chiamata
    return this._http.get<SDCollegatiVo[]>(url).pipe(
      map((result: ISDCollegatiVo[]) => {
        // Converto la risposta del server
        return result?.map((sdc: ISDCollegatiVo) => new SDCollegatiVo(sdc));
        // #
      })
    );
  }

  /**
   * Funzione che recupera i tipi ricerca rimborsi esistenti.
   * @returns Observable<TipoRicercaRimborsoVo[]> con la lista degli elementi tornati dal servizio.
   */
  getTipiRicercaRimborsi(): Observable<TipoRicercaRimborsoVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._tipiRicercaRimborsi?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._tipiRicercaRimborsi);
    }

    // Definisco l'url
    const url = this.appUrl(this.PATH_TIPI_RICERCA_RIMBORSI);
    // Richiamo il servizio
    return this._http.get<TipoRicercaRimborsoVo[]>(url).pipe(
      tap((trRimborsi: TipoRicercaRimborsoVo[]) => {
        // Assegno localmente il risultato
        this._tipiRicercaRimborsi = cloneDeep(trRimborsi);
      })
    );
  }
}
