import { HttpClient } from '@angular/common/http';
import { Injectable, OnDestroy } from '@angular/core';
import { clone, cloneDeep, differenceWith, findIndex } from 'lodash';
import { Observable, Subject, Subscription, timer } from 'rxjs';
import { map, switchMap, takeUntil, tap } from 'rxjs/operators';
import {
  ElaborazioneVo,
  IElaborazioneVo,
} from '../../../../core/commons/vo/elaborazione-vo';
import {
  EsportaDatiVo,
  IEsportaDatiVo,
} from '../../../../core/commons/vo/esporta-dati-vo';
import { IReportLocationVo } from '../../../../core/commons/vo/report-location-vo';
import {
  IReportResultVo,
  ReportResultVo,
} from '../../../../core/commons/vo/report-result-vo';
import {
  IRiscossioneSearchV2Vo,
  RiscossioneSearchV2Vo,
} from '../../../../core/commons/vo/riscossione-search-vo';
import { TipoElaborazioneVo } from '../../../../core/commons/vo/tipo-elaborazione-vo';
import { ConfigService } from '../../../../core/services/config.service';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  RiscaFruitori,
  RiscaServerError,
  ServerNumberAsBoolean,
} from '../../../../shared/utilities';
import { PagamentiFunzionalitaConst } from '../../../pagamenti/consts/pagamenti/pagamenti.consts';
import { PagamentiService } from '../../../pagamenti/service/pagamenti/pagamenti.service';
import { IRicercaMorosita } from '../../../pagamenti/service/ricerca-morosita/utilities/ricerca-morosita.interfaces';
import { IRicercaRimborsi } from '../../../pagamenti/service/ricerca-rimborsi/utilities/ricerca-rimborsi.interfaces';
import { CodModalitaRicerca } from '../../../pratiche/components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.enums';
import { ReportsStatus } from './utilities/report.enums';
import {
  ICreaRuoloMorosita,
  IRicercaPagamentiParams,
  IPollReportsStatus,
  IReportBilancioParams,
  IReportVincoliCompetenzaParams,
} from './utilities/report.interfaces';
import { IRicercaPagamentiVo } from '../../../../core/commons/vo/ricerca-pagamenti-vo';

export interface IRicercaDatiContabili {
  offset: number;
  limit: number;
}

@Injectable({ providedIn: 'root' })
export class ReportService extends HttpUtilitiesService implements OnDestroy {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti per la sezione dell'applicazione. */
  private PF_C = PagamentiFunzionalitaConst;

  /** Costante per il path: /report */
  private PATH_REPORT = '/report';
  /** Costante per il path: /report/polling-job */
  private PATH_POLL_REPORT_JOB = `${this.PATH_REPORT}/polling-job`;
  /** Costante per il path: /report/ricerca-avanzata */
  private PATH_CREA_REPORT_RICERCA_AVANZATA = `${this.PATH_REPORT}/ricerca-avanzata`;
  /** Costante per il path: /report/ricerca-morosita */
  private PATH_CREA_REPORT_RICERCA_MOROSITA = `${this.PATH_REPORT}/ricerca-morosita`;
  /** Costante per il path: /report/ricerca-rimborsi */
  private PATH_CREA_REPORT_RICERCA_RIMBORSI = `${this.PATH_REPORT}/ricerca-rimborsi`;
  /** Costante per il path: /report/ricerca-pagamenti */
  private PATH_CREA_REPORT_RICERCA_PAGAMENTI = `${this.PATH_REPORT}/ricerca-pagamenti`;
  /** Costante per il path: /report/bilancio */
  private PATH_CREA_REPORT_BILANCIO = `${this.PATH_REPORT}/bilancio`;
  /** Costante per il path: /report/variazioni-competenza */
  private PATH_CREA_REPORT_VARIAZIONI_COMPETENZA = `${this.PATH_REPORT}/variazioni-competenza`;
  /** Costante per il path: /report/esporta-dati */
  private PATH_CREA_REPORT_ESPORTA_DATI = `${this.PATH_REPORT}/esporta-dati`;
  /** Costante per il path: /file-450/ruolo-ricerca-morosita */
  private PATH_CREA_RUOLO_MOROSITA = `${this.PATH_REPORT}/file-450/ruolo-ricerca-morosita`;

  /** Costante per il path: /elabora */
  private PATH_ELABORA = `/elabora`;
  /** Costante per il path: /elabora/codiceFiscale */
  private PATH_REPORT_VECCHIE_SESSIONI = `${this.PATH_ELABORA}/codiceFiscale`;

  /** TipoElaborazione[] con la lista di oggetto per poter generare un report tramite funzione esporta dati. */
  private _teReport: TipoElaborazioneVo[] = [];

  /** IReportLocation[] con la lista di tutti i "puntamenti" per i jobs per la generazione dei report attualmente in corso. */
  private _reportsLocation: IReportLocationVo[] = [];
  /** IPollReportsStatus con tutte le informazioni sugli stati dei reports, che siano completati, in corso o in errore. */
  private _reportsStatus: IPollReportsStatus;

  /** Subscription valorizzato con l'avanzamento del polling dei dati per i report da creare. */
  private _pollingReports$: Subscription;

  /** Subject adibito alla chiusura della funzione di polling dati. */
  private _stopPollingReports$ = new Subject<any>();
  /** Subject utilizzato per emettere l'evento di polling completato sullo stato di creazione dei report. */
  onPollingReportsSuccess$ = new Subject<IPollReportsStatus>();
  /** Subject utilizzato per emettere l'evento di polling con errore sullo stato di creazione dei report. */
  onPollingReportsErrors$ = new Subject<RiscaServerError>();

  /** ElaborazioneVo con la lista di tutte le elaborazioni con report generati in sessioni precedenti. */
  private _elaborazioniReports: ElaborazioneVo[];
  /** Subject utilizzato per emettere l'evento di avvenuta modifica dei dati dei report collegati alle elaborazioni. */
  onElaborazioniReportsChange$ = new Subject<ElaborazioneVo[]>();

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Costrutture.
   */
  constructor(
    config: ConfigService,
    private _http: HttpClient,
    private _pagamenti: PagamentiService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Tento di fare l'unsubscribe degli observable
    try {
      if (this._stopPollingReports$) {
        this._stopPollingReports$.unsubscribe();
      }
      if (this.onPollingReportsSuccess$) {
        this.onPollingReportsSuccess$.unsubscribe();
      }
      if (this.onPollingReportsErrors$) {
        this.onPollingReportsErrors$.unsubscribe();
      }
    } catch (e) {}
    // Caso particolare, gestisco l'unsubscribe a parte
    try {
      if (this._pollingReports$) {
        this._pollingReports$.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * ###############################
   * GETTER DATI COLLEGATI AI REPORT
   * ###############################
   */

  /**
   * Funzione che recupera i tipi elaborazione per i bollettini
   * @param idAmbito number con id ambito per la ricerca.
   * @param flgVisibile ServerNumberAsBoolean che definisce la visibilità della bollettazione. Per default è: ServerNumberAsBoolean.true.
   * @returns Observable con la lista di tipi elaborazione
   */
  getTipiElaboraReport(
    idAmbito: number,
    flgVisibile = ServerNumberAsBoolean.true
  ): Observable<TipoElaborazioneVo[]> {
    // Se li ho già scaricati, li restituisco
    if (this._teReport?.length > 0) {
      // Ritorno il dato presente nel servizio
      return this._riscaUtilities.responseWithDelay(this._teReport);
    }

    // Definisco l'idFunzionalita il contesto
    const idFB = this.idFunzionalitaReport;
    // Recupero i dati
    return this._pagamenti
      .getTipiElaborazione(idAmbito, idFB, flgVisibile)
      .pipe(
        tap((tipiElaborazione: TipoElaborazioneVo[]) => {
          // Salvo i tipi elaborazione
          this._teReport = cloneDeep(tipiElaborazione);
        })
      );
  }

  /**
   * ##############################
   * FUNZIONI DI GENERAZIONE REPORT
   * ##############################
   */

  /**
   * Funzione che gestisce il flusso logico a seguito della creazione di un nuovo job per la generazione di un report.
   * @param reportLocation IReportLocationVo con il nuovo oggetto di cui gestirne lo stato.
   */
  private onReportJobCreated(reportLocation: IReportLocationVo) {
    // Verifico l'input
    if (!reportLocation) {
      // Configurazione mancante
      return;
    }

    // Aggiungo alla lista dei report da verificare il nuovo report location
    this._reportsLocation.push(reportLocation);

    // Verifico se è in corso il polling delle informazioni
    if (this._pollingReports$) {
      // C'è stata una sottoscrizione, l'annullo
      this._stopPollingReports$.next();
    }

    // Lancio il polling degli stati
    this.pollReportsStatus();
  }

  /**
   * Funzione che effettua la creazione di un report tramite le informazioni di ricerca avanzata.
   * @param ricerca RiscossioneSearchV2Vo come oggetto di ricerca avanzata.
   * @returns Observable<IReportLocationVo> con le informazioni per gestire lo stato di generazione report.
   */
  createReportRicercaAvanzata(
    ricerca: RiscossioneSearchV2Vo
  ): Observable<IReportLocationVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_CREA_REPORT_RICERCA_AVANZATA);
    // Definisco i parametri per la ricerca
    let params: any = {};

    // Recupero, se esiste, la modalita di ricerca definita nell'oggetto in input
    const codModRicerca = ricerca?.modalitaRicerca?.cod_modalita_ricerca;
    // Recupero il codice per sapere se il codice ricerca è: stato debitorio
    const codModSD = CodModalitaRicerca.statoDebitorio;
    // Verifico se la modalita di ricerca è quella dello stato debitorio
    if (codModRicerca == codModSD) {
      // Aggiungo il query param "modalitaRicerca"
      params.modalitaRicerca = 'statoDebitorio';
      // #
    }

    // Genero l'oggetto con i filtri di ricerca, formattato BE
    const bodyRicerca: IRiscossioneSearchV2Vo = ricerca.toServerFormat();
    // Genero l'oggetto delle options
    const options = { params };

    // Richiamo il servizio
    return this._http.post<IReportLocationVo>(url, bodyRicerca, options).pipe(
      tap((reportLocation: IReportLocationVo) => {
        // Aggiungo alla lista dei jobs in corso il nuovo job
        this.onReportJobCreated(reportLocation);
        // #
      })
    );
  }

  /**
   * Funzione che effettua la creazione di un report tramite le informazioni di ricerca morosita.
   * @param ricerca IRicercaMorosita come oggetto di ricerca morosita.
   * @returns Observable<IReportLocationVo> con le informazioni per gestire lo stato di generazione report.
   */
  createReportRicercaMorosita(
    ricerca: IRicercaMorosita
  ): Observable<IReportLocationVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_CREA_REPORT_RICERCA_MOROSITA);
    // Creo i query params per la ricerca
    const params = this.createQueryParams(ricerca);
    // Definisco le options
    const options = { params };

    // Richiamo il servizio
    return this._http.get<IReportLocationVo>(url, options).pipe(
      tap((reportLocation: IReportLocationVo) => {
        // Aggiungo alla lista dei jobs in corso il nuovo job
        this.onReportJobCreated(reportLocation);
        // #
      })
    );
  }

  /**
   * Funzione che effettua la creazione di un documento ruolo tramite le informazioni di ricerca morosita.
   * @param ricerca IRicercaMorosita come oggetto di ricerca morosita.
   * @param idStatiDebitori number[] con la lista degli stati debitori specifici selezionati dall'utente per la generazione del documento del ruolo. Se non definito, il servizio considererà tutti gli stati debitori ottenuti dalla ricerca.
   * @returns Observable<IReportLocationVo> con le informazioni per gestire lo stato di generazione documento ruolo.
   */
  createRuoloRicercaMorosita(
    ricerca: IRicercaMorosita,
    idStatiDebitori?: number[]
  ): Observable<IReportLocationVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_CREA_RUOLO_MOROSITA);

    // Verifico la lista di id stati debitori
    if (!idStatiDebitori) {
      // La variabile deve essere settata a [] x il servizio
      idStatiDebitori = [];
      // #
    }
    // Definisco l'oggetto con le inforamzioni per la generazione del ruolo
    const creaRuoloParams: ICreaRuoloMorosita = {
     // fruitore: RiscaFruitori.RISCA,
      ...ricerca,
    };
    // Creo i query params la configurazione specifica
    const params = this.createQueryParams(creaRuoloParams);
    // Definisco le options
    const options = { params };
    // Definisco il body
    const body = idStatiDebitori;

    // Richiamo il servizio
    return this._http.post<IReportLocationVo>(url, body, options).pipe(
      tap((reportLocation: IReportLocationVo) => {
        // Aggiungo alla lista dei jobs in corso il nuovo job
        this.onReportJobCreated(reportLocation);
        // #
      })
    );
  }

  /**
   * Funzione che effettua la creazione di un report tramite le informazioni di ricerca rimborsi.
   * @param ricerca IRicercaRimborsi come oggetto di ricerca rimborsi.
   * @returns Observable<IReportLocationVo> con le informazioni per gestire lo stato di generazione report.
   */
  createReportRicercaRimborsi(
    ricerca: IRicercaRimborsi
  ): Observable<IReportLocationVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_CREA_REPORT_RICERCA_RIMBORSI);
    // Creo i query params per la ricerca
    const params = this.createQueryParams(ricerca);
    // Definisco le options
    const options = { params };

    // Richiamo il servizio
    return this._http.get<IReportLocationVo>(url, options).pipe(
      tap((reportLocation: IReportLocationVo) => {
        // Aggiungo alla lista dei jobs in corso il nuovo job
        this.onReportJobCreated(reportLocation);
        // #
      })
    );
  }

  /**
   * Funzione che effettua la creazione di un report tramite le informazioni di esportazione dati.
   * @param ricerca IEsportaDatiVo come oggetto di esportazione dati.
   * @returns Observable<IReportLocationVo> con le informazioni per gestire lo stato di generazione report.
   */
  createReportEsportaDati(
    ricerca: IEsportaDatiVo
  ): Observable<IReportLocationVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_CREA_REPORT_ESPORTA_DATI);

    // Richiamo il servizio
    return this._http.post<IReportLocationVo>(url, ricerca).pipe(
      tap((reportLocation: IReportLocationVo) => {
        // Aggiungo alla lista dei jobs in corso il nuovo job
        this.onReportJobCreated(reportLocation);
        // #
      })
    );
  }
  
  /**
   * Funzione che effettua la creazione di un report tramite le informazioni di ricerca pagamenti.
   * @param ricerca IRicercaPagamenti come oggetto di ricerca pagamenti.
   * @returns Observable<IReportLocationVo> con le informazioni per gestire lo stato di generazione report.
   */
  createReportRicercaPagamenti(
    ricerca: IRicercaPagamentiVo
  ): Observable<IReportLocationVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_CREA_REPORT_RICERCA_PAGAMENTI);

    // Definisco l'oggetto con le inforamzioni per i query params
    const ricercaPagamentiParams: IRicercaPagamentiParams = {
      // fruitore: RiscaFruitori.RISCA
    };
    // Creo i query params la configurazione specifica
    const params = this.createQueryParams(ricercaPagamentiParams);
    // Definisco le options
    const options = { params };
    // Definisco il body di richiesta per il servizio
    const body = ricerca;

    // Richiamo il servizio
    return this._http.post<IReportLocationVo>(url, body, options).pipe(
      tap((reportLocation: IReportLocationVo) => {
        // Aggiungo alla lista dei jobs in corso il nuovo job
        this.onReportJobCreated(reportLocation);
        // #
      })
    );
  }

  /**
   * Funzione che effettua la creazione di un report tramite le informazioni di esportazione dati.
   * La funzione è specifica per il tipo elaborazione: report bilancio.
   * @param esportaDati EsportaDatiVo con l'oggetto con le configurazioni di generazione report.
   * @returns Observable<IReportLocationVo> con le informazioni per gestire lo stato di generazione report.
   */
  createReportBilancio(
    esportaDati: EsportaDatiVo
  ): Observable<IReportLocationVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_CREA_REPORT_BILANCIO);

    // Estraggo dall'oggetto i dati per la ricerca
    let reportBilancioParams: Partial<IReportBilancioParams>;
    reportBilancioParams = esportaDati?.reportBilancioParams;
    // Definisco l'oggetto completo dei query params
    const queryParams: IReportBilancioParams = {
      anno: reportBilancioParams?.anno ?? null,
    };
    // Creo i query params per la ricerca
    const params = this.createQueryParams(queryParams);
    // Definisco le options
    const options = { params };

    // Richiamo il servizio
    return this._http.get<IReportLocationVo>(url, options).pipe(
      tap((reportLocation: IReportLocationVo) => {
        // Aggiungo alla lista dei jobs in corso il nuovo job
        this.onReportJobCreated(reportLocation);
        // #
      })
    );
  }

  /**
   * Funzione che effettua la creazione di un report tramite le informazioni di esportazione dati.
   * La funzione è specifica per il tipo elaborazione: report per variazioni di competenza.
   * @param esportaDati EsportaDatiVo con l'oggetto con le configurazioni di generazione report.
   * @returns Observable<IReportLocationVo> con le informazioni per gestire lo stato di generazione report.
   */
  createReportVariazioniCompetenza(
    esportaDati: EsportaDatiVo
  ): Observable<IReportLocationVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_CREA_REPORT_VARIAZIONI_COMPETENZA);

    // Estraggo dall'oggetto i dati per la ricerca
    let reportVarComp: Partial<IReportVincoliCompetenzaParams>;
    reportVarComp = esportaDati?.reportVariazioniCompetenzaParams;
    // Definisco l'oggetto completo dei query params
    const queryParams: IReportVincoliCompetenzaParams = {
      dataInizio: reportVarComp?.dataInizio ?? null,
      dataFine: reportVarComp?.dataFine ?? null,
    };
    // Creo i query params per la ricerca
    const params = this.createQueryParams(queryParams);
    // Definisco le options
    const options = { params };

    // Richiamo il servizio
    return this._http.get<IReportLocationVo>(url, options).pipe(
      tap((reportLocation: IReportLocationVo) => {
        // Aggiungo alla lista dei jobs in corso il nuovo job
        this.onReportJobCreated(reportLocation);
        // #
      })
    );
  }

  /**
   * Funzione che recupera tutti i report generati in sessioni precedenti.
   * @param cf string con il codice fiscale dell'utente per il recupero dei report generati in sessioni precedenti.
   * @returns Observable<ElaborazioneVo[]> con le informazioni per gestire i report generati in sessioni precedenti.
   */
  retrieveReportsOldSessions(
    cf: string,
    emit?: boolean
  ): Observable<ElaborazioneVo[]> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_REPORT_VECCHIE_SESSIONI, cf);
    // Verifico l'input
    emit = emit ?? false;

    // Richiamo il servizio
    return this._http.get<IElaborazioneVo[]>(url).pipe(
      map((iElaborazioniReport: IElaborazioneVo[]) => {
        // Modifico le informazioni per la gestione FE
        let elaborazioniReports: ElaborazioneVo[];
        elaborazioniReports = iElaborazioniReport?.map(
          (iER: IElaborazioneVo) => {
            // Converto l'oggetto
            return new ElaborazioneVo(iER);
            // #
          }
        );

        // Creo una copia locale dei dati
        this._elaborazioniReports = clone(elaborazioniReports);
        // Ritorno le elaborazioni ritornate
        return elaborazioniReports;
      }),
      tap((elaborazioniReports: ElaborazioneVo[]) => {
        // Verifico se è stata chiesta la propagazione dei dati
        if (emit) {
          // Emetto le informazioni
          this.onElaborazioniReportsChange$.next(elaborazioniReports);
          // #
        }
      })
    );
  }

  /**
   * Funzione che scarica un'elaborazione con i dati di dowload per il report collegato.
   * Insieme allo scarico dell'elaborazione, il report collegato all'elaborazione viene flaggato come "scaricato".
   * @param idElabora number con l'id dell'elaborazione a cui è collegato il report generato in sessioni precedenti.
   * @returns Observable<ElaborazioneVo> con le informazioni del report.
   */
  downloadReportOldSessions(idElabora: number): Observable<ElaborazioneVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_ELABORA, idElabora);
    // Creo il query param per il download
    const params = this.createQueryParams({ download: true });
    // Definisco le options per la chiamata
    const options = { params };

    // Richiamo il servizio
    return this._http.get<IElaborazioneVo>(url, options).pipe(
      map((elaborazione: IElaborazioneVo) => {
        // Converto l'oggetto ritornato
        return new ElaborazioneVo(elaborazione);
        // #
      })
    );
  }

  /**
   * ###############################################
   * FUNZIONI DI GESTIONE STATO DEI JOB PER I REPORT
   * ###############################################
   */

  /**
   * Funzione che esegue la get dello stato per il job che gestisce un report.
   * @param jobLocation string con il path del job ritornato dalla funzione di creazione report.
   * @returns Observable<ReportResultVo> con lo stato del job.
   */
  getReportsStatus(
    jobLocation: IReportLocationVo[]
  ): Observable<ReportResultVo[]> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_POLL_REPORT_JOB);
    // Effettuo la chiamata e ritorno il risultato
    return this._http.post<IReportResultVo[]>(url, jobLocation).pipe(
      map((reportResult: IReportResultVo[]) => {
        // Ritorno la conversione in FE
        return reportResult?.map((rr: IReportResultVo) => {
          // Creo l'oggetto FE
          return new ReportResultVo(rr);
        });
      })
    );
  }

  /**
   * Funzione che esegue la get dello stato per il job che gestisce un report.
   * Questa funzione è impostata per non attivare lo spinner di caricamento standard RISCA.
   * @param jobLocation string con il path del job ritornato dalla funzione di creazione report.
   * @returns Observable<ReportResultVo> con lo stato del job.
   */
  getReportsStatusNoSpinner(
    jobLocation: IReportLocationVo[]
  ): Observable<ReportResultVo[]> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_POLL_REPORT_JOB);
    // Definisco l'header per non attivare lo spinner
    const headers = this.headerNoSpinner();

    // Effettuo la chiamata e ritorno il risultato
    return this._http
      .post<IReportResultVo[]>(url, jobLocation, { headers })
      .pipe(
        map((reportResult: IReportResultVo[]) => {
          // Ritorno la conversione in FE
          return reportResult?.map((rr: IReportResultVo) => {
            // Creo l'oggetto FE
            return new ReportResultVo(rr);
          });
        })
      );
  }

  /**
   * Funzione che gestisce il polling dati per lo stato dei job.
   */
  pollReportsStatus() {
    // Verifico se la sottoscrizione al polling dati è chiusa
    if (this._pollingReports$?.closed) {
      // La sottoscrizione è stata chiusa, resetto la variabile
      this._pollingReports$ = undefined;
    }

    // Variabili di comodo
    const interval = this._config.jobPollingInterval;
    const numRetry = 10;

    // Sottoscrivo una variabile al flusso di polling
    this._pollingReports$ = timer(1, interval)
      .pipe(
        switchMap(() => this.getReportsStatusNoSpinner(this.reportsLocation)),
        // RISCA-641 | @Laura Veneruso ha detto per le vie brevi che non vogliono logica di retry
        // retryWhen((error: Observable<RiscaServerError>) => {
        //   // Ritorno la gestione dell'errore
        //   return error.pipe(
        //     delay(interval),
        //     take(numRetry),
        //     concatMap((errorRetry: RiscaServerError, numAttempt: number) => {
        //       // Ritorno e chiudo l'errore
        //       return numAttempt < numRetry
        //         ? of(errorRetry)
        //         : throwError(errorRetry);
        //       // #
        //     })
        //   );
        // }),
        takeUntil(this._stopPollingReports$),
        map((reportsResults: ReportResultVo[]) => {
          // Rimappo le informazioni per gli oggetti completati
          let response: ReportResultVo[];
          response = reportsResults?.map((rr: ReportResultVo) => {
            // Lancio la funzione di completamento dell'oggetto report status
            return this.completeReportStatus(rr);
            // #
          });
          // Ritorno la lista aggiornata
          return response;
        })
      )
      .subscribe({
        next: (reportsResults: ReportResultVo[]) => {
          // Lancio la funzione di gestione delle informazioni
          this.onPollReportStatus(reportsResults);
          // #
        },
        error: (e: RiscaServerError) => {
          // Lancio la funzione di gestione errori
          this.onPollReportStatusError(e);
          // #
        },
      });
  }

  /**
   * Funzione invocata a seguito della conferma per il polling dati sull'avanzamento dei job che generano i file di report.
   * @param reportsResults ReportResultVo[] con il risultato sugli stati di avanzamento dei report richiesti.
   */
  private onPollReportStatus(reportsResults: ReportResultVo[]) {
    // Verifico l'input
    if (!reportsResults) {
      // Non ci sono informazioni
      return;
    }

    // Creo l'oggetto da propagare alle componenti applicative
    let reportsStatus: IPollReportsStatus = this.reportsStatus;
    // Creo una copia dei dati di request appena conclusa
    reportsStatus.request = cloneDeep(this._reportsLocation);

    // Ciclo la lista dei risultati e vado a smistare i risultati
    reportsResults.forEach((rr: ReportResultVo) => {
      // Verifico lo status della risposta e aggiorno gli oggetti di tracciamento
      switch (rr.status) {
        case ReportsStatus.completato:
          // Completato
          reportsStatus.completed.push(rr);
          break;
        // #
        case ReportsStatus.errore:
          // Completato
          reportsStatus.errors.push(rr);
          break;
        // #
        case ReportsStatus.inCorso:
          // In corso
          reportsStatus.inProgress.push(rr);
          break;
        // #
        default:
          // In caso ci sia uno stato non mappato, aggiungo alla lista degli sconosciuti
          reportsStatus.unknown.push(rr);
          break;
      }
    });

    // Assegno localmente la lista delle richieste dei report
    let richieste = clone(this._reportsLocation);
    // Aggiorno le richieste rimuovendo i report in stato
    // # completato
    richieste = this.aggiornaRichiesteReports(
      richieste,
      reportsStatus.completed
    );
    // # in errore
    richieste = this.aggiornaRichiesteReports(richieste, reportsStatus.errors);
    // Aggiorno localmente la lista delle richieste report
    this._reportsLocation = clone(richieste);

    // Inserisco nell'oggetto di ritonro la lista dei report ancora da completare
    reportsStatus.actual = cloneDeep(this._reportsLocation);

    // Verifico se tutti i report sono stati completati
    if (this._reportsLocation.length === 0) {
      // Tutti i report sono stati generati, blocco il polling
      this._stopPollingReports$.next();
    }

    // Creo una copia locale dei dati generati
    this._reportsStatus = clone(reportsStatus);
    // Emetto l'oggetto di stato dei report
    this.emitReportsStatus();
  }

  /**
   * Funzione che rimuovere da una lista iniziale di richieste, tutte quelle che risultano all'interno dell'array "da rimuovere".
   * @param richieste IReportLocationVo[] con la lista di richieste da gestire.
   * @param daRimuovere ReportResultVo[] con la lista di report gestiti completamente e che risultano da rimuovere.
   * @returns IReportLocationVo[] con la lista aggiornata.
   */
  private aggiornaRichiesteReports(
    richieste: IReportLocationVo[],
    daRimuovere: ReportResultVo[]
  ): IReportLocationVo[] {
    // Verifico l'input
    if (!richieste || !daRimuovere) {
      // Mancano le configurazioni
      return richieste;
    }

    // Rimuovo dalla lista originale di richieste, tutti gli oggetti che risultano nella lista "da rimuovere"
    return differenceWith(
      richieste,
      daRimuovere,
      (a: IReportLocationVo, b: ReportResultVo) => {
        // Effettua una comparazione per stesso id_risca
        return a.id_risca === b.id_risca;
      }
    );
  }

  /**
   * Funzione invocata a seguito di errori sul polling dati per l'avanzamento dei job che generano i file di report.
   * @param e RiscaServerError con l'oggetto di errore ritornato dal server.
   */
  private onPollReportStatusError(e: RiscaServerError) {
    // Emetto l'evento passando l'errore generato
    this.onPollingReportsErrors$.next(e);
  }

  /**
   * Funzione che completa alcune informazioni dell'oggetto contenente il risultato della generazione del report.
   * L'oggetto viene manipolato se lo stato risulta completato. La manipolazione prevede:
   * - L'aggiunta del nome del file.
   * @param reportStatus ReportResultVo con le informazioni sullo stato del report generato.
   * @returns ReportResultVo con l'oggetto manipolato secondo le condizioni specifiche.
   */
  private completeReportStatus(reportStatus: ReportResultVo): ReportResultVo {
    // Verifico l'input
    if (!reportStatus) {
      // Niente configurazione
      return reportStatus;
    }

    // Verifico se lo stato del report è completato
    if (reportStatus.status == ReportsStatus.completato) {
      // Stato completato, recupero l'url del file
      const url = reportStatus.report_url;
      // Definisco il contenitore per il nome del file
      let fileName = '';
      let fileNameExt = '';

      // Imposto un try catch per evitare problematiche con la gestione della stringa dell'url
      try {
        // Recupero l'indice dell'ultimo "/" della stringa
        const iLastSlash: number = url.lastIndexOf('/');
        // Definisco i caratteri per la separazione dall'estensione
        const ext = '.'; // REPORT_FILE_EXT_ODS // Costante globale
        // Recupero l'indice dell'estensione del file
        const iFileExt: number = url.lastIndexOf(ext);

        // Verifico se non esiste l'indice per lo slash
        if (iLastSlash == -1) {
          // Non è stato trovato lo slash
          fileName = "Cannot find file's name [slash index error]";
          // #
        }
        // Verifico se non esiste l'estensione .ods
        else if (iFileExt == -1) {
          // Non è stata trovata l'estensione .ods
          fileName = "Cannot find file's name [extension index error]";
          // #
        }
        // L'indice dello slash deve essere precedente rispetto all'indice dell'estensione
        else if (iLastSlash >= iFileExt) {
          // Formattazione dell'url non corretta per l'estrazione del nome del file
          fileName =
            "Cannot find file's name [slash index after extension index error]";
          // #
        } else {
          // I controlli sono passati, estraggo una sotto stringa utilizzando gli indici
          fileName = url.substring(iLastSlash + 1, iFileExt);
          fileNameExt = url.substring(iLastSlash + 1);
          // #
        }
      } catch (e) {
        // Imposto il nome del file con una stringa di rimpiazzo
        reportStatus.file_name = 'Cannot extract file name [catched error]';
      }

      // Assegno all'oggetto il nome del file estratto
      reportStatus.file_name = fileName;
      reportStatus.file_name_ext = fileNameExt;
    }

    // Ritorno l'oggetto
    return reportStatus;
  }

  /**
   * #################################
   * FUNZIONI DI GESTIONE DATI REPORTS
   * #################################
   */

  /**
   * Funzione di utility per rimuovere da ReportResultVo[] un oggetto ReportResultVo.
   * @param reports ReportResultVo[] con la lista da modificare.
   * @param report ReportResultVo con l'oggetto da ricercare.
   * @returns boolean con il risultato dell'operazione.
   */
  removeReportFromList(
    reports: ReportResultVo[],
    report: ReportResultVo
  ): boolean {
    // Cerco all'interno della lista l'oggetto per id
    const iReport = findIndex(reports, (r: ReportResultVo) => {
      // Effettuo un match tra gli id
      return r?.id_risca == report.id_risca;
    });
    // Verifico che sia stato trovato l'indice
    if (iReport != -1) {
      // Elemento trovato, lo rimuovo
      reports.splice(iReport, 1);
      // Operazione completata
      return true;
    }

    // Operazione fallita
    return false;
  }

  /**
   * Funzione che aggiorna le informazioni dei report disponibili a seguito dello scarico di un report da parte dell'utente.
   * @param report ReportResultVo con l'oggetto che l'utente risulta aver scaricato.
   * @param emit boolean che definisce se la modifica ai report generati deve essere propagato. Per default è: true.
   */
  reportScaricato(report: ReportResultVo, emit: boolean = true) {
    // Verifico l'input
    if (!report) {
      // Nessuna configurazione
      return;
    }

    // Recupero la lista dei report completati dall'oggetto completo
    const reportsCompleted = this._reportsStatus?.completed ?? [];
    // Tento di rimuovere l'oggetto in input dalla lista (la modifica avverrà per riferimento)
    const success = this.removeReportFromList(reportsCompleted, report);
    // Verifico se è richiesta la propagazione del dato
    if (success && emit) {
      // Emetto l'oggetto di stato dei report
      this.emitReportsStatus();
    }
  }

  /**
   * Funzione che aggiorna le informazioni dei report disponibili a seguito dello scarico di un report da parte dell'utente.
   * @param report ReportResultVo con l'oggetto che l'utente risulta aver scaricato.
   * @param emit boolean che definisce se la modifica ai report generati deve essere propagato. Per default è: true.
   */
  reportInErroreVisualizzato(report: ReportResultVo, emit: boolean = true) {
    // Verifico l'input
    if (!report) {
      // Nessuna configurazione
      return;
    }

    // Recupero la lista dei report in errore dall'oggetto completo
    const reportsInErrors = this._reportsStatus?.errors ?? [];
    // Tento di rimuovere l'oggetto in input dalla lista (la modifica avverrà per riferimento)
    const success = this.removeReportFromList(reportsInErrors, report);
    // Verifico se è richiesta la propagazione del dato
    if (success && emit) {
      // Emetto l'oggetto di stato dei report
      this.emitReportsStatus();
    }
  }

  /**
   * Funzione di comodo che crea una copia dell'oggetto status ed emette il suo valore.
   */
  emitReportsStatus() {
    // Creo una copia dell'oggetto
    let reportStatus: IPollReportsStatus;
    reportStatus = cloneDeep(this._reportsStatus);
    // Emetto l'evento passando le informazioni impacchettate sullo stato dei report
    this.onPollingReportsSuccess$.next(reportStatus);
  }

  /**
   * #############################################
   * FUNZIONI DI GESTIONE DATI ELABORAZIONI/REPORT
   * #############################################
   */

  /**
   * Funzione di utility per rimuovere da ElaborazioneVo[] un oggetto ElaborazioneVo.
   * @param elaborazioni ElaborazioneVo[] con la lista da modificare.
   * @param elaborazione ElaborazioneVo con l'oggetto da ricercare.
   * @returns boolean con il risultato dell'operazione.
   */
  removeElaborazioniReportsFromList(
    elaborazioni: ElaborazioneVo[],
    elaborazione: ElaborazioneVo
  ): boolean {
    // Cerco all'interno della lista l'oggetto per id
    const iReport = findIndex(elaborazioni, (r: ElaborazioneVo) => {
      // Effettuo un match tra gli id
      return r?.id_elabora == elaborazione.id_elabora;
    });
    // Verifico che sia stato trovato l'indice
    if (iReport != -1) {
      // Elemento trovato, lo rimuovo
      elaborazioni.splice(iReport, 1);
      // Operazione completata
      return true;
    }

    // Operazione fallita
    return false;
  }

  /**
   * Funzione che aggiorna le informazioni dei report "vecchi" disponibili, collegati alle elaborazioni, a seguito dello scarico di un report da parte dell'utente.
   * @param elaborazione ElaborazioneVo con l'oggetto che l'utente risulta aver scaricato.
   * @param emit boolean che definisce se la modifica ai report generati deve essere propagato. Per default è: true.
   */
  reportElaborazioneScaricato(
    elaborazione: ElaborazioneVo,
    emit: boolean = true
  ) {
    // Verifico l'input
    if (!elaborazione) {
      // Nessuna configurazione
      return;
    }

    // Recupero la lista dei report completati dall'oggetto completo
    const elaborazioniReport = this._elaborazioniReports ?? [];
    // Tento di rimuovere l'oggetto in input dalla lista (la modifica avverrà per riferimento)
    const success = this.removeElaborazioniReportsFromList(
      elaborazioniReport,
      elaborazione
    );

    // Verifico se è richiesta la propagazione del dato
    if (success && emit) {
      // Emetto l'oggetto di stato dei report
      this.emitReportsElaborazioni();
    }
  }

  /**
   * Funzione di comodo che crea una copia degli oggetti elaborazione con i dati dei report ed emette il suo valore.
   */
  emitReportsElaborazioni() {
    // Creo una copia dell'oggetto
    let elaborazioniReports: ElaborazioneVo[];
    elaborazioniReports = cloneDeep(this._elaborazioniReports);
    // Emetto l'evento passando le informazioni impacchettate sullo stato dei report
    this.onElaborazioniReportsChange$.next(elaborazioniReports);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter per _reportsLocation.
   * @returns IReportLocationVo[] con la lista dati per la "location" dei job dei report.
   */
  private get reportsLocation(): IReportLocationVo[] {
    // Ritorno la lista di oggetti generati
    return cloneDeep(this._reportsLocation) ?? [];
  }

  /**
   * Getter per _reportsStatus.
   * @returns IPollReportsStatus i dati nel servizio.
   */
  get reportsStatus(): IPollReportsStatus {
    // Verifico se esiste l'oggetto
    if (!this._reportsStatus) {
      // Creo l'oggetto da propagare alle componenti applicative
      this._reportsStatus = {
        request: [],
        actual: [],
        completed: [],
        errors: [],
        inProgress: [],
        unknown: [],
      };
    }

    // Ritorno la lista di oggetti generati
    return cloneDeep(this._reportsStatus);
  }

  /**
   * Setter per _reportsStatus.
   * @params IPollReportsStatus con i dati d'assegnare.
   */
  set reportsStatus(reportsStatus: IPollReportsStatus) {
    // Assegno localmente i dati
    this._reportsStatus = reportsStatus;
  }

  /**
   * Getter per _reportsStatus.completed.
   * @returns ReportResultVo[] con la lista di report generati e disponibili.
   */
  get reportsCompleted(): ReportResultVo[] {
    // Ritorno la lista di oggetti generati
    return cloneDeep(this._reportsStatus?.completed) ?? [];
  }

  /**
   * Getter per _reportsStatus.errors.
   * @returns ReportResultVo[] con la lista di report andati in errore.
   */
  get reportsErrors(): ReportResultVo[] {
    // Ritorno la lista di oggetti generati
    return cloneDeep(this._reportsStatus?.errors) ?? [];
  }

  /**
   * Getter di comodo per l'idFunzionalita di questa specifica sezione.
   * @returns number con l'id della funzionalità della sezione specifica.
   */
  get idFunzionalitaReport() {
    // Recupero l'id funzionalità per questa specifica sezione
    return this.PF_C.report.id_funzionalita;
  }

  /**
   * Getter per _elaborazioniReports.
   * @returns ElaborazioneVo[] con la lista di report generati in sessioni precedenti e non ancora scaricati.
   */
  get elaborazioniReports(): ElaborazioneVo[] {
    // Ritorno la lista di oggetti generati
    return cloneDeep(this._elaborazioniReports) ?? [];
  }
}
