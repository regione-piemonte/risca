import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  forwardRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Subscription } from 'rxjs/index';
import { FormInputsService } from '../../../services/form-inputs/form-inputs.service';
import { RiscaSelectService } from '../../../services/form-inputs/risca-select/risca-select.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaFormInputSelect } from '../../../utilities';
import { FormInputComponent } from '../form-input/form-input.component';

/**
 * Componente che permette una gestione unica delle select nell'applicazione.
 * Se gli oggetti che popolano il source, contengono queste proprietà:
 * - __disabled;
 * - __selected;
 * E' possibile configurare rispettivamente: un'option disabilitata; un'option selezionata di default.
 */
@Component({
  selector: 'risca-select',
  templateUrl: './risca-select.component.html',
  styleUrls: ['./risca-select.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RiscaSelectComponent),
      multi: true,
    },
  ],
  // changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RiscaSelectComponent
  extends FormInputComponent<RiscaFormInputSelect>
  implements OnInit, OnChanges, OnDestroy, ControlValueAccessor
{
  /** Lista di oggetti any che contiene il pool di dati da visualizzare nella tendina. */
  @Input('dataSource') source: any[] = [];
  /** String che definisce il nome della proprietà da visualizzare all'interno della lista select. */
  @Input() propertyToShow: string = '';
  /** boolean che definisce se il default dal server va ignorato per la definizione del valore di default. */
  @Input() ignoreDBDefault: boolean = false;
  /** Boolean che definisce se il componente si deve registrare all'event: refreshData. */
  @Input('listenToRefreshData') refreshData: boolean = false;
  /** Boolean che definisce se il componente deve gestire il valore "null" definito dal componente padre come un init del valore iniziale della lista. */
  @Input() nullToDefault: boolean = false;
  /**
   * Boolean che definisce un comportamento di forzatura per rimanere in ascolto della modifica dei valori.
   * Per qualche motivo, quando viene selezionato un valore di default, la funzione di onChange non viene "sentita" dal form.
   * Per cui la modifica del dato non viene poi passata al FormGroup, nonostante nella select ci sia il dato.
   */
  @Input() manualListener: boolean = false;

  /**
   * Boolean che definisce al componente che in modifica un determinato campo dev'essere disabilitato campo dev'essre disabilitato
   */
  @Input() disabled: boolean;

  /** Output che emette l'evento di dato modificato gestito manualmente per il componente della select. E' collegato al flag manualListener. */
  @Output() manualEmitter = new EventEmitter<any>();

  /** Flag booleano che permette d'ignorare gli errori qualora esista un valore di placeholder selezionato. */
  ignoreDefault: boolean = false;
  /** Flag booleano che gestisce il controllo sul submitted del form. */
  checkValidation: boolean;
  /** Boolean che permette la rigenerazione di una parte del componente per gestire un bug grafico. */
  refreshDOM: boolean = true;

  /** Subscription registrato in ascolto dell'evento: refreshData. */
  private onRefreshData: Subscription;

  /**
   * Costruttore.
   */
  constructor(
    private _changeDetector: ChangeDetectorRef,
    formInputs: FormInputsService,
    private _riscaSelect: RiscaSelectService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(formInputs, riscaUtilities);
  }

  ngOnInit() {
    // Verifico che esistano i dati input richiamando il super
    super.ngOnInit();
    // Verifico che esistano i dati input
    if (!this.source) {
      throw new Error('dataSource not defined');
    }

    // Init del componente
    this.initComponente();
  }

  ngOnChanges(changes: SimpleChanges): void {
    // Verifico se è stata modificata la lista in input
    if (changes.source && !changes.source.firstChange) {
      // Effetuo verifiche e setup per il dato di default della select
      this.initDefaultValue();
      // Effettuo il setup del valore di selezionato
      this.initSelectedValue();
    }
    // Verifico se è stato aggiornato il flag di submbitted
    if (changes.formSubmitted && !changes.formSubmitted.firstChange) {
      // Lancio la verifica sul check validation
      this.verifyCheckValidation();
    }
  }

  ngOnDestroy() {
    // Tento di fare l'unsubscribe dei listener
    try {
      // Stacco la changedetection dal componente
      this._changeDetector.detach();
      // #
    } catch (e) {}

    // Verifico e tento l'unsubscribe delle subscription
    try {
      if (this.onRefreshData) {
        this.onRefreshData.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * Funzione di inizializzazione del componente tramite hook di Angular.
   */
  private initComponente() {
    // Richiamo l'init per i listener del componente
    this.initListeners();
    // Effetuo verifiche e setup per il dato di default della select
    this.initDefaultValue();
    // Effettuo il setup del valore di placeholder/default
    this.initSelectedValue();
    // Richiamo il check per la validazione
    this.verifyCheckValidation();

    // Aggiorno il flag booleano per l'ignoramento del valore di default selezionato
    this.ignoreDefault = this.dataConfig.ignoreDefault || false;
  }

  /**
   * Funzione che effettua il setup degli ascoltatori del componente.
   */
  private initListeners() {
    // Verifico se è attivo il listener: refreshData
    if (this.refreshData) {
      // Assegno localmente il subscriber
      this.onRefreshData = this._riscaSelect.refreshDOM$.subscribe({
        next: () => {
          // Attivo le logiche di refresh del valore
          this.refreshValue();
          // #
        },
      });
    }
  }

  /**
   * Funzione che verifica e imposta il valore di default della select se presente e configurato.
   */
  setDefault() {
    // Controllo se il primo valore è il default da selezionare
    if (this.source[0]?.__null && this.source[0]?.__selected) {
      // Aggiorno value
      this._value = this.source[0];
      // Attivo l'onChange
      this.onChange(null);
    }
  }

  /**
   * Funzione di comodo che verifica se alla prima posizione della select esiste un placholder.
   * @returns boolean con il risultato del check
   */
  private isFirstElemPh(): boolean {
    // Verifico se nel source è già presente un placeholder
    const firstElement = this.source[0];
    const isFistElementPh = firstElement && firstElement.__null;
    // Ritorno se il primo elemento non è un placeholder
    return isFistElementPh;
  }

  /**
   * Funzione che verifica e gestisce il possibile valore di default della select.
   * Il valore di default è un oggetto specifico, definito nella testata della select, e ritornerà come valore sempre null.
   */
  private initDefaultValue() {
    // Verifico se è necessario inserire nella lista source un placeholder
    if (this.dataConfig?.allowEmpty) {
      // Verifico se il primo elemento non è un placeholder
      if (!this.isFirstElemPh()) {
        // Aggiungo un oggetto di placeholder
        const nullObj = this.createPlaceholder();
        // Aggiungo un oggetto nullo ad inizio array
        this.source.unshift(nullObj);
        // #
      } else {
        // Resetto la proprietà __selected
        this.source[0].__selected = false;
      }
    }
  }

  /**
   * Funzione di comodo che genera un oggetto null per il placeholder.
   * @returns any con l'oggetto di placeholder.
   */
  private createPlaceholder(): any {
    // Definisco un oggetto considerato null
    const nullObj: any = {};
    // Aggiungo le informazioni minime all'oggetto
    nullObj[this.propertyToShow] = this.dataConfig.emptyLabel;
    // Aggiogno una proprietà specifica per riconoscerlo
    nullObj.__null = true;
    // Definisco la proprietà "selected" per l'oggetto
    nullObj.__selected = false;

    // Ritorno il valore del placeholder
    return nullObj;
  }

  /**
   * Funzione di setup per il valore di default della select.
   */
  private initSelectedValue() {
    // Verifico e tento d'impostare il valore selected definito dal componente padre
    const insertC = this.defaultByComponent();
    // Controllo il risultato dell'operazione
    if (!insertC) {
      // Verifico e tento d'impostare il valore selected definito dal server
      const insertDB = this.defaultByDatabase();
      // Controllo il risultato dell'operazione
      if (!insertDB) {
        // Tento d'impostare il valore selected definito per default
        this.defaultByEmpty();
      }
    }
  }

  /**
   * #################################
   * VALORE DI DEFAULT PER: COMPONENTE
   * #################################
   */

  /**
   * Funzione che verifica e imposta il valore selected per la lista, basandosi sul flag __selected che può risultare impostato dal componente padre che passa il source a questa componente.
   * @returns boolean che definisce se è stato impostato un oggetto come "selected".
   */
  private defaultByComponent(): boolean {
    // Verifico se la select è singola o multipla
    if (this.selectSingola) {
      // Gestisco la singola
      return this.defaultByComponentSingle();
      // #
    } else {
      // Gestisco la multipla
      return this.defaultByComponentMultiple();
      // #
    }
  }

  /**
   * Funzione che verifica e imposta il valore selected per la lista, basandosi sul flag __selected che può risultare impostato dal componente padre che passa il source a questa componente.
   * La funzione si basa sulla gestione del dato per una select a selezione singola.
   * @returns boolean che definisce se è stato impostato un oggetto come "selected".
   */
  private defaultByComponentSingle(): boolean {
    // Cerco all'interno del source se esiste già un oggetto selezionato
    const iCSelected = this.source?.findIndex((item: any) => {
      // Verifico se l'item è selezionato e non è il placeholder
      return !item?.__null && item?.__selected;
    });

    // Verifico se c'è già un elemento selezionato
    if (iCSelected !== -1) {
      // Imposto il value nel componente
      this.value = this.source[iCSelected];
      // Emetto l'evento di value aggiornato
      this.handleValue();
      // Ritorno true, è stato trovato
      return true;
      // #
    } else {
      // Non c'è un valore già impostato di default
      return false;
    }
  }

  /**
   * Funzione che verifica e imposta il valore selected per la lista, basandosi sul flag __selected che può risultare impostato dal componente padre che passa il source a questa componente.
   * La funzione si basa sulla gestione del dato per una select a selezione multipla.
   * @returns boolean che definisce se è stato impostato un oggetto come "selected".
   */
  private defaultByComponentMultiple(): boolean {
    // Definisco una variabile come contenitore degli elementi che risultano selezionati
    let iCSelected: any[] = [];

    // Ciclo la lista di elementi in input
    this.source?.forEach((item: any, i: number) => {
      // Verifico se l'item è selezionato
      if (!item?.__null && item?.__selected) {
        // E' selezionato, aggiungo l'elemento alla lista
        iCSelected.push(this.source[i]);
        // #
      }
    });

    // Verifico se ci sono già elementi selezionati
    if (iCSelected.length > 0) {
      // Imposto il value nel componente
      this.value = iCSelected;
      // Emetto l'evento di value aggiornato
      this.handleValue();
      // Ritorno true, è stato trovato
      return true;
      // #
    } else {
      // Non c'è un valore già impostato di default
      return false;
    }
  }

  /**
   * ###############################
   * VALORE DI DEFAULT PER: DATABASE
   * ###############################
   */

  /**
   * Funzione che verifica e imposta il valore selected per la lista, basandosi sul flag di configurazione del server.
   * @returns boolean che definisce se è stato impostato un oggetto come "selected".
   */
  private defaultByDatabase(): boolean {
    // Verifico se è disabilitata la selezione di default da DB
    if (this.ignoreDBDefault) {
      // Blocco la logica
      return false;
    }

    // Verifico se la select è singola o multipla
    if (this.selectSingola) {
      // Gestisco la singola
      return this.defaultByDatabaseSingle();
      // #
    } else {
      // Gestisco la multipla
      return this.defaultByDatabaseMultiple();
      // #
    }
  }

  /**
   * Funzione che verifica e imposta il valore selected per la lista, basandosi sul flag di configurazione del server.
   * La funzione si basa sulla gestione del dato per una select a selezione singola.
   * @returns boolean che definisce se è stato impostato un oggetto come "selected".
   */
  private defaultByDatabaseSingle(): boolean {
    // Verifico se è disabilitata la selezione di default da DB
    if (this.ignoreDBDefault) {
      // Blocco la logica
      return false;
    }

    // Cerco nel source se esiste la configurazione DB per la selezione del default [flg_default]
    const iDBSelected = this.source?.findIndex(
      (item: any) => item?.flg_default
    );

    // Verifico se c'è un oggetto selezionato da DB
    if (iDBSelected !== -1) {
      // Imposto come valore di default quello selezionato
      this.source[iDBSelected].__selected = true;
      // Aggiorno il value
      this.value = this.source[iDBSelected];
      // Emetto l'evento di value aggiornato
      this.handleValue();
      // Ritorno true, è stato trovato
      return true;
      // #
    } else {
      // Non c'è un valore già impostato di default
      return false;
    }
  }

  /**
   * Funzione che verifica e imposta il valore selected per la lista, basandosi sul flag di configurazione del server.
   * La funzione si basa sulla gestione del dato per una select a selezione multipla.
   * @returns boolean che definisce se è stato impostato un oggetto come "selected".
   */
  private defaultByDatabaseMultiple(): boolean {
    // Verifico se è disabilitata la selezione di default da DB
    if (this.ignoreDBDefault) {
      // Blocco la logica
      return false;
    }

    // Definisco una variabile come contenitore degli elementi che risultano selezionati
    let iDBSelected: number[] = [];

    // Ciclo la lista di elementi in input
    this.source?.forEach((item: any, i: number) => {
      // Verifico se l'item è selezionato
      if (item?.flg_default) {
        // E' selezionato, aggiungo l'indice alla lista
        iDBSelected.push(i);
        // #
      }
    });

    // Verifico se c'è un oggetto selezionato da DB
    if (iDBSelected.length > 0) {
      // Definisco un array di appoggio per gli elementi selezionati
      const elementsSelected: any[] = [];
      // Ciclo la lista di indici selezionati
      iDBSelected.forEach((iSel: number) => {
        // Imposto il flag di selezione a true
        this.source[iSel].__selected = true;
        // Aggiungo l'elemento alla lista dei selezionati
        elementsSelected.push(this.source[iSel]);
        // #
      });

      // Aggiorno il value
      this.value = elementsSelected;
      // Emetto l'evento di value aggiornato
      this.handleValue();
      // Ritorno true, è stato trovato
      return true;
      // #
    } else {
      // Non c'è un valore già impostato di default
      return false;
    }
  }

  /**
   * ############################
   * VALORE DI DEFAULT PER: VUOTO
   * ############################
   */

  /**
   * Funzione che verifica e imposta il valore selected per la lista, basandosi sul fatto che il primo elemento (default) è selezionato, ma con il value ad undefined.
   * @returns boolean che definisce se è stato impostato un oggetto come "selected".
   */
  private defaultByEmpty(): boolean {
    // Cerco nel source se esiste la configurazione DB per la selezione del default [flg_default]
    const anySelected = this.source?.some(
      (item: any) => !item?.__null && item?.__selected
    );

    // Verifico le condizioni per mettere come selezionato il placholder
    const reqPh = this.dataConfig?.emptyLabelSelected;
    const selectPh = !anySelected && reqPh && this.isFirstElemPh();

    // Verifico se il value è stato già valorizzato
    if (selectPh) {
      // Imposto come valore di default quello selezionato
      this.source[0].__selected = true;
      // Aggiorno il value
      this.value = this.source[0];
      // Ritorno true, è stato impostato
      return true;
      // #
    } else {
      // Non è stato eseguito nulla
      return false;
    }
  }

  /**
   * Funzione che gestisce il check sulla validazione tramite "formSubmitted".
   */
  private verifyCheckValidation() {
    // Variabili di comodo
    const fs = this.formSubmitted;
    const valueExist = this.value !== undefined;
    const valueNull = valueExist && (this.value === null || this.value.__null);
    const dontIgnoreD = !this.ignoreDefault;

    // Se il form è stato submittato, ma il valore è null e ignoreDefault è attivo blocco il controllo
    this.checkValidation = fs && valueNull && dontIgnoreD;
  }

  /**
   * Funzione collegata al change della select, per la comunicazione al padre di oggetto modificato.
   */
  onSelectChange() {
    // Richiamo la gestione del value
    this.handleValue();
  }

  /**
   * Funzione di comodo che gestisce la restituzione del dato al componente chiamante.
   */
  handleValue() {
    // Verifico se è stato selezionato l'oggetto null
    if (this._value?.__null) {
      // L'oggetto è il default iniziale, ritorno null
      this.onChange(null);
      // Verifico la configurazione per l'emissione del valore manuale
      if (this.manualListener) {
        // Emetto l'evento con il dato
        this.manualEmitter.emit(null);
      }
      // #
    } else {
      // Attivo la funzione di change
      this.onChange(this._value);
      // Verifico la configurazione per l'emissione del valore manuale
      if (this.manualListener) {
        // Emetto l'evento con il dato
        this.manualEmitter.emit(this._value);
      }
    }
  }

  /**
   * Funzione di supporto per le verifiche e i controlli d'applicare alla detect change.
   */
  private detectChanges() {
    // Utilizzo un try catch per gli errori
    try {
      // Se un oggetto esterno aggiorna il valore ho un aggiornamento automatico
      this._changeDetector.detectChanges();
      // #
    } catch (e) {}
  }

  /**
   * Riassegno il value della select per gestire possibili errori grafici.
   */
  private refreshValue() {
    // Rimuovo il DOM della select
    this.refreshDOM = false;
    // Attiviamo la detect change
    // this.detectChanges();

    // Imposto un timeout per permettere la digest applicativac
    setTimeout(() => {
      // Ripristino il DOM
      this.refreshDOM = true;
      // Attiviamo la detect change
      // this.detectChanges();
    });
  }

  /**
   * Funzione di comodo che permette di resettare il valore della select al suo valore di default (iniziale).
   */
  resetSelectedValue() {
    // Lancio la funzione di inizializzazione per il valore selezionato di default.
    this.initSelectedValue();
  }

  /**
   * #########################################
   * INTERFACCE PER GESTIONE DEL FORM CONTROL
   * #########################################
   */

  /**
   * Funzione implementata dall'interfaccia ControlValueAccessor per il cambio del valore dal Form padre.
   * @param value any con il nuovo valore.
   * @override
   */
  writeValue(value: any) {
    // Verifico se è attiva la configurazione che converte il valore 'null', definito dal padre, nel valore di default della select
    if (value === null && this.nullToDefault) {
      // Essendo null, vado a re-impostare il valore di init della select
      this.initSelectedValue();
      // #
    } else if (value === null && this.source[0]?.__null) {
      // Verifico se il value come parametro è null e nella select esiste un valore di placeholder
      // Aggiorno value
      this._value = this.source[0];
      // #
    } else {
      // Assegno locamente il valore
      this._value = value;
    }

    // Se un oggetto esterno aggiorna il valore ho un aggiornamento automatico
    // this.detectChanges();
  }

  /**
   * Funzione implementata dall'interfaccia ControlValueAccessor per assegnare localmente il valore di campo disabilitato dal Form padre.
   * @param isDisabled boolean con il valore per disabilitare l'input.
   * @override
   */
  setDisabledState(isDisabled: boolean): void {
    // Aggiorno il flag per la disable
    this.disabled = isDisabled;
    // Aggiorno la view
    // this.detectChanges();
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter che recupera la configurazione per la gestione di una select multipla.
   * @returns boolean con il risultato del check.
   */
  get selectMultipla(): boolean {
    // Ritorno la configurazione
    return this.dataConfig.multiple;
    // #
  }

  /**
   * Getter che recupera la configurazione per la gestione di una select singola.
   * @returns boolean con il risultato del check.
   */
  get selectSingola(): boolean {
    // Ritorno la configurazione
    return !this.selectMultipla;
    // #
  }
}
