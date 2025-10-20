import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { Gruppo } from '../../../core/commons/vo/gruppo-vo';
import { SoggettoVo } from '../../../core/commons/vo/soggetto-vo';
import { RiscaTableDataConfig } from '../../components/risca/risca-table/utilities/risca-table.classes';
import { lableStyleOverflow } from '../../consts/utilities.consts';
import {
  NgBoostrapPlacements,
  NgBoostrapTriggers,
} from '../enums/ng-bootstrap.enums';
import {
  RCOrientamento,
  RiscaAppendixPosition,
  RiscaButtonTypes,
  RiscaInfoLevels,
  RiscaSortTypes,
} from '../enums/utilities.enums';
import {
  DynamicObjAny,
  DynamicObjString,
  ICallbackDataModal,
  ICommonParamsModal,
  IParseValueRules,
  IRiscaAlertConfigs,
  IRiscaAppendix,
  IRiscaButtonAllConfig,
  IRiscaButtonConfig,
  IRiscaButtonCss,
  IRiscaDDButtonConfig,
  IRiscaDDButtonCss,
  IRiscaDDItemConfig,
  IRiscaFormInputChoice,
  IRiscaFormInputCommon,
  IRiscaFormInputCss,
  IRiscaFormInputDatepicker,
  IRiscaFormInputEmail,
  IRiscaFormInputNumber,
  IRiscaFormInputSelect,
  IRiscaFormInputText,
  IRiscaFormInputTypeahead,
  IRiscaIconConfigs,
  IRiscaPopoverConfig,
  IRiscaServerError,
  IRiscaServerErrorDetail,
  IRiscaServerErrorInfo,
  IRiscaTableBodyInputField,
  IRiscaTableCss,
  IRiscaTableGraphic,
  IRiscaTableInput,
  IRiscaTablePagination,
  IRiscaTableSMHActions,
  IRiscaTableSourceMapBody,
  IRiscaTableSourceMapHeader,
  RiscaTableBodyCommonIconData,
  RiscaTableBodySourceData,
  RiscaTableBodyTabMethodConfig,
  RiscaTableSorting,
} from '../interfaces/utilities.interfaces';
import {
  RiscaCss,
  TRiscaAlertType,
  TRiscaFormInputChoiceOrientation,
} from '../types/utilities.types';

/**
 * Classe personalizzata per la gestione del modello dati della modale di conferma.
 */
export class ConfirmDataModal implements ICommonParamsModal {
  title: string;
  body: string;
  buttonCancel: string;
  buttonConfirm: string;
  onConfirm: (...args: any[]) => any;
  onClose: (...args: any[]) => any;
  onCancel: (...args: any[]) => any;
  confirmPayload: any;
  closePayload: any;
  cancelPayload: any;
  showConfirmBtn: boolean;
  showCloseBtn: boolean;
  showCancelBtn: boolean;

  constructor(c?: ICommonParamsModal) {
    this.title = c?.title ?? '';
    this.body = c?.body ?? '';
    this.buttonCancel = c?.buttonCancel ? c.buttonCancel : 'ANNULLA';
    this.buttonConfirm = c?.buttonConfirm ? c.buttonConfirm : 'CONFERMA';
    this.onConfirm = c?.onConfirm;
    this.onClose = c?.onClose;
    this.onCancel = c?.onCancel;
    this.confirmPayload = c?.confirmPayload;
    this.closePayload = c?.closePayload;
    this.cancelPayload = c?.cancelPayload;
    this.showConfirmBtn = c?.showConfirmBtn ?? true;
    this.showCloseBtn = c?.showCloseBtn ?? true;
    this.showCancelBtn = c?.showCancelBtn ?? true;
  }

  /**
   * Funzione di setup dati partendo da un oggetto di configurazione.
   * Se una configurazione non viene definita, non aggiornerà il valore della classe.
   * @param c ICommonParamsModal con l'oggetto per il setup.
   */
  setupByICommonParamsModal(c?: ICommonParamsModal) {
    // Verifico l'input
    if (!c) {
      // Niente configurazione
      return;
    }

    // Verifico il parametro
    if (c.title) {
      this.title = c.title;
    }
    if (c.body) {
      this.body = c.body;
    }
    if (c.buttonCancel) {
      this.buttonCancel = c.buttonCancel;
    }
    if (c.buttonConfirm) {
      this.buttonConfirm = c.buttonConfirm;
    }
    if (c.onConfirm) {
      this.onConfirm = c.onConfirm;
    }
    if (c.onClose) {
      this.onClose = c.onClose;
    }
    if (c.onCancel) {
      this.onCancel = c.onCancel;
    }
    if (c.confirmPayload) {
      this.confirmPayload = c.confirmPayload;
    }
    if (c.closePayload) {
      this.closePayload = c.closePayload;
    }
    if (c.cancelPayload) {
      this.cancelPayload = c.cancelPayload;
    }
  }

  /**
   * Funzione di comodo che genera un oggetto ICallbackDataModal dalle callback interne.
   */
  getCallbacks(): ICallbackDataModal {
    // Genero e ritorno un oggetto per le callback
    return {
      onConfirm: this.onConfirm,
      onClose: this.onClose,
      onCancel: this.onCancel,
    };
  }
}

/**
 * Classe personalizzata contenente la definizione di regular expression utili in risca.
 */
export class RiscaRegExp {
  /** RegExp: solo caratteri numerici. */
  onlyNumber: RegExp = new RegExp(/^[0-9]*$/);
  /** RegExp: solo caratteri numerici. */
  onlyNumberWDecimal: RegExp = new RegExp(/^[0-9.,]*$/);
  /** RegExp: i caratteri definiti nell'array. */
  usoLeggeProperty: RegExp = new RegExp(/[#-@]/g);
  /** RegExp: validazione email. */
  email: RegExp = new RegExp(/[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/);
  /** RegExp: codice fiscale per persone fisiche. */
  codiceFiscalePF: RegExp = new RegExp(
    /^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$/
  );
  /** RegExp: codice fiscale per persone giuridiche private. */
  codiceFiscalePGPrivata: RegExp = new RegExp(/^[0-9]{11}$/);
  /** RegExp: codice fiscale per persone giuridiche pubblica. */
  codiceFiscalePGPubblica: RegExp = new RegExp(/^[0-9]{11}$/);
  /** RegExp: codice per la gestione della partita iva. */
  partitaIva: RegExp = new RegExp(/^[0-9]{11}$/);
  /** RegExp: formato view date. */
  viewDate: RegExp = new RegExp(
    /^(0?[1-9]|[12][0-9]|3[01])[\/](0?[1-9]|1[012])[\/\-]\d{4}$/
  );

  /** RegExp: per tutti i tipi di codice fiscale. */
  codiceFiscaleAll: RegExp;

  constructor() {
    // Recupero le regex per i codici fiscali singoli
    const CF_PF = this.codiceFiscalePF.source;
    const CF_PGPri = this.codiceFiscalePGPrivata.source;
    const CF_PGPub = this.codiceFiscalePGPubblica.source;

    // Definisco la concatenazione per la gestione dei codici fiscali
    this.codiceFiscaleAll = new RegExp(`${CF_PF}|${CF_PGPri}|${CF_PGPub}`);
  }
}

/**
 * Classe che definisce le configurazioni CSS utilizzate dai componenti form-inputs.
 */
export class RiscaFormInputCss {
  /** String or any, compatibile con le direttive NgClass e NgStyle, che definisce la classe CSS del contenitore del form-group. */
  customForm: string | any;
  /** String che definisce la classe CSS del contenitore del form-group. */
  customFormCheck: string;
  /** String or any, compatibile con le direttive NgClass e NgStyle, che definisce la classe CSS dell'input. */
  customInput: string | any;
  /** String or any, compatibile con le direttive NgClass e NgStyle, che definisce la classe CSS della label. */
  customLabel: string | any;
  /** String compatibile con le classi css, o oggetto compatibile con la direttiva NgStyle che definisce lo stile del container. */
  customError: string | any;
  /** Boolean che definisce se visualizzare eventuali messaggi d'errore per il form group. */
  showErrorFG: boolean;
  /** Boolean che definisce se visualizzare eventuali messaggi d'errore per il form control. */
  showErrorFC: boolean;

  /** String or any, compatibile con le direttive NgClass e NgStyle, che definisce la classe CSS per il contenitore della label di sinistra. */
  labelColLeft: string | any;
  /** String or any, compatibile con le direttive NgClass e NgStyle, che definisce la classe CSS per il contenitore della label di destra. */
  labelColRight: string | any;

  /** RiscaAppendix con le informazioni da utilizzare per la gestione dell'appendice sulle input. */
  appendix?: RiscaAppendix;

  constructor(c: IRiscaFormInputCss) {
    this.customForm = c.customForm || '';
    this.customFormCheck = c.customFormCheck || '';
    this.customInput = c.customInput || '';
    this.customLabel = c.customLabel || '';
    this.customError = c.customError || '';
    this.showErrorFG = c.showErrorFG !== undefined ? c.showErrorFG : false;
    this.showErrorFC = c.showErrorFC !== undefined ? c.showErrorFC : false;
    this.labelColLeft = c.labelColLeft || '';
    this.labelColRight = c.labelColRight || '';
    this.appendix = c.appendix ? new RiscaAppendix(c.appendix) : undefined;
  }
}

/**
 * Classe che definisce le proprietà per la gestione dell'appendice per le input di RISCA.
 */
export class RiscaAppendix {
  text: string;
  position: RiscaAppendixPosition;

  constructor(c?: IRiscaAppendix) {
    this.text = c?.text ?? '-';
    this.position = c?.position ?? RiscaAppendixPosition.right;
  }
}

/**
 * Classe che definisce le configurazioni CSS utilizzate dal componente risca-table.
 */
export class RiscaTableCss {
  /** Boolean che definisce se visualizzare il contenitore esterno (true) o solo la tabella (false). */
  showContainer: boolean;
  /** String che definisce il title per la tabella, contenuta nel container. */
  titleContainer: string;
  /** String che definisce la class di css d'associare a: titleContainer */
  titleCss: string;
  /** string compatibile con la direttiva NgClass o DynamicObjAny compatibile con la direttiva NgStyle per definire stili custom al container di wrapper della tabella. */
  table: string | DynamicObjAny;
  /** String che definisce la class di css d'associare a: thead */
  thead: string;
  /** String che definisce la class di css d'associare a: thead tr */
  thead_tr: string;
  /** String che definisce la class di css d'associare a: th */
  th: string;
  /** String che definisce la class di css d'associare a: tbody */
  tbody: string;
  /** String che definisce la class di css d'associare a: tbody tr */
  tbody_tr: string;
  /** String che definisce la class di css d'associare a: td */
  td: string;

  /** Funzione che viene invocata per ogni riga della tabella e gestisce, in base ai dati passati, una stringa compatibile con NgClass che definisce stili specifici. */
  trDynamicClass?: (row: RiscaTableDataConfig<any>) => string;

  constructor(dataTable?: IRiscaTableGraphic, cssTable?: IRiscaTableCss) {
    this.showContainer =
      dataTable?.showContainer !== undefined ? dataTable.showContainer : false;
    this.titleContainer = dataTable?.titleContainer || '';
    this.titleCss = dataTable?.titleCss || '';
    this.table = cssTable?.table || '';
    this.thead = cssTable?.thead || '';
    this.thead_tr = cssTable?.thead_tr || '';
    this.th = cssTable?.th || '';
    this.tbody = cssTable?.tbody || '';
    this.tbody_tr = cssTable?.tbody_tr || '';
    this.td = cssTable?.td || '';

    this.trDynamicClass = cssTable?.trDynamicClass;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs.
 */
export class RiscaFormInputCommon implements IRiscaFormInputCommon {
  /** String che definisce la label dell'input in posizione: sopra. */
  label?: string;
  /** String che definisce la label dell'input in posizione: sinistra. */
  labelLeft?: string;
  /** String che definisce la label dell'input in posizione: destra. */
  labelRight?: string;
  /** String che definisce la label dell'input in posizione: sotto. */
  labelBottom?: string;
  /** Boolean che definisce se la label dell'input deve essere nascosta (mantenendo lo spazio dell'HTML). */
  hideLabel?: boolean;
  /** Boolean che definisce se la label dell'input deve essere nascosta (mantenendo lo spazio dell'HTML). */
  hideLabelLeft?: boolean;
  /** Boolean che definisce se la label dell'input deve essere nascosta (mantenendo lo spazio dell'HTML). */
  hideLabelRight?: boolean;
  /** Boolean che definisce se la label dell'input deve essere nascosta (mantenendo lo spazio dell'HTML). */
  hideLabelBottom?: boolean;
  /** String che definisce la label dell'input, posizionata a destra come mini descrizione. */
  extraLabelRight?: string;
  /** String che definisce la label dell'input, posizionata sotto come mini descrizione. */
  extraLabelSub?: string;
  /** String che definisce il placeholder da visualizzare all'interno della input. */
  placeholder?: string;
  /** Number che definisce la lunghezza massima del campo. */
  maxLength: number;
  /** Boolean che definisce se l'input deve contenere solo numeri. */
  onlyNumber?: boolean;
  /** Boolean che definisce se l'input deve contenere anche i decimali, se "onlyNumber" è true. */
  useDecimal?: boolean;
  /** Boolean che definisce se l'input può contenere il segno (-), se "onlyNumber" è true. */
  useSign?: boolean;
  /** String che definisce il tooltip da visualizzare sulla label del componente. */
  title?: string;

  /** (value: any) => any; che permette di definire una logica di sanitizzazione della input. */
  sanitizeLogic?: (value: any) => any;

  /**
   * Costruttore.
   */
  constructor(c: IRiscaFormInputCommon) {
    this.label = c.label;
    this.labelLeft = c.labelLeft;
    this.labelRight = c.labelRight;
    this.labelBottom = c.labelBottom;
    this.hideLabel = c.hideLabel ?? false;
    this.hideLabelLeft = c.hideLabelLeft ?? false;
    this.hideLabelRight = c.hideLabelRight ?? false;
    this.hideLabelBottom = c.hideLabelBottom ?? false;
    this.extraLabelRight = c.extraLabelRight;
    this.extraLabelSub = c.extraLabelSub;
    this.placeholder = c.placeholder ?? '';
    this.maxLength = c.maxLength ?? 15;
    this.onlyNumber = c.onlyNumber ?? false;
    this.useDecimal = c.useDecimal ?? false;
    this.useSign = c.useSign ?? false;
    this.title = c.title ?? '';

    this.sanitizeLogic = c.sanitizeLogic ?? undefined;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs: datepicker.
 */
export class RiscaFormInputDatepicker
  extends RiscaFormInputCommon
  implements IRiscaFormInputDatepicker
{
  /** NgbDateStruct che definisce la data minima del calendario. */
  minDate: NgbDateStruct;
  /** NgbDateStruct che definisce la data massima del calendario. */
  maxDate: NgbDateStruct;

  constructor(c: IRiscaFormInputDatepicker) {
    super(c);
    this.minDate = c.minDate;
    this.maxDate = c.maxDate;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs: text.
 */
export class RiscaFormInputText
  extends RiscaFormInputCommon
  implements IRiscaFormInputText
{
  constructor(c: IRiscaFormInputText) {
    super(c);
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs: search.
 */
export class RiscaFormInputSearch
  extends RiscaFormInputCommon
  implements IRiscaFormInputText
{
  constructor(c: IRiscaFormInputText) {
    super(c);
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs: number.
 */
export class RiscaFormInputNumber
  extends RiscaFormInputCommon
  implements IRiscaFormInputNumber
{
  /** Number che definisce il minimo impostabile per la input. */
  min: number;
  /** Number che definisce il massimo impostabile per la input. */
  max: number;
  /** Number che definisce il massimo impostabile per la input. */
  step: number;
  /** Number che definisce il numero di decimali da gestire per la input. */
  decimals: number;
  /** boolean che permette di mantenere i decimali 0 (non significativi) se la quantità di decimali non raggiungesse il numero di decimali impostati. */
  decimaliNonSignificativi: boolean = false;
  /** boolean che permette di attivare il separatore delle migliaia. */
  allowThousandsSeparator: boolean;

  constructor(c: IRiscaFormInputNumber) {
    super(c);
    this.min = c.min;
    this.step = c.max;
    this.max = c.step;
    this.decimals = c.decimals ?? 0;
    this.decimaliNonSignificativi = c?.decimaliNonSignificativi ?? false;
    this.allowThousandsSeparator = c?.allowThousandsSeparator;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs: email.
 */
export class RiscaFormInputEmail
  extends RiscaFormInputCommon
  implements IRiscaFormInputEmail
{
  constructor(c: IRiscaFormInputEmail) {
    super(c);
    this.maxLength = c.maxLength || 40;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs: selezione (checkbox, radio).
 */
export class RiscaFormInputChoice implements IRiscaFormInputChoice {
  /** String che definisce l'orientamento dell'input. */
  orientation: TRiscaFormInputChoiceOrientation;

  constructor(c: IRiscaFormInputChoice) {
    this.orientation = c.orientation || RCOrientamento.orizzontale;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs: checkbox.
 */
export class RiscaFormInputCheckbox
  extends RiscaFormInputChoice
  implements IRiscaFormInputChoice
{
  constructor(c: IRiscaFormInputChoice) {
    super(c);
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs: radio.
 */
export class RiscaFormInputRadio
  extends RiscaFormInputChoice
  implements IRiscaFormInputChoice
{
  constructor(c: IRiscaFormInputChoice) {
    super(c);
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs: typeahead.
 */
export class RiscaFormInputTypeahead
  extends RiscaFormInputCommon
  implements IRiscaFormInputTypeahead
{
  /** Number che definisce quando attivare la ricerca. */
  searchOnLength: number;
  /** Funzione utilizzata per la ricerca dei valori da visualizzare come suggerimento. Deve ritornare un Observable. */
  typeaheadSearch: (v: string) => Observable<any[]>;
  /** Funzione utilizzata per la mappatura dei risultati ritornati dalla funzione "typeaheadSearch". Verrà utilizzata anche per valorizzare l'input alla selezione dal popup. */
  typeaheadMap: (v: any) => string;
  /** Number che definisce i millisecondi di attesa prima di lanciare la funzione di ricerca. */
  debounceTime: number;
  /** Boolean che definisce se la lista di elementi deve essere gestita con il controllo di data_fine_validita e l'evidenziazione della riga. */
  dataValidita?: boolean;

  constructor(c: IRiscaFormInputTypeahead) {
    super(c);
    this.searchOnLength = c.searchOnLength || 3;
    this.typeaheadSearch = c.typeaheadSearch;
    this.typeaheadMap = c.typeaheadMap;
    this.debounceTime = c.debounceTime || 300;
    this.dataValidita = c.dataValidita ?? false;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dai componenti form-inputs: select.
 */
export class RiscaFormInputSelect
  extends RiscaFormInputCommon
  implements IRiscaFormInputSelect
{
  /** Boolean che definisce se la selecte deve comportarsi come una multi-select. */
  multiple: boolean;
  /** Boolean che definisce se aggiungere un elemento vuoto all'inizio della select. */
  allowEmpty: boolean;
  /** String che definisce la label da visualizzare per l'elemento vuoto. */
  emptyLabel: string;
  /** Boolean che definisce se la empty label è selezionata per default. */
  emptyLabelSelected: boolean;
  /** Boolean che definisce il valore di default deve essere ignorato per i controlli. */
  ignoreDefault: boolean;
  /** Funzione che permette di manipolare l'oggetto gestito dalla select per un custom output come descrizione dell'option. */
  customOption?: (v: any) => string;
  /** Funzione che permette di manipolare la classe di stile per un'option della select. Il ritorno deve essere compatibile con la direttiva NgClass. */
  customOptionClass?: (v: any) => string[];
  /** Funzione che permette di manipolare lo stile per un'option della select. Il ritorno deve essere compatibile con la direttiva NgStyle. */
  customOptionStyle?: (v: any) => DynamicObjString;

  constructor(c: IRiscaFormInputSelect) {
    super(c);
    this.multiple = c.multiple !== undefined ? c.multiple : false;
    this.allowEmpty = c.allowEmpty !== undefined ? c.allowEmpty : false;
    this.emptyLabel = c.emptyLabel || '';
    this.emptyLabelSelected =
      c.emptyLabelSelected !== undefined ? c.emptyLabelSelected : false;
    this.ignoreDefault =
      c.ignoreDefault !== undefined ? c.ignoreDefault : false;
    this.customOption = c.customOption;
    this.customOptionClass = c.customOptionClass;
    this.customOptionStyle = c.customOptionStyle;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dal componente risca-table.
 */
export class RiscaTableInput {
  /** RiscaTableSourceMap contenente le informazioni di configurazione per le colonne e i dati per le righe. */
  sourceMap: RiscaTableSourceMap[] = [];
  /** RiscaTableSourceMap contenente le informazioni di configurazione per i dati delle righe. */
  sourceMapSub?: RiscaTableSourceMap[];
  /** RiscaTableSorting che definisce le regole di ordinamento della tabella. */
  sorting?: RiscaTableSorting;
  /** RiscaTablePagination contenente le informazioni della paginazione. */
  pagination?: RiscaTablePagination;

  constructor(c: IRiscaTableInput) {
    // Verifico l'input
    if (!c) {
      // Non ci sono configurazioni
      return;
      // #
    } else {
      // Inizializzo le proprietà della classe
      this.sourceMap = c.sourceMap;
      this.sourceMapSub = c.sourceMapSub;
      this.sorting = c.sorting;
      this.pagination = c.pagination;
    }
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dal componente risca-table per la configurazione delle colonne e degli script.
 */
export class RiscaTableSourceMap {
  /** Oggetto RiscaTableSourceMapHeader che definisce la configurazione della riga di testata della tabella. */
  header?: RiscaTableSourceMapHeader;
  /** Oggetto RiscaTableSourceMapBody che definisce la configurazione delle righe di "valori" della tabella. */
  body: RiscaTableSourceMapBody;

  constructor(
    header?: RiscaTableSourceMapHeader,
    body?: RiscaTableSourceMapBody
  ) {
    this.header = header;
    this.body = body;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dal componente risca-table per la configurazione dell'header delle colonne.
 */
export class RiscaTableSourceMapHeader {
  /** String che definisce il nome della colonna. */
  label: string;
  /** String che definisce il tooltip (title) da visualizzare. */
  title?: string = '';
  /** Boolean che definisce che questa proprietà è ordinabile. */
  sortable: boolean;
  /** RiscaSortTypes che definisce il tipo di ordine di default. */
  sortType: RiscaSortTypes;
  /** String che definisce una classe di stile (compatibile con NgClass) o un oggetto any che definisce proprietà css (compatibile con NgStyle). */
  cssCell: string | any;
  /** String che definisce una classe di stile (compatibile con NgClass) o un oggetto any che definisce proprietà css (compatibile con NgStyle). */
  cssLabel: string | any;
  /** IRiscaTableSMHActions contenente le configurazioni per delle possibili azioni per la tabella. Prendono priorità rispetto ad una normale label, se definite. */
  action?: IRiscaTableSMHActions;
  /** boolean che definisce che la configurazione di questa colonna, per l'header, va unita mediante HTML colspan. */
  colspan?: boolean;

  constructor(c: IRiscaTableSourceMapHeader) {
    this.label = c.label;
    this.title = c.title !== undefined ? c.title : '';
    this.sortable = c.sortable ?? false;
    this.sortType = c.sortType || RiscaSortTypes.nessuno;
    this.cssCell = c.cssCell;
    this.cssLabel = c.cssLabel;
    this.action = c.action;
    this.colspan = c.colspan ?? false;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dal componente risca-table per la configurazione del body delle colonne.
 */
export class RiscaTableSourceMapBody {
  /** Boolean che definisce che la colonna visualizzerà i dati del source. */
  useSource: boolean;
  /** Boolean che definisce che la colonna visualizzerà una input dati. */
  useInput: boolean;
  /** Boolean che definisce che la colonna visualizzerà una struttura built-in della tabella. */
  useTabMethod: boolean;
  /** Boolean che definisce che la colonna utilizzerà un'icona comune per tutti i valori di source. */
  useCommonIcon: boolean;

  /** Oggetto RiscaTableBodySourceData con i dati di configurazione quando il flag: "useSource" è attivo. */
  sourceData: RiscaTableBodySourceData;
  /** Oggetto IRiscaTableBodyInputField con i dati di configurazione quando il flag: "useInput" è attivo. */
  inputData: IRiscaTableBodyInputField;
  /** Oggetto RiscaTableBodyTabMethodConfig con i dati di configurazione quando il flag: "useTabMethod" è attivo. */
  tabMethodData: RiscaTableBodyTabMethodConfig;
  /** Oggetto RiscaTableBodyCommonIconData con i dati di configurazione quando il flag: "useCommonIcon" è attivo. */
  commonIconData: RiscaTableBodyCommonIconData;

  /** String che definisce una classe di stile (compatibile con NgClass) o un oggetto any che definisce proprietà css (compatibile con NgStyle). */
  cssCell: string | any;
  /** String che definisce una classe di stile (compatibile con NgClass) o un oggetto any che definisce proprietà css (compatibile con NgStyle). */
  cssLabel: string | any;
  /** Funzione che definisce lo stile della casella in base alla logica di una funzione che si basa sul valore della row in input. */
  cssCustom?: (row: RiscaTableDataConfig<any>) => RiscaCss;

  /** String che definisce un title di default per la cella della colonna. */
  title?: string;
  /** Funzione che permette di definire un title in base alla logica della funzione. */
  titleCustom?: (data: any) => string;

  constructor(c: IRiscaTableSourceMapBody) {
    this.useSource = c.useSource ?? false;
    this.useInput = c.useInput ?? false;
    this.useTabMethod = c.useTabMethod ?? false;
    this.useCommonIcon = c.useCommonIcon ?? false;
    this.sourceData = c.sourceData;
    this.inputData = c.inputData;
    this.tabMethodData = c.tabMethodData;
    this.commonIconData = c.commonIconData;
    this.cssCell = c.cssCell;
    this.cssLabel = { ...c.cssLabel, ...lableStyleOverflow };
    this.cssCustom = c.cssCustom;
    this.title = c.title;
    this.titleCustom = c.titleCustom;
  }
}

/**
 * Classe che definisce le configurazioni DATI utilizzate dal componente risca-table per la configurazione della paginazione.
 */
export class RiscaTablePagination {
  /** Number che definisce il totale di elementi della tabella. */
  total?: number;
  /** Boolean che definisce se visualizzare il totale degli elementi. */
  showTotal?: boolean;
  /** String che definisce la label parlante per il totale degli elementi. */
  label?: string;
  /** Number che definisce il numero massimo di elementi da visualizzare per pagina. */
  elementsForPage?: number;
  /** Pagina corrente da mostrare. Si parte da 1. */
  currentPage?: number;
  /** Campo di ordinamento dei record */
  sortBy?: string;
  /** Verso di ordinamento dei record */
  sortDirection?: RiscaSortTypes;
  /** Numero massimo di pagine che si possono visualizzare */
  maxPages?: number;

  constructor(c?: IRiscaTablePagination) {
    this.total = c?.total;
    this.showTotal = c?.showTotal ?? true;
    this.label = c?.label ?? 'Risultati di ricerca';
    this.elementsForPage = c?.elementsForPage ?? 10;
    this.currentPage = c?.currentPage ?? 1;
    this.sortBy = c?.sortBy;
    this.sortDirection = c?.sortDirection;
    this.maxPages = c?.maxPages ?? 10;
  }
}

/**
 * Class che definisce la struttura di gestione degli errori da server.
 * Questa classe di base è stata pensata come estensione dell'oggetto: HttpErrorResponse; ma per comodità non viene dichiarata esplicamente l'extends della classe.
 */
export class RiscaServerError implements IRiscaServerError {
  /** RiscaServerErrorDetail con il dettaglio degli errori */
  error?: RiscaServerErrorInfo;
  errors?: RiscaServerErrorInfo[];
  [key: string]: any;

  constructor(c?: IRiscaServerError) {
    this.error = c?.error;
    this.errors = c?.errors;
  }
}

/**
 * Class che definisce la struttura del dettaglio di gestione degli errori da server.
 */
export class RiscaServerErrorInfo implements IRiscaServerErrorInfo {
  status?: string;
  code?: string;
  title?: string;
  detail?: IRiscaServerErrorDetail;
  links?: string[];

  constructor(c?: IRiscaServerErrorInfo) {
    this.status = c.status;
    this.code = c.code;
    this.title = c.title;
    this.detail = c.detail;
    this.links = c.links;
  }
}

/**
 * Classe che definisce le configurazioni per gli stili dei risca-button
 */
export class RiscaButtonCss implements IRiscaButtonCss {
  customButton?: string | any;
  typeButton?: RiscaButtonTypes;

  constructor(c?: IRiscaButtonCss) {
    this.customButton = c?.customButton;
    this.typeButton = c?.typeButton;
  }
}

/**
 * Classe che definisce le configurazioni per gli stili dei risca-button
 */
export class RiscaButtonConfig implements IRiscaButtonConfig {
  label: string;
  codeAEA?: string;

  constructor(c?: IRiscaButtonConfig) {
    this.label = c?.label;
    this.codeAEA = c?.label;
  }
}

/**
 * Classe che definisce le configurazioni per gli stili dei risca-dropdown-button
 */
export class RiscaDDButtonCss
  extends RiscaButtonCss
  implements IRiscaDDButtonCss
{
  customItems?: RiscaCss;

  constructor(c?: IRiscaDDButtonCss) {
    super(c);
    this.customItems = c?.customItems;
  }
}

/**
 * Classe che definisce le configurazioni per la struttura dati del risca-dropdown-button
 */
export class RiscaDDButtonConfig
  extends RiscaButtonConfig
  implements IRiscaDDButtonConfig
{
  dropdownItems: RiscaDDItemConfig[];

  constructor(c?: IRiscaDDButtonConfig) {
    super(c);
    this.dropdownItems = c?.dropdownItems ?? [];
  }
}

/**
 * Classe che definisce le configurazioni per la struttura dati di un elemento della dropdown del componente risca-dropdown-button.
 */
export class RiscaDDItemConfig implements IRiscaDDItemConfig {
  id: string;
  label: string;
  disabled: boolean;
  title: string;
  data: any;
  codeAEA: string;
  itemCss: RiscaCss;
  useIcon: boolean;
  useIconLeft: boolean;
  useIconRight: boolean;
  iconLeft: RiscaIconConfigs;
  iconRight: RiscaIconConfigs;

  constructor(c?: IRiscaDDItemConfig) {
    this.id = c.id;
    this.label = c.label;
    this.disabled = c?.disabled ?? false;
    this.title = c?.title ?? '';
    this.data = c?.data;
    this.codeAEA = c?.codeAEA;
    this.itemCss = c?.itemCss;
    this.useIcon = c?.useIcon ?? false;
    this.useIconLeft = c?.useIconLeft ?? false;
    this.useIconRight = c?.useIconRight ?? false;
    this.iconLeft = c?.iconLeft ?? c?.icon;
    this.iconRight = c?.iconRight ?? c?.icon;
  }
}

/**
 * Class che definisce l'oggetto contenente le informazioni di un gruppo e la lista di soggetti che definisco i componenti del gruppo.
 */
export class GruppoEComponenti {
  /** Gruppo contenente le informazioni del gruppo. */
  gruppo: Gruppo;
  /** Array di Soggetto che definiscono i dati dei componenti. */
  componenti: SoggettoVo[];

  constructor(g?: Gruppo, c?: SoggettoVo[]) {
    this.gruppo = g;
    this.componenti = c;
  }
}

/**
 * Interfaccia che definisce le regole per la gestione specifica di un qualsiasi valore
 */
export class ParseValueRules implements IParseValueRules {
  /** Controllo che verifica se un oggetto è vuoto, deve essere convertito a null. */
  emptyObjToNull: boolean = true;
  /** Controllo che va a sanitificare tutte le proprietà front end all'interno dell'oggetto. */
  sanitizeFEProperties: boolean = true;

  /**
   * Costruttore
   */
  constructor(c?: IParseValueRules) {
    // Verifico l'esistenza della configurazione
    if (!c) {
      // Nessuna inizializzazione
      return;
    }

    // Verifico se esistono le regole
    this.emptyObjToNull = this.checkRule(c.emptyObjToNull);
    this.sanitizeFEProperties = this.checkRule(c.sanitizeFEProperties);
  }

  private checkRule(r?: boolean): boolean {
    // Verifico l'input
    if (r === undefined) {
      // Ritorno false
      return false;
      // #
    } else {
      // Ritorno il flag della regola
      return r;
    }
  }
}

/**
 * Classe che rappresenta la configurazione dell'alert.
 */
export class RiscaAlertConfigs {
  /** string che definisce la tipologia di messaggio da gestire. */
  type?: TRiscaAlertType = RiscaInfoLevels.none;
  /** string o array string che definisce i messaggi da visualizzare. */
  messages?: string | string[] = [];
  /** boolean che definisce se il pannello di alert è persistente. Per default è true. */
  persistentMessage?: boolean = true;
  /** number che definisce dopo quanto nascondere il panel (in millisecondi). Per default il tempo è 5000 millisecondi. */
  timeoutMessage?: number = 5000;
  /** boolean che definisce se usare il layout compatto. Il default è false. */
  compactLayout?: boolean = false;
  /** string compatibile con le classi css, o oggetto compatibile con la direttiva NgStyle che definisce lo stile del container. */
  containerCss?: string | any;
  /** IRiscaButtonAllConfig che definisce le configurazioni per il pulsante di "conferma" dell'alert. */
  buttonConfirm?: IRiscaButtonAllConfig;
  /** IRiscaButtonAllConfig che definisce le configurazioni per il pulsante di "annulla" dell'alert. */
  buttonCancel?: IRiscaButtonAllConfig;
  /** boolean che definisce se mostrare o meno il pulsante X in alto a destra. */
  allowAlertClose?: boolean = false;
  /** any che definisce le informazioni per il pulsante di "conferma", nel momento della sua pressione. */
  payloadConfirm?: any;
  /** any che definisce le informazioni per il pulsante di "annulla", nel momento della sua pressione. */
  payloadCancel?: any;

  constructor(c?: IRiscaAlertConfigs) {
    // Verifico l'input
    if (!c) {
      return;
    }

    if (c.type) {
      this.type = c.type;
    }
    if (c.messages) {
      this.messages = c.messages;
    }
    if (c.persistentMessage !== undefined) {
      this.persistentMessage = c.persistentMessage;
    }
    if (c.timeoutMessage !== undefined) {
      this.timeoutMessage = c.timeoutMessage;
    }
    if (c.compactLayout !== undefined) {
      this.compactLayout = c.compactLayout;
    }
    if (c.containerCss) {
      this.containerCss = c.containerCss;
    }
    if (c.buttonConfirm) {
      this.buttonConfirm = c.buttonConfirm;
    }
    if (c.buttonCancel) {
      this.buttonCancel = c.buttonCancel;
    }
    if (c.payloadConfirm) {
      this.payloadConfirm = c.payloadConfirm;
    }
    if (c.payloadCancel) {
      this.payloadCancel = c.payloadCancel;
    }
    if (c.allowAlertClose != undefined) {
      this.allowAlertClose = c.allowAlertClose;
    }
  }
}

/**
 * Classe che rappresenta le configurazioni per il componente risca-icon.
 */
export class RiscaIconConfigs implements IRiscaIconConfigs {
  /** Array di string che definisce le classi da utilizzare. */
  classes: string[] = [];
  /** String che definisce l'url della risorsa dell'icona. */
  icon: string = '';
  /** Any che definisce gli css per l'icona, deve essere compatibile con NgStyle. */
  iconStyles: RiscaCss = {};
  /** String che definisce il title dell'icona. */
  iconTitle: string = '';
  /** Boolean che definisce se l'icona è disabilitata. */
  disabled: boolean = false;
  /** Boolean che definisce se utilizzare la classe di default dell'icona. */
  useDefault: boolean = true;
  /** Boolean che definisce se attivare il click dell'icona. */
  enableClick: boolean = false;

  constructor(c?: IRiscaIconConfigs) {
    this.classes = c?.classes ?? [];
    this.icon = c?.icon ?? '';
    this.iconStyles = c?.iconStyles ?? {};
    this.iconTitle = c?.iconTitle ?? '';
    this.disabled = c?.disabled ?? false;
    this.useDefault = c?.useDefault ?? true;
    this.enableClick = c?.enableClick ?? false;
  }
}

/**
 * Classe di configurazione che mappa le configurazioni per la gestione per Angular Bootstrap Popover.
 * @see https://ng-bootstrap.github.io/releases/11.x/#/components/popover/api
 * @tutorial https://ng-bootstrap.github.io/releases/11.x/#/components/popover/examples
 */
export class RiscaPopoverConfig {
  // INPUTS
  animation: boolean;
  autoClose: true | false | 'inside' | 'outside';
  closeDelay: number;
  // container: string;
  disablePopover: boolean;
  ngbPopover: (e: any) => string; // string | TemplateRef<any> | null | undefined;
  popoverTitle?: (e: any) => string | undefined; // string | TemplateRef<any> | null | undefined;
  openDelay: number;
  placement: NgBoostrapPlacements;
  popoverClass: string;
  triggers: NgBoostrapTriggers;
  // OUTPUTS
  hidden: () => any;
  shown: () => any;
  // METHODS
  open: (context: any) => void;
  close: () => void;
  toggle: () => void;
  isOpen: () => boolean;

  constructor(c?: IRiscaPopoverConfig) {
    this.animation = c?.animation ?? true;
    this.autoClose = c?.autoClose ?? true;
    this.closeDelay = c?.closeDelay ?? 0;
    // this.container = c?.container ?? ;
    this.disablePopover = c?.disablePopover ?? false;
    this.ngbPopover = c?.ngbPopover ?? undefined;
    this.popoverTitle = c?.popoverTitle ?? undefined;
    this.openDelay = c?.openDelay ?? 0;
    this.placement = c?.placement ?? NgBoostrapPlacements.auto;
    this.popoverClass = c?.popoverClass ?? 'popover-risca';
    this.triggers = c?.triggers ?? NgBoostrapTriggers.click;
    this.hidden = c?.hidden;
    this.shown = c?.shown;
    this.open = c?.open;
    this.close = c?.close;
    this.toggle = c?.toggle;
    this.isOpen = c?.isOpen;
  }
}
