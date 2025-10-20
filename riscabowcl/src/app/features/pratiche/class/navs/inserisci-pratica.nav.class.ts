import {
  IRiscaNav,
  RiscaNavClass,
} from '../../../../shared/classes/risca-nav/risca-nav.class';
import {
  IRiscaNavItem,
  RiscaIstanzePratica,
} from '../../../../shared/utilities';

/**
 * Classe utilizzata per la gestione della barra di navigazione del componente pratica.
 */
export class InserisciPraticaNavClass
  extends RiscaNavClass<RiscaIstanzePratica>
  implements IRiscaNav
{
  /** Costante che identifica l'attuale istanza della pratica aperta: generali-amministrativi. */
  IST_PRT_GEN_AMM = RiscaIstanzePratica.generaliAmministrativi;
  /** Costante che identifica l'attuale istanza della pratica aperta: dati-anagrafici. */
  IST_PRT_DAT_ANA = RiscaIstanzePratica.datiAnagrafici;
  /** Costante che identifica l'attuale istanza della pratica aperta: dati-tecnici. */
  IST_PRT_DAT_TEC = RiscaIstanzePratica.datiTecnici;
  /** Costante che identifica l'attuale istanza della pratica aperta: dati-contabili. */
  IST_PRT_DAT_CON = RiscaIstanzePratica.datiContabili;
  /** Costante che identifica l'attuale istanza della pratica aperta: documenti-allegati. */
  IST_PRT_DOC_ALL = RiscaIstanzePratica.documentiAllegati;

  /** Costante che identifica il componente da visualizzare: appGeneraliAmministrativi. */
  GEN_AMM_COMPONENT: string = 'appGeneraliAmministrativi';
  /** Costante che identifica il componente da visualizzare: appDatiAnagrafici. */
  DAT_ANA_COMPONENT: string = 'appDatiAnagrafici';
  /** Costante che identifica il componente da visualizzare: appQuadriTecnici. */
  QUA_TEC_COMPONENT: string = 'appQuadriTecnici';
  /** Costante che identifica il componente da visualizzare: appDatiContabili. */
  DAT_CON_COMPONENT: string = 'appDatiContabili';
  /** Costante che identifica il componente da visualizzare: appDocumenti. */
  DOC_COMPONENT: string = 'appDocumenti';

  /** IRiscaNav contenente la configurazione per la tab: appGeneraliAmministrativi. */
  private generaliAmministrativi: IRiscaNavItem<RiscaIstanzePratica> = {
    ngbNavItem: this.IST_PRT_GEN_AMM,
    label: 'Dati generali/amministrativi',
    component: this.GEN_AMM_COMPONENT,
    default: true,
  };
  /** IRiscaNav contenente la configurazione per la tab: appDatiAnagrafici. */
  private datiAnagrafici: IRiscaNavItem<RiscaIstanzePratica> = {
    ngbNavItem: this.IST_PRT_DAT_ANA,
    label: 'Dati anagrafici',
    component: this.DAT_ANA_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab: appQuadriTecnici. */
  private quadriTecnici: IRiscaNavItem<RiscaIstanzePratica> = {
    ngbNavItem: this.IST_PRT_DAT_TEC,
    label: 'Dati tecnici',
    component: this.QUA_TEC_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab: appDatiContabili. */
  private datiContabili: IRiscaNavItem<RiscaIstanzePratica> = {
    ngbNavItem: this.IST_PRT_DAT_CON,
    label: 'Dati contabili',
    component: this.DAT_CON_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab: appDocumenti. */
  private documenti: IRiscaNavItem<RiscaIstanzePratica> = {
    ngbNavItem: this.IST_PRT_DOC_ALL,
    label: 'Documenti allegati',
    component: this.DOC_COMPONENT,
    disabled: false,
  };

  /**
   * Costruttore
   */
  constructor() {
    // Richiamo il super
    super();
    // Lancio la funzione di generazione delle navs
    this.generaNavs();
  }

  /**
   * Funzione che genera le navs per la navigazione.
   */
  generaNavs() {
    // Verifico le sezioni d'aggiungere
    this.addNav(this.generaliAmministrativi);
    this.addNav(this.datiAnagrafici);
    this.addNav(this.quadriTecnici);
    this.addNav(this.datiContabili);
    this.addNav(this.documenti);
  }
}
