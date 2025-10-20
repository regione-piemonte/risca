import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { ComuneVo } from '../../../../../../core/commons/vo/comune-vo';
import { NazioneVo } from '../../../../../../core/commons/vo/nazione-vo';
import { ProvinciaVo } from '../../../../../../core/commons/vo/provincia-vo';
import { StatoRiscossioneVo } from '../../../../../../core/commons/vo/stati-riscossione-vo';
import { TipoIstanzaProvvedimentoVo } from '../../../../../../core/commons/vo/tipo-istanza-provvidemento-vo';
import { TipoTitoloVo } from '../../../../../../core/commons/vo/tipo-titolo-vo';
import { TipoSoggettoVo } from '../../../../../ambito/models';
import {
  CodModalitaRicerca,
  DesModalitaRicerca,
  IdModalitaRicerca,
} from './form-ricerca-avanzata-pratiche-sd.enums';
import { IRiscaCheckboxData } from '../../../../../../shared/utilities';
import { ProvinciaCompetenzaVo } from '../../../../../../core/commons/vo/provincia-competenza-vo';
import { TipoRiscossioneVo } from '../../../../../../core/commons/vo/tipo-riscossione-vo';

/**
 * Interfaccia di comodo che definisce la struttura dati per la select della modalità di ricerca.
 */
export interface IFRAPModalitaRicerca {
  id_modalita_ricerca: IdModalitaRicerca;
  cod_modalita_ricerca: CodModalitaRicerca;
  des_modalita_ricerca: DesModalitaRicerca;
}

/**
 * Dati request per ricerca pratica avanzata, lista istanze
 */
export interface IFRAIstanza {
  tipoIstanza?: TipoIstanzaProvvedimentoVo;
  dataDa?: NgbDateStruct;
  dataA?: NgbDateStruct;
}

/**
 * Dati request per ricerca pratica avanzata, lista provvedimenti
 */
export interface IFRAProvvedimento {
  tipoProvvedimento?: TipoIstanzaProvvedimentoVo;
  tipoTitolo?: TipoTitoloVo;
  numeroTitolo?: string;
  dataDa?: NgbDateStruct;
  dataA?: NgbDateStruct;
}

/** Dati request per ricerca pratica avanzata */
export interface IRicercaPraticaAvanzataFormRaw {
  // Modalita ricerca
  modalitaRicerca?: IFRAPModalitaRicerca;
  //Sezione di criteri relativi al titolare
  codiceUtenza?: string;
  tipoSoggetto?: TipoSoggettoVo;
  ragioneSocialeOCognome?: string;
  codiceFiscale?: string;
  partitaIVA?: string;
  stato?: NazioneVo;
  provincia?: ProvinciaVo;
  indirizzo?: string;
  comuneResidenza?: ComuneVo;
  cittaEsteraResidenza?: string;
  // Dati amministrativi
  scadenzaConcessioneDa?: NgbDateStruct;
  scadenzaConcessioneA?: NgbDateStruct;
  tipoTitolo?: TipoTitoloVo;
  tipoProvvedimento?: TipoIstanzaProvvedimentoVo;
  numeroTitolo?: string;
  dataTitoloDa?: NgbDateStruct;
  dataTitoloA?: NgbDateStruct;
  tipologiaPratica?: TipoRiscossioneVo;  
  statoPratica?: StatoRiscossioneVo;
  provinciaCompetenza?: ProvinciaCompetenzaVo;
  dataRinunciaDa?: NgbDateStruct;
  dataRinunciaA?: NgbDateStruct;
  dataRevocaDa?: NgbDateStruct;
  dataRevocaA?: NgbDateStruct;
  annoCanone?: number;
  canone?: number;
  restituitoAlMittente?: IRiscaCheckboxData;
  istanze: IFRAIstanza[];
}


/** Dati request per ricerca pratica avanzata */
export interface IRicercaPraticaAvanzataForm {
  // Modalita ricerca
  modalitaRicerca?: IFRAPModalitaRicerca;
  //Sezione di criteri relativi al titolare
  codiceUtenza?: string;
  tipoSoggetto?: TipoSoggettoVo;
  ragioneSocialeOCognome?: string;
  codiceFiscale?: string;
  partitaIVA?: string;
  stato?: NazioneVo;
  provincia?: ProvinciaVo;
  indirizzo?: string;
  comuneResidenza?: ComuneVo;
  cittaEsteraResidenza?: string;
  // Dati amministrativi
  scadenzaConcessioneDa?: NgbDateStruct;
  scadenzaConcessioneA?: NgbDateStruct;
  tipoTitolo?: TipoTitoloVo;
  tipoProvvedimento?: TipoIstanzaProvvedimentoVo;
  numeroTitolo?: string;
  dataTitoloDa?: NgbDateStruct;
  dataTitoloA?: NgbDateStruct;
  tipologiaPratica?: TipoRiscossioneVo;
  statoPratica?: StatoRiscossioneVo;
  provinciaCompetenza?: ProvinciaCompetenzaVo;
  dataRinunciaRevocaDa?: NgbDateStruct;
  dataRinunciaRevocaA?: NgbDateStruct;
  annoCanone?: number;
  canone?: number;
  restituitoAlMittente?: boolean;
  istanze: IFRAIstanza[];
  provvedimenti: IFRAProvvedimento[];
}
