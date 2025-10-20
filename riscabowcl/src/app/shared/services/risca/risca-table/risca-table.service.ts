import { Injectable } from '@angular/core';
import { concat, uniqWith } from 'lodash';
import { Subject } from 'rxjs';
import { LoggerService } from '../../../../core/services/logger.service';
import { RiscaTableDataConfig } from '../../../components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../consts/common-consts.consts';
import {
  RiscaSortConfig,
  RiscaSortTypes,
  RiscaTableSortStatus,
  RiscaTableSourceMap,
} from '../../../utilities';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';
import { ITableEnableChanges } from './utilities/risca-table.interfaces';

/**
 * Servizio di utility con funzionalità di comodo per la gestione degli script per il componente risca-table.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaTableService {
  /** Costante contenente le informazioni comuni all'applicazione. */
  private C_C = new CommonConsts();

  /** Subject che permette la propagazione dell'evento di abilitazione/disabilitazione delle input delle tabelle. */
  toggleEnableInputs$ = new Subject<ITableEnableChanges>();
  /** Subject che permette la propagazione dell'evento di reset delle informazioni delle input di una tabella. */
  resetInputsValues$ = new Subject<string>();
  /** Subject che permette la propagazione dell'evento: tutte le righe di tabella submittate. */
  allRowsSubmitted$ = new Subject<string>();
  /** Subject che permette la propagazione dell'evento: tutte le righe di tabella non submittate. */
  allRowsUnsubmitted$ = new Subject<string>();

  /**
   * Costruttore
   */
  constructor(
    private _logger: LoggerService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * ###########################
   * FUNZIONI DI GESTIONE EVENTI
   * ###########################
   */

  /**
   * Funzione che emette l'evento di aggiornamento di abilitazione delle input di una tabella.
   * @param update ITableEnableChanges con i dati d'aggiornamento di abilitazione delle input.
   */
  updateInputsAccess(update: ITableEnableChanges) {
    // Propago l'evento
    this.toggleEnableInputs$.next(update);
  }

  /**
   * Funzione che emette l'evento di abilitazione delle input di una tabella.
   * @param tableId string che definisce l'id della tabella d'andare ad aggiornare.
   */
  enableInputs(tableId: string) {
    // Definisco l'oggetto d'aggiornamento
    const update: ITableEnableChanges = { tableId, enable: true };
    // Propago l'evento
    this.updateInputsAccess(update);
  }

  /**
   * Funzione che emette l'evento di disabilitazione delle input di una tabella.
   * @param tableId string che definisce l'id della tabella d'andare ad aggiornare.
   */
  disableInputs(tableId: string) {
    // Definisco l'oggetto d'aggiornamento
    const update: ITableEnableChanges = { tableId, enable: false };
    // Propago l'evento
    this.updateInputsAccess(update);
  }

  /**
   * Funzione che emette l'evento di reset del valore delle input di una tabella.
   * @param tableId string che definisce l'id della tabella d'andare ad aggiornare.
   */
  resetInputs(tableId: string) {
    // Propago l'evento
    this.resetInputsValues$.next(tableId);
  }

  /**
   * Funzione che emette l'evento di submit per tutte le input della tabella.
   * @param tableId string che definisce l'id della tabella d'andare ad aggiornare.
   */
  formSubmitsInputs(tableId: string) {
    // Propago l'evento
    this.allRowsSubmitted$.next(tableId);
  }

  /**
   * Funzione che emette l'evento di unsubmit per tutte le input della tabella.
   * @param tableId string che definisce l'id della tabella d'andare ad aggiornare.
   */
  formUnsubmitsInputs(tableId: string) {
    // Propago l'evento
    this.allRowsUnsubmitted$.next(tableId);
  }

  /**
   * ############################
   * FUNZIONI DI GESTIONE TABELLA
   * ############################
   */

  /**
   * Semplice funzione di check per verificare se l'id della tabella A è il medesimo dell'id tabella B.
   * @param tableIdA string che definisce l'id della tabella A da verificare.
   * @param tableIdB string che definisce l'id della tabella B da verificare.
   * @returns boolean con il risultato del check.
   */
  sameTable(tableIdA: string, tableIdB: string): boolean {
    // Verifico e ritorno il risultato
    return tableIdA === tableIdB;
  }

  /**
   * Funzione che verifica se una proprietà è sortable, ossia: se la proprietà è definita dentro la configurazione dei sort.
   * @param sortConfig RiscaTableSortStatus contenente i sort.
   * @param sortField string che definisce il nome del campo di filtro.
   * @returns boolean che definisce se la proprietà è sortable.
   */
  isPropertySortable(
    sortConfig: RiscaTableSortStatus,
    sortField: string
  ): boolean {
    // Verifico l'input
    if (!sortConfig || !sortField) {
      return false;
    }
    // Verifico se sortConfig è un oggetto
    if (typeof sortConfig !== 'object') {
      return false;
    }

    // Ritorno se l'oggetto sortConfig ha una proprietà sortField
    return sortConfig.hasOwnProperty(sortField);
  }

  /**
   * Funzione di generazione degli stati del sort della tabella.
   * @param sourceMap Array di RiscaTableSourceMap per la generazione dell'oggetto di status.
   * @returns RiscaTableSortStatus con gli stati di sort.
   */
  generaSortStatusTable(
    sourceMap: RiscaTableSourceMap[]
  ): RiscaTableSortStatus {
    // Verifico che esista l'input
    if (!sourceMap || sourceMap.length === 0) {
      return {};
    }

    // Creo l'oggetto con gli stati
    const status: RiscaTableSortStatus = {};

    // Ciclo gli oggetti della source map
    for (let i = 0; i < sourceMap.length; i++) {
      // Estraggo l'oggetto
      const data = sourceMap[i];
      // Verifico se la proprietà è sortable
      if (data.header?.sortable) {
        // Aggiungo la proprietà con il relativo tipo di sort
        status[data.body.sourceData.property] = data.header.sortType;
      }
    }

    // Ritorno l'oggetto generato
    return status;
  }

  /**
   * Funzione che effettua il toggle dell'ordinamento, definendo campo e tipologia d'ordinamento.
   * @param sortConfig RiscaTableSortStatus contenente l'attuale configurazione di sort.
   * @param sortField string che definisce la proprietà per la quale effettuare il sort.
   * @returns RiscaSortConfig contenente nome e tipo di sort.
   */
  defineSortTable(
    sortConfig: RiscaTableSortStatus,
    sortField: string
  ): RiscaSortConfig {
    // Verifico l'input
    if (!this.isPropertySortable(sortConfig, sortField)) {
      return { field: sortField, type: RiscaSortTypes.nessuno };
    }

    // Verifico lo stato di sort della proprietà
    switch (sortConfig[sortField]) {
      case RiscaSortTypes.crescente:
        return { field: sortField, type: RiscaSortTypes.decrescente };
      case RiscaSortTypes.decrescente:
      case RiscaSortTypes.nessuno:
        return { field: sortField, type: RiscaSortTypes.crescente };
      default:
        return { field: sortField, type: RiscaSortTypes.nessuno };
    }
  }

  /**
   * Funzione che estrae il campo e la tipologia d'ordinamento se quest'ultima non è RiscaSortTypes.nessuno.
   * @param sortStatus RiscaTableSortStatus che definisce lo stato d'ordinamento.
   * @returns RiscaSortConfig con il campo da ordinare o undefined.
   */
  getOneSortTable(sortStatus: RiscaTableSortStatus): RiscaSortConfig {
    // Controllo l'input
    if (!sortStatus) {
      return;
    }

    // Creo un contenitore per il risultato
    let sortConfig: RiscaSortConfig;

    // Itero le proprietà del sort della tabella
    for (const [key, value] of Object.entries(sortStatus)) {
      // Verifico che il valore non sia RiscaSortTypes.nessuno
      if (value !== RiscaSortTypes.nessuno) {
        // Definisco chiave e valore per il sort
        sortConfig = { field: key, type: value };
        // Interrompo il ciclo
        break;
      }
    }

    // Ritorno l'oggetto
    return sortConfig;
  }

  /**
   * Funzione di set per i sort della tabella.
   * Verrà applicato il sort solo su una proprietà, le altre verranno disabilitate.
   * @param sortConfig RiscaTableSortStatus contenente i sort.
   * @param sortField string che definisce il nome del campo di filtro.
   * @param sortType RiscaSortTypes che definisce il tipo d'ordinamento. Per default è RiscaSortTypes.crescente.
   * @returns RiscaTableSortStatus con i filtri aggiornati.
   */
  setOneSortTable(
    sortConfig: RiscaTableSortStatus,
    sortField: string,
    sortType: RiscaSortTypes = RiscaSortTypes.crescente
  ): RiscaTableSortStatus {
    // Resetto gli status dell'oggetto
    const newSortConfig: RiscaTableSortStatus = {};

    // Itero le proprietà del sort della tabella
    for (const [key] of Object.entries(sortConfig)) {
      // Inserisco la chiave e resetto il valore
      newSortConfig[key] = RiscaSortTypes.nessuno;
    }

    // Definisco il filtro attivo
    if (sortField) {
      newSortConfig[sortField] = sortType;
    }
    // Ritorno l'oggetto di filtro
    return newSortConfig;
  }

  /**
   * Funzione di sort per la tabella.
   * Verrà applicato un solo sort alla volta.
   * @param dataTable Array di RiscaTableDataConfig<any> da ordinare.
   * @param sourceMap Array di RiscaTableSourceMap che definisce la configurazione della tabella.
   * @param sort RiscaTableSortStatus come configurazione per il sort.
   * @returns Array di any ordinato per sort.
   */
  sortOneTable<T>(
    dataTable: RiscaTableDataConfig<any>[],
    sourceMap: RiscaTableSourceMap[],
    sort: RiscaTableSortStatus
  ): any[] {
    // Verifico che gli input esistano
    if (!dataTable || dataTable.length === 0) {
      return [];
    }
    if (!sourceMap || !sort) {
      return dataTable;
    }

    // Definisco un array di ritorno, creando una copia dell'input
    let arraySort = [...dataTable];
    // Definisco una variabile contenitore per la proprietà da ordinare
    let sourceData: RiscaTableSourceMap;

    // Verifico il tipo di sort
    let { field, type } = this.getFieldAndType(sort);
    // Verifico che il type non sia none
    if (type === RiscaSortTypes.nessuno) {
      return dataTable;
    }

    // Recupero le informazioni dell'oggetto per le configurazioni
    sourceData = sourceMap.find((d: RiscaTableSourceMap) => {
      // Recupero il campo
      const sField = d?.body?.sourceData?.property;
      // Verifico che la colonna sia di dati
      const useSource = d?.body?.useSource;

      // Comparo le proprietà
      return field === sField && useSource;
    });

    // Definisco la tipologia del sort in base al tipo dell'oggetto della colonna
    switch (sourceData.body?.sourceData?.type) {
      case 'date':
        // Verifico se nella configurazione è definita la formattazione della data
        const dateFormat =
          sourceData.body.sourceData.dateFormat || this.C_C.DATE_FORMAT_VIEW;
        // Lancio il sort
        return arraySort.sort((a, b) => {
          return this.sortDatesString(a, b, field, type, dateFormat);
        });
      case 'string':
        // Lancio il sort
        return arraySort.sort((a, b) => {
          return this.sortStrings(a, b, field, type);
        });
      case 'number':
        // Lancio il sort
        return arraySort.sort((a, b) => {
          return this.sortNumbers(a, b, field, type);
        });
      default:
        return arraySort;
    }
  }

  /**
   * Funzione che estrae le informazioni da un oggetto RiscaTableSortStatus.
   * @param sort RiscaTableSortStatus dalla quale estrarre i dati.
   * @returns Oggettto { field: string; type: RiscaSortTypes; } con le informazioni.
   */
  public getFieldAndType(sort: RiscaTableSortStatus): {
    field: string;
    type: RiscaSortTypes;
  } {
    // Verifico il tipo di sort
    let field: string;
    let type: RiscaSortTypes;
    // Cerco all'interno dell'oggetto di sort il campo da ordinare
    for (let sortField in sort) {
      // Verifico se il campo non è RiscaSortTypes.nessuno
      if (sort[sortField] !== RiscaSortTypes.nessuno) {
        // Definisco il sortData
        field = sortField;
        type = sort[sortField] as RiscaSortTypes;
        // Interrompo il ciclo
        break;
      }
    }
    // Ritorno i dati
    return { field, type };
  }

  /**
   * Funzione di sort tra stringhe per i dati della tabella.
   * @param a RiscaTableDataConfig<any> con il primo oggetto di confronto.
   * @param b RiscaTableDataConfig<any> con il secondo oggetto di confronto.
   * @param property string che definisce il nome della property degli oggetti per il confronto.
   * @param sortType RiscaSortTypes che definisce la tipologia di ordinamento.
   * @returns number che definisce l'ordinamento, compatibile con la funzione Array.prototype.sort di Javascript.
   */
  private sortStrings<T>(
    a: RiscaTableDataConfig<any>,
    b: RiscaTableDataConfig<any>,
    property: string,
    sortType: RiscaSortTypes
  ): number {
    // Recupero la proprietà degli oggetti
    const stringA = a?.dataTable[property] || '';
    const stringB = b?.dataTable[property] || '';

    // Richiamo la funzione di compare
    return this._riscaUtilities.sortStrings(stringA, stringB, sortType);
  }

  /**
   * Funzione di sort tra numeri per i dati della tabella.
   * @param a RiscaTableDataConfig<any> con il primo oggetto di confronto.
   * @param b RiscaTableDataConfig<any> con il secondo oggetto di confronto.
   * @param property string che definisce il nome della property degli oggetti per il confronto.
   * @param sortType RiscaSortTypes che definisce la tipologia di ordinamento.
   * @returns number che definisce l'ordinamento, compatibile con la funzione Array.prototype.sort di Javascript.
   */
  private sortNumbers<T>(
    a: RiscaTableDataConfig<any>,
    b: RiscaTableDataConfig<any>,
    property: string,
    sortType: RiscaSortTypes
  ): number {
    // Recupero la proprietà degli oggetti
    const numberA = a?.dataTable[property] || 0;
    const numberB = b?.dataTable[property] || 0;

    // Richiamo la funzione di compare
    return this._riscaUtilities.sortNumbers(numberA, numberB, sortType);
  }

  /**
   * Funzione di sort tra date per i dati della tabella.
   * @param a RiscaTableDataConfig<any> con il primo oggetto di confronto.
   * @param b RiscaTableDataConfig<any> con il secondo oggetto di confronto.
   * @param property string che definisce il nome della property degli oggetti per il confronto.
   * @param sortType RiscaSortTypes che definisce la tipologia di ordinamento.
   * @param dateFormat string che definisce il formato della data visualizzato nella tabella.
   * @returns number che definisce l'ordinamento, compatibile con la funzione Array.prototype.sort di Javascript.
   */
  private sortDatesString<T>(
    a: RiscaTableDataConfig<any>,
    b: RiscaTableDataConfig<any>,
    property: string,
    sortType: RiscaSortTypes,
    dateFormat: string
  ): number {
    // Recupero la proprietà degli oggetti
    const dateA = a?.dataTable[property];
    const dateB = b?.dataTable[property];

    // Richiamo la funzione di compare
    return this._riscaUtilities.sortDatesString(
      dateA,
      dateB,
      dateFormat,
      sortType
    );
  }

  /**
   * Funzione che ritorna un oggetto dall'array source, dato un oggetto RiscaTableDataConfig<any> da cercare.
   * @param source Array di RiscaTableDataConfig<any> contenente tutti gli oggetti delle righe della tabella.
   * @param data RiscaTableDataConfig<any> che definisce il dataset da comparare per la ricerca.
   * @returns RiscaTableDataConfig<any> con l'oggetto trovato.
   */
  getElementSource<T>(
    source: RiscaTableDataConfig<any>[],
    data: RiscaTableDataConfig<any>
  ): RiscaTableDataConfig<any> {
    // Verifico l'input
    if (!source || !data) {
      return;
    }

    // Eseguo la ricerca, mediante la funzionalità getElementSourceById
    return this.getElementSourceById(source, data.id);
  }

  /**
   * Funzione che ritorna un oggetto dall'array source, dato un id da cercare.
   * @param source Array di RiscaTableDataConfig<any> contenente tutti gli oggetti delle righe della tabella.
   * @param id string che definisce l'id da comparare per la ricerca.
   * @returns RiscaTableDataConfig<any> con l'oggetto trovato.
   */
  getElementSourceById<T>(
    source: RiscaTableDataConfig<any>[],
    id: string
  ): RiscaTableDataConfig<any> {
    // Verifico l'input
    if (!source || !id) {
      return;
    }

    // Eseguo la ricerca
    return source.find((s) => s.id === id);
  }

  /**
   * Funzione di comodo che recupera da un array in input, tutti gli oggetti che risultano selezionati.
   * @param source Array di RiscaTableDataConfig<any> dalla quale estrarre i dati.
   * @returns Array di RiscaTableDataConfig<any> contenente tutti gli oggetti selezionati.
   */
  getSourcesSelected<T>(
    source: RiscaTableDataConfig<any>[]
  ): RiscaTableDataConfig<any>[] {
    // Verifico l'input
    if (!source) {
      return [];
    }

    // Effettuo una filter sulla proprietà selected
    return source.filter((s) => s.selected);
  }

  /**
   * Funzione di comodo che recupera da un array in input, l'oggetto che risulta selezionato.
   * @param source Array di RiscaTableDataConfig<any> dalla quale estrarre il dato.
   * @returns RiscaTableDataConfig<any> contenente tutti l'oggetto selezionato.
   */
  getSourceSelected<T>(
    source: RiscaTableDataConfig<any>[]
  ): RiscaTableDataConfig<any> {
    // Verifico l'input
    if (!source) {
      return;
    }

    // Effettuo una filter sulla proprietà selected
    return source.find((s) => s.selected);
  }

  /**
   * Funzione di comodo che aggiorna tutti valori di source, impostando un valore per tutte le checkbox.
   * @param source Array di RiscaTableDataConfig<any> contenente tutti gli oggetti delle righe della tabella.
   * @param checkAll boolean che definisce se tutte le checkbox devono essere checkate/uncheckate.
   * @returns Array di RiscaTableDataConfig<any> contenente i dati aggiornati.
   */
  updateAllCheckboxes<T>(
    source: RiscaTableDataConfig<any>[],
    checkAll: boolean
  ): RiscaTableDataConfig<any>[] {
    // Verifico l'input
    if (!source) {
      return [];
    }
    if (checkAll === undefined) {
      checkAll = false;
    }

    // Effettuo una map dell'array aggiornando il flag selected
    return source.map((s) => {
      // Modifico il flag selected
      s.selected = checkAll;
      // Ritorno l'oggetto
      return s;
    });
  }

  /**
   * Funzione che aggiorna il valore di check di un determinato oggetto dell'array in input.
   * @param source Array di RiscaTableDataConfig<any> contenente tutti gli oggetti delle righe della tabella.
   * @param id string che definisce l'id dell'oggetto RiscaTableDataConfig<any> d'aggiornare.
   * @param selection boolean che definisce con quale valore aggiornare l'oggetto.
   */
  updateCheckbox<T>(
    source: RiscaTableDataConfig<any>[],
    id: string,
    selection: boolean = false
  ) {
    // Verifico l'input
    if (!source) {
      return;
    }
    if (!id) {
      // Loggo un warning
      this._logger.warning('updateCheckbox', 'id not defined');
      // Blocco il flusso
      return;
    }

    // Cerco l'oggetto in source
    const iObj = source.findIndex((s) => s.id === id);
    // Verifico se è stato trovato l'oggetto
    if (iObj !== -1) source[iObj].selected = selection;
  }

  /**
   * Funzione che aggiorna il valore di radio di un determinato oggetto dell'array in input.
   * Tutti gli altri oggetti verranno settati a false.
   * @param source Array di RiscaTableDataConfig<any> contenente tutti gli oggetti delle righe della tabella.
   * @param id string che definisce l'id dell'oggetto RiscaTableDataConfig<any> d'aggiornare.
   */
  updateRadio<T>(source: RiscaTableDataConfig<any>[], id: string) {
    // Verifico l'input
    if (!source) {
      return;
    }
    if (!id) {
      // Loggo un warning
      this._logger.warning('updateRadio', 'id not defined');
      // Blocco il flusso
      return;
    }

    // Ciclo l'array
    source.forEach((s) => {
      // Aggiorno il flag selected sulla base degli id
      s.selected = s.id === id;
    });
  }

  /**
   * Funzione che, in base ai dati in input, va a gestiore le logiche di set automatico di selezione delle righe.
   * La modifica dati avverrà per riferimento.
   * @param sources RiscaTableDataConfig<any>[] con l'array di dati sorgente da gestire.
   * @param selected RiscaTableDataConfig<any>[] con l'array di dati contenente le righe già selezionate nella tabella.
   * @param comparator (a: RiscaTableDataConfig<any>, b: RiscaTableDataConfig<any>) => boolean con la funzione che definisce le logiche di comparazione tra dati.
   */
  setSelectedRows(
    sources: RiscaTableDataConfig<any>[],
    selected: RiscaTableDataConfig<any>[],
    comparator: (
      a: RiscaTableDataConfig<any>,
      b: RiscaTableDataConfig<any>
    ) => boolean
  ) {
    // Verifico l'input
    if (!sources || !selected || !comparator) {
      // Non ci sono le informazioni per gestire le logiche
      return;
    }

    // Itero la lista di elementi sorgente
    sources?.forEach((source: RiscaTableDataConfig<any>) => {
      // Verifico se all'interno della lista di oggetti selezionati è presente l'oggetto source
      const sourceCheck = selected?.some(
        (select: RiscaTableDataConfig<any>) => {
          // Faccio una verifica per id stato debitorio
          return comparator(source, select);
        }
      );
      // Assegno sovrascrivendo la gestione del flag selected
      source.selected = sourceCheck;
      // #
    });
  }

  /**
   * Funzione di supporto che verifica e ritorna la logica di comparazione.
   * Se l'input comparator non è definito, verrà ritornata una funzione di defualt.
   * @param comparator (a: RiscaTableDataConfig<any>, b: RiscaTableDataConfig<any>) => boolean con la logica custom di comparazione.
   * @returns (a: RiscaTableDataConfig<any>, b: RiscaTableDataConfig<any>) => boolean con la funzione di comparazione definita.
   */
  private defineComparator(
    comparator?: (
      a: RiscaTableDataConfig<any>,
      b: RiscaTableDataConfig<any>
    ) => boolean
  ): (a: RiscaTableDataConfig<any>, b: RiscaTableDataConfig<any>) => boolean {
    // Definisco una funzione di default per la comparazione tra elementi
    const defaultCompare = (
      a: RiscaTableDataConfig<any>,
      b: RiscaTableDataConfig<any>
    ): boolean => {
      // Effettuo la compare minima tra gli oggetti
      return a.id === b.id;
    };
    // Definisco la logica effettiva di comparazione
    const compareLogic = comparator ?? defaultCompare;

    // Ritorno la logica di compare
    return compareLogic;
  }

  /**
   * Funzione che aggiorna la lista di risorse selezionate basandosi sullo stato di selezione della riga in input.
   * Se row.selected == true allora bisogna aggiungere un elemento agli elementi selezionati.
   * Se row.selected == false allora bisogna rimuovere un elemento dagli elementi selezionati.
   * La modifica dati avverrà per riferimento.
   * @param sources RiscaTableDataConfig<any>[] con la lista di elementi selezionati nella tabella.
   * @param row RiscaTableDataConfig<any> con l'oggetto modificato per la selezione della riga.
   * @param comparator (a: RiscaTableDataConfig<any>, b: RiscaTableDataConfig<any>) => boolean con la funzione che definisce le logiche di comparazione tra dati.
   */
  updateSourcesSelected(
    sources: RiscaTableDataConfig<any>[],
    row: RiscaTableDataConfig<any>,
    comparator?: (
      a: RiscaTableDataConfig<any>,
      b: RiscaTableDataConfig<any>
    ) => boolean
  ) {
    // Verifico l'input
    if (!row) {
      // Manca la configurazione minima per le logiche
      return;
    }
    if (!sources) {
      // Effettuo un'inizializzazione dell'array
      sources = [];
    }

    // Definisco la logica effettiva di comparazione
    const compareLogic = this.defineComparator(comparator);

    // Verifico se la riga è stata aggiunta o rimossa
    if (row.selected === true) {
      // #######################################################################
      // La riga è stata aggiunta, verifico che non sia già presente nella lista
      // #######################################################################
      const sourceCheck = sources?.some((source: RiscaTableDataConfig<any>) => {
        // Faccio una verifica per id stato debitorio
        return compareLogic(source, row);
      });
      // Verifico se è già presenet
      if (!sourceCheck) {
        // Non esiste nell'array, aggiungo l'oggetto agli elementi
        sources.push(row);
      }
      // #
    } else if (row.selected === false) {
      // #######################################################################
      // La riga è stata rimossa, cerco all'interno dell'array l'oggetto
      // #######################################################################
      const iRowSel = sources?.findIndex(
        (source: RiscaTableDataConfig<any>) => {
          // Faccio una verifica per id stato debitorio
          return compareLogic(source, row);
        }
      );
      // Verifico se è stato trovato l'oggetto nell'array
      if (iRowSel != -1) {
        // Rimuovo l'oggetto dall'array
        sources.splice(iRowSel, 1);
      }
    }
  }

  /**
   * Funzione che mergia le informazioni tra due array di selezione.
   * L'array risultante sarà l'unione delle informazioni selezionate fino ad ora all'interno della tabella e l'array di dati selezionati per la paginazione attiva.
   * @param allSelected RiscaTableDataConfig<any>[] che definisce l'array che contiene tutti gli oggetti selezionati per tutte le possibile pagine della tabella navigate.
   * @param currentSelected RiscaTableDataConfig<any>[] che definisce l'array degli elementi selezionati per la sola paginazione attuale.
   * @param comparator (a: RiscaTableDataConfig<any>, b: RiscaTableDataConfig<any>) => boolean con la funzione che definisce le logiche di comparazione tra dati.
   * @returns RiscaTableDataConfig<any>[] con la lista di elementi unici selezionati durante tutta la navigazione della tabella.
   */
  mergeSourcesSelected(
    allSelected: RiscaTableDataConfig<any>[],
    currentSelected: RiscaTableDataConfig<any>[],
    comparator?: (
      a: RiscaTableDataConfig<any>,
      b: RiscaTableDataConfig<any>
    ) => boolean
  ): RiscaTableDataConfig<any>[] {
    // Verifico l'input
    if (!allSelected) {
      // Imposto un default
      allSelected = [];
    }
    if (!currentSelected) {
      // Imposto un default
      currentSelected = [];
    }

    // Definisco il contenitore dei dati da ritornare
    let finalSelected: RiscaTableDataConfig<any>[] = [];
    // Definisco la logica effettiva di comparazione
    const compareLogic = this.defineComparator(comparator);

    // Concateno gli array in un unico array
    finalSelected = concat(allSelected, currentSelected);
    // Vado a filtrare le informazioni in maniera tale da avere un array di elementi unici, dato il comparatore
    finalSelected = uniqWith(finalSelected, compareLogic);

    // Ritorno l'array finale generato
    return finalSelected;
  }
}
