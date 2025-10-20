import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IJourneySnapshot } from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { ParametriDilazioneConsts } from './utilities/parametri-dilazione.consts';
import { IParametriDilazioneParams } from './utilities/parametri-dilazione.interfaces';

@Component({
  selector: 'parametri-dilazione',
  templateUrl: './parametri-dilazione.component.html',
  styleUrls: ['./parametri-dilazione.component.scss'],
})
export class ParametriDilazioneComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti del componente di riferimento. */
  PD_C = new ParametriDilazioneConsts();

  /**
   * Costruttore.
   */
  constructor(
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlertService: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessageService: RiscaMessagesService,
    private _router: Router
  ) {
    super(
      logger,
      navigationHelper,
      riscaAlertService,
      riscaFormSubmitHandler,
      riscaMessageService,
    );
    this.stepConfig = this.PD_C.NAVIGATION_CONFIG;
  }

  ngOnInit() {
    // Richiamo l'init per i dati possibilmante salvati come snapshot
    this.initJSnapshot();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * Funzione di init per i dati della snapshot salvata in journey.
   */
  private initJSnapshot() {
    // // Recupero i parametri dallo state della route
    // const state = this._navigationHelper.getRouterState(this._router);
    // Carico i dati della snapshot
    this.loadJSnapshot(this);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che restitusce un oggetto IJourneySnapshot per generare una snapshot del componente.
   * @returns IJourneySnapshot con la configurazione del componente.
   */
  get snapshotConfigs(): IJourneySnapshot {
    return {
      source: this,
      mapping: this.PD_C.SNAPSHOT_CONFIG.mapping,
      saveFunc: this.PD_C.SNAPSHOT_CONFIG.saveFunc,
    };
  }
}
