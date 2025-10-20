import { AmbitoVo } from "./ambito-vo";

export class TipoTitoloVo {
  constructor(
    public id_tipo_titolo: number,
    public cod_tipo_titolo: string,
    public des_tipo_titolo: string,
    public data_inizio_validita: string, // ISO Date
    public ambito: AmbitoVo,
    public ordina_tipo_titolo: number, // Indice posizionale
    public flg_default: number // Number da considerare come boolean
  ) {}
}
