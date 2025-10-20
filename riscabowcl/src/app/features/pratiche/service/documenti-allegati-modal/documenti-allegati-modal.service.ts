import { Injectable } from '@angular/core';
import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { AllegatoVo } from 'src/app/core/commons/vo/documento-allegato-vo';
import { RiscaAllegatiModalComponent } from 'src/app/shared/components/risca/risca-modals/risca-allegati-modal/risca-allegati-modal.component';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import { ApriModalConfigs } from '../../../../shared/utilities/classes/risca-modal/risca-modal.classes';

/**
 * Servizio di utility per la gestione delle modali custom di risca, richieste da CSI, per gli allegati.
 */
@Injectable({
  providedIn: 'root',
})
export class DocumentiAllegatiModalService {
  // Opzioni di visualizzazione componente
  private DOC_ALL_MODAL_OPTIONS: NgbModalOptions = {
    windowClass: 'r-mdl-nuova-prenotazione',
    backdrop: 'static',
  };

  /**
   * Costruttore
   */
  constructor(private _riscaModal: RiscaModalService) {}

  /**
   * #####################
   * CONFIGURAZIONE MODALI
   * #####################
   */

  /**
   * Funzione che gestisce le configurazioni e apre la modale di visualizzazione degli allegati.
   * @param allegati AllegatoVo[] con la ista di allegati da mostrare.
   */
  openDocAllegatiModal(idRiscossione: number, allegati: AllegatoVo[]) {
    // Verifico l'input
    if (!allegati || allegati.length == 0) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Variabili di configurazione fisse
    const component = RiscaAllegatiModalComponent;
    // Opzioni di visualizzazione
    const options = this.DOC_ALL_MODAL_OPTIONS;

    // Genero la configurazione
    const mConfig = {
      component,
      options,
      params: { idRiscossione, allegati },
    };
    // Richiamo la funzione di apertura della modale
    this._riscaModal.apriModal(new ApriModalConfigs(mConfig));
  }
}
