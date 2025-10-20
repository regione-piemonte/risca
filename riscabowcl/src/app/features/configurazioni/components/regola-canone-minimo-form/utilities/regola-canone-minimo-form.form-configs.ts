import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { numberInRangeValidator, numberCompositionValidator } from '../../../../../shared/miscellaneous/forms-validators/forms-validators';
import { RiscaFormConfigurator } from '../../../../../shared/services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaErrorKeys } from '../../../../../shared/utilities/classes/errors-keys';
import { RegolaCanoneMinimoFormConsts } from './regola-canone-minimo-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RegolaCanoneMinimoFormFormConfigs extends RiscaFormConfigurator {
  /** RegolaCanoneMinimoFormConsts come classe che definisce le costanti del componente. */
  RCMF_C = new RegolaCanoneMinimoFormConsts();

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
   * @param keys RegolaCanoneMinimoFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: RegolaCanoneMinimoFormConsts) {
    // Estraggo le chiavi dalla classe
    const { CANONE_MINIMO } = keys;

    this.formConfigs[CANONE_MINIMO] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required, numberCompositionValidator(7, 2)] }
    );
  }

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys RegolaCanoneMinimoFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormValidators(keys: RegolaCanoneMinimoFormConsts) {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Assegno i validatori
    this.formValidators = [];
  }
}
