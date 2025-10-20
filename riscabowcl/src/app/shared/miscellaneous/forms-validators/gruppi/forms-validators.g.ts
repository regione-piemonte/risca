import { FormControl, ValidationErrors } from '@angular/forms';
import { ComponenteGruppo } from 'src/app/core/commons/vo/componente-gruppo-vo';
import { RiscaErrorKeys } from 'src/app/shared/utilities/classes/errors-keys';
import {
  checkValidationFormControl,
  generaObjErrore,
} from '../forms-validators';

/**
 * Form validator personalizzato.
 * Il validatore verificherà che l'array contenga almeno un capogruppo.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const almenoUnComponenteEUnCapogruppoValidator = () => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Effettuo il controllo sul form control
    const componenti: ComponenteGruppo[] =
      checkValidationFormControl(formControl);

    // Verifico che esistano i componenti
    if (!componenti || componenti.length === 0) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.QUANTITA_COMPONENTI_GRUPPO;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Verifico che ci sia almeno un capogruppo
    const existCG = componenti.some((c) => c.flg_capo_gruppo);
    // Se non esiste torno un errore
    if (!existCG) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.PRESENZA_CAPOGRUPPO;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Tutto a posto
    return null;
  };
};
