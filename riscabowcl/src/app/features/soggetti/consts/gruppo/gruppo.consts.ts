import { JourneySnapshot } from '../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes } from '../../../../shared/utilities';

/**
 * Intefaccia che definisce le proprietà dell'oggetto di costanti per il componente gruppo.component.ts.
 */
interface IGruppoConsts {
  /** Campi form */
  NOME_GRUPPO: string;
  COMPONENTI_GRUPPO: string;
  /** Label campi */
  LABEL_NOME_GRUPPO: string;

  /** Titoli per la pagina. */
  TITLE_GRUPPO: string;
  TITLE_INSERISCI_GRUPPO: string;

  /** Configurazioni per bottoni */
  TITLE_INDIRIZZO_SPEDIZIONE: string;
  INDIRIZZO_SPEDIZIONE_PRI: string;
  INDIRIZZO_SPEDIZIONE_ALT: string;
  RECAPITO_PRINCIPALE: string;
  RECAPITO_ALTERNATIVO: string;

  /** Navigazione */
  NAVIGATION_CONFIG: IJStepConfig;
  SNAPSHOT_CONFIG: JourneySnapshot;
}

/**
 * Oggetto costante contenente una serie di costanti per il componente form-cerca-soggetto.
 */
export const GruppoConsts: IGruppoConsts = {
  /** Costante che rappresenta il campo del form: nomeGruppo. */
  NOME_GRUPPO: 'nomeGruppo',
  /** Costante che rappresenta il campo del form: componentiGruppo. */
  COMPONENTI_GRUPPO: 'componentiGruppo',

  /** Costante che rappresenta la label per il campo del form: codiceFiscale. */
  LABEL_NOME_GRUPPO: 'Nome gruppo',

  /** Titoli per la pagina. */
  TITLE_GRUPPO: 'Gruppo',
  TITLE_INSERISCI_GRUPPO: 'Inserisci gruppo',

  /** Definisce il titolo per l'indirizzo di spedizione da mostrare nel tooltip */
  TITLE_INDIRIZZO_SPEDIZIONE: 'Indirizzo spedizione',
  /** Definisce il nome della action per l'indirizzo di spedizione da uare nel bottone della tabella dei recapiti */
  INDIRIZZO_SPEDIZIONE_PRI: 'indirizzospedizione',
  /** Definisce il nome della action per l'indirizzo di spedizione da uare nel bottone della tabella dei recapiti alle righe dei recapiti alternativi*/
  INDIRIZZO_SPEDIZIONE_ALT: 'indirizzospedizionesub',
  /** Costante che definisce il valore per la colonna "Tipo Recapito" per un recapito principale */
  RECAPITO_PRINCIPALE: 'Principale',
  /** Costante che definisce il valore per la colonna "Tipo Recapito" per un recapito alternativo */
  RECAPITO_ALTERNATIVO: 'Alternativo',

  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: {
    componentId: 'gruppo_component',
    name: 'Gruppo',
    routeStep: AppRoutes.gruppo,
    stepCaller: AppCallers.gruppo,
  },
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: {
    saveFunc: false,
    mapping: {
      _gruppoConfigs: true,
      modalita: true,
      nomeGruppo: true,
    },
  },
};
