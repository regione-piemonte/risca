import { RiscaTablePagination } from '../../../shared/utilities';
import {
  IRicercaIncrementaleResponse,
  IRicercaPaginataResponse,
} from '../../interfaces/http-helper/http-helper.interfaces';

/**
 * Oggetto che rappresenta i dati e la paginazione insieme
 */
export class RicercaPaginataResponse<T> {
  sources: T;
  paging: RiscaTablePagination;

  constructor(c?: IRicercaPaginataResponse<T>) {
    this.sources = c?.sources;
    this.paging = c?.paging;
  }
}

/**
 * Oggetto che rappresenta i dati e la paginazione insieme, ma del tipo che segnala solo che ci sono altri elementi.
 */
export class RicercaIncrementaleResponse<T> {
  sources: T;
  hasMoreItems: boolean;

  constructor(c?: IRicercaIncrementaleResponse<T>) {
    this.sources = c?.sources;
    this.hasMoreItems = c?.hasMoreItems;
  }
}
