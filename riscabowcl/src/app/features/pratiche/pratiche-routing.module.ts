import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../shared/guards/auth.guard';
import { InserisciPraticaComponent } from './components/inserisci-pratica/inserisci-pratica.component';
import { PraticaComponent } from './components/pratica/pratica.component';
import { PraticheCollegateComponent } from './components/pratiche-collegate/pratiche-collegate.component';
import { RicercaAvanzataComponent } from './components/ricerca-avanzata/ricerca-avanzata/ricerca-avanzata.component';
import { RicercaSemplicePraticheComponent } from './components/ricerca-semplice-pratiche/ricerca-semplice-pratiche.component';
import { PraticheCollegateGuard } from './guards/pratiche-collegate/pratiche-collegate.guard';
import { PraticheCollegateResolver } from './resolvers/pratiche-collegate/pratiche-collegate.resolver';

const routes: Routes = [
  {
    path: 'pratiche',
    children: [
      { path: '', redirectTo: 'pratica', pathMatch: 'full' },
      {
        path: 'pratica',
        component: PraticaComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Pratica' /* reuse: true */ },
      },
      {
        path: 'inserisci-pratica',
        component: InserisciPraticaComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Inserisci Pratica' /* reuse: true */ },
      },
      {
        path: 'ricerca-semplice-pratica',
        component: RicercaSemplicePraticheComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Ricerca Semplice' /* reuse: true */ },
      },
      {
        path: 'ricerca-avanzata-pratica',
        component: RicercaAvanzataComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Ricerca Avanzata' /* reuse: true */ },
      },
      {
        path: 'pratiche-collegate',
        component: PraticheCollegateComponent,
        canActivate: [AuthGuard, PraticheCollegateGuard],
        resolve: { praticheCollegate: PraticheCollegateResolver },
        data: { breadcrumb: 'Pratiche collegate' /* reuse: true */ },
      },
    ],
  },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
  declarations: [],
})
export class PraticheRoutingModule {}
