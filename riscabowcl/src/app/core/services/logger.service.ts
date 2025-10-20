import { Injectable } from '@angular/core';
import {
  GuardsCheckEnd,
  GuardsCheckStart,
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
} from '@angular/router';
import * as moment from 'moment';
import { RiscaInfoLevels } from '../../shared/utilities/enums/utilities.enums';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root',
})
export class LoggerService {
  /** Costanti come classe di stile per il log di livello: DEBUG. */
  private CSS_DEBUG = 'font-size: 14px; color: grey;';
  /** Costanti come classe di stile per il log di livello: ERROR. */
  private CSS_ERROR = 'font-size: 14px; color: red;';
  /** Costanti come classe di stile per il log di livello: INFO. */
  private CSS_SUCCESS = 'font-size: 14px; color: green;';
  /** Costanti come classe di stile per il log di livello: LOG. */
  private CSS_LOG = 'font-size: 14px; color: #80bfff;';
  /** Costanti come classe di stile per il log di livello: WARNING. */
  private CSS_WARNING = 'font-size: 14px; color: orange;';
  /** Costanti come classe di stile per il log di livello: HTTP_REQ. */
  private CSS_HTTP_REQ = 'font-size: 14px; color: #e8651a;';
  /** Costanti come classe di stile per il log di livello: CSS_DEV_TRACK. */
  private CSS_DEV_TRACK = 'font-size: 14px; color: #ff409c;';
  /** Costanti come classe di stile per il log di livello: DATI_TECNICI. */
  private CSS_DATI_TECNICI = 'font-size: 14px; color: #8f16cc;';
  /** Costanti come classe di stile per il log di livello: ROUTE_EVENT. */
  private CSS_ROUTE_EVENT = 'font-size: 14px; color: #17b00c;';

  private isDebugMode: boolean;

  constructor(configService: ConfigService) {
    this.isDebugMode = configService.isDebugMode();
  }

  /**
   * Funzione di logging delle informazioni in console. Livello: debug.
   * @param title string titolo del log.
   * @param body any contenente il body del log.
   */
  debug(title: string, body: any) {
    this.print(RiscaInfoLevels.debug, title, body);
  }

  /**
   * Funzione di logging delle informazioni in console. Livello: error.
   * @param title string titolo del log.
   * @param body any contenente il body del log.
   */
  error(title: string, body: any) {
    this.print(RiscaInfoLevels.error, title, body);
  }

  /**
   * Funzione di logging delle informazioni in console. Livello: info.
   * @param title string titolo del log.
   * @param body any contenente il body del log.
   */
  success(title: string, body: any) {
    this.print(RiscaInfoLevels.success, title, body);
  }

  /**
   * Funzione di logging delle informazioni in console. Livello: log.
   * @param title string titolo del log.
   * @param body any contenente il body del log.
   */
  log(title: string, body: any) {
    this.print(RiscaInfoLevels.log, title, body);
  }

  /**
   * Funzione di logging delle informazioni in console. Livello: warning.
   * @param title string titolo del log.
   * @param body any contenente il body del log.
   */
  warning(title: string, body: any) {
    this.print(RiscaInfoLevels.warning, title, body);
  }

  /**
   * Funzione per il logging delle informazioni in console.
   * @param type RiscaInfoLevels.debug | RiscaInfoLevels.error | RiscaInfoLevels.success | RiscaInfoLevels.log | RiscaInfoLevels.warning definisce il tipo di log da stampare.
   * @param title string titolo del log.
   * @param body any contenente il body del log.
   */
  print(
    type:
      | RiscaInfoLevels.debug
      | RiscaInfoLevels.error
      | RiscaInfoLevels.success
      | RiscaInfoLevels.log
      | RiscaInfoLevels.warning,
    title: string,
    body: any
  ) {
    // Verifico ci siano i parametri
    const checkType = !type;
    const checkTitle = title === undefined;
    const checkNotDebug = !this.isDebugMode;
    const checkIsErrWarn =
      type != RiscaInfoLevels.error && type != RiscaInfoLevels.warning;
    // Controllo le condizioni
    if (checkType || checkTitle || (checkNotDebug && checkIsErrWarn)) {
      return;
    }

    // Definisco il messaggio di test
    const logTitle = this.logTitle(type, title);

    // Verifico il tipo di log
    switch (type) {
      case RiscaInfoLevels.debug:
        // Stampo la testata del gruppo
        console.group(logTitle, this.CSS_DEBUG);
        // Stampo il corpo del messaggio
        console.log(body);
        break;
      case RiscaInfoLevels.error:
        // Stampo la testata del gruppo
        console.group(logTitle, this.CSS_ERROR);
        // Stampo il corpo del messaggio
        console.error(body);
        break;
      case RiscaInfoLevels.success:
        // Stampo la testata del gruppo
        console.group(logTitle, this.CSS_SUCCESS);
        // Stampo il corpo del messaggio
        console.info(body);
        break;
      case RiscaInfoLevels.log:
        // Stampo la testata del gruppo
        console.group(logTitle, this.CSS_LOG);
        // Stampo il corpo del messaggio
        console.log(body);
        break;
      case RiscaInfoLevels.warning:
        // Stampo la testata del gruppo
        console.group(logTitle, this.CSS_WARNING);
        // Stampo il corpo del messaggio
        console.warn(body);
        break;
    }

    // Chiudo il gruppo
    console.groupEnd();
  }

  /**
   * Funzione per il logging delle informazioni in console.
   * La funzione è pensata specificatamente per le http request
   * @param req any contenente i dati per la request.
   */
  httpRequest(req: any) {
    // Verifico se non sono in modalità di debug
    const checkNotDebug = !this.isDebugMode;
    // Controllo la possibilità di log
    if (checkNotDebug) {
      // Non sono in debug, non scrivo log
      return;
    }

    // Definisco un timestamp
    const timestamp = this.timestamp;
    // Definisco l'url della chiamata
    const url = req?.urlWithParams || req?.url;
    // Definisco il messaggio di test
    const title = this.logTitle(`[Http Request]`, timestamp, url);

    // Stampo la testata del gruppo
    console.group(title, this.CSS_HTTP_REQ);
    // Stampo il corpo del messaggio
    console.debug(req);

    // Chiudo il gruppo
    console.groupEnd();
  }

  /**
   * Funzione per il logging delle informazioni in console.
   * La funzione è pensata specificatamente per la tracciatura di sviluppo.
   * @param data any contenente i dati per la request.
   */
  devTracker(data: any) {
    // Verifico se non sono in modalità di debug
    const checkNotDebug = !this.isDebugMode;
    // Controllo la possibilità di log
    if (checkNotDebug) {
      // Non sono in debug, non scrivo log
      return;
    }

    // Definisco un timestamp
    const timestamp = this.timestamp;
    // Definisco i dati da tracciare
    const track = data;
    // Definisco il messaggio di test
    const title = this.logTitle(`[Track event]`, timestamp, track);

    // Stampo la testata del gruppo
    console.group(title, this.CSS_DEV_TRACK);
    // Stampo il corpo del messaggio
    console.debug(data);

    // Chiudo il gruppo
    console.groupEnd();
  }

  /**
   * Funzione di logging delle informazioni in console. Livello: dati tecnici.
   * @param title string titolo del log.
   * @param body any contenente il body del log.
   * @param noData boolean come flag per effettuare il log di una semplice descrizione.
   */
  datiTecnici(title: string, body: any, noData = false) {
    // Verifico se non sono in modalità di debug
    const checkNotDebug = !this.isDebugMode;
    // Controllo la possibilità di log
    if (checkNotDebug) {
      // Non sono in debug, non scrivo log
      return;
    }

    // Definisco la testata del log
    const testata = `[DATI TECNICI]`;
    const descrizione = title;
    // Definisco il messaggio di test
    const logTitle = this.logTitle(testata, descrizione);

    // Verifico il flag di gestione dati
    if (noData) {
      // Stampo il corpo del messaggio
      console.info(logTitle, this.CSS_DATI_TECNICI);
      // #
    } else {
      // Stampo la testata del gruppo
      console.group(logTitle, this.CSS_DATI_TECNICI);
      // Stampo il corpo del messaggio
      console.info(body);
      // Chiudo il gruppo
      console.groupEnd();
    }
  }

  /**
   * Funzione per il logging delle informazioni in console.
   * La funzione è pensata specificatamente per gli eventi di cambio di route.
   * @param event any con l'evento di cambio di route.
   */
  routeEvent(event: any) {
    // Verifico se non sono in modalità di debug
    const checkNotDebug = !this.isDebugMode;
    // Controllo la possibilità di log
    if (checkNotDebug) {
      // Non sono in debug, non scrivo log
      return;
    }

    // Definisco un timestamp
    const timestamp = this.timestamp;
    // Definisco la label a seconda del tipo di evento generato dalla navigazione
    let eventType: string = 'event';

    // Gestisco tutte le casistiche
    if (event instanceof NavigationStart) {
      // Definisco l'evento come label
      eventType = 'NavigationStart';
      // #
    } else if (event instanceof NavigationEnd) {
      // Definisco l'evento come label
      eventType = 'NavigationEnd';
      // #
    } else if (event instanceof NavigationError) {
      // Definisco l'evento come label
      eventType = 'NavigationError';
      // #
    } else if (event instanceof NavigationCancel) {
      // Definisco l'evento come label
      eventType = 'NavigationCancel';
      // #
    } else if (event instanceof GuardsCheckStart) {
      // Definisco l'evento come label
      eventType = 'GuardsCheckStart';
      // #
    } else if (event instanceof GuardsCheckEnd) {
      // Definisco l'evento come label
      eventType = 'GuardsCheckEnd';
      // #
    }

    // Definisco il messaggio di test
    const title = this.logTitle(`[Route event]`, timestamp, eventType);

    // Stampo la testata del gruppo
    console.group(title, this.CSS_ROUTE_EVENT);
    // Stampo il corpo del messaggio
    console.debug(event);

    // Chiudo il gruppo
    console.groupEnd();
  }

  /**
   * ##################
   * FUNZIONI DI COMODO
   * ##################
   */

  /**
   * Funzione per la generazione del title per i log.
   * @param lvl2 string con la descrizione per il "dominio di livello": 1.
   * @param lvl1 string con la descrizione per il "dominio di livello": 2.
   * @param lvl3 string con la descrizione per il "dominio di livello": 3. Se non definito verrà impostato un timestamp.
   * @returns string con la concatenazione delle informazioni formando un titolo per i log.
   */
  private logTitle(lvl1: string, lvl2: string, lvl3?: string): string {
    // Verifico se esiste il livello 3
    lvl3 = lvl3 ? lvl3 : this.timestamp;
    // Definisco il messaggio di test
    const title = `%c${lvl1} | ${lvl2} | ${lvl3}`;
    // Ritorno il titolo formattato
    return title;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che ritorna una stringa con il timestamp del log.
   * @returns string con il timestamp del log, in formato: HH:mm:ss.SSSS - DD/MM/YYYY
   */
  get timestamp() {
    // Definisco il formato
    const tsFormat = `HH:mm:ss.SSSS - DD/MM/YYYY`;
    // Uso moment per generare il timestamp
    return moment().format(tsFormat);
  }
}
