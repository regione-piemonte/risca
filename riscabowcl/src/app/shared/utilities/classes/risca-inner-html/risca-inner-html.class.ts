import { includes, replace } from 'lodash';

/** Enum che definisce le chiavi dei codici per le regole della classe RiscaInnerHTML. */
export enum RiscaInnerHTMLCodes {
  backslashN = 'Backslash N - return carriage',
}

export interface IRiscaInnerHTMLRule {
  /** RiscaInnerHTMLCodes che definisce un codice identificativo per la regola. */
  codice: RiscaInnerHTMLCodes;
  /** String che definisce l'elemento string da convertire. */
  toConvert: string;
  /** String che definisce l'elemento string come risultato della conversione. */
  toResult: string;
}

/**
 * Classe che racchiude le varie regole di gestione per la sostituzione degli elementi.
 */
export class RiscaInnerHTML {
  /** IRiscaInnerHTMLRule[] con la lista di regole da ciclare per "correggere" la formattazione della stringa. */
  regole: IRiscaInnerHTMLRule[] = [];

  /** IRiscaInnerHTMLRule per la conversione di \n al tag <br>. */
  backslashN: IRiscaInnerHTMLRule = {
    codice: RiscaInnerHTMLCodes.backslashN,
    toConvert: '\\n',
    toResult: '<br>',
  };

  constructor() {
    // Aggiungo le regole in maniera progressiva
    this.regole.push(this.backslashN);
  }

  /**
   * Funzione che itera tutte le regole definite nella classe ed effettua le sostituzioni all'interno del testo in input.
   * @param text string con il testo da convertire per l'HTML.
   * @returns string con le sottoparti convertite per l'HTML.
   */
  applicaRegole(text: string): string {
    // Verifico l'input
    if (text == undefined) {
      // Ritorno il default
      return '';
    }

    // Definisco un contenitore con la nuova stringa
    let textHTML = text;

    // Itero tutte le regole e applico la sostituzione
    this.regole.forEach((r: IRiscaInnerHTMLRule) => {
      // Estraggo dall'oggetto le informazioni
      const { toConvert, toResult } = r;
      // Verifico se all'interno della stringa c'è la substring "from"
      if (includes(text, toConvert)) {
        // Esiste la substring, sostituisco con il target
        textHTML = replace(textHTML, toConvert, toResult);
        // #
      }
    });

    // Al termine della codifica, ritorno la stringa aggiornata
    return textHTML;
  }
}
