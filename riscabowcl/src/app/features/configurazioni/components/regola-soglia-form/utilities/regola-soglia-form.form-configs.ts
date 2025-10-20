import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { numberCompositionValidator } from '../../../../../shared/miscellaneous/forms-validators/forms-validators';
import { RiscaFormConfigurator } from '../../../../../shared/services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaErrorKeys } from '../../../../../shared/utilities/classes/errors-keys';
import { RegolaSogliaFormConsts } from './regola-soglia-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RegolaSogliaFormFormConfigs extends RiscaFormConfigurator {
  /** RegolaSogliaFormConsts come classe che definisce le costanti del componente. */
  RCMF_C = new RegolaSogliaFormConsts();

  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.RCMF_C);
    // Lancio la funzione di setup per i validatori
    this.setFormValidators(this.RCMF_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys RegolaSogliaFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: RegolaSogliaFormConsts) {
    // Estraggo le chiavi dalla classe
    const { SOGLIA, MINIMO_INFERIORE, MINIMO_SUPERIORE } = keys;

    this.formConfigs[SOGLIA] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required, numberCompositionValidator(5, 4)] }
    );

    this.formConfigs[MINIMO_INFERIORE] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required, numberCompositionValidator(7, 2)] }
    );

    this.formConfigs[MINIMO_SUPERIORE] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required, numberCompositionValidator(7, 2)] }
    );
  }

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys RegolaSogliaFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormValidators(keys: RegolaSogliaFormConsts) {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Assegno i validatori
    this.formValidators = [];
  }
}
