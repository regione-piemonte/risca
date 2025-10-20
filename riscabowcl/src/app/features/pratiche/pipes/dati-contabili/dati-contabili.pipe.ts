import { Pipe, PipeTransform } from '@angular/core';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { RiscaDatiContabili } from '../../../../shared/utilities';
import { DatiContabiliUtilityService } from '../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';

/**
 * Pipe che verifica se uno stato debitorio è stato selezionato all'interno della scheda "stati debitori" della pratica.
 * Lo stato debitorio selezionato si basa su quello scelto dalla tabella mediante radio button.
 */
@Pipe({ name: 'disableBtnSD' })
export class DisabilitaBtnStatiDebitoriPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _datiContabiliUtility: DatiContabiliUtilityService) {}

  /**
   * Funzione che verifica la configurazione in input dello stato debitorio e definisce se disabilitare i pulsanti per la schermata degli stati debitori.
   * @param statoDebitorio StatoDebitorioVo che definisce l'oggetto dello stato debitorio selezionato dalla tabella.
   * @param sezione RiscaDatiContabili che definisce la sezione a cui è collegato il pulsante.
   * @returns boolean che definisce se il pulsante specificato dall'input è disabilitato.
   */
  transform(
    statoDebitorio: StatoDebitorioVo,
    sezione: RiscaDatiContabili
  ): boolean {
    // Richiamo la funzione di check del servizio di utility
    const abilitato = this._datiContabiliUtility.checkNavigazione(
      sezione,
      statoDebitorio
    );
    // Inverto il valore del check
    return !abilitato;
  }
}
