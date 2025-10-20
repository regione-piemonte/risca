import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../../shared/guards/auth.guard';
import { ConfigurazioneComponent } from './components/configurazione/configurazione.component';

const routes: Routes = [
  {
    path: 'configurazioni',
    children: [
      { path: '', redirectTo: 'configurazione', pathMatch: 'full' },
      {
        path: 'configurazione',
        component: ConfigurazioneComponent,
        canActivate: [AuthGuard],
        data: { breadcrumb: 'Configurazione' /* reuse: true */ },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ConfigurazioniRoutingModule {}
