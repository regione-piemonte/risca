import { PagamentoVo } from '../../../../../core/commons/vo/pagamento-vo';

/**
 * Interface con l'insieme delle informazioni per i pagamenti da visionare.
 */
export interface IPagamentiDaVisionareInfo {
  pagamentiDV: PagamentoVo[];
  pagamentoDV: PagamentoVo;
}
