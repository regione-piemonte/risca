import { RecapitoVo } from 'src/app/core/commons/vo/recapito-vo';
import { SoggettoVo } from 'src/app/core/commons/vo/soggetto-vo';

/**
 * Interfaccia di supporto che definisce le informazioni restituite dalla chiusura della modale cerca-titolare-modal
 */
export interface IRecapitoAlternativoModal {
  idPratica?: number;
  datiAnagrafici: SoggettoVo;
  ra: RecapitoVo;
  component: any;
  onConfirmModal: (r: RecapitoVo) => void;
}
