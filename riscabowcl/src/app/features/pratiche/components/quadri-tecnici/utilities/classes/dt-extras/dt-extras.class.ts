/**
 * Classe specifica adibita alla gestione dei dati extra per un uso di legge.
 * La classe è gestita tramite i seguenti generics:
 * - E => Indica l'enumeratore specifico contenente la mappa con le chiavi dei parametri extra all'interno dell'uso di legge.
 */
export class DTExtrasClass<E> {
  /** string che definisce il codice dell'uso di riferimento. */
  usoRiferimento: string;
  /** E contenente la mappatura delle chiavi presenti come dati extra all'interno dell'uso di legge. */
  extras: E;

  /**
   * Constructor.
   */
  constructor() {
    // Imposto le informazioni di default
    this.usoRiferimento = '';
    this.extras = {} as E;
  }

  /**
   * ######################
   * FUNZIONI DA OVERRIDARE
   * ######################
   */

  /**
   * Funzione che genera una stringa HTML per la visualizzazione dei dati all'interno del popover delle tabelle dei dati tecnici.
   * La funzione è pensata per l'override dalla classe che estende questa classe.
   * @returns string come template DOM HTML da utilizzare come corpo del popover delle tabelle dei dati tecnici.
   */
  popoverPratica(): string {
    // Per default non ritorno niente
    return '';
  }

  /**
   * Funzione che genera una stringa HTML per la visualizzazione dei dati all'interno del popover delle tabelle dei dati tecnici annualità.
   * La funzione è pensata per l'override dalla classe che estende questa classe.
   * @returns string come template DOM HTML da utilizzare come corpo del popover delle tabelle dei dati tecnici.
   */
  popoverAnnualita(): string {
    // Per default non ritorno niente
    return '';
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione che verifica se l'uso di legge passato in input è lo stesso di quello definito a livello di classe.
   * Se l'uso coincide, tenterà di estrarre dall'oggetto dato tecnico le informazione presenti nella mappatura definita a livello di classe.
   * @param codiceUso string con il codice dell'uso di legge.
   * @param dtUso any contenente le informazioni dei dati tecnici per un uso. Dovrebbe essere passato sempre un oggetto.
   */
  addExtrasToClass(codiceUso: string, dtUso: any) {
    // Verifico l'input
    if (!codiceUso || !dtUso) {
      // Mancano le configurazioni minime
      return;
    }

    // Per evitare errori bloccanti, racchiudo tutto in un try catch (l'accesso alle informazioni con strutture non corrette bloccherebbe tutto)
    try {
      // Recupero dalla configurazione di classe la mappatura
      const extras: E = this.extras;
      // Vado ad iterare tutte le informazioni della mappatura
      for (let [keyFE, keyDT] of Object.entries(extras)) {
        // Dall'oggetto del dato tecnico tento di estrarre il valore tramite chiave dato tecnico
        const value = dtUso[keyDT];
        // Assegno localmente, impostando la chiave di FE, il valore alla classe
        this[keyFE] = value;
      }
      // #
    } catch (e) {
      // Scrivo un warning
      console.warn(`addExtrasToClass failed on mapping:`, codiceUso);
      console.warn(`addExtrasToClass failed based on tech data:`, dtUso);
      console.warn(`addExtrasToClass failed with mapper:`, this.extras);
    }
  }
}
