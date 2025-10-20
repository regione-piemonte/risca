import { Observable } from 'rxjs';
import { AttivitaSDVo, IAttivitaSDVo } from 'src/app/core/commons/vo/attivita-sd-vo';
import {
  Accertamento,
  AccertamentoVo,
} from './../../../../../../core/commons/vo/accertamento-vo';
import { TipoAccertamentoVo } from './../../../../../../core/commons/vo/tipo-accertamento-vo';

export interface IAccertamentiInitReq {
  tipiAttivita: Observable<AttivitaSDVo[]>;
  tipiAccertamenti: Observable<TipoAccertamentoVo[]>;
}

export interface IAccertamentiInitRes {
  tipiAttivita: AttivitaSDVo[];
  attivita: AttivitaSDVo[];
  accertamenti: Accertamento[];
  tipiAccertamenti: TipoAccertamentoVo[];
}

export interface IFormAccertamenti {
  attivita_stato_deb: IAttivitaSDVo;
  accertamenti: AccertamentoVo[];
}

export interface IAccertamenti {
  attivita_stato_deb: AttivitaSDVo;
  accertamenti: AccertamentoVo[];
}
