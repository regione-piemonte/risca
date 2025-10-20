import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { FormInputsService } from '../../../services/form-inputs/form-inputs.service';
import { MappaErroriFormECodici, RiscaFormInputCss } from '../../../utilities';

@Component({
  selector: 'risca-form-group-error',
  templateUrl: './risca-form-group-error.component.html',
  styleUrls: ['./risca-form-group-error.component.scss'],
})
export class RiscaFormGroupErrorComponent implements OnInit {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();

  /** Input che definisce le configurazioni per gli stili della input. */
  @Input() cssConfig: RiscaFormInputCss;

  /** FormGroup a cui fa riferimento il componente. */
  @Input() formGroup: FormGroup;
  /** Lista di oggetti MappaErroriFormECodici contenente la lista degli errori che devono essere gestiti per il form control. */
  @Input('errMapFormGroup') mappaErroriFG: MappaErroriFormECodici[] = [];
  /** Boolean che tiene traccia dello stato di Submit del form padre. */
  @Input() formSubmitted: boolean = false;

  /** Boolean che definisce se abilitare la modalità di debug. */
  @Input() debug = false;

  constructor(private _formInputs: FormInputsService) {}

  ngOnInit() {
    // Verifico che esistano i dati input
    if (!this.formGroup) {
      throw new Error('formGroup not defined');
    }
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di comodo per il recupero della casistica di errore sul form.
   * @returns boolean con il check di presenza di errori nel form group.
   */
  get hasFormErrors(): boolean {
    // Lancio la funzione del servizio
    return this._formInputs.getFormErrors(this.formGroup, this.mappaErroriFG);
  }

  /**
   * Getter che recupera l'oggetto degli errori dal form group.
   * @returns any con l'oggetto degli errori generato dal form group.
   */
  get formErrors(): any {
    // Tento di recuperare l'oggetto degli errori del form
    return this.formGroup?.errors;
  }
}
