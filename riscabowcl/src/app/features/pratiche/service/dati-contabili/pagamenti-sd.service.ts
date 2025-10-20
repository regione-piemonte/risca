import { Injectable } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { DettaglioPagSearchResultVo } from '../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { DettaglioPagamentoExtraVo } from '../../../../core/commons/vo/dettaglio-pagamento-extra';
import { PagamentoVo } from '../../../../core/commons/vo/pagamento-vo';
import {
  IDettagliPagamentoPerModifica,
  IFullUpdatePagamentoReq,
  IFullUpdatePagamentoRes,
} from './utilities/pagamenti-sd.interfaces';
import { VersamentiService } from './versamenti/versamenti.service';

/**
 * Interfaccia custom che racchiude le richieste per lo scarico dati per la modifica di un pagamento.
 */
export interface IDettagliPagamentoPerModificaReq {
  pagamento: Observable<PagamentoVo>;
  dettagliPagamentoSearch: Observable<DettaglioPagSearchResultVo[]>;
}

/**
 * Interfaccia custom che racchiude le risposte dopo lo scarico dati per la modifica di un pagamento.
 */
export interface IDettagliPagamentoPerModificaRes {
  pagamento: PagamentoVo;
  dettagliPagamentoSearch: DettaglioPagSearchResultVo[];
}

@Injectable({ providedIn: 'root' })
export class PagamentiSDService {
  /**
   * Costruttore.
   */
  constructor(private _versamenti: VersamentiService) {}

  /**
   * ############################################################
   * FUNZIONI CRUD E AGGIORNAMENTO PAGAMENTO E DETTAGLI PAGAMENTO
   * ############################################################
   */

  /**
   * Funzione che inserisci i dati di un pagamento su DB.
   * A seguito del salvataggio, vengono scaricati i dettagli pagamento.
   * @param pagamento PagamentoVo contenente le informazioni del pagamento.
   * @param idRiscossione number che definisce l'id pratica di riferimento per lo scarico dei dettagli pagamento.
   * @param idStatoDebitorio number che definisce l'id stato debitorio di riferimento per lo scarico dei dettagli pagamento.
   * @returns Observable<DettaglioPagamentoExtraVo[]> con i dettagli pagamento aggiornati.
   */
  inserisciPagamentoEAggiornaDettagli(
    pagamento: PagamentoVo,
    idRiscossione: number,
    idStatoDebitorio: number
  ): Observable<DettaglioPagamentoExtraVo[]> {
    // Richiamo l'inserimento del pagamento tramite servizio
    return this._versamenti.insertPagamento(pagamento).pipe(
      switchMap((p: PagamentoVo) => {
        // Variabili di comodo
        const idR = idRiscossione;
        const idSD = idStatoDebitorio;
        // Ignoro il valore di ritorno e lancio il servizio di scarico dettagli pagamenti extra
        return this._versamenti.getDettagliPagamenti(idR, idSD);
      })
    );
  }

  /**
   * Funzione che aggiorna i dati di un pagamento su DB.
   * A seguito del salvataggio, vengono scaricati i dettagli pagamento.
   * @param pagamento PagamentoVo contenente le informazioni del pagamento.
   * @param idRiscossione number che definisce l'id pratica di riferimento per lo scarico dei dettagli pagamento.
   * @param idStatoDebitorio number che definisce l'id stato debitorio di riferimento per lo scarico dei dettagli pagamento.
   * @returns Observable<DettaglioPagamentoExtraVo[]> con i dettagli pagamento aggiornati.
   */
  aggiornaPagamentoEAggiornaDettagli(
    pagamento: PagamentoVo,
    idRiscossione: number,
    idStatoDebitorio: number
  ): Observable<DettaglioPagamentoExtraVo[]> {
    // Richiamo l'inserimento del pagamento tramite servizio
    return this._versamenti.updatePagamento(pagamento).pipe(
      switchMap((p: PagamentoVo) => {
        // Variabili di comodo
        const idR = idRiscossione;
        const idSD = idStatoDebitorio;
        // Ignoro il valore di ritorno e lancio il servizio di scarico dettagli pagamenti extra
        return this._versamenti.getDettagliPagamenti(idR, idSD);
      })
    );
  }

  /**
   * Funzione che aggiorna i dati di un pagamento su DB.
   * A seguito del salvataggio, vengono scaricati i dettagli pagamento.
   * @param pagamento PagamentoVo contenente le informazioni del pagamento.
   * @param idRiscossione number che definisce l'id pratica di riferimento per lo scarico dei dettagli pagamento.
   * @param idStatoDebitorio number che definisce l'id stato debitorio di riferimento per lo scarico dei dettagli pagamento.
   * @returns Observable<DettaglioPagamentoExtraVo[]> con i dettagli pagamento aggiornati.
   */
  aggiornaPagamentoECancellaDettagli(
    pagamento: PagamentoVo,
    dettagliPagCancellati: DettaglioPagSearchResultVo[],
    idRiscossione: number,
    idStatoDebitorio: number
  ): Observable<DettaglioPagamentoExtraVo[]> {
    // Definisco l'oggetto di request per la gestione del pagamento e dei dettagli
    const req: IFullUpdatePagamentoReq = {
      pagamento: this._versamenti.updatePagamento(pagamento),
    };

    // Verifico se esiste anche la lista di dettagli pagamento cancellati
    if (dettagliPagCancellati && dettagliPagCancellati.length > 0) {
      // Estraggo gli id dettaglio pagamento dall'array
      const idDettagliPag = dettagliPagCancellati.map(
        (dettPagDel: DettaglioPagSearchResultVo) => {
          // Ritorno solo l'id del dettaglio pagamento
          return dettPagDel.dettaglio_pag?.id_dettaglio_pag;
        }
      );
      // Esistono anche dettagli da cancellare, aggiungo la request
      req.dettagliPagCancellati =
        this._versamenti.deleteDettagliPagamento(idDettagliPag);
    }

    // Richiamo l'inserimento del pagamento tramite servizio
    return forkJoin(req).pipe(
      switchMap((res: IFullUpdatePagamentoRes) => {
        // Variabili di comodo
        const idR = idRiscossione;
        const idSD = idStatoDebitorio;
        // Ignoro il valore di ritorno e lancio il servizio di scarico dettagli pagamenti extra
        return this._versamenti.getDettagliPagamenti(idR, idSD);
      })
    );
  }

  /**
   * Funzione che elimina i dati di un pagamento su DB.
   * A seguito della cancellazione, vengono scaricati i dettagli pagamento.
   * @param idPagamento number che definisce l'id del pagamento da eliminare.
   * @param idRiscossione number che definisce l'id pratica di riferimento per lo scarico dei dettagli pagamento.
   * @param idStatoDebitorio number che definisce l'id stato debitorio di riferimento per lo scarico dei dettagli pagamento.
   * @returns Observable<DettaglioPagamentoExtraVo[]> con i dettagli pagamento aggiornati.
   */
  eliminaPagamentoEAggiornaDettagli(
    idPagamento: number,
    idRiscossione: number,
    idStatoDebitorio: number
  ): Observable<DettaglioPagamentoExtraVo[]> {
    // Richiamo l'inserimento del pagamento tramite servizio
    return this._versamenti.deletePagamento(idPagamento).pipe(
      switchMap((idPag: number) => {
        // Variabili di comodo
        const idR = idRiscossione;
        const idSD = idStatoDebitorio;
        // Ignoro il valore di ritorno e lancio il servizio di scarico dettagli pagamenti extra
        return this._versamenti.getDettagliPagamenti(idR, idSD);
      })
    );
  }

  /**
   * ######################################################
   * FUNZIONI RECUPERO INFORMAZIONI PER DETTAGLIO PAGAMENTO
   * ######################################################
   */

  /**
   * Funzione che recupera tutte le informazioni per la modifica di un pagamento.
   * @param idPagamento number che definisce l'id del pagamento da eliminare.
   * @returns Observable<IDettagliPagamentoPerModifica> con le informazioni per la modifica del pagamento.
   */
  dettagliPagamentoPerModifica(
    idPagamento: number
  ): Observable<IDettagliPagamentoPerModifica> {
    // Definisco l'oggetto contenente le request per i dati
    const req: IDettagliPagamentoPerModificaReq = {
      pagamento: this._versamenti.getPagamento(idPagamento),
      dettagliPagamentoSearch:
        this._versamenti.getDettagliPagSearchResult(idPagamento),
    };

    return forkJoin(req).pipe(
      map((res: IDettagliPagamentoPerModificaRes) => {
        // Estraggo dalla response le informazioni ritornate
        const { pagamento, dettagliPagamentoSearch } = res || {};
        // Definisco l'oggetto di ritorno per le chiamate
        const data: IDettagliPagamentoPerModifica = {
          pagamento,
          dettagliPagamentoSearch,
        };
        // Ritorno le informazioni mappate
        return data;
      })
    );
  }
}
