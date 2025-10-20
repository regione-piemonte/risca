import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { SoggettoVo } from '../../core/commons/vo/soggetto-vo';
import { LoggerService } from '../../core/services/logger.service';
import { ISoggettoRouteParams } from '../../features/soggetti/components/soggetto/utilities/soggetto.interfaces';
import { RiscaUtilitiesService } from '../services/risca/risca-utilities/risca-utilities.service';
import { AppRoutes } from '../utilities';

/**
 * Guard che gestisce la possibilità d'accedere alle route alle informazioni passate nello stato del router.
 * Per accedere alla pagina dei soggetti è necessario che siano configurate le informazioni di tipo soggetto e natura giuridica.
 */
@Injectable()
export class AccessoSoggettiGuard implements CanActivate {
  /**
   * Costruttore.
   */
  constructor(
    private _logger: LoggerService,
    private _router: Router,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * canActivate.
   */
  canActivate(): boolean {
    // Recupero i parametri dallo state della route
    const state: ISoggettoRouteParams = this._riscaUtilities.getRouterState(this._router);
    // Recupero le informazioni
    const { soggetto } = state;
    
    // Definisco una variabile tipizzata
    const da: SoggettoVo = soggetto;
    // Verifico i parametri, se esistono permetto la rotta
    if (da?.tipo_soggetto && da?.cf_soggetto) return true;

    // I dati sono mancanti o parziali, loggo la situazione
    this._logger.warning('AccessoSoggettiGuard - canActivate', {
      datiAnagrafici: da,
    });

    // Effettuo un redirect alla home page
    this._router.navigateByUrl(AppRoutes.home);
    // Blocco la navigazione
    return false;
  }
}
