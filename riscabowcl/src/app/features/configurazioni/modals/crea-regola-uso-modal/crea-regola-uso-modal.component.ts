import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RegolaUsoVo } from '../../../../core/commons/vo/regola-uso-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaServerError } from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { CreaRegolaUsoFormComponent } from '../../components/crea-regola-uso-form/crea-regola-uso-form.component';
import { ICreaRegolaUsoForm } from '../../components/crea-regola-uso-form/utilities/crea-regola-uso-form.interfaces';
import { ConfigurazioniService } from '../../services/configurazioni/configurazioni.service';
import { IPostCreaAnnualita } from '../../services/configurazioni/utilities/configurazioni.interfaces';
import { CreaRegolaUsoModalConsts } from './utilities/crea-regola-uso-modal.consts';
import { ICreaRegolaUsoModal, ICreaRegolaUsoModalConfirm } from './utilities/crea-regola-uso-modal.interfaces';

@Component({
  selector: 'crea-regola-uso-modal',
  templateUrl: './crea-regola-uso-modal.component.html',
  styleUrls: ['./crea-regola-uso-modal.component.scss'],
})
export class CreaRegolaUsoModalComponent extends RiscaFormParentComponent implements OnInit, OnDestroy
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti per il componente attuale. */
  CRUM_C = new CreaRegolaUsoModalConsts();

  /** CreaRegolaUsoFormComponent con la referenza al componente che gestisce i dati tramite form. */
  @ViewChild('creaRegolaUsoFormComp')
  creaRegolaUsoFormComp: CreaRegolaUsoFormComponent;

  /** ICreaRegolaUsoModal che definisce i dati di configurazione per la modale. */
  @Input() dataModal: ICreaRegolaUsoModal;

  /**
   * Costruttore.
   */
  constructor(
    public activeModal: NgbActiveModal,
    private _configurazioni: ConfigurazioniService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    private _user: UserService
  ) {
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages
    );

    // // Lancio il setup del componente
    // this.setupComponente();
  }

  ngOnInit() {
    // // Lancio l'init del componente
    // this.initComponente();
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

  // /**
  //  * Funzione di setup per le informazioni del componente.
  //  */
  // private setupComponente() {}

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  // /**
  //  * Funzione di init per le informazioni del componente.
  //  */
  // private initComponente() {}

  /**
   * ###############################################
   * FUNZIONE PER LA GESTIONE DEI PULSANTI IN PAGINA
   * ###############################################
   */

  /**
   * Funzione agganciata al pulsante di annulla.
   * Richiede il reset delle informazioni delle form.
   */
  annullaCreaRegolaUso() {
    // Resetto l'alert
    this.resetAlertConfigs();
    // Lancio il reset del form, tramite referenza del componente
    this.creaRegolaUsoFormComp?.onFormReset();
  }

  /**
   * Funzione agganciata al pulsante di conferma.
   * Richiede il submit/salvataggio della regola uso.
   */
  confermaCreaRegolaUso() {
    // Resetto l'alert
    this.resetAlertConfigs();
    // Lancio il submit del form, tramite referenza del componente
    this.creaRegolaUsoFormComp?.onFormSubmit();
  }

  /**
   * ##################################
   * FUNZIONI PER GESTIONE DELLA MODALE
   * ##################################
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
   * @param payload any con i dati da restitutire al chiamate.
   */
  modalConfirm(payload: ICreaRegolaUsoModalConfirm) {
    // Close della modale
    this.activeModal.close(payload);
  }

  /**
   * ######################################
   * FUNZIONI PER LA GESTIONE DEL FORM DATI
   * ######################################
   */

  /**
   * Funzione collegata al submit della form dati.
   * @param data ICreaRegolaUsoForm con le informazioni submittate dall'utente.
   */
  onFormSubmit(data: ICreaRegolaUsoForm) {
    // Verifico l'input
    if (!data) {
      // Non è stata ritornata nessuna configurazione
      return;
    }

    // Repcupero l'id ambito dal servizio
    const idAmbito: number = this.idAmbito;
    // Estraggo le informazioni generate dal form
    const anno: number = data.annualita;
    const percentuale: number = data.percentualeAggiornamento;

    // Definisco la configurazione per salvare le informazioni
    let postParams: IPostCreaAnnualita;
    postParams = { idAmbito, anno, percentuale };

    // Richiamo il servizio per salvare le informazioni
    this._configurazioni.postCreaAnnualita(postParams).subscribe({
      next: (regole: RegolaUsoVo[]) => {
        // Richiamo la funzione di gestione della risposta
        this.onCreaAnnualita(anno);
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione invocata per gestire il flusso dati a seguito della creazione delle regole per una nuova annualità.
   * @param anno number con l'anno dell'annualità generata.
   */
  private onCreaAnnualita(anno: number) {
    // Recupero il codice del messaggio da visualizzare
    const code = RiscaNotifyCodes.I042;
    // Definisco le informazioni per la gestione della chiusura della modale
    let modalConfirm: ICreaRegolaUsoModalConfirm;
    modalConfirm = { anno, code };

    // Chiudo la modale
    this.modalConfirm(modalConfirm);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter per il recupero dei dati di configurazione dai dati della modale.
   * @returns ICreaRegolaUsoForm con le configurazioni recuperate.
   */
  get creaRegolaUsoForm(): ICreaRegolaUsoForm {
    // Tento di recuperare la configurazione
    return this.dataModal?.creaRegolaUsoForm;
  }

  /**
   * Getter per l'id dell'ambito.
   * @returns number con l'id.
   */
  get idAmbito(): number {
    // Ritorno l'id ambito
    return this._user.idAmbito;
  }
}
