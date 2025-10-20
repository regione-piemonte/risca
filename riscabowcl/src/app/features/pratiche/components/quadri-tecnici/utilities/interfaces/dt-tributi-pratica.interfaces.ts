import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { IUsoLeggeVo } from '../../../../../../core/commons/vo/uso-legge-vo';
import {
  IRiscaAnnoSelect,
  IRiscaCheckboxData,
} from '../../../../../../shared/utilities';
import { AnnualitaSDVo } from '../../../../../../core/commons/vo/annualita-sd-vo';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati uso di legge che utilizzano le form dati tecnici.
 * L'interfaccia è condivisa tra i dati tecnici della pratica e i dati tecnici dello stato debitorio
 */
export interface UsoLeggePSDTributiInfo {
  usoDiLegge?: IUsoLeggeVo;
  popolazione?: number;
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form dati tecnici.
 */
export interface IDatiTecniciTributi {
  uso?: IUsoLeggeVo;
  popolazione?: number;
  annualita?: AnnualitaSDVo;
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form dati tecnici.
 */
export interface DTTributiASD {
  usoDiLegge?: UsoLeggePSDTributiInfo;

  idComponenteDt?: number;
  annualita?: IRiscaAnnoSelect;
  rateoPrimaAnnualita?: IRiscaCheckboxData;
  dataInizio?: NgbDateStruct;
  numeroMesi?: number;
  canoneAnnualita?: number;
}
