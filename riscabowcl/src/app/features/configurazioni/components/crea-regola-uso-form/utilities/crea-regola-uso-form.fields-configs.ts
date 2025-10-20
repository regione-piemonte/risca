import {
  RiscaComponentConfig,
  RiscaFormInputNumber,
  RiscaFormBuilderSize,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { CreaRegolaUsoFormConsts } from './crea-regola-uso-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class CreaRegolaUsoFormFieldsConfig {
  /** CreaRegolaUsoFormConsts come classe che definisce le costanti del componente. */
  CRUF_C = new CreaRegolaUsoFormConsts();

  /** Oggetto di configurazione per il campo: annualita. */
  annualitaConfig: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: percentuale aggiornamento canoni. */
  percentualeAggiornamentoConfig: RiscaComponentConfig<RiscaFormInputNumber>;

  /**
   * Costruttore.
   */
  constructor(riscaFormBuilder: RiscaFormBuilderService) {
    // Lancio il setup delle configurazioni
    this.setupFormInputs(riscaFormBuilder);
  }

  /**
   * Funzione di setup per le input del form.
   * @param riscaFormBuilder RiscaFormBuilderService servizio per la costruzione delle form input.
   * @returns Oggetto contenente le configurazioni dei campi del form.
   */
  setupFormInputs(riscaFormBuilder: RiscaFormBuilderService) {
    this.annualitaConfig = riscaFormBuilder.genInputNumber({
      label: this.CRUF_C.LABEL_ANNUALITA,
      min: 1000,
      max: 9999,
      useDecimal: false,
      maxLength: 4,
      showErrorFC: true,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.annualitaConfig.css['customForm'] = '';
    this.annualitaConfig.css['customError'] = {
      'max-width': '133px',
    };

    this.percentualeAggiornamentoConfig = riscaFormBuilder.genInputNumber({
      label: this.CRUF_C.LABEL_PERCENTUALE_AGGIORNAMENTO,
      useDecimal: true,
      /**
       * NOTA BENE: essendo che, al momento, la input tronca sempre e comunque i decimali definiti,
       * MA eventuali controlli si attivano con i numeri prima del troncamento, accade che il numero della input è troncata,
       * ma il messaggio d'errore dice che non accetta più dei decimali richiesti. Quindi mettendo un numero più alto,
       * anche se tronca, la visualizzazione è migliore.
       */
      decimals: 4,
      useSign: true,
      showErrorFC: true,
      size: RiscaFormBuilderSize.small,
    });
    // Rimuovo la classe container così che la label possa "uscire" dalla dimensione del campo
    this.percentualeAggiornamentoConfig.css['customForm'] = '';
    this.percentualeAggiornamentoConfig.css['customError'] = {
      'max-width': '133px',
    };
  }
}
