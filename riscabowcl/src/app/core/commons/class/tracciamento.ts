/**
 * Classe che definisce i dati per il tracciamento login.
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
export class LoginTracciamento {
  flg_operazione: string = 'L';
  id_riscossione: string = null;
  json_tracciamento: string = null;
  tipo_json: string = null;
}
