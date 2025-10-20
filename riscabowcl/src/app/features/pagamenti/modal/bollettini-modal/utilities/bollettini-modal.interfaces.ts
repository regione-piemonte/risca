import { ElaborazioneVo } from '../../../../../core/commons/vo/elaborazione-vo';
import { TipoElaborazioneVo } from '../../../../../core/commons/vo/tipo-elaborazione-vo';
import { NPModalitaModale } from '../../../component/bollettini/utilities/nuova-prenotazione.enums';

export interface IBollettiniModalDataConfigs {
  modalita: NPModalitaModale;
  elaborazione?: ElaborazioneVo;
  tipoElaborazione?: TipoElaborazioneVo;
}
