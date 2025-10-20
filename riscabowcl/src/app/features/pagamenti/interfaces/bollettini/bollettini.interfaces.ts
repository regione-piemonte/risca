import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import {
  DynamicObjAny,
  ICallbackDataModal,
} from '../../../../shared/utilities';
import { IBollettiniModalDataConfigs } from '../../modal/bollettini-modal/utilities/bollettini-modal.interfaces';

/**
 * Interfaccia che definisce le proprietà di configurazione per le modali dei bollettini.
 */
export interface IBollettiniModalConfigs {
  callbacks: ICallbackDataModal;
  modalMap: DynamicObjAny;
  dataModal: IBollettiniModalDataConfigs;
}

/**
 * Interfaccia che rappresenta l'oggetto generato dalla form generica per i bollettini.
 */
export interface IBollettiniModalForm {
  tipoElaborazione: string;
  dataScadenzaPagamento: NgbDateStruct;
  dataProtocollo: NgbDateStruct;
  numeroProtocollo: string;
  dirigenteProTempore: string;
}
