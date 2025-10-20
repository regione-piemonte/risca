/**
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
export class IndirizzoSpedizioneVo {
  id_recapito?: number;
  id_recapito_postel?: number;
  id_gruppo_soggetto?: number;
  destinatario_postel?: string;
  presso_postel?: string;
  indirizzo_postel?: string;
  citta_postel?: string;
  provincia_postel?: string;
  cap_postel?: string;
  frazione_postel?: string;
  nazione_postel?: string;
  // Number che identifica un booleanno => 1 : true - 0 : false;
  ind_valido_postel?: number;
}