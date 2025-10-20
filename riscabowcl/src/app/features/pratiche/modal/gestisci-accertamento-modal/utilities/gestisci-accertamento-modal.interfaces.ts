import { AccertamentoVo } from './../../../../../core/commons/vo/accertamento-vo';

/**
 * Interfaccia che definisce i parametri da passare alla modale.
 */
export interface IGestisciAccertamentoConfigs {
  /** boolean che definisce se i gruppi sono attivi per questo operatore. */
  gruppiAbilitati: boolean;
  /** boolean che definisce se i soggetti sono attivi per questo operatore. */
  soggettiAbilitati: boolean;
  /** string che definisce il testo descrittivo della form da visualizzare. */
  description?: string;
  /** RimborsoVo con le informazioni per pre-popolare la form dati dell'accertamento. */
  accertamento?: AccertamentoVo;
}
