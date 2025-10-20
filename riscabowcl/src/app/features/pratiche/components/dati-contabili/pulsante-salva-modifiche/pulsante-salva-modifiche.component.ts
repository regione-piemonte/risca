import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DatiContabiliConsts } from '../../../consts/dati-contabili/dati-contabili.consts';

@Component({
  selector: 'pulsante-salva-modifiche',
  templateUrl: './pulsante-salva-modifiche.component.html',
  styleUrls: ['./pulsante-salva-modifiche.component.scss'],
})
export class PulsanteSalvaModificheComponent implements OnInit {
  /** Oggetto contenente una serie di costanti comuni per la sezione dei dati contabili. */
  DC_C = DatiContabiliConsts;

  /** Boolean che definisce lo stato di disabled del pulsante. */
  disabled: boolean = false;

  /** Input setter per il disabled del button. */
  @Input() set disable(disabled: boolean) {
    // Assegno il disabled
    this.disabled = disabled;
  }

  /** Output che definisce l'evento di click sul pulsante. */
  @Output('salvaModifiche') salvaModifiche$ = new EventEmitter<any>();

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione agganciata all'evento di click del pulsante.
   */
  salvaModifiche() {
    // Emetto l'evento
    this.salvaModifiche$.emit();
  }
}
