import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbDatepickerModule, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DataTablesModule } from 'angular-datatables';
import { RiscaAllegatiModalComponent } from 'src/app/shared/components/risca/risca-modals/risca-allegati-modal/risca-allegati-modal.component';
import { SharedModule } from '../../shared/shared.module';
import { DatiAnagraficiComponent } from './components/dati-anagrafici/dati-anagrafici.component';
import { AccertamentiComponent } from './components/dati-contabili/accertamenti/accertamenti.component';
import { CalcoloInteressiComponent } from './components/dati-contabili/calcolo-interessi/calcolo-interessi.component';
import { CercaSoggettoDatiContabiliComponent } from './components/dati-contabili/cerca-soggetto-dc/cerca-soggetto-dc.component';
import { DatiContabiliComponent } from './components/dati-contabili/dati-contabili/dati-contabili.component';
import { DettaglioSDNAPComponent } from './components/dati-contabili/numero-avviso-pagamento/dettaglio-sd-nap/dettaglio-sd-nap.component';
import { NumeroAvvisoPagamentoComponent } from './components/dati-contabili/numero-avviso-pagamento/numero-avviso-pagamento/numero-avviso-pagamento.component';
import { StatiDebitoriNAPComponent } from './components/dati-contabili/numero-avviso-pagamento/stati-debitori-nap/stati-debitori-nap.component';
import { PagamentiSDComponent } from './components/dati-contabili/pagamenti-sd/pagamenti-sd.component';
import { PulsanteSalvaModificheComponent } from './components/dati-contabili/pulsante-salva-modifiche/pulsante-salva-modifiche.component';
import { PulsanteStampaComponent } from './components/dati-contabili/pulsante-stampa/pulsante-stampa.component';
import { RimborsiComponent } from './components/dati-contabili/rimborsi/rimborsi.component';
import { StatiDebitoriDedicatedService } from './components/dati-contabili/stati-debitori/services/stati-debitori-dedicated.service';
import { StatiDebitoriComponent } from './components/dati-contabili/stati-debitori/stati-debitori.component';
import { AnnualitaComponent } from './components/dati-contabili/stato-debitorio/annualita/annualita.component';
import { DatiAnagraficiSdComponent } from './components/dati-contabili/stato-debitorio/dati-anagrafici-sd/dati-anagrafici-sd.component';
import { GeneraliAmministrativiDilazioneComponent } from './components/dati-contabili/stato-debitorio/generali-amministrativi-dilazione/generali-amministrativi-dilazione.component';
import { StatoDebitorioComponent } from './components/dati-contabili/stato-debitorio/stato-debitorio/stato-debitorio.component';
import { TornaAStatiDebitoriComponent } from './components/dati-contabili/torna-a-stati-debitori/torna-a-stati-debitori.component';
import { DatiSintesiComponent } from './components/dati-sintesi/dati-sintesi.component';
import { DocumentiAllegatiComponent } from './components/documenti-allegati/documenti-allegati.component';
import { GeneraliAmministrativiComponent } from './components/generali-amministrativi/generali-amministrativi.component';
import { InserisciPraticaComponent } from './components/inserisci-pratica/inserisci-pratica.component';
import { PraticaComponent } from './components/pratica/pratica.component';
import { PraticheCollegateComponent } from './components/pratiche-collegate/pratiche-collegate.component';
import { DTAmbienteSDAnnualitaComponent } from './components/quadri-tecnici/components/ambito-ambiente/version-20211001/dt-ambiente-sd-annualita/dt-ambiente-sd-annualita.component';
import { DTAmbienteSDRiepilgoComponent } from './components/quadri-tecnici/components/ambito-ambiente/version-20211001/dt-ambiente-sd-riepilogo/dt-ambiente-sd-riepilogo.component';
import { DTCanonePratica20211001Component } from './components/quadri-tecnici/components/ambito-ambiente/version-20211001/dt-canone-pratica/dt-canone-pratica.component';
import { DTPratica20211001Component } from './components/quadri-tecnici/components/ambito-ambiente/version-20211001/dt-pratica/dt-pratica.component';
import { DTRicerca20211001Component } from './components/quadri-tecnici/components/ambito-ambiente/version-20211001/dt-ricerca/dt-ricerca.component';
import { DatiTecniciTributiComponent } from './components/quadri-tecnici/components/ambito-tributi/version-20220101/dati-tecnici-tributi/dati-tecnici-tributi.component';
import { DTTributiSDAnnualitaComponent } from './components/quadri-tecnici/components/ambito-tributi/version-20220101/dt-tributi-sd-annualita/dt-tributi-sd-annualita.component';
import { DTTributiSDRiepilogoComponent } from './components/quadri-tecnici/components/ambito-tributi/version-20220101/dt-tributi-sd-riepilogo/dt-tributi-sd-riepilogo.component';
import { RicercaDatiTecniciTributiComponent } from './components/quadri-tecnici/components/ambito-tributi/version-20220101/ricerca-dati-tecnici-tributi/ricerca-dati-tecnici-tributi.component';
import { DatiTecniciPraticaComponent } from './components/quadri-tecnici/components/core/dati-tecnici-pratica/dati-tecnici-pratica.component';
import { DatiTecniciSDRiepilogoComponent } from './components/quadri-tecnici/components/core/dati-tecnici-sd-riepilogo/dati-tecnici-sd-riepilogo.component';
import { DatiTecniciSDComponent } from './components/quadri-tecnici/components/core/dati-tecnici-sd/dati-tecnici-sd.component';
import { RicercaDatiTecniciComponent } from './components/quadri-tecnici/components/core/ricerca-dati-tecnici/ricerca-dati-tecnici.component';
import { QuadriTecniciPraticaComponent } from './components/quadri-tecnici/components/middlewares/quadri-tecnici-pratica/quadri-tecnici-pratica.component';
import { QuadriTecniciRicercaComponent } from './components/quadri-tecnici/components/middlewares/quadri-tecnici-ricerca/quadri-tecnici-ricerca.component';
import { QuadriTecniciSDRiepilogoComponent } from './components/quadri-tecnici/components/middlewares/quadri-tecnici-sd-riepilogo/quadri-tecnici-sd-riepilogo.component';
import { QuadriTecniciSDComponent } from './components/quadri-tecnici/components/middlewares/quadri-tecnici-sd/quadri-tecnici-sd.component';
import { DT_PRATICA_CONFIG } from './components/quadri-tecnici/utilities/configs/dt-pratica.injectiontoken';
import { DT_RICERCA_CONFIG } from './components/quadri-tecnici/utilities/configs/dt-ricerca.injectiontoken';
import { FormRicercaAvanzataPraticheSDComponent } from './components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/form-ricerca-avanzata-pratiche-sd.component';
import { RicercaAvanzataComponent } from './components/ricerca-avanzata/ricerca-avanzata/ricerca-avanzata.component';
import { RicercaSemplicePraticheComponent } from './components/ricerca-semplice-pratiche/ricerca-semplice-pratiche.component';
import { PraticheCollegateGuard } from './guards/pratiche-collegate/pratiche-collegate.guard';
import { AnnualitaModalComponent } from './modal/annualita-modal/annualita-modal.component';
import { CalcoloInteressiModalComponent } from './modal/calcolo-interessi-modal/calcolo-interessi-modal.component';
import { CercaSoggettoDCModalComponent } from './modal/cerca-soggetto-dc-modal/cerca-soggetto-dc-modal.component';
import { CercaTitolareModalComponent } from './modal/cerca-titolare-modal/cerca-titolare-modal.component';
import { DilazioneSDModalComponent } from './modal/dilazione-sd-modal/dilazione-sd-modal.component';
import { GestisciAccertamentoModal } from './modal/gestisci-accertamento-modal/gestisci-accertamento-modal.component';
import { GestisciRimborsoModal } from './modal/gestisci-rimborso-modal/gestisci-rimborso-modal.component';
import { GestisciSalvataggioSDModalComponent } from './modal/gestisci-salvataggio-sd-modal/gestisci-salvataggio-sd-modal.component';
import { PagamentoSDModalComponent } from './modal/pagamento-sd-modal/pagamento-sd-modal.component';
import { SDCollegatiModalComponent } from './modal/sd-collegati-modal/sd-collegati-modal.component';
import { DisabilitaBtnStatiDebitoriPipe } from './pipes/dati-contabili/dati-contabili.pipe';
import {
  AbilitaPulsantiPraticaPipe,
  DisabilitaNavPraticaPipe,
} from './pipes/inserisci-pratica/inserisci-pratica.pipe';
import { PraticheRoutingModule } from './pratiche-routing.module';
import { PraticheCollegateResolver } from './resolvers/pratiche-collegate/pratiche-collegate.resolver';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    NgbModule,
    PraticheRoutingModule,
    DataTablesModule,
    NgbDatepickerModule,
  ],
  declarations: [
    // COMPONENT
    InserisciPraticaComponent,
    GeneraliAmministrativiComponent,
    PraticaComponent,
    QuadriTecniciPraticaComponent,
    QuadriTecniciSDComponent,
    QuadriTecniciSDRiepilogoComponent,
    QuadriTecniciRicercaComponent,
    DatiTecniciPraticaComponent,
    DatiTecniciSDRiepilogoComponent,
    DatiTecniciSDComponent,
    DTPratica20211001Component,
    DatiAnagraficiComponent,
    CalcoloInteressiComponent,
    CercaTitolareModalComponent,
    DTCanonePratica20211001Component,
    RicercaSemplicePraticheComponent,
    RicercaAvanzataComponent,
    PraticheCollegateComponent,
    DatiContabiliComponent,
    DocumentiAllegatiComponent,
    DatiSintesiComponent,
    FormRicercaAvanzataPraticheSDComponent,
    RicercaDatiTecniciComponent,
    DTRicerca20211001Component,
    RicercaDatiTecniciTributiComponent,
    RiscaAllegatiModalComponent,
    DatiTecniciTributiComponent,
    DisabilitaNavPraticaPipe,
    RimborsiComponent,
    StatiDebitoriComponent,
    TornaAStatiDebitoriComponent,
    StatoDebitorioComponent,
    GeneraliAmministrativiDilazioneComponent,
    DatiAnagraficiSdComponent,
    AnnualitaComponent,
    DTAmbienteSDRiepilgoComponent,
    DTTributiSDRiepilogoComponent,
    DTAmbienteSDAnnualitaComponent,
    DTTributiSDAnnualitaComponent,
    CercaSoggettoDatiContabiliComponent,
    CercaSoggettoDCModalComponent,
    GestisciRimborsoModal,
    GestisciAccertamentoModal,
    DilazioneSDModalComponent,
    AnnualitaModalComponent,
    CalcoloInteressiModalComponent,
    PagamentoSDModalComponent,
    SDCollegatiModalComponent,
    GestisciSalvataggioSDModalComponent,
    NumeroAvvisoPagamentoComponent,
    StatiDebitoriNAPComponent,
    DettaglioSDNAPComponent,
    PagamentiSDComponent,
    AccertamentiComponent,
    PulsanteStampaComponent,
    PulsanteSalvaModificheComponent,
    // PIPE
    DisabilitaNavPraticaPipe,
    DisabilitaBtnStatiDebitoriPipe,
    AbilitaPulsantiPraticaPipe,
  ],
  exports: [
    // COMPONENT
    InserisciPraticaComponent,
    GeneraliAmministrativiComponent,
    PraticaComponent,
    QuadriTecniciPraticaComponent,
    QuadriTecniciSDComponent,
    QuadriTecniciSDRiepilogoComponent,
    QuadriTecniciRicercaComponent,
    DatiTecniciPraticaComponent,
    DatiTecniciSDRiepilogoComponent,
    DatiTecniciSDComponent,
    DTPratica20211001Component,
    CercaTitolareModalComponent,
    DTCanonePratica20211001Component,
    PraticheCollegateComponent,
    DatiContabiliComponent,
    DocumentiAllegatiComponent,
    FormRicercaAvanzataPraticheSDComponent,
    RicercaDatiTecniciComponent,
    DTRicerca20211001Component,
    RicercaDatiTecniciTributiComponent,
    RiscaAllegatiModalComponent,
    DatiTecniciTributiComponent,
    StatiDebitoriComponent,
    TornaAStatiDebitoriComponent,
    StatoDebitorioComponent,
    GeneraliAmministrativiDilazioneComponent,
    DatiAnagraficiSdComponent,
    CalcoloInteressiComponent,
    AnnualitaComponent,
    DTAmbienteSDRiepilgoComponent,
    DTTributiSDRiepilogoComponent,
    DTAmbienteSDAnnualitaComponent,
    DTTributiSDAnnualitaComponent,
    CercaSoggettoDatiContabiliComponent,
    CercaSoggettoDCModalComponent,
    GestisciRimborsoModal,
    GestisciAccertamentoModal,
    DilazioneSDModalComponent,
    AnnualitaModalComponent,
    CalcoloInteressiModalComponent,
    PagamentoSDModalComponent,
    NumeroAvvisoPagamentoComponent,
    StatiDebitoriNAPComponent,
    DettaglioSDNAPComponent,
    PagamentiSDComponent,
    AccertamentiComponent,
    PulsanteStampaComponent,
    PulsanteSalvaModificheComponent,
    RimborsiComponent,
    // PIPE
    DisabilitaNavPraticaPipe,
    DisabilitaBtnStatiDebitoriPipe,
    AbilitaPulsantiPraticaPipe,
  ],
  entryComponents: [
    CercaTitolareModalComponent,
    DTPratica20211001Component,
    DTRicerca20211001Component,
    RicercaDatiTecniciTributiComponent,
    DatiTecniciTributiComponent,
    RiscaAllegatiModalComponent,
    GestisciRimborsoModal,
    GestisciAccertamentoModal,
    DTAmbienteSDRiepilgoComponent,
    DTTributiSDRiepilogoComponent,
    DTAmbienteSDAnnualitaComponent,
    DTTributiSDAnnualitaComponent,
    CercaSoggettoDCModalComponent,
    DilazioneSDModalComponent,
    AnnualitaModalComponent,
    CalcoloInteressiModalComponent,
    PagamentoSDModalComponent,
    SDCollegatiModalComponent,
    GestisciSalvataggioSDModalComponent,
  ],
  providers: [
    /* { provide: RouteReuseStrategy, useClass: RiscaReuseStrategy }, */
    { provide: DT_PRATICA_CONFIG, useValue: '' },
    { provide: DT_RICERCA_CONFIG, useValue: '' },
    PraticheCollegateGuard,
    PraticheCollegateResolver,
    StatiDebitoriDedicatedService,
  ],
})
export class PraticheModule {}
