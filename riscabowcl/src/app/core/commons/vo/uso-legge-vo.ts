import { Moment } from 'moment';
import { AmbitoVo } from './ambito-vo';
import { HelperVo } from './helper-vo';
import { IUnitaMisuraVo } from './unita-misura-vo';

export interface IUsoLeggeVo {
  id_tipo_uso?: number;
  id_ambito?: number;
  id_unita_misura?: number;
  id_accerta_bilancio?: number;
  cod_tipo_uso?: string;
  des_tipo_uso?: string;
  flg_uso_principale?: string;
  ordina_tipo_uso?: number;
  data_fine_validita?: string; // string formattata in iso date
  data_inizio_validita?: string; // string formattata in iso date
  flg_default?: number;

  ambito?: AmbitoVo;
  unita_misura?: IUnitaMisuraVo;
}

export class UsoLeggeVo extends HelperVo {
  id_tipo_uso?: number;
  id_ambito?: number;
  id_unita_misura?: number;
  id_accerta_bilancio?: number;
  cod_tipo_uso?: string;
  des_tipo_uso?: string;
  flg_uso_principale?: string;
  ordina_tipo_uso?: number;
  data_fine_validita?: Moment; // string formattata in iso date
  data_inizio_validita?: Moment; // string formattata in iso date
  flg_default?: number;
  ambito?: AmbitoVo;
  unita_misura?: IUnitaMisuraVo;

  constructor(iULVo?: IUsoLeggeVo) {
    super();

    this.id_tipo_uso = iULVo?.id_tipo_uso;
    this.id_ambito = iULVo?.id_ambito;
    this.id_unita_misura = iULVo?.id_unita_misura;
    this.id_accerta_bilancio = iULVo?.id_accerta_bilancio;
    this.cod_tipo_uso = iULVo?.cod_tipo_uso;
    this.des_tipo_uso = iULVo?.des_tipo_uso;
    this.flg_uso_principale = iULVo?.flg_uso_principale;
    this.ordina_tipo_uso = iULVo?.ordina_tipo_uso;
    this.data_fine_validita = this.convertServerDateToMoment(
      iULVo?.data_fine_validita
    );
    this.data_inizio_validita = this.convertServerDateToMoment(
      iULVo?.data_inizio_validita
    );
    this.flg_default = iULVo?.flg_default;
    this.ambito = iULVo?.ambito;
    this.unita_misura = iULVo?.unita_misura;
  }

  toServerFormat(): IUsoLeggeVo {
    // Definisco l'oggetto da ritornare al BE
    const be: IUsoLeggeVo = {
      id_tipo_uso: this.id_tipo_uso,
      id_ambito: this.id_ambito,
      id_unita_misura: this.id_unita_misura,
      id_accerta_bilancio: this.id_accerta_bilancio,
      cod_tipo_uso: this.cod_tipo_uso,
      des_tipo_uso: this.des_tipo_uso,
      flg_uso_principale: this.flg_uso_principale,
      ordina_tipo_uso: this.ordina_tipo_uso,
      data_fine_validita: this.convertMomentToServerDate(this.data_fine_validita),
      data_inizio_validita: this.convertMomentToServerDate(this.data_inizio_validita),
      flg_default: this.flg_default,
      ambito: this.ambito,
      unita_misura: this.unita_misura,
    };

    // Ritorno l'oggetto generato
    return be;
  }
}
