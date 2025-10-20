import { RiduzioneAumentoVo } from 'src/app/core/commons/vo/riduzione-aumento-vo';
import { HelperVo } from './helper-vo';

export interface IUsoRiduzioneAumentoVo {
  id_riduzione_aumento: number;
  riduzione_aumento: RiduzioneAumentoVo;
}

export class UsoRiduzioneAumentoVo extends HelperVo {
  id_riduzione_aumento: number;
  riduzione_aumento: RiduzioneAumentoVo;

  constructor(iUsoRA?: IUsoRiduzioneAumentoVo) {
    super();

    this.id_riduzione_aumento = iUsoRA?.id_riduzione_aumento;
    this.riduzione_aumento = iUsoRA?.riduzione_aumento;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   */
  toServerFormat(): IUsoRiduzioneAumentoVo {
    // Recupero gli usi, altrimenti imposto array vuoto
    const uso_ridaum = this.riduzione_aumento;
    // Pulisco gli oggetti riduzioni aumenti dai metdati del server
    this.sanitizeFEProperties(uso_ridaum);

    // Definisco l'oggetto da ritornare al BE
    const be: IUsoRiduzioneAumentoVo = {
      id_riduzione_aumento: this.id_riduzione_aumento,
      riduzione_aumento: uso_ridaum,
    };

    // Ritorno l'oggetto generato
    return be;
  }
}
