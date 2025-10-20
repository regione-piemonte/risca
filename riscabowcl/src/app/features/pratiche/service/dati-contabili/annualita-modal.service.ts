import { Injectable } from '@angular/core';
import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import { ICallbackDataModal } from '../../../../shared/utilities';
import { ApriModalConfigs } from '../../../../shared/utilities/classes/risca-modal/risca-modal.classes';
import { IApriModalConfigsForClass } from '../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { AnnualitaModalComponent } from '../../modal/annualita-modal/annualita-modal.component';
import { IAnnualitaModalConfigs } from '../../modal/annualita-modal/utilities/annualita-modal.interfaces';

@Injectable({ providedIn: 'root' })
export class AnnualitaModalService {
  /** String che definisce la classe di stile per le modali dell'annualità. */
  private MODAL_CONTAINER = 'r-mdl-annualita';

  /**
   * Costruttore.
   */
  constructor(private _riscaModal: RiscaModalService) {}

  /**
   * ####################################
   * GESTIONE DELLA MODALE PER L'APERTURA
   * ####################################
   */

  /**
   * Funzione che apre un modale per la gestione di un'annualità.
   * @param dataModal IAnnualitaInsModalConfigs contenente i parametri da passare alla modale.
   * @param callbacks ICallbackDataModal contenente le configurazioni per le callback della modale.
   */
  apriModaleAnnualita(
    dataModal: IAnnualitaModalConfigs,
    callbacks: ICallbackDataModal
  ) {
    // Variabili di configurazione fisse
    const component = AnnualitaModalComponent;
    const options: NgbModalOptions = {
      windowClass: this.MODAL_CONTAINER,
      backdrop: 'static',
    };

    // Creo la configurazione per l'apertura della modale
    const mConfig: IApriModalConfigsForClass = {
      callbacks,
      component,
      options,
      params: { dataModal },
    };

    // Richiamo l'apertura del modale passando le configurazioni
    this._riscaModal.apriModal(new ApriModalConfigs(mConfig));
  }
}
