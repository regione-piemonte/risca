import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { RiscaFormConfigurator } from '../../../../services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaRicercaMorositaConsts } from './risca-ricerca-morosita.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RiscaRicercaMorositaFormConfigs extends RiscaFormConfigurator {
  /** RiscaRicercaMorositaConsts come classe contenente le costanti associate al componente. */
  private RRM_C = new RiscaRicercaMorositaConsts();

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
   * @param keys RiscaRicercaMorositaConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: RiscaRicercaMorositaConsts) {
    // Estraggo le chiavi dalla classe
    const {
      TIPO_RICERCA_MOROSITA: STATO_MOROSITA,
      ANNO,
      RESTITUITO_MITTENTE,
      ANNULLATO,
      LIMITE_INVIO_ACCERTAMENTO,
    } = keys;

    // Definisco per l'oggetto di configurazione chiavi e proprietà
    this.formConfigs[STATO_MOROSITA] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required] }
    );
    this.formConfigs[ANNO] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[RESTITUITO_MITTENTE] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[ANNULLATO] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[LIMITE_INVIO_ACCERTAMENTO] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
  }
}
