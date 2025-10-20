/**
 * Classe che definisce la struttura di un DocumentoAllegato ritornato dal server.
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
export class DocumentoAllegatoVo {
  id_classificazione: string;
  db_key_classificazione: string;
  entrata_uscita?: string;
  protocollo_regionale?: string;
  protocollo_mittente?: string;
  descrizione?: string;
  visibilita: string;
  data_inserimento?: string;
  rappresentazione_digitale: boolean;
  doc_con_allegati: boolean;
  valido: boolean;
}

/**
 * Classe che definisce la struttura di un Documento ritornato dal server.
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
export class AllegatoVo {
  id_classificazione: string;
	descrizione: string;
	rappresentazione_digitale: boolean;
	doc_con_allegati: boolean;
}

/**
 * Classe che definisce la struttura di un Allegato ritornato dal server.
 */
 export class FileACTAVo {
  length: number;
  mimeType: string;
  filename: string;
  streamMTOM: string;
}