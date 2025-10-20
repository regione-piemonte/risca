import { FormControl, FormGroup, ValidationErrors } from '@angular/forms';
import { RiscaErrorKeys } from 'src/app/shared/utilities/classes/errors-keys';
import { ComuneVo } from '../../../../core/commons/vo/comune-vo';
import {
  checkValidationFormControl,
  checkValidationFormControlByFormGroup,
  generaObjErrore,
} from '../forms-validators';
import { isDataValiditaAttiva } from '../../../services/risca/risca-utilities/risca-utilities.functions';

/**
 * Form validator personalizzato per il formgroup.
 * Il validatore verificherà se il campo comuneFormControlName è stato valorizzato con un comune valido.
 * @param comuneFormControlName string nome del form control per il comune.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const comuneDataFineValiditaFG = (comuneFormControlName: string) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      return;
    }

    // Controllo i form control tramite funzione
    let comune: ComuneVo = checkValidationFormControlByFormGroup(
      formGroup,
      comuneFormControlName
    );

    // Verifico che sia stato passato un comune
    return checkComuneDataFineValidita(comune);
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà se il campo è un comune valido.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const comuneDataFineValiditaFC = () => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formControl) {
      return;
    }

    // Controllo i form control tramite funzione
    let comune: ComuneVo = checkValidationFormControl(formControl);

    // Verifico che sia stato passato un comune
    return checkComuneDataFineValidita(comune);
  };
};

/**
 * Funzione usata dai validatori del comune per verificare se il comune è valido in base alla sua data validità. In caso non lo sia, ritorna l'errore.
 * @param comune ComuneVo da verificare se è valito.
 * @returns ValidationErrors se il comune non è valido, altrimenti null.
 */
export const checkComuneDataFineValidita = (
  comune: ComuneVo
): ValidationErrors | null => {
  // Verifico che sia stato passato un comune
  if (!comune || typeof comune == 'string') {
    return null;
  }

  // Verifico la validità per data_fine_validità
  const comuneValido = isDataValiditaAttiva(comune);

  // Verifico se il comune risulta non valido per la data fine validita
  if (!comuneValido) {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;
    // Preparo l'oggetto di errore
    const key = ERR_KEY.COMUNE_DATA_FINE_VALIDITA;
    // Genero e ritorno l'errore
    const customFormError = generaObjErrore(key);
    // Ritorno l'errore custom
    return customFormError;
    // #
  } else {
    // Niente da segnalare
    return null;
  }
};
