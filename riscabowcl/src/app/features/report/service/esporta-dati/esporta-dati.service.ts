import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { EsportaDatiVo } from '../../../../core/commons/vo/esporta-dati-vo';
import { IReportLocationVo } from '../../../../core/commons/vo/report-location-vo';
import { TipoElaborazioneVo } from '../../../../core/commons/vo/tipo-elaborazione-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import {
  IRiscaServerError,
  RiscaServerError,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { ReportService } from '../report/report.service';
import { CodiciTipiElaboraReport } from './utilities/esporta-dati.enums';

@Injectable({ providedIn: 'root' })
export class EsportaDatiService {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Costrutture.
   */
  constructor(private _logger: LoggerService, private _report: ReportService) {}

  /**
   * #############################
   * GESTIONE LOGICHE ESPORTA DATI
   * #############################
   */

  /**
   * Funzione che effettua la creazione di un report tramite le informazioni di esportazione dati.
   * @param esportaDati EsportaDatiVo come oggetto di esportazione dati.
   * @returns Observable<IReportLocationVo> con le informazioni per gestire lo stato di generazione report.
   */
  createReportEsportaDati(
    esportaDati: EsportaDatiVo
  ): Observable<IReportLocationVo> {
    // Verifico l'input
    if (!esportaDati) {
      // Ritorno l'errore
      return this.reportEsportaDatiNoData();
    }

    // Verifico il codice del tipo elaborazione
    if (esportaDati.elaboraReportBilancio) {
      // Richiamo il servizio di generazione report specifico
      return this._report.createReportBilancio(esportaDati);
      // #
    } else if (esportaDati.elaboraReportVariazioniCompetenza) {
      // Richiamo il servizio di generazione report specifico
      return this._report.createReportVariazioniCompetenza(esportaDati);
      // #
    } else {
      // Recupero il tipo elaborazione
      let tipoElaboraReport: TipoElaborazioneVo;
      tipoElaboraReport = esportaDati.tipoElaboraReport;
      // La configurazione non esiste o non è mappata
      return this.reportEsportaDatiNoElaborazione(tipoElaboraReport);
      // #
    }
  }

  /**
   * ############################
   * GESTIONE ERRORI ESPORTA DATI
   * ############################
   */

  /**
   * Funzione di comodo che gestisce la segnalazione per mancanza dati per l'esporta dati.
   * @returns Observable<any> con l'errore generato.
   */
  private reportEsportaDatiNoData(): Observable<any> {
    // Mancano le configurazioni
    const code = RiscaNotifyCodes.E005;
    const title = `[RISCA_FE] createReportEsportaDati has empty configuration.`;
    const error: IRiscaServerError = { error: { code, title } };
    // Genero un errore
    const e = new RiscaServerError(error);
    // Loggo in console l'errore specifico
    this._logger.error(title, e);
    // Ritorno l'errore
    return throwError(e);
  }

  /**
   * Funzione di comodo che gestisce la segnalazione per mancanza dati per il tipo elaborazione per esporta dati.
   * @param tipoElabora TipoElaborazioneVo con l'oggetto dell'elaborazione.
   * @returns Observable<any> con l'errore generato.
   */
  private reportEsportaDatiNoElaborazione(
    tipoElabora: TipoElaborazioneVo
  ): Observable<any> {
    // Mancano le configurazioni
    const code = RiscaNotifyCodes.E005;
    const title = `[RISCA_FE] createReportEsportaDati - tipo elaborazione not found.`;
    const detail = { tipoElabora };
    const error: IRiscaServerError = { error: { code, title, detail } };
    // Genero un errore
    const e = new RiscaServerError(error);
    // Loggo in console l'errore specifico
    this._logger.error(title, e);
    // Ritorno l'errore
    return throwError(e);
  }
}
