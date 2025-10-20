import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { round, truncate } from 'lodash';
import * as moment from 'moment';
import { Moment } from 'moment';
import { ComuneVo } from '../../../../core/commons/vo/comune-vo';
import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { IstanzaProvvedimentoVo } from '../../../../core/commons/vo/istanza-provvedimento-vo';
import { PraticaVo } from '../../../../core/commons/vo/pratica-vo';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { CodiciTipiRecapito } from '../../../../core/commons/vo/tipo-recapito-vo';
import { CommonConsts } from '../../../consts/common-consts.consts';
import {
  CodIstatNazioni,
  IRiscaAnnoSelect,
  RiscaCss,
  RiscaCssHandlerTypes,
  RiscaFormatoDate,
  RiscaRegExp,
  RiscaServerError,
  RiscaSortTypes,
  RiscaTablePagination,
  ServerStringAsBoolean,
  StatiSDFlagAnnullato,
  TipiIstanzaProvvedimento,
  TNIPFormData,
  TRiscaServerBoolean,
} from '../../../utilities';
import { RiscaInnerHTML } from '../../../utilities/classes/risca-inner-html/risca-inner-html.class';

/**
 * #################################################################
 * FUNZIONI TYPESCRIPT UTILIZZABILI AL DI FUORI DEL CONTESTO ANGULAR
 * #################################################################
 */

/**
 * Funzione che verifica la validità di una partita IVA.
 * Essendo la funzione usata anche all'esterno di strutture Angular, è stata dichiarata come una funzione esportabile.
 * @param pi string per la partita IVA da controllare.
 * @returns boolean con (true) per partita IVA valida, altrimenti (false).
 */
export const controllaPartitaIVA = (pi: string): boolean => {
  // Verifico che l'input esista
  if (!pi) {
    return false;
  }

  // Recupero l'oggetto della regexp
  const riscaRegExp = new RiscaRegExp();

  // Effettuo il test sulla regexp
  if (!riscaRegExp.partitaIva.test(pi)) {
    return false;
  }

  // Eseguo lo script di validazione della partita iva
  let s = 0;

  for (let i = 0; i <= 9; i += 2) {
    s += pi.charCodeAt(i) - '0'.charCodeAt(0);
  }
  for (let i = 1; i <= 9; i += 2) {
    var c = 2 * (pi.charCodeAt(i) - '0'.charCodeAt(0));
    if (c > 9) c = c - 9;
    s += c;
  }

  const controllo = (10 - (s % 10)) % 10;

  if (controllo != pi.charCodeAt(10) - '0'.charCodeAt(0)) return false;

  // Partit IVA valida
  return true;
};

/**
 * Funzione che genera una stringa alfanumerica di 10 caratteri.
 * A seconda del valore passato in input, verrà generato un id più complesso.
 * La quantità di caratteri verranno moltiplicati sulla base di complexity.
 * Quindi per complexity:
 * - 1 = random id di 10 caratteri;
 * - 2 = random id di 20 caratteri;
 * - etc...
 * @param complexity number che definisce la complessità dell'id da generare. Default: 2.
 * @returns string che definisci un id randomico.
 */
export const generateRandomId = (complexity = 2): string => {
  // Definisco la funzione interna di generazione di un id
  const randomId = (): string => Math.random().toString(36).slice(2);
  // Verifico l'input
  if (!complexity || complexity <= 0) {
    return randomId();
  }

  // Definisco il contenitore dell'id
  let id = '';
  // Itero per la complessita
  for (let i = 0; i < complexity; i++) {
    id = `${id}${randomId()}`;
  }

  // Ritorno l'id
  return id;
};

/**
 * Funzione di supporto per la conversione dei boolean ricevuti da server, in formato string, in boolean standard.
 * @param sb TRiscaServerBoolean che definisce la tipologia di boolean server.
 * @returns boolean convertito.
 */
export const convertServerBooleanStringToBoolean = (
  sb: TRiscaServerBoolean
): boolean => {
  // Verifico il valore dell'input
  if (sb?.toUpperCase() === ServerStringAsBoolean.true.toUpperCase()) {
    // Ritorno true
    return true;
    // #
  } else {
    // Ritorno false
    return false;
  }
};

/**
 * Funzione di supporto per la conversione dei boolean ricevuti da server, in formato number, in boolean standard.
 * @param sb number che definisce la tipologia di boolean server.
 * @returns boolean convertito.
 */
export const convertServerBooleanNumberToBoolean = (sb: number): boolean => {
  // Verifico il valore dell'input
  if (sb === 1) {
    // Ritorno true
    return true;
    // #
  } else {
    // Ritorno false
    return false;
  }
};

/**
 * Funzione di supporto per la verifica dei boolean ricevuti da server, in formato string, in boolean standard con condizione: true.
 * @param sb TRiscaServerBoolean che definisce la tipologia di boolean server.
 * @returns boolean che indica se il valore è true.
 */
export const isSBSTrue = (sb: TRiscaServerBoolean): boolean => {
  // Richiamo la funzione di conversione
  return convertServerBooleanStringToBoolean(sb);
};

/**
 * Funzione di supporto per la verifica dei boolean ricevuti da server, in formato number, in boolean standard con condizione: true.
 * @param sb number che definisce la tipologia di boolean server.
 * @returns boolean che indica se il valore è true.
 */
export const isSBNTrue = (sb: number): boolean => {
  // Richiamo la funzione di conversione
  return convertServerBooleanNumberToBoolean(sb);
};

/**
 * Funzione di supporto per la conversione di un boolean starndard, in un boolean string gestito dal server.
 * @param b boolean che definisce il dato da convertire.
 * @returns string che definisce il boolean in formato string del server, convertito.
 */
export const convertBooleanToServerBooleanString = (b: boolean): string => {
  // Verifico il valore dell'input
  return b ? ServerStringAsBoolean.true : ServerStringAsBoolean.false;
};

/**
 * Funzione di supporto per la conversione di un boolean starndard, in un boolean number gestito dal server.
 * @param b boolean che definisce il dato da convertire.
 * @returns number che definisce il boolean in formato number del server, convertito.
 */
export const convertBooleanToServerBooleanNumber = (b: boolean): number => {
  // Verifico il valore dell'input
  return b ? 1 : 0;
};

/**
 * Funzione che esegue un sort tra due stringhe, basato sul sort degli array in javascript.
 * @param a string a per il sort.
 * @param b string b per il sort.
 * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
 * @returns number che definisce il risultato del sort.
 */
export const sortStrings = (
  a: string,
  b: string,
  sortType: RiscaSortTypes = RiscaSortTypes.crescente
): number => {
  // Verifico che gli input esistano
  if (a === undefined || b === undefined) {
    return 0;
  }

  // Effettuo l'uppercase per il controllo
  const stringA = a.toUpperCase();
  const stringB = b.toUpperCase();

  // Ordinamento crescente
  if (sortType === RiscaSortTypes.crescente) {
    if (stringA < stringB) {
      return -1;
    }
    if (stringA > stringB) {
      return 1;
    }
  }
  // Ordinamento decrescente
  else if (sortType === RiscaSortTypes.decrescente) {
    if (stringA < stringB) {
      return 1;
    }
    if (stringA > stringB) {
      return -1;
    }
  }

  // Niente sort
  return 0;
};

/**
 * Funzione che esegue un sort tra due numeri, basato sul sort degli array in javascript.
 * @param a number a per il sort.
 * @param b number b per il sort.
 * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
 * @returns number che definisce il risultato del sort.
 */
export const sortNumbers = (
  a: number,
  b: number,
  sortType: RiscaSortTypes = RiscaSortTypes.crescente
): number => {
  // Verifico che gli input esistano
  if (a === undefined || b === undefined) {
    return 0;
  }

  // Ordinamento crescente
  if (sortType === RiscaSortTypes.crescente) {
    return a - b;
  }
  // Ordinamento decrescente
  else if (sortType === RiscaSortTypes.decrescente) {
    return b - a;
  }

  // Niente sort
  return 0;
};

/**
 * Funzione che esegue un sort tra due date (in formato stringa), basato sul sort degli array in javascript.
 * @param a string a per il sort.
 * @param b string b per il sort.
 * @param f string che definisce il formato delle stringhe.
 * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
 * @returns number che definisce il risultato del sort.
 */
export const sortDatesString = (
  a: string,
  b: string,
  f: string,
  sortType: RiscaSortTypes = RiscaSortTypes.crescente
): number => {
  // Controllo il formato
  if (!f) {
    return 0;
  }

  // Definisco il sort
  const sortAsc = sortType === RiscaSortTypes.crescente;
  const sortDesc = sortType === RiscaSortTypes.decrescente;

  // Verifico a
  if (!a) {
    // Verifico l'ordinamento
    if (sortAsc) {
      return -1;
    } else if (sortDesc) {
      return 1;
    } else {
      return 0;
    }
  }
  // Verifico b
  if (!b) {
    // Verifico l'ordinamento
    if (sortAsc) {
      return 1;
    } else if (sortDesc) {
      return -1;
    } else {
      return 0;
    }
  }

  // Effettuo l'uppercase per il controllo
  const momentA = moment(a, f);
  const momentB = moment(b, f);

  // Ordinamento crescente
  if (sortAsc) {
    if (momentA.isBefore(momentB)) {
      return -1;
    }
    if (momentA.isAfter(momentB)) {
      return 1;
    }
  }
  // Ordinamento decrescente
  else if (sortDesc) {
    if (momentA.isBefore(momentB)) {
      return 1;
    }
    if (momentA.isAfter(momentB)) {
      return -1;
    }
  }

  // Niente sort
  return 0;
};

/**
 * Funzione che esegue un sort tra due date (in formato moment), basato sul sort degli array in javascript.
 * @param a string a per il sort.
 * @param b string b per il sort.
 * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
 * @returns number che definisce il risultato del sort.
 */
export const sortMoments = (
  a: Moment,
  b: Moment,
  sortType: RiscaSortTypes = RiscaSortTypes.crescente
): number => {
  // Definisco il sort
  const sortAsc = sortType === RiscaSortTypes.crescente;
  const sortDesc = sortType === RiscaSortTypes.decrescente;

  // Verifico a
  if (!moment(a).isValid()) {
    // Verifico l'ordinamento
    if (sortAsc) {
      return -1;
    } else if (sortDesc) {
      return 1;
    } else {
      return 0;
    }
  }
  // Verifico b
  if (!moment(b).isValid()) {
    // Verifico l'ordinamento
    if (sortAsc) {
      return 1;
    } else if (sortDesc) {
      return -1;
    } else {
      return 0;
    }
  }

  // Effettuo l'uppercase per il controllo
  const momentA = a;
  const momentB = b;

  // Ordinamento crescente
  if (sortAsc) {
    if (momentA.isBefore(momentB)) {
      return -1;
    }
    if (momentA.isAfter(momentB)) {
      return 1;
    }
  }
  // Ordinamento decrescente
  else if (sortDesc) {
    if (momentA.isBefore(momentB)) {
      return 1;
    }
    if (momentA.isAfter(momentB)) {
      return -1;
    }
  }

  // Niente sort
  return 0;
};

/**
 * Funzione che esegui un sort tra due oggetti, basato sul sort degli array in javascript.
 * @param a Object a per il sort.
 * @param b Object b per il sort.
 * @param sortLogic Funzione (a: any, b: any) => number che definisce le logiche di sort.
 * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
 * @returns number che definisce il risultato del sort.
 */
export const sortObjects = (
  a: any,
  b: any,
  sortLogic: (a: any, b: any) => number,
  sortType: RiscaSortTypes = RiscaSortTypes.crescente
): number => {
  // Verifico la compare esista
  if (!sortLogic) {
    return 0;
  }

  // Lancio la logica di sort definita
  const result = sortLogic(a, b);

  // Ordinamento crescente
  if (sortType === RiscaSortTypes.crescente) {
    if (result < 0) {
      return -1;
    }
    if (result > 0) {
      return 1;
    }
  }
  // Ordinamento decrescente
  else if (sortType === RiscaSortTypes.decrescente) {
    if (result < 0) {
      return 1;
    }
    if (result > 0) {
      return -1;
    }
  }

  // Niente sort
  return 0;
};

/**
 * Funzione di supporto che gestisce il cognome e nome, o denominazione del soggetto.
 * @param soggetto SoggettoVo contenente i dati del soggetto.
 * @returns string con l'identificativo del soggetto.
 */
export const identificativoSoggetto = (soggetto: SoggettoVo): string => {
  // Verifico l'input
  if (!soggetto) {
    return '';
  }

  // Estraggo e verifico la denominazione
  const denominazione = soggetto.den_soggetto;
  // Verifico se esiste una denominazione (prende priorità rispetto a cognome nome)
  if (denominazione) {
    // Ritorno la denominazione del soggetto
    return denominazione;
  }

  // Denominazione assente, recupero cognome e nome
  const { cognome, nome } = soggetto;
  // Genero le generalità
  const generalita = `${cognome || ''} ${nome || ''}`.trim();
  // Verifico se esistono le generalità
  if (generalita) {
    // Ritorno le generalità
    return generalita;
  }

  // Ritorno stringa vuota
  return '';
};

/**
 * Funzione di supporto che gestisce il cognome, nome e denominazione del soggetto.
 * Questa funzione ritorna il dato completo e concatenato del soggetto nella seguente struttura:
 * { den_soggetto - cognome nome }
 * @param soggetto SoggettoVo contenente i dati del soggetto.
 * @returns string con l'identificativo completo del soggetto.
 */
export const identificativoSoggettoCompleto = (
  soggetto: SoggettoVo
): string => {
  // Verifico l'input
  if (!soggetto) {
    return '';
  }

  // Cerco di estrarre cognome, nome e generalità
  const { cognome, nome } = soggetto;
  const denominazione = soggetto.den_soggetto;
  // Genero le generalità
  const generalita = `${cognome || ''} ${nome || ''}`.trim();

  // Verifico i casi per la gestione delle informazioni
  if (denominazione && generalita) {
    // Esistono sia denominazione che cognome, nome. Concateno e ritorno
    return `${denominazione} - ${generalita}`;
    // #
  } else if (!denominazione && generalita) {
    // Esistono solo nome e cognome
    return generalita;
    // #
  } else if (denominazione && !generalita) {
    // Esiste solo la denominazione
    return denominazione;
    // #
  } else {
    // Mancano i dati
    return '';
  }
};

/**
 * Funzione di comodo che estrae il recapito principale di un soggetto.
 * @param soggetto SoggettoVo dalla quale estrarre il recapito principale.
 * @returns RecapitoVo con il recapito principale.
 */
export const recapitoPrincipaleSoggetto = (
  soggetto: SoggettoVo
): RecapitoVo => {
  // Verifico l'input
  if (!soggetto) {
    // Ritorno undefined
    return undefined;
  }

  // Recupero il recapito principale
  return soggetto.recapiti?.find((r: RecapitoVo) => {
    // Ritorno il recapito principale
    return isRecapitoPrincipale(r);
  });
};

/**
 * Funzione di comodo che estrae i reapiti alternativi di un soggetto.
 * @param soggetto SoggettoVo dalla quale estrarre i recapiti alternativi.
 * @returns RecapitoVo con i recapiti alternativi.
 */
export const recapitiAlternativiSoggetto = (
  soggetto: SoggettoVo
): RecapitoVo[] => {
  // Verifico l'input
  if (!soggetto) {
    // Ritorno undefined
    return undefined;
  }

  // Recupero il recapito principale
  return soggetto.recapiti?.filter((r: RecapitoVo) => {
    // Ritorno il recapito principale
    return isRecapitoAlternativo(r);
  });
};

/**
 * Funzione di comodo che estrae il recapito principale da una pratica.
 * @param pratica PraticaVo dalla quale estrarre il recapito principale.
 * @returns RecapitoVo con il recapito principale.
 */
export const recapitoPrincipalePratica = (pratica: PraticaVo): RecapitoVo => {
  // Verifico l'input
  if (!pratica || !pratica.recapiti_riscossione) {
    // Niente configurazione
    return undefined;
  }

  // Recupero i recapiti della riscossione
  const recapiti = pratica.recapiti_riscossione;
  // Cerco e ritorno il recapito princiaple
  return recapiti.find((r: RecapitoVo) => {
    // Ritorno l'oggetto se il recapito è il principale
    return isRecapitoPrincipale(r);
  });
};

/**
 * Funzione di comodo che estrae il possibile recapito alternativo di una pratica.
 * @param pratica PraticaVo dalla quale estrarre i recapiti alternativi.
 * @returns RecapitoVo con i recapiti alternativi.
 */
export const recapitoAlternativoPratica = (pratica: PraticaVo): RecapitoVo => {
  // Verifico l'input
  if (!pratica || !pratica.recapiti_riscossione) {
    // Niente configurazione
    return undefined;
  }

  // Recupero i recapiti della riscossione
  const recapiti = pratica.recapiti_riscossione;
  // Cerco e ritorno il recapito alterntivo
  return recapiti.find((r: RecapitoVo) => {
    // Ritorno l'oggetto se il recapito è il principale
    return isRecapitoAlternativo(r);
  });
};

/**
 * Funzione di supporto che gestisce la composizione dei dati dell'indirizzo del soggetto.
 * @param soggetto SoggettoVo dalla quale estrarre le informazioni.
 * @param recapito RecapitoVo che definisce il recapito per la quale estrarre le informazioni. Se non definito, verrà estratto il recapito principale.
 * @returns string con l'indirizzo del soggetto.
 */
export const indirizzoRecapitoSoggetto = (
  soggetto: SoggettoVo,
  recapito?: RecapitoVo
): string => {
  // Verifico l'input
  if (!soggetto) {
    return '';
  }

  // Definisco il recapito per la gestione dei dati
  recapito = recapito ? recapito : recapitoPrincipaleSoggetto(soggetto);
  // Ritorno la composizione dell'indirizzo
  return indirizzoRecapito(recapito);
};

/**
 * Funzione di supporto che gestisce la composizione dei dati dell'indirizzo di un recapito.
 * @param recapito RecapitoVo che definisce il recapito per la quale estrarre le informazioni.
 * @returns string con l'indirizzo del recapito.
 */
export const indirizzoRecapito = (recapito: RecapitoVo): string => {
  // Definisco il contenitore dell'indirizzo
  let indirizzo = '';
  // Verifico che il recapito sia esistente
  if (!recapito) {
    // Non ho un recapito di riferimento per la composizione
    return indirizzo;
  }

  // Recupero i dati "comuni" dal recapito
  const ind = `${recapito.indirizzo || ''}`;
  const nciv = `${recapito.num_civico || ''}`;
  // Compongo la prima parte dell'indirizzo
  indirizzo = `${ind} ${nciv}`;

  // Verifico la nazione recuperata dal recapito
  const istatNazione = recapito.nazione_recapito?.cod_istat_nazione;
  // Verifico che il codice istat esista
  if (istatNazione === undefined) {
    // Ritorno la composizione dell'indirizzo parziale
    return indirizzo;
  }

  // Se la nazione del recapito principale è l'Italia compongo l'indirizzo in un modo
  if (istatNazione === CodIstatNazioni.italia) {
    // Recupero i dati del comune di residenza
    const comune = `${recapito?.comune_recapito?.denom_comune || ''}`;
    const cap = `${recapito.cap_recapito || ''}`;
    // Recupero la sigla della provincia dal comune
    let provincia = recapito?.comune_recapito?.provincia?.sigla_provincia;
    // Se esiste la sigla, creo una stringa per l'output
    provincia = provincia ? '(' + provincia.trim() + ')' : '';

    // Compongo l'indirizzo
    indirizzo = `${indirizzo} ${cap} ${comune} ${provincia}`;
    // #
  } else {
    // NazioneVo estera, compongo l'indirizzo con le altre informazioni
    const citta = `${recapito.citta_estera_recapito || ''}`;
    const nazione = `${recapito.nazione_recapito?.denom_nazione || ''}`;
    const cap = `${recapito.cap_recapito || ''}`;

    // Compongo l'indirizzo
    indirizzo = `${indirizzo} ${cap} ${citta} ${nazione}`;
  }

  // Effettuo una trim di pulizia
  indirizzo = indirizzo.trim();
  // Ritorno la composizione dell'indirizzo
  return indirizzo;
};

/**
 * Funzione di supporto che verifica se un recapito è di tipo principale.
 * @param recapito RecapitoVo che definisce il recapito per la verifica.
 * @returns boolean che definisce se il recapito è un recapito principale.
 */
export const isRecapitoPrincipale = (recapito: RecapitoVo): boolean => {
  // Verifico l'input
  if (!recapito || !recapito.tipo_recapito) {
    // Non sono definite le informazioni, ritorno false
    return false;
  }

  // Recupero il codice per il recapito principale
  const codRP = CodiciTipiRecapito.principale;
  // Ritorno per stesso id
  return recapito.tipo_recapito.cod_tipo_recapito === codRP;
};

/**
 * Funzione di supporto che verifica se un recapito è di tipo alternativo.
 * @param recapito RecapitoVo che definisce il recapito per la verifica.
 * @returns boolean che definisce se il recapito è un recapito alternativo.
 */
export const isRecapitoAlternativo = (recapito: RecapitoVo): boolean => {
  // Verifico l'input
  if (!recapito || !recapito.tipo_recapito) {
    // Non sono definite le informazioni, ritorno false
    return false;
  }

  // Recupero il codice per il recapito alternativo
  const codRA = CodiciTipiRecapito.alternativo;
  // Ritorno per stesso id
  return recapito.tipo_recapito.cod_tipo_recapito === codRA;
};

/**
 * Funzione di supporto effettua dei controlli e tenta il parse di un oggetto.
 * @param o any da verificare e parsare.
 * @returns any come oggetto parsato.
 */
export const riscaJSONParse = (o: any): any => {
  // Verifico l'input
  if (o === undefined) {
    // Blocco le logiche
    return;
  }

  // Verifico la tipologia
  if (typeof o === 'string') {
    // Ritorno il parse
    return JSON.parse(o);
    // #
  } else {
    // Ritorno l'input com'è entrato
    return o;
  }
};

/**
 * Funzione che verifica il tipo dell'input è Istanza o Provvedimento.
 * @param istanzaOProvvedimento TNIPFormData da verificare.
 * @returns TipiIstanzaProvvedimento che definisce la tipologia.
 */
export const typeOfIstanzaOrProvvedimento = (
  istanzaOProvvedimento: TNIPFormData
): TipiIstanzaProvvedimento => {
  // Definisco due costanti per riconoscere la tipologia dell'input
  const TIPOLOGIA_ISTANZA = 'tipologiaIstanza';
  const TIPOLOGIA_PROVVEDIMENTO = 'tipologiaProvvedimento';
  // Definisco di che tipo è l'oggetto da convertire
  const isIstanza = istanzaOProvvedimento.hasOwnProperty(TIPOLOGIA_ISTANZA);
  const isProvvedimento = istanzaOProvvedimento.hasOwnProperty(
    TIPOLOGIA_PROVVEDIMENTO
  );

  // Verifico la tipologia
  if (isIstanza) {
    return TipiIstanzaProvvedimento.istanza;
  }
  if (isProvvedimento) {
    return TipiIstanzaProvvedimento.provvedimento;
  }

  // Tipo non definito o errato
  throw new Error('typeOfIstanzaOrProvvedimento | invalid input');
};

/**
 * Funzione che verifica se l'input è un'istanza.
 * @param istanzaOProvvedimento IstanzaProvvedimentoVo da verificare.
 * @returns boolean che definisce se l'oggetto è un'istanza.
 */
export const isIstanzaVo = (
  istanzaOProvvedimento: IstanzaProvvedimentoVo
): boolean => {
  // Recupero il flag istanza dell'oggetto
  const flgIstanza = istanzaOProvvedimento?.id_tipo_provvedimento?.flg_istanza;
  // Verifico che esista la variabile
  if (flgIstanza == undefined) {
    // Non esiste il valore
    return false;
  }

  // Verifico che l'id sia 0 = istanza
  return convertServerBooleanNumberToBoolean(parseInt(flgIstanza));
};

/**
 * Funzione che verifica se l'input è un provvedimento.
 * @param istanzaOProvvedimento IstanzaProvvedimentoVo da verificare.
 * @returns boolean che definisce se l'oggetto è un provvedimento.
 */
export const isProvvedimentoVo = (
  istanzaOProvvedimento: IstanzaProvvedimentoVo
): boolean => {
  // Recupero il flag istanza dell'oggetto
  const flgIstanza = istanzaOProvvedimento?.id_tipo_provvedimento?.flg_istanza;
  // Verifico che esista la variabile
  if (flgIstanza == undefined) {
    // Non esiste il valore
    return false;
  }

  // Verifico che l'id sia 0 = istanza
  return !convertServerBooleanNumberToBoolean(parseInt(flgIstanza));
};

/**
 * Funzione che verifica se l'input è un'istanza.
 * @param istanzaOProvvedimento TNIPFormData da verificare.
 * @returns boolean che definisce se l'oggetto è un'istanza.
 */
export const isIstanza = (istanzaOProvvedimento: TNIPFormData): boolean => {
  // Richiamo la funzione per sapere il tipo dell'oggetto
  const typeOfIP = typeOfIstanzaOrProvvedimento(istanzaOProvvedimento);
  // Ritorno il check sul tipo
  return typeOfIP === TipiIstanzaProvvedimento.istanza;
};

/**
 * Funzione che verifica se l'input è un provvedimento.
 * @param istanzaOProvvedimento TNIPFormData da verificare.
 * @returns boolean che definisce se l'oggetto è un provvedimento.
 */
export const isProvvedimento = (
  istanzaOProvvedimento: TNIPFormData
): boolean => {
  // Richiamo la funzione per sapere il tipo dell'oggetto
  const typeOfIP = typeOfIstanzaOrProvvedimento(istanzaOProvvedimento);
  // Ritorno il check sul tipo
  return typeOfIP === TipiIstanzaProvvedimento.provvedimento;
};

/**
 * Funzione che formatta l'input in data: GG<separatore>MM<separatore>AAAA.
 * Se la data è già una stringa, viene ritornata senza parsing.
 * @param date string | NgbDateStruct da convertire.
 * @param separatore string che definisce il separatore della data. Il default è "-".
 * @param returnIfDateIsNull qualsiasi cosa si vuole che sia ritornata se date non è valorizzato. Il default è una stringa vuota
 * @returns string data formattata.
 */
export const convertNgbDateStructToString = (
  date: string | NgbDateStruct,
  separatore: string = '-',
  returnIfDateIsNull: any = ''
): string => {
  // Verifico che la data esista
  if (!date) {
    return returnIfDateIsNull;
  }
  // Verifico se la data non è già stringa
  if (date && typeof date === 'string') {
    return date;
  }

  // La data è un oggetto NgbDateStruct
  const dateNgb = date as NgbDateStruct;
  // Compongo la data
  const dataString = `${dateNgb.day}${separatore}${dateNgb.month}${separatore}${dateNgb.year}`;
  // Compongo il formato della data
  const dataFormat = `DD${separatore}MM${separatore}YYYY`;
  // Tramite moment creo la data e la ritorno formattata
  return moment(dataString, dataFormat).format(dataFormat);
};

/**
 * Funzione che formatta l'input in data string.
 * @param date NgbDateStruct da convertire.
 * @returns string data formattata.
 */
export const convertNgbDateStructToMoment = (date: NgbDateStruct): Moment => {
  // Verifico che la data esista
  if (!date) {
    return undefined;
  }

  // Parso l'oggetto in una stringa
  const dateS = `${date.day}/${date.month}/${date.year}`;
  // Tento di convertire la data in moment
  const dateM = moment(dateS, RiscaFormatoDate.view);

  // Verifico se la conversione è andata a buon fine
  if (moment(dateM).isValid()) {
    // Ritorno la data convertita
    return dateM;
    // #
  } else {
    // La data aveva qualcosa che non andava, ritorno undefined
    return undefined;
  }
};

/**
 * Funzione che formatta l'input in data string.
 * Se la data è già una stringa, viene ritornata senza parsing.
 * @param date string | NgbDateStruct da convertire.
 * @param formato string che definisce il formato di destinazione.
 * @returns string data formattata.
 */
export const convertNgbDateStructToDateString = (
  date: string | NgbDateStruct,
  formato: string
): string => {
  // Verifico che la data esista
  if (!date || !formato) {
    return '';
  }
  // Verifico se la data non è già stringa
  if (date && typeof date === 'string') {
    return date;
  }

  // La data è un oggetto NgbDateStruct
  const dateNgb = date as NgbDateStruct;
  // Parso l'oggetto in un moment
  const dateM = convertNgbDateStructToMoment(dateNgb);

  // Tramite moment creo la data e la ritorno formattata
  return dateM.format(formato);
};

/**
 * Funzione che formatta l'input in data con formato "view".
 * Se la data è già una stringa, viene ritornata senza parsing.
 * @param date string | NgbDateStruct da convertire.
 * @returns string data formattata.
 */
export const convertNgbDateStructToViewDate = (
  date: string | NgbDateStruct
): string => {
  // Verifico che la data esista
  if (!date) {
    return '';
  }
  // Verifico se la data non è già stringa
  if (date && typeof date === 'string') {
    return date;
  }

  // Converto l'oggetto in un moment
  const momentDate: Moment = convertNgbDateStructToMoment(
    date as NgbDateStruct
  );
  // Verifico che il moment sia stato generato
  if (!momentDate || !momentDate.isValid()) {
    // Ritorno stringa vuota
    return '';
  }

  // Moment valido, lo converto con formattazione dedicata
  return momentDate.format(RiscaFormatoDate.view);
};

/**
 * Funzione che formatta l'input in data con formato "server".
 * Se la data è già una stringa, viene ritornata senza parsing.
 * @param date string | NgbDateStruct da convertire.
 * @returns string data formattata.
 */
export const convertNgbDateStructToServerDate = (
  date: string | NgbDateStruct
): string => {
  // Verifico che la data esista
  if (!date) {
    return '';
  }
  // Verifico se la data non è già stringa
  if (date && typeof date === 'string') {
    return date;
  }

  // Converto l'oggetto in un moment
  const momentDate: Moment = convertNgbDateStructToMoment(
    date as NgbDateStruct
  );
  // Verifico che il moment sia stato generato
  if (!momentDate || !momentDate.isValid()) {
    // Ritorno stringa vuota
    return '';
  }

  // Moment valido, lo converto con formattazione dedicata
  return momentDate.format(RiscaFormatoDate.server);
};

/**
 * Funzione di formattazione di una data, da stringa, in una data di destazione (dato i formati di inizio e fine).
 * @param date string data da formattare.
 * @param startFormat string formato della stringa in ingresso.
 * @param targetFormat string formato della stringa in uscita.
 * @returns string data formattata.
 */
export const formatDateFromString = (
  date: string,
  startFormat: string,
  targetFormat: string
): string => {
  // Dichiaro la stringa di destinazione
  let targetDate = '';

  // Verifico che gli input esistano
  if (!date || !startFormat || !targetFormat) {
    return targetDate;
  }

  // Creo un moment partendo dalla stringa d'ingresso
  const dateMoment = moment(date, startFormat);
  // Verifico che la data sia valida
  if (!dateMoment.isValid()) {
    return targetDate;
  }
  // Data valida effettuo la formattazione
  targetDate = dateMoment.format(targetFormat);

  // Ritorno la stringa formattata
  return targetDate;
};

/**
 * Funzione di supporto che verifica la data dal server ed eventualmente forza la formattazione alla versione standard: YYYY-MM-DD.
 * @param serverDate string con la data del server da verificare e normalizzare.
 * @returns string con la data normalizzata per il server.
 */
export const normalizeServerDate = (serverDate: string): string => {
  // Verifico la data
  if (!serverDate || typeof serverDate !== 'string') {
    // Manca la data, ritorno il valore
    return serverDate;
    // #
  }

  // La data è stringa, potrebbe essere una stringa YYYY-MM-DDTHH:mm:ss.sssZ - provo a gestirla
  const isDateMoreThen10Chars: boolean = serverDate.length > 10;
  const isDash4thCh: boolean = serverDate[4] === '-';
  const isDash7thCh: boolean = serverDate[7] === '-';
  // Verifico la posizione dei separatori per anno-mese-giorno e se la stringa è più lunga di 10 caratteri
  if (isDateMoreThen10Chars && isDash4thCh && isDash7thCh) {
    // Ho una data da normalizzare, tronco al 10° carattere
    return serverDate.slice(0, 10);
    // #
  }

  // Caso non gestito, ritorno la stringa
  return serverDate;
};

/**
 * Funzione di parsing data da un formato in input al formato server.
 * @param date string | NgbDateStruct data da convertire.
 * @returns string data convertita.
 */
export const convertViewDateToServerDate = (
  date: string | NgbDateStruct
): string => {
  // Verifico il tipo della data
  if (typeof date !== 'string') {
    // Parso la data da NgbDateStruct a string
    date = convertNgbDateStructToString(date, '/');
  }

  // Aggiorno il formato della data
  const viewFormat = RiscaFormatoDate.view;
  const serverFormat = RiscaFormatoDate.server;

  // Richiamo la funzione di formattazione
  return formatDateFromString(date, viewFormat, serverFormat);
};

/**
 * Funzione di parsing data da un formato server al formato view.
 * @param date string | NgbDateStruct data da convertire.
 * @returns string data convertita.
 */
export const convertServerDateToViewDate = (
  date: string | NgbDateStruct
): string => {
  // Verifico il tipo della data
  if (typeof date !== 'string') {
    // Parso la data da NgbDateStruct a string
    date = convertNgbDateStructToString(date, '/');
  }

  // Aggiorno il formato della data
  const viewFormat = RiscaFormatoDate.view;
  const serverFormat = RiscaFormatoDate.server;

  // Normalizzo la stringa per sicurezza
  const dateNormalized: string = normalizeServerDate(date);

  // Richiamo la funzione di formattazione
  return formatDateFromString(dateNormalized, serverFormat, viewFormat);
};

/**
 * Funzione che effettua il convert da una data string con formato view ad una data NgbDateStruct.
 * @param date string da convertire.
 * @returns NgbDateStruct convertita.
 */
export const convertViewDateToNgbDateStruct = (date: string): NgbDateStruct => {
  // Verifico l'input
  if (!date) {
    return null;
  }

  // Definisco la classe di costanti comuni
  const C_C = new CommonConsts();
  // Creo un moment
  const dateMoment = moment(date, C_C.DATE_FORMAT_VIEW);
  // Ritorno un'oggetto NgbDateStruct
  return {
    year: dateMoment.year(),
    month: dateMoment.month() + 1,
    day: dateMoment.date(),
  } as NgbDateStruct;
};

/**
 * Funzione che effettua il convert da una data string con formato server ad una data NgbDateStruct.
 * @param date string da convertire.
 * @returns NgbDateStruct convertita.
 */
export const convertServerDateToNgbDateStruct = (
  date: string
): NgbDateStruct => {
  // Verifico l'input
  if (!date) {
    return null;
  }

  // Definisco la classe di costanti comuni
  const C_C = new CommonConsts();
  // Normalizzo la stringa per sicurezza
  const dateNormalized: string = normalizeServerDate(date);
  // Creo un moment
  const dateMoment = moment(dateNormalized, C_C.DATE_FORMAT_SERVER);
  // Ritorno un'oggetto NgbDateStruct
  return {
    year: dateMoment.year(),
    month: dateMoment.month() + 1,
    day: dateMoment.date(),
  } as NgbDateStruct;
};

/**
 * Funzione che effettua il convert da una data string con formato server ad un Moment.
 * @param date string da convertire.
 * @returns Moment convertito.
 */
export const convertServerDateToMoment = (date: string): Moment => {
  // Verifico l'input
  if (!date) {
    return null;
  }

  // Definisco la classe di costanti comuni
  const C_C = new CommonConsts();
  // Normalizzo la stringa per sicurezza
  const dateNormalized: string = normalizeServerDate(date);
  // Creo un moment
  return moment(dateNormalized, C_C.DATE_FORMAT_SERVER);
};

/**
 * Funzione che effettua il convert da una data string con formato view ad un Moment.
 * @param date string da convertire.
 * @returns Moment convertito.
 */
export const convertViewDateToMoment = (date: string): Moment => {
  // Verifico l'input
  if (!date) {
    return null;
  }

  // Definisco la classe di costanti comuni
  const C_C = new CommonConsts();
  // Creo un moment
  return moment(date, C_C.DATE_FORMAT_VIEW);
};

/**
 * Funzione che effettua il convert da una data string, compatibile con l'oggetto Date di javascrpit, in un formato Moment.
 * @param date string da convertire.
 * @returns Moment convertito.
 */
export const convertStringDateToMoment = (date: string): Moment => {
  // Verifico l'input
  if (!date) {
    return null;
  }
  // Converto il date in moment
  return moment(new Date(date));
};

/**
 * Funzione che effettua il convert da un moment ad una iso date string.
 * @param dateMoment Moment da convertire.
 * @returns string in formato Date ISO convertito.
 */
export const convertMomentToISOString = (dateMoment: Moment): string => {
  // Verifico l'input
  if (!dateMoment || !dateMoment.isValid()) {
    return '';
  }
  // Converto il moment in iso date string
  return dateMoment.toDate().toISOString();
};

/**
 * Funzione di parsing data da un formato moment al formato view date.
 * @param dateMoment Moment da convertire.
 * @returns string data convertita.
 */
export const convertMomentToViewDate = (dateMoment: Moment): string => {
  // Verifico l'input
  if (!dateMoment || !dateMoment.isValid()) {
    return '';
  }
  // Aggiorno il formato della data
  const viewFormat = RiscaFormatoDate.view;
  // Converto il moment in iso date string
  return dateMoment.format(viewFormat);
};

/**
 * Funzione di parsing data da un formato moment al formato server date.
 * @param dateMoment Moment da convertire.
 * @returns string data convertita.
 */
export const convertMomentToServerDate = (dateMoment: Moment): string => {
  // Verifico l'input
  if (!dateMoment || !dateMoment.isValid()) {
    return '';
  }
  // Aggiorno il formato della data
  const serverFormat = RiscaFormatoDate.server;
  // Converto il moment in iso date string
  return dateMoment.format(serverFormat);
};

/**
 * Funzione di convert da un Moment ad un NgbDateStruct.
 * @param dateMoment Moment da convertire
 * @returns NgbDateStruct convertita.
 */
export const convertMomentToNgbDateStruct = (
  dateMoment: Moment
): NgbDateStruct => {
  // Verifico l'input
  if (!dateMoment) {
    return;
  }

  // Ritorno un oggetto NgbDateStruct
  return {
    year: dateMoment.year(),
    month: dateMoment.month() + 1,
    day: dateMoment.date(),
  };
};

/**
 * Genera un array di anni dalla data iniziale alla data finale
 * @param start Moment data iniziale, primo anno
 * @param end Moment data finale, ultimo anno. Se null, è uguale a start+100 anni
 * @returns number[] elenco di anni dal primo all'ultimo.
 */
export const generateYearsForSelect = (
  start: Moment,
  end: Moment = null
): number[] => {
  if (!end) {
    end = start.add(100, 'years');
  }

  let yearCounter = start.year();
  let maxYear = end.year();

  let yearsArray: number[] = [];

  while (yearCounter <= maxYear) {
    yearsArray.push(yearCounter);
    yearCounter += 1;
  }

  return yearsArray;
};

/**
 * Funzione che gestisce i dati del css, manipolando le informazioni per generare un oggetto compatibile con le direttive NgClass o NgStyle.
 * @param cssType RiscaCssHandlerTypes il tipo di stile per gestire l'output.
 * @param css string o RiscaCss che definisce le informazioni dello stile.
 * @returns string o RiscaCss con la configurazione css.
 */
export const riscaCssHandler = (
  cssType: RiscaCssHandlerTypes,
  css?: RiscaCss
): RiscaCss => {
  // Verifico la configurazione del css
  const cssClass = cssType === RiscaCssHandlerTypes.class;
  const cssStyle = cssType === RiscaCssHandlerTypes.style;
  // Verifico che la classe di stile esista
  if (css === undefined && cssClass) {
    return '';
  }
  if (css === undefined && cssStyle) {
    return {};
  }

  // Verifico i dati del css
  const isCssString = typeof css === 'string';
  const isCssObject = typeof css === 'object';
  // Verifico la validità delle combinazioni
  const cssValid = (isCssString && cssClass) || (isCssObject && cssStyle);

  // Verifico la validità del css
  if (cssValid) {
    return css;
  }
  if (cssClass) {
    return '';
  }
  if (cssStyle) {
    return {};
  }
};

/**
 * Genera un array di anni a partire dalla data start-fromBefore fino all'anno start+tillAfter
 * @param start data di base
 * @param fromBefore quantità prima del base
 * @param tillAfter quatità dopo il base
 * @returns array di anni
 */
export const generateAnni = (
  start: number,
  fromBefore: number = 100,
  tillAfter: number = 100
): IRiscaAnnoSelect[] => {
  // Creo l'array direttamente formato per non doverlo reistanziare quando viene riempito
  const listaAnni = new Array<IRiscaAnnoSelect>(1 + fromBefore + tillAfter);
  // Calcolo i parametri per ciclare
  const annoFine = start + tillAfter;
  let annoCorrente = start - fromBefore;
  // Ciclo
  for (let i = 0; annoCorrente <= annoFine; i++) {
    listaAnni[i] = { anno: annoCorrente };
    annoCorrente++;
  }
  // Ritorno la lista
  return listaAnni;
};

/**
 * Funzione che genera un oggetto per la paginazione con informazioni di default.
 * @param source Array di any con l'array d'informazioni per la paginazione.
 * @returns RiscaTablePagination come oggetti di paginazione di default.
 */
export const defaultPagination = (
  source?: any[],
  sortBy?: string
): RiscaTablePagination => {
  return {
    total: source?.length || 0,
    label: 'Risultati di ricerca',
    elementsForPage: 10,
    showTotal: true,
    currentPage: 1,
    sortBy: sortBy,
    sortDirection: RiscaSortTypes.crescente,
    maxPages: 10,
  };
};

/**
 * Funzione di comodo che definisce le logiche di formattazione dell'output per i typeahead del comune.
 * @param c Comune con le informazioni del comune.
 * @returns string con il nome del comune formattato <NOME_COMUNE> (<SIGLA_PROVINCIA>).
 */
export const typeaheadComuneFormatter = (c: ComuneVo): string => {
  // Recupero il nome del comune
  const cDes = c?.denom_comune ?? '';
  // Recupero la sigla provincia
  const sProv = c?.provincia?.sigla_provincia;
  const spDes = sProv ? `(${sProv})` : '';

  // Definisco la descrizione
  const desc = `${cDes} ${spDes}`.trim();

  // Mappo le informazioni dell'oggetto per valorizzare la input
  return desc;
};

/**
 * Funzione di supporto che definisce le logiche di recupero per l'oggetto recapito collegato all'indirizzo di spedizione.
 * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni dell'indirizzo di spedizione.
 * @param recapiti Array di RecapitoVo con le informazioni relative ai recapiti di gestione. Verrà selezionato in base alle informazioni dell'indirizzo di spedizione.
 * @returns RecapitoVo come oggetto di riferimento per l'indirizzo di spedizione.
 */
export const estraiRecapito = (
  indirizzoSpedizione: IndirizzoSpedizioneVo,
  recapiti: RecapitoVo[]
): RecapitoVo => {
  // Recupero l'id del recapito dall'indirizzo spedizione
  const idRecapito = indirizzoSpedizione?.id_recapito;
  // Ricerco e ritorno il recapito per stesso id
  return recapiti?.find((r: RecapitoVo) => r.id_recapito === idRecapito);
};

/**
 * Funzione di supporto che definisce le logiche di recupero per l'oggetto recapito collegato all'indirizzo di spedizione.
 * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni dell'indirizzo di spedizione.
 * @param gruppi Array di Gruppo con le informazioni relative ai gruppi di gestione. Verrà selezionato in base alle informazioni dell'indirizzo di spedizione.
 * @returns Gruppo come oggetto di riferimento per l'indirizzo di spedizione.
 */
export const estraiGruppo = (
  indirizzoSpedizione: IndirizzoSpedizioneVo,
  gruppi: Gruppo[]
): Gruppo => {
  // Recupero l'id del gruppo dall'indirizzo spedizione
  const idGruppo = indirizzoSpedizione?.id_gruppo_soggetto;
  // Verifico se esiste l'idGruppo (perché se il recapito è del soggetto, questo id non è valorizzato)
  if (idGruppo == undefined) {
    // Nessun dato, ritorno undefined
    return undefined;
  }

  // L'id gruppo esiste, ricerco e ritorno il gruppo per stesso id
  return gruppi?.find((g: Gruppo) => g.id_gruppo_soggetto === idGruppo);
};

/**
 * Funzione di comodo che verifica se un RiscaServerError è composto da multi errori.
 * @param e RiscaServerError da verificare.
 * @returns boolean che specifica se esistono molteplici errori nell'oggetto.
 */
export const riscaServerMultiErrors = (e: RiscaServerError): boolean => {
  // Verifico che esista la proprietà per i multi errori e che siano almeno 1
  return e?.errors && e.errors.length > 0;
};

/**
 * Funzione che esegue la funzione passata come parametro.
 * @param f (v?: any, ...p: any[]) => any da eseguire.
 * @param v any con il valore da passare come parametro alla funzione.
 * @param p Array di any che definisce n possibili parametri passati alla funzione.
 * @returns any come risultato dell'operazione.
 */
export const riscaExecute = (
  f: (v?: any, ...p: any[]) => any,
  v: any,
  ...p: any[]
): any | undefined => {
  // Verifico l'input
  if (!f) {
    return false;
  }

  // Lancio la funzione
  const result = f(v, p);
  // Eseguo la funzione e ritorno il valore
  return result;
};

/**
 * Funzione per la conversione di una stringa in formato Base64 ad un array di Uint8Array.
 * @param b64Data string che definisce un Base64 da convertire.
 * @param sliceSize number che definisce la porzione di byte per la conversione da Base64 ad array di byte.
 * @returns Array di Uint8Array risultato della conversione.
 */
export const base64ToByteArray = (
  b64Data: string,
  sliceSize: number = 512
): Uint8Array[] => {
  // Per sicurezza, metto dentro tutto all'interno di un try catch
  try {
    // Replace per la compatibilità con Internet Explorer
    b64Data = b64Data.replace(/\s/g, '');
    // Converto la stringa in caratteri "byte"
    let byteCharacters = atob(b64Data);
    // Definisco un array che conterrà i byte array dalla tokenizzazione della stringa
    let byteArrays: Uint8Array[] = [];

    // Ciclo le informazioni dei caratteri
    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      // Effettuo uno slice dei dati per il recupero della porzione di byte
      let slice = byteCharacters.slice(offset, offset + sliceSize);
      // Creo un array a suo volta dal pezzo di dati generato
      let byteNumbers = new Array(slice.length);

      // Ciclo i caratteri del pezzo di dati generato
      for (var i = 0; i < slice.length; i++) {
        // Definisco il codice del carattere e lo definisco come byte
        byteNumbers[i] = slice.charCodeAt(i);
      }

      // Converto l'array di byte in un carattere Uint8
      let byteArray = new Uint8Array(byteNumbers);
      // Aggiungo all'array dei bytes finale il risultato della conversione
      byteArrays.push(byteArray);
      // #
    }

    // Ritorno l'array di byte generato
    return byteArrays;
    // #
  } catch (e) {
    // Gestisco l'errore
    return [];
  }
};

/**
 * Funzione per la conversione di una stringa in formato Base64 ad un oggetto Blob.
 * @param b64Data string che definisce un Base64 da convertire.
 * @param contentType string come MIMEType per la conversione del Blob.
 * @param sliceSize number che definisce la porzione di byte per la conversione da Base64 a caratteri byte poi in Blob.
 * @returns Blob risultato della conversione.
 */
export const base64ToBlob = (
  b64Data: string,
  contentType: string = '',
  sliceSize: number = 512
): Blob => {
  // Definisco un array che conterrà i byte array dalla tokenizzazione della stringa
  let byteArrays: Uint8Array[] = base64ToByteArray(b64Data, sliceSize);
  // Converto l'array di byte generato in un blob
  return new Blob(byteArrays, { type: contentType });
};

/**
 * Funzione per la conversione di una stringa in formato URI ad un oggetto Blob.
 * @param dataURI string che definisce un URI da convertire.
 * @param contentType string come MIMEType per la conversione del Blob.
 * @returns Blob risultato della conversione.
 */
export const dataURItoBlob = (
  dataURI: string,
  contentType: string = ''
): Blob => {
  // Converto la stringa
  const byteString = atob(dataURI);
  // Creo un buffer array della lunghezza del bytestring generato
  const arrayBuffer = new ArrayBuffer(byteString.length);
  // Converto l'array in un Uint8Array
  const int8Array = new Uint8Array(arrayBuffer);

  // Ciclo il byte string
  for (let i = 0; i < byteString.length; i++) {
    // Converto il carattere in byte
    int8Array[i] = byteString.charCodeAt(i);
  }

  // Genero il blob dall'array di byte convertito
  const blob = new Blob([int8Array], { type: contentType });

  // Ritorno il blob
  return blob;
};

/**
 * Formatta un importo mettendo una virgola a separare i decimali ed il punto per separare le migliaia.
 * La formattazione troncherà i numeri decimali.
 * @param importo number | string da formattare.
 * @param decimal number con il numero di decimali da mandatenere.
 * @param decimaliNonSignificativi boolean che definisce se, dalla gestione dei decimali, bisogna completare i decimali con gli 0 mancanti (non significativi). Per default è false.
 * @returns string con la formattazione applicata.
 */
export const formatoImportoITA = (
  importo: number | string,
  decimals?: number,
  decimaliNonSignificativi: boolean = false
): string => {
  // Controllo di esistenza
  if (importo !== 0 && (importo == undefined || importo == '')) {
    return '';
  }
  // Verifico il typing
  const isTNumber = typeof importo === 'number';
  const isTString = typeof importo === 'string';
  if (!isTNumber && !isTString) {
    // Tipo dell'input non valido
    return '';
  }

  // Trasformo l'importo in stringa, lo divido tra interi e decimali
  var parts = importo.toString().split('.');
  // Inserisco i punti delle migliaia
  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');

  // Definisco una funzione di comodo univoca per la gestione dati
  const formatIT = (parts: string[]) => parts.join(',');

  // Definisco delle condizioni specifiche e di comodo
  const soloInteri = parts.length == 1;
  const conDecimali =
    parts[1] !== undefined || parts[1] !== null || parts[1] !== '';
  const decNotDef = decimals === undefined || decimals === null;
  const noDecimals = decimals === 0;
  const someDecimals = decimals > 0;

  // Verifico se è richiesta la gestione specifica mettendo tutti i decimali non significativi
  if (decimaliNonSignificativi) {
    // Definisco quanti decimali con '0' devo piazzare
    let nonSignDecimals: number = 0;

    // Verifico la prima condizione
    if (soloInteri || !conDecimali || noDecimals) {
      // Ho solo interi, verifico se sono richiesti decimali da placeholdare
      if (someDecimals) {
        // Assegno tutti i decimali come 0 da definire
        nonSignDecimals = decimals;
        // #
      } else if (decNotDef || noDecimals) {
        // Ritorno solo la parte d'intero del numero
        return `${parts[0]}`;
        // #
      }
    } else if (conDecimali) {
      // Verifico se ci sono meno decimali di quelli richiesti (come number, ogni zero a destra non è definito)
      if (parts[1].length < decimals) {
        // Ho meno decimali di quelli richiesti, definisco gli 0 d'aggiungere
        nonSignDecimals = decimals - parts[1].length;
      }
    }

    // Verifico se ho effettivamente degli 0 d'aggiungere
    if (nonSignDecimals > 0) {
      // Verifico che esista effettivamente la parte dei decimali
      parts[1] = parts[1] ?? '';

      // Per ogni decimale mancante, aggiungo lo 0 ai decimali
      for (let i = 0; i < nonSignDecimals; i++) {
        // Aggiungo uno zero ai decimali
        parts[1] = `${parts[1]}0`;
      }

      // Ritorno l'insieme dei decimali
      return parts.join(',');
    }
  }

  // Verifico la casistica di: soliInteri o con decimali non definiti
  if (soloInteri || !conDecimali || noDecimals) {
    // Ho solo interi, verifico se sono richiesti decimali da placeholdare
    if (decNotDef || someDecimals) {
      // Metto 00 dopo la virgola
      parts.push('00');
      // Ritorno la formattazione
      return formatIT(parts);
      // #
    } else if (noDecimals) {
      // Ritorno solo la parte d'intero del numero
      return `${parts[0]}`;
      // #
    }
  }

  // Verifico che il numero abbia decimali
  if (conDecimali) {
    // se ho i decimali controllo sia di almeno 2 cifre
    if (parts[1].length < 2) {
      // se non lo è, aggiungo uno 0 alla fine
      parts[1] = parts[1] + '0';
      // #
    } else if (parts[1].length > 2 && decimals == undefined) {
      // Recupero solo i primi due decimali
      parts[1] = `${parts[1][0]}${parts[1][1]}`;
      // #
    } else if (parts[1].length > 2 && decimals != undefined) {
      // Definisco la quantità di decimali da mantenere
      let decimalsNubers: number = 0;
      // Verifico se ci sono abbastanza decimali rispetto a quelli richiesti
      if (parts[1].length >= decimals) {
        // Ci sono abbastanza decimali, definisco la quantità come l'input
        decimalsNubers = decimals;
        // #
      } else {
        // Non ci sono abbastanza elementi, mantengo quelli dell'importo
        decimalsNubers = parts[1].length;
        // #
      }

      // Verifico se c'è almeno un decimale da visualizzare
      if (decimalsNubers > 0) {
        // Effettuo una substring sui decimali e mantengo solo quelli richiesti
        parts[1] = `${parts[1].substr(0, decimalsNubers)}`;
        // #
      } else {
        // Non bisogna far vedere i decimali, rimuovo la parte dei decimali dall'array
        parts.splice(1, 1);
      }
    }
  }

  // Unisco di nuovo decimali e interi per comporre la stringa finale
  return parts.join(',');
  // #
};

/**
 * Formatta un importo italiano in un numero compatibile con javascript.
 * @param importo string da formattare.
 * @returns number con il numero correttamente convertito.
 */
export const importoITAToJsFloat = (importo: string): number => {
  // Controllo di esistenza
  if (!importo) {
    return 0;
  }

  // Definisco il contenitore principale per la stringa
  let importoItaJs = importo;
  // Definisco i caratteri da sostituire
  const regExpMigliaia = new RegExp('\\.', 'g');
  const regExpDecIT = new RegExp('\\,', 'g');
  const sepDecimaliJS = '.';

  // Effettuo una sostituzione di tutti i separatori
  importoItaJs = replaceAll(importoItaJs, regExpMigliaia, '');
  importoItaJs = replaceAll(importoItaJs, regExpDecIT, sepDecimaliJS);

  // Generata una stringa "pulita" in formato numerico JS, tento la conversione
  const importoJS: number = parseFloat(importoItaJs);

  // Verifico e ritorno l'importo se effettivamente è stato convertito
  return isNaN(importoJS) ? 0 : importoJS;
  // #
};

/**
 * Forza la formattazione di un importo gestendo il troncamento dei decimali e assicurandosi che sia un numero gestibile.
 * @param importo number | string da formattare.
 * @param decimal number con il numero di decimali da mandatenere.
 * @returns number con l'importo fomattato.
 */
export const forzaFormattazioneImporto = (
  importo: number | string,
  decimals?: number
) => {
  // Ritorno la concatenazione delle funzioni di formattazione
  return importoITAToJsFloat(formatoImportoITA(importo, decimals));
  // #
};

/**
 * Funzione che rimuove i decimali non significativi da un numero formattato IT.
 * @param numeroIT string con il formato dell'importo italiano.
 * @returns string con l'importo senza decimali non significativi.
 */
export const rimuoviDecimaliNonSignificativiIT = (numeroIT: string): string => {
  // Controllo di esistenza
  if (!numeroIT) {
    // Ritorno l'importo vuoto
    return '';
  }

  // Effettuo lo split dell'importo
  const importoSezionato: string[] = numeroIT.split(',');
  // Recupero la parte di interi e decimali dell'importo
  const interi: string = importoSezionato[0];
  let decimali: string = importoSezionato[1];

  // Verifico se ci sono i decimali
  if (!decimali || decimali.length === 0) {
    // Ritorno direttamente l'intero
    return interi;
    // #
  }

  // Ci sono i decimali ne definisco la lunghezza
  const decimaliTotali: number = decimali.length;
  // Ciclo i decimali partendo dal fondo
  for (let i = decimaliTotali - 1; i >= 0 && decimali.length > 0; i--) {
    // Recupero il decimale tramite indice
    const ilDecimale: string = decimali[i];
    // Verifico se il numero è 0
    if (ilDecimale === '0') {
      // Il decimale è uno 0, lo rimuovo dai decimali
      decimali = decimali.slice(0, i);
      // #
    } else {
      // Ho un decimale numerico significativo, interrompo il ciclo
      break;
    }
  }

  // Verifico se sono stati rimossi tutti i decimali
  if (decimali.length === 0) {
    // Sono stati tolti tutti i decimali, ritorno gli interi
    return interi;
    // #
  } else {
    // Ci sono dei decimali, riunisco i numeri del numero
    const numeroITUpd: string = `${interi},${decimali}`;
    // Ritorno il nuovo numero
    return numeroITUpd;
  }
};

/**
 * Funzione che effettua una replaceAll di una stringa all'interno di un'altra stringa.
 * @param stringa string come base per la sostituzione.
 * @param trovaRegExp RegExp con la regular expression da trovare all'interno della stringa principale.
 * @param sostituisci string con la stringa da sostituire alla stringa trovata.
 * @returns string con le sostituzioni effettuate.
 */
export const replaceAll = (
  stringa: string = '',
  trovaRegExp: RegExp,
  sostituisci: string = ''
): string => {
  // Definisco il contenitore per il risultato
  let risultato = '';
  // Metto dentro tutto un try catch per evitare situazioni inattese
  try {
    // La replace all bisogna farla per forza usando replace e regex
    risultato = stringa?.replace(trovaRegExp, sostituisci) ?? '';
    // #
  } catch (e) {}

  // Ritorno il risultato
  return risultato;
};

/**
 * Funzione che costruisce il testo da mostrare in tabella nella colonna Quantità della ricerca pratiche.
 * @param quantitaDa limite sinistro della ricerca.
 * @param quantitaA limite destro della ricerca.
 * @returns stringa formata come "Da -", "Da - a -" o "Fino a -".
 */
export const quantitaDaADescrittiva = (
  quantitaDa: number = null,
  quantitaA: number = null
): string => {
  // Verifico che esistano effettivamente dei valori
  if (quantitaDa == null && quantitaA == null) {
    // Niente informazioni
    return '';
  }

  // Converto i valori numerici in formato importo ITA
  let quantitaDaITA = formatoImportoITA(quantitaDa);
  let quantitaAITA = formatoImportoITA(quantitaA);

  // Definisco la base per la quantità come stringa
  let quantitaString = quantitaDaITA != null ? 'Da ' + quantitaDaITA : 'Fino';
  // Verifico se esiste la quantita A
  if (quantitaAITA) {
    // Esiste, concateno le informazioni
    quantitaString += ' a ' + quantitaAITA;
  }

  // Ritorno la formattazione per la quantità
  return quantitaString;
};

/**
 * Funzione di sanitizzazione di un oggetto dalle proprietà impiegate dal frontend.
 * Per prassi, le proprietà utilizzate solo dal frontend hanno il prefisso '__<proprietà>'.
 * La modifica avverrà per referenza.
 * @param obj any da sanitizzare.
 */
export const sanitizeFEProperties = (obj: any) => {
  // Verifico l'input
  if (!obj) {
    // Ritorno il valore dell'oggetto in input
    return obj;
  }

  // Definisco una piccola funzione di contrllo dei caratteri
  const isFEProperty = (s: string) => {
    // Verifico la stringa
    const includes = s.includes('__');
    const check = includes && s[0] === '_' && s[1] === '_';
    // Ritorno il controllo
    return check;
  };

  // Ciclo le proprietà dell'oggetto
  for (let [property, value] of Object.entries(obj)) {
    // Verifico se la proprietà ha il prefisso fe
    if (isFEProperty(property)) {
      // La proprietà è di fe, la elimino
      delete obj[property];
      // #
    } else {
      // Verifico il tipo e gestisco la proprietà
      if (Array.isArray(value)) {
        // Per ogni elemento effettuo la sanitificazione
        value.forEach((v: any) => {
          sanitizeFEProperties(v);
        });
        // #
      } else if (typeof value === 'object') {
        // Tento la sanitizzazione dell'oggetto
        sanitizeFEProperties(value);
        // #
      }
    }
  }
};

/**
 * Funzione di sanitizzazione di una lista di oggetti dalle proprietà impiegate dal frontend.
 * Per prassi, le proprietà utilizzate solo dal frontend hanno il prefisso '__<proprietà>'.
 * @param list any[] da sanitizzare.
 * @returns any[] con gli oggetti sanitizzati.
 */
export const sanitizeFEPropertiesList = (list: any[]): any[] => {
  // Verifico la lista
  if (!list) {
    // Ritorno array vuoto
    return [];
  }

  // Richiamo la funzione di utility
  return list.map((e: any) => sanitizeFEProperties(e));
};

/**
 * Funzione che verifica se un oggetto è vuoto.
 * @param o any da verificare.
 * @returns boolean che definisce se l'oggetto è vuoto o undefined.
 */
export const isEmptyObject = (o: any): boolean => {
  // Verifico l'input
  if (!o) {
    return true;
  }

  // Verifico se l'oggetto ha proprietà
  return Object.keys(o).length === 0;
};

/**
 * Funzione che verifica se l'oggetto passato in input ha tutte le proprietà undefined o null.
 * @param o any da verificare.
 * @returns boolean con il risultato della verifica.
 */
export const isEmptyDeep = (o: any): boolean => {
  // Verifico l'input
  if (!o) {
    // Lo considero true
    return true;
  }
  // Verifico che sia un oggetto
  if (typeof o !== 'object') {
    // Lo considero true
    return true;
  }

  // Ciclo i valori dell'oggetto
  for (let v of Object.values(o)) {
    // Basta che un valore di una proprietà non sia undefined
    if (v !== undefined && v !== null) {
      // Blocco il ciclo, ritorno false
      return false;
    }
  }

  // Tutte le proprietà sono senza valore
  return true;
};

/**
 * Funzione di comodo che verifica se l'input è un oggetto Date.
 * @param d Date da verificare.
 * @returns boolean che definisce se l'input è una Date.
 */
export const isDate = (d: Date): boolean => {
  // Verifico l'input
  if (!d) {
    // Oggetto undefined
    return false;
  }

  // Verifico l'istanza dell'oggetto
  const isInstanceDate = d instanceof Date;
  const isValidDate = isInstanceDate && !isNaN(d.valueOf());

  // Ritorno se la data è valida
  return isValidDate;
};

/**
 * Funzione di comodo che verifica se un oggetto possiede la proprietà per le date validità.
 * Nello specifico, verrà controllato se esiste la proprietà "data_fine_validita" ed il suo valore.
 * @param o any contenente le informazioni per le date di validità.
 * @returns boolean che definisce se l'oggetto è attualmente valido: non esiste data_fine_validita, oppure la data fine validita è successiva ad "oggi".
 */
export const isDataValiditaAttiva = (o: any): boolean => {
  // Verifico l'oggetto
  if (!o) {
    // Non esiste l'oggetto, consideriamo non valido
    return false;
  }

  // Recupero la proprietà "data_fine_validita"
  let dataFineValidita: any = o.data_fine_validita;
  // Verifico se esiste una data fine validita
  if (!dataFineValidita) {
    // Non esiste data fine validita, consideriamo l'oggetto valido
    return true;
  }

  // Effettuo delle verifiche per ottenere una data in formato moment
  const isTypeString: boolean = typeof dataFineValidita === 'string';
  const isTypeDate: boolean = isDate(dataFineValidita);
  const isMomentValid: boolean = moment(dataFineValidita).isValid();
  // Gestisco l'incrocio dei check
  const case1ConvertMoment = isTypeDate && isMomentValid;
  const case2ConvertMoment = isTypeString && isMomentValid;

  // Verifico le varie condizioni per gestire correttamente la data
  if (case1ConvertMoment || case2ConvertMoment) {
    // E' una Date, la converto in moment
    dataFineValidita = moment(dataFineValidita);
    // #
  }

  // Gestisco in un trycatch la situazione
  try {
    // Definisco una variabile di comodo per oggi
    const today = moment();
    // Verifico che la data di fine validità sia successiva ad oggi
    return dataFineValidita.isAfter(today);
    // #
  } catch (e) {
    // Si è verificato un errore
    console.error('isDataValiditaAttiva - invalid format: ', dataFineValidita);
    // Non è possibile verificare
    return false;
  }
};

/**
 * Funzione di supporto che gestisce la descrizione delle select, mediante logica: se esiste data fine validità, la metto nella descrizione.
 * @param o any contenente le informazioni dell'oggetto.
 * @param p string che definisce il nome della property di riferimento per la descrizione.
 * @returns string con la label da visualizzare nella select.
 */
export const addDesSelectDataFineVal = (o: any, p: string): string => {
  // Verifico l'input
  if (!o || !p) {
    return '';
  }

  // Definisco la label da visualizzare
  let label = o[p] || '';
  // Verifico se l'oggetto ha la proprietà data_fine_validita
  if (o.data_fine_validita) {
    // Normalizzo la stringa per sicurezza
    const dateNormalized: string = normalizeServerDate(o.data_fine_validita);
    // Converto la data server in data view
    const dataView = convertServerDateToViewDate(dateNormalized);
    // Concateno la label con l'extra testo
    label = desSelectDataFineVal(dataView, label);
  }

  // Ritorno la label
  return label;
};

/**
 * Funzione di supporto che aggiunge la descrizione di default per la gestione delle option della select.
 * @param d string che definisce la data da inserire nella label.
 * @param p string che definisce il prefisso della label da generare.
 * @param s string che definisce il suffisso della label da generare.
 * @returns string con la label da visualizzare nella select.
 */
export const desSelectDataFineVal = (
  d: string,
  p?: string,
  s?: string
): string => {
  // Verifico l'input
  p = p ? p : '';
  s = s ? s : '';

  // Definisco la label da visualizzare
  let label = `${p} (Fine val. ${d}) ${s}`;
  // Rimuovo gli spazi bianchi
  label = label.trim();

  // Ritorno la label
  return label;
};

/**
 * Funzione di supporto che gestisce l'etichetta riferita allo stato dello stato debitorio secondo il flag annullato.
 * @param sd StatoDebitorioVo con le informazioni per il check sullo stato.
 * @returns string con l'etichetta per la definizione dello stato in base al flag annullato.
 */
export const statoSDFlagAnnullato = (sd: StatoDebitorioVo): string => {
  // Verifico l'input
  if (!sd) {
    // Non c'è configurazione
    return StatiSDFlagAnnullato.sconosciuto;
  }

  // Recupero il flag annullato
  const annullato = sd.flg_annullato;

  // Verifico se non è settato
  if (annullato == undefined) {
    // Ritorno come stato sconosciuto
    return StatiSDFlagAnnullato.sconosciuto;
  }

  // Verifico e ritorno la label in base allo stato annullato
  return annullato
    ? StatiSDFlagAnnullato.annullato
    : StatiSDFlagAnnullato.attivo;
};

/**
 * Funzione di comodo che effettua il troncamento di un numero ad una data cifra decimale. Se non definita una cifra decimale, tutti i decimali verranno rimossi.
 * ATTENZIONE: Il js è un po' strano e il truncate fatto con "toFixed" a volte può fare degli scherzi. Quindi potrebbe tornare valori inattesi.
 * @param n number da troncare.
 * @param decimals number che definisce a quale cifra decimale troncare. Per default è: 2.
 * @returns number troncato.
 */
export const mathTruncate = (n: number, decimals: number = 2): number => {
  // Verifico l'input
  if (!n) {
    // Niente numero, ritorno 0
    return 0;
  }

  // Verifico il decimal precision
  if (decimals == undefined || decimals < 0) {
    // Reimposto il decimal a 1
    decimals = 0;
  }

  // Per sicurezza wrappo tutto in un trycatch
  try {
    // LA MATEMATICA JS E I FLOAT NON VANNO MOLTO D'ACCORDO. Verifico se i decimali sono 0
    if (decimals == 0) {
      // Uso il MATH per troncare completamente i decimali senza arrotondamenti
      const truncINT = Math.trunc(n);
      // Ritorno il valore troncato
      return truncINT;
      // #
    } else {
      // Uso il MATH per calcolare il valore troncato
      const truncFLT = parseFloat(
        (Math.floor((n + Number.EPSILON) * 100) / 100).toFixed(decimals)
      );
      // Ritorno il valore troncato
      return truncFLT;
    }
    // #
  } catch (e) {
    // Come errore ritorno -1
    return -1;
  }
};

/**
 * Funzione che permette di aggiornare tutte le proprietà di un oggetto, con le stesse proprietà di un altro oggetto (dello stesso tipo) in input.
 * La funzione lavorerà sul riferimento dell'oggetto base
 * @param base T che definisce l'oggetto sulla quale aggiornare le informazioni.
 * @param update T | Partial<T> che definisce l'oggetto con le informazioni d'aggiornare.
 */
export const mergeDataWith = function <T>(base: T, update: T | Partial<T>) {
  // Verifico l'input
  if (!base && !update) {
    // Non ci sono elementi
    return undefined;
    // #
  } else if (base && !update) {
    // Ritorno solo l'oggetto base
    return base;
    // #
  } else if (!base && update) {
    // Ritorno solo l'oggetto aggiornato
    return update;
    // #
  }

  // Itero tutte le proprietà dell'oggetto d'aggiornamento
  for (let [property, value] of Object.entries(update)) {
    // Aggiorno nell'oggetto originale la proprietà estratta
    base[property] = value;
  }
};

/**
 * Funzione che converte determinate codifiche all'interno di una stringa in maniera tale da convertirle in tag HTML.
 * @param stringHTML string che identifica il testo che verrà convertito tramite funzione InnerHTML.
 * @returns string con la stringa formattate gestibile tramite HTML.
 */
export const parseInnerHTML = (stringHTML: string): string => {
  // Verifico la stringa
  if (stringHTML == undefined) {
    // Ritorno stringa vuota per default
    return '';
  }

  // Instanzio la classe di supporto che mappa le informazioni per la conversione
  const riscaInnerHTML = new RiscaInnerHTML();
  // Itero tutte le regole definite e applico i replace
  return riscaInnerHTML.applicaRegole(stringHTML);
};

/**
 * Funzione che compara i dati di due paginazioni diverse.
 * A seconda che siano uguali o differenti nei dati, ritorna un valore di check.
 * @param newPag RiscaTablePagination con la nuova paginazione.
 * @param oldPag RiscaTablePagination con la vecchia paginazione.
 * @returns true se la paginazione è cambiata
 */
export const samePaginazioni = (
  newPag: RiscaTablePagination,
  oldPag: RiscaTablePagination
): boolean => {
  // Verifico gli oggetti sulla base dell'esistenza
  if (!newPag && !oldPag) {
    // Entrambi undefined
    return true;
    // #
  } else if ((newPag && !oldPag) || (!newPag && oldPag)) {
    // Un oggetto esiste e l'altro no
    return false;
  }

  // Variabil di comodo
  const sameCP = newPag?.currentPage == oldPag?.currentPage;
  const sameSB = newPag?.sortBy == oldPag?.sortBy;
  const sameSD = newPag?.sortDirection == oldPag?.sortDirection;
  const sameE4P = newPag?.elementsForPage == oldPag?.elementsForPage;
  const sameTE = newPag?.total == oldPag?.total;

  // Se è cambiato anche un solo elemento, la paginazione è cambiata
  return sameCP && sameSB && sameSD && sameE4P && sameTE;
};

/**
 * Aggiorna la paginazione impostando i nuovi parametri generati dal servizio di ricerca.
 * @param paginazione RiscaTablePagination con l'oggetto di paginazione d'aggiornare.
 * @param newPaginazione RiscaTablePagination con il nuovo oggetto di paginazione.
 */
export const updatePaginazione = (
  paginazione: RiscaTablePagination = null,
  newPaginazione: RiscaTablePagination = null
) => {
  // Riassegna i dati della paginazione se richiesto
  if (!newPaginazione) {
    // Non esiste una configurazione
    return;
  }

  // Controllo se è cambiata la paginazione
  const cambiata = !samePaginazioni(paginazione, newPaginazione);
  // Controllo
  if (cambiata) {
    // Verifico se esiste una paginazione locale
    if (paginazione) {
      // Aggiorno i campi della paginazione
      paginazione.total = newPaginazione.total;
      paginazione.currentPage = newPaginazione.currentPage;
      paginazione.sortBy = newPaginazione.sortBy;
      paginazione.sortDirection = newPaginazione.sortDirection;
    } else {
      // Assegno direttamente il nuovo oggetto di paginazione
      paginazione = newPaginazione;
    }
  }
};

/**
 * Funzione che effettua un troncamento con ellipsis ad una stringa.
 * @param input string sulla quale effettuare il troncamento.
 * @param truncAt number con la quantità di caratteri alla quale effettuare il troncamento.
 * @returns string con il risultato del troncamento con ellipsis.
 */
export const ellipsisAt = (input: string, truncAt: number): string => {
  // Verifico i dati in input
  const inputOK = input && input.length >= 2;
  const truncOK = truncAt && truncAt >= 1;
  // Controllo i check
  if (!inputOK || !truncOK) {
    // Ritorno l'input
    return input;
  }

  // Definisco la configurazione per il troncamento della stringa
  const options = {
    length: truncAt,
    separator: ' ',
  };

  // Lancio il troncamento e ritorno il risultato
  return truncate(input, options);
};

/**
 * Funzione di supporto che converte, gestisce e formatta una label per la descrizione da una data inizio ad una data fine.
 * @param da NgbDateStruct con la data inizio da convertire in label.
 * @param a NgbDateStruct con la data fine da convertire in label.
 * @returns string con la label generata.
 */
export const da_a_labelNgbDateStruct = (
  da: NgbDateStruct,
  a: NgbDateStruct
): string => {
  // Effettuo il parse degli oggetti a stringa
  const dataDa: Moment = convertNgbDateStructToMoment(da);
  const dataA: Moment = convertNgbDateStructToMoment(a);

  // Richiamo la funzione di conversione
  return da_a_labelMoment(dataDa, dataA);
};

/**
 * Funzione di supporto che converte, gestisce e formatta una label per la descrizione da una data inizio ad una data fine.
 * @param da Moment con la data inizio da convertire in label.
 * @param a Moment con la data fine da convertire in label.
 * @returns string con la label generata.
 */
export const da_a_labelMoment = (da: Moment, a: Moment): string => {
  // Effettuo il parse degli oggetti a stringa
  const dataDa = convertMomentToViewDate(da);
  const dataA = convertMomentToViewDate(a);
  // Verifico e costruisco la descrizione per la tabella per le date
  const desDa = dataDa != null && dataDa != '' ? `da ${dataDa}` : '';
  const desA = dataA != null && dataA != '' ? `a ${dataA}` : '';

  // Definisco le date per l'oggetto della tabella
  return `${desDa} ${desA}`.trim();
};

/**
 * Funzione di arrotondamento per eccesso di un numero.
 * Se non definiti, i decimali sono 0.
 * @param n number con il numero d'arrotondare.
 * @param d number con i decimali da arrotondare.
 */
export const arrotondaEccesso = (n: number, d: number = 0): number => {
  // Richiamo la funzione di lodash ed effettuo l'arrotondamento
  return round(n, d);
};

/**
 * Funzione di arrotondamento per eccesso di un numero.
 * Se non definiti, i decimali sono 0.
 * @param n number | string con il numero d'arrotondare.
 * @param d number con i decimali da arrotondare.
 */
export const arrotondaDifetto = (n: number | string, d: number = 0): number => {
  // Verifico l'input
  if (n == undefined) {
    // Ritorno undefined
    return undefined;
  }
  // Verifico il tipo dell'input
  if (typeof n === 'number') {
    // Riassegno n come stringa
    n = n.toString();
  }

  // Richiamo la funzione di lodash ed effettuo l'arrotondamento
  return Number(Number.parseFloat(n).toFixed(d));
};

/**
 * Funzione che "pulisce" un filename da possibili path posizionali sul server.
 * @param filename string con il filename da gestire.
 * @param charSeparator string carattere custom come indicatore d'inizio del filename. Per default è "/".
 * @returns string con il filename pulito.
 */
export const clearFileName = (
  filename: string,
  charSeparator?: string
): string => {
  // Verifico l'input
  if (!filename) {
    // Ritorno stringa vuota
    return '';
  }

  // Definisco il carattere come indicatore per l'inizio del nome del file
  charSeparator = charSeparator ?? '/';

  // Definisco l'indice posizionale del carattere nel namefile
  const lastIndex: number = filename.lastIndexOf(charSeparator);
  // Verifico se è stato trovato il carattere
  if (lastIndex !== -1) {
    // Indice trovato, recupero la substring
    let filenameClear: string = filename.substring(lastIndex + 1);
    // Ritorno il filename pulito
    return filenameClear;
    // #
  } else {
    // Ritorno la stringa
    return filename;
  }
};

/**
 * Funzione di comodo che recupera le informazioni da un evento "incolla" dell'utente.
 * @param event ClipboardEvent con l'evento generato dall'utente.
 * @returns any con il valore che l'utente sta incollando.
 */
export const getValoreEventoIncolla = (event: ClipboardEvent): any => {
  // Recupero i dati incollati
  const clipboardData = event.clipboardData;
  let pastedData = clipboardData.getData('text/plain');
  // Ritorno le informazioni
  return pastedData;
};

/**
 * Funzione di comodo che gestisce le logiche di sanitizzazione di un numero di telefono per RISCA.
 * @param numero string con il valore da sanitizzare.
 * @param allowChar string[] con la lista dei caratteri ammessi per il numero di telefono.
 * @returns string con il numero sanitizzato.
 */
export const riscaPhoneSanitizer = (
  numero: string,
  allowChar?: string[]
): any => {
  // Verifico l'input
  numero = numero ?? '';
  // Verifico la tipizzazione
  if (typeof numero === 'number') {
    // Creo una variabile di appoggio
    const numNumero = numero as number;
    // Converto l'informazione in stringa
    numero = numNumero.toString();
    // #
  }
  if (typeof numero !== 'string') {
    // Per qualche motivo l'input ha un formato errato, forzo la stringa
    numero = '';
    // #
  }

  // Ho il numero di telefono, definisco i caratteri permessi
  allowChar = allowChar ?? ['+', '-', '/', ' ', '.', '(', ')', '[', ']'];
  // Creo una regexp per gestire i caratteri da sanitizzare, mantenendo quelli permessi
  const sanitizeChar = new RegExp(`[^\\d${allowChar.join('\\')}]`, 'g');
  const sanitizeEmptiesSpaces = new RegExp('\\s+', 'g');
  // Sanitizzo il numero di telefono dai caratteri indesiderati e dagli spazi bianchi doppi o più
  const numeroSanitizzato = numero
    .replace(sanitizeEmptiesSpaces, ' ')
    .replace(sanitizeChar, '');

  // Rimuovo possibili spazi bianchi ad inizio/fine numero
  return numeroSanitizzato.trim();
};
