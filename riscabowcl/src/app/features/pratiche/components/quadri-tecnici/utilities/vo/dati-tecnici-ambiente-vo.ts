import { ServerStringAsBoolean } from '../../../../../../shared/utilities';

/**
 * Classe che definisce la struttura dei dati tecnici generali per l'invio al server.
 * I dati non obbligatori string saranno "" per default.
 * I dati non obbligatori number saranno -1 per default.
 */
export class DatiTecniciGeneraliAmbienteVo {
  constructor(
    public corpo_idrico_captazione: string,
    public comune: string,
    public nome_impianto_idrico: string,
    public portata_da_assegnare: number = -1,
    public gestione_manuale: ServerStringAsBoolean
  ) {
    // Check for null
    if (this.corpo_idrico_captazione == '') {
      this.corpo_idrico_captazione = null;
    }
    if (this.comune == '') {
      this.comune = null;
    }
    if (this.nome_impianto_idrico == '') {
      this.nome_impianto_idrico = null;
    }
  }
}

/**
 * Classe che definisce la struttura dei dati tecnici riduzione per l'invio al server.
 * I dati non obbligatori string saranno "" per default.
 * I dati non obbligatori number saranno -1 per default.
 */
export class DatiTecniciRiduzioneVo {
  constructor(
    public id_riduzione: number,
    public motivazione: string,
    public perc_riduzione_motiv: number
  ) {}
}

/**
 * Classe che definisce la struttura dei dati tecnici aumento per l'invio al server.
 * I dati non obbligatori string saranno "" per default.
 * I dati non obbligatori number saranno -1 per default.
 */
export class DatiTecniciAumentoVo {
  constructor(
    public id_aumento: number,
    public motivazione: string,
    public perc_aumento_motiv: number
  ) {}
}

/**
 * Classe che definisce la struttura dei dati tecnici uso per l'invio al server.
 * I dati non obbligatori string saranno "" per default.
 * I dati non obbligatori number saranno -1 per default.
 */
export class DatiTecniciUsoAmbienteVo {
  constructor(
    public id_tipo_uso_legge: number,
    public uso_di_legge: string,
    public uso_specifico: string[] = [],
    public unita_di_misura: string,
    public qta_acqua: number = -1,
    public qta_falda_profonda: number = -1,
    public perc_falda_profonda: number = -1,
    public qta_falda_superficie: number = -1,
    public perc_falda_superficie: number = -1,
    public riduzione: DatiTecniciRiduzioneVo[] = [],
    public aumento: DatiTecniciAumentoVo[] = [],
    public data_scadenza_emas_iso: string
  ) {
    // Check for null
    if (this.uso_specifico === null) {
      this.uso_specifico = [];
    }
    if (this.qta_falda_profonda === null) {
      this.qta_falda_profonda = -1;
    }
    if (this.perc_falda_profonda === null) {
      this.perc_falda_profonda = -1;
    }
    if (this.qta_falda_superficie === null) {
      this.qta_falda_superficie = -1;
    }
    if (this.perc_falda_superficie === null) {
      this.perc_falda_superficie = -1;
    }
    if (this.riduzione === null) {
      this.riduzione = [];
    }
    if (this.aumento === null) {
      this.aumento = [];
    }
  }
}

/**
 * Interfaccia che definisce la struttura dei dati tecnici usi per l'invio al server.
 */
export interface DatiTecniciUsiAmbienteVo {
  [key: string]: DatiTecniciUsoAmbienteVo;
}

/**
 * Classe che definisce la struttura dei dati tecnici per l'invio al server.
 */
export class DatiTecniciAmbienteVo {
  constructor(
    public dati_generali: DatiTecniciGeneraliAmbienteVo,
    public usi: DatiTecniciUsiAmbienteVo
  ) {}
}
