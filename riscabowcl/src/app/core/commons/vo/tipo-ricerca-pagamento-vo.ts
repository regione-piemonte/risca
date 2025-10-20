export class TipoRicercaPagamentoVo {
  constructor(
    public id_tipo_ricerca_pagamento: number,
    public cod_tipo_ricerca_pagamento: string,
    public des_tipo_ricerca_pagamento: string
  ) {}
}

/**
 * Interfaccia strettamente collegata alla costante: TipiRicercaPagamentoConst.
 */
export interface IPagamentiFunzionalitaConst {
  daConfermare: TipoRicercaPagamentoVo;
  daRimborsare: TipoRicercaPagamentoVo;
  daVisionare: TipoRicercaPagamentoVo;
  nonDiCompetenza: TipoRicercaPagamentoVo;
  nonIdentificato: TipoRicercaPagamentoVo;
  rimborsato: TipoRicercaPagamentoVo;
  collegato: TipoRicercaPagamentoVo;
}

/**
 * Costante che rappresenta le funzionalità presenti su db per la parte dei tipi ricerca pagamento.
 */
export const TipiRicercaPagamentoConst: IPagamentiFunzionalitaConst = {
  daConfermare: {
    id_tipo_ricerca_pagamento: 1,
    cod_tipo_ricerca_pagamento: 'DCO',
    des_tipo_ricerca_pagamento: 'DA CONFERMARE',
  },
  daRimborsare: {
    id_tipo_ricerca_pagamento: 2,
    cod_tipo_ricerca_pagamento: 'DRI',
    des_tipo_ricerca_pagamento: 'DA RIMBORSARE',
  },
  daVisionare: {
    id_tipo_ricerca_pagamento: 3,
    cod_tipo_ricerca_pagamento: 'DVI',
    des_tipo_ricerca_pagamento: 'DA VISIONARE',
  },
  nonDiCompetenza: {
    id_tipo_ricerca_pagamento: 4,
    cod_tipo_ricerca_pagamento: 'NDC',
    des_tipo_ricerca_pagamento: 'NON DI COMPETENZA',
  },
  nonIdentificato: {
    id_tipo_ricerca_pagamento: 5,
    cod_tipo_ricerca_pagamento: 'NID',
    des_tipo_ricerca_pagamento: 'NON IDENTIFICATO',
  },
  rimborsato: {
    id_tipo_ricerca_pagamento: 6,
    cod_tipo_ricerca_pagamento: 'RIM',
    des_tipo_ricerca_pagamento: 'RIMBORSATO',
  },
  collegato: {
    id_tipo_ricerca_pagamento: 7,
    cod_tipo_ricerca_pagamento: 'COL',
    des_tipo_ricerca_pagamento: 'COLLEGATO',
  },
};