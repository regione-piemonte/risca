import { RiscaTablePagination } from 'src/app/shared/utilities';
import { RiscaOpenFileMethods } from '../../enums/http-helper/http-helper.enums';
import { IFileDownloadVo } from '../../commons/vo/file-download-vo';

/**
 * Interfaccia che gestisce la configurazione per la classe RicercaPaginataResponse.
 */
export interface IRicercaPaginataResponse<T> {
  sources: T;
  paging: RiscaTablePagination;
}

/**
 * Interfaccia che gestisce la configurazione per la classe RicercaIncrementaleResponse.
 */
export interface IRicercaIncrementaleResponse<T> {
  sources: T;
  hasMoreItems: boolean;
}

/**
 * Interfaccia che gestisce la configurazione per l'apertura di un file secondo possibili configurazioni.
 */
export interface IRiscaOpenFileConfigs extends IFileDownloadVo {
  file_name: string;
  stream: any;
  extension?: string;
  mime_type?: string;
  convertBase64?: boolean;
  openMethod?: RiscaOpenFileMethods;
}
