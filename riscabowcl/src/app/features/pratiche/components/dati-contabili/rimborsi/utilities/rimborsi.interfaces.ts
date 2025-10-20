import { Observable } from 'rxjs';
import { AttivitaSDVo } from 'src/app/core/commons/vo/attivita-sd-vo';
import { TipoRimborsoVo } from '../../../../../../core/commons/vo/tipo-rimborso-vo';
import {
  Rimborso,
  RimborsoVo,
} from './../../../../../../core/commons/vo/rimborso-vo';
import { SoggettoVo } from '../../../../../../core/commons/vo/soggetto-vo';

export interface IAsyncDataReq {
  soggettoSD?: Observable<SoggettoVo>;
  tipiAttivita: Observable<AttivitaSDVo[]>;
  tipiRimborsi: Observable<TipoRimborsoVo[]>;
}

export interface IAsyncDataRes {
  soggettoSD?: SoggettoVo;
  tipiAttivita: AttivitaSDVo[];
  tipiRimborsi: TipoRimborsoVo[];
}

export interface IRimborsi {
  attivita_stato_deb: AttivitaSDVo;
  rimborsi: RimborsoVo[];
}
