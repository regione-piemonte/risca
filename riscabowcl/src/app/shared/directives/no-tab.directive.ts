import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({ selector: '[NoTab]' })
export class RiscaNoTab {
  // --------------------------------------

  /**
   * Class constructor
   * @param hostElement
   */
  constructor(private hostElement: ElementRef) {}

  /**
   * Event handler for host's keydown event
   * @param event
   */
  @HostListener('keydown', ['$event']) onKeyDown(e: KeyboardEvent) {
    let key: string = e.key;
    if (key == 'Tab') {
      e.preventDefault();
    }
  }

  /**
   * Event handler for host's keyup event
   * @param event
   */
  @HostListener('keyup', ['$event']) onKeyUp(e: KeyboardEvent) {
    let key: string = e.key;
    if (key == 'Tab') {
      e.preventDefault();
    }
  }

  /**
   * Event handler for host's keypress event
   * @param event
   */
  @HostListener('keypress', ['$event']) onKeyPress(e: KeyboardEvent) {
    let key: string = e.key;
    if (key == 'Tab') {
      e.preventDefault();
    }
  }
}
