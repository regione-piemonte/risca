import { RiscaAlertConfigs } from '../../..';
import { TRiscaDataPlaceholders } from '../../../types/utilities.types';

/**
 * Interfaccia che definisce la struttura per la configurazione dell'interfaccia per il RiscaAlert tramite configurazione del messaggio da DB.
 */
export interface IAlertConfigsFromCode {
  code: string;
  codesPlaceholders?: string[];
  dataReplace?: TRiscaDataPlaceholders;
  fallbackPlaceholder?: string;
}

/**
 * Interfaccia che definisce la struttura dell'oggetto per applicare differenti controlli sull'alert configs.
 */
export interface IAlertConfigsChecks {
  [key: string]: (c: RiscaAlertConfigs) => boolean;
}
