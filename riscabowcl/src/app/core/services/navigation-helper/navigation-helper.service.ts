import { LocationStrategy } from '@angular/common';
import { Injectable } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { clone } from 'lodash';
import { from, of, Subject } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { RiscaUtilitiesService } from '../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppCallers,
  AppClaimants,
  AppRoutes,
  DynamicObjAny,
  RiscaMethod,
} from '../../../shared/utilities';
import {
  IJStepCheckRules,
  JourneySnapshot,
  JourneyStep,
} from '../../classes/navigation-helper/navigation-helper.classes';
import {
  IJourneySnapshot,
  IJourneyStep,
  IJStepConfig,
  IJToStep,
  IStepNavigation,
} from '../../interfaces/navigation-helper/navigation-helper.interfaces';
import { NavigationhelperConsts } from './utilities/navigation-helper.consts';

/**
 * Servizio dedicato alla gestione del routing applicativo.
 * Il servizio nasce dall'esigenza di gestire in maniera particolare la componentistica dell'applicazione, permettendo la gestione personalizzata del back del broswer e del pre-caricamento delle informazioni dei componenti.
 */
@Injectable({
  providedIn: 'root',
})
export class NavigationHelperService {
  /** NavigationhelperConsts con le costanti del servizio. */
  NH_C = new NavigationhelperConsts();

  /** Subject che permette di registrarsi all'evento di avvenuta modifica sugli step di journey. */
  onJourneyChange$ = new Subject<JourneyStep[]>();

  /**
   * Costruttore.
   */
  constructor(
    private _locationStrategy: LocationStrategy,
    private _riscaUtilities: RiscaUtilitiesService,
    private _router: Router
  ) {}

  /**
   * ###########################
   * FUNZIONI E GESTIONE JOURNEY
   * ###########################
   */

  /**
   * Array di oggetti JourneyStep che contiene le informazione per lo stato di tracciamento "journey" (viaggio).
   * La variabile journey permette di tracciare in formato storico, la navigazione dell'utente secondo dei path specifici (gestiti dalle apposite funzionalità).
   * L'idea è quella di creare una sorta di "history" del browser, ma gestita applicativamente. Questo permette il caricamento e il recupero degli stati dei componente (snapshot).
   * La gestione avviene tramite "steps" (passi) e lo stato dei dati viene gestito dalle "snapshots" (fotografie), come se fosse un vero e proprio viaggio, per la quale se si vuole andare a vedere il passato, è sufficiente vedere le fotografie fatte ai componenti.
   */
  private _journey: JourneyStep[] = [];

  /**
   * Getter per _journey.
   * @returns Array di oggetti JourneyStep che contiene le informazione per lo stato di tracciamento "journey" (viaggio).
   */
  get journey(): JourneyStep[] {
    // Ritorno l'array journey creando una copia, non passando il riferimento
    return clone(this._journey);
  }

  /**
   * Funzione di reset per journey
   */
  resetJourney() {
    // Imposto ad array vuoto journey
    this._journey = [];
    // Emetto l'evento di cambio di journey
    this.journeyChanged(this.journey);
  }

  /**
   * Funzione di supporto che verifica se all'interno di journey è già presente uno step per componentId o una route.
   * @param idOrRoute string che definisce il componentId o il routeStep verso la quale navigare.
   * @param checkRules IJStepCheckRules contenente le regole di check. Per defualt tutte le regole sono attive.
   * @returns boolean con l'indicazione di presenza (true) o di assenza (false) dello step in journey.
   */
  stepInJourney(idOrRoute: string, checkRules?: IJStepCheckRules): boolean {
    // Verifico l'input
    if (idOrRoute == undefined) {
      // Ritorno il default
      return false;
    }
    // Ricerco all'interno dell'array l'oggetto
    return this.indexStepInJourney(idOrRoute, checkRules) !== -1;
  }

  /**
   * Funzione di supporto che verifica se all'interno di journey è già presente uno step per componentId o per route step.
   * Verrà tornato l'indice posizionale nell'array, altrimenti -1.
   * @param idOrRoute string che definisce il componentId o il routeStep verso la quale navigare.
   * @param checkRules IJStepCheckRules contenente le regole di check. Per defualt tutte le regole sono attive.
   * @returns number con l'indice posizionale all'interno dell'array journey.
   */
  indexStepInJourney(idOrRoute: string, checkRules?: IJStepCheckRules): number {
    // Verifico l'input
    if (idOrRoute == undefined) {
      // Ritorno il default
      return -1;
    }

    // Verifico il check sulle regole
    checkRules = checkRules ? checkRules : new IJStepCheckRules();
    // Definisco il flag di check
    const cCId = checkRules.checkCId;
    const cRS = checkRules.checkRS;

    // Tento di recuperare da journey lo step
    return this._journey.findIndex((s: JourneyStep) => {
      // Definisco le condizioni di comparazione
      const sameCI = cCId && s.componentId === idOrRoute;
      const sameRS = cRS && s.routeStep === idOrRoute;
      // Ritorno il check sugli step
      return sameCI || sameRS;
    });
  }

  /**
   * Funzione che aggiunge a journey un nuovo step.
   * La funzione registrerà i dati dello step e attiverà le logiche per il routing verso la nuova rotta.
   * @param step IJourneyStep che definisce le configurazioni per il salvataggio dei dati di uno step di journey.
   * @param snapshot IJourneySnapshot che definisce le configurazioni per il salvataggio dei dati per le informazioni dello snapshot contenute poi all'interno dello step.
   * @param sameRoute boolean che definisce se la rotta di navigazione è la stessa rispetto a quella su cui ci si trova. Default: false.
   * @returns Promise<boolean> con il risultato della navigazione alla pagina.
   */
  stepForward(
    step: IJourneyStep,
    snapshot?: IJourneySnapshot,
    sameRoute?: boolean
  ): Promise<boolean> {
    // Verifico l'input
    if (!step) {
      // Blocco il flusso
      return of(false).toPromise();
    }

    // Verifico che lo step non sia già presnete nell'array
    if (this.stepInJourney(step.componentId)) {
      // Blocco il flusso
      return of(false).toPromise();
    }

    // Gestisco il flag sameRoute
    sameRoute = sameRoute != undefined ? sameRoute : false;

    // Verifico se nella configurazione dello step non esiste una snapshot ed è richiesta generazione mediante parametro
    if (!step.snapshot && snapshot) {
      // Definisco una variabile per le informazioni da salvare nello step per snapshot
      let jSnapshot: JourneySnapshot;
      // L'oggetto esiste, genero la configurazione
      jSnapshot = this.createSnapshot(snapshot);
      // Aggiungo allo step i dati per la snapshot
      step.snapshot = jSnapshot;
    }

    // Genero l'oggetto per lo step
    const jStep = new JourneyStep(step);
    // Aggiungo a journey uno step
    this._journey.push(jStep);
    // Emetto l'evento di cambio di journey
    this.journeyChanged(this.journey);

    // Definisco la route per la navigazione
    const route = step.routeTarget;
    // Definisco l'oggetto per la navigation extra
    const extras: NavigationExtras = { state: step?.stateTarget };

    // Navigo alla pagina desiderata, passando lo state
    return this.stepNavigation(route, extras, sameRoute);
  }

  /**
   * Funzione che effettua il caricamento della pagina precedente a quella dalla quale viene invocato il metodo.
   * Verranno recuperate le informazioni dell'ultimo step disponibile e verrà effettuata la navigazione.
   * @param prevStep IJToStep contenente le informazioni da utilizzare per la navigazione alla pagina precedente.
   * @param sameRoute boolean che definisce se la rotta di navigazione è la stessa rispetto a quella su cui ci si trova. Default: false.
   * @returns Promise<boolean> con il risultato della navigazione alla pagina.
   */
  stepBack(prevStep?: IJToStep, sameRoute?: boolean): Promise<boolean> {
    // Recupero l'indice dell'ultimo elemento
    const iJStep = this._journey.length;

    // Verifico se ci sono step nel journey
    if (iJStep === 0) {
      // Non ci sono pagine da caricare
      return of(false).toPromise();
    }

    // Gestisco il flag sameRoute
    sameRoute = sameRoute != undefined ? sameRoute : false;

    // Recupero l'ultimo step del journey
    const lastStep = this.getLastStep();
    // Definisco le informazioni da passare nello state dello step
    const stepState = this.generateStepState(lastStep, prevStep);

    // Definisco la route per la navigazione
    const route = lastStep.routeStep;
    // Definisco l'oggetto per la navigation extra
    const extras: NavigationExtras = { state: stepState };

    // Navigo alla pagina desiderata, passando lo state
    return this.stepNavigation(route, extras, sameRoute);
  }

  /**
   * Funzione di supporto che gestisce la navigazione di uno step, in base ai parametri in ingresso.
   * @param route AppRoutes con la rotta da navigare.
   * @param extras NavigationExtras con le informazioni da passare al servizio di routing di Angular.
   * @param sameRoute boolean definisce se è necessaria una gestione particolare della navigazione.
   * @returns Promise<boolean> che si ritorna (true) se la navigazione va a buon fine, (false) se fallisce, o viene rejected se si verificano errori.
   */
  private stepNavigation(
    route: AppRoutes,
    extras: NavigationExtras,
    sameRoute: boolean
  ): Promise<boolean> {
    // Verifico la gestione della stessa rotta
    if (!sameRoute) {
      // Navigazione normale
      return this.goTo(route, extras);
      // #
    } else {
      // Navigazione stessa rotta
      return this.stepNavigationSameRoute(route, extras);
    }
  }

  /**
   * Funzione di supporto che gestisce la navigazione di uno step sulla stessa rotta del richiedente.
   * @param route AppRoutes con la rotta da navigare.
   * @param extras NavigationExtras con le informazioni da passare al servizio di routing di Angular.
   * @returns Promise<boolean> che si ritorna (true) se la navigazione va a buon fine, (false) se fallisce, o viene rejected se si verificano errori.
   */
  private stepNavigationSameRoute(
    route: AppRoutes,
    extras: NavigationExtras
  ): Promise<boolean> {
    // Definisco la rotta per la root applicativa
    const root = AppRoutes.root;
    // Definisco una configurazione specifica per gestire la rotta
    const rootExtras: NavigationExtras = { skipLocationChange: true };

    // Navigazione stessa rotta
    return from(this.goTo(root, rootExtras))
      .pipe(
        switchMap((result: boolean) => {
          // Verifico il risultato
          if (result) {
            // Procedo con la chiamata alla rotta effettiva
            return from(this.goTo(route, extras));
            // #
          } else {
            // Ritorno false
            return of(result);
          }
        })
      )
      .toPromise();
  }

  /**
   * Funzione che recupera i dati di uno step salvato in journey.
   * @param idOrRoute string che definisce il componentId o il routeStep per il recupero dello step.
   * @param checkRules IJStepCheckRules contenente le regole di check. Per defualt tutte le regole sono attive.
   * @returns JourneyStep con i dati dello step salvato in journey.
   */
  getStep(idOrRoute: string, checkRules?: IJStepCheckRules): JourneyStep {
    // Recupero lo step
    const iStep = this.indexStepInJourney(idOrRoute, checkRules);
    // Verifico che l'elemento esista nel journey
    if (iStep === -1) {
      // Niente da ritornare
      return;
    }
    // Step trovato, ritorno le informazioni
    return this._journey[iStep];
  }

  /**
   * Funzione che permette di aggiungere uno step alla navigazione journey.
   * @param step JourneyStep con i dati dello step d'aggiungere.
   */
  addStep(step: JourneyStep) {
    // Verifico l'input
    if (!step) {
      // Blocco le logiche
      return;
    }

    // Aggiungo a journey uno step
    this._journey.push(step);
    // Emetto l'evento di cambio di journey
    this.journeyChanged(this.journey);
  }

  /**
   * Funzione che permette di creare, dall'interfaccia di configurazione e di aggiungere uno step alla navigazione journey.
   * @param stepConfig IJourneyStep con i dati dello step da creare e aggiungere.
   */
  createAndAddStep(stepConfig: JourneyStep) {
    // Verifico l'input
    if (!stepConfig) {
      // Blocco le logiche
      return;
    }

    // Genero l'oggetto per lo step
    const jStep = new JourneyStep(stepConfig);
    // Aggiungo a journey uno step
    this.addStep(jStep);
  }

  /**
   * Funzione che rimuove i dati di uno step salvato in journey.
   * @param idOrRoute string che definisce il componentId o il routeStep per il recupero dello step.
   * @param removeNexts boolean che definisce se tutti gli step successivi posizionalmente da quello da rimuovere sono anch'essi da rimuovere. Default: false.
   * @param checkRules IJStepCheckRules contenente le regole di check. Per defualt tutte le regole sono attive.
   * @returns JourneyStep con i dati dello step rimosso da journey.
   */
  removeStep(
    idOrRoute: string,
    removeNexts: boolean = false,
    checkRules?: IJStepCheckRules
  ): JourneyStep {
    // Recupero lo step
    const iStep = this.indexStepInJourney(idOrRoute, checkRules);
    // Verifico che l'elemento esista nel journey
    if (iStep === -1) {
      // Niente da ritornare
      return;
    }

    // Recupero lo step
    const stepRemoved = this._journey[iStep];
    // Verifico se rimuovere solo lo step o anche quelli successivi
    if (removeNexts) {
      // Rimuovo lo step e gli step successivi
      this._journey.splice(iStep);
      // #
    } else {
      // Rimuovo tutti gli step successivi
      this._journey.splice(iStep, 1);
    }
    // Emetto l'evento di cambio di journey
    this.journeyChanged(this.journey);

    // Step rimosso, ritorno le informazioni rimosse
    return stepRemoved;
  }

  /**
   * Funzione che permette di navigare verso una pagina.
   * @param route AppRoutes con la rotta verso la quale navigare.
   * @param extras NavigationExtras contenente le informazioni da passare alla rotta.
   * @returns Promise<boolean> che si ritorna (true) se la navigazione va a buon fine, (false) se fallisce, o viene rejected se si verificano errori.
   */
  goTo(route: AppRoutes, extras?: NavigationExtras): Promise<boolean> {
    // Navigazione alla rotta
    return this._router.navigateByUrl(route, extras);
  }

  /**
   * Funzione che permette di navigare verso uno step qualunque del journey.
   * Se esiste lo step di navigazione, tutti gli step successivi posizionalmente allo step verso la quale verrà gestita la rotta verranno rimossi da journey.
   * @param idOrRoute string che definisce il componentId o il routeStep verso la quale navigare.
   * @param toStep IJToStep contenente le informazioni da utilizzare per la navigazione alla pagina.
   * @returns Promise<boolean> con il risultato della navigazione alla pagina.
   */
  goToStep(idOrRoute: string, toStep?: IJToStep): Promise<boolean> {
    // Recupero lo step per id component o step route
    const iStep = this.indexStepInJourney(idOrRoute);

    // Verifico se esiste lo step
    if (iStep === -1) {
      // Nessuno step trovato
      return of(false).toPromise();
    }

    // Lo step esiste, recupero l'oggetto
    const step = this._journey[iStep];
    // Rimuovo tutti gli step successivi
    this._journey.splice(iStep + 1);
    // Emetto l'evento di cambio di journey
    this.journeyChanged(this.journey);

    // Definisco le informazioni da passare nello state dello step
    const stepState = this.generateStepState(step, toStep);
    // Definisco l'oggetto per la navigation extra
    const extras: NavigationExtras = { state: stepState };

    // Navigo verso lo step
    return this.goTo(step.routeStep, extras);
  }

  /**
   * Funzione che carica le informazioni della snapshot di uno step all'interno di journey.
   * Una volta caricati i dati, l'oggetto step verrà rimosso da journey perché vorrà dire che ci si trova all'interno dello step.
   * @param idOrRoute string che definisce il componentId o il routeStep per il caricamento delle informazioni.
   * @param data any che definisce l'oggetto che verrà aggiornato con i dati della snapshot.
   * @param checkRules IJStepCheckRules contenente le regole di check. Per defualt tutte le regole sono attive.
   */
  loadStep(idOrRoute: string, data: any, checkRules?: IJStepCheckRules) {
    // Verifico l'input
    if (idOrRoute == undefined || data == undefined) {
      // Non posso caricare i dati della snapshot
      return;
    }

    // Verifico il check sulle regole
    checkRules = checkRules ? checkRules : new IJStepCheckRules();
    // Recupero lo step
    const iStep = this.indexStepInJourney(idOrRoute, checkRules);

    // Verifico se esiste lo step
    if (iStep === -1) {
      // Nessuno step trovato
      return;
    }

    // Lo step esiste, recupero l'oggetto
    const step = this._journey[iStep];
    // Rimuovo tutti gli step successivi
    this._journey.splice(iStep);
    // Emetto l'evento di cambio di journey
    this.journeyChanged(this.journey);

    // Effettuo il restore dei dati della snapshot
    this.restoreSnapshot(data, step.snapshot);
  }

  /**
   * Funzione che verifica se l'ultimo step è quello richiesto dall'input.
   * @param idOrRoute string che definisce il componentId o il routeStep per il recupero delle informazioni.
   * @param checkRules IJStepCheckRules contenente le regole di check. Per defualt tutte le regole sono attive.
   * @returns boolean che definisce il match tra step.
   */
  isLastStep(idOrRoute: string, checkRules?: IJStepCheckRules): boolean {
    // Recupero l'ultimo elemento
    const lastStepIndex = this.getLastStepIndex();

    // Verifico se esiste l'oggetto
    if (lastStepIndex >= 0) {
      // Recupero e ritorno se l'ultimo step è quello richiesto
      const reqStepIndex = this.indexStepInJourney(idOrRoute, checkRules);
      // Verifico se l'indice dell'ultimo elemento è lo stesso di quello richiesto
      return lastStepIndex === reqStepIndex;
      // #
    } else {
      // Nessun oggetto per la verifica
      return false;
    }
  }

  /**
   * Funzione che verifica se l'ultimo step è associato ad una specifico caller.
   * @param caller AppCallers per la veririca.
   * @returns boolean che definisce il match tra callers.
   */
  isLastCaller(caller: AppCallers): boolean {
    // Recupero l'ultimo elemento
    const lastStep = this.getLastStep();

    // Verifico se esiste l'oggetto
    if (lastStep) {
      // Verifico se la route dell'ultimo step è quello richiesto
      return lastStep.stepCaller === caller;
      // #
    } else {
      // Nessun oggetto per la verifica
      return false;
    }
  }

  /**
   * Funzione che verifica se l'ultimo step è associato ad una specifico claimant.
   * @param claimant AppClaimants per la veririca.
   * @returns boolean che definisce il match tra claimants.
   */
  isLastClaimant(claimant: AppClaimants): boolean {
    // Recupero l'ultimo elemento
    const lastStep = this.getLastStep();

    // Verifico se esiste l'oggetto
    if (lastStep) {
      // Verifico se la route dell'ultimo step è quello richiesto
      return lastStep.stepClaimant === claimant;
      // #
    } else {
      // Nessun oggetto per la verifica
      return false;
    }
  }

  /**
   * Funzione che recupera l'indice dell'ultimo step nella lista.
   * @returns number che definisce l'indice dell'ultimo step, se esiste.
   */
  getLastStepIndex(): number {
    // Recupero l'indice dell'ultimo elemento
    const iLastStep = this._journey.length - 1;
    // Recupero l'ultimo indice
    return iLastStep;
  }

  /**
   * Funzione che recupera l'ultimo step nella lista.
   * @returns JourneyStep se esiste l'ultimo step.
   */
  getLastStep(): JourneyStep {
    // Recupero l'indice dell'ultimo elemento
    const iLastStep = this.getLastStepIndex();
    // Verifico che esista almeno un elemento
    if (iLastStep >= 0) {
      // Recupero l'ultimo elemento
      const lastStep = this._journey[iLastStep];
      // Ritorno l'oggetto
      return lastStep;
      // #
    } else {
      // Non esiste nessuno step
      return undefined;
    }
  }

  /**
   * Funzione che recupera dall'ultimo step in jounery, l'oggetto extras.
   * @returns DynamicObjAny contenete le informazioni custom salvate nello step.
   */
  getLastExtras(): DynamicObjAny {
    // Recupero l'ultimo oggetto
    const lastStep = this.getLastStep();
    // Ritorno la proprietà extras
    return lastStep?.extras;
  }

  /**
   * Funzione di supporto che genera lo state da passare alla rotta dello step.
   * @param step JourneyStep dalla quale estrarre state preservati.
   * @param toStep IJToStep con la definizione di parametri specifici per la rotta.
   * @returns DynamicObjectAny che definisce le informazioni da passare allo state.
   */
  private generateStepState(
    step: JourneyStep,
    toStep?: IJToStep
  ): DynamicObjAny {
    // Creo un oggetto strutturato, tornando il merge tra le proprietà degli state
    return { ...step.stateKeeper, ...toStep?.stateTarget };
  }

  /**
   * Funzione di supporto per la generazione dell'oggetto per la navigazione.
   * @param target AppRoutes che identifica la rotta per la quale navigare.
   * @param state any che definisce lo state da passare alla rotta di destinazione.
   * @returns IStepNavigation come configurazione della navigazione.
   */
  navigationConfigs(target: AppRoutes, state?: any): IStepNavigation {
    // Genero/ritorno l'oggetto per la navigazione
    return {
      routeTarget: target,
      stateTarget: state,
    };
  }

  /**
   * Funzione di comodo per la generazione delle configurazioni per il salvataggio di uno step.
   * @param step IJStepConfig con i dati per lo step.
   * @param target IStepNavigation con i dati per lo step.
   * @returns IJourneyStep con la configurazione per lo step.
   */
  generateIJourneyStep(
    step: IJStepConfig,
    target: IStepNavigation
  ): IJourneyStep {
    // Compongo l'oggetto
    return { ...step, ...target };
  }

  /**
   * Funzione di comodo che gestisce il controllo e l'emissione delle informazioni per la variabile journey.
   * @param journey JourneyStep[] con la lista degli step contenuti al suo interno.
   */
  journeyChanged(journey: JourneyStep[]) {
    // Verifico l'input
    const jSteps = journey ? journey : [];
    // Emetto l'evento con i nuovi step
    this.onJourneyChange$.next(jSteps);
  }

  /**
   * ############################
   * FUNZIONI E GESTIONE SNAPSHOT
   * ############################
   */

  /**
   * Funzione che permette di creare lo snapshot di un oggetto.
   * @param cSnapshot IJourneySnapshot contenente le informazioni per la generazione della snapshot.
   * @returns JourneySnapshot con l'oggetto snapshot generato.
   */
  private createSnapshot(cSnapshot: IJourneySnapshot): JourneySnapshot {
    // Verifico l'input
    if (!cSnapshot) {
      // Blocco la funzione
      return;
    }

    // Creo il contenitore
    const snapshotData: any = {};

    // Estraggo i parametri dall'oggetto di configurazione
    let { source, mapping, saveFunc } = cSnapshot;
    // Verifico saveFunc
    saveFunc = saveFunc == undefined ? false : saveFunc;
    // Definisco una funzione che salvi il dato nello snapshot
    const salvaDato = (snapshot: any, property: any, information: any) => {
      // Verifico il tipo del dato
      const isFunc = this._riscaUtilities.isFunction(information);
      // Verifico se è una funzione ed è richiesto il salvataggio
      if (!isFunc || (isFunc && saveFunc)) {
        // Salvo il dato
        snapshot[property] = information;
      }
    };

    // Verifico la tipologia di salvataggio d'applicare: dalla mappa o dell'intero oggetto
    if (mapping) {
      // Vado ad iterare le proprietà dell'oggetto
      for (let [property, toSave] of Object.entries(mapping)) {
        // Verifico se la proprietà è da salvare
        if (toSave) {
          // Recupero il dato dall'oggetto
          const info = source[property];
          // Controllo ed eventualmente salvo il dato
          salvaDato(snapshotData, property, info);
        }
      }
    } else {
      // Vado ad iterare le proprietà dell'oggetto
      for (let [property, data] of Object.entries(source)) {
        // Controllo ed eventualmente salvo il dato
        salvaDato(snapshotData, property, data);
      }
    }

    // Creo il contenitore
    const snapshot: JourneySnapshot = {
      data: snapshotData,
      mapping: mapping,
      saveFunc: saveFunc,
    };

    // Ritorno l'oggetto generato
    return snapshot;
  }

  /**
   * Funzione che permette di effettuare una "restore" dati recuperando le informazioni salvate in una snapshot.
   * La modifica dati avviene per referenza dell'oggetto in input.
   * @param base any che verrà popolato con le informazioni della snapshot.
   * @param snapshot JourneySnapshot che definisce l'oggetto dati dalla quale ripristinare i dati.
   */
  private restoreSnapshot(base: any, snapshot: JourneySnapshot) {
    // Verifico l'input
    if (!base || !snapshot) {
      // Non si può effettuare la restore
      return;
    }

    // Estraggo l'oggetto che contiene le informazioni della snapshot
    const { data } = snapshot;
    // Verifico se esiste l'oggetto dati contenente la snapshot
    if (!data) {
      // Nessun dato da ripristinare
      return;
    }

    // Itero le proprietà dell'oggetto e le assegno alla base
    for (let [property, value] of Object.entries(data)) {
      // Assegno alla base il dato
      base[property] = value;
    }
  }

  /**
   * #################################################
   * FUNZIONI E GESTIONE PER IL TASTO BACK DEL BROWSER
   * #################################################
   */

  /** Funzione invocata al click del pulsante "indietro" del browser. */
  private _overrideBrowserBack: () => void;

  /**
   * Funzione che gestisce l'override delle logiche per il tasto back del browser.
   * E' possibile definire una funzione di fallback da utilizzare al posto del back del browser.
   * Se non è definita la funzione di fallback, questa funzione semplicemente bloccherà il pulsante back del browser.
   * NOTA BENE: il parametro deve passare una funzione lambda, che definisca al suo interno le logiche da gestire.
   *            Un esempio sarà: _riscaRouting.overrideBrowserBack(() => { this.tornaIndietro() });
   *            La funzione this.tornaIndietro() permetterà di mantenere il contesto del componente, altrimenti tutte le variabili all'interno saranno undefined.
   */
  overrideBrowserBack() {
    // Resetta le configurazioni per la route corrente
    history.pushState(null, null, location.href);
    // Intercetto il back del browser
    this._locationStrategy.onPopState(() => {
      // Interrompo le logiche di back del browser
      history.pushState(null, null, location.href);
      // Richiamo la funzione locale di routing
      setTimeout(() => {
        // Richiamo possibili funzioni di back
        if (this._overrideBrowserBack) {
          // Lancio l'override del back del browser
          this._overrideBrowserBack();
        }
      });
    });
  }

  /**
   * Setta la funzione che fa l'override del tasto indietro del browser
   * @param back RiscaMethod che definisce le logiche per il tasto "back" del browser.
   */
  setBrowserBack(back: RiscaMethod) {
    // Assegno localmente il back dell'override
    this._overrideBrowserBack = back;
  }

  /**
   * Cancella la funzione che fa override del tasto indietro del browser.
   * E' consigliato richiamare questa funzione sull'ngOnDestroy del componente.
   */
  deleteBrowerBack() {
    // Rimuovo le logiche del back del browser
    this._overrideBrowserBack = undefined;
  }

  /**
   * #############################################
   * FUNZIONI DI GESTIONE DEI PARAMETRI IN ROUTING
   * #############################################
   */

  /**
   * Funzione che verifica e recupera lo state dal servizio Router.
   * Se non sono forniti valori, verrà ritornato {}.
   * @param r Router service di Angular.
   * @returns T contenente i dati dentro Router per il suo state, o oggetto vuoto se non è stato trovato niente.
   */
  getRouterState(r: Router): any {
    // Richiamo la funzione dentro il servizio di utilit
    return this._riscaUtilities.getRouterState(r);
  }

  /**
   * Funzione che verifica e recupera il data dal servizio ActivatedRoute.
   * Se non sono forniti valori, verrà ritornato {}.
   * @param ar ActivatedRoute service di Angular.
   * @returns DynamicObjAny contenente i dati dentro ActivatedRoute per il suo data, o oggetto vuoto se non è stato trovato niente.
   */
  getActivatedRouteData(ar: ActivatedRoute): DynamicObjAny {
    // Richiamo la funzione dentro il servizio di utilit
    return this._riscaUtilities.getActivatedRouteData(ar);
  }
}
