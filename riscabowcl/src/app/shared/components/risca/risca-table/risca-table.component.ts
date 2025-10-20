import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { clone, cloneDeep, compact, intersectionWith } from 'lodash';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { LoggerService } from '../../../../core/services/logger.service';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaTableConsts } from '../../../consts/risca/risca-table.consts';
import { RiscaTableService } from '../../../services/risca/risca-table/risca-table.service';
import {
  IRiscaTableSMHACheckbox,
  IRiscaTableSMHActions,
  RiscaSortConfig,
  RiscaSortTypes,
  RiscaTableBodyTabMethodData,
  RiscaTableBodyTabMethods,
  RiscaTableCss,
  RiscaTableInput,
  RiscaTablePagination,
  RiscaTableSMHActions,
  RiscaTableSortStatus,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
} from '../../../utilities';
import { RiscaTableDataConfig } from './utilities/risca-table.classes';
import { RiscaTableTargetConfig } from './utilities/risca-table.enums';
import {
  IRiscaTableACEvent,
  IRiscaTableAzioneCustom,
  IRiscaTableCheboxesChange,
  IRiscaTableSubRowData,
} from './utilities/risca-table.interfaces';

/**
 * Componente adibito alla gestione delle tabelle dell'app Risca.
 */
@Component({
  selector: 'risca-table',
  templateUrl: './risca-table.component.html',
  styleUrls: ['./risca-table.component.scss'],
})
export class RiscaTableComponent<T> implements OnInit, OnChanges {
  /** Oggetto di costanti contenente le informazioni comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto di costanti RiscaTableConsts per il componente. */
  RT_C = RiscaTableConsts;
  /** RiscaTableTargetConfig con l'enumeratore per le configurazioni della tabella. */
  riscaTableTargetConfig = RiscaTableTargetConfig;

  /** Input string che definisce l'id d'associare alla tabella e ai suoi componenti. */
  @Input('tableId') private _tableId?: string;
  /** Input che definisce le configurazioni per gli stili della input. */
  @Input() cssConfig: RiscaTableCss;
  /** Input che definisce le configurazioni dati per la tabella. */
  @Input() dataConfig: RiscaTableInput;
  /** Input che definisce il source dati per la tabella. */
  @Input('source') inputSource: RiscaTableDataConfig<T>[];
  /** Input che definisce se la tabella è di sola gestione FE. Se false, la tabella è gestita con paginazione e ordinamento tramite dati da BE. */
  @Input() isFETable: boolean = true;
  /** Input che definisce la logica di disabilitazione delle checkbox delle righe, se viene attivata la checkbox di testata. */
  @Input('allSelectedDisableRowsCheck') allSeldDisableRC: boolean = false;
  /** Input che definisce la funzione che permette di comparare due righe della tabella. */
  @Input() comparatorRows?: (
    a: RiscaTableDataConfig<T>,
    b: RiscaTableDataConfig<T>
  ) => boolean;
  /** Input che definisce la funzione che permette di comparare due oggetti originali dentro due righe della tabella. */
  @Input() comparatorOriginals?: (a: any, b: any) => boolean;
  /** Input RiscaTableDataConfig<T>[] contenente gli oggetti che risultano selezionati. */
  @Input() sourcesSelected: RiscaTableDataConfig<T>[] = [];
  /** Boolean che tiene traccia del flag per "tutte le righe selezionate". */
  @Input('tutteLeRigheSelezionate') allRowsSelectedFlag: boolean;
  /** string che definisce una descrizione per la tabella. */
  @Input() tableDescription: string = '';

  /** Output per il click della riga. */
  @Output() onRowClick = new EventEmitter<RiscaTableDataConfig<T>>();
  /** Output per il click della riga. */
  @Output() onSubRowClick = new EventEmitter<IRiscaTableSubRowData>();

  /** Output per il click sul radio button della riga. */
  @Output() onRadioClick = new EventEmitter<RiscaTableDataConfig<T>>();

  /** Output per il change delle checkbox della tabella pilotato dalla checkbox di testata. Questo evento ritorna: tutte le righe attualmente selezionate. */
  @Output() onAllCheckboxesChange = new EventEmitter<
    RiscaTableDataConfig<any>[]
  >();
  /** Output per il change delle checkbox della tabella. Questo evento ritorna: l'ultima riga modificata; tutte le righe attualmente selezionate. */
  @Output() onCheckboxesChange = new EventEmitter<
    IRiscaTableCheboxesChange<T>
  >();
  /** Output per il change delle checkbox della tabella. Questo evento ritorna: l'ultima riga modificata.  */
  @Output() onCheckboxChanged = new EventEmitter<RiscaTableDataConfig<any>>();
  /** Output per il change delle checkbox della tabella. Questo evento ritorna: tutte le righe attualmente selezionate. */
  @Output() onCheckboxesChecked = new EventEmitter<
    RiscaTableDataConfig<any>[]
  >();

  /** Output per la cancellazione della riga. */
  @Output() onRowDelete = new EventEmitter<RiscaTableDataConfig<any>>();
  /** Output per il click della riga. */
  @Output() onSubRowDelete = new EventEmitter<IRiscaTableSubRowData>();
  /** Output per il dettaglio della riga. */
  @Output() onRowDetail = new EventEmitter<RiscaTableDataConfig<any>>();
  /** Output per il click della riga. */
  @Output() onSubRowDetail = new EventEmitter<IRiscaTableSubRowData>();
  /** Output per la modifica della riga. */
  @Output() onRowModify = new EventEmitter<RiscaTableDataConfig<any>>();
  /** Output per il click della riga. */
  @Output() onSubRowModify = new EventEmitter<IRiscaTableSubRowData>();

  /** Output per un'azione custom sulla riga. */
  @Output() onRowCustomAction = new EventEmitter<IRiscaTableACEvent<T>>();
  /** Output per un'azione custom sulla riga. */
  @Output() onSubRowCustomAction = new EventEmitter<IRiscaTableACEvent<T>>();

  /** Output per il cambio pagina. */
  @Output() onCambioPagina = new EventEmitter<RiscaTablePagination>();

  /** Array di RiscaTableDataConfig<T> che definisce il source della tabella. */
  tableSource: RiscaTableDataConfig<T>[];
  /** Oggetto chiave valore, contenente come chiave il nome della proprietà definita nelle configurazioni, e come value il tipo di sort. */
  currentSortStatus: RiscaTableSortStatus;
  /** Oggetto chiave valore, contenente come chiave il nome della proprietà definita nelle configurazioni, e come value il tipo di sort. Questo oggetto viene valorizzato all'init del componente e mantiene lo stato iniziale del sort status. */
  initialSortStatus: RiscaTableSortStatus;
  /** Boolean che definisce se la tabella gestirà dei radio buttons. */
  hasTableRadios: boolean;
  /** RiscaTableDataConfig<T> contenente il valore del radio selezionato. */
  radioValue: RiscaTableDataConfig<T>;
  /** string che definisce il nome per connettere tutti i radio buttons. */
  radioGroupName: string;

  /** string che definisce l'id del th per le multi check. => FORZATO DA SONARQUBE. */
  multicheckId: string;
  /** string che definisce l'id base del th la parte di ordinamento delle colonne. => FORZATO DA SONARQUBE. */
  orderColumnId: string;

  /** RiscaTablePagination con la configurazione della paginazione della tabella. */
  private _paginazioneTabella: RiscaTablePagination;

  /**
   * ################
   * SETUP COMPONENTE
   * ################
   */

  /**
   * Costruttore.
   */
  constructor(
    private _logger: LoggerService,
    private _riscaTable: RiscaTableService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Lancio il setup della tabella
    this.setupTabella();
  }

  ngOnInit() {
    // Lancio il setup della tabella
    this.initTabella();
  }

  ngOnChanges(changes: SimpleChanges): void {
    // Verifico se è stato modificato il source
    if (changes.inputSource && !changes.inputSource.firstChange) {
      // Assegno il nuovo valore locale
      this.initSource();

      // Verifico e gestisco l'ordinamento solo per i dati gestiti da FE
      if (this.isFETable) {
        // Recupero lo stato precedente e attuale
        const prevSource = changes.inputSource.previousValue;
        const nowSource = changes.inputSource.currentValue;
        // Lancio la funzione per la gestione dell'ordinamento
        this.changeGestisciOrdinamento(prevSource, nowSource);
      }
    }

    // Verifico se è stato modificato l'oggetto di configurazione dati
    if (changes.dataConfig && !changes.dataConfig.firstChange) {
      // Verifico se la paginazione è cambiata
      this.handlePaginationChange(this.dataConfig.pagination);
    }
  }

  /**
   * ############################
   * FUNZIONI DI SETUP COMPONENTE
   * ############################
   */

  /**
   * Funzione che lancia i setup del componete.
   */
  private setupTabella() {
    // Lancio il setup per gli id delle th della tabella
    this.setupIdTh();
  }

  /**
   * Funzione che effettua il setup per gli id dei th per la tabella.
   */
  private setupIdTh() {
    // Genero l'id per il th del multicheck
    this.multicheckId = this._riscaUtilities.generateRandomId();
    this.orderColumnId = `sort_col_${this._riscaUtilities.generateRandomId()}`;
  }

  /**
   * ###########################
   * FUNZIONI DI INIT COMPONENTE
   * ###########################
   */

  /**
   * Funzione di setup per la tabella.
   */
  private initTabella() {
    // Lancio la funzione di init della tabella
    this.initTableId();
    // Lancio la funzione di setup delle funzioni della tabella
    this.initMethods();
    // Lancio la funzione di setup del source
    this.initSource();
    // Lancio la funzione di setup della configurazione di sort della tabella
    this.initSortStatus();
    // Lancio la funzione di setup dei sort dati della tabella
    this.initSortData();
    // Lancio la funzione di init che gestisce le configurazioni della tabella
    this.initDataConfigs();
  }

  /**
   * Funzione che verifica l'esistenza di un id per la tabella.
   * Se non esiste, viene generato un id  di default.
   */
  private initTableId() {
    // Verifico se è stato definito un id
    if (!this.tableId) {
      // Ne genero uno e lo assegno
      this._tableId = this._riscaUtilities.generateRandomId();
    }
  }

  /**
   * Funzione che gestice le funzioni della tabella se non configurate appositamente.
   */
  private initMethods() {
    // Verifico se esiste una funzione di comparazione
    if (!this.comparatorRows) {
      // Imposto una funzione di default
      this.comparatorRows = (
        a: RiscaTableDataConfig<T>,
        b: RiscaTableDataConfig<T>
      ): boolean => {
        // Faccio la compare tra gli id degli elementi
        return a.id === b.id;
      };
    }

    // Verifico se esiste una funzione di comparazione per oggetti originals
    if (!this.comparatorOriginals) {
      // Imposto una funzione di default
      this.comparatorOriginals = (a: any, b: any): boolean => {
        // Variabili di comodo
        const title = `risca-table.component.ts`;
        const body = `No comparatorOriginals function defined. Using the default logic.`;
        // Scrivo un log per definire che non è stata definita una funzione specifica
        this._logger.warning(title, body);
        // Faccio la compare tra gli id degli elementi
        return a.id === b.id;
      };
    }
  }

  /**
   * Funzione di init del source. Verrà generata una copia della tabella per la gestione del componente.
   */
  private initSource() {
    // Resetto la tabella
    this.tableSource = [];

    // Verifico se l'inputSource è definito
    const inputSource = this.inputSource ? this.inputSource : [];
    // Effettuo una copia creando una nuova referenza dell'array
    this.tableSource = clone(inputSource);

    // Gestusci gli elementi selezionati
    this.initSourcesSelected(this.tableSource);
    // Lancio la funzione di setup per la gestione dei radio button
    this.initRadioButtons();
  }

  /**
   * Funzione d'inizializzazione che gestisce la metodologia di pre-selezione degli elementi.
   * In base alla configurazione del componente, verrà gestita la pre-selezione degli elementi.
   */
  private initSourcesSelected(tableSource: RiscaTableDataConfig<T>[]) {
    // Recupero della configurazione esiste la funzione di auto selezione
    const comparatorRows = this.comparatorRows;
    // Recupero la lista di elementi selezionati
    const sourcesSelected = this.sourcesSelected;

    // Verifico se la gestione di sola FE
    if (this.isFETable) {
      // Variabile di comodo
      const t = tableSource;
      // Recupero gli oggetti selected
      this.sourcesSelected = this._riscaTable.getSourcesSelected(t);
      // #
    } else if (Boolean(this.allRowsSelectedFlag)) {
      // Verifico ed eventualmente lancio l'aggiornamento per le logiche collegate al "seleziona tutti"
      this.updateAllSelectedData(this.allRowsSelectedFlag);
      // Lancio l'aggiornamento del source
      this.updateAllCheckboxesByFlag(this.allRowsSelectedFlag);
      // #
    } else if (comparatorRows && sourcesSelected?.length > 0) {
      // Lancio la funzione e aggiorno i dati della tabella a seconda della configurazione
      this._riscaTable.setSelectedRows(
        tableSource,
        sourcesSelected,
        comparatorRows
      );
      // #
    }
  }

  /**
   * Funzione che effettua il setup delle configurazioni di sort per i dati della tabella.
   */
  private initSortStatus() {
    // Richiamo il servizio per il setup dello stato dei sort
    this.currentSortStatus = this._riscaTable.generaSortStatusTable(
      this.dataConfig.sourceMap
    );

    // Creo una copia del sort per il valore iniziale
    this.initialSortStatus = clone(this.currentSortStatus);
  }

  /**
   * Funzione che effettua il sort dei dati della tabella.
   */
  private initSortData() {
    // Definisco l'oggetto per l'ordinamento
    let sortStatus: RiscaTableSortStatus;
    // Variabili di comodo
    const existCSS = this.currentSortStatus != null;
    const existSB = this.dataConfig?.pagination?.sortBy != null;
    const existSD = this.dataConfig?.pagination?.sortDirection != null;

    // Verifico le condizioni per il sort e la paginazione
    if (existCSS && existSB && existSD) {
      // Reset di ogni chiave di ordinamento
      Object.keys(this.currentSortStatus).forEach((item) => {
        this.currentSortStatus[item] = RiscaSortTypes.nessuno;
      });
      // Definisco la chiave per impostare l'ordinamento mediante paginazione
      const sortKey = this.dataConfig.pagination.sortBy;
      const sortDir = this.dataConfig.pagination.sortDirection;
      // Aggiorno la chiave di ordinamento specifica
      this.currentSortStatus[sortKey] = sortDir;
      // Cerco di ordinare la tabella mediante configurazione corrente
      sortStatus = this.currentSortStatus;
      // #
    } else {
      // Cerco di ordinare la tabella mediante configurazione iniziale
      sortStatus = this.initialSortStatus;
    }

    // Genero l'oggetto per il sort
    const sortConfig = this._riscaTable.getOneSortTable(sortStatus);
    // Se esiste una configurazione ordino i dati
    if (sortConfig) {
      // Lancio l'ordinamento dei dati
      this.sortTableRowsByRiscaSortConfig(sortConfig, true);
    }

    // Imposto le informazioni per la paginazione
    this.paginazioneTabella = this.dataConfig?.pagination;
  }

  /**
   * Funzione di init per la gestione dei radio buttons.
   */
  private initRadioButtons() {
    // Controllo se la tabella ha da gestire radio button
    this.hasTableRadios = this.tabellaConRadio;
    // Verifico se la tabella gestisce radio buttons
    if (!this.hasTableRadios) {
      return;
    }

    // La tabella gestisce i radio buttons, recupero un potenziale default
    this.radioValue = this._riscaTable.getSourceSelected(this.tableSource);

    // Definisco delle variabili per comporre un name per il radio gruop univoco
    const prefix = this.RT_C.RADIO_GN_PREFIX;
    const id = this._riscaUtilities.generateRandomId();
    // Unisco le variabili per generare un radio gruop name
    this.radioGroupName = `${prefix}_${id}`;
  }

  /**
   * Funzione di init che recupera le informazioni di configurazione e imposta delle informazioni localmente.
   */
  private initDataConfigs() {
    // Recupero dalla configurazione input le informazioni di data config
    const dc = this.dataConfig;
    // Lanio le funzioni di init delle informazioni
    this.initAllRowsSelectedFlag(dc);
  }

  /**
   * Funzione che gestisce l'inizializzazione del flag di "tutte le righe selezionate".
   * In fase d'inizializzazione il valore di riferimento del check è quello presente nella configurazione.
   * @param dataConfig RiscaTableInput con le informazioni di configurazione della tabella.
   */
  private initAllRowsSelectedFlag(dataConfig: RiscaTableInput) {
    // Verifico se il flag è stato settato tramite input
    if (this.allRowsSelectedFlag != undefined) {
      // Blocco l'inizializzazione del flag
      return;
    }
    // Verifico l'input
    if (!dataConfig) {
      // Nessuna configurazione
      this.allRowsSelectedFlag = false;
      // Blocco le logiche
      return;
    }

    // Estraggo le informazioni da data config
    const sourceMap: RiscaTableSourceMap[] = dataConfig?.sourceMap ?? [];
    // Recupero le configurazioni dagli header per le actions
    const actions: IRiscaTableSMHActions[] = sourceMap?.map(
      (sm: RiscaTableSourceMap) => {
        // Ritorno solo gli oggetti per gli headers
        return sm?.header?.action;
      }
    );
    // Rimuovo tutte le configurazioni undefined
    const actionsCompact = compact(actions);

    // Recupero il nome della proprietà dell'oggetto che gestisce le checkbox di testata
    const CHECK_KEY = this.headerActionCheckbox;
    // Cerco all'interno delle action se esiste la gestione di tutte le checkbox
    const allChecksConfig = actionsCompact?.find((a: IRiscaTableSMHActions) => {
      // Verifico se l'oggetto contiene la proprietà per la gestione checkbox
      return a[CHECK_KEY] != undefined;
    });

    // Verifico se è stato trovato l'oggetto
    if (allChecksConfig) {
      // Esiste, recupero i dettagli delle configurazioni
      const allChecksData: IRiscaTableSMHACheckbox = allChecksConfig[CHECK_KEY];
      // Assegno localmente il valore per la selezione
      this.allRowsSelectedFlag = allChecksData?.selected ?? false;
    }
  }

  /**
   * ##############################
   * FUNZIONI DI CHANGES COMPONENTE
   * ##############################
   */

  /**
   * Funzione che gestisce l'ordinamento quando il source viene modificato.
   * @param prev Array di RiscaTableDataConfig<any> che rappresenta il vecchio valore di source.
   * @param now Array di RiscaTableDataConfig<any> che rappresenta il nuovo valore di source.
   */
  private changeGestisciOrdinamento(
    prev: RiscaTableDataConfig<any>[],
    now: RiscaTableDataConfig<any>[]
  ) {
    // Definisco le condizioni di reset del sort
    const resetOnChange = this.dataConfig.sorting?.resetOnChange;
    const resetOnAdd =
      this.dataConfig.sorting?.resetSortOnAdd && prev.length < now.length;
    const resetOnRemove =
      this.dataConfig.sorting?.resetSortOnRemove && prev.length > now.length;

    // Verifico le configurazioni di reset
    if (resetOnChange || resetOnAdd || resetOnRemove) {
      // Lancio il reset
      this.initSortData();
      // #
    } else {
      // Riordino con l'attuale ordinamento attivo
      this.currentSortData();
    }
  }

  /**
   * Funzione di gestione del cambio paginazione, che comparara la possibile nuova paginazione con la paginazione attualmente definita a livello applicativo.
   * A seconda che la paginazione risulti differente o la stessa, si attiverà un flusso specifico di gestione.
   * @param pagination RiscaTablePagination con la configurazione della nuova paginazione.
   */
  private handlePaginationChange(pagination: RiscaTablePagination) {
    // Definisco le informazioni per la compare tra paginazioni
    const newPag = pagination;
    const oldPag = this.paginazioneTabella;

    // Verifico se le paginazioni sono diverse
    if (!this.samePaginazioni(newPag, oldPag)) {
      // Aggiorno l'oggetto della paginazione del componente
      this.paginazioneTabella = pagination;
      // Rilancio la gestione per il check delle righe
    }
  }

  /**
   * Funzione che compara i dati di due paginazioni diverse.
   * A seconda che siano uguali o differenti nei dati, ritorna un valore di check.
   * @param newPag RiscaTablePagination con la nuova paginazione.
   * @param oldPag RiscaTablePagination con la vecchia paginazione.
   * @returns true se la paginazione è cambiata
   */
  private samePaginazioni(
    newPag: RiscaTablePagination,
    oldPag: RiscaTablePagination
  ): boolean {
    // Richiamo la funzione di utility
    return this._riscaUtilities.samePaginazioni(newPag, oldPag);
  }

  /**
   * ################################
   * FUNZIONALITA' SORT DELLA TABELLA
   * ################################
   */

  /**
   * Funzione che esegue il sort dei dati della tabella per una colonna, data la configurazione.
   * @param property string che definisce per quale property deve essere ordinata la tabella.
   */
  onClickSortTable(property: string) {
    // Variabile di comodo
    const check = this._riscaTable.isPropertySortable(
      this.currentSortStatus,
      property
    );
    // Verifico che esista l'input e che almeno un campo sia ordinabile
    if (!check) {
      return;
    }

    // Richiamo la funzione per sapere qual è il nuovo stato del sort
    const sortToApply: RiscaSortConfig = this._riscaTable.defineSortTable(
      this.currentSortStatus,
      property
    );

    // Richiamo la funzione di sort della tabella per la colonna
    this.sortTableRowsByRiscaSortConfig(sortToApply);
  }

  /**
   * Funzione di supporto il set di currentSortStatus al suo stato iniziale.
   */
  initialSortConfigs() {
    // Variabile di comodo
    const iss = this.initialSortStatus;
    // Genero l'oggetto per il sort
    const sortConfig: RiscaSortConfig = this._riscaTable.getOneSortTable(iss);
    // Richiamo l'aggiornamento del sort config, passando l'oggetto init
    this.updateSortConfigs(sortConfig);
  }

  /**
   * Funzione di supporto per l'aggiornamento del currentSortStatus.
   * @param config RiscaSortConfig che definisce l'ordinamento da aggiornare.
   */
  updateSortConfigs(config: RiscaSortConfig) {
    // Verifico che esista l'input
    if (!config) {
      return;
    }
    // Aggiorno l'oggetto di sort
    this.currentSortStatus = this._riscaTable.setOneSortTable(
      this.currentSortStatus,
      config.field,
      config.type
    );
  }

  /**
   * Funzione che ordina le righe della tabella data la configurazione per una colonna.
   * @param config RiscaSortConfig con la configurazione d'applicare al sort della tabella.
   * @param isInit boolean che definisce se il chiamante fa parte del processo di init del componente. Se true, vengono disattivate delle specifiche logiche basate sulla paginazione (che durante l'init non necessitano di essere eseguite). Per default è: false.
   */
  private sortTableRowsByRiscaSortConfig(
    config: RiscaSortConfig,
    isInit = false
  ) {
    // Verifico il tipo di ordinamento
    if (this.isSortFE) {
      // richiamo il sort di FE
      this.sortTableRowsByRiscaSortConfigFE(config);
    } else {
      // richiamo il sort di BE
      this.sortTableRowsByRiscaSortConfigBE(config, isInit);
    }
  }

  /**
   * Funzione che ordina le righe della tabella data la configurazione per una colonna.
   * Questa funzione è specifica per l'ordinamento di FE.
   * @param config RiscaSortConfig con la configurazione d'applicare al sort della tabella.
   */
  private sortTableRowsByRiscaSortConfigFE(config: RiscaSortConfig) {
    // Verifico che esista l'input
    if (!config) {
      return;
    }

    // Aggiorno l'oggetto di sort
    this.updateSortConfigs(config);

    // Se non ho una paginazione, devo fare il sorting sul FE. Aggiorno la lista source:
    this.tableSource = this._riscaTable.sortOneTable(
      this.tableSource,
      this.dataConfig.sourceMap,
      this.currentSortStatus
    );
  }

  /**
   * Funzione che ordina le righe della tabella data la configurazione per una colonna.
   * Questa funzione è specifica per l'ordinamento di BE.
   * @param config RiscaSortConfig con la configurazione d'applicare al sort della tabella.
   * @param isInit boolean che definisce se il chiamante fa parte del processo di init del componente. Se true, vengono disattivate delle specifiche logiche basate sulla paginazione (che durante l'init non necessitano di essere eseguite). Per default è: false.
   */
  private sortTableRowsByRiscaSortConfigBE(
    config: RiscaSortConfig,
    isInit = false
  ) {
    // Verifico che esista l'input
    if (!config) {
      return;
    }

    // Verifico se gli ordinamenti sono stati modificati
    const sameField = config.field === this.dataConfig?.pagination.sortBy;
    const sameType = config.type === this.dataConfig?.pagination?.sortDirection;
    // Verifico se vi è stato un cambiamento effettivo
    if (sameField && sameType) {
      // Non è cambiato niente nel sort, blocco le logiche
      return;
    }

    // Aggiorno l'oggetto di sort
    this.updateSortConfigs(config);

    // Se ho una paginazione BE, devo fare il suo sorting, ma se non sono nel processo di init
    if (!isInit) {
      // Ottengo l'ordinamento corrente
      const ordinamento = this._riscaTable.getFieldAndType(
        this.currentSortStatus
      );

      // Aggiorno l'oggetto di paginazione per emetterlo
      const paginazione: RiscaTablePagination = this.dataConfig.pagination;

      // Se non devo aggiornare nulla non devo emettere eventi
      if (
        paginazione.sortBy == ordinamento.field &&
        paginazione.sortDirection == ordinamento.type
      ) {
        return;
      }

      // Assegno il campo su cui fare l'ordinamento
      paginazione.sortBy = ordinamento.field;
      // // Assegno la direzione dell'ordinamento
      paginazione.sortDirection = ordinamento.type;

      // Emetto l'evento di cambio della paginazione
      this.cambioPagina(paginazione);
    }
  }

  /**
   * ####################################
   * FUNZIONE DI CLICK DIRETTO DELLA RIGA
   * ####################################
   */

  /**
   * Funzione richiamata dal template quando viene premuta una riga.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga principale che è stata premuta.
   */
  clickRiga(row: RiscaTableDataConfig<any>) {
    // Emetto l'evento di click riga
    this.onRowClick.emit(row);
  }

  /**
   * Funzione richiamata dal template quando viene premuta una sotto riga.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga principale.
   * @param subRow RiscaTableDataConfig<any> che definisce l'oggetto della sotto riga premuta.
   */
  clickSottoRiga(
    row: RiscaTableDataConfig<any>,
    subRow: RiscaTableDataConfig<any>
  ) {
    // Emetto l'evento di click riga
    this.onSubRowClick.emit({ row, subRow });
  }

  /**
   * #####################################
   * FUNZIONE DI CLICK PER: DETTAGLIO RIGA
   * #####################################
   */

  /**
   * Funzione richiamata dal template quando viene premuto il pulsante di dettaglio.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga principale per la quale si vuole vedere il dettaglio.
   */
  dettaglioRiga(row: RiscaTableDataConfig<any>) {
    // Emetto l'evento di dettaglio riga
    this.onRowDetail.emit(row);
  }

  /**
   * Funzione richiamata dal template quando viene premuto il pulsante di dettaglio.
   * @param subRow RiscaTableDataConfig<any> che definisce l'oggetto della sotto riga per la quale si vuole vedere il dettaglio.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga principale.
   */
  dettaglioSottoRiga(
    subRow: RiscaTableDataConfig<any>,
    row: RiscaTableDataConfig<any>
  ) {
    // Emetto l'evento di dettaglio riga
    this.onSubRowDetail.emit({ row, subRow });
  }

  /**
   * ####################################
   * FUNZIONE DI CLICK PER: MODIFICA RIGA
   * ####################################
   */

  /**
   * Funzione richiamata dal template quando viene premuto il pulsante di modifica.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga principale che si vuole modificare.
   */
  modificaRiga(row: RiscaTableDataConfig<any>) {
    // Emetto l'evento di modifica riga
    this.onRowModify.emit(row);
  }

  /**
   * Funzione richiamata dal template quando viene premuto il pulsante di modifica.
   * @param subRow RiscaTableDataConfig<any> che definisce l'oggetto della sotto riga che si vuole modificare.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga principale.
   */
  modificaSottoRiga(
    subRow: RiscaTableDataConfig<any>,
    row: RiscaTableDataConfig<any>
  ) {
    // Emetto l'evento di modifica riga
    this.onSubRowModify.emit({ row, subRow });
  }

  /**
   * ###################################
   * FUNZIONE DI CLICK PER: ELIMINA RIGA
   * ###################################
   */

  /**
   * Funzione richiamata dal template quando viene premuto il pulsante di cancellazione.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga principale che si vuole eliminare.
   */
  eliminaRiga(row: RiscaTableDataConfig<any>) {
    // Emetto l'evento di cancellazione riga
    this.onRowDelete.emit(row);
  }

  /**
   * Funzione richiamata dal template quando viene premuto il pulsante di cancellazione.
   * @param subRow RiscaTableDataConfig<any> che definisce l'oggetto della sotto riga che si vuole eliminare.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga principale.
   */
  eliminaSottoRiga(
    subRow: RiscaTableDataConfig<any>,
    row: RiscaTableDataConfig<any>
  ) {
    // Emetto l'evento di cancellazione riga
    this.onSubRowDelete.emit({ row, subRow });
  }

  /**
   * ###################################
   * FUNZIONE DI CLICK CUSTOM DELLA RIGA
   * ###################################
   */

  /**
   * Funzione richiamata dal template quando viene premuto sul custom DOM che gestisce un'azione custom.
   * @param customEvent IRiscaTableAzioneCustom con le informazioni dell'azione custom, contenente i dati della riga principale.
   */
  azioneCustom(customEvent: IRiscaTableAzioneCustom<T>) {
    // Converto l'oggetto per l'emissione
    const event: IRiscaTableACEvent<any> = {
      action: customEvent.action,
      row: customEvent.data,
    };
    // Emetto l'evento di azione custom
    this.onRowCustomAction.emit(event);
  }

  /**
   * Funzione richiamata dal template quando viene premuto sul custom DOM che gestisce un'azione custom.
   * @param customEvent IRiscaTableAzioneCustom con le informazioni dell'azione custom, contenente i dati della sotto riga.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga principale.
   */
  azioneCustomSottoRiga(
    customEvent: IRiscaTableAzioneCustom<T>,
    row: RiscaTableDataConfig<any>
  ) {
    // Converto l'oggetto per l'emissione
    const event: IRiscaTableACEvent<any> = {
      action: customEvent.action,
      row,
      subRow: customEvent.data,
    };
    // Emetto l'evento di azione custom
    this.onSubRowCustomAction.emit(event);
  }

  /**
   * #################################
   * FUNZIONI CHECKBOX E RADION BUTTON
   * #################################
   */

  /**
   * Funzione richiamata dal template quando viene premuto un radio button.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga per la quale è cambiato un checkbox.
   */
  changedCheckbox(row: RiscaTableDataConfig<any>) {
    // Verifico se esiste la lista di elementi selezionati
    if (!this.sourcesSelected) {
      // Inizializzo l'array
      this.sourcesSelected = [];
    }

    // Variabili di comodo
    let allSel: RiscaTableDataConfig<T>[] = this.sourcesSelected;
    const c = this.comparatorRows;
    const ts: RiscaTableDataConfig<T>[] = this.tableSource;
    const currentSel = this._riscaTable.getSourcesSelected(ts);

    // Aggiorno la lista di elementi selezionati in base alla singola operazione sulla riga (check/uncheck della riga)
    this._riscaTable.updateSourcesSelected(allSel, row, c);
    // Vado a mergiare le informazioni tra l'array di elementi già selezionati e quelli selezionati per questa composizione del source
    allSel = this._riscaTable.mergeSourcesSelected(allSel, currentSel, c);

    // Assegno gli oggetti per poter fare l'emissione dati
    const rowChanged = row;
    const rowsChecked = this.sourcesSelected;
    // Emetto gli eventi per cominicare al componente padre l'avvenuto cambio dati
    this.onCheckboxesChange.emit({ rowChanged, rowsChecked });
    this.onCheckboxChanged.emit(rowChanged);
    this.onCheckboxesChecked.emit(rowsChecked);
  }

  /**
   * Funzione che permette di aggiornare le checkboxes della tabella basandosi su una lista di dati originali.
   * @param dataOrigins any[] con le informazioni delle righe originali come "selezionati". Se viene passato undefined, non viene eseguita nessuna operazione. Se viene passato un array vuoto, le righe selezionate vengono resettate.
   * @param updatePagination boolean che permette di aggiornare la paginazione al cambio delle righe selezionate. Per default è: false.
   */
  updateSelectedRowsByOrigin(dataOrigins: any[], updatePagination = false) {
    // Verifico l'input
    if (!dataOrigins) {
      // Niente da gestire
      return;
    }
    // Verifico se l'array è svuotato
    if (dataOrigins.length === 0) {
      // Resetto la lista di elementi selezionati
      this.resetSelectedRows();
    }

    // Verifico se devo ricaricare la paginazione attuale
    if (updatePagination) {
      // Recupero la paginazione attuale
      const paginazione: RiscaTablePagination = this._paginazioneTabella;
      // Rilancio la paginazione
      this.cambioPagina(paginazione);
      // #
    } else {
      // Recupero i dati della tabella
      const ts = this.tableSource;
      // Recupero delle configurazioni per i comparatori
      const cRows = this.comparatorRows;
      const cOriginals = this.comparatorOriginals;

      // Recupero la lista di elementi selezionati
      this.sourcesSelected = intersectionWith(
        this.sourcesSelected,
        dataOrigins,
        (a: RiscaTableDataConfig<T>, b: any) => {
          // Recupero l'oggetto originale della riga A
          const originalA = a?.original;
          // Richiamo e ritorno il risultato del comparatore tra oggetti originali
          return cOriginals(originalA, b);
        }
      );

      // Lancio la funzione e aggiorno i dati della tabella
      this._riscaTable.setSelectedRows(ts, this.sourcesSelected, cRows);
    }
  }

  /**
   * Funzione richiamata dal template quando viene premuto un radio button.
   * @param data RiscaTableDataConfig<any> che definisce l'oggetto della riga per la quale è stato selezionato un radio.
   */
  clickRadio(data: RiscaTableDataConfig<any>) {
    // Definisco il radio selezionato
    data.selected = true;
    // Assegno localmente il selected
    this.sourcesSelected = [data];
    // Resetto tutti gli altri radio
    this._riscaTable.updateRadio(this.tableSource, data.id);

    // Emetto l'evento di click del radio
    this.onRadioClick.emit(data);
  }

  /**
   * #############################################
   * FUNZIONALITA' SPECIFICHE DI GESTIONE CHECKBOX
   * #############################################
   */

  /**
   * Funzione di comodo che setta la proprietà selected di tutti gli oggetti di source.
   */
  setCheckboxes() {
    // Richiamo la funzione di set con il flag true
    this.setCheckboxesSelection(true);
  }

  /**
   * Funzione di comodo che resetta la proprietà selected di tutti gli oggetti di source.
   */
  resetCheckboxes() {
    // Richiamo la funzione di set con il flag false
    this.setCheckboxesSelection(false);
  }

  /**
   * Funzione di comodo che resetta i dati relativi alle righe selezionate dall'utente.
   */
  resetSelectedRows() {
    // Restto l'array di elementi selezionati
    this.sourcesSelected = [];
  }

  /**
   * Funzione di comodo che setta la proprietà selected di un oggetto di source.
   * @param id string che definisce l'id dell'oggetto RiscaTableDataConfig<any> d'aggiornare.
   */
  setCheckbox(id: string) {
    // Richiamo la funzione del servizio
    this._riscaTable.updateCheckbox(this.tableSource, id, true);
  }

  /**
   * Funzione di comodo che resetta la proprietà selected di un oggetto di source.
   * @param id string che definisce l'id dell'oggetto RiscaTableDataConfig<any> d'aggiornare.
   */
  resetCheckbox(id: string) {
    // Richiamo la funzione del servizio
    this._riscaTable.updateCheckbox(this.tableSource, id, false);
  }

  /**
   * Funzione agganciata alla checkbox di testata che gestisce tutte le checkbox nelle righe della tabella.
   * @param checkConfigs IRiscaTableSMHACheckbox con la configurazione della checkbox di testata.
   */
  updateAllCheckboxesByConfig(checkConfigs: IRiscaTableSMHACheckbox) {
    // Recupero il valore impostato per la checkbox di testata
    const checkAll = this.allRowsSelectedFlag;

    // Verifico ed eventualmente lancio l'aggiornamento per le logiche collegate al "seleziona tutti"
    this.updateAllSelectedData(checkAll);
    // Lancio l'aggiornamento del source
    this.updateAllCheckboxesByFlag(checkAll);

    // Emetto l'evento di aggiornamento di tutte le righe
    this.onAllCheckboxesChange.emit(checkAll ? this.tableSource : []);
  }

  /**
   * Funzione che permette di aggiornare tutte le checkbox della tabella, passando in input un flag.
   * @param flag boolean con il valore da defire per tutte le checkbox.
   */
  updateAllCheckboxesByFlag(flag: boolean) {
    // Recupero il valore impostato per la checkbox di testata
    const checkAll = flag ?? false;
    // Lancio l'aggiornamento del source
    this.tableSource = this._riscaTable.updateAllCheckboxes(
      this.tableSource,
      checkAll
    );
  }

  /**
   * Funzione di supporto che gestisce le specifiche configurazioni e settaggi dei dati nel caso in cui il flag di "seleziona tutte le righe" venga attivato.
   * @param flag boolean con il valore da defire per tutte le checkbox.
   */
  private updateAllSelectedData(flag: boolean) {
    // Verifico il check sulle checkbox
    if (this.allSeldDisableRC) {
      // Disabilito tutte le righe, modificando la configurazione
      this.disableAllRowsCheck(flag);
    }
  }

  /**
   * Funzione adibita alla modifica delle configurazioen per le righe.
   * @param checkAll boolean con il valore da defire per tutte le checkbox.
   */
  private disableAllRowsCheck(checkAll: boolean) {
    // Recupero le configurazioni dal componente
    const dataConfig = cloneDeep(this.dataConfig);
    // Verifico l'input
    if (!dataConfig) {
      // Nessuna configurazione
      return;
    }

    // Itero le configurazioni per il data config
    dataConfig.sourceMap?.forEach((sm: RiscaTableSourceMap) => {
      // Estraggo la configurazione del body
      const body: RiscaTableSourceMapBody = sm?.body;

      // Verifico se il body che sto iterando è configurato per le actions
      const isTabMeth = body?.useTabMethod;
      if (isTabMeth) {
        // Body che gestisce le actions, le recupero
        const actions = body?.tabMethodData?.actions ?? [];
        // Verifico se all'interno dell'array delle action è presente quello della checkbox
        const actionCheck = actions?.find((a: RiscaTableBodyTabMethodData) => {
          // Verifico se l'azione è que
          return a?.action === RiscaTableBodyTabMethods.check;
        });
        // Verifico se è stato trovato l'oggetto per il check
        if (actionCheck) {
          // Oggetto di configurazione trovato, la funzione ritonerà true
          actionCheck.disableDynamic = (v?: RiscaTableDataConfig<any>) => {
            // Ritorno la condizione dinamica
            return checkAll;
          };
        }
      }
    });

    // Aggiorno la referenza della configurazione, così d'aggiornare tutte le logiche
    this.dataConfig = dataConfig;
  }

  /**
   * Funzione che va ad impostare un valore per tutte le righe della tabella.
   * @param checkAll boolean che definisce se tutte le checkbox devono essere checkate/uncheckate.
   */
  setCheckboxesSelection(checkAll: boolean) {
    // Verifico l'input
    if (checkAll === undefined) {
      checkAll = false;
    }

    // Lancio l'aggiornamento del source
    this.tableSource = this._riscaTable.updateAllCheckboxes(
      this.tableSource,
      checkAll
    );
  }

  /**
   * ##########################################
   * FUNZIONALITA' SPECIFICHE DI GESTIONE RADIO
   * ##########################################
   */

  /**
   * Funzione di comodo che setta come attivo una riga della tabella, "accendendo" il radio button.
   * @param id string che definisce l'id dell'oggetto RiscaTableDataConfig<any> d'aggiornare.
   */
  setRadio(id: string) {
    // Richiamo la funzione del servizio
    this.radioValue = this._riscaTable.getElementSourceById(
      this.tableSource,
      id
    );
  }

  /**
   * Funzione di comodo che resetta la riga attiva per il radio button.
   */
  resetRadio() {
    // Rimuovo il valore all'iterno della variabile di gestione
    this.radioValue = undefined;
  }

  /**
   * Gestione dell'evento di cambio pagina nella paginazione
   * @param paginaCorrente Questa è la pagina selezionata. Parte da 1.
   */
  cambioPagina(paginazione: RiscaTablePagination) {
    // Emetto i dati della paginazione
    this.onCambioPagina.emit(paginazione);
  }

  /**
   * ###################################
   * FUNZIONALITA' DI UTILITY GENERICHE
   * ###################################
   */

  /**
   * Funzione che effettua il sort dei dati della tabella, tramite sort status attuale.
   */
  currentSortData() {
    // Cerco di ordinare la tabella mediante configurazione iniziale
    this.sortData(this.currentSortStatus);
  }

  /**
   * Funzione che effettua il sort dei dati della tabella con l'attuale oggetto di sort attivo.
   * @param sortStatus RiscaTableSortStatus che definisce la mappatura per il sort della tabella.
   */
  private sortData(sortStatus: RiscaTableSortStatus) {
    // Genero l'oggetto per il sort
    const sortConfig: RiscaSortConfig =
      this._riscaTable.getOneSortTable(sortStatus);

    // Se esiste una configurazione ordino i dati
    if (sortConfig) {
      // Lancio l'ordinamento dei dati
      this.sortTableRowsByRiscaSortConfig(sortConfig);
    }
  }

  /**
   * #############################
   * GETTER E SETTER COME FUNZIONI
   * #############################
   */

  /**
   * Funzione di reset per _allRowsSelectedFlag.
   */
  resetAllRowsSelectedFlag() {
    // Ritorno il valore del flag
    this.allRowsSelectedFlag = false;
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter che verifica se la configurazione della tabella prevede la gestione di radio button.
   */
  get tabellaConRadio() {
    // Definisco una variabile di comodo
    const actionRadio = RiscaTableBodyTabMethods.radio;
    // Recupero la configurazione del sourceMap per sourceMap
    const sourceMap = this.dataConfig?.sourceMap || [];
    // Verifico se esiste una configurazione per i radio button all'interno della source map
    const hasRadio = sourceMap
      .map((s) => s.body) // Estraggo la proprietà body
      .filter((b) => b !== undefined) // Rimuovo possibili referenze undefined
      .filter((b) => b.useTabMethod && b.tabMethodData?.actions?.length > 0) // Recupero solo i body che sono impostati per usare tabMethods e hanno definito almeno una action
      .map((b) => b.tabMethodData.actions) // Recupero actions per tabMethodData
      .flat() // Riduco la matrice generata in un unico array di actions
      .some((a) => a.action === actionRadio && !a.custom); // Verifico se l'action è radio e non è una custom action

    // Ritorno il controllo
    return hasRadio;
  }

  /**
   * Getter per l'header action della tabella: checkbox.
   */
  get headerActionCheckbox() {
    // Ritorno l'enum che identifica la proprietà checkbox
    return RiscaTableSMHActions.checkbox;
  }

  /**
   * Getter di supporto per sapere la tipologia di sort della tabella.
   */
  get isSortFE() {
    return this.dataConfig.pagination == null;
  }

  /**
   * Setter di comodo che assegna, dai dati di configurazione, l'oggetto di paginazione nel componente.
   * @returns RiscaTablePagination con le informazioni della paginazione.
   */
  set paginazioneTabella(paginazioneTabella: RiscaTablePagination) {
    // Assegno la paginazione
    this._paginazioneTabella = clone(paginazioneTabella);
  }

  /**
   * Getter di comodo che recupera dai dati di configurazione l'oggetto di paginazione.
   * @returns RiscaTablePagination con le informazioni della paginazione.
   */
  get paginazioneTabella(): RiscaTablePagination {
    // Ritorno l'oggetto di paginazione
    return this._paginazioneTabella;
  }

  /**
   * Getter per il tableId.
   * @returns string con il tableId.
   */
  get tableId(): string {
    // Ritorno il table id
    return this._tableId;
  }
}
