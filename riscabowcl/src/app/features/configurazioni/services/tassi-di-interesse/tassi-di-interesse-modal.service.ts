import { Injectable } from '@angular/core';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import { ApriModalConfigs } from '../../../../shared/utilities/classes/risca-modal/risca-modal.classes';
import { IApriModalConfigsForClass } from '../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { IApriDettaglioInteressiLegaliModal, IApriDettaglioInteressiMoraModal } from './utilies/tassi-di-interesse-modal.interfaces';
import { InteressiLegaliModalComponent } from '../../modals/interessi-legali-modal/interessi-legali-modal.component';
import { InteressiDiMoraModalComponent } from '../../modals/interessi-di-mora-modal/interessi-di-mora-modal.component';

@Injectable({ providedIn: 'root' })
export class TassiDiInteresseModalService {
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
   * Funzione che gestisce le configurazioni e apre la modale di gestione per visualizzare i dettagli degli interessi legali.
   * @param config IApriDettaglioInteressiLegaliModal con le configurazioni per l'apertura delle modali.
   */
  openModificaInteressiLegaliModal(config: IApriDettaglioInteressiLegaliModal) {
    // Verifico l'input
    if (!config) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Estraggo i parametri dalla configurazione
    const { callbacks, dataModal } = config;
    // Recupero il componente per la modale
    const component = InteressiLegaliModalComponent;

    // Creo la configurazione per l'apertura della modale
    const mConfig: IApriModalConfigsForClass = {
      callbacks,
      component,
      options: { windowClass: 'r-mdl-modifica-interesse-legale'},
      params: { dataModal },
    };

    // Richiamo la funzione di apertura della modale
    this._riscaModal.apriModal(new ApriModalConfigs(mConfig));
  }

  /**
   * Funzione che gestisce le configurazioni e apre la modale di gestione per visualizzare i dettagli degli interessi di mora.
   * @param config IApriDettaglioInteressiMoraModal con le configurazioni per l'apertura delle modali.
   */
  openModificaInteressiMoraModal(config: IApriDettaglioInteressiMoraModal) {
    // Verifico l'input
    if (!config) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Estraggo i parametri dalla configurazione
    const { callbacks, dataModal } = config;
    // Recupero il componente per la modale

    /* DEVO CREARE LA COMPONENTE CHE PERMETERRA DI APRIRE LA  MODALE PER MODIFICARE GLI INTERESSI DI MORA */
      const component = InteressiDiMoraModalComponent;

    // Creo la configurazione per l'apertura della modale
    const mConfig: IApriModalConfigsForClass = {
      callbacks,
      component,
      options: { windowClass: 'r-mdl-modifica-interesse-di-mora'},
      params: { dataModal },
    };

    // Richiamo la funzione di apertura della modale
    this._riscaModal.apriModal(new ApriModalConfigs(mConfig));

  }
}
