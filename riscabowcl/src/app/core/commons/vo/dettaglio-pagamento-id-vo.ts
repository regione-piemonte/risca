import { HelperVo } from './helper-vo';

export interface IDettaglioPagamentoIdVo {
  id_pagamento?: number;
  id_dettaglio_pag?: number;
  id_rata_sd?: number;
  importo_versato?: number;
  interessi_maturati?: number;
}

export class DettaglioPagamentoIdVo extends HelperVo {
  id_pagamento?: number;
  id_dettaglio_pag?: number;
  id_rata_sd?: number;
  importo_versato?: number;
  interessi_maturati?: number;

  constructor(iDPVo?: IDettaglioPagamentoIdVo) {
    super();

    this.id_pagamento = iDPVo?.id_pagamento;
    this.id_dettaglio_pag = iDPVo?.id_dettaglio_pag;
    this.id_rata_sd = iDPVo?.id_rata_sd;
    this.importo_versato = iDPVo?.importo_versato;
    this.interessi_maturati = iDPVo?.interessi_maturati;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IDettaglioPagamentoIdVo {
    // Definisco l'oggetto da ritornare al server
    const be: IDettaglioPagamentoIdVo = {
      id_pagamento: this.id_pagamento,
      id_dettaglio_pag: this.id_dettaglio_pag,
      id_rata_sd: this.id_rata_sd,
      importo_versato: this.importo_versato,
      interessi_maturati: this.interessi_maturati,
    };

    // Ritorno l'oggetto per il be
    return be;
  }
}
