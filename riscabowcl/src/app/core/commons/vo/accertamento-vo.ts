import { Moment } from 'moment';
import { HelperVo } from './helper-vo';
import {
  ITipoAccertamentoVo,
  TipoAccertamentoVo,
} from './tipo-accertamento-vo';

/**
 * Classe che definisce la struttura di una riga degli accertamenti provenienti dal BE.
 */
export class Accertamento {
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

export interface IAccertamentoVo {
  id_accertamento?: number;
  id_stato_debitorio?: number;
  tipo_accertamento?: ITipoAccertamentoVo;
  id_file_450?: string;
  num_protocollo?: string;
  data_protocollo?: string; // Le date sono formattate in ServerDate
  data_scadenza?: string; // Le date sono formattate in ServerDate
  data_notifica?: string; // Le date sono formattate in ServerDate
  flg_restituito?: number;
  flg_annullato?: number;
  nota?: string;
}

export class AccertamentoVo extends HelperVo {
  __id?: string;
  id_accertamento?: number;
  id_stato_debitorio?: number;
  tipo_accertamento?: TipoAccertamentoVo;
  id_file_450?: string;
  num_protocollo?: string;
  data_protocollo?: Moment;
  data_scadenza?: Moment;
  data_notifica?: Moment;
  flg_restituito?: boolean;
  flg_annullato?: boolean;
  nota?: string;

  constructor(iRVo?: IAccertamentoVo) {
    super();

    this.id_accertamento = iRVo?.id_accertamento;
    this.id_stato_debitorio = iRVo?.id_stato_debitorio;
    this.tipo_accertamento = new TipoAccertamentoVo(iRVo?.tipo_accertamento);
    this.id_file_450 = iRVo?.id_file_450;
    this.num_protocollo = iRVo?.num_protocollo;
    this.data_protocollo = this.convertServerDateToMoment(
      iRVo?.data_protocollo
    );
    this.data_scadenza = this.convertServerDateToMoment(iRVo?.data_scadenza);
    this.flg_restituito = this.convertServerBoolNumToBoolean(iRVo?.flg_restituito);
    this.flg_annullato = this.convertServerBoolNumToBoolean(iRVo?.flg_annullato);
    this.data_notifica = this.convertServerDateToMoment(iRVo?.data_notifica);
    this.nota = iRVo?.nota;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IAccertamentoVo {
    // Converto i dati
    const data_protocollo = this.convertMomentToServerDate(
      this.data_protocollo
    );
    const data_scadenza = this.convertMomentToServerDate(this.data_scadenza);
    const data_notifica = this.convertMomentToServerDate(this.data_notifica);

    // Creo l'oggetto per il server
    const be: IAccertamentoVo = {
      id_accertamento: this.id_accertamento,
      id_stato_debitorio: this.id_stato_debitorio,
      tipo_accertamento: this.tipo_accertamento.toServerFormat(),
      id_file_450: this.id_file_450,
      num_protocollo: this.num_protocollo,
      data_protocollo,
      data_scadenza,
      flg_restituito: this.convertBooleanToServerBoolNum(this.flg_restituito),
      flg_annullato: this.convertBooleanToServerBoolNum(this.flg_annullato),
      data_notifica,
      nota: this.nota,
    };

    // Ritorno l'oggetto server like
    return be;
  }
}
