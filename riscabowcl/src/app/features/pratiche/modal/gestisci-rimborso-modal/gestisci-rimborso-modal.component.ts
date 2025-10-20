import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RimborsoVo } from 'src/app/core/commons/vo/rimborso-vo';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import {
  IFormCercaSoggetto,
  RiscaButtonConfig,
  RiscaInfoLevels,
} from 'src/app/shared/utilities';
import { RiscaRimborsoComponent } from '../../../../shared/components/risca/risca-rimborso/risca-rimborso.component';
import { RiscaUtilitiesComponent } from '../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { CercaSoggettoDatiContabiliComponent } from '../../components/dati-contabili/cerca-soggetto-dc/cerca-soggetto-dc.component';
import { ICercaSoggettoDCConfigs } from '../../components/dati-contabili/cerca-soggetto-dc/utilities/cerca-soggetto-dc.interfaces';
import { GestisciRimborsoModalConsts } from './utilities/gestisci-rimborso-modal.consts';
import { IGestisciRimborsoConfigs } from './utilities/gestisci-rimborso-modal.interfaces';

@Component({
  selector: 'gestisci-rimborso-modal',
  templateUrl: './gestisci-rimborso-modal.component.html',
  styleUrls: ['./gestisci-rimborso-modal.component.scss'],
})
export class GestisciRimborsoModal
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  GRM_C = GestisciRimborsoModalConsts;
  /** Titolo della modale. */
  modalTitle: string = this.GRM_C.MODAL_TITLE;

  /** Oggetto contenente i parametri per la modal. */
  @Input() dataModal: IGestisciRimborsoConfigs;

  /** Form di inserimento/modifica del rimborso. */
  @ViewChild('riscaRimborso') riscaRimborso: RiscaRimborsoComponent;
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
      const m =
        'No configuration for "abilitazione soggetti" or "abilitazione gruppi"';
      throw new Error(m);
    }
  }

  /**
   * ##########################
   * GESTIONE MODIFICA RIMBORSO
   * ##########################
   */

  /**
   * Funzione invocata al click del pulsante per l'annullamento delle modifiche al rimborso.
   * La funzione invocherà il reset delle informazioni dipendentemente dalla modalità di gestione della modale.
   */
  onAnnullaRimborso() {
    // Verifico la modalità della modale
    if (this.inserimento) {
      // Reset completo della form del rimborso.
      this.riscaRimborso.onFormReset();
      // #
    } else if (this.modifica) {
      // Fa un restore delle informazioni del rimborso al loro stato iniziale, come se stessi aprendo in modifica senza aver toccato nulla.
      this.riscaRimborso.onFormRestore();
      // #
    }
  }

  /**
   * Funzione invocata al click del pulsante per la conferma delle modifiche al rimborso.
   * La funzione invocherà il submit della form del rimborso per la validazione ed emissione dei dati.
   */
  onConfermaRimborso() {
    // Lancio la sumbit della form
    this.riscaRimborso.aggiungiAttivitaRimborso();
  }

  /**
   * Cattura l'evento di modifica del rimborso.
   * La funzione consolida la modifica del rimborso.
   * @param rimborso RimborsoVo modificato.
   */
  aggiungiRimborso(rimborso: RimborsoVo) {
    // Chiudo la modale
    this._activeModal.close(rimborso);
  }

  /**
   * Funzione collegata all'evento di click per la ricerca di un creditore.
   * Verrà visualizzata una nuova porzione di DOM, visualizzando la form di ricerca di un creditore.
   * La form del creditore verrà disabilitata.
   */
  onCercaCreditore() {
    // Visualizzo la parte di cambio creditore
    this.showCambiaCreditore = true;
    // Disabilito la form del rimborso
    this.riscaRimborso.disableFormRimborso();
  }

  /**
   * Funzione collegata all'evento di cambio risultato della ricerca.
   * Ritorna un booleano che specifica se la ricerca è da considerarsi OK (true) oppure fallita o mancante (false).
   * @param risultato boolean che definisce lo stato del risultato della ricerca.
   */
  onRicercaResult(risultato: boolean) {
    // Aggiorno il flag per il submit dei dati del creditore secondo il risultato della ricerca
    this.creditoreTrovato = risultato;
  }

  /**
   * Funzione collegata all'evento di click sul pulsante per l'annullamento ricerca del creditore.
   * L'operatore avrà annullato la ricerca, per cui bisogna re-impostare graficamente la sezione.
   */
  onAnnullaCambiaCreditore() {
    // Chiudo la sezione della ricerca creditore
    this.rimuoviCercaCreditore();
  }

  /**
   * Funzione collegata all'evento di click sul pulsante per il cambio creditore.
   * L'operatore avrà ricercato e selezionato un nuovo soggetto come creditore del rimborso, verrà quindi aggiornata la form dati del rimborso con il nuovo creditore.
   * @param ricercaCreditore IFormCercaSoggetto con tutte le informazioni del nuovo creditore da modificare.
   */
  onCambiaCreditore(ricercaCreditore: IFormCercaSoggetto) {
    // Verifico che la ricerca di ritorno esista
    if (ricercaCreditore) {
      // Aggiorno la form del rimborso
      this.riscaRimborso.aggiornaCreditoreByRicerca(ricercaCreditore);
      // #
    } else {
      // Chiedo alla componente figlia di inviarmi il soggetto creditore
      this.cambiaCreditore.modalConfirm();
    }
    // Chiudo la sezione della ricerca creditore e resetto il flag di ricerca
    this.rimuoviCercaCreditore();
    // Visualizzo un alert che indichi all'operatore che il creditore è stato modificato
    this.onCreditoreModificato();
  }

  /**
   * Funzione di comodo quando la sezione del cerca creditore deve essere rimossa.
   */
  private rimuoviCercaCreditore() {
    // Chiudo la sezione della ricerca creditore e resetto il flag di ricerca
    this.showCambiaCreditore = false;
    this.creditoreTrovato = false;
    this.riscaRimborso.enableFormRimborso();
  }

  /**
   * Funzione di comodo che visualizza l'alert di success, informando che il creditore è stato modificato sulla maschera.
   */
  private onCreditoreModificato() {
    // Recupero il codice del messaggio da visualizzare
    const code = RiscaNotifyCodes.P008;

    // Definisco i dati per aggiornare l'alert
    const a = this.alertConfigs;
    const m = [this._riscaMessages.getMessage(code)];
    const t = RiscaInfoLevels.success;

    // Imposto l'alert con la possibilità di essere chiuso
    a.allowAlertClose = true;

    // Aggiorno l'alert
    this.aggiornaAlertConfigs(a, m, t);
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
   * Getter che genera un RiscaButtonConfig che definisce la struttura di default del pulsante: CAMBIA CREDITORE.
   */
  get btnCambiaCreditore(): RiscaButtonConfig {
    // Definisco una label di default
    const label = this.GRM_C.LABEL_CAMBIA_CREDITORE;
    // Creo l'oggetto di configurazione
    const btnCambiaCreditore: RiscaButtonConfig = { label };
    // Ritorno la configurazione
    return btnCambiaCreditore;
  }

  /**
   * Getter di comodo che costruisce l'oggetto di configurazione da passare alla form di ricerca soggetto creditore.
   */
  get configsCambiaCreditore(): ICercaSoggettoDCConfigs {
    // Creo l'oggetto di configurazione e mappo i campi
    const configs: ICercaSoggettoDCConfigs = {
      soggettiAbilitati: this.dataModal?.soggettiAbilitati,
      gruppiAbilitati: this.dataModal?.gruppiAbilitati,
      description: this.dataModal?.description,
    };

    // Ritorno la configurazione
    return configs;
  }

  /**
   * Getter di comodo che prende il rimborso dal dataModal.
   */
  get rimborso(): RimborsoVo {
    return this.dataModal?.rimborso;
  }
}
