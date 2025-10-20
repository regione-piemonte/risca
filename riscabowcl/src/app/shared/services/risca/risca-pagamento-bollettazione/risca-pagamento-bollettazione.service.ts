import { Injectable } from '@angular/core';
import { clone, compact, sumBy } from 'lodash';
import { DettaglioPagSearchResultVo } from '../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import {
  IPagamentoNonProprioVo,
  PagamentoNonProprioVo,
} from '../../../../core/commons/vo/pagamento-non-proprio-vo';
import { PagamentoVo } from '../../../../core/commons/vo/pagamento-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import {
  ITipoImpNonProprioVo,
  TipiImpNonProprioVo,
} from '../../../../core/commons/vo/tipo-importo-non-proprio-vo';
import {
  IAggiornaPagNonPropri,
  IAutoImportoVersatoSD,
  IGeneraPagamentoNonProprio,
  IImportiMancantiEccedentiSDAsImportoPagamento,
  IPagamentoBollettazioneForm,
  IVerificaImporti,
  IVerificaImportiResult,
} from '../../../components/risca/risca-pagamento-bollettazione/utilities/risca-pagamento-bollettazione.interfaces';
import { RiscaNotifyCodes } from '../../../utilities/enums/risca-notify-codes.enums';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';

/**
 * Servizio di utility con funzionalità a supporto del componente omonimo.
 */
@Injectable({ providedIn: 'root' })
export class RiscaPagamentoBollettazioneService {
  /** TipiImpNonPropriVo con le informazioni costanti riguardanti i tipi non propri. */
  TINP_C = new TipiImpNonProprioVo();

  /**
   * Costruttore.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * #######################################################
   * GESTIONE VERIFICHE E AUTOMATIZZAZIONI IMPORTI PAGAMENTO
   * #######################################################
   */

  /**
   * Funzione di controllo che verifica i dati in ottica di "automatizzazione" delle informazioni.
   * La casistica ha le seguenti regole per essere vera:
   * - L'utente ha selezionato un solo stato debitorio dalla ricerca stati debitori;
   * - L'utente non ha compilato "importo_versato" dell'unico stato debitorio selezionato;
   * - L'utente non ha compilato nessun campo per gli importi "non propri".
   * Se si verificano queste casistiche, "importo_versato" dell'unico stato debitorio sarà uguale a "importo_versato" dell'oggetto pagamento.
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @returns IAutoImportoVersatoSD con le informazioni generate a seguito della verifica.
   */
  autoImportoStatoDebitorio(data: IVerificaImporti): IAutoImportoVersatoSD {
    // Definisco l'oggetto di ritorno per la funzione
    const result: IAutoImportoVersatoSD = { verified: false };
    // Verifico l'input
    if (!data) {
      // Nessuna configurazione
      return result;
    }

    // Estraggo le informazioni dall'oggetto in input
    const {
      importoPagamento,
      statiDebitoriSel,
      dettagliPagamento,
      importoDaRimborsare,
      importoNonDiCompetenza,
      importoNonIdentificato,
    } = data;

    // Creo dei check di verifica
    const notExistSD = !statiDebitoriSel;
    const existDP = dettagliPagamento?.length > 0;
    // Verifico che esistano le informazioni per gli stati debitori
    if (notExistSD || existDP) {
      // Non ci sono le condizioni per l'auto valorizzazione
      return result;
    }

    // Esistono gli stati debitori verifico se esiste un solo elemento
    if (statiDebitoriSel.length === 1) {
      // Estraggo lo stato debitorio dalla lista
      const sd: StatoDebitorioVo = statiDebitoriSel[0];
      // Esiste un solo stato debitorio, verifico se non è stato definito "importo_versato"
      const isEmptyIV = !sd.importo_versato;
      // Verifico se anche gli importi non propri non sono stati definiti
      const isEmptyIDR = !importoDaRimborsare;
      const isEmptyINDC = !importoNonDiCompetenza;
      const isEmptyINI = !importoNonIdentificato;

      // Verifico se tutte le informazioni non sono state definite
      if (isEmptyIV && isEmptyIDR && isEmptyINDC && isEmptyINI) {
        // Nessuna informazione impostata, aggiorno lo stato debitorio con il valore dell'importo del pagamento
        sd.importo_versato = importoPagamento;
        // Aggiorno l'oggetto di ritorno
        result.verified = true;
        result.statoDebitorioUpd = sd;
      }
    }

    // Ritorno il risultato finale generato
    return result;
  }

  /**
   * A MEMORIA STORICA: RISCA-757.
   * Funzione di controllo che verifica i dati in ottica di "automatizzazione" delle informazioni.
   * La casistica ha le seguenti regole per essere vera:
   * - Almeno 2 stati debitori;
   * - Importi non propri a 0 o non definiti (tutti);
   * - Nessuna input "importo_versato" valorizzata per gli stati debitori;
   * - PROVVISORIO: RISCA-757 => Non devono esistere dettagli pagamento per il pagamento stesso.
   * Se si verificano queste casistiche, valorizzare ogni input "importo_versato" degli stati debitori con:
   * - Il valore "imp_mancante_imp_eccedente" dello stato debitorio in valore assoluto, se "imp_mancante_imp_eccedente" è negativo.
   * - Il valore 0, se "imp_mancante_imp_eccedente" è positivo.
   * SCALANDO progressivamente dal valore totale del pagamento, per ogni stato debitorio.
   * Quando la "scalatura" rende 0 il totale del pagamento, tutti i restanti stati debitori saranno valorizzati con 0.
   * Se la "scalatura" è parziale rispetto a "imp_mancante_imp_eccedente", definire il valore parziale.
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @returns IImportiMancantiEccedentiSDAsImportoPagamento con le informazioni generate a seguito della verifica.
   * @summary Sivlia Cordero aveva ipotizzato un'alternativa per la funzione "importiMancantiUgualeImportoPagamento", ma con le regole descritte in questo commento. Per il momento mantengo le regole, in futuro se viene completamente abbandonata la questione, verrà rimosso.
   */

  /**
   * Funzione di controllo che verifica i dati in ottica di "automatizzazione" delle informazioni.
   * La casistica ha le seguenti regole per essere vera:
   * - Almeno 2 stati debitori;
   * - Importi non propri a 0 o non definiti (tutti);
   * - Nessuna input "importo_versato" valorizzata per gli stati debitori;
   * - MODIFICA: RISCA-799 => Ogni campo "importo mancante con interessi" dello stato debitorio (StatoDebitorio.importoMancanteConInteressi) deve essere > 0;
   * - PROVVISORIO: RISCA-757 => Non devono esistere dettagli pagamento per il pagamento stesso.
   * - Importo totale pagato (in alto) - (SOMMA di ogni [StatoDebitorio.importoMancanteConInteressi]) = 0;
   * Se si verificano queste casistiche, valorizzare ogni input "importo_versato" con il valore (StatoDebitorio.importoMancanteConInteressi) dello stato debitorio.
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @returns IImportiMancantiEccedentiSDAsImportoPagamento con le informazioni generate a seguito della verifica.
   */
  importiMancantiUgualeImportoPagamento(
    data: IVerificaImporti
  ): IImportiMancantiEccedentiSDAsImportoPagamento {
    // Definisco l'oggetto di ritorno per la funzione
    let result: IImportiMancantiEccedentiSDAsImportoPagamento;
    result = { verified: false };
    // Verifico l'input
    if (!data) {
      // Nessuna configurazione
      return result;
    }

    // Estraggo le informazioni dall'oggetto in input
    const {
      importoPagamento,
      statiDebitoriSel,
      dettagliPagamento,
      importoDaRimborsare,
      importoNonDiCompetenza,
      importoNonIdentificato,
    } = data;

    // Definisco le regole per l'automatismo
    // #1 - Almeno 2 stati debitori
    const qtaSD2OrMore = statiDebitoriSel?.length >= 2;
    // Verifico
    if (!qtaSD2OrMore) {
      // Ritorno il risultato
      return result;
    }

    // #2 - Importi non propri a 0 o non definiti
    const impDaRimb0OrUndef =
      importoDaRimborsare == undefined || importoDaRimborsare == 0;
    const impNonComp0OrUndef =
      importoNonDiCompetenza == undefined || importoNonDiCompetenza == 0;
    const impNonIdent0OrUndef =
      importoNonIdentificato == undefined || importoNonIdentificato == 0;
    // Verifico
    if (!impDaRimb0OrUndef || !impNonComp0OrUndef || !impNonIdent0OrUndef) {
      // Ritorno il risultato
      return result;
    }

    // #3 - Stati debitori selezionati senza importo definito
    let allImportoVersatoSDAs0: boolean;
    allImportoVersatoSDAs0 = statiDebitoriSel.every(
      (sdSel: StatoDebitorioVo) => {
        // Ogni importo versato deve essere 0 o non definito
        const impVersNotDef = sdSel?.importo_versato == undefined;
        const impVersAs0 = sdSel?.importo_versato === 0;
        // Ritorno l'insieme delle condizioni
        return impVersNotDef || impVersAs0;
      }
    );
    // Verifico
    if (!allImportoVersatoSDAs0) {
      // Ritorno il risultato
      return result;
    }

    // 4# - Tutti gli importi (sd.StatoDebitorio.importoMancanteConInteressi) devono essere maggiori di 0
    let allImpManConIntGreater0: boolean;
    allImpManConIntGreater0 = statiDebitoriSel.every(
      (sdSel: StatoDebitorioVo) => {
        // Estraggo le informazioni per la verifica
        let importoMancanteConInteressi: number;
        importoMancanteConInteressi = sdSel.importoMancanteConInteressi;
        // Ogni importo mancante con interessi deve essere maggiore di 0
        const impMancInterssiGreat0 = importoMancanteConInteressi > 0;
        // Verifico e ritorno
        return impMancInterssiGreat0;
        // #
      }
    );

    // Verifico la condizione definita sopra
    if (!allImpManConIntGreater0) {
      // Ritorno il risultato
      return result;
    }

    // 5# - PROVVISORIO - Non devono esistere dettagli pagamento per il pagamento
    const noDettPags: boolean =
      !dettagliPagamento || dettagliPagamento?.length === 0;
    // Verifico
    if (!noDettPags) {
      // Ritorno il risultato
      return result;
    }

    // 6# - Il totale del pagamento è equivalente alla somma di tutti gli importi (sd.importoMancanteConInteressi) degli stati debitori selezionati
    const totalePag: number = importoPagamento;
    let totaleImpMancInteressi: number = 0;
    statiDebitoriSel.forEach((sdSel: StatoDebitorioVo) => {
      // NOTA BENE: arrivati a questo punto, tutti i valori per (sd.importoMancanteConInteressi) dovrebbero essere positivi
      let importoMancanteConInteressi: number;
      importoMancanteConInteressi = sdSel.importoMancanteConInteressi;
      // Effettuo una somma degli importi mancanti/interessi
      totaleImpMancInteressi =
        totaleImpMancInteressi + importoMancanteConInteressi;
      // #
    });
    // Verifico che la differenza tra l'importo pagamento e la somma degli importi mancanti/interessi dia 0
    const importoPagamentoAsImpMancInteressi =
      totalePag - totaleImpMancInteressi;
    if (importoPagamentoAsImpMancInteressi !== 0) {
      // Ritorno il risultato
      return result;
    }

    // Tutti i controlli sono stati passati, assegno all'importo versato di ogni stato debitorio, il valore dell'importo mancante/interessi
    let statiDebitoriUpd: StatoDebitorioVo[];
    statiDebitoriUpd = statiDebitoriSel.map((sdSel: StatoDebitorioVo) => {
      // Definisco un nuovo stato debitorio
      const newSD = clone(sdSel);

      // Recupero l'importo mancante eccedente e lo faccio diventare positivo
      let impMancInteressi: number;
      impMancInteressi = sdSel.importoMancanteConInteressi;
      // Assegno l'importo positivo all'importo versato dello stato debitorio
      newSD.importo_versato = impMancInteressi;

      // Ritorno il nuovo oggetto dello stato debitorio
      return newSD;
      // #
    });

    // Aggiorno il risultato dell'operazione
    result = { verified: true, statiDebitoriUpd };

    // Ritorno il risultato finale generato
    return result;
  }

  /**
   * Funzione che prende in input una serie d'informazioni e a seconda della configurazione di quest'ultime, genera in output un set di dati per pilotare le logiche del componente.
   * All'interno della funzione verranno effettuati controlli di gestione sui dati.
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @returns IVerificaImportiResult con il risultato delle verifiche.
   */
  verificaImporti(data: IVerificaImporti): IVerificaImportiResult {
    // Definisco il contenitore per il risultato di default
    const result: IVerificaImportiResult = {
      validated: false,
      notifica: RiscaNotifyCodes.E096,
    };
    // Verifico l'input
    if (!data) {
      // Niente configurazione
      return result;
    }

    /**
     * RISCA-ISSUES-50: E' stata richiesta la modifica sulla verifica per i dati del pagamento.
     * Nei pagamenti da visionare, la mancanza completa dei dati deve generare errore.
     * Nel dettaglio pagamento invece la mancanza completa dei dati è concessa, a patto che si informi l'utente che così facendo il pagamento tornerà in stato "da visionare".
     */
    // Verifico se non ci sono dati definiti per gli importi
    // [VF] Issue 88 - si richiede di poter inserire il valore 0 sull'importo versato per sd
    // e/o sugli importi versati nei dettagli di pagamento se si ricerca un pagamento precedentemente assegnato
    const noAmounts: boolean = this.verificaImportiNessunImporto(data);
    // Verifico il risultato
    if (noAmounts) {
      // L'utente non ha inserito nemmeno un'informazione riguardo gli importi, verifico se sta gestendo un "pagamento da visionare"
      if (!data.pagamentoDaVisionare) {
        // Sta gestendo un pagamento dal suo dettaglio, quindi la rimozione dei dati causerà il ritorno del pagamento nello stato "da visionare"
        result.validated = true;
        result.notifica = RiscaNotifyCodes.I047;
        // #
      }
      // Ritorno il risultato
      return result;
    }

    // Verifico se non ci sono dati definiti per gli importi stati debitori
    const allSDEvalueted: boolean = this.verificaImportiTuttiSD(data);
    // Verifico il risultato
    if (!allSDEvalueted) {
      // Ritorno l'oggetto di result
      return result;
    }

    // Verifico se non ci sono dati definiti per gli importi dettagli pagamento
    const allDPEvalueted: boolean = this.verificaImportiDettagliPagamento(data);
    // Verifico il risultato
    if (!allDPEvalueted) {
      // Ritorno l'oggetto di result
      return result;
    }

    // // RISCA-757: E' stato richiesta la rimozione di questo controllo.
    // // Verifico se gli importi dello stato debitorio sono maggiori del canone
    // const importiCanone: boolean = this.verficaImportiSDCanone(data);
    // // Verifico il risultato
    // if (!importiCanone) {
    //   // Modifico la lista di errori
    //   result.notifica = RiscaNotifyCodes.E082;
    //   // Ritorno il risultato
    //   return result;
    // }

    // // RISCA-799: Il controllo per gli stati debitori è da modificare con importi mancanti con interessi.
    // // RISCA-ISSUES-39: Richiesta di rimozione totale del controllo.
    // // Verifico se gli importi dello stato debitorio sono maggiori rispetto all'importo mancante con interessi
    // const importiMancantiInteressi: boolean =
    //   this.verficaImportiSDMancanteConInteressi(data);
    // // Verifico il risultato
    // if (!importiMancantiInteressi) {
    //   // Modifico la lista di errori
    //   result.notifica = RiscaNotifyCodes.E082;
    //   // Ritorno il risultato
    //   return result;
    // }

    // Verifico che la somma degli importi tra stati debitori e importi "non propri" siano minori dell'importo del pagamento
    let residuoImporti: number = this.verificaImportiPagamento(data);
    // Verifico il risultato
    if (residuoImporti >= 0) {
      // Importi corretti modifico il risultato
      result.validated = true;
      result.notifica = RiscaNotifyCodes.I031;
      result.residuo = residuoImporti;
      // Ritorno il risultato
      return result;
    }

    // Ritorno il risultato
    return result;
  }

  /**
   * Funzione che prende in input le informazioni per i verifica importi ed effettua un controllo specifico.
   * Il controllo specifico viene eseguito sulla base di definizione degli importi.
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @returns boolean con valore [true] se non sono definiti importi tra stati debitori e importi "non propri". Altrimenti false.
   */
  verificaImportiNessunImporto(data: IVerificaImporti): boolean {
    // Verifico l'input
    if (!data) {
      // Niente configurazione
      return true;
    }

    // Estraggo le informazioni dall'oggetto in input
    const {
      dettagliPagamento,
      statiDebitoriSel,
      importoDaRimborsare,
      importoNonDiCompetenza,
      importoNonIdentificato,
    } = data;
    // Verifico le condizioni sugli importi versati
    const isEmptyIVDP =
      !dettagliPagamento ||
      dettagliPagamento.length == 0 ||
      dettagliPagamento.every((dp: DettaglioPagSearchResultVo) => {
        // Ritorno il check di non esistenza sull'importo versato
        // [VF] Issue 88 - si richiede di poter inserire il valore 0 sull'importo versato per sd.
        return dp.importo_versato == undefined; //|| dp.importo_versato == 0;
        // #
      });

    const isEmptyIVSD =
      !statiDebitoriSel ||
      statiDebitoriSel.length == 0 ||
      statiDebitoriSel.every((sd: StatoDebitorioVo) => {
        // Ritorno il check di non esistenza sull'importo versato
        // [VF] Issue 88 - si richiede di poter inserire il valore 0 sull'importo versato per sd.
        return sd.importo_versato == undefined; //|| sd.importo_versato == 0;
        // #
      });
    const isEmptyIDR = !importoDaRimborsare;
    const isEmptyINDC = !importoNonDiCompetenza;
    const isEmptyINI = !importoNonIdentificato;

    // Ritorno il risultato
    return (
      isEmptyIVDP && isEmptyIVSD && isEmptyIDR && isEmptyINDC && isEmptyINI
    );
  }

  /**
   * Funzione che prende in input le informazioni per i verifica importi ed effettua un controllo specifico.
   * Il controllo specifico viene eseguito sulla base di definizione degli importi degli stati debitori selezionati. Tutti gli stati debitori selezionati devono essere valorizzati.
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @returns boolean con valore [true] se tutti gli importi versati degli stati debitori sono maggiori o uguali a 0. Altrimenti false.
   */
  verificaImportiTuttiSD(data: IVerificaImporti): boolean {
    // Verifico l'input
    if (!data) {
      // Niente configurazione
      return true;
    }

    // Estraggo le informazioni dall'oggetto in input
    const { statiDebitoriSel } = data;

    const tuttiImportiValidi =
      statiDebitoriSel == undefined ||
      statiDebitoriSel.length == 0 ||
      statiDebitoriSel.every((sd: StatoDebitorioVo) => {
        // Ritorno il check sull'importo versato
        // [VF] Issue 88 - deve essere consentito inserire importo_versato = 0
        return sd.importo_versato >= 0;
        // #
      });

    // Ritorno il risultato
    return tuttiImportiValidi;
  }

  /**
   * Funzione che prende in input le informazioni per i verifica importi ed effettua un controllo specifico.
   * Il controllo specifico viene eseguito sulla base di definizione degli importi dei dettagli pagamento. Tutti i dettagli pagamento devono essere valorizzati.
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @returns boolean con valore [true] se tutti gli importi versati dei dettagli pagamento sono maggiori o uguali a 0. Altrimenti false.
   */
  verificaImportiDettagliPagamento(data: IVerificaImporti): boolean {
    // Verifico l'input
    if (!data) {
      // Niente configurazione
      return true;
    }

    // Estraggo le informazioni dall'oggetto in input
    const { dettagliPagamento } = data;

    const tuttiImportiValidi =
      dettagliPagamento == undefined ||
      dettagliPagamento.length == 0 ||
      dettagliPagamento.every((dettPag: DettaglioPagSearchResultVo) => {
        // Ritorno il check sull'importo versato
        // [VF] Issue 88 - deve essere consentito inserire importo_versato = 0
        return dettPag.importo_versato >= 0;
        // #
      });

    // Ritorno il risultato
    return tuttiImportiValidi;
  }

  /**
   * Funzione che prende in input le informazioni per i verifica importi ed effettua un controllo specifico.
   * Il controllo specifico viene eseguito sulla base di definizione degli importi e del valore del canone versato dello stato debitorio.
   * Se non ci sono dati per gli stati debitori, il controllo ritornerà [true].
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @returns boolean con valore [true] se i valori risultano minori del canone versato dello stato debitorio. Altrimenti false.
   */
  verficaImportiSDCanone(data: IVerificaImporti): boolean {
    // Verifico l'input
    if (!data) {
      // Niente configurazione
      return true;
    }

    // Estraggo le informazioni dall'oggetto in input
    const { statiDebitoriSel } = data;
    // Verifico gli stati debitori
    if (!statiDebitoriSel) {
      // Niente controllo, ritorno true
      return true;
    }

    // Definisco la variabile che terrà conto del controllo
    let importiMinCanone: boolean;
    importiMinCanone = statiDebitoriSel.every((sd: StatoDebitorioVo) => {
      // Se l'importo versato è undefined bypasso il controllo
      if (sd.importo_versato == undefined) {
        // Do per valido il valore
        return true;
        // #
      }

      // Verifico e ritorno che importo versato sia minore del canone
      return sd.importo_versato <= sd.canone_dovuto;
      // #
    });

    // Ritorno il risultato
    return importiMinCanone;
  }

  /**
   * Funzione che prende in input le informazioni per i verifica importi ed effettua un controllo specifico.
   * Il controllo specifico viene eseguito sulla base di definizione degli importi e del valore del dell'importo mancante con interessi dello stato debitorio.
   * Se non ci sono dati per gli stati debitori, il controllo ritornerà [true].
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @returns boolean con valore [true] se i valori risultano minori dell'importo mancante con interessi dello stato debitorio. Altrimenti false.
   */
  verficaImportiSDMancanteConInteressi(data: IVerificaImporti): boolean {
    // Verifico l'input
    if (!data) {
      // Niente configurazione
      return true;
    }

    // Estraggo le informazioni dall'oggetto in input
    const { statiDebitoriSel } = data;
    // Verifico gli stati debitori
    if (!statiDebitoriSel) {
      // Niente controllo, ritorno true
      return true;
    }

    // Definisco la variabile che terrà conto del controllo
    let importiMinCanone: boolean;
    importiMinCanone = statiDebitoriSel.every((sd: StatoDebitorioVo) => {
      // Se l'importo versato è undefined bypasso il controllo
      if (sd.importo_versato == undefined) {
        // Do per valido il valore
        return true;
        // #
      }

      // Calcolo il totale e lo formatto
      let totImpMancInteressi: number = sd.importoMancanteConInteressi;
      // Verifico e ritorno che importo versato sia minore del canone
      return sd.importo_versato <= totImpMancInteressi;
      // #
    });

    // Ritorno il risultato
    return importiMinCanone;
  }

  /**
   * Funzione che prende in input le informazioni per i verifica importi ed effettua un controllo specifico.
   * Il controllo specifico verifica che il valore dell'importo del pagamento sia maggiore della somma tra gli importi degli stati debitori/dettagli pagamento e degli importi non propri.
   * Se il risultato ritorna:
   * - result < 0  --> Gli importi definiti sono maggiori rispetto alla soglia dell'importo del pagamento;
   * - result == 0 --> Gli importi coprono completamente l'importo del pagamento;
   * - result > 0  --> Gli importi coprono parzialmente l'importo del pagamento, creando un residuo;
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @returns number con il risultato dell'operazione tra importi.
   */
  verificaImportiPagamento(data: IVerificaImporti): number {
    // Verifico l'input
    if (!data) {
      // Niente configurazione
      return -1;
    }

    // Estraggo le informazioni dall'oggetto in input
    const {
      importoPagamento,
      statiDebitoriSel,
      dettagliPagamento,
      importoDaRimborsare,
      importoNonDiCompetenza,
      importoNonIdentificato,
    } = data;
    // Definisco i totalizzatori per le varie informazioni
    let valueImpPag: number = importoPagamento ?? 0;
    let valueImpDR: number = importoDaRimborsare ?? 0;
    let valueImpNDC: number = importoNonDiCompetenza ?? 0;
    let valueImpNI: number = importoNonIdentificato ?? 0;
    let valueSD: number = 0;
    let valueDP: number = 0;

    // Definisco una string che contiene il nome della proprietà per la somma totalizzatrice per gli array
    const importo_versato = 'importo_versato';
    // Per gli stati debitori/dettagli pagamento, si terrà valido uno solo degli array
    if (statiDebitoriSel?.length > 0) {
      // Esistono gli stati debitori, li prendo in considerazione
      valueSD = sumBy(statiDebitoriSel, importo_versato);
      // #
    }
    if (dettagliPagamento?.length > 0) {
      // Esistono i dettagli pagamento, li prendo in considerazione
      valueDP = sumBy(dettagliPagamento, importo_versato);
      // #
    }

    // Mi assicuro che non ci siano problemi con la gestione dell'EPSILON dei numeri per la verifica importi
    valueImpPag = this._riscaUtilities.arrotondaEccesso(valueImpPag, 2);
    valueImpDR = this._riscaUtilities.arrotondaEccesso(valueImpDR, 2);
    valueImpNDC = this._riscaUtilities.arrotondaEccesso(valueImpNDC, 2);
    valueImpNI = this._riscaUtilities.arrotondaEccesso(valueImpNI, 2);
    valueSD = this._riscaUtilities.arrotondaEccesso(valueSD, 2);
    valueDP = this._riscaUtilities.arrotondaEccesso(valueDP, 2);

    // Calcolo il residuo
    let residuo: number =
      valueImpPag - (valueImpDR + valueImpNDC + valueImpNI + valueSD + valueDP);
    // Effettuo un arrotondamento di sicurezza, per evitare problemi di infiniti decimali
    residuo = this._riscaUtilities.arrotondaEccesso(residuo, 2);

    // Ritorno il risultato
    return residuo;
  }

  /**
   * ###########################################
   * GESTIONE GENERAZIONE OGGETTI PAG NON PROPRI
   * ###########################################
   */

  /**
   * Funzione che gestisce le informazioni per i pagamenti non propri in base ai dati presenti sul form.
   * @param formData IPagamentoBollettazioneForm con i dati presenti sul form.
   * @param pagametno PagamentoVo con le informazioni del pagamento per la gestione dati.
   * @returns PagamentoNonPropriVo[] con le informazioni convertite rispetto alla definizione dell'utente.
   */
  gestisciPagamentiNonPropri(
    formData: IPagamentoBollettazioneForm,
    pagamento: PagamentoVo
  ): PagamentoNonProprioVo[] {
    // Verifico l'input
    if (!formData || !pagamento) {
      // Niente dati
      return [];
    }

    // Recupero l'id pagamento dall'oggetto in input
    const idPagamento: number = pagamento?.id_pagamento;
    const pagNonPropri: PagamentoNonProprioVo[] = pagamento?.pag_non_propri;

    // Dai dati del form estraggo le informazioni per gli importi non propri
    const {
      importoNonIdentificato,
      importoNonDiCompetenza,
      importoDaRimborsare,
    } = formData;
    // Definisco una struttura di supporto per la gestione degli aggiornamenti dati
    const aggiornaPNP: IAggiornaPagNonPropri[] = [
      {
        importo: importoNonIdentificato,
        tipo: this.TINP_C.nonIdentificato,
      },
      {
        importo: importoNonDiCompetenza,
        tipo: this.TINP_C.nonDiCompetenza,
      },
      {
        importo: importoDaRimborsare,
        tipo: this.TINP_C.daRimborsare,
      },
    ];

    // Ciclo l'array dati e per ogni informazione verifico ed eventualmente genero un oggetto per il pagamento non proprio
    let pagamentiNP: PagamentoNonProprioVo[];
    pagamentiNP = aggiornaPNP.map((o: IAggiornaPagNonPropri) => {
      // Estraggo le informazioni dall'oggetto
      const { importo, tipo } = o;
      // Genero l'oggetto di configurazione per la generazione dell'oggetto
      const c: IGeneraPagamentoNonProprio = {
        importo,
        tipo,
        idPagamento,
        pagNonPropri,
      };

      // Richiamo la funzione che gestirà la generazione dell'oggetto
      let pnp: PagamentoNonProprioVo;
      pnp = this.generaPagamentoNonProprio(c);

      // Ritorno l'oggetto generato
      return pnp;
    });

    // Riassegno l'array rimuovendo le informazioni undefined
    pagamentiNP = compact(pagamentiNP);

    // Ritorno l'array dati
    return pagamentiNP;
  }

  /**
   * Funzione che genera un oggetto PagamentoNonPropriVo a seconda delle configurazioni in input.
   * @param config IGeneraPagamentoNonProprio con le informazioni per la generazione dell'oggetto PagamentoNonProprioVo.
   * @returns PagamentoNonProprioVo con l'oggetto generato.
   */
  generaPagamentoNonProprio(
    config: IGeneraPagamentoNonProprio
  ): PagamentoNonProprioVo {
    // Verifico l'input
    if (!config) {
      // Niente configurazione
      return undefined;
    }

    // Estraggo le informazioni dalla configurazione
    const importo: number = config.importo;
    const tipo: ITipoImpNonProprioVo = config.tipo;
    const idPagamento: number = config.idPagamento;
    const pagNonPropri: PagamentoNonProprioVo[] = config.pagNonPropri;
    // Verifico le informazioni minime necessarie
    if (!tipo || !idPagamento) {
      // Configurazioni minime mancanti
      return undefined;
    }
    // Se non esiste un importo ritorno undefined, potrebbe essere che l'utente vuole rimuovere un pagamento (nota: vale sia undefined che 0)
    if (!importo) {
      // Nessun importo, quindi potrebbe essere una rimozione pagamento
      return undefined;
    }

    // Verifico se esiste una lista di pagamenti non propri
    if (pagNonPropri) {
      // Esiste una lista di pag non propri, verifico se esiste per lo stesso tipo
      let pagNonProprio: PagamentoNonProprioVo;
      pagNonProprio = pagNonPropri.find((pnp: PagamentoNonProprioVo) => {
        // Effettuo un match tra i codici dei tipi pag non propri
        const codOldPNP = pnp?.tipo_imp_non_propri?.cod_tipo_imp_non_propri;
        const codNewPNP = tipo.cod_tipo_imp_non_propri;
        // Verifico e ritorno il risultato della comparazione
        return codOldPNP == codNewPNP;
      });

      // Verifico se è stato trovato un oggetto già creato
      if (pagNonProprio) {
        // Esiste, allora aggiorno l'importo
        pagNonProprio.importo_versato = importo;
        // Ritorno l'oggetto modificato e blocco il flusso
        return pagNonProprio;
        // #
      }
    }

    // Creo e ritorno un nuovo oggetto
    return this.nuovoPagamentoNonProprio(idPagamento, importo, tipo);
  }

  /**
   * Funzione di supporto che crea un nuovo oggetto PagamentoNonProprioVo dato un input.
   * @param idPagamento number con l'id del pagamento.
   * @param importoVersato number con l'importo del pagamento non proprio.
   * @param tipoImporto ITipoImpNonProprioVo con la tipologia del pagamento non proprio.
   * @returns PagamentoNonProprioVo con l'oggetto generato.
   */
  private nuovoPagamentoNonProprio(
    idPagamento: number,
    importoVersato: number,
    tipoImporto: ITipoImpNonProprioVo
  ): PagamentoNonProprioVo {
    // Definisco la configurazione per il pagamento non proprio
    const iPagNonProri: IPagamentoNonProprioVo = {
      id_pagamento: idPagamento,
      importo_versato: importoVersato,
      tipo_imp_non_propri: tipoImporto,
    };
    // Genero un nuovo oggetto
    const pagNonProprio = new PagamentoNonProprioVo(iPagNonProri);
    // Ritorno l'oggetto generato
    return pagNonProprio;
  }

  /**
   * #######################################
   * FUNZIONE DI RECUPERO IMPORTI NON PROPRI
   * #######################################
   */

  /**
   * Funzione che tenta di recuperare l'importo del pagamento non proprio ricercato all'interno della lista dei pagamenti non propri.
   * Se manca qualche livello dati, verrà ritornato null.
   * @param importiNonPropri PagamentoNonProprioVo[] con la lista oggetti in cui fare la ricerca.
   * @param codiceImporto string con il codice per la ricerca dati.
   * @returns number con l'importo del pagamento non proprio.
   */
  recuperaImportoNonProprio(
    importiNonPropri: PagamentoNonProprioVo[],
    codiceImporto: string
  ): number | null {
    // Verifico l'input
    if (!importiNonPropri || !codiceImporto) {
      // Configurazioni mancante
      return null;
    }

    // Tento di recuperare il pagamento non proprio
    let pnp: PagamentoNonProprioVo;
    pnp = importiNonPropri.find((inp: PagamentoNonProprioVo) => {
      // Verifico il codice dell'oggetto con quello passato in input
      const codObj = inp.tipo_imp_non_propri?.cod_tipo_imp_non_propri;
      // Verifico le informazioni
      return codObj == codiceImporto;
      // #
    });

    // Se è stato tornato un oggetto ritorno l'importo
    return pnp?.importo_versato ?? null;
  }
}
