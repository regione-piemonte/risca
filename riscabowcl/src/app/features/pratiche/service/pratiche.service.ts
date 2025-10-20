import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { cloneDeep } from 'lodash';
import { forkJoin, Observable, of, throwError } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { RicercaPaginataResponse } from 'src/app/core/classes/http-helper/http-helper.classes';
import { ProvinciaCompetenzaVo } from 'src/app/core/commons/vo/provincia-competenza-vo';
import { TipoIstanzaProvvedimentoVo } from 'src/app/core/commons/vo/tipo-istanza-provvidemento-vo';
import { HttpHelperService } from 'src/app/core/services/http-helper/http-helper.service';
import { IstanzaProvvedimentoVo } from '../../../core/commons/vo/istanza-provvedimento-vo';
import {
  PraticaDTVo,
  PraticaEDatiTecnici,
  PraticaVo,
} from '../../../core/commons/vo/pratica-vo';
import { RiscossioneSearchResultVo } from '../../../core/commons/vo/riscossione-search-result-vo';
import {
  IRiscossioneSearchV2Vo,
  RiscossioneSearchV2Vo,
} from '../../../core/commons/vo/riscossione-search-vo';
import { StatoRiscossioneVo } from '../../../core/commons/vo/stati-riscossione-vo';
import { TipoAutorizzazioneVo } from '../../../core/commons/vo/tipo-autorizzazioni-vo';
import { TipoRiscossioneVo } from '../../../core/commons/vo/tipo-riscossione-vo';
import { TipoTitoloVo } from '../../../core/commons/vo/tipo-titolo-vo';
import { VerificaPraticheStDebitoriVo } from '../../../core/commons/vo/verifica-pratiche-stati-deboitori-vo';
import { ConfigService } from '../../../core/services/config.service';
import { UserService } from '../../../core/services/user.service';
import { CommonConsts } from '../../../shared/consts/common-consts.consts';
import { HttpUtilitiesService } from '../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IdIstanzaProvvedimento,
  RiscaServerError,
  RiscaTablePagination,
  VerifyIndTipiOggetti,
  VerifyIndTipiOperazioni,
} from '../../../shared/utilities';
import { CodModalitaRicerca } from '../components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.enums';
import { Gruppo, GruppoVo } from './../../../core/commons/vo/gruppo-vo';
import { GruppoSoggettoService } from './dati-anagrafici/gruppo-soggetto.service';

/**
 * INterfaccia di comodo per la verifica di diversi gruppi, come request.
 */
interface IVerifyGruppiReq {
  [key: string]: Observable<VerificaPraticheStDebitoriVo>;
}

/**
 * INterfaccia di comodo per la verifica di diversi gruppi, come response. Le proprietà saranno gli id dei gruppi con la richiesta di verifica.
 */
export interface IVerifyGruppiRes {
  [key: string]: VerificaPraticheStDebitoriVo;
}

@Injectable({ providedIn: 'root' })
export class PraticheService extends HttpUtilitiesService {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** Costante per il separatore dei path */
  private PATH_SEPARATORE = '/';
  /** Costante per il path: /dati-tecnici */
  private PATH_DATI_TECNICI = '/dati-tecnici';
  /** Costante per il path: /riscossioni */
  private PATH_RISCOSSIONI = '/riscossioni';
  /** Costante per il path: /codRiscossione */
  private PATH_CODICE_RISCOSSIONE = '/codRiscossione';
  /** Costante per il path: /numPratica */
  private PATH_NUMERO_PRATICA = '/numPratica';
  /** Costante per il path: /ambiti */
  private PATH_AMBITI = '/ambiti';
  /** Costante per il path: /tipi-provvedimentoistanza */
  private PATH_TIPI_PROVVEDIMENTO_ISTANZA = '/tipi-provvedimentoistanza';
  /** Costante per il path: /stati-riscossione */
  private PATH_STATI_RISCOSSIONE = '/stati-riscossione';
  /** Costante per il path: /tipi-autorizzazione */
  private PATH_TIPI_AUTORIZZAZIONE = '/tipi-autorizzazione';
  /** Costante per il path: /tipi-riscossione */
  private PATH_TIPI_RISCOSSIONE = '/tipi-riscossione';
  /** Costante per il path: /tipi-titolo */
  private PATH_TIPI_TITOLO = '/tipi-titolo';
  /** Costante per il path: /_verify_riscossioni_stdebitori */
  private PATH_VERIFY_RISCOSSIONI_STDEBITORI =
    '/_verify_riscossioni_stdebitori';
  /** Costante per il path: /_search/riscossioni */
  private PATH_SEARCH_RISCOSSIONI = '/_search/riscossioni';
  /** Costante per il path: /provvedimenti-istanze */
  private PATH_PROVVEDIMENTI_ISTANZE = '/provvedimenti-istanze';

  /** ProvinciaCompetenzaVo[] con la lista delle province competenza dell'applicazione. */
  private _provinceCompetenza: ProvinciaCompetenzaVo[] = [];
  /** TipoRiscossioneVo[] con la lista dei tipi riscossione scaricati per l'ambito. */
  private _tipiRiscossione: TipoRiscossioneVo[] = [];
  /** TipoTitoloVo[] con la lista dei tipi riscossione scaricati per l'ambito. */
  private _tipiTitolo: TipoTitoloVo[] = [];
  /** TipoProvvedimentoVo[] con la lista dei tipi istanza scaricati per l'ambito. */
  private _tipiIstanza: TipoIstanzaProvvedimentoVo[] = [];
  /** TipoProvvedimentoVo[] con la lista dei tipi istanza scaricati per l'ambito. */
  private _tipiProvvedimento: TipoIstanzaProvvedimentoVo[] = [];
  /** TipoAutorizzazioneVo[] con la lista dei tipi autorizzazione scaricati per l'ambito. */
  private _tipiAutorizzazione: TipoAutorizzazioneVo[] = [];
  /** StatoRiscossione[] con la lista degli stati riscossione scaricati per l'ambito. */
  private _statiRiscossione: StatoRiscossioneVo[] = [];

  /**
   * Costruttore.
   */
  constructor(
    config: ConfigService,
    private _gruppoSoggetto: GruppoSoggettoService,
    private _http: HttpClient,
    private _httpHelper: HttpHelperService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * #########################
   * GESTIONE TIPI RISCOSSIONE
   * #########################
   */

  /**
   * Funzione di recupero per i tipi riscossione.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @param dataInizioVal string che definisce la data inizio validatà. Se non definita, verranno presi tutti gli elementi esistenti.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<TipoRiscossioneVo[]> con i dati per le tipologie di pratiche.
   */
  getTipiRiscossione(
    idAmbito: number,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<TipoRiscossioneVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._tipiRiscossione?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._tipiRiscossione);
    }

    // Costruisco l'url di richiesta
    const PATH_AMBITI_ID = `${this.PATH_AMBITI}/${idAmbito}`;
    const url: string = this.appUrl(PATH_AMBITI_ID, this.PATH_TIPI_RISCOSSIONE);
    // Definisco i query params per la chiamata
    const params = this.createQueryParams({
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Scarico i dati
    return this._http.get<TipoRiscossioneVo[]>(url, { params }).pipe(
      tap((tipiRiscossione: TipoRiscossioneVo[]) => {
        // Assegno localmente i codici utenza
        this._tipiRiscossione = cloneDeep(tipiRiscossione);
      })
    );
  }

  /**
   * Funzione di recupero per un tipo riscossione, dato un id riscossione da cercare.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @param idTipoRiscossione number che definisce l'id tipo riscossione per la ricerca.
   * @param dataInizioVal string che definisce la data inizio validatà. Se non definita, verranno presi tutti gli elementi esistenti.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<TipoRiscossioneVo> con il tipo riscossione recuperato..
   */
  getTipiRiscossioneByIdTipoRiscossione(
    idAmbito: number,
    idTipoRiscossione: number,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<TipoRiscossioneVo> {
    // Richiamo la funzione di recupero dei dati
    return this.getTipiRiscossione(idAmbito, dataInizioVal, dataFineVal).pipe(
      map((tipiRiscossione: TipoRiscossioneVo[]) => {
        // Effettuo il filtro dei dati
        return tipiRiscossione?.find((tr) => {
          // Ricerco per stesso id
          return tr.id_tipo_riscossione === idTipoRiscossione;
        });
      })
    );
  }

  /**
   * ##########################
   * GESTIONE STATI RISCOSSIONE
   * ##########################
   */

  /**
   * Funzione di recupero per gli stati riscossione.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @returns Observable<StatoRiscossioneVo[]>.
   */
  getStatiRiscossione(idAmbito: number): Observable<StatoRiscossioneVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._statiRiscossione?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._statiRiscossione);
    }

    // Creo l'url per la chiamta
    const PATH_ID_AMBITO = `/${idAmbito}`;
    const url: string = this.appUrl(
      this.PATH_AMBITI,
      PATH_ID_AMBITO,
      this.PATH_STATI_RISCOSSIONE
    );

    // Tramite servizio scarico i dati
    return this._http.get<StatoRiscossioneVo[]>(url).pipe(
      tap((statiRiscossione: StatoRiscossioneVo[]) => {
        // Assegno localmente i codici utenza
        this._statiRiscossione = cloneDeep(statiRiscossione);
      })
    );
  }

  /**
   * ############################
   * GESTIONE TIPI AUTORIZZAZIONE
   * ############################
   */

  /**
   * Funzione di recupero per i tipi autorizzazione.
   * Questa è la funzione standard per l'applicazione, senza filtri sulle date.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @returns Observable<TipoAutorizzazioneVo[]> con la lista dei tipi autorizzazione delle pratiche.
   */
  getTipiAutorizzazione(idAmbito: number): Observable<TipoAutorizzazioneVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._tipiAutorizzazione?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._tipiAutorizzazione);
    }

    // Tramite servizio scarico i dati
    return this.getTipiAutorizzazioneWithPeriod(idAmbito).pipe(
      tap((tipiAutorizzazione: TipoAutorizzazioneVo[]) => {
        // Assegno localmente i codici utenza
        this._tipiAutorizzazione = cloneDeep(tipiAutorizzazione);
      })
    );
  }

  /**
   * Funzione di recupero per i tipi autorizzazione.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @param dataInizioVal string che definisce la data inizio validatà. Se non definita, verranno presi tutti gli elementi esistenti.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<TipoAutorizzazioneVo[]> con la lista dei tipi autorizzazione delle pratiche.
   */
  getTipiAutorizzazioneWithPeriod(
    idAmbito: number,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<TipoAutorizzazioneVo[]> {
    // Costruisco l'url di richiesta
    const PATH_AMBITI_ID = `${this.PATH_AMBITI}/${idAmbito}`;
    const url: string = this.appUrl(
      PATH_AMBITI_ID,
      this.PATH_TIPI_AUTORIZZAZIONE
    );
    // Definisco i query params per la chiamata
    const params = this.createQueryParams({
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });
    // Tramite servizio scarico i dati
    return this._http.get<TipoAutorizzazioneVo[]>(url, { params });
  }

  /**
   * #####################
   * GESTIONE TIPI ISTANZA
   * #####################
   */

  /**
   * Funzione di recupero per i tipi istanza.
   * Questa è la funzione standard per l'applicazione, senza filtri sulle date.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @returns Observable<TipoProvvedimentoVo[]> con la lista dei tipi istanza delle pratiche.
   */
  getTipiIstanza(idAmbito: number): Observable<TipoIstanzaProvvedimentoVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._tipiIstanza?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._tipiIstanza);
    }

    // Tramite servizio scarico i dati
    return this.getTipiIstanzaWithPeriod(idAmbito).pipe(
      tap((tipiIstanza: TipoIstanzaProvvedimentoVo[]) => {
        // Assegno localmente i codici utenza
        this._tipiIstanza = cloneDeep(tipiIstanza);
      })
    );
  }

  /**
   * Funzione di recupero per i tipi istanza.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @param dataInizioVal string che definisce la data inizio validatà. Se non definita, verranno presi tutti gli elementi esistenti.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<TipoProvvedimentoVo[]> con la lista dei tipi istanza delle pratiche.
   */
  getTipiIstanzaWithPeriod(
    idAmbito: number,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<TipoIstanzaProvvedimentoVo[]> {
    // Definisco il flag istanza
    const flgIstanza = IdIstanzaProvvedimento.istanza;
    // Costruisco l'url di richiesta
    const PATH_AMBITI_ID = `${this.PATH_AMBITI}/${idAmbito}`;
    const url: string = this.appUrl(
      PATH_AMBITI_ID,
      this.PATH_TIPI_PROVVEDIMENTO_ISTANZA
    );
    // Definisco i query params per la chiamata
    const params = this.createQueryParams({
      flgIstanza: flgIstanza,
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Tramite servizio scarico i dati
    return this._http.get<TipoIstanzaProvvedimentoVo[]>(url, { params });
  }

  /**
   * ###########################
   * GESTIONE TIPI PROVVEDIMENTI
   * ###########################
   */

  /**
   * Funzione di recupero per i tipi provvedimento.
   * Questa è la funzione standard per l'applicazione, senza filtri sulle date.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @returns Observable<TipoProvvedimentoVo[]> con la lista dei tipi provvedimento delle pratiche.
   */
  getTipiProvvedimento(idAmbito: number): Observable<TipoIstanzaProvvedimentoVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._tipiProvvedimento?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._tipiProvvedimento);
    }

    // Tramite servizio scarico i dati
    return this.getTipiProvvedimentoWithPeriod(idAmbito).pipe(
      tap((tipiProvvedimento: TipoIstanzaProvvedimentoVo[]) => {
        // Assegno localmente i codici utenza
        this._tipiProvvedimento = cloneDeep(tipiProvvedimento);
      })
    );
  }

  /**
   * Funzione di recupero per i tipi provvedimento.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @param dataInizioVal string che definisce la data inizio validatà. Se non definita, verranno presi tutti gli elementi esistenti.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<TipoProvvedimentoVo[]> con la lista dei tipi provvedimento delle pratiche.
   */
  getTipiProvvedimentoWithPeriod(
    idAmbito: number,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<TipoIstanzaProvvedimentoVo[]> {
    // Definisco il flag provvedimento
    const flgProvvedimento = IdIstanzaProvvedimento.provvedimento;
    // Costruisco l'url di richiesta
    const PATH_AMBITI_ID = `${this.PATH_AMBITI}/${idAmbito}`;
    const url: string = this.appUrl(
      PATH_AMBITI_ID,
      this.PATH_TIPI_PROVVEDIMENTO_ISTANZA
    );
    // Definisco i query params per la chiamata
    const params = this.createQueryParams({
      flgIstanza: flgProvvedimento,
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Tramite servizio scarico i dati
    return this._http.get<TipoIstanzaProvvedimentoVo[]>(url, { params });
  }

  /**
   * #####################################
   * GESTIONE DELETE ISTANZA/PROVVEDIMENTI
   * #####################################
   */

  /**
   * Funzione di DELETE per un'oggetto istanza/provvedimento.
   * @param idIstanzaProvvedimento number che definisce l'id dell'istanza provvedimento da cancellare.
   * @returns Observable<IstanzaProvvedimentoVo> con il risultato della delete.
   */
  deleteIstanzaProvvedimento(
    idIstanzaProvvedimento: number
  ): Observable<IstanzaProvvedimentoVo> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(
      this.PATH_PROVVEDIMENTI_ISTANZE,
      idIstanzaProvvedimento
    );

    // Tramite servizio scarico i dati
    return this._http.delete<IstanzaProvvedimentoVo>(url);
  }

  /**
   * ####################
   * GESTIONE TIPI TITOLO
   * ####################
   */

  /**
   * Funzione di recupero per i tipi titolo.
   * Questa è la funzione standard per l'applicazione, senza filtri sulle date.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @returns Observable<TipoTitoloVo[]> con la lista dei tipi titolo delle pratiche.
   */
  getTipiTitolo(idAmbito: number): Observable<TipoTitoloVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._tipiTitolo?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._tipiTitolo);
    }

    // Tramite servizio scarico i dati
    return this.getTipiTitoloWithPeriod(idAmbito).pipe(
      tap((tipiTitolo: TipoTitoloVo[]) => {
        // Assegno localmente i codici utenza
        this._tipiTitolo = cloneDeep(tipiTitolo);
      })
    );
  }

  /**
   * Funzione di recupero per i tipi titolo.
   * @param idAmbito number che definisce l'id dell'ambito per la quale scaricare i dati.
   * @param dataInizioVal string che definisce la data inizio validatà. Se non definita, verranno presi tutti gli elementi esistenti.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<TipoTitoloVo[]> con la lista dei tipi titolo delle pratiche.
   */
  getTipiTitoloWithPeriod(
    idAmbito: number,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<TipoTitoloVo[]> {
    // Costruisco l'url di richiesta
    const PATH_AMBITI_ID = `${this.PATH_AMBITI}/${idAmbito}`;
    const url: string = this.appUrl(PATH_AMBITI_ID, this.PATH_TIPI_TITOLO);
    // Definisco i query params per la chiamata
    const params = this.createQueryParams({
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Tramite servizio scarico i dati
    return this._http.get<TipoTitoloVo[]>(url, { params });
  }

  /**
   * ############################
   * GESTIONE PROVINCE COMPETENZA
   * ############################
   */

  /**
   * Funzione che recupera dal server gli oggetti per le province di competenza.
   * @returns Observable<ProvinciaCompetenzaVo[]> con la response del server.
   */
  getProvinceCompetenza(): Observable<ProvinciaCompetenzaVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._provinceCompetenza?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._provinceCompetenza);
    }

    // PATH FISOO REGIONE PROVINCE
    const pathProvinceCompetenza = '/regione/01/province';
    // Creo l'url per la chiamata
    const url: string = this.appUrl(pathProvinceCompetenza);
    // Tramite servizio scarico i dati
    return this._http.get<ProvinciaCompetenzaVo[]>(url).pipe(
      tap((provinceCompetenza: ProvinciaCompetenzaVo[]) => {
        // Assegno localmente i codici utenza
        this._provinceCompetenza = cloneDeep(provinceCompetenza);
      })
    );
  }

  /**
   * Funzione che recupera dal server il codice utenza dato il codice riscossione provincia.
   * @param codRiscossioneProvincia string che definisce il codice delle pratica provincia per la ricerca.
   * @returns Observable<ProvinciaCompetenzaVo> con il codice utenza trovato.
   */
  getCodiceUtenzaByCodiceRiscossioneProvincia(
    codRiscossioneProvincia: string
  ): Observable<ProvinciaCompetenzaVo> {
    // Richiamo la funzione di scarico delle liste di codici utenza
    return this.getProvinceCompetenza().pipe(
      map((codiciUtenza: ProvinciaCompetenzaVo[]) => {
        // Ricerco nella lista per codice riscossione provincia
        return codiciUtenza?.find((cu) => {
          // Ricerco per stesso id
          return cu.sigla_provincia === codRiscossioneProvincia;
        });
      })
    );
  }

  /**
   * Funzione che contatta il server e verifica se esiste già una pratica basandosi sul codice utenza generato per ambito pratica.
   * @param codiceUtenzaByAmbito string che definisce il codice utenza per ambito.
   * @returns Observable<number> che identifica con 1 che il codice utenza è già presente, 0 che il codice utenza non è presente.
   */
  controllaCodiceUtenzaAmbito(
    codiceUtenzaByAmbito: string
  ): Observable<number> {
    // Genero l'url
    const url: string = this.appUrl(
      this.PATH_RISCOSSIONI,
      this.PATH_CODICE_RISCOSSIONE,
      this.PATH_SEPARATORE,
      codiceUtenzaByAmbito
    );

    // Richiamo la funzione del server
    return this._http.get<number>(url).pipe();
  }

  /**
   * #######################
   * GESTIONE NUMERO PRATICA
   * #######################
   */

  /**
   * Funzione che contatta il server e verifica se esiste già una pratica basandosi sul numero della pratica.
   * @param numeroPratica string che definisce il numero pratica.
   * @returns Observable<number> che identifica con 1 che il numero pratica è già presente, 0 che il numero pratica non è presente.
   */
  controllaNumeroPratica(numeroPratica: string | number): Observable<number> {
    // Effetuo una conversione del parametro di input
    numeroPratica = this._riscaUtilities.convertParamToString(numeroPratica);
    // Genero l'url
    const url: string = this.appUrl(
      this.PATH_RISCOSSIONI,
      this.PATH_NUMERO_PRATICA,
      this.PATH_SEPARATORE,
      numeroPratica
    );

    // Richiamo la funzione del server
    return this._http.get<number>(url);
  }

  /**
   * ############################################
   * GESTIONE VERIFY PER PRATICHE, STATI DEBITORI
   * ############################################
   */

  /**
   * Funzione di controllo che verifica se per il soggetto, per la quale viene passato l'id, ha delle pratiche collegate.
   * @param idOggetto number con l'id dell'oggetto da verificare, che sia pratica, soggetto, recapiti, o gruppo.
   * @param indTipoOggetto VerifyIndTipiOggetti che definisce la configurazione per il tipo di verifica da effettuare.
   * @param idTipoOper VerifyIndTipiOperazioni che definisce la configurazione per il tipo di verifica da effettuare.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<VerificaPraticheStDebitoriVo> con le pratiche o gli stati debitori correlati.
   */
  verifyRiscossioniStatiDebitori(
    idOggetto: number,
    indTipoOggetto: VerifyIndTipiOggetti,
    idTipoOper: VerifyIndTipiOperazioni,
    excludeIdRiscossione?: number
  ): Observable<VerificaPraticheStDebitoriVo> {
    // Definisco le informazioni per la generazione dell'url
    const path = this.PATH_VERIFY_RISCOSSIONI_STDEBITORI;
    const idO = idOggetto;
    const idRiscossione = excludeIdRiscossione;

    // Creo l'url tramite servizio
    const url: string = this.appUrl(path, idO);
    // Creo i query param
    const params = this.createQueryParams({
      indTipoOggetto,
      idTipoOper,
      idRiscossione,
    });

    // Richiamo il servizio
    return this._http.get<VerificaPraticheStDebitoriVo>(url, { params });
  }

  /**
   * Funzione di controllo che verifica se per il soggetto, per la quale viene passato l'id, ha delle pratiche o stati debitori collegati.
   * @param idSoggetto number con l'id del soggetto.
   * @param idTipoOper VerifyIndTipiOperazioni che definisce la configurazione per il tipo di verifica da effettuare.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<VerificaPraticheStDebitoriVo> con le pratiche o gli stati debitori correlati.
   */
  verifyPStDSoggetto(
    idSoggetto: number,
    idTipoOper: VerifyIndTipiOperazioni,
    excludeIdRiscossione?: number
  ): Observable<VerificaPraticheStDebitoriVo> {
    // Definisco le informazioni per la verify
    const idS = idSoggetto;
    const indTOgg = VerifyIndTipiOggetti.soggetto;
    const idTOper = idTipoOper;
    const exIdR = excludeIdRiscossione;

    // Richiamo e ritorno il risultato per la verifica
    return this.verifyRiscossioniStatiDebitori(idS, indTOgg, idTOper, exIdR);
  }

  /**
   * Funzione di controllo che verifica se per il gruppo, per la quale viene passato l'id, ha delle pratiche o stati debitori collegati.
   * @param idGruppo number con l'id del gruppo.
   * @param indTipoOggetto VerifyIndTipiOggetti.gruppoReferenteUpd | VerifyIndTipiOggetti.gruppoReferenteNotUpd che definisce se il gruppo della verifica ha o non ha il capogruppo modificato rispetto al suo stato iniziale.
   * @param idTipoOper VerifyIndTipiOperazioni che definisce la configurazione per il tipo di verifica da effettuare.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<VerificaPraticheStDebitoriVo> con le pratiche o gli stati debitori correlati.
   */
  verifyPStDGruppo(
    idGruppo: number,
    indTipoOggetto:
      | VerifyIndTipiOggetti.gruppoReferenteUpd
      | VerifyIndTipiOggetti.gruppoReferenteNotUpd,
    idTipoOper: VerifyIndTipiOperazioni,
    excludeIdRiscossione?: number
  ): Observable<VerificaPraticheStDebitoriVo> {
    // Definisco le informazioni per la verify
    const idG = idGruppo;
    const indTOgg = indTipoOggetto;
    const idTOper = idTipoOper;
    const exIdR = excludeIdRiscossione;

    // Richiamo e ritorno il risultato per la verifica
    return this.verifyRiscossioniStatiDebitori(idG, indTOgg, idTOper, exIdR);
  }

  /**
   * Funzione di controllo che verifica se per i gruppi, per la quale viene passato un array di id, hanno delle pratiche collegate.
   * @param idGruppi Array di number con gli id dei gruppi.
   * @param idTipoOper VerifyIndTipiOperazioni che definisce la configurazione per il tipo di verifica da effettuare.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IVerifyGruppiRes> con le pratiche o gli stati debitori correlati per ogni gruppo.
   */
  verifyPStDGruppi(
    idGruppi: number[],
    idTipoOper: VerifyIndTipiOperazioni,
    excludeIdRiscossione?: number
  ): Observable<IVerifyGruppiRes> {
    // Verifico l'input
    if (!idGruppi || idGruppi.length === 0) {
      // Ritorno una risposta vuota
      return of(undefined);
    }

    // Definisco le informazioni per la verify
    const indTOgg = VerifyIndTipiOggetti.gruppoReferenteNotUpd;
    const exIdR = excludeIdRiscossione;
    // Definisco un contenitore
    const verifyGruppi: IVerifyGruppiReq = {};

    // Ciclo gli id dei gruppi per il controllo
    idGruppi.forEach((idG: number) => {
      // Definisco l'id come stringa
      const idKey = idG.toString();
      // Aggingo all'oggetto di verifica la chiamta
      verifyGruppi[idKey] = this.verifyPStDGruppo(
        idG,
        indTOgg,
        idTipoOper,
        exIdR
      );
    });

    // Richiamo il servizio
    return forkJoin(verifyGruppi);
  }

  /**
   * Funzione di controllo che verifica se per il recapito principale, per la quale viene passato l'id, ha delle pratiche o stati debitori collegati.
   * @param idRecapitoA number con l'id del recapito principale.
   * @param idTipoOper VerifyIndTipiOperazioni che definisce la configurazione per il tipo di verifica da effettuare.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<VerificaPraticheStDebitoriVo> con le pratiche o gli stati debitori correlati.
   */
  verifyPStDRecapitoPrincipale(
    idRecapitoA: number,
    idTipoOper: VerifyIndTipiOperazioni,
    excludeIdRiscossione?: number
  ): Observable<VerificaPraticheStDebitoriVo> {
    // Definisco le informazioni per la verify
    const idRA = idRecapitoA;
    const indTOgg = VerifyIndTipiOggetti.recapitoPrincipale;
    const idTOper = idTipoOper;
    const exIdR = excludeIdRiscossione;

    // Richiamo e ritorno il risultato per la verifica
    return this.verifyRiscossioniStatiDebitori(idRA, indTOgg, idTOper, exIdR);
  }

  /**
   * Funzione di controllo che verifica se per il recapito alternativo, per la quale viene passato l'id, ha delle pratiche o stati debitori collegati.
   * @param idRecapitoA number con l'id del recapito alternativo.
   * @param idTipoOper VerifyIndTipiOperazioni che definisce la configurazione per il tipo di verifica da effettuare.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<VerificaPraticheStDebitoriVo> con le pratiche o gli stati debitori correlati.
   */
  verifyPStDRecapitoAlternativo(
    idRecapitoA: number,
    idTipoOper: VerifyIndTipiOperazioni,
    excludeIdRiscossione?: number
  ): Observable<VerificaPraticheStDebitoriVo> {
    // Definisco le informazioni per la verify
    const idRA = idRecapitoA;
    const indTOgg = VerifyIndTipiOggetti.recapitoAlternativo;
    const idTOper = idTipoOper;
    const exIdR = excludeIdRiscossione;

    // Richiamo e ritorno il risultato per la verifica
    return this.verifyRiscossioniStatiDebitori(idRA, indTOgg, idTOper, exIdR);
  }

  /**
   * #############################
   * GESTIONE RISCOSSIONI/PRATICHE
   * #############################
   */

  /**
   * Funzione che scarica tutte le pratiche disponibili.
   * @returns Observable<PraticaVo[]> con la lista di pratiche.
   */
  getPratiche(): Observable<PraticaVo[]> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_RISCOSSIONI);

    // Lancio la richiesta
    return this._http.get<PraticaVo[]>(url).pipe(
      map((pratiche: PraticaVo[]) => {
        // Eseguo il mapping
        pratiche.forEach((p: PraticaVo) => {
          // Recupero i gruppi per convertirli
          const gS = p.gruppo_soggetto as GruppoVo;
          // Ri-assegno i gruppi a seguito della conversione
          p.gruppo_soggetto = this._gruppoSoggetto.convertGruppoVoToGruppo(gS);
        });

        // Ritorno i valori mappati
        return pratiche;
      })
    );
  }

  /**
   * Funzione che scarica una pratica per l'id pratica in input.
   * @param idOrCodPratica number | string che definisce l'id pratica per la ricerca.
   * @returns Observable<PraticaVo> con pratica per l'id.
   */
  getPratica(idOrCodPratica: number): Observable<PraticaVo> {
    // Definisco l'url
    const url: string = this.appUrl(this.PATH_RISCOSSIONI, idOrCodPratica);

    // Effettuo la chiamata al servizio
    return this._http.get<PraticaVo>(url).pipe(
      map((pratica: PraticaVo) => {
        // Assegno localmente il gruppo per la conversione
        const g = pratica.gruppo_soggetto as GruppoVo;
        // Eseguo il mapping
        pratica.gruppo_soggetto =
          this._gruppoSoggetto.convertGruppoVoToGruppo(g);
        // Ritorno i valori mappati
        return pratica;
        // #
      })
    );
  }

  /**
   * Funzione che scarica una pratica e dati tecnici per l'id o il codice pratica in input.
   * @param idOrCodPratica number | string che definisce l'id o il codice pratica per la ricerca.
   * @returns Observable<PraticaEDatiTecnici> con la lista di pratiche.
   */
  getPraticaEDatiTecnici(
    idOrCodPratica: number
  ): Observable<PraticaEDatiTecnici> {
    // Scarico i dati della pratica
    return this.getPratica(idOrCodPratica).pipe(
      switchMap((praticaVo: PraticaVo) => {
        // Definisco un contenitore per la join dei dati
        const praticaEDT = {
          pratica: of(praticaVo),
          datiTecnici: this.getDatiTecnici(praticaVo.id_riscossione),
        };

        // Ritorno la forkJoin delle chiamate
        return forkJoin(praticaEDT);
      })
    );
  }

  /**
   * Funzione che verifica se la pratica in input risulta valida secondo alcuni criteri.
   * @param praticaVo PraticaVo da verificare.
   */
  private checkPratica(praticaVo: PraticaVo): boolean {
    // Verifico l'input
    if (!praticaVo) {
      // Niente da controllare, false
      return false;
    }

    // Variabili di comodo
    const method = 'checkPratica';

    // Verifico che la pratica abbia id_riscossione
    if (praticaVo.id_riscossione == undefined) {
      // Effettuo un log della validazione
      // this._logger.warning(method, 'id_riscossione undefined');
      // Controllo fallito
      return false;
    }

    // Validazione oke
    return true;
  }

  /**
   * Funzione che salva i dati della pratica della form.
   * @param praticaVo PraticaVo contenente i dati della pratica da salvare.
   * @returns Observable<PraticaVo> con i dati salvati.
   */
  insertPratica(praticaVo: PraticaVo): Observable<PraticaVo> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_RISCOSSIONI);

    // Eseguo il mapping del gruppo soggetto
    praticaVo.gruppo_soggetto = this._gruppoSoggetto.convertGruppoToGruppoVo(
      praticaVo.gruppo_soggetto as Gruppo
    );

    // Salvo i dati sul server
    return this._http.post<PraticaVo>(url, praticaVo).pipe(
      switchMap((pratica: PraticaVo) => {
        // Verifico la pratica ritornata dall'inserimento
        if (!this.checkPratica(pratica)) {
          // Check fallito, genero un errore
          const e = new RiscaServerError({
            error: {
              code: 'FE_500',
              title: 'checkPratica failed on data return',
            },
          });
          // Ritorno un errore
          return throwError(e);
          // #
        }

        // Ritorno la pratica
        return of(pratica);
      })
    );
  }

  /**
   * Funzione che aggiorna i dati della pratica della form.
   * @param praticaVo PraticaVo contenente i dati della pratica da salvare.
   * @returns Observable<PraticaVo> con i dati salvati.
   */
  updatePratica(praticaVo: PraticaVo) {
    // Eseguo il mapping del gruppo soggetto
    praticaVo.gruppo_soggetto = this._gruppoSoggetto.convertGruppoToGruppoVo(
      praticaVo.gruppo_soggetto as Gruppo
    );
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_RISCOSSIONI);

    // Salvo i dati sul server
    return this._http.put<PraticaVo>(url, praticaVo).pipe(
      switchMap((pratica: PraticaVo) => {
        // Verifico la pratica ritornata dall'inserimento
        if (!this.checkPratica(pratica)) {
          // Check fallito, genero un errore
          const e = new RiscaServerError({
            error: {
              code: 'FE_500',
              title: 'checkPratica failed on data return',
            },
          });
          // Ritorno un errore
          return throwError(e);
          // #
        }

        // Ritorno la pratica
        return of(pratica);
      })
    );
  }

  /**
   * Funzione che effettua la post dei dati tecnici al server.
   * @param praticaDTVo PraticaDTVo contenente i dati tecnici da salvare.
   * @returns Observable<PraticaDTVo> con la risposta del servizio.
   */
  insertDatiTecnici(praticaDTVo: PraticaDTVo): Observable<PraticaDTVo> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_DATI_TECNICI);
    // Lancio la chiamata per il salvataggio dati
    return this._http.post<PraticaDTVo>(url, praticaDTVo);
  }

  /**
   * Funzione che effettua la put dei dati tecnici al server.
   * @param praticaDTVo PraticaDTVo contenente i dati tecnici d'aggiornare.
   * @returns Observable<PraticaDTVo> con la risposta del servizio.
   */
  updateDatiTecnici(praticaDTVo: PraticaDTVo): Observable<PraticaDTVo> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_DATI_TECNICI);

    // Lancio la chiamata per il salvataggio dati
    return this._http.put<PraticaDTVo>(url, praticaDTVo).pipe();
  }

  /**
   * Funzione che effettua la get dei dati tecnici al server.
   * @param idRiscossione number che definisc un id_riscossione.
   * @returns Observable<PraticaDTVo> con la risposta del servizio.
   */
  getDatiTecnici(idRiscossione: number): Observable<PraticaDTVo> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(
      this.PATH_RISCOSSIONI,
      idRiscossione,
      this.PATH_DATI_TECNICI
    );

    // Lancio la chiamata per il salvataggio dati
    return this._http.get<PraticaDTVo>(url).pipe(
      map((p: PraticaDTVo) => {
        // Recupero la proprietà dei dati tecnici
        const dt = p?.riscossione?.dati_tecnici;
        // NOTA BENE: gestione inattesa del dato, lo forzo
        if (dt != undefined && typeof dt !== 'string') {
          // Stringhizzo i dati tecnici
          p.riscossione.dati_tecnici = JSON.stringify(dt);
          // #
        }
        // Ritorno la pratica manipolata
        return p;
        // #
      })
    );
  }

  /**
   * Funzione di recupero di un dato tecnici dato un id_riscossione.
   * La funzione ritornerà in maniera sincrona il risultato dello scarico dei dati.
   * @param idPratica number che definisce un id_riscossione.
   * @returns Promise<SoggettoVo> con il soggetto scaricato.
   */
  async getDatiTecniciSync(idPratica: number): Promise<PraticaDTVo> {
    // Richiamo la funzione di get del soggetto
    const praticaDTSearched = await this.getDatiTecnici(idPratica).toPromise();
    // Ritorno il soggetto ritornato
    return praticaDTSearched;
  }

  /**
   * Funzione di ricerca dei dati delle pratiche mediante un oggetto di ricerca: RiscossioneSearchV2Vo.
   * @param ricerca RiscossioneSearchV2Vo contenente i dati per la ricerca delle pratiche.
   * @returns Array di RiscossioneSearchResultVo con i dati restituiti dal server.
   */
  searchPratiche(
    ricerca: RiscossioneSearchV2Vo,
    paginazione?: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<RiscossioneSearchResultVo[]>> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_SEARCH_RISCOSSIONI);
    // Definisco i parametri per la ricerca
    let searchParams: any = {};

    // Recupero, se esiste, la modalita di ricerca definita nell'oggetto in input
    const codModRicerca = ricerca?.modalitaRicerca?.cod_modalita_ricerca;
    // Recupero il codice per sapere se il codice ricerca è: stato debitorio
    const codModSD = CodModalitaRicerca.statoDebitorio;
    // Verifico se la modalita di ricerca è quella dello stato debitorio
    if (codModRicerca == codModSD) {
      // Aggiungo il query param "modalitaRicerca"
      searchParams.modalitaRicerca = 'statoDebitorio';
      // #
    }

    // Definisco i query params per la chiamata
    const params = this.createPagingParams(paginazione, searchParams);
    // Definisco le options
    const options = { params };
    // Genero l'oggetto con i filtri di ricerca, formattato BE
    const filtri: IRiscossioneSearchV2Vo = ricerca.toServerFormat();

    // Lancio la chiamata per il recupero dati
    return this._httpHelper.searchWithPost<RiscossioneSearchResultVo[]>(
      url,
      filtri,
      options
    );
  }

  /**
   * Funzione di ricerca dei dati delle pratiche mediante l'id soggetto.
   * @param idSoggetto number contenente l'id soggetto per la quale ricercare le pratiche.
   * @returns Array di RiscossioneSearchResultVo con i dati restituiti dal server.
   */
  getPraticheSoggetto(
    idSoggetto: number
  ): Observable<RiscossioneSearchResultVo[]> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_SEARCH_RISCOSSIONI);

    // Genero un oggetto di ricerca definendo l'id in input
    const ricerca = new RiscossioneSearchV2Vo({ id_soggetto: idSoggetto });
    // Genero l'oggetto con i filtri di ricerca, formattato BE
    const filtri: IRiscossioneSearchV2Vo = ricerca.toServerFormat();

    // Lancio la chiamata per il recupero dati
    return this._http.post<RiscossioneSearchResultVo[]>(url, filtri).pipe();
  }

  /**
   * Funzione di ricerca dei dati delle pratiche mediante l'id gruppo.
   * @param idGruppo number contenente l'id gruppo per la quale ricercare le pratiche.
   * @returns Array di RiscossioneSearchResultVo con i dati restituiti dal server.
   */
  getPraticheGruppo(idGruppo: number): Observable<RiscossioneSearchResultVo[]> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_SEARCH_RISCOSSIONI);

    // Genero un oggetto di ricerca definendo l'id in input
    const ricerca = new RiscossioneSearchV2Vo({ id_gruppo: idGruppo });
    // Genero l'oggetto con i filtri di ricerca, formattato BE
    const filtri: IRiscossioneSearchV2Vo = ricerca.toServerFormat();

    // Lancio la chiamata per il recupero dati
    return this._http.post<RiscossioneSearchResultVo[]>(url, filtri).pipe();
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per il codice fiscale dell'user collegato.
   * @returns string con il codice fiscale dell'utente collegato.
   */
  get cf() {
    // Recupero il codice fiscale dell'utente
    return this._user.user.codFisc;
  }
}
