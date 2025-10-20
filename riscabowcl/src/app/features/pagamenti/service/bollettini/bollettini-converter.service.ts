import { Injectable } from '@angular/core';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import {
  ITipoElaborazioneVo,
  TipoElaborazioneVo,
} from '../../../../core/commons/vo/tipo-elaborazione-vo';
import {
  IRicercaBollettini,
  IRicercaElaborazioni,
} from '../../../../shared/utilities';

@Injectable()
export class BollettiniConverterService {
  /**
   * Costruttore.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * #########################################################################
   * FUNZIONALITA' DI CONVERSIONE DA IRicercaBollettini a IRicercaElaborazioni
   * #########################################################################
   */

  /**
   * Funzione che converte un oggetto IRicercaBollettini in IRicercaElaborazioni.
   * @param rb IRicercaBollettini da convertire.
   * @returns IRicercaElaborazioni convertito.
   */
  convertIRicercaBollettiniToIRicercaElaborazioni(
    rb: IRicercaBollettini
  ): IRicercaElaborazioni {
    // Variabili di comodo
    let stato = rb.stato;
    let tipo: ITipoElaborazioneVo;
    let drDa: string;
    let drA: string;

    // Converto il tipo elaborazione
    tipo = rb.tipo?.toServerFormat();
    // Converto la data di richiesta DA
    drDa = this.convertNgbDateStructToServerDate(rb.dataRichiestaInizio);
    // Converto dataRichiestaFine
    drA = this.convertNgbDateStructToServerDate(rb.dataRichiestaFine);

    // Costruisco l'oggetto finale
    const result: IRicercaElaborazioni = {
      tipo,
      stato,
      dataRichiestaInizio: drDa,
      dataRichiestaFine: drA,
    };

    return result;
  }

  /**
   * #########################################################################
   * FUNZIONALITA' DI CONVERSIONE DA IRicercaElaborazioni a IRicercaBollettini
   * #########################################################################
   */

  /**
   * Funzione che converte un oggetto IRicercaElaborazioni in IRicercaBollettini.
   * @param re IRicercaElaborazioni da convertire.
   * @returns IRicercaBollettini convertito.
   */
  convertIRicercaElaborazioniToIRicercaBollettini(
    re: IRicercaElaborazioni
  ): IRicercaBollettini {
    // Converto lo stato
    const stato = re.stato;
    // Converto il tipo
    const tipo = new TipoElaborazioneVo(re.tipo);
    // Converto dataRichiestaInizio
    const dataRichiestaInizio =
      this._riscaUtilities.convertServerDateToNgbDateStruct(
        re.dataRichiestaInizio
      );
    // Converto dataRichiestaFine
    const dataRichiestaFine =
      this._riscaUtilities.convertServerDateToNgbDateStruct(
        re.dataRichiestaFine
      );

    // Costruisco l'oggetto finale
    const result: IRicercaBollettini = {
      tipo,
      stato,
      dataRichiestaInizio: dataRichiestaInizio,
      dataRichiestaFine: dataRichiestaFine,
    };

    return result;
  }

  /**
   * ##################
   * FUNZIONI DI COMODO
   * ##################
   */

  /**
   * Funzione di comodo che richiama la funzione che formatta l'input in data con formato "server".
   * Se la data è già una stringa, viene ritornata senza parsing.
   * @param date string | NgbDateStruct da convertire.
   * @returns string data formattata.
   */
  convertNgbDateStructToServerDate(date: NgbDateStruct): string {
    // Ritorno il risultato della funzione
    return this._riscaUtilities.convertNgbDateStructToServerDate(date);
  }
}
