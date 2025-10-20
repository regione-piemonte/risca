import { PagamentoVo } from 'src/app/core/commons/vo/pagamento-vo';
import { DettaglioPagSearchResultVo } from '../../../../../core/commons/vo/dettaglio-pag-search-result-vo';

/**
 * Interfaccia che definisce i parametri da passare alla modale.
 */
export interface IVersamentoConfigs {
  /** PagamentoVo con le informazioni per la gestione della modale. */
  pagamento?: PagamentoVo;
  /** DettaglioPagSearchResultVo[] cone le informazionia ggiuntive per la gestione del pagamento nella modale. */
  dettagliPagamentoSearch: DettaglioPagSearchResultVo[];
}

/**
 * Interfaccia che definisce le informazioni ritornate dalla modale a seguito della conferma pagamento.
 */
export interface IConfirmModificaPag {
  pagamento: PagamentoVo;
  dettagliPagCancellati?: DettaglioPagSearchResultVo[];
}
