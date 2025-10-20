import { HelperVo } from './helper-vo';

export interface IAttivitaSDVo {
  id_attivita_stato_deb?: number;
  cod_attivita_stato_deb?: string;
  des_attivita_stato_deb?: string;
  id_tipo_attivita_stato_deb?: number;
}

export class AttivitaSDVo extends HelperVo {
  id_attivita_stato_deb?: number;
  cod_attivita_stato_deb?: string;
  des_attivita_stato_deb?: string;
  id_tipo_attivita_stato_deb?: number;

  constructor(iASD?: IAttivitaSDVo) {
    super();

    this.id_attivita_stato_deb = iASD?.id_attivita_stato_deb;
    this.cod_attivita_stato_deb = iASD?.cod_attivita_stato_deb;
    this.des_attivita_stato_deb = iASD?.des_attivita_stato_deb;
    this.id_tipo_attivita_stato_deb = iASD?.id_tipo_attivita_stato_deb;
  }

  toServerFormat(): IAttivitaSDVo {
    // Definisco l'oggetto di ritorno per il be
    let be: IAttivitaSDVo = {
      id_attivita_stato_deb: this.id_attivita_stato_deb,
      cod_attivita_stato_deb: this.cod_attivita_stato_deb,
      des_attivita_stato_deb: this.des_attivita_stato_deb,
      id_tipo_attivita_stato_deb: this.id_tipo_attivita_stato_deb,
    };

    // Ritorno l'oggetto per il be
    return be;
  }
}
