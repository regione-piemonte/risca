import { Component, OnInit, ViewChild } from '@angular/core';
import { clone, cloneDeep } from 'lodash';
import { RicercaPaginataResponse } from 'src/app/core/classes/http-helper/http-helper.classes';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaRicercaBollettiniTable } from 'src/app/shared/classes/risca-table/bollettini/ricerca-bollettini/ricerca-bollettini.table';
import { RiscaRicercaBollettiniComponent } from 'src/app/shared/components/risca/risca-ricerca-bollettini/risca-ricerca-bollettini.component';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { RiscaInfoLevels } from 'src/app/shared/utilities';
import { RiscaNotifyCodes } from 'src/app/shared/utilities/enums/risca-notify-codes.enums';
import { StatoElaborazioneVo } from '../../../../core/commons/vo/stato-elaborazione-vo';
import { TipoElaborazioneVo } from '../../../../core/commons/vo/tipo-elaborazione-vo';
import { RiscaTableComponent } from '../../../../shared/components/risca/risca-table/risca-table.component';
import { IRiscaTableACEvent } from '../../../../shared/components/risca/risca-table/utilities/risca-table.interfaces';
import { RiscaUtilitiesComponent } from '../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RRBollettiniPagamenti } from '../../../../shared/consts/risca/risca-ricerca-bollettini.consts';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { BollettiniConsts } from '../../consts/bollettini/bollettini.consts';
import { CodiciStatiElaborazione } from '../../enums/gestione-pagamenti/stato-elaborazione.enums';
import { IBollettiniModalConfigs } from '../../interfaces/bollettini/bollettini.interfaces';
import { BollettiniModalService } from '../../service/bollettini-modal/bollettini-modal.service';
import { ElaborazioneVo } from './../../../../core/commons/vo/elaborazione-vo';
import { UserService } from './../../../../core/services/user.service';
import {
  RiscaServerError,
  RiscaTablePagination,
} from './../../../../shared/utilities/classes/utilities.classes';
import {
  ICallbackDataModal,
  IRicercaBollettini,
  IRiscaDDItemConfig,
} from './../../../../shared/utilities/interfaces/utilities.interfaces';
import { BollettiniService } from './../../service/bollettini/bollettini.service';
import {
  INuovaPrenotazioneClass,
  NuovaPrenotazioneDropDown,
} from './utilities/nuova-prenotazione.configs';
import {
  NPModalitaModale,
  TipiPrenotazione,
} from './utilities/nuova-prenotazione.enums';

/**
 * Interfaccia di comodo che definisce le informazioni per l'ultima ricerca effettuata.
 */
interface IBUltimaRicerca {
  filtri: IRicercaBollettini;
  paginazione?: RiscaTablePagination;
}

@Component({
  selector: 'bollettini',
  templateUrl: './bollettini.component.html',
  styleUrls: ['./bollettini.component.scss'],
})
export class BollettiniComponent
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente i valori costanti del componente. */
  B_C = BollettiniConsts;
  /** Oggetto contenente i valori costanti per il componente. */
  RRB_C = RRBollettiniPagamenti;
  /** Oggetto contenente i valori costanti che identificano i tipi prenotazione. */
  TP_C = TipiPrenotazione;

  /** Costante string che definisce il codice per il recupero del messaggio che indica quando non sono stati trovati elementi per la tabella. */
  NO_TABLE_DATA_CODE = RiscaNotifyCodes.I001;

  /** ViewChild che definisce la connessione al componente: RiscaRicercaBollettiniComponent. */
  @ViewChild('riscaRicercaBollettini')
  riscaRicercaBollettini: RiscaRicercaBollettiniComponent;
  /** ViewChild che definisce la connessione al componente: RiscaRicercaBollettiniComponent. */
  @ViewChild('riscaBollettazioneTable')
  riscaBollettazioneTable: RiscaTableComponent<any>;

  /** Tabelle per i risultati */
  tableBollettini = new RiscaRicercaBollettiniTable();
  /** RiscaDDButtonConfig che definisce le configurazioni per il pulsante di dropdown, per l'apertura delle modali. */
  nuovaPrenotazione: NuovaPrenotazioneDropDown;
  /** IBUltimaRicerca che contiene le informazioni dell'ultima ricerca effettuata. */
  lastRicerca: IBUltimaRicerca;

  /**
   * Costruttore.
   */
  constructor(
    private _bollettini: BollettiniService,
    private _bollettiniModal: BollettiniModalService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaFA: RiscaFiloAriannaService,
    riscaMessages: RiscaMessagesService,
    private _user: UserService
  ) {
    super(navigationHelper, riscaAlert, riscaMessages);
    this.stepConfig = this.B_C.NAVIGATION_CONFIG;

    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit(): void {
    // Richiamo l'init per i dati possibilmante salvati come snapshot
    this.initJSnapshot();
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
    // Lancio il setup per il pulsante dropdown
    this.setupDDButton();
    // Lancio il setup per il filo d'arianna
    this.setupFiloArianna();
  }

  /**
   * Funzione di comodo per il setup del pulsante di dropdown per la nuova bollettazione.
   */
  private setupDDButton() {
    // Recupero i dati per i tipi elaborazione per la prenotazione
    this._bollettini.getTipiBollettazione(this.idAmbito).subscribe({
      next: (te: TipoElaborazioneVo[]) => {
        // Creo la configurazione per il pulsante dropdown
        const ddbConfig: INuovaPrenotazioneClass = {
          tipiPrenotazione: te,
        };
        // Inizializzo il pulsante dropdown
        this.nuovaPrenotazione = new NuovaPrenotazioneDropDown(ddbConfig);
      },
      error: (e: RiscaServerError) => {
        // Richiamo l'alert per l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione di setup per il filo d'arianna.
   */
  private setupFiloArianna() {
    // Definisco il livello per bollettini
    const bollettiniFA = this._riscaFA.bollettini;
    // Imposto il filo d'arianna
    this._riscaFA.aggiungiSegmentoByLivelli(bollettiniFA);
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
    // Carico i dati della snapshot
    this.loadJSnapshot(this);
    // Attivo il setup dei dati di route
    this.loadRicercaPA();
  }

  /**
   * Funzione impiegata per il caricamento dei dati delle pratiche.
   */
  private loadRicercaPA() {
    // Verifico se è presente l'oggetto per la form ricerca pratiche
    if (!this.lastRicerca) {
      // Definisco una struttura di defualt per la ricerca (usata come prima ricerca)
      this.lastRicerca = {
        filtri: null,
        paginazione: this.tableBollettini.getPaginazione(),
      };
    }

    // I dati esistono, rilancio la ricerca aggiornando la paginazione
    this.tableBollettini.updatePaginazione(this.lastRicerca.paginazione);
  }

  /**
   * ##################################
   * FUNZIONI COLLEGATE ALL'INTERFACCIA
   * ##################################
   */

  /**
   * Avviato quando viene avviata la form di ricerca la prima volta
   */
  avviaPrimaRicerca(): void {
    // Devo avviare la prima ricerca
    if (!this.lastRicerca?.filtri) {
      // Triggero manualmente il submit della form di filtri di ricerca
      this.riscaRicercaBollettini.avviaRicerca();
    }
  }

  /**
   * Funzione che gestisce le informazioni prima dello scarico dati per la tabella bollettazione.
   * @param filtri IRicercaBollettini filtri di ricerca.
   * @param paginazione RiscaTablePagination dati della paginazione.
   */
  filtraBollettini(
    filtri: IRicercaBollettini,
    paginazione?: RiscaTablePagination
  ) {
    // Resetto l'alert della pagina
    this.aggiornaAlertConfigs();

    // Gestisco l'oggetto di paginazione, per la ricerca a seguito del filtro dati
    const paginationeConfig = this.paginazioneBollettiniOnFiltra(paginazione);
    // Salvo nell'oggetto di appoggio le informazioni della ricerca lanciata
    this.lastRicerca = { filtri, paginazione };

    // Lancio la ricerca effettiva dei dati
    this.ricercaBollettazione(filtri, paginationeConfig, true);
  }

  /**
   * ###################################
   * FUNZIONI DI GESTIONE DEL COMPONENTE
   * ###################################
   */

  /**
   * Funzione che gestisce la paginazione quando viene applicato un filtro per i dati delle bollettazioni.
   * @param paginazione RiscaTablePagination definisce la configurazione per la paginazione.
   * @returns RiscaTablePagination con la configurazione per la paginazione d'applicare.
   */
  private paginazioneBollettiniOnFiltra(
    paginazione?: RiscaTablePagination
  ): RiscaTablePagination {
    // Definisco l'oggetto della paginazione d'applicare
    let paginazioneConfig: RiscaTablePagination;
    // Recupero la paginazione di default per la tabella dei bollettini
    let defaultConfig: RiscaTablePagination =
      this.tableBollettini.getDefaultPagination();

    // Verifico se esiste un oggetto di paginazione in input
    if (!paginazione) {
      // Non esiste paginazione, imposto quella di default
      paginazioneConfig = defaultConfig;
      // #
    } else {
      // Esiste la paginazione imposto dall'input
      paginazioneConfig = clone(paginazione);
      // Per specifiche funzionali, al filtra bisogna resettare la ricerca sulla base del campo e ordinamento di default
      paginazione.sortBy = defaultConfig.sortBy;
      paginazione.sortDirection = defaultConfig.sortDirection;
      // #
    }

    // Ritorno l'oggeto di configurazione
    return paginazioneConfig;
  }

  /**
   * Funzione che gestisce lo scarico delle informazioni per le bollettazioni massive, poi usate nella tabella.
   * @param filtri IRicercaBollettini filtri di ricerca.
   * @param paginazione RiscaTablePagination dati della paginazione.
   * @param initSortTable boolean che definisce se il sort della tabella deve essere ripristinato al suo stato originale. Default: false.
   */
  private ricercaBollettazione(
    filtri: IRicercaBollettini,
    paginazione: RiscaTablePagination,
    initSortTable = false
  ) {
    // Variabili di comodo
    const idA = this._user.idAmbito;
    const f = filtri;
    const p = paginazione;
    
    // Scarico le informazioni per la tabella
    this._bollettini.getElaborazioniBollettiniPagination(idA, f, p).subscribe({
      next: (response: RicercaPaginataResponse<ElaborazioneVo[]>) => {
        // Richiamo la funzione per gestire i risultati della ricerca
        this.onRicercaBollettini(response, initSortTable);
        // #
      },
      error: (error: any) => {
        // Gestisco il messaggio d'errore inaspettato
        this.onServiziError(error);
      },
    });
  }

  /**
   * Funzione che gestisce i risultati della ricerca per la bollettazione massiva.
   * @param response RicercaPaginataResponse<Elaborazione[]> con i risultati della ricerca.
   * @param initSortTable boolean che definisce se il sort della tabella deve essere ripristinato al suo stato originale. Default: false.
   */
  private onRicercaBollettini(
    response: RicercaPaginataResponse<ElaborazioneVo[]>,
    initSortTable = false
  ) {
    // Estraggo dalla risposta le informazioni utili
    const listaBollettini = response.sources;
    const paginazione = response.paging;

    // Inserisco i dati trovati nella tabella dei risultati
    this.tableBollettini.setElements(listaBollettini);
    // Aggiorno la paginazione
    this.tableBollettini.updatePaginazioneAfterSearch(paginazione);

    // Verifico se devo allineare graficamente il sort della tabella
    if (initSortTable) {
      // Se esiste l'oggetto della tabella, richiamo il set del sort init
      this.bollettazioneTable?.initialSortConfigs();
    }
  }

  /**
   * Funzione di comodo che effettua un refresh dei dati basandosi sull'ultima ricerca effettuata.
   */
  private refreshTabellaElaborazione() {
    // Recupero i dati dell'ultima ricerca fatta per la tabella
    const { filtri, paginazione } = this.lastRicerca;
    // Aggiorno la ricerca dati secondo l'ultima ricerca effettuata
    this.ricercaBollettazione(filtri, paginazione);
  }

  /**
   * #################################################
   * FUNZIONI CONNESSE ALLA TABELLA DELLE ELABORAZIONI
   * #################################################
   */

  /**
   * Questa funzione ricarica la ricerca ma passa anche i dati della paginazione al BE, così da cambiare pagina
   * @param pagination RiscaTablePagination con i dati della paginazione
   */
  cambiaPagina(pagination: RiscaTablePagination) {
    // Aggiorno la paginazione
    this.lastRicerca.paginazione = pagination;
    // Ricarico i dati
    const { filtri: ricerca, paginazione } = this.lastRicerca;
    // Avvio la ricerca
    this.ricercaBollettazione(ricerca, paginazione);
  }

  /**
   * Funzione agganciata all'evento di azione custom da parte della tabella dei bollettini.
   * @param customAction IRiscaTableACEvent<any> con la configurazione dati per il pulsante premuto.
   */
  customBTable(customAction: IRiscaTableACEvent<any>) {
    // Recupero l'oggetto elaborazione della riga
    const elaborazione: ElaborazioneVo = customAction?.row?.original;

    // Verifico quale pulsante è stato premuto
    switch (customAction.action.action) {
      case this.RRB_C.RIC_BOLL_CONFERMA: {
        // Richiamo la modale per la conferma
        this.onConfermaPrenotazione(elaborazione);
        break;
      }
      case this.RRB_C.RIC_BOLL_ANNULLA: {
        // Richiamo l'annullamento
        this.onAnnullaPrenotazione(elaborazione);
        break;
      }
      case this.RRB_C.RIC_BOLL_SCARICA_FILE: {
        // Richiamo il download del file dell'elaborazione
        this.onScaricaFile(elaborazione);
        break;
      }
    }
  }

  /**
   * ################################################
   * FUNZIONI PER GESTIONE DELLE MODALI D'INSERIMENTO
   * ################################################
   */

  /**
   * Funzione agganciata al componente del pulsante dropdown, che intercetta la pressione di uno degli item della dropdown.
   * La funzione aprirà una modale per l'inserimento dei dati per una nuova prenotazione.
   * @param item IRiscaDDItemConfig che contiene le informazioni riguardante l'item premuto nella dropdown.
   */
  onNuovaPrenotazione(item: IRiscaDDItemConfig) {
    // Resetto l'alert dei messaggi
    this.aggiornaAlertConfigs();
    // Recupero il tipo elaborazione dall'item
    const tipoElaborazione: TipoElaborazioneVo = item.data;

    // RISCA-762: tutti i casi di verifica ricadono per tutti i tipi elaborazione, per sicurezza commento il codice.
    // Lancio la verifica per il tipo elaborazione
    this._bollettini
      .verifyInserimentoElaborazione(/* tipoElaborazione */)
      .subscribe({
        next: (verify: boolean) => {
          // Verifico se la verifica ha trovato elementi
          if (verify) {
            // Ci sono elementi, gestisco la situazione
            this.onVerifyInsElaborazioneError();
            // #
          } else {
            // Apro la modale d'inserimento
            this.apriModalNP(tipoElaborazione);
            // #
          }
        },
      });
  }

  /**
   * Funzione di comodo per gestire lo stato di errore per quello che riguarda la verifica di un tipo elaborazione: inserimento.
   */
  onVerifyInsElaborazioneError() {
    // Gestisco la situazione di errore
    const a = this.alertConfigs;
    const m = [this._riscaMessages.getMessage(RiscaNotifyCodes.E055)];
    const e = RiscaInfoLevels.danger;
    // Aggiorno l'alert
    this.aggiornaAlertConfigs(a, m, e);
  }

  /**
   * Funzione agganciata al componente del pulsante dropdown, che intercetta la pressione di uno degli item della dropdown.
   * La funzione aprirà una modale per l'inserimento dei dati per una nuova prenotazione.
   * @param tipoElaborazione TipoElaborazione che contiene le informazioni riguardante l'item premuto nella dropdown.
   */
  apriModalNP(tipoElaborazione: TipoElaborazioneVo) {
    // Variabili di comodo
    const onConfirm = (elaborazione: ElaborazioneVo) => {
      // Richiamo la funzione di gestione per la chiusura
      this.onModalNPConfirm(elaborazione);
    };
    // Definisco la modalita per la modale
    const modalita = NPModalitaModale.emissione_prenotazione;

    // Definisco le configurazioni per la gestione delle modali
    const bmConfigs: IBollettiniModalConfigs = {
      callbacks: { onConfirm },
      modalMap: this.nuovaPrenotazione.modalMap,
      dataModal: { modalita, tipoElaborazione },
    };

    // Richiamo il servizio che aprirà la modale
    this._bollettiniModal.openPrenotazioneModal(bmConfigs);
  }

  /**
   * Funzione che viene richiamata quando la modale d'inserimento di una nuova prenotazione viene chiusa con conferma.
   * @param elaborazione Elaborazione che definisce le informazioni restituite dalla modale.
   */
  private onModalNPConfirm(elaborazione: ElaborazioneVo) {
    // Lancio l'inserimento dell'elaborazione
    this._bollettini.insertElaborazione(elaborazione).subscribe({
      next: (e: ElaborazioneVo) => {
        // Oggetto aggiunto aggiorno l'alert
        this.onServiziSuccess();
        // Effettuo un refresh dei dati
        this.refreshTabellaElaborazione();
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco il messaggio d'errore inaspettato
        this.onServiziError(e);
      },
    });
  }

  /**
   * ##############################################
   * FUNZIONI PER GESTIONE DELLE MODALI DI CONFERMA
   * ##############################################
   */

  /**
   * Funzione richiamata nel momento in cui un utente vuole confermare una prenotazione.
   * Verranno effettuati i controlli per verificare se l'utente può fare questo tipo d'operazione.
   * @param elaborazione Elaborazione che contiene le informazioni dell'elaborazione da confermare.
   */
  onConfermaPrenotazione(elaborazione: ElaborazioneVo) {
    // Resetto l'alert dei messaggi
    this.aggiornaAlertConfigs();
    // Recupero il tipo elaborazione dall'item
    const tipoElaborazione = elaborazione.tipo_elabora;

    // Lancio la verifica per il tipo elaborazione
    this._bollettini
      .verifyConfermaElaborazione(/* tipoElaborazione */)
      .subscribe({
        next: (verify: boolean) => {
          // Verifico se la verifica ha trovato elementi
          if (verify) {
            // Ci sono elementi, gestisco la situazione
            this.onVerifyElaborazioneErr();
            // #
          } else {
            // Apro la modale di conferma
            this.apriModalCP(elaborazione);
            // #
          }
        },
      });
  }

  /**
   * Funzione richiamata nel momento in cui un utente vuole annullare un'elaborazione.
   * @param elaborazione Elaborazione che contiene le informazioni dell'elaborazione.
   */
  onAnnullaPrenotazione(elaborazione: ElaborazioneVo) {
    // Resetto l'alert dei messaggi
    this.aggiornaAlertConfigs();
    // Recupero il tipo elaborazione dall'item
    const tipoElaborazione = elaborazione.tipo_elabora;

    // RISCA-762: tutti i casi di verifica ricadono per tutti i tipi elaborazione, per sicurezza commento il codice.
    // Lancio la verifica per il tipo elaborazione
    this._bollettini.verifyAnnullaElaborazione(/* tipoElaborazione */).subscribe({
      next: (verify: boolean) => {
        // Verifico se la verifica ha trovato elementi
        if (verify) {
          // Ci sono elementi, gestisco la situazione
          this.onVerifyElaborazioneErr();
          // #
        } else {
          // Apro la modale di conferma annullamento
          this.apriModalAP(elaborazione);
          // #
        }
      },
    });
  }

  /**
   * Funzione richiamata nel momento in cui un utente vuole scaricare un file.
   * @param elaborazione Elaborazione che contiene le informazioni del file da scaricare.
   */
  onScaricaFile(elaborazione: ElaborazioneVo) {
    // Resetto l'alert dei messaggi
    this.aggiornaAlertConfigs(this.alertConfigs);
    // Prendo l'id_elabora
    const { id_elabora } = elaborazione;
    // Scarico il file
    this._bollettini.scaricaEApriFile(id_elabora).subscribe({
      error: (error: RiscaServerError) => {
        // Gestisco il messaggio d'errore
        this.onServiziError(error);
      },
    });
  }

  /**
   * Funzione di comodo per gestire lo stato di errore per quello che riguarda la verifica di un tipo elaborazione.
   * @param code string contenente il codice d'errore per la gestione della messaggistica. Per default è: RiscaNotifyCodes.E055.
   */
  onVerifyElaborazioneErr(code: string = RiscaNotifyCodes.E055) {
    // Gestisco la situazione di errore
    const a = this.alertConfigs;
    const m = [this._riscaMessages.getMessage(code)];
    const e = RiscaInfoLevels.danger;
    // Aggiorno l'alert
    this.aggiornaAlertConfigs(a, m, e);
  }

  /**
   * Funzione invocata alla pressione del pulsante "conferma" della tabella.
   * La funzione aprirà una modale per la conferma dei dati per una prenotazione.
   * @param elaborazione Elaborazione con i dati della configurazione da confermare.
   */
  apriModalCP(elaborazione: ElaborazioneVo) {
    // Variabili di comodo
    const onConfirm = (elaborazione: ElaborazioneVo) => {
      // Richiamo la funzione di gestione per la chiusura
      this.onModalCPConfirm(elaborazione);
    };
    // Definisco la modalita per la modale
    const modalita = NPModalitaModale.conferma_prenotazione;
    // Creo un clone dell'oggetto originale per sganciare modifiche tramite referenza
    const elaborazioneClone = cloneDeep(elaborazione);

    // Definisco le configurazioni per la gestione delle modali
    const bmConfigs: IBollettiniModalConfigs = {
      callbacks: { onConfirm },
      modalMap: this.nuovaPrenotazione.modalMap,
      dataModal: { modalita, elaborazione: elaborazioneClone },
    };

    // Richiamo il servizio che aprirà la modale
    this._bollettiniModal.openPrenotazioneModal(bmConfigs);
  }

  /**
   * Funzione che viene richiamata quando la modale di conferma prenotazione viene chiusa con conferma.
   * @param elaborazione Elaborazione che definisce le informazioni restituite dalla modale.
   */
  private onModalCPConfirm(elaborazione: ElaborazioneVo) {
    // Lancio l'inserimento dell'elaborazione
    this._bollettini.updateElaborazione(elaborazione).subscribe({
      next: (e: ElaborazioneVo) => {
        // Oggetto aggiunto aggiorno l'alert
        this.onServiziSuccess();
        // Effettuo un refresh dei dati
        this.refreshTabellaElaborazione();
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco il messaggio d'errore inaspettato
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione invocata alla pressione del pulsante "conferma" della tabella.
   * La funzione aprirà una modale per la conferma di annullamento di una prenotazione.
   * @param elaborazione Elaborazione con i dati della configurazione da confermare.
   */
  apriModalAP(elaborazione: ElaborazioneVo) {
    // Variabili di comodo
    const onConfirm = () => {
      // Modifico l'elaborazione e imposto lo stato di "annulla richiesta"
      elaborazione.stato_elabora = this.annullaRichiesta;
      // Richiamo la funzione di gestione per la chiusura
      this.onModalAPConfirm(elaborazione);
    };

    // Definisco le configurazioni per la gestione della modale
    const callbacks: ICallbackDataModal = { onConfirm };
    // Richiamo il servizio che aprirà la modale
    this._bollettiniModal.openAnnullaPrenotazioneModal(callbacks);
  }

  /**
   * Funzione che viene richiamata quando la modale di annulla prenotazione viene chiusa con conferma.
   * @param elaborazione Elaborazione che definisce le informazioni restituite dalla modale.
   */
  private onModalAPConfirm(elaborazione: ElaborazioneVo) {
    // Lancio l'inserimento dell'elaborazione
    this._bollettini.updateElaborazione(elaborazione).subscribe({
      next: (e: ElaborazioneVo) => {
        // Oggetto aggiunto aggiorno l'alert
        this.onServiziSuccess();
        // Effettuo un refresh dei dati
        this.refreshTabellaElaborazione();
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco il messaggio d'errore inaspettato
        this.onServiziError(e);
      },
    });
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo per l'idAmbito.
   * @returns number con l'idAmbito.
   */
  get idAmbito() {
    return this._user.idAmbito;
  }

  /**
   * Getter di comodo che ritorna se la configurazione per il dropdown button esiste.
   */
  get existNPConfig() {
    return this.nuovaPrenotazione !== undefined;
  }

  /**
   * Getter di comodo per sapere se ci sono informazioni per i bollettini.
   */
  get existBollettini() {
    return this.tableBollettini?.source?.length > 0;
  }

  /**
   * Getter di comodo che restituisce, se esiste, l'istanza view child di: riscaBollettazioneTable
   */
  get bollettazioneTable() {
    return this.riscaBollettazioneTable;
  }

  /**
   * Getter di comodo per lo stato elaborazione: ANNULLA_RICHIESTA
   */
  get annullaRichiesta(): StatoElaborazioneVo {
    // Definisco lo stato
    const stato = CodiciStatiElaborazione.ANNULLA_RICHIESTA;
    // Richiamo la funzione del servizio passando il codice per l'oggetto ANNULLA_RICHIESTA
    return this._bollettini.statoElaborazioneByCode(stato);
  }
}
