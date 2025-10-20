import { Pipe, PipeTransform } from '@angular/core';
import { AccessoElementiAppService } from '../../../core/services/accesso-elementi-app.service';

/**
 * Pipe dedicata gestione della disabilitazione dei vari elementi dell'applicazione, basandosi sulla configurazione: profilazione-utilizzo.
 */
@Pipe({ name: 'accessoElementoApp' })
export class RiscaAccessoElementoAppPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _accessoEA: AccessoElementiAppService) {}

  /**
   * Pipe che definisce le logiche per il ritorno dello stato di accesso di un elemento dell'app, dato un codice e/o altri flag.
   * @param code string che definisce il codice per il recupero della configurazione.
   * @param othersEnabled Operatore spread che definisce una lista di extra condizioni per la gestione dell'abilitazione dell'elemento.
   * @returns boolean che definisce se l'elemento è abilitato (true) o disabilitato (false).
   */
  transform(code: string, ...othersEnabled: boolean[]): boolean {
    // Recupero lo stato tramite configurazione
    const accessoEA = this.getAccessoElementoApp(code);
    // Verifico la configurazione
    if (!accessoEA) {
      // Per configurazione l'elemento è disabilitato
      return false;
    }

    // Verifico e definisco lo stato di abilitazione dalla lista degli altri flag
    const othersStatus = this.checkOthersEnabled(othersEnabled);
    // Ritorno lo stato ritornato dal controllo
    return othersStatus;
  }

  /**
   * Funzione che recupera la configurazione per l'accesso app.
   * @param code string che definisce il codice per il recupero della configurazione.
   * @returns boolean che definisce se la configurazione è abilitata (true) o disabilitata (false).
   */
  private getAccessoElementoApp(code: string): boolean {
    // Definisco il flag di accesso dell'elemento tramite configurazione
    let accessoEA: boolean = true;
    // Verifico l'input
    if (code) {
      // Se esiste il codice, recupero la configurazione
      accessoEA = this._accessoEA.checkAccessoElementoApp(code);
    }

    // Ritorno lo stato della configurazione
    return accessoEA;
  }

  /**
   * Funzione che definisce lo stato di enabled da una lista di stati.
   * Se anche un solo elemento è false, il ritorno sarà false.
   * @param othersEnabled Array di boolean che definisce altri stati d'abilitazione.
   * @returns boolean che definisce lo stato di abilitazione.
   */
  private checkOthersEnabled(othersEnabled: boolean[]): boolean {
    // Verifico l'input
    if (!othersEnabled || othersEnabled.length === 0) {
      // Non ci sono altri stati, ritorno true
      return true;
    }

    // Cerco all'interno dell'array se esiste un valore false o undefined
    const falseInArray =
      othersEnabled.findIndex((e) => {
        // Verifico se l'elemento è false o undefined
        return e == undefined || e === false;
      }) !== -1;

    // Ritorno lo stato finale
    return !falseInArray;
  }
}
