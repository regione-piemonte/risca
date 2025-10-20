import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/index';
import { AccertamentoVo } from '../../../../../core/commons/vo/accertamento-vo';
import {
  AttivitaSDVo,
  IAttivitaSDVo,
} from '../../../../../core/commons/vo/attivita-sd-vo';
import { PraticaVo } from '../../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { TipoAccertamentoVo } from '../../../../../core/commons/vo/tipo-accertamento-vo';
import { LoggerService } from '../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../core/services/navigation-helper/navigation-helper.service';
import { AttivitaAccertamentiTable } from '../../../../../shared/classes/risca-table/accertamenti/attivita-accertamenti.table';
import { RiepilogoSDAccertamentiTable } from '../../../../../shared/classes/risca-table/accertamenti/riepilogo-sd-accertamenti.table';
import { RiscaAccertamentoComponent } from '../../../../../shared/components/risca/risca-accertamento/risca-accertamento.component';
import { RiscaFormParentAndChildComponent } from '../../../../../shared/components/risca/risca-form-parent-and-child/risca-form-parent-and-child.component';
import { RiscaTableDataConfig } from '../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../../shared/consts/common-consts.consts';
import { RiscaSpinnerService } from '../../../../../shared/services/risca-spinner.service';
import { RiscaAlertService } from '../../../../../shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaMessagesService } from '../../../../../shared/services/risca/risca-messages.service';
import { RiscaModalService } from '../../../../../shared/services/risca/risca-modal.service';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  ICallbackDataModal,
  RiscaServerError,
} from '../../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../../shared/utilities/classes/errors-maps';
import { IActionRejectSD } from '../../../interfaces/dati-contabili/dati-contabili.interfaces';
import { AccertamentiService } from '../../../service/accertamenti/accertamenti.service';
import { DatiContabiliModalService } from '../../../service/dati-contabili/dati-contabili/dati-contabili-modal.service';
import { DatiContabiliUtilityService } from '../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { DatiContabiliService } from '../../../service/dati-contabili/dati-contabili/dati-contabili.service';
import { AccertamentiConsts } from './utilities/accertamenti.consts';
import { AFieldsConfigClass } from './utilities/accertamenti.fields-configs';
import {
  IAccertamenti,
  IAccertamentiInitRes,
  IFormAccertamenti,
} from './utilities/accertamenti.interfaces';

/**
 * Interfaccia di comodo che definisce le informazioni per la init delle componenti della pagina.
 */
interface IIniComponenti {
  tipiAccertamenti: TipoAccertamentoVo[];
  tipiAttivita: AttivitaSDVo[];
  statoDebitorio: StatoDebitorioVo;
}

@Component({
  selector: 'accertamenti',
  templateUrl: './accertamenti.component.html',
  styleUrls: ['./accertamenti.component.scss'],
})
export class AccertamentiComponent
  extends RiscaFormParentAndChildComponent<IAccertamenti>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente una serie di costanti per la gestione dei accertamenti. */
  ACC_C = AccertamentiConsts;

  /** Input boolean che definisce se visualizzare il pulsante "TORNA A STATI DEBITORI". */
  @Input('tornaAStatiDebitori') showTASD: boolean = true;
  /** Input boolean che definisce se visualizzare il pulsante "INDIETRO". */
  @Input('tornaIndietro') showIndietro: boolean = false;

  /** Output event con l'evento collegato al pulsante "INDIETRO". */
  @Output('onTornaIndietro') onTornaIndietro$ = new EventEmitter<any>();

  /** Form di inserimento/modifica del rimborso. */
  @ViewChild('riscaAccertamento') riscaAccertamento: RiscaAccertamentoComponent;

  /** Lista delle attività rimborso nella select. */
  tipiAttivitaAccertamento: AttivitaSDVo[] = [];
  /** Lista dei tipi di rimborso nella select della form di inserimento dei nuovi accertamenti. */
  tipiAccertamento: TipoAccertamentoVo[] = [];

  /** Id della riscossione selezionata. */
  idPratica: number;
  /** PraticaVo con i dati della pratica dello stato debitore. */
  pratica: PraticaVo;
  /** StatoDebitorioVo con le informazioni dello stato debitorio per i accertamenti. */
  statoDebitorio: StatoDebitorioVo;

  /** Classe RFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: AFieldsConfigClass;

  /**
   * #############################
   * GESTIONE ACCORDION COMPONENTE
   * #############################
   */
  /** Boolean per la visualizzazione dei nuovi accertamenti per l'utente. */
  showNuoviAccertamenti = false;

  /**
   * ################################
   * TABELLE IMPIEGATE NEL COMPONENTE
   * ################################
   */
  /** Oggetto StatoDebitorioAccertamentiTable che conterrà le configurazioni per la tabella dei accertamenti. */
  riepilogoSDAccertanentiTable: RiepilogoSDAccertamentiTable;
  /** Oggetto AttivitaAccertamentiTable che conterrà le configurazioni per la tabella delle attività di rimborso. */
  accertamentiTable: AttivitaAccertamentiTable;

  /**
   * ##############################################
   * SUBSCRIPTION AL SALVATAGGIO DELLE INFORMAZIONI
   * ##############################################
   */
  /** Subscription che permette la condivisione dell'evento di modifica di un oggetto StatoDebitorioVo. */
  onSDModSuccess: Subscription;
  /** Subscription che permette la condivisione dell'evento d'errore durante una modifica di un oggetto StatoDebitorioVo. */
  onSDModError: Subscription;
  /** Subscription che permette la condivisione dell'evento di annuallamento della funzionalità di modifica di un oggetto StatoDebitorioVo. */
  onSDModCancel: Subscription;

  /**
   * Costruttore
   */
  constructor(
    private _accertamenti: AccertamentiService,
    private _datiContabili: DatiContabiliService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _datiContabiliModal: DatiContabiliModalService,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaModal: RiscaModalService,
    private _riscaSpinner: RiscaSpinnerService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages,
      riscaUtilities
    );

    // Lancio il setup del componente
    this.setupComponente();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup del componente.
   */
  private setupComponente() {
    // Lancio il setup dei dati della pratica
    this.setupPratica();
    // Lancio il setup dei dati dello stato debitorio
    this.setupStatoDebitorio();
    // Lancio il setup per la tabella degli stati debitori (N.B.: DA FARE DOPO SETUP PRATICA!)
    this.setupTables();
    // Lancio il setup delle forms
    this.setupFields();
  }

  /**
   * Funzione di comodo per il recupero dell'informazione della pratica dal servizio condiviso.
   */
  private setupPratica() {
    // Recupero dal servizio condiviso le informazioni per l'id pratica
    this.idPratica = this._datiContabiliUtility.idPratica;
    // Recupero dal servizio condiviso le informazioni per la pratica
    this.pratica = this._datiContabiliUtility.pratica;
  }

  /**
   * Funzione di comodo per il recupero dell'informazione dello stato debitorio dal servizio condiviso.
   */
  private setupStatoDebitorio() {
    // Recupero dal servizio condiviso le informazioni per lo stato debitorio
    this.statoDebitorio = this._datiContabiliUtility.statoDebitorio;
  }

  /**
   * Funzione di setup per la tabella dello stato debitorio.
   */
  private setupTables() {
    /** Oggetto StatoDebitorioAccertamentiTable che conterrà le configurazioni per la tabella dello stato debitorio. */
    this.riepilogoSDAccertanentiTable = new RiepilogoSDAccertamentiTable({
      statoDebitorio: this.statoDebitorio,
      disableUserInputs: this.disableUserInputs,
    });

    /** Oggetto AttivitaAccertamentiTable che conterrà le configurazioni per la tabella degli accertamenti. */
    this.accertamentiTable = new AttivitaAccertamentiTable({
      attivitaAccertamenti: this.statoDebitorio?.accertamenti,
      disableUserInputs: this.disableUserInputs,
    });
  }

  /**
   * Funzione di setup per i campi del form del componente.
   */
  private setupFields() {
    // Definisco in una variabile il servizio del builder
    const riscaFormBuilder = this._riscaFormBuilder;
    // Inizializzo le configurazioni per i campi
    this.formInputs = new AFieldsConfigClass({ riscaFormBuilder });
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Init
   */
  ngOnInit() {
    // Inizializzazione della form principale
    this.initMainForm();
    // Avvio la ricerca dei dati contabili
    this.downloadAndInitComponenti();
    // Lancio l'init dei listener del componente
    this.initListeners();
  }

  /**
   * Destroy
   * @override
   */
  ngOnDestroy(): void {
    // Invoco il destroy del padre
    super.ngOnDestroy();
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onSDModSuccess) {
        this.onSDModSuccess.unsubscribe();
      }
      if (this.onSDModError) {
        this.onSDModError.unsubscribe();
      }
      if (this.onSDModCancel) {
        this.onSDModCancel.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * Funzione che prevede l'inizializzazione della variabile mainForm per la gestione del form d'inserimento.
   * @override
   */
  protected initMainForm() {
    // Inizializzo la form
    this.mainForm = this._formBuilder.group({
      attivita_stato_deb: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      accertamenti: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
    });

    // Verifico la gestione di disabilitazione delle form
    if (this.disableUserInputs) {
      // Disabilito le operazioni
      this.mainForm.disable();
    }
  }

  /**
   * Funzione che scarica le informazioni per le varie parti della pagina.
   * Una volta scaricate le informazioni, vengono valorizzati e inizializzati tutti i dati.
   */
  private downloadAndInitComponenti() {
    // Richiamo la funzione del servizio per le informazioni di base del componente
    this._accertamenti.getInfoAccertamenti().subscribe({
      next: (info: IAccertamentiInitRes) => {
        // Forzo il blocco dello spinner
        this._riscaSpinner.hideAll();
        // Aggiorno l'alert
        this.aggiornaAlertConfigs(this.alertConfigs);

        // Prendo i valori
        const { tipiAccertamenti, tipiAttivita } = info;
        // Creo l'oggetto per il set dei dati
        const configs: IIniComponenti = {
          tipiAccertamenti,
          tipiAttivita,
          statoDebitorio: this.statoDebitorio,
        };

        // Richiamo la funzione di configurazione
        this.initComponenti(configs);
        // #
      },
      error: (e: RiscaServerError) => {
        // Forzo il blocco dello spinner
        this._riscaSpinner.hideAll();
        // Gestisco il messaggio d'errore inaspettato
        this.onServiziError(e);
        // #
      },
    });
  }

  /**
   * Questa funzione è pensata per valorizzare le componenti della pagina, recuperando le informazioni dall'input.
   * @param configs IIniComponenti contente le configurazioni per le componenti della pagina.
   */
  private initComponenti(configs: IIniComponenti) {
    // Recupero le informazioni dall'oggetto in input
    const tipiAccertamenti = configs.tipiAccertamenti ?? [];
    const tipiAttivita = configs.tipiAttivita ?? [];
    const statoDebitorio = configs.statoDebitorio;
    const accertamenti = statoDebitorio?.accertamenti ?? [];
    const statiDebitori = statoDebitorio ? [statoDebitorio] : [];

    // Popolo la select con i tipi rimborso
    this.onScaricoTipiAccertamenti(tipiAccertamenti);
    // Popolo la select con i tipi attività rimborso
    this.onScaricoTipiAttivitaAccertamenti(tipiAttivita);
    // Aggiorno la tabella con lo stato debitorio
    this.riepilogoSDAccertanentiTable.setElements(statiDebitori);
    // Aggiorno la tabella con le attività di rimborso
    this.accertamentiTable.setElements(accertamenti);
  }

  /**
   * Scarico dei tipi rimborso ottenuti dal servizio
   * @param tipiAccertamenti TipoAccertamentoVo[] scaricati dal servizio
   */
  private onScaricoTipiAccertamenti(tipiAccertamenti: TipoAccertamentoVo[]) {
    // Inserisco i tipi attività nella select
    if (tipiAccertamenti != undefined && tipiAccertamenti.length > 0) {
      // Assegno i tipi accertamenti
      this.tipiAccertamento = tipiAccertamenti;
    }
  }

  /**
   * Scarico dei tipi attività rimborso ottenuti dal servizio
   * @param tipiAttivita TipoAccertamentoVo[] scaricati dal servizio
   */
  private onScaricoTipiAttivitaAccertamenti(tipiAttivita: AttivitaSDVo[]) {
    // Inserisco i tipi attività nella select
    if (tipiAttivita != undefined && tipiAttivita.length > 0) {
      this.tipiAttivitaAccertamento = tipiAttivita;
    }
    // Funzione di find
    const find = (a: AttivitaSDVo, b: AttivitaSDVo) => {
      // Se uno dei due è undefined, non li confronto nemmeno
      if (a == undefined || b == undefined) {
        // Sono certamente diversi
        return false;
      }
      // Altrimenti controllo se sono uguali
      return a.cod_attivita_stato_deb == b.cod_attivita_stato_deb;
    };

    // Seleziono il valore
    this._riscaUtilities.setFormValueAndSelect(
      this.mainForm,
      this.ACC_C.TIPO_ATTIVITA,
      this.tipiAttivitaAccertamento,
      this.statoDebitorio?.attivita_stato_deb,
      find
    );
  }

  /**
   * Funzione di init per i listener del componente, per rimanere in ascolto degli eventi per il cambio pagina dei dati contabili.
   */
  private initListeners() {
    this.onSDModSuccess = this._datiContabili.onSDModSuccess$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Gestisco l'evento di success
        this.onSDModSuccessResult(statoDebitorio);
      },
    });

    this.onSDModError = this._datiContabili.onSDModError$.subscribe({
      next: (error: RiscaServerError) => {
        // Gestisco l'errore anche nel caso sia multiplo
        this.onSDModErrorResult(error);
      },
    });

    this.onSDModCancel = this._datiContabili.onSDModCancel$.subscribe({
      next: (reject?: IActionRejectSD) => {
        // Gestisco l'evento di cancellazione
        this.onSDModCancelResult(reject);
      },
    });
  }

  /**
   * Gestisce le operazioni da eseguire alla fine del successo nel salvataggio dello stato debitorio.
   * @param statoDebitorio StatoDebitorioVo salvato.
   */
  onSDModSuccessResult(statoDebitorio: StatoDebitorioVo) {
    // Resetto l'alert
    this.resetAlertConfigs(this.alertConfigs);

    // Utilizzo la funzione di comodo per gestire il salvataggio dati
    const alertSuccess =
      this._datiContabiliUtility.onStatoDebitorioSalvato(statoDebitorio);
    // Aggiorno l'alert con la configurazione ritornata
    this.alertConfigs = alertSuccess;

    // Vado a riaggiornre i dati della pagina con le informazioni salvate
    this.aggiornaPagina();
  }

  /**
   * Gestisce le operazioni da eseguire in caso di errore nel salvataggio dello stato debitorio.
   * @param error RiscaServerError con le informazioni di errore generate dal server.
   */
  onSDModErrorResult(error: RiscaServerError) {
    // Nella cancel/close devo eseguire la reset dello stato debitorio
    this._datiContabiliUtility.restoreStatoDebitorio();
    // Gestisco l'errore anche nel caso sia multiplo
    this.onServiziErrors(error);
  }

  /**
   * Gestisce le operazioni da eseguire alla fine della cancellazione nel salvataggio dello stato debitorio.
   * @param reject IActionRejectSD contenente possibili informazioni per gestire la reject utente.
   */
  onSDModCancelResult(reject?: IActionRejectSD) {
    // Nella cancel/close devo eseguire la reset dello stato debitorio
    this._datiContabiliUtility.restoreStatoDebitorio();

    // Verifico se ci sono informazioni per la reject
    if (reject) {
      // Genero l'alert partendo dalla reject
      this.alertConfigs = this._datiContabiliUtility.alertConfigsReject(reject);
    }
  }

  /**
   * Funzione che ripristina le informazioni della pagina al termine delle operazioni di
   * salvataggio, gestione errori e annullamento della modifica
   * dello stato debitorio con le informazioni di questa pagina.
   */
  aggiornaPagina() {
    // Lancio il setup dei dati dello stato debitorio
    this.setupStatoDebitorio();
    // Lancio il setup per la tabella degli stati debitori (N.B.: DA FARE DOPO SETUP PRATICA!)
    this.setupTables();

    // Recupero le informazioni per aggiornare i dati
    const sd = this.statoDebitorio;
    const statoDebitorio = sd ? [sd] : [];
    const accertamenti = sd?.accertamenti;
    // Re-inizializzo i dati delle tabelle
    this.riepilogoSDAccertanentiTable.setElements(statoDebitorio);
    this.accertamentiTable.setElements(accertamenti);
    // Ripristino il rimborso
    this.onScaricoTipiAttivitaAccertamenti(this.tipiAttivitaAccertamento);
  }

  /**
   * ################
   * FUNZIONI BOTTONI
   * ################
   */

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @returns IAccertamenti contente le informazioni del form in modalità raw value.
   * @override
   */
  getMainFormRawValue(): IAccertamenti {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }

    // Recupero l'oggetto generato dal form dati
    let formDati: IFormAccertamenti;
    formDati = this.mainForm.getRawValue();

    // Definisco l'oggetto contenente i dati da ritornare dal form
    let dati: IAccertamenti;
    dati = { accertamenti: undefined, attivita_stato_deb: undefined };

    // Definisco le informazioni specifiche da ritornare
    dati.accertamenti = formDati.accertamenti;

    // Recupero l'attività stato debitorio dal form
    const attivitaSD: IAttivitaSDVo = formDati.attivita_stato_deb;
    // Verifico l'attività dello stato debitorio
    if (attivitaSD?.cod_attivita_stato_deb != undefined) {
      // L'attività è stata selezionata, la converto e passo all'oggetto
      dati.attivita_stato_deb = new AttivitaSDVo(formDati.attivita_stato_deb);
      // #
    }

    // Ritorno i dati formattati
    return dati;
  }

  /**
   * Funzione di supporto che gestisce il success del submit della form.
   * @param data IAccertamenti contenente le informazioni del form.
   * @override
   */
  protected onFormSuccess(data: IAccertamenti) {
    // Estraggo dall'input le informazioni per l'attività dello stato debitorio
    const attivitaSD: AttivitaSDVo = data?.attivita_stato_deb;
    // Recupero l'id dello stato debitorio
    const idStatoDebitorio: number = this.idStatoDebitorio;

    // Lancio il servizio di verifica ed aggiornamento dello stato debitorio
    this._datiContabili
      .putAttivitaStatoDebitorio(attivitaSD, idStatoDebitorio)
      .subscribe({
        next: (statoDebitorioPartial: StatoDebitorioVo) => {
        // Aggiorno i dati della pagina
          this.onAttivitaSDAggiornata(attivitaSD);
          //#
        },
        error: (err: RiscaServerError) => {
          // Gestisco l'errore
          this.onServiziError(err);
          //#
        },
      });
  }

  /**
   * Funzione che esegue le logiche di segnalazione/gestione dati a seguito dell'aggiornamento dell'attività stato debitorio per lo stato debitorio.
   * @param attivitaSD AttivitaSDVo con le informazioni aggiornate.
   */
  private onAttivitaSDAggiornata(attivitaSD: AttivitaSDVo) {
    // Chiamo la funzione di aggiornamento dello stato debitorio
    this._datiContabiliUtility.updateAttivitaStatoDebitorio(attivitaSD);

    // Recupero l'oggetto dello stato debitorio aggiornato
    const statoDebitorio: StatoDebitorioVo = this.statoDebitorio;
    // Utilizzo la funzione di comodo per gestire la segnalazione sui dati salvati e forzo la funzione a non aggiornare (nuovamente) lo stato debitorio nel servizio
    // L'aggiornamento è fatto dalla funzione sopra: this._datiContabiliUtility.updateAttivitaStatoDebitorio(attivitaSD);
    const alertSuccess = this._datiContabiliUtility.onStatoDebitorioSalvato(
      statoDebitorio,
      true
    );
    // Aggiorno l'alert con la configurazione ritornata per la segnalazione utente
    this.alertConfigs = alertSuccess;
  }

  /**
   * Funzione che prepara i dati prima del submit effettivo del form.
   * @override
   */
  prepareMainFormForValidation() {
    // Recupero dalla tabella la lista degli accertmenti
    const accertamenti = this.accertamentiTable.getDataSource();
    // Imposto all'interno del mainform gli accertmenti
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.ACC_C.ACCERTAMENTI,
      accertamenti
    );
  }

  /**
   * Funzione collegata al pulsante "INDIETRO".
   */
  tornaIndietro() {
    // Lancio l'evento associato
    this.onTornaIndietro$.emit(true);
  }

  /**
   * #######################################################
   * GESTIONE DELL'ACCORDION E DELL'INSERIMENTO ACCERTAMENTO
   * #######################################################
   */

  /**
   * Funzione agganciata all'accordion per la gestione dei nuovi accertamenti.
   * @param aperto boolean che definisce se l'accordion è aperto o chiuso.
   */
  toggleNuoviAccertamenti(aperto: boolean) {
    // Aggiorno lo stato dell'accordion
    this.showNuoviAccertamenti = aperto;
  }

  /**
   * Avvia il salvataggio dell'attività rimborso al click del pulsante salva
   */
  aggiungiAccertamento() {
    // Avvio l'evento di salvataggio
    this.riscaAccertamento.aggiungiAttivitaAccertamento();
  }

  /**
   * Funzione che aggiunge un nuovo accertamento alla lista delle attività degli accertamenti.
   * @param accertamento AccertamentoVo con le informazioni d'aggiungere alla tabella.
   */
  onAggiungiAccertamento(accertamento: AccertamentoVo) {
    // Resetto eventuali messaggistica presente
    this.resetAlertConfigs();

    // In inserimento di un accertamento non è presente l'id dello stato debitorio, lo aggiungo
    // In modifica di un accertamento l'informazion è presente nell'oggetto di input e quindi è gestito già
    accertamento.id_stato_debitorio = this.statoDebitorio?.id_stato_debitorio;

    // Richiamo il servizio per inserire il nuovo accertamento
    this._accertamenti.postAccertamento(accertamento).subscribe({
      next: (accertamentoIns: AccertamentoVo) => {
        // Aggiungo l'elemento
        this.accertamentiTable.addElement(accertamentoIns);
        // Chiudi l'inserimento
        this.toggleNuoviAccertamenti(false);
        // Gestisco la sincronizzazione degli accertamenti con lo stato debitorio
        this.sincronizzaAccertamentiStatoDebitorio();
        // #
      },
      error: (e: RiscaServerError) => {
        // Segnalo eventuali errori
        this.onServiziError(e);
        // #
      },
    });
  }

  /**
   * ######################
   * FUNZIONI DELLE TABELLE
   * ######################
   */

  /**
   * Funzione collegata all'evento di "cancellazione" di un accertamento dalla tabella degli accertamenti dello stato debitorio.
   * La funzione cancellerà le informazioni e gestirà il flusso applicativo.
   * @param cancellaAccertamento RiscaTableDataConfig<AccertamentoVo> da rimuovere.
   */
  rimuoviAccertamento(
    cancellaAccertamento: RiscaTableDataConfig<AccertamentoVo>
  ) {
    // Resetto eventuali messaggistica presente
    this.resetAlertConfigs();

    // Lancio la funzione di rimozione dell'elemento
    const onConfirm = () => {
      // L'utente ha confermato la cancellazione dell'accertamento
      this.procediRimuoviAccertamento(cancellaAccertamento);
      // #
    };
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConfermaCancellazione({ onConfirm });
    // #
  }

  /**
   * Funzione che esegue le logiche di cancellazione di un accertamento al "conferma" utente per la cancellazione.
   * @param cancellaAccertamento RiscaTableDataConfig<AccertamentoVo> da rimuovere.
   */
  private procediRimuoviAccertamento(
    cancellaAccertamento: RiscaTableDataConfig<AccertamentoVo>
  ) {
    // Recupero dalla configurazione le informazioni dell'accertamento
    const accertamento: AccertamentoVo = cancellaAccertamento?.original;
    const idAccertamento: number = accertamento?.id_accertamento;

    // Lancio la funzione di cancellazione per l'accertamento
    this._accertamenti.deleteAccertamento(idAccertamento).subscribe({
      next: (response: any) => {
        // Accertamento cancellato, lo rimuovo dalla tabella
        this.accertamentiTable.removeElement(cancellaAccertamento);
        // Gestisco la sincronizzazione degli accertamenti con lo stato debitorio
        this.sincronizzaAccertamentiStatoDebitorio();
        // #
      },
      error: (e: RiscaServerError) => {
        // Segnalo l'errore
        this.onServiziError(e);
        // #
      },
    });
  }

  /**
   * Modifica il record in input dalla tabella degli accertamenti.
   * @param rowAccertamento RiscaTableDataConfig<AccertamentoVo> da modificare.
   */
  modificaAccertamento(rowAccertamento: RiscaTableDataConfig<AccertamentoVo>) {
    // Verifico l'esistenza
    if (!rowAccertamento) {
      // Blocco le logiche
      return;
    }
    // Resetto eventuali messaggistica presente
    this.resetAlertConfigs();

    // Prendo l'elemento
    const accertamento = rowAccertamento.original as AccertamentoVo;
    // Dichiaro le operazioni di callback
    const onConfirm = (accertamentoUpd: AccertamentoVo) => {
      // L'utente ha confermato la cancellazione dell'oggetto
      this.proseguiModificaAccertamento(accertamentoUpd, rowAccertamento);
      // #
    };
    // Definisco un oggetto contenenti le callback
    const cb: ICallbackDataModal = { onConfirm };

    // Apro la modale di modifica
    this._datiContabiliModal.openModificaAccertamentoModal(accertamento, cb);
  }

  /**
   * Funzione che gestisce le logiche di aggiornamento dell'accertamento a seguito della conferma utente delle modifiche effettuate nella modale.
   * @param accertamento AccertamentoVo con l'oggetto dati contenente le informazioni dell'accertamento da modificare.
   * @param rowAccertamento RiscaTableDataConfig<AccertamentoVo> con l'oggetto di configurazione della tabella contenente i dati dell'accertamento.
   */
  private proseguiModificaAccertamento(
    accertamento: AccertamentoVo,
    rowAccertamento: RiscaTableDataConfig<AccertamentoVo>
  ) {
    // Richiamo la funzione di modifica per l'accertamento
    this._accertamenti.putAccertamento(accertamento).subscribe({
      next: (accertamentoSaved: AccertamentoVo) => {
        // Aggiorno l'elemento
        this.accertamentiTable.convertAndUpdateElement(
          accertamentoSaved,
          rowAccertamento
        );
        // Gestisco la sincronizzazione degli accertamenti con lo stato debitorio
        this.sincronizzaAccertamentiStatoDebitorio();
        // #
      },
      error: (e: RiscaServerError) => {
        // Segnalo l'errore
        this.onServiziError(e);
        // #
      },
    });
  }

  /**
   * Funzione di supporto che raccoglie le informazioni all'interno della tabella degli accertamenti e sincronizza i sullo stato debitorio.
   * La tabella verrà sempre aggiornata a seguito delle possibili CRUD sulle informazioni degli accertamenti.
   * A seguito di tali modifiche, bisogna allineare le informazioni sullo stato debitorio e sulla pagina.
   * @author Ismaele Bottelli
   * @date 20/01/2025
   * @git RISCA-ISSUES-60
   * @notes Nuova implementazione della logica.
   */
  private sincronizzaAccertamentiStatoDebitorio() {
    // Recupero le informazioni dalla tabella degli accertamenti
    const accertamenti: AccertamentoVo[] =
      this.accertamentiTable.getDataSource();

    // Recuperate le informazioni, aggiorno i dati nel servizio condiviso
    this._datiContabiliUtility.updateAccertamenti(accertamenti);
    // Aggiornati i dati, vado a lanciare l'allineamento delle informazioni per lo stato debitorio
    // NOTA BENE: la collega @Valeria_Foco ha confermato che non c'è bisogno di fare aggiornamenti per tutta la pagina a seguito delle modifiche sugli accertamenti
    this.setupStatoDebitorio();

    // Recupero l'oggetto dello stato debitorio aggiornato
    const statoDebitorio: StatoDebitorioVo = this.statoDebitorio;
    // Utilizzo la funzione di comodo per gestire la segnalazione sui dati salvati e forzo la funzione a non aggiornare (nuovamente) lo stato debitorio nel servizio
    // L'aggiornamento è fatto dalla funzione sopra: this._datiContabiliUtility.updateAccertamenti(accertamenti);
    const alertSuccess = this._datiContabiliUtility.onStatoDebitorioSalvato(
      statoDebitorio,
      true
    );
    // Aggiorno l'alert con la configurazione ritornata per la segnalazione utente
    this.alertConfigs = alertSuccess;
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertConfigs);
  }

  /**
   * Getter di verifica per la tabella dello stato debitorio.
   */
  get checkSDTable() {
    // Verifico che la tabella esista e abbia dati
    return this.riepilogoSDAccertanentiTable?.source?.length > 0;
  }

  /**
   * Getter di verifica per la tabella delle attività di accertamento.
   */
  get checkAccertamentiTable() {
    // Verifico che la tabella esista e abbia dati
    return this.accertamentiTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.isPraticaLocked;
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

  /**
   * Getter di comodo che recupera l'id stato debitorio dall'oggetto del componente.
   * @returns number con l'informazione estratta.
   */
  get idStatoDebitorio(): number {
    // Ritorno l'informazione
    return this.statoDebitorio?.id_stato_debitorio;
  }
}
