import { Injectable } from '@angular/core';
import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { RimborsoVo } from 'src/app/core/commons/vo/rimborso-vo';
import { AccertamentoVo } from '../../../../../core/commons/vo/accertamento-vo';
import { PraticaVo } from '../../../../../core/commons/vo/pratica-vo';
import { RiscaModalService } from '../../../../../shared/services/risca/risca-modal.service';
import {
  AbilitaDASezioni,
  AbilitaDASoggetti,
  AppActions,
  ICallbackDataModal,
} from '../../../../../shared/utilities';
import { ApriModalConfigs } from '../../../../../shared/utilities/classes/risca-modal/risca-modal.classes';
import { IApriModalConfigsForClass } from '../../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { AmbitoService } from '../../../../ambito/services';
import { RimborsiConsts } from '../../../components/dati-contabili/rimborsi/utilities/rimborsi.consts';
import { DatiAnagraficiSDConsts } from '../../../components/dati-contabili/stato-debitorio/dati-anagrafici-sd/utilities/dati-anagrafici-sd.consts';
import { DatiContabiliConsts } from '../../../consts/dati-contabili/dati-contabili.consts';
import { CercaSoggettoDatiContabili } from '../../../enums/dati-contabili/dati-contabili.enums';
import { CercaSoggettoDCModalComponent } from '../../../modal/cerca-soggetto-dc-modal/cerca-soggetto-dc-modal.component';
import {
  ICercaSoggettoCDModalLabels,
  ICercaSoggettoDCModalConfigs,
} from '../../../modal/cerca-soggetto-dc-modal/utilities/cerca-soggetto-dc-modal.interfaces';
import { GestisciAccertamentoModal } from '../../../modal/gestisci-accertamento-modal/gestisci-accertamento-modal.component';
import { IGestisciAccertamentoConfigs } from '../../../modal/gestisci-accertamento-modal/utilities/gestisci-accertamento-modal.interfaces';
import { GestisciRimborsoModal } from '../../../modal/gestisci-rimborso-modal/gestisci-rimborso-modal.component';
import { IGestisciRimborsoConfigs } from '../../../modal/gestisci-rimborso-modal/utilities/gestisci-rimborso-modal.interfaces';
import { PagamentoSDModalComponent } from '../../../modal/pagamento-sd-modal/pagamento-sd-modal.component';
import { IVersamentoConfigs } from '../../../modal/pagamento-sd-modal/utilities/pagamento-sd-modal.interfaces';
import { SDCollegatiModalComponent } from '../../../modal/sd-collegati-modal/sd-collegati-modal.component';
import { GruppoSoggettoService } from '../../dati-anagrafici/gruppo-soggetto.service';
import { IModificaVersamentoModalConfigs } from '../utilities/dati-contabili-modal.interfaces';

@Injectable({ providedIn: 'root' })
export class DatiContabiliModalService {
  /** Oggetto contenente una serie di costanti per la gestione dei dati contabili. */
  private DC_C = DatiContabiliConsts;
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  private RMB_C = RimborsiConsts;
  /** Oggetto contenente una serie di costanti per la gestione dei dati anagrafici sd. */
  private DASD_C = DatiAnagraficiSDConsts;

  /**
   * Costruttore.
   */
  constructor(
    private _ambito: AmbitoService,
    private _gruppoSoggetto: GruppoSoggettoService,
    private _riscaModal: RiscaModalService
  ) {}

  /**
   * #####################################
   * FUNZIONI PER LA GESTIONE DELLE MODALI
   * #####################################
   */

  /**
   * Funzione che apre il modale per la ricerca di un creditore.
   * @param callbacks CallbackDataModal contenente le funzioni di callback del modale.
   */
  apriCambiaCreditoreModal(callbacks?: ICallbackDataModal) {
    // Definisco il target per la gestione della modale
    const target = CercaSoggettoDatiContabili.creditore;
    // Richiamo l'apertura della modale per il cambio creditore
    this.apriCercaSoggettoDCModal(target, callbacks);
  }

  /**
   * Funzione che apre il modale per la ricerca di un titolare.
   * @param callbacks CallbackDataModal contenente le funzioni di callback del modale.
   */
  apriCambiaTitolareModal(callbacks?: ICallbackDataModal) {
    // Definisco il target per la gestione della modale
    const target = CercaSoggettoDatiContabili.titolare;
    // Richiamo l'apertura della modale per il cambio titolare
    this.apriCercaSoggettoDCModal(target, callbacks);
  }

  /**
   * Funzione di comodo che restituisce le label da visualizzare per la modale di ricerca soggetto.
   * @param target CercaSoggettoDatiContabili con il target per la ricerca del soggetto.
   * @returns ICercaSoggettoCDModalLabels contenente le etichette per la modale.
   */
  private labelModalByTarget(
    target: CercaSoggettoDatiContabili
  ): ICercaSoggettoCDModalLabels {
    // Verfico il target per le label
    switch (target) {
      case CercaSoggettoDatiContabili.creditore:
        return {
          modalTitle: this.RMB_C.CERCA_CREDITORE_MODAL_TITLE,
          description: this.RMB_C.DESCRIPTION_CERCA_CREDITORE,
          labelBtnAnnulla: this.RMB_C.LABEL_ANNULLA,
          labelBtnConferma: this.RMB_C.LABEL_INSERISCI_CREDITORE,
        };
      case CercaSoggettoDatiContabili.titolare:
        return {
          modalTitle: this.DASD_C.CERCA_TITOLARE_MODAL_TITLE,
          description: this.DASD_C.DESCRIPTION_CERCA_TITOLARE,
          labelBtnAnnulla: this.DASD_C.LABEL_ANNULLA,
          labelBtnConferma: this.DASD_C.LABEL_INSERISCI_TITOLARE,
        };
      default:
        return {
          modalTitle: this.DC_C.CERCA_SOGGETTO_MODAL_TITLE,
          description: this.DC_C.DESCRIPTION_CERCA_SOGGETTO,
          labelBtnAnnulla: this.DC_C.LABEL_ANNULLA,
          labelBtnConferma: this.DC_C.LABEL_INSERISCI_SOGGETTO,
        };
    }
  }

  /**
   * Funzione che apre il modale per la ricerca di un soggetto.
   * @param target CercaSoggettoDatiContabili che definisce il tipo di soggetto da ricercare.
   * @param callbacks CallbackDataModal contenente le funzioni di callback del modale.
   */
  apriCercaSoggettoDCModal(
    target: CercaSoggettoDatiContabili,
    callbacks?: ICallbackDataModal
  ) {
    // Definisco l'oggetto di configurazione per la modal di bootstrap
    const options: NgbModalOptions = {
      windowClass: 'cerca-soggetto-dc-modal',
      backdrop: 'static',
    };
    // Definisco l'istanza del componente della modale
    const component = CercaSoggettoDCModalComponent;
    // Recupero le label per la modale
    const labels = this.labelModalByTarget(target);

    // Definisco i parametri da passare alla modal
    const dataModal: ICercaSoggettoDCModalConfigs = {
      soggettiAbilitati: this.isGestioneAbilitata,
      gruppiAbilitati: this.isGruppoAbilitato,
      modalTitle: labels.modalTitle,
      description: labels.description,
      labelBtnAnnulla: labels.labelBtnAnnulla,
      labelBtnConferma: labels.labelBtnConferma,
    };
    const params = { dataModal };

    // Definisco l'oggetto di configurazioni per il servizio
    const configs: IApriModalConfigsForClass = {
      component,
      options,
      callbacks,
      params,
    };

    // Richiamo il servizio per l'apertura del modale
    this._riscaModal.apriModal(new ApriModalConfigs(configs));
  }

  /**
   * Funzione che gestisce le configurazioni e apre la modale di modifica dei rimborsi.
   * @param rimborso AttivitaRimborsi con il rimborso da modificare.
   */
  openModificaRimborsoModal(
    rimborso: RimborsoVo,
    callbacks?: ICallbackDataModal
  ) {
    // Verifico l'input
    if (!rimborso) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Variabili di configurazione fisse
    const component = GestisciRimborsoModal;
    const modalita = AppActions.modifica;
    // Opzioni di visualizzazione
    const options: NgbModalOptions = {
      windowClass: 'r-mdl-modifica-rimborso',
      backdrop: 'static',
    };
    const dataModal: IGestisciRimborsoConfigs = {
      soggettiAbilitati: this.isGestioneAbilitata,
      gruppiAbilitati: this.isGruppoAbilitato,
      description: this.RMB_C.DESCRIPTION_CERCA_CREDITORE,
      rimborso,
    };

    // Genero la configurazione
    const modalConfigs = {
      component,
      options,
      params: { modalita, dataModal },
      callbacks,
    };

    // Richiamo la funzione di apertura della modale
    this._riscaModal.apriModal(new ApriModalConfigs(modalConfigs));
  }

  /**
   * Funzione che gestisce le configurazioni e apre la modale di modifica dei rimborsi.
   * @param accertamento AttivitaRimborsi con il rimborso da modificare.
   */
  openModificaAccertamentoModal(
    accertamento: AccertamentoVo,
    callbacks?: ICallbackDataModal
  ) {
    // Verifico l'input
    if (!accertamento) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Variabili di configurazione fisse
    const component = GestisciAccertamentoModal;
    const modalita = AppActions.modifica;
    // Opzioni di visualizzazione
    const options: NgbModalOptions = {
      windowClass: 'r-mdl-modifica-accertamento',
      backdrop: 'static',
    };
    const dataModal: IGestisciAccertamentoConfigs = {
      soggettiAbilitati: this.isGestioneAbilitata,
      gruppiAbilitati: this.isGruppoAbilitato,
      description: this.RMB_C.DESCRIPTION_CERCA_CREDITORE,
      accertamento,
    };

    // Genero la configurazione
    const modalConfigs = {
      component,
      options,
      params: { modalita, dataModal },
      callbacks,
    };

    // Richiamo la funzione di apertura della modale
    this._riscaModal.apriModal(new ApriModalConfigs(modalConfigs));
  }

  /**
   * Funzione che gestisce l'apertura della modale per la modifica di un pagamento.
   * I dati in input del pagamento saranno il dettaglio della tabella, dalla quale verranno poi recuperate le informazioni per la gestione del dato.
   * @param configs IModificaVersamentoModalConfigs con i dettagli delle informazioni da gestire per la modale.
   * @param callbacks ICallbackDataModal con le informazioni per le funzioni di callback una volta chiusa la modale.
   */
  openModificaPagamendoSDModal(
    configs: IModificaVersamentoModalConfigs,
    callbacks?: ICallbackDataModal
  ) {
    // Verifico l'input
    if (!configs) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Estraggo dalle configurazioni i dettagli
    const { pagamento, dettagliPagamentoSearch } = configs;
    // Verifico che esistano i dettagli minimi
    if (!pagamento) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Variabili di configurazione fisse
    const component = PagamentoSDModalComponent;
    const modalita = AppActions.modifica;
    // Opzioni di visualizzazione
    const options: NgbModalOptions = {
      windowClass: 'r-mdl-modifica-pagamento-sd',
      backdrop: 'static',
    };
    const dataModal: IVersamentoConfigs = {
      pagamento,
      dettagliPagamentoSearch,
    };

    // Genero la configurazione
    const modalConfigs = {
      component,
      options,
      params: { modalita, dataModal },
      callbacks,
    };
    // Creo la classe dati per la modale
    const modal = new ApriModalConfigs(modalConfigs);

    // Richiamo la funzione di apertura della modale
    this._riscaModal.apriModal(modal);
  }

  /**
   * Funzione che gestisce le configurazioni e apre la modale di modifica dei rimborsi.
   * @param rimborso AttivitaRimborsi con il rimborso da modificare.
   */
  openStatiDebitoriCollegatiModal(rimborso: RimborsoVo, pratica: PraticaVo) {
    // Verifico l'input
    if (!rimborso) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Variabili di configurazione fisse
    const component = SDCollegatiModalComponent;
    // Opzioni di visualizzazione
    const options: NgbModalOptions = {
      windowClass: 'r-mdl-sd-collegati',
      backdrop: 'static',
    };

    // Genero la configurazione
    const modalConfigs = {
      component,
      options,
      params: { rimborso, pratica },
    };

    // Richiamo la funzione di apertura della modale
    this._riscaModal.apriModal(new ApriModalConfigs(modalConfigs));
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che recupera l'abilitazione 'isGestioneAbilitata' per i soggetti.
   */
  get isGestioneAbilitata(): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    return this._ambito.getAbilitazioneSoggettiGruppi(
      AbilitaDASezioni.soggetti,
      AbilitaDASoggetti.isGestioneAbilitata
    );
  }

  /**
   * Getter che recupera l'abilitazione 'isAbilitato' per i gruppi.
   */
  get isGruppoAbilitato(): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    return this._gruppoSoggetto.isAbilitato;
  }
}
