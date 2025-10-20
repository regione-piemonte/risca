import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { CodiciTipiRecapito } from '../../../../core/commons/vo/tipo-recapito-vo';
import { VerificaPraticheStDebitoriVo } from '../../../../core/commons/vo/verifica-pratiche-stati-deboitori-vo';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IResultVerifyRStDDettaglio,
  VerifyIndTipiOperazioni,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { PraticheService } from '../../../pratiche/service/pratiche.service';

@Injectable({
  providedIn: 'root',
})
export class SoggettoService {
  /**
   * Costruttore
   */
  constructor(
    private _pratiche: PraticheService,
    private _riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * ###########################
   * FUNZIONI DI VERIFY SOGGETTO
   * ###########################
   */

  /**
   * Funzione che effettua tutti i controlli sulle pratiche/stati debitori per un soggetto in modifica.
   * @param s SoggettoVo da verifcare per l'operazione di: modifica.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IResultVerifyRStDDettaglio> con il risultato delle pratiche.
   */
  controllaRStDSoggettoModifica(
    s: SoggettoVo,
    excludeIdRiscossione?: number
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabili di comodo
    const idTOper = VerifyIndTipiOperazioni.update;
    const exIdR = excludeIdRiscossione;
    // Recupero l'id del soggetto
    const idS = s?.id_soggetto;

    // Richiamo e ritorno la verifica per pratiche e st debitori
    return this._pratiche.verifyPStDSoggetto(idS, idTOper, exIdR).pipe(
      map((r: VerificaPraticheStDebitoriVo) => {
        // Ritorno un risultato di comodo
        return this.dettaglioFEVerifyPStD(r);
      })
    );
  }

  /**
   * Funzione che effettua tutti i controlli sulle pratiche/stati debitori per un soggetto in cancellazione.
   * @param s SoggettoVo da verifcare per l'operazione di: cancellazione.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IResultVerifyPSD> che definisce se anche un solo controllo ha trovato delle pratiche collegate.
   */
  controllaRStDSoggettoElimina(
    s: SoggettoVo,
    excludeIdRiscossione?: number
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabili di comodo
    const idTOper = VerifyIndTipiOperazioni.delete;
    const exIdR = excludeIdRiscossione;
    // Recupero l'id del soggetto
    const idS = s?.id_soggetto;

    // Richiamo e ritorno la verifica per pratiche e st debitori
    return this._pratiche.verifyPStDSoggetto(idS, idTOper, exIdR).pipe(
      map((r: VerificaPraticheStDebitoriVo) => {
        // Ritorno un risultato di comodo
        return this.dettaglioFEVerifyPStD(r);
      })
    );
  }

  /**
   * ###########################
   * FUNZIONI DI VERIFY RECAPITI
   * ###########################
   */

  /**
   * Funzione che, sulla base del tipo recapito, definisce le logiche di verifica per il recapito con modalità: elimina.
   * @param recapito RecapitoVo con le informazioni del recapito per la verifica.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IResultVerifyRStDDettaglio> con il risultato delle pratiche.
   */
  controllaRStDTipoRecapitoElimina(
    recapito: RecapitoVo,
    excludeIdRiscossione?: number
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabile di comodo
    const r = recapito;
    const idR = excludeIdRiscossione;
    // Recupero la tipologia del recapito
    const tipoRecapito = recapito.cod_recapito;

    // Verifico la tipologia del recapito
    switch (tipoRecapito) {
      case CodiciTipiRecapito.principale:
        // Ritorno la chiamata per il recapito principale
        return this.controllaRStDRecapitoPrincipaleElimina(r, idR);
      case CodiciTipiRecapito.alternativo:
        // Ritorno la chiamata per il recapito alternativo
        return this.controllaRStDRecapitoAlternativoElimina(r, idR);
      default:
        // Ritorno la chiamata per il recapito alternativo
        return this.controllaRStDRecapitoPrincipaleElimina(r, idR);
    }
  }

  /**
   * Funzione che, sulla base del tipo recapito, definisce le logiche di verifica per il recapito con modalità: modifica.
   * @param recapito RecapitoVo con le informazioni del recapito per la verifica.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IResultVerifyRStDDettaglio> con il risultato delle pratiche.
   */
  controllaRStDTipoRecapitoModifica(
    recapito: RecapitoVo,
    excludeIdRiscossione?: number
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabile di comodo
    const r = recapito;
    const idR = excludeIdRiscossione;
    // Recupero la tipologia del recapito
    const tipoRecapito = recapito.tipo_recapito?.cod_tipo_recapito;

    // Verifico la tipologia del recapito
    switch (tipoRecapito) {
      case CodiciTipiRecapito.principale:
        // Ritorno la chiamata per il recapito principale
        return this.controllaRStDRecapitoPrincipaleModifica(r, idR);
      case CodiciTipiRecapito.alternativo:
        // Ritorno la chiamata per il recapito alternativo
        return this.controllaRStDRecapitoAlternativoModifica(r, idR);
      default:
        // Ritorno la chiamata per il recapito alternativo
        return this.controllaRStDRecapitoAlternativoModifica(r, idR);
    }
  }

  /**
   * ######################################
   * FUNZIONI DI VERIFY RECAPITO PRINCIPALE
   * ######################################
   */

  /**
   * Funzione che effettua tutti i controlli sulle pratiche/stati debitori per un recapito principale in cancellazione.
   * @param ra RecapitoVo da verifcare per l'operazione di: cancellazione.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IResultVerifyRStDDettaglio> con il risultato delle pratiche.
   */
  controllaRStDRecapitoPrincipaleElimina(
    ra: RecapitoVo,
    excludeIdRiscossione?: number
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabili di comodo
    const idTOper = VerifyIndTipiOperazioni.delete;
    const exIdR = excludeIdRiscossione;
    // Recupero l'id del soggetto
    const idRA = ra?.id_recapito;

    // Richiamo e ritorno la verifica per pratiche e st debitori
    return this._pratiche
      .verifyPStDRecapitoPrincipale(idRA, idTOper, exIdR)
      .pipe(
        map((r: VerificaPraticheStDebitoriVo) => {
          // Ritorno un risultato di comodo
          return this.dettaglioFEVerifyPStD(r);
        })
      );
  }

  /**
   * Funzione che effettua tutti i controlli sulle pratiche/stati debitori per un recapito principale in modifica.
   * @param ra RecapitoVo da verifcare per l'operazione di: modifica.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IResultVerifyRStDDettaglio> con il risultato delle pratiche.
   */
  controllaRStDRecapitoPrincipaleModifica(
    ra: RecapitoVo,
    excludeIdRiscossione?: number
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabili di comodo
    const idTOper = VerifyIndTipiOperazioni.update;
    const exIdR = excludeIdRiscossione;
    // Recupero l'id del soggetto
    const idRA = ra?.id_recapito;

    // Richiamo e ritorno la verifica per pratiche e st debitori
    return this._pratiche
      .verifyPStDRecapitoPrincipale(idRA, idTOper, exIdR)
      .pipe(
        map((r: VerificaPraticheStDebitoriVo) => {
          // Ritorno un risultato di comodo
          return this.dettaglioFEVerifyPStD(r);
        })
      );
  }

  /**
   * #######################################
   * FUNZIONI DI VERIFY RECAPITO ALTERNATIVO
   * #######################################
   */

  /**
   * Funzione che effettua tutti i controlli sulle pratiche/stati debitori per un recapito alternativo in cancellazione.
   * @param ra RecapitoVo da verifcare per l'operazione di: cancellazione.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IResultVerifyRStDDettaglio> con il risultato delle pratiche.
   */
  controllaRStDRecapitoAlternativoElimina(
    ra: RecapitoVo,
    excludeIdRiscossione?: number
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabili di comodo
    const idTOper = VerifyIndTipiOperazioni.delete;
    const exIdR = excludeIdRiscossione;
    // Recupero l'id del soggetto
    const idRA = ra?.id_recapito;

    // Richiamo e ritorno la verifica per pratiche e st debitori
    return this._pratiche
      .verifyPStDRecapitoAlternativo(idRA, idTOper, exIdR)
      .pipe(
        map((r: VerificaPraticheStDebitoriVo) => {
          // Ritorno un risultato di comodo
          return this.dettaglioFEVerifyPStD(r);
        })
      );
  }

  /**
   * Funzione che effettua tutti i controlli sulle pratiche/stati debitori per un recapito alternativo in modifica.
   * @param ra RecapitoVo da verifcare per l'operazione di: modifica.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IResultVerifyRStDDettaglio> con il risultato delle pratiche.
   */
  controllaRStDRecapitoAlternativoModifica(
    ra: RecapitoVo,
    excludeIdRiscossione?: number
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabili di comodo
    const idTOper = VerifyIndTipiOperazioni.update;
    const exIdR = excludeIdRiscossione;
    // Recupero l'id del soggetto
    const idRA = ra?.id_recapito;

    // Richiamo e ritorno la verifica per pratiche e st debitori
    return this._pratiche
      .verifyPStDRecapitoAlternativo(idRA, idTOper, exIdR)
      .pipe(
        map((r: VerificaPraticheStDebitoriVo) => {
          // Ritorno un risultato di comodo
          return this.dettaglioFEVerifyPStD(r);
        })
      );
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione di comodo che converte il risultato di controllo su un soggetto in un oggetto di comodo per il chiamante.
   * @param verificaPStDVo VerificaPraticheStDebitoriVo con il risultato del controllo.
   * @returns IResultVerifyRStDDettaglio con le informazioni di comodo.
   */
  private dettaglioFEVerifyPStD(
    verificaPStDVo: VerificaPraticheStDebitoriVo
  ): IResultVerifyRStDDettaglio {
    // Richiamo il servizio di utility
    return this._riscaUtilities.dettaglioFEVerifyPStD(verificaPStDVo);
  }

  /**
   * Funzione di appoggio per richiamare la funzione di recupero del messaggio per le pratiche collegate e stati debitori collegati.
   * @param verifica IResultVerifyRStDDettaglio che definisce il tipo di verifica.
   * @param code string che definisce il codice del messaggio da ritornare in caso di verifica confermata.
   */
  msgFromVerificaPStD(verifica: IResultVerifyRStDDettaglio, code: string): string {
    // Recupero il messaggio con placeholder
    return this._riscaUtilities.msgFromVerificaPStD(verifica, code);
  }

  /**
   * Funzione di comodo che gestisce tutte le logiche di comunicazione all'utente per quanto riguarda le pratiche/stati debitori collegate al soggetto.
   * La gestione avverrà per l'operazione di: cancellazione.
   * @param verifica IResultVerifyRStDDettaglio con l'oggetto di controllo generato dalla funzione. Contiene il risultato dei controlli.
   * @returns string con il messaggio da visualizzare all'utente.
   */
  messaggioControlRStDSElimina(verifica: IResultVerifyRStDDettaglio): string {
    // Definisco le configurazioni per la generazione del messaggio
    const code = RiscaNotifyCodes.E028;
    // Recupero il messaggio con placeholder
    return this.msgFromVerificaPStD(verifica, code);
  }

  /**
   * Funzione di comodo che gestisce tutte le logiche di comunicazione all'utente per quanto riguarda le pratiche/stati debitori collegate al soggetto.
   * La gestione avverrà per l'operazione di: modifica.
   * @param verifica IResultVerifyRStDDettaglio con l'oggetto di controllo generato dalla funzione. Contiene il risultato dei controlli.
   * @returns string contenente il messaggio da visualizzare all'utente.
   */
  messaggioControlRStDSModifica(verifica: IResultVerifyRStDDettaglio) {
    // Definisco le configurazioni per la generazione del messaggio
    const code = RiscaNotifyCodes.A015;
    // Recupero il messaggio con placeholder
    return this.msgFromVerificaPStD(verifica, code);
  }

  /**
   * Funzione di comodo che gestisce il tipo di messaggio d'errore da gestire per il controllo sulla rimozione di un recapito alternativo.
   * @param verifica IResultVerifyRStDDettaglio con le informazioni riguardanti il risultato della verifica.
   * @returns string con il messaggio da visualizzare all'utente per la gestione applicativa.
   */
  messaggioControlRStDRAElimina(verifica: IResultVerifyRStDDettaglio): string {
    // Richiamo la funzione di verifica per la comunicazione con l'utente
    let m = '';

    // Verifico se ho trovato le pratiche/stati debitori e definisco il messaggio
    if (verifica.isObjCollegato) {
      // Definisco le configurazioni per la generazione del messaggio
      const code = RiscaNotifyCodes.A009;
      // Recupero il messaggio con placeholder
      m = this.msgFromVerificaPStD(verifica, code);
      // #
    } else {
      // Recupero il testo del messaggio
      const code = RiscaNotifyCodes.A014;
      // Recupero il messaggio con placeholder
      m = this._riscaMessages.getMessage(code);
      // #
    }

    // Ritorno il messaggio
    return m;
  }

  /**
   * Funzione di comodo che gestisce tutte le logiche di comunicazione all'utente per quanto riguarda le pratiche/stati debitori collegate al recapito alternativo.
   * La gestione avverrà per l'operazione di: modifica.
   * @param verifica IResultVerifyRStDDettaglio con l'oggetto di controllo generato dalla funzione. Contiene il risultato dei controlli.
   * @returns string contenente il messaggio da visualizzare all'utente.
   */
  messaggioControlRStDRAModifica(verifica: IResultVerifyRStDDettaglio) {
    // Definisco le configurazioni per la generazione del messaggio
    const code = RiscaNotifyCodes.A010;
    // Recupero il messaggio con placeholder
    return this.msgFromVerificaPStD(verifica, code);
  }
}
