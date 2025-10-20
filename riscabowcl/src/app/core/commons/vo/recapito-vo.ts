import { TipoInvio } from '../../../shared/models/contatti/tipo-invio.model';
import { ComuneVo } from './comune-vo';
import { IndirizzoSpedizioneVo } from './indirizzo-spedizione-vo';
import { NazioneVo } from './nazione-vo';
import { TipoRecapitoVo } from './tipo-recapito-vo';
import { TipoSedeVo } from './tipo-sede-vo';

/**
 * @version SONARQUBE_22_04_24 Modificata vecchia verifica sui dati per l'assegnamento dei valori alle proprietà di classe.
 */
export class RecapitoVo {
  // Proprietà di comodo definita da fe quando id_recapito non è stato ancora generato
  __id?: string;
  // Proprietà di comodo che gestisce l'aggiunta di un recapito alternativo nella scheda dei dati anagrafici della pratica. Questa proprietà server per poter sapere quale nuovo recapito alternativo è stato impostato per la pratica e aggiornare il dato con le informazioni create su DB
  _risca_id_recapito?: string;

  fonte?: any;
  id_recapito?: number;
  id_soggetto?: number;
  id_fonte_origine?: number;
  cod_recapito?: string;
  indirizzo?: string;
  num_civico?: string;
  presso?: string;
  citta_estera_recapito?: string;
  cap_recapito?: string;
  des_localita?: string;
  tipo_sede?: TipoSedeVo;
  tipo_recapito?: TipoRecapitoVo;
  comune_recapito?: ComuneVo;
  nazione_recapito?: NazioneVo;

  tipo_invio?: TipoInvio;
  pec?: string;
  email?: string;
  telefono?: string;
  cellulare?: string;

  indirizzi_spedizione?: IndirizzoSpedizioneVo[] = [];

  constructor(r?: RecapitoVo) {
    // Verifico l'input
    if (!r) {
      // Niente configurazione
      return;
    }

    this.fonte = r.fonte;
    this.id_recapito = r.id_recapito;
    this.id_soggetto = r.id_soggetto;
    this.id_fonte_origine = r.id_fonte_origine;
    this.cod_recapito = r.cod_recapito;
    this.indirizzo = r.indirizzo;
    this.num_civico = r.num_civico;
    this.presso = r.presso;
    this.citta_estera_recapito = r.citta_estera_recapito;
    this.cap_recapito = r.cap_recapito;
    this.des_localita = r.des_localita;
    this.tipo_sede = r.tipo_sede;
    this.tipo_recapito = r.tipo_recapito;
    this.comune_recapito = r.comune_recapito;
    this.nazione_recapito = r.nazione_recapito;
    this.tipo_invio = r.tipo_invio;
    this.pec = r.pec;
    this.email = r.email;
    this.telefono = r.telefono;
    this.cellulare = r.cellulare;
    this.indirizzi_spedizione = r.indirizzi_spedizione ?? [];
  }
}
