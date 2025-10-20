import { Pipe, PipeTransform } from '@angular/core';
import { RiscaTableDataConfig } from '../../components/risca/risca-table/utilities/risca-table.classes';
import { IRiscaTableAzioneCustom } from '../../components/risca/risca-table/utilities/risca-table.interfaces';
import {
  riscaCssHandler,
  riscaExecute,
} from '../../services/risca/risca-utilities/risca-utilities.functions';
import {
  IRiscaTableSMHActions,
  RiscaCssHandlerTypes,
  RiscaSortTypes,
  RiscaTableBodyTabMethodData,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../utilities';
import { RiscaCss } from './../../utilities/types/utilities.types';
import { RiscaUtilitiesService } from '../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaTableTargetConfig } from '../../components/risca/risca-table/utilities/risca-table.enums';

/**
 * Pipe che recupera una determinata action per l'header.
 * Se non viene specificato nessuna action da recuperare, la Pipe ritornerà true, se esiste una qualsiasi action, o false se non esistono.
 */
@Pipe({ name: 'riscaTableHActions' })
export class RiscaTableHeaderActionsPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che estrae i dati per una determinata action.
   * Se non è definita nessuna action da ritornare, verrà ritornato true se trova un'azione qualunque, o false se non trova nessuna azione.
   * @param actions IRiscaTableSMHActions contenente le informazioni delle azioni dell'header.
   * @param action string contenente il nome dell'azione da recuperare. Nullable.
   * @returns any | boolean recuperando l'oggetto dell'azione, o se ha trovato azioni [true] o no [false].
   */
  transform(actions: IRiscaTableSMHActions, action?: string): any | boolean {
    // Verifico l'input
    if (!actions) {
      return false;
    }

    // Verifico se esiste l'action
    if (action) {
      // Tento di recuperare una specifica action
      const hAction = actions[action];
      // Ritorno l'oggetto se trovato
      return hAction;
      // #
    } else {
      // Verifico se l'oggetto ha almeno un'header action definita
      return Object.keys(actions).length > 0;
    }
  }
}

/**
 * Pipe dedicata alla gestione delle frecce d'ordinamento per la tabella.
 */
@Pipe({ name: 'riscaTableSortIcon' })
export class RiscaTableSortIconPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /** Costante che definisce l'icona di ordinamento disponibile. */
  private ICON_SORT = 'fa-sort';
  /** Costante che definisce l'icona di ordine cresctente. */
  private ICON_ASC = 'fa-sort-up';
  /** Costante che definisce l'icona di ordine decrescente. */
  private ICON_DESC = 'fa-sort-down';

  /**
   * Funzione che gestisce quale icona d'ordinamento deve essere mostrata.
   * @param sortConfig DynamicObjBoolean contenente coppie chiave/valore che identificano il tipo di sort attivo.
   * @param sortField string contenente il nome del campo per il check ordinamento.
   * @returns string come classe che identifica l'icona d'ordinamento.
   */
  transform(
    sortConfig: { [key: string]: RiscaSortTypes },
    sortField: string
  ): string | undefined {
    // Verifico che gli input siano definiti
    if (!sortConfig || !sortField) {
      return;
    }

    // Recupero l'ordinamento
    const sortValue: RiscaSortTypes = sortConfig[sortField];
    // Verifico che esista
    if (!sortValue) {
      return;
    }

    // Verifico che tipo di sort è attivo
    switch (sortValue) {
      case RiscaSortTypes.crescente:
        return this.ICON_ASC;
      case RiscaSortTypes.decrescente:
        return this.ICON_DESC;
      case RiscaSortTypes.nessuno:
        return this.ICON_SORT;
    }
  }
}

/**
 * Pipe dedicata alla gestione dell'output descrittivo source per la tabella.
 */
@Pipe({ name: 'riscaRowTitle' })
export class RiscaTableRowTitlePipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione che gestisce la descrizione da mostrare per la casistica di uso con source.
   * @param row RiscaTableDataConfig<T> che definisce un singolo oggetto di source.
   * @param colConfig RiscaTableSourceMap contenente le configurazioni dati per la colonna della tabella.
   * @returns string come descrizione da visualizzare nella cella della tabella.
   */
  transform(
    row: RiscaTableDataConfig<any>,
    colConfig: RiscaTableSourceMap
  ): string | undefined {
    // Verifico che gli input siano definiti
    if (!row || !row.original || !colConfig) {
      // Non esistono dati, ritorno stringa vuota
      return '';
    }

    // Estraggo l'oggetto originale
    const original = row.original;

    // Recupero e verifico se è stato definito un title per la cella
    let title = colConfig?.body?.title;
    if (title) {
      // Ritorno il title
      return title;
    }

    // Recupero e verifico se è stato definita una funzione per la definizione custom del title
    let titleCustom = colConfig?.body?.titleCustom;
    if (titleCustom) {
      // Richiamo la funzione e ritorno il suo valore
      return titleCustom(original);
    }

    // Non è stata definita una logica, uso quella di fallback
    const riscaSourceOutput = new RiscaTableSourceOutputPipe(
      this._riscaUtilities
    );
    // Invoco la transform della pipe e ritorno il valore
    return riscaSourceOutput.transform(row.dataTable, colConfig, false);
  }
}

/**
 * Pipe dedicata alla gestione dell'output del title per la riga della tabella per la tabella.
 */
@Pipe({ name: 'riscaSourceOutput' })
export class RiscaTableSourceOutputPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione che gestisce la descrizione da mostrare per la casistica di uso con source.
   * @param dataTable any che definisce un singolo oggetto di source.
   * @param colConfig RiscaTableSourceMap contenente le configurazioni dati per la colonna della tabella.
   * @param isCellData boolean che definisce se il dato è quello diretto della cella. Serve per distinguerlo da altri tipi di richieste.
   * @returns string come descrizione da visualizzare nella cella della tabella.
   */
  transform(
    dataTable: any,
    colConfig: RiscaTableSourceMap,
    isCellData: boolean = true,
    useDoubleDash: boolean = false
  ): string | undefined {
    // Verifico che gli input siano definiti
    if (!dataTable || !colConfig) {
      // Non esistono dati, ritorno stringa vuota
      return '';
    }

    // Definisco il contenitore dell'output
    let output = '';

    // Recupero le "componenti" della descrizione
    let prefisso = colConfig?.body?.sourceData?.prefix;
    let descrizione = dataTable[colConfig?.body?.sourceData?.property];
    let suffisso = colConfig?.body?.sourceData?.suffix;

    // Verifico la descrizione
    const descrizioneU = descrizione === undefined;
    const descrizioneN = descrizione === null;
    const descrizioneES = descrizione === '';
    // Se non esiste descrizione, non visualizzo niente
    if (descrizioneU || descrizioneN || descrizioneES) {
      // Non è possibile compilare la descrizione
      return useDoubleDash ? '--' : '';
    }

    // Verifico la composizione delle componenti e manipolo il dato
    prefisso = prefisso === undefined || prefisso === null ? '' : prefisso;
    suffisso = suffisso === undefined || suffisso === null ? '' : suffisso;

    // Compongo la stringa
    output = `${prefisso}${descrizione}${suffisso}`;

    // Recupero la possibile funzione di configurazione del dato
    const { outputFormat, ellipsisAt } = colConfig?.body?.sourceData ?? {};
    // Verifico se esiste una funzione di formattazione dell'output
    if (outputFormat != undefined) {
      // Ritorno il risultato della funzione di formattazione
      output = outputFormat(output);
      // #
    }
    // Verifico se esiste un limite di caratteri per l'ellipsis
    if (isCellData && ellipsisAt != undefined) {
      // Ritorno il risultato dell'ellipsis
      output = this.ellipsisAt(output, ellipsisAt);
    }

    // Niente funzione di formattazione
    return output;
  }

  /**
   * Funzione che effettua un troncamento con ellipsis ad una stringa.
   * @param input string sulla quale effettuare il troncamento.
   * @param truncAt number con la quantità di caratteri alla quale effettuare il troncamento.
   * @returns string con il risultato del troncamento con ellipsis.
   */
  private ellipsisAt(input: string, truncAt: number): string {
    // Lancio a funzione di utility
    return this._riscaUtilities.ellipsisAt(input, truncAt);
  }
}

/**
 * Pipe che verifica se una colonna di header è ordinabile.
 * Se lo è, ritorna una classe di stile, altrimenti stringa vuota.
 * Se è una testata con action, non è ordinabile per default.
 */
@Pipe({ name: 'riscaTableCssHSortable' })
export class RiscaTableCssHeaderSortablePipe implements PipeTransform {
  /** string che definisce la classe di stile per il cursore: puntatore. */
  private POINTER = 'cursor-pointer';

  /**
   * Costruttore del Pipe.
   */
  constructor(private riscaTableHActions: RiscaTableHeaderActionsPipe) {}

  /**
   * Funzione che ritorna la classe di stile per la gestione di una testata sortable.
   * Se è una testata con action, non è ordinabile per default.
   * @param header RiscaTableDataType con il tipo di dato da ritornare.
   * @returns string contenente la classe di stile per la colonna sortable oppure stringa vuota.
   */
  transform(header: RiscaTableSourceMapHeader): string {
    // Verifico l'header
    if (!header) {
      return '';
    }
    // Verifico se l'header possiede una action
    const hasHAction = this.riscaTableHActions.transform(header.action);
    // Verifico se l'header è sortable
    const isSortable = header.sortable;

    // Verifico il risultato
    if (hasHAction || !isSortable) {
      // E' un header action, ritorno stringa vuota
      return '';
      // #
    } else {
      // E' un header con etichetta ed è sortable
      return this.POINTER;
    }
  }
}

/**
 * Pipe dedicato al componente risca-table che definisce l'oggetto di configurazione per il componente risca-table-action.
 */
@Pipe({ name: 'riscaTableAction' })
export class RiscaTableActionPipe<T> implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che genera l'oggetto di configurazione per il componente risca-table-action, per: @Input('actionConfig').
   * @param actionConfig RiscaTableBodyTabMethodData contenente l'oggetto principale di configurazione.
   * @param data RiscaTableDataConfig<any> contenente le informazioni della riga che gestira il componente risca-table-action.
   * @param actionExtra RiscaTableBodyTabMethodData conentente configurazioni extra rispetto alla configurazione principale. Se definito, le sue proprietà sovrascriveranno quelle dell'oggetto principale.
   * @returns IRiscaTableAzioneCustom<T> con la configurazione delle informazioni da passare come parametro al componente risca-table-action.
   */
  transform(
    actionConfig: RiscaTableBodyTabMethodData,
    data: RiscaTableDataConfig<any>,
    actionExtra?: RiscaTableBodyTabMethodData
  ): IRiscaTableAzioneCustom<T> {
    // Creo un oggetto per le configurazioni dell'action
    let action = actionConfig;
    // Verifico se esiste una configurazione extra per l'action, mergio le proprietà
    if (actionExtra) {
      // Definisco l'insieme delle azioni
      action = { ...action, ...actionExtra };
    }

    // Ritorno la composizione dei valori
    return { action, data };
  }
}

/**
 * Pipe dedicato al componente risca-table-action che verifica la funzione di disabilitazione del pulsante e ritorna la classe di stile per gestire l'icona attiva/disattiva.
 */
@Pipe({ name: 'riscaTableCAIcon' })
export class RiscaTableCAIconPipe<T> implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che gestisce l'icona attiva e disattiva.
   * @param v IRiscaTableAzioneCustom<T> con la definizione dei valore del custom action.
   * @param p Array di any che definisce n possibili parametri passabili alla funzione.
   * @returns string con la classe di stile contenente la definizione dell'icona da caricare.
   */
  transform(v: IRiscaTableAzioneCustom<T>, ...p: any[]): string {
    // Verifico l'input
    if (!v) {
      return '';
    }

    // Variabile contenitore per le icone
    let iconaAttiva = '';
    let iconaDisattiva = '';
    // Recupero le informazioni dall'oggetto di configurazione
    let { disable } = v.action || {};

    // Verifico la combinazione di configurazioni per la parte di icona abilitata
    iconaAttiva = this.iconPicker(v, true, p);
    iconaDisattiva = this.iconPicker(v, false, p);

    // Definisco una funzione di fallback per la gestione delle logiche
    const fallback = () => false;
    // Verifico se esiste la funzione di disable, altrimenti imposto un default
    disable = disable ?? fallback;
    // Lancio la funzione e verifico il risulato
    const disabled = disable(v.data, p);

    // Lancio la funzione di disable e verifico il risultato
    if (disabled) {
      // Ritorno l'icona disabilitata
      return iconaDisattiva;
      // #
    } else {
      // Ritorno l'icona abilitata
      return iconaAttiva;
    }
  }

  /**
   * Funzione che definisce le logiche di selezione delle logiche da visualizzare per una custom action della tabella.
   * @param config IRiscaTableAzioneCustom<T> con le configurazioni riguardanti il tipo di action da gestire.
   * @param enableInfo boolean che definisce se le logiche da gestire sono per l'icona abilitata o l'icona disabilitata.
   * @param p Array di any che definisce n possibili parametri passabili alla funzione.
   */
  private iconPicker(
    config: IRiscaTableAzioneCustom<T>,
    enableInfo: boolean,
    ...p: any[]
  ): string {
    // Verifico l'input
    if (!config.action || !config.data) {
      // Ritorno stringa vuota
      return '';
    }
    // Variabile di comodo
    const a = config.action;
    const d = config.data;
    const ei = enableInfo;

    // Definisco le variabili che gestiranno la definizione dell'icona
    let actionIcon: string;
    let actionIconFunc: (v?: RiscaTableDataConfig<any>, ...p: any[]) => string;

    // Verifico quale delle informazioni devo controllare
    actionIcon = ei ? a.iconEnabled : a.iconDisabled;
    actionIconFunc = ei ? a.chooseIconEnabled : a.chooseIconDisabled;

    // Verifico la combinazione di configurazioni per la parte di icona abilitata
    if (actionIconFunc != undefined) {
      // L'icona viene definita dalla funzione custom
      return actionIconFunc(d, p);
      // #
    } else if (actionIcon != undefined) {
      // L'icona viene definita direttamente dalla proprietà
      return actionIcon;
      // #
    } else {
      // Nessuna configurazione
      return '';
    }
  }
}

/**
 * Pipe dedicato al componente risca-table che controlla se una lista di elementi RiscaTableDataConfig<any>[] non esiste o è vuota.
 */
@Pipe({ name: 'checkSottorighe' })
export class RiscaTableCheckSottorighePipe<T> {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che controlla se un RiscaTableDataConfig<any>[] non esiste o è vuota.
   * @param dataConfigs RiscaTableBodyTabMethodData contenente l'oggetto principale da controllare.
   * @returns False se è vuota o undefined. True se ha almeno un elemento.
   */
  transform(dataConfigs?: RiscaTableDataConfig<any>[]): boolean {
    if (!dataConfigs) {
      return false;
    }

    return dataConfigs.length > 0;
  }
}

/**
 * Pipe dedicata alla gestione della visibilita delle action per la tabella, sulla base della configurazione.
 */
@Pipe({ name: 'visibleTableAction' })
export class RiscaVisibleTableActionPipe<T> {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che veirifica e ritorna se un'action della tabella è visible.
   * @param actionConfig RiscaTableBodyTabMethodData contenente l'oggetto di configurazione per la gestione della visibilità.
   * @param row RiscaTableDataConfig<T> con l'oggetto che identifica la riga della tabella.
   * @returns boolean che definisce se l'action è visibile (true) o non visibile (false).
   */
  transform(
    actionConfig: RiscaTableBodyTabMethodData,
    row: RiscaTableDataConfig<T>
  ): boolean {
    // Verifico l'input
    if (!actionConfig) {
      // Non risulta visibile
      return false;
    }

    // Recupero dalla configurazione la funzione di visible
    const { visible } = actionConfig;
    // Verifico se non è stata dichiarata una funzione
    if (visible == undefined) {
      // Per default è visibile
      return true;
    }

    // Esiste la funzione, la eseguo passando le informazioni della riga
    return visible(row);
  }
}

/**
 * Pipe dedicata alla gestione della visibilita delle action per la tabella, sulla base della configurazione.
 */
@Pipe({ name: 'tableSourceCss' })
export class RiscaTableSourceCssPipe<T> {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che verifica e ritorna lo stile per un source della tabella.
   * @param body RiscaTableSourceMapBody contenente l'oggetto con la configurazione della tabella.
   * @param row RiscaTableDataConfig<T> con l'oggetto che identifica la riga della tabella.
   * @param type RiscaCssHandlerTypes il tipo di stile per gestire l'output.
   * @returns RiscaCss che definisce l'output grafico.
   */
  transform(
    body: RiscaTableSourceMapBody,
    row: RiscaTableDataConfig<T>,
    type: RiscaCssHandlerTypes
  ): RiscaCss {
    // Verifico l'input
    if (!this.checkInput(body, type)) {
      // Non risulta visibile
      return riscaCssHandler(type);
    }

    // Genero l'output per il css per la proprietà cssLabel
    const cssLabel: RiscaCss = this.handleCssLabel(body, type);
    // Genero l'output per il css per la proprietà cssCustom
    const cssCustom: RiscaCss = this.handleCssCustom(body, row, type);

    // Esiste la funzione, la eseguo passando le informazioni della riga
    return this.handleCssBody(cssLabel, cssCustom, type);
  }

  /**
   * Controlla i campi di definizione dello stile della tabella
   * @param body RiscaTableSourceMapBody contenente l'oggetto con la configurazione della tabella.
   * @param row RiscaTableDataConfig<T> con l'oggetto che identifica la riga della tabella.
   * @returns boolean ritorna true se i controlli minimi sono passati, false se non c'è il body
   */
  private checkInput(
    body: RiscaTableSourceMapBody,
    type: RiscaCssHandlerTypes
  ): boolean {
    // Controllo il type
    if (type == undefined) {
      throw new Error(
        'No RiscaCssHandlerTypes defined - RiscaVisibleTableActionPipe'
      );
    }
    // Controllo il body
    if (body == undefined) {
      return false;
    }
    // I controlli minimi sono passati
    return true;
  }

  /**
   * Gestisce l'output grafico della proprietà cssLabel dell'oggetto body.
   * @param body RiscaTableSourceMapBody contenente l'oggetto con la configurazione della tabella.
   * @param type RiscaCssHandlerTypes il tipo di stile per gestire l'output.
   * @returns RiscaCss che definisce l'output grafico.
   */
  private handleCssLabel(
    body: RiscaTableSourceMapBody,
    type: RiscaCssHandlerTypes
  ): RiscaCss {
    // Estraggo la proprietà cssLabel
    const { cssLabel } = body;
    // Lancio la funzione di gestione per il css
    return riscaCssHandler(type, cssLabel);
  }

  /**
   * Gestisce l'output grafico della proprietà cssCustom dell'oggetto body.
   * @param body RiscaTableSourceMapBody contenente l'oggetto con la configurazione della tabella.
   * @param row RiscaTableDataConfig<T> con l'oggetto che identifica la riga della tabella.
   * @param type RiscaCssHandlerTypes il tipo di stile per gestire l'output.
   * @returns RiscaCss che definisce l'output grafico.
   */
  private handleCssCustom(
    body: RiscaTableSourceMapBody,
    row: RiscaTableDataConfig<T>,
    type: RiscaCssHandlerTypes
  ): RiscaCss {
    // Estraggo la proprietà cssCustom
    const { cssCustom } = body;
    // Verifica di esistenza della proprietà cssCustom
    if (cssCustom == undefined) {
      // Non esiste, quindi lancio la Handler senza dati
      return riscaCssHandler(type);
    }
    // Esiste, quindi lancio la funzione di esecuzione di una funzione
    const cssCustomRes: RiscaCss = cssCustom(row);
    // Lancio la funzione di gestione per il css
    return riscaCssHandler(type, cssCustomRes);
  }

  /**
   * Gestisce l'output grafico dell'oggetto body.
   * @param cssLabel RiscaCss che definisce l'output grafico per la proprietà cssLabel del body
   * @param cssCustom RiscaCss che definisce l'output grafico per la proprietà cssCustom del body
   * @param type RiscaCssHandlerTypes il tipo di stile per gestire l'output.
   * @returns RiscaCss che definisce l'output grafico.
   */
  private handleCssBody(
    cssLabel: RiscaCss,
    cssCustom: RiscaCss,
    type: RiscaCssHandlerTypes
  ): RiscaCss {
    // Verifico se il css è da gestire come classe
    if (type === RiscaCssHandlerTypes.class) {
      // Unisco gli stili come stringhe
      const cssClass = `${cssLabel} ${cssCustom}`.trim();
      // Ritorno le classi di stile concatenate
      return cssClass;
    }
    // Verifico se il css è da gestire come style
    if (type === RiscaCssHandlerTypes.style) {
      // Forzo la tipizzazione dell'input per gestirli come oggetti
      const cssLObj = cssLabel as object;
      const cssCObj = cssCustom as object;
      // Unisco gli stili come oggetti
      const cssStyle = { ...cssLObj, ...cssCObj };
      // Ritorno le classi di stile concatenate
      return cssStyle;
    }
    // Questa situazione non deve verificarsi mai ma la mettiamo come callback
    throw new Error('RiscaVisibleTableActionPipe - handleCssBody type unknown');
  }
}

/**
 * Pipe che esegue una funzione in input per gestire diverse situazioni.
 */
@Pipe({ name: 'tableTrDynamicClass' })
export class RiscaTableTrDynamicClassPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che esegue la funzione passata come parametro.
   * @param f (v?: any, ...p: any[]) => any da eseguire.
   * @param row RiscaTableDataConfig<any> con il valore da passare come parametro alla funzione.
   * @returns any come risultato dell'operazione.
   */
  transform(
    f: (v?: any, ...p: any[]) => any,
    row: RiscaTableDataConfig<any>
  ): string {
    // Verifico sia definita la funzione per la classe dinamica
    if (!f) {
      // Nessuna funzione
      return '';
    }

    // Richiamo il servizio di utility per l'esecuzione della funzione
    return riscaExecute(f, row);
  }
}

/**
 * Pipe che estrae il dato relativo alla checbox per la compilazione del dato in tabella.
 */
@Pipe({ name: 'riscaTableCACheckboxData' })
export class RiscaTableCACheckboxDataPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che estrae il check per la checkbox.
   * @param config IRiscaTableAzioneCustom<T> con i dati di configurazione.
   * @returns boolean come valore d'assegnare alla checkbox.
   */
  transform(config: IRiscaTableAzioneCustom<any>): boolean {
    // Verifico l'input
    if (!config) {
      // Default: false
      return false;
    }

    // Estraggo dalla configurazione l'oggetto dati della riga
    const dataTable: any = config.data?.dataTable;
    // Estraggo dalla configurazione il nome della proprietà
    const property: string = config.action?.checkboxConfigs?.dataTableProperty;

    // Verifico esistano sia dataTable che property
    if (!dataTable || property == undefined) {
      // Default: false
      return false;
    }

    // Cerco di recuperare il valore finale
    const check = dataTable[property];

    // Ritorno il valore del check
    return check ?? false;
  }
}

/**
 * Pipe che gestisce il colspan per la tabella a seconda delle configurazioni in input.
 */
@Pipe({ name: 'riscaTableColspan' })
export class RiscaTableColspan implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che gestisce il numero di colspan della tabella, in base alle configurazioni in input.
   * @param riscaTableTargetConfig RiscaTableTargetConfig con la configurazione per identificare la configurazione per la tabella.
   * @param sourceMap RiscaTableSourceMap[] con i dati di configurazione della tabella, per header e body.
   * @param i number con l'indice posizionale della colonna che richiama la pipe.
   * @returns number con il numero di colspan da dafinire per la colonna.
   */
  transform(
    riscaTableTargetConfig: RiscaTableTargetConfig,
    sourceMap: RiscaTableSourceMap[],
    i: number
  ): number {
    // Verifico l'input
    if (!riscaTableTargetConfig || !sourceMap || i < 0) {
      // Mancano le configurazioni, niente colspan
      return 0;
    }

    // Verifico quale delle configurazioni bisogna gestire
    if (riscaTableTargetConfig === RiscaTableTargetConfig.header) {
      // Colspan sull'header, richiamo la funzione specifica
      return this.colspanHeader(sourceMap, i);
      // #
    }

    // Condizione non gestita
    return 0;
  }

  /**
   * Funzione che gestisce il numero di colspan della tabella, per la configurazione dell'header.
   * @param sourceMap RiscaTableSourceMap[] con i dati di configurazione della tabella, per header.
   * @param i number con l'indice posizionale della colonna che richiama la pipe.
   * @returns number con il numero di colspan da dafinire per la colonna.
   */
  private colspanHeader(sourceMap: RiscaTableSourceMap[], i: number): number {
    // Verifico l'input
    if (!sourceMap || i < 0) {
      // Mancano le configurazioni, niente colspan
      return 0;
    }

    // Definisco un contatore per il colspan (parte da 1 = nessun colspan), verrà incrementato per ogni configurazione "successiva" che è definita come colspan
    let colspan: number = 1;
    // Definisco un flag che mi tiene conto della logica di uscita dal ciclo di controlli
    let checkNext: boolean = true;
    // Definisco l'indice posizionale dell'elemento successivo per verificare la configurazione
    let nextI = i + 1;

    // Inizio i cicli progressivi sulle configurazioni, fino a che non c'è una configurazione successiva che non è da gestire come colspan
    while (checkNext) {
      // Tento di recuperare l'oggetto alla posizione successiva
      let nextObj: RiscaTableSourceMap;
      nextObj = sourceMap[nextI];

      // Verifico se esiste la configurazione
      if (nextObj?.header?.colspan) {
        // E' una configurazione colspan, incremento il contatore
        colspan++;
        // Incremento anche l'indice per verificare la configurazione successiva
        nextI++;
        // #
      } else {
        // Non esiste configurazione successiva, interrompo il ciclo
        checkNext = false;
        // #
      }
    }

    // Ritorno il contatore dei colspan
    return colspan;
  }
}
