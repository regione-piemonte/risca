import { Injectable } from '@angular/core';
import { backEnd } from 'src/environments/back-end';
import { environment } from 'src/environments/environment';

/**
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto. 
 */
@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  getMockUrl(): string {
    return `${environment.mockServer}/api`;
  }

  getBEUrl(): string {
    return `${environment.beServerPrefix}${backEnd.path}`;
  }

  getApiAccessToken(): string {
    return environment.apiAccessToken;
  }

  isDebugMode(): boolean {
    return !environment.production;
  }

  /**
   * Funzione che genera il path url per le chiamate ai servizi di backend.
   * @param paths Spread syntax che permette di passare un numero variabile di path che comporranno l'url.
   * @returns string con la url compilata per la chiamata ai servizi di BE.
   */
  appUrl(...paths: any[]): string {
    // Verifico l'input
    paths = paths ?? [];

    // Inizializzo l'url per la chiamata
    let url = this.getBEUrl();
    // Rimuovo possibili informazioni undefined
    paths = paths.filter((p: any) => p != undefined);
    // Effettuo una conversione e pulizia dei path da possibili "/" ad inizio stringa
    paths = paths.map((p: any) => {
      // Converto le informazioni in stringa
      p = p.toString();
      // Creo la regex che definisce se il primo carattere di una stringa è "/"
      const firstCharSlash = /^\//;
      // Tramite regex ritorno la stringa senza il primo carattere se "/"
      return p.replace(firstCharSlash, '');
    });

    // Compongo l'url con i vari path
    paths.forEach((path: string) => {
      // Costruisco la stringa
      url = `${url}/${path}`;
    });

    // Ritorno l'url generato
    return url;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  get envConfigs() {
    return environment;
  }

  get localLogoutRedirect(): string {
    return environment.localLogout;
  }

  /**
   * Getter per il prefisso per la gestione del backend.
   * @returns string con la configurazione.
   */
  get beServerPrefix(): string {
    // Ritorno la configurazione
    return environment.beServerPrefix;
  }

  /**
   * Getter che ritorna l'url completo per l'applicazione.
   * @returns string con la configurazione.
   */
  get applicationUrl(): string {
    // Ritorno l'url completo
    return `${environment.beServerPrefix}/${environment.application}/`;
  }

  /**
   * Getter per l'intervallo temporale da usare tra un poll e l'altro per gli stati dei report.
   * @returns number con la configurazione.
   */
  get jobPollingInterval(): number {
    // Ritorno la configurazione dell'ambiente o un default
    return environment.jobPollingInterval ?? 10000;
  }
}
