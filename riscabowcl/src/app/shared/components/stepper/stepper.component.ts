import { CdkStepper } from '@angular/cdk/stepper';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'stepper',
  templateUrl: './stepper.component.html',
  styleUrls: ['./stepper.component.scss'],
  providers: [{ provide: CdkStepper, useExisting: StepperComponent }]
})
export class StepperComponent extends CdkStepper implements OnInit {

  ngOnInit() {
  }

}
