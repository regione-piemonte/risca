import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { chunk } from 'lodash';
import { Observable, Subject } from 'rxjs';
import { takeUntil, tap } from 'rxjs/operators';
import {
  AllegatoVo,
  DocumentoAllegatoVo,
  FileACTAVo,
} from 'src/app/core/commons/vo/documento-allegato-vo';
import { IRiscaOpenFileConfigs } from 'src/app/core/interfaces/http-helper/http-helper.interfaces';
import { ConfigService } from 'src/app/core/services/config.service';
import { HttpHelperService } from 'src/app/core/services/http-helper/http-helper.service';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { DocumentiAllegatiTablePagination } from '../../../../shared/classes/risca-table/documenti-allegati/documenti-allegati.table';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import {
  RiscaServerError,
  RiscaTablePagination,
} from '../../../../shared/utilities';
import {
  IParamsDocumentiAllegati,
  IRicercaDocumentiAllegati,
} from './utilities/documenti-allegati.interfaces';

@Injectable({ providedIn: 'root' })
export class DocumentiAllegatiService extends HttpUtilitiesService {
  /** Costante per il path: /classificazione */
  private PATH_DOC_ALLEGATI = '/classificazioni';
  /** Costante per il path: /allegati */
  private PATH_ALLEGATI = '/allegati';
  /** Costante per il path: /actaContentStream */
  private PATH_DOWNLOAD = '/actaContentStream';

  /** Subject che permette di interrompere una chiamata ai documenti allegati, se l'utente chiude la riscossione. */
  private stopDocsDownload: Subject<void>;
  /** Subject contenente il valore per la gestione dei documenti allegati di una riscossione. */
  documentiAllegatiSuccess$: Subject<
    RicercaPaginataResponse<DocumentoAllegatoVo[]>
  > = new Subject<RicercaPaginataResponse<DocumentoAllegatoVo[]>>();
  /** Subject contenente l'errore generato allo scaricamento dei documenti allegati di una riscossione. */
  documentiAllegatiErrors$: Subject<RiscaServerError> =
    new Subject<RiscaServerError>();

  /** DocumentoAllegatoVo[] con la lista dei documenti allegati scaricati per una pratica. */
  private _documentiAllegati: DocumentoAllegatoVo[];
  /** RiscaServerError con l'errore generato dallo scarico dei documenti allegati.  */
  private _documentiAllegatiError: RiscaServerError;

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
   * Avvia la ricerca dei documenti ed allegati della riscossione, filtrati in base alla stringa "filtro" in input.
   * @param idRiscossione number con l'id della riscossione per la ricerca dati.
   * @param queryParams IParamsDocumentiAllegati con i possibili query params per la ricerca dati.
   * @returns Observable<DocumentoAllegatoVo[]> con la lista di risultati.
   */
  ricercaDocumentiEAllegati(
    idRiscossione: number,
    queryParams?: IParamsDocumentiAllegati
  ): Observable<DocumentoAllegatoVo[]> {
    // Creo l'oggetto di ricerca
    const ricerca: IRicercaDocumentiAllegati = {
      ricerca: queryParams?.ricerca,
      fruitore: queryParams?.fruitore,
    };
    // Definisco l'url
    const url = this.appUrl(this.PATH_DOC_ALLEGATI, idRiscossione);
    // Definisco i query params
    const params = this.createQueryParams(ricerca);
    // Opzioni
    const options = { params };
    // Effettuo la chiamata
    return this._http.get<DocumentoAllegatoVo[]>(url, options);
  }

  /**
   * Avvia la ricerca dei documenti ed allegati della riscossione, filtrati in base alla stringa "filtro" in input
   * @param idRiscossione id della riscossione corrente
   * @param dbKeyClassificazione stringa con ulteriore criterio di filtro
   * @returns Observable<DocumentoVo[]> con la lista di allegati dal BE
   */
  getAllegati(
    idRiscossione: number,
    dbKeyClassificazione: string
  ): Observable<AllegatoVo[]> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_ALLEGATI, dbKeyClassificazione);
    // Creo l'oggetto di ricerca
    // Definisco i query params per la chiamata
    const params = this.createQueryParams({ idRiscossione });
    // Definisco le options
    const options = { params };
    // Effettuo la chiamata
    return this._http.get<AllegatoVo[]>(url, options);
  }

  /**
   * Avvia la ricerca dei documenti ed allegati della riscossione, filtrati in base alla stringa "filtro" in input
   * @param idClassificazione id della riscossione corrente
   * @returns Observable<FileACTAVo> con l'HttpResponse con dentro il file
   */
  scaricaEApriFile(
    idRiscossione: number,
    idClassificazione: string
  ): Observable<FileACTAVo> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_DOWNLOAD, idClassificazione);
    // Definisco i query params per la chiamata
    const params = this.createQueryParams({ idRiscossione });
    // Definisco le options
    const options = { params };
    // Effettuo la chiamata
    return this._http.get<FileACTAVo>(url, options).pipe(
      tap((file: FileACTAVo) => {
        // Genero la configurazione del file
        const configs: IRiscaOpenFileConfigs = {
          file_name: file.filename,
          stream: file.streamMTOM,
          mime_type: file.mimeType,
        };
        // Chiamo l'apertura del file passando la configurazione
        this._httpHelper.fileDownloadByConfigs(configs);
      })
    );
  }

  /**
   * ##################################
   * GESTIONE DOCUMENTI ALLEGATI LOCALE
   * ##################################
   */

  /**
   * Avvia la ricerca dei documenti ed allegati della riscossione, filtrati in base alla stringa "filtro" in input.
   * La funzione è pensata per essere chiamata una volta sola al caricamento dei dati della riscossione.
   * Le informazioni riguardanti i documenti allegati verranno poi gestiti in sessione mediante altre funzioni specifiche.
   * @param idRiscossione number con l'id della riscossione per la ricerca dati.
   * @param queryParams IParamsDocumentiAllegati con i possibili query params per la ricerca dati.
   * @param paginazione RiscaTablePagination con la paginazione iniziale per la chiamata.
   */
  avviaRicercaDocEAllLocale(
    idRiscossione: number,
    queryParams?: IParamsDocumentiAllegati,
    paginazione?: RiscaTablePagination
  ) {
    // Verifico se esiste il parametro per la paginazione altrimenti creo un default
    paginazione = this.paginazioneOrDocsDefault(paginazione);

    // Creo l'oggetto di ricerca
    const ricerca: IRicercaDocumentiAllegati = {
      ricerca: queryParams?.ricerca,
      fruitore: queryParams?.fruitore,
    };
    // Definisco l'url
    const url = this.appUrl(this.PATH_DOC_ALLEGATI, idRiscossione);
    // Definisco i query params
    const params: HttpParams = this.createQueryParams(ricerca);
    // Definisco l'header per non attivare lo spinner
    const headers = this.headerNoSpinner();
    // Opzioni
    const options = { params, headers };

    // Resetto le informazioni per quello che riguarda i dati dei documenti allegati
    this.resetDocsData();
    // Inizializzo il subject per la gestione della chiamata
    this.stopDocsDownload = new Subject<void>();

    // Assegno la chiamata con scarico dati alla variabile del servizio
    this._http
      .get<DocumentoAllegatoVo[]>(url, options)
      .pipe(takeUntil(this.stopDocsDownload))
      .subscribe({
        next: (docAll: DocumentoAllegatoVo[]) => {
          // Salvo localmente le informazioni dei documenti allegati
          this._documentiAllegati = docAll;
          // Converto la risposta in un oggetto di ricerca paginata
          let ricerca: RicercaPaginataResponse<DocumentoAllegatoVo[]>;
          ricerca = this.documentiAllegatiToRicercaPaginata(
            docAll,
            paginazione
          );
          // Aggiorno le informazioni tramite subject
          this.documentiAllegatiSuccess$.next(ricerca);
          // #
        },
        error: (e: RiscaServerError) => {
          // Salvo localmente l'errore per i documenti allegati
          this._documentiAllegatiError = e;
          // Aggiorno le informazioni tramite subject
          this.documentiAllegatiErrors$.next(e);
          // #
        },
      });
  }

  /**
   * Funzione che gestisce il cambio pagina per quanto riguarda i dati dei documenti allegati.
   * @param paginazione RiscaTablePagination con la paginazione iniziale per la chiamata.
   * @returns Observable<RicercaPaginataResponse<DocumentoAllegatoVo[]>> con le informazioni di ricerca paginate.
   */
  cambiaPaginaDocEAllLocale(
    paginazione?: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<DocumentoAllegatoVo[]>> {
    // Salvo localmente le informazioni dei documenti allegati
    const docAll: DocumentoAllegatoVo[] = this.documentiAllegati;
    // Vado a generare un oggetto di ricerca sulla base della paginazione in input
    let ricerca: RicercaPaginataResponse<DocumentoAllegatoVo[]>;
    ricerca = this.documentiAllegatiToRicercaPaginata(docAll, paginazione);

    // La gestione dell'observable verrà fatto manualmente con un timeout
    return this._riscaUtilities.responseWithDelay(ricerca, 750);
  }

  /**
   * Funzione di comodo che combina le informazioni per i documenti allegati e una paginazione in un oggetto di ricerca.
   * @param documentiAllegati DocumentoAllegatoVo[] per la composizione del dato di ritorno.
   * @param paginazione RiscaTablePagination per la composizione del dato di ritorno.
   * @returns RicercaPaginataResponse<DocumentoAllegatoVo[]> con le informazioni di ricerca.
   */
  private documentiAllegatiToRicercaPaginata(
    documentiAllegati: DocumentoAllegatoVo[],
    paginazione?: RiscaTablePagination
  ): RicercaPaginataResponse<DocumentoAllegatoVo[]> {
    // Verifico l'input e gestisco i dati
    let da: DocumentoAllegatoVo[];
    da = documentiAllegati ? [...documentiAllegati] : [];
    // Definisco i dati della paginazione
    let p: RiscaTablePagination;
    p = this.paginazioneOrDocsDefault(paginazione);
    p.total = da.length;

    // Estraggo dai dati della paginazione i parametri per paginare i documenti allegati
    const { currentPage, elementsForPage } = p;
    // Vado spezzettare l'array in sotto array, composti da un numero di elementi definito da: "elemetsForPage"
    let matrixChunks: DocumentoAllegatoVo[][];
    matrixChunks = chunk(da, elementsForPage);

    // Il source è definito dal chunk della matrice alla posizione della proprietà "currentPage" -1
    let sources: DocumentoAllegatoVo[];
    sources = matrixChunks[currentPage - 1];
    // La paginazione la copio dalle informazioni locali
    let paging: RiscaTablePagination;
    paging = p;

    // Creo un oggetto e ritorno le informazioni
    return new RicercaPaginataResponse({ sources, paging });
  }

  /**
   * Funzione di comodo che va a resettare le informazioni impostate per una pratica.
   */
  resetDocsData() {
    // Resetto le informazioni per quello che riguarda i dati dei documenti allegati
    this._documentiAllegati = undefined;
    this._documentiAllegatiError = undefined;

    // Metto un try catch di sicurezza
    try {
      // Gestisco il subject per interrompere le chiamate in corso ai documenti allegati
      this.stopDocsDownload?.next();
      this.stopDocsDownload?.complete();
      this.stopDocsDownload = undefined;
      // #
    } catch (e) {}
  }

  /**
   * Funzione di supporto che gestisce la logica per la definizione della paginazione d'applicare ai documenti allegati.
   * Se non viene definita una specifica paginazione, verrà utilizzata quella di default per i documenti allegati.
   * @param p RiscaTablePagination con la configurazione in input.
   */
  private paginazioneOrDocsDefault(
    p?: RiscaTablePagination
  ): RiscaTablePagination {
    // Ritorono la paginazione in input, se definita, altrimenti il default per i documenti allegati
    return p ?? DocumentiAllegatiTablePagination();
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica se i dati sono in caricamento.
   * @returns boolean che definisce se lo stato di caricamento dati è in corso.
   */
  get isDocsLoading(): boolean {
    // I documenti allegati sono in caricamento se nel servizio i dati hanno un tipo di valore
    const noDocs: boolean = this.documentiAllegati === undefined;
    const noErrors: boolean = this.documentiAllegatiError === undefined;

    // Ritorno la condizione per: non ci sono docs e non ci sono errori
    return noDocs && noErrors;
  }

  /**
   * Getter che verifica se i documenti allegati sono stati caricati e pronti all'uso.
   * @returns boolean con il risultato del check.
   */
  get docsLoaded(): boolean {
    // I documenti allegati sono caricati se esiste l'array in sessione
    const docsLoaded: boolean = this._documentiAllegati !== undefined;
    // Ritorno la condizione
    return docsLoaded;
  }

  /**
   * Getter che verifica se i documenti allegati non sono stati scaricati e hanno prodotto un errore.
   * @returns boolean con il risultato del check.
   */
  get docsErrors(): boolean {
    // I documenti allegati sono in errore, se l'oggetto con i dettagli è in sessione
    const docsErrors: boolean = this._documentiAllegatiError !== undefined;
    // Ritorno la condizione
    return docsErrors;
  }

  /**
   * Getter che ritorna le informazioni riguardanti lo scarico dei documenti allegati.
   * @returns DocumentoAllegatoVo[] con le informazioni in sessione.
   */
  get documentiAllegati(): DocumentoAllegatoVo[] {
    // Ritorno le informazioni per la ricerca
    return this._documentiAllegati;
  }

  /**
   * Getter che ritorna le informazioni riguardanti lo scarico dei documenti allegati.
   * @returns RiscaServerError con le informazioni in sessione.
   */
  get documentiAllegatiError(): RiscaServerError {
    // Ritorno le informazioni per la ricerca
    return this._documentiAllegatiError;
  }
}
