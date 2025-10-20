import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { RicercaStatiDebitoriPagamentoVo } from '../../../../core/commons/vo/ricerca-stati-debitori-pagamento-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { RiscaServerError } from '../../../utilities';

/**
 * Interfaccia che definisce le configurazioni dati per la gestione della modale.
 */
export interface IRiscaSDPerPagamentoModalConfig {
  /** RicercaStatiDebitoriPagamentoVo con le informazioni per la ricerca dati degli stati debitori per un pagamento. */
  ricerca: RicercaStatiDebitoriPagamentoVo;
  /** RicercaPaginataResponse<StatoDebitorioVo[]> con i dati pre caricati per la tabella da visualizzare. */
  preLoadedSearch?: RicercaPaginataResponse<StatoDebitorioVo[]>;
  /** RiscaServerError con le informazioni d'errore per il tentativo di pre caricare i dati della tabella da visualizzare. */
  preLoadedError?: RiscaServerError;
}
