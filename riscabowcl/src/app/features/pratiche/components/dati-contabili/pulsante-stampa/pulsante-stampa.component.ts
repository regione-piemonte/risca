import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { DatiContabiliConsts } from '../../../consts/dati-contabili/dati-contabili.consts';

@Component({
  selector: 'pulsante-stampa',
  templateUrl: './pulsante-stampa.component.html',
  styleUrls: ['./pulsante-stampa.component.scss'],
})
export class PulsanteStampaComponent implements OnInit {
  /** Oggetto contenente una serie di costanti comuni per la sezione dei dati contabili. */
  DC_C = DatiContabiliConsts;

  /** Output che definisce l'evento di click sul pulsante. */
  @Output('stampa') stampa$ = new EventEmitter<any>();

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
  stampa() {
    // Emetto l'evento
    this.stampa$.emit();
  }
}
