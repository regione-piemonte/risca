import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';

/**
 * Guard che gestisce la possibilità d'accedere alle route in base ai dati nei servizi relativi alla pagina.
 */
@Injectable()
export class DettaglioMorositaGuard implements CanActivate {
  /**
   * Costruttore.
   */
  constructor(private _router: Router) {}

  /**
   * canActivate.
   */
  canActivate(): Observable<boolean> | Promise<boolean> | boolean {
    return true;
  }
}
