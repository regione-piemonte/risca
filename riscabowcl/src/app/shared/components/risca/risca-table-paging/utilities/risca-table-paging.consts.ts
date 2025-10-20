import { RiscaButtonConfig } from "../../../../utilities";

/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente risca-table-paging.
 */
interface IRTablePagingConsts {
  INIZIO: string;
  INDIETRO: string;
  ALTRO: string;
  PAGINA: string;
  AVANTI: string;
  FINE: string;
  BTN_VAI_A: RiscaButtonConfig;
}

/**
 * Oggetto contenente una serie di costanti per il componente risca-table-paging.
 */
export const RTablePagingConsts: IRTablePagingConsts = {
  INIZIO: 'Inizio',
  INDIETRO: 'Indietro',
  ALTRO: 'Altro',
  PAGINA: 'Pagina',
  AVANTI: 'Avanti',
  FINE: 'Fine',
  BTN_VAI_A: { label: 'Vai a' },
};
