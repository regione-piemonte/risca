import { HelperVo } from './helper-vo';
import { IPagamentoVo, PagamentoVo } from './pagamento-vo';
import { IRataVo, RataVo } from './rata-vo';

export interface IDettaglioPagVo {
  id_dettaglio_pag: number;
  rata: IRataVo;
  pagamento: IPagamentoVo;
  importo_versato: number;
  interessi_maturati: number;
}

export class DettaglioPagVo extends HelperVo {
  id_dettaglio_pag: number;
  rata: RataVo;
  pagamento: PagamentoVo;
  importo_versato: number;
  interessi_maturati: number;

  constructor(iDPVo?: IDettaglioPagVo) {
    super();

    this.id_dettaglio_pag = iDPVo?.id_dettaglio_pag;
    this.rata = new RataVo(iDPVo?.rata);
    this.pagamento = new PagamentoVo(iDPVo?.pagamento);
    this.importo_versato = iDPVo?.importo_versato;
    this.interessi_maturati = iDPVo?.interessi_maturati;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IDettaglioPagVo {
    // Riconverto gli oggetti per il server
    const rata: IRataVo = this.rata?.toServerFormat() ?? null;
    const pagamento: IPagamentoVo = this.pagamento?.toServerFormat() ?? null;

    // Definisco l'oggetto da ritornare al server
    const be: IDettaglioPagVo = {
      id_dettaglio_pag: this.id_dettaglio_pag,
      rata,
      pagamento,
      importo_versato: this.importo_versato,
      interessi_maturati: this.interessi_maturati,
    };

    // Ritorno l'oggetto per il be
    return be;
  }
}

// #########################################

export interface IDettaglioPagListVo {
  rate_sd: IRataVo[];
  pagamento: IPagamentoVo;
}

export class DettaglioPagListVo extends HelperVo {
  rate_sd: RataVo[];
  pagamento: PagamentoVo;

  constructor(iDPLVo?: IDettaglioPagListVo) {
    super();

    this.rate_sd = this.convertRateVo(iDPLVo?.rate_sd);
    this.pagamento = new PagamentoVo(iDPLVo?.pagamento);
  }

  /**
   * Funzione di conversione dati da IRataVo a RataVo.
   * @param iReVo IRataVo[] da convertire.
   * @returns RataVo[] convertito.
   */
  convertRateVo(iReVo: IRataVo[]): RataVo[] {
    // Verifico l'input
    if (!iReVo) {
      // Niente da convertire
      return [];
    }

    // Converto tutti gli oggetti
    return iReVo.map((iRaVo: IRataVo) => {
      // Converto l'oggetto
      return new RataVo(iRaVo);
    });
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IDettaglioPagListVo {
    // Effettuo un parse per le liste
    const raSD = this.rate_sd as RataVo[];
    const rate = this.listToServerFormat(raSD);
    // Riconverto gli oggetti per il server
    const pagamento: IPagamentoVo = this.pagamento?.toServerFormat() ?? null;

    // Definisco l'oggetto da ritornare al server
    const be: IDettaglioPagListVo = {
      rate_sd: rate,
      pagamento,
    };

    // Ritorno l'oggetto per il be
    return be;
  }
}
