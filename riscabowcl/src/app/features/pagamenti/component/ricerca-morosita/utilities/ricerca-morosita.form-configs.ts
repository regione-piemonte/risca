import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { RiscaFormConfigurator } from '../../../../../shared/services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RicercaMorositaConsts } from './ricerca-morosita.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RicercaMorositaFormConfigs extends RiscaFormConfigurator {
  /** RicercaMorositaConsts come classe contenente le costanti associate al componente. */
  private RRM_C = new RicercaMorositaConsts();

  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.RRM_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys RicercaMorositaConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: RicercaMorositaConsts) {
    // Estraggo le chiavi dalla classe
    const { ATTIVITA_MOROSITA_SD } = keys;

    // Definisco per l'oggetto di configurazione chiavi e proprietà
    this.formConfigs[ATTIVITA_MOROSITA_SD] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required] }
    );
  }
}
