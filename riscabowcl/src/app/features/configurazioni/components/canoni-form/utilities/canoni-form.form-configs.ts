import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { RiscaFormConfigurator } from '../../../../../shared/services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaErrorKeys } from '../../../../../shared/utilities/classes/errors-keys';
import { CanoniFormConsts } from './canoni-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class CanoniFormFormConfigs extends RiscaFormConfigurator {
  /** CanoniFormConsts come classe contenente le costanti associate al componente. */
  private CF_C = new CanoniFormConsts();

  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.CF_C);
    // Lancio la funzione di setup per i validatori
    this.setFormValidators(this.CF_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys CanoniFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: CanoniFormConsts) {
    // Estraggo le chiavi dalla classe
    const { CANONE_UFFICIALE } = keys;

    this.formConfigs[CANONE_UFFICIALE] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required] }
    );
  }

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys CanoniFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormValidators(keys: CanoniFormConsts) {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Assegno i validatori
    this.formValidators = [];
  }
}
