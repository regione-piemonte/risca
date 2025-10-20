import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LoggerService } from 'src/app/core/services/logger.service';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from 'src/app/shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { RiscaErrorsMap } from 'src/app/shared/utilities/classes/errors-maps';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { AppActions } from '../../../../shared/utilities';
import { CalcoloInteressiComponent } from '../../components/dati-contabili/calcolo-interessi/calcolo-interessi.component';
import { CalcoloCanoneConsts } from '../../consts/dati-contabili/calcolo-interessi.consts';
import { ICalcoloInteressiModalConfigs } from './utilities/calcolo-interessi-modal.interfaces';

@Component({
  selector: 'calcolo-interessi-modal',
  templateUrl: './calcolo-interessi-modal.component.html',
  styleUrls: ['./calcolo-interessi-modal.component.scss'],
})
export class CalcoloInteressiModalComponent
  extends RiscaFormParentComponent
  implements OnInit
{
  /** Oggetto contenente una serie di costanti per la gestione degli errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  AI_C = CalcoloCanoneConsts;

  /** Titolo della modale. */

  /** Titolo della modale */
  modalTitle: string = this.AI_C.TITOLO_CALCOLO_INTERESSI;

  /** IAnnualitaInsModalConfigs contenente i parametri per la modal. */
  @Input() dataModal: ICalcoloInteressiModalConfigs;

  /** ViewChild collegato al componente: quadri-tecnici. */
  @ViewChild('appCalcoloInteressi') appCIST: CalcoloInteressiComponent;

  /**
   * Costruttore.
   */
  constructor(
    public activeModal: NgbActiveModal,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages,
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit(): void {
    // Lancio l'init del componente
    this.initComponente();
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
   * ###########################
   * FUNZIONI DI INIT COMPONENTE
   * ###########################
   */

  /**
   * Funzione di init del componente.
   */
  private initComponente() {
    // Lancio l'init della modalita. Non serve ma non posso non metterla, quindi ne metto una di default
    this.modalita = AppActions.inserimento;
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
   * #############################################################
   * GESTIONE LOGICA PER IL SUBMIT DELLE FORM DEI COMPONENTI FIGLI
   * #############################################################
   */

  /**
   * Funzione invocata al click del pulsante per l'annullamento delle modifiche al rimborso.
   * La funzione invocherà il reset delle informazioni dipendentemente dalla modalità di gestione della modale.
   */
  onAnnulla() {
    // Richiamo il reset dei dati tecnici
    this.appCIST.onFormReset();
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
}
