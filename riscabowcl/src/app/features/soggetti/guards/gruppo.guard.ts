import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { LoggerService } from '../../../core/services/logger.service';
import { AppRoutes } from '../../../shared/utilities';
import { GruppoSoggettoService } from '../../pratiche/service/dati-anagrafici/gruppo-soggetto.service';

/**
 * Guard che gestisce la possibilità d'accedere alle route per il componente gruppo.
 */
@Injectable()
export class GruppoGuard implements CanActivate {
  /**
   * Costruttore.
   */
  constructor(
    private _gruppo: GruppoSoggettoService,
    private _logger: LoggerService,
    private _router: Router
  ) {}

  /**
   * canActivate.
   */
  canActivate(): boolean {
    // Recupero dal servizio l'abilitazione per i gruppi
    const isAbilitato = this._gruppo.isAbilitato;
    // Loggo se l'utente è o non è abilitato
    if (isAbilitato) {
      // Log
      this._logger.success('GruppoGuard', { isAbilitato })
      // #
    } else {
      // Log
      this._logger.warning('GruppoGuard', { isAbilitato });
      // Redirect alla pagina di default
      this._router.navigateByUrl(AppRoutes.root);
    }

    // Return
    return isAbilitato;
  }
}
