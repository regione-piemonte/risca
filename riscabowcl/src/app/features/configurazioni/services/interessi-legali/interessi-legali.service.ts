import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserService } from 'src/app/core/services/user.service';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { IInteressiLegaliVo, InteressiLegaliVo, toListInteressiLegaliVo } from '../../../../core/commons/vo/interessi-legali-vo';
import { ConfigService } from '../../../../core/services/config.service';
import { HttpHelperService } from '../../../../core/services/http-helper/http-helper.service';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaTablePagination, RiscaFruitori } from '../../../../shared/utilities';
import { IInteressiLegaliParams } from './utilities/interessi-legali.interfaces';
import { InteressiLegaliConst } from './utilities/interessi-legali.const';

@Injectable({ providedIn: 'root' })
export class InteressiLegaliService extends HttpUtilitiesService {
  /** Costante per il path: /tassi-interesse */
  readonly PATH_TASSI_INTERESSE = '/tassi-interesse';
  // Dichiaro la costante per gli interessi legali
  IL_C = new InteressiLegaliConst();


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
   * Get che recupera la lista degli interessi legali da visualizzare.
   * @returns Observable<InteressiLegaliVo[]> con i dati scaricati.
   */
  getInteressiLegali(): Observable<InteressiLegaliVo[]> {
    // Dichiaro l'url per la chiamata al servizio
    const url: string = this.appUrl(this.PATH_TASSI_INTERESSE);

    // Richiamo il servizio tramite API
    return this._http.get<IInteressiLegaliVo[]>(url).pipe(
      // Convertire i dati da BE a FE
      map((interessiLegaliVo: IInteressiLegaliVo[]) => {
        // Converto la lista in input su oggetto FE
        return toListInteressiLegaliVo(interessiLegaliVo);//CAPIRE SE VA CAMBIATO CREADO UNA NUOVA CLASSE IInteressiDiMoraVo
        // #
      })
    );
  }

  /**
   * Funzione di GET che recupera le informazioni relative alle lista degli interessi legali.
   * La funzione ritornerà i risultati paginati.
   * @param paginazione RiscaTablePagination con l'oggetto di ricerca per la paginazione.
   * @returns Observable<RicercaPaginataResponse<InteressiLegaliVo[]>> con le informazioni scaricate.
   */
  getInteressiLegaliPaginated(paginazione: RiscaTablePagination): Observable<RicercaPaginataResponse<InteressiLegaliVo[]>> {
    // Definisco l'url
    const url: string = this.appUrl(this.PATH_TASSI_INTERESSE);
    // Definisco i query params specifici per la
    const queryParams: IInteressiLegaliParams = {
      idAmbito: this.userService.idAmbito,
      fruitore: this.userService.cf ?? RiscaFruitori.RISCA,
      tipoDiInteresse: this.IL_C.INTERESSE_LEGALE
    };
    // Definisco i query params per la chiamata
    let params: HttpParams;
    params = this.createPagingParams(paginazione, queryParams);
    // Definisco le options
    const options = { params };

    // Effettuo la chiamata al servizio
    return this._httpHelper
      .searchWithGet<IInteressiLegaliVo[]>(url, options)
      .pipe(
        map((res: RicercaPaginataResponse<IInteressiLegaliVo[]>) => {
          // Ritorno la response convertita
          return this.paginateInteressiLegali(res);
        })
      );
  }

  /**
   * Funzione di POST che tenta il salvataggio degli interessi legali aggiunti dall'utente.
   * @param interessiLegali InteressiLegaliVo con i dati da salvare.
   * @return Observable<InteressiLegaliVo> con la risposta ottenuta dal server.
   */
  postInteressiLegali(interessiLegali: InteressiLegaliVo): Observable<InteressiLegaliVo> {
    // Definisco l'url
    const url: string = this.appUrl(this.PATH_TASSI_INTERESSE);
    // Definisco i query params specifici per la
    const queryParams: IInteressiLegaliParams = {
        //fruitore: this.userService.cf ?? RiscaFruitori.RISCA,
    };
    // Definisco i query params per la chiamata
    let params: HttpParams;
    params = this.createQueryParams(queryParams);
    // Definisco le options
    const options = { params };
    // Converto l'oggetto FE in oggetto BE like
    const body: IInteressiLegaliVo = interessiLegali?.toServerFormat();

    // Richiamo il servizio e ritorno l'observable come risposta
    return this._http.post<IInteressiLegaliVo>(url, body, options).pipe(
      map((interessiLegaliRes: IInteressiLegaliVo) => {
        // Converto la risposta con un oggetto FE
        return new InteressiLegaliVo(interessiLegaliRes);
        // #
      })
    );
  }

  /**
   * Funzione di DELETE che tenta la cancellazione degli interessi legali in input.
   * @param interessiLegali InteressiLegaliVo con i dati da salvare.
   * @return Observable<InteressiLegaliVo> con la risposta ottenuta dal server.
   */
  deleteInteressiLegali(interessiLegali: InteressiLegaliVo): Observable<InteressiLegaliVo> {
    // Tento di recuperare l'id dell'oggetto dell'interesse legale
    const idAmbitoInteresse: number = interessiLegali?.id_ambito_interesse;
    const PATH_AMBITO_INTERESSE: string = `/idAmbitoInteresse/${idAmbitoInteresse}`;
    const PATH_DELETE_TASSO_DI_INTERESSE: string = `${this.PATH_TASSI_INTERESSE}${PATH_AMBITO_INTERESSE}`;

    // Definisco l'url
    const url: string = this.appUrl(PATH_DELETE_TASSO_DI_INTERESSE);

    // Definisco i query params specifici per la
    const queryParams: IInteressiLegaliParams = {
      fruitore: this.userService.cf ?? RiscaFruitori.RISCA,
    };
    // Definisco i query params per la chiamata
    let params: HttpParams;
    params = this.createQueryParams(queryParams);
    // Definisco le options
    const options = { params };

    // Richiamo il servizio e ritorno l'observable come risposta
    return this._http.delete<IInteressiLegaliVo>(url, options).pipe(
      map((interessiLegaliRes: IInteressiLegaliVo) => {
        return new InteressiLegaliVo(interessiLegaliRes);
        // #
      })
    );
  }


  /**
   * Funzione di PUT che tenta la modifica degli interessi legali in input.
   */
  updateInteressiLegali(interessiLegali: InteressiLegaliVo): Observable<InteressiLegaliVo> {
    // Definisco l'url
    const url: string = this.appUrl(this.PATH_TASSI_INTERESSE);

    // Definisco i query params specifici per la chiamata
    const queryParams: IInteressiLegaliParams = {
      fruitore: this.userService.cf ?? RiscaFruitori.RISCA,
    };

    // definisco le options
    const options = { params: this.createQueryParams(queryParams) };

    //richiamo il servizio e ritorno l'observable come risposta
    return this._http.put<IInteressiLegaliVo>(url, interessiLegali.toServerFormat(), options).pipe(
      map((interessiLegaliRes: IInteressiLegaliVo) => {
        return new InteressiLegaliVo(interessiLegaliRes);
      })
    );
  }



  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che lancia le logiche di conversione dati e gestione paginazione per gli interessi legali.
   * @param res RicercaPaginataResponse<IInteressiLegaliVo[]> con la risposta ottenuta dal server.
   * @returns RicercaPaginataResponse<InteressiLegaliVo[]> come risposta convertita.
   */
  private paginateInteressiLegali(res: RicercaPaginataResponse<IInteressiLegaliVo[]>): RicercaPaginataResponse<InteressiLegaliVo[]> {
    // Recupero la lista di oggetti ritornati dal server
    const { sources } = res;

    // Effettuo un parse dei dati in una lista FE friendly
    let interessiLegali: InteressiLegaliVo[];
    interessiLegali = toListInteressiLegaliVo(sources);
    // Creo una nuova response da ritornare
    const newPag: RicercaPaginataResponse<InteressiLegaliVo[]> = {
      paging: res.paging,
      sources: interessiLegali,
    };

    // Ritorno la response modificata
    return newPag;
  }
}
