import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { RiduzioneAumentoVo } from '../../../../../../core/commons/vo/riduzione-aumento-vo';
import { IUnitaMisuraVo } from '../../../../../../core/commons/vo/unita-misura-vo';
import { IUsoLeggeSpecificoVo } from '../../../../../../core/commons/vo/uso-legge-specifico-vo';
import { IUsoLeggeVo } from '../../../../../../core/commons/vo/uso-legge-vo';
import {
  IRiscaAnnoSelect,
  IRiscaCheckboxData,
  TipoCalcoloCanoneADT,
} from '../../../../../../shared/utilities';
import { DTExtrasClass } from '../classes/dt-extras/dt-extras.class';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati uso di legge che utilizzano le form dati tecnici ambiente.
 * L'interfaccia è condivisa tra i dati tecnici della pratica e i dati tecnici dello stato debitorio
 */
export interface UsoLeggePSDAmbienteInfo {
  usoDiLegge?: IUsoLeggeVo;
  usiDiLeggeSpecifici?: IUsoLeggeSpecificoVo[];
  unitaDiMisura?: IUnitaMisuraVo;
  quantita?: number;
  quantitaFaldaProfonda?: number;
  percFaldaProfonda?: number;
  quantitaFaldaSuperficiale?: number;
  percFaldaSuperficiale?: number;
  percRiduzioni?: RiduzioneAumentoVo[];
  percAumenti?: RiduzioneAumentoVo[];
  dataScadenzaEmasIso?: NgbDateStruct;
  tipoCalcoloCanone?: TipoCalcoloCanoneADT;
  canoneUso?: number;
  canoneUnitarioUso?: number;
  canoneUsoFrazionato?: number;

  extras?: DTExtrasClass<any>;
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form datiTecniciAmbienteForm.
 */
export interface IDatiTecniciAmbiente {
  comune?: string;
  corpoIdricoCaptazione?: string;
  gestioneManuale?: IRiscaCheckboxData;
  nomeImpiantoIdroElettrico?: string;
  portataDaAssegnare?: number;
  usiDiLegge?: UsoLeggePSDAmbienteInfo[];
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form datiTecniciAmbienteSDForm.
 */
export interface IDTAmbienteASD {
  comune?: string;
  corpoIdricoCaptazione?: string;
  nomeImpiantoIdroElettrico?: string;
  portataDaAssegnare?: number;
  usiDiLegge?: UsoLeggePSDAmbienteInfo[];
  gestioneManuale?: IRiscaCheckboxData;

  idComponenteDt?: number;
  annualita?: IRiscaAnnoSelect;
  rateoPrimaAnnualita?: IRiscaCheckboxData;
  dataInizio?: NgbDateStruct;
  numeroMesi?: number;
  canoneAnnualita?: number;
}

/**
 * Interfaccia di comodo che definisce l'insieme di chiamate per il recupero dei dati correlati ad un uso di legge.
 */
export interface IDatiUsoLeggeReq {
  usiSpecifici: Observable<IUsoLeggeSpecificoVo[]>;
  unitaMisura: Observable<IUnitaMisuraVo>;
  riduzioni: Observable<RiduzioneAumentoVo[]>;
  aumenti: Observable<RiduzioneAumentoVo[]>;
}

/**
 * Interfaccia di comodo che definisce l'insieme di risposte dei dati correlati ad un uso di legge.
 */
export interface IDatiUsoLeggeRes {
  usiSpecifici: IUsoLeggeSpecificoVo[];
  unitaMisura: IUnitaMisuraVo;
  riduzioni: RiduzioneAumentoVo[];
  aumenti: RiduzioneAumentoVo[];
}
