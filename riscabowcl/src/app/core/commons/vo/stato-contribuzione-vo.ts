import { HelperVo } from './helper-vo';

export interface IStatoContribuzioneSDVo {
  id_stato_contribuzione: number;
  cod_stato_contribuzione: string;
  des_stato_contribuzione: string;
}

export class StatoContribuzioneSDVo extends HelperVo {
  id_stato_contribuzione: number;
  cod_stato_contribuzione: string;
  des_stato_contribuzione: string;

  constructor(iSCVo?: IStatoContribuzioneSDVo) {
    super();

    this.id_stato_contribuzione = iSCVo?.id_stato_contribuzione;
    this.cod_stato_contribuzione = iSCVo?.cod_stato_contribuzione;
    this.des_stato_contribuzione = iSCVo?.des_stato_contribuzione;
  }

  toServerFormat(): IStatoContribuzioneSDVo {
    // Definisco l'oggetto da ritornare
    const be: IStatoContribuzioneSDVo = {
      id_stato_contribuzione: this.id_stato_contribuzione,
      cod_stato_contribuzione: this.cod_stato_contribuzione,
      des_stato_contribuzione: this.des_stato_contribuzione,
    };

    // Ritorno l'oggetto del BE
    return be;
  }
}
