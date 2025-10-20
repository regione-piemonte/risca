import { Injectable } from '@angular/core';
import { PraticaEDatiTecnici } from '../../../../core/commons/vo/pratica-vo';
import {
  IJourneyStep,
  IJStepConfig,
  IStepNavigation,
} from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import {
  AppActions,
  AppRoutes,
  IPraticaRouteParams,
  RiscaAzioniPratica,
  RiscaIstanzePratica,
  RiscaServerError,
} from '../../../../shared/utilities';
import { IIPConfigs } from '../../components/inserisci-pratica/utilities/inserisci-pratica.interfaces';
import { InserisciPraticaConsts } from '../../consts/inserisci-pratica/inserisci-pratica.consts';
import { PraticaRouteKeys } from '../../enums/pratica/pratica.enums';
import { IRModificaPratica } from '../../interfaces/ricerca-pratiche/ricerca-pratiche.interfaces';
import { PraticheService } from '../pratiche.service';

@Injectable({
  providedIn: 'root',
})
export class RicercaPraticheService {
  /** Oggetto contenente i valori costanti per il componente inserisci pratica. */
  IP_C = InserisciPraticaConsts;

  /**
   * Costruttore.
   */
  constructor(
    private _logger: LoggerService,
    private _navigationHelper: NavigationHelperService,
    private _pratiche: PraticheService
  ) {}

  /**
   * Funzione che scarica e indirizza l'utente verso la modifica di una pratica.
   * @param config IRModificaPratica con i dati di configurazione per gestire il processo di modifica pratica.
   */
  richiediModificaPratica(config: IRModificaPratica) {
    // Verifico l'input
    if (!config) {
      // Blocco il flusso
      return;
    }

    // Estraggo le proprietà
    const { idRiscossione, stepConfig, snapshotConfigs, currentNav } = config;
    // Verifico che esistano le proprietà
    if (!idRiscossione || !stepConfig || !snapshotConfigs || !currentNav) {
      // Blocco il flusso
      return;
    }

    // Scarico i dati della pratica
    this._pratiche.getPraticaEDatiTecnici(idRiscossione).subscribe({
      next: (pedt: PraticaEDatiTecnici) => {
        // Aggiungo all'oggetto di configurazione i dati della pratica
        config.pedt = pedt;
        // Richiamo la funzione che gestirà la modifica della pratica
        this.vaiAModificaPratica(config);
        // #
      },
      error: (e: RiscaServerError) => {
        // Definisco il title
        const t = `RiscaRicercaPraticheService getPraticaEDatiTecnici`;
        // Richiamo la funzione di gestione errori dal server
        this._logger.error(t, e);
        // #
      },
    });
  }

  /**
   * Funzione di comodo invocata al termine del caricamento dei dati della pratica e dei dati tecnici.
   * @param config IRModificaPratica con i dati di configurazione per gestire il processo di modifica pratica.
   */
  private vaiAModificaPratica(config: IRModificaPratica) {
    // Estraggo le proprietà
    const { pedt, stepConfig, snapshotConfigs, currentNav } = config;
    // Verifico che esistano le proprietà
    if (!pedt || !stepConfig || !snapshotConfigs || !currentNav) {
      // Blocco il flusso
      return;
    }

    // Definisco lo state da passare alla pratica
    const state: IPraticaRouteParams = {
      navTarget: currentNav,
      pageTarget: RiscaAzioniPratica.inserisciPratica,
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

    // Definisco le informazioni per il salvataggio dello step
    const step = this.generateIJourneyStep(stepConfig, target);

    // Estraggo dallo step le informazioni da mantenere al ritorno sul componente della ricerca avanzata
    const raSK = step.stateKeeper;
    // Definisco un oggetto specifico per le logiche di ritorno di back sulla macro sezione della pratica
    const pSK = {
      // Quando verrà ricaricata la rotta col l'indietro, devo attivare la nav della ricerca del richiedente (semplice, avanzata)
      navTarget: currentNav,
    };

    // Come definizione dello state keeper definisco le informazioni per la pagina della pratica
    step.stateKeeper = pSK;
    // Aggiungo dinamicamente la chiave specifica per il recuper dati per la gestione della ricerca avanzata
    step.stateKeeper[PraticaRouteKeys.ricercaAvanzata] = raSK;

    // Utilizzo il servizio per aggiungere uno step al journey
    this._navigationHelper.stepForward(step, snapshotConfigs, true);
  }

  /**
   * Funzione di supporto per la generazione dell'oggetto per la navigazione.
   * @param target AppRoutes che identifica la rotta per la quale navigare.
   * @param state any che definisce lo state da passare alla rotta di destinazione.
   * @returns IStepNavigation come configurazione della navigazione.
   */
  private navigationConfigs(target: AppRoutes, state?: any): IStepNavigation {
    // Genero/ritorno l'oggetto per la navigazione
    return this._navigationHelper.navigationConfigs(target, state);
  }

  /**
   * Funzione di comodo per la generazione delle configurazioni per il salvataggio di uno step.
   * @param step IJStepConfig con i dati per lo step.
   * @param target IStepNavigation con i dati per lo step.
   * @returns IJourneyStep con la configurazione per lo step.
   */
  private generateIJourneyStep(
    step: IJStepConfig,
    target: IStepNavigation
  ): IJourneyStep {
    // Genero/ritorno l'oggetto per la navigazione
    return this._navigationHelper.generateIJourneyStep(step, target);
  }
}
