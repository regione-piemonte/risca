import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { clone, cloneDeep, compact, max, sumBy } from 'lodash';
import * as moment from 'moment';
import { Moment } from 'moment';
import { forkJoin, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import {
  CalcoloCanoneV2Vo,
  CanoneUso,
} from '../../../../../../../../core/commons/vo/calcolo-canone-vo';
import { PraticaDTVo } from '../../../../../../../../core/commons/vo/pratica-vo';
import { RiduzioneAumentoVo } from '../../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { IUnitaMisuraVo } from '../../../../../../../../core/commons/vo/unita-misura-vo';
import { IUsoLeggeSpecificoVo } from '../../../../../../../../core/commons/vo/uso-legge-specifico-vo';
import { IUsoLeggeVo } from '../../../../../../../../core/commons/vo/uso-legge-vo';
import { LoggerService } from '../../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../../../../../core/services/user.service';
import { IUsiLeggePraticaTableConfigs } from '../../../../../../../../shared/classes/risca-table/ambiti/ambito-ambiente/version-20211001/dati-tecnici-ambiente-pratica/usi-legge-pratica.table';
import { UsiLeggeSDTable } from '../../../../../../../../shared/classes/risca-table/ambiti/ambito-ambiente/version-20211001/dati-tecnici-ambiente-sd/usi-legge-sd.table';
import { RiscaTableDataConfig } from '../../../../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../../../../../shared/consts/common-consts.consts';
import {
  canoneECanoneUnitario,
  rateoPrimaAnnualitaDataInizioValidator,
  unitaMisuraQuantitaValidator,
  usiDiLeggeValidi,
  valoreDataEmasIsoRiduzioneR5,
  valoreQuantitaFaldaEAumenti,
  valoreQuantitaFaldaValidator,
} from '../../../../../../../../shared/miscellaneous/forms-validators/dati-tecnici/form-validators.dta';
import {
  arrayContainsAtLeastValidator,
  minOrNullValidator,
  numberCompositionValidator,
} from '../../../../../../../../shared/miscellaneous/forms-validators/forms-validators';
import { RiscaAlertService } from '../../../../../../../../shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from '../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  DynamicObjString,
  FormUpdatePropagation,
  IRiscaAnnoSelect,
  IRiscaCheckboxData,
  MappaErroriFormECodici,
  RiscaAlertConfigs,
  RiscaInfoLevels,
  RiscaRegExp,
  RiscaServerError,
  TipiFrazionamentoCanone,
  TipoCalcoloCanoneADT,
} from '../../../../../../../../shared/utilities';
import { RiscaErrorKeys } from '../../../../../../../../shared/utilities/classes/errors-keys';
import { RiscaErrorsMap } from '../../../../../../../../shared/utilities/classes/errors-maps';
import { RiscaNotifyCodes } from '../../../../../../../../shared/utilities/enums/risca-notify-codes.enums';
import { DatiTecniciAmbienteSDConsts } from '../../../../../../consts/quadri-tecnici/dati-tecnici-ambiente-sd/dati-tecnici-ambiente-sd.consts';
import { PraticheService } from '../../../../../../service/pratiche.service';
import { AutoRiduzioniAumentiDTAmbienteService } from '../../../../services/ambito-ambiente/version-20211001/auto-riduzioni-aumenti-dta.service';
import { DatiTecniciAmbienteService } from '../../../../services/ambito-ambiente/version-20211001/dati-tecnici-ambiente.service';
import { DTAmbienteSDAnnualitaConverterService } from '../../../../services/ambito-ambiente/version-20211001/dt-ambiente-sd-annualita-converter.service';
import { DTAmbienteSDAnnualitaService } from '../../../../services/ambito-ambiente/version-20211001/dt-ambiente-sd-annualita.service';
import { DatiTecniciEventsService } from '../../../../services/dati-tecnici/dati-tecnici-events.service';
import { DatiTecniciService } from '../../../../services/dati-tecnici/dati-tecnici.service';
import { QuadriTecniciSDService } from '../../../../services/quadri-tecnici/stato-debitorio/quadri-tecnici-sd.service';
import {
  DTAnnualitaConfig,
  DT_ANNUALITA_CONFIG,
} from '../../../../utilities/configs/dt-annualita.injectiontoken';
import {
  IDatiUsoLeggeReq,
  IDatiUsoLeggeRes,
  IDTAmbienteASD,
  UsoLeggePSDAmbienteInfo,
} from '../../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { DatiTecniciAmbienteVo } from '../../../../utilities/vo/dati-tecnici-ambiente-vo';
import { DatiTecniciSDComponent } from '../../../core/dati-tecnici-sd/dati-tecnici-sd.component';
import { DTAmbienteASDData } from '../../../middlewares/quadri-tecnici-sd/utilities/quadri-tecnici-sd.component.interfaces';
import { DTASDAFieldsConfigClass } from './utilities/dt-ambiente-sd-annualita.fields-configs';
import {
  GestisciUsoConCanone,
  IParametriCalcolaCanone,
} from './utilities/dt-ambiente-sd-annualita.interfaces';

/**
 * Componente per la gestione del form dati tecnici annualita, ambito: ambiente.
 * Il form principale non ha un submit diretto.
 * Questo deve essere gestito dal padre.
 * Le informazioni del form verranno emesse registrandosi all'event emitter onFormChange.
 */
@Component({
  selector: 'dt-ambiente-sd-annualita',
  templateUrl: './dt-ambiente-sd-annualita.component.html',
  styleUrls: ['./dt-ambiente-sd-annualita.component.scss'],
  providers: [AutoRiduzioniAumentiDTAmbienteService],
})
export class DTAmbienteSDAnnualitaComponent
  extends DatiTecniciSDComponent<DTAmbienteASDData>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti utilizzate comuni. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_SD_C = new DatiTecniciAmbienteSDConsts();
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** RiscaErrorKeys contenente la lista di chiavi d'errore per la gestione dei form. */
  EK = clone(RiscaErrorKeys);

  /** Oggetto RiscaRegExp utilizzate come supporto per la validazione dei campi. */
  private riscaRegExp: RiscaRegExp = new RiscaRegExp();

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertConfigs: RiscaAlertConfigs;

  /** FormGroup che definisce la struttura della sotto form "usi". */
  usiForm: FormGroup;
  /** Array di MappaErroriFormECodici contente la lista degli errori da gestire per usiForm. */
  usiFormErrors: MappaErroriFormECodici[] = [];
  /** Boolean che tiene traccia se il form è stato submittato per: usiForm. */
  usiFormSubmitted = false;
  /** Boolean che definisce lo stato dell'accordion per gli usi di legge. */
  usiApriChiudi = false;

  /** DatiTecniciAmbienteSD che conterrà la configurazione dei dati generati dall'input. */
  datiTecniciAmbienteSD: IDTAmbienteASD;
  /** DatiTecniciAmbienteSD che conterrà la configurazione dei dati generati dall'input, al suo stato iniziale. */
  datiTecniciAmbienteSDInitial: IDTAmbienteASD;

  /** Lista degli anni da mostrare nelle annualità. */
  listaAnni: IRiscaAnnoSelect[] = [];
  /** Array di oggetti UsoLeggeVo recuperata dal server. */
  listaUsiDiLegge: IUsoLeggeVo[] = [];
  /** Array di oggetti UsoLeggeSpecificoVo recuperata dal server tramite informazioni dentro oggetto UsoLeggeVo. */
  listaUsiDiLeggeSpecifici: IUsoLeggeSpecificoVo[] = [];
  /** Array di oggetti RiduzioneAumentoVo recuperata dal server tramite informazioni dentro oggetto UsoLeggeVo. */
  listaPercRiduzione: RiduzioneAumentoVo[] = [];
  /** Array di oggetti RiduzioneAumentoVo recuperata dal server tramite informazioni dentro oggetto UsoLeggeVo. */
  listaPercAumento: RiduzioneAumentoVo[] = [];

  /** Classe DTASDAFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: DTASDAFieldsConfigClass;
  /** Oggetto UsiDiLeggeTable che contiene le informazioni della tabella usi di legge. */
  usiTable: UsiLeggeSDTable;

  /** Boolean che definisce un flag per la gestione dell'abilitazione dell'accordion per la gestione degli usi. */
  public accessibilityAccordionUsi: boolean = true;
  /** Boolean che definisce un flag per la gestione dell'abilitazione del pulsante aggiungi uso. */
  private accessibilityAggiungiUso: boolean = true;
  /** Boolean che definisce un flag per la gestione dell'abilitazione del pulsante calcola canone dati annualità. */
  public accessibilityCalcola: boolean = true;

  /**
   * Costruttore.
   */
  constructor(
    @Inject(DT_ANNUALITA_CONFIG) injConfig: DTAnnualitaConfig,
    private _autoAR: AutoRiduzioniAumentiDTAmbienteService,
    private _datiTecnici: DatiTecniciService,
    private _datiTecniciAmbiente: DatiTecniciAmbienteService,
    private _datiTecniciEvents: DatiTecniciEventsService,
    private _dtaSDAnnualita: DTAmbienteSDAnnualitaService,
    private _dtaSDAConverter: DTAmbienteSDAnnualitaConverterService,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    pratiche: PraticheService,
    quadriTecniciSD: QuadriTecniciSDService,
    private _riscaAlert: RiscaAlertService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Richiamo il super per il setup della classe estesa
    super(
      injConfig,
      logger,
      navigationHelper,
      pratiche,
      quadriTecniciSD,
      riscaFormSubmitHandler,
      riscaUtilities
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit() {
    // Funzione di init per le tabelle del componente
    this.initTabelle();
    // Funzione che converte un oggetto PraticaDTVo in DatiTecniciAmbienteSD
    this.initDatiTecniciAmbienteSD();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Setup degli errori da verificare al submit della form
    this.setupMainFormErrors();
    // Setup delle input del calendario
    this.setupFormInputs();

    // Caricamento dei dati per la lista degli usi di legge
    this.getUsiDiLegge();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [
      ...this.EM.MAP_FORM_GROUP_REQUIRED,
      ...this.EM.MAP_FORMATO_NUMERO_6_INT_4_DEC,
      ...this.EM.MAP_ALMENO_UN_ELEMENTO,
      ...this.EM.MAP_STATO_DEBITORIO,
    ];

    this.usiFormErrors = [...this.EM.MAP_FORM_GROUP_CANONE_CANONE_USO];
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Essendo le configurazioni parecchie, si utilizza una classe di supporto
    this.formInputs = new DTASDAFieldsConfigClass(
      this._riscaFormBuilder,
      this._riscaUtilities
    );
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione che inizializza le tabelle del componente.
   */
  private initTabelle() {
    // Variabili di comodo
    const disableUserInputs = this.disableUserInputs;
    // Definisco un oggetto di configurazione per la tabella
    const c: IUsiLeggePraticaTableConfigs = { disableUserInputs };

    // Setup della tabella usiDiLeggeTab
    this.usiTable = new UsiLeggeSDTable(c);
  }

  /**
   * Funzione che prepara possibili dati in input per la gestione di dati utilizzabili dal componente.
   */
  private initDatiTecniciAmbienteSD() {
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
    this.initFormDTAmbienteAnnualita();
    // Setup della struttura della sotto form usi #####
    this.initFormUsiAnnualita();
    // Setup dei listiner dei form per la manipolazione dei dati
    this.initFormsListeners();

    // Setup di possibili configurazioni in input con valorizzazione dati componente
    this.initComponenteConfigs(this.datiTecniciAmbienteSD, undefined);
  }

  /**
   * Funzione che definisce le logiche di init per le configurazioni del componente.
   */
  private initConfigs() {
    // Verifico che esista la configurazione
    if (!this.praticaDT) {
      // Nessuna configurazione
      this.dtNotDefined();
      // Interrompo il flusso dati
      return;
    }

    // A seconda della modalità di gestione, ci troveremo differenti configurazioni
    if (this.inserimento) {
      // La configurazione deriva dall'inserimento
      this.initConfigsInsert();
      // #
    } else if (this.modifica) {
      // La configurazione deriva dalla modifica
      this.initConfigsUpdate();
      // #
    } else {
      // Configurazione mancante, è un errore
      const e = 'dt-ambiente-sd-annualita.component.ts modalita undefined';
      throw new Error(e);
    }
  }

  /**
   * Inizializzazione dati per l'inserimento dei dati tecnici annualità.
   * L'inserimento ha come sorgente dati iniziale i dati della pratica.
   */
  private initConfigsInsert() {
    // Verifico che esista la configurazione
    if (!this.praticaDT) {
      // Nessuna configurazione
      return;
    }

    // Lancio al funzione di accessibilità per l'inserimento
    this.formAccessibilityInitInsert();

    // Variabile di comodo
    const pDT = this.praticaDT;
    const jsonDT = pDT?.riscossione?.dati_tecnici;
    // Effettuo la conversione dell'oggetto
    this._dtaSDAConverter
      .convertPraticaDTVoToDatiTecniciAmbienteSD(pDT)
      .subscribe({
        next: (dtaSD: IDTAmbienteASD) => {
          // Inizializzo i dati
          this.iniDatiTecniciAmbienteSD(dtaSD, jsonDT);
          // #
        },
        error: (e: RiscaServerError) => {
          // Richiamo la funzione di gestione errori
          this.onServiziErrorAnnualita(e);
          // #
        },
      });
  }

  /**
   * Inizializzazione dati per la modifica dei dati tecnici annualità.
   * La modifica ha come sorgente dati iniziale i dati dell'annualità.
   */
  private initConfigsUpdate() {
    // Verifico che esista la configurazione
    if (!this.annualitaDT) {
      // Nessuna configurazione
      return;
    }

    // Lancio al funzione di accessibilità per l'inserimento
    this.formAccessibilityInitUpdate();

    // Variabile di comodo
    const aDT = this.annualitaDT;
    const jsonDT = aDT?.json_dt_annualita_sd;
    // Effettuo la conversione dell'oggetto
    this._dtaSDAConverter.convertAnnualitaSDVoToDTAmbienteASD(aDT).subscribe({
      next: (dtaSD: IDTAmbienteASD) => {
        // Inizializzo i dati
        this.iniDatiTecniciAmbienteSD(dtaSD, jsonDT);
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione errori
        this.onServiziErrorAnnualita(e);
        // #
      },
    });
  }

  /**
   * Funzione di comodo che inizializza le informazioni del dato tecnico ambiente stato debitorio.
   * @param dtaSD DatiTecniciAmbienteSD come informazione d'inizializzazione dati del componente.
   * @param jsonDT string che definisce il dato tecnico della pratica/annualità.
   */
  private iniDatiTecniciAmbienteSD(dtaSD: IDTAmbienteASD, jsonDT: string) {
    // Assegno l'oggetto convertito
    this.datiTecniciAmbienteSD = dtaSD;
    // Creo una copia per l'oggetto originale
    this.datiTecniciAmbienteSDInitial = cloneDeep(dtaSD);
    // Lancio l'init iniziale per tutti i dati del componente
    this.initComponenteFirstConfigs(this.datiTecniciAmbienteSD, jsonDT);
  }

  /**
   * Funzione specifica adibita al setup del form mainForm.
   */
  private initFormDTAmbienteAnnualita() {
    this.mainForm = this._formBuilder.group(
      {
        rateoPrimaAnnualita: new FormControl(
          { value: false, disabled: this.rateoPrimaAnnualitaDisabled },
          { validators: [] }
        ),
        annualita: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        dataInizio: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        numeroMesi: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        // Per l'annualità questo campo non è obbligatorio
        corpoIdricoCaptazione: new FormControl(
          { value: '', disabled: false },
          { validators: [] }
        ),
        // Per l'annualità questo campo non è obbligatorio
        comune: new FormControl(
          { value: '', disabled: false },
          { validators: [] }
        ),
        nomeImpiantoIdroElettrico: new FormControl(
          { value: '', disabled: false },
          { validators: [] }
        ),
        portataDaAssegnare: new FormControl(
          { value: null, disabled: false },
          {
            validators: [numberCompositionValidator(6, 4)],
          }
        ),
        canoneAnnualita: new FormControl(
          { value: null, disabled: false },
          { validators: [numberCompositionValidator(7, 2)] }
        ),
        gestioneManuale: new FormControl(
          { value: false, disabled: false },
          { validators: [] }
        ),
        usiDiLegge: new FormControl(
          { value: null, disabled: false },
          {
            validators: [arrayContainsAtLeastValidator()],
          }
        ),
      },
      {
        validators: [
          rateoPrimaAnnualitaDataInizioValidator(
            this.DTA_SD_C.RATEO_PRIMA_ANNUALITA,
            this.DTA_SD_C.DATA_INIZIO
          ),
          usiDiLeggeValidi(this.DTA_SD_C.USI_DI_LEGGE),
        ],
      }
    );

    // Verifico il flag di disabilitazione della pagina
    if (this.disableUserInputs) {
      // Disabilito la form
      this.mainForm.disable();
    }
  }

  /**
   * Funzione specifica adibita al setup del form usiForm.
   */
  private initFormUsiAnnualita() {
    this.usiForm = this._formBuilder.group(
      {
        usoDiLegge: new FormControl(
          { value: '', disabled: false },
          { validators: [Validators.required] }
        ),
        usiDiLeggeSpecifici: new FormControl(
          { value: [], disabled: true },
          { validators: [] }
        ),
        /**
         * Per la gestione di input che hanno un modello dati come oggetto, si crea un form group innestato.
         * I form control saranno i nomi delle proprietà dell'oggetto: UnitaMisuraVo.
         */
        unitaDiMisura: this._formBuilder.group({
          des_unita_misura: new FormControl({ value: '', disabled: true }),
          id_unita_misura: new FormControl({ value: -1, disabled: true }),
          ordina_unita_misura: new FormControl({ value: 0, disabled: true }),
          sigla_unita_misura: new FormControl({ value: '', disabled: true }),
        }),
        unitaDiMisuraDesc: new FormControl(
          { value: [], disabled: true },
          { validators: [] }
        ),
        quantita: new FormControl(
          { value: null, disabled: false },
          {
            validators: [
              minOrNullValidator(),
              Validators.pattern(this.riscaRegExp.onlyNumberWDecimal),
              numberCompositionValidator(6, 4),
            ],
          }
        ),
        quantitaFaldaProfonda: new FormControl(
          { value: null, disabled: false },
          {
            validators: [
              minOrNullValidator(),
              Validators.pattern(this.riscaRegExp.onlyNumberWDecimal),
              numberCompositionValidator(6, 4),
            ],
          }
        ),
        percFaldaProfonda: new FormControl(
          { value: '', disabled: true },
          { validators: [] }
        ),
        quantitaFaldaSuperficiale: new FormControl(
          { value: '', disabled: true },
          { validators: [] }
        ),
        percFaldaSuperficiale: new FormControl(
          { value: '', disabled: true },
          { validators: [] }
        ),
        percRiduzioni: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        percAumenti: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        dataScadenzaEmasIso: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        canoneUso: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        canoneUnitarioUso: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
      },
      {
        validators: [
          unitaMisuraQuantitaValidator(
            this.DTA_SD_C.QUANTITA,
            this.DTA_SD_C.UNITA_DI_MISURA
          ),
          valoreQuantitaFaldaValidator(
            this.DTA_SD_C.QUANTITA,
            this.DTA_SD_C.QUANTITA_FALDA_PROFONDA
          ),
          valoreDataEmasIsoRiduzioneR5(
            this.DTA_SD_C.DATA_SCADENZA_EMAS_ISO,
            this.DTA_SD_C.PERC_DI_RIDUZIONE_MOTIVAZIONE
          ),
          valoreQuantitaFaldaEAumenti(
            this.DTA_SD_C.QUANTITA_FALDA_PROFONDA,
            this.DTA_SD_C.PERC_DI_AUMENTO_MOTIVAZIONE
          ),
          canoneECanoneUnitario(
            this.DTA_SD_C.CANONE_USO,
            this.DTA_SD_C.CANONE_UNITARIO_USO
          ),
        ],
      }
    );

    // Verifico il flag di disabilitazione della pagina
    if (this.disableUserInputs) {
      // Disabilito la form
      this.usiForm.disable();
    }
  }

  /**
   * Funzione specifica che gestisce in maniera sequenziale le fasi d'init del componente.
   * @param dta DTAmbienteASD che definisce l'oggetto per la popolazione delle informazioni della form.
   * @param jsonDT string che definisce il dato tecnico della pratica/annualità.
   */
  private initComponenteFirstConfigs(dta: IDTAmbienteASD, jsonDT: string) {
    // Estraggo l'anno per l'init annualità
    const annoSelect = dta?.annualita;
    // Richiamo la funzione di set dell'anno annualità e rimango in ascolto della conclusione
    this.initListeSelect(annoSelect).subscribe({
      next: (lista: IRiscaAnnoSelect[]) => {
        // La lista è già settata dentro la funzione, lancio l'init delle configurazioni componente
        this.initComponenteConfigs(dta, jsonDT);
      },
      error: (e: RiscaServerError) => {
        // Segnalo l'errore tramite servizio
        this.onServiziErrorAnnualita(e);
      },
    });
  }

  /**
   * Popola i campi della form in base all'oggetto da modificare che é stato passato.
   * @param dta DTAmbienteASD che definisce l'oggetto per la popolazione delle informazioni della form.
   * @param jsonDT string che definisce il dato tecnico della pratica/annualità.
   */
  private initComponenteConfigs(dta: IDTAmbienteASD, jsonDT: string) {
    // verifico esistano configurazioni
    if (!dta) {
      // Niente configurazioni
      return;
    }

    if (dta.comune != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_SD_C.COMUNE,
        dta.comune,
        { emitEvent: false }
      );
    }

    if (dta.corpoIdricoCaptazione != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_SD_C.CORPO_IDRICO_CAPTAZIONE,
        dta.corpoIdricoCaptazione,
        { emitEvent: false }
      );
    }

    if (dta.nomeImpiantoIdroElettrico != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_SD_C.NOME_IMPIANTO_IDROELETTRICO,
        dta.nomeImpiantoIdroElettrico,
        { emitEvent: false }
      );
    }

    this.initRateoPrimaAnnualita(dta.rateoPrimaAnnualita);

    if (dta.dataInizio != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_SD_C.DATA_INIZIO,
        dta.dataInizio,
        { emitEvent: false }
      );
    }

    if (dta.numeroMesi != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_SD_C.NUMERO_MESI,
        dta.numeroMesi,
        { emitEvent: false }
      );
    }

    if (dta.portataDaAssegnare != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_SD_C.PORTATA_DA_ASSEGNARE,
        dta.portataDaAssegnare,
        { emitEvent: false }
      );
    }

    if (dta.canoneAnnualita != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_SD_C.CANONE_ANNUALITA,
        this._riscaUtilities.mathTruncate(dta.canoneAnnualita, 2),
        { emitEvent: false }
      );
    }

    if (dta.gestioneManuale != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_SD_C.GESTIONE_MANUALE,
        dta.gestioneManuale,
        { emitEvent: false }
      );
      // Aggiorno il source della configurazione dell'input della checkbox
      this._riscaUtilities.updateRFICheckboxSource(
        this.formInputs.gestioneManualeConfig,
        dta.gestioneManuale
      );
    }

    // Popolo le liste degli usi di legge
    if (dta.usiDiLegge != undefined) {
      // Resetto gli usi di legge
      this.usiTable.clear();
      // L'input di partenza è quello della pratica, ma essendo compatibili gli oggetti, forzo il parsing
      const usiInfo: UsoLeggePSDAmbienteInfo[] = dta.usiDiLegge;
      // Lancio il calcolo dei canoni
      this.initUsiTable(usiInfo);
    }
  }

  /**
   * Funzione che esegue l'init per la tabella degli usi.
   * A seconda della modalità, verranno gestite in maniera differenti alcune configurazioni.
   * In inserimento: la tabella sarà bloccata per la cancellazione; l'utente deve confermare i dati dell'annualità. "Canone" e "Canone unitario" saranno vuoti fino alla prima conferma dei dati dell'annualità.
   * In modifica: nessuna gestione particolare. La modalità di ogni uso verrà considerata "automatica".
   * @param usiInfo UsoLeggePSDInfo[] cone le informazioni da inserire nella tabella.
   */
  private initUsiTable(usiInfo: UsoLeggePSDAmbienteInfo[]) {
    // Verifico l'input
    usiInfo = usiInfo?.length >= 0 ? usiInfo : [];
    // Variabili per la gestione della tabella
    let tipoCalcoloCanone: TipoCalcoloCanoneADT;

    // Verifico la modalità in cui mi trovo
    if (this.inserimento) {
      // Nell'inizializzazione per l'inserimento inibisco i pulsanti della tabella
      this.usiTable.disableActions = true;
      // In inserimento non è definita la modalità di calcolo canone all'inizio
      tipoCalcoloCanone = TipoCalcoloCanoneADT.inserimento;
      // #
    } else if (this.modifica) {
      // In modifica per default e decisione con gli analisti, la modalità è automatica per tutti gli usi
      tipoCalcoloCanone = TipoCalcoloCanoneADT.automatico;
    }

    // Creo una nuova istanza degli oggetti dell'array mappando la proprietà della tipologia di canone
    const usiInfoTC = usiInfo.map((ui: UsoLeggePSDAmbienteInfo) => {
      // Creo la copia del dato
      const uiClone = cloneDeep(ui);
      // Definisco la tipologia di calcolo canone
      uiClone.tipoCalcoloCanone = tipoCalcoloCanone;
      // Ritorno l'oggetto
      return uiClone;
    });
    // Vado a settare i nuovi dati all'interno della tabella
    this.usiTable.setNewElements(usiInfoTC);

    // Aggiorno il form con i dati della tabella
    this.aggiornaUsiLeggeInForm();
    // Aggiorno la struttura della listaUsiDiLegge disabilitando le voci prensenti negli usi di legge
    this.disabilitaUsiDiLeggeInseriti();
  }

  /**
   * Funzione di setup per le liste per le select del form.
   * @param annoAnnualita IRiscaAnnoSelect che definisce l'anno dell'annualità selezione. Verrà escloso dai blocchi e verrà impostato come valore di default nella form.
   * @returns Observable<IRiscaAnnoSelect[]> contenente la lista degli anni per la select.
   */
  private initListeSelect(
    annoAnnualita?: IRiscaAnnoSelect
  ): Observable<IRiscaAnnoSelect[]> {
    // Recupero l'anno dall'annualità
    const anno = annoAnnualita?.anno;
    // Recupero l'ambito di riferimento
    const idAmbito = this._user.idAmbito;

    // Genero la lista degli anni nella select 'anni'
    return this._dtaSDAnnualita
      .generaListaAnnualita(idAmbito, this.statoDebitorio, undefined, anno)
      .pipe(
        tap((lista: IRiscaAnnoSelect[]) => {
          // Assegno la lista degli anni generata
          this.listaAnni = lista;
          // Setto l'annualità di default
          this.initAnnualitaDefault(annoAnnualita);
          // Imposto un minimo e un massimo per il calendario della data inizio
          this.setMinMaxDataInizio(annoAnnualita);
          // #
        })
      );
  }

  /**
   * Funzione che setta l'annualità di default subito dopo lo scarico della lista degli anni.
   * Se non ho un valore presente nell'annualità, prende la data maggiore.
   * @param annoAnnualita IRiscaAnnoSelect che definisce l'oggetto dell'annualità di default.
   */
  private initAnnualitaDefault(annoAnnualita?: IRiscaAnnoSelect) {
    // Prendo l'annualità presente
    const annualita = annoAnnualita ?? this.annoAnnualita;
    // Definisco la proprietà per l'anno da settare
    let anno = annualita ? annualita.anno : undefined;

    // Verifico se non c'è nun anno di default
    if (!anno) {
      // Non c'è, estraggo l'anno non disabilitato più recente/futuro
      const anniSel: IRiscaAnnoSelect[] = this.listaAnni ?? [];
      // Estraggo solo gli anni d'interesse
      const anniOk: number[] = anniSel
        // Filtro recuperdando solo gli anni abilitati
        .filter((aSel: any | IRiscaAnnoSelect) => !aSel.__disabled)
        // Estraggo solo gli anni dagli oggetti
        .map((aOk: IRiscaAnnoSelect) => aOk.anno);

      // Recupero il valore maggiore oppure undefined
      anno = max(anniOk);
    }

    // Variabili di comodo per la gestione di select e dato nel form
    const mf = this.mainForm;
    const k = this.DTA_SD_C.ANNUALITA;
    const l = this.listaAnni;
    const v = { anno };
    const f = (a: IRiscaAnnoSelect, b: IRiscaAnnoSelect) => {
      return b.anno == a.anno;
    };
    const o: FormUpdatePropagation = { emitEvent: false };
    // Setto l'annualità massima trovata
    this._riscaUtilities.setFormValueAndSelect(mf, k, l, v, f, o);
  }

  /**
   * Funzione che imposta una data minima e massima per il calendario per "Data inizio".
   * Il campo dovrà essere limitato per evitare errori nel calcolo del frazionamento.
   * @param annoAnnualita IRiscaAnnoSelect che definisce l'oggetto dell'annualità di default.
   */
  private setMinMaxDataInizio(annoAnnualita?: IRiscaAnnoSelect) {
    // Essendo configurazioni innestate, gestisco in sicurezza la logica con un trycatch
    try {
      // Verifico se esiste l'input
      if (!annoAnnualita) {
        // Non c'è la configurazione, resetto le configurazioni
        this.formInputs.dataInizioConfig.data.minDate = undefined;
        this.formInputs.dataInizioConfig.data.maxDate = undefined;
        // #
      } else {
        // Esiste un anno di riferimento, definisco una data minima e massima rispetto all'anno definito
        const anno: number = annoAnnualita.anno;
        // Definisco gli oggetti per il settaggio
        const firstOfYearMoment: Moment = moment([anno]);
        const lastOfYearMoment: Moment = moment([anno]).endOf('years');
        // Converto gli oggetti moment in oggetti NgbDateStruct
        const min: NgbDateStruct =
          this._riscaUtilities.convertMomentToNgbDateStruct(firstOfYearMoment);
        const max: NgbDateStruct =
          this._riscaUtilities.convertMomentToNgbDateStruct(lastOfYearMoment);
        // Assegno i valori per le configurazioni
        this.formInputs.dataInizioConfig.data.minDate = min;
        this.formInputs.dataInizioConfig.data.maxDate = max;
      }
    } catch (e) {}
  }

  /**
   * Funzione che inizializza la messaggistica utente per determinate casistiche.
   * @param annoAnnualita number con l'anno dell'annualità selezionata.
   */
  private setInfoUtente(annoAnnualita: number) {
    // Recupero la data scadenza concessione
    const dataScadConc: Moment = this.dataScadenzaConcessione;
    const dataSCExist: boolean = dataScadConc != undefined;
    const dataSCSameAnnualita: boolean =
      dataSCExist && annoAnnualita === dataScadConc.year();

    // Verifico se esiste la data scadenza concessione
    if (dataSCSameAnnualita) {
      // Converto la data per visualizzarla
      const dtSCView: string =
        this._riscaUtilities.convertMomentToViewDate(dataScadConc);
      // La data esiste, definisco un alert specifico per l'utente
      const msg: string = `Per l'annualità selezionata viene applicato il frazionamento in base alla data scadenza concessione salvata: ${dtSCView}`;

      // Creo un alert e lo assegno alla variabile locale
      this.alertConfigs = new RiscaAlertConfigs({
        allowAlertClose: true,
        messages: [msg],
        type: RiscaInfoLevels.info,
      });
    }
  }

  /**
   * Funzione di comodo per l'inizializzazione del flag del rateo prima annualità.
   * @param rateoPrimaAnnualita IRiscaCheckboxData con il dato del rateo prima annualità.
   */
  private initRateoPrimaAnnualita(rateoPrimaAnnualita?: IRiscaCheckboxData) {
    // Verifico il rateo prima annualita
    let rateo: IRiscaCheckboxData = {
      id: this.DTA_SD_C.LABEL_FLAG_RATEO_PRIMA_ANNUALITA,
      label: this.DTA_SD_C.LABEL_FLAG_RATEO_PRIMA_ANNUALITA,
      value: false,
      check: false,
    };
    // Se esiste la configurazione, sovrascrivo il contenitore
    rateo = rateoPrimaAnnualita ?? rateo;
    // Aggiorno il dato del form
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.DTA_SD_C.RATEO_PRIMA_ANNUALITA,
      rateo
    );
    // Aggiorno il source della configurazione dell'input della checkbox
    this._riscaUtilities.updateRFICheckboxSource(
      this.formInputs.rateoPrimaAnnuallitaConfig,
      rateo
    );
    // Richiamo la funzione per il cambio rateo
    this.onRateoPAChange(rateo);
  }

  /**
   * Funzione specifica adibita al setup dei listner delle form.
   * La funzione registrerà diversi eventi sulle form, atte a gestire la logica e la manipolazione dei dati.
   */
  private initFormsListeners() {
    // Sottoscrivo gli eventi al cambio dell'annualita
    this.mainForm
      .get(this.DTA_SD_C.ANNUALITA)
      .valueChanges.subscribe((annualita: IRiscaAnnoSelect) => {
        // Richiamo la funzione per gestire il cambio dell'annualità
        this.onAnnualitaChange(annualita);
        // #
      });

    // Sottoscrivo gli eventi al check del flagRateoPrimaAnnualita
    this.mainForm
      .get(this.DTA_SD_C.RATEO_PRIMA_ANNUALITA)
      .valueChanges.subscribe((rateoPrimaAnnualita: IRiscaCheckboxData) => {
        // Richiamo al funzione per gestire il cambio del rateo
        this.onRateoPAChange(rateoPrimaAnnualita);
        // #
      });

    // Listener al cambio del flag gestione manuale
    this.mainForm
      .get(this.DTA_SD_C.GESTIONE_MANUALE)
      .valueChanges.subscribe((gestioneManuale: IRiscaCheckboxData) => {
        // Lancio l'evento di pulizia dati
        this.cleanDati();
        // Manipolo, a seconda della gestione manuale, i dati degli usi inseriti
        this.aggiornaRiduzioniAumentiUsi(gestioneManuale);
        // Resetto la form degli usi
        this.resetUsiForm();
        // Aggiorno il form con i dati della tabella
        this.aggiornaUsiLeggeInForm();
        // Alla fine della manipolazione, aggiorno il calcolo canone
        this.calcolaCanoniUsiAttuali();
      });

    // Listener al cambio di un uso di legge, verranno popolate le informazioni dei campi connessi
    this.usiForm
      .get(this.DTA_SD_C.USO_DI_LEGGE)
      .valueChanges.subscribe((usoDiLeggeUpd: IUsoLeggeVo) => {
        // Lancio la funzione di gestione dei dati tecnici a seguito della selezione di un uso di legge
        this.onUsoLeggeSelected(usoDiLeggeUpd);
      });

    // Listener al cambio della quantità, verranno calcolati i relativi valori
    this.usiForm.get(this.DTA_SD_C.QUANTITA).valueChanges.subscribe(() => {
      // Lancio la funzione di calcolo per le quantita per il pannello usi
      this._datiTecniciAmbiente.calcolaQuantitaPerUsi(this.usiForm);
    });

    // Listener al cambio della quantità, verranno calcolati i relativi valori
    this.usiForm
      .get(this.DTA_SD_C.QUANTITA_FALDA_PROFONDA)
      .valueChanges.subscribe(() => {
        // Lancio la funzione di calcolo per le quantita per il pannello usi
        this._datiTecniciAmbiente.calcolaQuantitaPerUsi(this.usiForm);
      });
  }

  /**
   * #####################################################
   * FUNZIONI DI GESTIONE DEGLI EVENTI CANONE E CANONE USO
   * #####################################################
   */

  /**
   * Funzione invocata al cambio del valore della select per l'annualità.
   * @param annualita IRiscaAnnoSelect con l'anno selezionato.
   */
  private onAnnualitaChange(annualita: IRiscaAnnoSelect) {
    // Verifico se sono in inserimento
    if (this.inserimento) {
      // Vado a gestire le impostazioni per il flag rateo prima annualità, resettando il valore
      this.initRateoPrimaAnnualita();
      // Lancio il setup per i range di selezione date
      this.setMinMaxDataInizio(annualita);
    }

    // Per una questione di DIGEST (non ho tempo di gestire perfettamente il flusso) uso un timeout
    setTimeout(() => {
      // Gestisco la visualizzazione per la messaggistica utente
      this.setInfoUtente(annualita.anno);
      // #
    }, 500);
  }

  /**
   * Funzione invocata al cambio del flag rateo prima annualità.
   * @param rateoPrimaAnnualita IRiscaCheckboxData con il valore del campo.
   */
  private onRateoPAChange(rateoPrimaAnnualita: IRiscaCheckboxData) {
    // Prendo il formcontrol di anno
    const dataInizioFC = this.mainForm.get(this.DTA_SD_C.DATA_INIZIO);
    // Recupero il valore del rateo
    const rateo = rateoPrimaAnnualita?.check;

    // Verifico se sono in inserimento
    if (this.inserimento) {
      // Controllo se rateo prima annualità è true
      if (rateo) {
        // Abilito il formcontrol di data inizio
        dataInizioFC?.enable();
        // #
      } else {
        // Disabilito e svuoto il form control
        dataInizioFC?.reset();
        dataInizioFC?.disable();
      }
      // #
    }
  }

  /**
   * ########################################
   * GESTIONE ACCESSIBILITA' FORM INSERIMENTO
   * ########################################
   */

  /**
   * Funzione che gestisce l'accessibilità del form in inserimento all'inizializzazione del componente.
   * L'inizializzazione del form in inserimento prevede un flusso specifico d'interazione:
   * - L'utente deve compilare prima i campi del form per la sezione "Dati annualità";
   * - Il form rimarra praticamente tutto disabilitato fino a che l'utente non preme il pulsante "Calcola";
   */
  private formAccessibilityInitInsert() {
    // Definisco l'oggetto per non lanciare l'emit del dato
    const o: FormUpdatePropagation = { emitEvent: false };
    // Abilito le parti di maschera per i dati dell'annualità
    this.annualitaFC?.enable(o);
    this.rateoPrimaAnnualitaFC?.enable(o);
    this.accessibilityCalcola = true;
    // Disabilito le parti di maschera per i dati tecnici
    this.portataDaAssegnareFC?.disable(o);
    this.canoneAnnualitaFC?.disable(o);
    this.gestioneManualeFC?.disable(o);
    this.usiApriChiudi = false;
    this.accessibilityAccordionUsi = false;
    this.accessibilityAggiungiUso = false;
    if (this.usiTable) {
      // Disabilito le azioni della tabella
      this.usiTable.disableActions = true;
    }
  }

  /**
   * Funzione che gestisce l'accessibilità del form in inserimento, a seguito del pulsante "Calcola".
   * L'inizializzazione del form in inserimento prevede un flusso specifico d'interazione:
   * - Premuto il pulsante "Calcola", l'utente avrà possibilità di modificare anche le altre informazioni, ma non più quelle per "Dati annualità".
   */
  private formAccessibilityCalcolaInsert() {
    // Definisco l'oggetto per non lanciare l'emit del dato
    const o: FormUpdatePropagation = { emitEvent: false };
    // Disabilito le parti di maschera per i dati dell'annualità
    this.annualitaFC?.disable(o);
    this.rateoPrimaAnnualitaFC?.disable(o);
    this.dataInizioFC?.disable(o);
    this.accessibilityCalcola = false;
    // Abilito le parti di maschera per i dati tecnici
    this.portataDaAssegnareFC?.enable(o);
    this.canoneAnnualitaFC?.enable(o);
    this.gestioneManualeFC?.enable(o);
    this.accessibilityAccordionUsi = true;
    this.accessibilityAggiungiUso = true;
    if (this.usiTable) {
      // Disabilito le azioni della tabella
      this.usiTable.disableActions = false;
    }
  }

  /**
   * Funzione che gestisce l'accessibilità del form in modifica.
   * In modifica le informazioni sull'annualità non sono modificabili.
   */
  private formAccessibilityInitUpdate() {
    // Definisco l'oggetto per non lanciare l'emit del dato
    const o: FormUpdatePropagation = { emitEvent: false };
    // Disabilito le parti di maschera per i dati dell'annualità
    this.annualitaFC?.disable(o);
    this.rateoPrimaAnnualitaFC?.disable(o);
    this.dataInizioFC?.disable(o);
  }

  /**
   * #######################################
   * FUNZIONI DI GESTIONE DEGLI USI DI LEGGE
   * #######################################
   */

  /**
   * Funzione adibita al recupero dati dal server per gli usi di legge.
   */
  private getUsiDiLegge() {
    // Recupero l'id dell'ambito
    const idAmbito = this._user.idAmbito;
    // Chiamata al server per il recupero dati per gli usi di legge (null permette di recuperare tutti gli usi di legge)
    this._datiTecnici.getUsiDiLegge(idAmbito).subscribe({
      next: (res: IUsoLeggeVo[]) => {
        // Aggiorno la lista degli usi di legge
        this.listaUsiDiLegge = res;
        // Richiamo la lista per bloccare gli usi di legge
        this.disabilitaUsiDiLeggeInseriti();
        // Verifico se esiste già l'oggetto della form
        if (this.usiForm !== undefined) {
          // Pulisco lo stato del form
          this.usiForm.markAsPristine();
        }
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione errori
        this.onServiziErrorAnnualita(e);
        // #
      },
    });
  }

  /**
   * ####################################################
   * FUNZIONI COLLEGATE ALLA SELEZIONE DI UN USO DI LEGGE
   * ####################################################
   */

  /**
   * Funzione che recupera tutte le informazioni collegate ad un uso di legge, una volta selezionato dalla tendina.
   * @param usoDiLegge UsoLeggeVo per il recupero delle informazioni collegate.
   */
  private onUsoLeggeSelected(usoDiLegge: IUsoLeggeVo) {
    // Controllo che l'oggetto esista e abbia le informazioni per lo scarico dei dati connessi
    if (!usoDiLegge || !usoDiLegge.id_tipo_uso) {
      // Blocco il flusso
      return;
    }

    // Recupero il valore della gestione manuale
    const gestioneManuale = this.isGestioneManuale;
    // Genero la richiesta per lo scarico delle informazioni relative all'uso di legge.
    let usoLeggeReq: IDatiUsoLeggeReq;
    usoLeggeReq = this.datiCorrelatiUsoLeggeReq(usoDiLegge, gestioneManuale);

    // Effettuo le chiamate
    this.usoLeggeSelectedReq(usoLeggeReq).subscribe({
      error: (e: RiscaServerError) => {
        // Segnalo l'errore tramite servizio
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione che effettua le chiamate per lo scarico dati.
   * A seguito dello scarico, esegue le logiche di aggiornamento dati nel form degli usi di legge.
   * E' necessario effettuare la subscribe alla funzione per poterla lanciare.
   * @param usoLeggeReq IDatiUsoLeggeReq con la richiesta dati da eseguire.
   * @returns Observable<IDatiUsoLeggeRes> con le informazioni scaricate e le strutture aggiornate.
   */
  private usoLeggeSelectedReq(
    usoLeggeReq: IDatiUsoLeggeReq
  ): Observable<IDatiUsoLeggeRes> {
    // Effettuo la chiamata in forkjoin delle richieste e ritorno il risultato
    return forkJoin(usoLeggeReq).pipe(
      tap((datiUso: IDatiUsoLeggeRes) => {
        // Recupero le informazioni dalla response
        const { usiSpecifici, unitaMisura, riduzioni, aumenti } = datiUso;

        // Chiamo la funzione per la gestione di usi di legge specifici
        this.onScaricoUsiLeggeSpecifici(usiSpecifici);
        // Chiamo la funzione per la gestione dell'unita di misura
        this.onScaricoUnitaDiMisura(unitaMisura);
        // Chiamo la funzione per la gestione delle motivazioni con percentuale riduzione
        this.onScaricoPercRiduzioni(riduzioni);
        // Chiamo la funzione per la gestione delle motivazioni con percentuale aumento
        this.onScaricoPercAumenti(aumenti);
      })
    );
  }

  /**
   * Funzione adibita al setup delle informazioni correlate ad un uso di legge, per gli usi specifici.
   * @param usiSpecifici Array di UsoLeggeSpecificoVo contenente i dati recuperati dal servizio.
   */
  private onScaricoUsiLeggeSpecifici(usiSpecifici: IUsoLeggeSpecificoVo[]) {
    // Aggiorno la lista degli usi di legge specifici
    this.listaUsiDiLeggeSpecifici = usiSpecifici || [];

    // Controllo che ci siano dei valori selezionabili
    if (this.listaUsiDiLeggeSpecifici?.length > 0) {
      // Abilito la multi select tramite chiave form control
      this.usiForm.get(this.DTA_SD_C.USI_DI_LEGGE_SPECIFICI).enable();
    } else {
      // Disabilito la multi select tramite chiave form control
      this.usiForm.get(this.DTA_SD_C.USI_DI_LEGGE_SPECIFICI).disable();
    }
  }

  /**
   * Funzione adibita al setup delle informazioni correlate ad un uso di legge, per l'unità di misura.
   * @param unita UnitaMisuraVo contenente i dati recuperati dal servizio.
   */
  private onScaricoUnitaDiMisura(unita: IUnitaMisuraVo) {
    // Richiamo la funzione di set del dato
    this.setUnitaDiMisuraInForm(unita);
  }

  /**
   * Funzione adibita al setup delle informazioni correlate ad un uso di legge, per gli usi specifici.
   * @param riduzioni Array di RiduzioneAumentoVo contenente i dati recuperati dal servizio.
   */
  private onScaricoPercRiduzioni(riduzioni: RiduzioneAumentoVo[]) {
    // Aggiorno la lista degli usi di legge specifici
    this.listaPercRiduzione = riduzioni || [];

    // Controllo che ci siano dei valori selezionabili
    if (this.listaPercRiduzione?.length > 0) {
      // Abilito la multi select tramite chiave form control
      this.usiForm.get(this.DTA_SD_C.PERC_DI_RIDUZIONE_MOTIVAZIONE).enable();
    } else {
      // Disabilito la multi select tramite chiave form control
      this.usiForm.get(this.DTA_SD_C.PERC_DI_RIDUZIONE_MOTIVAZIONE).disable();
    }
  }

  /**
   * Funzione adibita al setup delle informazioni correlate ad un uso di legge, per gli usi specifici.
   * @param aumenti Array di RiduzioneAumentoVo contenente i dati recuperati dal servizio.
   */
  private onScaricoPercAumenti(aumenti: RiduzioneAumentoVo[]) {
    // Aggiorno la lista degli usi di legge specifici
    this.listaPercAumento = aumenti;

    // Controllo che ci siano dei valori selezionabili
    if (this.listaPercAumento?.length > 0) {
      // Abilito la multi select tramite chiave form control
      this.usiForm.get(this.DTA_SD_C.PERC_DI_AUMENTO_MOTIVAZIONE).enable();
    } else {
      // Disabilito la multi select tramite chiave form control
      this.usiForm.get(this.DTA_SD_C.PERC_DI_AUMENTO_MOTIVAZIONE).disable();
    }
  }

  /**
   * Funzione che gestisce, al cambio delle informazioni, i dati per gli usi.
   * Se la gestione manuale viene disattivata, andranno rimossi tutti gli usi di legge automatici.
   * @param gestioneManuale IRiscaCheckboxData con lo stato della checkbox per la gestione manuale.
   */
  private aggiornaRiduzioniAumentiUsi(gestioneManuale: IRiscaCheckboxData) {
    // Verifico l'input
    if (!gestioneManuale) {
      // Niente logiche
      return;
    }

    // Recupero gli usi di legge attualmente inseriti
    let usi: UsoLeggePSDAmbienteInfo[] = this.usiTable.getDataSource();
    // Verifico ci siano degli elementi
    if (usi?.length > 0) {
      // Aggiorno la tabella rimuovendo riduzioni/aumenti automatici
      usi = this._autoAR.rimuoviRiduzioniAumentiAutomaticiUsi(usi);
      // Ripulisco la tabella
      this.usiTable.clear();
      // Inserisco i dati aggiornati
      this.usiTable.setElements(usi);
    }
  }

  /**
   * ############################################
   * FUNZIONI DI UTILITY GENERICHE DEL COMPONENTE
   * ############################################
   */

  /**
   * Funzione di comodo per la gestione degli errori dei servizi.
   */
  onServiziErrorAnnualita(e: RiscaServerError) {
    // Emetto l'errore tramite evento
    this._datiTecniciEvents.erroreServiziAnnualita(e);
  }

  /**
   * Funzione invocata quando l'evento dei dati tecnici viene attivato.
   * @param praticaDTVo PraticaDTVo con i dati tecnici aggiornati.
   * @override
   */
  datiTecniciUpdated(praticaDTVo: PraticaDTVo) {
    // Assegno localmente i dati aggiornati
    this.praticaDT = praticaDTVo;
    // Richiamo il processo di aggiornamento dei dati
    this.initConfigs();
  }

  /**
   * Funzione di comodo che genera un oggetto PraticaDTVo, dato in input il json dato tecnico per il calcolo canone.
   * @param jsonDT string che definisce il json dei dati tecnici per il calcolo canone.
   * @returns PraticaDTVo come oggetto per il calcolo canone.
   */
  private generaPraticaDTVo(jsonDT: string): PraticaDTVo {
    // Genero l'oggetto
    const praticaDTVo: PraticaDTVo = {
      riscossione: {
        data_inserimento: null,
        data_modifica: null,
        gest_UID: null,
        id_riscossione: null,
        dati_tecnici: jsonDT,
      },
    };
    // Ritorno l'oggetto generato
    return praticaDTVo;
  }

  /**
   * Funzione di supporto che recupera le informazioni e lancia il calcolo del canone totale e dei canoni usi attualmente definiti.
   * @param disableCCFields boolean che definisce se, a seguito del calolo canone, bisogna disattivare le input annualità, rate e data inizio. Per default è: false.
   */
  private calcolaCanoniUsiAttuali(disableCCFields: boolean = false) {
    // Recupero l'oggetto della form con i dati, forzando il typing
    const annualitaSD: IDTAmbienteASD = this.mainForm.getRawValue();
    // Lancio la funzione di preparazione e calcolo
    this.prepareAndCalcolaCanoni(annualitaSD, disableCCFields);
  }

  /**
   * Funzione di comodo che gestisce la preparazione dati per il calcolo canone usi, e poi esegue il calcolo.
   * @param annualitaSD DTAmbienteASD come oggetto di riferimento per il calcolo canone.
   * @param disableCCFields boolean che definisce se, a seguito del calolo canone, bisogna disattivare le input annualità, rate e data inizio. Per default è: false.
   */
  private prepareAndCalcolaCanoni(
    annualitaSD: IDTAmbienteASD,
    disableCCFields: boolean = false
  ) {
    // Dall'oggetto recuperato estraggo gli usi di legge
    let datiUsi: UsoLeggePSDAmbienteInfo[] = annualitaSD?.usiDiLegge ?? [];

    // Lancio la conversione delle informazioni in formato json
    let dtVo: DatiTecniciAmbienteVo;
    dtVo =
      this._dtaSDAConverter.convertDTAmbienteASDToDatiTecniciVo(annualitaSD);
    // Converto l'oggetto in stringa
    const jsonDT = JSON.stringify(dtVo);

    // Richiamo la funzione di calcolo dei canoni
    this.calcolaCanoni(datiUsi, jsonDT, disableCCFields);
  }

  /**
   * Funzione di supporto che esegue le logiche per i calcoli del canone dell'annualità e degli usi di legge.
   * @param usiInfo UsoLeggePSDInfo[] con le informazioni degli usi gestiti dalla form usiLegge.
   * @param jsonDT string che definisce il dato tecnico della pratica/annualità.
   * @param disableCCFields boolean che definisce se, a seguito del calolo canone, bisogna disattivare le input annualità, rate e data inizio. Per default è: false.
   */
  private calcolaCanoni(
    usiInfo: UsoLeggePSDAmbienteInfo[],
    jsonDT: string,
    disableCCFields: boolean = false
  ) {
    // Verifico le informazioni in input
    const noUsiInfo = !usiInfo || usiInfo.length === 0;
    const noJsonDT = !jsonDT;

    // Verifico le casistiche di gestione
    if (noUsiInfo && !noJsonDT) {
      // Non esistono usi, ma il dato tecnico è presente, magari la lista usi è stata cancellata tutta manualmente, aggiorno il canone annualità
      this.canoneAnnualita = 0;
      // #
    } else if (!usiInfo || usiInfo.length === 0 || !jsonDT) {
      // Niente configurazioni
      return;
      // #
    }

    // Creo un oggetto praticaDTVo per il calcolo canono
    const pDTVo = this.generaPraticaDTVo(jsonDT);
    // Richiamo la funzione pe generare i parametri per il calcolo canone
    const paramsCC: IParametriCalcolaCanone = this.parametriCalcolaCanone();

    // Verifico se è stato generato un errore dalla generazione dei parametri
    if (paramsCC.errorCode) {
      // Definisco la proprietà per la generazione dell'errore
      const code = paramsCC.errorCode;
      // Genero un errore e lo visualizzo
      this.onServiziErrorAnnualita(new RiscaServerError({ error: { code } }));
      // Blocco il flusso
      return;
    }

    // Recupero la data di riferimento per il calcolo canone
    const dataRif: string = paramsCC.dataRif;
    // Recupero le possibili informazioni per il frazionamento
    const flgFraz: TipiFrazionamentoCanone = paramsCC.flgFraz;
    const dataFraz: Moment = paramsCC.dataFraz;

    // Richiamo il servizio per il calcolo canone
    this._datiTecnici
      .calcoloCanoneAnnualita(pDTVo, dataRif, dataFraz, flgFraz)
      .subscribe({
        next: (canoneAnnualita: CalcoloCanoneV2Vo) => {
          // Verifico la configurazione per la disabilitazione dei campi
          if (disableCCFields) {
            // Richiamo la funzione per l'abilitazione della maschera
            this.formAccessibilityCalcolaInsert();
            // #
          }
          // Richiamo la funzione al calcolo annualità
          this.onCalcoloAnnualita(canoneAnnualita, usiInfo);
          // #
        },
        error: (e: RiscaServerError) => {
          // Richiamo la funzione di gestione errori
          this.onServiziErrorAnnualita(e);
          // #
        },
      });
  }

  /**
   * Funzione che contiene le logiche per gestire i parametri per la gestione per il calcolo dei canoni.
   * RISCA-ISSUES-43: Il calcolo canone dipende dalle informazioni relative a:
   * - Data scadenza concessione (pratica);
   * - Flg rateo prima annualità + data inizio (annualità);
   * Si sono riveste le logiche per gestire quale informazione deve essere usata per il calcolo canone.
   * @returns IParametriCalcolaCanone con l'oggetto con i parametri per la gestione del calcolo canone.
   */
  private parametriCalcolaCanone(): IParametriCalcolaCanone {
    // Definisco l'oggetto base per il ritorno dei parametri
    let paramsCC: IParametriCalcolaCanone = {
      dataRif: undefined,
      flgFraz: undefined,
      dataFraz: undefined,
    };

    // Definisco le variabili per gestire la casistiche
    const inserimento: boolean = this.inserimento;
    const modifica: boolean = this.modifica;
    // Recupero la data di riferimento per il calcolo canone
    const dataRif: Moment = this.dataRiferimentoCanoneMoment;
    const dataRifStr: string =
      this._riscaUtilities.convertMomentToServerDate(dataRif);
    // Recupero le possibili informazioni per il frazionamento
    const rateoPrimaAnn: TipiFrazionamentoCanone =
      this.rateoPrimaAnnualitaCanone;
    const dataInizioCC: Moment = this.dataInizioCalcoloCanone;
    const dataScadConc: Moment = this.dataScadenzaConcessione;
    // Verifico le condizioni per la data scadenza della riscossione
    const dataSCExist: boolean = dataScadConc != undefined;
    const annoAnnualita: number = this.annoAnnualita.anno;
    const dataSCSameAnnualita: boolean =
      dataSCExist && annoAnnualita === dataScadConc.year();

    // Gestisco le casistiche per la verifica di congruenza dati
    // CASO E1) utante ha inserito il flag frazione e la data inizio + esiste una data scadenza annualita
    const caseE1: boolean = dataInizioCC != undefined && dataSCSameAnnualita;
    // Verifico le condizioni d'errore
    if (caseE1) {
      // Errore, definisco il codice e ritorno l'oggetto
      paramsCC.errorCode = RiscaNotifyCodes.E115;
      // Ritorno la configurazione
      return paramsCC;
    }

    // La data riferimento è sempre la stessa
    paramsCC.dataRif = dataRifStr;

    // Gestisco le casistiche per la gestione del flusso di assegnazione dei valori
    // CASO 1A) Inserimento + utente ha inserito il flag frazionamento + data inizio
    const case1A: boolean = inserimento && dataInizioCC != undefined;
    // CASO 1B) modifica + data scadenza non esiste
    const case1B: boolean = modifica && !dataSCExist;
    // Verifico i casi definiti
    if (case1A || case1B) {
      // Definisco le informazioni e ritorno l'oggetto
      paramsCC.flgFraz = rateoPrimaAnn;
      paramsCC.dataFraz = dataInizioCC;
    }

    // CASO 2A) Inserimento + utente non ha inserito flg frazionamento ne data inizio + data scadenza concessione esiste ed è lo stesso anno annualità
    const case2A: boolean = inserimento && !dataInizioCC && dataSCSameAnnualita;
    // CASO 2B) Modifica + data scadenza concessione esiste ed è lo stesso anno annualità
    const case2B: boolean = modifica && dataSCSameAnnualita;
    // Verifico i casi definiti
    if (case2A || case2B) {
      // Definisco le informazioni e ritorno l'oggetto
      paramsCC.flgFraz = TipiFrazionamentoCanone.fine;
      paramsCC.dataFraz = dataScadConc;
    }

    // Ritorno l'oggetto dei parametri
    return paramsCC;
  }

  /**
   * ################################################
   * GESTIONE PULSANTE CALCOLA ANNUALITA' INSERIMENTO
   * ################################################
   */

  /**
   * Funzione collegata al pulsante "Calcola".
   * La funzione bloccherà le informazioni dei dati annualità e abiliterà il resto della form dati.
   */
  calcolaCanoneAnnualita() {
    // Lancio l'evento di pulizia dati
    this.cleanDati();
    // Lancio manualmente la validazione del form
    this.mainForm.markAllAsTouched();
    // Verifico se c'è un errore sul campo flag rateo e data inizio
    let errors: DynamicObjString;
    errors = this._riscaUtilities.getFormGroupErrors(this.mainForm);

    // Verifico che nell'oggetto con gli errori non ci sia quello per la data inizio
    if (!errors[this.EK.DATA_INIZIO_REQUIRED]) {
      // Lancio il calcolo automatico degli usi di legge
      this.calcolaCanoniUsiAttuali(true);
      // #
    }
  }

  /**
   * #############################
   * FUNZIONI DI GESTIONE DEL FORM
   * #############################
   */

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @returns DTAmbienteAnnualitaSD contente le informazioni del form in modalità raw value.
   */
  getMainFormRawValue(): DTAmbienteASDData {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      // Niente informazione
      return undefined;
    }

    // Recupero il dato dal form
    let dt: IDTAmbienteASD = this.mainForm.getRawValue();

    // Aggiungo l'idComponenteDt alle informazioni del form
    dt.idComponenteDt = this.idComponenteDt;
    // Converto l'oggetto del form in un oggetto annualità
    const annualita =
      this._dtaSDAConverter.convertDTAmbienteASDToAnnualitaSDVo(dt);

    // Creo l'oggetto da ritornare
    const dtaASD: DTAmbienteASDData = { annualita };
    // Ritorno l'oggetto generato
    return dtaASD;
  }

  /**
   * ##########################
   * GESTIONE FORM USI DI LEGGE
   * ##########################
   */

  /**
   * Funzione di toggle per la visualizzazione della form usiDiLegge.
   * @param stato boolean che definisce il nuovo stato dello switch accordion.
   */
  toggleUsiDiLegge(stato: boolean) {
    // Effettuo il toggle per la visualizzazione
    this.usiApriChiudi = !this.usiApriChiudi;
  }

  /**
   * Funzione di comodo che aggiorna i dati del FormGroup inserendo le informazioni degli usi di legge.
   */
  private aggiornaUsiLeggeInForm() {
    // Recupero i dati della tabella usi
    let usiLegge: UsoLeggePSDAmbienteInfo[];
    usiLegge = this.usiTable.getDataSource();
    // Definisco la chiave del form per il campo
    const formKey = this.DTA_SD_C.USI_DI_LEGGE;

    // Aggiungo i dati della tabella all'interno del form per gestire la validazione
    this._riscaUtilities.setFormValue(this.mainForm, formKey, usiLegge);
  }

  /**
   * Funzione che aggiunge i dati Uso all'interno della tabella usiDiLeggeTab.
   * L'inserimento di un nuovo uso di legge, prevede il calcolo canone di tutti gli usi di legge presenti più il nuovo uso.
   * Verrà quindi richiamato il calcolo canone passando tutti gli usi di legge interessati.
   * @param usoDiLegge UsoLeggePSDInfo contenente i dati della form usiForm.
   */
  private aggiungiUso(usoDiLegge: UsoLeggePSDAmbienteInfo) {
    // Recupero l'oggetto della form con i dati, forzando il typing
    const annualitaSD: IDTAmbienteASD = this.mainForm.getRawValue();
    // Se esiste, aggiungo il nuovo uso di legge
    if (usoDiLegge) {
      // Aggiungo il nuovo uso alla lista
      annualitaSD?.usiDiLegge?.push(usoDiLegge);
    }

    // Lancio la funzione di preparazione e calcolo
    this.prepareAndCalcolaCanoni(annualitaSD);
  }

  /**
   * Funzione che modifica i dati Uso all'interno della tabella usiDiLeggeTab.
   * @param row RiscaTableDataConfig<UsoLeggePSDAmbienteInfo> con le informazioni della riga d'aggiornare.
   * @param datiUso UsoDiLeggeInfo contenente i dati per l'aggiornamento della riga.
   */
  private modificaUsoEsistente(
    row: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>,
    datiUso: UsoLeggePSDAmbienteInfo
  ) {
    // Lancio l'aggiornamento dati per gli usi dell'annualità
    this.modificaUsoEsistenteAnnualita(datiUso);

    // Recupero l'oggetto della form con i dati, forzando il typing
    const annualitaSD: IDTAmbienteASD = this.mainForm.getRawValue();
    // Lancio la funzione di preparazione e calcolo
    this.prepareAndCalcolaCanoni(annualitaSD);
  }

  /**
   * Funzione di modifica uso esistente per la struttura dati correlata all'annualità.
   * @param datiUso UsoLeggePSDAmbienteInfo con i dati per l'aggiornamento.
   */
  private modificaUsoEsistenteAnnualita(datiUso: UsoLeggePSDAmbienteInfo) {
    // Recupero l'oggetto della form con i dati, forzando il typing
    const annualitaSD: IDTAmbienteASD = this.mainForm.getRawValue();
    // Se esiste, aggiungo il nuovo uso di legge
    if (datiUso) {
      // Cerco negli usi dell'annualità se trovo corrispondenza per uso di legge
      let iUsoAnn: number;
      iUsoAnn = annualitaSD?.usiDiLegge?.findIndex(
        (usoAnnualita: UsoLeggePSDAmbienteInfo) => {
          // Recupero i dati per il confronto tra gli usi di legge
          let usoCodForm: string = datiUso?.usoDiLegge?.cod_tipo_uso;
          let usoCodAnn: string = usoAnnualita?.usoDiLegge?.cod_tipo_uso;
          // Effettuo una verifica sugli usi di legge
          return usoCodForm === usoCodAnn;
        }
      );
      // Verifico il dato per sicurezza e lo gestisco eventualmente come se non fosse stato trovato nelal lista
      iUsoAnn = iUsoAnn ?? -1;

      // Verifico se ho trovato corrispondenza nell'array degli usi di legge dell'annualità
      if (iUsoAnn !== -1) {
        // Ho un indice nell'array, sostituisco l'oggetto
        annualitaSD.usiDiLegge[iUsoAnn] = datiUso;
        // #
      }
    }
  }

  /**
   * Funzione che gestisce il ritorno dalla chiamata del calcolo canone annualità.
   * @param canoneAnnualita CalcoloCanoneV2Vo che definisce l'oggetto generato dal calcolo canone annualità.
   * @param usiTabella UsoLeggePSDInfo[] che contiene le informazioni per popolare la tabella degli usi.
   */
  private onCalcoloAnnualita(
    canoneAnnualita: CalcoloCanoneV2Vo,
    usiTabella: UsoLeggePSDAmbienteInfo[]
  ) {
    // Verifico l'input
    if (!canoneAnnualita) {
      // Niente configurazione
      return;
    }

    // Recupero i dati del canone usi dal calcolo canone
    const canoneUsi = canoneAnnualita.canone_usi;
    // Aggiorno la tabella degli usi
    let usiJRE: IUsoLeggeVo[];
    usiJRE = this.aggiornaUsiConCanone(usiTabella, canoneUsi);

    // L'importo complessivo non è definito dall'API ma dalla somma dei canoni all'interno della tabella
    let datiUsi: UsoLeggePSDAmbienteInfo[];
    datiUsi = this.usiTable?.getDataSource() ?? [];

    // Recupero i mesi per il frazionamento
    const numeroMesi: number = canoneAnnualita.num_mesi;
    // Gestisco l'importo totale per i canoni
    let importoCanone = this.calcolaCanoneAnnualitaForm(datiUsi, numeroMesi);
    // Recupero il canone totale e lo aggiorno nel form
    this.canoneAnnualita = importoCanone;

    // Recupero il numero dei mesi e lo aggiorno nel form
    const mesi = canoneAnnualita.num_mesi;
    this.numeroMesi = mesi;

    // Aggiorno il form con i dati della tabella
    this.aggiornaUsiLeggeInForm();
    // Aggiorno la struttura della listaUsiDiLegge disabilitando le voci prensenti negli usi di legge
    this.disabilitaUsiDiLeggeInseriti();

    // Recupero se ci sono stati problemi con i json regola sul canone degli usi
    const jRMCanoneUsi = usiJRE?.length > 0;
    // Verifico e gestisco la situazione di regole mancanti
    if (jRMCanoneUsi) {
      // Emetto gli eventi per i json regola mancanti
      this._datiTecniciEvents.regolaMancanteUsiAnnualita(usiJRE);

      // Recupero l'uso di legge dalla form
      const usoLeggeForm: IUsoLeggeVo = this.usoDiLegge;
      // Verifico se l'uso di legge dentro la form è quello che è risultato in errore per la regola
      const isUsoLeggeJRM = usiJRE.some((usoJRM: IUsoLeggeVo) => {
        // Faccio una comparazione tra codici
        return usoJRM.cod_tipo_uso == usoLeggeForm?.cod_tipo_uso;
        // #
      });
      // Se non è l'uso di legge dentro la form ad aver generato l'errore di regola mancante
      if (!isUsoLeggeJRM) {
        // Posso pulire la form dati
        this.resetUsiForm();
      }
      // #
    } else {
      // Resetto la form
      this.resetUsiForm();
    }
  }

  /**
   * #################################
   * AGGIORNAMENTO DATI USI CON CANONE
   * #################################
   */

  /**
   * Funzione di comodo che aggiorna gli usi dentro la tabella, arricchiti del valore del canone.
   * @param usiTabella UsoLeggePSDInfo[] che definisce gli elementi della tabella d'arricchire.
   * @param canoneUsi CanoneUso[] che definisce gli elementi del calcolo canone scaricati.
   * @returns UsoLeggeVo[] con la lista di tutti gli usi che hanno avuto un problema con il json regola.
   */
  private aggiornaUsiConCanone(
    usiTabella: UsoLeggePSDAmbienteInfo[],
    canoneUsi: CanoneUso[]
  ): IUsoLeggeVo[] {
    // Verifico l'input
    if (!usiTabella || !canoneUsi) {
      // Niente configurazioni
      return [];
    }

    // Gestisco le informazioni per l'uso di tabella ottenendo le informazioni per l'uso e l'eventuale json regola mancante
    const usiConCanone: GestisciUsoConCanone[] = usiTabella.map(
      (ut: UsoLeggePSDAmbienteInfo) => {
        // Richiamo la funzione che gestirà nello specifico l'aggiornamento
        return this.gestisciUsoConCanone(ut, canoneUsi);
        // #
      }
    );

    // Definisco due array che conterranno le informazioni per i dati della tabella e degli errori
    let usiJRM: IUsoLeggeVo[] = [];
    let usiTabUpd: UsoLeggePSDAmbienteInfo[] = [];
    // Ciclo gli oggetti ottenuti e separo i dati
    usiConCanone.forEach((uCC: GestisciUsoConCanone) => {
      // Aggiungo le informazioni agli array
      usiJRM.push(uCC.usoJsonRegola);
      usiTabUpd.push(uCC.usoTabella);
    });
    // Rimuovo le righe non più valide (controllo sui json regola mancanti)
    usiJRM = compact(usiJRM);
    usiTabUpd = compact(usiTabUpd);

    // Aggiorno i valori nella tabella
    this.usiTable.setNewElements(usiTabUpd);
    // Ritorno la lista degli usi con json regola
    return usiJRM;
  }

  /**
   * Funzione di supporto impiegata per la gestione dell'aggiornamento dati delle righe della tabella degli usi di legge con le informazioni dei canoni calcolati.
   * A seconda delle informazioni della riga, verranno aggiornati i dati della riga con i canoni calcolati.
   * @param usoTabella UsoLeggePSDInfo con l'oggetto della tabella d'aggiornare.
   * @param canoneUsi CanoneUso[] con la lista dei canoni usi calcolati.
   * @returns GestisciUsoConCanone con le informazioni ottenute dalle verifiche e aggiornamenti sull'uso di legge.
   */
  private gestisciUsoConCanone(
    usoTabella: UsoLeggePSDAmbienteInfo,
    canoneUsi: CanoneUso[]
  ): GestisciUsoConCanone {
    // Definisco l'oggetto contenitore per il ritorno al chiamante
    const result: GestisciUsoConCanone = {};

    // Recupero l'oggetto canone per codice uso
    const canone = canoneUsi.find((cu: CanoneUso) => {
      // Recupero i codici per la verifica
      const codUT = usoTabella.usoDiLegge.cod_tipo_uso;
      const codCU = cu.cod_tipo_uso;
      // Verifico il codice per l'oggetto
      return codUT === codCU;
    });
    // Verifico che sia stata trovata corrispondenza tra gli oggetti
    if (!canone) {
      // Non c'è corrispondenza, ritorno i dati undefined
      return { usoJsonRegola: undefined, usoTabella: undefined };
    }

    // Variabile di comodo
    const tipoCalcolo = TipoCalcoloCanoneADT;
    // Gestisco la casistica per l'aggiornamento dati
    const jrm = canone.json_regola_mancante;
    const tccUnd = usoTabella.tipoCalcoloCanone == undefined;
    const tccMan = usoTabella.tipoCalcoloCanone == tipoCalcolo.manuale;
    const tccIns = usoTabella.tipoCalcoloCanone == tipoCalcolo.inserimento;
    const tccAut = usoTabella.tipoCalcoloCanone == tipoCalcolo.automatico;
    const tccNes = usoTabella.tipoCalcoloCanone == tipoCalcolo.nessuno;

    // Esiste una caso specifico che va considerato come manuale: l'uso ha definiti "canoneUnitarioUso" e "canoneUso", ma il tipo calcolo non è manuale
    const existCanoneUniUso = usoTabella.canoneUnitarioUso != undefined;
    const existCanoneUso = usoTabella.canoneUso != undefined;
    const usoConCanoni = !tccMan && jrm && existCanoneUniUso && existCanoneUso;

    // Verifico se l'uso è stato inserito manualmente
    if (tccMan || usoConCanoni) {
      // Oggetto manuale, assegno le informazioni
      result.usoTabella = usoTabella;
      // #
    } else if (jrm && tccIns) {
      // Se la regola è mancante, ma l'oggetto ha la tipologia di calcolo "inserimento", vuol dire che è stato effettuato il calcolo canone con gli usi inizializzati per l'inserimento
      // Ritorno sia l'oggetto per json regola mancante, sia l'oggetto della riga, perché non deve essere rimossa dalla tabella
      result.usoJsonRegola = usoTabella.usoDiLegge;
      result.usoTabella = usoTabella;
      // #
    } else if (jrm) {
      // Manca la json regola di una riga gestita automaticamente
      result.usoJsonRegola = usoTabella.usoDiLegge;
      // #
    } else {
      // Verifico le condizioni (tipo canone calcolato: inserimento, undefined, automatico, nessuno)
      if (tccIns || tccUnd || tccAut || tccNes) {
        // Verifico se il flag rateo prima annualità è attivo
        if (this.rateoPrimaAnnualita) {
          // Canone frazionato, recupero il valore generato dal frazionamento
          usoTabella.canoneUsoFrazionato = canone.canone_uso_frazionato;
          // #
        }
        // Salvo le informazioni per il canone uso e il canone unitario
        usoTabella.canoneUso = canone.canone_uso;
        usoTabella.canoneUnitarioUso = canone.canone_unitario;

        // Se il tipo canone non è stato definito, lo forzo ad "automatico"
        if (tccUnd || tccNes) {
          // Aggiorno la tipologia di calcolo
          usoTabella.tipoCalcoloCanone = TipoCalcoloCanoneADT.automatico;
        }
      }

      // Definisco il dato da ritornare
      result.usoTabella = usoTabella;
    }

    // Ritorno le informazioni compilate
    return result;
  }

  /**
   * #####################################
   * AGGIORNAMENTO CANONE TOTALE ANNUALITA
   * #####################################
   */

  /**
   * Funzione che effettua il calcolo dell'annualità totale, date le informazioni degli usi della tabella.
   * @param datiUsi UsoLeggePSDAmbienteInfo[] con le informazioni aggiornate degli usi di legge gestiti del calcolo canone.
   * @param numeroMesi number con il numero mesi d'applicare al frazionamento del canone. RISCA-758 => per il momento, il frazionamento dei mesi, quando non c'è da frazionare, ritorna 0 mesi. In realtà dovrebbe essere 12 mesi (anno completo, senza frazionamenti). Per il momento mettiamo nella casistica di IF sia lo 0 sia il 12.
   * @version 1.0.0 Questa funzione è pensata per aggiornare il totale a seguito del calcolo canone per tutti gli usi.
   * @returns number con il totale canone annualità calcolato.
   */
  private calcolaCanoneAnnualitaForm(
    datiUsi: UsoLeggePSDAmbienteInfo[],
    numeroMesi: number
  ): number {
    // Verifico l'input per una corretta gestione del flusso
    datiUsi = datiUsi ?? [];
    numeroMesi = numeroMesi ?? 12; // 12 viene condiderata l'annualità intera, quindi senza frazionamento

    // Gestisco l'importo totale per i canoni
    let importoCanone = 0;

    // Verifico se esistono effettivamente dati per gli usi
    if (datiUsi.length > 0) {
      // Effettuo la somma dei canoni
      let sommaCanoni: number = 0;
      sommaCanoni = sumBy(datiUsi, (c: UsoLeggePSDAmbienteInfo) => {
        // Sommo i canoni uso, ma se esistono frazionati prendono la priorità
        return c.canoneUso;
      });

      // Verifico che il numero di mesi sia frazionabile (non 0 e non 12)
      if (numeroMesi > 0 && numeroMesi < 12) {
        // Esiste un frazionamento, ricalcolo il totale dell'annualità
        sommaCanoni = (sommaCanoni / 12) * numeroMesi;
        // #
      }

      // La somma calcolata, per SOMMA GRAZIA DI JAVASCRIPT, a volte risulta sporca, effettuo un troncamento ai decimali
      let sommaCanoniITA: string;
      sommaCanoniITA = this._riscaUtilities.formatoImportoITA(sommaCanoni, 0);
      // Converto la stringa generata in un numbero
      importoCanone = this._riscaUtilities.importoITAToJsFloat(sommaCanoniITA);
    }

    // Ritorno il canone calcolato
    return importoCanone;
  }

  /**
   * ##################################
   * FUNZIONI PER GESTIONE MODIFICA USO
   * ##################################
   */

  /**
   * Funzione che permette la modifica dei dati per un uso di legge.
   * @param row RiscaTableDataConfig<UsoLeggePSDAmbienteInfo> con i dati della riga.
   */
  modificaUso(row: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>) {
    // Verifico l'input
    if (!row || !row.original) {
      // Mancano i dati, blocco il flusso
      return;
    }

    const fp: FormUpdatePropagation = { emitEvent: false };
    this.usiForm.get(this.DTA_SD_C.USO_DI_LEGGE).disable(fp);

    // Recupero l'oggetto dati per la modifica
    let datiUso: UsoLeggePSDAmbienteInfo;
    datiUso = row.original;
    // Recupero l'uso di legge dai dati della riga
    let usoDiLegge: IUsoLeggeVo;
    usoDiLegge = datiUso.usoDiLegge;

    // Controllo che l'oggetto esista e abbia le informazioni per lo scarico dei dati connessi
    if (!usoDiLegge || !usoDiLegge.id_tipo_uso) {
      // Blocco il flusso
      return;
    }

    // Apro l'accordion del form degli usi
    this.usiApriChiudi = true;

    // Recupero il valore della gestione manuale
    const gestioneManuale = this.isGestioneManuale;
    // Genero la richiesta per lo scarico delle informazioni relative all'uso di legge.
    let usoLeggeReq: IDatiUsoLeggeReq;
    usoLeggeReq = this.datiCorrelatiUsoLeggeReq(usoDiLegge, gestioneManuale);

    // Effettuo le chiamate
    this.usoLeggeSelectedReq(usoLeggeReq).subscribe({
      next: () => {
        // A seguito del set delle informazioni, vado ad impostare i default per i campi
        this.setUsiFormToModify(datiUso);
        // #
      },
      error: (e: RiscaServerError) => {
        // Segnalo l'errore tramite servizio
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione che gestisce il set delle informazioni per il form dell'uso di legge.
   * @param datiUso UsoLeggePSDAmbienteInfo con le informazioni da settare per il form.
   */
  private setUsiFormToModify(datiUso: UsoLeggePSDAmbienteInfo) {
    // Estraggo dai dati della riga di tabella le informazioni
    let usoLegge: IUsoLeggeVo;
    usoLegge = datiUso?.usoDiLegge;
    let usiLeggeSpecifici: IUsoLeggeSpecificoVo[];
    usiLeggeSpecifici = datiUso?.usiDiLeggeSpecifici;
    // let unitaMisura: IUnitaMisuraVo;
    // unitaMisura = datiUso?.unitaDiMisura;
    let quantita: number;
    quantita = datiUso?.quantita;
    let quantitaFaldaProfonda: number;
    quantitaFaldaProfonda = datiUso?.quantitaFaldaProfonda;
    let percentualiRiduzioni: RiduzioneAumentoVo[];
    percentualiRiduzioni = datiUso?.percRiduzioni;
    let percentualiAumenti: RiduzioneAumentoVo[];
    percentualiAumenti = datiUso?.percAumenti;
    let dataScadenzaEMASISO: NgbDateStruct;
    dataScadenzaEMASISO = datiUso?.dataScadenzaEmasIso;
    let canoneUso: number;
    canoneUso = datiUso?.canoneUso;
    let canoneUnitarioUso: number;
    canoneUnitarioUso = datiUso?.canoneUnitarioUso;

    // Lancio le funzioni di set per i dati rispetto alla riga della tabella
    this.setUsoLeggeInForm(usoLegge);
    this.setUsiLeggeSpecificiInForm(usiLeggeSpecifici);
    /**
     * NOTA DELLO SVILUPPATORE: il dato tecnico scaricato da DB non ha le informazioni complete per l'unità di misura, ma solo la descrizione.
     * Prima della modifica, il dato veniva scaricato "fresco" ogni volta che l'utente cancellava e re-inseriva lo stesso uso.
     * Per il momento si tiene così il flusso, il cambio di struttura del dato tecnico è troppo impattante al momento ed il funzionamento è il medesimo pre-modifica uso.
     */
    // this.setUnitaDiMisuraInForm(unitaMisura);
    this.setQuantitaInForm(quantita);
    this.setQuantitaFaldaProfondaInForm(quantitaFaldaProfonda);
    this.setPercentualiRiduzioneInForm(percentualiRiduzioni);
    this.setPercentualiAumentoInForm(percentualiAumenti);
    this.setDataScadenzaEMASISOInForm(dataScadenzaEMASISO);
    this.setCanoneUsoInForm(canoneUso);
    this.setCanoneUnitarioUsoInForm(canoneUnitarioUso);

    // Imposto un timeout per dare il tempo alle input di aggiornarsi mediante form
    setTimeout(() => {
      // Lancio la validazione del form
      this.usiForm?.markAllAsTouched();
      // #
    });
  }

  /**
   * Funzione che effettua il set di un uso di legge per la lista degli usi di legge.
   * @param usoLegge IUsoLeggeVo con l'uso di legge da impostare come selezionato.
   */
  private setUsoLeggeInForm(usoLegge: IUsoLeggeVo) {
    // Definisco le informazioni per il set dati
    const f: FormGroup = this.usiForm;
    const field = this.DTA_SD_C.USO_DI_LEGGE;
    const list: IUsoLeggeVo[] = this.listaUsiDiLegge;
    const data: IUsoLeggeVo = usoLegge;
    const fp: FormUpdatePropagation = { emitEvent: false };
    // Funzione di find
    const find = (a: IUsoLeggeVo, b: IUsoLeggeVo) => {
      // Se uno dei due è undefined, non li confronto nemmeno
      if (a == undefined || b == undefined) {
        // Sono certamente diversi
        return false;
      }
      // Altrimenti controllo se sono uguali
      return a.cod_tipo_uso == b.cod_tipo_uso;
    };

    // Seleziono il valore
    this._riscaUtilities.setFormValueAndSelect(f, field, list, data, find, fp);
  }

  /**
   * Funzione che effettua il set degli usi di legge specifici per la lista degli usi di legge specifici.
   * @param usiLeggeSpecifici IUsoLeggeSpecificoVo[] con gli usi di legge specifici da impostare come selezionati.
   */
  private setUsiLeggeSpecificiInForm(
    usiLeggeSpecifici: IUsoLeggeSpecificoVo[]
  ) {
    // Definisco le informazioni per il set dati
    const f: FormGroup = this.usiForm;
    const field = this.DTA_SD_C.USI_DI_LEGGE_SPECIFICI;
    const list: IUsoLeggeSpecificoVo[] = this.listaUsiDiLeggeSpecifici;
    const data: IUsoLeggeSpecificoVo[] = usiLeggeSpecifici;
    // Funzione di find
    const find = (a: IUsoLeggeSpecificoVo, b: IUsoLeggeSpecificoVo) => {
      // Se uno dei due è undefined, non li confronto nemmeno
      if (a == undefined || b == undefined) {
        // Sono certamente diversi
        return false;
      }
      // Altrimenti controllo se sono uguali
      return a.cod_tipo_uso == b.cod_tipo_uso;
    };

    // Seleziono il valore
    this._riscaUtilities.setFormValueAndMultiSelect(f, field, list, data, find);
  }

  /**
   * Funzione adibita al setup delle informazioni correlate ad un uso di legge, per l'unità di misura.
   * @param unita UnitaMisuraVo come valore di default da impostare per il campo.
   */
  private setUnitaDiMisuraInForm(unita: IUnitaMisuraVo) {
    // Recupero i dati per l'aggiornamento
    const f = this.usiForm;
    const fcnUDM = this.DTA_SD_C.UNITA_DI_MISURA;
    const fcnUDMD = this.DTA_SD_C.UNITA_DI_MISURA_DESC;
    const v = unita;

    // Aggiorno le informazioni per il form control del form group
    this._riscaUtilities.setFormValue(f, fcnUDM, v);
    // Aggiorno anche la descrizione per la visualizzazione all'utente
    this._riscaUtilities.setFormValue(f, fcnUDMD, v.des_unita_misura);
  }

  /**
   * Funzione adibita al setup delle informazioni correlate ad un uso di legge.
   * @param quantita number come valore di default da impostare per il campo.
   */
  private setQuantitaInForm(quantita: number) {
    // Recupero i dati per l'aggiornamento
    const f = this.usiForm;
    const fcn = this.DTA_SD_C.QUANTITA;
    const v = quantita;

    // Aggiorno le informazioni per il form control del form group
    this._riscaUtilities.setFormValue(f, fcn, v);
  }

  /**
   * Funzione adibita al setup delle informazioni correlate ad un uso di legge.
   * @param quantitaFaldaProfonda number come valore di default da impostare per il campo.
   */
  private setQuantitaFaldaProfondaInForm(quantitaFaldaProfonda: number) {
    // Recupero i dati per l'aggiornamento
    const f = this.usiForm;
    const fcn = this.DTA_SD_C.QUANTITA_FALDA_PROFONDA;
    const v = quantitaFaldaProfonda;

    // Aggiorno le informazioni per il form control del form group
    this._riscaUtilities.setFormValue(f, fcn, v);
  }

  /**
   * Funzione che effettua il set delle percentuali di riduzioni dell'uso.
   * @param percentualiRiduzioni RiduzioneAumentoVo[] con le percentuali di riduzione selezionate.
   */
  private setPercentualiRiduzioneInForm(
    percentualiRiduzioni: RiduzioneAumentoVo[]
  ) {
    // Definisco le informazioni per il set dati
    const f: FormGroup = this.usiForm;
    const field = this.DTA_SD_C.PERC_DI_RIDUZIONE_MOTIVAZIONE;
    const list: RiduzioneAumentoVo[] = this.listaPercRiduzione;
    const data: RiduzioneAumentoVo[] = percentualiRiduzioni;
    // Funzione di find
    const find = (a: RiduzioneAumentoVo, b: RiduzioneAumentoVo) => {
      // Se uno dei due è undefined, non li confronto nemmeno
      if (a == undefined || b == undefined) {
        // Sono certamente diversi
        return false;
      }
      // Altrimenti controllo se sono uguali
      return a.id_riduzione_aumento == b.id_riduzione_aumento;
    };

    // Seleziono il valore
    this._riscaUtilities.setFormValueAndMultiSelect(f, field, list, data, find);
  }

  /**
   * Funzione che effettua il set delle percentuali di aumento dell'uso.
   * @param percentualiAumento RiduzioneAumentoVo[] con le percentuali di aumento selezionate.
   */
  private setPercentualiAumentoInForm(
    percentualiAumento: RiduzioneAumentoVo[]
  ) {
    // Definisco le informazioni per il set dati
    const f: FormGroup = this.usiForm;
    const field = this.DTA_SD_C.PERC_DI_AUMENTO_MOTIVAZIONE;
    const list: RiduzioneAumentoVo[] = this.listaPercAumento;
    const data: RiduzioneAumentoVo[] = percentualiAumento;
    // Funzione di find
    const find = (a: RiduzioneAumentoVo, b: RiduzioneAumentoVo) => {
      // Se uno dei due è undefined, non li confronto nemmeno
      if (a == undefined || b == undefined) {
        // Sono certamente diversi
        return false;
      }
      // Altrimenti controllo se sono uguali
      return a.id_riduzione_aumento == b.id_riduzione_aumento;
    };

    // Seleziono il valore
    this._riscaUtilities.setFormValueAndMultiSelect(f, field, list, data, find);
  }

  /**
   * Funzione adibita al setup delle informazioni correlate ad un uso di legge.
   * @param dataScadenzaEMASISO NgbDateStruct come valore di default da impostare per il campo.
   */
  private setDataScadenzaEMASISOInForm(dataScadenzaEMASISO: NgbDateStruct) {
    // Recupero i dati per l'aggiornamento
    const f = this.usiForm;
    const fcn = this.DTA_SD_C.DATA_SCADENZA_EMAS_ISO;
    const v = dataScadenzaEMASISO;

    // Aggiorno le informazioni per il form control del form group
    this._riscaUtilities.setFormValue(f, fcn, v);
  }

  /**
   * Funzione adibita al setup delle informazioni correlate ad un uso di legge.
   * @param canoneUso number come valore di default da impostare per il campo.
   */
  private setCanoneUsoInForm(canoneUso: number) {
    // Recupero i dati per l'aggiornamento
    const f = this.usiForm;
    const fcn = this.DTA_SD_C.CANONE_USO;
    const v = canoneUso;

    // Aggiorno le informazioni per il form control del form group
    this._riscaUtilities.setFormValue(f, fcn, v);
  }

  /**
   * Funzione adibita al setup delle informazioni correlate ad un uso di legge.
   * @param canoneUnitarioUso number come valore di default da impostare per il campo.
   */
  private setCanoneUnitarioUsoInForm(canoneUnitarioUso: number) {
    // Recupero i dati per l'aggiornamento
    const f = this.usiForm;
    const fcn = this.DTA_SD_C.CANONE_UNITARIO_USO;
    const v = canoneUnitarioUso;

    // Aggiorno le informazioni per il form control del form group
    this._riscaUtilities.setFormValue(f, fcn, v);
  }

  /**
   * #########################
   * ELIMINAZIONE USO DI LEGGE
   * #########################
   */

  /**
   * Funzione che rimuove i dati dalla tabella usi.
   * @param row RiscaTableDataConfig<UsoDiLeggeInfo> contenente i dati della riga.
   */
  eliminaUso(row: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>) {
    // Richiamo la funzione di gestione logiche per cancellazione uso
    this.onEliminaUso(row);
  }

  /**
   * Funzione lanciata se l'utente conferma la cancellazione di un uso di legge.
   * @param row RiscaTableDataConfig<UsoDiLeggeInfo> contenente i dati della riga.
   */
  private onEliminaUso(row: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>) {
    // Elimino l'elemento dalla tabella
    const rowRemoved = this.usiTable.removeRow(row);
    // Aggiorno la struttura della listaUsiDiLegge disabilitando le voci prensenti negli usi di legge
    this.disabilitaUsiDiLeggeInseriti();

    // Recupero la lista di usi aggiornata
    const usi: UsoLeggePSDAmbienteInfo[] = this.usiTable.getDataSource();
    // Aggiorno i dati del form
    this.usiDiLegge = usi;

    // Verifico se è stata rimossa la riga
    if (rowRemoved) {
      // Lancio la funzione di calcolo del canone
      this.calcolaCanoniUsiAttuali();
    }
  }

  /**
   * ############################
   * DISABILITAZIONE USI DI LEGGE
   * ############################
   */

  /**
   * Funzione che aggiunge un flag "disabled" agli oggetti "UsoDiLegge" per impedire il multi-inserimento.
   */
  private disabilitaUsiDiLeggeInseriti() {
    // Recupero i dati per gli usi di legge inseriti
    const usiLeggeIns = this.usiTable.getDataSource();
    // Verifico che la lista ci sia. Va bene che sia null.
    if (usiLeggeIns != null) {
      // Aggiorno la lista degli usi di legge
      this.listaUsiDiLegge = this._datiTecniciAmbiente.disabilitaUsiDiLegge(
        this.listaUsiDiLegge,
        usiLeggeIns
      );
    }
  }

  /**
   * #########################
   * SUBMIT/RESET USI DI LEGGE
   * #########################
   */

  /**
   * Funzione agganciata al bottone di Submit per la form usiForm.
   */
  usiSubmit() {
    // Il form è stato submittato
    this.usiFormSubmitted = true;
    // Verifico che la form sia valida
    if (this.usiForm.valid) {
      // Recupero i dati dalla form mediante la getRawValue (recupera i dati anche delle input disattivate)
      const nuovoUso: UsoLeggePSDAmbienteInfo = this.usiForm.getRawValue();
      // Per default il tipo canone è "nessuno"
      nuovoUso.tipoCalcoloCanone = TipoCalcoloCanoneADT.nessuno;

      // Verifico se l'uso inserito ha definito canone e canone unitario
      if (nuovoUso.canoneUso != undefined) {
        // E' definito un canone manuale, aggiungo l'informazione
        nuovoUso.tipoCalcoloCanone = TipoCalcoloCanoneADT.manuale;
      }

      // Lancio la funzione di gestione del dato del form
      this.gestisciUsoPerTabella(nuovoUso);
    }

    const fp: FormUpdatePropagation = { emitEvent: false };
    this.usiForm.get(this.DTA_SD_C.USO_DI_LEGGE).enable(fp)
  }

  /**
   * Funzione che gestisce l'aggiunta o la modifica dati di una riga di tabella con i dati di un uso di legge.
   * @param usoData UsoLeggePSDAmbienteInfo con le informazioni recuperate dal form.
   */
  private gestisciUsoPerTabella(usoData: UsoLeggePSDAmbienteInfo) {
    // Recupero i dati dalla tabella dati degli usi
    let rowUsiTab: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>[];
    rowUsiTab = this.usiTable?.source ?? [];

    // Verifico se ho degli usi effettivamente
    if (rowUsiTab.length === 0) {
      // Non ho usi in tabella, lo aggiungo
      this.aggiungiUso(usoData);
      // Blocco il flusso
      return;
    }

    // Ho degli usi, verifico se l'uso che sto cercando di aggiungere esiste già
    let rowUso: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>;
    rowUso = rowUsiTab.find(
      (rowUsoTab: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>) => {
        // Recupero i dati per il confronto tra gli usi di legge
        let usoCodForm = usoData?.usoDiLegge?.cod_tipo_uso;
        let usoCodTab = rowUsoTab?.original?.usoDiLegge?.cod_tipo_uso;
        // Effettuo una verifica sugli usi di legge
        return usoCodForm === usoCodTab;
        // #
      }
    );

    // Verifico se ho trovato effettivamente una riga della tabella con stesso uso di legge
    if (rowUso) {
      // Esiste già l'uso nella tabella, lo aggiorno
      this.modificaUsoEsistente(rowUso, usoData);
      // #
    } else {
      // Non è stato trovato nella tabella, lo aggiungo
      this.aggiungiUso(usoData);
    }
  }

  /**
   * Funzione di reset della form: usiForm.
   */
  private resetUsiForm() {
    // Resetto i campi della form usiForm
    this.usiForm.reset();
    // Resetto il flag di submit
    this.usiFormSubmitted = false;
    // Resetto le liste di supporto
    this.listaUsiDiLegge = this.listaUsiDiLegge?.map(
      (usoLegge: IUsoLeggeVo | any) => {
        // Rimuovo la proprietà di selezione di un possibile default
        usoLegge.__selected = false;
        // Ritorno l'oggetto senza la proprietà selezionata di default
        return usoLegge;
      }
    );
    this.listaUsiDiLeggeSpecifici = [];
    this.listaPercRiduzione = [];
    this.listaPercAumento = [];
    // Disabilito la multi select tramite chiave form control
    this.usiForm.get(this.DTA_SD_C.USI_DI_LEGGE_SPECIFICI).disable();
    this.usiForm.get(this.DTA_SD_C.PERC_DI_RIDUZIONE_MOTIVAZIONE).disable();
    this.usiForm.get(this.DTA_SD_C.PERC_DI_AUMENTO_MOTIVAZIONE).disable();
  }

  /**
   * ########################
   * GESTIONE FORM PRINCIPALE
   * ########################
   */

  /**
   * Funzione che effettua il reset dei dati della form datti tecnici ambiente.
   * Richiamabile dal padre.
   * @override
   */
  onFormReset() {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Chiudo il form degli uso
    this.usiApriChiudi = false;

    // Lancio l'evento di pulizia dati
    this.cleanDati();

    // Verifico la modalita
    if (this.inserimento) {
      // Richiamo il reset per l'inserimento
      this.onFormResetIns();
      // #
    } else if (this.modifica) {
      // Richiamo il reset per l'inserimento
      this.onFormResetMod();
      // #
    }
  }

  /**
   * Funzione di supporto invocata quando è necessario resettare le informazioni del form quando si è in modalità: inserimento.
   */
  private onFormResetIns() {
    // Resetto i form
    const o: FormUpdatePropagation = { emitEvent: false };
    this.mainForm?.reset(o);
    this.usiForm?.reset();

    // Setto l'annualità di default
    this.initAnnualitaDefault();
    // Rilancio la configurazione dati
    this.initConfigs();
  }

  /**
   * Funzione di supporto invocata quando è necessario resettare le informazioni del form quando si è in modalità: modifica.
   */
  private onFormResetMod() {
    // Resetto i form
    const o: FormUpdatePropagation = { emitEvent: false };
    this.mainForm?.reset(o);
    this.usiForm?.reset();

    // Setto l'annualità di default
    this.initAnnualitaDefault();
    // Rilancio la configurazione dati
    this.initConfigs();
  }

  /**
   * Funzione di comodo che emette l'evento di avvenuta "pulizia" dei dati.
   */
  private cleanDati() {
    // Richiamo ed emetto l'evento dal servizio
    this._datiTecniciEvents.datiPuliti();
  }

  /**
   * ##############################
   * FUNZIONI UTILITIES PER SERVIZI
   * ##############################
   */

  /**
   * Funzione che genera la richiesta dati per le informazioni relative ad un uso di legge.
   * La funzione richiama la funzione del servizio.
   * @param usoDiLegge UsoLeggeVo per il recupero delle informazioni collegate.
   * @param isGestioneManuale boolean con il flag per la gestione manuale dei dati.
   * @returns IDatiUsoLeggeReq con l'insieme di richieste per i dati relativi all'uso di legge.
   */
  datiCorrelatiUsoLeggeReq(
    usoDiLegge: IUsoLeggeVo,
    isGestioneManuale: boolean
  ): IDatiUsoLeggeReq {
    // Richiamo e ritorno la funzione del servizio
    return this._datiTecniciAmbiente.datiCorrelatiUsoLeggeReq(
      usoDiLegge,
      isGestioneManuale
    );
  }

  /**
   * Funzione che definisce un comportamento standard quando viene emesso l'evento: onAlertHidden; da parte del componente: risca-alert.
   * @param hidden boolean che definisce lo stato di nascosto dell'alert.
   * @param alertConfigs RiscaAlertConfigs da resettare.
   */
  onAlertHidden(hidden: boolean, alertConfigs: RiscaAlertConfigs) {
    // Verifico il risultato
    if (hidden) {
      // Resetto la configurazione dell'alert
      this.resetAlertConfigs(alertConfigs);
    }
  }

  /**
   * Funzione che gestisce il reset del prompt dei messaggi.
   * Se i parametri non sono definiti, verrà effettuato un reset.
   * @param c RiscaAlertConfigs da resettare.
   */
  protected resetAlertConfigs(c?: RiscaAlertConfigs) {
    // Verifico se esiste l'oggetto per l'alert, altrimenti imposto quello locale
    c = c ?? this.alertConfigs;
    // Resetto la configurazione
    c = this._riscaAlert.aggiornaAlertConfigs(c);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica che le informazioni per la configurazione di alertCEConfigs siano valide.
   */
  get alertConfigscheck() {
    // Verifico che la configurazione per l'alert delle comunicazioni esista e contenga dati
    return this.alertConfigs?.messages?.length > 0;
  }

  /**
   * Prende il valore del flag rateo prima annualità
   */
  get flagRPA(): boolean {
    return this._riscaUtilities.getFormValue(
      this.mainForm,
      this.DTA_SD_C.RATEO_PRIMA_ANNUALITA
    )?.check as boolean;
  }

  /**
   * Pilota apertura e chiusura dell'accordion degli usi di legge
   */
  get formUsiDiLeggeOpen() {
    return this.usiApriChiudi && !this.dataInizialeInvalid;
  }

  /**
   * Determina se flagRPA e dataInizio sono undefined
   */
  get dataInizialeInvalid(): boolean {
    // Controllo se devo disabilitare il pulsante aggiungi uso
    return this.flagRPA && this.dataInizio == undefined;
  }

  // Disabilita la form degli usi
  get disableUsi() {
    return this.AEA_DTDisabled || this.dataInizialeInvalid;
  }

  /**
   * Disabilita il pulsante aggiungiuso
   */
  get disableAggiungiUso() {
    // Definisco le regole di disabilitazione del pulsante
    const abilitazioneAEA = this.AEA_DTDisabled;
    const usiFormInvalid = !this.usiForm.valid;
    const accessibilityInsert = !this.accessibilityAggiungiUso;
    const dtInizialeInvalid = this.dataInizialeInvalid;
    // Definisco una variabile per il check completo per la disabilitazione
    const isDisabled =
      abilitazioneAEA ||
      usiFormInvalid ||
      accessibilityInsert ||
      dtInizialeInvalid;

    // Ritorno le condizioni di disabilitazione del pulsante
    return isDisabled;
  }

  get existTableUsi() {
    return this.usiTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che recupera e trasforma la data inserimento pratica in input in formato moment.
   * @returns Moment con la data inserimento pratica.
   */
  get dataInsPratica(): Moment {
    // Verifico esista la data
    if (!this.dataInserimentoPratica) {
      // Return undefined
      return undefined;
    }

    // Variabile di comodo
    const d = this.dataInserimentoPratica;
    // Converto la data
    const dInsP = this._riscaUtilities.convertServerDateToMoment(d);
    // Verifico sia una data valida
    if (dInsP && dInsP.isValid()) {
      // Ritorno la data in formato moment
      return dInsP;
      // #
    } else {
      // Qualcosa è andato storto
      return undefined;
    }
  }

  /**
   * Getter che genera la data di riferimento per il calcolo canone.
   * La data di riferimento di comporrà:
   * - Giorno inserimento pratica;
   * - Mese inserimento pratica;
   * - Annulità scelta dalla tendina;
   * La data è ritornata in formato server (YYYY-MM-DD).
   * @returns Moment con la data riferimento per il calcolo canone.
   */
  get dataRiferimentoCanoneMoment(): Moment {
    // Recupero l'annualità dalla form
    const f = this.mainForm;
    const k = this.DTA_SD_C.ANNUALITA;
    // Estraggo l'oggetto dal form
    const objAnno: IRiscaAnnoSelect = this._riscaUtilities.getFormValue(f, k);

    // Definisco le variabili per ritornare la data riferimento
    let yyyy = 0;
    let mm = 0;
    let dd = 1;

    // Estraggo l'anno dell'annualità
    yyyy = objAnno?.anno ?? moment().year();
    // Recupero dalla pratica la data d'inserimento
    const dataIns: Moment = this.dataInsPratica;
    // Verifico la data inserimento pratica sia valida
    if (dataIns && dataIns.isValid()) {
      // Recupero giorno e mese dalla data
      mm = dataIns.month();
      dd = dataIns.date();
    }

    // Creo un moment di appoggio
    const dataRif = moment().year(yyyy).month(mm).date(dd);
    // Ritorno la data completa
    return dataRif;
  }

  /**
   * Getter che genera la data di riferimento per il calcolo canone.
   * Viene utilizzata la logica per il getter "dataRiferimentoCanoneMoment" e poi la data viene convertita in string.
   * @returns string con la data riferimento per il calcolo canone.
   */
  get dataRiferimentoCanone(): string {
    // Genero la data
    const dataRif = this.dataRiferimentoCanoneMoment;
    // Ritorno la data completa
    return this._riscaUtilities.convertMomentToServerDate(dataRif);
  }

  /**
   * Getter di supporto che recupera il rateo prima annualita per il calcolo del canone.
   * Il rateo annualita è quello che verrà usato come flag di frazionamento.
   * @returns boolean con lo stato per il flag di frazionamento.
   */
  get rateoPrimaAnnualitaCanone(): TipiFrazionamentoCanone {
    // Recupero le informazioni per il get della data inizio
    const f = this.mainForm;
    const k = this.DTA_SD_C.RATEO_PRIMA_ANNUALITA;
    // Recupero dal form la data inizio
    let rateoAnn: IRiscaCheckboxData;
    rateoAnn = this._riscaUtilities.getFormValue(f, k);
    // Verifico se esiste un valore, altrimenti assegno false
    const flgRateoAnn = rateoAnn?.check ?? false;

    // Ritorno il valore del check
    return flgRateoAnn ? TipiFrazionamentoCanone.inizio : undefined;
  }

  /**
   * Getter di supporto che recupera la data inizio per il calcolo del canone.
   * La data inizio è quella che verrà usata come data di frazionamento.
   * @returns Moment con la data d'inizio per il calcolo canone.
   */
  get dataInizioCalcoloCanone(): Moment {
    // Recupero le informazioni per il get della data inizio
    const f = this.mainForm;
    const k = this.DTA_SD_C.DATA_INIZIO;
    // Recupero dal form la data inizio
    const dataInizio = this._riscaUtilities.getFormValue(f, k);

    // Definisco il contenitore per la data da ritornare
    let mDI: Moment;
    // Verifico se esiste la data all'interno del form
    if (dataInizio != undefined) {
      // La data esiste, converto l'oggetto
      mDI = this._riscaUtilities.convertNgbDateStructToMoment(dataInizio);
      // #
    }

    // Ritorno la data inizio per il calcolo canone
    return mDI;
  }

  /**
   * Setter di comodo per aggiornare il valore del form per il canone totale dell'annualità.
   */
  set canoneAnnualita(canone: number) {
    // Recupero le informazioni per aggiornare il dato
    const f = this.mainForm;
    const k = this.DTA_SD_C.CANONE_ANNUALITA;
    // Lancio la funzione di aggiornamento del dato
    this._riscaUtilities.setFormValue(f, k, canone);
  }

  /**
   * Getter di comodo per recuperare il valore del form per il numero mesi dell'annualità.
   * @returns number con il valore del form.
   */
  get numeroMesi(): number {
    // Recupero le informazioni per aggiornare il dato
    const f = this.mainForm;
    const k = this.DTA_SD_C.NUMERO_MESI;
    // Lancio la funzione di aggiornamento del dato
    return this._riscaUtilities.getFormValue(f, k);
  }

  /**
   * Setter di comodo per aggiornare il valore del form per il numero mesi dell'annualità.
   * @params number con il valore da inserire nel form.
   */
  set numeroMesi(numeroMesi: number) {
    // Recupero le informazioni per aggiornare il dato
    const f = this.mainForm;
    const k = this.DTA_SD_C.NUMERO_MESI;
    // Lancio la funzione di aggiornamento del dato
    this._riscaUtilities.setFormValue(f, k, numeroMesi);
  }

  /**
   * Getter di comodo che restituisce il rateo prima annualità dalla form.
   * @returns boolean con il valore del campo.
   */
  get rateoPrimaAnnualita(): boolean {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.DTA_SD_C.RATEO_PRIMA_ANNUALITA;

    // Recupero il dato dal form control
    let rateo: IRiscaCheckboxData;
    rateo = this._riscaUtilities.getFormValue(f, k);

    // Ritorno il valore del campo
    return rateo?.check ?? false;
  }

  /**
   * Getter di comodo che restituisce la data inizio dalla form.
   * @returns NgbDateStruct con il valore del campo.
   */
  get dataInizio(): NgbDateStruct {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.DTA_SD_C.DATA_INIZIO;

    // Recupero il dato dal form control
    let dataInizio: NgbDateStruct;
    dataInizio = this._riscaUtilities.getFormValue(f, k);

    // Ritorno il valore del campo
    return dataInizio;
  }

  /**
   * Setter di comodo che setta gli usi di legge nel form.
   * @param usi UsoLeggePSDInfo[] con la lista di usi d'aggiornare.
   */
  set usiDiLegge(usi: UsoLeggePSDAmbienteInfo[]) {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.DTA_SD_C.USI_DI_LEGGE;

    // Recupero il dato dal form control
    this._riscaUtilities.setFormValue(f, k, usi);
  }

  /**
   * Getter di comodo che restituisce l'anno annualità dalla form.
   * @returns IRiscaAnnoSelect con il valore del campo.
   */
  get annoAnnualita(): IRiscaAnnoSelect {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.DTA_SD_C.ANNUALITA;

    // Recupero il dato dal form control
    let anno: IRiscaAnnoSelect;
    anno = this._riscaUtilities.getFormValue(f, k);

    // Ritorno il valore del campo
    return anno;
  }

  /**
   * Getter che recupera il valore del form control per la chiave: DTA_SD_C.USO_DI_LEGGE.
   * @returns UsoLeggeVo con l'informazione dal form.
   */
  get usoDiLegge(): IUsoLeggeVo {
    // Recupero il dato dal form group
    const value = this.usoDiLeggeFC?.value;
    // Verifico il valore
    if (!value) {
      // Ritorno null
      return null;
      // #
    } else {
      // Ritorno l'oggetto
      return value;
    }
  }

  /**
   * #########################
   * GETTER PER I FORM CONTROL
   * #########################
   */

  /**
   * Getter che recupera il form control per la chiave: DTA_SD_C.RATEO_PRIMA_ANNUALITA.
   * @returns AbstractControl con il riferimento al form control.
   */
  get rateoPrimaAnnualitaFC(): AbstractControl {
    // Definisco la chiave di recupero
    const k = this.DTA_SD_C.RATEO_PRIMA_ANNUALITA;
    // Recupero dal form group il form control
    return this.mainForm?.get(k);
  }

  /**
   * Getter che recupera il form control per la chiave: DTA_SD_C.ANNUALITA.
   * @returns AbstractControl con il riferimento al form control.
   */
  get annualitaFC(): AbstractControl {
    // Definisco la chiave di recupero
    const k = this.DTA_SD_C.ANNUALITA;
    // Recupero dal form group il form control
    return this.mainForm?.get(k);
  }

  /**
   * Getter che recupera il form control per la chiave: DTA_SD_C.DATA_INIZIO.
   * @returns AbstractControl con il riferimento al form control.
   */
  get dataInizioFC(): AbstractControl {
    // Definisco la chiave di recupero
    const k = this.DTA_SD_C.DATA_INIZIO;
    // Recupero dal form group il form control
    return this.mainForm?.get(k);
  }

  /**
   * Getter che recupera il form control per la chiave: DTA_SD_C.PORTATA_DA_ASSEGNARE.
   * @returns AbstractControl con il riferimento al form control.
   */
  get portataDaAssegnareFC(): AbstractControl {
    // Definisco la chiave di recupero
    const k = this.DTA_SD_C.PORTATA_DA_ASSEGNARE;
    // Recupero dal form group il form control
    return this.mainForm?.get(k);
  }

  /**
   * Getter che recupera il form control per la chiave: DTA_SD_C.CANONE_ANNUALITA.
   * @returns AbstractControl con il riferimento al form control.
   */
  get canoneAnnualitaFC(): AbstractControl {
    // Definisco la chiave di recupero
    const k = this.DTA_SD_C.CANONE_ANNUALITA;
    // Recupero dal form group il form control
    return this.mainForm?.get(k);
  }

  /**
   * Getter che recupera il form control per la chiave: DTA_SD_C.GESTIONE_MANUALE.
   * @returns AbstractControl con il riferimento al form control.
   */
  get gestioneManualeFC(): AbstractControl {
    // Definisco la chiave di recupero
    const k = this.DTA_SD_C.GESTIONE_MANUALE;
    // Recupero dal form group il form control
    return this.mainForm?.get(k);
  }

  /**
   * Getter che recupera il form control per la chiave: DTA_SD_C.CANONE_USO.
   * @returns AbstractControl con il riferimento al form control.
   */
  get usoDiLeggeFC(): AbstractControl {
    // Definisco la chiave di recupero
    const k = this.DTA_SD_C.USO_DI_LEGGE;
    // Recupero dal form group il form control
    return this.usiForm?.get(k);
  }

  /**
   * Getter che recupera il form control per la chiave: DTA_SD_C.CANONE_USO.
   * @returns AbstractControl con il riferimento al form control.
   */
  get canoneUsoFC(): AbstractControl {
    // Definisco la chiave di recupero
    const k = this.DTA_SD_C.CANONE_USO;
    // Recupero dal form group il form control
    return this.usiForm?.get(k);
  }

  /**
   * Getter che recupera il form control per la chiave: DTA_SD_C.CANONE_UNITARIO_USO.
   * @returns AbstractControl con il riferimento al form control.
   */
  get canoneUnitarioUsoFC(): AbstractControl {
    // Definisco la chiave di recupero
    const k = this.DTA_SD_C.CANONE_UNITARIO_USO;
    // Recupero dal form group il form control
    return this.usiForm?.get(k);
  }

  /**
   * Getter di comodo che recupera lo stato della gestione manuale.
   * @returns boolean con il valore del dato.
   */
  get isGestioneManuale(): boolean {
    // Recupero l'oggetto della gestione manuale
    const f = this.mainForm;
    const GM = this.DTA_SD_C.GESTIONE_MANUALE;
    const dGM: IRiscaCheckboxData = this._riscaUtilities.getFormValue(f, GM);

    // Verifico se l'oggetto esiste
    if (dGM) {
      // Ritorno il suo stato di check
      return dGM.check;
      // #
    } else {
      // Ritorno false
      return false;
    }
  }

  /**
   * Getter che recupera dai dati dell'istanza salvata, le informazioni per: data scadenza concessione.
   * @returns Moment con le informazioni dalla configurazione.
   */
  get dataScadenzaConcessione(): Moment {
    // Tento di recuperare il dato
    const dsc: string =
      this.praticaEDatiTecnici?.pratica?.data_scad_concessione;
    // Converto l'informazione
    let dscMoment: Moment = this._riscaUtilities.convertServerDateToMoment(dsc);
    // Verifico se la data è valida
    dscMoment = dscMoment?.isValid() ? dscMoment : undefined;

    // Ritorno l'informazione
    return dscMoment;
  }
}
