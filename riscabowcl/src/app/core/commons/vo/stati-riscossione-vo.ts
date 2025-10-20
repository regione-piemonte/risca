import { AmbitoVo } from './ambito-vo';

export class StatoRiscossioneVo {
  constructor(
    public id_stato_riscossione: number,
    public cod_stato_riscossione: string,
    public des_stato_riscossione: string,
    public ambito?: AmbitoVo
  ) {}
}
