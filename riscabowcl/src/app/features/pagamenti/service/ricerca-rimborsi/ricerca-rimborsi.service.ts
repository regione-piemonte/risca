import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { AttivitaSDVo } from '../../../../core/commons/vo/attivita-sd-vo';
import {
  IStatoDebitorioVo,
  StatoDebitorioVo,
} from '../../../../core/commons/vo/stato-debitorio-vo';
import { ConfigService } from '../../../../core/services/config.service';
import { HttpHelperService } from '../../../../core/services/http-helper/http-helper.service';
import { IFiltriRicercaRimborsi } from '../../../../shared/components/risca/risca-ricerca-rimborsi/utilities/risca-ricerca-rimborsi.interfaces';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaTablePagination } from '../../../../shared/utilities';
import { IRicercaRimborsi } from './utilities/ricerca-rimborsi.interfaces';

@Injectable({ providedIn: 'root' })
export class RicercaRimborsiService extends HttpUtilitiesService {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** Costante per il path: /ricerca_rimborsi */
  private PATH_SEARCH_SD_RIMBORSI = '/ricerca_rimborsi';
  /** Costante per il path: /stati_debitori/attivita-rimborsi-list */
  private PATH_UPD_ALL_SD_RIMBORSI = '/stati_debitori/attivita-rimborsi-list';
  /** Costante per il path: /stati_debitori/attivita */
  private PATH_UPD_MANUAL_SD_RIMBORSI = '/stati_debitori/attivita';

  /**
   * Costruttore.
   */
  constructor(
    config: ConfigService,
    private _http: HttpClient,
    private _httpHelper: HttpHelperService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Imposto il super
    super(config, riscaUtilities);
  }

  /**
   * ###############################
   * FUNZIONI CHE RICHIAMO I SERVIZI
   * ###############################
   */

  /**
   * Funzione che richiama la ricerca stati debitori con filtri per i rimborsi.
   * @param searchFilters IRicercaRimborsi con i query param per la ricerca.
   * @returns Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> con i risultati di ricerca.
   */
  searchSDByFiltriRimborsi(
    searchFilters: IRicercaRimborsi,
    paginazione: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_SEARCH_SD_RIMBORSI);
    // Definisco i parametri per la ricerca
    let searchParams: any = searchFilters;

    // Definisco i query params per la chiamata
    const params = this.createPagingParams(paginazione, searchParams);
    // Definisco le options
    const options = { params };

    // Lancio la chiamata per il recupero dati
    return this._httpHelper.searchWithGet(url, options).pipe(
      map((res: RicercaPaginataResponse<IStatoDebitorioVo[]>) => {
        // Ritorno la response convertita
        return this.paginateStatiDebitori(res);
      })
    );
  }

  /**
   * Funzione che aggiorna tutti gli stati debitori ricercati tramite filtri rimborsi, anche quelli non visibili a schermo.
   * @param ricercaSD IRicercaRimborsi con l'oggetto per la ricerca degli stati debitori.
   * @param attivitaStatoDeb AttivitaSDVo con l'oggetto per l'aggiornamento dell'attività degli stati debitori.
   * @returns Observable<StatoDebitorioVo[]> con i dati aggiornati.
   */
  updateAllSDRimborsi(
    ricercaSD: IRicercaRimborsi,
    attivitaStatoDeb: AttivitaSDVo
  ): Observable<StatoDebitorioVo[]> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_UPD_ALL_SD_RIMBORSI);
    // Definisco i query param per l'aggiornamento dati
    const params = this.createQueryParams(ricercaSD);
    // Definisco le options
    const options = { params };

    // Richiamo l'aggiornamento dei dati
    return this._http
      .put<IStatoDebitorioVo[]>(url, attivitaStatoDeb, options)
      .pipe(
        map((sdUpdate: IStatoDebitorioVo[]) => {
          // Ritorno i dati convertiti
          return this.convertiStatiDebitori(sdUpdate);
        })
      );
  }

  /**
   * Funzione che aggiorna la lista di stati debitori filtrati per rimborsi.
   * @param statiDebitori StatoDebitorioVo[] con la lista di oggetti d'aggiornare.
   * @returns Observable<StatoDebitorioVo[]> con i dati aggiornati.
   */
  updateSDRimborsi(
    statiDebitori: StatoDebitorioVo[]
  ): Observable<StatoDebitorioVo[]> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_UPD_MANUAL_SD_RIMBORSI);
    // Converto le informazioni per ottenere oggetti BE like
    const sdBE =
      statiDebitori?.map((sd: StatoDebitorioVo) => {
        // Converto e ritorno un oggetto BE like
        return sd.toServerFormat();
      }) ?? [];

    // Richiamo l'aggiornamento dei dati
    return this._http.put<IStatoDebitorioVo[]>(url, sdBE).pipe(
      map((sdUpdate: IStatoDebitorioVo[]) => {
        // Ritorno i dati convertiti
        return this.convertiStatiDebitori(sdUpdate);
      })
    );
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che lancia le logiche di conversione dati e gestione paginazione per gli stati debitori.
   * @param res RicercaPaginataResponse<IStatoDebitorioVo[]> con la risposta ottenuta dal server.
   * @returns RicercaPaginataResponse<StatoDebitorioVo[]> come risposta convertita.
   */
  private paginateStatiDebitori(
    res: RicercaPaginataResponse<IStatoDebitorioVo[]>
  ): RicercaPaginataResponse<StatoDebitorioVo[]> {
    // Recupero la lista di oggetti ritornati dal server
    const { sources } = res;
    // Effettuo un parse dei dati in una lista FE friendly
    const statiDebitori: StatoDebitorioVo[] = sources?.map(
      (iSD: IStatoDebitorioVo) => {
        // Creo l'oggetto di FE
        return new StatoDebitorioVo(iSD);
      }
    );
    // Creo una nuova response da ritornare
    const newSD: RicercaPaginataResponse<StatoDebitorioVo[]> = {
      paging: res.paging,
      sources: statiDebitori,
    };

    // Ritorno la response modificata
    return newSD;
  }

  /**
   * Funzione di comodo per la conversione di un array di IStatoDebitorioVo.
   * @param statiDebitori IStatoDebitorioVo[] da convertire.
   * @returns StatoDebitorioVo[] convertito.
   */
  private convertiStatiDebitori(
    statiDebitori: IStatoDebitorioVo[]
  ): StatoDebitorioVo[] {
    // Converto la lista in input
    return (
      statiDebitori?.map((sd: IStatoDebitorioVo) => {
        // Creo e ritorno l'oggetto convertito
        return new StatoDebitorioVo(sd);
      }) ?? []
    );
  }

  /**
   * #######################
   * FUNZIONI DI CONVERSIONI
   * #######################
   */

  /**
   * Funzione di conversione che converte le informazioni generate dal form di ricerca RiscaRicercaRimborsi in un oggetto compatibile con i parametri di ricerca per i query param del servizio.
   * @param f IFiltriRicercaRimborsi da convertire.
   * @returns RicercaRimborsi convertito.
   */
  convertIFiltriRicercaRimborsiToIRicercaRimborsi(
    f: IFiltriRicercaRimborsi
  ): IRicercaRimborsi {
    // Verifico l'input
    if (!f) {
      // Nessuna configurazione
      return;
    }

    // Converto le informazioni per l'oggetto
    const tipoRicercaRimborsi: string =
      f.tipoRicercaRimborsi?.cod_tipo_ricerca_rimborso ?? null;
    const anno: number = f.anno?.anno ?? null;
    // Creo un oggetto per i query param
    const queryFiltrers: IRicercaRimborsi = {
      tipoRicercaRimborsi,
      anno,
    };

    // Ritorno l'oggetto con i dati per la query
    return queryFiltrers;
  }
}
