import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { cloneDeep, isNil, pickBy } from 'lodash';
import { Subscription } from 'rxjs';
import { LoggerService } from 'src/app/core/services/logger.service';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from 'src/app/shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { RiscaErrorsMap } from 'src/app/shared/utilities/classes/errors-maps';
import { AnnualitaSDVo } from '../../../../core/commons/vo/annualita-sd-vo';
import { PraticaEDatiTecnici } from '../../../../core/commons/vo/pratica-vo';
import { IUsoLeggeVo } from '../../../../core/commons/vo/uso-legge-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { RiscaLockPraticaService } from '../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IRiscaFormTreeParent,
  RiscaAlertConfigs,
  RiscaInfoLevels,
  RiscaServerError,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { QuadriTecniciSDComponent } from '../../components/quadri-tecnici/components/middlewares/quadri-tecnici-sd/quadri-tecnici-sd.component';
import { DTAmbienteASDData } from '../../components/quadri-tecnici/components/middlewares/quadri-tecnici-sd/utilities/quadri-tecnici-sd.component.interfaces';
import { DatiTecniciEventsService } from '../../components/quadri-tecnici/services/dati-tecnici/dati-tecnici-events.service';
import { AnnualitaConsts } from '../../consts/dati-contabili/annualita.consts';
import { IAnnualitaModalConfigs } from './utilities/annualita-modal.interfaces';

@Component({
  selector: 'annualita-modal',
  templateUrl: './annualita-modal.component.html',
  styleUrls: ['./annualita-modal.component.scss'],
})
export class AnnualitaModalComponent
  extends RiscaFormParentComponent
  implements OnInit
{
  /** Oggetto contenente una serie di costanti per la gestione degli errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  AI_C = AnnualitaConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** IAnnualitaInsModalConfigs contenente i parametri per la modal. */
  @Input() dataModal: IAnnualitaModalConfigs;

  /** ViewChild collegato al componente: quadri-tecnici. */
  @ViewChild('appQuadriTecniciSD') appQTSD: QuadriTecniciSDComponent;

  /** Boolean che definisce se la pagina dei dati tecnici deve risultare bloccata. */
  AEA_DTDisabled: boolean = false;

  /** Subscription collegato all'evento: errore durante la chiamata ai servizi del componente dato tecnico. */
  private onServiziAnnualitaError$: Subscription;
  /** Subscription che intercetta l'evento aggiornamento con "pulizia" dati dal componente. */
  private onClean$: Subscription;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    public activeModal: NgbActiveModal,
    logger: LoggerService,
    private _datiTecniciEvents: DatiTecniciEventsService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit(): void {
    // Lancio l'init del componente
    this.initComponente();
  }

  ngOnDestroy(): void {
    // Rimuovo gli ascoltatori
    super.ngOnDestroy();
    this.onServiziAnnualitaError$?.unsubscribe();
    this.onClean$?.unsubscribe();
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
    // Effettuo il setup del servizio di submit handler
    this.setupFormSubmitHandlerService();
    // Funzione che recupera la configurazione d'abilitazione della form
    this.setupDTDisabled();
    // Lancio la funzione di setup per gli ascoltatori
    this.setupListeners();
  }

  /**
   * Funzione di setup per il servizio RiscaFormSubmitHandlerService.
   */
  private setupFormSubmitHandlerService() {
    // Definisco la chiave per il parent
    const parent = this.AI_C.FORM_KEY_PARENT;
    // Definisco le chiavi per i figli
    const children = [this.AI_C.FORM_KEY_CHILD_DTSD];

    // Richiamo il super
    this.setFormsSubmitHandler(parent, children);
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  setupDTDisabled() {
    // Recupero la chiave per la configurazione della form
    const dtKey = this.AEAK_C.PAGINA_DATI_TECNICI;
    const detSDKey = this.AEAK_C.DET_STATO_DEB;

    // Verifico se la configurazione di accesso agli elementi è attivo
    let AEA_DTDisabled: boolean;
    let AEA_detSDDisabled: boolean;
    AEA_DTDisabled = this._accessoElementiApp.isAEADisabled(dtKey);
    AEA_detSDDisabled = this._accessoElementiApp.isAEADisabled(detSDKey);

    // Definisco la configurazione di disabilitazione per i dati tecnici
    this.AEA_DTDisabled = AEA_DTDisabled || AEA_detSDDisabled;
  }

  /**
   * Funzione di setup che imposta gli ascoltatori per gli eventi applicativi.
   */
  private setupListeners() {
    this.onServiziAnnualitaError$ =
      this._datiTecniciEvents.onServiziAnnualitaError$.subscribe({
        next: (e: RiscaServerError) => {
          // Verifico se esiste un oggetto
          if (e) {
            // Esiste, creo un alert e lo visualizzo
            let alertErr: RiscaAlertConfigs;
            alertErr = this._riscaAlert.createAlertFromServerError(e);
            // Assegno l'oggetto creato
            this.alertConfigs = alertErr;
            // #
          }
        },
      });

    this.onClean$ = this._datiTecniciEvents.onClean$.subscribe({
      next: () => {
        // Lancio il reset per eventuali alert
        this.resetAlertConfigs();
      },
    });
  }

  /**
   * ###########################
   * FUNZIONI DI INIT COMPONENTE
   * ###########################
   */

  /**
   * Funzione di init del componente.
   */
  private initComponente() {
    // Lancio l'init della modalita
    this.modalita = this.dataModal?.modalita;
  }

  /**
   * ######################
   * FUNZIONI PER LA MODALE
   * ######################
   */

  /**
   * Funzione che effettua la close (e quindi la resolved) della modale.
   */
  modalConfirm(payload?: any) {
    // Close della modale
    this.activeModal.close(payload);
  }

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalCancel(payload?: any) {
    // Dismiss della modale
    this.activeModal.dismiss(payload);
  }

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalClose(payload?: any) {
    // Dismiss della modale
    this.activeModal.dismiss(payload);
  }

  /**
   * ###############################
   * GESTIONE INSERIMENTO ANNUALITA'
   * ###############################
   */

  /**
   * Funzione invocata al click del pulsante per l'annullamento delle modifiche al rimborso.
   * La funzione invocherà il reset delle informazioni dipendentemente dalla modalità di gestione della modale.
   */
  onAnnulla() {
    // Richiamo il reset dei dati tecnici
    this.appQTSD.onFormReset();
  }

  /**
   * Funzione invocata al click del pulsante per la conferma dell'inserimento dell'annualità.
   */
  onConferma() {
    // Richiamo il submit dei dati tecnici
    this.appQTSD.onFormSubmit();
  }

  /**
   * #############################################################
   * GESTIONE LOGICA PER IL SUBMIT DELLE FORM DEI COMPONENTI FIGLI
   * #############################################################
   */

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsValid.
   * @param formsValid IRiscaFormTreeParent con i dati validi.
   * @override
   */
  formsValid(formsValid: IRiscaFormTreeParent) {
    // Resetto l'alert dei messaggi
    this.resetAlertConfigs();

    // Recupero la chiave per l'annualita
    const K_ANN = this.AI_C.FORM_KEY_CHILD_DTSD;
    // Recupero la mappa dati dei componenti
    const mapData = this.getRiscaFormTreedData(formsValid);
    // Recupero il dato dal componente annualita
    const annData: DTAmbienteASDData = mapData.get(K_ANN);

    // Richiamo la funzione di preparazione dei dati da ritornare
    this.prepareModalConfirm(annData);
  }

  /**
   * Funzione di preparazione dati prima della conferma della modale.
   * @param annData DTAmbienteASDData con l'oggetto generato dal dato tecnico.
   */
  private prepareModalConfirm(annData: DTAmbienteASDData) {
    // Verifico se mi trovo in modifica
    if (this.modifica && this.annualita) {
      // Unisco i dati generati dell'annualità con l'annualità passata come configurazione
      const { annualita } = annData;

      // Rimuovo dall'annualità generata le proprietà null o undefined
      let annCLN: Partial<AnnualitaSDVo>;
      annCLN = pickBy(annualita, (p: any) => !isNil(p));
      // Creo una copia dell'annualità in input
      const annMRG: AnnualitaSDVo = cloneDeep(this.annualita);
      // Unisco le proprietà dell'annualità iniziale con quella generata
      this._riscaUtilities.mergeDataWith<AnnualitaSDVo>(annMRG, annCLN);

      // Aggiorno la configurazione da ritornare con l'annualità finale
      annData.annualita = annMRG;
    }

    // Chiudo la modale e ritorno l'annualità generata
    this.modalConfirm(annData);
  }

  /**
   * ####################################################
   * FUNZIONI DI GESTIONE EVENTI PER JSON REGOLA MANCANTE
   * ####################################################
   */

  /**
   * Funzione collegata all'evento del componente per: json_regola_mancante per un uso annualità.
   * @param usoJRM UsoLeggeVo con le informazioni che risultano in errore.
   */
  onUsoAnnualitaJRM(usoJRM: IUsoLeggeVo) {
    // Lancio l'errore per la regola mancante
    const code = RiscaNotifyCodes.C003;
    // Recupero il messaggio per gruppi non trovati
    const jrmMsg = [this._riscaMessages.getMessage(code)];
    // Variabili di comodo
    const a = this.alertConfigs;
    a.allowAlertClose = true;
    const e = RiscaInfoLevels.danger;
    // Aggiorno l'alert
    this.aggiornaAlertConfigs(a, jrmMsg, e);
  }

  /**
   * Funzione collegata all'evento del componente per: json_regola_mancante per gli usi annualità.
   * @param usoJRM UsoLeggeVo[] con le informazioni che risultano in errore.
   */
  onUsiAnnualitaJRM(usiJRM: IUsoLeggeVo[]) {
    // Lancio l'errore per la regola mancante
    const code = RiscaNotifyCodes.C003;
    // Recupero il messaggio per gruppi non trovati
    const jrmMsg = [this._riscaMessages.getMessage(code)];
    // Variabili di comodo
    const a = this.alertConfigs;
    a.allowAlertClose = true;
    const e = RiscaInfoLevels.danger;
    // Aggiorno l'alert
    this.aggiornaAlertConfigs(a, jrmMsg, e);
  }

  /**
   * Funzione collegata all'evento del componente per: json_regola_mancante per il canone annualità.
   * @param usoJRM any con le informazioni che risultano in errore.
   */
  onCanoneAnnualitaJRM(usoJRM: any) {
    // Lancio l'errore per la regola mancante
    const code = RiscaNotifyCodes.C003;
    // Recupero il messaggio per gruppi non trovati
    const jrmMsg = [this._riscaMessages.getMessage(code)];
    // Variabili di comodo
    const a = this.alertConfigs;
    a.allowAlertClose = true;
    const e = RiscaInfoLevels.danger;
    // Aggiorno l'alert
    this.aggiornaAlertConfigs(a, jrmMsg, e);
  }

  /**
   * Funzione collegata all'evento del componente per la gestione della pulizia dei dati e ricalcolo informazioni.
   * @param data any con le informazioni comunicate dal componente.
   */
  onClean(data: any) {
    // Resetto possibili alert
    this.resetAlertConfigs();
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo per recuperare le informazioni dalla configurazione in input: statoDebitorio.
   */
  get statoDebitorio() {
    // Recupero la proprietà dalla configurazione
    return this.dataModal?.statoDebitorio;
  }

  /**
   * Getter di comodo per recuperare le informazioni dalla configurazione in input: annualita.
   */
  get annualita() {
    // Recupero la proprietà dalla configurazione
    return this.dataModal?.annualita;
  }

  /**
   * Getter di comodo per recuperare le informazioni dalla configurazione in input: praticaEDT.pratica.
   */
  get pratica() {
    // Recupero la proprietà dalla configurazione
    return this.dataModal?.praticaEDT?.pratica;
  }

  /**
   * Getter di comodo per recuperare le informazioni dalla configurazione in input: praticaEDT.
   * @returns PraticaEDatiTecnici con le informazioni relative ai dati tecnici.
   */
  get praticaEDatiTecnici(): PraticaEDatiTecnici {
    // Recupero la proprietà dalla configurazione
    return this.dataModal?.praticaEDT;
  }

  /**
   * Getter di comodo per recuperare le informazioni dalla configurazione in input: praticaEDT.pratica.data_inserimento.
   */
  get dataInserimentoPratica() {
    // Recupero la proprietà dalla configurazione
    return this.praticaEDatiTecnici?.datiTecnici?.riscossione?.data_inserimento;
  }

  /**
   * Getter di comodo per recuperare l'id_componente_dt della pratica.
   */
  get idComponenteDt() {
    // A seconda della modalita del componente recupero l'id_componente_dt di riferimento
    if (this.inserimento) {
      // In inserimento recupero l'id della pratica
      return this.pratica?.id_componente_dt;
      // #
    } else {
      // In modifica recupero l'id dell'annualità
      return this.annualita?.id_componente_dt;
    }
  }

  get rateoPrimaAnnualita() {
    // Recupero la proprietà dalla configurazione
    return this.dataModal?.rateoPrimaAnnualita ?? false;
  }

  /**
   * Getter di comodo per il recupero del nome del titolo della modale.
   * @returns string con il titolo della modale.
   */
  get modalTitle(): string {
    // Recupero il titolo in base alla gestione della modale
    if (this.inserimento) {
      // Inserimento
      return this.AI_C.ANNUALITA_MODAL_TITLE_NUOVA;
      // #
    } else if (this.modifica) {
      // Modifica
      return this.AI_C.ANNUALITA_MODAL_TITLE_MODIFICA;
      // #
    } else {
      // Generico
      return this.AI_C.ANNUALITA_MODAL_TITLE;
      // #
    }
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.isPraticaLocked || this.AEA_DTDisabled;
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
