import { HelperVo } from './helper-vo';

/**
 * Enum che definisce la tipologia di id_app
 */
export enum RiscaRpTypes {
  sviluppo = 'RISCA_RP_SVI_RISCABE',
  produzione = 'RISCA_RP_PROD_RISCABE',
}

/**
 * Interface che definisce i dati per il log audit login.
 */
export interface ICSILogAuditVo {
  // data_ora?: string;
  id_app?: RiscaRpTypes;
  ip_address?: string;
  utente?: string;
  operazione?: string;
  ogg_oper?: string;
  key_oper?: string;
}

/**
 * Classe che definisce i dati per il log audit login.
 */
export class CSILogAuditVo extends HelperVo {
  // data_ora: string;
  id_app: RiscaRpTypes | string = '';
  ip_address: string = null;
  utente: string = '';
  operazione: string = '';
  ogg_oper: string = '';
  key_oper: string = null;

  constructor(iCSILAVo?: ICSILogAuditVo) {
    super();

    // this.data_ora = iCSILAVo?.data_ora ?? new Date().toISOString();
    this.id_app = iCSILAVo?.id_app ?? '';
    this.ip_address = iCSILAVo?.ip_address ?? null;
    this.utente = iCSILAVo?.utente ?? '';
    this.operazione = iCSILAVo?.operazione ?? '';
    this.ogg_oper = iCSILAVo?.ogg_oper ?? '';
    this.key_oper = iCSILAVo?.key_oper ?? null;
  }

  toServerFormat(): ICSILogAuditVo {
    // Definisco la variabile per il be
    let be: ICSILogAuditVo;

    // Recupero le informazioni dall'oggetto
    be = {
      // data_ora: this.data_ora, => lo imposta il servizio di BE
      id_app: this.id_app as RiscaRpTypes,
      ip_address: this.ip_address,
      utente: this.utente,
      operazione: this.operazione,
      ogg_oper: this.ogg_oper,
      key_oper: this.key_oper,
    };

    // Ritorno l'oggetto convertito
    return be;
  }
}
