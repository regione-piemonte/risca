import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouteReuseStrategy } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RiscaReuseStrategy } from '../../shared/services/risca/risca-route-strategy.service';
import { SharedModule } from '../../shared/shared.module';
import { GruppoComponent } from './components/gruppo/gruppo.component';
import { SoggettoComponent } from './components/soggetto/soggetto.component';
import { GruppoGuard } from './guards/gruppo.guard';
import { SoggettoResolver } from './resolvers/soggetto/soggetto.resolver';
import { SoggettiRoutingModule } from './soggetti-routing.module';

@NgModule({
  imports: [CommonModule, SharedModule, SoggettiRoutingModule],
  providers: [
    {
      provide: RouteReuseStrategy,
      useClass: RiscaReuseStrategy,
    },
    NgbActiveModal,
    GruppoGuard,
    SoggettoResolver,
  ],
  exports: [SoggettoComponent, GruppoComponent],
  declarations: [SoggettoComponent, GruppoComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SoggettiModule {}
