import { DatiTecniciGeneraliAmbienteVo } from './dati-tecnici-ambiente-vo';

/**
 * Classe che definisce la struttura dei dati tecnici uso per l'invio al server.
 * I dati non obbligatori string saranno "" per default.
 * I dati non obbligatori number saranno -1 per default.
 */
export class DatiTecniciUsoTributoVo {
  constructor(
    public id_tipo_uso_legge: number,
    public uso_di_legge: string,
    public popolazione: number
  ) {}
}

/**
 * Interfaccia che definisce la struttura dei dati tecnici usi per l'invio al server.
 */
export interface DatiTecniciUsiTributiVo {
  [key: string]: DatiTecniciUsoTributoVo;
}

/**
 * Classe che definisce la struttura dei dati tecnici per l'invio al server.
 */
export class DatiTecniciTributiVo {
  constructor(
    public dati_generali: DatiTecniciGeneraliAmbienteVo,
    public usi: DatiTecniciUsiTributiVo
  ) {}
}
