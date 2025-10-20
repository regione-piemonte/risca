import { ProvinciaVo } from './provincia-vo';

export interface IComuneVo {
  cap_comune: string;
  cod_belfiore_comune: string;
  cod_istat_comune: string;
  data_inizio_validita: string;
  data_fine_validita?: string;
  denom_comune: string;
  dt_id_comune: number;
  dt_id_comune_next: number;
  dt_id_comune_prev: number;
  flg_attivo?: number;
  id_comune: number;
  id_provincia: number;
  provincia?: ProvinciaVo;
}

export class ComuneVo {
  cap_comune: string;
  cod_belfiore_comune: string;
  cod_istat_comune: string;
  data_inizio_validita: Date;
  data_fine_validita?: Date;
  denom_comune: string;
  dt_id_comune: number;
  dt_id_comune_next: number;
  dt_id_comune_prev: number;
  flg_attivo?: number;
  id_comune: number;
  id_provincia: number;
  provincia?: ProvinciaVo;
}

/*
  cap_comune: "00000"
  cod_belfiore_comune: "F205"
  cod_istat_comune: "015146"
  data_inizio_validita: "2006-09-20T00:00:00.000+0000"
  denom_comune: "MILANO"
  dt_id_comune: 12865
  dt_id_comune_next: 0
  dt_id_comune_prev: 3976
  id_comune: 3703
  id_provincia: 15
*/
