import { Istanza } from 'src/app/shared/models';
import { TipologiaOggetto } from './tipologia-oggetto.model';
import { UbicazioneOggettoIstanza } from './ubicazione-oggetto-istanza.model';

// OggettoIstanzaExtendedDTO
export interface OggettoIstanza {
  id_oggetto_istanza?: number;
  gestUID?: string;
  id_oggetto?: number;
  istanza?: Istanza;
  tipologia_oggetto?: TipologiaOggetto;
  ind_geo_stato?: string;
  cod_sira?: number;
  cod_scriva?: number;
  den_oggetto?: string;
  des_oggetto?: string;
  coordinata_x?: number;
  coordinata_y?: number;
  flg_esistente?: boolean;
  id_masterdata_origine?: number;
  id_masterdata?: number;
  ubicazione_oggetto?: UbicazioneOggettoIstanza[];
}
