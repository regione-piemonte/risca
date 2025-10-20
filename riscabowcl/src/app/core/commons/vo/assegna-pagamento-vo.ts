import { DettaglioPagVo, IDettaglioPagVo } from './dettaglio-pagamento-vo';
import { HelperVo } from './helper-vo';
import { IPagamentoVo, PagamentoVo } from './pagamento-vo';
import { IStatoDebitorioVo, StatoDebitorioVo } from './stato-debitorio-vo';

/**
 * Interfaccia che rappresenta l'oggetto per poter aggiornare un pagamento.
 */
export interface IAssegnaPagamento {
  pagamento: IPagamentoVo;
  stati_debitori?: IStatoDebitorioVo[];
  dettagli_pagamento?: IDettaglioPagVo[];
  dettagli_pagamento_da_cancellare?: IDettaglioPagVo[];
}

/**
 * Interfaccia che rappresenta l'oggetto per poter aggiornare un pagamento.
 */
export class AssegnaPagamento extends HelperVo {
  pagamento: PagamentoVo;
  statiDebitori: StatoDebitorioVo[];
  dettagliPagamento?: DettaglioPagVo[];
  dettagliPagamentoCancellare?: DettaglioPagVo[];

  constructor(iAP?: IAssegnaPagamento) {
    super();

    this.pagamento = new PagamentoVo(iAP?.pagamento);
    this.statiDebitori = this.convertiIStatiDebitoriVo(iAP?.stati_debitori);
    this.dettagliPagamento = this.convertiIDettaglioPagVo(
      iAP?.dettagli_pagamento
    );
    this.dettagliPagamentoCancellare = this.convertiIDettaglioPagVo(
      iAP?.dettagli_pagamento_da_cancellare
    );
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @returns IAssegnaPagamento.
   * @override
   */
  toServerFormat(): IAssegnaPagamento {
    // Converto le liste dati
    const sd = this.statiDebitori;
    const statiDebitori = this.listToServerFormat(sd) as IStatoDebitorioVo[];
    // #
    const dp = this.dettagliPagamento;
    const dettagliPagamento = this.listToServerFormat(dp) as IDettaglioPagVo[];
    // #
    const dpc = this.dettagliPagamentoCancellare;
    const dettagliPagamentoCancellare = this.listToServerFormat(
      dpc
    ) as IDettaglioPagVo[];

    // Costruisco l'oggetto di be
    const be: IAssegnaPagamento = {
      pagamento: this.pagamento.toServerFormat(),
      stati_debitori: statiDebitori,
      dettagli_pagamento: dettagliPagamento,
      dettagli_pagamento_da_cancellare: dettagliPagamentoCancellare,
    };

    // Ritorno l'oggetto
    return be;
  }

  /**
   * Funzione di conversione di una lista di stati debitori.
   * @param iSD IStatoDebitorioVo[] da convertire.
   * @returns StatoDebitorioVo[] convertiti.
   */
  convertiIStatiDebitoriVo(iSD: IStatoDebitorioVo[]): StatoDebitorioVo[] {
    // Rimappo le informazioni come oggeti StatoDebitorioVo
    return iSD?.map((iSd: IStatoDebitorioVo) => new StatoDebitorioVo(iSd));
  }

  /**
   * Funzione di conversione di una lista di stati debitori.
   * @param iDP IDettaglioPagVo[] da convertire.
   * @returns DettaglioPagVo[] convertiti.
   */
  convertiIDettaglioPagVo(iDP: IDettaglioPagVo[]): DettaglioPagVo[] {
    // Rimappo le informazioni come oggeti DettaglioPagVo
    return iDP?.map((iDp: IDettaglioPagVo) => new DettaglioPagVo(iDp));
  }
}
