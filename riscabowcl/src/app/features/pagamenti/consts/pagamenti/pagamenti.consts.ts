import { PagamentiFunzionalitaVo } from '../../../../core/commons/vo/pagamenti-funzionalita-vo';

/**
 * Interfaccia strettamente collegata alla costante: PagamentiFunzionalitaConst.
 */
export interface IPagamentiFunzionalitaConst {
  bollettazione: PagamentiFunzionalitaVo;
  stampe: PagamentiFunzionalitaVo;
  morosita: PagamentiFunzionalitaVo;
  utenze: PagamentiFunzionalitaVo;
  simulazioneCanoni: PagamentiFunzionalitaVo;
  doqui: PagamentiFunzionalitaVo;
  report: PagamentiFunzionalitaVo;
}

/**
 * Costante che rappresenta le funzionalità presenti su db per la parte dei pagamenti.
 */
export const PagamentiFunzionalitaConst: IPagamentiFunzionalitaConst = {
  bollettazione: {
    id_funzionalita: 1,
    cod_funzionalita: 'BOL',
    des_funzionalita: 'Bollettazione',
  },
  stampe: {
    id_funzionalita: 2,
    cod_funzionalita: 'STP',
    des_funzionalita: 'Stampe',
  },
  morosita: {
    id_funzionalita: 3,
    cod_funzionalita: 'MOR',
    des_funzionalita: 'Morosità',
  },
  utenze: {
    id_funzionalita: 4,
    cod_funzionalita: 'UTE',
    des_funzionalita: 'Utenze',
  },
  simulazioneCanoni: {
    id_funzionalita: 5,
    cod_funzionalita: 'CAN',
    des_funzionalita: 'SimulazioneCanoni',
  },
  doqui: {
    id_funzionalita: 6,
    cod_funzionalita: 'DOQ',
    des_funzionalita: 'Doqui',
  },
  report: {
    id_funzionalita: 10,
    cod_funzionalita: 'REP',
    des_funzionalita: 'Report',
  },
};
