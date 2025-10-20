import { IRiscaNavRouteParams } from '../../../../../shared/utilities';
import { AzioniConfigurazione } from './configurazione.enums';

/**
 * Interfaccia che definisce la mappatura dei parametri di route gestiti dal componente configurazione.
 */
export interface IConfigurazioneRouteParams
  extends IRiscaNavRouteParams<AzioniConfigurazione> {
  pageTarget?: AzioniConfigurazione;
}
