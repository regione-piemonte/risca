import { Observable } from 'rxjs';
import { RicercaPaginataResponse } from '../../../../../../core/classes/http-helper/http-helper.classes';
import { RiscossioneSearchResultVo } from '../../../../../../core/commons/vo/riscossione-search-result-vo';
import { StatoRiscossioneVo } from '../../../../../../core/commons/vo/stati-riscossione-vo';

/**
 * Interfaccia che definisce l'oggetto di richiesta per la gestione della ricerca pratiche avanzata.
 */
export interface IRicercaPraticaAvanzataReq {
  ricercaPratiche: Observable<
    RicercaPaginataResponse<RiscossioneSearchResultVo[]>
  >;
  statiRiscossione: Observable<StatoRiscossioneVo[]>;
}

/**
 * Interfaccia che definisce l'oggetto di risposta per la gestione della ricerca pratiche avanzata.
 */
export interface IRicercaPraticaAvanzataRes {
  ricercaPratiche: RicercaPaginataResponse<RiscossioneSearchResultVo[]>;
  statiRiscossione: StatoRiscossioneVo[];
}
