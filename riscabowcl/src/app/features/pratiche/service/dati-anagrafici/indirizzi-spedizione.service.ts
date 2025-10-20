import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { cloneDeep, remove } from 'lodash';
import { forkJoin, Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { ConfigService } from '../../../../core/services/config.service';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { riscaServerMultiErrors } from '../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaServerError } from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import {
  IRisultatiVerifyIS,
  IRVISInvalidi,
} from '../../interfaces/indirizzi-spedizione/indirizzi-spedizione.interfaces';
import { VerificaIndirizzoSpedizione } from '../../types/indirizzi-spedizione/indirizzi-spedizione.types';

@Injectable({
  providedIn: 'root',
})
export class IndirizziSpedizioneService {
  /** Oggetto di costanti comuni all'applicazione. */
  private C_C = new CommonConsts()

  /** Costante per il path: /indirizzi-spedizione */
  private PATH_INDIRIZZI_SPEDIZIONE = '/indirizzi-spedizione';

  /**
   * Costruttore
   */
  constructor(
    private _config: ConfigService,
    private _http: HttpClient,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * ############################################
   * GESTIONE DEL SINGOLO INDIRIZZO DI SPEDIZIONE
   * ############################################
   */

  /**
   * Funzione di update per i dati di un indirizzo di spedizione.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo con i dati d'aggiornare.
   * @returns Observable<IndirizzoSpedizioneVo> con le informazioni aggiornate.
   */
  updateIndirizzoSpedizione(
    indirizzoSpedizione: IndirizzoSpedizioneVo
  ): Observable<IndirizzoSpedizioneVo> {
    // Creo l'url da richiamare
    const url = this._config.appUrl(this.PATH_INDIRIZZI_SPEDIZIONE);
    // Richiamo la PUT per l'aggiornamento
    return this._http.put<IndirizzoSpedizioneVo>(url, indirizzoSpedizione);
  }

  /**
   * Funzione di verifica per i dati di un indirizzo di spedizione.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo con i dati da verificare.
   * @returns Observable<IndirizzoSpedizioneVo> con l'oggetto mandato, che indica che la verifica è andata a buon fine.
   */
  verifyIndirizzoSpedizione(
    indirizzoSpedizione: IndirizzoSpedizioneVo
  ): Observable<IndirizzoSpedizioneVo> {
    // Creo l'url da richiamare
    const url = this._config.appUrl(this.PATH_INDIRIZZI_SPEDIZIONE);

    // Definisco la configurazione per la verifica
    const modalitaVerifica = 1;
    // Creo i query param per la richiesta della verifica
    const params = this._riscaUtilities.createQueryParams({ modalitaVerifica });

    // Richiamo la PUT per la verifica
    return this._http.put<IndirizzoSpedizioneVo>(url, indirizzoSpedizione, {
      params,
    });
  }

  /**
   * ######################################
   * GESTIONE MULTI INDIRIZZI DI SPEDIZIONE
   * ######################################
   */

  /**
   * Funzione di verifica per i dati di un indirizzo di spedizione.
   * Questa funzione ha una gestione specifica, poiché se viene generato un errore, questo non lancia un error, ma viene gestisto in maniera tale da restituire l'oggetto di errore compilato con le informazioni dell'indirizzo e la lista degli errori per la verifica.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo con i dati da verificare.
   * @returns Observable<VerificaIndirizzoSpedizione> con l'oggetto mandato, che indica che la verifica è andata a buon fine, oppure l'errore generato dal server (sempre nella success).
   */
  verifyIndirizzoSpedizioneMulti(
    indirizzoSpedizione: IndirizzoSpedizioneVo
  ): Observable<VerificaIndirizzoSpedizione> {
    // Creo l'url da richiamare
    const url = this._config.appUrl(this.PATH_INDIRIZZI_SPEDIZIONE);

    // Definisco la configurazione per la verifica
    const modalitaVerifica = 1;
    // Creo i query param per la richiesta della verifica
    const params = this._riscaUtilities.createQueryParams({ modalitaVerifica });

    // Richiamo la PUT per la verifica
    return this._http
      .put<IndirizzoSpedizioneVo>(url, indirizzoSpedizione, {
        params,
      })
      .pipe(
        catchError((error: RiscaServerError) => {
          // Verifico se l'errore generato è dovuto al check
          if (riscaServerMultiErrors(error)) {
            // Mi creo un oggetto per la gestione del dettaglio
            const detail = { indirizzoSpedizione }; // <== @@##@@DETTAGLIO_INDIRIZZO_SPEDIZIONE_ERRORE
            // Verifico se esiste la struttura dell'errore singolo
            if (!error.error) {
              // Genero la struttura per error
              error.error = {};
            }
            // Verifico se esiste la struttura per il dettaglio dell'errore singolo
            if (!error.error.detail) {
              // Genero la struttura per detail
              error.error.detail = {};
            }

            // Mergio le proprietà di detail
            error.error.detail = { ...error.error.detail, ...detail };
            // Ritorno l'oggetto di errore, come se fosse valido
            return of(error);
          }

          // Errore normale, devo passare il trowh
          return throwError(error);
        })
      );
  }

  /**
   * Funzione di verifica per i dati di un indirizzo di spedizione.
   * @param indirizziSpedizione Array di IndirizzoSpedizioneVo con i dati da verificare.
   * @returns Observable<VerificaIndirizzoSpedizione[]> con i risultati della verifica.
   */
  verifyIndirizziSpedizione(
    indirizziSpedizione: IndirizzoSpedizioneVo[]
  ): Observable<VerificaIndirizzoSpedizione[]> {
    // Verifico l'input
    if (!indirizziSpedizione || indirizziSpedizione.length === 0) {
      // Nessun dato, definisco un errore
      const e = new RiscaServerError({
        error: { code: RiscaNotifyCodes.E005 },
      });
      // Ritorno l'errore
      return throwError(e);
    }

    // Partendo dalla mappa, creo l'oggetto di request di verifica
    const listReq = this.generateReqVerifyIS(indirizziSpedizione);

    // Lancio la forkJoin delle richieste
    return forkJoin(listReq);
  }

  /**
   * Funzione di supporto che genera l'oggetto di request per la verifica degli indirizzi di spedizione.
   * @param indirizziSpedizione Array di IndirizzoSpedizioneVo con le informazioni per generare la request.
   * @returns Array di Observable<VerificaIndirizzoSpedizione> con la lista di chiamate da passare ad una forkJoin.
   */
  generateReqVerifyIS(
    indirizziSpedizione: IndirizzoSpedizioneVo[]
  ): Observable<VerificaIndirizzoSpedizione>[] {
    // Verifico l'input
    if (!indirizziSpedizione) {
      // Niente configurazione
      return undefined;
    }

    // Definisco l'oggetto per la request
    const request = indirizziSpedizione.map((is: IndirizzoSpedizioneVo) => {
      // Ritorno la chiamata al verifica indirizzo
      return this.verifyIndirizzoSpedizioneMulti(is);
    });

    // Ritorno l'oggetto di request compilato
    return request;
  }

  /**
   * Funzione di supporto che divide in due array distinti gli indirizzi di spedizione passati sotto la verifica.
   * @param indirizziVerify Array di VerificaIndirizzoSpedizione dalla quale estrarre i dati.
   * @returns IRisultatiVerifyIS con le informazioni riguardanti gli indirizzi.
   */
  risultatiVerifyIndirizziSpedizione(
    indirizziVerify: VerificaIndirizzoSpedizione[]
  ): IRisultatiVerifyIS {
    // Verifico l'input
    if (!indirizziVerify) {
      // Ritorno oggetto vuoto
      return { indirizziValidi: [], indirizziInvalidi: [] };
    }

    // Creo una copia per l'array e imposto la divisione (N.B.: Forzo la tipizzazione degli indirizzi validi, poiché saranno "puliti" dagli errori)
    let indirizziValidi: any[] = cloneDeep(indirizziVerify); // => as IndirizzoSpedizioneVo[];
    // Utilizzando la remove di lodash, tolgo gli errori dall'array di indirizzi validi e creo un array di errori
    const indirizziErrore: any[] = remove(
      indirizziValidi,
      (iv: VerificaIndirizzoSpedizione) => {
        // Creo una variabile d'appoggio, forzando la tipizzazione
        const e = iv as RiscaServerError;
        // Verifico se esiste la property "errors"
        return e.error !== undefined;
      }
    ); // => as RiscaServerError[];

    // Genero le informazioni per gli indirizzi invalidi
    const indirizziInvalidi: IRVISInvalidi[] = indirizziErrore.map(
      (error: RiscaServerError) => {
        // Per logiche applicative so che il dettaglio dell'indirizzo è definito, cerca: @@##@@DETTAGLIO_INDIRIZZO_SPEDIZIONE_ERRORE
        const indirizzoSpedizione = error?.error?.detail?.indirizzoSpedizione;
        // Ritorno l'oggetto formattato
        return { error, indirizzoSpedizione };
      }
    );

    // Ritorno le informazioni al chiamante
    return { indirizziValidi, indirizziInvalidi };
  }
}
