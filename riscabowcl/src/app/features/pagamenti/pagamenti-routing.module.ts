import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from 'src/app/shared/guards/auth.guard';
import { RiscaGestionePagamentoComponent } from '../../shared/components/risca/risca-gestione-pagamento/risca-gestione-pagamento.component';
import { BollettiniComponent } from './component/bollettini/bollettini.component';
import { DettaglioMorositaComponent } from './component/dettaglio-morosita/dettaglio-morosita.component';
import { DettaglioRimborsiComponent } from './component/dettaglio-rimborsi/dettaglio-rimborsi.component';
import { GestionePagamentiComponent } from './component/gestione-pagamenti/gestione-pagamenti.component';
import { PagamentiDaVisionareComponent } from './component/pagamenti-da-visionare/pagamenti-da-visionare.component';
import { RicerchePagamentiComponent } from './component/ricerche-pagamenti/ricerche-pagamenti.component';
import { GestioneVerificheComponent } from './component/gestione-verifiche/gestione-verifiche.component';

const routes: Routes = [
  {
    path: 'pagamenti',
    children: [
      { path: '', redirectTo: 'gestione-pagamenti', pathMatch: 'full' },
      {
        path: 'gestione-pagamenti',
        component: GestionePagamentiComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Gestione pagamenti' },
      },
      {
        path: 'ricerche',
        component: RicerchePagamentiComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Ricerche' },
      },
      {
        path: 'pagamenti-da-visionare',
        component: PagamentiDaVisionareComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Pagamenti da visionare' },
      },
      {
        path: 'dettaglio-pagamenti',
        component: RiscaGestionePagamentoComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Dettaglio pagamenti' },
      },
    ],
  },
  {
    path: 'spedizioni',
    children: [
      { path: '', redirectTo: 'bollettini', pathMatch: 'full' },
      {
        path: 'bollettini',
        component: BollettiniComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Bollettini' },
      },
    ],
  },
  {
    path: 'verifiche',
    children: [
      { path: '', redirectTo: 'gestione-verifiche', pathMatch: 'full' },
      {
        path: 'gestione-verifiche',
        component: GestioneVerificheComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Gestione verifiche' },
      },
      {
        path: 'dettaglio-morosita',
        component: DettaglioMorositaComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Dettaglio morosità' },
      },
      {
        path: 'dettaglio-rimborsi',
        component: DettaglioRimborsiComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Dettaglio rimborsi' },
      },
    ],
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
  declarations: [],
})
export class PagamentiRoutingModule {}
