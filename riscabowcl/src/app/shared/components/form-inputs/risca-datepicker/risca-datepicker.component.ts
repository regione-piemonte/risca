import { Component, forwardRef, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import {
  NgbDateParserFormatter,
  NgbDateStruct,
} from '@ng-bootstrap/ng-bootstrap';
import { Moment } from 'moment';
import { NgbDateCustomParserFormatter } from '../../../../core/commons/class/dateformat';
import { FormInputsService } from '../../../services/form-inputs/form-inputs.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaFormInputDatepicker } from '../../../utilities';
import { FormInputComponent } from '../form-input/form-input.component';

@Component({
  selector: 'risca-datepicker',
  templateUrl: './risca-datepicker.component.html',
  styleUrls: ['./risca-datepicker.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RiscaDatepickerComponent),
      multi: true,
    },
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class RiscaDatepickerComponent
  extends FormInputComponent<RiscaFormInputDatepicker>
  implements OnInit, ControlValueAccessor
{
  /** RiscaFormInputDatepicker che definisce le configurazioni per i dati della input. */
  dataConfig: RiscaFormInputDatepicker;

  /**
   * Costruttore.
   */
  constructor(
    formInputs: FormInputsService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(formInputs, riscaUtilities);
  }

  ngOnInit() {
    // Verifico che esistano i dati input richiamando il super
    super.ngOnInit();
  }

  onDateSelect() {
    // Emetto il cambio del valore
    this.onChange(this._value);
  }

  /**
   * Funzione di reset dati per il calendario.
   */
  resetCalendario() {
    // Resetto il valore
    this._value = null;
    // Emetto il cambio del valore
    this.onChange(this._value);
  }

  /**
   * Funzione invocata al blur del campo.
   */
  onDatepickerBlur() {
    // Recupero il valore presente nella input
    const data: NgbDateStruct = this.value;
    // Verifico che esista effettivamente una data
    if (data == undefined) {
      // Ritorno direttamente il valore
      this.onChange(data);
      // Interrompo il flusso logico
      return;
    }
    // Verifico se il convertitore non è riuscito a gestire l'input come data
    if (typeof data === 'string') {
      // La data non è stata convertita correttamente, resetto il dato
      this.resetCalendario();
      // Interrompo il flusso logico
      return;
    }

    // Converto la data generata in un moment
    let dataMoment: Moment;
    dataMoment = this._riscaUtilities.convertNgbDateStructToMoment(data);
    // Recupero possibili minimi/massimi
    const minDateMoment: Moment = this.minDateMoment;
    const maxDateMoment: Moment = this.maxDateMoment;

    // Effettuo delle verifiche a step
    if (minDateMoment && dataMoment.isBefore(minDateMoment)) {
      // La data è precedente al limite minimo
      this.resetCalendario();
      // #
    } else if (maxDateMoment && dataMoment.isAfter(maxDateMoment)) {
      // La data è successiva al limite massimo
      this.resetCalendario();
      // #
    } else {
      // Controlli passati, emetto il valore
      this.onChange(data);
    }
  }

  /**
   * Getter di comodo per la configurazione: minDate.
   * @returns NgbDateStruct con il valore della configurazione.
   */
  get minDate(): NgbDateStruct {
    // Tento di ritornare la configurazione
    return this.dataConfig?.minDate;
  }

  /**
   * Getter di comodo per la configurazione: maxDate.
   * @returns NgbDateStruct con il valore della configurazione.
   */
  get maxDate(): NgbDateStruct {
    // Tento di ritornare la configurazione
    return this.dataConfig?.maxDate;
  }

  /**
   * Getter di comodo per la configurazione: minDate.
   * @returns Moment con il valore della configurazione.
   */
  get minDateMoment(): Moment {
    // Tento di ritornare la configurazione
    return this._riscaUtilities.convertNgbDateStructToMoment(this.minDate);
  }

  /**
   * Getter di comodo per la configurazione: maxDate.
   * @returns Moment con il valore della configurazione.
   */
  get maxDateMoment(): Moment {
    // Tento di ritornare la configurazione
    return this._riscaUtilities.convertNgbDateStructToMoment(this.maxDate);
  }
}
