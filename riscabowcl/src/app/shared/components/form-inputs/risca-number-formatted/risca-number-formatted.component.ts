import {
  Component,
  ElementRef,
  forwardRef,
  Input,
  OnInit,
  ViewChild,
} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormInputsService } from '../../../services/form-inputs/form-inputs.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaFormInputNumber } from '../../../utilities';
import { FormInputComponent } from '../form-input/form-input.component';

@Component({
  selector: 'risca-number-formatted',
  templateUrl: './risca-number-formatted.component.html',
  styleUrls: ['./risca-number-formatted.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RiscaNumberFormattedComponent),
      multi: true,
    },
  ],
})
export class RiscaNumberFormattedComponent
  extends FormInputComponent<RiscaFormInputNumber>
  implements OnInit, ControlValueAccessor
{
  /** Number che definisce il numero di decimali da visualizzare. Per default è 2. */
  @Input() decimals: number = 2;
  /** Funzione richiamata all'evento di blur della input. */
  @Input() onBlur: (value: string) => {};

  /** ViewChild che definisce il collegamento tra l'input del DOM e il componente. */
  @ViewChild('riscaNumber') riscaNumber: ElementRef;

  /** RiscaFormInputNumber che definisce le configurazioni per i dati della input. */
  dataConfig: RiscaFormInputNumber;

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

  /**
   * Funzione agganciata all'evento blur della input.
   */
  onInputBlur() {
    // Logiche di Blur
  }

  /**
   * #########################################
   * INTERFACCE PER GESTIONE DEL FORM CONTROL
   * #########################################
   */

  /**
   * Funzione implementata dall'interfaccia ControlValueAccessor per il cambio del valore dal Form padre.
   * NOTA: sovrascrivo l'interfaccia di default per poter manipolare il valore impostato nella input.
   * @param value number con il nuovo valore.
   */
  writeValue(value: number) {
    // Formatto il valore numerico in una stringa formattata
    const v = this._riscaUtilities.formatoImportoITA(value, this.decimals, true);
    // Assegno locamente il valore
    this._value = v;
  }
}
