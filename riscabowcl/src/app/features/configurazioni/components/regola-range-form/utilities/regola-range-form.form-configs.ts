import {
  FormBuilder,
  FormControl,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import {
  numberCompositionValidator,
  oneFieldEvaluetedValidator,
  minMaxNumberValidator,
} from '../../../../../shared/miscellaneous/forms-validators/forms-validators';
import { RiscaFormConfigurator } from '../../../../../shared/services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaErrorKeys } from '../../../../../shared/utilities/classes/errors-keys';
import { JsonRegolaRangeFormats } from '../../../services/configurazioni/utilities/configurazioni.consts';
import { RegolaRangeFormConsts } from './regola-range-form.consts';
import { RiscaNotifyCodes } from '../../../../../shared/utilities/enums/risca-notify-codes.enums';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RegolaRangeFormFormConfigs extends RiscaFormConfigurator {
  /** RegolaRangeFormConsts come classe che definisce le costanti del componente. */
  RRF_C = new RegolaRangeFormConsts();
  /** JsonRegolaRangeFormats con le costanti per la gestione del formato dati dei JsonRegolaRangeVo. */
  JRRF_C = new JsonRegolaRangeFormats();

  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.RRF_C);
    // Lancio la funzione di setup per i validatori
    this.setFormValidators(this.RRF_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys RegolaRangeFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: RegolaRangeFormConsts) {
    // Estraggo le chiavi dalla classe
    const { VALORE_1, VALORE_2, CANONE_MINIMO } = keys;

    this.formConfigs[VALORE_1] = new FormControl(
      { value: null, disabled: false },
      {
        validators: [
          numberCompositionValidator(
            this.JRRF_C.INT_SOGLIA_MIN,
            this.JRRF_C.FLOAT_SOGLIA_MIN
          ),
        ],
      }
    );

    this.formConfigs[VALORE_2] = new FormControl(
      { value: null, disabled: false },
      {
        validators: [
          numberCompositionValidator(
            this.JRRF_C.INT_SOGLIA_MAX,
            this.JRRF_C.FLOAT_SOGLIA_MAX
          ),
        ],
      }
    );

    this.formConfigs[CANONE_MINIMO] = new FormControl(
      { value: null, disabled: false },
      {
        validators: [
          Validators.required,
          numberCompositionValidator(
            this.JRRF_C.INT_CANONE_MINIMO,
            this.JRRF_C.FLOAT_CANONE_MINIMO
          ),
        ],
      }
    );
  }

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys RegolaRangeFormConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormValidators(keys: RegolaRangeFormConsts) {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Validatore per i campi "minimi"
    let almenoUnValoreDefinito: ValidatorFn;
    almenoUnValoreDefinito = oneFieldEvaluetedValidator(
      [keys.VALORE_1, keys.VALORE_2],
      ERR_KEY.CAMPI_OBBLIGATORI
    );

    // Validatore per il minimo/massimo per Valore1 < Valore2
    let valore1MinValore2: ValidatorFn;
    valore1MinValore2 = minMaxNumberValidator(
      keys.VALORE_1,
      keys.VALORE_2,
      true,
      ERR_KEY.RANGE_VALORE1_GREAT_VALORE2
    );

    // Assegno i validatori
    this.formValidators = [almenoUnValoreDefinito, valore1MinValore2];
  }
}
