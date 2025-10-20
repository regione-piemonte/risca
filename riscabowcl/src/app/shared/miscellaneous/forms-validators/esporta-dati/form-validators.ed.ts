import { FormGroup, ValidationErrors } from '@angular/forms';
import {
  checkValidationFormControlByFormGroup,
  generaObjErrore,
} from 'src/app/shared/miscellaneous/forms-validators/forms-validators';
import { RiscaErrorKeys } from 'src/app/shared/utilities/classes/errors-keys';
import { convertNgbDateStructToMoment } from '../../../services/risca/risca-utilities/risca-utilities.functions';

/**
 * Form validator personalizzato.
 * Il validatore verificherà che la data di fine periodo selezionata per la ricerca della funzione "esporta dati", sia nello stesso anno della data inizio.
 * @param calendarioInizioFormControlName string nome del form control per il calendario d'inizio.
 * @param calendarioFineFormControlName string nome del form control per il calendario di fine.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const dataFineStessoAnnoDataInizio = (
  calendarioInizioFormControlName: string,
  calendarioFineFormControlName: string
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      // Niente oggetto
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
    // Recupero l'anno per le date
    const annoInizio = momentInizio.get('year');
    const annoFine = momentFine.get('year');

    // Verifico se gli anni son diversi
    if (annoFine !== annoInizio) {
      // Definisco la chiave per l'errore
      const key = ERR_KEY.DATE_END_EXPORT_DATA_SAME_YEAR;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Non ci sono errori
    return null;
  };
};
