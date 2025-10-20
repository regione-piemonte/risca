import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RiscaAlertService } from '../../../../services/risca/risca-alert.service';
import { ConfirmDataModal } from '../../../../utilities';
import { RiscaUtilityModalComponent } from '../risca-utility-modal/risca-utility-modal.component';

/**
 * Modal di conferma.
 */
@Component({
  selector: 'risca-confirm-modal',
  templateUrl: './risca-confirm-modal.component.html',
  styleUrls: ['./risca-confirm-modal.component.scss'],
})
export class RiscaConfirmModalComponent
  extends RiscaUtilityModalComponent
  implements OnInit
{
  /**
   * dataModal che contiene le informazioni da visualizzare nella modal di conferma.
   */
  @Input() dataModal: ConfirmDataModal;

  constructor(activeModal: NgbActiveModal, riscaAlert: RiscaAlertService) {
    // Richiamo il super
    super(activeModal, riscaAlert);
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}
