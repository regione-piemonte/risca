import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { cloneDeep } from 'lodash';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { RicercaPaginataResponse } from 'src/app/core/classes/http-helper/http-helper.classes';
import {
  ElaborazioneVo,
  IElaborazioneVo,
} from 'src/app/core/commons/vo/elaborazione-vo';
import { StatoElaborazioneVo } from 'src/app/core/commons/vo/stato-elaborazione-vo';
import {
  ITipoElaborazioneVo,
  TipoElaborazioneVo,
} from 'src/app/core/commons/vo/tipo-elaborazione-vo';
import { ConfigService } from 'src/app/core/services/config.service';
import { HttpHelperService } from 'src/app/core/services/http-helper/http-helper.service';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IRicercaElaborazioni,
  RiscaAmbiti,
  RiscaCodiciRaggruppamenti,
  ServerNumberAsBoolean,
} from 'src/app/shared/utilities';
import { ParametroElaborazioneVo } from '../../../../core/commons/vo/parametro-elaborazione-vo';
import { TipoModalitaPagamentoVo } from '../../../../core/commons/vo/tipo-modalita-pagamento-vo';
import { TipoRicercaPagamentoVo } from '../../../../core/commons/vo/tipo-ricerca-pagamento-vo';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import { getParametroElaborazione } from '../../consts/pagamenti/pagamenti.functions';
import { TipiVerificaElabora } from '../../enums/bollettini/bollettini.enums';
import { RiscaTablePagination } from './../../../../shared/utilities/classes/utilities.classes';

/**
 * Interfaccia di appoggio per i filtri di ricerca per le elaborazioni massive.
 */
interface IRicercaElaborazioniFilter {
  idAmbito: number;
  codTipoElabora?: string;
  codStatoElabora?: string;
  dataRichiestaInizio?: string;
  dataRichiestaFine?: string;
  codFunzionalita?: string;
}

@Injectable({
  providedIn: 'root',
})
export class PagamentiService extends HttpUtilitiesService {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** Costante per il path: /elabora */
  private PATH_ELABORA = '/elabora';
  /** Costante per il path: /tipi-elaborazione */
  private PATH_TIPI_ELABORAZIONE = '/tipi-elaborazione';
  /** Costante per il path: /stato-elaborazione */
  private PATH_STATI_ELABORAZIONE = '/stato-elaborazione';
  /** Costante per il path: /_verifyElabora */
  private PATH_VERIFY_ELABORA = '/_verifyElabora';
  /** Costante per il path: /tipi-ricerca-pagamenti */
  private PATH_TIPI_RIRCERCA_PAGAMENTI = '/tipi-ricerca-pagamenti';
  /** Costante per il path: /tipi-modalita-pagamenti */
  private PATH_MODALITA_PAGAMENTO = '/tipi-modalita-pagamenti';

  /** TipoRicercaPagamentoVo[] con la lista dei tipi ricerca pagamenti scaricati. */
  private _tipiRicercaPagamenti: TipoRicercaPagamentoVo[];
  /** TipoModalitaPagamentoVo[] con la lista delle modalità di pagamento scaricate. */
  private _modalitaPagamento: TipoModalitaPagamentoVo[];

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
   * Funzione che recupera i tipi elaborazione a seconda dei parametri di input.
   * @param idAmbito number con id ambito per la ricerca.
   * @param idFunzionalita number con l'id della funzionalità per la ricerca.
   * @param flgVisibile ServerNumberAsBoolean che definisce la visibilità della bollettazione.
   * @returns Observable<TipoElaborazioneVo[]> con i dati recuperati dal servizio.
   */
  getTipiElaborazione(
    idAmbito: number,
    idFunzionalita: number,
    flgVisibile: ServerNumberAsBoolean
  ): Observable<TipoElaborazioneVo[]> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(this.PATH_TIPI_ELABORAZIONE);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      idAmbito,
      idFunzionalita,
      flgVisibile,
    });

    // Scarico i dati
    return this._http.get<ITipoElaborazioneVo[]>(url, { params }).pipe(
      map((iTipiElaborazioneVo: ITipoElaborazioneVo[]) => {
        // Converto l'oggetto
        return iTipiElaborazioneVo?.map((iTEVo: ITipoElaborazioneVo) => {
          // Rimappo l'oggetto facendo una new
          return new TipoElaborazioneVo(iTEVo);
        });
      })
    );
  }

  /**
   * Funzione che recupera gli stati di elaborazione a seconda dei parametri di input.
   * @param idAmbito number con id ambito per la ricerca.
   * @param idFunzionalita number con l'id della funzionalità per la ricerca.
   * @returns Observable<StatoElaborazioneVo[]> con i dati recuperati dal servizio.
   */
  getStatiElaborazione(
    idAmbito: number,
    idFunzionalita: number
  ): Observable<StatoElaborazioneVo[]> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(this.PATH_STATI_ELABORAZIONE);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      idAmbito,
      idFunzionalita,
    });

    // Scarico i dati
    return this._http.get<StatoElaborazioneVo[]>(url, { params });
  }

  /**
   * Funzione ricerca i dati per le elaborazioni massive.
   * @param idAmbito number che definisce l'id ambito per la ricerca.
   * @param filtri IRicercaElaborazioni che definisce i filtri di ricerca.
   * @returns Observable<Elaborazione[]> con i risultati della ricerca.
   */
  getElaborazioni(
    idAmbito: number,
    filtri: IRicercaElaborazioni
  ): Observable<ElaborazioneVo[]> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(this.PATH_ELABORA);
    // Creo l'oggetto di ricerca
    const ricerca: IRicercaElaborazioniFilter = {
      idAmbito,
      codTipoElabora: filtri?.tipo?.cod_tipo_elabora,
      codStatoElabora: filtri?.stato?.cod_stato_elabora,
      dataRichiestaInizio: filtri?.dataRichiestaInizio,
      dataRichiestaFine: filtri?.dataRichiestaFine,
    };

    // Prendo i parametri
    const params = this._riscaUtilities.createQueryParams(ricerca);
    // Definisco le options
    const options = { params };

    // Lancio la chiamata per il recupero dati
    return this._http.get<IElaborazioneVo[]>(url, options).pipe(
      map((iElaborazioniVo: IElaborazioneVo[]) => {
        // Converto l'oggetto
        return iElaborazioniVo?.map((iEVo: IElaborazioneVo) => {
          // Rimappo l'oggetto facendo una new
          return new ElaborazioneVo(iEVo);
        });
      })
    );
  }

  /**
   * Funzione ricerca i dati per le elaborazioni massive.
   * @param idAmbito number che definisce l'id ambito per la ricerca.
   * @param filtri IRicercaElaborazioni che definisce i filtri di ricerca.
   * @param paginazione RiscaTablePagination dati di paginazione della tabella.
   * @returns Observable<RicercaPaginataResponse<Elaborazione[]>> con i risultati della ricerca.
   */
  getElaborazioniPagination(
    idAmbito: number,
    filtri: IRicercaElaborazioni,
    paginazione?: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<ElaborazioneVo[]>> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(this.PATH_ELABORA);
    // Creo l'oggetto di ricerca
    const ricerca: IRicercaElaborazioniFilter = {
      idAmbito,
      codTipoElabora: filtri?.tipo?.cod_tipo_elabora,
      codStatoElabora: filtri?.stato?.cod_stato_elabora,
      dataRichiestaInizio: filtri?.dataRichiestaInizio,
      dataRichiestaFine: filtri?.dataRichiestaFine,
      codFunzionalita: filtri?.codFunzionalita,
    };

    // Prendo i parametri
    const params = this._riscaUtilities.createPagingParams(
      paginazione,
      ricerca
    );
    // Definisco le options
    const options = { params };

    // Lancio la chiamata per il recupero dati
    return this._httpHelper.searchWithGet<IElaborazioneVo[]>(url, options).pipe(
      map((ricercaVo: RicercaPaginataResponse<IElaborazioneVo[]>) => {
        // Creo un oggetto per la conversione
        const ricerca = new RicercaPaginataResponse<ElaborazioneVo[]>();
        // Converto le informazioni specifiche
        const sVo = ricercaVo.sources;
        const s = sVo?.map((iEVo: IElaborazioneVo) => {
          // Rimappo l'oggetto facendo una new
          return new ElaborazioneVo(iEVo);
        });
        // Aggiorno l'oggetto ricerca con le nuove informazioni
        ricerca.paging = ricercaVo.paging;
        ricerca.sources = s;

        // Ritorno l'oggetto rimappato
        return ricerca;
      })
    );
  }

  /**
   * Funzione che recupera i tipi ricerca pagamenti esistenti.
   * @returns Observable<TipoRicercaPagamentoVo[]> con la lista degli elementi tornati dal servizio.
   */
  getTipiRicercaPagamento(): Observable<TipoRicercaPagamentoVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._tipiRicercaPagamenti?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._tipiRicercaPagamenti);
    }

    // Definisco l'url
    const url = this.appUrl(this.PATH_TIPI_RIRCERCA_PAGAMENTI);
    // Richiamo il servizio
    return this._http.get<TipoRicercaPagamentoVo[]>(url).pipe(
      tap((trPagamenti: TipoRicercaPagamentoVo[]) => {
        // Assegno localmente il risultato
        this._tipiRicercaPagamenti = cloneDeep(trPagamenti);
      })
    );
  }

  /**
   * Funzione che recupera le modalità di pagamento esistenti.
   * @returns Observable<TipoModalitaPagamentoVo[]> con la lista degli elementi tornati dal servizio.
   */
  getModalitaPagamento(): Observable<TipoModalitaPagamentoVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._modalitaPagamento?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._modalitaPagamento);
    }

    // Definisco l'url
    const url = this.appUrl(this.PATH_MODALITA_PAGAMENTO);
    // Richiamo il servizio
    return this._http.get<TipoModalitaPagamentoVo[]>(url).pipe(
      tap((modalitaPagamenti: TipoModalitaPagamentoVo[]) => {
        // Assegno localmente il risultato
        this._modalitaPagamento = cloneDeep(modalitaPagamenti);
      })
    );
  }

  /**
   * Funzione di POST per un oggetto Elaborazione.
   * @param elaborazione Elaborazione da inserire come dato.
   * @returns Elaborazione come oggetto salvato su DB.
   */
  insertElaborazione(elaborazione: ElaborazioneVo): Observable<ElaborazioneVo> {
    // Converto l'oggetto da Elaborazione a ElaborazioneVo
    const iEVo: IElaborazioneVo = elaborazione?.toServerFormat();

    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(this.PATH_ELABORA);
    // Lancio la chiamata per l'inserimento dati
    return this._http.post<IElaborazioneVo>(url, iEVo).pipe(
      map((rIEVo: IElaborazioneVo) => {
        // Converto l'oggetto
        return new ElaborazioneVo(rIEVo);
      })
    );
  }

  /**
   * Funzione di PUT per un oggetto Elaborazione.
   * @param elaborazione Elaborazione d'aggiornare come dato.
   * @returns Elaborazione come oggetto aggiornato su DB.
   */
  updateElaborazione(elaborazione: ElaborazioneVo): Observable<ElaborazioneVo> {
    // Converto l'oggetto da Elaborazione a ElaborazioneVo
    const iEVo: IElaborazioneVo = elaborazione?.toServerFormat();

    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(this.PATH_ELABORA);
    // Lancio la chiamata per l'inserimento dati
    return this._http.put<IElaborazioneVo>(url, iEVo).pipe(
      map((rIEVo: IElaborazioneVo) => {
        // Converto l'oggetto
        return new ElaborazioneVo(rIEVo);
      })
    );
  }

  /**
   * Funzione che definisce le logiche di verifica per permettere le azioni all'interno dell'applicazione.
   * Dato un tipo elaborazione e il tipo di verifica, verrà controllato se ci sono delle elaborazioni che già occupano lo slot dell'azione (true) o nessuna elaborazione ha lo stato di verifica (false).
   * @param idAmbito number con id ambito per la verifica.
   * @deprecated idTipoElabora number che definisce il tipo elaborazione per cui si chiede la verifica per la possibilità d'inserimento di una elaborazione.
   * @param verifica TipiVerificaElabora con la tipologia di verifica da controllare.
   * @returns Observable<boolean> che definisce se ci sono elaborazioni con lo stato richiesto in base alla verifica.
   */
  verifyElaborazione(
    idAmbito: number,
    // idTipoElabora: number,
    verifica: TipiVerificaElabora
  ): Observable<boolean> {
    // Costruisco l'url di richiesta
    const url: string = this._config.appUrl(this.PATH_VERIFY_ELABORA);
    // Prendo i parametri
    // RISCA-762: è stato richiesto di verificare per ogni elaborazione sempre tutti i 4 i tipi elaborazione attualmente presenti in app (23/02/2024). Si è optato per rimuovere il query param, perché comunque tutti cadranno sempre nella stessa logica.
    //            Non sono sicuro che però sia la cosa giusta, quindi commento il parametro, perché se aggiungeranno altri tipi elaborazioni, tutta la logica non starà più in piedi.
    const params = this._riscaUtilities.createQueryParams({
      idAmbito,
      // idTipoElabora,
      verifica,
    });
    // Definisco le options
    const options = { params };

    // Lancio la chiamata per l'inserimento dati
    return this._http.get<ElaborazioneVo[]>(url, options).pipe(
      map((elaborazioni: ElaborazioneVo[]) => {
        // Verifico se esistono o non esistono elaborazioni
        return elaborazioni?.length > 0;
      })
    );
  }

  /**
   * #######################
   * FUNZIONALITA' DI COMODO
   * #######################
   */

  /**
   * Funzione che recupera, dalla proprietà "parametri" di un oggetto Elaborazione, un oggetto specifico data una chiave di ricerca.
   * @param proprieta Array di ParametroElaborazioneVo contenente le informazioni dei parametri di un oggetto Elaborazione/ElaborazioneVo.
   * @param chiaveParametro string che definisce la chiave per il recupero dello specifica parametro dall'oggetto elaborazione.
   * @returns ParametroElaborazioneVo con i dati estratti. Altrimenti undefined.
   */
  getParametroElaborazione(
    proprieta: ParametroElaborazioneVo[],
    chiaveParametro: string
  ): ParametroElaborazioneVo {
    // Richiamo la funzione di comodo
    return getParametroElaborazione(proprieta, chiaveParametro);
  }

  /**
   * Funzione che recupera il codice del raggruppamento dato un ambito.
   * @param idAmbito number che definisce il tipo raggruppamento riferito ad un ambito.
   * @returns string che definisce il codice per un determinato raggruppamento.
   */
  raggruppamentoByAmbito(idAmbito: number): string {
    // Verifico l'id ambito
    switch (idAmbito) {
      case RiscaAmbiti.ambiente:
        // Ritorno il codice per ambiente
        return RiscaCodiciRaggruppamenti.ambiente;
      default:
        // Nessun codice censito
        return '';
    }
  }
}
