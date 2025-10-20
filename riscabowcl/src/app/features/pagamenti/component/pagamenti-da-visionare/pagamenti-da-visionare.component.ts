import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { clone } from 'lodash';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { AssegnaPagamento } from '../../../../core/commons/vo/assegna-pagamento-vo';
import { PagamentoVo } from '../../../../core/commons/vo/pagamento-vo';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaPagamentoBollettazioneComponent } from '../../../../shared/components/risca/risca-pagamento-bollettazione/risca-pagamento-bollettazione.component';
import {
  IPagamentoBollettazioneData,
  IVerificaImportiSuccess,
} from '../../../../shared/components/risca/risca-pagamento-bollettazione/utilities/risca-pagamento-bollettazione.interfaces';
import { RiscaUtilitiesComponent } from '../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaSpinnerService } from '../../../../shared/services/risca-spinner.service';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  RiscaInfoLevels,
  RiscaServerError,
  RiscaTablePagination,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IAlertConfigsFromCode } from '../../../../shared/utilities/interfaces/risca/risca-alert/risca-alert.interface';
import { VersamentiService } from '../../../pratiche/service/dati-contabili/versamenti/versamenti.service';
import { PagamentiDaVisionareService } from '../../service/pagamenti-da-visionare/pagamenti-da-visionare.service';
import { PagamentiDaVisionareConsts } from './utilities/pagamenti-da-visionare.consts';

@Component({
  selector: 'pagamenti-da-visionare',
  templateUrl: './pagamenti-da-visionare.component.html',
  styleUrls: ['./pagamenti-da-visionare.component.scss'],
})
export class PagamentiDaVisionareComponent
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** PagamentiDaVisionareConsts con le costanti definite per il componente. */
  PDV_C = new PagamentiDaVisionareConsts();

  /** AppActions che definisce di che tipo di azioni verranno fatti sui dati. */
  @Input() modalita: AppActions;
  /** Form di modifica del rimborso. */
  @ViewChild('riscaPagamentoBollettazione')
  riscaPagamentoBollettazione: RiscaPagamentoBollettazioneComponent;

  /** RiscaTablePagination con l'oggetto che gestisce la "paginazione" degli pagamenti da visionare. */
  paginazionePDV: RiscaTablePagination;

  /** PagamentoVo[] con la lista dei pagamenti da visionare. */
  pagamentiDaVisionare: PagamentoVo[];
  /** PagamentoVo con i dati del pagamento visualizzato.*/
  pagamentoDaVisionare: PagamentoVo;

  /**
   * Costruttore.
   */
  constructor(
    navigationHelper: NavigationHelperService,
    private _pagamentiDaVisionare: PagamentiDaVisionareService,
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService,
    private _riscaSpinner: RiscaSpinnerService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _versamenti: VersamentiService
  ) {
    // Lancio il super
    super(navigationHelper, riscaAlert, riscaMessages);
    // Lancio la funzione di setup
    this.setupComponente();
  }

  /**
   * ngOnInit.
   */
  ngOnInit(): void {
    // Lancio la funzione di init
    this.initComponente();
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione che raccoglie le funzionalità di init del componente.
   */
  private setupComponente() {
    // Lancio il recupero dati per la gestione dati
    this.setupPagamentiDaVisionare();
  }

  /**
   * Funzione che gestisce il flusso logico di download delle informazioni per i pagamenti da visionare.
   */
  private setupPagamentiDaVisionare() {
    // Lancio la funzione che recupera i pagamenti da visionare
    this.getPagamentiDaVisionare();
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione che raccoglie le funzionalità di init del componente.
   */
  private initComponente() {
    // Gestione della modalità componente
    this.initModalita();
  }

  /**
   * Funzione che gestisce l'init per la modalità del componente.
   */
  private initModalita() {
    // Verifico se la modalità è definito
    if (!this.modalita) {
      // Imposto la modalità di default
      this.modalita = AppActions.modifica;
    }
  }

  /**
   * #####################################
   * FUNZIONI PER LO SCARICO DEI PAGAMENTI
   * #####################################
   */

  /**
   * Funzione che lancia tutte le logiche di gestione per i pagamenti da visionare.
   * Questa funzione lancia in automatico tutto il flusso d'aggiornamento delle informazioni per la gestione dei pagamenti da visionare.
   */
  private getPagamentiDaVisionare() {
    // Lancio uno spinner dedicato
    this._riscaSpinner.show();

    // Verifico se esiste una paginazione già impostata
    if (!this.paginazionePDV) {
      // Assegno un default per la paginazione
      this.paginazionePDV = this.paginazionePDVDefault;
    }
    // Definisco la configurazione per la paginazione
    const p = this.paginazionePDV;

    // Lancio la funzione di recupero dati e gestisco la risposta del flusso
    this._pagamentiDaVisionare.getPagamentiDaVisionarePaginated(p).subscribe({
      next: (pagamentiRes: RicercaPaginataResponse<PagamentoVo[]>) => {
        // Richiamo la funzione di gestione dati
        this.onPDVInfo(pagamentiRes);
        // Chiudo lo spinner dedicato
        this._riscaSpinner.hide();
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
        // Chiudo lo spinner dedicato
        this._riscaSpinner.hide();
      },
    });
  }

  /**
   * Funzione di comodo che gestisce le informazioni recuperate per i pagamenti da visionare.
   * @param pagamentiRes RicercaPaginataResponse<PagamentoVo[]> con i dati per i pagamenti da visionare.
   */
  private onPDVInfo(pagamentiRes: RicercaPaginataResponse<PagamentoVo[]>) {
    // Assegno localmente le informazioni
    this.pagamentiDaVisionare = pagamentiRes?.sources ?? [];
    this.pagamentoDaVisionare = this.pagamentiDaVisionare[0];
    // Aggiorno l'oggetto di paginazione
    this._riscaUtilities.updatePaginazione(
      this.paginazionePDV,
      pagamentiRes?.paging
    );
  }

  /**
   * ############################
   * GESTIONE PAGINATORE DEI DATI
   * ############################
   */

  /**
   * Funzione che rimane in ascolto del cambio di paginazione.
   * @param newPaginazione RiscaTablePagination con la informazioni per la nuova paginazione.
   */
  cambioPDV(newPaginazione: RiscaTablePagination) {
    // Resetto il form del pagamento
    this.resetPagamento();

    // Aggiorno la paginazione
    this.paginazionePDV = newPaginazione;
    // Lancio la ricerca del dato
    this.getPagamentiDaVisionare();
  }

  /**
   * ##########################
   * FUNZIONE DI CLEAR DEI DATI
   * ##########################
   */

  /**
   * Funzione agganciata all'evento per il clear dei dati.
   * Il child sta effettuando delle operazioni con nuovi dati.
   */
  onClearData() {
    // Resetto gli alert
    this.resetAlertConfigs(this.alertConfigs);
  }

  /**
   * ####################
   * GESTIONE SUBMIT DATI
   * ####################
   */

  /**
   * Funzione invocata al click del pulsante per l'annullamento delle modifiche al pagamento.
   * La funzione invocherà il reset delle informazioni dipendentemente dalla modalità di gestione della modale.
   */
  resetPagamento() {
    // Reset completo della form del pagamento.
    this.riscaPagamentoBollettazione.resetVersamento();
  }

  /**
   * Funzione invocata al click del pulsante per la conferma delle modifiche al pagamento.
   * La funzione invocherà il submit della form del pagamento per la validazione ed emissione dei dati.
   */
  aggiornaPagamento(importiVerificati: boolean = false) {
    // RISCA-757: Il salva lancia sempre prima la verifica importi.
    // Se la verifica importi va a buon fine, allora richiamare questa funzione passando "importiVerificati" a true.
    if (importiVerificati) {
      // Lancio la submit del pagamento
      this.riscaPagamentoBollettazione.submitVersamento();
      // #
    } else {
      // Lancio la verifica degli importi, passando il flag per salvare i dati una volta verificati
      this.riscaPagamentoBollettazione.verificaImporti(true);
    }
  }

  /**
   * ##################################################
   * FUNZIONI DI GESTIONE DEL SALVATAGGIO DEL PAGAMENTO
   * ##################################################
   */

  /**
   * Funzione agganciata al submit del form dei dati per il pagamento.
   * @param datiPag IPagamentoBollettazioneData con le informazioni generate dal componente.
   */
  onPagamentoSubmit(datiPag: IPagamentoBollettazioneData) {
    // Resetto possibili alert di messaggistica
    this.resetAlertConfigs();

    // Recupero dall'oggetto le informazioni ritornate
    const assegnaPagamanto = new AssegnaPagamento();
    // Assegno le singole informazioni
    assegnaPagamanto.pagamento = datiPag.pagamento;
    assegnaPagamanto.statiDebitori = datiPag.statiDebitoriSelezionati;

    // Lancio l'aggiornamento dell'oggetto
    this._versamenti.updateAssegnaPagamento(assegnaPagamanto).subscribe({
      next: (assegnaPag: AssegnaPagamento) => {
        // Oggetto salvato correttamente
        this.onPagamentoAggiornato(assegnaPag);
        // #
      },
      error: (e: RiscaServerError) => {
        // Lancio la gestione dell'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione richiamata all'aggiornamento del pagamento in pagina.
   * @param assegnaPag AssegnaPagamento con i dati aggiornati per il pagamento.
   */
  private onPagamentoAggiornato(assegnaPag: AssegnaPagamento) {
    // Verifico l'input
    if (!assegnaPag) {
      // Niente logiche
      return;
    }

    // Effettuo il reset del form
    this.resetPagamento();

    // Visualizzo un alert con il messaggio d'aggiornamento
    const code = RiscaNotifyCodes.P001;
    // Lancio l'aggiornamento dell'alert mandando il codice
    this.alertConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
    this.alertConfigs.timeoutMessage = 5000;
    this.alertConfigs.persistentMessage = false;

    // Resetto la paginazione partendo dal default
    this.paginazionePDV = this.paginazionePDVDefault;
    // Rilancio la get per il pagamento da visionare
    this.getPagamentiDaVisionare();
  }

  /**
   * ##################################################
   * FUNZIONI DI GESTIONE DEL SALVATAGGIO DEL PAGAMENTO
   * ##################################################
   */

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
   * ##################################################
   * FUNZIONI DI GESTIONE PER LA VERIFICA DEGLI IMPORTI
   * ##################################################
   */

  /**
   * Funzione agganciata all'evento di verifica degli importi con successo.
   * @param verificaImporti IVerificaImportiSuccess con il codice di notifica da visualizzare.
   */
  onVerificaImporti(verificaImporti: IVerificaImportiSuccess) {
    // Estraggo dall'oggetto emesso le informazioni
    const { notifica, salvaDopoVerifica } = verificaImporti;

    // Controllo se il verifica importi deriva dalla pressione del pulsante SALVA
    if (salvaDopoVerifica) {
      // E' stato verificato dopo il SALVA, procedo con le logiche di salvataggio effettivo dei dati
      this.aggiornaPagamento(true);
      // #
    } else {
      // Definisco l'oggetto per la gestione dell'alert
      const alertFromCode: IAlertConfigsFromCode = { code: notifica };
      // Lancio l'aggiornamento dell'alert mandando il codice
      this.alertConfigs =
        this._riscaAlert.createAlertFromMsgCode(alertFromCode);
    }
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
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che genera un oggetto di paginazione di default.
   * @returns RiscaTablePagination con l'oggetto generato.
   */
  get paginazionePDVDefault(): RiscaTablePagination {
    // Genero un oggetto di paginazione come default
    const p: RiscaTablePagination = clone(this.PDV_C.DEFAULT_PAGINATION);
    // Ritorno l'oggetto
    return p;
  }

  /**
   * Getter che verifica le condizioni di visualizzazione per i pagamenti da visionare.
   * @returns boolean con il risutalto del check.
   */
  get checkPagamentiDaVisionare(): boolean {
    // Verifico se esistono pagamenti da visionare
    const checkPagamentiDV = this.pagamentiDaVisionare?.length > 0;
    // Ritorno il risultato del check
    return checkPagamentiDV;
  }
}
