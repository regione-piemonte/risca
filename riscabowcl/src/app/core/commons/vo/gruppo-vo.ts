import { ComponenteGruppo, ComponenteGruppoVo } from './componente-gruppo-vo';

/**
 * Class che definisce l'oggetto scaricato dal server.
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
export class GruppoVo {
  id_gruppo_soggetto: number;
  cod_gruppo_soggetto: string; // Mandare stringa vuota, mai null
  cod_gruppo_fonte: string;
  des_gruppo_soggetto: string;
  id_fonte: number;
  fonte?: any;
  id_fonte_origine: number;
  componenti_gruppo: ComponenteGruppoVo[];
}

/**
 * Class che definisce l'oggetto convertito dal server per ComponenteGruppoVo.
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
export class Gruppo {
  id_gruppo_soggetto: number;
  cod_gruppo_soggetto: string;
  cod_gruppo_fonte: string;
  des_gruppo_soggetto: string;
  id_fonte: number;
  fonte?: any;
  id_fonte_origine: number;
  componenti_gruppo: ComponenteGruppo[];
}