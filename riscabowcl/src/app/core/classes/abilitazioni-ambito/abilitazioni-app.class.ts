import { ConfigurazioneAmbitoVo } from 'src/app/core/commons/vo/configurazione-ambito-vo';
import {
  filterConfigurazioniByEnum,
  sanitizeConfigs,
  searchConfigurazioneAmbito,
} from './abilitazioni-utilities.functions';
import { isSBSTrue } from '../../../shared/services/risca/risca-utilities/risca-utilities.functions';

/**
 * Enum che mappa i dati per "chiave" di un oggetto ConfigurazioneAmbitoVo per l'applicazione.
 */
enum CDAKeysApp {
  limiteAccertamento = 'LimiteAccertamento',
  reportTrasversali = 'REPORT.ReportTrasversali',
}

/**
 * Classe custom utilizzata per gestire in maniera più ordinata le informazioni per le configurazioni dell'app.
 */
export class AbilitazioniApp {
  /** ConfigurazioneAmbitoVo con le informazioni per la chiave: LimiteAccertamento. */
  private _limiteAccertamento: ConfigurazioneAmbitoVo;
  /** ConfigurazioneAmbitoVo con le informazioni per la chiave: REPORT.ReportTrasversali. */
  private _reportTrasversali: ConfigurazioneAmbitoVo;

  /**
   * Costruttore.
   */
  constructor(configs?: ConfigurazioneAmbitoVo[]) {
    // Verifico se esiste l'input
    this.impostaAbilitazioni(configs);
  }

  /**
   * Funzione che esegue tutto il processo di gestione e definizione del dato all'interno della classe.
   * @param configs Array di ConfigurazioneAmbitoVo da inserire nella classe.
   */
  impostaAbilitazioni(configs?: ConfigurazioneAmbitoVo[]) {
    // Verifico se esiste l'input
    if (!configs || configs.length === 0) {
      return;
    }
    // Assegno alla variabile locale il dato
    const configsSintized = sanitizeConfigs(configs);
    // Lancio la funzione di setup, convert e assegnazione alle variabili locali
    this.setupConfigs(configsSintized);
  }

  /**
   * ################################################
   * FUNZIONALITA DI CONFIGURAZIONE DATI DELLA CLASSE
   * ################################################
   */

  /**
   * Funzione di setup delle configurazioni per la gestione dati per la classe.
   * @param configs Array di ConfigurazioneAmbitoVo per il setup.
   */
  setupConfigs(configs: ConfigurazioneAmbitoVo[]) {
    // Verifico che esista
    if (!configs || configs.length === 0) {
      return;
    }

    // Mergio in un unico oggetto le chiavi per soggetti e gruppi
    const appKeys = { ...CDAKeysApp };
    // Effettuo il filtro degli oggetti per le sole informazioni necessarie
    const fConfigs = filterConfigurazioniByEnum(configs, appKeys);
    // Assegno localmente i dati
    this.assignLocalData(fConfigs);
  }

  /**
   * Funzione di comodo che assegna alle variabili locali le informazioni dalla lista ConfigurazioneAmbitoVo.
   * @param ambitoConfigs ConfigurazioneAmbitoVo[] per l'assegnazione dati.
   */
  private assignLocalData(ambitoConfigs: ConfigurazioneAmbitoVo[]) {
    // Variabile di comodo
    const c = ambitoConfigs;
    // Definisco le chiavi per le configurazioni
    const K_LA = CDAKeysApp.limiteAccertamento;
    const K_RRT = CDAKeysApp.reportTrasversali;

    // Recupero e assegno le configurazioni
    this._limiteAccertamento = searchConfigurazioneAmbito(c, K_LA);
    this._reportTrasversali = searchConfigurazioneAmbito(c, K_RRT);
  }

  /**
   * ###########
   * GETTER DATI
   * ###########
   */

  /**
   * Getter per i dati connessi a: LimiteAccertamento.
   * @returns number con il valore estratto dalla configurazione.
   */
  get limiteAccertamento(): number {
    // Verifico esista l'oggetto
    if (this._limiteAccertamento) {
      // Ritorno dall'oggetto il valore della configurazione
      return parseInt(this._limiteAccertamento.valore);
    }

    // Non esiste l'oggetto, ritorno un valore invalido
    return -1;
  }

  /**
   * Getter per i dati connessi a: reportTrasversali.
   * @returns boolean con il flag di reportTrasversali.
   */
  get reportTrasversali(): boolean {
    // Verifico esista l'oggetto
    if (this._reportTrasversali) {
      // Ritorno dall'oggetto il valore della configurazione
      return isSBSTrue(this._reportTrasversali.valore);
    }

    // Non esiste l'oggetto, ritorno false
    return false;
  }
}
