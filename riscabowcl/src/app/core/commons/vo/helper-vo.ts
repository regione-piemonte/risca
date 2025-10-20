import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Moment } from 'moment';
import { DynamicObjAny } from 'src/app/shared/utilities';
import {
  convertBooleanToServerBooleanNumber,
  convertMomentToServerDate,
  convertNgbDateStructToMoment,
  convertServerBooleanNumberToBoolean,
  convertServerDateToMoment,
  sanitizeFEProperties,
  sanitizeFEPropertiesList,
} from '../../../shared/services/risca/risca-utilities/risca-utilities.functions';

/**
 * Classe di supporto per la gestione degli oggetti Vo ritornati dal server.
 * @version SONARQUBE_22_04_24 Rimosso costruttore vuoto.
 */
export class HelperVo /* implements IServerMetadata */ {
  /**
   * Funzione di comodo per la conversione di una data string server in un moment.
   * @param serverDate string con la data da convertire.
   * @returns Moment con la conversione del dato.
   */
  protected convertServerDateToMoment(serverDate: string): Moment {
    // Verifico l'input
    if (!serverDate) {
      // Niente da convertire
      return undefined;
    }

    // Converto la data
    const momentDate = convertServerDateToMoment(serverDate);
    // Verifico e ritorno la data
    return momentDate.isValid() ? momentDate : undefined;
  }

  /**
   * Funzione di comodo per la conversione di una data moment in string server.
   * @param momentDate Moment con la data da convertire.
   * @returns string con la conversione del dato.
   */
  protected convertMomentToServerDate(momentDate: Moment): string {
    // Verifico l'input
    if (
      !momentDate ||
      typeof momentDate !== 'object' ||
      !momentDate.isValid()
    ) {
      // Niente da convertire
      return '';
    }

    // Converto la data
    const serverDate = convertMomentToServerDate(momentDate);
    // Verifico e ritorno la data
    return serverDate;
  }

  /**
   * Funzione che formatta l'input in data string.
   * @param date NgbDateStruct da convertire.
   * @returns string data formattata.
   */
  convertNgbDateStructToMoment(date: NgbDateStruct): Moment {
    // Verifico che la data esista
    if (!date) {
      return undefined;
    }
    // Richiamo la funzione di conversione
    return convertNgbDateStructToMoment(date);
  }

  
  /**
   *  Funzione che converte un oggetto Moment in un oggetto NgbDateStruct.
   * @param momentDate 
   * @returns 
   */
   momentToNgbDateStruct(momentDate: moment.Moment): NgbDateStruct {
    return {
      year: momentDate.year(),
      month: momentDate.month() + 1, // I mesi in moment.js sono indicizzati a partire da 0
      day: momentDate.date()
    };
  }


  /**
   * Funzione di comodo per la conversione di un booleano server (number) in un booleano puro (boolean).
   * Se undefined, viene considerato false.
   * @param serverBool number che definisce il booleano del server da convertire.
   * @returns boolean risultato della conversione.
   */
  protected convertServerBoolNumToBoolean(serverBool: number): boolean {
    // Verifico l'input
    if (serverBool == undefined) {
      // Niente dato
      return false;
    }

    // Lancio la conversione del flag tramite utility
    return convertServerBooleanNumberToBoolean(serverBool);
  }

  /**
   * Funzione di comodo per la conversione di un booleano puro (boolean) in un booleano server (number).
   * Se undefined, viene considerato false.
   * @param pureBool number che definisce il booleano del server da convertire.
   * @returns boolean risultato della conversione.
   */
  protected convertBooleanToServerBoolNum(pureBool: boolean): number {
    // Verifico l'input
    if (pureBool == undefined) {
      // Niente dato
      return 0;
    }

    // Lancio la conversione del flag tramite utility
    return convertBooleanToServerBooleanNumber(pureBool);
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @param extras any con un oggetto di configurazione generico.
   * Necessario @override della classe che estende HelperVo.
   */
  toServerFormat(extras?: any): DynamicObjAny {
    // Se non viene fatto l'override o non trova nulla, deve tornare undefined.
    return undefined;
  }

  /**
   * Funzione di comodo, che permette di trasformare un qualunque oggetto HelperVo o che estende HelperVo nella sua form "server like".
   * @param o HelperVo da convertire.
   * @returns DynamicObjAny con l'oggetto convertito
   */
  objToServerFormat(o: HelperVo): DynamicObjAny {
    // Lancio la conversione
    return o?.toServerFormat() ?? o;
  }

  /**
   * Funzione di comodo, che permette di trasformare una qualunque lista di oggetti HelperVo o che estende HelperVo nella sua form "server like".
   * @param list Array<T extends HelperVo> da convertire.
   * @returns DynamicObjAny[] con gli oggetti convertiti.
   */
  listToServerFormat<T extends HelperVo>(list: Array<T>): DynamicObjAny[] {
    // Verifico l'input
    if (!list) {
      // Niente da convertire
      return undefined;
    }

    // Converto la lista di oggetti
    return list.map((e: T) => {
      // Ritorno l'oggetto
      return e.toServerFormat();
    });
  }

  /**
   * Funzione di sanitizzazione di un oggetto dalle proprietà impiegate dal frontend.
   * Per prassi, le proprietà utilizzate solo dal frontend hanno il prefisso '__<proprietà>'.
   * La modifica avverrà per referenza.
   * @param obj any da sanitizzare.
   */
  sanitizeFEProperties(obj: any) {
    // Richiamo la funzione di utility
    sanitizeFEProperties(obj);
  }

  /**
   * Funzione di sanitizzazione di una lista di oggetti dalle proprietà impiegate dal frontend.
   * Per prassi, le proprietà utilizzate solo dal frontend hanno il prefisso '__<proprietà>'.
   * @param list any[] da sanitizzare.
   * @returns any[] con gli oggetti sanitizzati.
   */
  sanitizeFEPropertiesList(list: any[]) {
    // Richiamo la funzione di utility
    return sanitizeFEPropertiesList(list);
  }
}
