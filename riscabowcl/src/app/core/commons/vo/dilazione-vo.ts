import { Moment } from 'moment';
import { HelperVo } from './helper-vo';

export interface IDilazioneVo {
  id_tipo_dilazione: number;
  data_inizio_val: string; // Date string in formato server
  data_fine_val: string; // Date string in formato server
  num_annualita_magg: number;
  importo_magg: number;
  importo_min: number;
  num_mesi: number;
  num_rate: number;
  id_ambito: number;
}

export class DilazioneVo extends HelperVo {
  id_tipo_dilazione: number;
  data_inizio_val: Moment; // Date string in formato server
  data_fine_val: Moment; // Date string in formato server
  num_annualita_magg: number;
  importo_magg: number;
  importo_min: number;
  num_mesi: number;
  num_rate: number;
  id_ambito: number;

  constructor(iDVo?: IDilazioneVo) {
    // Richiamo il super del
    super();

    this.id_tipo_dilazione = iDVo?.id_tipo_dilazione;
    this.data_inizio_val = this.convertServerDateToMoment(
      iDVo?.data_inizio_val
    );
    this.data_fine_val = this.convertServerDateToMoment(iDVo?.data_fine_val);
    this.num_annualita_magg = iDVo?.num_annualita_magg;
    this.importo_magg = iDVo?.importo_magg;
    this.importo_min = iDVo?.importo_min;
    this.num_mesi = iDVo?.num_mesi;
    this.num_rate = iDVo?.num_rate;
    this.id_ambito = iDVo?.id_ambito;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IDilazioneVo {
    // Conversione della data
    const data_inizio_val = this.convertMomentToServerDate(
      this.data_inizio_val as Moment
    );
    const data_fine_val = this.convertMomentToServerDate(
      this.data_fine_val as Moment
    );

    const be: IDilazioneVo = {
      id_tipo_dilazione: this.id_tipo_dilazione,
      data_inizio_val,
      data_fine_val,
      num_annualita_magg: this.num_annualita_magg,
      importo_magg: this.importo_magg,
      importo_min: this.importo_min,
      num_mesi: this.num_mesi,
      num_rate: this.num_rate,
      id_ambito: this.id_ambito,
    };

    return be;
  }
}
