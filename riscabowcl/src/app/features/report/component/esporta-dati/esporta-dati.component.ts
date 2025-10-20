import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { ElaborazioneVo } from '../../../../core/commons/vo/elaborazione-vo';
import { EsportaDatiVo } from '../../../../core/commons/vo/esporta-dati-vo';
import { IReportLocationVo } from '../../../../core/commons/vo/report-location-vo';
import { ReportResultVo } from '../../../../core/commons/vo/report-result-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { HttpHelperService } from '../../../../core/services/http-helper/http-helper.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaUtilitiesComponent } from '../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaStampaPraticaService } from '../../../../shared/services/risca/risca-stampa/risca-stampa-pratica.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IRiscaAlertConfigs,
  RiscaAlertConfigs,
  RiscaInfoLevels,
  RiscaServerError,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IAlertConfigsFromCode } from '../../../../shared/utilities/interfaces/risca/risca-alert/risca-alert.interface';
import { AmbitoService } from '../../../ambito/services';
import { PagamentiService } from '../../../pagamenti/service/pagamenti/pagamenti.service';
import { EsportaDatiService } from '../../service/esporta-dati/esporta-dati.service';
import { ReportService } from '../../service/report/report.service';
import { IPollReportsStatus } from '../../service/report/utilities/report.interfaces';
import { RicercaEsportaDatiComponent } from '../risca-ricerca-rimborsi/ricerca-esporta-dati.component';
import { IFiltriRicercaEsportaDatiFE } from '../risca-ricerca-rimborsi/utilities/ricerca-esporta-dati.interfaces';
import { EsportaDatiConsts } from './utilities/esporta-dati.consts';
import { IEDResetAlerts } from './utilities/esporta-dati.interfaces';

@Component({
  selector: 'esporta-dati',
  templateUrl: './esporta-dati.component.html',
  styleUrls: ['./esporta-dati.component.scss'],
})
export class EsportaDatiComponent
  extends RiscaUtilitiesComponent
  implements OnInit, OnDestroy
{
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts();
  /** EsportaDatiConsts come classe di costanti del componente. */
  ED_C = new EsportaDatiConsts();
  /** RiscaNotifyCodes assegnata localmente e pubblica per poter gestire la configurazione sul template. */
  RISCA_NOTIFY_CODES = RiscaNotifyCodes;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** ViewChild collegato al form di ricerca. */
  @ViewChild('ricercaEsportaDati')
  ricercaEsportaDati: RicercaEsportaDatiComponent;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert che indica lo stato di disattivazione per le configurazioni dei report trasversali. */
  alertReportTrasversaliConfigs: RiscaAlertConfigs;
  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente per: Report Bilancio. */
  alertReportBilancioConfigs: RiscaAlertConfigs;
  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente per: Report Variazioni Competenza. */
  alertReportVariazioniCompetenzaConfigs: RiscaAlertConfigs;

  /** IFiltriRicercaEsportaDati con le informazioni per la ricerca/esportazione dati. */
  filtriED: IFiltriRicercaEsportaDatiFE;
  /** IFiltriRicercaEsportaDati con le informazioni per la ricerca/esportazione dati come configurazione iniziale. */
  filtriEDIniziali: IFiltriRicercaEsportaDatiFE;

  /** ReportResultVo[] con la lista di report disponibili allo scarico file. */
  reportsDisponibili: ReportResultVo[] = [];
  /** ReportResultVo[] con la lista di report che sono andati in errore durante la loro generazione. */
  reportsInErrore: ReportResultVo[] = [];
  /** ElaborazioneVo[] con la lista di elaborazioni con report collegati generati in sessioni precedenti. */
  elaborazioniOldSessions: ElaborazioneVo[] = [];

  /** Boolean che definisce se la form deve risultare bloccata per la configurazione di accesso all'app. */
  AEA_EDDisabled: boolean = false;

  /** Subscription valorizzato con l'avanzamento del polling dei dati per i report da creare. */
  private onReportsStatus$: Subscription;
  /** Subscription che permette di collegarsi all'evento di errore generato dalla stampa pratica pdf. */
  private onStampaError$: Subscription;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _ambito: AmbitoService,
    private _esportaDati: EsportaDatiService,
    private _httpHelper: HttpHelperService,
    navigationHelper: NavigationHelperService,
    private _pagamenti: PagamentiService,
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService,
    private _report: ReportService,
    private _riscaStampaP: RiscaStampaPraticaService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);
    // Lancio il setup del componente
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
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onReportsStatus$) {
        this.onReportsStatus$.unsubscribe();
      }
      if (this.onStampaError$) {
        this.onStampaError$.unsubscribe();
      }
    } catch (e) {}
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
    // Lancio la funzione di set per la chiave di abilitazioni elementi app
    this.setupAEAConfigs();
    // Lancio la funzione di setup per l'alert specifico che informa l'utente delle configurazioni che disabilitano i report trasversali
    this.setupAlertReportTrasversali();
    // Lancio il setup per le informazioni della lista dei report generati
    this.setupReportsData();
    // Lancio il set dei listener del componente
    this.setupListeners();
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  private setupAEAConfigs() {
    // Recupero la chiave per la configurazione della form
    const edKey = this.AEAK_C.ESPORTA_DATI;
    // Recupero la configurazione della form dal servizio
    this.AEA_EDDisabled = this._accessoElementiApp.isAEADisabled(edKey);
  }

  /**
   * Funzione di setup per l'alert informativo sulle abilitazioni dei report trasversali.
   */
  private setupAlertReportTrasversali() {
    // Creo la configurazione specifica per la generazione dell'alert
    const c: IAlertConfigsFromCode = { code: RiscaNotifyCodes.I032 };
    // Genero l'alert partendo dal codice
    let alertRT: RiscaAlertConfigs;
    alertRT = this._riscaAlert.createAlertFromMsgCode(c);

    // Modifico alcune configurazioni specifiche
    alertRT.allowAlertClose = false;
    alertRT.persistentMessage = true;

    // Assegno l'oggetto alla variabile del componente
    this.alertReportTrasversaliConfigs = alertRT;
  }

  /**
   * Funzione di setup per i report che hanno già ottenuto un risposta per la loro generazione.
   */
  private setupReportsData() {
    // Recupero dal servizio la lista già presente dei report disponibili
    this.reportsDisponibili = this._report.reportsCompleted;
    // Recupero dal servizio la lista già presente dei report in errore
    this.reportsInErrore = this._report.reportsErrors;
    // Recupero dal servizio la lista di elaborazioni con report precedenti
    this.elaborazioniOldSessions = this._report.elaborazioniReports;
  }

  /**
   * Funzione di setup per i listener del componente.
   */
  private setupListeners() {
    // Mi aggancio all'evento di completamento dei report
    this.onReportsStatus$ = this._report.onPollingReportsSuccess$.subscribe({
      next: (reportsStatus: IPollReportsStatus) => {
        // Gestisco le informazioni ritornate dal polling
        this.onReportsStatus(reportsStatus);
      },
    });

    // Mi aggancio all'evento di errore stampa pdf pratica
    this.onStampaError$ = this._riscaStampaP.onStampaError$.subscribe({
      next: (e: RiscaServerError) => {
        // Gestisco l'errore generato
        this.onServiziError(e);
      },
    });
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  private initComponent() {
    // Gestione logiche
  }

  /**
   * ###################################
   * FUNZIONI COLLEGATE AGLI ASCOLTATORI
   * ###################################
   */

  /**
   * Funzione invocata nel momento in cui viene intercettato l'evento di polling completato.
   */
  private onReportsStatus(reportsStatus: IPollReportsStatus) {
    // Verifico l'input
    if (!reportsStatus) {
      // Niente configurazione
      return;
    }

    // Assegno localmente le informazioni sullo stato dei report
    this.reportsDisponibili = reportsStatus.completed ?? [];
    this.reportsInErrore = reportsStatus.errors ?? [];
  }

  /**
   * ################################
   * FUNZIONI DEI PULSANTI PRINCIPALI
   * ################################
   */

  /**
   * Funzione collegata al pulsante ANNULLA della pagina.
   * La funzione prevede la pulizia dei dati sia per la tabella, sia per il form di ricerca.
   */
  annullaRicerca() {
    // Resetto gli alert
    this.resetAlertConfigs(this.alertConfigs);

    // Tramite referenza, chiedo il reset dei filtri
    this.ricercaEsportaDati.onFormReset();
  }

  /**
   * Funzione collegata al pulsante CERCA della pagina.
   * La funzione prevede la richiesta di submit dei filtri da parte del sotto componente di ricerca.
   */
  avviaRicerca() {
    // Tramite referenza, chiedo il submit dei filtri
    this.ricercaEsportaDati.onFormSubmit();
  }

  /**
   * ##################################################
   * FUNZIONI COLLEGATE AGLI EVENTI DEL FORM DI RICERCA
   * ##################################################
   */

  /**
   * Funzione collegata all'evento: onFormSubmit; del componente di ricerca.
   * La funzione riceverà i filtri di ricerca generati dal componente.
   * @param filtri IFiltriRicercaEsportaDati con le informazioni dei filtri generati dal componente.
   */
  onFiltriReady(filtri: IFiltriRicercaEsportaDatiFE) {
    // Verifico l'input
    if (!filtri) {
      // Nessuna configurazione
      return;
    }

    // Aggiorno i filtri di locali
    this.filtriED = filtri;
    this.filtriEDIniziali = filtri;
    // Converto le informazioni per generare un oggetto EsportaDatiVo
    let dataDa: string;
    dataDa = this._riscaUtilities.convertNgbDateStructToServerDate(
      filtri?.dataDa
    );
    let dataA: string;
    dataA = this._riscaUtilities.convertNgbDateStructToServerDate(
      filtri?.dataA
    );
    // Genero l'oggetto effettivo
    const esportaDati = new EsportaDatiVo({ dataDa, dataA });
    // Il tipo elaborazione è già un oggetto FE
    esportaDati.tipoElaboraReport = filtri?.tipoElaboraReport;

    // // Lancio la funzione di ricerca dati
    this.esportaDati(esportaDati);
  }

  /**
   * Funzione collegata all'evento: onFormErrors; del componente di ricerca.
   * La funzione riceverà i messaggi d'errore generati dal componente.
   * @param errori string[] con la lista degli errori generati dal form.
   */
  onFiltriErrors(errori: string[]) {
    // Segnalo gli errori lanciati dal form
    const a = this.alertConfigs;
    const m = errori;
    const t = RiscaInfoLevels.danger;
    // Aggiorno l'alert con gli errori
    this._riscaAlert.aggiornaAlertConfigs(a, m, t);
  }

  /**
   * Funzione collegata all'evento: onEmitServiziError; del componente di ricerca.
   * La funzione riceverà l'errore generato dal componente figlio dalle chiamate al server.
   * @param e RiscaServerError con l'errore generato.
   */
  onEmitServiziError(e: RiscaServerError) {
    // Richiamo la funzione di gestione degli errori
    this.onServiziError(e);
  }

  /**
   * ##########################################
   * FUNZIONI DI GESTIONE PER ESPORTAZIONE DATI
   * ##########################################
   */

  /**
   * Funzione funzione che lancia la funzione d'esportazione dati, con generazione dei documenti.
   * @param esportaDati EsportaDatiVo con le informazioni dei filtri generati dal componente.
   */
  esportaDati(esportaDati: EsportaDatiVo) {
    // Resetto gli alert
    this.resetAlerts({
      generic: true,
      reportBilancio: true,
      reportTrasversali: true,
    });

    // Lancio la funzione di verifica per il report bilancio
    const validRB: boolean = this.controllaReportBilancio(esportaDati);
    // Verifico se devo bloccare il flusso
    if (!validRB) {
      // Blocco il flusso
      return;
    }

    // Lancio la funzione che gestirà le casistiche sul tipo di esportazione dati
    this._esportaDati.createReportEsportaDati(esportaDati).subscribe({
      next: (reportLocation: IReportLocationVo) => {
        // Visualizzo il messaggio di report gestito
        this.onCreaReport();
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione errori servizi
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione che verifica la validità dei dati per: Report Bilancio.
   * La funzione, di per se, non ha controlli bloccanti e ritornerà sempre "true".
   * Gestirà invece la segnalazione per alcuni controlli.
   * @param esportaDati EsportaDatiVo con le informazioni da verificare.
   */
  private controllaReportBilancio(esportaDati: EsportaDatiVo): boolean {
    // Verifico se esiste l'input
    if (!esportaDati || !esportaDati.elaboraReportBilancio) {
      // Non ci sono dati o non è report bilancio, ma considero valida la gestione
      return true;
    }

    // Definisco un array per contenere differenti segnalazioni
    const segnalazioni: RiscaNotifyCodes[] = [];

    // Verifico se ci sono segnalazioni per l'utente
    const verificaAnno: RiscaNotifyCodes =
      esportaDati.checkDataPerReportBilancio();
    // Verifico se è stato ritornato un codice
    if (verificaAnno) {
      // E' stato generato un codice, lo aggiungo alla lista
      segnalazioni.push(verificaAnno);
      // #
    }

    // Verifico se ci sono segnalazioni
    if (segnalazioni.length > 0) {
      // Ci sono segnalazioni, creo un array per i messaggi
      const messaggi: string[] = segnalazioni.map((s: RiscaNotifyCodes) => {
        // Recupero il messaggio x il codice
        return this._riscaMessages.getMessage(s);
        // #
      });
      // Definisco le informazioni per la visualizzazione dell'alert
      const configs: IRiscaAlertConfigs = {
        allowAlertClose: true,
        messages: messaggi,
        type: RiscaInfoLevels.info,
      };
      // Visalizzo l'alert per il report bilancio
      this.alertReportBilancioConfigs = new RiscaAlertConfigs(configs);
    }

    // Ritorno la validazione dei controlli
    return true;
  }

  /**
   * Funzione invocata alla conclusione della funzione di creazione report.
   * La funzione gestirà la segnalazione verso l'utente di avvenuta presa in carico della creazione del report con i dati richiesti.
   */
  private onCreaReport() {
    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.P009;
    // Compongo l'oggetto per generare l'alert
    const c: IAlertConfigsFromCode = { code };
    // Genero l'alert sulla base del codice da visualizzare
    const newAlert = this._riscaAlert.createAlertFromMsgCode(c);

    // Modifico alcune informazioni della configurazione dell'alert
    newAlert.allowAlertClose = true;
    newAlert.timeoutMessage = 5000;
    newAlert.persistentMessage = false;

    // Assegno l'oggetto generato all'alert della pagina
    this.alertConfigs = newAlert;
  }

  /**
   * ####################################
   * FUNZIONI DI GESTIONI PULSANTI FOOTER
   * ####################################
   */

  /**
   * Funzione collegata al pulsante "CREA REPORT".
   * Verranno eseguite le funzioni per la gestione della creazione report.
   */
  creaReport() {
    // Richiamo il submit per il componente form
    this.ricercaEsportaDati.onFormSubmit();
  }

  /**
   * Funzione attivata al click sul nome di un report disponibile al download.
   * @param report ReportResultVo con i dati del report da scaricare.
   */
  onReportClick(report: ReportResultVo) {
    // Verifico l'input
    if (!report) {
      // Niente configurazione
      return;
    }

    // Recupero dall'oggetto report le informazioni di scarico
    const { report_url, file_name_ext } = report;
    // Tento di recuperare l'elaborazione dall'oggetto report
    let elabora: ElaborazioneVo;
    elabora = report.elabora;

    // Rimuovo dall'elaborazione scaricata il valore per la proprietà nome_file_generato
    elabora.nome_file_generato = undefined;
    // Per rimuovere l'elaborazione dalla lista di report scaricabili, lancio l'update non passando il nome_file_generato
    this._pagamenti.updateElaborazione(elabora).subscribe({
      next: (elaborazione: ElaborazioneVo) => {
        // Scarico il file aprendo in una nuova tab il link
        this._httpHelper.downloadResource(report_url, file_name_ext);

        // Aggiorno i report disponibili tramite servizio
        this._report.reportScaricato(report, true);
        // Tento di rimuovere l'oggetto in input dalla lista
        this._report.removeReportFromList(this.reportsDisponibili, report);
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione attivata al click sul nome di un report in errore.
   * @param report ReportResultVo con i dati del report in errore.
   */
  onReportErrorClick(report: ReportResultVo) {
    // Verifico l'input
    if (!report) {
      // Niente configurazione
      return;
    }

    // Aggiorno i report in errore tramite servizio
    this._report.reportInErroreVisualizzato(report, true);
    // Tento di rimuovere l'oggetto in input dalla lista
    this._report.removeReportFromList(this.reportsInErrore, report);
  }

  /**
   * Funzione attivata al click sul nome di un report di una vecchia sessione disponibile al download.
   * @param elaborazioneOS ElaborazioneVo con i dati del report da scaricare.
   */
  onElaborazioneOldSessionsClick(elaborazioneOS: ElaborazioneVo) {
    // Verifico l'input
    if (!elaborazioneOS || !elaborazioneOS.nome_file_generato) {
      // Niente configurazione
      return;
    }

    // Recupero l'id elaborazione per generare l'url del download
    const { id_elabora } = elaborazioneOS;
    // Richiamo la funzione specifica che genererà l'url completo e sostituendo il valore alla variabile nome_file_generato
    this._report
      .downloadReportOldSessions(id_elabora)
      .pipe(
        tap((elaborazioneDownload: ElaborazioneVo) => {
          // Recupero dall'oggetto elaborazione le informazioni di scarico
          let { nome_file_generato, nomeFile } = elaborazioneDownload;
          // Scarico il file aprendo in una nuova tab il link
          this._httpHelper.downloadResource(nome_file_generato, nomeFile);

          // Aggiorno i report/elaborazione disponibili tramite servizio
          this._report.reportElaborazioneScaricato(elaborazioneOS, true);
          // Tento di rimuovere l'oggetto in input dalla lista
          this._report.removeElaborazioniReportsFromList(
            this.elaborazioniOldSessions,
            elaborazioneOS
          );
          // #
        }),
        switchMap((elaborazioneDownloaded: ElaborazioneVo) => {
          // Rimuovo dall'elaborazione scaricata il valore per la proprietà nome_file_generato
          elaborazioneDownloaded.nome_file_generato = undefined;
          // Per rimuovere l'elaborazione dalla lista di report scaricabili, lancio l'update non passando il nome_file_generato
          return this._pagamenti.updateElaborazione(elaborazioneDownloaded);
          // #
        })
      )
      .subscribe({
        next: (elaborazione: ElaborazioneVo) => {
          // Per ora non è necessaria la gestione dati
        },
        error: (e: RiscaServerError) => {
          // Gestisco l'errore
          this.onServiziError(e);
        },
      });

    // Per rimuovere l'elaborazione dalla lista di report scaricabili, lancio l'update non passando il nome_file_generato
    this._pagamenti.updateElaborazione(elaborazioneOS).subscribe({
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione che resetta gli alert del componente.
   * @param target IResetAlerts con l'indicazione specifica su quali alert resettare. Se non definito, tutti gli altert verranno resettati.
   */
  private resetAlerts(target?: IEDResetAlerts) {
    // Verifico l'input
    target = target ?? {
      generic: true,
      reportBilancio: true,
      reportTrasversali: true,
      reportVariazioniCompetenza: true,
    };

    // Estraggo dall'input la configurazione per i campi
    const resetGeneric = target.generic;
    const resetReportBilancio = target.reportBilancio;
    const resetReportTrasversali = target.reportTrasversali;
    const resetReportVariazioniCompetenza = target.reportVariazioniCompetenza;

    // Gestisco il reset degli alert
    if (resetGeneric) {
      this.resetAlertConfigs(this.alertConfigs);
    }
    if (resetReportBilancio) {
      this.resetAlertConfigs(this.alertReportTrasversaliConfigs);
    }
    if (resetReportTrasversali) {
      this.resetAlertConfigs(this.alertReportBilancioConfigs);
    }
    if (resetReportVariazioniCompetenza) {
      this.resetAlertConfigs(this.alertReportVariazioniCompetenzaConfigs);
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   * @returns boolean con il risultato del check.
   */
  get alertReportBilancioCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertReportBilancioConfigs);
  }

  /**
   * Getter di comodo che verifica se estono report disponibili allo scarico.
   * @returns boolean con il risultato del check.
   */
  get checkReportsDisponibili(): boolean {
    // Verifico e ritorno il controllo
    return this.reportsDisponibili?.length > 0;
  }

  /**
   * Getter di comodo che verifica se estono report andati in errore.
   * @returns boolean con il risultato del check.
   */
  get checkReportsInErrore(): boolean {
    // Verifico e ritorno il controllo
    return this.reportsInErrore?.length > 0;
  }

  /**
   * Getter di comodo che verifica se estono elaborazioni con report generati in sessioni precedenti disponibili allo scarico.
   * @returns boolean con il risultato del check.
   */
  get checkElaborazioniOldSessions(): boolean {
    // Verifico e ritorno il controllo
    return this.elaborazioniOldSessions?.length > 0;
  }

  /**
   * Getter di comodo che verifica se estono report, di qualunque tipo, disponibili allo scarico.
   * @returns boolean con il risultato del check.
   */
  get checkReportsScaricabili(): boolean {
    // Verifico e ritorno il controllo
    return this.checkReportsDisponibili || this.checkElaborazioniOldSessions;
  }

  /**
   * Getter di comodo che verifica l'abilitazione applicativa per i report trasversali.
   * @returns boolean con il valore dell'abilitazione.
   */
  get enableReportTrasversali(): boolean {
    // Recupero dalla configurazione degli ambiti l'accesso ai report
    let allowReport: boolean;
    allowReport = this._ambito.abilitazioniApp?.reportTrasversali;
    // Ritorno il risultato del flag
    return allowReport;
  }
}
