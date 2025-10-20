import { Component } from '@angular/core';
import { RiscaButtonComponent } from '../risca-button/risca-button.component';

@Component({
  selector: 'risca-link-button',
  templateUrl: './risca-link-button.component.html',
  styleUrls: ['./risca-link-button.component.scss'],
})
export class RiscaLinkButtonComponent extends RiscaButtonComponent {
  /**
   * Costruttore
   */
  constructor() {
    super();
  }
}
