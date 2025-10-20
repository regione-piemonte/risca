import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { RicercaStatiDebitoriPagamentoVo } from '../../../../core/commons/vo/ricerca-stati-debitori-pagamento-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { DatiContabiliService } from '../../../../features/pratiche/service/dati-contabili/dati-contabili/dati-contabili.service';
import { getDefaultPaginationPagaSDTable } from '../../../classes/risca-table/dati-contabili/pagamento-stati-debitori.table';
import { RiscaSDPerPagamentoModalComponent } from '../../../modals/risca-sd-nap-codice-utenza-modal/risca-sd-per-pagamento-modal.component';
import { RiscaServerError, RiscaTablePagination } from '../../../utilities';
import { ApriModalConfigs } from '../../../utilities/classes/risca-modal/risca-modal.classes';
import { IApriModalConfigsForClass } from '../../../utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { RiscaSpinnerService } from '../../risca-spinner.service';
import { RiscaModalService } from '../risca-modal.service';
import { IRiscaPBModalConfigs } from './utilities/risca-pagamento-bollettazione-modal.interfaces';

/**
 * Servizio di utility per la gestione delle modali per la sezione: risca-versamento-non-manuale.
 */
@Injectable({ providedIn: 'root' })
export class RiscaPagamentoBollettazioneModalService {
  /** string con l'identificato dello spinner. */
  private SPINNER: string =
    'RISCA_PAGAMENTO_BOLLETTAZIONE_MODAL_SERVICE_SPINNER';

  /**
   * Costruttore
   */
  constructor(
    private _datiContabili: DatiContabiliService,
    private _riscaModal: RiscaModalService,
    private _riscaSpinner: RiscaSpinnerService
  ) {}

  /**
   * #####################
   * CONFIGURAZIONE MODALI
   * #####################
   */

  /**
   * Funzione che gestisce le configurazioni e apre la modale di gestione per la lista stati debitori usando: NAP.
   * @param config IRiscaVNMModalConfigs con le informazioni per l'apertura della modale.
   */
  openRicercaSDPagamentoBollettazione(config: IRiscaPBModalConfigs) {
    // Verifico l'input
    if (!config) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Estraggo i parametri dalla configurazione
    const { callbacks, dataModal } = config;
    // Verifico che sia definito l'elemento di ricerca
    if (!dataModal?.ricerca) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Recupero il componente dalla mappatura delle modali per le prenotazioni
    const component = RiscaSDPerPagamentoModalComponent;

    // Creo la configurazione per l'apertura della modale
    const mConfig: IApriModalConfigsForClass = {
      callbacks,
      component,
      options: {
        windowClass: 'r-mdl-sd-per-pagamento',
        backdrop: 'static',
      },
      params: { dataModal },
    };

    // RISCA-ISSUES-35: è stato richiesto per le vie brevi (Silvia Cordero) di scaricare i dati prima di aprire la modale per averli disponibili all'apertura della modale e non lasciare la modale aperta e vuota, con lo spinner di caricamnto.
    // Attivo manualmente uno spinner
    this.showSpinner();
    // Recupero i parametri per lo scarico dati
    const ricerca: RicercaStatiDebitoriPagamentoVo = config.dataModal.ricerca;
    const paginazione: RiscaTablePagination = getDefaultPaginationPagaSDTable();

    this.ricercaSDxPagamentoPaginata(ricerca, paginazione).subscribe({
      next: (response: RicercaPaginataResponse<StatoDebitorioVo[]>) => {
        // Ho ricevuto risposta con i dati, modifico le informazioni da passare alla modale
        mConfig.params.dataModal.preLoadedSearch = response;
        // Richiamo la funzione di apertura della modale
        this._riscaModal.apriModal(new ApriModalConfigs(mConfig));
        // Chiuso lo spinner
        this.hideSpinner();
        // #
      },
      error: (e: RiscaServerError) => {
        // Ho ricevuto risposta con i dati, modifico le informazioni da passare alla modale
        mConfig.params.dataModal.preLoadedError = e;
        // Ho ricevuto errore, apro la modale passando l'errore generato
        this._riscaModal.apriModal(new ApriModalConfigs(mConfig));
        // Chiuso lo spinner
        this.hideSpinner();
        // #
      },
    });
  }

  /**
   * Funzione richiamata per la ricerca degli stati debitori per un pagamento.
   * @param ricerca RicercaStatiDebitoriPagamentoVo con i filtri di ricerca.
   * @param paginazione RiscaTablePagination con la configurazione per la paginazione dei dati.
   */
  private ricercaSDxPagamentoPaginata(
    ricerca: RicercaStatiDebitoriPagamentoVo,
    paginazione: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> {
    // Richiamo il servizio per lo scarico dati
    return this._datiContabili.getStatiDebitorixPagamentoPagination(
      ricerca,
      paginazione
    );
  }

  /**
   * ######################
   * GESTIONE DELLO SPINNER
   * ######################
   */

  /**
   * Funzione di comodo per l'avvio di uno spinner specifico.
   */
  private showSpinner() {
    // Recupero la chiave dello spinner specifico
    const k = this.SPINNER;
    // Lancio lo spinner
    this._riscaSpinner.show(k);
  }

  /**
   * Funzione di comodo per la chisura dello spinner specifico.
   */
  private hideSpinner() {
    // Recupero la chiave dello spinner specifico
    const k = this.SPINNER;
    // Lancio lo spinner
    this._riscaSpinner.hide(k);
  }
}
