import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { MessaggioUtenteVo } from '../../../core/commons/vo/messaggio-utente-vo';
import { LoggerService } from '../../../core/services/logger.service';
import {
  RiscaAlertConfigs,
  RiscaServerError,
  TRiscaAlertType,
} from '../../utilities';
import { RiscaNotifyCodes } from '../../utilities/enums/risca-notify-codes.enums';
import { RiscaCodTMStiliAlert } from '../../utilities/enums/risca/risca-alert/risca-alert.enums';
import { RiscaInfoLevels } from '../../utilities/enums/utilities.enums';
import {
  IAlertConfigsChecks,
  IAlertConfigsFromCode,
} from '../../utilities/interfaces/risca/risca-alert/risca-alert.interface';
import { RiscaMessagesService } from './risca-messages.service';
import { RiscaUtilitiesService } from './risca-utilities/risca-utilities.service';

/**
 * Servizio di utility con funzionalità di gestione per gli alert dell'applicazione.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaAlertService {
  /** Subject che propaga la configurazione di un alert a chi è in ascolto.. */
  onAlertEmitted$ = new Subject<RiscaAlertConfigs>();

  /**
   * Costruttore.
   */
  constructor(
    private _logger: LoggerService,
    private _riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * Funzione che gestisce il prompt dei messaggi.
   * Se i parametri non sono definiti, verrà effettuato un reset.
   * @param c RiscaAlertConfigs che definisce l'oggetto di configrazione per risca-alert.
   * @param messaggi Array di string contenente i messaggi da visualizzare.
   * @param tipo TRiscaAlertType che definisce la tipologia di alert da visualizzare.
   * @return RiscaAlertConfigs con l'oggetto aggiornato.
   */
  aggiornaAlertConfigs(
    c: RiscaAlertConfigs,
    messaggi?: string[],
    tipo?: TRiscaAlertType
  ): RiscaAlertConfigs {
    // Verifico l'input
    c = c ?? new RiscaAlertConfigs();
    
    // Se non esistono messaggi resetto le impostazioni
    if (!messaggi) {
      // Resetto la configurazione
      c.messages = [];
      c.type = RiscaInfoLevels.none;
      c.persistentMessage = false;

      // Ritorno l'oggetto
      return c;
    }

    // Verifico che tipologia di alert visualizzare
    switch (tipo) {
      case RiscaInfoLevels.danger:
      case RiscaInfoLevels.info:
      case RiscaInfoLevels.warning:
        c.persistentMessage = true;
        break;
      case RiscaInfoLevels.success:
        c.persistentMessage = false;
        break;
      default:
        c.persistentMessage = true;
    }

    // Aggiorno la configurazione
    c.messages = messaggi;
    c.type = tipo || RiscaInfoLevels.none;

    // Ritorno l'oggetto
    return c;
  }

  /**
   * Funzione di comodo che partendo da un errore server, genera uno specifico oggetto di alert gestendo la tipologia/colore dell'alert.
   * @param error RiscaServerError che definisce le informazioni dell'errore generato dal server.
   */
  createAlertFromServerError(error: RiscaServerError): RiscaAlertConfigs {
    // Verifico esista l'oggetto
    if (!error) {
      // Nessuna configurazione
      return undefined;
    }

    // Estraggo dall'oggetto l'errore del server
    const code = error?.error?.code;
    // Ritorno la configurazione dell'alert passando il codice
    return this.createAlertFromMsgCode({ code });
  }

  /**
   * Funzione di comodo che partendo da un messaggio d'errore, genera uno specifico oggetto di alert gestendo la tipologia/colore dell'alert.
   * @param code IAlertConfigsFromCode che definisce le configurazioni per la generazione dell'alert.
   */
  createAlertFromMsgCode(configs: IAlertConfigsFromCode): RiscaAlertConfigs {
    // Verifico il codice
    if (!configs || !configs.code) {
      // Niente codice, niente configurazione
      return undefined;
    }

    // Estraggo le informazioni per la configurazione dell'alert
    const { code } = configs || {};

    // Recupero dal servizio l'oggetto intero che identifica un messaggio
    const m = this._riscaMessages.getMessaggioUtenteVo(code);
    // Verifico esista una configurazione
    if (!m) {
      // Codice non trovato, configurazione non definita
      return undefined;
    }

    // Creo l'oggetto per l'alert da ritornare
    const alert = new RiscaAlertConfigs();

    // Definisco il messaggio dell'alert da visualizzare
    this.messageForAlertFromMsgCode(m, alert, configs);
    // Definisco lo stile grafico dell'alert
    this.typeForAlertFromMsgCode(m, alert);

    // Ritorno l'oggetto di alert
    return alert;
  }

  /**
   * Funzione che modifica tramite referenza i messaggi dell'alert passato in input.
   * Le informazioni dei messaggi verranno definite a seconda dell'input.
   * @param messaggio MessaggioUtenteVo con la configurazione per la definizione del messaggio dell'alert.
   * @param alert RiscaAlertConfigs con la configurazione per la definizione del messaggio dell'alert.
   * @param configs IAlertConfigsFromCode con la configurazione per la definizione del messaggio dell'alert.
   */
  private messageForAlertFromMsgCode(
    messaggio: MessaggioUtenteVo,
    alert: RiscaAlertConfigs,
    configs: IAlertConfigsFromCode
  ) {
    // Verifico esista l'alert
    if (!alert) {
      // nessuna configurazione
      return;
    }

    // Estraggo le informazioni per la configurazione dell'alert
    const { code, codesPlaceholders, dataReplace, fallbackPlaceholder } =
      configs || {};
    // Recupero le informazioni dalla configurazione del messaggio
    const { des_testo_messaggio } = messaggio || {};

    // Definisco il messaggio per l'alert a seconda della configurazione
    if (codesPlaceholders && dataReplace) {
      // Esistono informazioni per un messaggio con parametri da rimpiazzare
      const msg = this._riscaMessages.getMessageWithPlacholder(
        code,
        codesPlaceholders,
        dataReplace,
        fallbackPlaceholder
      );
      // Definisco un array inserendo il messaggio recuperato
      alert.messages = [msg];
      // #
    } else {
      // E' un messaggio semplice
      alert.messages = [des_testo_messaggio ?? ''];
      // #
    }
  }

  /**
   * Funzione che modifica tramite referenza lo stile dell'alert passato in input.
   * Le informazioni dei messaggi verranno definite a seconda dell'input.
   * @param messaggio MessaggioUtenteVo con la configurazione per la definizione del messaggio dell'alert.
   * @param alert RiscaAlertConfigs con la configurazione per la definizione del messaggio dell'alert.
   */
  private typeForAlertFromMsgCode(
    messaggio: MessaggioUtenteVo,
    alert: RiscaAlertConfigs
  ) {
    // Verifico esista l'alert
    if (!alert) {
      // nessuna configurazione
      return;
    }

    // Recupero le informazioni dalla configurazione del messaggio
    const { tipo_messaggio } = messaggio;
    // Recupero il codice del tipo messaggio
    const { cod_tipo_messaggio } = tipo_messaggio || {};

    // Recupero tramite mappatura codice messaggio/stile alert, lo stile per l'alert
    const type = RiscaCodTMStiliAlert[cod_tipo_messaggio];
    // Assegno lo stile all'alert
    alert.type = type ?? RiscaInfoLevels.info;
  }

  /**
   * Funzione di comodo che imposta il messaggio d'errore
   * @param callError RiscaServerError che definisce il corpo dell'errore.
   * @param messageCode string che definisce il messaggio d'errore da visualizzare in testata. Default è 'E005'.
   * @param otherMessages Array di string contenente un array con altri messaggi di dettaglio per l'errore. Default è [].
   */
  messagesFromServerError(
    callError: RiscaServerError,
    messageCode = RiscaNotifyCodes.E005,
    otherMessages: string[] = []
  ): string[] {
    // Variabile di comodo
    const { error } = callError ?? {};
    // Definisco un array di messaggi d'errore
    let erroriValidazione: string[] = [];

    // Verifico se da server è stato restituito un codice specifico di errore
    if (error?.code) {
      // Sovrascrivo il message code di default
      messageCode = error.code as RiscaNotifyCodes;
    }

    // Recupero il messaggio d'errore dal servizio
    const message = this._riscaMessages.getMessage(messageCode, error?.title);
    // Recupero il codice per errore inaspettato
    erroriValidazione.push(message);

    // Verifico se esistono anche altri messaggi
    if (otherMessages) {
      // Concateno le liste di errori
      erroriValidazione = erroriValidazione.concat(otherMessages);
    }

    // Aggiorno la lista di errori
    return erroriValidazione;
  }

  /**
   * Funzione che verifica per default se l'alert config in input esiste, e contiene messaggi.
   * @param alertConfigs RiscaAlertConfigs da verificare.
   * @param alertConfigsChecks IAlertConfigsChecks contenente possibili controlli d'applicare sull'oggetto RiscaAlertConfigs.
   * @returns boolean che definisce se l'oggetto è valido, per default e per ogni possibile check di configurazione
   */
  alertConfigsCheck(
    alertConfigs: RiscaAlertConfigs,
    alertConfigsChecks?: IAlertConfigsChecks
  ): boolean {
    // Verifico che l'oggetto esista
    if (!alertConfigs) {
      return false;
    }

    // Definisco un flag che definisce il risultato del controllo sull'oggetto
    let check = false;

    // Definisco il controllo di default
    check = alertConfigs.messages?.length > 0;
    // Verifico il risultato
    if (!check) {
      return false;
    }

    // Controllo principale passato, verifico se esistono altri check da fare
    const checks = alertConfigsChecks !== undefined;
    const othersChecks =
      checks && !this._riscaUtilities.isEmptyObject(alertConfigs);
    // Verifico i controlli
    if (!othersChecks) {
      return true;
    }

    // Gestisco le logiche in un try catch per evitare possibili errori nell'input
    try {
      // Ciclo le proprietà dell'oggetto
      for (let [keyCheck] of Object.keys(alertConfigsChecks)) {
        // Aggiorno il flag di default
        check = alertConfigsChecks[keyCheck](alertConfigs);
        // Verifico se la funzione ha invalidato il check
        if (!check) {
          return false;
        }
      }

      // Tutte le funzioni di check sono valide
      return true;
      // #
    } catch (e) {
      // Loggo un warning
      this._logger.warning('alertConfigsCheck', { error: e });
      // Ritorno il controllo principale
      return true;
    }
  }

  /**
   * #####################
   * GESTIONE DEGLI EVENTI
   * #####################
   */

  /**
   * Funzione che emette la struttura di un alert.
   * @param alertConfigs RiscaAlertConfigs con la configurazione dell'alert.
   */
  emettiAlert(alertConfigs: RiscaAlertConfigs) {
    // Propago l'alert
    this.onAlertEmitted$.next(alertConfigs);
  }
}
