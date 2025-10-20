/**
 * Interfaccia che rappresenta l'oggetto ritornato dai servizi per il download di un file.
 */
export interface IFileDownloadVo {
  file_name?: string;
  stream?: any;
  mime_type?: string;
}