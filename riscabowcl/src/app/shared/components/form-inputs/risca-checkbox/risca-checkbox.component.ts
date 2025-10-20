import { Component, forwardRef, Input, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormInputsService } from '../../../services/form-inputs/form-inputs.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { IRiscaCheckboxData, RiscaFormInputCheckbox } from '../../../utilities';
import { FormInputComponent } from '../form-input/form-input.component';

@Component({
  selector: 'risca-checkbox',
  templateUrl: './risca-checkbox.component.html',
  styleUrls: ['./risca-checkbox.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RiscaCheckboxComponent),
      multi: true,
    },
  ],
})
export class RiscaCheckboxComponent
  extends FormInputComponent<RiscaFormInputCheckbox>
  implements OnInit, ControlValueAccessor
{
  /** Lista di oggetti IRiscaCheckboxData che contiene il pool di dati da visualizzare come voci. */
  @Input('dataSource') source: IRiscaCheckboxData[] = [];
  /** String che definisce il nome della proprietà da visualizzare come label del radio. */
  @Input() propertyToShow: string = 'label';
  /** Boolean che pilota il cambiamento del dato ritornando solo l'ultimo oggetto che ha subito l'evento "change". Questo può risultare comodo nel caso in cui ci sia solo una checkbox. */
  @Input('onlyLastValue') lastValue = false;

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

    // Verifico che esistano i dati input del componente
    this.initComponent();
    // Gestisco la label del componente
    this.handleLabel();
    // Lancio il primo aggiornamento per allineare il FormGroup
    this.onCheckboxChange(this.source[0]);
  }

  /**
   * Funzione di init delle informazioni del componente.
   */
  private initComponent() {
    // Verifico che esistano i dati input
    if (!this.source || this.source.length === 0) {
      throw new Error('source not defined');
    }
    if (!this.propertyToShow) {
      throw new Error('propertyToShow not defined');
    }
  }

  /**
   * Funzione di init della label del componente.
   */
  private handleLabel() {
    // Aggiungo una classe di stile alla label
    this.cssConfig.customLabel = `${this.cssConfig.customLabel} form-check-label`;
  }

  /**
   * Funzione collegata al change della checkbox.
   * @param checkbox IRiscaCheckboxData con la checkbox modificata.
   */
  onCheckboxChange(checkbox: IRiscaCheckboxData) {
    // Verifico se è richiesto solo l'ultimo valore
    if (this.lastValue) {
      // Emetto l'evento di change per l'ultimo valore
      this.onChange(checkbox);
      // #
    } else {
      // Ritorno i valori
      this.onChange(this.source);
    }
  }
}
