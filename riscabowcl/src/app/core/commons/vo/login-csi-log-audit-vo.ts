import { CSILogAuditVo, RiscaRpTypes } from "./csi-log-audit-vo";

/**
 * Classe che definisce i dati per il log audit login.
 */
export class LoginCSILogAuditVo extends CSILogAuditVo {
  // data_ora: string;
  id_app: string = '';
  ip_address: string = null;
  utente: string = '';
  operazione: string = 'login';
  ogg_oper: string = 'login applicativo';
  key_oper: string = null;

  constructor(idApp: RiscaRpTypes, cf: string) {
    super();

    this.id_app = idApp;
    this.utente = cf;
  };
}
