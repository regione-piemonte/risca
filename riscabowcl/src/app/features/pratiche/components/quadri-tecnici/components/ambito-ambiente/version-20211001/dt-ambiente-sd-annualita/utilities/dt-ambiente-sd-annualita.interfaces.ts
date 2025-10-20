import { IUsoLeggeVo } from '../../../../../../../../../core/commons/vo/uso-legge-vo';
import { UsoLeggePSDAmbienteInfo } from '../../../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { TipiFrazionamentoCanone } from '../../../../../../../../../shared/utilities';
import { Moment } from 'moment';
import { RiscaNotifyCodes } from '../../../../../../../../../shared/utilities/enums/risca-notify-codes.enums';

/**
 * Interfaccia che rappresenta l'insieme delle informazioni per la gestione dell'aggiornamento dell'uso.
 */
export interface GestisciUsoConCanone {
  /** UsoLeggeVo con l'uso di legge che risulta dal calcolo canone senza regola di calcolo. */
  usoJsonRegola?: IUsoLeggeVo;
  /** UsoLeggePSDInfo come oggetto aggiornato per la tabella degli usi di legge. */
  usoTabella?: UsoLeggePSDAmbienteInfo;
}

/**
 * Interfaccia che rappresenta le informazioni utilizzate per il calcolo canone.
 */
export interface IParametriCalcolaCanone {
  dataRif?: string;
  flgFraz?: TipiFrazionamentoCanone;
  dataFraz?: Moment;
  errorCode?: RiscaNotifyCodes;
}