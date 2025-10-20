import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonConsts } from '../../../../consts/common-consts.consts';
import { generateRandomId } from '../../../../services/risca/risca-utilities/risca-utilities.functions';
import { RiscaButtonTypes } from '../../../../utilities';
import {
  RiscaButtonConfig,
  RiscaButtonCss,
} from './../../../../utilities/classes/utilities.classes';

@Component({
  selector: 'risca-button',
  templateUrl: './risca-button.component.html',
  styleUrls: ['./risca-button.component.scss'],
})
export class RiscaButtonComponent implements OnInit {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Variabile che contiene le informazioni dell'enum RiscaButtonTypes, per l'utilizzo nel DOM. */
  riscaButtonTypes = RiscaButtonTypes;

  /** Input che definisce le configurazioni per gli stili della input. */
  @Input() cssConfig: RiscaButtonCss;
  /** Input che definisce le configurazioni per i dati della input. */
  @Input() dataConfig: RiscaButtonConfig;

  /** Output che definisce l'evento di: click. */
  @Output() onClick = new EventEmitter<any>();

  /** Boolean che definisce lo stato di disabled del pulsante. */
  disabled: boolean = false;
  /** String che definisce un id univoco per l'istanza del componente. */
  idDOM: string = 'riscaButton';

  /** Input setter per il disabled del button. */
  @Input() set disable(disabled: boolean) {
    // Assegno il disabled
    this.disabled = disabled;
  }

  /**
   * Costruttore
   */
  constructor() {
    // Lancio il steup del componente
    this.setupComponenteRB();
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup del componente.
   */
  private setupComponenteRB() {
    // Lancio la generazione dell'id da usare per il dom
    this.idDOM = `${this.idDOM}-${generateRandomId(1)}`;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter per la condizione di costruzione del DOM.
   */
  get attivaAEA() {
    // Verifico se nella configurazione esiste il codice AEA
    return this.dataConfig?.codeAEA != undefined;
  }

  /**
   * Getter per il type del button.
   */
  get typeButton() {
    // Verifico se esiste la configurazione
    return this.cssConfig?.typeButton ?? this.riscaButtonTypes.primary;
  }

  /**
   * Getter per la classe di stile per la svg associata al type button.
   */
  get typeButtonSvg() {
    // Richiamo il type button
    return `${this.typeButton}-svg`;
  }
}
