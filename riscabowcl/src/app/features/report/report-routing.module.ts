import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from 'src/app/shared/guards/auth.guard';
import { EsportaDatiComponent } from './component/esporta-dati/esporta-dati.component';

const routes: Routes = [
  {
    path: 'report',
    children: [
      { path: '', redirectTo: 'esporta-dati', pathMatch: 'full' },
      {
        path: 'esporta-dati',
        component: EsportaDatiComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Esporta dati' },
      },
    ],
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
  declarations: [],
})
export class ReportRoutingModule {}
