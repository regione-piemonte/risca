import { HelperVo } from './helper-vo';

export interface IRimborsoUtilizzatoVo {
  id_stato_debitorio: number;
  id_rimborso: number;
  imp_utilizzato: number;
}

export class RimborsoUtilizzatoVo extends HelperVo {
  id_stato_debitorio: number;
  id_rimborso: number;
  imp_utilizzato: number;

  constructor(iRUVo?: IRimborsoUtilizzatoVo) {
    super();

    this.id_stato_debitorio = iRUVo?.id_stato_debitorio;
    this.id_rimborso = iRUVo?.id_rimborso;
    this.imp_utilizzato = iRUVo?.imp_utilizzato;
  }

  toServerFormat(): IRimborsoUtilizzatoVo {
    // Definisco l'oggetto formattato
    const be: IRimborsoUtilizzatoVo = {
      id_stato_debitorio: this.id_stato_debitorio,
      id_rimborso: this.id_rimborso,
      imp_utilizzato: this.imp_utilizzato,
    };

    // Ritorno l'oggetto formattato per il be
    return be;
  }
}
