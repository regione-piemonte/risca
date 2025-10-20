import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { IRiscaCheckboxData } from './../../../../utilities/interfaces/utilities.interfaces';
import { ITipoAccertamentoVo } from './../../../../../core/commons/vo/tipo-accertamento-vo';

/**
 * Interfaccia di comodo che definisce gli accertamenti nei dati contabili.
 */
export interface IAccertamentoForm {
  id_accertamento?: number;
  id_stato_debitorio?: number;
  tipo_accertamento?: ITipoAccertamentoVo;
  id_file_450?: string;
  num_protocollo?: string;
  data_protocollo?: NgbDateStruct;
  data_scadenza?: NgbDateStruct;
  data_notifica?: NgbDateStruct;
  flg_restituito?: IRiscaCheckboxData;
  flg_annullato?: IRiscaCheckboxData;
  nota?: string;
}
