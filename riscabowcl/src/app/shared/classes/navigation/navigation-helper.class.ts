import {
  IJourneySnapshot,
  IJourneyStep,
  IJStepConfig,
  IStepNavigation,
} from '../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { NavigationHelperService } from '../../../core/services/navigation-helper/navigation-helper.service';
import { AppRoutes } from '../../utilities';

/**
 * Classe di comodo che definisce delle funzioni comuni per il supporto delle logiche di gestione del routing Risca.
 */
export class NavigationHelperClass {
  /** IJStepConfig che definisce la configurazione dell'oggetto per la navigazione. */
  protected stepConfig: IJStepConfig;

  /**
   * Costruttore.
   */
  constructor(
    protected _navigationHelper: NavigationHelperService,
    stepConfig?: IJStepConfig
  ) {
    if (stepConfig) {
      this.stepConfig = stepConfig;
    }
  }

  /**
   * #######################
   * GESTIONE DELLA SNAPSHOT
   * #######################
   */

  /**
   * Funzione che va a caricare i dati della snapshot, se esiste uno step nel servizio di routing applicativo.
   * @param obj any che definisce l'oggetto da valorizzare.
   */
  loadJSnapshot(obj: any) {
    // Verifico input e servizio
    if (!obj || !this.stepConfig) {
      // Blocco il flusso
      return;
    }

    // Recupero il component id per il load dello step
    const componentId: string = this.stepConfig.componentId;
    // Effettuo il restore di possibili snapshot
    this._navigationHelper.loadStep(componentId, obj);
  }

  /**
   * Funzione di supporto che attiva il cambio di route, mediante journey
   * @param target INavigation con i dati per la navigazione.
   * @param snapshot IJourneySnapshot con le informazioni per il salvataggio dei dati per una snapshot.
   * @returns Promise<boolean> con il risultato della navigazione.
   */
  journeyNavigation(
    target: IStepNavigation,
    snapshot?: IJourneySnapshot
  ): Promise<boolean> {
    // Verifico input e servizio
    if (!this.stepConfig) {
      // Blocco il flusso
      return;
    }

    // Definisco le informazioni per il salvataggio dello step
    const step = this.generateIJourneyStep(this.stepConfig, target);
    // Utilizzo il servizio per aggiungere uno step al journey
    return this._navigationHelper.stepForward(step, snapshot);
  }

  /**
   * Funzione di supporto per la generazione dell'oggetto per la navigazione.
   * @param target AppRoutes che identifica la rotta per la quale navigare.
   * @param state any che definisce lo state da passare alla rotta di destinazione.
   * @returns IStepNavigation come configurazione della navigazione.
   */
  navigationConfigs(target: AppRoutes, state?: any): IStepNavigation {
    // Genero/ritorno l'oggetto per la navigazione
    return this._navigationHelper.navigationConfigs(target, state);
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
    // Genero/ritorno l'oggetto per la navigazione
    return this._navigationHelper.generateIJourneyStep(step, target);
  }
}
