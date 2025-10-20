import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, Observable, of } from 'rxjs';
import { delay, map, tap } from 'rxjs/operators';
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
import {
  AccertamentoVo,
  IAccertamentoVo,
} from '../../../../core/commons/vo/accertamento-vo';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IAccertamentiInitReq,
  IAccertamentiInitRes,
} from '../../components/dati-contabili/accertamenti/utilities/accertamenti.interfaces';
import {
  ITipoAccertamentoVo,
  TipoAccertamentoVo,
} from './../../../../core/commons/vo/tipo-accertamento-vo';
import { AccertamentiConsts } from './../../components/dati-contabili/accertamenti/utilities/accertamenti.consts';

export interface IRicercaDatiContabili {
  offset: number;
  limit: number;
}

@Injectable({
  providedIn: 'root',
})
export class AccertamentiService extends HttpUtilitiesService {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  ACC_C = AccertamentiConsts;

  /** Costante per il path: /tipi-accertamento. */
  private PATH_TIPI_ACCERTAMENTI = '/tipi-accertamento';
  /** Costante per il path: /attivita-stato-deb. */
  private PATH_ATTIVITA_STATO_DEB =
    '/attivita-stato-deb?tipoAttivita=accertamenti';
  /** Costante per il path: /stati-debitori. */
  private PATH_STATI_DEB_COLL = '/stati-debitori/idRimborso';

  /** Costante per il path: /accertamenti. */
  private PATH_ACCERTAMENTI = '/accertamenti';
  /** Costante per il path: /accertamenti/idAccertamento. */
  private PATH_ACCERTAMENTI_DELETE = `${this.PATH_ACCERTAMENTI}/idAccertamento`;

  /**
   * ###############################
   * Liste di appoggio per le select
   * ###############################
   */

  /** Lista di appoggio per tipi attività degli accertamenti. */
  private _tipiAttivitaAccertamento: TipoAccertamentoVo[] = [];
  /** Lista di appoggio per tipi attività svolte dei rimborsi. */
  private _tipiAttivitaSvolteRimborso: AttivitaSDVo[] = [];

  constructor(
    config: ConfigService,
    private _http: HttpClient,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * Funzione che scarica le informazioni per la gestione degli accertamenti.
   * @returns Observable<IAccertamentiInitRes> con le informazioni per gli accertamenti.
   */
  getInfoAccertamenti(): Observable<IAccertamentiInitRes> {
    // Chiamata per i tipi rimborso
    const tipiAccertamenti = this.getTipiAccertamenti();
    // Chiamata per i tipi attività rimborso
    const tipiAttivita = this.getAttivitaStatoDebitorio();

    // Insieme delle request
    const attivitaReq: IAccertamentiInitReq = {
      tipiAccertamenti,
      tipiAttivita,
    };

    // Richiamo la funzione con un mini ritardo per gestire un comportamento inatteso con il caricamento dei dati
    return forkJoin(attivitaReq).pipe(delay(500));
  }

  /**
   * Ottiene la lista dei tipi di rimborso per la select nella pagina dei rimborsi.
   */
  getTipiAccertamenti(): Observable<TipoAccertamentoVo[]> {
    // Recupero il dato
    const tipiAttiAcc = this.tipiAttivitaAccertamento;
    // Se ho già gli elementi, li ritorno
    if (tipiAttiAcc?.length > 0) {
      // Ritorno il dato salvato in sessione con un delay
      return this._riscaUtilities.responseWithDelay(tipiAttiAcc);
    }

    // Definisco l'url
    const url = this.appUrl(this.PATH_TIPI_ACCERTAMENTI);
    // Effettuo la chiamata
    return this._http.get<ITipoAccertamentoVo[]>(url).pipe(
      map((result: ITipoAccertamentoVo[]) => {
        // RISCA-ISSUES-60: è stato richiesto il filtro per le voci così da ottenere solo gli oggetti con gestione manuale
        return result?.filter((r: ITipoAccertamentoVo) => {
          // Ritorno solo quelli con gestione manuale
          return r?.flg_manuale === 1;
          // #
        });
        // #
      }),
      map((result: ITipoAccertamentoVo[]) => {
        // Converto la risposta dal server
        return result?.map((tAR: ITipoAccertamentoVo) => {
          // Rimappo l'oggetto e lo ritorno
          return new TipoAccertamentoVo(tAR);
        });
        // #
      }),
      tap((result: TipoAccertamentoVo[]) => {
        // Salvo localmente l'informazione
        this._tipiAttivitaAccertamento = result;
      })
    );
  }

  /**
   * Ottiene la lista dei tipi di attività rimborso per la select nella pagina di inserimento dei rimborsi.
   */
  getAttivitaStatoDebitorio(): Observable<AttivitaSDVo[]> {
    // Recupero il dato
    const tipiAttSvolteRimb = this.tipiAttivitaSvolteRimborso;
    // Se ho già gli elementi, li ritorno
    if (tipiAttSvolteRimb?.length > 0) {
      // Ritorno il dato salvato in sessione con un delay
      return this._riscaUtilities.responseWithDelay(tipiAttSvolteRimb);
    }

    // Definisco l'url
    const url = this.appUrl(this.PATH_ATTIVITA_STATO_DEB);

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
    const url = this.appUrl(this.PATH_STATI_DEB_COLL, idRimborso);
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
   * Funzione di POST per l'inserimento di un nuovo accertamento.
   * @param accertamento AccertamentoVo con le informazioni dell'accertamento da salvare.
   * @returns Observable<AccertamentoVo> con il risultato della funzione di salvataggio.
   */
  postAccertamento(accertamento: AccertamentoVo): Observable<AccertamentoVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_ACCERTAMENTI);
    // Vado a convertire l'oggetto FE in BE
    const accertamentoBE: IAccertamentoVo = accertamento?.toServerFormat();

    // Effettuo la chiamata di salvataggio
    return this._http.post<IAccertamentoVo>(url, accertamentoBE).pipe(
      map((accertamentoSaved: IAccertamentoVo) => {
        // Vado a creare e ritornare un oggetto FE
        return new AccertamentoVo(accertamentoSaved);
        // #
      })
    );
  }

  /**
   * Funzione di PUT per l'inserimento di un nuovo accertamento.
   * @param accertamento AccertamentoVo con le informazioni dell'accertamento da salvare.
   * @returns Observable<AccertamentoVo> con il risultato della funzione di salvataggio.
   */
  putAccertamento(accertamento: AccertamentoVo): Observable<AccertamentoVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_ACCERTAMENTI);
    // Vado a convertire l'oggetto FE in BE
    const accertamentoBE: IAccertamentoVo = accertamento?.toServerFormat();

    // Effettuo la chiamata di salvataggio
    return this._http.put<IAccertamentoVo>(url, accertamentoBE).pipe(
      map((accertamentoSaved: IAccertamentoVo) => {
        // Vado a creare e ritornare un oggetto FE
        return new AccertamentoVo(accertamentoSaved);
        // #
      })
    );
  }

  /**
   * Funzione di DELETE per la cancellazione di un accertamento.
   * @param accertamento number con l'id dell'accertamento da cancellare.
   * @returns Observable<AccertamentoVo> con il risultato della funzione di cancellazione.
   */
  deleteAccertamento(idAccertamento: number): Observable<any> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_ACCERTAMENTI_DELETE, idAccertamento);
    // Effettuo la chiamata di salvataggio
    return this._http.delete<IAccertamentoVo>(url);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo per il recupero dei dati nel servizio: tipiAttivitaAccertamento.
   * @returns TipoAccertamentoVo[] con i dati in sessione.
   */
  get tipiAttivitaAccertamento(): TipoAccertamentoVo[] {
    // Creo una copia e ritorno le informazioni
    return [...this._tipiAttivitaAccertamento];
  }

  /**
   * Getter di comodo per il recupero dei dati nel servizio: tipiAttivitaSvolteRimborso.
   * @returns AttivitaSDVo[] con i dati in sessione.
   */
  get tipiAttivitaSvolteRimborso(): AttivitaSDVo[] {
    // Creo una copia e ritorno le informazioni
    return [...this._tipiAttivitaSvolteRimborso];
  }
}
