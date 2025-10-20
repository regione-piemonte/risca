import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { InteressiDiMoraVo } from 'src/app/core/commons/vo/interessi-di-mora-vo';
import { InteressiLegaliVo } from 'src/app/core/commons/vo/interessi-legali-vo';
import {
  IInteressiDiMoraTableConfigs,
  InteressiDiMoraTable,
  interessiDiMoraTablePagination,
} from 'src/app/shared/classes/risca-table/tassi-di-interesse/interessi-di-mora/interessi-di-mora.table';
import {
  IInteressiLegaliTableConfigs,
  InteressiLegaliTable,
  interessiLegaliTablePagination,
} from 'src/app/shared/classes/risca-table/tassi-di-interesse/interessi-legali/interessi-legali.table';
import { RiscaTableDataConfig } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.classes';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { IJourneySnapshot } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import {
  ICallbackDataModal,
  RiscaServerError,
  RiscaTablePagination,
  AppActions,
  RiscaAlertConfigs,
  RiscaInfoLevels,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IInteressiDiMoraFormDataModal } from '../../modals/interessi-di-mora-modal/utilities/interessi-di-mora-modal.interface';
import { IInteressiLegaliFormDataModal } from '../../modals/interessi-legali-modal/utilities/interessi-legali-modal.inteface';
import { InteressiDiMoraService } from '../../services/interessi-di-mora/interessi-di-mora.service';
import { InteressiLegaliService } from '../../services/interessi-legali/interessi-legali.service';
import { TassiDiInteresseModalService } from '../../services/tassi-di-interesse/tassi-di-interesse-modal.service';
import {
  IApriDettaglioInteressiLegaliModal,
  IApriDettaglioInteressiMoraModal,
} from '../../services/tassi-di-interesse/utilies/tassi-di-interesse-modal.interfaces';
import { InteressiDiMoraFormComponent } from '../interessi-di-mora-form/interessi-di-mora-form.component';
import { IInteressiDiMoraFormData } from '../interessi-di-mora-form/utilities/interessi-di-mora-form.interfaces';
import { InteressiLegaliFormComponent } from '../interessi-legali-form/interessi-legali-form.component';
import { IInteressiLegaliFormData } from '../interessi-legali-form/utilities/interessi-legali-form.interfaces';
import { TassiDiInteresseConsts } from './utilities/tassi-di-interesse.consts';

@Component({
  selector: 'tassi-di-interesse',
  templateUrl: './tassi-di-interesse.component.html',
  styleUrls: ['./tassi-di-interesse.component.scss'],
})
export class TassiDiInteresseComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti del componente di riferimento. */
  TDI_C = new TassiDiInteresseConsts();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** ViewChild per poter accedere al metodo reset e resettera i campi della form Interessi Legali una volta conclusa la post. */
  @ViewChild('interessiLegaliFormComponent')
  interessiLegaliFormComponent: InteressiLegaliFormComponent;
  /** ViewChild per poter accedere al metodo reset e resettera i campi della form Interessi di mora una volta conclusa la post. */
  @ViewChild('interessiDiMoraFormComponent')
  interessiDiMoraFormComponent: InteressiDiMoraFormComponent;

  /** InteressiLegaliTable con la configurazione per la tabella dati per: interessi legali. */
  interessiLegaliTable: InteressiLegaliTable;
  /** InteressiDiMoraTable con la configurazione per la tabella dati per: interessi di mora. */
  interessiDiMoraTable: InteressiDiMoraTable;

  /** boolean che contiene la configurazione di gestione per l'abilitazione degli elementi applicativi della pagina. */
  AEA_TDIDisabled: boolean;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. Questo allert è specifico per: Interessi legali */
  alertInteressiLegaliConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. Questo allert è specifico per: Interessi di mora */
  alertInteressiMoraConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    logger: LoggerService,
    private _interessiLegaliService: InteressiLegaliService,
    private _interessiDiMora: InteressiDiMoraService,
    navigationHelper: NavigationHelperService,
    riscaAlertService: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessageService: RiscaMessagesService,
    private _riscaModal: RiscaModalService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _tassiDiInteresseModalService: TassiDiInteresseModalService
  ) {
    super(
      logger,
      navigationHelper,
      riscaAlertService,
      riscaFormSubmitHandler,
      riscaMessageService
    );
    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Funzione di gestione logiche init del componente
    this.initComponente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
    // Imposto le configurazioni di navigazioni per il componente
    this.stepConfig = this.TDI_C.NAVIGATION_CONFIG;
    // Imposto il setup per le chiavi di accesso per le componenti applicative
    this.setupTDIDisabled();
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  setupTDIDisabled() {
    // Recupero la chiave per la configurazione della form
    const tdiKey = this.AEAK_C.PAG_TASSI_INTERESSE;
    // Recupero la configurazione della form dal servizio
    this.AEA_TDIDisabled = this._accessoElementiApp.isAEADisabled(tdiKey);
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di init per le logiche del componente.
   */
  private initComponente() {
    // Richiamo l'init per le informazioni della tabella
    this.initTabelleTassiDiInteresse();
  }

  /**
   * Funzione di init per la tabella dei dati per gli interessi legali.
   */
  private initTabelleTassiDiInteresse() {
    // Richiamo la funzione di ricerca dati per gli interessi legali
    this.cercaInteressiLegali();
    // Richiamo la funzione di ricerca dati per gli interessi di mora
    this.cercaInteressiDiMora();
  }

  /**
   * Funzione di setup per la tabella degli interessi legali.
   */
  setupTabellaInteressiLegali() {
    // Verifico se non esiste la configurazione della tabella
    if (!this.interessiLegaliTable) {
      // Creo la configurazione iniziale della tabella passando le informazioni della configurazione
      let tableConfig: IInteressiLegaliTableConfigs;
      tableConfig = { AEADisabled: this.AEA_TDIDisabled };
      // Creo la tabella con la configurazione per gli interessi legali
      this.interessiLegaliTable = new InteressiLegaliTable(tableConfig);
      // #
    }
  }

  /**
   * Funzione di setup per la tabella degli interessi di mora.
   */
  setupTabellaInteressiDiMora() {
    // Verifico se non esiste la configurazione della tabella
    if (!this.interessiDiMoraTable) {
      // Creo la configurazione iniziale della tabella passando le informazioni della configurazione
      let tableConfig: IInteressiDiMoraTableConfigs;
      tableConfig = { AEADisabled: this.AEA_TDIDisabled };
      // creo la tabella con la configurazione per gli interessi di mora
      this.interessiDiMoraTable = new InteressiDiMoraTable(tableConfig);
      // #
    }
  }

  /**
   * ############################
   * FUNZIONI DI GESTIONE TABELLA
   * ############################
   */

  /**
   * Funzione collegata all'evento di submit del componente interessiLegaliForm.
   * @param interessiLegaliForm IInteressiLegaliFormData che contiene le informazioni submittate del form.
   */
  interessiLegaliFormSubmit(interessiLegaliForm: IInteressiLegaliFormData) {
    // Verifico l'oggetto ritornato dalla form
    if (!interessiLegaliForm) {
      // Non esiste, blocco le logiche
      return;
    }

    //Resetto eventuali segnalazioni di errore su interessi legali
    this.resetAlertConfigs(this.alertInteressiLegaliConfigs);
    //Resetto eventuali segnalazioni di errore su interessi di mora
    this.resetAlertConfigs(this.alertInteressiMoraConfigs);

    // Estraggo le informazioni dall'oggetto del form
    const { percentuale, dataInizio, giorni } = interessiLegaliForm;
    // Converto la data inizio da mandare al server
    let dataInizioServer: string;
    dataInizioServer =
      this._riscaUtilities.convertNgbDateStructToServerDate(dataInizio);

    // Creo un oggetto per gli interessi legali come classe dati
    let interessiLegali = new InteressiLegaliVo({
      percentuale: percentuale,
      data_inizio: dataInizioServer,
      giorni_legali: giorni,
      tipo_interesse: this.TDI_C.INTERESSE_LEGALE,
    });

    // Lancio il salvataggio dati per il nuovo interesse legale aggiunto
    this._interessiLegaliService.postInteressiLegali(interessiLegali).subscribe({
      next: (interessiLegaliSaved: InteressiLegaliVo) => {
        // Resetto la form
        this.interessiLegaliFormComponent.onFormReset();
        // Lancio l'aggiornamento dei dati per la tabella
        this.cercaInteressiLegaliByPaginazioneCorrente();
        // Recupero il codice del messaggio per avvenuto inserimento 
        const code = RiscaNotifyCodes.P001;
        // Visualizzo il messeggio di avvenuta cancellazione del dato
        this.alertInteressiLegaliConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
      },
      error: (e: RiscaServerError) => {
        // Richiamo le funzione di gestione degli errori
        this.onServiziErrorInteressiLegali(e);
        // #
      },
    });
  }
  /**
   * Funzione collegata all'evento di submit del componente interessiDiMoraForm.
   * @param interessiDiMoraForm IInteressiDiMoraFormData che contiene le informazioni submittate del form.
   */
  interessiDiMoraFormSubmit(interessiDiMoraForm: IInteressiDiMoraFormData) {
    // Verifico l'oggetto ritornato dalla form
    if (!interessiDiMoraForm) {
      // Non esiste, blocco le logiche
      return;
    }

    //Resetto eventuali segnalazioni di errore su interessi di mora
    this.resetAlertConfigs(this.alertInteressiMoraConfigs);
    //Resetto eventuali segnalazioni di errore su interessi legali
    this.resetAlertConfigs(this.alertInteressiLegaliConfigs);

    // Estraggo le informazioni dall'oggetto del form
    const { percentuale, dataInizio } = interessiDiMoraForm;
    // Converto la data inizio da mandare al server
    let dataInizioServer: string;
    dataInizioServer =
      this._riscaUtilities.convertNgbDateStructToServerDate(dataInizio);

    // Creo un oggetto per gli interessi legali come classe dati
    let interessiDiMora = new InteressiDiMoraVo({
      percentuale: percentuale,
      data_inizio: dataInizioServer,
      giorni_legali: undefined,
      tipo_interesse: this.TDI_C.INTERESSE_DI_MORA,
    });

    // Lancio il salvataggio dati per il nuovo interesse legale aggiunto
    this._interessiDiMora.postInteressiDiMora(interessiDiMora).subscribe({
      next: (interessiDiMoraSaved: InteressiDiMoraVo) => {
        // Resetto la form
        this.interessiDiMoraFormComponent.onFormReset();
        // Lancio l'aggiornamento della tabella data la sua paginazione
        this.cercaInteressiDiMoraByPaginazioneCorrente();
        // Recupero il codice del messaggio per avvenuto inserimento 
        const code = RiscaNotifyCodes.P001;
        // Visualizzo il messeggio di avvenuta cancellazione del dato
        this.alertInteressiMoraConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione degli errori
        this.onServiziErrorInteressiDiMora(e);
      },
    });
  }

  /**
   * Funzione collegata all'evento di modifica riga della tabella per gli interessi legali.
   * @param row RiscaTableDataConfig<InteressiLegaliVo> con le informazioni della riga da modificare.
   */
  onModificaInteressiLegali(row: RiscaTableDataConfig<InteressiLegaliVo>) {
    //Resetto eventuali segnalazioni di errore su interessi legali
    this.resetAlertConfigs(this.alertInteressiLegaliConfigs);
    //Resetto eventuali segnalazioni di errore su interessi di mora
    this.resetAlertConfigs(this.alertInteressiMoraConfigs);
    // Recupero il dato originale dalla riga
    const interessiLegali: InteressiLegaliVo = row?.original;
    // Definisco le informazioni per i dati da passare alla modale
    const dataModal: IInteressiLegaliFormDataModal = {
      interessiLegali,
      readOnly: false,
    };
    // Definisco le chiamate per le callback
    const callbacks: ICallbackDataModal = {
      onConfirm: (payload: any) => {
        // Cancello possibili alert aperti con altre segnalazioni
        this.resetAlertConfigs();
        // Lancio la ricerca con paginazione per il nuovo dato aggiornato
        this.cercaInteressiLegaliByPaginazioneCorrente();
        // Recupero il codice del messaggio per avvenuto inserimento 
        const code = RiscaNotifyCodes.P001;
        // Visualizzo il messeggio di avvenuta cancellazione del dato
        this.alertInteressiLegaliConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
      },
    };
    // Definisco la configurazione per l'apertura della modale
    const config: IApriDettaglioInteressiLegaliModal = {
      callbacks,
      dataModal,
    };
    // Chiamo la funzione di apertura della modale
    this._tassiDiInteresseModalService.openModificaInteressiLegaliModal(config);
  }

  /**
   * Funzione collegata all'evento di modifica riga della tabella per gli interessi di Mora.
   * @param row RiscaTableDataConfig<InteressiDiMoraVo> con le informazioni della riga da modificare.
   */
  onModificaInteressiDiMora(row: RiscaTableDataConfig<InteressiDiMoraVo>) {
    //Resetto eventuali segnalazioni di errore su interessi legali
    this.resetAlertConfigs(this.alertInteressiLegaliConfigs);
    //Resetto eventuali segnalazioni di errore su interessi di mora
    this.resetAlertConfigs(this.alertInteressiMoraConfigs);
    // Recupero il dato originale dalla riga
    const interessiDiMora: InteressiDiMoraVo = row?.original;
    // Definisco le informazioni per i dati da passare alla modale
    //readonly: false poichè stiamo aprendo la modale per la modifica
    const dataModal: IInteressiDiMoraFormDataModal = {
      interessiDiMora,
      readOnly: false,
    };
    // Definisco le chiamate per le callback
    const callbacks: ICallbackDataModal = {
      onConfirm: (payload: any) => {
        // Cancello possibili alert aperti con altre segnalazioni
        this.resetAlertConfigs();
        // Lancio la ricerca con paginazione per il nuovo dato aggiornato
        this.cercaInteressiDiMoraByPaginazioneCorrente();
        // Recupero il codice del messaggio per avvenuto inserimento 
        const code = RiscaNotifyCodes.P002;
        // Visualizzo il messeggio di avvenuta cancellazione del dato
        this.alertInteressiMoraConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
      },
    };
    // Definisco la configurazione per l'apertura della modale
    const config: IApriDettaglioInteressiMoraModal = {
      callbacks,
      dataModal,
    };
    // Chiamo la funzione di apertura della modale
    this._tassiDiInteresseModalService.openModificaInteressiMoraModal(config);
  }

  /**
   * Funzione collegata all'evento di cancellazione riga della tabella per gli interessi legali.
   * @param row RiscaTableDataConfig<InteressiLegaliVo> con le informazioni della riga dal cancellare.
   */
  onCancellaInteressiLegali(row: RiscaTableDataConfig<InteressiLegaliVo>) {
    //Resetto eventuali segnalazioni di errore su interessi legali
    this.resetAlertConfigs(this.alertInteressiLegaliConfigs);
    //Resetto eventuali segnalazioni di errore su interessi di mora
    this.resetAlertConfigs(this.alertInteressiMoraConfigs);
    // Estraggo dalla riga le informazioni effettive per gli interessi legali
    const interessiLegali: InteressiLegaliVo = row?.original;

    // Definisco le informazioni per aprire la modale
    const code: string = RiscaNotifyCodes.P002;
    const callbacks: ICallbackDataModal = {
      onConfirm: () => {
        // Cancello possibili alert aperti con altre segnalazioni
        this.resetAlertConfigs();
        // Lancio la cancellazione dell'interesse legale
        this.deleteInteressiLegali(interessiLegali);
      },
    };

    // Apro la modale e chiedo conferma di cancellazione
    this._riscaModal.apriModalConfermWithCodeAndPayloads(code, callbacks);
  }
  /**
   * Funzione che tenta di cancellare le informazioni relative a degli interessi legali.
   * @param interessiLegali InteressiLegaliVo con il dato da cancellare.
   */
  private deleteInteressiLegali(interessiLegali: InteressiLegaliVo) {
    // Verifico l'input
    if (!interessiLegali) {
      // Manca la configurazione
      return;
    }

    // Richiamo il servizio per la delete dell'oggetto degli interessi
    this._interessiLegaliService.deleteInteressiLegali(interessiLegali).subscribe({
      next: (interessiLegaliDel: InteressiLegaliVo) => {
        // Lancio l'aggiornamento dai dati tramite ricerca
        this.cercaInteressiLegaliByPaginazioneCorrente();
        // Recupero il codice del messaggio per avvenuta cancellazione
        const code = RiscaNotifyCodes.P002;
        // Visualizzo il messeggio di avvenuta cancellazione del dato
        this.alertInteressiLegaliConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione degli errori
        this.onServiziError(e);
        // #
      },
    });
  }

  /**
   * Funzione collegata all'evento di cancellazione riga della tabella per gli interessi di mora.
   * @param row RiscaTableDataConfig<InteressiDiMoraVo> con le informazioni della riga dal cancellare.
   */
  onCancellaInteressiDiMora(row: RiscaTableDataConfig<InteressiDiMoraVo>) {
    //Resetto eventuali segnalazioni di errore su interessi legali
    this.resetAlertConfigs(this.alertInteressiLegaliConfigs);
    //Resetto eventuali segnalazioni di errore su interessi di mora
    this.resetAlertConfigs(this.alertInteressiMoraConfigs);
    // Estraggo dalla riga le informazioni effettive per gli interessi legali
    const interessiDiMora: InteressiDiMoraVo = row?.original;

    // Definisco le informazioni per aprire la modale
    const code: string = RiscaNotifyCodes.A002;
    const callbacks: ICallbackDataModal = {
      onConfirm: () => {
        // Cancello possibili alert aperti con altre segnalazioni
        this.resetAlertConfigs();
        // Lancio la cancellazione dell'interesse legale
        this.deleteInteressiDiMora(interessiDiMora);
      },
    };

    // Apro la modale e chiedo conferma di cancellazione
    this._riscaModal.apriModalConfermWithCodeAndPayloads(code, callbacks);
  }
  /**
   * Funzione che tenta di cancellare le informazioni relative a degli interessi di mora.
   * @param interessiDiMora InteressiDiMoraVo con il dato da cancellare.
   */
  private deleteInteressiDiMora(interessiDiMora: InteressiDiMoraVo) {
    // Verifico l'input
    if (!interessiDiMora) {
      // Manca la configurazione
      return;
    }

    // Richiamo il servizio per la delete dell'oggetto degli interessi
    this._interessiDiMora.deleteInteressiDiMora(interessiDiMora).subscribe({
      next: (interessiDiMoraDel: InteressiDiMoraVo) => {
        // Lancio l'aggiornamento dei dati per la tabella
        this.cercaInteressiDiMoraByPaginazioneCorrente();
        // Recupero il codice del messaggio per avvenuta cancellazione
        const code = RiscaNotifyCodes.P002;
        // Visualizzo il messeggio di avvenuta cancellazione del dato
        this.alertInteressiMoraConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione degli errori
        this.onServiziError(e);
        // #
      },
    });
  }

  /**
   * ###################
   * FUNZIONI DI RICERCA
   * ###################
   */

  /**
   * Funzione che effettua la ricerca degli interessi legali usando la paginazione attualmente attiva sulla tabella degli interessi legali.
   */
  private cercaInteressiLegaliByPaginazioneCorrente() {
    // Verifico se l'oggetto della tabella è già istanziato
    this.setupTabellaInteressiLegali();

    // L'oggetto è stato modificato, recupero la paginazione attuale
    let p: RiscaTablePagination;
    p = this.interessiLegaliTable.getPaginazione();
    // Aggiorno la tabella scaricando i dati
    this.cercaInteressiLegali(p);
  }

  /**
   * Funzione che effettua la ricerca degli interessi legali.
   * @param paginazione RiscaTablePagination con le informazioni di paginazione per la ricerca.
   */
  private cercaInteressiLegali(pagination?: RiscaTablePagination) {
    // Verifico se esiste la paginazione, altrimenti recupero quella di default dalle costanti della tabella
    pagination = pagination ?? interessiLegaliTablePagination();

    // Effettuo la chiamata di ricerca per i dati
    this._interessiLegaliService.getInteressiLegaliPaginated(pagination).subscribe({
      next: (ricerca: RicercaPaginataResponse<InteressiLegaliVo[]>) => {
        // Lancio l'aggiornamento dei dati per la tabella degli interessi legali
        this.updateTabInteressiLegali(ricerca);
        // #
      },
      error: (e?: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione che effettua la ricerca degli interessi legali usando la paginazione attualmente attiva sulla tabella degli interessi di mora.
   */
  private cercaInteressiDiMoraByPaginazioneCorrente() {
    // Verifico se l'oggetto della tabella è già istanziato
    this.setupTabellaInteressiDiMora();

    // L'oggetto è stato modificato, recupero la paginazione attuale
    let p: RiscaTablePagination;
    p = this.interessiDiMoraTable.getPaginazione();
    // Aggiorno la tabella scaricando i dati
    this.cercaInteressiDiMora(p);
  }

  /**
   * Funzione che effettua la ricerca degli interessi di mora.
   * @param paginazione RiscaTablePagination con le informazioni di paginazione per la ricerca.
   */
  private cercaInteressiDiMora(pagination?: RiscaTablePagination) {
    // Verifico se esiste la paginazione, altrimenti recupero quella di default dalle costanti della tabella
    pagination = pagination ?? interessiDiMoraTablePagination();

    // Effettuo la chiamata di ricerca per i dati
    this._interessiDiMora.getInteressiDiMoraPaginated(pagination).subscribe({
      next: (ricerca: RicercaPaginataResponse<InteressiDiMoraVo[]>) => {
        // Lancio l'aggiornamento della tabella per gli interessi di mora
        this.updateTabInteressiDiMora(ricerca);
        // #
      },
      error: (e?: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * INTERESSI LEGALI
   * Funzione che aggiorna i dati della tabella partendo da un oggetto di ricerca dati.
   * @param ricerca RicercaPaginataResponse<InteressiLegaliVo[]> con i dati di ricerca da impostare.
   */
  private updateTabInteressiLegali(
    ricerca: RicercaPaginataResponse<InteressiLegaliVo[]>
  ) {
    // Verifico l'input
    if (!ricerca) {
      // Blocco il flusso
      return;
    }

    // Recupero dall'input le informazioni per generare la tabella
    let sources: InteressiLegaliVo[];
    sources = ricerca.sources ?? [];
    let paginazione: RiscaTablePagination;
    paginazione = ricerca.paging;

    // Verifico se non esiste la configurazione della tabella
    if (!this.interessiLegaliTable) {
      // Creo la configurazione iniziale della tabella passando le informazioni della ricerca
      let tableConfig: IInteressiLegaliTableConfigs;
      tableConfig = {
        interessiLegali: sources,
        paginazione,
        AEADisabled: this.AEA_TDIDisabled,
      };
      // Creo la tabella con la configurazione
      this.interessiLegaliTable = new InteressiLegaliTable(tableConfig);
      // #
    } else {
      // Esiste già l'oggetto, aggiorno le informazioni
      this.interessiLegaliTable.setNewElements(sources);
      this.interessiLegaliTable.updatePaginazioneAfterSearch(paginazione);
      // #
    }
  }

  /**
   * INTERESSI DI MORA
   * Funzione che aggiorna i dati della tabella partendo da un oggetto di ricerca dati.
   * @param ricerca RicercaPaginataResponse<InteressiLegaliVo[]> con i dati di ricerca da impostare.
   */
  private updateTabInteressiDiMora(
    ricerca: RicercaPaginataResponse<InteressiDiMoraVo[]>
  ) {
    // Verifico l'input
    if (!ricerca) {
      // Blocco il flusso
      return;
    }

    // Recupero dall'input le informazioni per generare la tabella
    let sources: InteressiDiMoraVo[];
    sources = ricerca.sources ?? [];
    let paginazione: RiscaTablePagination;
    paginazione = ricerca.paging;

    // Verifico se non esiste la configurazione della tabella
    if (!this.interessiDiMoraTable) {
      // Creo la configurazione iniziale della tabella passando le informazioni della ricerca
      let tableConfig: IInteressiDiMoraTableConfigs;
      tableConfig = {
        interessiDiMora: sources,
        paginazione,
        AEADisabled: this.AEA_TDIDisabled,
      };
      // Creo la tabella con la configurazione
      this.interessiDiMoraTable = new InteressiDiMoraTable(tableConfig);
      // #
    } else {
      // Esiste già l'oggetto, aggiorno le informazioni
      this.interessiDiMoraTable.setNewElements(sources);
      this.interessiDiMoraTable.updatePaginazioneAfterSearch(paginazione);
    }
  }

  /**
   * Funzione collegata al cambio pagina della tabella: interessi legali.
   * La funzione attiverà una nuova ricerca con i nuovi parametri di paginazione.
   * @param paginazione RiscaTablePagination con le nuove configurazioni di paginazione per la ricerca dati.
   */
  onCambioPaginaInteressiLegali(paginazione: RiscaTablePagination) {
    // Effettuo la chiamata alla ricerca, passando i parametri
    this.cercaInteressiLegali(paginazione);
  }

  /**
   * Funzione collegata al cambio pagina della tabella: interessi legali.
   * La funzione attiverà una nuova ricerca con i nuovi parametri di paginazione.
   * @param paginazione RiscaTablePagination con le nuove configurazioni di paginazione per la ricerca dati.
   */
  onCambioPaginaInteressiDiMora(paginazione: RiscaTablePagination) {
    // Effettuo la chiamata alla ricerca, passando i parametri
    this.cercaInteressiDiMora(paginazione);
  }

  /**
 * ###############
 * FUNZIONI DI UTILITY
 * ###############
 */

  /**
   * Funzione di comodo che imposta il messaggio d'errore
   * @param error RiscaServerError che definisce il corpo dell'errore.
   * @param messageCode string che definisce il messaggio d'errore da visualizzare in testata. Default è 'E005'.
   * @param otherMessages Array di string contenente un array con altri messaggi di dettaglio per l'errore. Default è [].
   */
  protected onServiziErrorInteressiLegali(
    error: RiscaServerError,
    messageCode = RiscaNotifyCodes.E005,
    otherMessages: string[] = []
  ) {
    // Definisco un array di messaggi d'errore
    let erroriValidazione = this._riscaAlert.messagesFromServerError(
      error,
      messageCode,
      otherMessages
    );

    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(
      this.alertInteressiLegaliConfigs,
      erroriValidazione,
      RiscaInfoLevels.danger
    );
  }

  /**
* Funzione di comodo che imposta il messaggio d'errore
* @param error RiscaServerError che definisce il corpo dell'errore.
* @param messageCode string che definisce il messaggio d'errore da visualizzare in testata. Default è 'E005'.
* @param otherMessages Array di string contenente un array con altri messaggi di dettaglio per l'errore. Default è [].
*/
  protected onServiziErrorInteressiDiMora(
    error: RiscaServerError,
    messageCode = RiscaNotifyCodes.E005,
    otherMessages: string[] = []
  ) {
    // Definisco un array di messaggi d'errore
    let erroriValidazione = this._riscaAlert.messagesFromServerError(
      error,
      messageCode,
      otherMessages
    );

    // Aggiorno la lista di errori
    this.aggiornaAlertConfigs(
      this.alertInteressiMoraConfigs,
      erroriValidazione,
      RiscaInfoLevels.danger
    );
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che restitusce un oggetto IJourneySnapshot per generare una snapshot del componente.
   * @returns IJourneySnapshot con la configurazione del componente.
   */
  get snapshotConfigs(): IJourneySnapshot {
    return {
      source: this,
      mapping: this.TDI_C.SNAPSHOT_CONFIG.mapping,
      saveFunc: this.TDI_C.SNAPSHOT_CONFIG.saveFunc,
    };
  }

  /**
   * Getter che verifica l'esistenza delle informazioni per la tabella omonima.
   * @returns boolean con il risultato del check.
   */
  get existInteressiLegali(): boolean {
    // Verifico se esitono elementi nella tabella
    return this.interessiLegaliTable?.source?.length > 0;
  }

  /**
   * Getter che verifica l'esistenza delle informazioni per la tabella omonima.
   * @returns boolean con il risultato del check.
   */
  get existInteressiDiMora(): boolean {
    // Verifico se esitono elementi nella tabella
    return this.interessiDiMoraTable?.source?.length > 0;
  }

  /**
   * Getter che ritorna la modalità di gestione del componente.
   * @returns AppActions con la modalità di gestione.
   */
  get modalitaInserimento(): AppActions {
    // Ritorno la modalita
    return AppActions.inserimento;
  }

  /**
 * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
 */
  get alertInteressiLegaliConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertInteressiLegaliConfigs);
  }

  /**
* Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
*/
  get alertInteressiMoraConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertInteressiMoraConfigs);
  }
}
