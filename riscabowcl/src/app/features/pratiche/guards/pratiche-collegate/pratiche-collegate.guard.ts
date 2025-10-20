import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';
import {
  ILoadPraticheCollegate,
  PraticheCollegateService,
} from '../../service/pratiche-collegate/pratiche-collegate.service';

/**
 * Guard che gestisce la possibilità d'accedere alle route in base al ruolo dell'utente.
 */
@Injectable()
export class PraticheCollegateGuard implements CanActivate {
  /**
   * Costruttore.
   */
  constructor(
    private _praticheCollegate: PraticheCollegateService,
    private _router: Router
  ) {}

  /**
   * canActivate.
   */
  canActivate(): Observable<boolean> | Promise<boolean> | boolean {
    // Recupero i dati per il caricamento delle pratiche collegate
    const loadPC = this._praticheCollegate.getPraticheCollegateForLoad(
      this._router
    );

    // Lancio la funzione di check per lo state
    return this.checkPCLoader(loadPC);
  }

  /**
   * Funzione di check per lo state passato nel router per le pratiche collegate.
   * @param load ILoadPraticheCollegate contenente i parametri di configurazione.
   * @returns boolean che definisce se l'utente può accedere alla route.
   */
  private checkPCLoader(load: ILoadPraticheCollegate): boolean {
    // Verifico esista lo state
    if (!load) {
      // Ritorno false
      return false;
    }

    // Ritorno il check sui dati dello state
    return this.checkPCData(load);
  }

  /**
   * Funzione che controlla i dati passati alle pratiche collegate.
   * @param load ILoadPraticheCollegate contenente i parametri di configurazione.
   * @returns boolean che definisce se l'utente può accedere alla route (true) o bisogna bloccare la navigazione (false).
   */
  private checkPCData(load: ILoadPraticheCollegate): boolean {
    // Recupero i dati per il check
    const idSoggetto = load.idSoggetto;
    const idGruppo = load.idGruppo;

    // Verifico se non esiste nessun id valido
    if (!idSoggetto && !idGruppo) {
      // Blocco del routing
      return false;
    }

    // Controlli validi, accedo alla route
    return true;
  }
}
