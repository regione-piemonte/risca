import {
  Component,
  EventEmitter,
  forwardRef,
  Input,
  OnInit,
  Output,
  ViewEncapsulation,
} from '@angular/core';
import {
  ControlValueAccessor,
  FormGroup,
  NG_VALUE_ACCESSOR,
} from '@angular/forms';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { FormInputsService } from '../../../services/form-inputs/form-inputs.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  MappaErroriFormECodici,
  RiscaAppendix,
  RiscaAppendixPosition,
  RiscaFormHooks,
  RiscaFormInputCss,
  RiscaLabelPosition,
} from '../../../utilities';
import { getValoreEventoIncolla } from '../../../services/risca/risca-utilities/risca-utilities.functions';

@Component({
  selector: 'form-input',
  templateUrl: './form-input.component.html',
  styleUrls: ['./form-input.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FormInputComponent),
      multi: true,
    },
  ],
})
export class FormInputComponent<T> implements OnInit, ControlValueAccessor {
  /** Oggetto di costanti comuni all'applicazione. */
  C_C = new CommonConsts();

  /** Boolean che definisce se abilitare la modalità di debug. */
  @Input() debug: boolean = false;
  /** Input che definisce le configurazioni per gli stili della input. */
  @Input() cssConfig: RiscaFormInputCss;
  /** Input che definisce le configurazioni per i dati della input. */
  @Input() dataConfig: T;

  /** String che definisce il nome del FormControl a cui è assegnato il componente. */
  @Input('idFormControl') idFC: string;
  /** FormGroup a cui fa riferimento il componente. */
  @Input() formGroup: FormGroup;
  /** Lista di oggetti MappaErroriFormECodici contenente la lista degli errori che devono essere gestiti per il form control. */
  @Input('errMapFormControl') mappaErroriFC: MappaErroriFormECodici[] = [];
  /** Lista di oggetti MappaErroriFormECodici contenente la lista degli errori che devono essere gestiti per il form control. */
  @Input('errMapFormGroup') mappaErroriFG: MappaErroriFormECodici[] = [];
  /** Boolean che tiene traccia dello stato di Submit del form padre. */
  @Input() formSubmitted: boolean = false;

  /** Boolean che disabilita il controllo sul FormControl required per la label. */
  @Input('disableRequiredCheck') disableRequired = true;
  /** Se true, il testo deve essere sempre maiuscolo */
  @Input() toUpperCase: boolean = false;
  /** Se true, il testo deve essere sempre minuscolo */
  @Input() toLowerCase: boolean = false;
  /** Se true, il background deve essere blu. */
  @Input() aggiornatoDallaFonte: boolean = false;
  /** Input che definisce la metodologia di aggiornamento per il campo. Per default è: blur. */
  @Input('updateOn') inputUpdateOn = RiscaFormHooks.blur;

  /** Boolean che definisce se è d'attivare la gestione della pressione del pulsante: invio. */
  @Input('invio') keydownEnter = false;
  
  /** (value: any) => any; che permette di definire la logica di sanitizzazione dell'input. */
  @Input('sanitize') sanitizeLogicInput: (value: any) => any;

  /** Output che emette l'evento di pressione del pulsante: invio. Verrà passato come parametro il valore dell'input. */
  @Output() onInvio = new EventEmitter<any>();

  /** Value della input. */
  _value: any;
  /** Flag che definisce le logiche di disabilitazione dell'input. */
  disabled = false;
  /** String che definisce l'id dell'elemento dell'input, usato per la property id e for. */
  idDOM: string;

  /** Dichiarazione del contenitore della funzione OnChange che viene registrata dal Form padre. */
  onChange: any = (change: any) => {};
  /** Dichiarazione del contenitore della funzione OnTouched che viene registrata dal Form padre. */
  onTouched: any = (touch: any) => {};

  /**
   * Costruttore.
   */
  constructor(
    public _formInputs: FormInputsService,
    protected _riscaUtilities: RiscaUtilitiesService
  ) {}

  ngOnInit() {
    // Effettuo i controlli di validita degli input
    this.initInputCheck();
    // Funzione di init per il componente
    this.initBaseComponent();
  }

  /**
   * Funzione di init che verifica che gli input obbligatori siano stati definiti.
   */
  initInputCheck() {
    // Verifico che esistano i dati input
    if (!this.idFC) {
      throw new Error('idFormControl not defined');
    }
    if (!this.formGroup) {
      throw new Error('formGroup not defined');
    }
    if (!this.cssConfig) {
      throw new Error('cssConfig not defined');
    }
    if (!this.dataConfig) {
      throw new Error('dataConfig not defined');
    }
  }

  /**
   * Funzione di init del componente.
   */
  private initBaseComponent() {
    // Lancio la funzione che generi l'id del DOM per l'elemento
    this.generateIdDOM(this.idFC);
  }

  /**
   * #########################################
   * INTERFACCE PER GESTIONE DEL FORM CONTROL
   * #########################################
   */

  /**
   * Funzione implementata dall'interfaccia ControlValueAccessor per il cambio del valore dal Form padre.
   * @param value any con il nuovo valore.
   */
  writeValue(value: any) {
    // Assegno locamente il valore
    this.value = value;
  }

  /**
   * Funzione implementata dall'interfaccia ControlValueAccessor per assegnare localmente la funzione di change dal Form padre.
   * @param fn Funzione da registrare.
   */
  registerOnChange(fn: any): void {
    // Assegno locamente la funzione
    this.onChange = fn;
  }

  /**
   * Funzione implementata dall'interfaccia ControlValueAccessor per assegnare localmente la funzione di touched dal Form padre.
   * @param fn Funzione da registrare.
   */
  registerOnTouched(fn: any): void {
    // Assegno locamente la funzione
    this.onTouched = fn;
  }

  /**
   * Funzione implementata dall'interfaccia ControlValueAccessor per assegnare localmente il valore di campo disabilitato dal Form padre.
   * @param isDisabled boolean con il valore per disabilitare l'input.
   */
  setDisabledState?(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  /**
   * ########################
   * FUNZIONALITA DI UTILITY
   * ########################
   */

  /**
   * Funzione che genera un id univoco che verrà assegnato alla input.
   * @param idFC string che definisce l'id del form control, come base per la generazione dell'id del DOM.
   */
  generateIdDOM(idFC: string) {
    // Genero un id random
    const idRandom = this._riscaUtilities.generateRandomId();
    // Unisco l'id del form control con l'id randomico
    this.idDOM = `${idFC}_${idRandom}`;
  }

  /**
   * Applica la modifica del testo in maiuscolo o minuscolo in base alle input toUpperCase e toLowerCase
   */
  transformText(): void {
    // Verifico lo stato di value
    const existV = this._value !== null && this._value !== undefined;
    const existFC = this.formControl !== null && this.formControl !== undefined;
    const valueString = existV && typeof this._value === 'string';

    // Verifico i flag
    if (!existV || !existFC || !valueString) {
      // Nessuna operazione
      return;
    }

    if (this.toUpperCase) {
      this.value = this._value.toUpperCase();
    }
    if (this.toLowerCase) {
      this.value = this._value.toLowerCase();
    }
  }

  /**
   * Funzione che gestisce l'evento di: keydown.enter.
   * @param event KeyboardEvent contenente le informazioni dell'evento della tastiera.
   * @param element HTMLElement contenente le informazioni dell'oggetto a cui è collegata la funzione.
   */
  onKeydownEnter(event: KeyboardEvent, element?: HTMLElement) {
    // Verifico la configurazione
    if (this.keydownEnter) {
      // Triggero il blur dell'elemento
      element?.blur();
      // Emetto l'evento di onInvio
      this.onInvio.emit(this.value);
    }
  }

  /**
   * Funzione di utility che effettua il trim del valore della input.
   * Questo serve ad evitare informazioni sporche.
   */
  trimValue() {
    // Effettuo il trim del valore, essendo any, imposto un try catch
    try {
      // Trim del valore
      this._value = this._value?.trim();
      // #
    } catch (e) {
      // Errore dovuto al fatto che value non è string
    }
  }

  /**
   * Funzione di utility che effettua la sanitizzazione del valore in input data una funzione di sanitizzazione.
   * Questo serve ad evitare informazioni sporche.
   */
  sanitizeValue() {
    // Effettuo il trim del valore, essendo any, imposto un try catch
    try {
      // Verifico se è stata dichiarata logica per la gestione dell'incolla
      const sanitizeLogic = this.sanitizeLogic;
      // Se non è dichiarata una funziona, non faccio niente
      if (!sanitizeLogic) {
        // Niente logica
        return;
      }
        
      // Esiste logica, l'eseguo con ritorno del risultato
      const sanitizedValue = sanitizeLogic(this._value);
      // Verifico se ho un valore o undefined
      if (sanitizedValue !== undefined) {
        // Assegno il valore localmente
        this.value = sanitizedValue;
        // #
      }
    } catch (e) {
      // Errore dovuto al fatto che value non è string
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter per value.
   */
  get value(): any {
    return this._value;
  }

  /**
   * Setter per value.
   */
  set value(v: any) {
    this._value = v;
  }

  /**
   * Getter per il form control.
   */
  get formControl() {
    return this.formGroup.get(this.idFC);
  }

  /**
   * Getter per il tipo di aggiornamento definito per il componente.
   * Verifica updateOn: change.
   * @returns boolean con il check sul tipo di aggiornamento.
   */
  get updateOnChange(): boolean {
    // Recupero il form control e tento di recuperare il tipo di update (FormHooks)
    const updateOn = this.formControl?.updateOn;
    // Verifico se il tipo di aggiornamento
    return updateOn === 'change';
  }

  /**
   * Getter per il tipo di aggiornamento definito per il componente.
   * Verifica updateOn: blur.
   * @returns boolean con il check sul tipo di aggiornamento.
   */
  get updateOnBlur(): boolean {
    // Recupero il form control e tento di recuperare il tipo di update (FormHooks)
    const updateOn = this.formControl?.updateOn;
    // Verifico se il tipo di aggiornamento
    return updateOn === 'blur';
  }

  /**
   * Getter per il tipo di aggiornamento definito per il componente.
   * Verifica updateOn: change.
   * Le condizioni sono verificate per @Input('updateOn') e la configurazione del FormControl, devono essere entrambe true.
   * @returns boolean con il check sul tipo di aggiornamento.
   */
  get inputUpdOnChange(): boolean {
    // Recupero le configurazioni
    const inputUpdOnChange = this.inputUpdateOn === RiscaFormHooks.change;
    const angularUpdOnChange = this.updateOnChange;
    // Verifico le configurazioni
    return inputUpdOnChange && angularUpdOnChange;
  }

  /**
   * Getter per il tipo di aggiornamento definito per il componente.
   * Verifica updateOn: blur.
   * Le condizioni sono verificate per @Input('updateOn') e la configurazione del FormControl, basta che il valore dell'input componente sia true.
   * @returns boolean con il check sul tipo di aggiornamento.
   */
  get inputUpdOnBlur(): boolean {
    // Recupero le configurazioni
    const angularUpdOnBlur = this.updateOnBlur;
    const inputUpdOnBlur = this.inputUpdateOn === RiscaFormHooks.blur;
    // Verifico le configurazioni
    return inputUpdOnBlur || angularUpdOnBlur;
  }

  /**
   * Getter di comodo per il recupero della casistica di errore sul form.
   */
  get formErrors() {
    // Lancio la funzione del servizio
    return this._formInputs.getFormErrors(this.formGroup, this.mappaErroriFG);
  }

  /**
   * Getter di comodo che permette di generare le configurazioni, compatibili con [NgStyle]="{ ... }".
   * Verranno mergiate tutte le informazioni per la gestione dello stile.
   */
  get conditionalClass(): string {
    // Definisco una stringa che conterrà le classi di stile condizionali
    let c = '';

    // Configurazione: aggiornatoDallaFonte
    const cADF = this.aggiornatoFonte;
    // Concateno la classe
    c = `${c} ${cADF}`;

    // Ritorno un oggetto con le proprietà mergiate tra le configurazioni
    return c;
  }

  /**
   * Getter di comodo per la configurazione dello stile per: aggiornatoDallaFonte;
   */
  get aggiornatoFonte() {
    // Verifico se è stata definita la configurazione di aggiornamento dalla fonte
    const isADF = this.aggiornatoDallaFonte;
    // Verifico
    if (isADF) {
      // Ritorno la classe per la gestione del background color
      return 'risca-input-aggiornato-fonte';
      // #
    } else {
      // Ritorno una stringa vuota
      return '';
    }
  }

  /**
   * Getter per sapere se il template con label top.
   */
  get labelTmpl() {
    // Recupero l'oggetto per la configurazione
    const dataConfig = this.dataConfig as any;
    // Verifico se esiste la label top
    return dataConfig.label !== undefined;
  }

  /**
   * Getter per sapere se il template con label left.
   */
  get labelLeftTmpl() {
    // Recupero l'oggetto per la configurazione
    const dataConfig = this.dataConfig as any;
    // Verifico se esiste la label left
    return dataConfig.labelLeft !== undefined;
  }

  /**
   * Getter per sapere se il template con label right.
   */
  get labelRightTmpl() {
    // Recupero l'oggetto per la configurazione
    const dataConfig = this.dataConfig as any;
    // Verifico se esiste la label left
    return dataConfig.labelRight !== undefined;
  }

  /**
   * Getter per la posizione della label: top.
   */
  get labelTop() {
    return RiscaLabelPosition.top;
  }

  /**
   * Getter per la posizione della label: bottom.
   */
  get labelBottom() {
    return RiscaLabelPosition.bottom;
  }

  /**
   * Getter per la posizione della label: left.
   */
  get labelLeft() {
    return RiscaLabelPosition.left;
  }

  /**
   * Getter per la posizione della label: right.
   */
  get labelRight() {
    return RiscaLabelPosition.right;
  }

  /**
   * Getter di comodo che recupera il valore e mostra nella input una descrizione.
   * @returns string con la descrizione del valore.
   */
  get vauleAsTitle(): string {
    // Verifico e ritorno il valore come title
    return this._value ?? '';
  }

  /**
   * Getter di comodo verifica l'esistenza della configurazione omonima.
   * @returns boolean con il risultato del check.
   */
  get checkAppendix(): boolean {
    // Recupero la configurazione dall'input e verifico esista
    return this.appendix != undefined;
  }

  /**
   * Getter di comodo che recupera la configurazione omonima.
   * @returns RiscaAppendix con le configurazioni.
   */
  get appendix(): RiscaAppendix {
    // Recupero la configurazione dall'input
    return this.cssConfig?.appendix;
  }

  /**
   * Getter di comodo verifica l'esistenza della configurazione omonima.
   * @returns boolean con il risultato del check.
   */
  get checkAppendixLeft(): boolean {
    // Recupero la configurazione dall'input
    return this.appendix?.position === RiscaAppendixPosition.left;
  }

  /**
   * Getter di comodo verifica l'esistenza della configurazione omonima.
   * @returns boolean con il risultato del check.
   */
  get checkAppendixRight(): boolean {
    // Recupero la configurazione dall'input
    return this.appendix?.position === RiscaAppendixPosition.right;
  }

  /**
   * Getter di comodo che recupera la funzione per la gestione dell'evento "paste".
   * @return (value: any) => any; con la logica definita a livello di configurazione.
   */
  get sanitizeLogic(): (value: any) => any {
    // Recupero la configurazione dati
    const data = this.dataConfig as any;
    // Tento di recuperare la configurazione
    const pasteEvent = this.sanitizeLogicInput ?? data.sanitizeLogic;
    // Ritorno la funzione
    return pasteEvent;
  }
}
