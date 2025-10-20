import { AfterViewInit, Component, ElementRef, forwardRef, OnInit, ViewChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormInputsService } from '../../../services/form-inputs/form-inputs.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaFormInputSearch } from '../../../utilities';
import { FormInputComponent } from '../form-input/form-input.component';

@Component({
  selector: 'risca-search',
  templateUrl: './risca-search.component.html',
  styleUrls: ['./risca-search.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RiscaSearchComponent),
      multi: true,
    },
  ],
})
export class RiscaSearchComponent
  extends FormInputComponent<RiscaFormInputSearch>
  implements OnInit, ControlValueAccessor
{
  /** ViewChild che definisce il collegamento tra l'input del DOM e il componente. */
  @ViewChild('riscaSearch') riscaSearch: ElementRef;

  /** RiscaFormInputSearch che definisce le configurazioni per i dati della input. */
  dataConfig: RiscaFormInputSearch;

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
    // Verifico che la modalità di aggiornamento sia blur
    if (!this.inputUpdOnBlur) {
      // Non configurato, non gestisco il valore
      return;
    }

    // Richiamo la gestione del valore input
    this.handleValue();
  }

  /**
   * Funzione agganciata all'evento change della input.
   */
  onInputChange() {
    // Verifico che la modalità di aggiornamento sia change
    if (!this.inputUpdOnChange) {
      // Non configurato, non gestisco il valore
      return;
    }

    // Richiamo la gestione del valore input
    this.handleValue();
  }

  /**
   * Funzione che gestisce il valore della input e la sua emissione.
   */
  private handleValue() {
    // Effettuo il trim automatico
    this.trimValue();
    // Applico la trasformazione del testo
    this.transformText();
    // Lancio il change del valore
    this.onChange(this.value);
  }

  /**
   * Funzione che gestisce l'evento di: keydown.enter.
   * @param event KeyboardEvent contenente le informazioni dell'evento della tastiera.
   * @param element HTMLElement contenente le informazioni dell'oggetto a cui è collegata la funzione.
   * @override
   */
  onKeydownEnter(event: KeyboardEvent, element?: HTMLElement) {
    // Verifico la configurazione
    if (this.keydownEnter) {
      // Triggero il blur dell'elemento
      if (element) {
        // Lancio il blur del campo
        element?.blur();
        // #
      } else {
        // Richiamo manualmente il blur
        this.onInputBlur();
      }
      // Emetto l'evento di onInvio
      this.onInvio.emit(this.value);
    }
  }
}
