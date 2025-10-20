import {
  Component,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  NgForm,
  Validators,
} from '@angular/forms';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin, Observable, of } from 'rxjs';
import { ProvinciaCompetenzaVo } from 'src/app/core/commons/vo/provincia-competenza-vo';
import { TipoIstanzaProvvedimentoVo } from 'src/app/core/commons/vo/tipo-istanza-provvidemento-vo';
import { TipoTitoloVo } from 'src/app/core/commons/vo/tipo-titolo-vo';
import { RiscaTableComponent } from 'src/app/shared/components/risca/risca-table/risca-table.component';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import {
  arrayContainsAtLeastProvvedimentoValidator,
  dataProvvedimentoValidator,
  dataScadenzaConcessioneValidator,
  progressivoUtenzaPerTipologiaPraticaValidator,
  statoRiscossionePraticaValidator,
} from 'src/app/shared/miscellaneous/forms-validators/generali-amministrativi/form-validators.ga';
import { NgbDateCustomParserFormatter } from '../../../../core/commons/class/dateformat';
import { StatoRiscossioneVo } from '../../../../core/commons/vo/stati-riscossione-vo';
import { TipoAutorizzazioneVo } from '../../../../core/commons/vo/tipo-autorizzazioni-vo';
import { TipoRiscossioneVo } from '../../../../core/commons/vo/tipo-riscossione-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import {
  IIstanzeProvvedimentiTableConfigs,
  IstanzeProvvedimentiTable,
} from '../../../../shared/classes/risca-table/generali-amministrativi/generali-amministrativi.istanze-provvedimenti.table';
import { RiscaFormChildComponent } from '../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { calendarioNgbInizioFineValidator } from '../../../../shared/miscellaneous/forms-validators/forms-validators';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  FormUpdatePropagation,
  GeneraliAmministrativi,
  IRiscaCheckboxData,
  NuovaIstanzaFormData,
  NuovoProvvedimentoFormData,
  RiscaRegExp,
  RiscaServerError,
} from '../../../../shared/utilities';
import { RiscaErrorKeys } from '../../../../shared/utilities/classes/errors-keys';
import { RiscaErrorsMap } from '../../../../shared/utilities/classes/errors-maps';
import {
  AppActions,
  CodStatiRiscossione,
} from '../../../../shared/utilities/enums/utilities.enums';
import { GeneraliAmministrativiConsts } from '../../consts/generali-amministrativi/generali-amministrativi.consts';
import { DatiContabiliService } from '../../service/dati-contabili/dati-contabili/dati-contabili.service';
import { GeneraliAmministrativiService } from '../../service/generali-amministrativi/generali-amministrativi.service';
import { PraticheService } from '../../service/pratiche.service';
import { IstanzaProvvedimentoVo } from './../../../../core/commons/vo/istanza-provvedimento-vo';
import { TNIPFormData } from './../../../../shared/utilities/types/utilities.types';
import { GAFieldsConfigClass } from './utilities/generali-amministrativi.fields-configs';

/**
 * Interfaccia di comodo per lo scarico delle liste del componente. Formato: request.
 */
interface IListeGAReq {
  tipiRiscossione: Observable<TipoRiscossioneVo[]>;
  codiciUtenza: Observable<ProvinciaCompetenzaVo[]>;
  tipiTributiUtenza: Observable<any[]>;
  procedimenti: Observable<TipoAutorizzazioneVo[]>;
  tipiIstanza: Observable<TipoIstanzaProvvedimentoVo[]>;
  tipiProvvedimento: Observable<TipoIstanzaProvvedimentoVo[]>;
  tipiTitolo: Observable<TipoTitoloVo[]>;
  statiPratica: Observable<StatoRiscossioneVo[]>;
}

/**
 * Interfaccia di comodo per lo scarico delle liste del componente. Formato: response.
 */
interface IListeGARes {
  tipiRiscossione: TipoRiscossioneVo[];
  codiciUtenza: ProvinciaCompetenzaVo[];
  tipiTributiUtenza: any[];
  procedimenti: TipoAutorizzazioneVo[];
  tipiIstanza: TipoIstanzaProvvedimentoVo[];
  tipiProvvedimento: TipoIstanzaProvvedimentoVo[];
  tipiTitolo: TipoTitoloVo[];
  statiPratica: StatoRiscossioneVo[];
}

@Component({
  selector: 'generali-amministrativi',
  templateUrl: './generali-amministrativi.component.html',
  styleUrls: ['./generali-amministrativi.component.scss'],
  // Setup per gestire i calendari in formato italiano
  providers: [
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class GeneraliAmministrativiComponent
  extends RiscaFormChildComponent<GeneraliAmministrativi>
  implements OnInit, OnChanges, OnDestroy
{
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti per il componente. */
  GA_C = GeneraliAmministrativiConsts;
  /** Oggetto contenente una serie di costanti per la gestione delle chiavi per gli errori del form. */
  EK = RiscaErrorKeys;
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;
  /** Oggetto RiscaRegExp utilizzate come supporto per la validazione dei campi. */
  private riscaRegExp: RiscaRegExp = new RiscaRegExp();

  /** Form group che definisce la struttura della form: nuova istanza. */
  nuovaIstanzaForm: FormGroup;
  /** Form group che definisce la struttura della form: nuovo provvedimento. */
  nuovoProvvedimentoForm: FormGroup;

  /** Id della pratica. Può essere undefined. */
  @Input('idPratica') idPratica?: number;
  /** Input per i dati in caso di modifica */
  @Input('generaliAmministrativi') configs: GeneraliAmministrativi;

  /** ViewChild connesso alla form: nuovaIstanzaForm. */
  @ViewChild('subNuovaIstanzaForm') subNuovaIstanzaForm: NgForm;
  /** ViewChild connesso alla form: nuovoProvvedimentoForm. */
  @ViewChild('subNuovoProvvedimentoForm') subNuovoProvvedimentoForm: NgForm;
  /** ViewChild connesso alla tabella: ipTable. */
  @ViewChild('ipTable') ipTable: RiscaTableComponent<IstanzaProvvedimentoVo>;

  /** Flag boolean che tiene traccia del submit della form: nuova istanza. */
  nuovaIstanzaSubmitted: boolean;
  /** Flag boolean che tiene traccia del submit della form: nuovo provvedimento. */
  nuovoProvvedimentoSubmitted: boolean;

  /** Lista di supporto contenente le informazioni per: tipologia pratica. */
  listaTipologiePratica: TipoRiscossioneVo[] = [];
  /** Lista di supporto contenente le informazioni per: codice utenza. */
  listaCodiciUtenza: ProvinciaCompetenzaVo[] = [];
  /** Lista di supporto contenente le informazioni per: tipo tributo utenza. */
  listaTipiTributiUtenza: any[] = [];
  /** Lista di supporto contenente le informazioni per: procedimento. */
  listaProcedimenti: TipoAutorizzazioneVo[] = [];
  /** Lista di supporto contenente le informazioni per: stato pratica. */
  listaStatiPratica: StatoRiscossioneVo[] = [];
  /** Lista di supporto contenente le informazioni per: tipologia istanza. */
  listaTipologieIstanza: TipoIstanzaProvvedimentoVo[] = [];
  /** Lista di supporto contenente le informazioni per: tipologia provvedimento. */
  listaTipologieProvvedimento: TipoIstanzaProvvedimentoVo[] = [];
  /** Lista di supporto contenente le informazioni per: tipo titolo. */
  listaTipiTitolo: TipoTitoloVo[] = [];

  /** Classe GAFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: GAFieldsConfigClass;
  /** Classe IstanzeProvvedimentiTable utilizzata per la gestione della tabella istanze/provvedimenti. */
  istanzeProvvedimentiTable: IstanzeProvvedimentiTable;

  /** Flag boolean che definisce se l'ambito corrente contiene opere pubbliche. */
  isAmbitoPraticaOperePubbliche = false;

  /** Boolean che tiene traccia dell'apertura/chiusura dell'accordion: nuova istanza. */
  istanzaApriChiudi = false;
  /** Boolean che tiene traccia dell'apertura/chiusura dell'accordion: nuovo provvedimento. */
  provvedimentoApriChiudi = false;

  /** Boolean che definisce se la pagina dei generali amministrativi deve risultare bloccata. */
  AEA_GADisabled: boolean = false;

  /** Boolean che definisce se lo stato della pratica è: RINUNCIA o REVOCA. */
  isStatoPratica_RIN_REV: boolean = false;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _datiContabili: DatiContabiliService,
    private _formBuilder: FormBuilder,
    private _generaliAmministrativi: GeneraliAmministrativiService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _pratiche: PraticheService,
    private _riscaAlert: RiscaAlertService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaModal: RiscaModalService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio il setup delle form
    this.initForms();
  }

  ngOnChanges(changes: SimpleChanges) {
    // Verifico se è stata modificata la lista in input
    if (changes.configs && !changes.configs.firstChange) {
      // Setup di possibili configurazioni in input con valorizzazione dati componente
      this.initComponenteConfigs();
      this.initSDInvioSpeciale();
    }
    // Verifico se è stata modificata la modalità del componente
    if (changes.modalita && !changes.modalita.firstChange) {
      // Abilito/Disabilito i campi in base alla modalità
      this._generaliAmministrativi.setDisabledPerModalita(
        this.mainForm,
        this.modalita
      );
    }
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Funzione che recupera la configurazione d'abilitazione della form
    this.setupGADisabled();
    // Setep degli errori da verificare al submit della form
    this.setupMainFormErrors();
    // Setup delle input form del componente
    this.setupFormInputs();
    // Setup delle liste per le select del form
    this.setupListeSelect();
    // Setup per la tabella istanze/provvedimenti
    this.setupIstanzeProvvedimentiTable();
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  setupGADisabled() {
    // Recupero la chiave per la configurazione della form
    const gaKey = this.AEAK_C.PAGINA_DATI_AMM;
    // Recupero la configurazione della form dal servizio
    this.AEA_GADisabled = this._accessoElementiApp.isAEADisabled(gaKey);
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [
      ...this.EM.MAP_FORM_GROUP_REQUIRED,
      ...this.EM.MAP_PATTERN_NUMBER,
      ...this.EM.MAP_PRATICA_PROGRESSIVO_UTENZA,
      ...this.EM.MAP_CALENDARIO_CONCESSIONE,
      ...this.EM.MAP_CALENDARIO_SOSPENSIONE,
      ...this.EM.MAP_CALENDARIO_SOSPENSIONE_REQ,
      ...this.EM.MAP_STATO_RISCOSSIONE,
      ...this.EM.MAP_ALMENO_UN_PROVVEDIMENTO,
    ];
  }

  /**
   * Funzione di setup per le liste per le select del form.
   */
  private setupListeSelect() {
    // Recupero l'idAmbito
    const idA = this._user.idAmbito;
    // Creo un oggetto con le varie richieste di scarico dati
    const requestList: IListeGAReq = {
      tipiRiscossione: this._pratiche.getTipiRiscossione(idA),
      codiciUtenza: this._pratiche.getProvinceCompetenza(),
      tipiTributiUtenza: of([]),
      procedimenti: this._pratiche.getTipiAutorizzazione(idA),
      tipiIstanza: this._pratiche.getTipiIstanza(idA),
      tipiProvvedimento: this._pratiche.getTipiProvvedimento(idA),
      tipiTitolo: this._pratiche.getTipiTitolo(idA),
      statiPratica: this._pratiche.getStatiRiscossione(idA),
    };

    setTimeout(() => {
      // Lancio il recupero delle informazioni
      forkJoin(requestList).subscribe({
        next: (response: IListeGARes) => {
          // Estraggo i dati
          const {
            tipiRiscossione,
            codiciUtenza,
            tipiTributiUtenza,
            procedimenti,
            tipiIstanza,
            tipiProvvedimento,
            tipiTitolo,
            statiPratica,
          } = response;

          // Richiamo le funzioni di setup delle liste
          this.onScaricoTipiRiscossione(tipiRiscossione);
          this.onScaricoCodiciUtenza(codiciUtenza);
          this.onScaricoTipiTributiUtenza(tipiTributiUtenza);
          this.onScaricoProcedimenti(procedimenti);
          this.onScaricoTipiIstanza(tipiIstanza);
          this.onScaricoTipiProvvedimento(tipiProvvedimento);
          this.onScaricoTipiTitolo(tipiTitolo);
          this.onScaricoStatiPratica(statiPratica);

          // Timeout per far triggherare tutti gli eventi di tutti i form control
          setTimeout(() => {
            // Effettuo un clean di ogni campo potenzialmente sporco, dato che le liste delle tendine sono state appena caricate
            this.mainForm.markAsPristine();
            // #
          }, 500);
        },
        error: (e: RiscaServerError) => {
          // Errore, gestisco la comunicazione
          this.onServiceError(e);
        },
      });
    });
  }

  /**
   * Funzione di setup delle inputs form del componente.
   */
  private setupFormInputs() {
    // Essendo le configurazioni parecchie, si utilizza una classe di supporto
    this.formInputs = new GAFieldsConfigClass(
      this._riscaFormBuilder,
      this._riscaUtilities
    );
  }

  /**
   * Funzione che effettua il setup della tabella istanze provvedimenti.
   */
  private setupIstanzeProvvedimentiTable() {
    // Variabili di comodo
    const disableUserInputs = this.disableUserInputs;
    // Definisco l'oggetto di configurazione
    const c: IIstanzeProvvedimentiTableConfigs = { disableUserInputs };

    // Creo l'oggetto per la gestione della tabella
    this.istanzeProvvedimentiTable = new IstanzeProvvedimentiTable(c);
  }

  /**
   * Funzione che effettua l'init per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Init della struttura della form principale
    this.initFormDatiGenAmm();
    // Init dei sotto form della pagina
    this.initFormNuovaIstanza();
    this.initFormNuovoProvvedimento();

    // Init dei listiner dei form per la manipolazione dei dati
    this.initFormListener();
    // Init di possibili configurazioni in input con valorizzazione dati componente
    this.initComponenteConfigs();
    // Init per verificare se la pratica ha uno stato debitorio con invio speciale
    this.initSDInvioSpeciale();
  }

  /**
   * Setup del form: mainForm.
   */
  private initFormDatiGenAmm() {
    this.mainForm = this._formBuilder.group(
      {
        tipologiaPratica: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        codiceUtenza: new FormControl(
          { value: null, disabled: true },
          { validators: [Validators.required] }
        ),
        tipoTributoUtenza: new FormControl({ value: null, disabled: false }),
        progressivoUtenza: new FormControl(
          { value: null, disabled: true },
          { validators: [Validators.pattern(this.riscaRegExp.onlyNumber)] }
        ),
        numeroPratica: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        statoPratica: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataRinunciaRevoca: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        procedimento: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        statoDebInvioSpeciale: new FormControl({
          value: false,
          disabled: true,
        }),
        prenotata: new FormControl({ value: false, disabled: false }),
        motivazione: new FormControl({ value: null, disabled: true }),
        note: new FormControl({ value: null, disabled: false }),
        dataInizioConcessione: new FormControl({
          value: null,
          disabled: false,
        }),
        dataFineConcessione: new FormControl({ value: null, disabled: false }),
        dataInizioSospensione: new FormControl({
          value: null,
          disabled: false,
        }),
        dataFineSospensione: new FormControl({ value: null, disabled: false }),
        istanzeProvvedimenti: new FormControl(
          { value: null, disabled: false },
          { validators: [arrayContainsAtLeastProvvedimentoValidator()] }
        ),
      },
      // Validatori campi incrociati
      {
        validators: [
          progressivoUtenzaPerTipologiaPraticaValidator(
            this._user.idAmbito,
            this.GA_C.TIPOLOGIA_PRATICA,
            this.GA_C.PROGRESSIVO_UTENZA,
            this.modalita
          ),
          calendarioNgbInizioFineValidator(
            this.GA_C.DATA_INIZIO_CONCESSIONE,
            this.GA_C.DATA_FINE_CONCESSIONE,
            this.EK.DATA_CONCESSIONE
          ),
          calendarioNgbInizioFineValidator(
            this.GA_C.DATA_INIZIO_SOSPENSIONE,
            this.GA_C.DATA_FINE_SOSPENSIONE,
            this.EK.DATA_SOSPENSIONE
          ),
          dataScadenzaConcessioneValidator(
            this._user.idAmbito,
            this.GA_C.DATA_FINE_CONCESSIONE,
            this.GA_C.PROCEDIMENTO,
            this.GA_C.STATO_PRATICA
          ),
          statoRiscossionePraticaValidator(
            this._user.idAmbito,
            this.GA_C.STATO_PRATICA,
            this.GA_C.DATA_INIZIO_SOSPENSIONE,
            this.GA_C.DATA_FINE_SOSPENSIONE,
            this.GA_C.DATA_FINE_CONCESSIONE,
            this.istanzeProvvedimentiTable
          ),
        ],
      }
    );

    // Abilito/Disabilito i campi in base alla modalità
    this._generaliAmministrativi.setDisabledPerModalita(
      this.mainForm,
      this.modalita
    );

    // Verifico il flag di disabilitazione della pagina
    if (this.disableUserInputs) {
      // Disabilito la form
      this.mainForm.disable();
    }
  }

  /**
   * Setup del form: nuovaIstanzaForm.
   */
  private initFormNuovaIstanza() {
    this.nuovaIstanzaForm = this._formBuilder.group({
      tipologiaIstanza: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      numeroIstanza: new FormControl({ value: null, disabled: false }),
      dataIstanza: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      noteIstanza: new FormControl({ value: null, disabled: false }),
    });

    // Verifico il flag di disabilitazione della pagina
    if (this.disableUserInputs) {
      // Disabilito la form
      this.nuovaIstanzaForm.disable();
    }
  }

  /**
   * Setup del form: nuovoProvvedimentoForm.
   */
  private initFormNuovoProvvedimento() {
    this.nuovoProvvedimentoForm = this._formBuilder.group(
      {
        tipologiaProvvedimento: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        tipoTitolo: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        numeroProvvedimento: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        dataProvvedimento: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        noteProvvedimento: new FormControl({ value: null, disabled: false }),
      },
      {
        validators: [
          dataProvvedimentoValidator(
            this._user.idAmbito,
            this.GA_C.DATA_PROVVEDIMENTO
          ),
        ],
      }
    );

    // Verifico il flag di disabilitazione della pagina
    if (this.disableUserInputs) {
      // Disabilito la form
      this.nuovoProvvedimentoForm.disable();
    }
  }

  /**
   * Setup dei listener sulle form della pagina.
   */
  private initFormListener() {
    // Listener al cambio di un uso di legge, verranno popolate le informazioni dei campi connessi
    this.mainForm
      .get(this.GA_C.TIPOLOGIA_PRATICA)
      .valueChanges.subscribe((tipoPratica: TipoRiscossioneVo) => {
        // Aggiorno lo stato per i campi connessi
        this.bloccaSbloccaCampiConnessiTipoPratica(tipoPratica);
        // Verifico se il campo Tipo Tributo Utenza è d'abilitare
        this.abilitaTipoTributoUtenza(tipoPratica);
      });

    // Listener al cambio di un progressivo utenza
    this.mainForm
      .get(this.GA_C.PROGRESSIVO_UTENZA)
      .valueChanges.subscribe((progressivoUtenza: string) => {
        // Lancio la funzione di completamento del progressivo
        this.completaProgressivoUtenza();
      });

    // Listener al cambio del campo prenotata
    this.mainForm
      .get(this.GA_C.PRENOTATA)
      .valueChanges.subscribe((prenotata: IRiscaCheckboxData) => {
        // Eseguo le logiche al cambio del check
        this.prenotataChange(prenotata);
      });

    // Listener al cambio del campo stato pratica
    this.mainForm
      .get(this.GA_C.STATO_PRATICA)
      .valueChanges.subscribe((statoPratica: StatoRiscossioneVo) => {
        // Eseguo le logiche al cambio del check
        this.statoPraticaChange(statoPratica);
      });
  }

  /**
   * Popola i campi della form in base all'oggetto da modificare che é stato passato.
   */
  private initComponenteConfigs() {
    // verifico esistano configurazioni
    if (!this.configs) {
      return;
    }

    // In questa parte si controllano tutte le proprietà dell'oggetto configs e prevalorizzare
    // i relativi campi di mainForm

    // Esistono 2 passaggi:
    //- questa funzione va ad assegnare i valori del form che non dipendono da liste
    // tutto ciò che non è dipendente dalle liste scaricate lo metto qui
    //- tutto ciò che è dipendente dalle liste lo devo andare a gestire all'interno dell'onScarico()

    // NOTA: le liste sono gestite dopo il caricamento delle liste
    this.setProgressivoUtenza(this.configs.progressivoUtenza);
    if (this.configs.numeroPratica) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.NUMERO_PRATICA,
        this.configs.numeroPratica
      );
    }
    if (this.configs.progressivoUtenza) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.PROGRESSIVO_UTENZA,
        this.configs.progressivoUtenza
      );
    }
    if (this.configs.dataRinunciaRevoca) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.DATA_RINUNCIA_REVOCA,
        this.configs.dataRinunciaRevoca
      );
    }
    if (this.configs.prenotata) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.PRENOTATA,
        this.configs.prenotata
      );
      // Aggiorno il source della configurazione dell'input della checkbox
      this._riscaUtilities.updateRFICheckboxSource(
        this.formInputs.prenotataConfig,
        this.configs.prenotata
      );
      // Aggiorno il form
      this.prenotataChange(this.configs.prenotata);
    }
    if (this.configs.motivazione) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.MOTIVAZIONE,
        this.configs.motivazione
      );
    }
    if (this.configs.note) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.NOTE,
        this.configs.note
      );
    }
    if (this.configs.dataInizioConcessione) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.DATA_INIZIO_CONCESSIONE,
        this.configs.dataInizioConcessione
      );
    }
    if (this.configs.dataFineConcessione) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.DATA_FINE_CONCESSIONE,
        this.configs.dataFineConcessione
      );
    }
    if (this.configs.dataInizioSospensione) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.DATA_INIZIO_SOSPENSIONE,
        this.configs.dataInizioSospensione
      );
    }
    if (this.configs.dataFineSospensione) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.DATA_FINE_SOSPENSIONE,
        this.configs.dataFineSospensione
      );
    }

    // Popolo le liste di istanze e provvedimenti.
    if (this.configs.istanzeProvvedimenti?.length > 0) {
      // Rimuovo gli elementi dalla tabella
      this.istanzeProvvedimentiTable.clear();
      // Inserisco i nuovi elementi
      const istProv = this.configs.istanzeProvvedimenti;
      this.istanzeProvvedimentiTable.setNewElements(istProv);
    }
  }

  /**
   * Funzione di comodo che verifica se la pratica in modifica ha uno stato debitorio con invio speciale.
   */
  private initSDInvioSpeciale() {
    // Verifico la modalità del componente
    if (this.inserimento) {
      // In inserimento non esistono ancora stati debitori
      this.sdInvioSpeciale = false;
      // #
    } else if (this.idPratica != undefined) {
      // Recupero l'id della pratica
      const idP = this.idPratica;
      // Utilizzo l'API per scaricare lo stato dell'invio speciale
      this._datiContabili.praticaConSDInvioSpeciale(idP).subscribe({
        next: (praticaSDInvSpec: boolean) => {
          // Aggiorno la variabile per l'invio speciale di uno stato debitorio della pratica
          this.sdInvioSpeciale = praticaSDInvSpec;
          // #
        },
        error: (e: RiscaServerError) => {
          // Errore, gestisco la comunicazione
          this.onServiceError(e);
        },
      });
      // #
    } else {
      // In inserimento non esistono ancora stati debitori
      this.sdInvioSpeciale = false;
    }
  }

  /**
   * #############################
   * FUNZIONI RESPONSE DELLE LISTE
   * #############################
   */

  /**
   * Funzione richiamata quando lo scarico dei dati TipoRiscossioneVo è completata.
   * La funzione effettuerà il setup delle informazioni.
   * @param listaTipiRiscossione Array di TipoRiscossioneVo contenente la lista di oggetti scaricati.
   */
  private onScaricoTipiRiscossione(listaTipiRiscossione: TipoRiscossioneVo[]) {
    // Valorizzo il campo se presente un valore di configs
    if (this.configs?.tipologiaPratica) {
      // Variabili di comodo
      const idTPC = this.configs?.tipologiaPratica?.id_tipo_riscossione;
      // Cerco l'indice dell'oggetto per stesso id
      const i = listaTipiRiscossione.findIndex(
        (tp) => tp.id_tipo_riscossione === idTPC
      );

      // Verifico che esista l'indice
      if (i !== -1) {
        // Recupero l'oggetto
        const tipoRiscossione = listaTipiRiscossione[i] as any;
        // Aggiorno l'oggetto referenziato per risca-select
        tipoRiscossione.__selected = true;
        // Imposto il valore nel form
        this._riscaUtilities.setFormValue(
          this.mainForm,
          this.GA_C.TIPOLOGIA_PRATICA,
          tipoRiscossione
        );
      }
    }

    // Assegno la lista localmente
    this.listaTipologiePratica = listaTipiRiscossione;
  }

  /**
   * Funzione richiamata quando lo scarico dei dati ProvinciaCompetenzaVo è completata.
   * La funzione effettuerà il setup delle informazioni.
   * @param listaCodiciUtenza Array di ProvinciaCompetenzaVo contenente la lista di oggetti scaricati.
   */
  private onScaricoCodiciUtenza(listaCodiciUtenza: ProvinciaCompetenzaVo[]) {
    // Assegno la lista localmente
    this.listaCodiciUtenza = listaCodiciUtenza;

    // Valorizzo il campo se presente un valore di configs
    if (this.configs?.codiceUtenza) {
      // Lancio una sanitizzazione degli oggetti qualora ci siano già dei dafeult
      this._riscaUtilities.sanitizeRiscaSelectedFlag(this.listaCodiciUtenza);

      // Variabili di comodo
      const id = this.configs?.codiceUtenza?.cod_provincia;
      // Cerco l'indice dell'oggetto per stesso id
      const i = this.listaCodiciUtenza.findIndex(
        // Il BE ci manda la sigla provincia. La snapshot memorizza il codice provincia. Quindi matcho uno o l'altro.
        (tp) => tp.cod_provincia == id || tp.sigla_provincia == id
      );

      // Verifico che esista l'indice
      if (i !== -1) {
        // Recupero l'oggetto
        const codiceUtenza = this.listaCodiciUtenza[i] as any;
        // Aggiorno l'oggetto referenziato per risca-select
        codiceUtenza.__selected = true;
        // Imposto il valore nel form
        this._riscaUtilities.setFormValue(
          this.mainForm,
          this.GA_C.CODICE_UTENZA,
          codiceUtenza
        );
      }
    }
  }

  /**
   * Funzione richiamata quando lo scarico dei dati any è completata.
   * La funzione effettuerà il setup delle informazioni.
   * @param listaTipiTributiUtenza Array di any contenente la lista di oggetti scaricati.
   */
  private onScaricoTipiTributiUtenza(listaTipiTributiUtenza: any[]) {
    // Assegno la lista localmente
    this.listaTipiTributiUtenza = listaTipiTributiUtenza;

    // Valorizzo il campo se presente un valore di configs
    if (this.configs?.tipoTributoUtenza) {
      // Variabili di comodo
      const id = this.configs.tipoTributoUtenza.campo;
      // Cerco l'indice dell'oggetto per stesso id
      const i = this.listaTipiTributiUtenza.findIndex(
        (tp: any) => tp.campo === id
      );

      // Verifico che esista l'indice
      if (i !== -1) {
        // Recupero l'oggetto
        const tipoTributo = this.listaTipiTributiUtenza[i] as any;
        // Aggiorno l'oggetto referenziato per risca-select
        tipoTributo.__selected = true;
        // Imposto il valore nel form
        this._riscaUtilities.setFormValue(
          this.mainForm,
          this.GA_C.TIPO_TRIBUTO_UTENZA,
          tipoTributo
        );
      }
    }
  }

  /**
   * Funzione richiamata quando lo scarico dei dati TipoAutorizzazioneVo è completata.
   * La funzione effettuerà il setup delle informazioni.
   * @param listaProcedimenti Array di TipoAutorizzazioneVo contenente la lista di oggetti scaricati.
   */
  private onScaricoProcedimenti(listaProcedimenti: TipoAutorizzazioneVo[]) {
    // Assegno la lista localmente
    this.listaProcedimenti = listaProcedimenti;

    // Valorizzo il campo se presente un valore di configs
    if (this.configs?.procedimento) {
      // Variabili di comodo
      const id = this.configs.procedimento.id_tipo_autorizza;
      // Cerco l'indice dell'oggetto per stesso id
      const i = this.listaProcedimenti.findIndex(
        (t) => t.id_tipo_autorizza === id
      );

      // Verifico che esista l'indice
      if (i !== -1) {
        // Recupero l'oggetto
        const procedimento = this.listaProcedimenti[i] as any;
        // Aggiorno l'oggetto referenziato per risca-select
        procedimento.__selected = true;
        // Imposto il valore nel form (uso la timeout per problemi di velocità di scarico dei dati rispetto al rendering)
        setTimeout(() => {
          this._riscaUtilities.setFormValue(
            this.mainForm,
            this.GA_C.PROCEDIMENTO,
            procedimento
          );
        });
      }
    }
  }

  /**
   * Funzione richiamata quando lo scarico dei dati TipoProvvedimentoVo è completata.
   * La funzione effettuerà il setup delle informazioni.
   * @param listaTipiIstanza Array di TipoProvvedimentoVo contenente la lista di oggetti scaricati.
   */
  private onScaricoTipiIstanza(listaTipiIstanza: TipoIstanzaProvvedimentoVo[]) {
    // Assegno la lista localmente
    this.listaTipologieIstanza = listaTipiIstanza;
  }

  /**
   * Funzione richiamata quando lo scarico dei dati TipoProvvedimentoVo è completata.
   * La funzione effettuerà il setup delle informazioni.
   * @param listaTipiProvvedimento Array di TipoProvvedimentoVo contenente la lista di oggetti scaricati.
   */
  private onScaricoTipiProvvedimento(
    listaTipiProvvedimento: TipoIstanzaProvvedimentoVo[]
  ) {
    // Assegno la lista localmente
    this.listaTipologieProvvedimento = listaTipiProvvedimento;
  }

  /**
   * Funzione richiamata quando lo scarico dei dati TipoTitoloVo è completata.
   * La funzione effettuerà il setup delle informazioni.
   * @param listaTipiTitolo Array di TipoTitoloVo contenente la lista di oggetti scaricati.
   */
  private onScaricoTipiTitolo(listaTipiTitolo: TipoTitoloVo[]) {
    // Assegno la lista localmente
    this.listaTipiTitolo = listaTipiTitolo;
  }

  /**
   * Funzione richiamata quando lo scarico dei dati StatoRiscossioneVo è completata.
   * La funzione effettuerà il setup delle informazioni.
   * @param listaStatiPratica Array di StatoRiscossioneVo contenente la lista di oggetti scaricati.
   */
  private onScaricoStatiPratica(listaStatiPratica: StatoRiscossioneVo[]) {
    this.listaStatiPratica = listaStatiPratica;

    // Valorizzo il campo se presente un valore di configs
    if (this.configs?.statoPratica) {
      // Variabili di comodo
      const id = this.configs.statoPratica.id_stato_riscossione;
      // Cerco l'indice dell'oggetto per stesso id
      const i = this.listaStatiPratica.findIndex(
        (t) => t.id_stato_riscossione == id
      );

      // Verifico che esista l'indice
      if (i !== -1) {
        // Recupero l'oggetto
        const statoPratica = this.listaStatiPratica[i] as any;
        // Creo una copia per inserire l'oggetto nel form
        const statoPraticaForm = statoPratica;
        // Aggiorno l'oggetto referenziato per risca-select
        statoPraticaForm.__selected = true;
        // Imposto il valore nel form
        this._riscaUtilities.setFormValue(
          this.mainForm,
          this.GA_C.STATO_PRATICA,
          statoPraticaForm
        );
        // Lancio la gestione specifica della data dataRinunciaRevoca
        this.statoPraticaChange(statoPratica);
      }
    }
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione di supporto per bloccare o sbloccare i campi della form connessi al campo tipologiaPratica.
   * @param tipoPratica TipoRiscossioneVo che identifica il nuovo valore.
   */
  private bloccaSbloccaCampiConnessiTipoPratica(
    tipoPratica: TipoRiscossioneVo
  ) {
    // Se sono in modifica non deve avvenire il blocco sblocco
    if (this.checkGAFormDisabledIsModifica) {
      // Fallback
      return;
    }

    // Definisco le configurazion per enable e disable
    const config = { emitEvent: false };
    // Definisco le chiavi del form
    const codUtenza = this.GA_C.CODICE_UTENZA;
    const progressivo = this.GA_C.PROGRESSIVO_UTENZA;
    const statoPratica = this.GA_C.STATO_PRATICA;

    // Verifico che l'oggetto esista
    if (tipoPratica) {
      // Sblocco i campi
      this.mainForm.get(codUtenza).enable(config);
      this.mainForm.get(progressivo).enable(config);
      this.mainForm.get(statoPratica).enable(config);
    } else {
      // Blocco i campi
      this.mainForm.get(codUtenza).disable(config);
      this.mainForm.get(progressivo).disable(config);
      this.mainForm.get(statoPratica).disable(config);
    }
  }

  /**
   * Funzione di supporto per visualizzare o nascondere il campo Tipo Tributo Pratica.
   * Se il codice dell'ambito è uguale a COD_OPERE_PUBBLICHE, allora il campo verrà abilitato.
   * @param tipoPratica TipoRiscossioneVo che identifica i dati da confrontare.
   */
  private abilitaTipoTributoUtenza(tipoPratica: TipoRiscossioneVo) {
    // Verifico che l'input esista, e contenga l'oggetto ambito
    if (!tipoPratica || !tipoPratica.ambito) {
      return;
    }

    // Verifico che l'id ambito quello delle opere pubbliche
    if (tipoPratica.ambito.cod_ambito === this.GA_C.COD_OPERE_PUBBLICHE) {
      // Abilito il campo
      this.isAmbitoPraticaOperePubbliche = true;
    }
  }

  /**
   * Funzione sul blur del campo progressivoUtenza del form mainForm.
   */
  completaProgressivoUtenza() {
    // Richiamo il servizio per completare il progressivo utenza
    this._generaliAmministrativi.completaProgressivoUtenza(this.mainForm);
  }

  /**
   * Funzione che gestisce le logiche al cambio del flag prenotata.
   * @param prenotata IRiscaCheckboxData con i dati della checkbox.
   */
  private prenotataChange(prenotata: IRiscaCheckboxData) {
    // Verifico lo stato della form
    if (this.checkGAFormDisabled) {
      // Blocco le logiche
      return;
    }

    // Definisco le configurazioni
    const f = this.mainForm;
    const fcn = this.GA_C.MOTIVAZIONE;
    const v = [];
    const uC = { onlySelf: true, emitEvent: false };

    // Verifico lo stato della checkbox
    if (prenotata?.check) {
      // Imposto come obbligatorio la motivazione
      v.push(Validators.required);
      // Abilito il campo motivazione
      f.get(fcn).enable();
      // #
    } else {
      // Disabilito il campo motivazione
      f.get(fcn).disable();
      // Pulisco il valore di motivazione
      this._riscaUtilities.setFormValue(f, fcn, null);
    }

    // Imposto come obbligatorio la motivazione
    this._riscaUtilities.setFieldValidator(f, fcn, v, true, uC);
    // Aggiorno campi e validatori
    this._riscaUtilities.updateValueAndValidity(f, fcn, uC);
  }

  /**
   * Funzione che gestisce le logiche al cambio dello stato della pratica.
   * @param statoPratica StatoRiscossioneVo con lo stato della pratica.
   */
  private statoPraticaChange(statoPratica: StatoRiscossioneVo) {
    // Verifico lo stato della form
    if (this.checkGAFormDisabled) {
      // Blocco le logiche
      return;
    }

    // Lancio la gestione per la data rinuncia
    this.handleDataRinunciaField(statoPratica);
  }

  /**
   * Funzione che gestisce le logiche specifiche per il campo "Data rinuncia".
   * @param statoPratica StatoRiscossioneVo con lo stato della pratica selezionata.
   */
  private handleDataRinunciaField(statoPratica: StatoRiscossioneVo) {
    // Definisco le configurazioni
    const f = this.mainForm;
    const fcn = this.GA_C.DATA_RINUNCIA_REVOCA;
    const v = [];
    const uC = { onlySelf: true, emitEvent: false };
    // Recupero dall'input il codice dello stato della pratica
    const codStatoP: string = statoPratica?.cod_stato_riscossione;
    // Definisco le condizioni per la gestione specifica dello stato
    const isCodSP_RINUNCIATA = codStatoP === CodStatiRiscossione.RINUNCIATA;
    const isCodSP_REVOCATA = codStatoP === CodStatiRiscossione.REVOCATA;

    // Verifico lo stato della pratica
    if (isCodSP_RINUNCIATA || isCodSP_REVOCATA) {
      // Visualizzo l'input nella pagina
      this.isStatoPratica_RIN_REV = true;
      // Imposto i validatori del campo
      v.push(Validators.required);
      // Gestisco l'abilitazione del campo del form
      f.get(fcn).enable();
      // #
    } else {
      // Nascondo l'input nella pagina
      this.isStatoPratica_RIN_REV = false;
      // Resetto la gestione campo del form
      f.get(fcn).reset();
      // Gestisco l'abilitazione del campo del form
      f.get(fcn).disable();
    }

    // Imposto come obbligatorio la motivazione
    this._riscaUtilities.setFieldValidator(f, fcn, v, true, uC);
    // Aggiorno campi e validatori
    this._riscaUtilities.updateValueAndValidity(f, fcn, uC);
  }

  /**
   * Funzione che effettua il reset dei dati della form datti tecnici ambiente.
   * Richiamabile dal padre.
   * @override
   */
  onFormReset() {
    // Se mi trovo in inserimento, cancello tutto. Altrimenti torno alle informazioni originali.
    if (this.modalita == AppActions.modifica) {
      // Eseguo la funzione di setup dei campi
      this.initComponenteConfigs();
      this.initSDInvioSpeciale();
      // Richiamo le funzioni di setup delle liste
      this.onScaricoTipiRiscossione(this.listaTipologiePratica);
      this.onScaricoCodiciUtenza(this.listaCodiciUtenza);
      this.onScaricoTipiTributiUtenza(this.listaTipiTributiUtenza);
      this.onScaricoProcedimenti(this.listaProcedimenti);
      this.onScaricoTipiIstanza(this.listaTipologieIstanza);
      this.onScaricoTipiProvvedimento(this.listaTipologieProvvedimento);
      this.onScaricoTipiTitolo(this.listaTipiTitolo);
      this.onScaricoStatiPratica(this.listaStatiPratica);
    } else {
      // Se sono in inserimento (o in un altro caso?) devo cancellare i campi
      // Richiamo il reset sulla form dati tecnici ambiente
      this.mainForm.reset();
      // Resetto il flag di submit
      this.mainFormSubmitted = false;

      // Chiudo gli accordion
      this.istanzaApriChiudi = false;
      this.provvedimentoApriChiudi = false;
      // Resetto le sub form
      this.resetNuovaIstanzaForm();
      this.resetNuovoProvvedimentoForm();

      // Resetto l'oggetto della tabella
      this.setupIstanzeProvvedimentiTable();
    }
  }

  /**
   * Funzione invocata prima della validazione del mainform.
   * Aggiunge tutte le informazioni esterne al form, per poi essere prese assieme alle altre nel getRawValue()
   * @override
   */
  prepareMainFormForValidation() {
    // Recupero istanze e provvedimenti dalla tabella
    const istanzeEProvvedimenti =
      (this.istanzeProvvedimentiTable.getDataSource() as (
        | NuovaIstanzaFormData
        | NuovoProvvedimentoFormData
      )[]) || [];

    // Aggiorno il valore nel form
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.GA_C.ISTANZE_PROVVEDIMENTI,
      istanzeEProvvedimenti
    );
  }

  /**
   * Funzione esposta per il componente padre che permette il set del progressivo utenza.
   * @param progressivoUtenza string progressivo utenza generato dal server (se non definito nella form).
   */
  setProgressivoUtenza(progressivoUtenza: string) {
    // Verifico che l'input esista
    if (!progressivoUtenza) {
      return;
    }

    // Recupero il progressivo utenza nella form
    const progressivoUtenzaF = this._riscaUtilities.getFormValue(
      this.mainForm,
      this.GA_C.PROGRESSIVO_UTENZA
    );

    // Verifico se esiste il dato
    if (progressivoUtenzaF === null || progressivoUtenzaF === '') {
      // Aggiorno il dato
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.GA_C.PROGRESSIVO_UTENZA,
        progressivoUtenza
      );
      // Lancio la formattazione del dato
      this.completaProgressivoUtenza();
    }
  }

  /**
   * Funzione che permette di aggiornare istanze e provvedimenti della tabella con i dati salvati dal server
   * @param istanzeProvvedimenti
   */
  updateIstanzeProvvedimenti(istanzeProvvedimenti: IstanzaProvvedimentoVo[]) {
    // Converto istanze e provvedimenti in input
    const tnip: TNIPFormData[] =
      this._generaliAmministrativi.convertIstanzeProvvedimentiVoToTNIPFormDatas(
        istanzeProvvedimenti
      );
    // Lancio la clear della tabella istanze-provvedimenti
    this.istanzeProvvedimentiTable.clear();
    // Setto il nuovo array di dati nella tabella
    this.istanzeProvvedimentiTable.setElements(tnip);
    // Lancio l'ordinamento attuale della tabella
    this.ipTable.currentSortData();
  }

  /**
   * ################
   * GESTIONE ISTANZE
   * ################
   */

  /**
   * Funzione di toggle per la visualizzazione della form nuovaIstanzaForm.
   * L'area di visualizzazione della form nuovoProvvedimentoForm verrà chiusa.
   * @param stato boolean che definisce il nuovo stato dello switch accordion.
   */
  toggleNuovaIstanza(stato: boolean) {
    // Effettuo il toggle per la visualizzazione
    this.istanzaApriChiudi = !this.istanzaApriChiudi;

    // Verifico se sto aprendo l'accordion
    if (this.istanzaApriChiudi) {
      // #### APERTURA ISTANZA ####
      // Lancio le logiche per l'apertura dell'accordion
      this.apriIstanza();
      // #
    } else {
      // Chiudo e resetto il form dell'istanza
      this.resetNuovaIstanzaForm(true);
    }
  }

  /**
   * Funzione che gestisce le logiche di apertura dell'accordion per una nuova istanza.
   */
  apriIstanza() {
    // Verifico se il provvedimento è aperto
    if (this.provvedimentoApriChiudi) {
      // Resetto il form e chiudo l'accordion
      this.resetNuovoProvvedimentoForm(true);
    }
  }

  /**
   * Funzione di appoggio per l'emit della submit per la form: nuovaIstanzaForm.
   */
  emitNuovaIstanzaSubmit() {
    // NOTA: è usato come workaround per le form innestate
    this.subNuovaIstanzaForm.ngSubmit.emit();
  }

  /**
   * Funzione di submit per il form nuovaIstanzaForm.
   */
  nuovaIstanzaSubmit() {
    // Il form è stato submittato
    this.nuovaIstanzaSubmitted = true;
    // Verifico che la form sia valida
    if (this.nuovaIstanzaForm.valid) {
      // Aggiungo alla tabella l'istanza
      this.aggiungiIstanza(this.nuovaIstanzaForm.getRawValue());
      // Resetto la form
      this.resetNuovaIstanzaForm();
      // Aggiorno la validazione per la form principale
      this.mainForm.updateValueAndValidity();
    }
  }

  /**
   * Funzione che aggiunge ai dati del form: mainForm; le informazioni di una nuova istanza.
   * @param istanza NuovaIstanzaFormData contente le informazioni da inserire all'interno del form mainForm.
   */
  private aggiungiIstanza(istanza: NuovaIstanzaFormData) {
    // Richiamo la funzione di aggiunta della tabella
    this.istanzeProvvedimentiTable.addElement(istanza);
  }

  /**
   * Funzione di reset per il form: nuovaIstanzaForm.
   * @param chiudiAccordion boolean che specifica se l'accordion è da chiudere. Default è: false.
   */
  private resetNuovaIstanzaForm(chiudiAccordion = false) {
    // Verifico se l'accordion è da chiudere
    if (chiudiAccordion) {
      // Chiudo l'accordion
      this.istanzaApriChiudi = false;
    }

    // Resetto il form
    this.nuovaIstanzaForm?.reset();
    // Resetto il flag di submit
    this.nuovaIstanzaSubmitted = false;
  }

  /**
   * ######################
   * GESTIONE PROVVEDIMENTI
   * ######################
   */

  /**
   * Funzione di toggle per la visualizzazione della form nuovoProvvedimentoForm.
   * L'area di visualizzazione della form nuovaIstanzaForm verrà chiusa.
   * @param stato boolean che definisce il nuovo stato dello switch accordion.
   */
  toggleNuovoProvvedimento(stato: boolean) {
    // Effettuo il toggle per la visualizzazione
    this.provvedimentoApriChiudi = !this.provvedimentoApriChiudi;

    // Verifico se sto aprendo l'accordion
    if (this.provvedimentoApriChiudi) {
      // #### APERTURA ISTANZA ####
      // Lancio le logiche per l'apertura dell'accordion
      this.apriProvvedimento();
      // #
    } else {
      // Chiudo e resetto il form dell'istanza
      this.resetNuovoProvvedimentoForm(true);
    }
  }

  /**
   * Funzione che gestisce le logiche di apertura dell'accordion per un nuovo provvedimento.
   */
  apriProvvedimento() {
    // Verifico se il provvedimento è aperto
    if (this.istanzaApriChiudi) {
      // Resetto il form e chiudo l'accordion
      this.resetNuovaIstanzaForm(true);
    }
  }

  /**
   * Funzione di appoggio per l'emit della submit per la form: nuovoProvvedimentoForm.
   */
  emitNuovoProvvedimentoSubmit() {
    // NOTA: è usato come workaround per le form innestate
    this.subNuovoProvvedimentoForm.ngSubmit.emit();
  }

  /**
   * Funzione di submit per il form nuovaIstanzaForm.
   */
  nuovoProvvedimentoSubmit() {
    // Il form è stato submittato
    this.nuovoProvvedimentoSubmitted = true;
    // Verifico che la form sia valida
    if (this.nuovoProvvedimentoForm.valid) {
      // Aggiungo alla tabella il provvedimento
      this.aggiungiProvvedimento(this.nuovoProvvedimentoForm.getRawValue());
      // Resetto la form
      this.resetNuovoProvvedimentoForm();
      // Aggiorno la validazione per la form principale
      this.mainForm.updateValueAndValidity();
    }
  }

  /**
   * Funzione che aggiunge ai dati del form: mainForm; le informazioni di un nuovo provvedimento.
   * @param provvedimento NuovoProvvedimentoFormData contenente le informazioni da inserire all'interno del form mainForm.
   */
  private aggiungiProvvedimento(provvedimento: NuovoProvvedimentoFormData) {
    // Richiamo la funzione di aggiunta della tabella
    this.istanzeProvvedimentiTable.addElement(provvedimento);
  }

  /**
   * Funzione di reset per il form: nuovoProvvedimentoForm.
   * @param chiudiAccordion boolean che specifica se l'accordion è da chiudere. Default è: false.
   */
  private resetNuovoProvvedimentoForm(chiudiAccordion = false) {
    // Verifico se l'accordion è da chiudere
    if (chiudiAccordion) {
      // Chiudo l'accordion
      this.provvedimentoApriChiudi = false;
    }

    // Resetto i dati del form
    this.nuovoProvvedimentoForm?.reset();
    // Resetto il flag di submit
    this.nuovoProvvedimentoSubmitted = false;
  }

  /**
   * #####################################
   * FUNZIONI COMUNI ISTANZE PROVVEDIMENTI
   * #####################################
   */

  /**
   * Funzione collegata all'HTML che permette l'eliminazione di una riga della tabella.
   * @param row RiscaTableDataConfig<any> contenente i dati della riga.
   */
  eliminaIstanzaProvvedimento(row: RiscaTableDataConfig<any>) {
    // Definisco la funzione di callback nel caso in cui l'utente confermi la cancellazione della riga
    const onConfirm = () => {
      // Richiamo la remove della table
      this.onConfirmEliminaIP(row);
      // #
    };
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConfermaCancellazione({ onConfirm });
  }

  /**
   * Funzione di supporto che gestisce le logiche di cancellazione dei dati di un'istanza/provvedimento dalla tabella.
   * @param row RiscaTableDataConfig<TNIPFormData> con i dati della riga di tabella.
   */
  private onConfirmEliminaIP(row: RiscaTableDataConfig<TNIPFormData>) {
    /**
     * RISCA-816 => la delete del dato deve avvenire solo al SALVA dei dati riscossione.
     * Era stata fatta una modifica per la gestione di istanze/provvedimenti per un legame con gli stati debitori.
     * Con questa segnalazione, dato che il legame non è più presente, la gestione specifica è stata rimossa e sarà il servizio a gestire i dati.
     */
    // Richiamo la cancellazione della riga della tabella
    this.deleteIPFromTable(row);
  }

  /**
   * Funzione di comodo per la cancellazione di una riga dalla tabella istanze provvedimenti.
   * @param row RiscaTableDataConfig<TNIPFormData> da cancellare.
   */
  private deleteIPFromTable(row: RiscaTableDataConfig<TNIPFormData>) {
    // Richiamo la remove della table
    this.istanzeProvvedimentiTable.removeElement(row);
    // Aggiorno la validazione per la form principale
    this.mainForm.updateValueAndValidity();
  }

  /**
   * Funzione collegata all'evento specifico della select al cambio dato.
   * @param value any con il valore ritornato dalla selezione della select.
   * @param idFormControl string con il nome del form control del dato.
   */
  onManualEmitterIstanza(value: any, idFormControl: string) {
    // Effettuo il set del dato forzato all'interno del form
    this.onManualEmitter(value, idFormControl, this.nuovaIstanzaForm);
  }

  /**
   * Funzione collegata all'evento specifico della select al cambio dato.
   * @param value any con il valore ritornato dalla selezione della select.
   * @param idFormControl string con il nome del form control del dato.
   */
  onManualEmitterProvvedimento(value: any, idFormControl: string) {
    // Effettuo il set del dato forzato all'interno del form
    this.onManualEmitter(value, idFormControl, this.nuovoProvvedimentoForm);
  }

  /**
   * Funzione che modifica le informazioni di un determinato form.
   * La funzione è strettamente collegata alle funzioni di ascolto manuale del cambio dati per istanze/provvedimenti.
   * @param value any con il valore ritornato dalla selezione della select.
   * @param idFormControl string con il nome del form control del dato.
   */
  private onManualEmitter(value: any, idFormControl: string, form: FormGroup) {
    // Definisco l'oggetto di configurazione per la propagazione del dato
    const o: FormUpdatePropagation = { emitEvent: false, onlySelf: false };
    // Effettuo il set del dato forzato all'interno del form
    form?.get(idFormControl)?.setValue(value, o);
  }

  /**
   * ##################################
   * FUNZIONI DI COMODO EXTRA UTILITIES
   * ##################################
   */

  /**
   * Funzione di comodo per il get dati in una form.
   * @param form FormGroup per la quale recuperare il valore.
   * @param key string che definisce il nome del FormControl per il get del valore.
   */
  getFormValue(form: FormGroup, key: string): any {
    // Richiamo la funzione del servizio di utility
    return this._riscaUtilities.getFormValue(form, key);
  }

  /**
   * Funzione di comodo che gestisce gli errori generati dal server.
   * @param e RiscaServerError che definisce l'errore generato da una chiamata del servizio andata male.
   */
  onServiceError(e: RiscaServerError) {
    // Gestisco il messaggio d'errore inaspettato
    const a = this._riscaAlert.createAlertFromServerError(e);
    // Comunico al padre che si è verificato un errore
    this.notifyParent(a);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo per il check sulle configurazioni principali del componente.
   */
  get checkGAFormDisabled() {
    // Verifico le condizioni per la disabilitazione dei campi
    const formDisabled = this.mainForm.disabled;
    // Verifico il flag per la gestione abilitata
    return formDisabled;
  }

  /**
   * Getter di comodo per il check sulle configurazioni principali del componente.
   */
  get checkGAFormDisabledIsModifica() {
    // Verifico le condizioni per la disabilitazione dei campi
    const isModifica = this.modalita == AppActions.modifica;
    const formDisabled = this.checkGAFormDisabled;

    // Verifico il flag per la gestione abilitata
    return isModifica || formDisabled;
  }

  /**
   * Getter per il valore del form: statoDebInvioSpeciale.
   * @returns boolean che definisce se la pratica ha uno stato debitorio con invio speciale.
   */
  get sdInvioSpeciale(): boolean {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.GA_C.STATO_DEBITORIO_INVIO_SPECIALE;

    // Recupero il valore dalla form
    const sdInvioSpeciale: IRiscaCheckboxData = f?.get(k)?.value;
    // Ritorno il valore specifico
    return sdInvioSpeciale?.check ?? false;
  }

  /**
   * Setter per il valore del form: statoDebInvioSpeciale.
   * @param sdInvioSpeciale boolean che definisce se la pratica ha uno stato debitorio con invio speciale.
   */
  set sdInvioSpeciale(sdInvioSpeciale: boolean) {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.GA_C.STATO_DEBITORIO_INVIO_SPECIALE;
    const fiSD = this.formInputs.statoDebitorioConfig;

    // Definisco l'oggetto per gestire la checkbox
    const sdISCheckbox: IRiscaCheckboxData = {
      id: this.GA_C.STATO_DEBITORIO_INVIO_SPECIALE,
      label: this.GA_C.LABEL_STATO_DEBITORIO_INVIO_SPECIALE,
      value: sdInvioSpeciale,
      check: sdInvioSpeciale,
    };

    // Aggiorno il valore per il form
    this._riscaUtilities.setFormValue(f, k, sdISCheckbox);
    // Aggiorno il source della configurazione dell'input della checkbox
    this._riscaUtilities.updateRFICheckboxSource(fiSD, sdISCheckbox);
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.AEA_GADisabled || this.isPraticaLocked;
  }

  /**
   * Getter di comodo che verifica se la pratica è lockata.
   * @returns boolean con il risultato del check.
   */
  get isPraticaLocked(): boolean {
    // Verifico che esista nel servizio la configurazione del lock pratica
    const existLockStatus = this._riscaLockP.isPraticaInLockStatus();
    // Verifico se un altro utente sta lockando la pratica
    const anotherUserLocker = this._riscaLockP.isAnotherUserLockingPratica();
    // Ritorno le condizioni di disabilitazione
    return existLockStatus && anotherUserLocker;
  }
}
