import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from '../shared/shared.module';
import { FeaturesRoutingModule } from './features-routing.module';

@NgModule({
  imports: [FeaturesRoutingModule, SharedModule, NgbModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [
    /* { provide: RouteReuseStrategy, useClass: RiscaReuseStrategy }, */
  ],
})
export class FeaturesModule {}
