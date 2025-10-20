import { Procedimento } from '../procedimento/procedimento.model';
import { Quadro } from './quadro.model';

export interface Template {
  id_template: number;
  tipo_procedimento?: Procedimento;
  cod_template?: string;
  des_template?: string;
  data_inizio_validita?: Date;
  data_cessazione?: Date;
  pdf_template?: string;
  json_configura_template?: any;
  quadri?: Quadro[];
}
