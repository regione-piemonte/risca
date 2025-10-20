import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from '../../core/services/user.service';

/**
 * Guard che gestisce la possibilità d'accedere alle route in base al ruolo dell'utente.
 */
@Injectable()
export class AuthGuard implements CanActivate {
  /**
   * Costruttore.
   */
  constructor(private _router: Router, private _user: UserService) {}

  /**
   * canActivate.
   */
  canActivate(): boolean {
    // Verifico se sono stati scaricati i dati per utente e ambito
    const existA = this._user.idAmbito !== undefined;

    // Stampo un log di debug
    if (!existA) {
      // Lancio il routing verso l'unautorized
      this._router.navigateByUrl('/');
    }

    // Se entrambe le informazioni esistono ritorno true
    return existA;
  }
}
