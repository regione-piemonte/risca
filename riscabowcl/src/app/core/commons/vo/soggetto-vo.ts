import {
  TipoNaturaGiuridica,
  TipoSoggettoVo,
} from '../../../features/ambito/models';
import { ComuneVo } from './comune-vo';
import { FonteVo } from './fonte-vo';
import { Gruppo, GruppoVo } from './gruppo-vo';
import { NazioneVo } from './nazione-vo';
import { RecapitoVo } from './recapito-vo';

/**
 * Classe che definisce la struttura di un soggetto ritornato dal server.
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
export class SoggettoVo {
  id_soggetto?: number;
  id_ambito?: number;
  // ambito?: AmbitoVo;
  cf_soggetto?: string;
  tipo_soggetto?: TipoSoggettoVo;
  tipo_natura_giuridica?: TipoNaturaGiuridica;
  recapiti?: RecapitoVo[];
  gruppo_soggetto?: GruppoVo[] | Gruppo[];
  fonte?: FonteVo;
  fonte_origine?: FonteVo;
  nome?: string;
  cognome?: string;
  den_soggetto?: string;
  partita_iva_soggetto?: string;
  data_nascita_soggetto?: string;
  comune_nascita?: ComuneVo;
  nazione_nascita?: NazioneVo;
  citta_estera_nascita?: string;
}
