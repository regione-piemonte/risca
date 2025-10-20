import { Observable } from 'rxjs';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { ElaborazioneVo } from '../../../../core/commons/vo/elaborazione-vo';
import { StatoElaborazioneVo } from '../../../../core/commons/vo/stato-elaborazione-vo';
import { TipoElaborazioneVo } from '../../../../core/commons/vo/tipo-elaborazione-vo';
import {
  IRicercaElaborazioni,
  RiscaTablePagination,
  ServerNumberAsBoolean,
} from '../../../../shared/utilities';

/**
 * Interfaccia di comodo per la request per la gestione dei dati in tabella del componente bollettini.
 */
export interface IElaborazioneCompleteReq {
  elaborazioni: Observable<RicercaPaginataResponse<ElaborazioneVo[]>>;
  tipi: Observable<TipoElaborazioneVo[]>;
  stati: Observable<StatoElaborazioneVo[]>;
}

/**
 * Interfaccia di comodo per la response per la gestione dei dati in tabella del componente bollettini.
 */
export interface IElaborazioneCompleteRes {
  elaborazioni: RicercaPaginataResponse<ElaborazioneVo[]>;
  tipi: TipoElaborazioneVo[];
  stati: StatoElaborazioneVo[];
}

/**
 * Interfaccia di comodo che serve a definire le configurazioni per il servizio di recupero elaborazioni complete, per: elaborazione.
 */
export interface IElaborazioneCommonConfig {
  idFunzionalita: number;
}

/**
 * Interfaccia di comodo che serve a definire le configurazioni per il servizio di recupero elaborazioni complete, per: elaborazione.
 */
export interface IElaborazioneConfig {
  filtri: IRicercaElaborazioni;
  paginazione?: RiscaTablePagination;
}

/**
 * Interfaccia di comodo che serve a definire le configurazioni per il servizio di recupero elaborazioni complete, per: tipi elaborazione.
 */
export interface IETipiElaborazioneConfig extends IElaborazioneCommonConfig {
  flgVisibile: ServerNumberAsBoolean;
}

/**
 * Interfaccia di comodo che serve a definire le configurazioni per il servizio di recupero elaborazioni complete, per: stati elaborazione.
 */
export interface IEStatiElaborazioneConfig extends IElaborazioneCommonConfig {}
