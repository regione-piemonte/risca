import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PagamentoVo } from 'src/app/core/commons/vo/pagamento-vo';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { RiscaButtonConfig, RiscaInfoLevels } from 'src/app/shared/utilities';
import { DettaglioPagSearchResultVo } from '../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { RiscaPagamentoBollettazioneComponent } from '../../../../shared/components/risca/risca-pagamento-bollettazione/risca-pagamento-bollettazione.component';
import { IPagamentoBollettazioneData } from '../../../../shared/components/risca/risca-pagamento-bollettazione/utilities/risca-pagamento-bollettazione.interfaces';
import { RiscaPagamentoManualeComponent } from '../../../../shared/components/risca/risca-pagamento-manuale/risca-pagamento-manuale.component';
import { RiscaUtilitiesComponent } from '../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { PagamentiSDConsts } from '../../components/dati-contabili/pagamenti-sd/utilities/pagamenti-sd.consts';
import { VersamentiService } from '../../service/dati-contabili/versamenti/versamenti.service';
import { VersamentoModalConsts } from './utilities/pagamento-sd-modal.consts';
import {
  IConfirmModificaPag,
  IVersamentoConfigs,
} from './utilities/pagamento-sd-modal.interfaces';

@Component({
  selector: 'pagamento-sd-modal',
  templateUrl: './pagamento-sd-modal.component.html',
  styleUrls: ['./pagamento-sd-modal.component.scss'],
})
export class PagamentoSDModalComponent
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Oggetto contenente una serie di costanti per la gestione dei versamenti. */
  V_C = PagamentiSDConsts;
  /** Oggetto contenente una serie di costanti per la gestione della modale dei versamenti. */
  VM_C = VersamentoModalConsts;
  /** Titolo della modale. */
  modalTitle: string = this.VM_C.MODAL_TITLE;

  /** Oggetto contenente i parametri per la modal. */
  dataModal: IVersamentoConfigs;

  /** Form di inserimento/modifica del rimborso. */
  @ViewChild('riscaVersamentoManuale')
  riscaVersamentoManuale: RiscaPagamentoManualeComponent;
  /** Form di modifica del rimborso. */
  @ViewChild('riscaVersamentoNonManuale')
  riscaVersamentoNonManuale: RiscaPagamentoBollettazioneComponent;

  /** Boolean che definisce se la modifica del creditore, mediante ricerca, è attualmente attiva. Pulsanti e form di ricerca verranno cambiati/visualizzati. */
  showCambiaCreditore: boolean = false;
  /** Boolean che permette la visualizzazione del pulsante per confermare il cambio creditore. */
  creditoreTrovato: boolean = false;
  /** boolean che gestisce l'abilitazione del pulsante di salvataggio. */
  permettiSalvaPagamento: boolean = false;

  /**
   * Costruttore.
   */
  constructor(
    public _activeModal: NgbActiveModal,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService,
    private _versamenti: VersamentiService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);
  }

  ngOnInit() {
    // Lancio l'init del componente
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
   * Funzione comprensiva delle logiche di init del componente.
   */
  private initComponente() {
    // Init flag abilitazione salvataggio
    this.initPermettiSalvaPagamento();
  }

  /**
   * Funzione che gestisce le logiche per il flag di gestione di salvataggio pagamento.
   */
  private initPermettiSalvaPagamento() {
    // Verifico se il pagamento è manuale o della bollettazione
    if (this.manuale) {
      // Pagamento manuale, salva sempre attivo
      this.permettiSalvaPagamento = true;
      // #
    } else {
      // Pagamento da bollettazione, all'inizio il salva è disabilitato
      this.permettiSalvaPagamento = false;
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
    // Controllo se è un versamento manuale
    if (this.manuale) {
      // Ripristino il form
      this.riscaVersamentoManuale.onFormRestore();
      // #
    } else {
      // Ripristino il form
      this.riscaVersamentoNonManuale.onFormReset();
      // Disabilito il salvataggio
      this.permettiSalvaPagamento = false;
      // #
    }
  }

  /**
   * Funzione invocata al click del pulsante per la conferma delle modifiche al rimborso.
   * La funzione invocherà il submit della form del rimborso per la validazione ed emissione dei dati.
   */
  onConfermaRimborso() {
    // Controllo il tipo di rimborso
    if (this.manuale) {
      // Lancio la submit della form manuale
      this.riscaVersamentoManuale.aggiungiVersamento();
    } else {
      // Lancio la submit della form non manuale
      this.riscaVersamentoNonManuale.submitVersamento();
    }
  }

  /**
   * Cattura l'evento di modifica del rimborso.
   * La funzione consolida la modifica del rimborso.
   * @param pagamento PagamentoVo con i dati ritornati dal componente della form manuale.
   */
  modificaPagamentoManuale(pagamento: PagamentoVo) {
    // Compongo l'oggetto per il ritorno alla pagina
    const res: IConfirmModificaPag = { pagamento };

    // Chiudo la modale
    this._activeModal.close(res);
  }

  /**
   * Cattura l'evento di modifica del rimborso.
   * La funzione consolida la modifica del rimborso.
   * @param versamentoNM IVersamentoNonManualeData con i dati ritornati dai componenti delle form.
   */
  modificaPagamentoBollettazione(versamentoNM: IPagamentoBollettazioneData) {
    // Compongo l'oggetto per il ritorno alla pagina
    const res: IConfirmModificaPag = {
      pagamento: versamentoNM.pagamento,
      dettagliPagCancellati: versamentoNM.dettagliPagCancellati,
    };

    // Chiudo la modale
    this._activeModal.close(res);
  }

  /**
   * Funzione che gestisce i messaggi d'errore a seguito del submit della form.
   * @param messaggi string[] con i messaggi d'errore generati.
   */
  onPagamentoErrors(messaggi: string[]) {
    // Definisco variabili di comodo
    const a = this.alertConfigs;
    const msg = messaggi;
    const t = RiscaInfoLevels.danger;

    // Gestisco e segnalo l'errore
    this.aggiornaAlertConfigs(a, msg, t);
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
   * ##################################################
   * FUNZIONI DI GESTIONE PER LA VERIFICA DEGLI IMPORTI
   * ##################################################
   */

  /**
   * Funzione agganciata all'evento di verifica degli importi con successo.
   * @param code RiscaNotifyCodes con il codice di notifica da visualizzare.
   */
  onVerificaImporti(code: RiscaNotifyCodes) {
    // Lancio l'aggiornamento dell'alert mandando il codice
    this.alertConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
    // Abilito il pulsante di salvataggio
    this.permettiSalvaPagamento = true;
  }

  /**
   * Funzione agganciata all'evento di verifica degli importi con successo.
   * @param code RiscaNotifyCodes con il codice di notifica da visualizzare.
   */
  onVerificaImportiError(code: RiscaNotifyCodes) {
    // Lancio l'aggiornamento dell'alert mandando il codice
    this.alertConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
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
    const label = this.VM_C.LABEL_ANNULLA;
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
    const label = this.VM_C.LABEL_CONFERMA;
    // Creo l'oggetto di configurazione
    const btnConferma: RiscaButtonConfig = { label };
    // Ritorno la configurazione
    return btnConferma;
  }

  /**
   * Getter di comodo che prende il pagamento dal dataModal.
   * @returns PagamentoVo cone le informazioni recuperate.
   */
  get pagamento(): PagamentoVo {
    // Ritorno la configurazione del pagamento
    return this.dataModal?.pagamento;
  }

  /**
   * Getter di comodo per i dettagli del pagamento dal dataModal.
   * @returns DettaglioPagSearchResultVo[] cone le informazioni recuperate.
   */
  get dettagliPagamentoSearch(): DettaglioPagSearchResultVo[] {
    // Ritorno la configurazione dei dettagli del pagamento
    return this.dataModal?.dettagliPagamentoSearch;
  }

  /**
   * Getter che determina se il pagamento è manuale o meno
   */
  get manuale() {
    // Richiamo la funzione di verifica
    return this._versamenti.isPagamentoManuale(this.pagamento);
  }
}
