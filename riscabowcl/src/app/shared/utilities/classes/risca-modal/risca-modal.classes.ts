import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { IApriModalConfigsForClass } from '../../interfaces/risca/risca-modal/risca-modal.interfaces';
import { ICallbackDataModal } from '../../interfaces/utilities.interfaces';
import { IParamsModalConfigs } from './risca-modal.interfaces';

/**
 * Interfaccia di configurazione per le modali tramite servizio risca-modal.service.ts
 */
export class ApriModalConfigs implements IApriModalConfigsForClass {
  /** Istanza del componente che si vuole aprire. Deve essere definito nelle entryComponents del modulo. */
  component: any;
  /** NgbModalOptions contenente le informazioni di configurazione per le opzioni del modale, se non ci sono informazioni, definire un oggetto vuoto. */
  options: NgbModalOptions;
  /** IParamsModalConfigs che deve riflettere i nomi delle @Input() del componente e i propri valori. */
  params?: IParamsModalConfigs;
  /** CallbackDataModal contenente la logica delle callback del modale. */
  callbacks?: ICallbackDataModal;

  constructor(c: IApriModalConfigsForClass) {
    this.component = c.component;
    this.options = c.options;
    this.params = c.params;
    this.callbacks = c.callbacks;
  }
}
