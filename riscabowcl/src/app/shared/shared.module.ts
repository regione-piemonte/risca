import { CdkStepperModule } from '@angular/cdk/stepper';
import { CommonModule, registerLocaleData } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import localeDeExtra from '@angular/common/locales/extra/it';
import localeDe from '@angular/common/locales/it';
import { CUSTOM_ELEMENTS_SCHEMA, LOCALE_ID, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ConfigService } from '../core/services/config.service';
import {
  EmptyRouteComponent,
  StepperComponent,
  StepperFooterComponent,
} from './components';
import { FormCercaSoggettoComponent } from './components/form-cerca-soggetto/form-cerca-soggetto.component';
import { FormInputComponent } from './components/form-inputs/form-input/form-input.component';
import { RiscaCheckboxComponent } from './components/form-inputs/risca-checkbox/risca-checkbox.component';
import { RiscaDatepickerComponent } from './components/form-inputs/risca-datepicker/risca-datepicker.component';
import { RiscaEmailComponent } from './components/form-inputs/risca-email/risca-email.component';
import { RiscaFormControlErrorComponent } from './components/form-inputs/risca-form-control-error/risca-form-control-error.component';
import { RiscaFormGroupErrorComponent } from './components/form-inputs/risca-form-group-error/risca-form-group-error.component';
import { RiscaInputLabelComponent } from './components/form-inputs/risca-input-label/risca-input-label.component';
import { RiscaNumberFormattedComponent } from './components/form-inputs/risca-number-formatted/risca-number-formatted.component';
import { RiscaNumberItFormatComponent } from './components/form-inputs/risca-number-it-format/risca-number-it-format.component';
import { RiscaNumberComponent } from './components/form-inputs/risca-number/risca-number.component';
import { RiscaRadioComponent } from './components/form-inputs/risca-radio/risca-radio.component';
import { RiscaSearchComponent } from './components/form-inputs/risca-search/risca-search.component';
import { RiscaSelectComponent } from './components/form-inputs/risca-select/risca-select.component';
import { RiscaTextFakeComponent } from './components/form-inputs/risca-text-fake/risca-text-fake.component';
import { RiscaTextComponent } from './components/form-inputs/risca-text/risca-text.component';
import { RiscaTextareaComponent } from './components/form-inputs/risca-textarea/risca-textarea.component';
import { RiscaTypeaheadComponent } from './components/form-inputs/risca-typeahead/risca-typeahead.component';
import { RiscaAccertamentoComponent } from './components/risca/risca-accertamento/risca-accertamento.component';
import { RiscaAccordionSwitchComponent } from './components/risca/risca-accordion-switch/risca-accordion-switch.component';
import { RiscaAlertListenerComponent } from './components/risca/risca-alert-listener/risca-alert-listener.component';
import { RiscaAlertComponent } from './components/risca/risca-alert/risca-alert.component';
import { RiscaButtonComponent } from './components/risca/risca-buttons/risca-button/risca-button.component';
import { RiscaDefaultButtonComponent } from './components/risca/risca-buttons/risca-default-button/risca-default-button.component';
import { RiscaDropdownButtonComponent } from './components/risca/risca-buttons/risca-dropdown-button/risca-dropdown-button.component';
import { RiscaLinkButtonComponent } from './components/risca/risca-buttons/risca-link-button/risca-link-button.component';
import { RiscaPrimaryButtonComponent } from './components/risca/risca-buttons/risca-primary-button/risca-primary-button.component';
import { RiscaContattiComponent } from './components/risca/risca-contatti/risca-contatti.component';
import { GestioneAnagraficaComponent } from './components/risca/risca-dati-anagrafici/gestione-anagrafica.component';
import { GestioneRecapitoComponent } from './components/risca/risca-dati-anagrafici/gestione-recapito.component';
import { RiscaDatiSoggettoComponent } from './components/risca/risca-dati-soggetto/risca-dati-soggetto.component';
import { RiscaFiloAriannaComponent } from './components/risca/risca-filo-arianna/risca-filo-arianna.component';
import { RiscaFormChildComponent } from './components/risca/risca-form-child/risca-form-child.component';
import { RiscaFormParentAndChildComponent } from './components/risca/risca-form-parent-and-child/risca-form-parent-and-child.component';
import { RiscaFormParentComponent } from './components/risca/risca-form-parent/risca-form-parent.component';
import { RiscaGestionePagamentoComponent } from './components/risca/risca-gestione-pagamento/risca-gestione-pagamento.component';
import { RiscaIconComponent } from './components/risca/risca-icon/risca-icon.component';
import { RiscaImageBox } from './components/risca/risca-image-box/risca-image-box.component';
import { RiscaIndirizzoSpedizioneFormComponent } from './components/risca/risca-indirizzo-spedizione-form/risca-indirizzo-spedizione-form.component';
import { RiscaConfirmModalComponent } from './components/risca/risca-modals/risca-confirm-modal/risca-confirm-modal.component';
import { RiscaModalHeaderComponent } from './components/risca/risca-modals/risca-modal-header/risca-modal-header.component';
import { RiscaRecapitoModalComponent } from './components/risca/risca-modals/risca-recapito-modal/risca-recapito-modal.component';
import { RiscaUtilityModalComponent } from './components/risca/risca-modals/risca-utility-modal/risca-utility-modal.component';
import { RiscaNavHelperComponent } from './components/risca/risca-nav-helper/risca-nav-helper.component';
import { RiscaPagamentoBollettazioneComponent } from './components/risca/risca-pagamento-bollettazione/risca-pagamento-bollettazione.component';
import { RiscaPagamentoManualeComponent } from './components/risca/risca-pagamento-manuale/risca-pagamento-manuale.component';
import { RiscaRecapitoComponent } from './components/risca/risca-recapito/risca-recapito.component';
import { RiscaRicercaBollettiniComponent } from './components/risca/risca-ricerca-bollettini/risca-ricerca-bollettini.component';
import { RiscaRicercaMorositaComponent } from './components/risca/risca-ricerca-morosita/risca-ricerca-morosita.component';
import { RiscaRicercaPagamentiComponent } from './components/risca/risca-ricerca-pagamenti/risca-ricerca-pagamenti.component';
import { RiscaRicercaRimborsiComponent } from './components/risca/risca-ricerca-rimborsi/risca-ricerca-rimborsi.component';
import { RiscaRicercaSemplicePraticheComponent } from './components/risca/risca-ricerca-semplice-pratiche/risca-ricerca-semplice-pratiche.component';
import { RiscaRimborsoComponent } from './components/risca/risca-rimborso/risca-rimborso.component';
import { RiscaSeparatorComponent } from './components/risca/risca-separator/risca-separator.component';
import { RiscaStampaPraticaBtnComponent } from './components/risca/risca-stampa-pratica-btn/risca-stampa-pratica-btn.component';
import { RiscaTableActionComponent } from './components/risca/risca-table-action/risca-table-action.component';
import { RiscaTableInputComponent } from './components/risca/risca-table-input/risca-table-input.component';
import { RiscaTablePagingComponent } from './components/risca/risca-table-paging/risca-table-paging.component';
import { RiscaTableTraceSelectionsComponent } from './components/risca/risca-table-trace-selector/risca-table-trace-selections.component';
import { RiscaTableComponent } from './components/risca/risca-table/risca-table.component';
import { RiscaUtilitiesComponent } from './components/risca/risca-utilities/risca-utilities.component';
import { CONFIG } from './config.injectiontoken';
import { RiscaDisplayDirective } from './directives/display-dom.directive';
import { RiscaNumbersOnly } from './directives/input-only-number.directive';
import { RiscaNoTab } from './directives/no-tab.directive';
import {
  RiscaMaintainDomDirective,
  RiscaRemoveDomDirective,
} from './directives/remove-dom.directive';
import { RiscaAccessoElementiAppDirective } from './directives/risca/risca-accesso-elementi-app.directive';
import { AbilitazioniGuard } from './guards/abilitazioni.guard';
import { AccessoSoggettiGuard } from './guards/accesso-soggetti.guard';
import { AuthGuard } from './guards/auth.guard';
import { RiscaLoggerInterceptor } from './interceptors/risca-logger.interceptor';
import { RiscaNoCacheInterceptor } from './interceptors/risca-no-cache.interceptor';
import { RiscaServerErrorInterceptor } from './interceptors/risca-server-error.interceptor';
import { RiscaSpinnerInterceptor } from './interceptors/risca-spinner.interceptor';
import { RiscaAllegatiComponent } from './modals/risca-allegati/risca-allegati.component';
import { RiscaIndirizziSpedizioneModalComponent } from './modals/risca-indirizzi-spedizione-modal/risca-indirizzi-spedizione-modal.component';
import { RiscaSDPerPagamentoModalComponent } from './modals/risca-sd-nap-codice-utenza-modal/risca-sd-per-pagamento-modal.component';
import {
  AbilitazioneDAPipe,
  ADAGIsAbilitatoPipe,
  ADASIsGestioneAbilitataPipe,
} from './pipes/abilitazioni.pipe';
import { RiscaAccessoElementoAppPipe } from './pipes/risca/risca-accesso-elementi-app.pipe';
import { RiscaFormErrorPipe } from './pipes/risca/risca-form-errors.pipe';
import {
  RiscaIndirizzoSpedizioneTitlePipe,
  RiscaRecapitoPerIndirizzoSpedizionePipe,
} from './pipes/risca/risca-indirizzi-spedizione-modal.pipe';
import { RiscaNavLinkClassPipe } from './pipes/risca/risca-nav.pipe';
import { RiscaOptionValuePipe } from './pipes/risca/risca-select.pipe';
import { RiscaTablePagingMostraTotalePipe } from './pipes/risca/risca-table-paging.pipe';
import {
  RiscaTableActionPipe,
  RiscaTableCACheckboxDataPipe,
  RiscaTableCAIconPipe,
  RiscaTableCheckSottorighePipe,
  RiscaTableColspan,
  RiscaTableCssHeaderSortablePipe,
  RiscaTableHeaderActionsPipe,
  RiscaTableRowTitlePipe,
  RiscaTableSortIconPipe,
  RiscaTableSourceCssPipe,
  RiscaTableSourceOutputPipe,
  RiscaTableTrDynamicClassPipe,
  RiscaVisibleTableActionPipe,
} from './pipes/risca/risca-table.pipe';
import {
  RiscaTypeaheadDataValiditaPipe,
  RiscaTypeaheadMapPipe,
} from './pipes/risca/risca-typeahead.pipe';
import {
  RiscaAggiornatoFontePipe,
  RiscaCaricaNavContentPipe,
  RiscaCssHandlerPipe,
  RiscaExecutePipe,
  RiscaFormatoImportoITAPipe,
  RiscaInnerHTMLPipe,
  RiscaMessagePipe,
  RiscaMessageWithPlacholderByCodePipe,
  RiscaSanitizePipe,
  RiscaStylesHandlerPipe,
} from './pipes/risca/risca-utilities.pipe';
import { RiscaFormBuilderService } from './services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaModalService } from './services/risca/risca-modal.service';
import { RiscaUtilitiesService } from './services/risca/risca-utilities/risca-utilities.service';
import { StepManagerService } from './services/step-manager.service';

registerLocaleData(localeDe, 'it', localeDeExtra);

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CdkStepperModule,
    FormsModule,
    NgbModule,
  ],
  providers: [
    ConfigService,
    StepManagerService,
    RiscaModalService,
    RiscaUtilitiesService,
    RiscaFormBuilderService,
    // Guard
    AuthGuard,
    AbilitazioniGuard,
    AccessoSoggettiGuard,
    RiscaTableHeaderActionsPipe,
    { provide: LOCALE_ID, useValue: 'it-IT' },
    { provide: CONFIG, useValue: '' },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RiscaNoCacheInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RiscaServerErrorInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RiscaSpinnerInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RiscaLoggerInterceptor,
      multi: true,
    },
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  declarations: [
    // Componenti
    EmptyRouteComponent,
    StepperComponent,
    StepperFooterComponent,
    RiscaConfirmModalComponent,
    RiscaAlertComponent,
    RiscaAlertListenerComponent,
    FormCercaSoggettoComponent,
    FormInputComponent,
    RiscaAccertamentoComponent,
    RiscaRecapitoModalComponent,
    RiscaSeparatorComponent,
    RiscaDatepickerComponent,
    RiscaTextComponent,
    RiscaSearchComponent,
    RiscaTextFakeComponent,
    RiscaSelectComponent,
    RiscaTableComponent,
    RiscaTableInputComponent,
    RiscaTableActionComponent,
    RiscaFormControlErrorComponent,
    RiscaFormGroupErrorComponent,
    RiscaDatiSoggettoComponent,
    RiscaTypeaheadComponent,
    RiscaRecapitoComponent,
    RiscaRicercaSemplicePraticheComponent,
    RiscaEmailComponent,
    RiscaContattiComponent,
    RiscaInputLabelComponent,
    RiscaNumberComponent,
    RiscaNumberFormattedComponent,
    RiscaNumberItFormatComponent,
    RiscaTextareaComponent,
    RiscaCheckboxComponent,
    RiscaRadioComponent,
    RiscaAccordionSwitchComponent,
    FormCercaSoggettoComponent,
    RiscaFormParentAndChildComponent,
    RiscaFormParentComponent,
    RiscaFormChildComponent,
    GestioneAnagraficaComponent,
    GestioneRecapitoComponent,
    RiscaButtonComponent,
    RiscaLinkButtonComponent,
    RiscaPrimaryButtonComponent,
    RiscaDefaultButtonComponent,
    RiscaNavHelperComponent,
    RiscaIconComponent,
    RiscaDropdownButtonComponent,
    RiscaUtilitiesComponent,
    RiscaModalHeaderComponent,
    RiscaIndirizzoSpedizioneFormComponent,
    RiscaModalHeaderComponent,
    RiscaIndirizziSpedizioneModalComponent,
    RiscaPagamentoManualeComponent,
    RiscaPagamentoBollettazioneComponent,
    RiscaSDPerPagamentoModalComponent,
    RiscaModalHeaderComponent,
    RiscaIndirizziSpedizioneModalComponent,
    RiscaAllegatiComponent,
    RiscaUtilityModalComponent,
    RiscaRimborsoComponent,
    RiscaFiloAriannaComponent,
    RiscaStampaPraticaBtnComponent,
    RiscaImageBox,
    RiscaRicercaPagamentiComponent,
    RiscaRicercaMorositaComponent,
    RiscaRicercaRimborsiComponent,
    RiscaTableTraceSelectionsComponent,
    RiscaGestionePagamentoComponent,
    // Direttive
    RiscaRemoveDomDirective,
    RiscaMaintainDomDirective,
    RiscaNumbersOnly,
    RiscaNoTab,
    RiscaAccessoElementiAppDirective,
    RiscaDisplayDirective,
    // Pipes
    RiscaAggiornatoFontePipe,
    RiscaFormErrorPipe,
    RiscaTableSortIconPipe,
    RiscaTableSourceOutputPipe,
    RiscaTableRowTitlePipe,
    RiscaExecutePipe,
    RiscaTableActionPipe,
    RiscaTableHeaderActionsPipe,
    RiscaTableCssHeaderSortablePipe,
    RiscaTableCAIconPipe,
    RiscaTableTrDynamicClassPipe,
    RiscaCssHandlerPipe,
    RiscaStylesHandlerPipe,
    RiscaTypeaheadMapPipe,
    RiscaTypeaheadDataValiditaPipe,
    RiscaNavLinkClassPipe,
    RiscaOptionValuePipe,
    AbilitazioneDAPipe,
    RiscaTablePagingComponent,
    ADASIsGestioneAbilitataPipe,
    ADAGIsAbilitatoPipe,
    RiscaAccessoElementoAppPipe,
    RiscaRicercaBollettiniComponent,
    RiscaSanitizePipe,
    RiscaMessagePipe,
    RiscaMessageWithPlacholderByCodePipe,
    RiscaIndirizzoSpedizioneTitlePipe,
    RiscaRecapitoPerIndirizzoSpedizionePipe,
    RiscaTableCheckSottorighePipe,
    RiscaTableSourceCssPipe,
    RiscaVisibleTableActionPipe,
    RiscaTableCACheckboxDataPipe,
    RiscaInnerHTMLPipe,
    RiscaTablePagingMostraTotalePipe,
    RiscaFormatoImportoITAPipe,
    RiscaTableColspan,
    RiscaCaricaNavContentPipe,
  ],
  exports: [
    // Moduli
    CdkStepperModule,
    ReactiveFormsModule,
    // Componente
    EmptyRouteComponent,
    StepperComponent,
    StepperFooterComponent,
    RiscaAlertComponent,
    RiscaAlertListenerComponent,
    FormCercaSoggettoComponent,
    FormInputComponent,
    RiscaAccertamentoComponent,
    RiscaConfirmModalComponent,
    RiscaRecapitoModalComponent,
    RiscaSeparatorComponent,
    RiscaDatepickerComponent,
    RiscaTextComponent,
    RiscaSearchComponent,
    RiscaTextFakeComponent,
    RiscaSelectComponent,
    RiscaTableComponent,
    RiscaTableInputComponent,
    RiscaTableActionComponent,
    RiscaFormControlErrorComponent,
    RiscaFormGroupErrorComponent,
    RiscaDatiSoggettoComponent,
    RiscaTypeaheadComponent,
    RiscaRecapitoComponent,
    RiscaRicercaBollettiniComponent,
    RiscaPagamentoManualeComponent,
    RiscaPagamentoBollettazioneComponent,
    RiscaSDPerPagamentoModalComponent,
    RiscaRicercaSemplicePraticheComponent,
    RiscaEmailComponent,
    RiscaContattiComponent,
    RiscaInputLabelComponent,
    RiscaNumberComponent,
    RiscaNumberFormattedComponent,
    RiscaNumberItFormatComponent,
    RiscaTextareaComponent,
    RiscaCheckboxComponent,
    RiscaRadioComponent,
    RiscaAccordionSwitchComponent,
    FormCercaSoggettoComponent,
    RiscaFormParentAndChildComponent,
    RiscaFormParentComponent,
    RiscaFormChildComponent,
    GestioneAnagraficaComponent,
    GestioneRecapitoComponent,
    RiscaButtonComponent,
    RiscaLinkButtonComponent,
    RiscaPrimaryButtonComponent,
    RiscaDefaultButtonComponent,
    RiscaNavHelperComponent,
    RiscaIconComponent,
    RiscaDropdownButtonComponent,
    RiscaUtilitiesComponent,
    RiscaModalHeaderComponent,
    RiscaIndirizzoSpedizioneFormComponent,
    RiscaModalHeaderComponent,
    RiscaIndirizziSpedizioneModalComponent,
    RiscaRimborsoComponent,
    RiscaUtilityModalComponent,
    RiscaRimborsoComponent,
    RiscaFiloAriannaComponent,
    RiscaStampaPraticaBtnComponent,
    RiscaImageBox,
    RiscaRicercaPagamentiComponent,
    RiscaRicercaMorositaComponent,
    RiscaRicercaRimborsiComponent,
    RiscaTablePagingComponent,
    RiscaTableTraceSelectionsComponent,
    RiscaGestionePagamentoComponent,
    // Direttive
    RiscaRemoveDomDirective,
    RiscaMaintainDomDirective,
    RiscaNumbersOnly,
    RiscaNoTab,
    RiscaAccessoElementiAppDirective,
    RiscaDisplayDirective,
    // Pipe
    RiscaAggiornatoFontePipe,
    RiscaFormErrorPipe,
    RiscaTableSortIconPipe,
    RiscaTableSourceOutputPipe,
    RiscaTableRowTitlePipe,
    RiscaExecutePipe,
    RiscaTableActionPipe,
    RiscaTableHeaderActionsPipe,
    RiscaTableCssHeaderSortablePipe,
    RiscaTableCAIconPipe,
    RiscaTableTrDynamicClassPipe,
    RiscaCssHandlerPipe,
    RiscaStylesHandlerPipe,
    RiscaTypeaheadMapPipe,
    RiscaTypeaheadDataValiditaPipe,
    RiscaNavLinkClassPipe,
    RiscaOptionValuePipe,
    AbilitazioneDAPipe,
    ADASIsGestioneAbilitataPipe,
    ADAGIsAbilitatoPipe,
    RiscaAccessoElementoAppPipe,
    RiscaSanitizePipe,
    RiscaMessagePipe,
    RiscaMessageWithPlacholderByCodePipe,
    RiscaIndirizzoSpedizioneTitlePipe,
    RiscaRecapitoPerIndirizzoSpedizionePipe,
    RiscaVisibleTableActionPipe,
    RiscaTableCheckSottorighePipe,
    RiscaTableSourceCssPipe,
    RiscaInnerHTMLPipe,
    RiscaTablePagingMostraTotalePipe,
    RiscaFormatoImportoITAPipe,
    RiscaTableColspan,
    RiscaCaricaNavContentPipe,
  ],
  entryComponents: [
    EmptyRouteComponent,
    RiscaConfirmModalComponent,
    RiscaConfirmModalComponent,
    RiscaUtilityModalComponent,
    RiscaRecapitoModalComponent,
    RiscaIndirizziSpedizioneModalComponent,
    RiscaSDPerPagamentoModalComponent,
  ],
})
export class SharedModule {}
