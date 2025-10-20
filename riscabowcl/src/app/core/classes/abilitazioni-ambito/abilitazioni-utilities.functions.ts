import { ConfigurazioneAmbitoVo } from '../../commons/vo/configurazione-ambito-vo';

/**
 * Funzione di supporto che recupera, da un array di oggetti ConfigurazioneAmbito, un oggetto data la chiave.
 * @param configsS Array di ConfigurazioneAmbito per la ricerca.
 * @param key string che definisce la chiave da cercare.
 * @returns ConfigurazioneAmbito con i dati cercati.
 */
export const searchConfigurazioneAmbito = (
  configsS: ConfigurazioneAmbitoVo[],
  key: string
): ConfigurazioneAmbitoVo => {
  // Verifico che esista
  if (!configsS || !key || configsS.length === 0) {
    return;
  }

  // Effettuo una find dell'oggetto
  return configsS.find((cS) => {
    // Effettuo un match tra chiavi
    return cS.chiave === key;
  });
};

/**
 * Funzione di sanitifizzazione degli oggetti ConfigurazioneAmbitoVo.
 * @param configs Array di ConfigurazioneAmbitoVo da sanitizizzare.
 * @returns Array di ConfigurazioneAmbitoVo sanitizizzati.
 */
export const sanitizeConfigs = (
  configs: ConfigurazioneAmbitoVo[]
): ConfigurazioneAmbitoVo[] => {
  // Rimappo i dati con la sanitizzazione
  return configs.map((c) => sanitizeConfig(c)).filter((c) => c != undefined);
};

/**
 * Funzione di sanitifizzazione di un oggetto ConfigurazioneAmbitoVo.
 * @param config ConfigurazioneAmbitoVo da sanitizizzare.
 * @returns ConfigurazioneAmbitoVo sanitizizzato.
 */
export const sanitizeConfig = (
  config: ConfigurazioneAmbitoVo
): ConfigurazioneAmbitoVo => {
  // Verifico l'oggetto
  if (!config) {
    // Niente configurazione
    return;
  }

  // Sanitizzo le proprietà
  config.chiave = config.chiave?.trim() ?? '';
  config.note = config.note?.trim() ?? '';
  config.valore = config.valore?.trim() ?? '';

  // Ritorno l'oggetto
  return config;
};

/**
 * Funzione che filtra le informazioni da un array ConfigurazioneAmbitoVo, applicando la condizione di verifica sulla chiave.
 * Se la chiave ha un match con il valore dell'enumeratore passato come parametro, allora quel valore verrà restituito nell'array filtrato.
 * @param configs Array di ConfigurazioneAmbitoVo con la lista di oggetti da filtrare.
 * @param configsChiavi any, ma è da considerare come oggetto/enum per estrarre gli oggetti dalla lista.
 * @returns Array di ConfigurazioneAmbitoVo filtrato secondo la struttura della mappa.
 */
export const filterConfigurazioniByEnum = (
  configs: ConfigurazioneAmbitoVo[],
  configsChiavi: any
): ConfigurazioneAmbitoVo[] => {
  // Verifico l'input
  if (!configs || !configsChiavi) {
    // Ritorno array vuoto
    return [];
  }

  // Filtro e ritorno i valori che matchano con la chiave
  return configs.filter((c: ConfigurazioneAmbitoVo) => {
    // Estraggo i valori dall'oggetto/enum in input
    const chiavi: string[] = Object.values(configsChiavi);
    // Recupero la chiave dalla configurazione
    const chiave = c.chiave;
    // Ritorno true se la chiave è all'interno dei values
    return chiavi.some((c) => c === chiave);
  });
};
