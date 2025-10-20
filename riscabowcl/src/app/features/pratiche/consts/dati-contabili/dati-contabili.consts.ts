import { RiscaButtonConfig } from '../../../../shared/utilities';

/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente dati contabili.
 */
interface IDatiContabiliConsts {
  BTN_TORNA_A_STATI_DEBITORI: RiscaButtonConfig;
  BTN_STAMPA: RiscaButtonConfig;
  BTN_SALVA_MODIFICHE: RiscaButtonConfig;

  CERCA_SOGGETTO_MODAL_TITLE: string;
  DESCRIPTION_CERCA_SOGGETTO: string;
  LABEL_ANNULLA: string;
  LABEL_INSERISCI_SOGGETTO: string;
}

/**
 * Oggetto contenente una serie di costanti per il componente dati contabili.
 */
export const DatiContabiliConsts: IDatiContabiliConsts = {
  BTN_TORNA_A_STATI_DEBITORI: { label: 'TORNA A STATI DEBITORI' },
  BTN_STAMPA: { label: 'STAMPA' },
  BTN_SALVA_MODIFICHE: { label: 'SALVA MODIFICHE' },

  CERCA_SOGGETTO_MODAL_TITLE: 'Cerca soggetto',
  DESCRIPTION_CERCA_SOGGETTO: 'Indicare il codice fiscale del <strong>soggetto</strong>',
  LABEL_ANNULLA: 'ANNULLA',
  LABEL_INSERISCI_SOGGETTO: 'INSERISCI SOGGETTO',
};
