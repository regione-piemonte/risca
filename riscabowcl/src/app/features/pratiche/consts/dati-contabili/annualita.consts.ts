import { RiscaButtonConfig } from '../../../../shared/utilities';

/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente stati debitori.
 */
interface IAnnualitaConsts {
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
  /** Titolo della modale per annualità */
  ANNUALITA_MODAL_TITLE: string;
  /** Titolo della modale di inserimento annualità */
  ANNUALITA_MODAL_TITLE_NUOVA: string;
  /** Titolo della modale di modifica annualità */
  ANNUALITA_MODAL_TITLE_MODIFICA: string;
  /** Titolo della modale di visualizzazione stati debitori collegati */
  SD_COLLEGATI_MODAL_TITLE: string;

  /**
   * ######################
   * BOTTONI DI NAVIGAZIONE
   * ######################
   */

  LABEL_FLAG_RATEO_PRIMA_ANNUALITA: any;
  LABEL_DATA_SCADENZA_PAGAMENTO: string;
  LABEL_PORTATA_ASSEGNATA: string;
  LABEL_CANONE: string;
  LABEL_CALCOLA_CANONE_ANNUO: string;
  LABEL_NUMERO_MESI_ECCEDENTE: string;

  /**
   * #######################
   * BOTTONI FORM ANNUALITA'
   * #######################
   */
  LABEL_CONFERMA: string;
  LABEL_ANNULLA: string;

  /**
   * #######################################
   * CONFIGURAZIONE DI COMODO PER I PULSANTI
   * #######################################
   */
  BTN_MODAL_ANNULLA: RiscaButtonConfig;
  BTN_MODAL_INDIETRO: RiscaButtonConfig;
  BTN_MODAL_CONFERMA: RiscaButtonConfig;
}

/**
 * Oggetto contenente una serie di costanti per il componente dati contabili.
 */
export const AnnualitaConsts: IAnnualitaConsts = {
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
  ANNUALITA_MODAL_TITLE: 'Annualità',
  ANNUALITA_MODAL_TITLE_NUOVA: 'Aggiungi nuova annualità',
  ANNUALITA_MODAL_TITLE_MODIFICA: 'Dettaglio annualità',
  SD_COLLEGATI_MODAL_TITLE: 'Stati Debitori Collegati',

  /**
   * ######################
   * BOTTONI DI NAVIGAZIONE
   * ######################
   */
  LABEL_FLAG_RATEO_PRIMA_ANNUALITA: 'Rateo prima annualità',
  LABEL_DATA_SCADENZA_PAGAMENTO: 'Data inizio',
  LABEL_PORTATA_ASSEGNATA: 'Portata da assegnare',
  LABEL_CANONE: 'Calcola',
  LABEL_CALCOLA_CANONE_ANNUO: 'Calcola canone',
  LABEL_NUMERO_MESI_ECCEDENTE: 'Numero mesi',

  /**
   * #######################
   * BOTTONI FORM ANNUALITA'
   * #######################
   */
  LABEL_CONFERMA: 'CONFERMA',
  LABEL_ANNULLA: 'ANNULLA',

  /**
   * #######################################
   * CONFIGURAZIONE DI COMODO PER I PULSANTI
   * #######################################
   */
  BTN_MODAL_ANNULLA: { label: 'ANNULLA' },
  BTN_MODAL_INDIETRO: { label: 'INDIETRO' },
  BTN_MODAL_CONFERMA: { label: 'CONFERMA' },
};
