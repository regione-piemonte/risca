import { HelperVo } from './helper-vo';

export interface ITipoAccertamentoVo {
  id_tipo_accertamento: number;
  cod_tipo_accertamento: string;
  des_tipo_accertamento: string;
  flg_automatico: number;
  flg_manuale: number;
}

export class TipoAccertamentoVo extends HelperVo {
  id_tipo_accertamento: number;
  cod_tipo_accertamento: string;
  des_tipo_accertamento: string;
  flg_automatico: boolean;
  flg_manuale: boolean;

  constructor(iTRVo?: ITipoAccertamentoVo) {
    super();

    this.id_tipo_accertamento = iTRVo?.id_tipo_accertamento;
    this.cod_tipo_accertamento = iTRVo?.cod_tipo_accertamento;
    this.des_tipo_accertamento = iTRVo?.des_tipo_accertamento;
    this.flg_automatico = this.convertServerBoolNumToBoolean(iTRVo?.flg_automatico);
    this.flg_manuale = this.convertServerBoolNumToBoolean(iTRVo?.flg_manuale);
  }

  toServerFormat(): ITipoAccertamentoVo {
    // Definisco l'oggetto per il be
    const be: ITipoAccertamentoVo = {
      id_tipo_accertamento: this.id_tipo_accertamento,
      cod_tipo_accertamento: this.cod_tipo_accertamento,
      des_tipo_accertamento: this.des_tipo_accertamento,
      flg_automatico: this.convertBooleanToServerBoolNum(this.flg_automatico),
      flg_manuale: this.convertBooleanToServerBoolNum(this.flg_manuale),
    };

    // Ritorno l'oggetto per il be
    return be;
  }
}
