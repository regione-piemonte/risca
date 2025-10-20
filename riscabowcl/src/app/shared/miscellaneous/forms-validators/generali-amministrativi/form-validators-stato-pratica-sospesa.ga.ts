import { FormGroup, ValidationErrors } from '@angular/forms';
import * as moment from 'moment';
import { convertNgbDateStructToMoment } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import { ICheckValidators } from '../../../utilities';
import { RiscaErrorKeys } from '../../../utilities/classes/errors-keys';
import {
  checkValidationFormControlByFormGroup,
  checkValidatorsResult,
} from '../forms-validators';

/**
 * Funzione di supporto che effettua i check di verifica per lo stato riscossione in caso di sospensione.
 * Dal documento di analisi (19/07/2022 - WP2-2.2-USR-V07-US005_GestioneRiscossione-Dati Generali e Amministrativi.docx):
 * - La data d'inizio e fine sospensione DEVONO esistere e "oggi" deve essere compreso tra queste due date.
 * Il compreso prevede che le tre date possano accavallarsi, ci sarà poi un altro controllo che prevede che la data d'inizio sia minore della data di fine.
 * @param formGroup FormGroup dalla quale estrarre le informazioni per il controllo.
 * @param dataInizioSospensioneFormControlName string che definisce il form control name del campo data inizio sospensione, all'interno dell'oggetto FormGroup.
 * @param dataFineSospensioneFormControlName string che definisce il form control name del campo data inizio sospensione, all'interno dell'oggetto FormGroup.
 * @param checkStatoAttiva boolean che definisce se il check deriva dalla pratica. Per default è false ed identifica il check diretto per lo stato SOSPESA.
 * @returns ICheckValidators che definisce se il check di controllo è passato (valdi: true) o non passato (valid: false), è con l'aggiunta di un messaggio d'errore.
 */
export const checkStatoRiscossioneSospesa = (
  formGroup: FormGroup,
  dataInizioSospensioneFormControlName: string,
  dataFineSospensioneFormControlName: string,
): ICheckValidators => {
  // Costanti per la gestione delle logiche
  const DAY = 'day';
  const ERR_KEY = RiscaErrorKeys;
  const SRS_DT_SOSP = ERR_KEY.STATO_RISCOSSIONE_SOSPESA_DATA_SOSPENSIONE;
  // Variabili di comodo
  const f = formGroup;
  const disFCN = dataInizioSospensioneFormControlName;
  const dfsFCN = dataFineSospensioneFormControlName;

  // Estraggo il valore per le date
  const dis = checkValidationFormControlByFormGroup(f, disFCN);
  const dfs = checkValidationFormControlByFormGroup(f, dfsFCN);

  // Verifico se le date esistono
  if (!dis || !dfs) {
    // La date devono esistere
    return { valid: false, error: SRS_DT_SOSP };
  }

  // Converto le date in moment per poter verificarle (uso lo start of day per non incappare in problemi di orari)
  const dIniSM = convertNgbDateStructToMoment(dis)?.startOf(DAY);
  const dFinsM = convertNgbDateStructToMoment(dfs)?.startOf(DAY);
  // Verifico che le date generate siano valida
  const validDisM = dIniSM != undefined && moment(dIniSM).isValid();
  const validDfsM = dFinsM != undefined && moment(dFinsM).isValid();
  const validD = validDisM && validDfsM && dIniSM.isBefore(dFinsM);
  // Verifico che le date siano valide e la data inzio sia precedente della data di fine
  if (!validD) {
    // Ritorno come check fallito
    return { valid: false, error: SRS_DT_SOSP };
  }

  // Definisco la data di oggi (uso lo start of day per non incappare in problemi di orari)
  const oggi = moment().startOf(DAY);

  // Verifico se oggi è compreso tra le due date
  if (oggi.isBetween(dIniSM, dFinsM, undefined, '[]')) { // '[]' => estremo sinistro ed estremo destro compresi
    // La data è valida
    return { valid: true };
    // #
  } else {
    // La data non è valida
    return { valid: false, error: SRS_DT_SOSP };
  }
};

/**
 * Funzione di supporto che verifica e genera un oggetto per la gestione degli errori per le angular forms.
 * @param formGroup FormGroup dalla quale estrarre le informazioni per il controllo.
 * @param dataInizioSospensioneFormControlName string che definisce il form control name del campo data inizio sospensione, all'interno dell'oggetto FormGroup.
 * @param dataFineSospensioneFormControlName string che definisce il form control name del campo data inizio sospensione, all'interno dell'oggetto FormGroup.
 * @returns ValidationErrors | null che definisce se il check di controllo è passato (null) o non passato (oggetto d'errore).
 */
export const validatorStatoRiscossioneSospesa = (
  formGroup: FormGroup,
  dataInizioSospensioneFormControlName: string,
  dataFineSospensioneFormControlName: string
): ValidationErrors | null => {
  // Variabili di comodo
  const f = formGroup;
  const disFCN = dataInizioSospensioneFormControlName;
  const dfsFCN = dataFineSospensioneFormControlName;

  // Richiamo il check sulle logiche dei dati
  let validSRS: ICheckValidators;
  validSRS = checkStatoRiscossioneSospesa(f, disFCN, dfsFCN);

  // Utilizzo la funzione di comodo per gestire il return
  return checkValidatorsResult(validSRS);
};
