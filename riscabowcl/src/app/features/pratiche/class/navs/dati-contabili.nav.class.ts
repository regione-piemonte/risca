import {
  IRiscaNav,
  RiscaNavClass,
} from '../../../../shared/classes/risca-nav/risca-nav.class';
import {
  IRiscaNavItem,
  RiscaDatiContabili,
} from '../../../../shared/utilities';

/**
 * Classe utilizzata per la gestione della barra di navigazione.
 */
export class DatiContabiliNavClass
  extends RiscaNavClass<RiscaDatiContabili>
  implements IRiscaNav
{
  /** Costante che identifica una sezione dei dati contabili. */
  DT_C_ST_DEBITORI = RiscaDatiContabili.statiDebitori;
  /** Costante che identifica una sezione dei dati contabili. */
  DT_C_VERSAMENTI = RiscaDatiContabili.versamenti;
  /** Costante che identifica una sezione dei dati contabili. */
  DT_C_RIMBORSI = RiscaDatiContabili.rimborsi;
  /** Costante che identifica una sezione dei dati contabili. */
  DT_C_ACCERTAMENTI = RiscaDatiContabili.accertamenti;
  /** Costante che identifica una sezione dei dati contabili. */
  DT_C_ST_DEBITORIO = RiscaDatiContabili.statoDebitorio;
  /** Costante che identifica una sezione dei dati contabili. */
  DT_C_INS_ST_DEBITORIO = RiscaDatiContabili.inserimentoStatoDebitorio;
  /** Costante che identifica una sezione dei dati contabili. */
  DT_C_NAP = RiscaDatiContabili.nap;
  /** Costante che identifica una sezione dei dati contabili. */
  DT_C_CALCOLA_INTERESSI = RiscaDatiContabili.calcolaInteressi;

  /** Costante che identifica il componente da visualizzare per i dati contabili. */
  ST_DEBITORI_COMPONENT: string = 'appStatiDebitori';
  /** Costante che identifica il componente da visualizzare per i dati contabili. */
  PAGAMENTI_SD_COMPONENT: string = 'appPagamentiSD';
  /** Costante che identifica il componente da visualizzare per i dati contabili. */
  RIMBORSI_COMPONENT: string = 'appRimborsi';
  /** Costante che identifica il componente da visualizzare per i dati contabili. */
  ACCERTAMENTI_COMPONENT: string = 'appAccertamenti';
  /** Costante che identifica il componente da visualizzare per i dati contabili. */
  ST_DEBITORIO_COMPONENT: string = 'appStatoDebitorio';
  /** Costante che identifica il componente da visualizzare per i dati contabili. */
  INS_ST_DEBITORIO_COMPONENT: string = 'appInserimentoStatoDebitorio';
  /** Costante che identifica il componente da visualizzare per i dati contabili. */
  NAP_COMPONENT: string = 'appNap';

  /** IRiscaNav contenente la configurazione per la tab dei dati contabili. */
  statiDebitori: IRiscaNavItem<RiscaDatiContabili> = {
    ngbNavItem: this.DT_C_ST_DEBITORI,
    label: 'Stati debitori',
    component: this.ST_DEBITORI_COMPONENT,
    default: false,
  };
  /** IRiscaNav contenente la configurazione per la tab dei dati contabili. */
  versamenti: IRiscaNavItem<RiscaDatiContabili> = {
    ngbNavItem: this.DT_C_VERSAMENTI,
    label: 'Pagamenti',
    component: this.PAGAMENTI_SD_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab dei dati contabili. */
  rimborsi: IRiscaNavItem<RiscaDatiContabili> = {
    ngbNavItem: this.DT_C_RIMBORSI,
    label: 'Rimborsi',
    component: this.RIMBORSI_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab dei dati contabili. */
  accertamenti: IRiscaNavItem<RiscaDatiContabili> = {
    ngbNavItem: this.DT_C_ACCERTAMENTI,
    label: 'Accertamenti',
    component: this.ACCERTAMENTI_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab dei dati contabili. */
  statoDebitorio: IRiscaNavItem<RiscaDatiContabili> = {
    ngbNavItem: this.DT_C_ST_DEBITORIO,
    label: 'Stato debitorio',
    component: this.ST_DEBITORIO_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab dei dati contabili. */
  inserimentoStatoDebitorio: IRiscaNavItem<RiscaDatiContabili> = {
    ngbNavItem: this.DT_C_INS_ST_DEBITORIO,
    label: 'Inserimento stato debitorio',
    component: this.INS_ST_DEBITORIO_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab dei dati contabili. */
  nap: IRiscaNavItem<RiscaDatiContabili> = {
    ngbNavItem: this.DT_C_NAP,
    label: 'NAP',
    component: this.NAP_COMPONENT,
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
    this.addNav(this.statiDebitori);
    this.addNav(this.versamenti);
    this.addNav(this.rimborsi);
    this.addNav(this.accertamenti);
    this.addNav(this.statoDebitorio);
    this.addNav(this.inserimentoStatoDebitorio);
    this.addNav(this.nap);
  }
}
