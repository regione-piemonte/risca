import { TipoIstanzaProvvedimentoVo } from './tipo-istanza-provvidemento-vo';
import { TipoTitoloVo } from 'src/app/core/commons/vo/tipo-titolo-vo';

export interface IIstanzaProvvedimentoVo {
  id_provvedimento?: number;
  id_tipo_provvedimento?: TipoIstanzaProvvedimentoVo;
  data_provvedimento?: string;
  id_tipo_titolo?: TipoTitoloVo;
  num_titolo?: string;
  note?: string;
  id_riscossione?: number;
}

export class IstanzaProvvedimentoVo {
  id_provvedimento: number;
  id_tipo_provvedimento: TipoIstanzaProvvedimentoVo;
  data_provvedimento: string;
  id_tipo_titolo: TipoTitoloVo;
  num_titolo: string;
  note: string;
  id_riscossione?: number;

  constructor(iIstProvVo?: IIstanzaProvvedimentoVo) {
    this.id_provvedimento = iIstProvVo?.id_provvedimento;
    this.id_tipo_provvedimento = iIstProvVo?.id_tipo_provvedimento;
    this.data_provvedimento = iIstProvVo?.data_provvedimento;
    this.id_tipo_titolo = iIstProvVo?.id_tipo_titolo;
    this.num_titolo = iIstProvVo?.num_titolo;
    this.note = iIstProvVo?.note;
    this.id_riscossione = iIstProvVo?.id_riscossione;
  }
}
