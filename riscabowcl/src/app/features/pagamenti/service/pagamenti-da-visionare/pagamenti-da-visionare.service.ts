import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { PagamentoVo } from '../../../../core/commons/vo/pagamento-vo';
import { RiscaTablePagination } from '../../../../shared/utilities';
import { VersamentiService } from '../../../pratiche/service/dati-contabili/versamenti/versamenti.service';

@Injectable({ providedIn: 'root' })
export class PagamentiDaVisionareService {
  /**
   * Costruttore.
   */
  constructor(private _versamenti: VersamentiService) {}

  /**
   * #################################################################
   * FUNZIONI DI RECUPERO E GESTIONE DATI PER I PAGAMENTI DA VISIONARE
   * #################################################################
   */

  /**
   * Funzione che richiama il get dei pagamenti da visionare e ritorna le informazioni da visualizzare.
   * @returns Observable<PagamentoVo[]> con i pagamenti da visionare dall'utente.
   */
  getPagamentiDaVisionare(): Observable<PagamentoVo[]> {
    // Richiamo il servizio di scarico dati
    return this._versamenti.getPagamentiDaVisionare();
  }

  /**
   * Get che ritorna la lista dei pagamenti da visionare con paginazione.
   * @param paginazione RiscaTablePagination con l'oggetto per la paginazione.
   * @returns Observable<RicercaPaginataResponse<PagamentoVo[]>> con la lista dei dati scaricati e paginati.
   */
  getPagamentiDaVisionarePaginated(
    paginazione: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<PagamentoVo[]>> {
    // Richiamo il servizio di scarico dati
    return this._versamenti.getPagamentiDaVisionarePaginated(paginazione);
  }
}
