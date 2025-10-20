import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaAccertamentoComponent } from 'src/app/shared/components/risca/risca-accertamento/risca-accertamento.component';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { RiscaButtonConfig } from 'src/app/shared/utilities';
import { RiscaUtilitiesComponent } from '../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { CercaSoggettoDatiContabiliComponent } from '../../components/dati-contabili/cerca-soggetto-dc/cerca-soggetto-dc.component';
import { AccertamentoVo } from './../../../../core/commons/vo/accertamento-vo';
import { GestisciAccertamentoModalConsts } from './utilities/gestisci-accertamento-modal.consts';
import { IGestisciAccertamentoConfigs } from './utilities/gestisci-accertamento-modal.interfaces';

@Component({
  selector: 'gestisci-accertamento-modal',
  templateUrl: './gestisci-accertamento-modal.component.html',
  styleUrls: ['./gestisci-accertamento-modal.component.scss'],
})
export class GestisciAccertamentoModal
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  GRM_C = GestisciAccertamentoModalConsts;
  /** Titolo della modale. */
  modalTitle: string = this.GRM_C.MODAL_TITLE;

  /** Oggetto contenente i parametri per la modal. */
  @Input() dataModal: IGestisciAccertamentoConfigs;

  /** Form di inserimento/modifica dell'Accertamento. */
  @ViewChild('riscaAccertamento') riscaAccertamento: RiscaAccertamentoComponent;
  /** Form di ricerca del soggetto creditore. */
  @ViewChild('cambiaCreditore')
  cambiaCreditore: CercaSoggettoDatiContabiliComponent;

  /** Boolean che definisce se la modifica del creditore, mediante ricerca, è attualmente attiva. Pulsanti e form di ricerca verranno cambiati/visualizzati. */
  showCambiaCreditore: boolean = false;
  /** Boolean che permette la visualizzazione del pulsante per confermare il cambio creditore. */
  creditoreTrovato: boolean = false;

  /**
   * Costruttore.
   */
  constructor(
    public _activeModal: NgbActiveModal,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);
  }

  ngOnInit() {
    // Funzione di init del componente
    this.initComponente();
  }

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   * @param resource any che definisce le informazioni da ritornare per la chiusura del modal.
   */
  modalDismiss(resource?: any) {
    // Dismiss della modale
    this._activeModal.dismiss(resource);
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di init per il componente.
   */
  private initComponente() {
    // Lancio la funzioni di init dei controlli
    this.initControlli();
  }

  /**
   * Funzione di init per i controlli minimi del componente.
   */
  private initControlli() {
    // Recupero dalla configurazione le informazioni da verificare
    const { soggettiAbilitati, gruppiAbilitati } = this.dataModal || {};
    // Variabili di controllo
    const existSA = soggettiAbilitati != undefined;
    const existGA = gruppiAbilitati != undefined;

    // Verifico la configurazione
    if (!existSA || !existGA) {
      // Lancio un errore
      const m = 'No configuration for "gestisci accertamento"';
      throw new Error(m);
    }
  }

  /**
   * ##############################
   * GESTIONE MODIFICA ACCERTAMENTO
   * ##############################
   */

  /**
   * Funzione invocata al click del pulsante per l'annullamento delle modifiche all'accertamento.
   * La funzione invocherà il reset delle informazioni dipendentemente dalla modalità di gestione della modale.
   */
  onAnnullaAccertamento() {
    // Verifico la modalità della modale
    if (this.inserimento) {
      // Reset completo della form dell'accertamento.
      this.riscaAccertamento.onFormReset();
      // #
    } else if (this.modifica) {
      // Fa un restore delle informazioni dell'accertamento al loro stato iniziale, come se stessi aprendo in modifica senza aver toccato nulla.
      this.riscaAccertamento.onFormRestore();
      // #
    }
  }

  /**
   * Funzione invocata al click del pulsante per la conferma delle modifiche dell'accertamento.
   * La funzione invocherà il submit della form dell'accertamento per la validazione ed emissione dei dati.
   */
  onConfermaAccertamento() {
    // Lancio la sumbit della form
    this.riscaAccertamento.aggiungiAttivitaAccertamento();
  }

  /**
   * Cattura l'evento di modifica del rimborso.
   * La funzione consolida la modifica del rimborso.
   * @param accertamento AccertamentoVo modificato.
   */
  aggiungiAccertamento(accertamento: AccertamentoVo) {
    // Chiudo la modale
    this._activeModal.close(accertamento);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter che genera un RiscaButtonConfig che definisce la struttura di default del pulsante: ANNULLA.
   */
  get btnAnnulla(): RiscaButtonConfig {
    // Definisco una label di default
    const label = this.GRM_C.LABEL_ANNULLA;
    // Creo l'oggetto di configurazione
    const btnAnnulla: RiscaButtonConfig = { label };
    // Ritorno la configurazione
    return btnAnnulla;
  }

  /**
   * Getter che genera un RiscaButtonConfig che definisce la struttura di default del pulsante: CONFERMA.
   */
  get btnConferma(): RiscaButtonConfig {
    // Definisco una label di default
    const label = this.GRM_C.LABEL_CONFERMA;
    // Creo l'oggetto di configurazione
    const btnConferma: RiscaButtonConfig = { label };
    // Ritorno la configurazione
    return btnConferma;
  }

  /**
   * Getter che genera un RiscaButtonConfig che definisce la struttura di default del pulsante: ANNULLA RICERCA.
   */
  get btnAnnullaRicerca(): RiscaButtonConfig {
    // Definisco una label di default
    const label = this.GRM_C.LABEL_ANNULLA_RICERCA;
    // Creo l'oggetto di configurazione
    const btnAnnullaRicerca: RiscaButtonConfig = { label };
    // Ritorno la configurazione
    return btnAnnullaRicerca;
  }

  /**
   * Getter di comodo che prende l'accertamento dal dataModal.
   */
  get accertamento(): AccertamentoVo {
    return this.dataModal?.accertamento;
  }
}
