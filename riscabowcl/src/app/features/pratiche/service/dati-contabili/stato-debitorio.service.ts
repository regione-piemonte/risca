import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IRiscaTabChanges,
  RiscaServerError,
  RiscaStatoDebitorio,
} from '../../../../shared/utilities';

@Injectable({ providedIn: 'root' })
export class StatoDebitorioService {
  /**
   * Costruttore.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /** EventEmitter che definisce il cambio di tab per quanto riguarda la nav bar del componente. */
  onSezioneSDChanges$ = new Subject<IRiscaTabChanges>();
  /** EventEmitter che permette la comunicazione tra figlio e padre. */
  onChildError$ = new Subject<RiscaServerError>();

  /**
   * ###################################
   * EVENTI DI GESTIONE PER I COMPONENTI
   * ###################################
   */

  /**
   * Funzione che emette un evento per definire la tab che sta per venire aperta.
   * @param tabs IRiscaTabChanges con il target della tab d'aprire.
   */
  sezioneSDChanged(tabs: IRiscaTabChanges) {
    // Emetto l'evento
    this.onSezioneSDChanges$.next(tabs);
  }

  /**
   * Funzione che emette l'evento d'errore generato da uno dei componenti figli.
   * @param e RiscaServerError con la struttura dell'errore.
   */
  childError(e: RiscaServerError) {
    // Emetto l'evento
    this.onChildError$.next(e);
  }

  /**
   * Funzione di wrapping per una maggiore lettura del codice e per la gestione degli errori da parte del componente figli: generali amministrativi dilazione.
   * @param e RiscaServerError con la struttura dell'errore.
   */
  onGADError(e: RiscaServerError) {
    // Emetto l'evento
    this.childError(e);
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che verifica il cambio di tab della sezione stato debitorio.
   * Se avviene un cambio verso una nuova tab e i la sezione in input è attiva, allora ritorno true..
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   * @param sectionToCheck RiscaStatoDebitorio con la sezione da verificare.
   * @returns boolean che definisce se la sta per avvenire un cambio di sezione, rispetto a quella da controllare.
   */
  checkTabSD(
    tabs: IRiscaTabChanges,
    sectionToCheck: RiscaStatoDebitorio
  ): boolean {
    // Ritorno la combinazione di controlli
    return this._riscaUtilities.movingFromTab(tabs, sectionToCheck);
  }
}
