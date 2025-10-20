export interface IIuvVo {
  id_iuv: number;
  nap: string;
  stato_iuv: IStatoIuvVo;
  iuv: string;
  codice_avviso: string;
  importo: number;
  codice_versamento: string;
}

export class IuvVo {
  id_iuv: number;
  nap: string;
  stato_iuv: StatoIuvVo;
  iuv: string;
  codice_avviso: string;
  importo: number;
  codice_versamento: string;

  constructor(iIuvVo?: IIuvVo) {
    this.id_iuv = iIuvVo?.id_iuv;
    this.nap = iIuvVo?.nap;
    this.stato_iuv = iIuvVo?.stato_iuv;
    this.iuv = iIuvVo?.iuv;
    this.codice_avviso = iIuvVo?.codice_avviso;
    this.importo = iIuvVo?.importo;
    this.codice_versamento = iIuvVo?.codice_versamento;
  }
}

export interface IStatoIuvVo {
  id_stato_iuv: number;
  cod_stato_iuv: string;
  des_stato_iuv: string;
}

export class StatoIuvVo {
  id_stato_iuv: number;
  cod_stato_iuv: string;
  des_stato_iuv: string;

  constructor(iSIuvVo?: IStatoIuvVo) {
    this.id_stato_iuv = iSIuvVo?.id_stato_iuv;
    this.cod_stato_iuv = iSIuvVo?.cod_stato_iuv;
    this.des_stato_iuv = iSIuvVo?.des_stato_iuv;
  }
}
