import { AbstractControl, AbstractControlOptions } from '@angular/forms';

/** Type che definisce un tipo d'oggetto per la configurazione dei form. */
export type RiscaFormConfigs = { [key: string]: AbstractControl };
/** Type che definisce la struttura di configurazione per le opzioni del FormGroup. */
export type FormGroupOptions =
  | AbstractControlOptions
  | {
      [key: string]: any;
    };
