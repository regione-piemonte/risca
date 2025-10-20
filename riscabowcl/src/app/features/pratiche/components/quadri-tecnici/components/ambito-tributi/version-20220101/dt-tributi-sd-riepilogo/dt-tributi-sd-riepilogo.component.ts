import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { PraticaDTVo } from '../../../../../../../../core/commons/vo/pratica-vo';
import { LoggerService } from '../../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { CommonConsts } from '../../../../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaStorageService } from '../../../../../../../../shared/services/risca/risca-storage.service';
import { RiscaUtilitiesService } from '../../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaServerError } from '../../../../../../../../shared/utilities';
import { DatiTecniciTributiConsts } from '../../../../../../consts/quadri-tecnici/dati-tecnici-tributi/dati-tecnici-tributi.consts';
import { PraticheService } from '../../../../../../service/pratiche.service';
import { DatiTecniciTributiConverterService } from '../../../../services/ambito-tributi/version-20220101/dati-tecnici-tributi-converter.service';
import { QuadriTecniciService } from '../../../../services/quadri-tecnici/pratica/quadri-tecnici.service';
import {
  DTPraticaConfig,
  DT_PRATICA_CONFIG,
} from '../../../../utilities/configs/dt-pratica.injectiontoken';
import { IDatiTecniciTributi } from '../../../../utilities/interfaces/dt-tributi-pratica.interfaces';
import { DatiTecniciSDRiepilogoComponent } from '../../../core/dati-tecnici-sd-riepilogo/dati-tecnici-sd-riepilogo.component';
import { DTTSDTributiFieldsConfigClass } from './classes/dt-tributi-sd-riepilogo.fields-configs';

@Component({
  selector: 'app-dt-tributi-sd-riepilogo',
  templateUrl: './dt-tributi-sd-riepilogo.component.html',
  styleUrls: ['./dt-tributi-sd-riepilogo.component.scss'],
})
export class DTTributiSDRiepilogoComponent
  extends DatiTecniciSDRiepilogoComponent<PraticaDTVo>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti utilizzate comuni. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTT_C = new DatiTecniciTributiConsts();

  /** DatiTecniciTributi che conterrà la configurazione dei dati generati dall'input. */
  datiTecniciTributi: IDatiTecniciTributi;

  /** Classe DTTSDRiepilogoFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: DTTSDTributiFieldsConfigClass;

  /**
   * Costruttore.
   */
  constructor(
    @Inject(DT_PRATICA_CONFIG) injConfig: DTPraticaConfig,
    private _formBuilder: FormBuilder,
    private _dttConverter: DatiTecniciTributiConverterService,
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
    this.initDatiTecniciTributi();
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
    this.formInputs = new DTTSDTributiFieldsConfigClass(this._riscaFormBuilder);
  }

  /**
   * Funzione che prepara possibili dati in input per la gestione di dati utilizzabili dal componente.
   */
  private initDatiTecniciTributi() {
    // Inizializzo il componente
    this.initForms();
    // Iniizializzazioni delle configurazioni
    this.initConfigs();
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup della struttura della form principale #####
    this.initFormDatiTecniciTributi();
    // Setup di possibili configurazioni in input con valorizzazione dati componente
    this.initComponenteConfigs(this.datiTecniciTributi);
  }

  /**
   * Funzione che definisce le logiche di init per le configurazioni del componente.
   */
  private initConfigs() {
    // Verifico che esista la configurazione
    if (!this.praticaDT) {
      // Nessuna configurazione
      return;
    }

    // Variabile di comodo
    const c = this.praticaDT;
    // Effettuo la conversione dell'oggetto (riferimento: versione 20211001)
    this._dttConverter.convertPraticaDTVoToDatiTecniciTributi(c).subscribe({
      next: (dtt: IDatiTecniciTributi) => {
        // Assegno l'oggetto convertito
        this.datiTecniciTributi = dtt;
        // Setup di possibili configurazioni in input con valorizzazione dati componente
        this.initComponenteConfigs(this.datiTecniciTributi);
      },
      error: (e: RiscaServerError) => {
        // Loggo l'errore
        this._logger.error('initDatiTecniciAmbiente', e);
      },
    });
  }

  /**
   * Funzione specifica adibita al setup del form mainForm.
   */
  private initFormDatiTecniciTributi() {
    this.mainForm = this._formBuilder.group({
      uso: new FormControl({ value: '', disabled: true }, { validators: [] }),
      popolazione: new FormControl(
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
  private initComponenteConfigs(dtt: IDatiTecniciTributi) {
    // verifico esistano configurazioni
    if (!dtt) {
      // Niente configurazioni
      return;
    }

    // Variabili di comodo
    const f = this.mainForm;
    const o = { emitEvent: false };

    // Verifico e setto il dato
    if (dtt.uso != undefined) {
      // Varibili del campo
      const k = this.DTT_C.USO;
      const d = dtt.uso?.des_tipo_uso ?? '';
      // Aggiorno il campo della form
      this._riscaUtilities.setFormValue(f, k, d, o);
    }

    // Verifico e setto il dato
    if (dtt.popolazione != undefined) {
      // Varibili del campo
      const k = this.DTT_C.POPOLAZIONE;
      const d = dtt.popolazione;
      // Aggiorno il campo della form
      this._riscaUtilities.setFormValue(f, k, d, o);
    }
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
