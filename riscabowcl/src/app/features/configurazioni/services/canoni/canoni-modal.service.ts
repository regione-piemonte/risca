import { Injectable } from '@angular/core';
import { RegolaUsoVo } from '../../../../core/commons/vo/regola-uso-vo';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import { ICommonParamsModal } from '../../../../shared/utilities';
import { ApriModalConfigs } from '../../../../shared/utilities/classes/risca-modal/risca-modal.classes';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IApriModalConfigsForClass } from '../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { CreaRegolaUsoModalComponent } from '../../modals/crea-regola-uso-modal/crea-regola-uso-modal.component';
import {
  ICreaNuovaRegolaModal,
  ISalvaModificheRegoleModal,
  IApriDettaglioRegolaUsoModal,
} from './utilities/canoni.interfaces';
import { DettaglioRegolaUsoModalComponent } from '../../modals/dettaglio-regola-uso-modal/dettaglio-regola-uso-modal.component';

@Injectable({ providedIn: 'root' })
export class CanoniModalService {
  /**
   * Costruttore
   */
  constructor(
    private _riscaMessages: RiscaMessagesService,
    private _riscaModal: RiscaModalService
  ) {}

  /**
   * #####################
   * CONFIGURAZIONE MODALI
   * #####################
   */

  /**
   * Funzione che gestisce le configurazioni e apre la modale di gestione il salvataggio delle regole modificate dall'utente.
   * @param config ISalvaModificheRegoleModal con le configurazioni per l'apertura delle modali.
   */
  openSalvaModificheRegoleModal(config: ISalvaModificheRegoleModal) {
    // Verifico l'input
    if (!config) {
      // Nessuna configurazione, blocco il flusso
      return;
    }
    // Estraggo i parametri dalla configurazione
    const { onConfirm, onCancel, onClose, regoleModificate } = config;
    // Genero il testo da visualizzare per l'alert
    let body: string;
    body = this.salvaModificheRegoleModalBody(regoleModificate);

    // Definisco i parametri da passare alla modale
    let params: ICommonParamsModal;
    params = {
      title: 'Canoni modificati',
      body,
      buttonCancel: 'ANNULLA',
      buttonConfirm: 'SALVA',
      onConfirm,
      onClose,
      onCancel,
    };

    // Richiamo la funzione di apertura della modale di conferma
    this._riscaModal.apriModalConfermaWithParams(params);
  }

  /**
   * Funzione che gestisce le configurazioni e apre la modale di gestione per creare una nuova regola uso.
   * @param config ICreaNuovaRegolaModal con le configurazioni per l'apertura delle modali.
   */
  openCreaNuovaRegolaModal(config: ICreaNuovaRegolaModal) {
    // Verifico l'input
    if (!config) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Estraggo i parametri dalla configurazione
    const { callbacks, dataModal } = config;
    // Recupero il componente dalla mappatura delle modali per le prenotazioni
    const component = CreaRegolaUsoModalComponent;

    // Creo la configurazione per l'apertura della modale
    const mConfig: IApriModalConfigsForClass = {
      callbacks,
      component,
      options: { windowClass: 'r-mdl-crea-regola-uso' },
      params: { dataModal },
    };

    // Richiamo la funzione di apertura della modale
    this._riscaModal.apriModal(new ApriModalConfigs(mConfig));
  }

  /**
   * Funzione che gestisce le configurazioni e apre la modale di gestione per visualizzare i dettagli delle regole uso.
   * @param config IApriDettaglioRegolaUsoModal con le configurazioni per l'apertura delle modali.
   */
  openDettaglioRegolaUsoModal(config: IApriDettaglioRegolaUsoModal) {
    // Verifico l'input
    if (!config) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Estraggo i parametri dalla configurazione
    const { callbacks, dataModal } = config;
    // Recupero il componente dalla mappatura delle modali per le prenotazioni
    const component = DettaglioRegolaUsoModalComponent;

    // Creo la configurazione per l'apertura della modale
    const mConfig: IApriModalConfigsForClass = {
      callbacks,
      component,
      options: { windowClass: 'r-mdl-dettaglio-regola-uso' },
      params: { dataModal },
    };

    // Richiamo la funzione di apertura della modale
    this._riscaModal.apriModal(new ApriModalConfigs(mConfig));
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione di utility che genera il testo, con tag HTML, da visualizzare all'interno della modale.
   * Questa funzione gestisce le logiche per la funzione: openSalvaModificheRegoleModal.
   * @param regoleMod RegolaUsoVo[] con la lista di regole modificate.
   */
  private salvaModificheRegoleModalBody(
    regoleModificate: RegolaUsoVo[]
  ): string {
    // Recupero il messaggio specifico per la modale
    const code = RiscaNotifyCodes.A030;
    // Recupero il messaggio
    let messaggioCodice: string;
    messaggioCodice = this._riscaMessages.getMessage(code);
    // Definisco un titolo statico richiesto dall'analisi
    let titoloUsi: string;
    titoloUsi = `Usi per canone modificati:`;

    // Definisco la variabile che conterrà le informazioni delle regole
    let desRegole: string = '';
    // Definisco il separatore delle descrizioni
    let sepDesUsi: string = ' |';
    // Effettuo una verifica sull'input per non generare errori
    const regoleMod: RegolaUsoVo[] = regoleModificate ?? [];

    // Itero la lista di regole modificate e genero uno specifico html
    desRegole = regoleMod
      .map((r: RegolaUsoVo) => {
        // Ritorno la descrizione dell'uso
        return r.tipo_uso?.des_tipo_uso ?? '-';
        // #
      })
      .join(sepDesUsi);

    /**
     * NOTA BENE: al momento dello sviluppo, si fa riferimento al documento: ;
     * La definizione dell'interfaccia è stata passata per le vie brevi dalla collega Laura Veneruso, che ha dato direttive per chat.
     * Non è presente, al momento, un capitolo che tratti questa parte specifica, ma rientra negli use case: al momento => variante 5.
     */
    // Per la descrizione da ritornare, genero una piccola porzione di HTML
    let body: string = `
      <div class="row">
        <div class="col">
          ${messaggioCodice}
        </div>
      </div>
      <div class="row r-m-intL-t">
        <div class="col">
          ${titoloUsi}
        </div>
      </div>
      <div class="row">
        <div class="col">
          ${desRegole}
        </div>
      </div>
    `;

    // Ritorno il body generato
    return body;
  }
}
