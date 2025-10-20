import { FormGroup, ValidationErrors, FormControl } from '@angular/forms';
import { generaObjErrore } from 'src/app/shared/miscellaneous/forms-validators/forms-validators';
import { RiscaErrorKeys } from 'src/app/shared/utilities/classes/errors-keys';

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il form control importo versato sia inferiore all'importo da assegnare.
 * @param importoVersatoFormControlName string nome del form control per l'importo versato.
 * @param importoDaAssegnareFormControlName string nome del form control per l'importo da assegnare.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const valoreImportoVersato = (
  importoVersatoFormControlName: string,
  importoDaAssegnareFormControlName: string
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
    const importoVersato =
      formGroup.get(importoVersatoFormControlName)?.value ?? 0;
    const importoDaAssegnare =
      formGroup.get(importoDaAssegnareFormControlName)?.value ?? 0;

    // Verifico i dati
    if (importoVersato < importoDaAssegnare) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.IMPORTO_DA_ASSEGNARE_SUPERIORE_A_IMPORTO_VERSATO;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }
    // Nessun errore
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il form control per l'importo non proprio sia maggiore di 0.
 * @param valore number con il valore da confrontare. Per default: 0.
 * @param isEqual boolean che accetta che il valore sia anche uguale. Per default: false.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const importoNonProprioMagZero = (
  valore: number = 0,
  isEqual: boolean = false
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formControl) {
      // Niente oggetto del form control
      return null;
    }
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Controllo i form control tramite funzione
    const importoNonProprio = formControl.value;
    // Verifico se l'input è null o undefined
    if (importoNonProprio == undefined) {
      // Nessun controllo
      return null;
    }

    // Definisco delle variabili di check
    const case1 = isEqual && importoNonProprio < valore;
    const case2 = importoNonProprio <= valore;

    // Verifico i dati
    if (case1 || case2) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.IMPORTO_NON_PROPRIO_ZERO;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Nessun errore
    return null;
  };
};