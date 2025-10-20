import { Moment } from 'moment';
import { AnnualitaUsoSDVo, IAnnualitaUsoSDVo } from './annualita-uso-sd-vo';
import { HelperVo } from './helper-vo';

export interface IAnnualitaSDVo {
  id_annualita_sd: number;
  id_stato_debitorio: number;
  anno: number;
  json_dt_annualita_sd: string;
  canone_annuo: number;
  flg_rateo_prima_annualita: number;
  numero_mesi: number;
  data_inizio: string; // String con la data in formato server
  id_componente_dt: number;
  annualita_uso_sd: IAnnualitaUsoSDVo[];
}

export class AnnualitaSDVo extends HelperVo {
  /** String che definisce un identificativo custom di FE per riconoscere oggetti ancora NON salvati su DB. */
  __id?: string;
  id_annualita_sd: number;
  id_stato_debitorio: number;
  anno: number;
  json_dt_annualita_sd: string;
  canone_annuo: number;
  flg_rateo_prima_annualita: boolean;
  numero_mesi: number;
  data_inizio: Moment; // String con la data in formato server
  id_componente_dt: number;
  annualita_uso_sd: AnnualitaUsoSDVo[];

  constructor(aSD?: IAnnualitaSDVo) {
    super();

    this.id_annualita_sd = aSD?.id_annualita_sd;
    this.id_stato_debitorio = aSD?.id_stato_debitorio;
    this.anno = aSD?.anno;
    this.canone_annuo = aSD?.canone_annuo;
    this.flg_rateo_prima_annualita = this.convertServerBoolNumToBoolean(
      aSD?.flg_rateo_prima_annualita
    );
    this.numero_mesi = aSD?.numero_mesi;
    this.data_inizio = this.convertServerDateToMoment(aSD?.data_inizio);
    this.json_dt_annualita_sd = aSD?.json_dt_annualita_sd;
    this.id_componente_dt = aSD?.id_componente_dt;
    this.annualita_uso_sd = this.convertIAnnualitaUsoSD(aSD?.annualita_uso_sd);
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IAnnualitaSDVo {
    const data_inizio = this.convertMomentToServerDate(
      this.data_inizio as Moment
    );

    // Effettuo un parse forzato
    const annualitaUsoSd = this.annualita_uso_sd;
    const annualita_uso_sd = annualitaUsoSd.map((aUsoSD: AnnualitaUsoSDVo) => {
      // Ritrasformo l'oggetto in BE like
      const iAUsoSD: IAnnualitaUsoSDVo = aUsoSD.toServerFormat();
      // Ritorno l'oggetto convertito
      return iAUsoSD;
    });

    const flg_rateo_prima_annualita = this.convertBooleanToServerBoolNum(
      this.flg_rateo_prima_annualita
    );

    // Genero le proprietà ad uso del BE
    const be: IAnnualitaSDVo = {
      id_annualita_sd: this.id_annualita_sd,
      id_stato_debitorio: this.id_stato_debitorio,
      anno: this.anno,
      json_dt_annualita_sd: this.json_dt_annualita_sd,
      canone_annuo: this.canone_annuo,
      flg_rateo_prima_annualita: flg_rateo_prima_annualita,
      numero_mesi: this.numero_mesi,
      data_inizio,
      id_componente_dt: this.id_componente_dt,
      annualita_uso_sd,
    };

    // Ritorno l'oggetto per il BE
    return be;
  }

  /**
   * Funzione che converte IAnnualitaUsoSD[] a AnnualitaUsoSD[].
   * @param iAUsiSD IAnnualitaUsoSD[] da convertire.
   * @returns AnnualitaUsoSD[] convertito.
   */
  convertIAnnualitaUsoSD(iAUsiSD: IAnnualitaUsoSDVo[]): AnnualitaUsoSDVo[] {
    // Verifico l'input
    if (!iAUsiSD) {
      // Niente da convertire
      return [];
    }

    // Converto tutti gli oggetti
    return iAUsiSD.map((iAUsoSD: IAnnualitaUsoSDVo) => {
      // Converto l'oggetto
      return new AnnualitaUsoSDVo(iAUsoSD);
    });
  }
}
