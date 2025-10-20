import { HelperVo } from './helper-vo';

/**
 * Interfaccia che definisce l'oggetto ritornato dal server per: LockRiscossione.
 */
export interface ILockRiscossioneVo {
  id_riscossione: number;
  utente_lock: string;
  cf_utente_lock: string;
}

/**
 * Classe utilizzata all'interno del FE, basata sull'oggetto ritornato dal server: ILockRiscossioneVo.
 */
export class LockRiscossioneVo extends HelperVo {
  id_riscossione: number;
  utente_lock: string;
  cf_utente_lock: string;

  /** Boolean di uso FE che definisce se l'utente corrente è colui che sta lockando la pratica. */
  isUserPraticaLocker: boolean;

  constructor(lockR?: ILockRiscossioneVo) {
    super();

    this.id_riscossione = lockR?.id_riscossione;
    this.utente_lock = lockR?.utente_lock;
    this.cf_utente_lock = lockR?.cf_utente_lock;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): ILockRiscossioneVo {
    // Converto questo oggetto in formato server
    const be: ILockRiscossioneVo = {
      id_riscossione: this.id_riscossione,
      utente_lock: this.utente_lock,
      cf_utente_lock: this.cf_utente_lock,
    };

    // Ritorno l'oggetto per il server
    return be;
  }
}
