import { HelperVo } from './helper-vo';
import { Moment } from 'moment';

/**
 * Interfaccia che definisce i possibili valori che si possono gestire per la tipologia di estrazione dati: nap o codice utenza.
 */
export enum TipoRicercaSDPagamento {
  nap = 'NAP',
  codiceUtenza = 'CODICE_UTENZA',
  numeroPratica = 'NUMERO_PRATICA',
}

/**
 * Interfaccia che contiene l'oggetto di ricerca per gli stati debitori per il dettaglio pagamento.
 */
export interface IRicercaStatiDebitoriPagamentoVo {
  cod_utenza?: string;
  nap?: string;
  num_pratica?: string;
  calcola_interessi?: string;
  sd_da_escludere?: number[];
  importo_da?: number;
  importo_a?: number;
  titolare?: string;
  flg_pratica?: 'SI' | 'NO';
}

/**
 * Classe che contiene l'oggetto di ricerca per gli stati debitori per il dettaglio pagamento.
 */
export class RicercaStatiDebitoriPagamentoVo extends HelperVo {
  /** string che definisce il codice utenza. Per l'oggetto di ricerca, questo campo se valorizzato esclude: nap e num_pratica. */
  cod_utenza?: string;
  /** string che definisce il nap. Per l'oggetto di ricerca, questo campo se valorizzato esclude: cod_utenza e num_pratica. */
  nap?: string;
  /** string che definisce il numero pratica. Per l'oggetto di ricerca, questo campo se valorizzato esclude: cod_utenza e nap. */
  num_pratica?: string;

  /** Moment che contiene la data operazione val per la gestione del calcolo interessi. Se non definita, non verranno calcolati gli interessi degli stati debitori. */
  calcola_interessi?: Moment;

  sd_da_escludere?: number[];
  importo_da?: number;
  importo_a?: number;
  titolare?: string;
  flgPratica?: boolean;

  constructor(iRSDPVo?: IRicercaStatiDebitoriPagamentoVo) {
    super();

    this.cod_utenza = iRSDPVo?.cod_utenza;
    this.nap = iRSDPVo?.nap;
    this.num_pratica = iRSDPVo?.num_pratica;
    this.calcola_interessi = this.convertServerDateToMoment(
      iRSDPVo?.calcola_interessi
    );
    this.sd_da_escludere = iRSDPVo?.sd_da_escludere ?? [];
    this.importo_da = iRSDPVo?.importo_da;
    this.importo_a = iRSDPVo?.importo_a;
    this.titolare = iRSDPVo?.titolare;
    this.flgPraticaBE = iRSDPVo?.flg_pratica;
  }

  toServerFormat(): IRicercaStatiDebitoriPagamentoVo {
    // La ricerca per cod_utenza, nap e num_pratica è esclusiva, definisco le variabili d'appoggio
    let cod_utenza: string;
    let nap: string;
    let num_pratica: string;

    // Codice utenza per primo
    if (this.cod_utenza) {
      // Assegno la variabile
      cod_utenza = this.cod_utenza;
      // #
    } else if (this.nap) {
      // Assegno la variabile
      nap = this.nap;
      // #
    } else if (this.num_pratica) {
      // Assegno la variabile
      num_pratica = this.num_pratica;
      // #
    }

    // La ricerca del titolare va in coppia con il flg_pratica, se il titolare non è definito, il flag pratica deve essere undefined
    let flgPratica: 'SI' | 'NO';
    // Verifico se esiste il titolare
    if (this.titolare) {
      // Esiste, definisco il flag
      flgPratica = this.flgPraticaBE;
    }

    // Definisco l'oggetto formattato
    const be: IRicercaStatiDebitoriPagamentoVo = {
      cod_utenza,
      nap,
      num_pratica,
      calcola_interessi: this.convertMomentToServerDate(this.calcola_interessi),
      sd_da_escludere:
        this.sd_da_escludere?.length > 0 ? this.sd_da_escludere : undefined,
      importo_da: this.importo_da,
      importo_a: this.importo_a,
      titolare: this.titolare,
      flg_pratica: flgPratica,
    };

    // Ritorno l'oggetto formattato per il be
    return be;
  }

  /**
   * Getter che ritorna il valore di flg_pratica, per il BE.
   * @returns string risultato della conversione del boolean flgPratica con 'SI' a true o 'NO' a false, come da definizione del BE.
   */
  private get flgPraticaBE(): 'SI' | 'NO' {
    // Verifico il flag pratica
    const flgPratica = this.flgPratica;
    // Verifico il valore e gestisco il ritorno
    return flgPratica === true ? 'SI' : flgPratica === false ? 'NO' : null;
  }

  /**
   * Setter che definisce il valore di flg_pratica, per il BE.
   * @param flgPratica 'SI' o 'NO' da convertire per la gestione dati.
   */
  private set flgPraticaBE(flgPratica: 'SI' | 'NO') {
    // Verifico il valore e gestisco l'assegnamento
    this.flgPratica =
      flgPratica === 'SI' ? true : flgPratica === 'NO' ? false : null;
  }

  /**
   * Getter che ritorna il tipo ricerca stati debitori per il pagamento.
   * @returns TipoRicercaSDPagamento con il tipo ricerca stati debitori per il pagamento.
   */
  public get tipoRicercaSDPagamento(): TipoRicercaSDPagamento {
    // Codice utenza per primo
    if (this.cod_utenza) {
      // Ritorno la ricerca pagamento specifica
      return TipoRicercaSDPagamento.codiceUtenza;
      // #
    } else if (this.nap) {
      // Ritorno la ricerca pagamento specifica
      return TipoRicercaSDPagamento.nap;
      // #
    } else if (this.num_pratica) {
      // Ritorno la ricerca pagamento specifica
      return TipoRicercaSDPagamento.numeroPratica;
      // #
    }
  }
}
