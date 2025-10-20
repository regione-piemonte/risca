import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AbilitazioniGuard } from '../../shared/guards/abilitazioni.guard';
import { AccessoSoggettiGuard } from '../../shared/guards/accesso-soggetti.guard';
import {
  AbilitaDAGruppi,
  AbilitaDASezioni
} from '../../shared/utilities/enums/utilities.enums';
import { GruppoComponent } from './components/gruppo/gruppo.component';
import { SoggettoComponent } from './components/soggetto/soggetto.component';
import { SoggettoResolver } from './resolvers/soggetto/soggetto.resolver';

const routes: Routes = [
  {
    path: 'soggetto',
    component: SoggettoComponent,
    canActivate: [AccessoSoggettiGuard],
    resolve: { soggetto: SoggettoResolver },
    data: {
      reuse: false,
    },
  },
  {
    path: 'gruppo',
    component: GruppoComponent,
    canActivate: [AbilitazioniGuard],
    data: {
      reuse: false,
      abilitazioni: [
        {
          sezione: AbilitaDASezioni.gruppi,
          abilitazione: AbilitaDAGruppi.isAbilitato,
        },
      ],
    },
  },
  { path: '', redirectTo: 'soggetto', /* data: { reuse: true } */ },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SoggettiRoutingModule {}
