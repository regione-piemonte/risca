import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  NgbActiveModal,
  NgbDatepickerModule,
  NgbModule,
} from '@ng-bootstrap/ng-bootstrap';
import { DataTablesModule } from 'angular-datatables';
import { SharedModule } from 'src/app/shared/shared.module';
import { PraticheModule } from '../pratiche/pratiche.module';
import { BollettiniComponent } from './component/bollettini/bollettini.component';
import { DettaglioMorositaComponent } from './component/dettaglio-morosita/dettaglio-morosita.component';
import { DettaglioRimborsiComponent } from './component/dettaglio-rimborsi/dettaglio-rimborsi.component';
import { GestionePagamentiComponent } from './component/gestione-pagamenti/gestione-pagamenti.component';
import { GestioneVerificheComponent } from './component/gestione-verifiche/gestione-verifiche.component';
import { PagamentiDaVisionareComponent } from './component/pagamenti-da-visionare/pagamenti-da-visionare.component';
import { RicercaMorositaComponent } from './component/ricerca-morosita/ricerca-morosita.component';
import { RicercaPagamentiComponent } from './component/ricerca-pagamenti/ricerca-pagamenti.component';
import { RicercaRimborsiComponent } from './component/ricerca-rimborsi/ricerca-rimborsi.component';
import { RicerchePagamentiComponent } from './component/ricerche-pagamenti/ricerche-pagamenti.component';
import { DettaglioMorositaGuard } from './guards/dettaglio-accertamenti/dettaglio-accertamenti.guard';
import { DettaglioRimborsiGuard } from './guards/dettaglio-rimborsi/dettaglio-rimborsi.guard';
import { AvvisoBonarioModalComponent } from './modal/avviso-bonario-modal/avviso-bonario-modal.component';
import { BollettazioneGrandeIdroelettricoModalComponent } from './modal/bollettazione-grande-idroelettrico/bollettazione-grande-idroelettrico-modal.component';
import { BollettazioneOrdinariaModalComponent } from './modal/bollettazione-ordinaria-modal/bollettazione-ordinaria-modal.component';
import { BollettazioneSpecialeModalComponent } from './modal/bollettazione-speciale-modal/bollettazione-speciale-modal.component';
import { BollettiniModalComponent } from './modal/bollettini-modal/bollettini-modal.component';
import { SollecitoPagamentoModalComponent } from './modal/sollecito-pagamento-modal/sollecito-pagamento-modal.component';
import { PagamentiRoutingModule } from './pagamenti-routing.module';
import { BollettiniConverterService } from './service/bollettini/bollettini-converter.service';

const declarations = [
  // COMPONENTI
  GestionePagamentiComponent,
  GestioneVerificheComponent,
  PagamentiDaVisionareComponent,
  BollettiniComponent,
  RicerchePagamentiComponent,
  RicercaPagamentiComponent,
  RicercaMorositaComponent,
  RicercaRimborsiComponent,
  DettaglioMorositaComponent,
  DettaglioRimborsiComponent,
  // MODALI
  BollettiniModalComponent,
  AvvisoBonarioModalComponent,
  SollecitoPagamentoModalComponent,
  BollettazioneSpecialeModalComponent,
  BollettazioneOrdinariaModalComponent,
  BollettazioneGrandeIdroelettricoModalComponent,
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    NgbModule,
    PagamentiRoutingModule,
    PraticheModule,
    DataTablesModule,
    NgbDatepickerModule,
  ],
  providers: [
    NgbActiveModal,
    BollettiniConverterService,
    DettaglioMorositaGuard,
    DettaglioRimborsiGuard,
  ],
  declarations: declarations,
  exports: declarations,
  entryComponents: [
    BollettiniModalComponent,
    SollecitoPagamentoModalComponent,
    BollettazioneSpecialeModalComponent,
    BollettazioneOrdinariaModalComponent,
    BollettazioneGrandeIdroelettricoModalComponent,
    AvvisoBonarioModalComponent,
  ],
})
export class PagamentiModule {}
