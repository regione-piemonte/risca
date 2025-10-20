import { RegioneVo } from './regione-vo';

export interface ProvinciaVo {
  regione: RegioneVo;
  cod_provincia: string;
  data_inizio_validita: Date;
  denom_provincia: string;
  id_provincia: number;
  sigla_provincia: string;
}

/*
  Regione: {id_regione: 6, cod_regione: '06', denom_regione: 'FRIULI-VENEZIA GIULIA', data_inizio_validita: '1866-11-19T00:00:00.000+0000', NazioneVo: {…}}
  cod_provincia: "030"
  data_inizio_validita: "1866-11-19T00:00:00.000+0000"
  denom_provincia: "UDINE"
  id_provincia: 30
  sigla_provincia: "UD"
*/
