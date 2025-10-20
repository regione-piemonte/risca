import { Component } from '@angular/core';
import { RiscaButtonComponent } from '../risca-button/risca-button.component';

@Component({
  selector: 'risca-default-button',
  templateUrl: './risca-default-button.component.html',
  styleUrls: ['./risca-default-button.component.scss'],
})
export class RiscaDefaultButtonComponent extends RiscaButtonComponent {
  /**
   * Costruttore
   */
  constructor() {
    super();
  }
}
