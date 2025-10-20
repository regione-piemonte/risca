import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import {
  IPagamentoNonProprioVo,
  PagamentoNonProprioVo,
} from 'src/app/core/commons/vo/pagamento-non-proprio-vo';
import {
  IPagamentoVo,
  PagamentoVo,
} from 'src/app/core/commons/vo/pagamento-vo';
import { ConfigService } from 'src/app/core/services/config.service';
import { RicercaPaginataResponse } from '../../../../../core/classes/http-helper/http-helper.classes';
import {
  AssegnaPagamento,
  IAssegnaPagamento,
} from '../../../../../core/commons/vo/assegna-pagamento-vo';
import {
  DettaglioPagSearchResultVo,
  IDettaglioPagSearchResultVo,
} from '../../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import {
  DettaglioPagamentoExtraVo,
  IDettaglioPagamentoExtraVo,
} from '../../../../../core/commons/vo/dettaglio-pagamento-extra';
import {
  DettaglioPagListVo,
  DettaglioPagVo,
  IDettaglioPagListVo,
  IDettaglioPagVo,
} from '../../../../../core/commons/vo/dettaglio-pagamento-vo';
import { HttpHelperService } from '../../../../../core/services/http-helper/http-helper.service';
import { HttpUtilitiesService } from '../../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaTablePagination } from '../../../../../shared/utilities';
import { isPagamentoManuale } from './versamenti.functions';

@Injectable({ providedIn: 'root' })
export class VersamentiService extends HttpUtilitiesService {
  /** Costante per il path: /pagamenti */
  private PATH_PAGAMENTI = '/pagamenti';
  /** Costante per il path: /assegna-pagamenti */
  private PATH_ASSEGNA_PAGAMENTI = '/assegna-pagamenti';
  /** Costante per il path: /pagamenti/idStatoDebitorio */
  private PATH_PAGAMENTI_STATO_DEBITORIO = '/pagamenti/idStatoDebitorio';
  /** Costante per il path: /pagamenti/idPagamento */
  private PATH_PAGAMENTI_PAGAMENTO = '/pagamenti/idPagamento';
  /** Costante per il path: /pagamenti/idRiscossione */
  private PATH_DETTAGLI_PAGAMENTI_RISCOSSIONE = '/pagamenti/idRiscossione';
  /** Costante per il path: /dettaglio-pag */
  private PATH_DETTAGLIO_PAG = '/dettaglio-pag';
  /** Costante per il path: /dettaglio-pag */
  private PATH_DETTAGLIO_PAG_LIST = '/dettaglio-pag-list';
  /** Costante per il path: /idDettaglioPag */
  private PATH_ID_DETTAGLIO_PAG = '/idDettaglioPag';
  /** Costante per il path: /dettaglio-pag-search-result/idPagamento */
  private PATH_DETTAGLIO_PAG_SEARCH_RESULT =
    '/dettaglio-pag-search-result/idPagamento';
  /** Costante per il path: /pagamenti/pagamenti-da-visionare */
  private PATH_PAGAMENTI_DA_VISIONARE = '/pagamenti/pagamenti-da-visionare';

  /**
   * Costruttore.
   */
  constructor(
    config: ConfigService,
    private _http: HttpClient,
    private _httpHelper: HttpHelperService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * Funzione che prende i pagamenti di una pratica
   * @param idStatoDebitorio number idStatoDebitorio di cui prendere i pagamenti
   * @returns lista dei pagamenti della pratica.
   */
  getPagamenti(idStatoDebitorio: number): Observable<PagamentoVo[]> {
    // Creo l'url da richiamare
    const url = this.appUrl(
      this.PATH_PAGAMENTI_STATO_DEBITORIO,
      idStatoDebitorio
    );

    // Richiamo il servizio
    return this._http.get<IPagamentoVo[]>(url).pipe(
      map((p: IPagamentoVo[]) => {
        // Array di appoggio
        const pagam: PagamentoVo[] = [];
        // Converto i pagamenti dal server
        p.forEach((pag: IPagamentoVo) => {
          const pagamento = new PagamentoVo(pag);
          pagam.push(pagamento);
        });
        // Response
        return pagam;
      })
    );
  }

  /**
   * Funzione che recupera una lista di dettaglio pagamenti dato idRiscossione e idStatoDebitorio.
   * @param idRiscossione number con l'id riscossione per il recupero dati.
   * @param idStatoDebitorio number l'id stato debitorio per il recupero dati.
   * @returns Observable<DettaglioPagamentoExtraVo[]> con la lista delle informazioni di dettaglio con pagamento.
   */
  getDettagliPagamenti(
    idRiscossione: number,
    idStatoDebitorio: number
  ): Observable<DettaglioPagamentoExtraVo[]> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_DETTAGLIO_PAG);
    // Definisco i query params
    const params = this._riscaUtilities.createQueryParams({
      idRiscossione,
      idStatoDebitorio,
    });

    // Richiamo il servizio
    return this._http.get<IDettaglioPagamentoExtraVo[]>(url, { params }).pipe(
      map((dettPags: IDettaglioPagamentoExtraVo[]) => {
        // Rimappo le informazioni ritornate in oggetto FE
        return dettPags.map((dp: IDettaglioPagamentoExtraVo) => {
          // Converto e ritorno l'oggetto
          return new DettaglioPagamentoExtraVo(dp);
        });
      })
    );
  }

  /**
   * Funzione che recupera una lista di dettaglio pagamenti search result dato idPagamento..
   * @param idPagamento number con l'id pagamento per il recupero dati.
   * @returns Observable<DettaglioPagSearchResultVo[]> con la lista delle informazioni di dettaglio con pagamento.
   */
  getDettagliPagSearchResult(
    idPagamento: number
  ): Observable<DettaglioPagSearchResultVo[]> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_DETTAGLIO_PAG_SEARCH_RESULT, idPagamento);

    // Richiamo il servizio
    return this._http.get<IDettaglioPagSearchResultVo[]>(url).pipe(
      map((dettPags: IDettaglioPagSearchResultVo[]) => {
        // Rimappo le informazioni ritornate in oggetto FE
        return dettPags.map((dp: IDettaglioPagSearchResultVo) => {
          // Converto e ritorno l'oggetto
          return new DettaglioPagSearchResultVo(dp);
          // #
        });
      })
    );
  }

  /**
   * Funzione che recupera la lista dei dettagli pagamenti per una riscossione.
   * @param idRiscossione number idRiscossione per lo scarico di tutti i dettagli.
   * @returns DettaglioPagamentoNonPropriVo dettaglio pagamento trovato.
   */
  getDettagliPagamentiRiscossione(
    idRiscossione: number
  ): Observable<PagamentoVo[]> {
    // Creo l'url da richiamare
    const url = this.appUrl(
      this.PATH_DETTAGLI_PAGAMENTI_RISCOSSIONE,
      idRiscossione
    );

    // Richiamo il servizio
    return this._http.get<IPagamentoVo[]>(url).pipe(
      map((p: IPagamentoVo[]) => {
        // Array di appoggio
        const pagam: PagamentoVo[] = [];
        // Converto i pagamenti dal server
        p.forEach((pag: IPagamentoVo) => {
          const pagamento = new PagamentoVo(pag);
          pagam.push(pagamento);
        });
        // Response
        return pagam;
      })
    );
  }

  /**
   * Funzione che prende un pagamento specifico di una pratica
   * @param idDettaglioPag number idRiscossione di cui prendere i pagamenti
   * @returns DettaglioPagamentoNonPropriVo dettaglio pagamento trovato.
   */
  getDettaglioPagNonProprio(
    idDettaglioPag?: number
  ): Observable<PagamentoNonProprioVo> {
    // Controllo di esistenza
    if (idDettaglioPag == undefined) {
      // Ritorno null
      return of(null);
    }

    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_PAGAMENTI, idDettaglioPag);

    // Richiamo il servizio
    return this._http.get<IPagamentoNonProprioVo>(url).pipe(
      map((p: IPagamentoNonProprioVo) => {
        // Converto l'oggetto
        const pag = new PagamentoNonProprioVo(p);
        // Ritorno l'oggetto traformato
        return pag;
      })
    );
  }

  /**
   * Funzione che scarica tramite servizio un oggetto pagamento.
   * @param idPagamento number come id del pagamento da recuperare.
   * @returns Observable<PagamentoVo> con l'oggetto pagamento scaricato.
   */
  getPagamento(idPagamento: number): Observable<PagamentoVo> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_PAGAMENTI_PAGAMENTO, idPagamento);
    // Imposto i valori
    return this._http.get<IPagamentoVo>(url).pipe(
      map((iPagamento: IPagamentoVo) => {
        // Ritorno l'oggetto FE
        return new PagamentoVo(iPagamento);
      })
    );
  }

  /**
   * Funzione di supporto che gestisce l'inserimento di un pagamento dalla pagina dei versamenti dello stato debitorio
   * @param pagamento PagamentoVo contenente le informazioni del form.
   * @returns pagamento appena inserito contenente il suo idPagamento.
   */
  insertPagamento(pagamento: PagamentoVo): Observable<PagamentoVo> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_PAGAMENTI);
    // Genero l'oggetto dell'interfaccia inserendo solo i dati modificati
    const pag: IPagamentoVo = pagamento.toServerFormat();

    // Imposto i valori
    return this._http.post<IPagamentoVo>(url, pag).pipe(
      map((p: IPagamentoVo) => {
        // Trasformo il dato
        return new PagamentoVo(p);
      })
    );
  }

  /**
   * Funzione che gestisce l'inserimento delle informazioni madiante DettaglioPagListVo.
   * @param dettaglioPagList DettaglioPagListVo con le informazioni da savare.
   * @returns Observable<DettaglioPagVo[]> con gli oggetti creati dall'insert.s
   */
  insertDettaglioPagList(
    dettaglioPagList: DettaglioPagListVo
  ): Observable<DettaglioPagVo[]> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_DETTAGLIO_PAG_LIST);
    // Genero l'oggetto dell'interfaccia inserendo solo i dati modificati
    const dettPagList: IDettaglioPagListVo = dettaglioPagList.toServerFormat();

    // Imposto i valori
    return this._http.post<IDettaglioPagVo[]>(url, dettPagList).pipe(
      map((dettaglioPagamentiVo: IDettaglioPagVo[]) => {
        // Trasformo i dati
        return dettaglioPagamentiVo?.map((dettPag: IDettaglioPagVo) => {
          // Converto il singolo oggetto
          return new DettaglioPagVo(dettPag);
        });
      })
    );
  }

  /**
   * Funzione di supporto che gestisce l'aggiornamento dello stato debitorio dalla pagina dei versamenti
   * @param data IVersamenti contenente le informazioni del form
   * @returns pagamento appena modificato contenente il suo idPagamento.
   */
  updatePagamento(pagamento: PagamentoVo): Observable<PagamentoVo> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_PAGAMENTI);

    // Genero l'oggetto dell'interfaccia
    const pag: IPagamentoVo = pagamento.toServerFormat();

    // Imposto i valori
    return this._http.put<IPagamentoVo>(url, pag).pipe(
      map((p: IPagamentoVo) => {
        // Trasformo il dato
        return new PagamentoVo(p);
      })
    );
  }

  /**
   * Funzione di supporto che gestisce l'aggiornamento del pagamento, tramite "assegna pagamento".
   * @param assegnaPagamento AssegnaPagamento contenente le informazioni per l'aggiornamento del pagamento.
   * @returns Observable<AssegnaPagamento> come risultato dell'aggiornamento dati.
   */
  updateAssegnaPagamento(
    assegnaPagamento: AssegnaPagamento
  ): Observable<AssegnaPagamento> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_ASSEGNA_PAGAMENTI);
    // Genero l'oggetto dell'interfaccia
    const assegnaPag: IAssegnaPagamento = assegnaPagamento.toServerFormat();

    // Imposto i valori
    return this._http.post<IAssegnaPagamento>(url, assegnaPag).pipe(
      map((ap: IAssegnaPagamento) => {
        // Trasformo il dato
        return new AssegnaPagamento(ap);
      })
    );
  }

  /**
   * Funzione di supporto che gestisce l'aggiornamento dello stato debitorio dalla pagina dei versamenti
   * @param data IVersamenti contenente le informazioni del form.
   * @returns risultato della cancellazione.
   */
  deletePagamento(idPagamento: number): Observable<number> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_PAGAMENTI_PAGAMENTO, idPagamento);
    // Imposto i valori
    return this._http.delete<number>(url);
  }

  /**
   * Funzione che elimina un oggetto dettaglio pagamento, dato il suo id.
   * @param idDettaglioPag number con l'id dettaglio pagamento da cancellare.
   * @returns Observable<number> con l'id del dettaglio pagamento cancellato.
   */
  deleteDettaglioPagamento(idDettaglioPag: number): Observable<number> {
    // Creo l'url da richiamare
    const url = this.appUrl(
      this.PATH_DETTAGLIO_PAG,
      this.PATH_ID_DETTAGLIO_PAG,
      idDettaglioPag
    );

    // Richiamo il servizio
    return this._http.delete<number>(url);
  }

  /**
   * Funzione che elimina una lista di oggetti dettaglio pagamento, dati gli id.
   * @param idDettagliPag number[] con gli id dettaglio pagamento da cancellare.
   * @returns Observable<number[]> con gli id dei dettagli pagamento cancellati.
   */
  deleteDettagliPagamento(idDettagliPag: number[]): Observable<number[]> {
    // Verifico l'input
    if (!idDettagliPag || idDettagliPag.length == 0) {
      // Niente configurazione
      return of([]);
    }

    // Esiste l'array di id, creo un array di request tramite cancellazione singola
    const reqs: Observable<number>[] = idDettagliPag.map((idDetPag: number) => {
      // Ritorno la request di cancellazione
      return this.deleteDettaglioPagamento(idDetPag);
    });

    // Richiamo la forkjoin dei dati
    return forkJoin(reqs);
  }

  /**
   * Get che ritorna la lista dei pagamenti da visionare.
   * @returns Observable<PagamentoVo[]> con la lista dei dati scaricati.
   */
  getPagamentiDaVisionare(): Observable<PagamentoVo[]> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_PAGAMENTI_DA_VISIONARE);

    // Richiamo il servizio
    return this._http.get<IPagamentoVo[]>(url).pipe(
      map((p: IPagamentoVo[]) => {
        // Verifico e gestisco l'oggetto
        p = p ? p : [];
        // Converto i pagamenti dal server
        return p.map((pag: IPagamentoVo) => {
          // Converto l'oggetto da BE a FE
          return new PagamentoVo(pag);
        });
      })
    );
  }

  /**
   * Get che ritorna la lista dei pagamenti da visionare con paginazione.
   * @param paginazione RiscaTablePagination con l'oggetto per la paginazione.
   * @returns Observable<RicercaPaginataResponse<PagamentoVo[]>> con la lista dei dati scaricati e paginati.
   */
  getPagamentiDaVisionarePaginated(
    paginazione: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<PagamentoVo[]>> {
    // Creo l'url da richiamare
    const url = this.appUrl(this.PATH_PAGAMENTI_DA_VISIONARE);
    // Prendo i parametri
    const params = this.createPagingParams(paginazione);
    // Definisco le options
    const options = { params };

    // Richiamo il servizio
    return this._httpHelper.searchWithGet<IPagamentoVo[]>(url, options).pipe(
      map((p: RicercaPaginataResponse<IPagamentoVo[]>) => {
        // Lancio la funzione di conversione e ritorno il valore
        return this.paginatePagamenti(p);
      })
    );
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che lancia le logiche di conversione dati e gestione paginazione per i pagamenti.
   * @param res RicercaPaginataResponse<IPagamentoVo[]> con la risposta ottenuta dal server.
   * @returns RicercaPaginataResponse<PagamentoVo[]> come risposta convertita.
   */
  private paginatePagamenti(
    res: RicercaPaginataResponse<IPagamentoVo[]>
  ): RicercaPaginataResponse<PagamentoVo[]> {
    // Recupero la lista di oggetti ritornati dal server
    const { sources } = res;
    // Effettuo un parse dei dati in una lista FE friendly
    const pagamenti: PagamentoVo[] = sources?.map((iSD: IPagamentoVo) => {
      // Creo l'oggetto di FE
      return new PagamentoVo(iSD);
    });
    // Creo una nuova response da ritornare
    const newPag: RicercaPaginataResponse<PagamentoVo[]> = {
      paging: res.paging,
      sources: pagamenti,
    };

    // Ritorno la response modificata
    return newPag;
  }
  
  /**
   * Funzione di comodo che verifica se un pagamento è di tipo manuale.
   * @param pagamento PagamentoVo con l'oggetto da verificare.
   * @returns boolean che indica se il pagamento è manuale.
   */
  isPagamentoManuale(pagamento: PagamentoVo): boolean {
    // Richiamo al funzione di supporto
    return isPagamentoManuale(pagamento);
  }
}
