import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../shared/guards/auth.guard';

const routes: Routes = [
  {
    path: 'home',
    canActivate: [AuthGuard],
    loadChildren: () => import('./home/home.module').then((m) => m.HomeModule),
  },
  {
    path: 'pratiche',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pratiche/pratiche.module').then((m) => m.PraticheModule),
  },
  {
    path: 'soggetti',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./soggetti/soggetti.module').then((m) => m.SoggettiModule),
  },
  {
    path: 'pagamenti',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./pagamenti/pagamenti.module').then((m) => m.PagamentiModule),
  },
  {
    path: 'report',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./report/report.module').then((m) => m.ReportModule),
  },
  {
    path: 'configurazioni',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./configurazioni/configurazioni.module').then((m) => m.ConfigurazioniModule),
  },
  { path: '', redirectTo: '' /* data: { reuse: false } */ },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FeaturesRoutingModule {}
