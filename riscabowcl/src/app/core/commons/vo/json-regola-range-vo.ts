import { HelperVo } from './helper-vo';

/**
 * Oggetto dal server con le informazioni dei json regola.
 */
export interface IJsonRegolaRangeVo {
  soglia_min: number;
  soglia_max: number;
  canone_minimo: number;
}

export class JsonRegolaRangeVo extends HelperVo {
  soglia_min: number;
  soglia_max: number;
  canone_minimo: number;

  constructor(iJRRVo?: IJsonRegolaRangeVo) {
    super();

    this.soglia_min = iJRRVo?.soglia_min;
    this.soglia_max = iJRRVo?.soglia_max;
    this.canone_minimo = iJRRVo?.canone_minimo;
  }

  toServerFormat(): IJsonRegolaRangeVo {
    // Definisco l'oggetto per il server
    let be: IJsonRegolaRangeVo = {
      soglia_min: this.soglia_min,
      soglia_max: this.soglia_max,
      canone_minimo: this.canone_minimo,
    };

    // Ritorno l'oggetto formattato per il be
    return be;
  }

  /**
   * Getter che verifica se l'oggetto range contenente i dati per il minimo principale.
   * @returns boolean con il risultato della verifica sull'oggetto.
   */
  get regolaRangeAsMinimoPrincipale(): boolean {
    // Esiste l'oggetto, recupero le informazioni per il controllo
    const sogliaMin: number = this.soglia_min;
    const sogliaMax: number = this.soglia_max;
    // Verifico se entrambe le soglie sono 0
    return sogliaMin === 0 && sogliaMax === 0;
    // #
  }
}
