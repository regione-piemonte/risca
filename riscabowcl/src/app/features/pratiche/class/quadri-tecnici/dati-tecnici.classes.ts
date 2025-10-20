import { Observable } from 'rxjs';
import { ComponenteDt } from '../../../../core/commons/vo/componente-dt-vo';

export class ComponentiDtConfigsReq {
  ricercaDt: Observable<ComponenteDt>;
  praticaInsDt: Observable<ComponenteDt>;
  praticaModDts: Observable<ComponenteDt[]>;
  // statiDebitoriInsDt: Observable<ComponenteDt>;
  // statiDebitoriModDts: Observable<ComponenteDt[]>;
}

export class ComponentiDtConfigsRes {
  ricercaDt: ComponenteDt;
  praticaInsDt: ComponenteDt;
  praticaModDts: ComponenteDt[];
  // statiDebitoriInsDt: ComponenteDt;
  // statiDebitoriModDts: ComponenteDt[];
}
