import { JourneySnapshot } from '../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes } from '../../../../shared/utilities';

/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il home.
 */
interface IHomeConsts {
  TARGET: string;
  CAMPO_LIBERO: string;

  LABEL_CODICE_UTENZA: string;
  LABEL_NUMERO_AVVISO_PAGAMENTO: string;
  LABEL_NUMERO_PRATICA: string;
  LABEL_CODICE_FISCALE: string;
  LABEL_RAGIONE_SOCIALE_COGNOME: string;
  LABEL_CODICE_AVVISO: string;

  VOCE_SOGGETTO_GRUPPO: string;
  VOCE_SOLO_SOGGETTO: string;
  VOCE_SOLO_GRUPPO: string;

  TAB_SOG_PRATICHE_COLLEGATE: string;
  TAB_SOG_NUOVA_PRATICA: string;
  TAB_GRP_PRATICHE_COLLEGATE: string;
  TAB_GRP_NUOVA_PRATICA: string;

  NAVIGATION_CONFIG: IJStepConfig;
  SNAPSHOT_CONFIG: JourneySnapshot;
}

/**
 * Oggetto contenente una serie di costanti per il componente home.
 */
export const HomeConsts: IHomeConsts = {
  /** Costante che definisce il nome del campo form: target. */
  TARGET: 'target',
  /** Costante che definisce il nome del campo form: campoLibero. */
  CAMPO_LIBERO: 'campoLibero',

  /** Costante che definisce la label per il campo form: codiceUtenza. */
  LABEL_CODICE_UTENZA: 'Codice utenza',
  /** Costante che definisce la label per il campo form: numeroAvvisoPagamento. */
  LABEL_NUMERO_AVVISO_PAGAMENTO: 'NAP',
  /** Costante che definisce la label per il campo form: numeroPratica. */
  LABEL_NUMERO_PRATICA: 'Numero pratica',
  /** Costante che definisce la label per il campo form: codiceFiscale. */
  LABEL_CODICE_FISCALE: 'Codice fiscale',
  /** Costante che definisce la label per il campo form: ragioneSocialeOCognome. */
  LABEL_RAGIONE_SOCIALE_COGNOME: 'Ragione sociale/Cognome',
  /** Costante che definisce la label per il campo form: codiceAvviso. */
  LABEL_CODICE_AVVISO: 'Codice avviso',

  /** Costante che definisce la label per il radio button collegato al campo form: target. */
  VOCE_SOGGETTO_GRUPPO: 'cerca Soggetto e Gruppo',
  /** Costante che definisce la label per il radio button collegato al campo form: target. */
  VOCE_SOLO_SOGGETTO: 'cerca solo Soggetto',
  /** Costante che definisce la label per il radio button collegato al campo form: target. */
  VOCE_SOLO_GRUPPO: 'cerca solo Gruppo',

  /** Costante che definisce l'id per l'azione custom della tabella soggetti: soggetto-pratiche-collegate. */
  TAB_SOG_PRATICHE_COLLEGATE: 'soggetto-pratiche_collegate',
  /** Costante che definisce l'id per l'azione custom della tabella soggetti: soggetto-nuova-pratica. */
  TAB_SOG_NUOVA_PRATICA: 'soggetto-nuova-pratica',
  /** Costante che definisce l'id per l'azione custom della tabella gruppi: gruppo-pratiche-collegate. */
  TAB_GRP_PRATICHE_COLLEGATE: 'gruppo-pratiche_collegate',
  /** Costante che definisce l'id per l'azione custom della tabella gruppi: gruppo-nuova-pratica. */
  TAB_GRP_NUOVA_PRATICA: 'gruppo-nuova-pratica',

  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: {
    componentId: 'home_component',
    name: 'Home',
    routeStep: AppRoutes.home,
    stepCaller: AppCallers.home,
  },
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: {
    saveFunc: false,
    mapping: {
      ricercaSGData: true,
      ricercaPS: true,
      ricercaSG: true,
    },
  },
};
