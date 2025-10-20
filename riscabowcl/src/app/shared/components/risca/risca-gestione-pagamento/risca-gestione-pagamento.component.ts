import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { clone, compact } from 'lodash';
import { AssegnaPagamento } from '../../../../core/commons/vo/assegna-pagamento-vo';
import { DettaglioPagSearchResultVo } from '../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { DettaglioPagVo } from '../../../../core/commons/vo/dettaglio-pagamento-vo';
import { PagamentoVo } from '../../../../core/commons/vo/pagamento-vo';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaAzioniGestionePagamenti } from '../../../../features/pagamenti/component/gestione-pagamenti/utilities/gestione-pagamenti.enums';
import { IGestionePagamentiRouteParams } from '../../../../features/pagamenti/component/gestione-pagamenti/utilities/gestione-pagamenti.interfaces';
import { TipiRicerchePagamenti } from '../../../../features/pagamenti/component/ricerche-pagamenti/utilities/ricerche-pagamenti.enums';
import { DettaglioPagamentiService } from '../../../../features/pagamenti/service/dettaglio-pagamenti/dettaglio-pagamenti.service';
import { VersamentiService } from '../../../../features/pratiche/service/dati-contabili/versamenti/versamenti.service';
import { FASegmento } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { RiscaPagamentoBollettazioneComponent } from '../../../../shared/components/risca/risca-pagamento-bollettazione/risca-pagamento-bollettazione.component';
import {
  IPagamentoBollettazioneData,
  IVerificaImportiSuccess,
} from '../../../../shared/components/risca/risca-pagamento-bollettazione/utilities/risca-pagamento-bollettazione.interfaces';
import { RiscaUtilitiesComponent } from '../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import {
  AppActions,
  AppClaimants,
  RiscaInfoLevels,
  RiscaServerError,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IAlertConfigsFromCode } from '../../../utilities/interfaces/risca/risca-alert/risca-alert.interface';
import { FALivello } from '../risca-filo-arianna/utilities/rfa-level.class';

@Component({
  selector: 'risca-gestione-pagamento',
  templateUrl: './risca-gestione-pagamento.component.html',
  styleUrls: ['./risca-gestione-pagamento.component.scss'],
})
export class RiscaGestionePagamentoComponent
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** AppActions che definisce di che tipo di azioni verranno fatti sui dati. */
  @Input() modalita: AppActions;

  /** EventEmitter<AssegnaPagamento> che permette di sapere quando il pagamento è stato salvato. */
  @Output('onPagamentoAggiornato') _onPagamentoAggiornato$ =
    new EventEmitter<AssegnaPagamento>();
  /** EventEmitter<any> che permette di sapere quando è stato premuto il pulsante "Indietro". */
  @Output('onIndietro') _onIndietro$ = new EventEmitter<any>();

  /** Form di modifica del rimborso. */
  @ViewChild('riscaPagamentoBollettazione')
  riscaPagamentoBollettazione: RiscaPagamentoBollettazioneComponent;

  /** PagamentoVo con le informazioni del pagamento. */
  pagamento: PagamentoVo;
  /** DettaglioPagSearchResultVo[] con la lista dei possibili dati di dettaglio del pagamento. */
  dettagliPagamento: DettaglioPagSearchResultVo[];
  /** FALivello[] con la lista dei livelli da impostare all'interno del filo d'arianna. */
  faLivelli: FALivello[] = [];
  /** boolean che indica il comportamento di gestione del componente per alcuni flussi logici. */
  standAlone: boolean = true;
  /** AppClaimants con il chiamante definito all'interno del servizio. */
  appClaimant: AppClaimants;

  /**
   * Costruttore.
   */
  constructor(
    private _dettaglioPagamenti: DettaglioPagamentiService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    riscaMessages: RiscaMessagesService,
    private _versamenti: VersamentiService
  ) {
    // Lancio il super
    super(navigationHelper, riscaAlert, riscaMessages);
    // Richiamo il setup per il componente
    this.setupComponente();
  }

  /**
   * ngOnInit.
   */
  ngOnInit() {
    // Lancio l'init del componente
    this.initComponent();
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Rimuovo dal servizio le informazioni per il pagamento
    this._dettaglioPagamenti.resetDati();
    // Rimuovo la funzionalità del back del browser
    this._navigationHelper.deleteBrowerBack();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
    // Lancio il setup dei parametri per il componente recuperati dal servizio
    this.setupParametriServizio();
    // Lancio il setup per il filo d'arianna
    this.setupFiloArianna();
    // Setup funzionale per sovrascrivere il comportamento del back del browser
    this.setupBrowserRouting();
  }

  /**
   * Funzione che gestisce le logiche per il recupero/set dati per il componente dal servizio.
   */
  private setupParametriServizio() {
    // Recupero e assegno localmente le configurazioni
    this.pagamento = this._dettaglioPagamenti.pagamento;
    this.dettagliPagamento = this._dettaglioPagamenti.dettagliPagamento;
    this.faLivelli = this._dettaglioPagamenti.faLivelli;
    this.standAlone = this._dettaglioPagamenti.standAlone;
    this.appClaimant = this._dettaglioPagamenti.appClaimant;
  }

  /**
   * Funzione di setup che genera i livelli per il filo d'arianna.
   */
  private setupFiloArianna() {
    // Recupero i livelli per il filo d'arianna
    const livelli = this.faLivelli;
    // Creo un segmento con i livelli
    let segmento: FASegmento;
    segmento = this._riscaFA.creaSegmentoByLivelli(...livelli);
    // E' stato generato un segmento, lo aggiungo al filo
    this._riscaFA.aggiungiSegmento(segmento);
  }

  /**
   * Setta la funzione di override del back del browser
   */
  private setupBrowserRouting() {
    // Definisco una funzione custom per la gestione del back del browser
    const back = () => {
      // Richiamo la funzione locale
      this.onTornaIndietro();
    };
    // Registro una funzione custom per il back del browser
    this._navigationHelper.setBrowserBack(back);
    // Sovrascrivo la funzione di default del back con la funzione sopra definita
    this._navigationHelper.overrideBrowserBack();
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di init del componente.
   */
  private initComponent() {
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
   * ##########################################
   * FUNZIONI COLLEGATE AL COMPONENTE PAGAMENTI
   * ##########################################
   */

  /**
   * Funzione collegata all'evento onTornaIndietro del componente.
   * @param pagamentoSalvato boolean che definisce se l'oggetto è stato salvato.
   */
  onTornaIndietro(pagamentoSalvato?: boolean) {
    // Cancello il filo d'arianna
    this.rimuoviSegmentoDettaglioPagamenti();

    // Effettuo l'indietro tramite servizio
    if (this.claimantRicercaPagamenti) {
      // Torno alla ricerca
      this.tornaRicercaPagamenti(pagamentoSalvato);
      // #
    } else if (this.claimantPagamentoSD) {
      // Emetto l'evento per tornare indietro
      this.onIndietro();
    }
  }

  /**
   * Funzione che effettua un reindirizzamento verso la pagina della ricerca pagamenti.
   * @param pagamentoSalvato boolean che definisce se l'oggetto è stato salvato.
   */
  private tornaRicercaPagamenti(pagamentoSalvato?: boolean) {
    // Definisco i parametri per la pagina della pratica
    const state: IGestionePagamentiRouteParams = {
      navTarget: RiscaAzioniGestionePagamenti.ricerche,
      tipoRicercaPagamenti: TipiRicerchePagamenti.pagamenti,
    };

    // Salvo all'interno del servizio condiviso, il fatto che il pagamento è stato salvato
    this._dettaglioPagamenti.pagamentoSalvato = pagamentoSalvato;

    // Richiamo la funzione di back del servizio di navigazione
    this._navigationHelper.stepBack({ stateTarget: state });
  }

  /**
   * Funzione per la gestione dell'emissione dell'evento "INDIETRO".
   */
  private onIndietro() {
    // Emetto l'evento per "indietro"
    this._onIndietro$.emit();
  }

  /**
   * #####################
   * GESTIONE FILO ARIANNA
   * #####################
   */

  /**
   * Funzione di comodo che rimuove i segmenti partendo dalla tab ricerche.
   */
  private rimuoviSegmentoDettaglioPagamenti() {
    // Recupero i livelli del filo d'arianna
    const livelli = this.faLivelli;
    // Definisco una rray che conterrà la condizione se uno dei segmenti è nel filo d'arianna
    const segmenti: FASegmento[] = livelli?.map((l: FALivello) => {
      // Verifico se il livello è dentro un segmento del filo d'arianna
      return this._riscaFA.segmentoInFAByLivello(l);
    });
    // Verifico se esiste almeno un segmento all'interno dell'array
    let segmento: FASegmento;
    segmento = segmenti?.find((s: FASegmento) => s != undefined);

    // Verifico se è stato trovato un segmento
    if (segmento) {
      // Segmento di gestione trovato, elimino lui e i segmenti successivi
      this._riscaFA.rimuoviSegmento(segmento);
    }
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
    // Recupero le informazioni per i dettagli pagamenti
    const { dettagliPag, dettagliPagCancellati } = datiPag;
    let dettPag: DettaglioPagVo[];
    let dettPagCanc: DettaglioPagVo[];
    dettPag = this.recuperaDettaglioPagamento(dettagliPag);
    dettPagCanc = this.recuperaDettaglioPagamento(dettagliPagCancellati);

    // Assegno le singole informazioni
    assegnaPagamanto.pagamento = datiPag.pagamento;
    assegnaPagamanto.statiDebitori = datiPag.statiDebitoriSelezionati;
    assegnaPagamanto.dettagliPagamento = dettPag;
    assegnaPagamanto.dettagliPagamentoCancellare = dettPagCanc;

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
   * Funzione di comodo che verifica e ritorna una lista DettaglioPagVo da una lista DettaglioPagSearchResultVo.
   * @param list DettaglioPagSearchResultVo[] con la lista dati.
   * @returns DettaglioPagVo[] con la lista dati estratta.
   */
  private recuperaDettaglioPagamento(
    list: DettaglioPagSearchResultVo[]
  ): DettaglioPagVo[] {
    // Verifico l'input
    if (!list) {
      // Nessuna configurazione
      return [];
    }

    // Estraggo tutti i dati necessari dalla lista
    const listConvert = list.map((e: DettaglioPagSearchResultVo) => {
      // Creo una copia dell'oggetto per il dettaglio pagamento
      let dettPag: DettaglioPagVo;
      dettPag = clone(e?.dettaglio_pag);
      // Verifico se esiste effettivamente un oggetto
      if (dettPag) {
        // Aggiorno l'importo versato rispetto all'importo versato del pag search result
        dettPag.importo_versato = e.importo_versato;
        // #
      }
      // Ritorno l'oggetto dettaglio pagamento
      return dettPag;
    });

    // Ritorno la lista senza possibili elementi undefined
    return compact(listConvert);
  }

  /**
   * Funzione richiamata all'aggiornamento del pagamento in pagina.
   * @param assegnaPagamento AssegnaPagamento con i dati aggiornati per il pagamento.
   */
  private onPagamentoAggiornato(assegnaPagamento: AssegnaPagamento) {
    // Verifico l'input
    if (!assegnaPagamento) {
      // Niente logiche
      return;
    }

    // Visualizzo un alert con il messaggio d'aggiornamento
    const code = RiscaNotifyCodes.P001;
    // Lancio l'aggiornamento dell'alert mandando il codice
    this.alertConfigs = this._riscaAlert.createAlertFromMsgCode({ code });

    // Il pagamento è aggiornato
    this.pagamentoAggiornato(assegnaPagamento);
    // Navigo all'indietro
    this.tornaIndietro(true);
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
   * ####################
   * GESTIONE SUBMIT DATI
   * ####################
   */

  /**
   * Funzione invocata al click del pulsante per l'annullamento delle modifiche al pagamento.
   * La funzione invocherà il reset delle informazioni dipendentemente dalla modalità di gestione della modale.
   */
  resetPagamento() {
    // Resetto l'alert
    this.resetAlertConfigs();

    // Reset completo della form del pagamento.
    this.riscaPagamentoBollettazione.resetVersamento();
  }

  /**
   * Funzione invocata al click del pulsante per la conferma delle modifiche al pagamento.
   * La funzione invocherà il submit della form del pagamento per la validazione ed emissione dei dati.
   */
  aggiornaPagamento(importiVerificati: boolean = false) {
    // RISCA-757: Il salva lancia sempre prima la verifica importi. Se la verifica importi va a buon fine, allora richiamare questa funzione passando "importiVerificati" a true.
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
   * ##########################################
   * FUNZIONI PER LA LOGICA DI "TORNA INDIETRO"
   * ##########################################
   */

  /**
   * Funzione che attiva la route per ritornare alla pagina precedente.
   * @param pagamentoSalvato boolean che definisce se l'oggetto è stato salvato.
   */
  tornaIndietro(pagamentoSalvato?: boolean) {
    // Richiamo la funzione specifica di back
    this.onTornaIndietro(pagamentoSalvato);
  }

  /**
   * #########################################
   * FUNZIONI DI GESTIONE PAGAMENTO AGGIORNATO
   * #########################################
   */

  /**
   * Funzione che emette l'evento di pagamento aggiornato.
   * @param assegnaPagamento AssegnaPagamento con il risultato del salvataggio dati.
   */
  pagamentoAggiornato(assegnaPagamento?: AssegnaPagamento) {
    // Emetto l'evento di pagamento aggiornato
    this._onPagamentoAggiornato$.emit(assegnaPagamento);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica se il claimant è definito per 'ricerca pagamenti'.
   */
  get claimantRicercaPagamenti(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastClaimant(AppClaimants.ricercaPagamenti);
  }

  /**
   * Getter che verifica se il claimant è definito per 'pagamento sd'.
   */
  get claimantPagamentoSD(): boolean {
    // Verifico il caller
    return this.appClaimant == AppClaimants.pagamentoSD;
  }
}
