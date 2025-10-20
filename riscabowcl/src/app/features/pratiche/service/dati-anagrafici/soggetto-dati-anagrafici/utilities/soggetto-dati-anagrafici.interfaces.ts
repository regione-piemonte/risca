import { SoggettoVo } from '../../../../../../core/commons/vo/soggetto-vo';
import { IJsonWarning } from '../../../../../../core/services/http-helper/utilities/http-helper.interfaces';

/**
 * Interfaccia di comodo utilizzata per definire il recupero delle informazioni di un soggetto da POST e PUT.
 * L'oggetto permette di mappare, oltre ai dati del soggetto, delle extra informazioni per l'applicativo.
 */
export interface SoggettoHTTPResponse {
  soggetto: SoggettoVo;
  jsonWarning?: IJsonWarning;
}
