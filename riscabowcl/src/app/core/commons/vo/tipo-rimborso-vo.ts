import { HelperVo } from './helper-vo';

export interface ITipoRimborsoVo {
  id_tipo_rimborso: number;
  cod_tipo_rimborso: string;
  des_tipo_rimborso: string;
}

export class TipoRimborsoVo extends HelperVo {
  id_tipo_rimborso: number;
  cod_tipo_rimborso: string;
  des_tipo_rimborso: string;

  constructor(iTRVo?: ITipoRimborsoVo) {
    super();

    this.id_tipo_rimborso = iTRVo?.id_tipo_rimborso;
    this.cod_tipo_rimborso = iTRVo?.cod_tipo_rimborso;
    this.des_tipo_rimborso = iTRVo?.des_tipo_rimborso;
  }

  toServerFormat(): ITipoRimborsoVo {
    // Definisco l'oggetto per il be
    const be: ITipoRimborsoVo = {
      id_tipo_rimborso: this.id_tipo_rimborso,
      cod_tipo_rimborso: this.cod_tipo_rimborso,
      des_tipo_rimborso: this.des_tipo_rimborso,
    };

    // Ritorno l'oggetto per il be
    return be;
  }
}
