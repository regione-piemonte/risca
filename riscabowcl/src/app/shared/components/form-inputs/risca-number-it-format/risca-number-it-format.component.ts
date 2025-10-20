import {
  Component,
  ElementRef,
  forwardRef,
  Input,
  OnInit,
  ViewChild,
} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { FormInputsService } from '../../../services/form-inputs/form-inputs.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaFormInputNumber } from '../../../utilities';
import { FormInputComponent } from '../form-input/form-input.component';

@Component({
  selector: 'risca-number-it-format',
  templateUrl: './risca-number-it-format.component.html',
  styleUrls: ['./risca-number-it-format.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RiscaNumberItFormatComponent),
      multi: true,
    },
  ],
})
export class RiscaNumberItFormatComponent
  extends FormInputComponent<RiscaFormInputNumber>
  implements OnInit, ControlValueAccessor
{
  /** Funzione richiamata all'evento di blur della input. */
  @Input() onBlur: (value: any) => {};
  /** Boolean che permette di configurare lo stepper per l'input numerica. */
  @Input() stepper: boolean = false;
  /** number che permette di configurare i decimali del dato. */
  @Input('decimals') inputDecimals: number;
  /** boolean che permette di mantenere i decimali 0 (non significativi) se la quantità di decimali non raggiungesse il numero di decimali impostati. */
  @Input('decimaliNonSignificativi') nonSignDec: boolean = false;
  /** boolean che permette di configurare il troncamento. */
  @Input() truncate: boolean;
  /** boolean che permette di configurare l'arrotondamento del dato. */
  @Input() round: boolean;

  /** ViewChild che definisce il collegamento tra l'input del DOM e il componente. */
  @ViewChild('riscaNumberItFormatInput') riscaNumberItFormatInput: ElementRef;

  /**
   * Costruttore.
   */
  constructor(
    formInputs: FormInputsService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(formInputs, riscaUtilities);
  }

  ngOnInit() {
    // Verifico che esistano i dati input richiamando il super
    super.ngOnInit();

    // Per la gestione del dato, devo convertire il value
    this.value = this.formatImportoITA(this.value, this.decimals);
  }

  /**
   * Funzione agganciata all'evento blur della input.
   */
  onInputBlur() {
    // Evito che la input converta il campo vuoto (null) come 0
    if (this.value == undefined || this.value === '') {
      // Lancio la propagazione del valore
      this.handlePropagation(null);
      // Imposto il value ad undefined
      this.value = undefined;
      // #
    } else {
      // Definisco il valore su cui devo andare a lavorare
      let valueIT: number;
      // Converto il valore in un number float gestibile
      valueIT = this.importoITAToJsFloat(this.value);
      // Gestisco il numeber secondo le logiche di troncamento e gestione
      valueIT = this.transformValue(valueIT);
      // Lancio la propagazione del valore
      this.handlePropagation(valueIT);

      // Recupero le configurazioni del componente
      const d: number = this.decimals;
      const nsd: boolean = this.decimaliNonSignificativi;
      // Effettuo la formattazione dell'input in importo italiano
      this.value = this.formatImportoITA(valueIT, d, nsd);
    }
  }

  /**
   * Funzione che gestisce la propagazione del valore della input.
   * @param value any con l'informazione da propagare.
   */
  private handlePropagation(value: any) {
    // Verifico se è stata definita una funzione per il blur
    if (this.onBlur !== undefined) {
      // Eseguo la funzione di blur passando il value
      this.onBlur(value);
    } else {
      // Lancio il change del valore
      this.onChange(value);
    }
  }

  /**
   * Funzione che applica delle regole per la gestione del valore in output.
   * @param value number con il valore da gestire.
   */
  private transformValue(value: number): number {
    // Assegno localmente il valore
    const v: number = value;
    // Verifico se esiste un valore
    if (v == undefined) {
      // Nessun valore
      return v;
    }

    // Recupero il numero di decimali
    const decimals = this.decimals;
    // Verifico se sono stati configurati decimali specifici
    if (decimals == undefined) {
      // Nessuna configurazione ritorno il valore
      return v;
    }

    // Verifico se c'è da effettuare un troncamento
    if (this.truncate || decimals > 0) {
      // Converto l'informazione in un numero e tronco i decimali
      let valueTrunc: string;
      valueTrunc = this.formatImportoITA(v, decimals);
      // Riconverto il valore in un numero
      return this.importoITAToJsFloat(valueTrunc);
    }

    // Verifico se c'è d'arrotondare
    if (this.round) {
      // Faccio l'arrotondamento del dato
      return this.arrotondaEccesso(v, decimals);
    }

    // Ritorno il valore
    return v;
  }

  /**
   * Funzione implementata dall'interfaccia ControlValueAccessor per il cambio del valore dal Form padre.
   * @param value any con il nuovo valore.
   * @override
   */
  writeValue(value: any) {
    // Assegno locamente il valore
    this.value = this.formatImportoITA(value, this.decimals);
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Formatta un importo mettendo una virgola a separare i decimali ed il punto per separare le migliaia.
   * La formattazione troncherà i numeri decimali.
   * @param importo number | string da formattare.
   * @param decimal number con il numero di decimali da mandatenere.
   * @param decimaliNonSignificativi boolean che definisce se, dalla gestione dei decimali, bisogna completare i decimali con gli 0 mancanti (non significativi). Per default è false.
   * @returns string con la formattazione applicata.
   */
  formatImportoITA(
    importo: number | string,
    decimals?: number,
    decimaliNonSignificativi: boolean = false
  ): string {
    // Richiamo la funzione di comodo
    return this._riscaUtilities.formatoImportoITA(
      importo,
      decimals,
      decimaliNonSignificativi
    );
    // #
  }

  /**
   * Formatta un importo italiano in un numero compatibile con javascript.
   * @param importo string da formattare.
   * @returns number con il numero correttamente convertito.
   */
  importoITAToJsFloat(importo: string): number {
    // Richiamo la funzione di comodo
    return this._riscaUtilities.importoITAToJsFloat(importo);
    // #
  }

  /**
   * Funzione di arrotondamento per eccesso di un numero.
   * Se non definiti, i decimali sono 0.
   * @param n number con il numero d'arrotondare.
   * @param d number con i decimali da arrotondare.
   */
  arrotondaEccesso(n: number, d: number = 0): number {
    // Richiamo la funzione di utility
    return this._riscaUtilities.arrotondaEccesso(n, d);
  }

  /**
   * Funzione di arrotondamento per eccesso di un numero.
   * Se non definiti, i decimali sono 0.
   * @param n number | string con il numero d'arrotondare.
   * @param d number con i decimali da arrotondare.
   */
  arrotondaDifetto(n: number | string, d: number = 0): number {
    // Richiamo la funzione di utility
    return this._riscaUtilities.arrotondaDifetto(n, d);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che ritorna i decimali per la visualizzazione del numero.
   * @returns number con i decimali da visualizzare.
   */
  get decimals(): number {
    // Verifico la valorizzazione e ritorno le informazioni
    return this.inputDecimals ?? this.dataConfig.decimals ?? 0;
  }

  /**
   * Getter che ritorna la configurazione di gestione per i decimali non significativi.
   * @returns boolean con il valore della configurazione.
   */
  get decimaliNonSignificativi(): boolean {
    // Verifico la valorizzazione e ritorno le informazioni
    return this.nonSignDec || this.dataConfig.decimaliNonSignificativi;
  }

  /**
   * Getter che ritorna la configurazione per l'utilizzo del separatore delle migliaia.
   * @returns boolean con la configurazione.
   */
  get allowThousandsSeparator(): boolean {
    // Verifico la configurazione e ritorno il valore
    return this.dataConfig.allowThousandsSeparator ?? true;
  }
}
