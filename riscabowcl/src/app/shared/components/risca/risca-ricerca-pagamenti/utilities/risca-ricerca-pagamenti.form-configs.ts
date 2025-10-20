import { FormBuilder, FormControl, ValidatorFn } from '@angular/forms';
import {
  calendarioNgbInizioFineValidator,
  minMaxNumberValidator,
  oneFieldEvaluetedValidator,
} from '../../../../miscellaneous/forms-validators/forms-validators';
import { RiscaFormConfigurator } from '../../../../services/risca/risca-form-builder/utilities/risca-form-builer.classes';
import { RiscaErrorKeys } from '../../../../utilities/classes/errors-keys';
import { RiscaRicercaPagamentiConsts } from './risca-ricerca-pagamenti.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RiscaRicercaPagamentiFormConfigs extends RiscaFormConfigurator {
  /** RiscaRicercaPagamentiConsts come classe contenente le costanti associate al componente. */
  private RRP_C = new RiscaRicercaPagamentiConsts();

  /**
   * Costruttore.
   */
  constructor(formBuilder: FormBuilder) {
    // Richiamo il super
    super(formBuilder);
    // Lancio la funzione di setup dell'oggetto di configurazione
    this.setFormConfigs(this.RRP_C);
    // Lancio la funzione di setup per i validatori
    this.setFormValidators(this.RRP_C);
  }

  /**
   * Funzione che definisce le logiche di popolazione dell'oggetto per la configurazione dei campi del form.
   * La funzione è pensata per l'override.
   * @param keys RiscaRicercaPagamentiConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormConfigs(keys: RiscaRicercaPagamentiConsts) {
    // Estraggo le chiavi dalla classe
    const {
      STATO_PAGAMENTO,
      MODALITA_PAGAMENTO,
      DATA_OP_VALUTA_DA,
      DATA_OP_VALUTA_A,
      QUINTO_CAMPO,
      CODICE_AVVISO,
      CODICE_RIFERIMENTO_OPERAZIONE,
      IMPORTO_DA,
      IMPORTO_A,
      SOGGETTO,
    } = keys;

    // Definisco per l'oggetto di configurazione chiavi e proprietà
    this.formConfigs[STATO_PAGAMENTO] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[MODALITA_PAGAMENTO] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[DATA_OP_VALUTA_DA] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[DATA_OP_VALUTA_A] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[QUINTO_CAMPO] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[CODICE_AVVISO] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[CODICE_RIFERIMENTO_OPERAZIONE] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[IMPORTO_DA] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[IMPORTO_A] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
    this.formConfigs[SOGGETTO] = new FormControl(
      { value: null, disabled: false },
      { validators: [] }
    );
  }

  /**
   * Funzione che definisce le logiche di generazione delle funzioni di controlli per i campi incrociati del form.
   * La funzione è pensata per l'override.
   * @param keys RiscaRicercaPagamentiConsts che definisce la struttura dalla quale estrarre le chiavi.
   * @override
   */
  protected setFormValidators(keys: RiscaRicercaPagamentiConsts) {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Validatore per data op da/a
    let dateOpValidator: ValidatorFn;
    dateOpValidator = calendarioNgbInizioFineValidator(
      keys.DATA_OP_VALUTA_DA,
      keys.DATA_OP_VALUTA_A
    );
    // Validatore range importi ricerca
    let rangeImportiValidator: ValidatorFn;
    rangeImportiValidator = minMaxNumberValidator(
      keys.IMPORTO_DA,
      keys.IMPORTO_A,
      true
    );

    // Validatore per i campi "minimi"
    let campiMinimiRicPag: ValidatorFn;
    campiMinimiRicPag = oneFieldEvaluetedValidator(
      [
        keys.STATO_PAGAMENTO,
        keys.MODALITA_PAGAMENTO,
        keys.DATA_OP_VALUTA_DA,
        keys.DATA_OP_VALUTA_A,
        keys.QUINTO_CAMPO,
        keys.CODICE_AVVISO,
        keys.CODICE_RIFERIMENTO_OPERAZIONE,
        keys.IMPORTO_DA,
        keys.IMPORTO_A,
        keys.SOGGETTO,
      ],
      ERR_KEY.ALMENO_UN_CAMPO_RICERCA_PAGAMENTI_VALORIZZATO
    );

    // Assegno i validatori
    this.formValidators = [
      dateOpValidator,
      rangeImportiValidator,
      campiMinimiRicPag,
    ];
  }
}
