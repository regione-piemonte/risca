import { Component, Input, OnInit } from '@angular/core';
import { Subscription } from 'rxjs/index';
import { RicercaPaginataResponse } from 'src/app/core/classes/http-helper/http-helper.classes';
import { DocumentiAllegatiTable } from 'src/app/shared/classes/risca-table/documenti-allegati/documenti-allegati.table';
import { RiscaTableDataConfig } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.classes';
import { IRiscaTableACEvent } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.interfaces';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  IRiscaTabChanges,
  RiscaInfoLevels,
  RiscaIstanzePratica,
  RiscaTablePagination,
} from 'src/app/shared/utilities';
import { RiscaNotifyCodes } from 'src/app/shared/utilities/enums/risca-notify-codes.enums';
import { PraticaEDatiTecnici } from '../../../../core/commons/vo/pratica-vo';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaUtilitiesComponent } from '../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { DocumentiAllegatiConsts } from '../../consts/documenti-allegati/documenti-allegati.consts';
import { DocumentiAllegatiModalService } from '../../service/documenti-allegati-modal/documenti-allegati-modal.service';
import { DocumentiAllegatiService } from '../../service/documenti-allegati/documenti-allegati.service';
import { InserisciPraticaService } from '../../service/inserisci-pratica/inserisci-pratica.service';
import {
  AllegatoVo,
  DocumentoAllegatoVo,
} from './../../../../core/commons/vo/documento-allegato-vo';
import { RiscaServerError } from './../../../../shared/utilities/classes/utilities.classes';

@Component({
  selector: 'documenti-allegati',
  templateUrl: './documenti-allegati.component.html',
  styleUrls: ['./documenti-allegati.component.scss'],
})
export class DocumentiAllegatiComponent
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Oggetto con le costanti per la sezione dei documenti allegati. */
  DA_C = DocumentiAllegatiConsts;

  /** Number che definisce l'id riscossione per la ricerca dei documenti allegati- */
  @Input() idRiscossione: number | undefined;

  /** Contiene il testo del campo di ricerca della pagina */
  filtroRicerca: string | undefined;
  /** Label del bottone di ricerca */
  labelBottoneRicerca = { label: 'Cerca' };

  /** Label del bottone di ricerca allegati */
  labelBottoneMostraAltriRisultati = { label: 'Mostra altri risultati' };

  /** boolean che permette di gestire graficamente il caricamento dei dati. */
  isLoading: boolean = false;
  /** Variabile che determina se la ricerca di documenti e allegati ha altri elementi */
  hasMoreElements: boolean = true;

  /** RiscaTablePagination con l'ultima paginazione impostata per la pagina. */
  paginazioneDocs: RiscaTablePagination;

  /** Subscription registrato per il cambio modalità della pratica. */
  onCambioModalita$: Subscription;
  /** Subscription registrato per il cambio dei dati della pratica. */
  onPraticaChange$: Subscription;
  /** Subscription registrato per il cambio tab della pratica. */
  onISTabChanged$: Subscription;

  /** Subscription registrato per il recupero delle informazioni dai servizi per i dati dei documenti allegati. */
  documentiAllegatiSuccess$: Subscription;
  /** Subscription registrato per il recupero delle informazioni dai servizi per gli errori dei documenti allegati. */
  documentiAllegatiErrors$: Subscription;

  /** DocumentiAllegatiTable che conterrà le configurazioni per la tabella dei documenti e allegati. */
  documentiAllegatiTable: DocumentiAllegatiTable;

  /**
   * Costruttore
   */
  constructor(
    private _documentiAllegati: DocumentiAllegatiService,
    private _docAllegatiModal: DocumentiAllegatiModalService,
    private _inserisciPratica: InserisciPraticaService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);

    // Lancio la chiamata alla funzione di setup
    this.setupComponente();
  }

  /**
   * ##################################
   * FUNZIONI DI DESTROY DEL COMPONENTE
   * ##################################
   */

  ngOnDestroy() {
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      this.onCambioModalita$?.unsubscribe();
      this.onPraticaChange$?.unsubscribe();
      this.onISTabChanged$?.unsubscribe();
      this.documentiAllegatiSuccess$?.unsubscribe();
      this.documentiAllegatiErrors$?.unsubscribe();
      // #
    } catch (e) {}
  }

  /**
   * ###############################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di setup che lancia le logiche iniziali del componente.
   */
  private setupComponente() {
    // Lancio la funzione di setup per l'alert che chiede all'utente di aspettare il caricamento dei dati
    this.setupCaricamentoDocs();
  }

  /**
   * Funzione di setup che genera un alert informativo nella quale chiede all'utente di attendere il caricamento dei documenti allegati.
   */
  private setupCaricamentoDocs() {
    // Visualizzo come default un alert che dice all'utente che i dati stanno venendo caricati
    const code: string = RiscaNotifyCodes.I004;
    // Genero un alert partendo dal codice
    this.alertConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  ngOnInit() {
    // Verifico che la modalità sia impostata su "modifica"
    if (this.modifica) {
      // Avvio subito la ricerca all'inizializzazione della componente
      // this.avviaRicercaDocumentiAllegati();
    }

    // Richiamo la funzione di init per i listener del componente
    this.initListeners();
  }

  /**
   * Funzione di init per i listener del componente, per rimanere in ascolto degli eventi per il cambio pagina dei documenti allegati.
   */
  private initListeners() {
    // Registro i listener del componente
    this.onCambioModalita$ =
      this._inserisciPratica.onModalitaChanges$.subscribe({
        next: (modalita: AppActions) => {
          // Aggiorno la modalità del componente dato dalla gestione della pratica
          this.modalita = modalita;
        },
      });

    this.onPraticaChange$ = this._inserisciPratica.onPraticaChanges$.subscribe({
      next: (praticaEDatiTecnici: PraticaEDatiTecnici) => {
        // Aggiorno l'id pratica per il componente
        this.idRiscossione = praticaEDatiTecnici?.pratica?.id_riscossione;

        /**
         * RISCA-ISSUES-37 al cambio della pratica non bisogna far partire la ricerca.
         * Solo nel caso in cui la riscossione viene inserita verrà lanciata la ricerca dei documenti.
         * Il flusso però vale come quello d'inizializzazione, quindi sarà il componente della riscossione a lanciare manualmente la ricerca dei documenti allegati.
         * Gli altri ascoltatori rimarranno in attesa di risposta.
         */
        // // Lancio l'aggiornamento delle informazioni per i documenti allegati
        // this.avviaRicercaDocumentiAllegati();
      },
    });

    this.onISTabChanged$ = this._inserisciPratica.onISTabChanges$.subscribe({
      next: (tabs: IRiscaTabChanges) => {
        // Lancio la funzione di verifica ed eventualmente pulizia dei servizi
        this.isTabMovingAway(tabs);
        // Lancio la funzione di verifica ed eventualmente pulizia dei servizi
        this.isTabMovingHere(tabs);
      },
    });

    this.documentiAllegatiSuccess$ =
      this._documentiAllegati.documentiAllegatiSuccess$.subscribe({
        next: (ricerca: RicercaPaginataResponse<DocumentoAllegatoVo[]>) => {
          // Lancio la funzione specifica per la gestione del dato
          this.onRicercaDocumentiAllegatiResponse(ricerca);
          // #
        },
      });

    this.documentiAllegatiErrors$ =
      this._documentiAllegati.documentiAllegatiErrors$.subscribe({
        next: (error: RiscaServerError) => {
          // Verifico se è ritornato un errore
          if (!error && !this.isDocsLoading) {
            // Non c'è la configurazione
            this.resetAlertConfigs(this.alertConfigs);
            // #
          } else if (error) {
            // Gestisco il messaggio d'errore
            this.alertFromServiziError(error);
            // #
          }
        },
      });
  }

  /**
   * Funzione che crea un'istanza della tabella dei documenti allegati.
   * @param documenti DocumentoAllegatoVo[] lista dei documentiAllegati da mostrare in tabella.
   */
  creaTabellaDocumentiAllegati(documenti: DocumentoAllegatoVo[] = []) {
    // Recupero il messaggio di configurazione per i documenti invalidi
    let docInvalidMsg = this.documentInvalidMessage;
    // Creo la tabella
    this.documentiAllegatiTable = new DocumentiAllegatiTable({
      documenti,
      docInvalidMsg,
    });
  }

  /**
   * Funzione che verifica il cambio di tab della componente padre: inserisci-pratica.
   * Se avviene un cambio verso una nuova tab e i dati contabili erano attivi, allora pulisco i dati dentro il servizio.
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   */
  private isTabMovingAway(tabs: IRiscaTabChanges) {
    // Recupero il tab dei dati contabili
    const docAllegati = RiscaIstanzePratica.documentiAllegati;
    // Verifico se il tab sta cambiando in questo specifico
    const isChanging = this._riscaUtilities.movingIntoTab(tabs, docAllegati);

    // Controllo se ci stiamo quindi spostando dai dati contabili ad un'altra tab
    if (isChanging) {
      // Resetto le informazioni per la tabella di ricerca
      this.paginazioneDocs = undefined;
      this.documentiAllegatiTable = undefined;
    }
  }

  /**
   * Funzione che verifica il cambio di tab della componente padre: inserisci-pratica.
   * Se avviene un cambio verso una nuova tab e i dati contabili erano attivi, allora pulisco i dati dentro il servizio.
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   */
  private isTabMovingHere(tabs: IRiscaTabChanges) {
    // Recupero il tab dei dati contabili
    const docAllegati = RiscaIstanzePratica.documentiAllegati;
    // Verifico se il tab sta cambiando in questo specifico
    const isChanging = this._riscaUtilities.movingIntoTab(tabs, docAllegati);

    // Controllo se ci stiamo quindi spostando su questa specifica sezione e i dati sono già stati caricati
    if (isChanging) {
      // RISCA-ISSUES-37 provo a vedere se ci sono dati scaricati all'interno del servizio, mediante la funzione comune di ricerca
      this.caricamentoDatiDocumenti();
    }
  }

  /**
   * Funzione che gestisce nello specifico lo stato di caricamento dati dei documenti allegati.
   * A seconda dello stato di caricamento, gestisco dati/segnalazioni verso l'utente.
   */
  private caricamentoDatiDocumenti() {
    // Verifico se il caricamento dei documenti allegati è in corso
    if (this.isDocsLoading) {
      // I documenti sono ancora in fase di caricamento, visualizzo un alert informativo
      const code: string = RiscaNotifyCodes.I004;
      // Genero un alert partendo dal codice
      this.alertConfigs = this._riscaAlert.createAlertFromMsgCode({ code });
      // #
    } else if (this.docsLoaded) {
      // I documenti allegati sono stati caricati, recupero le informazioni
      this.avviaRicercaDocumentiAllegati();
      // #
    } else if (this.docsErrors) {
      // I documenti allegati sono andati in errore
      this.alertFromServiziError(this.documentiAllegatiError);
      // #
    }
  }

  /**
   * ###############################
   * GESTIONE DELLA RICERCA ALLEGATI
   * ###############################
   */

  /**
   * Al click del pulsante di avvio della ricerca, chiama il servizio passando il contenuto del filtroRicerca.
   */
  avviaRicercaDocumentiAllegati() {
    // Verifico se esiste la tabella, se non esiste la creo
    this.checkDocumentiAllegatiTable();

    // Verifico se esiste una paginazione locale
    if (!this.paginazioneDocs) {
      // Definisco localmente la paginazione come quella di default definita nella classe della tabella
      this.paginazioneDocs = this.documentiAllegatiTable.getPaginazione();
    }
    // Definisco localmente la paginazione
    const paginazione = this.paginazioneDocs;

    // Faccio la chiamata
    this._documentiAllegati.cambiaPaginaDocEAllLocale(paginazione).subscribe({
      next: (response: RicercaPaginataResponse<DocumentoAllegatoVo[]>) => {
        // Lancio la funzione di gestione della risposta
        this.onRicercaDocumentiAllegatiResponse(response);
        // #
      },
      error: (error: RiscaServerError) => {
        // Gestisco il messaggio d'errore
        this.alertFromServiziError(error);
      },
    });
  }

  /**
   * Funzione che gestisce la risposta ottenuta dalla ricerca dati per i documenti allegati.
   * @param response RicercaPaginataResponse<DocumentoAllegatoVo[]> con le informazioni ottenute dalla ricerca.
   */
  private onRicercaDocumentiAllegatiResponse(
    response: RicercaPaginataResponse<DocumentoAllegatoVo[]>
  ) {
    // Verifico l'input
    if (!response) {
      // Non è stata passata una riposta dati
      return;
    }

    // Aggiorno l'alert
    this.resetAlertConfigs(this.alertConfigs);
    // Richiamo la funzione per gestire i risultati della ricerca
    this.onRicercaDocumentiAllegati(response.sources);
    // Recupero la paginazione
    const p = response.paging;
    // Aggiorno la paginazione
    this.documentiAllegatiTable.updatePaginazioneAfterSearch(p);
    // #
  }

  /**
   * Funzione di supporto per il check di esistenza della tabella dei documenti.
   * Se la tabella non esiste, viene generata.
   */
  private checkDocumentiAllegatiTable() {
    // Creo la tabella se non esiste
    if (!this.documentiAllegatiTable) {
      // Creo l'oggetto della tabella
      this.creaTabellaDocumentiAllegati();
    }
  }

  /**
   * Funzione che gestisce i risultati della ricerca dei documenti e degli allegati.
   * @param listaDocAll DocumentoAllegatoVo[] con i risultati della ricerca.
   */
  private onRicercaDocumentiAllegati(listaDocAll: DocumentoAllegatoVo[]) {
    // Verifico se c'è almeno un record
    if (!listaDocAll || listaDocAll.length == 0) {
      // Definisco il codice del messaggio
      const code = RiscaNotifyCodes.I001;
      // Recupero il messaggio per documenti/allegati non trovati
      const docAllMsg = this._riscaMessages.getMessage(code);
      // Variabili di comodo
      const a = this.alertConfigs;
      const w = RiscaInfoLevels.warning;
      // Aggiorno l'alert
      this.aggiornaAlertConfigs(a, [docAllMsg], w);
    }

    // Inserisco i dati trovati nella tabella dei risultati
    if (this.documentiAllegatiTable) {
      // Se la tabella esiste, vi inserisco gli elementi
      this.documentiAllegatiTable.setElements(listaDocAll);
    } else {
      // Se la tabella non esiste, la creo
      this.creaTabellaDocumentiAllegati(listaDocAll);
    }
  }

  /**
   * Questa funzione ricarica la ricerca cambiando pagina
   * @param paginazione RiscaTablePagination con i dati della paginazione
   */
  cambiaPaginaDocAll(paginazione: RiscaTablePagination) {
    // Aggiorno la paginazione corrente
    this.paginazioneDocs = paginazione;
    // Ricarico i dati
    this.avviaRicercaDocumentiAllegati();
  }

  /**
   * Funzione agganciata all'evento di azione custom da parte della tabella di documenti & allegati.
   * @param customAction IRiscaTableACEvent<any> con la configurazione dati per il pulsante premuto.
   */
  customActionRATable(customAction: IRiscaTableACEvent<any>) {
    // Definisco le chiavi per le azioni
    const ACTION_DOCUMENTO = this.DA_C.ACTION_DOCUMENTO;
    const ACTION_ALLEGATO = this.DA_C.ACTION_ALLEGATI;
    // Verifico quale pulsante è stato premuto, recupero l'action
    const { action } = customAction?.action || {};
    // Recupero dalla configurazione la lista di allegati dalla riga di tabella
    const documentoAllegato: RiscaTableDataConfig<DocumentoAllegatoVo> =
      customAction.row;

    // Verifico quale azione è stata premuta
    switch (action) {
      case ACTION_DOCUMENTO:
        // Download documento
        this.apriDocumento(documentoAllegato);
        break;
      // #
      case ACTION_ALLEGATO: {
        // Richiamo la funzione per visualizzare gli allegati
        this.apriAllegati(documentoAllegato);
        break;
      }
      // #
      default:
        return;
    }
  }

  /**
   * #######################
   * GESTIONE DEGLI ALLEGATI
   * #######################
   */

  /**
   * Apre il documento selezionato
   * @param docAllegati RiscaTableDataConfig<DocumentoAllegatoVo>
   */
  private apriDocumento(
    docAllegati: RiscaTableDataConfig<DocumentoAllegatoVo>
  ) {
    // Svuoto gli errori
    this.aggiornaAlertConfigs();
    // Definisco i parametri per la chiamata
    const idPratica = /* this.test_idPratica ?? */ this.idRiscossione;
    const idClassificazione = docAllegati.original.id_classificazione;
    // Chiamo il servizio
    this.scaricaEApriFile(idPratica, idClassificazione);
  }

  /**
   * Scarica un file in base all'idClassificazione e poi lo apre.
   * @param idRiscossione number che definisce l'id della riscossione per lo scarico e apertura del file.
   * @param idClassificazione id del file, che sia un documento o un allegato.
   */
  scaricaEApriFile(idRiscossione: number, idClassificazione: string) {
    // Definisco i parametri per la chiamata
    const idPratica = /* this.test_idPratica ?? */ idRiscossione;
    const idClass = idClassificazione;

    // Chiamo il servizio per avere il path del file
    this._documentiAllegati.scaricaEApriFile(idPratica, idClass).subscribe({
      error: (error: RiscaServerError) => {
        // Gestisco il messaggio d'errore
        this.alertFromServiziError(error);
      },
    });
  }

  /**
   * Funzione di supporto che gestisce l'apertura degli allegati da parte dell'utente.
   * @param docAllegati RiscaTableDataConfig<DocumentoAllegatoVo> contenente i dati della riga con documento ed allegati selezionata dal'utente.
   */
  private apriAllegati(docAllegati: RiscaTableDataConfig<DocumentoAllegatoVo>) {
    // Svuoto gli errori
    this.aggiornaAlertConfigs();
    // Verifico l'input
    if (!docAllegati) {
      // Non ci sono dati
      return;
    }
    // Chiave di ricerca per l'allegato: idClassificazione
    let { db_key_classificazione } = docAllegati.original;
    // // DECOMMENTARE PER TEST CON DATI DIVERSI DA PRATICA
    // db_key_classificazione = this.test_dbKeyClassificazione ?? db_key_classificazione;

    // Richiamo la modale di selezione degli allegati
    this.scaricaAllegati(db_key_classificazione);
  }

  /**
   * ####################################
   * GESTIONE DELLA MODALE PER L'APERTURA
   * ####################################
   */

  /**
   * Funzione che scarica gli allegati e se esiste un solo allegato, lo salva sulla macchina.
   * Altrimenti apre una modale e visualizza i dati degli allegati per lo scarico singolo.
   * @param dbKeyClassificazione number con l'idClassificazione (key) dei file.
   */
  scaricaAllegati(dbKeyClassificazione: string) {
    // Definisco i parametri per la chiamata | DECOMMENTARE PER TEST CON DATI DIVERSI DA PRATICA
    const idPratica = /* this.test_idPratica ?? */ this.idRiscossione;
    const dbKeyClass =
      /* this.test_dbKeyClassificazione ?? */ dbKeyClassificazione;

    // Prendo gli allegati
    this._documentiAllegati.getAllegati(idPratica, dbKeyClass).subscribe({
      next: (allegati: AllegatoVo[]) => {
        // Se ho un solo elemento, lo apro direttamente
        if (allegati.length == 1) {
          // Definisco i parametri per la chiamata
          const idClassificazione = allegati[0].id_classificazione;
          // Chiamo il servizio per aprire il file
          this.scaricaEApriFile(idPratica, idClassificazione);
          // #
        } else {
          // Richiamo l'apertura del modale passando le configurazioni
          this._docAllegatiModal.openDocAllegatiModal(idPratica, allegati);
        }
      },
      error: (error: RiscaServerError) => {
        // Gestisco il messaggio d'errore
        this.alertFromServiziError(error);
      },
    });
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica se i documenti sono in caricamento.
   * @returns boolean che definisce se lo stato di caricamento dati è in corso.
   */
  get isDocsLoading(): boolean {
    // Ritorno il check dal servizio
    return this._documentiAllegati.isDocsLoading;
  }

  /**
   * Getter che verifica se i documenti allegati sono stati caricati e pronti all'uso.
   * @returns boolean con il risultato del check.
   */
  get docsLoaded(): boolean {
    // Ritorno il check dal servizio
    return this._documentiAllegati.docsLoaded;
  }

  /**
   * Getter che verifica se i documenti allegati non sono stati scaricati e hanno prodotto un errore.
   * @returns boolean con il risultato del check.
   */
  get docsErrors(): boolean {
    // Ritorno il check dal servizio
    return this._documentiAllegati.docsErrors;
  }

  /**
   * Getter che ritorna le informazioni riguardanti lo scarico dei documenti allegati.
   * @returns DocumentoAllegatoVo[] con le informazioni in sessione.
   */
  get documentiAllegati(): DocumentoAllegatoVo[] {
    // Ritorno le informazioni per la ricerca
    return this._documentiAllegati.documentiAllegati;
  }

  /**
   * Getter che ritorna le informazioni riguardanti lo scarico dei documenti allegati.
   * @returns RiscaServerError con le informazioni in sessione.
   */
  get documentiAllegatiError(): RiscaServerError {
    // Ritorno le informazioni per la ricerca
    return this._documentiAllegati.documentiAllegatiError;
  }

  /**
   * Getter di verifica per la tabella dei recapiti alternativi.
   */
  get checkDocAllTable() {
    // Verifico che la tabella esista e abbia dati
    return this.documentiAllegatiTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che recupera il messaggio d'errore da visualizzare come title per un documento non valido.
   * @returns string con il messaggio per documento invalido.
   */
  get documentInvalidMessage(): string {
    // Definisco il codice da recuperare
    const code = RiscaNotifyCodes.I018;
    // Cerco il messaggio tramite servizio
    const msg = this._riscaMessages.getMessage(code);
    // Ritorno il messaggio
    return msg ?? '';
  }
}
