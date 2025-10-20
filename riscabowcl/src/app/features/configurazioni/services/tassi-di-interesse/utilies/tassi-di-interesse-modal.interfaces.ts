import { ICallbackDataModal } from '../../../../../shared/utilities';
import { IInteressiLegaliFormDataModal} from '../../../modals/interessi-legali-modal/utilities/interessi-legali-modal.inteface';
import {IInteressiDiMoraFormDataModal} from '../../../modals/interessi-di-mora-modal/utilities/interessi-di-mora-modal.interface';

/**
 * Interfaccia che definisce i parametri da passare alla modale di gestione 
 * per visualizzare i dettagli dei dati per interessi legali ed poterli modificare
 */
export interface IApriDettaglioInteressiLegaliModal {
  callbacks: ICallbackDataModal;
  dataModal: IInteressiLegaliFormDataModal;
}

/**
 * Interfaccia che definisce i parametri da passare alla modale di gestione
 *  per visualizzare i dettagli dei dati per interessi di mora e poterli modificare
 */
export interface IApriDettaglioInteressiMoraModal {
  callbacks: ICallbackDataModal;
  dataModal: IInteressiDiMoraFormDataModal;
}

