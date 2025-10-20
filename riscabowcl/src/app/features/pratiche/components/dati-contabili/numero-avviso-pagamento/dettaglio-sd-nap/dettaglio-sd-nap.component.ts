import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { StatoDebitorioVo } from '../../../../../../core/commons/vo/stato-debitorio-vo';
import { RiscaUtilitiesComponent } from '../../../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaLockPraticaService } from '../../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { DettaglioSDNAPConsts } from './utilities/dettaglio-sd-nap.consts';

@Component({
  selector: 'dettaglio-sd-nap',
  templateUrl: './dettaglio-sd-nap.component.html',
  styleUrls: ['./dettaglio-sd-nap.component.scss'],
})
export class DettaglioSDNAPComponent
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Oggetto contenente una serie di costanti per la gestione del componente. */
  DSD_NAP_C = DettaglioSDNAPConsts;

  /** StatoDebitorioVo che identifica i dati dello stato debitorio in modifica/dettaglio. */
  @Input() statoDebitorio: StatoDebitorioVo;

  /** EventEmitter che richiede l'operazione di "indietro" rispetto al dettaglio dello stato debitorio nap. */
  @Output('onTornaAStatiDebitoriNap') onTornaASDNap = new EventEmitter<any>();

  /**
   * Costruttore
   */
  constructor(
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaLockP: RiscaLockPraticaService,
    riscaMessages: RiscaMessagesService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);

    // Lancio il setup del componente
    this.setupComponente();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup del componente.
   */
  private setupComponente() {
    // Funzioni di setup del componente
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Init
   */
  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  /**
   * Funzione di init del componente.
   */
  private initComponente() {
    // Funzioni di init del componente
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * #######################
   * PULSANTI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione collegata al pulsante indietro del dettaglio.
   */
  tornaAStatiDebitoriNAP() {
    // Emetto l'evento di "indietro"
    this.onTornaASDNap.emit(true);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.isPraticaLocked;
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
