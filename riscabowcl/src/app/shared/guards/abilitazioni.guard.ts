import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate } from '@angular/router';
import { LoggerService } from '../../core/services/logger.service';
import { AmbitoService } from '../../features/ambito/services';
import { IAbilitazioniDA } from '../utilities';

/**
 * Guard che gestisce la possibilità d'accedere alle route in base al ruolo dell'utente.
 */
@Injectable()
export class AbilitazioniGuard implements CanActivate {
  /**
   * Costruttore.
   */
  constructor(private _logger: LoggerService, private _ambito: AmbitoService) {}

  /**
   * canActivate.
   */
  canActivate(route: ActivatedRouteSnapshot): boolean {
    // Recupero dai parametri di route le abilitazioni richieste
    const abilitazioni: IAbilitazioniDA[] = route.data?.abilitazioni;

    // Verifico che esista una sezione
    if (!abilitazioni) {
      // Log dell'errore
      this._logger.warning(
        'abilitazioni.guard - canActivate',
        'Nessune abilitazioni definite per la route'
      );
      // Blocco del routing
      return false;
    }

    // Dall'array di abilitazioni creo una mappa di boolean (che rappresentano se abilitati o no)
    const abilitati = abilitazioni.map((a) =>
      this._ambito.getAbilitazioneSoggettiGruppi(a.sezione, a.abilitazione)
    );

    // Verifico se esiste anche solo un'abilitazione false
    const block = abilitati.some((a) => a === false);

    // Ritorno l'abilitazione
    return !block;
  }
}
