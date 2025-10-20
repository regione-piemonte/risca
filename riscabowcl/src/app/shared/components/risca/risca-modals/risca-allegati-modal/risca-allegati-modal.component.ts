import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AllegatoVo } from 'src/app/core/commons/vo/documento-allegato-vo';
import { DocumentiAllegatiService } from 'src/app/features/pratiche/service/documenti-allegati/documenti-allegati.service';
import { RiscaAlertHelperClass } from 'src/app/shared/classes/risca-alert/risca-alert-helper.class';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from './../../../../services/risca/risca-messages.service';
import { RiscaServerError } from './../../../../utilities/classes/utilities.classes';

@Component({
  selector: 'risca-allegati-modal',
  templateUrl: './risca-allegati-modal.component.html',
  styleUrls: ['./risca-allegati-modal.component.scss'],
})
export class RiscaAllegatiModalComponent
  extends RiscaAlertHelperClass
  implements OnInit
{
  /**
   * idRiscossione degli allegati
   */
  idRiscossione: number;
  /**
   * lista egli allegati che contiene le informazioni da visualizzare nella modal di conferma.
   */
  allegati: AllegatoVo[];

  /**
   * Costruttore.
   */
  constructor(
    public activeModal: NgbActiveModal,
    private _docAllegati: DocumentiAllegatiService,
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService
  ) {
    // Super
    super(riscaAlert, riscaMessages);
  }

  ngOnInit(): void {}

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalDismiss() {
    // Dismiss della modale
    this.activeModal.dismiss();
  }

  /**
   * Funzione che effettua la close (e quindi la resolved) della modale.
   */
  modalConfirm() {
    // Close della modale
    this.activeModal.close();
  }

  /**
   * Funzione al click dell'elemento della tabella
   * @param allegato oggetto selezionato della tabella
   */
  clickAllegato(allegato: AllegatoVo) {
    // Svuoto gli errori
    this.aggiornaAlertConfigs();
    // Prendo l'idClassificazione
    const idClassificazione = allegato.id_classificazione;
    // Chiamo il servizio per aprire il file
    this.apriFile(idClassificazione);
  }

  /**
   * Scarica un file in base all'idClassificazione e poi lo apre.
   * @param idClassificazione id del file, che sia un documento o un allegato.
   */
  apriFile(idClassificazione: string) {
    // Chiamo il servizio per avere il path del file
    this._docAllegati
      .scaricaEApriFile(this.idRiscossione, idClassificazione)
      .subscribe({
        error: (err: RiscaServerError) => {
          // Gestisco l'errore
          this.onServiziError(err);
        },
      });
  }
}
