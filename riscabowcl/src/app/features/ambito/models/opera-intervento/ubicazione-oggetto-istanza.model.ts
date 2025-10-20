import { ComuneVo } from '../../../../core/commons/vo/comune-vo';

export interface UbicazioneOggettoIstanza {
  id_ubicazione_oggetto_istanza?: number;
  gestUID?: string;
  id_oggetto_istanza?: number;
  comune?: ComuneVo;
  den_indirizzo?: string;
  num_civico?: string;
  des_localita?: string;
  ind_geo_provenienza?: string;
}
