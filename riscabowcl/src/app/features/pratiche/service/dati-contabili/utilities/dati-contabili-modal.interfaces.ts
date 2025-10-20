import { DettaglioPagSearchResultVo } from '../../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { PagamentoVo } from '../../../../../core/commons/vo/pagamento-vo';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { ICallbackDataModal } from '../../../../../shared/utilities';
import { IActionHandleVerifySD } from '../../../interfaces/dati-contabili/dati-contabili.interfaces';

/**
 * Interfaccia che contiene le informazioni per gestire l'apertura della modale di modifica pagamento sd tramite servizio.
 */
export interface IModificaVersamentoModalConfigs {
  pagamento: PagamentoVo;
  dettagliPagamentoSearch: DettaglioPagSearchResultVo[];
}

/**
 * Interfaccia che definisce le informazioni di configurazione per la gestione delle domande per la modifica/salvataggio di uno stato debitorio.
 */
export interface IGestioneSDSalvataggio {
  /** ICallbackDataModal che definisce le callback principali per la gestione della modale. */
  callbacksSD: ICallbackDataModal;
  /** IActionHandleVerifySD[] che definisce le configurazioni per ogni domanda da fare all'utente per gestire lo stato debitorio. */
  prompts: IActionHandleVerifySD[];
  /** StatoDebitorioVo con le informazioni dello stato debitorio da modificare per il salvataggio. */
  statoDebitorio: StatoDebitorioVo;
}
