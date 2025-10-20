import { Component, forwardRef, Input, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormInputsService } from '../../../services/form-inputs/form-inputs.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { IRiscaRadioData, RiscaFormInputRadio } from '../../../utilities';
import { FormInputComponent } from '../form-input/form-input.component';

@Component({
  selector: 'risca-radio',
  templateUrl: './risca-radio.component.html',
  styleUrls: ['./risca-radio.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RiscaRadioComponent),
      multi: true,
    },
  ],
})
export class RiscaRadioComponent
  extends FormInputComponent<RiscaFormInputRadio>
  implements OnInit, ControlValueAccessor
{
  /** Lista di oggetti IRiscaRadioData che contiene il pool di dati da visualizzare come voci. */
  @Input('dataSource') source: IRiscaRadioData[] = [];
  /** String che definisce il nome della proprietà da visualizzare come label del radio. */
  @Input() propertyToShow: string = 'label';

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
    // Aggiungo una classe di stile alla label
    this.cssConfig.customLabel = `${this.cssConfig.customLabel} form-check-label`;

    // Verifico che esistano i dati input
    if (!this.source) {
      throw new Error('source not defined');
    }
    if (!this.propertyToShow) {
      throw new Error('propertyToShow not defined');
    }
  }

  /**
   * Funzione collegata al change del radio.
   */
  onRadioChange() {
    // Emetto l'evento di change
    this.onChange(this.value);
  }
}
