import { RegioneVo } from './regione-vo';
import { NazioneVo } from './nazione-vo';

export class ProvinciaCompetenzaVo {
  constructor(
    public id_provincia: number,
    public cod_provincia: string,
    public denom_provincia: string,
    public sigla_provincia: string,
    public data_inizio_validita: Date,
    public regione: RegioneVo,
    public nazione: NazioneVo
  ) {}
}
