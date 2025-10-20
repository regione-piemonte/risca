import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { RiscaFormConfigurator } from '../../../../services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaRicercaRimborsiConsts } from './risca-ricerca-rimborsi.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RiscaRicercaRimborsiFormConfigs extends RiscaFormConfigurator {
  /** RiscaRicercaRimborsiConsts come classe contenente le costanti associate al componente. */
  private RRR_C = new RiscaRicercaRimborsiConsts();

  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.RRR_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys RiscaRicercaRimborsiConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: RiscaRicercaRimborsiConsts) {
    // Estraggo le chiavi dalla classe
    const {
      TIPO_RICERCA_RIMBORSI: STATO_RIMBORSO,
      ANNO,
    } = keys;

    // Definisco per l'oggetto di configurazione chiavi e proprietà
    this.formConfigs[STATO_RIMBORSO] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required] }
    );
    this.formConfigs[ANNO] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
  }
}
