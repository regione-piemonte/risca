import { Moment } from 'moment';
import { DettaglioPagVo, IDettaglioPagVo } from './dettaglio-pagamento-vo';
import { HelperVo } from './helper-vo';

export interface IDettaglioPagSearchResultVo {
  cod_riscossione?: string;
  anno?: number;
  canone_dovuto?: number;
  importo_versato?: number;
  data_scadenza_pagamento?: string; // Formato server YYYY-MM-DD
  dettaglio_pag?: IDettaglioPagVo;
  interessi?: number;
}

export class DettaglioPagSearchResultVo extends HelperVo {
  cod_riscossione?: string;
  anno?: number;
  canone_dovuto?: number;
  importo_versato?: number;
  data_scadenza_pagamento?: Moment;
  dettaglio_pag?: DettaglioPagVo;
  interessi?: number;

  constructor(iDPSRVo?: IDettaglioPagSearchResultVo) {
    super();

    this.cod_riscossione = iDPSRVo?.cod_riscossione;
    this.anno = iDPSRVo?.anno;
    this.canone_dovuto = iDPSRVo?.canone_dovuto;
    this.importo_versato = iDPSRVo?.importo_versato;
    this.data_scadenza_pagamento = this.convertServerDateToMoment(
      iDPSRVo?.data_scadenza_pagamento
    );
    this.dettaglio_pag = new DettaglioPagVo(iDPSRVo?.dettaglio_pag);
    this.interessi = iDPSRVo?.interessi ?? 0;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IDettaglioPagSearchResultVo {
    // Definisco l'oggetto da ritornare al server
    const be: IDettaglioPagSearchResultVo = {
      cod_riscossione: this.cod_riscossione,
      anno: this.anno,
      canone_dovuto: this.canone_dovuto,
      importo_versato: this.importo_versato,
      data_scadenza_pagamento: this.convertMomentToServerDate(
        this.data_scadenza_pagamento
      ),
      dettaglio_pag: this.dettaglio_pag?.toServerFormat(),
    };

    // Ritorno l'oggetto per il be
    return be;
  }
}
