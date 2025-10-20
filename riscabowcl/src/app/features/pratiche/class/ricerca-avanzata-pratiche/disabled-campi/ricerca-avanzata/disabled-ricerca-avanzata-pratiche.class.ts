import { FormRicercaAvanzataPraticheConsts } from '../../../../consts/ricerca-avanzata-pratiche/form-ricerca-avanzata-pratiche.consts';
import { DisabledCampiRAPClass } from '../disabled-campi.ricerca-avanzata-pratiche.class';

/**
 * Classe comune per la gestione dell'accesso alla sezione: dati soggetto.
 */
export class DisabledRicercaAvanzataPraticheClass extends DisabledCampiRAPClass {
  /** Costante RiscaDatiSoggettoConsts contenente le costanti utilizzate dal componente. */
  protected FRAP_C = new FormRicercaAvanzataPraticheConsts();

  constructor() {
    super();
  }
}
