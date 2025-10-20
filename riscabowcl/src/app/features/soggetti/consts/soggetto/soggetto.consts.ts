import { IJStepConfig } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes } from '../../../../shared/utilities';

/**
 * Interfaccia che definisce la costruzione dell'oggetto SoggettoConsts.
 */
interface ISoggettoConsts {
  FORM_KEY_PARENT_SOGGETTO: string;
  FORM_KEY_CHILD_DATI_SOGGETTO: string;
  FORM_KEY_CHILD_RECAPITO: string;
  FORM_KEY_CHILD_CONTATTI: string;

  FORM_KEY_PARENT_SOGGETTO_RECAPITI_ALTERNATIVI: string;
  FORM_KEY_CHILD_RECAPITO_ALTERNATIVO: string;
  FORM_KEY_CHILD_CONTATTI_ALTERNATIVO: string;

  TITLE_INDIRIZZO_SPEDIZIONE: string;
  INDIRIZZO_SPEDIZIONE: string;

  ACC_NUO_REC_ALT: string;

  NAVIGATION_CONFIG: IJStepConfig;
}

/**
 * Oggetto costante contenente una serie di costanti per il componente dati-anagrafici.
 */
export const SoggettoConsts: ISoggettoConsts = {
  /** Chiavi per la gestione dei form principali. */
  FORM_KEY_PARENT_SOGGETTO: 'FORM_KEY_PARENT_SOGGETTO',
  FORM_KEY_CHILD_DATI_SOGGETTO: 'FORM_KEY_CHILD_DATI_SOGGETTO',
  FORM_KEY_CHILD_RECAPITO: 'FORM_KEY_CHILD_RECAPITO',
  FORM_KEY_CHILD_CONTATTI: 'FORM_KEY_CHILD_CONTATTI',

  /** Chiavi per la gestione dei form per i recapiti alternativi. */
  FORM_KEY_PARENT_SOGGETTO_RECAPITI_ALTERNATIVI:
    'FORM_KEY_PARENT_SOGGETTO_RECAPITI_ALTERNATIVI',
  FORM_KEY_CHILD_RECAPITO_ALTERNATIVO: 'FORM_KEY_CHILD_RECAPITO_ALTERNATIVO',
  FORM_KEY_CHILD_CONTATTI_ALTERNATIVO: 'FORM_KEY_CHILD_CONTATTI_ALTERNATIVO',

  /** Definisce il titolo per l'indirizzo di spedizione */
  TITLE_INDIRIZZO_SPEDIZIONE: '',
  /** Definisce il nome della action per l'indirizzo di spedizione da usare nel bottone della tabella dei recapiti */
  INDIRIZZO_SPEDIZIONE: 'indirizzospedizione',

  /** Costante string che definisce la label per l'accordion: nuovo recaptio alternativo. */
  ACC_NUO_REC_ALT: 'Nuovo recapito alternativo',

  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: {
    componentId: 'soggetto_component',
    name: 'Soggetto',
    routeStep: AppRoutes.soggetto,
    stepCaller: AppCallers.soggetto,
  },
};
