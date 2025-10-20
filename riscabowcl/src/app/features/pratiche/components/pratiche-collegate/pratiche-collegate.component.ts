import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { SoggettoVo } from 'src/app/core/commons/vo/soggetto-vo';
import { PraticaEDatiTecnici } from '../../../../core/commons/vo/pratica-vo';
import { RiscossioneSearchResultVo } from '../../../../core/commons/vo/riscossione-search-result-vo';
import {
  IJourneySnapshot,
  IStepNavigation,
} from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import { NavigationHelperClass } from '../../../../shared/classes/navigation/navigation-helper.class';
import { PraticheCollegateGruppoTable } from '../../../../shared/classes/risca-table/pratiche-collegate/pratiche-collegate-gruppo.table';
import { PraticheCollegatePraticheTable } from '../../../../shared/classes/risca-table/pratiche-collegate/pratiche-collegate-pratiche.table';
import { PraticheCollegateSoggettiTable } from '../../../../shared/classes/risca-table/pratiche-collegate/pratiche-collegate-soggetti.table';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { RiscaLockPraticaService } from '../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { IRichiestaLockPraticaRes } from '../../../../shared/services/risca/risca-lock-pratica/utilities/risca-lock-pratica.interfaces';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  AppCallers,
  AppRoutes,
  GruppoEComponenti,
  IPraticaRouteParams,
  RiscaAlertConfigs,
  RiscaAzioniPratica,
  RiscaInfoLevels,
  RiscaIstanzePratica,
  RiscaServerError,
  TRiscaAlertType,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IHomeRouteParams } from '../../../home/components/home/utilities/home.interfaces';
import { IGruppoRouteParams } from '../../../soggetti/components/gruppo/utilities/gruppo.interfaces';
import { ISoggettoRouteParams } from '../../../soggetti/components/soggetto/utilities/soggetto.interfaces';
import { GruppoConsts } from '../../../soggetti/consts/gruppo/gruppo.consts';
import { SoggettoConsts } from '../../../soggetti/consts/soggetto/soggetto.consts';
import { InserisciPraticaConsts } from '../../consts/inserisci-pratica/inserisci-pratica.consts';
import { PraticheCollegateConsts } from '../../consts/pratiche-collegate/pratiche-collegate.consts';
import { PraticaRouteKeys } from '../../enums/pratica/pratica.enums';
import { IPraticheCollegateRes } from '../../resolvers/pratiche-collegate/pratiche-collegate.resolver';
import { PraticheService } from '../../service/pratiche.service';
import { IIPConfigs } from '../inserisci-pratica/utilities/inserisci-pratica.interfaces';

@Component({
  selector: 'pratiche-collegate',
  templateUrl: './pratiche-collegate.component.html',
  styleUrls: ['./pratiche-collegate.component.scss'],
})
export class PraticheCollegateComponent
  extends NavigationHelperClass
  implements OnInit, OnDestroy
{
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts();
  /** Oggetto PraticheCollegateConsts contenente le costanti di uso comune per le pratiche collegate. */
  PC_C = PraticheCollegateConsts;
  /** Oggetto contenente i valori costanti per il componente soggetto. */
  S_C = SoggettoConsts;
  /** Oggetto contenente i valori costanti per il componente gruppo. */
  G_C = GruppoConsts;
  /** Oggetto contenente i valori costanti per il componente inserisci pratica. */
  IP_C = InserisciPraticaConsts;

  /** RiscossioneSearchV2Vo contenente i dati passati tramite resolver. */
  private pratiche: RiscossioneSearchResultVo[];
  /** SoggettoVo contenete i dati passati tramite resolver. */
  private soggetto: SoggettoVo;
  /** Gruppo contenete i dati passati tramite resolver. */
  private gruppoEComponenti: GruppoEComponenti;

  /** Oggetto PraticheCollegateSoggettiTable che conterrà le configurazioni per la tabella del soggetto. */
  tableSoggetto: PraticheCollegateSoggettiTable;
  /** Oggetto PraticheCollegateGruppiTable che conterrà le configurazioni per la tabella dei gruppi. */
  tableGruppo: PraticheCollegateGruppoTable;
  /** Oggetto PraticheCollegatePraticheTable o PraticheCollegateGruppiTable che conterrà le configurazioni per la tabella delle pratiche correlate o dei gruppi. */
  tablePraticheCollegate: PraticheCollegatePraticheTable;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /**
   * Costruttore.
   */
  constructor(
    private _activatedRoute: ActivatedRoute,
    private _logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _pratiche: PraticheService,
    private _riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(navigationHelper, PraticheCollegateConsts.NAVIGATION_CONFIG);

    // Funzione di setup per il routing del browser
    this.setupBrowserRouting();
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  ngOnDestroy(): void {
    // Rimuovo la funzionalità del back del browser
    this._navigationHelper.deleteBrowerBack();
  }

  /**
   * ##############################
   * FUNZIONI DI SETUP: CONSTRUCTOR
   * ##############################
   */

  /**
   * Setta la funzione di override del back del browser
   */
  private setupBrowserRouting() {
    // Definisco una funzione custom per la gestione del back del browser
    const back = () => {
      // Richiamo la funzione locale
      this.tornaIndietro();
    };
    // Registro una funzione custom per il back del browser
    this._navigationHelper.setBrowserBack(back);
    // Sovrascrivo la funzione di default del back con la funzione sopra definita
    this._navigationHelper.overrideBrowserBack();
  }

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Effettuo la gestione dei dati della snapshot del componente
    this.setupComponentSnapshot();
  }

  /**
   * Funzione rimuove possibili step e snapshot dal serivizio di navigazione.
   */
  private setupComponentSnapshot() {
    // Recupero l'id del component step
    const componentId = PraticheCollegateConsts.NAVIGATION_CONFIG.componentId;
    // Rimuovo lo step da journey
    this._navigationHelper.removeStep(componentId, true);
  }

  /**
   * ##########################
   * FUNZIONI DI INIT: NGONINIT
   * ##########################
   */

  /**
   * Funzione di comodo che racchiude le funzioni d'init per il componente.
   */
  private initComponente() {
    // Effettuo il setup dei dati passati al componente tramite activatedRoute
    this.initActivatedRouteParams();
    // Effettuo il setup per le tabelle
    this.initTabelle();
  }

  /**
   * Funzione di init delle informazioni relative al routing della pagina.
   */
  private initActivatedRouteParams() {
    // Repero il data dal servizio
    const data = this._riscaUtilities.getActivatedRouteData(
      this._activatedRoute
    );
    // La variabile data è composta dai parametri definiti nel <nome_modulo>-routing.module.ts che implementa il componente
    const resolveData: IPraticheCollegateRes = data?.praticheCollegate;
    // Estraggo le proprietà
    const { pratiche, soggetto, gruppo, error } = resolveData || {};

    // Assegno localmente le variabili
    this.pratiche = pratiche;
    this.soggetto = soggetto;
    this.gruppoEComponenti = gruppo;

    // Verifico se ci sono errori da gestire
    if (error) {
      // Richiamo la funzione di gestione errori dal server
      this.onServiziError(error);
    }
  }

  /**
   * Funzione di comodo per la gestione del setup delle tabelle.
   */
  private initTabelle() {
    // Manipolo le informazioni per la generazione delle tabelle
    if (this.soggetto) {
      // Esiste un soggetto, genero una struttura di comodo
      const sT = [this.soggetto];
      // Genero la tabella del soggetto
      this.tableSoggetto = new PraticheCollegateSoggettiTable(sT);
      // #
    } else if (this.gruppoEComponenti) {
      // Esiste un gruppo, genero una struttura di comodo
      const gT = [this.gruppoEComponenti];
      // Genero la tabella del gruppo
      this.tableGruppo = new PraticheCollegateGruppoTable(gT);
      // #
    }

    // Verifico le pratiche
    if (this.pratiche) {
      // Esistono le pratiche, genero una struttura di comodo
      const p = this.pratiche;
      // Genero la tabella delle pratiche
      this.tablePraticheCollegate = new PraticheCollegatePraticheTable(p);
      // #
    } else {
      // Gestisco un warning per la mancanza di pratiche
      const m = this._riscaMessages.getMessage(RiscaNotifyCodes.A008);
    }
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione che effettua un reindirizzamento verso la pagina della home.
   */
  private tornaHome() {
    // Definisco i parametri per la pagina home
    const state: IHomeRouteParams = {};
    // Richiamo la funzione di back del servizio di navigazione
    this._navigationHelper.stepBack({ stateTarget: state });
  }

  /**
   * Funzione di comodo che imposta il messaggio d'errore
   * @param error RiscaServerError che definisce il corpo dell'errore.
   * @param messageCode string che definisce il messaggio d'errore da visualizzare in testata. Default è 'E005'.
   * @param otherMessages Array di string contenente un array con altri messaggi di dettaglio per l'errore. Default è [].
   */
  onServiziError(
    error: RiscaServerError,
    messageCode = RiscaNotifyCodes.E005,
    otherMessages: string[] = []
  ) {
    // Definisco un array di messaggi d'errore
    let erroriValidazione = this._riscaAlert.messagesFromServerError(
      error,
      messageCode,
      otherMessages
    );

    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(
      this.alertConfigs,
      erroriValidazione,
      RiscaInfoLevels.danger
    );
  }

  /**
   * Funzione che gestisce il prompt dei messaggi.
   * Se i parametri non sono definiti, verrà effettuato un reset.
   * @param c RiscaAlertConfigs d'aggiornare con le nuove informazioni.
   * @param messaggi Array di string contenente i messaggi da visualizzare.
   * @param tipo TRiscaAlertType che definisce la tipologia di alert da visualizzare.
   */
  protected aggiornaAlertConfigs(
    c: RiscaAlertConfigs,
    messaggi?: string[],
    tipo?: TRiscaAlertType
  ) {
    // Aggiorno la configurazione
    this._riscaAlert.aggiornaAlertConfigs(c, messaggi, tipo);
  }

  /**
   * Funzione che definisce un comportamento standard quando viene emesso l'evento: onAlertHidden; da parte del componente: risca-alert.
   * @param hidden boolean che definisce lo stato di nascosto dell'alert.
   * @param alertConfigs RiscaAlertConfigs da resettare.
   */
  onAlertHidden(hidden: boolean, alertConfigs: RiscaAlertConfigs) {
    // Verifico il risultato
    if (hidden && alertConfigs) {
      // Resetto la configurazione dell'alert
      this.aggiornaAlertConfigs(alertConfigs);
    }
  }

  /**
   * ###################################
   * GESTIONE DEGLI EVENTI DELLE TABELLE
   * ###################################
   */

  /**
   * Funzione agganciata all'evento di dettaglio lanciato dalla tabella.
   * @param dettaglio RiscaTableDataConfig<SoggettoVo> contenente i dati della riga.
   */
  dettaglioSoggetto(dettaglio: RiscaTableDataConfig<SoggettoVo>) {
    // Verifico l'input
    if (!dettaglio) {
      return;
    }

    // Aggiungo al filo d'arianna il segmento per dettaglio soggetto
    const dettSFA: FALivello = this._riscaFA.dettaglioSoggetto;
    // Creo e aggiungo il segmento
    this._riscaFA.aggiungiSegmentoByLivelli(dettSFA);

    // Recupero il dato originale
    const soggetto: SoggettoVo = dettaglio.original;
    // Definisco lo state per la route per soggetto
    const state: ISoggettoRouteParams = {
      soggetto: soggetto,
      modalita: AppActions.modifica,
    };

    // Variabile di comodo
    const route = AppRoutes.soggetto;
    // Genero l'oggetto per il target del routing
    const target: IStepNavigation = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.S_C.NAVIGATION_CONFIG;

    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * Funzione agganciata all'evento di dettaglio lanciato dalla tabella.
   * @param dettaglio RiscaTableDataConfig<GruppoEComponenti> contenente i dati della riga.
   */
  dettaglioGruppo(dettaglio: RiscaTableDataConfig<GruppoEComponenti>) {
    // Verifico l'input
    if (!dettaglio) {
      return;
    }

    // Aggiungo al filo d'arianna il segmento per dettaglio gruppo
    const dettGFA: FALivello = this._riscaFA.dettaglioGruppo;
    // Creo e aggiungo il segmento
    this._riscaFA.aggiungiSegmentoByLivelli(dettGFA);

    // Recupero il dato originale
    const gec: GruppoEComponenti = dettaglio.original;
    // Recupero il dato del gruppo
    const { gruppo } = gec;
    // Definisco lo state per la route per gruppo
    const state: IGruppoRouteParams = {
      gruppo: gruppo,
      modalita: AppActions.modifica,
    };

    // Variabile di comodo
    const route = AppRoutes.gruppo;
    // Genero l'oggetto per il target del routing
    const target: IStepNavigation = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.G_C.NAVIGATION_CONFIG;

    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * Funzione agganciata all'evento di dettaglio lanciato dalla tabella.
   * @param dettaglio RiscaTableDataConfig<RiscossioneSearchResultVo> contenente i dati della riga.
   */
  dettaglioPratica(dettaglio: RiscaTableDataConfig<RiscossioneSearchResultVo>) {
    // Verifico l'input
    if (!dettaglio) {
      return;
    }

    // Recupero il dato originale
    const pratica: RiscossioneSearchResultVo = dettaglio.original;
    // Recupero l'id della pratica
    const { id_riscossione } = pratica || {};

    // Definisco una variabile per gestire la richiesta
    let req: Observable<PraticaEDatiTecnici>;

    // Il lock della riscossione è attivo solo se il ruolo non è consultatore
    if (this._user.isCONSULTATORE) {
      // Non deve avvenire il lock della riscossione
      req = this._pratiche.getPraticaEDatiTecnici(id_riscossione);
      // #
    } else {
      // Devo effettuare il lock della riscossione
      req = this._riscaLockP.bloccaPratica(id_riscossione).pipe(
        tap((resLock: IRichiestaLockPraticaRes) => {
          // Richiamo il servizio di lock per la gestione del risultato
          this._riscaLockP.utenteLockaPratica(resLock);
          // #
        }),
        switchMap((resLock: IRichiestaLockPraticaRes) => {
          // Scarico i dati della pratica
          return this._pratiche.getPraticaEDatiTecnici(id_riscossione);
        })
      );
    }

    // Verifico il lock sulla riscossione prima di aprirla
    req.subscribe({
      next: (pedt: PraticaEDatiTecnici) => {
        // Richiamo la funzione di gestione dati per la navigazione
        this.onPraticaEDatiTecnici(pedt);
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione errori dal server
        this.onServiziError(e);
        // #
      },
    });
  }

  /**
   * Funzione di comodo invocata al termine del caricamento dei dati della pratica e dei dati tecnici.
   * @param pedt PraticaEDatiTecnici con il risultato dello scarico dati di pratica e dati tecnici.
   */
  private onPraticaEDatiTecnici(pedt: PraticaEDatiTecnici) {
    // Definisco i livelli per il filo d'arianna
    const dettPraticaFA: FALivello = this._riscaFA.dettaglioPratica;
    // Imposto il filo d'arianna
    this._riscaFA.aggiungiSegmentoByLivelli(dettPraticaFA);

    // Definisco lo state da passare alla pratica
    const state: IPraticaRouteParams = {
      navTarget: RiscaAzioniPratica.inserisciPratica,
      modalita: AppActions.modifica,
    };

    // Creo l'oggetto con tutti i parametri per il setup dell'inserimento pratica
    const praticaInserimento: IIPConfigs = {
      navTarget: RiscaIstanzePratica.generaliAmministrativi,
      praticaEDatiTecnici: pedt,
    };
    // Definisco i dati specifici per l'inserimento
    state[PraticaRouteKeys.inserimento] = praticaInserimento;

    // Variabile di comodo
    const route = AppRoutes.gestionePratiche;
    // Genero l'oggetto per il target del routing
    const target: IStepNavigation = this.navigationConfigs(route, state);
    // Aggiungo per il navigator la configurazione per il la pagina successiva
    target.jStepTarget = this.IP_C.NAVIGATION_CONFIG;

    // Navigo sulla rotta
    this.journeyNavigation(target, this.snapshotConfigs);
  }

  /**
   * #####################
   * PULSANTI DELLA PAGINA
   * #####################
   */

  /**
   * Funzione che attiva la route per ritornare alla pagina precedente.
   */
  tornaIndietro() {
    // Verifico il caller
    if (this.callerHome) {
      // Ritorno alla home
      this.tornaHome();
    }
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione di comodo per la generazione delle label del componente.
   * @returns Oggetto { targetPratiche: string; denominazione: string } con le label generate.
   */
  private generaLabelPagina(): {
    targetPratiche: string;
    denominazione: string;
  } {
    // Definisco l'oggetto contenitore
    const labels = {
      targetPratiche: '',
      denominazione: '',
    };

    // Verifico i dati per la denominazione
    if (this.soggetto) {
      // Creo le generalità
      const generalita = this._riscaUtilities.identificativoSoggetto(
        this.soggetto
      );
      // Ritorno le generalità
      return { targetPratiche: 'soggetto', denominazione: generalita };
      // #
    } else if (this.gruppoEComponenti) {
      // Recupero la descrizione del gruppo
      const dg = this.gruppoEComponenti?.gruppo?.des_gruppo_soggetto;
      // Ritorno la descrizione del gruppo
      return { targetPratiche: 'gruppo', denominazione: dg };
      // #
    } else {
      // Nessuna descrizione, qualcosa non va :I
      this._logger.warning('get denominazione', 'No description provided.');
      // Ritorno stringa vuota
      return labels;
    }
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertConfigs);
  }

  /**
   * Getter che verifica se il caller è deinito per 'home'.
   */
  get callerHome(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastCaller(AppCallers.home);
  }

  /**
   * Getter che recupera il nome del soggetto o del gruppo in input del componente.
   */
  get denominazione() {
    // Richiamo la funzione di generazione label
    return this.generaLabelPagina().denominazione;
  }

  get noPratiche() {
    // Richiamo la funzione di generazione label
    return this.generaLabelPagina();
  }

  /**
   * Getter di comodo per il check della tabella per: Soggetto.
   */
  get checkTSoggetto() {
    // Verifico se l'oggetto della tabella esiste e ha dati
    return this.tableSoggetto?.source?.length > 0;
  }

  /**
   * Getter di comodo per il check della tabella per: Gruppo.
   */
  get checkTGruppo() {
    // Verifico se l'oggetto della tabella esiste e ha dati
    return this.tableGruppo?.source?.length > 0;
  }

  /**
   * Getter di comodo per il check della tabella per: Partiche.
   */
  get checkTPartiche() {
    // Verifico se l'oggetto della tabella esiste e ha dati
    return this.tablePraticheCollegate?.source?.length > 0;
  }

  /**
   * Getter di comodo che restitusce un oggetto IJourneySnapshot per generare una snapshot del componente.
   */
  get snapshotConfigs(): IJourneySnapshot {
    return {
      source: this,
      mapping: PraticheCollegateConsts.SNAPSHOT_CONFIG.mapping,
      saveFunc: PraticheCollegateConsts.SNAPSHOT_CONFIG.saveFunc,
    };
  }
}
