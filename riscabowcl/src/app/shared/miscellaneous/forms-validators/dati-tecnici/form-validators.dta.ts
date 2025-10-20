import { FormGroup, ValidationErrors } from '@angular/forms';
import { intersectionWith } from 'lodash';
import { generaObjErrore } from 'src/app/shared/miscellaneous/forms-validators/forms-validators';
import { RiscaErrorKeys } from 'src/app/shared/utilities/classes/errors-keys';
import { RiduzioneAumentoVo } from '../../../../core/commons/vo/riduzione-aumento-vo';
import { UsoLeggePSDAmbienteInfo } from '../../../../features/pratiche/components/quadri-tecnici/utilities/interfaces/dt-ambiente-pratica.interfaces';
import { isEmptyObject } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  CodiciRiduzioniAumenti,
  DynamicObjAny,
  IdUnitaDiMisura,
} from '../../../utilities';
import { checkValidationFormControlByFormGroup } from '../forms-validators';

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il form control dataInizio sia valorizzato se il flag rateoPrimaAnnualità è true .
 * @param rateoPrimaAnnualitaFormControlName string nome del form control per la quantita.
 * @param dataInizioFormControlName string nome del form control per la quantita falda.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const rateoPrimaAnnualitaDataInizioValidator = (
  rateoPrimaAnnualitaFormControlName: string,
  dataInizioFormControlName: string
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
    const flag = checkValidationFormControlByFormGroup(
      formGroup,
      rateoPrimaAnnualitaFormControlName
    )?.check;
    const dataInizio = checkValidationFormControlByFormGroup(
      formGroup,
      dataInizioFormControlName
    );

    // Verifico i dati
    if (flag && !dataInizio) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.DATA_INIZIO_REQUIRED;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }
    // Nessun errore
    return;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il form control quantita sia come valore maggiore uguale a quello del form control quantita falda.
 * Esegue anche un controllo sulla valorizzazione della quantità in base all'unità di misura scelta.
 * @param quantitaFormControlName string nome del form control per la quantita.
 * @param quantitaFaldaFormControlName string nome del form control per la quantita falda.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const valoreQuantitaFaldaValidator = (
  quantitaFormControlName: string,
  quantitaFaldaFormControlName: string
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
    const valueQuantita = checkValidationFormControlByFormGroup(
      formGroup,
      quantitaFormControlName
    );
    const valueQuantitaFalda = checkValidationFormControlByFormGroup(
      formGroup,
      quantitaFaldaFormControlName
    );

    // Verifico i dati
    if (valueQuantita === false && valueQuantitaFalda === false) {
      // Nessun controllo
      return;
    }

    // Verifico se esiste la quantità falda profonda
    const qtaFPExist = valueQuantitaFalda !== false && valueQuantitaFalda >= 0;
    const qtaExist = valueQuantita !== false && valueQuantita >= 0;
    // Definisco i check per l'errore
    const checkQta =
      (qtaFPExist && !qtaExist) || valueQuantita < valueQuantitaFalda;

    // Verifico se esiste la quantita falda profonda, MA NON ESISTE la quantita
    if (checkQta) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.QUANT_FALD_PROF_INVALID;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Nessun errore
    return;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il form control quantita sia come valore maggiore uguale a quello del form control quantita falda.
 * Esegue anche un controllo sulla valorizzazione della quantità in base all'unità di misura scelta.
 * @param quantitaFaldaFormControlName string nome del form control per la quantita falda.
 * @param percentualeAumentiFormControlName string nome del form control per la percentuale aumenti.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const valoreQuantitaFaldaEAumenti = (
  quantitaFaldaFormControlName: string,
  percentualeAumentiFormControlName: string
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
    const qtaFaldaProfFCN = quantitaFaldaFormControlName;
    const percAumentiFCN = percentualeAumentiFormControlName;

    // Recupero i valori dal form
    let qtaFaldaProf: number;
    qtaFaldaProf = formGroup.get(qtaFaldaProfFCN)?.value;
    let percAumenti: RiduzioneAumentoVo[];
    percAumenti = formGroup.get(percAumentiFCN)?.value;

    // Verifico i dati
    if (!percAumenti || percAumenti.length == 0) {
      // Nessun controllo
      return;
    }

    // Esistono delle percentuali di aumento, verifico se c'è uno della lista
    const aumentiQtaObbl = [CodiciRiduzioniAumenti.M1];
    // Verifico se all'interno degli aumenti scelti esiste almeno uno della lista
    const sameCodes = intersectionWith(
      percAumenti,
      aumentiQtaObbl,
      (a: RiduzioneAumentoVo, b: string) => {
        // Verifco per il codice
        return a.sigla_riduzione_aumento == b;
      }
    );

    // Definisco i check per la gestione degli errori
    const check1 = qtaFaldaProf == undefined || qtaFaldaProf == 0;
    const check2 = sameCodes && sameCodes.length > 0;

    // Verifico se esiste la quantita falda profonda, MA NON ESISTE la quantita
    if (check1 && check2) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.QUANT_FALD_PROF_AUM_INVALID;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Nessun errore
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il form control quantita sia congruo con il tipo di unità di misura.
 * @param quantitaFormControlName string nome del form control per la quantita.
 * @param unitaDiMisuraFormControlName string nome del form control per l'unità di misura.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const unitaMisuraQuantitaValidator = (
  quantitaFormControlName: string,
  unitaDiMisuraFormControlName: string
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
    const valueQuantita = checkValidationFormControlByFormGroup(
      formGroup,
      quantitaFormControlName
    );
    const unitaDiMisuraObj = checkValidationFormControlByFormGroup(
      formGroup,
      unitaDiMisuraFormControlName
    );

    // Verifico che esista l'unità di misura (-1 è un id placeholder definito a livello di codice FE)
    if (unitaDiMisuraObj && unitaDiMisuraObj.id_unita_misura === -1) {
      // Il controllo va bloccato
      return null;
    }

    // Recupero l'unità di misura dall'oggetto
    const unitaDiMisura: number = unitaDiMisuraObj.id_unita_misura;

    // Definisco le condizioni per il controllo sull'errore
    const isEuroLitriSecondo =
      unitaDiMisura === IdUnitaDiMisura.euroLitriAlSecondo;
    const notValueQnt = valueQuantita === false;

    // Se l’uso selezionato ha unità di misura diversa da “€ l/sec” la quantità è un dato obbligatorio e deve essere maggiore o uguale a 0 (zero).
    if (!isEuroLitriSecondo && notValueQnt) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.QUANTITA_RIS_IDRICHE_PER_UNITA;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    }

    // Nessun errore
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che se è stata selezionata la condizione di Riduzione R5, la data scadenza EMAS o ISO è obbligatoria
 * @param dataScadenzaEmasIsoFormControlName string nome del form control per la data scadenza emas iso.
 * @param percRiduzioneFormControlName string nome del form control per la percentuale riduzione.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const valoreDataEmasIsoRiduzioneR5 = (
  dataScadenzaEmasIsoFormControlName: string,
  percRiduzioneFormControlName: string
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
    const valuedataScadenzaEmasIso = checkValidationFormControlByFormGroup(
      formGroup,
      dataScadenzaEmasIsoFormControlName
    );
    const valuePercRiduzione = checkValidationFormControlByFormGroup(
      formGroup,
      percRiduzioneFormControlName
    );

    // Verifico che esista la struttura dati
    if (!valuePercRiduzione) {
      // Nessun controllo da fare
      return null;
    }

    // Definisco delle variabili di comodo
    const dataEINotDef = !valuedataScadenzaEmasIso;
    const rWithF5 = valuePercRiduzione?.some((rVo: RiduzioneAumentoVo) => {
      // Verifico per la riduzione R5
      return rVo.sigla_riduzione_aumento == CodiciRiduzioniAumenti.R5;
    });

    // Se valuedataScadenzaEmasIso è valorizzata e valuePercRiduzione contiene l'R5
    if (dataEINotDef && rWithF5) {
      // Recupero la chiave d'errore
      const key = ERR_KEY.DATA_SCADENZA_EMAS_ISO_R5;
      // Genero e ritorno l'errore
      return generaObjErrore(key);
    } else {
      // La form è valida
      return null;
    }
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che se è stato definito il campo "canone" o il campo "canone unitario", l'altro campo è obbligatorio che sia valorizzato.
 * @param canoneFormControlName string nome del form control del campo.
 * @param canoneUnitarioFormControlName string nome del form control del campo.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const canoneECanoneUnitario = (
  canoneFormControlName: string,
  canoneUnitarioFormControlName: string
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
    const canoneFCN = canoneFormControlName;
    const canoneUFCN = canoneUnitarioFormControlName;

    // Recupero i valori dal form control tramite form group
    const canone = formGroup.get(canoneFCN)?.value;
    const canoneU = formGroup.get(canoneUFCN)?.value;

    // Definisco le condizioni di controllo
    const isCanoneEval = canone !== undefined && canone !== null;
    const isCanoneUEval = canoneU !== undefined && canoneU !== null;
    // Definisco i casi in cui vanno bene i valori
    const case1 = isCanoneEval && isCanoneUEval;
    const case2 = !isCanoneEval && !isCanoneUEval;

    // Verifico le casistiche
    if (case1 || case2) {
      // Controllo passato
      return null;
    }

    // Recupero la chiave d'errore
    const key = ERR_KEY.CANONE_E_CANONE_UNITARIO_REQUIRED;
    // Genero e ritorno l'errore
    return generaObjErrore(key);
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che gli usi di legge siano corretti.
 * @param usiFormControlName string nome del form control del campo.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const usiDiLeggeValidi = (usiFormControlName: string) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      return;
    }

    // Variabili di comodo
    const usiFCN = usiFormControlName;

    // Recupero i valori dal form control tramite form group
    const usi: UsoLeggePSDAmbienteInfo[] = formGroup.get(usiFCN)?.value ?? [];
    // Creo un oggetto dinamico fatto da chiave valore che conterrà i possibili errori
    let errors: DynamicObjAny = {};

    // Gestisco la casistica per gli usi validi secondo il canone
    const canoniErrors: DynamicObjAny = verificaCanoniUsi(usi);

    // Unisco le varie proprietà che definiscono gli errori per gli usi
    errors = { ...canoniErrors };

    // Se l'oggetto contenente gli errori è vuoto, allora è tutto a posto
    if (isEmptyObject(errors)) {
      // Imposto a null il valore, che viene considerato come "nessun errore"
      errors = null;
    }

    // Ritorno il valore con gli errori o, se tutto oke, null
    return errors;
  };
};

/**
 * Funzione di comodo che verifica che gli usi di legge siano validi.
 * Il controllo di questa funzione è dedicata ai canoni degli usi di legge.
 * Un uso è corretto quando: canone e canone unitario sono definiti.
 * @param usiLegge UsoLeggePSDInfo[] con gli usi da verificare.
 * @returns DynamicObjAny con chiave/valore per gli errori sugli usi.
 */
const verificaCanoniUsi = (
  usiLegge: UsoLeggePSDAmbienteInfo[]
): DynamicObjAny => {
  // Verifico l'input
  if (!usiLegge) {
    // Niente da verificare
    return {};
  }

  // Definisco la costante contenente le chiavi d'errore per i forms validators
  const ERR_KEY = RiscaErrorKeys;

  // Definisco un contenitore per gli errori
  const errors: DynamicObjAny = {};
  const usiErrKey = ERR_KEY.USI_INVALIDI_CANONE;

  // Itero ogni uso di legge e gestisco il controllo
  usiLegge.forEach((uso: UsoLeggePSDAmbienteInfo) => {
    // Recupero i valori del canone dall'uso
    const { usoDiLegge, canoneUso, canoneUnitarioUso } = uso || {};

    // Entrambe le informazioni devono essere definite
    if (canoneUso == undefined || canoneUnitarioUso == undefined) {
      // L'uso non è da considerarsi valido, verifico se già esiste l'oggetto di dettaglio
      if (!errors[usiErrKey]) {
        // Creo la proprietà con i dettagli
        errors[usiErrKey] = {};
        // #
      }

      // Recupero il codice dell'uso di legge per censirlo
      const codiceUso = usoDiLegge.cod_tipo_uso;
      // Aggiungo ai dati di dettaglio per gli usi invalidi codice + oggetto
      errors[usiErrKey][codiceUso] = usoDiLegge;
      // #
    }
  });

  // Ritorno l'oggetto d'errore
  return errors;
};
