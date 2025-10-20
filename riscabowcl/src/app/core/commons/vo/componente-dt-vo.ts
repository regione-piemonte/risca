import * as moment from 'moment';
import { Moment } from 'moment';

export class ComponenteDtVo {
  id_componente_dt: number;
  id_ambito: number;
  nome_componente_dt: string;
  des_componente_dt: string;
  // Data stringa, si può fare la new Date per ottenere un'oggetto javascript
  data_inizio_validita: string;
  tipo_componente_dt: TipoComponenteDtVo;
}

export interface IComponenteDt {
  id_componente_dt?: number;
  id_ambito?: number;
  nome_componente_dt?: string;
  des_componente_dt?: string;
  // Data stringa, si può fare la new Date per ottenere un'oggetto javascript
  data_inizio_validita?: Moment;
  tipo_componente_dt?: TipoComponenteDtVo;
}

export class ComponenteDt {
  id_componente_dt: number;
  id_ambito: number;
  nome_componente_dt: string;
  des_componente_dt: string;
  // Data stringa, si può fare la new Date per ottenere un'oggetto javascript
  data_inizio_validita: Moment;
  tipo_componente_dt: TipoComponenteDtVo;

  constructor(c?: IComponenteDt) {
    this.id_componente_dt = c?.id_componente_dt;
    this.id_ambito = c?.id_ambito;
    this.nome_componente_dt = c?.nome_componente_dt;
    this.des_componente_dt = c?.des_componente_dt;
    this.data_inizio_validita = c?.data_inizio_validita;
    this.tipo_componente_dt = c?.tipo_componente_dt;
  }

  /**
   * Funzione di conversione da ComponenteDtVo a ComponenteDt.
   * @param c ComponenteDtVo da convertire e settare.
   */
  setComponenteDtVoAsComponenteDt(c: ComponenteDtVo) {
    this.id_componente_dt = c?.id_componente_dt;
    this.id_ambito = c?.id_ambito;
    this.nome_componente_dt = c?.nome_componente_dt;
    this.des_componente_dt = c?.des_componente_dt;
    this.tipo_componente_dt = c?.tipo_componente_dt;

    // Verifico se esiste la data
    if (c?.data_inizio_validita != null) {
      // Converto data da string a Date
      const dataIniVal = new Date(c.data_inizio_validita);
      // Converto data Date a Moment
      const dataIniValM = moment(dataIniVal);
      // Assegno alla classe l'oggetto
      this.data_inizio_validita = dataIniValM;
    }
  }
}

export class TipoComponenteDtVo {
  id_tipo_componente_dt: number;
  cod_tipo_componente_dt: string;
  des_tipo_componente_dt: string;
}

/*
{
  "id_componente_dt": 1,
  "id_ambito": 1,
  "nome_componente_dt": "NOME_COMPONENTE_ANGULAR",
  "des_componente_dt": "Componente Angular per la gestione dei DT per Ambiente",
  "data_inizio_validita": "2021-01-10T00:00:00.000+0000",
  "tipo_componente_dt": {
      "id_tipo_componente_dt": 1,
      "cod_tipo_componente_dt": "GESTIONE  ",
      "des_tipo_componente_dt": "Componente di Gestione                            "
  }
}
*/
