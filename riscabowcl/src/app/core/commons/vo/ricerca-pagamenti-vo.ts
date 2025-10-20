import { TipoModalitaPagamentoVo } from './tipo-modalita-pagamento-vo';
import { TipoRicercaPagamentoVo } from './tipo-ricerca-pagamento-vo';

/**
 * Classe che racchiude le informazioni per la ricerca pagamenti da utilizzare per i query param di ricerca.
 */
export class IRicercaPagamentiVo {
  stato_pagamento?: TipoRicercaPagamentoVo;
  modalita_pagamento?: TipoModalitaPagamentoVo;
  data_op_da?: string;
  data_op_a?: string;
  quinto_campo?: string;
  codice_avviso?: string;
  cro?: string;
  importo_da?: number;
  importo_a?: number;
  soggetto_versamento?: string;
}
