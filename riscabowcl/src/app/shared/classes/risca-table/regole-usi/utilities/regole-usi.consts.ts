import { RiscaTablePagination } from "../../../../utilities";

export class RegoleUsiTableConsts {
  /** RiscaTablePagination con i dati di default per la paginazione. */
  DEFAULT_PAGINATION: RiscaTablePagination = {
    total: 0,
    label: 'Risultati di ricerca',
    elementsForPage: 20,
    showTotal: true,
    currentPage: 1,
    maxPages: 10,
  };
}