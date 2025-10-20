import { HelperVo } from './helper-vo';
import { IJsonRegolaRangeVo, JsonRegolaRangeVo } from './json-regola-range-vo';
import {
  IJsonRegolaSogliaVo,
  JsonRegolaSogliaVo,
} from './json-regola-soglia-vo';

/**
 * Oggetto dal server con le informazioni dei json regola.
 */
export interface IJsonRegolaVo {
  canone_unitario?: number;
  canone_percentuale?: number;
  canone_minimo?: number;
  anno_riferimento?: number;
  ranges?: IJsonRegolaRangeVo[];
  soglia?: number;
  canone_minimo_soglia_inf?: number;
  canone_minimo_soglia_sup?: number;
}

export class JsonRegolaVo extends HelperVo {
  canone_unitario?: number;
  canone_percentuale?: number;
  canone_minimo?: number;
  anno_riferimento?: number;
  ranges?: JsonRegolaRangeVo[];
  soglia?: JsonRegolaSogliaVo;

  constructor(iJRVo?: IJsonRegolaVo) {
    super();

    this.canone_unitario = iJRVo?.canone_unitario;
    this.canone_percentuale = iJRVo?.canone_percentuale;
    this.canone_minimo = iJRVo?.canone_minimo;
    this.anno_riferimento = iJRVo?.anno_riferimento;
    this.ranges = this.convertJsonRegolaRangeVo(iJRVo?.ranges);

    // Creo l'oggetto per la gestione dei dati della soglia
    let soglia = iJRVo?.soglia;
    let canone_minimo_soglia_inf = iJRVo?.canone_minimo_soglia_inf;
    let canone_minimo_soglia_sup = iJRVo?.canone_minimo_soglia_sup;
    let iSoglia: IJsonRegolaSogliaVo;
    iSoglia = { soglia, canone_minimo_soglia_inf, canone_minimo_soglia_sup };
    // Assegno i dati per la soglia
    this.soglia = new JsonRegolaSogliaVo(iSoglia);
  }

  /**
   * Funzione di conversione di una lista di dati.
   * @param iJRRVoList IJsonRegolaRangeVo[] con la lista da convertire.
   * @returns JsonRegolaRangeVo[] con la lista convertita.
   */
  convertJsonRegolaRangeVo(
    iJRRVoList?: IJsonRegolaRangeVo[]
  ): JsonRegolaRangeVo[] {
    // Verifico l'input
    iJRRVoList = iJRRVoList ?? [];
    // Effettuo la conversione della lista dati
    return iJRRVoList.map((iJRRVo: IJsonRegolaRangeVo) => {
      // Creo l'oggetto
      return new JsonRegolaRangeVo(iJRRVo);
    });
  }

  toServerFormat(): IJsonRegolaVo {
    // Converto le lista dati
    // # json_ranges
    let jsonRanges: IJsonRegolaRangeVo[];
    jsonRanges = this.listToServerFormat(this.ranges) as IJsonRegolaRangeVo[];
    // I json ranges devono essere ritornati solo se non sono vuoti
    if (jsonRanges.length === 0) {
      // Non esistono ranges, imposto ad undefined la proprietà di ritorno
      jsonRanges = undefined;
    }

    // Definisco l'oggetto per il server
    let be: IJsonRegolaVo = {
      canone_unitario: this.canone_unitario,
      canone_percentuale: this.canone_percentuale,
      canone_minimo: this.canone_minimo,
      anno_riferimento: this.anno_riferimento,
      ranges: jsonRanges,
      soglia: this.soglia?.soglia,
      canone_minimo_soglia_inf: this.soglia?.canone_minimo_soglia_inf,
      canone_minimo_soglia_sup: this.soglia?.canone_minimo_soglia_sup,
    };

    // Ritorno l'oggetto formattato per il be
    return be;
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione di supporto che cerca all'interno dei ranges della regola quel range contenente i dati per il minimo principale.
   * Il minimo principale è definito da un range che ha range.soglia_min e range.soglia_max == 0.
   * @returns number con l'indice posizionale dell'oggetto range per il calcolo del minimo principale, altrimenti -1.
   */
  indexRangeMinimoPrincipale(): number {
    // Recupero i dati per i ranges
    const ranges: JsonRegolaRangeVo[] = this.ranges;
    // Verifico se esiste la struttura dati
    if (!ranges || ranges.length === 0) {
      // Non esistono dati
      return -1;
    }

    // La struttura esiste, cerco all'interno della lista il range che ha le soglie a 0
    let iRangePrincipale: number;
    iRangePrincipale = ranges.findIndex((r: JsonRegolaRangeVo) => {
      // Lancio la funzione di verifica
      return r?.regolaRangeAsMinimoPrincipale ?? false;
      // #
    });

    // Ritorno il risultato della ricerca
    return iRangePrincipale;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica e ritorna la condizione: oggetto json regola con configurazioni ranges.
   * @returns boolean che definisce se la classe ha le informazioni delle regole ranges.
   */
  get isJsonRegolaRanges(): boolean {
    // Recupero i dati per i ranges
    const ranges: JsonRegolaRangeVo[] = this.ranges;
    // Verifico se esiste la struttura dati
    return ranges?.length > 0;
  }

  /**
   * Funzione di supporto che cerca all'interno dei ranges della regola, quel range contenente i dati per il minimo principale.
   * Il minimo principale è definito da un range che ha range.soglia_min e range.soglia_max == 0.
   * @returns JsonRegolaRangeVo con l'oggetto range con per il calcolo del minimo principale, altrimenti undefined.
   */
  get rangeMinimoPrincipale(): JsonRegolaRangeVo {
    // Verifico se esiste la struttura dati
    if (!this.isJsonRegolaRanges) {
      // Non esistono dati
      return undefined;
    }

    // La struttura esiste, cerco all'interno della lista il range che ha le soglie a 0
    let iRangePrincipale: number;
    iRangePrincipale = this.indexRangeMinimoPrincipale();

    // Verifico l'indice posizionale
    if (iRangePrincipale !== -1) {
      // Indice trovato, recupero i dati per i ranges
      const ranges: JsonRegolaRangeVo[] = this.ranges;
      // Ritorno l'oggetto dato l'indice posizionale
      return ranges[iRangePrincipale];
      // #
    } else {
      // Oggetto non trovato
      return undefined;
    }
  }

  /**
   * Getter per la proprietà indicata in applicazione come "Minimo principale".
   * Verrà recuperato il valore numerico che identifica tale informazione solo nel caso in cui sia definita la struttura dei ranges.
   */
  get minimoPrincipale(): number {
    // Verifico se esiste la struttura dati
    if (!this.isJsonRegolaRanges) {
      // Non esistono dati
      return undefined;
    }

    // La struttura esiste, cerco all'interno della lista il range che ha le soglie a 0
    let rangePrincipale: JsonRegolaRangeVo;
    rangePrincipale = this.rangeMinimoPrincipale;
    // Verifico se esiste il range per il minimo principale
    if (rangePrincipale) {
      // Esiste, ritorno il valore del canone minimo
      return rangePrincipale.canone_minimo ?? 0;
      // #
    }

    // Caso finale come default
    return undefined;
  }

  /**
   * Getter per la proprietà indicata in applicazione come "Minimo principale".
   * Verrà recuperato il valore numerico che identifica tale informazione solo nel caso in cui sia definita la struttura dei ranges.
   * @param minimoPrincipale number che definisce il valore del minimo principale da settare.
   */
  set minimoPrincipale(minimoPrincipale: number) {
    // Verifico se esiste la struttura dati
    if (!this.isJsonRegolaRanges) {
      // Non esistono dati, definisco almeno un array vuoto
      this.ranges = [];
    }

    // La struttura esiste, cerco all'interno della lista il range che ha le soglie a 0
    let iRangePrincipale: number;
    iRangePrincipale = this.indexRangeMinimoPrincipale();

    // Definisco le casistiche per la gestione dell'aggiornamento dati
    // # 1) minimoPrincipale esiste ed esiste il range con i dati per il minimo principale
    let case1: boolean;
    case1 = minimoPrincipale != undefined && iRangePrincipale !== -1;
    // # 2) minimoPrincipale esiste, ma non esiste il range con i dati per il minimo principale
    let case2: boolean;
    case2 = minimoPrincipale != undefined && iRangePrincipale === -1;
    // # 3) minimoPrincipale non esiste, ma esiste il range con i dati per il minimo principale
    let case3: boolean;
    case3 = minimoPrincipale == undefined && iRangePrincipale !== -1;
    // // # 4) minimoPrincipale non esiste e non esiste il range con i dati per il minimo principale
    // let case4: boolean;
    // case4 = minimoPrincipale == undefined && iRangePrincipale === -1;

    // Verifico le casistiche per la gestione
    if (case1) {
      // I dati esitono, recupero i dati per i ranges
      const ranges: JsonRegolaRangeVo[] = this.ranges;
      // Modifico l'oggetto andando ad aggiornare il canone minimo
      ranges[iRangePrincipale].canone_minimo = minimoPrincipale;
      // #
    } else if (case2) {
      // Non esiste un range per la gestione del canone minimo principale, imposto i dati
      let iJsonRegolaRange: IJsonRegolaRangeVo;
      iJsonRegolaRange = {
        canone_minimo: minimoPrincipale,
        soglia_min: 0,
        soglia_max: 0,
      };
      // Genero effettivamente l'oggetto regola range
      const regolaRange = new JsonRegolaRangeVo(iJsonRegolaRange);
      // Aggiungo l'elemento alla lista dei range
      this.ranges.push(regolaRange);
      // #
    } else if (case3) {
      // Manca l'importo in input, devo cancellare il range dalla lista
      this.ranges.splice(iRangePrincipale, 1);
      // #
    }
  }

  /**
   * Getter che recupera la lista dei ranges della regola, ma rimuovendo il range che rappresenta quello con il valore del minimo principale.
   * @returns JsonRegolaRangeVo[] con la lista dei ranges, senza l'oggetto che identifica l'informazione del minimo principale.
   */
  get rangesFE(): JsonRegolaRangeVo[] {
    // Verifico se esiste la struttura dati
    if (!this.isJsonRegolaRanges) {
      // Non esistono dati
      return [];
    }

    // Esistono i ranges, li recupero
    const ranges: JsonRegolaRangeVo[] = this.ranges;
    // Definisco una variabile e filtro la lista dei ranges
    let rangesFE: JsonRegolaRangeVo[];
    rangesFE = ranges.filter((r: JsonRegolaRangeVo) => {
      // Ritorno tutti gli oggetti che non risultano conenente i dati per il minimo principale
      return r && !r.regolaRangeAsMinimoPrincipale;
      // #
    });

    // Ritorno la lista generata
    return rangesFE ?? [];
  }
}
