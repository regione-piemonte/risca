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
import { mathTruncate } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaFormInputNumber } from '../../../utilities';
import { FormInputComponent } from '../form-input/form-input.component';

@Component({
  selector: 'risca-number',
  templateUrl: './risca-number.component.html',
  styleUrls: ['./risca-number.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RiscaNumberComponent),
      multi: true,
    },
  ],
})
export class RiscaNumberComponent
  extends FormInputComponent<RiscaFormInputNumber>
  implements OnInit, ControlValueAccessor
{
  /** Funzione richiamata all'evento di blur della input. */
  @Input() onBlur: (value: string) => {};
  /** Boolean che permette di configurare lo stepper per l'input numerica. */
  @Input() stepper: boolean = false;
  /** number che permette di configurare i decimali del dato. */
  @Input() decimals: number;
  /** boolean che permette di configurare il troncamento. */
  @Input() truncate: boolean;
  /** boolean che permette di configurare l'arrotondamento del dato. */
  @Input() round: boolean;

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
    // Gestisco il valore
    this.value = this.transformValue(this.value);

    // Verifico se è stata definita una funzione per il blur
    if (this.onBlur !== undefined) {
      // Eseguo la funzione di blur passando il value
      this.onBlur(this.value);
    } else {
      // Lancio il change del valore
      this.onChange(this.value);
    }

    // Evito che la input converta il campo vuoto (null) come 0
    if (this.value === null) {
      // Imposto il value ad undefined
      this.value = undefined;
    }
  }

  /**
   * Funzione che applica delle regole per la gestione del valore in output.
   * @param value number con il valore da gestire.
   */
  private transformValue(value: number): number {
    // Assegno localmente il valore
    const v: number = value;
    // Verifico se esiste un valore
    if (v == undefined) {
      // Nessun valore
      return v;
    }

    // Recupero il numero di decimali
    const decimals = this.decimals;
    // Verifico se sono stati configurati decimali specifici
    if (decimals == undefined) {
      // Nessuna configurazione ritorno il valore
      return v;
    }

    // Verifico se c'è da effettuare un troncamento
    if (this.truncate) {
      // Tronco l'informazione
      return mathTruncate(v, this.decimals);
    }
    // Verifico se c'è d'arrotondare

    if (this.round) {
      // Faccio l'arrotondamento del dato
      return Number(v.toFixed(this.decimals));
    }

    // Ritorno il valore
    return v;
  }
}
