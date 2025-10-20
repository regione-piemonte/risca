import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'risca-separator',
  templateUrl: './risca-separator.component.html',
  styleUrls: ['./risca-separator.component.scss'],
})
export class RiscaSeparatorComponent implements OnInit {
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}
