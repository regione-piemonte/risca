import { Moment } from 'moment';
import { HelperVo } from './helper-vo';

export interface IRataVo {
  id_rata_sd?: number;
  id_stato_debitorio?: number;
  id_rata_sd_padre?: number;
  canone_dovuto?: number;
  data_scadenza_pagamento?: string; // String che definisce la data in formato server
  interessi_maturati?: number;
  // dettaglio_pag?: IDettaglioPagamentoVo[];
}

export class RataVo extends HelperVo {
  id_rata_sd?: number;
  id_stato_debitorio?: number;
  id_rata_sd_padre?: number;
  canone_dovuto?: number;
  interessi_maturati?: number;
  data_scadenza_pagamento?: Moment; // String che definisce la data in formato server
  // dettaglio_pag?: DettaglioPagamentoVo[];

  constructor(iRataVo?: IRataVo) {
    super();

    this.id_rata_sd = iRataVo?.id_rata_sd;
    this.id_stato_debitorio = iRataVo?.id_stato_debitorio;
    this.id_rata_sd_padre = iRataVo?.id_rata_sd_padre;
    this.canone_dovuto = iRataVo?.canone_dovuto;
    this.interessi_maturati = iRataVo?.interessi_maturati;
    this.data_scadenza_pagamento = this.convertServerDateToMoment(
      iRataVo?.data_scadenza_pagamento
    );
    // this.dettaglio_pag = this.convertIDettagliPagamentiVo(iRataVo?.dettaglio_pag);
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IRataVo {
    // Conversione delle date
    const data_scadenza_pagamento = this.convertMomentToServerDate(
      this.data_scadenza_pagamento as Moment
    );

    // Effettuo un parse per le liste
    // const dPSD = this.dettaglio_pag as DettaglioPagamentoVo[];
    // const dettagliopag = this.listToServerFormat(dPSD);

    const be: IRataVo = {
      id_rata_sd: this.id_rata_sd,
      id_stato_debitorio: this.id_stato_debitorio,
      id_rata_sd_padre: this.id_rata_sd_padre,
      canone_dovuto: this.canone_dovuto,
      interessi_maturati: this.interessi_maturati,
      data_scadenza_pagamento,
      // dettaglio_pag: dettagliopag,
    };

    // Ritorno l'oggetto per il server
    return be;
  }

  // /**
  //  * Funzione di conversione dati da IDettaglioPagamentoVo a DettaglioPagamentoVo.
  //  * @param iDPiVo IDettaglioPagamentoVo[] da convertire.
  //  * @returns DettaglioPagamentoVo[] convertito.
  //  */
  // convertIDettagliPagamentiVo(
  //   iDPiVo: IDettaglioPagamentoVo[]
  // ): DettaglioPagamentoVo[] {
  //   // Verifico l'input
  //   if (!iDPiVo) {
  //     // Niente da convertire
  //     return undefined;
  //   }

  //   // Converto tutti gli oggetti
  //   return iDPiVo.map((iDPVo: IDettaglioPagamentoVo) => {
  //     // Converto l'oggetto
  //     return new DettaglioPagamentoVo(iDPVo);
  //   });
  // }
}
