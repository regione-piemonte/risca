import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Moment } from 'moment';
import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {
  CalcoloCanoneV2Vo,
  CalcoloCanoneVo,
} from '../../../../../../core/commons/vo/calcolo-canone-vo';
import {
  ComponenteDt,
  ComponenteDtVo,
} from '../../../../../../core/commons/vo/componente-dt-vo';
import { PraticaDTVo } from '../../../../../../core/commons/vo/pratica-vo';
import { RiduzioneAumentoVo } from '../../../../../../core/commons/vo/riduzione-aumento-vo';
import { IUnitaMisuraVo } from '../../../../../../core/commons/vo/unita-misura-vo';
import { IUsoLeggeVo } from '../../../../../../core/commons/vo/uso-legge-vo';
import { ConfigService } from '../../../../../../core/services/config.service';
import { RiscaUtilitiesService } from '../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  CodiciComponentiDt,
  DTAmbienteFlagManuale,
  TipiFrazionamentoCanone,
} from '../../../../../../shared/utilities';
import {
  ComponentiDtConfigsReq,
  ComponentiDtConfigsRes,
} from '../../../../class/quadri-tecnici/dati-tecnici.classes';
import { DatiTecniciConverterService } from './dati-tecnici-converter.service';

@Injectable({
  providedIn: 'root',
})
export class DatiTecniciService {
  /** Costante per il separatore dei path */
  private PATH_SEPARATORE = '/';
  /** Costante per il path: /unita-misura */
  private PATH_UNITA_MISURA = '/unita-misura';
  /** Costante per il path: /calcolo-canone */
  private PATH_CALCOLO_CANONE = '/calcolo-canone';
  /** Costante per il path: /calcolo-canone */
  private PATH_CALCOLO_CANONE_V2 = '/_calcolo-canone/dataRif';
  /** Costante per il path: /idRiscossione */
  private PATH_ID_RISCOSSIONE = '/idRiscossione';
  /** Costante per il path: /dataRif */
  private PATH_DATA_RIF = '/dataRif';
  /** Costante per il path: /tipi-uso */
  private PATH_TIPI_USO = '/tipi-uso';
  /** Costante per il path: /riduzioni */
  private PATH_RIDUZIONI = '/riduzioni';
  /** Costante per il path: /aumenti */
  private PATH_AUMENTI = '/aumenti';
  /** Costante per il path: /ambiti */
  private PATH_AMBITI = '/ambiti';
  /** Costante per il path: /componenti-dt */
  private PATH_COMPONENTI_DT = '/componenti-dt';

  /** String che definisce il path iniziale al backend. */
  private beUrl: string;

  /** ComponenteDt che definisce la configurazione per il componente dei dati tecnici per: ricerca. */
  private _ricercaDt: ComponenteDt;
  /** ComponenteDt che definisce la configurazione per il componente dei dati tecnici per: inserisci pratica. */
  private _praticaInsDt: ComponenteDt;
  /** Array di ComponenteDt che definisce le configurazioni per i componenti dei dati tecnici per: dettaglio/modifica pratica. */
  private _praticaModDts: ComponenteDt[];
  /** ComponenteDt che definisce la configurazione per il componente dei dati tecnici per: inserisci stati debitori. */
  private _statiDebitoriInsDt: ComponenteDt;
  /** Array di ComponenteDt che definisce le configurazioni per i componenti dei dati tecnici per: dettaglio/modifica stati debitori. */
  private _statiDebitoriModDts: ComponenteDt[];

  /**
   * Costruttore
   */
  constructor(
    private _config: ConfigService,
    private _dtConverter: DatiTecniciConverterService,
    private _http: HttpClient,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Recupero il path per il backend
    this.beUrl = this._config.getBEUrl();
  }

  /**
   * ##########################################################################
   * FUNZIONI PER LO SCARICO DELLE CONFIGURAZIONI PER I COMPONENTI DATI TECNICI
   * ##########################################################################
   */

  /**
   * Funzione che scarica una lista di componenti dt, dato il codice per il tipo di componente.
   * @param idAmbito number con l'id ambito per lo scarico dei dati.
   * @param codTipoComponente string con il codice componente da scaricare.
   * @param attivo boolean che definisce se attivare il filtro per ottenere solo oggetti attualmente attivi.
   * @returns Observable<ComponenteDtVo[]> con i dati scaricati.
   */
  getComponentiDtVo(
    idAmbito: number,
    codTipoComponente?: string,
    attivo = false
  ): Observable<ComponenteDtVo[]> {
    // Definisco l'url
    const url = `${this.beUrl}${this.PATH_AMBITI}/${idAmbito}${this.PATH_COMPONENTI_DT}`;
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      codTipoComponente,
      attivo,
    });

    // Lancio la chiamata per il recupero dati
    return this._http.get<ComponenteDtVo[]>(url, { params });
  }

  /**
   * Funzione che scarica una lista di componenti dt, dato il codice per il tipo di componente.
   * @param idAmbito number con l'id ambito per lo scarico dei dati.
   * @param codTipoComponente string con il codice componente da scaricare.
   * @param attivo boolean che definisce se attivare il filtro per ottenere solo oggetti attualmente attivi.
   * @returns Observable<ComponenteDt[]> con i dati scaricati.
   */
  getComponentiDt(
    idAmbito: number,
    codTipoComponente?: string,
    attivo = false
  ): Observable<ComponenteDt[]> {
    // Richiamo la funzione per lo scarico dei componenti dt vo e converto gli oggetti
    return this.getComponentiDtVo(idAmbito, codTipoComponente, attivo).pipe(
      map((componentiDtVo: ComponenteDtVo[]) => {
        // Converto l'array
        return this._dtConverter.convertComponentiDtVoToComponentiDt(
          componentiDtVo
        );
      })
    );
  }

  /**
   * Funzione che scarica il componente dt per: ricerca.
   * @param idAmbito number con l'id ambito per lo scarico dei dati.
   * @returns Observable<ComponenteDtVo> con i dati scaricati.
   */
  getRicercaDt(idAmbito: number): Observable<ComponenteDt> {
    // Definisco il codice tipo componente
    const ricerca = CodiciComponentiDt.ricerca;
    // Lancio la chiamata per il recupero dati
    return this.getComponentiDt(idAmbito, ricerca, true).pipe(
      map((componentiDt: ComponenteDt[]) => {
        // Recupero e salvo localmente il componente per la ricerca
        this._ricercaDt = componentiDt[0];
        // Ritorno il singolo oggett
        return this._ricercaDt;
      })
    );
  }

  /**
   * Funzione che scarica i componenti dt per: inserisci pratica.
   * @param idAmbito number con l'id ambito per lo scarico dei dati.
   * @returns Observable<ComponenteDt> con i dati scaricati.
   */
  getPraticaInsDt(idAmbito: number): Observable<ComponenteDt> {
    // Definisco il codice tipo componente
    const pratica = CodiciComponentiDt.pratica;
    // Lancio la chiamata per il recupero dati
    return this.getComponentiDt(idAmbito, pratica, true).pipe(
      map((componentiDt: ComponenteDt[]) => {
        // Recupero e salvo localmente il componente per la pratica
        this._praticaInsDt = componentiDt[0];
        // Ritorno il singolo oggett
        return this._praticaInsDt;
      })
    );
  }

  /**
   * Funzione che scarica i componenti dt per: modifica pratica.
   * @param idAmbito number con l'id ambito per lo scarico dei dati.
   * @returns Observable<ComponenteDt[]> con i dati scaricati.
   */
  getPraticaModDts(idAmbito: number): Observable<ComponenteDt[]> {
    // Definisco il codice tipo componente
    const pratica = CodiciComponentiDt.pratica;
    // Lancio la chiamata per il recupero dati
    return this.getComponentiDt(idAmbito, pratica).pipe(
      map((componentiDt: ComponenteDt[]) => {
        // Recupero e salvo localmente il componente per la pratica
        this._praticaModDts = componentiDt;
        // Ritorno il singolo oggett
        return this._praticaModDts;
      })
    );
  }

  /**
   * Funzione che scarica i componenti dt per: inserisci stati debitori.
   * @param idAmbito number con l'id ambito per lo scarico dei dati.
   * @returns Observable<ComponenteDt> con i dati scaricati.
   */
  getStatiDebitoriInsDt(idAmbito: number): Observable<ComponenteDt> {
    // Definisco il codice tipo componente
    const statiDebitori = CodiciComponentiDt.sdGeneraliTecnici;
    // Lancio la chiamata per il recupero dati
    return this.getComponentiDt(idAmbito, statiDebitori, true).pipe(
      map((componentiDt: ComponenteDt[]) => {
        // Recupero e salvo localmente il componente per stati debitori
        this._statiDebitoriInsDt = componentiDt[0];
        // Ritorno il singolo oggett
        return this._statiDebitoriInsDt;
      })
    );
  }

  /**
   * Funzione che scarica i componenti dt per: modifica stati debitori.
   * @param idAmbito number con l'id ambito per lo scarico dei dati.
   * @returns Observable<ComponenteDt[]> con i dati scaricati.
   */
  getStatiDebitoriModDts(idAmbito: number): Observable<ComponenteDt[]> {
    // Definisco il codice tipo componente
    const statiDebitori = CodiciComponentiDt.sdGeneraliTecnici;
    // Lancio la chiamata per il recupero dati
    return this.getComponentiDt(idAmbito, statiDebitori).pipe(
      map((componentiDt: ComponenteDt[]) => {
        // Recupero e salvo localmente il componente per stati debitori
        this._statiDebitoriModDts = componentiDt;
        // Ritorno il singolo oggett
        return this._statiDebitoriModDts;
      })
    );
  }

  /**
   * Funzione di supporto che scarica le configurazioni per i dati tecnici dell'applicazione.
   * @param idAmbito number con l'id ambito per lo scarico dei dati.
   * @returns Observable<ComponentiDtConfigsRes> con i dati scaricati.
   */
  getComponentiDtConfigs(idAmbito: number): Observable<ComponentiDtConfigsRes> {
    // Creo l'oggetto per lo scarico dati
    const req: ComponentiDtConfigsReq = {
      ricercaDt: this.getRicercaDt(idAmbito),
      praticaInsDt: this.getPraticaInsDt(idAmbito),
      praticaModDts: this.getPraticaModDts(idAmbito),
      // statiDebitoriInsDt: this.getStatiDebitoriInsDt(idAmbito),
      // statiDebitoriModDts: this.getStatiDebitoriModDts(idAmbito),
    };
    // Lancio la forkJoin delle chiamate
    return forkJoin(req);
  }

  /**
   * #######################################
   * FUNZIONI DI GESTIONE PER I DATI TECNICI
   * #######################################
   */

  /**
   * Funzione che recupera dal server le informazioni per gli "usi di legge", dato un "tipo uso padre" di default e un "ambito" parametrizzato.
   * @param idAmbito string | number che identifica un "ambito".
   * @param idTipoUsoPadre string o number che identifica un "tipo uso padre".
   * @param dataInizioVal string che definisce la data inizio validatà. Se non definita, verranno presi tutti gli elementi esistenti.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<any> contenente i dati per gli "usi di legge".
   */
  getUsiDiLegge(
    idAmbito: string | number,
    idTipoUsoPadre?: number,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<IUsoLeggeVo[]> {
    // Verifico e converto i parametri in string
    idAmbito = this._riscaUtilities.convertParamToString(idAmbito);

    // Definisco l'url per il collegamento al servizio del server
    const url: string = this._riscaUtilities.createUrl([
      this.beUrl,
      this.PATH_AMBITI,
      this._riscaUtilities.creaUrlPath(idAmbito, this.PATH_SEPARATORE),
      this.PATH_TIPI_USO,
    ]);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      idTipoUsoPadre: idTipoUsoPadre,
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Lancio la chiamata per il recupero dati
    return this._http.get<IUsoLeggeVo[]>(url, { params });
  }

  /**
   * Funzione che recupera dal server le informazioni per gli "usi di legge", dato un "tipo uso padre" di default e un "ambito" parametrizzato.
   * @param idUsoDiLegge string | number che identifica un "ambito".
   * @returns Observable<any> contenente i dati per gli "usi di legge".
   */
  getUsoDiLegge(idUsoDiLegge: string | number): Observable<IUsoLeggeVo> {
    // Definisco l'url per il collegamento al servizio del server
    const PATH_TIPO_USO_ID = `/${idUsoDiLegge}`;
    const url = `${this.beUrl}${this.PATH_TIPI_USO}${PATH_TIPO_USO_ID}`;

    // Lancio la chiamata per il recupero dati
    return this._http.get<IUsoLeggeVo>(url);
  }

  /**
   * Funzione che recupera dal server l'informazione per l'unità di misura dato un idTipoUso.
   * @param idUnitaMisura number che definisce l'id dell'unità di misura da scaricare.
   * @returns Observable<UnitaMisuraVo> contenente i dati per l'unità di misura.
   */
  getUnitaDiMisura(idUnitaMisura: string | number): Observable<IUnitaMisuraVo> {
    // Definisco l'url per il collegamento al servizio del server
    const PATH_UNITA_ID = `/${idUnitaMisura}`;
    const url = `${this.beUrl}${this.PATH_UNITA_MISURA}${PATH_UNITA_ID}`;

    // Lancio la chiamata per il recupero dati
    return this._http.get<IUnitaMisuraVo>(url);
  }

  /**
   * Funzione che recupera dal server le informazioni per % di riduzione motivazione.
   * @param idTipoUso string o number che identifica un idTipoUso.
   * @param flagManuale DTAmbienteFlagManuale che definisce il filtro per lo scarico dati. Il flag pilota le informazioni automatiche (0), o manuali (1). Non passando nulla vengono scaricate entrambi i tipi di dato.
   * @param dataInizioVal string che definisce la data inizio validatà. Se non definita, verranno presi tutti gli elementi esistenti.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<RiduzioneAumentoVo> contenente i dati per per % di riduzione motivazione.
   */
  getRiduzioniFlagManuale(
    idTipoUso: string | number,
    flagManuale?: DTAmbienteFlagManuale,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<RiduzioneAumentoVo[]> {
    // Verifico e converto i parametri in string
    idTipoUso = this._riscaUtilities.convertParamToString(idTipoUso);

    // Definisco l'url per il collegamento al servizio del server
    const url: string = this._riscaUtilities.createUrl([
      this.beUrl,
      this.PATH_TIPI_USO,
      this._riscaUtilities.creaUrlPath(idTipoUso, this.PATH_SEPARATORE),
      this.PATH_RIDUZIONI,
    ]);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      flgManuale: flagManuale,
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Lancio la chiamata per il recupero dati
    return this._http.get<RiduzioneAumentoVo[]>(url, { params });
  }

  /**
   * Funzione che recupera dal server le informazioni per % di aumento motivazione.
   * @param idTipoUso string o number che identifica un idTipoUso.
   * @param flagManuale DTAmbienteFlagManuale che definisce il filtro per lo scarico dati. Il flag pilota le informazioni automatiche (0), o manuali (1). Non passando nulla vengono scaricate entrambi i tipi di dato.
   * @param dataInizioVal string che definisce la data inizio validatà. Se non definita, verranno presi tutti gli elementi esistenti.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<RiduzioneAumentoVo> contenente i dati per per % di aumento motivazione.
   */
  getAumentiFlagManuale(
    idTipoUso: string | number,
    flagManuale?: DTAmbienteFlagManuale,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<RiduzioneAumentoVo[]> {
    // Verifico e converto i parametri in string
    idTipoUso = this._riscaUtilities.convertParamToString(idTipoUso);

    // Definisco l'url per il collegamento al servizio del server
    const url: string = this._riscaUtilities.createUrl([
      this.beUrl,
      this.PATH_TIPI_USO,
      this._riscaUtilities.creaUrlPath(idTipoUso, this.PATH_SEPARATORE),
      this.PATH_AUMENTI,
    ]);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      flgManuale: flagManuale,
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Lancio la chiamata per il recupero dati
    return this._http.get<RiduzioneAumentoVo[]>(url, { params });
  }

  /**
   * Funzione che recupera dal server le informazioni per % di riduzione motivazione.
   * @param flagManuale DTAmbienteFlagManuale che definisce il filtro per lo scarico dati. Il flag pilota le informazioni automatiche (0), o manuali (1). Non passando nulla vengono scaricate entrambi i tipi di dato.
   * @param dataInizioVal string che definisce la data inizio validatà.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<RiduzioneAumentoVo[]> contenente i dati per per % di riduzione motivazione.
   */
  getPercentualiRiduzioni(
    flagManuale?: DTAmbienteFlagManuale,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<RiduzioneAumentoVo[]> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this._riscaUtilities.createUrl([
      this.beUrl,
      this.PATH_RIDUZIONI,
    ]);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      flgManuale: flagManuale,
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Lancio la chiamata per il recupero dati
    return this._http.get<RiduzioneAumentoVo[]>(url, { params });
  }

  /**
   * Funzione che recupera dal server le informazioni per % di aumento motivazione.
   * @param flagManuale DTAmbienteFlagManuale che definisce il filtro per lo scarico dati. Il flag pilota le informazioni automatiche (0), o manuali (1). Non passando nulla vengono scaricate entrambi i tipi di dato.
   * @param dataInizioVal string che definisce la data inizio validatà.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<RiduzioneAumentoVo[]> contenente i dati per per % di aumento motivazione.
   */
  getPercentualiAumenti(
    flagManuale?: DTAmbienteFlagManuale,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<RiduzioneAumentoVo[]> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this._riscaUtilities.createUrl([
      this.beUrl,
      this.PATH_AUMENTI,
    ]);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      flgManuale: flagManuale,
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Lancio la chiamata per il recupero dati
    return this._http.get<RiduzioneAumentoVo[]>(url, { params });
  }

  /**
   * Funzione che recupera dal server le informazioni per % di riduzione motivazione.
   * @param idRiduzione string o number che identifica un id o codice per la riduzione.
   * @param dataInizioVal string che definisce la data inizio validatà.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<RiduzioneAumentoVo> contenente i dati per per % di riduzione motivazione.
   */
  getPercentualeRiduzione(
    idRiduzione: string | number,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<RiduzioneAumentoVo> {
    // Verifico e converto i parametri in string
    idRiduzione = this._riscaUtilities.convertParamToString(idRiduzione);

    // Definisco l'url per il collegamento al servizio del server
    const url: string = this._riscaUtilities.createUrl([
      this.beUrl,
      this.PATH_RIDUZIONI,
      this._riscaUtilities.creaUrlPath(idRiduzione, this.PATH_SEPARATORE),
    ]);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Lancio la chiamata per il recupero dati
    return this._http.get<RiduzioneAumentoVo>(url, { params });
  }

  /**
   * Funzione che recupera dal server le informazioni per % di aumento motivazione.
   * @param idAumento string o number che identifica un id o codice per l'aumento.
   * @param dataInizioVal string che definisce la data inizio validatà.
   * @param dataFineVal string che definisce la data fine validatà.
   * @returns Observable<RiduzioneAumentoVo> contenente i dati per per % di aumento motivazione.
   */
  getPercentualeAumento(
    idAumento: string | number,
    dataInizioVal?: string,
    dataFineVal?: string
  ): Observable<RiduzioneAumentoVo> {
    // Verifico e converto i parametri in string
    idAumento = this._riscaUtilities.convertParamToString(idAumento);

    // Definisco l'url per il collegamento al servizio del server
    const url: string = this._riscaUtilities.createUrl([
      this.beUrl,
      this.PATH_AUMENTI,
      this._riscaUtilities.creaUrlPath(idAumento, this.PATH_SEPARATORE),
    ]);
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      dataIniVal: dataInizioVal,
      dataFineVal: dataFineVal,
    });

    // Lancio la chiamata per il recupero dati
    return this._http.get<RiduzioneAumentoVo>(url, { params });
  }

  /**
   * Funzione che richiama il server per il calcolo del canone per la pratica.
   * @param idRiscossione string o number che identifica un idRiscossione.
   * @param dataRiferimento string o number che identifica una dataRiferimento.
   * @returns Observable<CalcoloCanoneVo> contenente il risultato del calcolo del canone.
   */
  calcoloCanonePratica(
    idRiscossione: string | number,
    dataRiferimento: string | number
  ): Observable<CalcoloCanoneVo> {
    // Verifico e converto i parametri in string
    idRiscossione = this._riscaUtilities.convertParamToString(idRiscossione);
    dataRiferimento =
      this._riscaUtilities.convertParamToString(dataRiferimento);

    // Definisco l'url per il collegamento al servizio del server
    const url = this._config.appUrl(
      this.PATH_CALCOLO_CANONE,
      this.PATH_ID_RISCOSSIONE,
      idRiscossione,
      this.PATH_DATA_RIF,
      dataRiferimento
    );

    // Lancio la chiamata per il recupero dati
    return this._http.get<CalcoloCanoneVo>(url);
  }

  /**
   * Funzione che richiama il server per il calcolo del canone per un'annualità dello stato debitorio.
   * Anche se la struttura del dato tecnico in input è della pratica, è valida anche per l'annualità
   * @param praticaDTVo PraticaDTVo con l'oggetto strutturato del dato tecnico.
   * @param dataRiferimento string con la data in formato server (YYYY-MM-DD) che definisce la data di riferimento per il calcolo canone.
   * @param dataFrazionamento Moment che definisce la data per il frazionamento del canone.
   * @param frazionamento TipiFrazionamentoCanone che definisce il tipo di frazionamento. Per default, con data frazionamento definita, è: TipiFrazionamentoCanone.inizio.
   * @returns Observable<CalcoloCanoneV2Vo> contenente il risultato del calcolo del canone.
   */
  calcoloCanoneAnnualita(
    praticaDTVo: PraticaDTVo,
    dataRif: string,
    dataFrazionamento?: Moment,
    frazionamento?: TipiFrazionamentoCanone
  ): Observable<CalcoloCanoneV2Vo> {
    // Per il calcolo definisco flag e data frazionamento
    let parameters: any = {};

    // Verifico se la data frazionamento esiste
    if (dataFrazionamento && dataFrazionamento.isValid()) {
      // La data è valida, procedo a definire flag e data da passare
      parameters.flgFraz = frazionamento ?? TipiFrazionamentoCanone.inizio;
      parameters.dataFraz =
        this._riscaUtilities.convertMomentToServerDate(dataFrazionamento);
    }

    // Definisco l'url per il collegamento al servizio del server
    const url = this._config.appUrl(this.PATH_CALCOLO_CANONE_V2, dataRif);
    // Definisco i query param
    const params = this._riscaUtilities.createQueryParams(parameters);

    // Lancio la chiamata per il recupero dati
    return this._http.post<CalcoloCanoneV2Vo>(url, praticaDTVo, { params });
  }

  /**
   * ####################################
   * FUNZIONI DI COMODO E DI ELABORAZIONE
   * ####################################
   */

  /**
   * Funzione che ricerca all'interno dell'array dei componenti tecnici per la modifica/dettaglio pratica, il componente dato il suo id.
   * @param idComponenteDt number dell'elemento da cercare nell'array.
   * @returns ComponenteDt trovato o undefined.
   */
  praticaModDtById(idComponenteDt: number): ComponenteDt {
    // Verifico l'input
    if (idComponenteDt == null) {
      // Non posso ricercare
      return undefined;
    }

    // Cerco all'interno della lista di elementi il dato
    return this.praticaModDts.find((c: ComponenteDt) => {
      // Verifico gli id
      return c.id_componente_dt === idComponenteDt;
    });
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  public get ricercaDt(): ComponenteDt {
    return this._ricercaDt;
  }

  public get praticaInsDt(): ComponenteDt {
    return this._praticaInsDt;
  }

  public get praticaModDts(): ComponenteDt[] {
    return this._praticaModDts;
  }

  public get statiDebitoriInsDt(): ComponenteDt {
    return this._statiDebitoriInsDt;
  }

  public get statiDebitoriModDts(): ComponenteDt[] {
    return this._statiDebitoriModDts;
  }
}
