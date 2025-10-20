import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { clone } from 'lodash';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import {
  AccessoElementiAppVo,
  ProfiloOggettoAppVo,
} from '../commons/vo/accesso-elementi-app-vo';
import { ConfigService } from './config.service';

@Injectable({ providedIn: 'root' })
export class AccessoElementiAppService {
  /** Costante per il path per: /profilazione. */
  private PATH_PROFILAZIONE_UTILIZZO = '/profilazione';

  /** AccessoElementiApp con i dati scaricati dal servizio. */
  private _accessoEA: AccessoElementiAppVo;

  /**
   * Costruttore.
   */
  constructor(private _config: ConfigService, private _http: HttpClient) {}

  /**
   * ################################################
   * FUNZIONI PER LA GESTIONE DEGLI ELEMENTI DELL'APP
   * ################################################
   */

  /**
   * Funzione che recupera a seconda del ruolo dell'user, l'accesso agli elementi dell'applicazione.
   * L'accesso agli elementi dell'app è una oggetto che definisce quali componenti, pulsanti, pagine, etc... da abilitare/disabilitare all'interno dell'applicazione.
   * @returns Observable<AccessoElementiAppVo> con i dati restituiti dal servizio.
   */
  getAccessoElementiApp(): Observable<AccessoElementiAppVo> {
    // Costruisco l'url per la chiamata
    const url = this._config.appUrl(this.PATH_PROFILAZIONE_UTILIZZO);

    // Effettuo la chiamata al servizio
    return this._http.get<AccessoElementiAppVo>(url).pipe(
      tap((accessoEA: AccessoElementiAppVo) => {
        // Assegno localmente i dati
        this._accessoEA = accessoEA;
      })
    );
  }

  /**
   * Funzione che cerca, all'interno della configurazione degli elementi, un oggetto ProfiloOggettoAppVo.
   * @param codOggettoApp string che definisce il codice oggetto per la ricerca.
   * @returns ProfiloOggettoAppVo trovato o undefined se non esiste.
   */
  getAccessoElementoApp(codOggettoApp: string): ProfiloOggettoAppVo {
    // Controllo di esistenza
    if (!codOggettoApp) {
      // Nulla da cercare
      return undefined;
    }

    // Recupero la configurazione
    const accessoEA = this.accessoElementiApp;
    // Recupero la lista di oggetti
    const oggettiApp = accessoEA?.profilo_oggetto_app;

    // Cerco l'oggetto e lo restituisco
    return oggettiApp?.find((poa: ProfiloOggettoAppVo) => {
      // Estraggo l'oggetto app
      const { oggetto_app } = poa || {};
      // Cerco per codice oggetto app
      return oggetto_app?.cod_oggetto_app == codOggettoApp;
    });
  }

  /**
   * Funzione che cerca, all'interno della configurazione degli elementi, un oggetto ProfiloOggettoApp e ne ritorna il suo stato di utilizzo.
   * @param codOggettoApp string che definisce il codice oggetto per la ricerca.
   * @returns boolean che definisce se l'elemento è attivo (true) o disattivo (false).
   */
  checkAccessoElementoApp(codOggettoApp: string): boolean {
    // Controllo di esistenza
    if (!codOggettoApp) {
      // Nulla da cercare
      return false;
    }

    // Ottengo l'oggetto cercato
    const profiloOggettoApp = this.getAccessoElementoApp(codOggettoApp);
    // Verifico che l'oggetto esista e ritorno il valore del campo flag_attivo
    return profiloOggettoApp && profiloOggettoApp.flg_attivo;
  }

  /**
   * Funzione che cerca, all'interno della configurazione degli elementi, un oggetto ProfiloOggettoApp e ne ritorna il suo stato di utilizzo come "disabled" per l'app.
   * @param codOggettoApp string che definisce il codice oggetto per la ricerca.
   * @returns boolean che definisce se l'elemento è disabled (true) o enabled (false).
   */
  isAEADisabled(codOggettoApp: string): boolean {
    // Richiamo la funzione di check, invertendo il flag
    return !this.checkAccessoElementoApp(codOggettoApp);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per accessoEA.
   * @returns AccessoElementiApp con il valore di accessoEA.
   */
  get accessoElementiApp(): AccessoElementiAppVo {
    // Ritorno una copia dei dati di configurazione
    return clone(this._accessoEA);
  }
}
