import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { TipoElaborazioneVo } from '../../../../../core/commons/vo/tipo-elaborazione-vo';

/**
 * Interfaccia che rappresenta i filtri di ricerca generati dal form.
 */
export interface IFiltriRicercaEsportaDatiFE {
  dataDa: NgbDateStruct;
  dataA: NgbDateStruct;
  tipoElaboraReport: TipoElaborazioneVo;
}
