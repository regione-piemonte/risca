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
import { forkJoin } from 'rxjs';
import { Subscription } from 'rxjs/index';
import { AttivitaSDVo } from '../../../../../core/commons/vo/attivita-sd-vo';
import { PraticaVo } from '../../../../../core/commons/vo/pratica-vo';
import { RimborsoVo } from '../../../../../core/commons/vo/rimborso-vo';
import { SoggettoVo } from '../../../../../core/commons/vo/soggetto-vo';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { TipoRimborsoVo } from '../../../../../core/commons/vo/tipo-rimborso-vo';
import { LoggerService } from '../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../core/services/navigation-helper/navigation-helper.service';
import { AttivitaRimborsiTable } from '../../../../../shared/classes/risca-table/rimborsi/attivita-rimborsi.table';
import { RiepilogoSDRimborsiTable } from '../../../../../shared/classes/risca-table/rimborsi/riepilogo-sd-rimborsi.table';
import { RiscaFormParentAndChildComponent } from '../../../../../shared/components/risca/risca-form-parent-and-child/risca-form-parent-and-child.component';
import { RiscaRimborsoComponent } from '../../../../../shared/components/risca/risca-rimborso/risca-rimborso.component';
import { RiscaTableDataConfig } from '../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { IRiscaTableACEvent } from '../../../../../shared/components/risca/risca-table/utilities/risca-table.interfaces';
import { CommonConsts } from '../../../../../shared/consts/common-consts.consts';
import { RiscaSpinnerService } from '../../../../../shared/services/risca-spinner.service';
import { RiscaAlertService } from '../../../../../shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaMessagesService } from '../../../../../shared/services/risca/risca-messages.service';
import { RiscaModalService } from '../../../../../shared/services/risca/risca-modal.service';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaServerError } from '../../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../../shared/utilities/classes/errors-maps';
import { ICCRMCloseParams } from '../../../interfaces/cambia-creditore-rimborso-modal/cambia-creditore-rimborso-modal.interfaces';
import { SoggettoDatiAnagraficiService } from '../../../service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { DatiContabiliModalService } from '../../../service/dati-contabili/dati-contabili/dati-contabili-modal.service';
import { DatiContabiliUtilityService } from '../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { DatiContabiliService } from '../../../service/dati-contabili/dati-contabili/dati-contabili.service';
import { RimborsiService } from '../../../service/rimborsi/rimborsi.service';
import { RimborsiConsts } from './utilities/rimborsi.consts';
import { RFieldsConfigClass } from './utilities/rimborsi.fields-configs';
import {
  IAsyncDataReq,
  IAsyncDataRes,
  IRimborsi,
} from './utilities/rimborsi.interfaces';
import { IActionRejectSD } from '../../../interfaces/dati-contabili/dati-contabili.interfaces';

/**
 * Interfaccia di comodo che definisce le informazioni per la init delle componenti della pagina.
 */
interface IInitComponenti {
  tipiRimborsi: TipoRimborsoVo[];
  tipiAttivita: AttivitaSDVo[];
  statoDebitorio: StatoDebitorioVo;
  soggettoSD: SoggettoVo;
}

@Component({
  selector: 'rimborsi',
  templateUrl: './rimborsi.component.html',
  styleUrls: ['./rimborsi.component.scss'],
})
export class RimborsiComponent
  extends RiscaFormParentAndChildComponent<IRimborsi>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  RMB_C = RimborsiConsts;

  /** Input boolean che definisce se visualizzare il pulsante "TORNA A STATI DEBITORI". */
  @Input('tornaAStatiDebitori') showTASD: boolean = true;
  /** Input boolean che definisce se visualizzare il pulsante "INDIETRO". */
  @Input('tornaIndietro') showIndietro: boolean = false;

  /** Output event con l'evento collegato al pulsante "INDIETRO". */
  @Output('onTornaIndietro') onTornaIndietro$ = new EventEmitter<any>();

  /** Form di inserimento/modifica del rimborso. */
  @ViewChild('riscaRimborso') riscaRimborso: RiscaRimborsoComponent;

  /** Lista delle attività rimborso nella select. */
  tipiAttivitaRimborso: AttivitaSDVo[] = [];
  /** Lista dei tipi di rimborso nella select della form di inserimento dei nuovi rimborsi. */
  tipiRimborso: TipoRimborsoVo[] = [];

  /** Id della riscossione selezionata. */
  idPratica: number;
  /** PraticaVo con i dati della pratica dello stato debitore. */
  pratica: PraticaVo;
  /** StatoDebitorioVo con le informazioni dello stato debitorio per i rimborsi. */
  statoDebitorio: StatoDebitorioVo;
  /** SoggettoVo con le informazioni del soggetto recuperato dallo stato debitorio. Viene usato per pre-popolare la form del rimborso. */
  soggettoSD: SoggettoVo;

  /** Classe RFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: RFieldsConfigClass;

  /**
   * #############################
   * GESTIONE ACCORDION COMPONENTE
   * #############################
   */
  /** Boolean per la visualizzazione dei nuovi rimborsi per l'utente. */
  showNuoviRimborsi = false;

  /**
   * ################################
   * TABELLE IMPIEGATE NEL COMPONENTE
   * ################################
   */
  /** Oggetto RimborsiTable che conterrà le configurazioni per la tabella dei rimborsi. */
  riepilogoSDRimborsiTable: RiepilogoSDRimborsiTable;
  /** Oggetto AttivitaRimborsiTable che conterrà le configurazioni per la tabella delle attività di rimborso. */
  attivitaRimborsiTable: AttivitaRimborsiTable;

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
    private _datiContabili: DatiContabiliService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _datiContabiliModal: DatiContabiliModalService,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _rimborsi: RimborsiService,
    riscaAlert: RiscaAlertService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaModal: RiscaModalService,
    private _riscaSpinner: RiscaSpinnerService,
    riscaUtilities: RiscaUtilitiesService,
    private _soggettoDA: SoggettoDatiAnagraficiService
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
    this.setupTableStatoDebitorio();
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
  private setupTableStatoDebitorio() {
    /** Oggetto RimborsiTable che conterrà le configurazioni per la tabella dello stato debitorio. */
    this.riepilogoSDRimborsiTable = new RiepilogoSDRimborsiTable({
      pratica: this.pratica,
      statoDebitorio: this.statoDebitorio,
      disableUserInputs: this.disableUserInputs,
    });

    /** Oggetto RimborsiTable che conterrà le configurazioni per la tabella dei rimborsi. */
    this.attivitaRimborsiTable = new AttivitaRimborsiTable({
      attivitaRimborsi: this.statoDebitorio?.rimborsi as RimborsoVo[],
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
    this.formInputs = new RFieldsConfigClass({ riscaFormBuilder });
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
      rimborsi: new FormControl(
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
    // Recupero l'id del soggetto dello stato debitorio
    const idSoggetto = this.statoDebitorio?.id_soggetto;
    // Insieme delle request
    const attivitaReq: IAsyncDataReq = {
      // Chiamata per il soggetto legato allo stato debitorio
      soggettoSD: this._soggettoDA.getSoggetto(idSoggetto),
      // Chiamata per i tipi rimborso
      tipiRimborsi: this._rimborsi.getTipiRimborso(),
      // Chiamata per i tipi attività rimborso
      tipiAttivita: this._rimborsi.getAttivitaStatoDebitorio(),
    };

    // Effettuo la chiamata
    forkJoin(attivitaReq).subscribe({
      next: (res: IAsyncDataRes) => {
        // Prendo i valori
        const { tipiRimborsi, tipiAttivita, soggettoSD } = res;
        // Aggiorno l'alert
        this.aggiornaAlertConfigs(this.alertConfigs);

        // Creo l'oggetto per il set dei dati
        const configs: IInitComponenti = {
          tipiRimborsi,
          tipiAttivita,
          statoDebitorio: this.statoDebitorio,
          soggettoSD,
        };

        // Richiamo la funzione di configurazione
        this.initComponenti(configs);
        // Forzo la chiusura dello spinner
        this._riscaSpinner.hideAll();
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la gestione dell'errore
        this.onServiziError(e);
        // Forzo la chiusura dello spinner
        this._riscaSpinner.hideAll();
        // #
      },
    });
  }

  /**
   * Questa funzione è pensata per valorizzare le componenti della pagina, recuperando le informazioni dall'input.
   * @param configs IIniComponenti contente le configurazioni per le componenti della pagina.
   */
  private initComponenti(configs: IInitComponenti) {
    // Recupero le informazioni dall'oggetto in input
    const tipiRimborsi = configs.tipiRimborsi ?? [];
    const tipiAttivita = configs.tipiAttivita ?? [];
    const statoDebitorio = configs.statoDebitorio;
    const rimborsi = statoDebitorio?.rimborsi ?? [];
    const statiDebitori = statoDebitorio ? [statoDebitorio] : [];
    const soggettoSD = configs.soggettoSD;

    // Assegno localmente il soggetto dello stato debitorio
    this.soggettoSD = soggettoSD;
    // Popolo la select con i tipi rimborso
    this.onScaricoTipiRimborsi(tipiRimborsi);
    // Popolo la select con i tipi attività rimborso
    this.onScaricoTipiAttivitaRimborsi(tipiAttivita);
    // Aggiorno la tabella con lo stato debitorio
    this.riepilogoSDRimborsiTable.setElements(statiDebitori);
    // Aggiorno la tabella con le attività di rimborso
    this.attivitaRimborsiTable.setElements(rimborsi);
  }

  /**
   * Scarico dei tipi rimborso ottenuti dal servizio
   * @param tipiRimborsi TipoRimborsoVo[] scaricati dal servizio
   */
  private onScaricoTipiRimborsi(tipiRimborsi: TipoRimborsoVo[]) {
    // Inserisco i tipi attività nella select
    if (tipiRimborsi != undefined && tipiRimborsi.length > 0) {
      // Definisco la lista dei tipi rimborsi
      this.tipiRimborso = tipiRimborsi;
    }
  }

  /**
   * Scarico dei tipi attività rimborso ottenuti dal servizio
   * @param tipiAttivita AttivitaSDVo[] scaricati dal servizio
   */
  private onScaricoTipiAttivitaRimborsi(tipiAttivita: AttivitaSDVo[]) {
    // Inserisco i tipi attività nella select
    if (tipiAttivita != undefined && tipiAttivita.length > 0) {
      this.tipiAttivitaRimborso = tipiAttivita;
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
      this.RMB_C.TIPO_ATTIVITA,
      this.tipiAttivitaRimborso,
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
    // @Ismaele => Tengo un commento come ancora per l'attività RISCA-ISSUES-76 per forzare get StatoDebitorio e avere TUTTI i dati aggiornati
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
    this.setupTableStatoDebitorio();

    // Recupero le informazioni per aggiornare i dati
    const sd = this.statoDebitorio;
    const statiDebitori = sd ? [sd] : [];
    const rimborsi = sd?.rimborsi;
    // Re-inizializzo i dati delle tabelle
    this.riepilogoSDRimborsiTable.setElements(statiDebitori);
    this.attivitaRimborsiTable.setElements(rimborsi);
    // Ripristino il rimborso
    this.onScaricoTipiAttivitaRimborsi(this.tipiAttivitaRimborso);
  }

  /**
   * Funzione collegata al pulsante "INDIETRO".
   */
  tornaIndietro() {
    // Lancio l'evento associato
    this.onTornaIndietro$.emit(true);
  }

  /**
   * ################
   * FUNZIONI BOTTONI
   * ################
   */

  /**
   * Funzione di supporto che gestisce il success del submit della form.
   * @param data IRimborsi contenente le informazioni del form.
   * @override
   */
  protected onFormSuccess(data: IRimborsi) {
    // Chiamo la funzione di aggiornamento dello stato debitorio
    this._datiContabiliUtility.updateRimborsi(data);
    // Recuperare lo stato debitorio aggiornato
    const statoDebitorio = this._datiContabiliUtility.statoDebitorio;
    // Lancio il servizio di verifica ed aggiornamento dello stato debitorio
    this._datiContabili.verifyAndUpdateStatoDebitorio(statoDebitorio);
  }

  /**
   * Funzione che prepara i dati prima del submit effettivo del form.
   * @override
   */
  prepareMainFormForValidation() {
    // Recupero dalla tabella la lista dei rimborsi
    const rimborsi = this.attivitaRimborsiTable.getDataSource();
    // Imposto all'interno del mainform i rimborsi
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.RMB_C.RIMBORSI,
      rimborsi
    );
  }

  /**
   * ###################################################
   * GESTIONE DELL'ACCORDION E DELL'INSERIMENTO RIMBORSO
   * ###################################################
   */

  /**
   * Funzione agganciata all'accordion per la gestione dei nuovi recapiti alternativi.
   * @param aperto boolean che definisce se l'accordion è aperto o chiuso.
   */
  toggleNuoviRimborsi(aperto: boolean) {
    // Aggiorno lo stato dell'accordion
    this.showNuoviRimborsi = aperto;
  }

  /**
   * Avvia il salvataggio dell'attività rimborso al click del pulsante salva
   */
  aggiungiAttivitaRimborso() {
    // Avvio l'evento di salvataggio
    this.riscaRimborso.aggiungiAttivitaRimborso();
  }

  /**
   * Funzione collegata all'evento di click per la ricerca di un creditore.
   * Aprirò una modale per la ricerca di un creditore, in maniera analoga alla ricerca titolare della pratica.
   */
  onCercaCreditore() {
    // Definisco la callback di success
    const onConfirm = (callback: ICCRMCloseParams) => {
      // Richiamo la funzione di gestione dei dati
      this.onCCRMClose(callback);
    };

    // Richiamo il servizio per l'apertura del modale
    this._datiContabiliModal.apriCambiaCreditoreModal({ onConfirm });
  }

  /**
   * Funzione che gestisce la chiusura della modale di ricerca creditore con i nuovi dati del creditore.
   * @param callback ICCRMCloseParams con i dati di ritorno dalla modale.
   */
  private onCCRMClose(callback: ICCRMCloseParams) {
    // Recupero le informazioni dalla callback
    const { data } = callback || {};
    // Verifico se esistono effettivamente informazioni
    if (data) {
      // Aggiorno la form del rimborso
      this.riscaRimborso.aggiornaCreditoreByRicerca(data);
    }
  }

  /**
   * Funzione che aggiunge un rimborso alla lista delle attività dei rimborsi
   * @param rimborso RimborsoVo da aggiungere
   */
  aggiungiRimborso(rimborso: RimborsoVo) {
    // Aggiungo l'elemento
    this.attivitaRimborsiTable.addElement(rimborso);
    // Chiudi l'inserimento
    this.toggleNuoviRimborsi(false);
  }

  /**
   * ######################
   * FUNZIONI DELLE TABELLE
   * ######################
   */

  /**
   * Cancella il record in input dalla tabella delle attività rimborsi
   * @param event RiscaTableDataConfig<AttivitaRimborsi> da rimuovere
   */
  rimuoviAttivitaRimborso(event: RiscaTableDataConfig<RimborsoVo>) {
    // Lancio la funzione di rimozione dell'elemento
    const onConfirm = () => this.attivitaRimborsiTable.removeElement(event);
    // Richiamo il servizio delle modal per chiedere conferma all'utente
    this._riscaModal.apriModalConfermaCancellazione({ onConfirm });
  }

  /**
   * Modifica il record in input dalla tabella delle attività rimborsi.
   * @param row RiscaTableDataConfig<Rimborso> da modificare.
   */
  modificaAttivitaRimborso(row: RiscaTableDataConfig<RimborsoVo>) {
    // Verifico l'esistenza
    if (!row) {
      return;
    }

    // Prendo l'elemento
    const rimborso = row.original as RimborsoVo;
    // Dichiaro le operazioni di callback
    const onConfirm = (rimborsoUpd: RimborsoVo) => {
      // Aggiorno l'elemento
      this.attivitaRimborsiTable.convertAndUpdateElement(rimborsoUpd, row);
    };
    // Apro la modale di modifica
    this._datiContabiliModal.openModificaRimborsoModal(rimborso, { onConfirm });
  }

  /**
   * Visualizza gli stati debitori collegati all'attività di rimborso
   * @param event IRiscaTableACEvent<Rimborso> per cui visualizzare i dati
   */
  visualizzaStatiDebitoriCollegati(event: IRiscaTableACEvent<RimborsoVo>) {
    // Verifico l'esistenza
    if (!event) {
      return;
    }

    // Prendo l'elemento
    const rimborso = event.row?.original as RimborsoVo;
    // Apro la modale di modifica
    this._datiContabiliModal.openStatiDebitoriCollegatiModal(
      rimborso,
      this.pratica
    );
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
   * Getter di verifica per la tabella dei imborsi.
   */
  get checkRimborsiTable() {
    // Verifico che la tabella esista e abbia dati
    return this.riepilogoSDRimborsiTable?.source?.length > 0;
  }
  /**
   * Getter di verifica per la tabella delle attività di rimborso.
   */
  get checkAttivitaRimborsiTable() {
    // Verifico che la tabella esista e abbia dati
    return this.attivitaRimborsiTable?.source?.length > 0;
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
}
