import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { map } from 'rxjs/operators';
import { RicercaPaginataResponse } from 'src/app/core/classes/http-helper/http-helper.classes';
import { HttpHelperService } from 'src/app/core/services/http-helper/http-helper.service';
import {
  ComponenteGruppo,
  ComponenteGruppoVo,
} from '../../../../core/commons/vo/componente-gruppo-vo';
import { Gruppo, GruppoVo } from '../../../../core/commons/vo/gruppo-vo';
import { ConfigService } from '../../../../core/services/config.service';
import { LoggerService } from '../../../../core/services/logger.service';
import { UserService } from '../../../../core/services/user.service';
import { RiscaCompareService } from '../../../../shared/services/risca/risca-compare.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  ParseValueRules,
  RiscaTablePagination,
} from '../../../../shared/utilities';
import {
  AbilitaDAGruppi,
  AbilitaDASezioni,
  FlagRicercaCL,
  ServerNumberAsBoolean,
} from '../../../../shared/utilities/enums/utilities.enums';
import { AmbitoService } from '../../../ambito/services';
import { AbilitazioniDatiAnagrafici } from '../../../../core/classes/abilitazioni-ambito/abilitazioni-dati-anagrafici.class';

@Injectable({
  providedIn: 'root',
})
export class GruppoSoggettoService {
  /** Costante per il path: /gruppi */
  private PATH_GRUPPI = '/gruppi';
  /** Costante per il path: /gruppi */
  private PATH_GRUPPI_QRY = '/gruppi-qry';

  /** String contenente il path url del be. */
  private beUrl: string;

  /** AbilitazioniDatiAnagrafici per la gestione delle abilitazioni del soggetto. */
  private _abilitazioniDA: AbilitazioniDatiAnagrafici;

  /**
   * Costruttore
   */
  constructor(
    private _ambito: AmbitoService,
    private _config: ConfigService,
    private _http: HttpClient,
    private _httpHelper: HttpHelperService,
    private _logger: LoggerService,
    private _riscaCompare: RiscaCompareService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Setup del be url
    this.beUrl = this._config.getBEUrl();
  }

  /**
   * #################
   * CHIAMATE ALLE API
   * #################
   */

  /**
   * Funzione per il recupero di un gruppo dato un id o un codice gruppo.
   * @param idOrCodGruppo number | string che definisce l'id gruppo per la ricerca.
   * @returns Observable<Gruppo> con il gruppo trovato.
   */
  getGruppo(idOrCodGruppo: number): Observable<Gruppo> {
    // Definisco l'url
    const url = `${this.beUrl}${this.PATH_GRUPPI}/${idOrCodGruppo}`;

    // Effettuo la chiamata al servizio
    return this._http.get<GruppoVo>(url).pipe(
      map((gruppoVo: GruppoVo) => {
        // Converto il res
        return this.convertGruppoVoToGruppo(gruppoVo);
      })
    );
  }

  /**
   * Funzione che effettua la put (modifica) dei dati del gruppo.
   * @param gruppo Gruppo contenente i dati d'aggiornare.
   * @returns Observable<GruppoVo> con la risposta del servizio.
   */
  updateGruppo(gruppo: Gruppo): Observable<Gruppo> {
    // Verifico l'input
    if (!gruppo) {
      // Variabili di comodo
      const t = 'updateGruppo';
      const d = { gruppo };
      // Creo un d'errore come quello del server
      const e = this._riscaUtilities.customResponseError(t, null, d);
      // Ritorno un errore
      return throwError(e);
    }

    // Effettuo una sanitizzazione per i dati
    this._riscaUtilities.sanitizeFEProperties(gruppo);
    // Effettuo la conversione dell'oggetto
    let gruppoVo = this.convertGruppoToGruppoVo(gruppo);

    // Definisco l'url
    const url = `${this.beUrl}${this.PATH_GRUPPI}`;

    // Lancio la chiamata per il salvataggio dati
    return this._http.put<GruppoVo>(url, gruppoVo).pipe(
      map((gVo: GruppoVo) => {
        // Mappo l'oggetto
        return this.convertGruppoVoToGruppo(gVo);
      })
    );
  }

  /**
   * Funzione che effettua la post (inserimento) dei dati del gruppo.
   * @param gruppo Gruppo contenente i dati d'aggiornare.
   * @returns Observable<Gruppo> con la risposta del servizio.
   */
  insertGruppo(gruppo: Gruppo): Observable<Gruppo> {
    // Verifico l'input
    if (!gruppo) {
      // Variabili di comodo
      const t = 'insertGruppo';
      const d = { gruppo };
      // Creo un d'errore come quello del server
      const e = this._riscaUtilities.customResponseError(t, null, d);
      // Ritorno un errore
      return throwError(e);
    }

    // Effettuo una sanitizzazione per i dati
    this._riscaUtilities.sanitizeFEProperties(gruppo);
    // Effettuo la conversione dell'oggetto
    let gruppoVo = this.convertGruppoToGruppoVo(gruppo);

    // Definisco l'url
    const url = `${this.beUrl}${this.PATH_GRUPPI}`;

    // Lancio la chiamata per il salvataggio dati
    return this._http.post<GruppoVo>(url, gruppoVo).pipe(
      map((gVo: GruppoVo) => {
        // Mappo l'oggetto
        return this.convertGruppoVoToGruppo(gVo);
      })
    );
  }

  /**
   * Funzione per la cancellazione di un gruppo dato un id o un codice gruppo.
   * @param idOrCodGruppo number | string che definisce l'id gruppo per la cancellazione.
   * @returns Observable<boolean> che definisce se la cancellazione è avvenuta.
   */
  deleteGruppo(idOrCodGruppo: number): Observable<boolean> {
    // Verifico la descrizione
    if (!idOrCodGruppo) {
      // Variabili di comodo
      const t = 'deleteGruppo';
      const d = { idOrCodGruppo };
      // Creo un d'errore come quello del server
      const e = this._riscaUtilities.customResponseError(t, null, d);
      // Ritorno un errore
      return throwError(e);
    }

    // Definisco l'url
    const url = `${this.beUrl}${this.PATH_GRUPPI}/${idOrCodGruppo}`;

    // Effettuo la chiamata al servizio
    return this._http.delete<number>(url).pipe(
      map((deleted: number) => {
        // Converto il res
        return deleted === ServerNumberAsBoolean.true;
      })
    );
  }

  /**
   * Fuzione che effettua la ricerca dei soggetti sul db.
   * Se vengono trovati dei soggetti, verranno ritornati in un array, altrimenti undefined.
   * @param idAmbito number id ambito dell'utente.
   * @param campoRicerca string che definisce la ricerca da applicare a tutti i filtri.
   * @param target FlagRicercaCL che definisce il target di ricerca. G o T.
   * @returns RicercaPaginataResponse con i soggetti o undefined ed i dati della paginazione.
   */
  cercaGruppiCampoLibero(
    idAmbito: number,
    campoRicerca: string,
    target: FlagRicercaCL,
    paginazione: RiscaTablePagination = undefined
  ): Observable<RicercaPaginataResponse<Gruppo[]>> {
    // Definisco l'url
    const url = `${this.beUrl}${this.PATH_GRUPPI_QRY}`;

    // Ottengo i parametri della paginazione
    const pagingParams = this._riscaUtilities.createPagingParamsItem(paginazione);
    const queryParams = { idAmbito, campoRicerca, flgTipoRicerca: target };

    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      ...queryParams,
      ...pagingParams,
    });

    // Lancio la chiamata per il salvataggio dati
    return this._http
      .get<GruppoVo[]>(url, {
        params,
        observe: 'response', // Permette di avere la response intera e leggere gli header
      })
      .pipe(
        map((response: HttpResponse<GruppoVo[]>) => {
          // Ottengo l'oggetto con il risultato e la paginazione passando la funzione di conversione dei gruppi
          const ricercaPaginata =
            this._httpHelper.ricercaPaginataResponse<GruppoVo[]>(response);
          // Lo ritorno
          return ricercaPaginata;
          // #
        }),
        map((ricercaPaginata: RicercaPaginataResponse<GruppoVo[]>) => {
          // Converto gli oggetti della response in quelli attesi: GruppoVo[] => Gruppo[]
          const sources = this.convertGruppiVoToGruppi(ricercaPaginata.sources);
          const paging = ricercaPaginata.paging;
          // Ritorno l'oggetto finale
          return new RicercaPaginataResponse<Gruppo[]>({
            sources,
            paging,
          });
        })
      );
  }

  /**
   * ###################################
   * FUNZIONI DI UTILITY PER IL SERVIZIO
   * ###################################
   */

  /**
   * ##################################
   * CONVERTITORI DA GruppoVo A Gruppo
   * ##################################
   */

  /**
   * Funzione che converte un array di GruppoVo in un array di Gruppo.
   * @param gruppiVo Array di GruppoVo da convertire.
   * @returns Array di Gruppo convertito.
   */
  convertGruppiVoToGruppi(gruppiVo: GruppoVo[]): Gruppo[] {
    // Verifico l'input
    if (!gruppiVo) {
      return [];
    }

    // Ritorno una map dell'array utilizzando la funzione di conversione del singolo oggetto
    return gruppiVo.map((gruppoVo) =>
      // Richiamo la funzione di conversione dell'oggetto
      this.convertGruppoVoToGruppo(gruppoVo)
    );
  }

  /**
   * Funzione che converte un oggetto GruppoVo in un oggetto Gruppo.
   * @param gruppoVo GruppoVo da convertire.
   * @returns Gruppo convertito.
   */
  convertGruppoVoToGruppo(gruppoVo: GruppoVo): Gruppo {
    // Verifico l'input
    if (!gruppoVo) {
      return;
    }

    // Creo una copia any dell'oggetto
    const gruppo: Gruppo = { ...gruppoVo } as any;
    // Modifico le proprietà
    gruppo.componenti_gruppo =
      this.convertComponeponentiGruppoVoToComponentiGruppo(
        gruppoVo.componenti_gruppo
      );
    // Ritorno l'oggetto
    return gruppo;
  }

  /**
   * Funzione che converte un array di ComponenteGruppoVo in un array di ComponenteGruppo.
   * @param ciVo Array di ComponenteGruppoVo da convertire.
   * @returns Array di ComponenteGruppo convertito.
   */
  convertComponeponentiGruppoVoToComponentiGruppo(
    ciVo: ComponenteGruppoVo[]
  ): ComponenteGruppo[] {
    // Verifico l'input
    if (!ciVo) {
      return [];
    }

    // Ritorno una map dell'array utilizzando la funzione di conversione del singolo oggetto
    return ciVo.map((cVo) =>
      // Richiamo la funzione di conversione dell'oggetto
      this.convertComponenteGruppoVoToComponenteGruppo(cVo)
    );
  }

  /**
   * Funzione che converte un oggetto ComponenteGruppoVo in un oggetto ComponenteGruppo.
   * @param cVo ComponenteGruppoVo da convertire.
   * @returns ComponenteGruppo convertito.
   */
  convertComponenteGruppoVoToComponenteGruppo(
    cVo: ComponenteGruppoVo
  ): ComponenteGruppo {
    // Verifico l'input
    if (!cVo) {
      return;
    }

    // Creo una copia any dell'oggetto
    const c: ComponenteGruppo = { ...cVo } as any;

    // Verifico che la proprietà non sia undefined
    if (cVo.flg_capo_gruppo !== undefined) {
      // Modifico le proprietà
      c.flg_capo_gruppo = cVo.flg_capo_gruppo ? true : false;
    }

    // Ritorno l'oggetto convertito
    return c;
  }

  /**
   * ##################################
   * CONVERTITORI DA Gruppo A GruppoVo
   * ##################################
   */

  /**
   * Funzione che converte un array di Gruppo in un array di GruppoVo.
   * @param gruppiVo Array di Gruppo da convertire.
   * @returns Array di GruppoVo convertito.
   */
  convertGruppiToGruppiVo(gruppi: Gruppo[]): GruppoVo[] {
    // Verifico l'input
    if (!gruppi) {
      return [];
    }

    // Ritorno una map dell'array utilizzando la funzione di conversione del singolo oggetto
    return gruppi.map((gruppo) =>
      // Richiamo la funzione di conversione dell'oggetto
      this.convertGruppoToGruppoVo(gruppo)
    );
  }

  /**
   * Funzione che converte un oggetto Gruppo in un oggetto GruppoVo.
   * @param gruppo Gruppo da convertire.
   * @returns GruppoVo convertito.
   */
  convertGruppoToGruppoVo(gruppo: Gruppo): GruppoVo {
    // Verifico l'input
    if (!gruppo) {
      return;
    }

    // Creo una copia any dell'oggetto
    const gruppoVo: GruppoVo = { ...gruppo } as any;
    // Modifico le proprietà
    gruppoVo.componenti_gruppo =
      this.convertComponeponentiGruppoToComponentiGruppoVo(
        gruppo.componenti_gruppo
      );
    // Ritorno l'oggetto
    return gruppoVo;
  }

  /**
   * Funzione che converte un array di ComponenteGruppo in un array di ComponenteGruppoVo.
   * @param ci Array di ComponenteGruppo da convertire.
   * @returns Array di ComponenteGruppoVo convertito.
   */
  convertComponeponentiGruppoToComponentiGruppoVo(
    ci: ComponenteGruppo[]
  ): ComponenteGruppoVo[] {
    // Verifico l'input
    if (!ci) {
      return [];
    }

    // Ritorno una map dell'array utilizzando la funzione di conversione del singolo oggetto
    return ci.map((c) =>
      // Richiamo la funzione di conversione dell'oggetto
      this.convertComponeponenteGruppoToComponenteGruppoVo(c)
    );
  }

  /**
   * Funzione che converte un oggetto ComponenteGruppo in un oggetto ComponenteGruppoVo.
   * @param c ComponenteGruppo da convertire.
   * @returns ComponenteGruppoVo convertito.
   */
  convertComponeponenteGruppoToComponenteGruppoVo(
    c: ComponenteGruppo
  ): ComponenteGruppoVo {
    // Verifico l'input
    if (!c) {
      return;
    }

    // Creo una copia any dell'oggetto
    const cVo: ComponenteGruppoVo = { ...c } as any;
    // Verifico che la proprietà non sia undefined
    if (c.flg_capo_gruppo !== undefined) {
      // Modifico le proprietà
      cVo.flg_capo_gruppo = c.flg_capo_gruppo ? 1 : 0;
    }
    // Ritorno l'oggetto convertito
    return cVo;
  }
  /**
   * ################################
   * FUNZIONE DI COMPARE TRA SOGGETTI
   * ################################
   */

  /**
   * Funzione che verifica se un gruppo ha le stesse informazioni di un altro gruppo.
   * @param g1 Gruppo da verificare.
   * @param g2 Gruppo da verificare.
   * @param parseRules ParseValueRules che definisce le logiche di sanitizzazione degli oggetti.
   * @returns boolean che definisce se i soggetti in input hanno le stesse informazioni.
   */
  compareGruppi(g1: Gruppo, g2: Gruppo, parseRules?: ParseValueRules): boolean {
    // Richiamo la funzione del servizio
    return this._riscaCompare.sameGruppi(g1, g2, parseRules);
  }

  /**
   * Funzione che verifica se nei gruppi in input i capi gruppo sono gli stessi.
   * @param g1 Gruppo per la verifica.
   * @param g2 Gruppo per la verifica.
   * @returns boolan che definisce se i capogruppi sono gli stessi.
   */
  sameCapigruppoByGruppi(g1: Gruppo, g2: Gruppo): boolean {
    // Verifico l'input
    if (!g1 || !g2) {
      // Mancano i dati
      return false;
    }

    // Recupero i componenti del gruppo
    const comp1 = g1.componenti_gruppo;
    const comp2 = g2.componenti_gruppo;

    // Ritorno la verifica
    return this._riscaCompare.sameCapigruppo(comp1, comp2);
  }

  /**
   * ##############################################
   * GETTER DI COMODO PER LE VARIABILI DEL SERVIZIO
   * ##############################################
   */

  /**
   * Getter per abilitazioniDA.
   * @returns AbilitazioniDatiAnagrafici con l'ultimo valore.
   */
  get abilitazioniDA(): AbilitazioniDatiAnagrafici {
    return this._abilitazioniDA;
  }

  /**
   * Getter che recupera l'abilitazione 'isAbilitato' per i gruppi.
   */
  get isAbilitato(): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    return this._ambito.getAbilitazioneSoggettiGruppi(
      AbilitaDASezioni.gruppi,
      AbilitaDAGruppi.isAbilitato
    );
  }

  /**
   * Getter per il codice fiscale dell'user collegato.
   * @returns string con il codice fiscale dell'utente collegato.
   */
  get cf() {
    // Recupero il codice fiscale dell'utente
    return this._user.user.codFisc;
  }
}
