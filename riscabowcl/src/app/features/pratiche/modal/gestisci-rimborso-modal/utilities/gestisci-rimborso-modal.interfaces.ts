import { AppActions } from 'src/app/shared/utilities';
import { RimborsoVo } from 'src/app/core/commons/vo/rimborso-vo';

/**
 * Interfaccia che definisce i parametri da passare alla modale.
 */
export interface IGestisciRimborsoConfigs {
  /** boolean che definisce se i gruppi sono attivi per questo operatore. */
  gruppiAbilitati: boolean;
  /** boolean che definisce se i soggetti sono attivi per questo operatore. */
  soggettiAbilitati: boolean;
  /** string che definisce il testo descrittivo della form da visualizzare. */
  description?: string;
  /** RimborsoVo con le informazioni per pre-popolare la form dati del rimborso. */
  rimborso?: RimborsoVo;
}
