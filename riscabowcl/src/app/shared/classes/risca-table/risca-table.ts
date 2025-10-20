import { clone, cloneDeep } from 'lodash';
import { from, Observable, of } from 'rxjs';
import { concatMap, delay, last, tap } from 'rxjs/operators';
import { RiscaTableDataConfig } from '../../components/risca/risca-table/utilities/risca-table.classes';
import {
  generateRandomId,
  samePaginazioni,
  sortObjects,
} from '../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaSortTypes,
  RiscaTableCss,
  RiscaTableInput,
  RiscaTablePagination,
} from '../../utilities';

/**
 * Interfaccia di supporto da far estendere alle classi table come supporto alla definizione dei metodi.
 */
export interface IRiscaTableClass<T> {
  /** Funzione di setup per la configurazione dati. */
  setupDataConfig: () => void;
  /** Funzione di setup per la configurazione css. */
  setupCssConfig: () => void;
  /** Funzione di setup per la configurazione di searcher, converter, comparator e/o altre funzioni della tabella. */
  setTableFunctions: () => void;
  /** Funzione di search di un oggetto all'inteno dell'array source. */
  searcher: (e: any, eSource: RiscaTableDataConfig<any>) => boolean;
  /** Funzione di parsing degli oggetti in input in oggetti compatibili con la tabella. */
  converter: (e: any) => T;
  /** Funzione che definisce le logiche di generazione dati per le sottorighe della tabella. */
  subRowsGenerator?: (e: any) => RiscaTableDataConfig<any>[];
}

/**
 * Classe usata per definire le funzionalità comuni delle classi table usate per il componente: risca-table.
 */
export class RiscaTableClass<T> {
  /** String costante che identifica la classe di stile per le tr disabilitate. */
  TR_DISABLE_CLASS: string = 'risca-tr-disabled';

  /** Input che definisce le configurazioni per gli stili della input. */
  cssConfig: RiscaTableCss;
  /** Input che definisce le configurazioni dati per la tabella. */
  dataConfig: RiscaTableInput;
  /** Input che definisce il source dati per la tabella. */
  source: RiscaTableDataConfig<any>[];
  /** Input che definisce il source dati per la tabella che risulta selezionato. */
  sourceSelected: RiscaTableDataConfig<any>[] = [];
  /** Boolean che gestisce la condizione di tutti gli elementi della tabella attivati. */
  tutteLeRigheSelezionate: boolean = false;
  /** Funzione di search di un oggetto all'inteno dell'array source. */
  searcher: (e: any, eSource: RiscaTableDataConfig<any>) => boolean;
  /** Funzione di parsing degli oggetti in input, in oggetti compatibili con la tabella. */
  converter: (e: any) => T;
  /** Funzione che permette di comparare due righe della tabella. */
  comparatorRows?: (
    a: RiscaTableDataConfig<any>,
    b: RiscaTableDataConfig<any>
  ) => boolean;
  /** Input che definisce la funzione che permette di comparare due oggetti originali dentro due righe della tabella. */
  comparatorOriginals?: (a: any, b: any) => boolean;
  /** Funzione che definisce le logiche di generazione dati per le sottorighe della tabella. */
  subRowsGenerator?: (e: any) => RiscaTableDataConfig<any>[];

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * #########################################
   * FUNZIONI DI MANIPOLAZIONE DATI PER SOURCE
   * #########################################
   */

  /**
   * Funzione di comodo che effettua la rimozione di tutti gli elementi della tabella.
   */
  clear() {
    // Rimuovo gli elementi
    this.source = [];
  }

  updateSource() {
    // Creo una copia dei dati di source
    const cloned = cloneDeep(this.source);
    // Resetto la variabile
    this.source = [];
    // Riassegno la variabile di source per il trigger della change detection
    this.source = cloned;
  }

  /**
   * Setter che gestisce l'inserimento di un array di elementi.
   * @param elements Array di any da definire come source.
   * @param converter (e: any) => T; converter personalizzato.
   */
  setElements(elements: any[], converter?: (e: any) => T) {
    // Definisco il set per source
    this.source = elements
      ? this.convertElementsToSource(elements, converter)
      : [];
  }

  /**
   * Funzione che ritorna le informazioni inserite all'interno come valore original.
   * @param extras any che permette di passare alla funzione un qualsiasi parametro. E' pensato nel caso in cui venga fatta l'override della funzione.
   * @returns any[] con la lista di oggetti originali recuperati dalla struttura della tabella.
   */
  getDataSource(extras?: any): any[] {
    // Ritorno tutti gli oggetti originali da source
    return this.source?.map((e) => e.original) || [];
  }

  /**
   * Funzione che effettua una ricerca all'interno dei dati della tabella.
   * Verrà ricercato l'elemento passato in input, mediante la logica di "find" passata in input.
   * Se viene trovato un oggetto, verrà ritornato (true).
   * @param element any da ricercare tra gli elementi della tabella.
   * @param findLogic (e: any, eSource: RiscaTableDataConfig<any>) => boolean che definisce la logica di confronto tra i dati.
   */
  isElementInTable(
    element: any,
    findLogic: (e: any, eSource: RiscaTableDataConfig<any>) => boolean
  ): boolean {
    // Verifico l'input
    if (!element || !findLogic) {
      // Non ci sono configurazioni minime
      return false;
    }

    // Ritorno il risultato della ricerca
    return this.source.some((eSource: RiscaTableDataConfig<any>) => {
      // Lancio la funzione di ricerca
      return findLogic(element, eSource);
    });
  }

  /**
   * Funzione che cerca nei dati della classe le informazioni di un elemento.
   * @param element any da cercare.
   * @param findLogic Funzione: (e: any, eSource: RiscaTableDataConfig<any>) => boolean; che definisce le logiche di ricerca di un oggetto nella lista source.
   * @returns number che indica l'indice dell'elemento. Se -1 l'elemento non è stato trovato.
   */
  findElementIndex(
    element: any,
    findLogic?: (e: any, eSource: RiscaTableDataConfig<any>) => boolean
  ): number {
    // Verifico l'input esista
    if (!element) {
      return -1;
    }

    // Definisco una variabile per la funzione di ricerca
    const search = this.handleFindLogic(findLogic);
    // Cerco l'indice dell'oggetto nella lista
    return this.source.findIndex((eSource: RiscaTableDataConfig<any>) => {
      // Lancio la funzione di ricerca
      return search(element, eSource);
    });
  }

  /**
   * Funzione che recupera nei dati della classe le informazioni di un elemento.
   * @param element any da recuperare.
   * @param getLogic Funzione: (e: any, eSource: RiscaTableDataConfig<any>) => boolean; che definisce le logiche di ricerca di un oggetto nella lista source.
   * @returns RiscaTableDataConfig<any> con l'oggetto trovato. Se non viene trovato ritorna undefined.
   */
  getElement(
    element: any,
    getLogic?: (e: any, eSource: RiscaTableDataConfig<any>) => boolean
  ): RiscaTableDataConfig<any> {
    // Verifico l'input esista
    if (!element) {
      return undefined;
    }

    // Cerco l'indice dell'oggetto nella lista
    const iObj = this.findElementIndex(element, getLogic);
    // Rimuovo l'oggetto
    return this.getElementByIndex(iObj);
  }

  /**
   * Funzione che recupera dai dati della classe le informazioni di un elemento dato il suo indice.
   * @param i number che definisce l'indice dell'oggetto.
   * @returns RiscaTableDataConfig<any> con l'oggetto trovato. Se non viene trovato ritorna undefined.
   */
  getElementByIndex(i: number): RiscaTableDataConfig<any> {
    // Verifico l'input esista
    if (i == undefined || i < 0) {
      return undefined;
    }

    // Ritorno l'oggetto dato l'indice
    return this.source[i];
  }

  /**
   * Funzione che resetta l'attuale lista di elementi nella tabella e imposta una lista di nuovi elementi in input.
   * @param newElements any[] da aggiungere.
   * @returns boolean che indica se l'elemento è stato aggiunto.
   */
  setNewElements(newElements: any[]): boolean {
    // Verifico l'input esista
    if (!newElements) {
      // Non aggiung niente
      return false;
    }

    // Sostituisco il source con la nuova lista di elementi
    this.source = this.convertElementsToSource(newElements);
    // Elemento aggiunto
    return true;
  }

  /**
   * Funzione che aggiunge ai dati della classe le informazioni di un nuovo elemento.
   * @param newElement any da aggiungere.
   * @returns boolean che indica se l'elemento è stato aggiunto.
   */
  addElement(newElement: any): boolean {
    // Verifico l'input esista
    if (!newElement) {
      // Non aggiung niente
      return false;
    }

    // Aggiungo al source il nuovo elemento modificando il riferimento
    this.source = [
      ...this.source,
      ...[this.convertElementToSource(newElement)],
    ];
    // Elemento aggiunto
    return true;
  }

  /**
   * Funzione che aggiunge ai dati della classe le informazioni di n nuovi elementi.
   * @param newElements Array di any da aggiungere.
   * @param safeDOMMode boolean che definisce se l'aggiunta deve avvenire in safe DOM mode. Ossia invece di mettere dentro tutti gli elementi insieme, viene aggiunta una riga alla volta permettendo al DOM di renderizzare con calma le informazioni. Per default è: false.
   * @param delayTime number che definisce i millisecondi da far passare prima di aggiungere un'altra riga. Per default è: 100.
   * @returns Observable<boolean> che indica se l'elemento è stato aggiunto.
   */
  addElements(
    newElements: any[],
    safeDOMMode: boolean = false,
    delayTime: number = 100
  ): Observable<boolean> {
    // Verifico l'input esista
    if (!newElements || newElements.length === 0) {
      // Non aggiungo elementi
      return of(false);
    }

    // Verifico la modalità d'inserimento
    if (safeDOMMode) {
      // Utilizzo gli operatori rxjs per creare un effetto di delay e aggiunta progressiva
      return from(newElements).pipe(
        concatMap((e: any) => {
          // Ritorno il singolo elemento con un delay
          return of(e).pipe(
            delay(delayTime),
            tap((e: any) => {
              // Aggiungo l'elemento alla tabella
              this.addElement(e);
            })
          );
        }),
        // Verifico se è l'ultimo elemento della from
        last(() => {
          // Ritorno che l'operazione è conclusa
          return true;
        })
      );
      // #
    } else {
      // Aggiungo al source tutti i nuovi elementi
      this.source = [
        ...this.source,
        ...this.convertElementsToSource(newElements),
      ];
      // Elementi aggiunti
      return of(true);
    }
  }

  /**
   * Funzione che aggiunge ai dati della classe le informazioni di n nuovi elementi.
   * @param newElements Array di any da aggiungere.
   * @returns boolean che indica se l'elemento è stato aggiunto.
   */
  addElementsBlock(newElements: any[]): boolean {
    // Verifico l'input esista
    if (!newElements || newElements.length === 0) {
      // Non aggiungo elementi
      return false;
    }

    // Aggiungo al source tutti i nuovi elementi
    this.source = [
      ...this.source,
      ...this.convertElementsToSource(newElements),
    ];
    // Elementi aggiunti
    return true;
  }

  /**
   * Funzione che rimuove dai dati della classe una riga della tabella.
   * @param delRow RiscaTableDataConfig<any> da rimuovere.
   * @returns boolean che indica se l'elemento è stato rimosso.
   */
  removeRow(delRow: RiscaTableDataConfig<any>): boolean {
    // Verifico l'input esista
    if (!delRow) {
      return false;
    }

    // Cerco l'indice dell'oggetto nella lista
    const iObj = this.source?.findIndex((row: RiscaTableDataConfig<any>) => {
      // Verifico per stesso id
      return delRow?.id === row.id;
    });
    // Rimuovo l'oggetto
    return this.removeElementByIndex(iObj);
  }

  /**
   * Funzione che rimuove dai dati della classe le informazioni di un elemento.
   * @param delElement any da rimuovere.
   * @param removeLogic Funzione: (e: any, eSource: RiscaTableDataConfig<any>) => boolean; che definisce le logiche di ricerca di un oggetto nella lista source.
   * @returns boolean che indica se l'elemento è stato rimosso.
   */
  removeElement(
    delElement: any,
    removeLogic?: (e: any, eSource: RiscaTableDataConfig<any>) => boolean
  ): boolean {
    // Verifico l'input esista
    if (!delElement) {
      return false;
    }

    // Cerco l'indice dell'oggetto nella lista
    const iObj = this.findElementIndex(delElement, removeLogic);
    // Rimuovo l'oggetto
    return this.removeElementByIndex(iObj);
  }

  /**
   * Funzione che rimuove dai dati della classe le informazioni di un elemento dato il suo indice.
   * @param iObj number che definisce l'indice per l'eliminazione di un oggetto.
   * @returns boolean che indica se l'elemento è stato rimosso.
   */
  removeElementByIndex(iObj: number): boolean {
    // Se l'iObj non è -1 è stato trovato l'oggetto
    if (iObj !== undefined && iObj > -1) {
      // Assegno ad una variabile locale la copia dell'array
      const newSource = this.source.slice() || [];
      // Rimuovo dall'array l'oggetto
      newSource.splice(iObj, 1);
      // Riassegno l'oggetto per attivare la change detection
      this.source = newSource;
      // Elemento rimosso
      return true;
    }

    // Elemento non rimuovibile
    return false;
  }

  /**
   * Funzione che effettua il convert di un oggetto e lo sostituisce all'oggetto RiscaTableDataConfig<any> di riferimento.
   * La funzione aggiornerà le proprietà:
   * - original;
   * - dataTable;
   * Lasciando però inviariato la proprietà: id; così da poter riferirsi sempre allo stesso oggetto.
   * @param element any che verrà convertito e definito come oggetto da aggiornare per updElement.
   * @param row RiscaTableDataConfig<any> da aggiornare.
   * @param updateLogic Funzione: (e: any, eSource: RiscaTableDataConfig<any>) => boolean; che definisce le logiche di ricerca di un oggetto nella lista source.
   * @returns boolean che indica se l'elemento è stato aggiornato.
   */
  convertAndUpdateElement(
    element: any,
    row: RiscaTableDataConfig<any>,
    updateLogic?: (
      e: RiscaTableDataConfig<any>,
      eSource: RiscaTableDataConfig<any>
    ) => boolean
  ): boolean {
    // Verifico l'input
    if (!element || !row) {
      return false;
    }

    // Effettuo il convert dell'oggetto
    const { original, dataTable } = this.convertElementToSource(element);
    // Aggiorno le proprietà dell'oggetto updElement
    row.original = original;
    row.dataTable = dataTable;

    // Richiamo e ritorno la funzione di aggiornamento
    return this.updateElement(row, updateLogic);
  }

  /**
   * Funzione che aggiorna dai dati della classe le informazioni di un elemento.
   * @param updElement RiscaTableDataConfig<any> da aggiornare.
   * @param updateLogic Funzione: (e: any, eSource: RiscaTableDataConfig<any>) => boolean; che definisce le logiche di ricerca di un oggetto nella lista source.
   * @returns boolean che indica se l'elemento è stato aggiornato.
   */
  updateElement(
    updElement: RiscaTableDataConfig<any>,
    updateLogic?: (
      e: RiscaTableDataConfig<any>,
      eSource: RiscaTableDataConfig<any>
    ) => boolean
  ): boolean {
    // Verifico l'input esista
    if (!updElement) {
      return false;
    }

    // Cerco l'indice dell'oggetto nella lista
    const iObj = this.findElementIndex(updElement, updateLogic);
    // Rimuovo l'oggetto
    return this.updateElementByIndex(iObj, updElement);
  }

  /**
   * Funzione che aggiorna dai dati della classe le informazioni di un elemento dato il suo indice.
   * @param iObj number che definisce l'indice per l'aggiornamento di un oggetto.
   * @param updElement RiscaTableDataConfig<any> da aggiornare.
   * @returns boolean che indica se l'elemento è stato aggiornato.
   */
  updateElementByIndex(
    iObj: number,
    updElement: RiscaTableDataConfig<any>
  ): boolean {
    // Se l'iObj non è -1 è stato trovato l'oggetto
    if (iObj !== undefined && iObj !== -1) {
      // Assegno ad una variabile locale la copia dell'array
      const newSource = this.source.slice() || [];
      // Rimuovo dall'array l'oggetto
      newSource.splice(iObj, 1, updElement);
      // Riassegno l'oggetto per attivare la change detection
      this.source = newSource;
      // Elemento aggiornato
      return true;
    }

    // Elemento non aggiornato
    return false;
  }

  /**
   * #############################################
   * FUNZIONI PER LA GESTIONE DEI DATI SELEZIONATI
   * #############################################
   */

  /**
   * Funzione di comodo che aggiorna tutti valori di source, impostando un valore per tutte le checkbox.
   * @param checkAll boolean che definisce se tutti i valori devono essere selected o no.
   */
  setAllElementsSelection(checkAll?: boolean) {
    // Verifico l'input
    if (checkAll === undefined) {
      checkAll = false;
    }

    // Effettuo un map del source aggiornando il flag selected
    this.source = this.source.map((s) => {
      // Imposto il flag selected
      s.selected = checkAll;
      // Ritorno l'oggetto aggiornato
      return s;
    });
  }

  /**
   * Funzione di selezione dei valori selected all'interno della tabella.
   * @param selectedLogic (sourceValue: any) => boolean che definisce le logiche per impostare i valori di default.
   * @returns RiscaTableDataConfig<any>[] con la lista delle righe che sono risultate selezionate dalla logica.
   */
  setElementsSelection(
    selectedLogic: (originalValue: any) => boolean
  ): RiscaTableDataConfig<any>[] {
    // Verifico l'input
    if (!selectedLogic) {
      return;
    }

    // Definisco un contenitore per le informazioni che risulteranno selezionate a seguito della logica
    const rowsSelected: RiscaTableDataConfig<any>[] = [];

    // Effettuo un map del source aggiornando il flag selected
    this.source = this.source.map((sourceData: RiscaTableDataConfig<any>) => {
      // Recupero l'oggetto originale
      const originalValue = sourceData.original;
      // Aggiorno la variabile di selected sulla base della funzione in input
      sourceData.selected = selectedLogic(originalValue);

      // Verifico se il dato è stato selezionato effettivamente
      if (sourceData.selected) {
        // La riga è stata selezionata, l'aggiunto al contenitore
        rowsSelected.push(cloneDeep(sourceData));
        // #
      }

      // Ritorno l'oggetto aggiornato
      return sourceData;
    });

    // Ritorno la lista delle righe selezionate
    return rowsSelected;
  }

  /**
   * Funzione di selezione del valore di default all'interno della tabella.
   * Questa funzione è indicata per il setup dei dati di default per la gestione dei radio.
   * Solo il primo oggetto che matcha con il risultato della funzione di selezione sarà aggiornato, gli altri valori saranno impostati a false.
   * @param selectedLogic (sourceValue: any) => boolean che definisce le logiche per impostare il valore di default.
   * @return boolean che definisce se è stato definito come selezionato almeno un elemento.
   */
  setElementSelectedExclusive(
    selectedLogic: (sourceValue: any) => boolean
  ): boolean {
    // Verifico l'input
    if (!selectedLogic) {
      return;
    }

    // Definisco un flag per indicare se è stato selezionato un oggetto
    let isSelected = false;

    // Effettuo un map del source aggiornando il flag selected
    this.source = this.source.map((sourceData) => {
      // Verifico se è già stato selezionato un oggetto
      if (isSelected) {
        // Imposto il selected a false
        sourceData.selected = false;
        // #
      } else {
        // Recupero l'oggetto originale
        const sourceValue = sourceData.original;
        // Aggiorno la variabile di selected sulla base della funzione in input
        sourceData.selected = selectedLogic(sourceValue);
        // Verifico se l'oggetto è stato selezionato
        if (sourceData.selected) {
          isSelected = true;
        }
      }

      // Ritorno l'oggetto aggiornato
      return sourceData;
    });

    // Ritorno il flag che indifica se è stato settato un elemento a selezionato
    return isSelected;
  }

  /**
   * Funzione che recupera gli oggetti marcati come selected.
   * @return Array di RiscaTableDataConfig<any> con gli oggetti attualmente selezionati.
   */
  getElementsSelected(): RiscaTableDataConfig<any>[] {
    // Effettuo una filter per individuare gli oggetti selezionati
    const selectedElements = this.source.filter((s) => s.selected);
    // Ritorno la riga
    return selectedElements;
  }

  /**
   * Funzione che recupera l'oggetto marcato come selected.
   * ATTENZIONE: se più elementi risultano selezionati, verrà ritornato solo il primo valore che risulta selected.
   * @return RiscaTableDataConfig<any> con l'oggetto attualmente selezionato.
   */
  getElementSelected(): RiscaTableDataConfig<any> {
    // Effettuo una findIndex per individuare l'oggetto
    const iObj = this.source.findIndex((s) => s.selected);
    // Ritorno la riga
    return this.getElementByIndex(iObj);
  }

  /**
   * Funzione che aggiorna direttamente la variabile che identifica le righe di tabella selezionate.
   * @param rows RiscaTableDataConfig<any>[] con le righe di tabella selezionate.
   */
  setSourceSelected(rows: RiscaTableDataConfig<any>[]) {
    // Verifico l'input
    if (!rows) {
      // Niente configurazione
      return;
    }

    // Esistono dati, li assegno localmente
    this.sourceSelected = clone(rows);
  }

  /**
   * ###########################
   * FUNZIONI DI SORT DEL SOURCE
   * ###########################
   */

  /**
   * Funzione che permette di ordinare gli elementi della tabella manualmente, definendo una specifica logica di ordinamento.
   * @param sortLogic Funzione (rowA: RiscaTableDataConfig<any>, rowB: RiscaTableDataConfig<any>) => number che definisce le logiche di sort.
   * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
   */
  sortElements(
    sortLogic: (
      rowA: RiscaTableDataConfig<any>,
      rowB: RiscaTableDataConfig<any>
    ) => number,
    sortType?: RiscaSortTypes
  ) {
    // Definisco la funzione di sort dati
    const sort = (
      a: RiscaTableDataConfig<any>,
      b: RiscaTableDataConfig<any>
    ) => {
      // Richiamo la funzione di sort di utility
      return sortObjects(a, b, sortLogic, sortType);
    };

    // Richiamo il sort dei dati
    const sortedSource = this.source.sort(sort);
    // Ri-assegno l'array per la change detection
    this.source = sortedSource;
  }

  /**
   * ###########################################
   * FUNZIONE CHE GESTISCE LE LOGICHE DI RICERCA
   * ###########################################
   */

  /**
   * Funzione che gestisce la tipologia di search per i dati nell'array source.
   * @param customFind (e: any, eSource: RiscaTableDataConfig<any>) => boolean; funzione personalizzata per la ricerca.
   * @returns (e: any, eSource: RiscaTableDataConfig<any>) => boolean; che definisce la funzione di ricerca da usare.
   */
  private handleFindLogic(
    customFind: (e: any, eSource: RiscaTableDataConfig<any>) => boolean
  ): (e: any, eSource: RiscaTableDataConfig<any>) => boolean {
    // Definisco una funzione locale di ricerca dell'oggetto da eliminare all'interno di source
    const searchLocal = (e: any, s: RiscaTableDataConfig<any>): boolean => {
      // Effettuo un confronto tra referenze degli oggetti
      return e.id === s.id;
    };
    // Definisco una variabile per la funzione di ricerca
    const search = customFind || this.searcher || searchLocal;
    // Ritorno la funzione di ricerca
    return search;
  }

  /**
   * #################################################
   * FUNZIONI DI PARSING INPUT PRINCIPALE VERSO SOURCE
   * #################################################
   */

  /**
   * Funzione di convert dell'array in input in un array di T.
   * @param elements Array di any da convertire.
   * @param converter (e: any) => T; converter personalizzato.
   * @param subRowsGenerator (e: any) => RiscaTableDataConfig<any>[]; converter personalizzato per la sottoriga.
   * @returns Array di RiscaTableDataConfig<any> convertito.
   */
  protected convertElementsToSource(
    elements: any[],
    converter?: (e: any) => T,
    subRowsGenerator?: (e: any) => RiscaTableDataConfig<any>[]
  ): RiscaTableDataConfig<any>[] {
    // Verifico che esista l'input
    if (!elements) {
      return [];
    }

    // Rimuovo eventuali oggetti undefined dall'array
    elements = elements.filter((e) => e !== undefined);
    // Ritorno il mapping dell'arrray
    return elements.map((e: any) =>
      // Ritorno la conversione del singolo oggetto
      this.convertElementToSource(e, converter, subRowsGenerator)
    );
  }

  /**
   * Funzione di convert dell'oggetto in input in un oggetto T.
   * @param element any da convertire.
   * @param converter (e: any) => any; converter personalizzato.
   * @param subRowsGenerator (e: any) => RiscaTableDataConfig<any>[]; converter personalizzato per la sottoriga.
   * @returns T convertito.
   */
  protected convertElementToSource(
    element: any,
    converter?: (e: any) => any,
    subRowsGenerator?: (e: any) => RiscaTableDataConfig<any>[]
  ): RiscaTableDataConfig<any> {
    // Verifico l'oggetto esista
    if (!element) {
      return undefined;
    }

    // Dichiaro i contenitori per le funzioni di conversione
    let convert: (e: any) => any;
    let subRowsGenerate: (e: any) => RiscaTableDataConfig<any>[];

    // Definisco il converter per la riga principale
    convert = converter || this.converter;
    // Verifico che esista un converter
    if (!convert) {
      // Il convertitore è obbligatorio!
      throw new Error('convertElementToSource | no converter defined');
      // #
    }

    // Definisco il converter per le sotto righe, rispetto alla riga principale
    subRowsGenerate = subRowsGenerator || this.subRowsGenerator;
    // Verifico e creo la lista di oggetti per la sotto riga
    const subRows = subRowsGenerate ? subRowsGenerate(element) : undefined;

    // Definisco un oggetto RiscaTableDataConfig<any>
    const elementParsed: RiscaTableDataConfig<any> = {
      // Salvo il dato originale
      original: element,
      // Utilizzo la funzione di parsing per convertire l'oggetto
      dataTable: convert(element),
      // Genero un id randomico che identifichi la riga
      id: generateRandomId(3),
      // Per default l'oggetto non è selezionato
      selected: false,
      // Gestisco il parsing per le sotto righe della riga principale
      tableDataConfigSubs: subRows,
    };

    // Ritorno l'oggetto convertito
    return elementParsed;
  }

  /**
   * ##########################
   * FUNZIONI DELLA PAGINAZIONE
   * ##########################
   */

  /**
   * Aggiorna i dati della paginazione dopo la ricerca sul BE
   * @param paginazione RicercaPaginataResponse con i dati della ricerca
   */
  updatePaginazioneAfterSearch(paginazione: RiscaTablePagination) {
    // Aggiorno l'oggetto della paginazione
    this.updatePaginazione(paginazione, true);
  }

  /**
   * Aggiorna la paginazione impostando i nuovi parametri generati dal servizio di ricerca.
   * @param newPaginazione Parametro opzionale. Se presente, sostituisce i dati della paginazione corrente con quelli di questa.
   */
  updatePaginazione(
    newPaginazione: RiscaTablePagination = null,
    emitEvent: boolean = false
  ) {
    // Riassegna i dati della paginazione se richiesto
    if (!newPaginazione) {
      // Non esiste una configurazione
      return;
    }

    // Controllo se è cambiata la paginazione
    const cambiata = this.paginazioneIsChanged(newPaginazione);
    // Controllo
    if (cambiata) {
      // Verifico se esiste una paginazione locale
      if (this.dataConfig.pagination) {
        // Aggiorno i campi della paginazione
        this.dataConfig.pagination.total = newPaginazione.total;
        this.dataConfig.pagination.currentPage = newPaginazione.currentPage;
        this.dataConfig.pagination.sortBy = newPaginazione.sortBy;
        this.dataConfig.pagination.sortDirection = newPaginazione.sortDirection;
      } else {
        // Assegno direttamente il nuovo oggetto di paginazione localmente
        this.dataConfig.pagination = newPaginazione;
      }

      // Verifico se è richiesto il refresh dati
      if (emitEvent) {
        // Creo un nuovo oggetto data config, così da creare un nuovo riferimento
        const newDataConfig = new RiscaTableInput(undefined);
        // Assegno manualmente le informazioni già presenti dentro dataConfig locale
        newDataConfig.pagination = this.dataConfig.pagination;
        newDataConfig.sorting = this.dataConfig.sorting;
        newDataConfig.sourceMap = this.dataConfig.sourceMap;
        newDataConfig.sourceMapSub = this.dataConfig.sourceMapSub;
        // Il fatto di "emettere" l'aggiornamento implica aggiornare il riferimento all'oggetto dataConfig
        this.dataConfig = newDataConfig;
      }
    }
  }

  /**
   * Controlla se la paginazione corrente è uguale a quella in input
   * @param paginazioneNuova RiscaTablePagination oggetto di paginazione
   * @returns boolean che ritorna se la paginazione è cambiata.
   */
  paginazioneIsChanged(paginazioneNuova: RiscaTablePagination) {
    // Richiamo la funzione di utility
    return !samePaginazioni(paginazioneNuova, this.dataConfig?.pagination);
  }

  /**
   * Ottiene l'oggetto corrente della paginazione
   * @returns RiscaTablePagination oggetto della paginazione
   */
  getPaginazione() {
    return this.dataConfig?.pagination;
  }
}
