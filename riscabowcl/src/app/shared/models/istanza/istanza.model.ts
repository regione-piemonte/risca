//import { Compilante } from '../compilante/compilante.model';
import { Procedimento } from '../procedimento/procedimento.model';
import { StatoIstanza } from './stato-istanza.model';

export interface Istanza {
  id_istanza?: number;
  gestUID?: string;
  stato_istanza?: StatoIstanza;
//  compilante?: Compilante;
  tipo_procedimento?: Procedimento;
  cod_istanza?: string;
  anno_istanza?: number;
  prog_anno?: number;
  cod_autorita_competente?: string;
  data_inserimento_istanza?: string;
  data_modifica_istanza?: string;
  json_data?: '{}';
}
