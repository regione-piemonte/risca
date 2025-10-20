import { Moment } from 'moment';
import { HelperVo } from './helper-vo';

/**
 * Enum con le informazioni relativi ai tipi d'interessi che vengono gestiti
 */
export enum TipiTassiInteresse {
  interesseLegale = 'L',
  interesseMora = 'M',
}

export interface ITassiDiInteresseVo {
  id_ambito_interesse?: number;
  id_ambito?: number;
  percentuale?: number;
  data_inizio?: string;
  data_fine?: string;
  giorni_legali?: number;
  tipo_interesse?: TipiTassiInteresse;
  flg_cancellazione?: boolean;
}

export class TassiDiInteresseVo extends HelperVo {
  id_ambito_interesse?: number;
  id_ambito?: number;
  percentuale?: number;
  data_inizio?: Moment;
  data_fine?: Moment;
  giorni_legali?: number;
  tipo_interesse?: TipiTassiInteresse;
  flg_cancellazione?: boolean;

  constructor(imVo?: ITassiDiInteresseVo) {
    super();
    this.id_ambito_interesse = imVo?.id_ambito_interesse;
    this.id_ambito = imVo?.id_ambito;
    this.percentuale = imVo?.percentuale;
    this.data_inizio = this.convertServerDateToMoment(imVo?.data_inizio);
    this.data_fine = this.convertServerDateToMoment(imVo?.data_fine);
    this.giorni_legali = imVo?.giorni_legali;
    this.tipo_interesse = imVo?.tipo_interesse;
    this.flg_cancellazione = imVo?.flg_cancellazione;
  }

  /**
   * Funzione che converte le informazioni FE in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): ITassiDiInteresseVo {
    // Definisco l'oggetto da ritornare al server
    const be: ITassiDiInteresseVo = {
      id_ambito_interesse: this.id_ambito_interesse,
      id_ambito: this.id_ambito,
      percentuale: this.percentuale,
      data_inizio: this.convertMomentToServerDate(this.data_inizio),
      data_fine: this.convertMomentToServerDate(this.data_fine),
      giorni_legali: this.giorni_legali,
      tipo_interesse: this.tipo_interesse,
      // flg_cancellazione: this.flg_cancellazione,
    };

    // Ritorno l'oggetto per il be
    return be;
  }
}
