import { HelperVo } from './helper-vo';
import { ITipoImpNonProprioVo } from './tipo-importo-non-proprio-vo';

export interface IPagamentoNonProprioVo {
  id_pag_non_propri?: number;
  id_pagamento: number;
  importo_versato: number;
  tipo_imp_non_propri: ITipoImpNonProprioVo;
}

export class PagamentoNonProprioVo extends HelperVo {
  id_pag_non_propri?: number;
  id_pagamento: number;
  importo_versato: number;
  tipo_imp_non_propri: ITipoImpNonProprioVo;

  constructor(iPNPVo?: IPagamentoNonProprioVo) {
    super();

    this.id_pag_non_propri = iPNPVo.id_pag_non_propri;
    this.id_pagamento = iPNPVo.id_pagamento;
    this.importo_versato = iPNPVo.importo_versato;
    this.tipo_imp_non_propri = iPNPVo.tipo_imp_non_propri;
  }

  /**
   * Funzione che converte le informazioni del dettaglio pagamento non proprio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IPagamentoNonProprioVo {
    // Definisco l'oggetto da ritornare al server
    const be: IPagamentoNonProprioVo = {
      id_pag_non_propri: this.id_pag_non_propri,
      id_pagamento: this.id_pagamento,
      importo_versato: this.importo_versato,
      tipo_imp_non_propri: this.tipo_imp_non_propri,
    };

    // Ritorno l'oggetto per il be
    return be;
  }
}
