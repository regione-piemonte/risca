import { PraticaEDatiTecnici } from '../../../../core/commons/vo/pratica-vo';
import {
  IJourneySnapshot,
  IJStepConfig,
} from '../../../../core/interfaces/navigation-helper/navigation-helper.interfaces';
import { RiscaAzioniPratica } from '../../../../shared/utilities';

/**
 * Interfaccia che definisce le informazioni per l'apertura di una pratica in modifica.
 */
export interface IRModificaPratica {
  /** L'id della riscossione. */
  idRiscossione?: number;
  /** Il nav che sta richiedendo la modifica della pratica. */
  currentNav: RiscaAzioniPratica;
  /** La configurazione base dello step (componente). */
  stepConfig: IJStepConfig;
  /** La configurazione per lo snapshot dello step. */
  snapshotConfigs: IJourneySnapshot;
  /** Il dato della pratica e dei dati tecnici. */
  pedt?: PraticaEDatiTecnici;
}
