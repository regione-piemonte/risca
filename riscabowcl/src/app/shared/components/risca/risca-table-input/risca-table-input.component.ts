import { Component, Input, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
} from '@angular/forms';
import { clone } from 'lodash';
import { Subscription } from 'rxjs/index';
import { RiscaTableService } from '../../../services/risca/risca-table/risca-table.service';
import { ITableEnableChanges } from '../../../services/risca/risca-table/utilities/risca-table.interfaces';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  FormUpdatePropagation,
  IRiscaTableBodyInputField,
  IRiscaTableEmailConfig,
  IRiscaTableNumberConfig,
  IRiscaTableTextConfig,
  MappaErroriFormECodici,
  RiscaFormInputCss,
  RiscaFormInputEmail,
  RiscaFormInputNumber,
  RiscaFormInputText,
  TipoInputTable,
  TRTInputValidator,
  IRTInputValidatorParams,
} from '../../../utilities';
import { RiscaTableDataConfig } from '../risca-table/utilities/risca-table.classes';

/**
 * Type personalizzato per la gestione dei tipo di input.
 */
type TRiscaTipoInput =
  | IRiscaTableTextConfig
  | IRiscaTableNumberConfig
  | IRiscaTableEmailConfig;

type TRiscaConfiguraInput =
  | RiscaFormInputText
  | RiscaFormInputNumber
  | RiscaFormInputEmail;

/**
 * Componente che gestisce le input per la tabella risca.
 */
@Component({
  selector: 'risca-table-input',
  templateUrl: './risca-table-input.component.html',
  styleUrls: ['./risca-table-input.component.scss'],
})
export class RiscaTableInputComponent implements OnInit {
  /** Input IRiscaTableBodyInputField che definisce le configurazioni per la gestione del campo della tabella. */
  @Input() inputConfig: IRiscaTableBodyInputField;
  /** Input  che definisce le informazioni della riga della tabella. */
  @Input() row: RiscaTableDataConfig<any>;
  /** Input string che definisce l'id della tabella a cui è associato l'input. */
  @Input() tableId: string;

  /** Subscription che permette di rimanere in ascolto dell'evento di abilitazione/disabilitazione delle input della tabella. */
  toggleEnableInputs$: Subscription;
  /** Subscription che permette di rimanere in ascolto dell'evento di reset delle informazioni delle input di una tabella. */
  resetInputsValues$: Subscription;
  /** Subscription che permette di rimanere in ascolto dell'evento di tutte le righe di tabella submittate. */
  allRowsSubmitted$: Subscription;
  /** Subscription che permette di rimanere in ascolto dell'evento di tutte le righe di tabella non submittate. */
  allRowsUnsubmitted$: Subscription;

  /** boolean per gestire il caricamento di tutte le componenti. */
  showForm: boolean = false;

  /** FormGroup che gestisce la logica per il campo. */
  formGroup: FormGroup;
  /** Boolean che tiene traccia della submit del form. */
  formGroupSubmitted: boolean = false;
  /** String che definisce il nome del campo del form per la cella della tabella. Verrà impostato dinamicamente tramite configurazione. */
  formControlName: string;
  /** String che definisce l'id campo del form per la cella della tabella. Verrà impostato dinamicamente tramite configurazione. */
  idFormControl: string;

  /**
   * RiscaFormInputCss con la configurazione cssConfig generata dalle funzioni di customizzazioni.
   * Essendo che la normale configurazione RiscaFormInputCss viene applicata a tutte le righe, qualora ci fosse l'esecuzione di un funzione di personalizzazione custom,
   * questo oggetto verrà valorizzat e utilizzato al posto di quella comune.
   */
  cssConfigCustom: RiscaFormInputCss;

  /**
   * ################
   * SETUP COMPONENTE
   * ################
   */

  /**
   * Costruttore.
   */
  constructor(
    private _formBuilder: FormBuilder,
    private _riscaTable: RiscaTableService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Lancio il setup del componente
    this.setupComponente();
  }

  /**
   * Funzione che lancia i setup necessari alla gestione del componente.
   */
  private setupComponente() {
    // Lancio i setup dei listeners
    this.setupListeners();
  }

  /**
   * Funzione che effettua il setup dei listeners del componente.
   * I listeners sono collegati ai servizi di gestione della tabella.
   */
  private setupListeners() {
    // Aggancio l'evento per la gestione dell'input
    this.toggleEnableInputs$ = this._riscaTable.toggleEnableInputs$.subscribe({
      next: (updateChanges: ITableEnableChanges) => {
        // Lancio la funzione di aggiornamento dell'abilitazione dell'input
        this.onUpdateEnable(updateChanges);
        // #
      },
    });

    // Aggancio l'evento per la gestione dell'input
    this.resetInputsValues$ = this._riscaTable.resetInputsValues$.subscribe({
      next: (tableId: string) => {
        // Lancio la funzione di aggiornamento del dato
        this.onResetValue(tableId);
        // #
      },
    });

    // Aggancio l'evento per la gestione dell'input
    this.allRowsSubmitted$ = this._riscaTable.allRowsSubmitted$.subscribe({
      next: (tableId: string) => {
        // Lancio la funzione di aggiornamento del dato
        this.onHandleSubmitRows(tableId, true);
        // #
      },
    });

    // Aggancio l'evento per la gestione dell'input
    this.allRowsUnsubmitted$ = this._riscaTable.allRowsUnsubmitted$.subscribe({
      next: (tableId: string) => {
        // Lancio la funzione di aggiornamento del dato
        this.onHandleSubmitRows(tableId, false);
        // #
      },
    });
  }

  /**
   * ###################
   * INIT DEL COMPONENTE
   * ###################
   */

  /**
   * ngOnInit.
   */
  ngOnInit() {
    // Lancio la funzione di init del componente
    this.initComponente();
  }

  /**
   * ngAfterViewInit.
   */
  ngAfterViewInit() {
    // La gestione di alcune configurazioni crea problemi, creo un flag di attivazione dell'intera sezione con un timeout per non far si che le logiche si pestino i piedi a vicenda
    setTimeout(() => {
      // Visualizzo la pagina
      this.showForm = true;
    });
  }

  /**
   * Funzione di init che gestisce le sotto funzioni di inizializzazione del componente.
   */
  private initComponente() {
    // Lancio l'init dei dati per il form
    this.initDatiForm();
    // Lancio l'init del form
    this.initForm();
    // Lancio l'init degli ascoltatori del form
    this.initFormListeners();
    // Lancio l'init per tutte le funzioni custom di gestione delle configurazioni dell'input
    this.initInputCustomFns();
  }

  /**
   * Funzione che effettua l'init dei dati necessari al form.
   */
  private initDatiForm() {
    // Estraggo dalla configurazione il nome del campo d'aggiornare
    const originalProperty = this.originalProperty;
    // Creo un id randomico per poter rendere univoco il nome della proprietà
    const id = this._riscaUtilities.generateRandomId();
    // Creo una chiave univoca d'assegnare ai dati del form control
    const controlId = `${originalProperty}_${id}`;
    // Assegno localmente l'informazione
    this.formControlName = controlId;
    this.idFormControl = controlId;
  }

  /**
   * Funzione che effettua l'init della form.
   */
  private initForm() {
    // Recupero il possibile valore all'interno dell'oggetto original
    const valore: any = this.originalValue;
    // Recupero la configurazione per la gestione della disabilitazione del campo
    const isDisabled: boolean = this.isInputDisabled;
    // Recupero i validatori per il form control
    const validatori: ValidatorFn[] = this.inputValidators;
    // Recupero il form control name per la generazione del form
    const fcn: string = this.formControlName;

    // Genero il form control per l'input
    const formControls: any = {};
    formControls[fcn] = new FormControl(
      { value: valore, disabled: isDisabled },
      { validators: validatori }
    );

    // Genero il form group, passando la configurazione del campo
    this.formGroup = this._formBuilder.group(formControls);
  }

  /**
   * Funzione che imposta gli ascoltatori per gli eventi del form.
   */
  private initFormListeners() {
    // Al blur del dato, aggiorno l'informazione nell'oggetto della riga
    this.formGroup
      .get(this.formControlName)
      .valueChanges.subscribe((value: any) => {
        // Aggiorno il valore all'interno dell'oggetto originale
        this.originalValue = value;
      });
  }

  /**
   * Funzione di init per tutte le funzioni custom che possono essere definite per l'aggiornamento delle configurazioni della input.
   * Queste funzioni vengono eseguite perché si ha il "contesto" di un singolo oggetto (row), dalla quale si possono gestire delle logiche per specifica riga.
   */
  private initInputCustomFns() {
    // Recupero le informazioni
    let inputConfig: IRiscaTableBodyInputField;
    inputConfig = this.inputConfig;
    let row: RiscaTableDataConfig<any>;
    row = this.row;

    // Richiamo le funzioni di gestione specifiche
    this.initAppendix(inputConfig, row);
  }

  /**
   * Funzione di gestione specifica della funzione di customizzazione dati: customAppendix.
   * @param inputConfig IRiscaTableBodyInputField con le informazioni di configurazione dell'input.
   * @param row RiscaTableDataConfig<any> con i dati della riga passati in input.
   */
  private initAppendix(
    inputConfig: IRiscaTableBodyInputField,
    row: RiscaTableDataConfig<any>
  ) {
    // Cerco di estrarre le informazioni dall'input
    // # FUNZIONE CUSTOM
    let customAppendix: (
      original: any,
      cssConfig: RiscaFormInputCss
    ) => RiscaFormInputCss;
    customAppendix = inputConfig?.customAppendix;
    // # DATO ORIGINALE
    let original: any = row?.original;
    // # CONFIGURAZIONE CSS DEFAULT
    let cssConfig: RiscaFormInputCss;
    cssConfig = clone(this.cssConfig);

    // Verifico che esistano i dati minimi
    if (!customAppendix) {
      // Manca la funzione
      return;
    }

    // Esiste la funzione, la eseguo passando le informazioni e assegnandola al cssConfig
    this.cssConfigCustom = customAppendix(original, cssConfig);
  }

  /**
   * ##############################
   * FUNZIONI COLLEGATE AGLI EVENTI
   * ##############################
   */

  /**
   * Funzione che verifica e aggiorna lo stato di abilitazione di una input a seguito dell'intercettazione di un evento.
   * @param updateChanges ITableEnableChanges con i dati per l'aggiornamento.
   */
  private onUpdateEnable(updateChanges: ITableEnableChanges) {
    // Verifico l'input
    if (!updateChanges) {
      // Niente configurazione
      return;
    }

    // Estraggo dall'input le informazioni
    const { enable, tableId } = updateChanges;
    // Verifico se l'id tabella del componente e quello passato nell'evento sono gli stessi
    if (this._riscaTable.sameTable(this.tableId, tableId)) {
      // Stesso id, vado a modificare l'abilitazione della input
      this.enableInput = enable;
      // #
    }
  }

  /**
   * Funzione che verifica e resetta il valore del form della input a seguito dell'intercettazione di un evento.
   * @param tableId string con l'id della tabella per eseguire il reset del dato.
   */
  private onResetValue(tableId: string) {
    // Verifico l'input
    if (!tableId) {
      // Niente configurazione
      return;
    }

    // Verifico se l'id tabella del componente e quello passato nell'evento sono gli stessi
    if (this._riscaTable.sameTable(this.tableId, tableId)) {
      // Resetto il valore della input
      this.inputValue = null;
      // #
    }
  }

  /**
   * Funzione che verifica e gestisce la variabile di submit per le righe della tabella.
   * @param tableId string con l'id della tabella per eseguire il reset del dato.
   * @param submit boolean con il flag che definisce se attivare o disattivare il form submitted.
   */
  private onHandleSubmitRows(tableId: string, submit: boolean) {
    // Verifico l'input
    if (!tableId) {
      // Niente configurazione
      return;
    }

    // Verifico se l'id tabella del componente e quello passato nell'evento sono gli stessi
    if (this._riscaTable.sameTable(this.tableId, tableId)) {
      // Resetto il valore della input
      this.formGroupSubmitted = submit ?? false;
      // #
    }
  }

  /**
   * ################################
   * FUNZIO DI UTILITY DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione che genera una funzione ValidatorFn compatibile con gli oggetto FormControl.
   * @param customValidator TRTInputValidator<any> con le logiche custom che verranno eseguite come controllo.
   * @param row RiscaTableDataConfig<any> con le informazioni della riga di tabella.
   * @returns ValidatorFn con le logiche di validazione dell'input.
   */
  private inputValidatorFC(
    customValidator: TRTInputValidator<any>,
    row: RiscaTableDataConfig<any>
  ): ValidatorFn {
    // Definisco un default per la gestione degli errori
    const defaultValidator: TRTInputValidator<any> = (param: any) => null;
    // Verifico e imposto la funzione di validazione
    const validator = customValidator ?? defaultValidator;

    // Definisco una funzione validatore localmente, a cui passo le informazioni del component
    const validatorField = (
      formControl: FormControl
    ): ValidationErrors | null => {
      // Verifico che il formControl esista
      if (!formControl) {
        // Oggetto del form control non presente
        return null;
      }
      // Recupero le informazioni da passare come paramentri
      const value = formControl.value;
      // Definisco l'oggetto con i parametri
      let params: IRTInputValidatorParams<any>;
      params = {
        value,
        row,
        inputConfig: this.inputConfig,
      }
      // Lancio la funzione di validazione
      return validator(params);
    };

    // Ritorno la funzione di validazione del campo
    return validatorField;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che ritorna il nome della proprietà, all'interno dell'oggetto "original" della riga.
   * @returns string con il nome del campo a cui viene associata l'informazione della input.
   */
  get originalProperty(): string {
    // Recupero dall'input il nome della proprietà
    const originalProperty: string = this.inputConfig?.originalProperty;
    // Verifico e ritorno il tipo
    return originalProperty;
  }

  /**
   * Getter che ritorna il valore da gestire che si trova all'interno dell'oggetto "original" della riga.
   * @returns any con il valore contenuto all'interno dell'oggetto dati.
   */
  get originalValue(): any {
    // Recupero dall'input il nome della proprietà
    const originalProperty: string = this.originalProperty;
    // Recupero dalla riga il valore da gestire
    const originalValue: any = this.row?.original[originalProperty];
    // Verifico e ritorno il tipo
    return originalValue ?? null;
  }

  /**
   * Setter che imposta il valore che si trova all'interno dell'oggetto "original" della riga.
   * @param any con il valore d'aggiornare all'interno dell'oggetto dati.
   */
  set originalValue(value: any) {
    // Recupero dall'input il nome della proprietà
    const originalProperty: string = this.originalProperty;

    // Verifico che ci siano tutti i livelli dell'oggetto
    if (this.row?.original) {
      // Imposto il valore
      this.row.original[originalProperty] = value;
    }
  }

  /**
   * Getter che ritorna il check sulla configurazione per il tipo di input.
   * @returns boolean con il risultato del check.
   */
  get isInputText(): boolean {
    // Recupero il tipo dall'input
    const type: TipoInputTable = this.inputConfig?.type;
    // Verifico e ritorno il tipo
    return type === TipoInputTable.text;
  }

  /**
   * Getter che ritorna il check sulla configurazione per il tipo di input.
   * @returns boolean con il risultato del check.
   */
  get isInputNumber(): boolean {
    // Recupero il tipo dall'input
    const type: TipoInputTable = this.inputConfig?.type;
    // Verifico e ritorno il tipo
    return type === TipoInputTable.number;
  }

  /**
   * Getter che ritorna il check sulla configurazione per il tipo di input.
   * @returns boolean con il risultato del check.
   */
  get isInputNumberItFormat(): boolean {
    // Recupero il tipo dall'input
    const type: TipoInputTable = this.inputConfig?.type;
    // Verifico e ritorno il tipo
    return type === TipoInputTable.numberItFormat;
  }

  /**
   * Getter che ritorna il check sulla configurazione per il tipo di input.
   * @returns boolean con il risultato del check.
   */
  get isInputEmail(): boolean {
    // Recupero il tipo dall'input
    const type: TipoInputTable = this.inputConfig?.type;
    // Verifico e ritorno il tipo
    return type === TipoInputTable.email;
  }

  /**
   * Getter che ritorna il flag che definisce se la input è da disabilitare.
   * @returns boolean con il valore del check.
   */
  get isInputDisabled(): boolean {
    // Recupero la configurazione dell'input
    const config: TRiscaTipoInput = this.inputTypeConfig;
    // Verifico e ritorno il tipo
    return config?.disabled ?? false;
  }

  /**
   * Getter che ritorna i validatori per il campo.
   * @returns ValidatorFn[] con i validatori del campo.
   */
  get inputValidators(): ValidatorFn[] {
    // Recupero la configurazione dell'input
    const config: TRiscaTipoInput = this.inputTypeConfig;
    // Recupero dalla configurazione i validatori custom e di angular
    const customVal: TRTInputValidator<any>[] = config?.customValidators ?? [];
    const angularVal: ValidatorFn[] = config?.angularValidators ?? [];

    // Converto i validatori custom in validatori compatibili con la struttura angular
    let convertedVal: ValidatorFn[];
    convertedVal = customVal.map((cv: TRTInputValidator<any>) => {
      // Eseguo la conversione mediante funzione di support
      return this.inputValidatorFC(cv, this.row);
    });

    // Mergio in un unico array tutti i validatori
    const validators: ValidatorFn[] = [...convertedVal, ...angularVal];

    // Ritorno i validatori per il campo
    return validators;
  }

  /**
   * Getter per la configurazione CSS dell'input.
   * @returns RiscaFormInputCss con la configurazione.
   */
  get cssConfigDefault(): RiscaFormInputCss {
    // Recupero la tipologia dell'oggetto
    const type: TipoInputTable = this.inputConfig?.type;
    // Il type è configurato in modo da poter estrarre l'oggetto giusto dalla configurazione
    let inputConfig: TRiscaTipoInput;
    inputConfig = this.inputConfig[type];
    // Ritorno la configurazione grafica
    return inputConfig?.cssConfig;
  }

  /**
   * Getter per la configurazione CSS dell'input.
   * Il getter verificherà se esiste una configurazione custom esistente.
   * Se esiste la ritornerà, altrimenti ritonerà quella di default.
   * @returns RiscaFormInputCss con la configurazione.
   */
  get cssConfig(): RiscaFormInputCss {
    // Ritorno la configurazione css con priorità a quella custom, se esiste
    return this.cssConfigCustom ?? this.cssConfigDefault;
  }

  /**
   * Getter per la configurazione dell'input secondo il tipo.
   * @returns TRiscaTipoInput con la configurazione per l'input.
   */
  get inputTypeConfig(): TRiscaTipoInput {
    // Recupero la tipologia dell'oggetto
    const type: TipoInputTable = this.inputConfig?.type;
    // Il type è configurato in modo da poter estrarre l'oggetto giusto dalla configurazione
    return this.inputConfig[type];
  }

  /**
   * Getter per la configurazione DATI dell'input.
   * @returns TRiscaTipoInput con la configurazione.
   */
  get dataConfig(): TRiscaConfiguraInput {
    // Recupero la configurazione dell'input per il tipo definito
    const inputConfig: TRiscaTipoInput = this.inputTypeConfig;
    // Ritorno la configurazione grafica
    return inputConfig?.dataConfig;
  }

  /**
   * Getter per la configurazione sugli errori dell'input.
   * @returns MappaErroriFormECodici[] con la configurazione.
   */
  get errMapFormControl(): MappaErroriFormECodici[] {
    // Recupero la configurazione dell'input per il tipo definito
    const inputConfig: TRiscaTipoInput = this.inputTypeConfig;
    // Ritorno la configurazione grafica
    return inputConfig?.errMapFormControl;
  }

  /**
   * Getter per la configurazione sugli errori dell'input.
   * @returns MappaErroriFormECodici[] con la configurazione.
   */
  get errMapFormGroup(): MappaErroriFormECodici[] {
    // Recupero la configurazione dell'input per il tipo definito
    const inputConfig: TRiscaTipoInput = this.inputTypeConfig;
    // Ritorno la configurazione grafica
    return inputConfig?.errMapFormGroup;
  }

  /**
   * Getter che recupera l'istanza del form control per la input.
   * @returns AbstractControl con l'istanza del form control.
   */
  get inputFC(): AbstractControl {
    // Tento di recuperare dal form gruop il form control
    return this.formGroup?.get(this.formControlName);
  }

  /**
   * Setter per la gestione dell'abilitazione/disabilitazione della input.
   * @param enable boolean con lo stato d'abilitazione da impostare per la input.
   */
  set enableInput(enable: boolean) {
    // Definisco l'oggetto per la gestione della propagazione dell'evento sul form group
    const o: FormUpdatePropagation = { emitEvent: false };
    // Verifico il flag e imposto l'abilitazione dell'input
    enable ? this.inputFC?.enable(o) : this.inputFC?.disable(o);
  }

  /**
   * Setter per il valore della input del form.
   * @param value any con il valore da impostare per la input.
   */
  set inputValue(value: any) {
    // Definisco l'oggetto per la gestione della propagazione dell'evento sul form group
    const o: FormUpdatePropagation = { emitEvent: false };
    // Tento di aggiornare i dato per la input
    this.inputFC?.setValue(value, o);
  }
}
