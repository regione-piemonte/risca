import {
  AppCallers,
  AppClaimants,
  AppRoutes,
  DynamicObjAny,
  DynamicObjBoolean,
} from '../../../shared/utilities';
import {
  IJourneySnapshot,
  IJourneyStep,
  IJStepConfig,
} from '../../interfaces/navigation-helper/navigation-helper.interfaces';

/**
 * Classe che definisce le proprietà per la gestione dello "snapshot" di un componente.
 */
export class JourneySnapshot {
  /** any contenente i dati dello snapshot. */
  data?: any;
  /** DynamicObjBoolean con il mapping delle proprietà da salvare. */
  mapping?: DynamicObjBoolean;
  /** Boolean che definisce se salvare anche le funzioni. Default: false. */
  saveFunc?: boolean;

  /**
   * Costruttore.
   */
  constructor(c?: IJourneySnapshot) {
    this.mapping = c?.mapping;
    this.saveFunc = c?.saveFunc !== undefined ? c?.saveFunc : false;
  }
}

/**
 * Classe che definisce le proprietà per la gestione del routing dell'applicazione mediante journey.
 * L'interfaccia gestisce le informazioni che verranno aggiunte come step all'interno di journey.
 */
export class JourneyStep {
  /** String identificativo del componente che aggiunge lo step. */
  componentId: string;
  /** String che definisce la label associata a questo step. */
  name: string;
  /** AppRoutes che definisce la rotta del componente che sta aggiungendo lo step al journey. */
  routeStep: AppRoutes;
  /** JourneySnapshot che definisce le informazioni da salvare per il componente che aggiunge lo step al journey. */
  snapshot?: JourneySnapshot;
  /** AppCallers che definisce chi è il chiamante per lo step. */
  stepCaller?: AppCallers;
  /** AppClaimants che definisce quale elemento specifico ha richiesto la navigazione che ha portato alla generazione dello step. */
  stepClaimant?: AppClaimants;
  /** DynamicObjectAny che permette d'inserire all'iterno di uno step un insieme d'informazioni che possono essere gestite in qualunque modo dall'utente. */
  extras?: DynamicObjAny;
  /** DynamicObjectAny che permette di definire una serie d'informazioni da ricaricare automaticamente nel momento in cui viene effettuata una navigazione sullo step. Se il chiamante definisce delle proprietà con lo stesso nome di quelle conservate in questo oggetto, le proprietà di QUESTO oggetto verranno sovrascritte. */
  stateKeeper?: DynamicObjAny;

  /** AppRoutes che definisce la rotta di destinazione successiva allo step. */
  routeTarget: AppRoutes;
  /** any che definisce le infomazioni da passare alla rotta di destinazione. Verrà gestita come lo "state" del routing di Angular. */
  stateTarget?: any;
  /** IJStepConfig che definisce la configurazione dello step successivo. */
  jStepTarget?: IJStepConfig;

  /**
   * Costruttore.
   */
  constructor(c?: IJourneyStep) {
    if (!c) {
      return;
    }
    this.componentId = c.componentId;
    this.name = c.name;
    this.routeStep = c.routeStep;
    this.snapshot = c.snapshot;
    this.stepCaller = c.stepCaller;
    this.stepClaimant = c.stepClaimant;
    this.extras = c.extras;
    this.stateKeeper = c.stateKeeper;

    this.routeTarget = c.routeTarget;
    this.stateTarget = c.stateTarget;
    this.jStepTarget = c.jStepTarget;
  }
}

/**
 * Classe che definisce le regole per il check di uno step all'interno di journey.
 */
export class IJStepCheckRules {
  constructor(
    /** Boolean che definisce la regola sul check del componentId dello step. */
    public checkCId: boolean = true,
    /** Boolean che definisce la regola sul check dello routeStep dello step. */
    public checkRS: boolean = true
  ) {}
}
