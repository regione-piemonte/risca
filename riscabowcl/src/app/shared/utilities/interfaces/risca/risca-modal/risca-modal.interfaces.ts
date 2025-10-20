import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { ICallbackDataModal, DynamicObjAny } from '../../..';

/**
 * Interfaccia di configurazione per le modali tramite servizio risca-modal.service.ts
 */
export interface IApriModalConfigs {
  /** Istanza del componente che si vuole aprire. Deve essere definito nelle entryComponents del modulo. */
  component?: any;
  /** NgbModalOptions contenente le informazioni di configurazione per le opzioni del modale, se non ci sono informazioni, definire un oggetto vuoto. */
  options?: NgbModalOptions;
  /** DynamicObjAny che deve riflettere i nomi delle @Input() del componente e i propri valori. */
  params?: DynamicObjAny;
  /** CallbackDataModal contenente la logica delle callback del modale. */
  callbacks?: ICallbackDataModal;
}

/**
 * Interfaccia di configurazione per le modali tramite servizio risca-modal.service.ts
 */
export interface IApriModalConfigsForClass {
  /** Istanza del componente che si vuole aprire. Deve essere definito nelle entryComponents del modulo. */
  component: any;
  /** NgbModalOptions contenente le informazioni di configurazione per le opzioni del modale, se non ci sono informazioni, definire un oggetto vuoto. */
  options: NgbModalOptions;
  /** DynamicObjAny che deve riflettere i nomi delle @Input() del componente e i propri valori. */
  params?: DynamicObjAny;
  /** CallbackDataModal contenente la logica delle callback del modale. */
  callbacks?: ICallbackDataModal;
}

/**
 * Interfaccia di comodo che gestisce la validazione delle callback della modale.
 * Se la callback di confirm, close o cancel esistono, verranno valorizzati i relativi flag boolean a true.
 */
export interface ICheckCallbacksDataModal {
  confirm: boolean;
  close: boolean;
  cancel: boolean;
}
