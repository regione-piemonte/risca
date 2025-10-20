export interface ISDCollegatiVo {
  cod_utenza: string;
  desc_periodo_pagamento: string;
}

/**
 * Classe che definisce l'oggetto che si aspetta il server per la ricerca.
 */
export class SDCollegatiVo implements ISDCollegatiVo {
  cod_utenza: string;
  desc_periodo_pagamento: string;

  constructor(c?: SDCollegatiVo | ISDCollegatiVo) {
    // Set dati
    this.cod_utenza = c.cod_utenza;
    this.desc_periodo_pagamento = c.desc_periodo_pagamento;
  }
}
