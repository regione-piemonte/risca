import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
} from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Moment } from 'moment';
import { convertNgbDateStructToMoment } from '../../services/risca/risca-utilities/risca-utilities.functions';
import { DynamicObjAny, ICheckValidators } from '../../utilities';
import { RiscaErrorKeys } from '../../utilities/classes/errors-keys';

/**
 * Interfaccia personalizzata che contiene le informazioni degli errori lanciati dai validatori di form personalizzati.
 */
export interface ValidatorErrorBody {
  data?: any;
  message?: string;
  value?: any;
}

/**
 * Interfaccia personalizzata che contiene le informazioni per l'errore generato dal FormValidator: numberCompositionValidator.
 */
export interface NumberCompositionValidatorError {
  numberCompositionValidator: ValidatorErrorBody;
}

/**
 * ####################################
 * FUNZIONI DI UTILITY PER I VALIDATORI
 * ####################################
 */

/**
 * Funzione di supporto che effettua i check di validazione/esistenza delle informazioni minime per un form control.
 * Se la validazione è andata a buon fine, ritorna il valore del form control.
 * @param formGroup FormGroup da verificare.
 * @param formControlName string che identifica un form control del form group, da verificare.
 * @param checkEmptyString boolean che permette di controllare se il campo sia o non sia stringa vuota. Se abilitato e il value è stringa vuota, ritorna false. Per default il flag è false.
 * @param checkZeroValue boolean che permette di controllare se il campo sia o non sia un number a 0. Se abilitato e il value è zero, ritorna false. Per default il flag è false.
 * @returns boolean che definisce se il FormControl esiste ed è valorizzato.
 */
export const checkValidationFormControlByFormGroup = (
  formGroup: FormGroup,
  formControlName: string,
  checkEmptyString = false,
  checkZeroValue = false
): boolean | any => {
  // Recupero i formControl tramite name
  const formControl = formGroup.get(formControlName);
  // Verifico che i formControl esistano
  if (formControl == undefined) {
    // Blocco il flusso
    return false;
  }

  // Richiamo la funzione di check del form control
  return checkValidationFormControl(
    formControl,
    checkEmptyString,
    checkZeroValue
  );
};

/**
 * Funzione di supporto che effettua i check di validazione/esistenza delle informazioni minime per un form control.
 * Se la validazione è andata a buon fine, ritorna il valore del form control.
 * @param formControl FormControl che identifica un form control del form group, da verificare.
 * @param checkEmptyString boolean che permette di controllare se il campo sia o non sia stringa vuota. Se abilitato e il value è stringa vuota, ritorna false. Per default il flag è false.
 * @param checkZeroValue boolean che permette di controllare se il campo sia o non sia un number a 0. Se abilitato e il value è zero, ritorna false. Per default il flag è false.
 * @returns boolean | any che definisce se il FormControl non esiste, o se esiste ritorna il valore associato.
 */
export const checkValidationFormControl = (
  formControl: FormControl | AbstractControl,
  checkEmptyString = false,
  checkZeroValue = false
): boolean | any => {
  // Verifico che i formControl esistano
  if (formControl == undefined) {
    // Blocco il flusso
    return false;
  }

  // Recupero i valori dei form control
  const value = formControl.value;

  // Creo le condizioni di verifica
  const valueNotDefined = value === undefined || value === null;
  const valueEmptyString = !valueNotDefined && checkEmptyString && value === '';
  const valueZero = !valueNotDefined && checkZeroValue && Number(value) === 0;

  // Verifico che esistano i valori
  if (valueNotDefined || valueEmptyString || valueZero) {
    return false;
  }

  // Form control valido
  return value;
};

/**
 * Funzione di supporto che genera l'oggetto di errore da ritornare al form.
 * @param key string che definisce la chiave dell'errore.
 * @param value any che definisce il value per la chiave dell'errore.
 * @returns DynamicObjAny contenente le configurazioni degli errori.
 */
export const generaObjErrore = (
  key: string = 'NO_KEY',
  value: any = true
): DynamicObjAny => {
  // Genero un oggetto che conterrà le informazioni dell'errore
  const o = {};
  // Aggiungo la chiave all'oggetto
  o[key] = value;
  // Ritorno l'oggetto
  return o;
};

/**
 * Funzione di supporto che gestisce le logiche per il ritorno degli errori delle form, partendo da un oggetto ICheckValidators.
 * @param result ICheckValidators con il risultato di un check per un validatore.
 * @returns ValidationErrors | null che definisce se il check di controllo è passato (null) o non passato (oggetto d'errore).
 */
export const checkValidatorsResult = (
  result: ICheckValidators
): ValidationErrors | null => {
  // Verifico l'input
  if (!result) {
    // Ignoro tutto e ritorno OK
    return null;
  }

  // Verifico il risultato del check
  if (!result.valid) {
    // C'è un errore, genero un oggetto tramite chiave
    const key = result.error;
    // Genero e ritorno l'errore
    return generaObjErrore(key);
  }

  // Tutto a posto
  return null;
};

/**
 * ################################
 * VALIDATORI EFFETTIVI PER LE FORM
 * ################################
 */

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il campo definito come input e recuperato dall'oggetto FormControl sia un valore numerico composto da x cifre intere e y cifre decimali.
 * Per default: x vale 6 | y vale 4.
 * @param maxInt number che definisce il numero massimo di cifre intere accettate dal form control. Il default è 6.
 * @param maxFloat number che definisce il numero massimo di cifre decimali accettate dal form control. Il default è 4.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const numberCompositionValidator = (
  maxInt: number = 6,
  maxFloat: number = 4
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Lancio la funzione di validazione
    return numberCompositionValidatorFC(formControl, maxInt, maxFloat);
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il campo definito come input e recuperato dall'oggetto FormControl.
 * sia un valore numerico composto da x cifre intere e y cifre decimali.
 * Per default: x vale 6 | y vale 4.
 * @param formControl FormControl con il riferimento al form control dalla quale estrarre il valore da verificare.
 * @param maxInt number che definisce il numero massimo di cifre intere accettate dal form control. Il default è 6.
 * @param maxFloat number che definisce il numero massimo di cifre decimali accettate dal form control. Il default è 4.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const numberCompositionValidatorFC = (
  formControl: FormControl,
  maxInt: number = 6,
  maxFloat: number = 4
): ValidationErrors | null => {
  // Recupero il contenuto del formControl
  let inputValue = formControl.value;
  // Lancio la funzione di verifica passando i parametri
  return numberComposition(inputValue, maxInt, maxFloat);
};

/**
 * Il validatore verificherà che il campo definito come input e recuperato dall'oggetto FormControl.
 * sia un valore numerico composto da x cifre intere e y cifre decimali.
 * Per default: x vale 6 | y vale 4.
 * @param maxInt number che definisce il numero massimo di cifre intere accettate dal form control. Il default è 6.
 * @param maxFloat number che definisce il numero massimo di cifre decimali accettate dal form control. Il default è 4.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const numberComposition = (
  inputValue: any,
  maxInt: number = 6,
  maxFloat: number = 4
): ValidationErrors | null => {
  // Definizione delle costanti
  const SEPARATORE = '.';
  // Definisco la costante contenente le chiavi d'errore per i forms validators
  const ERR_KEY = RiscaErrorKeys;

  // Verifico che l'input esista
  if (inputValue == undefined || inputValue === '') {
    return null;
  }

  // Verifico che l'input value sia un numero
  if (typeof inputValue !== 'number' || isNaN(Number(inputValue))) {
    // Recupero la chiave d'errore
    const key = ERR_KEY.NUMBER_COMP_NOT_NUMBER;
    // Genero e ritorno l'errore
    return generaObjErrore(key);
  }

  // Per il controllo, devo avere il numero assoluto. Quindi forzo a positivo il numero in input
  inputValue = Math.abs(inputValue);

  // Verifico il numero massimo che può assumere la parte intera e decimale
  const inputIntFloat = inputValue.toString().split(SEPARATORE);
  const maxIntSuperato =
    inputIntFloat[0] !== undefined ? inputIntFloat[0].length > maxInt : false;
  const maxDecimale =
    inputIntFloat[1] !== undefined ? inputIntFloat[1].length > maxFloat : false;

  // Verifico se le parti del numero hanno superato la soglia massima
  if (maxIntSuperato || maxDecimale) {
    // Recupero la chiave d'errore
    const key = ERR_KEY.NUMBER_COMP_INVALID;
    // Genero e ritorno l'errore
    return generaObjErrore(key);
  }

  // La validazione è corretta
  return null;
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il form control contenga all'interno dell'array che lo valorizza almeno un elemento.
 * @param quantity number che definisce la quantità minima da verificare. Per default è 1.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const arrayContainsAtLeastValidator = (quantity: number = 1) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Verifico che il formControl esista
    if (!formControl) return;
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Recupero il valore dal form control
    const value = formControl.value;
    // Verifico che il valore esista, sia un array e contenga un elmeneto
    if (!value || !Array.isArray(value) || value.length < quantity) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.CONTAIN_AT_LEAST;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Rimuovo eventuali errori
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il campo definito come input e recuperato dall'oggetto FormControl.
 * Il valore deve essere maggiore di una quantità "min", ma accetta anche la possibilità di valore null.
 * Per default: min vale 0.
 * @param min number che definisce il numero minimo che l'utente può inserire.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const minOrNullValidator = (min: number = 0) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Recupero il contenuto del formControl
    const inputValue = formControl.value;

    // Verifico che l'input esista
    if (inputValue == null) {
      // Nessun errore, null è un valore valido
      return null;
    }

    // Verifico che l'input value sia un numero
    if (typeof inputValue !== 'number' || isNaN(Number(inputValue))) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.NUMBER_COMP_NOT_NUMBER;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Verifico che il numero sia maggiore uguale al minimo
    const isValid = inputValue >= min;

    // Verifico se le regole sono rispettate
    if (!isValid) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.NUMBER_MIN_INVALID;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // La validazione è corretta
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il campo definito come input e recuperato dall'oggetto FormControl.
 * Il valore deve essere minore di una quantità "max", ma accetta anche la possibilità di valore null.
 * Per default: max vale 100000.
 * @param max number che definisce il numero minimo che l'utente può inserire.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const maxOrNullValidator = (max: number = 100000) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Recupero il contenuto del formControl
    const inputValue = formControl.value;

    // Verifico che l'input esista
    if (inputValue === null) {
      // Nessun errore, null è un valore valido
      return null;
    }

    // Verifico che l'input value sia un numero
    if (typeof inputValue !== 'number' || isNaN(Number(inputValue))) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.NUMBER_COMP_NOT_NUMBER;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Verifico che il numero sia maggiore uguale al minimo
    const isValid = inputValue <= max;

    // Verifico se le regole sono rispettate
    if (!isValid) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.NUMBER_MAX_INVALID;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // La validazione è corretta
    return null;
  };
};

/**
 * Verifica che dataStartFormControlName risulti precedente a dataEndFormControlName.
 * Il dato del campo deve essere definito come oggetto NgbDateStruct.
 * @param dataStartFormControlName string che definisce il nome del formControl della data che deve risultare antecendente.
 * @param dataEndFormControlName string che definisce il nome del formControl della data che deve risultare successiva.
 * @param lessOrEqual boolean che definisce se la data di partenza può essere anche uguale a quella di fine.
 * @param customError string con un errore custom da utilizzare al posto del default.
 * @returns ValidationErrors con la mappatura dell'errore se il validator non soddisfa le condizioni.
 */
export const dataInizioDataFineValidator = (
  dataStartFormControlName: string,
  dataEndFormControlName: string,
  lessOrEqual: boolean = false,
  customError?: string
) => {
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che il formControl esista
    if (!formGroup) {
      return;
    }
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Variabili di comodo
    const f = formGroup;
    const dsFCN = dataStartFormControlName;
    const deFCN = dataEndFormControlName;
    let ds: NgbDateStruct;
    let de: NgbDateStruct;

    // Recupero i valori dei form control dal FormGroup
    ds = checkValidationFormControlByFormGroup(f, dsFCN);
    de = checkValidationFormControlByFormGroup(f, deFCN);

    // Verifico che i campi esistano
    if (!ds || !de) {
      // Non si può fare il check
      return null;
    }

    // Converto le date in moment
    const dsM: Moment = convertNgbDateStructToMoment(ds);
    const deM: Moment = convertNgbDateStructToMoment(de);

    // Definisco la condizione per generare l'errore
    let validDates = true;
    // Verifico la condizione in base al flag lessOrEqual
    if (lessOrEqual) {
      // Definisco che la data di start deve essere uguale o prima di quella di end
      validDates = dsM.isSameOrBefore(deM);
      // #
    } else {
      // Definisco che la data di start deve essere prima di quella di end
      validDates = dsM.isBefore(deM);
      // #
    }

    // La data di protocollo deve essere antecedente alla data scadenza pagamento
    if (!validDates) {
      // Preparo l'oggetto di errore
      const keyErrDSPAndDP = customError ?? ERR_KEY.DATE_START_END_INVALID;
      // Ritorno l'errore generato
      return generaObjErrore(keyErrDSPAndDP);
    }

    // Rimuovo eventuali errori
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il calendario d'inizio (come form control) sia minore del calendario di fine (come form control).
 * Il validatore funziona per i calendari NgbDatepicker.
 * @param calendarioInizioFormControlName string nome del form control per il calendario d'inizio.
 * @param calendarioFineFormControlName string nome del form control per il calendario di fine.
 * @param customError string che identifica il nome da utilizzare per la generazione dell'errore.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const calendarioNgbInizioFineValidator = (
  calendarioInizioFormControlName: string,
  calendarioFineFormControlName: string,
  customError?: string
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      return;
    }
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Controllo i form control tramite funzione
    const calendarioInizio = checkValidationFormControlByFormGroup(
      formGroup,
      calendarioInizioFormControlName
    );
    const calendarioFine = checkValidationFormControlByFormGroup(
      formGroup,
      calendarioFineFormControlName
    );

    // Verifico che siano ritornate i valori
    if (!calendarioInizio || !calendarioFine) {
      return null;
    }

    // Creo dei moment per la comparazione
    const momentInizio = convertNgbDateStructToMoment(calendarioInizio);
    const momentFine = convertNgbDateStructToMoment(calendarioFine);

    // Verifico che la data d'inizio sia minore di quella di fine
    if (momentInizio.isSameOrAfter(momentFine)) {
      // Definisco la chiave per l'errore
      const key = customError ? customError : ERR_KEY.DATE_START_END_INVALID;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Non ci sono errori
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il campo definito sia composto da un esatto numero di caratteri, definito dall'input.
 * @param length number che definisce il numero di caratteri esatto per la lunghezza del campo.
 * @param minorOrGreaterErrors boolean che definisce se è necessario gestire un errore specifico se il valore è minore, ed un errore specifico se maggiore. Default: false.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const exactLengthValidator = (
  length: number,
  minorOrGreaterErrors = false
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Verifico il parametro di input
    if (length == undefined) {
      // Lancio un'eccezione
      throw new Error('exactLengthValidator - no input provide');
    }

    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Recupero il contenuto del formControl
    const inputValue = formControl.value;
    // Definisco le condizioni d'invalidità
    const invalidValue = inputValue == undefined || inputValue == '';

    // Verifico che l'input esista
    if (invalidValue) {
      // Nessun errore, null è un valore valido
      return null;
    }

    // Per ragioni di sicurezza e stabilità, utilizzo un try catch
    try {
      // "Converto" l'input in una stringa
      const input = `${inputValue ?? ''}`;
      // Verifico se il campo è lungo quanto richiesto
      const exactLength = input.length === length;
      // Verifico se il campo è lungo meno di quanto richiesto
      const minorLength = input.length < length;
      // Verifico se il campo è lungo più di quanto richiesto
      const greaterLength = input.length > length;

      // Verifico se le regole sono rispettate
      if (!exactLength && !minorOrGreaterErrors) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.LUNGHEZZA_CAMPO_ESATTA;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
        // #
      } else if (minorLength) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.LUNGHEZZA_CAMPO_MINORE;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
        // #
      } else if (greaterLength) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.LUNGHEZZA_CAMPO_MAGGIORE;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
        // #
      }

      // Nessun errore
      return null;
      // #
    } catch (e) {
      // Gestisco la situazione ignorando gli errori
      return null;
    }
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che la input numerica minima (come form control) sia minore della input numerica massima (come form control).
 * Il validatore funziona per i campi numerici.
 * @param calendarioInizioFormControlName string nome del form control per il calendario d'inizio.
 * @param calendarioFineFormControlName string nome del form control per il calendario di fine.
 * @param checkEqual boolean che abilita la possibilità di verficare se il numero minimo non deve essere uguale al massimo. Per default è: false.
 * @param customError string che identifica il nome da utilizzare per la generazione dell'errore.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const minMaxNumberValidator = (
  numberMinFormControlName: string,
  numberMaxFormControlName: string,
  checkEqual: boolean = false,
  customError?: string
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      return;
    }
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;
    // Variabili di comodo
    const fg = formGroup;
    const numMinFCN = numberMinFormControlName;
    const numMaxFCN = numberMaxFormControlName;

    // Controllo i form control tramite funzione
    let numMin: number;
    numMin = fg.get(numMinFCN)?.value;
    let numMax: number;
    numMax = fg.get(numMaxFCN)?.value;

    // Verifico che siano ritornate i valori
    if (numMin == undefined || numMax == undefined) {
      // Le informazioni risultano valide
      return null;
    }

    // Definisco il flag di gestione delle condizioni
    let valuesInError = false;
    // Verifico il flag per la condizioni di uguaglianza
    if (checkEqual) {
      // Verifico con uguaglianza
      valuesInError = numMin >= numMax;
      // #
    } else {
      // Verifica senza uguaglianza
      valuesInError = numMin > numMax;
    }

    // Verifico se i numeri non sono in range corretto
    if (valuesInError) {
      // Definisco la chiave per l'errore
      const key = customError ?? ERR_KEY.NUMBER_MIN_MAX_INVALID;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Non ci sono errori
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che almeno uno dei campi, passati con il nome della chiave all'interno dell'array, sia valorizzato.
 * @param fields string[] con la lista di chiavi come nomi dei campi del form da verificare.
 * @param customError string con un errore custom da utilizzare al posto del default.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const oneFieldEvaluetedValidator = (
  fields: string[],
  customError?: string
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      return;
    }
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Ciclo la lista di chiavi e imposto la logica di verifica
    const atLeastOneEvalueted: boolean = fields?.some((key: string) => {
      // Estraggo dal form il valore del campo
      const value = formGroup.get(key)?.value;
      // Ritorno il check sulla valorizzazione
      return value != null && value !== '';
    });

    // Verifico che la data d'inizio sia minore di quella di fine
    if (!atLeastOneEvalueted) {
      // Definisco la chiave per l'errore
      const key = customError ? customError : ERR_KEY.ERRORE_FORMATO_GENERICO;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Non ci sono errori
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che la input numerica sia in un range di valori.
 * Il validatore funziona per i campi numerici.
 * @param min number che definisce il valore minimo accettato per il controllo.
 * @param max number che definisce il valore massimo accettato per il controllo.
 * @param customError string che identifica il nome da utilizzare per la generazione dell'errore.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const numberInRangeValidator = (
  min: number,
  max: number,
  customError?: string
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Verifico che l'oggetto FormControl esista
    if (!formControl) {
      // Non esiste, il controllo è ok
      return null;
    }
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;
    // Recupero il valore della input
    let value: number;
    value = formControl.value;

    // Verifico che il valore sia compreso tra minimo e massimo
    if (value < min || value > max) {
      // Definisco la chiave per l'errore
      const key = customError ?? ERR_KEY.NUMBER_NOT_IN_RANGE;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Gestisco la situazione ignorando gli errori
    return null;
  };
};
