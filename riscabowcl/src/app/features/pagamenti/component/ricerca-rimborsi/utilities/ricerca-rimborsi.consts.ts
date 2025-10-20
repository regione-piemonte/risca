import { RiscaButtonConfig, AppRoutes, AppCallers, AppClaimants } from "../../../../../shared/utilities";
import { IJStepConfig, IJourneySnapshot } from "../../../../../core/interfaces/navigation-helper/navigation-helper.interfaces";

/**
 * Classe contenente le costanti per il componente associato.
 */
export class RicercaRimborsiConsts {
  /** Costante che definisce il nome del campo form: attivitaRimborsoSD. */
  ATTIVITA_RIMBORSO_SD: string = 'attivitaRimborsoSD';

  /** Costante che definisce la label del campo form: attivitaRimborsoSD. */
  LABEL_ATTIVITA_RIMBORSO_SD: string =
    'Attività da applicare agli stati  debitori selezionati';

  /** Costante che definisce il nome della proprietà per la select del campo form: attivitaRimborsoSD. */
  PROPERTY_ATTIVITA_RIMBORSO_SD: string = 'des_attivita_stato_deb';

  BTN_APPLICA_ATTIVITA_RIMBORSO: RiscaButtonConfig = {
    label: 'Applica ai selezionati',
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
    componentId: 'ricerca_rimborsi_component',
    name: 'Ricerca rimborsi',
    routeStep: AppRoutes.gestioneVerifiche,
    stepCaller: AppCallers.ricercaRimborsi,
  };
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: IJourneySnapshot = {
    source: undefined,
    saveFunc: false,
    mapping: {
      paginazioneSDRimborsi: true,
      filtriSDRimborsi: true,
      filtriSDRIniziali: true,
      statiDebitoriSelezionati: true,
      statiDebitoriSelezionatiRows: true,
      allSDSelezionati: true,
      sdSelezionatiApriChiudi: true,
      attivitaStatoDebSelezionato: true,
    },
  };
}
