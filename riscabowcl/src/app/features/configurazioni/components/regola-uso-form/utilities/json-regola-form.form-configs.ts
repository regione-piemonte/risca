import { FormBuilder, FormControl } from '@angular/forms';
import { numberCompositionValidator } from '../../../../../shared/miscellaneous/forms-validators/forms-validators';
import { RiscaFormConfigurator } from '../../../../../shared/services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaErrorKeys } from '../../../../../shared/utilities/classes/errors-keys';
import { JsonRegolaFormConsts } from './json-regola-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class JsonRegolaFormFormConfigs extends RiscaFormConfigurator {
  /** JsonRegolaFormConsts come classe che definisce le costanti del componente. */
  JRF_C = new JsonRegolaFormConsts();

  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.JRF_C);
    // Lancio la funzione di setup per i validatori
    this.setFormValidators(this.JRF_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys JsonRegolaFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: JsonRegolaFormConsts) {
    // Estraggo le chiavi dalla classe
    const { MINIMO_PRINCIPALE } = keys;

    this.formConfigs[MINIMO_PRINCIPALE] = new FormControl(
      { value: null, disabled: false },
      { validators: [numberCompositionValidator(7, 2)] }
    );
  }

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys JsonRegolaFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormValidators(keys: JsonRegolaFormConsts) {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;
    // Assegno i validatori
    this.formValidators = [];
  }
}
