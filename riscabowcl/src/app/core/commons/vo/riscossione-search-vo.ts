import { Moment } from 'moment';
import { TipoSoggettoVo } from '../../../features/ambito/models';
import {
  IFRAPModalitaRicerca,
  IRicercaPraticaAvanzataForm,
} from '../../../features/pratiche/components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.interfaces';
import { RiscaBooleanNumber } from '../../../shared/utilities';
import { ComuneVo } from './comune-vo';
import { HelperVo } from './helper-vo';
import { NazioneVo } from './nazione-vo';
import { ProvinciaCompetenzaVo } from './provincia-competenza-vo';
import { ProvinciaVo } from './provincia-vo';
import { StatoRiscossioneVo } from './stati-riscossione-vo';
import { TipoIstanzaProvvedimentoVo } from './tipo-istanza-provvidemento-vo';
import { TipoRiscossioneVo } from './tipo-riscossione-vo';
import { TipoTitoloVo } from './tipo-titolo-vo';

/**
 * Dati request nella ricerca delle pratiche contenente i dati delle istanze da ricercare
 */
export interface IRicercaIstanzaVo {
  id_istanza: number;
  data_istanza_da: string;
  data_istanza_a: string;
}

/**
 * Dati request nella ricerca delle pratiche contenente i dati dei provvedimenti da ricercare
 */
export interface IRicercaProvvedimentoVo {
  id_provvedimento: number;
  id_tipo_titolo: number;
  numero_titolo: string;
  data_provvedimento_da: string;
  data_provvedimento_a: string;
}

/**
 * Classe che definisce le informazioni per la ricerca delle riscossioni.
 * L'oggetto viene utilizzato sia per la ricerca semplice che per la ricerca avanzata.
 * La struttura è quella che si aspetta il servizio, per cui la tipizzazione dei dati è differente da quella di FE.
 */
export interface IRiscossioneSearchV2Vo {
  // FE: Codice utenza
  cod_utente?: string;
  // FE: Tipo utenza => descrizione
  des_tipo_soggetto?: string;
  // FE: Tipo utenza => id
  id_tipo_utente?: number;
  // FE: Ragione sociale/Cognome nome
  ragione_sociale?: string;
  // FE: Codice fiscale
  codice_fiscale?: string;
  // FE: Partita IVA
  partita_iva?: string;
  // FE: Stato
  nazione?: NazioneVo;
  // FE: Comune di residenza
  comune_di_residenza?: ComuneVo;
  // FE: Provincia
  provincia?: ProvinciaVo;
  // FE: Non mappato
  provincia_di_competenza?: ProvinciaCompetenzaVo;
  // FE: Città estera => ATTENZIONE NON E' MAI STATA MAPPATA NELL'OGGETTO DI RICERCA
  citta_estera_nascita?: string;
  // FE: Indirizzo
  indirizzo?: string;
  // FE: Scadenza concessione DA/A => Formato server: YYYY-MM-DD
  scadenza_concessione_da?: string;
  scadenza_concessione_a?: string;
  // FE: Tipo titolo => id
  id_tipo_titolo?: number;
  // FE: Tipo provvedimento => id
  id_tipo_provvedimento?: number;
  // FE: Numero titolo
  numero_titolo?: string;
  // FE: Data titolo DA/A => Formato server: YYYY-MM-DD
  data_titolo_da?: string;
  data_titolo_a?: string;
  // FE: Tipo pratica => id
  id_tipo_riscossione?: number;
  // FE: Stato pratica => id
  id_stato_riscossione?: number;
  // FE: Data rinuncia DA/A => Formato server: YYYY-MM-DD
  data_rinuncia_revoca_da?: string;
  data_rinuncia_revoca_a?: string;
  // FE: Canone
  canone?: number;
  // FE: Anno canone
  anno_canone?: number;
  // FE: Restituito al mittente
  restituito_al_mittente?: RiscaBooleanNumber;
  // FE: Tabella con le differenti istanze
  istanze?: IRicercaIstanzaVo[];
  // FE: Tabella con le differenti provvedimenti
  provvedimenti?: IRicercaProvvedimentoVo[];
  // FE: Informazione generata dalle componenti tecniche
  dati_tecnici?: string;

  // FE: Numero pratica
  numero_pratica?: string;
  // FE: Codice utenza
  cod_riscossione?: string;
  // FE: NAP
  nap?: string;
  // FE: Codice avviso
  codice_avviso?: string;

  // FE: Non mappato
  id_soggetto?: number;
  // FE: Non mappato
  id_gruppo?: number;
}

/**
 * Classe che definisce le informazioni per la ricerca delle riscossioni.
 * La classe è alla base delle logiche di FE e permette la gestione delle informazioni per la gestione del server.
 */
export class RiscossioneSearchV2Vo extends HelperVo {
  modalitaRicerca?: IFRAPModalitaRicerca;
  codiceUtenza?: string;
  tipoSoggetto?: TipoSoggettoVo;
  ragioneSociale?: string; // Questo può contenere anche cognome/nome
  codiceFiscale?: string;
  partitaIva?: string;
  stato?: NazioneVo;
  comune?: ComuneVo;
  provincia?: ProvinciaVo;
  provinciaCompetenza?: ProvinciaCompetenzaVo;
  cittaEstera?: string;
  indirizzo?: string;
  scadenzaConcessioneDa?: Moment;
  scadenzaConcessioneA?: Moment;
  tipoTitolo?: TipoTitoloVo;
  tipoProvvedimento?: TipoIstanzaProvvedimentoVo;
  numeroTitolo?: string;
  dataTitoloDa?: Moment;
  dataTitoloA?: Moment;
  tipologiaPratica?: TipoRiscossioneVo;
  statoPratica?: StatoRiscossioneVo;
  dataRinunciaRevocaDa?: Moment;
  dataRinunciaRevocaA?: Moment;
  canone?: number;
  annoCanone?: number;
  restituitoAlMittente?: boolean;
  istanze: IRicercaIstanzaVo[];
  provvedimenti: IRicercaProvvedimentoVo[];
  datiTecnici?: string;
  numeroPratica?: string;
  codiceRiscossione?: string;
  nap?: string;
  codiceAvviso?: string;

  idSoggetto?: number;
  idGruppo?: number;

  /**
   * Costruttore.
   * @param iRSVo IRiscossioneSearchV2Vo con configurazione iniziale.
   */
  constructor(iRSVo?: IRiscossioneSearchV2Vo) {
    super();

    // Verifico esiste la configurazione
    if (!iRSVo) {
      // Nessuna configurazione
      return;
    }

    this.modalitaRicerca = undefined;
    this.tipoSoggetto = this.generateTipoUtenza(iRSVo.id_tipo_utente);
    this.codiceUtenza = iRSVo.cod_utente;
    this.ragioneSociale = iRSVo.ragione_sociale;
    this.codiceFiscale = iRSVo.codice_fiscale;
    this.partitaIva = iRSVo.partita_iva;
    this.stato = iRSVo.nazione;
    this.comune = iRSVo.comune_di_residenza;
    this.provincia = iRSVo.provincia;
    this.provinciaCompetenza = iRSVo.provincia_di_competenza;
    this.cittaEstera = iRSVo.citta_estera_nascita;
    this.indirizzo = iRSVo.indirizzo;
    this.scadenzaConcessioneDa = this.convertServerDateToMoment(
      iRSVo.scadenza_concessione_da
    );
    this.scadenzaConcessioneA = this.convertServerDateToMoment(
      iRSVo.scadenza_concessione_a
    );
    this.tipoTitolo = this.generateTipoTitolo(iRSVo.id_tipo_titolo);
    this.tipoProvvedimento = this.generateTipoProvvedimento(
      iRSVo.id_tipo_provvedimento
    );
    this.numeroTitolo = iRSVo.numero_titolo;
    this.dataTitoloDa = this.convertServerDateToMoment(iRSVo.data_titolo_da);
    this.dataTitoloA = this.convertServerDateToMoment(iRSVo.data_titolo_a);
    this.tipologiaPratica = this.generateTipoRiscossione(
      iRSVo.id_tipo_riscossione
    );
    this.statoPratica = this.generateStatoRiscossione(
      iRSVo.id_stato_riscossione
    );
    this.dataRinunciaRevocaDa = this.convertServerDateToMoment(
      iRSVo.data_rinuncia_revoca_da
    );
    this.dataRinunciaRevocaA = this.convertServerDateToMoment(
      iRSVo.data_rinuncia_revoca_a
    );
    this.canone = iRSVo.canone;
    this.annoCanone = iRSVo.anno_canone;
    this.restituitoAlMittente = this.convertServerBoolNumToBoolean(
      iRSVo.restituito_al_mittente
    );
    this.istanze = iRSVo.istanze;
    this.provvedimenti = iRSVo.provvedimenti;
    this.datiTecnici = iRSVo.dati_tecnici;
    this.numeroPratica = iRSVo.numero_pratica;
    this.codiceRiscossione = iRSVo.cod_riscossione;
    this.nap = iRSVo.nap;
    this.codiceAvviso = iRSVo.codice_avviso;

    this.idSoggetto = iRSVo.id_soggetto;
    this.idGruppo = iRSVo.id_gruppo;
  }

  /**
   * Funzione di comodo per la generazione di un oggetto: TipoSoggetto.
   * L'oggetto da BE non è strutturato in maniera completa, la funzione quindi ricostruisce parte dell'oggetto.
   * @param id_tipo_utente number con id_tipo_utente per la generazione del dato.
   * @returns TipoSoggetto generato.
   */
  private generateTipoUtenza(id_tipo_utente: number): TipoSoggettoVo {
    // Verifico l'input
    if (id_tipo_utente != undefined) {
      // Genero un oggetto parziale
      const obj: TipoSoggettoVo = {
        cod_tipo_soggetto: undefined,
        des_tipo_soggetto: undefined,
        id_tipo_soggetto: id_tipo_utente,
        ordina_tipo_soggetto: undefined,
      };
      // Ritorno l'oggetto generato
      return obj;
    }

    // Niente configurazione possibile
    return undefined;
  }

  /**
   * Funzione di comodo per la generazione di un oggetto: TipoTitoloVo.
   * L'oggetto da BE non è strutturato in maniera completa, la funzione quindi ricostruisce parte dell'oggetto.
   * @param id_tipo_titolo number con id_tipo_titolo per la generazione del dato.
   * @returns TipoTitoloVo generato.
   */
  private generateTipoTitolo(id_tipo_titolo: number): TipoTitoloVo {
    // Verifico l'input
    if (id_tipo_titolo != undefined) {
      // Genero un oggetto parziale
      const obj: TipoTitoloVo = {
        cod_tipo_titolo: undefined,
        des_tipo_titolo: undefined,
        id_tipo_titolo: id_tipo_titolo,
        data_inizio_validita: undefined,
        ambito: undefined,
        ordina_tipo_titolo: undefined,
        flg_default: 0,
      };
      // Ritorno l'oggetto generato
      return obj;
    }

    // Niente configurazione possibile
    return undefined;
  }

  /**
   * Funzione di comodo per la generazione di un oggetto: TipoProvvedimentoVo.
   * L'oggetto da BE non è strutturato in maniera completa, la funzione quindi ricostruisce parte dell'oggetto.
   * @param id_tipo_provvedimento number con id_tipo_provvedimento per la generazione del dato.
   * @returns TipoProvvedimentoVo generato.
   */
  private generateTipoProvvedimento(
    id_tipo_provvedimento: number
  ): TipoIstanzaProvvedimentoVo {
    // Verifico l'input
    if (id_tipo_provvedimento != undefined) {
      // Genero un oggetto parziale
      const obj: TipoIstanzaProvvedimentoVo = {
        cod_tipo_provvedimento: undefined,
        des_tipo_provvedimento: undefined,
        id_tipo_provvedimento,
        flg_istanza: undefined,
        data_inizio_validita: undefined,
        ambito: undefined,
        ordina_tipo_provv: undefined,
        flg_default: 0,
      };
      // Ritorno l'oggetto generato
      return obj;
    }

    // Niente configurazione possibile
    return undefined;
  }

  /**
   * Funzione di comodo per la generazione di un oggetto: TipoRiscossioneVo.
   * L'oggetto da BE non è strutturato in maniera completa, la funzione quindi ricostruisce parte dell'oggetto.
   * @param id_tipo_riscossione number con id_tipo_riscossione per la generazione del dato.
   * @returns TipoRiscossioneVo generato.
   */
  private generateTipoRiscossione(
    id_tipo_riscossione: number
  ): TipoRiscossioneVo {
    // Verifico l'input
    if (id_tipo_riscossione != undefined) {
      // Genero un oggetto parziale
      const obj: TipoRiscossioneVo = {
        id_tipo_riscossione: id_tipo_riscossione,
        cod_tipo_riscossione: undefined,
        des_tipo_riscossione: undefined,
        ambito: undefined,
        flg_default: 0,
        ordina_tipo_risco: 0,
      };
      // Ritorno l'oggetto generato
      return obj;
    }

    // Niente configurazione possibile
    return undefined;
  }

  /**
   * Funzione di comodo per la generazione di un oggetto: StatoRiscossioneVo.
   * L'oggetto da BE non è strutturato in maniera completa, la funzione quindi ricostruisce parte dell'oggetto.
   * @param id_stato_riscossione number con id_tipo_provvedimento per la generazione del dato.
   * @returns StatoRiscossioneVo generato.
   */
  private generateStatoRiscossione(
    id_stato_riscossione: number
  ): StatoRiscossioneVo {
    // Verifico l'input
    if (id_stato_riscossione != undefined) {
      // Genero un oggetto parziale
      const obj: StatoRiscossioneVo = {
        id_stato_riscossione,
        cod_stato_riscossione: undefined,
        des_stato_riscossione: undefined,
      };
      // Ritorno l'oggetto generato
      return obj;
    }

    // Niente configurazione possibile
    return undefined;
  }

  /**
   * Funzione che gestisce l'aggiornamento delle informazioni della classe partendo da un oggetto di tipo: IRicercaPraticaAvanzataForm.
   * Le informazioni se non definite andranno comunque a sovrascrivere quelle già presenti nella classe.
   * @param data IRicercaPraticaAvanzataForm con le informazioni per l'aggiornamento dati della classe.
   */
  setDataFromRAPForm(data: IRicercaPraticaAvanzataForm) {
    // Verifico l'input
    if (!data) {
      // Niente d'aggiornare
      return;
    }

    // Estraggo le proprietà dall'oggetto data
    const {
      modalitaRicerca,
      codiceUtenza,
      tipoSoggetto,
      ragioneSocialeOCognome,
      codiceFiscale,
      partitaIVA,
      stato,
      provincia,
      indirizzo,
      comuneResidenza,
      cittaEsteraResidenza,
      scadenzaConcessioneDa,
      scadenzaConcessioneA,
      tipoTitolo,
      tipoProvvedimento,
      numeroTitolo,
      dataTitoloDa,
      dataTitoloA,
      tipologiaPratica,
      statoPratica,
      provinciaCompetenza,
      dataRinunciaRevocaDa,
      dataRinunciaRevocaA,
      annoCanone,
      canone,
      restituitoAlMittente,
    } = data;

    this.modalitaRicerca = modalitaRicerca;
    this.codiceUtenza = codiceUtenza;
    this.tipoSoggetto = tipoSoggetto;
    this.ragioneSociale = ragioneSocialeOCognome;
    this.codiceFiscale = codiceFiscale;
    this.partitaIva = partitaIVA;
    this.stato = stato;
    this.provincia = provincia;
    this.provinciaCompetenza = provinciaCompetenza;
    this.indirizzo = indirizzo;
    this.comune = comuneResidenza;
    this.cittaEstera = cittaEsteraResidenza;
    this.scadenzaConcessioneDa = this.convertNgbDateStructToMoment(
      scadenzaConcessioneDa
    );
    this.scadenzaConcessioneA =
      this.convertNgbDateStructToMoment(scadenzaConcessioneA);
    this.tipoTitolo = tipoTitolo;
    this.tipoProvvedimento = tipoProvvedimento;
    this.numeroTitolo = numeroTitolo;
    this.dataTitoloDa = this.convertNgbDateStructToMoment(dataTitoloDa);
    this.dataTitoloA = this.convertNgbDateStructToMoment(dataTitoloA);
    this.tipologiaPratica = tipologiaPratica;
    this.statoPratica = statoPratica;
    this.dataRinunciaRevocaDa =
      this.convertNgbDateStructToMoment(dataRinunciaRevocaDa);
    this.dataRinunciaRevocaA =
      this.convertNgbDateStructToMoment(dataRinunciaRevocaA);
    this.annoCanone = annoCanone;
    this.canone = canone;
    this.restituitoAlMittente = restituitoAlMittente;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IRiscossioneSearchV2Vo {
    // Creo l'oggetto per il server
    let be: IRiscossioneSearchV2Vo = {
      cod_utente: this.codiceUtenza,
      des_tipo_soggetto: this.tipoSoggetto?.des_tipo_soggetto,
      id_tipo_utente: this.tipoSoggetto?.id_tipo_soggetto,
      ragione_sociale: this.ragioneSociale,
      codice_fiscale: this.codiceFiscale,
      partita_iva: this.partitaIva,
      nazione: this.stato,
      comune_di_residenza: this.comune,
      provincia: this.provincia,
      citta_estera_nascita: this.cittaEstera,
      indirizzo: this.indirizzo,
      scadenza_concessione_da: this.convertMomentToServerDate(
        this.scadenzaConcessioneDa
      ),
      scadenza_concessione_a: this.convertMomentToServerDate(
        this.scadenzaConcessioneA
      ),
      id_tipo_titolo: this.tipoTitolo?.id_tipo_titolo,
      id_tipo_provvedimento: this.tipoProvvedimento?.id_tipo_provvedimento,
      numero_titolo: this.numeroTitolo,
      data_titolo_da: this.convertMomentToServerDate(this.dataTitoloDa),
      data_titolo_a: this.convertMomentToServerDate(this.dataTitoloA),
      id_tipo_riscossione: this.tipologiaPratica?.id_tipo_riscossione,
      id_stato_riscossione: this.statoPratica?.id_stato_riscossione,
      data_rinuncia_revoca_da: this.convertMomentToServerDate(
        this.dataRinunciaRevocaDa
      ),
      data_rinuncia_revoca_a: this.convertMomentToServerDate(
        this.dataRinunciaRevocaA
      ),
      canone: this.canone,
      anno_canone: this.annoCanone,
      restituito_al_mittente: this.convertBooleanToServerBoolNum(
        this.restituitoAlMittente
      ),
      istanze: this.istanze,
      provvedimenti: this.provvedimenti,
      dati_tecnici: this.datiTecnici,
      numero_pratica: this.numeroPratica,
      cod_riscossione: this.codiceRiscossione,
      nap: this.nap,
      codice_avviso: this.codiceAvviso,
      id_soggetto: this.idSoggetto,
      id_gruppo: this.idGruppo,
      provincia_di_competenza: this.provinciaCompetenza,
    };

    // Itero le proprietà e definisco a null tutte le proprietà che risultano: undefined, 0 o ""
    // Vado ad iterare le proprietà dell'oggetto
    for (let [p, v] of Object.entries(be)) {
      // Verifico se il valore rientra nei casi di controllo
      const isUnd = v === undefined;
      const isEmptyStr = v === '';
      const isZero = v === 0;
      const toNull = isUnd || isEmptyStr || isZero;
      // Verifico le condizioni
      if (toNull) {
        // Devo trasformare il valore in null
        be[p] = null;
      }
    }

    // Pulisco le informazioni dai metadati di FE
    this.sanitizeFEProperties(be);
    // Ritorno l'oggetto server like
    return be;
  }
}
