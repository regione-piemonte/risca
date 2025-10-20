import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserService } from 'src/app/core/services/user.service';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { ConfigService } from '../../../../core/services/config.service';
import { HttpHelperService } from '../../../../core/services/http-helper/http-helper.service';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaFruitori, RiscaTablePagination } from '../../../../shared/utilities';
import { IInteressiDiMoraParams } from './utilities/interessi-di-mora.interfaces';
import { IInteressiDiMoraVo, InteressiDiMoraVo, toListInteressiDiMoraVo } from 'src/app/core/commons/vo/interessi-di-mora-vo';
import { InteressiDiMoraConst } from './utilities/interessi-di-mora.const';

@Injectable({ providedIn: 'root' })
export class InteressiDiMoraService extends HttpUtilitiesService {
  /** Costante per il path: /tassi-interesse */
  readonly PATH_TASSI_INTERESSE = '/tassi-interesse';
  // Dichiaro la costante per gli interessi di mora
  IM_C = new InteressiDiMoraConst();

  /**
   * Costruttore.
   */
  constructor(
    config: ConfigService,
    private _http: HttpClient,
    private _httpHelper: HttpHelperService,
    riscaUtilities: RiscaUtilitiesService,
    private userService: UserService
  ) {
    // Richiamo il super con le configurazioni necessarie
    super(config, riscaUtilities);
  }

  /**
   * Get che recupera la lista degli interessi di mora da visualizzare.
   * @returns Observable<InteressiLegaliVo[]> con i dati scaricati.
   */
  getInteressiDiMora(): Observable<InteressiDiMoraVo[]> {
    // Dichiaro l'url per la chiamata al servizio
    const url: string = this.appUrl(this.PATH_TASSI_INTERESSE);

    // Richiamo il servizio tramite API
    return this._http.get<IInteressiDiMoraVo[]>(url).pipe(
      // Convertire i dati da BE a FE
      map((interessiDiMoraVo: IInteressiDiMoraVo[]) => {
        // Converto la lista in input su oggetto FE
        return toListInteressiDiMoraVo(interessiDiMoraVo);
        // #
      })
    );
  }

  /**
   * Funzione di GET che recupera le informazioni relative alle lista degli interessi di mora.
   * La funzione ritornerà i risultati paginati.
   * @param paginazione RiscaTablePagination con l'oggetto di ricerca per la paginazione.
   * @returns Observable<RicercaPaginataResponse<InteressiLegaliVo[]>> con le informazioni scaricate.
   */
  getInteressiDiMoraPaginated(paginazione: RiscaTablePagination): Observable<RicercaPaginataResponse<InteressiDiMoraVo[]>> {
    // Definisco l'url
    const url: string = this.appUrl(this.PATH_TASSI_INTERESSE);
    // Definisco i query params specifici per la
    const queryParams: IInteressiDiMoraParams = {
      idAmbito: this.userService.idAmbito,
      fruitore: this.userService.cf ?? RiscaFruitori.RISCA,
      tipoDiInteresse: this.IM_C.INTERESSE_DI_MORA
    };
    // Definisco i query params per la chiamata
    let params: HttpParams;
    params = this.createPagingParams(paginazione, queryParams);
    // Definisco le options
    const options = { params };

    // Effettuo la chiamata al servizio
    return this._httpHelper
      .searchWithGet<IInteressiDiMoraVo[]>(url, options)
      .pipe(
        map((res: RicercaPaginataResponse<IInteressiDiMoraVo[]>) => {
          // Ritorno la response convertita
          return this.paginateInteressiDiMora(res);
        })
      );
  }

  /**
   * Funzione di POST che tenta il salvataggio degli interessi di mora aggiunti dall'utente.
   * @param interessiDiMora InteressiLegaliVo con i dati da salvare.
   * @return Observable<InteressiLegaliVo> con la risposta ottenuta dal server.
   */
  postInteressiDiMora(interessiDiMora: InteressiDiMoraVo): Observable<InteressiDiMoraVo> {
    // Definisco l'url
    const url: string = this.appUrl(this.PATH_TASSI_INTERESSE);
    // Definisco i query params specifici per la
    const queryParams: IInteressiDiMoraParams = {
      //fruitore: RiscaFruitori.RISCA,
    };
    // Definisco i query params per la chiamata
    let params: HttpParams;
    params = this.createQueryParams(queryParams);
    // Definisco le options
    const options = { params };
    // Converto l'oggetto FE in oggetto BE like
    const body: IInteressiDiMoraVo = interessiDiMora?.toServerFormat();

    // Richiamo il servizio e ritorno l'observable come risposta
    return this._http.post<IInteressiDiMoraVo>(url, body, options).pipe(
      map((interessiDiMoraRes: IInteressiDiMoraVo) => {
        // Converto la risposta con un oggetto FE
        return new InteressiDiMoraVo(interessiDiMoraRes);
        // #
      })
    );
  }

  /**
   * Funzione di DELETE che tenta la cancellazione degli interessi di mora in input.
   * @param interessiDiMora InteressiLegaliVo con i dati da salvare.
   * @return Observable<InteressiLegaliVo> con la risposta ottenuta dal server.
   */
  deleteInteressiDiMora(interessiDiMora: InteressiDiMoraVo): Observable<InteressiDiMoraVo> {
    // Tento di recuperare l'id dell'oggetto dell'interesse legale
    const idAmbitoInteresse: number = interessiDiMora?.id_ambito_interesse;
    const PATH_AMBITO_INTERESSE: string = `/idAmbitoInteresse/${idAmbitoInteresse}`;
    const PATH_DELETE_TASSO_DI_INTERESSE: string = `${this.PATH_TASSI_INTERESSE}${PATH_AMBITO_INTERESSE}`;

    // Definisco l'url
    const url: string = this.appUrl(PATH_DELETE_TASSO_DI_INTERESSE);

    // Definisco i query params specifici per la
    const queryParams: IInteressiDiMoraParams = {
      fruitore: this.userService.cf ?? RiscaFruitori.RISCA,
    };
    // Definisco i query params per la chiamata
    let params: HttpParams;
    params = this.createQueryParams(queryParams);
    // Definisco le options
    const options = { params };

    // Richiamo il servizio e ritorno l'observable come risposta
    return this._http.delete<IInteressiDiMoraVo>(url, options).pipe(
      map((interessiDiMoraRes: IInteressiDiMoraVo) => {
        return new InteressiDiMoraVo(interessiDiMoraRes);
        // #
      })
    );
  }


  /**
   * Funzione di PUT che tenta la modifica degli interessi di mora in input.
   */
  updateInteressiDiMora(interessiDiMora: InteressiDiMoraVo): Observable<InteressiDiMoraVo> {
    // Definisco l'url
    const url: string = this.appUrl(this.PATH_TASSI_INTERESSE);

    // Definisco i query params specifici per la chiamata
    const queryParams: IInteressiDiMoraParams = {
      //fruitore: RiscaFruitori.RISCA,
    };

    // definisco le options
    const options = { params: this.createQueryParams(queryParams) };

    //richiamo il servizio e ritorno l'observable come risposta
    return this._http.put<IInteressiDiMoraVo>(url, interessiDiMora.toServerFormat(), options).pipe(
      map((interessiDiMoraRes: IInteressiDiMoraVo) => {
        return new InteressiDiMoraVo(interessiDiMoraRes);
      })
    );
  }



  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che lancia le logiche di conversione dati e gestione paginazione per gli interessi di mora.
   * @param res RicercaPaginataResponse<IInteressiLegaliVo[]> con la risposta ottenuta dal server.
   * @returns RicercaPaginataResponse<InteressiLegaliVo[]> come risposta convertita.
   */
  private paginateInteressiDiMora(res: RicercaPaginataResponse<IInteressiDiMoraVo[]>): RicercaPaginataResponse<InteressiDiMoraVo[]> {
    // Recupero la lista di oggetti ritornati dal server
    const { sources } = res;

    // Effettuo un parse dei dati in una lista FE friendly
    let interessiDiMora: InteressiDiMoraVo[];
    interessiDiMora = toListInteressiDiMoraVo(sources);
    // Creo una nuova response da ritornare
    const newPag: RicercaPaginataResponse<InteressiDiMoraVo[]> = {
      paging: res.paging,
      sources: interessiDiMora,
    };

    // Ritorno la response modificata
    return newPag;
  }
}
