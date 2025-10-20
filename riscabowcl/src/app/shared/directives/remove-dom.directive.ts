import {
  Directive,
  Input,
  OnInit,
  TemplateRef,
  ViewContainerRef,
} from '@angular/core';

@Directive({
  selector: '[riscaRemove]',
})
export class RiscaRemoveDomDirective implements OnInit {
  constructor(
    private _templateRef: TemplateRef<any>,
    private _viewContainerRef: ViewContainerRef
  ) {}

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  @Input()
  set riscaRemove(remove: boolean) {
    // Richiamo la funzione di remove
    this.remove(remove);
  }

  /**
   * Funzione che mantiene o rimuove il DOM.
   * @param remove boolean che definisce se il DOM è da rimuovere.
   */
  private remove(remove: boolean) {
    if (remove) {
      this._viewContainerRef.clear();
    } else {
      this._viewContainerRef.createEmbeddedView(this._templateRef);
    }
  }
}

@Directive({
  selector: '[riscaMaintain]',
})
export class RiscaMaintainDomDirective implements OnInit {
  constructor(
    private _templateRef: TemplateRef<any>,
    private _viewContainerRef: ViewContainerRef
  ) {}

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  @Input()
  set riscaMaintain(maintain: boolean) {
    // Richiamo la funzione di mantain
    this.maintain(maintain);
  }

  /**
   * Funzione che mantiene o rimuove il DOM.
   * @param maintain boolean che definisce se il DOM è da mantenere.
   */
  private maintain(maintain: boolean) {
    if (maintain) {
      this._viewContainerRef.createEmbeddedView(this._templateRef);
    } else {
      this._viewContainerRef.clear();
    }
  }
}
