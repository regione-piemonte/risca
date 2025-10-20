import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { cloneDeep } from 'lodash';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { RegolaUsoVo } from '../../../../core/commons/vo/regola-uso-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { IJourneySnapshot } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import {
  RegoleUsiTable,
  RegoleUsiTableConfigs,
} from '../../../../shared/classes/risca-table/regole-usi/regole-usi.table';
import { RegoleUsiTableConsts } from '../../../../shared/classes/risca-table/regole-usi/utilities/regole-usi.consts';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import {
  ICallbackDataModal,
  IRiscaAnnoSelect,
  IRiscaTablePagination,
  RiscaServerError,
  RiscaTablePagination,
  TRiscaDataPlaceholders,
} from '../../../../shared/utilities';
import { RiscaNotifyPlaceholders } from '../../../../shared/utilities/consts/risca-notify-placeholders.consts';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IAlertConfigsFromCode } from '../../../../shared/utilities/interfaces/risca/risca-alert/risca-alert.interface';
import {
  ICreaRegolaUsoModal,
  ICreaRegolaUsoModalConfirm,
} from '../../modals/crea-regola-uso-modal/utilities/crea-regola-uso-modal.interfaces';
import {
  IDettaglioRegolaUsoModal,
  IDettaglioRegolaUsoModalConfirm,
} from '../../modals/dettaglio-regola-uso-modal/utilities/dettaglio-regola-uso-modal.interfaces';
import { CanoniModalService } from '../../services/canoni/canoni-modal.service';
import { CanoniService } from '../../services/canoni/canoni.service';
import {
  IApriDettaglioRegolaUsoModal,
  ICreaNuovaRegolaModal,
  IPutListaRegoleParams,
  ISalvaModificheRegoleModal,
} from '../../services/canoni/utilities/canoni.interfaces';
import { IRicercaUsiRegola } from '../../services/configurazioni/utilities/configurazioni.interfaces';
import { CanoniFormComponent } from '../canoni-form/canoni-form.component';
import { ICanoniFormData } from '../canoni-form/utilities/canoni-form.interfaces';
import { ICreaRegolaUsoForm } from '../crea-regola-uso-form/utilities/crea-regola-uso-form.interfaces';
import { CanoniConsts } from './utilities/canoni.consts';
import {
  IOpenCreaRegolaAnnualita,
  IOpenDettaglioRegola,
  IOpenSalvaModificheRegole,
} from './utilities/canoni.interfaces';

@Component({
  selector: 'canoni',
  templateUrl: './canoni.component.html',
  styleUrls: ['./canoni.component.scss'],
})
export class CanoniComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti del componente di riferimento. */
  CAN_C = new CanoniConsts();
  /** RegoleUsiTableConsts con le costanti per la struttura omonima. */
  private RUT_C = new RegoleUsiTableConsts();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** ViewChild con la referenza al componente: CanoniFormComponent. */
  @ViewChild('canoniForm') canoniForm: CanoniFormComponent;

  /** Boolean che definisce l'abilitazione di tutte le componenti della pagina. */
  AEA_pagina_canoni_disabled: boolean;
  /** Boolean che definisce l'abilitazione del pulsante di creazione dell'annualità con regole. */
  AEA_crea_annualita_disabled: boolean;

  /** RegoleUsiTable con l'oggetto di configurazione per la tabella dati. */
  regolaUsiTable: RegoleUsiTable;

  /** ICanoniFormData contenente le informazioni per l'ultima ricerca effettuata. */
  ultimiFiltri: ICanoniFormData;
  /** RicercaPaginataResponse<RegolaUsoVo[]> con le informazioni dell'ultima ricerca completata. */
  ultimaRicerca: RicercaPaginataResponse<RegolaUsoVo[]>;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _canoni: CanoniService,
    private _canoniModal: CanoniModalService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlertService: RiscaAlertService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaMessageService: RiscaMessagesService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _user: UserService
  ) {
    super(
      logger,
      navigationHelper,
      riscaAlertService,
      riscaFormSubmitHandler,
      riscaMessageService
    );
    this.stepConfig = this.CAN_C.NAVIGATION_CONFIG;

    // Lancio la funzione di setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Richiamo l'init per i dati possibilmante salvati come snapshot
    this.initJSnapshot();
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
   * Funzione iniziale di setup per le informazioni del componente.
   */
  private setupComponente() {
    // Richiamo l'inizializzazione delle chiavi per l'accesso agli elementi
    this.setupAEADisabled();
  }

  /**
   * Funzione di setup per l'inizializzazione dei flag di abilitazione agli elementi.
   */
  private setupAEADisabled() {
    // Recupero le chiavi per la verifica sulla disabilitazione della nav
    const PC_KEY = this.AEAK_C.PAGINA_CANONI;
    const CD_KEY = this.AEAK_C.CREA_ANNUALITA;
    // Definisco disabled per le nav
    this.AEA_pagina_canoni_disabled =
      this._accessoElementiApp.isAEADisabled(PC_KEY);
    this.AEA_crea_annualita_disabled =
      this._accessoElementiApp.isAEADisabled(CD_KEY);
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di init per i dati della snapshot salvata in journey.
   */
  private initJSnapshot() {
    // // Recupero i parametri dallo state della route
    // const state = this._navigationHelper.getRouterState(this._router);
    // Carico i dati della snapshot
    this.loadJSnapshot(this);
  }

  /**
   * #################
   * FUNZIONI DEL FORM
   * #################
   */

  /**
   * Funzione collegata all'evento di submit corretto da parte del form dei canoni.
   * @param data ICanoniFormData con le informazioni generate dalla form.
   */
  onCanoniFormSubmit(data: ICanoniFormData) {
    // Verifico l'input
    if (!data) {
      // Manca la configurazione
      return;
    }

    // Aggiorno i dati per l'ultima ricerca effettuata
    this.ultimiFiltri = data;

    // Una volta salvate le informazioni localmente le informazioni, genero quelle per la ricerca
    let pathParams: IRicercaUsiRegola;
    pathParams = this.pathParamsFromComponent();

    // Richiamo la funzione di ricerca
    this.cercaRegoleUsi(pathParams);
  }

  /**
   * Funzione collegata all'evento di errori generati dal form dei canoni.
   * @param errors string[] con i messaggi d'errore generati.
   */
  onCanoniFormError(errors: string[]) {
    // Gestione dell'errore del form
  }

  /**
   * ###################
   * FUNZIONI DI RICERCA
   * ###################
   */

  /**
   * Funzione che effettua la ricerca delle regole per gli usi di legge dato un anno in input.
   * @param pathParams IRicercaUsiRegola con le informazioni per la ricerca dati.
   * @param paginazione RiscaTablePagination con le informazioni di paginazione per la ricerca.
   */
  private cercaRegoleUsi(
    pathParams: IRicercaUsiRegola,
    pagination?: RiscaTablePagination
  ) {
    // Verifico l'input
    if (!pathParams?.anno) {
      // Manca la definizione dell'anno
      return;
    }

    // Lancio il reset dell'alert
    this.resetAlertConfigs();

    // Verifico se esiste la paginazione, altrimenti recupero quella di default dalle costanti della tabella
    pagination = pagination ?? this.RUT_C.DEFAULT_PAGINATION;

    // Effettuo la chiamata di ricerca per le regole uso
    this._canoni
      .getListaRegolePaginated(pathParams, pagination)
      .pipe(
        map((ricerca: RicercaPaginataResponse<RegolaUsoVo[]>) => {
          // Per l'oggetto di ricerca, vado ad attivare la funzione della classe RegolaUsoVo per la gestione degli importi
          this.prepareRegolaUsoVoToTable(ricerca);
          // Ritorno la lista aggiornata
          return ricerca;
          // #
        })
      )
      .subscribe({
        next: (ricerca: RicercaPaginataResponse<RegolaUsoVo[]>) => {
          // Salvo localmente una copia della ricerca, per poter ripristinare i dati
          this.ultimaRicerca = cloneDeep(ricerca);
          // Richiamo l'aggiornamento dei dati di tabella
          this.updateTabRegoleUsi(ricerca);
          // #
        },
        error: (e?: RiscaServerError) => {
          // Gestisco l'errore
          this.onServiziError(e);
        },
      });
  }

  /**
   * ############################
   * FUNZIONI DI GESTIONE TABELLA
   * ############################
   */

  /**
   * Funzione di preparazione dei dati per la visualizzazione in tabella.
   * Per ogni oggetto RegolaUsoVo viene lanciata la funzione di preparazione alla tabella di FE.
   * La modifica avviene per riferimento.
   * @param ricerca RicercaPaginataResponse<RegolaUsoVo[]> con le informazioni generate dalla ricerca dati.
   */
  private prepareRegolaUsoVoToTable(
    ricerca: RicercaPaginataResponse<RegolaUsoVo[]>
  ) {
    // Per l'oggetto di ricerca, vado ad attivare la funzione della classe RegolaUsoVo per la gestione degli importi
    ricerca.sources?.forEach((regolaUso: RegolaUsoVo) => {
      // Lancio la funzione di gestione dei canoni per l'utilizzo in tabella
      regolaUso.prepareRegolaUsoVoToTable();
      // #
    });
  }

  /**
   * Funzione che aggiorna i dati della tabella partendo da un oggetto di ricerca dati.
   * @param ricercaPratiche RicercaPaginataResponse<RegolaUsoVo[]> con i dati di ricerca da impostare.
   */
  updateTabRegoleUsi(ricercaPratiche: RicercaPaginataResponse<RegolaUsoVo[]>) {
    // Verifico l'input
    if (!ricercaPratiche) {
      // Blocco il flusso
      return;
    }

    // Recupero dall'input le informazioni per generare la tabella
    let sources: RegolaUsoVo[];
    sources = ricercaPratiche.sources ?? [];
    let paginazione: RiscaTablePagination;
    paginazione = ricercaPratiche.paging;
    // Definisco parametri esterni la configurazione per la costruzione della tabella
    let riscaFormBuilder: RiscaFormBuilderService;
    riscaFormBuilder = this._riscaFormBuilder;
    // Recupero la configurazione di abilitazioni elementi app
    const AEA_pagina_canoni_disabled = this.AEA_pagina_canoni_disabled;

    // Verifico se non esiste la configurazione della tabella
    if (!this.regolaUsiTable) {
      // Creo la configurazione iniziale della tabella passando le informazioni della ricerca
      let tableConfig: RegoleUsiTableConfigs;
      tableConfig = {
        regoleUsi: sources,
        paginazione,
        AEA_pagina_canoni_disabled,
      };
      // Creo la tabella con la configurazione
      this.regolaUsiTable = new RegoleUsiTable(tableConfig, riscaFormBuilder);
      // #
    } else {
      // Esiste già l'oggetto, aggiorno le informazioni
      this.regolaUsiTable.setNewElements(sources);
      this.regolaUsiTable.updatePaginazioneAfterSearch(paginazione);
      // #
    }
  }

  /**
   * Funzione che resetta i dati della tabella partendo da un oggetto di ricerca dati.
   */
  resetTabRegoleUsi() {
    // Recupero dal componente l'ultima ricerca effettuata
    let ultimaRicerca: RicercaPaginataResponse<RegolaUsoVo[]>;
    ultimaRicerca = this.ultimaRicerca;

    // Verifico se esistono effettivamente i dati di ricerca
    if (ultimaRicerca) {
      // Resetto le proprietà di FE con le informazioni delle regole
      ultimaRicerca.sources = ultimaRicerca.sources.map((r: RegolaUsoVo) => {
        // Lancio la funzione per assegnare i valori della regola alle proprietà FE
        r.prepareRegolaUsoVoToTable();
        // Ritorno l'oggetto modificato
        return r;
      });

      // Aggiorno la tabella con i dati dell'ultima ricerca
      this.updateTabRegoleUsi(ultimaRicerca);
    }
  }

  /**
   * Funzione collegata al "cambio pagina" o "nuovo ordinamento delle colonne" della tabella.
   * La funzione attiverà una nuova ricerca con i nuovi parametri di paginazione.
   * @param paginazione RiscaTablePagination con le nuove configurazioni di paginazione per la ricerca dati.
   */
  onCambioPagina(paginazione: RiscaTablePagination) {
    // Genero le informazioni per la ricerca
    let pathParams: IRicercaUsiRegola;
    pathParams = this.pathParamsFromComponent();
    // Verifico se esistono regole modificate e non salvate dall'utente
    let regoleMod: RegolaUsoVo[];
    regoleMod = this.canoniRegoleModificati();

    // Verifico se esistono regole modificate
    if (regoleMod.length > 0) {
      // Almeno una regola è stata modificata, chiedo conferma
      this.salvaRegoleECambioPagina(regoleMod, pathParams, paginazione);
      // #
    } else {
      // Effettuo la chiamata alla ricerca, passando i parametri
      this.cercaRegoleUsi(pathParams, paginazione);
    }
  }

  /**
   * Funzione di gestione della richiesta di salvataggio per le regole modificate.
   * La funzione attiverà il prompt di richiesta, e poi gestirà il flusso per: cambio pagina tabella regole.
   * @param regoleMod RegolaUsoVo[] con la lista di regole che risultano modificate dall'utente.
   * @param pathParams IRicercaUsiRegola con le informazioni per la ricerca dati.
   * @param paginazione RiscaTablePagination con le informazioni di paginazione per la ricerca.
   */
  private salvaRegoleECambioPagina(
    regoleMod: RegolaUsoVo[],
    pathParams: IRicercaUsiRegola,
    paginazione?: RiscaTablePagination
  ) {
    // Definisco le configurazioni per la gestione della modale
    let regoleModificate: RegolaUsoVo[];
    regoleModificate = regoleMod ?? [];
    // Definisco le logiche da attivare una volta completato il flusso della modale
    let onComplete: (...args: any) => any;
    onComplete = () => {
      // Proseguo con le logiche utente
      this.cercaRegoleUsi(pathParams, paginazione);
    };

    // Definisco la variabile per la configurazione della modale
    let config: IOpenSalvaModificheRegole;
    config = { regoleModificate, onComplete, pathParams, paginazione };

    // Lancio la funzione di gestione del flusso dati
    this.openSalvaModificheRegoleModal(config);
  }

  /**
   * Funzione collegata al click del dettaglio per una riga della tabella.
   * @param row RiscaTableDataConfig<RegolaUsoVo> con i dati della riga di dettaglio premuta.
   */
  onDettaglioRegolaUso(row: RiscaTableDataConfig<RegolaUsoVo>) {
    // Verifico se esistono regole modificate e non salvate dall'utente
    let regoleMod: RegolaUsoVo[];
    regoleMod = this.canoniRegoleModificati();

    // Verifico se esistono regole modificate
    if (regoleMod.length > 0) {
      // Almeno una regola è stata modificata, chiedo conferma
      this.salvaRegoleEApriDettaglioRegolaUso(regoleMod, row);
      // #
    } else {
      // Lancio la funzione per l'apertura del dettaglio
      this.apriDettaglioRegolaUso(row);
    }
  }

  /**
   * Funzione di gestione della richiesta di salvataggio per le regole modificate.
   * La funzione attiverà il prompt di richiesta, e poi gestirà il flusso per: apertura dettaglio regola.
   * @param regoleMod RegolaUsoVo[] con la lista di regole che risultano modificate dall'utente.
   * @param row RiscaTableDataConfig<RegolaUsoVo> con i dati della riga di dettaglio premuta.
   */
  private salvaRegoleEApriDettaglioRegolaUso(
    regoleMod: RegolaUsoVo[],
    row: RiscaTableDataConfig<RegolaUsoVo>
  ) {
    // Definisco le configurazioni per la gestione della modale
    let regoleModificate: RegolaUsoVo[];
    regoleModificate = regoleMod ?? [];
    // Definisco le logiche da attivare una volta completato il flusso della modale
    let onComplete: (...args: any) => any;
    onComplete = (ricerca: RicercaPaginataResponse<RegolaUsoVo[]>) => {
      // Proseguo con le logiche utente
      this.apriDettaglioRegolaUso(row, ricerca);
    };

    // Recupero le informazioni per la ricerca
    let pathParams: IRicercaUsiRegola;
    pathParams = this.pathParamsFromComponent();
    let paginazione: IRiscaTablePagination;
    paginazione = this.ultimaRicerca?.paging;

    // Definisco la variabile per la configurazione della modale
    let config: IOpenSalvaModificheRegole;
    config = { regoleModificate, onComplete, pathParams, paginazione };

    // Lancio la funzione di gestione del flusso dati
    this.openSalvaModificheRegoleModal(config);
  }

  /**
   * Funzione predisposta all'apertura del dettaglio di una regola uso.
   * Se in input viene passato l'oggetto di ricerca, significa che sono stati salvati i dati. Se i dati sono stati salvati, allora la funzione estrarrà la regola aggiornata.
   * @param row RiscaTableDataConfig<RegolaUsoVo> con i dati della riga di dettaglio premuta.
   * @param ricerca RicercaPaginataResponse<RegolaUsoVo[]> con le informazioni aggiornate a seguito del salvataggio pre-apertura dettaglio.
   */
  private apriDettaglioRegolaUso(
    row: RiscaTableDataConfig<RegolaUsoVo>,
    ricerca?: RicercaPaginataResponse<RegolaUsoVo[]>
  ) {
    // Definisco una variabile contenitore per la regola da aggiornare
    let regola: RegolaUsoVo;

    // Verifico se è stato passato un oggetto "ricerca"
    if (ricerca) {
      // Estraggo dalla ricerca la lista di regole
      const regole: RegolaUsoVo[] = ricerca?.sources;
      // Estraggo dalla riga l'oggetto della regola
      const regolaRow: RegolaUsoVo = row.original;
      // Assegno alla variabile regola, da modificare, la regola aggiornata
      regola = regole.find((regolaUpd: RegolaUsoVo) => {
        // Recupero i codici dei tipi uso per la comparazione
        const codTUUpd: string = regolaUpd?.tipo_uso?.cod_tipo_uso;
        const codTURow: string = regolaRow?.tipo_uso?.cod_tipo_uso;
        // Confronto il l'uso di legge
        return codTUUpd === codTURow;
      });
      // #
    } else {
      // Estraggo dalla riga l'oggetto della regola
      regola = row.original;
    }

    // Definisco l'oggetto di configurazione da passare per aprire la modale
    let config: IOpenDettaglioRegola;
    config = { row, regola };

    // Richiamo la funzione di apertura della modale
    this.openDettaglioRegola(config);
  }

  /**
   * #####################################
   * FUNZIONI CREA NUOVA REGOLA ANNUALITA'
   * #####################################
   */

  /**
   * Funzione collegata al pulsante di creazione nuova annualità.
   * La funzione verificherà se ci sono dati in pagina, se sì, verificherà se sono stati modificati.
   * Qualora fossero modificate, chiederà all'utente se vuole salvare i dati prima di creare una nuova annualità.
   */
  creaRegolaAnnualita() {
    // Verifico se esistono regole modificate e non salvate dall'utente
    let regoleMod: RegolaUsoVo[];
    regoleMod = this.canoniRegoleModificati();

    // Verifico se esistono regole modificate
    if (regoleMod.length > 0) {
      // Almeno una regola è stata modificata, chiedo conferma
      this.salvaRegoleECreaRegolaAnnualita(regoleMod);
      // #
    } else {
      // Non esistono dati, apro la modale
      this.apriCreaRegolaAnnualita();
    }
  }

  /**
   * Funzione di gestione della richiesta di salvataggio per le regole modificate.
   * La funzione attiverà il prompt di richiesta, e poi gestirà il flusso per: creare regola annualità.
   * @param regoleMod RegolaUsoVo[] con la lista di regole che risultano modificate dall'utente.
   */
  private salvaRegoleECreaRegolaAnnualita(regoleMod: RegolaUsoVo[]) {
    // Definisco le configurazioni per la gestione della modale
    let regoleModificate: RegolaUsoVo[];
    regoleModificate = regoleMod ?? [];
    // Definisco le logiche da attivare una volta completato il flusso della modale
    let onComplete: (...args: any) => any;
    onComplete = () => {
      // Proseguo con le logiche utente
      this.apriCreaRegolaAnnualita();
    };

    // Recupero le informazioni per la ricerca
    let pathParams: IRicercaUsiRegola;
    pathParams = this.pathParamsFromComponent();
    let paginazione: IRiscaTablePagination;
    paginazione = this.ultimaRicerca?.paging;

    // Definisco la variabile per la configurazione della modale
    let config: IOpenSalvaModificheRegole;
    config = { regoleModificate, onComplete, pathParams, paginazione };

    // Lancio la funzione di gestione del flusso dati
    this.openSalvaModificheRegoleModal(config);
  }

  /**
   * Funzione predisposta all'apertura della modale per la creazione di una nuova annualità.
   */
  private apriCreaRegolaAnnualita() {
    // Recupero la lista per i canoni dalla componente della form
    let anniCanoni: IRiscaAnnoSelect[];
    anniCanoni = this.canoniForm?.listaCanoniUfficiali ?? [];

    // Dalla lista, recupero il primo elemento che è anche il default del servizio e aggiungo 1 anno
    let defaultAnnoCanoni: number;
    defaultAnnoCanoni = anniCanoni[0]?.anno;
    let anno: number;
    anno = defaultAnnoCanoni ? defaultAnnoCanoni + 1 : undefined;

    // Definisco l'oggetto di configurazione da passare per aprire la modale
    let config: IOpenCreaRegolaAnnualita;
    config = { anno };

    // Richiamo la funzione di apertura della modale
    this.openCreaRegolaAnnualita(config);
  }

  /**
   * ##############################################
   * FUNZIONI GESTIONE MODALE CREA REGOLA ANNUALITA
   * ##############################################
   */

  /**
   * Funzione di supporto per l'apertura della modale che permetterà la creazione di una nuova regola uso.
   * @param config IOpenCreaRegolaAnnualita con le configurazioni per la modale.
   */
  private openCreaRegolaAnnualita(config: IOpenCreaRegolaAnnualita) {
    // Preparo i dati di configurazione per la modale
    let creaRegolaUsoForm: ICreaRegolaUsoForm;
    creaRegolaUsoForm = { annualita: config?.anno };

    // Definisco la configuraizone con i parametri della modale
    let dataModal: ICreaRegolaUsoModal;
    dataModal = { creaRegolaUsoForm };

    // Definisco le callback per la modale
    let callbacks: ICallbackDataModal;
    callbacks = {
      onConfirm: (payload: ICreaRegolaUsoModalConfirm) => {
        // Richiamo la funzione di gestione della conferma della modale
        this.onConfirmCreaRegolaAnnualita(payload);
        // #
      },
    };

    // Creo i parametri di configurazione da passare al servizio
    let modalConfigs: ICreaNuovaRegolaModal;
    modalConfigs = { dataModal, callbacks };

    // Richiamo la funzione di apertura della modale
    this._canoniModal.openCreaNuovaRegolaModal(modalConfigs);
  }

  /**
   * Funzione invocata nel momento in cui la modale di creazione delle regole di una nuova annualità viene chiusa con successo.
   * @param payload ICreaRegolaUsoModalConfirm con le informazioni risultati dal salvataggio dell'annualità.
   */
  private onConfirmCreaRegolaAnnualita(payload: ICreaRegolaUsoModalConfirm) {
    // Verifico l'input
    if (!payload) {
      // Non ci sono configurazioni
      return;
    }

    // NOTA: per il momento c'è una singola gestione
    // Estraggo dal payload di ritorno le informazioni
    const { anno, code } = payload;
    // Recupero le informazioni per la gestione del messaggio
    let codesPlaceholders: string[];
    codesPlaceholders = RiscaNotifyPlaceholders.I042;
    let dataReplace: TRiscaDataPlaceholders;
    dataReplace = [anno];
    // Definisco la configurazione completa per la generazione dell'alert
    let c: IAlertConfigsFromCode;
    c = { code, codesPlaceholders, dataReplace };
    this.alertConfigs = this._riscaAlert.createAlertFromMsgCode(c);

    // Devo aggiornare la lista degli anni e aggiornare la tabella dei dati, utilizzando la referenza del componente canoni form
    this.canoniForm.aggiornaListaCanoniUfficialiSubmit();
  }

  /**
   * ##############################################
   * FUNZIONI GESTIONE MODALE CREA REGOLA ANNUALITA
   * ##############################################
   */

  /**
   * Funzione di supporto per l'apertura della modale che permetterà la visualizzazione del dettaglio per una regola.
   * @param config IOpenDettaglioRegola con le configurazioni per la modale.
   */
  private openDettaglioRegola(config: IOpenDettaglioRegola) {
    // Estraggo le informazioni dall'oggetto in input
    const regola: RegolaUsoVo = config?.regola;
    // Verifico esista la regola
    if (!regola) {
      // Non c'è la configurazione minima
      return;
    }

    // Definisco la configuraizone con i parametri della modale
    let dataModal: IDettaglioRegolaUsoModal;
    dataModal = { regola };

    // Definisco le callback per la modale
    let callbacks: ICallbackDataModal;
    callbacks = {
      onConfirm: (payload: IDettaglioRegolaUsoModalConfirm) => {
        // Richiamo la funzione di gestione della conferma della modale
        this.onConfirmDettaglioRegola(config, payload);
        // #
      },
    };

    // Creo i parametri di configurazione da passare al servizio
    let modalConfigs: IApriDettaglioRegolaUsoModal;
    modalConfigs = { dataModal, callbacks };

    // Richiamo la funzione di apertura della modale
    this._canoniModal.openDettaglioRegolaUsoModal(modalConfigs);
  }

  /**
   * Funzione invocata nel momento in cui la modale di dettaglio della regola annualità viene chiusa con successo.
   * @param config IOpenDettaglioRegola con le configurazioni per la modale.
   * @param payload IDettaglioRegolaUsoModalConfirm con le informazioni risultati dalla modale di dettaglio regola.
   */
  private onConfirmDettaglioRegola(
    config: IOpenDettaglioRegola,
    payload: IDettaglioRegolaUsoModalConfirm
  ) {
    // Verifico l'input
    if (!config || !payload) {
      // Non ci sono configurazioni
      return;
    }

    // Estraggo dagli oggetti in input le informazioni per aggiornare la tabella
    const { row } = config;
    const { regolaUso } = payload;
    // Aggiorno la riga di tabella
    this.regolaUsiTable.convertAndUpdateElement(regolaUso, row);

    // Visualizzo un messaggio di avvenuto salvataggio
    const code = RiscaNotifyCodes.P001;
    // Genero un nuovo alert
    this.alertConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
    this.alertConfigs.persistentMessage = false;
    this.alertConfigs.timeoutMessage = 5000;
  }

  /**
   * #############################################
   * FUNZIONI GESTIONE MODALE VERFICA/SALVA REGOLE
   * #############################################
   */

  /**
   * Funzione di supporto per l'apertura della modale che chiederà all'utente conferma salvataggio dei canoni delle regole modificate.
   * Se confermato, verranno salvati i dati, altrimenti si proseguirà con la funzione definita dall'utente.
   * La funzione "onConfirm" verrà dichiarata in questa funzione, ma è tuttavia possibile passarla come parametro. Se definita, avrà la priorità su quella di default.
   * @param config IOpenSalvaModificheRegole con le configurazioni per la modale.
   */
  private openSalvaModificheRegoleModal(config: IOpenSalvaModificheRegole) {
    // Verifico l'input
    if (!config.regoleModificate) {
      // Manca la configurazione
      return;
    }

    // Estraggo dall'oggetto di configurazione i parametri
    const { regoleModificate, onComplete, pathParams, paginazione } = config;
    // Definisco i parametri per l'aggiornamento delle regole
    let params: IPutListaRegoleParams;
    params = { regoleModificate, pathParams, paginazione };

    // Definisco localmente le funzioni di fallback
    let onConfirm: (...args: any) => any;
    let onCancel: (...args: any) => any;
    let onClose: (...args: any) => any;
    let onCompleteWithoutSave = () => {
      // Resetto i valori inseriti nella tabella
      this.resetTabRegoleUsi();
      // Lancio la funzione prosecuzione
      onComplete();
      // #
    };
    onConfirm = () => {
      // Lancio la funzione di aggiornamento dei dati
      this.aggiornaRegoleUsiSubscription(params).subscribe({
        next: (ricerca: RicercaPaginataResponse<RegolaUsoVo[]>) => {
          // Proseguo il flusso delle azioni che stava facendo l'utente
          onComplete(ricerca);
          // #
        },
        error: (e: RiscaServerError) => {
          // Gestisco gli errori
          this.onServiziError(e);
          // #
        },
      });
      // #
    };
    // Verifico se la funzione di "onConfirm" necessita di logica custom, altrimenti mantengo quella di default
    onConfirm = config.onConfirm ?? onConfirm;
    // Definisco le altre funzioni di gestione della modale
    onClose = onCompleteWithoutSave;
    onCancel = onCompleteWithoutSave;

    // Definisco l'oggetto di configurazione da passare alla funzione del servizio
    let c: ISalvaModificheRegoleModal;
    c = { regoleModificate, onCancel, onClose, onConfirm };
    // Richiamo la funzione del servizio
    this._canoniModal.openSalvaModificheRegoleModal(c);
  }

  /**
   * ####################################
   * FUNZIONI DI SALVATAGGIO INFORMAZIONI
   * ####################################
   */

  /**
   * Funzione di salvataggio che invoca la PUT dei dati ed esegue l'aggiornamento della tabella regole uso con le informazioni restituite.
   * La funzione è pensata per effettuare un salvataggio diretto dei dati, recuperando le informazioni al loro stato attuale in pagina.
   * @param skipCanoniSynch boolean che permette saltare il passaggio di sincronizzazione delle proprietà degli oggetti RegolaUsoVo. Per default è: false.
   */
  aggiornaRegoleUsiAttuali(skipCanoniSynch: boolean = false) {
    // Recupero le informazioni dalla tabella
    let regole: RegolaUsoVo[];
    regole = this.regolaUsiTable?.getDataSource();
    // Verifico che esistano effettivamente regole
    if (!regole || regole.length == 0) {
      // Non ci sono regole, blocco il flusso
      return;
    }

    // Verifico se ci sono state modifiche alle regole in pagina
    let regoleModificate: RegolaUsoVo[];
    regoleModificate = this.canoniRegoleModificati();
    // Controllo il risultato del check
    if (regoleModificate.length == 0) {
      // Non sono state apportate modifiche, segnalo all'utente la casistica
      const code: string = RiscaNotifyCodes.I015;
      // Genero un alert con il codice
      const c: IAlertConfigsFromCode = { code };
      this.alertConfigs = this._riscaAlert.createAlertFromMsgCode(c);
      this.alertConfigs.allowAlertClose = true;
      // Interrompo il flusso
      return;
    }

    // Recupero le informazioni per il salvataggio/aggiornamento dei dati in tabella
    let pathParams: IRicercaUsiRegola;
    pathParams = this.pathParamsFromComponent();
    let paginazione: IRiscaTablePagination;
    paginazione = this.ultimaRicerca?.paging;

    // Definisco l'oggetto con i parametri per la chiamata al servizio
    let params: IPutListaRegoleParams;
    params = { regoleModificate, pathParams, paginazione };

    // Lancio la funzione di aggiornamento dati
    this.aggiornaRegoleUsiSubscription(params, skipCanoniSynch).subscribe({
      next: (ricerca: RicercaPaginataResponse<RegolaUsoVo[]>) => {
        // Visualizzo un messaggio di success
        this.onAggiornaRegoleUsiAttuali();
      },
      error: (e: RiscaServerError) => {
        // Gestisco eventuali errori
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione invocata nel momento in cui le informazioni delle regole canone sono state salvate.
   */
  private onAggiornaRegoleUsiAttuali() {
    // Recupero il codice per il salvataggio
    const code = RiscaNotifyCodes.P001;
    // Genero un nuovo alert
    this.alertConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
    this.alertConfigs.persistentMessage = false;
    this.alertConfigs.timeoutMessage = 5000;
  }

  /**
   * Funzione di salvataggio che invoca la PUT dei dati ed esegue l'aggiornamento della tabella regole uso con le informazioni restituite.
   * @param params IPutListaRegoleParamscon le informazioni per eseguire la PUT dei dati e l'aggiornamento completo di FE.
   * @param skipCanoniSynch boolean che permette saltare il passaggio di sincronizzazione delle proprietà degli oggetti RegolaUsoVo. Per default è: false.
   * @returns Observable<RicercaPaginataResponse<RegolaUsoVo[]>> con le informazioni aggiornate e con lo scarico paginato delle informazioni.
   */
  private aggiornaRegoleUsiSubscription(
    params: IPutListaRegoleParams,
    skipCanoniSynch: boolean = false
  ): Observable<RicercaPaginataResponse<RegolaUsoVo[]>> {
    // Lancio la funzione di aggiornamento dati
    return this.putListaRegoleWithPaginatedResponse(
      params,
      skipCanoniSynch
    ).pipe(
      tap((ricerca: RicercaPaginataResponse<RegolaUsoVo[]>) => {
        // Per l'oggetto di ricerca, vado ad attivare la funzione della classe RegolaUsoVo per la gestione degli importi
        this.prepareRegolaUsoVoToTable(ricerca);
        // Lancio l'aggiornamento della tabella
        this.updateTabRegoleUsi(ricerca);
        // #
      })
    );
  }

  /**
   * Funzione di PUT dei dati modificati per le regole uso.
   * La funzione prevede la sincronizzazione delle informazioni FE: __json_regola_canone, __json_regola_tipo_canone; con i canoni effettivi della regola.
   * @param params IPutListaRegoleParamscon le informazioni per eseguire la PUT dei dati e l'aggiornamento completo di FE.
   * @param skipCanoniSynch boolean che permette saltare il passaggio di sincronizzazione delle proprietà degli oggetti RegolaUsoVo. Per default è: false.
   * @returns Observable<RicercaPaginataResponse<RegolaUsoVo[]>> con le informazioni aggiornate e con lo scarico paginato delle informazioni.
   */
  private putListaRegoleWithPaginatedResponse(
    params: IPutListaRegoleParams,
    skipCanoniSynch: boolean = false
  ): Observable<RicercaPaginataResponse<RegolaUsoVo[]>> {
    // Verifico se devo saltare la sincronizzazione dei dati di FE degli oggetti RegolaUsoVo[]
    if (!skipCanoniSynch) {
      // Estraggo dall'input le informazioni
      let regoleMod: RegolaUsoVo[];
      regoleMod = params?.regoleModificate ?? [];
      // Per ogni regola, vado a lanciare la funzione di sincronizzazione, con la modifica al riferimento degli oggetti
      regoleMod.forEach((r: RegolaUsoVo) => {
        // Lancio la funzione di sync dati FE
        r?.updateRegolaUsoVoFromTable();
      });
    }

    // Lancio la funzione di aggiornamento dati
    return this._canoni.putListaRegoleWithPaginatedResponse(params);
  }

  /**
   * #####################
   * FUNZIONI DI UTILITIES
   * #####################
   */

  /**
   * Funzione di supporto che verifica se esistono delle regole usi modificate dall'utente e non salvate.
   * @returns RegolaUsoVo[] con la lista di regole modificate. Se non esistono regole, o nessuna è stata modificata, ritorna [].
   */
  canoniRegoleModificati(): RegolaUsoVo[] {
    // Definisco una variabile che conterrà la lista di regole modificate
    let regoleMod: RegolaUsoVo[] = [];

    // Tento di recuperare i dati dalla tabella in pagina
    let regole: RegolaUsoVo[];
    regole = this.regolaUsiTable?.getDataSource();

    // Verifico se esistono dati
    if (regole?.length > 0) {
      // Esistono delle regole, verifico se son state modificate
      regoleMod = this._canoni.canoniRegoleModificati(regole);
      // #
    }

    // Ritorno la lista di regole modificate
    return regoleMod;
  }

  /**
   * Funzione di supporto che recupera, dalle varibili presenti nel componente, le informazioni e genera un oggetto di ricerca.
   * Le informazioni devono essere presenti a livello di componente per poter generare un corretto oggetto di ricerca.
   * @returns IRicercaUsiRegola con l'oggetto di ricerca generato.
   */
  private pathParamsFromComponent(): IRicercaUsiRegola {
    // Recupero dalle variabili del componente l'ultima ricerca fatta
    const ultimaRicerca: ICanoniFormData = this.ultimiFiltri;
    // Recupero le informazioni di ricerca
    const anno: number = ultimaRicerca?.canoneUfficiale?.anno;

    // Recupero l'id ambito
    const idAmbito: number = this.idAmbito;

    // Definisco la struttura per i path param di ricerca
    let pathParams: IRicercaUsiRegola;
    pathParams = { anno, idAmbito };

    // Ritorno l'oggetto utilizzato per la ricerca
    return pathParams;
  }

  /**
   * ####################
   * FUNZIONI DI OVERRIDE
   * ####################
   */

  /**
   * Funzione di comodo che imposta il messaggio d'errore
   * @param error RiscaServerError che definisce il corpo dell'errore.
   * @param messageCode string che definisce il messaggio d'errore da visualizzare in testata. Default è 'E005'.
   * @param otherMessages Array di string contenente un array con altri messaggi di dettaglio per l'errore. Default è [].
   */
  onServiziError(
    error: RiscaServerError,
    messageCode = RiscaNotifyCodes.E005,
    otherMessages: string[] = []
  ) {
    // Verifico se esiste un errore in input
    if (error?.error?.code) {
      // Gestisco l'errore rigenerando la struttura dell'alert
      this.alertConfigs = this._riscaAlert.createAlertFromServerError(error);
      // #
    } else {
      // Utilizzo la logica di default
      super.onServiziError(error, messageCode, otherMessages);
      // #
    }
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
      mapping: this.CAN_C.SNAPSHOT_CONFIG.mapping,
      saveFunc: this.CAN_C.SNAPSHOT_CONFIG.saveFunc,
    };
  }

  /**
   * Getter per l'id dell'ambito.
   * @returns number con l'id.
   */
  get idAmbito(): number {
    // Ritorno l'id ambito
    return this._user.idAmbito;
  }

  /**
   * Getter di verifica per la tabella delle json regole.
   * @returns boolean con il risultato del check.
   */
  get checkJsonRegoleTable(): boolean {
    // Verifico che la tabella esista e abbia dati
    return this.regolaUsiTable?.source?.length > 0;
  }

  /**
   * Getter per il flag di abilitazione omonima.
   * @returns boolean con il flag.
   */
  get disablePaginaCanoni(): boolean {
    // Ritorno il flag
    return this.AEA_pagina_canoni_disabled;
  }

  /**
   * Getter per il flag di abilitazione omonima.
   * @returns boolean con il flag.
   */
  get disableCreaAnnualita(): boolean {
    // Ritorno il flag
    return this.AEA_crea_annualita_disabled;
  }
}
