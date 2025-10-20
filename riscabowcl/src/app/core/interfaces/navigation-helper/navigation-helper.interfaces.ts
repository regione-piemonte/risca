import { DynamicObjBoolean, AppRoutes, AppCallers, AppClaimants, DynamicObjAny } from "../../../shared/utilities";
import { JourneySnapshot, JourneyStep } from "../../classes/navigation-helper/navigation-helper.classes";

/**
 * Interfaccia che definisce le proprietà per la configurazione della classe JourneySnapshot.
 */
export interface IJourneySnapshot {
  /** any contenente la sorgente dati per la snapshot. */
  source: any;
  /** DynamicObjBoolean con il mapping delle proprietà da salvare. */
  mapping?: DynamicObjBoolean;
  /** Boolean che definisce se salvare anche le funzioni. */
  saveFunc?: boolean;
}

/**
 * Interfaccia che definisce le proprietà per la configurazione di uno step di un componente.
 */
export interface IJStepConfig {
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
}

/**
 * Interfaccia che definisce le informazioni base per la navigazione.
 */
export interface IStepNavigation {
  /** AppRoutes che definisce la rotta di destinazione successiva allo step. */
  routeTarget: AppRoutes;
  /** any che definisce le infomazioni da passare alla rotta di destinazione. Verrà gestita come lo "state" del routing di Angular. */
  stateTarget?: any;
  /** JourneyStep che definisce la configurazione dello step successivo. */
  jStepTarget?: IJStepConfig;
}

/**
 * Interfaccia che definisce le proprietà per la gestione del routing dell'applicazione mediante journey.
 * L'interfaccia gestisce le informazioni per aggiungere uno step al journey.
 */
export interface IJourneyStep extends IJStepConfig, IStepNavigation {}

/**
 * Interfaccia che definisce le proprietà per la gestione del routing dell'applicazione mediante journey.
 * L'interfaccia gestisce le informazioni per ritornare all'ultimo step del journey.
 */
export interface IJToStep {
  /** any che definisce le infomazioni da passare alla rotta di destinazione. Verrà gestita come lo "state" del routing di Angular. */
  stateTarget?: any;
}
