//import { Ambito } from '..';

export interface Procedimento {
  preferito?: boolean;
  id_tipo_procedimento: number;
//  ambito: Ambito;
  des_tipo_procedimento: string;
  des_estesa_tipo_procedimento: string;
  ordinamento_tipo_procedimento?: number;
}
