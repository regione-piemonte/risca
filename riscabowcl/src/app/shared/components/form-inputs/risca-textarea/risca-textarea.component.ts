import { Component, forwardRef, Input, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormInputsService } from '../../../services/form-inputs/form-inputs.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaFormInputText } from '../../../utilities';
import { FormInputComponent } from '../form-input/form-input.component';

@Component({
  selector: 'risca-textarea',
  templateUrl: './risca-textarea.component.html',
  styleUrls: ['./risca-textarea.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RiscaTextareaComponent),
      multi: true,
    },
  ],
})
export class RiscaTextareaComponent
  extends FormInputComponent<RiscaFormInputText>
  implements OnInit, ControlValueAccessor
{
  /** Funzione richiamata all'evento di blur della input. */
  @Input() onBlur: (value: string) => {};

  /** RiscaFormInputText che definisce le configurazioni per i dati della input. */
  dataConfig: RiscaFormInputText;

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
    // Verifico se è stata definita una funzione per il blur
    if (this.onBlur !== undefined) {
      // Eseguo la funzione di blur passando il value
      this.onBlur(this.value);
    } else {
      // Lancio il change del valore
      this.onChange(this.value);
    }

    // Effettuo il trim automatico
    this.trimValue();
    // Applico la trasformazione del testo
    this.transformText();
  }
}
