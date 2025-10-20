import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RiscaAlertService } from '../../../../services/risca/risca-alert.service';
import { ConfirmDataModal, RiscaAlertConfigs } from '../../../../utilities';
import { RiscaUtilityModalConsts } from './utilities/risca-utility-modal.consts';

/**
 * Modal di conferma.
 */
@Component({
  selector: 'risca-utility-modal',
  template: ``,
  styles: [``],
})
export class RiscaUtilityModalComponent implements OnInit {
  /** Oggetto con le costanti del componente. */
  RUM_C = RiscaUtilityModalConsts;

  /** ConfirmDataModal che contiene le informazioni da visualizzare nella modal di conferma. */
  @Input() dataModal: ConfirmDataModal;

  /** String che permette di definire il title della modale a livello di componente e non come configurazione. */
  title: string;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente per la ricerca semplice. */
  alertConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /**
   * Costruttore.
   */
  constructor(
    public activeModal: NgbActiveModal,
    private _riscaAlert: RiscaAlertService
  ) {}

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * ######################
   * FUNZIONI PER LA MODALE
   * ######################
   */

  /**
   * Funzione che effettua la close (e quindi la resolved) della modale.
   */
  modalConfirm(payload?: any) {
    // Close della modale
    this.activeModal.close(payload);
  }

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalCancel(payload?: any) {
    // Dismiss della modale
    this.activeModal.dismiss(payload);
  }

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalClose(payload?: any) {
    // Dismiss della modale
    this.activeModal.dismiss(payload);
  }

  /**
   * ##################
   * FUNZIONI DI COMODO
   * ##################
   */

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
      this.resetAlertConfigs(alertConfigs);
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo per il title dalla modale.
   */
  get modalTitle(): string {
    // Recupero il title dalla configurazione
    return this.dataModal?.title ?? this.title;
  }

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertConfigs);
  }

  /**
   * Getter di comodo per il payload della funzione di confirm.
   */
  get confirmPayload() {
    // Recupero dalla configurazione il dato
    return this.dataModal?.confirmPayload;
  }

  /**
   * Getter di comodo per il payload della funzione di close.
   */
  get closePayload() {
    // Recupero dalla configurazione il dato
    return this.dataModal?.closePayload;
  }

  /**
   * Getter di comodo per il payload della funzione di cancel.
   */
  get cancelPayload() {
    // Recupero dalla configurazione il dato
    return this.dataModal?.cancelPayload;
  }
}
