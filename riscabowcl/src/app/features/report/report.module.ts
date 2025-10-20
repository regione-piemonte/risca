import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouteReuseStrategy } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RiscaReuseStrategy } from '../../shared/services/risca/risca-route-strategy.service';
import { SharedModule } from '../../shared/shared.module';
import { EsportaDatiComponent } from './component/esporta-dati/esporta-dati.component';
import { ReportRoutingModule } from './report-routing.module';
import { RicercaEsportaDatiComponent } from './component/risca-ricerca-rimborsi/ricerca-esporta-dati.component';

const declarations = [EsportaDatiComponent, RicercaEsportaDatiComponent];

@NgModule({
  imports: [CommonModule, SharedModule, ReportRoutingModule],
  providers: [
    {
      provide: RouteReuseStrategy,
      useClass: RiscaReuseStrategy,
    },
    NgbActiveModal,
  ],
  declarations: declarations,
  exports: declarations,
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ReportModule {}
