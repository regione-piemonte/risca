import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { SoggettoVo } from '../../../../../core/commons/vo/soggetto-vo';
import { ITipoRimborsoVo } from './../../../../../core/commons/vo/tipo-rimborso-vo';

/**
 * Interfaccia di comodo che definisce i rimborsi nei dati contabili.
 */
export interface IRimborsoForm {
  soggetto?: SoggettoVo;
  codiceFiscale?: string;
  ragioneSociale?: string;
  tipoRimborso?: ITipoRimborsoVo;
  importoEccedente?: number;
  numeroProvvedimento?: number;
  dataProvvedimento?: NgbDateStruct;
  causaleRimborso?: string;
  idGruppo?: number;
}
