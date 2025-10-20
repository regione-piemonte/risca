import { Observable } from 'rxjs';
import { DettaglioPagSearchResultVo } from '../../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { PagamentoVo } from '../../../../../core/commons/vo/pagamento-vo';

/**
 * Interfaccia custom che racchiude le informazioni per la modifica di un pagamento.
 */
export interface IDettagliPagamentoPerModifica {
  pagamento: PagamentoVo;
  dettagliPagamentoSearch: DettaglioPagSearchResultVo[];
}

/**
 * Interfaccia che rappresenta la request specifica per aggiornare un pgamento e cancellare i dettagli pagamento.
 */
export interface IFullUpdatePagamentoReq {
  pagamento: Observable<PagamentoVo>;
  dettagliPagCancellati?: Observable<number[]>;
}

/**
 * Interfaccia che rappresenta la response specifica per aggiornare un pgamento e cancellare i dettagli pagamento.
 */
export interface IFullUpdatePagamentoRes {
  pagamento: PagamentoVo;
  dettagliPagCancellati?: number[];
}
