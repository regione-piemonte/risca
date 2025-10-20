import { Injectable } from '@angular/core';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';
import { forkJoin, Observable, of, throwError } from 'rxjs';
import { Subject } from 'rxjs/index';
import { catchError, map } from 'rxjs/operators';
import { Gruppo } from 'src/app/core/commons/vo/gruppo-vo';
import { TipoRiscossioneVo } from 'src/app/core/commons/vo/tipo-riscossione-vo';
import { IstanzaProvvedimentoVo } from '../../../../core/commons/vo/istanza-provvedimento-vo';
import {
  IPraticaVo,
  PraticaDTDataVo,
  PraticaDTVo,
  PraticaEDatiTecnici,
  PraticaVo,
} from '../../../../core/commons/vo/pratica-vo';
import { ProvinciaCompetenzaVo } from '../../../../core/commons/vo/provincia-competenza-vo';
import { TipoIstanzaProvvedimentoVo } from '../../../../core/commons/vo/tipo-istanza-provvidemento-vo';
import { TipoTitoloVo } from '../../../../core/commons/vo/tipo-titolo-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { UserService } from '../../../../core/services/user.service';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import {
  isIstanzaVo,
  isProvvedimentoVo,
} from '../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  DatiAnagrafici,
  GeneraliAmministrativi,
  IRiscaCheckboxData,
  IRiscaTabChanges,
  NuovaIstanzaFormData,
  NuovoProvvedimentoFormData,
  PraticaCorrelati,
  PraticaCorrelatiReq,
  PraticaCorrelatiRes,
  PraticaForms,
  RiscaIstanzePratica,
  RiscaServerError,
  TNIPFormData,
} from '../../../../shared/utilities';
import { GeneraliAmministrativiConsts } from '../../consts/generali-amministrativi/generali-amministrativi.consts';
import { GruppoSoggettoService } from '../dati-anagrafici/gruppo-soggetto.service';
import { PraticheService } from '../pratiche.service';
import { GruppoVo } from './../../../../core/commons/vo/gruppo-vo';

/**
 * Interfaccia di comodo per lo scarico delle liste del componente. Formato: request.
 */
interface ICompGAReq {
  tipiRiscossione: Observable<TipoRiscossioneVo[]>;
  codiciUtenza: Observable<ProvinciaCompetenzaVo[]>;
  tipiIstanza: Observable<TipoIstanzaProvvedimentoVo[]>;
  tipiProvvedimento: Observable<TipoIstanzaProvvedimentoVo[]>;
  tipiTitolo: Observable<TipoTitoloVo[]>;
}

/**
 * Interfaccia di comodo per lo scarico delle liste del componente. Formato: response.
 */
interface ICompGARes {
  tipiRiscossione: TipoRiscossioneVo[];
  codiciUtenza: ProvinciaCompetenzaVo[];
  tipiIstanza: TipoIstanzaProvvedimentoVo[];
  tipiProvvedimento: TipoIstanzaProvvedimentoVo[];
  tipiTitolo: TipoTitoloVo[];
}

/**
 * Interfaccia di comodo per la conversione dei dati istanze provvedimenti ricevuti dal sever.
 */
interface IIstProvConvert {
  istanzeProvvedimenti: IstanzaProvvedimentoVo[];
  tipiIstanza: TipoIstanzaProvvedimentoVo[];
  tipiProvvedimento: TipoIstanzaProvvedimentoVo[];
  tipiTitolo: TipoTitoloVo[];
}

/**
 * Interfaccia di comodo la gestione delle date convertite localmente per i generali amministrativi.
 */
interface IDateGenAmmConverted {
  dataRinunciaRevoca: string;
  dataIniConcesione: string;
  dataFinConcesione: string;
  dataIniSospensione: string;
  dataFinSospensione: string;
}

@Injectable({
  providedIn: 'root',
})
export class InserisciPraticaService {
  /** Oggetto di costanti comuni all'applicazione. */
  private C_C = new CommonConsts();
  /** Oggetto contenente le costanti per il componente. */
  private GA_C = GeneraliAmministrativiConsts;

  /** EventEmitter che definisce il cambio di modalità della pagina per inserimento/modifica pratica. */
  onModalitaChanges$ = new Subject<AppActions>();
  /** EventEmitter che definisce il cambio dei dati della pratica. */
  onPraticaChanges$ = new Subject<PraticaEDatiTecnici>();
  /** EventEmitter che definisce il cambio di tab per quanto riguarda la nav bar del componente inserisci-pratica.component.ts. */
  onISTabChanges$ = new Subject<IRiscaTabChanges>();

  /**
   * Costruttore
   */
  constructor(
    private _gruppoSoggetto: GruppoSoggettoService,
    private _logger: LoggerService,
    private _pratiche: PraticheService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Funzione di setup del servizio
    this.setupServizio();
  }

  /**
   * Funzione di comodo per il setup dati del servizio.
   */
  private setupServizio() {
    // Gestione logiche
  }

  /**
   * #################################################
   * FUNZIONI DI GESTIONE PER GLI EVENTI DELLA PRATICA
   * #################################################
   */

  /**
   * Funzione che emette un evento per impostare la modalità della pagina della pratica.
   * @param m AppActions che definisce la modalità di gestione della pratica.
   */
  modalitaChanged(m: AppActions) {
    // Emetto l'evento
    this.onModalitaChanges$.next(m);
  }

  /**
   * Funzione che emette un evento per definire le nuove informazioni per i dati della pratica.
   * @param praticaEDatiTecnici PraticaEDatiTecnici con i dati della pratica e i dati tecnici aggiornati.
   */
  praticaChanged(praticaEDatiTecnici: PraticaEDatiTecnici) {
    // Emetto l'evento
    this.onPraticaChanges$.next(praticaEDatiTecnici);
  }

  /**
   * Funzione che emette un evento per definire la tab che sta per venire aperta.
   * @param tabs IRiscaTabChanges con il target della tab d'aprire.
   */
  inserisciPraticaTabChanged(tabs: IRiscaTabChanges) {
    // Emetto l'evento
    this.onISTabChanges$.next(tabs);
  }

  /**
   * ################################
   * FUNZIONI DI CONVERSIONE DEI DATI
   * ################################
   */

  /**
   * Funzione di convert da un oggetto GeneraliAmministrativi ad un oggetto PraticaVo.
   * @param genAmm GeneraliAmministrativi da convertire.
   * @returns PraticaVo convertito.
   */
  convertGeneraliAmministrativiToPraticaVo(
    genAmm: GeneraliAmministrativi
  ): PraticaVo {
    // Verifico l'input
    if (!genAmm) {
      // Definisco il messaggio d'errore
      const e = `[convertGeneraliAmministrativiToPraticaVo] - No data found`;
      // Lancio un errore
      throw new Error(e);
    }

    // Definisco il codice riscossione progressivo
    const crp =
      genAmm.progressivoUtenza === '' ? null : genAmm.progressivoUtenza;
    // Converto le date per l'oggetto
    const date = this.convertDateGAForPraticaVo(genAmm);
    // Effettuo il convert per i dati boolean
    const prenotata = genAmm.prenotata?.check ? 1 : 0;

    // Effettuo il convert degli oggetti istanze/provvedimenti
    const istanzeProvvedimentiParse: IstanzaProvvedimentoVo[] =
      this.convertGAIstanzeProvvedimentiToIstanzeProvvedimentiReqVo(
        genAmm.istanzeProvvedimenti
      );

    // Definisco l'oggetto per la configurazione del dato
    const c: IPraticaVo = {
      id_tipo_riscossione: genAmm.tipologiaPratica.id_tipo_riscossione,
      stato_riscossione: genAmm.statoPratica,
      data_rinuncia_revoca: date?.dataRinunciaRevoca,
      soggetto: null,
      gruppo_soggetto: null,
      tipo_autorizza: genAmm.procedimento,
      cod_riscossione_prov: genAmm.codiceUtenza.sigla_provincia,
      cod_riscossione_prog: crp,
      data_ini_concessione: date?.dataIniConcesione || '',
      data_scad_concessione: date?.dataFinConcesione || '',
      data_ini_sosp_canone: date?.dataIniSospensione || '',
      data_fine_sosp_canone: date?.dataFinSospensione || '',
      num_pratica: genAmm.numeroPratica.toString(),
      flg_prenotata: prenotata,
      ambito: this._user.idAmbito.toString(),
      motivo_prenotazione: genAmm.motivazione,
      note_riscossione: genAmm.note,
      provvedimentoIstanza: istanzeProvvedimentiParse,
    };

    // Sanitize dei campi
    this._riscaUtilities.sanitizeFEProperties(c);

    // Creo e ritorno l'oggetto
    return new PraticaVo(c);
  }

  /**
   * Funzione di comodo che converte le date dell'oggetto GeneraliAmministrativi per il salvataggio di una PraticaVo.
   * @param genAmm GeneraliAmministrativi dalla quale estrarre le date.
   * @returns IDateGenAmmConverted con le date convertite.
   */
  convertDateGAForPraticaVo(
    genAmm: GeneraliAmministrativi
  ): IDateGenAmmConverted {
    // Estraggo le date per la conversione
    const dRR: NgbDateStruct = genAmm.dataRinunciaRevoca;
    const dIC: NgbDateStruct = genAmm.dataInizioConcessione;
    const dFC: NgbDateStruct = genAmm.dataFineConcessione;
    const dIS: NgbDateStruct = genAmm.dataInizioSospensione;
    const dFS: NgbDateStruct = genAmm.dataFineSospensione;

    // Parso le date calendari in date server
    const dataRR: string =
      this._riscaUtilities.convertViewDateToServerDate(dRR);
    const dataIC: string =
      this._riscaUtilities.convertViewDateToServerDate(dIC);
    const dataFC: string =
      this._riscaUtilities.convertViewDateToServerDate(dFC);
    const dataIS: string =
      this._riscaUtilities.convertViewDateToServerDate(dIS);
    const dataFS: string =
      this._riscaUtilities.convertViewDateToServerDate(dFS);

    // Costruisco l'oggetto con le date convertite
    const converted: IDateGenAmmConverted = {
      dataRinunciaRevoca: dataRR,
      dataIniConcesione: dataIC,
      dataFinConcesione: dataFC,
      dataIniSospensione: dataIS,
      dataFinSospensione: dataFS,
    };

    // Ritorno l'oggetto composto
    return converted;
  }

  /**
   * Funzione di convert da un array di GAIstanzeProvvedimentiTableData ad un array di IstanzaProvvedimentoVo.
   * @param istanzeProvvedimenti Array di TNIPFormData da convertire.
   * @returns Array di IstanzaProvvedimentoVo convertito.
   */
  convertGAIstanzeProvvedimentiToIstanzeProvvedimentiReqVo(
    istanzeProvvedimenti: TNIPFormData[]
  ): IstanzaProvvedimentoVo[] {
    // Verifico l'input
    if (!istanzeProvvedimenti || istanzeProvvedimenti.length === 0) {
      return [];
    }

    // Effettuo un map delle informazioni
    return istanzeProvvedimenti.map((ip: TNIPFormData) => {
      // Effettuo il convert dell'oggetto
      return this.convertGAIstanzaProvvedimentoToIstanzaProvvedimentoReqVo(ip);
    });
  }

  /**
   * Funzione di convert da un oggetto TNIPFormData ad un oggetto IstanzaProvvedimentoVo.
   * @param istanzaProvvedimento TNIPFormData da convertire.
   * @returns IstanzaProvvedimentoVo convertito.
   */
  convertGAIstanzaProvvedimentoToIstanzaProvvedimentoReqVo(
    istanzaProvvedimento: TNIPFormData
  ): IstanzaProvvedimentoVo {
    // Verifico l'input
    if (!istanzaProvvedimento) {
      return undefined;
    }

    // Verifico la tipologia dell'oggetto
    const isIstanza = this._riscaUtilities.isIstanza(istanzaProvvedimento);
    const isProvvedimento =
      this._riscaUtilities.isProvvedimento(istanzaProvvedimento);

    // A seconda del tipo, estraggo il source dati
    if (isIstanza) {
      // Recupero il source dati
      const source = istanzaProvvedimento as NuovaIstanzaFormData;

      // Formatto la data
      const dataIst = this._riscaUtilities.convertViewDateToServerDate(
        source.dataIstanza
      );

      // Creo un oggetto e lo ritorno
      return new IstanzaProvvedimentoVo({
        id_provvedimento: source.original?.id_provvedimento,
        id_tipo_provvedimento:
          source.tipologiaIstanza ?? source.original?.id_tipo_provvedimento,
        data_provvedimento: dataIst,
        id_tipo_titolo: null,
        num_titolo: source.numeroIstanza ?? '',
        note: source.noteIstanza ?? '',
      });
      // #
    } else if (isProvvedimento) {
      // Recupero il source dati
      const source = istanzaProvvedimento as NuovoProvvedimentoFormData;

      // Formatto la data
      const dataProv = this._riscaUtilities.convertViewDateToServerDate(
        source.dataProvvedimento
      );

      // Creo un oggetto e lo ritorno
      return new IstanzaProvvedimentoVo({
        id_provvedimento: source.original?.id_provvedimento,
        id_tipo_provvedimento: source.tipologiaProvvedimento,
        data_provvedimento: dataProv,
        id_tipo_titolo: source.tipoTitolo,
        num_titolo: source.numeroProvvedimento,
        note: source.noteProvvedimento,
      });
      // #
    } else {
      // Definisco un messaggio per l'errore
      const e = `[convertGAIstanzaProvvedimentoToIstanzaProvvedimentoReqVo] - typeof istanzaProvvedimento unkonw`;
      // Titolo inatteso
      throw new Error(e);
    }
  }

  /**
   * Funzione di convert da un oggetto DatiAnagrafici ad un oggetto PraticaVo.
   * @param datiAnagrafici DatiAnagrafici da convertire.
   * @returns PraticaVo convertito.
   */
  convertDatiAnagraficiToPraticaVo(datiAnagrafici: DatiAnagrafici): PraticaVo {
    // Verifico l'input
    if (!datiAnagrafici) {
      // Definisco il messaggio d'errore
      const e = `[convertDatiAnagraficiToPraticaVo] - No data found`;
      // Lancio un errore
      throw new Error(e);
    }

    // Recupero dall'oggetto in input le informazioni
    const { soggettoPratica, gruppoPratica, recapitiPratica } = datiAnagrafici;
    // Converto l'oggetto del gruppo
    const gruppoS = this._gruppoSoggetto.convertGruppoToGruppoVo(gruppoPratica);
    // Converto i dati del soggetto in maniera standard per il server
    soggettoPratica.gruppo_soggetto =
      this._gruppoSoggetto.convertGruppiToGruppiVo(
        soggettoPratica.gruppo_soggetto as Gruppo[]
      );

    // Definisco l'oggetto per la configurazione del dato
    const c: IPraticaVo = {
      soggetto: soggettoPratica,
      recapiti_riscossione: recapitiPratica,
      gruppo_soggetto: gruppoS,
    };

    // Creo e ritorno l'oggetto
    return new PraticaVo(c);
  }

  /**
   * Funzione di comodo che esegue la generazione dei dati PraticaDTVo per il salvataggio dei dati tecnici.
   * @param praticaVo PraticaVo salvati dall'operrazione di salvataggio dati generali amministrativi.
   * @param praticaDTVo DatiTecnici dati del form da convertire per il salvataggio.
   * @returns PraticaDTVo come risultato delle generazione dei dati.
   */
  generaPraticaDTVoConPraticaVo(
    praticaVo: PraticaVo,
    praticaDTVo: PraticaDTVo
  ): PraticaDTVo {
    // Verifico che l'input esista
    if (!praticaVo || !praticaDTVo) {
      // Errore
      return undefined;
    }

    // Recupero il formato della data per il server
    const DFS = this.C_C.DATE_FORMAT_SERVER;

    // Genero un oggetto per la chiamata al salvataggio dati tecnici
    const riscossioneDT = this.generaPraticaDTVo(
      praticaVo.id_riscossione,
      moment().format(DFS),
      moment().format(DFS),
      praticaDTVo
    );

    // Ritorno l'oggetto generato
    return riscossioneDT;
  }

  /**
   * Funzione che genera le informazioni per ottenere un oggetto PraticaDTVo.
   * @param idRiscossione number che definisce l'id riscossione per la compilazione dati.
   * @param dataModifica string che definisce la data di modifica per la compilazione dati.
   * @param dataInserimento string che definisce la data d'inserimento per la compilazione dati.
   * @param praticaDTVo DatiTecniciVo per la compilazione dei dati.
   * @returns PraticaDTVo generato.
   */
  generaPraticaDTVo(
    idRiscossione: number,
    dataModifica: string,
    dataInserimento: string,
    praticaDTVo: PraticaDTVo
  ): PraticaDTVo {
    // Variabili di comodo
    const neIDR = idRiscossione === undefined;
    const neDM = dataModifica === undefined;
    const neDI = dataInserimento === undefined;
    const nePDTVo = praticaDTVo === undefined;

    // Verifico che l'input esista
    if (neIDR || neDM || neDI || nePDTVo) {
      // Definisco un nome comune per l'errore
      const method = 'generaPraticaDTVo | DatiTecnici';
      const nd = 'non è stato definito';

      // Verifico ogni elemento
      if (neIDR) {
        // Loggo un errore
        this._logger.error(`${method} | idRiscossione | ${nd}`, {
          idRiscossione,
        });
      }
      if (neDM) {
        // Loggo un errore
        this._logger.error(`${method} | dataModifica | ${nd}`, {
          dataModifica,
        });
      }
      if (neDI) {
        // Loggo un errore
        this._logger.error(`${method} | dataInserimento | ${nd}`, {
          dataInserimento,
        });
      }
      if (nePDTVo) {
        // Loggo un errore
        this._logger.error(`${method} | praticaDTVo | ${nd}`, { praticaDTVo });
      }

      // Non esisono i dati per generare la pratica
      throw new Error(method);
    }

    // Recupero i dati tecnici dalla praticaDTVo
    const datiTecnici = praticaDTVo.riscossione?.dati_tecnici || '';
    // Genero un oggetto PraticaDTDataVo
    const riscossioneDati = new PraticaDTDataVo(
      '', // Non è mai il FE a generarlo
      idRiscossione,
      dataModifica,
      dataInserimento,
      datiTecnici
    );

    // Ritorno un nuovo oggetto PraticaDTVo
    return new PraticaDTVo(riscossioneDati);
  }

  /**
   * Funzione di convert da PraticaVo a GeneraliAmministrativi.
   * @param pratica PraticaVo da convertire.
   * @returns Observable<GeneraliAmministrativi> con i dati compatibili con la maschera.
   */
  convertPraticaVoToGeneraliAmministrativi(
    pratica: PraticaVo
  ): Observable<GeneraliAmministrativi> {
    // Verifico l'input
    if (!pratica) {
      // Niente da impostare
      return of(undefined);
    }

    // Recupero l'id ambito
    const idA = this._user.idAmbito;

    // Creo un oggetto con le varie richieste di scarico dati
    const requestList: ICompGAReq = {
      tipiRiscossione: this._pratiche.getTipiRiscossione(idA),
      codiciUtenza: this._pratiche.getProvinceCompetenza(),
      tipiIstanza: this._pratiche.getTipiIstanza(idA),
      tipiProvvedimento: this._pratiche.getTipiProvvedimento(idA),
      tipiTitolo: this._pratiche.getTipiTitolo(idA),
    };

    // Lancio il recupero delle informazioni
    return forkJoin(requestList).pipe(
      map((response: ICompGARes) => {
        // Estraggo i dati
        const {
          tipiRiscossione,
          codiciUtenza,
          tipiIstanza,
          tipiProvvedimento,
          tipiTitolo,
        } = response || {};

        // Recupero il tipo pratica
        const tipoPratica = tipiRiscossione?.find((tr) => {
          // Ricerco per stesso id
          return tr.id_tipo_riscossione === pratica.id_tipo_riscossione;
        });
        // Recupero il codice utenza
        const codiceUtenza = codiciUtenza?.find((cu) => {
          // Ricerco per stesso id
          return cu.sigla_provincia === pratica.cod_riscossione_prov;
        });
        // Definisco l'oggetto per il checkbox prenotata
        const prenotata: IRiscaCheckboxData = {
          id: this.GA_C.LABEL_PRENOTATA,
          label: this.GA_C.LABEL_PRENOTATA,
          value: pratica.flg_prenotata === 1,
          check: pratica.flg_prenotata === 1,
        };
        // Definisco un oggetto di supporto con le informazioni per la gestione di istanze e provvedimenti
        const iPConfigs: IIstProvConvert = {
          istanzeProvvedimenti: pratica.provvedimentoIstanza,
          tipiIstanza,
          tipiProvvedimento,
          tipiTitolo,
        };

        // Genero l'oggetto con i vari dati
        const genAmm: GeneraliAmministrativi = {
          tipologiaPratica: tipoPratica,
          codiceUtenza: codiceUtenza,
          tipoTributoUtenza: null,
          progressivoUtenza: pratica.cod_riscossione_prog,
          numeroPratica: pratica.num_pratica,
          statoPratica: pratica.stato_riscossione,
          dataRinunciaRevoca:
            this._riscaUtilities.convertServerDateToNgbDateStruct(
              pratica.data_rinuncia_revoca
            ),
          procedimento: pratica.tipo_autorizza,
          prenotata: prenotata,
          statoDebInvioSpeciale: null,
          motivazione: pratica.motivo_prenotazione,
          note: pratica.note_riscossione,
          dataInizioConcessione:
            this._riscaUtilities.convertServerDateToNgbDateStruct(
              pratica.data_ini_concessione
            ),
          dataFineConcessione:
            this._riscaUtilities.convertServerDateToNgbDateStruct(
              pratica.data_scad_concessione
            ),
          dataInizioSospensione:
            this._riscaUtilities.convertServerDateToNgbDateStruct(
              pratica.data_ini_sosp_canone
            ),
          dataFineSospensione:
            this._riscaUtilities.convertServerDateToNgbDateStruct(
              pratica.data_fine_sosp_canone
            ),
          istanzeProvvedimenti:
            this.convertListaIstanzaProvvedimentoVoToListaTNIPFormData(
              iPConfigs
            ),
        };

        // Ritorno l'oggetto generato
        return genAmm;
        // #
      }),
      catchError((e: RiscaServerError) => {
        // Loggo l'errore
        this._logger.error('convertPraticaVoToGeneraliAmministrativi', e);
        // Ritorno un valore nullo
        return throwError(e);
      })
    );
  }

  /**
   * Converte una lista di IstanzaProvvedimentoVo in una lista di NuovaIstanzaFormData o NuovoProvvedimentoFormData
   * @param c IIstProvConvert contenente le informazioni per la conversione delle informazioni.
   * @returns lista di NuovaIstanzaFormData o NuovoProvvedimentoFormData convertiti
   */
  private convertListaIstanzaProvvedimentoVoToListaTNIPFormData(
    c: IIstProvConvert
  ): TNIPFormData[] {
    // Verifico l'input
    const existC = c != undefined;
    const existIP = existC && c.istanzeProvvedimenti?.length > 0;
    const existTI = existC && c.tipiIstanza?.length > 0;
    const existTP = existC && c.tipiProvvedimento?.length > 0;
    const existTT = existC && c.tipiTitolo?.length > 0;
    const checkData = existIP && existTI && existTP && existTT;
    // Verifico tutte le condizioni
    if (!checkData) {
      // Ritorno array vuoto, i dati sono parziali o assenti
      return [];
    }

    // Definsco un array di elementi
    const listaTNIPFD: TNIPFormData[] = [];
    // Estraggo i dati dalla configurazione
    const { istanzeProvvedimenti, tipiIstanza, tipiProvvedimento, tipiTitolo } =
      c;

    // Distunguo i due tipi di dati tramite il check sulla proprietà tipo titolo
    const istanze: IstanzaProvvedimentoVo[] = istanzeProvvedimenti.filter(
      (istProv: IstanzaProvvedimentoVo) => isIstanzaVo(istProv)
    );
    const provvedimenti: IstanzaProvvedimentoVo[] = istanzeProvvedimenti.filter(
      (istProv: IstanzaProvvedimentoVo) => isProvvedimentoVo(istProv)
    );

    // Ciclo le istanze e costruisco gli oggetti
    istanze.forEach((i: IstanzaProvvedimentoVo) => {
      // Creo il contenitore per un'istanza
      let newI: NuovaIstanzaFormData = {
        original: undefined,
        tipologiaIstanza: undefined,
        dataIstanza: undefined,
      };
      // Cerco i dati del tipo istanza
      const tipoIstanza = tipiIstanza.find((ti) => {
        // Cerco per stesso id
        return (
          ti.id_tipo_provvedimento ===
          i.id_tipo_provvedimento.id_tipo_provvedimento
        );
      });
      // Effettuo il parse della data
      const dataIstanza = this._riscaUtilities.convertServerDateToNgbDateStruct(
        i.data_provvedimento
      );

      // Aggiungo le informazioni
      newI.original = i;
      newI.dataIstanza = dataIstanza;
      newI.noteIstanza = i.note;
      newI.numeroIstanza = i.num_titolo;
      newI.tipologiaIstanza = tipoIstanza;

      // Aggiungo l'istanza all'array dati
      listaTNIPFD.push(newI);
    });

    // Ciclo i provvedimenti e costruisco gli oggetti
    provvedimenti.forEach((p: IstanzaProvvedimentoVo) => {
      // Creo il contenitore per un'istanza
      let newP: NuovoProvvedimentoFormData = {
        original: undefined,
        dataProvvedimento: undefined,
        numeroProvvedimento: undefined,
        tipoTitolo: undefined,
        tipologiaProvvedimento: undefined,
      };
      // Cerco i dati del tipo istanza
      const tipoProvvedimento = tipiProvvedimento.find((tp) => {
        // Cerco per stesso id
        return (
          tp.id_tipo_provvedimento ===
          p.id_tipo_provvedimento.id_tipo_provvedimento
        );
      });
      // Cerci i dati del tipo titolo
      const tipoTitolo = tipiTitolo.find((tt) => {
        // Ricerco per stesso id_tipo_titolo
        return tt.id_tipo_titolo === p.id_tipo_titolo.id_tipo_titolo;
      });
      // Effettuo il parse della data
      const dataProvvedimento =
        this._riscaUtilities.convertServerDateToNgbDateStruct(
          p.data_provvedimento
        );

      // Aggiungo le informazioni
      newP.original = p;
      newP.dataProvvedimento = dataProvvedimento;
      newP.noteProvvedimento = p.note;
      newP.numeroProvvedimento = p.num_titolo;
      newP.tipoTitolo = tipoTitolo;
      newP.tipologiaProvvedimento = tipoProvvedimento;

      // Aggiungo l'istanza all'array dati
      listaTNIPFD.push(newP);
    });

    // Ritorno la lista
    return listaTNIPFD;
  }

  /**
   * Funzione di convert dati da un oggetto PraticaVo ad un oggetto DatiAnagrafici.
   * @param pratica PraticaVo da convertire.
   * @returns DatiAnagrafici convertito.
   */
  convertPraticaVoToDatiAnagrafici(pratica: PraticaVo): DatiAnagrafici {
    // Verifico l'input
    if (!pratica) {
      return;
    }

    // Recupero le informazioni dalla pratica
    const { soggetto, gruppo_soggetto, recapiti_riscossione } = pratica;
    // Converto il tipo del gruppo della pratica
    const gruppo = this._gruppoSoggetto.convertGruppoVoToGruppo(
      gruppo_soggetto as GruppoVo
    );

    // Creo un oggetto con le informazioni
    const datiAnagrafici: DatiAnagrafici = {
      soggettoPratica: soggetto,
      gruppoPratica: gruppo,
      recapitiPratica: recapiti_riscossione,
    };

    // Ritorno l'oggetto convertito
    return datiAnagrafici;
  }

  /**
   * ################################
   * FUNZIONI DI CONTROLLO PRATICA
   * ################################
   */

  /**
   * Funzione che estrae da un oggetto GeneraliAmministrativi le informazioni per comporre il codice utenza in base all'ambito
   * @param genAmm GeneraliAmministrativi contenete i dati inseriti nella form generali/amministrativi.
   * @returns string che definisce il codice utenza a secondo dell'ambito.
   */
  getCodiceUtenzaPerAmbito(genAmm: GeneraliAmministrativi): string {
    // Verifico che esista l'oggetto
    if (!genAmm) {
      return '';
    }

    // TODO: verificare l'ambito e gestire di conseguenza il codice utenza.
    // Per ora vale solo ambito risorse idriche

    // Verifico la struttura per il recupero dati
    const siglaProvincia = genAmm.codiceUtenza?.sigla_provincia ?? '';
    const progressivoUtenza = genAmm.progressivoUtenza ?? '';

    // Creo e ritorno il codice utenza
    return `${siglaProvincia}${progressivoUtenza}`;
  }

  /**
   * ###########################
   * FUNZIONI DI SALVATAGGIO
   * ###########################
   */

  /**
   * Funzione che effettua il convert dei dati da PraticaForms a PraticaVo e salva i dati sul server.
   * @param datiPratica PraticaForms da convertire.
   * @returns PraticaVo che rappresentano i dati salvati.
   */
  inserisciPratica(datiPratica: PraticaForms): Observable<PraticaVo> {
    // Verifico che ci sia l'oggetto
    if (!datiPratica) {
      // Lancio un errore
      throw new Error('[inserisciPratica] - No data found');
    }

    // Recupero le informazioni dei form
    const { generaliAmministrativi, datiAnagrafici } = datiPratica;

    // Converto l'oggetto GeneraliAmministrativi in PraticaVo
    const praticaVoGA = this.convertGeneraliAmministrativiToPraticaVo(
      generaliAmministrativi
    );
    // Converto l'oggetto DatiAnagrafici in PraticaVo
    const praticaVoDA: PraticaVo =
      this.convertDatiAnagraficiToPraticaVo(datiAnagrafici);

    // Mergio le informazioni degli oggetti
    const praticaVo = new PraticaVo();
    // Aggiungo i dati generai amministrativi
    praticaVo.setData(praticaVoGA);
    // Aggiungo i dati generai amministrativi
    praticaVo.setData(praticaVoDA);
    // Aggiungo l'id del componente dt utilizzato per i dati tecnici
    praticaVo.id_componente_dt = datiPratica.idComponenteDt;

    // Richiamo e ritorno il salvataggio dei dati amministrativi
    return this._pratiche.insertPratica(praticaVo);
  }

  /**
   * Funzione che effettua il convert dei dati da PraticaForms a PraticaVo e aggiorna i dati sul server.
   * @param datiPratica PraticaForms da convertire.
   * @returns PraticaVo che rappresentano i dati salvati.
   */
  modificaPratica(
    idPratica: number,
    datiPratica: PraticaForms
  ): Observable<PraticaVo> {
    // Verifico che ci sia l'oggetto
    if (!datiPratica || idPratica == undefined) {
      // Lancio un errore
      throw new Error('[modificaPratica] - No data found');
    }

    // Recupero le informazioni dei form
    const { generaliAmministrativi, datiAnagrafici } = datiPratica;

    // Converto l'oggetto GeneraliAmministrativi in PraticaVo
    const praticaVoGA = this.convertGeneraliAmministrativiToPraticaVo(
      generaliAmministrativi
    );
    // Converto l'oggetto DatiAnagrafici in PraticaVo
    const praticaVoDA = this.convertDatiAnagraficiToPraticaVo(datiAnagrafici);

    // Mergio le informazioni degli oggetti
    const praticaVo = new PraticaVo();
    // Aggiungo i dati generai amministrativi
    praticaVo.setData(praticaVoGA);
    // Aggiungo i dati anagrafici
    praticaVo.setData(praticaVoDA);
    // Aggiungo l'id del componente dt utilizzato per i dati tecnici
    praticaVo.id_componente_dt = datiPratica.idComponenteDt;
    // Aggiungo l'id della pratica per la modifica
    praticaVo.id_riscossione = idPratica;

    // Richiamo e ritorno il salvataggio dei dati amministrativi
    return this._pratiche.updatePratica(praticaVo);
  }

  /**
   * Funzione che salva i dati della pratica correlati alla pratica.
   * @param praticaVo PraticaVo contente i dati della pratica.
   * @param correlati PraticaCorrelati contenente le varie sezioni dati per la pratica da salvare.
   * @returns Observable<PraticaCorrelatiRes>.
   */
  salvaDatiCorrelatiAPratica(
    praticaVo: PraticaVo,
    correlati: PraticaCorrelati
  ): Observable<PraticaCorrelatiRes> {
    // Verifico l'input
    if (!praticaVo || !correlati) {
      // Variabili di comodo
      const t = 'salvaDatiCorrelatiAPratica';
      const d = { praticaVo, correlati };
      // Creo un d'errore come quello del server
      const e = this._riscaUtilities.customResponseError(t, null, d);
      // Ritorno un errore come Observable
      return throwError(e);
    }

    // Loggo l'inizio salvataggio dati
    this._logger.debug('salvaDatiCorrelatiAPratica', 'SAVING');
    // Loggo gli oggetti da salvare
    for (let [sezione, dati] of Object.entries(correlati)) {
      // Loggo sezione e dati
      this._logger.debug(sezione, dati);
    }

    // Creo un oggetto con le richieste di salvataggio dati
    const pcReq: PraticaCorrelatiReq = {
      datiTecnici: this.salvaDatiTecnici(praticaVo, correlati.datiTecnici),
    };

    // Lancio il salvataggio dei dati correlati ai generali amministrativi
    return forkJoin(pcReq);
  }

  /**
   * Funzione che aggiorna i dati della pratica correlati alla pratica.
   * @param praticaVo PraticaVo contente i dati della pratica.
   * @param correlati PraticaCorrelati contenente le varie sezioni dati per la pratica da salvare.
   * @returns Observable<PraticaCorrelatiRes>.
   */
  modificaDatiCorrelatiAPratica(
    praticaVo: PraticaVo,
    correlati: PraticaCorrelati
  ): Observable<PraticaCorrelatiRes> {
    // Verifico l'input
    if (!praticaVo || !correlati) {
      // Variabili di comodo
      const t = 'modificaDatiCorrelatiAPratica';
      const d = { praticaVo, correlati };
      // Creo un d'errore come quello del server
      const e = this._riscaUtilities.customResponseError(t, null, d);
      // Ritorno un errore come Observable
      return throwError(e);
    }

    // Loggo l'inizio salvataggio dati
    this._logger.debug('modificaDatiCorrelatiAPratica', 'SAVING');
    // Loggo gli oggetti da salvare
    for (let [sezione, dati] of Object.entries(correlati)) {
      // Loggo sezione e dati
      this._logger.debug(sezione, dati);
    }

    // Creo un oggetto con le richieste di salvataggio dati
    const pcReq: PraticaCorrelatiReq = {
      datiTecnici: this.salvaDatiTecnici(praticaVo, correlati.datiTecnici),
    };

    // Lancio il salvataggio dei dati correlati ai generali amministrativi
    return forkJoin(pcReq);
  }

  /**
   * Funzione che salva i dati tecnici.
   * @param praticaVo PraticaVo contenente le informazioni utili per il salvataggio.
   * @param praticaDTVo PraticaDTVo contente le informazioni da salvare per i dati tecnici.
   * @returns Observable<praticaDTVo> come risultato del salvataggio.
   */
  salvaDatiTecnici(praticaVo: PraticaVo, praticaDTVo: PraticaDTVo) {
    // Loggo l'azione
    this._logger.debug('salvaDatiTecnici', { praticaVo, praticaDTVo });

    // Genero un oggetto per la chiamata al salvataggio dati tecnici
    const pDtVo = this.generaPraticaDTVoConPraticaVo(praticaVo, praticaDTVo);

    // Loggo l'azione
    this._logger.debug('salvaDatiTecnici', pDtVo);
    // Richiamo il servizio per il salvataggio dei dati
    return this._pratiche.insertDatiTecnici(pDtVo);
  }

  /**
   * Funzione che modifica i dati tecnici.
   * @param praticaVo PraticaVo contenente le informazioni utili per il salvataggio.
   * @param praticaDTVo PraticaDTVo contente le informazioni da salvare per i dati tecnici.
   * @returns Observable<PraticaDTVo> come risultato del salvataggio.
   */
  modificaDatiTecnici(praticaVo: PraticaVo, praticaDTVo: PraticaDTVo) {
    // Genero un oggetto per la chiamata al salvataggio dati tecnici
    const pDtVo = this.generaPraticaDTVoConPraticaVo(praticaVo, praticaDTVo);

    // Loggo l'azione
    this._logger.debug('modificaDatiTecnici', pDtVo);
    // Richiamo il servizio per il salvataggio dei dati
    return this._pratiche.updateDatiTecnici(pDtVo);
  }

  /**
   * Funzione che effettua la get dei dati tecnici di una pratica.
   * @param idRiscossione number che definisce l'id della pratica per il recupero dei dati.
   * @returns Observable<PraticaDTVo> con i dati tecnici della pratica.
   */
  getDatiTecnici(idRiscossione: number): Observable<PraticaDTVo> {
    // Richiamo la funzione
    return this._pratiche.getDatiTecnici(idRiscossione);
  }

  /**
   * ##############################################################################
   * FUNZIONI DI COMODO PER LA GESTIONE DEL CAMBIO DI TAB PER L'INSERIMENTO PRATICA
   * ##############################################################################
   */

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: generali-amministrativi.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  movingToGeneraliAmministrativi(tabs: IRiscaTabChanges): boolean {
    // Definisco la costante con la tab specifica da controllare
    const ga = RiscaIstanzePratica.generaliAmministrativi;
    // Lancio la funzione di verifica
    return this._riscaUtilities.movingIntoTab(tabs, ga);
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: dati anagrafici.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  movingToDatiAnagrafici(tabs: IRiscaTabChanges): boolean {
    // Definisco la costante con la tab specifica da controllare
    const da = RiscaIstanzePratica.datiAnagrafici;
    // Lancio la funzione di verifica
    return this._riscaUtilities.movingIntoTab(tabs, da);
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: dati tecnici.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  movingToDatiTecnici(tabs: IRiscaTabChanges): boolean {
    // Definisco la costante con la tab specifica da controllare
    const dt = RiscaIstanzePratica.datiTecnici;
    // Lancio la funzione di verifica
    return this._riscaUtilities.movingIntoTab(tabs, dt);
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: dati contabili.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  movingToDatiContabili(tabs: IRiscaTabChanges): boolean {
    // Definisco la costante con la tab specifica da controllare
    const dc = RiscaIstanzePratica.datiContabili;
    // Lancio la funzione di verifica
    return this._riscaUtilities.movingIntoTab(tabs, dc);
  }

  /**
   * Funzione di supporto che verifica se ci si sta spostando verso la tab: documenti allegati.
   * @param tabs IRiscaTabChanges con le indicazioni della tab attuale e della tab su cui ci si sta spostando.
   * @returns boolean che indica se ci si sta spostando su questa specifica tab.
   */
  movingToDocumentiAllegati(tabs: IRiscaTabChanges): boolean {
    // Definisco la costante con la tab specifica da controllare
    const doc = RiscaIstanzePratica.documentiAllegati;
    // Lancio la funzione di verifica
    return this._riscaUtilities.movingIntoTab(tabs, doc);
  }
}
