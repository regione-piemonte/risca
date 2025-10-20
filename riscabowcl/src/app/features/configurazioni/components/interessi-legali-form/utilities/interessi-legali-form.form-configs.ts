import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { RiscaFormConfigurator } from '../../../../../shared/services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaErrorKeys } from '../../../../../shared/utilities/classes/errors-keys';
import { InteressiLegaliFormConsts } from './interessi-legali-form.consts';
import { numberCompositionValidator } from '../../../../../shared/miscellaneous/forms-validators/forms-validators';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class InteressiLegaliFormFormConfigs extends RiscaFormConfigurator {
  /** InteressiLegaliFormConsts come classe contenente le costanti associate al componente. */
  private ILF_C = new InteressiLegaliFormConsts();
  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.ILF_C);
    // Lancio la funzione di setup per i validatori
    this.setFormValidators(this.ILF_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys InteressiLegaliFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: InteressiLegaliFormConsts) {
    // Estraggo le chiavi dalla classe
    const { PERCENTUALE, DATA_INIZIO, DATA_FINE, GIORNI } = keys;

    this.formConfigs[PERCENTUALE] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required, numberCompositionValidator(3, 4)] }
    );
    this.formConfigs[DATA_INIZIO] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required] }
    );
    this.formConfigs[DATA_FINE] = new FormControl(
      { value: null, disabled: true },
      { validators: [Validators.required] }
    );
    this.formConfigs[GIORNI] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required, Validators.min(1)] }
    );
  }

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys InteressiLegaliFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormValidators(keys: InteressiLegaliFormConsts) {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Assegno i validatori
    this.formValidators = [];
  }
}
