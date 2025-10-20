import { Injectable } from '@angular/core';
import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { cloneDeep, sumBy } from 'lodash';
import { Subject } from 'rxjs';
import { IRimborsoVo, RimborsoVo } from 'src/app/core/commons/vo/rimborso-vo';
import { SoggettoVo } from 'src/app/core/commons/vo/soggetto-vo';
import { RiscaModalService } from 'src/app/shared/services/risca/risca-modal.service';
import { ApriModalConfigs } from 'src/app/shared/utilities/classes/risca-modal/risca-modal.classes';
import { AnnualitaSDVo } from '../../../../../core/commons/vo/annualita-sd-vo';
import {
  AttivitaSDVo,
  IAttivitaSDVo,
} from '../../../../../core/commons/vo/attivita-sd-vo';
import {
  PraticaDTVo,
  PraticaEDatiTecnici,
  PraticaVo,
} from '../../../../../core/commons/vo/pratica-vo';
import { RataVo } from '../../../../../core/commons/vo/rata-vo';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { RiscaAlertService } from '../../../../../shared/services/risca/risca-alert.service';
import { RiscaLockPraticaService } from '../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaMessagesService } from '../../../../../shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  ICallbackDataModal,
  RiscaAlertConfigs,
  RiscaDatiContabili,
  RiscaInfoLevels,
  RiscaServerError,
  RiscaTablePagination,
} from '../../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IAlertConfigsFromCode } from '../../../../../shared/utilities/interfaces/risca/risca-alert/risca-alert.interface';
import { IAccertamenti } from '../../../components/dati-contabili/accertamenti/utilities/accertamenti.interfaces';
import { IRimborsi } from '../../../components/dati-contabili/rimborsi/utilities/rimborsi.interfaces';
import { GeneraliAmministrativiDilazione } from '../../../components/dati-contabili/stato-debitorio/generali-amministrativi-dilazione/utilities/generali-amministrativi-dilazione.interfaces';
import { IDatiAnagraficiSDUpd } from '../../../interfaces/dati-contabili/dati-anagrafici-sd.interfaces';
import { IDCSetFormValue } from '../../../interfaces/dati-contabili/dati-contabili-utility.interfaces';
import { IActionRejectSD } from '../../../interfaces/dati-contabili/dati-contabili.interfaces';
import { CalcoloInteressiModalComponent } from '../../../modal/calcolo-interessi-modal/calcolo-interessi-modal.component';
import { ICalcoloInteressiModalConfigs } from '../../../modal/calcolo-interessi-modal/utilities/calcolo-interessi-modal.interfaces';
import { SoggettoDatiAnagraficiUtilityService } from '../../dati-anagrafici/soggetto-utility.service';
import {
  annualitaPiuRecente,
  getRataPadre,
} from './dati-contabili-utility.functions';
import { AccertamentoVo } from '../../../../../core/commons/vo/accertamento-vo';

@Injectable({ providedIn: 'root' })
export class DatiContabiliUtilityService {
  /** PraticaVo con le informazioni della pratica alla quale sono associati i dati contabili. */
  private _praticaEDT: PraticaEDatiTecnici;
  /** StatoDebitorioVo che definisce le informazioni della riga selezionata dalla tabella degli stati debitori. */
  private _statoDebitorio: StatoDebitorioVo;
  /** StatoDebitorioVo che definisce le informazioni della riga selezionata dalla tabella degli stati debitori. */
  private _statoDebitorioInit: StatoDebitorioVo;

  /** Subject che permette la condivisione dell'evento per la gestione della sezione: StatiDebitori. */
  onStatiDebitori$ = new Subject<any>();
  /** Subject che permette la condivisione dell'evento per la gestione della sezione: Versamenti. */
  onVersamenti$ = new Subject<StatoDebitorioVo>();
  /** Subject che permette la condivisione dell'evento per la gestione della sezione: Rimborsi. */
  onRimborsi$ = new Subject<StatoDebitorioVo>();
  /** Subject che permette la condivisione dell'evento per la gestione della sezione: Accertamenti. */
  onAccertamenti$ = new Subject<StatoDebitorioVo>();
  /** Subject che permette la condivisione dell'evento per la gestione della sezione: StatoDebitorio. */
  onStatoDebitorio$ = new Subject<StatoDebitorioVo>();
  /** Subject che permette la condivisione dell'evento per la gestione della sezione: InserimentoStatoDebitorio. */
  onInserimentoStatoDebitorio$ = new Subject<StatoDebitorioVo>();
  /** Subject che permette la condivisione dell'evento per la gestione della sezione: Nap. */
  onNap$ = new Subject<StatoDebitorioVo>();
  // /** Subject che permette la condivisione dell'evento per la gestione della sezione: CalcolaInteressi. */
  // onCalcolaInteressi$ = new Subject<StatoDebitorioVo>();

  /** Subject che permette la condivisione di un errore con i componenti della sezione. */
  onSectionError$ = new Subject<RiscaServerError>();

  /** StatoDebitorioVo con l'oggetto dello stato debitorio che risulta selezionato nella scheda dei dati contabili. */
  private _datiContabiliSDSelezionato: StatoDebitorioVo;
  /** RiscaTablePagination con l'oggetto di paginazione memorizzato per le ricerche utente. */
  private _datiContabiliSDPaginazione: RiscaTablePagination;

  /**
   * Costruttore.
   */
  constructor(
    private _riscaAlert: RiscaAlertService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaMessages: RiscaMessagesService,
    private _riscaModal: RiscaModalService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _soggettoDAUtility: SoggettoDatiAnagraficiUtilityService
  ) {}

  /**
   * ################################
   * FUNZIONI BOTTONI PER NAVIGAZIONE
   * ################################
   */

  /**
   * Funzione che emette l'evento associato al click del pulsante Stati Debitori.
   */
  navigateStatiDebitori() {
    // Emetto l'evento per il cambio di sezione
    this.onStatiDebitori$.next();
  }

  /**
   * Funzione che emette l'evento associato al click del pulsante Versamenti.
   * @param statoDebitorio StatoDebitorioVo come parametro da passare ai componenti della sezione dei dati contabili.
   */
  navigateVersamenti(statoDebitorio?: StatoDebitorioVo) {
    // Emetto l'evento per il cambio di sezione
    this.onVersamenti$.next(statoDebitorio);
  }

  /**
   * Funzione che emette l'evento associato al click del pulsante Rimborsi.
   * @param statoDebitorio StatoDebitorioVo come parametro da passare ai componenti della sezione dei dati contabili.
   */
  navigateRimborsi(statoDebitorio?: StatoDebitorioVo) {
    // Emetto l'evento per il cambio di sezione
    this.onRimborsi$.next(statoDebitorio);
  }

  /**
   * Funzione che emette l'evento associato al click del pulsante Accertamenti.
   * @param statoDebitorio StatoDebitorioVo come parametro da passare ai componenti della sezione dei dati contabili.
   */
  navigateAccertamenti(statoDebitorio?: StatoDebitorioVo) {
    // Emetto l'evento per il cambio di sezione
    this.onAccertamenti$.next(statoDebitorio);
  }

  /**
   * Funzione che emette l'evento associato al click del pulsante Stato Debitorio.
   * @param statoDebitorio StatoDebitorioVo come parametro da passare ai componenti della sezione dei dati contabili.
   */
  navigateStatoDebitorio(statoDebitorio?: StatoDebitorioVo) {
    // Emetto l'evento per il cambio di sezione
    this.onStatoDebitorio$.next(statoDebitorio);
  }

  /**
   * Funzione che emette l'evento associato al click del pulsante Inserimento Stato Debitorio.
   * @param statoDebitorio StatoDebitorioVo come parametro da passare ai componenti della sezione dei dati contabili.
   */
  navigateInserimentoStatoDebitorio(statoDebitorio?: StatoDebitorioVo) {
    // Emetto l'evento per il cambio di sezione
    this.onInserimentoStatoDebitorio$.next(statoDebitorio);
  }

  /**
   * Funzione che emette l'evento associato al click del pulsante NAP.
   * @param statoDebitorio StatoDebitorioVo come parametro da passare ai componenti della sezione dei dati contabili.
   */
  navigateNap(statoDebitorio?: StatoDebitorioVo) {
    // Emetto l'evento per il cambio di sezione
    this.onNap$.next(statoDebitorio);
  }

  /**
   * Funzione che emette l'evento associato al click del pulsante Calcola Interessi.
   * @param statoDebitorio StatoDebitorioVo come parametro da passare ai componenti della sezione dei dati contabili.
   */
  navigateCalcolaInteressi(statoDebitorio?: StatoDebitorioVo) {
    // Creo le configurazioni
    const configs: ICalcoloInteressiModalConfigs = { statoDebitorio };
    // Creo le callback
    const callback: ICallbackDataModal = {};
    // Apro la modale di calcolo interessi
    this.apriModaleCalcoloInteressi(configs, callback);
  }

  /**
   * ###########################################
   * GESTIONE DELLA MODALE PER CALCOLO INTERESSI
   * ###########################################
   */

  /**
   * Funzione che apre un modale per la gestione di un'annualità.
   * @param dataModal IAnnualitaInsModalConfigs contenente i parametri da passare alla modale.
   * @param callbacks ICallbackDataModal contenente le configurazioni per le callback della modale.
   */
  apriModaleCalcoloInteressi(
    dataModal: ICalcoloInteressiModalConfigs,
    callbacks: ICallbackDataModal
  ) {
    // Variabili di configurazione fisse
    const component = CalcoloInteressiModalComponent;
    const options: NgbModalOptions = {
      windowClass: 'r-mdl-calcola-interessi',
      backdrop: 'static',
    };

    // Richiamo l'apertura del modale passando le configurazioni
    this._riscaModal.apriModal(
      new ApriModalConfigs({
        component,
        options: options,
        callbacks: callbacks,
        params: { dataModal },
      })
    );
  }

  /**
   * #####################################
   * FUNZIONI GESTIONE ERRORI NELLE PAGINE
   * #####################################
   */

  /**
   * Funzione che emette l'evento di generazione di un errore.
   * @param error RiscaServerError con il dettaglio dell'errore generato.
   */
  onSectionError(error: RiscaServerError) {
    // Emetto l'evento per la visualizzazione dell'errore
    this.onSectionError$.next(error);
  }

  /**
   * ####################################
   * FUNZIONI DI CHECK PER LA NAVIGAZIONE
   * ####################################
   */

  /**
   * Funzione di check per la navigazione tra le pagine dei dati contabili.
   * @param sezione RiscaDatiContabili che definisce la sezione alla quale si sta tentado d'accedere.
   * @param statoDebitorio StatoDebitorioVo che definisce l'oggetto dello stato debitorio da passare.
   * @returns boolean che indica se la navigazione è agibile o è da bloccare.
   */
  checkNavigazione(
    sezione: RiscaDatiContabili,
    statoDebitorio: StatoDebitorioVo
  ): boolean {
    // Verifico se lo stato debitorio esiste
    const existSD = statoDebitorio != undefined;

    // Gestisco i possibili casi per la navigazione
    switch (sezione) {
      case RiscaDatiContabili.calcolaInteressi:
      case RiscaDatiContabili.accertamenti:
      case RiscaDatiContabili.versamenti:
        // Per accedere alla scheda versamenti, deve essere selezionato uno stato debitorio
        return existSD;
      // #
      case RiscaDatiContabili.rimborsi:
        // Check specifico
        const cnR = this.checkNavigazioneRimborsi(statoDebitorio);
        return cnR;
      // #
      case RiscaDatiContabili.statoDebitorio:
        // Per accedere alla scheda stato debitorio, deve essere selezionato uno stato debitorio
        return existSD;
      // #
      case RiscaDatiContabili.inserimentoStatoDebitorio:
        // Check specifico
        const cnInsSD = this.checkNavigazioneInserisciSD();
        return cnInsSD;
      // #
      case RiscaDatiContabili.nap:
        // Check specifico
        const cnNapSD = this.checkNavigazioneNap(statoDebitorio);
        return cnNapSD;
      // #
      case RiscaDatiContabili.statiDebitori:
        // La scheda principale con la lista di tutti gli stati debitori è sempre accessibile
        return true;
      // #
    }

    // Return di sicurezza
    return false;
  }

  /**
   * Funzione di check dello stato debitorio per l'abilitazione alla navigazione sulla scheda: Rimborsi.
   * @param statoDebitorio StatoDebitorioVo contenente i dati da verificare per abilitare la scheda.
   * @returns boolean che definisce se la navigazione è permessa.
   */
  checkNavigazioneRimborsi(statoDebitorio: StatoDebitorioVo): boolean {
    // Verifico l'input
    if (!statoDebitorio) {
      // Niente configurazione da verificare
      return false;
    }

    // ATTENZIONE: RISCA-589 => Viene richiesto di rimuovere tutti i controlli per il pulsante rimborsi (per sicurezza, manteniamo il vecchio codice)
    return true;

    // // Recupero le informazioni dallo stato debitorio
    // const scSD: StatoContribuzioneSDVo = statoDebitorio.stato_contribuzione;
    // const aSD: AttivitaSDVo = statoDebitorio.attivita_stato_deb;

    // // Verifico che lo stato_contribuzione esista
    // if (!scSD) {
    //   // Niente configurazione da verificare
    //   return false;
    // }
    // // Verifico che l'attivitaSD esista
    // const existASD = aSD && aSD.cod_attivita_stato_deb != undefined;

    // // Variabile di comodo per i codici stati contribuzione
    // const codSC = RiscaCodStatiContribuzione;
    // // Funzione di comodo per il check dello stato contribuzione
    // const checkSC = (
    //   sc: StatoContribuzioneSDVo,
    //   cod: RiscaCodStatiContribuzione
    // ): boolean => {
    //   // Effettuo il check tra il codice dell'oggetto e il codice richiesto
    //   return sc?.cod_stato_contribuzione === cod;
    // };

    // // Definisco le condizioni per il check sui rimborsi
    // const condition: boolean[] = [
    //   checkSC(scSD, codSC.ECCEDENTE),
    //   checkSC(scSD, codSC.REGOLARE) && existASD,
    //   checkSC(scSD, codSC.INSUFFICIENTE) && existASD,
    //   checkSC(scSD, codSC.REGOLARIZZATO) && existASD,
    //   checkSC(scSD, codSC.INESIGIBILE) && existASD,
    // ];

    // // Il controllo è valido se una qualunque delle condizioni è vera
    // return condition.some((check: boolean) => check);
  }

  /**
   * Funzione di check dello stato debitorio per l'abilitazione alla navigazione sulla scheda: inserisci stato debitorio.
   * @param statoDebitorio StatoDebitorioVo contenente i dati da verificare per abilitare la scheda.
   * @returns boolean che definisce se la navigazione è permessa.
   */
  checkNavigazioneInserisciSD(): boolean {
    // Definisco il flag per la gestione dell'inserimento
    let checkISD = true;

    // Si può inserire uno stato debitorio se la pratica non è lockata
    checkISD = checkISD && this._riscaLockP.isCurrentUserLockingPratica();

    // Ritorno il valore del check
    return checkISD;
  }

  /**
   * Funzione di check dello stato debitorio per l'abilitazione alla navigazione sulla scheda: NAP.
   * @param statoDebitorio StatoDebitorioVo contenente i dati da verificare per abilitare la scheda.
   * @returns boolean che definisce se la navigazione è permessa.
   */
  checkNavigazioneNap(statoDebitorio: StatoDebitorioVo): boolean {
    // Verifico che esista lo stato debitorio
    if (!statoDebitorio) {
      // Niente configurazione
      return false;
    }

    // Estraggo il nap dallo stato debitorio
    const nap = statoDebitorio.nap;
    // Verifico che esista il nap
    if (nap != undefined) {
      // Il nap è definito
      return true;
      // #
    } else {
      // Niente nap
      return false;
    }
  }

  /**
   * ####################################################################
   * FUNZIONI DI COMODO PER LA GESTIONE DEI DATI ALL'INTERNO DEL SERVIZIO
   * ####################################################################
   */

  /**
   * Funzione di reset delle informazioni per la pratica del servizio.
   */
  resetPratica() {
    this._praticaEDT = undefined;
  }

  /**
   * Funzione di reset delle informazioni per lo stato debitorio del servizio.
   */
  resetStatoDebitorio() {
    this._statoDebitorio = undefined;
    this._statoDebitorioInit = undefined;
  }

  /**
   * Funzione di restore delle informazioni per lo stato debitorio del servizio.
   */
  restoreStatoDebitorio() {
    this._statoDebitorio = cloneDeep(this._statoDebitorioInit);
  }

  /**
   * Funzione di reset per le informazioni dei dati contabili relativi agli stati debitori per tabella e stato debitorio selezionato dall'utente.
   */
  resetDatiContabiliSD() {
    this._datiContabiliSDPaginazione = undefined;
    this._datiContabiliSDSelezionato = undefined;
  }

  /**
   * ##############################################
   * FUNZIONI DI UTILITY CONDIVISI CON I COMPONENTI
   * ##############################################
   */

  /**
   * Funzione di comodo che gestisce in aumatico le logiche a seguito del salvataggio di uno stato debitorio.
   * @param statoDebitorio StatoDebitorioVo con i dati salvati.
   * @param skipSave boolean come flag che permette di bypassare la logica di aggiornamento dei dati per lo stato debitorio. Per default è: false.
   * @returns RiscaAlertConfigs con la configurazione dell'alert per avvenuto salvataggio stato debitorio.
   */
  onStatoDebitorioSalvato(statoDebitorio: StatoDebitorioVo, skipSave: boolean = false): RiscaAlertConfigs {
    // Nella success devo visualizzare il messaggio P007.
    const code = RiscaNotifyCodes.P007;
    // Definisco la lista messaggi per l'utente
    const successMsgs = [];
    // Recupero il codice per la comunicazione utente
    successMsgs.push(this._riscaMessages.getMessage(code));
    // Definisco un oggetto di alert
    const alert = new RiscaAlertConfigs({
      type: RiscaInfoLevels.success,
      messages: successMsgs,
      persistentMessage: false,
      timeoutMessage: 5000,
    });

    // Verifico la gestione per lo skip del salvataggio dati
    if (!skipSave) {
      // Devo andare ad aggiornare lo stato debitorio dentro il servizio
      this._statoDebitorio = statoDebitorio;
      // #
    }

    // Ritorno la configurazione generata per l'alert
    return alert;
  }

  /**
   * ###########################################################
   * FUNZIONI DI AGGIORNAMENTO DEI DATI ALL'INTERNO DEL SERVIZIO
   * ###########################################################
   */

  /**
   * Funzione di supporto che gestisce la definizione dell'id pratica per lo stato debitorio.
   * @param idRiscossione number che identifica l'id della pratica.
   */
  updateIdRiscossione(idRiscossione: number) {
    // Aggiorno l'oggetto stato debitorio con l'id
    this._statoDebitorio.id_riscossione = idRiscossione;
  }

  /**
   * Funzione di supporto che gestisce l'aggiornamento dello stato debitorio dalla pagina dei rimborsi.
   * @param data IRimborsi contenente le informazioni del form.
   */
  updateRimborsi(data: IRimborsi) {
    // Prendo i dati della componente
    const { attivita_stato_deb, rimborsi } = data;
    // Converto i rimborsi per l'invio al BE
    const rimborsiConvertiti = rimborsi.map((r: RimborsoVo) => {
      // Genero l'oggetto dell'interfaccia
      const rimborso: IRimborsoVo = {
        id_rimborso: r.id_rimborso,
        id_stato_debitorio: r.id_stato_debitorio,
        id_gruppo_soggetto: r.id_gruppo_soggetto,
        tipo_rimborso: r.tipo_rimborso,
        soggetto_rimborso: this.sanificaSoggettorimborso(r.soggetto_rimborso),
        imp_rimborso: r.imp_rimborso,
        causale: r.causale,
        num_determina: r.num_determina,
        data_determina: this._riscaUtilities.convertMomentToServerDate(
          r.data_determina
        ),
        imp_restituito: r.imp_restituito,
        rimborso_sd_utilizzato: r.rimborso_sd_utilizzato,
      };
      // Creo il nuovo rimborso
      return new RimborsoVo(rimborso);
    });

    // Setto gli __id per i rimborsi che non hanno id
    rimborsiConvertiti?.forEach((r) => {
      // Controllo se c'è almeno uno dei due id
      const id_rimborsoUnd = r?.id_rimborso == undefined;
      const idUnd = r.__id == undefined;
      // Assegno l'id
      if (id_rimborsoUnd && idUnd) {
        r.__id = this._riscaUtilities.generateRandomId();
      }
    });

    // Converto l'attività stato debitorio
    const attivita: IAttivitaSDVo = {
      id_attivita_stato_deb: attivita_stato_deb?.id_attivita_stato_deb,
      cod_attivita_stato_deb: attivita_stato_deb?.cod_attivita_stato_deb,
      des_attivita_stato_deb: attivita_stato_deb?.des_attivita_stato_deb,
      id_tipo_attivita_stato_deb:
        attivita_stato_deb?.id_tipo_attivita_stato_deb,
    };
    // Costruisco l'oggetto
    const attivitaSDConvertito = new AttivitaSDVo(attivita);
    // Imposto i valori
    this._statoDebitorio.attivita_stato_deb = attivitaSDConvertito;
    this._statoDebitorio.rimborsi = rimborsiConvertiti;
  }

  /**
   * Funzione di supporto che gestisce l'aggiornamento di alcune informazioni dello stato debitorio.
   * @param accertamenti AccertamentoVo[] contenente le informazioni d'aggiornare sullo stato debitorio.
   */
  updateAccertamenti(accertamenti: AccertamentoVo[]) {
    // Aggiorno l'oggetto stato debitorio con gli accertamenti
    this._statoDebitorio.accertamenti = accertamenti;
  }
  
  /**
   * Funzione di supporto che gestisce l'aggiornamento di alcune informazioni dello stato debitorio.
   * @param attivitaSD AttivitaSDVo contenente le informazioni d'aggiornare sullo stato debitorio.
   */
  updateAttivitaStatoDebitorio(attivitaSD: AttivitaSDVo) {
    // Aggiorno l'oggetto stato debitorio con l'attività stato debitorio
    this._statoDebitorio.attivita_stato_deb = attivitaSD;
  }

  /**
   * Funzione di supporto che permette di preparare il soggetto rimborso per l'invio
   * @param soggetto SoggettoVo associato al rimborso
   */
  private sanificaSoggettorimborso(soggetto: SoggettoVo): SoggettoVo {
    // Effettuo una sanitizzazione per i dati
    this._riscaUtilities.sanitizeFEProperties(soggetto);
    // Converto i dati del gruppo
    soggetto =
      this._soggettoDAUtility.convertSoggettoGruppiToGruppiVo(soggetto);
    // Ritorno il soggetto
    return soggetto;
  }

  /**
   * Funzione di supporto che gestisce l'aggiornamento dello stato debitorio dalla pagina dei generali amministrativi dilazione.
   * @param data GeneraliAmministrativiDilazione contenente le informazioni del form.
   * @param rate RataVo[] con la lista di rate d'assegnare allo stato debitorio.
   */
  updateGeneraliAmminitrativiDilazione(
    data: GeneraliAmministrativiDilazione,
    rate: RataVo[]
  ) {
    // Controllo di esistenza
    if (!this._statoDebitorio) {
      throw new Error('Stato debitorio into service is undefined.');
    }

    // Eseguo l'aggiornamento dei campi dello stato debitorio
    this._statoDebitorio.num_richiesta_protocollo = data.numRichiestaProtocollo;
    this._statoDebitorio.data_richiesta_protocollo =
      this._riscaUtilities.convertNgbDateStructToMoment(
        data.dataRichiestaProtocollo
      );
    this._statoDebitorio.flg_restituito_mittente =
      data.restituitoMittente?.check ?? false;
    this._statoDebitorio.desc_periodo_pagamento = data.periodoPagamento;
    this._statoDebitorio.flg_invio_speciale =
      data.invioSpecialePostel?.check ?? false;
    this._statoDebitorio.flg_annullato = data.annullato?.check ?? false;
    this._statoDebitorio.desc_motivo_annullo = data.motivazione;
    this._statoDebitorio.imp_recupero_canone = data.annualitaPrecedente;
    this._statoDebitorio.canone_dovuto = data.canoneDovuto;
    this._statoDebitorio.flg_addebito_anno_successivo =
      data.addebitoAnnoSuccessivo?.check ?? false;
    this._statoDebitorio.imp_recupero_interessi = data.interessiMaturati;
    this._statoDebitorio.imp_spese_notifica = data.speseNotifica;
    this._statoDebitorio.imp_compensazione_canone = data.importoCompensazione;
    this._statoDebitorio.flg_dilazione = data.dilazione?.check ?? false;
    this._statoDebitorio.num_titolo = data.numeroTitolo;
    this._statoDebitorio.data_provvedimento =
      this._riscaUtilities.convertNgbDateStructToMoment(data.dataTitolo);
    this._statoDebitorio.note = data.noteSD;
    this._statoDebitorio.rate = rate;
    this._statoDebitorio.tipo_titolo = data.tipoTitolo;
  }

  /**
   * Funzione di supporto che gestisce l'aggiornamento dello stato debitorio dalla pagina dei dati anagrafici.
   * @param data IDatiAnagraficiSDUpd con i dati di aggiornamento dei ati anagrafici nello stato debitorio
   */
  updateDatiAnagrafici(data: IDatiAnagraficiSDUpd) {
    // Controllo di esistenza
    if (!this._statoDebitorio) {
      throw new Error('Stato debitorio into service is undefined.');
    }
    // Eseguo l'aggiornamento dei campi dello stato debitorio
    this._statoDebitorio.id_soggetto = data.idSoggetto;
    this._statoDebitorio.id_gruppo_soggetto = data.idGruppo;
    this._statoDebitorio.id_recapito_stato_debitorio = data.idRecapito;
  }

  /**
   * Funzione di supporto che gestisce l'aggiornamento dello stato debitorio dalla pagina delle annualità per i dati tecnici ambiente.
   * @param annualita AnnualitaSDVo[] con la lista d'informazioni d'aggiornare dentro lo stato debitorio.
   */
  updateAnnualita(annualita: AnnualitaSDVo[]) {
    // Controllo di esistenza
    if (!this._statoDebitorio) {
      throw new Error('Stato debitorio into service is undefined.');
    }
    // Controllo di esistenza
    if (!annualita) {
      // Se non esiste, ritorno
      return;
    }

    // Aggiorno la lista di annualità dello stato debitorio
    this._statoDebitorio.annualita_sd = annualita;
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY PER LA SEZIONE
   * ##################################
   */

  /**
   * Funzione che recupera da uno stato debitorio l'annualità più recente.
   * @param annualita AnnualitaSDVo[] dalla quale estrarre l'annualità più recente.
   * @returns AnnualitaSDVo con l'annualità più recente. Se non ci sono dati: undefined.
   */
  annualitaPiuRecente(annualita: AnnualitaSDVo[]): AnnualitaSDVo {
    // Richiamo la funzione di utility
    return annualitaPiuRecente(annualita);
  }

  /**
   * Funzione che recupera da una lista di rate, la rata che risulta senza rata padre (id_rata_sd_padre == null).
   * @param rate RataVo[] dalla quale estrarre la rata senza padre.
   * @returns RataVo con la rata senza padre. Se non esiste: undefined.
   */
  getRataPadre(rate: RataVo[]): RataVo {
    // Richiamo la funzione di utility
    return getRataPadre(rate);
  }

  /**
   * Funzione di comodo che aggiorna una rata all'interno dell'array rate.
   * Una volta avvenuta la modifica, ritorna la lista di rate aggiornata.
   * @param rate RataVo[] con la lista d'aggiornare.
   * @param rata RataVo con l'oggetto per aggiornare la lista.
   * @returns RataVo[] con la lista di elementi aggiornata.
   */
  updateRataInList(rate: RataVo[], rata: RataVo): RataVo[] {
    // Verifico l'input
    if (!rate || !rata) {
      // Ritorno la lista di rate
      return rate ?? [];
    }

    // Ricerco all'interno dell'array per stesso id
    const iRata = rate.findIndex((r: RataVo) => {
      // Match tra id degli oggetti
      return r.id_rata_sd == rata.id_rata_sd;
    });
    // Verifico se è stato trovato un id
    if (iRata != -1) {
      // Rata trovata, la sostituisco nell'array
      rate.splice(iRata, 1, rata);
    }

    // Ritorno la lista aggiornata
    return rate;
  }

  /**
   * Funzione che effettua il set dell'informazione per un form control di un form group, date le configurazioni dati.
   * Il set dei dati avverrà per referenza dell'oggetto mainForm.
   * @param configs IDCSetFormValue contenente le informazioni per il set dati.
   */
  setFormControlValue(configs: IDCSetFormValue) {
    // Verifico l'input
    if (!configs) {
      // Niente da configurare
      return;
    }

    // Recupero i dati per la configurazione
    const mf = configs.mainForm;
    const fck = configs.formControlKey;
    const v = configs.value;
    const o = configs.options;

    // Aggiorno il campo del form
    this._riscaUtilities.setFormValue(mf, fck, v, o);
  }

  /**
   * Funzione che calcola la somma di tutti i canoni delle annualità.
   * Se non vegono restituiti dati, ritorna undefined.
   * @param annualita AnnualitaSDVo[] contenente le informazioni delle annualità per il calcolo.
   * @returns number che definisce il totale del calcolo dei canoni.
   */
  canoneAnnualitaTotale(annualita: AnnualitaSDVo[]): number {
    // Verifico l'input
    if (!annualita) {
      // Niente configurazione
      return;
    }

    // Calcolo il totale del canone
    const canoneTotale = sumBy(annualita, (a: AnnualitaSDVo) => {
      // Recupero il canone, altrimenti 0
      return a.canone_annuo ?? 0;
    });

    // Ritorno il totale calcolato
    return canoneTotale;
  }

  /**
   * Funzione di comodo che gestisce le informazioni contenute dentro l'oggetto reject.
   * Se esistono dati ed esiste un codice, genero e ritorno un oggetto AlertConfigs.
   * @param reject IActionRejectSD con le informazioni di reject della modale passate.
   * @returns RiscaAlertConfigs con la gestione dell'alert.
   */
  alertConfigsReject(reject: IActionRejectSD): RiscaAlertConfigs {
    // Verifico l'input
    if (!reject || !reject.code) {
      // Mancano le configurazioni
      return undefined;
    }

    // Estraggo il codice dall'oggetto passato in input
    const { code } = reject;
    // Definisco l'oggetto di configurazione per la creazione dell'alert
    let configs: IAlertConfigsFromCode;
    configs = { code };

    // Genero e ritorno l'alert
    return this._riscaAlert.createAlertFromMsgCode(configs);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter per l'oggetto della pratica e dati tecnici.
   */
  get praticaEDT(): PraticaEDatiTecnici {
    return cloneDeep(this._praticaEDT);
  }

  /**
   * Setter per l'oggetto della pratica e dati tecnici.
   */
  set praticaEDT(praticaEDT: PraticaEDatiTecnici) {
    this._praticaEDT = cloneDeep(praticaEDT);
  }

  /**
   * Getter per l'oggetto della pratica.
   */
  get pratica(): PraticaVo {
    return this.praticaEDT?.pratica;
  }

  /**
   * Getter per l'id pratica.
   */
  get idPratica(): number {
    return this.pratica?.id_riscossione;
  }

  /**
   * Getter per i dati tecnici della pratica.
   */
  get datiTecnici(): PraticaDTVo {
    return this.praticaEDT?.datiTecnici;
  }

  /**
   * Getter per l'oggetto stato debitorio selezionato dalla tabella.
   */
  get statoDebitorio(): StatoDebitorioVo {
    return cloneDeep(this._statoDebitorio);
  }

  /**
   * Setter per l'oggetto stato debitorio selezionato dalla tabella.
   */
  set statoDebitorio(statoDebitorio: StatoDebitorioVo) {
    // Clono l'input
    const cloneSD = cloneDeep(statoDebitorio);
    this._statoDebitorio = cloneSD;
    this._statoDebitorioInit = cloneSD;
  }

  /**
   * Getter di comodo che recupera il codice utenza dallo stato debitorio.
   */
  get codiceUtenza(): string {
    // Tento di recuperare il codice utenza dallo stato debitorio
    const cuSD = this.statoDebitorio?.cod_utenza;
    // Verifico se esiste
    if (cuSD && cuSD != '') {
      // Ritorno il codice utenza dello stato debitorio
      return cuSD;
    }

    // Recupero la pratica dal servizio
    const pratica = this.pratica;
    // Recupero i codici riscossione
    const crProv = `${pratica?.cod_riscossione_prov ?? ''}`;
    const crProg = `${pratica?.cod_riscossione_prog ?? ''}`;
    // Per il codice utenza concateno i codici riscossione della pratica
    return `${crProv}${crProg}`;
  }

  /**
   * Getter di comodo per l'id del soggetto.
   */
  get idSoggettoSD() {
    // Ritorno, se esiste l'id_soggetto dello stato debitorio
    return this._statoDebitorio?.id_soggetto;
  }

  /**
   * Getter di comodo per l'id del gruppo.
   */
  get idGruppoSD() {
    // Ritorno, se esiste l'id_sgruppo_oggetto dello stato debitorio
    return this._statoDebitorio?.id_gruppo_soggetto;
  }

  /**
   * Getter di comodo per l'id del recapito dello stato debitorio.
   */
  get idRecapitoSD() {
    // Ritorno, se esiste l'id_recapito_stato_debitorio dello stato debitorio
    return this._statoDebitorio?.id_recapito_stato_debitorio;
  }

  /**
   * Getter per la paginazione utente salvata in sessione per gli stati debitori della tabella dei dati contaibili.
   * @returns RiscaTablePagination con l'oggetto di paginazione impostato alla ricerca utente.
   */
  get paginazioneSDUtente(): RiscaTablePagination {
    // Recupero il dato all'interno del servizio
    return cloneDeep(this._datiContabiliSDPaginazione);
    // #
  }

  /**
   * Setter per la paginazione utente sa salvare in sessione per gli stati debitori della tabella dei dati contaibili.
   * @param paginazione RiscaTablePagination con l'oggetto di paginazione impostato alla ricerca utente.
   */
  set paginazioneSDUtente(paginazione: RiscaTablePagination) {
    // Recupero il dato all'interno del servizio
    this._datiContabiliSDPaginazione = cloneDeep(paginazione);
    // #
  }

  /**
   * Getter per lo stato debitorio selezionato dall'utente salvato in sessione della tabella dei dati contaibili.
   * @returns StatoDebitorioVo con l'oggetto selezionato dall'utente.
   */
  get statoDebitorioUtente(): StatoDebitorioVo {
    // Recupero il dato all'interno del servizio
    return cloneDeep(this._datiContabiliSDSelezionato);
    // #
  }

  /**
   * Setter per lo stato debitorio selezionato dall'utente salvato in sessione della tabella dei dati contaibili.
   * @param statoDebitorio StatoDebitorioVo con l'oggetto selezionato dall'utente.
   */
  set statoDebitorioUtente(statoDebitorio: StatoDebitorioVo) {
    // Recupero il dato all'interno del servizio
    this._datiContabiliSDSelezionato = cloneDeep(statoDebitorio);
    // #
  }
}
