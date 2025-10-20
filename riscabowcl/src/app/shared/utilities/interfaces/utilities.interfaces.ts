import { ValidatorFn } from '@angular/forms';
import { NavigationExtras } from '@angular/router';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { ComponenteDt } from '../../../core/commons/vo/componente-dt-vo';
import { ComponenteGruppo } from '../../../core/commons/vo/componente-gruppo-vo';
import { ComuneVo } from '../../../core/commons/vo/comune-vo';
import { Gruppo } from '../../../core/commons/vo/gruppo-vo';
import { IstanzaProvvedimentoVo } from '../../../core/commons/vo/istanza-provvedimento-vo';
import { NazioneVo } from '../../../core/commons/vo/nazione-vo';
import { PraticaDTVo } from '../../../core/commons/vo/pratica-vo';
import { ProvinciaCompetenzaVo } from '../../../core/commons/vo/provincia-competenza-vo';
import { ProvinciaVo } from '../../../core/commons/vo/provincia-vo';
import { RecapitoVo } from '../../../core/commons/vo/recapito-vo';
import { RicercaSoggetto } from '../../../core/commons/vo/ricerca-soggetto-vo';
import { SoggettoVo } from '../../../core/commons/vo/soggetto-vo';
import { StatoRiscossioneVo } from '../../../core/commons/vo/stati-riscossione-vo';
import { StatoElaborazioneVo } from '../../../core/commons/vo/stato-elaborazione-vo';
import { TipoAutorizzazioneVo } from '../../../core/commons/vo/tipo-autorizzazioni-vo';
import {
  ITipoElaborazioneVo,
  TipoElaborazioneVo,
} from '../../../core/commons/vo/tipo-elaborazione-vo';
import { TipoIstanzaProvvedimentoVo } from '../../../core/commons/vo/tipo-istanza-provvidemento-vo';
import { TipoRecapitoVo } from '../../../core/commons/vo/tipo-recapito-vo';
import { TipoRiscossioneVo } from '../../../core/commons/vo/tipo-riscossione-vo';
import { TipoSedeVo } from '../../../core/commons/vo/tipo-sede-vo';
import { TipoTitoloVo } from '../../../core/commons/vo/tipo-titolo-vo';
import { IUsoLeggeSpecificoVo } from '../../../core/commons/vo/uso-legge-specifico-vo';
import { IUsoLeggeVo } from '../../../core/commons/vo/uso-legge-vo';
import { VerificaPraticheStDebitoriVo } from '../../../core/commons/vo/verifica-pratiche-stati-deboitori-vo';
import { IJStepConfig } from '../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import {
  TipoNaturaGiuridica,
  TipoSoggettoVo,
} from '../../../features/ambito/models';
import { FASegmento } from '../../components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { RiscaTableDataConfig } from '../../components/risca/risca-table/utilities/risca-table.classes';
import { IRiscaTableDataConfig } from '../../components/risca/risca-table/utilities/risca-table.interfaces';
import { TipoInvio } from '../../models/contatti/tipo-invio.model';
import {
  RiscaAlertConfigs,
  RiscaButtonConfig,
  RiscaButtonCss,
  RiscaDDItemConfig,
  RiscaFormInputCss,
  RiscaFormInputEmail,
  RiscaFormInputNumber,
  RiscaFormInputText,
  RiscaIconConfigs,
  RiscaPopoverConfig,
  RiscaServerError,
  RiscaTableCss,
  RiscaTablePagination,
  RiscaTableSourceMap,
} from '../classes/utilities.classes';
import {
  NgBoostrapPlacements,
  NgBoostrapTriggers,
} from '../enums/ng-bootstrap.enums';
import {
  AbilitaDAGruppi,
  AbilitaDASezioni,
  AbilitaDASoggetti,
  AppActions,
  AppClaimants,
  AppRoutes,
  RiscaAppendixPosition,
  RiscaAzioniPratica,
  RiscaButtonTypes,
  RiscaCloningType,
  RiscaFormStatus,
  RiscaSortTypes,
  TipoInputTable,
} from '../enums/utilities.enums';
import {
  RiscaCss,
  RiscaTableBodyTabMethodType,
  TNIPFormData,
  TRiscaAlertType,
  TRiscaFormInputChoiceOrientation,
  TRiscaTableTypeSorting,
  TRTInputValidator,
} from '../types/utilities.types';
import { IndirizzoSpedizioneVo } from './../../../core/commons/vo/indirizzo-spedizione-vo';

/**
 * Interfaccia che definisce un oggetto dinamico formato da chiave: string, e valore: any.
 */
export interface DynamicObjAny {
  [key: string]: any;
}

/**
 * Interfaccia che definisce un oggetto dinamico formato da chiave: string, e valore di tipo: T.
 */
export interface DynamicObjType<T> {
  [key: string]: T;
}

/**
 * Interfaccia che definisce un oggetto dinamico formato da chiave: string, e valore: string.
 */
export interface DynamicObjString {
  [key: string]: string;
}

/**
 * Interfaccia che definisce un oggetto dinamico formato da chiave: string, e valore: boolean.
 */
export interface DynamicObjBoolean {
  [key: string]: boolean;
}

/**
 * Interfaccia che definisce la tipologia di errore che viene ritornata dal server per gli errori sulle chiamate alle API.
 * L'interfaccia va a gestire una HttpRequest in errore, definendo al suo interno un eventuale errore generato dal server.
 */
export interface IRiscaServerError extends DynamicObjAny {
  error?: IRiscaServerErrorInfo;
  errors?: IRiscaServerErrorInfo[];
}

/**
 * Interfaccia che definisce le informazioni dell'errore che viene ritornata dal server per gli errori sulle chiamate alle API.
 */
export interface IRiscaServerErrorInfo {
  status?: string;
  code?: string;
  title?: string;
  detail?: IRiscaServerErrorDetail;
  links?: string[];
}

/**
 * Interfaccia che definisce il dettaglio delle info dell'errore che viene ritornata dal server per gli errori sulle chiamate alle API.
 */
export interface IRiscaServerErrorDetail extends DynamicObjAny {}

/**
 * Interfaccia personalizzata come supporto per le classi che gestiscono modali.
 */
export interface ICallbackDataModal {
  /** Funzione che viene invocata quando il modale viene chiuso in modalità close (success). */
  onConfirm?: (...args: any) => any;
  /** Funzione che viene invocata quando il modale viene chiuso in modalità dismiss (close) e sono stati forniti parametri. */
  onClose?: (...args: any) => any;
  /** Funzione che viene invocata quando il modale viene chiuso in modalità dismiss (cancel) e non sono stati forniti parametri. */
  onCancel?: () => any;
}

/**
 * Interfaccia personalizzata come supporto per le classi che gestiscono modali.
 */
export interface ICallbackPayloadModal {
  /** Any come payload ritornato dalla funzione: onConfirm. */
  confirmPayload?: any;
  /** Any come payload ritornato dalla funzione: closePayload. */
  closePayload?: any;
  /** Any come payload ritornato dalla funzione: cancelPayload. */
  cancelPayload?: any;
}

/**
 * Interfaccia personalizzata che estende le callback per la modale e definisce una serie di parametri di configurazione comuni.
 */
export interface ICommonParamsModal extends ICallbackDataModal {
  title?: string;
  body?: string;
  buttonCancel?: string;
  buttonConfirm?: string;
  onConfirm?: (...args: any) => any;
  onClose?: (...args: any) => any;
  onCancel?: (...args: any) => any;
  confirmPayload?: any;
  closePayload?: any;
  cancelPayload?: any;
  showConfirmBtn?: boolean;
  showCloseBtn?: boolean;
  showCancelBtn?: boolean;
}

/**
 * Interfaccia che definisce tutti i dati da salvare correlati alla pratica.
 */
export interface PraticaForms {
  idComponenteDt: number;
  generaliAmministrativi: GeneraliAmministrativi;
  datiAnagrafici: DatiAnagrafici;
}

/**
 * Interfaccia che definisce tutti i dati da salvare correlati alla pratica, come input al servizio.
 */
export interface PraticaCorrelati {
  datiTecnici: PraticaDTVo;
}

/**
 * Interfaccia che definisce tutti i dati da salvare correlati alla pratica, come dati per la request.
 */
export interface PraticaCorrelatiReq {
  datiTecnici: Observable<PraticaDTVo>;
}

/**
 * Interfaccia che definisce tutti i dati da salvare correlati alla pratica, come dati per la response.
 */
export interface PraticaCorrelatiRes {
  datiTecnici: PraticaDTVo;
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form datiGenAmmForm.
 */
export interface GeneraliAmministrativi {
  tipologiaPratica: TipoRiscossioneVo;
  codiceUtenza: ProvinciaCompetenzaVo;
  tipoTributoUtenza: any;
  progressivoUtenza: string;
  numeroPratica: string;
  statoPratica: StatoRiscossioneVo;
  dataRinunciaRevoca: NgbDateStruct;
  procedimento: TipoAutorizzazioneVo;
  prenotata: IRiscaCheckboxData;
  statoDebInvioSpeciale: IRiscaCheckboxData;
  motivazione: string;
  note: string;
  dataInizioConcessione: NgbDateStruct;
  dataFineConcessione: NgbDateStruct;
  dataInizioSospensione: NgbDateStruct;
  dataFineSospensione: NgbDateStruct;
  istanzeProvvedimenti: TNIPFormData[];
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form datiAnagrafici.
 */
export interface DatiAnagrafici {
  /** SoggettoVo con i dati del soggetto associati alla pratica. */
  soggettoPratica: SoggettoVo;
  /** GruppoVo contenente i dati del grupo. */
  gruppoPratica: Gruppo;
  /** Array di RecapitoVo contenente i recapiti del soggetto associati alla pratica. */
  recapitiPratica: RecapitoVo[];
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form risca-dati-soggetto.
 */
export interface RiscaDatiSoggetto {
  tipoSoggetto: TipoSoggettoVo;
  codiceFiscale: string;
  naturaGiuridica: TipoNaturaGiuridica;
  nome: string;
  cognome: string;
  ragioneSociale: string;
  partitaIva: string;
  comuneDiNascita: ComuneVo;
  dataDiNascita: NgbDateStruct;
  statoDiNascita: NazioneVo;
  provinciaDiNascita: ProvinciaVo;
  cittaEsteraNascita: string;
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form risca-recapito.
 */
export interface RiscaRecapito {
  tipoSede: TipoSedeVo;
  presso: string;
  stato: NazioneVo;
  comune: ComuneVo;
  provincia: ProvinciaVo;
  cittaEsteraRecapito: string;
  localita: string;
  indirizzo: string;
  numeroCivico: string;
  cap: string;
  tipoRecapito: TipoRecapitoVo;
  indirizziSpedizione?: IndirizzoSpedizioneVo[];
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form risca-contatti.
 */
export interface RiscaContatto {
  tipoInvio: TipoInvio;
  pec: string;
  email: string;
  telefono: string;
  cellulare: string;
}

/**
 * Interfaccia personalizzata che rappresenta i campi della form gruppo.
 */
export interface RiscaGruppo {
  nomeGruppo: string;
  componentiGruppo: ComponenteGruppo[];
}

/**
 * Interfaccia personalizzata che definisce la struttura per l'aggiunta del nome del campo a seguito dell'estrazione degli errori.
 */
export interface MappaNomeCampo {
  /** string che definisce la label semplice d'associare ad un form control. */
  label?: string;
  /** Funzione che permette di gestire della logica custom per la visualizzazione del messaggio d'errore dinamico. Se definita insieme alla proprietà "label" questa proprietà avrà meno priorità. */
  erroreDinamico?: (e: any, msg: string) => string;
}

/**
 * Interfaccia personalizzata che definisce la struttura per il recupero dei messaggi d'errore, partendo dagli errori di una form.
 */
export interface MappaErroriFormECodici {
  /** erroreForm è la proprietà dell'oggetto errors della form. */
  erroreForm: string;
  /** erroreCodice è il codice fornitoci da CSI e presente nella documentazione degli errori. */
  erroreCodice: string;
  /** nomeCampo è un oggetto che permette d'inserire, nel messaggio d'errore, il nome del campo in errore. Valido solo per i FormControl. */
  nomeCampo?: MappaNomeCampo;
}

/**
 * Interfaccia personalizzata che definisce i parametri di configurazione per i date picker di bootstrap.
 */
export interface RiscaDatepickerConfig {
  minDate?: NgbDateStruct;
  maxDate?: NgbDateStruct;
}

/**
 * Interfaccia personalizzata che definisce il campo e il tipo di sort per le tabelle.
 */
export interface RiscaSortConfig {
  field: string;
  type: RiscaSortTypes;
}

/**
 * Interfaccia personalizzata che definisce la mappatura delle proprietà per il form: nuovaIstanzaForm.
 */
export interface NuovaIstanzaFormData {
  original: IstanzaProvvedimentoVo;
  dataIstanza: NgbDateStruct;
  noteIstanza?: string;
  numeroIstanza?: string;
  tipologiaIstanza: TipoIstanzaProvvedimentoVo;
}

/**
 * Interfaccia personalizzata che definisce la mappatura delle proprietà per il form: nuovoProvvedimentoForm.
 */
export interface NuovoProvvedimentoFormData {
  original: IstanzaProvvedimentoVo;
  dataProvvedimento: NgbDateStruct;
  noteProvvedimento?: string;
  numeroProvvedimento: string;
  tipoTitolo: TipoTitoloVo;
  tipologiaProvvedimento: TipoIstanzaProvvedimentoVo;
}

/**
 * Interfaccia di comodo che rappresenta l'oggetto di controllo per i dati generali amministrativi.
 * Verrà controllata la validità del codice utenza e del numero pratica.
 */
export interface RiscaValidaCodUtenzaNumPraticaReq {
  validCodiceUtenza: Observable<boolean>;
  validNumeroPratica: Observable<boolean>;
}

/**
 * Interfaccia di comodo che rappresenta l'oggetto di ritorno sulla verifica dei dati generali amministrativi.
 */
export interface RiscaValidaCodUtenzaNumPraticaRes {
  validCodiceUtenza: boolean;
  validNumeroPratica: boolean;
}

/** Dati request per ricerca pratica semplice */
export interface RicercaPraticaSemplice {
  codiceUtenza: string;
  numeroAvvisoPagamento: string;
  numeroPratica: string;
  codiceFiscale: string;
  ragioneSocialeOCognome: string;
  codiceAvviso: string;
}

/** Dati response per ricerca pratica semplice */
export interface IRicercaPraticaSempliceRes {
  id_riscossione: number;
  codiceUtenza: string;
  titolare: string;
  numero: string;
  procedimento: string;
  stato: StatoRiscossioneVo;
}

/**
 * Interfaccia che definisce l'oggetto che viene generato dalla componente risca-ricerca-bollettini.
 */
export interface IRicercaBollettini {
  tipo: TipoElaborazioneVo;
  stato: StatoElaborazioneVo;
  dataRichiestaInizio?: NgbDateStruct;
  dataRichiestaFine?: NgbDateStruct;
}

/**
 * Interfaccia che definisce l'oggetto che viene utilizzato per la request di ricerca delle elaborazioni dei pagamenti.
 */
export interface IRicercaElaborazioni {
  tipo: ITipoElaborazioneVo;
  stato: StatoElaborazioneVo;
  dataRichiestaInizio?: string; // String definito dalla conversione dell'oggetto Date javascript toISOString
  dataRichiestaFine?: string; // String definito dalla conversione dell'oggetto Date javascript toISOString
  codFunzionalita?: string;
}

/**
 *  Dati response per ricerca pratica avanzata
 * */
export interface IRicercaPraticaAvanzataReduced {
  id_riscossione: number;
  codiceUtenza: string;
  titolare: string;
  numero: string;
  procedimento: string;
  stato: StatoRiscossioneVo;
  comuneOperaDiPresa: string;
  corpoIdrico: string;
  nomeImpianto: string;
  total: number;
}

/**
 * Dati request per ricerca pratica avanzata, usi di legge
 */
export interface IRicercaPraticaUsiReq {
  usoDiLegge: IUsoLeggeVo;
  usoSpecifico: IUsoLeggeSpecificoVo;
  unitaDiMisura: string;
  quantitaDa: string;
  quantitaA: string;
  dataScadenzaEMASoISODa: NgbDateStruct;
  dataScadenzaEMASoISOA: NgbDateStruct;
  provinciaCompetenza: ProvinciaVo;
  corpoIdrico: string;
  comune: ComuneVo;
}

/**
 * Interfaccia di comodo che racchiude le configurazioni per la gestione delle input form dell'applicazione.
 */
export interface RiscaComponentConfig<T> {
  css: RiscaFormInputCss | RiscaTableCss;
  data: T;
  source?: any | any[];
}

/**
 * Interfaccia di comodo che definisce le proprietà per la gestione del css per i componenti risca-form-input.
 */
export interface IRiscaFormInputCss {
  customForm?: string | any;
  customFormCheck?: string;
  customInput?: string;
  customLabel?: string;
  customError?: string;
  showErrorFG?: boolean;
  showErrorFC?: boolean;
  labelColLeft?: string | any;
  labelColRight?: string | any;
  appendix?: IRiscaAppendix;
}

/**
 * Interfaccia che definisce le proprietà per la gestione dell'appendice per le input di RISCA.
 */
export interface IRiscaAppendix {
  text: string;
  position?: RiscaAppendixPosition;
}

/**
 * Interfaccia che definisce le configurazioni DATI utilizzate dal componente risca-table.
 */
export interface IRiscaTableInput {
  /** Oggetto RiscaTableSourceMap contenente le informazioni di configurazione per le colonne e i dati per le righe. */
  sourceMap: RiscaTableSourceMap[];
  /** Oggetto RiscaTableSourceMap contenente le informazioni di configurazione per i dati delle righe. */
  sourceMapSub?: RiscaTableSourceMap[];
  /** Oggetto RiscaTableSorting che definisce le regole di ordinamento della tabella. */
  sorting?: RiscaTableSorting;
  /** Oggetto  RiscaTablePagination contenente le informazioni della paginazione. */
  pagination?: RiscaTablePagination;
}

/**
 * Interfaccia di comodo che definisce le proprietà per la gestione grafica per il componente risca-table.
 */
export interface IRiscaTableGraphic {
  showContainer?: boolean;
  titleContainer?: string;
  titleCss?: string;
}

/**
 * Interfaccia di comodo che definisce le proprietà per la gestione del css per il componente risca-table.
 */
export interface IRiscaTableCss {
  table?: string | DynamicObjAny;
  thead?: string;
  thead_tr?: string;
  th?: string;
  tbody?: string;
  tbody_tr?: string;
  td?: string;

  /** Funzione che viene invocata per ogni riga della tabella e gestisce, in base ai dati passati, una stringa compatibile con NgClass che definisce stili specifici. */
  trDynamicClass?: (row: RiscaTableDataConfig<any>) => string;
}

/**
 * Interfaccia di comodo che definisce le regole generale di sorting per il componente risca-table.
 */
export interface RiscaTableSorting {
  /** Boolean che definisce il sorting quando il source cambia. Questo flag ha priorità sugli altri flag. */
  resetOnChange?: boolean;
  /** Boolean che definisce il sorting quando viene aggiunto un oggetto. */
  resetSortOnAdd?: boolean;
  /** Boolean che definisce il sorting quando viene rimosso un oggetto. */
  resetSortOnRemove?: boolean;
}

/**
 * Interfaccia di comodo che definisce la struttura dello status sort per il componente risca-table.
 */
export interface RiscaTableSortStatus {
  [key: string]: RiscaSortTypes;
}

/**
 * Interfaccia che rappresenta i dati di configurazione per RiscaTableSourceMapBody, per i dati "source".
 */
export interface RiscaTableBodySourceData {
  /** String che definisce il nome della proprietà da visualizzare. */
  property: string;
  /** TRiscaTableTypeSorting che definisce il tipo della proprietà. */
  type: TRiscaTableTypeSorting;
  /** String di prefisso da aggiungere all'output della tabella. */
  prefix?: string;
  /** String di suffisso da aggiungere all'output della tabella. */
  suffix?: string;
  /** String che definisce il formato per le date. */
  dateFormat?: string;
  /** Funzione che permette di lanciare una formttazione per la renderizzazione finale del dato. */
  outputFormat?: (v: any) => any;
  /** Number che definisce a quanti caratteri deve essere effettuato il troncamento (con ... alla fine) del dato. Il numero minimo di caratteri per l'ellipsis è 4. */
  ellipsisAt?: number;
}

/**
 * Interfaccia che rappresenta la configurazione dati per la gestione delle input all'interno della tabella.
 * La configurazione è attiva per il flag "useInput".
 */
export interface IRiscaTableBodyInputField {
  /** String che definisce il nome della proprietà da visualizzare. */
  property: string;
  /** String che definisce su quale proprietà andare a modificare l'informazione definita dall'input. */
  originalProperty: string;
  /**
   * TipoInputTable che definisce il tipo di input da gestire all'interno della tabella.
   * NOTA BENE: type, come TipoInputTable, definisce quale delle configurazioni successive verranno usate per il caricamento delle configurazioni. Quindi "type" dovrà avere lo stesso nome della proprietà da utilizzare.
   */
  type: TipoInputTable;
  /** any contente la configurazione per la gestione del campo: "text". */
  textConfig?: IRiscaTableTextConfig;
  /** any contente la configurazione per la gestione del campo: "number". */
  numberConfig?: IRiscaTableNumberConfig;
  /** any contente la configurazione per la gestione del campo: "number-it-format". */
  numberItFormatConfig?: IRiscaTableNumberConfig;
  /** any contente la configurazione per la gestione del campo: "email". */
  emailConfig?: IRiscaTableEmailConfig;

  /** (original: any, cssConfig: RiscaFormInputCss) => RiscaFormInputCss; come funzione che gestisce le logiche di visualizzazione delle appendici per le input. La funzione deve restituire un oggetto RiscaFormInputCss. */
  customAppendix?: (
    original: any,
    cssConfig: RiscaFormInputCss
  ) => RiscaFormInputCss;

  /** Funzione che definisce la logica di disabilitazione dell'azione. In input ci sarà l'oggetto RiscaTableDataConfig<any>. */
  disable?: (v?: RiscaTableDataConfig<any>, ...p: any[]) => boolean;
}

/**
 * Interfaccia che rappresenta la struttura dati per le configurazioni comuni per le input della tabella.
 */
export interface IRiscaTableInputConfig {
  /** RiscaFormInputCss con la configurazione grafica per la input. */
  cssConfig: RiscaFormInputCss;
  /** TRTInputValidator<any>[] che definisce la lista di funzioni custom di validazione per il campo. */
  customValidators?: TRTInputValidator<any>[];
  /** ValidatorFn[] che definisce la lista di validatori già impostati secondo le definizioni di Angular. */
  angularValidators?: ValidatorFn[];
  /** Lista di oggetti MappaErroriFormECodici contenente la lista degli errori che devono essere gestiti per il form control. */
  errMapFormControl?: MappaErroriFormECodici[];
  /** Lista di oggetti MappaErroriFormECodici contenente la lista degli errori che devono essere gestiti per il form control. */
  errMapFormGroup?: MappaErroriFormECodici[];
  /** Boolean che definisce se la input è disabilitata. */
  disabled?: boolean;
}

/**
 * Interfaccia che rappresenta la struttura dati per la configurazione di input nella tabella in formato: text.
 */
export interface IRiscaTableTextConfig extends IRiscaTableInputConfig {
  /** RiscaFormInputText con la configurazione dati per la input. */
  dataConfig: RiscaFormInputText;
}

/**
 * Interfaccia che rappresenta la struttura dati per la configurazione di input nella tabella in formato: number.
 */
export interface IRiscaTableNumberConfig extends IRiscaTableInputConfig {
  /** RiscaFormInputNumber con la configurazione dati per la input. */
  dataConfig: RiscaFormInputNumber;
}
/**
 * Interfaccia che rappresenta la struttura dati per la configurazione di input nella tabella in formato: email.
 */
export interface IRiscaTableEmailConfig extends IRiscaTableInputConfig {
  /** RiscaFormInputEmail con la configurazione dati per la input. */
  dataConfig: RiscaFormInputEmail;
}

/**
 * Interfaccia che rappresenta i dati di configurazione per RiscaTableSourceMapBody, per i dati "tab method".
 */
export interface RiscaTableBodyTabMethodConfig {
  /** RiscaTableBodyTabMethodData che definisce le informazioni di gestione delle logiche dei metodi della tabella. */
  actions: RiscaTableBodyTabMethodData[];
}

/**
 * Interfaccia di configurazione per la gestione specifica delle configurazioni per le checkobox della tabella.
 */
export interface RTBTMDCheckboxConfigs {
  /** Boolean che definisce se la checkbox identifica un dato specifico della riga. Se non definito, la checkbox viene identificata come checkbox di riga. */
  isRowData?: boolean;
  /** String che definisce la proprietà di configurazione dentro l'oggetto "dataTable" dove sono definiti i valori per la "colonna" della riga. */
  dataTableProperty?: string;
}

/**
 * Interfaccia che rappresenta le informazioni
 */
export interface RiscaTableBodyTabMethodData {
  /** RiscaTableBodyTabMethodType che definisce il nome della proprietà da visualizzare. */
  action: RiscaTableBodyTabMethodType | string;
  /** Boolean che definisce se la tabella deve utilizzare una customizzazione. */
  custom?: boolean;
  /** String che definisce la label dell'elemento. */
  label?: string;
  /** String che definisce il tooltip (title) dell'elemento. */
  title?: string;
  /** String che definisce la classe di stile da definire per il div container. Verrà usato nella direttiva NgClass. */
  class?: string;
  /** Any che definisce gli stili da definire per il div container. Verrà usato nella direttiva NgStyle. */
  style?: any;
  /** String che definisce la classe css per la gestione dell'icona attiva. Verrà utilizzata la funzione 'disable' per gestire l'icona. */
  iconEnabled?: string;
  /** String che definisce la classe css per la gestione dell'icona disattivata. Verrà utilizzata la funzione 'disable' per gestire l'icona. */
  iconDisabled?: string;
  /** RTBTMDCheckboxConfigs che definisce possibili configurazioni per la gestione dell'action "checbox". */
  checkboxConfigs?: RTBTMDCheckboxConfigs;
  /** Funzione che definisce la logica di disabilitazione dell'azione. In input ci sarà l'oggetto RiscaTableDataConfig<any>. */
  disable?: (v?: RiscaTableDataConfig<any>, ...p: any[]) => boolean;
  /** Funzione che definisce la logica di disabilitazione dell'azione in modalità dinamica per logiche interne. Questa funzione includerà in AND la funzione di disable, se esiste. In input ci sarà l'oggetto RiscaTableDataConfig<any>. */
  disableDynamic?: (v?: RiscaTableDataConfig<any>, ...p: any[]) => boolean;
  /** Funzione che definisce la logica di visualizzazione dell'azione. In input ci sarà l'oggetto RiscaTableDataConfig<any>. */
  visible?: (v?: RiscaTableDataConfig<any>, ...p: any[]) => boolean;
  /** Funzione che definisce la logica di selezione dell'icona per lo stato di "abilitazione". Se questa funzione è definita prende priorità sulla proprietà "iconEnabled". */
  chooseIconEnabled?: (v?: RiscaTableDataConfig<any>, ...p: any[]) => string;
  /** Funzione che definisce la logica di selezione dell'icona per lo stato di "disabilitazione". Se questa funzione è definita prende priorità sulla proprietà "iconDisabled". */
  chooseIconDisabled?: (v?: RiscaTableDataConfig<any>, ...p: any[]) => string;

  /** RiscaPopoverConfig con la configurazione per gestire l'action come "popover". */
  popover?: RiscaPopoverConfig;
}

/**
 * Interfaccia che rappresenta i dati di configurazione per RiscaTableSourceMapBody, per i dati "common icon".
 */
export interface RiscaTableBodyCommonIconData {
  /** String che definisce l'icona font awesome da visualizzare. E' possibile concatenare, separati da spazi vuoti, altre classi di stile. */
  fontAwesome?: string;
  /** String che definisce il di una classe css che contiene al suo interno la dichiarazione di un'immagine interna all'app. */
  classImage?: string;
}

/**
 * Interfaccia comune a tutte le componenti form-inputs.
 */
export interface IRiscaFormInputCommon {
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
  /** String che definisce la label dell'input, posizionata a destra come mini descrizione. */
  extraLabelSub?: string;
  /** String che definisce il placeholder da visualizzare all'interno della input. */
  placeholder?: string;
  /** Number che definisce la lunghezza massima del campo. */
  maxLength?: number;
  /** Boolean che definisce se l'input deve contenere solo numeri. */
  onlyNumber?: boolean;
  /** Boolean che definisce se l'input deve contenere anche i decimali, se "onlyNumber" è true. */
  useDecimal?: boolean;
  /** Boolean che definisce se l'input può contenere il segno (-), se "onlyNumber" è true. */
  useSign?: boolean;
  /** String che definisce il tooltip da visualizzare sulla label del componente. */
  title?: string;
  
  /** (value: any) => any; che permette di definire la logica di sanitizzazione delle input. */
  sanitizeLogic?: (value: any) => any;
}

/**
 * Interfaccia che definisce le configurazioni DATI utilizzate dai componenti form-inputs: datepicker.
 */
export interface IRiscaFormInputDatepicker extends IRiscaFormInputCommon {
  /** NgbDateStruct che definisce la data minima del calendario. */
  minDate: NgbDateStruct;
  /** NgbDateStruct che definisce la data massima del calendario. */
  maxDate: NgbDateStruct;
}

/**
 * Intefaccia che definisce le configurazioni DATI utilizzate dai componenti form-inputs: text.
 */
export interface IRiscaFormInputText extends IRiscaFormInputCommon {}

/**
 * Intefaccia che definisce le configurazioni DATI utilizzate dai componenti form-inputs: email.
 */
export interface IRiscaFormInputEmail extends IRiscaFormInputCommon {}

/**
 * Intefaccia che definisce le configurazioni DATI utilizzate dai componenti form-inputs: typeahead.
 */
export interface IRiscaFormInputTypeahead extends IRiscaFormInputCommon {
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
}

/**
 * Intefaccia che definisce le configurazioni DATI utilizzate dai componenti form-inputs: select.
 */
export interface IRiscaFormInputSelect extends IRiscaFormInputCommon {
  /** Boolean che definisce se la selecte deve comportarsi come una multi-select. */
  multiple: boolean;
  /** Boolean che definisce se aggiungere un elemento vuoto all'interno della select. */
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
}

/**
 * Intefaccia che definisce le configurazioni DATI utilizzate dai componenti form-inputs: number.
 */
export interface IRiscaFormInputNumber extends IRiscaFormInputCommon {
  /** Number che definisce il minimo impostabile per la input. */
  min?: number;
  /** Number che definisce il massimo impostabile per la input. */
  max?: number;
  /** Number che definisce il massimo impostabile per la input. */
  step?: number;
  /** Number che definisce il numero di decimali da gestire per la input. */
  decimals?: number;
  /** boolean che permette di mantenere i decimali 0 (non significativi) se la quantità di decimali non raggiungesse il numero di decimali impostati. */
  decimaliNonSignificativi?: boolean;
  /** boolean che permette di attivare il separatore delle migliaia. */
  allowThousandsSeparator?: boolean;
}

/**
 * Intefaccia che definisce le configurazioni DATI utilizzate dai componenti form-inputs: selezione (checkbox, radio).
 */
export interface IRiscaFormInputChoice extends IRiscaFormInputCommon {
  /** String che definisce l'orientamento dei selettori. */
  orientation?: TRiscaFormInputChoiceOrientation;
}

/**
 * Interfaccia impiegata per la costruzione delle nav-bar.
 */
export interface IRiscaNavItem<T> {
  ngbNavItem: T;
  label: string;
  component: string;
  disabled?: boolean;
  default?: boolean;
}

/**
 * Interfaccia che definisce le configurazioni per l'azione "checkbox" per IRiscaTableSMHActions.
 */
export interface IRiscaTableSMHACheckbox {
  selected?: boolean;
  cssInput?: string | any;
  disable?: (v?: any) => boolean;
}

/**
 * Interfaccia che definisce le configurazioni per le azioni impostabili come header per IRiscaTableSourceMapHeader.
 */
export interface IRiscaTableSMHActions {
  checkbox?: IRiscaTableSMHACheckbox;
}

/**
 * Interfaccia impiegata per la classe RiscaTableSourceMapHeader come parametro del costrutture.
 */
export interface IRiscaTableSourceMapHeader {
  label?: string;
  title?: string;
  sortable?: boolean;
  sortType?: RiscaSortTypes;
  cssCell?: string | any;
  cssLabel?: string | any;
  action?: IRiscaTableSMHActions;
  colspan?: boolean;
}

/**
 * Interfaccia impiegata per la classe RiscaTableSourceMapBody come parametro del costrutture.
 */
export interface IRiscaTableSourceMapBody {
  useSource?: boolean;
  useInput?: boolean;
  useTabMethod?: boolean;
  useCommonIcon?: boolean;
  sourceData?: RiscaTableBodySourceData;
  inputData?: IRiscaTableBodyInputField;
  tabMethodData?: RiscaTableBodyTabMethodConfig;
  commonIconData?: RiscaTableBodyCommonIconData;
  cssCell?: string | any;
  cssLabel?: string | any;
  cssCustom?: (row: RiscaTableDataConfig<any>) => RiscaCss;
  title?: string;
  titleCustom?: (data: any) => string;
}

/**
 * Interfaccia impiegata per la classe RiscaTablePagination come parametro del costrutture.
 */
export interface IRiscaTablePagination {
  total?: number;
  showTotal?: boolean;
  label?: string;
  elementsForPage?: number;
  currentPage?: number;
  sortBy?: string;
  sortDirection?: RiscaSortTypes;
  maxPages?: number;
}

/**
 * Interfaccia di comodo che definisce le informazioni necessarie per ottenere lo stato di un'abilitazione sui dati anagrafici.
 */
export interface IAbilitazioniDA {
  sezione: AbilitaDASezioni;
  abilitazione: AbilitaDASoggetti | AbilitaDAGruppi;
}

/**
 * Interfaccia che viene utilizzata per definire la configurazione della funzione di un FormControl: updateValueAndValidity.
 * Maggiori dettagli al link:
 * @link https://angular.io/api/forms/AbstractControl#updateValueAndValidity
 */
export interface FormUpdatePropagation {
  onlySelf?: boolean;
  emitEvent?: boolean;
}

/**
 * Interfaccia che definisce il tipo di oggetto accettato dai componenti di selezione mediante checkbox e radio.
 */
export interface IRiscaChoiceData extends DynamicObjAny {
  id: string;
  value: any;
  label?: string;
  disabled?: boolean;
}

/**
 * Interfaccia che definisce il tipo di oggetto accettato dal componente: risca-checkbox; come source.
 */
export interface IRiscaCheckboxData extends IRiscaChoiceData {
  /** Boolean che definisce lo stato della checkbox. A differenza della proprietà "value", che è un'informazione settata e rimane quella, il check viene modificato in base al click dell'operatore sulla checkbox. */
  check: boolean;
}

/**
 * Interfaccia che definisce il tipo di oggetto accettato dal componente: risca-radio; come source.
 */
export interface IRiscaRadioData extends IRiscaChoiceData {}

/**
 * Interfaccia che permette la configurazione del cloning di un oggetto.
 */
export interface IRiscaCloneConfig {
  cloningType: RiscaCloningType;
}

/**
 * Interfaccia che definisce i parametri comuni per il routing per la gestione degli stati di snapshot.
 */
export interface ICommonRouteParams {
  /** Definisce la modalità dei componenti. */
  modalita?: AppActions;
}

/**
 * Interfaccia di comodo che definisce i parametri di configurazione per risca-alert.
 */
export interface IAlertConfigs {
  messaggi?: string[];
  tipo?: TRiscaAlertType;
  permanent?: boolean;
}

/**
 * Interfaccia utilizzata per il ritorno delle informazioni trovate tramite ricerca.
 */
export interface IFormCercaSoggetto {
  tipoSoggetto?: TipoSoggettoVo;
  codiceFiscale?: string;
  ricercaSoggetto?: RicercaSoggetto;
  gruppoSelezionato?: Gruppo;
}

/**
 * Interfaccia utilizzata per il ritorno delle informazioni trovate tramite ricerca.
 */
export interface ITitolareModalRes extends IFormCercaSoggetto {
  gruppo?: Gruppo;
}

/**
 * Interfaccia che definisce le regole per la gestione specifica di un qualsiasi valore
 */
export interface IParseValueRules {
  /** Controllo che verifica se un oggetto è vuoto, deve essere convertito a null. */
  emptyObjToNull?: boolean;
  /** Controllo che va a sanitificare tutte le proprietà front end all'interno dell'oggetto. */
  sanitizeFEProperties?: boolean;
}

/**
 * Interfaccia che definisce le regole di gestione dei parametri per la composizione di query params.
 */
export interface IRulesQueryParams {
  allowNull: boolean;
  allowUndefined: boolean;
  allowEmptyString: boolean;
}

/**
 * Interfaccia che definisce le configurazioni per gli stili dei risca-button
 */
export interface IRiscaButtonCss {
  /**
   * Per la gestione degli stili utilizzare questa struttura
   * [ngClass]="cssConfig.customButton | riscaCssHandler:C_C.CSS_TYPE_CLASS"
   * [ngStyle]="cssConfig.customButton | riscaCssHandler:C_C.CSS_TYPE_STYLE"
   */
  customButton?: string | any;
  /** Tipo dichiarativo derivato da RiscaButtonTypes. */
  typeButton?: RiscaButtonTypes;
}

/**
 * Interfaccia che definisce le configurazioni per il comportamento dei risca-button
 */
export interface IRiscaButtonConfig {
  /** String che definisce la label del pulsante. */
  label: string;
  /** String che definisce il codice di accesso all'elemento applicativo, se definito attivà passivamente la direttiva di gestione AEA. */
  codeAEA?: string;
}

/**
 * Interfaccia che definisce la struttura completa di configurazione per i comportamenti grafici e dati per il componente risca-button.
 */
export interface IRiscaButtonAllConfig {
  cssConfig: RiscaButtonCss;
  dataConfig: RiscaButtonConfig;
}

/**
 * Interfaccia che definisce le configurazioni per gli stili dei risca-button
 */
export interface IRiscaDDButtonCss extends IRiscaButtonCss {
  customItems?: RiscaCss;
}

/**
 * Interfaccia che definisce le configurazioni per il comportamento dei risca-dropdown-button
 */
export interface IRiscaDDButtonConfig extends IRiscaButtonConfig {
  dropdownItems: RiscaDDItemConfig[];
}

/**
 * Interfaccia che definisce le configurazioni per la lista dei pulsanti per il componente risca-dropdown-button.
 */
export interface IRiscaDDItemConfig {
  id: string;
  label: string;
  disabled?: boolean;
  title?: string;
  data?: any;
  codeAEA?: string;
  itemCss?: RiscaCss;
  useIcon?: boolean;
  useIconLeft?: boolean;
  useIconRight?: boolean;
  icon?: RiscaIconConfigs;
  iconLeft?: RiscaIconConfigs;
  iconRight?: RiscaIconConfigs;
}

/**
 * Interfaccia che definisce la configurazione per la verifica formale di validità e ritorno del componente dt da caricare per la parte inerente ai dati tecnici.
 */
export interface IVerifyComponeteDt {
  componentiLocali: DynamicObjAny;
  componenteDt: ComponenteDt;
  prefissoCDt?: string;
  suffissoCDt?: string;
}

/**
 * Interfaccia che definisce la configurazione di input per la gestione dell'alert che visualizza i dati dei campi soggetto aggiornati dalla fonte.
 */
export interface IAlertCAFConfigs {
  listaCampiAggiornati: string[];
  alertConfigs: RiscaAlertConfigs;
}

/**
 * Interfaccia che rappresenta la configurazione dell'alert.
 */
export interface IRiscaAlertConfigs {
  /** string che definisce la tipologia di messaggio da gestire. */
  type?: TRiscaAlertType;
  /** string o array string che definisce i messaggi da visualizzare. */
  messages?: string | string[];
  /** boolean che definisce se il pannello di alert è persistente. */
  persistentMessage?: boolean;
  /** number che definisce dopo quanto nascondere il panel (in millisecondi). */
  timeoutMessage?: number;
  /** boolean che definisce se usare il layout compatto. */
  compactLayout?: boolean;
  /** string compatibile con le classi css, o oggetto compatibile con la direttiva NgStyle che definisce lo stile del container. */
  containerCss?: string | any;
  /** IRiscaButtonAllConfig che definisce le configurazioni per il pulsante di "conferma" dell'alert. */
  buttonConfirm?: IRiscaButtonAllConfig;
  /** IRiscaButtonAllConfig che definisce le configurazioni per il pulsante di "annulla" dell'alert. */
  buttonCancel?: IRiscaButtonAllConfig;
  /** boolean che definisce se mostrare o meno il pulsante X in alto a destra. */
  allowAlertClose?: boolean;
  /** any che definisce le informazioni per il pulsante di "conferma", nel momento della sua pressione. */
  payloadConfirm?: any;
  /** any che definisce le informazioni per il pulsante di "annulla", nel momento della sua pressione. */
  payloadCancel?: any;
}

/**
 * Interfaccia personalizzata che gestisce il "check" di una form.
 */
export interface IRiscaFormCheck {
  /** Data conterrà due possibili tipi d'informazioni: l'oggetto dati della form oppure un array di stringhe con la lista degli errori sulla form. */
  data: any | string[];
  /** String che definisce il componente padre a cui fa riferimento la prorietà "child". */
  parent: string;
  /** String che definisce il componente relativo a "data". Questo campo verrà valorizzato con la chiave definita all'interno della mappa _formStatus. */
  child: string;
  /** Stato di validazione della form. */
  status: RiscaFormStatus;
}

/**
 * Interfaccia personalizzata che gestisce lo stato di validazione di un gruppo di form, identificando il livello: padre.
 */
export interface IRiscaFormTreeParent {
  /** La proprietà di primo livello dell'oggetto rappresenta la chiave del componente padre. */
  [key: string]: IRiscaFormTreeChildren;
}

/**
 * Interfaccia personalizzata che gestisce lo stato di validazione di un gruppo di form, identificando il livello: figlio.
 */
export interface IRiscaFormTreeChildren {
  /** La proprietà di primo livello dell'oggetto rappresenta la chiave dei componenti figli. */
  [key: string]: IRiscaFormCheck;
}

/**
 * Interfaccia che definisce le configurazioni della richiesta di navigazione delle forms figlie al padre.
 */
export interface IRFCReqJourneyNavigation {
  /** string che definisce l'id del RiscaFormChild che ha richiesta la snapshot. */
  childFormKey?: string;
  /** AppRoutes che definisce la rotta per la navigazione da gestire al padre. */
  route: AppRoutes;
  /** NavigationExtras che definisce le informazioni da passare alla navigazione di Angular. */
  extras?: NavigationExtras;
  /** AppClaimants che definisce nello specifico quale parte del componente richiede la navigazione. */
  claimant?: AppClaimants;
  /** Boolean che definisce se la rotta da richiamare è la stessa del componente che ne fa richiesta. */
  sameRoute?: boolean;
  /** JourneyStep che definisce la configurazione dello step successivo. */
  jStepTarget?: IJStepConfig;
  /** FASegmento che permette di configurare un oggetto d'aggiungere al filo d'arianna. */
  segmentoFA?: FASegmento;
}

/**
 * Interfaccia che definisce le configurazioni per gestire le informazioni sulla funzione: onFormErrors.
 */
export interface IRFCFormErrorsConfigs {
  formErrors?: MappaErroriFormECodici[];
  serverError?: RiscaServerError;
}

/**
 * Interfaccia che definisce il fallback per la navigazione quando si tenta di accedere ad una nav bloccata.
 */
export interface IRiscaTabFallback<T> {
  navTarget: T;
  state: DynamicObjAny;
}

/**
 * Interfaccia che definisce la mappatura dei parametri di route gestiti dal componente per risca-nav-helper.
 */
export interface IRiscaNavRouteParams<T> extends ICommonRouteParams {
  navTarget?: T;
}

/**
 * Interfaccia che definisce la mappatura dei parametri di route gestiti dal componente pratica.
 */
export interface IPraticaRouteParams
  extends IRiscaNavRouteParams<RiscaAzioniPratica> {
  pageTarget?: RiscaAzioniPratica;
}

/**
 * Interfaccia di supporto che specifica le classi di stile da utilizzare a seconda della posizione del nav.
 */
export interface IRiscaNavLinkConfig {
  first: string;
  center: string;
  last: string;
}

/**
 * Interfaccia che rappresenta le configurazioni per il componente risca-icon.
 */
export interface IRiscaIconConfigs {
  /** Array di string che definisce le classi da utilizzare. */
  classes?: string[];
  /** String che definisce l'url della risorsa dell'icona. */
  icon?: string;
  /** Any che definisce gli css per l'icona, deve essere compatibile con NgStyle. */
  iconStyles?: RiscaCss;
  /** String che definisce il title dell'icona. */
  iconTitle?: string;
  /** Boolean che definisce se l'icona è disabilitata. */
  disabled?: boolean;
  /** Boolean che definisce se utilizzare la classe di default dell'icona. */
  useDefault?: boolean;
  /** Boolean che definisce se attivare il click dell'icona. */
  enableClick?: boolean;
}

/**
 * Interfaccia per la gestione dei dati relativi alle select per la selezione anni
 */
export interface IRiscaAnnoSelect {
  anno: number;
}

/**
 * Interfaccia che definisce la struttura delle informazioni della verifica pratiche e stati debitori, per l'utilizzo nei messaggi all'utente.
 */
export interface VerificaPStDInfo {
  label: string;
  quantita: number;
}

/**
 * Interfaccia che rappresenta le informazioni sullo stato di verifica pratiche e stati debitori.
 */
export interface IResultVerifyRStDDettaglio {
  isObjCollegato: boolean;
  dettaglio: VerificaPraticheStDebitoriVo;
}

/**
 * Interfaccia di comodo che definisce il risultato generato dalla funzione di check per i validatori.
 */
export interface ICheckValidators {
  valid: boolean;
  error?: string;
}

/**
 * Interfaccia di comodo che definisce la struttura dell'oggetto passato dall'evento di cambio tab per il cambio navigazione.
 */
export interface IRiscaTabChanges {
  prev?: any;
  actual?: any;
  next?: any;
}

/**
 * Interfaccia di configurazione che mappa le configurazioni per la gestione per Angular Bootstrap Popover.
 * @see https://ng-bootstrap.github.io/releases/11.x/#/components/popover/api
 * @tutorial https://ng-bootstrap.github.io/releases/11.x/#/components/popover/examples
 */
export interface IRiscaPopoverConfig {
  // INPUTS
  animation?: boolean;
  autoClose?: true | false | 'inside' | 'outside';
  closeDelay?: number;
  // container?: string;
  disablePopover?: boolean;
  ngbPopover: (e: any) => string; // string | TemplateRef<any> | null | undefined;
  popoverTitle?: (e: any) => string | undefined; // string | TemplateRef<any> | null | undefined;
  openDelay?: number;
  placement?: NgBoostrapPlacements;
  popoverClass?: string;
  triggers?: NgBoostrapTriggers;
  // OUTPUTS
  hidden?: () => any;
  shown?: () => any;
  // METHODS
  open?: (context: any) => void;
  close?: () => void;
  toggle?: () => void;
  isOpen?: () => boolean;
}

/**
 * Interfaccia che definisce le proprietà passate alle funzioni di controllo custom per le input-table.
 */
export interface IRTInputValidatorParams<T> {
  value: T;
  row: IRiscaTableDataConfig;
  inputConfig: IRiscaTableBodyInputField;
}
