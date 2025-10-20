import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Subscription } from 'rxjs/index';
import { DilazioneVo } from 'src/app/core/commons/vo/dilazione-vo';
import { StatoDebitorioVo } from 'src/app/core/commons/vo/stato-debitorio-vo';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { AnnualitaModalService } from 'src/app/features/pratiche/service/dati-contabili/annualita-modal.service';
import { DatiContabiliService } from 'src/app/features/pratiche/service/dati-contabili/dati-contabili/dati-contabili.service';
import { SDAnnualitaTable } from 'src/app/shared/classes/risca-table/dati-contabili/annualita.table';
import { RiscaTableDataConfig } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.classes';
import { RiscaUtilitiesComponent } from 'src/app/shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { RiscaModalService } from 'src/app/shared/services/risca/risca-modal.service';
import { PraticaEDatiTecnici } from '../../../../../../core/commons/vo/pratica-vo';
import { AccessoElementiAppKeyConsts } from '../../../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../../../core/services/accesso-elementi-app.service';
import { RiscaLockPraticaService } from '../../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaUtilitiesService } from '../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  ICallbackDataModal,
  IRiscaTabChanges,
  RiscaServerError,
  RiscaStatoDebitorio,
} from '../../../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../../../shared/utilities/enums/risca-notify-codes.enums';
import { AnnualitaConsts } from '../../../../consts/dati-contabili/annualita.consts';
import { DatiContabiliConsts } from '../../../../consts/dati-contabili/dati-contabili.consts';
import { IAnnualitaModalConfigs } from '../../../../modal/annualita-modal/utilities/annualita-modal.interfaces';
import { AnnualitaUtilitiesService } from '../../../../service/dati-contabili/annualita-utilities.service';
import { DatiContabiliUtilityService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { StatoDebitorioService } from '../../../../service/dati-contabili/stato-debitorio.service';
import { DTAmbienteASDData } from '../../../quadri-tecnici/components/middlewares/quadri-tecnici-sd/utilities/quadri-tecnici-sd.component.interfaces';
import { AnnualitaSDVo } from './../../../../../../core/commons/vo/annualita-sd-vo';
import { UserService } from './../../../../../../core/services/user.service';
import { SDAnnualitaTableConfigs } from './../../../../../../shared/classes/risca-table/dati-contabili/annualita.table';

@Component({
  selector: 'annualita',
  templateUrl: './annualita.component.html',
  styleUrls: ['./annualita.component.scss'],
})
export class AnnualitaComponent
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Costante per le informazioni della sezione. */
  DC_C = DatiContabiliConsts;
  /** Costante per le informazioni del componente. */
  A_C = AnnualitaConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** Output con evento che tiene traccia degli errori della form. */
  @Output('onFormErrors') onFormErrors$ = new EventEmitter<string[]>();

  /** Oggetto SDAnnualitaTable che conterrà le configurazioni per la tabella delle annualità. */
  annualitaTable: SDAnnualitaTable;

  /** StatoDebitorioVo come copia dell'oggetto salvato nel servizio per le funzionalità locali. */
  statoDebitorio: StatoDebitorioVo;
  /** PraticaEDatiTecnici come copia dell'oggetto salvato nel servizio per le funzionalità locali. */
  praticaEDT: PraticaEDatiTecnici;

  /** Dilazione presa dal BE nell'inizializzazione della componente */
  dilazione: DilazioneVo;

  /**
   * ##############################################
   * SUBSCRIPTION AL SALVATAGGIO DELLE INFORMAZIONI
   * ##############################################
   */
  /** Subscription che permette di sapere quando la tab dello stato debitorio viene cambiato dall'utente. */
  onSezioneSDChange: Subscription;

  /** Subscription che permette la condivisione dell'evento di inserimento di un oggetto StatoDebitorioVo. */
  onSDInsSuccess: Subscription;
  /** Subscription che permette la condivisione dell'evento d'errore durante un inserimento di un oggetto StatoDebitorioVo. */
  onSDInsError: Subscription;
  /** Subscription che permette la condivisione dell'evento di annuallamento della funzionalità di inserimento di un oggetto StatoDebitorioVo. */
  onSDInsCancel: Subscription;

  /** Subscription che permette la condivisione dell'evento di modifica di un oggetto StatoDebitorioVo. */
  onSDModSuccess: Subscription;
  /** Subscription che permette la condivisione dell'evento d'errore durante una modifica di un oggetto StatoDebitorioVo. */
  onSDModError: Subscription;
  /** Subscription che permette la condivisione dell'evento di annuallamento della funzionalità di modifica di un oggetto StatoDebitorioVo. */
  onSDModCancel: Subscription;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _annualitaModal: AnnualitaModalService,
    private _annualitaUtilities: AnnualitaUtilitiesService,
    private _datiContabili: DatiContabiliService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService,
    private _riscaLockP: RiscaLockPraticaService,
    private _riscaModal: RiscaModalService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _statoDebitorioServ: StatoDebitorioService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);
    // Lancio il setup del componente
    this.setupComponente();
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
    // Funzione di setup per i dati tecnici della pratica
    this.setupPraticaEDTRiepilogo();
    // Lancio il setup dei dati dello stato debitorio
    this.setupStatoDebitorio();
    // Creo la tabella
    this.setupAnnualitaTable([]);
  }

  /**
   * Funzione di setup per le configurazioni del componente tecnico di riepilogo.
   * Essendo che il servizio crea una copia in tutto e per tutto, e l'operazione può risultare onerosa, viene fatta una singola inizializzazione del dato quando il componente viene instanziato, così d'agevolare tutte le possibili elaborazioni locali.
   */
  private setupPraticaEDTRiepilogo() {
    // Recupero il dato della pratica
    this.praticaEDT = this._datiContabiliUtility.praticaEDT;
  }

  /**
   * Funzione di comodo per il recupero dell'informazione dello stato debitorio dal servizio condiviso.
   */
  private setupStatoDebitorio() {
    // Recupero dal servizio condiviso le informazioni per lo stato debitorio
    this.statoDebitorio = this._datiContabiliUtility.statoDebitorio;
  }

  /**
   * Funzione di comodo che permette l'inizializzazione delle tabelle.
   * @param recapiti Array di RecapitoVo con i dati di complicazione della tabella.
   */
  private setupAnnualitaTable(annualita?: AnnualitaSDVo[]) {
    // Definisco gli oggetti di configurazione delle tabelle
    const cra: SDAnnualitaTableConfigs = {
      annualita,
      disableUserInputs: this.disableUserInputs,
    };
    // Lancio la configurazione della tabella
    this.annualitaTable = new SDAnnualitaTable(cra);
    // Effettuo l'ordinamento delle annualità
    this.ordinaAnnualita();
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
    // Lancio l'init dei listener del componente
    this.initListeners();
    // Lancio l'init del tipo dilazione
    this.initTipoDilazione();
  }

  /**
   * Destroy
   * @override
   */
  ngOnDestroy(): void {
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onSezioneSDChange) {
        this.onSezioneSDChange.unsubscribe();
      }
      if (this.onSDInsSuccess) {
        this.onSDInsSuccess.unsubscribe();
      }
      if (this.onSDInsError) {
        this.onSDInsError.unsubscribe();
      }
      if (this.onSDInsCancel) {
        this.onSDInsCancel.unsubscribe();
      }
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
   * Funzione di init per il componente.
   */
  private initComponente() {
    // Recupero la lista di annualità
    const listaAnnualita = this.statoDebitorio?.annualita_sd;
    // Setto gli elementi nella tabella
    this.annualitaTable.setElements(listaAnnualita);
    // Imposto l'ordinamento delle annualità
    this.ordinaAnnualita();
  }

  /**
   * Funzione di init per i listener del componente, per rimanere in ascolto degli eventi per il cambio pagina dei dati contabili.
   */
  private initListeners() {
    this.onSezioneSDChange =
      this._statoDebitorioServ.onSezioneSDChanges$.subscribe({
        next: (tabs: IRiscaTabChanges) => {
          // Lancio la funzione di cambio tab
          this.tabSDChanged(tabs);
        },
      });

    this.onSDInsSuccess = this._datiContabili.onSDInsSuccess$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Gestisco l'evento di success
        this.onSDSuccessResult(statoDebitorio);
      },
    });

    this.onSDInsError = this._datiContabili.onSDInsError$.subscribe({
      next: (error: RiscaServerError) => {
        // Gestisco l'errore anche nel caso sia multiplo
        this.onSDErrorResult(error);
      },
    });

    this.onSDInsCancel = this._datiContabili.onSDInsCancel$.subscribe({
      next: () => {
        // Gestisco l'evento di cancellazione
        this.onSDCancelResult();
      },
    });

    this.onSDModSuccess = this._datiContabili.onSDModSuccess$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Gestisco l'evento di success
        this.onSDSuccessResult(statoDebitorio);
      },
    });

    this.onSDModError = this._datiContabili.onSDModError$.subscribe({
      next: (error: RiscaServerError) => {
        // Gestisco l'errore anche nel caso sia multiplo
        this.onSDErrorResult(error);
      },
    });

    this.onSDModCancel = this._datiContabili.onSDModCancel$.subscribe({
      next: () => {
        // Gestisco l'evento di cancellazione
        this.onSDCancelResult();
      },
    });
  }

  /**
   * Gestisce le operazioni da eseguire alla fine del successo nel salvataggio dello stato debitorio.
   * @param statoDebitorio StatoDebitorioVo salvato.
   */
  onSDSuccessResult(statoDebitorio: StatoDebitorioVo) {
    // Il componente attiva la modalità modifica
    this.modalita = AppActions.modifica;
    // poi devo copiarmelo in questo componente e ripristinare la pagina
    this.aggiornaPagina();
  }

  /**
   * Gestisce le operazioni da eseguire in caso di errore nel salvataggio dello stato debitorio.
   */
  onSDErrorResult(error: RiscaServerError) {
    // Gestione della risposta dall'ascoltatore
  }

  /**
   * Gestisce le operazioni da eseguire alla fine della cancellazione nel salvataggio dello stato debitorio.
   */
  onSDCancelResult() {
    // Gestione della risposta dall'ascoltatore
  }

  /**
   * Funzione che ripristina le informazioni della pagina al termine delle operazioni di
   * salvataggio, gestione errori e annullamento della modifica
   * dello stato debitorio con le informazioni di questa pagina.
   */
  aggiornaPagina() {
    // Lancio il setup dei dati dello stato debitorio
    this.setupStatoDebitorio();
    // Riavvio l'inizializzazione dei dati del componente
    this.initComponente();
  }

  /**
   * Prende il tipo dilazione per
   */
  initTipoDilazione() {
    // Recupero l'idAmbito
    const idAmbito = this._user.idAmbito;
    // Recupero l'id tipo dilazione dallo stato debitori
    const idDilazione = this.statoDebitorio?.id_tipo_dilazione;
    // Faccio la chiamata per il tipo dilazione
    this._datiContabili.getDilazione(idAmbito, idDilazione).subscribe({
      next: (d: DilazioneVo) => {
        // Segno i valori che mi servono
        this.dilazione = d;
        //#
      },
      error: (error: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(error);
        // #
      },
    });
  }

  /**
   * ##########################
   * FUNZIONALITA' DEI PULSANTI
   * ##########################
   */

  /**
   * Funzione collegata al click del pulsante: Aggiungi nuova annualità.
   * Il pulsante aprirà una modale per l'inserimento delle informazioni di una nuova annualità.
   */
  aggiungiAnnualita() {
    // Richiamo l'apertura dell'aggiungi annualita
    this.apriAggiungiAnnualita();
  }

  /**
   * Funzione di comodo che gestisce i dati e permette l'inserimento di una nuova annualità.
   */
  private apriAggiungiAnnualita() {
    // Definisco i parametri da passare alla modale
    const dataModal: IAnnualitaModalConfigs = {
      statoDebitorio: this.statoDebitorio,
      praticaEDT: this.praticaEDT,
      modalita: AppActions.inserimento,
      rateoPrimaAnnualita: this.rateoPrimaAnnualita,
    };
    const callbacks: ICallbackDataModal = {
      onConfirm: (datiAnnualita: DTAmbienteASDData) => {
        // Richiamo la funzione per aggiornare i dati
        this.onInsAnnualita(datiAnnualita);
      },
    };

    // Aprire modale di inserimento annualità
    this._annualitaModal.apriModaleAnnualita(dataModal, callbacks);
  }

  /**
   * Funzione di eliminazione di un elemento dalla tabella
   * @param row RiscaTableDataConfig<AnnualitaSDVo> contenente la riga selezionata
   */
  rimuoviAnnualita(row: RiscaTableDataConfig<AnnualitaSDVo>) {
    // Estraggo dalla riga l'oggetto dell'annualita
    const annualita = row.original;

    // Creo oggetto di wrapper per la cancellazione
    const datiAnnualita: DTAmbienteASDData = { annualita };
    // Richiamo la funzione per la conferma della cancellazione
    this.confirmRimuoviAnnualita(datiAnnualita);
  }

  /**
   * Funzione che chiede la conferma all'utente per la cancellazione di un'annualità.
   * @param datiAnnualita DTAmbienteASDData con le informazioni per la cancellazione dati.
   */
  private confirmRimuoviAnnualita(datiAnnualita: DTAmbienteASDData) {
    // Creo la callback dalla conferma
    const callback: ICallbackDataModal = {
      onConfirm: () => {
        // Richiamo la funzione per aggiornare i dati
        this.onDelAnnualita(datiAnnualita);
      },
    };
    // Chiamo la modale di conferma
    this._riscaModal.apriModalConfermaCancellazione(callback);
  }

  /**
   * Funzione di apertura della modale di modifica di una annualità
   * @param row RiscaTableDataConfig<AnnualitaSDVo> contenente la riga selezionata
   */
  modificaAnnualita(row: RiscaTableDataConfig<AnnualitaSDVo>) {
    // Recupero l'annualità dalla riga della tabella
    const annualitaTable = row.original;
    // Definisco i parametri da passare alla modale
    const dataModal: IAnnualitaModalConfigs = {
      statoDebitorio: this.statoDebitorio,
      praticaEDT: this.praticaEDT,
      annualita: annualitaTable,
      modalita: AppActions.modifica,
    };
    const callbacks: ICallbackDataModal = {
      onConfirm: (datiAnnualita: DTAmbienteASDData) => {
        // Richiamo la funzione per aggiornare i dati
        this.onModAnnualita(datiAnnualita);
      },
    };

    // Apri modale di modifica
    this._annualitaModal.apriModaleAnnualita(dataModal, callbacks);
  }

  /**
   * Funzione collegata al click del pulsante: Salva modifica.
   */
  salvaModifiche() {
    // Recupero la lista di annualità
    const annualita: AnnualitaSDVo[] = this.annualitaTable.getDataSource();

    // Verifico se esistono annualità
    if (annualita?.length > 0) {
      // Richiamo la funzione di salvataggio
      this.salvataggioAnnualita();
      // #
    } else {
      // Non ci sono annualità, definisco il codice di errore
      const E048 = RiscaNotifyCodes.E048;
      // Recupero il messaggio d'errore
      const noAnnualita = this._riscaMessages.getMessage(E048);
      // Emetto l'evento per la gestione delle annualità mancanti
      this.onFormErrors$.emit([noAnnualita]);
    }
  }

  /**
   * Funzione che gestisce il salvataggio e aggiornato per i dati delle annualità.
   */
  private salvataggioAnnualita() {
    // Aggiorno lo stato debitorio e lo recupero
    const statoDebitorio = this.aggiornaAnnualitaSD();

    // In base alla modalità eseguo verifica e salvataggio
    if (this.inserimento) {
      // Lancio il servizio di verifica ed inserimento dello stato debitorio
      this._datiContabili.verifyAndInsertStatoDebitorio(statoDebitorio);
      // #
    } else {
      // Lancio il servizio di verifica ed aggiornamento dello stato debitorio
      this._datiContabili.verifyAndUpdateStatoDebitorio(statoDebitorio);
      // #
    }
  }

  /**
   * ##############################################
   * FUNZIONI CHE GESTISCONO INSERIMENTO ANNUALITA'
   * ##############################################
   */

  /**
   * Funzione invocata quando viene inserita una nuova annualità dalla modale.
   * @param datiAnnualita DTAmbienteAnnualitaSD contenente le informazioni generate dalla modale.
   */
  private onInsAnnualita(datiAnnualita: DTAmbienteASDData) {
    // Destrutturo l'input ed estraggo l'annualità
    const { annualita } = datiAnnualita;
    // Essendo un inserimento, valorizzo l'id di FE, per avere un riferimento dell'oggetto
    annualita.__id = this._riscaUtilities.generateRandomId();

    // Aggiorno la tabella delle annualità
    this.annualitaTable.addElement(annualita);
    // Aggiungo allo stato debitorio la nuova annualità
    this.statoDebitorio?.annualita_sd?.push(annualita);
    // Ordino gli elementi della tabella
    this.ordinaAnnualita();
  }

  /**
   * ###########################################
   * FUNZIONI CHE GESTISCONO MODIFICA ANNUALITA'
   * ###########################################
   */

  /**
   * Funzione invocata quando viene modifica un'annualità dalla modale.
   * @param datiAnnualita DTAmbienteAnnualitaSD contenente le informazioni generate dalla modale.
   */
  private onModAnnualita(datiAnnualita: DTAmbienteASDData) {
    // Estraggo l'annualità dai datiAnnualita
    const { annualita } = datiAnnualita;

    // Cerco l'annualità nella tabella
    const iAnn = this.indexAnnualitaInTable(annualita);
    // Verifico se ho trovato l'elemento
    if (iAnn >= 0) {
      // Recupero la riga dell'annualità
      const rowAnn = this.annualitaTable.getElementByIndex(iAnn);
      // Richiamo la funzione di aggiornamento del dato
      this.annualitaTable.convertAndUpdateElement(annualita, rowAnn);
      // #
    }

    // Cerco l'annualità nell'oggetto stato debitorio
    const iAnnSD = this.indexAnnualitaInSD(annualita);
    // Verifico se ho trovato l'elemento
    if (iAnnSD >= 0) {
      // Elemento trovato, lo sostituisco
      this.statoDebitorio?.annualita_sd?.splice(iAnnSD, 1, annualita);
      // #
    }
    // Ordino gli elementi della tabella
    this.ordinaAnnualita();
  }

  /**
   * ################################################
   * FUNZIONI CHE GESTISCONO CANCELLAZIONE ANNUALITA'
   * ################################################
   */

  /**
   * Funzione invocata quando viene cancellata un'annualità dalla tabella.
   * @param datiAnnualita DTAmbienteAnnualitaSD contenente le informazioni generate dalla modale.
   */
  private onDelAnnualita(datiAnnualita: DTAmbienteASDData) {
    // Estraggo l'annualità dai datiAnnualita
    const { annualita } = datiAnnualita || {};

    // Cerco l'annualità nella tabella
    const iAnn = this.indexAnnualitaInTable(annualita);
    // Rimuovo l'elemento dalla tabella
    this.annualitaTable.removeElementByIndex(iAnn);

    // Cerco l'annualità nello stato debitorio
    const iAnnSD = this.indexAnnualitaInSD(annualita);
    // Rimuovo l'elemento se l'indice è > 0
    if (iAnnSD >= 0) {
      // Elemento trovato, lo rimuovo
      this.statoDebitorio?.annualita_sd?.splice(iAnnSD, 1);
    }
    // Ordino gli elementi della tabella
    this.ordinaAnnualita();
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione di comodo che crea i dati ed emette l'evento di cambio tab dello stato debitorio.
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   */
  private tabSDChanged(tabs: IRiscaTabChanges) {
    // Recupero l'identificativo di questo componente
    const tabComp = RiscaStatoDebitorio.annualita;

    // Verifico tramite servizio se ci si sta spostando su un'altra tab
    if (this._statoDebitorioServ.checkTabSD(tabs, tabComp)) {
      // Mi sto spostando, aggiorno i dati dello stato debitorio nel servizio
      this.aggiornaAnnualitaSD();
    }
  }

  /**
   * Funzione di utility che ricerca un'annualità dentro la tabella annualità.
   * @param annualita AnnualitaSDVo con le informazioni dell'annualità da cercare.
   * @return number con l'indice posizionale dell'oggetto nella tabella.
   */
  private indexAnnualitaInTable(annualita: AnnualitaSDVo): number {
    // Recupero la lista di annualità dalla tabella
    const annualitaList: AnnualitaSDVo[] = this.annualitaTable.getDataSource();
    // Richiamo il servizio di utility per la ricerca
    return this._annualitaUtilities.indexAnnualitaInListById(
      annualita,
      annualitaList
    );
  }

  /**
   * Funzione di utility che ricerca un'annualità dentro le annualità dello stato debitorio.
   * @param annualita AnnualitaSDVo con le informazioni dell'annualità da cercare.
   * @return number con l'indice posizionale dell'oggetto nelle annualità dello stato debitorio.
   */
  private indexAnnualitaInSD(annualita: AnnualitaSDVo): number {
    // Recupero la lista di annualità dalla tabella
    const annualitaList: AnnualitaSDVo[] = this.annualitaSD;
    // Richiamo il servizio di utility per la ricerca
    return this._annualitaUtilities.indexAnnualitaInListById(
      annualita,
      annualitaList
    );
  }

  /**
   * Funzione di comodo che aggiorna le annualità dello stato debitorio, recuperando le informazioni dentro i dati della tabella.
   * @returns StatoDebitorioVo aggiornato alle ultime informazioni.
   */
  private aggiornaAnnualitaSD(): StatoDebitorioVo {
    // Recupero le informazioni delle annualità dalla tabella
    const annualitaSD: AnnualitaSDVo[] = this.annualitaTable.getDataSource();
    // Aggiungo allo stato debitorio la nuova annualità ed i suoi usi
    this._datiContabiliUtility.updateAnnualita(annualitaSD);

    // Recuperare lo stato debitorio aggiornato
    return this._datiContabiliUtility.statoDebitorio;
  }

  /**
   * Ordina le annualità in tabella
   */
  private ordinaAnnualita() {
    // Prendo gli elementi
    const listaAnnualita =
      this.annualitaTable.getDataSource() as AnnualitaSDVo[];
    // Riordino gli elementi
    const annualitaOrdinate = listaAnnualita.sort(this.sortByAnno);
    // Setta gli elementi della tabella
    this.annualitaTable.setElements(annualitaOrdinate);
  }

  /**
   * Funzione usata per ordinare i dati della tabella delle annualità.
   * @param a AnnualitaSDVo annualità usata per il confronto
   * @param b AnnualitaSDVo annualità usata per il confronto
   */
  private sortByAnno(a: AnnualitaSDVo, b: AnnualitaSDVo) {
    // Controllo di esistenza
    if (a == undefined && b == undefined) {
      return 0;
    }
    if (a == undefined) {
      return 1;
    }
    if (b == undefined) {
      return -1;
    }
    // Calcolo ordinamento
    return b.anno - a.anno;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che verifica che la tabella delle annualità esista e sia popolata.
   */
  get checkAnnualitaTable() {
    // Verifico esistenza e presenza di elementi
    return this.annualitaTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che ritorna se all'interno della tabelle dell'annualità esiste già un'annualità con flg_rateo_prima_annualita a true.
   * @returns boolean con il check del controllo.
   */
  get rateoPrimaAnnualita() {
    // Recupero i dati dalla tabella
    let annualita: AnnualitaSDVo[];
    annualita = this.annualitaTable?.getDataSource() ?? [];

    // Verifico se all'interno esiste un'annualità con il flag settato
    const rpa = annualita.some((a: AnnualitaSDVo) => {
      // Ritorno il flag del rateo prima annualita
      return a.flg_rateo_prima_annualita;
    });

    // Ritorno il check di presenza
    return rpa;
  }

  /**
   * Getter di comodo per il recupero delle annualità dello stato debitorio.
   * @returns AnnualitaSDVo[] come lista annualità dello stato debitorio.
   */
  get annualitaSD(): AnnualitaSDVo[] {
    // Ritorno la lista delle annualità dello stato debitorio
    return this.statoDebitorio?.annualita_sd ?? [];
  }

  /**
   * Getter che verifica che le informazioni per la configurazione di alertCEConfigs siano valide.
   */
  get alertConfigscheck() {
    // Verifico che la configurazione per l'alert delle comunicazioni esista e contenga dati
    return this.alertConfigs?.messages?.length > 0;
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.isPraticaLocked || this.AEA_detSDDisabled;
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
   * Getter per il recupero del flag per AEA: dettaglio stato debitorio.
   * @returns boolean con il valore della configurazione.
   */
  get AEA_detSDDisabled(): boolean {
    // Verifico se la configurazione di accesso agli elementi è attivo
    let AEA_detSDDisabled: boolean;
    AEA_detSDDisabled = this._accessoElementiApp.isAEADisabled(
      this.AEAK_C.DET_STATO_DEB
    );

    // Ritorno la configurazione
    return AEA_detSDDisabled;
  }
}
