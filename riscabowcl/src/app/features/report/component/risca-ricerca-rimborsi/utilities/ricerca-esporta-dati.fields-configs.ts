import * as moment from 'moment';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import {
  RiscaComponentConfig,
  RiscaFormInputDatepicker,
  RiscaFormInputSelect,
} from 'src/app/shared/utilities';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { convertMomentToNgbDateStruct } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RicercaEsportaDatiConsts } from './ricerca-esporta-dati.consts';

/**
 * Classe che definisce le configurazioni per i campi del componente associato.
 */
export class RicercaEsportaDatiFieldsConfig {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** RicercaEsportaDatiConsts come classe che definisce le costanti del componente. */
  RED_C = new RicercaEsportaDatiConsts();

  /** Oggetto di configurazione per il campo: dataDa. */
  dataDaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: dataA. */
  dataAConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: tipoReport. */
  tipoElaboraReportConfig: RiscaComponentConfig<RiscaFormInputSelect>;

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
    // Definisco un minimo ed un massimo per le date
    const min = moment('01/01/2000', this.C_C.DATE_FORMAT_VIEW);
    const max = moment().endOf('year').add(1, 'year');
    // Converto le date in strutture per l'input
    const ngbDateMin = convertMomentToNgbDateStruct(min);
    const ngbDateMax = convertMomentToNgbDateStruct(max);

    this.dataDaConfig = riscaFormBuilder.genInputDatepicker({
      label: this.RED_C.LABEL_DATA_DA,
      showErrorFC: true,
      showErrorFG: true,
      minDate: ngbDateMin,
      maxDate: ngbDateMax,
    });

    this.dataAConfig = riscaFormBuilder.genInputDatepicker({
      label: this.RED_C.LABEL_DATA_A,
      showErrorFC: true,
      showErrorFG: true,
      minDate: ngbDateMin,
      maxDate: ngbDateMax,
    });

    this.tipoElaboraReportConfig = riscaFormBuilder.genInputSelect({
      label: this.RED_C.LABEL_TIPO_ELABORA_REPORT,
      defaultLabel: this.C_C.SELEZIONA,
      showErrorFC: true,
    });
  }
}
