import { RiscaInfoLevels } from '../../..';

/**
 * Enum che definisce i possibili layout per l'alert.
 */
export enum RAlertLayouts {
  /** string che identifica il layout css: alert-container. */
  standard = 'alert-container',
  /** string che identifica il layout css: alert-container-compact. */
  compact = 'alert-container-compact',
}

/**
 * Enum che definisce le possibili classi per il layout dell'alert.
 */
export enum RAlertClasses {
  /** string che identifica una class per il container: DANGER */
  danger = 'alert-danger',
  /** string che identifica una class per il container: INFO */
  info = 'alert-info',
  /** string che identifica una class per il container: SUCCESS */
  success = 'alert-success',
  /** string che identifica una class per il container: WARNING */
  warning = 'alert-warning',
}

/**
 * Enum che definisce le possibili classi per il layout dell'alert.
 */
export enum RAlertIcons {
  /** string che identifica il path soruce per l'icona di: DANGER */
  danger = 'assets/icon-alert.svg',
  /** string che identifica il path soruce per l'icona di: INFO */
  info = 'assets/icon-info.svg',
  /** string che identifica il path soruce per l'icona di: SUCCESS */
  success = 'assets/icon-success.svg',
  /** string che identifica il path soruce per l'icona di: WARNING */
  warning = 'assets/icon-alert-info.svg',
}

/**
 * Enum che mappa i codici tipo messeggio e lo style d'associare all'alert. La mappa risulta tra: RiscaCodTipiMessaggioUtente (valore) e RAlertClasses (chiave).
 * Verde   1	P	Messaggi feedback positivo
 * Rosso   2	E	Messaggi di errore
 * Azzurro 3	A	Messaggio di avviso legato ad un’azione 
 * Azzurro 4	I	Messaggi Informativi
 * Azzurro 5	F	Messaggi di controllo formale
 * Rosso   6	C	Messaggi di errore calcolo canone
 * Azzurro 7	U	Messaggio relativo ad abilitazione utente
 */
export enum RiscaCodTMStiliAlert {
  P = RiscaInfoLevels.success,
  E = RiscaInfoLevels.danger,
  A = RiscaInfoLevels.info,
  I = RiscaInfoLevels.info,
  F = RiscaInfoLevels.info,
  C = RiscaInfoLevels.danger,
  U = RiscaInfoLevels.info,
}
