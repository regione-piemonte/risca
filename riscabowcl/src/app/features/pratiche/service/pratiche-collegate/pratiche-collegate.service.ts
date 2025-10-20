import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { GruppoEComponenti } from '../../../../shared/utilities';
import { IPraticheCollegateRouteParams } from '../../components/pratiche-collegate/utilities/pratiche-collegate.utilities';
import { PraticheCollegateConsts } from '../../consts/pratiche-collegate/pratiche-collegate.consts';

/**
 * Interfaccia che definisce i dati per il caricamento delle pratiche collegate.
 */
export interface ILoadPraticheCollegate {
  idSoggetto?: number;
  idGruppo?: number;
}

@Injectable({ providedIn: 'root' })
export class PraticheCollegateService {
  /**
   * Costruttore
   */
  constructor(
    private _navigationHelper: NavigationHelperService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * Funzione di supporto che genera le fonti dati per il download delle pratiche collegate.
   * Verranno recuperati gli id di soggetto o id gruppo dallo state, oppure dal servizio di navigazione.
   * @param router Router dalla quale recuperare le possibili informazioni all'interno dello state.
   * @returns ILoadPraticheCollegate con i dati per il download delle pratiche collegate.
   */
  getPraticheCollegateForLoad(router: Router): ILoadPraticheCollegate {
    // Tento di recuperare i dati per il download dallo state del router
    let state: IPraticheCollegateRouteParams;
    // Recupero lo state del routing
    state = this._riscaUtilities.getRouterState(router);
    // Tento di recuperare gli id dallo state
    const idSS = state?.soggetto?.id_soggetto;
    const idGS = state?.gruppo?.id_gruppo_soggetto;

    // Verifico se esistono i dati
    if (idSS != undefined || idGS != undefined) {
      // Ritorno i dati
      return { idSoggetto: idSS, idGruppo: idGS };
    }

    // Recupero l'id dello step per la possibile presenza in navigazione
    const idStep = PraticheCollegateConsts.NAVIGATION_CONFIG.componentId;
    // Recupero i dati dello step possibilmente salvati nella snapshot del journey
    const pcStep = this._navigationHelper.getStep(idStep);

    // Estraggo soggetto e gruppo dalla snapshot
    const { soggetto, gruppoEComponenti } = pcStep?.snapshot?.data || {};
    // Tipizzo i dati della snapshot
    const sS: SoggettoVo = soggetto;
    const gecS: GruppoEComponenti = gruppoEComponenti;

    // Tento di recuperare gli id dalla snapshot
    const idSSn = sS?.id_soggetto;
    const idGSn = gecS?.gruppo?.id_gruppo_soggetto;

    // Ritorno i dati
    return { idSoggetto: idSSn, idGruppo: idGSn };
  }
}
