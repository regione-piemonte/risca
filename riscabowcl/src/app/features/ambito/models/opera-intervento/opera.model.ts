import { StatoOggetto } from './stato-oggetto.model';
import { TipologiaOggetto } from './tipologia-oggetto.model';
import { UbicazioneOggetto } from './ubicazione-oggetto.model';

// OggettoUbicazioneExtendedDTO
export interface Opera {
  id_oggetto?: number;
  gestUID?: string;
  tipologia_oggetto?: TipologiaOggetto;
  stato_oggetto?: StatoOggetto;
  cod_scriva?: number;
  cod_sira?: number;
  den_oggetto?: string;
  des_oggetto?: string;
  coordinata_x?: number;
  coordinata_y?: number;
  flg_esistente?: boolean;
  id_masterdata_origine?: number;
  id_masterdata?: number;
  ubicazione_oggetto?: UbicazioneOggetto[];
}
