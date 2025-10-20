import { AmbitoVo } from './ambito-vo';

export class TipoRiscossioneVo {
  constructor(
    public id_tipo_riscossione: number,
    public cod_tipo_riscossione: string,
    public des_tipo_riscossione: string,
    public ambito: AmbitoVo,
    public ordina_tipo_risco: number, // Indice posizionale
    public flg_default: number, // Number da considerare come boolean
  ) {}
}
