import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { cloneDeep } from 'lodash';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ConfigService } from 'src/app/core/services/config.service';
import { HttpHelperService } from 'src/app/core/services/http-helper/http-helper.service';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { ElaborazioneVo } from '../../../../core/commons/vo/elaborazione-vo';
import { ParametroElaborazioneVo } from '../../../../core/commons/vo/parametro-elaborazione-vo';
import { StatoElaborazioneVo } from '../../../../core/commons/vo/stato-elaborazione-vo';
import { TipoElaborazioneVo } from '../../../../core/commons/vo/tipo-elaborazione-vo';
import { UserService } from '../../../../core/services/user.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IRicercaBollettini,
  IRicercaElaborazioni,
  RiscaTablePagination,
  ServerNumberAsBoolean,
} from '../../../../shared/utilities';
import { getParametroElaborazioneBollettini } from '../../consts/bollettini/bollettini.functions';
import { PagamentiFunzionalitaConst } from '../../consts/pagamenti/pagamenti.consts';
import { TipiVerificaElabora } from '../../enums/bollettini/bollettini.enums';
import { CodiciStatiElaborazione } from '../../enums/gestione-pagamenti/stato-elaborazione.enums';
import { TipoParametroElaborazione } from '../../enums/pagamenti/pagamenti.enums';
import { TBollettiniPE } from '../../types/bollettini/bollettini.types';
import { PagamentiService } from '../pagamenti/pagamenti.service';
import { FileBollettinoVo } from './../../../../core/commons/vo/file-bollettino-vo';
import { BollettiniConverterService } from './bollettini-converter.service';

@Injectable({
  providedIn: 'root',
})
export class BollettiniService {
  /** Oggetto contenente le costanti per la sezione dell'applicazione. */
  private PF_C = PagamentiFunzionalitaConst;

  /** Costante per il path che esegue il download dei bollettini: /elabora */
  private PATH_DOWNLOAD = '/elabora';

  /** Salvo i tipi elaborazione scaricati per non doverli riscaricare */
  private _teBollettini: TipoElaborazioneVo[] = [];
  /** Salvo gli stati elaborazione scaricati per non doverli riscaricare */
  private _seBollettini: StatoElaborazioneVo[] = [];

  /**
   * Costruttore.
   */
  constructor(
    private _bollettiniConverter: BollettiniConverterService,
    private _pagamenti: PagamentiService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _user: UserService,
    private _config: ConfigService,
    private _http: HttpClient,
    private _httpHelper: HttpHelperService
  ) {}

  /**
   * Funzione che recupera i tipi elaborazione per i bollettini
   * @param idAmbito number con id ambito per la ricerca.
   * @param flgVisibile ServerNumberAsBoolean che definisce la visibilità della bollettazione. Per default è: ServerNumberAsBoolean.true.
   * @returns Observable con la lista di tipi elaborazione
   */
  getTipiBollettazione(
    idAmbito: number,
    flgVisibile = ServerNumberAsBoolean.true
  ): Observable<TipoElaborazioneVo[]> {
    // Se li ho già scaricati, li restituisco
    if (this._teBollettini?.length > 0) {
      // Ritorno il dato presente nel servizio
      return this._riscaUtilities.responseWithDelay(this._teBollettini);
    }

    // Definisco l'idFunzionalita il contesto
    const idFB = this.idFunzionalitaBollettazione;
    // Recupero i dati
    return this._pagamenti
      .getTipiElaborazione(idAmbito, idFB, flgVisibile)
      .pipe(
        tap((tipiElaborazione: TipoElaborazioneVo[]) => {
          // Salvo i tipi elaborazione
          this._teBollettini = tipiElaborazione;
        })
      );
  }

  /**
   * Funzione che recupera gli stati di elaborazione per i bollettini.
   * @param idAmbito number con id ambito per la ricerca.
   * @returns Observable<StatoElaborazioneVo[]> con i dati recuperati dal servizio.
   */
  getStatiBollettazione(idAmbito: number): Observable<StatoElaborazioneVo[]> {
    // Se li ho già scaricati, li restituisco
    if (this._seBollettini?.length > 0) {
      // Ritorno il dato presente nel servizio
      return this._riscaUtilities.responseWithDelay(this._seBollettini);
    }

    // Definisco l'idFunzionalita il contesto
    const idF = this.idFunzionalitaBollettazione;
    // Recupero i dati
    return this._pagamenti.getStatiElaborazione(idAmbito, idF).pipe(
      tap((statiElaborazione: StatoElaborazioneVo[]) => {
        // Salvo i tipi elaborazione
        this._seBollettini = statiElaborazione;
      })
    );
  }

  /**
   * Funzione che permette di recuperare, dalla lista locale di stati elaborazione, uno specifico oggetto.
   * @param code CodiciStatiElaborazione che definisce lo stato elaborazione da recuperare.
   * @returns StatoElaborazioneVo con l'oggetto richiesto. Se non esiste, viene ritornato: undefined.
   */
  statoElaborazioneByCode(code: CodiciStatiElaborazione): StatoElaborazioneVo {
    // Verifico che gli stati siano stati scaricati
    if (!this._seBollettini) {
      // Niente configurazioni
      return undefined;
    }

    // Ricerco all'interno della lista degli stati quello richiesto tramite codice
    const statoElab: StatoElaborazioneVo = this._seBollettini.find(
      (se: StatoElaborazioneVo) => {
        // Effettuo un confronto tra codici
        return se?.cod_stato_elabora === code;
      }
    );

    // Ritorno una copia dell'oggetto
    return cloneDeep(statoElab);
  }

  /**
   * Ottiene la lista delle elaborazioni massive per i bollettini, per popolare la tabella del componente dei bolletini.
   * @param idAmbito number con id ambito per la ricerca.
   * @param filtri IRicercaBollettini con i dati per i filtri di ricerca.
   * @returns Observable<Elaborazione[]> con i dati scaricati.
   */
  getElaborazioniBollettini(
    idAmbito: number,
    filtri: IRicercaBollettini
  ): Observable<ElaborazioneVo[]> {
    // Variabili di comodo
    const idA = idAmbito;
    // Converto l'oggetto per la ricerca delle elaborazioni
    const f = this.convertRBollettiniToRElaborazioni(filtri);

    // Richiamo la funzione per il recupero delle informazioni
    return this._pagamenti.getElaborazioni(idA, f);
  }

  /**
   * Ottiene la lista delle elaborazioni massive per i bollettini, per popolare la tabella del componente dei bolletini.
   * @param idAmbito number con id ambito per la ricerca.
   * @param filtri IRicercaBollettini con i dati per i filtri di ricerca.
   * @param paginazione RiscaTablePagination dati di paginazione della tabella.
   * @returns Observable<RicercaPaginataResponse<Elaborazione[]>> con i dati scaricati.
   */
  getElaborazioniBollettiniPagination(
    idAmbito: number,
    filtri: IRicercaBollettini,
    paginazione?: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<ElaborazioneVo[]>> {
    // Variabili di comodo
    const idA = idAmbito;
    // Converto l'oggetto per la ricerca delle elaborazioni
    let f: IRicercaElaborazioni;
    f = this.convertRBollettiniToRElaborazioni(filtri);
    // Aggiungo per la sezione dei bollettini un query param fisso non presente in maschera
    f.codFunzionalita =
      PagamentiFunzionalitaConst.bollettazione.cod_funzionalita; // => 'BOL' valore specifico per bollettazione

    // Richiamo la funzione per il recupero delle informazioni
    return this._pagamenti.getElaborazioniPagination(idA, f, paginazione);
  }

  /**
   * Funzione di comodo che richiama l'insert del servizio pagamenti per l'aggiunta su DB di una nuova Elaborazione.
   * @param elaborazione Elaborazione da inserire.
   * @returns Observable<Elaborazione> con l'oggetto inserito.
   */
  insertElaborazione(elaborazione: ElaborazioneVo): Observable<ElaborazioneVo> {
    // Richiamo il servizio
    return this._pagamenti.insertElaborazione(elaborazione);
  }

  /**
   * Funzione di PUT per un oggetto Elaborazione.
   * @param elaborazione Elaborazione d'aggiornare come dato.
   * @returns Elaborazione come oggetto aggiornato su DB.
   */
  updateElaborazione(elaborazione: ElaborazioneVo): Observable<ElaborazioneVo> {
    // Richiamo il servizio
    return this._pagamenti.updateElaborazione(elaborazione);
  }

  /**
   * #####################################
   * FUNZIONI DI VERIFICA PER ELABORAZIONE
   * #####################################
   */

  /**
   * Funzione che definisce le logiche di verifica per permettere le azioni all'interno dell'applicazione.
   * Dato un tipo elaborazione e il tipo di verifica, verrà controllato se ci sono delle elaborazioni che già occupano lo slot dell'azione (true) o nessuna elaborazione ha lo stato di verifica (false).
   * @param idAmbito number con id ambito per la verifica.
   * @deprecated tipoElaborazione TipoElaborazione per cui si chiede la verifica per la possibilità d'inserimento di una elaborazione.
   * @param verifica TipiVerificaElabora con la tipologia di verifica da controllare.
   * @returns Observable<boolean> che definisce se ci sono elaborazioni con lo stato richiesto in base alla verifica.
   */
  verifyElaborazione(
    idAmbito: number,
    // tipoElaborazione: TipoElaborazioneVo,
    verifica: TipiVerificaElabora
  ): Observable<boolean> {
    // Recupero l'id del tipo elaborazione
    // const idTipoElabora = tipoElaborazione?.id_tipo_elabora;

    // Richiamo la funzione del servizio dei pagamenti
    return this._pagamenti.verifyElaborazione(
      idAmbito,
      // idTipoElabora,
      verifica
    );
  }

  /**
   * Funzione che definisce le logiche di verifica di presenza di un'elaborazione, dato il tipo elaborazione.
   * Il controllo verrà effettuato per verificare se esistono Elaborazioni con lo stato elaborazione che definiscono la possibilità d'inserimento.
   * @deprecated tipoElaborazione TipoElaborazione per cui si chiede la verifica per la possibilità d'inserimento di una elaborazione.
   * @param idAmbito number con id ambito per la verifica.
   * @returns Observable<boolean> che definisce se l'elaborazione risulta già in uno degli stati d'inserimento.
   */
  verifyInserimentoElaborazione(
    // tipoElaborazione: TipoElaborazioneVo,
    idAmbito?: number
  ): Observable<boolean> {
    // Verifico se è stato definito un idAmbito come input, altrimenti lo recupero dal servizio
    idAmbito = idAmbito != undefined ? idAmbito : this.idAmbito;
    // Definisco il tipo di verifica per l'elaborazione
    const tipoVerifica = TipiVerificaElabora.inserimento;

    // Richiamo la funzione di verify "generica"
    return this.verifyElaborazione(
      idAmbito,
      /* tipoElaborazione, */ tipoVerifica
    );
  }

  /**
   * Funzione che definisce le logiche di verifica di presenza di un'elaborazione, dato il tipo elaborazione.
   * Il controllo verrà effettuato per verificare se esistono Elaborazioni con lo stato elaborazione che definiscono la possibilità di conferma.
   * @deprecated tipoElaborazione TipoElaborazione per cui si chiede la verifica per la possibilità d'inserimento di una elaborazione.
   * @param idAmbito number con id ambito per la verifica.
   * @returns Observable<boolean> che definisce se l'elaborazione risulta già in uno degli stati di conferma.
   */
  verifyConfermaElaborazione(
    // tipoElaborazione: TipoElaborazioneVo,
    idAmbito?: number
  ): Observable<boolean> {
    // Verifico se è stato definito un idAmbito come input, altrimenti lo recupero dal servizio
    idAmbito = idAmbito != undefined ? idAmbito : this.idAmbito;
    // Definisco il tipo di verifica per l'elaborazione
    const tipoVerifica = TipiVerificaElabora.conferma;

    // Richiamo la funzione di verify "generica"
    return this.verifyElaborazione(
      idAmbito,
      /* tipoElaborazione, */ tipoVerifica
    );
  }

  /**
   * Funzione che definisce le logiche di verifica di presenza di un'elaborazione, dato il tipo elaborazione.
   * Il controllo verrà effettuato per verificare se esistono Elaborazioni con lo stato elaborazione che definiscono la possibilità di annullamento.
   * @deprecated tipoElaborazione TipoElaborazione per cui si chiede la verifica per la possibilità di annullamento di una elaborazione.
   * @param idAmbito number con id ambito per la verifica.
   * @returns Observable<boolean> che definisce se l'elaborazione risulta già in uno degli stati di annullamento.
   */
  verifyAnnullaElaborazione(
    // tipoElaborazione: TipoElaborazioneVo,
    idAmbito?: number
  ): Observable<boolean> {
    // Verifico se è stato definito un idAmbito come input, altrimenti lo recupero dal servizio
    idAmbito = idAmbito != undefined ? idAmbito : this.idAmbito;
    // Definisco il tipo di verifica per l'elaborazione
    const tipoVerifica = TipiVerificaElabora.annulla;

    // Richiamo la funzione di verify "generica"
    return this.verifyElaborazione(
      idAmbito,
      /* tipoElaborazione, */ tipoVerifica
    );
  }

  /**
   * ##################################
   * FUNZIONI DI APPOGGIO PER CONVERTER
   * ##################################
   */

  /**
   * Funzione di appoggio per la conversione della configurazione per la ricerca bollettini, verso un oggetto per la ricerca elaborazioni.
   * @param f IRicercaBollettini che definisce le configurazioni per la ricerca bollettini.
   * @returns IRicercaElaborazioni come oggetto per la ricerca elaborazioni.
   */
  private convertRBollettiniToRElaborazioni(
    f: IRicercaBollettini
  ): IRicercaElaborazioni {
    return this._bollettiniConverter.convertIRicercaBollettiniToIRicercaElaborazioni(
      f
    );
  }

  /**
   * ################################
   * FUNZIONI DI SUPPORTO AL SERVIZIO
   * ################################
   */

  /**
   * Funzione che recupera, dalla proprietà "parametri" di un oggetto Elaborazione, un oggetto specifico data una chiave di ricerca.
   * @param proprieta Array di ParametroElaborazioneVo contenente le informazioni dei parametri di un oggetto Elaborazione/ElaborazioneVo.
   * @param chiaveParametro TBollettiniPE che definisce la chiave (che alla fine sarà una stringa) per il recupero dello specifica parametro dall'oggetto elaborazione.
   * @returns ParametroElaborazioneVo con i dati estratti. Altrimenti undefined.
   */
  getParametroElaborazioneBollettini(
    proprieta: ParametroElaborazioneVo[],
    chiaveParametro: TBollettiniPE
  ): ParametroElaborazioneVo {
    // Richiamo la funzione di comodo
    return getParametroElaborazioneBollettini(proprieta, chiaveParametro);
  }

  /**
   * Funzione che recupera, dalla proprietà "parametri" di un oggetto Elaborazione, l'oggetto ParametroElaborazioneVo che identifica la data protocollo della bollettazione.
   * @param proprieta Array di ParametroElaborazioneVo contenente le informazioni dei parametri di un oggetto Elaborazione/ElaborazioneVo.
   * @returns ParametroElaborazioneVo con i dati estratti. Altrimenti undefined.
   */
  getPEBDataProtocollo(
    proprieta: ParametroElaborazioneVo[]
  ): ParametroElaborazioneVo {
    // Recupero la chiave per il parametro
    const chiaveParametro = TipoParametroElaborazione.DT_PROTOCOLLO;
    // Richiamo la funzione di comodo
    return this.getParametroElaborazioneBollettini(proprieta, chiaveParametro);
  }

  /**
   * Funzione che recupera, dalla proprietà "parametri" di un oggetto Elaborazione, l'oggetto ParametroElaborazioneVo che identifica il numero protocollo della bollettazione.
   * @param proprieta Array di ParametroElaborazioneVo contenente le informazioni dei parametri di un oggetto Elaborazione/ElaborazioneVo.
   * @returns ParametroElaborazioneVo con i dati estratti. Altrimenti undefined.
   */
  getPEBNumeroProtocollo(
    proprieta: ParametroElaborazioneVo[]
  ): ParametroElaborazioneVo {
    // Recupero la chiave per il parametro
    const chiaveParametro = TipoParametroElaborazione.NUM_PROTOCOLLO;
    // Richiamo la funzione di comodo
    return this.getParametroElaborazioneBollettini(proprieta, chiaveParametro);
  }

  /**
   * Funzione che recupera, dalla proprietà "parametri" di un oggetto Elaborazione, l'oggetto ParametroElaborazioneVo che identifica la data scadenza pagamento della bollettazione.
   * @param proprieta Array di ParametroElaborazioneVo contenente le informazioni dei parametri di un oggetto Elaborazione/ElaborazioneVo.
   * @returns ParametroElaborazioneVo con i dati estratti. Altrimenti undefined.
   */
  getPEBDataScadenzaPagamento(
    proprieta: ParametroElaborazioneVo[]
  ): ParametroElaborazioneVo {
    // Recupero la chiave per il parametro
    const chiaveParametro = TipoParametroElaborazione.DT_SCAD_PAG;
    // Richiamo la funzione di comodo
    return this.getParametroElaborazioneBollettini(proprieta, chiaveParametro);
  }

  /**
   * Funzione che recupera, dalla proprietà "parametri" di un oggetto Elaborazione, l'oggetto ParametroElaborazioneVo che identifica l'anno per una bollettazione ordinaria.
   * @param proprieta Array di ParametroElaborazioneVo contenente le informazioni dei parametri di un oggetto Elaborazione/ElaborazioneVo.
   * @returns ParametroElaborazioneVo con i dati estratti. Altrimenti undefined.
   */
  getPEBOAnno(proprieta: ParametroElaborazioneVo[]): ParametroElaborazioneVo {
    // Recupero la chiave per il parametro
    const chiaveParametro = TipoParametroElaborazione.ANNO;
    // Richiamo la funzione di comodo
    return this.getParametroElaborazioneBollettini(proprieta, chiaveParametro);
  }

  /**
   * Avvia il download di un file
   * @param idElabora id del file da scaricare
   * @returns Observable<FileBollettinoVo> con l'HttpResponse con dentro il file
   */
  scaricaEApriFile(idElabora: number): Observable<FileBollettinoVo> {
    // Definisco l'url
    const url = this._config.appUrl(this.PATH_DOWNLOAD, idElabora);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({ download: true });
    // Definisco le options
    const options = { params };
    // Effettuo la chiamata
    return this._http.get<FileBollettinoVo>(url, options).pipe(
      tap((fileBollettino: FileBollettinoVo) => {
        // Lancio la funzione di scarico/apertura file
        this.apriFileBollettino(fileBollettino);
      })
    );
  }

  /**
   * Funzione che scarica un file di un bollettino generato dalla chiamata specifica.
   * @param fileBollettino FileBollettinoVo con le informazioni generate a seguito del dowloand del file.
   */
  private apriFileBollettino(fileBollettino: FileBollettinoVo) {
    // Recupero dall'input le informazioni per lo scarico del fil
    const urlFile = fileBollettino?.nome_file_generato;
    // Verifico l'input
    if (!urlFile) {
      // Errore
      throw Error('Link al file non presente. Errore nel servizio');
    }

    // Ottengo il nome del file
    const nomeFile = this._riscaUtilities.convertFilePathToFileName(urlFile);
    // Apro il file
    this._httpHelper.downloadResource(urlFile, nomeFile);
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

  /**
   * Getter di comodo per l'idFunzionalita di questa specifica sezione.
   * @returns number con l'id della funzionalità della sezione specifica.
   */
  get idFunzionalitaBollettazione() {
    // Recupero l'id funzionalità per questa specifica sezione
    return this.PF_C.bollettazione.id_funzionalita;
  }

  /**
   * Getter di comodo per l'idAmbito.
   * @returns number con l'idAmbito.
   */
  get idAmbito() {
    return this._user.idAmbito;
  }

  /**
   * Getter per gli stati bollettazione.
   * @returns Array di StatoElaborazioneVo per la bollettazione.
   */
  get statiBollettazione() {
    // Ritorno la lista di stati elaborazione per la bollettazione
    return this._seBollettini ?? [];
  }
}
