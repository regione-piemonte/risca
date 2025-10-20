import { Component, Inject, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
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
import { DatiTecniciTributiConsts } from '../../../../../../consts/quadri-tecnici/dati-tecnici-tributi/dati-tecnici-tributi.consts';
import { PraticheService } from '../../../../../../service/pratiche.service';
import { DatiTecniciTributiConverterService } from '../../../../services/ambito-tributi/version-20220101/dati-tecnici-tributi-converter.service';
import { DatiTecniciService } from '../../../../services/dati-tecnici/dati-tecnici.service';
import { QuadriTecniciService } from '../../../../services/quadri-tecnici/pratica/quadri-tecnici.service';
import {
  DTPraticaConfig,
  DT_PRATICA_CONFIG,
} from '../../../../utilities/configs/dt-pratica.injectiontoken';
import { IDatiTecniciTributi } from '../../../../utilities/interfaces/dt-tributi-pratica.interfaces';
import { DatiTecniciPraticaComponent } from '../../../core/dati-tecnici-pratica/dati-tecnici-pratica.component';
import { DTTFieldsConfigClass } from './classes/dati-tecnici-tributi.fields-configs';

/**
 * Componente per la gestione del form dati tecnici, ambito: idrico.
 * Il form principale non ha un submit diretto.
 * Questo deve essere gestito dal padre.
 * Le informazioni del form verranno emesse registrandosi all'event emitter onFormChange.
 */
@Component({
  selector: 'dati-tecnici-tributi',
  templateUrl: './dati-tecnici-tributi.component.html',
  styleUrls: ['./dati-tecnici-tributi.component.scss'],
  providers: [],
})
export class DatiTecniciTributiComponent
  extends DatiTecniciPraticaComponent<PraticaDTVo>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti utilizzate comuni. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTT_C = new DatiTecniciTributiConsts();
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();

  /** Input per i dati in caso di modifica */
  @Input('datiTecniciTributi') praticaDT: PraticaDTVo;

  /** DatiTecniciAmbiente che conterrà la configurazione dei dati generati dall'input. */
  datiTecniciTributi: IDatiTecniciTributi;
  /** DatiTecniciAmbiente che conterrà la configurazione dei dati generati dall'input, al suo stato iniziale. */
  datiTecniciTributiInitial: IDatiTecniciTributi;

  /** Array di oggetti UsoLeggeVo recuperata dal server. */
  listaUsiDiLegge: IUsoLeggeVo[] = [];

  /** Classe DTIFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: DTTFieldsConfigClass;

  /**
   * Costruttore.
   */
  constructor(
    @Inject(DT_PRATICA_CONFIG) injConfig: DTPraticaConfig,
    private _formBuilder: FormBuilder,
    private _datiTecnici: DatiTecniciService,
    private _dtaConverter: DatiTecniciTributiConverterService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    pratiche: PraticheService,
    quadriTecnici: QuadriTecniciService,
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
    // Inizializzo le select ed i campi
    this.initConfigs();
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
    this.formInputs = new DTTFieldsConfigClass(this._riscaFormBuilder);
  }

  /**
   * Funzione che prepara possibili dati in input per la gestione di dati utilizzabili dal componente.
   */
  private initDatiTecniciTributi() {
    // Inizializzo il componente
    this.initForms();
    // Iniizializzazioni delle configurazioni
    this.initFieldsConfigs();
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup della struttura della form principale #####
    this.initFormDatiTecniciTributi();
    // NOTA BENE: DA RICHIAMARE COME ULTIMISSIMA CHIAMATA A SEGUITO DI TUTTI I SETUP SUL FORM PER EVITARE CICLI DI CODICE SUPERFLUI
    this.setupDataCheckpoint();
  }

  /**
   * Inizializza le select ed i campi
   */
  private initConfigs() {
    // Recupero l'id dell'ambito
    const idAmbito = this._user.idAmbito;
    // Chiamata al server per il recupero dati per gli usi di legge (null permette di recuperare tutti gli usi di legge)
    this._datiTecnici.getUsiDiLegge(idAmbito).subscribe({
      next: (res: IUsoLeggeVo[]) => {
        // Aggiorno la lista degli usi di legge
        this.listaUsiDiLegge = res;
        // Inizializzo le configurazioni
        this.initFieldsConfigs();
        // #
      },
      error: (e: RiscaServerError) => {
        // Loggo l'errore
        this._logger.error('getUsiDiLegge', e);
        // #
      },
    });
  }

  /**
   * Funzione che definisce le logiche di init per le configurazioni del componente.
   */
  private initFieldsConfigs() {
    // Verifico che esista la configurazione
    if (!this.praticaDT) {
      // Nessuna configurazione
      return;
    }

    // Variabile di comodo
    const c = this.praticaDT;
    // Effettuo la conversione dell'oggetto
    this._dtaConverter.convertPraticaDTVoToDatiTecniciTributi(c).subscribe({
      next: (dtt: IDatiTecniciTributi) => {
        // Assegno l'oggetto convertito
        this.datiTecniciTributi = dtt;
        // Creo una copia per l'oggetto originale
        this.datiTecniciTributiInitial = clone(dtt);
        // Setup di possibili configurazioni in input con valorizzazione dati componente
        this.initComponenteConfigs(this.datiTecniciTributi);
      },
      error: (e: RiscaServerError) => {
        // Loggo l'errore
        this._logger.error('initDatiTecniciTributi', e);
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
        { validators: [Validators.required] }
      ),
      uso: new FormControl(
        { value: undefined, disabled: false },
        { validators: [Validators.required] }
      ),
    });

    // Verifico il flag di disabilitazione della pagina
    if (this.disableUserInputs) {
      // Disabilito la form
      this.mainForm.disable();
    }
  }

  /**
   * #############################
   * FUNZIONI DI GESTIONE DEL FORM
   * #############################
   */

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns T contente le informazioni del form in modalità raw value.
   */
  getMainFormRawValue(c?: any): PraticaDTVo {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }

    // Recupero il dato dal form
    let dtt: IDatiTecniciTributi = this.mainForm.getRawValue();

    // Converto l'oggetto dta in un oggetto PraticaDTVo
    const pdtVo = this._dtaConverter.convertDatiTecniciTributiToPraticaDTVo(
      this.praticaDT,
      dtt
    );

    // Ritorno il dato aggiornato
    return pdtVo;
  }

  /**
   * ########################
   * GESTIONE FORM PRINCIPALE
   * ########################
   */

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
        this.DTT_C.USO,
        usoSelected
      );
    }

    // Inserisco la popolazione
    if (popolazione != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTT_C.POPOLAZIONE,
        popolazione
      );
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */
}
