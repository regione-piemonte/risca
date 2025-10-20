import { Moment } from 'moment';
import { HelperVo } from './helper-vo';
import {
  IRimborsoUtilizzatoVo,
  RimborsoUtilizzatoVo,
} from './rimborso-utilizzato-vo';
import { SoggettoVo } from './soggetto-vo';
import { ITipoRimborsoVo, TipoRimborsoVo } from './tipo-rimborso-vo';
import { isEmptyDeep } from '../../../shared/services/risca/risca-utilities/risca-utilities.functions';

/**
 * Classe che definisce la struttura di una riga dei rimborsi provenienti dal BE.
 */
export class Rimborso {
  codiceUtenza: string;
  importoDovuto?: string;
  dataScadenza?: string;
  interessiDovuti: string;
  importoVersato?: string;
  importoEccedente?: string;
  importoRimborsoCompensazione?: string;
  dataPagamento?: string;
  stato?: string;
}

export interface IRimborsoVo {
  id_rimborso?: number;
  id_stato_debitorio?: number;
  id_gruppo_soggetto?: number;
  tipo_rimborso?: ITipoRimborsoVo;
  soggetto_rimborso?: SoggettoVo;
  imp_rimborso?: number;
  causale?: string;
  num_determina?: number;
  data_determina?: string; // String che contiene la data in formato server
  imp_restituito?: number;
  rimborso_sd_utilizzato?: IRimborsoUtilizzatoVo;
}

export class RimborsoVo extends HelperVo {
  __id?: string;
  id_rimborso?: number;
  id_stato_debitorio?: number;
  id_gruppo_soggetto?: number;
  tipo_rimborso?: TipoRimborsoVo;
  soggetto_rimborso?: SoggettoVo;
  imp_rimborso?: number;
  causale?: string;
  num_determina?: number;
  data_determina?: Moment; // String che contiene la data in formato server
  imp_restituito?: number;
  rimborso_sd_utilizzato?: RimborsoUtilizzatoVo;

  constructor(iRVo?: IRimborsoVo) {
    super();

    this.id_rimborso = iRVo?.id_rimborso;
    this.id_stato_debitorio = iRVo?.id_stato_debitorio;
    this.id_gruppo_soggetto = iRVo?.id_gruppo_soggetto;
    this.tipo_rimborso = new TipoRimborsoVo(iRVo?.tipo_rimborso);
    this.soggetto_rimborso = iRVo?.soggetto_rimborso;
    this.imp_rimborso = iRVo?.imp_rimborso;
    this.causale = iRVo?.causale;
    this.num_determina = iRVo?.num_determina;
    this.data_determina = this.convertServerDateToMoment(iRVo?.data_determina);
    this.imp_restituito = iRVo?.imp_restituito;
    this.rimborso_sd_utilizzato = new RimborsoUtilizzatoVo(
      iRVo?.rimborso_sd_utilizzato
    );
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IRimborsoVo {
    const data_determina = this.convertMomentToServerDate(
      this.data_determina as Moment
    );

    // Effettuo il parse degli oggetti
    let rimborso_sd_utilizzato = this.objToServerFormat(
      this.rimborso_sd_utilizzato
    ) as RimborsoUtilizzatoVo;
    // Verifico se ci sono proprietà
    if (isEmptyDeep(rimborso_sd_utilizzato)) {
      // L'oggetto è vuoto, lo imposto a null
      rimborso_sd_utilizzato = null;
    }

    const be: IRimborsoVo = {
      id_rimborso: this.id_rimborso,
      id_stato_debitorio: this.id_stato_debitorio,
      id_gruppo_soggetto: this.id_gruppo_soggetto,
      tipo_rimborso: this.tipo_rimborso,
      soggetto_rimborso: this.soggetto_rimborso,
      imp_rimborso: this.imp_rimborso,
      causale: this.causale,
      num_determina: this.num_determina,
      data_determina,
      imp_restituito: this.imp_restituito,
      rimborso_sd_utilizzato: rimborso_sd_utilizzato,
    };
    
    // Ritorno l'oggetto server like
    return be;
  }
}
