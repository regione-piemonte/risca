import { Injectable } from '@angular/core';
import { ValidationErrors } from '@angular/forms';
import { JsonRegolaRangeVo } from '../../../../core/commons/vo/json-regola-range-vo';
import { RegolaUsoVo } from '../../../../core/commons/vo/regola-uso-vo';
import { numberComposition } from '../../../../shared/miscellaneous/forms-validators/forms-validators';
import { legendaMinimiRegoleUso } from './configurazioni-utilities.functions';
import { JsonRegolaRangeFormats } from './utilities/configurazioni.consts';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';

@Injectable({ providedIn: 'root' })
export class ConfigurazioniUtilitiesService {
  /** JsonRegolaRangeFormats con le costanti per la gestione del formato dati dei JsonRegolaRangeVo. */
  JRRF_C = new JsonRegolaRangeFormats();

  /**
   * Funzione di utility che genera la descrizione per la legenda "minimi" per una regola uso.
   * @param regolaUso RegolaUsoVo dalla quale generare la legenda "minimi".
   * @returns string con la descrizione generata.
   */
  legendaMinimiRegoleUso(regolaUso: RegolaUsoVo): string {
    // Richiamo la funzione di utility
    return legendaMinimiRegoleUso(regolaUso);
  }

  /**
   * @deprecated Controlli deprecati. Si tiene la funzione in caso di riutilizzi.
   * Funzione di supporto che verifica la validità di un array di ranges.
   * Le verifiche sono:
   * - Deve esistere almeno un range;
   * - Almeno una soglia tra: soglia_min, soglia_max; deve essere valorizzata;
   * - Le soglie devono essere un numero con massimo 5 interi e 4 decimali;
   * - Canone minimo è obbligatorio;
   * - Canone minimo deve essere un numero con massimo 7 interi e 2 decimali;
   * @param ranges JsonRegolaRangeVo[] con la lista dei ranges da verificare.
   */
  validJsonRegolaRangesData(ranges: JsonRegolaRangeVo[]): boolean {
    // Verifico l'input
    if (!ranges || ranges.length == 0) {
      // Non si può fare la validazione
      return false;
    }

    // Verifico le informazioni, se anche un solo elemento non è valido, ritorno false
    let validJRR: boolean;
    validJRR = ranges.every((jrr: JsonRegolaRangeVo) => {
      // Estraggo le informazioni dall'oggetto
      const { soglia_min, soglia_max, canone_minimo } = jrr;

      // - Almeno una soglia tra: soglia_min, soglia_max; deve essere valorizzata;
      if (soglia_min == undefined && soglia_max == undefined) {
        // Manca la definizione di almeno una soglia
        return false;
      }

      // - Le soglie devono essere un numero con massimo 5 interi e 4 decimali;
      // Uso lo stesso validatore del campo. Se viene restituito un oggetto, il numero non è valido
      let sogliaMinNC: ValidationErrors = numberComposition(
        soglia_min,
        this.JRRF_C.INT_SOGLIA_MIN,
        this.JRRF_C.FLOAT_SOGLIA_MIN
      );
      let sogliaMinInvalid: boolean;
      sogliaMinInvalid = sogliaMinNC != null;

      let sogliaMaxNC: ValidationErrors = numberComposition(
        soglia_max,
        this.JRRF_C.INT_SOGLIA_MAX,
        this.JRRF_C.FLOAT_SOGLIA_MAX
      );
      let sogliaMaxInvalid: boolean;
      sogliaMinInvalid = sogliaMaxNC != null;

      if (sogliaMinInvalid || sogliaMaxInvalid) {
        // Composizione di una soglia errata
        return false;
      }

      // - Canone minimo è obbligatorio;
      if (canone_minimo == undefined) {
        // E' obbligatorio
        return false;
      }

      // - Canone minimo deve essere un numero con massimo 7 interi e 2 decimali;
      let canoneMinimoNC: ValidationErrors = numberComposition(
        canone_minimo,
        this.JRRF_C.INT_CANONE_MINIMO,
        this.JRRF_C.FLOAT_CANONE_MINIMO
      );
      let canoneMinimoInvalid: boolean;
      canoneMinimoInvalid = canoneMinimoNC != null;

      if (canoneMinimoInvalid) {
        // Composizione di una soglia errata
        return false;
      }

      // Non ci sono errori
      return true;
    });

    // Ritorno il controllo
    return validJRR;
  }

  /**
   * Funzione di supporto che verifica la validità di un array di ranges.
   * Le verifiche sono:
   * - L'array deve contenere un range e questo range deve avere minimo e massimo definiti e diversi da 0;
   * @param ranges JsonRegolaRangeVo[] con la lista dei ranges da verificare.
   * @returns undefined se non ci sono stati errori, altrimenti il codice del messaggio da visualizzare.
   */
  validJsonRegolaRanges(ranges: JsonRegolaRangeVo[]): RiscaNotifyCodes {
    // Verifico l'input
    if (!ranges || ranges.length === 0) {
      // Non si può fare la validazione
      return RiscaNotifyCodes.E104;
    }

    // Filtro i ranges e rimuovo la possibile configurazione per il minimo principale
    const rangesNoMP: JsonRegolaRangeVo[] = ranges.filter(
      (range: JsonRegolaRangeVo) => {
        // Ritonro solo gli oggetti non minimo principale
        return !range?.regolaRangeAsMinimoPrincipale;
        // #
      }
    );
    // Verifico che a seguito del filtro ci siano almeno 2 ranges
    if (rangesNoMP.length < 2) {
      // Ci devono essere almeno 2 ranges
      return RiscaNotifyCodes.E104;
    }

    // Verifico la validità degli elementi
    const checkAtLeastOneRangeValid: boolean = ranges.some(
      (range: JsonRegolaRangeVo) => {
        // L'oggetto non deve avere entrambe le soglie a 0
        const { soglia_min, soglia_max } = range ?? {};

        // Verifico esistano dati
        if (soglia_min == undefined && soglia_max == undefined) {
          // I range non sono stati definiti
          return false;
        }
        // Verifico l'oggetto non sia dedicato al minimo principale
        if (soglia_min === 0 && soglia_max === 0) {
          // Esiste solo un range, ma è dedicato al minimo principale
          return false;
        }

        // Controllo passato
        return true;
        // #
      }
    );
    // Verifico il risultato della validazione
    if (!checkAtLeastOneRangeValid) {
      // Errore, ritorno il codice specifico
      return RiscaNotifyCodes.E104;
    }

    // I controlli sono validi
    return undefined;
  }
}
