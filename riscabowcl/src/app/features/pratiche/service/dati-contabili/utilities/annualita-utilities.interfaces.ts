import { AnnualitaSDVo } from '../../../../../core/commons/vo/annualita-sd-vo';
import { DilazioneVo } from '../../../../../core/commons/vo/dilazione-vo';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';

/**
 * Interfaccia utilizzata per la raccolta dati per il check di cancellazione di un'annualità.
 */
export interface IAnnualitaCancellabile {
  statoDebitorio: StatoDebitorioVo;
  annualitaList: AnnualitaSDVo[];
  annualitaDel: AnnualitaSDVo;
  dilazione: DilazioneVo;
}
