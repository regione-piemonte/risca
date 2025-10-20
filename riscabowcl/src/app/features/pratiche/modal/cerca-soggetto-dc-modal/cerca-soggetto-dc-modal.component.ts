import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LoggerService } from 'src/app/core/services/logger.service';
import { RiscaFormParentComponent } from 'src/app/shared/components/risca/risca-form-parent/risca-form-parent.component';
import { RiscaFormSubmitHandlerService } from 'src/app/shared/services/risca/risca-form-submit-handler.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import {
  IFormCercaSoggetto,
  RiscaButtonConfig,
} from '../../../../shared/utilities';
import { CercaSoggettoDatiContabiliComponent } from '../../components/dati-contabili/cerca-soggetto-dc/cerca-soggetto-dc.component';
import { ICercaSoggettoDCConfigs } from '../../components/dati-contabili/cerca-soggetto-dc/utilities/cerca-soggetto-dc.interfaces';
import { ICercaSoggettoDCModalConfigs } from './utilities/cerca-soggetto-dc-modal.interfaces';
import { CercaSoggettoDCModalConsts } from './utilities/cerca.soggetto-dc-modal.consts';

/**
 * Modal per la datiSoggetto di un titolare.
 */
@Component({
  selector: 'cerca-soggetto-dc-modal',
  templateUrl: './cerca-soggetto-dc-modal.component.html',
  styleUrls: ['./cerca-soggetto-dc-modal.component.scss'],
})
export class CercaSoggettoDCModalComponent
  extends RiscaFormParentComponent
  implements OnInit
{
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  CSDCM_C = CercaSoggettoDCModalConsts;

  /** Oggetto contenente i parametri per la modal. */
  @Input() dataModal: ICercaSoggettoDCModalConfigs;

  /** Form di ricerca del soggetto. */
  @ViewChild('cercaSoggetto')
  cercaSoggetto: CercaSoggettoDatiContabiliComponent;

  /** Boolean che permette la visualizzazione del pulsante per confermare il cambio soggetto. */
  soggettoTrovato: boolean = false;

  /**
   * Costruttore
   */
  constructor(
    public activeModal: NgbActiveModal,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages
    );
  }

  ngOnInit() {
    // Funzione di init del componente
    this.initComponente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
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
      const m =
        'No configuration for "abilitazione soggetti" or "abilitazione gruppi"';
      throw new Error(m);
    }
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione collegata all'evento di cambio risultato della ricerca.
   * Ritorna un booleano che specifica se la ricerca è da considerarsi OK (true) oppure fallita o mancante (false).
   * @param risultato boolean che definisce lo stato del risultato della ricerca.
   */
  onRicercaResult(risultato: boolean) {
    // Aggiorno il flag per il submit dei dati del soggetto secondo il risultato della ricerca
    this.soggettoTrovato = risultato;
  }

  /**
   * Conferma la scelta del soggetto trovato.
   */
  inserisciSoggetto() {
    // Conferma la scelta alla form
    this.cercaSoggetto.modalConfirm();
  }

  /**
   * Funzione collegata all'evento di click sul pulsante per il cambio soggetto.
   * L'operatore avrà ricercato e selezionato un nuovo soggetto come soggetto del rimborso, verrà quindi aggiornata la form dati del rimborso con il nuovo soggetto.
   * @param ricercaCreditore IFormCercaSoggetto con tutte le informazioni del nuovo soggetto da modificare.
   */
  onCambiaCreditore(ricercaCreditore: IFormCercaSoggetto) {
    // Chiudo la modale passando il risultato della ricerca al
    this.modalConfirm(ricercaCreditore);
  }

  /**
   * ################################
   * FUNZIONI PER GESTIONE DELLA FORM
   * ################################
   */

  resetRicerca() {
    // Resetto la ricerca
    this.cercaSoggetto.resetRicercaSoggetto();
    // Resetto la selezione
    this.soggettoTrovato = false;
  }

  /**
   * ##################################
   * FUNZIONI PER GESTIONE DELLA MODALE
   * ##################################
   */

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalDismiss() {
    // Dismiss della modale
    this.activeModal.dismiss();
  }

  /**
   * Funzione che effettua la close (e quindi la resolved) della modale.
   * @param ricerca IFormCercaSoggetto con le informazioni del soggetto trovato.
   */
  modalConfirm(ricerca: IFormCercaSoggetto) {
    // Close della modale
    this.activeModal.close({ data: ricerca });
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che recupera la title della modale.
   */
  get titleModal(): string {
    // Definisco una label di default
    const d = this.CSDCM_C.MODAL_TITLE;
    // Recupero la configurazione dalla modale
    const c = this.dataModal?.modalTitle;
    // Definisco la label da ritornare
    const label = c || d;

    // Ritorno la configurazione
    return label;
  }

  /**
   * Getter che genera un RiscaButtonConfig che definisce la struttura di default del pulsante: ANNULLA.
   */
  get btnAnnulla(): RiscaButtonConfig {
    // Definisco una label di default
    const d = this.CSDCM_C.LABEL_ANNULLA;
    // Recupero la configurazione dalla modale
    const c = this.dataModal?.labelBtnAnnulla;
    // Definisco la label da ritornare
    const label = c || d;

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
    const d = this.CSDCM_C.LABEL_CONFERMA;
    // Recupero la configurazione dalla modale
    const c = this.dataModal?.labelBtnConferma;
    // Definisco la label da ritornare
    const label = c || d;

    // Creo l'oggetto di configurazione
    const btnConferma: RiscaButtonConfig = { label };
    // Ritorno la configurazione
    return btnConferma;
  }

  /**
   * Getter di comodo che costruisce l'oggetto di configurazione da passare alla form di ricerca soggetto.
   */
  get configsRicercaSoggetto(): ICercaSoggettoDCConfigs {
    // Creo l'oggetto di configurazione e mappo i campi
    const configs: ICercaSoggettoDCConfigs = {
      soggettiAbilitati: this.dataModal?.soggettiAbilitati,
      gruppiAbilitati: this.dataModal?.gruppiAbilitati,
      description: this.dataModal?.description,
    };

    // Ritorno la configurazione
    return configs;
  }
}
