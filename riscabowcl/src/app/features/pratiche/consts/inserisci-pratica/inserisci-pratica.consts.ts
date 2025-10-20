import { JourneySnapshot } from '../../../../core/classes/navigation-helper/navigation-helper.classes';
import { IJStepConfig } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AppCallers, AppRoutes } from '../../../../shared/utilities';

/**
 * Interfaccia per il mapping dell'oggetto QuadriTecniciConsts
 */
interface IInserisciPraticaConsts {
  FORM_KEY_PARENT: string;
  FORM_KEY_CHILD_DGA: string;
  FORM_KEY_CHILD_DA: string;
  FORM_KEY_CHILD_DT: string;
  FORM_KEY_CHILD_DC: string;
  FORM_KEY_CHILD_DOC: string;
  HEAD_DATI_GEN_AMM: string;
  HEAD_DATI_ANAG: string;
  HEAD_DATI_TECNICI: string;

  NAVIGATION_CONFIG: IJStepConfig;
  SNAPSHOT_CONFIG: JourneySnapshot;
}

/**
 * Oggetto costante contenente una serie di costanti per il componente quadri-tecnici.
 */
export const InserisciPraticaConsts: IInserisciPraticaConsts = {
  FORM_KEY_PARENT: 'KEY_INS_PRATICA',
  FORM_KEY_CHILD_DGA: 'KEY_DATI_GEN_AMM',
  FORM_KEY_CHILD_DA: 'KEY_DATI_ANAG',
  FORM_KEY_CHILD_DT: 'KEY_DATI_TECNICI_PER_AMBITO',
  FORM_KEY_CHILD_DC: 'KEY_DATI_CONTABILI',
  FORM_KEY_CHILD_DOC: 'KEY_DOCUMENTI_ALLEGATI',
  /** Costante che identifica il titolo di sezione errori per: generali amministrativi. */
  HEAD_DATI_GEN_AMM: '<strong>Sezione: Dati generali/amministrativi</strong>',
  /** Costante che identifica il titolo di sezione errori per: dati anagrafici. */
  HEAD_DATI_ANAG: '<strong>Sezione: Dati anagrafici</strong>',
  /** Costante che identifica il titolo di sezione errori per: dati tecnici. */
  HEAD_DATI_TECNICI: '<strong>Sezione: Dati tecnici</strong>',

  /** IJStepConfig con la configurazione per la navigazione. */
  NAVIGATION_CONFIG: {
    componentId: 'inserisci_pratica_component',
    name: 'Pratica',
    routeStep: AppRoutes.gestionePratiche,
    stepCaller: AppCallers.pratica,
  },
  /** JourneySnapshot con la configurazione per la snapshot del componente. */
  SNAPSHOT_CONFIG: {
    saveFunc: false,
    mapping: {
      praticaEDatiTecnici: true,
      idComponenteDt: true,
      datiGenAmm: true,
      datiTecnici: true,
      datiAnagrafici: true,
      istanzaPratica: true,
      istanzaPraticaNav: true,
      compDatiTecnici: true,
      datiGenAmmConfigs: true,
      datiTecniciConfigs: true,
      datiAnagraficiConfigs: true,
      modalita: true,
    },
  },
};
