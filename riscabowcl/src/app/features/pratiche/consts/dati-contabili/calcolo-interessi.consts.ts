import { RiscaButtonConfig } from '../../../../shared/utilities';

/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente stati debitori.
 */
interface ICalcoloInteressiConsts {
  /**
   * ############################
   * CHIAVI PER PARENT/CHILD FORM
   * ############################
   */
  FORM_KEY_PARENT: string;
  FORM_KEY_CHILD_DTSD: string;

  /**
   * ######
   * PAGINA
   * ######
   */

  /** Titolo della modale di inserimento annualità */
  // TITOLO DELLA MODALE
  TITOLO_CALCOLO_INTERESSI: string;

  /**
   * ######################
   * BOTTONI DI NAVIGAZIONE
   * ######################
   */

  /**
   * #######################
   * BOTTONI FORM ANNUALITA'
   * #######################
   */
  LABEL_CHIUDI: string;
  LABEL_ANNULLA: string;

  /**
   * #######################################
   * CONFIGURAZIONE DI COMODO PER I PULSANTI
   * #######################################
   */
  BTN_MODAL_ANNULLA: RiscaButtonConfig;
  BTN_MODAL_CHIUDI: RiscaButtonConfig;
}

/**
 * Oggetto contenente una serie di costanti per il componente dati contabili.
 */
export const CalcoloCanoneConsts: ICalcoloInteressiConsts = {
  /**
   * ############################
   * CHIAVI PER PARENT/CHILD FORM
   * ############################
   */
  FORM_KEY_PARENT: 'FORM_KEY_PARENT',
  FORM_KEY_CHILD_DTSD: 'FORM_KEY_CHILD_DTSD',

  /**
   * ######
   * PAGINA
   * ######
   */
  // TITOLO DELLA MODALE
  TITOLO_CALCOLO_INTERESSI: 'Calcolo Interessi',

  /**
   * ######################
   * BOTTONI DI NAVIGAZIONE
   * ######################
   */

  /**
   * #######################
   * BOTTONI FORM ANNUALITA'
   * #######################
   */
  LABEL_ANNULLA: 'ANNULLA',
  LABEL_CHIUDI: 'CHIUDI',

  /**
   * #######################################
   * CONFIGURAZIONE DI COMODO PER I PULSANTI
   * #######################################
   */
  BTN_MODAL_ANNULLA: { label: 'ANNULLA' },
  BTN_MODAL_CHIUDI: { label: 'Chiudi' },
};
