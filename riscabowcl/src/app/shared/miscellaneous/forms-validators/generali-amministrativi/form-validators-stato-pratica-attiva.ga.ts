import { FormGroup, ValidationErrors } from '@angular/forms';
import { IstanzeProvvedimentiTable } from '../../../classes/risca-table/generali-amministrativi/generali-amministrativi.istanze-provvedimenti.table';
import { ICheckValidators } from '../../../utilities';
import { RiscaErrorKeys } from '../../../utilities/classes/errors-keys';
import { checkValidatorsResult } from '../forms-validators';
import { checkStatoRiscossioneScaduta } from './form-validators-stato-pratica-scaduta.ga';

/**
 * Funzione di supporto che effettua i check di verifica per lo stato riscossione in caso di sospensione.
 * Dal documento di analisi (19/07/2022 - WP2-2.2-USR-V07-US005_GestioneRiscossione-Dati Generali e Amministrativi.docx):
 * - Una pratica può avere stato ATTIVA se tutti gli altri controlli sugli altri stati risultano "false".
 * @param formGroup FormGroup dalla quale estrarre le informazioni per il controllo.
 * @param dataInizioSospensioneFormControlName string con il form control name per la data inizio sospensione.
 * @param dataFineSospensioneFormControlName string con il form control name per la data fine sospensione.
 * @param dataFineConcessioneFormControlName string con il form control name per la data fine concessione.
 * @param istanzeProvvedimentiTable IstanzeProvvedimentiTable con le informzioni relative alla tabella delle istanze e provvedimenti.
 * @returns ICheckValidators che definisce se il check di controllo è passato (valdi: true) o non passato (valid: false), è con l'aggiunta di un messaggio d'errore.
 */
export const checkStatoRiscossioneAttiva = (
  formGroup: FormGroup,
  dataInizioSospensioneFormControlName: string,
  dataFineSospensioneFormControlName: string,
  dataFineConcessioneFormControlName: string,
  istanzeProvvedimentiTable: IstanzeProvvedimentiTable
): ICheckValidators => {
  // Costanti per la gestione delle logiche
  const ERR_KEY = RiscaErrorKeys;
  // Variabili di comodo
  const f = formGroup;
  const disFCN = dataInizioSospensioneFormControlName;
  const dfsFCN = dataFineSospensioneFormControlName;
  const dfcFCN = dataFineConcessioneFormControlName;
  const isTable = istanzeProvvedimentiTable;

  // Verifico le condizioni per gli altri stati della pratica
  // RISCA-592 => Richiesta rimozione delle logiche di controllo per lo stato: SOSPESA
  // const validSospesa = checkStatoRiscossioneSospesa(f, disFCN, dfsFCN).valid;
  // RISCA-632 => RIchiesta rimozione delle logiche di controllo per lo stato: RINUNCIATA
  // const validRinunciata = checkStatoRiscossioneRinunciata(isTable).valid;
  const validScaduta = checkStatoRiscossioneScaduta(f, dfcFCN, isTable).valid;

  // Se anche uno solo degli stati è vero, allora la pratica NON può essere attiva
  if (/* validSospesa || */ validScaduta /* || validRinunciata */) {
    // C'è uno stato valido, non può essere attiva
    const key = ERR_KEY.STATO_RISCOSSIONE_ATTIVA;
    // Genero e ritorno l'errore
    return { valid: false, error: key };
    // #
  } else {
    // Il check è valido
    return { valid: true };
  }
};

/**
 * Funzione di supporto che verifica e genera un oggetto per la gestione degli errori per le angular forms.
 * @param formGroup FormGroup dalla quale estrarre le informazioni per il controllo.
 * @param dataInizioSospensioneFormControlName string con il form control name per la data inizio sospensione.
 * @param dataFineSospensioneFormControlName string con il form control name per la data fine sospensione.
 * @param dataFineConcessioneFormControlName string con il form control name per la data fine concessione.
 * @param istanzeProvvedimentiTable IstanzeProvvedimentiTable con le informzioni relative alla tabella delle istanze e provvedimenti.
 * @returns ValidationErrors | null che definisce se il check di controllo è passato (null) o non passato (oggetto d'errore).
 */
export const validatorStatoRiscossioneAttiva = (
  formGroup: FormGroup,
  dataInizioSospensioneFormControlName: string,
  dataFineSospensioneFormControlName: string,
  dataFineConcessioneFormControlName: string,
  istanzeProvvedimentiTable: IstanzeProvvedimentiTable
): ValidationErrors | null => {
  // Variabili di comodo
  const f = formGroup;
  const disFCN = dataInizioSospensioneFormControlName;
  const dfsFCN = dataFineSospensioneFormControlName;
  const dfcFCN = dataFineConcessioneFormControlName;
  const isTable = istanzeProvvedimentiTable;

  // Richiamo il check sulle logiche dei dati
  let validSRA: ICheckValidators;
  validSRA = checkStatoRiscossioneAttiva(f, disFCN, dfsFCN, dfcFCN, isTable);

  // Utilizzo la funzione di comodo per gestire il return
  return checkValidatorsResult(validSRA);
};
