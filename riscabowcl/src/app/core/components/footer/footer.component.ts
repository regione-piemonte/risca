import { Component, OnInit } from '@angular/core';

/**
 * @version SONARQUBE_22_04_24 Rimosso costruttore e OnInit vuoto.
 */
@Component({
  selector: 'risca-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class RiscaFooterComponent {
  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}
