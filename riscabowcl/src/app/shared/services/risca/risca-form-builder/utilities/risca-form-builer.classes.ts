import {
  AsyncValidatorFn,
  FormBuilder,
  FormGroup,
  ValidatorFn,
} from '@angular/forms';
import { IRiscaFormConfigs } from './risca-form-builer.interfaces';
import { FormGroupOptions, RiscaFormConfigs } from './risca-form-builer.types';

/**
 * Classe che definisce le informazioni minime per la configurazione automatica di un form group.
 */
export class RiscaFormConfigurator implements IRiscaFormConfigs {
  /** RiscaFormConfigs che definisce la configurazione per i campi del form group. */
  formConfigs: RiscaFormConfigs;
  /** ValidatorFn | ValidatorFn[] che definisce la funzioni di validazione del form group. */
  formValidators: ValidatorFn | ValidatorFn[];
  /** AsyncValidatorFn | AsyncValidatorFn[] che definisce la funzioni asincrone di validazione del form group. */
  formAsyncValidators: AsyncValidatorFn | AsyncValidatorFn[];

  /**
   * Costruttore.
   */
  constructor(protected _formBuilder: FormBuilder) {
    // Inizializzazione della classe
    this.formConfigs = {};
    this.formValidators = [];
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys any che definisce la struttura dalla quale estrarre le chiavi per i campi.
   */
  protected setFormConfigs(keys: any) {}

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys any che definisce la struttura dalla quale estrarre le chiavi per i campi.
   */
  protected setFormValidators(keys: any) {}

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli asincroni per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys any che definisce la struttura dalla quale estrarre le chiavi per i campi.
   */
  protected setFormAsyncValidators(keys: any) {}

  /**
   * Funzione che effettua genera un'istanza di FormGroup.
   * @param formConfigs RiscaFormConfigs con una configurazione specifica per il form. Se non definito, viene recuperata la configurazione della classe.
   * @param formValidators ValidatorFn | ValidatorFn[] con una configurazione specifica per i validatori del form. Se non definito, viene recuperata la configurazione della classe.
   * @param formAsyncValidators AsyncValidatorFn | AsyncValidatorFn[] con una configurazione specifica per i validatori asincroni del form. Se non definito, viene recuperata la configurazione della classe.
   * @returns FormGroup con l'istanza del form da utilizzare.
   */
  instantiateForm(
    formConfigs?: RiscaFormConfigs,
    formValidators?: ValidatorFn | ValidatorFn[],
    formAsyncValidators?: AsyncValidatorFn | AsyncValidatorFn[]
  ): FormGroup {
    // Verifico l'input
    const fc = formConfigs ?? this.formConfigs;
    const fv = formValidators ?? this.formValidators;
    const fav = formAsyncValidators ?? this.formAsyncValidators;
    // Definisco la configurazione come options
    const o: FormGroupOptions = {
      validators: fv,
      asyncValidators: fav,
    };

    // Creo e ritorno un oggetto form group
    return this._formBuilder.group(fc, o);
  }
}
