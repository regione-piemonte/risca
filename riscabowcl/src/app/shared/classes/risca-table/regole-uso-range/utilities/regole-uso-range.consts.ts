import { RiscaTablePagination } from "../../../../utilities";

export class RegoleUsoRangeTableConsts {
  /** RiscaTablePagination con i dati di default per la paginazione. */
  DEFAULT_PAGINATION: RiscaTablePagination = {
    total: 0,
    label: 'Risultati di ricerca',
    elementsForPage: 10,
    showTotal: true,
    currentPage: 1,
    maxPages: 10,
  };

  TABLE_ID: string = 'RegoleUsoRangeTableId';

  FIELD_VALORE_1: string = 'valore1';
  FIELD_VALORE_2: string = 'valore2';
  LABEL_VALORE_1: string = 'Valore 1';
  LABEL_VALORE_2: string = 'Valore 2';
  LABEL_CONDIZIONE_1: string = 'Condizione 1';
  LABEL_CONDIZIONE_2: string = 'Condizione 2';
  LABEL_SEGNO_CONDIZIONE_1: string = '≥';
  LABEL_SEGNO_CONDIZIONE_2: string = '≤';

  LABEL_CANONE_MINIMO: string = 'Canone minimo';
}
