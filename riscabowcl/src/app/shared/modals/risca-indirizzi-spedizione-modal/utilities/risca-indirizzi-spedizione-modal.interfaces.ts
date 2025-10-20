import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { AppActions, RiscaGestioneISModal } from '../../../utilities';
import { IndirizzoSpedizioneConfig } from './risca-indirizzi-spedizione-modal.classes';

/**
 * Interfaccia di comodo, che definisce le configurazioni per impostare gli indirizzi di spedizione della modale.
 */
export interface IISComponentConfig {
  indirizziSpedizione?: IndirizzoSpedizioneVo[];
  modalita?: AppActions;
}

/**
 * Interfaccia che definisce le configurazioni dati per la gestione della modale risca-indirizzi-spedizione.
 * Le proprietà principali sono:
 * - indirizzoSpedizione;
 * - indirizziSpedizione;
 * Se entrambe le configurazioni sono definite, prevale la logica per il singolo oggetto.
 */
export interface IIndirizziSpedizioneModalConfig {
  /** Array di RecapitoVo che definisce i riferimenti per gli indirizzi di spedizione. */
  recapiti: RecapitoVo[];
  /** Array di IndirizzoSpedizioneVo che definisce la gestione di più dati. Differisce nelle logiche rispetto alla gestione del singolo oggetto. */
  indirizziSpedizione: IndirizzoSpedizioneVo[];
  /** Array di Gruppo contenente le informazioni di possibili gruppi del soggetto e collegati agli indirizzi di spedizione. */
  gruppi?: Gruppo[];
  /** AppActions che definisce la tipologia di gestione dei dati. */
  modalita?: AppActions;
  /** RiscaGestioneISModal che definisce la gestione delle informazioni all'interno della modale. */
  gestione: RiscaGestioneISModal;
  /** Boolean che permette di disabilitare il form tramite condizioni esterne al componante. */
  isFormDisabilitato?: boolean;
}

/**
 * Interfaccia che definisce le proprietà per la creazione della configurazione del componente da visualizzare per gli indirizzi di spedizione.
 */
export interface IIndirizzoSpedizioneConfig {
  indirizzoSpedizione?: IndirizzoSpedizioneVo;
  modalita: AppActions;
  parentFormKey: string;
  childFormKey: string;
}

/**
 * Interfaccia che definisce l'oggetto generato per l'init della modale che gestisce le informazioni per gli indirizzi di spedizione.
 */
export interface IISInitModalConfig {
  indirizziConfigs: IndirizzoSpedizioneConfig[];
  children: string[];
}
