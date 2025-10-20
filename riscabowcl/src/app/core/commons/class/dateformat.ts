import { Injectable } from '@angular/core';
import {
  NgbDateParserFormatter,
  NgbDateStruct,
} from '@ng-bootstrap/ng-bootstrap';
import { Moment } from 'moment';
import {
  convertMomentToNgbDateStruct,
  convertViewDateToMoment,
  convertNgbDateStructToViewDate,
} from '../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaRegExp } from '../../../shared/utilities';

@Injectable()
export class NgbDateCustomParserFormatter extends NgbDateParserFormatter {
  /** RiscaRegExp con le regular expression da usare per le verifiche. */
  private riscaRegExp = new RiscaRegExp();

  /**
   * Funzione che cerca di convertire l'input in un oggetto NgbDateStruct.
   * La funzione cercherà di validare la input come view date, convertendola poi in un oggetto NgbDateStruct.
   * @param data string con il valore che l'utente sta digitando nella input.
   * @returns NgbDateStruct con la data convertita.
   */
  parse(data: string): NgbDateStruct {
    // Verifico la data in input
    if (!data) {
      // Non è definita, ritorno null
      return null;
    }
    // Verifico se la data è formattata correttamente
    if (!this.riscaRegExp.viewDate.test(data)) {
      // Non è una data formattata valida
      return null;
    }

    // Tento di convertire la stringa in input in un oggetto Moment
    let dataMoment: Moment;
    dataMoment = convertViewDateToMoment(data);

    // Verifico se il moment è valido
    if (dataMoment.isValid()) {
      // Il moment risulta valido, trasformo il moment in un NgbDateStruct
      let dataNgbDS: NgbDateStruct;
      dataNgbDS = convertMomentToNgbDateStruct(dataMoment);
      // Ritorno l'oggetto convertito
      return dataNgbDS;
      // #
    } else {
      // Data non valida, ritorno null
      return null;
    }
  }

  /**
   * Funzione che cerca di convertire l'input in una stringa.
   * La funzione cercherà di validare la input come NgbDateStruct, convertendola poi in una stringa.
   * @param data NgbDateStruct con il valore che l'utente ha selezionato dal calendario.
   * @returns string con la data convertita.
   */
  format(data: NgbDateStruct): string {
    // Verifico la data in input
    if (!data) {
      // Non è definita, ritorno null
      return null;
    }

    // Lancio la funzione di conversione dell'oggetto in view date
    const dataView: string = convertNgbDateStructToViewDate(data);
    // Ritorno la data convertita
    return dataView;
  }
}
