/**
 * Interfaccia che definisce i parametri da passare alla modale.
 */
export interface ICercaSoggettoDCModalConfigs {
  /** boolean che definisce se i gruppi sono attivi per questo operatore. */
  gruppiAbilitati: boolean;
  /** boolean che definisce se i soggetti sono attivi per questo operatore. */
  soggettiAbilitati: boolean;
  /** string che definisce la configurazione per il titolo della modale. */
  modalTitle?: string;
  /** string che definisce la configurazione per il tasto: ANNULLA. */
  labelBtnAnnulla?: string;
  /** string che definisce la configurazione per il tasto: INSERISCI. */
  labelBtnConferma?: string;
  /** string che definisce il testo descrittivo della form da visualizzare. */
  description?: string;
}

/**
 * Interfaccia che definisce i parametri per le label della modale ICercaSoggettoDCModalConfigs.
 */
export interface ICercaSoggettoCDModalLabels {
  /** string che definisce la configurazione per il titolo della modale. */
  modalTitle?: string;
  /** string che definisce la configurazione per il tasto: ANNULLA. */
  labelBtnAnnulla?: string;
  /** string che definisce la configurazione per il tasto: INSERISCI. */
  labelBtnConferma?: string;
  /** string che definisce il testo descrittivo della form da visualizzare. */
  description?: string;
}
