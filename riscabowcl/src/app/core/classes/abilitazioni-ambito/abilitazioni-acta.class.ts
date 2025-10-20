import { ConfigurazioneAmbitoVo } from 'src/app/core/commons/vo/configurazione-ambito-vo';
import { isSBSTrue } from '../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { AbilitaACTA } from '../../../shared/utilities/enums/utilities.enums';
import {
  filterConfigurazioniByEnum,
  sanitizeConfigs,
  searchConfigurazioneAmbito,
} from './abilitazioni-utilities.functions';

/**
 * Enum che mappa i dati per "chiave" di un oggetto ConfigurazioneAmbitoVo per i soggetti.
 */
enum CACTAKeys {
  parolaChiaveSF = 'ACTA.parolaChiaveSF',
  visDocumentiPratica = 'ACTA.visDocumentiPratica',
  idAOO = 'ACTA.idAOO',
  idnodo = 'ACTA.idnodo',
  idStruttura = 'ACTA.idStruttura',
}

/**
 * 
 * Classe custom utilizzata per gestire in maniera più ordinata le informazioni per le configurazioni di ACTA.
 */
export class AbilitazioniACTA {
  /** ConfigurazioneAmbitoVo contenente le informazioni per la configurazione ACTA, con chiave: ACTA.parolaChiaveSF */
  private _parolaChiaveSF: ConfigurazioneAmbitoVo;
  /** ConfigurazioneAmbitoVo contenente le informazioni per la configurazione ACTA, con chiave: ACTA.visDocumentiPratica */
  private _visDocumentiPratica: ConfigurazioneAmbitoVo;
  /** ConfigurazioneAmbitoVo contenente le informazioni per la configurazione ACTA, con chiave: ACTA.idAOO */
  private _idAOO: ConfigurazioneAmbitoVo;
  /** ConfigurazioneAmbitoVo contenente le informazioni per la configurazione ACTA, con chiave: ACTA.idnodo */
  private _idNodo: ConfigurazioneAmbitoVo;
  /** ConfigurazioneAmbitoVo contenente le informazioni per la configurazione ACTA, con chiave: ACTA.idStruttura */
  private _idStruttura: ConfigurazioneAmbitoVo;

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

    // Effettuo il filtro degli oggetti per le sole informazioni necessarie
    const fConfigs = filterConfigurazioniByEnum(configs, CACTAKeys);
    // Assegno localmente i dati
    this.assignLocalData(fConfigs);
  }

  /**
   * Funzione di comodo che assegna alle variabili locali le informazioni dalla lista ConfigurazioneAmbitoVo.
   * @param configsS Array di ConfigurazioneAmbitoVo per l'assegnazione dati.
   */
  private assignLocalData(configsS: ConfigurazioneAmbitoVo[]) {
    // Variabile di comodo
    const c = configsS;
    // Definisco le chiavi per ACTA
    const K_PCSF = CACTAKeys.parolaChiaveSF;
    const K_VDP = CACTAKeys.visDocumentiPratica;
    const K_IDAOO = CACTAKeys.idAOO;
    const K_IDN = CACTAKeys.idnodo;
    const K_IDS = CACTAKeys.idStruttura;

    // Recupero i dati tramite le chiavi
    const pcsf = searchConfigurazioneAmbito(c, K_PCSF);
    const vdp = searchConfigurazioneAmbito(c, K_VDP);
    const idaoo = searchConfigurazioneAmbito(c, K_IDAOO);
    const idn = searchConfigurazioneAmbito(c, K_IDN);
    const ids = searchConfigurazioneAmbito(c, K_IDS);

    // Assegno localmente i dati recuperati per le configurazioni
    this._parolaChiaveSF = pcsf;
    this._visDocumentiPratica = vdp;
    this._idAOO = idaoo;
    this._idNodo = idn;
    this._idStruttura = ids;
  }

  /**
   * #######################
   * FUNZIONALITA' DI COMODO
   * #######################
   */

  /**
   * Funzione che recupera un'abilitazione, dato il tipo di abilitazione richiesta.
   * @param abilitazione AbilitaACTA che identifica l'abilitazione.
   * @returns boolean che definisce il tipo di abilitazione.
   */
  getAbilitazioneACTA(abilitazione: AbilitaACTA): boolean {
    // Verifico l'input
    if (!abilitazione) {
      return false;
    }

    // Verifico l'abilitazione
    switch (abilitazione) {
      case AbilitaACTA.visDocumentiPratica:
        return this.visDocumentiPratica;
      default:
        return false;
    }
  }

  /**
   * ###########
   * GETTER DATI
   * ###########
   */

  /**
   * Getter per i dati connessi a: _parolaChiaveSF.
   * @returns boolean con il flag di abilitazione.
   */
  get parolaChiaveSF() {
    // Ritorno il valore della configurazone
    return this._parolaChiaveSF?.valore;
  }

  /**
   * Getter per i dati connessi a: _visDocumentiPratica.
   * @returns boolean con il flag di abilitazione.
   */
  get visDocumentiPratica() {
    // Verifico esista l'oggetto
    if (this._visDocumentiPratica) {
      // Ritorno dall'oggetto il valore della configurazione
      return isSBSTrue(this._visDocumentiPratica.valore);
    }

    // Non esiste l'oggetto, ritorno false
    return false;
  }

  /**
   * Getter per i dati connessi a: _idAOO.
   * @returns boolean con il flag di abilitazione.
   */
  get idAOO() {
    // Ritorno il valore della configurazone
    return this._idAOO?.valore;
  }

  /**
   * Getter per i dati connessi a: _idnodo.
   * @returns boolean con il flag di abilitazione.
   */
  get idNodo() {
    // Ritorno il valore della configurazone
    return this._idNodo?.valore;
  }

  /**
   * Getter per i dati connessi a: _idStruttura.
   * @returns boolean con il flag di abilitazione.
   */
  get idStruttura() {
    // Ritorno il valore della configurazone
    return this._idStruttura?.valore;
  }
}
