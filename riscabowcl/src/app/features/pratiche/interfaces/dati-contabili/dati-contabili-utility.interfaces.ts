import { FormGroup } from '@angular/forms';
import { PraticaVo } from '../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import {
  AppActions,
  FormUpdatePropagation,
} from '../../../../shared/utilities';
import { GADFieldsConfigClass } from '../../components/dati-contabili/stato-debitorio/generali-amministrativi-dilazione/utilities/generali-amministrativi-dilazione.fields-configs';

/**
 * Interfaccia che definisce tutta una serie di parametri extra per la gestione del check della navigazione per i dati contbaili.
 */
export interface IDatiContabiliExtras {
  existSDInvioSpeciale?: boolean;
}

/**
 * Interfaccia che definisce le proprietà per il set dati di un form per la sezione dei dati contabili.
 */
export interface IDCSetFormValue {
  mainForm: FormGroup;
  formControlKey: string;
  value: any;
  options?: FormUpdatePropagation;
}

/**
 * Interfaccia che definisce le proprietà per il set dati di un form per la sezione dei dati contabili: generali-amministrativi-dilazione.
 */
export interface IGenAmmDilSetFormValue {
  mainForm: FormGroup;
  statoDebitorio: StatoDebitorioVo;
  pratica: PraticaVo;
  modalita: AppActions;
  options?: FormUpdatePropagation;
  formInputs?: GADFieldsConfigClass;
}

/**
 * Interfaccia che definisce le proprietà per il set dati di un form per la sezione dei dati contabili: generali-amministrativi-dilazione.
 */
export interface IGADListFormValue extends IGenAmmDilSetFormValue {
  list?: any[];
  matchProperty?: string;
}
