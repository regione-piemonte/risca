import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ComponenteGruppo } from '../../../../core/commons/vo/componente-gruppo-vo';
import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { FormCercaSoggettoDatiGruppiTable } from '../../../../shared/classes/risca-table/cerca-titolare-modal/cerca-soggetto.gruppi.table';
import { FCSDatiSoggettoTable } from '../../../../shared/classes/risca-table/cerca-titolare-modal/cerca-soggetto.soggetti.table';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaEventEmitterService } from '../../../../shared/services/risca/risca-event-emitter.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import {
  IFormCercaSoggetto,
  RiscaServerError,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { RiscaInfoLevels } from '../../../../shared/utilities/enums/utilities.enums';
import { DatiAnagraficiConsts } from '../../consts/dati-anagrafici/dati-anagrafici.consts';
import { ICercaTitolareModalConfigs } from './utilities/cerca-titolare-modal.interfaces';

/**
 * Modal per la datiSoggetto di un titolare.
 */
@Component({
  selector: 'cerca-titolare-modal',
  templateUrl: './cerca-titolare-modal.component.html',
  styleUrls: ['./cerca-titolare-modal.component.scss'],
})
export class CercaTitolareModalComponent
  extends RiscaFormParentComponent
  implements OnInit
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti per la sezione dei dati anagrafici. */
  DA_C = new DatiAnagraficiConsts();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** Oggetto contenente i parametri per la modal. */
  @Input() dataModal: ICercaTitolareModalConfigs;

  /** Boolean che definisce se è possibile inserire un nuovo soggetto. */
  gestioneSoggettiON: boolean;
  /** Boolean che pilota la costruzione della modale, rimuovendo i gruppi se non sono accessibili. */
  gestioneGruppiON: boolean;
  /** Boolean che definisce lo stato per la check: visualizza gruppi. */
  visualizzaGruppi: boolean;

  /** Oggetto IFormCercaSoggetto contenente le informazioni del soggetto cercato. */
  ricerca: IFormCercaSoggetto;
  /** Oggetto FormCercaSoggettoDatiSoggettoTable che conterrà configurazioni per la tabella dei soggetti. */
  tableSoggetti: FCSDatiSoggettoTable;
  /** Oggetto FormCercaSoggettoDatiGruppiTable che conterrà configurazioni per la tabella dei gruppi. */
  tableGruppi: FormCercaSoggettoDatiGruppiTable;

  /** Gruppo che definisce il gruppo selezionato dalla tabella. */
  private gruppoSelezionato: Gruppo;

  /**
   * Costruttore
   */
  constructor(
    public activeModal: NgbActiveModal,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaEE: RiscaEventEmitterService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages
    );
  }

  ngOnInit() {
    // Init del componente
    this.initComponente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di setup per le informazioni del componente.
   */
  private initComponente() {
    // Effettuo il setup per la gestione soggetti
    this.initGestioneSoggetti();
    // Effettuo il setup per la visualizzazione/gestione dei gruppi
    this.initGestioneGruppi();
  }

  /**
   * Funzione di init per la gestione dei soggetti.
   */
  private initGestioneSoggetti() {
    // Verifico se esiste la configurazione sui soggetti
    if (this.dataModal?.soggettiAbilitati !== undefined) {
      // Effettuo il setup per l'inserimento soggetto
      this.gestioneSoggettiON = this.dataModal.soggettiAbilitati;
      // #
    } else {
      // Effettuo il setup per l'inserimento soggetto
      this.gestioneSoggettiON = true;
    }
  }

  /**
   * Funzione di setup per le la visualizzazione dei gruppi.
   */
  private initGestioneGruppi() {
    // Verifico se esiste la configurazione sui gruppi
    if (this.dataModal?.gruppiAbilitati !== undefined) {
      // Effettuo il setup per la visualizzazione/gestione dei gruppi
      this.visualizzaGruppi = true;
      // Effettuo il setup del flag di gestione dei gruppi
      const { gruppiAbilitati, soggettiAbilitati } = this.dataModal || {};
      this.gestioneGruppiON = gruppiAbilitati && soggettiAbilitati;
      // #
    } else {
      // Effettuo il setup per la visualizzazione/gestione dei gruppi
      this.visualizzaGruppi = false;
      // Effettuo il setup del flag di gestione dei gruppi
      this.gestioneGruppiON = false;
    }
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione di toggle per il check: visualizza gruppi.
   */
  toggleVisualizzaGruppi() {
    // Toggle del boolean
    this.visualizzaGruppi = !this.visualizzaGruppi;

    // Rimuovo la selezione del gruppo
    this.gruppoSelezionato = undefined;
    // Rimuovo la selezione nella tabella
    this.tableGruppi?.setAllElementsSelection();
  }

  /**
   * Funzione agganciata al componente di datiSoggetto soggetto.
   * Viene invocata quando la ricerca viene avviata.
   * @param params
   */
  onSoggettoRicerca(params: any) {
    // Richiamo il reset della scheda
    this.resetNuovaRicerca();
  }

  /**
   * Funzione agganciata al componente di datiSoggetto soggetto.
   * Viene invocata quando la ricerca viene conclusa.
   * @param ricerca IFormCercaSoggetto contente i dati ottenuti dalla ricerca del soggetto.
   */
  onSoggettoCercato(ricerca: IFormCercaSoggetto) {
    // Resetto l'alert
    this.resetAlertConfigs(this.alertConfigs);

    // Aggiorno i dati della ricerca
    this.ricerca = ricerca;

    // Verifico il soggetto
    if (this.verificaDatiSoggetto(ricerca)) {
      // Recupero i dati anagrafici
      const { soggetto, fonte_ricerca } = ricerca?.ricercaSoggetto || {};

      // Genero la tabella dei soggetti
      this.setSoggettoTable(soggetto, fonte_ricerca);
      // Genero la tabella dei soggetti
      this.setGruppiTable(soggetto);
    }
  }

  /**
   * Funzione che verifica i dati del soggetto ritornata dalla ricerca.
   * @param ricerca IFormCercaSoggetto con i dati del soggetto.
   * @returns boolean se i dati del soggetto sono validi.
   */
  private verificaDatiSoggetto(ricerca: IFormCercaSoggetto): boolean {
    // Recupero il soggetto dalla ricerca
    const { soggetto } = ricerca?.ricercaSoggetto || {};

    // Verifico che esista il valore di ritorno
    if (!soggetto) {
      // Gestisco i messaggi d'errore
      this.gestisciAlertNoSoggetto();
      // Dato non valido
      return false;
    }

    // Dati validi
    return true;
  }

  /**
   * Funzione agganciata all'evento di errore della form di ricerca.
   * @param e RiscaServerError con l'oggetto d'errore generato dal server.
   */
  onSoggettoError(e: RiscaServerError) {
    // Definisco un oggetto senza dati per attivare l'alert
    this.ricerca = {};

    // Errore generico, gestisco normalmente
    this.onServiziError(e);
  }

  /**
   * Funzione di supporto per gestire l'alert quando la ricerca non trova il soggetto.
   */
  private gestisciAlertNoSoggetto() {
    // Variabili per il set dell'alert
    let code = '';
    let type = undefined;
    const a = this.alertConfigs;

    // Verifico se è attivo l'inserimento per i soggetti
    if (this.gestioneSoggettiON) {
      // Definisco il codice per inserire il soggetto
      code = RiscaNotifyCodes.A008;
      // Definisco il tipo di alert
      type = RiscaInfoLevels.warning;
      // #
    } else {
      // Definisco il codice che non permette l'inserimento soggetto
      code = RiscaNotifyCodes.I001;
      // Definisco il tipo di alert
      type = RiscaInfoLevels.warning;
    }

    // Recupero il messaggio di warning per l'utente
    const mNoSogg = this._riscaMessages.getMessage(code);
    // Setto le configurazioni dell'alert
    this.aggiornaAlertConfigs(a, [mNoSogg], type);
  }

  /**
   * Funzione che effettua l'istanza della tabella soggetti.
   * @param soggetto SoggettoVo contenente i dati anagrafici del titolare trovato.
   * @param fonteRicerca string che definisce la fonte di ricerca del soggetto.
   */
  private setSoggettoTable(soggetto: SoggettoVo, fonteRicerca?: string) {
    // Genero la tabella
    this.tableSoggetti = new FCSDatiSoggettoTable({
      soggetti: [soggetto],
      fonteRicerca,
    });
  }

  /**
   * Funzione che effettua l'istanza della tabella dei gruppi.
   * @param soggetto SoggettoVo contenente i dati dei gruppi del titolare trovato.
   */
  private setGruppiTable(soggetto: SoggettoVo) {
    // Estraggo i gruppi e l'id_soggetto
    const idS = soggetto.id_soggetto;
    let gruppi = (soggetto.gruppo_soggetto as Gruppo[]) || [];

    // Filtro i gruppi ed estraggo solo quelli dove il soggetto è titolare
    gruppi = gruppi.filter((g: Gruppo) => {
      // Cerco il capogruppo (is soggetto capogruppo)
      const isSCG = g.componenti_gruppo?.find((cg: ComponenteGruppo) => {
        // Verifico il flag capogruppo e l'id
        const flgCG = cg.flg_capo_gruppo;
        const idSCG = cg.id_soggetto;
        // Ritorno la condizione
        return flgCG && idS === idSCG;
      });
      // Ritorno il gruppo se il soggetto è capogruppo
      return isSCG;
    });

    // Genero la tabella
    this.tableGruppi = new FormCercaSoggettoDatiGruppiTable(gruppi);
  }

  /**
   * Funzione invocata alla pressione del pulsante ANNULLA all'interno dell'interfaccia.
   * Verrà resettata la tabella dei risultati per la ricerca soggetto.
   */
  resetNuovaRicerca() {
    // Resetto l'alert
    this.aggiornaAlertConfigs(this.alertConfigs);
    // Resetto la ricerca
    this.ricerca = undefined;
    // Resetto le tabelle
    this.tableSoggetti = undefined;
    this.tableGruppi = undefined;
  }

  /**
   * Funzione invocata alla pressione del pulsante ANNULLA all'interno dell'interfaccia.
   * Verrà resettata la tabella dei risultati per la ricerca soggetto.
   */
  resetRicercaSoggetto() {
    // Resetto l'alert
    this.aggiornaAlertConfigs(this.alertConfigs);
    // Resetto la ricerca
    this.ricerca = undefined;
    // Resetto le tabelle
    this.tableSoggetti = undefined;
    this.tableGruppi = undefined;
    // Imposto il flag a true per la visualizzazione gruppi
    this.visualizzaGruppi = true;

    // Emetto l'evento custom di reset cerca soggetto.
    this._riscaEE.emitter.next({ key: this.C_C.REE_KEY_REST_FORM_CERCA_SOG });
  }

  /**
   * Funzione che collega il gruppo scelto nella tabella dei gruppi alla pratica.
   * @param dettaglio RiscaTableDataConfig<Gruppo> contenente i dati della riga.
   */
  public collegaGruppoAPratica(dettaglio: RiscaTableDataConfig<Gruppo>) {
    // Definisco il gruppo a livello di componente
    this.gruppoSelezionato = dettaglio.original;
  }

  /**
   * ################################
   * FUNZIONI PER GESTIONE DELLA FORM
   * ################################
   */

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalDismiss() {
    // Dismiss della modale
    this.activeModal.dismiss();
  }

  /**
   * Funzione che effettua la close (e quindi la resolved) della modale.
   */
  modalConfirm(action: string) {
    // Verifico se è stato selezionato un gruppo
    if (this.gruppoSelezionato) {
      // Aggiungo il dato all'oggetto ricerca
      this.ricerca.gruppoSelezionato = this.gruppoSelezionato;
    }
    // Close della modale
    this.activeModal.close({ action, data: this.ricerca });
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter che verifica che la ricerca sia stata effettuata e abbia dati anagrafici.
   */
  get soggettoTrovato() {
    // recupero i dati anagrafici
    const { soggetto } = this.ricerca?.ricercaSoggetto || {};
    // Verifico se la ricerca è stata effettuata
    const existR = soggetto !== undefined;

    // Ritorno la condizione di verifica
    return existR;
  }

  /**
   * Getter che verifica se la tabella dei soggetti è da visualizzare.
   */
  get mostraTabellaSoggetto() {
    // Verifico se la ricerca è stata effettuata
    const existR = this.soggettoTrovato;
    // Verifico se la tabella oggetti esiste e ha dati
    const existTS = this.tableSoggetti?.source?.length > 0;

    // Ritorno la condizione di verifica
    return existR && existTS;
  }

  /**
   * Getter che verifica se la tabella dei soggetti è da visualizzare.
   */
  get mostraTabellaGruppi() {
    // Verifico se la ricerca è stata effettuata
    const existR = this.soggettoTrovato;
    // Verifico se la tabella oggetti esiste e ha dati
    const existTS = this.tableGruppi?.source?.length > 0;
    // Verifico se la visualizzazione dei gruppi è attiva
    const okGruppi = this.visualizzaGruppi && this.gestioneGruppiON;
    // Verifico se anche la configurazione di gestione soggetti è attiva
    const okSogg = this.gestioneSoggettiON;

    // Ritorno la condizione di verifica
    return existR && existTS && okGruppi && okSogg;
  }

  /**
   * Getter di comodo per la gestione soggetti OFF.
   */
  get gestioneSoggettiOFF() {
    return !this.gestioneSoggettiON;
  }

  /**
   * Getter di comodo per la gestione gruppi OFF.
   */
  get gestioneGruppiOFF() {
    return !this.gestioneSoggettiON;
  }
}
