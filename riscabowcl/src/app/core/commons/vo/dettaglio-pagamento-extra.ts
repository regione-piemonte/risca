import { HelperVo } from './helper-vo';
import { IPagamentoVo, PagamentoVo } from './pagamento-vo';
import { IDettaglioPagamentoIdVo } from './dettaglio-pagamento-id-vo';

/**
 * Interfaccia che definisce le informazioni per dettaglio pagamento con l'aggiunta dati dell'oggetto Pagamento.
 */
export interface IDettaglioPagamentoExtraVo extends IDettaglioPagamentoIdVo {
  pagamento?: IPagamentoVo;
}

export class DettaglioPagamentoExtraVo extends HelperVo {
  id_pagamento?: number;
  id_dettaglio_pag?: number;
  id_rata_sd?: number;
  importo_versato?: number;
  interessi_maturati?: number;
  pagamento?: PagamentoVo;

  constructor(iDPExtraVo?: IDettaglioPagamentoExtraVo) {
    super();

    this.id_pagamento = iDPExtraVo?.id_pagamento;
    this.id_dettaglio_pag = iDPExtraVo?.id_dettaglio_pag;
    this.id_rata_sd = iDPExtraVo?.id_rata_sd;
    this.importo_versato = iDPExtraVo?.importo_versato;
    this.interessi_maturati = iDPExtraVo?.interessi_maturati;

    this.pagamento = new PagamentoVo(iDPExtraVo.pagamento);
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IDettaglioPagamentoIdVo {
    // Definisco l'oggetto da ritornare al server
    const be: IDettaglioPagamentoExtraVo = {
      id_pagamento: this.id_pagamento,
      id_dettaglio_pag: this.id_dettaglio_pag,
      id_rata_sd: this.id_rata_sd,
      importo_versato: this.importo_versato,
      interessi_maturati: this.interessi_maturati,
      pagamento: this.pagamento.toServerFormat(),
    };

    // Ritorno l'oggetto per il be
    return be;
  }
}
