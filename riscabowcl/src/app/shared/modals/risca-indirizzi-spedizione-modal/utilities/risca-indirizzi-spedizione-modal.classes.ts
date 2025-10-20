import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { AppActions } from '../../../utilities';
import {
  IIndirizzoSpedizioneConfig,
  IISComponentConfig,
} from './risca-indirizzi-spedizione-modal.interfaces';

/**
 * Classe di comodo, che definisce le configurazioni per impostare gli indirizzi di spedizione della modale.
 */
export class ISComponentConfig {
  indirizziSpedizione: IndirizzoSpedizioneVo[];
  modalita: AppActions;

  constructor(c?: IISComponentConfig) {
    this.indirizziSpedizione = c?.indirizziSpedizione ?? [];
    this.modalita = c?.modalita ?? AppActions.inserimento;
  }
}

/**
 * Interfaccia che definisce le proprietà per la creazione della configurazione del componente da visualizzare per gli indirizzi di spedizione.
 */
export class IndirizzoSpedizioneConfig {
  indirizzoSpedizione?: IndirizzoSpedizioneVo;
  modalita: AppActions;
  parentFormKey: string;
  childFormKey: string;

  constructor(c: IIndirizzoSpedizioneConfig) {
    this.indirizzoSpedizione = c.indirizzoSpedizione;
    this.modalita = c.modalita;
    this.parentFormKey = c.parentFormKey;
    this.childFormKey = c.childFormKey;
  }
}
