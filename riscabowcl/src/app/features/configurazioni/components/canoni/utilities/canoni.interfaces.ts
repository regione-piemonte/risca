import { RegolaUsoVo } from '../../../../../core/commons/vo/regola-uso-vo';
import { RiscaTableDataConfig } from '../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { RiscaTablePagination } from '../../../../../shared/utilities';
import { IRicercaUsiRegola } from '../../../services/configurazioni/utilities/configurazioni.interfaces';

/**
 * Interfaccia che rappresenta le informazioni passabili come parametri al componente omonimo.
 */
export class ICanoniParams {}

/**
 * Interfaccia che definisce i parametri da passare alla funzione per la conferma di salvataggio delle regole uso.
 */
export interface IOpenSalvaModificheRegole {
  /** RegolaUsoVo[] con la lista di regole modificate. */
  regoleModificate: RegolaUsoVo[];
  /** Funzione che invocata quando tutto il flusso della gestione della model è completato. */
  onComplete: (...args: any) => any;
  /** Funzione che viene invocata se l'utente conferma il salvataggio dei dati delle regole. */
  onConfirm?: (...args: any) => any;
  /** IRicercaUsiRegola con le informazioni di ricerca in path params. */
  pathParams: IRicercaUsiRegola;
  /** RiscaTablePagination con l'oggetto di ricerca per la paginazione. */
  paginazione?: RiscaTablePagination;
}

/**
 * Interfaccia che definisce i parametri da passare alla funzione di apertura della modale di creazione per una nuova regola annualità.
 */
export interface IOpenCreaRegolaAnnualita {
  /**
   * number con l'anno di defualt da visualizzare per la generazione della regola.
   * Secondo analisi: Proposto pari al default restituito dalla lista "canoni ufficiali" + 1 (es. se il primo anno [default] è 2024, visualizzare 2025).
   */
  anno?: number;
}

/**
 * Interfaccia che definisce i parametri da passare alla funzione di apertura della modale di gestione dettaglio regola.
 */
export interface IOpenDettaglioRegola {
  row: RiscaTableDataConfig<RegolaUsoVo>;
  regola: RegolaUsoVo;
}
