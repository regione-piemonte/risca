import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { PraticaDTVo } from '../../../../../../../../core/commons/vo/pratica-vo';
import { LoggerService } from '../../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { CommonConsts } from '../../../../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaStorageService } from '../../../../../../../../shared/services/risca/risca-storage.service';
import { RiscaUtilitiesService } from '../../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaServerError } from '../../../../../../../../shared/utilities';
import { DatiTecniciAmbienteConsts } from '../../../../../../consts/quadri-tecnici/dati-tecnici-ambiente/dati-tecnici-ambiente.consts';
import { PraticheService } from '../../../../../../service/pratiche.service';
import { DatiTecniciAmbienteConverterService } from '../../../../services/ambito-ambiente/version-20211001/dati-tecnici-ambiente-converter.service';
import { QuadriTecniciService } from '../../../../services/quadri-tecnici/pratica/quadri-tecnici.service';
import {
  DTRiepilogoSDConfig,
  DT_RIEPILOGO_SD_CONFIG,
} from '../../../../utilities/configs/dt-riepilogo-sd.injectiontoken';
import {
  IDatiTecniciAmbiente,
  UsoLeggePSDAmbienteInfo,
} from '../../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { DatiTecniciSDRiepilogoComponent } from '../../../core/dati-tecnici-sd-riepilogo/dati-tecnici-sd-riepilogo.component';
import { DTASDRiepilogoFieldsConfigClass } from './classes/dt-ambiente-sd-riepilogo.fields-configs';

/**
 * Componente per la gestione del form dati tecnici, ambito: ambiente.
 * Questo componente è di sola lettura e riepilogo dei dati della pratica.
 */
@Component({
  selector: 'dt-ambiente-sd-riepilogo',
  templateUrl: './dt-ambiente-sd-riepilogo.component.html',
  styleUrls: ['./dt-ambiente-sd-riepilogo.component.scss'],
})
export class DTAmbienteSDRiepilgoComponent
  extends DatiTecniciSDRiepilogoComponent<PraticaDTVo>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti utilizzate comuni. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_C = new DatiTecniciAmbienteConsts();

  /** DatiTecniciAmbiente che conterrà la configurazione dei dati generati dall'input. */
  datiTecniciAmbiente: IDatiTecniciAmbiente;

  /** Classe DTASDRiepilogoFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: DTASDRiepilogoFieldsConfigClass;

  /**
   * Costruttore.
   */
  constructor(
    @Inject(DT_RIEPILOGO_SD_CONFIG)
    injConfig: DTRiepilogoSDConfig,
    private _formBuilder: FormBuilder,
    private _dtaConverter: DatiTecniciAmbienteConverterService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    pratiche: PraticheService,
    quadriTecnici: QuadriTecniciService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaStorage: RiscaStorageService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super per il setup della classe estesa
    super(
      injConfig,
      logger,
      navigationHelper,
      pratiche,
      quadriTecnici,
      riscaFormSubmitHandler,
      riscaStorage,
      riscaUtilities
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit() {
    // Funzione che converte un oggetto PraticaDTVo in DatiTecniciAmbiente
    this.initDatiTecniciAmbiente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Setup delle input del calendario
    this.setupFormInputs();
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Essendo le configurazioni parecchie, si utilizza una classe di supporto
    this.formInputs = new DTASDRiepilogoFieldsConfigClass(
      this._riscaFormBuilder
    );
  }

  /**
   * Funzione che prepara possibili dati in input per la gestione di dati utilizzabili dal componente.
   */
  private initDatiTecniciAmbiente() {
    // Inizializzo il componente
    this.initForms();
    // Iniizializzazioni delle configurazioni
    this.initDatiTecnici();
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup della struttura della form principale #####
    this.initFormDatiTecniciAmbiente();
    // Setup di possibili configurazioni in input con valorizzazione dati componente
    this.initComponenteConfigs(this.datiTecniciAmbiente);
  }

  /**
   * Funzione che definisce le logiche di init per le configurazioni del componente.
   */
  private initDatiTecnici() {
    // Verifico che esista la configurazione
    if (!this.praticaDT) {
      // Nessuna configurazione
      return;
    }

    // Variabile di comodo
    const pDT = this.praticaDT;
    const sd = this.statoDebitorio;
    // Effettuo la conversione dell'oggetto (riferimento: versione 20211001)
    this._dtaConverter
      .convertStatoDebitorioRiepilogoToDatiTecniciAmbiente(pDT, sd)
      .subscribe({
        next: (dta: IDatiTecniciAmbiente) => {
          // Assegno l'oggetto convertito
          this.datiTecniciAmbiente = dta;
          // Setup di possibili configurazioni in input con valorizzazione dati componente
          this.initComponenteConfigs(this.datiTecniciAmbiente);
        },
        error: (e: RiscaServerError) => {
          // Richiamo al gestione dell'errore
          this.onServiziError(e);
        },
      });
  }

  /**
   * Funzione specifica adibita al setup del form mainForm.
   */
  private initFormDatiTecniciAmbiente() {
    this.mainForm = this._formBuilder.group({
      corpoIdricoCaptazione: new FormControl(
        { value: '', disabled: true },
        { validators: [Validators.required] }
      ),
      comune: new FormControl(
        { value: '', disabled: true },
        { validators: [Validators.required] }
      ),
      nomeImpiantoIdroElettrico: new FormControl(
        { value: '', disabled: true },
        { validators: [] }
      ),
      usiRiepilogo: new FormControl(
        { value: '', disabled: true },
        { validators: [] }
      ),
    });

    // Disabilito la form
    this.mainForm.disable();
  }

  /**
   * Popola i campi della form in base all'oggetto da modificare che é stato passato.
   */
  private initComponenteConfigs(dta: IDatiTecniciAmbiente) {
    // verifico esistano configurazioni
    if (!dta) {
      // Niente configurazioni
      return;
    }

    // Variabili di comodo
    const f = this.mainForm;
    const o = { emitEvent: false };

    // Verifico e setto il dato
    if (dta.comune != undefined) {
      // Varibili del campo
      const k = this.DTA_C.COMUNE;
      const d = dta.comune;
      // Aggiorno il campo della form
      this._riscaUtilities.setFormValue(f, k, d, o);
    }

    // Verifico e setto il dato
    if (dta.corpoIdricoCaptazione != undefined) {
      // Varibili del campo
      const k = this.DTA_C.CORPO_IDRICO_CAPTAZIONE;
      const d = dta.corpoIdricoCaptazione;
      // Aggiorno il campo della form
      this._riscaUtilities.setFormValue(f, k, d, o);
    }

    // Verifico e setto il dato
    if (dta.nomeImpiantoIdroElettrico != undefined) {
      // Varibili del campo
      const k = this.DTA_C.NOME_IMPIANTO_IDROELETTRICO;
      const d = dta.nomeImpiantoIdroElettrico;
      // Aggiorno il campo della form
      this._riscaUtilities.setFormValue(f, k, d, o);
    }

    // Verifico e setto il dato
    if (dta.usiDiLegge != undefined) {
      // Varibili del campo
      const k = this.DTA_C.USI_RIEPILOGO;
      // Concateno le descrizione degli usi
      const d = this.concatDesUsiLegge(dta.usiDiLegge);
      // Aggiorno il campo della form
      this._riscaUtilities.setFormValue(f, k, d, o);
    }
  }

  /**
   * Funzione di comodo che concatena le descrizioni degli usi di legge da visualizzare.
   * @param usi Array di UsoDiLeggeInfo contenente gli oggetti degli usi della pratica.
   */
  private concatDesUsiLegge(usi: UsoLeggePSDAmbienteInfo[]) {
    // Verifico l'input
    if (!usi || usi.length === 0) {
      // Nessun dato, ritorno stringa vuota
      return '';
    }

    // Effettuo una concatenazione delle informazioni
    return usi
      .map((uso: UsoLeggePSDAmbienteInfo) => {
        return uso.usoDiLegge?.des_tipo_uso ?? '';
      })
      .filter((des: string) => des != '')
      .join(', ');
  }

  /**
   * ########################
   * GESTIONE FORM PRINCIPALE
   * ########################
   */

  /**
   * Funzione che effettua il submit dei dati della form datti tecnici ambiente.
   * Richiamabile dal padre.
   * @override
   */
  onFormSubmit() {
    // NOTA: Per mantenere graficamente la struttura si usa la form, ma s'inibiscono le funzioni facendo l'override delle dei metodi del form.
  }

  /**
   * Funzione che effettua il reset dei dati della form datti tecnici ambiente.
   * Richiamabile dal padre.
   * @override
   */
  onFormReset() {
    // NOTA: Per mantenere graficamente la struttura si usa la form, ma s'inibiscono le funzioni facendo l'override delle dei metodi del form.
  }
}
