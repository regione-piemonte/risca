/**
 * ###############
 * SESSION STORAGE
 * ###############
 */

/**
 * sessionStorage.
 * Funzione che ritorna una chiave, dato un indice in input.
 * @param i number che definisce l'indice per il recupero della chiave.
 * @returns string con il nome della chiave per l'indice richiesto.
 */
export const sKeyAt = (i: number): string => {
  // Ritorno la chiave all'indice i
  return sessionStorage.key(i);
};

/**
 * sessionStorage.
 * Funzione che ritorna un valore, data una chiave in input.
 * @param key string che definisce il nome della chiave.
 * @returns any contenente il valore associato alla chiave.
 */
export const sGetItem = (key: string): any => {
  // Recupero il valore per la chiave
  return convert(sessionStorage.getItem(key));
};

/**
 * sessionStorage.
 * Funzione che ritorna un valore, data una chiave in input.
 * @param key string che definisce il nome della chiave.
 * @param value any che definisce l'informazione da salvare nel storage.
 * @returns any contenente il valore associato alla chiave.
 */
export const sSetItem = (key: string, value: any) => {
  // Recupero il valore per la chiave
  sessionStorage.setItem(key, stringify(value));
};

/**
 * sessionStorage.
 * Funzione che rimuove un valore, data una chiave in input.
 * @param key string che definisce il nome della chiave.
 * @returns any contenente il valore associato alla chiave.
 */
export const sRemoveItem = (key: string) => {
  // Recupero il valore per la chiave
  sessionStorage.getItem(key);
};

/**
 * sessionStorage.
 * Funzione che rimuove tutti i valori all'interno dello storage.
 */
export const sClear = (): any => {
  // Recupero il valore per la chiave
  sessionStorage.clear();
};

/**
 * ###############
 * SESSION STORAGE
 * ###############
 */

/**
 * localStorage.
 * Funzione che ritorna una chiave, dato un indice in input.
 * @param i number che definisce l'indice per il recupero della chiave.
 * @returns string con il nome della chiave per l'indice richiesto.
 */
export const lKeyAt = (i: number): string => {
  // Ritorno la chiave all'indice i
  return localStorage.key(i);
};

/**
 * localStorage.
 * Funzione che ritorna un valore, data una chiave in input.
 * @param key string che definisce il nome della chiave.
 * @returns any contenente il valore associato alla chiave.
 */
export const lGetItem = (key: string): any => {
  // Recupero il valore per la chiave
  return convert(localStorage.getItem(key));
};

/**
 * localStorage.
 * Funzione che ritorna un valore, data una chiave in input.
 * @param key string che definisce il nome della chiave.
 * @param value any che definisce l'informazione da salvare nel storage.
 * @returns any contenente il valore associato alla chiave.
 */
export const lSetItem = (key: string, value: any) => {
  // Recupero il valore per la chiave
  localStorage.setItem(key, stringify(value));
};

/**
 * localStorage.
 * Funzione che rimuove un valore, data una chiave in input.
 * @param key string che definisce il nome della chiave.
 * @returns any contenente il valore associato alla chiave.
 */
export const lRemoveItem = (key: string) => {
  // Recupero il valore per la chiave
  localStorage.getItem(key);
};

/**
 * localStorage.
 * Funzione che rimuove tutti i valori all'interno dello storage.
 */
export const lClear = (): any => {
  // Recupero il valore per la chiave
  localStorage.clear();
};

/**
 * ###################################################
 * FUNZIONI DI COMODO PER GESTIRE I DATI DELLA STORAGE
 * ###################################################
 */

/**
 * Funzione che verifica l'input per il set del dato nella storage.
 * Se l'input non è un primitivo, viene effettuata una stringhizzazione.
 * @param value any da verificare e manipolare.
 * @returns any con il dato da salvare.
 */
export const stringify = (value: any): any => {
  // Verifico l'input
  if (value === undefined) {
    return '';
  }

  // Verifico la tipologia di value
  switch (typeof value) {
    case 'number':
    case 'bigint':
    case 'string':
    case 'symbol':
    case 'boolean':
      return value;
    case 'object':
      return JSON.stringify(value);
    default:
      return '';
  }
};

/**
 * Funzione che verifica l'output di un valore dallo storage.
 * Se l'input è una string, viene tentato il parsing del dato in oggetto.
 * Se il parsing fallisce, viene ritornato il valore come stringa.
 * @param value any da verificare e manipolare.
 * @returns any con il dato da restituire.
 */
export const convert = (value: any): any => {
  // Verifico il tipo del valore
  if (typeof value === 'string') {
    // Definsco un contenitore di supporto
    let valueParse: any;
    // Tento di effettuare il convert della string, perché potrebbe essere un oggetto
    try {
      // Tento il convert
      valueParse = JSON.parse(value);
      // #
    } catch (e) {
      // Value è una normale stringa
      valueParse = value;
    }
    // Ritorno il valore
    return valueParse;
    // #
  } else {
    // Ritorno al valore
    return value;
  }
};
