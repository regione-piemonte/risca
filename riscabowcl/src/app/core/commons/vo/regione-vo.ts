import { NazioneVo } from './nazione-vo';

export interface RegioneVo {
  nazione?: NazioneVo;
  cod_regione: string;
  data_inizio_validita: Date;
  denom_regione: string;
  id_regione: number;
}

/*
  NazioneVo: {id_nazione: 1, cod_istat_nazione: '100', denom_nazione: 'ITALIA', data_inizio_validita: '1861-01-01T00:00:00.000+0000', dt_id_stato: 418, …}
  cod_regione: "19"
  data_inizio_validita: "1861-01-01T00:00:00.000+0000"
  denom_regione: "SICILIA"
  id_regione: 19
*/
