import { RiscaAlertService } from '../../services/risca/risca-alert.service';
import {
  RiscaAlertConfigs,
  RiscaInfoLevels,
  RiscaServerError,
  TRiscaAlertType,
} from '../../utilities';
import { RiscaNotifyCodes } from '../../utilities/enums/risca-notify-codes.enums';
import { RiscaMessagesService } from './../../services/risca/risca-messages.service';

/**
 * Classe di comodo che definisce delle funzioni comuni per il supporto delle logiche di gestione degli alert di Risca.
 */
export class RiscaAlertHelperClass {
  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /**
   * Costruttore.
   */
  constructor(
    protected _riscaAlert: RiscaAlertService,
    protected _riscaMessages: RiscaMessagesService
  ) {}

  /**
   * ####################################
   * FUNZIONI PER LA GESTIONE DEI SERVIZI
   * ####################################
   */

  /**
   * Funzione di comodo che imposta il messaggio di success quando la chiamata ai servizi va a buon fine.
   * @param messageCode string che definisce il codice per il messaggio da visualizzare. Default è 'P001'.
   * @param otherMessages Array di string contenente un array con altri messaggi di dettaglio per l'alert. Default è [].
   */
  protected onServiziSuccess(
    messageCode = RiscaNotifyCodes.P001,
    otherMessages: string[] = []
  ) {
    // Recupero il messaggio di success
    const messaggioSuccess = this._riscaMessages.getMessage(messageCode);
    // Definisco un array di string con tutti i messaggi da visualizzare
    const messaggi = [...[messaggioSuccess], ...otherMessages];

    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(
      this.alertConfigs,
      messaggi,
      RiscaInfoLevels.success
    );
  }

  /**
   * Funzione di comodo che imposta il messaggio d'errore
   * @param error RiscaServerError che definisce il corpo dell'errore.
   * @param messageCode string che definisce il messaggio d'errore da visualizzare in testata. Default è 'E005'.
   * @param otherMessages Array di string contenente un array con altri messaggi di dettaglio per l'errore. Default è [].
   */
  protected onServiziError(
    error: RiscaServerError,
    messageCode = RiscaNotifyCodes.E005,
    otherMessages: string[] = []
  ) {
    // Definisco un array di messaggi d'errore
    let erroriValidazione = this._riscaAlert.messagesFromServerError(
      error,
      messageCode,
      otherMessages
    );

    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(
      this.alertConfigs,
      erroriValidazione,
      RiscaInfoLevels.danger
    );
  }

  /**
   * Funzione di comodo che imposta il messaggio d'errore per molteplici errori
   * @param errors RiscaServerError che definisce il corpo dell'errore.
   */
  protected onServiziErrors(error: RiscaServerError) {
    // Variabile di comodo per controllare gli errori
    const errors = error.errors;

    // Se ho un errore solo, verifico se ha dei sottoerrori
    if (errors == undefined || errors.length == 0) {
      // Se non ne ha, invoco la funzione di gestione del singolo errore
      this.onServiziError(error);
      // Non devo fare altro
      return;
    }

    // Variabile di appoggio per la lista di errori di validazione
    let erroriValidazione: string[] = [];
    // Itero sugli errori per ottenere
    errors.forEach((error: RiscaServerError) => {
      // Prendo la lista di messaggi di errore
      const listaMessaggi = this._riscaAlert.messagesFromServerError(error);
      // Aggiungo agli errori di validazione la lista di errori
      erroriValidazione.push(...listaMessaggi);
    });

    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(
      this.alertConfigs,
      erroriValidazione,
      RiscaInfoLevels.danger
    );
  }

  /**
   * Funzione di comodo che imposta il messaggio d'errore generando un alert config specifico sulla base della configurazione dell'errore del server.
   * @param error RiscaServerError che definisce il corpo dell'errore.
   */
  protected alertFromServiziError(error: RiscaServerError) {
    // Gestisco il messaggio d'errore inaspettato
    const a = this._riscaAlert.createAlertFromServerError(error);
    // Modifico il dato dell'alert config
    this.alertConfigs = a;
  }

  /**
   * ###################################
   * FUNZIONI PER LA GESTIONE DELL'ALERT
   * ###################################
   */

  /**
   * Funzione che gestisce il prompt dei messaggi.
   * Se i parametri non sono definiti, verrà effettuato un reset.
   * @param c RiscaAlertConfigs d'aggiornare con le nuove informazioni.
   * @param messaggi Array di string contenente i messaggi da visualizzare.
   * @param tipo TRiscaAlertType che definisce la tipologia di alert da visualizzare.
   */
  protected aggiornaAlertConfigs(
    c?: RiscaAlertConfigs,
    messaggi?: string[],
    tipo?: TRiscaAlertType
  ) {
    // Verifico se esiste l'oggetto per l'alert, altrimenti imposto quello locale
    c = c ?? this.alertConfigs;
    // Aggiorno la configurazione
    c = this._riscaAlert.aggiornaAlertConfigs(c, messaggi, tipo);
  }

  /**
   * Funzione che gestisce il reset del prompt dei messaggi.
   * Se i parametri non sono definiti, verrà effettuato un reset.
   * @param c RiscaAlertConfigs da resettare.
   */
  protected resetAlertConfigs(c?: RiscaAlertConfigs) {
    // Verifico se esiste l'oggetto per l'alert, altrimenti imposto quello locale
    c = c ?? this.alertConfigs;
    // Resetto la configurazione
    c = this._riscaAlert.aggiornaAlertConfigs(c);
  }

  /**
   * Funzione che definisce un comportamento standard quando viene emesso l'evento: onAlertHidden; da parte del componente: risca-alert.
   * @param hidden boolean che definisce lo stato di nascosto dell'alert.
   * @param alertConfigs RiscaAlertConfigs da resettare.
   */
  onAlertHidden(hidden: boolean, alertConfigs: RiscaAlertConfigs) {
    // Verifico il risultato
    if (hidden) {
      // Resetto la configurazione dell'alert
      this.aggiornaAlertConfigs(alertConfigs);
    }
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertConfigs);
  }
}
