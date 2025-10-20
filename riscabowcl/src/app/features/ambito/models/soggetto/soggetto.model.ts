import { ComuneVo } from '../../../../core/commons/vo/comune-vo';
import { TipoNaturaGiuridica } from './tipo-natura-giuridica.model';
import { TipoSoggettoVo } from './tipo-soggetto.model';

export interface Soggetto {
  // persona giuridica
  tipo_natura_giuridica?: TipoNaturaGiuridica;
  den_soggetto?: string;
  den_provincia_cciaa?: string;
  den_anno_cciaa?: number;
  den_numero_cciaa?: string;
  comune_sede_legale?: ComuneVo;
  partita_iva_soggetto?: string;

  // persona fisica
  cognome?: string;
  nome?: string;

  comune_residenza?: ComuneVo;
  comune_nascita?: ComuneVo;
  data_nascita_soggetto?: string;

  citta_estera_residenza?: string;
  citta_estera_nascita?: string;
  stato_estero_nascita?: string;

  // npo e npo
  tipo_soggetto: TipoSoggettoVo;
  gestUID?: string;
  id_soggetto?: number;
  cf_soggetto: string;
  num_telefono?: string;
  data_cessazione_soggetto?: string;
  des_email?: string;
  des_pec?: string;
  indirizzo_soggetto?: string;
  num_civico_indirizzo?: string;

  id_masterdata?: number;
  id_masterdata_origine?: number;
}
