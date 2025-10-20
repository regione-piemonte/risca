import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';

/**
 * Componente adibito alla gestione delle tabelle dell'app Risca.
 */
@Component({
  selector: 'risca-accordion-switch',
  templateUrl: './risca-accordion-switch.component.html',
  styleUrls: ['./risca-accordion-switch.component.scss'],
})
export class RiscaAccordionSwitchComponent implements OnInit, OnChanges {
  /** String che definisce la label da visualizzare. */
  @Input() label: string = '';
  /** Boolean che definisce se lo switch è disabilitato. */
  @Input() disabled: boolean = false;
  /** Boolean che definisce lo stato inziale di apertura dell'accordion. */
  @Input() accordionAperto: boolean = false;

  /** Output per comunicare al padre il cambio di stato dell'accordion. */
  @Output() onAccordionClick: EventEmitter<boolean> =
    new EventEmitter<boolean>();

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnChanges(changes: SimpleChanges): void {
    // Verifico se è stato cambiato il valore dell'accordion
    if (changes.accordionAperto && !changes.accordionAperto.firstChange) {
      // Aggiorno il valore locale
      this.accordionAperto = changes.accordionAperto.currentValue;
    }
  }

  /**
   * Funzione collegata allo switch accordion che gestisce il cambio di stato.
   */
  accordionClick() {
    // Verifico se l'accordion è disabilitato
    if (this.disabled) {
      // Blocco il click
      return;
    }

    // Effettuo il toggle dello stato
    this.accordionAperto = !this.accordionAperto;
    // Emetto l'evento di change dell'accordion
    this.onAccordionClick.emit(this.accordionAperto);
  }
}
