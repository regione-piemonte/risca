import { TipoRicercaRimborsoVo } from "../../../../../core/commons/vo/tipo-ricerca-rimborso-vo";
import { IRiscaAnnoSelect } from "../../../../utilities";

/**
 * Interfaccia che rappresenta i filtri di ricerca generati dal form.
 */
export interface IFiltriRicercaRimborsi {
  tipoRicercaRimborsi: TipoRicercaRimborsoVo;
  anno: IRiscaAnnoSelect;
}
