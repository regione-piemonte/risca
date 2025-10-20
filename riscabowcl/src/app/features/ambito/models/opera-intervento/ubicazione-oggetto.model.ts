import { ComuneVo } from '../../../../core/commons/vo/comune-vo';

// UbicazioneOggettoExtendedDTO
export interface UbicazioneOggetto {
  id_ubicazione_oggetto?: number;
  gestUID?: string;
  id_oggetto?: number;
  comune?: ComuneVo;
  den_indirizzo?: string;
  num_civico?: string;
  des_localita?: string;
  ind_geo_provenienza?: string;
}
