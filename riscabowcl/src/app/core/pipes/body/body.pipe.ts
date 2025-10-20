import { Pipe, PipeTransform } from '@angular/core';
import { Router } from '@angular/router';
import { IRiscaBodyNav } from '../../components/body/utilities/body.interfaces';
import { BodyConsts } from '../../consts/body/body.consts';

/**
 * Pipe che gestisce l'attivazione del menù in base all'url passato.
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto. 
 */
@Pipe({ name: 'riscaBodyActiveNav' })
export class RiscaBodyActiveNavPipe implements PipeTransform {
  /** Costante con la definizione delle proprietà del componente body. */
  private B_C = BodyConsts;
  /** Costante che definisce la proprietà per il check della route. */
  private PROPERTY_CHECK = 'basePath';
  /** Costante che definisce la proprietà per il l'id della route. */
  private PROPERTY_LINK = 'path';

  /**
   * Funzione che esegue la funzione passata come parametro.
   * @param url string che definisce l'url attivo dal servizio di Router.
   * @param navs Array di IRiscaBodyNav contenente le possibili voci di menù.
   * @param router Servizio di Router di Angular.
   * @param selector (r: Router | string, n: IRiscaBodyNav) => boolean che definisce le logiche per la verifica di attivazione nav.
   * @returns string che definisce l'id per selezionare la nav.
   */
  transform(
    url: string,
    navs: IRiscaBodyNav[],
    router?: Router,
    selector?: (r: Router | string, n: IRiscaBodyNav) => boolean
  ): string {
    // Verifico l'input
    if (!url || !navs) return '';
    // Verifico che l'url non sia impostato sul default
    if (url === '/') return this.B_C.NAV_PATH_DEFAULT;

    // Definisco le proprietà di check e link
    const check = this.PROPERTY_CHECK;
    const link = this.PROPERTY_LINK;

    // Definisco un flag di supporto
    let useDefaultSelector = false;
    // Verifico se è stato definito il servizio Router e una funzione di selezione custom
    if (!router || !selector) {
      // Aggiorno il flag
      useDefaultSelector = true;
      // Definisco le logiche di default del selector
      selector = (r: Router | string, nav: IRiscaBodyNav) => {
        // Definisco l'input come string
        const urlPath = r as string;
        // Recupero il path dell'oggetto
        const checkPath = nav[check];

        // Verifico se il path del nav è contenuto all'interno dell'url partendo dalla posizione 0
        // Nota bene: per evitare che domini con lo stesso nome di un possibile nav principale coincida, si verifica che la posizione trovata sia 0.
        const isNav = urlPath.indexOf(checkPath) === 0;

        // Ritorno il flag
        return isNav;
      };
    }

    // Effettuo una find del possibile oggetto di riferimento per la route
    const parentNav = navs.find((n) => {
      // Verifico quale funzione richiamare
      return useDefaultSelector ? selector(url, n) : selector(router, n);
    });

    // Ritorno la proprietà del nav
    return parentNav ? parentNav[link] : url;
  }
}
