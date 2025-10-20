import { Component } from '@angular/core';
import { RiscaButtonComponent } from '../risca-button/risca-button.component';

@Component({
  selector: 'risca-primary-button',
  templateUrl: './risca-primary-button.component.html',
  styleUrls: ['./risca-primary-button.component.scss'],
})
export class RiscaPrimaryButtonComponent extends RiscaButtonComponent {
  /**
   * Costruttore
   */
  constructor() {
    super();
  }
}
