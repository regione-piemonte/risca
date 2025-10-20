import { ICommonParamsModal } from '../../interfaces/utilities.interfaces';

/**
 * Interfaccia di configurazione per i parametri delle modali tramite servizio risca-modal.service.ts
 */
export interface IParamsModalConfigs {
  /** ICommonParamsModal che definisce una specifica sezioni di parametri utilizzati automaticamente dalla componente di utility: risca-utility-modal.component.ts. */
  dataModal?: ICommonParamsModal;
  /** Per questo oggetto è possibile definire un qualunque parametro, che deve risultare poi mappato all'interno del componente della modale come variabile. */
  [key: string]: any;
}
