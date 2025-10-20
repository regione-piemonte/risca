/**
 * Oggetto contenente una serie di costanti per il componente omonimo.
 */
export class RegolaRangeFormConsts {
  VALORE_1: string = 'valore1';
  LABEL_VALORE_1: string = '*Valore 1';

  VALORE_2: string = 'valore2';
  LABEL_VALORE_2: string = '*Valore 2';

  CANONE_MINIMO: string = 'canoneMinimo';
  LABEL_CANONE_MINIMO: string = 'Canone minimo';

  LABEL_CONDIZIONE_1: string = 'Condizione 1';
  LABEL_CONDIZIONE_2: string = 'Condizione 2';
  LABEL_SEGNO_CONDIZIONE_1: string = '≥';
  LABEL_SEGNO_CONDIZIONE_2: string = '≤';
  STYLE_SEGNO_CONDIZIONE_1: any = { 'font-size': '25px' };
  STYLE_SEGNO_CONDIZIONE_2: any = { 'font-size': '25px' };

  /** any compatibile con la direttiva NgStyle per la gestione grafica. */
  STYLE_ROW_MINIMO_PRINCIPALE: any = {};
  /** any compatibile con la direttiva NgStyle per la gestione grafica. */
  STYLE_COL_MINIMO_PRINCIPALE: any = {};
}
