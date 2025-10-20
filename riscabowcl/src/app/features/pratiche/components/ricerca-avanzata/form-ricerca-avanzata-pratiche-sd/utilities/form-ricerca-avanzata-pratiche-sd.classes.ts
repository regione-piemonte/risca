import {
  IRicercaIstanzaVo,
  IRicercaProvvedimentoVo,
} from '../../../../../../core/commons/vo/riscossione-search-vo';
import { RiscaTablePagination } from '../../../../../../shared/utilities';
import {
  IFRAPModalitaRicerca,
  IRicercaPraticaAvanzataForm,
} from './form-ricerca-avanzata-pratiche-sd.interfaces';

/**
 * Interfaccia di comodo per gestire i dati della ricerca per le snapshot
 */
export interface IRicercaAvanzataPraticheSDFilters {
  modalitaRicerca?: IFRAPModalitaRicerca;
  datiTecnici?: string;
  pratica?: IRicercaPraticaAvanzataForm;
  istanze?: IRicercaIstanzaVo[];
  provvedimenti?: IRicercaProvvedimentoVo[];
  paginazione?: RiscaTablePagination;
}
