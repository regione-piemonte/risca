import { FormGroup, ValidationErrors } from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';
import { Moment } from 'moment';
import { IstanzeProvvedimentiTable } from '../../../classes/risca-table/generali-amministrativi/generali-amministrativi.istanze-provvedimenti.table';
import { convertNgbDateStructToMoment } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  CodiciIstanzaAmbiente,
  ICheckValidators,
  NuovaIstanzaFormData,
  NuovoProvvedimentoFormData,
  TNIPFormData,
} from '../../../utilities';
import { RiscaErrorKeys } from '../../../utilities/classes/errors-keys';
import {
  checkValidationFormControlByFormGroup,
  checkValidatorsResult,
} from '../forms-validators';
import { TipoIstanzaProvvedimentoVo } from '../../../../core/commons/vo/tipo-istanza-provvidemento-vo';

/**
 * Funzione di supporto che effettua i check di verifica per lo stato riscossione in caso di sospensione.
 * Dal documento di analisi (19/07/2022 - WP2-2.2-USR-V07-US005_GestioneRiscossione-Dati Generali e Amministrativi.docx):
 * 1) Data Scadenza Concessione deve risultare precedente alla data SYSDATE (la data in cui si effettua l'operazione, quindi un "oggi");
 * 2) La condizione 1 risulta vera e si verifica una delle seguenti condizioni (sono definite in OR):
 *  a) Non sono presenti istanze all'interno della tabella "istanze e provvedimenti";
 *  b) Sono presenti istanze all'interno della tabella "istanze e provvedimenti", ma nessuna istanza ha la "data istanza" definita;
 *  c) Sono presenti istanze all'interno della tabella "istanze e provvedimenti", ma nessuna istanza ha il tipo istanza del tipo richiesto dal controllo (vedi documento di analisi, che specifica il tipo d'istanza per il controllo);
 *  d) Sono presenti istanze all'interno della tabella "istanze e provvedimenti" e almeno un'istanza ha: il tipo istanza richiesto dal controllo, la data istanza è stata definita e la data istanza deve risultare precedente rispetto alla data scadenza concessione;
 * @param formGroup FormGroup dalla quale estrarre le informazioni per il controllo.
 * @param dataFineConcessioneFormControlName string che definisce il form control name del campo data fine concessione, all'interno dell'oggetto FormGroup.
 * @param istanzeProvvedimentiTable IstanzeProvvedimentiTable contenente l'oggetto che gestisce le informazioni della tabella per istanze e provvedimenti.
 * @returns ICheckValidators che definisce se il check di controllo è passato (valdi: true) o non passato (valid: false), è con l'aggiunta di un messaggio d'errore.
 */
export const checkStatoRiscossioneScaduta = (
  formGroup: FormGroup,
  dataFineConcessioneFormControlName: string,
  istanzeProvvedimentiTable: IstanzeProvvedimentiTable
): ICheckValidators => {
  // Costanti per la gestione delle logiche
  const DAY = 'day';
  const ERR_KEY = RiscaErrorKeys;
  const SRS_DT_CONC = ERR_KEY.STATO_RISCOSSIONE_SCADUTA_DATA_CONCESSIONE;
  const SRS_SCAD_IST = ERR_KEY.STATO_RISCOSSIONE_SCADUTA_ISTANZA;
  // Variabili di comodo
  const f = formGroup;
  const dfcFCN = dataFineConcessioneFormControlName;
  const ipT = istanzeProvvedimentiTable;

  // Definisco il contenitore per il valore del form control
  let dataScadenzaConcessione: NgbDateStruct;
  // Controllo che la data scadenza sospensione ci sia
  dataScadenzaConcessione = checkValidationFormControlByFormGroup(f, dfcFCN);

  // Verifico se non esiste la data
  if (!dataScadenzaConcessione) {
    // Nessun dato il check non passa
    return { valid: false, error: SRS_DT_CONC };
  }

  // Variabili di comodo per i controlli
  const oggi = moment().startOf(DAY);
  // Converto la data scadenza concessione in moment
  const dscM: Moment = convertNgbDateStructToMoment(dataScadenzaConcessione);
  // Imposto un default per l'orario per non avere problemi con la comparazione
  dscM.startOf(DAY);

  // Definisco la prima condizione: “Data Scadenza Concessione” < SYSDATE (data di oggi)
  const dscBeforeToday = dscM.isBefore(oggi);
  // Verifico se “Data Scadenza Concessione” >= SYSDATE (data di oggi) allora non è valida
  if (!dscBeforeToday) {
    // La giornata risulta dopo oggi
    return { valid: false, error: SRS_DT_CONC };
  }

  // 1) Devono esistere dati nella tabella
  const existIPT = ipT?.source?.length > 0;
  // Verifico ci sia almeno un elemento
  if (!existIPT) {
    // Non ci sono elementi, quindi il controllo è valido
    return { valid: true, error: '' };
  }

  // 2) Nella tabella esistono effettivamente dati
  // Definisco la lista di condizioni per la tipologia istanza
  const checkTI = [
    CodiciIstanzaAmbiente.IST_RINNOVO,
    CodiciIstanzaAmbiente.IST_SANATORIA,
    CodiciIstanzaAmbiente.AUT_PROVVISORIA,
    // RISCA-698 => Rimossi questi controlli - CodiciIstanzaAmbiente.IST_RINNOVO_PR_88_96,
    // RISCA-698 => Rimossi questi controlli - CodiciIstanzaAmbiente.IST_RICONOSCIMENTO,
  ];
  // CHECK 1 # Definisco una funzione che verifichi se il codice istanza è contenuto nell'array di controllo
  const istanzaProvvedimentoInCheckList = (
    i: NuovaIstanzaFormData | NuovoProvvedimentoFormData
  ) => {
    // Generalizzo l'input e cerco di estrarre l'oggetto TipoIstanzaProvvedimentoVo
    let iAny = i as any;
    // Tento di estrarre l'oggetto sia per istanza che provvedimento
    let tipologiaIstProv: TipoIstanzaProvvedimentoVo;
    tipologiaIstProv = iAny.tipologiaIstanza ?? iAny.tipologiaProvvedimento;

    // Definisco variabili di comodo
    const codTP = tipologiaIstProv?.cod_tipo_provvedimento;
    // Verifico che il codice esista
    if (!codTP) {
      // Nessun codice, non è un'istanza da verificare
      return false;
    }

    // Verifico se il codice istanza è uguale ad uno dei codici definiti dal check
    return checkTI.some((check) => check === codTP);
  };

  // Recupero l'array d'informazioni per il check (uso | any[] per forzare la tipizzazione successivamente)
  const righeIST: TNIPFormData[] | any[] = ipT.getDataSource();

  // Effettuo una serie di filtri sui dati, per ottenere una struttura con sole istanze richieste dal check e con date definite
  let istanzeForDateCheck: (
    | NuovaIstanzaFormData
    | NuovoProvvedimentoFormData
  )[];
  istanzeForDateCheck = righeIST
    /**
     * MODIFICA => RISCA-698 | Il controllo è da fare su istanze E provvedimenti
     * .filter((ip: TNIPFormData) => isIstanza(ip)) // Filtro i risultati recuperando solo gli oggetti istanza
     */
    /**
     * MODIFICA => RISCA-592 | La gestione sulle date non è più richiesta
     * .filter((i: NuovaIstanzaFormData) => i.dataIstanza !== undefined) // Filtro recuperando solo gli oggetti che hanno una data definita
     */
    .filter((i: NuovaIstanzaFormData | NuovoProvvedimentoFormData) => {
      // Filtro recuperando solo le istanze con dei codici istanza presenti nella lista di check
      return istanzaProvvedimentoInCheckList(i);
    });

  // Verifico se a seguito delle funzioni di filtro, sono state trovati istanze che rispettino le condizioni di verifica
  if (!istanzeForDateCheck && istanzeForDateCheck.length === 0) {
    // Non ci sono controlli da effettuare sulle istanze, questa casistica è da considerarsi corretta
    return { valid: true, error: '' };
  }

  /** 
    * MODIFICA => RISCA-592 | La gestione sulle date non è più richiesta
    * ####
      // CHECK 2 # Definisco una funzione che verifichi che la data istanza sia precedente alla data di scadenza concessione
      const istanzaDateValid = (i: NuovaIstanzaFormData): boolean => {
        // Definisco variabili di comodo
        const dataI = convertNgbDateStructToMoment(i?.dataIstanza);
        // Verifico l'input
        if (!dataI) {
          // Niente dato, la considero valido
          return true;
        }

        // Definisco una standardizzazione della data, per i controlli
        dataI.startOf(DAY);
        // Per essere un'istanza valida per lo stato scaduta, la data deve essere precedente a quella di scadenza
        return dataI.isBefore(dscM);
      };
      // Sono state trovate delle istanze che hanno i dati per il controllo, verifico quindi la data dell'istanza
      const istanzeInvalid = istanzeForDateCheck.some((i: NuovaIstanzaFormData) => {
        // Ritorno la verifica sulla data dell'istanza
        return !istanzaDateValid(i);
      });

      // Verifico se anche solo un'istanza è risultata invalida
      if (istanzeInvalid) {
        // C'è almeno un'istanza non valida
        return { valid: false, error: SRS_SCAD_IST };
      }
    * ###
  */

  // MODIFICA => RISCA-592 | Verifico se esiste anche una sola tra istanza di rinnovo o istanza di sanatoria
  if (istanzeForDateCheck && istanzeForDateCheck.length > 0) {
    // C'è almeno un'istanza non valida
    return { valid: false, error: SRS_SCAD_IST };
  }

  // Tutti i controlli superati
  return { valid: true, error: '' };
};

/**
 * Funzione di supporto che verifica e genera un oggetto per la gestione degli errori per le angular forms.
 * @param formGroup FormGroup dalla quale estrarre le informazioni per il controllo.
 * @param dataFineConcessioneFormControlName string che definisce il form control name del campo data fine concessione, all'interno dell'oggetto FormGroup.
 * @param istanzeProvvedimentiTable IstanzeProvvedimentiTable contenente l'oggetto che gestisce le informazioni della tabella per istanze e provvedimenti.
 * @returns ValidationErrors | null che definisce se il check di controllo è passato (null) o non passato (oggetto d'errore).
 */
export const validatorStatoRiscossioneScaduta = (
  formGroup: FormGroup,
  dataFineConcessioneFormControlName: string,
  istanzeProvvedimentiTable: IstanzeProvvedimentiTable
): ValidationErrors | null => {
  // Variabili di comodo
  const f = formGroup;
  const dfcFCN = dataFineConcessioneFormControlName;
  const ipT = istanzeProvvedimentiTable;

  // Richiamo il check sulle logiche dei dati
  let validSRS: ICheckValidators;
  validSRS = checkStatoRiscossioneScaduta(f, dfcFCN, ipT);

  // Utilizzo la funzione di comodo per gestire il return
  return checkValidatorsResult(validSRS);
};
