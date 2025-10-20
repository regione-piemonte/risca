import { TipoRicercaMorositaVo } from '../../../../../core/commons/vo/tipo-ricerca-morosita-vo';
import { IRiscaCheckboxData, IRiscaRadioData, IRiscaAnnoSelect } from '../../../../utilities';

/**
 * Interfaccia che rappresenta i filtri di ricerca generati dal form.
 */
export interface IFiltriRicercaMorosita {
  tipoRicercaMorosita: TipoRicercaMorositaVo;
  anno: IRiscaAnnoSelect;
  restituitoAlMittente: IRiscaCheckboxData;
  annullato: IRiscaCheckboxData;
  limiteInvioAccertamento: IRiscaRadioData;
}
