import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { RiscaServerError } from '../../../../shared/utilities';

export type VerificaIndirizzoSpedizione =
  | IndirizzoSpedizioneVo
  | RiscaServerError;
