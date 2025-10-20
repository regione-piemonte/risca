import { IFormCercaSoggetto } from '../../../../shared/utilities';
import { CercaTitolareModalChiusure } from '../../enums/cerca-titolare-modal/cerca-titolare-modal.enums';

/**
 * Interfaccia di supporto che definisce le informazioni restituite dalla chiusura della modale cerca-titolare-modal
 */
export interface ICTMCloseParams {
  action: CercaTitolareModalChiusure;
  data: IFormCercaSoggetto;
}
