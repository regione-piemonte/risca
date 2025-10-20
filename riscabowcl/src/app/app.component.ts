import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { AccessoElementiAppVo } from './core/commons/vo/accesso-elementi-app-vo';
import { ComuneVo } from './core/commons/vo/comune-vo';
import { ConfigurazioneAmbitoVo } from './core/commons/vo/configurazione-ambito-vo';
import { ElaborazioneVo } from './core/commons/vo/elaborazione-vo';
import { MessaggioUtenteVo } from './core/commons/vo/messaggio-utente-vo';
import { NazioneVo } from './core/commons/vo/nazione-vo';
import { ProvinciaCompetenzaVo } from './core/commons/vo/provincia-competenza-vo';
import { ProvinciaVo } from './core/commons/vo/provincia-vo';
import { StatoRiscossioneVo } from './core/commons/vo/stati-riscossione-vo';
import { TipoAutorizzazioneVo } from './core/commons/vo/tipo-autorizzazioni-vo';
import { TipoElaborazioneVo } from './core/commons/vo/tipo-elaborazione-vo';
import { TipoIstanzaProvvedimentoVo } from './core/commons/vo/tipo-istanza-provvidemento-vo';
import { TipoRecapitoVo } from './core/commons/vo/tipo-recapito-vo';
import { TipoRiscossioneVo } from './core/commons/vo/tipo-riscossione-vo';
import { TipoSedeVo } from './core/commons/vo/tipo-sede-vo';
import { TipoTitoloVo } from './core/commons/vo/tipo-titolo-vo';
import { UserInfoVo } from './core/commons/vo/user-info-vo';
import { AccessoElementiAppService } from './core/services/accesso-elementi-app.service';
import { LoggerService } from './core/services/logger.service';
import { UserService } from './core/services/user.service';
import { TipoNaturaGiuridica, TipoSoggettoVo } from './features/ambito/models';
import { AmbitoService } from './features/ambito/services';
import { BollettiniService } from './features/pagamenti/service/bollettini/bollettini.service';
import { ComponentiDtConfigsRes } from './features/pratiche/class/quadri-tecnici/dati-tecnici.classes';
import { DatiTecniciService } from './features/pratiche/components/quadri-tecnici/services/dati-tecnici/dati-tecnici.service';
import { RecapitiService } from './features/pratiche/service/dati-anagrafici/recapiti.service';
import { SoggettoDatiAnagraficiService } from './features/pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { PraticheService } from './features/pratiche/service/pratiche.service';
import { ReportService } from './features/report/service/report/report.service';
import { CommonConsts } from './shared/consts/common-consts.consts';
import { TipoInvio } from './shared/models/contatti/tipo-invio.model';
import { ContattiService } from './shared/services/contatti.service';
import { LocationService } from './shared/services/location.service';
import { RiscaSpinnerService } from './shared/services/risca-spinner.service';
import { RiscaFiloAriannaService } from './shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaMessagesService } from './shared/services/risca/risca-messages.service';
import {
  RiscaLoadStatus,
  ServerNumberAsBoolean,
  TRiscaLoadStatus,
} from './shared/utilities';

/**
 * Interfaccia che definisce la struttura della chiamata per la profilatura.
 */
interface ProfilazioneReq {
  user: Observable<UserInfoVo>;
}

/**
 * Interfaccia che definisce la struttura della risposta per la profilatura.
 */
interface ProfilazioneRes {
  user: UserInfoVo;
}

/**
 * Interfaccia con la configurazione per la richiesta scarico dei dati.
 */
interface AppDataReq {
  profilatura: Observable<ProfilazioneRes>;
  riscaMessages: Observable<MessaggioUtenteVo[]>;
  tipiRecapito: Observable<TipoRecapitoVo[]>;
  tipiNaturaGiuridica: Observable<TipoNaturaGiuridica[]>;
  provinceCompetenza: Observable<ProvinciaCompetenzaVo[]>;
}

/**
 * Interfaccia con la configurazione per la risposta scarico dei dati.
 */
interface AppDataRes {
  profilatura: ProfilazioneRes;
  riscaMessages: MessaggioUtenteVo[];
  tipiRecapito: TipoRecapitoVo[];
  tipiNaturaGiuridica: TipoNaturaGiuridica[];
  provinceCompetenza: ProvinciaCompetenzaVo[];
}

/**
 * Interfaccia che definisce la struttura della chiamata per la profilatura.
 */
interface CorrelatiProfilazioneReq {
  accessoElementiApp: Observable<AccessoElementiAppVo>;
  ambitoConfigs: Observable<ConfigurazioneAmbitoVo[]>;
  componentiDt: Observable<ComponentiDtConfigsRes>;
  tipiBollettazione: Observable<TipoElaborazioneVo[]>;
  tipiRiscossione: Observable<TipoRiscossioneVo[]>;
  tipiTitolo: Observable<TipoTitoloVo[]>;
  istanze: Observable<TipoIstanzaProvvedimentoVo[]>;
  provvedimenti: Observable<TipoIstanzaProvvedimentoVo[]>;
  tipiSoggetto: Observable<TipoSoggettoVo[]>;
  tipiSede: Observable<TipoSedeVo[]>;
  tipiInvio: Observable<TipoInvio[]>;
  nazioniAttive: Observable<NazioneVo[]>;
  nazioniAll: Observable<NazioneVo[]>;
  comuniAttivi: Observable<ComuneVo[]>;
  comuniAll: Observable<ComuneVo[]>;
  provinceAttive: Observable<ProvinciaVo[]>;
  provinceAll: Observable<ProvinciaVo[]>;
  tipiAutorizzazione: Observable<TipoAutorizzazioneVo[]>;
  statiRiscossione: Observable<StatoRiscossioneVo[]>;
  reportsOldSessions?: Observable<ElaborazioneVo[]>;
}

/**
 * @version SONARQUBE_22_04_24 Rimosse funzioni vuote NgOnInit e NgOnDestroy. 
 */
@Component({
  selector: 'root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /**
   * HostListener event: 'window:popstate' che rimane in ascolto del "back" del browser.
   * @param event any come evento generato dal browser.
   */
  @HostListener('window:popstate', ['$event'])
  onPopState(event: any) {
    // Richiamo il clear degli spinner
    this._riscaSpinner.hideAllGeneric();
  }

  /** TRiscaLoadStatus che definisce lo stato dell'applicazione. */
  status: TRiscaLoadStatus;
  /** TRiscaLoadStatus per lo stato: caricamento. */
  statusLoading = RiscaLoadStatus.caricamento;
  /** TRiscaLoadStatus per lo stato: caricato. */
  statusLoaded = RiscaLoadStatus.caricato;
  /** TRiscaLoadStatus per lo stato: fallito. */
  statusFailed = RiscaLoadStatus.fallito;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoEA: AccessoElementiAppService,
    private _ambito: AmbitoService,
    private _bollettini: BollettiniService,
    private _contatti: ContattiService,
    private _datiTecnici: DatiTecniciService,
    private _location: LocationService,
    private _logger: LoggerService,
    private _pratiche: PraticheService,
    private _recapiti: RecapitiService,
    private _report: ReportService,
    private _riscaFA: RiscaFiloAriannaService,
    private _riscaMessages: RiscaMessagesService,
    private _riscaSpinner: RiscaSpinnerService,
    private _soggettoDA: SoggettoDatiAnagraficiService,
    private _user: UserService
  ) {
    // Lancio lo scarico dei dati per l'applicazione
    this.setupAppData();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di comodo per lo scarico dei dati per l'applicazione.
   */
  private setupAppData() {
    // Definisco lo stato di caricamento dati
    this.status = this.statusLoading;

    // Lancio il setup delle configurazioni per i livelli del filo d'arianna
    this._riscaFA.setupFALivelli();
    // Definisco l'oggetto per la chiamata ai dati
    const dataReq: AppDataReq = {
      profilatura: this.caricaDatiProfilatura(),
      riscaMessages: this.caricaMessaggiApp(),
      tipiRecapito: this.caricaTipiRecapito(),
      tipiNaturaGiuridica: this.caricaTipiNaturaGiuridica(),
      provinceCompetenza: this.caricaProvinceCompetenza(),
    };

    // Tramite forkJoin effettuo le chiamate e attendo risposta
    forkJoin(dataReq)
      .pipe(
        switchMap((adr: AppDataRes) => {
          // Recupero i dati dalla response
          const { profilatura } = adr;
          // Recupero l'ambito
          const { user } = profilatura;
          // Recupero le informazioni utili per lo scarico dati
          const ambito: number = user.ambito;
          const cf: string = user.codFisc;

          // Definisco l'oggetto con le richieste dati per i correlati alla profizione
          const correlatiProfilazioneReq: CorrelatiProfilazioneReq = {
            accessoElementiApp: this._accessoEA.getAccessoElementiApp(),
            ambitoConfigs: this.caricaAmbitoConfigs(ambito),
            componentiDt: this.componentiDt(ambito),
            tipiBollettazione: this.tipiBollettazione(ambito),
            tipiRiscossione: this.caricaTipiRiscossione(ambito),
            tipiTitolo: this.caricaTipiTitolo(ambito),
            istanze: this.caricaIstanze(ambito),
            provvedimenti: this.caricaProvvedimenti(ambito),
            tipiSoggetto: this.caricaTipiSoggetto(),
            tipiSede: this.caricaTipiSede(),
            tipiInvio: this.caricaTipiInvio(),
            nazioniAttive: this.caricaNazioniAttive(),
            nazioniAll: this.caricaNazioniInattive(),
            comuniAttivi: this.caricaComuniAttivi(),
            comuniAll: this.caricaComuniAll(),
            provinceAttive: this.caricaProvinceAttive(),
            provinceAll: this.caricaProvinceAll(),
            tipiAutorizzazione: this.caricaTipiAutorizzazione(ambito),
            statiRiscossione: this.caricaStatiRiscossione(ambito),
            reportsOldSessions: this.recuperaReportVecchieSessioni(cf),
          };
          // Richiamo lo scarico dati per le configurazioni correlate alla profilazione
          return forkJoin(correlatiProfilazioneReq);
          // #
        })
      )
      .subscribe({
        next: (r: any) => {
          // I dati sono caricati, aggiorno lo status
          this.status = this.statusLoaded;
          // Emetto l'evento di login completato
          this._user.onLoginSuccess();
        },
        error: (e: any) => {
          this._logger.warning('setupAppData', e);
          // Si è verificato un errore, redirect
          this.status = this.statusFailed;
        },
      });
  }

  /**
   * Funzione per il caricamento dei dati di profilatura utente.
   * @returns ProfilaturaReq con i dati scaricati.
   */
  private caricaDatiProfilatura(): Observable<ProfilazioneRes> {
    // Definisco le funzioni di profilatura
    const profilatura: ProfilazioneReq = {
      user: this._user.getUser(),
    };

    // Richiamo le funzioni di profilatura
    return forkJoin(profilatura);
  }

  /**
   * Funzione per il caricamento dei dati tecnici.
   * @param idAmbito number con l'id ambito per lo scarico dei dati tecnici.
   * @returns Observable<ComponentiDtConfigsRes> con i dati scaricati.
   */
  private componentiDt(idAmbito: number): Observable<ComponentiDtConfigsRes> {
    // Lancio la chiamata per il recupero dei dati tecnici
    return this._datiTecnici.getComponentiDtConfigs(idAmbito);
  }

  /**
   * Funzione per il caricamento dei messaggi dell'applicazione.
   * @returns Observable<MessaggioUtenteVo> con i dati scaricati.
   */
  private caricaMessaggiApp(): Observable<MessaggioUtenteVo[]> {
    // Lancio la chiamata per il recupero dei messaggi applicativi
    return this._riscaMessages.getMessagesRemote();
  }

  /**
   * Funzione per il caricamento dei dati di configurazioni per l'ambito.
   * @params idAmbito number che definisce l'id ambito per il download dei dati.
   * @returns Observable<ConfigurazioneAmbitoVo[]> con i dati scaricati.
   */
  private caricaAmbitoConfigs(
    idAmbito: number
  ): Observable<ConfigurazioneAmbitoVo[]> {
    // Lancio la chiamata per il recupero dei dati tecnici
    return this._ambito.getAmbitoConfigs(idAmbito);
  }

  /**
   * Funzione che scarica i dati per i tipi recapiti.
   * @returns Observable<TipoRecapitoVo[]> con i dati scaricati.
   */
  private caricaTipiRecapito(): Observable<TipoRecapitoVo[]> {
    // Lancio la chiamata per il recupero dei dati tecnici
    return this._recapiti.getTipiRecapito();
  }

  /**
   * Funzione che scarica i dati per i tipi natura giuridica.
   * @returns Observable<TipoNaturaGiuridica[]> con i dati scaricati.
   */
  private caricaTipiNaturaGiuridica(): Observable<TipoNaturaGiuridica[]> {
    // Lancio la chiamata per il recupero dati
    return this._soggettoDA.getTipiNaturaGiuridica();
  }

  /**
   * Funzione che scarica i dati per i codici utenza.
   * @returns Observable<ProvinciaCompetenzaVo[]> con i dati scaricati.
   */
  private caricaProvinceCompetenza(): Observable<ProvinciaCompetenzaVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._pratiche.getProvinceCompetenza();
  }

  /**
   * Funzione che scarica i dati per i tipi elaborazione della bollettazione.
   * @params idAmbito number che definisce l'id ambito per il download dei dati.
   * @returns Observable<TipoElaborazione[]> con i dati scaricati.
   */
  private tipiBollettazione(
    idAmbito: number
  ): Observable<TipoElaborazioneVo[]> {
    // Variabile di comodo
    const flgVisibile = ServerNumberAsBoolean.true;
    // Lancio la chiamata per il recupero dati
    return this._bollettini.getTipiBollettazione(idAmbito, flgVisibile);
  }

  /**
   * Funzione che scarica i dati per i tipi riscossione.
   * @params idAmbito number che definisce l'id ambito per il download dei dati.
   * @returns Observable<TipoRiscossioneVo[]> con i dati scaricati.
   */
  private caricaTipiRiscossione(
    idAmbito: number
  ): Observable<TipoRiscossioneVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._pratiche.getTipiRiscossione(idAmbito);
  }

  /**
   * Funzione che scarica i dati per i tipi titolo della per le pratiche.
   * @params idAmbito number che definisce l'id ambito per il download dei dati.
   * @returns Observable<TipoTitoloVo[]> con i dati scaricati.
   */
  private caricaTipiTitolo(idAmbito: number): Observable<TipoTitoloVo[]> {
    // Lancio la chiamata per il recupero dei dati
    return this._pratiche.getTipiTitolo(idAmbito);
  }

  /**
   * Funzione che scarica i dati per le istanze delle pratiche.
   * @params idAmbito number che definisce l'id ambito per il download dei dati.
   * @returns Observable<TipoProvvedimento[]> con i dati scaricati.
   */
  private caricaIstanze(
    idAmbito: number
  ): Observable<TipoIstanzaProvvedimentoVo[]> {
    // Lancio la chiamata per il recupero dei dati
    return this._pratiche.getTipiIstanza(idAmbito);
  }

  /**
   * Funzione che scarica i dati per i provvedimenti delle pratiche.
   * @params idAmbito number che definisce l'id ambito per il download dei dati.
   * @returns Observable<TipoProvvedimento[]> con i dati scaricati.
   */
  private caricaProvvedimenti(
    idAmbito: number
  ): Observable<TipoIstanzaProvvedimentoVo[]> {
    // Lancio la chiamata per il recupero dei dati
    return this._pratiche.getTipiProvvedimento(idAmbito);
  }

  /**
   * Funzione che scarica i dati per i tipi soggetto.
   * @returns Observable<TipoSoggettoVo[]> con i dati scaricati.
   */
  private caricaTipiSoggetto(): Observable<TipoSoggettoVo[]> {
    // Lancio la chiamata per il recupero dei dati
    return this._soggettoDA.getTipiSoggetto();
  }

  /**
   * Funzione che scarica i dati per i tipi sede dei soggetti.
   * @returns Observable<TipoSedeVo[]> con i dati scaricati.
   */
  private caricaTipiSede(): Observable<TipoSedeVo[]> {
    // Lancio la chiamata per il recupero dei dati
    return this._location.getTipiSede();
  }

  /**
   * Funzione che scarica i dati per i tipi sede dei soggetti.
   * @returns Observable<TipoInvio[]> con i dati scaricati.
   */
  private caricaTipiInvio(): Observable<TipoInvio[]> {
    // Lancio la chiamata per il recupero dei dati
    return this._contatti.getTipiInvio();
  }

  /**
   * Funzione che scarica i dati per le nazioni attive.
   * @returns Observable<NazioneVo[]> con le informazioni scaricate.
   */
  private caricaNazioniAttive(): Observable<NazioneVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._location.getNazioniAttive();
  }

  /**
   * Funzione che scarica i dati per le nazioni attive e non attive.
   * @returns Observable<NazioneVo[]> con le informazioni scaricate.
   */
  private caricaNazioniInattive(): Observable<NazioneVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._location.getNazioniAll();
  }

  /**
   * Funzione che scarica i dati per i comuni attivi.
   * @returns Observable<ComuneVo[]> con le informazioni scaricate.
   */
  private caricaComuniAttivi(): Observable<ComuneVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._location.getComuniAttivi();
  }

  /**
   * Funzione che scarica i dati per i comuni attivi e non attivi.
   * @returns Observable<ComuneVo[]> con le informazioni scaricate.
   */
  private caricaComuniAll(): Observable<ComuneVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._location.getComuniAll();
  }

  /**
   * Funzione che scarica i dati per le province attive.
   * @returns Observable<ProvinciaVo[]> con le informazioni scaricate.
   */
  private caricaProvinceAttive(): Observable<ProvinciaVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._location.getProvinceAttive();
  }

  /**
   * Funzione che scarica i dati per le province attive e non attive.
   * @returns Observable<ProvinciaVo[]> con le informazioni scaricate.
   */
  private caricaProvinceAll(): Observable<ProvinciaVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._location.getProvinceAll();
  }

  /**
   * Funzione che scarica i dati dei tipi autorizzazione.
   * @params idAmbito number che definisce l'id ambito per il download dei dati.
   * @returns Observable<TipoAutorizzazioneVo[]> con le informazioni scaricate.
   */
  private caricaTipiAutorizzazione(
    idAmbito: number
  ): Observable<TipoAutorizzazioneVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._pratiche.getTipiAutorizzazione(idAmbito);
  }

  /**
   * Funzione che scarica i dati per gli stati riscossioni.
   * @params idAmbito number che definisce l'id ambito per il download dei dati.
   * @returns Observable<StatoRiscossioneVo[]> con le informazioni scaricate.
   */
  private caricaStatiRiscossione(
    idAmbito: number
  ): Observable<StatoRiscossioneVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._pratiche.getStatiRiscossione(idAmbito);
  }

  /**
   * Funzione che scarica i dati per i report delle vecchie sessioni.
   * @param cf string con il codice fiscale dell'utente collegato.
   * @returns Observable<ElaborazioneVo[]> con le informazioni scaricati.
   */
  private recuperaReportVecchieSessioni(
    cf: string
  ): Observable<ElaborazioneVo[]> {
    // Lancio la chiamata per il recupero dati
    return this._report.retrieveReportsOldSessions(cf, true);
  }
}
