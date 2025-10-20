export class TipoRecapitoVo {
  cod_tipo_recapito?: string;
  des_tipo_recapito?: string;
  id_tipo_recapito?: number;
}

export enum IdTipiRecapito {
  principale = 1,
  alternativo = 2,
}

export enum CodiciTipiRecapito {
  principale = 'P',
  alternativo = 'A',
}
