import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import {
  IRegolaUsoReadVo,
  IRegolaUsoWriteVo,
  RegolaUsoVo,
} from '../../../../core/commons/vo/regola-uso-vo';
import {
  IUsoLeggeVo,
  UsoLeggeVo,
} from '../../../../core/commons/vo/uso-legge-vo';
import { ConfigService } from '../../../../core/services/config.service';
import { HttpHelperService } from '../../../../core/services/http-helper/http-helper.service';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaTablePagination } from '../../../../shared/utilities';
import {
  IPostCreaAnnualita,
  IRicercaUsiRegola,
} from './utilities/configurazioni.interfaces';

@Injectable({ providedIn: 'root' })
export class ConfigurazioniService extends HttpUtilitiesService {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** Costante per il path: /tipi-usi-regole */
  readonly PATH_TIPI_USI_REGOLE = '/tipi-usi-regole';
  /** Costante per il path: /tipi-usi-regole/anni */
  readonly PATH_ANNI_REGOLA = `${this.PATH_TIPI_USI_REGOLE}/anni`;
  /** Costante per il path: /tipi-usi-regole/lista-regole */
  readonly PATH_LISTA_REGOLE = `${this.PATH_TIPI_USI_REGOLE}/lista-regole`;

  /** Costante per il path: /tipi-uso/idTipoUsoPadre */
  readonly PATH_TIPI_USO_EFFETTIVI = '/tipi-uso/idTipoUsoPadre';

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
   * Funzione di GET che recupera la lista degli anni disponibili per i tipi usi regole, dato un id ambito di riferimento.
   * @param idAmbito number con l'id ambito da utilizzare per la ricerca dati.
   * @returns Observable<number[]> con le informazioni scaricate.
   */
  getAnniUsiRegole(idAmbito: number): Observable<number[]> {
    // Definisco l'url
    let url: string;
    url = this.appUrl(this.PATH_ANNI_REGOLA, idAmbito);

    // Effettuo la chiamata al servizio
    return this._http.get<number[]>(url);
  }

  /**
   * Funzione di GET che recupera le informazioni relative alle lista delle regole per gli usi di legge.
   * @param idAmbito number con l'id ambito da utilizzare per la ricerca dati.
   * @param anno number con l'anno da utilizzare per la ricerca dati.
   * @returns Observable<RegolaUsoVo[]> con le informazioni scaricate.
   */
  getListaRegole(idAmbito: number, anno: number): Observable<RegolaUsoVo[]> {
    // Definisco l'url
    let url: string;
    url = this.appUrl(this.PATH_LISTA_REGOLE, idAmbito, anno);

    // Effettuo la chiamata al servizio
    return this._http.get<IRegolaUsoReadVo[]>(url).pipe(
      map((res: IRegolaUsoReadVo[]) => {
        // Rimappo la risposta in formato FE
        return this.convertListToRegolaUsoVo(res);
      })
    );
  }

  /**
   * Funzione di GET che recupera le informazioni relative alle lista delle regole per gli usi di legge.
   * La funzione ritornerà i risultati paginati.
   * @param pathParams IRicercaUsiRegola con le informazioni di ricerca in path params.
   * @param paginazione RiscaTablePagination con l'oggetto di ricerca per la paginazione.
   * @returns Observable<RegolaUsoVo[]> con le informazioni scaricate.
   */
  getListaRegolePaginated(
    pathParams: IRicercaUsiRegola,
    paginazione?: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<RegolaUsoVo[]>> {
    // Estraggo dalla chiamata i path params
    const { idAmbito, anno } = pathParams ?? {};

    // Definisco l'url
    let url: string;
    url = this.appUrl(this.PATH_LISTA_REGOLE, idAmbito, anno);
    // Definisco i query params per la chiamata
    const params = this.createPagingParams(paginazione);
    // Definisco le options
    const options = { params };

    // Effettuo la chiamata al servizio
    return this._httpHelper
      .searchWithGet<IRegolaUsoReadVo[]>(url, options)
      .pipe(
        map((res: RicercaPaginataResponse<IRegolaUsoReadVo[]>) => {
          // Ritorno la response convertita
          return this.paginateListaRegole(res);
        })
      );
  }

  /**
   * Funzione di PUT che effettua l'aggiornamento della lista di regole uso.
   * @param regole RegolaUsoVo[] con la lista di oggetti da aggiornare.
   * @returns Observable<RegolaUsoVo[]> con le informazioni aggiornate.
   */
  putListaRegole(regole: RegolaUsoVo[]): Observable<RegolaUsoVo[]> {
    // Verifico l'input
    regole = regole ?? [];

    // Definisco l'url
    let url: string;
    url = this.appUrl(this.PATH_LISTA_REGOLE);

    // Definisco il body della richiesta, andando a formattare gli oggetti per il BE
    let body: IRegolaUsoWriteVo[];
    body = regole.map((r: RegolaUsoVo) => {
      // Effettuo la conversione dell'oggetto
      return r.toServerFormat();
    });

    // Effettuo la chiamata al servizio
    return this._http.put<IRegolaUsoWriteVo[]>(url, body).pipe(
      map((res: IRegolaUsoWriteVo[]) => {
        // Rimappo la risposta in formato FE
        return this.convertListToRegolaUsoVo(res);
      })
    );
  }

  /**
   * Funzione di PUT che effettua l'aggiornamento della lista di regole uso.
   * @param regola RegolaUsoVo con l'oggetto da aggiornare.
   * @returns Observable<RegolaUsoVo> con le informazioni aggiornate.
   */
  putRegola(regola: RegolaUsoVo): Observable<RegolaUsoVo> {
    // Definisco l'url
    let url: string;
    url = this.appUrl(this.PATH_TIPI_USI_REGOLE);

    // Definisco il body della richiesta, andando a formattare gli oggetti per il BE
    let body: IRegolaUsoWriteVo;
    body = regola.toServerFormat();

    // Effettuo la chiamata al servizio
    return this._http.put<IRegolaUsoWriteVo>(url, body).pipe(
      map((res: IRegolaUsoWriteVo) => {
        // Rimappo la risposta in formato FE
        return new RegolaUsoVo(res);
      })
    );
  }

  /**
   * Funzione di POST che crea le informazioni relative ad una nuova annualità.
   * @param postParams IPostCreaAnnualita con le informazioni per effettuare la funzione post.
   * @returns Observable<RegolaUsoVo[]> con le informazioni scaricate.
   */
  postCreaAnnualita(postParams: IPostCreaAnnualita): Observable<RegolaUsoVo[]> {
    // Definisco l'url
    let url: string;
    url = this.appUrl(this.PATH_TIPI_USI_REGOLE);
    // Definisco il body della richiesta
    let body: IPostCreaAnnualita;
    body = postParams ?? { anno: undefined, idAmbito: undefined };

    // Effettuo la chiamata al servizio
    return this._http.post<IRegolaUsoReadVo[]>(url, body).pipe(
      map((res: IRegolaUsoReadVo[]) => {
        // Rimappo la risposta in formato FE
        return this.convertListToRegolaUsoVo(res);
      })
    );
  }

  /**
   * Funzione di GET che recupera la lista degli usi di legge effettivi, collegati ad un uso di legge.
   * @param idUsoPadre number con l'id uso per il riferimento dello scarico dati.
   * @returns Observable<UsoLeggeVo[]> con le informazioni scaricate.
   */
  getUsiEffettivi(idUsoPadre: number): Observable<UsoLeggeVo[]> {
    // Definisco l'url
    let url: string;
    url = this.appUrl(this.PATH_TIPI_USO_EFFETTIVI, idUsoPadre);

    // Effettuo la chiamata al servizio
    return this._http.get<IUsoLeggeVo[]>(url).pipe(
      map((res: IUsoLeggeVo[]) => {
        // Converto e ritorno la lista
        return this.convertListToUsoLeggeVo(res);
      })
    );
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che lancia le logiche di conversione dati e gestione paginazione per le regole.
   * @param res RicercaPaginataResponse<IRegolaUsoReadVo[] | IRegolaUsoWriteVo[]> con la risposta ottenuta dal server.
   * @returns RicercaPaginataResponse<RegolaUsoVo[]> come risposta convertita.
   */
  private paginateListaRegole(
    res: RicercaPaginataResponse<IRegolaUsoReadVo[] | IRegolaUsoWriteVo[]>
  ): RicercaPaginataResponse<RegolaUsoVo[]> {
    // Recupero la lista di oggetti ritornati dal server
    const { sources } = res;

    // Effettuo un parse dei dati in una lista FE friendly
    let regoleUsi: RegolaUsoVo[];
    regoleUsi = this.convertListToRegolaUsoVo(sources);
    // Creo una nuova response da ritornare
    const newPag: RicercaPaginataResponse<RegolaUsoVo[]> = {
      paging: res.paging,
      sources: regoleUsi,
    };

    // Ritorno la response modificata
    return newPag;
  }

  /**
   * Funzione di supporto che converte una lista di oggetti IRegolaUsoReadVo[] o IRegolaUsoWriteVo[] in una lista di oggetti RegolaUsoVo[].
   * @param regoleUso IRegolaUsoReadVo[] | IRegolaUsoWriteVo[] da convertire.
   * @returns RegolaUsoVo[] convertiti.
   */
  private convertListToRegolaUsoVo(
    regoleUso: IRegolaUsoReadVo[] | IRegolaUsoWriteVo[]
  ): RegolaUsoVo[] {
    /**
     * NOTA BENE: questa forzatura funziona solo perché l'unione dei tipi: IRegolaUsoReadVo[] | IRegolaUsoWriteVo[];
     * come definizione non piace a typescript per poter gestire la variabile come un array di oggetti.
     * Anche se quindi definisco: "regole" come => (IRegolaUsoReadVo | IRegolaUsoWriteVo)[]; l'array sarà sempre composto da uno solo dei due tipi.
     */
    // Verifico l'input e definisco un eventuale valore di default
    let regole: (IRegolaUsoReadVo | IRegolaUsoWriteVo)[];
    regole = (regoleUso as (IRegolaUsoReadVo | IRegolaUsoWriteVo)[]) ?? [];

    // Converto e ritorno la lista
    return regole?.map((iRURW: IRegolaUsoReadVo | IRegolaUsoWriteVo) => {
      // Creo l'oggetto di FE
      return new RegolaUsoVo(iRURW);
    });
  }

  /**
   * Funzione di supporto che converte una lista di oggetti IUsoLeggeVo[] in una lista di oggetti UsoLeggeVo[].
   * @param usiLegge IUsoLeggeVo[] da convertire.
   * @returns UsoLeggeVo[] convertiti.
   */
  private convertListToUsoLeggeVo(usiLegge: IUsoLeggeVo[]): UsoLeggeVo[] {
    // Verifico l'input e definisco un eventuale valore di default
    usiLegge = usiLegge ?? [];
    // Converto e ritorno la lista
    return usiLegge?.map((usoLegge: IUsoLeggeVo) => {
      // Creo l'oggetto di FE
      return new UsoLeggeVo(usoLegge);
    });
  }
}
