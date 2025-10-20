import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { RiscaFormConfigurator } from '../../../../../shared/services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaErrorKeys } from '../../../../../shared/utilities/classes/errors-keys';
import { CreaRegolaUsoFormConsts } from './crea-regola-uso-form.consts';
import { numberInRangeValidator, numberCompositionValidator } from '../../../../../shared/miscellaneous/forms-validators/forms-validators';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class CreaRegolaUsoFormFormConfigs extends RiscaFormConfigurator {
  /** CreaRegolaUsoFormConsts come classe che definisce le costanti del componente. */
  CRUF_C = new CreaRegolaUsoFormConsts();

  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.CRUF_C);
    // Lancio la funzione di setup per i validatori
    this.setFormValidators(this.CRUF_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys CreaRegolaUsoFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: CreaRegolaUsoFormConsts) {
    // Estraggo le chiavi dalla classe
    const { ANNUALITA, PERCENTUALE_AGGIORNAMENTO } = keys;

    this.formConfigs[ANNUALITA] = new FormControl(
      { value: null, disabled: true },
      { validators: [Validators.required, numberInRangeValidator(1000, 9999)] }
    );

    this.formConfigs[PERCENTUALE_AGGIORNAMENTO] = new FormControl(
      { value: null, disabled: false },
      { validators: [numberCompositionValidator(3, 4)] }
    );
  }

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys CreaRegolaUsoFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormValidators(keys: CreaRegolaUsoFormConsts) {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Assegno i validatori
    this.formValidators = [];
  }
}
