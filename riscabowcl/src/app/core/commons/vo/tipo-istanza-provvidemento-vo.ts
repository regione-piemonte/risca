import { AmbitoVo } from "./ambito-vo";

export class TipoIstanzaProvvedimentoVo {
  constructor(
    public id_tipo_provvedimento: number,
    public cod_tipo_provvedimento: string,
    public des_tipo_provvedimento: string,
    public flg_istanza: string,
    public data_inizio_validita: string, // ISO Date
    public ambito: AmbitoVo,
    public ordina_tipo_provv: number, // Indice posizionale
    public flg_default: number // Number da considerare come boolean
  ) {}
}
