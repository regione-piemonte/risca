import { RegolaUsoVo } from '../../../../../core/commons/vo/regola-uso-vo';
import {
  ICallbackDataModal,
  RiscaTablePagination,
} from '../../../../../shared/utilities';
import { ICreaRegolaUsoModal } from '../../../modals/crea-regola-uso-modal/utilities/crea-regola-uso-modal.interfaces';
import { IRicercaUsiRegola } from '../../configurazioni/utilities/configurazioni.interfaces';
import { IDettaglioRegolaUsoModal } from '../../../modals/dettaglio-regola-uso-modal/utilities/dettaglio-regola-uso-modal.interfaces';

/**
 * Interfaccia che definisce i parametri da passare alla funzione per la conferma di salvataggio delle regole uso.
 */
export interface ISalvaModificheRegoleModal extends ICallbackDataModal {
  regoleModificate: RegolaUsoVo[];
}

/**
 * Interfaccia che definisce i parametri da passare alla modale di gestione per creare una nuova regola uso.
 */
export interface ICreaNuovaRegolaModal {
  callbacks: ICallbackDataModal;
  dataModal: ICreaRegolaUsoModal;
}

/**
 * Interfaccia che definisce i parametri da passare alla modale di gestione per visualizzare i dettagli di una regola uso.
 */
export interface IApriDettaglioRegolaUsoModal {
  callbacks: ICallbackDataModal;
  dataModal: IDettaglioRegolaUsoModal;
}

/**
 * Interfaccia che raccoglie le informazioni per effettuare la PUT dati per le regole uso modificate.
 */
export interface IPutListaRegoleParams {
  /** IRicercaUsiRegola con le informazioni di ricerca in path params. */
  pathParams: IRicercaUsiRegola;
  /** RiscaTablePagination con l'oggetto di ricerca per la paginazione. */
  paginazione?: RiscaTablePagination;
  /** RegolaUsoVo[] con la lista di regole modificata da salvare. */
  regoleModificate: RegolaUsoVo[];
}
