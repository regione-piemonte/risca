import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { join, replace } from 'lodash';
import { Observable, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { MessaggioUtenteVo } from '../../../core/commons/vo/messaggio-utente-vo';
import { ConfigService } from '../../../core/services/config.service';
import { LoggerService } from '../../../core/services/logger.service';
import {
  RiscaServerError,
  RiscaServerErrorInfo,
  TRiscaDataPlaceholder,
  TRiscaDataPlaceholders,
} from '../../utilities';
import { RiscaNotifyPlaceholders } from '../../utilities/consts/risca-notify-placeholders.consts';

/**
 * Struttura di supporto per velocizzare il recupero dei messaggi dell'utente.
 * L'oggetto avrà come proprietà il codice messaggio dell'oggetto MessaggioUtenteVo, e come valore l'oggetto MessaggioUtenteVo.
 */
interface RiscaMessaggiUtente {
  [key: string]: MessaggioUtenteVo;
}

/**
 * Servizio per la gestione dei messaggi per la visualizzazione all'utente.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaMessagesService {
  /** string costante che definisce il placeholder per i messaggi non trovati. */
  private MSG_PLACEHOLDER = 'Messaggio non presente sul DB';
  /** string costante che definisce il path per: /messaggi. */
  private PATH_MESSAGGI = '/messaggi';

  /** string che definisce l'url del back end. */
  private beUrl: string;
  /** RiscaMessaggiUtente contenente la mappatura dei messaggi per l'utente. */
  private _riscaMU: RiscaMessaggiUtente = {};

  /**
   * Costruttore.
   */
  constructor(
    configService: ConfigService,
    private _logger: LoggerService,
    private _http: HttpClient
  ) {
    // Configuro la variabile per l'url del back-end
    this.beUrl = configService.getBEUrl();
  }

  /**
   * ######################
   * CONVERTER DEL SERVIZIO
   * ######################
   */

  /**
   * Funzione di comodo che converte le informazioni dei dataPlaceholder in un'unica stringa.
   * @param dataReplace TRiscaDataPlaceholder con le informazioni da convertire.
   * @returns string con una stringa leggibile.
   */
  private convertTRiscaDataPlaceholderToString(
    dataReplace: TRiscaDataPlaceholder
  ): string {
    // Verifico l'input
    if (dataReplace === undefined) {
      // Ritorno un default
      return;
    }

    // Funzione di comodo
    const isStringOrNumber = (v: any): boolean => {
      // Varibili di comodo
      const isString = typeof v === 'string';
      const isNumber = typeof v === 'number';
      // Ritorno le condizioni
      return isString || isNumber;
    };

    // Verfico la tipologia dell'input
    if (isStringOrNumber(dataReplace)) {
      // E' una stringa o un numero, ritorno il suo valore
      return dataReplace.toString();
      // #
    }

    // Variabili di comodo
    let dataMsg = '';
    const isArray = Array.isArray(dataReplace);
    // Verifico la tipologia dell'input
    if (isArray) {
      // Effettuo un typing forzato per evitare errori
      const dataReplaceTyped = dataReplace as any[];
      // Effettuo una join delle informazioni
      dataMsg = join(dataReplaceTyped, ', ');
      // Ritorno la stringa
      return dataMsg;
      // #
    }

    // Fallback, non si dovrebbe mai arrivare qui
    return dataReplace as any;
  }

  /**
   * #####################
   * FUNZIONI DEL SERVIZIO
   * #####################
   */

  /**
   * Funzione che restituisce un messaggio dato il suo codice.
   * @param code string codice del messaggio.
   * @param fallbackPlaceholder string opzionale che definisce il messaggio da ritornare in caso in cui non venga trovato per il codice.
   * @returns string con il messaggio.
   */
  getMessage(code: string, fallbackPlaceholder?: string): string {
    // Tento di recuperare il messaggio
    const messaggio = this._riscaMU[code];

    // Verifico che sia stato trovato un messaggio
    if (!messaggio) {
      // Loggo un warning
      this._logger.warning('getMessage', {
        error: 'Codice messaggio non trovato',
        code,
      });
      // Ritorno un placeholder
      return fallbackPlaceholder || this.defaultMSGWithCode(code);
    }

    // Ritorno il messaggio
    return messaggio.des_testo_messaggio;
  }

  /**
   * Funzione che restituisce un messaggio dato il suo codice.
   * @param code string codice del messaggio.
   * @returns MessaggioUtenteVo con l'intera configurazione del messaggio.
   */
  getMessaggioUtenteVo(code: string): MessaggioUtenteVo {
    // Tento di recuperare il messaggio
    const messaggio = this._riscaMU[code];
    // Ritorno il messaggio
    return messaggio;
  }

  /**
   * Funzione che restituisce un messaggio con placeholder, tramite codice messaggio e i dati per la replace del placeholder.
   * Le chiavi di placeholder verranno recuperate per stesso codice messaggio.
   * ATTENZIONE: gli array codesPlaceholder e dataReplace verranno gestite utilizzando gli indici posizionali di codesPlaceholder. Quindi il placeholder alla posizione 0, verra sostituito dai dati alla posizione 0.
   * @param code string codice del messaggio.
   * @param dataReplace TRiscaDataPlaceholders con i dati da sostiture per i placeholder.
   * @param fallbackPlaceholder string opzionale che definisce il messaggio da ritornare in caso in cui non venga trovato per il codice.
   * @returns string con il messaggio.
   */
  getMessageWithPlacholderByCode(
    code: string,
    dataReplace: TRiscaDataPlaceholders,
    fallbackPlaceholder?: string
  ): string {
    // Recupero le chiavi di placeholder
    const cP = RiscaNotifyPlaceholders[code];
    // Variabili di comodo
    const c = code;
    const dR = dataReplace;
    const fPh = fallbackPlaceholder;
    // Ritorno il messaggio con placeholder
    return this.getMessageWithPlacholder(c, cP, dR, fPh);
  }

  /**
   * Funzione che restituisce un messaggio con placeholder, tramite codice messaggio e i dati per la replace del placeholder.
   * ATTENZIONE: gli array codesPlaceholder e dataReplace verranno gestite utilizzando gli indici posizionali di codesPlaceholder. Quindi il placeholder alla posizione 0, verra sostituito dai dati alla posizione 0.
   * @param code string codice del messaggio.
   * @param codesPlaceholders Array di string con i codici dei placeholder da sostituire nel messaggio.
   * @param dataReplace TRiscaDataPlaceholders con i dati da sostiture per i placeholder.
   * @param fallbackPlaceholder string opzionale che definisce il messaggio da ritornare in caso in cui non venga trovato per il codice.
   * @returns string con il messaggio.
   */
  getMessageWithPlacholder(
    code: string,
    codesPlaceholders: string[],
    dataReplace: TRiscaDataPlaceholders,
    fallbackPlaceholder?: string
  ): string {
    // Recupero il testo del messaggio
    let messaggio = this.getMessage(code, fallbackPlaceholder);

    // Verifico che esistano le strutture per il placeholding
    if (codesPlaceholders && dataReplace) {
      // Ciclo la lista dei placeholder
      for (let i = 0; i < codesPlaceholders.length; i++) {
        // Recupero il placholder
        const placeholder = codesPlaceholders[i];
        // Recupero e converto il dato per la sostituzione
        const dR = dataReplace[i];
        const dataPh = this.convertTRiscaDataPlaceholderToString(dR);

        // Verifico che sia stata effettuata la conversione
        if (dataPh !== undefined) {
          // Effettuo il raplace del placeholder con il dato
          messaggio = replace(messaggio, placeholder, dataPh);
          // #
        }
      }
    }

    // Ritorno il messaggio
    return messaggio;
  }

  /**
   * #############################
   * FUNZIONI INTERNE DEL SERVIZIO
   * #############################
   */

  /**
   * Funzione di supporto che genera un messaggio d'errore per un codice messaggio non trovato.
   * Dalla label di default viene aggiunto il dettaglio di quale codice non è stato trovato.
   * @param code string con il codice non trovato.
   */
  private defaultMSGWithCode(code: string) {
    // Compongo e ritorno la stringa con il messaggio d'errore
    return `${this.MSG_PLACEHOLDER} - CODICE: @${code}`;
  }

  /**
   * Funzione che effettua il convert tra un array MessaggioUtenteVo ad un oggetto RiscaMessaggiUtente.
   * @param m Array di MessaggioUtenteVo da convertire.
   * @returns RiscaMessaggiUtente convertito.
   */
  private convertMessaggiUtenteVoToRiscaMessaggiUtente(
    m: MessaggioUtenteVo[]
  ): RiscaMessaggiUtente {
    // Verifico l'input
    if (!m) {
      return {};
    }

    // Creo un contenitore dei dati
    const rmc = {};
    // Effettuo una conversione dell'array all'oggetto
    m.forEach((o) => {
      // Definisco come proprietà il codice del messaggio
      const key = o.cod_messaggio.trim();
      // Assegno la contenitore la coppia key e oggetto
      rmc[key] = o;
    });

    // Ritorno l'oggetto
    return rmc;
  }

  /**
   * #########################
   * FUNZIONI CON CHIAMATE API
   * #########################
   */

  /**
   * Funzione che recupera dal server il messaggio da utilizzare all'interno dell'applicazione.
   * @param code string che definisce il codice del messaggio da visualizzare.
   * @returns Observable<MessaggioUtenteVo> contenente tutte le informazioni del messaggio scaricato.
   */
  getMessageRemote(code: string): Observable<MessaggioUtenteVo> {
    // Verifico l'input
    if (!code) {
      // Loggo l'errore
      this._logger.error('getMessageRemote', { code });

      // Creo un oggetto custom per la gestione delle informazioni dell'errore
      const i = new RiscaServerErrorInfo({
        title: 'getMessageRemote',
        detail: { code },
      });
      // Creo un d'errore come quello del server
      const e = new RiscaServerError({ error: i });
      // Ritorno un errore
      return throwError(e);
    }

    // Loggo l'azione
    this._logger.log('getMessageRemote', { code });

    // Creo l'url per la chiamata al servizio
    const url = `${this.beUrl}${this.PATH_MESSAGGI}/${code}`;
    // Lancio la chiamata per il recupero dati
    return this._http.get<MessaggioUtenteVo>(url).pipe(
      tap((r: MessaggioUtenteVo) => {
        // Loggo il risultato
        this._logger.success('getMessageRemote', r);
      })
    );
  }

  /**
   * Funzione che recupera dal server tutti i messaggi da utilizzare all'interno dell'applicazione.
   * @returns Observable<MessaggioUtenteVo[]> contenente tutte le informazioni dei messaggi dell'applicazione.
   */
  getMessagesRemote(): Observable<MessaggioUtenteVo[]> {
    // Creo l'url per la chiamata al servizio
    const url = `${this.beUrl}${this.PATH_MESSAGGI}`;
    // Lancio la chiamata per il recupero dati
    return this._http.get<MessaggioUtenteVo[]>(url).pipe(
      tap((r: MessaggioUtenteVo[]) => {
        // Aggiorno locamente le configurazioni
        this._riscaMU = this.convertMessaggiUtenteVoToRiscaMessaggiUtente(r);
      })
    );
  }
}
