import { FormControl, FormGroup, ValidationErrors } from '@angular/forms';
import { TipoSoggettoVo } from 'src/app/features/ambito/models';
import { controllaPartitaIVA } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.functions';
import { CodiceTipoSoggetto, RiscaRegExp } from 'src/app/shared/utilities';
import { RiscaErrorKeys } from 'src/app/shared/utilities/classes/errors-keys';
import { checkValidationFormControlByFormGroup, generaObjErrore } from '../forms-validators';

/**
 * Form validator personalizzato.
 * Il validatore verificherà se il campo tSFormControlName è stato valorizzato.
 * Se lo è, verifica che anche il campo cFFormControlName venga valorizzato secondo le verifiche del codice fiscale.
 * @param tSFormControlName string nome del form control per tipo soggetto.
 * @param cFFormControlName string nome del form control per codice fiscale.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const codiceFiscalePerTipoSoggetto = (
  tSFormControlName: string,
  cFFormControlName: string
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      return;
    }

    // Controllo i form control tramite funzione
    let tipoSoggetto = checkValidationFormControlByFormGroup(
      formGroup,
      tSFormControlName
    );
    let codiceFiscale = checkValidationFormControlByFormGroup(
      formGroup,
      cFFormControlName
    );

    // Verifico che siano ritornate i valori
    if (!tipoSoggetto || !codiceFiscale) {
      return null;
    }

    // Definisco l'oggetto di regexp
    const riscaRegExp: RiscaRegExp = new RiscaRegExp();
    // Riassegno le variabili
    tipoSoggetto = tipoSoggetto as TipoSoggettoVo;
    codiceFiscale = codiceFiscale as string;
    // Definisco delle costanti per i codici soggetto
    const PF = CodiceTipoSoggetto.PF; // Persona fisica
    const PB = CodiceTipoSoggetto.PB; // Persona giuridica pubblica
    const PG = CodiceTipoSoggetto.PG; // Persona giuridica privata

    // Definisco una funzione per la generazione del codice errore
    const genCodErr = (idErr: string, valid: boolean) => {
      // Verifico il risultato
      if (valid) {
        return null;
      } else {
        // Creo un oggetto vuoto
        const res = {};
        // Definisco la proprietà dell'errore
        res[idErr] = true;
        // Ritorno l'errore
        return res;
      }
    };
    // Defenisco una funzione di comodo per il controllo del codice fiscale
    const checkCodiceFiscale = (idErr: string, regExp: RegExp, cf: string) => {
      // Testo la regexp
      const valid = regExp.test(cf);
      // Ritorno la generazione del risultato
      return genCodErr(idErr, valid);
    };
    // Defenisco una funzione di comodo per il controllo della partita iva
    const checkPartitaIVA = (idErr: string, pi: string) => {
      // Lancio il controllo sulla partita iva
      const valid = controllaPartitaIVA(pi);
      // Ritorno la generazione del risultato
      return genCodErr(idErr, valid);
    };

    // Verifico il tipo soggetto
    switch (tipoSoggetto.cod_tipo_soggetto) {
      case PF:
        // Ritorno la validazione
        return checkCodiceFiscale(
          ERR_KEY.COD_FISC_PF,
          new RegExp(riscaRegExp.codiceFiscalePF),
          codiceFiscale
        );
      case PB:
        // Ritorno la validazione
        return checkPartitaIVA(ERR_KEY.PARTITA_IVA_PB, codiceFiscale);
      case PG:
        // Definisco i casi di verifica
        const checkCodFisc = checkCodiceFiscale(
          ERR_KEY.COD_FISC_PG,
          new RegExp(riscaRegExp.codiceFiscalePF),
          codiceFiscale
        );
        const checkPartitaIva = checkPartitaIVA(
          ERR_KEY.PARTITA_IVA_PG,
          codiceFiscale
        );

        // Se uno dei due check è null allora è un codice fiscale/partita iva valido
        if (checkCodFisc === null) {
          return checkCodFisc;
        }
        if (checkPartitaIva === null) {
          return checkPartitaIva;
        }

        // Verifico se il codice fiscale è alfanumerico (cf persona) o solo numerico (partita iva)
        const isPartitaIva = new RegExp(/^\d+$/).test(codiceFiscale);
        // Altrimenti ritorno errore
        if (checkCodFisc && !isPartitaIva) {
          // Ritorno l'errore per il codice fiscale
          return checkCodFisc;
          // #
        } else if (checkPartitaIva && isPartitaIva) {
          // Ritorno l'errore per la partita iva
          return checkPartitaIva;
          // #
        } else {
          // Casistica finale di sicurezza
          return checkCodFisc || checkPartitaIva;
          // #
        }

      default:
        return null;
    }
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà se la partita iva è formalmente corretta.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const partitaIvaValidator = () => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formControl) {
      return;
    }
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Controllo i form control tramite funzione
    let partitaIva: string = formControl.value;
    // Verifico che esista la partita iva
    if (partitaIva == undefined) {
      // Non è stata digitato nessun carattere
      return null;
    }

    // Lancio il controllo sulla partita iva
    const valid: boolean = controllaPartitaIVA(partitaIva);
    // Verifico la validità
    if (!valid) {
      // Definisco la chiave per l'errore
      const key = ERR_KEY.PARTITA_IVA_PB;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Nessun errore
    return null;
  };
};
