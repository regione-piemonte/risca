import { Gruppo, GruppoVo } from '../vo/gruppo-vo';
import { IstanzaProvvedimentoVo } from '../vo/istanza-provvedimento-vo';
import { RecapitoVo } from '../vo/recapito-vo';
import { SoggettoVo } from '../vo/soggetto-vo';
import { StatoRiscossioneVo } from './stati-riscossione-vo';
import { TipoAutorizzazioneVo } from './tipo-autorizzazioni-vo';

export interface IPraticaVo {
  /** Dalla tipologia della pratica; è il valore per: id_tipo_riscossione. */
  id_tipo_riscossione?: number;
  /** Id del componente; al momento viene gestito solo l'id: 1; per i dati tecnici. */
  id_componente_dt?: number;
  /** StatoRiscossioneVo che definisce lo stato della pratica. */
  stato_riscossione?: StatoRiscossioneVo;
  /** Data in formato: YYYY-MM-DD. */
  data_rinuncia_revoca?: string;
  /** SoggettoVo con i dati del soggetto associati alla pratica. */
  soggetto: SoggettoVo;
  /** GruppoVo contenente i dati del grupo. */
  gruppo_soggetto: GruppoVo;
  /** Dalla tipologia della pratica; è il valore per il campo procedimento: TipoAutorizzazioneVo. */
  tipo_autorizza?: TipoAutorizzazioneVo;
  /** Definisce; dal codice utente; il tipo di provincia. */
  cod_riscossione_prov?: string;
  /** Definisce il numero progressivo della pratica. */
  cod_riscossione_prog?: string;
  /** Data in formato: YYYY-MM-DD. */
  data_ini_concessione?: string;
  /** Data in formato: YYYY-MM-DD. */
  data_scad_concessione?: string;
  /** Data in formato: YYYY-MM-DD. */
  data_ini_sosp_canone?: string;
  /** Data in formato: YYYY-MM-DD. */
  data_fine_sosp_canone?: string;
  /** Definisce il numero della pratica. */
  num_pratica?: string;
  /** Number che definisce il valore booleano: 1 true; 0 false. */
  flg_prenotata?: number;
  /** */
  ambito?: string;
  /** Note che descrivono il motivo della prenotazione. */
  motivo_prenotazione?: string;
  /** Note della riscossione */
  note_riscossione?: string;
  /** Lista di oggetti IstanzaProvvedimentoVo. */
  provvedimentoIstanza?: IstanzaProvvedimentoVo[];
  /** Variabile ritornata dal server. */
  id_riscossione?: number;
  /** Array di any. */
  riscossione_recapito?: any[];
  /** Array di RecapitoVo contenente i recapiti del soggetto associati alla pratica. */
  recapiti_riscossione?: RecapitoVo[];
}

/**
 * @version SONARQUBE_22_04_24 Modificata vecchia verifica sui dati per l'assegnamento dei valori alle proprietà di classe.
 */
export class PraticaVo {
  /** Variabile ritornata dal server. */
  id_riscossione: number;
  /** Dalla tipologia della pratica; è il valore per: id_tipo_riscossione. */
  id_tipo_riscossione: number;
  /** StatoRiscossioneVo che definisce lo stato della pratica. */
  stato_riscossione: StatoRiscossioneVo;
  /** Data in formato: YYYY-MM-DD. */
  data_rinuncia_revoca?: string;
  /** SoggettoVo con i dati del soggetto associati alla pratica. */
  soggetto: SoggettoVo;
  /** GruppoVo contenente i dati del grupo. */
  gruppo_soggetto: GruppoVo | Gruppo;
  /** Array di RecapitoVo contenente i recapiti del soggetto associati alla pratica. */
  recapiti_riscossione: RecapitoVo[];
  /** Dalla tipologia della pratica; è il valore per il campo: procedimento. */
  tipo_autorizza: TipoAutorizzazioneVo;
  /** Id del componente; al momento viene gestito solo l'id: 1; per i dati tecnici. */
  id_componente_dt: number;
  /** Definisce, dal codice utente, il tipo di provincia. */
  cod_riscossione_prov: string;
  /** Definisce il numero progressivo della pratica. */
  cod_riscossione_prog: string;
  /** Data in formato: YYYY-MM-DD. */
  data_ini_concessione: string;
  /** Data in formato: YYYY-MM-DD. */
  data_scad_concessione: string;
  /** Data in formato: YYYY-MM-DD. */
  data_ini_sosp_canone: string;
  /** Data in formato: YYYY-MM-DD. */
  data_fine_sosp_canone: string;
  /** Definisce il numero della pratica. */
  num_pratica: string;
  /** Number che definisce il valore booleano: 1 true; 0 false. */
  flg_prenotata: number;
  /** */
  ambito: string;
  /** Note che descrivono il motivo della prenotazione. */
  motivo_prenotazione: string;
  /** Note della riscossione */
  note_riscossione: string;
  /** Lista di oggetti IstanzaProvvedimentoVo. */
  provvedimentoIstanza: IstanzaProvvedimentoVo[];
  /** Array di any. */
  riscossione_recapito: any[];

  constructor(c?: IPraticaVo) {
    // Definisco i dati
    this.setData(c);
  }

  setData(c: PraticaVo | IPraticaVo) {
    // Verifico l'input
    if (!c) {
      // No configuration
      return;
    }

    this.id_tipo_riscossione =
      c.id_tipo_riscossione ?? this.id_tipo_riscossione;
    this.id_componente_dt = c.id_componente_dt ?? this.id_componente_dt;
    this.stato_riscossione = c.stato_riscossione ?? this.stato_riscossione;
    this.data_rinuncia_revoca =
      c.data_rinuncia_revoca ?? this.data_rinuncia_revoca;
    this.soggetto = c.soggetto ?? this.soggetto;
    this.gruppo_soggetto = c.gruppo_soggetto ?? this.gruppo_soggetto;
    this.tipo_autorizza = c.tipo_autorizza ?? this.tipo_autorizza;
    this.cod_riscossione_prov =
      c.cod_riscossione_prov ?? this.cod_riscossione_prov;
    this.cod_riscossione_prog =
      c.cod_riscossione_prog ?? this.cod_riscossione_prog;
    this.data_ini_concessione =
      c.data_ini_concessione ?? this.data_ini_concessione;
    this.data_scad_concessione =
      c.data_scad_concessione ?? this.data_scad_concessione;
    this.data_ini_sosp_canone =
      c.data_ini_sosp_canone ?? this.data_ini_sosp_canone;
    this.data_fine_sosp_canone =
      c.data_fine_sosp_canone ?? this.data_fine_sosp_canone;
    this.num_pratica = c.num_pratica ?? this.num_pratica;
    this.flg_prenotata = c.flg_prenotata ?? this.flg_prenotata;
    this.ambito = c.ambito ?? this.ambito;
    this.motivo_prenotazione =
      c.motivo_prenotazione ?? this.motivo_prenotazione;
    this.note_riscossione = c.note_riscossione ?? this.note_riscossione;
    this.provvedimentoIstanza =
      c.provvedimentoIstanza ?? this.provvedimentoIstanza;
    this.id_riscossione = c.id_riscossione ?? this.id_riscossione;
    this.riscossione_recapito =
      c.riscossione_recapito ?? this.riscossione_recapito;
    this.recapiti_riscossione =
      c.recapiti_riscossione ?? this.recapiti_riscossione;
  }
}

/**
 * Classe che identifica i dati tecnici, nel dettaglio, di una riscossione per il salvataggio.
 */
export class PraticaDTDataVo {
  constructor(
    /** stringa alfanumerica */
    public gest_UID: string,
    /** string contenente un id numerico */
    public id_riscossione: number,
    /** string con data in formato YYYY-MM-GG */
    public data_modifica: string,
    /** string con data in formato YYYY-MM-GG */
    public data_inserimento: string,
    /** string contenente un un json object, da convertire */
    public dati_tecnici: string
  ) {}
}

/**
 * Classe che identifica i dati tecnici di una riscossione per il salvataggio.
 */
export class PraticaDTVo {
  constructor(public riscossione: PraticaDTDataVo) {}
}

/** Classe che definisce l'insieme dei dati della pratica e dei suoi dati tecnici. */
export class PraticaEDatiTecnici {
  constructor(public pratica: PraticaVo, public datiTecnici?: PraticaDTVo) {}
}
