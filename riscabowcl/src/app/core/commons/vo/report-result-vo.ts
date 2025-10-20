import * as moment from 'moment';
import { Moment } from 'moment';
import { ReportsStatus } from '../../../features/report/service/report/utilities/report.enums';
import { DynamicObjAny } from '../../../shared/utilities';
import { HelperVo } from './helper-vo';
import { ElaborazioneVo, IElaborazioneVo } from './elaborazione-vo';

/**
 * Interfaccia con l'oggetto di result che gestisce le informazioni di generazione di un report.
 */
export interface IReportResultVo {
  report_url: string;
  id_risca: number;
  status: ReportsStatus;
  timestamp: string; // Formato ISO string
  file_name?: string;
  error?: IReportErrorVo;
  elabora?: IElaborazioneVo;
}

/**
 * Classe FE generata dai dati di BE.
 */
export class ReportResultVo extends HelperVo {
  report_url: string;
  id_risca: number;
  status: ReportsStatus;
  timestamp: Moment;
  file_name?: string;
  file_name_ext?: string;
  error?: ReportErrorVo;
  elabora?: ElaborazioneVo;

  constructor(iRRVo?: IReportResultVo) {
    // Richiamo il super
    super();

    this.report_url = iRRVo?.report_url;
    this.id_risca = iRRVo?.id_risca;
    this.status = iRRVo?.status;
    this.file_name = iRRVo?.file_name;
    this.timestamp = moment(new Date(iRRVo?.timestamp));
    this.error = new ReportErrorVo(iRRVo?.error);
    this.elabora = new ElaborazioneVo(iRRVo?.elabora);
  }

  toServerFormat(): IReportResultVo {
    // Creo l'oggetto per il server
    const be: IReportResultVo = {
      report_url: this.report_url,
      id_risca: this.id_risca,
      status: this.status,
      file_name: this.file_name,
      timestamp: this.timestamp?.toDate().toISOString(),
      error: this.error?.toServerFormat(),
    };

    // Ritorno l'oggetto server like
    return be;
  }
}

/**
 * Interfaccia con l'oggetto di result che gestisce i dati di errore sulla generazione di un report.
 */
export interface IReportErrorVo {
  status: string;
  code: string;
  title: string;
  detail?: IReportErrorDetailVo;
}

/**
 * Classe FE generata dai dati di BE.
 */
export class ReportErrorVo extends HelperVo {
  status: string;
  code: string;
  title: string;
  detail?: IReportErrorDetailVo;

  constructor(iREVo?: IReportErrorVo) {
    // Richiamo il super
    super();

    this.status = iREVo?.status;
    this.code = iREVo?.code;
    this.title = iREVo?.title;
    this.detail = iREVo?.detail;
  }

  toServerFormat(): IReportErrorVo {
    // Creo l'oggetto per il server
    const be: IReportErrorVo = {
      status: this.status,
      code: this.code,
      title: this.title,
      detail: this.detail,
    };

    // Ritorno l'oggetto server like
    return be;
  }
}

/**
 * Interfaccia con l'oggetto di result che gestisce i dati di dettaglio per l'errore sulla generazione di un report.
 */
export interface IReportErrorDetailVo extends DynamicObjAny {
  des_tipo_elabora: string;
}
