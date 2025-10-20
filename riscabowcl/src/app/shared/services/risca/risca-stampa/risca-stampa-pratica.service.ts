import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { IFileDownloadVo } from '../../../../core/commons/vo/file-download-vo';
import { IRiscaOpenFileConfigs } from '../../../../core/interfaces/http-helper/http-helper.interfaces';
import { ConfigService } from '../../../../core/services/config.service';
import { HttpHelperService } from '../../../../core/services/http-helper/http-helper.service';
import {
  IRiscaServerError,
  RiscaServerError,
  RiscaServerErrorInfo,
} from '../../../utilities';
import { HttpUtilitiesService } from '../../http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';

/**
 * Servizio di utility con funzionalità di gestione per gli alert dell'applicazione.
 */
@Injectable({ providedIn: 'root' })
export class RiscaStampaPraticaService extends HttpUtilitiesService {
  /** Costante con il path per: /stampa-riscossione. */
  private PATH_STAMPA_PDF_PRATICA = '/stampa-riscossione';

  /** Subject che permette di gestire l'evento di: stampa in errore. */
  onStampaError$ = new Subject<RiscaServerError>();

  /** number che definisce l'id pratica attualmente in sessione/gestione. */
  private _idPratica: number;

  /**
   * Costruttore.
   */
  constructor(
    config: ConfigService,
    private _http: HttpClient,
    private _httpHelper: HttpHelperService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * Funzione che lancia la stampa PDF di una pratica, dato l'id pratica.
   * Se l'id pratica NON E' definito, verrà usato quello salvato in sessione del servizio.
   * @param idPratica number con l'id della pratica per la stampa.
   * @returns Obsvervable<IFileDownloadVo> con le informazioni per il salvataggio del documento.
   */
  stampaPDFPratica(idPratica?: number): Observable<IFileDownloadVo> {
    // Verifico l'input
    const idP = idPratica ?? this._idPratica;
    // Definisco l'url per l'api
    const url = this.appUrl(this.PATH_STAMPA_PDF_PRATICA, idP);

    // Richiamo la funzione di stampa
    return this._http.get<IFileDownloadVo>(url);
  }

  /**
   * Funzione di comodo che lancia la stampa del pdf e apre il file in una nuova tab.
   * Se l'id pratica NON E' definito, verrà usato quello salvato in sessione del servizio.
   * Se la stampa della pratica dovesse andare in errore, verrà emesso l'evento: onStampaError$; del servizio 'risca-stampa-pratica.service.ts'.
   * @param idPratica number con l'id della pratica per la stampa.
   */
  stampaEApriPDFPratica(idPratica?: number) {
    // Richiamo il servizio di stampa
    this.stampaPDFPratica(idPratica).subscribe({
      next: (fileInfo: IFileDownloadVo) => {
        // Lancio la funzione di gestione della stampa
        this.onStampaPratica(fileInfo);
        // #
      },
      error: (e: RiscaServerError) => {
        // E' stato generato un errore per la stampa, emette l'evento
        this.onStampaError(e);
      },
    });
  }

  /**
   * Funzione invocata al success della stampa del pdf.
   * @param fileInfo IFileDownloadVo con le informazioni del file generate dal servizio.
   */
  private onStampaPratica(fileInfo: IFileDownloadVo) {
    // Verifico il risultato
    if (!this.isStampaPraticaNonTrovata(fileInfo)) {
      // Estraggo le informazioni dall'interfaccia e genero l'oggetto per l'apertua del file
      const { file_name, stream, mime_type } = fileInfo;
      // Genero la configurazione del file
      const configs: IRiscaOpenFileConfigs = { file_name, stream, mime_type };
      // Chiamo l'apertura del file passando la configurazione
      this._httpHelper.openFileInTab(configs);
    }
  }

  /**
   * Funzione invocata per verificare che il file per il download della pratica esiste.
   * La funzione gestisce in automatico la propagazione dell'evento per pratica in errore.
   * @param fileInfo IFileDownloadVo con le informazioni del file generate dal servizio.
   * @returns boolean che definisce se la stampa non è stata trovata.
   */
  private isStampaPraticaNonTrovata(fileInfo: IFileDownloadVo): boolean {
    // Verifico se l'oggetto non esiste o non ha dati
    const file404 = !fileInfo || !fileInfo.stream;

    // Verifico il risultato del check
    if (file404) {
      // Genero un errore custom, partendo dai dettagli degli errori
      const fakeRSED: IRiscaServerError = {
        error: new RiscaServerErrorInfo({
          code: '404',
          status: '404',
        }),
      };
      // Genero poi la classe di errore
      const fakeRSE = new RiscaServerError(fakeRSED);
      // L'errore è per un 404, ossia informazioni mancanti
      this.onStampaError(fakeRSE);

      // Ritorno la chiamate che la stampa è andata in errore
      return true;
      // #
    }

    // Tutto a posto
    return false;
  }

  /**
   * ########################
   * GESTIONE DELL'ID PRATICA
   * ########################
   */

  /**
   * Funzione di comodo per il reset dell'id pratica.
   */
  resetIdPratica() {
    // Reset dell'id ad undefined
    this._idPratica = undefined;
  }

  /**
   * Funzione di set dati per l'id della pratica da stampare.
   * @param idPratica number con l'id della pratica da impostare nel servizio.
   */
  setIdPraticaStampa(idPratica: number) {
    // Setto l'id della pratica per la stampa
    this._idPratica = idPratica;
  }

  /**
   * Funzione di get dati per l'id della pratica da stampare.
   * @return number con l'id della pratica da impostare nel servizio.
   */
  getIdPraticaStampa(): number {
    // Ritorno l'id della pratica per la stampa
    return this._idPratica;
  }

  /**
   * #####################################
   * FUNZIONI PER LE GESTIONE DEGLI EVENTI
   * #####################################
   */

  /**
   * Funzione che permette di emettere un evento di: errore stampa.
   * @param e RiscaServerError con le informazioni generate dall'errore.
   */
  onStampaError(e: RiscaServerError) {
    // Emetto l'evento di errore
    this.onStampaError$.next(e);
  }
}
