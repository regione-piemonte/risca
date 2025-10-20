import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import {
  IRiscaCheckboxData,
  ServerStringAsBoolean,
} from '../../../../../../shared/utilities';
import { IUnitaMisuraVo } from '../../../../../../core/commons/vo/unita-misura-vo';
import { IUsoLeggeSpecificoVo } from '../../../../../../core/commons/vo/uso-legge-specifico-vo';
import { IUsoLeggeVo } from '../../../../../../core/commons/vo/uso-legge-vo';

/**
 * Classe che definisce la struttura dei dati tecnici generali per l'invio al server.
 * I dati non obbligatori string saranno "" per default.
 * I dati non obbligatori number saranno -1 per default.
 */
export class DatiTecniciGeneraliAmbienteRicercaVo {
  constructor(
    public corpo_idrico_captazione: string,
    public comune: string,
    public nome_impianto_idrico: string,
    public portata_da_assegnare: number = -1,
    public gestione_manuale: ServerStringAsBoolean
  ) {
    // Check for null
    if (this.portata_da_assegnare === null) this.portata_da_assegnare = -1;
  }
}

/**
 * Classe che definisce la struttura dei dati tecnici riduzione per l'invio al server.
 * I dati non obbligatori string saranno "" per default.
 * I dati non obbligatori number saranno -1 per default.
 */
export class DatiTecniciRiduzioneRicercaVo {
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
export class DatiTecniciAumentoRicercaVo {
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
export class DatiTecniciUsoRicercaVo {
  constructor(
    public id_tipo_uso_legge: number,
    public uso_di_legge: string,
    public uso_specifico: string[] = [],
    public unita_di_misura: string,
    public qta_acqua_da: number,
    public qta_acqua_a: number,
    public qta_falda_profonda: number = -1,
    public perc_falda_profonda: number = -1,
    public qta_falda_superficie: number = -1,
    public perc_falda_superficie: number = -1,
    public riduzione: DatiTecniciRiduzioneRicercaVo[] = [],
    public aumento: DatiTecniciAumentoRicercaVo[] = [],
    public data_scadenza_emas_iso_da: string,
    public data_scadenza_emas_iso_a: string
  ) {
    // Check for null
    if (this.uso_specifico === null) this.uso_specifico = [];
    if (this.qta_falda_profonda === null) this.qta_falda_profonda = -1;
    if (this.perc_falda_profonda === null) this.perc_falda_profonda = -1;
    if (this.qta_falda_superficie === null) this.qta_falda_superficie = -1;
    if (this.perc_falda_superficie === null) this.perc_falda_superficie = -1;
    if (this.riduzione === null) this.riduzione = [];
    if (this.aumento === null) this.aumento = [];
  }
}

/**
 * Interfaccia che definisce la struttura dei dati tecnici usi per l'invio al server.
 */
export interface DatiTecniciUsiRicercaVo {
  [key: string]: DatiTecniciUsoRicercaVo;
}

/**
 * Interfaccia che definisce la struttura dei dati tecnici usi per l'invio al server.
 */
export interface DatiTecniciTributoVoRicerca {
  [key: string]: {
    popolazione: number;
    id_tipo_uso_legge: number;
    uso_di_legge: string;
  };
}

/**
 * Classe che definisce la struttura dei dati tecnici per l'invio al server.
 */
export class DatiTecniciVoRicerca {
  constructor(
    public dati_generali: DatiTecniciGeneraliAmbienteRicercaVo,
    public usi: DatiTecniciUsiRicercaVo
  ) {}
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form datiTecniciAmbienteForm.
 */
export class DatiTecniciAmbienteRicerca {
  comune?: string;
  corpoIdricoCaptazione?: string;
  gestioneManuale?: IRiscaCheckboxData;
  nomeImpiantoIdroElettrico?: string;
  portataDaAssegnare?: number;
  usiDiLegge?: UsoDiLeggeInfoRicerca[];
}

/**
 * Interfaccia personalizzata che definisce la struttura dei dati uso di legge che utilizzano le form di ricerca pratiche sui dati tecnici ambiente.
 */
export interface UsoDiLeggeInfoRicerca {
  usoDiLegge?: IUsoLeggeVo;
  usiDiLeggeSpecifici?: IUsoLeggeSpecificoVo[];
  unitaDiMisura?: IUnitaMisuraVo;
  quantitaDa?: number;
  quantitaA?: number;
  dataScadenzaEmasIsoDa?: NgbDateStruct;
  dataScadenzaEmasIsoA?: NgbDateStruct;
}
