import { HTTP_INTERCEPTORS } from '@angular/common/http';
import {
  ModuleWithProviders,
  NgModule,
  Optional,
  SkipSelf,
} from '@angular/core';
import { RouteReuseStrategy } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { RiscaReuseStrategy } from '../shared/services/risca/risca-route-strategy.service';
import { AppHttpInterceptor } from './interceptors/app-http.interceptor';
import { RiscaBodyActiveNavPipe } from './pipes/body/body.pipe';
import { AppAPIErrorsInterceptor } from './interceptors/app-api-errors.interceptor';

@NgModule({
  imports: [],
  declarations: [RiscaBodyActiveNavPipe],
  exports: [RiscaBodyActiveNavPipe],
  providers: [{ provide: RouteReuseStrategy, useClass: RiscaReuseStrategy }],
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error(
        'CoreModule has already been loaded. Import Core modules in the AppModule only.'
      );
    }
  }

  static forRoot(): ModuleWithProviders<CoreModule> {
    return {
      ngModule: CoreModule,

      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AppHttpInterceptor,
          multi: true,
        },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AppAPIErrorsInterceptor,
          multi: true,
        },
        CookieService,
      ],
    };
  }
}
