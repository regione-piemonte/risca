import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { clone, cloneDeep } from 'lodash';
import { forkJoin, Observable, of, Subject, throwError } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { RicercaPaginataResponse } from 'src/app/core/classes/http-helper/http-helper.classes';
import { ComponenteGruppo } from '../../../../../core/commons/vo/componente-gruppo-vo';
import { Gruppo } from '../../../../../core/commons/vo/gruppo-vo';
import { RecapitoVo } from '../../../../../core/commons/vo/recapito-vo';
import {
  IRicercaSoggettoVo,
  RicercaSoggetto,
} from '../../../../../core/commons/vo/ricerca-soggetto-vo';
import { SoggettoVo } from '../../../../../core/commons/vo/soggetto-vo';
import { ConfigService } from '../../../../../core/services/config.service';
import { HttpHelperService } from '../../../../../core/services/http-helper/http-helper.service';
import { HTTP_HELPER_OBSERVE_RESPONSE } from '../../../../../core/services/http-helper/utilities/http-helper.consts';
import { IJsonWarning } from '../../../../../core/services/http-helper/utilities/http-helper.interfaces';
import { LoggerService } from '../../../../../core/services/logger.service';
import { UserService } from '../../../../../core/services/user.service';
import { HttpUtilitiesService } from '../../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  GruppoEComponenti,
  IAlertCAFConfigs,
  ICallbackDataModal,
  ParseValueRules,
  RiscaAlertConfigs,
  RiscaContatto,
  RiscaDatiSoggetto,
  RiscaRecapito,
  RiscaServerError,
  RiscaTablePagination,
} from '../../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../../shared/utilities/enums/risca-notify-codes.enums';
import {
  AbilitaDASezioni,
  AbilitaDASoggetti,
  CodiceTipoSoggetto,
  FlagPubblico,
  FlagRicercaCL,
  RiscaServerStatus,
} from '../../../../../shared/utilities/enums/utilities.enums';
import { TipoNaturaGiuridica, TipoSoggettoVo } from '../../../../ambito/models';
import { AmbitoService } from '../../../../ambito/services';
import { GruppoSoggettoService } from '../gruppo-soggetto.service';
import { SoggettoDatiAnagraficiUtilityService } from '../soggetto-utility.service';
import { SoggettoHTTPResponse } from './utilities/soggetto-dati-anagrafici.interfaces';

@Injectable({ providedIn: 'root' })
export class SoggettoDatiAnagraficiService extends HttpUtilitiesService {
  /** String costante che definisce la chiave di gestione per le ricerche. */
  private OBSERVE_RESPONSE = HTTP_HELPER_OBSERVE_RESPONSE;

  /** Costante per il path: /soggetti */
  private PATH_SOGGETTI = '/soggetti';
  /** Costante per il path: /soggetti-qry */
  private PATH_SOGGETTI_QRY = '/soggetti-qry';
  /** Costante per il path: /tipi-natura-giuridica */
  private PATH_TIPI_NATURA_GIURIDICA = '/tipi-natura-giuridica';
  /** Costante per il path: /tipi-soggetto */
  private PATH_TIPI_SOGGETTO = '/tipi-soggetto';

  /** Subject che permette la gestione dell'evento: codice fiscale non salvato su RISCA. */
  onCFRiscaInvalido$ = new Subject<RiscaNotifyCodes>();

  /** Array di TipoSoggetto mantenuto in sessione per il recupero rapido delle informazioni. */
  private _tipiSoggetto: TipoSoggettoVo[] = [];
  /** Array di TipoNaturaGiuridica mantenuto in sessione per il recupero rapido delle informazioni. */
  private _tipiNaturaGiuridica: TipoNaturaGiuridica[] = [];

  /**
   * Costruttore
   */
  constructor(
    private _ambito: AmbitoService,
    config: ConfigService,
    private _gruppoSoggetto: GruppoSoggettoService,
    private _http: HttpClient,
    private _httpHelper: HttpHelperService,
    private _logger: LoggerService,
    riscaUtilities: RiscaUtilitiesService,
    private _soggettoUtility: SoggettoDatiAnagraficiUtilityService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * #####################################
   * FUNZIONI PER LA GESTIONE DELLE MODALI
   * #####################################
   */

  /**
   * Funzione che apre il modale per la ricerca di un titolare.
   * @param callbacks CallbackDataModal contenente le funzioni di callback del modale.
   */
  apriCercaTitolareModal(callbacks?: ICallbackDataModal) {
    // Richiamo la funzione del servizio di utility
    this._soggettoUtility.apriCercaTitolareModal(callbacks);
  }

  /**
   * ###########################
   * FUNZIONI DI PARSING OGGETTI
   * ###########################
   */

  /**
   * Funzione che genera un oggetto SoggettoVo dati in input le parti per la composizione dell'oggetto.
   * @param datiSoggetto RiscaDatiSoggetto con i dati soggetto.
   * @param recapiti Array di RecapitoVo con i recapiti del soggetto.
   * @returns SoggettoVo con le informazioni dei dati anagrafici.
   */
  convertDatiSoggettoFormToSoggettoVo(
    datiSoggetto: RiscaDatiSoggetto,
    recapiti: RecapitoVo[]
  ): SoggettoVo {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.convertDatiSoggettoFormToSoggettoVo(
      datiSoggetto,
      recapiti
    );
  }

  /**
   * Funzione di parsing da un oggetto RiscaDatiSoggetto ad un oggetto compatibile con SoggettoVo.
   * @param datiSoggetto RiscaDatiSoggetto da convertire.
   * @returns SoggettoVo contenente i dati del soggetto.
   */
  convertRiscaDatiSoggettoToSoggettoVo(
    datiSoggetto: RiscaDatiSoggetto
  ): SoggettoVo {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.convertRiscaDatiSoggettoToSoggettoVo(
      datiSoggetto
    );
  }

  /**
   * Funzione di convert dei dati front-end per un recapito in un oggetto compatibile con SoggettoVo.
   * @param recapito RiscaRecapito da convertire.
   * @param contatti RiscaContatto da convertire.
   * @returns Oggetto parziale SoggettoVo convertito.
   */
  convertRiscaRecapitoToSoggettoVo(
    recapito: RiscaRecapito,
    contatti: RiscaContatto
  ): SoggettoVo {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.convertRiscaRecapitoToSoggettoVo(
      recapito,
      contatti
    );
  }

  /**
   * #################
   * CHIAMATE ALLE API
   * #################
   */

  /**
   * Funzione di recupero di un soggetto dato un id_soggetto.
   * @param idSoggetto number che definisce un id_soggetto.
   * @returns Observable<SoggettoVo> con la richiesta di scarico di un soggetto.
   */
  getSoggetto(idSoggetto: number): Observable<SoggettoVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_SOGGETTI, idSoggetto);

    // Lancio la chiamata per il recupero dati
    return this._http.get<SoggettoVo>(url).pipe(
      map((soggettoVo: SoggettoVo) => {
        // Aggiorno i dati dei gruppi del soggetto
        return this.convertSoggettoGruppiVoToGruppi(soggettoVo);
        // #
      })
    );
  }

  /**
   * Fuzione che effettua la ricerca di un soggetto sul db.
   * La chiave è composta dai tre parametri in input.
   * @param idAmbito number id ambito dell'utente.
   * @param idTipoSoggetto number id tipo soggetto dell'utente.
   * @param cfSoggetto string codice fiscale dell'utente.
   * @returns Observable<RicercaSoggetto> con i dati relativi alla ricerca soggetto.
   */
  cercaSoggetto(
    idAmbito: number,
    idTipoSoggetto: number,
    cfSoggetto: string
  ): Observable<RicercaSoggetto> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_SOGGETTI);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      idAmbito,
      idTipoSoggetto,
      cfSoggetto,
    });

    // Lancio la chiamata per il recupero dati
    return this._http.get<IRicercaSoggettoVo>(url, { params }).pipe(
      catchError((e: RiscaServerError) => {
        // Definisco codice ed errore gestibili
        const s = RiscaServerStatus.notFound;
        const c = RiscaNotifyCodes.A008;
        // Verifico se l'errore è stato generato perché non è stato trovato un soggetto
        const sNotFound = this._riscaUtilities.isServerErrorManageable(e, s, c);
        // Verifico il risultato
        if (sNotFound) {
          // Posso continuare il flusso dati
          return of(undefined);
          // #
        } else {
          // E' un altro tipo di errore
          return throwError(e);
        }
        // #
      }),
      map((r: IRicercaSoggettoVo) => {
        // Converto l'oggetto ricerca
        const ricercaSoggettoVo = new RicercaSoggetto(r);
        // Gestico la proprietà gruppo_soggetto
        ricercaSoggettoVo.soggetto = this.convertSoggettoGruppiVoToGruppi(
          ricercaSoggettoVo.soggetto
        );
        // Ritorno l'oggetto ricerca
        return ricercaSoggettoVo;
        // #
      }),
      tap((r: RicercaSoggetto) => {
        // Log di tracciamento
        this._logger.success('cercaSoggetto post map', r);
        // #
      })
    );
  }

  /**
   * Fuzione che effettua la ricerca dei soggetti sul db.
   * Se vengono trovati dei soggetti, verranno ritornati in un array, altrimenti undefined.
   * @param idAmbito number id ambito dell'utente.
   * @param campoRicerca string che definisce la ricerca da applicare a tutti i filtri.
   * @returns Observable<RicercaPaginataResponse> con i soggetti o undefined ed i dati della paginazione.
   */
  cercaSoggettiCampoLibero(
    idAmbito: number,
    campoRicerca: string,
    paginazione: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<SoggettoVo[]>> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_SOGGETTI_QRY);

    // Ottengo i parametri della paginazione
    const pagingParams =
      this._riscaUtilities.createPagingParamsItem(paginazione);
    const queryParams = { idAmbito, campoRicerca };

    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      ...queryParams,
      ...pagingParams,
    });

    // Lancio la chiamata per il salvataggio dati
    return this._http
      .get<SoggettoVo[]>(url, {
        params,
        observe: 'response', // Permette di avere la response intera e leggere gli header
      })
      .pipe(
        map((response: HttpResponse<SoggettoVo[]>) => {
          // Ottengo l'oggetto con il risultato e la paginazione
          const ricercaPaginata =
            this._httpHelper.ricercaPaginataResponse<SoggettoVo[]>(response);
          // Verifico se sono stati trovati soggetti
          if (!this.checkSoggetti(ricercaPaginata.sources as SoggettoVo[])) {
            // Ritorno un array vuoto
            ricercaPaginata.sources = [];
            // #
          }
          // L'array è valido, lo ritorno
          return ricercaPaginata;
        })
      );
  }

  /**
   * Fuzione che effettua il controllo di presenza di un soggetto sul db.
   * La chiave è composta dai tre parametri in input.
   * Se viene trovato un soggetto, verrà ritornato un oggetto SoggettoVo con le proprietà, altrimenti un oggetto vuoto.
   * La funzione verificherà e ritornerà un boolean.
   * @param idAmbito number id ambito dell'utente.
   * @param idTipoSoggetto number id tipo soggetto dell'utente.
   * @param cfSoggetto string codice fiscale dell'utente.
   * @returns boolean che definisce se esiste (true) o non esiste (false) un soggetto.
   */
  controllaSoggetto(
    idAmbito: number,
    idTipoSoggetto: number,
    cfSoggetto: string
  ): Observable<boolean> {
    // Lancio la chiamata per il salvataggio dati
    return this.cercaSoggetto(idAmbito, idTipoSoggetto, cfSoggetto).pipe(
      map((v: RicercaSoggetto) => {
        // Recupero il soggetto dalla ricerca
        const { soggetto } = v;
        // Modifico la response e la rimappo su un boolean
        return this.checkSoggetto(soggetto);
        // #
      })
    );
  }

  /**
   * Funzione che effettua la post (inserimento) dei dati anagrafici al server.
   * @param soggetto SoggettoVo contenente i dati anagrafici da salvare.
   * @returns Observable<SoggettoHTTPResponse> con la risposta del servizio.
   */
  insertDASoggetto(soggetto: SoggettoVo): Observable<SoggettoHTTPResponse> {
    // Effettuo una sanitizzazione per i dati
    this._riscaUtilities.sanitizeFEProperties(soggetto);
    // Converto i dati del gruppo
    soggetto = this.convertSoggettoGruppiToGruppiVo(soggetto);

    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_SOGGETTI);
    // Aggiungo all'oggetto delle option il parametro per la gestione della ricerca
    const options: any = { observe: this.OBSERVE_RESPONSE };

    // Lancio la chiamata per il salvataggio dati
    return this._http.post<SoggettoVo>(url, soggetto, options).pipe(
      map((res: HttpResponse<SoggettoVo>) => {
        // Converto la response e genero l'oggetto per la paginazione
        let soggettoRes: SoggettoHTTPResponse;
        soggettoRes = this.soggettoFromHTTPResponse(res);
        // Analizzo i possibili errori
        // this.checkJsonWarning(soggettoRes.jsonWarning);

        // Ritorno i dati del soggetto
        return soggettoRes;
      })
    );
  }

  /**
   * Funzione che effettua la put (modifica) dei dati anagrafici al server.
   * @param soggetto SoggettoVo contenente i dati anagrafici da salvare.
   * @param indModManuale boolean che specifica se la modifica del soggetto deve risultare con gli indirizzi spedizione editati manualmente.
   * @returns Observable<SoggettoHTTPResponse> con la risposta del servizio.
   */
  updateDASoggetto(
    soggetto: SoggettoVo,
    indModManuale?: boolean
  ): Observable<SoggettoHTTPResponse> {
    // Effettuo una sanitizzazione per i dati
    this._riscaUtilities.sanitizeFEProperties(soggetto);
    // Converto i dati del gruppo
    soggetto = this.convertSoggettoGruppiToGruppiVo(soggetto);

    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_SOGGETTI);
    // Aggiungo all'oggetto delle option il parametro per la gestione della ricerca
    const options: any = { observe: this.OBSERVE_RESPONSE };

    // Verifico se esiste il queryparam
    if (indModManuale) {
      // Definisco i query params per la chiamata
      const params = this.createQueryParams({ indModManuale: 1 });
      // Definisco la request con il queryparam
      options.params = params;
    }

    // Lancio la chiamata per il salvataggio dati
    return this._http.put<SoggettoVo>(url, soggetto, options).pipe(
      map((res: HttpResponse<SoggettoVo>) => {
        // Converto la response e genero l'oggetto per la paginazione
        let soggettoRes: SoggettoHTTPResponse;
        soggettoRes = this.soggettoFromHTTPResponse(res);
        // Analizzo i possibili errori
        // this.checkJsonWarning(soggettoRes.jsonWarning);

        // Ritorno i dati del soggetto
        return soggettoRes;
      })
    );
  }

  /**
   * Funzione di gestione della response di ritorno per POST e PUT soggetto.
   * Dalla risposta HTTP completa verranno estratti i dati del soggetto e altre informazioni utilit per l'applicativo.
   * @param response HttpResponse<SoggettoVo> con la risposta alla chiamata HTTP.
   */
  private soggettoFromHTTPResponse(
    response: HttpResponse<SoggettoVo>
  ): SoggettoHTTPResponse {
    // Estraggo l'informazione dal body come SoggettoVo
    const body: SoggettoVo = { ...response.body! };

    // Estraggo le informazioni dall'header della response per: saveCFScriva
    const jsonWarn: IJsonWarning = this._httpHelper.getJsonWarning(response);

    // Creo l'oggetto di ritorno per il soggetto
    const soggettoHTTPRes: SoggettoHTTPResponse = {
      soggetto: body,
      jsonWarning: jsonWarn,
    };

    // Restituisco l'oggetto formattato
    return soggettoHTTPRes;
  }

  /**
   * Funzione che verifica se sono avvenuti errori non bloccanti per i soggetti.
   * @param jsonWarn IJsonWarning con le informazioni riguardanti i warning.
   */
  private checkJsonWarning(jsonWarn: IJsonWarning) {
    // Verifico l'input
    if (!jsonWarn) {
      // Nessuna segnalazione
      return;
    }

    // Recupero il codice del warning
    const { code } = jsonWarn;
    // Effettuo uno switch e gestisco i possibili warning
    switch (code) {
      case RiscaNotifyCodes.I025:
        // Codice fiscale non valido per scriva, emetto l'evento
        this.onCFRiscaInvalido();
        break;
    }
  }

  /**
   * Funzione che effettua la delete (eliminazione) dei dati anagrafici al server.
   * @param idSoggetto number che definisce l'id del soggetto da cancellare.
   * @returns Observable<SoggettoVo> con la risposta del servizio.
   */
  eliminaDASoggetto(idSoggetto: number): Observable<SoggettoVo> {
    // Definisco l'url per il collegamento al servizio del server
    const url = this.appUrl(this.PATH_SOGGETTI, idSoggetto);
    // Lancio la chiamata per la cancellazione
    return this._http.delete<SoggettoVo>(url);
  }

  /**
   * Fuzione che effettua la ricerca dei gruppi sul db.
   * Se vengono trovati dei gruppi, verranno scaricate le informazioni anche dei soggetti che compongono la lista dei componenti gruppo.
   * @param idAmbito number id ambito dell'utente.
   * @param campoRicerca string che definisce la ricerca da applicare a tutti i filtri.
   * @param target FlagRicercaCL che definisce il flag di ricerca per i gruppi.
   * @param onlyCapogruppo boolean che definisce se è da scaricare il dettaglio del solo soggetto capogruppo. Default è: false.
   * @returns Observable<RicercaPaginataResponse> con i gruppi e i relativi componenti (come dato completo) e i dati della paginazione.
   */
  cercaGruppiEComponentiCampoLibero(
    idAmbito: number,
    campoRicerca: string,
    target: FlagRicercaCL,
    onlyCapogruppo: boolean = false,
    paginazioneGruppi: RiscaTablePagination = undefined
  ): Observable<RicercaPaginataResponse<GruppoEComponenti[]>> {
    // Variabili di comodo
    const method = 'cercaGruppiEComponentiCampoLibero';

    // Vado a richiamare la funzione di ricerca gruppi
    return this._gruppoSoggetto
      .cercaGruppiCampoLibero(idAmbito, campoRicerca, target, paginazioneGruppi)
      .pipe(
        switchMap((result: RicercaPaginataResponse<Gruppo[]>) => {
          // Ottengo i gruppi
          const gruppi = result.sources;
          const paging = result.paging;
          let sources = [];

          // Verifico che sia tornato un valore e abbia almeno un elemento
          if (!gruppi || gruppi.length === 0) {
            // Ritorno un array vuoto
            return of(
              new RicercaPaginataResponse<GruppoEComponenti[]>({
                sources,
                paging,
              })
            );
            // #
          } else {
            // Richiamo la funzione di scarico dati componenti per i gruppi
            return this.getDettaglioComponentiPerGruppi(
              gruppi,
              onlyCapogruppo
            ).pipe(
              // Mappo il risultato della ricerca dei componenti del gruppo in modo da metterlo dentro la response con la paginazione
              map((gruppi: GruppoEComponenti[]) => {
                // Assegno i gruppi all'oggetto di ritorno
                sources = gruppi;
                // Restituisco l'oggetto per intero
                return new RicercaPaginataResponse<GruppoEComponenti[]>({
                  sources,
                  paging,
                });
              })
            );
          }
        })
      );
  }

  /**
   * Funzione che scarica, dato un array di gruppi, tutti i dati dei componenti di ogni gruppo.
   * @param gruppi Array di Gruppo per lo scarico dati.
   * @param onlyCapogruppo boolean che definisce se è da scaricare il dettaglio del solo soggetto capogruppo. Default è: false.
   * @returns Observable<GruppoEComponenti[]> con l'array di oggetti composto da gruppo e relativi soggetti/componenti gruppo.
   */
  getDettaglioComponentiPerGruppi(
    gruppi: Gruppo[],
    onlyCapogruppo: boolean = false
  ): Observable<GruppoEComponenti[]> {
    // Verifico se non ci sono gruppi nell'array
    if (!gruppi || gruppi.length === 0) {
      // Ritorno array vuoto
      return of([]);
    }

    // Genero una richiesta per ogni componente
    const gReq: Observable<GruppoEComponenti>[] = [];
    // Popolo l'array con le richieste di scarico.
    gruppi.forEach((g: Gruppo) => {
      // Per ogni gruppo effettuo una richiesta
      gReq.push(this.getDettaglioComponentiPerGruppo(g, onlyCapogruppo));
    });

    // Verifico che l'array di request abbia valori
    if (gReq.length === 0) {
      // Ritorno un of con array vuoto
      return of([]);
    }

    // Richiamo la forkjoin per lo scarico dei dati
    return forkJoin(gReq);
  }

  /**
   * Funzione che scarica, dato un gruppo, tutti i dati dei componenti del gruppo.
   * @param gruppo Gruppo per lo scarico dati.
   * @param onlyCapogruppo boolean che definisce se è da scaricare il dettaglio del solo soggetto capogruppo. Default è: false.
   * @returns Observable<GruppoEComponenti> con l'oggetto composto da gruppo e relativi soggetti/componenti gruppo.
   */
  getDettaglioComponentiPerGruppo(
    gruppo: Gruppo,
    onlyCapogruppo: boolean = false
  ): Observable<GruppoEComponenti> {
    // Variabile di comodo
    const method = 'getDettaglioComponentiPerGruppo';

    // Verifico che esista il gruppo
    if (!gruppo) {
      // Variabili di comodo
      const m = `${method} | gruppo undefined`;
      // Loggo la mancaza del gruppo
      this._logger.error(m, gruppo);

      // Variabili di comodo
      const d = { gruppo };
      // Creo un d'errore come quello del server
      const e = this._riscaUtilities.customResponseError(method, null, d);
      // Ritorno un errore
      return throwError(e);
    }

    // Estraggo dal gruppo la lista dei componenti
    const cig = gruppo.componenti_gruppo || [];

    // Richiamo la forkjoin per lo scarico dei dati
    return this.getDettaglioComponenti(cig, onlyCapogruppo).pipe(
      map((soggetti: SoggettoVo[]) => {
        // Genero l'oggetto di ritorno
        const gec = new GruppoEComponenti(gruppo, soggetti);
        // Ritorno l'oggetto
        return gec;
      })
    );
  }

  /**
   * Funzione che scarica, dato un id gruppo, l'oggetto gruppo e tutti i dati dei componenti del gruppo.
   * @param idGruppo number con l'id gruppo per lo scarico dati.
   * @param onlyCapogruppo boolean che definisce se è da scaricare il dettaglio del solo soggetto capogruppo. Default è: false.
   * @returns Observable<GruppoEComponenti> con l'oggetto composto da gruppo e relativi soggetti/componenti gruppo.
   */
  getGruppoEDettaglioComponenti(
    idGruppo: number,
    onlyCapogruppo: boolean = false
  ): Observable<GruppoEComponenti> {
    // Lancio la chiamata alla getGruppo
    return this._gruppoSoggetto.getGruppo(idGruppo).pipe(
      switchMap((gruppo: Gruppo) => {
        // Richiamo la funzione di scarico dati per i componenti
        return this.getDettaglioComponentiPerGruppo(gruppo, onlyCapogruppo);
      })
    );
  }

  /**
   * Funzione che scarica, dato un array di ComponenteGruppo, tutti i dettagli dei componenti del gruppo.
   * @param componenti Array di ComponenteGruppo per lo scarico dei dati di dettaglio.
   * @param onlyCapogruppo boolean che definisce se è da scaricare il dettaglio del solo soggetto capogruppo. Default è: false.
   * @returns Observable<SoggettoVo[]> con l'array di dettagli definiti da oggetti SoggettoVo, per i componenti.
   */
  getDettaglioComponenti(
    componenti: ComponenteGruppo[],
    onlyCapogruppo: boolean = false
  ): Observable<SoggettoVo[]> {
    // Estraggo dal gruppo la lista dei componenti
    const cig = componenti || [];
    // Verifico ci sia almeno un complemente nel gruppo
    if (cig.length === 0) {
      // Nessun componente, ritorno array vuoto
      return of([]);
    }

    // Genero una richiesta per ogni componente
    const cReq: Observable<SoggettoVo>[] = [];
    // Popolo l'array con le richieste di scarico.
    cig.forEach((c: ComponenteGruppo) => {
      // Recupero l'id_soggetto
      const idSoggetto = c.id_soggetto;
      // Definisco la condizione per aggiungere il dato del soggetto
      const addS = !onlyCapogruppo || (onlyCapogruppo && c.flg_capo_gruppo);

      // Verifico se sono richiesti solo i dati del capogruppo
      if (addS) {
        // Definisco la chiave come id e la richiesta come value
        cReq.push(this.getSoggetto(idSoggetto));
      }
    });

    // Verifico se esiste almeno una richiesta all'interno dell'array
    if (cReq.length === 0) {
      // Non ci sono elementi da ricercare
      return of([]);
      // #
    }

    // Richiamo la forkjoin per lo scarico dei dati
    return forkJoin(cReq);
  }

  /**
   * #######################################
   * CHIAMATE API COLLEGATE AI DATI SOGGETTO
   * #######################################
   */

  /**
   * Getter per tipi soggetto
   * @param forceDownload boolean che forza lo scarico dei dati della funzione anche se ci sono dati nel servizio. Default: false;
   * @returns Array di tipi soggetto
   */
  getTipiSoggetto(forceDownload = false): Observable<TipoSoggettoVo[]> {
    // Recupero il dato dal servizio
    const tipiSoggetto = this.tipiSoggetto;
    // Effettuo un check per lo scarico dati
    const useLocal = tipiSoggetto?.length > 0 && !forceDownload;

    // Verifico se esiste il dato nel servizio
    if (useLocal) {
      // Ritorno il dato del servizio
      return this._riscaUtilities.responseWithDelay(tipiSoggetto);
      // #
    } else {
      // Creo l'url per la chiamata
      const url = this.appUrl(this.PATH_TIPI_SOGGETTO);
      // Tramite servizio scarico i dati
      return this._http.get<TipoSoggettoVo[]>(url).pipe(
        tap((res: TipoSoggettoVo[]) => {
          // Assegno localmente il risultato
          this._tipiSoggetto = clone(res);
        })
      );
    }
  }

  /**
   * Getter per nature giuridiche.
   * @returns Observable<TipoNaturaGiuridica[]> con la lista delle nature giuridiche presenti su DB.
   */
  getTipiNaturaGiuridica(): Observable<TipoNaturaGiuridica[]> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_TIPI_NATURA_GIURIDICA);

    // Richiamo il servizio
    return this._http.get<TipoNaturaGiuridica[]>(url).pipe(
      tap((tng: TipoNaturaGiuridica[]) => {
        // Assegno localmente la risorsa
        this._tipiNaturaGiuridica = tng;
      })
    );
  }

  /**
   * Getter per natura giuridica filtrate per il tipo soggetto.
   * @param idTipoSoggetto string che definisce l'id del tipo soggetto.
   * @returns Observable<TipoNaturaGiuridica[]> filtrate per tipo soggetto.
   */
  getTipiNaturaGiuridicaByTipoSoggetto(
    idTipoSoggetto?: string
  ): Observable<TipoNaturaGiuridica[]> {
    // Ritorno il dato del servizio
    return this._riscaUtilities
      .responseWithDelay(this.tipiNaturaGiuridica)
      .pipe(
        map((tng: TipoNaturaGiuridica[]) => {
          // Se idTipoSoggetto è di natura fisica, non devo ritornare niente
          if (!idTipoSoggetto || idTipoSoggetto === CodiceTipoSoggetto.PF) {
            // Secondo le direttive progettuali non bisogna tornare nature giuridiche per una persona fisica
            return [];
          }

          // Verifico la tipologia di persona giuridica
          return tng.filter((ng: TipoNaturaGiuridica) => {
            // Verifico esista idTipoSoggetto
            if (!idTipoSoggetto) {
              // Ritorno l'oggetto
              return true;
            }

            // Verifico le casistiche di ritorno
            const personaGiuridicaPrivata =
              idTipoSoggetto === CodiceTipoSoggetto.PG &&
              ng.flg_pubblico === FlagPubblico.NO;
            const personaGiuridicaPubblica =
              idTipoSoggetto === CodiceTipoSoggetto.PB &&
              ng.flg_pubblico === FlagPubblico.SI;

            // Ritorno il boolean di verifica
            return personaGiuridicaPrivata || personaGiuridicaPubblica;
          });
        })
      );
  }

  /**
   * ##########################################
   * FUNZIONI DI CONVERSIONE SOGGETTO => GRUPPI
   * ##########################################
   */

  /**
   * Funzione di convert che gestisce la struttura dati del SoggettoVo in input, convertendo l'eventuale array gruppo_soggetto, da GruppoVo[] a Gruppo[].
   * @param soggettoVo SoggettoVo per la conversione.
   * @returns SoggettoVo aggiornato.
   */
  convertSoggettoGruppiVoToGruppi(soggettoVo: SoggettoVo): SoggettoVo {
    // Verifico l'input
    if (!soggettoVo) {
      // Blocco il flusso
      return soggettoVo;
    }
    // Ritorno la funzione di conversione
    return this._soggettoUtility.convertSoggettoGruppiVoToGruppi(soggettoVo);
  }

  /**
   * Funzione di convert che gestisce la struttura dati del SoggettoVo in input, convertendo l'eventuale array gruppo_soggetto, da Gruppo[] a GruppoVo[].
   * @param soggettoVo SoggettoVo per la conversione.
   * @returns SoggettoVo aggiornato.
   */
  convertSoggettoGruppiToGruppiVo(soggettoVo: SoggettoVo): SoggettoVo {
    // Verifico l'input
    if (!soggettoVo) {
      // Blocco il flusso
      return soggettoVo;
    }
    // Ritorno la funzione di conversione
    return this._soggettoUtility.convertSoggettoGruppiToGruppiVo(soggettoVo);
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che compone i messaggi per l'alert di gestione dei campi aggiornati alla fonte.
   * @param labels Array di string che contiene tutti i dati per i messaggi.
   */
  messaggiAlertCampiAggiornatiFonte(labels: string[]): string[] {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.messaggiAlertCampiAggiornatiFonte(labels);
  }

  /**
   * Funzione che verifica la response per i dati di un soggetto.
   * @param response Array di SoggettoVo da verificare.
   * @returns boolean che definisce se tutta la lista è composta da oggetti vuoti.
   */
  private checkSoggetti(response: SoggettoVo[]): boolean {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.checkSoggetti(response);
  }

  /**
   * Funzione che verifica la response per i dati di un soggetto.
   * @param response SoggettoVo da verificare.
   * @returns boolean che definisce se un soggetto esiste.
   */
  private checkSoggetto(response: SoggettoVo): boolean {
    // Richiamo il controllo del servizio
    return !this._riscaUtilities.isEmptyObject(response);
  }

  /**
   * Funzione di utility che recupera il soggetto referente o capogruppo, da un oggetto GruppoEComponenti.
   * @param gec GruppoEComponenti dalla quale estrarre i dati.
   * @returns SoggettoVo con i dati del referente/capogruppo.
   */
  getCapogruppoFromGruppoEComponenti(gec: GruppoEComponenti): SoggettoVo {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.getCapogruppoFromGruppoEComponenti(gec);
  }

  /**
   * Funzione che recupera le informazioni dei recapiti alternativi fusi tra un soggetto 1 e un soggetto 2.
   * I recapiti del soggetto 2, definiranno il valore delle proprietà comuni.
   * @param s1 SoggettoVo dalla quale estrarre i dati dei recapiti.
   * @param s2 SoggettoVo dalla quale estrarre i dati dei recapiti.
   * @returns Array di RecapitoVo con le informazioni mergiate.
   */
  mergeRecapitiSoggetti(s1: SoggettoVo, s2: SoggettoVo): RecapitoVo[] {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.mergeRecapitiSoggetti(s1, s2);
  }

  /**
   * Funzione che mergia i dati comuni agli array in input.
   * I dati del secondo array, definiranno il valore delle proprietà comuni.
   * @param r1 Array di RecapitoVo per il merge.
   * @param r2 Array di RecapitoVo per il merge.
   * @returns Array di RecapitoVo con le informazioni mergiate.
   */
  mergeArrayRecapiti(r1: RecapitoVo[], r2: RecapitoVo[]): RecapitoVo[] {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.mergeArrayRecapiti(r1, r2);
  }

  /**
   * Funzione che mergia i dati dei recapiti principali dagli array in input.
   * I dati del secondo array, definiranno il valore delle proprietà comuni.
   * @param r1 Array di RecapitoVo per il merge.
   * @param r2 Array di RecapitoVo per il merge.
   * @returns RecapitoVo con le informazioni mergiate.
   */
  mergeRecapitiPrincipaliDaRecapiti(
    r1: RecapitoVo[],
    r2: RecapitoVo[]
  ): RecapitoVo[] {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.mergeRecapitiPrincipaliDaRecapiti(r1, r2);
  }

  /**
   * Funzione che verifica se un soggetto ha le stesse informazioni di un altro soggetto.
   * @param s1 SoggettoVo da verificare.
   * @param s2 SoggettoVo da verificare.
   * @param parseRules ParseValueRules che definisce le logiche di sanitizzazione degli oggetti.
   * @returns boolean che definisce se i soggetti in input hanno le stesse informazioni.
   */
  compareSoggetti(
    s1: SoggettoVo,
    s2: SoggettoVo,
    parseRules?: ParseValueRules
  ): boolean {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.compareSoggetti(s1, s2, parseRules);
  }

  /**
   * Funzione che mergia i dati tra due soggetti.
   * ATTENZIONE: se i soggetti hanno proprietà comuni, verranno mantenute quelle dell'oggetto s2.
   * @param s1 SoggettoVo da mergiare.
   * @param s2 SoggettoVo da mergiare.
   * @returns SoggettoVo con i dati mergiati.
   */
  mergeSoggetti(s1: SoggettoVo, s2: SoggettoVo): SoggettoVo {
    // Richiamo la funzione del servizio di utility
    const sm = this._soggettoUtility.mergeSoggetti(s1, s2);

    // Ritorno le informazioni mergiate
    return sm;
  }

  /**
   * Funzione che genera un RiscaAlertConfigs con i dati per i campi aggiornati dalla fonte.
   * @param configs IAlertCAFConfigs con le configurazioni dell'oggetto.
   * @returns RiscaAlertConfigs con le informazioni dell'alert per la visualizzazione dei campi aggiornati dalla fonte.
   */
  alertCampiAggiornatiFonte(configs: IAlertCAFConfigs): RiscaAlertConfigs {
    // Richiamo la funzione del servizio di utility
    return this._soggettoUtility.alertCampiAggiornatiFonte(configs);
  }

  /**
   * Funzione di comodo che rimuove i metadati da un oggetto: RecapitoVo.
   * @param recapito RecapitoVo da pulire dai metadati.
   * @returns RecapitoVo senza i metadati di RISCA.
   */
  clearRiscaMDRecapito(recapito: RecapitoVo): RecapitoVo {
    // Verifico se esiste l'oggetto
    if (recapito) {
      // Rimuovo i metadati dall'oggetto
      delete recapito._risca_id_recapito;
    }

    // Ritorno l'oggetto
    return recapito;
  }

  /**
   * ###################################
   * FUNZIONI DI SET AUTOMATICI PER FORM
   * ###################################
   */

  /**
   * Funzione che estrae e imposta come valore di default un tipo soggetto all'interno di un form.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param listaTipiSoggetto Array di TipoSoggetto dalla quale cercare il default.
   * @param codTipoSoggetto string che definisce il codice tipo soggetto da ricercare.
   */
  setTipoSoggettoDefault(
    f: FormGroup,
    fcn: string,
    listaTipiSoggetto: TipoSoggettoVo[],
    codTipoSoggetto: string
  ) {
    // Richiamo la funzione del servizio di utility
    this._soggettoUtility.setTipoSoggettoDefault(
      f,
      fcn,
      listaTipiSoggetto,
      codTipoSoggetto
    );
  }

  /**
   * Funzione che estrae e imposta come valore di default un tipo soggetto all'interno di un form.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param listaNatureGiuridiche Array di TipoNaturaGiuridica dalla quale cercare il default.
   * @param naturaGiuridica TipoNaturaGiuridica che definisce la natura giuridica da ricercare.
   */
  setNaturaGiuridicaDefault(
    f: FormGroup,
    fcn: string,
    listaNatureGiuridiche: TipoNaturaGiuridica[],
    naturaGiuridica: TipoNaturaGiuridica
  ) {
    // Richiamo la funzione del servizio di utility
    this._soggettoUtility.setNaturaGiuridicaDefault(
      f,
      fcn,
      listaNatureGiuridiche,
      naturaGiuridica
    );
  }

  /**
   * ###############################
   * GESTIONE DI PROPAGAZIONE EVENTI
   * ###############################
   */

  /**
   * Funzione che emette l'evento per il codice I025.
   * Al salvataggio del soggetto su SCRIVA, il codice fiscale non è risultato in anagrafica.
   */
  onCFRiscaInvalido() {
    // Emetto l'evento passando il codice
    this.onCFRiscaInvalido$.next(RiscaNotifyCodes.I025);
  }

  /**
   * ##############################################
   * GETTER DI COMODO PER LE VARIABILI DEL SERVIZIO
   * ##############################################
   */

  /**
   * Getter che recupera l'abilitazione 'isGestioneAbilitata' per i soggetti.
   */
  get isGestioneAbilitata(): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    return this._ambito.getAbilitazioneSoggettiGruppi(
      AbilitaDASezioni.soggetti,
      AbilitaDASoggetti.isGestioneAbilitata
    );
  }

  /**
   * Getter per _tipiSoggetto.
   * @returns Array di TipoSoggetto.
   */
  get tipiSoggetto(): TipoSoggettoVo[] {
    // Creo una copia del dato
    return cloneDeep(this._tipiSoggetto);
  }

  /**
   * Getter per _tipiNaturaGiuridica
   * @returns Array di TipoNaturaGiuridica.
   */
  get tipiNaturaGiuridica(): TipoNaturaGiuridica[] {
    // Creo una copia del dato
    return cloneDeep(this._tipiNaturaGiuridica);
  }

  /**
   * Getter per il codice fiscale dell'user collegato.
   * @returns string con il codice fiscale dell'utente collegato.
   */
  get cf() {
    // Recupero il codice fiscale dell'utente
    return this._user.user.codFisc;
  }
}
