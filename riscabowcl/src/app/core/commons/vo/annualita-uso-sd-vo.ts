import { HelperVo } from './helper-vo';
import { IUsoLeggeVo } from './uso-legge-vo';
import {
  IUsoRiduzioneAumentoVo,
  UsoRiduzioneAumentoVo,
} from './uso-riduzione-aumento-vo';

export interface IAnnualitaUsoSDVo {
  id_annualita_uso_sd: number;
  id_annualita_sd: number;
  tipo_uso: IUsoLeggeVo;
  canone_uso: number;
  canone_unitario: number;
  uso_ridaum: IUsoRiduzioneAumentoVo[];
}

export class AnnualitaUsoSDVo extends HelperVo {
  /** String che definisce un identificativo custom di FE per riconoscere oggetti ancora NON salvati su DB. */
  __id?: string;
  id_annualita_uso_sd?: number;
  id_annualita_sd?: number;
  tipo_uso?: IUsoLeggeVo;
  canone_uso?: number;
  canone_unitario?: number;
  uso_ridaum?: UsoRiduzioneAumentoVo[];

  constructor(aUsoSD?: IAnnualitaUsoSDVo) {
    super();

    this.id_annualita_uso_sd = aUsoSD?.id_annualita_uso_sd;
    this.id_annualita_sd = aUsoSD?.id_annualita_sd;
    this.tipo_uso = aUsoSD?.tipo_uso;
    this.canone_uso = aUsoSD?.canone_uso;
    this.canone_unitario = aUsoSD?.canone_unitario;
    this.uso_ridaum = this.convertUsoRiduzioneAumentoVo(aUsoSD?.uso_ridaum);
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   */
  toServerFormat(): IAnnualitaUsoSDVo {
    // Parse per le liste
    const usiRidAum = this.uso_ridaum;
    const usiRA = this.listToServerFormat(usiRidAum);

    // Sanitizzo il tipo uso
    this.sanitizeFEProperties(this.tipo_uso);

    // Definisco l'oggetto da ritornare al BE
    const be: IAnnualitaUsoSDVo = {
      id_annualita_uso_sd: this.id_annualita_uso_sd,
      id_annualita_sd: this.id_annualita_sd,
      tipo_uso: this.tipo_uso,
      canone_uso: this.canone_uso,
      canone_unitario: this.canone_unitario,
      uso_ridaum: usiRA as IUsoRiduzioneAumentoVo[],
    };

    // Ritorno l'oggetto generato
    return be;
  }

  /**
   * Funzione di conversione dati da IUsoRiduzioneAumentoVo a UsoRiduzioneAumentoVo.
   * @param iUsiRAVo IUsoRiduzioneAumentoVo[] da convertire.
   * @returns UsoRiduzioneAumentoVo[] convertito.
   */
  convertUsoRiduzioneAumentoVo(
    iUsiRAVo: IUsoRiduzioneAumentoVo[]
  ): UsoRiduzioneAumentoVo[] {
    // Verifico l'input
    if (!iUsiRAVo) {
      // Niente da convertire
      return [];
    }

    // Converto tutti gli oggetti
    return iUsiRAVo.map((iUsoRAVo: IUsoRiduzioneAumentoVo) => {
      // Converto l'oggetto
      return new UsoRiduzioneAumentoVo(iUsoRAVo);
    });
  }
}
