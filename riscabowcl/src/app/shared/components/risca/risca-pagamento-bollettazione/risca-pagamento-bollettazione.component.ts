import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { compact, concat, uniq, uniqBy } from 'lodash';
import { PagamentoNonProprioVo } from 'src/app/core/commons/vo/pagamento-non-proprio-vo';
import { PagamentoVo } from 'src/app/core/commons/vo/pagamento-vo';
import {
  DettagliPagamentoSDTable,
  DettagliVersamentoTableConfigs,
} from 'src/app/shared/classes/risca-table/pagamenti-sd/dettagli-pagamento-sd.table';
import { DettaglioPagSearchResultVo } from '../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { ImmagineVo } from '../../../../core/commons/vo/immagine-vo';
import {
  RicercaStatiDebitoriPagamentoVo,
  TipoRicercaSDPagamento,
} from '../../../../core/commons/vo/ricerca-stati-debitori-pagamento-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { TipiImpNonProprioVo } from '../../../../core/commons/vo/tipo-importo-non-proprio-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import {
  SDSelezionatiTableConfigs,
  StatiDebitoriSelezionatiTable,
} from '../../../classes/risca-table/pagamenti-sd/stati-debitori-selezionati.table';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { minMaxNumberValidator } from '../../../miscellaneous/forms-validators/forms-validators';
import { importoNonProprioMagZero } from '../../../miscellaneous/forms-validators/pagamenti/form-validators.sdv';
import { IRiscaSDPerPagamentoModalConfig } from '../../../modals/risca-sd-nap-codice-utenza-modal/utilities/risca-sd-per-pagamento-modal.interfaces';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaPagamentoBollettazioneModalService } from '../../../services/risca/risca-pagamento-bollettazione-modal/risca-pagamento-bollettazione-modal.service';
import { IRiscaPBModalConfigs } from '../../../services/risca/risca-pagamento-bollettazione-modal/utilities/risca-pagamento-bollettazione-modal.interfaces';
import { RiscaPagamentoBollettazioneService } from '../../../services/risca/risca-pagamento-bollettazione/risca-pagamento-bollettazione.service';
import { RiscaResourcesService } from '../../../services/risca/risca-resources.service';
import { RiscaTableService } from '../../../services/risca/risca-table/risca-table.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  ICallbackDataModal,
  IRiscaCheckboxData,
  IRiscaRadioData,
  RiscaFormHooks,
} from '../../../utilities';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { RiscaServerError } from '../../../utilities/classes/utilities.classes';
import { RiscaNotifyCodes } from '../../../utilities/enums/risca-notify-codes.enums';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';
import { RiscaTableComponent } from '../risca-table/risca-table.component';
import { RiscaTableDataConfig } from '../risca-table/utilities/risca-table.classes';
import { RiscaPagamentoBollettazioneConsts } from './utilities/risca-pagamento-bollettazione.consts';
import { RicercaSDTitolare } from './utilities/risca-pagamento-bollettazione.enums';
import { RiscaPagamentoBollettazioneFieldsConfigClass } from './utilities/risca-pagamento-bollettazione.fields-configs';
import {
  IAutoImportoVersatoSD,
  IImportiMancantiEccedentiSDAsImportoPagamento,
  IPagamentoBollettazioneData,
  IPagamentoBollettazioneForm,
  IRicercaSDPagamentoForm,
  IVerificaImporti,
  IVerificaImportiResult,
  IVerificaImportiSuccess,
} from './utilities/risca-pagamento-bollettazione.interfaces';

@Component({
  selector: 'risca-pagamento-bollettazione',
  templateUrl: './risca-pagamento-bollettazione.component.html',
  styleUrls: ['./risca-pagamento-bollettazione.component.scss'],
})
export class RiscaPagamentoBollettazioneComponent
  extends RiscaFormChildComponent<IPagamentoBollettazioneData>
  implements OnInit, OnChanges, OnDestroy
{
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente una serie di costanti per la gestione degli errori. */
  EM = new RiscaErrorsMap();
  /** RiscaPagamentoBollettazioneConsts contenente una serie di costanti per la gestione del pagamento bollettazione. */
  RPB_C = RiscaPagamentoBollettazioneConsts;
  /** TipiImpNonPropriVo con le informazioni costanti riguardanti i tipi non propri. */
  TINP_C = new TipiImpNonProprioVo();
  /** RiscaFormHooks per la gestione dell'enumeratore nella componente html. */
  riscaFormHooks = RiscaFormHooks;

  /** PagamentoVo da gestire per inserimento/modifica. */
  @Input() pagamento: PagamentoVo;
  /** Input DettaglioPagSearchResultVo[] cone le informazioni di dettaglio del versamento. */
  @Input() dettagliPagamento: DettaglioPagSearchResultVo[] = [];
  /** Input boolean che definisce il flag di gestione per i dettagli pagamento. */
  @Input() pagamentiDaVisionare: boolean = false;
  /** Input boolean che definisce il flag di gestione per i dettagli pagamento. */
  @Input() ricercaAttiva: boolean = true;

  /** Output come event emitter che definisce l'avvenuta verifica degli importi. */
  @Output('onVerificaImporti') onVerificaImportiSuccess$ =
    new EventEmitter<IVerificaImportiSuccess>();
  /** Output come event emitter che definisce l'avvenuta verifica degli importi, ma con un errore. */
  @Output('onVerificaImportiError') onVerificaImportiError$ =
    new EventEmitter<RiscaNotifyCodes>();

  /** ViewChild con riferimento al blocco HTML per l'immagine del bollettino. */
  @ViewChild('bollettinoImg') bollettinoImg: ElementRef;
  /** ViewChild che definisce la connessione al componente: RiscaRicercaBollettiniComponent. */
  @ViewChild('sdSelezionatoTableRef')
  sdSelezionatoTableRef: RiscaTableComponent<any>;

  /** ImmagineVo con i dati dell'immagine scaricata per il bollettino. */
  imgBollettino: ImmagineVo;

  /** Classe RimborsoFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: RiscaPagamentoBollettazioneFieldsConfigClass;

  /** StatiDebitoriSelezionatiTable con la tabella degli stati debitori selezionati per la gestione del pagamento da visionare. */
  statiDebitoriSelezionatiTable: StatiDebitoriSelezionatiTable;

  /** DettagliPagamentoSDTable che definisce la tabella di gestione dati per i dati dei versamenti. */
  dettagliPagamentoTable: DettagliPagamentoSDTable;
  /** DettaglioPagSearchResultVo[] con gli oggetti rimossi dalla tabella per i dettagli pagamenti. */
  private dettPagToRemove: DettaglioPagSearchResultVo[] = [];

  /**
   * Costruttore.
   */
  constructor(
    public activeModal: NgbActiveModal,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    private _riscaResources: RiscaResourcesService,
    riscaUtilities: RiscaUtilitiesService,
    private _riscaPB: RiscaPagamentoBollettazioneService,
    private _riscaPBModal: RiscaPagamentoBollettazioneModalService,
    private _riscaTable: RiscaTableService
  ) {
    // Richiamo il super
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);
    // Funzione di setup generico
    this.setupComponente();
  }

  /**
   * ngOnInit.
   */
  ngOnInit(): void {
    // Lancio lo scarico dell'immagine se il pagamento ha un'immagine collegata
    this.initImmagineBollettino();
    // Lancio il setup delle form
    this.initForms();
  }

  /**
   * ngOnChanges.
   */
  ngOnChanges(changes: SimpleChanges): void {
    // Variabili di comodo
    const changesP = changes?.pagamento;
    // Verifico se sono state apportate modifiche agli oggetti in input
    const moddedP = changesP && !changesP.firstChange;

    // Verifico se ci sono da aggiornare i dati per la pratica
    if (moddedP) {
      // Resetto il form dati per poter inizializzare di nuovo i campi
      this.mainForm.reset();
      this.mainFormSubmitted = false;
      // Lancio lo scarico dell'immagine se il pagamento ha un'immagine collegata
      this.initImmagineBollettino();
      // Init dei campi form in base alla parametrizzazione degli input
      this.initPagamentoFormFields();

      // Init della tabella dei dettagli pagamento
      this.initTabellaDettagliPagamento();
    }
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy(): void {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * ############################
   * FUNZIONI DI SETUP COMPONENTE
   * ############################
   */

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
      ...this.EM.MAP_SOGGETTO_PAGAMENTO,
    ];
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Genero il setup per i campi
    this.formInputs = new RiscaPagamentoBollettazioneFieldsConfigClass(
      this._riscaFormBuilder
    );
  }

  /**
   * ###########################
   * FUNZIONI DI INIT COMPONENTE
   * ###########################
   */

  /**
   * Funzione di init per caricare l'immagine del bollettino.
   */
  private initImmagineBollettino() {
    // Tento di recuperare l'immagine del bolletino del pagamento
    const pagamento: PagamentoVo = this.pagamento;
    // Recupero dal pagamento l'id dell'immagine
    const idImmagine: number = pagamento?.id_immagine;

    // Resetto i dati dell'immagine precedente, se esiste
    this.resetImageData();

    // Verifico se esiste l'id dell'immagine
    if (idImmagine == undefined) {
      // Non c'è un'immagine associata
      return;
    }

    // Richiamo la funzione di caricamento dell'immagine
    this._riscaResources.getImmagine(idImmagine).subscribe({
      next: (immagine: ImmagineVo) => {
        // Lancio il set per far visualizzare l'immagine
        this.generaImmagineBollettino(immagine);
        // #
      },
      error: (e: RiscaServerError) => {
        // Ninete da gestire per ora, l'immagine non viene caricata e basta
      },
    });
  }

  /**
   * Funzione che effettua l'init per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Init del form
    this.initPagamentoForm();
    // Lancio l'inizzazione dei listener
    this.initListeners();
    // Init dei campi form in base alla parametrizzazione degli input
    this.initPagamentoFormFields();
    // Init della tabella dei dettagli pagamento
    this.initTabellaDettagliPagamento();
    // Init dei radio button del componente
    this.initTargetRicercaStatiDebitori();
    this.initTargetRicercaTitolare();
  }

  /**
   * Setup del form principale.
   */
  private initPagamentoForm() {
    // Controllo se mi è stato passato un input
    if (this.modalita == AppActions.inserimento) {
      this.pagamento = new PagamentoVo();
    }
    // Creo la form
    this.mainForm = this._formBuilder.group(
      {
        // Dati principali
        soggettoVersamento: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required, Validators.maxLength(200)] }
        ),
        // Form di ricerca
        targetRicercaStatiDebitori: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        ricercaPuntuale: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        targetRicercaTitolare: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        ricercaTitolare: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        importoDa: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        importoA: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        // Importo non propri
        importoNonIdentificato: new FormControl(
          { value: null, disabled: false },
          { validators: [importoNonProprioMagZero()] }
        ),
        importoNonDiCompetenza: new FormControl(
          { value: null, disabled: false },
          { validators: [importoNonProprioMagZero()] }
        ),
        importoDaRimborsare: new FormControl(
          { value: null, disabled: false },
          { validators: [importoNonProprioMagZero()] }
        ),
        // Flag rimborsato
        rimborsato: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        // Importo da assegnare
        importoDaAssegnare: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        // Note finali
        note: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.maxLength(500)] }
        ),
      },
      {
        validators: [
          minMaxNumberValidator(
            this.RPB_C.IMPORTO_DA,
            this.RPB_C.IMPORTO_A,
            false
          ),
        ],
      }
    );
  }

  /**
   * Funzione di init per i listener del componente, per rimanere in ascolto degli eventi per il cambio pagina dei dati contabili.
   */
  private initListeners() {
    // // Listener alla modifica del campo
    // this.mainForm
    //   .get(this.RPB_C.RICERCA_PUNTUALE)
    //   .valueChanges.subscribe((ricercaPuntuale: string) => {
    //     // Richiamo il getter per forzare il refresh
    //     this.ricercaStatiDebitoriAttiva;
    //     // #
    //   });

    // // Listener alla modifica del campo
    // this.mainForm
    //   .get(this.RPB_C.RICERCA_TITOLARE)
    //   .valueChanges.subscribe((ricercaTitolare: string) => {
    //     // Richiamo il getter per forzare il refresh
    //     this.ricercaStatiDebitoriAttiva;
    //     // #
    //   });
  }

  /**
   * Setup dei campi della form
   */
  private initPagamentoFormFields() {
    // Controllo se siamo in inserimento
    if (this.inserimento || !this.pagamento) {
      return;
    }
    // Setto i campi della form
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.RPB_C.SOGGETTO_VERSAMENTO,
      this.pagamento.soggetto_versamento
    );
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.RPB_C.NOTE,
      this.pagamento.note
    );
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.RPB_C.IMPORTO_DA_ASSEGNARE,
      this.pagamento.imp_da_assegnare
    );

    // Rimborsato va gestito manualmente
    const checkRimborsato: IRiscaCheckboxData = {
      id: this.RPB_C.LABEL_RIMBORSATO,
      value: this.pagamento?.flg_rimborsato,
      label: this.RPB_C.LABEL_RIMBORSATO,
      disabled: false,
      check: this.pagamento?.flg_rimborsato,
    };
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.RPB_C.RIMBORSATO,
      checkRimborsato
    );
    // Aggiorno il source della configurazione dell'input della checkbox
    this._riscaUtilities.updateRFICheckboxSource(
      this.formInputs.rimborsatoConfigs,
      checkRimborsato
    );

    // Lancio il set delle informazioni per i pag non propri
    this.initPagNonPropri(this.pagamento);
  }

  /**
   * Funzione di init per le informazioni relative ai pagamenti non propri.
   * @param pagamento PagamentoVo con le informazioni del pagamento.
   */
  private initPagNonPropri(pagamento: PagamentoVo) {
    // Verifico l'input
    if (!pagamento) {
      // Niente configurazione
      return;
    }

    // Definisco le variabili per il set delle informazioni sugli importi non propri
    const f: FormGroup = this.mainForm;
    // Lista degli importi non propri dal pagamento
    const importiNP: PagamentoNonProprioVo[] = pagamento.pag_non_propri;
    // Importo non identificato
    const kINI: string = this.RPB_C.IMPORTO_NON_IDENTIFICATO;
    const codNI: string = this.TINP_C.codNonIdentificato;
    // Importo non di competenza
    const kINDC: string = this.RPB_C.IMPORTO_NON_DI_COMPETENZA;
    const codNDC: string = this.TINP_C.codNonDiCompetenza;
    // Importo da rimborsare
    const kIDR: string = this.RPB_C.IMPORTO_DA_RIMBORSARE;
    const codDR: string = this.TINP_C.codDaRimborsare;

    // Tento di recuperare gli oggetti degli importi
    let impNI: number;
    impNI = this._riscaPB.recuperaImportoNonProprio(importiNP, codNI);
    let impNDC: number;
    impNDC = this._riscaPB.recuperaImportoNonProprio(importiNP, codNDC);
    let impDR: number;
    impDR = this._riscaPB.recuperaImportoNonProprio(importiNP, codDR);

    // Setto le informazioni nel form dati
    this._riscaUtilities.setFormValue(f, kINI, impNI);
    this._riscaUtilities.setFormValue(f, kINDC, impNDC);
    this._riscaUtilities.setFormValue(f, kIDR, impDR);
  }

  /**
   * Funzione che popola i dati della tabella con i dettagli dei pagamenti
   */
  initTabellaDettagliPagamento() {
    // Recupero la lista dei dettagli pagamenti search
    let dettPagSearch: DettaglioPagSearchResultVo[];
    dettPagSearch = this.dettagliPagamento ?? [];

    // Recupero l'importo del pagamento
    let importoVersatoPagamento: number;
    importoVersatoPagamento = this.pagamento.importo_versato;

    // Definisco la configurazione per la tabella
    const c: DettagliVersamentoTableConfigs = {
      dettPagSearch,
      disabled: false,
      importoVersatoPagamento,
    };

    // Setto le informazioni per i dettagli versamento
    this.dettagliPagamentoTable = new DettagliPagamentoSDTable(
      c,
      this._riscaFormBuilder
    );
  }

  /**
   * Funzione che inizializza il campo target ricerca stati debitori.
   * Verranno effettuate le logiche di controllo e inizializzazione sul campo.
   */
  private initTargetRicercaStatiDebitori() {
    // Contenitore di supporto
    let radioSelected: IRiscaRadioData;

    // Definisco quello che dovrà essere il valore selezionato per default
    const findValue: TipoRicercaSDPagamento = TipoRicercaSDPagamento.nap;
    // Recupero dalla lista dei target il solo soggetto
    radioSelected =
      this.formInputs.targetRicercaStatiDebitoriConfig.source.find(
        (t: IRiscaRadioData) => {
          // Ritorno il check sul value
          return t.value === findValue;
        }
      );

    // Imposto il valore di default per il campo 'target'
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.RPB_C.TARGET_RICERCA_STATI_DEBITORI,
      radioSelected
    );
  }

  /**
   * Funzione che inizializza il campo target ricerca titolare.
   * Verranno effettuate le logiche di controllo e inizializzazione sul campo.
   */
  private initTargetRicercaTitolare() {
    // Contenitore di supporto
    let radioSelected: IRiscaRadioData;

    // Definisco quello che dovrà essere il valore selezionato per default
    const findValue: RicercaSDTitolare = RicercaSDTitolare.pratica;
    // Recupero dalla lista dei target il solo soggetto
    radioSelected = this.formInputs.targetRicercaTitolareConfig.source.find(
      (t: IRiscaRadioData) => {
        // Ritorno il check sul value
        return t.value === findValue;
      }
    );

    // Imposto il valore di default per il campo 'target'
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.RPB_C.TARGET_RICERCA_TITOLARE,
      radioSelected
    );
  }

  /**
   * ###################################
   * FUNZIONI DI GESTIONE PER L'IMMAGINE
   * ###################################
   */

  /**
   * Funzione che gestisce le logiche per far visualizzare l'immagine all'interno della pagina.
   * @param immagine ImmagineVo con i dati dell'immagine da visualizzare.
   */
  private generaImmagineBollettino(immagine: ImmagineVo) {
    // Verifico l'input
    if (!immagine) {
      // Niente immagine per il pagamento
      return;
    }

    // Assegno localmente la variabile
    this.imgBollettino = immagine;

    // Recupero l'url per l'immagine
    const url = immagine?.path_immagine;

    // Verifico se esiste la struttura del native element
    if (this.checkImgNativeElement) {
      // Tramite riferimento del template aggiorno l'src
      this.bollettinoImg.nativeElement.src = url;
    }
  }

  /**
   * Funzione di comodo che resetta le informazioni collegate all'immagine del bollettino.
   */
  private resetImageData() {
    // Resetto l'oggetto dell'immagine
    this.imgBollettino = undefined;
    // Verifico se esiste la struttura del native element
    if (this.checkImgNativeElement) {
      // Rimuovo dal riferimento il possibile src dell'immagine
      this.bollettinoImg.nativeElement.src = '';
    }
  }

  /**
   * #################################################
   * GESTIONE DELLE LOGICHE PER RICERCA STATI DEBITORI
   * #################################################
   */

  /**
   * Funzione agganciata al pulsante di ricerca per gli stati debitori.
   * La funzione cercherà le informazioni e aprirà una modale per selezionare gli stati debitori da collegare al pagamento.
   */
  avviaRicercaSD() {
    // Verifico per sicurezza che sia possibile fare la ricerca
    if (this.ricercaStatiDebitoriAttiva) {
      // Lancio il clear dei dati
      this.clearData();

      // Recupero dal form i dati di ricerca e li inserisco nell'oggetto dati
      const ricercaSD: IRicercaSDPagamentoForm = {
        targetRicercaStatiDebitori: this.targetRicercaStatiDebitori,
        ricercaPuntuale: this.ricercaPuntuale,
        targetRicercaTitolare: this.targetRicercaTitolare,
        ricercaTitolare: this.ricercaTitolare,
        importoDa: this.importoDa,
        importoA: this.importoA,
        // Definisco una lista per tutti gli id_stato_debitorio delle rate dalla tabella dei dettagli pagamento
        sdDaEscludere: this.idSDDaEscludere(),
      };

      // Lancio la gestione per lo scarico stati debitori tramite filtri di ricerca
      this.ricercaStatiDebitori(ricercaSD);
      // #
    }
  }

  /**
   * Funzione che estrae la lista degli "id_stato_debitorio" dai dati della tabella per i dettagli pagamento attuali e eventuali stati debitori già selezionati.
   * La lista verrà usata per filtrare i risultati di ricerca.
   * @returns number[] con gli id estratti.
   */
  idSDDaEscludere(): number[] {
    // Definisco una lista per tutti gli id_stato_debitorio delle rate dalla tabella dei dettagli pagamento
    let idStatiDebitori: number[] = [];

    // Verifico se esiste la tabella dati per i dettagli pagamento attualmente presenti
    if (this.dettagliPagamentoTable) {
      // Recupero la lista di oggetto dalla tabella
      let dpAttuali: DettaglioPagSearchResultVo[];
      dpAttuali = this.dettagliPagamentoTable.getDataSource();

      // Dalla lista di oggetti aggiorno la lista di id_stato_debitorio
      const idSDDPAttuali = dpAttuali.map((dpa: DettaglioPagSearchResultVo) => {
        // Recupero la proprietà id_stato_debitorio
        return dpa?.dettaglio_pag?.rata?.id_stato_debitorio;
      });

      // Concateno le informazioni
      idStatiDebitori = concat(idStatiDebitori, idSDDPAttuali);
      // #
    }
    // Verifico se esistono già stati debitori selezionati
    if (this.statiDebitoriSelezionatiTable) {
      // Recupero la lista di oggetto dalla tabella
      let sdSelezionati: StatoDebitorioVo[];
      sdSelezionati = this.statiDebitoriSelezionatiTable.getDataSource();

      // Dalla lista di oggetti aggiorno la lista di id_stato_debitorio
      const idSDSelezionati = sdSelezionati.map((sds: StatoDebitorioVo) => {
        // Recupero la proprietà id_stato_debitorio
        return sds?.id_stato_debitorio;
      });

      // Concateno le informazioni
      idStatiDebitori = concat(idStatiDebitori, idSDSelezionati);
    }

    // Rimuovo eventuali valori undefined
    idStatiDebitori = compact(idStatiDebitori);
    // Rimuovo possibili valori doppi
    idStatiDebitori = uniq(idStatiDebitori);

    // Ritorno la lista dati
    return idStatiDebitori;
  }

  /**
   * ##################################
   * FUNZIONI DI RICERCA STATI DEBITORI
   * ##################################
   */

  /**
   * Funzione richiamata per la ricerca degli stati debitori, dati i filtri di ricerca dalla form.
   * @param ricercaSD IRicercaSDPagamentoForm con i filtri di ricerca per gli stati debitori.
   */
  private ricercaStatiDebitori(ricercaSD: IRicercaSDPagamentoForm) {
    // Effettuo una conversione dei dati dalla form in un oggetto di ricerca specifico
    const ricerca = this.formRicercaSDToRicercaSDPagamento(ricercaSD);
    // Definisco le configurazioni per i dati della modale
    let dataModal: IRiscaSDPerPagamentoModalConfig;
    dataModal = { ricerca };

    // Definisco le callback per la modale
    const callbacks: ICallbackDataModal = {
      onConfirm: (sdSelezionati: StatoDebitorioVo[]) => {
        // Richiamo la funzione al ritorno dalla modale
        this.onRicercaSDxNAP(sdSelezionati);
      },
    };

    // Definisco la configurazione da inviare al servizio
    const config: IRiscaPBModalConfigs = { callbacks, dataModal };
    // Richiamo il servizio e apro la modale
    this._riscaPBModal.openRicercaSDPagamentoBollettazione(config);
  }

  /**
   * Funzione di conversione dei dati dall'oggetto del form generato in pagina, ad un oggetto di ricerca per il server.
   * @param ricercaSD IRicercaSDPagamentoForm con i filtri di ricerca per gli stati debitori.
   */
  private formRicercaSDToRicercaSDPagamento(
    ricercaSD: IRicercaSDPagamentoForm
  ) {
    // Estraggo dai dati del form le informazioni per la gestione dell'oggetto di ricerca
    const {
      targetRicercaStatiDebitori,
      ricercaPuntuale,
      targetRicercaTitolare,
      ricercaTitolare,
      importoDa,
      importoA,
      sdDaEscludere,
    } = ricercaSD;
    // Gestisco la selezione del target del titolare
    const flgPratica =
      targetRicercaTitolare?.value === RicercaSDTitolare.pratica;

    // Effettuo una conversione dei dati dalla form in un oggetto di ricerca specifico
    const ricerca = new RicercaStatiDebitoriPagamentoVo({
      titolare: ricercaTitolare,
      importo_da: importoDa,
      importo_a: importoA,
      sd_da_escludere: sdDaEscludere,
    });
    // Definisco altre informazioni non gestite dal costruttore
    ricerca.calcola_interessi = this.pagamento.data_op_val;
    ricerca.flgPratica = flgPratica;

    // Verifico qual è il target di ricerca per i dati puntuali degli stati debitori
    switch (targetRicercaStatiDebitori?.value) {
      case TipoRicercaSDPagamento.codiceUtenza:
        // Assegno la ricerca al codice utenza
        ricerca.cod_utenza = ricercaPuntuale;
        break;
      case TipoRicercaSDPagamento.nap:
        // Assegno la ricerca al codice utenza
        ricerca.nap = ricercaPuntuale;
        break;
      case TipoRicercaSDPagamento.numeroPratica:
        // Assegno la ricerca al codice utenza
        ricerca.num_pratica = ricercaPuntuale;
        break;
    }

    // Ritorno l'oggetto di ricerca generato
    return ricerca;
  }

  /**
   * Funzione invocata al ritorno dalla modale degli stati debitori.
   * @param sdSelezionati StatoDebitorioVo[] con le informazioni restituite dalla modale.
   */
  private onRicercaSDxNAP(sdSelezionati: StatoDebitorioVo[]) {
    // Richiamo la funzione comune di gestione
    this.onRicercaStatiDebitori(sdSelezionati);
  }

  /**
   * ##########################
   * STATI DEBITORI SELEZIONATI
   * ##########################
   */

  /**
   * Funzione comune per le logiche di ricerca degli stati debitori, che sia per NAP o codice utenza.
   * @param sdSelezionati StatoDebitorioVo[] con le informazioni restituite dalla modale.
   */
  private onRicercaStatiDebitori(sdSelezionati: StatoDebitorioVo[]) {
    // Verifico se sono stati selezionati stati debitori
    if (sdSelezionati?.length > 0) {
      // Recupero le possibili informazioni già definite in precedenza
      let sdGiaSelezionati: StatoDebitorioVo[];
      sdGiaSelezionati = this.statiDebitoriSelezionatiTab;
      // Creo un nuovo array unendo le informazioni esistenti con le nuove
      let allSD: StatoDebitorioVo[];
      allSD = [...sdGiaSelezionati, ...sdSelezionati];
      // Vado a rimuovere tutti i possibili duplicati
      let statiDebitori: StatoDebitorioVo[];
      statiDebitori = uniqBy(allSD, 'id_stato_debitorio');

      // Recupero l'importo del pagamento
      let importoVersatoPagamento: number;
      importoVersatoPagamento = this.pagamento.importo_versato;

      // Creo una configurazione per la creazione di una nuova tabella per gli stati debitori selezionati
      const config: SDSelezionatiTableConfigs = {
        statiDebitori,
        importoVersatoPagamento,
      };
      // Creo una nuova tabella
      this.statiDebitoriSelezionatiTable = new StatiDebitoriSelezionatiTable(
        config,
        this._riscaFormBuilder
      );
    }
  }

  /**
   * #####################
   * METODI DEL COMPONENTE
   * #####################
   */

  /**
   * Funzione lanciata all'evento di aggiunta di una attività rimborso
   */
  resetVersamento() {
    // Committo la form
    this.onFormReset();
    // #
  }

  /**
   * Funzione lanciata all'evento di aggiunta di una attività rimborso
   */
  submitVersamento() {
    // Committo la form
    this.onFormSubmit();
    // #
  }

  /**
   * ###########################
   * FUNZIONALITA' DELLE TABELLE
   * ###########################
   */

  /**
   * Funzione che rimuove uno stato debitorio selezionato dalla tabella.
   * @param row RiscaTableDataConfig<StatoDebitorioVo> con i dati della riga.
   */
  rimuoviStatoDebitorioSelezionato(
    row: RiscaTableDataConfig<StatoDebitorioVo>
  ) {
    // Rimuovo l'elemento dalla tabella
    this.statiDebitoriSelezionatiTable.removeElement(row);
  }

  /**
   * Funzione che rimuove il dettaglio di un pagamento dalla tabella.
   * @param row RiscaTableDataConfig<DettaglioPagSearchResultVo> con i dati della riga.
   */
  rimuoviDettaglioPagamento(
    row: RiscaTableDataConfig<DettaglioPagSearchResultVo>
  ) {
    // Aggiungo alla lista di supporto il dettaglio pagamento rimosso
    this.dettPagToRemove.push(row.original);
    // Rimuovo l'elemento dalla tabella
    this.dettagliPagamentoTable.removeElement(row);
  }

  /**
   * #########################
   * GESTIONE VERIFICA IMPORTI
   * #########################
   */

  /**
   * Funzione associata al pulsante "Verifica importi".
   * La funzione verificherà alcune condizioni sui dati e gestirà le combinazioni di gestione delle informazioni per ottenere, nel caso tutto fosse corretto, la condizione di somma tra gli importi.
   * @param salvaDopoVerifica boolean che identifica il flag che verrà emesso insieme alle informazioni del pagamento quando la verifica importi è andata a buon fine. RISCA-757: con questa modifica, il SALVA del componente può lanciare una verifica importi e se questa da esito positivo deve poi essere intercettata e lanciare subito il SALVA. Prima di questo, il salva era abilitato solo se il verifi importi era stato premuto manualmente dall'utente.
   */
  verificaImporti(salvaDopoVerifica: boolean = false) {
    // Lancio il clear dei dati per il componente padre
    this.clearData();
    // Resetto il campo per "importo d'assegnare"
    this.importoDaAssegnare = null;

    // Definisco tutte la variabili necessarie alla gestione delle verifiche
    let importoPagamento: number;
    let dettagliPagamento: DettaglioPagSearchResultVo[];
    let statiDebitoriSel: StatoDebitorioVo[];
    let importoNonIdentificato: number;
    let importoNonDiCompetenza: number;
    let importoDaRimborsare: number;

    // Assegno i valori alle variabili locali
    importoPagamento = (this.importo_versato as number) ?? 0;
    dettagliPagamento = this.dettagliPagamentoTab;
    statiDebitoriSel = this.statiDebitoriSelezionatiTab;
    importoNonIdentificato = this.importoNonIdentificato ?? 0;
    importoNonDiCompetenza = this.importoNonDiCompetenza ?? 0;
    importoDaRimborsare = this.importoDaRimborsare ?? 0;

    // Compongo l'oggetto per la verifica delle informazioni
    const data: IVerificaImporti = {
      // RISCA-ISSUES-50 - La gestione per il controllo da implementare necessita della distinzione tra pagamenti da visionare e dettagli pagamento
      pagamentoDaVisionare: this.pagamentiDaVisionare,
      importoPagamento,
      dettagliPagamento,
      statiDebitoriSel,
      importoNonIdentificato,
      importoNonDiCompetenza,
      importoDaRimborsare,
    };

    // Lancio la funzione di gestione degli automatismi dell'applicazione
    this.gestisciAutomatismi(data);
    // Lancio la funzione di gestione e verifica delle informazioni
    this.gestisciVerificaImporti(data, salvaDopoVerifica);
  }

  /**
   * Funzione specifica che gestisce gli automatismi per i dati degli importi del pagamento.
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   */
  private gestisciAutomatismi(data: IVerificaImporti) {
    // Verifico la casistica di automatizzazione dati per singolo stato debitorio
    let caseAutoSD: IAutoImportoVersatoSD;
    caseAutoSD = this._riscaPB.autoImportoStatoDebitorio(data);
    // Verifico se rientro nella casistica di automatismo che va a definire automaticamente gli importi per gli stati debitori
    if (caseAutoSD.verified) {
      // Casistica verificata, recupero lo stato debitorio
      const sd: StatoDebitorioVo = caseAutoSD.statoDebitorioUpd;
      // Aggiorno la tabella degli stati debitori
      this.statiDebitoriSelezionatiTable?.setNewElements([sd]);
      // Aggiorno la lista locale con le nuove informazioni definite
      data.statiDebitoriSel = this.statiDebitoriSelezionatiTab;
      // Casistica verificata, blocco il flusso
      return;
    }

    // Verifico la casistica di automatizzazione dati per gli importi mancanti pari al totale del pagamento
    let importiMancEccAsPagamento: IImportiMancantiEccedentiSDAsImportoPagamento;
    importiMancEccAsPagamento =
      this._riscaPB.importiMancantiUgualeImportoPagamento(data);
    // Verifico se rientro nella casistica di automatismo che va a definire automaticamente gli importi mancanti eccedenti degli stati debitori come importo versato degli stati debitori stessi
    if (importiMancEccAsPagamento.verified) {
      // Casistica verificata, recupero lo stato debitorio
      let sds: StatoDebitorioVo[];
      sds = importiMancEccAsPagamento.statiDebitoriUpd;
      // Aggiorno la tabella degli stati debitori
      this.statiDebitoriSelezionatiTable?.setNewElements(sds);
      // Aggiorno la lista locale con le nuove informazioni definite
      data.statiDebitoriSel = this.statiDebitoriSelezionatiTab;
      // Casistica verificata, blocco il flusso
      return;
    }
  }

  /**
   * Funzione che esegue i controlli veri e propri per la gestione della verifica degli importi.
   * La funzione prenderà in esame varie casistiche e produrrà un risultato a seconda delle informazioni impostate sulla maschera.
   * @param data IVerificaImporti come oggetto di configurazione contenente le informazioni da verificare.
   * @param salvaDopoVerifica boolean che identifica il flag che verrà emesso insieme alle informazioni del pagamento quando la verifica importi è andata a buon fine. RISCA-757: con questa modifica, il SALVA del componente può lanciare una verifica importi e se questa da esito positivo deve poi essere intercettata e lanciare subito il SALVA. Prima di questo, il salva era abilitato solo se il verifi importi era stato premuto manualmente dall'utente.
   */
  private gestisciVerificaImporti(
    data: IVerificaImporti,
    salvaDopoVerifica: boolean = false
  ) {
    // Richiamo la funzione di gestione degli importi
    const result: IVerificaImportiResult = this._riscaPB.verificaImporti(data);
    // Estraggo dal risultato le informazioni generate
    const { validated, notifica, residuo } = result;

    // Verifico se c'è stato un errore sulla verifica
    if (!validated) {
      // Si è verificato qualche errore
      this.onVerificaImportiError(notifica);
      // #
    } else {
      // Verifica andata a buon fine
      this.onVerificaImportiSuccess(notifica, residuo, salvaDopoVerifica);
      // #
    }
  }

  /**
   * Funzione che gestisce la casistica di errore a seguito della verifica importi.
   * @param notifica RiscaNotifyCodes con il codice di notifica per l'errore.
   */
  private onVerificaImportiError(notifica: RiscaNotifyCodes) {
    // Emetto l'evento di trigger delle righe
    const tableId = this.RPB_C.SD_SELEZIONATI_TABLE_ID;
    this._riscaTable.formSubmitsInputs(tableId);

    // Emetto l'evento di verifica importi con errore
    this.onVerificaImportiError$.emit(notifica);
  }

  /**
   * Funzione che gestisce la casistica di successo a seguito della verifica importi.
   * @param notifica RiscaNotifyCodes con il codice di notifica di successo.
   * @param residuo number con il valore residuo della verifica importi.
   * @param salvaDopoVerifica boolean che identifica il flag che verrà emesso insieme alle informazioni del pagamento quando la verifica importi è andata a buon fine. RISCA-757: con questa modifica, il SALVA del componente può lanciare una verifica importi e se questa da esito positivo deve poi essere intercettata e lanciare subito il SALVA. Prima di questo, il salva era abilitato solo se il verifi importi era stato premuto manualmente dall'utente.
   */
  private onVerificaImportiSuccess(
    notifica: RiscaNotifyCodes,
    residuo: number,
    salvaDopoVerifica: boolean = false
  ) {
    // Assegno a "importo d'assegnare" il valore residuo calcolato
    this.aggiornaImportoDaAssegnareConRedisuo(residuo);

    /**
     * NOTA DELLO SVILUPPATORE - RISCA-757:
     * A seguito della richiesta di implementare il SALVA + verifica, Angular esegue prima le funzioni di submit del form che dell'assegnazione
     * del valore dell'importo da assegnare. Questo significa che l'importo da assegnare risulta NULL quando viene effettuato il submit della form.
     * Per correggere questo comportamento, si mette un piccolo timeout per dare il tempo ad Angular di aggiornare il form con il valore dell'importo.
     */
    setTimeout(() => {
      // Emetto l'evento di verifica importi con errore
      this.onVerificaImportiSuccess$.emit({ notifica, salvaDopoVerifica });
      // Gestisco la disabilitazione delle parti della pagina interessate dal flusso di verifica
      this.disabilitazioneVerificaImporti();
      // #
    }, 100);
  }

  /**
   * Funzione di supporto che effettua le formattazioni di sicurezza sull'importo da assegnare e poi lo aggiunge come valore al form.
   * @param residuo number con l'importo da assegnare da visualizzare all'utente.
   */
  private aggiornaImportoDaAssegnareConRedisuo(residuo: number) {
    // Ritrasformo il residuo in un numero js
    let formatted: number;
    formatted = this._riscaUtilities.forzaFormattazioneImporto(residuo, 2);

    // Assegno il residuo formattato al form
    this.importoDaAssegnare = formatted;
  }

  /**
   * ########################################################
   * GESTIONE ABILITAZIONE DATI COLLEGATI AL VERIFICA IMPORTI
   * ########################################################
   */

  /**
   * Funzione che abilita tutte le strutture della pagina gestite dal flusso per "verrifica importi".
   */
  private abilitazioneVerificaImporti() {
    // // Sblocco tutte le input degli importi per stati debitori e importi "non propri"
    // this.importoDaRimborsareFC?.enable();
    // this.importoNonDiCompetenzaFC?.enable();
    // this.importoNonIdentificatoFC?.enable();
    // // Recupero l'id dell'istanza della tabella e richiedo l'abilitazione delle input
    // const tableId = this.RPB_C.SD_SELEZIONATI_TABLE_ID;
    // this._riscaTable.enableInputs(tableId);
    // // Sblocco i pulsanti di ricerca e di verifica importi
    // this.permettiCercaSD = true;
    // // Per la gestione della tabella dei dettagli pagamenti lancio una funzione specifica
    // this.abilitaDettagliPagamentiTable(true);
  }

  /**
   * Funzione che disabilita tutte le strutture della pagina gestite dal flusso per "verrifica importi".
   */
  private disabilitazioneVerificaImporti() {
    // Blocco tutte le input degli importi per stati debitori e importi "non propri"
    // ### RISCA-757 => è stato richiesto di rimuovere la disabilitazione al "verifica importi" per i campi "non propri"
    // this.importoDaRimborsareFC?.disable();
    // this.importoNonDiCompetenzaFC?.disable();
    // this.importoNonIdentificatoFC?.disable();
    // // Recupero l'id dell'istanza della tabella e richiedo la disabilitazione delle input
    // const tableId = this.RPB_C.SD_SELEZIONATI_TABLE_ID;
    // this._riscaTable.disableInputs(tableId);
    // // Blocco i pulsanti di ricerca e di verifica importi
    // this.permettiCercaSD = false;
    // // Per la gestione della tabella dei dettagli pagamenti lancio una funzione specifica
    // this.abilitaDettagliPagamentiTable(false);
  }

  /**
   * Funzione che gestisce l'abilitazione/disabilitazione della tabella dei dettagli pagamento.
   * @param abilita boolean con lo stato di abilitazione della tabella.
   */
  private abilitaDettagliPagamentiTable(abilita: boolean) {
    // Verifico se la tabella esiste
    if (this.checkDettagliPagamento) {
      // Recupero le informazioni dei dettagli pagamento
      let dettPag: DettaglioPagSearchResultVo[];
      dettPag = this.dettagliPagamentoTable.getDataSource();
      // Recupero l'importo del pagamento
      let importoVersatoPagamento: number;
      importoVersatoPagamento = this.pagamento.importo_versato;

      // Ricreo la tabella passando la configurazione per il check di abilitazione
      this.dettagliPagamentoTable = new DettagliPagamentoSDTable(
        {
          dettPagSearch: dettPag,
          disabled: !abilita,
          importoVersatoPagamento,
        },
        this._riscaFormBuilder
      );
    }
  }

  /**
   * ######################
   * FUNZIONALITA' DEL FORM
   * ######################
   */

  /**
   * Funzione di reset manuale richiamabile dal componente padre.
   * @override
   */
  onFormReset() {
    // Resetto la form
    this.mainForm.reset();
    // Resetto il flag del form submitted
    this.mainFormSubmitted = false;
    // Resetto la tabella degli stati debitori selezionati
    this.statiDebitoriSelezionatiTable?.clear();
    // Abilito tutte le parti della maschera gestite dal flusso "Verifica importi"
    this.abilitazioneVerificaImporti();
    // Lancio l'inizializzazione dei radio button
    this.initTargetRicercaStatiDebitori();
    this.initTargetRicercaTitolare();

    // Verifico se sono in modifica
    if (this.modifica) {
      // Resetto le informazioni per i dettagli pagamenti
      this.dettPagToRemove = [];
      this.dettagliPagamentoTable?.clear();

      // Lancio lo scarico dell'immagine se il pagamento ha un'immagine collegata
      this.initImmagineBollettino();
      // Init dei campi form in base alla parametrizzazione degli input
      this.initPagamentoFormFields();
      // Init della tabella dei dettagli pagamento
      this.initTabellaDettagliPagamento();
    }
  }

  /**
   * Funzione che combina ed impacchetta le informazioni della pagina come submit della form dati.
   * @returns IPagamentoBollettazioneData con l'oggetto generato dai dati della pagina.
   * @override
   */
  getMainFormRawValue(): IPagamentoBollettazioneData {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      // Manca l'istanza del form
      return undefined;
    }
    // Il main form esiste, ritorno i dati del form
    let pagamentoForm: IPagamentoBollettazioneForm;
    pagamentoForm = this.mainForm.getRawValue();

    // Prendo il pagamento
    const pagamento: PagamentoVo = this.pagamento;
    // Genero la lista dei pagamenti non propri
    let pagamentiNP: PagamentoNonProprioVo[] =
      this._riscaPB.gestisciPagamentiNonPropri(pagamentoForm, pagamento);

    // Aggiorno il valore per i pagamenti non propri nel pagamento
    pagamento.pag_non_propri = pagamentiNP;
    // Aggiorno flag rimborsato
    pagamento.flg_rimborsato = pagamentoForm.rimborsato.check;
    // Versante
    pagamento.soggetto_versamento = pagamentoForm.soggettoVersamento;
    // Note
    pagamento.note = pagamentoForm.note;
    // Importo da assegnare
    pagamento.imp_da_assegnare = pagamentoForm.importoDaAssegnare ?? 0;

    // Tento di recuperare la lista di stati debitori selezionati (potrebbe non esistere)
    let sdSelezionati: StatoDebitorioVo[];
    sdSelezionati = this.statiDebitoriSelezionatiTable?.getDataSource() ?? [];
    // Tento di recupera la lista dei dettagli pagamenti ancora attivi in tabella
    let dettPagAttivi: DettaglioPagSearchResultVo[];
    dettPagAttivi = this.dettagliPagamentoTable?.getDataSource();

    // Genero il dato di ritorno completo
    const resData: IPagamentoBollettazioneData = {
      pagamento: pagamento,
      dettagliPag: dettPagAttivi,
      dettagliPagCancellati: this.dettPagToRemove,
      statiDebitoriSelezionati: sdSelezionati,
    };

    // Ritorno l'oggetto aggiornato
    return resData;
  }

  /**
   * #################################
   * FUNZIONI DI GESTIONE DELLA MODALE
   * #################################
   */

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalDismiss() {
    // Dismiss della modale
    this.activeModal.dismiss();
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione collegata al click sull'immagine del bollettino.
   * La funzione aprirà l'immagine in una nuova scheda permettendo di vedere l'immagine con lo zoom.
   */
  onBollettinoImgClick() {
    // Recupero il link dell'immagine
    const srcBollettino =
      this.bollettinoImg?.nativeElement?.getAttribute('src');
    // Richiedo l'apertura dell'immagine in una nuova scheda
    var w = window.open(srcBollettino, '_blank');
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  get des_modalita_pag() {
    return this.pagamento?.tipo_modalita_pag?.des_modalita_pag ?? '';
  }

  get importo_versato() {
    return this.pagamento?.importo_versato ?? '';
  }

  get cro() {
    return this.pagamento?.cro ?? '';
  }

  get data_op_val() {
    return this._riscaUtilities.convertMomentToViewDate(
      this.pagamento?.data_op_val
    );
  }

  get quinto_campo() {
    return this.pagamento?.quinto_campo ?? '';
  }

  get causale() {
    return this.pagamento?.causale ?? '';
  }

  /**
   * Getter di comodo che verifica se esiste il native element per l'immgagine del bollettino.
   * @returns boolean con il risultato del check.
   */
  get checkImgNativeElement(): boolean {
    // Ritorno il controllo sul native element
    return this.bollettinoImg?.nativeElement != undefined;
  }

  /**
   * Getter di verifica per la tabella dei dettagli pagamento della tabella.
   * @returns boolean con il risultato del check.
   */
  get checkDettagliPagamento() {
    // Verifico che la tabella esista e abbia dati
    return this.dettagliPagamentoTable?.source?.length > 0;
  }

  /**
   * Getter di verifica per la tabella degli stati debitori selezionati.
   * @returns boolean con il risultato del check.
   */
  get checkStatiDebitoriSelezionati(): boolean {
    // Verifico che la tabella esista e abbia dati
    return this.statiDebitoriSelezionatiTable?.source?.length > 0;
  }

  /**
   * Getter per il recupero del dato del form
   * @returns IRiscaRadioData con le informazioni del campo.
   */
  get targetRicercaStatiDebitori(): IRiscaRadioData {
    // Informazioni per il recupero dati
    const f = this.mainForm;
    const k = this.RPB_C.TARGET_RICERCA_STATI_DEBITORI;
    // Recupero il dato del form
    return this._riscaUtilities.getFormValue(f, k);
  }

  /**
   * Getter per il recupero del dato del form
   * @returns string con le informazioni del campo.
   */
  get ricercaPuntuale(): string {
    // Informazioni per il recupero dati
    const f = this.mainForm;
    const k = this.RPB_C.RICERCA_PUNTUALE;
    // Recupero il dato del form
    return this._riscaUtilities.getFormValue(f, k);
  }

  /**
   * Getter per il recupero del dato del form
   * @returns IRiscaRadioData con le informazioni del campo.
   */
  get targetRicercaTitolare(): IRiscaRadioData {
    // Informazioni per il recupero dati
    const f = this.mainForm;
    const k = this.RPB_C.TARGET_RICERCA_TITOLARE;
    // Recupero il dato del form
    return this._riscaUtilities.getFormValue(f, k);
  }

  /**
   * Getter per il recupero del dato del form
   * @returns string con le informazioni del campo.
   */
  get ricercaTitolare(): string {
    // Informazioni per il recupero dati
    const f = this.mainForm;
    const k = this.RPB_C.RICERCA_TITOLARE;
    // Recupero il dato del form
    return this._riscaUtilities.getFormValue(f, k);
  }

  /**
   * Getter per il recupero del dato del form
   * @returns number con le informazioni del campo.
   */
  get importoDa(): number {
    // Informazioni per il recupero dati
    const f = this.mainForm;
    const k = this.RPB_C.IMPORTO_DA;
    // Recupero il dato del form
    return this._riscaUtilities.getFormValue(f, k);
  }

  /**
   * Getter per il recupero del dato del form.
   * @returns number con le informazioni del campo.
   */
  get importoA(): number {
    // Informazioni per il recupero dati
    const f = this.mainForm;
    const k = this.RPB_C.IMPORTO_A;
    // Recupero il dato del form
    return this._riscaUtilities.getFormValue(f, k);
  }

  /**
   * Getter che ritorna la possibilità di effettuare la ricerca stati debitori.
   * @returns boolean con il risultato del check.
   */
  get ricercaStatiDebitoriAttiva(): boolean {
    // Recupero i valori per il form di ricerca
    let targetRicercaStatiDebitori = this.targetRicercaStatiDebitori;
    let ricercaPuntuale = this.ricercaPuntuale;
    let targetRicercaTitolare = this.targetRicercaTitolare;
    let ricercaTitolare = this.ricercaTitolare;
    let importoDa = this.importoDa;
    let importoA = this.importoA;

    // Definisco i casi di gestione con la presenza dati
    // 1) Radio button nap, codice utenza, numero pratica e input collegata valorizzata
    let case1: boolean;
    case1 =
      targetRicercaStatiDebitori != undefined &&
      ricercaPuntuale != undefined &&
      ricercaPuntuale != '';
    // 2) Radio button pratica, stato debitorio e input collegata valorizzata
    let case2: boolean;
    case2 =
      targetRicercaTitolare != undefined &&
      ricercaTitolare != undefined &&
      ricercaTitolare != '';
    // 3) Tutte e due le input valorizzate e importoDa <= importoA oppure almeno una delle due input valorizzate
    let case3: boolean;
    const existImpDa: boolean = importoDa != undefined; // importoDa esiste
    const existImpA: boolean = importoA != undefined; // importoA esiste
    const checkImpDaA: boolean = importoDa <= importoA; // importoDa <= importoA
    const subCase1: boolean = existImpDa && existImpA && checkImpDaA; // Entrambe input valorizzate e importoDa <= importoA
    const subCase2: boolean = existImpDa && !existImpA; // importoDa valorizzato, importoA non valorizzato
    const subCase3: boolean = !existImpDa && existImpA; // importoDa non valorizzato, importoA valorizzato
    case3 = subCase1 || subCase2 || subCase3;

    // Si può effettuare la ricerca solo se almeno un campo è valorizzato
    return case1 || case2 || case3;
  }

  /**
   * Getter per il recupero del valore del campo: importoNonIdentificato.
   * @returns number con il valore del campo.
   */
  get importoNonIdentificato(): number {
    // Informazioni per il recupero dati
    const f = this.mainForm;
    const k = this.RPB_C.IMPORTO_NON_IDENTIFICATO;
    // Recupero il dato del form
    return this._riscaUtilities.getFormValue(f, k);
  }

  /**
   * Getter per il recupero del campo: importoNonDiCompetenza.
   * @returns AbstractControl con il valore del campo.
   */
  get importoNonDiCompetenzaFC(): AbstractControl {
    // Informazioni per il recupero dati
    const fg = this.mainForm;
    const fcn = this.RPB_C.IMPORTO_NON_DI_COMPETENZA;
    // Recupero il dato del form
    return fg.get(fcn);
  }

  /**
   * Getter per il recupero del campo: importoDaRimborsare.
   * @returns AbstractControl con il valore del campo.
   */
  get importoDaRimborsareFC(): AbstractControl {
    // Informazioni per il recupero dati
    const fg = this.mainForm;
    const fcn = this.RPB_C.IMPORTO_DA_RIMBORSARE;
    // Recupero il dato del form
    return fg.get(fcn);
  }

  /**
   * Getter per il recupero del campo: importoNonIdentificato.
   * @returns AbstractControl con il valore del campo.
   */
  get importoNonIdentificatoFC(): AbstractControl {
    // Informazioni per il recupero dati
    const fg = this.mainForm;
    const fcn = this.RPB_C.IMPORTO_NON_IDENTIFICATO;
    // Recupero il dato del form
    return fg.get(fcn);
  }

  /**
   * Getter per il recupero del valore del campo: importoNonDiCompetenza.
   * @returns number con il valore del campo.
   */
  get importoNonDiCompetenza(): number {
    // Informazioni per il recupero dati
    const f = this.mainForm;
    const k = this.RPB_C.IMPORTO_NON_DI_COMPETENZA;
    // Recupero il dato del form
    return this._riscaUtilities.getFormValue(f, k);
  }

  /**
   * Getter per il recupero del valore del campo: importoDaRimborsare.
   * @returns number con il valore del campo.
   */
  get importoDaRimborsare(): number {
    // Informazioni per il recupero dati
    const f = this.mainForm;
    const k = this.RPB_C.IMPORTO_DA_RIMBORSARE;
    // Recupero il dato del form
    return this._riscaUtilities.getFormValue(f, k);
  }

  /**
   * Setter per il campo del form: importoDaAssegnare.
   * @param v number con il valore da assegnare al campo.
   */
  set importoDaAssegnare(v: number) {
    // Informazioni per il recupero dati
    const f = this.mainForm;
    const k = this.RPB_C.IMPORTO_DA_ASSEGNARE;
    // Recupero il dato del form
    this._riscaUtilities.setFormValue(f, k, v);
  }

  /**
   * Getter che recupera i valori definiti per i dettagli pagamento dalla tabella.
   * @returns DettaglioPagSearchResultVo[] con la lista degli elementi nella tabella.
   */
  get dettagliPagamentoTab(): DettaglioPagSearchResultVo[] {
    // Recupero i dati della tabella
    let list: DettaglioPagSearchResultVo[];
    list = this.dettagliPagamentoTable?.getDataSource() ?? [];

    // Ritorno la lista di elementi dalla tabella
    return list;
  }

  /**
   * Getter che recupera i valori definiti per gli stati debitori selezionati dalla tabella.
   * @returns StatoDebitorioVo[] con la lista degli elementi nella tabella.
   */
  get statiDebitoriSelezionatiTab(): StatoDebitorioVo[] {
    // Recupero i dati della tabella
    let list: StatoDebitorioVo[];
    list = this.statiDebitoriSelezionatiTable?.getDataSource() ?? [];

    // Ritorno la lista di elementi dalla tabella
    return list;
  }
}
