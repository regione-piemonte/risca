import { Component, Inject, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { clone } from 'lodash';
import { PraticaDTVo } from '../../../../../../../../core/commons/vo/pratica-vo';
import { IUsoLeggeVo } from '../../../../../../../../core/commons/vo/uso-legge-vo';
import { LoggerService } from '../../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../../../../../core/services/user.service';
import { CommonConsts } from '../../../../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaStorageService } from '../../../../../../../../shared/services/risca/risca-storage.service';
import { RiscaUtilitiesService } from '../../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaServerError } from '../../../../../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../../../../../shared/utilities/classes/errors-maps';
import { PraticheService } from '../../../../../../service/pratiche.service';
import { DatiTecniciTributiConverterService } from '../../../../services/ambito-tributi/version-20220101/dati-tecnici-tributi-converter.service';
import { DatiTecniciService } from '../../../../services/dati-tecnici/dati-tecnici.service';
import { QuadriTecniciRicercaService } from '../../../../services/quadri-tecnici/ricerca/quadri-tecnici-ricerca.service';
import {
  DTRicercaConfig,
  DT_RICERCA_CONFIG,
} from '../../../../utilities/configs/dt-ricerca.injectiontoken';
import { IDatiTecniciTributi } from '../../../../utilities/interfaces/dt-tributi-pratica.interfaces';
import { RicercaDatiTecniciComponent } from '../../../core/ricerca-dati-tecnici/ricerca-dati-tecnici.component';
import { RicercaDatiTecniciTributiConsts } from './utilities/ricerca-dati-tecnici-tributi.consts';
import { RDTTFieldsConfigClass } from './utilities/ricerca-dati-tecnici-tributi.fields-configs';

/**
 * Componente per la gestione del form dati tecnici, ambito: tributi.
 * Il form principale non ha un submit diretto.
 * Questo deve essere gestito dal padre.
 * Le informazioni del form verranno emesse registrandosi all'event emitter onFormChange.
 */
@Component({
  selector: 'ricerca-dati-tecnici-tributi',
  templateUrl: './ricerca-dati-tecnici-tributi.component.html',
  styleUrls: ['./ricerca-dati-tecnici-tributi.component.scss'],
})
export class RicercaDatiTecniciTributiComponent
  extends RicercaDatiTecniciComponent<PraticaDTVo>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti utilizzate comuni. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  RDTT_C = new RicercaDatiTecniciTributiConsts();
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();

  /** Input per i dati in caso di modifica. */
  @Input('datiTecniciCore') configs: string;

  /** Flag che tiene traccia se il form è stato submittato per: usiForm. */
  usiFormSubmitted = false;

  /** Array di oggetti UsoLeggeVo recuperata dal server. */
  listaUsiDiLegge: IUsoLeggeVo[] = [];

  /** Classe DTAFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: RDTTFieldsConfigClass;

  /**
   * Costruttore.
   */
  constructor(
    @Inject(DT_RICERCA_CONFIG) injConfig: DTRicercaConfig,
    private _formBuilder: FormBuilder,
    private _datiTecnici: DatiTecniciService,
    private _dtaConverter: DatiTecniciTributiConverterService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    pratiche: PraticheService,
    ricercaQuadriTecnici: QuadriTecniciRicercaService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaStorage: RiscaStorageService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Richiamo il super per il setup della classe estesa
    super(
      injConfig,
      logger,
      navigationHelper,
      pratiche,
      ricercaQuadriTecnici,
      riscaFormSubmitHandler,
      riscaStorage,
      riscaUtilities
    );
    // Funzione di setup generico
    this.setupComponente();
    // Caricamento dei dati per la lista degli usi di legge
    this.getUsiDiLegge();
  }

  ngOnInit() {
    // Funzione di setup per la form
    this.initForms();
    // Iniizializzazioni delle configurazioni
    this.initCoreConfigs();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Setep degli errori da verificare al submit della form
    this.setupMainFormErrors();
    // Setup delle input del calendario
    this.setupFormInputs();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [...this.EM.MAP_FORM_CONTROL_REQUIRED];
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Essendo le configurazioni parecchie, si utilizza una classe di supporto
    this.formInputs = new RDTTFieldsConfigClass(this._riscaFormBuilder);
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup della struttura della form principale #####
    this.initFormDatiTecniciTributi();
  }

  /**
   * Funzione che definisce le logiche di init per le configurazioni del componente.
   */
  private initCoreConfigs() {
    // Verifico che esista la configurazione
    if (!this.configs) {
      // Nessuna configurazione
      return;
    }

    // Variabile di comodo
    const c = this.configs;
    // Effettuo la conversione dell'oggetto
    this._dtaConverter
      .convertDatiTecniciCoreRicercaToDatiTecniciTributiRicerca(c)
      .subscribe({
        next: (dta: IDatiTecniciTributi) => {
          // Lancio il setup delle tabelle
          this.initComponenteConfigs(dta);
          // #
        },
        error: (e: RiscaServerError) => {
          // Loggo l'errore
          this._logger.error('initDatiTecniciTributi', e);
          // #
        },
      });
  }

  /**
   * Funzione specifica adibita al setup del form mainForm.
   */
  private initFormDatiTecniciTributi() {
    this.mainForm = this._formBuilder.group({
      popolazione: new FormControl(
        { value: undefined, disabled: false },
        { validators: [] }
      ),
      uso: new FormControl(
        { value: undefined, disabled: false },
        { validators: [] }
      ),
    });
  }

  /**
   * Funzione che popola la form principale dei dati tecnici tributi
   * @param datiTecniciTributi
   */
  initComponenteConfigs(datiTecniciTributi: IDatiTecniciTributi) {
    // Estraggo i dati
    const { uso, popolazione } = datiTecniciTributi;

    // Inserisco l'uso selezionato
    if (uso != undefined) {
      // Prendo l'uso selezionato
      const usoSelected = this.listaUsiDiLegge.find(
        (x) => x.cod_tipo_uso == uso.cod_tipo_uso
      );
      // Popolo il form
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDTT_C.USO,
        usoSelected
      );
    }

    // Inserisco la popolazione
    if (popolazione != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDTT_C.POPOLAZIONE,
        popolazione
      );
    }
  }

  /**
   * Funzione adibita al recupero dati dal server per gli usi di legge.
   */
  private getUsiDiLegge() {
    // Recupero l'id dell'ambito
    const idAmbito = this._user.idAmbito;
    // Chiamata al server per il recupero dati per gli usi di legge (null permette di recuperare tutti gli usi di legge)
    this._datiTecnici.getUsiDiLegge(idAmbito).subscribe((res) => {
      // Aggiorno la lista degli usi di legge
      this.listaUsiDiLegge = clone(res);
    });
  }

  /**
   * #############################
   * FUNZIONI DI GESTIONE DEL FORM
   * #############################
   */

  prepareMainFormForValidation() {
    if (!this.mainForm) {
      return undefined;
    }
  }

  /**
   * ########################
   * GESTIONE FORM PRINCIPALE
   * ########################
   */

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns T contente le informazioni del form in modalità raw value.
   * @override
   */
  getMainFormRawValue(c?: any): PraticaDTVo {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }
    // Recupero i dati dal form di localizzazione
    const dtaLocalizzazione: any = this.mainForm.getRawValue();
    // Converto l'oggetto dta in un oggetto PraticaDTVo
    const pdtVo =
      this._ricercaQuadriTecnici.convertRicercaDatiTecniciTributiToPraticaDTVoRicerca(
        dtaLocalizzazione
      );

    // Ritorno il dato aggiornato
    return pdtVo;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */
}
