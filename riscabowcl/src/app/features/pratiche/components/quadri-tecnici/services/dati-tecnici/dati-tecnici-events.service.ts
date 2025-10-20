import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { IUsoLeggeVo } from '../../../../../../core/commons/vo/uso-legge-vo';
import { RiscaServerError } from '../../../../../../shared/utilities';

/**
 * Servizio di utility per la parte dei dati tecnici.
 * Questo servizio è adibito alla gestione degli eventi per la comunicazione tra i componenti tecnici e le altre parti dell'applicazione.
 */
@Injectable({ providedIn: 'root' })
export class DatiTecniciEventsService {
  /** Subject collegato all'evento: errore durante la chiamata ai servizi del componente dato tecnico. */
  onServiziAnnualitaError$ = new Subject<RiscaServerError>();

  /** Subject collegato all'evento: json_regola_mancante per un uso annualità. */
  onUsoAnnualitaJRM$ = new Subject<IUsoLeggeVo>();
  /** Subject collegato all'evento: json_regola_mancante per gli usi annualità. */
  onUsiAnnualitaJRM$ = new Subject<IUsoLeggeVo[]>();
  /** Subject collegato all'evento: json_regola_mancante per il canone annualità. */
  onCanoneAnnualitaJRM$ = new Subject<any>();
  /** Subject che propaga al componente padre l'evento aggiornamento con "pulizia" dati dal componente. */
  onClean$ = new Subject<any>();

  /**
   * ################################
   * FUNZIONI PER EMETTERE GLI EVENTI
   * ################################
   */

  /**
   * Funzione che emette un evento per: errore generico sui servizi richiamati nel componente del dato tecnico annualità.
   * @param e RiscaServerError con l'oggetto d'errore generato dal server.
   */
  erroreServiziAnnualita(e: RiscaServerError) {
    // Emetto l'evento
    this.onServiziAnnualitaError$.next(e);
  }

  /**
   * Funzione che emette un evento per: regola mancante per un uso di legge dell'annualità.
   * @param uso UsoLeggeVo con il dettaglio dell'uso senza annualità.
   */
  regolaMancanteUsoAnnualita(uso: IUsoLeggeVo) {
    // Emetto l'evento
    this.onUsoAnnualitaJRM$.next(uso);
  }

  /**
   * Funzione che emette un evento per: regola mancante per usi di legge dell'annualità.
   * @param usi UsoLeggeVo[] con il dettaglio degli usi senza annualità.
   */
  regolaMancanteUsiAnnualita(usi: IUsoLeggeVo[]) {
    // Emetto l'evento
    this.onUsiAnnualitaJRM$.next(usi);
  }

  /**
   * Funzione che emette un evento per: regola mancante per il canone dell'annualità.
   */
  regolaMancanteCanoneAnnualita() {
    // Emetto l'evento
    this.onUsoAnnualitaJRM$.next();
  }

  /**
   * Funzione che emette un evento per: pulizia dei dati del component.
   */
  datiPuliti(data?: any) {
    // Emetto l'evento
    this.onClean$.next(data);
  }
}
