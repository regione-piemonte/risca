import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { cloneDeep } from 'lodash';
import { InteressiDiMoraVo } from 'src/app/core/commons/vo/interessi-di-mora-vo';
import { LoggerService } from 'src/app/core/services/logger.service';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from 'src/app/shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { AppActions, RiscaServerError } from '../../../../shared/utilities';
import { InteressiDiMoraFormComponent } from '../../components/interessi-di-mora-form/interessi-di-mora-form.component';
import { IInteressiDiMoraFormData } from '../../components/interessi-di-mora-form/utilities/interessi-di-mora-form.interfaces';
import { IInteressiLegaliFormData } from '../../components/interessi-legali-form/utilities/interessi-legali-form.interfaces';
import { InteressiDiMoraService } from '../../services/interessi-di-mora/interessi-di-mora.service';
import { InteressiDiMoraModalConsts } from './utilities/interessi-di-mora-modal.const';
import { IInteressiDiMoraFormDataModal } from './utilities/interessi-di-mora-modal.interface';

@Component({
  selector: 'app-interessi-legali-modal',
  templateUrl: './interessi-di-mora-modal.component.html',
  styleUrls: ['./interessi-di-mora-modal.component.scss'],
})
export class InteressiDiMoraModalComponent
  extends RiscaFormParentComponent
  implements OnInit
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti per il componente attuale. */
  IDMM_C = new InteressiDiMoraModalConsts();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** InteressiLegaliFormComponent con la referenza al componente che gestisce i dati tramite form.*/
  @ViewChild('interessiDiMoraFormComp') interessiDiMoraFormComp: InteressiDiMoraFormComponent;

  // TODO @Emmnauel commenta le variabili :3
  /** IInteressiLegaliFormDataModal */
  @Input() dataModal: IInteressiDiMoraFormDataModal;

  /** boolean che contiene la configurazione di gestione per l'abilitazione degli elementi applicativi della pagina. */
  AEA_TDIDisabled: boolean;

  constructor(
    public activeModal: NgbActiveModal,
    private _accessoElementiApp: AccessoElementiAppService,
    protected _logger: LoggerService,
    navigationHelper: NavigationHelperService,
    protected _riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    protected _riscaUtilities: RiscaUtilitiesService,
    riscaMessages: RiscaMessagesService,
    riscaAlert: RiscaAlertService,
    private _interessiDiMoraService: InteressiDiMoraService
  ) {
    super(
      _logger,
      navigationHelper,
      riscaAlert,
      _riscaFormSubmitHandler,
      riscaMessages
    );

    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {}

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
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
   * ##################################
   * FUNZIONI PER GESTIONE DEI PULSANTI DELLA FORM
   * ##################################
   */

  /**
   * Funzione agganciata al pulsante di annulla.
   * Richiede il reset delle informazioni delle form.
   */
  indietroModificaInteresseDiMora() {
    // Resetto l'alert
    this.resetAlertConfigs();
    // Chiudo la modale
    this.modalDismiss();
  }

  /**
   * Funzione agganciata al pulsante di annulla.
   * Richiede il reset delle informazioni delle form.
   */
  annullaModificaInteresseDiMora() {
    // Resetto l'alert
    this.resetAlertConfigs();
    // Chiudo la modale
    this.modalDismiss();
  }

  /**
   * Funzione agganciata al pulsante di conferma.
   * Richiede il submit/salvataggio della regola uso.
   */
  confermaModificaInteresseDiMora() {
    // Resetto l'alert
    this.resetAlertConfigs();
    // Lancio il submit del form, tramite referenza del componente
    this.interessiDiMoraFormComp?.onFormSubmit();
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
  modalConfirm(payload: IInteressiDiMoraFormDataModal) {
    // Close della modale
    this.activeModal.close(payload);
  }

  /**
   * Funzione che gestisce il submit della form per la modale che modifica il dato selezionato.
   * @param interessiDiMoraFormModal IInteressiLegaliFormDataModal con i dati della form.
   */
  onFormSubmitInteressiDiMora(data: IInteressiLegaliFormData) {
    //verifico l'input
    if (!data) {
      return;
    }

    // recupero l'oggetto interessiLegaliVo con i dati da aggiornare
    let interessiDiMoraVoUpdate: InteressiDiMoraVo = this.interessiDiMora;

    //faccio l'update dei dati modificati dall'utente che arrivano in input dalla submit
    // (giorni, percentuale, data inizio)
    interessiDiMoraVoUpdate.percentuale = data?.percentuale;
    interessiDiMoraVoUpdate.giorni_legali = data?.giorni; //non modificabile

    //chiamo il servizio per salvare i nuovi dati aggiornati
    this._interessiDiMoraService
      .updateInteressiDiMora(interessiDiMoraVoUpdate)
      .subscribe({
        next: (interessiDiMoraVoUpdate: InteressiDiMoraVo) => {
          const payload: IInteressiDiMoraFormDataModal = {
            interessiDiMora: interessiDiMoraVoUpdate,
            readOnly: this.readOnly,
          };
          this.modalConfirm(payload);
        },
        error: (e: RiscaServerError) => {
          this.onServiziError(e);
        }
      });
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che ritorna le informazioni convertite rispetto ad un oggetto InteressiDiMoraVo.
   * @returns IInteressiDiMoraFormData con i dati convertiti per la form.
   */
  get interessiDiMoraForm(): IInteressiDiMoraFormData {
    // Converto e ritorno le informazioni per il form
    return this.interessiDiMora?.toInteressiDiMoraFormData(
      this.dataModal?.interessiDiMora
    );
  }

  /**
   * Getter con il recupero dei dati dalla configurazione.
   * @returns InteressiLegaliVo come copia dell'oggetto input.
   */
  get interessiDiMora(): InteressiDiMoraVo {
    // Tento di recuperare e clonare le informazioni
    return cloneDeep(this.dataModal?.interessiDiMora);
  }

  /**
   * Getter con il recupero del flag di sola lettura.
   * @returns boolean con il flag di sola lettura.
   */
  get readOnly(): boolean {
    return this.dataModal?.readOnly;
  }

  /**
   * Getter che ritorna la modalità di gestione del componente.
   * @returns AppActions con la modalità di gestione.
   */
  get modalitaModifica(): AppActions {
    // Ritorno la modalita
    return AppActions.modifica;
  }
}
