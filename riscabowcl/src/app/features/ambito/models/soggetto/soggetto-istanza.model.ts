import { Istanza, RuoloCompilante } from 'src/app/shared/models';
import { RuoloSoggetto } from './ruolo-soggetto.model';
import { Soggetto } from './soggetto.model';

export interface SoggettoIstanza extends Soggetto {
  gestUID?: string;
  id_soggetto_istanza?: number;
  id_soggetto_padre?: number;
  soggetto: Soggetto;
  istanza: Istanza;
  ruolo_soggetto?: RuoloSoggetto;
  ruolo_compilante: RuoloCompilante;
}
