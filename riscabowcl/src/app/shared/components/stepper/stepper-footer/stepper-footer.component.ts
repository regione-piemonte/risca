import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'stepper-footer',
  templateUrl: './stepper-footer.component.html',
  styleUrls: ['./stepper-footer.component.scss']
})
export class StepperFooterComponent implements OnInit {

  @Input() showNext;
  @Input() showPrevious;

  @Output() avanti = new EventEmitter();
  @Output() indietro = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  onAvanti() {
    this.avanti.emit();
  }

  onIndietro() {
    this.indietro.emit();
  }

}
