import { HelperVo } from './helper-vo';

/**
 * Oggetto dal server con le informazioni dei json regola.
 */
export interface IJsonRegolaSogliaVo {
  soglia?: number;
  canone_minimo_soglia_inf?: number;
  canone_minimo_soglia_sup?: number;
}

export class JsonRegolaSogliaVo extends HelperVo {
  soglia: number;
  canone_minimo_soglia_inf: number;
  canone_minimo_soglia_sup: number;

  constructor(iJRSVo?: IJsonRegolaSogliaVo) {
    super();

    this.soglia = iJRSVo?.soglia;
    this.canone_minimo_soglia_inf = iJRSVo?.canone_minimo_soglia_inf;
    this.canone_minimo_soglia_sup = iJRSVo?.canone_minimo_soglia_sup;
  }

  toServerFormat(): IJsonRegolaSogliaVo {
    // Definisco l'oggetto per il server
    let be: IJsonRegolaSogliaVo = {
      soglia: this.soglia,
      canone_minimo_soglia_inf: this.canone_minimo_soglia_inf,
      canone_minimo_soglia_sup: this.canone_minimo_soglia_sup,
    };

    // Ritorno l'oggetto formattato per il be
    return be;
  }
}
