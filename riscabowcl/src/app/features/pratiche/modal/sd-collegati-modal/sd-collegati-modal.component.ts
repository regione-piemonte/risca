import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SDCollegatiVo } from 'src/app/core/commons/vo/sd-collegati-vo';
import { LoggerService } from 'src/app/core/services/logger.service';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { SDCollegatiTable } from 'src/app/shared/classes/risca-table/sd-collegati/sd-collegati.table';
import { RiscaFormParentComponent } from 'src/app/shared/components/risca/risca-form-parent/risca-form-parent.component';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from 'src/app/shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { IRiscaServerError } from 'src/app/shared/utilities';
import { RiscaErrorsMap } from 'src/app/shared/utilities/classes/errors-maps';
import { RimborsoVo } from '../../../../core/commons/vo/rimborso-vo';
import { AnnualitaConsts } from '../../consts/dati-contabili/annualita.consts';
import { PraticaVo } from './../../../../core/commons/vo/pratica-vo';
import { RimborsiService } from './../../service/rimborsi/rimborsi.service';

@Component({
  selector: 'app-sd-collegati-modal',
  templateUrl: './sd-collegati-modal.component.html',
  styleUrls: ['./sd-collegati-modal.component.scss'],
})
export class SDCollegatiModalComponent
  extends RiscaFormParentComponent
  implements OnInit
{
  /** Oggetto contenente una serie di costanti per la gestione degli errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  RA_C = AnnualitaConsts;

  /** Id del rimborso da cercare. */
  @Input() rimborso?: RimborsoVo;
  /** Pratica associata al rimborso */
  @Input() pratica?: PraticaVo;

  /** Titolo della modale. */
  modalTitle: string = this.RA_C.SD_COLLEGATI_MODAL_TITLE;

  /** SDCollegatiTable per la configurazione della tabella degli stati debitori collegati */
  sdCollegatiTable: SDCollegatiTable;

  /**
   * Costruttore
   */
  constructor(
    public _activeModal: NgbActiveModal,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    private _rimborsi: RimborsiService
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

  ngOnInit(): void {
    // Lancio l'init del componente
    this.initComponente();
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
    // Prendo gli stati debitori collegati dal rimborso
    this._rimborsi.getStatiDebitoriCollegati(this.idRimborso).subscribe({
      next: (sdCollegati: SDCollegatiVo[]) => {
        // Popolo la tabella
        this.sdCollegatiTable = new SDCollegatiTable({
          sdCollegati,
        });
      },
      error: (error: IRiscaServerError) => {
        // Gestisco il messaggio d'errore inaspettato
        this.onServiziError(error);
      },
    });
  }

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   * @param resource any che definisce le informazioni da ritornare per la chiusura del modal.
   */
  modalDismiss(resource?: any) {
    // Dismiss della modale
    this._activeModal.dismiss(resource);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che recupera l'id_rimborso dall'oggetto in input.
   */
  get idRimborso(): number {
    // Recupero l'id_rimborso
    return this.rimborso?.id_rimborso;
  }

  /**
   * Getter di comodo che recupera il check
   */
  get checkSdCollegatiTable(): boolean {
    // Verifico che il dato esista per gli stati debitori collegati e che siano presenti dati
    return this.sdCollegatiTable?.getDataSource()?.length > 0;
  }
}
