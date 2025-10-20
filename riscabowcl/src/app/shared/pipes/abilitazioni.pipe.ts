import { Pipe, PipeTransform } from '@angular/core';
import { AmbitoService } from '../../features/ambito/services';
import {
  AbilitaDAGruppi,
  AbilitaDASezioni,
  AbilitaDASoggetti,
} from '../utilities';

/**
 * Pipe dedicata alla gestione delle abilitazioni per i dati anagrafici.
 */
@Pipe({ name: 'abilitazioneDA' })
export class AbilitazioneDAPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _ambito: AmbitoService) {}

  /**
   * Funzione che recupera un'abilitazione, data la sezione e il tipo di abilitazione richiesta.
   * @param sezione AbilitaDASezioni che identifica la sezione.
   * @param abilitazione AbilitaDASoggetti | AbilitaDAGruppi che identifica l'abilitazione.
   * @returns boolean che definisce il tipo di abilitazione.
   */
  transform(
    sezione: AbilitaDASezioni,
    abilitazione: AbilitaDASoggetti | AbilitaDAGruppi
  ): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    const abilitato = this._ambito.getAbilitazioneSoggettiGruppi(
      sezione,
      abilitazione
    );
    // Ritorno l'abilitazione
    return abilitato;
  }
}

/**
 * Pipe dedicata alla gestione delle abilitazioni per i dati anagrafici, per il flag: isGestioneAbilitata (soggetti).
 */
@Pipe({ name: 'isGestioneAbilitataADAS' })
export class ADASIsGestioneAbilitataPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _ambito: AmbitoService) {}

  /**
   * Funzione che recupera un'abilitazione, data la sezione e il tipo di abilitazione richiesta.
   * @returns boolean che definisce il tipo di abilitazione.
   */
  transform(): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    const abilitato = this._ambito.getAbilitazioneSoggettiGruppi(
      AbilitaDASezioni.soggetti,
      AbilitaDASoggetti.isGestioneAbilitata
    );
    // Ritorno l'abilitazione
    return abilitato;
  }
}

/**
 * Pipe dedicata alla gestione delle abilitazioni per i dati anagrafici, per il flag: isAbilitato (gruppo).
 */
@Pipe({ name: 'isAbilitatoADAG' })
export class ADAGIsAbilitatoPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _ambito: AmbitoService) {}

  /**
   * Funzione che recupera un'abilitazione, data la sezione e il tipo di abilitazione richiesta.
   * @returns boolean che definisce il tipo di abilitazione.
   */
  transform(): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    const abilitato = this._ambito.getAbilitazioneSoggettiGruppi(
      AbilitaDASezioni.gruppi,
      AbilitaDAGruppi.isAbilitato
    );
    // Ritorno l'abilitazione
    return abilitato;
  }
}
