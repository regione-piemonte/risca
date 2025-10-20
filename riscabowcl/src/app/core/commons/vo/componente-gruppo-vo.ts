/**
 * Class che definisce l'oggetto scaricato dal server.
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
export class ComponenteGruppoVo {
  id_soggetto: number;
  id_gruppo_soggetto: number;
  flg_capo_gruppo: number;
}

/**
 * Class che definisce l'oggetto convertito dal server per ComponenteGruppoVo.
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
export class ComponenteGruppo {
  id_soggetto: number;
  id_gruppo_soggetto: number;
  flg_capo_gruppo: boolean;
}
