import { IRiscaNavItem } from '../../utilities';

/**
 * Interfaccia utilizzata per la gestione della barra di navigazione come interfaccia da implementare per tutte le classi di gestione nav.
 */
export interface IRiscaNav {
  generaNavs: (...args: any) => void;
}

/**
 * Classe utilizzata per la gestione della barra di navigazione come classe da estendere per tutte le classi di gestione nav.
 */
export class RiscaNavClass<T> {
  /** Array di IRiscaNav contenente le tab di navigazione. */
  private _navs: IRiscaNavItem<T>[] = [];

  /**
   * Costruttore
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * ######
   * GETTER
   * ######
   */

  /**
   * Getter per praticaNavs.
   */
  get navs(): IRiscaNavItem<T>[] {
    return this._navs;
  }

  /**
   * #######################
   * FUNZIONALITA' DI COMODO
   * #######################
   */

  /**
   * Funzione di add di un elemento nell'array delle navs.
   * @param nav IRiscaNavItem<T> d'aggiungere.
   */
  protected addNav(nav: IRiscaNavItem<T>) {
    this._navs.push(nav);
  }

  /**
   * Funzione che recupera un oggetto della nav.
   * Se viene passato un idLink, verrà ritornato uno specifico oggetto.
   * Se non specificato, verrà tornato il default.
   * @param ngbNavItem T per la ricerca della nav.
   * @returns IRiscaNavItem con l'oggetto della nav recuperato.
   */
  getNav(ngbNavItem?: T): IRiscaNavItem<T> {
    // Verifico l'input
    if (ngbNavItem) {
      // Tento di recuperare l'oggetto richiesto
      const nav = this._navs.find((n) => n.ngbNavItem === ngbNavItem);
      // Verifico che esista
      if (nav) {
        return nav;
      }
    }

    // Nessun ritorno specifico, cerco il default
    const defaultNav = this.getNavDefault();
    // Verifico esista il default
    if (defaultNav) {
      return defaultNav;
    }
    // Non esiste il default, tento di recuperare il primo oggetto nella lista
    return this._navs[0];
  }

  /**
   * Funzione che ritorna il nav di default.
   * Per questo menù, il default è: inserisci pratica.
   * @returns IRiscaNavItem con il default.
   */
  getNavDefault(): IRiscaNavItem<T> {
    // Effettuo la ricerca del default
    return this._navs.find((n) => n.default);
  }
}
