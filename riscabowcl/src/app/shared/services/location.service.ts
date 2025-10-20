import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { ComuneVo, IComuneVo } from '../../core/commons/vo/comune-vo';
import { NazioneVo } from '../../core/commons/vo/nazione-vo';
import { ProvinciaVo } from '../../core/commons/vo/provincia-vo';
import { TipoSedeVo } from '../../core/commons/vo/tipo-sede-vo';
import { ConfigService } from '../../core/services/config.service';
import { TipoSoggettoVo } from '../../features/ambito/models';
import {
  CodiceTipoSede,
  CodiceTipoSoggetto,
} from '../utilities/enums/utilities.enums';
import { HttpUtilitiesService } from './http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from './risca/risca-utilities/risca-utilities.service';
import { clone } from 'lodash';

@Injectable({ providedIn: 'root' })
export class LocationService extends HttpUtilitiesService {
  /** String costante che definisce il path per: /comuni. */
  private PATH_COMUNI = '/comuni';
  /** String costante che definisce il path per: /comuni-qry. */
  private PATH_COMUNI_QRY = '/comuni-qry';
  /** String costante che definisce il path per: /province. */
  private PATH_PROVINCE = '/province';
  /** String costante che definisce il path per: /tipi-sede. */
  private PATH_TIPI_SEDE = '/tipi-sede';
  /** String costante che definisce il path per: /nazioni. */
  private PATH_NAZIONI = '/nazioni';

  /** NazioneVo[] con le informazioni salvate in sessione. */
  private _nazioniAttive: NazioneVo[];
  /** NazioneVo[] con le informazioni salvate in sessione. */
  private _nazioniAll: NazioneVo[];
  /** TipoSedeVo[] con le informazioni salvate in sessione. */
  private _tipiSede: TipoSedeVo[] = [];
  /** ComuneVo[] con le informazioni salvate in sessione. */
  private _comuniAttivi: ComuneVo[] = [];
  /** ComuneVo[] con le informazioni salvate in sessione. */
  private _comuniAll: ComuneVo[] = [];
  /** ProvinciaVo[] con le informazioni salvate in sessione. */
  private _provinceAttive: ProvinciaVo[] = [];
  /** ProvinciaVo[] con le informazioni salvate in sessione. */
  private _provinceAll: ProvinciaVo[] = [];

  /**
   * Costruttore.
   */
  constructor(
    config: ConfigService,
    private _http: HttpClient,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * ############################
   * FUNZIONI PER GESTIONE COMUNI
   * ############################
   */

  /**
   * Getter per i comuni attivi.
   * @returns Observable<ComuneVo[]> con la lista di tutti i comuni definiti come "attivi".
   */
  getComuniAttivi(): Observable<ComuneVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._comuniAttivi?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._comuniAttivi);
    }

    // Richiamo la funzione del servizio
    return this.getComuniV2(true).pipe(
      tap((comuniAttivi: ComuneVo[]) => {
        // Assegno localmente il risultato
        this._comuniAttivi = clone(comuniAttivi);
      })
    );
  }

  /**
   * Getter per i comuni attivi e non più attivi.
   * @returns Observable<ComuneVo[]> con la lista di tutti i comuni, compresi quelli dismessi.
   */
  getComuniAll(): Observable<ComuneVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._comuniAll?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._comuniAll);
    }

    // Richiamo la funzione del servizio
    return this.getComuniV2(false).pipe(
      tap((comuniAll: ComuneVo[]) => {
        // Assegno localmente il risultato
        this._comuniAll = clone(comuniAll);
      })
    );
  }

  /**
   * Getter per i comuni, dato comune e stato.
   * @param attivo boolean che definisce se ricercare solo i comuni attivi (true) o tutti i comuni (false).
   * @returns Observable<ComuneVo[]> con la lista per i comuni secondo il flag "attivo".
   */
  private getComuniV2(attivo: boolean): Observable<ComuneVo[]> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_COMUNI);
    // Compongo i query param
    const params = this.createQueryParams({ attivo });

    return this._http
      .get<IComuneVo[]>(url, { params })
      .pipe(map((v) => this.convertComuni(v)));
  }

  /**
   * Getter per i comuni, dato comune e stato.
   * @param comune string nome del comune da ricercare.
   * @param attivo boolean che definisce se ricercare solo i comuni attivi (true) o tutti i comuni (false).
   * @returns Observable<ComuneVo[]>
   */
  getComuniQry(comune: string, attivo: boolean): Observable<ComuneVo[]> {
    // Creo l'url
    const url = this.appUrl(this.PATH_COMUNI_QRY);
    // Creo i parametri
    const params = this.createQueryParams({
      q: comune,
      attivo,
    });

    return this._http
      .get<IComuneVo[]>(url, { params })
      .pipe(map((ci: IComuneVo[]) => this.convertComuni(ci)));
  }

  /**
   * Getter per un comune, dato l'id del comune e stato.
   * Il comune verrà ricercato tra i comuni attivi.
   * @param codIstatComune string codice istat del comune da ricercare.
   * @returns Observable<Comune> con il comune dato l'id.
   */
  getComuneAttivo(codIstatComune: string): Observable<ComuneVo> {
    // Definisco la variabile contenente l'observable per la gestione della ricerca
    let getComuniAttivi: Observable<ComuneVo[]> = this.getComuniAttivi();

    // Dalla richiesta di lista dei comuni ritorno quello specifico
    return getComuniAttivi.pipe(
      map((ci: ComuneVo[]) => {
        // Cerco nella lista il comune
        return ci.find((c: ComuneVo) => {
          // Verifico per stesso codice istat
          return c.cod_istat_comune === codIstatComune;
        });
      })
    );
  }

  /**
   * Getter per un comune, dato l'id del comune e stato.
   * Il comune verrà ricercato tra i comuni attivi e non attivi.
   * @param codIstatComune string codice istat del comune da ricercare.
   * @returns Observable<Comune> con il comune dato l'id.
   */
  getComuneAll(codIstatComune: string): Observable<ComuneVo> {
    // Definisco la variabile contenente l'observable per la gestione della ricerca
    let getComuniAll: Observable<ComuneVo[]> = this.getComuniAll();

    // Dalla richiesta di lista dei comuni ritorno quello specifico
    return getComuniAll.pipe(
      map((ci: ComuneVo[]) => {
        // Cerco nella lista il comune
        return ci.find((c: ComuneVo) => {
          // Verifico per stesso codice istat
          return c.cod_istat_comune === codIstatComune;
        });
      })
    );
  }

  /**
   * Getter per un comune, dato l'id del comune e stato.
   * @param codIstatComune string codice istat del comune da ricercare.
   * @param attivo boolean che definisce se ricercare solo i comuni attivi (true) o tutti i comuni (false).
   * @returns Observable<Comune> con il comune dato l'id.
   */
  getComuneV2(codIstatComune: string, attivo: boolean): Observable<ComuneVo> {
    // Richiamo la get comuni e cerco il comune tramite idComune
    return this.getComuniV2(attivo).pipe(
      map((ci: ComuneVo[]) => {
        // Cerco nella lista il comune
        return ci.find((c: ComuneVo) => {
          // Verifico per stesso codice istat
          return c.cod_istat_comune === codIstatComune;
        });
      })
    );
  }

  /**
   * ##############################
   * FUNZIONI PER GESTIONE PROVINCE
   * ##############################
   */

  /**
   * Getter per le province attive.
   * @returns Observable<ProvinciaVo[]> con la lista di tutte le province definite come "attive".
   */
  getProvinceAttive(): Observable<ProvinciaVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._provinceAttive?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._provinceAttive);
    }

    // Richiamo la funzione del servizio
    return this.getProvinceV2(true).pipe(
      tap((provinceAttive: ProvinciaVo[]) => {
        // Assegno localmente il risultato
        this._provinceAttive = clone(provinceAttive);
      })
    );
  }

  /**
   * Getter per le province attive e non più attive.
   * @returns Observable<ProvinciaVo[]> con la lista di tutte le province, comprese quelle dismesse.
   */
  getProvinceAll(): Observable<ProvinciaVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._provinceAll?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._provinceAll);
    }

    // Richiamo la funzione del servizio
    return this.getProvinceV2(false).pipe(
      tap((provinceAll: ProvinciaVo[]) => {
        // Assegno localmente il risultato
        this._provinceAll = clone(provinceAll);
      })
    );
  }

  /**
   * Getter per le province, dato lo stato.
   * @param attivo boolean che definisce se ricercare solo le province attive (true) o tutte (false).
   * @returns Observable<ProvinciaVo[]> con la lista di province per il flag "attivo".
   */
  getProvinceV2(attivo: boolean): Observable<ProvinciaVo[]> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_PROVINCE);
    // Compongo i query param
    const params = this.createQueryParams({ attivo });

    return this._http
      .get<ProvinciaVo[]>(url, { params })
      .pipe(map((v) => this.convertProvince(v)));
  }

  /**
   * #############################
   * FUNZIONI PER GESTIONE NAZIONI
   * #############################
   */

  /**
   * Getter per le nazioni attive.
   * @returns Observable<NazioneVo[]> con la lista di nazioni per il flag "attivo".
   */
  getNazioniAttive(): Observable<NazioneVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._nazioniAttive?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._nazioniAttive);
    }

    // Richiamo la funzione del servizio
    return this.getNazioniV2(true).pipe(
      tap((nazioniAttive: NazioneVo[]) => {
        // Assegno localmente il risultato
        this._nazioniAttive = clone(nazioniAttive);
      })
    );
  }

  /**
   * Getter per le nazioni attive e non più attive.
   * @returns Observable<NazioneVo[]> con la lista di tutte la nazioni, anche quelle dismesse.
   */
  getNazioniAll(): Observable<NazioneVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._nazioniAll?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._nazioniAll);
    }

    // Richiamo la funzione del servizio
    return this.getNazioniV2(false).pipe(
      tap((nazioniInattive: NazioneVo[]) => {
        // Assegno localmente il risultato
        this._nazioniAll = clone(nazioniInattive);
      })
    );
  }

  /**
   * Getter per le nazioni.
   * @param attivo boolean che definisce se ricercare solo gli stati attivi (true) o tutti gli stati (false).
   * @returns Observable<NazioneVo[]> con la lista di nazioni per il flag "attivo".
   */
  getNazioniV2(attivo: boolean): Observable<NazioneVo[]> {
    // Definisco l'url
    const url = this.appUrl(this.PATH_NAZIONI);
    // Definisco i parametri
    const params = this.createQueryParams({ attivo });
    // Chiamo il servizio
    return this._http
      .get<NazioneVo[]>(url, { params })
      .pipe(map((v) => this.convertNazioni(v)));
  }

  /**
   * ######################
   * FUNZIONI PER TIPI SEDE
   * ######################
   */

  /**
   * Funzione che recupera i tipi sede tramite servizio.
   * @returns Observable<TipoSedeVo[]> con le informazioni scaricate.
   */
  getTipiSede(): Observable<TipoSedeVo[]> {
    // Verifico se esistono dati già scaricati
    if (this._tipiSede?.length > 0) {
      // Ritorno il dato scaricato
      return this._riscaUtilities.responseWithDelay(this._tipiSede);
    }

    // Definisco l'url
    const url = this.appUrl(this.PATH_TIPI_SEDE);
    // Richiamo il servizio dati
    return this._http.get<TipoSedeVo[]>(url).pipe(
      tap((res: TipoSedeVo[]) => {
        // Assegno localmente il risultato
        this._tipiSede = clone(res);
      })
    );
  }

  /**
   * ##################
   * FUNZIONI DI COMODO
   * ##################
   */

  /**
   * Funzione che estrae e imposta come valore di default uno stato all'interno di un form.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param listaNazioni Array di NazioneVo dalla quale cercare il default.
   * @param codNazione number che definisce l'id della nazione da ricercare.
   */
  setNazioneDefault(
    f: FormGroup,
    fcn: string,
    listaNazioni: NazioneVo[],
    codNazione: string
  ) {
    // Verifico che esista la lista nazioni
    if (!f || !fcn || !listaNazioni || !codNazione) {
      return;
    }

    // Lancio una sanitizzazione degli oggetti qualora ci siano già dei dafeult
    this._riscaUtilities.sanitizeRiscaSelectedFlag(listaNazioni);

    // Vado a recuperare l'indice dell'oggetto
    const i = listaNazioni.findIndex(
      (s: NazioneVo) => s.cod_istat_nazione === codNazione
    );
    // Verifico sia stato trovato l'oggetto
    if (i !== -1) {
      // Recupero l'oggetto
      const nazione = listaNazioni[i] as any;
      // Imposto il flag di select
      nazione.__selected = true;
      // Imposto l'oggetto come valore
      this._riscaUtilities.setFormValue(f, fcn, nazione);
    }
  }

  /**
   * Funzione che verifica se una nazione, data il codIstatNazione, è definito nel form.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param codNazione number che definisce l'id nazione da verificare.
   * @returns boolean che identifica se la nazione non esiste, o è quella definita da codIstatNazione.
   */
  isNazioneInForm(f: FormGroup, fcn: string, codNazione: string): boolean {
    // Recupero la nazione
    const nazione: NazioneVo = this._riscaUtilities.getFormValue(f, fcn);
    // Ritorno true se non è selezionata la nazione o la nazione è italia
    return !nazione || nazione.cod_istat_nazione === codNazione;
  }

  /**
   * Funzione che estrae e imposta come valore di default uno stato all'interno di un form.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param listaProvince Array di ProvinciaVo dalla quale cercare il default.
   * @param idProvincia number che definisce l'id della provincia da ricercare.
   */
  setProvinciaDefault(
    f: FormGroup,
    fcn: string,
    listaProvince: ProvinciaVo[],
    idProvincia: number
  ) {
    // Verifico che esista la lista nazioni
    if (!f || !fcn || !listaProvince || !idProvincia) {
      return;
    }

    // Lancio una sanitizzazione degli oggetti qualora ci siano già dei dafeult
    this._riscaUtilities.sanitizeRiscaSelectedFlag(listaProvince);

    // Vado a recuperare l'oggetto
    const i = listaProvince.findIndex((p) => p.id_provincia === idProvincia);

    // Verifico sia stato trovato l'oggetto
    if (i !== -1) {
      // Recupero l'oggetto alla posizione i
      const provincia: any = listaProvince[i];
      // Aggiorno il flag selected
      provincia.__selected = true;
      // Riassegno la lista per attivare il change del componente
      listaProvince = clone(listaProvince);
    }
  }

  /**
   * Funzione che estrae e imposta come valore di default uno stato all'interno di un form.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param listaTipiSede Array di TipoSedeVo dalla quale cercare il default.
   * @param tipoSoggetto TipoSoggetto che definisce il valore da selezionare di default, se il parametro [tipoSede] non è valorizzato.
   * @param tipoSede TipoSedeVo che definisce il tipo sede da valorizzare per default.
   */
  setTipoSedeDefaultByTipoSoggetto(
    f: FormGroup,
    fcn: string,
    listaTipiSede: TipoSedeVo[],
    tipoSoggetto: TipoSoggettoVo,
    tipoSede?: TipoSedeVo
  ) {
    // Verifico che esista la lista nazioni
    if (!f || !fcn || !listaTipiSede || !tipoSoggetto) {
      return;
    }

    // Lancio una sanitizzazione degli oggetti qualora ci siano già dei dafeult
    this._riscaUtilities.sanitizeRiscaSelectedFlag(listaTipiSede);

    // Vado a recuperare l'oggetto
    const i = listaTipiSede.findIndex((ts: TipoSedeVo) => {
      // Verfico se esiste un tipo sede di default da valorizzare
      if (tipoSede?.cod_tipo_sede != undefined) {
        // Definisco le condizioni di ricerca tramite il tipo sede desiderato
        const codTS = tipoSede.cod_tipo_sede;
        // Ritorno il match tra il tipo sede richiesto e l'elemento della lista
        return codTS === ts.cod_tipo_sede;
        // #
      } else {
        // Definisco le condizioni di ricerca tramite il tipo soggetto
        const isPF = tipoSoggetto.cod_tipo_soggetto === CodiceTipoSoggetto.PF;
        const isPG = tipoSoggetto.cod_tipo_soggetto === CodiceTipoSoggetto.PG;
        const isPB = tipoSoggetto.cod_tipo_soggetto === CodiceTipoSoggetto.PB;
        // Definisco le condizioni di ricerca per il tipo sede
        const isResidenza = ts.cod_tipo_sede === CodiceTipoSede.residenza;
        const isSedeLegale = ts.cod_tipo_sede === CodiceTipoSede.sedeLegale;
        const isSedeIstituzionale =
          ts.cod_tipo_sede === CodiceTipoSede.sedeIstituzionale;
        // Ritorno la combo di validazione
        return (
          (isPF && isResidenza) ||
          (isPG && isSedeLegale) ||
          (isPB && isSedeIstituzionale)
        );
      }
    });

    // Verifico sia stato trovato l'oggetto
    if (i !== -1) {
      // Vado a recuperare l'oggetto
      const tipoSede = listaTipiSede[i] as any;
      // Aggiorno l'oggetto referenziato
      tipoSede.__selected = true;
      // Imposto l'oggetto come valore
      this._riscaUtilities.setFormValue(f, fcn, tipoSede);
    }
  }

  /**
   * Funzione che estrae e imposta come valore una provincia all'interno di un form, dato un comune.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param listaProvince Array di ProvinciaVo dalla quale cercare il valore.
   * @param comune Comune che definisce il valore da selezionare.
   */
  setProvinciaByComune(
    f: FormGroup,
    fcn: string,
    listaProvince: ProvinciaVo[],
    comune: ComuneVo
  ) {
    // Verifico che esista la lista nazioni
    if (!f || !fcn || !listaProvince || !comune) {
      return;
    }

    // Vado a recuperare l'oggetto
    const i = listaProvince.findIndex(
      (p) => p.id_provincia === comune.id_provincia
    );

    // Verifico sia stato trovato l'oggetto
    if (i !== -1) {
      // Vado a recuperare l'oggetto
      const provincia = listaProvince[i];
      // Imposto l'oggetto come valore
      this._riscaUtilities.setFormValue(f, fcn, provincia);
    }
  }

  /**
   * Funzione che estrae e imposta come valore un cap all'interno di un form, dato un comune.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param comune Comune che definisce il valore da selezionare.
   */
  setCAPByComune(f: FormGroup, fcn: string, comune: ComuneVo) {
    // Verifico che esista la lista nazioni
    if (!f || !fcn || !comune) {
      return;
    }

    // Recupero il cap dal comune
    const CAP = comune.cap_comune;
    // Imposto l'oggetto come valore
    this._riscaUtilities.setFormValue(f, fcn, CAP);
  }

  /**
   * ###############
   * PARSING OGGETTI
   * ###############
   */

  /**
   * Funzione di convert dell'array di oggetti Comune ricevuto da server.
   * @param ci Array di IComuneVo da convertire.
   * @returns Array di ComuneVo da parsati.
   */
  private convertComuni(ci: IComuneVo[]): ComuneVo[] {
    // Verifico l'input
    if (!ci) {
      return undefined;
    }
    // Vado a fare il convert degli oggetti
    return ci.map((c) => this.convertComune(c));
  }

  /**
   * Funzione di convert dell'oggetto Comune ricevuto da server.
   * @param c Comune da convertire.
   * @returns Comune convertito.
   */
  private convertComune(c: IComuneVo): ComuneVo {
    // Verifico l'input
    if (!c) {
      return undefined;
    }
    // Creo un oggetto d'appoggio per la conversione
    const objC: any = { ...c };

    // Vado a fare il convert della data inizio validità
    objC.data_inizio_validita = new Date(objC.data_inizio_validita);
    // Verifico se esiste la data fine validità
    if (objC.data_fine_validita) {
      // Converto anche la data fine validità
      objC.data_fine_validita = new Date(objC.data_fine_validita);
      // #
    } else {
      // Non esiste, forzo il valore ad undefined
      objC.data_fine_validita = undefined;
    }

    // Dall'oggetto generato con le stesse proprietà, creo l'oggetto comune vero e proprio
    const comune: ComuneVo = objC;
    // Ritorno il convert
    return comune;
  }

  /**
   * Funzione di convert dell'array di oggetti ProvinciaVo ricevuto da server.
   * @param pe Array di ProvinciaVo da convertire.
   * @returns Array di ProvinciaVo da parsati.
   */
  private convertProvince(pe: ProvinciaVo[]): ProvinciaVo[] {
    // Verifico l'input
    if (!pe) {
      return pe;
    }
    // Vado a fare il convert degli oggetti
    return pe.map((p) => this.convertProvincia(p));
  }

  /**
   * Funzione di convert dell'oggetto ProvinciaVo ricevuto da server.
   * @param p ProvinciaVo da convertire.
   * @returns ProvinciaVo convertito.
   */
  private convertProvincia(p: ProvinciaVo): ProvinciaVo {
    // Verifico l'input
    if (!p) {
      return p;
    }
    // Verifico esista le date per la conversione
    if (p.data_inizio_validita) {
      p.data_inizio_validita = new Date(p.data_inizio_validita);
    }
    if (p.regione?.data_inizio_validita) {
      p.regione.data_inizio_validita = new Date(p.regione.data_inizio_validita);
    }
    if (p.regione?.nazione?.data_inizio_validita) {
      p.regione.nazione.data_inizio_validita = new Date(
        p.regione.nazione.data_inizio_validita
      );
    }

    // Ritorno il convert
    return p;
  }

  /**
   * Funzione di convert dell'array di oggetti NazioneVo ricevuto da server.
   * @param na Array di NazioneVo da convertire.
   * @returns Array di NazioneVo da parsati.
   */
  private convertNazioni(na: NazioneVo[]): NazioneVo[] {
    // Verifico l'input
    if (!na) {
      return na;
    }
    // Vado a fare il convert degli oggetti
    return na.map((n) => this.convertNazione(n));
  }

  /**
   * Funzione di convert dell'oggetto NazioneVo ricevuto da server.
   * @param n NazioneVo da convertire.
   * @returns NazioneVo convertito.
   */
  private convertNazione(n: NazioneVo): NazioneVo {
    // Verifico l'input
    if (!n) {
      return n;
    }
    // Vado a fare il convert della data
    n.data_inizio_validita = new Date(n.data_inizio_validita);
    // Ritorno il convert
    return n;
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per _nazioniAttive.
   * @returns Array di NazioneVo.
   */
  get nazioniAttive(): NazioneVo[] {
    // Recupero e creo una copia del dato
    return this._nazioniAttive;
  }

  /**
   * Getter per _nazioniInattive.
   * @returns Array di NazioneVo.
   */
  get nazioniInattive(): NazioneVo[] {
    // Recupero e creo una copia del dato
    return this._nazioniAll;
  }

  /**
   * Getter per _tipiSede.
   * @returns Array di TipoSedeVo.
   */
  get tipiSede(): TipoSedeVo[] {
    // Recupero e creo una copia del dato
    return this._tipiSede;
  }
}
