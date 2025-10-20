import { ConfigurazioneAmbitoVo } from 'src/app/core/commons/vo/configurazione-ambito-vo';
import { isSBSTrue } from '../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { AbilitaDAGruppi, AbilitaDASoggetti } from '../../../shared/utilities';
import { AbilitaDASezioni } from '../../../shared/utilities/enums/utilities.enums';
import {
  filterConfigurazioniByEnum,
  sanitizeConfigs,
  searchConfigurazioneAmbito,
} from './abilitazioni-utilities.functions';

/**
 * Enum che mappa i dati per "chiave" di un oggetto ConfigurazioneAmbitoVo per i soggetti.
 */
enum CDAKeysSoggetti {
  isGestioneAbilitata = 'SOGGETTO.isGestioneAbilitata',
  isFonteAbilitataInLettura = 'SOGGETTO.isFonteAbilitataInLettura',
  isFonteAbilitataInScrittura = 'SOGGETTO.isFonteAbilitataInScrittura',
}

/**
 * Enum che mappa i dati per "chiave" di un oggetto ConfigurazioneAmbitoVo per i gruppi.
 */
enum CDAKeysGruppi {
  isAbilitato = 'GRUPPO.isAbilitato',
}

/**
 * Interfaccia di comodo che mappa i nomi delle chiavi per i soggetti.
 */
interface ADASingolaSoggetti {
  isGestioneAbilitata: ConfigurazioneAmbitoVo;
  isFonteAbilitataInLettura: ConfigurazioneAmbitoVo;
  isFonteAbilitataInScrittura: ConfigurazioneAmbitoVo;
}

/**
 * Interfaccia di comodo che mappa i nomi delle chiavi per i gruppi.
 */
interface ADASingolaGruppi {
  isAbilitato: ConfigurazioneAmbitoVo;
}

/**
 * Classe custom utilizzata per gestire in maniera più ordinata le informazioni per le configurazioni dei dati anagrafici.
 */
export class AbilitazioniDatiAnagrafici {
  /** ADASingolaSoggetti di comodo per gestire rapidamente le sue informazioni. */
  private _soggetto: ADASingolaSoggetti;
  /** ADASingolaGruppi di comodo per gestire rapidamente le sue informazioni. */
  private _gruppo: ADASingolaGruppi;

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
    const cSGKeys = { ...CDAKeysSoggetti, ...CDAKeysGruppi };
    // Effettuo il filtro degli oggetti per le sole informazioni necessarie
    const fConfigs = filterConfigurazioniByEnum(configs, cSGKeys);
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
    // Definisco le chiavi per il soggetto
    const K_ISFAL = CDAKeysSoggetti.isFonteAbilitataInLettura;
    const K_ISFAS = CDAKeysSoggetti.isFonteAbilitataInScrittura;
    const K_ISGA = CDAKeysSoggetti.isGestioneAbilitata;

    // Assegno localmente i dati tramite chiavi
    this._soggetto = {
      isFonteAbilitataInLettura: searchConfigurazioneAmbito(c, K_ISFAL),
      isFonteAbilitataInScrittura: searchConfigurazioneAmbito(c, K_ISFAS),
      isGestioneAbilitata: searchConfigurazioneAmbito(c, K_ISGA),
    };

    // Definisco le chiavi per il gruppo
    const K_ISA = CDAKeysGruppi.isAbilitato;

    // Assegno localmente i dati tramite chiavi
    this._gruppo = {
      isAbilitato: searchConfigurazioneAmbito(c, K_ISA),
    };
  }

  /**
   * #######################
   * FUNZIONALITA' DI COMODO
   * #######################
   */

  /**
   * Funzione che recupera un'abilitazione, data la sezione e il tipo di abilitazione richiesta.
   * @param sezione AbilitaDASezioni che identifica la sezione.
   * @param abilitazione AbilitaDASoggetti | AbilitaDAGruppi che identifica l'abilitazione.
   * @returns boolean che definisce il tipo di abilitazione.
   */
  getAbilitazioneSoggettiGruppi(
    sezione: AbilitaDASezioni,
    abilitazione: AbilitaDASoggetti | AbilitaDAGruppi
  ): boolean {
    // Verifico l'input
    if (!sezione || !abilitazione) {
      return false;
    }

    // Verifico la sezione
    if (sezione === AbilitaDASezioni.soggetti) {
      // Verifico l'abilitazione
      switch (abilitazione) {
        case AbilitaDASoggetti.isFonteAbilitataInLettura:
          return this.isFonteAbilitataInLettura;
        case AbilitaDASoggetti.isFonteAbilitataInScrittura:
          return this.isFonteAbilitataInScrittura;
        case AbilitaDASoggetti.isGestioneAbilitata:
          return this.isGestioneAbilitata;
        default:
          return false;
      }
      // #
    } else if (sezione === AbilitaDASezioni.gruppi) {
      // Verifico l'abilitazione
      switch (abilitazione) {
        case AbilitaDAGruppi.isAbilitato:
          return this.isAbilitato;
        default:
          return false;
      }
      // #
    } else {
      // Nessuna sezione mappata
      return false;
    }
  }

  /**
   * ###########
   * GETTER DATI
   * ###########
   */

  /**
   * Getter per i dati connessi a: isGestioneAbilitata.
   * @returns boolean con il flag di abilitazione.
   */
  get isGestioneAbilitata() {
    // Verifico esista l'oggetto
    if (this._soggetto?.isGestioneAbilitata) {
      // Ritorno dall'oggetto il valore della configurazione
      return isSBSTrue(this._soggetto.isGestioneAbilitata.valore);
    }

    // Non esiste l'oggetto, ritorno false
    return false;
  }

  /**
   * Getter per i dati connessi a: isFonteAbilitataInLettura.
   * @returns boolean con il flag di abilitazione.
   */
  get isFonteAbilitataInLettura() {
    // Verifico esista l'oggetto
    if (this._soggetto?.isFonteAbilitataInLettura) {
      // Ritorno dall'oggetto il valore della configurazione come boolean (specifico per questa configurazione)
      return isSBSTrue(this._soggetto.isFonteAbilitataInLettura.valore);
    }

    // Non esiste l'oggetto, ritorno false
    return false;
  }

  /**
   * Getter per i dati connessi a: isFonteAbilitataInScrittura.
   * @returns boolean con il flag di abilitazione.
   */
  get isFonteAbilitataInScrittura() {
    // Verifico esista l'oggetto
    if (this._soggetto?.isFonteAbilitataInScrittura) {
      // Ritorno dall'oggetto il valore della configurazione
      return isSBSTrue(this._soggetto.isFonteAbilitataInScrittura.valore);
    }

    // Non esiste l'oggetto, ritorno false
    return false;
  }

  /**
   * Getter per i dati connessi a: isAbilitato.
   * @returns boolean con il flag di abilitazione.
   */
  get isAbilitato() {
    // Verifico esista l'oggetto
    if (this._gruppo?.isAbilitato) {
      // Ritorno dall'oggetto il valore della configurazione
      return isSBSTrue(this._gruppo.isAbilitato.valore);
    }

    // Non esiste l'oggetto, ritorno false
    return false;
  }
}
