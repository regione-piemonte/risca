import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

/**
 * Interfaccia di comodo che definisce i dati della form per il pagamento manuale.
 */
export interface IPagamentoManualeForm {
  id_pagamento?: number;
  data_op_val?: NgbDateStruct;
  importo_versato?: number;
  tipo_modalita_pag?: string;
  note?: string;
}
