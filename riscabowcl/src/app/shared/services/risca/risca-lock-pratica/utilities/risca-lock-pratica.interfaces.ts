import { LockRiscossioneVo } from '../../../../../core/commons/vo/lock-riscossione-vo';
import { RiscaNotifyCodes } from '../../../../utilities/enums/risca-notify-codes.enums';

/**
 * Interfaccia che definisce le informazioni per il flusso di gestione di verifica e blocco riscossione su DB.
 */
export interface IRichiestaLockPraticaRes {
  /** LockRiscossioneVo con i dati effettivi ritornati dal servizio. Può essere undefined. */
  lockRiscossione?: LockRiscossioneVo;
  /** boolean che definisce se l'utente che ha richiesto il lock della riscossione è effettivamente lo stesso che risulta salvato su DB. */
  isUserLockingRiscossione: boolean;
  /** RiscaNotifyCode che definisce il codice del messaggio da visualizzare per indicare che la riscossione è già lockata su DB. */
  code4PraticaLocked?: RiscaNotifyCodes;
}