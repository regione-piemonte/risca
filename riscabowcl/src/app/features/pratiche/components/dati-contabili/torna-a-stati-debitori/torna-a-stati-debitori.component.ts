import { Component, OnInit } from '@angular/core';
import { DatiContabiliUtilityService } from '../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { DatiContabiliConsts } from '../../../consts/dati-contabili/dati-contabili.consts';

@Component({
  selector: 'torna-a-stati-debitori',
  templateUrl: './torna-a-stati-debitori.component.html',
  styleUrls: ['./torna-a-stati-debitori.component.scss'],
})
export class TornaAStatiDebitoriComponent implements OnInit {
  /** Oggetto contenente una serie di costanti comuni per la sezione dei dati contabili. */
  DC_C = DatiContabiliConsts;

  /**
   * Costruttore.
   */
  constructor(private _datiContabiliUtility: DatiContabiliUtilityService) {}

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione attivitata al click sul pulsante.
   * Emette l'evento per ritornare alla pagina degli stati debitori.
   */
  tornaAStatiDebitori() {
    // Richiamo l'evento del servizio
    this._datiContabiliUtility.navigateStatiDebitori();
  }
}
