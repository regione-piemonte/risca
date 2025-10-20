import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IRiscaTabChanges,
  RiscaNumeroAvvisoPagamento,
} from '../../../../shared/utilities';

@Injectable({ providedIn: 'root' })
export class NumeroAvvisoPagamentoService {
  /**
   * Costruttore.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /** EventEmitter che definisce il cambio di tab per quanto riguarda la nav bar del componente. */
  onSezioneNAPChanges$ = new Subject<IRiscaTabChanges>();

  /**
   * ###################################
   * EVENTI DI GESTIONE PER I COMPONENTI
   * ###################################
   */

  /**
   * Funzione che emette un evento per definire la tab che sta per venire aperta.
   * @param tabs IRiscaTabChanges con il target della tab d'aprire.
   */
  sezioneNAPChanged(tabs: IRiscaTabChanges) {
    // Emetto l'evento
    this.onSezioneNAPChanges$.next(tabs);
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che verifica il cambio di tab della sezione numero avviso pagamento.
   * Se avviene un cambio verso una nuova tab e i la sezione in input è attiva, allora ritorno true.
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   * @param sectionToCheck RiscaNumeroAvvisoPagamento con la sezione da verificare.
   * @returns boolean che definisce se la sta per avvenire un cambio di sezione, rispetto a quella da controllare.
   */
  checkTabSD(
    tabs: IRiscaTabChanges,
    sectionToCheck: RiscaNumeroAvvisoPagamento
  ): boolean {
    // Ritorno la combinazione di controlli
    return this._riscaUtilities.movingFromTab(tabs, sectionToCheck);
  }
}
