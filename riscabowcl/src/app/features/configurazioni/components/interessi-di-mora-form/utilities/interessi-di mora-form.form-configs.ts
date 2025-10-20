import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { numberCompositionValidator } from '../../../../../shared/miscellaneous/forms-validators/forms-validators';
import { RiscaFormConfigurator } from '../../../../../shared/services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaErrorKeys } from '../../../../../shared/utilities/classes/errors-keys';
import { InteressiDiMoraFormConsts } from './interessi-di-mora-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class InteressiDiMoraFormFormConfigs extends RiscaFormConfigurator {
  /** InteressiDiMoraFormConsts come classe contenente le costanti associate al componente. */
  private IDMF_C = new InteressiDiMoraFormConsts();

  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.IDMF_C);
    // Lancio la funzione di setup per i validatori
    this.setFormValidators(this.IDMF_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys InteressiDiMoraFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: InteressiDiMoraFormConsts) {
    // Estraggo le chiavi dalla classe
    const { PERCENTUALE, DATA_INIZIO, DATA_FINE } = keys;

    this.formConfigs[PERCENTUALE] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required, numberCompositionValidator(3, 4)] }
    );
    this.formConfigs[DATA_INIZIO] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required] }
    );
    this.formConfigs[DATA_FINE] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
  }

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys InteressiDiMoraFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormValidators(keys: InteressiDiMoraFormConsts) {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Assegno i validatori
    this.formValidators = [];
  }
}
