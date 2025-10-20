import {
  IRiscaNav,
  RiscaNavClass,
} from '../../../../shared/classes/risca-nav/risca-nav.class';
import {
  IRiscaNavItem,
  RiscaStatoDebitorio,
} from '../../../../shared/utilities';

/**
 * Classe utilizzata per la gestione della barra di navigazione.
 */
export class StatoDebitorioNavClass
  extends RiscaNavClass<RiscaStatoDebitorio>
  implements IRiscaNav
{
  /** Costante che identifica una sezione dello stato debitorio. */
  DT_C_ST_GEN_AMM_DIL = RiscaStatoDebitorio.generaliAmministrativiDilazione;
  /** Costante che identifica una sezione dello stato debitorio. */
  DT_C_DATI_ANAG = RiscaStatoDebitorio.datiAnagrafici;
  /** Costante che identifica una sezione dello stato debitorio. */
  DT_C_ANNUALITA = RiscaStatoDebitorio.annualita;

  /** Costante che identifica il componente da visualizzare per lo stato debitorio. */
  GEN_AMM_DIL_COMPONENT: string = 'appGeneraliAmministrativiDilazione';
  /** Costante che identifica il componente da visualizzare per lo stato debitorio. */
  DATI_ANAG_COMPONENT: string = 'appDatiAnagrafici';
  /** Costante che identifica il componente da visualizzare per lo stato debitorio. */
  ANNUALITA_COMPONENT: string = 'appAnnualita';

  /** IRiscaNav contenente la configurazione per la tab dello stato debitorio. */
  genAmmDilazione: IRiscaNavItem<RiscaStatoDebitorio> = {
    ngbNavItem: this.DT_C_ST_GEN_AMM_DIL,
    label: 'Dati generali/amministrativi/dilazione',
    component: this.GEN_AMM_DIL_COMPONENT,
    default: true,
  };
  /** IRiscaNav contenente la configurazione per la tab dello stato debitorio. */
  datiAnagrafici: IRiscaNavItem<RiscaStatoDebitorio> = {
    ngbNavItem: this.DT_C_DATI_ANAG,
    label: 'Dati anagrafici',
    component: this.DATI_ANAG_COMPONENT,
  };
  /** IRiscaNav contenente la configurazione per la tab dello stato debitorio. */
  annualita: IRiscaNavItem<RiscaStatoDebitorio> = {
    ngbNavItem: this.DT_C_ANNUALITA,
    label: 'Annualità',
    component: this.ANNUALITA_COMPONENT,
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
    this.addNav(this.genAmmDilazione);
    this.addNav(this.datiAnagrafici);
    this.addNav(this.annualita);
  }
}
