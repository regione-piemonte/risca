import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { NgbActiveModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from '../../shared/shared.module';
import { AmbitoRoutingModule } from './ambito-routing.module';

@NgModule({
  entryComponents: [],
  declarations: [],
  imports: [CommonModule, NgbModule, SharedModule, AmbitoRoutingModule],
  providers: [NgbActiveModal],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AmbitoModule {}
