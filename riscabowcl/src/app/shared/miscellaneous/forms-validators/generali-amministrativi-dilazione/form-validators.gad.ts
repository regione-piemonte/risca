import { FormControl, FormGroup, ValidationErrors } from '@angular/forms';
import { AnnualitaSDVo } from 'src/app/core/commons/vo/annualita-sd-vo';
import { RimborsoVo } from 'src/app/core/commons/vo/rimborso-vo';
import { RiscaErrorKeys } from 'src/app/shared/utilities/classes/errors-keys';
import { IRiscaCheckboxData } from '../../../utilities';
import { checkValidationFormControlByFormGroup, generaObjErrore } from '../forms-validators';
import { AppActions, CodiciTipiRiscossione } from './../../../utilities/enums/utilities.enums';

/**
 * #################################
 * REGOLE PER INSERIMENTO E MODIFICA
 * #################################
 */

/**
 * Form validator personalizzato.
 * Il validatore verificherà che se il flag annullato è true, la motivazione deve essere valorizzata.
 * @param flagAnnullatoFieldName string nome del campo flag annullato nella form
 * @param motivazioneFieldName string nome del campo motivazione nella form
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const flagAnnullatoEMotivazioneValidator = (
  flagAnnullatoFieldName: string,
  motivazioneFieldName: string
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che il formControl esista
    if (!formGroup) {
      return;
    }

    // Variabili di comodo
    const fg = formGroup;
    const flgAnnFN = flagAnnullatoFieldName;
    const motFN = motivazioneFieldName;

    // Recupero i valori dei form control
    const flgAnnullato: IRiscaCheckboxData = checkValidationFormControlByFormGroup(fg, flgAnnFN);
    const motivazione: any | string = checkValidationFormControlByFormGroup(fg, motFN);

    // Verifico lo stato del flag
    if (!flgAnnullato) {
      // Niente da segnalare
      return null;
    }

    // Estraggo il valore del flag
    const annullato = flgAnnullato.check;
    const isMotValuated = motivazione === false;
    // Verifico i campi per il controllo
    if (annullato === true && (isMotValuated || !motivazione)) {
      // Definisco la costante contenente le chiavi d'errore per i forms validators
      const ERR_KEY = RiscaErrorKeys;
      // Preparo l'oggetto di errore
      const key = ERR_KEY.FLAG_ANNULLATO_E_MOTIVAZIONE;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Rimuovo eventuali errori
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che se il flag invio speciale è true, le annualità devono avere anni diversi tra loro.
 * @param flagInvSpecFieldName string nome del campo flag invio speciale nella form
 * @param annualita: AnnualitaSDVo[] lita di annualità da esaminare
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const verificaAnnualitaDoppiaValidator = (
  flagInvSpecFieldName: string,
  annualita: AnnualitaSDVo[]
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Verifico che il formControl esista
    if (!formControl) {
      return;
    }
    // Estraggo i valori
    const flagInvioSpeciale = formControl.get(flagInvSpecFieldName)?.value;
    // Eseguo i controlli
    if (flagInvioSpeciale && annualita != undefined && annualita.length > 1) {
      // Prendo tutti gli anni
      var listaAnni = annualita.map((item: AnnualitaSDVo) => {
        return item.anno;
      });
      // Controllo se per ogni anno ce ne sta un altro creando un Set dall'array: il set non ha duplicati, quindi se nell'array ci sono duplicati, il size del set è < length dell'array
      var haDuplicati = new Set(listaAnni).size !== listaAnni.length;
      // Se ho duplicati ritorno errore
      if (haDuplicati) {
        // Definisco la costante contenente le chiavi d'errore per i forms validators
        const ERR_KEY = RiscaErrorKeys;
        // Preparo l'oggetto di errore
        const key = ERR_KEY.VERIFICA_ANNUALITA_DOPPIA;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
      }
    }

    // Rimuovo eventuali errori
    return null;
  };
};

/**
 * ########################
 * REGOLE PER SOLA MODIFICA
 * ########################
 */

/**
 * Form validator personalizzato.
 * Il validatore verificherà che se l'importo compensazione canone è valorizzato, la modifica dello stato debitorio non è permessa.
 * @param impCompCanoneFieldName string nome del campo importo compensazione canone nella form
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const compensazioneCanoneValidator = (
  impCompCanoneFieldName: string,
  modalita: AppActions
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Verifico che il formControl esista e che siamo in modifica
    if (!formControl || modalita != AppActions.modifica) {
      return;
    }
    // Estraggo i valori
    const importoCompensazioneCanone = formControl.get(
      impCompCanoneFieldName
    )?.value;
    // Eseguo i controlli
    if (importoCompensazioneCanone != null && importoCompensazioneCanone != 0) {
      // Definisco la costante contenente le chiavi d'errore per i forms validators
      const ERR_KEY = RiscaErrorKeys;
      // Preparo l'oggetto di errore
      const key = ERR_KEY.IMPORTO_COMPENSAZIONE_CANONE;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Rimuovo eventuali errori
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che se esistono rimborsi “da compensare” o “compensati”, la modifica dello stato debitorio non è permessa.
 * @param rimborsi RimborsoVo[] con la lista dei rimborsi dello stato debitorio
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const rimborsiCompensiValidator = (
  rimborsi: RimborsoVo[],
  modalita: AppActions
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Verifico che il formControl esista e che siamo in modifica
    if (!formControl || modalita != AppActions.modifica) {
      return;
    }
    // Controllo se esiste almeno un rimborso con delle compensazioni
    const haCompensazioni =
      rimborsi.findIndex(
        (r: RimborsoVo) =>
          r.tipo_rimborso?.cod_tipo_rimborso ==
            CodiciTipiRiscossione.DA_COMPENSARE ||
          r.tipo_rimborso?.cod_tipo_rimborso == CodiciTipiRiscossione.COMPENSATO
      ) > -1;
    // Eseguo i controlli
    if (haCompensazioni) {
      // Definisco la costante contenente le chiavi d'errore per i forms validators
      const ERR_KEY = RiscaErrorKeys;
      // Preparo l'oggetto di errore
      const key = ERR_KEY.RIMBORSI_DA_COMPENSARE_O_COMPENSATI;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Rimuovo eventuali errori
    return null;
  };
};
