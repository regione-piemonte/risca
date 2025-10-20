import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/shared/shared.module';
import { AltriParametriComponent } from './components/altri-parametri/altri-parametri.component';
import { CanoniFormComponent } from './components/canoni-form/canoni-form.component';
import { CanoniComponent } from './components/canoni/canoni.component';
import { ConfigurazioneComponent } from './components/configurazione/configurazione.component';
import { CreaRegolaUsoFormComponent } from './components/crea-regola-uso-form/crea-regola-uso-form.component';
import { ParametriDilazioneComponent } from './components/parametri-dilazione/parametri-dilazione.component';
import { RegolaCanoneMinimoFormComponent } from './components/regola-canone-minimo-form/regola-canone-minimo-form.component';
import { RegolaRangeFormComponent } from './components/regola-range-form/regola-range-form.component';
import { RegolaSogliaFormComponent } from './components/regola-soglia-form/regola-soglia-form.component';
import { JsonRegolaFormComponent } from './components/regola-uso-form/json-regola-form.component';
import { TassiDiInteresseComponent } from './components/tassi-di-interesse/tassi-di-interesse.component';
import { ConfigurazioniRoutingModule } from './configuraioni-routing.module';
import { CreaRegolaUsoModalComponent } from './modals/crea-regola-uso-modal/crea-regola-uso-modal.component';
import { DettaglioRegolaUsoModalComponent } from './modals/dettaglio-regola-uso-modal/dettaglio-regola-uso-modal.component';
import { InteressiLegaliFormComponent } from './components/interessi-legali-form/interessi-legali-form.component';
import { InteressiDiMoraFormComponent } from './components/interessi-di-mora-form/interessi-di-mora-form.component';
import { InteressiLegaliModalComponent } from './modals/interessi-legali-modal/interessi-legali-modal.component';
import { InteressiDiMoraModalComponent } from './modals/interessi-di-mora-modal/interessi-di-mora-modal.component';

const MODULES = [
  CommonModule,
  SharedModule,
  ConfigurazioniRoutingModule,
  NgbModule,
];

const ENTRY_COMPONENTS = [
  CreaRegolaUsoModalComponent,
  DettaglioRegolaUsoModalComponent,
  InteressiLegaliModalComponent,
  InteressiDiMoraModalComponent
];

const COMPONENTS = [
  ConfigurazioneComponent,
  CanoniComponent,
  TassiDiInteresseComponent,
  ParametriDilazioneComponent,
  AltriParametriComponent,
  CanoniFormComponent,
  CreaRegolaUsoFormComponent,
  RegolaCanoneMinimoFormComponent,
  RegolaSogliaFormComponent,
  RegolaRangeFormComponent,
  JsonRegolaFormComponent,
  InteressiLegaliFormComponent,
  InteressiDiMoraFormComponent,
  ...ENTRY_COMPONENTS,
];

@NgModule({
  imports: [...MODULES],
  declarations: [...COMPONENTS],
  exports: [...COMPONENTS],
  entryComponents: [...ENTRY_COMPONENTS],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [],
})
export class ConfigurazioniModule {}
