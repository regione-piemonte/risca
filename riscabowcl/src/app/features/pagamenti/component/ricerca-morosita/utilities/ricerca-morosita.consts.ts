import {
  AppCallers,
  AppRoutes,
  RiscaButtonConfig,
  AppClaimants,
} from '../../../../../shared/utilities';
import { IJStepConfig, IJourneySnapshot } from '../../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';

/**
 * Classe contenente le costanti per il componente associato.
 */
export class RicercaMorositaConsts {
  /** Costante che definisce il nome del campo form: attivitaMorositaSD. */
  ATTIVITA_MOROSITA_SD: string = 'attivitaMorositaSD';

  /** Costante che definisce la label del campo form: attivitaMorositaSD. */
  LABEL_ATTIVITA_MOROSITA_SD: string =
    'Attività da applicare agli stati  debitori selezionati';

  /** Costante che definisce il nome della proprietà per la select del campo form: attivitaMorositaSD. */
  PROPERTY_ATTIVITA_MOROSITA_SD: string = 'des_attivita_stato_deb';

  /** RiscaButtonConfig con la configurazione per il pulsante di applicazione attività alle morosità selezionate. */
  BTN_APPLICA_ATTIVITA_MOROSITA: RiscaButtonConfig = {
    label: 'Applica ai selezionati',
  };

  /** RiscaButtonConfig con la configurazione per il pulsante di creazione ruolo. */
  BTN_CREA_RUOLO: RiscaButtonConfig = {
    label: 'Crea ruolo',
  };

  /** Costante che definisce l'azione di scarico file nella tabella. */
  SCARICA_FILE_ACTION: string = 'scarica_file';

  /** Costante come label per l'accordion degli stati debitori selezionati. */
  ACCORDION_SD_SELEZIONATI: string = 'Stati debitori selezionati';
  /** Costante come label per la sezione degli stati debitori selezionati, quando l'utente ha premuto su "seleziona tutti". */
  LABEL_TUTTI_SD_SELEZIONATI: string = 'Selezionati tutti gli stati debitori.';
  /** Costante come label per la sezione degli stati debitori selezionati, quando l'utente non ha ancora selezionato elementi dalla tabella. */
  LABEL_NESSUN_SD_SELEZIONATO: string = 'Nessun stato debitorio selezionato.';

  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: IJStepConfig = {
    componentId: 'ricerca_morosita_component',
    name: 'Ricerca morosità',
    routeStep: AppRoutes.gestioneVerifiche,
    stepCaller: AppCallers.ricercaMorosita,
  };
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: IJourneySnapshot = {
    source: undefined,
    saveFunc: false,
    mapping: {
      paginazioneSDMorosita: true,
      filtriSDMorosita: true,
      filtriSDMIniziali: true,
      statiDebitoriSelezionati: true,
      statiDebitoriSelezionatiRows: true,
      allSDSelezionati: true,
      sdSelezionatiApriChiudi: true,
      attivitaStatoDebSelezionato: true,
    },
  };
}
