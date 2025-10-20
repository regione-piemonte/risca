import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { cloneDeep, differenceWith, intersectionWith } from 'lodash';
import { Observable, of, Subject, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { RicercaPaginataResponse } from 'src/app/core/classes/http-helper/http-helper.classes';
import { IIuvVo, IuvVo } from 'src/app/core/commons/vo/iuv-vo';
import { ConfigService } from 'src/app/core/services/config.service';
import { HttpHelperService } from 'src/app/core/services/http-helper/http-helper.service';
import {
  AttivitaSDVo,
  IAttivitaSDVo,
} from '../../../../../core/commons/vo/attivita-sd-vo';
import {
  DilazioneVo,
  IDilazioneVo,
} from '../../../../../core/commons/vo/dilazione-vo';
import {
  IRicercaStatiDebitoriPagamentoVo,
  RicercaStatiDebitoriPagamentoVo,
} from '../../../../../core/commons/vo/ricerca-stati-debitori-pagamento-vo';
import {
  IStatoDebitorioVo,
  StatoDebitorioVo,
} from '../../../../../core/commons/vo/stato-debitorio-vo';
import { ISDInvioSpecialeVo } from '../../../../../core/commons/vo/verify-stato-debitorio-invio-speciale-vo';
import { HttpUtilitiesService } from '../../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaModalService } from '../../../../../shared/services/risca/risca-modal.service';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  DynamicObjAny,
  ICallbackDataModal,
  RiscaServerError,
  RiscaServerErrorInfo,
  RiscaTablePagination,
} from '../../../../../shared/utilities';
import { ApriModalConfigs } from '../../../../../shared/utilities/classes/risca-modal/risca-modal.classes';
import { RiscaNotifyCodes } from '../../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IApriModalConfigsForClass } from '../../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { ModalitaVerifySaveSD } from '../../../enums/dati-contabili/dati-contabili.enums';
import {
  IActionHandleVerifySD,
  IActionPayloadSD,
  IActionRejectSD,
  IGestioneVerifySD as IDataVerifySD,
  IVerifySDResult,
} from '../../../interfaces/dati-contabili/dati-contabili.interfaces';
import { GestisciSalvataggioSDModalComponent } from '../../../modal/gestisci-salvataggio-sd-modal/gestisci-salvataggio-sd-modal.component';
import { IGestioneSDSalvataggio } from '../utilities/dati-contabili-modal.interfaces';
import { onVerifyModA018 } from '../utilities/verifica-sd-modifica/verifica-sd-mod-A018';
import { onVerifyModA023 } from '../utilities/verifica-sd-modifica/verifica-sd-mod-A023';
import { onVerifyModA027 } from '../utilities/verifica-sd-modifica/verifica-sd-mod-A027';
import { onVerifyModA028 } from '../utilities/verifica-sd-modifica/verifica-sd-mod-A028';
import {
  IDCRicercaSDByMorosita,
  ISearchSDByCodiceUtenza,
  ISearchSDByNap,
} from './utilities/dati-contabili.interfaces';

/**
 * Interfaccia che definisce i campi per la request di ricerca dei dati contabili.
 */
export interface IRicercaDatiContabili {
  offset: number;
  limit: number;
  [key: string]: any;
}

@Injectable({ providedIn: 'root' })
export class DatiContabiliService extends HttpUtilitiesService {
  /** Costante per il path: /stati-debitori */
  private PATH_STATI_DEBITORI = '/stati-debitori';
  /** Costante per il path: /_verify_stato_debitorio_invio_speciale */
  private PATH_VERIFY_SD_INVIO_SPECIALE =
    '/_verify_stato_debitorio_invio_speciale/idRiscossione';
  /** Costante per il path: /iuv */
  private PATH_IUV = '/iuv';
  /** Costante per il path: /utenze-comp */
  private PATH_UTENZE_COMP = '/utenze-comp';
  /** Costante per il path: /idStatoDebitorio */
  private PATH_ID_SD = '/idStatoDebitorio';
  /** Costante per il path: /dilazione/ambito */
  private PATH_DILAZIONE = '/dilazione/ambito';
  /** Costante per il path: /_verify-stato-debitorio */
  private PATH_VERIFY_SD = '/_verify_stato_debitorio';
  /** Costante per il path: /nap */
  private PATH_NAP = '/nap';
  /** Costante per il path: /nap */
  private PATH_CANONE_DOVUTO = '/canone-dovuto';
  /** Costante per il path: /codUtenza */
  private PATH_CODICE_UTENZA = '/codUtenza';
  /** Costante per il path: /tipi-ricerca-morosita */
  private PATH_TIPI_RICERCA_MOROSITA = '/tipi-ricerca-morosita';
  /** Costante per il path: /pagamenti-da-visionare */
  private PATH_GESTIONE_PAGAMENTO = '/pagamenti-da-visionare';
  /** Costante per il path: /stati_debitori/attivita */
  private PATH_ATTIVITA_STATO_DEBITORIO = `/stati_debitori/attivita`;

  /** Subject che permette la condivisione dell'evento d'inserimento di un oggetto StatoDebitorioVo. */
  onSDInsSuccess$ = new Subject<StatoDebitorioVo>();
  /** Subject che permette la condivisione dell'evento d'errore durante un inserimento di un oggetto StatoDebitorioVo. */
  onSDInsError$ = new Subject<RiscaServerError>();
  /** Subject che permette la condivisione dell'evento di annuallamento della funzionalità d'inserimento di un oggetto StatoDebitorioVo. */
  onSDInsCancel$ = new Subject<any>();
  /** Subject che permette la condivisione dell'evento di modifica di un oggetto StatoDebitorioVo. */
  onSDModSuccess$ = new Subject<StatoDebitorioVo>();
  /** Subject che permette la condivisione dell'evento d'errore durante una modifica di un oggetto StatoDebitorioVo. */
  onSDModError$ = new Subject<RiscaServerError>();
  /** Subject che permette la condivisione dell'evento di annuallamento della funzionalità di modifica di un oggetto StatoDebitorioVo. */
  onSDModCancel$ = new Subject<IActionRejectSD>();

  private SEPARATORE_UTENZE_COMPENSAZIONE: string = ', ';

  constructor(
    config: ConfigService,
    private _http: HttpClient,
    private _httpHelper: HttpHelperService,
    private _riscaModal: RiscaModalService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * #############################################
   * API DI VERIFICA E SALVATAGGIO STATO DEBITORIO
   * #############################################
   */

  /**
   * Funzione di verify per l'oggetto stato debitorio. Vengono effettuate le verifiche a seconda della modalità.
   * La funzione di verifica ha una gestione specifica per alcuni tipi di errore. Se generati specificatamente per la verify, verrà creato un oggetto IVerifySDResult.
   * @param statoDebitorio StatoDebitorioVo come oggetto da verificare.
   * @param modalita ModalitaVerifySD che definisce il tipo di modalità da impiegare per la verifica.
   * @returns Observable<IVerifySDResult> con il risultato della verifica.
   */
  verifyStatoDebitorio(
    statoDebitorio: StatoDebitorioVo,
    modalita: ModalitaVerifySaveSD
  ): Observable<IVerifySDResult> {
    // Creo l'url da richiamare
    const url = this._config.appUrl(this.PATH_VERIFY_SD);
    // Definisco i query param
    const params = this._riscaUtilities.createQueryParams({ modalita });

    // Converto l'oggetto stato debitorio in un oggetto di BE
    const sdServer = statoDebitorio?.toServerFormat();

    // Richiamo il servizio di verifica
    return this._http.post<boolean>(url, sdServer, { params }).pipe(
      map((result: boolean) => {
        // Ritorno l'oggetto di risposta
        const verifyResult: IVerifySDResult = { isValid: result };
        // Ritorno l'oggetto
        return verifyResult;
        // #
      }),
      catchError((serverError: RiscaServerError) => {
        // Recupero le informazioni per capire il tipo d'errore
        const { errors } = serverError;
        // Verifico se esiste una lista d'errori
        const checkE = errors && errors.length > 0;

        // Verifico il check
        if (checkE) {
          // Situazione generata specificatamente per la verify, creo l'oggetto di ritorno
          const verifyResult: IVerifySDResult = {
            isValid: false,
            errors: errors,
          };
          // Ritorno l'errore come valido
          return of(verifyResult);
          // #
        } else {
          // E' un qualunque errore gestito dal server, lo ritorno
          return throwError(serverError);
        }
      })
    );
  }

  /**
   * Funzione di verify per l'oggetto stato debitorio. Vengono effettuate le verifiche a seconda della modalità: inserimento.
   * La funzione di verifica ha una gestione specifica per alcuni tipi di errore. Se generati specificatamente per la verify, verrà creato un oggetto IVerifySDResult.
   * @param statoDebitorio StatoDebitorioVo come oggetto da verificare.
   * @returns Observable<IVerifySDResult> con il risultato della verifica.
   */
  verifyInsStatoDebitorio(
    statoDebitorio: StatoDebitorioVo
  ): Observable<IVerifySDResult> {
    // Definisco la modalita per la verify
    const modalita = ModalitaVerifySaveSD.inserimento;
    // Richiamo e ritorno il servizio
    return this.verifyStatoDebitorio(statoDebitorio, modalita);
  }

  /**
   * Funzione di verify per l'oggetto stato debitorio. Vengono effettuate le verifiche a seconda della modalità: modifica.
   * La funzione di verifica ha una gestione specifica per alcuni tipi di errore. Se generati specificatamente per la verify, verrà creato un oggetto IVerifySDResult.
   * @param statoDebitorio StatoDebitorioVo come oggetto da verificare.
   * @returns Observable<IVerifySDResult> con il risultato della verifica.
   */
  verifyModStatoDebitorio(
    statoDebitorio: StatoDebitorioVo
  ): Observable<IVerifySDResult> {
    // Definisco la modalita per la verify
    const modalita = ModalitaVerifySaveSD.modifica;
    // Richiamo e ritorno il servizio
    return this.verifyStatoDebitorio(statoDebitorio, modalita);
  }

  /**
   * Funzione di salvataggio per l'oggetto stato debitorio.
   * A seconda dell'azione richiesta, viene lanciata una POST o una PUT dell'oggetto.
   * @param statoDebitorio StatoDebitorioVo da salvare.
   * @param modalita ModalitaVerifySaveSD con la modalità di salvataggio del dato.
   */
  saveStatoDebitorio(
    statoDebitorio: StatoDebitorioVo,
    modalita: ModalitaVerifySaveSD
  ): Observable<StatoDebitorioVo> {
    // Verifico l'input
    modalita = modalita ? modalita : ModalitaVerifySaveSD.inserimento;

    // A seconda della modalita, lancio e ritorno il servizio
    if (modalita === ModalitaVerifySaveSD.inserimento) {
      // Ritorno l'inserimento
      return this.insertStatoDebitorio(statoDebitorio);
      // #
    } else if (modalita === ModalitaVerifySaveSD.modifica) {
      // Ritorno la modfifica
      return this.updateStatoDebitorio(statoDebitorio);
      // #
    }
  }

  /**
   * Funzione di GET per l'oggetto StatoDebitorio.
   * @param idStatoDebitorio number che definisce l'id dello stato debitorio da scaricare.
   * @returns Observable<StatoDebitorioVo> con le informazioni dello stato debitorio trovato.
   */
  getStatoDebitorio(idStatoDebitorio: number): Observable<StatoDebitorioVo> {
    // Creo l'url da richiamare
    const url = this._config.appUrl(this.PATH_STATI_DEBITORI, idStatoDebitorio);

    // Richiamo il servizio per il salvataggio
    return this._http.get<IStatoDebitorioVo>(url).pipe(
      map((iSD: IStatoDebitorioVo) => {
        // Converto l'oggetto in formato FE like
        return new StatoDebitorioVo(iSD);
      })
    );
  }

  /**
   * Funzione di POST per l'oggetto StatoDebitorio.
   * @param statoDebitorio StatoDebitorioVo da salvare.
   * @returns Observable<StatoDebitorioVo> con le informazioni dello stato debitorio salvato.
   */
  insertStatoDebitorio(
    statoDebitorio: StatoDebitorioVo
  ): Observable<StatoDebitorioVo> {
    // Creo l'url da richiamare
    const url = this._config.appUrl(this.PATH_STATI_DEBITORI);
    // Lancio la funzione di conversione ad oggetto server dello stato debitorio
    const statoDebitorioServer = statoDebitorio?.toServerFormat();

    // Richiamo il servizio per il salvataggio
    return this._http.post<IStatoDebitorioVo>(url, statoDebitorioServer).pipe(
      map((iSD: IStatoDebitorioVo) => {
        // Converto l'oggetto in formato FE like
        return new StatoDebitorioVo(iSD);
      }),
      tap((statoDebitorio: StatoDebitorioVo) => {
        // Richiamo l'evento di oggetto salvato correttamente
        this.onSDInsSuccess$.next(statoDebitorio);
        // #
      })
    );
  }

  /**
   * Funzione di PUT per l'oggetto StatoDebitorio.
   * @param statoDebitorio StatoDebitorioVo da salvare.
   * @param queryParams DynamicObjAny con i query params da passare alla chiamata d'aggiornamento per lo stato debitorio.
   * @returns Observable<StatoDebitorioVo> con le informazioni dello stato debitorio salvato.
   */
  updateStatoDebitorio(
    statoDebitorio: StatoDebitorioVo,
    queryParams?: DynamicObjAny
  ): Observable<StatoDebitorioVo> {
    // Creo l'url da richiamare
    const url = this._config.appUrl(this.PATH_STATI_DEBITORI);
    // Lancio la funzione di conversione ad oggetto server dello stato debitorio
    const sdBody = statoDebitorio?.toServerFormat();
    // Definisco un oggetto per la gestione della request che dipende dalla presenza di query params
    let request: Observable<IStatoDebitorioVo>;

    // Verifico se esistono query params
    if (queryParams && !this._riscaUtilities.isEmptyObject(queryParams)) {
      // Esistono dei query param, genero l'oggetto per la chiamata
      const params = this._riscaUtilities.createQueryParams(queryParams);
      // Definisco la chiamata complata
      request = this._http.put<IStatoDebitorioVo>(url, sdBody, { params });
      // #
    } else {
      // Nessun query params, definisco la chiamata senza
      request = this._http.put<IStatoDebitorioVo>(url, sdBody);
    }

    // Richiamo il servizio per il salvataggio
    return request.pipe(
      map((iSD: IStatoDebitorioVo) => {
        // Converto l'oggetto in formato FE like
        return new StatoDebitorioVo(iSD);
      }),
      tap((statoDebitorio: StatoDebitorioVo) => {
        // Richiamo l'evento di oggetto salvato correttamente
        this.onSDModSuccess$.next(statoDebitorio);
        // #
      })
    );
  }

  /**
   * Funzione di PUT per l'aggiornamento dell'attività stato debitorio di un oggetto StatoDebitorio.
   * Questa funzione si basa su un'API specifica che ha già della logica di business utilizzata da altre infrastrutture.
   * La gestione quindi del dato si basa solo su id_stato_debitorio e attivita_stato_deb, sia come salvataggio che come informazioni restituite dal servizio.
   * @param attivitaStatoDebitorio AttivitaSDVo d'aggiornare su uno stato debitorio.
   * @param idStatoDebitorio number con l'id stato debitorio di riferimento per l'aggiornamento dei dati.
   * @returns Observable<Partial<StatoDebitorioVo>> con le informazioni parziali per lo stato debitorio salvato. Questa API gestisce in maniera specifica le informazioni, andando a gestire praticamente solo l'attività stato debitorio e l'id stato debitorio.
   * @author Ismaele Bottelli
   * @date 20/01/2025
   * @git RISCA-ISSUES-60
   * @notes Con la nuova gestione richiesta, gli accertamenti vengono gestiti in CRUD in maniera a se stante.
   */
  putAttivitaStatoDebitorio(
    attivitaStatoDebitorio: AttivitaSDVo,
    idStatoDebitorio: number
  ): Observable<Partial<StatoDebitorioVo>> {
    // Creo l'url da richiamare
    const url = this._config.appUrl(this.PATH_ATTIVITA_STATO_DEBITORIO);

    // Converto l'oggetto dell'attività stato debitorio per il server
    const attivitaSDBE: IAttivitaSDVo =
      attivitaStatoDebitorio?.toServerFormat();
    // Creo un oggetto parziale rispetto ad uno stato debitorio.
    // E' un caso molto particolare e specifico per non mandare troppe informazioni per l'aggiornamento dell'attività stato debitorio
    const statoDebitorioBE: IStatoDebitorioVo = {
      id_stato_debitorio: idStatoDebitorio,
      attivita_stato_deb: attivitaSDBE,
    };
    // Per l'API è necessario passare la lista
    const statiDebitoriBE: IStatoDebitorioVo[] = [statoDebitorioBE];

    // Richiamo il servizio per il salvataggio
    return this._http.put<IStatoDebitorioVo[]>(url, statiDebitoriBE).pipe(
      map((statiDebitoriUpdate: IStatoDebitorioVo[]) => {
        // Verifico il valore restituito dal servizio
        statiDebitoriUpdate = statiDebitoriUpdate ?? [];
        // Recupero l'unico elemento di cui ho richiesto l'aggiornamento
        const statoDebitorioBE: IStatoDebitorioVo = statiDebitoriUpdate[0];
        // Creo un oggetto parziale per lo stato debitorio
        const statoDebitorioFE = new StatoDebitorioVo();
        // Inserisco manualmente le informazioni per lo stato debitorio
        statoDebitorioFE.attivita_stato_deb = attivitaStatoDebitorio;
        statoDebitorioFE.id_stato_debitorio =
          statoDebitorioBE?.id_stato_debitorio;

        // Composte le informazioni, ritorno l'oggetto parziale
        return statoDebitorioFE;
        // #
      })
    );
  }

  /**
   * Funzione che effettua verifica ed inserimento di uno stato debitorio.
   * Vengono gestiti gli errori relativi alla verify. Se è necessario vengono aperti dei prompt di conferma verso l'utente.
   * In caso in cui il processo terminasse correttamente o in errore, verrà emesso un evento di aggiornamento sullo stato debitorio.
   * @param statoDebitorio StatoDebitorioVo da salvare.
   */
  verifyAndInsertStatoDebitorio(statoDebitorio: StatoDebitorioVo) {
    // Richiamo la funzione di verifica inserimento
    this.verifyInsStatoDebitorio(statoDebitorio).subscribe({
      next: (verify: IVerifySDResult) => {
        // Lancio la funzione di gestione della verify
        this.onVerifyAndInsSD(statoDebitorio, verify);
        // #
      },
      error: (serverError: RiscaServerError) => {
        // Emetto l'evento come errore
        this.onSDInsError$.next(serverError);
        // #
      },
    });
  }

  /**
   * Funzione che effettua verifica e modifica di uno stato debitorio.
   * Vengono gestiti gli errori relativi alla verify. Se è necessario vengono aperti dei prompt di conferma verso l'utente.
   * In caso in cui il processo terminasse correttamente o in errore, verrà emesso un evento di aggiornamento sullo stato debitorio.
   * @param statoDebitorio StatoDebitorioVo da salvare.
   */
  verifyAndUpdateStatoDebitorio(statoDebitorio: StatoDebitorioVo) {
    // Richiamo la funzione di verifica inserimento
    this.verifyModStatoDebitorio(statoDebitorio).subscribe({
      next: (verify: IVerifySDResult) => {
        // Lancio la funzione di gestione della verify
        this.onVerifyAndModSD(statoDebitorio, verify);
        // #
      },
      error: (serverError: RiscaServerError) => {
        // Emetto l'evento come errore
        this.onSDModError$.next(serverError);
        // #
      },
    });
  }

  /**
   * Funzione che gestisce un oggetto IVerifySDResult ottenuto da una verifica per inserimento di uno stato debitorio.
   * L'oggetto di verifica gestirà eventuali comunicazioni all'utente e/o salverà lo stato debitorio in input.
   * @param statoDebitorio StatoDebitorioVo da salvare.
   * @param verifica IVerifySDResult con il risultato della verifica dello stato debitorio.
   */
  onVerifyAndInsSD(
    statoDebitorio: StatoDebitorioVo,
    verifica: IVerifySDResult
  ) {
    // Verifico l'input
    if (!statoDebitorio || !verifica) {
      // Niente operazioni
      return;
    }

    // Definisco la modalità di riferimento
    const modalita = ModalitaVerifySaveSD.inserimento;
    // Definisco la configurazione per la gestione della modale
    const datiVerify: IDataVerifySD = { statoDebitorio, verifica, modalita };
    // Definisco un array di configurazioni che mappa uno specifico errore, con una specifica attività
    const azioni: IActionHandleVerifySD[] = [];

    // Lancio la funzione che gestisce il flusso analizzando le configurazioni in input
    this.verifySDWithModal(datiVerify, azioni);
  }

  /**
   * Funzione che gestisce un oggetto IVerifySDResult ottenuto da una verifica per modifica di uno stato debitorio.
   * L'oggetto di verifica gestirà eventuali comunicazioni all'utente e/o salverà lo stato debitorio in input.
   * @param statoDebitorio StatoDebitorioVo da salvare.
   * @param verifica IVerifySDResult con il risultato della verifica dello stato debitorio.
   */
  onVerifyAndModSD(
    statoDebitorio: StatoDebitorioVo,
    verifica: IVerifySDResult
  ) {
    // Verifico l'input
    if (!statoDebitorio || !verifica) {
      // Niente operazioni
      return;
    }

    // Definisco la modalità di riferimento
    const modalita = ModalitaVerifySaveSD.modifica;
    // Definisco la configurazione per la gestione della modale
    const datiVerify: IDataVerifySD = { statoDebitorio, verifica, modalita };
    // Definisco un array di configurazioni che mappa uno specifico errore, con una specifica attività
    const azioni: IActionHandleVerifySD[] = [
      this.onVerifyModA018,
      this.onVerifyModA023,
      this.onVerifyModA027,
      this.onVerifyModA028,
    ];

    // Lancio la funzione che gestisce il flusso analizzando le configurazioni in input
    this.verifySDWithModal(datiVerify, azioni);
  }

  /**
   * Funzione di supporto che analizza le configurazioni in input e gestisce il flusso di errori o salvataggio dello stato debitorio.
   * @param datiVerify IDataVerifySD contenente le informazioni di gestione generate dalla verify.
   * @param azioni IActionVerifySD[] che identifica le possibili azioni da eseguire
   */
  private verifySDWithModal(
    datiVerify: IDataVerifySD,
    azioni: IActionHandleVerifySD[]
  ) {
    // Verifico l'input
    if (!datiVerify || !azioni) {
      // Niente configurazioni
      return;
    }
    // Estraggo le informazioni dagli oggetti
    const { verifica, statoDebitorio, modalita } = datiVerify;

    // #####################################
    // 1) Verifico il risultato della verify
    // #####################################
    // Controllo il flag della verify
    if (verifica.isValid) {
      // Richiamo la funzione di salvataggio
      this.saveStatoDebitorioByIDataVerifySD(datiVerify);
      // Interrompo il flusso logico
      return;
    }

    // ##########################################################
    // 2) Verifico se ci sono errori bloccanti per il salvataggio
    // ##########################################################
    // Recupero dalle azioni la lista dei codici da verificare
    const codes = azioni.map((a: IActionHandleVerifySD) => a.code);
    // Recupero dalla verifica la lista degli errori generati
    const errors = verifica.errors;
    // Definisco le logiche per il match tra errori e codici
    const compare = (a: RiscaServerErrorInfo, b: RiscaNotifyCodes) => {
      // Verifico e ritorno i codici d'errore
      return a.code === b;
    };
    // Rimuovo da errors tutti i codici definiti in codes
    const blocks = differenceWith(errors, codes, compare);
    // Se esistono elementi dentro blocks, vuol dire che ci sono errori bloccanti da segnalare
    if (blocks.length > 0) {
      // Lancio la funzionalità di segnalazione degli errori
      this.errorStatoDebitorioVerify(modalita, blocks);
      // Blocco il flusso
      return;
    }

    // #################################################################
    // 3) Niente errori bloccanti, verifico quale richiesta visualizzare
    // #################################################################
    // Definisco una funzione locale di confronto per la gestione dell'intersezioni delle configurazioni tra azioni ed errori
    const intersectFn = (a: IActionHandleVerifySD, e: RiscaServerErrorInfo) => {
      // Vado a cercare il codice azione con il codice errore
      return a.code === e.code;
    };
    // Recupero la lista di errori/domande da gestire tramite prompt dati
    let prompts: IActionHandleVerifySD[];
    prompts = intersectionWith(azioni, errors, intersectFn);

    // Verifico se è stato trovato almeno un prompt da visualizzare all'utente
    if (prompts && prompts.length > 0) {
      // Creo una copia del dato per lo stato debitorio
      const sd: StatoDebitorioVo = cloneDeep(statoDebitorio);
      // Apro la gestione tramite modale delle domande
      this.gestisciDomandadeSaveSD(sd, prompts);
      // #
    } else {
      // Richiamo la funzione di salvataggio
      this.saveStatoDebitorioByIDataVerifySD(datiVerify);
      // #
    }
  }

  /**
   * Funzione di supporto che compone le informazioni e gestisce l'apertura di una modale dove verranno poste all'utente tutte le domande necessarie per gestire lo stato debitorio.
   * Una volta ottenuta risposta a tutte le domande, verrà salvato lo stato debitorio. I flussi vengono gestiti da specifiche configurazioni.
   * @param statoDebitorio StatoDebitorioVo che si sta cercando di salvare.
   * @param prompts IActionHandleVerifySD[] con la lista di configurazioni per le domande da porre all'utente.
   */
  private gestisciDomandadeSaveSD(
    statoDebitorio: StatoDebitorioVo,
    prompts: IActionHandleVerifySD[]
  ) {
    // Definisco la configurazione per le callbacks
    const callbacksSD: ICallbackDataModal = {
      onConfirm: (payload: IActionPayloadSD) => {
        // Estraggo le informazioni dal payload
        const { statoDebitorio, queryFlags } = payload;
        // Salvo lo stato debitorio
        this.updateStatoDebitorio(statoDebitorio, queryFlags).subscribe({
          next: (sd: StatoDebitorioVo) => {
            // Emetto l'evento di stato debitorio salvato
            this.onSDModSuccess$.next(sd);
          },
          error: (e: RiscaServerError) => {
            // Emetto l'evento d'errore salvataggio
            this.onSDModError$.next(e);
          },
        });
      },
      onCancel: (reject?: IActionRejectSD) => {
        // Emetto l'evento di cancellazione della funzionalità
        this.onSDModCancel$.next(reject);
      },
      onClose: (reject?: IActionRejectSD) => {
        // Emetto l'evento di cancellazione della funzionalità
        this.onSDModCancel$.next(reject);
      },
    };

    // Impacchetto le informazioni e richiamo l'apertura della modale
    this.openGestioneSDSalvataggio({ callbacksSD, prompts, statoDebitorio });
  }

  /**
   * Funzione che permette che gestisce l'apertura di una modale che pone delle specifiche domande all'utente per gestire i dati di uno stato debitorio prima del suo salvataggio.
   * Le domande gestiscono delle casistiche specifiche dello stato debitorio, modificandolo secondo specifiche logiche.
   * Al termine di tutte le modifiche, verrà richiamato un flusso di gestione unico.
   * @param configs ICallbackPayloadModal contenente i payload da ritornare sulle callback.
   */
  private openGestioneSDSalvataggio(configs: IGestioneSDSalvataggio) {
    // Verifico le configurazioni
    if (!configs) {
      // Niente configurazioni
      return;
    }

    // Estraggo le informazioni dalle configurazioni
    const { callbacksSD, prompts, statoDebitorio } = configs;

    // Creo la configurazione per l'apertura della modale
    const m: IApriModalConfigsForClass = {
      component: GestisciSalvataggioSDModalComponent,
      options: { windowClass: 'risca-modal-conferma', backdrop: 'static' },
      callbacks: callbacksSD,
      params: { statoDebitorio, prompts },
    };
    // Genero la classe effettiva per la gestione della modale
    const modal = new ApriModalConfigs(m);

    // Richiamo il servizio per l'apertura della modale
    this._riscaModal.apriModal(modal);
  }

  /**
   * Funzione di comodo che verifica se la modalita del salvataggio è: ModalitaVerifySaveSD.inserimento.
   * @param modalita ModalitaVerifySaveSD da verificare.
   * @returns boolean che indica se la modalità e inserimento.
   */
  private isVerifySaveIns(modalita: ModalitaVerifySaveSD): boolean {
    // Verifico se modalita è inserimento
    return modalita === ModalitaVerifySaveSD.inserimento;
  }

  /**
   * Funzione di comodo che verifica se la modalita del salvataggio è: ModalitaVerifySaveSD.modifica.
   * @param modalita ModalitaVerifySaveSD da verificare.
   * @returns boolean che indica se la modalità e modifica.
   */
  private isVerifySaveMod(modalita: ModalitaVerifySaveSD): boolean {
    // Verifico se modalita è modifica
    return modalita === ModalitaVerifySaveSD.modifica;
  }

  /**
   * Funzione di supporto per la gestione del salvataggio diretto dello stato debitorio a seguito dei controlli della verify stato debitorio.
   * @param datiVerify IDataVerifySD contenente le informazioni di gestione generate dalla verify.
   */
  private saveStatoDebitorioByIDataVerifySD(datiVerify: IDataVerifySD) {
    // Verifico l'input
    if (!datiVerify) {
      // Niente configurazioni
      return;
    }
    // Estraggo le informazioni dall'oggetto
    const { statoDebitorio, modalita } = datiVerify;

    // Verifico la modalità di salvataggio
    const isIns = this.isVerifySaveIns(modalita);
    const isMod = this.isVerifySaveMod(modalita);

    // Verifico e richiamo il salvataggio
    if (isIns) {
      // Inserisco lo stato debitorio
      this.insertStatoDebitorio(statoDebitorio).subscribe({
        next: (sd: StatoDebitorioVo) => {
          // Emetto l'evento di stato debitorio salvato
          this.onSDInsSuccess$.next(sd);
        },
        error: (e: RiscaServerError) => {
          // Emetto l'evento d'errore salvataggio
          this.onSDInsError$.next(e);
        },
      });
    } else if (isMod) {
      // Aggiorno lo stato debitorio
      this.updateStatoDebitorio(statoDebitorio).subscribe({
        next: (sd: StatoDebitorioVo) => {
          // Emetto l'evento di stato debitorio salvato
          this.onSDModSuccess$.next(sd);
        },
        error: (e: RiscaServerError) => {
          // Emetto l'evento d'errore salvataggio
          this.onSDModError$.next(e);
        },
      });
    }
  }

  /**
   * Funzione di supporto per la gestione dell'errore diretto dello stato debitorio a seguito dei controlli della verify stato debitorio.
   * @param datiVerify ModalitaVerifySaveSD che definisce la modalità di gestione dell'oggetto.
   * @param errors RiscaServerErrorInfo[] con le informazioni degli errori generati.
   */
  private errorStatoDebitorioVerify(
    modalita: ModalitaVerifySaveSD,
    errors: RiscaServerErrorInfo[]
  ) {
    // Verifico la modalità di salvataggio
    const isIns = this.isVerifySaveIns(modalita);
    const isMod = this.isVerifySaveMod(modalita);
    // Creo un oggetto RiscaServerError
    const e = new RiscaServerError({ errors });

    // Verifico e richiamo il salvataggio
    if (isIns) {
      // Emetto l'evento d'errore salvataggio
      this.onSDInsError$.next(e);
    } else if (isMod) {
      // Emetto l'evento d'errore salvataggio
      this.onSDModError$.next(e);
    }
  }

  /**
   * #############################
   * API PER SCARICO/GESTIONE DATI
   * #############################
   */

  /**
   * Avvia la ricerca degli stati debitori della riscossione, filtrati in base alla stringa "filtro" in input.
   * @param idRiscossione id della riscossione corrente.
   * @param paginazione dati della paginazione.
   * @returns Observable<RicercaPaginataResponse<DocumentoAllegatoVo[]>> con la lista di risultati paginati e info sulla paginazione dal BE.
   */
  ricercaStatiDebitoriPagination(
    idRiscossione?: number,
    paginazione?: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> {
    // Definisco l'url
    const url = this._config.appUrl(this.PATH_STATI_DEBITORI);
    // Definisco l'oggetto contenente i query params non di paginazione
    const queryParams = { idRiscossione };
    // Prendo i parametri
    const params = this._riscaUtilities.createPagingParams(
      paginazione,
      queryParams
    );
    // Definisco le options
    const options = { params };

    // Effettuo la chiamata
    return this._httpHelper.searchWithGet(url, options).pipe(
      map((res: RicercaPaginataResponse<IStatoDebitorioVo[]>) => {
        // Ritorno la response convertita
        return this.paginateStatiDebitori(res);
      })
    );
  }

  /**
   * Funzione di verifica che richiede al server se, per la pratica in input, esiste già uno stato debitorio ad essa collegata avente il flag invio speciale a [true].
   * @param idPratica number che definisce l'id della pratica da verificare.
   * @param idStatoDebitorio number che specifica l'id stato debitorio da escludere dalla verifica per i flag invio speciale.
   * @returns Observable<boolean> che definisce se la pratica ha già uno stato debitorio con invio speciale [true] o no [false].
   */
  praticaConSDInvioSpeciale(
    idPratica: number,
    idStatoDebitorio?: number
  ): Observable<boolean> {
    // Variabile di comodo
    const verifyUrl = this.PATH_VERIFY_SD_INVIO_SPECIALE;
    // Definisco l'url
    const url = this._config.appUrl(verifyUrl, idPratica);

    // Definisco il contenitore per la chiamata da effettuare
    let request: Observable<ISDInvioSpecialeVo>;

    // Verifico se esiste anche lo stato debitorio
    if (idStatoDebitorio != undefined) {
      // Esiste lo stato debitorio, genero il query param
      const params = this.createQueryParams({ idStatoDebitorio });
      // Definisco le options
      const o = { params };
      // Assegno alla variabile la chiamata
      request = this._http.get<ISDInvioSpecialeVo>(url, o);
      // #
    } else {
      // Non esiste uno stato debitorio da escludere, assegno la chiamata
      request = this._http.get<ISDInvioSpecialeVo>(url);
      // #
    }

    // Richiamo il servizio tramite get
    return request.pipe(
      map((res: ISDInvioSpecialeVo) => {
        // Verifico e ritorno il flag
        return res?.exist ?? false;
      })
    );
  }

  /**
   * Funzione che ritorna un oggetto IuvVo, partendo dal codice nap in input.
   * @param nap string che definisce il codice nap per lo scarico dello iuv.
   * @returns Observable<IuvVo> contenente il valore della GET.
   */
  getIuv(nap: string): Observable<IuvVo> {
    // Creo l'url
    const url = this._config.appUrl(this.PATH_IUV);
    // Creo i query params
    const params = this._riscaUtilities.createQueryParams({ nap });

    // Richiamo e ritorno il risultato della chiamata
    return this._http.get<IIuvVo>(url, { params }).pipe(
      map((iIuvVo: IIuvVo) => {
        // Converto l'oggetto
        return new IuvVo(iIuvVo);
      })
    );
  }

  /**
   * Funzione che ritorna una stringa che identifica le utenze compensazione per lo stato debitorio richiesto.
   * @param idStatoDebitorio number che definisce l'id dello stato debitorio per il recupero dati.
   * @returns Observable<string> contenente il valore della GET.
   */
  getUtenzeCompensazione(idStatoDebitorio: number): Observable<string> {
    // Creo l'url
    const url = this._config.appUrl(
      this.PATH_UTENZE_COMP,
      this.PATH_ID_SD,
      idStatoDebitorio
    );
    // Richiamo e ritorno il risultato della chiamata
    return this._http.get<string[]>(url).pipe(
      map((cu: string[]) => {
        return cu.join(this.SEPARATORE_UTENZE_COMPENSAZIONE);
      })
    );
  }

  /**
   * Funzione che richiama il calcolo del totale degli importi versati per gli stati debitori per stesso NAP.
   * @param nap string che definisce il NAP di riferimento per lo scarico delle informazioni.
   * @returns Observable<number> con le informazioni scaricate.
   */
  canoneDovutoStatiDebitoriByNAP(nap: string): Observable<number> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(
      this.PATH_STATI_DEBITORI,
      this.PATH_NAP,
      nap,
      this.PATH_CANONE_DOVUTO
    );

    // Definisco le options
    const options = {};

    // Lancio la chiamata per il recupero dati
    return this._http.get<number>(url, options);
  }

  /**
   * Funzione che ricerca i dati degli stati debitori dal filtro di ricerca gestito da un pagamento, in modalità paginata.
   * @param ricerca RicercaStatiDebitoriPagamentoVo con i filtri di ricerca.
   * @param paginazione RiscaTablePagination dati di paginazione della tabella.
   * @param searchParams ISearchSDByNap che definisce i possibili query params di ricerca dati.
   * @returns Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> con i risultati della ricerca.
   */
  getStatiDebitorixPagamentoPagination(
    ricerca: RicercaStatiDebitoriPagamentoVo,
    paginazione?: RiscaTablePagination,
    searchParams?: ISearchSDByNap
  ): Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(
      this.PATH_STATI_DEBITORI,
      this.PATH_GESTIONE_PAGAMENTO
    );

    // Verifico l'input per i parametri di ricerca
    searchParams = searchParams ?? {};
    // Definisco una variabile che potrebbe contenere i dati di filtro per gli stati debitori
    let qryParams: DynamicObjAny = { ...searchParams };
    // Definisco il body per gestire l'esclusione degli stati debitori dalla ricerca
    const body: IRicercaStatiDebitoriPagamentoVo = ricerca?.toServerFormat();

    // Prendo i parametri
    const params = this._riscaUtilities.createPagingParams(
      paginazione,
      qryParams
    );
    // Definisco le options
    const options = { params };

    // Lancio la chiamata per il recupero dati
    return this._httpHelper
      .searchWithPost<IStatoDebitorioVo[]>(url, body, options)
      .pipe(
        map((res: RicercaPaginataResponse<IStatoDebitorioVo[]>) => {
          // Ritorno la response convertita
          return this.paginateStatiDebitori(res);
        })
      );
  }

  /**
   * Funzione che ricerca i dati degli stati debitori per stesso NAP, in modalità paginata.
   * @param nap string che definisce il NAP di riferimento per lo scarico delle informazioni.
   * @param idStatiDebitori number[] con la lista degli id degli stati debitori da escludere dalla ricerca.
   * @param paginazione RiscaTablePagination dati di paginazione della tabella.
   * @param searchParams ISearchSDByNap che definisce i possibili query params di ricerca dati.
   * @returns Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> con i risultati della ricerca.
   */
  getStatiDebitoriByNAPPagination(
    nap: string,
    idStatiDebitori: number[],
    paginazione?: RiscaTablePagination,
    searchParams?: ISearchSDByNap
  ): Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(
      this.PATH_STATI_DEBITORI,
      this.PATH_NAP,
      nap
    );

    // Verifico l'input per i parametri di ricerca
    searchParams = searchParams ?? {};
    // Definisco una variabile che potrebbe contenere i dati di filtro per gli stati debitori
    let qryParams: DynamicObjAny = { ...searchParams };
    // Definisco il body per gestire l'esclusione degli stati debitori dalla ricerca
    let body: any = idStatiDebitori ?? [];

    // Prendo i parametri
    const params = this._riscaUtilities.createPagingParams(
      paginazione,
      qryParams
    );
    // Definisco le options
    const options = { params };

    // Lancio la chiamata per il recupero dati
    return this._httpHelper
      .searchWithPost<IStatoDebitorioVo[]>(url, body, options)
      .pipe(
        map((res: RicercaPaginataResponse<IStatoDebitorioVo[]>) => {
          // Ritorno la response convertita
          return this.paginateStatiDebitori(res);
        })
      );
  }

  /**
   * Funzione che ricerca i dati degli stati debitori per stesso NAP, in modalità paginata.
   * @param nap string che definisce il NAP di riferimento per lo scarico delle informazioni.
   * @param idStatiDebitori number[] con la lista degli id degli stati debitori da escludere dalla ricerca.
   * @param paginazione RiscaTablePagination dati di paginazione della tabella.
   * @param searchParams ISearchSDByCodiceUtenza che definisce i possibili query params di ricerca dati.
   * @returns Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> con i risultati della ricerca.
   */
  getStatiDebitoriByCodiceUtenzaPagination(
    codiceUtenza: string,
    idStatiDebitori: number[],
    paginazione?: RiscaTablePagination,
    searchParams?: ISearchSDByCodiceUtenza
  ): Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(
      this.PATH_STATI_DEBITORI,
      this.PATH_CODICE_UTENZA,
      codiceUtenza
    );

    // Verifico l'input per i parametri di ricerca
    searchParams = searchParams ?? {};
    // Definisco una variabile che potrebbe contenere i dati di filtro per gli stati debitori
    let qryParams: DynamicObjAny = { ...searchParams };
    // Definisco il body per gestire l'esclusione degli stati debitori dalla ricerca
    const body: any = idStatiDebitori ?? null;

    // Prendo i parametri
    const params = this._riscaUtilities.createPagingParams(
      paginazione,
      qryParams
    );
    // Definisco le options
    const options = { params };

    // Lancio la chiamata per il recupero dati
    return this._httpHelper
      .searchWithPost<IStatoDebitorioVo[]>(url, body, options)
      .pipe(
        map((res: RicercaPaginataResponse<IStatoDebitorioVo[]>) => {
          // Ritorno la response convertita
          return this.paginateStatiDebitori(res);
        })
      );
  }

  /**
   * Funzione che ricerca i dati degli stati debitori dal filtro di ricerca gestito da un pagamento, in modalità paginata.
   * @param ricerca RicercaStatiDebitoriPagamentoVo con i filtri di ricerca.
   * @param searchParams ISearchSDByNap che definisce i possibili query params di ricerca dati.
   * @returns Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> con i risultati della ricerca.
   */
  getStatiDebitorixPagamento(
    ricerca: RicercaStatiDebitoriPagamentoVo,
    searchParams?: ISearchSDByNap
  ): Observable<StatoDebitorioVo[]> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(
      this.PATH_STATI_DEBITORI,
      this.PATH_GESTIONE_PAGAMENTO
    );

    // Definisco il body per gestire l'esclusione degli stati debitori dalla ricerca
    const body: IRicercaStatiDebitoriPagamentoVo = ricerca?.toServerFormat();
    // Definisco una variabile contenente i query params
    let params: HttpParams;
    // Verifico l'input per i parametri di ricerca
    if (searchParams) {
      // Esistono parametri di ricerca, genero i query params
      params = this.createQueryParams(searchParams);
      // #
    }

    // Lancio la chiamata per il recupero dati
    return this._http.post<IStatoDebitorioVo[]>(url, body, { params }).pipe(
      map((res: IStatoDebitorioVo[]) => {
        // Ritorno la response convertita
        return this.listToStatiDebitoriVo(res);
      })
    );
  }

  /**
   * Funzione che ricerca i dati degli stati debitori per stesso NAP.
   * @param nap string che definisce il NAP di riferimento per lo scarico delle informazioni.
   * @param idStatiDebitori number[] con la lista degli id degli stati debitori da escludere dalla ricerca.
   * @param searchParams ISearchSDByNap che definisce i possibili query params di ricerca dati.
   * @returns Observable<StatoDebitorioVo[]> con i risultati della ricerca.
   */
  getStatiDebitoriByNAP(
    nap: string,
    idStatiDebitori: number[],
    searchParams?: ISearchSDByNap
  ): Observable<StatoDebitorioVo[]> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(
      this.PATH_STATI_DEBITORI,
      this.PATH_NAP,
      nap
    );

    // Tento di generare il filtro per gli stati debitori da escludere
    const body: any = idStatiDebitori ?? null;
    // Definisco una variabile contenente i query params
    let params: HttpParams;
    // Verifico l'input per i parametri di ricerca
    if (searchParams) {
      // Esistono parametri di ricerca, genero i query params
      params = this.createQueryParams(searchParams);
      // #
    }

    // Lancio la chiamata per il recupero dati
    return this._http.post<IStatoDebitorioVo[]>(url, body, { params }).pipe(
      map((res: IStatoDebitorioVo[]) => {
        // Ritorno la response convertita
        return this.listToStatiDebitoriVo(res);
      })
    );
  }

  /**
   * Funzione che ricerca i dati degli stati debitori per stesso NAP.
   * @param nap string che definisce il NAP di riferimento per lo scarico delle informazioni.
   * @param idStatiDebitori number[] con la lista degli id degli stati debitori da escludere dalla ricerca.
   * @param searchParams ISearchSDByCodiceUtenza che definisce i possibili query params di ricerca dati.
   * @returns Observable<StatoDebitorioVo[]> con i risultati della ricerca.
   */
  getStatiDebitoriByCodiceUtenza(
    codiceUtenza: string,
    idStatiDebitori: number[],
    searchParams?: ISearchSDByCodiceUtenza
  ): Observable<StatoDebitorioVo[]> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(
      this.PATH_STATI_DEBITORI,
      this.PATH_CODICE_UTENZA,
      codiceUtenza
    );

    // Tento di generare il filtro per gli stati debitori da escludere
    const body: any = idStatiDebitori ?? null;
    // Definisco una variabile contenente i query params
    let params: HttpParams;
    // Verifico l'input per i parametri di ricerca
    if (searchParams) {
      // Esistono parametri di ricerca, genero i query params
      params = this.createQueryParams(searchParams);
      // #
    }

    // Lancio la chiamata per il recupero dati
    return this._http.post<IStatoDebitorioVo[]>(url, body, { params }).pipe(
      map((res: IStatoDebitorioVo[]) => {
        // Ritorno la response convertita
        return this.listToStatiDebitoriVo(res);
      })
    );
  }

  /**
   * Funzione che ritorna un oggetto DilazioneVo dato l'id ambito e l'id tipo dilazione.
   * @param idAmbito number che definisce l'id ambito per lo scarico dati.
   * @param idTipoDilazione number che definisce l'id ambito per lo scarico dati.
   * @returns Observable<DilazioneVo> con il risultato del dato scaricato.
   */
  getDilazione(
    idAmbito: number,
    idTipoDilazione: number
  ): Observable<DilazioneVo> {
    // Definisco l'url per la chiamata
    const url = this._config.appUrl(this.PATH_DILAZIONE, idAmbito);
    // Definisco i query param per la chiamata
    const params = this._riscaUtilities.createQueryParams({ idTipoDilazione });

    // Richiamo il servizio
    return this._http.get<IDilazioneVo[]>(url, { params }).pipe(
      map((iDilazioneVo: IDilazioneVo[]) => {
        // Converto l'oggetto
        return new DilazioneVo(iDilazioneVo[0]);
      })
    );
  }

  /**
   * Funzione di ricerca che scarica gli stati debitori dati dei filtri di ricerca per morosità.
   * @param filtri IDCRicercaSDByMorosita con i dati per la ricerca degli stati debitori.
   * @param paginazione RiscaTablePagination con le informazioni di paginazione della tabella.
   * @returns Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> con il risultato della ricerca.
   */
  getSDByRicercaMorosita(
    filtri: IDCRicercaSDByMorosita,
    paginazione: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<StatoDebitorioVo[]>> {
    // Definisco l'url
    const url = this._config.appUrl(this.PATH_TIPI_RICERCA_MOROSITA);

    // Esraggo dall'oggetto di filtro, i parametri di ricerca
    const tipoRicercaMorosita = filtri?.tipoRicercaMorosita ?? null;
    const anno = filtri?.anno ?? null;
    const flgRest = filtri?.flgRest ?? null;
    const flgAnn = filtri?.flgAnn ?? null;
    const lim = filtri?.lim ?? null;
    // Definisco l'oggetto contenente i query params non di paginazione
    const queryParams = { tipoRicercaMorosita, anno, flgRest, flgAnn, lim };
    // Prendo i parametri
    const params = this._riscaUtilities.createPagingParams(
      paginazione,
      queryParams
    );
    // Definisco le options
    const options = { params };

    // Effettuo la chiamata
    return this._httpHelper.searchWithGet(url, options).pipe(
      map((res: RicercaPaginataResponse<IStatoDebitorioVo[]>) => {
        // Ritorno la response convertita
        return this.paginateStatiDebitori(res);
      })
    );
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che lancia le logiche di conversione dati e gestione paginazione per gli stati debitori.
   * @param res RicercaPaginataResponse<IStatoDebitorioVo[]> con la risposta ottenuta dal server.
   * @returns RicercaPaginataResponse<StatoDebitorioVo[]> come risposta convertita.
   */
  private paginateStatiDebitori(
    res: RicercaPaginataResponse<IStatoDebitorioVo[]>
  ): RicercaPaginataResponse<StatoDebitorioVo[]> {
    // Recupero la lista di oggetti ritornati dal server
    const { sources } = res;
    // Effettuo un parse dei dati in una lista FE friendly
    const statiDebitori: StatoDebitorioVo[] =
      this.listToStatiDebitoriVo(sources);
    // Creo una nuova response da ritornare
    const newSD: RicercaPaginataResponse<StatoDebitorioVo[]> = {
      paging: res.paging,
      sources: statiDebitori,
    };

    // Ritorno la response modificata
    return newSD;
  }

  /**
   * Funzione di supporto per convertire IStatoDebitorioVo[] in StatoDebitorioVo[];
   * @param list IStatoDebitorioVo[] da convertire.
   * @returns StatoDebitorioVo[] convertita.
   */
  private listToStatiDebitoriVo(list: IStatoDebitorioVo[]): StatoDebitorioVo[] {
    // Verifico l'input
    list = list ?? [];
    // Rimappo gli oggetti
    return list.map((iSD: IStatoDebitorioVo) => {
      // Creo l'oggetto di FE
      return new StatoDebitorioVo(iSD);
    });
  }

  /**
   * ##################################################################
   * FUNZIONI SPECIFICHE PER LA GESTIONE DELLA MODIFICA STATO DEBITORIO
   * ##################################################################
   */

  /**
   * Funzione che definisce le logiche di gestione quando la verifica, per l'aggiornamento di uno stato debitorio, genera il codice: A018.
   * @returns IActionVerifySD con la gestione delle logiche per la verifica con uno specifico risultato.
   */
  private get onVerifyModA018(): IActionHandleVerifySD {
    // Richiamo l'oggetto di configurazione specifico
    return onVerifyModA018;
  }

  /**
   * Funzione che definisce le logiche di gestione quando la verifica, per l'aggiornamento di uno stato debitorio, genera il codice: A023.
   * @returns IActionVerifySD con la gestione delle logiche per la verifica con uno specifico risultato.
   */
  private get onVerifyModA023(): IActionHandleVerifySD {
    // Richiamo l'oggetto di configurazione specifico
    return onVerifyModA023;
  }

  /**
   * Funzione che definisce le logiche di gestione quando la verifica, per l'aggiornamento di uno stato debitorio, genera il codice: A027.
   * @returns IActionVerifySD con la gestione delle logiche per la verifica con uno specifico risultato.
   */
  private get onVerifyModA027(): IActionHandleVerifySD {
    // Richiamo l'oggetto di configurazione specifico
    return onVerifyModA027;
  }

  /**
   * Funzione che definisce le logiche di gestione quando la verifica, per l'aggiornamento di uno stato debitorio, genera il codice: A028.
   * @returns IActionVerifySD con la gestione delle logiche per la verifica con uno specifico risultato.
   */
  private get onVerifyModA028(): IActionHandleVerifySD {
    // Richiamo l'oggetto di configurazione specifico
    return onVerifyModA028;
  }
}
