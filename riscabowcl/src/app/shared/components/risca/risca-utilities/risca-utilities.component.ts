import { Component, Input, OnInit } from '@angular/core';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { NavigationHelperClass } from '../../../classes/navigation/navigation-helper.class';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaAlertService } from '../../../services/risca/risca-alert.service';
import { RiscaMessagesService } from '../../../services/risca/risca-messages.service';
import {
  AppActions,
  RiscaAlertConfigs,
  RiscaInfoLevels,
  RiscaServerError,
  RiscaServerErrorInfo,
  TRiscaAlertType,
} from '../../../utilities';
import { RiscaNotifyCodes } from '../../../utilities/enums/risca-notify-codes.enums';

@Component({
  selector: 'risca-utilities',
  templateUrl: './risca-utilities.component.html',
  styleUrls: ['./risca-utilities.component.scss'],
})
export class RiscaUtilitiesComponent
  extends NavigationHelperClass
  implements OnInit
{
  /** CommonConsts come oggetto per le costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** AppActions che definisce di che tipo di azioni verranno fatti sui dati. */
  @Input() modalita: AppActions;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /**
   * Costruttore.
   */
  constructor(
    navigationHelper: NavigationHelperService,
    protected _riscaAlert: RiscaAlertService,
    protected _riscaMessages: RiscaMessagesService
  ) {
    // Richiamo il super
    super(navigationHelper);
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

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
    const errorsInfo: RiscaServerErrorInfo[] = error.errors;

    // Se ho un errore solo, verifico se ha dei sottoerrori
    if (errorsInfo == undefined || errorsInfo.length == 0) {
      // Se non ne ha, invoco la funzione di gestione del singolo errore
      this.onServiziError(error);
      // Non devo fare altro
      return;
    }

    // Variabile di appoggio per la lista di errori di validazione
    let erroriValidazione: string[] = [];

    // Estraggo la lista di codici errore generati
    const codes: string[] = errorsInfo.map((e: RiscaServerErrorInfo) => e.code);
    // Itero sugli errori per ottenere
    codes.forEach((code: string) => {
      // Prendo la lista di messaggi di errore
      const messaggio: string = this._riscaMessages.getMessage(code);
      // Aggiungo agli errori di validazione la lista di errori
      erroriValidazione.push(messaggio);
      // #
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
    // Propago l'alert tramite servizio
    this.emettiAlert(this.alertConfigs);
  }

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
    // Propago l'alert tramite servizio
    this.emettiAlert(c);
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
    // Propago l'alert tramite servizio
    this.emettiAlert(c);
  }

  /**
   * Funzione che emette la struttura di un alert per la visualizzazione.
   * @param a RiscaAlertConfigs con la configurazione dell'alert.
   */
  protected emettiAlert(a: RiscaAlertConfigs) {
    // Richiamo la funzione del servizio con l'aggiornamento dell'alert
    this._riscaAlert.emettiAlert(a);
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
      this.resetAlertConfigs(alertConfigs);
    }
  }

  /**
   * Funzione di controllo sulla modalità, se impostatao su: inserimento.
   */
  isInserimento(modalita: AppActions) {
    // Verifico la modalità
    return modalita === AppActions.inserimento;
  }

  /**
   * Funzione di controllo sulla modalità, se impostatao su: modifica.
   */
  isModifica(modalita: AppActions) {
    // Verifico la modalità
    return modalita === AppActions.modifica;
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

  /**
   * Getter per la modalità se impostata su inserimento.
   */
  get inserimento() {
    // Verifico la modalità
    return this.modalita === AppActions.inserimento;
  }

  /**
   * Getter per la modalità se impostata su modifica.
   */
  get modifica() {
    // Verifico la modalità
    return this.modalita === AppActions.modifica;
  }
}
