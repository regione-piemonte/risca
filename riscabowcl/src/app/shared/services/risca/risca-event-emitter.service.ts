import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { LoggerService } from '../../../core/services/logger.service';

/**
 * Interfaccia che definisce l'oggetto per poter emettere un valore all'interno dell'applicazione.
 */
export interface IRiscaEmitter {
  key: string;
  payload?: any;
}

/**
 * Servizio di utility che permette di generare degli eventi custom intercettabili dai componenti dell'applicazione.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaEventEmitterService {
  /** Subject che permette emettere un evento all'interno dell'applicazione. */
  emitter = new Subject<IRiscaEmitter>();

  /**
   * Costruttore.
   */
  constructor(private _logger: LoggerService) {
    // Lancio il setup del servizio
    this.setupService();
  }

  /**
   * Funzione di setup del servizio.
   */
  private setupService() {}

  /**
   * Funzione che wrappa le logiche per l'emissione di un evento dato un payload.
   * @param data any da passare all'interno dell'evento.
   */
  emit(data: any) {
    // Emetto l'evento passando il payload
    this.emitter.next(data);
  }
}
