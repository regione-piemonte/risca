import {
  FormBuilder,
  FormControl,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { calendarioNgbInizioFineValidator } from '../../../../../shared/miscellaneous/forms-validators/forms-validators';
import { RiscaFormConfigurator } from '../../../../../shared/services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaErrorKeys } from '../../../../../shared/utilities/classes/errors-keys';
import { RicercaEsportaDatiConsts } from './ricerca-esporta-dati.consts';
import { dataFineStessoAnnoDataInizio } from '../../../../../shared/miscellaneous/forms-validators/esporta-dati/form-validators.ed';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RicercaEsportaDatiFormConfigs extends RiscaFormConfigurator {
  /** RicercaEsportaDatiConsts come classe contenente le costanti associate al componente. */
  private RED_C = new RicercaEsportaDatiConsts();

  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.RED_C);
    // Lancio la funzione di setup per i validatori
    this.setFormValidators(this.RED_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys RicercaEsportaDatiConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: RicercaEsportaDatiConsts) {
    // Estraggo le chiavi dalla classe
    const { DATA_DA, DATA_A, TIPO_ELABORA_REPORT } = keys;

    // Definisco per l'oggetto di configurazione chiavi e proprietà
    this.formConfigs[DATA_DA] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required] }
    );
    this.formConfigs[DATA_A] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required] }
    );
    this.formConfigs[TIPO_ELABORA_REPORT] = new FormControl(
      { value: null, disabled: false },
      { validators: [Validators.required] }
    );
  }

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys RicercaEsportaDatiConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormValidators(keys: RicercaEsportaDatiConsts) {
    // Recupero le chiavi
    const dtDa = keys.DATA_DA;
    const dtA = keys.DATA_A;
    // Definisco la chiave custom per la gestione dell'errore
    const customE = RiscaErrorKeys.DATE_START_AFTER_END_EXPORT_DATA;
    // Definisco il validatore per le date
    let dateValidator: ValidatorFn;
    dateValidator = calendarioNgbInizioFineValidator(dtDa, dtA, customE);
    // Definisco il validatore per stesso anno
    let yearValidator: ValidatorFn;
    yearValidator = dataFineStessoAnnoDataInizio(dtDa, dtA);

    // Assegno i validatori
    this.formValidators = [dateValidator, yearValidator];
  }
}
