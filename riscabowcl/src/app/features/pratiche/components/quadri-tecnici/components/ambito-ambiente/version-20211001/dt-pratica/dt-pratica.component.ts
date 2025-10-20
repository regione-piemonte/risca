import {
  Component,
  Inject,
  Input,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { clone } from 'lodash';
import { forkJoin, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import {
  unitaMisuraQuantitaValidator,
  valoreDataEmasIsoRiduzioneR5,
  valoreQuantitaFaldaEAumenti,
  valoreQuantitaFaldaValidator,
} from 'src/app/shared/miscellaneous/forms-validators/dati-tecnici/form-validators.dta';
import { PraticaDTVo } from '../../../../../../../../core/commons/vo/pratica-vo';
import { RiduzioneAumentoVo } from '../../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { IUnitaMisuraVo } from '../../../../../../../../core/commons/vo/unita-misura-vo';
import { IUsoLeggeSpecificoVo } from '../../../../../../../../core/commons/vo/uso-legge-specifico-vo';
import { IUsoLeggeVo } from '../../../../../../../../core/commons/vo/uso-legge-vo';
import { LoggerService } from '../../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../../../../../core/services/user.service';
import {
  IUsiLeggePraticaTableConfigs,
  UsiLeggePraticaTable,
} from '../../../../../../../../shared/classes/risca-table/ambiti/ambito-ambiente/version-20211001/dati-tecnici-ambiente-pratica/usi-legge-pratica.table';
import { RiscaTableDataConfig } from '../../../../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../../../../../shared/consts/common-consts.consts';
import {
  arrayContainsAtLeastValidator,
  minOrNullValidator,
  numberCompositionValidator,
} from '../../../../../../../../shared/miscellaneous/forms-validators/forms-validators';
import { RiscaFormBuilderService } from '../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaModalService } from '../../../../../../../../shared/services/risca/risca-modal.service';
import { RiscaStorageService } from '../../../../../../../../shared/services/risca/risca-storage.service';
import { RiscaUtilitiesService } from '../../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  FormUpdatePropagation,
  IRiscaCheckboxData,
  RiscaRegExp,
  RiscaServerError,
} from '../../../../../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../../../../../shared/utilities/classes/errors-maps';
import { DatiTecniciAmbienteConsts } from '../../../../../../consts/quadri-tecnici/dati-tecnici-ambiente/dati-tecnici-ambiente.consts';
import { PraticheService } from '../../../../../../service/pratiche.service';
import { AutoRiduzioniAumentiDTAmbienteService } from '../../../../services/ambito-ambiente/version-20211001/auto-riduzioni-aumenti-dta.service';
import { DatiTecniciAmbienteConverterService } from '../../../../services/ambito-ambiente/version-20211001/dati-tecnici-ambiente-converter.service';
import { DatiTecniciAmbienteService } from '../../../../services/ambito-ambiente/version-20211001/dati-tecnici-ambiente.service';
import { DatiTecniciService } from '../../../../services/dati-tecnici/dati-tecnici.service';
import { QuadriTecniciService } from '../../../../services/quadri-tecnici/pratica/quadri-tecnici.service';
import {
  DTPraticaConfig,
  DT_PRATICA_CONFIG,
} from '../../../../utilities/configs/dt-pratica.injectiontoken';
import {
  IDatiTecniciAmbiente,
  IDatiUsoLeggeReq,
  IDatiUsoLeggeRes,
  UsoLeggePSDAmbienteInfo,
} from '../../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { DatiTecniciPraticaComponent } from '../../../core/dati-tecnici-pratica/dati-tecnici-pratica.component';
import { DTCanonePratica20211001Component } from '../dt-canone-pratica/dt-canone-pratica.component';
import { DTPratica20211001FieldsConfigClass } from './utilities/dt-pratica.fields-configs';

/**
 * Componente per la gestione del form dati tecnici, ambito: ambiente.
 * Il form principale non ha un submit diretto.
 * Questo deve essere gestito dal padre.
 * Le informazioni del form verranno emesse registrandosi all'event emitter onFormChange.
 */
@Component({
  selector: 'dt-pratica',
  templateUrl: './dt-pratica.component.html',
  styleUrls: ['./dt-pratica.component.scss'],
  providers: [AutoRiduzioniAumentiDTAmbienteService],
})
export class DTPratica20211001Component
  extends DatiTecniciPraticaComponent<PraticaDTVo>
  implements OnInit, OnDestroy {
  /** Oggetto contenente le costanti utilizzate comuni. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_C = new DatiTecniciAmbienteConsts();
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto RiscaRegExp utilizzate come supporto per la validazione dei campi. */
  private riscaRegExp: RiscaRegExp = new RiscaRegExp();

  /** Input per i dati in caso di modifica */
  @Input('datiTecniciAmbiente') praticaDT: PraticaDTVo;

  /** Template reference al componente del calcolo canone. */
  @ViewChild('canoneRef') canoneRef: DTCanonePratica20211001Component;

  /** FormGroup che definisce la struttura della sotto form "usi". */
  usiForm: FormGroup;
  /** Boolean che tiene traccia se il form è stato submittato per: usiForm. */
  usiFormSubmitted = false;
  /** Boolean che definisce lo stato dell'accordion per gli usi di legge. */
  usiApriChiudi = false;
  /** Boolean che non permette disattivare il calcolo canone più volte per successive modifiche dei dati. */
  private blockModifyCC = false;

  /** DatiTecniciAmbiente che conterrà la configurazione dei dati generati dall'input. */
  datiTecniciAmbiente: IDatiTecniciAmbiente;
  /** DatiTecniciAmbiente che conterrà la configurazione dei dati generati dall'input, al suo stato iniziale. */
  datiTecniciAmbienteInitial: IDatiTecniciAmbiente;

  /** Array di oggetti UsoLeggeVo recuperata dal server. */
  listaUsiDiLegge: IUsoLeggeVo[] = [];
  /** Array di oggetti UsoLeggeSpecificoVo recuperata dal server tramite informazioni dentro oggetto UsoLeggeVo. */
  listaUsiDiLeggeSpecifici: IUsoLeggeSpecificoVo[] = [];
  /** Array di oggetti RiduzioneAumentoVo recuperata dal server tramite informazioni dentro oggetto UsoLeggeVo. */
  listaPercRiduzione: RiduzioneAumentoVo[] = [];
  /** Array di oggetti RiduzioneAumentoVo recuperata dal server tramite informazioni dentro oggetto UsoLeggeVo. */
  listaPercAumento: RiduzioneAumentoVo[] = [];

  /** Classe DTPratica20211001FieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: DTPratica20211001FieldsConfigClass;
  /** Oggetto UsiDiLeggeTable che contiene le informazioni della tabella usi di legge. */
  usiTable: UsiLeggePraticaTable;

  /**
   * Costruttore.
   */
  constructor(
    @Inject(DT_PRATICA_CONFIG) injConfig: DTPraticaConfig,
    private _autoAR: AutoRiduzioniAumentiDTAmbienteService,
    private _formBuilder: FormBuilder,
    private _datiTecniciAmbiente: DatiTecniciAmbienteService,
    private _datiTecnici: DatiTecniciService,
    private _dtaConverter: DatiTecniciAmbienteConverterService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    pratiche: PraticheService,
    quadriTecnici: QuadriTecniciService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaModal: RiscaModalService,
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
    // Caricamento dei dati per la lista degli usi di legge
    this.getUsiDiLegge();
  }

  ngOnInit() {
    // Funzione di init per le tabelle del componente
    this.initTabelle();
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
    this.formErrors = [
      ...this.EM.MAP_FORM_GROUP_REQUIRED,
      ...this.EM.MAP_FORMATO_NUMERO_6_INT_4_DEC,
      ...this.EM.MAP_ALMENO_UN_ELEMENTO,
    ];
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Essendo le configurazioni parecchie, si utilizza una classe di supporto
    this.formInputs = new DTPratica20211001FieldsConfigClass(
      this._riscaFormBuilder,
      this._riscaUtilities
    );
  }

  /**
   * Funzione che inizializza le tabelle del componente.
   */
  private initTabelle() {
    // Variabili di comodo
    const disableUserInputs = this.disableUserInputs;
    // Definisco un oggetto di configurazione per la tabella
    const c: IUsiLeggePraticaTableConfigs = { disableUserInputs };

    // Setup della tabella usiDiLeggeTab
    this.usiTable = new UsiLeggePraticaTable(c);
  }

  /**
   * Funzione che prepara possibili dati in input per la gestione di dati utilizzabili dal componente.
   */
  private initDatiTecniciAmbiente() {
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
    this.initFormDatiTecniciAmbiente();
    // Setup della struttura della sotto form usi #####
    this.initFormUsi();
    // Setup dei listiner dei form per la manipolazione dei dati
    this.initFormListener();

    // Setup di possibili configurazioni in input con valorizzazione dati componente
    this.initComponenteConfigs(this.datiTecniciAmbiente);
    // NOTA BENE: DA RICHIAMARE COME ULTIMISSIMA CHIAMATA A SEGUITO DI TUTTI I SETUP SUL FORM PER EVITARE CICLI DI CODICE SUPERFLUI
    this.setupDataCheckpoint();
  }

  /**
   * Funzione che definisce le logiche di init per le configurazioni del componente.
   */
  private initConfigs() {
    // Resetto il flag di blocco
    this.blockModifyCC = false;

    // Verifico che esista la configurazione
    if (!this.praticaDT) {
      // Nessuna configurazione
      return;
    }

    // Variabile di comodo
    const c: PraticaDTVo = this.praticaDT;
    // Effettuo la conversione dell'oggetto
    this._dtaConverter.convertPraticaDTVoToDatiTecniciAmbiente(c).subscribe({
      next: (dta: IDatiTecniciAmbiente) => {
        // Assegno l'oggetto convertito
        this.datiTecniciAmbiente = dta;
        // Creo una copia per l'oggetto originale
        this.datiTecniciAmbienteInitial = clone(dta);
        // Setup di possibili configurazioni in input con valorizzazione dati componente
        this.initComponenteConfigs(this.datiTecniciAmbiente);
      },
      error: (e: RiscaServerError) => {
        // Segnalo l'errore tramite servizio
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
        { value: '', disabled: false },
        { validators: [Validators.required] }
      ),
      comune: new FormControl(
        { value: '', disabled: false },
        { validators: [Validators.required] }
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
    });

    // Verifico il flag di disabilitazione della pagina
    if (this.disableUserInputs) {
      // Disabilito la form
      this.mainForm.disable();
    }
  }

  /**
   * Funzione specifica adibita al setup del form usiForm.
   */
  private initFormUsi() {
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
      },
      {
        validators: [
          unitaMisuraQuantitaValidator(
            this.DTA_C.QUANTITA,
            this.DTA_C.UNITA_DI_MISURA
          ),
          valoreQuantitaFaldaValidator(
            this.DTA_C.QUANTITA,
            this.DTA_C.QUANTITA_FALDA_PROFONDA
          ),
          valoreDataEmasIsoRiduzioneR5(
            this.DTA_C.DATA_SCADENZA_EMAS_ISO,
            this.DTA_C.PERC_DI_RIDUZIONE_MOTIVAZIONE
          ),
          valoreQuantitaFaldaEAumenti(
            this.DTA_C.QUANTITA_FALDA_PROFONDA,
            this.DTA_C.PERC_DI_AUMENTO_MOTIVAZIONE
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
   * Popola i campi della form in base all'oggetto da modificare che é stato passato.
   */
  private initComponenteConfigs(dta: IDatiTecniciAmbiente) {
    // verifico esistano configurazioni
    if (!dta) {
      // Niente configurazioni
      return;
    }

    // In questa parte si controllano tutte le proprietà dell'oggetto dta e prevalorizzare
    // i relativi campi di mainForm

    // Esistono 2 passaggi:
    //- questa funzione va ad assegnare i valori del form che non dipendono da liste
    // tutto ciò che non è dipendente dalle liste scaricate lo metto qui
    //- tutto ciò che è dipendente dalle liste lo devo andare a gestire all'interno dell'onScarico()

    if (dta.comune != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_C.COMUNE,
        dta.comune,
        { emitEvent: false }
      );
    }

    if (dta.corpoIdricoCaptazione != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_C.CORPO_IDRICO_CAPTAZIONE,
        dta.corpoIdricoCaptazione,
        { emitEvent: false }
      );
    }

    if (dta.gestioneManuale != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_C.GESTIONE_MANUALE,
        dta.gestioneManuale,
        { emitEvent: false }
      );
      // Aggiorno il source della configurazione dell'input della checkbox
      this._riscaUtilities.updateRFICheckboxSource(
        this.formInputs.gestioneManualeConfig,
        dta.gestioneManuale
      );
    }

    if (dta.nomeImpiantoIdroElettrico != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_C.NOME_IMPIANTO_IDROELETTRICO,
        dta.nomeImpiantoIdroElettrico,
        { emitEvent: false }
      );
    }

    if (dta.portataDaAssegnare != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTA_C.PORTATA_DA_ASSEGNARE,
        dta.portataDaAssegnare,
        { emitEvent: false }
      );
    }

    // Popolo le liste degli usi di legge
    if (dta.usiDiLegge != undefined) {
      // Resetto gli usi di legge
      this.usiTable.source = [];
      // Reinserisco gli usi di legge
      this.usiTable.addElements(dta.usiDiLegge).subscribe({
        next: (insert: boolean) => {
          // Se l'add di tutti gli elementi è andato a buon fine, aggiorno tutte le strutture correlate
          if (insert) {
            // Aggiorno il form con i dati della tabella
            this.aggiornaUsiLeggeInForm();
            // Aggiorno la struttura della listaUsiDiLegge disabilitando le voci prensenti negli usi di legge
            this.disabilitaUsiDiLeggeInseriti();
          }
        },
        error: (e: any) => {
          // Log di errore
          this._logger.error('initComponenteConfigs', e);
        },
      });
    }
  }

  /**
   * Funzione specifica adibita al setup dei listner delle form.
   * La funzione registrerà diversi eventi sulle form, atte a gestire la logica e la manipolazione dei dati.
   */
  private initFormListener() {
    // Definisco un listener sull'intero main form
    this.mainForm.valueChanges.subscribe((form: IDatiTecniciAmbiente) => {
      // Lancio la funzione di reset calcolo canone
      this.onDTChanges({ form });
    });

    // Listener al cambio di un uso di legge, verranno popolate le informazioni dei campi connessi
    this.usiForm
      .get(this.DTA_C.USO_DI_LEGGE)
      .valueChanges.subscribe((usoDiLeggeUpd: IUsoLeggeVo) => {
        // Lancio la funzione di gestione dei dati tecnici a seguito della selezione di un uso di legge
        this.onUsoLeggeSelected(usoDiLeggeUpd);
      });

    // Listener al cambio della quantità, verranno calcolati i relativi valori
    this.usiForm.get(this.DTA_C.QUANTITA).valueChanges.subscribe(() => {
      // Lancio la funzione di calcolo per le quantita per il pannello usi
      this._datiTecniciAmbiente.calcolaQuantitaPerUsi(this.usiForm);
    });

    // Listener al cambio della quantità, verranno calcolati i relativi valori
    this.usiForm
      .get(this.DTA_C.QUANTITA_FALDA_PROFONDA)
      .valueChanges.subscribe(() => {
        // Lancio la funzione di calcolo per le quantita per il pannello usi
        this._datiTecniciAmbiente.calcolaQuantitaPerUsi(this.usiForm);
      });

    // Listener al cambio del flag gestione manuale
    this.mainForm
      .get(this.DTA_C.GESTIONE_MANUALE)
      .valueChanges.subscribe((gestioneManuale: IRiscaCheckboxData) => {
        // Manipolo, a seconda della gestione manuale, i dati degli usi inseriti
        this.aggiornaRiduzioniAumentiUsi(gestioneManuale);
        // Resetto la form degli usi
        this.resetUsiForm();
        // Lancio la funzione di reset calcolo canone
        this.onDTChanges({ gestioneManuale: true });
      });
  }

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
        // Segnalo l'errore tramite servizio
        this.onServiziError(e);
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
      this.usiForm.get(this.DTA_C.USI_DI_LEGGE_SPECIFICI).enable();
    } else {
      // Disabilito la multi select tramite chiave form control
      this.usiForm.get(this.DTA_C.USI_DI_LEGGE_SPECIFICI).disable();
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
      this.usiForm.get(this.DTA_C.PERC_DI_RIDUZIONE_MOTIVAZIONE).enable();
    } else {
      // Disabilito la multi select tramite chiave form control
      this.usiForm.get(this.DTA_C.PERC_DI_RIDUZIONE_MOTIVAZIONE).disable();
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
      this.usiForm.get(this.DTA_C.PERC_DI_AUMENTO_MOTIVAZIONE).enable();
    } else {
      // Disabilito la multi select tramite chiave form control
      this.usiForm.get(this.DTA_C.PERC_DI_AUMENTO_MOTIVAZIONE).disable();
    }
  }

  /**
   * ###################################
   * FUNZIONI AGGIORNAMENTO INFORMAZIONI
   * ###################################
   */

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
   * Funzione invocata quando l'evento della pratica salvata viene attivato.
   * @param v any con i dati di payload dell'evento.
   * @override
   */
  praticaSaved(v?: any) {
    // Modifico il flag di gestione blocco del calcolo canone
    this.blockModifyCC = false;
  }

  /**
   * #############################
   * FUNZIONI DI GESTIONE DEL FORM
   * #############################
   */

  /**
   * Funzione che gestisce l'evento di cambio dati della scheda dati tecnici.
   * @param c Oggetto { form?: DatiTecniciAmbiente; uso?: UsoDiLeggeInfo; gestioneManuale?: boolean; } con i dati per la gestione del blocco del calcolo canone.
   */
  private onDTChanges(c: {
    form?: IDatiTecniciAmbiente;
    uso?: UsoLeggePSDAmbienteInfo;
    gestioneManuale?: boolean;
  }) {
    // Definisco una funzione di comodo
    const blockModifyCC = () => {
      // Blocco le logiche di controllo
      this.blockModifyCC = true;
      // Vado a resettare e bloccare il calcolo canone
      this._quadriTecnici.resetEDisabilitaCanone();
    };

    // Verifico se sono in modifica
    if (this.modifica) {
      // Verifico quale tipologia del dato
      if (c.form != null) {
        // Recupero i dati per la compare
        const F1 = c.form;
        const F2 = this.datiTecniciAmbienteInitial;
        // Effettuo la compare dati
        const sF = this._datiTecniciAmbiente.compareDatiTecniciAmbiente(F1, F2);
        // Verifico le condizioni per il reset
        if (!sF && !this.blockModifyCC) {
          // Gestisco il blocco per il calcolo canone
          blockModifyCC();
        }
      }
      if (c.uso != null && !this.blockModifyCC) {
        // Gestisco il blocco per il calcolo canone
        blockModifyCC();
      }
      // Variabili di comodo
      const gm = c.gestioneManuale != null;
      if (gm && !this.blockModifyCC) {
        // Gestisco il blocco per il calcolo canone
        blockModifyCC();
      }
    }
  }

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
    let dta: IDatiTecniciAmbiente = this.mainForm.getRawValue();

    // Converto l'oggetto dta in un oggetto PraticaDTVo
    const pdtVo = this._dtaConverter.convertDatiTecniciAmbienteToPraticaDTVo(
      this.praticaDT,
      dta
    );

    // Ritorno il dato aggiornato
    return pdtVo;
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
    // Resetto la form di inserimento degli usi di legge
    this.resetUsiForm();
  }

  /**
   * Funzione di comodo che aggiorna i dati del FormGroup inserendo le informazioni degli usi di legge.
   */
  private aggiornaUsiLeggeInForm() {
    // Recupero i dati della tabella usi
    let usiLegge: UsoLeggePSDAmbienteInfo[];
    usiLegge = this.usiTable.getDataSource();
    // Definisco la chiave del form per il campo
    const formKey = this.DTA_C.USI_DI_LEGGE;

    // Aggiungo i dati della tabella all'interno del form per gestire la validazione
    this._riscaUtilities.setFormValue(this.mainForm, formKey, usiLegge);
  }

  /**
   * Funzione che aggiunge i dati Uso all'interno della tabella usiDiLeggeTab.
   * @param usoDiLegge UsoDiLeggeInfo contenente i dati della form usiForm.
   */
  private aggiungiUso(usoDiLegge: UsoLeggePSDAmbienteInfo) {
    // Aggiungo alla tabella un nuovo elemento
    this.usiTable.addElement(usoDiLegge);
    // Gestisco il calcolo canone
    this.onDTChanges({ uso: usoDiLegge });
    // Aggiorno il form con i dati della tabella
    this.aggiornaUsiLeggeInForm();

    // Aggiorno la struttura della listaUsiDiLegge disabilitando le voci prensenti negli usi di legge
    this.disabilitaUsiDiLeggeInseriti();
  }

  /**
   * Funzione che modifica i dati Uso all'interno della tabella usiDiLeggeTab.
   * @param row RiscaTableDataConfig<UsoLeggePSDAmbienteInfo> con le informazioni della riga d'aggiornare.
   * @param datiUso UsoLeggePSDAmbienteInfo contenente i dati per l'aggiornamento della riga.
   */
  private modificaUsoEsistente(
    row: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>,
    datiUso: UsoLeggePSDAmbienteInfo
  ) {
    // Modifico all'interno della tabella il dato per l'uso
    this.usiTable.convertAndUpdateElement(datiUso, row);
    // Gestisco il calcolo canone
    this.onDTChanges({ uso: datiUso });
    // Aggiorno il form con i dati della tabella
    this.aggiornaUsiLeggeInForm();

    // Aggiorno la struttura della listaUsiDiLegge disabilitando le voci prensenti negli usi di legge
    this.disabilitaUsiDiLeggeInseriti();
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

    
    // TODO: @Ismaele => Per @Emamnuel, non bisogna modificare la gestione del "disabled" da qua dentro, ma bisogna farlo sul NgForm.
    // 1) Recuperi il form: this.usiForm
    // 2) Sul form devi lanciare la sua funzione interna "disable";
    //   2a) Una roba tipo: this.usiForm.get(this.DTA_C.USO_DI_LEGGE).disable()
    // 3) Importante: fare un oggetto di tipo: FormUpdatePropagation; e passarlo dentro .disable(stopPropagation)
    //   3a) Una roba tipo: stopPropagation: FormUpdatePropagation = { emitEvent: true };

    //oggetto che ferma la propagazione del disable per il campo uso di legge una volta terminata la modifica
    const fp: FormUpdatePropagation = { emitEvent: false };
    // Disabilito il campo uso di legge e passo un oggetto che ferma la propagazione del disable
    this.usiForm.get(this.DTA_C.USO_DI_LEGGE).disable(fp);

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
  }

  /**
   * Funzione che effettua il set di un uso di legge per la lista degli usi di legge.
   * @param usoLegge IUsoLeggeVo con l'uso di legge da impostare come selezionato.
   */
  private setUsoLeggeInForm(usoLegge: IUsoLeggeVo) {
    // Definisco le informazioni per il set dati
    const f: FormGroup = this.usiForm;
    const field = this.DTA_C.USO_DI_LEGGE;
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
    const field = this.DTA_C.USI_DI_LEGGE_SPECIFICI;
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
    const fcnUDM = this.DTA_C.UNITA_DI_MISURA;
    const fcnUDMD = this.DTA_C.UNITA_DI_MISURA_DESC;
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
    const fcn = this.DTA_C.QUANTITA;
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
    const fcn = this.DTA_C.QUANTITA_FALDA_PROFONDA;
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
    const field = this.DTA_C.PERC_DI_RIDUZIONE_MOTIVAZIONE;
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
    const field = this.DTA_C.PERC_DI_AUMENTO_MOTIVAZIONE;
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
    const fcn = this.DTA_C.DATA_SCADENZA_EMAS_ISO;
    const v = dataScadenzaEMASISO;

    // Aggiorno le informazioni per il form control del form group
    this._riscaUtilities.setFormValue(f, fcn, v);
  }

  /**
   * #################################
   * FUNZIONI PER GESTIONE ELIMINA USO
   * #################################
   */

  /**
   * Funzione che rimuove i dati dalla tabella usi.
   * @param row RiscaTableDataConfig<UsoDiLeggeInfo> contenente i dati della riga.
   */
  eliminaUso(row: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>) {
    // Definisco la funzione di callback nel caso in cui l'utente confermi la cancellazione della riga
    const onConfirm = () => {
      // Aggiungo alla tabella un nuovo elemento
      this.usiTable.removeElement(row);
      // Gestisco il calcolo canone
      this.onDTChanges({ uso: row.original });
      // Aggiorno il form con i dati della tabella
      this.aggiornaUsiLeggeInForm();
      // Aggiorno la struttura della listaUsiDiLegge disabilitando le voci prensenti negli usi di legge
      this.disabilitaUsiDiLeggeInseriti();
    };

    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConfermaCancellazione({ onConfirm });
  }

  /**
   * ###########################
   * FUNZIONI AL SUBMIT FORM USO
   * ###########################
   */

  /**
   * Funzione agganciata al bottone di Submit per la form usiForm.
   */
  usiSubmit() {
    // Il form è stato submittato
    this.usiFormSubmitted = true;
    // Verifico che la form sia valida
    if (this.usiForm.valid) {
      // Recupero le informazioni dal form
      let usoData: UsoLeggePSDAmbienteInfo;
      usoData = this.usiForm.getRawValue();

      // Lancio la funzione di gestione del dato del form
      this.gestisciUsoPerTabella(usoData);

      // Resetto la form
      this.resetUsiForm();
    }
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
        let usoCodForm: string = usoData?.usoDiLegge?.cod_tipo_uso;
        let usoCodTab: string = rowUsoTab?.original?.usoDiLegge?.cod_tipo_uso;
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
    this.usiForm.get(this.DTA_C.USI_DI_LEGGE_SPECIFICI).disable();
    this.usiForm.get(this.DTA_C.PERC_DI_RIDUZIONE_MOTIVAZIONE).disable();
    this.usiForm.get(this.DTA_C.PERC_DI_AUMENTO_MOTIVAZIONE).disable();

    //
    const fp: FormUpdatePropagation = { emitEvent: false };

    // Abilito il campo uso di legge e passo un oggetto che ferma la propagazione del disable
    this.usiForm.get(this.DTA_C.USO_DI_LEGGE).enable(fp);

    /**
     * il campo usoDiLegge è disabilitato in quanto non modificabile.
     */
    // this.disabledFieldUsoDiLegge = false;
    // TODO: @Ismaele => Per @Emamnuel, come hai visto per l'altro commento relativo alla modifica, una volta che viene resettato il form
    //                   bisogna andare ad abilitare nuovamente la select per l'uso di legge. Quindi se nella modifica tu forzi la disabilitazione, qua forzi l'abilitazione.
    // 1) Recuperi il form: this.usiForm
    // 2) Sul form devi lanciare la sua funzione interna "enable";
    //   2a) Una roba tipo: this.usiForm.get(this.DTA_C.USO_DI_LEGGE).enable()
    // 3) Importante: fare un oggetto di tipo: FormUpdatePropagation; e passarlo dentro .enable(stopPropagation)
    //   3a) Una roba tipo: stopPropagation: FormUpdatePropagation = { emitEvent: true };
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
    // Resetto il flag di blocco
    this.blockModifyCC = false;

    // Se mi trovo in inserimento, cancello tutto. Altrimenti torno alle informazioni originali.
    if (this.modalita == AppActions.modifica) {
      // Funzione che converte un oggetto PraticaDTVo in DatiTecniciAmbiente
      this.initDatiTecniciAmbiente();
      // Resetto il canone, ma abilito il pulsante
      this.canoneRef.clearAndEnableCanone();
      // #
    } else {
      // Resetto il flag di submit
      this.mainFormSubmitted = false;
      // Richiamo il reset sulla form dati tecnici ambiente
      this.mainForm.reset();

      // Resetto la form Usi
      this.resetUsiForm();
      // Resetto la tabella degli usi
      this.initTabelle();
      // Resetto il canone completamente
      this.canoneRef.clearAndDisableCanone();
    }
  }

  /**
   * ##############################
   * FUNZIONI UTILITIES PER SERVIZI
   * ##############################
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
   * ###############
   * GETTER E SETTER
   * ###############
   */

  get disableAggiungiUso() {
    // Ritorno le condizioni di disabilitazione del pulsante
    return this.disableUserInputs || !this.usiForm.valid;
  }

  get existTableUsi() {
    return this.usiTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che recupera lo stato della gestione manuale.
   * @returns boolean con il valore del dato.
   */
  get isGestioneManuale(): boolean {
    // Recupero l'oggetto della gestione manuale
    const f = this.mainForm;
    const GM = this.DTA_C.GESTIONE_MANUALE;
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
}
