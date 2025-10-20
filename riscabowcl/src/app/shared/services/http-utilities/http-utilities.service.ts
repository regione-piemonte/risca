import { HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import {
  DynamicObjAny,
  IRiscaServerError,
  IRiscaTablePagination,
  IRulesQueryParams,
  RiscaServerError,
} from 'src/app/shared/utilities';
import { ConfigService } from '../../../core/services/config.service';
import { RiscaNotifyCodes } from '../../utilities/enums/risca-notify-codes.enums';
import { RiscaUtilitiesService } from '../risca/risca-utilities/risca-utilities.service';
import { RISCA_NO_SPINNER_HEADER } from '../../consts/utilities.consts';

/**
 * Servizio di supporto per altri servizi applicativi.
 */
@Injectable({ providedIn: 'root' })
export class HttpUtilitiesService {
  /**
   * Costruttore.
   */
  constructor(
    protected _config: ConfigService,
    protected _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * #############################
   * SUPPORTO PER LE CHIAMATE HTTP
   * #############################
   */

  /**
   * Funzione che converte un oggetto in input in un oggetto HttpParams.
   * Le proprietà dell'oggetto saranno le chiavi, mentre il valore il valore.
   * @param objParam DynamicObjAny contenente i parametri da convertire.
   * @param rules IRulesQueryParams che definisce le regole di gestione per la gestione dei dati del query param.
   * @returns HttpParams risultato della conversione.
   */
  createQueryParams(
    objParam: DynamicObjAny,
    rules?: IRulesQueryParams
  ): HttpParams {
    // Ritorno l'oggetto generato dalla creazione dei query params
    return this._riscaUtilities.createQueryParams(objParam, rules);
  }

  /**
   * Funzione che genera il path url per le chiamate ai servizi di backend.
   * @param paths Spread syntax che permette di passare un numero variabile di path che comporranno l'url.
   * @returns string con la url compilata per la chiamata ai servizi di BE.
   */
  appUrl(...paths: any[]): string {
    // Ritorno l'oggetto generato dalla creazione dei query params
    return this._config.appUrl(...paths);
  }

  /**
   * Funzione che genera gli HttpParams da passare all'API.
   * Nell'oggetto di ritorno, vengono definiti i parametri di paginazione per la chiamata e i normali parametri per l'API.
   * @param paginazione IRiscaTablePagination con la definizione dei dati per la paginazione.
   * @returns HttpParams con i parametri generati insieme alla paginazione.
   */
  createPagingParams(
    paginazione: IRiscaTablePagination,
    params?: DynamicObjAny
  ): HttpParams {
    // Ritorno l'oggetto generato dalla funzione del servizio
    return this._riscaUtilities.createPagingParams(paginazione, params);
  }

  /**
   * Funzione che gestisce l'header per non attivare lo spinner dalla struttura interceptor.
   * @param headers HttpHeaders con gli header per la chiamata.
   * @returns HttpHeaders con l'header per non avere lo spinner. Se non è stato passato un oggetto HttpHeaders in input, verrà creato nuovo.
   */
  headerNoSpinner(headers?: HttpHeaders): HttpHeaders {
    // Verifico l'input
    headers = headers ?? new HttpHeaders();

    // Recupero la chiave per bloccare lo spinner risca sulla chiamata
    const noSpinHeader = RISCA_NO_SPINNER_HEADER;
    // L'oggetto HttpHeaders è IMMUTABILE! quindi bisogna riassegnare l'header una volta fatto il set
    headers = headers.set(noSpinHeader, noSpinHeader);

    // Ritorno l'headers aggiornato
    return headers;
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione di supporto che genera un errore con ritorno di un RiscaServerError.
   * @param code RiscaNotifyCodes con il codice per la messaggistica dell'errore.
   * @param path string con l'url path del servizio che ha generato errore.
   * @param detail string con informazioni descrittive extra per l'errore generato.
   * @returns RiscaServerError con un errore RiscaServerError.
   */
  feError(
    code: RiscaNotifyCodes,
    path: string,
    detail?: string
  ): RiscaServerError {
    // Verifico l'input e definisco un default
    code = code ?? RiscaNotifyCodes.E005;
    path = path ?? '[throwFEError - path not defined]';
    detail = detail ?? 'generic error';

    // La lista vuota definisco le informazioni per la gestione dell'errore
    const title = `[RISCA_FE] ${path} ${detail}`;
    const error: IRiscaServerError = { code, title };

    // Genero e ritorno un errore
    const e = new RiscaServerError(error);
    return e;
  }
}
