import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { ConfigurazioneAmbitoVo } from 'src/app/core/commons/vo/configurazione-ambito-vo';
import { Istanza, RuoloCompilante } from 'src/app/shared/models';
import { AbilitazioniACTA } from '../../../core/classes/abilitazioni-ambito/abilitazioni-acta.class';
import { AbilitazioniDatiAnagrafici } from '../../../core/classes/abilitazioni-ambito/abilitazioni-dati-anagrafici.class';
import { ConfigService } from '../../../core/services/config.service';
import {
  AbilitaACTA,
  AbilitaDAGruppi,
  AbilitaDASezioni,
  AbilitaDASoggetti,
} from '../../../shared/utilities';
import {
  ConfigRuoloProcedimento,
  OggettoIstanza,
  Opera,
  Referente,
  RuoloSoggetto,
  Soggetto,
  SoggettoIstanza,
} from '../models';
import { AbilitazioniApp } from '../../../core/classes/abilitazioni-ambito/abilitazioni-app.class';

@Injectable({
  providedIn: 'root',
})
export class AmbitoService {
  /** Costante per il path: /ambiti-config */
  private PATH_AMBITI_CONFIG = '/ambiti-config';

  /** String contenente il path per le chiamate al back-end. */
  private beUrl = '';

  /** Array di ConfigurazioneAmbitoVo per la gestione dell'ambitoBS. */
  private _ambitoConfigs: ConfigurazioneAmbitoVo[];
  /** AbilitazioniDatiAnagrafici per la gestione delle abilitazioni del soggetto. */
  private _abilitazioniDA: AbilitazioniDatiAnagrafici;
  /** AbilitazioniACTA per la gestione delle abilitazioni del soggetto. */
  private _abilitazioniACTA: AbilitazioniACTA;
  /** AbilitazioniApp per la gestione delle abilitazioni per l'applicativo. */
  private _abilitazioniApp: AbilitazioniApp;

  constructor(private _http: HttpClient, private _config: ConfigService) {
    this.beUrl = this._config.getBEUrl();
  }

  /**
   * Funzione che recupera le configurazioni per l'ambito passato in input'.
   * @param idAmbito number che definisce l'id ambito per il recupero delle configurazioni.
   * @returns Observable<ConfigurazioneAmbitoVo> con le configurazioni dei dati generali amministrativi.
   */
  getAmbitoConfigs(idAmbito: number): Observable<ConfigurazioneAmbitoVo[]> {
    // Definisco l'url per il collegamento al servizio del server
    const url = this._config.appUrl(this.PATH_AMBITI_CONFIG, idAmbito);

    // Lancio la chiamata per il recupero dati
    return this._http.get<ConfigurazioneAmbitoVo[]>(url).pipe(
      tap((r: ConfigurazioneAmbitoVo[]) => {
        // Aggiorno locamente le configurazioni
        this._ambitoConfigs = r;
        // Lancio il set delle configurazioni per ambito
        this.setAbilitazioniSoggettiGruppi(this._ambitoConfigs);
        this.setAbilitazioniACTA(this._ambitoConfigs);
        this.setAbilitazioniApp(this._ambitoConfigs);
      })
    );
  }

  getRuoliCompilante(): Observable<RuoloCompilante[]> {
    return this._http.get<RuoloCompilante[]>(`${this.beUrl}/ruoli-compilante`);
  }

  getRuoliSoggetto(): Observable<RuoloSoggetto[]> {
    return this._http.get<RuoloSoggetto[]>(`${this.beUrl}/ruoli-soggetto`);
  }

  /*
   * API soggetti (e richiedente)
   */
  getSoggettoProcedimento(
    cf: string,
    codTipoSoggetto: string,
    desProcedimento: string
  ): Observable<Soggetto> {
    return this._http
      .get<Soggetto[]>(
        `${this.beUrl}/soggetti/cf/${cf}/tipo-soggetto/${codTipoSoggetto}/tipo-procedimento/${desProcedimento}`
      )
      .pipe(
        map((res) => {
          // Ritorno il risultato;
          return res[0];
        })
      );
  }

  getRichiedenteProcedimento(
    cfImpresa: string,
    cfSoggetto: string,
    desProcedimento: string
  ): Observable<Soggetto> {
    return this._http
      .get<Soggetto[]>(
        `${this.beUrl}/soggetti/cf-impresa/${cfImpresa}/cf-soggetto/${cfSoggetto}/tipo-soggetto/pf/tipo-procedimento/${desProcedimento}`
      )
      .pipe(
        map((res) => {
          // Ritorno il risultato;
          return res[0];
        })
      );
  }

  salvaSoggetti(soggetto: Soggetto): Observable<Soggetto> {
    if (soggetto.gestUID) {
      return this._http.put<Soggetto>(`${this.beUrl}/soggetti`, soggetto).pipe(
        map((res) => {
          // Ritorno il risultato;
          return res;
        })
      );
    } else {
      return this._http.post<Soggetto>(`${this.beUrl}/soggetti`, soggetto).pipe(
        map((res) => {
          // Ritorno il risultato;
          return res;
          // return res[0];
        })
      );
    }
  }

  /*
   * API opere
   */
  getOpere(): Observable<Opera[]> {
    return this._http.get<Opera[]>(`${this.beUrl}/oggetti`);
  }

  getOpereByComune(payload): Observable<Opera[]> {
    return this._http.post<Opera[]>(`${this.beUrl}/oggetti/search`, payload);
  }

  salvaOpera(opera: Opera) {
    if (opera.gestUID) {
      return this._http.put<Opera>(`${this.beUrl}/oggetti`, opera);
    } else {
      return this._http.post<Opera>(`${this.beUrl}/oggetti`, opera);
    }
  }

  /*
   * API opere-istanza (oggetto-istanza)
   */

  salvaOggettoIstanza(oggettoIstanza: OggettoIstanza) {
    if (oggettoIstanza.gestUID) {
      return this._http.put<OggettoIstanza>(
        `${this.beUrl}/oggetti-istanza`,
        oggettoIstanza
      );
    } else {
      return this._http.post<OggettoIstanza>(
        `${this.beUrl}/oggetti-istanza`,
        oggettoIstanza
      );
    }
  }

  eliminaOggettoIstanza(gestUID: string) {
    return this._http.delete<OggettoIstanza>(
      `${this.beUrl}/oggetti-istanza/${gestUID}`
    );
  }

  /*
   * API Geeco
   */
  getGeecoConfig(idOggettoIstanza: number) {
    return this._http.get(
      `${this.beUrl}/geeco/id-ruolo-applicativo/1/id-oggetto-istanza/${idOggettoIstanza}`
    );
  }

  checkGeometrie(idTipoProcedimento: number, idIstanza: number) {
    return this._http.get<boolean>(
      `${this.beUrl}/oggetti-istanza/tipo-procedimento/${idTipoProcedimento}/id-istanza/${idIstanza}`
    );
  }

  /*
   * API istanza
   */
  salvaIstanza(istanza: Istanza): Observable<Istanza> {
    if (istanza.gestUID) {
      return this._http.put<Istanza>(`${this.beUrl}/istanze`, istanza).pipe(
        map((res) => {
          // Ritorno il risultato;
          return res;
        })
      );
    } else {
      return this._http.post<Istanza>(`${this.beUrl}/istanze`, istanza).pipe(
        map((res) => {
          // Ritorno il risultato;
          return res;
          // return res[0];
        })
      );
    }
  }

  /*
   * API soggetto-istanza
   */
  salvaSoggettiIstanza(
    soggettoIstanza: SoggettoIstanza
  ): Observable<SoggettoIstanza> {
    if (soggettoIstanza.gestUID) {
      return this._http
        .put<SoggettoIstanza>(`${this.beUrl}/soggetti-istanza`, soggettoIstanza)
        .pipe(
          map((res) => {
            // Ritorno il risultato;
            return res;
          })
        );
    } else {
      return this._http
        .post<SoggettoIstanza>(
          `${this.beUrl}/soggetti-istanza`,
          soggettoIstanza
        )
        .pipe(
          map((res) => {
            // Ritorno il risultato;
            return res;
            // return res[0];
          })
        );
    }
  }

  eliminaSoggettiIstanza(
    uidSoggettoIstanza: string
  ): Observable<SoggettoIstanza> {
    return this._http
      .delete<SoggettoIstanza>(
        `${this.beUrl}/soggetti-istanza/${uidSoggettoIstanza}`
      )
      .pipe(
        map((res) => {
          // Ritorno il risultato;
          return res;
        })
      );
  }

  /*
   * API configurazioni
   */
  getConfigurazioneRuoliCompilanteByProcedimento(
    idTipoProcedimento
  ): Observable<ConfigRuoloProcedimento[]> {
    return this._http
      .get<ConfigRuoloProcedimento[]>(
        `${this.beUrl}/procedimenti-ruoli-compilante/id-tipo-procedimento/${idTipoProcedimento}`
      )
      .pipe(
        map((res) => {
          // Ritorno il risultato;
          return res;
        })
      );
  }

  getConfigurazioneRuoloCompilanteByProcedimento(
    idRuoloCompilante,
    idTipoProcedimento
  ): Observable<ConfigRuoloProcedimento> {
    return this._http
      .get<ConfigRuoloProcedimento>(
        `${this.beUrl}/procedimenti-ruoli-compilante/id-ruolo-compilante/${idRuoloCompilante}/id-tipo-procedimento/${idTipoProcedimento}`
      )
      .pipe(
        map((res) => {
          // Ritorno il risultato;
          return res[0];
        })
      );
  }

  getRuoliSoggettoFromRuoloCompilante(
    idRuoloCompilante,
    idTipoProcedimento
  ): Observable<RuoloSoggetto[]> {
    return this._http
      .get<RuoloSoggetto[]>(
        `${this.beUrl}/ruoli-soggetto/id-tipo-procedimento/${idTipoProcedimento}/id-ruolo-compilante/${idRuoloCompilante}`
      )
      .pipe(
        map((res) => {
          // Ritorno il risultato;
          return res;
        })
      );
  }

  /*
   * API referenti
   */
  getReferentiIstanza(idIstanza: number): Observable<Referente[]> {
    return this._http
      .get<Referente[]>(
        `${this.beUrl}/istanze/referenti/id-istanza/${idIstanza}`
      )
      .pipe(
        map((res) => {
          // Ritorno il risultato;
          return res;
        })
      );
  }

  salvaReferente(referente: Referente): Observable<Referente> {
    if (referente.gestUID) {
      return this._http
        .put<Referente>(`${this.beUrl}/istanze/referenti`, referente)
        .pipe(
          map((res) => {
            // Ritorno il risultato;
            return res;
          })
        );
    } else {
      return this._http
        .post<Referente>(`${this.beUrl}/istanze/referenti`, referente)
        .pipe(
          map((res) => {
            // Ritorno il risultato;
            return res;
            // return res[0];
          })
        );
    }
  }

  eliminaReferente(uidReferenteIstanza: string): Observable<Referente> {
    return this._http
      .delete<Referente>(
        `${this.beUrl}/istanze/referenti/${uidReferenteIstanza}`
      )
      .pipe(
        map((res) => {
          // Ritorno il risultato;
          return res;
        })
      );
  }

  /**
   * ####################
   * FUNZIONI DI SUPPORTO
   * ####################
   */

  /**
   * Funzione effettua il set delle configurazioni per soggetti e gruppi.
   * @param ambitoConfigs Array di ConfigurazioneAmbitoVo che definisce le configurazioni per ambito.
   */
  setAbilitazioniSoggettiGruppi(ambitoConfigs?: ConfigurazioneAmbitoVo[]) {
    // Verifico se esiste la configurazione per i soggetti
    const c = ambitoConfigs ? ambitoConfigs : this.ambitoConfigs;
    // Creo le configurazione dei soggetto
    this._abilitazioniDA = new AbilitazioniDatiAnagrafici(c);
  }

  /**
   * Funzione che recupera un'abilitazione, data la sezione e il tipo di abilitazione richiesta.
   * @param sezione AbilitaDASezioni che identifica la sezione.
   * @param abilitazione AbilitaDASoggetti | AbilitaDAGruppi che identifica l'abilitazione.
   * @returns boolean che definisce il tipo di abilitazione.
   */
  getAbilitazioneSoggettiGruppi(
    sezione: AbilitaDASezioni,
    abilitazione: AbilitaDASoggetti | AbilitaDAGruppi
  ): boolean {
    // Verifico se esiste la configurazione
    if (!this.abilitazioniDA) {
      // Lancio le configurazioni con l'oggetto di default
      this.setAbilitazioniSoggettiGruppi();
    }

    // Variabili di comodo
    const s = sezione;
    const a = abilitazione;
    // Richiamo la funzione dell'oggetto
    return this.abilitazioniDA.getAbilitazioneSoggettiGruppi(s, a);
  }

  /**
   * Funzione effettua il set delle configurazioni per ACTA.
   * @param ambitoConfigs Array di ConfigurazioneAmbitoVo che definisce le configurazioni per ambito.
   */
  setAbilitazioniACTA(ambitoConfigs?: ConfigurazioneAmbitoVo[]) {
    // Verifico se esiste la configurazione per i soggetti
    const c = ambitoConfigs ? ambitoConfigs : this.ambitoConfigs;
    // Creo le configurazione dei soggetto
    this._abilitazioniACTA = new AbilitazioniACTA(c);
  }

  /**
   * Funzione che recupera un'abilitazione, dato il tipo di abilitazione richiesta.
   * @param abilitazione AbilitaACTA che identifica l'abilitazione.
   * @returns boolean che definisce il tipo di abilitazione.
   */
  getAbilitazioneACTA(abilitazione: AbilitaACTA): boolean {
    // Verifico se esiste la configurazione
    if (!this.abilitazioniACTA) {
      // Lancio le configurazioni con l'oggetto di default
      this.setAbilitazioniACTA();
    }

    // Variabili di comodo
    const a = abilitazione;
    // Richiamo la funzione dell'oggetto
    return this.abilitazioniACTA.getAbilitazioneACTA(a);
  }

  /**
   * Funzione effettua il set delle configurazioni per l'applicazione.
   * @param ambitoConfigs Array di ConfigurazioneAmbitoVo che definisce le configurazioni per ambito.
   */
  setAbilitazioniApp(ambitoConfigs?: ConfigurazioneAmbitoVo[]) {
    // Verifico se esiste la configurazione per i soggetti
    const c = ambitoConfigs ? ambitoConfigs : this.ambitoConfigs;
    // Creo le configurazione dei soggetto
    this._abilitazioniApp = new AbilitazioniApp(c);
  }

  /**
   * ##############################################
   * GETTER DI COMODO PER LE VARIABILI DEL SERVIZIO
   * ##############################################
   */

  /**
   * Getter per ambitoConfigs.
   * @returns Array di ConfigurazioneAmbitoVo con l'ultimo valore.
   */
  get ambitoConfigs(): ConfigurazioneAmbitoVo[] {
    return this._ambitoConfigs;
  }

  /**
   * Getter per abilitazioniDA.
   * @returns AbilitazioniDatiAnagrafici con l'ultimo valore.
   */
  get abilitazioniDA(): AbilitazioniDatiAnagrafici {
    return this._abilitazioniDA;
  }

  /**
   * Getter per i dati connessi a: isGestioneAbilitata.
   * @returns boolean con il flag di abilitazione.
   */
  get isGestioneAbilitata() {
    return this._abilitazioniDA?.isGestioneAbilitata;
  }

  /**
   * Getter per i dati connessi a: isFonteAbilitataInLettura.
   * @returns boolean con il flag di abilitazione.
   */
  get isFonteAbilitataInLettura() {
    return this._abilitazioniDA?.isFonteAbilitataInLettura;
  }

  /**
   * Getter per i dati connessi a: isFonteAbilitataInScrittura.
   * @returns boolean con il flag di abilitazione.
   */
  get isFonteAbilitataInScrittura() {
    return this._abilitazioniDA?.isFonteAbilitataInScrittura;
  }

  /**
   * Getter per i dati connessi a: isAbilitato.
   * @returns boolean con il flag di abilitazione.
   */
  get isGruppoAbilitato() {
    return this._abilitazioniDA?.isAbilitato;
  }

  /**
   * Getter per la configurazione acta.
   */
  get abilitazioniACTA() {
    return this._abilitazioniACTA;
  }

  /**
   * Getter per la configurazione app.
   */
  get abilitazioniApp() {
    return this._abilitazioniApp;
  }

  /**
   * Getter per la configurazione ACTA, per la configurazione: parolaChiaveSF.
   */
  get parolaChiaveSF() {
    return this._abilitazioniACTA.parolaChiaveSF;
  }

  /**
   * Getter per la configurazione ACTA, per la configurazione: visDocumentiPratica.
   */
  get visDocumentiPratica() {
    return this._abilitazioniACTA.visDocumentiPratica;
  }

  /**
   * Getter per la configurazione ACTA, per la configurazione: idAOO.
   */
  get idAOO() {
    return this._abilitazioniACTA.idAOO;
  }

  /**
   * Getter per la configurazione ACTA, per la configurazione: idNodo.
   */
  get idNodo() {
    return this._abilitazioniACTA.idNodo;
  }

  /**
   * Getter per la configurazione ACTA, per la configurazione: idStruttura.
   */
  get idStruttura() {
    return this._abilitazioniACTA.idStruttura;
  }
}
