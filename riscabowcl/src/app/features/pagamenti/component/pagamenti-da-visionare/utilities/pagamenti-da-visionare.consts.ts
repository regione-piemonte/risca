import {
  RiscaSortTypes,
  RiscaTablePagination,
} from '../../../../../shared/utilities';

/**
 * Classe con le informazioni costanti associate al componente.
 */
export class PagamentiDaVisionareConsts {
  /** RiscaTablePagination con la paginazione di default da impostare come iniziale. */
  DEFAULT_PAGINATION = new RiscaTablePagination({
    total: 0,
    label: 'Risultati di ricerca',
    elementsForPage: 1,
    showTotal: false,
    currentPage: 1,
    maxPages: 5,
  });
}
