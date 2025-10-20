import { Component, OnInit } from '@angular/core';
import { RiscaAlertConfigs, RiscaInfoLevels } from '../../../shared/utilities';

/**
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
@Component({
  selector: 'risca-unauthorized',
  templateUrl: './unauthorized.component.html',
  styleUrls: ['./unauthorized.component.scss'],
})
export class RiscaUnauthorizedComponent implements OnInit {
  /** Array di string con messaggi statici per la comunicazione all'utente. */
  private unAuthMsgs = [
    '<strong>Accesso non autorizzato</strong><br>',
    "Il caricamento dei dati è fallito, ritentare il login all'applicazione.",
  ];

  /** RiscaAlertConfigs contenente i parametri per il componente risca-alert. */
  alertConfigs: RiscaAlertConfigs;

  ngOnInit() {
    // Imposto le informazioni per l'alert
    this.alertConfigs = new RiscaAlertConfigs({
      messages: this.unAuthMsgs,
      type: RiscaInfoLevels.danger,
    });
  }
}
