import { HttpClient, HttpClientModule } from '@angular/common/http';
import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SidebarModule } from 'ng-sidebar';
import { NgxSpinnerModule } from 'ngx-spinner';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RiscaBodyComponent } from './core/components/body/body.component';
import { RiscaFooterComponent } from './core/components/footer/footer.component';
import { RiscaHeaderComponent } from './core/components/header/header.component';
import { RiscaLandingComponent } from './core/components/landing/landing.component';
import { RiscaUnauthorizedComponent } from './core/components/unauthorized/unauthorized.component';
import { CoreModule } from './core/core.module';
import { ConfigService } from './core/services/config.service';
import { PagamentiModule } from './features/pagamenti/pagamenti.module';
import { PraticheModule } from './features/pratiche/pratiche.module';
import { ReportModule } from './features/report/report.module';
import { AppBootstrapInit } from './shared/resolvers/app-bootstrap/app-bootstrap.resolve';
import { SharedModule } from './shared/shared.module';
import { ConfigurazioniModule } from './features/configurazioni/configurazioni.module';

@NgModule({
  imports: [
    BrowserAnimationsModule,
    NgxSpinnerModule,
    BrowserModule,
    CoreModule.forRoot(),
    SidebarModule.forRoot(),
    HttpClientModule,
    NgbModule,
    RouterModule,
    AppRoutingModule,
    SharedModule,
    PraticheModule,
    PagamentiModule,
    ReportModule,
    ConfigurazioniModule
  ],
  declarations: [
    AppComponent,
    RiscaHeaderComponent,
    RiscaBodyComponent,
    RiscaFooterComponent,
    RiscaLandingComponent,
    RiscaUnauthorizedComponent,
  ],
  providers: [
    ConfigService,
    /* { provide: RouteReuseStrategy, useClass: RiscaReuseStrategy }, */
    // Per poter usufruire di APP_INITIALIZER è necessario definire nel provider tutti i servizi che richiede AppBootstrapInit
    HttpClient,
    AppBootstrapInit,
    {
      provide: APP_INITIALIZER,
      multi: true,
      deps: [AppBootstrapInit],
      useFactory: (appBootstrap: AppBootstrapInit) => () =>
        appBootstrap.initAppData(),
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
