import { Directive, ElementRef, Input, OnInit } from '@angular/core';

@Directive({
  selector: '[riscaDisplay]',
})
export class RiscaDisplayDirective implements OnInit {
  /** Input che definisce il boolena per visualizzare l'elemento accordion. */
  @Input('showOn') set show(show: boolean) {
    // Richiamo la funzione di show
    this.setDisplay(show);
  }
  /** Input che definisce il boolena per nascondere l'elemento accordion. */
  @Input('hideOn') set hide(hide: boolean) {
    // Richiamo la funzione di hide
    this.setDisplay(!hide);
  }

  /** ElementRef che definisce l'oggetto rappresentativo del DOM in pagina. */
  private _element: ElementRef;

  /**
   * Costruttore.
   */
  constructor(element: ElementRef) {
    // Assegno localmente la referenza dell'oggetto.
    this.element = element;
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione invocata per visualizzare o nascondere dell'elemento del DOM.
   * @param show boolean che definisce il comportamento di visualizzazione.
   */
  setDisplay(show: boolean) {
    // Verifico se è da visualizzare
    if (show) {
      // Mostro l'elemento
      this.display = '';
      // #
    } else {
      // Nascondo l'elemento
      this.display = 'none';
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Setter per element.
   */
  set element(e: ElementRef) {
    this._element = e;
  }

  /**
   * Getter per la proprietà native element.
   */
  get nativeElement() {
    return this._element?.nativeElement;
  }

  /**
   * Getter per la proprietà style di nativeElement.
   */
  get nativeElementStyle() {
    return this.nativeElement?.style;
  }

  /**
   * Setter per la proprietà display, derivata da nativeElementStyle.
   */
  set display(display: string) {
    if (this.nativeElementStyle) {
      this.nativeElementStyle.display = display;
    }
  }
}
