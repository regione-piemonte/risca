import { Pipe, PipeTransform } from '@angular/core';
import { IRiscaNavLinkConfig } from '../../utilities';

/**
 * Pipe dedicata gestione delle classi di stile per la posizione della nav.
 */
@Pipe({ name: 'riscaNavLinkClass' })
export class RiscaNavLinkClassPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che gestisce l'indice posizionale della nav e ritorna la classe di stile rischiesta dalla configurazione.
   * @param i number che definisce la posizione della nav.
   * @param array Array di any contenente la lista degli oggetti della nav.
   * @param config IRiscaNavLinkConfig contenente le classi per la posizione.
   * @returns string che definisce la classe di stile d'assegnare al nav.
   */
  transform(i: number, array: any[], config: IRiscaNavLinkConfig): string {
    // Verifico l'input
    if (i === undefined || !array || !config) {
      return '';
    }

    // Verifico la posizione della nav
    if (i === 0) {
      // Ritorno first
      return config.first || '';
      // #
    } else if (i === array.length - 1) {
      // Ritorn last
      return config.last || '';
      // #
    } else {
      // Ritorno center
      return config.center || '';
    }
  }
}
