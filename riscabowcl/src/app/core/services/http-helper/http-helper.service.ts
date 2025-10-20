import { HttpClient, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import {
  BlobTypes,
  DynamicObjAny,
  DynamicObjString,
  ResponseTypes,
  RiscaSortTypes,
  RiscaTablePagination,
} from 'src/app/shared/utilities';
import { base64ToByteArray } from '../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import {
  RicercaIncrementaleResponse,
  RicercaPaginataResponse,
} from '../../classes/http-helper/http-helper.classes';
import { RiscaOpenFileMethods } from '../../enums/http-helper/http-helper.enums';
import { IRiscaOpenFileConfigs } from '../../interfaces/http-helper/http-helper.interfaces';
import { LoggerService } from '../logger.service';
import {
  HTTP_HEADER_RISCA_WARNING,
  HTTP_HELPER_HAS_MORE_ITEMS,
  HTTP_HELPER_OBSERVE_RESPONSE,
  HTTP_HELPER_PAGINATION_INFO,
} from './utilities/http-helper.consts';
import { RiscaServicesSortTypes } from './utilities/http-helper.enums';
import { IJsonWarning, ISortInfoRes } from './utilities/http-helper.interfaces';

/**
 * Servizio dedicato alla gestione del routing applicativo.
 * Il servizio nasce dall'esigenza di gestire in maniera particolare la componentistica dell'applicazione, permettendo la gestione personalizzata del back del broswer e del pre-caricamento delle informazioni dei componenti.
 */
@Injectable({ providedIn: 'root' })
export class HttpHelperService {
  /** String costante che definisce la chiave di gestione per le ricerche. */
  private RESPONSE = HTTP_HELPER_OBSERVE_RESPONSE;
  /** Chiave con cui il BE restituisce nell'header le informazioni sulla paginazione. */
  public PAGINATION_INFO = HTTP_HELPER_PAGINATION_INFO;
  /** Chiave con cui il BE restituisce nell'header le informazioni sulla paginazione incrementale. */
  public HAS_MORE_ITEMS = HTTP_HELPER_HAS_MORE_ITEMS;

  /**
   * Costruttore.
   */
  constructor(private _http: HttpClient, private _logger: LoggerService) {}

  /**
   * ###################################
   * FUNZIONI DI GESTIONE PER GLI HEADER
   * ###################################
   */

  /**
   * Ottiene la lista di header dalla response.
   * @param httpInfo HttpRequest<any> | HttpResponse<any> che viene dal servizio da cui estrarre i dati.
   * @returns DynamicObjAny mappa chiave/valore dei valori dell'header.
   */
  getHeaders(httpInfo: HttpRequest<any> | HttpResponse<any>): DynamicObjAny {
    // Faccio il mapping di un oggetto header in un oggetto json
    const keys = httpInfo.headers.keys();
    const headers: DynamicObjAny = {};
    // Per ogni key vado a prendere il valore e li aggiungo all'oggetto che li conterrà
    keys.forEach((key: string) => {
      headers[key] = httpInfo.headers.get(key);
    });
    // Ritorno gli header convertiti
    return headers;
  }

  /**
   * Ottiene un particolare oggetto nell'header.
   * @param paramName key dell'oggetto da prendere nell'header.
   * @param httpInfo HttpRequest<any> | HttpResponse<any> che viene dal servizio da cui estrarre i dati.
   * @returns any con il dato riferito all'header.
   */
  getHeader(
    paramName: string,
    httpInfo: HttpRequest<any> | HttpResponse<any>
  ): any {
    // Controllo se esiste la response
    if (paramName && httpInfo) {
      // Prendo il parametro dall'header della response
      return httpInfo.headers.get(paramName);
    }
    // Se non esiste la response, restituisco null
    return null;
  }

  /**
   * Ottiene un oggetto generico dagli header.
   * @param paramName key dell'oggetto da prendere nell'header.
   * @param httpInfo HttpRequest<any> | HttpResponse<any> che ha nell'header l'oggetto da prendere nell'header.
   * @returns l'oggetto generico corrispondente alla key data.
   */
  getObjectFromHeader(
    paramName: string,
    httpInfo: HttpRequest<any> | HttpResponse<any>
  ): any {
    // Ottengo l'oggetto raw dall'header
    const itemStr = this.getHeader(paramName, httpInfo);
    // Se esiste, lo trasformo
    if (itemStr) {
      // Trasformo il dato dato in un oggetto
      const itemObjCoded: DynamicObjString = JSON.parse(itemStr);
      // Rimappo chiavi e valori facendoli passare sotto la decodifica URI
      const itemObjDecode: DynamicObjString = {};

      // Itero chiavi e valori dell'oggetto codificato dal servizio
      for (let [key, value] of Object.entries(itemObjCoded)) {
        // Decodifico chiave e valore
        const decodeKey = decodeURIComponent(key);
        const decodeValue = decodeURIComponent(value);
        // Inserisco chiave e valore decodificati nel nuovo oggetto
        itemObjDecode[decodeKey] = decodeValue;
        // #
      }

      // Ritorno l'oggetto decodificato
      return itemObjDecode;
    }

    // Altrimenti restituisco un oggetto vuoto
    return {};
  }

  /**
   * Funzione che estrae dagli header l'oggetto "Jsonwarning" per le segnalazioni non bloccanti sui servizi.
   * @param response HttpResponse che ha nell'header l'oggetto da prendere nell'header.
   * @returns IJsonWarning corrispondente alla key data.
   */
  getJsonWarning(response: HttpResponse<any>): IJsonWarning {
    // Estraggo le informazioni dall'header per i warning
    const jwKey = HTTP_HEADER_RISCA_WARNING;
    const jsonWarn: IJsonWarning = this.getObjectFromHeader(jwKey, response);

    // Ritorno l'oggetto di warning
    return jsonWarn;
  }

  /**
   * Ottiene una oggetto RicercaPaginataResponse { item, paging } che contiene il contenuto del body della response.
   * e la paginazione restituita dal BE. Il body conterrà una lista di oggetti.
   * @param response HttpResponse che ha nell'header l'oggetto da prendere nell'header di tipo T, dove T è un array di oggetti.
   * @returns RicercaPaginataResponse { item, paging }:
   * - item contiene l'array di oggetti di tipo T nel body della response.
   * - paging contiene le informazioni sulla paginazione provenienti dal BE. In quest'ultimo caso è di interesse il numero totale di elementi trovati.
   */
  ricercaPaginataResponse<T extends any[]>(
    response: HttpResponse<T>
  ): RicercaPaginataResponse<T> {
    /** PARTE 1: prendo gli oggetti dal body */
    // Ottengo il body come oggetto di tipo T, dove T è un array di oggetti
    let body = { ...response.body! };
    // Creo un array risultato da mettere in lista
    const sources = [];
    // Body non è un array ma un oggetto, quindi per convertirlo devo parsarlo ed ottenere i singoli elementi da inserire in un array
    Object.keys(body).forEach((pra) => {
      // Prendo l'oggetto dal body
      const obj = body[pra];
      // Inserisco l'elemento nella lista risultato
      sources.push(obj);
    });

    /** PARTE 2: prendo le informazioni per la paginazione */
    // Definisco la chiave per il recupero dati della paginazione
    const PAG_INFO = this.PAGINATION_INFO;
    // Estraggo le informazioni sulla paginazione
    const infoPaginazione = this.getObjectFromHeader(PAG_INFO, response);
    // Divido le informazioni sulla paginazione
    const { total_elements, page, total_pages, page_size, sort } =
      infoPaginazione;
    // Per gestire il sort, vado ad operare sulla formattazione del servizio
    const sortInfo: ISortInfoRes = this.parseSortInfoResponse(sort);

    // Creo l'oggetto della paginazione e lo popolo
    const paging: RiscaTablePagination = {
      // maxPages: total_pages,
      total: parseInt(total_elements),
      currentPage: parseInt(page),
      elementsForPage: parseInt(page_size),
      sortBy: sortInfo.sortBy,
      sortDirection: sortInfo.sortDirection,
    };

    // Restituisco l'oggetto con la lista di elementi trovati e la paginazione
    return new RicercaPaginataResponse<T>({ sources: sources as T, paging });
  }

  /**
   * Ottiene una oggetto RicercaIncrementaleResponse { item, hasMoreItems } che contiene il contenuto del body della response.
   * ed un parametro che comunica se ci sono altri elementi da mostrare, restituita dal BE. Il body conterrà una lista di oggetti.
   * @param response HttpResponse che ha nell'header l'oggetto da prendere nell'header di tipo T, dove T è un array di oggetti.
   * @returns RicercaIncrementaleResponse { item, paging }:
   * - item contiene l'array di oggetti di tipo T nel body della response.
   * - paging contiene le informazioni sulla paginazione provenienti dal BE. In quest'ultimo caso è di interesse il numero totale di elementi trovati.
   */
  ricercaIncrementaleResponse<T extends any[]>(
    response: HttpResponse<T>
  ): RicercaIncrementaleResponse<T> {
    /** PARTE 1: prendo gli oggetti dal body */
    // Ottengo il body come oggetto di tipo T, dove T è un array di oggetti
    let body = { ...response.body! };
    // Creo un array risultato da mettere in lista
    const sources = [];
    // Body non è un array ma un oggetto, quindi per convertirlo devo parsarlo ed ottenere i singoli elementi da inserire in un array
    Object.keys(body).forEach((pra) => {
      // Prendo l'oggetto dal body
      const obj = body[pra];
      // Inserisco l'elemento nella lista risultato
      sources.push(obj);
    });

    /** PARTE 2: prendo le informazioni per la paginazione */
    // Estraggo le informazioni sulla paginazione
    const infoPaginazione = this.getObjectFromHeader(
      this.HAS_MORE_ITEMS,
      response
    );
    // Divido le informazioni sulla paginazione
    const { hasMoreItems } = infoPaginazione;

    // Restituisco l'oggetto con la lista di elementi trovati e la paginazione
    return new RicercaIncrementaleResponse<T>({
      sources: sources as T,
      hasMoreItems,
    });
  }

  /**
   * Funzione che effettua la conversione delle informazioni di sort generate dai servizi, in informazioni di sort compatibili con il FE.
   * @param sort string con la definizione del sort applicato dal servizio.
   * @returns ISortInfoRes con la informazioni di sort convertite.
   */
  parseSortInfoResponse(sort: string): ISortInfoRes {
    // Definisco un oggetto contenitore per la risposta
    const sortInfo: ISortInfoRes = {};
    // Verifico l'input
    if (!sort) {
      // Non c'è una configurazione per il sort
      return sortInfo;
    }

    // Verifico il primo carattere del sort ritornato
    const sortDirection = sort.charAt(0);
    // Verifico quale carattere è stato recuperato. Se non definito nei case switch, non aggiorno l'oggetto da ritornare
    switch (sortDirection) {
      case RiscaServicesSortTypes.crescente:
        // Ordinamento crescente impostato
        sortInfo.sortDirection = RiscaSortTypes.crescente;
        // Recupero il resto della string come "campo" di sort
        sortInfo.sortBy = sort.substring(1);
        break;
      // #
      case RiscaServicesSortTypes.decrescente:
        // Ordinamento decrescente impostato
        sortInfo.sortDirection = RiscaSortTypes.decrescente;
        // Recupero il resto della string come "campo" di sort
        sortInfo.sortBy = sort.substring(1);
        break;
      // #
      default:
        // Non esiste ordinamento (neutro), recupero solo il campo di sort
        const sortBy = sort.trim();
        // Verifico se esiste almeno un carattere
        if (sortBy) {
          // Lo definisco all'interno dell'oggetto
          sortInfo.sortBy = sortBy;
        }
    }

    // Gestito l'oggetto per il sort, ritorno le informazioni
    return sortInfo;
  }

  /**
   * ############################
   * FUNZIONI DI GESTIONE RICERCA
   * ############################
   */

  /**
   * Funzione di wrapping che definisce le configurazioni specifiche per effettuare una ricerca tramite: POST.
   * @param url string che definisce l'url da richiamare.
   * @param body any contenente il body per la richiesta.
   * @param options any compatibile con le firme del servizio HttpClient di Angular, come options per la chiamata.
   * @returns Observable<RicercaPaginataResponse<T>> con la risposta alla chiamata.
   */
  searchWithPost<T extends any[]>(
    url: string,
    body?: any,
    options: any = {}
  ): Observable<RicercaPaginataResponse<T>> {
    // Lancio la chiamata per il recupero dati
    return this.postWithHeader<T>(url, body, options).pipe(
      map((res: HttpResponse<T>) => {
        // Converto la response e genero l'oggetto per la paginazione
        return this.ricercaPaginataResponse<T>(res);
      })
    );
  }

  /**
   * Funzione di wrapping che definisce le configurazioni specifiche per effettuare una ricerca tramite: GET.
   * @param url string che definisce l'url da richiamare.
   * @param options any compatibile con le firme del servizio HttpClient di Angular, come options per la chiamata.
   * @returns Observable<RicercaPaginataResponse<T>> con la risposta alla chiamata.
   */
  searchWithGet<T extends any[]>(
    url: string,
    options: any = {}
  ): Observable<RicercaPaginataResponse<T>> {
    // Lancio la chiamata per il recupero dati
    return this.getWithHeader<T>(url, options).pipe(
      map((res: HttpResponse<T>) => {
        // Converto la response e genero l'oggetto per la paginazione
        return this.ricercaPaginataResponse<T>(res);
      })
    );
  }

  /**
   * Funzione di wrapping che definisce le configurazioni specifiche per effettuare una ricerca tramite: POST.
   * @param url string che definisce l'url da richiamare.
   * @param body any contenente il body per la richiesta.
   * @param options any compatibile con le firme del servizio HttpClient di Angular, come options per la chiamata.
   * @returns Observable<RicercaPaginataResponse<T>> con la risposta alla chiamata.
   */
  searchWithPostIncremental<T extends any[]>(
    url: string,
    body?: any,
    options: any = {}
  ): Observable<RicercaIncrementaleResponse<T>> {
    // Lancio la chiamata per il recupero dati
    return this.postWithHeader<T>(url, body, options).pipe(
      map((res: HttpResponse<T>) => {
        // Converto la response e genero l'oggetto per la paginazione
        return this.ricercaIncrementaleResponse<T>(res);
      })
    );
  }

  /**
   * Funzione di wrapping che definisce le configurazioni specifiche per effettuare una ricerca tramite: GET.
   * @param url string che definisce l'url da richiamare.
   * @param options any compatibile con le firme del servizio HttpClient di Angular, come options per la chiamata.
   * @returns Observable<RicercaPaginataResponse<T>> con la risposta alla chiamata.
   */
  searchWithGetIncremental<T extends any[]>(
    url: string,
    options: any = {}
  ): Observable<RicercaIncrementaleResponse<T>> {
    // Lancio la chiamata per il recupero dati
    return this.getWithHeader<T>(url, options).pipe(
      map((res: HttpResponse<T>) => {
        // Converto la response e genero l'oggetto per la paginazione
        return this.ricercaIncrementaleResponse<T>(res);
      })
    );
  }

  /**
   * Funzione di wrapping che definisce le configurazioni specifiche per effettuare una POST con la possibilità di avere gli header.
   * @param url string che definisce l'url da richiamare.
   * @param body any contenente il body per la richiesta.
   * @param options any compatibile con le firme del servizio HttpClient di Angular, come options per la chiamata.
   * @returns Observable<HttpResponse<T>> con la risposta alla chiamata.
   */
  postWithHeader<T>(
    url: string,
    body?: any,
    options: any = {}
  ): Observable<HttpResponse<T>> {
    // Aggiungo all'oggetto delle option il parametro per la gestione della ricerca
    options.observe = this.RESPONSE;
    // Lancio la chiamata per il recupero dati
    return this._http.post<T>(url, body, options).pipe(
      map((res: HttpResponse<T>) => {
        // Ritorno la response pura
        return res;
      })
    );
  }

  /**
   * Funzione di wrapping che definisce le configurazioni specifiche per effettuare una GET con la possibilità di avere gli header.
   * @param url string che definisce l'url da richiamare.
   * @param options any compatibile con le firme del servizio HttpClient di Angular, come options per la chiamata.
   * @returns Observable<T> con la risposta alla chiamata.
   */
  getWithHeader<T>(
    url: string,
    options: any = {}
  ): Observable<HttpResponse<T>> {
    // Aggiungo all'oggetto delle option il parametro per la gestione della ricerca
    options.observe = this.RESPONSE;
    // Lancio la chiamata per il recupero dati
    return this._http.get<T>(url, options).pipe(
      map((res: HttpResponse<T>) => {
        // Ritorno la response pura
        return res;
      })
    );
  }

  /**
   * #####################################
   * FUNZIONI DI DOWNLOAD ED APERTURA FILE
   * #####################################
   */

  /**
   * Esegue il download di un file.
   * @param url string con l'url del servizio che ritorna il blob con il file.
   * @param responseType ResponseType opzionale, indica il tipo della risposta.
   * @returns Observable con l'HttpResponse contenente il blob scaricato.
   */
  downloadFile(
    url: string,
    responseType: ResponseTypes = ResponseTypes.blob
  ): Observable<any> {
    // Creo le options ed assegno i parametri
    const options: any = {};
    options.responseType = responseType;
    options.observe = this.RESPONSE;
    // Eseguo la chiamata
    return this._http.get<any>(url, options);
  }

  /**
   * Scarica ed apre un file generico dal BE.
   * @param url string con l'url del servizio che fa scaricare il file.
   * @param responseType ResponseType indica il tipo di file nel body della response.
   * @param configs IRiscaOpenFileConfigs che definisce la configurazione per l'apertura del file.
   * @returns Observable con l'HttpResponse contenente il blob scaricato.
   */
  downloadAndOpenFile(
    url: string,
    responseType: ResponseTypes = ResponseTypes.blob,
    configs?: IRiscaOpenFileConfigs
  ): Observable<any> {
    // Scarico il file
    return this.downloadFile(url, responseType).pipe(
      tap((response: HttpResponse<any>) => {
        // Apro il file
        this.openFileByHTTPResponse(response, configs);
      })
    );
  }

  /**
   * Gestisce la risposta della funzione downloadFile aprendo il file scaricato in un nuovo tab.
   * @param resp la response della chiamata downloadFile.
   * @param paramConfigs IRiscaOpenFileConfigs che definisce la configurazione per l'apertura del file.
   */
  openFileByHTTPResponse(
    resp: HttpResponse<any>,
    paramConfigs?: IRiscaOpenFileConfigs
  ) {
    // Costanti di comodo
    const contentDisposition = 'Content-Disposition';
    const filenamePrefix = 'filename=';
    // Gestisco l'header
    const contentDispositionHeader = resp.headers.get(contentDisposition);
    // Prendo il nome del file
    const fileName = contentDispositionHeader.split(filenamePrefix)[1];
    // Determino il type del file dall'estensione
    const mimeType = this.mimeTypeByIRiscaOpenFileConfigs(fileName);
    // Estraggo lo stream del file dal body della response
    const stream = resp.body;

    // Definisco un oggetto con le configurazioni di default
    const defaultConfigs: IRiscaOpenFileConfigs = {
      file_name: fileName,
      stream: undefined,
      mime_type: mimeType,
    };

    // Creo la configurazione effettiva del file, partendo dall'input o settando la configurazione di default
    const openConfigs = paramConfigs ?? defaultConfigs;
    // Definisco le informazioni per lo stream dati
    openConfigs.stream = stream;

    // Apro il file
    this.openFile(openConfigs);
  }

  /**
   * Gestisce la risposta della funzione scarico del file a seconda della configurazione passata in input.
   * @param configs IRiscaOpenFileConfigs che definisce la configurazione per l'apertura del file.
   */
  openFile(configs: IRiscaOpenFileConfigs) {
    // Verifico l'input
    if (!configs) {
      // Errore bloccante
      throw new Error('openFileByConfigs - configs missing');
    }

    // Recupero le altre informazioni per l'apertura del file
    const { openMethod, file_name } = configs;

    // Verifico la metodologia scelta per l'apertura
    switch (openMethod) {
      case RiscaOpenFileMethods.download:
        // Effettuo il download diretto del file
        this.fileDownloadByConfigs(configs);
        break;
      // #
      case RiscaOpenFileMethods.openInTab:
        // Apro una nuova tab
        this.openFileInTab(configs);
        break;
      // #
      default:
        // Per default, download diretto del file
        this.fileDownloadByConfigs(configs);
    }
  }

  /**
   * Gestisce la risposta della funzione downloadFile aprendo il file scaricato in un nuovo tab.
   * @param configs IRiscaOpenFileConfigs che definisce la configurazione per l'apertura del file.
   */
  fileDownloadByConfigs(configs: IRiscaOpenFileConfigs) {
    // Vado a generare l'url per l'apertura del file
    const url = this.generateBlobUrl(configs);
    // Recupero le altre informazioni per l'apertura del file
    const { file_name } = configs;
    // Richiamo la funzione di download della risorsa generata
    this.downloadResource(url, file_name);
  }

  /**
   * Funzione che generae un link HTML per la gestione della risorsa.
   * @param url string che definisce l'url della risorsa per il download.
   * @param fileName string che definisce il nome del file della risorsa.
   * @returns HTMLAnchorElement come elemento HTML creato per la risorsa.
   */
  private createLinkResource(
    url: string,
    fileName?: string
  ): HTMLAnchorElement {
    // Creo un tag <a href="linkAllaRisorsaScaricata"> nel DOM
    const htmlLink = 'a';
    const link = document.createElement(htmlLink);

    // Assegno l'href all'<a> creato
    link.href = url;

    // Definisco un file name di fallback
    const fallBackFN = `${moment().format('YYYYMMDDHHmmssSSS')}`;
    // Richiesto il download diretto
    link.download = fileName || fallBackFN;

    // Ritorno il link creato
    return link;
  }

  /**
   * Funzione che generae un link HTML per la gestione della risorsa.
   * @param link HTMLAnchorElement con il riferimento per il download della risorsa.
   * @param fileName string che definisce il nome del file della risorsa.
   * @returns HTMLAnchorElement come elemento HTML creato per la risorsa.
   */
  private downloadLinkResource(link: HTMLAnchorElement, url?: string) {
    // Verifico esista il link per la risorsa
    if (!link) {
      // Non esiste la risorsa
      return;
    }

    // Click programmatico sull'elemento <a> per attivarlo
    link.click();
    // Verifico se è definito l'url da utilizzare per la chiusura
    if (url) {
      // Rimuovo l'oggetto creato sulla finestra
      window.URL.revokeObjectURL(url);
    }
    // Rimuovo <a>
    link.remove();
  }

  /**
   * Funzione che scarica una risorsa qualsiasi, dato l'url per lo scarico e il nome del file.
   * @param url string che definisce l'url della risorsa per il download.
   * @param fileName string che definisce il nome del file della risorsa.
   */
  downloadResource(url: string, fileName?: string) {
    // Genero l'elemento per il download
    const link = this.createLinkResource(url, fileName);
    // Lancio l'evento di download della risorsa
    this.downloadLinkResource(link, url);
  }

  /**
   * Funzione che scarica una risorsa qualsiasi, dato il nome del file.
   * NOTA BENE: la risorsa locale deve essere presente all'interno della cartella download.
   * @param fileName string che definisce il nome del file della risorsa.
   */
  downloadLocalResource(fileName?: string) {
    // Definisco l'url per il download del file
    const url: string = `assets/download/${fileName}`;
    // Genero l'elemento per il download
    const link = this.createLinkResource(url, fileName);
    // Lancio l'evento di download della risorsa
    this.downloadLinkResource(link, url);
  }

  /**
   * Gestisce la risposta della funzione downloadFile aprendo il file scaricato in un nuovo tab.
   * A seconda che sia presente o meno il nome del file, verrà gestita l'apertura in maniera differente.
   * @param configs IRiscaOpenFileConfigs che definisce la configurazione per l'apertura del file.
   */
  openFileInTab(configs: IRiscaOpenFileConfigs) {
    // Verifico l'inpuf
    if (!configs) {
      // Niente configurazione
      return;
    }

    // Recupero le altre informazioni per l'apertura del file
    const { file_name } = configs;

    // Verifico se esiste il nome del file
    if (!file_name) {
      // Apro in tab senza nome
      this.openFileInTabNoName(configs);
      // #
    } else {
      // Apro in una nuova tab con il nome del file
      this.openFileInTabWithName(configs);
    }
  }

  /**
   * Gestisce la risposta della funzione downloadFile aprendo il file scaricato in un nuovo tab.
   * @param configs IRiscaOpenFileConfigs che definisce la configurazione per l'apertura del file.
   */
  openFileInTabNoName(configs: IRiscaOpenFileConfigs) {
    // Vado a generare l'url per l'apertura del file
    const url = this.generateBlobUrl(configs);
    // Apro una nuova tab
    window.open(url);
  }

  /**
   * Gestisce la risposta della funzione downloadFile aprendo il file scaricato in un nuovo tab.
   * @param configs IRiscaOpenFileConfigs che definisce la configurazione per l'apertura del file.
   */
  openFileInTabWithName(configs: IRiscaOpenFileConfigs) {
    // Vado a generare l'url per l'apertura del file
    const url = this.generateBlobUrl(configs);
    // Recupero il file name dalla configurazione
    const { file_name } = configs;

    // Genero l'elemento per il download
    const link = this.createLinkResource(url, file_name);
    // Definisco per il link che il target di apertura è una nuova tab
    link.target = '_blank';
    // Lancio l'evento di download della risorsa
    this.downloadLinkResource(link, url);
  }

  /**
   * Funzione che estrae il MIMEType in base alla configurazione in input.
   * @param fileName string nome del file.
   * @param configs IRiscaOpenFileConfigs configurazioni del mimeType.
   * @returns string MIMEType.
   */
  private mimeTypeByIRiscaOpenFileConfigs(
    fileName: string,
    configs?: IRiscaOpenFileConfigs
  ): string {
    // Se non c'è una configurazione, la ricavo dal nome del file
    if (!configs) {
      return this.mimeTypeByFilename(fileName);
    }
    // Estraggo le possibili configurazioni dall'oggetto
    const { extension, mime_type: mimeType } = configs;
    // Diamo priorità al mimetype se definito
    if (mimeType != undefined && mimeType != '') {
      // Ritorno direttamente il mimetype
      return mimeType;
    }
    // Controllo se esiste l'estensione
    if (extension != undefined && extension != '') {
      // Ritorno il mimetype in base all'estensione
      return this.mimeTypeByExtentionFile(extension);
    }
    // Se non si entra nelle condizioni ritorno per filename
    return this.mimeTypeByFilename(fileName);
  }

  /**
   * Ottiene il MIME type dal nome del file in base all'estensione.
   * @param fileName il nome completo del file.
   * @returns string del MIME type del file.
   */
  private mimeTypeByFilename(fileName: string): string {
    // Controllo di esistenza
    if (fileName == null || fileName.length == 0 || fileName.indexOf('.') < 0) {
      return '';
    }
    // Splitto il nome del file e lo ribalto
    const names = fileName.split('.').reverse();
    // Prendo l'estensione
    const ext = names[0].toLowerCase();
    // Se esiste lo ritorno
    return this.mimeTypeByExtentionFile(ext);
  }

  /**
   * Ottiene il MIME type dall'estensione.
   * @param ext string con l'estensione di un file (senza punto)
   * @returns BlobTypes tipo del file
   */
  private mimeTypeByExtentionFile(ext: string) {
    // Recupero dall'enum dei tipi file accedendo per proprietà
    const type = BlobTypes[ext];
    // Se esiste lo ritorno
    return type ?? '';
  }

  /**
   * Gestisce l'oggetto risca per l'apertura di un file e ritorna una stringa come url generato dal blob dati.
   * @param configs IRiscaOpenFileConfigs che definisce la configurazione per l'apertura del file.
   * @returns string con le informazioni per l'apertura del file.
   */
  private generateBlobUrl(configs: IRiscaOpenFileConfigs): string {
    // La configurazione è obbligatoria
    if (!configs) {
      // Scrivo log per identificare l'errore
      this._logger.error('http-helper.service.ts - configuration', configs);
      // Lancio un errore
      throw new Error('openFile - no configuration defined');
    }

    // Recupero dall'oggetto le informazioni per il file
    let { file_name, stream, convertBase64 } = configs;
    // fileName e stream sono obbligatori
    if (!file_name || !stream) {
      // Scrivo log per identificare l'errore
      this._logger.error('http-helper.service.ts - file name', file_name);
      this._logger.error('http-helper.service.ts - stream', stream);
      // Lancio un errore
      throw new Error('openFile - file_name or stream undefined');
    }

    // Determino il type del file dall'estensione
    const type = this.mimeTypeByIRiscaOpenFileConfigs(file_name, configs);

    // Definisco l'array di byte per generare un Blob
    let byteArray: any[] = [];
    // Verifico se lo stream è da convertire da Base64
    if (convertBase64 == undefined || convertBase64 === true) {
      // Converto l'informazione del Base64 in un byte array
      byteArray = base64ToByteArray(stream);
      // #
    } else {
      // Definisco il byte array come un array dello stream
      byteArray = [stream];
      // #
    }

    // Creo un blob a partire dallo stream in input
    const blob = new Blob(byteArray, { type });
    // Creo l'url per accedere alla risorsa scaricata
    const url = window.URL.createObjectURL(blob);

    // Ritorno l'url generato dal blob
    return url;
  }

  /**
   * #####################################
   * FUNZIONI DI SUPPORTO SUI QUERY PARAMS
   * #####################################
   */

  /**
   * Funzione di supporto che converte una lista di dati primitivi in una stringa compatibile per la gestione come query param.
   * @param primitivi string[] | number[] da convertire.
   * @param separatore string che definisce il separatore tra le informazioni. Se non definito, per default è: ','.
   * @returns string con il risultato della conversione. Se qualcosa non è andato a buon fine, ritorna stringa vuota.
   */
  primitiveListToQueryParam(
    primitivi: string[] | number[],
    separatore: string = '_'
  ): string {
    // Verifico l'input
    if (!primitivi) {
      // Manca la configurazione
      return '';
    }

    // Per generare il query param devo avere una lista di stringhe su cui lavorare
    let primitiviStr: string[] = [];
    // Converto la lista in input in tutte stringhe
    primitivi.forEach((p: string | number) => {
      // Converto e "pulisco" il primitivo di partenza
      const pStr = p?.toString()?.trim() ?? '';
      // Metto l'elemento nell'array se non è ''
      if (pStr != '') {
        // Pusho nell'array
        primitiviStr.push(pStr);
      }
    });

    // Effettuo una join tra le informazioni
    const queryParam: string = primitiviStr.join(separatore);

    // Ritorno la stringa generata
    return queryParam;
  }
}
