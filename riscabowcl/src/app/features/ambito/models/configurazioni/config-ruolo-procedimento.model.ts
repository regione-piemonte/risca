import { Procedimento, RuoloCompilante } from 'src/app/shared/models';

export interface ConfigRuoloProcedimento {
  tipo_procedimento: Procedimento;
  ruolo_compilante: RuoloCompilante;
  flg_modulo_delega: boolean;
  flg_modulo_procura: boolean;
  flg_popola_richiedente: boolean;
  flg_ruolo_default: boolean;
}
