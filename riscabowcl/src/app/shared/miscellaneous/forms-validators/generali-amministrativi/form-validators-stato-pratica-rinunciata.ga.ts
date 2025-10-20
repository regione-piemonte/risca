import { ValidationErrors } from '@angular/forms';
import { IstanzeProvvedimentiTable } from '../../../classes/risca-table/generali-amministrativi/generali-amministrativi.istanze-provvedimenti.table';
import { RiscaTableDataConfig } from '../../../components/risca/risca-table/utilities/risca-table.classes';
import {
  convertNgbDateStructToMoment,
  isIstanza,
  isProvvedimento,
  sortMoments,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  CodiciIstanzaAmbiente,
  CodiciProvvedimentoAmbiente,
  ICheckValidators,
  NuovaIstanzaFormData,
  NuovoProvvedimentoFormData,
  RiscaSortTypes,
  TNIPFormData,
} from '../../../utilities';
import { RiscaErrorKeys } from '../../../utilities/classes/errors-keys';
import { checkValidatorsResult } from '../forms-validators';

/**
 * Funzione di supporto che effettua i check di verifica per lo stato riscossione in caso di rinuncia.
 * Dal documento di analisi (19/07/2022 - WP2-2.2-USR-V07-US005_GestioneRiscossione-Dati Generali e Amministrativi.docx):
 * - Deve essere obbligatoriamente inserita un'istanza all'interno della tabella, questa istanza deve la tipologia: "Rinuncia totale" e con data più recente rispetto alle altre istanze.
 * ##############################################
 * AGGIORNAMENTO JIRA: RISCA-455 (10/11/2022)
 * - Deve essere obbligatoriamente inserita un'istanza OPPURE un provvedimento all'interno della tabella, questa istanza deve la tipologia: "Rinuncia totale" e con data più recente rispetto alle altre istanze O provvedimenti.
 * NOTE: il controllo deve essere fatto sia per istanze che provvedimenti. Il tipo di controllo è il medesimo.
 * @param istanzeProvvedimentiTable IstanzeProvvedimentiTable contenente l'oggetto che gestisce le informazioni della tabella per istanze e provvedimenti.
 * @returns ICheckValidators che definisce se il check di controllo è passato (valdi: true) o non passato (valid: false), è con l'aggiunta di un messaggio d'errore.
 */
export const checkStatoRiscossioneRinunciata = (
  istanzeProvvedimentiTable: IstanzeProvvedimentiTable
): ICheckValidators => {
  // Costanti per la gestione delle logiche
  const ERR_KEY = RiscaErrorKeys;
  // Variabili di comodo per la funzione
  const ipTable = istanzeProvvedimentiTable;

  // Tento di recuperare le informazioni dalla tabella
  const ipsT = ipTable?.source as RiscaTableDataConfig<TNIPFormData>[];
  // Verifico che esistano i dati
  if (ipsT == null || ipsT.length === 0) {
    // Non esistono dati, è errato
    const key = ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_ISTANZA;
    // Genero e ritorno l'errore
    return { valid: false, error: key };
  }

  // Lancio il check sia per istanze che per provvedimenti
  const iCheck: ICheckValidators = checkSRRIstanze(ipsT);
  const pCheck: ICheckValidators = checkSRRProvvedimenti(ipsT);

  // 1) Verifico se almeno uno dei due check è andato a buon fine
  if (iCheck.valid) {
    // Ritorno l'oggetto valid
    return iCheck;
  }
  if (pCheck.valid) {
    // Ritorno l'oggetto valid
    return pCheck;
  }

  // 2) Verifico quale dei due controlli è arrivato "più in profondità"
  if (iCheck.error === ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_TIPO) {
    // Ritorno l'oggetto d'errore
    return iCheck;
  }
  if (pCheck.error === ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_TIPO) {
    // Ritorno l'oggetto d'errore
    return pCheck;
  }

  // 3) Verifico quale dei due controlli ha il controllo base
  if (iCheck.error === ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_ISTANZA) {
    // Ritorno l'oggetto d'errore
    return iCheck;
  }
  if (pCheck.error === ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_ISTANZA) {
    // Ritorno l'oggetto d'errore
    return pCheck;
  }

  // 4) Return di default, non dovrebbe mai arrivare qui
  return iCheck ?? pCheck;
};

/**
 * Funzione di supporto che effettua i check di verifica per lo stato riscossione in caso di rinuncia per le istanze.
 * @param istanzeProvvedimentiData RiscaTableDataConfig<TNIPFormData>[] contenente la lista di dati della tabella istanze provvedimenti.
 * @returns ICheckValidators che definisce se il check di controllo è passato (valdi: true) o non passato (valid: false), con l'aggiunta di un messaggio d'errore.
 */
export const checkSRRIstanze = (
  istanzeProvvedimentiData: RiscaTableDataConfig<TNIPFormData>[]
): ICheckValidators => {
  // Costanti per la gestione delle logiche
  const ERR_KEY = RiscaErrorKeys;

  // 1) Filtro una prima volta le informazioni per ottenere solo le istanze
  const istanzeT = istanzeProvvedimentiData.filter(
    (ip: RiscaTableDataConfig<TNIPFormData>) => {
      // Verifico l'oggetto della tabella è istanza o provvedimento
      return isIstanza(ip.original);
      // #
    }
  ) as RiscaTableDataConfig<NuovaIstanzaFormData>[];
  // Verifico se non esistono istanze nella tabella
  if (istanzeT.length === 0) {
    // Non ci sono istanze, quindi il check è fallito
    const key = ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_ISTANZA;
    // Genero e ritorno l'errore
    return { valid: false, error: key };
  }

  // 2) Recupero gli oggetti specifici delle istanze
  let istanze: NuovaIstanzaFormData[];
  istanze = istanzeT.map((iT: RiscaTableDataConfig<NuovaIstanzaFormData>) => {
    // Ritorno la proprietà "origin"
    return iT.original;
  });

  // 3) Ordino le istanze per la propria data
  istanze = istanze.sort((a: NuovaIstanzaFormData, b: NuovaIstanzaFormData) => {
    // Recupero le date e le converto
    const dataA = a.dataIstanza;
    const dataB = b.dataIstanza;
    // Converto le date in moment
    const dataAM = convertNgbDateStructToMoment(dataA);
    const dataBM = convertNgbDateStructToMoment(dataB);
    // Definisco il tipo d'ordinamento
    const desc = RiscaSortTypes.decrescente;
    // Richiamo la funzione di utility per la gestione del sort
    return sortMoments(dataAM, dataBM, desc);
  });

  // 4) Estraggo dalla lista ordinata per data il primo oggetto (quello con data più recente)
  const iRecente = istanze[0];
  // 5) Verifico se la sua tipologia è quella di RINUNCIA TOTALE
  const codTP = iRecente?.tipologiaIstanza?.cod_tipo_provvedimento ?? '';
  const isIRRT = codTP === CodiciIstanzaAmbiente.IST_RINUNCIA_TOT;

  // Verifico il risultato del check e ritorno la validazione
  if (isIRRT) {
    // Controllo passato, ritorno valido
    return { valid: true, error: '' };
    // #
  } else {
    // Controllo non passato, c'è l'errore sulle informazioni
    const key = ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_TIPO;
    // Genero e ritorno l'errore
    return { valid: false, error: key };
  }
};

/**
 * Funzione di supporto che effettua i check di verifica per lo stato riscossione in caso di rinuncia per i provvedimenti.
 * @param istanzeProvvedimentiData RiscaTableDataConfig<TNIPFormData>[] contenente la lista di dati della tabella istanze provvedimenti.
 * @returns ICheckValidators che definisce se il check di controllo è passato (valdi: true) o non passato (valid: false), con l'aggiunta di un messaggio d'errore.
 */
export const checkSRRProvvedimenti = (
  istanzeProvvedimentiData: RiscaTableDataConfig<TNIPFormData>[]
): ICheckValidators => {
  // Costanti per la gestione delle logiche
  const ERR_KEY = RiscaErrorKeys;

  // 1) Filtro una prima volta le informazioni per ottenere solo i provvedimenti
  const provvedimentiT = istanzeProvvedimentiData.filter(
    (ip: RiscaTableDataConfig<TNIPFormData>) => {
      // Verifico l'oggetto della tabella è istanza o provvedimento
      return isProvvedimento(ip.original);
      // #
    }
  ) as RiscaTableDataConfig<NuovoProvvedimentoFormData>[];
  // Verifico se non esistono provvedimenti nella tabella
  if (provvedimentiT.length === 0) {
    // Non ci sono provvedimenti, quindi il check è fallito
    const key = ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_ISTANZA;
    // Genero e ritorno l'errore
    return { valid: false, error: key };
  }

  // 2) Recupero gli oggetti specifici dei provvedimenti
  let provvedimenti: NuovoProvvedimentoFormData[];
  provvedimenti = provvedimentiT.map(
    (pT: RiscaTableDataConfig<NuovoProvvedimentoFormData>) => {
      // Ritorno la proprietà "origin"
      return pT.original;
    }
  );

  // 3) Ordino i provvedimenti per la propria data
  provvedimenti = provvedimenti.sort(
    (a: NuovoProvvedimentoFormData, b: NuovoProvvedimentoFormData) => {
      // Recupero le date e le converto
      const dataA = a.dataProvvedimento;
      const dataB = b.dataProvvedimento;
      // Converto le date in moment
      const dataAM = convertNgbDateStructToMoment(dataA);
      const dataBM = convertNgbDateStructToMoment(dataB);
      // Definisco il tipo d'ordinamento
      const desc = RiscaSortTypes.decrescente;
      // Richiamo la funzione di utility per la gestione del sort
      return sortMoments(dataAM, dataBM, desc);
    }
  );

  // 4) Estraggo dalla lista ordinata per data il primo oggetto (quello con data più recente)
  const pRecente = provvedimenti[0];
  // 5) Verifico se la sua tipologia è quella di RINUNCIA TOTALE
  const codTP = pRecente?.tipologiaProvvedimento?.cod_tipo_provvedimento ?? '';
  const isPRT = codTP === CodiciProvvedimentoAmbiente.RIN_TOTALE;

  // Verifico il risultato del check e ritorno la validazione
  if (isPRT) {
    // Controllo passato, ritorno valido
    return { valid: true, error: '' };
    // #
  } else {
    // Controllo non passato, c'è l'errore sulle informazioni
    const key = ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_TIPO;
    // Genero e ritorno l'errore
    return { valid: false, error: key };
  }
};

/**
 * Funzione di supporto che verifica e genera un oggetto per la gestione degli errori per le angular forms.
 * @param istanzeProvvedimentiTable IstanzeProvvedimentiTable contenente l'oggetto che gestisce le informazioni della tabella per istanze e provvedimenti.
 * @returns ValidationErrors | null che definisce se il check di controllo è passato (null) o non passato (oggetto d'errore).
 */
export const validatorStatoRiscossioneRinunciata = (
  istanzeProvvedimentiTable: IstanzeProvvedimentiTable
): ValidationErrors | null => {
  // Variabili di comodo
  const ipT = istanzeProvvedimentiTable;

  // Richiamo il check sulle logiche dei dati
  let validSRR: ICheckValidators;
  validSRR = checkStatoRiscossioneRinunciata(ipT);

  // Utilizzo la funzione di comodo per gestire il return
  return checkValidatorsResult(validSRR);
};
