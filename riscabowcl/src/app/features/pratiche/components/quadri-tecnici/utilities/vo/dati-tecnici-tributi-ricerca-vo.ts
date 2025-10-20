import { IUsoLeggeVo } from '../../../../../../core/commons/vo/uso-legge-vo';
import { DatiTecniciGeneraliAmbienteRicercaVo } from './dati-tecnici-ambiente-ricerca-vo';

/**
 * Classe che definisce la struttura dei dati tecnici uso per l'invio al server.
 * I dati non obbligatori string saranno "" per default.
 * I dati non obbligatori number saranno -1 per default.
 */
export class DatiTecniciUsoTributiRicercaVo {
  constructor(
    public id_tipo_uso_legge: number,
    public uso_di_legge: string,
    public popolazione: number
  ) {
    // Check for null
    if (this.popolazione === null) this.popolazione = -1;
  }
}

/**
 * Interfaccia che definisce la struttura dei dati tecnici usi per l'invio al server.
 */
export interface DatiTecniciUsiTributiRicercaVo {
  [key: string]: DatiTecniciUsoTributiRicercaVo;
}

/**
 * Classe che definisce la struttura dei dati tecnici per l'invio al server.
 */
export class DatiTecniciTributiVoRicerca {
  constructor(
    public dati_generali: DatiTecniciGeneraliAmbienteRicercaVo,
    public usi: DatiTecniciUsiTributiRicercaVo
  ) {}
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form datiTecniciTributiForm.
 */
export class DatiTecniciTributiRicerca {
  popolazione?: number;
  uso?: IUsoLeggeVo;
}
