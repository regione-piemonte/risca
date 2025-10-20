import { AmbitoVo } from "./ambito-vo";

export class TipoAutorizzazioneVo {
  constructor(
    public id_tipo_autorizza: string,
    public cod_tipo_autorizza: string,
    public des_tipo_autorizza: string,
    public ambito: AmbitoVo,
    public ordina_tipo_autorizza: number, // Indice posizionale
    public flg_default: number // Number da considerare come boolean
  ) {}
}