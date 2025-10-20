import {
  RiscaAppendix,
  RiscaAppendixPosition,
  RiscaComponentConfig,
  RiscaFormBuilderSize,
  RiscaFormInputNumber,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RegolaSogliaFormConsts } from './regola-soglia-form.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RegolaSogliaFormFieldsConfig {
  /** RegolaSogliaFormConsts come classe che definisce le costanti del componente. */
  RCMF_C = new RegolaSogliaFormConsts();

  /** Oggetto di configurazione per il campo: soglia. */
  sogliaConfigs: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: minimo inferiore. */
  minimoInferioreConfigs: RiscaComponentConfig<RiscaFormInputNumber>;
  /** Oggetto di configurazione per il campo: minimo superiore. */
  minimoSuperioreConfigs: RiscaComponentConfig<RiscaFormInputNumber>;

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
    // Definisco le configurazioni per l'appendice
    const text: string = '€';
    const position = RiscaAppendixPosition.right;
    const appendix = new RiscaAppendix({ text, position });

    this.sogliaConfigs = riscaFormBuilder.genInputNumber({
      label: this.RCMF_C.LABEL_SOGLIA,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
      useDecimal: true,
      min: 0,
      decimals: 4,
      decimaliNonSignificativi: true
    });

    this.minimoInferioreConfigs = riscaFormBuilder.genInputNumber({
      label: this.RCMF_C.LABEL_MINIMO_INFERIORE,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
      useDecimal: true,
      min: 0,
      decimals: 2,
    });
    this.minimoInferioreConfigs.css['appendix'] = appendix;
    
    this.minimoSuperioreConfigs = riscaFormBuilder.genInputNumber({
      label: this.RCMF_C.LABEL_MINIMO_SUPERIORE,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
      useDecimal: true,
      min: 0,
      decimals: 2,
    });
    this.minimoSuperioreConfigs.css['appendix'] = appendix;
  }
}
